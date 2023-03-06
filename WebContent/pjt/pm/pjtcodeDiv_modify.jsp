<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부서공통 과제코드 수정"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.pjt.entity.*"
%>

<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	com.anbtech.pjt.entity.pjtCodeTable table;
	String pid = "";
	String pjt_code = "";
	String pjt_name = "";
	String in_date = "";
	String mgr_id = "";
	String mgr_name = "";
	String type = "";
	String pjt_status = "";

	//-----------------------------------
	//	공문공지 내용 & 전체 갯수 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new pjtCodeTable();
	Iterator table_iter = table_list.iterator();

	if(table_iter.hasNext()){
		table = (pjtCodeTable)table_iter.next();

		pid=table.getPid();							//관리번호
		pjt_code=table.getPjtCode();				//project code
		pjt_name=table.getPjtName();				//project name
		in_date=table.getInDate();					//등록일
		mgr_id=table.getMgrId();					//등록자 사번
		mgr_name=table.getMgrName();				//등록자 이름
		type=table.getType();						//P:전사공통, 부서코드:부서공통
		pjt_status=table.getPjtStatus();			//project 상태
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
	//수정검사
	var rtn = '';
	rtn = document.eForm.pjt_name.value;
	if(rtn.length == 0) { alert('과제이름을 지정하십시요.');  return; }

	//코드검사하기
	
	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../servlet/pjtCodeServlet';
	document.eForm.mode.value='PJC_MD';	
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif">과제명 수정</TD>
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
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과제코드</td>
			   <td width="80%" height="25" class="bg_04"><%=pjt_code%>
					<input type='hidden' name='pjt_code' value='<%=pjt_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과제이름</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='pjt_name' value='<%=pjt_name%>' size='60'></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='mgr_id' value='<%=mgr_id%>'>
<input type='hidden' name='mgr_name' value='<%=mgr_name%>'>
<input type='hidden' name='type' value='<%=type%>'>
<input type='hidden' name='pjt_status' value='<%=pjt_status%>'>
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
