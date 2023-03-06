package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();			//정렬
	private com.anbtech.mm.db.mfgModifyDAO mfgDAO = null;	
	private com.anbtech.mm.business.mrpInputBO mrpBO = null;
	private com.anbtech.mm.db.mrpModifyDAO mrpDAO = null;
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;	
	private String query = "";

	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mfgInputBO(Connection con) 
	{
		this.con = con;
		mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
		mrpBO = new com.anbtech.mm.business.mrpInputBO(con);
		mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		상태관리하기 
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  MFG MASTER에 상태관리하기
	//*******************************************************************/	
	public String setMfgStatus(String login_id,String login_name,String pid,String mrp_no,String mfg_no,
		String item_code,String item_unit,String fg_code,String mfg_count,String order_type,
		String order_status,String order_start_date,String order_end_date,String factory_no,
		String factory_name,String re_work) throws Exception
	{
		String data="",where="",update="";
		String date = anbdt.getDateNoformat();
		
		//공정생성, 작지작성상태, 출고부품소요량편집상태
		if(order_status.equals("2")) {
			//MFG ITEM에 소요량 일괄입력하기
			insertMfgItem(pid,mrp_no,mfg_no,item_code,order_start_date,factory_no,order_type,fg_code,mfg_count,re_work);

			//MFG OPERATOR 공정별 공정기본내용 일괄입력하기
			insertMfgOperator(pid,mrp_no,mfg_no,mfg_count,item_unit,order_start_date,order_end_date,factory_no,factory_name,order_type,fg_code,item_code,re_work);

			//진행상태 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "제조계획 공정이 생성되었습니다.";
		} 
		//작지확정상태, 출고부품예약상태,
		else if(order_status.equals("3")) {
			//부품 가용성 검사
			data = checkPuStatus(order_type,mrp_no,mfg_no,factory_no,item_code);
			if(!data.equals("P")) return data;

			//예약 진행하기 (생산관리:mfg_inout_master, 재고관리:st_item_stock_master)
			//pid는 mfg_master의 pid임
			reserveMfgItem(pid,mfg_no,item_code,factory_no,factory_name);

			//제조오더 확정 : 마스터
			update = "UPDATE mfg_master SET order_date='"+date+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//제조오더 확정 : 부품소요량
			update = "UPDATE mfg_item SET order_date='"+date+"' where gid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//제조오더 확정 : 작업지시서
			update = "UPDATE mfg_operator SET order_date='"+date+"',op_order='1' where gid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//진행상태 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//MPS의 진행상태 알려주기
			if(mrp_no.length() !=0) {
				where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
				String mps_no = mfgDAO.getColumData("MRP_MASTER","mps_no",where);
				update = "UPDATE mps_master SET mps_status='6' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				mfgDAO.executeUpdate(update);
			}
			data = "제조계획이 확정되었습니다."; 
		} 
		//부품출고의뢰요청상태
		else if(order_status.equals("4")) { 
			//부품출고의뢰하기
			data = saveBatchDelivery(pid,login_id,login_name,mfg_no,item_code,factory_no,factory_name);
			if(!data.equals("P")) return data;

			//진행상태 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "부품출고가 의뢰되었습니다.";
		} 
		//재공품 등록상태
		else if(order_status.equals("5")) {
			//진행상태 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "생산실적이 등록되었습니다.";
		} 
		//생산실적 마감 상태
		else if(order_status.equals("6")) {
			//진행상태 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "생산실적이 마감되었습니다.";
		}
		
		return data;
	}

	//*******************************************************************
	//  구매진행상태 검사하기
	//  Return : P [Pass] , else [에러문구]
	//*******************************************************************/	
	private String checkPuStatus(String order_type,String mrp_no,String mfg_no,
		String factory_no,String item_code) throws Exception
	{
		String data="P",where="",pu_req_no="",pu_status="",part="";
		String stock_quantity="";		//재고관리의 현재고 수량
		int dif_cnt = 0;				//긴급오더시 예약수량이 재고수량보다 크면.
		
		//긴급오더 제조생산일때 : 재고수량으로 
		if(order_type.equals("MANUAL")) {
			//item배열에 내용 담기
			//0:item_code,1:item_name,2:item_spec,3:item_unit
			//4:item_type,5:reserve_count,6:factory_no
			arrayMfgItemUniqueList(factory_no,mfg_no,item_code);
			for(int i=0; i<an; i++) {
				//현재고가 예약가능한지 판단하기
				where = "where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
				stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
				if(stock_quantity.length() == 0) stock_quantity = "0";
			
				//예약가능한 재고수량이 있는지 판단하기
				if(Integer.parseInt(stock_quantity) < Integer.parseInt(item[i][5])) {
					part += item[i][0]+"("+stock_quantity+":"+item[i][5]+"), ";
					dif_cnt++; 
				}
			}
			if(dif_cnt != 0) data = "재고관리의 재고수량이 충분하지 않은 품목["+part+"]이 있습니다.";
		}
		//MRS오더 제조생산일때 : 구매입고후 수입검사확정으로
		else {
			//구매의뢰번호 찾기
			where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
			pu_req_no = mfgDAO.getColumData("MRP_MASTER","pu_req_no",where);

			//구매입고상태 파악
			//where = "where request_no like '%"+pu_req_no+"%'";
			//pu_status = mfgDAO.getColumData("PU_ENTERED_ITEM","process_stat",where);
			com.anbtech.pu.db.PurchaseMgrDAO pmDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
			pu_status = pmDAO.getMaxStatForEnterItemByRequestNo(pu_req_no);

			//판단하기
			if(pu_status.equals("S25")) data = "P"; //Pass
			else if(pu_status.equals("S21")) data = "구매입고 상태입니다";
			else data = "구매발주의뢰 또는 구매발주진행 상태입니다.";
		}

		return data;
	}

	//*******************************************************************
	//  일괄부품출고의뢰 진행하기
	//*******************************************************************/	
	private String saveBatchDelivery(String gid,String login_id,String login_name,String mfg_no,String item_code,
		String factory_no,String factory_name) throws Exception
	{
		String data="P",where="",update="",input="",delivery_no="";

		//조건 : 일괄부품출고의뢰 가능한지 상태 검사하기
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		String order_status = mfgDAO.getColumData("MFG_MASTER","order_status",where);
		String order_type = mfgDAO.getColumData("MFG_MASTER","order_type",where);
		if(!order_status.equals("3")) {
			data = "부품예약 상태에서만 가능합니다.";
			return data;
		}

		//입력1 : 예약수량을 출고의뢰수량으로 update하기
		//0:pid,1:reserve_count
		arrayMfgItemList(factory_no,mfg_no,item_code);		//공정별 리스트
		for(int i=0; i<an; i++) {
			update = "UPDATE mfg_item SET request_count='"+item[i][1]+"' where pid='"+item[i][0]+"'";
			//System.out.println("update : " + update);
			mfgDAO.executeUpdate(update);
		}

		//입력2 : 부품출고의뢰서 입력하기 (재고관리 : ST_RESERVER_ITEM_INFO)
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:factory_no
		//단, 출고예약수량이 0보다 클경우만 저장됨.
		
		//부품출고의뢰번호 생성 (공장구분없이 생성됨)
		if(order_type.equals("MRP")) delivery_no = mfgDAO.getDeliveryNo();	//생산계획
		else delivery_no = mfgDAO.getDeliveryManNo();	//긴급오더

		arrayMfgItemUniqueList(factory_no,mfg_no,item_code); //부품별 리스트
		String[] div_info = new String[2];
		div_info = mfgDAO.getDivInfo(login_id);		//0:부서코드, 1:부서명
		for(int i=0; i<an; i++) {
			if(Integer.parseInt(item[i][5]) > 0) {
				input = "INSERT INTO st_reserved_item_info (delivery_no,item_code,item_name,item_desc,item_type,";
				input += "request_quantity,request_unit,request_date,factory_code,factory_name,ref_no,";
				input += "requestor_div_code,requestor_div_name,requestor_id,requestor_info,process_stat) values('";
				input += delivery_no+"','"+item[i][0]+"','"+item[i][1]+"','"+item[i][2]+"','"+item[i][4]+"','";
				input += item[i][5]+"','"+item[i][3]+"','"+anbdt.getDateNoformat()+"','"+factory_no+"','"+factory_name+"','";
				input += mfg_no+"','"+div_info[0]+"','"+div_info[1]+"','"+login_id+"','"+login_name+"','"+"S53"+"')";
				//System.out.println("input : " + input);
				mfgDAO.executeUpdate(input);
			}
		}

		//입력3 : 부품출고관리 마스터에 입력하기 (MFG_REQ_MASTER)
		where = "where item_no='"+item_code+"'";
		String item_spec = mfgDAO.getColumData("item_master","item_desc",where);
		String pid = anbdt.getID();

		input = "INSERT INTO mfg_req_master (pid,gid,mfg_no,mfg_req_no,assy_code,assy_spec,";
		input += "level_no,req_status,req_date,req_div_code,req_div_name,req_user_id,req_user_name,";
		input += "factory_no,factory_name) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+delivery_no+"','"+item_code+"','";
		input += item_spec+"','"+""+"','"+"2"+"','"+anbdt.getDateNoformat()+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+login_id+"','"+login_name+"','"+factory_no+"','"+factory_name+"')";
		//System.out.println("input : " + input);
		mfgDAO.executeUpdate(input);

		//입력4 : MFG_MASTER에 상태 바꿔주기
		update = "UPDATE mfg_master SET order_status='4' where pid='"+gid+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

		//입력5 : MFG_OPERATOR에 상태 바꿔주기
		update = "UPDATE mfg_operator SET op_order='2' where gid='"+gid+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		제조오더 에 관한 메소드 정의
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MFG MASTER에 데이터 입력하기
	//*******************************************************************/	
	public String insertMfg(String mrp_no,String model_code,String model_name,String fg_code,String item_code,
		String item_name,String item_spec,String item_unit,String mfg_count,String buy_type,String factory_no,
		String factory_name,String comp_code,String comp_name,String comp_user,String comp_tel,
		String order_type,String reg_date,String reg_id,String reg_name,String plan_date,String order_start_date,
		String order_end_date,String re_work,String link_mfg_no) throws Exception
	{
		String input="",update="",data="",where="",gid="",mfg_no="";
		String mrp_status="",bom_status="";

		//긴급등록을 고려하여 BOM구성된 FG인지, 해당BOM내에 있는 생산품목코드인지 검사한다.
		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) {
			data = "BOM구성이 안된 모델입니다. 먼저 BOM을 구성후 진행하십시오.";
			return data;
		}

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "품목코드는 FG코드로 구성된 BOM내의 제품 또는 반제품만 등록할 수 있습니다.";
			return data;
		}
		
		//MFG관리번호 구하기
		mfg_no = mfgDAO.getMfgNo(factory_no);

		//MFG MASTER에 입력하기 
		String pid = anbdt.getID();
		input = "INSERT INTO mfg_master (pid,mrp_no,mfg_no,model_code,model_name,fg_code,item_code,item_name,";
		input += "item_spec,item_unit,mfg_count,buy_type,factory_no,factory_name,comp_code,comp_name,";
		input += "comp_user,comp_tel,order_status,order_type,reg_date,reg_id,reg_name,plan_date,";
		input += "order_start_date,order_end_date,op_start_date,op_end_date,order_date,re_work,link_mfg_no) values('";
		input += pid+"','"+mrp_no+"','"+mfg_no+"','"+model_code+"','"+model_name+"','"+fg_code+"','";
		input += item_code+"','"+item_name+"','"+item_spec+"','"+item_unit+"','"+mfg_count+"','"+buy_type+"','";
		input += factory_no+"','"+factory_name+"','"+comp_code+"','"+comp_name+"','"+comp_user+"','"+comp_tel+"','";
		input += "1"+"','"+order_type+"','"+reg_date+"','"+reg_id+"','"+reg_name+"','"+plan_date+"','";
		input += order_start_date+"','"+order_end_date+"','"+""+"','"+""+"','"+""+"','"+re_work+"','"+link_mfg_no+"')";
		//System.out.println("input : " + input);
		mfgDAO.executeUpdate(input);

		//MRP MASTER의 진행상태 바꿔주기
		update = "UPDATE mrp_master SET mfg_order='1' where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
		mfgDAO.executeUpdate(update);
		
		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  MFG MASTER에 데이터 수정하기
	//*******************************************************************/	
	public String updateMfg(String pid,String plan_date,String model_code,String model_name,String fg_code,
		String item_code,String item_name,String item_spec,String item_unit,String mfg_count,String buy_type,
		String factory_no,String factory_name,String comp_code,String comp_name,String comp_user,String comp_tel,
		String order_type,String reg_date,String order_start_date,String order_end_date,String re_work,String link_mfg_no) throws Exception
	{
		String data="",where="",order_status="",update="";

		//진행상태가 작성중일때만 수정이 가능함
		where = "where pid='"+pid+"'";
		order_status = mfgDAO.getColumData("MFG_MASTER","order_status",where);
		if(!order_status.equals("1")) {
			data = "작성중일때만 수정이 가능합니다.";
			return data;
		}
		
		//MFG MASTER에 수정하기
		if(plan_date.length() !=0) {		//MRP에의한 접수 수정 
			update = "UPDATE mfg_master SET comp_code='"+comp_code+"',comp_name='"+comp_name;
			update += "',comp_user='"+comp_user+"',comp_tel='"+comp_tel+"',reg_date='"+reg_date;
			update += "',order_start_date='"+order_start_date+"',order_end_date='"+order_end_date;
			update += "',buy_type='"+buy_type+"',re_work='"+re_work+"',link_mfg_no='"+link_mfg_no;
			update += "' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);
		} else {							//긴급오더 수정
			update = "UPDATE mfg_master SET model_code='"+model_code+"',model_name='"+model_name;
			update += "',fg_code='"+fg_code+"',item_code='"+item_code+"',item_name='"+item_name;
			update += "',item_spec='"+item_spec+"',item_unit='"+item_unit+"',mfg_count='"+mfg_count;
			update += "',buy_type='"+buy_type+"',factory_no='"+factory_no+"',factory_name='"+factory_name;
			update += "',comp_code='"+comp_code+"',comp_name='"+comp_name+"',comp_user='"+comp_user;
			update += "',comp_tel='"+comp_tel+"',order_type='"+order_type+"',reg_date='"+reg_date;
			update += "',order_start_date='"+order_start_date+"',order_end_date='"+order_end_date;
			update += "',re_work='"+re_work+"',link_mfg_no='"+link_mfg_no+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);
		}
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// Mfg MASTER에 데이터 삭제하기
	//*******************************************************************/	
	public String deleteMfg(String pid,String mrp_no,String order_status) throws Exception
	{
		String delete = "",update="",data="";
		
		//진행상태 검사
		if(!order_status.equals("1")) {
			data = "작성상태일때만 삭제가 가능합니다.";
			return data;
		}
		
		//메뉴얼 입력만 삭제가능
		if(mrp_no.length() != 0) {
			data = "긴급오더만 삭제가 가능합니다.";
			return data;
		}

		//MFG MASTER삭제하기
		delete = "DELETE FROM mfg_master WHERE pid='"+pid+"'";
		mfgDAO.executeUpdate(delete);

		data = "정상적으로 삭제되었습니다.";
		return data;
	}
	//--------------------------------------------------------------------
	//
	//		제조오더 마스터(MFG OPERATOR) 에 관한 메소드 정의
	//		
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 공정별 공정내용 일괄입력하기
	// [MFG MASTER등록시 동시에 진행됨]
	//*******************************************************************/	
	private void insertMfgOperator(String gid,String mrp_no,String mfg_no,String mfg_count,String mfg_unit,
		String order_start_date,String order_end_date,String factory_no,String factory_name,String order_type,
		String fg_code,String item_code,String re_work) throws Exception
	{
		String pid = "",input="",where="",assy_spec="",op_no="";

		//Array list로 데이터 읽기
		ArrayList item_list = new ArrayList();
		//MRP NO가 있는경우 : MRP접수 
		if(mrp_no.length() != 0) {
			item_list = mfgDAO.getAssyOpList(factory_no,mrp_no,order_type);
		} 
		//긴급오더로 설계변경을 고려하여 mbom_item에서 데이터를 구한다.
		else {	
			if(re_work.equals("재작업")) { 
				item_list = mfgDAO.getMbomAssyOp(fg_code,item_code);	//1개의 ASSY코드만
			} else {
				if(fg_code.equals(item_code)) item_list = mfgDAO.getMbomAssyOpList(fg_code);	//전체시
				else item_list = getMbomAssyOpList(fg_code,item_code,order_start_date);			//하부구조시
			
			}
		}

		//저장하기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			//규격,op_no 구하기
			where = "where item_no='"+table.getAssyCode()+"'";
			assy_spec = mfgDAO.getColumData("ITEM_MASTER","item_desc",where);
			op_no = mfgDAO.getColumData("ITEM_MASTER","op_code",where);

			pid = anbdt.getNumID(n);
			//저장하기
			input = "INSERT INTO mfg_operator (pid,gid,mfg_no,assy_code,assy_spec,level_no,mfg_count,mfg_unit,";
			input += "op_start_date,op_end_date,order_date,buy_type,factory_no,factory_name,work_no,work_name,";
			input += "op_no,op_name,mfg_id,mfg_name,note,comp_code,comp_name,comp_user,comp_tel) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+table.getAssyCode()+"','"+assy_spec+"','";
			input += table.getLevelNo()+"','"+mfg_count+"','"+mfg_unit+"','";
			input += order_start_date+"','"+order_end_date+"','"+""+"','"+""+"','";
			input += factory_no+"','"+factory_name+"','"+""+"','"+""+"','";
			input += op_no+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	//  MFG OPERATOR에 데이터 수정하기
	//*******************************************************************/	
	public String updateMfgOperator(String pid,String op_start_date,String op_end_date,String buy_type,
		String work_no,String work_name,String op_no,String op_name,String mfg_id,String mfg_name,
		String note,String comp_code,String comp_name,String comp_user,String comp_tel) throws Exception
	{
		String data="",where="",order_status="",update="";

		//MFG OPERATOR에 수정하기
		update = "UPDATE mfg_operator SET op_start_date='"+op_start_date+"',op_end_date='"+op_end_date;
		update += "',buy_type='"+buy_type+"',work_no='"+work_no+"',work_name='"+work_name;
		update += "',op_no='"+op_no+"',op_name='"+op_name+"',mfg_id='"+mfg_id;
		update += "',mfg_name='"+mfg_name+"',note='"+note;
		update += "',comp_code='"+comp_code+"',comp_name='"+comp_name+"',comp_user='"+comp_user;
		update += "',comp_tel='"+comp_tel+"' where pid='"+pid+"'";
		mfgDAO.executeUpdate(update);
		
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// 기준공정이하의 공정 ASSY코드구하기
	//*******************************************************************/	
	public ArrayList getMbomAssyOpList(String fg_code,String parent_code,String sel_date) throws Exception
	{

		String where="",gid="",level_no="";

		//관련정보 구하기
		where = "where fg_code='"+fg_code+"'";
		gid = mfgDAO.getColumData("MBOM_MASTER","pid",where);

		where = "where gid='"+gid+"' and parent_code='"+parent_code+"'";
		level_no = mfgDAO.getColumData("MBOM_ITEM","level_no",where);

		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMbomItemList(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) item[i][j]="";

		//배열에 담기
		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			item[an][0] = table.getParentCode();
			item[an][1] = table.getLevelNo();
			an++;
		}

		//정렬
		sort.bubbleSortStringMultiAsc(item,1);

		//Array list에 담기
		ArrayList assy_list = new ArrayList();
		mrpItemTable assy = null;
		for(int i=0; i<an-1; i++) {
			if(!item[i][1].equals(item[i+1][1])) {
				assy = new mrpItemTable();	
				assy.setMrpNo("");
				assy.setAssyCode(item[i][0]);
				assy.setLevelNo(Integer.parseInt(item[i][1]));
				//System.out.println(item[i][0]+":"+item[i][1]); 
				assy_list.add(assy);
			}
		}
		assy = new mrpItemTable();	
		assy.setMrpNo("");
		assy.setAssyCode(item[an-1][0]);
		assy.setLevelNo(Integer.parseInt(item[an-1][1]));
		//System.out.println(item[an-1][0]+":"+item[an-1][1]); 
		assy_list.add(assy);
		

		return assy_list;
	}

	//--------------------------------------------------------------------
	//
	//		품목소요량관리(MFG ITEM) 에 관한 메소드 정의
	//		
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MFG접수시 MFG ITEM에 소요량 일괄입력하기
	// [MFG MASTER등록시 동시에 진행됨]
	//*******************************************************************/	
	private void insertMfgItem(String gid,String mrp_no,String mfg_no,String item_code,
		String order_start_date,String factory_no,String order_type,String fg_code,
		String mrp_count,String re_work) throws Exception
	{
		String input = "",pid = "",level_no="",where="",spare_count="0",reserve_count="",add_count="0";

		//level_no 구하기
		where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"' and assy_code='"+item_code+"'";
		level_no = mfgDAO.getColumData("MRP_ITEM","level_no",where);	

		//MRP ITEM의 부품정보를 가져온다.
		ArrayList item_list = new ArrayList();
		//MRP NO가 있는경우 : MRP접수 
		if(mrp_no.length() != 0) {
			item_list = mfgDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,order_type);	
		} 
		//긴급오더로 설계변경을 고려하여 mbom_item에서 데이터를 구한다.
		else {		
			if(re_work.equals("작업")) {		//다단계
				item_list = mrpBO.sortArrayStrList(fg_code,item_code,order_start_date,mrp_count,"0",factory_no);
			} else {							//단단계 (재작업으로 해당공정만 출력한다.)
				item_list = mrpBO.sortArraySingleStrList(fg_code,item_code,order_start_date,mrp_count,"0",factory_no);
			}
		}

		//테이블에 저장하기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			pid = anbdt.getNumID(n);

			//생산잔고Table에서 해당품목의 잔고수량가져오기 : 2004.6.8 관리하지않음
			//where = "where item_code='"+table.getItemCode()+"' and factory_no='"+factory_no+"'";
			//spare_count = mfgDAO.getColumData("MFG_STOCK_ITEM","stock_count",where);
			//if(spare_count.length() == 0) spare_count = "0";

			//출고예약가능수량 구하기(need_count - spare_count);
			//생산잔고 고려하여 예약수량 산출
			//reserve_count = Integer.toString(table.getNeedCount()-Integer.parseInt(spare_count));
			
			//생산잔고는 참조용으로하고 전산상 필요수량을 예약수량으로 할때
			//item type을 고려하여 (4:원부자재일때)
			if(table.getItemType().equals("4")) {
				add_count = "0";
				reserve_count = Integer.toString(table.getNeedCount());
			} else {
				add_count = Integer.toString(table.getNeedCount() * -1);
				reserve_count = "0";
			}

			//저장하기
			input = "INSERT INTO mfg_item (pid,gid,mfg_no,assy_code,level_no,item_code,item_name,";
			input += "item_spec,item_unit,item_type,item_loss,draw_count,mfg_count,need_count,spare_count,";
			input += "add_count,reserve_count,request_count,need_date,order_date,factory_no,factory_name) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+table.getAssyCode()+"','";
			input += table.getLevelNo()+"','"+table.getItemCode()+"','"+table.getItemName()+"','";
			input += table.getItemSpec()+"','"+table.getItemUnit()+"','"+table.getItemType()+"','"+"0"+"','";
			input += table.getDrawCount()+"','"+table.getMrpCount()+"','"+table.getNeedCount()+"','";
			input += spare_count+"','"+add_count+"','"+reserve_count+"','"+"0"+"','";
			input += order_start_date+"','"+""+"','"+factory_no+"','"+table.getFactoryName()+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	//  MFG ITEM 소요량조정 하기 
	// need_count : 전산상필요한 수량 , add_count : 추가한 수량
	//*******************************************************************/	
	public String updateMfgItem(String pid,String need_count,String add_count) throws Exception
	{
		String data="",where="",reserve_count="",update="";

		//예약수량 계산하기
		reserve_count = Integer.toString(Integer.parseInt(need_count) + Integer.parseInt(add_count));

		//MFG OPERATOR에 수정하기
		update = "UPDATE mfg_item SET add_count='"+add_count+"',reserve_count='"+reserve_count;
		update += "' where pid='"+pid+"'";
		mfgDAO.executeUpdate(update);
		
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// MFG ITEM 다단계 내용을 읽어 배열에 담기 
	//*******************************************************************/	
	public void arrayMfgItemList(String factory_no,String mfg_no,String item_code) throws Exception
	{

		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담기
		//--------------------------------------------
		String where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mfgDAO.getColumData("MFG_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mfgDAO.getMfgItemList(factory_no,mfg_no,level_no,item_code);
		int cnt = item_list.size();
		if(cnt == 0) return;
		item = new String[cnt][2];

		//재고시스템 재고예약및 배열에 담기
		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = Integer.toString(table.getReserveCount());
			an++;
		}
	}

	//*******************************************************************
	// MFG ITEM 다단계 내용을 읽어 배열에 담기 : 동일한 부품끼리 정리
	//*******************************************************************/	
	public void arrayMfgItemUniqueList(String factory_no,String mfg_no,String item_code) throws Exception
	{

		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담기
		//--------------------------------------------
		String where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mfgDAO.getColumData("MFG_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mfgDAO.getMfgItemList(factory_no,mfg_no,level_no,item_code);
		int cnt = item_list.size();
		if(cnt == 0) return;
		String[][] data = new String[cnt][7];

		//재고시스템 재고예약및 배열에 담기
		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			data[n][0] = table.getItemCode();
			data[n][1] = table.getItemName();
			data[n][2] = table.getItemSpec();
			data[n][3] = table.getItemUnit();
			data[n][4] = table.getItemType();
			data[n][5] = Integer.toString(table.getReserveCount());
			data[n][6] = table.getFactoryNo();
			n++;
		}
		//같은것 끼리 정렬하기
		sort.bubbleSortStringMultiAsc(data,0);

		//---------------------------------------------------
		// 동일한 부품끼리 정리하기
		//---------------------------------------------------
		item = new String[n][7];

		//새로운 배열로 담기 
		int item_cnt = n -1;
		int q=0; //수량계산	
		an = 0;	 //배열의 갯수
		for(int i=0; i<=item_cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < item_cnt) {
				//자품목코드가 같으면 숫자만 기록
				if(data[i][0].equals(data[i+1][0])) {	
					q += Integer.parseInt(data[i][5]);
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					q += Integer.parseInt(data[i][5]);
					item[an][0] = data[i][0];			//item code
					item[an][1] = data[i][1];			//item name
					item[an][2] = data[i][2];			//item spec
					item[an][3] = data[i][3];			//item unit
					item[an][4] = data[i][4];			//item type
					item[an][5] = Integer.toString(q);	//출고예약수량
					item[an][6] = data[i][6];			//공장번호
					an++;
					q=0;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				q += Integer.parseInt(data[i][5]);
				item[an][0] = data[i][0];			//item code
				item[an][1] = data[i][1];			//item name
				item[an][2] = data[i][2];			//item spec
				item[an][3] = data[i][3];			//item unit
				item[an][4] = data[i][4];			//item type
				item[an][5] = Integer.toString(q);	//출고예약수량
				item[an][6] = data[i][6];			//공장번호
				an++;
			} //else
		} //for

		//출력Test
		//for(int i=0; i<an; i++) {
		//	System.out.println(item[i][0]+":"+item[i][4]+":"+item[i][5]+":"+item[i][6]);
		//}

	}

	//--------------------------------------------------------------------
	//
	//		예약 진행하기 (생산관리:mfg_inout_master, 재고관리:st_item_stock_master)
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MFG ITEM 다단계 내용을 읽어 해당Table에 예약진행하기
	//*******************************************************************/	
	public void reserveMfgItem(String gid,String mfg_no,String item_code,String factory_no,String factory_name) throws Exception
	{
		//초기화
		String where="",input="",update="",pid="",outto_quantity="",stock_quantity="";

		//해당내용의 부품을 공통배열로 부품을 정리하여 담기
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:factory_no
		arrayMfgItemUniqueList(factory_no,mfg_no,item_code);

		//생산관리에 예약내용 저장하기 : 생산관리:mfg_inout_master
        for(int i=0; i<an; i++) {
			pid = anbdt.getNumID(i);
			input = "INSERT INTO mfg_inout_master (pid,gid,mfg_no,item_code,item_name,item_spec,item_unit,";
			input += "inout_status,reserve_count,factory_no,factory_name) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+item[i][0]+"','"+item[i][1]+"','";
			input += item[i][2]+"','"+item[i][3]+"','"+"0"+"','";
			input += item[i][5]+"','"+factory_no+"','"+factory_name+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
		}

		//재고관리에 예약내용 UPDATE하기 : 재고관리:st_item_stock_maste
		for(int i=0; i<an; i++) {
			//-----UPDATE할 출고예정수량 구하기 -----//
			//기존의 출고예정수량 구하기
			where = "where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
			outto_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","outto_quantity",where);
			if(outto_quantity.length() == 0) outto_quantity = "0";

			//출고예정수량 : 기존출고예정수량 + 출고예약수량
			outto_quantity = Integer.toString(Integer.parseInt(outto_quantity)+Integer.parseInt(item[i][5]));

			//-----UPDATE할 재고수량 구하기 -----//
			//기존의 재고수량 구하기
			stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
			if(stock_quantity.length() == 0) stock_quantity = "0";

			//재고수량 : 기존재고수량 - 출고예정수량
			stock_quantity = Integer.toString(Integer.parseInt(stock_quantity)-Integer.parseInt(item[i][5]));

			update = "UPDATE st_item_stock_master SET outto_quantity='"+outto_quantity;
			update += "',stock_quantity='"+stock_quantity;
			update += "' where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
			//System.out.println("update : " + update);
			mfgDAO.executeUpdate(update);
		}

	}

}


