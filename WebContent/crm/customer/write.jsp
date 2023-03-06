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
	customer = (CustomerInfoTable)request.getAttribute("CustomerInfo");
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String input_hidden = link.getInputHidden();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../crm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form method=post name="writeForm" action='../servlet/CrmServlet' enctype='multipart/form-data' style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> 고객정보등록 </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:checkForm();"><img src="../crm/images/bt_reg.gif" border="0"></a></TD>
			  <TD align=left width=30><a href="javascript:history.go(-1);"><img src="../crm/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객회사명</td>
           <td width="37%" height="25" class="bg_04"><table border="0" cellspacing="0" cellpadding="0"><tr><td style="padding-right:5px"><input type="text" name="company_name" value="<%=customer.getCompanyName()%>" size="30" readOnly class="text_01"></td><td><a href="javascript:searchCompany();"><img src="../crm/images/bt_search.gif" border="0"></a></td></tr></table></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">사업자번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_no" value="<%=customer.getCompanyNo()%>" size="12" maxlength="12" readOnly class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객명(한글)</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_kor" value="<%=customer.getNameKor()%>" size="15" maxlength="25" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객명(영문)</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_eng" value="<%=customer.getNameEng()%>" size="20" maxlength="25"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">성별</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="남" name="sex" <% if(customer.getSex().equals("남")||customer.getSex().equals("")) out.print("checked");%>>남 <input type="radio" value="여" name="sex" <% if(customer.getSex().equals("여")) out.print("checked");%>>여</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">직책(직급)</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="position_rank" value="<%=customer.getPositionRank()%>" size="10" maxlength="15"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">부서명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="division_name" value="<%=customer.getDivisionName()%>" size="20" maxlength="25" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주요업무</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_job" value="<%=customer.getMainJob()%>" size="30" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사전화번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_tel_no" value="<%=customer.getCompanyTelNo()%>" size="15" maxlength="25" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사팩스번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_fax_no" value="<%=customer.getCompanyFaxNo()%>" size="15" maxlength="25"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">이동전화번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="mobile_no" value="<%=customer.getMobileNo()%>" size="15" maxlength="25"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사우편번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_post_no" value="<%=customer.getCompanyPostNo()%>" size="7" maxlength="7"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사주소</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="company_address" value="<%=customer.getCompanyAddress()%>" size="70" maxlength="50"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">전자우편</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="email_address" value="<%=customer.getEmailAddress()%>" size="30" maxlength="40"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">홈페이지</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="homepage_url" value="<%=customer.getHomepageUrl()%>" size="30" maxlength="40"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객유형</td>
           <td width="37%" height="25" class="bg_04">
				<select name='customer_type'>
					<option value='매출고객'>매출고객</option>
					<option value='협력업체'>협력업체</option>
					<option value='공통'>공통</option>
					<option value='기타'>기타</option></select>
				<%	if(!customer.getCustomerType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.customer_type.value = '<%=customer.getCustomerType()%>';
						</script>
				<%	}	%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객분류</td>
           <td width="37%" height="25" class="bg_04">
		<%
			//종목이 변경될 경우 배열 item_list[] 인자만 변경하면 됨.
			//다른 부분은 수정될 필요 없음.
			String[] item_list = {"시스템","단말기","DRYER","개발","원자재","자산","기타"};
			int sel_cnt = item_list.length;

			//화면출력
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(customer.getCustomerClass().indexOf(item_list[i]) > 0)
					 out.println("<input type='checkbox' checked name='item' value='"+item_list[i]+"'>"+item_list[i]);
				else out.println("<input type='checkbox' name='item' value='"+item_list[i]+"'>"+item_list[i]);

			}
		%>
				</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--상세정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택전화번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="home_tel_no" value="<%=customer.getHomeTelNo()%>" size="15" maxlength="20"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택팩스번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="home_fax_no" value="<%=customer.getHomeFaxNo()%>" size="15" maxlength="20"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">DM발송처</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="1" name="where_to_dm" <% if(customer.getWhereToDm().equals("1")||customer.getWhereToDm().equals("")) out.print("checked");%>>회사 <input type="radio" value="2" name="where_to_dm" <% if(customer.getWhereToDm().equals("2")) out.print("checked");%>>자택</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택우편번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="home_post_no" value="<%=customer.getHomePostNo()%>" size="10" maxlength="7"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">자택주소</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="home_address" value="<%=customer.getHomeAddress()%>" size="70" maxlength="50"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주민등록번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="personal_no" value="<%=customer.getPersonalNo()%>" size="14" maxlength="14"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">생일</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="birthday" value="<%=customer.getBirthday()%>" size="10" maxlength="10" readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">결혼여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="미혼" name="whether_wedding" <% if(customer.getWhetherWedding().equals("미혼")||customer.getWhetherWedding().equals("")) out.print("checked");%>>미혼 <input type="radio" value="기혼" name="whether_wedding" <% if(customer.getWhetherWedding().equals("기혼")) out.print("checked");%>>기혼</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">결혼기념일</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="wedding_day" value="<%=customer.getWeddingDay()%>" size="10" maxlength="10"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">배우자이름</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="partner_name" value="<%=customer.getPartnerName()%>" size="10" maxlength="10"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">배우자생일</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="partner_birthday" value="<%=customer.getPartnerBirthday()%>" size="10" maxlength="10"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">취미</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="hobby" value="<%=customer.getHobby()%>" size="20" maxlength="25"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">전공분야</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="major_field" value="<%=customer.getMajorField()%>" size="20" maxlength="25"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="other_info" value="<%=customer.getOtherInfo()%>" size="70" maxlength="50"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<input type="hidden" name="no" value="<%=customer.getMid()%>">
<input type="hidden" name="modify_history" value="<%=customer.getModifyHistory()%>">
<input type="hidden" name="customer_class">
<%=input_hidden%>
</form>
</body>
</html>

<script language="javascript">
//필수 입력사항 체크
function checkForm(){ 
	var f = document.writeForm;

	if(f.company_no.value == ''){
		alert("등록하고자 하는 고객이 속한 고객사를 찾아서 선택하십시오.");
		f.company_no.focus();
		return;
	}

	if(f.name_kor.value == ''){
		alert("고객명(한글)을 입력하십시오.");
		f.name_kor.focus();
		return;
	}

	if(f.division_name.value == ''){
		alert("부서명을 입력하십시오.");
		f.division_name.focus();
		return;
	}

	if(f.division_name.value.indexOf("/") > 0){
		alert("부서명에는 / 문자를 삽입할 수 업습니다.");
		f.division_name.value = '';
		f.division_name.focus();
		return;
	}

	if(f.company_tel_no.value == ''){
		alert("회사전화번호를 입력하십시오.");
		f.company_tel_no.focus();
		return;
	}

	var items = "";
	var s_count = 0;
    for(i=0;i<f.item.length;i++){
		if(f.item[i].checked){
			items += "," + f.item[i].value;
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("종목을 한개 이상 선택하십시오.");
	   return;
    }
	
	f.customer_class.value = items;

	f.submit();

}

function sel_item(item_class,obj_name){
	var sParam = "src=../../em/admin/input_item_mgr.jsp&c_class="+item_class+"&frmWidth=400&frmHeight=500&title=sel_item";

		var sRtnValue=showModalDialog("../em/estimate/item_selModal.jsp?"+sParam,"sel_item","dialogWidth:400px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			eval ("document.writeForm." + obj_name + ".value= \"" + sRtnValue + "\"");
		}

}

function searchCompany(){
	wopen("../crm/company/searchCompany.jsp?sf=writeForm&sid=company_no&sname=company_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

</script>