<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%
	StockConfInfoTable table = new StockConfInfoTable();
	table = (StockConfInfoTable)request.getAttribute("STOCK_CONF");

	String mode					= request.getParameter("mode");
	String title				= "�����������";
	if(mode.equals("modify_conf_type")) title = "������������";	
	String mid					= table.getMid();
	String trade_type_code		= table.getTradeTypeCode();
	String trade_type_name		= table.getTradeTypeName();
	String stock_rise_fall		= table.getStockRiseFall();	
	String stock_type1			= table.getStockType1();
	String stock_type2			= table.getStockType2()==null?"":table.getStockType2();
	String is_cost_apply		= table.getIsCostApply();
	String is_count_posting		= table.getIsCountPosting();
	String is_wharehouse_move	= table.getIsWharehouseMove();
	String is_factory_move		= table.getIsFactoryMove();
	String is_item_move			= table.getIsItemMove();
	String is_no_move			= table.getIsNoMove();

	String readonly				= "";
	if(mode.equals("modify_conf_type")) readonly = "readonly";	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../st/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM name="eForm" method="post" action="StockConfigMgrServlet" enctype="multipart/form-data">
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
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">���������ڵ�</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='5' maxlength='2' name='trade_type_code' value='<%=trade_type_code%>' <%=readonly%> class="text_01" maxlength="2"></TD>
            <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">����������</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type='text' size='15' name='trade_type_name' value='<%=trade_type_name%>' <%=readonly%> class="text_01"></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�����������</TD>
            <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="1" <%if(stock_rise_fall.equals("1")) out.print("checked"); %> name="stock_rise_fall">���� 
				<INPUT type="radio" value="2" <%if(stock_rise_fall.equals("2")) out.print("checked"); %> name="stock_rise_fall">����
				<INPUT type="radio" value="3" <%if(stock_rise_fall.equals("3")) out.print("checked"); %> name="stock_rise_fall">����</TD>
           
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">���ܰ��ݿ�����</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_cost_apply.equals("y")) out.print("checked"); %> name="is_cost_apply">�� 
				<INPUT type="radio" value="n" <%if(is_cost_apply.equals("n")) out.print("checked"); %> name="is_cost_apply">�ƴϿ�</TD>
		</TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">â���̵�����</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_wharehouse_move.equals("y")) out.print("checked"); %> name="is_wharehouse_move">�� 
				<INPUT type="radio" value="n" <%if(is_wharehouse_move.equals("n")) out.print("checked"); %> name="is_wharehouse_move">�ƴϿ�</TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">���尣�̵�����</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_factory_move.equals("y")) out.print("checked"); %> name="is_factory_move">�� 
				<INPUT type="radio" value="n" <%if(is_factory_move.equals("n")) out.print("checked"); %> name="is_factory_move">�ƴϿ�</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">ǰ���̵�����</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_item_move.equals("y")) out.print("checked"); %> name="is_item_move">�� 
				<INPUT type="radio" value="n" <%if(is_item_move.equals("n")) out.print("checked"); %> name="is_item_move">�ƴϿ�</TD>
           <TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">�������̵�����</TD>
           <TD width="35%" height="25" class="bg_04">
				<INPUT type="radio" value="y" <%if(is_no_move.equals("y")) out.print("checked"); %> name="is_no_move">�� 
				<INPUT type="radio" value="n" <%if(is_no_move.equals("n")) out.print("checked"); %> name="is_no_move">�ƴϿ�</TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR><TD width="15%" height="25" class="bg_03" background="../st/images/bg-01.gif">ȸ��Pointing����</TD>
           <TD width="85%" height="25" class="bg_04" colspan='3'>
				<INPUT type="radio" value="y" <%if(is_count_posting.equals("y")) out.print("checked"); %> name="is_count_posting">�� 
				<INPUT type="radio" value="n" <%if(is_count_posting.equals("n")) out.print("checked"); %> name="is_count_posting">�ƴϿ�</TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE><BR>

<INPUT type='hidden' name='stock_type2' value='<%=stock_type2%>'>
<INPUT type='hidden' name='stock_type1' value='<%=stock_type1%>'>
<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</FORM>
</BODY>
</HTML>


<SCRIPT LANGUAGE=JAVASCRIPT>

function checkForm(){
	var f=document.eForm;

	if(f.trade_type_code.value==""){
		alert("�������� �ڵ带 �Է��Ͻʽÿ�.");
		f.trade_type_code.focus();
		return;
	}

	if(f.trade_type_name.value==""){
		alert("�������� ���� �Է��Ͻʽÿ�.");
		f.trade_type_name.focus();
		return;
	}

	f.submit();
}

function searchCompany(){
	
	wopen("../st/config/searchSystemMinorCode.jsp?sf=eForm&type=<%=trade_type_code%>&code=code&code_name=code_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
</script>