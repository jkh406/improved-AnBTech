<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*,com.anbtech.admin.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable master;
	TechDocTable techdoc;
	LinkUrl redirect;
	ApprovalInfoTable app_table;
	DmsEnvTable dmsenv;
%>

<%
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig();
	String writer_name		= app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName();
	String memo				= app_table.getMemo();


	String mode = request.getParameter("mode");
	String no = request.getParameter("no");
	String data_id = request.getParameter("d_id");
	String category = request.getParameter("category");
	String page_no = request.getParameter("page");
	String searchword = request.getParameter("searchword");
	String searchscope = request.getParameter("searchscope");

	/******************************
	 * master_data 테이블 정보 가져오기
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
	 * techdoc_data 테이블 정보 가져오기
	 ******************************/
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

	
	/******************************
	 * 링크 문자열 가져오기
	******************************/
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
<head>
<title>기술문서</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5" oncontextmenu="return false">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../dms/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">기술문서</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../dms/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../dms/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=56 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">문서 번호</td>
				<td width="35%" class="bg_06"><%=doc_no%></td>
				<td width="15%" height="25" align="middle" class="bg_05">문서 버젼</td>
				<td width="35%" class="bg_06"><%=ver_code%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">문서 분류</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=category_info%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">문서 제목</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=subject%></td></tr>
<%	if(enable_model.equals("y")){ //관련모델관리할때만 출력	%>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">형상명</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=model_name%></td></tr>
<%	}	%>
<%	if(enable_project.equals("y")){	//프로젝트 관리할때만 출력	%>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">프로젝트명</td>
				<td width="35%" class="bg_06"><%=pjt_name%></td>
				<td width="15%" height="25" align="middle" class="bg_05">문서종류명</td>
				<td width="35%" class="bg_06"><%=node_name%></td></tr>
<%	}	%>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">작&nbsp; 성&nbsp; 자</td>
				<td width="35%" class="bg_06"><%=writer%></td>
				<td width="15%" height="25" align="middle" class="bg_05">문서 출처</td>
				<td width="35%" class="bg_06"><%=where_from%></td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">등&nbsp; 록&nbsp; 자</td>
				<td width="35%" class="bg_06"><%=register%></td>
				<td width="15%" height="25" align="middle" class="bg_05">등&nbsp; 록&nbsp; 일</td>
				<td width="35%" class="bg_06"><%=register_day%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">첨부 파일</td>
				<td width="85%" class="bg_06" colspan="3"><%=file_link%></td></tr></table><BR><!-- 공통 정보 입력 끝 -->
	</td></tr>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="630" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">주요 내용</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><textarea rows="14" cols="80" style="border:0" readOnly><%=preview%></textarea></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">변경 사항</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><textarea rows="8" cols="80" style="border:0" readOnly><%=why_revision%></textarea></td></tr>
<!--
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">수정 이력</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><textarea rows="5" cols="80" style="border:0" readOnly><%=modify_info%></textarea></td></tr>
-->
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">보안 등급</td>
				<td width="35%" class="bg_06"><%=security_level%></td>
				<td width="15%" height="25" align="middle" class="bg_05">보존 기간</td>
				<td width="35%" class="bg_06"><%=save_period%></td></tr>
            <tr>
				<td width="15%" height="25" align="middle" class="bg_05">작성 언어</td>
				<td width="35%" class="bg_06"><%=written_lang%></td>
				<td width="15%" height="25" align="middle" class="bg_05">문서 유형</td>
				<td width="35%" class="bg_06"><%=doc_type%></td></tr>
            <tr>
				<td width="15%" height="25" align="middle" class="bg_05">보관 위치</td>
				<td width="35%" class="bg_06"><%=save_url%></td>
				<td width="15%" height="25" align="middle" class="bg_05">문서 건수</td>
				<td width="35%" class="bg_06"><%=copy_num%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">참조 자료</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><img src='' width='0' height='0'><%=reference%></td></tr>
			</table>
      </td></tr></table>
</body></html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>