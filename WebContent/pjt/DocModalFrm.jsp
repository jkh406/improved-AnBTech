<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "����� ���� ó��"	
	contentType = "text/html; charset=euc-kr" 	
	import="java.io.*"
	import="java.util.*"
%>

<%
	//--------------------------
	//���� ����
	//--------------------------
	String strSrc = "";				//���α׷� ��
	String mode = "";
	String id = "";					//������ ������ȣ
	String doc_id = "";				//���ڰ��� ������ȣ
	String no = "";					//�Խ����� ������ȣ

	//--------------------------
	//���޺��� �б�
	//--------------------------
	strSrc = request.getParameter("strSrc");	if(strSrc == null) strSrc = "";
	if(strSrc.indexOf("Servlet") != -1)			strSrc = "../servlet/"+strSrc;	//���긴���� �б�
	else if(strSrc.indexOf("write.jsp") != -1)	strSrc = strSrc;				//�ۼ� jsp�� �б�
	else if(strSrc.indexOf("App.jsp") != -1) 
						strSrc = "../gw/approval/module/"+strSrc;				//��� jsp�� �б�

	mode = request.getParameter("mode");		if(mode == null) mode = "";
	id = request.getParameter("id");			if(id == null) id = "";
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";
	no = request.getParameter("no");			if(no == null) no = "";
//out.println(mode+":"+id+":"+doc_id+":"+no);

%>

<HTML><HEAD><TITLE>xptm</TITLE>

<BODY style="PADDING-RIGHT: 0px; PADDING-LEFT: 0px; PADDING-BOTTOM: 0px; MARGIN: 0px; PADDING-TOP: 0px" scroll=no>

<IFRAME id=iframe style="BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; BORDER-RIGHT-WIDTH: 0px" src="<%=strSrc%>?id=<%=id%>&mode=<%=mode%>&doc_id=<%=doc_id%>&no=<%=no%>" width="720" height="750"></IFRAME>

</BODY>
</HTML>

