<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "�系���� LIST"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.dms.entity.OfficialDocumentTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//-----------------------------------
	//	�������� ���� & ��ü ���� �ľ��ϱ�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	String search_item = Hanguel.toHanguel(request.getParameter("sItem")); 
	if(search_item == null) search_item = "subject";
	String search_word = Hanguel.toHanguel(request.getParameter("sWord"));
	if(search_word == null) search_word = "";

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//������ �̵��ϱ�
function goPage(a) 
{
	document.sForm.action='../servlet/InDocumentServlet';
	document.sForm.page.value=a;
	document.sForm.submit();
}

//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/InDocumentServlet';
	document.sForm.page.value='1';
	document.sForm.submit();
}

//����ϱ�
function write()
{
	document.sForm.action='../ods/InDocument_write.jsp';
	document.sForm.submit();
}

//����ϱ�
function app()
{
	//sParam = "strSrc=InDocument_App.jsp&id=";
	sParam = "../gw/approval/module/InDocument_App.jsp?id=";
	var f = document.aForm.checkbox;
	if(f == undefined) { alert("����� ������ ������ ��, �����ϼ���."); return; }
	var s_count = 0;
    for(i=0;i<f.length;i++){
		if(f[i].checked){
			sParam += f[i].value;
			s_count ++;
		}
    }
	var t = document.aForm.checkbox.checked;
	if(t == true) { sParam += document.aForm.checkbox.value; s_count = 1; }
	
    if(s_count == 0){
	   alert("����� ������ ������ ��, �����ϼ���.");
	   return;
    } else if(s_count > 1) {
		alert("�ϳ��� ������ ��, �����ϼ���.");
	   return;
	}
	
	/*
	var rval = showModalDialog("../ods/DocModalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");
	if(rval == "RL") {
		document.sForm.action='../servlet/InDocumentServlet';
		document.sForm.mode.value='IND_L';
		document.sForm.submit();
	}
	*/
	
	document.sForm.action=sParam;
	document.sForm.submit();
}

//�������� ���� (���������)
function contentReview(id)
{
	//sParam = "strSrc=InDocumentServlet&mode=IND_V&id="+id;
	
	//var rval = showModalDialog("../ods/DocModalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	wopen('InDocumentServlet?mode=IND_V&id='+id,'view_doc','680','650','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//�������� ���� (���������)
function contentAppview(id,app_id)
{
	//sParam = "strSrc=InDocumentServlet&mode=IND_A&id="+id+"&doc_id="+app_id;
	
	//var rval = showModalDialog("../ods/DocModalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	wopen('InDocumentServlet?mode=IND_A&id='+id+'&doc_id='+app_id,'view_doc','680','650','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//��������ϱ�
function contentModify(id)
{
	document.sForm.action='../servlet/InDocumentServlet';
	document.sForm.mode.value='IND_M';
	document.sForm.id.value=id;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete(id)
{
	if(confirm("���� �Ͻðڽ��ϴ�.")){
	document.dForm.action='../servlet/InDocumentServlet';
	document.dForm.mode.value="IND_D";
	document.dForm.id.value=id;
	document.dForm.submit();
	} else {
		return;
	}
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>

<LINK href="../ods/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif" align="absmiddle"> �系����</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../ods/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style="font-size:9pt">  
					<%
						String[] sitems = {"subject","user_name","in_date"};
						String[] snames = {"����","�ۼ���","�ۼ���"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(search_item.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select> <input type="text" name="sWord" size="10" value="<%=search_word%>"> <a href='Javascript:goSearch();'><img src='../ods/images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:write()'><img src="../ods/images/bt_add_new2.gif" border='0' align='absmiddle'></a> <a href='javascript:app()'><img src="../ods/images/bt_sangsin.gif" border='0' align='absmiddle'></a>
					<input type="hidden" name="mode" size="15" value="IND_L">
					<input type="hidden" name="id" size="15" value="">
					<input type="hidden" name="page" size="15" value=""></form></TD>
			  <TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../ods/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../ods/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../ods/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../ods/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top><form name="aForm" method="post" style="margin:0">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>������ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>�ۼ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�ۼ�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ods/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>

	<% 
		while(table_iter.hasNext()) {
			table = (OfficialDocumentTable)table_iter.next();
			String doc_id = "";	//������ȣ

			String flag = table.getFlag();		//Flag
			if(flag == null) doc_id = "�̻��";
			else if(flag.equals("EE")) doc_id = "������";
			else if(flag.equals("EN")) doc_id = "�ݷ�";
			else if(flag.equals("EC")) doc_id = "������";
			else doc_id = table.getDocId();

			String user_id = table.getUserId();		//�ۼ��� ���
			String rdate = table.getInDate();		//�ۼ�����
			rdate = rdate.substring(0,10);
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'>
<% if (login_id.equals(user_id) && ((flag == null) || flag.equals("EN") || flag.equals("EC")))	{//����,�̻��,�ݷ�,������ ������ %>
				<input type="checkbox" name="checkbox" value='<%=table.getId()%>'>
    <% } %>
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=doc_id%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=table.getSubject()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getUserName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rdate%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getModify()%> <%=table.getDelete()%></TD></TR>
			<TR><TD colSpan=11 background="../ods/images/dot_line.gif"></TD></TR>
	<% 
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>

<form name="dForm" method="post" style="margin:0">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="id" value=''>
</form>

</body>
</html>

