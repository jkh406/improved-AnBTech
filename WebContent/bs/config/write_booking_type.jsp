<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.bs.entity.*"
%>
<%
	BookingTypeTable table = new BookingTypeTable();
	table = (BookingTypeTable)request.getAttribute("SALES_CONF");

	String mode				= request.getParameter("mode");
	String title			= "수주형태등록";
	if(mode.equals("modify_booking_type")) title = "수주형태수정";	
	String mid				= table.getMid();
	String order_code		= table.getOrderCode();
	String order_name		= table.getOrderName();
	String is_export		= table.getIsExport();	
	String is_return		= table.getIsReturn();
	String is_entry			= table.getIsEntry();
	String is_shipping		= table.getIsShipping();
	String is_auto_ship		= table.getIsAutoShip();
	String is_sale			= table.getIsSale();
	String shipping_type	= table.getShippingType();
	String is_use			= table.getIsUse();

	String readonly			= "";
	if(mode.equals("modify_booking_type")) readonly = "readonly";	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../bs/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" action="SalesConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bs/images/blet.gif"> <%=title%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../bs/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<IMG src='../bs/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">수주형태코드</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='5' maxlength='2' name='order_code' value='<%=order_code%>' <%=readonly%> class="text_01" maxlength="2"></TD>
            <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">수주형태명</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='15' maxlength='15' name='order_name' value='<%=order_name%>' class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">수출여부</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_export.equals("y")) out.print("checked"); %> name="is_export">예 
				<INPUT type="radio" value="n" <%if(is_export.equals("n")) out.print("checked"); %> name="is_export">아니오</TD>
           
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">반품여부</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_return.equals("y")) out.print("checked"); %> name="is_return">예 
				<INPUT type="radio" value="n" <%if(is_return.equals("n")) out.print("checked"); %> name="is_return">아니오</TD>
		</TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">통관여부</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_entry.equals("y")) out.print("checked"); %> name="is_entry">예 
				<INPUT type="radio" value="n" <%if(is_entry.equals("n")) out.print("checked"); %> name="is_entry">아니오</TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">출하여부</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_shipping.equals("y")) out.print("checked"); %> name="is_shipping">예 
				<INPUT type="radio" value="n" <%if(is_shipping.equals("n")) out.print("checked"); %> name="is_shipping">아니오</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">자동출하생성여부</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_auto_ship.equals("y")) out.print("checked"); %> name="is_auto_ship">예 
				<INPUT type="radio" value="n" <%if(is_auto_ship.equals("n")) out.print("checked"); %> name="is_auto_ship">아니오</TD>
           <TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">매출여부</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_sale.equals("y")) out.print("checked"); %> name="is_sale">예 
				<INPUT type="radio" value="n" <%if(is_sale.equals("n")) out.print("checked"); %> name="is_sale">아니오</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">사용여부</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_use.equals("y")) out.print("checked"); %> name="is_use">예 
				<INPUT type="radio" value="n" <%if(is_use.equals("n")) out.print("checked"); %> name="is_use">아니오</TD>
			<TD width="15%" height="25" class="bg_03" background="../bs/images/bg-01.gif">출하형태</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="text" name='shipping_type' class="text_01" size='15' maxlength='25' value='<%=shipping_type%>'></TD>
		</TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</FORM>
</BODY>
</HTML>


<SCRIPT LANGUAGE=JAVASCRIPT>
<!--

function checkForm(){
	var f=document.eForm;

	if(f.order_code.value==""){
		alert("수주형태 코드를 입력하십시요.");
		f.order_code.focus();
		return;
	}

	if(f.order_name.value==""){
		alert("수주형태명을 입력하십시요.");
		f.order_name.focus();
		return;
	}

	if(f.shipping_type.value==""){
		alert("출하형태를 입력하십시요.");
		f.shipping_type.focus();
		return;
	}

	f.submit();
}

function searchCompany(){
	//wopen("","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>