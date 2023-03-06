<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../admin/errorpage.jsp"
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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false" onLoad="showhide(1)">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  

  <TR><!--품목검색 -->
    <TD onmouseover="menu1.src='images/lm_ncm_over.gif'" 
    style="CURSOR: hand" onclick="showhide(1);subhideall()"
    onmouseout="menu1.src='images/lm_ncm.gif'"><A 
      href="../servlet/CodeMgrServlet?mode=list_item" target=up><IMG src="images/lm_ncm.gif" border=0 name=menu1 alt="품목검색"></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=21>
          <TD class=type1 vAlign=center align=left>&nbsp;</TD>
		  <TD class=type1 vAlign=center align=left style="padding-top:5px; padding-bottom:5px"><!--품목트리시작-->
			<%
				int n_id = request.getParameter("n_id")==null?0:Integer.parseInt(request.getParameter("n_id"));
				String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");	

				StringTokenizer str = new StringTokenizer(p_id, ",");
				int spec_count = str.countTokens();
				String item[] = new String[spec_count];
			%>
			<script language="JavaScript" src="admin/code_tree_items.js"></script>
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

<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("CM02");
	if (idx >= 0){
%>
  <TR><!--품목등록 -->
    <TD onmouseover="menu2.src='images/lm_ncm2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall();
    onmouseout="menu2.src='images/lm_ncm2.gif'"><A href="../servlet/CodeMgrServlet?mode=reg_item" target="up"><IMG src="images/lm_ncm2.gif" border=0 
    name=menu2 alt="품목등록"></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>
<% } else {
%>  <SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<% }
%>	

<%	// 관리자 권한 체크
	idx = prg_priv.indexOf("CM01");
	if (idx >= 0){
%>
	<TR><!--관리자 설정 -->
    <TD onmouseover="menu3.src='images/lm_mgr_over.gif'" style="CURSOR: hand" onclick='showhide(3);subhideall();selectedtext(3,1);' onmouseout="menu3.src='images/lm_mgr.gif'"><A onclick=selectedtext(3,1);subhideall() 
            href="admin/category_list.jsp" target="up"><IMG src="images/lm_mgr.gif" alt="기준정보관리" border=0 name=menu3></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
                
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="admin/category_list.jsp" target="up"><SPAN id=m3_1>품목분류관리</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,2);subhideall() 
             href="javascript:make_tree();" target="_self"><SPAN id=m3_2>품목분류트리적용</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,3);subhideall() 
            href="../servlet/CodeMgrServlet?mode=add_template_code" target=up><SPAN id=m3_3>품목별표준규격관리</SPAN></A> </TD></TR>
        <TR>
          <TD background=images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,4);subhideall() 
            href="admin/managerItemSpec.jsp" target=up><SPAN id=m3_4>품목별규격적용</SPAN></A></TD></TR>
		<TR>
          <TD background=images/left_hline.gif height=1></TD></TR>	
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="images/blank.gif" width=9><IMG src="images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,5);subhideall() 
            href="admin/settingPrivilege.jsp" target="up"><SPAN id=m3_5>관리자설정</SPAN></A> </TD></TR>	
		</TBODY></TABLE></SPAN></TD></TR>
<% } else {
%>  <SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<% }
%>	

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

<SCRIPT>
<!--
// 트리생성
function make_tree(){
wopen('../servlet/CodeMgrServlet?mode=make_tree','make_tree','5','5','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

-->
</SCRIPT>