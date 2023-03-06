<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "문서관리 구분자"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<%@	page import="com.anbtech.util.normalFormat"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	/*****************************************************
	//	변수 SETTING
	//****************************************************/
	String query = "";							//query문장 만들기
	
	/*****************************************************
	// 값 Setting하기 (수정/삭제/등록)
	*****************************************************/
	String no = request.getParameter("no");             if(no == null) no ="";
	String req = request.getParameter("req");             if(req == null) req ="";
	String ys_name = request.getParameter("ys_name");	  if(ys_name == null) ys_name = "";
	String ys_value = Hanguel.toHanguel(request.getParameter("ys_value"));
		  if(ys_value == null) ys_value = "";
	String ac_name = Hanguel.toHanguel(request.getParameter("ac_name"+no));
		  if(ac_name == null) ac_name = "";
	String ac_alpa = Hanguel.toHanguel(request.getParameter("ac_alpa"+no));
		  if(ac_alpa == null) ac_alpa = "";

	if(req.equals("ADD")) {
		//1.계정과목 구하기
		String[] isColumns = {"ys_name","ys_value"};
		query = "where ys_name like 'DN_%' order by ys_name ASC";
		bean.setTable("YANGSIC_ENV");
		bean.setColumns(isColumns);
		bean.setSearchWrite(query); 
		bean.init_write();
		int ecnt = bean.getTotalCount();

		//계정번호,계정명 배열만들기
		String[][] edata = new String[ecnt][2];		
		for(int n=0; n<ecnt; n++) for(int m=0; m<2; m++) edata[n][m] = "";

		int ei=0;	//data
		while(bean.isAll()){
			edata[ei][0] = bean.getData("ys_name");			//계정코드
			edata[ei][1] = bean.getData("ys_value");		//계정이름
			ei++;		
		}

		//2.등록할 계정과목 구하기
		String tstr = "0";
		if(ei != 0)
			tstr = edata[ei-1][0].substring(3,6);			//마지막계정의 숫자 구하기
		
		int tint = Integer.parseInt(tstr);					//정수값으로 바꾸기	
		tint++;												//1증가하기
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");
		ys_name = "DN_" + nmf.toDigits(tint);
		
		query  ="insert into yangsic_env(ys_name,ys_value) values('"+ys_name+"','"+ys_value+"')";
		bean.execute(query);
	}
	else if(req.equals("UPDATE")) {
		query="update yangsic_env set ys_value='"+ac_name+"', ys_option='"+ac_alpa+"' where ys_name='"+ys_name+"'";
		bean.execute(query);
	}
	else if(req.equals("DELETE")) {
		query = "delete from yangsic_env where ys_name='"+ys_name+"'";
		bean.execute(query);
	}

	/*****************************************************
	// 전체 계정과목 데이터 쿼리하기 (대분류계정 제외)
	*****************************************************/
	//계정과목내용 가져오기
	String[] itemColumns = {"ys_name","ys_value","ys_option"};
	query = "where ys_name like 'DN_%' order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int cnt = bean.getTotalCount();

	//번호,양식명,양식구분코드 배열만들기
	String[][] data = new String[cnt][3];		
	for(int n=0; n<cnt; n++) for(int m=0; m<3; m++) data[n][m] = "";

	int i=0;	//data
	while(bean.isAll()){
		data[i][0] = bean.getData("ys_name");		//계정코드
		data[i][1] = bean.getData("ys_value");		//양식영문이름
		data[i][2] = bean.getData("ys_option");		//양식구분자
		i++;		
	}

%>


<HTML>
<HEAD>
<LINK href="../css/style.css" rel=stylesheet>
<title>계정코드관리</title>
</HEAD>
<style type="text/css">
<!--
.inp {border:1 dotted #D9E8F2; height:18}
-->
</style>
<Script language = "Javascript">
 <!-- 

//수정하기
function userUpdate(a,b)
{
	d = confirm("수정시 등록된 계정에 영향을 줄 수 있습니다. 계속하시겠습니까?");
	if(d == false) return;

	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='UPDATE';
	document.sForm.ys_name.value=a;
	document.sForm.no.value=b;
	document.sForm.submit();
}
//삭제하기
function userDelete(a,b)
{
	d = confirm("삭제시 등록된 계정에 영향을 줄 수 있습니다. 계속하시겠습니까?");
	if(d == false) return;

	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='DELETE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.submit();
}
//등록하기
function userAdd()
{
	b = document.sForm.ys_desc.value;
	
	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='ADD';
	document.sForm.ys_value.value=b;
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
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 계정코드관리</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<!--//입력창 만들기-->
				&nbsp;&nbsp;
				양식명 <input type='text' size=20 name='ys_desc'>
				<a href="javascript:userAdd();"><img src='../images/bt_save.gif' border='0' align=absbottom></a> </TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
					
 <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
		<!--<TR height=23><TD colspan=12><p>
		&nbsp;&nbsp;&nbsp;[양식구분자 관리 등록]<br>
			&nbsp;&nbsp;&nbsp; - 양식명(영문한글표기)은 고정등록된 내용입니다.<br>
			&nbsp;&nbsp;&nbsp; - 양식명은 DB내에서 양식구분자로 설정되어있습니다. 반드시 정해진 영문한글표기되로 사용합니다.<br>
			&nbsp;&nbsp;&nbsp; - 양식구분자는 문서번호에 부여되며 양식별 문서번호가 구분됩니다.<br>
			&nbsp;&nbsp;&nbsp; - 문서번호 표기 : 부서코드,양식구분자,년도(2),-,일련번호(3)<br>
			&nbsp;&nbsp;&nbsp; - 문서번호 표기 예) 기획관리 휴가원 : PAC03-001<br><p>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></TD></TR>
		<TR height=23><TD colspan=12><IMG src='../images/gw_yang_number.gif' align='absmiddle'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>-->
		<TR vAlign=middle height=23>
			  <!--<TD noWrap width=120 align=middle class='list_title'>대분류명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>-->
			  <TD noWrap width=150 align=middle class='list_title'>계정코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>양식명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>구분자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>비고</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
  
	
<%		//화면 출력하기%>
		
		<!--<TD class=bgsetdw1 width='25%' rowspan='<%=i%>' align=center>양식항목</TD>-->
		
<%		for(int n=0; n<i; n++) {			//각 계정과목
%>			<!--<TD><IMG height=1 width=1></TD>-->
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
			<TD height='24' class='list_bg' align=center><%=data[n][0]%>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<input size=25 type='text' name='ac_name<%=n%>' value='<%=data[n][1]%>'></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<input size=5 type='text' name='ac_alpa<%=n%>' value='<%=data[n][2]%>'></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<a href="javascript:userUpdate('<%=data[n][0]%>','<%=n%>')"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a> 
				<a href="javascript:userDelete('<%=data[n][0]%>','<%=data[n][1]%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a></TD>
			<TD height='24' class='list_bg' align=center>
			<TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%		}
		
	%>
	  <TR><TD colSpan=12 background="../images/dot_line.gif"></TD></TR>
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



	