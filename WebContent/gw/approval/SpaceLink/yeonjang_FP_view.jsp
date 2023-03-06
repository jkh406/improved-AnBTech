<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "연장근무신청서 보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="java.sql.Connection"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기

	//연장근무신청서 내용관련
	String query = "";
	String div_name = "";			//부서명
	String user_name = "";			//대상자 명
	String user_rank = "";			//대상자 직위
	String doc_date = "";			//작성 년월일

	String doc_syear = "";			//근무일자 년
	String doc_smonth = "";			//근무일자 월
	String doc_sdate = "";			//근무일자 일
	String job_kind = "";			//분류
	String cost_prs = "";			//식비지급확인

	int work_cnt = 22;				//연장근무신청 작성 컬럼수

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

	//*********************************************************************
	// 결재선 내용 받기
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//관리번호
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//관리번호(링크정보로 pid와동일)
	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";	//PROCESS명
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";	//doc_ste

	//전자결재내용 & 결재선 읽기
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt = 0;									//결재선에 출력될 라인 갯수
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();
	
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
		else {	//협조 : 기결함문서시 협조자를 승인자 뒤로 보내기 위해
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_OTW")) {
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
	// 연장근무신청서 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"j_year","j_month","j_date","job_kind","cost_prs"};
	bean.setTable("janeup_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (ju_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_name = bean.getData("user_name");		//작성자 명
		user_rank = bean.getData("user_rank");		//작성자 직위
		doc_date = bean.getData("in_date");			//작성년월일

		doc_syear = bean.getData("j_year");			//근무일자 년
		doc_smonth = bean.getData("j_month");		//근무일자 월
		doc_sdate = bean.getData("j_date");			//근무일자 일
		job_kind = bean.getData("job_kind");		//구분
		cost_prs = bean.getData("cost_prs");		//식비지급확인
	} //while

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);			//	  월
		wdate = doc_date.substring(8,10);			//	  일
	}

	//근무자 찾기
	String[][] works = new String[work_cnt][5]; //근무자사번,이름,내용,퇴실시간,부서장확인
	for(int i=0; i<work_cnt; i++) for(int j=0; j<5; j++) works[i][j] = "";
	String[] workColumn = {"worker_id","worker_name","content","close_time","cfm"};
	bean.setTable("janeup_worker");			
	bean.setColumns(workColumn);
	bean.setOrder("ju_cid ASC");	
	query = "where (ju_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	
	int w = 0;
	while(bean.isAll()) {
		works[w][0] = bean.getData("worker_id");			//근무자사번
		works[w][1] = bean.getData("worker_name");		//이름
		works[w][2] = bean.getData("content");			//내용
		works[w][3] = bean.getData("close_time");		//퇴실시간
		works[w][4] = bean.getData("cfm");				//부서장확인
		w++;
	} //while

