<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "공정별 계획 LIST"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.mm.entity.mfgOperatorTable table;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";

	//--------------------------------------
	//제품정보 마스터 데이터 가져오기
	//--------------------------------------
	com.anbtech.mm.entity.mfgMasterTable item;
	item = (mfgMasterTable)request.getAttribute("MFG_master");
	String gid = item.getPid();
	String order_status = item.getOrderStatus();
	String mrp_no = item.getMrpNo();
	String mfg_no = item.getMfgNo();
	String item_code = item.getItemCode();
	String factory_no = item.getFactoryNo();
	String order_type = item.getOrderType();

	//--------------------------------------
	//공정별 리스트 가져오기
	//--------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ORDER_List");
	table = new mfgOperatorTable();
	Iterator table_iter = table_list.iterator();

	int od_list_cnt = table_list.size();				//전체 공정수량	
	int check_cnt = 0;									//관련정보 입력여부 갯수
%>

<HTML><HEAD><TITLE>공정별 계획 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display()'>
<form name="sForm" method="post" style="margin:0">
<!-- TITLE -->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
	<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
	<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> 공정계획</TD></TR></TBODY></TABLE>

<!-- 외곽 Line -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='88%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- 내용 -->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>공정별 계획정보</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--버튼-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='20%' align=left style='padding-left:5px;'>
					<%		if(order_status.equals("2")) { //공정생성 상태 
								out.println("<a href='javascript:sendOrder();'><img src='../mm/images/bt_order_conf.gif' align='absmiddle' border='0' alt='오더확정'></a>");	
							} 
					%>
								<a href="javascript:sendCont();"><img src="../mm/images/bt_view_d.gif" align="absmiddle" border='0'></a>
							</TD>
							<TD width='80%' align=left style='padding-lefg:5px;'>
							&nbsp;&nbsp;
							<IMG src='../mm/images/product_com.gif' border='0' align='absmiddle' alt='생산지시번호'><font color='#639DE9'><%=item.getMfgNo()%></font> &nbsp;&nbsp;
							<IMG src='../mm/images/model_code.gif' border='0' align='absmiddle' alt='모델명'> <font color='#639DE9'><%=item.getModelCode()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_name.gif' border='0' align='absmiddle' alt='모델명'> <font color='#639DE9'><%=item.getModelName()%></font>
						</TD>
						</TR></TBODY></TABLE></TD>
		</TR>
		<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
		
		<!--리스트-->
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR vAlign=middle height=25>
						  <TD noWrap width=40 align=middle class='list_title'>공정</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>생산구분</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=400 align=middle class='list_title'>생산품목번호 / 설명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=200 align=middle class='list_title'>작업장 / 설명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>시작예정일</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>종료예정일</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=60 align=middle class='list_title'>생산수량</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>오더확정일</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>담당자사번</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>담당자명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>외주담당자</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=120 align=middle class='list_title'>외주연락처</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=200 align=middle class='list_title'>외주업체명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=100 align=middle class='list_title'>사업자코드</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=28></TD></TR>

		<% 
					while(table_iter.hasNext()){
					table = (mfgOperatorTable)table_iter.next();
					
					String buy_type="";
					if(table.getBuyType().equals("M")) { buy_type = "사내가공";		check_cnt++; }
					else if(table.getBuyType().equals("O")) { buy_type = "외주가공";check_cnt++; }
					else if(table.getBuyType().equals("P")) { buy_type = "구매품";	check_cnt++; }
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle class='list_bg'><%=table.getOpNo()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=buy_type%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getAssyCode()%> <%=table.getAssySpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getWorkNo()%> <%=table.getWorkName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getOpStartDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getOpEndDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgCount()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'> <%=table.getOrderDate()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgId()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getMfgName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompUser()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompTel()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompName()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getCompCode()%></td>
							
						</TR>
						<TR><TD colSpan=28 background="../mm/images/dot_line.gif"></TD></TR>
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>


<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="op_order" size="15" value="">
<INPUT type="hidden" name="check_cnt" size="15" value="<%=check_cnt%>">
<INPUT type="hidden" name="mrp_no" size="15" value="<%=mrp_no%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="order_type" size="15" value="<%=order_type%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
</TD></TR></TABLE>
</FORM>

</BODY>
</HTML>
<SCRIPT language=javascript>
<!--
//메시지 전달
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}

//공정계획 편집
function assyView(pid)
{
	parent.reg.document.sForm.action='../servlet/mfgInfoServlet';
	parent.reg.document.sForm.mode.value='order_view';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.submit();
}
//작업지시확정
function sendOrder()
{
	var od_list_cnt = '<%=od_list_cnt%>';
	var check_cnt = document.sForm.check_cnt.value; 
	if(od_list_cnt != check_cnt) { 
		alert('내용이 누락된 공정이 있습니다. 먼저 입력후 진행하십시오.'); return; 
	} 

	var order_status='<%=order_status%>';
	if(order_status != '2') { 
		if(order_status == '3') status = "오더확정";
		else if(order_status == '4') status = "생산진행중";
		else if(order_status == '5') status = "생산마감";
		alert('작업지시서작성 상태에만 가능합니다. 현재 '+status+' 상태입니다.'); return; 
	}

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='order_act';
	document.sForm.op_order.value='1';
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//오더내용
function sendCont()
{
	var pid = document.sForm.gid.value;
	
	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='mfg_review';
	document.sForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}

function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // 현재 프레임의 크기
	//var div_h = h - 645;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;

}
-->
</SCRIPT>