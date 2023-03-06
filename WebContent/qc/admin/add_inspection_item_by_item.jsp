<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query			= "";
	String mode				= request.getParameter("mode");
	String mid				= request.getParameter("mid");
	String factory_code		= request.getParameter("factory_code");
	String item_code		= request.getParameter("item_code");
	String inspection_code	= request.getParameter("inspection_code")==null?"":request.getParameter("inspection_code");

	String factory_name				= "";
	String item_name				= "";
	String item_desc				= "";
	String inspection_class_code	= "";
	String inspection_class_name	= "";
	String inspection_name			= "";
	String inspection_result_type	= "";
	String inspection_order			= "";
	String inspection_type_code		= "";
	String inspection_type_name		= "";
	String inspection_grade			= "";
	String low_standard				= "";
	String upper_standard			= "";

    bean.openConnection();	

	//수정일 경우
	if(mode.equals("modify")){
		query = "SELECT * FROM qc_inspection_item_by_item WHERE mid = '" + mid + "'";
		bean.executeQuery(query);
		while(bean.next()){
			factory_code			= bean.getData("factory_code");			
			factory_name			= bean.getData("factory_name");			
			item_code				= bean.getData("item_code");
			item_name				= bean.getData("item_name");
			item_desc				= bean.getData("item_desc");
			inspection_class_code	= bean.getData("inspection_class_code");
			inspection_class_name	= bean.getData("inspection_class_name");
			inspection_code			= bean.getData("inspection_code");
			inspection_name			= bean.getData("inspection_name");
			inspection_result_type	= bean.getData("inspection_result_type");
			inspection_order		= bean.getData("inspection_order");
			inspection_type_code	= bean.getData("inspection_type_code");
			inspection_type_name	= bean.getData("inspection_type_name");
			inspection_grade		= bean.getData("inspection_grade");
			low_standard			= bean.getData("low_standard");
			upper_standard			= bean.getData("upper_standard");
		}
	}
	//기 등록된 품목에 검사항목 추가시
	//factory_info_table A, item_master B
	else if(mode.equals("add") && !factory_code.equals("") && !item_code.equals("")){
		query = "SELECT A.factory_name,B.item_name,B.item_desc FROM factory_info_table A, item_master B WHERE A.factory_code = '" + factory_code + "' AND B.item_no = '" + item_code + "' ";
		bean.executeQuery(query);
		while(bean.next()){
			factory_name			= bean.getData("factory_name");			
			item_name				= bean.getData("item_name");
			item_desc				= bean.getData("item_desc");
		}
	}
%>

<HTML>
<head>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<title></title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 품목검사항목등록</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<a href="javascript:checkForm()"><IMG src='../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
				<IMG src='../images/bt_cancel.gif' onclick='history.go(-1)' align='absmiddle' border='0' style='cursor:hand'>
				</TD></TR></TBODY>
		</TABLE></TD></TR></TBODY></TABLE>

<form name="writeForm" method="post" action="process_inspection_item_by_item.jsp" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">공장명</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='5' name='factory_code' value='<%=factory_code%>' readOnly class="text_01"> <input type='text' size='20' name='factory_name' value='<%=factory_name%>'  readOnly class="text_01"> <a href="javascript:sel_factory();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">품목코드</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='10' name='item_code' value='<%=item_code%>' readOnly class="text_01"> <a href="javascript:searchCMInfo();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">품목명</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='20' name='item_name' value='<%=item_name%>' readOnly class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">품목설명</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='35' name='item_desc' value='<%=item_desc%>' readOnly class="text_01"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR><TABLE><BR>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사분류</TD>
    <TD width="35%" height="25" class="bg_04" >
				<SELECT name='inspection_class_code' onChange="change_class_code();">
