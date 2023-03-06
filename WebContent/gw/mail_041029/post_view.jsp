<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "개인우편 보기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
	import="javax.activation.*"
	import="com.anbtech.file.textFileReader"
%>
<%@	page import="com.anbtech.text.Hanguel"			%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";		//메시지 전달 변수  

	String id = "";			//접속자 id
	String name="";			//접속자 이름

	//화면출력변수
	String wName = "";		//발신자 이름
	String wDivision = "";	//발신자 부서명
	String wTel = "";		//발신자 전화번호

	String psub = "";		//제목
	String pwde = "";		//날자
	String pwrn = "";		//작성자 이름
	String prec = "";		//수신자 명단
	String state= "";		//email 인지 자체개인우편인지 판단
	String psel = "";		//배달선택
	String path = "";		//첨부파일 path
	String Bpath = "";		//본문파일 path

	String pfie = "";		//본문 파일명 (window)
	String pcon = "";		//본문내용 (unix)

	String apath = "";		//첨부파일 Path
	String pad1o = "";		//첨부된 파일명1 원래이름	
	String pad1f = "";		//첨부된 파일명1
	String file1_size="";	//파일크기

	String pad2o = "";		//첨부된 파일명2 원래이름	
	String pad2f = "";		//첨부된 파일명2
	String file2_size="";	//파일크기

	String pad3o = "";		//첨부된 파일명3 원래이름	
	String pad3f = "";		//첨부된 파일명3	
	String file3_size="";	//파일크기

	//전달받은 변수 (from post_main.jsp)
	String pid = "";		//관리편지 번호
	String TITLE = "";		//읽을 편지함 종류(한글표현)
	String TIT = "";		//읽을 편지함 종류(영문표현:넘겨받은값)

	//배달선택종류에 따라 처리하기
	String cfm = "";		//수신확인 요청
	String sec = "";		//비밀편지 요청
	String rsp = "";		//회신요망 요청

