<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "공정별 생산실적등록 관리하기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧

	String mfg_count = (String)request.getAttribute("mfg_count"); 
	if(mfg_count == null) mfg_count = "0";

	//--------------------------------------
	//생산실적등록 마스터 데이터 가져오기
	//--------------------------------------
	com.anbtech.mm.entity.mfgProductMasterTable master = new com.anbtech.mm.entity.mfgProductMasterTable();
	master = (mfgProductMasterTable)request.getAttribute("PRODUCT_master");
	String gid = master.getGid();
	String mfg_no = master.getMfgNo();
	String item_code = master.getItemCode();
	String item_spec = master.getItemSpec();
	String item_name = master.getItemName();

	String order_count = Integer.toString(master.getOrderCount());
	if(order_count.equals("0")) order_count = mfg_count;			//최초 초기화 할때 필요

	String total_count = Integer.toString(master.getTotalCount());
	String good_count = Integer.toString(master.getGoodCount());
	String bad_count = Integer.toString(master.getBadCount());
	String output_status = master.getOutputStatus();
	String factory_no = master.getFactoryNo();
	String rst_total_count = nfm.toDigits(master.getTotalCount()) + "/" + nfm.toDigits(Integer.parseInt(order_count))+" EA";

	//----------------------------------------------------
	//  생산실적입력가능한 최대수량
	//----------------------------------------------------
	int check_count = Integer.parseInt(order_count) - master.getTotalCount();		

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	com.anbtech.mm.entity.mfgProductItemTable item;
	item = (mfgProductItemTable)request.getAttribute("PRODUCT_item");

	String pid = item.getPid();
	total_count = Integer.toString(item.getTotalCount());
	good_count = Integer.toString(item.getGoodCount());
	bad_count = Integer.toString(item.getBadCount());
	String bad_type = item.getBadType();
	String bad_note = item.getBadNote();
	String output_date = item.getOutputDate();		//등록일

	String todate = anbdt.getDate();		//금일(일일마감을 위해)

	
%>

<HTML>
<HEAD><TITLE>공정별 생산실적등록 관리하기</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="sForm" method="post" style="margin:0">
<!-- TITLE -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TBODY>
		<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'> 생산실적등록</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
		<TR><TD align=left width='100%' height='25' bgcolor='' style='padding-left:5px;'><!--버튼-->
	<%
		if(pid.length() == 0) {		//신규 
	%>
			<a href="javascript:sendSave();"><img src="../mm/images/bt_add.gif" border=0></a>
	<% } else {						//수정 
	%>
			<a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" border=0></a>
			<a href="javascript:sendView();"><img src="../mm/images/bt_new.gif" border=0></a>
	<% } %>
			</TD></TR>
	
		<!--내용-->
		<TR><TD>
			<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
				<TBODY>
					<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">실적수량</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="total_count" value="<%=total_count%>" size="10"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;누적생산량 : <%=rst_total_count%></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">양품수량</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="good_count" value="<%=good_count%>" size="10"></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">불량수량</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="bad_count" value="<%=bad_count%>" size="10"></TD>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">불량형태</TD>
						<TD width="37%" height="25" class="bg_04">
							<INPUT type="text" name="bad_type" value="<%=bad_type%>" size="20"></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">불량원인</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
							<TEXTAREA NAME="bad_note" rows='3' cols='70'><%=bad_note%></TEXTAREA></TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=pid%>">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="assy_code" size="15" value="">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="item_name" size="15" value="<%=item_name%>">
<INPUT type="hidden" name="item_spec" size="15" value="<%=item_spec%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</FORM>

</BODY>
</HTML>



