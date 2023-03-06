<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "자산 이관/반출 관리보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
	import="java.sql.Connection"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기

	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();		//1차일때
	com.anbtech.gw.entity.TableAppLine app2 = new com.anbtech.gw.entity.TableAppLine();		//2차일때

	//결재선 관련
	String pid = "";				//관리번호
	String doc_id = "";				//관련문서 관리번호
	String line="";					//읽은문서 결재선
	String r_line = "";				//재작성으로 넘겨주기
	String vdate = "";				//검토자 검토 일자
	String ddate = "";				//승인자 승인 일자
	String wid = "";				//기안자사번
	String vid = "";				//검토자사번
	String did = "";				//승인자사번
	String wname = "";				//기안자
	String vname = "";				//검토자
	String dname = "";				//승인자
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2차측 결재 관련
	String line2="";				//읽은문서 결재선
	String vdate2 = "";				//검토자 검토 일자
	String ddate2 = "";				//승인자 승인 일자
	String wid2 = "";				//기안자사번
	String vid2 = "";				//검토자사번
	String did2 = "";				//승인자사번
	String wname2 = "";				//기안자
	String vname2 = "";				//검토자
	String dname2 = "";				//승인자
	String PROCESS2 = "";			//PROCESS
	String doc_ste2 = "";			//doc_ste

	//자산관리
	String query = "";
	String writer_id = "",writer_name = "";
	String type = "";				//1차 or 2차 결재횟수
	String h_no = "";				//자산관리번호
	String as_no = "";				//자산번호
	String o_status = "";			//자산 이관/반출 구분
	String app_pid = "";			//1차 결재완료 결재관리번호
	String mode = "app_asset_view";

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	//*********************************************************************
	// 결재선 내용 받기
	//*********************************************************************
	pid = request.getParameter("pid");	if(pid == null) pid = "";				//관리번호
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";	//관련문서 관리번호

	//1차측 문서관리번호인지 2차측 문서관리번호 있지 알아보기
	String[] otColumn = {"h_no","as_no","type","o_status","as_status","pid","pid2"};
	bean.setTable("as_history");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("h_no DESC");	
	bean.setSearch("h_no",doc_id);
	bean.init_unique();

	String one_two = "";
	String plid = "",one="",two="";
	if(bean.isAll()){
		h_no = doc_id;
		as_no = bean.getData("as_no");
		type = bean.getData("type");
		o_status = bean.getData("o_status");
		plid = bean.getData("as_status");
		app_pid = bean.getData("pid"); if(app_pid == null) app_pid = "";
		one = bean.getData("pid");
		two = bean.getData("pid2");
	}

	//결재선및 결재선관리번호 찾기
	if(two.equals("empty")) {
		one_two = "one";				//1차 결재 완료
		pid = one;						//1차 결재선
	} else {
		one_two = "two";				//2차 결재 완료
		doc_id = one;					//1차 결재선
		pid = two;						//2차 결재선
	}

	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";		//PROCESS명
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";		//doc_ste

	//결재선 1.2차 변수
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//1차:협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt = 0;											//1차:결재선에 출력될 라인 갯수
	String ag_line2="",a_line2="",cmt2="",t_cmt2="",t_line2="";	//2차:협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt2 = 0;											//2차:결재선에 출력될 라인 갯수

	//--------------------------------
	//	1차부서 전자결재시 (2차부서 내용없음)
	//--------------------------------
	if(one_two.equals("one")) {
		//1차측 결재선 내용(결재선 포함)
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//결재선
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("기안")) {
				wname = app.getApName();	if(wname == null) wname="";		//기안자
				wid = app.getApSabun();		if(wid == null) wid="";			//기안자 사번
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("검토"))  {
				vname = app.getApName();	if(vname == null) vname="";		//검토자
				vid = app.getApSabun();		if(vid == null) vid="";			//검토자 사번
				vdate = app.getApDate();	if(vdate == null) vdate="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("승인"))  {
				dname = app.getApName();	if(dname == null) dname="";		//승인자
				did = app.getApSabun();		if(did == null) did="";			//승인자 사번
				ddate = app.getApDate();	if(ddate == null) ddate="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//협조
				if(PROCESS.equals("APP_BOX")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}

		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

	}

	//--------------------------------
	//	2차부서 전자결재시 (1차부서 내용포함)
	//--------------------------------
	else {	
		//1차측 결재선 내용(결재선 포함)
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//결재선
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("기안")) {
				wname = app.getApName();	if(wname == null) wname="";		//기안자
				wid = app.getApSabun();		if(wid == null) wid="";			//기안자 사번
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("검토"))  {
				vname = app.getApName();	if(vname == null) vname="";		//검토자
				vid = app.getApSabun();		if(vid == null) vid="";			//검토자 사번
				vdate = app.getApDate();	if(vdate == null) vdate="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("승인"))  {
				dname = app.getApName();	if(dname == null) dname="";		//승인자
				did = app.getApSabun();		if(did == null) did="";			//승인자 사번
				ddate = app.getApDate();	if(ddate == null) ddate="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//협조
				if(PROCESS.equals("APP_BOX")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}
		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

		//2차측 결재선 구하기
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);		
		ArrayList app2_line = new ArrayList();				
		app2_line = masterDAO.getTable_line();		
		Iterator app2_iter = app2_line.iterator();

		while(app2_iter.hasNext()) {
			app2 = (TableAppLine)app2_iter.next();
		
			//결재선
			cmt2 = app2.getApComment(); if(cmt2 == null) cmt2 = "";
			if(cmt2.length() != 0) { 
				t_cmt2 = "\r    "+cmt2; 
				cmt2 = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt2; 
				line_cnt2++; 
			}

			if(app2.getApStatus().equals("기안")) {
				wname2 = app2.getApName();		if(wname2 == null) wname2="";		//기안자
				wid2 = app2.getApSabun();		if(wid2 == null) wid2="";			//기안자 사번
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("검토"))  {
				vname2 = app2.getApName();		if(vname2 == null) vname2="";		//검토자
				vid2 = app2.getApSabun();		if(vid2 == null) vid2="";			//검토자 사번
				vdate2 = app2.getApDate();		if(vdate2 == null) vdate2="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("승인"))  {
				dname2 = app2.getApName();		if(dname2 == null) dname2="";		//승인자
				did2 = app2.getApSabun();		if(did2 == null) did2="";			//승인자 사번
				ddate2 = app2.getApDate();		if(ddate2 == null) ddate2="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else {	//협조
				if(PROCESS.equals("APP_BOX")) {
					ag_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					a_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				} else {
					line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					t_line += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				}
			}

		} //while
		if(ag_line2.length() != 0) { line2 += ag_line2;	t_line2 += a_line2; }	

	}//if
	
	//결재선 진행상태 알아보기 [APV(검토)단계이면 기안자가 상신취소가능] : 200408 : 공통
	String app_status = "",wrt_id="",app_line_data="",app_cancel="N";
	com.anbtech.gw.entity.TableAppMaster tabM = new com.anbtech.gw.entity.TableAppMaster();
	ArrayList tabML = new ArrayList();	
	tabML = masterDAO.getTable_MasterPid(pid);	
	Iterator tab_iter = tabML.iterator();
	while(tab_iter.hasNext()) {
		tabM = (TableAppMaster)tab_iter.next();
		app_status = tabM.getAmAppStatus();						//현결재 진행상태
		wrt_id = tabM.getAmWriter();							//작성자
		app_line_data = tabM.getAmAppLine(); 
		if(app_line_data.length() >2)
			app_line_data = app_line_data.substring(0,3);		//결재라인중 첫단계
	}
	//결재취소 가능여부 판단
	if(wrt_id.equals(login_id) && app_status.equals(app_line_data)) {
		app_cancel="Y";
	}

	//닫기
	connMgr.freeConnection("mssql",con);				//커넥션 닫기

	/*********************************************************************
	 	전달변수
		관리종류 [o_status : t:자산이관, o:자산반출]
		결재상태 [2:1차상신대기,3:1차결재완료,4:2차상신대기,5:2차결재완료]
	*********************************************************************/	
	String title_name = "";
	if(o_status.equals("t")) title_name = "자산이관";
	else if(o_status.equals("o")) title_name = "자산반출";
	
