<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MRS소요량 수정하기"		
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
	com.anbtech.text.StringProcess str = new StringProcess();
	com.anbtech.mm.entity.mrpMasterTable mrp;

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	String assy_code = (String)request.getAttribute("assy_code"); if(assy_code == null) assy_code = "";

	//--------------------------------------
	//MRP MASTER정보
	//--------------------------------------
	String gid="";	//MRP MASTER에는 없으나 MRP ITEM의 gid값이 MRP MASTER의 pid값임.
	String mps_no="",mrp_no="",model_code="",model_name="",org_item_code="";
	String fg_code="",factory_no="",factory_name="",mrp_status="";

	mrp = (mrpMasterTable)request.getAttribute("MRP_master");
	gid = mrp.getPid();
	mps_no = mrp.getMpsNo();
	mrp_no = mrp.getMrpNo();
	model_code = mrp.getModelCode();
	model_name = mrp.getModelName();
	fg_code = mrp.getFgCode();
	org_item_code=mrp.getItemCode();
	mrp_status = mrp.getMrpStatus();		
	factory_no = mrp.getFactoryNo();
	factory_name = mrp.getFactoryName();

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",parent_code="",item_code="",item_name="",item_spec="",plan_count="",item_unit="";
	String add_count="",item_type="";

	com.anbtech.mm.entity.mrpItemTable item;
	item = (mrpItemTable)request.getAttribute("MRP_item");

	pid = item.getPid();
	parent_code = item.getAssyCode();
	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	plan_count = Integer.toString(item.getPlanCount());
	item_unit = item.getItemUnit();
	add_count = Integer.toString(item.getAddCount());
	item_type = item.getItemType();
	
%>

<HTML>
<HEAD><TITLE>MRS소요량 수정하기</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="sForm" method="post" style="margin:0" onsubmit="return false;"> 
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>
	<TABLE border=0 cellspacing=0 cellpadding=1 width="100%">
		<TBODY>
			<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'>구매부품편집</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
			<TR><TD align=left width='100%' height='25' bgcolor=''><!--버튼-->
				 <a href="javascript:sendModify();"><img src="../mm/images/bt_modify.gif" align="absmiddle" border='0'></a>
				</TD></TR>
	
			<!--내용-->
			<TR><TD>
				<TABLE cellspacing=0 cellpadding=1 width="100%" border=0>
					<TBODY>
					<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
					<tr>
						<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">모품목코드</td>
						<td width="37%" height="25" class="bg_04"><%=parent_code%></td>
						<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">품목코드</td>
						<td width="37%" height="25" class="bg_04"><%=item_code%></td>
					</tr>
					<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
					<tr>
						<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">산출수량</td>
						<td width="37%" height="25" class="bg_04"><%=plan_count%></td>
						<td width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">추가수량</td>
						<td width="37%" height="25" class="bg_04">
							<input class='text_01' type='text' name='add_count' value='<%=add_count%>'></td>
					</tr>
					<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
				</TBODY></TABLE>
			</TD></TR>
		</TBODY></TABLE>
</TD></TR></TABLE>

<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='gid' value='<%=gid%>'>
<input type='hidden' name='mrp_no' value='<%=mrp_no%>'>
<input type='hidden' name='fg_code' value='<%=fg_code%>'>
<input type='hidden' name='item_code' value='<%=org_item_code%>'>
<input type='hidden' name='factory_no' value='<%=factory_no%>'>
<input type='hidden' name='mrp_status' value='<%=mrp_status%>'>
<input type='hidden' name='assy_code' value='<%=assy_code%>'>
<input type='hidden' name='plan_count' value='<%=plan_count%>'>
<input type='hidden' name='item_type' value='<%=item_type%>'>

</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//수정하기
function sendModify()
{
	var mrp_status = document.sForm.mrp_status.value;
	if(mrp_status != '0' && mrp_status != '1') {
		if(mrp_status == '3') mrp_status = 'MRP확정';
		else if(mrp_status == '4') mrp_status = '구매등록';
		else if(mrp_status == '5') mrp_status = '구매발주';

		alert('소요량을 수정할 수 없습니다. '+mrp_status+' 상태입니다.'); return; 
	}
	var item_type = document.sForm.item_type.value;
	if(item_type != '4') { alert('구매품만 수정이 가능합니다.'); return; }

	var add_count = document.sForm.add_count.value;
	if(isNaN(add_count)) { alert('숫자만 입력이 가능합니다.'); return; }
	else if(add_count.indexOf('.') != -1) { alert('정수만 입력이 가능합니다.'); return; }

	if(!confirm('해당내용을 수정하시겠습니까?')) return;  

	document.sForm.action='../servlet/mrpInfoServlet';
	document.sForm.mode.value='item_modify';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>