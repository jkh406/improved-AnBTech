<%@ include file="../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	PartInfoTable table;
	CodeLinkUrl redirect;
%>
<%
	
	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("LIST_ITEM");
	table = new PartInfoTable();
	Iterator table_iter = table_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new CodeLinkUrl();
	redirect = (CodeLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../cm/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" onLoad="document.srForm.searchword.focus();">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif" align="absmiddle"> ǰ��˻�</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../cm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../cm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../cm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='85'>
				   <form method=get action='../servlet/CodeMgrServlet' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('�˻���� 2���̻� �Է��ϼž� �մϴ�.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					<select name=searchscope onChange="selSearchScope();">
						  <option value='item_no'>ǰ���ڵ�</option>
						  <option value='item_desc'>ǰ�񼳸�</option>
						  <option value='register_info'>�����</option>
						  <option value='register_date'>�������</option>
					</select>
			</TD>
				<TD align=left width='500'>
					<div id="register_date" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						    <input type="text" name="s_date" size="8" maxlength="8"> ~ <input type="text" name="e_date" size="8" maxlength="8"> <a href="javascript:checkForm();"><img src="../mr/images/bt_search3.gif" border="0" align="absmiddle"></a> 
							<a href="javascript:search_detail();"><img src="../cm/images/bt_search_d.gif" border="0" align="absmiddle"></a>(��:20040210~20040225)
							</td></tr></table></div>
					<div id="register_etc" class="expanded" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''">
						   <input type="image" onfocus=blur() src="../mr/images/bt_search3.gif" border="0" align="absmiddle"><a href="javascript:search_detail();"><img src="../cm/images/bt_search_d.gif" border="0" align="absmiddle"></a>
						   </td></tr></table></div>
					  <%=input_hidden_search%>
					</form>
				 </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>ǰ�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=300 align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=180 align=middle class='list_title'>�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		    </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (PartInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegisterInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRegisterDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=13 background="../cm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>


<script language='javascript'>
<!--
function search_detail() {
	var url = "CodeMgrServlet?mode=search_by_spec";
	wopen(url,'search_detail','800','500','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

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
    
	if(f.searchscope.value == 'register_date'){
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
    
	if(f.searchscope.value == 'register_date'){
		show('register_date');
		hide('register_etc');
		//hide('field');
	
	}else{
		hide('register_date');
		show('register_etc');
		//hide('field');
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

/* �Է°��� Ư�� ����(chars)������ �Ǿ��ִ��� üũ */
function containsCharsOnly(input,chars) {
	for (var inx = 0; inx < input.value.length; inx++) {
	   if (chars.indexOf(input.value.charAt(inx)) == -1)
		   return false;
	}
	return true;
}
		
/* �Է°��� ���ڸ� �ִ��� üũ */
function isNumber(input) {
	var chars = "0123456789";
	return containsCharsOnly(input,chars);
}
-->
</script>