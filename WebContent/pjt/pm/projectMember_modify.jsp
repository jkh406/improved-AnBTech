<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제인력정보 수정"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	String msd = "", med = "";
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjtWord="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
	}

	//----------------------------------
	//해당과제의 구성원 1인 정보
	//----------------------------------
	com.anbtech.pjt.entity.projectTable one;
	ArrayList one_list = new ArrayList();
	one_list = (ArrayList)request.getAttribute("ONE_List");
	one = new projectTable();
	Iterator one_iter = one_list.iterator();
	
	String[] worker = new String[13];
	if(one_iter.hasNext()) {
		one = (projectTable)one_iter.next();
		worker[0] = one.getPid();
		worker[1] = one.getPjtCode();
		worker[2] = one.getPjtName();
		worker[3] = one.getPjtMbrType();

		msd = one.getMbrStartDate();
		if(msd.length() != 0) worker[4] = msd.substring(0,4)+"/"+msd.substring(4,6)+"/"+msd.substring(6,8);
		else worker[4] = "";

		med = one.getMbrEndDate();
		if(med.length() != 0) worker[5] = med.substring(0,4)+"/"+med.substring(4,6)+"/"+med.substring(6,8);
		else worker[5] = "";

		worker[6] = Double.toString(one.getMbrPoration());
		worker[7] = one.getPjtMbrId();
		worker[8] = one.getPjtMbrName();
		worker[9] = one.getPjtMbrJob();
		worker[10] = one.getPjtMbrTel();
		worker[11] = one.getPjtMbrGrade();
		worker[12] = one.getPjtMbrDiv();
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//수정하기
function sendModify()
{
	if(document.eForm.pjt_mbr_job.value == '')		{alert('담당업무를 입력하십시요.');			return;}

	//날자에서 '/'제거
	var msd = document.eForm.mbr_start_date.value;		//계획기간 시작일
	for(i=0;i<2;i++) msd = msd.replace('/','');	
	var med = document.eForm.mbr_end_date.value;		//계획기간 완료일
	for(i=0;i<2;i++) med = med.replace('/','');	

	document.eForm.action='../servlet/projectManServlet';
	document.eForm.mode.value='PMA_M';
	document.eForm.mbr_start_date.value=msd;
	document.eForm.mbr_end_date.value=med;
	document.eForm.submit();
}
//멤버 찾기
function searchMan()
{
	window.open("../pjt/searchPM.jsp?target=eForm.pjt_member","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif">과제인력정보 수정</TD>
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
					<TD align=left width=200><a href="javascript:sendModify();"><img src="../pjt/images/bt_modify.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../pjt/images/bt_cancel.gif" border="0"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과 제 명</td>
			   <td width="80%" height="25" class="bg_04"><%=worker[1]%> <%=worker[2]%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">직 책</td>
			   <td width="80%" height="25" class="bg_04">
					<select name='pjt_mbr_type'>
				<%
					String[] grade_no = {"A","B","C","D","E","F","G"};
					String[] grade_name = {"PM","상기 PL","개발 PL","SUB PL","전담개발","지원개발","외부인력"};
					String gsel = "";
					for(int i=0; i<grade_no.length; i++) {
						if(worker[3].equals(grade_no[i])) gsel = "selected";
						else gsel = "";
						out.print("<OPTION "+gsel+" value='"+grade_no[i]+"'>");
						out.println(grade_name[i]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">개발자 이름</td>
			   <td width="80%" height="25" class="bg_04"><%=worker[7]%>/<%=worker[8]%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">시 작 일</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='mbr_start_date' value='<%=worker[4]%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('mbr_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">완 료 일</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='mbr_end_date' value='<%=worker[5]%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('mbr_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">투 입 율</td>
			   <td width="80%" height="25" class="bg_04">
					<select name='mbr_poration'>
				<%
					String[] por = {"0.1","0.2","0.3","0.4","0.5","0.6","0.7","0.8","0.9","1.0"};
					String psel = "";
					for(int i=0; i<por.length; i++) {
						if(worker[6].equals(por[i])) psel = "selected";
						else psel = "";
						out.print("<OPTION "+psel+" value='"+por[i]+"'>");
						out.println(por[i]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">담당 업무</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='pjt_mbr_job' value='<%=worker[9]%>'></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pjt_code' value='<%=worker[1]%>'>
<input type='hidden' name='pjt_name' value='<%=worker[2]%>'>
<input type='hidden' name='pjtWord' value='<%=pjtWord%>'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>

<input type='hidden' name='pid' value='<%=worker[0]%>'>
<input type='hidden' name='pjt_mbr_id' value='<%=worker[7]%>'>
<input type='hidden' name='pjt_mbr_name' value='<%=worker[8]%>'>
<input type='hidden' name='pjt_mbr_tel' value='<%=worker[10]%>'>
<input type='hidden' name='pjt_mbr_grade' value='<%=worker[11]%>'>
<input type='hidden' name='pjt_mbr_div' value='<%=worker[12]%>'>
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
