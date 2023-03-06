<%@ include file="../../../admin/configHead.jsp"%>
<%@ page
	language	= "java"
	info		= "서비스 이력 상세보기"		
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.ViewQueryBean"
	errorPage	= "../../../admin/errorpage.jsp"
%>

<%
	//변수선언
	String s_day = "";
	String s_time = "";
	String s_pname = "";
	String s_name = "";
	String s_class = "";
	String s_subject = "";
	String s_content = "";
	String s_result = "";
	String s_file_name = "";
	String s_file_size = "";
	String s_umask = "";

	//데이터 쿼리
	com.anbtech.ViewQueryBean bean = new com.anbtech.ViewQueryBean();
	String ah_id = request.getParameter("ah_id");	// 이력ID
	String vcnt = request.getParameter("vcnt");		// 이력ID 갯수

	String query = "select a.s_day,a.s_time,b.name_kor 'pname',c.name_kor 'name',a.class,a.subject,a.content,a.result,a.file_name,a.file_size,a.umask from history_table a,company_customer b,personal_customer c where a.ah_id = '"+ah_id+"' and a.ap_id = b.company_no and a.at_id = c.mid";

	bean.openConnection();	
	bean.executeQuery(query);

	while(bean.next()){
		String getDay = bean.getData("s_day");
		String getTime = bean.getData("s_time");
		s_day = getDay.substring(0,4)+ "년 " + getDay.substring(5,6)+ "월 " + getDay.substring(7,8)+ "일";
		s_time = getTime.substring(0,2)+ "시 " + getTime.substring(3,5)+ "분 ~ " + getTime.substring(6,8)+ "시 " + getTime.substring(9,11) + "분";
		s_pname = bean.getData("pname"); 
		s_name = bean.getData("name");
		s_class = bean.getData("class");
		s_subject = bean.getData("subject");
		s_content = bean.getData("content");
		s_result = bean.getData("result");
		s_file_name = bean.getData("file_name");
		s_file_size = bean.getData("file_size");
		s_umask = bean.getData("umask");
	}

%>
<HTML><HEAD><TITLE>고객이력 정보 보기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">

    <!--이력정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문일자</td>
           <td width="37%" height="25" class="bg_02"><%=s_day%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문시간</td>
           <td width="37%" height="25" class="bg_02"><%=s_time%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문회사명</td>
           <td width="37%" height="25" class="bg_02"><%=s_pname%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문고객명</td>
           <td width="37%" height="25" class="bg_02"><%=s_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문목적</td>
           <td width="87%" height="25" colspan="3" class="bg_02">[<%=s_class%>] <%=s_subject%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">방문내용</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
				<%	String readcon = s_content;
					if(readcon.equals("null")) readcon = "";
					for(int i=0; i<readcon.length(); i++) {
						if(readcon.charAt(i) == '\n') out.print("<br>");
						else if(readcon.charAt(i) == ' ') out.print("&nbsp;");
						else out.print(readcon.charAt(i));
					}
				%>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
				<%	String readres = s_result;
					if(readres.equals("null")) readres = "";
					for(int i=0; i<readres.length(); i++) {
						if(readres.charAt(i) == '\n') out.print("<br>");
						else if(readres.charAt(i) == ' ') out.print("&nbsp;");
						else out.print(readres.charAt(i));
					}
				%>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
				<%
					if(!s_file_name.equals("null")) {
						out.println("<a href=../../../gw/approval/module/service_downloadp.jsp?fname="+s_file_name+"&fsize="+s_file_size+"&umask="+s_umask+"&extend=service/>"+s_file_name);
						out.println("</a>");
					}
				%>			   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

</td></tr></table></BODY></HTML>

<script>
<!--
function go(url)
{
	opener.location.href = url;
	this.close();
}

//첨부파일 열기
function addFileOpen(file_name)
{
	var file_add = file_name;
	window.open(file_add,'add_view',"width=800,height=750,left=100,top=30,scrollbar=yes,toolbar=no,status=yes,resizable=yes");
}
-->
</script>

