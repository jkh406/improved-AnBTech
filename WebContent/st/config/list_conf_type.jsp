<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%
	StockConfInfoTable stconfTable = new StockConfInfoTable();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("LIST_CONF");
	Iterator type_iter = type_list.iterator();

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../st/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad='display();'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif" align="absmiddle"> ����������Ȳ</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'><a href="StockConfigMgrServlet?mode=write_conf_type"><img src="../st/images/bt_add.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:278; overflow-x:no; overflow-y:scroll;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>��������<br>�ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���<br>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���ܰ�<br>�ݿ�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>ȸ��posting<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>â��<br>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>���尣<br>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=65 align=middle class='list_title'>ǰ��<br>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=65 align=middle class='list_title'>������<br>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=65 align=middle class='list_title'>����</TD>
			  
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
<%
	while(type_iter.hasNext()) {
		stconfTable = (StockConfInfoTable)type_iter.next();
		String stock_rise_fall = stconfTable.getStockRiseFall();

		if(stock_rise_fall.equals("1")){
			stock_rise_fall = "����";
		} else if (stock_rise_fall.equals("2")){
			stock_rise_fall = "����";
		} else if (stock_rise_fall.equals("3")){
			stock_rise_fall = "����";
		} 
		
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getTradeTypeCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getTradeTypeName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stock_rise_fall%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsCostApply()%></td>
			  <TD><IMG height=1 width=1></TD>
		      <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsCountPosting()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsWharehouseMove()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsFactoryMove()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsItemMove()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=stconfTable.getIsNoMove()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><A HREF="javascript:modify('<%=stconfTable.getMid()%>')"><IMG src='../st/images/lt_modify.gif' align='absmiddle' border='0' alt='����'></a>&nbsp;<!--<A HREF="javascript:del('<%=stconfTable.getMid()%>')"><IMG src='../st/images/lt_del.gif' align='absmiddle' border='0' alt='����'>--></td>			  
			</TR>
			<TR><TD colSpan=21 background="../st/images/dot_line.gif"></TD></TR>
<%
	}
%>			
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>
function add(){
	location.href="../servlet/StockConfigMgrServlet?mode=write_conf_type";
}

function modify(mid){
	location.href="../servlet/StockConfigMgrServlet?mode=modify_conf_type&mid="+mid;
}

function del(mid){
	if(confirm("���� �����Ͻðڽ��ϱ�?")) {
	location.href="../servlet/StockConfigMgrServlet?mode=delete_conf_type&mid="+mid;
	}
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//	var div_h = h - 430;
	var div_h = c_h - 62;
	item_list.style.height = div_h;

} 
</script>