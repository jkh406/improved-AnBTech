<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%
	FactoryInfoTable table = new FactoryInfoTable();
	table = (FactoryInfoTable)request.getAttribute("FACTORY_INFO");

	String mode					= request.getParameter("mode");
	String title				= "�����������";
	if(mode.equals("modify_factory_info")) title = "������������";
	
	String mid					= table.getMid();
	String factory_code			= table.getFactoryCode();	
	String factory_name			= table.getFactoryName();	
	String production_type		= table.getProductionType();	
	String main_product			= table.getMainProduct();	
	String factory_address		= table.getFactoryAddress();	
	String product_plan_term	= table.getProductPlanTerm();	
	String mps_confirm_term		= table.getMpsConfirmTerm();	
	String mps_plan_term		= table.getMpsPlanTerm();	
	String mrp_confirm_term		= table.getMrpConfirmTerm();				
	String agency_code			= table.getAgencyCode();
	String agency_name			= table.getAgencyName();

	String readonly				= "";
	if(mode.equals("modify_factory_info")) readonly = "readOnly";	
	if(production_type.equals("") || production_type==null) production_type="in";
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../st/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" action="StockConfigMgrServlet" enctype="multipart/form-data" style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../st/images/blet.gif"> <%=title%></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
						<IMG src='../st/images/bt_save.gif' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
						<IMG src='../st/images/bt_cancel.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�����</TD>
            <TD width="85%" height="25" class="bg_04" colspan='3'><INPUT type='text' name='agency_code' value='<%=agency_code%>' size="5"> <INPUT type='text' name='agency_name'	value='<%=agency_name%>' size="15" readOnly> <a href="javascript:sel_agency();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
	   	<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�����ڵ�</TD>
            <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='factory_code' value='<%=factory_code%>' <%=readonly%> class="text_01"></TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�����</TD>
            <TD width="35%" height="25" class="bg_04"><INPUT type='text' size='15' name='factory_name' value='<%=factory_name%>' class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">����Ÿ��</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="in" <%if(production_type.equals("in")) out.print("checked"); %> name="production_type"> �ڻ���� 
				<INPUT type="radio" value="out" <%if(production_type.equals("out")) out.print("checked"); %> name="production_type"> ���ֻ���
			</TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�ֿ����ǰ��</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='10' name='main_product' value='<%=main_product%>' <%=readonly%>></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>

		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�����ּ�</TD>
            <TD width="85%" height="25" class="bg_04" colspan='3'>
				<INPUT type='text' size='50'  name='factory_address' value='<%=factory_address%>' <%=readonly%>></TD>
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
		alert("�����ڵ带 �Է��Ͻʽÿ�.");
		f.factory_code.focus();
		return;
	}

	if(f.factory_name.value==""){
		alert("������� �Է��Ͻʽÿ�.");
		f.factory_name.focus();
		return;
	}
	f.submit();
}


// ����� ����
function sel_agency(){
	
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=eForm&div=one&type=AGENCY&code=agency_code&code_name=agency_name";
	wopen(url,'agency','500','307','scrollbars=auto,toolbar=no,status=no,resizable=no');
	//sf=���̸�&div=���д��&type=Ư�������ڵ�&code=�ڵ��ʵ�&code_name=�ڵ���ʵ�
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>