package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
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
	public CbomModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		ECR에 관한 메소드 정의
	//
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

	//*******************************************************************
	// ECC COM 전체LIST 가져오기
	// id : login 사번, code : login 부서관리코드
	//*******************************************************************/	
	public ArrayList getEccComList(String sItem,String sWord,String boxKind,String id,
			String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		String query="",where="";
		String code = getUserDivMgrCode(id);		//해당사번의 부서관리코드

		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//함종류별 조건문 만들기
		if(boxKind.equals("IW")) {				//개인작성함
			where = "(ecr_id='"+id+"' or eco_id='"+id+"') and ";
			where += "ecc_status in ('1','6') ";
		} else if(boxKind.equals("IR")) {		//개인수신함
			where = "(ecr_id='"+id+"' and ecc_status='0') or ";			//ECR반려
			where += "(eco_id='"+id+"' and ecc_status='5') or ";		//ECO반려
			where += "(mgr_id='"+id+"' and ecc_status='3') or ";		//기술검토책임자 접수
			where += "(eco_id='"+id+"' and ecc_status='4') ";			//기술검토담당자 접수
		} else if(boxKind.equals("IS")) {		//개인발신함
			where = "(ecr_id='"+id+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";	//ECR 시작
			where += "(eco_id='"+id+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO 시작
		} else if(boxKind.equals("DR")) {		//부서수신함
			where = "(mgr_code='"+code+"' and ecc_status='3') or ";		//ECR 책임자접수
			where += "(eco_code='"+code+"' and ecc_status='4') ";		//ECR 담당자접수
		} else if(boxKind.equals("DS")) {		//부서발신함
			where = "(ecr_code='"+code+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR 시작
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO 시작
		} else if(boxKind.equals("EA")) {		//ECR AUDIT
			where = "(ecc_status = '8') ";
		}
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM ECC_COM where ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM ECC_COM where "+sItem+" like '%"+sWord+"%' and ";
		query += where;
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
	// MBOM_MASTER 화면에서 페이지로 바로가기 표현하기
	//*******************************************************************/	
	public eccComTable getDisplayPage(String sItem,String sWord,String boxKind,String id,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		String query="",where="",mode="";	
		String code = getUserDivMgrCode(id);		//해당사번의 부서관리코드

		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//함종류별 조건문 만들기
		if(boxKind.equals("IW")) {				//개인작성함
			where = "(ecr_id='"+id+"' or eco_id='"+id+"') and ";
			where += "ecc_status in ('1','6') ";
			mode = "ecc_iwlist";
		} else if(boxKind.equals("IR")) {		//개인수신함
			where = "(ecr_id='"+id+"' and ecc_status='0') or ";			//ECR반려
			where += "(eco_id='"+id+"' and ecc_status='5') or ";		//ECO반려
			where += "(mgr_id='"+id+"' and ecc_status='3') or ";		//기술검토책임자 접수
			where += "(eco_id='"+id+"' and ecc_status='4') ";			//기술검토담당자 접수
			mode = "ecc_irlist";
		} else if(boxKind.equals("IS")) {		//개인발신함
			where = "(ecr_id='"+id+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR 시작
			where += "(eco_id='"+id+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO 시작
			mode = "ecc_islist";
		} else if(boxKind.equals("DR")) {		//부서수신함
			where = "(mgr_code='"+code+"' and eco_no like 'ECR%' and ecc_status='3') or ";		//ECR 책임자접수
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status='4') ";		//ECR 담당자접수
			mode = "ecc_drlist";
		} else if(boxKind.equals("DS")) {		//부서발신함
			where = "(ecr_code='"+code+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR 시작
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO 시작
			mode = "ecc_dslist";
		} else if(boxKind.equals("EA")) {		//ECR AUDIT
			where = "(ecc_status = '8') ";
		}
		
		//총갯수 구하기
		query = "SELECT COUNT(*) FROM ECC_COM where ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query문장 만들기
		query = "SELECT * FROM ECC_COM where "+sItem+" like '%"+sWord+"%' and ";
		query += where;
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
			pagecut = "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">[Prev]</a>";
		}

		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">[Next]</a>";
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
	//		설계변경 항목관리에 대한 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	설계변경 선택항목 가져오기
	//		F01xx : 변경이유  ECO에 사용
	//		F02xx : 적용구분  ECO에 사용
	//		F03xx : 적용범위  ECO에 사용
	//		F04xx : 업무구분  ECR에 사용
	//*******************************************************************/
	public ArrayList getEccItem(String keyword) throws Exception
	{
		//변수 초기화
		String query="",where="";	
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		mbomEnvTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT spec FROM mbom_env WHERE m_code LIKE '"+keyword+"[0-9][0-9]' ORDER BY m_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomEnvTable();
				table.setSpec(rs.getString("spec"));
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

	//*******************************************************************
	//	임직원 정보 구하기 
	//   사번,이름,부서관리코드,부서코드,부서명,전화번호
	//*******************************************************************/
	public String[] getUserData(String sabun) throws Exception
	{
		//변수 초기화
		String data[] = new String[6]; 
		for(int i=0; i<6; i++) data[i]="";

		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT a.id,a.name,b.code,b.ac_code,b.ac_name,a.office_tel FROM ";
		query += "user_table a,class_table b WHERE (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("id");
			data[1] = rs.getString("name");
			data[2] = rs.getString("code");
			data[3] = rs.getString("ac_code");
			data[4] = rs.getString("ac_name");
			data[5] = rs.getString("office_tel");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	사번으로 부서관리코드 찾기 
	//*******************************************************************/
	public String getUserDivMgrCode(String sabun) throws Exception
	{
		//변수 초기화
		String data = ""; 
		
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT a.id,a.name,b.code,b.ac_code,b.ac_name,a.office_tel FROM ";
		query += "user_table a,class_table b WHERE (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("code");
		stmt.close();
		rs.close();
		return data;			
	}

}

