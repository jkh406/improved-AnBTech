<%@ include file="admin/configHead.jsp"%>
<%@ page		
	info= "상단메뉴"		
	contentType = "text/html; charset=euc-kr" 		
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//변수 선언
	String sql = "",attorney_id="";

	//대리결재자 지정여부 판단하기
	sql = "select attorney_id from app_attorney where approval_id = '"+login_id+"'";
	bean.openConnection();	
	bean.executeQuery(sql);
	if(bean.next()){
		attorney_id = bean.getData(1);
	}

%>
<HTML>
<HEAD>
<TITLE>WEBFFICE GROUPWARE</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="css/style.css" rel=stylesheet>
<!--menu-->
<SCRIPT language=JavaScript>
	//부재중 대리결재자 지정여부 알려주기
	var al = '<%=attorney_id%>';
	if(al.length > 0) alert('부재중 대리결재인이 지정되어 있습니다.');


	var oldobj = "Layer1";
	var oldsub = "Layer11";
	var oldover = "Layer11";

	function fnMouseOver(id){
		document.all[oldover].style.visibility = 'hidden';
		document.all[id].style.visibility = 'visible';
		oldover = id;
	}

	function fnMouseOut(id){
		document.all[id].style.visibility = 'hidden';
		document.all[oldsub].style.visibility = 'visible';
		oldover = oldsub;
	}

	function fnMouseClick(id,subid){
		document.all[oldobj].style.visibility = 'hidden';
		document.all[oldsub].style.visibility = 'hidden';
		oldobj = id;
		oldsub = subid;
		document.all[id].style.visibility = 'visible';
		document.all[subid].style.visibility = 'visible';
	}
</SCRIPT>
<!--menu end-->

<SCRIPT language=JavaScript>

// 사우정보창
function viewUserInfo()
{
	wopen("admin/searchUsers.jsp?","User_Info","700","466");
}

// 로그아웃 처리
function logoff() {
	var message = confirm("로그아웃하시겠습니까?");
	if(message == true) top.location.href = "admin/logout.jsp";
}   

// 암호변경
function go_password() {
	wopen("admin/change_passwd.jsp","PWD","410","225");
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}
</SCRIPT>
</HEAD>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 oncontextmenu="return false">
<!--일정관리 -->
<DIV id=Layer1 style="Z-INDEX: 1; LEFT: 175px; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="gw/schedule/CalendarBody.htm" target="body"><IMG height=48 src="images/top_menu1b.jpg" width=94 border=0></A></DIV>
<DIV id=Layer11 style="Z-INDEX: 11; LEFT: 0px; POSITION: absolute; TOP: 0px"><BR>
<TABLE 
onmouseover="a.src='images/top_menu1b.jpg';fnMouseOver('Layer11')" 
onmouseout="a.src='images/top_menu1a.jpg'; fnMouseOut('Layer11');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 전자우편 -->
<DIV id=Layer2 style="Z-INDEX: 2; LEFT: 269px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="gw/mail/MailBody.htm" target="body"><IMG height=48 src="images/top_menu2b.jpg" width=94 border=0></A></DIV>
<DIV id=Layer12 style="Z-INDEX: 12; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="b.src='images/top_menu2b.jpg';fnMouseOver('Layer12')" onmouseout="b.src='images/top_menu2a.jpg'; fnMouseOut('Layer12');" cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 전자결재 -->
<DIV id=Layer3 
style="Z-INDEX: 3; LEFT: 363px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" href="gw/approval/ApprovalBody.jsp" target="body"><IMG height=48 src="images/top_menu3b.jpg" width=94 border=0></A></DIV>
<DIV id=Layer13 style="Z-INDEX: 13; LEFT: 340px; VISIBILITY: hidden; POSITION: absolute; TOP: 73px">
<TABLE 
onmouseover="c.src='images/top_menu3b.jpg';fnMouseOver('Layer13')" 
onmouseout="c.src='images/top_menu3a.jpg'; fnMouseOut('Layer13');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 공문관리 -->
<DIV id=Layer4 style="Z-INDEX: 4; LEFT: 457px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="ods/odsFrame.jsp" 
target="body"><IMG height=48 src="images/top_menu4b.jpg" width=94 border=0></A></DIV>
<DIV id=Layer14 style="Z-INDEX: 14; LEFT: 401px; VISIBILITY: hidden; POSITION: absolute; TOP: 64px"><!--
<TABLE 
onmouseover="d.src='images/top_menu4b.jpg';fnMouseOver('Layer14')" onclick="JavaScript:fnMouseClick('Layer4','Layer14')" 
onmouseout="d.src='images/top_menu4a.jpg'; fnMouseOut('Layer14');" cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD colSpan=11 height=8></TD></TR>
  <TR>
    <TD>
      <TABLE cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
        <TR>
          <TD><A onfocus="this.blur()" 
            href="../board/BbsBody.jsp?bbs=notice_board" 
            target="body">공지사항</A></TD>
          <TD><IMG height=25 src="images/line1.gif" width=17 border=0></TD>
          <TD><A onfocus="this.blur()" 
            href="../board/BbsBody.jsp?bbs=free_board" 
            target="body">자유게시판</A></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>--></DIV>

