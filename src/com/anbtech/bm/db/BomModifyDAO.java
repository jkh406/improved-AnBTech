package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();//문자
	private String query = "";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class
	private int total_page = 0;
	private int current_page = 0;
	private String assy_head = "[1,F]";				//Assy코드만 쿼리시 사용 1:Assy선두문자, F:FG코드 선두문자
	private String ele_assy = "[1][E]";				//회로Assy
	private String ele_item = "[2,3]";				//회로부품

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MBOM_MASTER정보 읽기
	//*******************************************************************/	
	public mbomMasterTable readMasterItem(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = new com.anbtech.bm.entity.mbomMasterTable();
		
		query = "SELECT * FROM mbom_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setModelgCode(rs.getString("modelg_code"));	
			table.setModelgName(rs.getString("modelg_name"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setPdgCode(rs.getString("pdg_code"));	
			table.setPdgName(rs.getString("pdg_name"));	
			table.setPdCode(rs.getString("pd_code"));	
			table.setPdName(rs.getString("pd_name"));
			table.setPjtCode(rs.getString("pjt_code"));	
			table.setPjtName(rs.getString("pjt_name"));	
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setRegDate(rs.getString("reg_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppName(rs.getString("app_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setBomStatus(rs.getString("bom_status"));	
			table.setAppNo(rs.getString("app_no"));	
			table.setMStatus(rs.getString("m_status"));	
			table.setPurpose(rs.getString("purpose"));	
		} else {
			table.setPid(anbdt.getID());	
			table.setModelgCode("");	
			table.setModelgName("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setPdgCode("");	
			table.setPdgName("");	
			table.setPdCode("");	
			table.setPdName("");	
			table.setPjtCode("");	
			table.setPjtName("");	
			table.setRegId("");	
			table.setRegName("");	
			table.setRegDate("");	
			table.setAppId("");	
			table.setAppName("");	
			table.setAppDate("");	
			table.setBomStatus("");	
			table.setAppNo("");	
			table.setMStatus("");	
			table.setPurpose("0");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MBOM_MASTER 전체LIST 가져오기
	//*******************************************************************/	
	public ArrayList getMasterList (String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM MBOM_MASTER";
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' order by pid desc";
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
				table = new mbomMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));	

				String mcode = rs.getString("model_code");
				String bom_status = rs.getString("bom_status");
				mcode = "<a href=\"javascript:masterView('"+pid+"');\">"+rs.getString("model_code")+"</a>";
				table.setModelCode(mcode);

				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	

				if(bom_status.equals("0")) bom_status = "반려";
				else if(bom_status.equals("1")) bom_status = "초기등록";
				else if(bom_status.equals("2")) bom_status = "템플릿등록";
				else if(bom_status.equals("3")) bom_status = "BOM등록";
				else if(bom_status.equals("4")) bom_status = "결재진행";
				else if(bom_status.equals("5")) bom_status = "BOM확정";
				else bom_status = "";
				table.setBomStatus(bom_status);

				table.setAppNo(rs.getString("app_no"));	
				String m_status = rs.getString("m_status");
				if(m_status.equals("0")) m_status = "단종";
				else if(m_status.equals("1")) m_status = "개발";
				else if(m_status.equals("2")) m_status = "양산";
				else m_status = "";
				table.setMStatus(m_status);	
				table.setPurpose(rs.getString("purpose"));	
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public mbomMasterTable getDisplayPage(String sItem,String sWord,
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
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM MBOM_MASTER";
		total_cnt = getTotalCount(query);

		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' order by pid desc";
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
			pagecut = "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist에 담기
		table = new mbomMasterTable();
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
	// MBOM_MASTER 임시BOM 정보 가져오기
	//*******************************************************************/	
	public ArrayList getTmpBomMasterList (String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' and purpose='1' order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));

				String mcode = rs.getString("model_code");
				String bom_status = rs.getString("bom_status");
				mcode = "<a href=\"javascript:masterView('"+pid+"');\">"+rs.getString("model_code")+"</a>";
				table.setModelCode(mcode);

				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	

				if(bom_status.equals("0")) bom_status = "반려";
				else if(bom_status.equals("1")) bom_status = "초기등록";
				else if(bom_status.equals("2")) bom_status = "템플릿등록";
				else if(bom_status.equals("3")) bom_status = "BOM등록";
				else if(bom_status.equals("4")) bom_status = "결재진행";
				else if(bom_status.equals("5")) bom_status = "BOM확정";
				else bom_status = "";
				table.setBomStatus(bom_status);

				table.setAppNo(rs.getString("app_no"));	
				String m_status = rs.getString("m_status");
				if(m_status.equals("0")) m_status = "단종";
				else if(m_status.equals("1")) m_status = "개발";
				else if(m_status.equals("2")) m_status = "양산";
				else m_status = "";
				table.setMStatus(m_status);	
				table.setPurpose(rs.getString("purpose"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER 에서 미확정 BOM LIST
	//*******************************************************************/	
	public ArrayList getXbomList (String sItem,String sWord,String login_id) throws Exception
	{
		//변수 초기화
		String pid="",model_code="",fg_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where bom_status in('0','1','2','3') and ";
		query += sItem+" like '%"+sWord+"%' order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();	
				pid = rs.getString("pid");
				table.setPid(pid);	

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));

				model_code = rs.getString("model_code");
				table.setModelCode(model_code);	
				table.setModelName(rs.getString("model_name"));

				fg_code = rs.getString("fg_code");
				fg_code = "<a href=\"javascript:goBranch('"+pid+"','"+model_code+"','"+fg_code+"');\">"+fg_code+"</a>";
				table.setFgCode(fg_code);

				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	
				table.setPurpose(rs.getString("purpose"));	

				//편집권한 찾기
				mgr = getFgGrade(login_id,pid);
				if(mgr.length() == 0) table_list.add(table);

		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER 에서 미확정 첫번째 모델은 : 최초값 구하기
	//*******************************************************************/	
	public String getXbomFirst() throws Exception
	{
		//변수 초기화
		String pid = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where bom_status in('0','1','2','3') order by pid desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) pid = rs.getString("pid");
		
		//공통 항목 끝내기 
		stmt.close();
		rs.close();
		return pid;
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MBOM_STR정보 읽기
	//*******************************************************************/	
	public mbomStrTable readStrItem(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
		
		query = "SELECT * FROM mbom_str where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setAppStatus(rs.getString("app_status"));
			table.setTag(rs.getString("tag"));
			String part_type = getPartType(rs.getString("gid"),rs.getString("pid"),rs.getString("child_code"),rs.getString("level_no"));
			table.setPartType(part_type);
		} else {
			table.setPid(anbdt.getID());	
			table.setGid("");	
			table.setParentCode("");	
			table.setChildCode("");	
			table.setLevelNo("");	
			table.setPartName("");	
			table.setPartSpec("");	
			table.setLocation("");	
			table.setOpCode("");	
			table.setQtyUnit("");	
			table.setQty("");	
			table.setMakerName("");	
			table.setMakerCode("");	
			table.setPriceUnit("");	
			table.setPrice("");
			table.setAddDate("");	
			table.setBuyType("");	
			table.setEcoNo("");	
			table.setAdTag("");	
			table.setBomStartDate("0");	
			table.setBomEndDate("0");
			table.setAppStatus("0");
			table.setTag("0");
			table.setPartType("P");		//P:부품, A:Assy
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	//	MBOM_STR에서 해당부품이 PART인지 ASSY코드인지 판단하기 : 하부구조 있나 없나
	//  편집시 삭제를 위한 조건으로 사용 [하부구조가 없으면 삭제 가능토록]
	//*******************************************************************/
	public String getPartType(String gid,String pid,String child_code,String level_no) throws Exception
	{
		//변수 초기화
		String part_type = "A";		//Assy코드임
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select count(*) from mbom_str where gid ='"+gid+"' and parent_code='"+child_code+"'";
		query += " and level_no > '"+level_no+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			int cnt = rs.getInt(1);
			if(cnt == 0) part_type = "P";	//부품임
		}

		//중복Assy 표시 [assy_dup='D']인 경우는 제외 : 부품으로 인식토록 하여 삭제할 수 있도록 함.
		query = "select assy_dup from mbom_str where gid='"+gid+"' and pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			String assy_dup = rs.getString("assy_dup");
			if(assy_dup.equals("D")) part_type="P";
		}
	
		stmt.close();
		rs.close();
		return part_type;			
	}

	//*******************************************************************
	//	MBOM_STR에서 LEVEL_NO구하기
	//*******************************************************************/
	public int getLevelNo(String query) throws Exception
	{
		//변수 초기화
		int level_no = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			level_no = Integer.parseInt(rs.getString("level_no"));
			level_no++;		//해당레벨에서 +1을하여 돌려준다
		}
		
		stmt.close();
		rs.close();
		return level_no;			
	}

	//--------------------------------------------------------------------
	//확정전 BOM 구성하기
	//		BOM STRUCTURE 에 관한 메소드 정의
	//			1. 정전개 [단일]
	//			2. 정전개 [다단계]
	//			3. 역전개
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM 정보를 배열에 담는다. 
	 * MBOM 정전개 TREE구하여 ArrayList에 담기 : 단 해당 모품목 코드만
	 *********************************************************************/
	public void saveSingleForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			item_list.add(mst);
		}
		rs.close();
		stmt.close(); 
		
	} //saveSingleForwardItems

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 단 해당모품목 코드만
	 *********************************************************************/
	public ArrayList getSingleForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		item_list = new ArrayList();
		saveSingleForwardItems(gid,level_no,parent_code);
		return item_list;
	}

	/**********************************************************************
	 * MBOM 정보를 배열에 담는다. 
	 * MBOM 정전개 TREE구하여 ArrayList에 담기 : 하부구조 전체
	 *********************************************************************/
	public void saveForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst);

			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveForwardItems(gid,lno,rs.getString("child_code"));
		}
		rs.close();
		stmt.close(); 
		
	} //saveForwardItems

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		item_list = new ArrayList();
		saveForwardItems(gid,level_no,parent_code);	

		//출력해보기
