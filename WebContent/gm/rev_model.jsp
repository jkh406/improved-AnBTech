<%@ include file="../admin/configHead.jsp"%>
<%@ include file="../admin/chk/chkGM01.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.gm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%!
	GoodsInfoTable table;
	GoodsInfoItemTable spec;
	GmLinkUrl redirect;
%>
<%
	String mode					= request.getParameter("mode");				// ���

	//���õ� ������ ��������
	table = (GoodsInfoTable)request.getAttribute("GoodsInfo");
	String class_str			= table.getOneClass();
	String three_class			= table.getThreeClass();
	String two_class_code		= table.getTwoClassCode();
	String revision_code		= table.getRevisionCode();
	String derive_code			= table.getDeriveCode();

	String model_code			= table.getGoodsCode();
	String model_name			= table.getGoodsName();
	String model_name2			= table.getGoodsName2();
	String short_name			= table.getShortName();
	String color_code			= table.getColorCode();
	String color_name			= table.getColorName();

	//���õ� ��ǰ�� ���ǵ� �����׸񸮽�Ʈ ��������
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SpecList");
	Iterator spec_iter = spec_list.iterator();

	redirect = new GmLinkUrl();
	redirect = (GmLinkUrl)request.getAttribute("Redirect");
	
	String input_hidden = redirect.getInputHidden();
%>

<html>
<link rel="stylesheet" type="text/css" href="../gm/css/style.css">
<head>

<title></title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="f1" method="post" enctype="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gm/images/blet.gif"> ���Ļ��������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../gm/images/bt_reg.gif' value='���' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../gm/images/bt_cancel.gif' value='���' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>


<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">��ǰ�з�</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=class_str%></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">�𵨸�(�ѱ�)</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=model_name%></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">�𵨸�(����)</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=model_name2%></td></tr>
		<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">�𵨾��</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=short_name%></td></tr>
		<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

 <table border=0 width='100%'><tr><td align=left><IMG src='../gm/images/title_model_spec.gif' border='0' alt='�𵨱԰�'></td></tr></table>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>�׸��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>�����Ͱ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>�ۼ���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	String code_str = "";
	int i = 1;
	while(spec_iter.hasNext()){
		spec = (GoodsInfoItemTable)spec_iter.next();
		code_str += spec.getItemCode() + ",";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=i%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=spec.getItemName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getItemValue()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getItemUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getWriteExam()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg'></td>
			</TR>
			<TR><TD colSpan=11 background="../gm/images/dot_line.gif"></TD></TR>
<%		i++;
	}
%>
		</TBODY></TABLE>
	<input type='hidden' name='code_str' value='<%=code_str%>'>
	<input type='hidden' name='spec_str'>
	<input type='hidden' name='mode' value='<%=mode%>'>
	<input type='hidden' name='two_class_code' value='<%=two_class_code%>'>
	<input type='hidden' name='three_class' value='<%=three_class%>'>
	<input type='hidden' name='revision_code' value='<%=revision_code%>'>
	<input type='hidden' name='derive_code' value='<%=derive_code%>'>
	<input type='hidden' name='code' value='<%=model_code%>'>
	<input type='hidden' name='name' value='<%=model_name%>'>
	<input type='hidden' name='name2' value='<%=model_name2%>'>
	<input type='hidden' name='short_name' value='<%=short_name%>'>
	<%=input_hidden%>
</form>
</body>
</html>


<script language=javascript>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function changeClass(){
	var f = document.f1;
	var one_class = f.one_class.value;
	var two_class = f.two_class.value;
	var three_class = f.three_class.value;

	location.href = "../servlet/GoodsInfoServlet?mode=add_model&one_class="+one_class+"&two_class="+two_class+"&three_class="+three_class;
}

// �Է� �� üũ
function checkForm()
{
	var f = document.forms[0];
/*
	if(f.color_code.value == ''){
		alert("�𵨻����ڵ带 ã�Ƽ� �����Ͻʽÿ�.");
		f.color_code.focus();
		return false;
	}

	if(f.color_code.value.length != 2){
		alert("�𵨻����ڵ�� �����빮�� 2�ڸ��� �ԷµǾ�� �մϴ�.");
		f.color_code.focus();
		return false;
	}
*/
	var spec_str = "";
	var code_item = f.code_str.value.split(",");
	for(var i=0; i<code_item.length-1; i++){
/* �ʼ��Է� üũ ����.
		if(eval("f.v_"+code_item[i]+".value;") == ''){
			alert("���� �ڵ� "+code_item[i]+"�� ���� �Է��ϼ���");
			eval("f.v_"+code_item[i]+".focus();");
			return false;
		}
*/
		spec_str += eval("f.v_"+code_item[i]+".value;") + "|" + eval("f.u_"+code_item[i]+".value;") + "!";
	}

	f.spec_str.value = spec_str;

//	f.code.value = f.two_class_code.value + f.short_name.value + f.revision_code.value + f.derive_code.value + "-" + f.color_code.value;
	f.code.value = f.two_class_code.value + f.short_name.value + f.revision_code.value + f.derive_code.value;

	var c = confirm("���� �������� ����Ͻðڽ��ϱ�? \n(���ڵ�:" + f.code.value + ")");
	if(c){
		f.action = "../servlet/GoodsInfoServlet";
		f.submit();
	}
}

var select_obj;
	
function ANB_layerAction(obj,status) { 

	var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
	if(_marginx < 0)
		_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
	else
		_tmpx = event.clientX + document.body.scrollLeft ;
	if(_marginy < 0)
		_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
	else
		_tmpy = event.clientY + document.body.scrollTop ;

	obj.style.posLeft=_tmpx-13;
	obj.style.posTop=_tmpy+20;

	if(status=='visible') {
		if(select_obj) {
			select_obj.style.visibility='hidden';
			select_obj=null;
		}
		select_obj=obj;
	}else{
		select_obj=null;
	}
	obj.style.visibility=status; 
}

function chkAttachFile(){
	var f = document.f1;

	if(f.is_file_same.checked){
		hide('attach_file');
		f.same_part_no.readOnly = false;
	}else{
		show('attach_file');
		f.same_part_no.value = '';
		f.same_part_no.readOnly = true;
	}
}

function searchColor(){
	wopen("../admin/config/minor_code/searchSystemMinorCode.jsp?sf=f1&type=COLOR&div=one&code=color_code&code_name=color_name","search_company",'600','308','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

</script>