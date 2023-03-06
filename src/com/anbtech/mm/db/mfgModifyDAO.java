package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//포멧
	private com.anbtech.mm.db.mrpModifyDAO mrpDAO = null;
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기 (BOM정보 담기)
	private mfgItemTable mfgIT = null;				//help class (제오오더산출 BOM내용)
	private mrpItemTable mrpIT = null;				//help class (소요량 산출BOM 내용)
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mfgModifyDAO(Connection con) 
	{
		this.con = con;
		mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		MRP MASTER 에 관한 메소드 정의
	//		[MFG 접수내용]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MFG접수(MRP MASTER)로 부터 MFG MASTER정보 만들기 
	//*******************************************************************/	
	public mfgMasterTable readRequestMfgMasterItem(String pid) throws Exception
	{
		//MRP MASTER로 부터 데이터 읽기
		com.anbtech.mm.entity.mrpMasterTable readT = new com.anbtech.mm.entity.mrpMasterTable();
		readT = mrpDAO.readMrpMasterItem(pid);

		//MFG MASTER에 입력할 데이터 형태로 담기
		mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
		table.setPid("");	
		table.setMrpNo(readT.getMrpNo());
		table.setMfgNo("");
		table.setModelCode(readT.getModelCode());
		table.setModelName(readT.getModelName());
		table.setFgCode(readT.getFgCode());
		table.setItemCode(readT.getItemCode());
		table.setItemName(readT.getItemName());
		table.setItemSpec(readT.getItemSpec());
		table.setItemUnit(readT.getItemUnit());
		table.setMfgCount(readT.getPCount());
		table.setBuyType("M");
		table.setFactoryNo(readT.getFactoryNo());
		table.setFactoryName(readT.getFactoryName());
		table.setCompCode("");
		table.setCompName("");
		table.setCompUser("");
		table.setCompTel("");
		table.setOrderStatus("");
		table.setOrderType("MRP");
		table.setRegDate("");
		table.setRegId("");
		table.setRegName("");
		table.setPlanDate(readT.getPlanDate());
		table.setOrderStartDate("");
		table.setOrderEndDate("");
		table.setOpStartDate("");
		table.setOpEndDate("");
		table.setOrderDate("");
		table.setReWork("작업");
		table.setLinkMfgNo("");
		table.setRstTotalCount(0);
		table.setRstGoodCount(0);
		table.setRstBadCount(0);
		table.setWorkingCount(0);
		table.setRstPassCount(0);
		table.setRstFailCount(0);
		
		return table;
	}

	//*******************************************************************
	// MRP을 제조오더에서 접수가능한 리스트 구하기 
	// mrp_status = '2','3','4' 이면서 mfg_order='0'인것
	//*******************************************************************/	
	public ArrayList getMfgRequestList (String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mrpMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mrp_master where mfg_order='0' and mrp_status in('2','3','4') ";
		query += "and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mrp_master where mfg_order='0' and mrp_status in('2','3','4') and "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"' ";
		query += "order by factory_no,mrp_no asc";
		rs = stmt.executeQuery(query);

		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//출력할 페이지

		//전체 page 구하기
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//페이지에 따른 query 계산하기
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//데이터 담기
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mrpMasterTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				String mrp_no = "<a href=\"javascript:mrpView('"+pid+"');\">"+rs.getString("mrp_no")+"</a>";
				table.setMrpNo(mrp_no);

				table.setMrpStartDate(anbdt.getSepDate(rs.getString("mrp_start_date"),"/"));
				table.setMrpEndDate(anbdt.getSepDate(rs.getString("mrp_end_date"),"/"));
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPCount(Integer.parseInt(rs.getString("p_count")));
				table.setPlanDate(rs.getString("plan_date"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMrpStatus(rs.getString("mrp_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setAppDate(anbdt.getSepDate(rs.getString("app_date"),"-"));
				table.setAppId(rs.getString("app_id"));
				table.setRegDivCode(rs.getString("reg_div_code"));
				table.setRegDivName(rs.getString("reg_div_name"));
				table.setRegId(rs.getString("reg_id"));
				table.setRegName(rs.getString("reg_name"));
				table.setAppNo(rs.getString("app_no"));
				table.setPuDevDate(rs.getString("pu_dev_date"));
				table.setPuReqNo(rs.getString("pu_req_no"));
				table.setStockLink(rs.getString("stock_link"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MRP을 제조오더에서 접수된것 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public mrpMasterTable getRequestDisplayPage(String factory_no,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mrpMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mrp_master where mfg_order='0' and mrp_status in('2','3','4') ";
		query += "and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mrp_master where mfg_order='0' and mrp_status in('2','3','4') and "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"' ";
		query += "order by factory_no,mrp_no asc";
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgInfoServlet?&mode=mfg_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgInfoServlet?&mode=mfg_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgInfoServlet?&mode=mfg_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist에 담기
		table = new mrpMasterTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		MRP ITEM 에 관한 메소드 정의
	//		[MRP ITEM을 MFG ITEM 및 MFG OPERATOR에 등록하기 위해]
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 정전개 다단계 Item 소요량 출력 
	 * 조건 : MRP NO,ASSY코드,레벨
	 * 구분(order_type) : MRP,MANUAL(긴급오더로 mrp_no=""로 리턴한다)
	 *********************************************************************/
	private void saveMrpItems(String factory_no,String mrp_no,String level_no,String assy_code,
		String order_type) throws Exception
	{
		//변수 초기화
		String lno = "",pid="",item_code="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MRP_ITEM ";
		query += "where level_no = '"+level_no+"' and assy_code = '"+assy_code+"' ";
		query += "and mrp_no = '"+mrp_no+"' and factory_no='"+factory_no+"' order by item_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mrpIT = new mrpItemTable();
			pid = rs.getString("pid");
			mrpIT.setPid(pid);	
			mrpIT.setGid(rs.getString("gid"));

			if(order_type.equals("MRP")) mrpIT.setMrpNo(rs.getString("mrp_no"));	
			else mrpIT.setMrpNo("");

			mrpIT.setAssyCode(rs.getString("assy_code"));	
			mrpIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mrpIT.setItemCode(rs.getString("item_code"));
			mrpIT.setItemName(rs.getString("item_name"));	
			mrpIT.setItemSpec(rs.getString("item_spec"));	
			mrpIT.setItemType(rs.getString("item_type"));	
			mrpIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mrpIT.setMrpCount(Integer.parseInt(rs.getString("mrp_count")));	
			mrpIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));	
			mrpIT.setStockCount(Integer.parseInt(rs.getString("stock_count")));	
			mrpIT.setOpenCount(Integer.parseInt(rs.getString("open_count")));	
			mrpIT.setPlanCount(Integer.parseInt(rs.getString("plan_count")));	
			mrpIT.setAddCount(Integer.parseInt(rs.getString("add_count")));	
			mrpIT.setMrsCount(Integer.parseInt(rs.getString("mrs_count")));		
			mrpIT.setItemUnit(rs.getString("item_unit"));	
			mrpIT.setBuyType(rs.getString("buy_type"));
			mrpIT.setFactoryNo(rs.getString("factory_no"));
			mrpIT.setFactoryName(rs.getString("factory_name"));
			mrpIT.setPuDevDate(rs.getString("pu_dev_date"));	
			mrpIT.setPuReqNo(rs.getString("pu_req_no"));
			
			item_list.add(mrpIT); 
			
			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			saveMrpItems(rs.getString("factory_no"),mrp_no,lno,rs.getString("item_code"),order_type);
		}
		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 다단계 item 소요량 출력 
	 * 하부구조 전체
	 * 구분(order_type) : MRP,MANUAL(긴급오더로 mrp_no=""로 리턴한다)
	 *********************************************************************/
	public ArrayList getMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code,
		String order_type) throws Exception
	{
		item_list = new ArrayList();
		saveMrpItems(factory_no,mrp_no,level_no,assy_code,order_type);	
		return item_list;
	}

	//*******************************************************************
	// MRP ITEM에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨 
	// 용도 : ASSY단위별 MFG OPERATOR내용을 입력하기위해
	// * 구분(order_type) : MRP,MANUAL(긴급오더로 mrp_no=""로 리턴한다)
	//*******************************************************************/	
	public ArrayList getAssyOpList(String factory_no,String mrp_no,String order_type) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mrpItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct assy_code,level_no from mrp_item where mrp_no='"+mrp_no;
		query += "' and factory_no='"+factory_no+"' order by level_no,assy_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mrpItemTable();	

				if(order_type.equals("MRP")) table.setMrpNo(mrp_no);	
				else table.setMrpNo("");

				table.setAssyCode(rs.getString("assy_code"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				//System.out.println(mrp_no+":"+rs.getString("assy_code")+":"+rs.getString("level_no"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM ITEM에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨 
	// 용도 : ASSY단위별 MFG OPERATOR내용을 입력하기위해
	// * 구분(order_type) : MRP,MANUAL(긴급오더로 mrp_no=""로 리턴한다)
	//*******************************************************************/	
	public ArrayList getMbomAssyOpList(String fg_code) throws Exception
	{
		//관련정보 구하기
		String where = "where fg_code='"+fg_code+"'";
		String gid = getColumData("MBOM_MASTER","pid",where);

		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mrpItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct parent_code,level_no from mbom_item where gid='"+gid;
		query += "' and level_no != '0' order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mrpItemTable();	
				
				table.setMrpNo("");
				table.setAssyCode(rs.getString("parent_code"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				//System.out.println(mrp_no+":"+rs.getString("assy_code"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM ITEM에서 해당 모델의 1개의 Assy코드만 찾기 : 첫번호는 1로 시작됨 
	// 용도 : ASSY단위별 MFG OPERATOR내용을 입력하기위해
	// * 구분(order_type) : MRP,MANUAL(긴급오더로 mrp_no=""로 리턴한다)
	//*******************************************************************/	
	public ArrayList getMbomAssyOp(String fg_code,String item_code) throws Exception
	{
		//관련정보 구하기
		String where = "where fg_code='"+fg_code+"'";
		String gid = getColumData("MBOM_MASTER","pid",where);

		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mrpItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct parent_code,level_no from mbom_item where gid='"+gid;
		query += "' and parent_code = '"+item_code+"' order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
				table = new mrpItemTable();	
				
				table.setMrpNo("");
				table.setAssyCode(rs.getString("parent_code"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				//System.out.println(mrp_no+":"+rs.getString("assy_code"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		MFG MASTER 에 관한 메소드 정의
	//		
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MFG MASTER정보 읽기 
	//*******************************************************************/	
	public mfgMasterTable readMfgMasterItem(String pid,String factory_no) throws Exception
	{
		//변수 초기화
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
		
		query = "SELECT * FROM mfg_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMrpNo(rs.getString("mrp_no"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setModelCode(rs.getString("model_code"));
			table.setModelName(rs.getString("model_name"));
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));
			table.setBuyType(rs.getString("buy_type"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setCompCode(rs.getString("comp_code"));
			table.setCompName(rs.getString("comp_name"));
			table.setCompUser(rs.getString("comp_user"));
			table.setCompTel(rs.getString("comp_tel"));
			table.setOrderStatus(rs.getString("order_status"));
			table.setOrderType(rs.getString("order_type"));
			table.setRegDate(rs.getString("reg_date"));
			table.setRegId(rs.getString("reg_id"));
			table.setRegName(rs.getString("reg_name"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setOrderStartDate(anbdt.getSepDate(rs.getString("order_start_date"),"/"));
			table.setOrderEndDate(anbdt.getSepDate(rs.getString("order_end_date"),"/"));
			table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"/"));
			table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"/"));
			table.setOrderDate(rs.getString("order_date"));
			table.setReWork(rs.getString("re_work"));
			table.setLinkMfgNo(rs.getString("link_mfg_no"));
			table.setRstTotalCount(Integer.parseInt(rs.getString("rst_total_count")));
			table.setRstGoodCount(Integer.parseInt(rs.getString("rst_good_count")));
			table.setRstBadCount(Integer.parseInt(rs.getString("rst_bad_count")));
			table.setWorkingCount(Integer.parseInt(rs.getString("working_count")));
			table.setRstPassCount(Integer.parseInt(rs.getString("rst_pass_count")));
			table.setRstFailCount(Integer.parseInt(rs.getString("rst_fail_count")));
		} else {
			//공장명 구하기
			where = "where factory_code='"+factory_no+"'";
			String factory_name = getColumData("factory_info_table","factory_name",where);

			table.setPid("");	
			table.setMrpNo("");
			table.setMfgNo("");
			table.setModelCode("");
			table.setModelName("");
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setItemUnit("");
			table.setMfgCount(0);
			table.setBuyType("");
			table.setFactoryNo(factory_no);
			table.setFactoryName(factory_name);
			table.setCompCode("");
			table.setCompName("");
			table.setCompUser("");
			table.setCompTel("");
			table.setOrderStatus("");
			table.setOrderType("MANUAL");
			table.setRegDate("");
			table.setRegId("");
			table.setRegName("");
			table.setPlanDate("");
			table.setOrderStartDate("");
			table.setOrderEndDate("");
			table.setOpStartDate("");
			table.setOpEndDate("");
			table.setOrderDate("");
			table.setReWork("");
			table.setLinkMfgNo("");
			table.setRstTotalCount(0);
			table.setRstGoodCount(0);
			table.setRstBadCount(0);
			table.setWorkingCount(0);
			table.setRstPassCount(0);
			table.setRstFailCount(0);
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// 해당관리코드로 해당 MFG MASTER정보 읽기 
	//*******************************************************************/	
	public mfgMasterTable getMfgMasterItem(String mfg_no,String factory_no) throws Exception
	{
		//변수 초기화
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
		
		query = "SELECT * FROM mfg_master where mfg_no ='"+mfg_no+"' and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMrpNo(rs.getString("mrp_no"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setModelCode(rs.getString("model_code"));
			table.setModelName(rs.getString("model_name"));
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));
			table.setBuyType(rs.getString("buy_type"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setCompCode(rs.getString("comp_code"));
			table.setCompName(rs.getString("comp_name"));
			table.setCompUser(rs.getString("comp_user"));
			table.setCompTel(rs.getString("comp_tel"));
			table.setOrderStatus(rs.getString("order_status"));
			table.setOrderType(rs.getString("order_type"));
			table.setRegDate(rs.getString("reg_date"));
			table.setRegId(rs.getString("reg_id"));
			table.setRegName(rs.getString("reg_name"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setOrderStartDate(anbdt.getSepDate(rs.getString("order_start_date"),"/"));
			table.setOrderEndDate(anbdt.getSepDate(rs.getString("order_end_date"),"/"));
			table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"/"));
			table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"/"));
			table.setOrderDate(rs.getString("order_date"));
			table.setReWork(rs.getString("re_work"));
			table.setLinkMfgNo(rs.getString("link_mfg_no"));
			table.setRstTotalCount(Integer.parseInt(rs.getString("rst_total_count")));
			table.setRstGoodCount(Integer.parseInt(rs.getString("rst_good_count")));
			table.setRstBadCount(Integer.parseInt(rs.getString("rst_bad_count")));
			table.setWorkingCount(Integer.parseInt(rs.getString("working_count")));
			table.setRstPassCount(Integer.parseInt(rs.getString("rst_pass_count")));
			table.setRstFailCount(Integer.parseInt(rs.getString("rst_fail_count")));
		} else {
			//공장명 구하기
			where = "where factory_code='"+factory_no+"'";
			String factory_name = getColumData("factory_info_table","factory_name",where);

			table.setPid("");	
			table.setMrpNo("");
			table.setMfgNo("");
			table.setModelCode("");
			table.setModelName("");
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setItemUnit("");
			table.setMfgCount(0);
			table.setBuyType("");
			table.setFactoryNo(factory_no);
			table.setFactoryName(factory_name);
			table.setCompCode("");
			table.setCompName("");
			table.setCompUser("");
			table.setCompTel("");
			table.setOrderStatus("");
			table.setOrderType("MANUAL");
			table.setRegDate("");
			table.setRegId("");
			table.setRegName("");
			table.setPlanDate("");
			table.setOrderStartDate("");
			table.setOrderEndDate("");
			table.setOpStartDate("");
			table.setOpEndDate("");
			table.setOrderDate("");
			table.setReWork("");
			table.setLinkMfgNo("");
			table.setRstTotalCount(0);
			table.setRstGoodCount(0);
			table.setRstBadCount(0);
			table.setWorkingCount(0);
			table.setRstPassCount(0);
			table.setRstFailCount(0);
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MFG MASTER 리스트 구하기
	//*******************************************************************/	
	public ArrayList getMfgMasterList (String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mfgMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mfg_master where "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mfg_master where "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"' ";
		query += "order by pid desc";
		rs = stmt.executeQuery(query);

		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//출력할 페이지

		//전체 page 구하기
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//페이지에 따른 query 계산하기
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//데이터 담기
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgMasterTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setMrpNo(rs.getString("mrp_no"));
				String mfg_no = "<a href=\"javascript:mfgView('"+pid+"');\">"+rs.getString("mfg_no")+"</a>";
				table.setMfgNo(mfg_no);

				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));
				table.setBuyType(rs.getString("buy_type"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setCompCode(rs.getString("comp_code"));
				table.setCompName(rs.getString("comp_name"));
				table.setCompUser(rs.getString("comp_user"));
				table.setCompTel(rs.getString("comp_tel"));
				table.setOrderStatus(rs.getString("order_status"));
				table.setOrderType(rs.getString("order_type"));
				table.setRegDate(rs.getString("reg_date"));
				table.setRegId(rs.getString("reg_id"));
				table.setRegName(rs.getString("reg_name"));
				table.setPlanDate(rs.getString("plan_date"));
				table.setOrderStartDate(anbdt.getSepDate(rs.getString("order_start_date"),"/"));
				table.setOrderEndDate(anbdt.getSepDate(rs.getString("order_end_date"),"/"));
				table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"/"));
				table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"/"));
				table.setOrderDate(rs.getString("order_date"));
				table.setReWork(rs.getString("re_work"));
				table.setLinkMfgNo(rs.getString("link_mfg_no"));
				table.setRstTotalCount(Integer.parseInt(rs.getString("rst_total_count")));
				table.setRstGoodCount(Integer.parseInt(rs.getString("rst_good_count")));
				table.setRstBadCount(Integer.parseInt(rs.getString("rst_bad_count")));
				table.setWorkingCount(Integer.parseInt(rs.getString("working_count")));
				table.setRstPassCount(Integer.parseInt(rs.getString("rst_pass_count")));
				table.setRstFailCount(Integer.parseInt(rs.getString("rst_fail_count")));
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MFG MASTER 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public mfgMasterTable getMfgDisplayPage(String factory_no,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mfgMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mfg_master where "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mfg_master where "+sItem+" like '%"+sWord+"%' ";
		query += "and factory_no='"+factory_no+"' ";
		query += "order by pid desc";
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgInfoServlet?&mode=mfg_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgInfoServlet?&mode=mfg_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgInfoServlet?&mode=mfg_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">[Next]</a>";
		}

		//arraylist에 담기
		table = new mfgMasterTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MFG관리번호 구하기
	// FORMAT : MFG+yy(2)+mm(2)+serial(2)
	//*******************************************************************/	
	public String getMfgNo(String factory_no) throws Exception
	{
		//변수 초기화
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//MPS번호중 Serial를 제외한 번호 구하기
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mfg_no = "MFG"+y+m;
		
		//query문장 만들기
		query = "SELECT mfg_no FROM mfg_master where factory_no='"+factory_no+"' and mfg_no like '"+mfg_no+"%' ";
		query += "order by mfg_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("mfg_no");
		
		if(data.length() == 0) {
			data = mfg_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mfg_no+serial;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//*******************************************************************
	// 부품출고의뢰관리번호 구하기 : MPS에 의한 부품출고의뢰번호
	// FORMAT : DM+yy(2)+mm(2)+"-"+serial(2)
	//*******************************************************************/	
	public String getDeliveryNo() throws Exception
	{
		//변수 초기화
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//부품출고의뢰관리번호 중 Serial를 제외한 번호 구하기
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mfg_req_no = "DM"+y+m+"-";
		
		//query문장 만들기
		query = "SELECT delivery_no FROM st_reserved_item_info where delivery_no like '"+mfg_req_no+"%' ";
		query += "order by delivery_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("delivery_no");
		
		if(data.length() == 0) {
			data = mfg_req_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mfg_req_no+serial;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//*******************************************************************
	// 부품출고의뢰관리번호 구하기 : 긴급오더에 의한 부품출고의뢰번호
	// FORMAT : DM+yy(2)+mm(2)+"-"+serial(2)
	//*******************************************************************/	
	public String getDeliveryManNo() throws Exception
	{
		//변수 초기화
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//부품출고의뢰관리번호 중 Serial를 제외한 번호 구하기
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mfg_req_no = "DE"+y+m+"-";
		
		//query문장 만들기
		query = "SELECT delivery_no FROM st_reserved_item_info where delivery_no like '"+mfg_req_no+"%' ";
		query += "order by delivery_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("delivery_no");
		
		if(data.length() == 0) {
			data = mfg_req_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mfg_req_no+serial;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		MFG OPERATOR 관련 정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 관리번호로 해당 MRP ITEM 정보 읽기 
	//*******************************************************************/	
	public mfgOperatorTable readMfgOperator(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
		
		query = "SELECT * FROM mfg_operator where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
	
		if(rs.next()) { 
			mfgOP.setPid(rs.getString("pid"));	
			mfgOP.setGid(rs.getString("gid"));	
			mfgOP.setMfgNo(rs.getString("mfg_no"));	
			mfgOP.setAssyCode(rs.getString("assy_code"));
			mfgOP.setAssySpec(rs.getString("assy_spec"));
			mfgOP.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgOP.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgOP.setMfgUnit(rs.getString("mfg_unit"));
			mfgOP.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"/"));	
			mfgOP.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"/"));	
			mfgOP.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgOP.setBuyType(rs.getString("buy_type"));	
			mfgOP.setFactoryNo(rs.getString("factory_no"));
			mfgOP.setFactoryName(rs.getString("factory_name"));
			mfgOP.setWorkNo(rs.getString("work_no"));	
			mfgOP.setWorkName(rs.getString("work_name"));
			mfgOP.setOpNo(rs.getString("op_no"));
			mfgOP.setOpName(rs.getString("op_name"));
			mfgOP.setMfgId(rs.getString("mfg_id"));
			mfgOP.setMfgName(rs.getString("mfg_name"));
			mfgOP.setNote(rs.getString("note"));
			mfgOP.setCompCode(rs.getString("comp_code"));
			mfgOP.setCompName(rs.getString("comp_name"));
			mfgOP.setCompUser(rs.getString("comp_user"));
			mfgOP.setCompTel(rs.getString("comp_tel"));
			mfgOP.setOpOrder(rs.getString("op_order"));
			
		} 
		else {
			mfgOP.setPid("");	
			mfgOP.setGid("");	
			mfgOP.setMfgNo("");	
			mfgOP.setAssyCode("");
			mfgOP.setAssySpec("");
			mfgOP.setLevelNo(0);
			mfgOP.setMfgCount(0);	
			mfgOP.setMfgUnit("");
			mfgOP.setOpStartDate("");	
			mfgOP.setOpEndDate("");	
			mfgOP.setOrderDate("");	
			mfgOP.setBuyType("");	
			mfgOP.setFactoryNo("");
			mfgOP.setFactoryName("");
			mfgOP.setWorkNo("");	
			mfgOP.setWorkName("");
			mfgOP.setOpNo("");
			mfgOP.setOpName("");
			mfgOP.setMfgId("");
			mfgOP.setMfgName("");
			mfgOP.setNote("");
			mfgOP.setCompCode("");
			mfgOP.setCompName("");
			mfgOP.setCompUser("");
			mfgOP.setCompTel("");
			mfgOP.setOpOrder("");
			
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return mfgOP;
	}

	//*******************************************************************
	// MFG OPERATOR 리스트 구하기
	//*******************************************************************/	
	public ArrayList getMfgOpetatorList (String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mfgOperatorTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mfg_operator where gid = '"+gid+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mfg_operator where gid = '"+gid+"'";
		query += "order by pid desc";
		rs = stmt.executeQuery(query);
		
		//데이터 담기
		while(rs.next()) { 
				table = new mfgOperatorTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setGid(rs.getString("gid"));	
				table.setMfgNo(rs.getString("mfg_no"));
				String assy_code = "<a href=\"javascript:assyView('"+pid+"');\">"+rs.getString("assy_code")+"</a>";
				table.setAssyCode(assy_code);
				table.setAssySpec(rs.getString("assy_spec"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
				table.setMfgUnit(rs.getString("mfg_unit"));
				table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"/"));	
				table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"/"));	
				table.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setWorkNo(rs.getString("work_no"));	
				table.setWorkName(rs.getString("work_name"));
				table.setOpNo(rs.getString("op_no"));
				table.setOpName(rs.getString("op_name"));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setNote(rs.getString("note"));
				table.setCompCode(rs.getString("comp_code"));
				table.setCompName(rs.getString("comp_name"));
				table.setCompUser(rs.getString("comp_user"));
				table.setCompTel(rs.getString("comp_tel"));
				table.setOpOrder(rs.getString("op_order"));

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		MFG ITEM 관련 정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 관리번호로 해당 MRP ITEM 정보 읽기 
	//*******************************************************************/	
	public mfgItemTable readMfgItem(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
		
		query = "SELECT * FROM mfg_item where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			mfgIT.setPid(rs.getString("pid"));	
			mfgIT.setGid(rs.getString("gid"));	
			mfgIT.setMfgNo(rs.getString("mfg_no"));	
			mfgIT.setAssyCode(rs.getString("assy_code"));	
			mfgIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgIT.setItemCode(rs.getString("item_code"));	
			mfgIT.setItemName(rs.getString("item_name"));	
			mfgIT.setItemSpec(rs.getString("item_spec"));	
			mfgIT.setItemUnit(rs.getString("item_unit"));
			mfgIT.setItemType(rs.getString("item_type"));	
			mfgIT.setItemLoss(Double.parseDouble(rs.getString("item_loss")));	
			mfgIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mfgIT.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));
			mfgIT.setSpareCount(Integer.parseInt(rs.getString("spare_count")));
			mfgIT.setAddCount(Integer.parseInt(rs.getString("add_count")));
			mfgIT.setReserveCount(Integer.parseInt(rs.getString("reserve_count")));
			mfgIT.setRequestCount(Integer.parseInt(rs.getString("request_count")));
			mfgIT.setNeedDate(anbdt.getSepDate(rs.getString("need_date"),"/"));	
			mfgIT.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgIT.setFactoryNo(rs.getString("factory_no"));
			mfgIT.setFactoryName(rs.getString("factory_name"));
		} 
		else {
			mfgIT.setPid("");	
			mfgIT.setGid("");	
			mfgIT.setMfgNo("");	
			mfgIT.setAssyCode("");	
			mfgIT.setLevelNo(0);
			mfgIT.setItemCode("");	
			mfgIT.setItemName("");	
			mfgIT.setItemSpec("");
			mfgIT.setItemUnit("");
			mfgIT.setItemType("");	
			mfgIT.setItemLoss(0);	
			mfgIT.setDrawCount(0);	
			mfgIT.setMfgCount(0);	
			mfgIT.setNeedCount(0);	
			mfgIT.setSpareCount(0);
			mfgIT.setAddCount(0);
			mfgIT.setReserveCount(0);
			mfgIT.setRequestCount(0);
			mfgIT.setNeedDate("");	
			mfgIT.setOrderDate("");	
			mfgIT.setFactoryNo("");
			mfgIT.setFactoryName("");
			
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return mfgIT;
	}

	/**********************************************************************
	 * 정전개 단단계 Item 소요량 출력
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void saveSingleMfgItems(String factory_no,String mfg_no,String level_no,String assy_code) throws Exception
	{
		//변수 초기화
		String lno = "",pid="",item_code="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MFG_ITEM ";
		query += "where level_no = '"+level_no+"' and assy_code = '"+assy_code+"' ";
		query += "and mfg_no = '"+mfg_no+"' and factory_no='"+factory_no+"' order by item_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mfgIT = new mfgItemTable();

			pid = rs.getString("pid");
			mfgIT.setPid(pid);	
			mfgIT.setGid(rs.getString("gid"));	
			mfgIT.setMfgNo(rs.getString("mfg_no"));	
			mfgIT.setAssyCode(rs.getString("assy_code"));	
			mfgIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));

			item_code = "<a href=\"javascript:itemView('"+pid+"');\">"+rs.getString("item_code")+"</a>";
			mfgIT.setItemCode(item_code);

			mfgIT.setItemName(rs.getString("item_name"));	
			mfgIT.setItemSpec(rs.getString("item_spec"));	
			mfgIT.setItemUnit(rs.getString("item_unit"));
			mfgIT.setItemType(rs.getString("item_type"));	
			mfgIT.setItemLoss(Double.parseDouble(rs.getString("item_loss")));	
			mfgIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mfgIT.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));
			mfgIT.setSpareCount(Integer.parseInt(rs.getString("spare_count")));
			mfgIT.setAddCount(Integer.parseInt(rs.getString("add_count")));
			mfgIT.setReserveCount(Integer.parseInt(rs.getString("reserve_count")));
			mfgIT.setRequestCount(Integer.parseInt(rs.getString("request_count")));
			mfgIT.setNeedDate(anbdt.getSepDate(rs.getString("need_date"),"/"));	
			mfgIT.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgIT.setFactoryNo(rs.getString("factory_no"));
			mfgIT.setFactoryName(rs.getString("factory_name"));
	
			item_list.add(mfgIT); 
		}

		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 단단계 item 소요량 출력
	 * 하부구조 전체
	 *********************************************************************/
	public ArrayList getSingleMfgItemList(String factory_no,String mfg_no,String level_no,String assy_code) throws Exception
	{
		item_list = new ArrayList();
		saveSingleMfgItems(factory_no,mfg_no,level_no,assy_code);	

		//출력해보기
/*		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			System.out.println(table.getAssyCode()+":"+table.getItemCode()+":"+table.getLevelNo()+":"+table.getDrawCount());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * 정전개 다단계 Item 소요량 출력
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void saveMfgItems(String factory_no,String mfg_no,String level_no,String assy_code) throws Exception
	{
		//변수 초기화
		String lno = "",pid="",item_code="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MFG_ITEM ";
		query += "where level_no = '"+level_no+"' and assy_code = '"+assy_code+"' ";
		query += "and mfg_no = '"+mfg_no+"' and factory_no='"+factory_no+"' order by item_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mfgIT = new mfgItemTable();

			mfgIT.setPid(rs.getString("pid"));	
			mfgIT.setGid(rs.getString("gid"));	
			mfgIT.setMfgNo(rs.getString("mfg_no"));	
			mfgIT.setAssyCode(rs.getString("assy_code"));	
			mfgIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgIT.setItemCode(rs.getString("item_code"));
			mfgIT.setItemName(rs.getString("item_name"));	
			mfgIT.setItemSpec(rs.getString("item_spec"));	
			mfgIT.setItemUnit(rs.getString("item_unit"));
			mfgIT.setItemType(rs.getString("item_type"));	
			mfgIT.setItemLoss(Double.parseDouble(rs.getString("item_loss")));	
			mfgIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mfgIT.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));
			mfgIT.setSpareCount(Integer.parseInt(rs.getString("spare_count")));
			mfgIT.setAddCount(Integer.parseInt(rs.getString("add_count")));
			mfgIT.setReserveCount(Integer.parseInt(rs.getString("reserve_count")));
			mfgIT.setRequestCount(Integer.parseInt(rs.getString("request_count")));
			mfgIT.setNeedDate(anbdt.getSepDate(rs.getString("need_date"),"/"));	
			mfgIT.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgIT.setFactoryNo(rs.getString("factory_no"));
			mfgIT.setFactoryName(rs.getString("factory_name"));
	
			item_list.add(mfgIT); 

			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			saveMfgItems(rs.getString("factory_no"),rs.getString("mfg_no"),lno,rs.getString("item_code"));
		}

		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 다단계 item 소요량 출력
	 * 하부구조 전체
	 *********************************************************************/
	public ArrayList getMfgItemList(String factory_no,String mfg_no,String level_no,String assy_code) throws Exception
	{
		item_list = new ArrayList();
		saveMfgItems(factory_no,mfg_no,level_no,assy_code);	

		//출력해보기
/*		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			System.out.println(table.getAssyCode()+":"+table.getItemCode()+":"+table.getLevelNo()+":"+table.getDrawCount());
		}
*/
		return item_list;
	}
	
	//*******************************************************************
	// MFG ITEM에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨
	// 용도 : ASSY단위별 편집을 위해
	//*******************************************************************/	
	public ArrayList getAssyList(String factory_no,String mfg_no) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct assy_code,level_no from mfg_item where mfg_no='"+mfg_no;
		query += "' and factory_no='"+factory_no+"' order by level_no,assy_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mfgItemTable();	
				table.setMfgNo(mfg_no);
				table.setAssyCode(rs.getString("assy_code"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				//System.out.println(mrp_no+":"+rs.getString("assy_code"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	
	//--------------------------------------------------------------------
	//
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	수량 파악하기 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//변수 초기화
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	//*******************************************************************
	// SQL update 실행하기
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	주어진사번의 사업부정보는
	//*******************************************************************/
	public String[] getDivInfo(String sabun) throws Exception
	{
		//변수 초기화
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code,b.ac_name from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("ac_code");
			data[1] = rs.getString("ac_name");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	주어진코드의 부품정보는
	//*******************************************************************/
	public String[] getItemInfo(String item_code) throws Exception
	{
		//변수 초기화
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select item_name,item_desc from item_master where item_no='"+item_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "";
			data[1] = rs.getString("item_desc");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	해당항목의 권한을 검사
	//*******************************************************************/
	public String checkGrade(String mgr_type,String login_id,String factory_no) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select mgr_code from mfg_grade_mgr where factory_no='"+factory_no+"' and ";
		query += "mgr_type='"+mgr_type+"' and mgr_id like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("mgr_code")+",";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

}

