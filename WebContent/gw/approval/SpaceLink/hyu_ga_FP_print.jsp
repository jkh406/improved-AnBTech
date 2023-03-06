<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "휴(공)가원 보기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식

	//휴가원 내용관련
	String query		= "&nbsp;";
	String div_name		= "&nbsp;";			//부서명
	String user_name	= "&nbsp;";			//대상자 명
	String user_rank	= "&nbsp;";			//대상자 직위
	String doc_huga		= "&nbsp;";			//휴가종류
	String huga_code	= "&nbsp;";			//휴가코드
	String purpose		= "&nbsp;";			//사유
	String syear		= "";				//시작 년
	String smonth		= "";				//  월
	String sdate		= "";				//  일
	String edyear		= "";				// 종료 년
	String edmonth		= "";			    //     월
	String eddate		= "";				//    일
	String doc_receiver = "&nbsp;";		    //업무인수인계자
	String doc_tel		= "&nbsp;";			//긴급연락처
	String doc_date		= "";			    //작성 년월일
	String period		= "";				//from ~ to 기간 : 일

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
	masterDAO.getTable_MasterPid(pid);	
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
		
		//결재선
		cmt = app.getApComment(); if(cmt == null) cmt = "";
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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_HDY")) {
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
	connMgr.freeConnection("mssql",con);

	/*********************************************************************
	// 	휴가원 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","hd_var","gt_purpose","hd_name","u_year","u_month",
		"u_date","tu_year","tu_month","tu_date","gt_time_per","proxy","em_tel","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name	= bean.getData("ac_name");			//부서명
		user_name	= bean.getData("user_name");		//작성자 명
		user_rank	= bean.getData("user_rank");		//작성자 직위
		huga_code	= bean.getData("hd_var");			//휴가종류 code
		doc_huga	= bean.getData("hd_name");			//휴가종류
		purpose		= bean.getData("gt_purpose");		//사유
		syear		= bean.getData("u_year");				//시작 년
		smonth		= fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    월
		sdate		= fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    일
		edyear		= bean.getData("tu_year");			//종료 년
		edmonth		= fmt.toDigits(Integer.parseInt(bean.getData("tu_month")));			//    월
		eddate		= fmt.toDigits(Integer.parseInt(bean.getData("tu_date")));			//    일
		period		= bean.getData("gt_time_per");		//기간
		doc_receiver = bean.getData("proxy");			//업무인수인계자
		doc_tel		= bean.getData("em_tel");			//긴급연락처
		doc_date	= bean.getData("in_date");			//작성년월일
	} //while

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);			//	  월
		wdate = doc_date.substring(8,10);			//	  일
	}
	

%>
<html>
<head>
<title>휴(공)가원</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5" oncontextmenu="return false">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">휴(공)가원</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- 닫기 -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=50 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
			if(vdate.length() == 0)	{//검토자
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("전결");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%>		
		</TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<%
			if(ddate.length() == 0)	{//승인자
				out.println("&nbsp;");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>			
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">소속부서</td>
				<td width="35%" class="bg_06"><%=div_name%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">대상자</td>
				<td width="35%" class="bg_06"><%=user_rank%> <%=user_name%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">휴가구분</td>
				<td width="35%" class="bg_06"><%=doc_huga%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">신청사유</td>
				<td width="35%" class="bg_06"><%=purpose%><img src='' width='0' height='0'></td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">시작일</td>
				<td width="35%" class="bg_06"><%=syear%>년 <%=smonth%>월  <%=sdate%>일</td>
				<td width="15%" height="25" align="middle" class="bg_05">종료일</td>
				<td width="35%" class="bg_06"><%=edyear%>년 <%=edmonth%>월 <%=eddate%>일</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">휴가일수</td>
				<td width="35%" class="bg_06">총 <%=period%> 일간</td>
				<td width="15%" height="25" align="middle" class="bg_05">긴급연락처</td>
				<td width="35%" class="bg_06"><%=doc_tel%><img src='' width='0' height='0'></td></tr>								
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">업무인수자</td>
				<td width="35%" class="bg_06"><%=doc_receiver%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">신청일자</td>
				<td width="35%" class="bg_06"><%=wyear%>년 <%=wmonth%>월 <%=wdate%>일</td></tr></table>
	</td></tr></table>
<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_huga' value='<%=huga_code%>'>
	<input type='hidden' name='purpose' value='<%=purpose%>'>
	<input type='hidden' name='doc_syear' value='<%=syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=sdate%>'>
	<input type='hidden' name='doc_edyear' value='<%=edyear%>'>
	<input type='hidden' name='doc_edmonth' value='<%=edmonth%>'>
	<input type='hidden' name='doc_eddate' value='<%=eddate%>'>
	<input type='hidden' name='doc_receiver' value='<%=doc_receiver%>'>
	<input type='hidden' name='doc_tel' value='<%=doc_tel%>'>
	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
</form>
</body></html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

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
	
	document.eForm.action = "hyu_ga_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}

//전자결재후 닫기
function winClose()
{
	window.returnValue='RL';
	self.close();
}
-->
</script>
