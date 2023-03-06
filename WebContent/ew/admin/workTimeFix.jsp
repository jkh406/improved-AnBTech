<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.date.anbDate,com.anbtech.text.Hanguel"
%>

<%
	anbDate anbdt = new com.anbtech.date.anbDate();		
	String hrs = anbdt.getTime();	
	String c_date = anbdt.getDateNoformat();
	String div = request.getParameter("div");

	StandardWorkTimeTable swtimeTable =  new StandardWorkTimeTable();
	swtimeTable = (StandardWorkTimeTable)request.getAttribute("workTime");
	
	String overday_n	 = swtimeTable.getOverDayN();	// [평일]금일/ 명일 구분자 (t:금일 m:명일)
	String overday_h	 = swtimeTable.getOverDayH();	// [휴일]금일/ 명일 구분자 (t:금일 m:명일)
	String overday_s	 = swtimeTable.getOverDayS();	// [토요일]금일/ 명일 구분자 (t:금일 m:명일)
%>

<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK rel="stylesheet" href="../ew/css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ew/images/blet.gif"> 기준 근무시간 설정</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR><TD align=left width=5 ></TD>
				<TD align=left width=500>
<%
		  if("first".equals(div)) {
%>				<a href="javascript:go_change()"><IMG src="../ew/images/bt_add_new2.gif" align="absmiddle"></a>
<%		  } else if("wtime_view".equals(div)) {
%>				<a href="javascript:go_change()"><IMG src="../ew/images/bt_modify.gif" align="absmiddle" border="0"></a>
<%		  } else if("wtime_input".equals(div)) {
%>				<a href="javascript:go_recording()"><IMG src="../ew/images/bt_save.gif" align="absmiddle" border='0'></a>
				<a href="javascript:history.back()"><IMG src="../ew/images/bt_cancel.gif" align="absmiddle" style="CURSOR: hand" border='0'></a>
<%		  }
%>		  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<FORM method="post" name="viewForm" action='../servlet/ExtraWorkServlet' style="margin:0">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
    <!--기본정보-->

