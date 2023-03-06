<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "공지공문 수정"		
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
	String doc_id="";				//문서번호
	String slogan="";				//슬로건
	String title_name="";			//부서 Title명
	String in_date="";				//기안일자		
	String receive="";				//수신
	String reference="";			//참조
	String sending="";				//발신
	String subject="";				//제목	
	String bon_path="";				//본문저장 확장path
	String bon_file="";				//본문저장 파일명
	String content="";				//본문내용
	String firm_name="";			//발신부서명
	String representative="";		//발신부서 대표명	
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
		doc_id=table.getDocId();					
			if(doc_id == null) doc_id = "";			//문서번호
		slogan=table.getSlogan();					//슬로건
		title_name=table.getTitleName();			//부서 Title명
		in_date=table.getInDate();					//기안일자		
		receive=table.getReceive();;				//수신
		reference=table.getReference();				
			if(reference == null) reference = "";	//참조
		sending=table.getSending();					//발신
		subject=table.getSubject();					//제목	
		bon_path=table.getBonPath();				//본문저장 확장path
		bon_file=table.getBonFile();				//본문저장 파일명
		firm_name=table.getFirmName();				//발신부서명
		representative=table.getRepresentative();	//발신부서 대표명	
		fname=table.getFname();						//공통:파일원래명	
		sname=table.getSname();						//공통:파일저장명		
		ftype=table.getFtype();						//공통:파일확장자명	
		fsize=table.getFsize();						//공통:파일크기
		module_name=table.getModuleName();			//보낼모듈종류
		mail=table.getMail();						//전자우편종류
		mail_add=table.getMailAdd();				//전자우편주소(사번/이름;)
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
	String query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
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
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
<script language=javascript>
<!--
//전자우편수신자 찾기
function searchProxy(users)
{
//	window.open("../ods/searchReceiver.jsp?target=eForm.mail_add|"+users,"proxy","width=510,height=460,scrollbar=yes,toolbar=no,status=no,resizable=no");
	wopen("../ods/searchReceiver.jsp?target=eForm.mail_add|"+users,"receiver",'510','467','scrollbars=no,toolbar=no,status=no,resizable=no');
}
//수정하기
function sendModify()
{
	var receive = document.eForm.receive.value; 
	if(receive.length == 0) { alert("수신을 지정하십시오."); return; }
	var subject = document.eForm.subject.value; 
	if(subject.length == 0) { alert("제목을 입력하십시오."); return; }
	var content = document.eForm.content.value; 
	if(content.length == 0) { alert("내용을 입력하십시오."); return; }
	var title_name = document.eForm.title_name.value; 
	if(title_name.length == 0) { alert("머리글을 입력하십시오."); return; }
	var firm_name = document.eForm.firm_name.value; 
	if(firm_name.length == 0) { alert("꼬리글을 입력하십시오."); return; }
	var representative = document.eForm.representative.value; 
	if(representative.length == 0) { alert("부서장명을 입력하십시오."); return; }
	
	var om = document.eForm.mail.checked; 
	var mm = document.eForm.module_name.checked; 
	var mail_add  = document.eForm.mail_add.value;
	if((om == false) && (mm == false)) { alert("공지방법을 입력하십시오."); return; }
	if((om == true) && (mail_add == 0)) { alert("전자우편 수신자를 입력해 주십시오."); return;}
	
	document.eForm.action='../servlet/OfficialDocumentMultiServlet';
	document.eForm.mode.value='OFD_modify';	
	document.eForm.submit();
}
//첨부파일 개별삭제하기
function attachDel(a)
{
	if(confirm("삭제 하시겠습니까?")) {
	document.eForm.action='../servlet/OfficialDocumentMultiServlet';
	document.eForm.mode.value='OFD_attachD';	
	document.eForm.ext.value=a;
	document.eForm.submit();
	} else {
		return;
	}
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
</HEAD>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ods/images/blet.gif"> 공지공문수정</TD></TR></TBODY>
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
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04">결재승인후 자동채번</td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수정일자</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getDate()%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수신</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='receive' value='<%=receive%>' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">기안자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%>
					<input type='hidden' name='user_id' value='<%=user_id%>'>
					<input type='hidden' name='user_name' value='<%=user_name%>'>
					<input type='hidden' name='user_rank' value='<%=user_rank%>'>
					<input type='hidden' name='div_id' value='<%=div_id%>'>
					<input type='hidden' name='div_code' value='<%=div_code%>'>
					<input type='hidden' name='code' value='<%=code%>'>
					<input type='hidden' name='div_name' value='<%=div_name%>'>	   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">참조</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='25' type='text' name='reference' value='<%=reference%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><input  size='60' type='text' name='subject' value='<%=subject%>' class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">내용</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="8" name="content" cols="93"  class='text_01'><%=content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
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
						out.println("&nbsp;<input type=file name=attachfile"+no+" size=60><br>");
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
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">머리글</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='title_name' value='<%=title_name%>' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">표어</td>
           <td width="37%" height="25" class="bg_04"><input  size='35' type='text' name='slogan' value='<%=slogan%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">꼬리글</td>
           <td width="37%" height="25" class="bg_04"><input  size='25' type='text' name='firm_name' value='<%=firm_name%>' class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">부서장명</td>
           <td width="37%" height="25" class="bg_04"><input  size='10' type='text' name='representative' value='<%=representative%>' class='text_01'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">공지방법</td>
           <td width="37%" height="25" class="bg_04">
		   <%
					String btag="",mtag="";
					if(module_name.equals("게시판")) btag = "checked";
					if(mail.equals("전자우편")) mtag = "checked";
				
					out.println("<input type='checkbox' "+btag+" name='module_name' value='게시판'>게시판");	
					out.println("<input type='checkbox' "+mtag+" name='mail' value='전자우편'>전자우편");	
			%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">우편수신</td>
           <td width="37%" height="25" class="bg_04"><textarea rows="1" name="mail_add" cols='30' readOnly style="background:#FFFFFF;border:1 solid #787878;"><%=mail_add%></textarea>&nbsp;<a href="Javascript:searchProxy('<%=mail_add%>');"><img src='../ods/images/bt_search.gif' border='0' align='absmiddle'></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='id' value='<%=id%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='sending' value='<%=div_name%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>

<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>
</form>

</body>
</html>

