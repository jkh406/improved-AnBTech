<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "사외공문 이메일수신자 지정하기"		
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
	String rec_name="";				//수신자 명 (콤마로 구분)
	String rec_mail="";				//수신자 주소 (콤마로 구분)
	String subject="";				//제목
	String address="";				//발신자 주소
	String tel="";					//발신자 전화번호
	String fax="";					//발신자 팩스번호
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
		rec_name=table.getRecName();				//수신자 이름(콤마로 구분)
		rec_mail=table.getRecMail();				//수신자 주소
		subject=table.getSubject();					//제목	
		address=table.getAddress();					//발신자 회사주소
		tel=table.getTel();							//전화번호
		fax=table.getFax();							//팩스번호
		bon_path=table.getBonPath();				//본문저장 확장path
		bon_file=table.getBonFile();				//본문저장 파일명
		firm_name=table.getFirmName();				//발신부서명
		representative=table.getRepresentative();	//발신부서 대표명	
		fname=table.getFname();						//공통:파일원래명	
		sname=table.getSname();						//공통:파일저장명		
		ftype=table.getFtype();						//공통:파일확장자명	
		fsize=table.getFsize();						//공통:파일크기
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
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","code"};
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

<script language=javascript>
<!--
//이메일 보내기
function sendEmail()
{
	document.eForm.action='../servlet/OutDocumentMultiServlet';
	document.eForm.mode.value='ODS_E';	
	document.eForm.submit();
}
//닫기
function winClose()
{
	self.close();
}
-->
</script>

<HTML><HEAD><TITLE>공문메일발송</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../ods/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
		<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
		<TBODY>
			<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
			<TR>
				<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../ods/images/pop_title_mail.gif" border='0'></TD></TR>
			<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>

    <!--버튼-->
	<tr><TD align='right'>
		<TABLE cellSpacing=0 cellPadding=0 border=0>
		<TBODY>
			<TR><TD align=left height='32' style='padding-right:20px'>
				<a href="javascript:sendEmail();"><img src="../ods/images/bt_export.gif" border=0></a> <a href="javascript:winClose();"><img src="../ods/images/bt_close.gif" border=0></a>
			</TD></TR></TBODY></TABLE></TD></TR>

	<!--공문수신자-->
	<TR><TD align='center'>
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody><form name="eForm" method="post" encType="multipart/form-data" style="margin:0">
		
		<!-- <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
          <tr>
		   <td height=22 colspan="2"><img src="../ods/images/mail_receiver.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="C7C7C7"><td height=1 colspan="2"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수신회사</td>
				<td width="87%" height="25" class="bg_04"><%=receive%></td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">메일주소</td>
				<td width="87%" height="25" class="bg_04">
					<textarea rows="2" name="rec_mail" cols='70' style="border:1 solid #787878;" class='text_01'><%=rec_mail%></textarea>
				<br> * 수신인이 여러명 일 경우 콤마(,)로 구분
				</td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>
				<input type='hidden' name='mode' value=''><input type='hidden' name='id' value='<%=id%>'>
				</form>

    <!--발신정보-->
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
			<tr><td height=20 colspan="4"></td></tr>
         <!--<tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../ods/images/send_info.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">머리글</td>
				<td width="37%" height="25" class="bg_04"><%=title_name%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">꼬리글</td>
				<td width="37%" height="25" class="bg_04"><%=firm_name%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">표어</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=slogan%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">대표자명</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=representative%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">전화번호</td>
				<td width="37%" height="25" class="bg_04"><%=tel%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">팩스번호</td>
				<td width="37%" height="25" class="bg_04"><%=fax%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">회사주소</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=address%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>

    <!--공문내용-->
		<table cellspacing=0 cellpadding=2 width="94%" border=0>
		<tbody>
			<tr><td height=20 colspan="4"></td></tr>
         <!--<tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4"><img src="../ods/images/doc_info.gif" width="209" height="25" border="0"></td></tr>-->
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">문서번호</td>
				<td width="87%" height="25" class="bg_04" colspan="3"><%=doc_id%></td>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">기안자</td>
				<td width="37%" height="25" class="bg_04"><%=user_name%></td>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">기안일자</td>
				<td width="37%" height="25" class="bg_04"><%=in_date%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">참조</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=reference%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">내용</td>
				<td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="10" cols="70" style="border:1 solid #787878;" readonly><%=content%></textarea></td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="13%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
				<td width="87%" height="25" colspan="3" class="bg_04">
		   <% 
				int ary_cnt = addFile.length;	//배열의 갯수
				for(int i=0; i<ary_cnt; i++) {
					out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>");
				}
			%>
				</td></tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
			<TR>
				<TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../ods/images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
			</TR>
			<TR>
				<TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
			</TR>
			</TBODY></TABLE></td></tr></table>
</BODY>
</HTML>