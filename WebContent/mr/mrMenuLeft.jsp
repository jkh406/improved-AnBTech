<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>

<HTML><HEAD><TITLE>실적메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 4; //주 메뉴 갯수
     
    function showhide(num)    { 
	  for (i=1; i<=main_cnt; i++)   { 
		  menu=eval("document.all.block"+i+".style"); 

		  if (num==i ) {
			if (menu.display=="block")
			{
				menu.display="none"; 
			}
			else
			{
			  menu.display="block"; 
			}

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 
	 
	 var sub_cnt = 0 // 하위 메뉴를 가지고 있는 2단계 서브메뉴 갯수

	 function subshowhide(num)    { 
	  for (i=1; i<=sub_cnt; i++)   { 
		  menu=eval("document.all.subblock"+i+".style"); 
		  if (num==i ) {
			if (menu.display=="block")
			{
				menu.display="none"; 
			}
			else
			{
				menu.display="block"; 
			}

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	 } 

     function subhide(num)    { 
          for (i=1; i<=sub_cnt; i++)   { 
              menu=eval("document.all.subblock"+i+".style"); 
     
              if (num==i) { 
                
                   menu.display="none"; 
         
              } 
          } 
      }	
	 
	 function subhideall()    { 
          for (i=1; i<=sub_cnt; i++)   { 
              menu=eval("document.all.subblock"+i+".style"); 
              menu.display="none";               
          } 
      }	

	 function show(num)    { 
	  for (i=1; i<=main_cnt; i++)   { 
		  menu=eval("document.all.block"+i+".style"); 

		  if (num==i ) {
			
			  menu.display="block"; 			

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 


	function subshow(num)    { 
	  for (i=1; i<=sub_cnt; i++)   { 
		  menu=eval("document.all.subblock"+i+".style"); 
		  if (num==i ) {
			
				menu.display="block"; 			

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 

	 function selectedtext(one,two)    { 

	   for ( i=1 ; i <= 14 ; i++ )
	   {
    	  menu = eval("document.all.m"+one+"_"+i);
	      if ( menu != null )
		  {
		    menu.style.color="#666666";			
		  }
	   }

	   menu = eval("document.all.m"+one+"_"+two);

	   menu.style.color="#000000";
	   
	} 


	function subselectedtext(one,two,three)    { 

	   for ( i=1 ; i <= 4 ; i++ )
	   {
    	  menu = eval("document.all.sm"+one+"_"+two+"_"+i);
	      if ( menu != null )
		  {
		    menu.style.color="#B1A28D";
		  }
	   }

	   menu = eval("document.all.sm"+one+"_"+two+"_"+three);

	   menu.style.color="#654C33";
	   
	} 

-->
</SCRIPT>

<SCRIPT language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh);
}
-->
</SCRIPT>
</HEAD>
<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR>
    <TD onmouseover="menu1.src='images/lm_pjt1_over.gif'" 
    style="CURSOR: hand" onclick="showhide(1);subhideall()" onmouseout="menu1.src='images/lm_pjt1.gif'">
	<A href="../servlet/asresultworkServlet?mode=ART_L" target="view">
	<IMG src="images/lm_pjt1.gif" border=0 name=menu1>유지보수업체</A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,1);subhideall()" href="../servlet/asresultworkServlet?mode=ART_WV" target="view">
		  <SPAN id=m1_1>A/S실적작성</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,2);subhideall()" href="../servlet/asresultworkServlet?mode=ART_L" target="view">
		  <SPAN id=m1_2>A/S실적조회</SPAN></A></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,3);subhideall()" href="../servlet/asvalueworkServlet?mode=AVE_L" target="view">
		  <SPAN id=m1_3>A/S평가조회</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu2.src='images/lm_pjt2_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall()" onmouseout="menu2.src='images/lm_pjt2.gif'">
	<A href="../servlet/asresultdivServlet?mode=ART_L" target="view">
	<IMG src="images/lm_pjt2.gif" border=0 name=menu2>사업부</A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,1);subhideall()" href="../servlet/asresultdivServlet?mode=ART_L" target="view">
		  <SPAN id=m2_1>A/S실적조회</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,2);subhideall()" href="../servlet/asvaluedivServlet?mode=AVE_L" target="view">
		  <SPAN id=m2_2>A/S평가</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu3.src='images/lm_pjt3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(3);subhideall()" onmouseout="menu3.src='images/lm_pjt3.gif'">
	<A href="../servlet/asresultitServlet?mode=ART_SL" target="view">
	<IMG src="images/lm_pjt3.gif" border=0 name=menu3>IT부서</A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,1);subhideall()" href="../servlet/asresultitServlet?mode=ART_SL" target="view">
		  <SPAN id=m3_1>A/S현황</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,2);subhideall()" href="../servlet/asvalueitServlet?mode=AVE_SL" target="view">
		  <SPAN id=m3_2>평가현황</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>

<%	// 관리자 권한 체크
	String prg_priv = "PJ01";
	int idx = prg_priv.indexOf("PJ01");
	if (idx >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu4.src='images/lm_admin_over.gif'" 
    style="CURSOR: hand" onclick="showhide(4);subhideall();selectedtext(4,1)" 
    onmouseout="menu4.src='images/lm_admin.gif'"><IMG src="images/lm_admin.gif" border=0 
      name=menu4></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(4,1);subhideall()" 
            href="mgr/processMgr.jsp" target="view"><SPAN id=m4_1>평가항목관리</SPAN></A> </TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(4,2);subhideall()" 
            href="mgr/projectMgr.jsp" target="view"><SPAN id=m4_2>환경설정관리</SPAN></A> </TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


