<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= ""		
	contentType = "text/html; charset=KSC5601" 		
	import="java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="tree" class="com.anbtech.cm.business.makeCodeTreeItems"/>

<%
	String prg_priv = sl.privilege;
	int idx1 = prg_priv.indexOf("ST01");
	int idx2 = prg_priv.indexOf("ST02");
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

	<TR><!-- 재고관리 -->
		<TD onmouseover="menu1.src='images/lm_st_over.gif'" style="CURSOR: hand" onclick="showhide(1);subhideall();" onmouseout="menu1.src='images/lm_st.gif'"><A href="../servlet/StockMgrServlet?mode=list_stock_master" target="up"><IMG src="images/lm_st.gif" border=0 name=menu1 alt="재고관리"></A></TD></TR>
	<TR>
		<TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
			<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,1);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_stock_master" target=up><SPAN id=m1_1>재고현황</SPAN></A></TD></TR>
				<TR><TD background=images/left_hline.gif height=1></TD></TR>
		
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,2);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_inout" target=up><SPAN id=m1_2>수불현황</SPAN></A></TD></TR>
				<TR><TD background=images/left_hline.gif height=1></TD></TR>

<%	if (idx2 >= 0){	%>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,3);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_stock_rop" target=up><SPAN id=m1_3>ROP대상품목</SPAN></A></TD></TR>
				<TR><TD background=images/left_hline.gif height=1></TD></TR>
		
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,4);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_stock_resonable" target=up><SPAN id=m1_4>ROP적정재고관리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
<%	}	%>
        </TBODY></TABLE>
	</SPAN></TD></TR>

	<TR><!-- 수불관리(입고) -->
    <TD onmouseover="menu2.src='images/lm_st2_over.gif'" style="CURSOR: hand" onclick="showhide(2);subhideall();" onmouseout="menu2.src='images/lm_st2.gif'"><A href="../servlet/StockMgrServlet?mode=list_etc_inout&in_or_out=IN" target="up"><IMG src="images/lm_st2.gif" border=0 name=menu2 alt="수불관리(입고)"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
<%	if (idx2 >= 0){	%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,1);subhideall() 
            href="../servlet/StockMgrServlet?mode=write_etc_inout_info&in_or_out=IN" target=up><SPAN id=m2_1>예외입고처리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
<%	}	%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,2);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_etc_inout&in_or_out=IN" target=up><SPAN id=m2_2>예외입고현황</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        </TBODY></TABLE>
	</SPAN></TD></TR>

	<TR><!--수불관리(출고) -->
    <TD onmouseover="menu3.src='images/lm_st3_over.gif'" style="CURSOR: hand" onclick="showhide(3);subhideall();" onmouseout="menu3.src='images/lm_st3.gif'"><A href="../servlet/StockMgrServlet?mode=list_deliveried_info" target="up"><IMG src="images/lm_st3.gif" border=0 name=menu3 alt="수불관리(출고)"></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
<!--   긴급생산출고는 생산관리모듈에서 이루어지므로 재고관리에서는 의뢰를 허용하지 않는다.   
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="../servlet/StockMgrServlet?mode=write_etc_delivery" target=up><SPAN id=m3_1>긴급생산출고의뢰</SPAN></A></TD></TR>

-->
        <SPAN id=m3_1></SPAN>

<%	if (idx2 >= 0){	%>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,6);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_reserved_item" target=up><SPAN id=m3_6>생산출고의뢰현황</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,3);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_toenter_item" target=up><SPAN id=m3_3>생산출고처리대상</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
<%	}	%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_deliveried_info" target=up><SPAN id=m3_2>생산출고현황</SPAN></A></TD></TR>

<%	if (idx2 >= 0){	%>
		<TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,4);subhideall() 
            href="../servlet/StockMgrServlet?mode=write_etc_inout_info&in_or_out=OUT" target=up><SPAN id=m3_4>예외출고처리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
<%	}	%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,5);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_etc_inout&in_or_out=OUT" target=up><SPAN id=m3_5>예외출고현황</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        </TBODY></TABLE>
	</SPAN></TD></TR>

	 <TR><!-- 수불관리(이동) -->
    <TD onmouseover="menu4.src='images/lm_st4_over.gif'" style="CURSOR: hand" onclick="showhide(4);subhideall();" onmouseout="menu4.src='images/lm_st4.gif'"><A href="../servlet/StockMgrServlet?mode=list_item_shift_info" target="up"><IMG src="images/lm_st4.gif" border=0 name=menu4 alt="수불관리(이동)"></A></TD></TR>
   <TR>
    <TD><SPAN id="block4" style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
<%	if (idx2 >= 0){	%>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,1);subhideall() 
            href="../servlet/StockMgrServlet?mode=write_item_ishift_info" target=up><SPAN id=m4_1>품목간재고이동</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="../servlet/StockMgrServlet?mode=write_item_shift_info" target=up><SPAN id=m4_2>공장간재고이동</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
<%	}	%>        
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,3);subhideall() 
            href="../servlet/StockMgrServlet?mode=list_item_shift_info" target=up><SPAN id=m4_3>재고이동현황</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        </TBODY></TABLE>
	</SPAN></TD></TR>

	 <TR><!--실사관리 -->
    <TD onmouseover="menu5.src='images/lm_st5_over.gif'" style="CURSOR: hand" onclick="showhide(5);subhideall();" onmouseout="menu5.src='images/lm_st5.gif'"><A href="../servlet/StockMgrServlet?mode=list_stock_real" target="up"><IMG src="images/lm_st5.gif" border=0 name=menu5 alt="실사관리"></A></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

<%
	if (idx1 >= 0){
%>
   <TR><!--기준정보관리 -->
    <TD onmouseover="menu6.src='images/lm_mgr_over.gif'" style="CURSOR: hand" onclick="showhide(6);subhideall();selectedtext(6,1);" onmouseout="menu6.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border="0" name="menu6" alt="기준정보관리"></TD></TR>
  <TR>
    <TD><SPAN id="block6" style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,1);subhideall() 
            href="../servlet/StockConfigMgrServlet?mode=list_conf_type" target=up><SPAN id=m6_1>수불유형정보관리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,2);subhideall() 
            href="../servlet/StockConfigMgrServlet?mode=list_factory" target=up><SPAN id=m6_2>공장정보관리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,3);subhideall() 
            href="../st/admin/settingPrivilege.jsp" target=up><SPAN id=m6_3>관리자설정</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>       
<!--
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,4);subhideall() 
            href="../servlet/StockConfigMgrServlet?mode=list_warehouse" target=up><SPAN id=m6_4>창고정보관리</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>-->
        </TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>
  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>