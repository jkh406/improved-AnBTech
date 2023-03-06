<%@ include file="../admin/configHead.jsp"%>
<%@ include file="../admin/chk/chkGM01.jsp"%>
<%@ page 
	info		= "모델정보등록관리"
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
	String mode					= request.getParameter("mode");				// 모드

	//선택된 모델정보 가져오기
	table = (GoodsInfoTable)request.getAttribute("GoodsInfo");
	String one_class_list		= table.getOneClass();
	String two_class_list		= table.getTwoClass();
	String two_class_code		= table.getTwoClassCode();
	String three_class_list		= table.getThreeClass();

	String model_code			= table.getGoodsCode();
	String model_name			= table.getGoodsName();
	String model_name2			= table.getGoodsName2();
	String short_name			= table.getShortName();
	String color_code			= table.getColorCode();
	String color_name			= table.getColorName();

	//선택된 제품에 정의된 스펙항목리스트 가져오기
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
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gm/images/blet.gif"> 모델정보등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../gm/images/bt_reg.gif' value='등록' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../gm/images/bt_cancel.gif' value='취소' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>


<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">제품분류</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="one_class" onChange="changeClass()" <% if(mode.equals("mod_model")) out.print("disabled"); %> class="text_01">
						<option value="">제품군 선택</option><%=one_class_list%>
					</select>
					<select name="two_class" onChange="changeClass()" <% if(mode.equals("mod_model")) out.print("disabled"); %> class="text_01">
						<option value="">제품 선택</option><%=two_class_list%>
					</select>
					<select name="three_class" <% if(mode.equals("mod_model")) out.print("disabled"); %> class="text_01">
						<option value="">모델군 선택</option><%=three_class_list%>
					</select></td></tr>

		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델명(한글)</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" size="30" name='name' value='<%=model_name%>' class="text_01" maxlength="15"></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델명(영문)</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" size="30" name='name2' value='<%=model_name2%>' class="text_01" maxlength="15"></td></tr>
		<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../gm/images/bg-01.gif">모델약어</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input type="text" size="3" name='short_name' value='<%=short_name%>' <% if(mode.equals("mod_model")) out.print("readOnly"); %> class="text_01" maxlength="2"></td></tr>
		<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

 <table border=0 width='100%'><tr><td align=left><IMG src='../gm/images/title_model_spec.gif' border='0' alt='모델규격'></td></tr></table>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>데이터값</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>작성예</TD>
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
	<input type='hidden' name='code' value='<%=model_code%>'>
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

// 입력 값 체크
function checkForm()
{
	var f = document.forms[0];

	if(f.one_class.value == ''){
		alert("제품군을 선택하십시오.");
		return false;
	}

	if(f.two_class.value == ''){
		alert("제품을 선택하십시오.");
		return false;
	}

	if(f.three_class.value == ''){
		alert("모델군을 선택하십시오.");
		return false;
	}

	if(f.name.value == ''){
		alert("모델명을 입력하십시오.");
		f.name.focus();
		return false;
	}

	if(f.short_name.value == ''){
		alert("모델약어를 입력하십시오.");
		f.short_name.focus();
		return false;
	}

	if(f.short_name.value.length != 2){
		alert("모델약어는 영문대문자 2자리로 입력되어야 합니다.");
		f.short_name.focus();
		return false;
	}
/*
	if(f.color_code.value == ''){
		alert("모델색상코드를 찾아서 선택하십시오.");
		f.color_code.focus();
		return false;
	}

	if(f.color_code.value.length != 2){
		alert("모델색상코드는 영문대문자 2자리로 입력되어야 합니다.");
		f.color_code.focus();
		return false;
	}
*/
	var spec_str = "";
	var code_item = f.code_str.value.split(",");
	for(var i=0; i<code_item.length-1; i++){
/* 필수입력 체크 안함.
		if(eval("f.v_"+code_item[i]+".value;") == ''){
			alert("스펙 코드 "+code_item[i]+"의 값을 입력하세요");
			eval("f.v_"+code_item[i]+".focus();");
			return false;
		}
*/
		spec_str += eval("f.v_"+code_item[i]+".value;") + "|" + eval("f.u_"+code_item[i]+".value;") + "!";
	}
	if(spec_str == ''){
		alert('선택하신 제품레벨에는 표준규격 템플릿이 정의되어 있지 않습니다.\n적어도 하나 이상의 규격정보를 입력해야 모델등록이 가능합니다. 모듈관리자에게 문의하십시오.');
		return;
	}

	f.spec_str.value = spec_str;

	//신규등록모드일때 모델코드를 생성한다.
	if(f.mode.value == 'add_model'){
//		f.code.value = f.two_class_code.value + f.short_name.value + "100-" + f.color_code.value;
		f.code.value = f.two_class_code.value + f.short_name.value + "100";
	}

	var c = confirm("현재 모델정보로 등록하시겠습니까? \n(모델코드:" + f.code.value + ")");
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