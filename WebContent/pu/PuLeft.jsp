<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*"
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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">

<!-- ���� �޴� ����-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
	<TBODY>
		<TR>
			<TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

		<TR><!--���ſ�û���� -->
			<TD onmouseover="menu1.src='images/lm_pu_over.gif'" style="CURSOR: hand" onclick="showhide(1);subhideall();selectedtext(1,2);" onmouseout="menu1.src='images/lm_pu.gif'"><A href="../servlet/PurchaseMgrServlet?mode=request_info&request_type=GEN" target="up"><IMG src="images/lm_pu.gif" border=0 name=menu1 alt="���ſ�û����"></A></TD></TR>
		<TR>
			<TD><SPAN id="block1" style="DISPLAY: none; xCURSOR: hand">
				<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR bgColor=#f3f3f3 height=22>
							<TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(1,1);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=request_info&request_type=RES" target=up><SPAN id=m1_1>�������籸�ſ�û</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(1,2);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=request_info&request_type=GEN" target=up><SPAN id=m1_2>�ڻ���񱸸ſ�û</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(1,3);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=request_search" target=up><SPAN id=m1_3>���ſ�û��Ȳ</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						</TBODY></TABLE></SPAN></TD></TR>
<%	// ���Ű������ �����ڱ��� üũ
	String prg_priv = sl.privilege;
	int idx1 = prg_priv.indexOf("PU02");
	if (idx1 >= 0){
%>
  <TR><!--���Ź��ְ��� -->
    <TD onmouseover="menu3.src='images/lm_pu2_over.gif'" style="CURSOR: hand" onclick="showhide(3);subhideall();" onmouseout="menu3.src='images/lm_pu2.gif'"><A href="../servlet/PurchaseMgrServlet?mode=requested_list" target="up"><IMG src="images/lm_pu2.gif" border=0 name=menu3 alt="���Ź��ְ���"></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR bgColor=#f3f3f3 height=22>
							<TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(3,1);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=requested_list" target=up><SPAN id=m3_1>���ִ��</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(3,2);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=order_search" target=up><SPAN id=m3_2>������Ȳ</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						</TBODY></TABLE>
	</SPAN></TD></TR>

  <TR><!--�����԰���� -->
    <TD onmouseover="menu2.src='images/lm_pu3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall();" onmouseout="menu2.src='images/lm_pu3.gif'"><A href="../servlet/PurchaseMgrServlet?mode=ordered_list" target="up"><IMG src="images/lm_pu3.gif" border=0 
    name="menu2" alt="�����԰����"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
			<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR bgColor=#f3f3f3 height=22>
							<TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(2,1);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=ordered_list" target=up><SPAN id=m2_1>�԰���</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(2,2);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=enter_search&upload_folder=receipt" target=up><SPAN id=m2_2>�԰���Ȳ</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(2,3);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=entered_list" target=up><SPAN id=m2_3>������������Ȳ</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(2,4);subhideall() 
							href="../servlet/PurchaseMgrServlet?mode=list_memo" target=up><SPAN id=m2_4>ǰ�񺰸޸𳻿뺸��</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						</TBODY></TABLE>
	</SPAN></TD></TR>

<!--����ǰ�����-->
  <TR> 
    <TD onmouseover="menu4.src='images/lm_pu4_over.gif'" style="CURSOR: hand" onclick='showhide(4);subhideall();' onmouseout="menu4.src='images/lm_pu4.gif'"><A href="../servlet/PurchaseConfigMgrServlet?mode=list_item_supply_info" target="up"><IMG src="images/lm_pu4.gif" alt="����ǰ�����" border="0" name="menu4"></A></TD></TR>
  <TR>
    <TD><SPAN id="block4" style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR bgColor=#f3f3f3 height=22>
							<TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(4,1);subhideall() 
							href="../servlet/PurchaseConfigMgrServlet?mode=list_item_supply_info" target=up><SPAN id=m4_1>ǰ�����ó����</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						<TR bgColor=#f3f3f3 height=22>
						  <TD class=type1 vAlign=center align=left><IMG 
							src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
							align=center> <A onclick=selectedtext(4,2);subhideall() 
							href="../servlet/PurchaseConfigMgrServlet?mode=list_item_cost" target=up><SPAN id=m4_2>ǰ��ܰ�����</SPAN></A></TD></TR>
						<TR><TD background=images/left_hline.gif height=1></TD></TR>
						</TBODY></TABLE>
	</SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>


<%	// ���Ű������ �����ڱ��� üũ
	int idx2 = prg_priv.indexOf("PU01");
	if (idx2 >= 0){
%>
  <TR><!--������������ -->
    <TD onmouseover="menu5.src='images/lm_mgr_over.gif'" style="CURSOR: hand" onclick="showhide(5);subhideall();selectedtext(5,1);" onmouseout="menu5.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border="0" name="menu5" alt="������������"></TD></TR>
  <TR>
    <TD><SPAN id="block5" style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="../servlet/PurchaseConfigMgrServlet?mode=list_order_type" target=up><SPAN id=m5_1>�������°���</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
        
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,5);subhideall() 
            href="../servlet/PurchaseConfigMgrServlet?mode=list_purchase_type" target=up><SPAN id=m5_5>�������°���</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
	 
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,6);subhideall() 
            href="../servlet/PurchaseConfigMgrServlet?mode=list_inout_type" target=up><SPAN id=m5_6>��������°���</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,7);subhideall() 
            href="../servlet/CrmServlet?mode=company_list&module=purchase" target=up><SPAN id=m5_7>���޾�ü��������</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>

		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,8);subhideall() 
            href="../pu/admin/settingPrivilege.jsp" target=up><SPAN id=m5_8>�����ڼ���</SPAN></A></TD></TR>
        <TR><TD background=images/left_hline.gif height=1></TD></TR>
	</TBODY></TABLE></SPAN></TD></TR>

<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>