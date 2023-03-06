<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "근태관리 메뉴"		
	contentType = "text/html; charset=euc-kr" 	
	import = "java.util.*"
%>

<%
	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyy");
	java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("MM");
	String y = sdf1.format(now);
	String m = sdf2.format(now);

%>
<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 6 //주 메뉴 갯수
     
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

	   for ( i=1 ; i <= 5 ; i++ )
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

		var c = confirm("현재 시각으로 " + text + "기록처리를 하시겠습니까?\n\n(주의)\n현재시각이 실제 " + text + "시각이 아닐경우에는 기록하지 마시고 근태관리자에게 문의하십시오.\n한번 기록된 출퇴근 시각은 변경할 수 없습니다.");

		if(c == true){
			parent.up.location.href = url;
		}else{
		}
	}
</script>
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  
  
    <TR><!--출퇴근시간입력 1-->
    <TD onmouseover="menu2.src='images/lm_es2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();selectedtext(2,1) 
    onmouseout="menu2.src='images/lm_es2.gif'"><IMG src="images/lm_es2.gif" border=0 
    name=menu2></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=21>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,1);subhideall() 
            href="javascript:w_confirm('in');"><SPAN id=m2_1>출근시각입력</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif colSpan=2 height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,9);subhideall() 
            href="javascript:w_confirm('out');"><SPAN id=m2_9>퇴근시각입력</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>
  
  <TR><!--근태/출장 신청 3-->
    <TD onmouseover="menu5.src='images/lm_es4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='images/lm_es4.gif'"><IMG src="images/lm_es4.gif" border=0 
    name=menu5 alt="근태/출장 신청"></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=21>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=HYU_GA" target="up"><SPAN id=m5_1>휴공가원작성</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif colSpan=2 height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=OE_CHUL" target="up"><SPAN id=m5_2>외출계작성</SPAN></A>
        <TR>
          <TD background=images/left_hline.gif colSpan=2 height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=CHULJANG_SINCHEONG" target="up"><SPAN id=m5_3>출장신청서작성</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>


	 <TR><!--월별개인근태현황 2-->
		<TD onmouseover="menu1.src='images/lm_es1_over.gif'" 
		style="CURSOR: hand" onclick=showhide(1);subhideall()
		onmouseout="menu1.src='images/lm_es1.gif'"><A 
		  href="../servlet/GeunTaeServlet?mode=person_month&y=<%=y%>&m=<%=m%>" target=up><IMG src="images/lm_es1.gif" border=0 
		  name=menu1></A></TD></TR>
	  <TR>
		<TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>



<%	// 권한 체크
	String prg_priv = sl.privilege;
	int idx1 = prg_priv.indexOf("ES01");
	int idx2 = prg_priv.indexOf("ES02");
	if (idx2 >= 0){
%>
  <TR><!--근태현황보기 4-->
    <TD onmouseover="menu3.src='images/lm_es3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();selectedtext(3,1) 
    onmouseout="menu3.src='images/lm_es3.gif'"><IMG src="images/lm_es3.gif" border=0 
      name=menu3></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="../servlet/GeunTaeServlet?mode=person_year" target=up><SPAN id=m3_1>부서별개인근태현황</SPAN></A> </TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
            href="../servlet/GeunTaeServlet?mode=div_month" target=up><SPAN id=m3_2>부서별근태현황</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,3);subhideall() 
            href="../servlet/GeunTaeServlet?mode=work_history" target=up><SPAN id=m3_3>부서별출퇴근현황</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// 권한 체크
	if (idx2 >= 0){
%>
  <TR><!--근태정보편집 5-->
    <TD onmouseover="menu6.src='images/lm_es5_over.gif'" style="CURSOR: hand" onclick=showhide(6);subhideall();selectedtext(6,1);
    onmouseout="menu6.src='images/lm_es5.gif'"><IMG src="images/lm_es5.gif" border=0 name=menu6 alt='근태정보편집'></TD></TR>
  <TR>
    <TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,1);subhideall() 
            href="../servlet/GeunTaeServlet" target=up><SPAN id=m6_1>근태정보편집</SPAN></A> </TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,2);subhideall() 
            href="admin/modifyInOutTime.jsp" target=up><SPAN id=m6_2>출퇴근시각변경</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>  

<%	// 권한 체크
	if (idx1 >= 0){
%>
  <TR><!--관리자 설정 6-->
    <TD onmouseover="menu4.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();selectedtext(4,1) 
    onmouseout="menu4.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 
      name=menu4 alt='기준정보관리'></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,1);subhideall() 
            href="../servlet/GeunTaeServlet?mode=manager_hyuga_day" target=up><SPAN id=m4_1>년월차계획산출</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="admin/settingPrivilege.jsp" target=up><SPAN id=m4_2>관리자설정</SPAN></A></TD></TR>
	  </TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>
