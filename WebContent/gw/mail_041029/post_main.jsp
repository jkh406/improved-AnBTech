<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "개인메일 메인"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.lang.SecurityException"
	import="java.io.UnsupportedEncodingException"
%>
<%@	page import="com.anbtech.text.Hanguel"			%>
<%@	page import="com.anbtech.date.anbDate"			%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<%@	page import="com.anbtech.file.textFileReader"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

	
<%
	//페이지당 출력수 설정
	bean.setRowNo(15);					//한페이지당 20개씩 출력

	//메시지 전달변수
	String Message="";					//메시지 전달 변수  

	String id = "";						//접속자 id
	String passwd="";					//접속자 비밀번호
	String name = "";					//접속자 이름
	String division = "";				//접속자 부서명	
	String PROCESS = "REC_ING";			//개인편지함 내용받기
	String PROCESS_NAME = "받은편지";		//개인편지함 이름쓰기
	
	//마스터DB Columns
	String[] masterColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};

	//휴지통 DB Columns
	String[] wasteColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};
	
	//LETTER DB Columns
	String[] letterColumns= {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_select","delete_date"};

	//삭제할 LINE찾기
	int count = 0;			//line 번호
	String ALL_SEL = "";		//전체선택하기 
	String ALL_CHG = "";		//전체선택하기 번갈아 바꿔주기

	//배달선택종류에 따라 처리하기
	String cfm = "";		//수신확인 요청
	String sec = "";		//비밀편지 요청
	String rsp = "";		//회신요망 요청

	String sendConfirm="";		//배달확인 조합하여 보낸편지에서 보여주기

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//파일 삭제

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = login_id; 		//접속자 login id

	String[] idColumn = {"a.id","a.passwd","a.name","b.ac_name"};
	bean.setTable("user_table a,class_table b");			//EBOM Master Table List
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	

	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	bean.isAll();
	name = bean.getData("name");			//접속자 명
	division = bean.getData("ac_name");	//접속자 부서명
	passwd = bean.getData("passwd");		//접속자 비밀번호

	/********************************************************************
		삭제할 편지 전체 선택여부알아보기
	*********************************************************************/
	ALL_SEL = request.getParameter("SEL");
	ALL_CHG = request.getParameter("CHG");	if(ALL_CHG == null) ALL_CHG = "";

	if(ALL_SEL == null) {
		 ALL_SEL = ALL_CHG = "";
	} else {
		//전체선택 누를때마다 값을 바꿔주기
		if(ALL_CHG.equals(ALL_SEL)) { 
			ALL_SEL = "";
			ALL_CHG = "";
		}
		else ALL_CHG = ALL_SEL; 	
	}

	/********************************************************************
		PROCESS 상태 구하기
	*********************************************************************/
	PROCESS = request.getParameter("ORDER");
	if(PROCESS != null) {
			if(PROCESS.equals("REC_ING")) PROCESS_NAME = "받은편지";
			else if(PROCESS.equals("SND_ING")) PROCESS_NAME = "보낸편지";
			else if(PROCESS.equals("TMP_ING")) PROCESS_NAME = "보낼편지";
			else if(PROCESS.equals("WST_ING")) PROCESS_NAME = "지운편지";
			else PROCESS_NAME = "받은편지";
	} //if
	else { 
			PROCESS = "REC_ING";
			PROCESS_NAME = "받은편지";
	}


	/********************************************************************
		선택한 편지 삭제하기
	*********************************************************************/
	String DEL_MSG = request.getParameter("DEL");
	Message="";
	if((DEL_MSG != null) && (DEL_MSG.length() > 0)) {
		for(int i=0; i<15; i++) {
			String check = "check" + i;
			String PID = request.getParameter(check);
			//삭제 또는 휴지통 
			if(PROCESS.equals("WST_ING") && (PID != null)) { //휴지통에서 --> 완전삭제하기
				//POST_LETTER 및 POST_WASTE에 해당PID가 없는경우에 한해서 
				//POST_LETTER 및 POST_MASTER table,본문,첨부파일 완전삭제하고
				//그렇지 않을 경우는 POST_WASTE table에서 해당레코드만 삭제함.(다른수신자에 link되어있음)
				String letter_flag = "NONE";	//LETTER table NONE: 삭제 불가, OK: 삭제가능	
				String master_flag = "NONE";	//MASTER table NONE: 삭제 불가, OK: 삭제가능
				String waste_flag = "NONE";		//WASTE  table NONE: 삭제 불가, OK: 삭제가능

				//조건1 POST_LETTER에 해당 PID가 있는지 없는지 검사
				String[] lCol = {"pid"};
				bean.setTable("POST_LETTER");
				bean.setColumns(lCol);
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID);
				bean.init_unique();
				if(bean.isEmpty())  letter_flag = "OK"; 
				else letter_flag = "NONE"; 

				//조건2 POST_MASTER에 해당 PID의 post_state='DEL' 있지 검사
				String[] mCol ={"pid","post_state"};
				bean.setTable("POST_MASTER");
				bean.setColumns(mCol);	
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID,"post_state","DEL");
				bean.init_unique();
				if(bean.isEmpty())  master_flag = "NONE"; 
				else master_flag = "OK";
				
				//조건3 POST_WASTE에 해당 PID가 1개(자기자신) 있는지 검사
				String[] wCol = {"pid"};
				bean.setTable("POST_WASTE");
				bean.setColumns(wCol);
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID);
				bean.init_unique();
				if(bean.isEmpty())  waste_flag = "OK"; 
				else {
					if(bean.getTotalCount() == 1)
						waste_flag = "OK";
					else waste_flag = "NONE";
				}

				//조건1,2,3 판단하여 본문및 첨부파일 삭제 실행
				if(letter_flag.equals("OK") && master_flag.equals("OK") && waste_flag.equals("OK")) {
					bean.setTable("POST_WASTE");
					bean.setColumns(wasteColumns);
					bean.setOrder("pid DESC");
					bean.setClear();
					bean.setSearch("pid",PID);
					bean.init_unique();
				
					if(bean.isAll()) {
						//본문삭제
						String bPath = bean.getData("bon_path");
						String bFile = bean.getData("bon_file");
						String bFileDEL = upload_path + crp + bPath + "/" + bFile;					
						text.delFilename(bFileDEL);			//본문삭제 
						Message="DELETE";
						//첨부파일 삭제
						int lastI = bPath.lastIndexOf('/');

						if(lastI != -1) {
							String aPath = upload_path + crp + bPath.substring(0,lastI) + "/addfile/";
							String a1_File = bean.getData("add_1_file");
							String a2_File = bean.getData("add_2_file");
							String a3_File = bean.getData("add_3_file");
							String a1_FileDEL = aPath + a1_File;
							String a2_FileDEL = aPath + a2_File;
							String a3_FileDEL = aPath + a3_File;
							text.delFilename(a1_FileDEL);				//첨부1 삭제
							text.delFilename(a2_FileDEL);				//첨부2 삭제 
							text.delFilename(a3_FileDEL);				//첨부3 삭제
						} //if
					 } //if 본문,첨부파일 완전삭제
					 //POST_MASTER 테이블에서 해당레코드 삭제하기
					 String mstDEL = "delete from POST_MASTER where pid='" + PID + "' and post_state='DEL'";
					 try { bean.execute(mstDEL); } catch (Exception e) { }
				}//조건1,2,3 판단하여 본문및 첨부파일 삭제 실행
				
				//POST_WASTE table 삭제
				String wstDEL = "delete from POST_WASTE where pid='" + PID + "' and post_receiver='" + id + "'";     //삭제하기 (post_waste)
				try { bean.execute(wstDEL); Message="DELETE"; } catch (Exception e) { }
			} else {			//편지함에서 --> 휴지통으로 보내기
				if(PROCESS.equals("REC_ING") && (PID != null)) { 	//받은편지
					String RwstIN = "insert into post_waste(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) select pid,post_subj,writer_id,writer_name,write_date,'"+id+"',isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date from post_master where pid='"+PID+"'";
					String RletDEL = "delete from POST_LETTER where pid='"+PID+"' and post_receiver='" + id + "'";	//해당라인 삭제

					//외부메일인 경우
					String RmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "' and post_state='email'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(RwstIN); 
						  bean.execute(RmstDEL);  //외부메일인 경우만 실행됨
						  bean.execute(RletDEL); Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}

				} else if(PROCESS.equals("SND_ING") && (PID != null)) {	//보낸편지 (수정후 보내고자 할때)
					String SwstIN = "insert into post_waste(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) select pid,post_subj,writer_id,writer_name,write_date,'"+id+"',isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date from post_master where pid='"+PID+"'";
					String SmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(SwstIN); 
						  bean.execute(SmstDEL); 
						  Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}	
				} else if(PROCESS.equals("TMP_ING") && (PID != null)) {	//보낼편지
					String TwstIN = "insert into POST_WASTE select * from POST_MASTER where pid = '" + PID + "'";	//모든것을그대로 담고
					String TwstUP = "update POST_WASTE set post_receiver='" + id + "' where pid = '" + PID + "'";	//삭제한 ID UPdate
					String TmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(TwstIN); 
						  bean.execute(TwstUP); 
						  bean.execute(TmstDEL); 
						  Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}
				} //if
			}
		} //for
	} //if
	/********************************************************************
		 개인편지 요청상태 알아보기
	*********************************************************************/
	//페이지 정보를 이용하여 PROCESS을 알아본다.
	//새로운 함 선택인지 다음 또는 이전 페이지 선택인지
	String pageNo = request.getParameter("pageNo");

	int ipage = 1;				
	if(pageNo!=null) ipage = Integer.parseInt(pageNo);	
	/********************************************************************
		 개인우편 내용 쿼리하기
	*********************************************************************/
	if((PROCESS == null) || (PROCESS.equals("REC_ING"))) {
		/***********************************************************************
		받은편지함
		***********************************************************************/	
		bean.setTable("POST_LETTER");
		bean.setColumns(letterColumns);
		bean.setOrder("write_date DESC");
		bean.setClear();
		bean.setSearch("post_receiver",id);
		bean.setPage(ipage);	
		bean.init_unique();
	} 
	else if (PROCESS.equals("SND_ING")) {
		/***********************************************************************
		보낸편지함
		***********************************************************************/	
		bean.setTable("POST_MASTER");
		bean.setColumns(masterColumns);
		//bean.setOrder("pid DESC");
		//bean.setClear();
		//bean.setSearch("post_state","SND","writer_id",id);
		//bean.setPage(ipage);	
		//bean.init_unique();
		String query = " where (post_state='SND' or post_state='email') and writer_id='"+id+"' ";
			query += "order by pid desc";
		bean.setSearchWrite(query);
		bean.setPage(ipage);
		bean.init_write();
	}
	else if (PROCESS.equals("TMP_ING")) {
		/***********************************************************************
		보낼편지함
		***********************************************************************/	
		bean.setTable("POST_MASTER");
		bean.setColumns(masterColumns);
		bean.setOrder("pid DESC");
		bean.setClear();
		bean.setSearch("post_state","TMP","writer_id",id);
		bean.setPage(ipage);	
		bean.init_unique();
	}
	else if (PROCESS.equals("WST_ING")) {
		/***********************************************************************
		휴지통
		***********************************************************************/	
		bean.setTable("POST_WASTE");
		bean.setColumns(wasteColumns);
		bean.setOrder("pid DESC");
		bean.setClear();
		bean.setSearch("post_receiver",id);
		bean.setPage(ipage);	
		bean.init_unique();		

	}

