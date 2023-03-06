<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String mode			= request.getParameter("mode");
	String item_class	= request.getParameter("item_class");
	String mid			= request.getParameter("mid");

	bean.openConnection();

	String item_name	= "";
	if(mode.equals("modify") && mid != null){
		String query = "SELECT item_class,item_name,essential FROM em_input_item_table WHERE mid = '" + mid + "'";
		bean.executeQuery(query);
		bean.next();
		item_name = bean.getData("item_name");
		item_class = bean.getData("item_class");
		String essential = bean.getData("essential");

		//필수항목인 경우는 수정할 수 없도록 한다.
		if(essential.equals("1")){
%>
		<script language='javascript'>
			alert("필수항목은 수정할 수 없습니다.");
			self.close();
		</script>
<%
		}
	}
%>
<HTML><HEAD><TITLE>입력항목등록</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="document.add_form.item_name.focus();" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_item_mgr.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="add_form" method="post" action="add_item_process.jsp" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="40%" height="25" class="bg_03" background="../images/bg-01.gif">항목명</td>
           <td width="60%" height="25" class="bg_04"><input type='text' name='item_name' value='<%=item_name%>' size="15"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='item_class' value='<%=item_class%>'>
<input type='hidden' name='mid' value='<%=mid%>'>
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><input type='image' src='../images/bt_save.gif' border='0' align='absmiddle' onClick='add_item();'> <% if(!mode.equals("add")){ %><input type='image' src='../images/bt_del.gif' border='0' align='absmiddle' onClick='javascript:del_item();'><%}%> <input type='image' src='../images/bt_close.gif' border='0' align='absmiddle' onClick='javascript:self.close();'></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<script language="javascript">

function add_item(){ 
	
	var f = document.add_form;
	if(f.item_name.value == ''){
		alert("항목명을 입력하세요.");
		f.item_name.focus();
		return;
	}

	f.submit();
}

function del_item(){ 
	
	var f = document.add_form;
	f.mode.value = "delete";
	f.submit();
}
</script>