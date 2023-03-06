<%@ include file="admin/configHead.jsp"%>
<%@ page		
	info= "상단메뉴"		
	contentType = "text/html; charset=euc-kr" 		
%>
<HTML>
<HEAD>
<TITLE>WEBFFICE ERP</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="css/style.css" rel=stylesheet>
<!--menu-->
<SCRIPT language=JavaScript>
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

<SCRIPT language=JavaScript>

// 사원정보
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

<!-- 구매관리 1-->
<DIV id=Layer1 style="Z-INDEX: 1; LEFT: 175px; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="pu/PuBody.htm" target="body"><IMG height=48 src="images/top_menu3f.gif" width=94 border=0></A></DIV>
<DIV id=Layer11 style="Z-INDEX: 11; LEFT: 0px; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="a.src='images/top_menu3e.gif';fnMouseOver('Layer11')" onmouseout="a.src='images/top_menu3e.gif'; fnMouseOut('Layer11');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 재고관리 2-->
<DIV id=Layer2 style="Z-INDEX: 2; LEFT: 269px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="st/StBody.htm" target="body"><IMG height=48 src="images/top_menu4f.gif" width=94 border=0></A></DIV>
<DIV id=Layer12 style="Z-INDEX: 12; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP:0px">
<TABLE onmouseover="b.src='images/top_menu4e.gif';fnMouseOver('Layer12')" onmouseout="b.src='images/top_menu4e.gif'; fnMouseOut('Layer12');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 생산관리 3-->
<DIV id=Layer3 
style="Z-INDEX: 3; LEFT: 363px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" href="mm/mmBody.jsp" target="body"><IMG height=48 src="images/top_menu5f.gif" width=94 border=0></A></DIV>
<DIV id=Layer13 style="Z-INDEX: 13; LEFT: 340px; VISIBILITY: hidden; POSITION: absolute; TOP: 73px">
<TABLE onmouseover="c.src='images/top_menu5e.gif';fnMouseOver('Layer13')" onmouseout="c.src='images/top_menu5e.gif'; fnMouseOut('Layer13');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 품질관리 4-->
<DIV id=Layer4 style="Z-INDEX: 4; LEFT: 457px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="qc/QcBody.htm" target="body"><IMG height=48 src="images/top_menu6f.gif" width=94 border=0></A></DIV>
<DIV id=Layer14 style="Z-INDEX: 14; LEFT: 510px; VISIBILITY: hidden; POSITION: absolute; TOP: 65px">
<TABLE onmouseover="d.src='images/top_menu6e.gif';fnMouseOver('Layer14')" onmouseout="d.src='images/top_menu6e.gif'; fnMouseOut('Layer14');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 서비스 5
<DIV id=Layer5 style="Z-INDEX: 5; LEFT: 551px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="sc/ScBody.jsp" target="body"><IMG height=48 src="images/top_menu7f.gif" width=94 border=0></A></DIV>
<DIV id=Layer15 style="Z-INDEX: 15; LEFT:0px; VISIBILITY: hidden; POSITION: absolute; TOP:0px">
<TABLE onmouseover="e.src='images/top_menu7f.gif';fnMouseOver('Layer15')" onmouseout="e.src='images/top_menu7e.gif'; fnMouseOut('Layer17');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>-->

<!--자산관리 6-->
<DIV id=Layer5 style="Z-INDEX: 5; LEFT: 551px; WIDTH: 95px; VISIBILITY: hidden; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="am/AmBody.htm" target="body"><IMG height=48 src="images/top_menu2f.gif" width=95 border=0></A></DIV>
<DIV id=Layer15 style="Z-INDEX: 15; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="f.src='images/top_menu2e.gif';fnMouseOver('Layer15')" onmouseout="f.src='images/top_menu2e.gif'; fnMouseOut('Layer15');" cellSpacing=0 cellPadding=0 border=0>
  <TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 견적서관리 7-->
