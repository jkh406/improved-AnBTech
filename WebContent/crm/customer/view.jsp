<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CustomerInfoTable customer;
	CrmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	customer = new CustomerInfoTable();
	customer = (CustomerInfoTable)request.getAttribute("CustomerInfo");

	link = new CrmLinkUrl();
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String link_modify = link.getLinkModify();
	String link_delete = link.getLinkDelete();

	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("CR01");
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../crm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> 고객상세정보 </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:location.href='<%=link_list%>'"><img src="../crm/images/bt_list.gif" border="0"></a></TD>
			  <TD align=left width=30><a href="javascript:location.href='<%=link_modify%>'"><img src="../crm/images/bt_modify.gif" border="0"></a></TD>
			  <% if(login_id.equals(customer.getWriter()) || idx >= 0){ //등록자 또는 모듈관리자만 삭제 가능 %>
			  <TD align=left width=30><a href="javascript:confirm_del();"><img src="../crm/images/bt_del.gif" border="0"></a></TD>
			  <% } %>
			  </TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/basic_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객명(한글)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getNameKor()%>(<%=customer.getSex()%>)</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객명(영문)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getNameEng()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사명</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">부서명</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getDivisionName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">직책(직급)</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPositionRank()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주요업무</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMainJob()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사전화번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyTelNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사팩스번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyFaxNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">이동전화번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMobileNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사우편번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCompanyPostNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사주소</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getCompanyAddress()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">전자우편</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getEmailAddress()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">홈페이지</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomepageUrl()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객유형</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCustomerType()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객분류</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getCustomerClass().substring(1)%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--상세정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택전화번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomeTelNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택팩스번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomeFaxNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">DM발송처</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWhereToDm().equals("1")?"회사":"자택"%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택우편번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHomePostNo()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택주소</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getHomeAddress()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주민등록번호</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPersonalNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">생년월일</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getBirthday()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">결혼여부</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWhetherWedding()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">결혼기념일</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWeddingDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">배우자이름</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPartnerName()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">배우자생년월일</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getPartnerBirthday()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">취미</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getHobby()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">전공분야</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getMajorField()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=customer.getOtherInfo()%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--등록정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/reg_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWriterInfo()%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">등록일자</td>
           <td width="37%" height="25" class="bg_04"><%=customer.getWrittenDay()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">정보변경이력</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><div style="position:relative; visibility:visible; width:100%; height:50; overflow:auto;"><table><tr><td><%=customer.getModifyHistory()%></td></tr></table></div></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</body>
</html>

<script language="javascript">
function confirm_del(){
	var f = confirm("삭제하고자 하는 고객정보가 다른 모듈에 이미 사용되었을 수 있습니다.\n이 경우 다른 모듈에 예기치 못한 에러를 유발할 수 있으니 유의하십시요.\n정말로 삭제하시겠습니까?");
	if(f) location.href = "<%=link_delete%>";
}
</script>