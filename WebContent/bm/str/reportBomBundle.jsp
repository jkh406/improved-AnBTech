<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "������ Report���"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.0");		//�������

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String model_code = (String)request.getAttribute("model_code");
	String fg_code = (String)request.getAttribute("fg_code");
	
	//-----------------------------------
	//	������ PART ����Ʈ
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><title>����Ʈ</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();' oncontextmenu="return false">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../bm/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">BOM ����</TD>
	<TD width='30%' align="right" valign="bottom">
	<DIV id="print" style="position:relative;visibility:visible;">
			<!--<a href='Javascript:winprint();'><img src='../bm/images/bt_print.gif' align='absmiddle' border='0'></a> --><!-- ��� -->
			<a href='Javascript:self.close();'><img src='../bm/images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
	</DIV></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=32>
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width="25%" height="25" class="bg_03">���ڵ� : <%=model_code%></td>
			    <TD align=left width="65%" height="25" class="bg_03">FG�ڵ� : <%=fg_code%></td>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:auto;">
		<TABLE cellSpacing=0 cellPadding=0 width="98%" bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle class='list_title'>LEVEL</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=350 align=middle class='list_title'>ǰ��԰�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=300 align=middle class='list_title'>Location No</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<%  
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=1; i<lv; i++) space += "&nbsp;";
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getParentCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getChildCode()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getPartSpec()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getLocation()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getQtyUnit()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getQty()%></TD>
			</TR>
			<TR><TD colSpan=19 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

</BODY>
</HTML>

<SCRIPT language='javascript'>
// ������������ ���������� �и��� �ʵ��� ó��
function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 273 ;
	
	item_list.style.height = div_h;

}
-->
</SCRIPT>

