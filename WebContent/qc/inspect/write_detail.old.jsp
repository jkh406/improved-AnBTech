<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String mode				= request.getParameter("mode");
	String request_no		= request.getParameter("request_no");		//검사의뢰번호
	String item_code		= request.getParameter("item_code");		//검사대상품목코드
	String inspection_code	= request.getParameter("inspection_code");	//검사항목코드
	String sampled_quantity	= request.getParameter("sampled_quantity");	//시료수량
	int no = Integer.parseInt(sampled_quantity);

	mode = "write_inspection_value";
%>

<HTML>
<HEAD><TITLE>검사결과등록</TITLE></HEAD>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../qc/images/bt_reg_chk_result.gif" alt="검사결과등록"></TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=19><TD></TD></TR>
  
	<!--리스트-->
	<TR height=100%>
		<TD vAlign=top>
		<form method=post name="writeForm" action='QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		    <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			    <TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
					<TR vAlign=middle height=23>
						  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>측정치</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
						  <TD noWrap width=50 align=middle class='list_title'>불량</TD>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
		for(int i=1; i<=no; i++){	
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'><%=i%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><input type='text' size='15' maxlength='15' name='inspection_value_<%=i%>'></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><input type='checkbox' name='is_passed_<%=i%>' value='N'></td>
					</TR>
					<TR><TD colSpan=5 background="../qc/images/dot_line.gif"></TD></TR>				
<%				no++;
	}	
%>	
				</TBODY></TABLE></DIV></TD></TR>
				<!--꼬릿말-->
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<IMG src='../images/bt_save.gif' onclick="javascript:checkForm('<%=sampled_quantity%>')" align='absmiddle' style='cursor:hand'>
					<IMG src='../images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'>
					</TD></TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
			</TD></TR></TABLE></TBODY></TABLE>
	<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='item_code' value='<%=item_code%>'>
<input type='hidden' name='inspection_code' value='<%=inspection_code%>'>
<input type='hidden' name='inspected_quantity' value='<%=sampled_quantity%>'>
</form>
</BODY>
</HTML>


<script language='javascript'>
//필수 입력사항 체크
function checkForm(no){ 
	var f = document.writeForm;

	for(var i=1; i<=no; i++){
		if(eval("f.inspection_value_" + i + ".value;") == ''){
			alert(i + "번째 품목의 측정치를 입력하십시오.");
			eval("f.inspection_value_" + i + ".focus();");
			return;
		}
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 585;
	item_list.style.height = div_h;
}
</script>