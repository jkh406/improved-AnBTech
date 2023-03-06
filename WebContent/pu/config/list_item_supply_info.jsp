<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	ItemSupplyInfoTable itemTable = new ItemSupplyInfoTable();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	ArrayList type_list = new ArrayList();
	type_list = (ArrayList)request.getAttribute("ITEM_SUPPLY_INFO");
	Iterator type_iter = type_list.iterator();

	//링크 문자열 가져오기
	com.anbtech.pu.entity.PurchaseConfigLinkUrl redirect = new com.anbtech.pu.entity.PurchaseConfigLinkUrl();
	redirect = (PurchaseConfigLinkUrl)request.getAttribute("REDIRECT");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = redirect.getLinkWriter();
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../pu/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="display();">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif" align="absmiddle"> 품목공급처관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../pu/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../pu/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../pu/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='PurchaseConfigMgrServlet' name=srForm onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name=searchscope>
						  <option value='item_code'>품목코드</option>
						  <option value='item_desc'>품목명</option>
						  <option value='supplyer_name'>공급업체명</option>
					  </select> 
					  <input type='text' name='searchword' size='10'>
					  <input type="image" onfocus=blur() src="../pu/images/bt_search3.gif" border="0" align="absmiddle">
					  <a href="PurchaseConfigMgrServlet?mode=write_item_supply_info"><img src="../pu/images/bt_reg.gif" border="0" align="absmiddle"></a>
					<%=input_hidden_search%></form>
					  </TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:418; overflow-x:auto; overflow-y:auto;">
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>품목번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>공급업체명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>공급업체코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>공급단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>발주배정가중치</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=60 align=middle class='list_title'>구매L/T</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>주공급처여부</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>거래여부</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	while(type_iter.hasNext()) {
		itemTable = (ItemSupplyInfoTable)type_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><A HREF="javascript:view('<%=itemTable.getMid()%>')"><%=itemTable.getItemCode()%></a></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getItemName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg'><%=itemTable.getItemDesc()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			   <TD align=middle class='list_bg'><%=itemTable.getSupplyerCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:2px'><%=sp.getMoneyFormat(itemTable.getSupplyUnitCost(),"")%>원</td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getOrderWeight()%>%</td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getLeadTime()%>일</td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getIsMainSupplyer()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=itemTable.getIsTradeNow()%></td>
			</TR>
			<TR><TD colSpan=19 background="../pu/images/dot_line.gif"></TD></TR>
<%
	}				
%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

</body>
</html>

<script language='javascript'>

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// 정보보기 화면
function view(mid){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=view_item_supply_info&mid="+mid;
}

// 수정화면
function modify(mid){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=modify_item_supply_info&mid="+mid;
}

// 삭제처리
function del(mid){
	if(confirm("정말 삭제 하시겠습니까?")) {
	location.href="../servlet/PurchaseConfigMgrServlet?mode=delete_item_supply_info&mid="+mid;
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 350;
	var div_h = c_h - 62;
	item_list.style.height = div_h;

}
</script>