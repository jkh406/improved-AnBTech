<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전자결재 메인Menu"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.gw.entity.*"
	
%>
<%
	//초기화 선언
	com.anbtech.gw.entity.TableItemCount table;
	
	//갯수 전달변수
	int APP_ING_CNT = 0;		//미결함 갯수
	int ASK_ING_CNT = 0;		//진행함 갯수

	int APP_BOX_CNT = 0;		//기결함 갯수
	int APP_GEN_CNT = 0;		//기결함 갯수(일반문서)
	int APP_SER_CNT = 0;		//기결함 갯수(고객관리문서)

	int APP_OUT_CNT = 0;		//기결함 갯수(외출계)
	int APP_BTR_CNT = 0;		//기결함 갯수(출장신청서)
	int APP_HDY_CNT = 0;		//기결함 갯수(휴가원)
	int APP_CAR_CNT = 0;		//기결함 갯수(배차신청서)
	int APP_REP_CNT = 0;		//기결함 갯수(보고서)
	int APP_BRP_CNT = 0;		//기결함 갯수(출장보고서)
	int APP_DRF_CNT = 0;		//기결함 갯수(기안서)
	int APP_CRD_CNT = 0;		//기결함 갯수(명함신청서)
	int APP_RSN_CNT = 0;		//기결함 갯수(사유서)
	int APP_HLP_CNT = 0;		//기결함 갯수(협조전)
	int APP_OTW_CNT = 0;		//기결함 갯수(연장근무신청서)
	int APP_OFF_CNT = 0;		//기결함 갯수(구인의뢰서)
	int APP_EDU_CNT = 0;		//기결함 갯수(교육일지)
	int APP_AKG_CNT = 0;		//기결함 갯수(승인원)
	int APP_TD_CNT = 0;			//기결함 갯수(기술문서)
	int APP_ODT_CNT = 0;		//기결함 갯수(공지공문)
	int APP_IDS_CNT = 0;		//기결함 갯수(사내공문)
	int APP_ODS_CNT = 0;		//기결함 갯수(사외공문)
	int APP_AST_CNT = 0;		//기결함 갯수(자산관리)
	int APP_EST_CNT = 0;		//기결함 갯수(견적관리)
	int APP_EWK_CNT = 0;		//기결함 갯수(특근관리)
	int APP_BOM_CNT = 0;		//기결함 갯수(BOM관리)
	int APP_DCM_CNT = 0;		//기결함 갯수(설계변경관리)
	int APP_PCR_CNT = 0;		//기결함 갯수(구매요청관리)
	int APP_ODR_CNT = 0;		//기결함 갯수(발주요청관리)
	int APP_PWH_CNT = 0;		//기결함 갯수(구매입고관리)
	int APP_TGW_CNT = 0;		//기결함 갯수(부품출고관리)

	int REJ_BOX_CNT = 0;		//반려함 갯수
	int TMP_BOX_CNT = 0;		//임시함 갯수
	int DEL_BOX_CNT = 0;		//삭제함 갯수 (admin 관리용)

	int SEE_BOX_CNT = 0;		//통보함 갯수 (읽지 않은 문서)
	int SEE_BOX_TOT = 0;		//통보함 갯수 (전체문서)

	//-----------------------------------
	//	전자결재 수량 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new com.anbtech.gw.entity.TableItemCount();
	Iterator table_iter = table_list.iterator();

	int no = 0;
	while(table_iter.hasNext()){
		table = (TableItemCount)table_iter.next();
		if(no == 0) APP_ING_CNT = table.getAppIngCnt();		//진행함
		if(no == 1) ASK_ING_CNT = table.getAskIngCnt();		//미결함

		if(no == 2) APP_BOX_CNT = table.getAppBoxCnt();		//기결함
		if(no == 3) APP_GEN_CNT = table.getAppGenCnt();		//기결 일반문서
		if(no == 4) APP_SER_CNT = table.getAppSerCnt();		//기결 고객관리

		if(no == 5) APP_OUT_CNT = table.getAppOutCnt();		
		if(no == 6) APP_BTR_CNT = table.getAppBtrCnt();
		if(no == 7) APP_HDY_CNT = table.getAppHdyCnt();
		if(no == 8) APP_CAR_CNT = table.getAppCarCnt();

		if(no == 9) APP_REP_CNT = table.getAppRepCnt();
		if(no == 10) APP_BRP_CNT = table.getAppBrpCnt();
		if(no == 11) APP_DRF_CNT = table.getAppDrfCnt();
		if(no == 12) APP_CRD_CNT = table.getAppCrdCnt();
		if(no == 13) APP_RSN_CNT = table.getAppRsnCnt();
		if(no == 14) APP_HLP_CNT = table.getAppHlpCnt();
		if(no == 15) APP_OTW_CNT = table.getAppOtwCnt();
		if(no == 16) APP_OFF_CNT = table.getAppOffCnt();
		if(no == 17) APP_EDU_CNT = table.getAppEduCnt();
		if(no == 18) APP_AKG_CNT = table.getAppAkgCnt();	//승인원
		if(no == 19) APP_TD_CNT = table.getAppTdCnt();		//기술문서
		if(no == 20) APP_ODT_CNT = table.getAppOdtCnt();	//공지공문
		if(no == 21) APP_IDS_CNT = table.getAppIdsCnt();	//사내공문
		if(no == 22) APP_ODS_CNT = table.getAppOdsCnt();	//사외공문
		if(no == 23) APP_AST_CNT = table.getAppAstCnt();	//자산관리
		if(no == 24) APP_EST_CNT = table.getAppEstCnt();	//견적관리
		if(no == 25) APP_EWK_CNT = table.getAppEwkCnt();	//특근관리
		if(no == 26) APP_BOM_CNT = table.getAppBomCnt();	//BOM관리
		if(no == 27) APP_DCM_CNT = table.getAppDcmCnt();	//설계변경관리
		if(no == 28) APP_PCR_CNT = table.getAppPcrCnt();	//구매요청관리
		if(no == 29) APP_ODR_CNT = table.getAppOdrCnt();	//발주요청관리
		if(no == 30) APP_PWH_CNT = table.getAppPwhCnt();	//구매입고관리
		if(no == 31) APP_TGW_CNT = table.getAppTgwCnt();	//부품출고관리

		if(no == 32) REJ_BOX_CNT = table.getRejBoxCnt();	//반려함
		if(no == 33) TMP_BOX_CNT = table.getTmpBoxCnt();	//저장함
		
		if(no == 34) SEE_BOX_CNT = table.getSeeBoxCnt();	//통보함 (조회수량)
		if(no == 35) SEE_BOX_TOT = table.getSeeBoxTot();	//통보함 (총수량)

		if(no == 36) DEL_BOX_CNT = table.getDelBoxCnt();	//삭제함

		no++;
	}

