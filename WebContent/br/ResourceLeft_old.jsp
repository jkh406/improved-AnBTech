<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "자원예약"		
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

<script language='javascript'>
	function w_confirm(type){
		var text;
		var url;
		if(type == "in"){
			text = "출근";
			url = "../servlet/GeunTaeServlet?mode=chk_in";
		}
		else{
			text = "퇴근";
			url = "../servlet/GeunTaeServlet?mode=chk_out"
		}

		var c = confirm(text+" 시각을 기록하시겠습니다.");

		if(c == true){
			parent.up.location.href = url;
		}else{
		}
	}
</script>
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR><!--공용차량예약현황 -->
    <TD onmouseover="menu1.src='images/lm_br1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='images/lm_br1.gif'"><A 
      href="../servlet/BookResourceServlet?category=car&mode=view_stat" target=up><IMG src="images/lm_br1.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

 <!-- <TR>회의실예약현황 
    <TD onmouseover="menu2.src='images/lm_br2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();
    onmouseout="menu2.src='images/lm_br2.gif'"><A 
      href="준비중" target=up><IMG src="images/lm_br2.gif" border=0 
    name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>
--><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN>

<%	// 자원예약 담당자 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("BR02");
	if (idx >= 0){
%>
  <TR><!--예약업무처리 -->
    <TD onmouseover="menu3.src='images/lm_br3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();selectedtext(3,1) 
    onmouseout="menu3.src='images/lm_br3.gif'"><A 
      href="../servlet/BookResourceServlet?category=car&mode=req_list&tablename=charyang_master" target=up><IMG src="images/lm_br3.gif" border=0 
      name=menu3></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="../servlet/BookResourceServlet?category=car&mode=req_list&tablename=charyang_master" target=up><SPAN id=m3_1>공용차량예약신청현황</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>


<%	// 예약관리 모듈 관리자 체크
	int idx2 = prg_priv.indexOf("BR01");
	if (idx2 >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu4.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();selectedtext(4,1) 
    onmouseout="menu4.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 
      name=menu4></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,1);subhideall() 
            href="../servlet/BookResourceServlet?category=car&mode=list" target=up><SPAN id=m4_1>공용차량등록관리</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="../br/admin/settingPrivilege.jsp" target=up><SPAN id=m4_2>관리자설정</SPAN></A></TD></TR>			
			</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>
	<!-- 업무공유 -->
	<TR><!--관리자 설정 -->
    <TD onmouseover="menu5.src='images/lm_share_over.gif'" alt="업무공유"
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='images/lm_share.gif'" alt='업무공유'><IMG src="images/lm_share.gif"  alt="업무공유" border=0 
      name=menu5></TD></TR>
	<TR><TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="../servlet/ShareBdServlet?tablename=com_rule" 
			target=up><SPAN id=m5_1>사규나들이</SPAN></A> </TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="../servlet/ShareBdServlet?tablename=form_bank" target=up><SPAN id=m5_2>양식꾸러미</SPAN></A></TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="../servlet/ShareBdServlet?tablename=manual_bank" 
			target=up><SPAN id=m5_3>메뉴얼꾸러미</SPAN></A> </TD></TR>
       </TBODY></TABLE></SPAN></TD></TR>

	<!---->

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

