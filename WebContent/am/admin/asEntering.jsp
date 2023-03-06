<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page 
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*,com.anbtech.am.entity.*,com.anbtech.date.anbDate"
%>
<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	com.anbtech.am.entity.AMUserTable auser = new com.anbtech.am.entity.AMUserTable();
	com.anbtech.am.entity.AsInfoTable asInfoTable;
	com.anbtech.am.entity.AsHistoryTable asHistoryTable;

	String status_temp = request.getParameter("as_status")==null?"":request.getParameter("as_status");
	//String sb=(String)request.getAttribute("CategoryList"); // 카테고리 ComboList
	String	div	  = request.getParameter("div");
	String c_date = anbdt.getDateNoformat();	
			
	 // -- 현재 사용자 정보
	auser = (com.anbtech.am.entity.AMUserTable)request.getAttribute("user");
	String user_id = auser.getUserId();
	String user_name = auser.getUserName();
	String user_rank = auser.getUserRank();
	
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
		   else as_statusinfo =" : "+as_statusinfo;
	String o_status = asHistoryTable.getOstatus();
	String wi_date = asHistoryTable.getWiDate();

	String as_status_name = asHistoryTable.getAsStatusName();
	String o_status_name = asHistoryTable.getOstatusName();

	
	String caption="입고신청";
	String info_str="<img src='../am/images/loan_info.gif' border='0'>";
	
		if(o_status.equals("t")) {
			info_str="<img src='../am/images/igwan.gif' border='0'>";
			caption="반출/이관업무";
		} else if(o_status.equals("o")){
			info_str="<img src='../am/images/ban_info.gif' border='0'>";
			caption="자산입고처리";
		} else if(o_status.equals("l")){
			if(as_status.equals("16")) {
				caption="자산 대여업무";
				info_str="<img src='../am/images/ban_in_info.gif'  border='0'>반납신청정보";
			} else {
				caption="자산 대여업무";
				info_str="<img src='../am/images/loan_out_info.gif'  border='0'>대여신청정보";
			}		
		}		
%>	

<HTML><HEAD><TITLE><%=caption%></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../am/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<form method='get' name='eForm'  action="../servlet/AssetServlet" onSubmit="return true">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../am/images/blet.gif" align="absmiddle"> <%=caption%></TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=32><!--버튼 및 페이징-->
			<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<TBODY>
				<TR><TD width=4>&nbsp;</TD>
					<TD align=left width='500'>
						<a href='javascript:go_submit()'><img src='../am/images/bt_confirm.gif' alt='입고' border='0' align="absmiddle"></a>
						<img src='../am/images/bt_cancel.gif' onclick="history.back()" border='0' style='cursor:hand' align='absmiddle'></TR></TBODY></TABLE></TD></TR>
		<TR height=100%>
		<TD vAlign=top>
