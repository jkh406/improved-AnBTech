<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	String year = anbdt.getYear();
	String month = anbdt.getMonth();
	String factory_no = request.getParameter("factory_no"); if(factory_no == null) factory_no = "";
	String para = "cal_month&year="+year+"&month="+month+"&factory_no="+factory_no;

	//공장번호 찾기
	String sql = "";
	bean.openConnection();

	sql = "SELECT count(*) FROM factory_info_table";
	bean.executeQuery(sql);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] factory = new String[cnt+1][2];
	factory[0][0]="";	factory[0][1]="전체";

	sql = "SELECT * FROM factory_info_table order by factory_code asc";
	bean.executeQuery(sql);
	int n=1;
	while(bean.next()) {
		factory[n][0] = bean.getData("factory_code");
		factory[n][1] = bean.getData("factory_name");
		n++;
	}

%>
<HTML><HEAD><TITLE>생산메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//공장번호 선택하기
function RefreshMenu() {
	var index = "";
	var form=document.forms[0];
	index = form.factory_no.options[form.factory_no.selectedIndex].value;
	txt	  = form.factory_no.options[form.factory_no.selectedIndex].text;
	var para = "cal_month&year=<%=year%>&month=<%=month%>&factory_no="+index;
	var conf = confirm(txt + ' 공장을 선택하시겠습니까?');
	if(conf){
		location.href="mmMenuLeft.jsp?factory_no=" + index;
		parent.view.location.href="../servlet/mpsInfoServlet?mode="+para;
	}
}

//왼쪽 메뉴 관련 스크립트
    var main_cnt = 6; //주 메뉴 갯수
     
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
<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='images/lm_bg.gif' height='100%' valign='top'>
	<TBODY>
		<TR>
			<TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  

		<TR><!--해당공장 지정 -->
			<TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" border=0><TR>
			<TD align='right'><img src='images/sel_factory.gif' border='0' alt='공장선택'></TD>
			<TD align='left'><FORM style='margin:0'><SELECT NAME="factory_no" onChange="RefreshMenu()">
<%	
			for(int si = 0; si < n; si++) {
				String SEL = "";
				if(factory_no.equals(factory[si][0])) SEL = "SELECTED";
				else SEL = "";
				out.println("<OPTION value=" + factory[si][0] + " " + SEL + ">" + factory[si][1]); 
			}
