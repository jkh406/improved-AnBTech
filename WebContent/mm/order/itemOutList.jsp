<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부품출고의뢰 LIST"		
	contentType = "text/html; charset=KSC5601" 		
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
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	
	//--------------------------------------
	//부품출고의뢰 마스터 데이터 가져오기
	//--------------------------------------
	com.anbtech.mm.entity.mfgReqMasterTable master = new com.anbtech.mm.entity.mfgReqMasterTable();
	master = (mfgReqMasterTable)request.getAttribute("REQ_master");
	String req_status = master.getReqStatus();
	String mfg_no = master.getMfgNo();
	String mfg_req_no = master.getMfgReqNo();
	String assy_code = master.getAssyCode();
	String level_no = Integer.toString(master.getLevelNo());
	String assy_spec = master.getAssySpec();
	String factory_no = master.getFactoryNo();
	String factory_name = master.getFactoryName();

	//--------------------------------------
	//부품출고의뢰 리스트 가져오기
	//--------------------------------------
	com.anbtech.mm.entity.mfgReqItemTable item = new com.anbtech.mm.entity.mfgReqItemTable();
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("REQ_ITEM_List");
	Iterator item_iter = item_list.iterator();

%>

<HTML><HEAD><TITLE>부품출고의뢰 LIST</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<form name="sForm" method="post" style="margin:0">

<!--상단 TITLE-->
<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
<TBODY>
<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../mm/images/blet.gif" align="absmiddle"> 부품 출고의뢰 현황</TD></TR></TBODY></TABLE>

<!---외곽 Line--->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height='93%' border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD valign='top'>

<!-- 실 내용 -->
<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25>
			<TD vAlign=center style='padding-left:5px' class='bg_05'  background='../bm/images/title_bm_bg.gif'> 부품 출고의뢰 현황</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=27>
			<TD height=27><!--버튼-->
				<TABLE cellSpacing=0 cellPadding=0 border=0>
					<TBODY>
						<TR>
							<TD width='10%' align=left  style='padding-left:5px;'>
							<a href="javascript:sendConfirm();"><img src="../mm/images/bt_itemout_req.gif" align="absmiddle" border='0'></a>
						</TD>
						<TD width='90%' align=left >
							<img src="../mm/images/item_out_no.gif" align="absmiddle" alt='부품출고번호' border='0'> <font color='#639DE9'><%=mfg_req_no%></font> &nbsp;
							<img src="../mm/images/selitem_code.gif" align="absmiddle" alt='제품명' border='0'>
							<font color='#639DE9'><%=assy_code%></font> &nbsp;
							<img src="../mm/images/description.gif" align="absmiddle" alt='description' border='0'>
							<font color='#639DE9'><%=assy_spec%></font>
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
							<TD noWrap width=90 align=middle class='list_title'>품목코드</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=100% align=middle class='list_title'>품목설명</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep2.gif"></TD>
							<TD noWrap width=80 align=middle class='list_title'>요청수량</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=40 align=middle class='list_title'>단위</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=40 align=middle class='list_title'>계정</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../mm/images/list_tep.gif"></TD>
							<TD noWrap width=90 align=middle class='list_title'>등록일</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>

		<% 
				while(item_iter.hasNext()){
				item = (mfgReqItemTable)item_iter.next();
		%>	
						<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReqCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getReqDate()%></td>					
						</TR>
						<TR><TD colSpan=11 background="../mm/images/dot_line.gif"></TD></TR>
							<TR height=23 onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle class='list_bg'><%=item.getItemCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=item.getItemSpec()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=nfm.toDigits(item.getReqCount())%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemUnit()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getItemType()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=item.getReqDate()%></td>					
						</TR>
						<TR><TD colSpan=11 background="../mm/images/dot_line.gif"></TD></TR>
		<% 		}  //while 
		%>
		</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>

<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="mfg_req_no" size="15" value="<%=mfg_req_no%>">
<input type="hidden" name="mfg_no" size="15" value="<%=mfg_no%>">
<input type="hidden" name="assy_code" size="15" value="<%=assy_code%>">
<input type="hidden" name="level_no" size="15" value="<%=level_no%>">
<input type="hidden" name="item_code" size="15" value="">
<input type="hidden" name="factory_no" size="15" value="<%=factory_no%>">
<input type="hidden" name="factory_name" size="15" value="<%=factory_name%>">
<input type="hidden" name="req_status" size="15" value="">
</TD></TR></TABLE>
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
//의뢰부품 편집전달
function itemView(pid,item_code)
{
	var req_status = '<%=req_status%>';
	if(req_status != '1') { 
		if(req_status == '2') status = "출고요청";
		else if(req_status == '3') status = "부품출고";
		alert('부품출고작성 상태에만 가능합니다. 현재 '+status+' 상태입니다.'); return; 
	}

	var mfg_no = document.sForm.mfg_no.value;
	var assy_code = document.sForm.assy_code.value;
	var level_no = document.sForm.level_no.value;
	var factory_no = document.sForm.factory_no.value;

	parent.reg.document.sForm.action='../servlet/mfgOrderServlet';
	parent.reg.document.sForm.mode.value='out_preview';
	parent.reg.document.sForm.pid.value=pid;
	parent.reg.document.sForm.mfg_no.value=mfg_no;
	parent.reg.document.sForm.assy_code.value=assy_code;
	parent.reg.document.sForm.level_no.value=level_no;
	parent.reg.document.sForm.item_code.value=item_code;
	parent.reg.document.sForm.factory_no.value=factory_no;
	parent.reg.document.sForm.submit();
}
//의뢰부품출고요청
function sendConfirm()
{
	var req_status = '<%=req_status%>';
	if(req_status != '1'){
		if(req_status == '2') status = "출고요청";
		else if(req_status == '3') status = "부품출고";
		alert('부품출고작성 상태에만 가능합니다. 현재 '+status+' 상태입니다.'); return; 
	}
	document.sForm.action='../servlet/mfgOrderServlet';
	document.sForm.mode.value='out_confirm';
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
	//var div_h = h - 478;
	var div_h = c_h - 86;
	
	item_list.style.height = div_h;
}
-->
</script>