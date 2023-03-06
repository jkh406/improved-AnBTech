<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "일정관리 메뉴"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
	import="java.util.StringTokenizer"
%>

<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<jsp:useBean id="anbdt" scope="request" class="com.anbtech.date.anbDate" />

<%!
	//login 계정 변수
	String id="";					//login id
	String lgname="";				//login name

	//메시지 전달변수
	String Message="";				//메시지 전달 변수 

	//view전달변수 정의하기
	String cal_id="";				//보고자하는 일정의 사번
	String view_td="";				//주간 전달날자 (yyyy/MM/dd)
	String Tyear="";				//월간 년도
	String Tmonth="";				//월간 월
	String Tday="";					//월간 일

	//일정공유자 찾기
	String[] sid;					//공유자 사번
	String[] sname;					//공유자 이름
	int shareid_cnt = 0;			//공유자 수 (갯수)

	//회사/부서 일정관리자 
	String CSMC_items = "";			//회사 공통일정관리자 명단
	String CSMC_NY = "";			//회사 신규등록 인지 편집인지
	String CSMD_items = "";			//부서 공통일정관리자 명단
	String CSMD_NY = "";			//부서 신규등록 인지 편집인지
%>

<%	

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	Message = "NO_SESSION";
	id = login_id; 				//접속자 사번
	lgname = login_name;		//접속자 이름

	//인수 초기화
	cal_id = view_td = "";
	
	/*********************************************************************
	 	넘겨온 변수 읽기 (from Calendar_View.jsp)   Sabun=&Date=
	*********************************************************************/
	cal_id = request.getParameter("Sabun");			//넘겨받은 사번
	if(cal_id == null) cal_id = id;

	/*********************************************************************
	//금주를 기준으로 view(주간,2주간)으로 넘겨줄 변수만들기
	// (해당주의 일요일 날자를 넘겨준다)
	*********************************************************************/	
	int iyear = Integer.parseInt(anbdt.getYear());			//금년
	int imonth = Integer.parseInt(anbdt.getMonth());		//금월
	int iday = Integer.parseInt(anbdt.getDates());			//금일
	int tmp_td = anbdt.getDay(iyear,imonth,iday);			//오늘요일 (1:일 2:월 ~ 7:토)

	//주어진 날자에서 요일을 이용하여 금주의 일요일 날자를 구한다.
	//찾고자 하는 날자의 일요일 날자로 setting후 주간보기로 넘겨준다.
	int sunday = iday-tmp_td+1;
	view_td = anbdt.setDate(iyear,imonth,sunday);		
	
	/*********************************************************************
	//일정 공유자 찾기
	*********************************************************************/	
	String[] indColumns = {"pid","id","item","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(indColumns);
	bean.setOrder("id ASC");
	bean.setClear();
	bean.setSearch("id",id,"item","SID");
	bean.init_unique();

	String sdata = "";
	while(bean.isAll()) {
		sdata += bean.getData("nlist");					//공유자 명단
	}

	//총 공유자 항목갯수 구하기
	shareid_cnt = 0;									//공유자 항목갯수
	for(int ii = 0; ii < sdata.length(); ii++)
		if(sdata.charAt(ii) == ';') shareid_cnt++;

	//배열에 공유자 항목담기
	sid = new String[shareid_cnt+1];					//사번 배열할당
	sname = new String[shareid_cnt+1];					//이름 배열할당	

	sid[0] = id;										//접속자 자신 사번 (초기값)
	sname[0] = lgname;									//접속자 자신 이름 (초기값)

	int aj = 0;											//복사 시작점
	int ak = 1;											//배열 시작수
	for(int ai = 0; ai < sdata.length(); ai++) {
		if(sdata.charAt(ai) == ';') {
			String slist = sdata.substring(aj,ai);		//"사번/이름" 단위로 끊어읽기
			int slash = slist.indexOf('/');
			
			sid[ak] = slist.substring(0,slash);			//사번
			sname[ak] = slist.substring(slash+1,slist.length());		//이름
			
			aj = ai+1;									//복사시작점
			ak++;
		}	//if
	} //for

	/*****************************************************
	//회사/부서 공통 일정관리자 명단을 가져오기
	*****************************************************/
	String[] itemColumns = {"item","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);

	//회사 공통 일정 관리자 명단
	String csmc_data = "where item='SMC' order by nlist DESC"; 
	bean.setSearchWrite(csmc_data);
	bean.init_write();
	
	CSMC_items = ";";							//회사 공통 일정관리자 명단 LIST (구분자 : ';')
	CSMC_NY = "";								//회사 신규 clear
	if(bean.isEmpty()) { 
		CSMC_NY = "new";						//신규등록임
		CSMC_items = "";						//공통 일정관리자 명단 없음
	} else {
		while(bean.isAll()) CSMC_items += bean.getData("nlist");
	}	

	//부서 공통 일정 관리자 명단
	String csmd_data = "where item='SMD' order by nlist DESC"; 
	bean.setSearchWrite(csmd_data);
	bean.init_write();
	
	CSMD_items = ";";							//부서 공통 일정관리자 명단 LIST (구분자 : ';')
	CSMD_NY = "";								//부서 신규 clear
	if(bean.isEmpty()) { 
		CSMD_NY = "new";						//신규등록임
		CSMD_items = "";						//공통 일정관리자 명단 없음
	} else {
		while(bean.isAll()) CSMD_items += bean.getData("nlist");
	}	

%>

<HTML><HEAD><TITLE>메뉴</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/lmenu.css" type=text/css rel=stylesheet>

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

<SCRIPT language=javascript>
<!--
//공유자 사번구하기
function RefreshMenu() {
	var index = "";
	var form=document.forms[0];
	index = form.Sabun.options[form.Sabun.selectedIndex].value;
	txt	  = form.Sabun.options[form.Sabun.selectedIndex].text;

	var conf = confirm(txt + '님의 일정을 보시겠습니까?');
	if(conf){
		location.href="CalendarLeft.jsp?Sabun=" + index;
		parent.up.location.href="Calendar_View.jsp?Sabun=" + index;
	}
}

function viewShare()
{
	window.open("CalendarShare.jsp","Info","scrollbars=no,toolbar=no,width=600,height=600");
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh);
}
-->
</SCRIPT>
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- 왼쪽 메뉴 시작-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='../images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>
  <TR><!--개인일정보기 -->
    <TD onmouseover="menu1.src='../images/lm_sch1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='../images/lm_sch1.gif'"><A 
      href="Calendar_View.jsp?FLAG=IND&Sabun=<%=cal_id%>" 
      target="up"><IMG src="../images/lm_sch1.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--개인일정등록 -->
    <TD onmouseover="menu2.src='../images/lm_sch2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall() onmouseout="menu2.src='../images/lm_sch2.gif'"><A 
      href="Calendar_WriteP.jsp?FLAG=INI" 
      target="up"><IMG src="../images/lm_sch2.gif" border=0 
    name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></A> </TD></TR>


  <TR><!--개인일정공유 -->
    <TD onmouseover="menu3.src='../images/lm_sch3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();
    onmouseout="menu3.src='../images/lm_sch3.gif'"><A 
      href="javascript:wopen('CalendarShare.jsp', '', 510,467);"><IMG src="../images/lm_sch3.gif" border=0 
      name=menu3></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

<% //부서 일정관리 권한 체크
	String toDay = anbdt.getDate(0);
	String perCom = "";
	StringTokenizer CSMD = new StringTokenizer(CSMD_items,";");

	while(CSMD.hasMoreTokens()) {
		String smd_id=CSMD.nextToken();
		if(smd_id.equals(id)) perCom = "PERMIT_DIV";
	}

	if(perCom.equals("PERMIT_DIV")) {
		//권한자 사업부 관리코드 찾기
		String[] divColumn={"id","ac_id"};	
		String item_data = "where id ='"+id+"'";
		bean.setTable("user_table");			
		bean.setColumns(divColumn);
		bean.setSearchWrite(item_data);
		bean.init_write();
		String div_id = "";
		while(bean.isAll()) div_id = bean.getData("ac_id");
		if(div_id == null) div_id = "";
%>
  <TR><!--부서일정관리 -->
    <TD onmouseover="menu4.src='../images/lm_sch4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();selectedtext(4,1) 
    onmouseout="menu4.src='../images/lm_sch4.gif'"><A 
      href="Calendar_divList.jsp?Sabun=<%=div_id%>&Date=<%=toDay%>" 
      target="up"><IMG src="../images/lm_sch4.gif" border=0 
      name=menu4></A></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,1);subhideall() 
            href="Calendar_divList.jsp?Sabun=<%=div_id%>&Date=<%=toDay%>" 
            target="up"><SPAN id=m4_1>부서일정보기</SPAN></A> </TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="Calendar_WriteD.jsp?FLAG=DIV&Sabun=<%=div_id%>" 
            target="up"><SPAN id=m4_2>부서일정등록</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<% //회사 일정관리 권한 체크
	perCom = "";
	StringTokenizer CSMC = new StringTokenizer(CSMC_items,";");

	while(CSMC.hasMoreTokens()) {
		String smc_id=CSMC.nextToken();
		if(smc_id.equals(id)) perCom = "PERMIT_COM";
	}

	if(perCom.equals("PERMIT_COM")) {
%>	
  <TR><!--회사일정관리 -->
    <TD onmouseover="menu5.src='../images/lm_sch5_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='../images/lm_sch5.gif'"><A 
      href="Calendar_comList.jsp?Sabun=0&Date=<%=toDay%>" 
      target="up"><IMG src="../images/lm_sch5.gif" border=0 
      name=menu5></A></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="Calendar_comList.jsp?Sabun=0&Date=<%=toDay%>" 
            target="up"><SPAN id=m5_1>회사일정보기</SPAN></A> </TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="Calendar_WriteC.jsp?FLAG=COM&Sabun=0" 
            target="up"><SPAN id=m5_2>회사일정등록</SPAN></A>
	</TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// 일정관리자 권한 체크
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("SC01");
	if (idx >= 0){
%>
  <TR><!--관리자 설정 -->
    <TD onmouseover="menu6.src='../images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(6);subhideall();selectedtext(6,1) 
    onmouseout="menu6.src='../images/lm_mgr.gif'"><A 
      href="../admin/settingSchedule.jsp" 
      target="up"><IMG src="../images/lm_mgr.gif" border=0 
      name=menu6></A></TD></TR>
  <TR>
    <TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,1);subhideall() 
            href="../admin/settingSchedule.jsp" 
            target="up"><SPAN id=m6_1>공휴일/일정관리자등록</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

    <TR><!--일정대상자 지정 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0><TR>
	  <TD align='right'><img src='../images/sel_user.gif' border='0'></TD>
	  <TD align='left'><FORM style='margin:0'><SELECT NAME="Sabun" onChange="RefreshMenu()">
<%	
			for(int si = 0; si < shareid_cnt+1; si++) {
				String SEL = "";
				if(cal_id.equals(sid[si])) SEL = "SELECTED";
				else SEL = "";
				out.println("<OPTION value=" + sid[si] + " " + SEL + ">" + sname[si]); 
			}
%>
		</SELECT></FORM></TD></TR></TABLE></TD></TR>

  <TR><!--바로가기 -->
    <TD align='center' height='1' valign='middle' bgcolor='#ffffff'></TD></TR>

  <TR><!--바로가기 -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>
	</TD></TR></TBODY></TABLE></BODY></HTML>

