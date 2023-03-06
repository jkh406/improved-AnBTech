<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "com.anbtech.text.Hanguel"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String category_info	= Hanguel.toHanguel(request.getParameter("category_info"));
	String category			= request.getParameter("category");
	String mode				= request.getParameter("mode");
%>

<HTML><HEAD><TITLE>상세검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_search_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="searchForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">문서분류</td>
           <td width="80%" height="25" class="bg_04"><%=category_info%></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">문서제목</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="subject" size="20"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">등록일자</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="s_day" size="9"> ~ <input type="text" name="e_day" size="9" > (예)20030623 ~ 20030830</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">작성자이름</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="writer" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">문서번호</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="doc_no" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">보존년한</td>
           <td width="80%" height="25" class="bg_04">
					<select size="1" name="save_period">
						<option value="">선택</option>
						<option value="1">1년</option>
						<option value="3">3년</option>
						<option value="5">5년</option>
						<option value="10">10년</option>
						<option value="0">영구</option>		
					</select>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">보안등급</td>
           <td width="80%" height="25" class="bg_04">
					<select size="1" name="security_level">
						<option value="">선택</option>
						<option value="1">1급</option>
						<option value="2">2급</option>
						<option value="3">3급</option>
						<option value="4">대외비</option>
						<option value="5">일반</option>
					</select>						   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">제안회사명</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="company_name" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">제안국가명</td>
           <td width="80%" height="25" class="bg_04"><input type="text" name="country_name" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go()'><img src='../images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script language='javascript'>

function go() {

	var f = document.searchForm;

	var subject			= f.subject.value;
	var s_day			= f.s_day.value;
	var e_day			= f.e_day.value;
	var day				= s_day + e_day;

	if(day.length > 0 && day.length != 16){
		alert("등록일자를 올바르게 입력하십시오.");
		return;
	}

	var writer			= f.writer.value;
//	var division		= f.division.value;
	var doc_no			= f.doc_no.value;
//	var eco_no			= f.eco_no.value;
	var save_period		= f.save_period.value;
	var security_level	= f.security_level.value;
	var company_name	= f.company_name.value;
	var country_name	= f.country_name.value;

	var subject_and		= 'and';
	var day_and			= 'and';
	var writer_and		= 'and';
//	var division_and	= 'and';
	var doc_no_and		= 'and';
//	var eco_no_and		= 'and';
	var save_period_and = 'and';
	var security_level_and = 'and';
	var company_name_and = 'and';
	var country_name_and = 'and';

/* or 검색이 의미가 없어 기능에서 삭제함.
	for(var i=0; i<2;i++){
		if(f.subject_and[i].checked) subject_and = f.subject_and[i].value;
		if(f.day_and[i].checked) day_and = f.day_and[i].value;
		if(f.writer_and[i].checked) writer_and = f.writer_and[i].value;

		if(f.division_and[i].checked) division_and = f.division_and[i].value;
		if(f.doc_no_and[i].checked) doc_no_and = f.doc_no_and[i].value;
		if(f.eco_no_and[i].checked) eco_no_and = f.eco_no_and[i].value;
		if(f.save_period_and[i].checked) save_period_and = f.save_period_and[i].value;
		if(f.security_level_and[i].checked) security_level_and = f.security_level_and[i].value;
	}
*/

	var str = '';
	if(subject != '') str+= subject_and + "|subject|" + subject + ",";
	if(day != '') str+= day_and + "|register_day|" + day + ",";
	if(writer != '') str += writer_and + "|writer_s|"+writer+",";
//	if(division != '') str += division_and + "|writer_s|"+division+",";
	if(doc_no != '') str += doc_no_and + "|doc_no|"+doc_no+",";
//	if(eco_no != '') str += eco_no_and + "|eco_no|"+eco_no+",";
	if(save_period != '') str += save_period_and + "|save_period|"+save_period+",";
	if(security_level != '') str += security_level_and + "|security_level|"+security_level+",";
	if(company_name != '') str += company_name_and + "|company_name|"+company_name+",";
	if(country_name != '') str += country_name_and + "|country_name|"+country_name+",";

//	alert(str);
//	window.returnValue = str;
	opener.location.href = "../../servlet/AnBDMS?category=<%=category%>&searchscope=detail&searchword="+str;
	self.close();
}
</script>