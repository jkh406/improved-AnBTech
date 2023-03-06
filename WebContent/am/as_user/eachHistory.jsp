<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage = "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.am.entity.*"%>

<%
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;
	com.anbtech.am.entity.AMLinkTable amLinkTable;

		ArrayList table_list = new ArrayList();
		table_list = (ArrayList)request.getAttribute("assetReqList1");
		asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		Iterator table_iter = table_list.iterator();

	// 카테고리 리스틀 (분류명)
	String sb =(String)request.getAttribute("CategoryList");

	//링크 문자열 가져오기
	amLinkTable = new AMLinkTable();
	amLinkTable = (AMLinkTable)request.getAttribute("Redirect");
	String view_total = ""+amLinkTable.getViewTotal();
	String view_boardpage = amLinkTable.getViewBoardpage();
	String view_totalpage = ""+amLinkTable.getViewtotalpage();
	String view_pagecut = amLinkTable.getViewPagecut();
	String category_full = amLinkTable.getWhereCategory();

	String login = request.getParameter("login_id");

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
		String  dc_count=asInfoTable.getDcCount();		
		String  dc_bound=asInfoTable.getDcBound();		
		String  as_each_dc=asInfoTable.getAsEachDc();	
		String  as_value=asInfoTable.getAsValue();	
		String  crr_id = asInfoTable.getCrrId();
		String  crr_name=asInfoTable.getCrrName();		
		String  crr_rank=asInfoTable.getCrrRank();
		String  u_name	=asInfoTable.getUname();
		String  as_maker=asInfoTable.getAsMaker();		
		String  as_setting=asInfoTable.getAsSetting();	
		String  as_status_info = asInfoTable.getAsStatus();	// 자산 정보(as_info 테이블) 상태	
		String  handle = asInfoTable.getHandle();
		String status_name = "";							// 자산 현재 상태정보
		String handle_name = "";							// 자산 반출 여부 상태정보

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

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<FORM method='post' name='eForm' enctype='multipart/FORM-data' action="../servlet/BookResourceServlet">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height=27><!-- 타이틀 및 페이지 정보 -->
			    <TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				  <TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> 자산상세정보</TD>
						<TD style="padding-right:10px" align='right' valign='middle'><img src="../am/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../am/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../am/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
				</TABLE></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR height=32><!--버튼 및 페이징-->
				<TD vAlign=top>
				<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
					<TBODY>
					<TR>
						<TD width=4>&nbsp;</TD>
						<TD align=left width='500'>
<%
			    if(as_status_info.equals("10") || as_status_info.equals("13") ) { 
					// 폐기, 수리 자산은 제외
				}else {
					if(crr_id.equals(login)) { // 현재 접속자와 현 자산의 사용자가 같을 경우만 반출/이관 신청 가능
						if( handle.equals("y")) {
%>  						
							<a href="javascript:as_moving('o')"><IMG src='../am/images/bt_ban_req.gif' border='0' align='absmiddle' alt='반출신청'></a>
<%						}	
%>							
							<a href="javascript:as_moving('t')"><IMG src='../am/images/bt_ban_req2.gif' border='0' align='absmiddle' alt='이관신청'></a>

<%                  }
						if(handle.equals("y")) {
%>							
							<a href="javascript:as_moving('l')"><IMG src='../am/images/bt_ban_req3.gif' border='0' align='absmiddle'></a>
<%						}
				}
%>
							<a href="javascript:go_asInfo('<%=as_no%>','<%=c_no%>')"><IMG src='../am/images/bt_view_b.gif' border='0' align='absmiddle'></a>
							<a href="javascript:go_list();"><IMG src='../am/images/bt_list.gif' border='0' align='absmiddle'></a></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY>
		</TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
    
		<TR height=100%><!--리스트-->
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
					<!--
					<TR>
						<TD width="100%" height="25" colspan='4'><IMG src="../am/images/am_info.gif" border='0' align='absmiddle'></TD>
					<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>-->
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_mid%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=model_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">분류명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=sb%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">품목명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_serial%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">제조업체명</TD>
						<TD width="37%" height="25" class="bg_04" ><%=as_maker%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>					
					<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">규격</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3><%=as_setting%></TD>
					<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
					<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">가용상태</TD>
						<TD width="37%" height="25" class="bg_04" ><%=status_name%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">반출가능여부</TD>
						<TD width="37%" height="25" class="bg_04" ><%=handle_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>										
					<TR><TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">현재사용자</TD>
						<TD width="37%" height="25" class="bg_04" ><%=u_name%></TD>
						<TD width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산관리자</TD>
						<TD width="37%" height="25" class="bg_04" ><%=crr_name%></TD></TR>
					<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
					<TR><TD height='10' colspan="4"></TD></TR>
					</TBODY>
				</TABLE>
	  
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
					<TR>
					<TD width="100%" height="25" colspan='15'><IMG src="../am/images/history.gif" border='0' align='absmiddle'></TD>
					<TR bgcolor="C7C7C7"><TD height="2" colspan="15"></TD></TR>
					<TR vAlign=middle height=23>
						<TD noWrap width=65 align=middle class='list_title'>구분</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=180 align=middle class='list_title'>적용일시</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>사  유</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=120 align=middle class='list_title'>신청자</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=90 align=middle class='list_title'>신청일</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=110 align=middle class='list_title'>비고</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>입고정보</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../am/images/list_tep2.gif"></TD>
						<TD noWrap width=60 align=middle class='list_title'>결재정보</TD>
					</TR>
					<TR bgColor=C7C7C7 height=1><TD colspan=15></TD></TR>
