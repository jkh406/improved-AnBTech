<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "��ǰ ������Ȳ LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.mm.entity.mfgProductMasterTable table;
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//����
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String msg="";
	String start_date = (String)request.getAttribute("start_date"); if(start_date == null) start_date = "";
	if(start_date.length() != 0) start_date = anbdt.getSepDate(start_date,"/");
	String end_date = (String)request.getAttribute("end_date"); if(end_date == null) end_date = "";
	if(end_date.length() != 0) end_date = anbdt.getSepDate(end_date,"/");
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
	if(factory_no.length() == 0) msg ="�ش�����ȣ�� �����Ͻʽÿ�.";

	//--------------------------------------
	//������ ��ũ ���ڿ� ��������
	//--------------------------------------
	String view_pagecut="";
	int view_total=0,view_boardpage=0,view_totalpage=0;

	com.anbtech.mm.entity.mfgProductMasterTable pageL = new com.anbtech.mm.entity.mfgProductMasterTable();
	pageL = (mfgProductMasterTable)request.getAttribute("PAGE_List");
	view_pagecut = pageL.getPageCut();
	view_total = pageL.getTotalArticle();
	view_boardpage = pageL.getCurrentPage();
	view_totalpage = pageL.getTotalPage();

	//--------------------------------------
	//����Ʈ ��������
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("PRODUCT_List");
	table = new mfgProductMasterTable();
	Iterator table_iter = table_list.iterator();
	
%>

<HTML><HEAD><TITLE>��ǰ ������Ȳ LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" onsubmit="javascript:goSearch(); return false;">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../mm/images/blet.gif"> ��ǰ ������Ȳ</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><img src="../mm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../mm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../mm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR>
					<TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../mm/images/bt_search3.gif' onClick='javascript:goSearch();' style='cursor:hand' align='absmiddle'>
					</TD>
					<TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR></TABLE>
<!-- �˻����� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">ǰ���ڵ�</TD>
			<TD width="37%" height="25" class="bg_04">
				
				<select name="sItem" style=font-size:9pt;color="black";>  
			<%
				String[] sitems = {"","model_name","fg_code","item_code"};
				String[] snames = {"��ü�˻�","�𵨸�","FG�ڵ�","���ǰ�ڵ�"};
				String sel = "";
				for(int si=0; si<sitems.length; si++) {
					if(sItem.equals(sitems[si])) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
				}
			%>
			</select>
				<INPUT type='text' size='20' name='sWord' value='<%=sWord%>'></TD>
			<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">����</TD>
			<TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='5' name='factory_no' value='<%=factory_no%>' readonly> 
				<INPUT type='text' size='20' name='factory_name' value='' readonly> 
				<a href="javascript:searchFactoryInfo();"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�˻�����</TD>
           <TD width="37%" height="25" class="bg_04">
				<INPUT type='text' size='10' maxlength='10' name='start_date' value='<%=start_date%>' readonly> <a href="Javascript:OpenCalendar('start_date')"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <INPUT type='text' size='10' maxlength='10' name='end_date' value='<%=end_date%>' readonly> <a href="Javascript:OpenCalendar('end_date')"><img src="../mm/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif"></TD>
           <TD width="37%" height="25" class="bg_04"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	   </TBODY></TABLE><br>

<!-- ������� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=90 align=middle class='list_title'>�� ��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�𵨸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>��ǰ�ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>��ǰ�԰�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�����ȹ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>������[%]</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (mfgProductMasterTable)table_iter.next();
		
%>
			<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle class='list_bg' height=23><%=table.getOutputDate()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getModelName()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'> <%=table.getItemCode()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getItemSpec()%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=nfm.toDigits(table.getOrderCount())%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=nfm.toDigits(table.getTotalCount())%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle class='list_bg'><%=table.getProductRate()%></TD>
				<TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colspan=17 background="../mm/images/dot_line.gif"></TD></TR>
<%		
	}
%>
		</TBODY></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="page" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//�޽��� ó��
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}
//�˻��ϱ�
function goSearch()
{
	var sd = document.sForm.start_date.value;	sd=sd.replace(/\//g,"");
	var ed = document.sForm.end_date.value;		ed=ed.replace(/\//g,"");
	if(sd > ed) { alert('�˻��������� �����Ϻ��� ���� �����Դϴ�.'); return; }

	document.sForm.action='../servlet/mfgViewServlet';
	document.sForm.mode.value='view_pd_glist';
	document.sForm.page.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../mm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�������� ã��
function searchFactoryInfo() {
	var f = document.sForm;
	var factory_no = f.factory_no.name;
	var factory_name = f.factory_name.name;
	
	var para = "field="+factory_no+"/"+factory_name;
	
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&"+para;
	wopen(url,'enterCode','400','228','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
