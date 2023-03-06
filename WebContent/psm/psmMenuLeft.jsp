<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
%>
<HTML><HEAD><TITLE>과제상태메뉴</TITLE>
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
    <TD onmouseover="menu1.src='images/lm_psm1_over.gif'" 
    style="CURSOR: hand" onclick="showhide(1);subhideall()" onmouseout="menu1.src='images/lm_psm1.gif'">
	<A href="../servlet/PsmBaseInfoServlet?mode=psm_prewrite&env_status=1" target="view">
	<IMG src="images/lm_psm1.gif" border=0 name=menu1 alt='과제정보등록'></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,1);subhideall()" href="../servlet/PsmBaseInfoServlet?mode=psm_prewrite&env_status=1" target="view">
		  <SPAN id=m1_1>예비과제등록</SPAN></A></TD></TR>
        
		<TR>
          <TD background="images/left_hline.gif" height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(1,2);subhideall()" href="../servlet/PsmBaseInfoServlet?mode=psm_prewrite&env_status=2" target="view">
		  <SPAN id=m1_2>정식과제등록</SPAN></A></TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
<TR>
   <TD onmouseover="menu2.src='images/lm_psm2_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall()" onmouseout="menu2.src='images/lm_psm2.gif'">
	<A href="../servlet/PsmBaseInfoServlet?mode=psm_bylist" target="view">
	<IMG src="images/lm_psm2.gif" border=0 name=menu2 alt='과제정보등록확정'></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>
<TR>
    <TD onmouseover="menu3.src='images/lm_psm3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(3);subhideall()" onmouseout="menu3.src='images/lm_psm3.gif'">
	<A href="../servlet/PsmProcessServlet?mode=view_search&psm_start_date=<%=anbdt.getYear()%>" target="view">
	<IMG src="images/lm_psm3.gif" border=0 name=menu3 alt='과제정보조회'></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,1);subhideall()" href="../servlet/PsmProcessServlet?mode=view_search&psm_start_date=<%=anbdt.getYear()%>" target="view">
		  <SPAN id=m3_1>과제상태현황보기</SPAN></A></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
		  <A onclick="selectedtext(3,2);subhideall()" href="../servlet/PsmProcessServlet?mode=view_matrix&psm_start_date=<%=anbdt.getYear()%>" target="view">
		  <SPAN id=m3_2>과제카테고리별현황보기</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>

	
	<TR>
		
		<TD onmouseover="menu4.src='images/lm_psm4_over.gif'" 
		style="CURSOR: hand" onclick="showhide(4);subhideall()" onmouseout="menu4.src='images/lm_psm4.gif'">
		<A href="../servlet/PsmBaseInfoServlet?mode=psm_list&psm_status=2" target="view">
		<IMG src="images/lm_psm4.gif" border=0 name=menu4 alt='과제상태관리'></A></TD></TR>
	  <TR>
		<TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>


  <%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("PS01");
	if (idx >= 0){
  %>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu5.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick="showhide(5);subhideall();selectedtext(5,1)" 
    onmouseout="menu5.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 
      name=menu5 alt='기준정보관리'></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(5,1);subhideall()" 
            href="mgr/psmMgrList.jsp" target="view"><SPAN id=m5_1>과제종류관리</SPAN></A> </TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(5,2);subhideall()" 
            href="mgr/categoryMgrList.jsp" target="view"><SPAN id=m5_2>과제카테고리관리</SPAN></A> </TD></TR>
		
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(5,3);subhideall()" 
            href="mgr/colorMgrList.jsp" target="view"><SPAN id=m5_3>과제상태별색상관리</SPAN></A> </TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(5,4);subhideall()" 
            href="mgr/psmViewMgrList.jsp" target="view"><SPAN id=m5_4>과제조회자설정</SPAN></A> </TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick="selectedtext(5,5);subhideall()" 
            href="mgr/settingPrivilege.jsp" target="view"><SPAN id=m5_5>관리자설정</SPAN></A> </TD></TR>

		</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


