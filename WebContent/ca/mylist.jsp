<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable table;
	CaLinkUrl redirect;
%>
<%
	
	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("CA_List");
	table = new CaMasterTable();
	Iterator table_iter = table_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new CaLinkUrl();
	redirect = (CaLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ca/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ca/images/blet.gif" align="absmiddle"> ��������</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../ca/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../ca/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../ca/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<a href="javascript:chkval();"><img src="../ca/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="javascript:modify();"><img src="../ca/images/bt_modify.gif" border="0" align="absmiddle"></a>
				<a href="javascript:drop();"><img src="../ca/images/bt_del.gif" border="0" align="absmiddle"></a></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top><form name='listForm' style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'><input type=checkbox name=checkbox onClick="check(document.listForm.checkbox)"></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
<!--			  <TD noWrap width=80 align=middle class='list_title'>�ӽý��ι�ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>-->
			  <TD noWrap width=150 align=middle class='list_title'>���ξ�ü��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>��ü��ǰ��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ca/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�Ƿ�����</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	while(table_iter.hasNext()){
		table = (CaMasterTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=table.getMid()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getItemNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
<!--			  <TD align=middle class='list_bg'><%=table.getApprovalNo()%></td>
			  <TD><IMG height=1 width=1></TD>-->
			  <TD align=middle class='list_bg'><%=table.getMakerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getMakerPartNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getRequestDate()%></td>
			</TR>
			<TR><TD colSpan=11 background="../ca/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></FORM>
</BODY>
</HTML>


<script language='javascript'>

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
}

function chkval() {
	var f = document.listForm.checkbox;
	var s_count = 0;
	var mids = "";
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			mids += f[i].value+"|";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("����� ������ �����Ͻʽÿ�.");
	   return;
    }
	//alert(mids);
	location.href = "../servlet/ComponentApprovalServlet?mode=eapproval&no="+mids;
}

function modify() {
	var f = document.listForm.checkbox;
	var s_count = 0;
	var mids = "";
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			mids = f[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("������ ������ ���� �����Ͻʽÿ�.");
	   return;
    }

    if(s_count > 1){
	   alert("�ѹ��� �Ѱ��� ���Ǹ�  ���� �����մϴ�.");
	   return;
    }

	var mod_confirm = confirm("������ ������ �����Ͻðڽ��ϱ�?");

	if(mod_confirm == true){
		location.href = "../servlet/ComponentApprovalServlet?mode=modify&no="+mids;
	}else{
		for(i=1;i<f.length;i++){
				f[i].checked = false;
		}
	}
}

function drop() {
	var f = document.listForm.checkbox;
	var s_count = 0;
	var mids = "";
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			mids += f[i].value + "|";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("������ ������ ���� �����Ͻʽÿ�.");
	   return;
    }

	var del_confirm = confirm("������ ������ �����Ͻðڽ��ϱ�?");

	if(del_confirm == true){
		location.href = "../servlet/ComponentApprovalServlet?mode=drop&no="+mids;
	}else{
		for(i=1;i<f.length;i++){
				f[i].checked = false;
		}
	}
}
</script>