package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomHistoryDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리
	
	private String query = "";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class : MBOM_STR
	private eccComTable ecdt = null;				//help class : ECC_COM
	private eccReqTable ecrt = null;				//help class : ECC_REQ
	private eccOrdTable ecot = null;				//help class : ECC_ORD
	private eccBomTable ecbt = null;				//help class : ECC_BOM
	private eccModelTable ecmt = null;				//help class : ECC_MODEL
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public CbomHistoryDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		ECR에 관한 메소드 정의
	//		ECC_COM,ECC_REQ,ECC_ORD읽기
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 ECC_COM 정보 읽기
	//*******************************************************************/	
	public eccComTable readEccCom(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccComTable table = new com.anbtech.dcm.entity.eccComTable();
		
		query = "SELECT * FROM ecc_com where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setEccSubject(rs.getString("ecc_subject"));
			table.setEcoNo(rs.getString("eco_no"));	
			table.setEcrId(rs.getString("ecr_id"));	
			table.setEcrName(rs.getString("ecr_name"));	
			table.setEcrCode(rs.getString("ecr_code"));	
			table.setEcrDivCode(rs.getString("ecr_div_code"));	
			table.setEcrDivName(rs.getString("ecr_div_name"));	
			table.setEcrTel(rs.getString("ecr_tel"));	
			table.setEcrDate(rs.getString("ecr_date"));	
			table.setMgrId(rs.getString("mgr_id"));	
			table.setMgrName(rs.getString("mgr_name"));	
			table.setMgrCode(rs.getString("mgr_code"));	
			table.setMgrDivCode(rs.getString("mgr_div_code"));	
			table.setMgrDivName(rs.getString("mgr_div_name"));	
			table.setEcoId(rs.getString("eco_id"));	
			table.setEcoName(rs.getString("eco_name"));	
			table.setEcoCode(rs.getString("eco_code"));	
			table.setEcoDivCode(rs.getString("eco_div_code"));	
			table.setEcoDivName(rs.getString("eco_div_name"));	
			table.setEcoTel(rs.getString("eco_tel"));	
			table.setEccReason(rs.getString("ecc_reason"));	
			table.setEccFactor(rs.getString("ecc_factor"));	
			table.setEccScope(rs.getString("ecc_scope"));	
			table.setEccKind(rs.getString("ecc_kind"));	
			table.setPdgCode(rs.getString("pdg_code"));	
			table.setPdCode(rs.getString("pd_code"));	
			table.setFgCode(rs.getString("fg_code"));	
			table.setPartCode(rs.getString("part_code"));	
			table.setOrderDate(rs.getString("order_date"));	
			table.setFixDate(rs.getString("fix_date"));	
			table.setEccStatus(rs.getString("ecc_status"));	
		} else {
			table.setPid(anbdt.getID());	
			table.setEccSubject("");	
			table.setEcoNo("");	
			table.setEcrId("");	
			table.setEcrName("");	
			table.setEcrCode("");	
			table.setEcrDivCode("");	
			table.setEcrDivName("");	
			table.setEcrTel("");	
			table.setEcrDate(anbdt.getDateNoformat());	
			table.setMgrId("");	
			table.setMgrName("");
			table.setMgrCode("");	
			table.setMgrDivCode("");	
			table.setMgrDivName("");	
			table.setEcoId("");	
			table.setEcoName("");	
			table.setEcoCode("");	
			table.setEcoDivCode("");	
			table.setEcoDivName("");	
			table.setEcoTel("");	
			table.setEccReason("");	
			table.setEccFactor("");	
			table.setEccScope("");	
			table.setEccKind("");	
			table.setPdgCode("");	
			table.setPdCode("");	
			table.setFgCode("");	
			table.setPartCode("");	
			table.setOrderDate("");	
			table.setFixDate("");	
			table.setEccStatus("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// 관리번호로 해당 ECC_REQ 정보 읽기
	//*******************************************************************/	
	public eccReqTable readEccReq(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccReqTable table = new com.anbtech.dcm.entity.eccReqTable();
		
		query = "SELECT * FROM ecc_req where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setChgPosition(rs.getString("chg_position"));	
			table.setTrouble(rs.getString("trouble"));	
			table.setCondition(rs.getString("condition"));	
			table.setSolution(rs.getString("solution"));	
			table.setFname(rs.getString("fname"));	
			table.setSname(rs.getString("sname"));	
			table.setFtype(rs.getString("ftype"));	
			table.setFsize(rs.getString("fsize"));	
			table.setAppNo(rs.getString("app_no"));	
		} else {
			table.setPid("");	
			table.setChgPosition("");	
			table.setTrouble("");	
			table.setCondition("");	
			table.setSolution("");	
			table.setFname("");	
			table.setSname("");	
			table.setFtype("");	
			table.setFsize("");	
			table.setAppNo("");	
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// 관리번호로 해당 ECC_ORD 정보 읽기
	//*******************************************************************/	
	public eccOrdTable readEccOrd(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccOrdTable table = new com.anbtech.dcm.entity.eccOrdTable();
		
		query = "SELECT * FROM ecc_ord where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setChgPosition(rs.getString("chg_position"));	
			table.setTrouble(rs.getString("trouble"));	
			table.setCondition(rs.getString("condition"));	
			table.setSolution(rs.getString("solution"));	
			table.setFname(rs.getString("fname"));	
			table.setSname(rs.getString("sname"));	
			table.setFtype(rs.getString("ftype"));	
			table.setFsize(rs.getString("fsize"));	
			table.setAppNo(rs.getString("app_no"));	
		} else {
			table.setPid("");	
			table.setChgPosition("");	
			table.setTrouble("");	
			table.setCondition("");	
			table.setSolution("");	
			table.setFname("");	
			table.setSname("");	
			table.setFtype("");	
			table.setFsize("");	
			table.setAppNo("");	
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//--------------------------------------------------------------------
	//
	//		검색조건에 맞는 리스트 출력
	//		
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ECC COM 전체LIST 가져오기
	//  검색조건 [기본관리] : ecc_subject,eco_no, ecr_s_date/ecr_e_date[발의시작/종료]
	//						  ecr_name,eco_s_date/eco_e_date[적용시작/종료]
	//						  eco_name,ecc_status
	//*******************************************************************/	
	public ArrayList getBaseEccComList(String ecc_subject,String eco_no,String ecr_s_date,String ecr_e_date,
			String ecr_name,String eco_s_date,String eco_e_date,String eco_name,String ecc_status,
			String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		ecr_s_date = str.repWord(ecr_s_date,"/","");		//발의일 검색시작일
		ecr_e_date = str.repWord(ecr_e_date,"/","");		//발의일 검색종료일
		eco_s_date = str.repWord(eco_s_date,"/","");		//적용일 검색시작일
		eco_e_date = str.repWord(eco_e_date,"/","");		//적용일 검색종료일

		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where 문장 만들기
		where = "ecc_subject like '%"+ecc_subject+"%' and eco_no like '%"+eco_no+"%' and ";
		where += "ecr_date >= '"+ecr_s_date+"' and ecr_date <= '"+ecr_e_date+"' and ";
		where += "order_date >= '"+eco_s_date+"' and order_date <= '"+eco_e_date+"' and ";
		where += "ecr_name like '%"+ecr_name+"%' and eco_name like '%"+eco_name+"%' and ";
		where += "ecc_status like '%"+ecc_status+"%'";

		//총갯수 구하기
		query = "SELECT COUNT(*) FROM ECC_COM where "+where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM ECC_COM where "+where;
		query += " order by eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);
			
				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	
				
				ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR반려";
				else if(ecc_status.equals("1")) ecc_status = "ECR작성";
				else if(ecc_status.equals("2")) ecc_status = "ECR결재";
				else if(ecc_status.equals("3")) ecc_status = "ECR책임자접수";
				else if(ecc_status.equals("4")) ecc_status = "ECR담당자접수";
				else if(ecc_status.equals("5")) ecc_status = "ECO반려";
				else if(ecc_status.equals("6")) ecc_status = "ECO작성";
				else if(ecc_status.equals("7")) ecc_status = "ECO결재";
				else if(ecc_status.equals("8")) ecc_status = "ECO승인";
				else if(ecc_status.equals("9")) ecc_status = "ECO확정";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public eccComTable getBaseDisplayPage(String ecc_subject,String eco_no,String ecr_s_date,String ecr_e_date,
			String ecr_name,String eco_s_date,String eco_e_date,String eco_name,String ecc_status,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		ecr_s_date = str.repWord(ecr_s_date,"/","");		//발의일 검색시작일
		ecr_e_date = str.repWord(ecr_e_date,"/","");		//발의일 검색종료일
		eco_s_date = str.repWord(eco_s_date,"/","");		//적용일 검색시작일
		eco_e_date = str.repWord(eco_e_date,"/","");		//적용일 검색종료일

		String query="",where="",mode="sch_base";	
		String para = "&ecc_subject="+ecc_subject+"&eco_no="+eco_no+"&ecr_s_date="+ecr_s_date+"&ecr_e_date=";
			  para += ecr_e_date+"&ecr_name="+ecr_name+"&eco_s_date="+eco_s_date+"&eco_e_date="+eco_e_date;
			  para += "&eco_name="+eco_name+"&ecc_status="+ecc_status;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where 문장 만들기
		where = "ecc_subject like '%"+ecc_subject+"%' and eco_no like '%"+eco_no+"%' and ";
		where += "ecr_date >= '"+ecr_s_date+"' and ecr_date <= '"+ecr_e_date+"' and ";
		where += "order_date >= '"+eco_s_date+"' and order_date <= '"+eco_e_date+"' and ";
		where += "ecr_name like '%"+ecr_name+"%' and eco_name like '%"+eco_name+"%' and ";
		where += "ecc_status like '%"+ecc_status+"%'";
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM ECC_COM where "+where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM ECC_COM where "+where;
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist에 담기
		table = new eccComTable();
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
	// ECC COM 전체LIST 가져오기
	//  검색조건 [선택조건] : pdg_code,pd_code,e_fg_code[발생FG],a_fg_code[적용FG]
	//						  part_code[발생부품],item_code[적용부품]
	//						  ecc_reason,ecc_factor,ecc_scope,ecc_kind
	//*******************************************************************/	
	public ArrayList getConditionEccComList(String pdg_code,String pd_code,String e_fg_code,String a_fg_code,
			String part_code,String item_code,String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,
			String page,int max_display_cnt) throws Exception
	{
		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where 문장 만들기
		where = "pdg_code like '%"+pdg_code+"%' and pd_code like '%"+pd_code+"%' and ";
		where += "a.fg_code like '%"+e_fg_code+"%' and b.fg_code like '%"+a_fg_code+"%' and ";
		where += "a.ecc_reason like '%"+ecc_reason+"%' and ecc_factor like '%"+ecc_factor+"%' and ";
		where += "ecc_scope like '%"+ecc_scope+"%' and ecc_kind like '%"+ecc_kind+"%' and ";
		where += "a.part_code like '%"+part_code+"%' and ";
		where += "(c.parent_code like '%"+item_code+"%' or c.child_code like '"+item_code+"%') and "; 
		where += "a.eco_no = b.eco_no and b.eco_no = c.eco_no and a.eco_no = c.eco_no";

		//총갯수 구하기
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		query += " order by a.eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);

				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	

				String ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR반려";
				else if(ecc_status.equals("1")) ecc_status = "ECR작성";
				else if(ecc_status.equals("2")) ecc_status = "ECR결재";
				else if(ecc_status.equals("3")) ecc_status = "ECR책임자접수";
				else if(ecc_status.equals("4")) ecc_status = "ECR담당자접수";
				else if(ecc_status.equals("5")) ecc_status = "ECO반려";
				else if(ecc_status.equals("6")) ecc_status = "ECO작성";
				else if(ecc_status.equals("7")) ecc_status = "ECO결재";
				else if(ecc_status.equals("8")) ecc_status = "ECO승인";
				else if(ecc_status.equals("9")) ecc_status = "ECO확정";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public eccComTable getConditionDisplayPage(String pdg_code,String pd_code,String e_fg_code,String a_fg_code,
			String part_code,String item_code,String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		String query="",where="",mode="sch_condition";	
		String para = "&pdg_code="+pdg_code+"&pd_code="+pd_code+"&e_fg_code="+e_fg_code+"&a_fg_code=";
			  para += a_fg_code+"&ecc_reason="+ecc_reason+"&ecc_factor="+ecc_factor+"&ecc_scope="+ecc_scope;
			  para += "&ecc_kind="+ecc_kind+"&part_code="+part_code+"&item_code="+item_code;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where 문장 만들기
		where = "pdg_code like '%"+pdg_code+"%' and pd_code like '%"+pd_code+"%' and ";
		where += "a.fg_code like '%"+e_fg_code+"%' and b.fg_code like '%"+a_fg_code+"%' and ";
		where += "a.ecc_reason like '%"+ecc_reason+"%' and ecc_factor like '%"+ecc_factor+"%' and ";
		where += "ecc_scope like '%"+ecc_scope+"%' and ecc_kind like '%"+ecc_kind+"%' and ";
		where += "a.part_code like '%"+part_code+"%' and ";
		where += "(c.parent_code like '%"+item_code+"%' or c.child_code like '"+item_code+"%') and "; 
		where += "a.eco_no = b.eco_no and b.eco_no = c.eco_no and a.eco_no = c.eco_no";

		//총갯수 구하기
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		query += " order by a.eco_no desc";
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist에 담기
		table = new eccComTable();
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
	// ECC COM 전체LIST 가져오기
	//  검색조건 [기본관리] : condition,solution,chg_position,trouble
	//*******************************************************************/	
	public ArrayList getContentEccComList(String condition,String solution,
			String chg_position,String trouble,String page,int max_display_cnt) throws Exception
	{
		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where 문장 만들기
		where = "(b.condition like '%"+condition+"%' and b.solution like '%"+solution+"%' and ";
		where += "b.chg_position like '%"+chg_position+"%' and b.trouble like '%"+trouble+"%' and a.pid=b.pid) or ";
		where += "(c.condition like '%"+condition+"%' and c.solution like '%"+solution+"%' and ";
		where += "c.chg_position like '%"+chg_position+"%' and c.trouble like '%"+trouble+"%' and a.pid=c.pid)";

		//총갯수 구하기
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		total_cnt = getTotalCount(query);

		//query문장 만들기
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		query += " order by a.eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);

				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	

				String ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR반려";
				else if(ecc_status.equals("1")) ecc_status = "ECR작성";
				else if(ecc_status.equals("2")) ecc_status = "ECR결재";
				else if(ecc_status.equals("3")) ecc_status = "ECR책임자접수";
				else if(ecc_status.equals("4")) ecc_status = "ECR담당자접수";
				else if(ecc_status.equals("5")) ecc_status = "ECO반려";
				else if(ecc_status.equals("6")) ecc_status = "ECO작성";
				else if(ecc_status.equals("7")) ecc_status = "ECO결재";
				else if(ecc_status.equals("8")) ecc_status = "ECO승인";
				else if(ecc_status.equals("9")) ecc_status = "ECO확정";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public eccComTable getContentDisplayPage(String condition,String solution,
			String chg_position,String trouble,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		String query="",where="",mode="sch_content";	
		String para = "&condition="+condition+"&solution="+solution+"&chg_position=";
			  para += chg_position+"&trouble="+trouble;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where 문장 만들기
		where = "(b.condition like '%"+condition+"%' and b.solution like '%"+solution+"%' and ";
		where += "b.chg_position like '%"+chg_position+"%' and b.trouble like '%"+trouble+"%' and a.pid=b.pid) or ";
		where += "(c.condition like '%"+condition+"%' and c.solution like '%"+solution+"%' and ";
		where += "c.chg_position like '%"+chg_position+"%' and c.trouble like '%"+trouble+"%' and a.pid=c.pid)";


		//총갯수 구하기
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		total_cnt = getTotalCount(query);

		//query문장 만들기
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		query += " order by a.eco_no desc";
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist에 담기
		table = new eccComTable();
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
	//		설계변경 적용모델에 관한 메소드
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	설계변경 적용모델 ECO_NO별 LIST
	//		TABLE : ECC_MODEL
	//*******************************************************************/
	public ArrayList getEcoModel(String eco_no) throws Exception
	{
		//변수 초기화
		String query="",where="";	
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		eccModelTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM ecc_model WHERE eco_no ='"+eco_no+"' ORDER BY fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new eccModelTable();
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table_list.add(table);
		}
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
}

