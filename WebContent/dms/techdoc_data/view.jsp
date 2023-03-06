<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable master;
	TechDocTable techdoc;
	LinkUrl redirect;
	DmsEnvTable dmsenv;
%>

<%
	String mode = request.getParameter("mode");
	String no = request.getParameter("no");
	String data_id = request.getParameter("d_id");
	String category = request.getParameter("category");
	String page_no = request.getParameter("page");
	String searchword = request.getParameter("searchword");
	String searchscope = request.getParameter("searchscope");

	/******************************
	 * master_data ���̺� ���� ��������
	 ******************************/
	master = new MasterTable();
	master = (MasterTable)request.getAttribute("MasterInfo");
	String subject			= master.getSubject()==null?"":master.getSubject();
	String category_info	= master.getWhereCategory();
	String doc_no			= master.getDocNo();
	String model_code		= master.getModelCode();
	String model_name		= master.getModelName();
	String pjt_code			= master.getPjtCode();
	String pjt_name			= master.getPjtName();
	String node_code		= master.getNodeCode();
	String node_name		= master.getNodeName();

	if(!model_code.equals("") && model_code != null){
		com.anbtech.gm.business.makeGoodsTreeItems mt = new com.anbtech.gm.business.makeGoodsTreeItems();
		model_name = mt.getGoodsClassStrByModelCode(0,model_code,"") + model_name;
	}

	/******************************
	 * techdoc_data ���̺� ���� ��������
	 ******************************/
	techdoc = new TechDocTable();
	techdoc = (TechDocTable)request.getAttribute("techdoc_data");
	String where_from		= techdoc.getWhereFrom()==null?"":techdoc.getWhereFrom();
	String security_level	= techdoc.getSecurityLevel()==null?"":techdoc.getSecurityLevel();
	String save_period		= techdoc.getSavePeriod()==null?"":techdoc.getSavePeriod();
	String written_lang		= techdoc.getWrittenLang()==null?"":techdoc.getWrittenLang();
	String doc_type			= techdoc.getDocType()==null?"":techdoc.getDocType();
	String save_url			= techdoc.getSaveUrl()==null?"":techdoc.getSaveUrl();
	String copy_num			= techdoc.getCopyNum()==null?"":techdoc.getCopyNum();
	String reference		= techdoc.getReference()==null?"":techdoc.getReference();
	String writer			= techdoc.getWriterS();
	String register			= techdoc.getRegisterS();
	String register_day		= techdoc.getRegisterDay();
	String ver_code			= techdoc.getVerCode();
	String file_link		= techdoc.getFileLink();
	String file_preview		= techdoc.getFilePreview();

	String data_no			= techdoc.getAncestor()==null?"":techdoc.getAncestor();
	String modify_info		= techdoc.getModifyHistory()==null?"�������� ����.":techdoc.getModifyHistory();
	String why_revision		= techdoc.getWhyRevision()==null?"":techdoc.getWhyRevision();
	String preview			= techdoc.getPreview()==null?"":techdoc.getPreview();

	/******************************
	 * ����з��� ���� ȯ�� ��������
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//������Ʈ ��������(y/n)
	String enable_model		= dmsenv.getEnableModel();		//���ø𵨸� ��������(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//������ ��������(y/n)
	
	/******************************
	 * ��ũ ���ڿ� ��������
	******************************/
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInView");
	String link_list		= redirect.getLinkList();
	String link_modify		= redirect.getLinkModify();
	String link_revision	= redirect.getLinkRevision();
	String input_hidden		= redirect.getInputHidden();
	String link_commit		= redirect.getLinkCommit();
	String link_loan		= redirect.getLinkLoan();
	String link_delete		= redirect.getLinkDelete();
	String link_print		= redirect.getLinkPrint();
	String link_approval	= redirect.getLinkApproval();

