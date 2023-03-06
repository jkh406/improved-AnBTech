<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String mode		= request.getParameter("mode");
	String code		= request.getParameter("code");
	String name		= "";
	String name2	= "";
	String stat		= "";

	if(mode.equals("add")){
		bean.openConnection();
		String query = "SELECT MAX(code) FROM maker_code_table";
		bean.executeQuery(query);
		bean.next();

		if(bean.getData(1)==null) code = "10000";
		else code = Integer.toString(Integer.parseInt(bean.getData(1)) + 1);
	}
	else if(mode.equals("update")){
		bean.openConnection();
		String query = "SELECT * FROM maker_code_table WHERE code = '"+code+"'";
		bean.executeQuery(query);
		bean.next();

		name	= bean.getData("name");
		name2	= bean.getData("name2");
		if(name2 == null) name2 = "";
		stat	= bean.getData("stat");
	}
%>
<HTML>
<HTML><HEAD><TITLE>승인업체정보입력(수정)</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_comp_u.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form method="post" action="addCompVendorProcess.jsp" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">업체코드</td>
           <td width="65%" height="25" class="bg_04"><input type=text name=code size=10 value="<%=code%>" readOnly></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">업체명1</td>
           <td width="65%" height="25" class="bg_04"><input type=text name=name size=30 value="<%=name%>" class="text_01"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">업체명2</td>
           <td width="65%" height="25" class="bg_04"><input type=text name=name2 size=30 value="<%=name2%>"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="35%" height="25" class="bg_03" background="../images/bg-01.gif">상태</td>
           <td width="65%" height="25" class="bg_04">
				<select name='stat'>
					<option value='1'>정상</option>
					<option value='0'>정지</option>
				</select>
				<%	if(!stat.equals("")){	%>
						<script language='javascript'>
							document.forms[0].stat.value = '<%=stat%>';
						</script>
				<%	}	%>		   
		   </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
<input type='hidden' name='mode' value='<%=mode%>'>
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go();'><img src='../images/bt_save.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script language="Javascript">

function go()
{
	var f = document.forms[0];
	var name  = f.name.value;

	if(name == ""){
		alert("업체명1을 입력하십시오.");
		f.name.focus();
		return;
	}

	f.submit();
}
</script>