<script language=javascript>
<!--
//등록하기
function sendSave()
{
	
	var total_count = document.sForm.total_count.value;
	if(isNaN(total_count)) { alert('[실적수량] 숫자만 입력이 가능합니다.'); return; }
	else if(total_count.indexOf('.') != -1) { alert('[실적수량] 정수만 입력이 가능합니다.'); return; }
	else if(total_count.indexOf('-') != -1) { alert('[실적수량] 자연수만 입력이 가능합니다.'); return; }
	else if(total_count == '0' || total_count.length == 0) { alert('[실적수량]수량을 입력하십시오.'); return;}

	var good_count = document.sForm.good_count.value;
	if(isNaN(good_count)) { alert('[양품수량] 숫자만 입력이 가능합니다.'); return; }
	else if(good_count.indexOf('.') != -1) { alert('[양품수량] 정수만 입력이 가능합니다.'); return; }
	else if(good_count.indexOf('-') != -1) { alert('[양품수량] 자연수만 입력이 가능합니다.'); return; } 

	var bad_count = document.sForm.bad_count.value;
	if(isNaN(bad_count)) { alert('[불량수량] 숫자만 입력이 가능합니다.'); return; }
	else if(bad_count.indexOf('.') != -1) { alert('[불량수량] 정수만 입력이 가능합니다.'); return; }
	else if(bad_count.indexOf('-') != -1) { alert('[불량수량] 자연수만 입력이 가능합니다.'); return; } 

	//수량검사
	if(eval(total_count - good_count - bad_count != 0)) { 
		alert('실적,양품,불량수량 입력의 전체적인 계산에 문제가 있습니다.'); return; }

	//불량수량이 있을때 입력항목 검사
	if(bad_count.length != 0) {
		if(eval(bad_count > 0)) {
			var bad_type = document.sForm.bad_type.value;
			var bad_note = document.sForm.bad_note.value;
			if(bad_type.length == 0) { alert('불량형태을 입력하십시오.'); return; }
			if(bad_note.length == 0) { alert('불량원인을 입력하십시오.'); return; }
		}
	}

	var check_count = '<%=check_count%>'; 
	if(eval(check_count - total_count < 0)) { alert('누적총생산량을 초과하였습니다.'); return; }

	var v = confirm('해당내용을 등록하시겠습니까?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_save';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//수정하기
function sendModify()
{
	var output_date = '<%=output_date%>';
	var todate = '<%=todate%>';
	if(output_date != todate) { alert('일일마감되었습니다.'); return; }

	var total_count = document.sForm.total_count.value;
	if(isNaN(total_count)) { alert('[실적수량] 숫자만 입력이 가능합니다.'); return; }
	else if(total_count.indexOf('.') != -1) { alert('[실적수량] 정수만 입력이 가능합니다.'); return; }
	else if(total_count.indexOf('-') != -1) { alert('[실적수량] 자연수만 입력이 가능합니다.'); return; } 

	var good_count = document.sForm.good_count.value;
	if(isNaN(good_count)) { alert('[양품수량] 숫자만 입력이 가능합니다.'); return; }
	else if(good_count.indexOf('.') != -1) { alert('[양품수량] 정수만 입력이 가능합니다.'); return; }
	else if(good_count.indexOf('-') != -1) { alert('[양품수량] 자연수만 입력이 가능합니다.'); return; } 

	var bad_count = document.sForm.bad_count.value;
	if(isNaN(bad_count)) { alert('[불량수량] 숫자만 입력이 가능합니다.'); return; }
	else if(bad_count.indexOf('.') != -1) { alert('[불량수량] 정수만 입력이 가능합니다.'); return; }
	else if(bad_count.indexOf('-') != -1) { alert('[불량수량] 자연수만 입력이 가능합니다.'); return; } 

	//수량검사
	if(eval(total_count - good_count - bad_count != 0)) { 
		alert('실적,양품,불량수량 입력의 전체적인 계산에 문제가 있습니다.'); return; }

	//불량수량이 있을때 입력항목 검사
	if(bad_count.length != 0) {
		if(eval(bad_count > 0)) {
			var bad_type = document.sForm.bad_type.value;
			var bad_note = document.sForm.bad_note.value;
			if(bad_type.length == 0) { alert('불량형태을 입력하십시오.'); return; }
			if(bad_note.length == 0) { alert('불량원인을 입력하십시오.'); return; }
		}
	}

	var pid = document.sForm.pid.value;
	if(pid == '') { alert('수정할 항목을 선택하십시오.'); return; }

	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_modify';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//신규등록
function sendView()
{
	var assy_code = document.sForm.item_code.value;
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='product_preview';
	document.sForm.pid.value='';
	document.sForm.assy_code.value=assy_code;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>