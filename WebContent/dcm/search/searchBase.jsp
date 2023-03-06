<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "기본관리 검색"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>

<%
	com.anbtech.date.anbDate anbdt = new anbDate();	

	String ecc_subject = Hanguel.toHanguel(request.getParameter("ecc_subject"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_subject"));
	String eco_no = Hanguel.toHanguel(request.getParameter("eco_no"))==null?"":Hanguel.toHanguel(request.getParameter("eco_no"));
	String ecr_name = Hanguel.toHanguel(request.getParameter("ecr_name"))==null?"":Hanguel.toHanguel(request.getParameter("ecr_name"));
	String eco_name = Hanguel.toHanguel(request.getParameter("eco_name"))==null?"":Hanguel.toHanguel(request.getParameter("eco_name"));
	String ecr_s_date = Hanguel.toHanguel(request.getParameter("ecr_s_date"))==null?anbdt.getDate(-60):Hanguel.toHanguel(request.getParameter("ecr_s_date"));
	String ecr_e_date = Hanguel.toHanguel(request.getParameter("ecr_e_date"))==null?anbdt.getDate(0):Hanguel.toHanguel(request.getParameter("ecr_e_date"));
	String eco_s_date = Hanguel.toHanguel(request.getParameter("eco_s_date"))==null?anbdt.getDate(-30):Hanguel.toHanguel(request.getParameter("eco_s_date"));
	String eco_e_date = Hanguel.toHanguel(request.getParameter("eco_e_date"))==null?anbdt.getDate(30):Hanguel.toHanguel(request.getParameter("eco_e_date"));
	String ecc_status = Hanguel.toHanguel(request.getParameter("ecc_status"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_status"));
		
%>

<HTML>
<HEAD>
<TITLE>ECO 상세검색(기본조건)</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM name="eForm" method="get" style="margin:0" onSubmit='javascript:goSearch();'>
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center"><!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_eco_dsearch.gif" border='0' align='absmiddle' alt='ECO 상세검색'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY>
		</TABLE>
	<TR><TD height=35><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left style='padding-left:12px'>
					<SELECT name=kind_search onChange='javascript:goSearchPage();'>
						<OPTION value='searchBase'>기본관리</OPTION>
						<OPTION value='searchBase'>기본관리</OPTION>
						<OPTION value='searchCondition'>ECO 선택조건</OPTION>
						<OPTION value='searchContent'>ECO 내용검색</OPTION>
					</SELECT>
						<!--<A href="javascript:goSearch();"><IMG src='../images/bt_search3.gif' border='0' align='absmiddle'></a>-->
				</TD></TR></TBODY></TABLE></TD>
		</TR>
	<TR><TD align='middle'>
		<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
			<tbody>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
         		<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">ECO NO</TD>
					<TD width="80%" height="25" class="bg_04">
					<INPUT type="text" name="eco_no" value="<%=eco_no%>" size="15"></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">제 목</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
					<INPUT type="text" name="ecc_subject" value="<%=ecc_subject%>" size="15"></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">발의자</TD>
					<TD width="80%" height="25" class="bg_04">
					<INPUT type="text" name="ecr_name" value="<%=ecr_name%>" size="6" maxlength='12'></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">검토자</TD>
					<TD width="80%" height="25" class="bg_04">
					<INPUT type="text" name="eco_name" value="<%=eco_name%>"  size="6" maxlength='12'></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">발의일</TD>
					<TD width="80%" height="25" class="bg_04">
					<INPUT type="text" name="ecr_s_date" value="<%=ecr_s_date%>" size="10"><A Href="Javascript:OpenCalendar('ecr_s_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A> ~
					<INPUT type="text" name="ecr_e_date" value="<%=ecr_e_date%>" size="10"><A Href="Javascript:OpenCalendar('ecr_e_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">적용일</TD>
					<TD width="80%" height="25" class="bg_04">
						<INPUT type="text" name="eco_s_date" value="<%=eco_s_date%>" size="10"><A Href="Javascript:OpenCalendar('eco_s_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A> ~
						<INPUT type="text" name="eco_e_date" value="<%=eco_e_date%>" size="10"><A Href="Javascript:OpenCalendar('eco_e_date');"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">진행상태</TD>
					<TD width="80%" height="25" class="bg_04">
						<select name="ecc_status"> 
						<OPTGROUP label='---------------'>
				<%
					String[] ecc_no = {"","0","1","2","3","4","5","6","7","8","9"};
					String[] ecc_wd = {"","ECR반려","ECR작성","ECR결재","ECR책임자접수","ECR담당자접수","ECO반려","ECO작성","ECO결재","ECO승인","ECO확정"};
					String sel = "";
					for(int i=0; i<ecc_no.length; i++) {
						if(ecc_status.equals(ecc_no[i])) sel = "selected";
						else sel = "";
						out.print("<option "+sel+" value='"+ecc_no[i]+"'>"+ecc_wd[i]+"</option>");
					} 
				%></select></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD height=15 colspan="2"></TD></TR></TBODY></TABLE></TD></TR>

			<!--꼬릿말-->
			<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR>
						<TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
						<INPUT type='image' src='../images/bt_search3.gif' onfocus='blur()' align='absmiddle'>
						 <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
					</TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
	        </TBODY></TABLE>
		</TD></TR></FORM>
</TABLE>
</BODY>
</HTML>

<script language='javascript'>
<!--
//유효일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,'search_detail','180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
	//newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//상세검색 방법분기
function goSearchPage(){
	var kindOfSearch = document.eForm.kind_search.value;
	document.eForm.action = eval("'"+kindOfSearch+".jsp"+"'");
	document.eForm.submit();
}

//검색진행하기
function goSearch()
{
	var f = document.eForm;

	var ecc_subject = f.ecc_subject.value;
	var eco_no		= f.eco_no.value;
	var ecr_s_date	= f.ecr_s_date.value;
	var ecr_e_date	= f.ecr_e_date.value;
	var ecr_name	= f.ecr_name.value;
	var eco_s_date	= f.eco_s_date.value;
	var eco_e_date	= f.eco_e_date.value;
	var eco_name	= f.eco_name.value;
	var ecc_status	= f.ecc_status.value;
	var mode		= "sch_base";

	var para = "&ecc_subject="+ecc_subject+"&eco_no="+eco_no+"&ecr_s_date="+ecr_s_date+"&ecr_e_date="+ecr_e_date+"&ecr_name="+ecr_name+"&eco_s_date="+eco_s_date+"&eco_e_date="+eco_e_date+"&eco_name="+eco_name+"&ecc_status="+ecc_status;

	opener.location.href = "../../servlet/CbomHistoryServlet?mode=sch_base"+para;
	self.close();

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
