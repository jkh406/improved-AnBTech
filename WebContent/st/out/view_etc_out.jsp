<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	EtcInOutInfoTable table;
	StockLinkUrl redirect;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String mode		= request.getParameter("mode");	// 모드
	
	int enableupload	= 5;		// 업로드 개수 지정

	table = (EtcInOutInfoTable)request.getAttribute("INOUT_INFO");
	String inout_no				= table.getInOutNo();
	String supplyer_code		= table.getSupplyerCode();
	String supplyer_name		= table.getSupplyerName();
	String inout_date			= table.getInOutDate();
	String requestor_div_code	= table.getRequestorDivCode();
	String requestor_div_name	= table.getRequestorDivName();
	String requestor_id			= table.getRequestorId();
	String requester_info		= table.getRequestorInfo();
	String inout_type			= table.getInOutType();
	String monetary_unit		= table.getMonetaryUnit();
	String filelink				= table.getFileLink();

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EtcInOutInfoTable();
	Iterator table_iter = item_list.iterator();

	//권한체크
	String prg_priv = sl.privilege;
	int idx1 = prg_priv.indexOf("ST01");
	int idx2 = prg_priv.indexOf("ST02");
%>
<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" oncontextmenu="return false" onLoad="display();">

<form name="reg" method="post" action="StockMgrServlet?upload_folder=etc_inout" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif"> 예외출고상세정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
<%	if (idx2 >= 0){	%>
				<img src='../st/images/bt_modify.gif' onClick="javascript:go_modify('<%=inout_no%>');" style='cursor:hand' align='absmiddle' alt="수정"> <img src='../st/images/bt_del.gif' onClick="javascript:go_delete('<%=inout_no%>');" style='cursor:hand' align='absmiddle' alt="삭제">
<%	}	%>					
				<a href="javascript:history.go(-1);"><img src='../st/images/bt_list.gif' style='cursor:hand' align='absmiddle' alt="목록" border="0"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고번호</td>
           <td width="37%" height="25" class="bg_04"><%=inout_no%></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고일자</td>
           <td width="37%" height="25" class="bg_04"><%=inout_date%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고유형</td>
           <td width="37%" height="25" class="bg_04">
<%
			if(inout_type.equals("AO")) out.print("재고조정출고");
			else if(inout_type.equals("DO")) out.print("개발자재출고");
			else if(inout_type.equals("RO")) out.print("반품출고");
			else if(inout_type.equals("WO")) out.print("폐기");
			else if(inout_type.equals("SO")) out.print("매각");
%>
		   </td>		   		   		   
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고담당자</td>
           <td width="37%" height="25" class="bg_04"><%=requestor_div_name%> <%=requester_info%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=filelink%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<!-- 출고품목 -->
 <TABLE border=0 width='100%'><TR><TD align=left><IMG src='../st/images/title_out_item.gif' border='0' alt='출고품목'></TD></TR></TABLE>
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=60 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>품목코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>출고수량</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>출고단위</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>출고단가</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>출고금액</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (EtcInOutInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemName()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getUnitCost(),"")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(table.getInOutCost(),"")%></td>
			</TR>
			<TR><TD colSpan=13 background="../st/images/dot_line.gif"></TD></TR>
<%
		no++;	
	}
%>
		</TBODY></TABLE></DIV>

<input type='hidden' name='mode' value='<%=mode%>'>
<input type='hidden' name='requester_id' value='<%=requestor_id%>'>
<input type='hidden' name='requester_div_code' value='<%=requestor_div_code%>'>
<input type='hidden' name='monetary_unit' value='<%=monetary_unit%>'>
<input type='hidden' name='supplyer_code' value='<%=supplyer_code%>'>
<input type='hidden' name='supplyer_name' value='<%=supplyer_name%>'>
<input type='hidden' name='in_or_out' value='OUT'>
<input type='hidden' name='item_no' value='<%=no%>'>
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

//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../st/common/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//수정
function go_modify(inout_no) 
{ 
	var c = confirm("출고정보 수정은 제한된 내용(첨부파일 등)에 한해서만 가능합니다.\n출고품목 수량을 조정하고자 할때는 신규입고 또는 출고처리를 이용하십시오.\n\n그래도 출고정보를 수정하시겠습니까?");
	if(c) location.href = "../servlet/StockMgrServlet?mode=update_etc_inout_info&inout_no="+inout_no+"&in_or_out=OUT";
}

//삭제
function go_delete(inout_no) 
{ 
	if(document.reg.item_no.value >= 2){
		alert("한개 이상의 출고품목이 존재할 경우는 삭제할 수 없습니다.");
		return;
	}
	
	var c = confirm("현재 출고정보를 삭제하시겠습니까?");
	if(c) location.href = "../servlet/StockMgrServlet?mode=delete_etc_inout_info&inout_no="+inout_no+"&in_or_out=OUT";
}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

// 숫자만 입력되게
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//	var div_h = h - 460;
	var div_h = c_h - 340;
	item_list.style.height = div_h;

} 
</script>