<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "사외공문 전자결재 Include"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage = "../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dms.entity.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.file.textFileReader"
%>
<%
	//-----------------------------------
	//초기화 선언
	//-----------------------------------
	com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
	com.anbtech.date.anbDate anbdt = new anbDate();							//날자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	
	//-----------------------------------
	// 변수선언
	//-----------------------------------
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
	String read_con="";				//본문내용
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
	
	//-----------------------------------
	//	사외공문 내용 읽기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_One");
	table = new OfficialDocumentTable();
	Iterator table_iter = table_list.iterator();
	
	if(table_iter.hasNext()){
		table = (OfficialDocumentTable)table_iter.next();

		user_name=table.getUserName();				//기안자 이름
		doc_id=table.getDocId(); 
			if(doc_id == null) doc_id = "결재승인후 자동채번";			//문서번호
		slogan=table.getSlogan();					//슬로건
		title_name=table.getTitleName();			//부서 Title명
		in_date=table.getInDate();					//기안일자
			in_date = in_date.substring(0,10);
		receive=table.getReceive();;				//수신
		reference=table.getReference();				//참조
			if(reference == null) reference = "";
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
	read_con = text.getFileString(full_path);

	//첨부파일읽어 배열에 담기
	if(fname == null) fname = "";
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

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
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}	
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../ods/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">

<!--내용-->
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
           <td width="37%" height="25" class="bg_04"><%=doc_id%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">등록일자</td>
           <td width="37%" height="25" class="bg_04"><%=in_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수신</td>
           <td width="37%" height="25" class="bg_04"><%=receive%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">기안자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">참조</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=reference%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">내용</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><textarea rows="15" name="content" cols="93" readonly><%=read_con%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		   </td></tr>
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
           <td width="37%" height="25" class="bg_04"><%=title_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">표어</td>
           <td width="37%" height="25" class="bg_04"><%=slogan%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">꼬리글</td>
           <td width="37%" height="25" class="bg_04"><%=firm_name%></td>
           <td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">대표자명</td>
           <td width="37%" height="25" class="bg_04"><%=representative%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

</body>
</html>

