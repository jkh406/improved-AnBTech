<%@ include file="../../checkAdmin.jsp"%>
<%@ include file= "../../configHead.jsp"%>
<%@ page language="java" import="java.sql.*,com.anbtech.text.Hanguel" contentType="text/html;charset=KSC5601" %>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
		
	String type = request.getParameter("type")==null?"":request.getParameter("type");
	bean.openConnection();
	String sql = "";

	sql = "SELECT DISTINCT type, type_name FROM system_minor_code";
	
	bean.executeQuery(sql);
	
		
%>
<html>
<head><title>System식별코드관리</title></head>
<link rel="stylesheet" type="text/css" href="../../css/style.css">
<body>
<form name='frml' action="minor_list.jsp" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title">&nbsp;<img src="../../images/blet.gif" align="absmiddle"> System식별코드 관리</TD>
						<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
			</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<TBODY>
				<TR><TD align=left width='500' style='padding-left:5px'>
				<SELECT name=type onchange='javascript:document.frml.submit()'>
					<option value=''>전체</option>
					<% while(bean.next()) {%>
					<option value='<%=bean.getData("type")%>'><%=bean.getData("type_name")%></option>
					<%}%>
				<% if(!type.equals("")) {%>
					<script>
						document.frml.type.value = '<%=type%>';
					</script>
				<% }%>
			  </SELECT> <a href="javascript:go_write()"><IMG src='../../images/bt_add.gif' border='0' align='absmiddle'></a>
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
				<TR vAlign=middle height=23>
					<TD noWrap width=40 align=middle class='list_title'>번호</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<TD noWrap width=80 align=middle class='list_title'>구분코드</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<% if(type.equals("")) { %>
					<TD noWrap width=100 align=middle class='list_title'>구분명</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<%	}     %>
					<TD noWrap width=80 align=middle class='list_title'>Minor 코드</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<TD noWrap width=120 align=middle class='list_title'>Minor 코드명</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>관리</td>
					<TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					<TD noWrap width=100% align=middle class='list_title'></td></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
		if(type == null || type.equals("")) {
			sql = "SELECT * FROM system_minor_code ORDER BY type";
		} else {
			sql = "SELECT * FROM system_minor_code WHERE type = '"+type+"'";
		}
		
		bean.executeQuery(sql);

		int no = 1 ;	

		while(bean.next()){	
%>
		<tr onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
			<td height='24' class='list_bg' align=center><%=no%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("type")%></td>
			<TD><IMG height=1 width=1></TD>
			<% if(type.equals("")) {%>
			<td height='24' class='list_bg' align=center><%=bean.getData("type_name")%></td>
			<TD><IMG height=1 width=1></TD>
			<%}%>
			<td height='24' class='list_bg' align=center><%=bean.getData("code")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("code_name")%></td>
		    <TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><A HREF="javascript:modify('<%=bean.getData("mid")%>','<%=type%>','<%=bean.getData("fixed")%>')"><IMG src='../../images/lt_modify.gif' align='absmiddle' border='0' alt='수정'></a>&nbsp;<A HREF="javascript:del('<%=bean.getData("mid")%>','<%=type%>','<%=bean.getData("fixed")%>')"><IMG src='../../images/lt_del.gif' align='absmiddle' border='0' alt='삭제'></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center></td></tr>
	  <TR><TD colSpan=13 background="../../images/dot_line.gif"></TD></tr>
<%	
	  no++;
	}	
%>
	</tbody>
	</table>
	</form>
</body>
</html>

<SCRIPT language='javascript'>
<!--

var f = document.frml;
function modify(mid,type,fixed){
	if(fixed=='y') {alert("이 항목은 기본 필수정보로서 수정불능 항목입니다."); return;}
	location.href="minor_write.jsp?mode=modify&mid="+mid+"&type="+type;
}

function del(mid,type,fixed){
	if(fixed=='y') {alert("이 항목은 기본 필수정보로서 삭제불능 항목입니다."); return;}
	if(confirm("정말 삭제 하시겠습니까?")) {
	location.href="minor_process.jsp?mode=delete&mid="+mid+"&type="+type;
	}
}

function go_write(){
	var type;
	var type_name;
	
	for(i=0; i<f.type.length;i++){
		if(f.type.options[i].selected == true){
			type_name = f.type.options[i].text;
			type	  = f.type.options[i].value;
		}	
	}
	
	location.href = "minor_write.jsp?mode=write&type="+type+"&type_name="+type_name;
}
-->
</script>