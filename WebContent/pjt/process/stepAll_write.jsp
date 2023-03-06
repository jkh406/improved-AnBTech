<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전사공통 관리항목(step) 신규등록"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.util.normalFormat"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%			
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
	 	프로세스 개발단계 전체List
	*********************************************************************/
	String[] phaseColumn = {"ph_code","ph_name"};
	bean.setTable("prs_phase");		
	bean.setColumns(phaseColumn);
	query = " where type='P' order by ph_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] phase = new String[cnt][2];
	int n=0;
	while(bean.isAll()) {
		phase[n][0] = bean.getData("ph_code");
		phase[n][1] = bean.getData("ph_name");
		n++;
	}

	/*********************************************************************
	 	내부처리및 예상되는 step코드 찾기
	*********************************************************************/
	String ph_code = request.getParameter("ph_code");
	if(ph_code == null) ph_code = "S01";

	String[] stepColumn = {"ph_code","step_code"};
	bean.setTable("prs_step");		
	bean.setColumns(stepColumn);
	query = " where type='P' and ph_code='"+ph_code+"' order by step_code DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	String step_code = "N00";
	if(bean.isAll()) step_code = bean.getData("step_code");
	String tmp_step_code = step_code;	//임시 보관

	if(step_code.length() == 3) {		//알파벳 확장코드가 아니면... +1을 진행
		int num = Integer.parseInt(step_code.substring(1,3))+1;
		normalFormat fmt = new com.anbtech.util.normalFormat("00");				
		step_code = "N"+fmt.toDigits(num);
	}

	/*********************************************************************
	 	내부처리및 예상되는 step코드을 사용할 수 있는지 판단하기
		(예상 Step Code가 하부구조에 구성되었나 확인하기)
	*********************************************************************/
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

	//하부구조에서 사용되었나 판단하기
	String use = "N";					//하부구조에 구성되었나 확인하기 (Y:구성됨  N:구성안됨)
	if(tmp_step_code.length() == 3)		//알파벳 확장코드가 없는경우 (예,N04A N04B ...)
		use = stepDAO.useStepAtActivity (step_code,"P");		//P:전사공통, 부서코드:부서공통

	//다시 step code을구하기
	if(use.equals("Y")) {											//사용됨
		step_code = stepDAO.searchStepCode(ph_code,tmp_step_code,"P");
	}
	else if(use.equals("N") && (tmp_step_code.length() == 4)) {		//알파벳 확장코드가 있는경우는 무조건
		step_code = stepDAO.searchStepCode(ph_code,tmp_step_code,"P");
	}
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
	if(rtn.length == 0) { alert('개발단계이름을 지정하십시요.');  return; }
	rtn = document.eForm.step_name.value;
	if(rtn.length == 0) { alert('관리항목이름을 지정하십시요.');  return; }

	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../../servlet/prsCodeServlet';
	document.eForm.mode.value='STP_WA';	
	document.eForm.submit();
}
//개발단계명 선택하여 관리항목코드 신규생성
function selectPhCode()
{
	document.eForm.action="stepAll_write.jsp";
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">전사공통 관리항목[Step]등록</TD>
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
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">개발단계이름</td>
			   <td width="80%" height="25" class="bg_04">
				<select name='ph_code' onChange="javascript:selectPhCode();">
				<% 
					String sel = "";
					for(int i=0; i<cnt; i++) {
						if(ph_code.equals(phase[i][0])) sel = " selected ";
						else sel = "";
						out.println("<OPTION value='"+phase[i][0]+"' "+sel+">"+phase[i][0]+" "+phase[i][1]+"</OPTION>");
					}
				%>
				</select>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리항목코드</td>
			   <td width="80%" height="25" class="bg_04"><%=step_code%>
					<input type='hidden' name='step_code' value='<%=step_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리항목이름</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='step_name' value=''></td>
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
<input type='hidden' name='tag' value='A'>    <%//A:전사공통, D:부서공통%>
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
