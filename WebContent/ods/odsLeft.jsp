<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>

<HTML><HEAD><TITLE>�޴�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//���� �޴� ���� ��ũ��Ʈ
    var main_cnt = 3 //�� �޴� ����
     
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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" onLoad="showhide(1);">

<!-- ���� �޴� ����-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR><!--�����ۼ� -->
    <TD onmouseover="menu1.src='images/lm_ods2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='images/lm_ods2.gif'"><A 
      href="../servlet/OfficialDocumentServlet?mode=OFD_L" target="view"><IMG src="images/lm_ods2.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,1);subhideall() 
            href="../servlet/OfficialDocumentServlet?mode=OFD_L" target="view"><SPAN id=m1_1>��������</SPAN></A></TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,2);subhideall() 
            href="../servlet/InDocumentServlet?mode=IND_L" target="view"><SPAN id=m1_2>�系����</SPAN></A></TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(1,3);subhideall() 
            href="../servlet/OutDocumentServlet?mode=OTD_L" target="view"><SPAN id=m1_3>��ܰ���</SPAN></A>
		</TD></TR></TBODY></TABLE></SPAN></TD></TR>
  <TR><!--�������� -->
    <TD onmouseover="menu2.src='images/lm_ods3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();
    onmouseout="menu2.src='images/lm_ods3.gif'"><A 
      href="../servlet/InDocumentRecServlet?mode=IDR_L" target="view"><IMG src="images/lm_ods3.gif" border=0 
      name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,1);subhideall() 
            href="../servlet/InDocumentRecServlet?mode=IDR_L" target="view"><SPAN id=m2_1>�系����</SPAN></A></TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,2);subhideall() 
            href="../servlet/OutDocumentRecServlet?mode=ODR_L" target="view"><SPAN id=m2_2>��ܰ���</SPAN></A>
		</TD></TR></TBODY></TABLE></SPAN></TD></TR>

  <TR><!--���������� -->
    <TD onmouseover="menu3.src='images/lm_ods4_over.gif'" 
    style="CURSOR: hand" onclick='showhide(3);subhideall();selectedtext(3,1)' 
    onmouseout="menu3.src='images/lm_ods4.gif'"><IMG src="images/lm_ods4.gif" border=0 
      name=menu3></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,1);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_SER" target="view"><SPAN id=m3_1>�������̷�</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,2);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_OUT" target="view"><SPAN id=m3_2>�����</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,3);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_BTR" target="view"><SPAN id=m3_3>�����û</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,4);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_HDY" target="view"><SPAN id=m3_4>��(��)����</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,5);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_CAR" target="view"><SPAN id=m3_5>������û</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,6);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_REP" target="view"><SPAN id=m3_6>����</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,7);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_BRP" target="view"><SPAN id=m3_7>���庸��</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,8);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_DRF" target="view"><SPAN id=m3_8>��ȼ�</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,9);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_CRD" target="view"><SPAN id=m3_9>���Խ�û</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,10);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_RSN" target="view"><SPAN id=m3_10>������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif'height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,11);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_HLP" target="view"><SPAN id=m3_11>������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,12);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_OFF" target="view"><SPAN id=m3_12>�����Ƿ�</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,13);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_EDU" target="view"><SPAN id=m3_13>��������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,14);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_AKG" target="view"><SPAN id=m3_14>���ο�</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,15);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_TD" target="view"><SPAN id=m3_15>�������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,16);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_ODT" target="view"><SPAN id=m3_16>��������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,17);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_IDS" target="view"><SPAN id=m3_17>�系����</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,18);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_ODS" target="view"><SPAN id=m3_18>��ܰ���</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,19);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_AST" target="view"><SPAN id=m3_19>�ڻ��̰�/����</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,20);subhideall()' 
            href="../servlet/ApprovalMenuServlet?mode=APP_EST" target="view"><SPAN id=m3_20>������</SPAN></A></TD></TR>
        <TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,21);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_EWK" target="view"><SPAN id=m3_21>Ư�ٽ�û</SPAN></A></TD></TR>
		<TR><TD background='images/left_hline.gif' height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,22);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_BOM" target="view"><SPAN id=m3_22>BOM����</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,23);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_DCM" target="view"><SPAN id=m3_23>���躯��</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,24);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_PCR" target="view"><SPAN id=m3_24>���ſ�û</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,25);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_ODR" target="view"><SPAN id=m3_25>���ֿ�û</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,26);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_PWH" target="view"><SPAN id=m3_26>�����԰�</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=20>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick='selectedtext(3,27);subhideall()'
            href="../servlet/ApprovalMenuServlet?mode=APP_TGW" target="view"><SPAN id=m3_27>��ǰ���</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>


  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


