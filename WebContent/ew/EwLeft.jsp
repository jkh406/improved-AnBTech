<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "Ư�ٰ����޴�"		
	contentType = "text/html; charset=KSC5601" 		
%>

<HTML><HEAD><TITLE>�޴�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//���� �޴� ���� ��ũ��Ʈ
    var main_cnt = 5 //�� �޴� ����
     
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
	 
	 var sub_cnt = 0 // ���� �޴��� ������ �ִ� 2�ܰ� ����޴� ����

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

<!-- ���� �޴� ����-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

 <TR><!--Ư�ٽ�û -->
    <TD onmouseover="menu2.src='images/lm_ew2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();
    onmouseout="menu2.src='images/lm_ew2.gif'"><A href="../servlet/ExtraWorkServlet?mode=req_extrawork" target="up"><IMG src="images/lm_ew2.gif" border=0 
      name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--Ư����Ȳ���� -->
    <TD onmouseover="menu1.src='images/lm_ew1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall();selectedtext(1,2);
    onmouseout="menu1.src='images/lm_ew1.gif'"><A href="../servlet/ExtraWorkServlet?mode=person_month" target="up"><IMG src="images/lm_ew1.gif" border=0 name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22><TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,1);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=eachEwList" target=up><SPAN id=m1_1>����Ư�ٽ�û������Ȳ</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22><TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,2);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=person_month" target=up><SPAN id=m1_2>����Ư��ó����Ȳ</SPAN></A> </TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22><TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,3);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=ew_daily_list" target=up><SPAN id=m1_3>�μ���Ư�ٴ������Ȳ</SPAN></A> </TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>

 

<%	// Ư�ٽ�û����� ���� üũ
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("EW02");
	if (idx >= 0){
%>
  <TR><!--Ư�ٽ�û���� -->
    <TD onmouseover="menu3.src='images/lm_ew3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();
    onmouseout="menu3.src='images/lm_ew3.gif'"><A href="../servlet/ExtraWorkServlet?mode=ewReqList" target="up"><IMG src="images/lm_ew3.gif" border=0 
      name=menu3></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// Ư���������� ���� üũ
	int idx2 = prg_priv.indexOf("EW03");
	if (idx2 >= 0){
%>
  <TR><!--Ư��������� -->
    <TD onmouseover="menu4.src='images/lm_ew4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();
    onmouseout="menu4.src='images/lm_ew4.gif'"><A href="../servlet/ExtraWorkServlet?mode=ew_process_list" target="up"><IMG src="images/lm_ew4.gif" border=0 name=menu4></A></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
          <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,3);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=ew_process_list" target=up><SPAN id=m4_3>����ó�����</SPAN></A></TD></TR>
		  <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
          <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=ew_result_list" target=up><SPAN id=m4_2>����ó����Ȳ</SPAN></A> </TD></TR>
          <TR>
         </TBODY>
		</TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// Ư�ٰ����� ���� üũ
	int idx3 = prg_priv.indexOf("EW01");
	if (idx3 >= 0){
%>
  <TR><!--������ ���� -->
    <TD onmouseover="menu5.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 
      name=menu5></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=standard_wtime_fix&div=wtime_view" target=up><SPAN id=m5_1>���رٹ��ð�����</SPAN></A> </TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="../servlet/ExtraWorkServlet?mode=manager_hourly_pay" target=up><SPAN id=m5_2>���޺��ñ޼���</SPAN></A> </TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="admin/settingPrivilege.jsp" target=up><SPAN id=m5_3>�����ڼ���</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>