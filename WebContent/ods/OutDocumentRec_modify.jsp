<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "사외공문 접수수정"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../admin/errorpage.jsp"
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
	//-----------------------------------
	//초기화 선언
	//-----------------------------------
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//날자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	FileWriteString file = new com.anbtech.file.FileWriteString();			//디렉토리 생성하기

	//-----------------------------------
	// 읽을 변수선언
	//-----------------------------------
	String id = "";					//관리번호
	String user_name="";			//기안자 이름
	String serial_no="";			//접수번호
	String doc_id="";				//문서번호
	String in_date="";				//접수일자	
	String send_date="";			//발신일자(업체가 보낸)	
	String receive="";				//수신
	String sending="";				//발신
	String sheet_cnt="";			//부수
	String subject="";				//제목	
	String bon_path="";				//본문저장 확장path
	String bon_file="";				//본문저장 파일명
	String content="";				//본문내용
	
	String fname="";				//공통:파일원래명	
	String sname="";				//공통:파일저장명		
	String ftype="";				//공통:파일확장자명	
	String fsize="";				//공통:파일크기
	String[][] addFile;				//첨부관련내용 담기
	String module_name="";			//보낼모듈종류
	String mail="";					//전자우편종류
	String mail_add="";				//전자우편주소(사번/이름;)
	int attache_cnt = 4;			//첨부파일 최대갯수 (미만)

	//-----------------------------------
	// 작성자 정보 변수선언
	//-----------------------------------
	String user_id = "";			//해당자 사번
	String user_rank = "";			//해당자 직급
	String div_id = "";				//해당자 부서명 관리코드
	String div_name = "";			//해당자 부서명
	String div_code = "";			//해당자 부서코드
	String code = "";				//작성자 부서Tree 관리코드

	/*********************************************************************
	 	공문공지 내용 읽기
	*********************************************************************/	
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();

	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		id=table.getId();							//관리번호
		user_id=table.getUserId();					//기안자 사번
		user_name=table.getUserName();				//기안자 이름
		serial_no=table.getSerialNo();				//접수번호
		doc_id=table.getDocId();					
			if(doc_id == null) doc_id = "";			//문서번호
		
		in_date=table.getInDate();					//접수일자	
		send_date=table.getSendDate();				//발신일자(업체가 보낸)
		receive=table.getReceive();;				//수신
		sending=table.getSending();					//발신
		sheet_cnt=table.getSheetCnt();				//발신 부수
		subject=table.getSubject();					//제목	
		bon_path=table.getBonPath();				//본문저장 확장path
		bon_file=table.getBonFile();				//본문저장 파일명
		
		fname=table.getFname();	if(fname==null)fname="";					//공통:파일원래명	
		sname=table.getSname();	if(sname==null)sname="";					//공통:파일저장명		
		ftype=table.getFtype();	if(ftype==null)ftype="";					//공통:파일확장자명	
		fsize=table.getFsize();	if(fsize==null)fsize="";					//공통:파일크기
		module_name=table.getModuleName();			//보낼모듈종류
	}

	//본문파일읽기
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	content = text.getFileString(full_path);

	//첨부파일읽어 배열에 담기
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//파일type 담기
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//파일크기 담기
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
		m = 0;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3].equals(".bin")) addFile[m][3] = "";
			//첨부파일에서 확장자(_1_2_3..)번호 찾기
			if(addFile[m][3].length() > 0) {
				int en = addFile[m][3].indexOf("_");
				addFile[m][4] = addFile[m][3].substring(en+1,en+2);
			} else addFile[m][4] = "0";
			m++;
		}
	}
	
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
//수신부서장 찾기
function searchChief()
{	//window.open("../ods/searchDivision.jsp?target=eForm.receive","division","width=490,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen('../ods/searchDivision.jsp?target=eForm.receive',"",'510','467');
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//수정하기
function sendModify()
{

	var f = document.eForm;

	if(f.doc_id.value == ""){
		alert("문서번호를 입력하십시오.");
		f.doc_id.focus();
		return;
	}

	if(f.receive.value == ""){
		alert("수신부서를 입력하십시오.");
		f.receive.focus();
		return;
	}

	if(f.subject.value == ""){
		alert("제목을 입력하십시오.");
		f.subject.focus();
		return;
	}


	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../servlet/OutDocumentRecMultiServlet';
	document.eForm.mode.value='ODR_modify';	
	document.eForm.submit();
}
//첨부파일 개별삭제하기
function attachDel(a)
{
	document.eForm.action='../servlet/OutDocumentRecMultiServlet';
	document.eForm.mode.value='ODR_attachD';	
	document.eForm.ext.value=a;
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
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> 사외공문 문서 수정</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=200><a href="javascript:sendModify();"><img src="../ods/images/bt_save.gif" border="0"></a> <a href="javascript:history.go(-1)"><img src="../ods/images/bt_cancel.gif" border="0"></a></TD></TR></TBODY></TABLE></TD></TR>
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">접수번호</td>
           <td width="40%" height="25" class="bg_04"><%=serial_no%></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;자</td>
           <td width="40%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">발&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;신</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='sending' value='<%=sending%>'></td>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">발송일자</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='send_date' value='<%=send_date%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">문서번호</td>
           <td width="40%" height="25" class="bg_04">
				<input size='25' type='text' name='doc_id' value='<%=doc_id%>' class='text_01'></td>
			 <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">부&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;수</td>
           <td width="40%" height="25" class="bg_04">
				<input class="box" size='25' type='text' name='sheet_cnt' value='<%=sheet_cnt%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;신</td>
           <td width="40%" height="25" class="bg_04">
				<textarea rows="3" name="receive" cols='22' readOnly style="background:#FCFCDF;border:1 solid #787878;"><%=receive%></textarea>&nbsp;<a href="Javascript:searchChief();"><img src="../ods/images/bt_search2.gif" border='0'></a>
		   <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수 령 자</td>
           <td width="40%" height="25" class="bg_04"><%=user_name%>
				<input type='hidden' name='user_id' value='<%=user_id%>'>
				<input type='hidden' name='user_name' value='<%=user_name%>'>
				<input type='hidden' name='user_rank' value='<%=user_rank%>'>
				<input type='hidden' name='div_id' value='<%=div_id%>'>
				<input type='hidden' name='div_code' value='<%=div_code%>'>
				<input type='hidden' name='code' value='<%=code%>'>
				<input type='hidden' name='div_name' value='<%=div_name%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<input size='60' type='text' name='subject' class='text_01' value='<%=subject%>'></td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		<tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">비&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;고</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<textarea rows="3" name="content" cols="93"><%=content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="90%" height="25" class="bg_04" colspan="3">
				<% 
				int ary_cnt = addFile.length;	//배열의 갯수
				String same = "0";				//첨부파일의 숫자(_1,_2,_3..)와 같지않다.
				int s_no = 0;					//같을때의 배열번호
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					for(int j=0; j<ary_cnt; j++) {
						int uno = Integer.parseInt(addFile[j][4]);
						if(no == uno) {
							same = "1";			//같다.
							s_no = j;
						}
					}
					if(same.equals("0")) { //첨부파일 확장명(_1,_2..)와 같은것이 없으면
						out.println("<input type=file name=attachfile"+no+" size=60><br>");
					} else {				//같은것이 있으면
						out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[s_no][0]+"&ftype="+addFile[s_no][1]+"&fsize="+addFile[s_no][2]+"&sname="+addFile[s_no][3]+"&extend="+bon_path+"'>"+addFile[s_no][0]+"</a>");
						out.println("<a href=javascript:attachDel('"+addFile[s_no][4]+"')>[삭제]<a><br>"); 
					}
					same = "0";					//clear
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
           <td width="10%" height="25" class="bg_03" background="../ods/images/bg-01.gif">배포방법</td>
           <td width="90%" height="25" class="bg_04">
				<%
					String btag="",mtag="";
					if(module_name.equals("게시판")) btag = "checked";
					if(module_name.equals("수신부서")) mtag = "checked";

					out.println("<input type='radio' "+mtag+" name='module_name' value='수신부서'>수신부서");
					out.println("<input type='radio' "+btag+" name='module_name' value='게시판'>게시판");	
				%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=id%>'>
<input type='hidden' name='serial_no' value='<%=serial_no%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>

<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>
</form>

<DIV id="lding" style="position:absolute;left:300px;top:150px;width:224px;height:150px;visibility:hidden;">
	<img src='../ods/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</body>
</html>
