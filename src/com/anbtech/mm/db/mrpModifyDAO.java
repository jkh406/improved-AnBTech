package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mrpModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//포멧
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기 (BOM정보 담기)
	private mbomItemTable mbomIT = null;			//help class (BOM초기 확정BOM 내용)
	private mrpItemTable mrpIT = null;				//help class (소요량 산출BOM 내용)
	private String assy_head = "1";					//공정 ASSY코드 헤드문자
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mrpModifyDAO(Connection con) 
	{
		this.con = con;
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		MPS MASTER 에 관한 메소드 정의
	//		[MRP 접수내용]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MRP접수(MRP MASTER)로 부터 MRP MASTER정보 만들기 
	//*******************************************************************/	
	public mrpMasterTable readRequestMrpMasterItem(String pid) throws Exception
	{
		//MRP MASTER로 부터 데이터 읽기
		com.anbtech.mm.entity.mpsMasterTable readT = new com.anbtech.mm.entity.mpsMasterTable();
		readT = mpsDAO.readMasterItem(pid,"","");

		//MRP MASTER에 입력할 데이터 형태로 담기
		mrpMasterTable table = new com.anbtech.mm.entity.mrpMasterTable();
		table.setPid("");	
		table.setMpsNo(readT.getMpsNo());
		table.setMrpNo("");
		table.setMrpStartDate(readT.getPlanDate());
		table.setMrpEndDate("");
		table.setModelCode(readT.getModelCode());
		table.setModelName(readT.getModelName());
		table.setFgCode(readT.getFgCode());
		table.setItemCode(readT.getItemCode());
		table.setItemName(readT.getItemName());
		table.setItemSpec(readT.getItemSpec());
		table.setPCount(readT.getPlanCount());
		table.setPlanDate(readT.getPlanDate());
		table.setItemUnit(readT.getItemUnit());
		table.setMrpStatus("");
		table.setFactoryNo(readT.getFactoryNo());
		table.setFactoryName(readT.getFactoryName());
		table.setRegDate("");
		table.setAppDate("");
		table.setAppId("");
		table.setRegDivCode("");
		table.setRegDivName("");
		table.setRegId("");
		table.setRegName("");
		table.setAppNo("");
		table.setPuDevDate("");
		table.setPuReqNo("");
		table.setStockLink("0");
		table.setPjtCode("");
		table.setPjtName("");
		
		return table;
	}

	//*******************************************************************
	// MPS을 MRP에서 접수된것 리스트 구하기 : mps_status = '3'인것만
	//*******************************************************************/	
	public ArrayList getMrpRequestList (String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mpsMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mps_master where mps_status='3' and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mps_master where mps_status='3' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by factory_no,mps_no asc";
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
				table = new mpsMasterTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				String mps_no = "<a href=\"javascript:mpsView('"+pid+"');\">"+rs.getString("mps_no")+"</a>";
				table.setMpsNo(mps_no);

				table.setOrderNo(rs.getString("order_no"));	
				table.setMpsType(rs.getString("mps_type"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPlanDate(anbdt.getSepDate(rs.getString("plan_date"),"-"));
				table.setPlanCount(rs.getInt("plan_count"));
				table.setSellCount(rs.getInt("sell_count"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMpsStatus(rs.getString("mps_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppNo(rs.getString("app_no"));
				table.setOrderComp(rs.getString("order_comp"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MPS을 MRP에서 접수된것 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public mpsMasterTable getRequestDisplayPage(String factory_no,String sItem,String sWord,
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
		mpsMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM mps_master where mps_status='3' and factory_no='"+factory_no+"'";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mps_master where mps_status='3' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by factory_no,mps_no asc";
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
			pagecut = "<a href=mrpInfoServlet?&mode=mrp_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mrpInfoServlet?&mode=mrp_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mrpInfoServlet?&mode=mrp_request&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist에 담기
		table = new mpsMasterTable();
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
	//		MRP MASTER 에 관한 메소드 정의
	//		
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MRP MASTER정보 읽기 
	//*******************************************************************/	
	public mrpMasterTable readMrpMasterItem(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mrpMasterTable table = new com.anbtech.mm.entity.mrpMasterTable();
		
		query = "SELECT * FROM mrp_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMpsNo(rs.getString("mps_no"));
			table.setMrpNo(rs.getString("mrp_no"));
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
			table.setRegDate(rs.getString("reg_date"));
			table.setAppDate(rs.getString("reg_date"));
			table.setAppId(rs.getString("app_id"));
			table.setRegDivCode(rs.getString("reg_div_code"));
			table.setRegDivName(rs.getString("reg_div_name"));
			table.setRegId(rs.getString("reg_id"));
			table.setRegName(rs.getString("reg_name"));
			table.setAppNo(rs.getString("app_no"));
			table.setPuDevDate(rs.getString("pu_dev_date"));
			table.setPuReqNo(rs.getString("pu_req_no"));
			table.setStockLink(rs.getString("stock_link"));
			table.setPjtCode(rs.getString("pjt_code"));
			table.setPjtName(rs.getString("pjt_name"));
			
		} else {
			table.setPid("");	
			table.setMpsNo("");
			table.setMrpNo("");
			table.setMrpStartDate("");
			table.setMrpEndDate("");
			table.setModelCode("");
			table.setModelName("");
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setPCount(0);
			table.setPlanDate("");
			table.setItemUnit("");
			table.setMrpStatus("");
			table.setFactoryNo("");
			table.setFactoryName("");
			table.setRegDate("");
			table.setAppDate("");
			table.setAppId("");
			table.setRegDivCode("");
			table.setRegDivName("");
			table.setRegId("");
			table.setRegName("");
			table.setAppNo("");
			table.setPuDevDate("");
			table.setPuReqNo("");
			table.setStockLink("");
			table.setPjtCode("");
			table.setPjtName("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MRP MASTER 리스트 구하기
	//*******************************************************************/	
	public ArrayList getMrpMasterList (String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
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
		query = "SELECT COUNT(*) FROM mrp_master where factory_no='"+factory_no+"' and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mrp_master where factory_no='"+factory_no+"' and "+sItem+" like '%"+sWord+"%' ";
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
				table = new mrpMasterTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setMpsNo(rs.getString("mps_no"));
				String mrp_no = "<a href=\"javascript:mrpView('"+pid+"');\">"+rs.getString("mrp_no")+"</a>";
				table.setMrpNo(mrp_no);

				table.setMrpStartDate(anbdt.getSepDate(rs.getString("mrp_start_date"),"-"));
				table.setMrpEndDate(anbdt.getSepDate(rs.getString("mrp_end_date"),"-"));
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
				table.setRegDate(rs.getString("reg_date"));
				table.setAppDate(rs.getString("reg_date"));
				table.setAppId(rs.getString("app_id"));
				table.setRegDivCode(rs.getString("reg_div_code"));
				table.setRegDivName(rs.getString("reg_div_name"));
				table.setRegId(rs.getString("reg_id"));
				table.setRegName(rs.getString("reg_name"));
				table.setAppNo(rs.getString("app_no"));
				table.setPuDevDate(rs.getString("pu_dev_date"));
				table.setPuReqNo(rs.getString("pu_req_no"));
				table.setStockLink(rs.getString("stock_link"));
				table.setPjtCode(rs.getString("pjt_code"));
				table.setPjtName(rs.getString("pjt_name"));

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MRP MASTER 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public mrpMasterTable getMrpDisplayPage(String factory_no,String sItem,String sWord,
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
		query = "SELECT COUNT(*) FROM mrp_master where factory_no='"+factory_no+"' and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM mrp_master where factory_no='"+factory_no+"' and "+sItem+" like '%"+sWord+"%' ";
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
			pagecut = "<a href=mrpInfoServlet?&mode=mrp_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mrpInfoServlet?&mode=mrp_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mrpInfoServlet?&mode=mrp_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&factory_no="+factory_no+">[Next]</a>";
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

	//*******************************************************************
	// MRP관리번호 구하기
	// FORMAT : MRP+yy(2)+mm(2)+serial(2)
	//*******************************************************************/	
	public String getMrpNo(String factory_no) throws Exception
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
		String mrp_no = "MRP"+y+m;
		
		//query문장 만들기
		query = "SELECT mrp_no FROM mrp_master where factory_no='"+factory_no+"' and mrp_no like '"+mrp_no+"%' ";
		query += "order by mrp_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("mrp_no");
		
		if(data.length() == 0) {
			data = mrp_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mrp_no+serial;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		MRP ITEM 관련 정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 관리번호로 해당 MRP ITEM 정보 읽기 
	//*******************************************************************/	
	public mrpItemTable readMrpItem(String pid,String factory_no) throws Exception
	{
		//변수 초기화
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mrpItemTable mrpIT = new com.anbtech.mm.entity.mrpItemTable();
		
		query = "SELECT * FROM mrp_item where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			mrpIT.setPid(rs.getString("pid"));	
			mrpIT.setGid(rs.getString("gid"));	
			mrpIT.setMrpNo(rs.getString("mrp_no"));	
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
			
		} 
		else {
			//공장명 구하기
			where = "where factory_code='"+factory_no+"'";
			String factory_name = getColumData("factory_info_table","factory_name",where);

			mrpIT.setPid("");	
			mrpIT.setGid("");	
			mrpIT.setMrpNo("");	
			mrpIT.setAssyCode("");	
			mrpIT.setLevelNo(0);
			mrpIT.setItemCode("");	
			mrpIT.setItemName("");	
			mrpIT.setItemSpec("");	
			mrpIT.setItemType("");	
			mrpIT.setDrawCount(0);	
			mrpIT.setMrpCount(0);	
			mrpIT.setNeedCount(0);	
			mrpIT.setStockCount(0);	
			mrpIT.setOpenCount(0);	
			mrpIT.setPlanCount(0);	
			mrpIT.setAddCount(0);	
			mrpIT.setMrsCount(0);	
			mrpIT.setItemUnit("EA");	
			mrpIT.setBuyType("");
			mrpIT.setFactoryNo(factory_no);
			mrpIT.setFactoryName(factory_name);
			mrpIT.setPuDevDate("");	
			mrpIT.setPuReqNo("");
			
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return mrpIT;
	}

	/**********************************************************************
	 * 정전개 다단계 Item 소요량 출력 : 편집용
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void saveMrpItems(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
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
			mrpIT.setMrpNo(rs.getString("mrp_no"));	
			mrpIT.setAssyCode(rs.getString("assy_code"));	
			mrpIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));

			item_code = "<a href=\"javascript:itemView('"+pid+"');\">"+rs.getString("item_code")+"</a>";
			mrpIT.setItemCode(item_code);
			
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
			saveMrpItems(rs.getString("factory_no"),mrp_no,lno,rs.getString("item_code"));
		}
		rs.close();
		stmt.close();
	}

	/**********************************************************************
	 * 정전개 다단계 Item 소요량 출력 : 조회용
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void saveViewMrpItems(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
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
			mrpIT.setMrpNo(rs.getString("mrp_no"));	
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
			saveViewMrpItems(rs.getString("factory_no"),mrp_no,lno,rs.getString("item_code"));
		}
		rs.close();
		stmt.close();
	}
	
	/**********************************************************************
	 * 정전개 다단계 item 소요량 출력
	 * 하부구조 전체
	 * use : 보기용[V], 편집용[E]
	 *********************************************************************/
	public ArrayList getMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code,String use) throws Exception
	{
		item_list = new ArrayList();

		if(use.equals("V")) saveViewMrpItems(factory_no,mrp_no,level_no,assy_code);	//조회용
		else saveMrpItems(factory_no,mrp_no,level_no,assy_code);					//편집용

		//출력해보기
/*		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			System.out.println(table.getAssyCode()+":"+table.getItemCode()+":"+table.getLevelNo()+":"+table.getDrawCount());
		}
*/
		return item_list;
	}
	/**********************************************************************
	 * 정전개 단단계 Item 소요량 출력 : ASSY단위별 출력
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void saveSingleMrpItems(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
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
			mrpIT.setMrpNo(rs.getString("mrp_no"));	
			mrpIT.setAssyCode(rs.getString("assy_code"));	
			mrpIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));

			item_code = "<a href=\"javascript:itemView('"+pid+"');\">"+rs.getString("item_code")+"</a>";
			mrpIT.setItemCode(item_code);
			
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
		}
		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 정전개 단단계 item 소요량 출력 : ASSY단위별
	 * 하부구조 전체
	 *********************************************************************/
	public ArrayList getSingleMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		item_list = new ArrayList();
		saveSingleMrpItems(factory_no,mrp_no,level_no,assy_code);	

		//출력해보기
/*		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			System.out.println(table.getAssyCode()+":"+table.getItemCode()+":"+table.getLevelNo()+":"+table.getDrawCount());
		}
*/
		return item_list;
	}

	//*******************************************************************
	// MRP ITEM에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨
	// 용도 : ASSY단위별 편집을 위해
	//*******************************************************************/	
	public ArrayList getAssyList(String factory_no,String mrp_no) throws Exception
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
				table.setMrpNo(mrp_no);
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
	//		구매등록관련 정보
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 구매등록을 위한 다단계 구하기
	 * 조건 : MRP NO,ASSY코드,레벨
	 *********************************************************************/
	private void buyMrpItems(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
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
			mrpIT.setMrpNo(rs.getString("mrp_no"));	
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
			buyMrpItems(rs.getString("factory_no"),mrp_no,lno,rs.getString("item_code"));
		}
		rs.close();
		stmt.close();
	} 
	/**********************************************************************
	 * 구매등록 다단계 item 소요량 출력
	 * 하부구조 전체
	 *********************************************************************/
	public ArrayList buyMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		item_list = new ArrayList();
		buyMrpItems(factory_no,mrp_no,level_no,assy_code);	

		//출력해보기
/*		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			System.out.println(table.getAssyCode()+":"+table.getItemCode()+":"+table.getLevelNo()+":"+table.getDrawCount());
		}
*/
		return item_list;
	}

	//--------------------------------------------------------------------
	//
	//		BOM관련 정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 해당품목이 해당관리코드(gid)내에 포함된 제품또는 반제품 품목코드인지 판단하기
	//*******************************************************************/	
	public int checkItemCode(String gid,String item_code) throws Exception
	{
		//변수 초기화
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT COUNT(*) FROM mbom_item where gid='"+gid+"' and parent_code='"+item_code+"'";
		rs = stmt.executeQuery(query);

		rs.next();
		cnt = rs.getInt(1);

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return cnt;
	}

	/**********************************************************************
	 * 정전개 다단계 Item 출력
	 * 확정된 BOM TREE 출력하기
	 * 조건 : 유효시작일,유효종료일
	 *********************************************************************/
	private void saveForwardBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_ITEM ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "((bom_start_date <='"+sel_date+"' and bom_end_date = '0') or "; 
		query += "(bom_start_date <='"+sel_date+"' and bom_end_date > '"+sel_date+"')) ";
		query += "order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mbomIT = new mbomItemTable();
			mbomIT.setPid(rs.getString("pid"));	
			mbomIT.setGid(rs.getString("gid"));	
			mbomIT.setParentCode(rs.getString("parent_code"));	
			mbomIT.setChildCode(rs.getString("child_code"));	
			mbomIT.setLevelNo(rs.getString("level_no"));	
			mbomIT.setPartName(rs.getString("part_name"));	
			mbomIT.setPartSpec(rs.getString("part_spec"));	
			mbomIT.setLocation(rs.getString("location"));	
			mbomIT.setOpCode(rs.getString("op_code"));	
			mbomIT.setQtyUnit(rs.getString("qty_unit"));	
			mbomIT.setQty(rs.getString("qty"));	
			mbomIT.setMakerName(rs.getString("maker_name"));	
			mbomIT.setMakerCode(rs.getString("maker_code"));	
			mbomIT.setPriceUnit(rs.getString("price_unit"));	
			mbomIT.setPrice(rs.getString("price"));
			mbomIT.setAddDate(rs.getString("add_date"));
			mbomIT.setItemType(rs.getString("item_type"));
			mbomIT.setBuyType(rs.getString("buy_type"));	
			mbomIT.setEcoNo(rs.getString("eco_no"));	
			mbomIT.setAdTag(rs.getString("adtag"));	
			mbomIT.setBomStartDate(rs.getString("bom_start_date"));	
			mbomIT.setBomEndDate(rs.getString("bom_end_date"));
			String assy_dup = rs.getString("assy_dup");
			mbomIT.setAssyDup(assy_dup);
			item_list.add(mbomIT); 
			
			//System.out.println(rs.getString("level_no")+":"+rs.getString("parent_code")+":"+rs.getString("child_code"));
			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveForwardBomItems(gid,lno,rs.getString("child_code"),sel_date);
		}
		rs.close();
		stmt.close();
	} 

	/**********************************************************************
	 * 정전개 단단계 Item 출력
	 * 확정된 BOM TREE 출력하기
	 * 조건 : 유효시작일,유효종료일
	 *********************************************************************/
	private void saveForwardSingleBomItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_ITEM ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "((bom_start_date <='"+sel_date+"' and bom_end_date = '0') or "; 
		query += "(bom_start_date <='"+sel_date+"' and bom_end_date > '"+sel_date+"')) ";
		query += "order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mbomIT = new mbomItemTable();
			mbomIT.setPid(rs.getString("pid"));	
			mbomIT.setGid(rs.getString("gid"));	
			mbomIT.setParentCode(rs.getString("parent_code"));	
			mbomIT.setChildCode(rs.getString("child_code"));	
			mbomIT.setLevelNo(rs.getString("level_no"));	
			mbomIT.setPartName(rs.getString("part_name"));	
			mbomIT.setPartSpec(rs.getString("part_spec"));	
			mbomIT.setLocation(rs.getString("location"));	
			mbomIT.setOpCode(rs.getString("op_code"));	
			mbomIT.setQtyUnit(rs.getString("qty_unit"));	
			mbomIT.setQty(rs.getString("qty"));	
			mbomIT.setMakerName(rs.getString("maker_name"));	
			mbomIT.setMakerCode(rs.getString("maker_code"));	
			mbomIT.setPriceUnit(rs.getString("price_unit"));	
			mbomIT.setPrice(rs.getString("price"));
			mbomIT.setAddDate(rs.getString("add_date"));
			mbomIT.setItemType(rs.getString("item_type"));
			mbomIT.setBuyType(rs.getString("buy_type"));	
			mbomIT.setEcoNo(rs.getString("eco_no"));	
			mbomIT.setAdTag(rs.getString("adtag"));	
			mbomIT.setBomStartDate(rs.getString("bom_start_date"));	
			mbomIT.setBomEndDate(rs.getString("bom_end_date"));
			String assy_dup = rs.getString("assy_dup");
			mbomIT.setAssyDup(assy_dup);
			item_list.add(mbomIT); 
		}
		rs.close();
		stmt.close();
	} 

	/**********************************************************************
	 * 정전개 다단계 TREE구성 출력
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getMbomItemList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveForwardBomItems(gid,level_no,parent_code,sel_date);	

		//출력해보기
/*		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo()+":"+table.getLocation());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * 정전개 단단계 TREE구성 출력
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getMbomSingleItemList(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveForwardSingleBomItems(gid,level_no,parent_code,sel_date);	

		//출력해보기
/*		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo()+":"+table.getLocation());
		}
*/
		return item_list;
	}

	//--------------------------------------------------------------------
	//
	//		재고 시스템 관련 메소드
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 해당품목의 현재 재고량/입고예정량 구하기
	//*******************************************************************/	
	public String[] getItemStockInfo(String item_code,String factory_no) throws Exception
	{
		//변수 초기화
		String[] data = new String[2];  data[0] = data[1] = "0";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select stock_quantity,into_quantity from st_item_stock_master where item_code ='"+item_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("stock_quantity");
			data[1] = rs.getString("into_quantity");
		}
	
		stmt.close();
		rs.close();

		return data;
	}

	//*******************************************************************
	// 해당품목의 현재 재고량/입고예정량 예약진행하기 : 2004.6.4 취소
	// 재고시스템의 입력값 : item_code,mrp_no,factory_no
	// 재고시스템 리턴값 : 현재고수량, 입고예정수량
	//*******************************************************************/	
	public String[] getItemStockInfo(String item_code,String factory_no,String mrp_no) throws Exception
	{
		//변수 초기화
		String[] data = new String[2];  data[0] = data[1] = "0";
		
		//재고수량 예약하고 예약수량 리턴받기
//		data = 재고리턴 메소드 ;

		return data;
	}

	//*******************************************************************
	// 해당품목의 현재 재고량 예약취소하기 (입고예정량은 유지됨) : 2004.6.4 취소
	// 재고시스템의 입력값 : item_code,mrp_no,factory_no
	// 재고시스템 리턴값 : 현재고수량
	//*******************************************************************/	
	public String cancelItemStockInfo(String item_code,String factory_no,String mrp_no) throws Exception
	{
		//변수 초기화
		String data = "0";
		
		//예약된 재고수량만 예약취소하기 (입고예정량은 유지됨)
//		data = 재고리턴 메소드 ;

		return data;
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

