<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*,com.anbtech.date.anbDate"%>
<%
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;

	com.anbtech.am.entity.AMLinkTable amLinkTable;
/*
	//링크 문자열 가져오기
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();
*/	
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
	String c_date   = anbdt.getDateNoformat();	
	String cur		= c_date.substring(0,4)+"-"+c_date.substring(4,6)+"-"+c_date.substring(6,8);

	String o_status = request.getParameter("o_status")==null?"o":request.getParameter("o_status");
	String view_total ="";
	String view_boardpage = "";
	String view_totalpage = "";
	String view_pagecut = "";
	String category_full = "";

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

	com.anbtech.am.entity.AMUserTable auser = new com.anbtech.am.entity.AMUserTable();
	auser = (com.anbtech.am.entity.AMUserTable)request.getAttribute("user");
		
	// -- 현재 사용자 정보
	 String user_id = auser.getUserId();
	 String user_name = auser.getUserName();
	 String user_rank = auser.getUserRank();
	//---------------------------------------
		
		user_name = user_id+"/"+user_name;
		String wid=user_id;
		String wname=user_name;
		String wrank=user_rank;
		String bid=user_id;
		String bname=user_name;
		String brank=user_rank;
		String crrid=user_id;
		String crrname=user_name;
		String crrrank=user_rank;



		asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		asInfoTable = (com.anbtech.am.entity.AsInfoTable)request.getAttribute("assetInfo");
		
		//sb=(String)request.getAttribute("CategoryList");
		String as_no=""+asInfoTable.getAsNo();	
		
		String as_mid=asInfoTable.getAsMid();		
		String as_item_no=asInfoTable.getAsItemNo();	
		String model_name=asInfoTable.getModelName();	
			
		String b_id = asInfoTable.getBid();
		String b_name = asInfoTable.getBname();
		String b_rank = asInfoTable.getBrank();
		String w_id = asInfoTable.getWid();
		String w_name = asInfoTable.getWname();
		String w_rank = asInfoTable.getWrank();
		String c_no = asInfoTable.getCno();
		String as_name=asInfoTable.getAsName();		
		String as_serial=asInfoTable.getAsSerial();		
		String buy_date=asInfoTable.getBuyDate();		
		String as_price=asInfoTable.getAsPrice();		
		String crr_name=asInfoTable.getCrrName();		
		String crr_rank=asInfoTable.getCrrRank();
		String u_name = asInfoTable.getUname();
		String as_setting=asInfoTable.getAsSetting();	
		String as_maker=asInfoTable.getAsMaker();
		String as_status_info = asInfoTable.getAsStatus();	 // 자산 정보(as_info 테이블) 상태	
		String handle = asInfoTable.getHandle();
		String status_name = "";	// 자산 현재 상태정보
		String handle_name = "";	// 자산 반출 여부 상태정보

		if(as_status_info.equals("6"))	{		status_name="정상";
		} else if(as_status_info.equals("10")){	status_name="폐기";
		} else if(as_status_info.equals("13")){ status_name="수리";
		}
		
		if(handle.equals("y")) { 
			handle_name ="가능"; 
		} else if(handle.equals("n")) {
			handle_name="불능";
		}
%>
	

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../am/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form method='get' name='eForm'  action="../servlet/AssetServlet">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
				  <TD valign='middle' class="title"> <img src="../am/images/blet.gif"> <%=text%></TD></TR></TBODY>
			</TABLE></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR><TD height=32><!--버튼-->
				<TABLE cellSpacing=0 cellPadding=0>
					<TBODY>
					<TR>
						<TD align=left width=5 ></TD>
						<TD align=left width=500>
							<A HREF='javascript:checkgosave()'><img src="../am/images/bt_save.gif" border='0' align="absmiddle" ></A>
							<A HREF='javascript:history.back()'><img src="../am/images/bt_cancel.gif" border=0 align="absmiddle"></A>
					</TD></TR>
					</TBODY></TABLE></TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='4'></TD></TR>

