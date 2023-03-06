<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "자산관리 이관/반출 결재상신"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	
	String writer_id = "";			//작성자 사번
	String writer_name = "";		//작성자 이름
	String query = "",subject="";
	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	/*********************************************************************
	 	전달변수
		관리종류 [o_status : t:자산이관, o:자산반출]
		결재상태 [2:1차상신대기,3:1차결재완료,4:2차상신대기,5:2차결재완료]
	*********************************************************************/	
	String title_name = "";
	String line = request.getParameter("doc_app_line"); if(line == null) line = "";		//결재선

	String mode = request.getParameter("mode");			if(mode == null) mode = "";		//mode
	String h_no = request.getParameter("h_no");			if(h_no == null) h_no = "";		//관리번호
	String as_no = request.getParameter("as_no");		if(as_no == null) as_no = "";	//자산번호
	String o_status = request.getParameter("o_status");	if(o_status == null) o_status = "";	//관리종류
	String as_status = request.getParameter("as_status");if(as_status == null) as_status = "";	//결재상태
	if(o_status.equals("t")) title_name = "자산이관";
	else if(o_status.equals("o")) title_name = "자산반출";
	else if(o_status.equals("l")) title_name = "자산대여";
	
	/*********************************************************************
	 	자산에서 제목만들기
	*********************************************************************/	
	String[] tColumn = {"a.as_name","a.model_name","b.takeout_reason"};
	bean.setTable("as_info a,as_history b");			
	bean.setColumns(tColumn);
	bean.setOrder("model_name ASC");	
	query = "where as_no ='"+as_no+"' and a.as_no = b.as_no";
	bean.setSearchWrite(query);
	bean.init_write();
	if(bean.isAll()) {
		subject = title_name+" : ["+bean.getData("as_name")+"/"+bean.getData("model_name")+"]";
		subject += bean.getData("takeout_reason");
	}
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title><%=title_name%></title>
<LINK href="../../css/style.css" rel=stylesheet>
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
<script language=javascript>
<!--
//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	window.open("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","width=680,height=600,scrollbar=yes,toolbar=no,status=no,resizable=no");
}

//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
	 //결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
		if(rstr.indexOf("협조") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }

	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	//제목구하기
	var doc_sub = '<%=subject%>';
alert(doc_sub);
	//일괄합의 결재상신진행
//	document.eForm.action='../../../servlet/ApprovalAssetServlet';	
//	document.eForm.app_mode.value='REQ';
//	document.eForm.doc_sub.value=doc_sub;
//	document.eForm.submit();
}

//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//닫기
function winClose()
{
	window.returnValue='';
	self.close();
}
-->
</script>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>
<form name="eForm" method="post">
<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
		<div id="print" style="position:absolute;left:420px;top:60px;width:300px;height:10px;visibility:visible;">
			<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../images/button_line_call.gif' align='middle' border='0'></a> <!-- 결재선 -->
			<a href="Javascript:eleApprovalRequest();"><img src='../../images/button_sangshin.gif' align='middle' border='0'></a> <!-- 상신 -->
			<a href='Javascript:winprint();'><img src='../../images/button_print.gif' align='middle' border='0'></a> <!-- 출력 -->
		</div>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b><%=title_name%> </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../images/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">메<p>모</td>
		<td width="420" height="96" rowspan=3>
		<TEXTAREA NAME="doc_app_line" rows=6 cols=57 readOnly style="border:1px solid #787878;"><%=line%></TEXTAREA>
		</td>
		<td width="20" height="96" rowspan=3 align="center">결<p>재</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">기 안</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">검 토</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">승 인</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
		<td width="60" height="50" align="center">&nbsp;</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;</td>   
	</tr>   
</table>

<iframe id="iframe_main" src="../../../servlet/AssetServlet?mode=<%=mode%>&h_no=<%=h_no%>&as_no=<%=as_no%>" width="640" height="300" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="yes">
</iframe>

<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='<%=title_name%>'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='h_no' value='<%=h_no%>'>					<% //자산 관리번호 %>
<input type='hidden' name='as_no' value='<%=as_no%>'>				<% //자산번호 %>
<input type='hidden' name='o_status' value='<%=o_status%>'>			<% //관리종류 %>
<input type='hidden' name='as_status' value='<%=as_status%>'>		<% //결재상태 %>

<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>  

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</center>
</body>
</html>

