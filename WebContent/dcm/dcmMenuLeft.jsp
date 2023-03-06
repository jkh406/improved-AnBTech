<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>

<HTML><HEAD><TITLE>설계변경메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 5; //주 메뉴 갯수
     
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
		  menu=eval("document.all.subblock"+i+".style"); alert(menu.display+":"+num+":"+i);
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
		  } alert(menu.display);
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
    <TD onmouseover="menu1.src='images/lm_dcm_over.gif'" 
    style="CURSOR: hand" onclick="showhide(1);subhideall()" onmouseout="menu1.src='images/lm_dcm.gif'">
	<A href="../servlet/CbomBaseInfoServlet?mode=ecr_prewrite" target="view">
	<IMG src="images/lm_dcm.gif" border=0 name=menu1 alt='ECR/ECO작성관리'></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,1);subhideall()" href="../servlet/CbomBaseInfoServlet?mode=ecr_prewrite" target="view">
		  <SPAN id=m1_1>ECR 작성</SPAN></A></TD></TR>
        <TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,2);subhideall()" href="../servlet/CbomBaseInfoServlet?mode=eco_prewrite" target="view">
		  <SPAN id=m1_2>ECO 작성</SPAN></A></TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
  
    <TD onmouseover="menu2.src='images/lm_dcm2_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall()" onmouseout="menu2.src='images/lm_dcm2.gif'">
	<A href="../servlet/CbomProcessServlet?mode=ecc_iwlist" target="view">
	<IMG src="images/lm_dcm2.gif" border=0 name=menu2 alt='ECR/ECO Sheet관리(개인)'></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,1);subhideall()" href="../servlet/CbomProcessServlet?mode=ecc_iwlist" target="view">
		  <SPAN id=m2_1>ECR/ECO 작성함</SPAN></A></TD></TR>
		 <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,2);subhideall()" href="../servlet/CbomProcessServlet?mode=ecc_islist" target="view">
		  <SPAN id=m2_2>ECR/ECO 발신함</SPAN></A></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(2,3);subhideall()" href="../servlet/CbomProcessServlet?mode=ecc_irlist" target="view">
		  <SPAN id=m2_3>ECR/ECO 수신함</SPAN></A></TD></TR>		
        </TBODY></TABLE></SPAN></TD></TR>
  
    <TD onmouseover="menu3.src='images/lm_dcm3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(3);subhideall()" onmouseout="menu3.src='images/lm_dcm3.gif'">
	<A href="../servlet/CbomProcessServlet?mode=ecc_drlist" target="view">
	<IMG src="images/lm_dcm3.gif" border=0 name=menu3 alt='ECR/ECO Sheet관리(부서)'></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,1);subhideall()" href="../servlet/CbomProcessServlet?mode=ecc_dslist" target="view">
		  <SPAN id=m3_1>ECR/ECO 발신함</SPAN></A></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,2);subhideall()" href="../servlet/CbomProcessServlet?mode=ecc_drlist" target="view">
		  <SPAN id=m3_2>ECR/ECO 수신함</SPAN></A></TD></TR>
		
        </TBODY></TABLE></SPAN></TD></TR>
 
    <TD onmouseover="menu4.src='images/lm_dcm4_over.gif'" 
    style="CURSOR: hand" onclick="showhide(4);subhideall()" onmouseout="menu4.src='images/lm_dcm4.gif'">
	<A href="../servlet/CbomProcessServlet?mode=audit_list" target="view">
	<IMG src="images/lm_dcm4.gif" border=0 name=menu4 alt='설계변경적용관리'></A></TD></TR>
	<TR>
		<TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
		  <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
		  border=0>
			<TBODY>
			<TR bgColor=#f3f3f3 height=22>
			  <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
			  <A onclick="selectedtext(4,1);subhideall()" href="../servlet/CbomProcessServlet?mode=audit_list" target="view">
			  <SPAN id=m4_1>ECO 적용대상</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
			  <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
			  <A onclick="selectedtext(4,2);subhideall()" href="../servlet/CbomHistoryServlet?mode=sch_base" target="view">
			  <SPAN id=m4_2>ECO 적용현황</SPAN></A></TD></TR>
		
        </TBODY></TABLE></SPAN></TD></TR>

<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("BM01");
	if (idx >= 0){
%>
     <TD onmouseover="menu5.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick="showhide(5);subhideall()" onmouseout="menu5.src='images/lm_mgr.gif'">
	<A href="../servlet/CbomHistoryServlet?mode=sch_base" target="view">
	<IMG src="images/lm_mgr.gif" border=0 name=menu5 alt='기준정보관리'></A></TD></TR>
	<TR>
		<TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
		  <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
		  border=0>
			<TBODY>
			<TR bgColor=#f3f3f3 height=22>
			  <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
			  <A onclick="selectedtext(5,1);subhideall()" href="../bm/admin/BmItem_list.jsp" target="view">
			  <SPAN id=m5_1>설계변경항목관리</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
			  <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
			  <A onclick="selectedtext(5,2);subhideall()" href="../bm/admin/BmProcessMgr.jsp" target="view">
			  <SPAN id=m5_2>관리자설정</SPAN></A></TD></TR>
		
        </TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


