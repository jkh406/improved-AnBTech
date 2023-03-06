<%@ include file="../admin/configHead.jsp"%>
<%@ include file="../admin/chk/chkCM02.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.cm.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%!
	PartInfoTable part;
%>

<%
	String mode = request.getParameter("mode");				// 모드

	//선택된 품목 대한 정보 가져오기
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String code_b_s		= part.getCodeBig();
	String one_class_s	= part.getCodeMid();
	String two_class_s	= part.getCodeSmall();
	String item_no		= part.getItemNo();
	String item_desc	= part.getItemDesc();
	String model_code	= part.getModelCode();

	//선택된 모델코드에 해당하는 코드리스트 가져오기
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

<SCRIPT>
<!--
// 배열생성
arrSelectName = new Array();
arrSelectValue = new Array();

arrSelectName[0] = new Array();
arrSelectName[0][0] = '품목구분';
arrSelectName[0][1] = 'ASSY분류';
arrSelectValue[0] = new Array();
arrSelectValue[0][0] = '';
arrSelectValue[0][1] = '';

// DB연동시에는 여기서부터 루프시작
arrSelectName[1] = new Array();
arrSelectValue[1] = new Array();
arrSelectName[1][0] = '회로ASSY';
arrSelectValue[1][0] = 'E';
arrSelectName[1][1] = 'ASSY분류선택';
arrSelectValue[1][1] = ''
arrSelectName[1][2] = 'ASSY SET';
arrSelectValue[1][2] = 'S';
arrSelectName[1][3] = '일반 ASSY';
arrSelectValue[1][3] = 'G';
arrSelectName[1][4] = 'PCB ASSY';
arrSelectValue[1][4] = 'P';
// 여기까지

arrSelectName[2] = new Array();
arrSelectValue[2] = new Array();
arrSelectName[2][0] = '기구ASSY';
arrSelectValue[2][0] = 'M';
arrSelectName[2][1] = 'ASSY분류선택';
arrSelectValue[2][1] = ''
arrSelectName[2][2] = 'ASSY SET';
arrSelectValue[2][2] = 'S';
arrSelectName[2][3] = '일반 ASSY';
arrSelectValue[2][3] = 'G';

arrSelectName[3] = new Array();
arrSelectValue[3] = new Array();
arrSelectName[3][0] = '케이블ASSY';
arrSelectValue[3][0] = 'C';
arrSelectName[3][1] = 'ASSY분류선택';
arrSelectValue[3][1] = ''
arrSelectName[3][2] = 'ASSY SET';
arrSelectValue[3][2] = 'S';
arrSelectName[3][3] = '일반 ASSY';
arrSelectValue[3][3] = 'G';

arrSelectName[4] = new Array();
arrSelectValue[4] = new Array();
arrSelectName[4][0] = '팬텀ASSY';
arrSelectValue[4][0] = 'PH';
arrSelectName[4][1] = '선택사항없음';
arrSelectValue[4][1] = ''

//-->
</script>

