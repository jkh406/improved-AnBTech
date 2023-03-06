<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = ""
	language = "java"
	contentType = "text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*"
%>
<%!
	RequestInfoTable table;
	com.anbtech.pu.business.PurchaseCodeNameBO cn = new com.anbtech.pu.business.PurchaseCodeNameBO();
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>
<%
	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("REQUEST_LIST");
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
			 <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 발주대상</TD></TR></TBODY>
		</TABLE></TD></TR>

  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			 	<TD align=left style='padding-left:5px;'>
					<IMG src='../pu/images/bt_search3.gif' onClick='javascript:search();' style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_balju_req.gif' onClick='javascript:order_selected();' style='cursor:hand' align='absmiddle' alt="선택품목발주의뢰">
					<IMG src='../pu/images/bt_gyon_req.gif' onClick='javascript:estimate_selected();' style='cursor:hand' align='absmiddle' alt="선택품목견적의뢰"></TD></TR></TBODY>
		</TABLE></TD></TR>
  
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=25><!--공통정보-->
    <TD vAlign=top>
		<TABLE height=25 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청번호</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='request_no' value=''></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목코드</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='item_code' value=''></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청부서</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='requester_div_name'></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청자</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='requester_info'></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청일자</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' maxlength='10' name='s_date' value=''> <a href="Javascript:OpenCalendar('s_date')"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a> ~ <input type='text' size='10' maxlength='10' name='e_date' value=''> <a href="Javascript:OpenCalendar('e_date')"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">희망납기일자</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' maxlength='10' name='delivery_date' value=''> <a href="Javascript:OpenCalendar('delivery_date');"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구분</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='10' name='request_type' value=''> <a href="javascript:sel_request_type()"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">과제코드</TD>
           <TD width="37%" height="25" class="bg_04"><input type='text' size='12' name='project_code' value=''> <a href="javascript:sel_model_code();"><img src="../pu/images/bt_search.gif" border="0" align="absmiddle"></a>
		   <input type='hidden' name='project_name'>
		   </TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
<!--
		<TR>
           <TD width="13%" height="25" class="bg_03" background="../pu/images/bg-01.gif">진행상태</TD>
           <TD width="37%" height="25" class="bg_04" colspan="3">
				<select name="process_stat">
					<option value="S03">발주접수</option>
					<option value="S05">발주등록</option>
					<option value="S06">일부발주</option>
					<option value="S21">입고등록</option>
					<option value="S25">입고완료</option>
					<option value="">모든품목</option>
				</select>
		   </TD></TR>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>-->
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
						  <TD noWrap width=30 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.srForm.checkbox)"></TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>구분</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>구매요청번호</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>과제명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>품목코드</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=120 align=middle class='list_title'>품목명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>요청수량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>요청단위</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>희망입고일</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=150 align=middle class='list_title'>희망발주업체명</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>희망단가</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>기발주량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>기입고량</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>첨부파일</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>요청일자</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>구매요청부서</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../pu/images/list_tep2.gif"></TD>
						  <TD noWrap width=70 align=middle class='list_title'>진행상태</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=35></TD></TR>
