<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = ""
	language = "java"
	contentType = "text/html;charset=euc-kr"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*"
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
			 <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 구매입고대상</TD></TR></TBODY>
		</TABLE></TD></TR>

  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			 	<TD align=left style='padding-left:5px;'>
					<IMG src='../pu/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_selitem_input.gif' onClick='javascript:enter_selected();' style='cursor:hand' align='absmiddle' alt="선택품목입고등록">
					<IMG src='../pu/images/bt_selitem_memo.gif' onClick='javascript:memo_selected();' style='cursor:hand' align='absmiddle' alt="선택품목메모입력"></TD></TR></TBODY>
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
<!--
		   <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">진행상태</td>
           <td width="37%" height="25" class="bg_04">
					<select name="process_stat">
					<option value="S13">입고대상품목</option>
					<option value="">모든품목</option>
					</select>		   		   
		   </td>-->
           <td width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <td width="87%" height="25" class="bg_04" colspan='3'><input type='text' size='12' name='supplyer_name'></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		</TBODY></TABLE></TD></TR>
  
	<TR><TD height='5px;'></TD></TR>
	<TR><TD align=left><IMG src='../pu/images/title_balju_item.gif' border='0' alt='발주품목'></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		    <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:173; overflow-x:scroll; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
						 <TD noWrap width=20 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.srForm.checkbox)"></TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>발주번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>품목코드</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>품목명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=250 align=middle class='list_title'>품목설명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>발주수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>발주단위</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>발주일자</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>공급업체</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>기입고수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>입고예정일</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>진행상태</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=25></TD></TR>
<%
			int no = 1;
			while(table_iter.hasNext()){
				table = (OrderInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle height="24" class='list_bg'>
<%						//if(table.getProcessStat().equals("S13")){	
						//if(Integer.parseInt(table.getOrderQuantity()) > Integer.parseInt(table.getDeliveryQuantity())){	%>
							<input type="checkbox" name="checkbox" value="<%=table.getRequestNo()%>|<%=table.getOrderNo()%>|<%=table.getItemCode()%>|<%=table.getSupplyerCode()%>">
<%		//}	%>
						  </td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle height="24" class='list_bg'><%=no%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderNo()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getItemName()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderUnit()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOrderDate()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></td>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></td>
					</TR>
					<TR><TD colSpan=25 background="../cm/images/dot_line.gif"></TD></TR>
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
	location.href = "PurchaseMgrServlet?mode=ordered_list&searchscope=detail&searchword=" + where_sea;
}

function enter_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
	not_same_supplyer = false;
    
	// 발주 품목 유무 체크
	if(f[1] == null) {
		alert("선택한 입고품목이 없습니다.");
		return;
	}

	//공급업체코드가 같은지 체크
	var fromField = f[1].value.split("|");
	var first_supplyer_code = fromField[3];

	for(i=2;i<f.length;i++){
		if(f[i].checked){
			fromField = f[i].value.split("|");
			var supplyer_code = fromField[3];
			if(first_supplyer_code != supplyer_code){
				not_same_supplyer = true;
			}
		}
    }

	if(not_same_supplyer){
		alert("선택된 품목들의 공급업체가 일치하지 않습니다. 동일 공급업체인 품목을 선택하십시오.");
		return;
	}				
	//여기까지 공급업체가 같은지 체크
	
	for(i=1;i<f.length;i++){
		if(f[i].checked){
			items += f[i].value+",";
			s_count ++;
		}
    }
	if(s_count == 0){
	   alert("품목을 선택하십시오.");
	   return;
    }
	var url = "../pu/enter/input_enter_no.jsp?items=" + items + "&supplyer_code=" + first_supplyer_code;
	wopen(url,'enter','300','157','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function memo_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
    for(i=1;i<f.length;i++){
		if(f[i].checked){
			items += f[i].value+",";
			s_count ++;
		}
    }
    if(s_count == 0){
	   alert("품목을 선택하십시오.");
	   return;
    }
	var url = "../pu/order/write_memo.jsp?items=" + items;
	wopen(url,'input_memo','550','250','scrollbars=yes,toolbar=no,status=no,resizable=no');
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

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
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
</script>