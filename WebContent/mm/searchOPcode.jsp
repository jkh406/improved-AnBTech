<%@ include file="../admin/configPopUp.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" 
	errorPage	= "../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//넘겨줄 파라미터 처리하기
	String target = request.getParameter("target");

	//2개로 분해하기
	String target_id = "";
	String target_name = "";
	int sh = target.indexOf("/");
	if(sh != -1) {
		target_id = target.substring(0,sh);
		target_name = target.substring(sh+1,target.length());
	} else {
		target_id = target;
		target_name = target;
	}

	//공정코드 읽기
	String sql = "";
	bean.openConnection();

	sql = "SELECT count(*) FROM mbom_env where flag='1'";
	bean.executeQuery(sql);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] opcode = new String[cnt][2];

	sql = "SELECT * FROM mbom_env where flag='1' order by m_code asc";
	bean.executeQuery(sql);
	int n=0;
	while(bean.next()) {
		opcode[n][0] = bean.getData("m_code");
		opcode[n][1] = bean.getData("spec");
		n++;
	}

%>

<HTML><HEAD><TITLE>공정검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name='sForm' method='post' style="margin:0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="./images/pop_op_search.gif" alt='공정검색'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="94%" border=0>
	   <tbody>
         <tr><td height="12"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1></td></tr>
         <tr>
           <td width="50%" style="padding-left:4px" background="./images/bg-011.gif">
				<table border="0" cellpadding="0" cellspacing="0" width="100%">
				  <tr><td width="100%" height="250" valign='top'>
						<select MULTIPLE size='17' name='opcode' onChange='javascript:goOpcode()' style=font-size:9pt;color='black';>
						<OPTGROUP label='----------------------'>
						<%
						for(int i=0; i<cnt; i++) {
							out.print("<option  value='"+opcode[i][0]+"|"+opcode[i][1]+"'>");
							out.println(opcode[i][0]+" "+opcode[i][1]+"</option>");
						}
						%>
						</select>
					</td></tr>
				</table>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height="13" colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="./images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>
<input type='hidden' name='target_id' value='<%=target_id%>'>
<input type='hidden' name='target_name' value='<%=target_name%>'>
</form>
</BODY>
</HTML>
<script language='javascript'>
<!--
//OP코드 선택하기
function goOpcode()
{
	var opcode = document.sForm.opcode.value;
	var op = opcode.split('|');

	var target_id = document.sForm.target_id.value; 
	var target_name = document.sForm.target_name.value; 
	eval("opener.document." + target_id).value = op[0]; 
	eval("opener.document." + target_name).value = op[1]; 
	self.close();
}
//-->
</script>