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
	CodeLinkUrl link;
%>

<%
	//���õ� ǰ������ ��������
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String item_no		= part.getItemNo();
	String item_desc	= part.getItemDesc();
	String mfg_no		= part.getMfgNo();
	String item_name	= part.getItemName();
	String item_type	= part.getItemType();
	String stock_unit	= part.getStockUnit();

	//��ũ���ڿ� ��������
	link = new CodeLinkUrl();
	link = (CodeLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String link_modify = link.getLinkModify();
	String link_delete = link.getLinkDelete();
%>

<html>
<head>
<script language=JavaScript>
<!--
var select_obj;
function ANB_layerAction(obj,status) { 

	var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
	if(_marginx < 0)
		_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
	else
		_tmpx = event.clientX + document.body.scrollLeft ;
	if(_marginy < 0)
		_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
	else
		_tmpy = event.clientY + document.body.scrollTop ;

	obj.style.posLeft=_tmpx-13;
	obj.style.posTop=_tmpy+20;

	if(status=='visible') {
		if(select_obj) {
			select_obj.style.visibility='hidden';
			select_obj=null;
		}
		select_obj=obj;
	}else{
		select_obj=null;
	}
	obj.style.visibility=status; 
}
//-->
</script>
<title></title>
<link rel="stylesheet" type="text/css" href="../cm/css/style.css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> ǰ�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../cm/images/bt_list.gif' onClick="javascript:location.href='../servlet/<%=link_list%>'" style='cursor:hand' border='0' align='center'>
<%	// ���� üũ
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("CM01");
	if (idx >= 0){
%>
<script language='javascript'>
function delete_item(){
	if(confirm("������ ���� �Ͻðڽ��ϱ�?")) location.href = '../servlet/<%=link_delete%>';
}
</script>
					<IMG src='../cm/images/bt_modify.gif'  onClick="javascript:location.href='../servlet/<%=link_modify%>'" style='cursor:hand' border='0' align='center'>
					<IMG src='../cm/images/bt_del.gif'  onClick="javascript:delete_item()" style='cursor:hand' border='0' align='center'>
<%	}	%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

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
           <TD width="87%" height="25" class="bg_04" colspan="3"><%=item_desc%></TD></TR>
        <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�����</TD>
           <TD width="37%" height="25" class="bg_04"><%=item_type%></TD></TR>
		<TR bgcolor="c7c7c7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">������</TD>
           <TD width="37%" height="25" class="bg_04"><%=stock_unit%></TD></TR>
		<TR bgcolor="c7c7c7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>
	</TBODY></TABLE>
</BODY>
</HTML>