<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		="java.util.*,com.anbtech.bs.entity.*"
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

	//��� ��������
	ItemUnitCostTable table = new ItemUnitCostTable();
	ArrayList list = new ArrayList();
	list = (ArrayList)request.getAttribute("ITEM_UNIT_COST_LIST");
	Iterator table_iter = list.iterator();

	//��ũ ���ڿ� ��������
	SalesConfigLinkUrl redirect = new SalesConfigLinkUrl();
	redirect = (SalesConfigLinkUrl)request.getAttribute("REDIRECT");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../bs/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="display();">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../bs/images/blet.gif" align="absmiddle"> ����ǰ��ܰ�����</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../bs/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../bs/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../bs/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='SalesConfigMgrServlet' name='srForm' onSubmit="if(this.searchword.value.length< 2){alert('�˻���� 2���̻� �Է��ϼž� �մϴ�.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name='searchscope'>
						  <option value='item_code'>ǰ���ڵ�</option>
						  <option value='item_name'>ǰ���</option>
						  <option value='customer_name'>����</option>
					  </select> 
					  <input type='text' name='searchword' size='10'>
					  <input type="image" onfocus=blur() src="../bs/images/bt_search3.gif" border="0" align="absmiddle">
					  <a href="SalesConfigMgrServlet?mode=write_item_unit_cost_by_customer"><img src="../bs/images/bt_reg.gif" border="0" align="absmiddle"></a>
					<%=input_hidden_search%></form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top><DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:218; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ŷ�ó�ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�ŷ�ó��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>ǰ���ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=260 align=middle class='list_title'>ǰ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�Ǹ�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�ǸŴ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>ȭ������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�ǸŴܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../bs/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
<%
	int total_i = Integer.parseInt(view_total);
	int boardpage = Integer.parseInt(view_boardpage);
	int no = total_i - ((boardpage-1)*10);
	while(table_iter.hasNext()) {
		table = (ItemUnitCostTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=table.getCustomerCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px;'><%=table.getCustomerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px;'><%=table.getItemName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getSaleType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getApprovalType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getApplyDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg'><%=table.getSaleUnit()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg'><%=table.getMoneyType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><font color='red'><%=sp.getMoneyFormat(table.getSaleUnitCost(),"")%></font></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=21 background="../bs/images/dot_line.gif"></TD></TR>
<%  no--;
	}				
%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</body>
</html>

<script language='javascript'>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 350;
	item_list.style.height = div_h;

}
</script>