<%	if(div.equals("")) { //입고처리화면	%>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">자산번호</TD>
					<TD width="35%" height="25" class="bg_04"><%=as_mid%></TD>
					<TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">모델명</TD>
					<TD width="35%" height="25" class="bg_04"><%=model_name%></TD></TR>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">품명</TD>
					<TD width="35%" height="25" class="bg_04"><%=as_name%></TD>
					<TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
					<TD width="35%" height="25" class="bg_04"><%=as_serial%></TD></TR>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">장비상태</TD>
					<TD width="85%"  height="25" class="bg_04" colspan="3">
							<SELECT name="as_status">
								<option value="12">정상</option>	
								<option value="13">수리요망</option>
							</SELECT></TD></TR>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">문제점 및 조치사항</TD>
					<TD width="85%" height="25" class="bg_04"  colspan="3">
					<textarea name="as_statusinfo" rows="2" cols="50"></textarea>
					<br>-장비상태가 정상이 아닐경우에는 문제점과 조치사항을 입력하세요.</TD></TR>
				<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">입고일자</TD>
				<TD width="85%" height="25" class="bg_04"  colspan="3"><input type=text name='in_date' size=12 value='<%=c_date.substring(0,4)%>-<%=c_date.substring(4,6)%>-<%=c_date.substring(6,8)%>'>
				<a href="javascript:wopen('../am/admin/Calendar.jsp?FieldName=in_date', '', 180, 250);"><img src='../am/images/bt_calendar.gif' border='0' align='absmiddle'></a>
			</TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>

<input type="hidden" name="h_no" value="<%=h_no%>">
<input type="hidden" name="c_no" value="<%=c_no%>">
<input type="hidden" name="as_no" value="<%=as_no_h%>">
<%	if ("l".equals(o_status)) { %>	
		<input type="hidden" name="mode" value="lending_input">
<%	}else {	%>
		<input type="hidden" name="mode" value="out_Input">
<% 	}%> 
<input type="hidden" name="w_name"  value='<%=wname%>' >
<input type="hidden" name='w_id'    value='<%=wid%>'> 
<input type="hidden" name='w_rank'  value='<%=wrank%>'> 
<input type="hidden" name='wi_date' value='<%=c_date%>' >
<input type="hidden" name="u_name"  value="<%=u_name%>">
<input type="hidden" name="u_id"    value="<%=u_id%>"> 
<input type="hidden" name='takeout_reason'  value="<%=takeout_reason%>">
	  
<%  }else if(div.equals("view")){  // 입고정보 보기 화면	%>		
		<TABLE width=100% cellSpacing=0 cellPadding=0 width="100%" border=0 >
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="100%" colspan="4"  height=28><img src="../am/images/am_info.gif" ></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif" >자산번호</TD>
				<TD width="35%" height="25" class="bg_04"><%=as_mid%></TD>
				<TD width="15%" height="25" class="bg_03" background="../am/images/bg-01.gif">고유번호</TD>
				<TD width="35%" height="25" class="bg_04"><%=as_serial%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">품&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</TD>
				<TD width="35%" height="25" class="bg_04"><%=as_name%></TD>
				<TD width="15%" class="bg_03" background="../am/images/bg-01.gif">모&nbsp;&nbsp;델&nbsp;&nbsp;명</TD>
				<TD width="35%" height="25" class="bg_04"><%=model_name%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">규&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;격</TD>
				<TD width="35%" height="25" class="bg_04"  ><%=as_setting%></TD>
				<TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">사용자</TD>
				<TD width="35%" height="25" class="bg_04"  ><%=crr_name%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>
		
		<TABLE width=100% cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TR><TD width="100%" colspan="4"  height=28><%=info_str%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">작 성 일</TD>
				<TD width="35%" height="25" class="bg_04"  ><%=write_date.substring(0,4)%>-<%=write_date.substring(4,6)%>-<%=write_date.substring(6,8)%></TD>
				<TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">작 성 자</TD>
				<TD width="35%" height="25" class="bg_04"  ><%=w_name_h%></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">인 수 자</TD>
				<TD width="85%" height="25" class="bg_04" colspan='3' ><%=u_name%></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>

<input type="hidden" name="mode" value="out_Input">
<input type="hidden" name="h_no" value="<%=h_no%>">
<input type="hidden" name="w_name" value='<%=w_name_h%>' size=15>
<input type=hidden name='c_date' size=15 value='<%=write_date.substring(0,4)%>-<%=write_date.substring(4,6)%>-<%=write_date.substring(6,8)%>'>
<input type="hidden" name="u_name" size=15 value="<%=u_name%>" readonly>

<%	
		if("o".equals(o_status) || "l".equals(o_status))  {  //  입고일경우
%>			
			<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">사유</TD>
				<TD width="85%" height="25" class="bg_04" colspan='3' ><%=takeout_reason%>
					<input type=hidden name='takeout_reason' size=20 value="<%=takeout_reason%>" readonly></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
<%
				if("12".equals(as_status)) {
%>
				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">반납일시</TD>
					<TD width="35%" height="25" class="bg_04"  ><%=tu_date.substring(0,4)%>-<%=tu_date.substring(4,6)%>-<%=tu_date.substring(6,8)%>
					<TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">실제반납일시</TD>
					<TD width="35%" height="25" class="bg_04"  ><%=in_date.substring(0,4)%>-<%=in_date.substring(4,6)%>-<%=in_date.substring(6,8)%></TD></TR>
					<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
<%				}			
%>				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">처리결과</TD>
					<TD width="85%" height="25" class="bg_04" colspan='3' ><%=as_status_name%><%=as_statusinfo%></TR>
					<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>

<%		} else if("t".equals(o_status)) {  // 이관일경우
%>				<TR><TD width="15%"  height="25" class="bg_03" background="../am/images/bg-01.gif">이관일시</TD>
					<TD width="85%" height="25" class="bg_04"   colspan='3'><%=u_date.substring(0,4)%>-<%=u_date.substring(4,6)%>-<%=u_date.substring(6,8)%></TR>
					
<%		}
%>		
		</TABLE>
<%	}
%>	  
		</TD></TR>
	</TBODY>
</TABLE>
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

	function go_submit(){
	
		var  temp = f.as_status.value
		var temp2 = f.as_statusinfo.value;
	
		if(temp==13 && temp2=="") {
			alert("문제점 및 조치사항을 기입해 주십시오.");
			return;
		}
		f.submit();
	}

	function go_change(){
		var status = f.as_status.value;
		var as_no = f.as_no.value;
		var c_no = f.c_no.value;
		var h_no = f.h_no.value;
		//var o_status = f.o_status.value;
			
		location.href ="../servlet/AssetServlet?mode=out_InputForm&c_no="+c_no+"&h_no="+h_no+"&as_no="+as_no+"&as_status="+status+"&o_status=<%=o_status%>";
	
	}
	
	function list_go() {		
		location.href ="../servlet/AssetServlet?mode=user_asset_list&c_no";				
	}

	function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}

</script>