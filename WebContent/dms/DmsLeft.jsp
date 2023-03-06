<%@ include file= "../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*, java.io.*, java.util.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<jsp:useBean id="tree" class="com.anbtech.dms.admin.makeCategoryTreeItems"/>


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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" onLoad="showhide(1);" oncontextmenu="return false">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

  <TR><!--문서검색 -->
    <TD onmouseover="menu1.src='images/lm_dms1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='images/lm_dms1.gif'"><A 
      href="../servlet/AnBDMS?category=1" target=up><IMG src="images/lm_dms1.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=21>
          <TD class=type1 vAlign=center align=left>&nbsp;</TD>
		  <TD class=type1 vAlign=center align=left style="padding-top:5px; padding-bottom:5px"><!--문서분류트리시작-->
			<%
				int n_id = request.getParameter("n_id")==null?0:Integer.parseInt(request.getParameter("n_id"));
				String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");	

				StringTokenizer str = new StringTokenizer(p_id, ",");
				int spec_count = str.countTokens();
				String item[] = new String[spec_count];
			%>
			<script language="JavaScript" src="admin/tree_items.js"></script>
			<script language="JavaScript" src="admin/tree.js"></script>
			<script language="JavaScript" src="admin/tree_tpl.js"></script>
			<script language="JavaScript">
				new tree (TREE_ITEMS, tree_tpl);
			<%
				for(int i=0; i<spec_count; i++){ 
					item[i] = str.nextToken();
					out.print("trees['0'].toggle('"+item[i]+"');\n");
				}
			%>
			</script><!--문서분류트리끝-->
		  </TD></TR></TBODY></TABLE></SPAN></TD></TR>


  <TR><!--내문서함 -->
    <TD onmouseover="menu2.src='images/lm_dms2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();selectedtext(2,1) 
    onmouseout="menu2.src='images/lm_dms2.gif'"><A 
      href="../servlet/AnBDMS?mode=mylist&tablename=techdoc_data&category=1" target=up><IMG src="images/lm_dms2.gif" border=0 
    name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=21>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,1);subhideall() 
            href="../servlet/AnBDMS?mode=mylist&tablename=techdoc_data&category=1" target="up"><SPAN id=m2_1>기술문서</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif colSpan=2 height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left colSpan=2><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(2,9);subhideall() 
            href="../servlet/AnBDMS?mode=mylist&tablename=proposal_data&category=1" target="up"><SPAN id=m2_9>제안서</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>

<%	// 문서관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("DM01");
	if (idx >= 0){
%>
  <TR><!--문서등록업무 -->
    <TD onmouseover="menu3.src='images/lm_dms3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();selectedtext(3,1) 
    onmouseout="menu3.src='images/lm_dms3.gif'"><A 
      href="../servlet/AnBDMS?mode=processing&tablename=techdoc_data&category=1" target=up><IMG src="images/lm_dms3.gif" border=0 
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
            href="../servlet/AnBDMS?mode=processing&tablename=techdoc_data&category=1" target=up><SPAN id=m3_1>기술문서</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
            href="../servlet/AnBDMS?mode=processing&tablename=proposal_data&category=1" target=up><SPAN id=m3_2>제안서</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// 문서관리자 권한 체크
	int idx2 = prg_priv.indexOf("DM01");
	if (idx2 >= 0){
%>
  <TR><!--문서대출업무 -->
    <TD onmouseover="menu4.src='images/lm_dms4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();
    onmouseout="menu4.src='images/lm_dms4.gif'"><A href="../servlet/AnBDMS?mode=loan" target="up"><IMG src="images/lm_dms4.gif" border=0 
      name=menu4></A></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// 문서관리자 권한 체크
	int idx3 = prg_priv.indexOf("DM01");
	if (idx3 >= 0){
%>  
  <TR><!--관리자 설정 -->
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
            href="admin/categoryList.jsp" target=up><SPAN id=m5_1>문서카테고리관리</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="admin/access/userl.jsp" target=up><SPAN id=m5_2>문서엑세스권한관리</SPAN></A> 
      </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="admin/authorityList.jsp" target=up><SPAN id=m5_3>임시엑세스권한관리</SPAN></A> 
      </TD></TR>	  
	  </TBODY></TABLE></SPAN></TD></TR>

<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