<DIV id=Layer6 style="Z-INDEX: 6; LEFT: 646px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="em/EmBody.htm" target="body"><IMG height=48 src="images/top_menu1f.gif" width=94 border=0></A></DIV>
<DIV id=Layer16 style="Z-INDEX: 16; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="g.src='images/top_menu1e.gif';fnMouseOver('Layer16')" onmouseout="g.src='images/top_menu1e.gif'; fnMouseOut('Layer16');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 영업관리 -->
<DIV id=Layer9 style="Z-INDEX: 9; LEFT: 740px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="bs/bsBody.htm" target="body"><IMG height=48 src="images/top_menu9d.gif" width=94 border=0></A></DIV>
<DIV id=Layer19 style="Z-INDEX: 19; LEFT: 510px; VISIBILITY: hidden; POSITION: absolute; TOP: 65px">
<TABLE onmouseover="h.src='images/top_menu9d.gif';fnMouseOver('Layer19')" onmouseout="h.src='images/top_menu9c.gif'; fnMouseOut('Layer19');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- BarCode 7-->
<DIV id=Layer10 style="Z-INDEX: 7; LEFT: 740px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="sc/ScBody.htm" target="body"><IMG height=48 src="images/top_menu9d.gif" width=94 border=0></A></DIV>
<DIV id=Layer20 style="Z-INDEX: 17; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="h.src='images/top_menu9d.gif';fnMouseOver('Layer20')" onmouseout="h.src='images/top_menu9c.gif'; fnMouseOut('Layer20');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>
<!------------------------------********레이어 끝********----------------------------------------->

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TBODY>
  <TR>
    <TD width=174 rowSpan=3>
      <TABLE cellSpacing=0 cellPadding=0 border=0>
        <TBODY>
        <TR>
          <TD height=71 align="middle"><EMBED src="images/logo_n.swf" quality="high" bgcolor="#FFFFFF"
         WIDTH=174 HEIGHT=71 TYPE="application/x-shockwave-flash"         
  PLUGINSPAGE="http://www.macromedia.com/shockwave/download/index.cgi?
                P1_Prod_Version=ShockwaveFlash"></EMBED></TD></TR>
        <TR>
          <TD align=right background=images/text_bg.gif height=26>
            <TABLE cellSpacing=0 cellPadding=0 width=174 border=0>
              <TBODY>
              <TR>
                <TD align=middle height=3></TD></TR>
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
          <TD width='100%' height='48' background='images/bg1.gif'>
		  <!------------------------------********메뉴********----------------------------------------->
            <TABLE cellSpacing=0 cellPadding=0 border=0>
              <TBODY>
              <TR>
			    
				<TD width=94><A onfocus="this.blur()" 
                  href="pu/PuBody.htm" 
                  target="body"><IMG 
                  onmouseover="a.src='images/top_menu3f.gif';fnMouseOver('Layer11')" 
                  onclick="JavaScript:fnMouseClick('Layer1','Layer11')" 
                  onmouseout="a.src='images/top_menu3e.gif';fnMouseOut('Layer11')" 
                  height=48 alt="구매관리" src="images/top_menu3e.gif" width=94 
                  border=0 name=a></A></TD>

                <TD width=94><A onfocus="this.blur()" 
                  href="st/StBody.htm" 
                  target="body"><IMG 
                  onmouseover="b.src='images/top_menu4f.gif';fnMouseOver('Layer12')" 
                  onclick="JavaScript:fnMouseClick('Layer2','Layer12')" 
                  onmouseout="b.src='images/top_menu4e.gif';fnMouseOut('Layer12')" 
                  height=48 alt="재고관리" src="images/top_menu4e.gif" width=94 
                  border=0 name=b></A></TD>

				<TD width=94><A onfocus="this.blur()" 
                  href="mm/mmBody.jsp" 
                  target="body"><IMG 
                  onmouseover="c.src='images/top_menu5f.gif';fnMouseOver('Layer13')" 
                  onclick="JavaScript:fnMouseClick('Layer3','Layer13')" 
                  onmouseout="c.src='images/top_menu5e.gif';fnMouseOut('Layer13')" 
                  height=48 alt="생산관리" src="images/top_menu5e.gif" width=94 
                  border=0 name=c></A></TD>

				 <TD width=94><A onfocus="this.blur()" 
                  href="qc/QcBody.htm" 
                  target="body"><IMG 
                  onmouseover="d.src='images/top_menu6f.gif';fnMouseOver('Layer14')" 
                  onclick="JavaScript:fnMouseClick('Layer4','Layer14')" 
                  onmouseout="d.src='images/top_menu6e.gif';fnMouseOut('Layer14')" 
                  height=48 alt="품질관리" src="images/top_menu6e.gif" width=94 
                  border=0 name=d></A></TD>
<!--
				<TD width=94><A onfocus="this.blur()" href="sc/ScBody.jsp" target="body"><IMG onmouseover="e.src='images/top_menu7f.gif';fnMouseOver('Layer15')" onclick="JavaScript:fnMouseClick('Layer5','Layer15')" onmouseout="e.src='images/top_menu7f.gif';fnMouseOut('Layer15')" height=48 alt="서비스지원" src="images/top_menu7e.gif" width=94 border=0 name=e></A></TD>
-->
				<TD width=95><A onfocus="this.blur()" href="am/AmBody.htm" target="body"><IMG onmouseover="f.src='images/top_menu2f.gif';fnMouseOver('Layer15')" onclick="JavaScript:fnMouseClick('Layer5','Layer15')" 
                onmouseout="f.src='images/top_menu2e.gif';fnMouseOut('Layer15')" height=48 alt="자산관리" src="images/top_menu2e.gif" width=95 border=0 name=f></A></TD>

                <TD width=94><A onfocus="this.blur()" 
                  href="em/EmBody.htm" 
                  target="body"><IMG 
                  onmouseover="g.src='images/top_menu1f.gif';fnMouseOver('Layer16')" 
                  onclick="JavaScript:fnMouseClick('Layer6','Layer16')" 
                  onmouseout="g.src='images/top_menu1e.gif';fnMouseOut('Layer16')" 
                  height=48 alt="견적서관리" src="images/top_menu1e.gif" width=94 
                  border=0 name=g></A></TD>
                <TD width=94><A onfocus="this.blur()" 
                  href="bs/bsBody.htm" 
                  target="body"><IMG 
                  onmouseover="h.src='images/top_menu9d.gif';fnMouseOver('Layer19')" 
                  onclick="JavaScript:fnMouseClick('Layer9','Layer19')" 
                  onmouseout="h.src='images/top_menu9c.gif';fnMouseOut('Layer19')" 
                  height=48 alt="영업관리" src="images/top_menu9c.gif" width=94 
                  border=0 name=h></A></TD>
				<TD width=94><A onfocus="this.blur()" 
                  href="sc/ScBody.htm" 
                  target="body"><IMG 
                  onmouseover="h.src='images/top_menu9d.gif';fnMouseOver('Layer20')" 
                  onclick="JavaScript:fnMouseClick('Layer10','Layer20')" 
                  onmouseout="h.src='images/top_menu9c.gif';fnMouseOut('Layer20')" 
                  height=48 alt="BARcode관리" src="images/top_menu9c.gif" width=94 
                  border=0 name=h></A></TD>
				  
				  </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD bgColor=#c1c1c1 height=1></TD></TR>
  <TR>
    <TD background=images/sub_bg.gif height=25></TD></TR></TBODY></TABLE>
</BODY></HTML>
