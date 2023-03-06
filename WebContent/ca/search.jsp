<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../admin/errorpage.jsp"
%>
<%
	String category = request.getParameter("category");
	String mode = request.getParameter("mode");
%>
<HTML><HEAD><TITLE>상세검색</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="css/style.css" rel="stylesheet" type="text/css">
</HEAD>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="images/pop_search_d.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="searchForm" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">승인번호</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="approval_no" size="15"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">부품번호</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="item_no" size="15"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">업체부품번호</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="maker_part_no" size="15"</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">부품설명</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="item_desc" size="15"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">승인판정구분</td>
           <td width="70%" height="25" class="bg_04">
					<select size="1" name="approve_type">
						<option value="">선택</option>
						<option value="A">합격</option>
						<option value="B">한정(조건부)승인</option>
<!--					<option value="C"></option>
						<option value="D"></option>
						<option value="E"></option>
						<option value="F"></option> -->		
					</select>						   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">승인자이름</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="approver_info" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">의뢰자이름</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="requestor_info" size="10"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">승인일자</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="s_day" size="8" maxlength="8"> ~ <input type="text" name="e_day" size="8" maxlength="8"><br>(예:20030806 ~ 20031231)</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_03" background="images/bg-01.gif">승인업체명</td>
           <td width="70%" height="25" class="bg_04"><input type="text" name="maker_name" size="15"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:go()'><img src='images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close()'><img src='images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>

function go() {

	var f = document.searchForm;

	var approval_no = f.approval_no.value;
	var item_no = f.item_no.value;
	var item_desc = f.item_desc.value;
	var maker_part_no = f.maker_part_no.value;
	var approve_type = f.approve_type.value;
	var approver_info = f.approver_info.value;
	var requestor_info = f.requestor_info.value;
	var maker_name = f.maker_name.value;
	var s_day = f.s_day.value;
	var e_day = f.e_day.value;
	var day = s_day + e_day;

	if(day.length > 0 && day.length != 16){
		alert("검색일자를 올바로 입력하십시오.");
		return;
	}

	var str = '';
	if(approval_no != '') str += "and|approval_no|" + approval_no + ",";
	if(item_no != '') str += "and|item_no|" + item_no + ",";
	if(item_desc != '') str += "and|item_desc|" + item_desc + ",";
	if(maker_part_no != '') str += "and|maker_part_no|" + maker_part_no + ",";
	if(approve_type != '') str += "and|approve_type|" + approve_type + ",";
	if(approver_info != '') str += "and|approver_info|" + approver_info + ",";
	if(requestor_info != '') str += "and|requestor_info|" + requestor_info + ",";
	if(maker_name != '') str += "and|maker_name|" + maker_name + ",";
	if(day != '') str += "and|approve_date|" + day + ",";

//	alert(str);
//	window.returnValue = str;

	sParam = "mode=<%=mode%>&category=<%=category%>&searchscope=detail&searchword="+str;
	opener.location.href = "../servlet/ComponentApprovalServlet?"+sParam;
	self.close();
}
</script>