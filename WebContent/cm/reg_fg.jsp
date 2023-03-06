<%@ include file="../admin/configHead.jsp"%>
<%@ include file="../admin/chk/chkCM02.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>

<%!
	PartInfoTable part;
	SpecCodeTable spec;
%>

<%
	String mode = request.getParameter("mode");				// ���

	//���õ� ǰ�� ���� ���� ��������
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String code_b_s		= part.getCodeBig();
	String one_class_s	= part.getCodeMid();
	String two_class_s	= part.getCodeSmall();
	String item_no		= part.getItemNo();
	String item_desc	= part.getItemDesc();
	String model_code	= part.getModelCode();

	//���õ� ���ڵ忡 �ش��ϴ� �ڵ帮��Ʈ ��������
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator item_iter = item_list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../cm/css/style.css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
<head>

<script language="Javascript">
function checkForm()
{
	var f = document.forms[0];

	if(f.two_class.value == ''){
		alert("�з��� �ùٷ� �����ϼ���.");
		return false;
	}

	if(f.model_code.value == ''){
		alert("���ڵ带 ã�Ƽ� �����ϼ���.");
		return false;
	}

	f.submit();
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

// ���õ� ���̾ ����
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// ���õ� �����̸� ������
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}
</script>

<title></title>
</head>

<body topmargin="0" leftmargin="0">
<form name="reg_fg" method="post" action="CodeMgrServlet" enctype="multipart/form-data" oncontextmenu="return false">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> ǰ����(FGǰ��)</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../cm/images/bt_save.gif' value='����' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../cm/images/bt_cancel.gif' value='���' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ��з�</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="code_big" onChange="javascript:changeClass();" <% if(mode.equals("modify_fg")) out.print("disabled"); %>>
						<option value="">ǰ�� ����</option><%=code_b_s%>
					</select></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">��ǰ�з�</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="one_class" onChange="javascript:changeClass();" <% if(mode.equals("modify_fg")) out.print("disabled"); %>>
						<option value="">��ǰ�� ����</option><%=one_class_s%>
					</select>
					<select name="two_class" onChange="javascript:changeClass();" <% if(mode.equals("modify_fg")) out.print("disabled"); %>>
						<option value="">��ǰ ����</option><%=two_class_s%>
					</select></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<% if(mode.equals("modify_item")){	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ���ȣ</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text size=30 name='item_no' value='<%=item_no%>' readOnly></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
<%	}	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">���ڵ�</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' size='20' name='model_code' value='<%=model_code%>'  readOnly> <a href="javascript:sel_model_code();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a> <a href="javascript:changeClass();"><img src="../cm/images/bt_code_list.gif" border="0" align="absmiddle" alt='�����Ͻ� �𵨰� ���õ� �ڵ�ä����Ȳ�� ����մϴ�.'></a></td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	   <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">��ϱ���</td>
           <td width="37%" height="25" class="bg_04">
				<input type="radio" value="n" checked name="reg_type">�ű� <input type="radio" value="d" name="reg_type" disabled>�Ļ�</td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
<!--
	   <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">ǰ�񼳸�</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='50' name='item_desc' value='<%=item_desc%>'  onFocus='javascript:make_desc();' readOnly></td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
-->
	   </tbody></table>

<!-- �����ڵ帮��Ʈ -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../cm/images/title_code_list.gif' border='0' alt='�ڵ�ä����Ȳ'></TD></TR></TABLE>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>ǰ�񱸺�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=170 align=middle class='list_title'>�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�����</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int no = 1;
	while(item_iter.hasNext()){
		part = (PartInfoTable)item_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=no%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=part.getItemType()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=part.getItemNo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=part.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=part.getRegisterInfo()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=part.getRegisterDate()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=13 background="../cm/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
%>
		</TBODY></TABLE>
	<input type='hidden' name='mode' value='<%=mode%>'>
	<input type='hidden' name='item_no' value='<%=item_no%>'>
	<input type='hidden' name='model_name'>
	<input type='hidden' name='item_type' value='M'>
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

//�з����� ó��
function changeClass(){ 
	var f = document.forms[0];
	var code_b = f.code_big.value;

	if(code_b == 'F'){ //����ǰ�ڵ� ��Ͻ� �б�
		var one_class = f.one_class.value;
		var two_class = f.two_class.value;
		var model_code = f.model_code.value;
		location.href = "../servlet/CodeMgrServlet?mode=reg_fg&code_big="+code_b+"&one_class="+one_class+"&two_class="+two_class+"&model_code="+model_code;;
	}else if(code_b == '1'){ //ASSY�ڵ� ��Ͻ� �б�
		location.href = "../servlet/CodeMgrServlet?mode=reg_assy&code_big="+code_b;
	}else{ //ǥ��ǰ �� ���ǰ ��Ͻ� �б�
		location.href = "../servlet/CodeMgrServlet?mode=reg_item&code_big="+code_b;
	}
}


function sel_model_code() 
{ 
	var gcode = document.reg_fg.two_class.value
	url = "../gm/SearchModel.jsp?gcode=" + gcode + "&target=reg_fg";
	wopen(url,'add','300','270','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function make_desc() 
{ 
	var f = document.forms[0];

	//���ڵ�
	var model_code = f.model_code.value;
	var model_name = f.model_name.value;

	//��ǰ����
	var one_class_txt;
	var len_one = f.one_class.options.length;
	for(var i=0; i<len_one; i++){
		if(f.one_class.options[i].selected) 
			one_class_txt = f.one_class.options[i].text;
	}

	//��ǰ��
	var two_class_txt;
	var len_two = f.two_class.options.length;
	for(var i=0; i<len_two; i++){
		if(f.two_class.options[i].selected) 
			two_class_txt = f.two_class.options[i].text;
	}
	
	//ǰ�񼳸� = F/G,�𵨸�(���ڵ�),��ǰ����,��ǰ��
	var item_desc = "F/G," + model_code + "," + model_name;

	f.item_desc.value = item_desc;
} 
</script>