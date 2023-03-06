<%@ include file="admin/configHead.jsp"%>
<%@ page		
	info= "상단메뉴"		
	contentType = "text/html; charset=euc-kr" 		
%>
<HTML>
<HEAD>
<TITLE>WEBFFICE PDM</TITLE>
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

<!-- 제품정보관리 1-->
<DIV id=Layer1 style="Z-INDEX: 1; LEFT: 175px; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="gm/GmBody.htm" target="body"><IMG height=48 src="images/top_menu4d.gif" width=94 border=0></A></DIV>
<DIV id=Layer11 style="Z-INDEX: 11; LEFT: 0px; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="a.src='images/top_menu4d.gif';fnMouseOver('Layer11')" onmouseout="a.src='images/top_menu4c.gif'; fnMouseOut('Layer11');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 과제상태관리 2-->
<DIV id=Layer2 style="Z-INDEX: 2; LEFT: 269px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="psm/psmBody.jsp" target="body"><IMG height=48 src="images/top_menu8d.gif" width=94 border=0></A></DIV>
<DIV id=Layer12 style="Z-INDEX: 12; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP:0px">
<TABLE onmouseover="b.src='images/top_menu8d.gif';fnMouseOver('Layer12')" onmouseout="b.src='images/top_menu8c.gif'; fnMouseOut('Layer12');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 부품마스터 3-->
<DIV id=Layer3 
style="Z-INDEX: 3; LEFT: 363px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A 
onfocus="this.blur()" href="cm/CmBody.htm" target="body"><IMG height=48 src="images/top_menu3d.gif" width=94 border=0></A></DIV>
<DIV id=Layer13 style="Z-INDEX: 13; LEFT: 340px; VISIBILITY: hidden; POSITION: absolute; TOP: 73px">
<TABLE onmouseover="c.src='images/top_menu3d.gif';fnMouseOver('Layer13')" onmouseout="c.src='images/top_menu3c.gif'; fnMouseOut('Layer13');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- BOM관리 4-->
<DIV id=Layer4 style="Z-INDEX: 4; LEFT: 457px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="bm/bmBody.jsp" target="body"><IMG height=48 src="images/top_menu6d.gif" width=94 border=0></A></DIV>
<DIV id=Layer14 style="Z-INDEX: 14; LEFT: 510px; VISIBILITY: hidden; POSITION: absolute; TOP: 65px">
<TABLE onmouseover="d.src='images/top_menu6d.gif';fnMouseOver('Layer14')" onmouseout="d.src='images/top_menu6c.gif'; fnMouseOut('Layer14');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 설계변경관리 5-->
<DIV id=Layer5 style="Z-INDEX: 5; LEFT: 551px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="dcm/dcmBody.jsp" target="body"><IMG height=48 src="images/top_menu7d.gif" width=94 border=0></A></DIV>
<DIV id=Layer15 style="Z-INDEX: 15; LEFT:0px; VISIBILITY: hidden; POSITION: absolute; TOP:0px">
<TABLE onmouseover="e.src='images/top_menu7d.gif';fnMouseOver('Layer15')" onmouseout="e.src='images/top_menu7c.gif'; fnMouseOut('Layer17');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!--기술문서관리 6-->
<DIV id=Layer6 style="Z-INDEX: 6; LEFT: 645px; WIDTH: 95px; VISIBILITY: hidden; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="dms/DmsBody.html" target="body"><IMG height=48 src="images/top_menu1d.gif" width=95 border=0></A></DIV>
<DIV id=Layer16 style="Z-INDEX: 16; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="f.src='images/top_menu1d.gif';fnMouseOver('Layer16')" onmouseout="f.src='images/top_menu1c.gif'; fnMouseOut('Layer16');" cellSpacing=0 cellPadding=0 border=0>
  <TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 승인원관리 7-->
<DIV id=Layer7 style="Z-INDEX: 7; LEFT: 740px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="ca/CaBody.htm" target="body"><IMG height=48 src="images/top_menu2d.gif" width=94 border=0></A></DIV>
<DIV id=Layer17 style="Z-INDEX: 17; LEFT: 0px; VISIBILITY: hidden; POSITION: absolute; TOP: 0px">
<TABLE onmouseover="g.src='images/top_menu2d.gif';fnMouseOver('Layer17')" onmouseout="g.src='images/top_menu2c.gif'; fnMouseOut('Layer17');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>

