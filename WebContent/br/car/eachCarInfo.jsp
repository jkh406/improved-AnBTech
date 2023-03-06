<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.br.entity.*,com.anbtech.text.Hanguel"
%>
<%!
	CarUseInfoTable eachcartable;
	CarInfoTable carinfotable;
	CarLinkTable redirect;
	Hanguel hanguel = new Hanguel();
%>

<%	
	// 차량관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("BR02");
	
	String year = request.getParameter("year")==null?"null":request.getParameter("year");
	String car_stat = "";

    //차량사용이력
	ArrayList table_list = new ArrayList();
	table_list		= (ArrayList)request.getAttribute("eachcar_list");
	eachcartable	= new CarUseInfoTable();
	
	//차량정보
	carinfotable	= new CarInfoTable();
	carinfotable	= (CarInfoTable)request.getAttribute("each_info");

	String ymd		= carinfotable.getBuyDate();
	String c_id		= carinfotable.getCid();

	//링크 문자열 가져오기
	redirect = new CarLinkTable();
	redirect = (CarLinkTable)request.getAttribute("Redirect");
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();
	String view_pagecut = redirect.getViewPagecut();

	car_stat = carinfotable.getStat();
%>
<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../br/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif" align="absmiddle"> 차량사용이력</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../br/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../br/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../br/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'><FORM method="get" name="menuForm" style="margin:0">
					<SELECT name='year' onChange='javascript:go()'>
						<OPTION value="2000">2000년</OPTION>
						<OPTION value="2001">2001년</OPTION>
						<OPTION value="2002">2002년</OPTION>
						<OPTION value="2003">2003년</OPTION>
						<OPTION value="2004">2004년</OPTION>
						<OPTION value="2005">2005년</OPTION>
						<OPTION value="2006">2006년</OPTION>
						<OPTION value="2007">2007년</OPTION>
						<OPTION value="2008">2008년</OPTION>
						<OPTION value="2009">2009년</OPTION>
						<OPTION value="2010">2010년</OPTION>
						<OPTION value="2011">2011년</OPTION>
					</SELECT>
					<% if (!car_stat.equals("5")) {%>
						<a href="javascript:lending_App();"><img src="../br/images/bt_reg_car.gif" border="0" align="absmiddle"></a> 
					<%	}
					%>
					<a href="javascript:location.href('../servlet/BookResourceServlet?category=car&mode=view_stat');"><img src="../br/images/bt_confirm.gif" border="0" align="absmiddle"></a>
					
				<%	if(!year.equals("")){	%>
						<script language='javascript'>
							document.menuForm.year.value = "<%=year%>";
						</script>
				<%	}	%></FORM></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD vAlign=top><!-- 차량 정보 -->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
         <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차종</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getCarType()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">구입일자</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getBuyDate()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">차량번호</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getCarNo()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">연료구분</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getFuelType()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">모델</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getModelName()%></TD>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">연비</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getFuelEfficiency()%>km/l</TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		 <TR>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">년식</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getProduceYear()%>년식</TD>
           <TD width="13%" height="25" class="bg_03" background="../br/images/bg-01.gif">제조회사</TD>
           <TD width="37%" height="25" class="bg_04"><%=carinfotable.getMakerCompany()%></TD></TR>
         <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
         </tbody></table>  
  </TD></TR>
  <TR><TD><TABLE border=0 width='100%'><TR><TD align=left><IMG src='../br/images/title_use_history.gif' border='0' alt='차량사용이력'></TD></TR></TABLE></TD></TR>  
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR>
    <TD vAlign=top height="100%"><FORM name=frm method="get" style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=30 align=middle class='list_title'>번호</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>사용일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>차량상태</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>차량사용자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>신청자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>신청일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../br/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
	int i=0,no=1;
	int judge = 0;
	no = Integer.parseInt(view_total) - (Integer.parseInt(view_boardpage)-1)*10;

	Iterator table_iter = table_list.iterator();
	while(table_iter.hasNext()){
		eachcartable = (CarUseInfoTable)table_iter.next();
		String cr_id		= eachcartable.getCrId();
		String write_name	= eachcartable.getWriteName();
		String write_id		= eachcartable.getWriteId();
		String user_name	= eachcartable.getUserName();
		String fellow_names = eachcartable.getFellowNames();
		String in_date		= eachcartable.getInDate();
		String v_status		= eachcartable.getVstatus();
		if(v_status.equals("배차중"))  ++judge;

		String u_year		= eachcartable.getUyear();
		String u_month		= eachcartable.getUmonth();
		String u_date		= eachcartable.getUdate();
		String u_time		= eachcartable.getUtime();
		String tu_year		= eachcartable.getTuYear();
		String tu_month		= eachcartable.getTuMonth();
		String tu_date		= eachcartable.getTuDate();
		String tu_time		= eachcartable.getTuTime();
		String cr_dest		= eachcartable.getCrDest();
		String cr_purpose	= eachcartable.getCrPurpose();
		String car_type		= eachcartable.getCarType();
		String car_no		= eachcartable.getCarNo();
		String content		= eachcartable.getContent();
		String model_name	= eachcartable.getModelName();
		String entering_state = eachcartable.getEnteringState();
		String sdate		= u_year+"-"+u_month+"-"+u_date+" "+u_time;
		String edate		= tu_year+"-"+tu_month+"-"+tu_date+" "+tu_time;
		String j_sdate		= u_year+u_month+u_date;
		String j_edate		= tu_year+tu_month+tu_date;
		String flag			= eachcartable.getFlag();
		
		if(entering_state==null) entering_state = "";		
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle class='list_bg'><%=no%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD height="25" align=middle class='list_bg'><a href="javascript:info(<%=c_id%>,<%=cr_id%>)"><%=sdate%> ~ <%=edate%></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=v_status%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=user_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=write_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=in_date%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
					<% if( idx >= 0 && (v_status.equals("사용가능") || v_status.equals("배차중"))) { %> 
							<a href="javascript:entering(<%=c_id%>,<%=cr_id%>)">
								<img src="../br/images/lt_in_process.gif" border='0' align='absmiddle' alt='입고처리'>
							</a> 
					<% }else if(idx < 0 && login_id.equals(write_id) && v_status.equals("예약중")) { %>
							<a href="javascript:lending_cancel(<%=c_id%>,<%=cr_id%>)">
								<img src="../br/images/lt_bk_cancel.gif" border='0' align='absmiddle' alt='예약취소'></a>
					<% }else if(idx >= 0 && v_status.equals("예약중")) { %>
							<a href="javascript:lending_cancel(<%=c_id%>,<%=cr_id%>)">
									<img src="../br/images/lt_bk_cancel.gif" border='0' align='absmiddle' alt='예약취소'></a>
							<a href="javascript:lending_process('<%=c_id%>','<%=cr_id%>')">
									<img src="../br/images/lt_out_car.gif" border='0' align='absmiddle' alt='배차처리'></a>
					<% }else if(idx >= 0 && v_status.equals("배차중")) { %>
							<a href="javascript:entering(<%=c_id%>,<%=cr_id%>)">
									<img src="../br/images/lt_in_process.gif" border='0' align='absmiddle' alt='입고처리'></a>
					<% }else if(v_status.equals("입고완료")) { %>
						<img src="../br/images/lt_in_car.gif" border='0' align='absmiddle' alt='입고완료'>
					<% } %>
			  </TD>
			<TR><TD colSpan=13 background="../br/images/dot_line.gif"></TD></TR>
<%			i+=3;
			no--;

	} // end_while
