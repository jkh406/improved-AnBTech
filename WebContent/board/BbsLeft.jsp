<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=euc-kr" %>
<%@ page import="java.util.*,com.anbtech.board.entity.*"%>
<%!
	Board_Env board_env;
	Table table;
	Redirect redirect;
%>
<%
	String tablename = request.getParameter("tablename");
	String tableno = "1";
	if(tablename.equals("free_board")) tableno = "2";
	//category_list에서 가져오기
	ArrayList category_list = new ArrayList();
	category_list = (ArrayList)request.getAttribute("Link_Category_List");
	Iterator category_iter = category_list.iterator();
%>

<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../board/css/lmenu.css" type=text/css rel=stylesheet>

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

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0" onLoad="showhide(<%=tableno%>);">
<base target="up">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='../board/images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='4' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR><!--공지게시판 -->
    <TD onmouseover="menu1.src='../board/images/lm_bd1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall();selectedtext(1,1);
    onmouseout="menu1.src='../board/images/lm_bd1.gif'"><A 
      href="../board/BbsBody.jsp?bbs=notice_board" target="_parent"><IMG src="../board/images/lm_bd1.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
<%
	if(tablename.equals("notice_board")){
		int i = 1;
		while(category_iter.hasNext()){
%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../board/images/blank.gif" width=15><IMG src="../board/images/bullet.gif" 
            align=center><SPAN id=m1_<%=i%>><%=(String)category_iter.next()%></SPAN></TD></TR>
        <TR><TD background=../board/images/left_hline.gif height=1></TD></TR>
<%		
			i++;
		}
	}
%>
		</TBODY></TABLE></SPAN></TD></TR>

  <TR><!--자유게시판 -->
    <TD onmouseover="menu2.src='../board/images/lm_bd2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall() onmouseout="menu2.src='../board/images/lm_bd2.gif'"><A 
      href="../board/BbsBody.jsp?bbs=free_board" target="_parent"><IMG src="../board/images/lm_bd2.gif" border=0 
    name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
<%
	if(tablename.equals("free_board")){
		int j = 1;
		while(category_iter.hasNext()){
%>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../board/images/blank.gif" width=15><IMG src="../board/images/bullet.gif" 
            align=center><SPAN id=m1_<%=j%>><%=(String)category_iter.next()%></SPAN></TD></TR>
        <TR><TD background=../board/images/left_hline.gif height=1></TD></TR>
<%		
			j++;
		}
	}
%>
		</TBODY></TABLE></SPAN></TD></TR>

<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("BD01");
	if (idx >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu3.src='../board/images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();selectedtext(3,1) 
    onmouseout="menu3.src='../board/images/lm_mgr.gif'"><IMG src="../board/images/lm_mgr.gif" border=0 
      name=menu3></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../board/images/blank.gif" width=9><IMG src="../board/images/bullet.gif" 
            align=center> <A onclick=selectedtext(3,1);subhideall() 
            href="../board/admin/settingBoard.jsp?tablename=<%=tablename%>&mode=" target="up"><SPAN id=m3_1>게시판환경설정</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>
