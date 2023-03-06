<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" import="java.sql.*,java.util.*,com.anbtech.text.Hanguel" contentType="text/html;charset=KSC5601" %>

<%							
	String filename	= request.getParameter("fname");		//원 파일명
	String filetype	= request.getParameter("ftype");		//파일 type
	String filesize	= request.getParameter("fsize");		//파일 크기
	String savename = request.getParameter("sname");		//저장 파일명
	String filepath = request.getParameter("fpath");		//저장 path

	String downFile	= filepath+"/"+savename;	
	if (filetype.indexOf("mage")<=0) filetype = "application/unknown";

	String strClient=request.getHeader("User-Agent");
	response.setHeader("Content-Type", "application/octet-stream;"); 
	response.setHeader("Content-Disposition", "attachment; filename="+filename+";"); 

	response.setContentType(filetype);
	response.setContentLength(Integer.parseInt(filesize));
					
	byte b[] = new byte[Integer.parseInt(filesize)];
	java.io.File f = new java.io.File(downFile);
	java.io.FileInputStream fin = new java.io.FileInputStream(f);
	ServletOutputStream fout = response.getOutputStream();
	fin.read(b);
	fout.write(b,0,Integer.parseInt(filesize));
	fout.close();

%>
<html>
<head>
<title>다운로드</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
</head>
<body>

</body>
</html>
