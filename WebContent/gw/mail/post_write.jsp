<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "개인우편 작성"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage = "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"

%>
<%@	page import="com.anbtech.date.anbDate"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";		//메시지 전달 변수  

	String id = "";			//접속자 id
	String name = "";		//접속자 이름
	String division = "";	//접속자 부서명
	String tel = "";		//접속자 전화번호

	//내부처리 변수
	String LIST="";			//수신인명단
	String RES="";			//발송/미발송 받기
	String subject="";		//문서제목
	String content="";		//본문
	String bPath = "";		//본문저장 path
	String path = "";		//본문path 확장path 포함(crp)
	String bFile = "";		//본문파일명

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

	String SMSG = "";		//배달선택사항 다시저장하기

	//전달받은 pid (from post_main.jsp)
	String rpid ="";		//전달받은 pid로 전송/미전송시 해당내용 삭제키 위해

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text문자 읽기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id
	
	String[] idColumn = {"a.id","a.name","a.office_tel","b.ac_name"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//접속자 명
		division = bean.getData("ac_name");		//접속자 부서명
		tel = bean.getData("office_tel");			//접속자 전화번호
	} //while

	/*********************************************************************
	 	보낼편지 전달받기 (from post_main.jsp)
	*********************************************************************/
	String mPID = request.getParameter("PID");

	//pid을 이용하여 관련내용을 읽는다.
	if(mPID != null) {
		//초기화
		subject=content=LIST=rpid=SMSG=""; 
		pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";

		//전송/미전송시 해당내용 삭제함
		rpid = mPID;

		//해당내용읽기
		String[] mCls = {"pid","post_subj","post_receiver","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
		bean.setTable("POST_MASTER");			
		bean.setColumns(mCls);	
		bean.setOrder("pid ASC");	
		bean.setClear();
		bean.setSearch("pid",mPID);			
		bean.init_unique();	

		bPath = "";
		bFile = "";
		while(bean.isAll()) {
			subject = bean.getData("post_subj");
			LIST = bean.getData("post_receiver");

			String psel = bean.getData("post_select");
			if((psel != null) && (psel.length() > 0)) {
				SMSG="";
				if(psel.indexOf('F') > 0) SMSG +="CFM,";		//수신확인 요청
				if(psel.indexOf('E') > 0) SMSG +="SEC,";		//비밀확인 요청
				if(psel.indexOf('P') > 0) SMSG +="RSP,";		//회신확인 요청	
			}

			bPath = bean.getData("bon_path");
			if(bPath == null) path = crp + "/";
			else 
				path = crp + bPath.substring(0,bPath.lastIndexOf('/'))+"/addfile";

			bFile = bean.getData("bon_file");

			pad1o = bean.getData("add_1_original");				//첨부된 파일명1 원래이름
			pad1f = bean.getData("add_1_file");					//첨부된 파일명1
			pad2o = bean.getData("add_2_original");				//첨부된 파일명2 원래이름
			pad2f = bean.getData("add_2_file");					//첨부된 파일명2
			pad3o = bean.getData("add_3_original");				//첨부된 파일명3 원래이름
			pad3f = bean.getData("add_3_file");					//첨부된 파일명3			

			//첨부파일 path구하기
			int lslash = bPath.lastIndexOf('/');
			if(lslash == -1) lslash = 0;
			apath = upload_path + crp + bPath.substring(0,lslash) + "/addfile/";	//첨부파일 Path

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
		} //while

		//Textfile 읽기
		String Textfile = upload_path + crp + bPath + "/" + bFile;
		try { content = Rtext.getFileString(Textfile);	   		//Text file Read 
		} catch (Exception e) { }		
	} //if

	/*********************************************************************
		보낼편지로 부터 다시읽은 첨부파일을 삭제하고자 할때
	*********************************************************************/
	String del_file1 = request.getParameter("file1");			//첨부파일1을 삭제할경우
	String del_file2 = request.getParameter("file2");			//첨부파일2을 삭제할경우	
	String del_file3 = request.getParameter("file3");			//첨부파일3을 삭제할경우	
	
	if(del_file1 != null) {
		//DB update
		String up_or1 = "update POST_MASTER set add_1_original='',";
			  up_or1 += "add_1_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or1); } catch (Exception e) { }
		//첨부파일 삭제
		String delFile = apath + pad1f;			//첨부파일명
		Rtext.delFilename(delFile);								//첨부파일 삭제

		//첨부내용 Clear
		pad1o=pad1f="";
	} else if(del_file2 != null) {
		//DB update
		String up_or2 = "update POST_MASTER set add_2_original='',";
			  up_or2 += "add_2_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or2); } catch (Exception e) { }

		//첨부파일 삭제
		String delFile = apath + pad2f;			//첨부파일명
		Rtext.delFilename(delFile);								//첨부파일 삭제

		//첨부내용 Clear
		pad2o=pad2f="";
	} else if(del_file3 != null) {
		//DB update
		String up_or3 = "update POST_MASTER set add_3_original='',";
			  up_or3 += "add_3_file='' where pid = '" + rpid + "'";
		try { bean.execute(up_or3);} catch (Exception e) { }

		//첨부파일 삭제
		String delFile = apath + pad3f;			//첨부파일명
		Rtext.delFilename(delFile);								//첨부파일 삭제

		//첨부내용 Clear
		pad3o=pad3f="";
	}

%>

<HTML><HEAD><TITLE>새편지작성</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <a href="Javascript:postReceiver();"><img border="0" src="../images/bt_sel_receiver.gif" align="absmiddle"></a>
		  <a href="Javascript:postSend();"><img border="0" src="../images/bt_export.gif" align="absmiddle"></a>
          <a href="Javascript:postTemp();"><img border="0" src="../images/bt_save_tmp.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--발송정보-->
	<form action="post_write.jsp" method="post" name="sForm" encType="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">작성자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name%> [전화: <%=tel%>,  부서명:<%=division%>,  작성일:<%=bean.getTime()%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">수신자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75 readOnly class='text_01'><%=LIST%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="<%=subject%>" class='text_01'></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">선택사항</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<input type="checkbox" name="ReturnReceipt" value="CFM">수신확인&nbsp;
			<input type="checkbox" name="SecretSetup" value="SEC">비밀편지&nbsp;
			<input type="checkbox" name="ReplySetup" value="RSP">긴급편지
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="CONTENT" rows=22 cols=75 class='text_01'><%=content%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
					<img src="../images/b-attach.gif" border="0">첨부1
						<% if(pad1f.length() == 0) { %>
								<input type="file" name="UP_FILE1" size=50>
						<% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad1o%>&fsize=<%=file1_size%>&umask=<%=pad1f%>&extend=<%=path%>'><%=pad1o%></a>
								<a href=javascript:addFile1_Delete('<%=apath%><%=pad1f%>')>[삭제]</a>
						<% } %>
					<br><img src="../images/b-attach.gif" border="0">첨부2
					    <% if(pad2f.length() == 0) { %>
								<input type="file" name="UP_FILE2" size=50>
					    <% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad2o%>&fsize=<%=file2_size%>&umask=<%=pad2f%>&extend=<%=path%>'><%=pad2o%></a>
								<a href=javascript:addFile2_Delete('<%=apath%><%=pad2f%>')>[삭제]</a>
					    <% } %>
					<br><img src="../images/b-attach.gif" border="0">첨부3
					    <% if(pad3f.length() == 0) { %>
								<input type="file" name="UP_FILE3" size=50>
					    <% } else { %>
								<a href='post_downloadp.jsp?fname=<%=pad3o%>&fsize=<%=file3_size%>&umask=<%=pad3f%>&extend=<%=path%>'><%=pad3o%></a>
								<a href=javascript:addFile3_Delete('<%=apath%><%=pad3f%>')>[삭제] </a>
					    <% } %>  
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
<input type="hidden" name="pid" value='<%=mPID%>'>
<input type="hidden" name="res">
</form>

<% //파일삭제을 스크립트로 처리하기 위해 작성 %>
<form name=d1Form method="post" style="margin:0">
<input type="hidden" name="file1" value='<%=pad1f%>'>
</form>

<form name=d2Form method="post" style="margin:0">
<input type="hidden" name="file2" value='<%=pad2f%>'>
</form>

<form name=d3Form method="post" style="margin:0">
<input type="hidden" name="file3" value='<%=pad3f%>'>
</form>

<DIV id="lding" style="position:absolute;left:150px;top:370px;width:450px;height:200px;visibility:hidden;">
	<TABLE width="450" border="0" cellspacing=1 cellpadding=1 bgcolor="white">
	<TR><TD height="35" align="center" valign="middle"><font color='blue'>
		<marquee behavior='alternate' width='80%' scrollamount=1>
		데이터 처리중입니다. 잠시만 기다려 주십시오.
		</marquee></font>
	</TD> 
	</TR>
	</TABLE>
</DIV>

</BODY></HTML>

<script>
<!--
//윈도우창 초기화
function setFocus() 
{
	defaultStatus = "새편지쓰기";			//윈도우 하단 출력메시지
	window.resizeTo(800,500);			//윈도우 크기조절	
	window.moveTo(200,200);				//윈도우 출력위치 조절	
	document.sForm.SUBJECT.focus();
}

//새편지작성 닫기
function postClose()
{
	self.close();
}

//편지발송
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("제목을 입력하십시요.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("내용을 입력하십시요.");	return;	}
	if(sForm.rec_name.value == ""){	alert("수신인을 지정하십시요.");return;	}

	var c = confirm("작성하신 내용으로 발송하시겠습니까?");
	if(c){
		document.all['lding'].style.visibility="visible";
		document.sForm.action="post_writeSave.jsp";
		document.sForm.res.value="SND";
		document.onmousedown=dbclick;
		document.sForm.submit();
	}
}

//미발송 저장하기
function postTemp()
{
	if(sForm.SUBJECT.value == ""){	alert("제목을 입력하십시요.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("내용을 입력하십시요.");	return;	}

	if(confirm('저장 하시겠습니까?') == false) {alert("저장하지 않습니다."); self.close()};
	document.all['lding'].style.visibility="visible";
	document.sForm.action="post_writeSave.jsp";
	document.sForm.res.value="TMP";
	document.onmousedown=dbclick;
	document.sForm.submit();
	
}

//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//수신인
function postReceiver()
{
	receivers = document.sForm.rec_name.value;
	wopen("post_Share.jsp?Title=Search&Rec="+receivers+"&target=sForm.rec_name","post_rSel","510","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//첨부파일1 삭제
function addFile1_Delete(file_name)
{
	var file_add = file_name;
	document.d1Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d1Form.submit();
}
//첨부파일2 삭제
function addFile2_Delete(file_name)
{
	var file_add = file_name;
	document.d2Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d2Form.submit();
}
//첨부파일3 삭제
function addFile3_Delete(file_name)
{
	var file_add = file_name;
	document.d3Form.action="post_write.jsp?PID=<%=rpid%>";
	document.d3Form.submit();
}

//실행버튼처리방지
function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

-->
</script>