<!--내용-->
	<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
		<!--기본정보-->
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
				<!--
				<TR>
					<TD width="100%" colspan="4" ><img src="../am/images/am_info.gif" border=0 style='cursor:hand' align='absmiddle'></TD></TR>
					<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>-->
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_mid%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=model_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_serial%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">제조업체명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_maker%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>					
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">규격</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3><%=as_setting%></TD>
					<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">가용상태</TD>
						<TD width="37%" height="25" class="bg_04" ><%=status_name%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출가능여부</TD>
						<TD width="37%" height="25" class="bg_04" ><%=handle_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>										
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">현재사용자</TD>
						<TD width="37%" height="25" class="bg_04" ><%=u_name%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산관리자</TD>
						<TD width="37%" height="25" class="bg_04" ><%=crr_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD height='10' colspan="4"></TD></TR>
			 </TBODY>
		</TABLE>

    <!--기본정보-->
<TR><TD>
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
			<TR><TD width="100%" colspan="4"><img src="../am/images/<%=caption%>" border=0></b></TD></TR>

<%	if("o".equals(o_status)){ // 반출신청인 경우	%>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
		    <TR>
			   <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출기간</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="3">
					<input type=text name=sdate size=10 maxlength=10 value="<%=cur%>"  class='text_01' readonly>
					<a href="javascript:wopen('../am/as_user/Calendar.jsp?FieldName=sdate', '', 180, 250);">
					<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a>
					&nbsp;부터&nbsp;
					<input type=text name=edate size=10 maxlength=10  value="<%=cur%>" class='text_01' readonly>
					<a href="javascript:wopen('../am/as_user/Calendar.jsp?FieldName=edate', '', 180, 250);">
					<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a>
					&nbsp;까지&nbsp;</TD>
			</TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><input type=text name='takeout_reason' size=50 class='text_01'></TD>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출장소</TD>
				<TD width="37%" height="25" class="bg_04"><input type=text name="out_destination" size=30></TD>
	            <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">사용자</TD>
		        <TD width="37%" height="25" class="bg_04">
					<input type="text" name="u_name" value="<%=crrname%>" size=20 readOnly> <img src="../am/images/bt_search.gif" onClick="javascript:searchSabun();" border="0" align='absmiddle' style='cursor:hand'>
					</TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>

<%	}else if("t".equals(o_status)){	//이관신청인 경우%>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관일자</TD>
			   <TD width="87%" height="25" colspan="4" class="bg_04">
					<input type=text name=sdate size=10 maxlength=10 value="<%=cur%>" class='text_01'><a href="javascript:wopen('../am/as_user/Calendar.jsp?FieldName=sdate', '', 180, 250);"><img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><input type=text name='takeout_reason' size=50 class='text_01'></TD>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">이관부서</TD>
				<TD width="37%" height="25" class="bg_04">인수자 부서로 이관됨.</TD>
	            <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">인수자</TD>
		        <TD width="37%" height="25" class="bg_04">
					<input type="text" name="u_name" value="<%=crrname%>" size=20 readOnly> <img src="../am/images/bt_search.gif" onClick="javascript:searchSabun();" border="0" align='absmiddle' style='cursor:hand'>
					</TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>

<%	} else if("l".equals(o_status)){ //대여신청인 경우	%>						
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">대여기간</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="4">
					<input type=text name=sdate size=10 maxlength=10 value="<%=cur%>"  class='text_01' readonly>
					<a href="javascript:wopen('../am/as_user/Calendar.jsp?FieldName=sdate', '', 180, 250);">
					<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a>
					&nbsp;부터&nbsp;
					<input type=text name=edate size=10 maxlength=10  value="<%=cur%>" class='text_01' readonly>
					<a href="javascript:wopen('../am/as_user/Calendar.jsp?FieldName=edate', '', 180, 250);">
						<img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a>&nbsp;까지</TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">대여사유</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><input type=text name='takeout_reason' size=50 class='text_01'></TD>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">사용자</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><input type="text" name="u_name" value="<%=crrname%>" size=20 readOnly> <img src="../am/images/bt_search.gif" onClick="javascript:searchSabun();" border="0" align='absmiddle' style='cursor:hand'></TD>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>

<%	}	%>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">신청자</TD>
				<TD width="37%" height="25" class="bg_04"><%=wname%></TD>
	            <TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">신청일자</TD>
		        <TD width="37%" height="25" class="bg_04"><%=c_date.substring(0,4)%>년 <%=c_date.substring(4,6)%>월 <%=c_date.substring(6,8)%>일</TD></TR>
	        <TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR></TBODY></TABLE></TD></TR>

