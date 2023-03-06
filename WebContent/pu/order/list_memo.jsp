<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*"
%>
<%!
	OrderInfoTable table;
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ORDER_LIST");
	Iterator table_iter = table_list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../pu/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display();">

<form name="srForm" method="post" action="PurchaseMgrServlet" enctype="multipart/form-data">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			 <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 품목별메모내용보기</TD></TR></TBODY>
		</TABLE></TD></TR>

  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			 	<TD align=left style='padding-left:5px;'>
					<IMG src='../pu/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=25><!--공통정보-->
    <TD vAlign=top>
		<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='order_no'></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' maxlength='10' name='s_date' value=''> <a href="Javascript:OpenCalendar('s_date')"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' maxlength='10' name='e_date' value=''> <a href="Javascript:OpenCalendar('e_date')"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망납기일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' maxlength='10' name='delivery_date' value=''> <a href="Javascript:OpenCalendar('delivery_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <td width="87%" height="25" class="bg_04" colspan='3'><input type='text' size='12' name='supplyer_name'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		</TBODY></TABLE></TD></TR>
  
	<TR><TD height='5px;'></TD></TR>
	<TR><TD align=left><IMG src='../pu/images/title_balju_item.gif' border='0' alt='발주품목'></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		    <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:acroll; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>발주번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>품목코드</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>메모내용</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (OrderInfoTable)table_iter.next();

				String other_info = table.getOtherInfo();
				if (other_info==null) other_info = "";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'><%=no%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderNo()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg' style='padding-left:5px'><%=other_info%></td>
					</TR>
					<TR><TD colSpan=9 background="../cm/images/dot_line.gif"></TD></TR>
<%		no++;
	}
%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</FORM>
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

	var order_no = f.order_no.value;
	var item_code = f.item_code.value;
	var s_date = f.s_date.value;
	var e_date = f.e_date.value;
	var order_date = s_date.substring(0,4) + s_date.substring(5,7) + s_date.substring(8,10) + e_date.substring(0,4) + e_date.substring(5,7) + e_date.substring(8,10);
	var delivery_date = f.delivery_date.value;
	var supplyer_name = f.supplyer_name.value;
//	var process_stat = f.process_stat.value;

	var where_sea = '';
	if(order_no != '') where_sea += "order_no|" + order_no + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(order_date != '') where_sea += "order_date|" + order_date + ",";
	if(delivery_date != '') where_sea += "delivery_date|" + delivery_date + ",";
	if(supplyer_name != '') where_sea += "supplyer_name|" + supplyer_name + ",";
//	if(process_stat != '') where_sea += "process_stat|" + process_stat + ",";

	//alert(where_sea);
	location.href = "PurchaseMgrServlet?mode=list_memo&searchscope=detail&searchword=" + where_sea;
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 462;
	var div_h = c_h - 171;
	item_list.style.height = div_h;

} 

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}
</script>