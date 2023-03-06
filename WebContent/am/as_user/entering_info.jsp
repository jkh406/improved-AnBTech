<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 	
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.date.anbDate"%>
<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	com.anbtech.am.entity.AMUserTable auser = new com.anbtech.am.entity.AMUserTable();
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;

	String status_temp = request.getParameter("as_status")==null?"":request.getParameter("as_status");
	//String sb=(String)request.getAttribute("CategoryList"); // 카테고리 ComboList
	//String	div	  = request.getParameter("div");
	//String c_date = anbdt.getDateNoformat();	
	String caption = "";
	asInfoTable = new com.anbtech.am.entity.AsInfoTable();
	asInfoTable = (com.anbtech.am.entity.AsInfoTable)request.getAttribute("assetInfo");
		
	String sb=(String)request.getAttribute("CategoryList");
	
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
	
	String tu_date = asHistoryTable.getTuDate();
	String tu_year = asHistoryTable.getTuYear();
	String tu_month = asHistoryTable.getTuMonth();
	String th_day = asHistoryTable.getTuDay();
	String in_date = asHistoryTable.getInDate();
	
	String as_status = asHistoryTable.getAsStatus();
	String as_statusinfo = asHistoryTable.getAsStatusInfo();
		   if(as_statusinfo==null) as_statusinfo="";
		//   else as_statusinfo =" : "+as_statusinfo;
	String o_status = asHistoryTable.getOstatus();
	String wi_date = asHistoryTable.getWiDate();

	String as_status_name = asHistoryTable.getAsStatusName();
	String o_status_name = asHistoryTable.getOstatusName();
	
	String indate_caption	= ""; 
	String realdate_caption = "";
	if(o_status.equals("o")){
		indate_caption = "예약입고일자";
		realdate_caption = "실제입고일자";
	} else if(o_status.equals("l")){
		indate_caption = "예약반납일자";
		realdate_caption = "실제반납일자";
	} else if(o_status.equals("t")){
		realdate_caption = "이관일자";
	}
	
%>	
<HTML><HEAD><TITLE>처리결과</TITLE>

<LINK href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
  <TR><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR><TD height="33" width='100%' valign="middle" bgcolor="#73AEEF"><IMG src='../am/images/am_inresult.gif' border='0' align='absmiddle'></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<TABLE  cellSpacing=0 cellPadding=0 width="95%" border=0 >
			<TR><TD width="100%" colspan="4"  height=28></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="20%" height="25" class="bg_03" background="../am/images/bg-01.gif" >자산번호</TD>
				<TD width="30%" height="25" class="bg_04"><%=as_mid%></TD>
				<TD width="20%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
				<TD width="30%" height="25" class="bg_04"><%=model_name%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif">품&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</TD>
				<TD width="30%" height="25" class="bg_04"><%=as_name%></TD>
				<TD width="20%" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
				<TD width="30%" height="25" class="bg_04"><%=as_serial%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif">장비상태</TD>
				<TD width="30%" height="25" class="bg_04"><%=as_status_name%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif">문제점 및 조치사항</TD>
				<TD width="30%" height="25" class="bg_04" colspan='3'><%=as_statusinfo%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
<%	
		if("o".equals(o_status) || "l".equals(o_status))  {  //  입고일경우
				if("12".equals(as_status) || "13".equals(as_status) ||"17".equals(as_status)) {
%>
			<TR><TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif"><%=indate_caption%></TD>
				<TD width="30%" height="25" class="bg_04"  ><%=tu_date.substring(0,4)%>-<%=tu_date.substring(4,6)%>-<%=tu_date.substring(6,8)%>
				<TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif"><%=realdate_caption%></TD>
				<TD width="30%" height="25" class="bg_04"  ><%=in_date.substring(0,4)%>-<%=in_date.substring(4,6)%>-<%=in_date.substring(6,8)%></TD></TR>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
<%				}			

		} else if("t".equals(o_status)) {  // 이관일경우
%>			<TR><TD width="20%"  height="25" class="bg_03" background="../am/images/bg-01.gif"><%=realdate_caption%></TD>
				<TD width="80%" height="25" class="bg_04"   colspan='3'><%=u_date.substring(0,4)%>-<%=u_date.substring(4,6)%>-<%=u_date.substring(6,8)%></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
<%		}
%>		
		</TABLE>
		<TR><td height=20 colspan="2"></td></tr>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
		<TBODY>
			<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<a href='javascript:self.close()'><img src='../am/images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
			<TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR></TBODY></TABLE></form></td></tr></table></BODY></HTML>

