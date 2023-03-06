<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "양식종류별 환경SETTING"		
	contentType = "text/html; charset=euc-kr" 		
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
	/*****************************************************
	//	변수 SETTING
	//****************************************************/
	String query = "";							//query문장 만들기
	String Message = "";						//메시지 전달

	String[][] ysforms = new String[26][2];		
	for(int n=0; n<26; n++) for(int m=0; m<2; m++)  ysforms[n][m] = "";
	ysforms[0][0]="SL_001"; ysforms[0][1] = "외출계";
	ysforms[1][0]="SL_002"; ysforms[1][1] = "출장신청서";
	ysforms[2][0]="SL_003"; ysforms[2][1] = "휴(공)가원";
	ysforms[3][0]="SL_004"; ysforms[3][1] = "교육일지";
	ysforms[4][0]="SL_005"; ysforms[4][1] = "배차신청서";
	ysforms[5][0]="SL_006"; ysforms[5][1] = "프로젝트용 소모품신청서";
	ysforms[6][0]="SL_007"; ysforms[6][1] = "보고서";
	ysforms[7][0]="SL_008"; ysforms[7][1] = "출장보고서";
	ysforms[8][0]="SL_009"; ysforms[8][1] = "연장근무 신청서";
	ysforms[9][0]="SL_010"; ysforms[9][1] = "구인의뢰서";
	ysforms[10][0]="SL_011"; ysforms[10][1] = "사직원";
	ysforms[11][0]="SL_012"; ysforms[11][1] = "지출품의서";
	ysforms[12][0]="SL_013"; ysforms[12][1] = "고정/재고자산 이동보고서";
	ysforms[13][0]="SL_014"; ysforms[13][1] = "입고표(검수확인서)";
	ysforms[14][0]="SL_015"; ysforms[14][1] = "출고요청서(표)";
	ysforms[15][0]="SL_016"; ysforms[15][1] = "기안서";
	ysforms[16][0]="SL_017"; ysforms[16][1] = "명함신청서";
	ysforms[17][0]="SL_018"; ysforms[17][1] = "사유서";
	ysforms[18][0]="SL_019"; ysforms[18][1] = "협조전";
	ysforms[19][0]="SL_020"; ysforms[19][1] = "전체공지사항";
	ysforms[20][0]="SL_021"; ysforms[20][1] = "사내수발송공문";
	ysforms[21][0]="SL_022"; ysforms[21][1] = "사외발송공문";
	ysforms[22][0]="SL_023"; ysforms[22][1] = "부서운영비명세서";
	ysforms[23][0]="SL_024"; ysforms[23][1] = "업무추진경비명세서";
	ysforms[24][0]="SL_025"; ysforms[24][1] = "차량운행관리비명세서";
	ysforms[25][0]="SL_026"; ysforms[25][1] = "출장전도금정산서";
	int fcnt = 26;		//양식 총갯수
		

	/*****************************************************
	// 값 Setting하기
	*****************************************************/
	String no = request.getParameter("no");             if(no == null) no ="";
	String req = request.getParameter("req");             if(req == null) req ="";
	String ys_name = request.getParameter("ys_name");	  if(ys_name == null) ys_name = "";
	String ys_value = request.getParameter("ys_value");   if(ys_value == null) ys_value = "";
	String ys_option = request.getParameter("ys_option"+no); if(ys_option == null) ys_option = "";

	if(req.equals("ADD")) {
		//사번검사하기
		String[] cColumns = {"name"};
		bean.setTable("USER_TABLE");
		bean.setColumns(cColumns);
		bean.setSearch("id",ys_value); 
		bean.init_unique();
		if(bean.isEmpty()) Message = "입력된 사번이 없습니다.";

		//중복입력 검사하기
		String[] yColumns = {"ys_name","ys_value"};
		bean.setTable("YANGSIC_ENV");
		bean.setColumns(yColumns);
		bean.setClear();
		bean.setSearch("ys_name",ys_name,"ys_value",ys_value); 
		bean.init_unique();
		if(!bean.isEmpty()) Message = "이미 등록된 사번입니다.";

		//등록하기
		if(Message.length() == 0) {
			query  ="insert into yangsic_env(ys_name,ys_value,ys_option) values('"+ys_name+"','";
			query +=ys_value+"','"+ys_option+"')";
			bean.execute(query);
		}
	}
	else if(req.equals("UPDATE")) {
		query="update yangsic_env set ys_option='"+ys_option+"' where ys_name='"+ys_name+"' and ys_value='"+ys_value+"'";
		bean.execute(query);
	}
	else if(req.equals("DELETE")) {
		query = "delete from yangsic_env where ys_name='"+ys_name+"' and ys_value='"+ys_value+"'";
		bean.execute(query);
	}
	
	/*****************************************************
	// 데이터 쿼리하기
	*****************************************************/
	//1.양식내용 가져오기
	String[] itemColumns = {"ys_name","ys_value","ys_option"};
	query = "where ys_name like 'SL_%' order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int cnt = bean.getTotalCount();

	//양식번호,양식권한사번,양식권한(편집/보기),권한자이름,양식이름 배열만들기
	String[][] data = new String[cnt][5];		
	for(int n=0; n<cnt; n++) for(int m=0; m<5; m++) data[n][m] = "";

	int i=0;	//data
	while(bean.isAll()){
		data[i][0]=bean.getData("ys_name");		//양식코드
		for(int n=0; n<fcnt; n++)				//양식이름
			if(data[i][0].equals(ysforms[n][0])) data[i][4] = ysforms[n][1];
		data[i][1] = bean.getData("ys_value");	//권한사번
		data[i][2] = bean.getData("ys_option");	//권한구분
		i++;		
	}

	//2.사번에 따른 이름 가져오기
	String[] Columns = {"name"};
	bean.setTable("USER_TABLE");
	bean.setColumns(Columns);
	bean.setClear();
	for(int k=0; k<i; k++) {
		bean.setSearch("id",data[k][1]); 
		bean.init_unique();
		if(bean.isAll()) data[k][3] = bean.getData("name");
	}	
