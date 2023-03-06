<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language="java"
	contentType="text/html;charset=euc-kr"
	errorPage="../../admin/errorpage.jsp"
	import="java.util.*,com.anbtech.qc.entity.*"
%>
<%
	//일련번호 가져오기
	String[] goods_serial_no = new String[2];
	goods_serial_no = (String[])request.getAttribute("SERIAL_NO");
	String serial_s = goods_serial_no[0];
	String serial_e = goods_serial_no[1];
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../qc/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

</body>
</html>

<script language='javascript'>
	opener.document.writeForm.serial_no_s.value = '<%=serial_s%>';
	opener.document.writeForm.serial_no_e.value = '<%=serial_e%>';
	alert("제품일련번호는 <%=serial_s%> ~ <%=serial_e%>입니다.");
	self.close();
</script>