<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%!
	OrderInfoTable table;
%>
<%
	//����Ʈ ��������
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();

	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ORDER_LIST");
	Iterator table_iter = table_list.iterator();

	com.anbtech.pu.entity.PurchaseLinkUrl redirect = new com.anbtech.pu.entity.PurchaseLinkUrl();
	redirect = (PurchaseLinkUrl)request.getAttribute("REDIRECT");

	String view_pagecut = redirect.getViewPagecut();
//	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String input_hidden = redirect.getInputHidden();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../pu/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif" align="absmiddle"> ������Ȳ</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../pu/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../pu/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../pu/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
				<TR>
				<TD align=left width='80' style="padding-left:5px">
				   <form method="get" action="../servlet/PurchaseMgrServlet" name="srForm" onSubmit="if(this.searchword.value.length< 2){alert('�˻���� 2���̻� �Է��ϼž� �մϴ�.');this.searchword.value='';this.searchword.focus();return false;}else{checkForm();}" style="margin:0">
					  <select name="searchscope" onChange="selSearchScope();">
						<option value='order_no'>���ֹ�ȣ</option>
						<option value='supplyer_name'>�ŷ�ó��</option>
						<option value='order_date'>��������</option>
					  </select>&nbsp;</TD>
				<TD align=left width='500'>
					  <div id="sword" class="expanded" style="position:relative;"><table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						  <input type="text" name="searchword" size="10"> <input type="image" onfocus=blur() src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></td></tr></table></div>
					  <div id="order_date" class="collapsed" style="position:relative;"><table cellSpacing=0 cellPadding=0 width="500" border='0'><tr><td>
						  <input type="text" name="s_date" size="8" maxlength="8"> ~ <input type="text" name="e_date" size="8" maxlength="8"> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a> (��:20040210~20040225)</td></tr></table></div></TD><%=input_hidden%><input type='hidden' name='mode' value='order_search'></form>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>���ֹ�ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>���޾�ü��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ѹ��ֱݾ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int no = Integer.parseInt(view_total) - ((Integer.parseInt(view_boardpage)-1)*15 );
	while(table_iter.hasNext()){
		table = (OrderInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getOrderNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getOrderDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(table.getOrderTotalMount(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=13 background="../pu/images/dot_line.gif"></TD></TR>
<%
		no--;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
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

//�˻� üũ
function checkForm(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'order_date'){
		f.searchword.value = f.s_date.value + f.e_date.value;

		if(f.searchword.value.length != 16){
			alert("�˻����ڸ� �ùٸ��� �Է��Ͻʽÿ�.");
			return;
		}
	}else{
		if(f.searchword.value.length < 2){
			alert("�˻�� 2�� �̻� �Է��Ͻʽÿ�.");
			f.searchword.focus();
			return;
		}	
	}

	f.submit();
}

//�˻��ʵ� ���� ó��
function selSearchScope(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'order_date'){
		show('order_date');
		hide('sword');
	}else{
		hide('order_date');
		show('sword');
	}
}

// ���õ� ���̾ ����
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// ���õ� �����̸� ������
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}
</script>