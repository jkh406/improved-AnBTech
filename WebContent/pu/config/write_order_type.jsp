<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	OrderTypeTable table = new OrderTypeTable();
	table = (OrderTypeTable)request.getAttribute("ORDER_TYPE_INFO");

	String mode = request.getParameter("mode");

	String mid			= table.getMid();
	String type			= table.getOrderType();
	String name			= table.getOrderName();
	String is_import	= table.getIsImport();
	String is_enter		= table.getIsEnter();
	String is_return	= table.getIsReturn();
	String is_sageup	= table.getIsSageup();
	String is_using		= table.getIsUsing();
	String is_pass		= table.getIsPass();
	String is_purchase	= table.getIsPurchase();
	String is_shipping  = table.getIsShipping();
	String enter_code	= table.getEnterCode();
	String enter_name	= table.getEnterName();
	String purchase_code= table.getPurchaseCode();
	String purchase_name= table.getPurchaseName();
	String outgo_code	= table.getOutgoCode();
	String outgo_name	= table.getOutgoName();
	
	String caption ="발주형태등록";

	if(mode.equals("modify_order_type")) caption ="발주형태수정";
%>
<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0">
<form name="eForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> <%=caption%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../pu/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_cancel.gif' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주형태코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='2' name='type' maxlength='2' value='<%=type%>' class="text_01"> *영문대분자 2자리</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주형태명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='name' value='<%=name%>' class="text_01"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">수입여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" <%if(is_import.equals("y")) out.print("checked"); %> name="is_import">예 <input type="radio" value="n" name="is_import" <%if(is_import.equals("n")) out.print("checked"); %>>아니오</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">통관여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_pass" <%if(is_pass.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_pass" <%if(is_pass.equals("n")) out.print("checked"); %>>아니오</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_enter" <%if(is_enter.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_enter" <%if(is_enter.equals("n")) out.print("checked"); %>>아니오</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">입고형태코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='enter_code' value='<%=enter_code%>'> <a href="javascript:get_enter_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">매입여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_purchase" <%if(is_purchase.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_purchase" <%if(is_purchase.equals("n")) out.print("checked"); %>>아니오</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">매입형태코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' maxlength='2' name='purchase_code' value='<%=purchase_code%>'> <a href="javascript:get_purchase_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">반품여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_return" <%if(is_return.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_return" <%if(is_return.equals("n")) out.print("checked"); %>>아니오</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사급여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_sageup" <%if(is_sageup.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_sageup" <%if(is_sageup.equals("n")) out.print("checked"); %>>아니오</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사용여부</td>
           <td width="37%" height="25" class="bg_04"><input type="radio" value="y" name="is_using" <%if(is_using.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_using" <%if(is_using.equals("n")) out.print("checked"); %>>아니오</td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">출고형태코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='outgo_code' value='<%=outgo_code%>' maxlength='2'> <a href="javascript:get_outgo_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">선적여부</td>
           <td width="87%" height="25" class="bg_04" colspan='3'><input type="radio" value="y" name="is_shipping" <%if(is_shipping.equals("y")) out.print("checked"); %>>예 <input type="radio" value="n" name="is_shipping" <%if(is_shipping.equals("n")) out.print("checked"); %>>아니오</td>
        </tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>
<input type='hidden' name='enter_name' value='<%=enter_name%>'>
<input type='hidden' name='purchase_name' value='<%=purchase_name%>'>
<input type='hidden' name='outgo_name' value='<%=outgo_name%>'>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='mid' value='<%=mid%>'>
</form>
</body>
</html>


<script language=javascript>
var f = document.eForm;

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function get_enter_code(){
	url = "../pu/config/searchInoutCode.jsp?tablename=pu_inout_type&field=enter_code/enter_name";
	wopen(url,'enterCode','400','309','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function get_purchase_code(){
	url = "../pu/config/searchPurchaseCode.jsp?tablename=pu_purchase_type&field=purchase_code/purchase_name";
	wopen(url,'purchase','400','309','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function get_outgo_code(){
	url = "../pu/config/searchInoutCode.jsp?tablename=pu_inout_type&field=outgo_code/outgo_name";
	wopen(url,'outgoCode','400','309','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function checkForm(){

	if(f.type.value==""){
		alert("발주형태코드를 입력하십시요.");
		f.type.focus();
		return;
	}
	if(f.name.value==""){
		alert("발주형태명을 입력하십시요.");
		f.name.focus();
		return;
	}
/*
	if(f.enter_code.value==""){
		alert("입고형태코드를 입력하십시요.");
		f.enter_code.focus();
		return;
	}
	if(f.purchase_code.value==""){
		alert("매입형태코드를 입력하십시요.");
		f.purchase_code.focus();
		return;
	}

	if(f.outgo_code.value==""){
		alert("출고형태코드를 입력하십시요.");
		f.outgo_code.focus();
		return;
	}
*/
	f.submit();
}
</script>