<%	if(o_status.equals("l")){ %> <input type = 'hidden' name="o_status" value="l">
<%	}else if(o_status.equals("o")){ %> <input type = 'hidden' name="o_status" value="o">
<%	}else if(o_status.equals("t")){ %> <input type = 'hidden' name="o_status" value="t">
<%	}	%>
<input type="hidden" name="w_name" value='<%=wname%>'>
<input type="hidden" name='w_id'   value='<%=wid%>'> 
<input type="hidden" name='w_rank' value='<%=wrank%>'> 
<input type="hidden" name='c_date' value='<%=c_date%>' >
<input type="hidden" name="u_id"   value="<%=crrid%>">
<input type="hidden" name="mode"   value="user_asreq_process">
<input type="hidden" name="as_no"  value="<%=as_no%>">
<input type="hidden" name="c_no"   value="<%=c_no%>">

</form>
</body>
</html>

<script language="javascript">
	var f = document.eForm

	function searchUsers()
	{
		var to = "eForm.u_name";
		window.open("../admin/UserTreeMainForSingle.jsp?target="+to+"&type=single","user","width=300,height=400,scrollbar=yes,toolbar=no,status=no,resizable=no");

	}

		//대상자 찾기
	function searchSabun()
	{
		wopen("../am/as_user/searchSabun.jsp?target=eForm.u_name","user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

	}

	function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}


	function checkgosave()
	{	var status = f.o_status.value;
		var sdate = f.sdate.value;
		var datefield = sdate.split("-")
		    sdate =  datefield[0]+datefield[1]+datefield[2]
		var edate 
		if(status=="o"|| status=="l"){
			edate = f.edate.value;
			datefield = edate.split("-")
			edate =  datefield[0]+datefield[1]+datefield[2]
		}

		if(f.takeout_reason.value==""){
			alert("사유을 기입해 주십시요");
			return;
		}

		if(sdate=="<%=cur%>"){
			alert("날짜를 선택해 주십시요.");
			return;
		}
		
		if(f.o_status.value =="o" || f.o_status.value=="l") {
			if(f.edate.value==""){
				alert("날짜를 선택해 주십시요.");
				return;
			}
		}
	

		// 오늘날짜
		today = new Date();
		var c_month = today.getMonth()+1;
		var c_date = today.getDate();
		
		if(c_month < 10) c_month = "0" + c_month;
		if(c_date < 10) c_date = "0" + c_date;
		
		var cday = today.getYear()+""+c_month+""+c_date;
		///
		
		if(status=="t") status = "이관";
		if(status=="l") { status = "대여";f.edate.value = edate;}
		if(status=="o") { status = "반출";f.edate.value = edate;}
		
		if( sdate < cday ) { // 날짜 비교 (검색날짜/현재날짜)
			alert("금일 이전 날짜에는 자산"+status+"요청을 할 수 없습니다.");		
			return;
		}
		
		/*f.sdate.value = sdate;
		if(status=="o"|| status=="l"){
			f.edate.value = edate;
				
		}*/
		f.submit();
	}

	
</script>