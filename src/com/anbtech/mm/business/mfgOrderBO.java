package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgOrderBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//정렬하기
	private com.anbtech.mm.db.mfgModifyDAO mfgDAO = null;	
	private com.anbtech.mm.db.mfgOrderDAO odrDAO = null;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private String query = "";
	private String delivery_no="";				//부품출고의뢰번호

	private String[][] item = null;				//배열
	private int an = 0;							//items의 배열 갯수

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mfgOrderBO(Connection con) 
	{
		this.con = con;
		mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
		odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		부품출고의뢰서 등록에 관한 메소드
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  부품출고의뢰 초기화 등록하기 : out_create
	//*******************************************************************/	
	public String saveInitDelivery(String login_id,String login_name,String gid,String mfg_no,
		String assy_code,String level_no,String assy_spec,String factory_no,String factory_name) throws Exception
	{
		String data="",where="",update="",input="";

		//조건1 : 부품출고의뢰 가능한지 상태 검사하기 : mfg_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		String order_status = odrDAO.getColumData("MFG_MASTER","order_status",where);
		String order_type = odrDAO.getColumData("MFG_MASTER","order_type",where);
		if(order_status.equals("6")) {
			data = "생산실적 마감상태입니다.";
			return data;
		}

		//조건2 : 해당공정의 진행상태를 검사한다. : mfg_operator
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and assy_code='"+assy_code+"'";
		String op_order = odrDAO.getColumData("MFG_OPERATOR","op_order",where);
		if(!op_order.equals("1")) {
			if(op_order.equals("2")) data = "부품출고마감 상태입니다.";
			else if(op_order.equals("3")) data = "생산실적 마감상태입니다.";
			return data;
		}

		//조건3 : 해당공정에 해당되는 부품출고의뢰가 현재진행중인 것이 있는지 판단한다. : mfg_req_master
		String req_status = odrDAO.checkProcess(mfg_no,assy_code,factory_no);
		if(req_status.equals("F")) {
			data = "부품출고의뢰 작성중인 내용이 있습니다.";
			return data;
		}

		//준비1 : 공정별 MFG ITEM을 배열에 담기 : 데이터가 있는지, 전체중 출고의뢰할 부품이 있는지
		data = arrayMfgItemList(mfg_no,assy_code,factory_no);
		if(data.length() != 0) {
			//출고가능한 부품이 없는경우 mfg_operator상태를 자재마감(op_order='2')으로 바꿔준다.
			//다음을 계속진행하기위해 (상태를 바꿔주지 않으면 실적등록(JSP에서)을 할 수 없음으로)
			update = "UPDATE mfg_operator SET op_order='2' where mfg_no='"+mfg_no+"' and ";
			update += "assy_code='"+assy_code+"' and factory_no='"+factory_no+"'";
			odrDAO.executeUpdate(update);

			return data; 
		}

		//등록1 : 부품출고의뢰 테이블에 저장하기 : st_reserved_item_info
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:request_count,7:의뢰가능수량,8:factory_no,9:factory_name
		
		//부품출고의뢰번호 생성 (공장구분없이 생성됨)
		if(order_type.equals("MRP")) delivery_no = mfgDAO.getDeliveryNo();	//생산계획
		else delivery_no = mfgDAO.getDeliveryManNo();	//긴급오더

		//부품출고의뢰 초기작성상태로 등록
		String[] div_info = new String[2];
		div_info = mfgDAO.getDivInfo(login_id);		//0:부서코드, 1:부서명
		for(int i=0; i<an; i++) {
			if(Integer.parseInt(item[i][7]) > 0) {
				input = "INSERT INTO st_reserved_item_info (delivery_no,item_code,item_name,item_desc,item_type,";
				input += "request_quantity,request_unit,request_date,factory_code,factory_name,ref_no,";
				input += "requestor_div_code,requestor_div_name,requestor_id,requestor_info,process_stat) values('";
				input += delivery_no+"','"+item[i][0]+"','"+item[i][1]+"','"+item[i][2]+"','"+item[i][4]+"','";
				input += item[i][7]+"','"+item[i][3]+"','"+anbdt.getDateNoformat()+"','"+factory_no+"','"+factory_name+"','";
				input += mfg_no+"','"+div_info[0]+"','"+div_info[1]+"','"+login_id+"','"+login_name+"','"+"S50"+"')";
				//System.out.println("input : " + input);
				odrDAO.executeUpdate(input);
			}
		}

		//등록2 : 부품출고관리 마스터에 입력하기 (MFG_REQ_MASTER)
		String pid = anbdt.getID();
		input = "INSERT INTO mfg_req_master (pid,gid,mfg_no,mfg_req_no,assy_code,assy_spec,";
		input += "level_no,req_status,req_date,req_div_code,req_div_name,req_user_id,req_user_name,";
		input += "factory_no,factory_name) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+delivery_no+"','"+assy_code+"','";
		input += assy_spec+"','"+level_no+"','"+"1"+"','"+anbdt.getDateNoformat()+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+login_id+"','"+login_name+"','"+factory_no+"','"+factory_name+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//등록3 : MFG_MASTER에 상태 바꿔주기
		if(order_status.equals("3")) {
			update = "UPDATE mfg_master SET order_status='4' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		//부품출고의뢰번호 SETTING 하기
		setDeliveryNo(delivery_no);

		data = "부품출고의뢰 초기화정보가 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  부품출고의뢰 요청하기 : out_confirm
	//*******************************************************************/	
	public String confirmDelivery(String mfg_no,String mfg_req_no,String assy_code,
		String level_no,String factory_no) throws Exception
	{
		String data="",where="",update="",input="";
		String reserve_count="",request_count="";
		int cnt = 0;

		//준비1 : 부품출고 작성중인 데이터 배열[dev_item],[dev_an]에 담기: st_reserved_item_info
		//0:pid, 1:mfg_no, 2:mfg_req_no, 3:item_code, 4:item_name, 5:item_spec
		//6:item_type, 7:item_unit, 8:req_count, 9:factory_no
		data = arrayMfgReqItemList(mfg_req_no,factory_no);
		if(data.length() != 0) return data;

		//등록1 : MFG_ITEM에 출고의뢰수량[request_count]을 update한다.
		for(int i=0; i<an; i++) {
			//기등록된 출고의뢰수량을 찾는다.
			where = "where item_code='"+item[i][3]+"' and factory_no='"+factory_no+"' ";
			where +="and mfg_no='"+mfg_no+"' and assy_code='"+assy_code+"' and level_no='"+level_no+"'";
			reserve_count = mfgDAO.getColumData("MFG_ITEM","reserve_count",where);		//예약수량
			request_count = mfgDAO.getColumData("MFG_ITEM","request_count",where);		//기출고의뢰수량
			if(reserve_count.length() == 0) reserve_count = "0";
			if(request_count.length() == 0) request_count = "0";

			//sum 출고의뢰수량 : 기출고의뢰수량[request_count] + 현출고의뢰수량[dev_item[][8]
			request_count = Integer.toString(Integer.parseInt(request_count) + Integer.parseInt(item[i][8]));

			//출고수량이 예약수량과 같은지 파악한다. : 같으면 출고가 완료되었음을 알리기위해 : MFG_OPERATOR
			if(reserve_count.equals(request_count)) cnt++;

			//출고수량을 update한다.
			update = "UPDATE mfg_item SET request_count='"+request_count+"' "+where;
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);

		}

		//등록2 : 부품출고의뢰상태임을 알린다 : st_reserved_item_info
		update = "UPDATE st_reserved_item_info SET process_stat='S53' where delivery_no='"+mfg_req_no+"' ";
		update += "and factory_code='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//등록3 : 부품출고의뢰 상태를 부품출고의뢰마스터에 알린다 : mfg_req_master
		update = "UPDATE mfg_req_master SET req_status='2' where mfg_req_no='"+mfg_req_no+"' ";
		update += "and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//등록4 : 해당공정의 부품출고의뢰를 더이상 진행할 수 있는지를 판단한다.
		if(an == cnt) {
			update = "UPDATE mfg_operator SET op_order='2' where mfg_no='"+mfg_no+"' ";
			update += "and assy_code='"+assy_code+"' and level_no='"+level_no+"' and factory_no='"+factory_no+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}
		data = "정상적으로 부품요청의뢰가 진행되었습니다.";
		return data;
	}

	//*******************************************************************
	//  부품출고의뢰 출고의뢰수량 수정하기 
	//*******************************************************************/	
	public String saveDeliveryItem(String pid,String req_count) throws Exception
	{
		String data="",update="",where="",outto_quantity="",stock_quantity="",was_req_count="0";
		String item_code="",factory_no="";

/*		//수정전 부품출고의뢰 수량 구하기
		where = "where mid='"+pid+"'";
		was_req_count = mfgDAO.getColumData("st_reserved_item_info","request_quantity",where);
		item_code = mfgDAO.getColumData("st_reserved_item_info","item_code",where);
		factory_no = mfgDAO.getColumData("st_reserved_item_info","factory_code",where);
		
		//부품재고 마스터
		//-----UPDATE할 출고예정수량 구하기 -----//
		//기존의 출고예정수량 구하기
		where = "where item_code='"+item_code+"' and factory_code='"+factory_no+"'";
		outto_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","outto_quantity",where);
		if(outto_quantity.length() == 0) outto_quantity = "0";

		//출고예정수량 : 기존출고예정수량 - 수정전 부품출고의뢰 수량 + 수정수량
		outto_quantity = Integer.toString(Integer.parseInt(outto_quantity)-Integer.parseInt(was_req_count)+Integer.parseInt(req_count));

		//-----UPDATE할 재고수량 구하기 -----//
		//기존의 재고수량 구하기
		stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
		if(stock_quantity.length() == 0) stock_quantity = "0";

		//재고수량 : 기존재고수량 + 수정전 부품출고의뢰 수량 - 출고예정수량
		stock_quantity = Integer.toString(Integer.parseInt(stock_quantity)+Integer.parseInt(was_req_count)-Integer.parseInt(req_count));

		update = "UPDATE st_item_stock_master SET outto_quantity='"+outto_quantity;
		update += "',stock_quantity='"+stock_quantity;
		update += "' where item_code='"+item_code+"' and factory_code='"+factory_no+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

*/
		//부품출고의뢰 마스터 
		update = "UPDATE st_reserved_item_info SET request_quantity='"+req_count+"' where mid='"+pid+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		data = "정상적으로 수정되었습니다.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		실적등록 관리에 관한 메소드 : mfg_product_master , mfg_product_item
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  실적등록 마스터 초기화 등록하기 
	//*******************************************************************/	
	public String saveInitProduct(String login_id,String login_name,String mfg_no,
		String assy_code,String level_no,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",pid="",gid="";

		//조건1 : 실적마스터 등록여부 판단 : mfg_product_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		where += " and item_code='"+assy_code+"' and mfg_id='"+login_id+"'";
		String output_status = odrDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		if(output_status.length() != 0) return data;
		

		//등록1 : 실적마스터를 등록한다. : mfg_product_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		where += " and assy_code='"+assy_code+"' and level_no='"+level_no+"'";
		pid = odrDAO.getColumData("MFG_OPERATOR","pid",where);
		mfgOperatorTable table = new mfgOperatorTable();
		table  = odrDAO.readMfgOperator(pid);

		String[] part_info = new String[2];
		part_info = odrDAO.getItemInfo(table.getAssyCode());			//이름,규격
		pid = anbdt.getID();

		where = "where m_code='"+table.getOpNo()+"'";
		String nickname = odrDAO.getColumData("MBOM_ENV","tag",where);	//op code 약어

		com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgT = odrDAO.readMfgMasterItem(mfg_no,factory_no);

		input = "INSERT INTO mfg_product_master (pid,gid,mfg_no,model_code,model_name,fg_code,";
		input += "item_code,item_name,item_spec,order_count,order_unit,mfg_id,mfg_name,output_status,";
		input += "factory_no,output_date,op_code,op_name,op_nickname) values('";
		input += pid+"','"+table.getGid()+"','"+table.getMfgNo()+"','"+mfgT.getModelCode()+"','"+mfgT.getModelName()+"','";
		input += mfgT.getFgCode()+"','"+table.getAssyCode()+"','"+part_info[0]+"','";
		input += part_info[1]+"','"+table.getMfgCount()+"','"+table.getMfgUnit()+"','"+login_id+"','"+login_name+"','";
		input += "1"+"','"+factory_no+"','"+anbdt.getDateNoformat()+"','"+table.getOpNo()+"','";
		input += table.getOpName()+"','"+nickname+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//등록2 : 제조진행상태 바꿔주기 : mfg_master
		//해당 작지로 부품출고된적이 있으면 진행함.
		query = "select count(*) from mfg_req_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and ";
		query +="req_status != '1'";
		int cnt = odrDAO.getTotalCount(query);
		if(cnt != 0) {
			update = "UPDATE mfg_master SET order_status='5' where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		return data;
	}

	//*******************************************************************
	//  공정별 생산실적 등록하기 : product_save
	//*******************************************************************/	
	public String saveProductItem(String login_id,String login_name,String gid,String mfg_no,String item_code,
		String item_name,String item_spec,String total_count,String good_count,String bad_count,
		String bad_type,String bad_note,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",pid="",used_cnt="",op_no ="";
		String delivery_quantity = "";		//출고수량
		int use_count = 0;					//총제조에 소요된 수량
		int rev_count = 0;					//예약수량
		int rst_total_count=0,rst_good_count=0,rst_bad_count=0,working_count=0;

		//조건1 : 작업지시 상태를 검사한후 내부에서 조건을 판단한다.
		where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String op_order = odrDAO.getColumData("MFG_OPERATOR","op_order",where);

		if(op_order.equals("1")) {		//작업지시 상태
			//조건1-1 : 부품출고의뢰신청한 적이 있는지 판단 : mfg_req_master
			query = "select count(*) from mfg_req_master where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' ";
			query += "and factory_no='"+factory_no+"'";
			if(odrDAO.getTotalCount(query) == 0) {
				data = "부품출고의뢰후 실적등록이 가능합니다.";
				return data;
			}
		} else if(op_order.equals("3")) {	//실적마감
			data = "실적마감 상태입니다.";
			return data;
		}

		//조건2 : 금일등록한 내용이 있는지 판단하기 (2중등록 방지)
		where = "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String output_date = odrDAO.getColumData("MFG_PRODUCT_ITEM","output_date",where);
		if(output_date.equals(anbdt.getDateNoformat())) {
			data = "금일등록한 내용이 있습니다.  내용을 수정하시오.";
			return data;
		}

		//준비1 : 공정별 MFG ITEM을 배열에 담기 : 출고부품에서 해당제품의 수량만큼 가감하기위해
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:draw_count,6:reserve_count,7:request_count,8:factory_no,9:factory_name
		data = getMfgItemList(mfg_no,item_code,factory_no);
		if(data.length() != 0) return data; 

		//준비2 : 생산실적 마스터 내용 읽기 : mfg_product_master
		com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
		productMT = odrDAO.readMfgProductMasterRead(mfg_no,item_code,factory_no);
		if(productMT.getPid().length() == 0) {
			data = "생산실적을 누적관리할 데이터가 없습니다. [mfg_product_master]"; return data;
		}

		//등록1 [생산실적]: 생산실적 제품등록 테이블에 저장하기 : mfg_product_item
		where = "where item_no='"+item_code+"'";
		op_no = odrDAO.getColumData("ITEM_MASTER","op_code",where);
		pid = anbdt.getID();
		input = "INSERT INTO mfg_product_item (pid,gid,mfg_no,item_code,item_name,item_spec,";
		input += "total_count,good_count,bad_count,mfg_id,mfg_name,output_date,op_no,";
		input += "bad_type,bad_note,output_status,factory_no) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+item_code+"','"+item_name+"','"+item_spec+"','";
		input += total_count+"','"+good_count+"','"+bad_count+"','"+login_id+"','"+login_name+"','";
		input += anbdt.getDateNoformat()+"','"+op_no+"','"+bad_type+"','"+bad_note+"','"+"1"+"','"+factory_no+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//등록2 [생산실적]: 생산실적 제품등록 마스터 테이블에 저장하기 : mfg_product_master
		rst_total_count = productMT.getTotalCount() + Integer.parseInt(total_count);
		rst_good_count = productMT.getGoodCount() + Integer.parseInt(good_count);
		rst_bad_count = productMT.getBadCount() + Integer.parseInt(bad_count);

		update = "UPDATE mfg_product_master SET total_count='"+rst_total_count+"',";
		update += "good_count='"+rst_good_count+"',bad_count='"+rst_bad_count+"',";
		update += "output_date='"+output_date+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);
		

		//등록3 [부품수량]: 재고관리의 재고수량 테이블에 빼기: st_item_stock_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//예약수량
			if(rev_count > 0) {	
				//기존 제조에 사용한 수량 구하기
				where = "where item_code='"+item[i][0]+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				//제조에서 사용한 수량 구하기 : BOM수량 * 제조수량
				use_count = Integer.parseInt(item[i][5]) * Integer.parseInt(total_count);
				if(use_count > rev_count) use_count = rev_count;		//예약수량을 넘지못함.

				//updte할 수량 (기존출고량 - 제조소요량)
				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - use_count);

				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][0]+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//등록4 [부품수량]: 생산관리의 재고수량 테이블에 실제조수량 더하기: mfg_inout_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//예약수량
			if(rev_count > 0) {	
				//기존 제조수량 구하기
				where = "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				where += "factory_no='"+factory_no+"'";
				used_cnt = odrDAO.getColumData("MFG_INOUT_MASTER","use_count",where);
				if(used_cnt.length() == 0) used_cnt = "0";

				//제조에서 사용한 수량 구하기 : BOM수량 * 제조수량
				use_count = Integer.parseInt(item[i][5]) * Integer.parseInt(total_count);
				if(use_count > rev_count) use_count = rev_count;		//예약수량을 넘지못함.

				//updte할 수량 (기존제조량 + 제조소요량)
				use_count = Integer.parseInt(used_cnt) + use_count;

				update = "UPDATE mfg_inout_master SET use_count='"+use_count+"' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				update += "factory_no='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//등록5 [생산실적]: 상태 바꿔주기 (단,최종제품수량 등록일 경우는 수량도 같이 입력한다.)
		com.anbtech.mm.entity.mfgMasterTable mfgMT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgMT = odrDAO.readMfgMasterItem(gid);
		if(mfgMT.getItemCode().equals(item_code)) {		//최종제품생산코드
			rst_total_count = mfgMT.getRstTotalCount() + Integer.parseInt(total_count);
			rst_good_count = mfgMT.getRstGoodCount() + Integer.parseInt(good_count);
			rst_bad_count = mfgMT.getRstBadCount() + Integer.parseInt(bad_count);
			working_count = mfgMT.getMfgCount() - rst_total_count; 

			update = "UPDATE mfg_master SET rst_total_count='"+rst_total_count+"',";
			update += "rst_good_count='"+rst_good_count+"',rst_bad_count='"+rst_bad_count+"',";
			update += "working_count='"+working_count+"' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		update = "UPDATE mfg_master SET order_status='5' ";
		update += "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		data = "생산실적이 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  공정별 생산실적 수정하기 : product_modify
	//*******************************************************************/	
	public String updateProductItem(String gid,String pid,String mfg_no,String item_code,String total_count,
		String good_count,String bad_count,String bad_type,String bad_note,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",used_cnt="";
		String delivery_quantity = "";		//출고수량
		int use_count = 0;					//총제조에 소요된 수량
		int rev_count = 0;					//예약수량
		int diff_total_count = 0;			//기존총생산수량 - 수정할 생산수량
		int diff_good_count = 0;			//기존총양품수량 - 수정할 양품수량
		int diff_bad_count = 0;				//기존총불량수량 - 수정할 불량수량
		int rst_total_count=0,rst_good_count=0,rst_bad_count=0,working_count=0;
		if(bad_count.length() == 0) bad_count = "0";

		//준비1 : 공정별 MFG ITEM을 배열에 담기 : 출고부품에서 해당제품의 수량만큼 가감하기위해
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:draw_count,6:reserve_count,7:request_count,8:factory_no,9:factory_name
		data = getMfgItemList(mfg_no,item_code,factory_no);
		if(data.length() != 0) return data; 

		//준비2 : 생산실적 마스터 내용 읽기 : mfg_product_master
		com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
		productMT = odrDAO.readMfgProductMasterRead(mfg_no,item_code,factory_no);
		if(productMT.getPid().length() == 0) {
			data = "생산실적을 누적관리할 데이터가 없습니다. [mfg_product_master]"; return data;
		}

		//준비3 : 기존등록된 총생산수량과 수정할 총생산수량의 차이를 구한다.
		//차이만큼 부품수량을 가감한다.
		com.anbtech.mm.entity.mfgProductItemTable productIT = new com.anbtech.mm.entity.mfgProductItemTable();
		productIT = odrDAO.readMfgProductItemRead(pid);
		diff_total_count = Integer.parseInt(total_count) - productIT.getTotalCount();	//+:추가,-:삭감 의미
		diff_good_count = Integer.parseInt(good_count) - productIT.getGoodCount();		//+:추가,-:삭감 의미
		diff_bad_count = Integer.parseInt(bad_count) - productIT.getBadCount();			//+:추가,-:삭감 의미
		//System.out.println(diff_total_count+" : "+diff_good_count+" : "+diff_bad_count);
		
		//등록1 [생산실적] : 생산실적 제품등록 테이블에 수정하기 : mfg_product_item
		update = "UPDATE mfg_product_item SET total_count='"+total_count+"',";
		update += "good_count='"+good_count+"',bad_count='"+bad_count+"',";
		if(bad_count.equals("0")) {		//불량수량이 있다가 수정으로 없을경우 불량형태및 원인을 없애
			update += "bad_type='',bad_note='' where pid='"+pid+"'";
		} else {
			update += "bad_type='"+bad_type+"',bad_note='"+bad_note+"' where pid='"+pid+"'";
		}
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//등록2 [생산실적] : 생산실적 제품등록 마스터 테이블에 수정하기 : mfg_product_master
		rst_total_count = productMT.getTotalCount() + diff_total_count;
		rst_good_count = productMT.getGoodCount() + diff_good_count;
		rst_bad_count = productMT.getBadCount() + diff_bad_count;

		update = "UPDATE mfg_product_master SET total_count='"+rst_total_count+"',";
		update += "good_count='"+rst_good_count+"',bad_count='"+rst_bad_count+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//등록3 [생산실적] : 최종제품수량 등록일 경우는 수량도 같이 수정한다. : mfg_master
		com.anbtech.mm.entity.mfgMasterTable mfgMT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgMT = odrDAO.readMfgMasterItem(gid);
		if(mfgMT.getItemCode().equals(item_code)) {		//최종제품생산코드
			rst_total_count = mfgMT.getRstTotalCount() + diff_total_count;
			rst_good_count = mfgMT.getRstGoodCount() + diff_good_count;
			rst_bad_count = mfgMT.getRstBadCount() + diff_bad_count;
			working_count = mfgMT.getMfgCount() - rst_total_count; 

			update = "UPDATE mfg_master SET rst_total_count='"+rst_total_count+"',";
			update += "rst_good_count='"+rst_good_count+"',rst_bad_count='"+rst_bad_count+"',";
			update += "working_count='"+working_count+"' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		//판단1 : 총생산수량이 차이가 없으면  양품수량,불량수량,불량형태,불량원인을 수정한것으로 간주하고 빠져나감.
		if(diff_total_count == 0) {
			data = "생산실적이 수정되었습니다.";
			return data;
		}
		
		//등록4 [부품수량] : 재고관리의 재고수량 테이블에 update하기: st_item_stock_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//예약수량
			if(rev_count > 0) {	
				//기존 제조에 사용한 수량 구하기
				where = "where item_code='"+item[i][0]+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				//제조에서 사용한 수정할 차이수량 구하기 : BOM수량 * 제조수정할 차이수량
				use_count = Integer.parseInt(item[i][5]) * diff_total_count;
				if(use_count > rev_count) use_count = rev_count;		//예약수량을 넘지못함.

				//updte할 수량 (기존출고량 - 제조소요량)
				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - use_count);

				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][0]+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//등록5 [부품수량] : 생산관리의 재고수량 테이블에 실제조수량 더하기: mfg_inout_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//예약수량
			if(rev_count > 0) {	
				//기존 제조수량 구하기
				where = "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				where += "factory_no='"+factory_no+"'";
				used_cnt = odrDAO.getColumData("MFG_INOUT_MASTER","use_count",where);
				if(used_cnt.length() == 0) used_cnt = "0";

				//제조에서 사용한 수정할 차이수량 구하기 : BOM수량 * 제조수정할 차이수량
				use_count = Integer.parseInt(item[i][5]) * diff_total_count;
				if(use_count > rev_count) use_count = rev_count;		//예약수량을 넘지못함.

				//updte할 수량 (기존제조량 + 제조소요량)
				use_count = Integer.parseInt(used_cnt) + use_count;

				update = "UPDATE mfg_inout_master SET use_count='"+use_count+"' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				update += "factory_no='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		data = "생산실적이 수정되었습니다.";
		return data;
	}

	//*******************************************************************
	//  공정별 생산실적 마감하기 : product_confirm
	//*******************************************************************/	
	public String closeProductItem(String login_id,String login_name,String mfg_no,String item_code,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",used_cnt="",output_status="";
		String product="",mrp_no="",mps_no="",rst_good_count="",re_work="",factory_name="";

		//준비1 : 해당작업지시의 최종생산품코드를 구하기
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		product = odrDAO.getColumData("MFG_MASTER","item_code",where);
		rst_good_count = odrDAO.getColumData("MFG_MASTER","rst_good_count",where);
		re_work = odrDAO.getColumData("MFG_MASTER","re_work",where);
		factory_name = odrDAO.getColumData("MFG_MASTER","factory_name",where);

		//--------------
		//공통 마감처리
		//--------------
		//최종 생산제품일경우 마감처리는 하부구조의 모든것이 마감되었을때 
		//처리가능토록 함.
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and item_code='"+item_code+"'";
		output_status = odrDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		if(product.equals(item_code) && output_status.equals("1")) {
			//전체공정 수량 구하기
			query = "SELECT count(*) FROM mfg_product_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			int t = odrDAO.getTotalCount(query)-1;

			//마감처리된 공정수량구하기
			query = "SELECT count(*) FROM mfg_product_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' ";
			query += "and output_status='2'";
			int c = odrDAO.getTotalCount(query);

			//비교하여 하부구조 마감되었나 판단하기 (하부구조가 전부마감처리 되었을때 가능)
			if(t != c) {
				data = "최종 생산마감은 하부공정이 전부 마감처리되었을때 가능합니다.";
				return data;
			}
		}

		//공통등록1 : 해당공정별 작업지시 마감처리 : [mfg_operator : op_order = '3']
		update = "UPDATE mfg_operator SET op_order='3' ";
		update += "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("공통등록1 update : " + update);
		odrDAO.executeUpdate(update);

		//공통등록2 : 해당공정별 실적등록 마스터 마감처리 : [mfg_product_master : output_status = '2']
		update = "UPDATE mfg_product_master SET output_status='2',output_date='"+anbdt.getDateNoformat()+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("공통등록2 update : " + update);
		odrDAO.executeUpdate(update);

		//공통등록3 : 해당공정별 실적등록 개별 마감처리 : [mfg_product_item : output_status = '2']
		update = "UPDATE mfg_product_item SET output_status='2' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("공통등록3 update : " + update);
		odrDAO.executeUpdate(update);

		data = "개별 공정마감이 진행되었습니다.";
		
		//------------------------------------
		//최종 생산제품으로 작업지시 전체 마감
		//------------------------------------
		if(product.equals(item_code)) {
			//준비1 : 작지별 부품입출고 현황 배열에 담기
			//0:pid,1:gid,2:mfg_no,3:item_code,4:item_name,5:item_spec,6:item_unit
			//7:inout_status,8:reserve_count,9:req_count,10:receive_count,11:use_count
			//12:rest_count,13:factory_no,14:factory_name
			arrayMfgInOutMasterList(mfg_no,factory_no);

			//준비2 : MRP NO구하기
			where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			mrp_no = odrDAO.getColumData("MFG_MASTER","mrp_no",where);

			//준비3 : MPS NO구하기
			where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
			mps_no = odrDAO.getColumData("MRP_MASTER","mps_no",where);

			//진행등록 1 : mfg_master [order_status='6']
			update = "UPDATE mfg_master SET order_status='6' ";
			update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
			update += " and factory_no='"+factory_no+"'";
			//System.out.println("진행등록1 update : " + update);
			odrDAO.executeUpdate(update);
			
			//진행등록 2 : mps_master [msp_status='7']
			update = "UPDATE mps_master SET mps_status='7' ";
			update += "where mps_no='"+mps_no+"' and item_code='"+item_code+"'";
			update += " and factory_no='"+factory_no+"'";
			//System.out.println("진행등록 2 update : " + update);
			odrDAO.executeUpdate(update);

			//잔고처리 : 생산관리 부품입출고관리 [mfg_inout_master]
			for(int i=0; i<an; i++) {
				update = "UPDATE mfg_inout_master SET rest_count='"+item[i][12]+"',inout_status='1' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][3]+"'";
				update += " and factory_no='"+factory_no+"'";
				//System.out.println("잔고처리 update : " + update);
				odrDAO.executeUpdate(update);
			}

			//잔고차감 : 재고관리 부품출고수량처리 [st_item_stock_master]
			String delivery_quantity="";
			for(int i=0; i<an; i++) {
				where = "where item_code='"+item[i][3]+"' and factory_code='"+factory_no+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - Integer.parseInt(item[i][12])); 
				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][3]+"' and factory_code='"+factory_no+"'";
				//System.out.println("잔고차감 update : " + update);
				odrDAO.executeUpdate(update);
			}

/*			//2004.08.25 재고관리 전송 기능 삭제함.
			//생산실적 수입검사전 입고 : 재고관리 부품출고수량처리 [st_item_stock_master]
			where = "where item_code='"+product+"' and factory_code='"+factory_no+"'";
			String into_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","into_quantity",where);

			if(into_quantity.length() == 0) {		//신규등록
				into_quantity = rst_good_count;
				String[] part_info = new String[2];
				part_info = odrDAO.getItemInfo(product);	//이름,규격
				
				input = "INSERT INTO st_item_stock_master(item_code,item_name,item_desc,";
				input += "stock_unit,stock_quantity,into_quantity,good_quantity,bad_quantity,";
				input += "outto_quantity,delivery_quantity,resonable_quantity,unit_type,unit_cost,";
				input += "last_updated_date,factory_code,factory_name) values('";
				input += product+"','"+part_info[0]+"','"+part_info[1]+"','"+"EA"+"','"+"0"+"','"+into_quantity+"','";
				input += "0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"1"+"','"+"0"+"','";
				input += anbdt.getDateNoformat()+"','"+factory_no+"','"+""+"')";
				//System.out.println("input : " + input);
				odrDAO.executeUpdate(input);
			} else {								//update
				into_quantity = Integer.toString(Integer.parseInt(into_quantity) + Integer.parseInt(rst_good_count)); 
				update = "UPDATE st_item_stock_master SET into_quantity='"+into_quantity+"' ";
				update += "where item_code='"+product+"' and factory_code='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
*/

			//품질로 생산실적정보 등록하기
			if(re_work.equals("작업")) {
				String[] part_info = new String[2];
				part_info = odrDAO.getItemInfo(product);	//제품 이름,규격

				String[] div_info = new String[2];
				div_info = odrDAO.getDivInfo(login_id);		//부서코드,부서명

				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				String req_inspect_no = qcDAO.getRequestNo("TST");
				qcDAO.saveInspectionInfo(req_inspect_no,product,part_info[0],part_info[1],"","FMT",mfg_no,rst_good_count,div_info[0],div_info[1],login_id,login_name,factory_no,factory_name,"S01");
			}
			
			data = "생산실적이 마감되었습니다.";
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		ST_RESERVED_ITEM_INFO 에 관한 메소드
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 공정별 ST_RESERVED_ITEM_INFO 출고의뢰내용 배열에 담기
	//*******************************************************************/	
	public String arrayMfgReqItemList(String mfg_req_no,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getMfgReqItemConfirmList(mfg_req_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="부품내용이 없습니다."; return data; }
		item = new String[cnt][10];

		mfgReqItemTable table = new mfgReqItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgReqItemTable)item_iter.next();

			item[an][0] = table.getPid();
			item[an][1] = table.getMfgNo();
			item[an][2] = table.getMfgReqNo();
			item[an][3] = table.getItemCode();
			item[an][4] = table.getItemName();
			item[an][5] = table.getItemSpec();
			item[an][6] = table.getItemType();
			item[an][7] = table.getItemUnit();
			item[an][8] = Integer.toString(table.getReqCount());						
			item[an][9] = table.getFactoryNo();
			
			//System.out.println(item[an][0]+" : "+item[an][3]+ " : "+item[an][8]+" : "+item[an][9]);
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		MFG ITEM 에 관한 메소드
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 공정별 MFG ITEM 단단계 내용을 읽어 배열에 담기 : 출고가능부품 찾기용
	//*******************************************************************/	
	public String arrayMfgItemList(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		String data = "";
		int out_cnt = 0;			//출고가능 수량

		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getSingleMfgItems(mfg_no,assy_code,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="부품내용이 없습니다."; return data; }
		item = new String[cnt][10];

		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getItemCode();
			item[an][1] = table.getItemName();
			item[an][2] = table.getItemSpec();
			item[an][3] = table.getItemUnit();
			item[an][4] = table.getItemType();
			item[an][5] = Integer.toString(table.getReserveCount());						//예약수량
			item[an][6] = Integer.toString(table.getRequestCount());						//기출고의뢰수량
			item[an][7] = Integer.toString(table.getReserveCount()-table.getRequestCount());//출고가능수량
			item[an][8] = table.getFactoryNo();
			item[an][9] = table.getFactoryName();
			if(item[an][7].equals("0")) out_cnt++;
			//System.out.println(item[an][0]+" : "+item[an][5]+ " : "+item[an][6]+" : "+item[an][7]);
			an++;
		}

		//출고가능 수량이 있는지 판단하기
		if(out_cnt == an) data = "출고가능한 부품이 없습니다.";

		return data;
	}

	//*******************************************************************
	// 공정별 MFG ITEM 단단계 내용을 읽어 배열에 담기 : 생산실적입력용
	//*******************************************************************/	
	public String getMfgItemList(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getSingleMfgItems(mfg_no,assy_code,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="부품내용이 없습니다."; return data; }
		item = new String[cnt][10];

		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getItemCode();
			item[an][1] = table.getItemName();
			item[an][2] = table.getItemSpec();
			item[an][3] = table.getItemUnit();
			item[an][4] = table.getItemType();
			item[an][5] = Integer.toString(table.getDrawCount());							//BOM수량
			item[an][6] = Integer.toString(table.getReserveCount());						//예약수량
			item[an][7] = Integer.toString(table.getRequestCount());						//기출고의뢰수량
			item[an][8] = table.getFactoryNo();
			item[an][9] = table.getFactoryName();
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		부품입출고에 관한 메소드 : mfg_inout_master
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 작지별 mfg_inout_master 입출고내용 배열에 담기 : 생산마감 잔고처리용
	//*******************************************************************/	
	public String arrayMfgInOutMasterList(String mfg_no,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getMfgInOutMasterList(mfg_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="부품내용이 없습니다."; return data; }
		item = new String[cnt][15];

		mfgInOutMasterTable table = new mfgInOutMasterTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgInOutMasterTable)item_iter.next();

			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getMfgNo();
			item[an][3] = table.getItemCode();
			item[an][4] = table.getItemName();
			item[an][5] = table.getItemSpec();
			item[an][6] = table.getItemUnit();
			item[an][7] = table.getInOutStatus();
			item[an][8] = Integer.toString(table.getReserveCount());
			item[an][9] = Integer.toString(table.getReqCount());
			item[an][10] = Integer.toString(table.getReceiveCount());
			item[an][11] = Integer.toString(table.getUseCount());
			item[an][12] = Integer.toString(table.getReceiveCount() - table.getUseCount());
			item[an][13] = table.getFactoryNo();
			item[an][14] = table.getFactoryName();
			
			//System.out.println(item[an][3]+" : "+item[an][8]+ " : "+item[an][10]+" : "+item[an][11]+" : "+item[an][12]);
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		제조중인 제품에 대한 메소드
	//			
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 재공중인 자재LIST [원가산출할때 활용]
	 **********************************************************************/
	public ArrayList getUniqueRunningItemList(String model_code,String mfg_no,String factory_no) throws Exception
	{
		//model_code가 있을때 해당되는 mfg_no구하기
		String mfg_list = odrDAO.getRunningMfgNo(model_code,factory_no);
		if(mfg_no.length() == 0) mfg_no = mfg_list;

		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getRunningItemList(mfg_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//배열에 담기
		mfgInOutMasterTable table = new mfgInOutMasterTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mfgInOutMasterTable)item_iter.next();
			data[n][0] = table.getItemCode();
			data[n][1] = table.getItemName();
			data[n][2] = table.getItemSpec();
			data[n][3] = table.getItemUnit();
			data[n][4] = Integer.toString(table.getReserveCount());
			data[n][5] = Integer.toString(table.getReqCount());
			data[n][6] = Integer.toString(table.getReceiveCount());
			data[n][7] = Integer.toString(table.getUseCount());
			data[n][8] = Integer.toString(table.getRestCount());
			data[n][9] = table.getFactoryNo();
			data[n][10] = "0";					//동일한 부품갯수를 입력하기 위해
			//System.out.println(data[n][0]+":"+data[n][4]+":"+data[n][6]);
			n++;
		}
		//Unique한 자품목코드로 갯수 출력하기
		bubbleUniqueSortAsc(data,0);
		
		//동일부품으로 정렬함에 따라 빈배열 없애고 단가입력하기[배열추가]
		//10:갯수,11:표준단가,12:평균단가,13:현재단가,14:표준총액,15:평균총액,16:현재총액
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][0]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//표준단가
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//평균단가
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//현재단가
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List로 담기
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][0]);		//품목코드
			price.setItemName(t[i][1]);		//품목이름
			price.setItemDesc(t[i][2]);		//품목규격
			price.setItemCount(t[i][6]);	//품목수량
			price.setStdPrice(t[i][11]);	//표준단가
			price.setAvePrice(t[i][12]);	//평균단가
			price.setCurPrice(t[i][13]);	//현재단가
			price.setStdSum(t[i][14]);		//표준단가 총액
			price.setAveSum(t[i][15]);		//평균단가 총액
			price.setCurSum(t[i][16]);		//현재단가 총액

			price_list.add(price);
		}

/*		//출력해보기
		primeCostTable view = new primeCostTable();
		Iterator price_iter = price_list.iterator();
		while(price_iter.hasNext()) {
			view = (primeCostTable)price_iter.next();
			System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;
	}

	/***************************************************************************
	 *  거품정렬로 배열의 원소를 Unique하게 정렬한다. : 내림차순
	 ****************************************************************************/
	 private void bubbleUniqueSortAsc(String[][] b,int sort_no)
	{
		 int b_len = b.length;			//배열의 갯수
		 int e_len = b[0].length;		//배열 원소의 갯수

		 //정렬하기
		 int rn = sort.bubbleSortStringMultiAsc(b,sort_no);
		 if(rn != 1) return;

		//unique한 내용끼리 묶어 배열에 담기
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int rev=0;				//예약수량
		int req=0;				//요구수량
		int rec=0;				//출고수량
		int use=0;				//사용수량
		int rst=0;				//남은수량
		int an = 0;				//배열의 갯수
		for(int i=0; i<=b_cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < b_cnt) {
				//sort_no와 같으면 숫자만 기록
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					rev += Integer.parseInt(b[i][4]);				//예약수량
					req += Integer.parseInt(b[i][5]);				//요구수량
					rec += Integer.parseInt(b[i][6]);				//출고수량
					use += Integer.parseInt(b[i][7]);				//사용수량
					rst += Integer.parseInt(b[i][8]);				//남은수량
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					rev += Integer.parseInt(b[i][4]);				//예약수량
					req += Integer.parseInt(b[i][5]);				//요구수량
					rec += Integer.parseInt(b[i][6]);				//출고수량
					use += Integer.parseInt(b[i][7]);				//사용수량
					rst += Integer.parseInt(b[i][8]);				//남은수량	
					
					for(int e=0; e<4; e++) a[an][e] = b[i][e];
					a[an][4] = Integer.toString(rev);	
					a[an][5] = Integer.toString(req);	
					a[an][6] = Integer.toString(rec);	
					a[an][7] = Integer.toString(use);	
					a[an][8] = Integer.toString(rst);	
					a[an][9] = b[i][9];
					a[an][10] = b[i][10];

					an++;
					rev=req=rec=use=rst=0;				
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				rev += Integer.parseInt(b[i][4]);				//예약수량
				req += Integer.parseInt(b[i][5]);				//요구수량
				rec += Integer.parseInt(b[i][6]);				//출고수량
				use += Integer.parseInt(b[i][7]);				//사용수량
				rst += Integer.parseInt(b[i][8]);				//남은수량
					
				for(int e=0; e<4; e++) a[an][e] = b[i][e];
				a[an][4] = Integer.toString(rev);	
				a[an][5] = Integer.toString(req);	
				a[an][6] = Integer.toString(rec);	
				a[an][7] = Integer.toString(use);	
				a[an][8] = Integer.toString(rst);	
				a[an][9] = b[i][9];
				a[an][10] = b[i][10];

				an++;
			} //else
		} //for

		//원래의 배열에 원소갯수를 1개더 늘려 맨마지막에 정렬갯수 담기
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//옮기기
			
	}


	//--------------------------------------------------------------------
	//
	//		기타 메소드
	//			
	//			
	//---------------------------------------------------------------------
	public void setDeliveryNo(String delivery_no)	{ this.delivery_no = delivery_no;	}
	public String getDeliveryNo()					{ return this.delivery_no;			}
		
}



