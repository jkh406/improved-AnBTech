<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.date.anbDate"%>

<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String hrs = anbdt.getDateNoformat();	
	
	int year = Integer.parseInt(hrs.substring(0,4));
	int month = Integer.parseInt(hrs.substring(4,6));

	String div = "";

%>
<HTML><HEAD><TITLE>자산감가처리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="mForm">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" width='100%' valign="middle" bgcolor="#73AEEF"><IMG src='../images/popup_dc.gif' border='0' align='absmiddle'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=0 width="90%" border=0>
	   <tbody>
        <tr><td height=20 colspan="2"></td></tr>
        <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
        <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">기준년</td>
           <td width="60%" height="25" class="bg_02">
				<SELECT name='year'>
<%					for( int i=-5 ; i<5 ; i++ ) {
						if(year==(year+i)) { div = "selected";} else { div=""; }
%>							<OPTION value=<%=year+i%> <%=div%>><%=year+i%></OPTION>
<%					}
%>
				</SELECT></td></tr>

        <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
        <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">기준월</td>
           <td width="60%" height="25" class="bg_02">
				<SELECT name='month'>
<%					for( int i=1 ; i<=12 ; i++ ) {
						if(month==i) { div = "selected";} else { div=""; }
%>							<OPTION value=<%=i%> <%=div%>><%=i%></OPTION>
<%					}
%>				</SELECT></td></tr>

        <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
        <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">최소자산가치</td>
           <td width="60%" height="25" class="bg_02"><input type=text size=10 name='min_value' value='1000'>원</td></tr>
        <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		<tr>
         
		</tbody></table>
		<tr><td height=20 colspan="2"></td></tr>


	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<a href='javascript:go_dc_process()'><IMG src='../images/bt_confirm.gif'  align='absmiddle' border='0'></a>
			<a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></form></td></tr></table></BODY></HTML>

<SCRIPT language="javascript">
var f = document.mForm

function go_dc_process() {

	var year  = f.year.value;
	var month = f.month.value;
	var value = f.min_value.value;
	var str = "mode=asset_list&assetupdate=update&year="+year+"&month="+month+"&value="+value;
   
	opener.location.href = "../../servlet/AssetServlet?" + str;
	self.close();
}
</SCRIPT>