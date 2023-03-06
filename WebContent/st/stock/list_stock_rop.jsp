<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	StockInfoTable table;
	StockLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("STOCK_LIST");
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

<form name="srForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../st/images/blet.gif"> ROP대상품목</TD>
					<TD style="padding-right:10px" align='right' valign='middle'><IMG src="../st/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <IMG src="../st/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <IMG src="../st/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=600>
					<IMG src='../st/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>&nbsp;<IMG src='../st/images/bt_select_item_req.gif' onClick='javascript:order_selected();' style='cursor:hand' align='absmiddle' alt="선택품목구매의뢰">
			  </TD><TD width='100%' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 검색조건 -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">공장명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='5' name='factory_code' readOnly> <input type='text' size='20' name='factory_name'> <a href="javascript:sel_factory();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='item_code'> <a href="javascript:searchCMInfo();"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">평균단가</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='unit_cost'>원 이상</td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">Lead Time</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='3' name='lead_time'>일 이상</td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">품목계정</td>
           <td width="37%" height="25" class="bg_04"><input type=text size=5 name='item_type'> <input type=text size=20 name='item_type_name'> <a href="javascript:sel_item_type();"><img src="../cm/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif"></td>
           <td width="37%" height="25" class="bg_04"></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table><br>

<!-- 재고현황 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../st/images/title_stock.gif' border='0' alt='재고현황'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.srForm.checkbox)"></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공장명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>재고단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>재고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>적정재고량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=70 align=middle class='list_title'>부족재고량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>평균단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>Lead Time</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (StockInfoTable)table_iter.next();
		int stock_quantity = Integer.parseInt(table.getStockQuantity());
		int resonable_quantity = Integer.parseInt(table.getResonableQuantity());
		int lack_quantity = resonable_quantity - stock_quantity;
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'>
					<input type="checkbox" name="checkbox" value="<%=table.getFactoryCode()%>|<%=table.getFactoryName()%>|<%=table.getItemCode()%>|<%=lack_quantity%>">
				  </TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' class='padding-left:5px'><%=table.getItemDesc()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getStockUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getStockQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getResonableQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><font color='red'><%=lack_quantity%></font></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCostA(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getLeadTime()%></td>
			</TR>
			<TR><TD colSpan=21 background="../st/images/dot_line.gif"></TD></TR>
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

	var item_code = f.item_code.value;
	var factory_code = f.factory_code.value;
	var unit_cost = f.unit_cost.value;
	var lead_time = f.lead_time.value;
	var item_type = f.item_type.value;

	var where_sea = '';
	if(factory_code != '') where_sea += "factory_code|" + factory_code + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(unit_cost != '') where_sea += "unit_cost|" + unit_cost + ",";
	if(lead_time != '') where_sea += "lead_time|" + lead_time + ",";
	if(item_type != '') where_sea += "item_type|" + item_type + ",";

	//alert(where_sea);
	location.href = "StockMgrServlet?mode=list_stock_rop&searchscope=detail&searchword=" + where_sea;
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 공장 선택
function sel_factory(){
	url = "../st/config/searchFactoryInfo.jsp?tablename=factory_info_table&field=factory_code/factory_name";
	wopen(url,'enterCode','400','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 품목 정보 가져오기
function searchCMInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=item_code&item_name=item_name&item_type=item_type&item_desc=item_desc&item_unit=request_unit";
	wopen(strUrl,"SEARCH_ITEM",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=ITEM_TYPE&div=one&code=item_type&code_name=item_type_name";
	wopen(url,'add','550','307','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function order_selected() {
	var f = document.srForm.checkbox;

	var sel_factory_code = "";
	var sel_factory_name = "";
	for(i=1;i<f.length;i++){
		if(f[i].checked){
			var fromField		= f[i].value.split("|");
			sel_factory_code	= fromField[0];
			sel_factory_name	= fromField[1];
		}
    }
	
	var items = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			var fromField		= f[i].value.split("|");
			var factory_code	= fromField[0];
			var factory_name	= fromField[1];
			var item_code		= fromField[2];
			var quantity		= fromField[3];

			if(sel_factory_code != factory_code){
				alert("같은 공장내의 품목만 구매요청 가능합니다.");
				return;
			}

			items += item_code + "|" + quantity +",";
			s_count ++;
		}
    }

    if(s_count == 0){
	   alert("품목을 선택하십시오.");
	   return;
    }

	var url = "StockMgrServlet?mode=request_rop_order&factory_code=" + sel_factory_code + "&factory_name=" + sel_factory_name + "&item_code=" + items;
	wopen(url,'order','300','183','scrollbars=no,toolbar=no,status=no,resizable=no');
//	alert(items);
}

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
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

//품목계정 선택
function sel_item_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=ITEM_TYPE&div=one&code=item_type&code_name=item_type_name";
	wopen(url,'add','550','307','scrollbars=no,toolbar=no,status=no,resizable=no');
} 

</script>