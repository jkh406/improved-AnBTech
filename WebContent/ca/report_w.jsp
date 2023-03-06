<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable table;
%>

<%
	ArrayList approval_list = new ArrayList();
	approval_list = (ArrayList)request.getAttribute("Approval_List");
	Iterator approval_iter = approval_list.iterator();
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
	while(approval_iter.hasNext()){
		table = (CaMasterTable)approval_iter.next();
		String item_no			= table.getItemNo();
		String item_name		= table.getItemName();
		String requestor_info	= table.getRequestorInfo();
		String request_date		= table.getRequestDate();
		String maker_code		= table.getMakerCode();
		String maker_name		= table.getMakerName();
		String maker_part_no	= table.getMakerPartNo();
		String item_desc		= table.getItemDesc();
		String prj_code			= table.getPrjCode();
		String prj_name			= table.getPrjName();
		String model_code		= table.getModelCode();
		String model_name		= table.getModelName();
		String item_unit		= table.getItemUnit();
		String approve_type		= table.getApproveType();
		if(approve_type.equals("A")) approve_type = "합격";
		else if(approve_type.equals("B")) approve_type = "조건부승인";
		else if(approve_type.equals("F")) approve_type = "사용금지";

		String apply_date		= table.getApplyDate()==null?"":table.getApplyDate();
		String apply_quantity	= table.getApplyQuantity()==null?"":table.getApplyQuantity();
		String why_approve		= com.anbtech.text.StringProcess.getContentTxt(table.getWhyApprove());
		String file_link		= table.getFileLink();
		String other_info		= com.anbtech.text.StringProcess.getContentTxt(table.getOtherInfo());
%>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_info%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">의뢰일자</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련프로젝트</td>
           <td width="37%" height="25" class="bg_04">[<%=prj_code%>] <%=prj_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">관련모델</td>
           <td width="37%" height="25" class="bg_04">[<%=model_code%>] <%=model_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목번호</td>
           <td width="37%" height="25" class="bg_04"><%=item_no%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목명</td>
           <td width="37%" height="25" class="bg_04"><%=item_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">품목설명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=item_desc%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>		 
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		 		 
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인업체</td>
           <td width="37%" height="25" class="bg_04">[<%=maker_code%>] <%=maker_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">업체부품번호</td>
           <td width="37%" height="25" class="bg_04"><%=maker_part_no%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">첨부문서</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=file_link%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">승인구분</td>
           <td width="37%" height="25" class="bg_04"><%=approve_type%></td>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용수량</td>
           <td width="37%" height="25" class="bg_04"><%=apply_quantity%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">적용일자</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=apply_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">판정사유</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=why_approve%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ca/images/bg-01.gif">비고</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=other_info%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table><br>
<%
	}
%>

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