<%	if("wtime_view".equals(div)) {
%>
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="7"></TD></TR>
			<TR><TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">평일근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getFixStime()%> ~ <%=swtimeTable.getFixEtime()%></TD>
				<TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">평일연장근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getOffStime()%> ~ <%=swtimeTable.getOverDayNName()%> <%=swtimeTable.getOffEtime()%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="7"></TD></TR>
			<TR><TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">토요일근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getFixStimeSat()%> ~ <%=swtimeTable.getFixEtimeSat()%></TD>
				<TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">토요일연장근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getOffStimeSat()%> ~ <%=swtimeTable.getOverDaySName()%> <%=swtimeTable.getOffEtimeSat()%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="7"></TD></TR>
			<TR><TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">휴일근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getFixStimeHoliday()%> ~ <%=swtimeTable.getFixEtimeHoliday()%></TD>
				<TD width="17%" height="25" class="bg_03" background="../ew/images/bg-01.gif">휴일연장근무시간</TD>
				<TD width="33%" height="25" class="bg_04"><%=swtimeTable.getOffStimeHoliday()%> ~ <%=swtimeTable.getOverDayHName()%> <%=swtimeTable.getOffEtimeHoliday()%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="7"></TD></TR></TBODY></TABLE>

<%	// 근무 기준 시간 등록
	} else if("wtime_input".equals(div) || "first".equals(div)) {
%>
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%" height="25" class="bg_03" background="../ew/images/bg-01.gif">평일근무시간</TD>
				<TD width="35%" height="25" class="bg_04">
					<SELECT name="fix_stime">
<%
					String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=7; asH<11; asH++){
						out.println(" <OPTION " + msSEL + " value='"+asHour[asH]+":00'>" + asHour[asH] + ":" + "00");
						out.println(" <OPTION value='"+asHour[asH]+":30'>" + asHour[asH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
%>

<%	if(swtimeTable.getFixStime() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_stime.value = '<%=swtimeTable.getFixStime()%>';
		</script>
<%	}	%>	
					<SELECT name="fix_etime">
<%
					String[] asHour_1 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL_1 = "";
					for(int asH=12; asH<24; asH++){
						out.println(" <OPTION " + msSEL_1 + " value='"+asHour_1[asH]+":00'>" + asHour_1[asH] + ":" + "00");
						out.println(" <OPTION value='"+asHour_1[asH]+":30'>" + asHour_1[asH] + ":" + "30");
					}
					out.println("</SELECT>");
%>
				</TD>
<%	if(swtimeTable.getFixEtime() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_etime.value = '<%=swtimeTable.getFixEtime()%>';
		</script>
<%	}	%>
				<TD width="16%" height="25" class="bg_03" background="../ew/images/bg-01.gif">평일연장근무시간</TD>
				<TD width="34%" height="25" class="bg_04">
					<SELECT name="off_stime">
<%					String[] asHour3 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL3 = "";
					
					for(int asH=7; asH<24; asH++){
						out.println(" <OPTION " + msSEL3 + " value='"+asHour3[asH]+":00'>" + asHour3[asH] + ":" + "00");
						out.println(" <OPTION value='"+asHour3[asH]+":30'>" + asHour3[asH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
%>
<%	if(swtimeTable.getOffStime() != null){	%>
		<script language='javascript'>
			document.viewForm.off_stime.value = '<%=swtimeTable.getOffStime()%>';
		</script>
<%	}	%>
					<SELECT name='overday_n'>
						<OPTION value='d'>당일</OPTION>
						<OPTION value='t'>명일</OPTION>
					</SELECT>
<%	if(swtimeTable.getOverDayN() != null){	%>
		<script language='javascript'>
			document.viewForm.overday_n.value = '<%=swtimeTable.getOverDayN()%>';
		</script>
<%	}	%>

					<SELECT name="off_etime">
<%					String[] asHour4 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL4 = "";
	
					for(int asH2=0; asH2<24; asH2++){
						out.println("<OPTION " + msSEL4 + " value='"+asHour4[asH2]+":00'>" + asHour4[asH2] + ":" + "00");
						out.println("<OPTION value='"+asHour4[asH2]+":30'>" + asHour4[asH2] + ":" + "30");
					}
					out.println("</SELECT>");
%>					</TD></TR>
<%	if(swtimeTable.getOffEtime() != null){	%>
		<script language='javascript'>
			document.viewForm.off_etime.value = '<%=swtimeTable.getOffEtime()%>';
		</script>
<%	}	%>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
			<TR><TD width="15%" height="25" class="bg_03" background="../ew/images/bg-01.gif">토요일근무시간</TD>
				<TD width="35%" height="25" class="bg_04">
					<SELECT name="fix_stime_sat">
<%
					String[] asHour2 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL2 = "";
					
					for(int asH2=7; asH2<11; asH2++){
						out.println("<OPTION " + msSEL2 + " value='"+asHour2[asH2]+":00'>" + asHour2[asH2] + ":" + "00");
						out.println("<OPTION value='"+asHour2[asH2]+":30'>" + asHour2[asH2] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
%>
<%	if(swtimeTable.getFixStimeSat() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_stime_sat.value = '<%=swtimeTable.getFixStimeSat()%>';
		</script>
<%	}	%>
					<SELECT name="fix_etime_sat">
<%					String[] asHour2_1 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL2_1 = "";
		
					for(int asH2=12; asH2<19; asH2++){
						out.println("<OPTION " + msSEL2_1 + " value='"+asHour2_1[asH2]+":00'>" + asHour2_1[asH2] + ":" + "00");
						out.println("<OPTION value='"+asHour2_1[asH2]+":30'>" + asHour2_1[asH2] + ":" + "30");
					}
					out.println("</SELECT>");
%>
<%	if(swtimeTable.getFixEtimeSat() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_etime_sat.value = '<%=swtimeTable.getFixEtimeSat()%>';
		</script>
<%	}	%>
				</TD>
				<TD width="14%" height="25" class="bg_03" background="../ew/images/bg-01.gif">토요일연장근무시간</TD>
				<TD width="36%" height="25" class="bg_04">
				
				<SELECT name="off_stime_sat">
<%				String[] asHour5 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL5 = "";
					
				for(int asH=7; asH<16; asH++){
					out.println(" <OPTION " + msSEL5 + " value='"+asHour5[asH]+":00'>" + asHour5[asH] + ":" + "00");
					out.println(" <OPTION value='"+asHour5[asH]+":30'>" + asHour5[asH] + ":" + "30");
				}
				out.println("</SELECT> ~ ");
%>
<%	if(swtimeTable.getOffStimeSat() != null){	%>
		<script language='javascript'>
			document.viewForm.off_stime_sat.value = '<%=swtimeTable.getOffStimeSat()%>';
		</script>
<%	}	%>
				<SELECT name='overday_s'>
					<OPTION value='d'>당일</OPTION>
					<OPTION value='t'>명일</OPTION>
				</SELECT>
<%	if(swtimeTable.getOverDayS() != null){	%>
		<script language='javascript'>
			document.viewForm.overday_s.value = '<%=swtimeTable.getOverDayS()%>';
		</script>
<%	}	%>

				<SELECT name="off_etime_sat">
<%				String[] asHour6 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL6 = "";

				for(int asH2=12; asH2<24; asH2++){
					out.println("<OPTION " + msSEL6 + " value='"+asHour6[asH2]+":00'>" + asHour6[asH2] + ":" + "00");
					out.println("<OPTION value='"+asHour6[asH2]+":30'>" + asHour6[asH2] + ":" + "30");
				}
				out.println("</SELECT>");
%>				</TD></TR>
<%	if(swtimeTable.getOffEtimeSat() != null){	%>
		<script language='javascript'>
			document.viewForm.off_etime_sat.value = '<%=swtimeTable.getOffEtimeSat()%>';
		</script>
<%	}	%>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%" height="25" class="bg_03" background="../ew/images/bg-01.gif">휴일근무시간</TD>
				<TD width="35%" height="25" class="bg_04">
				<SELECT name="fix_stime_holiday">
<%				String[] asHour7 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL7 = "";
				
				for(int asH=7; asH<10; asH++){
					out.println(" <OPTION " + msSEL7 + " value='"+asHour7[asH]+":00'>" + asHour7[asH] + ":" + "00");
					out.println(" <OPTION value='"+asHour7[asH]+":30'>" + asHour7[asH] + ":" + "30");
				}
				out.println("</SELECT> ~ ");
%>
<%	if(swtimeTable.getFixStimeHoliday() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_stime_holiday.value = '<%=swtimeTable.getFixStimeHoliday()%>';
		</script>
<%	}	%>
				<SELECT name="fix_etime_holiday">
<%
				String[] asHour8 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL8 = "";
				
				for(int asH2=7; asH2<19; asH2++){
					out.println("<OPTION " + msSEL8 + " value='"+asHour8[asH2]+":00'>" + asHour8[asH2] + ":" + "00");
					out.println("<OPTION value='"+asHour8[asH2]+":30'>" + asHour8[asH2] + ":" + "30");
				}
				out.println("</SELECT>");
%>
<%	if(swtimeTable.getFixEtimeHoliday() != null){	%>
		<script language='javascript'>
			document.viewForm.fix_etime_holiday.value = '<%=swtimeTable.getFixEtimeHoliday()%>';
		</script>
<%	}	%>
	           <TD width="14%" height="25" class="bg_03" background="../ew/images/bg-01.gif">휴일연장근무시간</TD>
		       <TD width="36%" height="25" class="bg_04"><SELECT name="off_stime_holiday">
<%
				String[] asHour9 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL9 = "";

				for(int asH=7; asH<24; asH++){
					out.println(" <OPTION " + msSEL9 + " value='"+asHour9[asH]+":00'>" + asHour9[asH] + ":" + "00");
					out.println(" <OPTION value='"+asHour9[asH]+":30'>" + asHour9[asH] + ":" + "30");
				}
				out.println("</SELECT> ~ ");
%>
<%	if(swtimeTable.getOffStimeHoliday() != null){	%>
		<script language='javascript'>
			document.viewForm.off_stime_holiday.value = '<%=swtimeTable.getOffStimeHoliday()%>';
		</script>
<%	}	%>
				<SELECT name='overday_h'>
					<OPTION value='d'>당일</OPTION>
					<OPTION value='t'>명일</OPTION>
				</SELECT>
<%	if(swtimeTable.getOverDayH() != null){	%>
		<script language='javascript'>
			document.viewForm.overday_h.value = '<%=swtimeTable.getOverDayH()%>';
		</script>
<%	}	%>
				<SELECT name="off_etime_holiday">
<%
				String[] asHour10 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL10 = "";
				
				for(int asH2=0; asH2<23; asH2++){
					out.println("<OPTION " + msSEL10 + " value='"+asHour10[asH2]+":00'>" + asHour10[asH2] + ":" + "00");
					out.println("<OPTION value='"+asHour10[asH2]+":30'>" + asHour10[asH2] + ":" + "30");
				}
				out.println("</SELECT>");
%>				</TD></TR>
<%	if(swtimeTable.getOffEtimeHoliday() != null){	%>
		<script language='javascript'>
			document.viewForm.off_etime_holiday.value = '<%=swtimeTable.getOffEtimeHoliday()%>';
		</script>
<%	}	%>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TBODY></TABLE>
		<input type='hidden' name='modify_date' value='<%=c_date%>'>
		<input type='hidden' name='mode' value='standard_wtime_save'>
<%
		}
%>


</FORM>
</TD></TR></TABLE>
</BODY>
</HTML>

<SCRIPT language="javascript">
	
function go_recording(){
	var f = document.viewForm
	if(confirm("기준 근무시간 정보를 변경하시겠습니까?")) f.submit();
}

function go_change(){
  location.href ="../servlet/ExtraWorkServlet?mode=standard_wtime_fix&div=wtime_input";
}

</SCRIPT>