/*		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * MBOM 정보를 배열에 담는다. 
	 * MBOM 역전개 TREE구하여 ArrayList에 담기
	 *********************************************************************/
	public void saveReverseItems(String child_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT distinct parent_code,child_code,level_no,part_name,part_spec,op_code from MBOM_STR ";
		query += "where child_code = '"+child_code+"' ";
		query += "order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setParentCode(rs.getString("parent_code"));
			mst.setChildCode(rs.getString("child_code"));
			mst.setLevelNo(rs.getString("level_no"));
			mst.setPartName(rs.getString("part_name"));
			mst.setPartSpec(rs.getString("part_spec"));
			mst.setOpCode(rs.getString("op_code"));
			item_list.add(mst);
			saveReverseItems(rs.getString("parent_code"));
		}
		rs.close();
		stmt.close(); 
		
	} //saveReverseItems

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다.
	 * MBOM 역전개 TREE구하여 배열에 담아 리턴하기
	 *********************************************************************/
	public ArrayList getReverseItems(String child_code) throws Exception
	{
		item_list = new ArrayList();
		saveReverseItems(child_code);	
		return item_list;
	}

	/**********************************************************************
	 * MBOM 정보를 배열에 담는다. : BOM 복사용
	 * MBOM 정전개 TREE구하여 ArrayList에 담기 : 하부구조 전체
	 *********************************************************************/
	public void saveCopyForwardItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "((bom_start_date <='"+sel_date+"' and bom_end_date = '0') or "; 
		query += "(bom_start_date <='"+sel_date+"' and bom_end_date > '"+sel_date+"'))";
		query += " order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst);

			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveCopyForwardItems(gid,lno,rs.getString("child_code"),sel_date);
		}
		rs.close();
		stmt.close(); 
		
	} //saveForwardItems

	/**********************************************************************
	 * MBOM정보를 담은 item배열을 리턴한다. : BOM복사용
	 * MBOM 정전개 TREE구하여 배열에 담아 리턴하기 : 하부구조 전체
	 *********************************************************************/
	public ArrayList getCopyForwardItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveCopyForwardItems(gid,level_no,parent_code,sel_date);	

