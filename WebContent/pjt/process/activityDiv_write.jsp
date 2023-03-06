<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부서공통 실행항목(activity) 신규등록"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.util.normalFormat"
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
	 	프로세스 개발단계[phase] 전체List
	*********************************************************************/
	String[] phaseColumn = {"ph_code","ph_name"};
	bean.setTable("prs_phase");		
	bean.setColumns(phaseColumn);
	query = " where type='"+div_code+"' order by ph_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int pcnt = bean.getTotalCount();
	String[][] phase = new String[pcnt][2];
	int p=0;
	while(bean.isAll()) {
		phase[p][0] = bean.getData("ph_code");
		phase[p][1] = bean.getData("ph_name");
		p++;
	}

	/*********************************************************************
	 	프로세스 관리항목[step] 전체List
	*********************************************************************/
	String ph_code = request.getParameter("ph_code");
	if(ph_code == null) ph_code = "D01";

	String[] stepColumn = {"step_code","step_name"};
	bean.setTable("prs_step");		
	bean.setColumns(stepColumn);
	query = " where type='"+div_code+"' and ph_code='"+ph_code+"' order by step_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int scnt = bean.getTotalCount();
	if(scnt == 0) {
		out.println("<script>");
		out.println("alert('해당되는 관리항목내용가 없습니다. 먼저 등록후 진행하십시요.');");
		out.println("history.back();");
		out.println("</script>");
		return;
	}
	String[][] step = new String[scnt][2];
	int s=0;
	while(bean.isAll()) {
		step[s][0] = bean.getData("step_code");
		step[s][1] = bean.getData("step_name");
		s++;
	}
	/*********************************************************************
	 	내부처리및 예상되는 activity코드 찾기
	*********************************************************************/
	String step_code = request.getParameter("step_code");
	if(step_code == null) step_code = "N01";

	//step선택후 phase선택시 ph_code clear
	String save_ph_code = request.getParameter("save_ph_code");	
	if(save_ph_code == null) save_ph_code = "C";
	if(save_ph_code.equals("C")) step_code = step[0][0];

	String[] actColumn = {"ph_code","step_code","act_code"};
	bean.setTable("prs_activity");		
	bean.setColumns(actColumn);
	query = " where type='"+div_code+"' and ph_code='"+ph_code+"' and step_code='"+step_code+"' order by act_code DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	String act_code = step_code+"00";
	if(bean.isAll()) act_code = bean.getData("act_code");

	//하부구조 구성후 신규로 step code가 알파벳이 추가된 경우 예,N01A N03B ....
	int num = 0;
	if(act_code.length() == 5) num = Integer.parseInt(act_code.substring(3,5))+1;
	else num = Integer.parseInt(act_code.substring(4,6))+1;

	normalFormat fmt = new com.anbtech.util.normalFormat("00");				
	act_code = step_code+fmt.toDigits(num);

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
	rtn = document.eForm.act_name.value;
	if(rtn.length == 0) { alert('실행항목이름을 등록하십시요.');  return; }

	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../../servlet/prsCodeServlet';
	document.eForm.mode.value='ACT_WD';	
	document.eForm.submit();
}
//개발단계명 선택하여 관리항목코드 신규생성
function selectPhCode()
{
	document.eForm.action="activityDiv_write.jsp";
	document.eForm.save_ph_code.value="C";
	document.eForm.submit();
}
//관리항목명 선택하여 실행항목코드 신규생성
function selectStepCode()
{
	document.eForm.action="activityDiv_write.jsp";
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">부서공통 실행항목[Activity]등록</TD>
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
					<input type='hidden' name='save_ph_code' value=''>
					<select name='ph_code' onChange="javascript:selectPhCode();">
					<% 
					String psel = "";
					for(int i=0; i<pcnt; i++) {
						if(ph_code.equals(phase[i][0])) psel = " selected ";
						else psel = "";
						out.println("<OPTION value='"+phase[i][0]+"' "+psel+">"+phase[i][0]+" "+phase[i][1]+"</OPTION>");
					}
					%>
					</select>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">관리항목이름</td>
			   <td width="80%" height="25" class="bg_04">	
			    <select name='step_code' onChange="javascript:selectStepCode();">
				<% 
					String sel = "";
					for(int i=0; i<scnt; i++) {
						if(step_code.equals(step[i][0])) sel = " selected ";
						else sel = "";
						out.println("<OPTION value='"+step[i][0]+"' "+sel+">"+step[i][0]+" "+step[i][1]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">실행항목코드</td>
			   <td width="80%" height="25" class="bg_04"><%=act_code%>
					<input type='hidden' name='act_code' value='<%=act_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">실행항목이름</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' size=30 name='act_name' value=''></td>
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
<input type='hidden' name='type' value='<%=div_code%>'>
<input type='hidden' name='tag' value='D'>    <%//A:전사공통, D:부서공통%>
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
