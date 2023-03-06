<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkSC01.jsp"%>

<%@ page		
	info= "공통일정관리 담당자 입력하기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	String CSMC_items = "";			//회사 공통일정관리자 명단
	String CSMC_NY = "";			//회사 신규등록 인지 편집인지
	String CSMD_items = "";			//부서 공통일정관리자 명단
	String CSMD_NY = "";			//부서 신규등록 인지 편집인지
		
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자

	CSMC_items = request.getParameter("CSMC_items");
	CSMC_NY = request.getParameter("CSMC_NY");
	CSMD_items = request.getParameter("CSMD_items");
	CSMD_NY = request.getParameter("CSMD_NY");

	/*********************************************************************
	 	회사/부서 일정관리자 추가/삭제 실행하기
	*********************************************************************/
	String REQ = "";
	REQ = request.getParameter("csmc_req");			//회사 일정관리자 요청사항
	if((REQ != null) && (REQ.length() == 0))
		REQ = request.getParameter("csmd_req");		//부서 일정관리자 요청사항

	if(REQ != null) {
		//회사 일정관리자 삭제 요청시 처리하기
		if(REQ.equals("SMC_DEL")) {			
			String delD = Hanguel.toHanguel(request.getParameter("csmc_items"));	//삭제할 회사일정관리자
			if(delD != null) {
				String upitems = str.repWord(CSMC_items,delD+";","");
				String delData = "update calendar_common set nlist='" + upitems + "' where item='SMC'";
				bean.execute(delData);
			} //if (del)

		//회사 일정관리자 추가 요청시 처리하기
		} else if (REQ.equals("SMC_ADD")) { //추가
			String addD = Hanguel.toHanguel(request.getParameter("csmc_id"));	//추가할 회사일정관리자
			if(addD != null) {
				String additems = "";	//추가할 회사일정관리자
				String addData = "";	//query 문장
				//신규추가
				if(CSMC_NY.equals("new")) {
					additems = addD;
					addData = "insert into calendar_common (pid,item,nlist) values('"+bean.getID()+"',";
					addData +="'SMC','"+additems+"')";
				//update
				} else {
					additems = CSMC_items+addD;
					addData = "update calendar_common set nlist='" + additems + "' where item='SMC'";
				}
				try {bean.execute(addData); } catch (Exception e) { out.println(e); }
			}//if (add)
		//부서 일정관리자 삭제 요청시 처리하기
		} else if(REQ.equals("SMD_DEL")) {			
			String delD = Hanguel.toHanguel(request.getParameter("csmd_items"));	//삭제할 부서일정관리자
			if(delD != null) {
				String upitems = str.repWord(CSMD_items,delD+";","");
				String delData = "update calendar_common set nlist='" + upitems + "' where item='SMD'";
				bean.execute(delData);
			} //if (del)

		//부서 일정관리자 추가 요청시 처리하기
		} else if (REQ.equals("SMD_ADD")) { //추가
			String addD = Hanguel.toHanguel(request.getParameter("csmd_id"));	//추가할 부서일정관리자
			if(addD != null) {
				String additems = "";	//추가할 부서일정관리자
				String addData = "";	//query 문장
				//신규추가
				if(CSMD_NY.equals("new")) {
					additems = addD;
					addData = "insert into calendar_common (pid,item,nlist) values('"+bean.getID()+"',";
					addData +="'SMD','"+additems+"')";
				//update
				} else {
					additems = CSMD_items+addD;
					addData = "update calendar_common set nlist='" + additems + "' where item='SMD'";
				}
				bean.execute(addData);
			}//if (add)
		} //if (del/add)
	} //if (전체)
	
	/*********************************************************************
	 	공휴일 추가/삭제 실행하기
	*********************************************************************/
	String query = "";
	REQ = request.getParameter("hd_add_req"); //공휴일 추가
	if((REQ != null) && (REQ.equals("HD_ADD"))) {
			String data = Hanguel.toHanguel(request.getParameter("hd_data"));	//년/월/일 휴일명 받기
			StringTokenizer HD_DATA = new StringTokenizer(data,"/");
			int hdi = 0;
			String[] RD = new String[4];
			while(HD_DATA.hasMoreTokens()) {
				String item=HD_DATA.nextToken();
				RD[hdi] = item;
				hdi++;	
			}
			query = "insert into calendar_common (pid,item,hdyear,hdmon,nlist) values('";
			query += bean.getID() + "','BHD','"+RD[0]+"','"+RD[1]+"','"+RD[2]+"#"+RD[3]+";')";
			bean.execute(query);
	}
	REQ = request.getParameter("hd_del_req"); //공휴일 삭제
	if((REQ != null) && (REQ.equals("HD_DEL"))) {
			String data = Hanguel.toHanguel(request.getParameter("hd_items"));	//년/월/일 휴일명 받기
			query = "delete from calendar_common where pid='"+data+"'";
			bean.execute(query);
	}


	/*****************************************************
	//회사/부서 공통 일정관리자 명단을 가져오기
	*****************************************************/
	String[] itemColumns = {"nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);

	//회사 공통 일정 관리자 명단
	String csmc_data = "where item='SMC' order by nlist DESC"; 
	bean.setSearchWrite(csmc_data);
	bean.init_write();

	CSMC_items = "";							//회사 공통 일정관리자 명단 LIST (구분자 : ';')
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
	
	CSMD_items = "";							//부서 공통 일정관리자 명단 LIST (구분자 : ';')
	CSMD_NY = "";								//부서 신규 clear
	if(bean.isEmpty()) { 
		CSMD_NY = "new";						//신규등록임
		CSMD_items = "";						//공통 일정관리자 명단 없음
	} else {
		while(bean.isAll()) CSMD_items += bean.getData("nlist");
	}	


%>


<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" action="settingSchedule.jsp" method="post" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 일정관리자 및 공휴일 설정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:history.go(-1);"><img src="../images/bt_confirm.gif" border="0"></a></TD>
			</TR></TBODY></TABLE></TD></TR></TABLE>


<!-- 회사일정 관리자 -->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
		<!-- 테두리 -->		
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		<TR><TD colspan='5'>
		
		<!--내용-->
		<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
			<TBODY>
			<TR height='25px'>
				<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'>회사일정 관리자</TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='5'></TD></TR>
			<TR><TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT : bolder;font-size: 9pt;'>권한자</TD>
				<TD style='padding-left:5px;' width='105'>
			
					<select name="csmc_items" size="5"  multiple style="width:100">
			<%
						StringTokenizer CSMC = new StringTokenizer(CSMC_items,";");
						while(CSMC.hasMoreTokens()) {
							String item=CSMC.nextToken();
							out.println("<option value='"+item+"'>" + item + "</option>");
						}
			%>		</select><input type="hidden" name="csmc_req">
				</TD>
				<TD style='padding-left:5px;' width='195' valign='bottom'><INPUT TYPE=text NAME='csmc_id' SIZE=13><br>
						<a href="javascript:addItemCsmc();"><img src='../images/bt_add.gif' border='0' align='absmiddle'></a>
						<a href="javascript:delItemCsmc();"><img src='../images/bt_del.gif' border='0' align='absmiddle'></a>
				</TD>
				<TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;'>역 할</TD>
				<TD style='padding-left:5px;font-size: 9pt;'>회사일정을 관리합니다.</TD></Tr>
				</TD></TR></TBODY></TABLE><!--내용끝-->	
		
		</TD></TR></TABLE><!--테두리 끝-->	
	</TD></TR>
</TBODY></TABLE><BR>


<!-- 부서일정 관리자 -->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
		<!-- 테두리 -->		
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		<TR><TD colspan='5'>
		
		<!--내용-->
		<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
			<TBODY>
			<TR height='25px'>
				<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'>부서일정 관리자</TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='5'></TD></TR>
			<TR><TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT : bolder;font-size: 9pt;'>권한자</TD>
				<TD style='padding-left:5px;' width='105'>
					<select name="csmd_items" size=5 multiple style="width:100">
			<%
						StringTokenizer CSMD = new StringTokenizer(CSMD_items,";");
						while(CSMD.hasMoreTokens()) {
							String item=CSMD.nextToken();
							out.println("<option value='"+item+"'>" + item + "</option>");
						}
			%>		</select><input type="hidden" name="csmd_req">
				</TD>
				<TD style='padding-left:5px;' width='195' valign='bottom'><INPUT TYPE=text NAME='csmd_id' SIZE=13><br><a href="javascript:addItemCsmd();"><img src='../images/bt_add.gif' border='0' align='absmiddle'></a>
				<a href="javascript:delItemCsmd();"><img src='../images/bt_del.gif' border='0' align='absmiddle'></a>
				</TD>
				<TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;'>역 할</TD>
				<TD style='padding-left:5px;font-size: 9pt;'>부서일정을 관리합니다.</TD></Tr>
				</TD></TR></TBODY></TABLE><!--내용끝-->	
		
		</TD></TR></TABLE><!--테두리 끝-->	
	</TD></TR>
</TBODY></TABLE><BR>


<!-- 공휴일 리스트 -->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
		<!-- 테두리 -->		
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		<TR><TD colspan='5'>
		
		<!--내용-->
		<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
			<TBODY>
			<TR height='25px'>
				<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'>공휴일 리스트</TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='5'></TD></TR>
			<TR><TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT : bolder;font-size: 9pt;'>공휴일일정</TD>
				<TD style='padding-left:5px;' width='105'>
			
					<select name="hd_items" size="5"   multiple style="width:150">
				<% //휴일 년/월/일 휴일명을 출력한다.
					String HLIST = "";
					String[] commonColumns = {"pid","item","hdyear","hdmon","nlist"};
					String holidy_data = "where item='BHD' order by hdyear,hdmon,nlist asc";
					bean.setTable("CALENDAR_COMMON");
					bean.setColumns(commonColumns);
					bean.setSearchWrite(holidy_data);
					bean.init_write();
		
					while(bean.isAll()) {
						String data = bean.getData("hdyear")+"/"+bean.getData("hdmon")+"/"+bean.getData("nlist");
						data = str.repWord(data,"#"," ");
						out.println("<option value='"+bean.getData("pid")+"'>" + data + "</option>");
					}	
				%>
				</select>
				</TD>
				<TD style='padding-left:5px;' width='150' valign='bottom'>
						<%
				//년도
				int cyear = Integer.parseInt(anbdt.getYear());
				out.println("<select name='hd_year'>");
				for(int cy=0; cy < 6; cy++) {
					int syear = cyear + cy;
					if(cy == 0) out.println("<option selected value='"+syear+"'>" + syear + "</option>"); 
					else out.println("<option value='"+syear+"'>" + syear + "</option>");
				}
				out.println("</select>");

				//월
				out.println("<select name='hd_month'>");
				for(int cm=1; cm < 13; cm++) {
					String smonth = "";
					if(cm < 10) smonth = "0"+cm;
					else smonth = Integer.toString(cm);

					if(cm == 1) out.println("<option selected value='"+smonth+"'>" + smonth + "</option>"); 
					else out.println("<option value='"+smonth+"'>" + smonth + "</option>");
				}
				out.println("</select>");

				//일
				out.println("<select name='hd_date'>");
				for(int cd=1; cd < 32; cd++) {
					String sdate = "";
					if(cd < 10) sdate = "0"+cd;
					else sdate = Integer.toString(cd);

					if(cd == 1) out.println("<option selected value='"+sdate+"'>" + sdate + "</option>"); 
					else out.println("<option value='"+sdate+"'>" + sdate + "</option>");
				}
				out.println("</select>");
			  %><br>
				<INPUT TYPE=text NAME='hd_text' SIZE=18><br><a href="javascript:addItemHD();"><img src='../images/bt_add.gif' border='0' align='absmiddle'></a><input type="hidden" name="hd_del_req">
				<a href="javascript:delItemHD();"><img src='../images/bt_del.gif' border='0' align="absmiddle"></a>
				</TD>
				<TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;'>역 할</TD>
				<TD style='padding-left:5px;font-size: 9pt;'>공휴일 일정을 관리합니다.</TD></Tr>
				</TD></TR></TBODY></TABLE><!--내용끝-->	
		
		</TD></TR></TABLE><!--테두리 끝-->	
	</TD></TR>
</TBODY></TABLE>


<INPUT TYPE=hidden NAME='hd_add_req' SIZE=10 MAXLENGTH=10>
<INPUT TYPE=hidden NAME='hd_data' SIZE=10 MAXLENGTH=10>
<input type='hidden' name='CSMC_items' value='<%=CSMC_items%>'>
<input type='hidden' name='CSMC_NY' value='<%=CSMC_NY%>'>
<input type='hidden' name='CSMD_items' value='<%=CSMD_items%>'>
<input type='hidden' name='CSMD_NY' value='<%=CSMD_NY%>'>
</FORM>
</BODY>
</HTML>

<Script language = "Javascript">
 <!-- 
//회사일정 관리자 삭제하기 
function delItemCsmc()
{
	var num = document.sForm.csmc_items.selectedIndex;
	if(num < 0){
		alert("삭제할 회사일정 관리자를 선택해 주십시오.");
		return;
	}
	if(!confirm("삭제하시겠습니까?"))
		return;
	document.sForm.action="settingSchedule.jsp";
	document.sForm.csmc_req.value="SMC_DEL";
	document.sForm.csmc_items.options[num].value;
	document.sForm.submit();
}

//회사일정 관리자 추가하기 
function addItemCsmc()
{
	var data = document.sForm.csmc_id.value;
	var indi = '<%=CSMC_items%>';
	if(data == ""){
		alert("추가할 관리자를 입력해 주십시오.");
		return;
	} else if(indi.indexOf(data) != -1){
		alert("회사일정 관리자로 이미등록된 사람입니다.");
		return;
	}
	document.sForm.action="settingSchedule.jsp";
	document.sForm.csmc_req.value="SMC_ADD";
	document.sForm.csmc_id.value=data+';';
	document.sForm.submit();
}
//부서일정 관리자 삭제하기 
function delItemCsmd()
{
	var num = document.sForm.csmd_items.selectedIndex;
	if(num < 0){
		alert("삭제할 부서일정 관리자를 선택해 주십시오.");
		return;
	}
	if(!confirm("삭제하시겠습니까?"))
		return;
	document.sForm.action="settingSchedule.jsp";
	document.sForm.csmd_req.value="SMD_DEL";
	document.sForm.csmd_items.options[num].value;
	document.sForm.submit();
}

//부서일정 관리자 추가하기 
function addItemCsmd()
{
	var data = document.sForm.csmd_id.value;
	var indi = '<%=CSMD_items%>';
	if(data == ""){
		alert("추가할 관리자를 입력해 주십시오.");
		return;
	} else if(indi.indexOf(data) != -1){
		alert("부서일정 관리자로 이미등록된 사람입니다.");
		return;
	}
	document.sForm.action="settingSchedule.jsp";
	document.sForm.csmd_req.value="SMD_ADD";
	document.sForm.csmd_id.value=data+';';
	document.sForm.submit();
}

//해당년도 휴일명을 추가하기
function addItemHD()
{
	var data;
	//년도
	var yn;
	var y = document.sForm.hd_year;
	var len = y.length;
	for (i=0;i<len;i++){
		if(y.options[i].selected) yn = y.selectedIndex;
	}
	data = y.options[yn].value+'/';
	//alert(y.options[yn].value);

	//월
	var mn;
	var m = document.sForm.hd_month;
	var len = m.length;
	for (i=0;i<len;i++){
		if(m.options[i].selected) mn = m.selectedIndex;
	}
	data += m.options[mn].value+'/';
	//alert(m.options[mn].value);

	//일
	var dn;
	var d = document.sForm.hd_date;
	var len = d.length;
	for (i=0;i<len;i++){
		if(d.options[i].selected) dn = d.selectedIndex;
	}
	data += d.options[dn].value+'/';
	//alert(d.options[dn].value);

	//휴일명
	var name = document.sForm.hd_text.value;
	data += name+'/';
	//alert(data);

	document.sForm.action="settingSchedule.jsp";
	document.sForm.hd_add_req.value="HD_ADD";
	document.sForm.hd_data.value=data;
	document.sForm.submit();
}

//해당년도 휴일명을 삭제하기 
function delItemHD()
{
	var num = document.sForm.hd_items.selectedIndex;
	if(num < 0){
		alert("삭제할 휴일명을 선택해 주십시오.");
		return;
	}
	if(!confirm("삭제하시겠습니까?"))
		return;
	document.sForm.action="settingSchedule.jsp";
	document.sForm.hd_del_req.value="HD_DEL";
	document.sForm.hd_items.options[num].value;
	document.sForm.submit();
}

-->
</Script>
