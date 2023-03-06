<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 기본정보 등록/수정하기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String msg = (String)request.getAttribute("msg");	if(msg == null) msg = "";


	//----------------------------------------------------
	//	입력/수정 정보 읽기
	//----------------------------------------------------
	String pid="",fg_code="",purpose="",pdg_code="",pdg_name="",pd_code="",pd_name="";
	String modelg_code="",modelg_name="",model_code="",model_name="",pjt_code="",pjt_name="";
	String reg_date="",bom_status="";

	com.anbtech.bm.entity.mbomMasterTable item;
	item = (mbomMasterTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	modelg_code = item.getModelgCode();
	modelg_name = item.getModelgName();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();
	pdg_code = item.getPdgCode();
	pdg_name = item.getPdgName();
	pd_code = item.getPdCode();
	pd_name = item.getPdName();
	pjt_code = item.getPjtCode();
	pjt_name = item.getPjtName();
	reg_date = item.getRegDate();
	purpose = item.getPurpose();
	bom_status = item.getBomStatus();	if(bom_status.length() == 0) bom_status="S";

	//----------------------------------------------------
	//	등록 과 수정,삭제 모드 구분 
	//----------------------------------------------------
	String stage = "W";							//등록상태
	String title = "BOM정보등록";
	if(reg_date.length() != 0) {				//수정,삭제상태
		stage = "M";
		title = "BOM정보수정";
	}

	reg_date = anbdt.getDateNoformat();			//등록/수정 일자

%>

<HTML>
<HEAD><TITLE>BOM 기본정보 등록/수정하기</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" method="post" style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif"> <%=title%></TD>
				</TR>
				</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32<!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px'>
			<%if(stage.equals("W")) { // 등록버튼 %>
					<a href="javascript:sendSave();"><img src="../bm/images/bt_reg.gif" border=0></a>
			<%} else {  //수정, 삭제 버튼%>
					<a href="javascript:sendModify();"><img src="../bm/images/bt_modify.gif" border=0></a>
					<a href="javascript:sendDelete();"><img src="../bm/images/bt_del.gif" border=0></a>
			<%}		%>
					<a href="javascript:sendList();"><img src="../bm/images/bt_cancel.gif" border=0></a>
					</TD></TR></TBODY></TABLE></TD>
	</TR></TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">F/G코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT class='text_01' type='text' name='fg_code' value='<%=fg_code%>'  size='12'  readonly>
			   <% if(bom_status.equals("S") || bom_status.equals("1")) { %>
					<a href="javascript:searchBomInfo();"><img src="../bm/images/bt_search.gif" border="0" align='absbottom'></a>
			   <% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">BOM구분</TD>
			   <TD width="37%" height="25" class="bg_04">
					<select name="purpose" style=font-size:9pt;color="black"; >
					<%
						String[] pp_no = {"0","1"};
						String[] pp_name = {"제조BOM","임시BOM"};
						String sel = "";
						for(int i=0; i<pp_no.length; i++) {
							if(pp_no[i].equals(purpose)) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+pp_no[i]+"'>");
							out.println(pp_name[i]+"</option>");
						} 
					%></select>
					</TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품군코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='pdg_code' value='<%=pdg_code%>'  size='5' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품군명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='pdg_name' value='<%=pdg_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type='text' name='pd_code' value='<%=pd_code%>' size='5' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='pd_name' value='<%=pd_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델군코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='modelg_code' value='<%=modelg_code%>'  size='5' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델군명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='modelg_name' value='<%=modelg_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='model_code' value='<%=model_code%>' size='15' readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT  type='text' name='model_name' value='<%=model_name%>' size='30' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">과제코드</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type='text' name='pjt_code' value='<%=pjt_code%>' readonly>
				<% if(bom_status.equals("S") || bom_status.equals("1")) { %>
					<a href="javascript:searchProject();"><img src="../bm/images/bt_search.gif" border="0" align='absbottom'></a>
			   <% } %></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">과제명</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type='text' name='pjt_name' size='35' value='<%=pjt_name%>' readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=10></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">등록자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=sl.id%>/<%=sl.name%>
					<INPUT type="hidden" name="reg_name" value="<%=sl.name%>" size="20">
					<INPUT type="hidden" name="reg_id" value="<%=sl.id%>" size="20"></TD>
					</TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">등록일</TD>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getDate()%>
					<INPUT type="hidden" name="reg_date" value="<%=anbdt.getDateNoformat()%>" size="30"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='fg_spec' value=''>
</FORM>

<DIV id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>


<script language=javascript>
<!--
//msg 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back();
	//location.href="servlet/BOMBaseInfoServlet";
}

//등록하기
function sendSave()
{
	var bom_status = '<%=bom_status%>';
	if(bom_status != 'S') {	alert('초기등록상태 일때만 가능합니다.'); return; }

	var mcode = document.eForm.model_code.value;
	if(mcode.length == 0) { alert('모델코드가 입력되지 않았습니다.'); return; }
	var mname = document.eForm.model_name.value;
	if(mname.length == 0) { alert('모델명이 입력되지 않았습니다.'); return; }
	var fgcode = document.eForm.fg_code.value;
	if(fgcode.length == 0) { alert('FG코드가 입력되지 않았습니다.'); return; }

	


	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='info_write';
	document.eForm.submit();
}
//부품 수정하기
function sendModify()
{
	var bom_status = '<%=bom_status%>';
	if(bom_status == '4') {	alert('결재중상태이므로 수정할 수 없습니다.'); return; }
	else if(bom_status == '5') {	alert('확정상태이므로 수정할 수 없습니다.'); return; }

	if(!confirm('수정하시겠습니까?'))return;

	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='info_modify';
	document.eForm.submit();
}
//부품 삭제하기
function sendDelete()
{
	var bom_status = '<%=bom_status%>';
	if(bom_status != '1') { alert('작성상태일때만 삭제 가능합니다.'); return; }

	if(!confirm('삭제하시겠습니까?'))return;

	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='info_delete';
	document.eForm.submit();
}
//목록으로
function sendList()
{
	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='info_list';
	document.eForm.submit();
}
//FG코드 검색하기
function searchBomInfo(){
		
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
}
// 과제찾기
function searchProject() {

	para = "&target=eForm.pjt_code/eForm.pjt_name";	wopen('../servlet/PsmProcessServlet?mode=search_project'+para,'search_pjt','400','230','scrollbars=no,toolbar=no,status=no,resizable=no');
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