%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>연장근무신청서</title> 
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>
<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
	<% 	
	//결재문서인 경우 처리
	if(PROCESS.equals("APP_ING")) {  %>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
		<% if(doc_ste.equals("APV") || doc_ste.equals("APL")) { //검토, 승인단계 %>
			<a href='Javascript:winDecision();'><img src='../../../gw/img/button_approval.gif' align='middle' border='0'></a> <!-- 승인 -->
			<a href='Javascript:winReject();'><img src='../../../gw/img/button_reject.gif' align='middle' border='0'></a> <!-- 반려 -->
		<% } else { //협조단계 %>
			<a href='Javascript:winDecision();'><img src='../../../gw/img/button_agree.gif' align='middle' border='0'></a> <!-- 승인 -->
			<a href='Javascript:winReject();'><img src='../../../gw/img/button_reject.gif' align='middle' border='0'></a> <!-- 반려 -->
		<% } %>
			<a href='Javascript:winprint();'><img src='../../../gw/img/button_print.gif' align='middle' border='0'></a> <!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../../../gw/img/002_013_del.gif' align='middle' border='0'></a> <!-- 닫기 -->
		</div>
	<% } 
	//재작성및 View 문서	
	else { %>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
		<% if(PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX")) {	//임시보관함,반려함 처리 %> 
			<a href='Javascript:winRewrite();'><img src='../../images/bt_rewrite.gif' align='absmiddle' border='0'></a> <!-- 재작성 -->
			<a href='Javascript:winDelete(<%=pid%>);'><img src='../../images/bt_del.gif' align='absmiddle' border='0' alt='삭제'></a>
		<%	} %>
			
		<!-- 기안자의 상신취소[기안자 바로 앞단계시] : 200408 : 공통 -->
		<% if(app_cancel.equals("Y")) {		//상신취소 %> 
			<a href='Javascript:appCancel(<%=pid%>);'><img src='../../images/bt_app_cancel.gif' align='absmiddle' border='0' alt='상신취소'></a>
		<%	} %>
		<!-- 삭제문서로 전달받을경우 와 그렇지 않을경우 메뉴 : 200408 : 공통 -->
		<% if(PROCESS.equals("DEL_BOX")) {	%>
			<a href='Javascript:winclose();'><img src='../../images/bt_close.gif' align='absmiddle' border='0' alt='닫기'></a>
		<% } else { %>
			<a href='Javascript:winprint(<%=pid%>);'><img src='../../images/bt_print.gif' align='absmiddle' border='0' alt='인쇄'></a>
			<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='목록'></a>
		<% } %>
		</div>
	<% } %>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b>연 장 근 무 신 청 서 </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../../gw/img/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">메<p>모</td>
		<% if(line_cnt < 7) {    %> 
			<td width="420" height="96" rowspan=3 valign="top"><%=line%></td>
		<% } else {				 %>
			<td width="420" height="96" rowspan=3 align="center" valign="middle">
			<TEXTAREA rows=7 cols=66 readOnly style="text-align:left;font-size:9pt;border:1px solid #787878;"><%=t_line%></TEXTAREA>
			</td>
		<% }					 %>
		<td width="20" height="96" rowspan=3 align="center">결<p>재</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">기 안</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">검 토</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">승 인</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></td>   
		<td width="60" height="50" align="center">
		<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
			if(vdate.length() == 0)	{//검토자
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("전결");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%></td>   
		<td width="60" height="50" align="center">
		<%
			if(ddate.length() == 0)	{//승인자
				out.println("&nbsp;");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>	
		</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;<%=wname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=vname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=dname%>&nbsp;</td>   
	</tr>   
</table>
		
<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
	<tr>
		<td width="100" height="30" align="center" valign="middle">소 속 부 서</td>
		<td width="540" height="30" align="center" colspan=3>&nbsp;<%=div_name%></td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">연장 근무일</td>
		<td width="540" height="30" align="center" colspan=3>
			<%=doc_syear%> 년 <%=doc_smonth%> 월 <%=doc_sdate%> 일  </td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle"><b>근 무 자</b></td>
		<td width="340" height="30" align="center" valign="middle"><b>업무내용</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>퇴실시간</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>부서장확인</b></td>
	</tr>
	<%
	for(int i=0; i<work_cnt; i++) {
		out.println("<tr>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][1]+"</td>");
		out.println("<td width='340' height='24' align='center' valign='middle'>"+works[i][2]+"</td>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][3]+"</td>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][4]+"</td>");
		out.println("</tr>");
	}
	%>
<!--
	<tr>
		<td width="100" height="470" align="center" valign="top">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%	//근무자
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='21' align='center' valign='bottom'>");
					out.println(works[i][1]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=black></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="340" height="470" align="center" valign="top">
			<table width='340' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='340' height='21' align='center' valign='bottom'>");
					out.println(works[i][2]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="top">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='21' align='center' valign='bottom'>");
					out.println(works[i][3]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="top">
			<table width='95' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='95' height='21' align='center' valign='bottom'>");
					out.println(works[i][4]);
					out.println("</td></tr>");
					out.println("<td width='95' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
	</tr>
-->
	<tr>
		<td width="100" height="30" align="center" valign="middle"><b>분 류</b></td>
		<td width="340" height="30" align="center" valign="middle"><b>&nbsp;<%=job_kind%></b></td>
		<td width="100" height="30" align="center" valign="middle"><b>식비지급확인</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>&nbsp;<%=cost_prs%></b></td>
	</tr>
	<tr>
		<td width="640" height="60" align="center" colspan=4><br>
			위와 같이 특별 근무를 신청합니다. <br>
			<%=wyear%> 년 <%=wmonth%> 월 <%=wdate%> 일 <br>
			신청인 : <%=user_name%> <br><br></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'> 
	<tr>            
		<td width="50%" height="40" rowspan=3 align="left">SLCG-005-0</td> 
		<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)복사용지75g/m<sup>2</sup></td> 
	</tr>   
</table>

<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_syear' value='<%=doc_syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=doc_smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=doc_sdate%>'>
	<input type='hidden' name='job_kind' value='<%=job_kind%>'>
	<input type='hidden' name='cost_prs' value='<%=cost_prs%>'>

	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
	<input type='hidden' name='read_worker' value='R'>
</form>
</center>
</body>
</html>

<script language=javascript>
<!--
//결재하기
function winDecision()
{
	window.open("../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS","eleA_app_decision","width=200,height=100,scrollbar=yes,toolbar=no,status=yes,resizable=no");
	window.returnValue='RL';
}
//반려하기
function winReject()
{
	window.open("../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ","eleA_app_decision","width=200,height=100,scrollbar=yes,toolbar=no,status=yes,resizable=no");
	window.returnValue='RL';
}

//재작성하기
function winRewrite()
{
	var line = '<%=r_line%>';
	var ln = "";
	for(i=0; i<line.length; i++) {
		if(line.charAt(i) == '@') ln += '\n';
		else ln += line.charAt(i);
	}
	
	document.eForm.action = "yeonjang_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}
//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//전자결재후 닫기
function winClose()
{
	window.returnValue='RL';
	self.close();
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
