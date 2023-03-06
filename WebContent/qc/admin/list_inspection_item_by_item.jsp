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
	String factory_code			= request.getParameter("factory_code");
	String factory_name			= Hanguel.toHanguel(request.getParameter("factory_name"));
	String item_code			= request.getParameter("item_code");

	if(factory_code == null) factory_code = "";
	if(factory_name == null) factory_name = "";
	if(item_code == null) item_code = "";
%>

<html>
<link rel="stylesheet" type="text/css" href="../../css/style.css">
<head>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">

<form name="srForm" method="post" action="list_inspection_item_by_item.jsp">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../images/blet.gif"> 품목별검사항목관리</TD>
					<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=500><IMG src='../images/bt_add.gif' onClick='javascript:item_add();' style='cursor:hand' align='absmiddle'> <IMG src='../images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 검색조건 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">공장</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='factory_code' value='<%=factory_code%>' readOnly> <input type='text' size='20' name='factory_name' value='<%=factory_name%>' readOnly> <a href="javascript:sel_factory();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code' value='<%=item_code%>'> <a href="javascript:searchCMInfo();"><img src="../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<!-- 검사항목리스트 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../images/title_chkitem_list.gif' border='0' alt='검사항목리스트'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=29></TD></TR>
			<TR vAlign=middle height=37>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
	<!--		  <TD noWrap width=40 align=middle class='list_title'>분류<br>코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>분류명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>-->
			  <TD noWrap width=60 align=middle class='list_title'>검사항목<br>코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사항목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>검사<br>기록속성</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>검사방식</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>검사<br>순서</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>중요도</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>합격상한기준</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>합격하한기준</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공장명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>편집</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=29></TD></TR>
<%
	String sql = "SELECT * FROM qc_inspection_item_by_item WHERE factory_code = '" + factory_code + "' AND item_code LIKE '%" + item_code + "%' ORDER BY inspection_order ASC";
	bean.openConnection();
	bean.executeQuery(sql);
	
	int no = 1;
	while(bean.next()){
		String modify_url = "<a href='add_inspection_item_by_item.jsp?mode=modify&mid="+bean.getData("mid")+"'><img src='../images/lt_modify.gif' border='0'></a>";
		String delete_url = "<a href='process_inspection_item_by_item.jsp?mode=delete&mid="+bean.getData("mid")+"&factory_code="+factory_code+"&item_code="+item_code+"&factory_name="+factory_name+"'><img src='../images/lt_del.gif' border='0'></a>";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_class_code")%></td>
				  <TD><IMG height=1 width=1></TD>
<!--				  <TD align=middle class='list_bg'><%=bean.getData("inspection_class_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_code")%></td>
				  <TD><IMG height=1 width=1></TD>-->
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_result_type")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_type_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_order")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("inspection_grade")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("low_standard")==null?"":bean.getData("low_standard")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("low_standard")==null?"":bean.getData("low_standard")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("factory_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("item_code")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=bean.getData("item_desc")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=modify_url%> <%=delete_url%></td>
			</TR><TR><TD colSpan=29 background="../images/dot_line.gif"></TD></tr>
<%		no++;
	}
%>
		</TBODY></TABLE></DIV>

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

//검색
function search() {

	var f = document.srForm;

	var item_code = f.item_code.value;
	var factory_code = f.factory_code.value;

	if(factory_code == ''){
		 alert("공장을 찾아서 선택하십시오.");
		 f.factory_code.focus();
		 return;
	}
/*
	if(item_code == ''){
		 alert("품목을 찾아서 선택하십시오.");
		 f.item_code.focus();
		 return;
	}
*/
	f.submit();
}

//추가
function item_add() {

	var f = document.srForm;

	var factory_code = f.factory_code.value;
	var item_code = f.item_code.value;
	
	location.href = "add_inspection_item_by_item.jsp?mode=add&factory_code=" + factory_code + "&item_code=" + item_code;
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

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 425;
	var div_h = c_h - 272;
	item_list.style.height = div_h;

} 
</script>