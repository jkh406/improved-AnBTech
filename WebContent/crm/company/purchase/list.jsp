<%@ include file= "../../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CompanyInfoTable company;
	CrmLinkUrl redirect;
%>

<%
	String mode = request.getParameter("mode");
	//����Ʈ ��������
	ArrayList company_list = new ArrayList();
	company_list = (ArrayList)request.getAttribute("CompanyList");
	company = new CompanyInfoTable();
	Iterator company_iter = company_list.iterator();

	//��ũ ���ڿ� ��������
	redirect = new CrmLinkUrl();
	redirect = (CrmLinkUrl)request.getAttribute("Redirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../crm/css/style.css" rel=stylesheet>
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif" align="absmiddle"> ���޾�ü �����Ȳ</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../crm/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../crm/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../crm/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='70'><form method='get' action='../servlet/CrmServlet' name='srForm' style="margin:0">
				  <select name="searchscope" onChange="sel_scope();">
						<option value='company_name'>���޾�ü��</option>
						<option value='chief_name'>��ǥ�ڸ�</option>
						<option value='business_type'>������</option>
						<option value='business_item'>�������</option>
				  </select>&nbsp;</TD>
			  <TD align=left width='70%'>
					<div id="scope1" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'>
						  <tr><td>
						    <select name="type">
								<option value='�����'>�����</option>
								<option value='���¾�ü'>���¾�ü</option>
								<option value='����'>����</option>
								<option value='��Ÿ'>��Ÿ</option>
							</select></td><td width='100%'>&nbsp;<a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=purchase"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
					<div id="scope2" class="collapsed" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td width='400'>
		<%
			//������ ����� ��� �迭 item_list[] ���ڸ� �����ϸ� ��.
			//�ٸ� �κ��� ������ �ʿ� ����.
			String[] item_list = {"�ý���","�ܸ���","DRYER","����","������","�ڻ�","��Ÿ"};
			int sel_cnt = item_list.length;

			for(int i=0; i<sel_cnt; i++) {
				out.println("<input type='checkbox' name='item' value='"+item_list[i]+"'>"+item_list[i]);

			}
		%>							
							</td><td><a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=basic"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
					<div id="scope3" class="expanded" style="position:relative;">
						<table cellSpacing=0 cellPadding=0 width="100%" border='0'><tr><td>
						   <input type="text" name="searchword" size="10" onFocus="document.srForm.searchword.value==''"></td><td width='100%'>&nbsp;<a href="javascript:checkForm();"><img src="../crm/images/bt_search3.gif" border="0"></a> <a href="../servlet/CrmServlet?mode=company_write&module=purchase"><img src="../crm/images/bt_add_new2.gif" border="0"></a></td></tr></table></div>
				<%=input_hidden_search%></form></TD>
			  <TD width='200' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0><form name='listForm' style='magrgin:0'>
        <TBODY>
			<TR vAlign=middle height=22>
			  <TD noWrap width=50 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>���޾�ü��1</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>���޾�ü��2</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��ǥ��ȭ��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>��ǥ�ѽ���ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../crm/images/list_tep.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>Ȩ�����ּ�</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	int no = 1;
	while(company_iter.hasNext()){
		company = (CompanyInfoTable)company_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getNameKor()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getNameEng()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getMainTelNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getMainFaxNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=company.getHomepageUrl()%></td>
			</TR>
			<TR><TD colSpan=11 background="../crm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE></form></TD></TR></TBODY></TABLE>
</body>
</html>


<script language='javascript'>

	//�˻�üũ
	function checkForm(){
		var f = document.srForm;

		if(f.searchscope.value == 'business_type'){
			f.searchword.value = f.type.value;

			if(f.searchword.value.length == ''){
				alert("�������� �����Ͻʽÿ�.");
				return;
			}
		}else if(f.searchscope.value == 'business_item'){
			var items = "";
			var s_count = 0;
			for(i=0;i<f.item.length;i++){
				if(f.item[i].checked){
					items += f.item[i].value + ",";
					s_count ++;
				}
			}
			if(s_count == 0){
			   alert("������ �Ѱ� �̻� �����Ͻʽÿ�.");
			   return;
			}
			
			f.searchword.value = items;

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
	function sel_scope(){
		var f = document.srForm;
		
		if(f.searchscope.value == 'business_type'){
			show('scope1');
			hide('scope2');
			hide('scope3');
		}else if(f.searchscope.value == 'business_item'){
			hide('scope1');
			show('scope2');
			hide('scope3');
		}else{
			hide('scope1');
			hide('scope2');
			show('scope3');
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

	var select_obj;
	
	function ANB_layerAction(obj,status) { 

		var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
		if(_marginx < 0)
			_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
		else
			_tmpx = event.clientX + document.body.scrollLeft ;
		if(_marginy < 0)
			_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
		else
			_tmpy = event.clientY + document.body.scrollTop ;

		obj.style.posLeft=_tmpx-13;
		obj.style.posTop=_tmpy+20;

		if(status=='visible') {
			if(select_obj) {
				select_obj.style.visibility='hidden';
				select_obj=null;
			}
			select_obj=obj;
		}else{
			select_obj=null;
		}
		obj.style.visibility=status; 

	}
</script>