%>  
		</TBODY></TABLE></TD></TR></TBODY></TABLE></FORM>

<FORM name='listForm' style="margin:0">
   <input type='hidden' name='cid' value='<%=c_id%>'>
</FORM>
</BODY>
</HTML>

<script language="javascript">
<!--

function go(){  // menuEachCar.jsp에서 선택된 차량의 당해 년도
		var cid = document.listForm.cid.value;
		var year = document.menuForm.year.value;
		location.href = "../servlet/BookResourceServlet?tablename=charyang_master&category=car&mode=eachcar&cid="+cid+"&y="+year;
}

function goinfo(){	
	location.href = "../servlet/BookResourceServlet?category='car'&mode='eachcar'";
	
}

function lending_App() {
	
	var cid = document.listForm.cid.value;
	location.href ="../servlet/BookResourceServlet?category=car&mode=add_lending&cid="+cid;	

}

function entering(cid,cr_id){

	location.href ="../servlet/BookResourceServlet?category=car&mode=entering_view&cid="+cid+"&cr_id="+cr_id;	

}

function info(cid,cr_id){

	location.href ="../servlet/BookResourceServlet?category=car&mode=carinfo_view&cid="+cid+"&cr_id="+cr_id;	

}

function lending_process(cid,cr_id){
	
	if( <%=judge%> > 0) {
		alert("이 차량은 이미 배차중입니다. 확인해 주십시요.");
		return;
	} else if(confirm("배차처리 하시겠습니까?")) {
		location.href ="../servlet/BookResourceServlet?category=car&mode=lending_Process&cid="+cid+"&cr_id="+cr_id+"&tablename=charyang_master";	
	} else {
		return;
	}
}

function lending_cancel(cid,cr_id){
	
	if(confirm("예약을 취소하시겠습니까?")) {
		location.href ="../servlet/BookResourceServlet?category=car&mode=lending_cancel&cid="+cid+"&cr_id="+cr_id;	
	} else {
		return;
	}
}
//-->
</SCRIPT>