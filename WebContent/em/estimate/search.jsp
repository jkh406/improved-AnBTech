<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
%>
<HTML><HEAD><TITLE>상세검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
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
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">견적회사명</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="company_name" size="15"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">견적고객명</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="charge_name" size="15"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">견적서제목</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="estimate_subj" size="15"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">견적번호</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="estimate_no" size="15"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">작성자이름</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="writer" size="15"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="../images/bg-01.gif">작성일자</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="s_day" size="9"> ~ <input type="text" name="e_day" size="9"><br>(예:20030801 ~ 20031230)</td></tr>
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

	var company_name = f.company_name.value;
	var charge_name = f.charge_name.value;
	var estimate_subj = f.estimate_subj.value;
	var estimate_no = f.estimate_no.value;
	var writer = f.writer.value;

	var s_day = f.s_day.value;
	var e_day = f.e_day.value;
	var day = s_day + e_day;

	if(day.length > 0 && day.length != 16){
		alert("검색일자를 올바로 입력하십시오.");
		return;
	}

	var str = '';
	if(company_name != '') str += "and|company_name|" + company_name + ",";
	if(charge_name != '') str += "and|charge_name|" + charge_name + ",";
	if(estimate_subj != '') str += "and|estimate_subj|" + estimate_subj + ",";
	if(estimate_no != '') str += "and|estimate_no|" + estimate_no + ",";
	if(writer != '') str += "and|writer|" + writer + ",";
	if(day != '') str += "and|written_day|" + day + ",";

	//window.returnValue = str;
	opener.location.href = "../../servlet/EstimateMgrServlet?mode=list&searchscope=detail&searchword="+str;
	self.close();
}

function search_user(){
	var to = "searchForm.register_id";
	window.open("../../admin/UserTreeMainForSingle.jsp?target="+to+"&type=single","user","width=300,height=400,scrollbar=yes,toolbar=no,status=no,resizable=no");
}
</script>