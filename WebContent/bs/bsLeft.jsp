<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "영업관리"		
	contentType = "text/html; charset=KSC5601" 		
%>

<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 5 //주 메뉴 갯수
     
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

</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR>
    <TD onmouseover="menu1.src='images/lm_bs1_over.gif'" style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='images/lm_bs1.gif'"><A href="" target=up><IMG src="images/lm_bs1.gif" border=0 name="menu1" alt="수주관리"></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR>
    <TD onmouseover="menu2.src='images/lm_bs2_over.gif'" style="CURSOR: hand" onclick=showhide(2);subhideall();
    onmouseout="menu2.src='images/lm_bs2.gif'"><A href="" target=up><IMG src="images/lm_bs2.gif" border=0 
    name="menu2" alt="출하관리"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR>
    <TD onmouseover="menu3.src='images/lm_bs3_over.gif'" style="CURSOR: hand" onclick=showhide(3);subhideall(); onmouseout="menu3.src='images/lm_bs3.gif'"><A href="" target=up><IMG src="images/lm_bs3.gif" border=0 name="menu3" alt="AS관리"></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR>
    <TD onmouseover="menu4.src='images/lm_bs4_over.gif'" style="CURSOR: hand" onclick=showhide(4);subhideall();
    onmouseout="menu4.src='images/lm_bs4.gif'"><IMG src="images/lm_bs4.gif" border=0 name="menu4" alt="거래처관리"></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR>
    <TD onmouseover="menu5.src='images/lm_mgr_over.gif'"  style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" alt="기준정보관리" border=0 
      name=menu5></TD></TR>
	<TR><TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="../servlet/SalesConfigMgrServlet?mode=list_booking_type" 
			target=up><SPAN id=m5_1>수주형태관리</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="../servlet/SalesConfigMgrServlet?mode=list_item_unit_cost" target=up><SPAN id=m5_2>품목단가관리</SPAN></A></TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="../servlet/SalesConfigMgrServlet?mode=list_item_unit_cost_by_customer" 
			target=up><SPAN id=m5_3>고객별품목단가관리</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,4);subhideall() 
            href="../servlet/SalesConfigMgrServlet?mode=list_item_premium" 
			target=up><SPAN id=m5_4>품목별할증정보관리</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,5);subhideall() 
            href="../servlet/SalesConfigMgrServlet?mode=list_item_premium_by_customer" 
			target=up><SPAN id=m5_5>고객별품목할증정보관리</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,6);subhideall() 
            href="admin/settingPrivilege.jsp" 
			target=up><SPAN id=m5_6>관리자설정</SPAN></A> </TD></TR>
       </TBODY></TABLE></SPAN></TD></TR>

	<!---->

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

