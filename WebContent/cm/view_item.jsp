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
	SpecCodeTable spec;
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

	//���帮��Ʈ ��������
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SPEC_LIST");
	spec = new SpecCodeTable();
	Iterator spec_iter = spec_list.iterator();

	// ÷������ ��������
	String downfile = "";
	downfile = (String)request.getAttribute("DOWN_FILE");

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

<body topmargin="0" leftmargin="0">
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
					<IMG src='../cm/images/bt_list.gif' onClick="javascript:location.href='../servlet/<%=link_list%>'"  style='cursor:hand' border='0' align='center'>
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
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ���ȣ</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=item_no%></td></tr>
        <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ���</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=item_name%></td></tr>
        <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�񼳸�</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=item_desc%></td></tr>
        <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�����</td>
           <td width="37%" height="25" class="bg_04"><%=item_type%></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><%=stock_unit%></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<%=downfile%>
	    </td></tr>
	    <tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
	</tbody></table>

<!-- ǰ��������� -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../cm/images/cm_spec_d.gif' border='0' alt='ǰ��԰�'></TD></TR></TABLE>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>�ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>�׸��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�����Ͱ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
	while(spec_iter.hasNext()){
		spec = (SpecCodeTable)spec_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=spec.getSpecCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecValue()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'></td>
			</TR>
			<TR><TD colSpan=9 background="../cm/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE>

</body>
</html>

