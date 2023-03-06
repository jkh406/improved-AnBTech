<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "공정별 생산부품소요량 관리하기"		
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

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String order_type = (String)request.getAttribute("order_type"); 
	if(order_type == null) order_type = "";

	String order_name = "";
	if(order_type.equals("MANUAL")) order_name = "[긴급오더]";
	else if(order_type.equals("MRP")) order_name = "[MRP오더]";

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",gid="",mfg_no="",assy_code="",level_no="",item_code="",item_name="",item_spec="";
	String item_unit="",item_type="",item_loss="",draw_count="",mfg_count="";
	String need_count="",spare_count="",add_count="",reserve_count="",request_count="";
	String need_date="",order_date="",factory_no="",factory_name="";

	com.anbtech.mm.entity.mfgItemTable item;
	item = (mfgItemTable)request.getAttribute("MFG_item");

	pid = item.getPid();
	gid = item.getGid();
	mfg_no = item.getMfgNo();
	assy_code = item.getAssyCode();
	level_no = Integer.toString(item.getLevelNo());
	item_code = item.getItemCode();
	item_spec = item.getItemSpec();
	item_unit = item.getItemUnit();
	item_type = item.getItemType();
	need_count = Integer.toString(item.getNeedCount());
	add_count = Integer.toString(item.getAddCount());
	factory_no = item.getFactoryNo();
	order_date = item.getOrderDate();
	
	//----------------------------------------------------
	//	편집 여부 판단하기
	//----------------------------------------------------
	String icon = "D";						//icon 출력여부
	String rd = "readonly";					//TEXT 수정가능 여부판단하기
	String ab = "disabled";					//선택박스 수정가능 여부판단하기
	String ra = "enable";					//Radio Button 수정가능 판단
	if(order_date.length() == 0) {									
		rd="";ab=""; icon="E";
	}
	
%>

<HTML>
<HEAD><TITLE>공정별 생산부품소요량 관리하기</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0" onSubmit="sendModify();return false;">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='100%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height=25><!-- 타이틀 및 페이지 정보 -->
				<TD vAlign="center" height=25 class="bg_05" style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'> 부품소요량조정 <%=order_name%></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR height="25"><TD vAlign="center" align='left' style='padding-left:5px;'  bgcolor=''>
			
			<% if(item_code.length()>1) { //편집
			%>	<INPUT type='image' onfocus=blur() src="../mm/images/bt_modify.gif" border=0 align='absmiddle'>
			<% }
			%>
				
			</TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR>
				<TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
						<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
								<tr><td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목코드</td>
									<td width="37%" height="25" class="bg_04"><%=item_code%>
										<input type="hidden" name="factory_no" value="<%=factory_no%>" size="12">
									</td>
									<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목규격</td>
									<td width="37%" height="25" class="bg_04"><%=item_spec%>
										<input type="hidden" name="item_spec" value="<%=item_spec%>" size="10"> 
									</td>
								</tr>
								<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
								<tr><td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">생산필요수량</td>
									<td width="37%" height="25" class="bg_04"><%=need_count%>
										<input type="hidden" name="need_count" value="<%=need_count%>" size="10"></td>
									<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">추가수량</td>
									<td width="37%" height="25" class="bg_04">
										<input type="text" name="add_count" value="<%=add_count%>" size="10" <%=rd%>> </td>
								</tr>
								<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></TR>
							</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='gid' value='<%=gid%>'>
<input type='hidden' name='mfg_no' value='<%=mfg_no%>'>
<input type='hidden' name='assy_code' value='<%=assy_code%>'>
<input type='hidden' name='level_no' value='<%=level_no%>'>
<input type='hidden' name='factory_no' value='<%=factory_no%>'>
<input type='hidden' name='order_type' value='<%=order_type%>'>
</form>

</BODY>
</HTML>
<script language=javascript>
<!--
//수정하기
function sendModify()
{
	var add_count = document.sForm.add_count.value;
	var item_code = '<%=item_code%>';
	if(item_code.length==0){ alert(item_code.length+"수정할 부품을 선택하십시오.");return;}
	if(isNaN(add_count)) { alert('숫자만 입력이 가능합니다.'); return; }
	else if(add_count.indexOf('.') != -1) { alert('정수만 입력이 가능합니다.'); return; }
	else if(add_count.indexOf('-') != -1) { 
		//출고가능부품(구매품)은 마이너스가 없음.
		item_type = '<%=item_type%>';
		if(item_type == '4') {alert('자연수만 입력이 가능합니다.'); return; } 
	}
 
	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_modify';
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


function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 700 ;
	
	item_list.style.height = div_h;

}

-->
</script>