<!-- 전자게시 -->
<DIV id=Layer5 
style="Z-INDEX: 5; LEFT: 551px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" 
href="board/BbsBody.jsp" 
target="body"><IMG height=48 src="images/top_menu5b.jpg" width=94 
border=0></A></DIV>
<DIV id=Layer15 
style="Z-INDEX: 15; LEFT: 510px; VISIBILITY: hidden; POSITION: absolute; TOP: 65px">
<TABLE 
onmouseover="e.src='images/top_menu5b.jpg';fnMouseOver('Layer15')" 
onmouseout="e.src='images/top_menu5a.jpg'; fnMouseOut('Layer15');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD colSpan=11 height=8></TD></TR>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 근태관리 -->
<DIV id=Layer6 
style="Z-INDEX: 6; LEFT: 645px; VISIBILITY: hidden; WIDTH: 95px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" 
href="es/GeunTaeBody.jsp" 
target="body"><IMG height=48 src="images/top_menu6b.jpg" width=95 
border=0></A></DIV>
<DIV id=Layer16 
style="Z-INDEX: 16; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE 
onmouseover="f.src='images/top_menu6b.jpg';fnMouseOver('Layer16')" 
onmouseout="f.src='images/top_menu6a.jpg'; fnMouseOut('Layer16');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 특근관리 -->
<DIV id=Layer7 
style="Z-INDEX: 7; LEFT: 740px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" href="ew/EwBody.htm" 
target="body"><IMG height=48 src="images/top_menu7b.jpg" width=94 
border=0></A></DIV>
<DIV id=Layer17 
style="Z-INDEX: 17; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE 
onmouseover="g.src='images/top_menu7b.jpg';fnMouseOver('Layer17')" 
onmouseout="g.src='images/top_menu7a.jpg'; fnMouseOut('Layer17');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 자원예약 -->
<DIV id=Layer8 
style="Z-INDEX: 8; LEFT: 834px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" 
href="br/ResourceBody.htm" 
target="body"><IMG height=48 src="images/top_menu8b.jpg" width=94 
border=0></A></DIV>
<DIV id=Layer18 
style="Z-INDEX: 18; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE 
onmouseover="h.src='images/top_menu8b.jpg';fnMouseOver('Layer18')" 
onmouseout="h.src='images/top_menu8a.jpg'; fnMouseOut('Layer18');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD></TD></TR></TBODY></TABLE></DIV>

