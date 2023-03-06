<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.br.entity.*,com.anbtech.date.anbDate"
%>
<%
	anbDate anbdt = new com.anbtech.date.anbDate();		
	String time = anbdt.getTime();	
	String hrs = anbdt.getHours();	
	String cur = anbdt.getYear()+ "/" + anbdt.getMonth() + "/" + anbdt.getDates();

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
	String cr_dest			= table2.getCrDest();
	String cr_purpose		= table2.getCrPurpose();
	String content			= table2.getContent(); //
	String em_tel			= table2.getEmTel(); 
	String sdate			= u_year+"-"+u_month+"-"+u_date+" "+u_time;
	String edate			= tu_year+"-"+tu_month+"-"+tu_date+" "+tu_time;
	
%>
<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK rel="stylesheet" href="../br/css/style.css" type="text/css">
<LINK rel="stylesheet" href="../br/css/datepicker.css" type="text/css">
<SCRIPT LANGUAGE="JavaScript" src="../br/js/datepicker.js"></SCRIPT>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../br/images/blet.gif"> 차량입고처리</TD></TR>
			</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
				<TD align=left width=5></TD>
				<TD align=left width=200><a href="javascript:entering_process(<%=cid%>,<%=cr_id%>)"><img src="../br/images/bt_confirm.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../br/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<FORM method='post' name='eForm' style="margin:0">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--차량정보-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<tbody>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량번호</TD>
			<TD width="37%" height="25" class="bg_04"><%=table.getCarNo()%></TD>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">모델명</TD>
			<TD width="37%" height="25" class="bg_04"><%=table.getModelName()%></TD></TR>
        <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
        <TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE>

	<!--예약정보-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">예약일시</TD>
			<TD width="87%" height="25" class="bg_04" colspan="3"><%=sdate%> ~ <%=edate%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">신청사유</TD>
			<TD width="37%" height="25" class="bg_04"><%=cr_purpose%></TD>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">행선지</TD>
			<TD width="37%" height="25" class="bg_04"><%=cr_dest%></TD></TR>
        <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량사용자</TD>
			<TD width="37%" height="25" class="bg_04"><%=user_name%></TD>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">긴급연락처</TD>
			<TD width="37%" height="25" class="bg_04"><%=em_tel%></TD></TR>
        <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">동행자</TD>
			<TD width="87%" height="25" class="bg_04" colspan="3"><%=fellow_names%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">기타사항</TD>
			<TD width="87%" height="25" class="bg_04" colspan="3"><%=content%></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
        <TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE>

	<!--입고정보-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<tbody>
	    <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">입고일시</TD>
			<TD width="37%" height="25" class="bg_04" >
				<INPUT type=text name=r_date size=10 maxlength=10 value='<%=cur%>' readOnly> <a href="javascript:wopen('../br/car/Calendar.jsp?FieldName=r_date', '', 180, 250);"><img src='../br/images/bt_calendar.gif' border='0' align='absmiddle'></a>

				<SELECT name="r_time">
			<%
			String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL = "";
				for(int asH=0; asH<24; asH++){
					if(asH == Integer.parseInt(hrs)) msSEL = " SELECTED "; else msSEL="";
					out.println(" <OPTION " + msSEL + " value='"+asHour[asH]+":00'>" + asHour[asH] + ":" + "00");
					out.println(" <OPTION value='"+asHour[asH]+":30'>" + asHour[asH] + ":" + "30");
				}
				out.println("</SELECT>");
			%>
			</TD>         
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량상태</TD>
			<TD width="37%" height="25" class="bg_04">
				<SELECT name="st">
					<OPTION value="양호" SELECTED>양호</OPTION>
					<OPTION value="불량">불량</OPTION>
					<OPTION value="수리중">수리입고</OPTION>
				</SELECT>		   
			</TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
		<TR>
			<TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">특이사항</TD>
			<TD width="87%" height="25" class="bg_04" colspan="3"><textarea name="chgcont" rows="3" cols="40" colspan=3></textarea></TD></TR>
		<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
        <TR><TD height=10 colspan="4"></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mgr_id' value='<%=mgr_id%>'>
<INPUT type="hidden" name="date" value='<%=sdate%> ~ <%=edate%>'>
<INPUT type="hidden" name="user_name" value='<%=user_name%>'>
<INPUT type="hidden" name="cr_purpose" value='<%=cr_purpose%>'>
<INPUT type="hidden" name="fellow_names" value='<%=fellow_names%>'>
<INPUT type='hidden' name="em_tel" value=<%=em_tel%>>
<INPUT type="hidden" name="return_date">
<INPUT type="hidden" name="mgr_name" value='<%=mgr_name%>'>
</FORM>
</BODY></HTML>

<SCRIPT language="javascript">
	
	var f=document.eForm

	function entering_process(cid,cr_id){
		var st=f.st.value;
		var chgcont=f.chgcont.value;
		var return_date = f.r_date.value+" "+f.r_time.value;

		if(f.st.value=="수리중"){
		
			if(confirm("차량을 수리입고 시키시겠습니까?")){
				location.href ="../servlet/BookResourceServlet?category=car&mode=entering_Process&cid="+cid+"&cr_id="+cr_id+"&st="+st+"&chg_cont="+chgcont+"&tablename=charyang_master&return_date="+return_date;	
			} else {
				return;
			}
		
		} else	{
		
		location.href ="../servlet/BookResourceServlet?category=car&mode=entering_Process&cid="+cid+"&cr_id="+cr_id+"&st="+st+"&chg_cont="+chgcont+"&tablename=charyang_master&return_date="+return_date;			
		
		}
	}

	function wopen(url, t, w, h) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}
</SCRIPT>