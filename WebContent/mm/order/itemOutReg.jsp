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
	
	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",mfg_no="",mfg_req_no="",item_code="",item_name="",item_spec="";
	String item_unit="",item_type="",req_count="",factory_no="";

	com.anbtech.mm.entity.mfgReqItemTable item;
	item = (mfgReqItemTable)request.getAttribute("REQ_item");

	pid = item.getPid();
	mfg_no = item.getMfgNo();
	mfg_req_no = item.getMfgReqNo();
	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	item_unit = item.getItemUnit();
	item_type = item.getItemType();
	req_count = Integer.toString(item.getReqCount());
	factory_no = item.getFactoryNo();

	//----------------------------------------------------
	//  부품예약및 출고한 부품수량으로 의뢰가능한 부품수량파악
	//----------------------------------------------------
	com.anbtech.mm.entity.mfgItemTable mfg_item;
	mfg_item = (mfgItemTable)request.getAttribute("MFG_item");
	int reserve_count = mfg_item.getReserveCount();			//예약수량
	int request_count = mfg_item.getRequestCount();			//출고수량
	int check_count = reserve_count - request_count;		//출고의뢰 가능한 최대수량

%>

<HTML>
<HEAD><TITLE>공정별 생산부품소요량 관리하기</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0" onsubmit='return false;'>
<!-- TITLE -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='100%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>
<!-- 내용 -->
<TABLE border=0 cellspacing=0 cellpadding=1 width="100%">
	<TBODY>
		<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'> 부품출고의뢰등록</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
		<TR><TD align=left width='100%' height='25' bgcolor=''><!--버튼-->
	<% 
		if(!item_code.equals("")){%>
			<INPUT type='image' onclick='sendModify();' src="../mm/images/bt_modify.gif" border=0 align='absmiddle'>
	<%	} 
	%>
			</TD></TR>
	
		<!--내용-->
		<TR><TD>
			<TABLE cellspacing=0 cellpadding=1 width="100%" border=0>
				<TBODY>
					<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목코드</td>
						<TD width="37%" height="25" class="bg_04"><%=item_code%></td>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목규격</td>
						<TD width="37%" height="25" class="bg_04"><%=item_spec%></td>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">계정</td>
						<TD width="37%" height="25" class="bg_04"><%=item_type%></td>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">출고의뢰수량</td>
						<TD width="37%" height="25" class="bg_04">
							<INPUT class='text_01' type="text" name="req_count" value="<%=req_count%>" size="10">
					&nbsp;&nbsp;&nbsp;&nbsp;의뢰가능수량 : <%=check_count%></td>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=pid%>">
<INPUT type="hidden" name="mfg_req_no" size="15" value="<%=mfg_req_no%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="assy_code" size="15" value="">
<INPUT type="hidden" name="level_no" size="15" value="">
<INPUT type="hidden" name="item_code" size="15" value="">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//수정하기
function sendModify()
{
	if('<%=item_type%>' == "") { return; }
	var req_count = document.sForm.req_count.value;
	if(isNaN(req_count)) { alert('숫자만 입력이 가능합니다.'); return; }
	else if(req_count.indexOf('.') != -1) { alert('정수만 입력이 가능합니다.'); return; }
	else if(req_count.indexOf('-') != -1) { 
		//출고가능부품(구매품)은 마이너스가 없음.
		item_type = '<%=item_type%>';
		if(item_type == '4') {alert('자연수만 입력이 가능합니다.'); return; } 
	}

	var check_count = '<%=check_count%>';
	if(eval(check_count - req_count < 0)) { alert('출고의뢰 가능수량을 초과하였습니다.'); return; }

	var v = confirm('해당내용을 수정하시겠습니까?'); 
	if(v == false) { return; }

	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='out_modify';
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