<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "노드 작업일지 작성하기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//---------------------------------------------------------
	//	파라미터 받기
	//--------------------------------------------------------
	String pjt_code="",pjt_name="",node_code="",pjtWord="",sItem="",sWord="",node_status="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();				//프로젝트코드
		pjt_name = para.getPjtName();				//프로젝트명
		node_code = para.getNodeCode();				//노드코드
		pjtWord = para.getPjtword();				//주간[W]/월간[M] 구분
		sItem = para.getSitem();		
		sWord = para.getSword();
		node_status = para.getNodeStatus();			//노드 상태
	}

	//----------------------------------------------------
	//	해당과제/노드의 기초 입력정보 읽기 
	//----------------------------------------------------
	String node_name="",progress="0";
	String psd="",ped="",csd="",ced="",rsd="",red="";		//DB상의 계획일,수정일,완료일
	String dpsd="",dped="",dcsd="",dced="",drsd="",dred="";	//화면출력용

	com.anbtech.pjt.entity.projectTable node;
	ArrayList node_list = new ArrayList();
	node_list = (ArrayList)request.getAttribute("NODE_List");
	node = new projectTable();
	Iterator node_iter = node_list.iterator();
	
	if(node_iter.hasNext()) {
		node = (projectTable)node_iter.next();
		node_name = node.getNodeName();

		psd = node.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0) dpsd = psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else dpsd = "";
		ped = node.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0) dped = ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else dped = "";

		csd = node.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0) dcsd = csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else dcsd = "";
		ced = node.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0) dced = ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else dced = "";

		rsd = node.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = "";
		red = node.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = "";

		progress = Double.toString(node.getProgress()*100+0.001);
		progress = progress.substring(0,progress.indexOf('.')+2);
	}

	//요일구하기
	int yy = Integer.parseInt(anbdt.getYear());
	int mm = Integer.parseInt(anbdt.getMonth());
	int dd = Integer.parseInt(anbdt.getDates());
	int day = anbdt.getDay(yy,mm,dd);
	String days = "일";
	if(day == 1) days = "일";	else if(day == 2) days = "월";	else if(day == 3) days = "화";
	else if(day == 4) days = "수";	else if(day == 5) days = "목";	else if(day == 6) days = "금";
	else if(day == 7) days = "토";
	days = days + "요일";
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//실적 입력하기
function sendSave()
{
	var evt_content = document.eForm.evt_content.value;
	if(evt_content < 10) { alert('진행내용을 입력하십시요.'); return; }

	var progress = document.eForm.progress.value;
	if(isNaN(progress)) { alert('숫자로 입력하십시요.'); return; }

	if(progress > 100) { alert('100%을 넘어갈수 없습니다.'); return; }
	else if(progress <= 0){ alert('진행율을 입력하십시요.'); return; } 
	else progress = progress/100;

	document.eForm.action='../servlet/projectEventServlet';
	document.eForm.mode.value='PSM_EW';
	document.eForm.progress.value=progress;
	document.eForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 해당노드 실적입력</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width='80%'>과제명 : <%=pjt_code%> <%=pjt_name%> </TD>
					<TD align=left width='20%'><a href="javascript:sendSave();"><img src="../pjt/images/bt_add.gif" border=0></a>
					<a href="javascript:history.go(-1);"><img src="../pjt/images/bt_cancel.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">노드이름</td>
			   <td width="40%" height="25" class="bg_04"><%=node_code%> <%=node_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작성자</td>
			   <td width="40%" height="25" class="bg_04"><%=login_name%>
					<input type='hidden' name='user_id' value='<%=login_id%>'>
					<input type='hidden' name='user_name' value='<%=login_name%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">계획기간</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">수정기간</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적기간</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='<%=drsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=dred%>' size=10 readonly></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">입력구분</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="wm_type" style=font-size:9pt;color="black";>  
					<%
						String[] s_code = {"W","M"};
						String[] s_name = {"주간실적","월간실적"};
						for(int ti=0; ti<s_code.length; ti++) {
							out.println("<option value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select>&nbsp;&nbsp;&nbsp;
					진행율 <input type="text" name="progress" size="3" value="<%=progress%>">%
					&nbsp;&nbsp;&nbsp;[<%=anbdt.getDate()%> <%=days%>]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">진행내용</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_content' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">문제점</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_note' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">이슈사항</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_issue' rows='7' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value=''>
<input type='hidden' name='in_date' value='<%=anbdt.getDate()%>'>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='node_code' value='<%=node_code%>'>
<input type='hidden' name='node_name' value='<%=node_name%>'>
<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>
<input type='hidden' name='node_status' value='<%=node_status%>'>

</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