%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<% if(page_no != null){	//����ó�� ���� ����ÿ� Ÿ��Ʋ�� ����� ����%>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif"> �����������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<%	 if(page_no != null){
							if(mode.equals("view") || mode.equals("view_t")){	%>
										<a href='<%=link_list%>'><img src="../dms/images/bt_list.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_revision%>'><img src="../dms/images/bt_revision.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_loan%>'><img src="../dms/images/bt_reg_loan.gif" border="0" align="absmiddle"></a>
										<a href="javascript:go_print('<%=link_print%>');"><img src="../dms/images/bt_print.gif" border="0" align="absmiddle"></a>
					<%		}if(mode.equals("view_a")){	%>
										<a href='<%=link_list%>'><img src="../dms/images/bt_list.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_modify%>'><img src="../dms/images/bt_modify.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_delete%>'><img src="../dms/images/bt_del.gif" border="0" align="absmiddle"></a>
										<a href="javascript:go_print('<%=link_print%>');"><img src="../dms/images/bt_print.gif" border="0" align="absmiddle"></a>
										<a href="javascript:confirm_reg('<%=link_commit%>')"><img src="../dms/images/bt_reg_process.gif" border="0" align="absmiddle"></a>
					<%		}if(mode.equals("view_m")){	%>
										<a href='<%=link_list%>'><img src="../dms/images/bt_list.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_modify%>'><img src="../dms/images/bt_modify.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_delete%>'><img src="../dms/images/bt_del.gif" border="0" align="absmiddle"></a>
										<a href='<%=link_approval%>'><img src="../dms/images/bt_sangsin.gif" border="0" align="absmiddle"></a>
					<%
							}
						}
					%>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>
<%	}	%>

<!--����-->
<form method=get name="viewForm" action='../servlet/AnBDMS' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04"><%=ver_code%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����з�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=category_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	if(enable_model.equals("y")){ //���ø𵨰����Ҷ��� ���	%>				
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ø�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=model_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
<%	if(enable_project.equals("y")){	//������Ʈ �����Ҷ��� ���	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ð���</td>
           <td width="37%" height="25" class="bg_04"><%=pjt_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����������</td>
           <td width="37%" height="25" class="bg_04"><%=node_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<%	}	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�ۼ���</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ó</td>
           <td width="37%" height="25" class="bg_04"><%=where_from%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=register%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=register_day%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" cols="93" readOnly><%=preview%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" cols="93" readOnly><%=why_revision%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<!--
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����̷�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" cols="93" readOnly><%=modify_info%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
-->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ȵ��</td>
           <td width="37%" height="25" class="bg_04"><%=security_level%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����Ⱓ</td>
           <td width="37%" height="25" class="bg_04"><%=save_period%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�ۼ����</td>
           <td width="37%" height="25" class="bg_04"><%=written_lang%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����Ÿ��</td>
           <td width="37%" height="25" class="bg_04"><%=doc_type%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ġ</td>
           <td width="37%" height="25" class="bg_04"><%=save_url%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����Ǽ�</td>
           <td width="37%" height="25" class="bg_04"><%=copy_num%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����ڷ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=reference%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<%=input_hidden%>
</form>
</td></tr></table>
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

//������
function why_revision(tablename,category,searchscope,searchword,page,no,data_id,ver) {

	var url = "../dms/techdoc_data/revision.jsp?tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

	wopen(url,'revision','350','139','scrollbars=no,toolbar=no,status=no,resizable=no');
/*
	var sParam = "src=revision.jsp&frmWidth=400&frmHeight=300&title=revision_type";
	
	var sRtnValue=showModalDialog("../dms/techdoc_data/modalFrm.jsp?"+sParam,"revision","dialogWidth:400px;dialogHeight:300px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver+"&why="+sRtnValue;
		location.href = "../servlet/AnBDMS?mode=revision" + sParam;
	}
*/
}

//�����û
function req_loan(tablename,category,searchscope,searchword,page,no,data_id,ver) {

	var url = "../dms/loan_list/req_loan.jsp?tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

	wopen(url,'revision','450','200','scrollbars=no,toolbar=no,status=no,resizable=no');

/*
	var sParam = "src=req_loan.jsp&frmWidth=400&frmHeight=300&title=req_loan";
	
	var sRtnValue=showModalDialog("../dms/loan_list/modalFrm.jsp?"+sParam,"loan","dialogWidth:400px;dialogHeight:300px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver+"&why="+sRtnValue;
		location.href = "../servlet/AnBDMS?mode=req_loan" + sParam;
	}
*/
}

function confirm_del(tablename,category,searchscope,searchword,page,no,data_id,ver){

	if (confirm("���� ���� �Ͻðڽ��ϱ�?")){
			var sParam = "&tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword;
			sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;
			location.href = "../servlet/AnBDMS?mode=delete" + sParam;   
	} else {
	   return;
	}
}

function go_print(url)
{
	wopen("../servlet/"+url,'print','670','600','scrollbars=yes,toolbar=yes,status=no,resizable=no');
}

function confirm_reg(url){

	if (confirm("���� ������ ���ó���Ͻðڽ��ϱ�?")){
		location.href = url;
	}
}
</script>

