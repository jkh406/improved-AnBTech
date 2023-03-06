<%@ include file="../../../admin/configHead.jsp"%>
<%@ page
	language = "java"
	info = "서비스 이력 출력하기"		
	contentType = "text/html; charset=euc-kr"
%>
<%@	page import="com.anbtech.ViewQueryBean" %>

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

	String query = "select a.s_day,a.s_time,b.name 'pname',c.name,a.class,a.subject,a.content,a.result,a.file_name,a.file_size,a.umask from history_table a,company_table b,customer_table c where a.ah_id = '"+ah_id+"' and a.ap_id = b.ap_id and a.at_id = c.at_id";
	
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

<html>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<table width='700' border='0' cellspacing='0' cellpadding='0' height="54" style="border-collapse: collapse" bordercolor="#111111">
<tr><td colspan="4" height="5">
	<table border="0" width="695">
	  <tr>
		<td width="100%" valign=top>
			<table border="1" cellpadding="0" cellspacing="0" borderColorDark=#FFFFFF borderColorLight=#676767 width="695" height=25 bordercolor="#111111">
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%" height=25>방문일자</td>
				<td width="37%"><%=s_day%></td>
				<td bgColor=#d6d3d6 align=center width="13%">방문회사</td>
				<td width="37%"><%=s_pname%></td>
			  </tr>
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%" height=25>방문시간</td>
				<td width="37%"><%=s_time%></td>
				<td bgColor=#d6d3d6 align=center width="13%">방문고객</td>
				<td width="37%"><%=s_name%></td>
			  </tr>
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%" height=25>방문목적</td>
				<td width="87%" colspan="3">[<%=s_class%>] <%=s_subject%></td>
			  </tr>
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%" height="450">방문내용</td>
				<td width="87%" colspan="3">
				<%	String readcon = s_content;
					if(readcon.equals("null")) readcon = "";
					for(int i=0; i<readcon.length(); i++) {
						if(readcon.charAt(i) == '\n') out.print("<br>");
						else if(readcon.charAt(i) == ' ') out.print("&nbsp;");
						else out.print(readcon.charAt(i));
					}
				%>
			　</td>
			  </tr>
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%"  height="150">이슈사항</td>
				<td width="87%" colspan="3">
				<%	String readres = s_result;
					if(readres.equals("null")) readres = "";
					for(int i=0; i<readres.length(); i++) {
						if(readres.charAt(i) == '\n') out.print("<br>");
						else if(readres.charAt(i) == ' ') out.print("&nbsp;");
						else out.print(readres.charAt(i));
					}
				%>
				&nbsp;</td>
			  </tr>
			  <tr>
				<td bgColor=#d6d3d6 align=center width="13%" height=25>첨부파일</td>
				<td width="87%" colspan="3">
				<%
					if(!s_file_name.equals("null")) {
						out.println(s_file_name);
					}
				%>	　
				</td>
			  </tr>
			</table>
		</td>
	  </tr>
	</table>
	</td>
	</tr>
</table><br>

</body>
</html>
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
