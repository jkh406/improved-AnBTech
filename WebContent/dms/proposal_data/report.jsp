<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable master;
	ProposalTable proposal;
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

	//master_data ���� ��������
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

	//proposal_data ���� ��������
	proposal = new ProposalTable();
	proposal = (ProposalTable)request.getAttribute("proposal_data");
	String where_from		= proposal.getWhereFrom()==null?"":proposal.getWhereFrom();
	String security_level	= proposal.getSecurityLevel()==null?"":proposal.getSecurityLevel();
	String save_period		= proposal.getSavePeriod()==null?"":proposal.getSavePeriod();
	String written_lang		= proposal.getWrittenLang()==null?"":proposal.getWrittenLang();
	String doc_type			= proposal.getDocType()==null?"":proposal.getDocType();
	String save_url			= proposal.getSaveUrl()==null?"":proposal.getSaveUrl();
	String copy_num			= proposal.getCopyNum()==null?"":proposal.getCopyNum();
	String reference		= proposal.getReference()==null?"":proposal.getReference();
	String writer			= proposal.getWriterS();
	String register			= proposal.getRegisterS();
	String register_day		= proposal.getRegisterDay();
	String ver_code			= proposal.getVerCode();
	String file_link		= proposal.getFileLink();
	String file_preview		=  proposal.getFilePreview();

	String data_no			= proposal.getAncestor()==null?"":proposal.getAncestor();
	String modify_info		= proposal.getModifyHistory()==null?"�������� ����.":proposal.getModifyHistory();
	String why_revision		= proposal.getWhyRevision()==null?"":proposal.getWhyRevision();
	String preview			= proposal.getPreview()==null?"":proposal.getPreview();

	String goods_name		= proposal.getGoodsName()==null?"":proposal.getGoodsName();
	String why_propose		= proposal.getWhyPropose()==null?"":proposal.getWhyPropose();
	String country_name		= proposal.getCountryName()==null?"":proposal.getCountryName();
	String comp_name		= proposal.getCompanyName()==null?"":proposal.getCompanyName();
	String customer_name	= proposal.getCustomerName()==null?"":proposal.getCustomerName();
	String customer_tel		= proposal.getCustomerTel()==null?"":proposal.getCustomerTel();

	
	//��ũ ���ڿ� ��������
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInView");
	String link_list		= redirect.getLinkList();
	String link_modify		= redirect.getLinkModify();
	String link_revision	= redirect.getLinkRevision();
	String input_hidden		= redirect.getInputHidden();
	String link_commit		= redirect.getLinkCommit();
	String link_loan		= redirect.getLinkLoan();
	String link_delete		= redirect.getLinkDelete();

	/******************************
	 * ����з��� ���� ȯ�� ��������
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//������Ʈ ��������(y/n)
	String enable_model		= dmsenv.getEnableModel();		//���ø𵨸� ��������(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//������ ��������(y/n)
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmeun="return false">
<div id="page_content" style="position:absolute;left:0;top:0;width:100%">

<!--����-->
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
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=model_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
<%	if(enable_project.equals("y")){	//������Ʈ �����Ҷ��� ���	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������Ʈ��</td>
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
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ȱ���</td>
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

    <!--����ȸ������-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">������ǰ��</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=goods_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���Ȼ���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_propose%></td></tr>
		 <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����ȸ��</td>
           <td width="37%" height="25" class="bg_04"><%=comp_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">���ȱ���</td>
           <td width="37%" height="25" class="bg_04"><%=country_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">����ڸ�</td>
           <td width="37%" height="25" class="bg_04"><%=customer_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">�������ȭ</td>
           <td width="37%" height="25" class="bg_04"><%=customer_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--��������-->
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
</td></tr></table>

</div> 
<script language="JavaScript1.2"> 
function iframe_reset(){ 
        dataobj=document.all? document.all.page_content : document.getElementById("page_content") 
         
        dataobj.style.top=0 
        dataobj.style.left=0 

        pagelength=dataobj.offsetHeight 
        pagewidth=dataobj.offsetWidth 

        parent.document.all.iframe_main.height=pagelength 
        parent.document.all.iframe_main.width=pagewidth 
} 
window.onload=iframe_reset 
</script>
</body>
</html>