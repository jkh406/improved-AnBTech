<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전사공통 개발단계(phase) 신규등록"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.prsPhaseDAO phaseDAO = new com.anbtech.pjt.db.prsPhaseDAO(con);
				
	//-----------------------------------
	// 작성자 정보 변수선언
	//-----------------------------------
	String user_id = "";			//작성자 사번
	String user_name = "";			//작성자 이름
	String user_rank = "";			//작성자 직급
	String div_id = "";				//작성자 부서명 관리코드
	String div_name = "";			//작성자 부서명
	String div_code = "";			//작성자 부서코드
	String code = "";				//작성자 부서Tree 관리코드

	/*********************************************************************
	 	해당자 정보 알아보기 (대상자) : 대상자 정보 [공통]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//기안자 사번
		user_name = bean.getData("name");				//기안자 명
		user_rank = bean.getData("ar_name");			//기안자 직급
		div_id = bean.getData("ac_id");					//기안자 부서명 관리코드
		div_name = bean.getData("ac_name");				//기안자 부서명 
		div_code = bean.getData("ac_code");				//기안자 부서코드
		code = bean.getData("code");					//작성자 부서Tree 관리코드
	} //while
	/*********************************************************************
	 	프로세스 개발단계 코드찾기
	*********************************************************************/
	String ph_code = "";
	ph_code = phaseDAO.getPhaseCode (user_id,div_code,"A");		//A:전사공통, D:부서공통
	connMgr.freeConnection("mssql",con);
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//등록하기
function sendSave()
{
	//등록검사
	var rtn = '';
	rtn = document.eForm.ph_code.value;
	if(rtn.length == 0) { alert('개발단계코드를 지정하십시요.');  return; }
	rtn = document.eForm.ph_name.value;
	if(rtn.length == 0) { alert('개발단계이름을 지정하십시요.');  return; }

	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../../servlet/prsCodeServlet';
	document.eForm.mode.value='PHA_WA';	
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">전사공통 개발단계[Phase]등록</TD>
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
					<TD align=left width=200><a href="javascript:sendSave();"><img src="../images/bt_add.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../images/bt_cancel.gif" border="0"></a>
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
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">개발단계코드</td>
			   <td width="80%" height="25" class="bg_04"><%=ph_code%>
					<input type='hidden' name='ph_code' value='<%=ph_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">개발단계이름</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='ph_name' value=''></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=bean.getID()%>'>
<input type='hidden' name='type' value='P'>
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