%>
		</SELECT></FORM></TD></TR></TABLE></TD></TR>
		<TR><TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
		<TR>
			<TD onmouseover="menu1.src='images/lm_bm1_over.gif'" 
    style="CURSOR: hand" onclick="showhide(1);subhideall()" onmouseout="menu1.src='images/lm_bm1.gif'">
				<A href="../servlet/mpsInfoServlet?mode=<%=para%>" target="view">
				<IMG src="images/lm_bm1.gif" border=0 name=menu1 alt='MPS정보'></A></TD></TR>
		<TR>
			<TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
			<TBODY>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
						<A onclick="selectedtext(1,1);subhideall()" href="../servlet/mpsInfoServlet?mode=<%=para%>" target="view">
				<SPAN id=m1_1>MPS월간일정</SPAN></A></TD></TR>
				<TR>
					<TD background="images/left_hline.gif" height=1></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(1,2);subhideall()" href="../servlet/mpsInfoServlet?mode=mps_view&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m1_2>MPS계획등록</SPAN></A></TD></TR>
			</TBODY></TABLE></SPAN></TD></TR>
		<TR>
			<TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
			<TD onmouseover="menu2.src='images/lm_bm2_over.gif'" 
    style="CURSOR: hand" onclick="showhide(2);subhideall()" onmouseout="menu2.src='images/lm_bm2.gif'">
				<A href="../servlet/mrpInfoServlet?mode=mrp_list&factory_no=<%=factory_no%>" target="view">
				<IMG src="images/lm_bm2.gif" border=0 name=menu2 alt='MRP정보'></A></TD></TR>
		<TR>
			<TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
			<TBODY>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(2,1);subhideall()" href="../servlet/mrpInfoServlet?mode=mrp_request&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m2_1>MRP접수현황</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(2,2);subhideall()" href="../servlet/mrpInfoServlet?mode=mrp_preview&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m2_2>임의소요량확인</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(2,3);subhideall()" href="../servlet/mrpInfoServlet?mode=mrp_list&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m2_3>MRP등록현황</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(2,4);subhideall()" href="../servlet/PurchaseMgrServlet?mode=request_search" target="view">
				<SPAN id=m2_4>구매요청현황</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(2,5);subhideall()" href="../servlet/PurchaseMgrServlet?mode=request_info&request_type=MAN" target="view">
				<SPAN id=m2_5>MRP외품목(MAN)구매</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
	<TR>
		<TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
		<TD onmouseover="menu3.src='images/lm_bm3_over.gif'" 
    style="CURSOR: hand" onclick="showhide(3);subhideall()" onmouseout="menu3.src='images/lm_bm3.gif'">
	<A href="../servlet/mfgInfoServlet?mode=mfg_list&factory_no=<%=factory_no%>" target="view">
	<IMG src="images/lm_bm3.gif" border=0 name=menu3 alt='제조정보'></A></TD></TR>
	<TR>
	<TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
			<TBODY>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(3,1);subhideall()" href="../servlet/mfgInfoServlet?mode=mfg_request&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m3_1>제조계획접수현황</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(3,2);subhideall()" href="../servlet/mfgInfoServlet?mode=mfg_preview&factory_no=<%=factory_no%>" target="view">
				<SPAN id=m3_2>긴급오더등록</SPAN></A></TD></TR>
			<TR bgColor=#f3f3f3 height=22>
				<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
				<A onclick="selectedtext(3,3);subhideall()" href="../servlet/mfgInfoServlet?mode=mfg_list&factory_no=<%=factory_no%>" target="view">
			<SPAN id=m3_3>제조계획등록현황</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
	<TR>
		<TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
		<TD onmouseover="menu4.src='images/lm_bm4_over.gif'" 
    style="CURSOR: hand" onclick="showhide(4);subhideall()" onmouseout="menu4.src='images/lm_bm4.gif'">
			<A href="../servlet/mfgOrderServlet?mode=order_receive&factory_no=<%=factory_no%>" target="view">
			<IMG src="images/lm_bm4.gif" border=0 name=menu4 alt='작업정보'></A></TD></TR>
	<TR>
		<TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
		<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
			<TBODY>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(4,1);subhideall()" href="../servlet/mfgOrderServlet?mode=order_receive&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m4_1>작업지시수신</SPAN></A></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(4,2);subhideall()" href="../servlet/mfgOrderServlet?mode=out_delivery&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m4_2>부품출고의뢰</SPAN></A></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(4,3);subhideall()" href="../servlet/mfgOrderServlet?mode=product_out_list&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m4_3>제조실적현황</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>
	<TR>
		<TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
		<TD onmouseover="menu5.src='images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick="showhide(5);subhideall()" onmouseout="menu5.src='images/lm_mgr.gif'">
			<A href="../servlet/mfgViewServlet?mode=view_run_glist&factory_no=<%=factory_no%>" target="view">
			<IMG src="images/lm_mgr.gif" border=0 name=menu5 alt='생산현황조회'></A></TD></TR>
	<TR>
		<TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
			<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
				<TBODY>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(5,1);subhideall()" href="../servlet/mfgViewServlet?mode=view_run_glist&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m5_1>재공품생산현황</SPAN></A></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(5,2);subhideall()" href="../servlet/mfgViewServlet?mode=view_run_blist&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m5_2>재공품불량현황</SPAN></A></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(5,3);subhideall()" href="../servlet/mfgViewServlet?mode=view_pd_glist&factory_no=<%=factory_no%>" target="view">
					<SPAN id=m5_3>제품생산현황</SPAN></A></TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left><IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center> 
					<A onclick="selectedtext(5,4);subhideall()" href="../servlet/mfgViewServlet?mode=view_pd_blist&factory_no=<%=factory_no%>" target="view">
				  <SPAN id=m5_4>제품불량현황</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>

<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("MM01");
	if (idx >= 0){
%>
	<TR><!--관리자 관리메뉴 -->
		<TD onmouseover="menu6.src='images/lm_admin_over.gif'" 
    style="CURSOR: hand" onclick="showhide(6);subhideall();selectedtext(6,1)" 
    onmouseout="menu6.src='images/lm_admin.gif'">
		<A onclick=selectedtext(6,1);subhideall() 
            href="admin/MmBase_list.jsp" target=view><IMG src="images/lm_admin.gif" border=0 name=menu6></a></TD></TR>
	<TR>
		<TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand">
			<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
				<TBODY>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left>
					<IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center alt='항목별권한관리'> 
					<A onclick=selectedtext(6,1);subhideall() 
            href="admin/MmBase_list.jsp" target=view><SPAN id=m6_1>항목별권한관리</SPAN></A> </TD></TR>
				<TR bgColor=#f3f3f3 height=22>
					<TD class=type1 vAlign=center align=left>
					<IMG src="images/blank.gif" width=9><IMG src="images/bullet.gif" align=center alt='작업장관리'> 
					<A onclick=selectedtext(6,2);subhideall() 
            href="admin/MmWork_list.jsp" target=view><SPAN id=m6_2>작업장관리</SPAN></A> </TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

	<TR><!--바로가기 -->
		<TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
		<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>
	<TR>
		<TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>


