<%@ include file="../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>

<%!
	PartInfoTable part;
%>

<%	String mode = request.getParameter("mode");	
	//���õ� ǰ������ ��������
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String item_no		= part.getItemNo();
	String item_desc	= part.getItemDesc();
	String mfg_no		= part.getMfgNo();
	String item_name	= part.getItemName();
	String item_type	= part.getItemType();
	String stock_unit	= part.getStockUnit();

%>

<html>
<head>
<title></title>
<link rel="stylesheet" type="text/css" href="../cm/css/style.css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="sForm" method="post" action="CodeMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> ǰ����(ASSY��FG)</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../cm/images/bt_modify.gif'  onClick='modify_item()' style='cursor:hand' border='0' align='center'>
					<IMG src='../cm/images/bt_cancel.gif' onClick='javascript:history.go(-1);' style='cursor:hand' border='0' align='center'>					
			  </TD></TR></TBODY></TABLE>
	</TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ���ȣ</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3"><%=item_no%></TD></TR>
        <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ���</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3"><%=item_name%></TD></TR>
        <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�񼳸�</TD>
           <TD width="87%" height="25" class="bg_04" colspan="3"><INPUT type='text' name='item_desc' value='<%=item_desc%>' size='35'></TD></TR>
        <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�����</TD>
           <TD width="37%" height="25" class="bg_04"><INPUT type='text' name='item_type' value='<%=item_type%>' size='5'><a href="javascript:sel_item_type();" ><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
		<TR bgcolor="c7c7c7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">������</TD>
           <TD width="37%" height="25" class="bg_04"><INPUT type='text' name='stock_unit' value='<%=stock_unit%>' size='5'><a href="javascript:sel_stock_unit();"  ><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
		<TR bgcolor="c7c7c7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>
	
	<INPUT type='hidden' name='mode' value='<%=mode%>'>
	<INPUT type='hidden' name='item_no' value='<%=item_no%>'>
</FORM>
</BODY>
</HTML>

<script language='javascript'>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function modify_item(){
	var f = document.sForm;
	f.submit();
}

//ǰ�� ���� �ڵ� ��������
function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=sForm&type=ITEM_TYPE&div=one&code=item_type";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

// ��� ���� ��������
function sel_stock_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=sForm&type=STOCK_UNIT&div=one&code=stock_unit";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

</script>
