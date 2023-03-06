<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	
	String target		= request.getParameter("target");
	String gcode		= request.getParameter("gcode")==null?"":request.getParameter("gcode");
	String searchword	= Hanguel.toHanguel(request.getParameter("searchword"));

	String sql = "SELECT code,name FROM goods_structure WHERE gcode LIKE '" + gcode +"%' AND glevel = '4' ORDER BY name DESC";
	if(searchword != null)
		sql = "SELECT code,name FROM goods_structure WHERE gcode LIKE '" + gcode +"%' AND name LIKE '%" + searchword +"%' AND glevel = '4' ORDER BY name DESC";

	bean.openConnection();	
	bean.executeQuery(sql);
%>
<HTML><HEAD><TITLE>모델코드검색</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../gm/images/pop_model_s.gif" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>


	<table border="0" cellpadding="5" cellspacing="0" width="100%">
	  <TR><TD height='5'></TD></TR>
      <TR bgcolor="c7c7c7"><TD height='1'></TD></TR>
	  <tr><TD width="100%" style="padding-left:10px" background="images/bg-011.gif" align='middle'><form name=frmSELECT style='margin:0'>
			<select name=selector size=9 onClick="check();" onChange="check();" style="WIDTH: 230px">
	<%	while(bean.next()){	%>
				<option value='<%=bean.getData("code")%>'><%=bean.getData("name")%></option>
	<%	}	%>
			</select></form>
		</td></tr>
	  <TR bgcolor="C7C7C7"><TD height="1"></TD></TR>
	  <tr><TD width="100%" style="padding-left:10px" background="images/bg-011.gif" align='middle'><form name=frmMAIN2 action="SearchModel.jsp" method="post" style='margin:0'><table border="0" cellpadding="0" cellspacing="0"><tr><td>한글모델명&nbsp;</td><td><input type=text name=searchword size=15 maxlength=30  onKeyUp="javascript:searchCode(event);"> <a href='javascript:document.frmMAIN2.submit();'><img src='images/bt_search.gif' border='0' align='absmiddle'></a></td></tr></table><input type='hidden' name='target' value='<%=target%>'><input type='hidden' name='gcode' value='<%=gcode%>'></form></td></tr>
	  <TR bgcolor="C7C7C7"><TD height="1"></TD></TR>
	</table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		   <TR>
            <TD width="100%" height=5 colSpan=4></TD>
          </TR>
		  <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../gm/images/bt_close.gif" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY></HTML>


<script language=javascript>
function check()
{
		document.frmMAIN2.searchword.value = document.frmSELECT.selector.options[document.frmSELECT.selector.selectedIndex].text;
		if(opener.document.<%=target%>.model_code)
			opener.document.<%=target%>.model_code.value = document.frmSELECT.selector.options[document.frmSELECT.selector.selectedIndex].value;
		if(opener.document.<%=target%>.model_name)
			opener.document.<%=target%>.model_name.value = document.frmSELECT.selector.options[document.frmSELECT.selector.selectedIndex].text;
}

function ichar_submit(ichar)
{
	var sl = document.frmSELECT.selector;

	for (var i = 0; i < sl.options.length; i++)
	{

		if (ichar <= sl.options[i].text)
		{
			sl.selectedIndex = i;	
			break;
		}
	}
	if (sl.selectedIndex < 0)
		sl.selectedIndex = 0;
}

function searchCode(evt)
{
	if (evt.keyCode == 13)
	{
		window.opener.document.<%=target%>.value = 
		document.frmSELECT.selector.options[document.frmSELECT.selector.selectedIndex].value;
		return;
	}

	var sl = document.frmSELECT.selector;
	var tx = document.frmMAIN2.searchword.value;

	for (var i=0; i<sl.options.length; i++)
	{
		if (tx <= sl.options[i].text)
		{
			sl.selectedIndex = i;	
			break;
		}
	}
	if (sl.selectedIndex < 0)
		sl.selectedIndex = 0;
}
</script>