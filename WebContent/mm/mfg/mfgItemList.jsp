<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "공정별 소요량조정 계획 LIST"		
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
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧
	com.anbtech.mm.entity.mfgItemTable assy;
	com.anbtech.mm.entity.mfgItemTable item;
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	String assy_code = (String)request.getAttribute("assy_code"); if(assy_code == null) assy_code = "";

	//--------------------------------------
	//제품정보 마스터 데이터 가져오기
	//--------------------------------------
	com.anbtech.mm.entity.mfgMasterTable master;
	master = (mfgMasterTable)request.getAttribute("MFG_master");
	String gid = master.getPid();
	String order_status = master.getOrderStatus();
	String mfg_no = master.getMfgNo();
	String factory_no = master.getFactoryNo();
	String factory_name = master.getFactoryName();
	String item_code = master.getItemCode();
	String order_type = master.getOrderType();

	//--------------------------------------
	//공정 리스트 가져오기
	//--------------------------------------
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mfgItemTable();
	Iterator assy_iter = assy_list.iterator();

	//--------------------------------------
	//해당공정의 부품 리스트 가져오기
	//--------------------------------------
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("ITEM_List");
	item = new mfgItemTable();
	Iterator item_iter = item_list.iterator();

%>

<HTML><HEAD><TITLE>공정별 소요량조정 계획 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form name="sForm" method="post" style="margin:0">

<!--상단 TITLE-->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> 부품조정</TD></TR></TBODY></TABLE>

<!---외곽 Line--->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='93%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- 실 내용 -->
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'>공정별 출고부품 소요량조정</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--버튼-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='10%' align=left noWrap class="bg_03" style='padding-left:5px;'>
		
							<a href="javascript:sendCont();"><img src="../mm/images/bt_view_d.gif" align="absmiddle" border='0'></a>
			<%
						if(order_status.equals("3")) {		//오더확정상태 
						out.println("<a href='javascript:itemDelivery();'><img src='../mm/images/bt_itemout_req.gif' align='absmiddle' border='0' alt='자재출고의뢰'></a>");
						} 
			%>
						<select name="assy" style=font-size:9pt;color="black"; onChange='javascript:goAssy();'>  
			<%
						String sel = "";
						while(assy_iter.hasNext()){
							assy = (mfgItemTable)assy_iter.next();
							if(assy_code.equals(assy.getAssyCode())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+assy.getAssyCode()+"|"+assy.getLevelNo()+"'>");
							out.println(assy.getAssyCode()+"</option>");
						}
				%>
						</select>	
						</TD>
						<TD width='90%' align=left noWrap>
							<IMG src='../mm/images/product_com.gif' border='0' align='absmiddle' alt='생산지시번호'><font color='#639DE9'><%=master.getMfgNo()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_code.gif' border='0' align='absmiddle' alt='모델명'> <font color='#639DE9'><%=master.getModelCode()%></font>&nbsp;&nbsp;
							<IMG src='../mm/images/model_name.gif' border='0' align='absmiddle' alt='모델명'> <font color='#639DE9'><%=master.getModelName()%></font>

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
			  <TD noWrap width=90 align=middle class='list_title'>품목번호</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>DESC</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
						<TD noWrap width=30 align=middle class='list_title'>계정</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>BOM수량</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=65 align=middle class='list_title'>생산소요량</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=60 align=middle class='list_title'>추가량</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=65 align=middle class='list_title'>출고요청량</TD>
					    <TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
					    <TD noWrap width=40 align=middle class='list_title'>단위</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>

		<% 
					while(item_iter.hasNext()){
					item = (mfgItemTable)item_iter.next();
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						  <TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getDrawCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getNeedCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getAddCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReserveCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							
						</TR>
						<TR><TD colSpan=15 background="../mm/images/dot_line.gif"></TD></TR>					
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

</TD></TR></TABLE>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<INPUT type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
<INPUT type="hidden" name="factory_name" size="15" value="<%=factory_name%>">
<INPUT type="hidden" name="level_no" size="15" value="">
<INPUT type="hidden" name="assy_code" size="15" value="">
<INPUT type="hidden" name="order_status" size="15" value="">
<INPUT type="hidden" name="item_code" size="15" value="<%=item_code%>">
<INPUT type="hidden" name="order_type" size="15" value="<%=order_type%>">

</FORM>

</BODY>
</HTML>


<script language=javascript>
<!--
//메시지 전달
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//공정바꾸기
function goAssy()
{
	var assy = document.sForm.assy.value.split("|");

	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_list';
	document.sForm.assy_code.value=assy[0];
	document.sForm.level_no.value=assy[1];
	document.onmousedown=dbclick;
	document.sForm.submit();
}
//공정부품소요량조정 편집
function itemView(pid) 
{
	var order_status = '<%=order_status%>';
	if(order_status != '2') { 
		if(order_status == '3') status = "오더확정";
		else if(order_status == '4') status = "부품출고";
		else if(order_status == '5') status = "생산진행중";
		else if(order_status == '6') status = "생산마감";
		alert('생산지시서작성 상태에만 가능합니다. 현재 '+status+' 상태입니다.'); return; 
	}

	var order_type = document.sForm.order_type.value;

	parent.reg.document.sForm.action='../servlet/mfgInfoServlet';
	parent.reg.document.sForm.mode.value='item_view';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.order_type.value=order_type;
	parent.reg.document.sForm.submit();
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
//자재일괄 출고의뢰
function itemDelivery() 
{
	var order_status = '<%=order_status%>';
	if(order_status != '3') { 
		if(order_status == '2') status = "생산지시서작성";
		else if(order_status == '4') status = "부품출고";
		else if(order_status == '5') status = "생산진행중";
		else if(order_status == '6') status = "생산마감";
		alert('오더확정 상태에만 가능합니다. 현재 '+status+' 상태입니다.'); return; 
	}
	document.sForm.action='../servlet/mfgInfoServlet';
	document.sForm.mode.value='item_delivery';
	document.sForm.order_status.value='4';
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
	//var div_h = h - 485;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;
}

-->
</script>