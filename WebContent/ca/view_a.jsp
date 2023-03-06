<%@ include file="../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.ca.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	CaMasterTable master;
	CaLinkUrl redirect;
%>

<%
	String no = request.getParameter("no");

	//ca_master 에서 가져오기
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
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ca/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ca/images/blet.gif"> 승인원상세정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="javascript:history.go(-1);"><img src="../ca/images/bt_list.gif" border="0" align="absmiddle"></a>
					<a href="javascript:go_print(<%=no%>);"><img src="../ca/images/bt_print.gif" border="0" align="absmiddle"></a>
					<a href="javascript:location.href='../servlet/ComponentApprovalServlet?mode=write_r&no=<%=no%>'"><img src="../ca/images/bt_reg_modify.gif" border="0" align="absmiddle"></a>
					<a href="javascript:location.href='../servlet/ComponentApprovalServlet?mode=write_d&no=<%=no%>'"><img src="../ca/images/bt_cancel_app.gif" border="0" align="absmiddle"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
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

function go_print(no)
{
	wopen("../servlet/ComponentApprovalServlet?mode=print&no="+no,'print','730','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>