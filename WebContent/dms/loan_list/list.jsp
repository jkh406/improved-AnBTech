<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	LoanTable table;
	LinkUrl redirect;
%>
<%
	//loan_list ����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Loan_List");
	table = new LoanTable();
	Iterator table_iter = table_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("Redirect");
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String where_category = redirect.getWhereCategory();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../dms/css/style.css" rel=stylesheet>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif" align="absmiddle"> �����û��Ȳ</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../dms/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../dms/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../dms/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='AnBDMS' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('�˻���� 2���̻� �Է��ϼž� �մϴ�.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name=searchscope>
						  <option value='loan_no' class=kissofgod-submit>�����ȣ</option>
						  <option value='doc_no' class=kissofgod-submit>������ȣ</option>
						  <option value='requestor_s' class=kissofgod-submit>������</option>
					  </select>
					  <input type=text name=searchword size='10'>
					  <input type="image" onfocus=blur() src="../dms/images/bt_search3.gif" border="0" align="absmiddle">
					  <%=input_hidden_search%></form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=80 align=middle class='list_title'>�����ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>������ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�����û��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>ó���Ϸ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../dms/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�����ݳ���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	//******************************
	//������ table������ �������� �� loop  **
	//******************************
	while(table_iter.hasNext()){
	table = (LoanTable)table_iter.next();
	String loan_no = table.getLoanNo();
	String doc_no = table.getDocNo();
	String requestor = table.getRequestor();
	String req_date = table.getReqDate();
	String return_date = table.getReturnDate();
	String stat = table.getStat();
	String loanend_date = table.getLoanEndDate();
	if(loanend_date == null) loanend_date = "";

%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=loan_no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=doc_no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=requestor%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=req_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=return_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=stat%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=loanend_date%></td>
			</TR>
			<TR><TD colSpan=13 background="../dms/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</body>
</html>



<script language='javascript'>
<!--
//�󼼰˻�
function search_detail() {

	var sParam = "src=search.jsp&category_info=<%=where_category%>&frmWidth=600&frmHeight=500&title=search_detail";
	var category = document.srForm.category.value;
	var mode = document.srForm.mode.value;
	
	var sRtnValue=showModalDialog("../dms/techdoc_data/modalFrm.jsp?"+sParam,"search","dialogWidth:600px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&category="+category+"&searchscope=detail&searchword="+sRtnValue;
		location.href = "../servlet/AnBDMS?mode="+mode + sParam;
//		alert(sParam);
	}
}
//-->
</script>