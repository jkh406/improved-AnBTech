<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.br.entity.*,com.anbtech.date.anbDate"
%>
<%
//	com.anbtech.br.business.CarResourceBO carResourceBo=new com.anbtech.br.business.CarResourceBO(con);

    //차량정보
	CarInfoTable table = new CarInfoTable();
	table = (CarInfoTable)request.getAttribute("each_car_info");
	String cid				= table.getCid();
	String car_type			= table.getCarType();
	String car_no			= table.getCarNo();
	String model_name		= table.getModelName();
	String produce_year		= table.getProduceYear();
	String buy_date			= table.getBuyDate();
	String price			= table.getPrice();
	String fuel_type		= table.getFuelType();
	String fuel_efficiency	= table.getFuelEfficiency();
	String maker_company	= table.getMakerCompany();
	String reg_date			= table.getRegDate();
	String stat				= table.getStat();
	String car_id			= table.getCarId();
	//stat = carResourceBo.getStatName(stat);
	
	
	//예약정보
	CarUseInfoTable table2 = new CarUseInfoTable();
	table2 = (CarUseInfoTable)request.getAttribute("each_lending_info");
	String cr_id			= table2.getCrId();
	String user_name		= table2.getUserName();
	String mgr_id			= table2.getMgrId();
	String mgr_name			= table2.getMgrName();
	String user_id			= table2.getUserId();
	String ac_name			= table2.getAcName();
	String fellow_names		= table2.getFellowNames();
	String v_status			= table2.getVstatus();
	String u_year			= table2.getUyear();
	String u_month			= table2.getUmonth();
	String u_date			= table2.getUdate();
	String u_time			= table2.getUtime();
	String tu_year			= table2.getTuYear();
	String tu_month			= table2.getTuMonth();
	String tu_date			= table2.getTuDate();
	String tu_time			= table2.getTuTime();
	String c_year			= table2.getCyear();
	String c_month			= table2.getCmonth();
	String c_date			= table2.getCdate();
	String c_time			= table2.getCtime();
	String cr_dest			= table2.getCrDest();
	String cr_purpose		= table2.getCrPurpose();
	String content			= table2.getContent(); //
	String chg_cont			= table2.getChgCont();
	String em_tel			= table2.getEmTel(); 
	String return_date		= table2.getReturnDate();
	String sdate			= u_year+"-"+u_month+"-"+u_date+" "+u_time;
	String edate			= tu_year+"-"+tu_month+"-"+tu_date+" "+tu_time;
	String ce				= c_year+"-"+c_month+"-"+c_date+" "+c_time;
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../br/css/style.css" type="text/css">
<link rel="stylesheet" href="../br/css/datepicker.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="../br/js/datepicker.js"></SCRIPT>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif"> 차량예약정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:history.go(-1)"><img src="../br/images/bt_confirm.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--차량정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량번호</td>
           <td width="37%" height="25" class="bg_04"><%=table.getCarNo()%></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">모델명</td>
           <td width="37%" height="25" class="bg_04"><%=table.getModelName()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<!--예약정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">예약일시</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=sdate%> ~ <%=edate%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">신청사유</td>
           <td width="37%" height="25" class="bg_04"><%=cr_purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">행선지</td>
           <td width="37%" height="25" class="bg_04"><%=cr_dest%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>

		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량사용자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><%=em_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">동행자</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=fellow_names%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">기타사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
	
<%	if(v_status.equals("7")) { %>
	<!--사용정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">실사용일시</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=sdate%> ~ <%=ce%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">특이사항</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=chg_cont%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">입고처리자</td>
           <td width="37%" height="25" class="bg_04"><%=mgr_name%></td>
           <td width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">입고처리일시</td>
           <td width="37%" height="25" class="bg_04"><%=return_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<%	}	%>

</td></tr></table>
</body></html>

