<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "개인우편 작성"		
	contentType = "text/html; charset=euc-kr" 		
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
		division = bean.getData("ac_name");			//접속자 부서명
		tel = bean.getData("office_tel");			//접속자 전화번호
	} //while

	/*********************************************************************
	 	제목및 본문내용 작성하기
	*********************************************************************/
	String pid = request.getParameter("pid");
	String subject="[보존된문서] 요청하신 문서입니다. 참고하시길 바랍니다.";		//문서제목
	String content="요청하신 보존문서 입니다.";										//본문
	String rpid = anbdt.getID();
	
%>

<HTML><HEAD><TITLE>새편지작성</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form method="post" name="sForm" encType="multipart/form-data" style="margin:0">
	
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--버튼-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <a href="Javascript:postReceiver();"><img border="0" src="../../images/bt_sel_receiver.gif" align="absmiddle"></a>
		  <a href="Javascript:postSend();"><img border="0" src="../../images/bt_export.gif" align="absmiddle"></a>
          <a href="Javascript:postClose();"><img border="0" src="../../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--발송정보-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">작성자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name%> [전화: <%=tel%>,  부서명:<%=division%>,  작성일:<%=bean.getTime()%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">수신자</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=2 cols=75  class='text_01' readOnly></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="SUBJECT" size=76 value="<%=subject%>" class='text_01' ></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">선택사항</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<input type="checkbox" name="ReturnReceipt" value="CFM">수신확인&nbsp;
			<input type="checkbox" name="SecretSetup" value="SEC">비밀편지&nbsp;
			<input type="checkbox" name="ReplySetup" value="RSP">긴급편지
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="CONTENT" rows=22 cols=75 class='text_01'><%=content%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">요청문서</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input size=60 type="text" name="ref_name"  class='text_01' readonly> <a href="Javascript:searchRefDocument();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../../images/bt_close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
<input type="hidden" name="pid" value='<%=anbdt.getID()%>'>
<input type="hidden" name="ref_id">
<input type="hidden" name="res">
</form>


</BODY></HTML>

<script>
<!--
//발송 저장하기
function postSend()
{
	if(sForm.SUBJECT.value == ""){	alert("제목을 입력하십시요.");	return;	}
	if(sForm.CONTENT.value == ""){	alert("내용을 입력하십시요.");	return;	}
	if(sForm.rec_name.value == ""){	alert("수신인을 지정하십시요.");return;	}
	if(sForm.ref_name.value == ""){	alert("요청문서을 지정하십시요.");return;	}

	var sel = confirm('발송 하시겠습니까?');
	if(sel == false) {	alert("발송하지 않습니다."); return; }

	var ref_id = document.sForm.ref_id.value;
	var content = document.sForm.CONTENT.value;
	content += "<p><p><a href=\"javascript:viewDel();\">상세내용보기</a> \n\n";
	content +="<script language=javascript><!-- \n";
	content +=" function viewDel(){ \n";
	content +="wopen('../../../../../../servlet/ApprovalDetailServlet?mode=DEL_BOX&PID="+ref_id+"','save_doc','860','650');} \n";
	content +=" function wopen(url, t, w, h) { var sw; var sh; ";
	content +=" sw = (screen.Width - w) / 2; sh = (screen.Height - h) / 2 - 50; ";
	content +=" window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+',Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=no'); } \n";
	content +=" --><\/script>";

	document.sForm.action="saveMailDelDocument.jsp";
	document.sForm.res.value="SND";
	document.sForm.CONTENT.value=content;
	document.sForm.submit();
}
//새편지작성 닫기
function postClose()
{
	this.close();
}
//수신인
function postReceiver()
{
	receivers = document.sForm.rec_name.value; 
	wopen("../../mail/post_Share.jsp?Title=Search&Rec="+receivers+"&target=sForm.rec_name","post_rSel","510","467");
}
//관련문서 찾기
function searchRefDocument()
{
	var url = "searchStoreHouseDocument.jsp?target_id=sForm.ref_id&target_name=sForm.ref_name&";
	url += "rec_name=sForm.rec_name";
	wopen(url,"ref_id","520","310","scrollbars=no,toolbar=no,status=no,resizable=no");
}
//창
function wopen(url, t, w, h, st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+st);
}
-->
</script>