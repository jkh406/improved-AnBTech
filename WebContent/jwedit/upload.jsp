<%---------------------------------------------------------------------
 * Author     : 변기중 (and32@nate.com) 
 * Homepage   : http://www.diane.pe.kr
 * Created on : 2003. 4. 4. 
 * Description: edit.jsp에서 전송된 이미지를 처리합니다.
---------------------------------------------------------------------%>

<%@ page contentType="text/html;charset=MS949"%>
<%@ page import="java.io.*" %>
<%@ page import="com.diane.web.*" %>

<%
	String curPath = application.getRealPath(request.getServletPath());

	// 윈도우즈와 유닉스상의 경로 문자열을 통일하기 위해서 '\' 를 '/'로 바꿉니다.
	curPath = curPath.replaceAll("\\\\{1}", "/");

	// upload.jsp가 위치한 절대 경로를 구합니다.
	curPath = curPath.substring(0, curPath.lastIndexOf("/"));

	String imgPath = curPath + "/upload_img";
	System.out.println("Upload Path : " + imgPath);

	// JWSoft의 웹편집기에서 전송되는 데이터는 Multipart Request타입으로 o'reilly 의 MultipartRequest를
	// 이용하여 데이터를 추출합니다.
	MPartRequest mpartReq = new MPartRequest(request, 50 * 1024 * 1024);	// 기본 허용 용량을 50M byte 로 설정
	File file = mpartReq.getFile("file_attach", imgPath);					// 중복된 파일명은 자동으로 처리됩니다.
	System.out.println("File Name : " + file.getName());

	out.print(file.getName());
%>