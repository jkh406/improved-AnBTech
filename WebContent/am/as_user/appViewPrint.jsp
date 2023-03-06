<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*"%>
<%@	page import="com.anbtech.admin.SessionLib"%>
<%!	
	com.anbtech.am.entity.AsApprovalInfoTable app_table;
	String pid = "";//request.getParameter("as_no");
	String pid2 ="";//(String)request.getParameter("pid2");
	//String pid ="";String pid2="";
	//System.out.println("pid:"+p);
//	out.println("pid:"+pid);
//	out.println("pid2:"+pid2);
%>

<%	// 1차 결재정보
	app_table = new com.anbtech.am.entity.AsApprovalInfoTable();
	app_table = (com.anbtech.am.entity.AsApprovalInfoTable)request.getAttribute("appInfo");

	String writer_sign		= app_table.getWriterSig();
	String writer_name		= app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName();
	String memo				= app_table.getMemo();

	// 2차 결재 정보

	app_table = (com.anbtech.am.entity.AsApprovalInfoTable)request.getAttribute("appInfo2");
	String writer_sign2		= app_table.getWriterSig();
	String writer_name2		= app_table.getWriterName();
	String reviewer_sign2	= app_table.getReviewerSig();
	String reviewer_name2	= app_table.getReviewerName();
	String decision_sign2	= app_table.getDecisionSig();
	String decision_name2	= app_table.getDecisionName();
	String memo2			= app_table.getMemo();


%>
<%
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;

	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String c_date = anbdt.getDateNoformat();	

	//String o_status = request.getParameter("o_status");
		
		asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		asInfoTable = (com.anbtech.am.entity.AsInfoTable)request.getAttribute("assetInfo");
		
		//sb=(String)request.getAttribute("CategoryList");
		String  as_no=""+asInfoTable.getAsNo();			
		String  as_mid=asInfoTable.getAsMid();		
		String  as_item_no=asInfoTable.getAsItemNo();	
		String  model_name=asInfoTable.getModelName();	
			
		String 	b_id = asInfoTable.getBid();
		String  b_name = asInfoTable.getBname();
		String  b_rank = asInfoTable.getBrank();
		String 	w_id = asInfoTable.getWid();
		String 	w_name = asInfoTable.getWname();
		String 	w_rank = asInfoTable.getWrank();
		String 	c_no = asInfoTable.getCno();
		String  as_name=asInfoTable.getAsName();		
		String  as_serial=asInfoTable.getAsSerial();		
		String  buy_date=asInfoTable.getBuyDate();		
		String  as_price=asInfoTable.getAsPrice();		
		String  crr_name=asInfoTable.getCrrName();		
		String  crr_rank=asInfoTable.getCrrRank();		
		String  now_user=asInfoTable.getUname();
		String  as_setting=asInfoTable.getAsSetting();	
		
%>
<%
		asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		asHistoryTable = (com.anbtech.am.entity.AsHistoryTable)request.getAttribute("historyInfo");

		String h_no = ""+asHistoryTable.getHno();	
		String as_no_h = asHistoryTable.getAsNo();
		String w_id_h = asHistoryTable.getWid();
		String w_name_h = asHistoryTable.getWname();
		String w_rank_h = asHistoryTable.getWrank();
		String u_id  = asHistoryTable.getUid();
		String u_name = asHistoryTable.getUname();
		String u_rank = asHistoryTable.getUrank();
		String takeout_reason = asHistoryTable.getTakeOutReason();
		String out_destination = asHistoryTable.getOutDestination();
		String write_date = asHistoryTable.getWriteDate();
		String u_date = asHistoryTable.getUdate();
		String u_year = asHistoryTable.getUyear();
		String u_month = asHistoryTable.getUmonth();
		String u_day = asHistoryTable.getUday();
		String tu_date = asHistoryTable.getTuDate();
		String tu_year = asHistoryTable.getTuYear();
		String tu_month = asHistoryTable.getTuMonth();
		String th_day = asHistoryTable.getTuDay();
		String in_date = asHistoryTable.getInDate();
		String c_year = asHistoryTable.getCyear();
		String c_month = asHistoryTable.getCmonth();
		String c_day = asHistoryTable.getCday();
		String as_status = asHistoryTable.getAsStatus();
		String as_statusinfo = asHistoryTable.getAsStatusInfo();
		String o_status = asHistoryTable.getOstatus();
		String wi_date = asHistoryTable.getWiDate();
		String pid = asHistoryTable.getPid();
		String pid2 = asHistoryTable.getPid2();

		String caption="";
		
		if(o_status.equals("t")) {
			caption="이관";
		} else if(o_status.equals("o")){
			caption="반출";
		} else if(o_status.equals("l")){
			caption="대여";
		}

