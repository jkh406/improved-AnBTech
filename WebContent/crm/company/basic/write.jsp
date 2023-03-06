<%@ include file= "../../../admin/configHead.jsp"%>
<%@ page 
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.crm.entity.*"
%>
<%!
	CompanyInfoTable company;
	CrmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	company = (CompanyInfoTable)request.getAttribute("CompanyInfo");
	link = (CrmLinkUrl)request.getAttribute("Redirect");
	String input_hidden = link.getInputHidden();
%>

<%  //첨부파일
	int ref_count		= 1;	// 최대 참조문서 개수 지정
	int enableupload	= 1;	// 업로드 개수 지정
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%
	CompanyInfoTable file = new CompanyInfoTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("FILE_LIST");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	if(file_iter.hasNext()){
		file = (CompanyInfoTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" 삭제! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../crm/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form method=post name="writeForm" action='../servlet/CrmServlet' enctype='multipart/form-data' style="margin:0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../crm/images/blet.gif"> 고객사정보등록 </TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:checkForm();"><img src="../crm/images/bt_reg.gif" border="0"></a>
			  <a href="javascript:history.go(-1);"><img src="../crm/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">사업자번호</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" name="company_no" value="<%=company.getCompanyNo()%>" size="12" maxlength="10" onKeyPress="currency(this);" <% if(mode.equals("company_modify")) out.print("readOnly onClick=\"javascript:alert('사업자등록번호는 수정할 수 없습니다. 잘못 입력하셨다면 삭제 후 다시 입력하십시요.');\""); %> class="text_01"> (-)를 제외한 사업자등록번호 10자리를 입력하십시오.</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객사명1</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_kor" value="<%=company.getNameKor()%>" maxlength="80" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객사명2</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="name_eng" value="<%=company.getNameEng()%>" maxlength="80"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표전화번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_tel_no" value="<%=company.getMainTelNo()%>" size="15" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표팩스번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_fax_no" value="<%=company.getMainFaxNo()%>" size="15" maxlength="30"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표자명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_name" value="<%=company.getChiefName()%>" size="15" maxlength="30"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표자주민번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_personal_no" value="<%=company.getChiefPersonalNo()%>" maxlength="14"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">고객유형</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_type'>
					<option value='매출고객'>매출고객</option>
					<option value='협력업체'>협력업체</option>
					<option value='공통'>공통</option>
					<option value='기타'>기타</option></select>
				<%	if(!company.getBusinessType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_type.value = '<%=company.getBusinessType()%>';
						</script>
				<%	}	%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">사업종목</td>
           <td width="37%" height="25" class="bg_04">
		<%
			//종목이 변경될 경우 배열 item_list[] 인자만 변경하면 됨.
			//다른 부분은 수정될 필요 없음.
			String[] item_list = {"시스템","단말기","DRYER","개발","원자재","자산","기타"};
			int sel_cnt = item_list.length;

			//화면출력
			for(int m=0,k=1; m<sel_cnt; m++,k++) {
				if(company.getBusinessItem().indexOf(item_list[m]) > 0)
					 out.println("<input type='checkbox' checked name='item' value='"+item_list[m]+"'>"+item_list[m]);
				else out.println("<input type='checkbox' name='item' value='"+item_list[m]+"'>"+item_list[m]);

			}
		%>
				</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">우편번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="company_post_no" value="<%=company.getCompanyPostNo()%>" size="7" maxlength="7"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">홈페이지주소</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="homepage_url" value="<%=company.getHomepageUrl()%>" size="30" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">회사주소</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" name="company_address" value="<%=company.getCompanyAddress()%>" size="60"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>		  
		  <!-- 화일 첨부 -->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">사업자등록증</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				
				<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>		   
	     </td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="25" colspan="4"><img src="../crm/images/detailed_info.gif" width="209" height="25" border="0"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표자명</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_name" value="<%=company.getChiefName()%>" size="15" maxlength="30"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">대표자주민번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="chief_personal_no" value="<%=company.getChiefPersonalNo()%>" maxlength="14"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<!--
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">업태(형태)</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_type'>
					<option value=''>선택하십시오.</option>
					<option value='제조업'>제조업</option>
					<option value='서비스업'>서비스업</option>
					<option value='도소매업'>도소매업</option>
					<option value='연구개발'>연구개발</option>
					<option value='기타'>기타</option></select>
				<%	if(!company.getBusinessType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_type.value = '<%=company.getBusinessType()%>';
						</script>
				<%	}	%></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">종목</td>
           <td width="37%" height="25" class="bg_04">
				<select name='business_item'>
					<option value=''>선택하십시오.</option>
					<option value='프로그램'>프로그램</option>
					<option value='전산장비'>전산장비</option>
					<option value='기타'>기타</option></select>
				<%	if(!company.getBusinessItem().equals("")){	%>
						<script language='javascript'>
							document.writeForm.business_item.value = '<%=company.getBusinessItem()%>';
						</script>
				<%	}	%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
-->
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">거래시작일자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="trade_start_time" value="<%=company.getTradeStartTime()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('trade_start_time');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">거래종료일자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="trade_end_time" value="<%=company.getTradeEndTime()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('trade_end_time');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">단체구분</td>
           <td width="37%" height="25" class="bg_04">
				<select name='company_type'>
					<option value=''>선택하십시오.</option>
					<option value='상장법인'>상장법인</option>
					<option value='비상장법인'>비상장법인</option>
					<option value='개인사업자'>개인사업자</option>
					<option value='기타'>기타</option></select>
				<%	if(!company.getCompanyType().equals("")){	%>
						<script language='javascript'>
							document.writeForm.company_type.value = '<%=company.getCompanyType()%>';
						</script>
				<%	}	%></td>		   
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">거래구분</td>
           <td width="37%" height="25" class="bg_04">
			   <input type="radio" value="매출" name="trade_type" <% if(company.getTradeType().equals("매출")) out.print("checked");%>>매출 <input type="radio" value="매입" name="trade_type" <% if(company.getTradeType().equals("매입")) out.print("checked");%>>매입 <input type="radio" value="매출입" name="trade_type" <% if(company.getTradeType().equals("매출입")) out.print("checked");%>>매출,매입</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">신용도</td>
           <td width="37%" height="25" class="bg_04">
				<select name='credit_level'>
					<option value=''>선택하십시오.</option>
					<option value='상'>상</option>
					<option value='중'>중</option>
					<option value='하'>하</option>
				<%	if(!company.getCreditLevel().equals("")){	%>
						<script language='javascript'>
							document.writeForm.credit_level.value = '<%=company.getCreditLevel()%>';
						</script>
				<%	}	%></td>		   		   
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">견적요청도</td>
           <td width="37%" height="25" class="bg_04">
				<select name='estimate_req_level'>
					<option value=''>선택하십시오.</option>
					<option value='상'>상</option>
					<option value='중'>중</option>
					<option value='하'>하</option>
				<%	if(!company.getEstimateReqLevel().equals("")){	%>
						<script language='javascript'>
							document.writeForm.estimate_req_level.value = '<%=company.getEstimateReqLevel()%>';
						</script>
				<%	}	%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">종업원수</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="worker_number" value="<%=company.getWorkerNumber()%>" size="5" maxlength="5" onKeyPress="currency(this);">명</td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주거래은행</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_bank_name" value="<%=company.getMainBankName()%>" maxlength="50"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주요게재신문</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_newspaper_name" value="<%=company.getMainNewspaperName()%>" maxlength="50"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">주요생산품</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="main_product_name" value="<%=company.getMainProductName()%>" maxlength="80"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">법인등록번호</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="corporation_no" value="<%=company.getCorporationNo()%>" maxlength="14"></td>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">설립일자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="founding_day" value="<%=company.getFoundingDay()%>" size="10" readOnly> <a href="Javascript:OpenCalendar('founding_day');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="4" name="other_info" cols="80"><%=company.getOtherInfo()%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

<input type="hidden" name="no" value="<%=company.getMid()%>">
<input type="hidden" name="modify_history" value="">
<input type="hidden" name="business_item">
<%=input_hidden%>
</form>
</body>
</html>

<script language="javascript">

//필수 입력사항 체크
function checkForm(){ 
	var f = document.writeForm;

	if(f.company_no.value.length != 10){
		alert("사업자등록번호를 올바로 입력하십시오.");
		f.company_no.focus();
		return;
	}

	if(!isValidOffNum(f.company_no.value)){
		alert("유효한 사업자등록번호가 아닙니다.");
		f.company_no.focus();
		return;
	}

	if(f.name_kor.value == ''){
		alert("고객사명을 입력하십시오.");
		f.name_kor.focus();
		return;
	}

	if(f.main_tel_no.value == ''){
		alert("대표전화번호를 입력하십시오.");
		f.main_tel_no.focus();
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
	
	f.business_item.value = items;
	f.submit();

}
//사업자 번호 체크
function isValidOffNum(tmpStr){
	tmpSum = new Number(0);
	tmpMod = new Number(0);
	resValue = new Number(0);
	var intOffNo = new Array(0,0,0,0,0,0,0,0,0,0);
	var strChkNum = new Array(1,3,7,1,3,7,1,3,5);

	for(i = 0 ; i < 10 ; i ++){
		intOffNo[i] = new Number(tmpStr.substring(i, i+1));
	}

	for(i = 0 ; i < 9 ; i ++){
		tmpSum = tmpSum + (intOffNo[i]*strChkNum[i]);
	}

	tmpSum = tmpSum + ((intOffNo[8]*5)/10);
	tmpMod = parseInt(tmpSum%10, 10);

	if(tmpMod == 0){
		resValue = 0;
	}
	else{
		resValue = 10 - tmpMod;
	}

	if(resValue == intOffNo[9]){
		return true;
	}
	else{
		return false;
	}
} 

function sel_item(item_class,obj_name){
	var sParam = "src=../../em/admin/input_item_mgr.jsp&c_class="+item_class+"&frmWidth=400&frmHeight=500&title=sel_item";

		var sRtnValue=showModalDialog("../em/estimate/item_selModal.jsp?"+sParam,"sel_item","dialogWidth:400px;dialogHeight:500px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");

		if (typeof sRtnValue != "undefined" && sRtnValue != "")
		{
			eval ("document.writeForm." + obj_name + ".value= \"" + sRtnValue + "\"");
		}

}

/**********************
 * 숫자만 입력되게
 **********************/
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}
</script>