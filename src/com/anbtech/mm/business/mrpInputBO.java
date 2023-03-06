package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mrpInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.SortArray sortA = new com.anbtech.util.SortArray();		//정렬
	private com.anbtech.mm.db.mrpModifyDAO mrpDAO = null;	
	private com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = null;						//구매관리
	private String query = "";

	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수
	private String purchase_type = "4";			//구매품 (item_type)
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mrpInputBO(Connection con) 
	{
		this.con = con;
		mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);
		purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		상태관리하기 
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  MRP MASTER에 상태관리하기
	// cur_mrp_status : 현재의 진행상태 , mrp_status : 바꿀 진행상태
	// tag : A(일괄),I(개별),그외 skip
	// pid : 일괄일때는 mrp master, 개별일때는 mrp item
	//*******************************************************************/	
	public String setMrpStatus(String pid,String cur_mrp_status,String mrp_status,String tag) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="",mps_no="",mrp_no="";
		String factory_no="",item_code="",delete="",order_status="",pu_req_no="";

		//MPS MASTE에서 해당정보 구하기
		com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
		mrpT = mrpDAO.readMrpMasterItem(pid);
		mps_no = mrpT.getMpsNo();
		mrp_no = mrpT.getMrpNo();
		factory_no = mrpT.getFactoryNo();
		item_code = mrpT.getItemCode();
		pu_req_no = mrpT.getPuReqNo();

		//---------------------------------------------
		// 관련 TABLE에 상태 수정하기
		//---------------------------------------------
		//결재 반려
		if(mrp_status.equals("0")) {
			update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
			mrpDAO.executeUpdate(update);

			deletePurchaseItem(pid);									//구매관리 : 등록내용 일괄삭제하기
			data = "MRS결재반려 되었습니다.";
		}
		//MRP접수등록
		else if(mrp_status.equals("1")) {
			if(cur_mrp_status.equals("2")) {			//재고예약 취소(재고관리만)	
					update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);

					data = "MRP편집요청이 진행되었습니다.";	
			} else if(cur_mrp_status.equals("3")) {	
					//mfg에서 접수하여 등록되었으면 MRP확정취소 불가함.
					where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					order_status = mrpDAO.getColumData("MFG_MASTER","order_status",where);

					//결재상신되었으면 MRP확정취소 불가함.
					where = "where plid like '"+pu_req_no+"%'";
					String app_state = mrpDAO.getColumData("APP_MASTER","app_state",where);
					if(app_state.length() == 0) order_status="";		//결재미상신
					else if(app_state.equals("APR")) order_status="";	//반려
					else order_status="31";
				
					//결재상신중이거나 결재완료
					if(order_status.equals("31")) {
						data = "전자결재 처리된 경우는 취소 할 수 없습니다.";
					}
					//결재미상신 && 제조오더 미등록 상태는 취소 가능
					else if(order_status.length() == 0) {
						update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
						mrpDAO.executeUpdate(update);

						deletePurchaseItem(pid);							//구매등록내용 일괄삭제하기 : tag = 'A'
						data = "MRP확정이 취소되었습니다.";
					} 
					//그외는 취소 불가
					else {
						data = "제조오더에서 처리된 경우는 취소 할 수 없습니다.";
					}

			} 
		} 
		//재고예약 -> MRP승인요청 (2004.6.4)
		else if(mrp_status.equals("2")) {
			if(cur_mrp_status.equals("1")) {		//재고예약 (재고관리만)
				update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
				mrpDAO.executeUpdate(update);

				data = reserveStockMrpItemList(factory_no,mrp_no,item_code);	//부품예약 및 수량update
				if(data.length() == 0) data = "MRP편집확정이 진행되었습니다.";	//재고부품예약이 진행되었습니다.
			} else if(cur_mrp_status.equals("3")) {	//MRP취소 : (구매등록삭제,mrp_master의구매요청삭제)
				update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
				mrpDAO.executeUpdate(update);

				deletePurchaseItem(pid);	
				data = "MRP확정이 취소되었습니다.";
			}
		} 
		//MRP확정 : 구매TABLE로 데이터 넘겨 생산부서에서 자체 구매결재
		else if(mrp_status.equals("3")) {
				if(cur_mrp_status.equals("31")) {	//전자결재 구매요청숭인시  
					update = "UPDATE mrp_master SET mrp_status='4' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);
				} else {							//MRP확정이면 (작성:1에서,반려:0에서 올수 있음)
					update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);
					
					savePurchaseItem(pid);			//구매부품 구매모듈로 전송하기				
					data = "MRP확정이 진행되었습니다.";
				}
		} 
		//결재승인 : 구매부서로 전송된 상태
		else if(mrp_status.equals("4")) {
				update = "UPDATE mps_master SET mps_status='5' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				mrpDAO.executeUpdate(update);
				data = "MRS결재승인 되었습니다.";

				//임시BOM일 경우 관련 TABLE모두 삭제(MPS_MASTER,MRP_MASTER,MRP_ITEM)
				//BOM_ITEM은 BOM을 새로 등록시 삭제후 다시입력함으로 제외시킴.
				where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				String purpose = mrpDAO.getColumData("MPS_MASTER","purpose",where);
				if(purpose.equals("1")) {		//임시BOM임
					delete = "DELETE FROM mps_master WHERE mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);

					delete = "DELETE FROM mrp_master WHERE mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);

					delete = "DELETE FROM mrp_item WHERE mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);
				}

		} 

		return data;
	}
	//--------------------------------------------------------------------
	//
	//		품목소요량관리(MRP MASTER) 에 관한 메소드 정의
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MRP MASTER에 데이터 입력하기
	//*******************************************************************/	
	public String insertMrp(String mps_no,String mrp_start_date,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String p_count,String plan_date,
		String item_unit,String factory_no,String factory_name,String reg_date,String reg_id,String reg_name,
		String pu_dev_date,String stock_link,String pjt_code,String pjt_name) throws Exception
	{
		String input="",update="",data="",where="",bom_status="",gid="",mrp_no="";
		String[] div_info = new String[2];		//사업부코드,사업부명
		String mrp_status = "";
		
		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) {
			data = "BOM구성이 안된 모델입니다. 먼저 BOM을 구성후 소요량산출을 실행하십시오.";
			return data;
		}

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt = mrpDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "품목번호는 FG코드로 구성된 BOM내의 제품 또는 반제품만 등록할 수 있습니다.";
			return data;
		}

		//MPS관리번호 구하기
		mrp_no = mrpDAO.getMrpNo(factory_no);

		//사업부코드 사업부명 구하기
		div_info = mrpDAO.getDivInfo(reg_id);

		//mrp_status의 상태를 정하기
		mrp_status = "1";				//작성상태로
		
		//MRP MASTER에 입력하기 
		String pid = anbdt.getID();
		input = "INSERT INTO mrp_master (pid,mps_no,mrp_no,mrp_start_date,mrp_end_date,model_code,model_name,";
		input += "fg_code,item_code,item_name,item_spec,p_count,plan_date,item_unit,mrp_status,factory_no,";
		input += "factory_name,reg_date,app_date,app_id,reg_div_code,reg_div_name,reg_id,reg_name,";
		input += "app_no,pu_dev_date,pu_req_no,stock_link,pjt_code,pjt_name) values('";
		input += pid+"','"+mps_no+"','"+mrp_no+"','"+mrp_start_date+"','"+""+"','"+model_code+"','"+model_name+"','";
		input += fg_code+"','"+item_code+"','"+item_name+"','"+item_spec+"','"+p_count+"','"+plan_date+"','"+item_unit+"','"+mrp_status+"','";
		input += factory_no+"','"+factory_name+"','"+reg_date+"','"+""+"','"+""+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+reg_id+"','"+reg_name+"','"+""+"','"+pu_dev_date+"','"+""+"','"+stock_link+"','";
		input += pjt_code+"','"+pjt_name+"')";
		//System.out.println("input : " + input);
		mrpDAO.executeUpdate(input);

		//MRP ITEM에 소요량 일괄입력하기
		insertMrpItem(pid,mrp_no,fg_code,item_code,mrp_start_date,p_count,factory_no,factory_name,pu_dev_date,stock_link);

		//MPS MASTER의 진행상태 바꿔주기
		update = "UPDATE mps_master SET mps_status='4' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrpDAO.executeUpdate(update);
		
		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  MRP MASTER에 데이터 수정하기
	//*******************************************************************/	
	public String updateMrp(String pid,String mrp_start_date,String reg_date,String pu_dev_date,
		String stock_link,String pjt_code,String pjt_name) throws Exception
	{
		String data="",where="",mrp_status="",update="";

		//진행상태가 작성중일때만 수정이 가능함
		where = "where pid='"+pid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);
		if(!mrp_status.equals("1")) {
			data = "작성중일때만 수정이 가능합니다.";
			return data;
		}
		
		//MBOM_STR에 수정하기
		update = "UPDATE mrp_master SET mrp_start_date='"+mrp_start_date+"',reg_date='"+reg_date;
		update += "',pu_dev_date='"+pu_dev_date+"',stock_link='"+stock_link;
		update += "',pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// MRP MASTER에 데이터 삭제하기
	//*******************************************************************/	
	public String deleteMrp(String pid,String mps_no,String mrp_no,String mrp_status,String factory_no) throws Exception
	{
		String delete = "",update="",data="";
		
		//진행상태 검사
		if(!mrp_status.equals("0") && !mrp_status.equals("1")) {
			data = "작성상태일때만 삭제가 가능합니다.";
			return data;
		} 

		//MRP MASTER 내용 삭제하기
		delete = "DELETE FROM mrp_master WHERE pid='"+pid+"'";
		mrpDAO.executeUpdate(delete);

		//MRP ITEM 내용 삭제하기
		delete = "DELETE FROM mrp_item WHERE mrp_no='"+mrp_no+"'";
		mrpDAO.executeUpdate(delete);

		//MPS MASTER의 진행상태 바꿔주기
		update = "UPDATE mps_master SET mps_status='3' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrpDAO.executeUpdate(update);
		
		data = "정상적으로 삭제되었습니다.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		하부구조 품목소요량관리(MRP ITEM) 에 관한 메소드 정의
	//		[일괄등록, 개별수정, MRP ITEM등록내용 보기,MRP ITEM의 ASSY LIST] 
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MPS접수시 MRP ITEM에 소요량 일괄입력하기
	// [MRP MASTER등록시 동시에 진행됨]
	//*******************************************************************/	
	private void insertMrpItem(String gid,String mrp_no,String fg_code,String item_code,String mrp_start_date,
		String mrp_count,String factory_no,String factory_name,String pu_dev_date,String stock_link) throws Exception
	{
		String input = "";
		String pid = "";

		//MBOM ITEM의 정보를 저장한다.(재고시스템 미연계 데이터임)
		ArrayList item_list = new ArrayList();
		item_list = sortArrayStrList(fg_code,item_code,mrp_start_date,mrp_count,stock_link,factory_no);	
	
		//테이블에 저장하기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			pid = anbdt.getNumID(n);
			//저장하기
			input = "INSERT INTO mrp_item (pid,gid,mrp_no,assy_code,level_no,item_code,item_name,";
			input += "item_spec,item_type,draw_count,mrp_count,need_count,stock_count,open_count,";
			input += "plan_count,add_count,mrs_count,item_unit,buy_type,factory_no,factory_name,";
			input += "pu_dev_date,pu_req_no) values('";
			input += pid+"','"+gid+"','"+mrp_no+"','"+table.getAssyCode()+"','";
			input += table.getLevelNo()+"','"+table.getItemCode()+"','"+table.getItemName()+"','";
			input += table.getItemSpec()+"','"+table.getItemType()+"','"+table.getDrawCount()+"','";
			input += table.getMrpCount()+"','"+table.getNeedCount()+"','"+table.getStockCount()+"','";
			input += table.getOpenCount()+"','"+table.getPlanCount()+"','"+table.getAddCount()+"','";
			input += table.getMrsCount()+"','"+table.getItemUnit()+"','"+table.getBuyType()+"','";
			input += factory_no+"','"+factory_name+"','"+pu_dev_date+"','"+""+"')";
			//System.out.println("input : " + input);
			mrpDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	// MRP ITEM에 개벌수정하기 [MRS수량]
	//*******************************************************************/
	public String updateMrpItem(String pid,String plan_count,String add_count) throws Exception
	{
		String data="",where="",mrp_status="",update="",mrs_count="";
		int pcnt = Integer.parseInt(plan_count);
		int acnt =  Integer.parseInt(add_count);

		//구매발주수량 구하기
		mrs_count = Integer.toString(pcnt + acnt);

		//MRP ITEM에 수정하기
		update = "UPDATE mrp_item SET add_count='"+add_count+"',mrs_count='"+mrs_count+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// 소용량 내용보기 : 재고시스템 연계 또는 미연계정보를 구분하여 실행
	// [MRP로부터 시작된것(mrp_no있음) 또는 임시로 MBOM ITEM에서 실행된것(mrp_no없음)]
	//*******************************************************************/	
	public ArrayList getStockMrpItemList(String factory_no,String mrp_no,String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String mrp_status) throws Exception
	{
		//초기화
		ArrayList mrp_item = new ArrayList();		//리턴할 mrp item List
		String[] stock = new String[2];				//재고시스템의 현재고량,입고예정수량
		int stock_cnt = 0;							//재고시스템상의 부품수 (재고량+입고예정량)
		int need_cnt = 0;							//필요수량(도면상의수량*생산량)
		
		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담고, 재고수량구하기
		//--------------------------------------------
		ArrayList item_list = new ArrayList();
		item_list = getMrpItemList(factory_no,mrp_no,fg_code,item_code,mrp_start_date,mrp_count,mrp_status);
		int cnt = item_list.size();
		if(cnt == 0) return mrp_item;
		String[][] data = new String[cnt][23];

		//배열에 담기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM수량
			data[n][10] = Integer.toString(table.getMrpCount());		//제조요구수량

			//-----------------------------------
			//현재고 및 입고예정수량 구하기
			//-----------------------------------
			//부품소요량 저장전
			if(mrp_status.equals("S")) {		
				if(stock_link.equals("1")) {		//재고시스템 연계
					stock = mrpDAO.getItemStockInfo(table.getItemCode(),factory_no);
					data[n][11] = stock[0];									//현재고수량
					data[n][12] = stock[1];									//입고예정수량
				} else {							//재고시스템 미연계
					data[n][11] = "0";		
					data[n][12] = "0";
				}

				//제조필요수량 : BOM수량 * 제조요구수량
				need_cnt = table.getDrawCount()*table.getMrpCount();
				data[n][13] = Integer.toString(need_cnt);					//제조필요수량

				//구매예정수량
				stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//재고수량
				data[n][14] = Integer.toString(need_cnt - stock_cnt);		//구매예정수량

				//추가수량 (원부자재를 고려하여)
				if(table.getItemType().equals("4")) data[n][15] = "0";											
				else data[n][15] = Integer.toString((need_cnt - stock_cnt) * -1);

				//구매발주수량
				data[n][16] = Integer.toString(Integer.parseInt(data[n][14]) + Integer.parseInt(data[n][15]));									

			} 
			//부품소요량저장 후
			else {
				data[n][11] = Integer.toString(table.getStockCount());		
				data[n][12] = Integer.toString(table.getOpenCount());
				data[n][13] = Integer.toString(table.getNeedCount());		//제조필요수량
				data[n][14] = Integer.toString(table.getPlanCount());		//구매예정수량
				data[n][15] = Integer.toString(table.getAddCount());		//추가수량
				data[n][16] = Integer.toString(table.getMrsCount());		//구매발주수량
			}

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][8]+":"+data[n][14]+":"+data[n][15]+":"+data[n][16]);
			n++;
		}

		//--------------------------------------------
		//Array list 전달할 내용 만들기
		//--------------------------------------------
		mrpItemTable tableS = new mrpItemTable();
		for(int i=0; i<n; i++) {
			tableS = new mrpItemTable();
			tableS.setPid(data[i][0]);	
			tableS.setGid(data[i][1]);	
			tableS.setMrpNo(data[i][2]);	
			tableS.setAssyCode(data[i][3]);	
			tableS.setLevelNo(Integer.parseInt(data[i][4]));
			tableS.setItemCode(data[i][5]);	
			tableS.setItemName(data[i][6]);	
			tableS.setItemSpec(data[i][7]);	
			tableS.setItemType(data[i][8]);	
			tableS.setDrawCount(Integer.parseInt(data[i][9]));	
			tableS.setMrpCount(Integer.parseInt(data[i][10]));
			tableS.setNeedCount(Integer.parseInt(data[i][13]));
			tableS.setStockCount(Integer.parseInt(data[i][11]));	
			tableS.setOpenCount(Integer.parseInt(data[i][12]));	
			tableS.setPlanCount(Integer.parseInt(data[i][14]));	
			tableS.setAddCount(Integer.parseInt(data[i][15]));
			tableS.setMrsCount(Integer.parseInt(data[i][16]));
			tableS.setItemUnit(data[i][17]);	
			tableS.setBuyType(data[i][18]);
			tableS.setFactoryNo(data[i][19]);
			tableS.setFactoryName(data[i][20]);
			tableS.setPuDevDate(data[i][21]);	
			tableS.setPuReqNo(data[i][22]);	
			mrp_item.add(tableS);
		}

		return mrp_item;
	}
	//*******************************************************************
	// 소용량 내용보기 
	// 재고시스템 미연계로 MRP ITEM or MBOM ITEM으로 부터읽은데잍터인지 구분
	// [MRP로부터 시작된것(mrp_no있음) 또는 임시로 MBOM ITEM에서 실행된것(mrp_no없음)]
	// level no : 1 이면 전체를 보여주고, 아니면 해당되는 단단계 정보를 보여준다:MRP ITEM의 경우만
	//*******************************************************************/	
	private ArrayList getMrpItemList(String factory_no,String mrp_no,String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String mrp_status) throws Exception
	{
		String where="",level_no="",use="E";
		ArrayList item_list = new ArrayList();

		//레벨번호 구하기
		where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		//소요량의 용도구하기
		//if(mrp_status.equals("0")) use = "V";		//반려상태로 구매소요량 내용보기
		//else if(mrp_status.equals("1")) use = "V";	//작성상태로 구매소요량 내용보기

		//경우별 소요량 구하기
		if(mrp_no.length() != 0) {		//MRP ITEM에서 소요량 보여주기
			if(level_no.equals("1")) {	//다단계 정보를 [전체보기]
				item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,use);	//조회용도
			} else {					//단단계 정보를 [해당 ASSY코드만 보기]
				item_list = mrpDAO.getSingleMrpItemList(factory_no,mrp_no,level_no,item_code);
			}
		} else {						//MBOM ITEM에서 소요량 보여주기
			item_list = sortArrayStrList(fg_code,item_code,mrp_start_date,mrp_count,"0",factory_no);
		}
		return item_list;
	}
	//*******************************************************************
	// ASSY CODE LIST 내용보기
	// 용도 : ASSY단위별 편집을 위해 [ASSY명,규격을 구하기위해...]
	//*******************************************************************/	
	public ArrayList getMrpItemAssyList(String factory_no,String mrp_no) throws Exception
	{
		//초기화
		ArrayList item_assy = new ArrayList();		//리턴할 Array List
		String[] item = new String[2];				//assy 명, 규격 구하기

		//Array list로 데이터 읽기
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getAssyList(factory_no,mrp_no);
		int cnt = item_list.size();
		if(cnt == 0) return item_assy;
		String[][] data = new String[cnt][3];

		//배열에 담기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getMrpNo();
			data[n][1] = table.getAssyCode();
			data[n][2] = Integer.toString(table.getLevelNo());
			//System.out.println(data[n][0]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}

		//해당ASSY의 품명,규격을 구하여 Array List로 다시 담는다.
		for(int i=0; i<n; i++) {
			table = new mrpItemTable();
			table.setMrpNo(data[i][0]);
			table.setAssyCode(data[i][1]);
			item = mrpDAO.getItemInfo(data[i][1]);
			table.setItemName(item[0]);
			table.setItemSpec(item[1]);
			table.setLevelNo(Integer.parseInt(data[i][2]));
			//System.out.println(data[i][2]+":"+data[i][1]+":"+item[0]+":"+item[1]);
			item_assy.add(table);
		}
		return item_assy;
	}

	//--------------------------------------------------------------------
	//
	//		재고시스템에 부품예약 및 취소 진행
	//			
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// 재고시스템에 현재고 수량 예약진행및 MRP ITEM에 UPDATE
	//*******************************************************************/	
	public String reserveStockMrpItemList(String factory_no,String mrp_no,String item_code) throws Exception
	{
		//초기화
		String rdata="",update="";
		String[] stock = new String[2];				//재고시스템의 현재고량,입고예정수량
		int stock_cnt = 0;							//재고시스템상의 부품수 (재고량+입고예정량)
		int need_cnt = 0;							//필요수량(도면상의수량*생산량)
		
		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담고, 재고수량구하기
		//--------------------------------------------
		String where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,"V");	//조회및 수량구하기
		int cnt = item_list.size();
		if(cnt == 0) {
			rdata = "UPDATE할 내용이 없습니다.";
			return rdata;
		}
		String[][] data = new String[cnt][23];

		//재고시스템 재고예약및 배열에 담기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM수량
			data[n][10] = Integer.toString(table.getMrpCount());		//제조요구수량

			//재고시스템연계 [현재고 및 입고예정수량] 예약및 수량구하기
			stock = mrpDAO.getItemStockInfo(table.getItemCode(),factory_no,mrp_no);
			data[n][11] = stock[0];									//현재고수량
			data[n][12] = stock[1];									//입고예정수량
			
			//제조필요수량 : BOM수량 * 제조요구수량
			need_cnt = table.getDrawCount()*table.getMrpCount();
			data[n][13] = Integer.toString(need_cnt);					//제조필요수량

			//구매예정수량
			stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//재고수량
			data[n][14] = Integer.toString(need_cnt - stock_cnt);		//구매예정수량

			//추가수량 (원부자재를 고려하여)
			if(table.getItemType().equals("4")) data[n][15] = "0";											
			else data[n][15] = Integer.toString((need_cnt - stock_cnt) * -1);

			//구매발주수량
			data[n][16] = Integer.toString(Integer.parseInt(data[n][14]) + Integer.parseInt(data[n][15]));									

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][9]+":"+data[n][10]+":"+data[n][13]);
			n++;
		}

		//--------------------------------------------
		//MRP ITEM에 수량 UPDATE진행하기
		//--------------------------------------------
		mrpItemTable tableS = new mrpItemTable();
		for(int i=0; i<n; i++) {
			update = "UPDATE mrp_item SET need_count='"+data[i][13]+"',stock_count='"+data[i][11];
			update +="',open_count='"+data[i][12]+"',plan_count='"+data[i][14]+"',add_count='"+data[i][15];
			update +="',mrs_count='"+data[i][16]+"' where pid='"+data[i][0]+"'";
			//System.out.println(update);
			mrpDAO.executeUpdate(update);
		}

		return rdata;
	}

	//*******************************************************************
	// 재고시스템에 현재고 수량 일괄 예약취소및 MRP ITEM에 UPDATE
	//*******************************************************************/	
	public String cancelStockMrpItemList(String factory_no,String mrp_no,String item_code,String mrp_status) throws Exception
	{
		//초기화
		String rdata="",update="";
		String stock = "";							///재고시스템의 현재고량
		int stock_cnt = 0;							//재고시스템상의 부품수 (재고량+입고예정량)
		int need_cnt = 0;							//필요수량(도면상의수량*생산량)
		int mrs_cnt = 0;							//실구매수량(구매예정 + 추가 + 재고취소수량)
		
		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담고, 재고수량구하기
		//--------------------------------------------
		String where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,"V");	//조회및 수량구하기
		int cnt = item_list.size();
		if(cnt == 0) {
			rdata = "UPDATE할 내용이 없습니다.";
			return rdata;
		}
		String[][] data = new String[cnt][23];

		//재고시스템 재고예약및 배열에 담기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM수량
			data[n][10] = Integer.toString(table.getMrpCount());		//제조요구수량

			//재고시스템연계 [현재고 및 입고예정수량] 취소및 수량구하기
			stock = mrpDAO.cancelItemStockInfo(table.getItemCode(),factory_no,mrp_no);
			data[n][11] = Integer.toString(Integer.parseInt(stock)*-1);	//현재고수량(예약취소로)
			data[n][12] = Integer.toString(table.getOpenCount());;	//입고예정수량
			
			//제조필요수량 : BOM수량 * 제조요구수량
			need_cnt = table.getDrawCount()*table.getMrpCount();
			data[n][13] = Integer.toString(need_cnt);					//제조필요수량

			//구매예정수량
			stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//재고수량
			data[n][14] = Integer.toString(need_cnt - stock_cnt);		//구매예정수량

			//추가수량및 구매발주수량
			data[n][15] = Integer.toString(table.getAddCount());		//추가수량
			mrs_cnt = Integer.parseInt(data[n][14])+Integer.parseInt(data[n][15]+Integer.parseInt(stock));
			data[n][16] = Integer.toString(mrs_cnt);					//구매발주수량

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][9]+":"+data[n][10]+":"+data[n][13]);
			n++;
		}

		//--------------------------------------------
		//MRP ITEM에 수량 UPDATE진행하기
		//--------------------------------------------
		//결재상신이후 에는 현재고 예약을 취소수량*-1로 표시하여 취소의미
		if(mrp_status.equals("4")) {				
			for(int i=0; i<n; i++) {
				update = "UPDATE mrp_item SET stock_count='"+data[i][11]+"' where pid='"+data[i][0]+"'";
				mrpDAO.executeUpdate(update);
			}
		} 
		//정상적으로 반영
		else {									
			for(int i=0; i<n; i++) {
				update = "UPDATE mrp_item SET need_count='"+data[i][13]+"',stock_count='"+data[i][11];
				update +="',open_count='"+data[i][12]+"',plan_count='"+data[i][14]+"',add_count='"+data[i][15];
				update +="',mrs_count='"+data[i][16]+"' where pid='"+data[i][0]+"'";
				//System.out.println(update);
				mrpDAO.executeUpdate(update);
			}
		}

		rdata = "정상적으로 UPDATE되었습니다.";
		return rdata;
	}
	//*******************************************************************
	// 재고시스템에 현재고 수량 개별 예약취소및 MRP ITEM에 UPDATE
	//*******************************************************************/	
	public String cancelIndStockMrpItemList(String pid,String cur_mrp_status,String factory_no) throws Exception
	{
		//초기화
		String rdata="",update="";
		String stock = "";							//재고시스템의 현재고량
		int stock_cnt = 0;							//재고시스템상의 부품수 (재고량+입고예정량)
		int need_cnt = 0;							//필요수량(도면상의수량*생산량)
		int mrs_cnt = 0;							//실구매수량(구매예정 + 추가 + 재고취소수량)

		//--------------------------------------------
		//Array list로 데이터 읽어 배열에 담고, 재고수량구하기
		//--------------------------------------------
		com.anbtech.mm.entity.mrpItemTable table = new com.anbtech.mm.entity.mrpItemTable();
		table = mrpDAO.readMrpItem(pid,factory_no);

		String[] data = new String[23];

		//재고시스템 재고예약및 배열에 담기
		data[0] = table.getPid();
		data[1] = table.getGid();
		data[2] = table.getMrpNo();
		data[3] = table.getAssyCode();
		data[4] = Integer.toString(table.getLevelNo());
		data[5] = table.getItemCode();
		data[6] = table.getItemName();
		data[7] = table.getItemSpec();
		data[8] = table.getItemType();
		data[9] = Integer.toString(table.getDrawCount());		//BOM수량
		data[10] = Integer.toString(table.getMrpCount());		//제조요구수량

		//재고시스템연계 [현재고 및 입고예정수량] 취소및 수량구하기
		stock = mrpDAO.cancelItemStockInfo(table.getItemCode(),table.getFactoryNo(),table.getMrpNo());
		data[11] = Integer.toString(Integer.parseInt(stock)*-1);	//현재고수량(예약취소로)
		data[12] = Integer.toString(table.getOpenCount());;	//입고예정수량
		
		//제조필요수량 : BOM수량 * 제조요구수량
		need_cnt = table.getDrawCount()*table.getMrpCount();
		data[13] = Integer.toString(need_cnt);					//제조필요수량

		//구매예정수량
		stock_cnt = Integer.parseInt(data[11]) + Integer.parseInt(data[12]);	//재고수량
		data[14] = Integer.toString(need_cnt - stock_cnt);		//구매예정수량

		//추가수량및 구매발주수량
		data[15] = Integer.toString(table.getAddCount());		//추가수량
		mrs_cnt = Integer.parseInt(data[14])+Integer.parseInt(data[15]+Integer.parseInt(stock));
		data[16] = Integer.toString(mrs_cnt);					//구매발주수량

		data[17] = table.getItemUnit();
		data[18] = table.getBuyType();
		data[19] = table.getFactoryNo();
		data[20] = table.getFactoryName();
		data[21] = table.getPuDevDate();
		data[22] = table.getPuReqNo();
	
		//--------------------------------------------
		//MRP ITEM에 수량 UPDATE진행하기
		//상태가 '4'이면 재고예약분만 취소된다.
		//--------------------------------------------
		if(!cur_mrp_status.equals("4")) {
			update = "UPDATE mrp_item SET need_count='"+data[13]+"',stock_count='"+data[11];
			update +="',open_count='"+data[12]+"',plan_count='"+data[14]+"',add_count='"+data[15];
			update +="',mrs_count='"+data[16]+"' where pid='"+data[0]+"'";
			//System.out.println(update);
			mrpDAO.executeUpdate(update);
		} 
		//결재상신이후 에는 현재고 예약을 -10000로 표시하여 취소의미
		else {
			update = "UPDATE mrp_item SET stock_count='"+data[11]+"' where pid='"+data[0]+"'";
			mrpDAO.executeUpdate(update);
		}
		

		rdata = "정상적으로 UPDATE되었습니다.";
		return rdata;
	}
	//--------------------------------------------------------------------
	//
	//		구매관리 에 관한 메소드 정의
	//			등록/수정/삭제 
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 일괄 구매관리에 등록하기
	 * pid : MRP MASTER의 pid
	 **********************************************************************/
	public void savePurchaseItem(String pid) throws Exception
	{
		
		String data="",where="",update="",input="";
		String mrp_no="",item_code="",level_no="",factory_no="",factory_name="";
		String reg_div_code="",reg_div_name="",reg_id="",reg_name="";
		String pjt_code="",pjt_name="";

		String request_no = purchaseDAO.getRequestNo();	//구매요청번호
		String todate = anbdt.getDate();				//구매요청일자
		String yy = todate.substring(2,4);				//요청년도 2자리
		String mm = anbdt.getMonth();					//요청월 2자리
		int len = request_no.length();
		String serial = request_no.substring(len-3,len);//요청 시리얼번호

		//MPS MASTE에서 해당정보 구하기
		com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
		mrpT = mrpDAO.readMrpMasterItem(pid);
		
		mrp_no = mrpT.getMrpNo();
		item_code = mrpT.getItemCode();
		factory_no = mrpT.getFactoryNo();
		factory_name = mrpT.getFactoryName();
		reg_div_code = mrpT.getRegDivCode();
		reg_div_name = mrpT.getRegDivName();
		reg_id = mrpT.getRegId();
		reg_name = mrpT.getRegName();
		pjt_code = mrpT.getPjtCode();
		pjt_name = mrpT.getPjtName();

		//구매 ITEM을 구하기위해 level no 구하기
		where = "where gid='"+pid+"' and assy_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		//--------------------------------------------------------
		//MRP에 구매요청번호 update하기 : mrp_master , mrp_item
		//--------------------------------------------------------
		update = "UPDATE mrp_master SET pu_req_no='"+request_no+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		update = "UPDATE mrp_item SET pu_req_no='"+request_no+"' where gid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		//--------------------------------------------------------
		//구매관리의 구매요청등록정보에 등록하기 : pu_requested_info
		//--------------------------------------------------------
		input = "INSERT INTO pu_requested_info (request_no,request_type,factory_code,factory_name,request_date,";
		input += "requester_div_code,requester_div_name,requester_id,requester_info,request_total_mount,";
		input += "reg_year,reg_month,reg_serial,project_code,project_name,aid) values('";
		input += request_no+"','"+"MRP"+"','"+factory_no+"','"+factory_name+"','"+todate+"','";
		input += reg_div_code+"','"+reg_div_name+"','"+reg_id+"','"+reg_name+"','"+"0"+"','";
		input += yy+"','"+mm+"','"+serial+"','"+pjt_code+"','"+pjt_name+"','"+""+"')";
		//System.out.println("input : " + input);
		mrpDAO.executeUpdate(input);

		//--------------------------------------------------------
		//구매관리의 구매요청품목리스트에 등록하기 : pu_requested_item
		//단 MRS수량이 0 보다 큰 수량만
		//--------------------------------------------------------
		ArrayList item_list = new ArrayList();
		item_list = sortArrayMrpItemList(factory_no,mrp_no,level_no,item_code);

		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			if(table.getMrsCount() > 0) {
				input = "INSERT INTO pu_requested_item (request_no,item_code,item_name,item_desc,request_unit,";
				input += "delivery_date,request_quantity,order_quantity,delivery_quantity,supplyer_code,";
				input += "supplyer_name,request_cost,fname,ftype,fsize,fpath,fumask,supply_cost,process_stat) values('";
				input += request_no+"','"+table.getItemCode()+"','"+table.getItemName()+"','"+table.getItemSpec()+"','"+table.getItemUnit()+"','";
				input += table.getPuDevDate()+"','"+table.getMrsCount()+"','"+0+"','"+0+"','"+""+"','";
				input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+"S01"+"')";
				//System.out.println("input : " + input);
				mrpDAO.executeUpdate(input);
			}
		}

	}

	//*******************************************************************
	// 개별취소 
	//	구매등록뒤 자재예약 취소시 구매에 자재예약분 반영하기 
	//  mrp_status = '3'이고 구매품이면 구매관리의 수량도 동시에 수정한다.
	//*******************************************************************/
	public void updateIndPurchaseItem(String pid,String factory_no) throws Exception
	{
		String data="",where="",gid="",mrp_status="",update="",mrs_count="",item_type="";
		String item_code="",pu_req_no="",request_quantity="",mrp_no="";
		int stock_count=0;

		//진행상태로 구매관리에 등록되었나 확인하여 등록시는 해당품목을 동시에 수정한다.
		//item_type='4' 인경우만 해당된다.
		com.anbtech.mm.entity.mrpItemTable mrpIT = new com.anbtech.mm.entity.mrpItemTable();
		mrpIT = mrpDAO.readMrpItem(pid,factory_no);
		gid = mrpIT.getGid();
		item_code = mrpIT.getItemCode();
		item_type = mrpIT.getItemType();
		stock_count = mrpIT.getStockCount();
		pu_req_no = mrpIT.getPuReqNo();
		mrp_no = mrpIT.getMrpNo();

		String stock = mrpDAO.cancelItemStockInfo(item_code,factory_no,mrp_no);

		//진행상태를 검사하여 mrp_status='3'인경우만 진행됨
		where = "where pid='"+gid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);

		if(!mrp_status.equals("3")) {		//상태가 3 인경우만 진행된다.
			return;
		}

		//구매품인지 판단하여 진행하기
		if(!item_type.equals(purchase_type)) {			//구매품만 진행된다. 
			return ;			
		}

		//구매에 등록된 수량을 파악하기
		where = "where request_no='"+pu_req_no+"' and item_code='"+item_code+"'";
		request_quantity = mrpDAO.getColumData("pu_requested_item","request_quantity",where);

		//등록할 수량을 구하기 (취소된 재고수량만큼 더하여 주기)
		mrs_count = Integer.toString(Integer.parseInt(stock) + Integer.parseInt(request_quantity));
	
		//pu_requested_item에 수정하기
		update = "UPDATE pu_requested_item SET request_quantity='"+mrs_count+"' ";
		update +="where request_no='"+pu_req_no+"' and item_code='"+item_code+"'";
		//System.out.println("up : "+update);
		mrpDAO.executeUpdate(update);
		
	}

	//*******************************************************************
	// 일괄취소 
	//	구매등록뒤 자재예약 일괄취소시 구매에 자재예약분 삭제하기
	//  mrp_status = '3'인 경우
	//*******************************************************************/
	public void deletePurchaseItem(String pid) throws Exception
	{
		String data="",where="",delete="",update="";
		String item_code="",mrp_status="",pu_req_no="";
		
		//진행상태를 검사하여 mrp_status='3'인경우만 진행됨
		where = "where pid='"+pid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);
		pu_req_no = mrpDAO.getColumData("MRP_MASTER","pu_req_no",where);

		//pu_requested_item,pu_requested_info 삭제하기
		delete = "DELETE from pu_requested_item where request_no='"+pu_req_no+"'";
		mrpDAO.executeUpdate(delete);

		delete = "DELETE from pu_requested_info where request_no='"+pu_req_no+"'";
		mrpDAO.executeUpdate(delete);

		//구매요청번호 삭제 : mrp_master,mrp_item
		update = "UPDATE mrp_master SET pu_req_no='' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		update = "UPDATE mrp_item SET pu_req_no='' where gid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		
		
	}

	/**********************************************************************
	 *  자품목끼리만 같은것
	 *  MRP ITEM 소요량 보기 
	 *  mrp_start_date : 출력 기준일자
	 **********************************************************************/
	private ArrayList sortArrayMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		//선언
		ArrayList item_list = new ArrayList();

		//------------------------------------------------------------------
		//전역 배열만들기
		//------------------------------------------------------------------
		saveMrpItemArray(factory_no,mrp_no,level_no,assy_code);

		//갯수 파악하기
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// 동일한 자품목 코드별로 묶기, 단 구매발주품목에 대해서만(item_type:4)
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//동일한 자품목끼리 묶을 배열선언
		String[][] data = new String[an][8];
		for(int i=0; i<an; i++) for(int j=0; j<8; j++) data[i][j]="";
		
		//새로운 배열로 담기 
		int cnt = an -1;
		int n=0,q=0;					//신규배열번호,mrs구매총수량
		for(int i=0; i<=cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < cnt) {
				//자품목코드가 같으면 숫자만 기록
				if(item[i][5].equals(item[i+1][5])) {	
					q += Integer.parseInt(item[i][16]);
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					q += Integer.parseInt(item[i][16]);
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][5];			//Item Code
					data[n][3] = item[i][6];			//Item Name
					data[n][4] = item[i][7];			//Item Spec
					data[n][5] = item[i][17];			//Item unit
					data[n][6] = item[i][21];			//희망입고일
					data[n][7] = Integer.toString(q);	//구매수량
					n++;
					q=0;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				q += Integer.parseInt(item[i][16]);
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][5];			//Item Code
				data[n][3] = item[i][6];			//Item Name
				data[n][4] = item[i][7];			//Item Spec
				data[n][5] = item[i][17];			//Item unit
				data[n][6] = item[i][21];			//희망입고일
				data[n][7] = Integer.toString(q);	//구매수량
			} //else
		} //for

		//출력Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][2]+":"+data[i][3]+":"+data[i][5]+":"+data[i][7]);
		//}

		//출력할 데이터로 ArrayList로 담아 전달하기
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setItemCode(data[i][2]);
			mit.setItemName(data[i][3]);
			mit.setItemSpec(data[i][4]);
			mit.setItemUnit(data[i][5]);
			mit.setPuDevDate(anbdt.getSepDate(data[i][6],"-"));
			mit.setMrsCount(Integer.parseInt(data[i][7]));
			item_list.add(mit); 
		}
		
		return item_list;
	}
	/**********************************************************************
	 * MRP ITEM을 배열로 담기
	 * [item type = '4'인 구매가능 품목만]
	 **********************************************************************/
	private void saveMrpItemArray(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.buyMrpItemList(factory_no,mrp_no,level_no,assy_code);
		int cnt = item_list.size();
		String[][] data = new String[cnt][23];
		for(int i=0; i<cnt; i++) for(int j=0; j<23; j++) data[i][j]="";

		//배열에 담기
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();

			if(table.getItemType().equals(purchase_type)) {
				data[an][0] = table.getPid();
				data[an][1] = table.getGid();
				data[an][2] = table.getMrpNo();
				data[an][3] = table.getAssyCode();
				data[an][4] = Integer.toString(table.getLevelNo());
				data[an][5] = table.getItemCode();
				data[an][6] = table.getItemName();
				data[an][7] = table.getItemSpec();
				data[an][8] = table.getItemType();
				data[an][9] = Integer.toString(table.getDrawCount());
				data[an][10] = Integer.toString(table.getMrpCount());
				data[an][11] = Integer.toString(table.getNeedCount());
				data[an][12] = Integer.toString(table.getStockCount());
				data[an][13] = Integer.toString(table.getOpenCount());
				data[an][14] = Integer.toString(table.getPlanCount());
				data[an][15] = Integer.toString(table.getAddCount());
				data[an][16] = Integer.toString(table.getMrsCount());
				data[an][17] = table.getItemUnit();
				data[an][18] = table.getBuyType();
				data[an][19] = table.getFactoryNo();
				data[an][20] = table.getFactoryName();
				data[an][21] = table.getPuDevDate();
				data[an][22] = table.getPuReqNo();
				//System.out.println(data[an][4]+":"+data[an][2]+":"+data[an][3]+":"+data[an][5]);
				an++;
			}
		}
		//전역배열로 담기
		item = new String[an][23];
		for(int i=0; i<an; i++) for(int j=0; j<23; j++) item[i][j]=data[i][j];

		//같은것 끼리 정렬하기
		sortA.bubbleSortStringMultiAsc(item,5);
	}

	//--------------------------------------------------------------------
	//
	//		MBOM ITEM의 ARRAY LIST을 배열에 담기
	//			
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 등록된 BOM구조를 배열에 담기 : 소요량구하기 준비 (다단계)
	 * 등록된 내용 출력보기
	 **********************************************************************/
	public void saveBomItemArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
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
			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getParentCode();
			item[an][3] = table.getChildCode();
			item[an][4] = table.getLevelNo();
			item[an][5] = table.getPartName();
			item[an][6] = table.getPartSpec();
			item[an][7] = table.getLocation();
			item[an][8] = table.getItemType();
			item[an][9] = table.getQtyUnit();
			item[an][10] = table.getBuyType();
			//System.out.println(item[an][4]+":"+item[an][2]+":"+item[an][3]);
			an++;
		}
	}
	/**********************************************************************
	 *  모품목별 / 자품목끼리 같은것 (다단계)
	 *  MBOM ITEM 소요량 보기 (MBOM ITEM정보를 ASSY별/자품목끼리 같은것 묶어 출력하기) 
	 *  mrp_start_date : 출력 기준일자
	 **********************************************************************/
	public ArrayList sortArrayStrList(String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String factory_no) throws Exception
	{
		//선언
		ArrayList item_list = new ArrayList();
		if(mrp_count.length() == 0) mrp_count = "1";

		//-----------------------------------------------------------------
		//검사 및 필요정보 구하기 : MBOM출력을 위한 필요정보 구하기
		//-----------------------------------------------------------------
		String where="",bom_status="",gid="",level_no="";

		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) return item_list;
		

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt_item = mrpDAO.checkItemCode(gid,item_code);
		if(cnt_item == 0) return item_list;
		
		//level no 구하기
		where = "where gid='"+gid+"' and parent_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MBOM_ITEM","level_no",where);	

		//------------------------------------------------------------------
		//전역 배열만들기
		//------------------------------------------------------------------
		saveBomItemArray(gid,level_no,item_code,mrp_start_date);

		//갯수 파악하기
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// ASSY코드별 동일한 자품목 코드별로 묶기
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//동일한 모/자품목에 대한 같은  배열선언
		String[][] data = new String[an][12];
		for(int i=0; i<an; i++) for(int j=0; j<12; j++) data[i][j]="";

		//새로운 배열로 담기 
		int cnt = an -1;
		int n=0,q=1;					//신규배열번호, 갯수
		for(int i=0; i<=cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < cnt) {
				//모품목코드와 자품목코드가 같으면 : location 만 기록
				if(item[i][2].equals(item[i+1][2]) && item[i][3].equals(item[i+1][3])) {
					data[n][7] += item[i][7]+",";	
					q++;
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][2];			//Assy Code
					data[n][3] = item[i][3];			//Item Code
					data[n][4] = item[i][4];			//Level no
					data[n][5] = item[i][5];			//Item Name
					data[n][6] = item[i][6];			//Item Spec
					data[n][7] += item[i][7];			//Location
					data[n][8] = item[i][8];			//Item Type
					data[n][9] = item[i][9];			//Item unit
					data[n][10] = item[i][10];			//Buy Type
					data[n][11] = Integer.toString(q);	//도면상의 동일한 item code 수량
					n++;
					q=1;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][2];			//Assy Code
				data[n][3] = item[i][3];			//Item Code
				data[n][4] = item[i][4];			//Level no
				data[n][5] = item[i][5];			//Item Name
				data[n][6] = item[i][6];			//Item Spec
				data[n][7] += item[i][7];			//Location
				data[n][8] = item[i][8];			//Item Type
				data[n][9] = item[i][9];			//Item unit
				data[n][10] = item[i][10];			//Buy Type
				data[n][11] = Integer.toString(q);	//도면상의 동일한 item code 수량
			} //else
		} //for

		//출력Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][4]+":"+data[i][2]+":"+data[i][3]+":"+data[i][11]);
		//}

		//출력할 데이터로 ArrayList로 담아 전달하기
		String[] stock = new String[2];
		int ncnt=0,stock_count=0,open_count=0,total_stock=0;
		int m_ncnt = 0;
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setPid(data[i][0]);
			mit.setGid(data[i][1]);
			mit.setMrpNo("");
			mit.setAssyCode(data[i][2]);
			mit.setLevelNo(Integer.parseInt(data[i][4]));
			mit.setItemCode(data[i][3]);
			mit.setItemName(data[i][5]);
			mit.setItemSpec(data[i][6]);
			mit.setItemType(data[i][8]);
			mit.setDrawCount(Integer.parseInt(data[i][11]));
			mit.setMrpCount(Integer.parseInt(mrp_count));

			ncnt = Integer.parseInt(data[i][11])*Integer.parseInt(mrp_count);	//실질필요수량
			mit.setNeedCount(ncnt);

			//재고시스템 미연계 : 재고예약이 없음으로 곧바로 하단수량을 입력한다.
			if(stock_link.equals("0")) {
				mit.setStockCount(0);
				mit.setOpenCount(0);
				mit.setPlanCount(ncnt);
				//item type에 따라 구분 (원부자재구분하여 수량 보여주기)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt);
				} else {
					m_ncnt = ncnt * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			} 
			//재고시스템 연계 : 재고예약시 하단수량을 입력한다.따라서 여기서는 모두 0
			else {
				//현재고 및 입고예정수량 수량구하기
				stock = mrpDAO.getItemStockInfo(data[i][3],factory_no);
				stock_count = Integer.parseInt(stock[0]);			//현재고수량
				open_count = Integer.parseInt(stock[1]);			//입고예정수량
				total_stock = stock_count + open_count;				//현재고+입고예정수량

				mit.setStockCount(stock_count);
				mit.setOpenCount(open_count);
				mit.setPlanCount(ncnt - total_stock);
				//item type에 따라 구분 (원부자재구분하여 수량 보여주기)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt - total_stock);
				} else {
					m_ncnt = (ncnt - total_stock) * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			}
			mit.setItemUnit(data[i][9]);
			mit.setBuyType(data[i][10]);
			mit.setFactoryNo("");
			mit.setFactoryName("");
			mit.setPuDevDate("");
			mit.setPuReqNo("");
			item_list.add(mit); 
		}
		
		return item_list;
	}

	/**********************************************************************
	 * 등록된 BOM구조를 배열에 담기 : 소요량구하기 준비 (단단계)
	 * 등록된 내용 출력보기
	 **********************************************************************/
	private void saveBomSingleItemArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMbomSingleItemList(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) item[i][j]="";

		//배열에 담기
		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getParentCode();
			item[an][3] = table.getChildCode();
			item[an][4] = table.getLevelNo();
			item[an][5] = table.getPartName();
			item[an][6] = table.getPartSpec();
			item[an][7] = table.getLocation();
			item[an][8] = table.getItemType();
			item[an][9] = table.getQtyUnit();
			item[an][10] = table.getBuyType();
			//System.out.println(item[an][4]+":"+item[an][2]+":"+item[an][3]);
			an++;
		}
	}
	/**********************************************************************
	 *  모품목별 / 자품목끼리 같은것 (단단계)
	 *  MBOM ITEM 소요량 보기 (MBOM ITEM정보를 ASSY별/자품목끼리 같은것 묶어 출력하기) 
	 *  mrp_start_date : 출력 기준일자
	 **********************************************************************/
	public ArrayList sortArraySingleStrList(String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String factory_no) throws Exception
	{
		//선언
		ArrayList item_list = new ArrayList();
		if(mrp_count.length() == 0) mrp_count = "1";

		//-----------------------------------------------------------------
		//검사 및 필요정보 구하기 : MBOM출력을 위한 필요정보 구하기
		//-----------------------------------------------------------------
		String where="",bom_status="",gid="",level_no="";

		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) return item_list;
		

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt_item = mrpDAO.checkItemCode(gid,item_code);
		if(cnt_item == 0) return item_list;
		
		//level no 구하기
		where = "where gid='"+gid+"' and parent_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MBOM_ITEM","level_no",where);	

		//------------------------------------------------------------------
		//전역 배열만들기
		//------------------------------------------------------------------
		saveBomSingleItemArray(gid,level_no,item_code,mrp_start_date);

		//갯수 파악하기
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// ASSY코드별 동일한 자품목 코드별로 묶기
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//동일한 모/자품목에 대한 같은  배열선언
		String[][] data = new String[an][12];
		for(int i=0; i<an; i++) for(int j=0; j<12; j++) data[i][j]="";

		//새로운 배열로 담기 
		int cnt = an -1;
		int n=0,q=1;					//신규배열번호, 갯수
		for(int i=0; i<=cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < cnt) {
				//모품목코드와 자품목코드가 같으면 : location 만 기록
				if(item[i][2].equals(item[i+1][2]) && item[i][3].equals(item[i+1][3])) {
					data[n][7] += item[i][7]+",";	
					q++;
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][2];			//Assy Code
					data[n][3] = item[i][3];			//Item Code
					data[n][4] = item[i][4];			//Level no
					data[n][5] = item[i][5];			//Item Name
					data[n][6] = item[i][6];			//Item Spec
					data[n][7] += item[i][7];			//Location
					data[n][8] = item[i][8];			//Item Type
					data[n][9] = item[i][9];			//Item unit
					data[n][10] = item[i][10];			//Buy Type
					data[n][11] = Integer.toString(q);	//도면상의 동일한 item code 수량
					n++;
					q=1;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][2];			//Assy Code
				data[n][3] = item[i][3];			//Item Code
				data[n][4] = item[i][4];			//Level no
				data[n][5] = item[i][5];			//Item Name
				data[n][6] = item[i][6];			//Item Spec
				data[n][7] += item[i][7];			//Location
				data[n][8] = item[i][8];			//Item Type
				data[n][9] = item[i][9];			//Item unit
				data[n][10] = item[i][10];			//Buy Type
				data[n][11] = Integer.toString(q);	//도면상의 동일한 item code 수량
			} //else
		} //for

		//출력Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][4]+":"+data[i][2]+":"+data[i][3]+":"+data[i][11]);
		//}

		//출력할 데이터로 ArrayList로 담아 전달하기
		String[] stock = new String[2];
		int ncnt=0,stock_count=0,open_count=0,total_stock=0;
		int m_ncnt = 0;
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setPid(data[i][0]);
			mit.setGid(data[i][1]);
			mit.setMrpNo("");
			mit.setAssyCode(data[i][2]);
			mit.setLevelNo(Integer.parseInt(data[i][4]));
			mit.setItemCode(data[i][3]);
			mit.setItemName(data[i][5]);
			mit.setItemSpec(data[i][6]);
			mit.setItemType(data[i][8]);
			mit.setDrawCount(Integer.parseInt(data[i][11]));
			mit.setMrpCount(Integer.parseInt(mrp_count));

			ncnt = Integer.parseInt(data[i][11])*Integer.parseInt(mrp_count);	//실질필요수량
			mit.setNeedCount(ncnt);

			//재고시스템 미연계 : 재고예약이 없음으로 곧바로 하단수량을 입력한다.
			if(stock_link.equals("0")) {
				mit.setStockCount(0);
				mit.setOpenCount(0);
				mit.setPlanCount(ncnt);
				//item type에 따라 구분 (원부자재구분하여 수량 보여주기)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt);
				} else {
					m_ncnt = ncnt * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			} 
			//재고시스템 연계 : 재고예약시 하단수량을 입력한다.따라서 여기서는 모두 0
			else {
				//현재고 및 입고예정수량 수량구하기
				stock = mrpDAO.getItemStockInfo(data[i][3],factory_no);
				stock_count = Integer.parseInt(stock[0]);			//현재고수량
				open_count = Integer.parseInt(stock[1]);			//입고예정수량
				total_stock = stock_count + open_count;				//현재고+입고예정수량

				mit.setStockCount(stock_count);
				mit.setOpenCount(open_count);
				mit.setPlanCount(ncnt - total_stock);
				//item type에 따라 구분 (원부자재구분하여 수량 보여주기)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt - total_stock);
				} else {
					m_ncnt = (ncnt - total_stock) * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			}
			mit.setItemUnit(data[i][9]);
			mit.setBuyType(data[i][10]);
			mit.setFactoryNo("");
			mit.setFactoryName("");
			mit.setPuDevDate("");
			mit.setPuReqNo("");
			item_list.add(mit); 
		}
		
		return item_list;
	}

}


