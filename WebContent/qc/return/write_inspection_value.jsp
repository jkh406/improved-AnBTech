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

	//리스트
	ArrayList inspection_value_list = new ArrayList();
	inspection_value_list = (ArrayList)request.getAttribute("INSPECTION_VALUE_LIST");
	Iterator table_iter = inspection_value_list.iterator();
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="top"><!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../qc/images/pop_reg_chk_result.gif" border='0' align='absmiddle' alt="검사결과등록"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE><br></td></tr>

  <tr><td valign="top" height="220"><!-- 검사내역 -->
	<form method=post name="writeForm" action='QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">
	  <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0>
        <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
			<TR vAlign=middle height=22>
			  <TD noWrap width=40 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>측정치</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>판정</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
	int no = 1;
	String[] values = new String[2];
	while(table_iter.hasNext()){
		values = (String[])table_iter.next();
		String inspection_value = values[0];
		String is_passed = values[1];
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='text' size='15' maxlength='15' name='inspection_value_<%=no%>' value='<%=inspection_value%>'></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><input type='radio' name='is_passed_<%=no%>' value='Y' <% if(is_passed.equals("Y")) out.print("checked"); %>>합격 <input type='radio' name='is_passed_<%=no%>' value='N' <% if(is_passed.equals("N")) out.print("checked"); %>>불합격</td>
			</TR>
			<TR><TD colSpan=5 background="../qc/images/dot_line.gif"></TD></TR>
<%	
		no++;
	}	
%>
		</TBODY></TABLE></td></tr>

  <tr><td align="top"><!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"> 
			<IMG src='../qc/images/bt_save.gif' onclick="javascript:checkForm('<%=sampled_quantity%>')" align='absmiddle' style='cursor:hand'>
		    <IMG src='../qc/images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD>
          </TR>
          <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR>
        </TBODY></TABLE>
</td></tr></table>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='request_no' value='<%=request_no%>'>
<input type='hidden' name='item_code' value='<%=item_code%>'>
<input type='hidden' name='inspection_code' value='<%=inspection_code%>'>
<input type='hidden' name='inspected_quantity' value='<%=sampled_quantity%>'>
</form>
</BODY></HTML>

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

		if(eval("f.inspection_value_" + i + ".value;").indexOf("#") > 0 || eval("f.inspection_value_" + i + ".value;").indexOf("|") > 0){
			alert("측정치에는 특수문자(#,|)를 포함할 수 없습니다.");
			eval("f.inspection_value_" + i + ".focus();");
			return;
		}
	}
	f.submit();
}
</script>