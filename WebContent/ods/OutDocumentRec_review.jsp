<%@ include file="../admin/configPopUp.jsp"%>
<%@ page		
	info= "사외공문 수신정보"		
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

<HTML><HEAD><TITLE>사외공문 수신정보</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../ods/images/o_send_info.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--수신정보-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height="20" colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">접수번호</td>
           <td width="30%" height="25" class="bg_04"><%=serial_no%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">접수일자</td>
           <td width="30%" height="25" class="bg_04"><%=in_date%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">발송처</td>
           <td width="30%" height="25" class="bg_04"><%=sending%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">발송일자</td>
           <td width="30%" height="25" class="bg_04"><%=send_date%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">문서번호</td>
           <td width="30%" height="25" class="bg_04"><%=doc_id%></td>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">부수</td>
           <td width="30%" height="25" class="bg_04"><%=sheet_cnt%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수신처</td>
           <td width="80%" height="25" colspan="3" class="bg_04">
				<%
				if(receive.length() > 3) {
					StringTokenizer rec = new StringTokenizer(receive,";");
					String recs = "";
					while(rec.hasMoreTokens()){
						String rec_data = rec.nextToken();
						if(rec_data.length() > 3)
							recs += rec_data.substring(rec_data.indexOf("/")+1,rec_data.length())+",";
					}
					out.println(recs.substring(0,recs.length()-1));
				}
				%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">제목</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">의견</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">첨부파일</td>
           <td width="80%" height="25" colspan="3" class="bg_04">
			<% 
				int ary_cnt = addFile.length;	//배열의 갯수
				for(int i=0; i<ary_cnt; i++) {
					out.println("&nbsp;<a href='../ods/attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>");
				}
			%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_03" background="../ods/images/bg-01.gif">수령자</td>
           <td width="80%" height="25" colspan="3" class="bg_04"><%=user_name%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr><td height="20" colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='Javascript:self.close();'><img src='../ods/images/bt_close.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>