<!-- 프로젝트관리 8-->
<DIV id=Layer8 style="Z-INDEX: 8; LEFT: 834px; VISIBILITY: hidden; WIDTH: 94px; POSITION: absolute; TOP: 23px; HEIGHT: 48px"><A onfocus="this.blur()" href="pjt/pjtBody.jsp" target="body"><IMG height=48 src="images/top_menu5d.gif" width=94 border=0></A></DIV>
<DIV id=Layer18 style="Z-INDEX: 18; LEFT: 510px; VISIBILITY: hidden; POSITION: absolute; TOP: 65px">
<TABLE onmouseover="h.src='images/top_menu5d.gif';fnMouseOver('Layer18')" onmouseout="h.src='images/top_menu5c.gif'; fnMouseOut('Layer18');" cellSpacing=0 cellPadding=0 border=0><TBODY><TR><TD></TD></TR></TBODY></TABLE></DIV>
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
                  href="gm/GmBody.htm" 
                  target="body"><IMG 
                  onmouseover="a.src='images/top_menu4d.gif';fnMouseOver('Layer11')" 
                  onclick="JavaScript:fnMouseClick('Layer1','Layer11')" 
                  onmouseout="a.src='images/top_menu4c.gif';fnMouseOut('Layer11')" 
                  height=48 alt="제품정보관리" src="images/top_menu4c.gif" width=94 
                  border=0 name=a></A></TD>

                <TD width=94><A onfocus="this.blur()" 
                  href="psm/psmBody.jsp" 
                  target="body"><IMG 
                  onmouseover="b.src='images/top_menu8d.gif';fnMouseOver('Layer12')" 
                  onclick="JavaScript:fnMouseClick('Layer2','Layer12')" 
                  onmouseout="b.src='images/top_menu8c.gif';fnMouseOut('Layer12')" 
                  height=48 alt="과제상태관리" src="images/top_menu8c.gif" width=94 
                  border=0 name=b></A></TD>

				<TD width=94><A onfocus="this.blur()" 
                  href="cm/CmBody.htm" 
                  target="body"><IMG 
                  onmouseover="c.src='images/top_menu3d.gif';fnMouseOver('Layer13')" 
                  onclick="JavaScript:fnMouseClick('Layer3','Layer13')" 
                  onmouseout="c.src='images/top_menu3c.gif';fnMouseOut('Layer13')" 
                  height=48 alt="부품마스터" src="images/top_menu3c.gif" width=94 
                  border=0 name=c></A></TD>

				 <TD width=94><A onfocus="this.blur()" 
                  href="bm/bmBody.jsp" 
                  target="body"><IMG 
                  onmouseover="d.src='images/top_menu6d.gif';fnMouseOver('Layer14')" 
                  onclick="JavaScript:fnMouseClick('Layer4','Layer14')" 
                  onmouseout="d.src='images/top_menu6c.gif';fnMouseOut('Layer14')" 
                  height=48 alt="BOM관리" src="images/top_menu6c.gif" width=94 
                  border=0 name=d></A></TD>

				<TD width=94><A onfocus="this.blur()" href="dcm/dcmBody.jsp" target="body"><IMG onmouseover="e.src='images/top_menu7d.gif';fnMouseOver('Layer15')" onclick="JavaScript:fnMouseClick('Layer5','Layer15')" onmouseout="e.src='images/top_menu7c.gif';fnMouseOut('Layer15')" height=48 alt="설계변경관리" src="images/top_menu7c.gif" width=94 border=0 name=e></A></TD>
				<TD width=95><A onfocus="this.blur()" href="dms/DmsBody.html" target="body"><IMG onmouseover="f.src='images/top_menu1d.gif';fnMouseOver('Layer16')" onclick="JavaScript:fnMouseClick('Layer6','Layer16')" 
                onmouseout="f.src='images/top_menu1c.gif';fnMouseOut('Layer16')" height=48 alt="기술문서관리" src="images/top_menu1c.gif" width=95 border=0 name=f></A></TD>

                <TD width=94><A onfocus="this.blur()" 
                  href="ca/CaBody.htm" 
                  target="body"><IMG 
                  onmouseover="g.src='images/top_menu2d.gif';fnMouseOver('Layer17')" 
                  onclick="JavaScript:fnMouseClick('Layer7','Layer17')" 
                  onmouseout="g.src='images/top_menu2c.gif';fnMouseOut('Layer17')" 
                  height=48 alt="승인원관리" src="images/top_menu2c.gif" width=94 
                  border=0 name=g></A></TD>

                <TD width=94><A onfocus="this.blur()" 
                  href="pjt/pjtBody.jsp" 
                  target="body"><IMG 
                  onmouseover="h.src='images/top_menu5d.gif';fnMouseOver('Layer18')" 
                  onclick="JavaScript:fnMouseClick('Layer8','Layer18')" 
                  onmouseout="h.src='images/top_menu5c.gif';fnMouseOut('Layer18')" 
                  height=48 alt="프로젝트관리" src="images/top_menu5c.gif" width=94 
                  border=0 name=h></A></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD bgColor=#c1c1c1 height=1></TD></TR>
  <TR>
    <TD background=images/sub_bg.gif height=25></TD></TR></TBODY></TABLE>
</BODY></HTML>
