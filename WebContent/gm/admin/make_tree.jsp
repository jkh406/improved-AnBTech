<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkGM01.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>

<script language='javascript'>
	alert('제품 트리가 정상적으로 생성되었습니다.');
	location.href = "../gm/admin/category_list.jsp"
</script>