/*		//출력해보기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/
		return item_list;
	}
	//--------------------------------------------------------------------
	//
	//		회로물에 대한 Location유무 및 중복검사를 위한 메소드
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 회로 Assy에 있는 회로부품에 대한 Location검사를 위해 
	 * [모든 BOM에 적용 : 작성중및 설계변경 까지 포함]
	 *********************************************************************/
	public ArrayList getElectronicItems(String gid) throws Exception
	{
		//변수 초기화
		item_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where gid = '"+gid+"' and parent_code like '"+ele_assy+"%' ";
		query += "and child_code like '"+ele_item+"%' ";
		query += "and adtag != 'D' and adtag != 'RB' ";
		query += "order by parent_code,child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));
			mst.setAssyDup(rs.getString("assy_dup"));
			item_list.add(mst);
		}
		rs.close();
		stmt.close(); 
		
		return item_list;
	} //getElectronicItems
	
	//--------------------------------------------------------------------
	//
	//		BOM 복사 , 붙이기 편집을 위한 메소드
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_MASTER FG코드 검색하기
	//*******************************************************************/	
	public ArrayList getFGList (String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				table.setPid(rs.getString("pid"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER FG코드 검색하기 : 복사대상 [확정된 BOM 만]
	//*******************************************************************/	
	public ArrayList getAppFGList (String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' and bom_status='5' order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				table.setPid(rs.getString("pid"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER FG코드 검색하기 : 붙이기대상 [권한이 있으면서,확정,승인중을 제외한 BOM]
	//*******************************************************************/	
	public ArrayList getMakeFGList (String sWord,String login_id) throws Exception
	{
		//변수 초기화
		String pid = "",mgr="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query문장 만들기
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' and bom_status in ('0','1','2','3') ";
		query += "order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
							
				pid = rs.getString("pid");				
				table.setPid(pid);	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				//편집권한 찾기
				mgr = getFgGrade(login_id,pid);
				if(mgr.length() == 0) table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STR에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨
	// 용도 : 파일 Import 할때 사용 [분기때문에]
	//*******************************************************************/	
	public ArrayList getAssyList(String gid) throws Exception
	{
		//변수 초기화
		String level_no="",parent_code="",p_code="",where="",part_spec="",op_code="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct gid,parent_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and (parent_code like '"+assy_head+"%' and level_no != '0') ";
		query += "order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				table.setLevelNo(level_no);

				p_code = rs.getString("parent_code"); 
				parent_code = "<a href=\"javascript:goBranch('"+gid+"','"+level_no+"','"+p_code+"');\">"+p_code+"</a>";
				table.setParentCode(parent_code);

				where = "where item_no = '"+p_code+"'";
				part_spec = getColumData("ITEM_MASTER","item_desc",where);
				table.setPartSpec(part_spec);

				table.setOpCode(rs.getString("op_code"));
				table_list.add(table);
		}

		//마지막레벨에 공정ASSY코드가 있으면 이를 붙여준다.
		query = "select gid,child_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and parent_code = '"+p_code+"' and level_no='"+level_no+"'";
		query += " and child_code like '"+assy_head+"%' order by level_no,child_code ASC";
		rs = stmt.executeQuery(query);
		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no) + 1);
				table.setLevelNo(level_no);

				p_code = rs.getString("child_code");
				parent_code = "<a href=\"javascript:goBranch('"+gid+"','"+level_no+"','"+p_code+"');\">"+p_code+"</a>";
				table.setParentCode(parent_code);

				where = "where item_no = '"+p_code+"'";
				part_spec = getColumData("ITEM_MASTER","item_desc",where);
				op_code = getColumData("ITEM_MASTER","op_code",where);
				table.setPartSpec(part_spec);

				table.setOpCode(op_code);
				////System.out.println(level_no+" : "+rs.getString("child_code")+" : "+parent_code.substring(len-2,len));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STR에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨
	// 용도 : 1. 복사, 붙이기 할때 사용
	//        2. BOM 정전개/역전개 시 완료BOM의 Assy정보 찾기
	//*******************************************************************/	
	public ArrayList getAssyListCP(String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct gid,child_code,level_no,part_spec,op_code from mbom_str where gid='"+gid;
		query += "' and (child_code like '"+assy_head+"%' or level_no = '0') ";
		query += "order by level_no,child_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);
				String level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no)+1);	//자품목으로 검색함으로 레벨은 +1
				table.setLevelNo(level_no);
				table.setParentCode(rs.getString("child_code"));
				table.setPartSpec(rs.getString("part_spec"));
				table.setOpCode(rs.getString("op_code"));
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당BOM내에서 자품목코드를 이용한 공정코드 찾기
	//*******************************************************************/	
	public String getOpCode (String gid,String child_code) throws Exception
	{
		//변수 초기화
		String op_code = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "select op_code from mbom_str where gid='"+gid+"' and child_code ='"+child_code+"' ";
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
				op_code = rs.getString("op_code");
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return op_code;
	}

	//*******************************************************************
	// MBOM_STR에서 해당 모델의 Assy코드만 찾기 : 첫번호는 1로 시작됨
	// 용도 : 1. Part List Import 할때 Template로 등록된 Assy코드 : tag='2'
	//        2. 품목코드,공정코드,Location 형태일때
	//*******************************************************************/	
	public ArrayList getAssyListTemp(String gid) throws Exception
	{
		//변수 초기화
		String parent_code="",level_no = "",op_code="",where="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy코드 찾기 [1xxx:공정ASSY,Fxxx:FG코드 로 시작되는 코드]
		query = "select distinct gid,parent_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and (parent_code like '"+assy_head+"%' or level_no = '0') and tag='2'";
		query += "order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				table.setLevelNo(level_no);

				parent_code = rs.getString("parent_code");
				table.setParentCode(parent_code);

				table.setOpCode(rs.getString("op_code"));
				////System.out.println(level_no+" : "+rs.getString("parent_code")+" : "+rs.getString("op_code"));
				table_list.add(table);
		}

		//Template코드로 구성된 경우[tag=2] 마지막의 자코드도 공정코드임으로 이를 추가한다.
		query = "select gid,child_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and parent_code = '"+parent_code+"' and level_no='"+level_no+"' and tag='2'";
		query += " and child_code like '"+assy_head+"%' order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);
		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no) + 1);
				table.setLevelNo(level_no);

				parent_code = rs.getString("child_code");
				table.setParentCode(parent_code);

				where = "where item_no = '"+parent_code+"'";
				op_code = getColumData("ITEM_MASTER","op_code",where);
				table.setOpCode(op_code);
				////System.out.println(level_no+" : "+rs.getString("child_code")+" : "+parent_code.substring(len-2,len));
				table_list.add(table);
		}


		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		부품 마스터에서 필요한 정보 쿼리하기
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	//	품목코드에 해당되는 정보쿼리하기
	// [0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,6op_code] 
	// 현재 : 규격만 사용
	//*******************************************************************/
	public String[] getComponentInfo(String part_code) throws Exception
	{
		//변수 초기화
		String[] data = new String[7];
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		for(int i=0; i<6; i++) data[i] = "";		//배열초기화
		query = "SELECT * FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "원자재";
			data[1] = str.repWord(rs.getString("item_desc"),"'","`");
			data[2] = rs.getString("mfg_no");		if(data[2] == null) data[2] = "";
			data[3] = "";
			data[4] = rs.getString("stock_unit");	if(data[4] == null) data[4] = "EA";
			data[5] = rs.getString("item_type");	if(data[5] == null) data[5] = "4";
			data[6] = rs.getString("op_code");		if(data[6] == null) data[6] = "";
		}
		
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	해당 품목코드의 SPEC정보 구하기
	//*******************************************************************/
	public String getComponentSpec(String part_code) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT item_desc FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("item_desc");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	품목 코드 검색하기
	//*******************************************************************/
	public ArrayList getComponentCode(String sWord) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//품목코드 찾기
		query = "SELECT item_no,item_desc FROM item_master WHERE item_no like '"+sWord+"%' order by item_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setParentCode(rs.getString("item_no"));
				table.setPartSpec(rs.getString("item_desc"));
				table_list.add(table);
		}

		stmt.close();
		rs.close();
		return table_list;			
	}
	//--------------------------------------------------------------------
	//
	//		해당 모델[FG 코드]의 사용권한 판단하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	임시BOM 관리권한이 있는지 판단하기
	//*******************************************************************/
	public String getTbomMgrGrade(String sabun) throws Exception
	{
		String msg = "",where="",mgr="";

		//임시BOM 승인/취소 권한유무 찾기
		where = "where owner like '"+sabun+"%' and keyname='TBOM_MGR'";
		mgr =getColumData("mbom_grade_mgr","keyname",where);

		//상신할 수 있는 권한이 있는지 판단하기
		if(!mgr.equals("TBOM_MGR")) {
			msg = "임시BOM에 대한 관리권한이 없습니다.";
		}

		return msg;
	}
	//*******************************************************************
	//	ECO AUDIT 관리권한이 있는지 판단하기
	//*******************************************************************/
	public String getEcoAuditGrade(String sabun) throws Exception
	{
		String msg = "",where="",mgr="";

		//임시BOM 승인/취소 권한유무 찾기
		where = "where owner like '"+sabun+"%' and keyname='ECO_AUDIT'";
		mgr =getColumData("mbom_grade_mgr","keyname",where);

		//상신할 수 있는 권한이 있는지 판단하기
		if(!mgr.equals("ECO_AUDIT")) {
			msg = "ECO AUDIT에 대한 관리권한이 없습니다.";
		}

		return msg;
	}
	//*******************************************************************
	//	BOM을 편집[수정,삭제,상신등]할 수 있는 권한이 있는지 판단하기
	//  sabun : 사번 ,   pid : mbom_master의 관리코드
	//*******************************************************************/
	public String getFgGrade(String sabun,String pid) throws Exception
	{
		String msg = "",where="",fg_code="FG",mgr_fg_code="MG";

		//MBOM_MASTER의 FG코드 찾기
		where = "where pid='"+pid+"'";
		fg_code =getColumData("mbom_master","fg_code",where);

		//MBOM_GRADE_MGR의 FG코드가 포함되었나 찾기
		mgr_fg_code = getMgrFgCode(sabun);
	
		//상신할 수 있는 권한이 있는지 판단하기
		if(mgr_fg_code.indexOf(fg_code) == -1) {
			msg = "해당 모델[FG]에대한 사용권한이 없습니다.";
		}

		return msg;
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
	//	쿼리데이터의 LEVEL NO의 합계
	//*******************************************************************/
	public int sumTotalLevelNo(String query) throws Exception
	{
		//변수 초기화
		int cnt = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cnt += rs.getInt("level_no") + 1;
		}
		
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
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumAllData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString(getcolumn)+",";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	주어진사번의 사업부 코드는
	//*******************************************************************/
	public String getDivCode(String sabun) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("ac_code");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	BOM GROUP관리의 해당사번의 FG코드 구하기
	//*******************************************************************/
	public String getMgrFgCode(String sabun) throws Exception
	{
		//변수 초기화
		String fg_code = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select keyname from mbom_grade_mgr where owner like '%"+sabun+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			fg_code += rs.getString("keyname");
		}
	
		stmt.close();
		rs.close();
		return fg_code;			
	}

}

