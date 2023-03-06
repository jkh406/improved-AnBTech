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
	 * proposal_data 테이블 정보 가져오기
	 ******************************/
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
	String file_preview		= proposal.getFilePreview();

	String data_no			= proposal.getAncestor()==null?"":proposal.getAncestor();
	String modify_info		= proposal.getModifyHistory()==null?"수정사항 없음.":proposal.getModifyHistory();
	String why_revision		= proposal.getWhyRevision()==null?"":proposal.getWhyRevision();
	String preview			= proposal.getPreview()==null?"":proposal.getPreview();

	String goods_name		= proposal.getGoodsName()==null?"":proposal.getGoodsName();
	String why_propose		= proposal.getWhyPropose()==null?"":proposal.getWhyPropose();
	String country_name		= proposal.getCountryName()==null?"":proposal.getCountryName();
	String comp_name		= proposal.getCompanyName()==null?"":proposal.getCompanyName();
	String customer_name	= proposal.getCustomerName()==null?"":proposal.getCustomerName();
	String customer_tel		= proposal.getCustomerTel()==null?"":proposal.getCustomerTel();

	/******************************
	 * 현재분류에 대한 환경 가져오기
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//프로젝트 관리여부(y/n)
	String enable_model		= dmsenv.getEnableModel();		//관련모델명 관리여부(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//리비젼 관리여부(y/n)
	
	/******************************
	 * 링크 문자열 가져오기
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
<% if(page_no != null){	//대출처리 문서 보기시에 타이틀을 숨기기 위해%>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif"> 문서등록정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
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

<!--내용-->
<form method=get name="viewForm" action='../servlet/AnBDMS' style="margin:0">
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
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">담당자</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">제안구분</td>
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

    <!--제안회사정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">제안제품명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=goods_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">제안사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_propose%></td></tr>
		 <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">제안회사</td>
           <td width="37%" height="25" class="bg_04"><%=comp_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">제안국가</td>
           <td width="37%" height="25" class="bg_04"><%=country_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">담당자명</td>
           <td width="37%" height="25" class="bg_04"><%=customer_name%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">담당자전화</td>
           <td width="37%" height="25" class="bg_04"><%=customer_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
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

//리비젼
function why_revision(tablename,category,searchscope,searchword,page,no,data_id,ver) {

	var url = "../dms/proposal_data/revision.jsp?tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

	wopen(url,'revision','350','139','scrollbars=no,toolbar=no,status=no,resizable=no');
/*
	var sParam = "src=revision.jsp&frmWidth=400&frmHeight=300&title=revision_type";
	
	var sRtnValue=showModalDialog("../dms/proposal_data/modalFrm.jsp?"+sParam,"revision","dialogWidth:400px;dialogHeight:300px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		sParam = "&tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver+"&why="+sRtnValue;
		location.href = "../servlet/AnBDMS?mode=revision" + sParam;
	}
*/
}

//대출신청
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

	if (confirm("정말 삭제 하시겠습니까?")){
			var sParam = "&tablename="+tablename+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword;
			sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;
			location.href = "../servlet/AnBDMS?mode=delete" + sParam;   
	} else {
	   return;
	}
}

function go_print(url)
{
	wopen("../servlet/"+url,'print','730','600','scrollbars=yes,toolbar=yes,status=no,resizable=no');
}

function confirm_reg(url){

	if (confirm("현재 문건을 등록처리하시겠습니까?")){
		location.href = url;
	}
}
</script>

