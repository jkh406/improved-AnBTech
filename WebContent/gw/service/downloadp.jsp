<%@ include file= "../../admin/configPopUp.jsp"%>
<%@ page 
	language	= "java"
	import		= "java.sql.*,java.util.*,com.anbtech.text.Hanguel"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>

<%
	String filename		= request.getParameter("fname");
	String filesize		= request.getParameter("fsize");
	String umask		= request.getParameter("umask");
	String filetype		= "application/unknown";

	String downFile		= upload_path + "/service/" + umask;

	if (filetype.indexOf("mage")<=0)
		filetype = "application/unknown";
		
	String strClient=request.getHeader("User-Agent");
	
	//경우1
//	if(strClient.indexOf("MSIE 5.5")>-1) 			response.setHeader("Content-Disposition","filename="+filename);
//	else response.setHeader("Content-Disposition","attachment; filename="+filename);
	//
	
	//경우2
	response.setHeader("Content-Type", "application/octet-stream;"); 
	response.setHeader("Content-Disposition", "attachment; filename=" + filename + ";"); 
	//

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
<title>첨부파일다운로드</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
</head>
<body>

</body>
</html>