<%
				int no = 1;
				while(table_iter.hasNext()){
					table = (RequestInfoTable)table_iter.next();
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'>
	<%//	if(table.getProcessStat().equals("S03") || table.getProcessStat().equals("S06")){	
			//if(Integer.parseInt(table.getRequestQuantity()) > Integer.parseInt(table.getOrderQuantity())){	%>
						<input type="checkbox" name="checkbox" value="<%=table.getRequestNo()%>|<%=table.getItemCode()%>|<%=table.getSupplyerCode()%>">
	<%		//}	%>
					  </TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=no%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestType()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestNo()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getProjectName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getItemCodeLink()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getItemName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestUnit()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getDeliveryDate()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getSupplyerName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=right class='list_bg' style='padding-left:1px'><%=sp.getMoneyFormat(table.getSupplyCost(),"")%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getOrderQuantity()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getDeliveryQuantity()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getFileLink()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequestDate()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getRequesterDivName()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=cn.getStatus(table.getProcessStat())%></TD>
				</TR>
				<TR><TD colSpan=35 background="../pu/images/dot_line.gif"></TD></TR>
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

	var request_no				= f.request_no.value;
	var item_code				= f.item_code.value;
	var requester_div_name		= f.requester_div_name.value;
	var requester_info			= f.requester_info.value;
	var s_date					= f.s_date.value;
	var e_date					= f.e_date.value;
	var request_date			= s_date.substring(0,4) + s_date.substring(5,7) + s_date.substring(8,10) + e_date.substring(0,4) + e_date.substring(5,7) + e_date.substring(8,10);
	var delivery_date			= f.delivery_date.value;
	var request_type			= f.request_type.value;
	var project_code			= f.project_code.value;
//	var process_stat			= f.process_stat.value;

	var where_sea = '';
	if(request_no != '') where_sea += "request_no|" + request_no + ",";
	if(item_code != '') where_sea += "item_code|" + item_code + ",";
	if(requester_div_name != '') where_sea += "requester_div_name|" + requester_div_name + ",";
	if(requester_info != '') where_sea += "requester_info|" + requester_info + ",";
	if(request_date != '') where_sea += "request_date|" + request_date + ",";
	if(delivery_date != '') where_sea += "delivery_date|" + delivery_date + ",";
	if(request_type != '') where_sea += "request_type|" + request_type + ",";
	if(project_code != '') where_sea += "project_code|" + project_code + ",";
//	if(process_stat != '') where_sea += "process_stat|" + process_stat + ",";

	//alert(where_sea);
	location.href = "PurchaseMgrServlet?mode=requested_list&searchscope=detail&searchword=" + where_sea;
}

function order_selected() {
	var f = document.srForm.checkbox;
	var items = "";
	var s_count = 0;
	not_same_supplyer = false;

	// 발주 품목 유무 체크
	if(f[1] == null) {
		alert("발주 품목이 없습니다.");
		return;
	}
	
	//희망공급업체코드가 같은지 체크
	var fromField = f[1].value.split("|");
	var first_supplyer_code = fromField[2];

	for(i=2;i<f.length;i++){
		if(f[i].checked){
			fromField = f[i].value.split("|");
			var supplyer_code = fromField[2];
			if(first_supplyer_code != supplyer_code){
				not_same_supplyer = true;
			}
		}
    }

	if(not_same_supplyer){
		var c = confirm("선택된 품목들의 희망발주업체가 모두 일치하지 않습니다.\n발주등록시에는 선택된 품목들이 한 공급업체로 발주처리됩니다.\n\n그래도 발주의뢰를 계속하시겠습니까?");
		if(!c) return;
	}				
	//여기까지 희망공급업체가 같은지 체크

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
	var url = "../pu/order/input_order_no.jsp?items=" + items;
//	alert(items);
	wopen(url,'order','300','183','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function estimate_selected() {
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
	var url = "../servlet/PurchaseOtherMgrServlet?mode=request_estimate&item_code=" + items;
	wopen(url,'estimate','800','300','scrollbars=no,toolbar=no,status=no,resizable=no');
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

function view_supply_info(item_code){
	var url = "../pu/config/search_item_supply_info.jsp?item_code="+item_code+"&sf=forms[0]";
	wopen(url,'order','800','292','scrollbars=no,toolbar=no,status=no,resizable=no');
}


// 과제찾기
function sel_model_code() {
	//wopen('../servlet/projectPmDocumentServlet?mode=PDT_PL','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
	para = "&target=srForm.project_code/srForm.project_name";	wopen('../servlet/PsmProcessServlet?mode=search_project'+para,'search_pjt','400','228','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pu/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 요청 단위 가져오기
function sel_request_type()
{
	url = "../admin/config/minor_code/searchSystemMinorCode.jsp?sf=srForm&type=REQUEST_TYPE&div=one&code=request_type";
	wopen(url,'add','550','307','scrollbars=yes,toolbar=no,status=no,resizable=no');
} 

//대상자 찾기
function searchUser()
{
	wopen("../pu/searchUser.jsp","user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 

	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 488;
	var div_h = c_h - 197;
	item_list.style.height = div_h;

} 
</script>