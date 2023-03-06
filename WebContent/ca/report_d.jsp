<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable master;
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<div id="page_content" style="position:absolute;left:0;top:0;width:100%">

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
<%
	master = new CaMasterTable();
	master = (CaMasterTable)request.getAttribute("CA_Info");

	String approval_no		= master.getApprovalNo();
	String item_no			= master.getItemNo();
	String item_name			= master.getItemName();
	String approver_info	= master.getApproverInfo();
	String approve_date		= master.getApproveDate();
	String requestor_info	= master.getRequestorInfo();
	String request_date		= master.getRequestDate();
	String maker_code		= master.getMakerCode();
	String maker_name		= master.getMakerName();
	String maker_part_no	= master.getMakerPartNo();
	String item_desc		= master.getItemDesc();
	String prj_code			= master.getPrjCode();
	String prj_name			= master.getPrjName();
	String model_code		= master.getModelCode();
	String model_name		= master.getModelName();
	String item_unit		= master.getItemUnit();
	String approve_type		= master.getApproveType();
	if(approve_type.equals("A")) approve_type = "합격";
	else if(approve_type.equals("B")) approve_type = "조건부승인";
	else if(approve_type.equals("F")) approve_type = "사용금지";
	String apply_date		= master.getApplyDate()==null?"":master.getApplyDate();
	String apply_quantity	= master.getApplyQuantity()==null?"":master.getApplyQuantity();
	String why_approve		= com.anbtech.text.StringProcess.getContentTxt(master.getWhyApprove());
	String file_link		= master.getFileLink();
	String other_info		= com.anbtech.text.StringProcess.getContentTxt(master.getOtherInfo());
	String change_info		= com.anbtech.text.StringProcess.getContentTxt(master.getChangeInfo());

%>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">사용금지사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=change_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품번호</td>
           <td width="37%" height="25" class="bg_04"><%=item_no%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품명</td>
           <td width="37%" height="25" class="bg_04"><%=item_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인부품설명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=item_desc%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인번호</td>
           <td width="37%" height="25" class="bg_04"><%=approval_no%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인업체</td>
           <td width="37%" height="25" class="bg_04">[<%=maker_code%>] <%=maker_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">업체부품번호</td>
           <td width="37%" height="25" class="bg_04"><%=maker_part_no%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인구분</td>
           <td width="37%" height="25" class="bg_04"><%=approve_type%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용일자</td>
           <td width="37%" height="25" class="bg_04"><%=apply_date%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용수량</td>
           <td width="37%" height="25" class="bg_04"><%=apply_quantity%> <%=item_unit%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인판정사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=why_approve%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련문서</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">비고</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=other_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인자</td>
           <td width="37%" height="25" class="bg_04"><%=approver_info%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인일시</td>
           <td width="37%" height="25" class="bg_04"><%=approve_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련과제</td>
           <td width="37%" height="25" class="bg_04">[<%=prj_code%>] <%=prj_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련모델</td>
           <td width="37%" height="25" class="bg_04">[<%=model_code%>] <%=model_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
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
</body></html>