<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	StShiftInfoTable table;
	StockLinkUrl redirect;
%>
<%
	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("SHIFT_INFO_LIST");
	Iterator table_iter = table_list.iterator();

	redirect = new com.anbtech.st.entity.StockLinkUrl();
	redirect = (StockLinkUrl)request.getAttribute("REDIRECT");

	String view_pagecut = redirect.getViewPagecut();
//	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	out.print(input_hidden_search);
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String input_hidden = redirect.getInputHidden();
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../st/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../st/images/blet.gif"> ����̵���Ȳ</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><IMG src="../st/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <IMG src="../st/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <IMG src="../st/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing="0" cellPadding="0" width="100%">
			<TBODY>
				<TR><TD align='left' width='70' style="padding-left:5px">
					<form method="get" action="../servlet/StockMgrServlet" name="srForm" style="margin:0">
						<select name="searchscope" onChange="selSearchScope();">
							<option value='shift_no'>�̵���ȣ</option>
							<option value='sr_item_code'>ǰ���ȣ</option>
							<option value='reg_date'>�̵�����</option>
							<option value='shift_type'>�̵�����</option>
						</SELECT>&nbsp;</TD>
					<TD align=left width='500'>
					<div id="reg_date" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						    <input type="text" name="s_date" size="8" maxlength="8"> ~ <input type="text" name="e_date" size="8" maxlength="8"> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a>(��:20040210~20040225)</td></tr></table></div>
					<div id="sht_type" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						    <select name='shift_type'>
								<option value='I'>ǰ��</option>
								<option value='F'>���尣</option></select> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a></td></tr></table></div>
					<div id="sword" class="expanded" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''">
						   <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a></td></tr></table></div>
					  <input type='hidden' name='mode' value='list_item_shift_info'>
					</form></TD>
					<TD width='200' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	<TBODY>
		<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
		<TR vAlign=middle height=23>
			  <TD noWrap width=120 align=middle class='list_title'>�̵���ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�̵�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�̵���ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�̵��İ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�̵���ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�̵�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>�̵�����</TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (StShiftInfoTable)table_iter.next();
		String shift_type_name = table.getShiftType();
		
		if (shift_type_name.equals("I")){
			shift_type_name = "ǰ��";
		} else if (shift_type_name.equals("F")){
			shift_type_name = "���尣";
		}
%>		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><A href="javascript:go_info('<%=table.getShiftType()%>','<%=table.getMid()%>')"><%=table.getShiftNo()%></A></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=shift_type_name%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getSrFactoryCode()%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getSrItemCode()%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getDtFactoryCode()%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getDtItemCode()%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getQuantity()%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle class='list_bg'><%=table.getRegDate()%></TD>
		</TR>
		<TR><TD colSpan=21 background="../st/images/dot_line.gif"></TD></TR>
<%		no++;
	}
%>
		</TBODY></TABLE>
</BODY>
</HTML>

<SCRIPT language=javascript>
	
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

	if(f.searchscope.value == 'reg_date'){
		f.searchword.value = f.s_date.value + f.e_date.value;

		if(f.searchword.value.length != 16){
			alert("�˻����ڸ� �ùٸ��� �Է��Ͻʽÿ�.");
			return;
		}

	}if(f.searchscope.value == 'shift_type'){
		f.searchword.value = f.shift_type.value
	}else{
		if(f.searchword.value.length < 2){
			alert("�˻�� 2�� �̻� �Է��Ͻʽÿ�.");
			f.searchword.focus();
			return;
		}	
	}
	f.submit();
}

// ����̵�Form���� �̵�
function go_info(shift_type,mid){
	if(shift_type=='I') {	// ǰ�� �̵�
		location.href="../servlet/StockMgrServlet?mode=view_item_ishift_info&mid="+mid;
	} else if(shift_type=='F') {	// ���尣 �̵�
		location.href="../servlet/StockMgrServlet?mode=view_item_shift_info&mid="+mid;
	}
}

//�˻��ʵ� ���� ó��
function selSearchScope(){
	var f = document.srForm;
    
	if(f.searchscope.value == 'reg_date'){
		show('reg_date');
		hide('sword');
		hide('sht_type');
	}else if(f.searchscope.value == 'shift_type'){
		show('sht_type');
		hide('sword');
		hide('reg_date');
	}else{
		hide('reg_date');
		hide('sht_type');
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