<%
	//항목분류코드 리스트
	query = "SELECT DISTINCT inspection_class_code,inspection_class_name FROM qc_inspection_item";
	bean.executeQuery(query);

	while(bean.next()) {
%>
						<option value='<%=bean.getData("inspection_class_code")%>'><%=bean.getData("inspection_class_name")%></option>
<%	}	%>
				<% if(!inspection_class_code.equals("")) {%>
					<script>
						document.writeForm.inspection_class_code.value = '<%=inspection_class_code%>';
					</script>
				<% }%>
			  </SELECT>	
	</TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사항목명</TD>
    <TD width="35%" height="25" class="bg_04" >
		<input type='text' size='5' name='inspection_code' value='<%=inspection_code%>' readOnly class="text_01"> <input type='text' size='20' name='inspection_name' value='<%=inspection_name%>'  readOnly class="text_01"> <a href="javascript:sel_item();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a>	
	</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사기록속성</TD>
    <TD width="35%" height="25" class="bg_04" >
				<SELECT name='inspection_result_type'>
					<option value='정량'>정량</option>
					<option value='정성'>정성</option>

				<% if(!inspection_result_type.equals("")) {%>
					<script>
						document.writeForm.inspection_result_type.value = '<%=inspection_result_type%>';
					</script>
				<% }%>
			  </SELECT>		
	</TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사방식명</TD>
    <TD width="35%" height="25" class="bg_04" >
				<SELECT name='inspection_type_name'>
					<option value='전수검사'>전수검사</option>
					<option value='계수샘플링검사'>계수샘플링검사</option>
					<option value='계량형샘플링검사'>계량형샘플링검사</option>
					<option value='규준형플링검사'>규준형플링검사</option>
					<option value='선별형플링검사'>선별형플링검사</option>
					<option value='조정형플링검사'>조정형플링검사</option>
				<% if(!inspection_type_name.equals("")) {%>
					<script>
						document.writeForm.inspection_type_name.value = '<%=inspection_type_name%>';
					</script>
				<% }%>	
	</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">검사순서</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='2' name='inspection_order' value='<%=inspection_order%>' maxlength="2" class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">중요도</TD>
    <TD width="35%" height="25" class="bg_04" >
				<SELECT name='inspection_grade'>
					<option value='상'>상</option>
					<option value='중'>중</option>
					<option value='하'>하</option>
				</SELECT>
				<% if(!inspection_grade.equals("")) {%>
					<script>
						document.writeForm.inspection_grade.value = '<%=inspection_grade%>';
					</script>
				<% }%>		
	</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">합격하한기준</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='20' name='low_standard' value='<%=low_standard%>'></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">합격상한기준</TD>
    <TD width="35%" height="25" class="bg_04" ><input type='text' size='20' name='upper_standard' value='<%=upper_standard%>'></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>
<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='mid' value='<%=mid%>'>
<input type='hidden' name='inspection_class_name' value=''>
</form>
</BODY>
</HTML>


<script>
<!--
function checkForm()
{
	var f = document.writeForm;

	if(f.factory_code.value == ""){
			alert("공장을 찾아서 선택하십시오.");
			return;
	}
	if(f.item_code.value == ""){
			alert("품목을 찾아서 선택하십시오.");
			return;
	}
	if(f.inspection_code.value == ""){
			alert("검사항목을 찾아서 선택하십시오.");
			return;
	}
	if(f.inspection_order.value == ""){
			alert("검사순서를 입력하십시오.(2자리 숫자)");
			f.inspection_order.focus();
			return;
	}

	var len = f.inspection_class_code.options.length;
	for(var i=0; i<len; i++){
		if(f.inspection_class_code.options[i].selected)
			f.inspection_class_name.value = f.inspection_class_code.options[i].text;
	}
	f.submit();
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 공장 선택
function sel_factory(){
	url = "../../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//품목계정 선택
function sel_item_type()
{
	url = "../../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=ITEM_TYPE&div=one&code=item_type&code_name=item_type_name";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

// 품목 정보 가져오기
function searchCMInfo(){
	var strUrl = "../../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//검사항모코드 찾기
function sel_item()
{
	var f = document.writeForm;
	var inspection_class_code = f.inspection_class_code.value;
	var url = "search_inspection_code.jsp?class_code=" + inspection_class_code;
	wopen(url,"search_inspection_code",'500','425','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//분류코드변경
function change_class_code()
{
	var f = document.writeForm;
	f.inspection_code.value='';
	f.inspection_name.value='';
}
-->
</script>