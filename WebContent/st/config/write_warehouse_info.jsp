<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%
	WarehouseInfoTable table = new WarehouseInfoTable();
	table = (WarehouseInfoTable)request.getAttribute("WAREHOUSE_INFO");

	String mode					= request.getParameter("mode");
	String title				= "창고정보등록";
	if(mode.equals("modify_warehouse_info")) title = "창고정보수정";
	
	String mid				= table.getMid();
	String warehouse_code	= table.getWarehouseCode();
	String warehouse_name	= table.getWarehouseName();
	String warehouse_type	= table.getWarehouseType();
	String factory_code		= table.getFactoryCode();
	String factory_name		= table.getFactoryName();
	String warehouse_address= table.getWarehouseAddress();
	String group_name		= table.getGroupName();
	String manager_name	= table.getManagerName();
	String manager_id		= table.getManagerId();
	String using_mrp		= table.getUsingMrp();
	String client			= table.getClient();		

	String readonly			= "";
	if(mode.equals("modify_warehouse_info")) readonly = "readonly";	
	
	if(warehouse_type.equals("") || warehouse_type==null) warehouse_type="in";
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../st/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<FORM name="eForm" method="post" action="StockConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../st/images/blet.gif"> <%=title%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../st/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<IMG src='../st/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">공장코드</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' name='factory_code' value='<%=factory_code%>' class="text_01" readonly>
					<a href="javascript:get_factory_info();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle">
				</TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">공장명</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='15' name='factory_name' value='<%=factory_name%>' class="text_01" readonly></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">창고코드</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' name='warehouse_code' value='<%=warehouse_code%>' <%=readonly%> class="text_01"></TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">창고명</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='15' name='warehouse_name' value='<%=warehouse_name%>' <%=readonly%> class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">창고타입</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="in" <%if(warehouse_type.equals("in")) out.print("checked"); %> name="warehouse_type"> 사내창고 
				<INPUT type="radio" value="out" <%if(warehouse_type.equals("out")) out.print("checked"); %> name="warehouse_type"> 거래처창고
			</TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">창고그룹명</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='15' name='group_name' value='<%=group_name%>' <%=readonly%>></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>

		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">창고주소</TD>
            <TD width="85%" height="25" class="bg_04" colspan='3'>
				<INPUT type='text' size='50'  name='warehouse_address' value='<%=warehouse_address%>' <%=readonly%>></TD>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>

			</TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
	</TBODY></TABLE><BR>
<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</FORM>
</BODY>
</HTML>


<SCRIPT LANGUAGE=JAVASCRIPT>

function checkForm(){
	var f=document.eForm;
	
	if(f.factory_code.value==""){
		alert("공장코드를 찾아 선택하십시오.");
		f.factory_code.focus();
		return;
	}

	if(f.warehouse_code.value==""){
		alert("창고코드를 입력하십시오.");
		f.warehouse_code.focus();
		return;
	}

	if(f.warehouse_name.value==""){
		alert("창고명을 입력하십시오.");
		f.warehouse_name.focus();
		return;
	}
	
	f.submit();
}


function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function get_factory_info(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}
</script>