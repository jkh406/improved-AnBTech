<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "개별 BOM등록/수정하기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//----------------------------------------------------
	// MBOM MASTER정보 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomMasterTable masterT;
	masterT = (mbomMasterTable)request.getAttribute("MASTER_List");
	String pdg_code = masterT.getPdgCode();
	String model_code = masterT.getModelCode();

	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",gid="",parent_code="",child_code="",part_name="";
	String part_spec="",location="",op_code="",qty_unit="EA";
	String part_type="";
	String level_no="0";
	String qty="0";

	com.anbtech.bm.entity.mbomStrTable item;
	item = (mbomStrTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	gid = item.getGid();
	parent_code = item.getParentCode();
	child_code = item.getChildCode();
	level_no = item.getLevelNo();
	part_name = item.getPartName();
	part_spec = item.getPartSpec();
	location = item.getLocation();
	op_code = item.getOpCode();
	qty_unit = item.getQtyUnit();
	qty = item.getQty();
	part_type = item.getPartType();		//A:Assy, P:부품

	//----------------------------------------------------
	//	등록 과 수정,삭제 모드 구분 
	//----------------------------------------------------
	String sTitle = "등록";
	String stage = "W";							//등록상태
	String file_tag = "w";
	if(gid.length() != 0) {
		sTitle = "수정/삭제";
		stage = "M";							//수정,삭제상태
		file_tag = "m";
	}

	//-----------------------------------
	//	파라미터 받기 
	//-----------------------------------
	if(gid.length() == 0) gid = (String)request.getAttribute("gid");
	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	int tg = msg.indexOf("사용권한");		//사용권한 있는지 판단하기

%>

<HTML>
<HEAD><TITLE>개별 BOM등록/수정하기</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD vAlign=top>
	<TABLE border=0 cellspacing=0 cellpadding=1 width="100%" height='100%'>
	<TBODY>
		<TR height=25><TD vAlign=center  style='padding-left:5px' class='bg_05' BACKGROUND='../bm/images/title_bm_bg.gif'>부품등록</TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
		<TR><TD align=left width='100%' height='25' bgcolor=''><!--버튼-->
	<%	
				if(stage.equals("W")) {		//등록일때
					out.print("&nbsp;<a href='javascript:sendSave();'><img src='../bm/images/bt_add.gif' border=0 align='absmiddle'></a>");
				} else {					//수정일때
					out.print("&nbsp;<a href='javascript:sendModify();'><img src='../bm/images/bt_modify.gif' border=0 align='absmiddle'></a>");
					if(part_type.equals("P")) //부품일때 삭제가능
						out.print("&nbsp;<a href='javascript:sendDelete();'><img src='../bm/images/bt_del.gif' border=0 align='absmiddle'></a>");
					else if(part_type.equals("A")) //Assy일때 전체삭제 기능
						out.print("&nbsp;<a href='javascript:sendAllDelete();'><img src='../bm/images/bt_totaldel.gif' border=0 alt='전체삭제' align='absmiddle'></a>");
					out.print("&nbsp;<a href='javascript:SaveForm();'><img src='../bm/images/bt_add_new2.gif' border=0 align='absmiddle'></a>");
				} 
%>		</TD></TR><TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	
		<!--내용-->
		<TR><TD valign="top">
			<TABLE cellspacing=0 cellpadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">모품목코드</TD>
					<TD width="35%" height="24" class="bg_04">

			   <% if(stage.equals("W")) { //신규등록 일때%>
					<INPUT class="text_01" type='text' name='parent_code' value='<%=parent_code%>'></TD>
			   <% } else { //수정,삭제 일때%>
					<%=parent_code%>
					<INPUT type='hidden' name='parent_code' value='<%=parent_code%>'></TD>
			   <% } %>

						   <TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">자품목코드</TD>
						   <TD width="35%" height="24" class="bg_04">
			   <% 
				//if(part_type.equals("P")) {		//부품으로 수정가능함 
					out.print("<INPUT class='text_01' type='text' name='child_code' value='"+child_code+"' readonly>");
					out.print("&nbsp;<a href='javascript:searchItemInfo();'><img src='../crm/images/bt_search.gif' border='0' align='absbottom'></a>");
			   // } else {						//Assy로 수정 불가능함 
				//	out.print("<INPUT class='text_01' type='text' name='child_code' value='"+child_code+"' readonly>");
			   // } 
				%></TD></TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">LOC NO</TD>
						<TD width="35%" height="24" class="bg_04">
							<INPUT  type='text' name='location' value='<%=location%>'></TD>
						<TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">공정코드</TD>
						<TD width="35%" height="24" class="bg_04">
							<INPUT class="text_01" type='text' name='op_code' value='<%=op_code%>' readonly>
							<A href='javascript:searchOPInfo();'><img src='../bm/images/bt_search.gif' border='0' align='absbottom'></a></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>			
		<%  // 신규등록일때
			if(stage.equals("W")) {  
		%>
			<TR><TD width="15%" height="24" class="bg_03" background="../bm/images/bg-01.gif">부품수량</TD>
				<TD width="85%" height="24" class="bg_04" colspan=3>
					<INPUT type='text' name='part_cnt' size='5' value='1'class="text_01"> 개</TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
<%		}
%>
			</TBODY></TABLE></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='level_no' value='<%=level_no%>'>
<INPUT type='hidden' name='part_name' value=''>
<INPUT type='hidden' name='part_spec' value=''>
<INPUT type='hidden' name='price_unit' value='원'>
<INPUT type='hidden' name='price' value='0'>
<INPUT type='hidden' name='qty_unit' value=''>
<INPUT type='hidden' name='qty' value='1'>
<INPUT type='hidden' name='part_type' value='<%=part_type%>'>
<INPUT type='hidden' name='op_name' value=''>
<INPUT type='hidden' name='model_code' value='<%=model_code%>'>
<% 
	if(tg != -1) out.println("<INPUT type='hidden' name='msg' value='"+msg+"'>");
	else out.println("<INPUT type='hidden' name='msg' value=''>");
%>
</FORM>



</td></tr></table>
</BODY>
</HTML>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
//	parent.tree.document.all['saving'].style.visibility="hidden";	//메뉴버튼 enable [LIST]
	parent.tree.document.body.style.overflow='';					//LIST의 스크롤바 다시보여주기
}

