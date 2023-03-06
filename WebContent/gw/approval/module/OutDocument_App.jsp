<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "사외공문 결재상신"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	
	String writer_id = "";			//작성자 사번
	String writer_name = "";		//작성자 이름

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	/*********************************************************************
	 	전달변수
	*********************************************************************/	
	String line = request.getParameter("doc_app_line"); if(line == null) line = "";	//결재선
	String id = request.getParameter("id");				if(id == null) id = "";		//공지공문 관리번호

	/*********************************************************************
	 	공지정보의 필요정보 찾기
	*********************************************************************/	
	String slogan = "";				//표어
	String title_name="";			//머리글
	String address="";				//주소
	String tel="";					//TEL
	String fax="";					//FAX
	String subject = "";			//제목
	String attache_file = "";		//첨부파일
	String attache_cnt = "";		//첨부파일갯수
	String[] odColumn = {"slogan","title_name","subject","address","tel","fax","sname"};
	bean.setTable("OutDocument_send");			
	bean.setColumns(odColumn);
	query = "where id ='"+id+"'";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		slogan = bean.getData("slogan");				//표어		
		title_name = bean.getData("title_name");		//머리글
		subject = bean.getData("subject");				//제목
		address = bean.getData("address");				//주소
		tel = bean.getData("tel");						//TEL
		fax = bean.getData("fax");						//FAX
		attache_file =  bean.getData("sname");			//첨부파일
	} //while

	//첨부파일 갯수 구하기
	StringTokenizer o = new StringTokenizer(attache_file,"|");		//저장파일 담기
	int no = 0;
	while(o.hasMoreTokens()) {
		String sn = o.nextToken();
		if(sn.length() > 4)	no++;
	}
	attache_cnt = Integer.toString(no);

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 사외공문</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><iframe id="iframe_main" src="../../../servlet/OutDocumentServlet?mode=ODS_V&id=<%=id%>" width="100%" height="500" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no">
</iframe></td></tr></table>

<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='사외공문 : <%=subject%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
<input type='hidden' name='doc_per' value='3'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>				<% //공지공문 종류 %>
<input type='hidden' name='id' value='<%=id%>'>			<% //공지공문 관리번호 %>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>
</body>
</html>

<script language=javascript>
<!--

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//결재선 지정지정 
function eleApprovalManagerLineSelect()
{

	wopen("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","520","467");
}

//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
	 //결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
		if(rstr.indexOf("협조") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/OutDocumentServlet';	
	document.eForm.app_mode.value='REQ';
	document.eForm.mode.value='ODS';
	document.eForm.submit();
}

//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//닫기
function winClose()
{
	window.returnValue='';
	self.close();
}
-->
</script>
