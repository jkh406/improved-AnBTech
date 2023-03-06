<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	InOutInfoTable table;
	StockLinkUrl redirect;
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("INOUT_LIST");
	Iterator table_iter = table_list.iterator();

	//링크문자열 가져오기
	redirect = new com.anbtech.st.entity.StockLinkUrl();
	redirect = (StockLinkUrl)request.getAttribute("REDIRECT");

	String view_pagecut = redirect.getViewPagecut();
//	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String input_hidden = redirect.getInputHidden();
%>

<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false" onLoad="display();">

<form name="srForm" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../st/images/blet.gif"> 재고수불현황</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><IMG src="../st/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <IMG src="../st/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <IMG src="../st/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=500>
					<IMG src='../st/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
			  </TD><TD width='100%' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 품목공통정보 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">품목계정</td>
           <td width="37%" height="25" class="bg_04"><input type=text size=3 name='item_type'> <input type=text size=15 name='item_type_name'> <a href="javascript:sel_item_type();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='item_code'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">수불일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' maxlength='10' name='s_date' value=''> <a href="Javascript:OpenCalendar('s_date')"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' maxlength='10' name='e_date' value=''> <a href="Javascript:OpenCalendar('e_date')"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">공장명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='factory_code'> <input type='text' size='20' name='factory_name'> <a href="javascript:sel_factory();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">수불유형</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='3' name='inout_type_code'> <input type='text' size='15' name='inout_type_name'> <a href="javascript:sel_conf_type();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif"></td>
           <td width="37%" height="25" class="bg_04"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<!-- 품목상제정보 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../st/images/title_inout.gif' border='0' alt='수불현황'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>수불유형</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>수불일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>수불공장</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (InOutInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getInOutTypeName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:10px'><%=table.getItemDesc()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getInOutDate()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>
			</TR>
			<TR><TD colSpan=17 background="../st/images/dot_line.gif"></TD></TR>
<%	
		no++;
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

function search() {

	var f = document.srForm;

	var item_type = f.item_type.value;
	var item_code = f.item_code.value;
	var s_date = f.s_date.value;
	var e_date = f.e_date.value;
	var inout_date = s_date.substring(0,4) + s_date.substring(5,7) + s_date.substring(8,10) + e_date.substring(0,4) + e_date.substring(5,7) + e_date.substring(8,10);
	var factory_name = f.factory_name.value;
	var inout_type = f.inout_type_code.value;

	var where_sea = '';
	if(item_type != '') where_sea += "item_type|" + item_type + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(inout_date != '') where_sea += "inout_date|" + inout_date + ",";
	if(factory_name != '') where_sea += "factory_name|" + factory_name + ",";
	if(inout_type != '') where_sea += "inout_type|" + inout_type + ",";

	//alert(where_sea);
	location.href = "StockMgrServlet?mode=list_inout&searchscope=detail&searchword=" + where_sea;
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 공장 선택
function sel_factory(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'SEL_FACTORY','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 수불유형 선택
function sel_conf_type(){
	url = "../st/config/search_conf_type.jsp?code=inout_type_code&name=inout_type_name";
	wopen(url,'SEL_CONF_TYPE','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//품목계정 선택
function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=ITEM_TYPE&div=one&code=item_type&code_name=item_type_name&type=분류코드&type_name=구분명&code=분류명&code_name=분류설명";
	wopen(url,'SEL_ITEM_TYPE','550','303','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 470;
	var div_h = c_h - 47;
	item_list.style.height = div_h;

} 
</script>