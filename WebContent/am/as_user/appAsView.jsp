<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.date.anbDate"%>
<%
		com.anbtech.am.entity.AsInfoTable asInfoTable;
		com.anbtech.am.entity.AsHistoryTable asHistoryTable;

		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String c_date = anbdt.getDateNoformat();	

		
		asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		asInfoTable = (com.anbtech.am.entity.AsInfoTable)request.getAttribute("assetInfo");
		
		//sb=(String)request.getAttribute("CategoryList");
		String  as_no		= ""+asInfoTable.getAsNo();			
		String  as_mid		= asInfoTable.getAsMid();		
		String  as_item_no	= asInfoTable.getAsItemNo();	
		String  model_name	= asInfoTable.getModelName();	
			
		String 	b_id		= asInfoTable.getBid();
		String  b_name		= asInfoTable.getBname();
		String  b_rank		= asInfoTable.getBrank();
		String 	w_id		= asInfoTable.getWid();
		String 	w_name		= asInfoTable.getWname();
		String 	w_rank		= asInfoTable.getWrank();
		String 	c_no		= asInfoTable.getCno();
		String  as_name		= asInfoTable.getAsName();		
		String  as_serial	= asInfoTable.getAsSerial();		
		String  buy_date	= asInfoTable.getBuyDate();		
		String  as_price	= asInfoTable.getAsPrice();		
		String  crr_name	= asInfoTable.getCrrName();		
		String  crr_rank	= asInfoTable.getCrrRank();	
		String  now_user	= asInfoTable.getUname();
		String  as_setting=asInfoTable.getAsSetting();	
		
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
		String as_maker=asInfoTable.getAsMaker();

		String caption = "";
		String text = "";

		if(o_status.equals("l")) {
			caption = "loan_info.gif";
			text = "대여신청";
		} else if(o_status.equals("t")) {
			caption = "igwan.gif";
			text = "이관신청";
		} else if(o_status.equals("o")) {
			caption = "ban_info.gif";
			text = "반출신청";
		}
%>	

<html>
<head>
<meta http-equiv=Content-Type content=text/html; charset=EUC-KR> 
<title></title>
<link rel="stylesheet" href="../am/css/style.css" type="text/css">
</head>
<body topmargin="0" leftmargin="0" >
<div id="page_content" style="position:absolute;left:0;top:0;width:100%">

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	<tr><td>
		<!-- 자산 정보 -->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR>
				<TD width="100%" height="25" colspan='4'><IMG src="../am/images/am_info.gif" border='0' align='absmiddle'></TD>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
				<TD width="37%" height="25" class="bg_04" ><%=as_mid%></TD>
				<td width="13%" height="25" align="left" class="bg_03" background="../am/images/bg-01.gif">고유&nbsp;번호</td>
				<td width="35%" class="bg_04"><%=as_serial%></td></tr>
			</TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<td width="13%" height="25" align="left" class="bg_03" background="../am/images/bg-01.gif">품&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</td>
		        <td width="35%" class="bg_04" ><%=as_name%></td>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
				<TD width="37%" height="25" class="bg_04" ><%=model_name%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">현재사용자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=now_user%></TD>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산관리자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=crr_name%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE>

		<!-- 신청정보 -->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR>
				<TD width="100%" height="25" colspan='4'><img src="../am/images/<%=caption%>" border=0></TD>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
<%	if("o".equals(o_status)){ // 반출일경우 %>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출장소</TD>
				<TD width="37%" height="25" class="bg_04" ><%=out_destination%></TD>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출기간</TD>
				<TD width="37%" height="25" class="bg_04" ><%=u_date.substring(0,4)%>-<%=u_date.substring(4,6)%>-<%=u_date.substring(6,8)%> ~ <%=tu_date.substring(0,4)%>-<%=tu_date.substring(4,6)%>-<%=tu_date.substring(6,8)%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=takeout_reason%></TD>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출신청자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=u_name%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>					
<%	} else if("t".equals(o_status)) { %>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관일자</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=u_date.substring(0,4) + "년 " + u_date.substring(4,6) + "월 " + u_date.substring(6,8) + "일"%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=takeout_reason%></TD>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관신청자</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=u_name%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
<%	} else if("l".equals(o_status)) { %>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">대여기간</TD>
				<TD width="87%" height="25" class="bg_04" colspan='3' colspan="3"><%=u_date.substring(0,4)%>-<%=u_date.substring(4,6)%>-<%=u_date.substring(6,8)%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">대여사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=takeout_reason%></TD>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">대여신청자</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><%=u_name%></TD>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
<%	}	%>
			<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">작성자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=w_name_h%></TD>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">작성일자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=write_date.substring(0,4) + "년 " + write_date.substring(4,6) + "월 " + write_date.substring(6,8) + "일"%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=10 colspan="4"></TD></TR>
			</TBODY></TABLE></TD></TR></TABLE>
</div> 
<script language="JavaScript1.2"> 
function iframe_reset(){ 
        dataobj=document.all? document.all.page_content : document.getElementById("page_content") 
         
        dataobj.style.top=0 
        dataobj.style.left=0 

        pagelength=dataobj.offsetHeight 
        pagewidth=dataobj.offsetWidth 

        parent.document.all.iframe_main.height=pagelength 
        parent.document.all.iframe_main.width=pagewidth 
} 
window.onload=iframe_reset 
</script>

</BODY>
</HTML>

