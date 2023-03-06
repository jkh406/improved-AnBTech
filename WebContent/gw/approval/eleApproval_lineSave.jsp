<%@ include file="../../admin/configPopUp.jsp"%>

<%@ 	page		
	info= "전자결재 결재선 저장하기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.*"
	import="com.anbtech.file.*"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	String Message = "";

	/*********************************************************************
	 	결재선 저장하기 
	*********************************************************************/
	//from eleApproval_ViewShaareList.jsp
	String receive_data =Hanguel.toHanguel(request.getParameter("Lsave"));	//저장할 결재선 내용
	String doc_dat=bean.getTime();						//결재선 저장일자
	String doc_pid = bean.getID();						//결재선 관리번호
	String doc_pat = "/eleApproval/" + login_id;		//저장 Dir Path (DB상에 저장path 알려주기)

	/******************************************************************************************
	//결재선저장시 결재선 저장테이블에 결재선 저장하기 (별도의 테이블)
	*******************************************************************************************/
	String NAME = Hanguel.toHanguel(request.getParameter("save_name")); //저장할 결재선 이름
	if(NAME == null) NAME = "";

	if((NAME.length() != 0) && (receive_data.length() != 0)) {
		String lineDir = upload_path + doc_pat + "/Linefile";	//File Path
		String line_fi = "LS" + doc_pid;										//결재라인저장 file 명

		//파일로 저장
		String FullPathName = lineDir;		//Full path Name
		text.WriteHanguel(FullPathName,line_fi,receive_data);	//path,파일명,내용
		
		//문제가 없으면 DB로 저장한다.
		String line_inputs="INSERT INTO APP_LINESAVE(pid,line_subj,writer,write_date,bon_path,line_file)";
			 line_inputs +=" values('"+doc_pid+"','"+NAME+"','"+login_id+"','"+doc_dat;
			 line_inputs +="','"+doc_pat+"','"+line_fi+"')";
		try { bean.execute(line_inputs);
			  receive_data = "";//clear
			  Message = "LINE_INSERT";
		} catch (Exception e) { Message = "QUERY"; }			
	} //if
%>

<HTML><HEAD><TITLE>결재선 저장</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="sForm" action="eleApproval_lineSave.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">결재선명</td>
           <td width="70%" height="25" class="bg_02"><input type="text" name="save_name" size="20"> 
			<input type='hidden' name='Lsave' value='<%=receive_data%>'></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:document.sForm.submit();'><img src='../images/bt_save.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></form></BODY></HTML>



<!-- ****************** 메시지 전달부분 ****************************** -->

<% if(Message == "LINE_INSERT") { %>
<script>
alert('결재선이 저장 되었습니다.')
close();
</script>
<% Message = "" ; } %>

<% if(Message == "Query") { %>
<script>
alert('저장에 실패했습니다.')
close();
</script>
<% Message = "" ; } %>

<% if(Message == "NO_LINE") { %>
<script>
alert('저장할 결재선 내용이 없습니다.')
close();
</script>
<% Message = "" ; } %>

