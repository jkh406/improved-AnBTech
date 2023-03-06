<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	="java"
	contentType	="text/html;charset=euc-kr"
	import="java.util.*,com.anbtech.ew.entity.*,com.anbtech.admin.entity.*"
%>
<%!
	ExtraWorkHistoryTable ew_table;
	ApprovalInfoTable app_table;
%>

<%
	//결재정보
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig()==null?"&nbsp;":app_table.getWriterSig();
	String writer_name		= app_table.getWriterName()==null?"&nbsp;":app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig()==null?"&nbsp;":app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName()==null?"&nbsp;":app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig()==null?"&nbsp;":app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName()==null?"&nbsp;":app_table.getDecisionName();
	String memo				= app_table.getMemo()==null?"&nbsp;":app_table.getMemo();

	//특근정보
	ew_table = new ExtraWorkHistoryTable();
	ew_table = (ExtraWorkHistoryTable)request.getAttribute("EW_INFO");

	String member_id		= ew_table.getMemberId();
	String member_name		= ew_table.getMemberName();
	String rank_name		= ew_table.getMemberRankName();
	String division_name	= ew_table.getDivisionName();
	String w_sdate			= ew_table.getWsdate();
	w_sdate = w_sdate.substring(0,4) + "-" + w_sdate.substring(4,6) + "-" + w_sdate.substring(6,8);
	String w_stime			= ew_table.getWstime();
	String w_edate			= ew_table.getWedate();
	w_edate = w_edate.substring(0,4) + "-" + w_edate.substring(4,6) + "-" + w_edate.substring(6,8);
	String w_etime			= ew_table.getWetime();
	if(!w_sdate.equals(w_edate)) w_etime = "명일 " + w_etime;
	String c_date			= ew_table.getCdate();
	c_date = c_date.substring(0,4) + "-" + c_date.substring(4,6) + "-" + c_date.substring(6,8);
	String r_sdate			= ew_table.getRsdate();
	String r_stime			= ew_table.getRstime();
	String r_edate			= ew_table.getRedate();
	String r_etime			= ew_table.getRetime();
	String total_time		= ew_table.getTotalTime();
	String result_time		= ew_table.getResultTime();
	String duty				= ew_table.getDuty();
	String duty_cont		= com.anbtech.text.StringProcess.getContentTxt(ew_table.getDutyCont());
	String pay_by_work		= ew_table.getPayByWork();
	String w_type			= ew_table.getWtype();
	if(w_type.equals("n")) w_type = "평일 특근";
	else if(w_type.equals("s")) w_type = "토요일 특근";
	else if(w_type.equals("h")) w_type = "휴일 특근";
%>

<html>
<head>
<title>특근신청</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ew/css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../ew/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">특근신청</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../ew/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../ew/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=56 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="630" border=0>
	<tr><td>
		<table cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">신청자</td>
				<td width="35%" class="bg_06"><%=division_name%> <%=rank_name%> <%=member_name%></td>
				<td width="15%" height="25" align="middle" class="bg_05">신청일자</td>
				<td width="35%" class="bg_06"><%=c_date%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">특근사유</td>
				<td width="35%" class="bg_06"><%=duty%></td>
				<td width="15%" height="25" align="middle" class="bg_05">근무구분</td>
				<td width="35%" class="bg_06"><%=w_type%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">특근일자</td>
				<td width="35%" class="bg_06"><%=w_sdate%></td>
				<td width="15%" height="25" align="middle" class="bg_05">특근시간</td>
				<td width="35%" class="bg_06"><%=w_stime%> ~ <%=w_etime%></td></tr>
		    <tr>
				<td width="15%" height="25" align="middle" class="bg_05">업무내용</td>
		        <td width="85%" class="bg_06" nowrap colspan="3" height="100" valign="top"><%=duty_cont%>&nbsp;</td></tr><table>
	</td></tr></table>
</body></html>

<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>