%>

<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../gw/css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//왼쪽 메뉴 관련 스크립트
    var main_cnt = 9 //주 메뉴 갯수
     
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

	   for ( i=1 ; i <= 23 ; i++ )
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
	
	function wopen(url, t, w, h,st) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
	}
  -->
</SCRIPT>
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='../gw/images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>  
  <TR><!--미결함 -->
    <TD onmouseover="menu1.src='../gw/images/lm_app1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='../gw/images/lm_app1.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=APP_ING" target="view"><IMG src="../gw/images/lm_app1.gif" border=0 
      name=menu1></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 13px;"><FONT COLOR= '#565656' SIZE='2'>(<%=APP_ING_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--진행함 -->
    <TD onmouseover="menu2.src='../gw/images/lm_app2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall() onmouseout="menu2.src='../gw/images/lm_app2.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=ASK_ING" target="view"><IMG src="../gw/images/lm_app2.gif" border=0 
    name=menu2></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 41px;"><FONT COLOR= '#565656' SIZE='2'>(<%=ASK_ING_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>


  <TR><!--반려함 -->
    <TD onmouseover="menu4.src='../gw/images/lm_app4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();
    onmouseout="menu4.src='../gw/images/lm_app4.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=REJ_BOX" target="view"><IMG src="../gw/images/lm_app4.gif" border=0 
      name=menu4></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 69px;"><FONT COLOR= '#565656' SIZE='2'>(<%=REJ_BOX_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--저장함 -->
    <TD onmouseover="menu5.src='../gw/images/lm_app5_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();
    onmouseout="menu5.src='../gw/images/lm_app5.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=TMP_BOX" target="view"><IMG src="../gw/images/lm_app5.gif" border=0 
      name=menu5></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 97px;"><FONT COLOR= '#565656' SIZE='2'>(<%=TMP_BOX_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--통보함 -->
    <TD onmouseover="menu6.src='../gw/images/lm_app6_over.gif'" 
    style="CURSOR: hand" onclick=showhide(6);subhideall();
    onmouseout="menu6.src='../gw/images/lm_app6.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=SEE_BOX" target="view"><IMG src="../gw/images/lm_app6.gif" border=0 
      name=menu6></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 124px;"><FONT COLOR= '#565656' SIZE='2'>(<%=SEE_BOX_CNT%>/<%=SEE_BOX_TOT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--기결함 -->
    <TD onmouseover="menu3.src='../gw/images/lm_app3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();
    onmouseout="menu3.src='../gw/images/lm_app3.gif'"><A 
      href="../servlet/ApprovalMenuServlet?mode=APP_BOX" target="view"><IMG src="../gw/images/lm_app3.gif" border=0 
      name=menu3></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 70px; POSITION: absolute; TOP: 152px;"><FONT COLOR= '#565656' SIZE='2'>(<%=APP_BOX_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></TD></TR>

  <TR><!--전자결재양식함 -->
    <TD onmouseover="menu9.src='../gw/images/lm_app8_over.gif'" 
    style="CURSOR: hand" onclick=showhide(9);subhideall();selectedtext(9,1) 
    onmouseout="menu9.src='../gw/images/lm_app8.gif'"><IMG src="../gw/images/lm_app8.gif" border=0 
      name=menu9></TD></TR>
  <TR>
    <TD><SPAN id=block9 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,1);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=HYU_GA" target="view"><SPAN id=m9_1>휴(공)가원</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,2);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=OE_CHUL" target="view"><SPAN id=m9_2>외출계</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,3);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=CHULJANG_SINCHEONG" target="view"><SPAN id=m9_3>출장신청서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,4);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=BOGO" target="view"><SPAN id=m9_4>보고서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,5);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=CHULJANG_BOGO" target="view"><SPAN id=m9_5>출장보고서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,6);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=GIAN" target="view"><SPAN id=m9_6>기안서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,7);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=MYEONGHAM" target="view"><SPAN id=m9_7>명함신청서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,8);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=SAYU" target="view"><SPAN id=m9_8>사유서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,9);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=HYEOPJO" target="view"><SPAN id=m9_9>협조전</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,10);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=GUIN" target="view"><SPAN id=m9_10>구인의뢰서</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(9,11);subhideall() 
            href="../gw/approval/SpaceLink/FormPapersInfo.jsp?ag_name2=GYOYUK_ILJI" target="view"><SPAN id=m9_11>교육일지</SPAN></A></TD></TR>
        </TBODY></TABLE></SPAN></TD></TR>


  <TR><!--환경설정 -->
    <TD onmouseover="menu7.src='../gw/images/lm_app7_over.gif'" 
    style="CURSOR: hand" onclick=showhide(7);subhideall();selectedtext(7,1) 
    onmouseout="menu7.src='../gw/images/lm_app7.gif'"><IMG src="../gw/images/lm_app7.gif" border=0 
      name=menu7></TD></TR>
  <TR>
    <TD><SPAN id=block7 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(7,1);subhideall() 
            href="javascript:showModalDialog('../gw/approval/modalEnv.jsp?strSrc=eleApproval_passwordMgr.jsp&width=450&height=197','absence','dialogWidth:457px;dialogHeight:223px;toolbar=0;scrollBars=no;status=no;resizable=no');"><SPAN id=m7_1>결재비밀번호설정</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick='selectedtext(7,2);subhideall()'            href="javascript:showModalDialog('../gw/approval/modalEnv.jsp?strSrc=../../servlet/ApprovalAttorneyServlet&mode=ATT_L&width=450&height=225','absence','dialogWidth:459px;dialogHeight:252px;toolbar=0;scrollBars=no;status=no;resizable=yes');"><SPAN id=m7_2>부재중결재자지정</SPAN></A></TD></TR></TBODY></TABLE></SPAN></TD></TR> 


