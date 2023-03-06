<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부품출고의뢰 전개 Frame"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	String factory_no="",mfg_req_no="",para="";
	String src_list="",src_reg="";

	String msg = (String)request.getAttribute("msg"); if(msg == null) msg = "";
	//JSP에서 분기
	if(msg.length() == 0) {
		factory_no = request.getParameter("factory_no"); if(factory_no == null) factory_no = "";
		mfg_req_no = request.getParameter("mfg_req_no"); if(mfg_req_no == null) mfg_req_no = "";
		para = "mfg_req_no="+mfg_req_no+"&factory_no="+factory_no;
		src_list = "../../servlet/mfgOrderServlet?mode=out_list&"+para;
		src_reg = "../../servlet/mfgOrderServlet?mode=out_preview&pid=";
	} 
	//Servlet에서 분기
	else { 
		factory_no = (String)request.getAttribute("factory_no"); if(factory_no == null) factory_no = "";
		mfg_req_no = (String)request.getAttribute("mfg_req_no"); if(mfg_req_no == null) mfg_req_no = "";
		para = "mfg_req_no="+mfg_req_no+"&factory_no="+factory_no;
		src_list = "../servlet/mfgOrderServlet?mode=out_list&"+para;
		src_reg = "../servlet/mfgOrderServlet?mode=out_preview&pid=";
	}
	
%>
<HTML>
<HEAD>
<TITLE> 공정 제조부품출고의뢰 전개 Frame</TITLE>
<script language=javascript>
<!--
//메시지 전달
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}
-->
</script>
</HEAD>

<frameset framespacing="0" border="1" bordercolor=black rows="*,110" frameborder="0">
	<frame name="list" target="list" src="<%=src_list%>" scrolling="no" noresize> 
	<frame name="reg" target="reg" src="<%=src_reg%>" scrolling="no" noresize>
	<noframes>
<BODY>

</BODY>
</noframes>
</frameset>
</HTML>