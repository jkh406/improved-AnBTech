<%@ include file="../../admin/configPopUp.jsp"%>

<%@ 	page		
	info= "전자결재 결재선 찾기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.lang.SecurityException"
	import="java.io.UnsupportedEncodingException"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.*"
	import="com.anbtech.file.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//---------------------------
	//변수 선언 
	//---------------------------
	String Message="";			//메시지 전달 변수  

	//접속자 정보 전달변수
	String name = "";			//접속자 이름
	String division = "";		//접속자 부서명	

	//저장된 결재라인 전달변수
	String[] line_subj;			//저장된 결재라인 명
	String[] line_file;			//저장된 결재라인 파일명
	String[] pid;				//관리코드 
	int line_cnt;				//라인 수

	String search_subj="";		//찾은결재라인 코드 계속유지
	String LINE_DATA="";		//저장된 데이터 내용

	//요청한 window로 부터 받은
	String TITLE="";			//요청된 결재선 결재권자는

	//저장된 결재선 데이터 읽어 넘겨줄 변수
	String SEND_DATA;			// 결재선 입력내용 넘겨줄데이터
	int send_cnt;				// 입력된 결재선 갯수


	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//text문자 읽기

	/*********************************************************************
	 	결재선명 변경
	*********************************************************************/
	String read_line_file = request.getParameter("L_FILE");
	String read_line_name = request.getParameter("L_NAME");

	if(read_line_file != null){
		//결재선명 한글보상
		read_line_name = Hanguel.toHanguel(read_line_name);					//한글
		String chg_data = "update APP_LINESAVE set line_subj='" + read_line_name + "' where line_file='" + read_line_file + "'";
		try { bean.execute(chg_data); } catch (Exception e) { Message = "QUERY"; }
	} //if

	/*********************************************************************
	 	결재선명 삭제
	*********************************************************************/
	String delete_line_file = request.getParameter("D_FILE");

	if(delete_line_file != null){
		//DB내의 해당 레코드 삭제
		String DEL = "delete from APP_LINESAVE where line_file='" + delete_line_file + "'";
		bean.execute(DEL);

		//해당 파일삭제 하기(Text file삭제)
		String Textdir = upload_path+"/eleApproval/"+login_id+"/Linefile/"+delete_line_file;
		text.delFilename(Textdir);
	} //if

	/*********************************************************************
	 	저장된 전체의 결재선 알아보기 (login한 작업자에 한해서)
	*********************************************************************/
	String[] line_dbColumns = {"pid","line_subj","writer","write_date","bon_path","line_file"};
	bean.setTable("APP_LINESAVE");
	bean.setColumns(line_dbColumns);
	bean.setOrder("pid ASC");
	bean.setSearch("writer",login_id);	
	bean.init();				//데이터 쿼리하여 Table에 담기

	line_cnt=bean.getTotalcount();

	line_subj = new String [line_cnt];	//saved Line name
	line_file = new String [line_cnt];	//saved file name
	pid = new String [line_cnt];		//관리코드
	int k=0;
	while(bean.isAll()){
		pid[k] = bean.getData("pid");
		line_subj[k] = bean.getData("line_subj");
		line_file[k] = bean.getData("line_file");
		k++;
	}

	/****************************************************************
		결재선 내용 읽기
	****************************************************************/
	String ReadFile = request.getParameter("moDivision");		//결재선 파일명
	if(ReadFile == null) ReadFile = "";
	LINE_DATA = "";
	//파일로 찾기 
	if(ReadFile.length() != 0){
		String LineSavefile = upload_path+"/eleApproval/"+login_id+"/Linefile/"+ReadFile;
		
		try { LINE_DATA = text.getFileString(LineSavefile);	   		//Text file Read 
		} catch (Exception e) { }
		LINE_DATA = str.repWord(LINE_DATA,";",";\r");
	}

	// 결재선 갯수파악하기
	SEND_DATA = "";
	SEND_DATA = LINE_DATA;
	send_cnt = 0;
	for(int ld=0; ld < LINE_DATA.length(); ld++){  
		char ch = LINE_DATA.charAt(ld);
		if(ch == ';') send_cnt++;
	}	

%>

