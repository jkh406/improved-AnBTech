<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "진행중노드 보기및 최근 실적내용"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjt_name="",node_code="",pjtWord="evt_note";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjt_name = para.getPjtName();
		node_code = para.getNodeCode();
		pjtWord = para.getPjtword();
	}
	if(pjtWord.length() <= 1) pjtWord = "evt_note";

	//-----------------------------------
	//	진행중인 activity노드 찾기
	//-----------------------------------
	int sel_no = 0;		//activity선택된 번호

	com.anbtech.pjt.entity.projectTable act;
	ArrayList act_list = new ArrayList();
	act_list = (ArrayList)request.getAttribute("ACT_List");
	act = new projectTable();
	Iterator act_iter = act_list.iterator();
	int act_cnt = act_list.size();
	String[][] activity = new String[act_cnt][8];
	
	int n=0;
	while(act_iter.hasNext()) {
		act = (projectTable)act_iter.next();
		activity[n][0] = act.getChildNode();	//노드코드
		activity[n][1] = act.getNodeName();		//노드명
		activity[n][2] = act.getUserId();		//담당자사번
		activity[n][3] = act.getUserName();		//담당자이름

		//진행율
		activity[n][4] = Double.toString(act.getProgress()*100+0.001);	
		activity[n][4] = activity[n][4].substring(0,activity[n][4].indexOf('.')+2);
		
		//계획기간
		activity[n][5] = anbdt.getSepDate(act.getPlanStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getPlanEndDate(),"/");

		//수정기간
		activity[n][6] = anbdt.getSepDate(act.getChgStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getChgEndDate(),"/");

		//실적기간
		activity[n][7] = anbdt.getSepDate(act.getRstStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getRstEndDate(),"/");

		n++;
	}

	//----------------------------------------------------
	//	해당과제/노드 입력된 실적내용보기
	//----------------------------------------------------
	String node_name="";
	String user_id="",user_name="",in_date="",evt_content="",evt_note="",evt_issue="";
	
	com.anbtech.pjt.entity.projectTable work;
	ArrayList work_list = new ArrayList();
	work_list = (ArrayList)request.getAttribute("WORK_List");
	work = new projectTable();
	Iterator work_iter = work_list.iterator();

	if(work_iter.hasNext()) {
		work = (projectTable)work_iter.next();
		
		pjt_code=work.getPjtCode();
		pjt_name=work.getPjtName();
		node_code=work.getNodeCode();
		node_name=work.getNodeName();
		user_id=work.getUserId();
		user_name=work.getUserName();
		in_date=work.getInDate();
		evt_content=work.getEvtContent();
		evt_note=work.getEvtNote();
		evt_issue=work.getEvtIssue();
	}

	//-----------------------------------
	//	진행중인 activity의 실적 입력일 전체LIST
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable indate;
	ArrayList indate_list = new ArrayList();
	indate_list = (ArrayList)request.getAttribute("INDATE_List");
	indate = new projectTable();
	Iterator indate_iter = indate_list.iterator();
	int indate_cnt = indate_list.size();
	String[] inputD = new String[indate_cnt];
	
	int d=0;
	while(indate_iter.hasNext()) {
		indate = (projectTable)indate_iter.next();
		inputD[d] = indate.getInDate();		//실적입력일
		d++;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectNoteServlet';
	document.sForm.mode.value='PNT_WV';
	document.sForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 해당과제의 실적내용</TD>
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
					<TD align=left width='100%'>과제명 : <%=pjt_code%> <%=pjt_name%> 
						<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String[] e_code = {"evt_content","evt_note","evt_issue"};
						String[] e_name = {"진행내용","문제점","이슈사항"};
						String esel = "";
						for(int ti=0; ti<e_code.length; ti++) {
							if(pjtWord.equals(e_code[ti])) esel = "selected";
							else esel = "";
							out.println("<option "+esel+" value='"+e_code[ti]+"'>"+e_name[ti]+"</option>");
						}
					%>
					</select>&nbsp;&nbsp;노드이름 :
					<select name="node_code" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String nsel = "";
						for(int ti=0; ti<act_cnt; ti++) {
							if(node_code.equals(activity[ti][0])) { nsel = "selected"; sel_no=ti; }
							else nsel = "";
							out.print("<option "+nsel+" value='"+activity[ti][0]+"'>");
							out.println(activity[ti][0]+" "+activity[ti][1]+"</option>");
						}
					%>
					</select>
						</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!-- 노드별 실적정보 작성 시작 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작성자</td>
			   <td width="40%" height="25" class="bg_04">
				<%=user_name%>	[진행율:<%=activity[sel_no][4]%>%]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적입력일</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="in_date" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'> 
					<%
						String dsel = "";
						for(int ti=0; ti<indate_cnt; ti++) {
							if(in_date.equals(inputD[ti])) dsel = "selected";
							else dsel = "";
							out.print("<option "+dsel+" value='"+inputD[ti]+"'>");
							out.println(inputD[ti]+"</option>");
						}
					%>
					</select>
					[<%=activity[sel_no][7]%>]</td>
			</tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">계획기간</td>
			   <td width="40%" height="25" class="bg_04"><%=activity[sel_no][5]%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">수정기간</td>
			   <td width="40%" height="25" class="bg_04"><%=activity[sel_no][6]%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			<% 	if(pjtWord.equals("evt_content")) {		%>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">진행내용</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_content' rows='5' cols='80' readonly><%=evt_content%></textarea></td>
			<% } else if(pjtWord.equals("evt_note")) {  %>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">문제점</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_note' rows='5' cols='80' readonly><%=evt_note%></textarea></td>
			<% } else if(pjtWord.equals("evt_issue")) {  %>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">이슈사항</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_issue' rows='5' cols='80' readonly><%=evt_issue%></textarea></td>
			<% } %>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='node_code' value='<%=activity[sel_no][0]%>'>
</form>

</body>
</html>