%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle">
			<%	
				if(PROCESS.equals("REC_ING")) out.println("받은 편지함");
				else if(PROCESS.equals("SND_ING")) out.println("보낸 편지함");
				else if(PROCESS.equals("TMP_ING")) out.println("보낼 편지함");
				else if(PROCESS.equals("WST_ING")) out.println("지운 편지함");
			%>						  
			  </TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=bean.getCurrentPage()%>/<%=bean.getLastPage()%> <img src="../images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='300'>
				<% if(request.getParameter("ORDER") == null) { 	//최초 메뉴입력시 null값임%>
						<a href="javascript:wopen('post_write.jsp','post_write','700','650');">
							<img src="../images/bt_new_letter.gif" border="0" align="absmiddle"></a>
						<a href='post_main.jsp?ORDER=REC_ING&SEL=ALL&DEL=&CHG=&pageNo=<%=ipage%>'>
							<img src="../images/bt_sel_all.gif" border="0" align="absmiddle"></a>
						<a href="javascript:postSelectDel();">
							<img src="../images/bt_del.gif" border="0" align="absmiddle"></a>
				<% } else { %>
						<a href="javascript:wopen('post_write.jsp','post_write','700','650');">
							<img src="../images/bt_new_letter.gif" border="0" align="absmiddle"></a>
						<a href='post_main.jsp?ORDER=<%=request.getParameter("ORDER")%>&SEL=ALL&DEL=&CHG=<%=ALL_CHG%>&pageNo=<%=ipage%>'>
							<img src="../images/bt_sel_all.gif" border="0" align="absmiddle"></a>
						<a href="javascript:postSelectDel();">
							<img src="../images/bt_del.gif" border="0" align="absmiddle"></a>
				<% } %></TD>
			  <TD width='' align='right' style="padding-right:10px">
				<%	if (bean.getCurrentPage() <= 1) {	%>
					<img src='../images/bt_previous.gif' border='0' align="absmiddle">
				<%	} else 	{	%>
					<a href='javascript:goPage(<%=bean.getCurrentPage()-1%>)'><img src='../images/bt_previous.gif' border='0' align="absmiddle"></a>
				<%	} if ((bean.getCurrentPage() != bean.getLastPage()) && (bean.getLastPage() != -1 )) { %>
					<a href='javascript:goPage(<%=bean.getCurrentPage()+1%>)'><img src='../images/bt_next.gif' border='0' align="absmiddle"></a>
				<%	} else 	{  %> 
					<img src='../images/bt_next.gif' border='0' align="absmiddle">
				<%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--리스트-->
  <TR height=100%><form method="post" name="dForm" action="post_main.jsp" style="margin:0">
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>선택</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>구분</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>발신자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>발신일</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<% 
	count=0;
	while(bean.isAvailable()) {	

		/***********************************************************************
		발신자 배달확인 요청 분석 (예, cFm,sEc,rsP,)
		***********************************************************************/
		String psel = bean.getData("post_select");

		cfm=sec=rsp=sendConfirm="";
		if((psel != null) && (psel.length() > 0)) {
			sendConfirm="[";
			if(psel.indexOf('F') > 0) cfm="수신";		//수신확인 요청
			if(psel.indexOf('E') > 0) sec="비밀";		//비밀확인 요청
			if(psel.indexOf('P') > 0) rsp="긴급";		//긴급확인 요청
			sendConfirm += cfm + " " + sec + " " + rsp + "]";		
		}
		if(sendConfirm.length() == 4) sendConfirm = "";	//옵션사항 없음
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><input type="checkbox" name="check<%=count%>" value='<%=bean.getData("pid")%>' <% if(ALL_SEL.equals("ALL")) out.print("CHECKED"); %>></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'>
			<% 	//내용봤는지 여부
				String isSee = bean.getData("isopen");
				if(isSee != null) {
					if(PROCESS.equals("REC_ING")) {			//받은편지
						if(isSee.equals("0")) { 				//미확인
							out.println("<img src='../images/new_letter.gif' align='middle' border='0'>"); 
						} else {						//확인
							out.println("&nbsp;");
						}
					} else if(PROCESS.equals("SND_ING")) {		//보낸편지
						out.println("&nbsp;"); 
					} else if(PROCESS.equals("TMP_ING")) {		//보낼편지
						out.println("&nbsp;"); 
					} else if(PROCESS.equals("WST_ING")) {		//휴지통
						out.println("&nbsp;"); 
					} //if
				} //if
			%>			  
			  </td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:3px">
			<% if((sec.length() > 0) && (PROCESS.equals("REC_ING"))){ 	//비밀문서, 받은편지만 적용 %>
				<a href="javascript:wopen('post_viewPass.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','300','145');" >&nbsp;<%=bean.getData("post_subj")%></a>
			<% } else if(PROCESS.equals("SND_ING")) { 			//보낸편지 적용 (배달선택사항 보여주기)%>
				<a href="javascript:wopen('post_view.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','700','650');"><%=sendConfirm%>&nbsp;
				<%=bean.getData("post_subj")%></a>
			<% } else if(PROCESS.equals("TMP_ING")) { //보낼편지면 배달선택사항 보여주고 다시 작성메뉴로  %>
				<a href="javascript:wopen('post_write.jsp?INI=&PID=<%=bean.getData("pid")%>','post_decision','700','650');"><%=sendConfirm%>&nbsp;
				<%=bean.getData("post_subj")%></a>	
			<% } else  { 							//제목만 보여주기 %>
				<a href="javascript:wopen('post_view.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','700','650');">&nbsp;
				<%=bean.getData("post_subj")%></a>
			<% } %>						  
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
				<%
					String wname = bean.getData("writer_name");
					if(wname.length() > 20) out.println(wname.substring(0,20));
					else out.println(wname);
				%>			  
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("write_date")%></TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<% 
	count++;
	}  //while 
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>

<input type="hidden" name="ORDER" value="<%=PROCESS%>">
<input type="hidden" name="DEL" value="OK">
<input type="hidden" name="SEL" value="">
<input type="hidden" name="pageNo" value="<%=ipage%>">
</form>

<form name = "eForm" method = "post">
	<input type="hidden" name="pageNo" value="">
	<input type="hidden" name="ORDER" value="<%=PROCESS%>">
</form>
</body>
</html>

<script language=javascript>
<!--
var m = '<%=Message%>';
if(m.length > 0) parent.Left.location.reload();

function goPage(pageNo)
{
	document.eForm.action = "post_main.jsp";
	document.eForm.pageNo.value = pageNo;
	document.eForm.submit();
}

function postSelectDel()
{
	var sel = confirm('정말로 삭제하시겠습니까?');
	if(sel == false) return;

	document.dForm.action="post_main.jsp";
	document.dForm.ORDER.values='<%=PROCESS%>';
	document.dForm.DEL.values='OK';
	document.dForm.SEL.values='';
	document.dForm.submit();

}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=yes');
}
-->
</script>