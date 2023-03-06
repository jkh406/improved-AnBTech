<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String class_code = request.getParameter("class_code")==null?"":request.getParameter("class_code");
%>
<html>
<head><title></title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<body>
<form name='frml' action="list_inspection_item.jsp" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- 타이틀 및 페이지 정보 -->
			<TD vAlign=top>
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 품질검사항목관리</TD>
						<TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
			</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
			<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
				<TBODY>
				<TR><TD align=left width='500' style='padding-left:5px'>
				<SELECT name='class_code' onchange='javascript:document.frml.submit()'>
					<option value=''>전체</option>
<%
	//항목분류코드 리스트
	String sql = "";
	sql = "SELECT DISTINCT inspection_class_code,inspection_class_name FROM qc_inspection_item";
	bean.openConnection();	
	bean.executeQuery(sql);

	while(bean.next()) {
%>
						<option value='<%=bean.getData("inspection_class_code")%>'><%=bean.getData("inspection_class_name")%></option>
<%	}	%>
				<% if(!class_code.equals("")) {%>
					<script>
						document.frml.class_code.value = '<%=class_code%>';
					</script>
				<% }%>
			  </SELECT> <a href="javascript:go_write()"><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a>
			  </TD></TR></TBODY></TABLE></TD></TR>
	<TR height=100%><!--리스트-->
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
				<TR vAlign=middle height=23>
					<TD noWrap width=40 align=middle class='list_title'>번호</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=80 align=middle class='list_title'>검사분류코드</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>검사분류명</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=80 align=middle class='list_title'>검사항목코드</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=120 align=middle class='list_title'>검사항목명</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>검사기록속성</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100 align=middle class='list_title'>편집</td>
					<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
					<TD noWrap width=100% align=middle class='list_title'></td></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%	
	//선택된 분류에 속한 검사항목 리스트 가져오기
	if(class_code == null || class_code.equals("")) {
		sql = "SELECT * FROM qc_inspection_item ORDER BY inspection_class_code ASC";
	} else {
		sql = "SELECT * FROM qc_inspection_item WHERE inspection_class_code = '" + class_code + "' ORDER BY inspection_code ASC";
	}
	bean.executeQuery(sql);

	int no = 1 ;	
	while(bean.next()){	
%>
		<tr onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
			<td height='24' class='list_bg' align=center><%=no%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("inspection_class_code")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("inspection_class_name")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("inspection_code")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("inspection_name")%></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><%=bean.getData("inspection_result_type")%></td>
		    <TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center><A HREF="javascript:modify('<%=bean.getData("mid")%>','<%=class_code%>')"><IMG src='../images/lt_modify.gif' align='absmiddle' border='0' alt='수정'></a>&nbsp;<A HREF="javascript:del('<%=bean.getData("mid")%>','<%=class_code%>')"><IMG src='../images/lt_del.gif' align='absmiddle' border='0' alt='삭제'></td>
			<TD><IMG height=1 width=1></TD>
			<td height='24' class='list_bg' align=center></td></tr>
	  <TR><TD colSpan=15 background="../images/dot_line.gif"></TD></tr>
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

function modify(mid,class_code){
	location.href="write_inspection_item.jsp?mode=modify&mid="+mid+"&class_code="+class_code;
}

function del(mid,class_code){
	if(confirm("정말 삭제 하시겠습니까?")) {
		location.href="process_inspection_item.jsp?mode=delete&mid="+mid+"&class_code="+class_code;
	}
}

function go_write(){
	var class_code;
	var class_name;
	
	for(i=0; i<f.class_code.length;i++){
		if(f.class_code.options[i].selected == true){
			class_name = f.class_code.options[i].text;
			class_code = f.class_code.options[i].value;
		}	
	}
	location.href = "write_inspection_item.jsp?mode=write&class_code="+class_code+"&class_name="+class_name;
}
-->
</script>