<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="tree" class="com.anbtech.cm.business.makeCodeTreeItems"/>

<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 3 //주 메뉴 갯수
     
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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" onLoad="showhide(1);">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

  <TR><!--검사대상조회 -->
    <TD onmouseover="menu1.src='images/lm_qc_over.gif'" style="CURSOR: hand" onclick="showhide(1);subhideall();" onmouseout="menu1.src='images/lm_qc.gif'"><A href="../servlet/QualityCtrlServlet?mode=list_inspect" target="up"><IMG src="images/lm_qc.gif" border=0 name=menu1 alt="품질검사대상"></A></TD></TR>
  <TR>
    <TD><SPAN id="block1" style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,1);subhideall() 
            href="../servlet/QualityCtrlServlet?mode=list_inspect" target=up><SPAN id=m1_1>검사대상
			</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,2);subhideall() 
            href="../servlet/QualityCtrlServlet?mode=list_return" target=up><SPAN id=m1_2>재검사대상</SPAN></A></TD></TR>
       <!--
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,3);subhideall() 
            href="../servlet/QualityCtrlServlet?mode=list_rework" target=up><SPAN id=m1_3>재작업대상</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>-->
        </TBODY></TABLE>
	
	</SPAN></TD></TR>

  <TR><!--검사결과조회 -->
    <TD onmouseover="menu2.src='images/lm_qc2_over.gif'" style="CURSOR: hand" onclick="showhide(2);subhideall();" onmouseout="menu2.src='images/lm_qc2.gif'"><A href="../servlet/QualityCtrlServlet?mode=list_result" target="up"><IMG src="images/lm_qc2.gif" border=0 name=menu2 alt="품질검사결과"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
			<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,1);subhideall() 
            href="../servlet/QualityCtrlServlet?mode=list_result" target=up><SPAN id=m2_1>검사결과현황
			</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,2);subhideall() 
            href="../servlet/QualityCtrlServlet?mode=failure_info" target=up><SPAN id=m2_2>불량현황</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        </TBODY></TABLE>
	
	</SPAN></TD></TR>

<%	// 품질관리모듈 관리자권한 체크
	String prg_priv = sl.privilege;
	int idx2 = prg_priv.indexOf("QC01");
	if (idx2 >= 0){
%>
  <TR><!--검사항목관리 -->
    <TD onmouseover="menu3.src='images/lm_mgr_over.gif'" style="CURSOR: hand" onclick="showhide(3);subhideall();selectedtext(3,1);" onmouseout="menu3.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border="0" name="menu3" alt="기준정보관리"></TD></TR>
  <TR>
    <TD><SPAN id="block3" style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="admin/list_inspection_item.jsp" target=up><SPAN id=m3_1>품질검사항목관리
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
            href="admin/list_inspection_item_by_item.jsp" target=up><SPAN id=m3_2>품목별검사항목관리</SPAN></A></TD></TR>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,3);subhideall() 
            href="../qc/admin/settingPrivilege.jsp" target=up><SPAN id=m3_3>관리자설정</SPAN></A></TD></TR>

        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>