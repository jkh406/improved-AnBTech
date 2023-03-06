<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>

<HTML><HEAD><TITLE>과제메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 7; //주 메뉴 갯수
     
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
	<A href="../servlet/prsCodeServlet?mode=PHA_LA&mgr_mode=PRS_MGR" target="view">
	<IMG src="images/lm_pjt1.gif" border=0 name=menu1>전사프로세스관리</A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,1);subhideall()" href="../servlet/prsCodeServlet?mode=PHA_LA" target="view">
		  <SPAN id=m1_1>PHASE등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,2);subhideall()" href="../servlet/prsCodeServlet?mode=STP_LA" target="view">
		  <SPAN id=m1_2>STEP등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,3);subhideall()" href="../servlet/prsCodeServlet?mode=ACT_LA" target="view">
		  <SPAN id=m1_3>ACTIVITY등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,4);subhideall()" href="../servlet/prsCodeServlet?mode=DOC_LA" target="view">
		  <SPAN id=m1_4>산출물등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,5);subhideall()" href="../servlet/prsStandardServlet?mode=PSN_LA" target="view">
		  <SPAN id=m1_5>프로세스명등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,6);subhideall()" href="process/processFrame.html?prs_code=P001" target="view">
		  <SPAN id=m1_6>프로세스구성등록</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu2.src='images/lm_pjt2_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall()" onmouseout="menu2.src='images/lm_pjt2.gif'">
	<A href="../servlet/prsCodeServlet?mode=PHA_LD&mgr_mode=PJT_PML" target="view">
	<IMG src="images/lm_pjt2.gif" border=0 name=menu2>부서프로세스관리</A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,1);subhideall()" href="../servlet/prsCodeServlet?mode=PHA_LD" target="view">
		  <SPAN id=m2_1>PHASE등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,2);subhideall()" href="../servlet/prsCodeServlet?mode=STP_LD" target="view">
		  <SPAN id=m2_2>STEP등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,3);subhideall()" href="../servlet/prsCodeServlet?mode=ACT_LD" target="view">
		  <SPAN id=m2_3>ACTIVITY등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,4);subhideall()" href="../servlet/prsCodeServlet?mode=DOC_LD" target="view">
		  <SPAN id=m2_4>산출물등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,5);subhideall()" href="../servlet/prsStandardServlet?mode=PSN_LD" target="view">
		  <SPAN id=m2_5>프로세스명등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,6);subhideall()" href="process/processFrameDiv.html?prs_code=D001" target="view">
		  <SPAN id=m2_6>프로세스구성등록</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu3.src='images/lm_pjt3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(3);subhideall()" onmouseout="menu3.src='images/lm_pjt3.gif'">
	<A href="../servlet/pjtCodeServlet?mode=PJC_LA&mgr_mode=PJT_MGR" target="view">
	<IMG src="images/lm_pjt3.gif" border=0 name=menu3>전사과제등록관리</A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,1);subhideall()" href="../servlet/pjtCodeServlet?mode=PJC_LA" target="view">
		  <SPAN id=m3_1>과제명등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,2);subhideall()" href="../servlet/projectGenServlet?mode=PBS_LA" target="view">
		  <SPAN id=m3_2>기본정보등록</SPAN></A></TD></TR>
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,3);subhideall()" href="../servlet/projectStatusServlet?mode=PJS_LA" target="view">
		  <SPAN id=m3_3>과제상태관리</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu4.src='images/lm_pjt4_over.gif'" 
    style="CURSOR: hand" onclick="showhide(4);subhideall()" onmouseout="menu4.src='images/lm_pjt4.gif'">
	<A href="../servlet/pjtCodeServlet?mode=PJC_LD&mgr_mode=PJT_MGRD" target="view">
	<IMG src="images/lm_pjt4.gif" border=0 name=menu4>부서과제등록관리</A></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(4,1);subhideall()" href="../servlet/pjtCodeServlet?mode=PJC_LD" target="view">
		  <SPAN id=m4_1>과제명등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(4,2);subhideall()" href="../servlet/projectGenServlet?mode=PBS_LD" target="view">
		  <SPAN id=m4_2>기본정보등록</SPAN></A></TD></TR>
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(4,3);subhideall()" href="../servlet/projectStatusServlet?mode=PJS_LD" target="view">
		  <SPAN id=m4_3>과제상태관리</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu5.src='images/lm_pjt5_over.gif'" 
    style="CURSOR: hand" onclick="showhide(5);subhideall()" onmouseout="menu5.src='images/lm_pjt5.gif'">
	<A href="../servlet/projectSchServlet?mode=PSC_L&mgr_mode=PJT_PML" target="view">
	<IMG src="images/lm_pjt5.gif" border=0 name=menu5>과제관리(PM)</A></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,1);subhideall()" href="pm/projectPrsFrame.html?prs_code=" target="view">
		  <SPAN id=m5_1>프로세스편집</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,2);subhideall()" href="../servlet/projectManServlet?mode=PMA_L" target="view">
		  <SPAN id=m5_2>인력등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,3);subhideall()" href="../servlet/projectSchServlet?mode=PSC_L" target="view">
		  <SPAN id=m5_3>일정등록</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,4);subhideall()" href="../servlet/projectNodeAppServlet?mode=PSN_L" target="view">
		  <SPAN id=m5_4>진행관리</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,5);subhideall()" href="../servlet/projectNoteServlet?mode=PNT_L" target="view">
		  <SPAN id=m5_5>문제점관리</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,6);subhideall()" href="../servlet/projectIssueServlet?mode=PIS_L" target="view">
		  <SPAN id=m5_6>ISSUE관리</SPAN></A></TD></TR>
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,7);subhideall()" href="../servlet/projectPmCostServlet?mode=PCO_SMLT" target="view">
		  <SPAN id=m5_7>비용관리</SPAN></A></TD></TR>
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(5,8);subhideall()" href="../servlet/projectPmDocumentServlet?mode=PDT_L" target="view">
		  <SPAN id=m5_8>산출물관리</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
    <TD onmouseover="menu6.src='images/lm_pjt6_over.gif'" 
    style="CURSOR: hand" onclick="showhide(6);subhideall()" onmouseout="menu6.src='images/lm_pjt6.gif'">
	<A href="../servlet/projectStaffServlet?mode=PSM_PL" target="view">
	<IMG src="images/lm_pjt6.gif" border=0 name=menu6>과제관리(STAFF)</A></TD></TR>
  <TR>
    <TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(6,1);subhideall()" href="../servlet/projectStaffServlet?mode=PSM_PL" target="view">
		  <SPAN id=m6_1>진행관리</SPAN></A></TD></TR>
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(6,2);subhideall()" href="../servlet/projectStfCostServlet?mode=PCO_SML" target="view">
		  <SPAN id=m6_2>비용관리</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(6,3);subhideall()" href="../servlet/AnBDMS?category=10101" target="view">
		  <SPAN id=m6_3>기술문서[산출물]</SPAN></A></TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
        
  
<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("PJ01");
	if (idx >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu7.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick="showhide(7);subhideall();selectedtext(7,1)" 
    onmouseout="menu7.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 
      name=menu7></TD></TR>
  <TR>
    <TD><SPAN id=block7 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(7,1);subhideall()" 
            href="mgr/processMgr.jsp" target="view"><SPAN id=m7_1>프로세스/과제등록자</SPAN></A> </TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left style='padding-left:10px'><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(7,2);subhideall()" 
            href="mgr/projectMgr.jsp" target="view"><SPAN id=m7_2>과제PM</SPAN></A> </TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block7 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