<!-- 고객관리 -->
<DIV id=Layer9 
style="Z-INDEX: 9; LEFT: 928px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" href="gw/service/ServiceBody.htm" target="body"><IMG height=48 
src="images/top_menu9b.jpg" width=94 border=0></A></DIV>
<DIV id=Layer19 
style="Z-INDEX: 19; LEFT: 642px; VISIBILITY: hidden; POSITION: absolute; TOP: 52px">
<!--
<TABLE 
onmouseover="i.src='images/top_menu9b.jpg';fnMouseOver('Layer19')" 
onclick="JavaScript:fnMouseClick('Layer9','Layer19')" 
onmouseout="i.src='images/top_menu9a.jpg'; fnMouseOut('Layer19');" 
cellSpacing=0 cellPadding=0 border=0>
  <TBODY>
  <TR>
    <TD colSpan=11 height=8></TD></TR>
  <TR>
    <TD>
      <TABLE cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
        <TR>
          <TD><A onfocus="this.blur()" href="javascript:go_Edm('전사');"><IMG 
            height=25 src="images/w0.gif" width=20 border=0></A></TD>
          <TD><IMG height=25 src="images/line1.gif" width=17 border=0></TD>
          <TD><A onfocus="this.blur()" href="javascript:go_Edm('기술문서');"><IMG 
            height=25 src="images/w6.gif" width=42 border=0></A></TD>
          <TD><IMG height=25 src="images/line1.gif" width=17 border=0></TD>
          <TD><A onfocus="this.blur()" href="javascript:go_Edm('제안서');"><IMG 
            height=25 src="images/w2.gif" width=42 border=0></A></TD>
          <TD><IMG height=25 src="images/line1.gif" width=17 border=0></TD>
          <TD><A onfocus="this.blur()" href="javascript:go_Edm('일반문서');"><IMG 
            height=25 src="images/w7.gif" width=42 border=0></A></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>--></DIV>

