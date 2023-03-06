<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "노드 작업일지 내용보기"		
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

	//----------------------------------------------------
	//	해당과제/노드 입력된 실적내용보기
	//----------------------------------------------------
	String pjt_code="",pjt_name="",node_code="",node_name="";
	String user_id="",user_name="",wm_type="",in_date="",evt_content="",evt_note="",evt_issue="",remark="";
	String days = "일";
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
		wm_type=work.getWmType();
		in_date=work.getInDate();
		evt_content=work.getEvtContent();
		evt_note=work.getEvtNote();
		evt_issue=work.getEvtIssue();
		remark=work.getRemark();
	}

	//요일구하기
	int yy = Integer.parseInt(in_date.substring(0,4));
	int mm = Integer.parseInt(in_date.substring(5,7));
	int dd = Integer.parseInt(in_date.substring(8,10));
	int day = anbdt.getDay(yy,mm,dd);
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

-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 해당노드 실적내용보기</TD>
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
					<TD align=left width='100%'>과제명 : <%=pjt_code%> <%=pjt_name%> </TD>
					<TD align=left width=200><a href="javascript:self.close();"><img src="../pjt/images/bt_close.gif" border="0"></a>
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
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">노드이름</td>
			   <td width="40%" height="25" class="bg_04"><%=node_code%> <%=node_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작성자</td>
			   <td width="40%" height="25" class="bg_04"><%=user_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">입력구분</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<%
					if(wm_type.equals("W")) out.println("주간실적");
					else if(wm_type.equals("M")) out.println("월간실적");
					else if(wm_type.equals("N")) out.println("승인요청");
					else if(wm_type.equals("A")) out.println("노드승인");
					else if(wm_type.equals("R")) out.println("노드반려");
				%>
				&nbsp;&nbsp;&nbsp;&nbsp;[<%=in_date%> <%=days%>]
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">진행내용</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_content' rows='7' cols='80' readonly><%=evt_content%></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">문제점</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_note' rows='7' cols='80' readonly><%=evt_note%></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">이슈사항</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_issue' rows='7' cols='80' readonly><%=evt_issue%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<%  if(wm_type.equals("A")) {				//승인 %>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">PM승인의견</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_issue' rows='7' cols='80' readonly><%=remark%></textarea></td>
			</tr>
			<% }else if(wm_type.equals("R")) {			//반려 %>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">PM반려의견</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
				<textarea name='evt_issue' rows='7' cols='80' readonly><%=remark%></textarea></td>
			</tr>
			<% } %>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>