stop : {
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//파일읽기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id

	//접속자 이름알아내기
	String[] naColumn = {"id","name","rank","office_tel"};
	bean.setTable("user_table");			
	bean.setColumns(naColumn);
	bean.setOrder("id ASC");	
	bean.setClear();
	bean.setSearch("id",id);				
	bean.init_unique();	
	if(bean.isAll()) name = bean.getData("name");

	// 변수 Clear 시키기	
	wName=wDivision=wTel=Message="";
	psub=pwde=pwrn=prec=state=psel=path=pfie=pcon="";
	apath=pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";
	pid=TITLE=cfm=sec=rsp="";

	/*********************************************************************
	 	전달받은 변수 읽기 (from post_main.jsp)
	*********************************************************************/
	String rPID = request.getParameter("PID");
	if(rPID != null) pid = rPID;

	TIT = request.getParameter("Title");
	if(TIT != null) {
		if(TIT.equals("REC_ING")) TITLE = "받은편지";
		else if(TIT.equals("SND_ING")) TITLE = "보낸편지";
		else if(TIT.equals("TMP_ING")) TITLE = "보낼편지";
		else if(TIT.equals("WST_ING")) TITLE = "휴지통";
	}

	/**********************************************************************
		발신자가 쓴 정보
	**********************************************************************/
	String[] pidColumn = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","post_state","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
	if(TITLE.equals("휴지통"))
		bean.setTable("POST_WASTE");
	else 
		bean.setTable("POST_MASTER");
	bean.setColumns(pidColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("pid",pid);			
	bean.init();

	String write_id = "";
	if(bean.isEmpty()) Message="NO_DATA";
	while(bean.isAll()) {	
		psub = bean.getData("post_subj");			//제목
		pwde = bean.getData("write_date");			//날자
		pwrn = bean.getData("writer_name");			//작성자 이름

		prec = bean.getData("post_receiver");		//수신자 명단
		state = bean.getData("post_state");			//email 인지 개인우편인지 판단
		psel = bean.getData("post_select");			//배달선택

		String Path = bean.getData("bon_path");		//본문path
		if(Path == null) path = crp + "/";
		else {
			path = crp + Path.substring(0,Path.lastIndexOf('/'))+"/addfile";	//첨부파일 path
			Bpath = crp + Path;													//본문파일 path
		}

		pfie = bean.getData("bon_file");			//본문 파일명 (window)
		if(pfie == null) pfie = "";

		pad1o = bean.getData("add_1_original");		//첨부된 파일명1 원래이름
		pad1f = bean.getData("add_1_file");			//첨부된 파일명1

		pad2o = bean.getData("add_2_original");		//첨부된 파일명2 원래이름
		pad2f = bean.getData("add_2_file");			//첨부된 파일명2

		pad3o = bean.getData("add_3_original");		//첨부된 파일명3 원래이름
		pad3f = bean.getData("add_3_file");			//첨부된 파일명3			

		//첨부파일 path구하기
		int lslash = Path.lastIndexOf('/');
		if(lslash == -1) lslash = 0;
		apath = upload_path + crp + Path.substring(0,lslash) + "/addfile/";

		//첨부파일 파일크기 구하기
		String fpath1 = apath + pad1f;
		File fn1 = new File(fpath1);
		file1_size = Long.toString(fn1.length());	//첨부1 파일크기

		String fpath2 = apath + pad2f;
		File fn2 = new File(fpath2);
		file2_size = Long.toString(fn2.length());	//첨부2 파일크기

		String fpath3 = apath + pad3f;
		File fn3 = new File(fpath3);
		file3_size = Long.toString(fn3.length());	//첨부3 파일크기

		write_id = bean.getData("writer_id");		//발신자 id
	} //while

	/***********************************************************************
		발신자 정보 LIST UP
	***********************************************************************/
	String[] idColumn = {"a.id","a.name","b.ac_name","a.office_tel"};
	String q = "where a.ac_id = b.ac_id and id ='" + write_id + "'";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setSearchWrite(q);				//id로 등록된 내용만을 선택할 수 있다.
	bean.init_write();

	if(bean.isEmpty()) wName = pwrn;			//email인경우 이름
	while(bean.isAll()) {
		wName = bean.getData("name");			//발신자명
		wDivision = bean.getData("ac_name");	//발신자 부서명
		wTel = bean.getData("office_tel");	//발신자 전화번호
	} //while

	/***********************************************************************
		발신자 배달확인 요청 분석 (예, cFm,sEc,rsP,)
	***********************************************************************/
	cfm=sec=rsp="";
	if(psel != null) {
		if(psel.indexOf('F') > 0) cfm="CFM";		//수신확인 요청
		if(psel.indexOf('E') > 0) sec="SEC";		//비밀확인 요청
		if(psel.indexOf('P') > 0) rsp="RSP";		//회신확인 요청
	}
 
	/**********************************************************************
		수신확인요청인 경우 수신확인여부 알려주기 (발신자에게만 표기해줌)
	***********************************************************************/
	if(cfm.equals("CFM") && write_id.equals(id)) {
		//out.println("수신인 : " + prec + "<br>");
		//수신인수 파악하여 구분자(;)을 이용하여 배열에 담기
		String[] rec_name = new String[prec.length()];	//수신자 이름
		String[] rec_id = new String[prec.length()];	//수신자 id

		int rec_cnt = 0;				//배열번호
		int rec_str = 0;				//구분자(;) 위치 (복사의 시작점)
		for(int ri=0; ri<prec.length(); ri++){
			if(prec.charAt(ri) == ';') {
				String full_rec = prec.substring(rec_str,ri);				//구분자 Full이름 (사번/이름)
				int sidx = full_rec.indexOf('/'); 							//구분자 위치 찾기(/)
				if(sidx > 0) {
					rec_id[rec_cnt] = full_rec.substring(0,sidx);			//사번만
					rec_name[rec_cnt] = full_rec.substring(sidx+1,full_rec.length());		//이름만
				}
				rec_cnt++;									//배열번호증가
				rec_str = ri+3;								//시작점 (\r\n으로 2더함 + ;(1))
			}
		}


		//수신을 이용 보았는지 여부를 알아보고, 개별수신함에 "수신확인" 표기하기
		prec = "";	//전 수신인을 클리어시키고 하단에서 다시 조합한다.
		String[] seeColumn = {"pid","post_receiver","isopen","open_date"};
		bean.setTable("POST_LETTER");			
		bean.setColumns(seeColumn);
		bean.setOrder("pid ASC");	
		bean.setClear();
		for(int rj=0; rj < rec_cnt; rj++){
			bean.setSearch("pid",pid,"post_receiver",rec_id[rj]);				
			bean.init_unique();
			String isRead = "0";
			if(bean.isAll()) isRead = bean.getData("isopen");
			
			//전수신자 조합하기
			if(isRead.equals("1")) { //보았음
				prec += rec_name[rj] + "/" + rec_id[rj] + ";" + "수신함\n";	//bean.getData("open_date")
			} else {
				prec += rec_name[rj] + "/" + rec_id[rj] + ";\n";
			}	
		} //for
	} //if

	/**********************************************************************
		관련문서를 처음보는지를 확인하여 하단을 처리함
	***********************************************************************/
	String[] letColumn = {"pid","post_receiver","isopen"};
	bean.setTable("POST_LETTER");			
	bean.setColumns(letColumn);
	bean.setOrder("pid ASC");	
	bean.setClear();
	bean.setSearch("pid",pid,"post_receiver",id);				
	bean.init_unique();
	
	String ISOPEN = "";
	if(bean.isAll()) ISOPEN = bean.getData("isopen");
	if(ISOPEN == null) ISOPEN = "1";

	if(ISOPEN.equals("0")) {		
		//---------------------------------------------
		//보았음,open일자,삭제예정일을 알려주기
		//----------------------------------------------
		int DelM = 1;		//삭제예정일 open후 1개월
		String seeL = "update POST_LETTER set isopen='1',open_date='" + anbdt.getTime() + "',delete_date='" + anbdt.getAddMonthNoformat(DelM) + "' where pid='" + pid + "' and post_receiver='" + id + "'";
		try { bean.execute(seeL); } catch (Exception e) {}
		//out.println("seeL : " + seeL + "<br>");
	} //if

} //stop

%>

<HTML><HEAD><TITLE>편지읽기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_r.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <% if(TITLE.equals("받은편지")) { %>
          <a href="Javascript:reply('<%=pid%>');"><img border="0" src="../images/bt_reply.gif" align='absmiddle'></a>
		  <% } %>
		 <a href="javascript:dclose();"><img src="../images/close.gif" border="0" align='absmiddle'></a></td></tr></tbody></table>

    <!--발송정보-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">발신자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=wName%> [전화: <%=wTel%>,  부서명:<%=wDivision%>,  작성일:<%=pwde%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">수신자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=1 cols=75 readOnly><%=prec%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=psub%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<% 
					//state.equals("email")  //email 받은것			
					//Textfile 읽기
					textFileReader text = new com.anbtech.file.textFileReader();			//파일읽기
					String buf = "";
					if(!Message.equals("NO_DATA")) {
						String Textfile = upload_path + Bpath + "/" + pfie;
						buf = text.getFileString(Textfile);
					} else buf = "데이터가 없습니다.";

					//화면출력하기
					if(state.equals("email")) {
						if(buf.indexOf("><") == -1) {
							out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//본문 파일
						} else {
							out.println("<IFRAME name=show src='"+server_path+"/upload"+Bpath+"/"+pfie+"' width=100% height=500 border=0 frameborder=0></IFRAME>"); 
						}
					} else if(state.equals("DEL")) {
						if(buf.indexOf("><") == -1) {
							out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//본문 파일
						} else {
							out.println("<IFRAME name=show src='"+server_path+"/upload"+Bpath+"/"+pfie+"' width=100% height=500 border=0 frameborder=0></IFRAME>"); 
						}
					} else {
						out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//본문 파일
					}
					
			%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<img src="../images/b-attach.gif" border="0">첨부파일1:<a href='post_downloadp.jsp?fname=<%=pad1o%>&fsize=<%=file1_size%>&umask=<%=pad1f%>&extend=<%=path%>'><%=pad1o%></a><br>
			<img src="../images/b-attach.gif" border="0">첨부파일2:<a href='post_downloadp.jsp?fname=<%=pad2o%>&fsize=<%=file2_size%>&umask=<%=pad2f%>&extend=<%=path%>'><%=pad2o%></a><br>
			<img src="../images/b-attach.gif" border="0">첨부파일3:<a href='post_downloadp.jsp?fname=<%=pad3o%>&fsize=<%=file3_size%>&umask=<%=pad3f%>&extend=<%=path%>'><%=pad3o%></a>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:dclose();"><img src="../images/close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--
function centerWindow() 
{ 
        var sampleWidth = 700;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 650;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function dclose()
{
	opener.parent.Left.location.reload();
	self.close();
}

//Reply
function reply(a)
{
	document.location.href="post_reply.jsp?PID=" + a;
}
-->
</script>

<!-- ****************** 메시지 전달부분 ****************************** -->
<% if(Message == "NO_CONTENT") { %>
<script>
alert("내용이 빠졌습니다.")
</script>
<% Message = "" ; } %>

<% if(Message == "INSERT") { %>
<script>
alert("등록 되었습니다.")
opener.location.reload();
close()
</script>
<% Message = "" ; } %>

<% if(Message == "QUERY") { %>
<script>
alert("전송에 실패했습니다. 다시 진행하십시요.")
</script>
<% Message = "" ; } %>

<% if(Message == "REPLY") { %>
<script>
alert("발신자:<%=wName%>[<%=wDivision%>] 께서 회신을 요청하셨습니다.")
</script>
<% Message = "" ; } %>
