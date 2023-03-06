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
	String title	= "예외출고정보등록";
	if(mode.equals("update_etc_inout_info")) title = "예외출고정보수정";
	
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

	//리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_LIST");
	table = new EtcInOutInfoTable();
	Iterator table_iter = item_list.iterator();

%>



<SCRIPT language='JavaScript'>
<%
		int i = 1;
		while(i < enableupload){
			if(i == enableupload-1){
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%> size=40 >";
			}
<%			break;
			}
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=40 ><FONT id=id<%=i+1%>></FONT>";
			}
<%			i++;
		}
%>

</SCRIPT>
<%

	String file_stat = "";
	if("update_etc_inout_info".equals(mode)) {
		com.anbtech.st.entity.EtcInOutInfoTable file = new com.anbtech.st.entity.EtcInOutInfoTable();

		ArrayList file_list = new ArrayList();
		file_list = (ArrayList)request.getAttribute("FILE_LIST");
		Iterator file_iter = file_list.iterator();

		i = 1;
		
		while(file_iter.hasNext()){
			file = (EtcInOutInfoTable)file_iter.next();
			file_stat = file_stat + "<INPUT type=file name='attachfile"+i+"' size=40> " + file.getFname()+" 삭제! <INPUT type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
			i++;
			}

	} else {
		i=1;
	}
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
			  <TD valign='middle' class="title"><img src="../st/images/blet.gif"> <%=title%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
				<img src='../st/images/bt_out_item_add.gif' onClick='javascript:save();' style='cursor:hand' align='absmiddle' alt="출고품목입력"> <a href="javascript:history.go(-1);"><img src='../st/images/bt_cancel.gif' style='cursor:hand' align='absmiddle' alt="취소" border="0"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고번호</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='inout_no' value='<%=inout_no%>' readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고일자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='10' name='inout_date' value='<%=inout_date%>' class="text_01" readOnly> <a href="Javascript:OpenCalendar('inout_date');"><img src="../st/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고유형</td>
           <td width="37%" height="25" class="bg_04">
			<select name='inout_type'>
				<option value=''>선택</option>
<!--				<option value='EO'>긴급생산출고</option>-->
				<option value='AO'>재고조정출고</option>
				<option value='DO'>개발자재출고</option>
				<option value='RO'>반품출고</option>
				<option value='WO'>폐기</option>
				<option value='SO'>매각</option>


			</select>
<%	if(!inout_type.equals("")){	%>
		<script language='javascript'>
			document.reg.inout_type.value = '<%=inout_type%>';
		</script>
<%	}	%></td>		   		   		   
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">출고담당자</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size='15' name='requester_div_name' value='<%=requestor_div_name%>' readOnly> <input type='text' size='10' name='requester_info' value='<%=requester_info%>' readOnly></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../st/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%	if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%	if(i < enableupload){
				%>
			    <INPUT type='file' name='attachfile<%=i%>' onClick='fileadd_action<%=i%>()' size='40' > 
			           <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <INPUT type='file' name='attachfile<%=i%>' size='40' >
				            <font id='id<%=i%>'></font>
				<%		}
					}
				%></td></tr>
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

//저장 및 품목추가
function save() 
{ 
	var f = document.reg;

	if(f.inout_date.value == ''){
		alert("출고일자를 선택하십시오.");
		return;
	}

	if(f.inout_type.value == ''){
		alert("출고유형을 선택하십시오.");
		return;
	}

	document.onmousedown=dbclick;
	f.submit();
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