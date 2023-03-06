<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 기본정보 결재상신 내용보기"		
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
	String sel_date = anbdt.getDateNoformat();
	String pid="",model_code="",model_name="",fg_code="",pd_code="",pd_name="";
	String pjt_code="",pjt_name="",reg_id="",reg_name="",reg_date="",purpose="";

	com.anbtech.bm.entity.mbomMasterTable item;
	item = (mbomMasterTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	reg_name = item.getRegName();
	reg_id = item.getRegId();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();
	pd_code = item.getPdCode();
	pd_name = item.getPdName();
	pjt_code = item.getPjtCode();
	pjt_name = item.getPjtName();
	reg_date = item.getRegDate();
	purpose = item.getPurpose();

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//msg 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	var n = 0;
	for(i=0; i<msg.length; i++) if(msg.charAt(i) == '|') n++;
	for(i=0; i<n; i++) msg = msg.replace('|','\n');
	alert(msg);
	history.back(-1);
}

//BOM내용 보기
function viewBom()
{
	var pid = '<%=pid%>';
	var model_code = '<%=model_code%>';
	//var para = "strSrc=../servlet/BomBaseInfoServlet&mode=info_bomview&pid="+pid;
	//para += "&model_code="+model_code+"&width=840&height=520";	//showModalDialog('../bm/modalFrm.jsp?'+para,'view','dialogWidth:855px;dialogHeight:550px;toolbar=0;scrollBars=auto;status=no;resizable=no');

	var para = "../servlet/BomBaseInfoServlet?mode=info_bomview&pid="+pid+"&model_code="+model_code;
	wopen(para,"bomview","700","600","scrollbars=no,toolbar=no,status=no,resizable=yes");
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;
	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>
<BODY topmargin="0" leftmargin="0"  oncontextmenu="return false">
<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
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
			   <TD width="13%" height="25" class="bg_03" background="../bm/images/bg-01.gif">첨부</TD>
			   <TD width="37%" height="25" class="bg_04"><a href='Javascript:viewBom();'><img src='../bm/images/bt_bom_info.gif' alt='BOM구조확인' align='absmiddle' border='0'></a></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height="10"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>			
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">등록자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=reg_id%>/<%=reg_name%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../crm/images/bg-01.gif">등록일</TD>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(reg_date,"-")%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>

<FORM name="sForm" method="post" style="margin:0">
<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
</FORM>

</BODY>
</HTML>