%>	
<html>
<head>
<title>자산결재정보</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../am/css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../am/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2"><%=caption%>신청</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../am/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../am/images/bt_close.gif" border="0"></a></div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>


		
<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
		<input type="hidden" name="mode" value="user_asreq_process">
		<input type="hidden" name="as_no" value="<%=as_no%>">
		<input type="hidden" name="c_no" value="<%=c_no%>">
		<input type="hidden" name="h_no" value="<%=c_no%>">
		<input type="hidden" name="o_status" value="<%=o_status%>">
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
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
<TABLE cellSpacing=0 cellPadding=0 width="700" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">자산&nbsp;번호</td>
				<td width="35%" class="bg_06"><%=as_mid%></td>
				<td width="15%" height="25" align="middle" class="bg_05">고유&nbsp;번호</td>
				<td width="35%" class="bg_06"><%=as_serial%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">품&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</td>
		        <td width="35%" class="bg_06" ><%=as_name%></td>
				<td width="15%" height="25" align="middle" class="bg_05">모&nbsp;&nbsp;델&nbsp;&nbsp;명</td>
		        <td width="35%" class="bg_06" ><%=model_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">규&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;격</td>
		        <td width="35%" class="bg_06" colspan='3'><%=as_setting%></td>
			</tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">현재사용자</td>
		        <td width="35%" class="bg_06" ><%=now_user%></td>
				<td width="15%" height="25" align="middle" class="bg_05">자산관리자</td>
				<td width="35%" class="bg_06"><%=crr_name%></td>
			</tr>
		</TABLE>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">요청&nbsp;형태</td>
				<td width="35%" class="bg_06"><%=caption%></td>
				<td width="15%" height="25" align="middle" class="bg_05">사&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;유</td>
				<td width="35%" class="bg_06"><%=takeout_reason%></td>
			</tr>
<%	
		if("o".equals(o_status)){ // 반출일경우
%>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">반출&nbsp;장소</td>
				<td width="35%" class="bg_06"><%=out_destination%></td>
				<td width="15%" height="25" align="middle" class="bg_05">반출기간</td>
				<td width="35%" class="bg_06"><%=u_date.substring(0,4)%>/<%=u_date.substring(4,6)%>/<%=u_date.substring(6,8)%>&nbsp;~&nbsp;<%=tu_date.substring(0,4)%>/<%=tu_date.substring(4,6)%>/<%=tu_date.substring(6,8)%></td></tr>


<%		} else if("t".equals(o_status)) {
%>
				<tr>
				<td width="15%" height="25" align="middle" class="bg_05">이관&nbsp;일자</td>
				<td width="85%" class="bg_06"  colspan='3'><%=u_date.substring(0,4)%>/<%=u_date.substring(4,6)%>/<%=u_date.substring(6,8)%></td></tr>


<%		} else if("l".equals(o_status)) {
%>
				<tr>
				<td width="15%" height="25" align="middle" class="bg_05">대여&nbsp;일자</td>
				<td width="85%" class="bg_06" colspan='3'><%=u_date.substring(0,4)%>/<%=u_date.substring(4,6)%>/<%=u_date.substring(6,8)%>&nbsp;~&nbsp;<%=tu_date.substring(0,4)%>/<%=tu_date.substring(4,6)%>/<%=tu_date.substring(6,8)%></td></tr>

<%		}
%>		
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">사&nbsp;&nbsp;용&nbsp;&nbsp;자</td>
		        <td width="85%" class="bg_06" nowrap colspan="3"><%=u_name%></td></tr>
            <tr>
				<td width="15%" height="25" align="middle" class="bg_05">작&nbsp;&nbsp;성&nbsp;&nbsp;자</td>
				<td width="35%" class="bg_06" ><%=w_name_h%></td>
				<td width="15%" height="25" align="middle" class="bg_05">작성&nbsp;일자</td>
				<td width="35%" class="bg_06" ><%=write_date%></td></tr>
           
			</table>

      </td></tr></table>
<br>
<%if(!pid2.equals("empty")) { %>

<TABLE cellSpacing=0 cellPadding=0 width="700" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
		<input type="hidden" name="mode" value="user_asreq_process">
		<input type="hidden" name="as_no" value="<%=as_no%>">
		<input type="hidden" name="c_no" value="<%=c_no%>">
		<input type="hidden" name="h_no" value="<%=c_no%>">
		<input type="hidden" name="o_status" value="<%=o_status%>">
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=memo2%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign2%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign2%></TD>
		<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign2%></TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=writer_name2%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name2%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=decision_name2%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>
<%	}
%>
</body></html>


<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
</script>