<%	// 관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("AP01");
	if (idx >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu8.src='../gw/images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(8);subhideall();selectedtext(8,1) 
    onmouseout="menu8.src='../gw/images/lm_mgr.gif'"><IMG src="../gw/images/lm_mgr.gif" border=0 
      name=menu8></TD></TR>
  <TR>
    <TD><SPAN id=block8 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(8,1);subhideall() 
            href="../gw/admin/settingApproval.jsp" target="view"><SPAN id=m8_1>전자결재환경설정</SPAN></A></TD></TR>
        <TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(8,2);subhideall() 
            href="../servlet/ApprovalMenuServlet?mode=DEL_BOX" target="view"><SPAN id=m8_2>보존함(<%=DEL_BOX_CNT%>)</SPAN></A></TD></TR>
		<TR>
          <TD background=../gw/images/left_hline.gif height=1></TD></TR>
		<TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../gw/images/blank.gif" width=9><IMG src="../gw/images/bullet.gif" 
            align=center> <A onclick=selectedtext(8,3);subhideall() 
            href="javascript:sendMail();"><SPAN id=m8_3>전자우편(보존문서)</SPAN></A></TD></TR>
		</TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block8 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

<script language=javascript>
<!--
function wopen(url, t, w, h, st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+st);
}
//삭제문서 작성자에게 전자우편보내기
function sendMail()
{
	var url = "../gw/approval/SpaceLink/sendMailDelDocument.jsp";	wopen(url,"del_view","700","650","scrollbar=no,toolbar=no,status=no,resizable=yes");
}
-->
</script>