//부품 등록하기
function sendSave()
{
	var pdg_code = '<%=pdg_code%>';		//모품목 제품군코드
	if(pdg_code.length == 0) { alert('해당 FG코드를 선택하십시오.'); return; }

	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var pcode = document.eForm.parent_code.value;
	if(pcode.length == 0) { alert('모품목코드가 입력되지 않았습니다.'); return; }

	var pcode_ini = pcode.substring(0,1);
	if(pcode_ini != '1' && pcode_ini != 'F') { 
		alert('모품목코드는 반드시 ASSY코드만 등록할 수 있습니다.'); return; 
	}

	var ccode = document.eForm.child_code.value;
	if(ccode.length == 0) { alert('자품목코드가 입력되지 않았습니다.'); return; }

	//자품목이 Assy코드일때 부품수량은 무조건 1임을 검증
	var pcnt = document.eForm.part_cnt.value;
	var chead = ccode.substring(0,1);
	if((chead == '1') && (pcnt > 1))  { 
		alert('ASSY코드는 동일부품갯수를 한개이상 입력할 수 없습니다.'); return; 
	}

	if(chead == '1') {
		var op_code = document.eForm.op_code.value;
		if(op_code.length == 0) { alert('공정코드가 입력되지 않았습니다.'); return; }
	}

	//동일한 제품군내의 코드인지 판단
	var pdg_code = '<%=pdg_code%>';		//모품목 제품군코드
	var chd_pdg = ccode.substring(3,4);	//자품목 제품군코드
	if((chead == '1') && (pdg_code != chd_pdg)) { 
		alert('동일한 제품군내의 코드를 사용해야 합니다.'); return; 
	}

	//모품목이 팬텀Assy일때 BOM구성할 수 없음
	var phead = pcode.substring(0,3); 
	if(phead == '1PH') { alert('Phantom Assy는 하부구조를 구성할 수 없습니다.'); return; }

	//처리중 메시지 출력
//	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
//	document.all['saving'].style.visibility="visible";				//메뉴버튼 Disable [자신]
//	parent.tree.document.all['saving'].style.visibility="visible";	//메뉴버튼 Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST의 스크롤바 숨기기

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_write';
	document.eForm.submit();
}
//부품 수정하기
function sendModify()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	//자품목이 Assy코드일때 공정코드 입력여부 검사
	var ccode = document.eForm.child_code.value;
	var chead = ccode.substring(0,1);
	if(chead == '1') {
		var op_code = document.eForm.op_code.value;
		if(op_code.length == 0) { alert('공정코드가 입력되지 않았습니다.'); return; }
	}

	//동일한 제품군내의 코드인지 판단
	var pdg_code = '<%=pdg_code%>';		//모품목 제품군코드
	var chd_pdg = ccode.substring(3,4);	//자품목 제품군코드
	if((chead == '1') && (pdg_code != chd_pdg)) { 
		alert('동일한 제품군내의 코드를 사용해야 합니다.'); return; 
	}

	var part_type = '<%=part_type%>';
	if(part_type == 'A') {
		var cn = confirm('ASSY코드로 해당되는 모든코드가 수정됩니다. 계속하시겠습니까?');
		if(cn == false) return;
	}

	//처리중 메시지 출력
//	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
//	document.all['saving'].style.visibility="visible";				//메뉴버튼 Disable [자신]
//	parent.tree.document.all['saving'].style.visibility="visible";	//메뉴버튼 Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST의 스크롤바 숨기기

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_modify';
	document.eForm.submit();
}
//부품 삭제하기
function sendDelete()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var cn = confirm('삭제하시겠습니까?');
	if(cn == false) return;
	
	//처리중 메시지 출력
//	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
//	document.all['saving'].style.visibility="visible";				//메뉴버튼 Disable [자신]
//	parent.tree.document.all['saving'].style.visibility="visible";	//메뉴버튼 Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST의 스크롤바 숨기기

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_delete';
	document.eForm.submit();
}
//Assy부품 삭제하기
function sendAllDelete()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var cn = confirm('해당Assy코드이하 모든 부품을 삭제하시겠습니까?');
	if(cn == false) return;
	
	//처리중 메시지 출력
//	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력
//	document.all['saving'].style.visibility="visible";				//메뉴버튼 Disable [자신]
//	parent.tree.document.all['saving'].style.visibility="visible";	//메뉴버튼 Disable [LIST]
//	parent.tree.document.body.style.overflow='hidden';				//LIST의 스크롤바 숨기기

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_all_delete';
	document.eForm.submit();
}
//수정모드에서 입력모드로 바꿔주기
function SaveForm()
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	document.eForm.action='../servlet/BomInputServlet';
	document.eForm.mode.value='pl_prewrite';
	document.eForm.pid.value='';
	document.eForm.submit();
}
// 품목정보 가져오기
function searchItemInfo(){
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=child_code&item_name=part_name&item_type=item_type&item_desc=part_spec&item_unit=qty_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//공정정보 가져오기
function searchOPInfo() {
	wopen("../mm/searchOPcode.jsp?target=eForm.op_code/eForm.op_name","opcode","250","350","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>