<%				
		while(table_iter.hasNext()) {
			asHistoryTable = (com.anbtech.am.entity.AsHistoryTable)table_iter.next();

			int h_no = asHistoryTable.getHno();
			String asno = asHistoryTable.getAsNo();
			String wid = asHistoryTable.getWid();
			String wname = asHistoryTable.getWname();
			String wrank = asHistoryTable.getWrank();
			String writedate = asHistoryTable.getWriteDate();
			if(!writedate.equals("0")) {
				writedate =writedate.substring(0,4)+"-"+writedate.substring(4,6)+"-"+writedate.substring(6,8);
			} else { writedate = "";}
			//asHistoryTable.getOstatus();
			String uid = asHistoryTable.getUid();
			String uname = asHistoryTable.getUname();
			String urank = asHistoryTable.getUrank();

			String out_reason = asHistoryTable.getTakeOutReason();
			String out_destination = asHistoryTable.getOutDestination();
			String u_date = asHistoryTable.getUdate();
			String tu_date = asHistoryTable.getTuDate();
			
			String tu_temp ="";
			if(!tu_date.equals("0")) {
				tu_temp=" ~ "+tu_date.substring(0,4)+"-"+tu_date.substring(4,6)+"-"+tu_date.substring(6,8);
			}  else { tu_temp = "";}
			
			String in_date = asHistoryTable.getInDate();
			if(!in_date.equals("0")) {
				in_date =" ~ "+in_date.substring(0,4)+"-"+in_date.substring(4,6)+"-"+in_date.substring(6,8);
			} else { in_date = "";}

			String as_status		= asHistoryTable.getAsStatus();
			String o_status			= asHistoryTable.getOstatus();
			String as_status_name	= asHistoryTable.getAsStatusName();
			String o_status_name	= asHistoryTable.getOstatusName();
			String link				= asHistoryTable.getLink();
			String pid				= asHistoryTable.getPid();
			String st_temp			= "";

			if(as_status.equals("10")||as_status.equals("11")||as_status.equals("12")||as_status.equals("13")||as_status.equals("17"))
			st_temp = "<IMG src='../am/images/lt_view_bt.gif' border='0' align='absmiddle'>";
%>
	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=o_status_name%>신청</TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=u_date.substring(0,4)%>-<%=u_date.substring(4,6)%>-<%=u_date.substring(6,8)%><%=in_date%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style='padding-left:5px'><%=out_reason%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=uname%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=writedate%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=link%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:entering_info('<%=h_no%>','<%=asno%>','<%=c_no%>','<%=o_status%>')"><%=st_temp%></a></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><a href="javascript:go_print('<%=h_no%>','<%=asno%>','<%=pid%>')"><IMG src='../am/images/lt_view_app.gif' align='absmiddle' border='0'></a></TD>
			</TR>
			<TR><TD colSpan=15 background="../am/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE></TD></TR>
	</TBODY>
</TABLE>
</FORM>
</body>
</html>


<script language="javascript">

	// 반출/이관/대여 Form으로 분기
	function as_moving(status){
		location.href ="../servlet/AssetServlet?mode=user_moving_req&as_no=<%=as_no%>&o_status="+status;
	}

	// 자산 처리 (입고/반출/이관/취소...)
	function go_as_process(u_date,u_id,h_no,as_no,o_status,as_status,mode){
	
		var txt_type="";
		
		if(o_status=="o" && as_status=="7") txt_type = "입고";
		if(o_status=="l" && as_status=="16") txt_type = "반납";
		if(o_status=="t" && ( as_status=="5" || as_status=="3")) {
				if(mode=="cancel_transfer") {
					txt_type="이관취소";
				} else {txt_type = "이관";
				}
		}
		if(o_status=="o" &&  as_status=="5" ) {
				if(mode=="cancel_out") {
					txt_type="반출취소";
				} else {txt_type = "반출";
				}
			
		}
		if(confirm(txt_type+" 처리 하시겠습니까?")) {
		location.href ="../servlet/AssetServlet?mode="+mode+"&u_id="+u_id+"&h_no="+h_no+"&as_no="+as_no+"&o_status="+o_status+"&as_status="+as_status+"&u_date="+u_date;
		} else {return
		}
	}

	// 반출/ 이관 상세 정보 보기 Form으로 분기
	function go_as_process2(u_id,h_no,asno,o_status,as_status,mode,div){
	
		var type;
		if(o_status=="반출") type = "o";
		if(o_status=="이관") type = "t";
		if(o_status=="대여") type = "l";
		
		location.href ="../servlet/AssetServlet?mode="+mode+"&u_id="+u_id+"&h_no="+h_no+"&as_no="+asno+"&o_status="+type+"&as_status="+as_status+"&div="+div+"&c_no="+<%=c_no%>;
		
	}

	function go_print(h_no,asno,pid)
	{
		var sParam = "../servlet/AssetServlet?mode=AppViewPrint&h_no="+h_no+"&as_no="+asno+"&pid="+pid+"&frmWidth=710&frmHeight=600";
		wopen(sParam,"","710","600","toolbar=0,location=0,directories=0,status=0,menuBar=0,scrollBars=0,resizable=0");
		
	}

	//상세보기
	function go_asInfo(as_no,c_no){
		location.href ="../servlet/AssetServlet?mode=asset_form&div=view&as_no="+as_no+"&c_no="+c_no;
	}

	function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}

	// 목록보기
	function go_list(){
		location.href = "../servlet/AssetServlet?mode=user_asset_list";
	}


	function entering_info(h_no,asno,c_no,o_status){
		var sParam = "../servlet/AssetServlet?mode=entering_info&h_no="+h_no+"&as_no="+asno+"&c_no="+c_no+"&o_status="+o_status+"&frmWidth=650&frmHeight=255";
		wopen(sParam,"","650","252","toolbar=0,location=0,directories=0,status=0,menuBar=0,scrollBars=0,resizable=0");
	}

</script>