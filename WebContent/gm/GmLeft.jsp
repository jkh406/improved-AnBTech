<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info		= "��ǰ�����޴�"		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../admin/errorpage.jsp"
%>

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
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" onLoad="showhide(1);">

<!-- ���� �޴� ����-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

<%	// ������ ���� üũ
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("GM01");
	if (idx >= 0){
%>
	<TR><!--�𵨵�� -->
		<TD onmouseover="menu1.src='images/lm_gm1_over.gif'" 
		style="CURSOR: hand" onclick=showhide(1);subhideall();
		onmouseout="menu1.src='images/lm_gm1.gif'"><A href="../servlet/GoodsInfoServlet?mode=add_model" target="up"><IMG src="images/lm_gm1.gif" border=0 
		name=menu1 alt="���������"></A></TD></TR>
	  <TR>
		<TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>
<%}%>
  <TR><!--�𵨰˻� -->
    <TD onmouseover="menu2.src='images/lm_gm2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall()
    onmouseout="menu2.src='images/lm_gm2.gif'"><A 
      href="../servlet/GoodsInfoServlet?mode=list_model" target=up><IMG src="images/lm_gm2.gif" border=0 
      name=menu2 alt="��������ȸ"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

<%	
	if (idx >= 0){
%>
  

  <TR><!--������ ���� -->
    <TD onmouseover="menu3.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();selectedtext(3,1) 
    onmouseout="menu3.src='images/lm_mgr.gif'"><IMG src="images/lm_mgr.gif" border=0 name=menu3 alt="������������"></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
       
        
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="admin/category_list.jsp" target=up><SPAN id=m3_1>��ǰ�з�����</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
            href="../servlet/GoodsInfoServlet?mode=add_template" target=up><SPAN id=m3_2> ��ǰ��ǥ�ر԰ݼ���</SPAN></A></TD></TR>
        <TR>
         <TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,3);subhideall() 
            href="../servlet/GoodsInfoServlet?mode=make_tree" target=up><SPAN id=m3_3>ī�װ�Ʈ������</SPAN></A></TD></TR>
		<TR>
			<TD background=images/left_hline.gif height=1></TD></TR>	
		 <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,4);subhideall() 
            href="admin/settingPrivilege.jsp" target="up"><SPAN id=m3_4>�����ڼ���</SPAN></A> </TD></TR>
        	
			
			</TBODY></TABLE></TD></TR>

<%	}else{	%>
	<SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>