<!------------------------------********레이어 끝********----------------------------------------->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD width=174 rowSpan=3>
      <TABLE cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
			<TR><TD height=71  align="middle">
				<EMBED src="images/logo_n.swf" quality="high" bgcolor="#FFFFFF"
         WIDTH=174 HEIGHT=71 TYPE="application/x-shockwave-flash"         
  PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?
                P1_Prod_Version=ShockwaveFlash"></EMBED></TD></TR>
			<TR>
				<TD align=right background=images/text_bg.gif height=26>
				<TABLE cellSpacing=0 cellPadding=0 width=174 border=0>
					<TBODY>
						<TR><TD align=middle height=3></TD></TR>
            <TR>
                <TD align=middle><IMG height=9 src="images/icon.gif" 
                 width=18><%=login_name%>/<%=login_id%></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD>
    <TD width=1 bgColor=#cbcbcb rowSpan=3><IMG height=1 
      src="images/blank.gif" width=1></TD>
    <TD height=59>
      <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR>
          <TD align=right width=972 bgColor=#ffffff height=22>
            <TABLE cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
              <TR>
                <TD><A onfocus="this.blur()" href="javascript:logoff();"><IMG src="images/logoff.jpg" width=59 height=23 border=0></A></TD>
                <TD><A onfocus="this.blur()" href="javascript:go_password();"><IMG src="images/chg_passwd.jpg" width=80 height=23 border=0></A></TD>
                <TD><A onfocus="this.blur()" href="javascript:viewUserInfo();"><IMG src="images/user_info.jpg" width=57 height=23 border=0></A></TD>
                <TD><A onfocus="this.blur()" href="admin/AdminBody.jsp" target="body"><IMG src="images/go_admin.jpg" width=70 height=23 border=0></A></TD></TR></TBODY></TABLE></TD></TR>

        <TR>
          <TD width=100% background=images/bg1.gif height=48><!------------------------------********메뉴********----------------------------------------->
            <TABLE cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
              <TR>
                <TD width=94><A onfocus="this.blur()" 
                  href="gw/schedule/CalendarBody.htm" 
                  target="body"><IMG 
                  onmouseover="a.src='images/top_menu1b.jpg';fnMouseOver('Layer11')" 
                  onclick="JavaScript:fnMouseClick('Layer1','Layer11')" 
                  onmouseout="a.src='images/top_menu1a.jpg';fnMouseOut('Layer11')" 
                  height=48 alt="일정관리" src="images/top_menu1a.jpg" width=94 
                  border=0 name=a></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="gw/mail/MailBody.htm" 
                  target="body"><IMG 
                  onmouseover="b.src='images/top_menu2b.jpg';fnMouseOver('Layer12')" 
                  onclick="JavaScript:fnMouseClick('Layer2','Layer12')" 
                  onmouseout="b.src='images/top_menu2a.jpg';fnMouseOut('Layer12')" 
                  height=48 alt="전자우편" src="images/top_menu2a.jpg" width=94 
                  border=0 name=b></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="gw/approval/ApprovalBody.jsp" 
                  target="body"><IMG 
                  onmouseover="c.src='images/top_menu3b.jpg';fnMouseOver('Layer13')" 
                  onclick="JavaScript:fnMouseClick('Layer3','Layer13')" 
                  onmouseout="c.src='images/top_menu3a.jpg';fnMouseOut('Layer13')" 
                  height=48 alt="전자결재" src="images/top_menu3a.jpg" width=94 
                  border=0 name=c></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="ods/odsFrame.jsp" 
                  target="body"><IMG 
                  onmouseover="d.src='images/top_menu4b.jpg';fnMouseOver('Layer14')" 
                  onclick="JavaScript:fnMouseClick('Layer4','Layer14')" 
                  onmouseout="d.src='images/top_menu4a.jpg';fnMouseOut('Layer14')" 
                  height=48 alt="공문관리" src="images/top_menu4a.jpg" width=94 
                  border=0 name=d></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="board/BbsBody.jsp" 
                  target="body"><IMG 
                  onmouseover="e.src='images/top_menu5b.jpg';fnMouseOver('Layer15')" 
                  onclick="JavaScript:fnMouseClick('Layer5','Layer15')" 
                  onmouseout="e.src='images/top_menu5a.jpg';fnMouseOut('Layer15')" 
                  height=48 alt="전자게시" src="images/top_menu5a.jpg" width=94 
                  border=0 name=e></A></TD>
                <TD width=95><A onfocus="this.blur()" 
                  href="es/GeunTaeBody.jsp" 
                  target="body"><IMG 
                  onmouseover="f.src='images/top_menu6b.jpg';fnMouseOver('Layer16')" 
                  onclick="JavaScript:fnMouseClick('Layer6','Layer16')" 
                  onmouseout="f.src='images/top_menu6a.jpg';fnMouseOut('Layer16')" 
                  height=48 alt="근태관리" src="images/top_menu6a.jpg" width=95 
                  border=0 name=f></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="ew/EwBody.htm" 
                  target="body"><IMG 
                  onmouseover="g.src='images/top_menu7b.jpg';fnMouseOver('Layer17')" 
                  onclick="JavaScript:fnMouseClick('Layer7','Layer17')" 
                  onmouseout="g.src='images/top_menu7a.jpg';fnMouseOut('Layer17')" 
                  height=48 alt="특근관리" src="images/top_menu7a.jpg" width=94 
                  border=0 name=g></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="br/ResourceBody.htm" 
                  target="body"><IMG 
                  onmouseover="h.src='images/top_menu8b.jpg';fnMouseOver('Layer18')" 
                  onclick="JavaScript:fnMouseClick('Layer8','Layer18')" 
                  onmouseout="h.src='images/top_menu8a.jpg';fnMouseOut('Layer18')" 
                  height=48 alt="자원예약" src="images/top_menu8a.jpg" width=94 
                  border=0 name=h></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="gw/service/ServiceBody.htm" target="body"><IMG 
                  onmouseover="i.src='images/top_menu9b.jpg';fnMouseOver('Layer19')" 
                  onclick="JavaScript:fnMouseClick('Layer9','Layer19')" 
                  onmouseout="i.src='images/top_menu9a.jpg';fnMouseOut('Layer19')" 
                  height=48 alt="고객관리" src="images/top_menu9a.jpg" width=94 
                  border=0 name=i></A></TD></TR></TBODY></TABLE><!------------------------------********메뉴 끝********-----------------------------------------></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD bgColor=#c1c1c1 height=1></TD></TR>
  <TR>
    <TD background=images/sub_bg.gif height=25></TD></TR></TBODY></TABLE>
</BODY></HTML>
