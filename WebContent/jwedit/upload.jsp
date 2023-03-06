<%---------------------------------------------------------------------
 * Author     : ������ (and32@nate.com) 
 * Homepage   : http://www.diane.pe.kr
 * Created on : 2003. 4. 4. 
 * Description: edit.jsp���� ���۵� �̹����� ó���մϴ�.
---------------------------------------------------------------------%>

<%@ page contentType="text/html;charset=MS949"%>
<%@ page import="java.io.*" %>
<%@ page import="com.diane.web.*" %>

<%
	String curPath = application.getRealPath(request.getServletPath());

	// ��������� ���н����� ��� ���ڿ��� �����ϱ� ���ؼ� '\' �� '/'�� �ٲߴϴ�.
	curPath = curPath.replaceAll("\\\\{1}", "/");

	// upload.jsp�� ��ġ�� ���� ��θ� ���մϴ�.
	curPath = curPath.substring(0, curPath.lastIndexOf("/"));

	String imgPath = curPath + "/upload_img";
	System.out.println("Upload Path : " + imgPath);

	// JWSoft�� �������⿡�� ���۵Ǵ� �����ʹ� Multipart RequestŸ������ o'reilly �� MultipartRequest��
	// �̿��Ͽ� �����͸� �����մϴ�.
	MPartRequest mpartReq = new MPartRequest(request, 50 * 1024 * 1024);	// �⺻ ��� �뷮�� 50M byte �� ����
	File file = mpartReq.getFile("file_attach", imgPath);					// �ߺ��� ���ϸ��� �ڵ����� ó���˴ϴ�.
	System.out.println("File Name : " + file.getName());

	out.print(file.getName());
%>