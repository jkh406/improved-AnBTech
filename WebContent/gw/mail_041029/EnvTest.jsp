<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "mail system test"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="java.io.*"
	import="java.sql.*"
	import="java.text.*"
	import="javax.servlet.*"
	import="javax.servlet.http.*"
	import="com.anbtech.email.emailSend"
	import="com.anbtech.gw.email.emailReceive"
%>
<%@	page import="java.io.BufferedInputStream"	%>
<%@	page import="java.io.FileOutputStream"		%>
<%@	page import="java.io.InputStream"		%>
<%@	page import="javax.mail.Message"		%>
<%@	page import="javax.mail.Multipart"		%>
<%@	page import="javax.mail.Part"			%>
<%@ page import="javax.mail.internet.MimeUtility"	%>
<%@	page import="javax.activation.DataSource"	%>
<%@	page import="javax.activation.URLDataSource"	%>
<%@	page import="javax.activation.DataHandler"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%!
	//�޽��� ���޺���
	String Message="";		//�޽��� ���� ����  
%>

<HTML><HEAD><TITLE>�����˻�(���)</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_p.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="25%" height="25" class="bg_01" background="../images/bg-01.gif">����޽���</td>
           <td width="75%" height="25" class="bg_02">
				<textarea cols=35 rows=5>
<%
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	emailSend smtp = new com.anbtech.email.emailSend();						//email������ �˻�
	emailReceive email = new com.anbtech.gw.email.emailReceive();				//email�������� �˻�
	

	/*********************************************************************
	 	���ϼ��� �����ϱ����� �������� ��������
	*********************************************************************/	
	String inmail = request.getParameter("INF");
	String prototype = "";					//�޴� �������� type
	String hostname = "";					//�޴� ���ϼ�����
	String username = "";					//����� ID
	String password = "";					//����� ��й�ȣ
	String smtphost = "";					//������ ���ϼ�����
	String useraddress = "";				//����� �̸����ּ�
	String sno = "";						//�⺻���� ������ ���ϼ�������(0:�����߱⺻��)
	if(inmail != null) {
		StringTokenizer str = new StringTokenizer(inmail,";");
		int cnt = str.countTokens();
		int i = 0;
		while(str.hasMoreTokens()){
			if(i == 0) prototype = str.nextToken();
			else if(i == 1) hostname = str.nextToken();
			else if(i == 2) username = str.nextToken();
			else if(i == 3) password = str.nextToken();
			else if(i == 4) smtphost = str.nextToken();
			else if(i == 5) useraddress = str.nextToken();
			else if(i == 6) sno = str.nextToken();
			i++;
		} //while
	} //if
	//out.println(inmail + ":"+prototype+"-"+hostname+"-"+username+"-"+password+"-"+smtphost+"-");

	/*********************************************************************
	 	�޴� ���ϼ��� �����Ͽ� �˻��ϱ�
	*********************************************************************/	
	email.setHost(hostname);				//�޴� ���ϼ�����
	email.setUserName(username);			//����� ID
	email.setPassWord(password);			//����� ��й�ȣ

	String proto="pop3";
	if(prototype != null){
		if(prototype.equals("POP3")) proto = "pop3";
		else if(prototype.equals("IMAP")) proto = "imap";
	} else proto = "pop3";
	email.setProtocol(proto);			//�������� typ
	email.isDelete("RO");					//�����б� ���� (READ_ONLY:RO, READ_WRITE:RW)
	
	//�޴� ���ϼ��� �������
	try {
		String Result = email.getConnect();
		email.close();
		out.println("�޴¸��ϼ��� �������� ----- " + Result);
	} catch (Exception e) {
		out.println("�޴¸��ϼ��� �������� Error�߻� ");
		out.println(e);
	}

	/*********************************************************************
	 	������ ���ϼ��� �����Ͽ� �˻��ϱ�
	*********************************************************************/	
	if(sno.equals("0")) {
		String sub = "�ܺθ��� ���� �׽�Ʈ �޽���";
		String smg = "SMTP���� ������ �׽�Ʈ�ϴ� ���� �ڵ����� ���� ���� ���� �޽����Դϴ�.";
		try {
			String Result = smtp.SetupMailServer(smtphost,useraddress,sub,smg);
			out.println("�����¸��ϼ��� �������� ----- " + Result);
		} catch (Exception e) {
			out.println("�����¸��ϼ��� �������� Error�߻� ");
			out.println(e);
		}
	}
%>
				</textarea>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:self.close()'><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>