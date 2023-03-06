<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*,com.anbtech.qc.entity.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	FailureInfoTable table;
%>

<%
	String mode				= request.getParameter("mode");

	//불량정보 가져오기
	table = (FailureInfoTable)request.getAttribute("FAILURE_INFO");
	String request_no			= table.getRequestNo();
	String item_code			= table.getItemCode();
	String serial_no			= table.getSerialNo();
	String inspection_code		= table.getInspectionCode()==null?"":table.getInspectionCode();
	String why_failure			= table.getWhyFailure();

	//불량 리스트 가져오기
	ArrayList failure_list = new ArrayList();
	failure_list = (ArrayList)request.getAttribute("FAILURE_LIST");
	table = new FailureInfoTable();
	Iterator table_iter = failure_list.iterator();
%>

<HTML><HEAD><TITLE>불량내역</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../qc/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form method=post name="writeForm" action='QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../qc/images/pop_bad_info.gif" border='0' alt="불량내역"></TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=19><TD></TD></TR>

		<TR><TD align='middle' valign="top"><!-- 검사품목정보 -->
	
			<TABLE  cellspacing=0 cellpadding=0 width="98%" border=0>
				<TBODY>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
					   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사의뢰번호</TD>
					   <TD width="30%" height="25" class="bg_04"><%=request_no%></TD>
					   <TD width="20%" height="25" class="bg_03" background="../qc/images/bg-01.gif">검사품목코드</TD>
					   <TD width="30%" height="25" class="bg_04"><%=item_code%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>
		</TD></TR>
	<TR><TD height='5'></TD></TR>
	<TR><TD align='middle' valign="top"><!-- 부적합리스트 -->
		<TABLE border=0 width='98%'><TR><TD align=left><IMG src='../qc/images/title_badinfo_list.gif' border='0' alt='불량리스트'></TD></TR></TABLE></TD></TR>

	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		    <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			    <TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
					<TR vAlign=middle height=23>
						  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>검사항목코드</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>검사항목명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>불량원인</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>품목일련번호</TD>
					<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
		int no = 1;
		while(table_iter.hasNext()){
		table = (FailureInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getInspectionCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getInspectionName()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getWhyFailure()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getSerialNo()%></TD>
					</TR>
					<TR><TD colSpan=9 background="../qc/images/dot_line.gif"></TD></TR>								
<%				no++;
	}	
%>	
				</TBODY></TABLE></DIV></TD></TR>
				<!--꼬릿말-->
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					 <IMG src='../qc/images/bt_close.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD></TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
			</TD></TR></TABLE></TBODY></TABLE>
	<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='item_code' value='<%=item_code%>'>
</form>
</BODY>
</HTML>


<script language='javascript'>
//필수 입력사항 체크
function checkForm(no){ 
	var f = document.writeForm;

	if(f.serial_no.value < 3){
		alert("품목일련번호를 입력하십시오.");
		f.serial_no.focus();
		return;	
	}

	if(f.inspection_code.value < 3){
		alert("문제가 발생한 검사항목코드를 입력하십시오.");
		f.inspection_code.focus();
		return;	
	}

	if(f.why_failure.value < 3){
		alert("불량이 발생한 원인을 입력하십시오.");
		f.why_failure.focus();
		return;	
	}

	f.submit();
}

function list(request_no,item_code){ 
	location.href = "../servlet/QualityCtrlServlet?mode=write_failure_info&request_no="+request_no+"&item_code="+item_code;
}

function delete_item(request_no,item_code,inspection_code){ 
	location.href = "../servlet/QualityCtrlServlet?mode=delete_failure_info&request_no="+request_no+"&item_code="+item_code+"&inspection_code="+inspection_code;
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 607;
	item_list.style.height = div_h;
}
</script>