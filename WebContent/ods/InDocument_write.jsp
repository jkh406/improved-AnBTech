<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "사내공문 수정"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.*"
	import="com.oreilly.servlet.MultipartRequest"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//날자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	FileWriteString file = new com.anbtech.file.FileWriteString();			//디렉토리 생성하기
	
	//-----------------------------------
	// 작성자 정보 변수선언
	//-----------------------------------
	String user_id = "";			//작성자 사번
	String user_name = "";			//작성자 이름
	String user_rank = "";			//작성자 직급
	String div_id = "";				//작성자 부서명 관리코드
	String div_name = "";			//작성자 부서명
	String div_code = "";			//작성자 부서코드
	String code = "";				//작성자 부서Tree 관리코드
	int attache_cnt = 4;			//첨부파일 최대갯수 (미만)

	/*********************************************************************
	 	해당자 정보 알아보기 (대상자) : 대상자 정보 [공통]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//기안자 사번
		user_name = bean.getData("name");				//기안자 명
		user_rank = bean.getData("ar_name");			//기안자 직급
		div_id = bean.getData("ac_id");					//기안자 부서명 관리코드
		div_name = bean.getData("ac_name");				//기안자 부서명 
		div_code = bean.getData("ac_code");				//기안자 부서코드
		code = bean.getData("code");					//작성자 부서Tree 관리코드
	} //while
%>

<script language=javascript>
<!--
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//수신부서장 찾기
function searchChief()
{
//	window.open("searchDivision.jsp?target=eForm.receive","division","width=490,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen("searchDivision.jsp?target=eForm.receive","receiver",'510','467','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//부서명 찾기
function searchDivision()
{
	window.open("searchDivision.jsp?target=eForm.module_add","division","width=490,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//전자우편수신자 찾기
function searchProxy()
{
	window.open("searchReceiver.jsp?target=eForm.mail_add","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//등록하기
function sendSave()
{
	//등록검사
	var rtn = '';
	rtn = document.eForm.receive.value;
	if(rtn.length == 0) { alert('수신을 지정하십시오.');  return; }
	rtn = document.eForm.subject.value;
	if(rtn.length == 0) { alert('제목을 입력하십시요.');  return; }
	rtn = document.eForm.content.value;
	if(rtn.length == 0) { alert('내용을 입력하십시요.');  return; }
	rtn = document.eForm.title_name.value;
	if(rtn.length == 0) { alert('머리글을 입력하십시요.');  return; }
	rtn = document.eForm.firm_name.value;
	if(rtn.length == 0) { alert('꼬리글을 입력하십시요.');  return; }

	document.eForm.action='../servlet/InDocumentMultiServlet';
	document.eForm.mode.value='IND_write';	
	document.eForm.submit();
}
//직접 상신하기
function app()
{
	//등록검사
	var rtn = '';
	rtn = document.eForm.receive.value;
	if(rtn.length == 0) { alert('수신을 지정하십시요.');  return; }
	rtn = document.eForm.subject.value;
	if(rtn.length == 0) { alert('제목을 입력하십시요.');  return; }
	rtn = document.eForm.content.value;
	if(rtn.length == 0) { alert('내용을 입력하십시요.');  return; }
	rtn = document.eForm.title_name.value;
	if(rtn.length == 0) { alert('머리글을 입력하십시요.');  return; }
	rtn = document.eForm.firm_name.value;
	if(rtn.length == 0) { alert('꼬리글을 입력하십시요.');  return; }

	//데이터 저장하기 후 결재상신하기
	document.eForm.action='../servlet/InDocumentMultiServlet';
	document.eForm.mode.value='IND_app';	
	document.eForm.submit();

}
-->
</script>


<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> 사내공문작성</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href='javascript:app()'><img src="../ods/images/bt_sangsin.gif" border='0'></a> <a href="javascript:sendSave();"><img src="../ods/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../ods/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
<!--	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>--></TABLE>

<!--내용-->
<form name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--정보1-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
<!--        <tr>
		   <td height="10" colspan="4"></td></tr> -->
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04">결재승인후 자동채번</td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">등록일자</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수신</td>
           <td width="37%" height="25" class="bg_04"><textarea rows="1" name="receive" cols='35' readOnly  class='text_01'></textarea>&nbsp;<a href="Javascript:searchChief();"><img src='../ods/images/bt_search.gif' border='0' align='absmiddle'></a></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">기안자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%>
					<input type='hidden' name='user_id' value='<%=user_id%>'>
					<input type='hidden' name='user_name' value='<%=user_name%>'>
					<input type='hidden' name='user_rank' value='<%=user_rank%>'>
					<input type='hidden' name='div_id' value='<%=div_id%>'>
					<input type='hidden' name='div_code' value='<%=div_code%>'>
					<input type='hidden' name='code' value='<%=code%>'>
					<input type='hidden' name='div_name' value='<%=div_name%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">참조</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input size='25' type='text' name='reference' value=''></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input size='60' type='text' name='subject' value='' class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">내용</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="10" name="content" cols="93"  class='text_01'></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
				<%
					for(int i=1; i<attache_cnt; i++) {
						out.println("&nbsp;<input type='file' name='attachfile"+i+"' size=60><br>");
					}
				%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

	<!--정보2-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
        <tr>
		   <td height="10" colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">머리글</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='title_name' value='' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">표어</td>
           <td width="37%" height="25" class="bg_04"><input  size='35' type='text' name='slogan' value=''></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">꼬리글</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='firm_name' value=''  class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">부서장명</td>
           <td width="37%" height="25" class="bg_04"><input  size='10' type='text' name='representative' value=''></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=bean.getID()%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='sending' value='<%=div_name%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
<input type='hidden' name='module_name' value='부서수신'>
<input type='hidden' name='module_add' value=''>
<input type='hidden' name='mail' value='전자우편'>
<input type='hidden' name='mail_add' value=''>
</form>

</body>
</html>