%>

<html>
<head><title>자산 이관/반출</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 자산관리</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<% 	//결재문서인 경우 처리
					if(PROCESS.equals("APP_ING")) {  %>
						<% if(doc_ste.equals("APV") || doc_ste.equals("APL")) { //검토, 승인단계 %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='승인'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='반려'></a>
						<% } else { //협조단계 %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='합의'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='반려'></a>
						<% } %>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='목록'></a>
				<%	} else { //재작성및 View 문서	 %> 
						<% if(PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX")) {	//임시보관함,반려함 처리 %> 
							<a href='Javascript:winDelete(<%=pid%>);'><img src='../../images/bt_del.gif' align='absmiddle' border='0' alt='삭제'></a>
						<%	} %>

						<!-- 삭제문서로 전달받을경우 와 그렇지 않을경우 메뉴 : 200408 : 공통 -->
						<% if(PROCESS.equals("DEL_BOX")) {	%>
							<a href='Javascript:winclose();'><img src='../../images/bt_close.gif' align='absmiddle' border='0' alt='닫기'></a>
						<% } else { %>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='목록'></a>
						<% } %>
				<% } %>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>					
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate.length() == 0)	{//검토자
							if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center" height=200><iframe id="iframe_main" src="../../../servlet/AssetServlet?mode=<%=mode%>&h_no=<%=h_no%>&as_no=<%=as_no%>" width="100%" height="100%" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no">
</iframe></td></tr></table>

<%	if(line2.length() != 0) { //2차측 부서 전자결재시만 보여준다 %>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
					<% if(wid2.length() != 0) { %>
						<img src="../../../gw/approval/sign/<%=wid2%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>
					<% } %>
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate2.length() == 0)	{//검토자
							if(ddate2.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid2 + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate2.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did2 + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname2%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>
<%	}	%>

<form name="eForm" method="post" encType="multipart/form-data">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="PID" value='<%=pid%>'>
</form>

</body>
</html>

<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//결재하기
function winDecision()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS&link_id=<%=doc_id%>&app_id=<%=pid%>',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}
//반려하기
function winReject()
{				
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ&link_id=<%=doc_id%>&app_id=<%=pid%>',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//출력하기
function winprint()
{
	wopen('../../../servlet/ApprovalDetailServlet?PID=<%=doc_id%>&mode=APP_PNT',"print","730","600","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//닫기
function winclose()
{
	if(window.name == 'save_doc') self.close();		//삭제문서 전자우편에서 열때
	else history.go(-1);							//삭제문서 바탕화면에서 열때
}
//상신취소 진행하기
function appCancel(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_CANCEL&PID="+pid;
	document.eForm.submit();
}
//보관,반려문서 삭제하기
function winDelete(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_DELETE&PID="+pid;
	document.eForm.submit();
}
-->
</script>