<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onload="document_onload();">
<form name="reg_assy" method="post" action="CodeMgrServlet" enctype="multipart/form-data" oncontextmenu="return false">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> 품목등록(ASSY품목)</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../cm/images/bt_save.gif' value='저장' onClick='javascript:checkForm();' style='cursor:hand' align='absmiddle'>
					<IMG src='../cm/images/bt_cancel.gif' value='취소' onClick='javascript:history.go(-1);' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목분류</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="code_big" onChange="javascript:changeClass();" <% if(mode.equals("modify_assy")) out.print("disabled"); %>>
						<option value="">품목분류선택</option><%=code_b_s%>
					</select></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">제품분류</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
					<select name="one_class" onChange="javascript:changeClass();" <% if(mode.equals("modify_assy")) out.print("disabled"); %>>
						<option value="">제품군선택</option><%=one_class_s%>
					</select>
					<select name="two_class" onChange="javascript:changeClass();" <% if(mode.equals("modify_assy")) out.print("disabled"); %>>
						<option value="">제품선택</option><%=two_class_s%>
					</select></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<% if(mode.equals("modify_assy")){	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목번호</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text size=30 name='item_no' value='<%=item_no%>' readOnly></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
<%	}	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">모델코드</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' size='20' name='model_code' value='<%=model_code%>' class="text_01" readOnly> <a href="javascript:sel_model_code();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a> <a href="javascript:changeClass();"><img src="../cm/images/bt_code_list.gif" border="0" align="absmiddle" alt='선택하신 모델과 관련된 코드채번현황을 출력합니다.'></a></td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	   <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목구분</td>
           <td width="37%" height="25" class="bg_04">
				<select name="item_type" onchange="changeItemType();" style="width:150px">
					<option value="" selected>자료전송중</option>
				</select>
				<select name="assy_type" style="width:150px" onChange="javascript:sel_assy_type();">
					<option value="" selected>자료전송중</option>
				</select></td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	   <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">공정선택</td>
           <td width="37%" height="25" class="bg_04">
				<div id="type_s" class="expanded">
				<table cellSpacing="2" cellPadding="0" width="350" border="0" bordercolordark="white" bordercolorlight="#9CA9BA">
					<tr>
						<td align="left" width='100%' style='padding-left:5px'>선택사항 없음.</td></tr>
				</table></div>
				<div id="type_m" class="collapsed">
				<table cellSpacing="2" cellPadding="0" width="350" border="0" bordercolordark="white" bordercolorlight="#9CA9BA">
					<tr>
						<td align="middle"><input type='checkbox' name='op_code' value='M0'></td>
						<td align="left" width='100%' style='padding-left:5px'>섀시</td></tr>
					<tr>
						<td align="middle"><input type='checkbox' name='op_code' value='M1'></td>
						<td align="left" width='100%' style='padding-left:5px'>도장</td></tr>
				</table></div>
				<div id="type_e" class="collapsed">
				<table cellSpacing="2" cellPadding="0" width="350" border="0" bordercolordark="white" bordercolorlight="#9CA9BA">
<%
	bean.openConnection();
	String sql = "SELECT * FROM mbom_env WHERE flag = '1'";
	bean.executeQuery(sql);
	while(bean.next()){
%>
					<tr>
						<td align="middle"><input type='checkbox' name='op_code' value='<%=bean.getData("m_code")%>'></td>
						<td align="left" width='100%' style='padding-left:5px'><%=bean.getData("tag")%> (<%=bean.getData("spec")%>)</td></tr>
<%	}	%>
				</table></div>
		   </td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	   <tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">형상명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='config_name' maxlength="10" class="text_01"></td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
	   <tr><td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">Assy구분</td>
	    <td width="37%" height="25" class="bg_04">
			<input type='radio' name='where_assy' value='1' checked>사내조립&nbsp;
			<input type='radio' name='where_assy' value='2'>외주조립
	   </td></tr>
	   <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>	
		</tbody></table>

<!-- 관련코드리스트 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../cm/images/title_code_list.gif' border='0' alt='코드채번현황'></TD></TR></TABLE>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>품목구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=170 align=middle class='list_title'>등록자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>등록일</TD>
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
	<input type='hidden' name='op_codes'>
</form>
</body>
</html>

<script language="javascript">
<!--
	var IE = (navigator.userAgent.indexOf('MSIE') != -1 || navigator.userAgent == "");
	if (IE) {
		document.write("<link type=\"text/css\" href=\"../asset/microsoft.css\" rel=\"stylesheet\" rev=\"stylesheet\">");
	} else {
		document.write("<link type=\"text/css\" href=\"../asset/netscape.css\" rel=\"stylesheet\" rev=\"stylesheet\">");
	}

	function document_onload(y) { // 폼에 배열 넣기
		var strValue, optSale;
	
			for(i=document.reg_assy.item_type.length; i>-1; i--) document.reg_assy.item_type.options[i] = null;
			for(i=document.reg_assy.assy_type.length; i>-1; i--) document.reg_assy.assy_type.options[i] = null;
			if( arrSelectName.length > 0 ){
				for( i=0; i< arrSelectName.length; i++){
					optSale = new Option(arrSelectName[i][0],arrSelectValue[i][0]);
					document.reg_assy.item_type.options[document.reg_assy.item_type.length] = optSale;
				}
				document.reg_assy.item_type.selectedIndex = 0;
				changeItemType();
				document.reg_assy.assy_type.selectedIndex = 0;
			}
	
	}
	function changeItemType() {	// 폼값 바꾸기
		var idxSale, strValue,optFund;
					
		for(i= document.reg_assy.assy_type.length; i>-1;i--) document.reg_assy.assy_type.options[i] = null;

		idxSale = document.reg_assy.item_type.selectedIndex;

		for(i= 1;i< arrSelectName[idxSale].length;i++){
			strValue = arrSelectName[idxSale][i];
			optFund = new Option( strValue, arrSelectValue[idxSale][i]);
			document.reg_assy.assy_type.options[document.reg_assy.assy_type.length] = optFund;
		}
		document.reg_assy.assy_type.selectedIndex = 0;

		show('type_s');
		hide('type_m');
		hide('type_e');
		unselect_all();		
	}

