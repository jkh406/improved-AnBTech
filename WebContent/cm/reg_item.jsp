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
	String mode = request.getParameter("mode");				// 모드
//	String item_type_temp = request.getParameter("item_type")==null?"":request.getParameter("item_type");

	//선택된 품목 대한 정보 가져오기
	part = (PartInfoTable)request.getAttribute("PART_INFO");

	String code_b_s		= part.getCodeBig();
	String code_m_s		= part.getCodeMid();
	String code_s_s		= part.getCodeSmall();
	String item_no		= part.getItemNo();
	String item_name	= part.getItemName();
	String item_type	= part.getItemType();
	String item_desc	= part.getItemDesc();
	String mfg_no		= part.getMfgNo();
	String stock_unit	= part.getStockUnit();
	if(stock_unit == null || stock_unit.equals("")) stock_unit = "EA";
//	if(item_type == null || item_type.equals("")) item_type = item_type_temp;

	//해당분류에 정의된 스펙리스트 가져오기
//	spec = new SpecCodeTable();
	ArrayList spec_list = new ArrayList();
	spec_list = (ArrayList)request.getAttribute("SPEC_LIST");
	Iterator spec_iter = spec_list.iterator();
%>


<%  //첨부파일
	int ref_count		= 5;	// 최대 참조문서 개수 지정
	int enableupload	= 4;	// 업로드 개수 지정
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%
	PartInfoTable file = new PartInfoTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("FILE_LIST");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	while(file_iter.hasNext()){
		file = (PartInfoTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" 삭제! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
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

	if(f.code_big.value == ''){
		alert("대분류를 선택하십시오.");
		return false;
	}

	if(f.code_mid.value == ''){
		alert("중분류를 선택하십시오.");
		return false;
	}

	if(f.code_small.value == ''){
		alert("소분류를 선택하십시오.");
		return false;
	}

	if(f.item_name.value.length < 3){
		alert("품목명을 입력하십시오.");
		f.item_name.focus();
		return false;
	}

	//상세스펙 중 필수입력항목의 체크
	var essence_code = f.essence_code.value.split(",");
	for(var i=0; i<essence_code.length-1; i++){
		if(eval("f.v_"+essence_code[i]+".value;") == ''){
			alert("스펙 코드 "+essence_code[i]+"의 값을 입력하십시오.");
			eval("f.v_"+essence_code[i]+".focus();");
			return false;
		}
	}

	//결과문자열 만들기
	var spec_str = "";
	var code_item = f.code_str.value.split(",");
	for(var i=0; i<code_item.length-1; i++){
		spec_str += code_item[i] + "|" + eval("f.v_"+code_item[i]+".value;") + "|" + eval("f.u_"+code_item[i]+".value;") + ",";
	}
	f.spec_str.value = spec_str;

	//Description 문자열 만들기
/*
	var item_desc = "";
	var desc_code = f.desc_code.value.split(",");
	
	item_desc += eval("f.v_"+code_item[0]+".value;");
	if(eval("f.u_"+code_item[0]+".value;") != 'na') item_desc += "[" + eval("f.u_"+code_item[0]+".value;") + "]";
	for(var i=1; i<desc_code.length-1; i++){
		item_desc += "," + eval("f.v_"+code_item[i]+".value;");
		if(eval("f.u_"+code_item[i]+".value;") != 'na') item_desc += "[" + eval("f.u_"+code_item[i]+".value;") + "]";
	}
*/

	var item_desc = "";
	var desc_code = f.desc_code.value.split(",");
	
	item_desc += eval("f.v_"+desc_code[0]+".value;");
	if(eval("f.u_"+desc_code[0]+".value;") != 'na') item_desc += "[" + eval("f.u_"+desc_code[0]+".value;") + "]";
	for(var i=1; i<desc_code.length-1; i++){
		item_desc += "," + eval("f.v_"+desc_code[i]+".value;");
		if(eval("f.u_"+desc_code[i]+".value;") != 'na') item_desc += "[" + eval("f.u_"+desc_code[i]+".value;") + "]";
	}

    for (i=0;i<f.code_small.length ;i++)
    {
		if(f.code_small.options[i].selected == true)
			item_desc = f.code_small.options[i].text + "," + item_desc;
    }
	
	if(f.stock_unit.value =="") f.stock_unit.value="EA";

	f.item_desc.value = item_desc;

//	alert(spec_str);
	var c = confirm("\"품목설명(item_desc):" + item_desc + "\" 로 생성됩니다. 등록하시겠습니까?");
	if(c){
		document.onmousedown=dbclick;
		f.submit();
	}
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
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
</script>

<title></title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="reg_item" method="post" action="CodeMgrServlet" enctype="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../cm/images/blet.gif"> 품목등록(회로및기구품목)</TD></TR></TBODY>
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
					<select name="code_big" onChange="javascript:changeClass();" <% if(mode.equals("modify_item")) out.print("disabled"); %>>
						<option value="">대분류 선택</option><%=code_b_s%>
					</select>
					<select name="code_mid" onChange="javascript:changeClass();" <% if(mode.equals("modify_item")) out.print("disabled"); %>>
						<option value="">중분류 선택</option><%=code_m_s%>
					</select>
					<select name="code_small" onChange="javascript:changeClass();" <% if(mode.equals("modify_item")) out.print("disabled"); %>>
						<option value="">소분류 선택</option><%=code_s_s%>
					</select></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<% if(mode.equals("modify_item")){	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목번호</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text size=30 name='item_no' value='<%=item_no%>' readOnly></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
<%	}	%>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목명</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text size=40 name='item_name' value='<%=item_name%>'></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목계정</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<input type=text size='2' name='item_type' value='<%=item_type%>'> <a href="javascript:sel_item_type();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">품목단위</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<input type=text size='5' name='stock_unit' value='<%=stock_unit%>' readonly> <a href="javascript:sel_stock_unit();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		<tr bgcolor="c7c7c7"><td height="1" colspan="4"></td></tr>
	    <!-- 화일 첨부 -->
		<tr>
           <td width="13%" height="25" class="bg_03" background="../cm/images/bg-01.gif">문서첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>		   
	   </td></tr>
	   <!---->
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<!-- 품목상제정보 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../cm/images/cm_spec_d.gif' border='0' alt='품목규격' align='absmiddle'> 항목명의 (*)표시는 필수 입력항목임.</TD></TR></TABLE>
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>항목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>항목값</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>작성예</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../cm/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	String code_str = "";		// 정의된 스펙코드들
	String essence_code = "";	// 필수입력으로 정의된 스펙코드들
	String desc_code = "";		// description에 포함될 스펙코드들
	while(spec_iter.hasNext()){
		spec = (SpecCodeTable)spec_iter.next();
		code_str += spec.getSpecCode() + ",";
		if(spec.getIsEssence().equals("y")) essence_code += spec.getSpecCode() + ",";
		if(spec.getIsDesc().equals("y")) desc_code += spec.getSpecCode() + ",";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=spec.getSpecCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecValue()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getSpecUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=spec.getWriteExam()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'></td>
			</TR>
			<TR><TD colSpan=11 background="../cm/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE>

	<input type='hidden' name='mode' value='<%=mode%>'>
	<input type='hidden' name='item_no' value='<%=item_no%>'>
	<input type='hidden' name='code_str' value='<%=code_str%>'>
	<input type='hidden' name='essence_code' value='<%=essence_code%>'>
	<input type='hidden' name='desc_code' value='<%=desc_code%>'>
	<input type='hidden' name='spec_str'>
	<input type='hidden' name='item_desc'>
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

//분류변경 처리
function changeClass(){ 
	var f		= document.forms[0];
	var code_b	= f.code_big.value;
	var code_m	= f.code_mid.value;
	var code_s	= f.code_small.value;


	//완제품코드 등록시 분기
	if(code_b == 'F')
		location.href = "../servlet/CodeMgrServlet?mode=reg_fg&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s;
	//ASSY코드 등록시 분기
	else if(code_b == '1')
		location.href = "../servlet/CodeMgrServlet?mode=reg_assy&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s;
	//표준품 및 사양품 등록시 분기
	else 
		location.href = "../servlet/CodeMgrServlet?mode=reg_item&code_big="+code_b+"&code_mid="+code_m+"&code_small="+code_s;
}


function sel_maker_code() 
{ 
	url = "../cm/searchMakerCode.jsp?target=document.reg_item.mfg_no";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=reg_item&type=ITEM_TYPE&div=one&code=item_type&code_field=품목분류코드&name_field=&minor_code=품목분류명&minor_field=품목분류설명";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

function sel_stock_unit()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=reg_item&type=STOCK_UNIT&div=one&code=stock_unit&code_field=품목분류코드&name_field=&minor_code=항목명&minor_field=항목값";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 


</script>