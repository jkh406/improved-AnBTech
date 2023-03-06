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

	//master_data 에서 가져오기
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

	//techdoc_data 에서 가져오기
	techdoc = new TechDocTable();
	techdoc = (TechDocTable)request.getAttribute("techdoc_data");
	String where_from = techdoc.getWhereFrom()==null?"":techdoc.getWhereFrom();
	String security_level = techdoc.getSecurityLevel()==null?"":techdoc.getSecurityLevel();
	String save_period = techdoc.getSavePeriod()==null?"":techdoc.getSavePeriod();
	String written_lang = techdoc.getWrittenLang()==null?"":techdoc.getWrittenLang();
	String doc_type = techdoc.getDocType()==null?"":techdoc.getDocType();
	String save_url = techdoc.getSaveUrl()==null?"":techdoc.getSaveUrl();
	String copy_num = techdoc.getCopyNum()==null?"":techdoc.getCopyNum();
	String reference = techdoc.getReference()==null?"":techdoc.getReference();
	String writer = techdoc.getWriterS();
	String register = techdoc.getRegisterS();
	String register_day = techdoc.getRegisterDay();
	String ver_code = techdoc.getVerCode();
	String file_link = techdoc.getFileLink();
	String file_preview =  techdoc.getFilePreview();

	String data_no = techdoc.getAncestor()==null?"":techdoc.getAncestor();
	String modify_info = techdoc.getModifyHistory()==null?"수정사항 없음.":techdoc.getModifyHistory();
	String why_revision = techdoc.getWhyRevision()==null?"":techdoc.getWhyRevision();
	String preview = techdoc.getPreview()==null?"":techdoc.getPreview();

	
	//링크 문자열 가져오기
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInView");
	String link_list = redirect.getLinkList();
	String link_modify = redirect.getLinkModify();
	String link_revision = redirect.getLinkRevision();
	String input_hidden = redirect.getInputHidden();
	String link_commit = redirect.getLinkCommit();
	String link_loan = redirect.getLinkLoan();
	String link_delete = redirect.getLinkDelete();

	/******************************
	 * 현재분류에 대한 환경 가져오기
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//프로젝트 관리여부(y/n)
	String enable_model		= dmsenv.getEnableModel();		//관련모델명 관리여부(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//리비젼 관리여부(y/n)
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmeun="return false">
<div id="page_content" style="position:absolute;left:0;top:0;width:100%">

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서버젼</td>
           <td width="37%" height="25" class="bg_04"><%=ver_code%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서분류</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=category_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	if(enable_model.equals("y")){ //관련모델관리할때만 출력	%>				
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">형상명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=model_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
<%	if(enable_project.equals("y")){	//프로젝트 관리할때만 출력	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">프로젝트명</td>
           <td width="37%" height="25" class="bg_04"><%=pjt_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서종류명</td>
           <td width="37%" height="25" class="bg_04"><%=node_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<%	}	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">작성자</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서출처</td>
           <td width="37%" height="25" class="bg_04"><%=where_from%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><%=register%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">등록일</td>
           <td width="37%" height="25" class="bg_04"><%=register_day%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--문서정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서요약</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" cols="93" readOnly><%=preview%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">리비젼사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" cols="93" readOnly><%=why_revision%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<!--
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">수정이력</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" cols="93" readOnly><%=modify_info%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
-->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보안등급</td>
           <td width="37%" height="25" class="bg_04"><%=security_level%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보존기간</td>
           <td width="37%" height="25" class="bg_04"><%=save_period%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">작성언어</td>
           <td width="37%" height="25" class="bg_04"><%=written_lang%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서타입</td>
           <td width="37%" height="25" class="bg_04"><%=doc_type%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보관위치</td>
           <td width="37%" height="25" class="bg_04"><%=save_url%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서건수</td>
           <td width="37%" height="25" class="bg_04"><%=copy_num%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">참조자료</td>
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