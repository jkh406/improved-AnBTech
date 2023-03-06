<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "임시BOM 승인확정/승인취소 하기"		
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
	String pid="",model_code="",model_name="",fg_code="",pd_code="",pd_name="";
	String pjt_code="",pjt_name="",reg_id="",reg_name="",reg_date="",purpose="";
	String bom_status="";

	com.anbtech.bm.entity.mbomMasterTable item;
	item = (mbomMasterTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();
	pd_code = item.getPdCode();
	pd_name = item.getPdName();
	pjt_code = item.getPjtCode();
	pjt_name = item.getPjtName();
	reg_date = item.getRegDate();
	purpose = item.getPurpose();
	reg_id = item.getRegId();
	reg_name = item.getRegName();
	reg_date = item.getRegDate();
	bom_status = item.getBomStatus();
	
%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../bm/images/blet.gif"> 임시BOM 관리</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px'>
					<a href="javascript:sendApproval();"><IMG src='../bm/images/bt_confirmyes.gif' alt='승인확정' align='absmiddle' border='0'></a>
					<a href="javascript:sendCancel();"><IMG src='../bm/images/bt_confirmno.gif' alt='승인취소' align='absmiddle' border='0'></a>
					<a href="javascript:sendList();"><img src="../bm/images/bt_cancel.gif" border='0' align='absmiddle' ></a>
					</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품코드</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pd_code%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">제품명</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pd_name%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델코드</TD>
			   <TD width="37%" height="25" class="bg_04"><%=model_code%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">모델명</TD>
			   <TD width="37%" height="25" class="bg_04"><%=model_name%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">F/G코드</TD>
			   <TD width="37%" height="25" class="bg_04"><%=fg_code%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">BOM구분</TD>
			   <TD width="37%" height="25" class="bg_04">
					<%
						String[] pp_no = {"0","1"};
						String[] pp_name = {"제조BOM","임시BOM"};
						for(int i=0; i<pp_no.length;i++) if(pp_no[i].equals(purpose)) out.println(pp_name[i]);
					%>
					</TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">과제코드</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pjt_code%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">과제명</TD>
			   <TD width="37%" height="25" class="bg_04"><%=pjt_name%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">BOM보기</TD>
			   <TD width="87%" height="25" class="bg_04" colspan='3'><a href='javascript:bomView();'><IMG src="../bm/images/bt_reg_content_view.gif" border='0' align='absmiddle' alt='등록내용보기'></a></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=10></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">등록자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=reg_id%>/<%=reg_name%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">등록일</TD>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(reg_date,"-")%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>

<FORM name="eForm" method="post" style="margin:0">
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='app_id' value='<%=sl.id%>'>
<INPUT type='hidden' name='app_name' value='<%=sl.name%>'>
<INPUT type='hidden' name='bom_status' value=''>
<INPUT type='hidden' name='app_no' value=''>
<INPUT type='hidden' name='model_code' value='<%=model_code%>'>
</FORM>

<DIV id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>


<SCRIPT LANGUAGE="javascript">
<!--

//msg 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}

//승인확정 하기
function sendApproval()
{
	var s = '<%=bom_status%>';
	if(s == '1') { alert('BOM을 등록후 진행하십시오.'); return; }

	var a = confirm('임시BOM 승인확정 상태가 됩니다. 계속하기겠습니까?');
	if(a == false) return;

	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='tbom_approval';
	document.eForm.bom_status.value='5';
	document.eForm.submit();

}

//승인취소 하기
function sendCancel()
{
	var s = '<%=bom_status%>';
	if(s != '5') { alert('승인확정후 승인취소가 가능합니다.'); return; }

	var a = confirm('임시BOM 승인취소로 BOM등록 상태가 됩니다. 계속하기겠습니까?');
	if(a == false) return;

	document.eForm.action='../servlet/BomBaseInfoServlet';
	document.eForm.mode.value='tbom_cancel';
	document.eForm.bom_status.value='3';
	document.eForm.submit();
}
//P/L등록 상세내용보기
function bomView()
{
	var pid = document.eForm.pid.value;
	var model_code = document.eForm.model_code.value; 
	var Url = "../servlet/BomBaseInfoServlet?mode=tbom_pl&pid="+pid+"&model_code="+model_code;
	wopen(Url,'tbom_pl','600','600','scrollbars=no,toolbar=no,status=no,resizable=yes');
}
//목록으로
function sendList()
{
	location.href = "../servlet/BomBaseInfoServlet?mode=tbom_list";
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
</SCRIPT>