//-->
</script>

<script language=javascript>
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//분류변경 처리
function changeClass(){ 
	var f = document.forms[0];
	var code_b = f.code_big.value;
		
	if(code_b == 'F'){ //완제품코드 등록시 분기
		var one_class = f.one_class.value;
		var two_class = f.two_class.value;
		location.href = "../servlet/CodeMgrServlet?mode=reg_fg&code_big="+code_b+"&one_class="+one_class+"&two_class="+two_class;
	}else if(code_b == '1'){ //ASSY코드 등록시 분기
		var one_class = f.one_class.value;
		var two_class = f.two_class.value;
		var model_code = f.model_code.value;
		location.href = "../servlet/CodeMgrServlet?mode=reg_assy&code_big="+code_b+"&one_class="+one_class+"&two_class="+two_class+"&model_code="+model_code;
	}else{ //표준품 및 사양품 등록시 분기
		location.href = "../servlet/CodeMgrServlet?mode=reg_item&code_big="+code_b;
	}
}


function sel_model_code() 
{ 
	var gcode = document.reg_assy.two_class.value
	url = "../gm/SearchModel.jsp?gcode=" + gcode + "&target=reg_assy";
	wopen(url,'add','300','270','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function make_desc() 
{ 
	var f = document.forms[0];

	//모델코드
	var model_code = f.model_code.value;
	var model_name = f.model_name.value;

	//제품군명
	var one_class_txt;
	var len_one = f.one_class.options.length;
	for(var i=0; i<len_one; i++){
		if(f.one_class.options[i].selected) 
			one_class_txt = f.one_class.options[i].text;
	}

	//제품명
	var two_class_txt;
	var len_two = f.two_class.options.length;
	for(var i=0; i<len_two; i++){
		if(f.two_class.options[i].selected) 
			two_class_txt = f.two_class.options[i].text;
	}
	
	//품목설명 = F/G,모델명(모델코드),제품군명,제품명
	var item_desc = "F/G," + model_name + "(" + model_code + ")" + "," + two_class_txt;

	f.item_desc.value = item_desc;
}

function sel_assy_type() 
{ 
	var f = document.forms[0];
	var item_type = f.item_type.value;
	var assy_type = f.assy_type.value;

	if(item_type == 'M' && assy_type == 'G'){
		hide('type_s');
		show('type_m');
		hide('type_e');	
		unselect_all();
	}else if(item_type == 'E' && assy_type == 'P'){
		hide('type_s');
		hide('type_m');
		show('type_e');
		unselect_all();
	}else{
		show('type_s');
		hide('type_m');
		hide('type_e');
		unselect_all();
	}
}

// 선택된 레이어를 숨김
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// 선택된 레이이를 보여줌
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}

function checkForm()
{
	var f = document.forms[0];
	if(f.two_class.value == ''){
		alert("분류를 올바로 선택하십시오.");
		return;
	}

	if(f.model_code.value == ''){
		alert("모델코드를 찾아서 선택하십시오.");
		return;
	}

	if(f.item_type.value == ''){
		alert("품목구분을 선택하십시오.");
		return;
	}

	var item_type = f.item_type.value;
	if(item_type != 'PH' && f.assy_type.value == ''){
		alert("ASSY분류을 선택하십시오.");
		return;
	}

	if(item_type != 'PH' && f.assy_type.value == ''){
		alert("ASSY분류을 선택하십시오.");
		return;
	}

	if(f.config_name.value == ''){
		alert("관련형상명을 입력하십시오.");
		f.config_name.focus();
		return;
	}

	var op_codes = "";
	var s_count = 0;
    for(i=0;i<f.op_code.length;i++){
		if(f.op_code[i].checked){
			op_codes += f.op_code[i].value+",";
			s_count ++;
		}
    }

	if(s_count == 0){
		op_codes = "00,";		
		//alert("등록할 공정코드를 선택하세요.");
		//return;
    }

	
	f.op_codes.value = op_codes;
//	alert(f.op_codes.value);
	f.submit();
}

function unselect_all()
{
	var f = document.forms[0];

    for(i=0;i<f.op_code.length;i++){
		f.op_code[i].checked = false;
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
</script>