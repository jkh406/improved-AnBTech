<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	StShiftInfoTable table;
%>
<%
	String mode = request.getParameter("mode");	// 모드
	table = (StShiftInfoTable)request.getAttribute("ITEM_SHIFT_INFO");
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../st/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM name="eForm" method="post" action="StockMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../st/images/blet.gif"> 재고이동등록(공장간) </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
					<IMG src='../st/images/bt_reg.gif' onClick='javascript:checkSave();' style='cursor:hand' align='absmiddle'>
					<IMG src='../st/images/bt_cancel.gif' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
					</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">대상품목코드</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='10' name='sr_item_code' readOnly class="text_01"> <a href="javascript:searchCMInfo();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">대상품목명</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='20' name='sr_item_name' readOnly class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">대상품목설명</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='35' name='sr_item_desc' readOnly class="text_01"></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동수량/단위</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='quantity' onKeyPress="currency(this);" class="text_01"> <INPUT type='text' size='5' name='stock_unit' readOnly class="text_01"></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동전공장코드</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='sr_factory_code' value='' readOnly class="text_01"> <a href="javascript:sel_factory('source');"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
		   <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동전공장명</TD>
           <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='sr_factory_name' readOnly class="text_01"> </TD></TR>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동후공장코드</TD>
			<TD width="35%" height="25" class="bg_04"><INPUT type='text' size='5' name='dt_factory_code' readOnly class="text_01"> <a href="javascript:sel_factory('destination');"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
			<TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">이동후공장명</TD>
			<TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='dt_factory_name' readOnly class="text_01"></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>
		
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">등록자</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getRequestorInfo()%>/<%=table.getRequestorId()%></TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">등록일자</TD>
           <TD width="35%" height="25" class="bg_04"><%=table.getRegDate()%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>

<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='sr_item_type'>
<INPUT type='hidden' name='shift_type' value='F'>
<INPUT type='hidden' name='dt_item_code'>
<INPUT type='hidden' name='dt_item_name'>
<INPUT type='hidden' name='dt_item_desc'>
<INPUT type='hidden' name='dt_stock_unit'>
<INPUT type='hidden' name='dt_item_type'>
<INPUT type='hidden' name='requestor_name' value='<%=table.getRequestorInfo()%>'>
<INPUT type='hidden' name='requestor_id' value='<%=table.getRequestorId()%>'>
<INPUT type='hidden' name='reg_date' value='<%=table.getRegDate()%>'>
</FORM>
</BODY>
</HTML>

<SCRIPT language=javascript>

//필수입력사항체크
function checkSave(){
	var f = document.eForm;

	if(f.sr_item_code.value==''){
		alert("이동대상 품목을 찾아서 선택하십시오.");
		return;
	}

	if(f.quantity.value == '' || f.quantity.value == '0'){
		alert("이동할 수량을 입력하십시오.(이동수량은 0보다 커야 합니다.");
		f.quantity.focus();
		return;
	}

	if(f.sr_factory_code.value == ''){
		alert("이동전 공장을 찾아서 선택하십시오.");
		return;
	}

	if(f.dt_factory_code.value == ''){
		alert("이동후 공장을 찾아서 선택하십시오.");
		return;
	}

	if(f.sr_factory_code.value == f.dt_factory_code.value){
		alert("이동전 공장과 이동후 공장은 같을 수 없습니다.");
		return;
	}

	f.dt_item_code.value = f.sr_item_code.value;
	f.dt_item_name.value = f.sr_item_name.value;
	f.dt_item_desc.value = f.sr_item_desc.value;
	f.dt_item_type.value = f.sr_item_type.value;

	document.onmousedown=dbclick;

	f.submit();
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// 숫자만 입력되게
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

// 공장 선택
function sel_factory(mode){
	
	var f = document.eForm;

	if(mode=='source'){
		var factory_code = f.sr_factory_code.name;
		var factory_name = f.sr_factory_name.name;
	} else if(mode=='destination'){
		var factory_code = f.dt_factory_code.name;
		var factory_name = f.dt_factory_name.name;
	}

	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field="+factory_code+"/"+factory_name;
	wopen(url,'enterCode','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 품목정보 가져오기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=sr_item_code&item_name=sr_item_name&item_type=sr_item_type&item_desc=sr_item_desc&item_unit=stock_unit";
	wopen(strUrl,"search",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
</SCRIPT>