%>


<HTML>
<HEAD>
<LINK href="../css/style.css" rel=stylesheet>
<title>양식관리 사용자권한 환경SETTING</title>
</HEAD>
<Script language = "Javascript">
 <!-- 
//메시지 전달하기
var msg = '<%=Message%>';
if(msg.length != 0) alert(msg);

//수정하기
function userUpdate(a,b,c)
{
	d = confirm("수정하시겠습니까?");
	if(d == false) return;

	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='UPDATE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.no.value=c;
	document.sForm.submit();
}
//삭제하기
function userDelete(a,b,c)
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;

	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='DELETE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.no.value=c;
	document.sForm.submit();
}
//등록하기
function userAdd()
{
	num = sForm.ys_code.selectedIndex;
	a = sForm.ys_code.options[num].value;
	b = document.sForm.ys_id.value;
	nm = sForm.ys_opt.length;
	for(i=0; i<nm; i++)
		if(sForm.ys_opt[i].checked) c = document.sForm.ys_opt[i].value;
	
	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='ADD';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.ys_option.value=c;
	document.sForm.submit();
}
-->
</Script>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="sForm" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 양식별 사용자 권한설정</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='100%'> 
				<!--//입력창 만들기-->
				&nbsp;
				양식종류 <select name='ys_code'>
				<%			for(int n=0; n<fcnt; n++) {%>
								<option value='"+ysforms[n][0]+"'><%=ysforms[n][1]%>
						<%	}%>
						</select>
				&nbsp;
				사용자 사번 <input  class='inp' type='text' size=10 name='ys_id'>
				&nbsp;
				사용자 권한 <input type='radio' name='ys_opt' value='0' >view
							<input type='radio' name='ys_opt' value='1' >편집
				&nbsp;		<a href="javascript:userAdd()"><img src='../images/bt_save.gif' border='0' align=absbottom></a></TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
					
 <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
		<!--<TR height=23><TD colspan=12><p>
		&nbsp;&nbsp;&nbsp;[사용자 등록]<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - 각 양식에 해당되는 모듈 사용자를 등록합니다.<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - 사용자별 모듈 사용권한을 등록합니다.<br><p>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></TD></TR>-->
		<!--<TR height=23><TD colspan=12><IMG src='../images/gw_yang_user.gif' align='absmiddle'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>-->
		<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>NO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>양식명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>이름</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>권한</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>비고</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
  
	<%	//화면 출력하기
		for(int n=0,m=1; n<i; n++,m++) {%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
				<TD align=middle height='24' class='list_bg'><%=m%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height='24' class='list_bg'>&nbsp;<%=data[n][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'><%=data[n][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'><%=data[n][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'>

		<%  if(data[n][2].equals("1")){%>
				<input type='radio' name="ys_option'<%=n%>'" value='0' >view
				<input type='radio' name="ys_option'<%=n%>'" value='1' checked >편집
		<%  } else {%>
				<input type='radio' name="ys_option'<%=n%>'" value='0' checked >view
				<input type='radio' name="ys_option'<%=n%>'" value='1' >편집
        <%  }%>
				</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'>
				<a href="javascript:userUpdate('<%=data[n][0]%>','<%=data[n][1]%>','<%=n%>')"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a> <a href="javascript:userDelete('<%=data[n][0]%>','<%=data[n][1]%>','<%=n%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a></TD>
			</TR>
			<TR><TD colSpan=16 background="../images/dot_line.gif"></TD></TR>
		<%	}%>
	  
	  </TBODY></TABLE>
		
	  <input type='hidden' name='ys_name'>
	  <input type='hidden' name='ys_value'>
	  <input type='hidden' name='ys_option'>
	  <input type='hidden' name='no'>
	  <input type='hidden' name='req'>
	  
</TBODY></TABLE>
</form>
</BODY>
</HTML>