<HTML><HEAD><TITLE>결재선 열기</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_o.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">
				<table border=0 width=100%>
				<tr><td width=100%>
					<form method=post  name="sForm" style="margin:0">
						<select name="moDivision" onChange="toDivision(this.form);">
						<option value='eleApproval_line.jsp?moDivision=&subName='>::결재선 선택::</option>
						<%for(int gk=0; gk < line_cnt; gk++) { 
							out.print("<option "); 
							//선택된 결재선을 selected 표기하기
							String div_fn = request.getParameter("moDivision");
							if(div_fn != null){ 
								if(div_fn.equals(line_file[gk])) out.print("selected ");
							}
							String[] send_p = new String[line_cnt];
							send_p[gk] = line_file[gk];	//파일로 부터 읽기 때문에 파일명을 
							out.println("value='eleApproval_line.jsp?moDivision=" + send_p[gk] + "&subName=" + gk + "'>" + line_subj[gk]); 
						}%> 
					</td></select></form></tr>
				<tr><td width=100%><b>결재선명</b><br>
						<form name="cForm" method="post" style="margin:0">
							<input type="hidden" name="L_FILE" value='<%=request.getParameter("moDivision")%>'>
							<input type="text" size="14" name="L_NAME" <% if((request.getParameter("subName") == null) || ((request.getParameter("subName").length() == 0))) out.println("vlaue="); else out.println("value='" + line_subj[Integer.parseInt(request.getParameter("subName"))] + "'"); %>></form></td></tr>
				<tr><td width=100% height="30"><form name="dForm" method="post" style="margin:0">
					<a href="javascript:line_change();"><img src='../images/bt_modify.gif' align='absmiddle' border='0'></a>	<input type="hidden" name="D_FILE" value='<%=request.getParameter("moDivision")%>'> <a href="javascript:line_delete();"><img src='../images/bt_del.gif' align='absmiddle' border='0'></a></td></form></td></tr></table>
		   </td>
           <td width="70%" height="25" class="bg_02">
			<form name="aForm" method="post" style="margin:0">
			  <TEXTAREA NAME="dec_app_line" rows=7 cols=40 readOnly><%=LINE_DATA%></TEXTAREA></form>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:return_value('<%=send_cnt%>','<%=SEND_DATA%>')"><img src='../images/bt_confirm.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--

//윈도우창 초기화
function setFocus() 
{
	defaultStatus = "결재선 저장내용 불러오기";		//윈도우 하단 출력메시지
	window.resizeTo(580,380);				//윈도우 크기조절	
	window.moveTo(100,100);					//윈도우 출력위치 조절
}

//이름변경
function line_change()
{
	document.cForm.action = "eleApproval_line.jsp";
	document.cForm.submit();
}

//이름삭제
function line_delete()
{
	document.dForm.action = "eleApproval_line.jsp";
	document.dForm.submit();
}

//모윈도우에 값 돌려주기
function return_value(no,str)
{
	var cnt = no;
	var my_str =str;
	var data = "";

	for(i = 0; i < cnt; i++) {
		var p = my_str.indexOf(';');
		if(i == 0)
			data = my_str.substring(0,p) + ';';
		else
			data = data + my_str.substring(0,p) + ';';
		my_str = my_str.substring(p+1,my_str.length);	
	}  //for

	//넘겨주기	
	opener.lineCall(no,data);
	this.close();
}

//선택된 결재선 바로읽기
function toDivision(form) {
    var myindex=form.moDivision.selectedIndex;
    if (form.moDivision.options[myindex].value != null) {
         window.location=form.moDivision.options[myindex].value;
    }
}

-->
</script>


<!-- ****************** 메시지 전달부분 ****************************** -->

<% if(Message == "NO_SESSION") { %>
<script>
alert("접속시간이 경과되었습니다.\n\n다시 접속후 진행하십시요.")
close()
</script>
<% Message = "" ; } %>

<% if(Message == "UPDATE") { %>
<script>
alert("결재선명이 변경되었습니다.")
</script>
<% Message = "" ; } %>

<% if(Message == "QUERY") { %>
<script>
alert("결재선명 변경에 문제가 있습니다.")
</script>
<% Message = "" ; } %>

