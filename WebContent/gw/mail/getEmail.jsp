<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "mail system"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="java.io.*"
	import="java.sql.*"
	import="java.text.*"
	import="javax.servlet.*"
	import="javax.servlet.http.*"
	import="com.anbtech.gw.email.*"
	
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
<%@	page import="com.anbtech.gw.email.emailReceive"	%>
<%@	page import="com.anbtech.file.FileWriteString"	%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//��������
	String id = "";			//������ id

	String pid = "";		//������ȣ
	String name = "";		//������ �̸�
	String division = "";		//������ �μ���
	String tel = "";		//������ ��ȭ��ȣ
	//�޾ƿ��¸��ϼ��� ����ȯ�溯��
	String[] prototype;		//�޾ƿ��� protocaltype
	String[] hostname;		//�޾ƿ��� ���ϼ�����
	String[] username;		//���ϰ��� id
	String[] password;		//���ϰ��� ��й�ȣ
	String[] readtype;		//���ڸ��ϰ������� �����¸��� ��������(READ_ONLY:RO, READ_WRITE:RW)	

	//email�޾ƿ��� ����
	String from = "";		//���� ��� �ּ� �� �̸�
	String sent_date = "";		//���� ���� 
	String subject="";		//���� ���� 
	String content="";		//���� ���� 
	String FileName="";		//÷������ 

	String pad1o = "";		//÷�ε� ���ϸ�1 �����̸�	
	String pad1f = "";		//÷�ε� ���ϸ�1
	String pad2o = "";		//÷�ε� ���ϸ�2 �����̸�	
	String pad2f = "";		//÷�ε� ���ϸ�2
	String pad3o = "";		//÷�ε� ���ϸ�3 �����̸�	
	String pad3f = "";		//÷�ε� ���ϸ�3	
	String pad4o = "";		//÷�ε� ���ϸ�4 �����̸�	
	String pad4f = "";		//÷�ε� ���ϸ�4
	String pad5o = "";		//÷�ε� ���ϸ�5 �����̸�	
	String pad5f = "";		//÷�ε� ���ϸ�5

	//������ ���ϸ� �� ������丮
	String bonfn;			//������ �������ϸ� 
	String addpath;			//÷������ ������ path
	String textpath;		//�������� ������ path
%>

<HTML><HEAD><TITLE>�ܺθ��� ����(���)</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_e.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">����޽���</td>
           <td width="80%" height="25" colspan="3" class="bg_02">
				<textarea cols=45 rows=10>
<%

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	emailReceive email = new com.anbtech.gw.email.emailReceive();			//email��������

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = sl.id; 		//������ login id

	String[] idColumn = {"a.id","a.name","a.office_tel","b.ac_name"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//������ ��
		division = bean.getData("ac_name");		//������ �μ���
		tel = bean.getData("office_tel");			//������ ��ȭ��ȣ
	} //while
	
	/*********************************************************************
	 	���ϼ��� �����ϱ����� �������� ��������
	*********************************************************************/	
	String[] emailColumn = {"id","rtype","rserver","loginid","loginpwd","readtype"};
	bean.setTable("emailInfo");			
	bean.setColumns(emailColumn);
	bean.setOrder("id ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();

	//�迭 �ʱ�ȭ
	int cnt = bean.getTotalCount();
	prototype = new String[cnt];					//�������� type
	hostname = new String[cnt];						//�޴� ���ϼ�����
	username = new String[cnt];						//����� ID
	password = new String[cnt];						//����� ��й�ȣ
	readtype = new String[cnt];						//���ڸ��Ͽ��� ������ ���� ��������

	int ei = 0;
	while(bean.isAll()) {
		prototype[ei] = bean.getData("rtype");		//�޾ƿ��� ���ϼ��� protocol  (pop3 or imap)
		hostname[ei] = bean.getData("rserver");		//�޾ƿ��� ���ϼ�����			
		username[ei] = bean.getData("loginid");		//���ϰ��� id	
		password[ei] = bean.getData("loginpwd");	//���ϰ��� ��й�ȣ		
		readtype[ei] = bean.getData("readtype");	//��������(READ_ONLY:RO, READ_WRITE:RW)
		ei++;
	} //while
	
	/*********************************************************************
	 	���ϼ��� �����ϱ�
	*********************************************************************/	

int si = 0;
nextRow :	
for(int hi = hostname.length; si < hi; si++) {
	if((hostname[si] != null) && (username[si] != null) && (password[si] != null)){ 
		email.setHost(hostname[si]);
		email.setUserName(username[si]);
		email.setPassWord(password[si]);
	}

	//prototype ��������
	String proto="";
	if(prototype[si] != null){
		if(prototype[si].equals("POP3")) proto = "pop3";
		else if(prototype[si].equals("IMAP")) proto = "imap";
	} else proto = "pop3";
	email.setProtocol(proto);

	//�������� �������� ��������
	String rwtype="RW";
	if((readtype[si] != null) || (readtype[si].length() != 0))
		rwtype = readtype[si];
	else rwtype = "RW";
	email.isDelete(rwtype);	
	
	//�޴� ���ϼ��� �������
	try {
		String Result = email.getConnect();		//���ϼ����� �����Ѵ�.
		out.println("�޴� ���� ������ : " + hostname[si]);
		out.println("���� ��� -------- " + Result + "\n");
	} catch (Exception e) {
		out.println("�޴� ���� ������ : " + hostname[si]);
		out.println(e);
		continue nextRow;		//���̺�� ��ҷ� �̵��Ѵ�.
	}

	//====================================================================
	// 	���ϳ��� �з��ϱ�
	//====================================================================
	//������ �������ϸ�
	bonfn = bean.getID() + si;		//�ߺ������� ����
				
	//����� �������� ���丮
	String contentDir = upload_path + crp + "/email/" + id + "/text";	//���� Dir     (window�� ���)
	text.setFilepath(contentDir);	//���丮 ������ �����
	email.setTextPath(contentDir);		

	//����� ÷������ ���丮
	String addDir = upload_path + crp + "/email/" + id + "/addfile";	//���� Dir     (window�� ���)
	text.setFilepath(contentDir);	//���丮 ������ �����
	email.setAddPath(addDir);	
  
	//�޽��� ���� �ľ��ϱ� 
	int mcnt = email.getMailCount();			

	//�̹� �޾ƿ� �������� ���θ� �˷��ִ� ����
	String flag = "NEW";		//���ο� email  , "OLD"�� �̹� ������  ����	 
			
	//-----------------------------------------------
	//	�޽��� ó���ϱ�
	//-----------------------------------------------
	//ó���� �޽����� ����  
	if(mcnt == 0) { 			
		email.close();	//�����ϱ� 
	//�޽��� ó�� �ϱ� 		
	} else {       								
		for(int i = 1; i <= mcnt; i++) {
			//0.���ú��� �ʱ�ȭ �ϱ�
			from=sent_date=subject=content=FileName="";
			pad1o=pad1f=pad2o=pad2f=pad3o=pad3f=pad4o=pad4f=pad5o=pad5f="";

			//0.������ȣ ����
			pid = bean.getID() + i;		//�ߺ��� ���ϱ� ����

			//0. ���ϸ� �ϳ��� �����ͼ� ó���ϱ�
			Message msg = email.getMail(i);

			//1.��������ּ�(��������̸�) �˾ƺ��� 
			String fromName = msg.getFrom()[0].toString();
			String fname = email.encordingName(fromName); //���ڵ��� ���ڵ��ϱ�	
			if(fname.equals("NOENC")) 
				from = new String(fromName.getBytes("8859_1"),"euc-kr");
			else
				from = fname; 

			//	DB������ ���� Ư������ �ٲٱ� (' -> `) 
			from = str.quoteReplace(from);		
			from = from.replace('"',' ');		
			from = from.replace('<','(');	
			from = from.replace('>',')');
			from = from.trim();

			//2.�������� �˾ƺ��� 
			java.util.Date now = msg.getSentDate();
			if(now != null) {
				java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd  HH:mm");
				sent_date = vans.format(now);
			}

			//-------------------------------------------
			//	�̹� �޾ƿ� �������� �ľ��ϱ�
			//--------------------------------------------
			String[] mailColumn = {"writer_id","write_date","post_receiver"};
			bean.setTable("POST_LETTER");			
			bean.setColumns(mailColumn);
			bean.setOrder("post_receiver ASC");	
			bean.setSearch("writer_id",hostname[si],"write_date",sent_date,"post_receiver",id);			
			bean.init_unique();

			int rcnt = bean.getTotalCount();			
			if(rcnt == 0) flag = "NEW";		//���ο� ����
			else flag = "OLD";			//�̹� �޾ƿ�
	
			//3.���� �˾ƺ���
			String title = msg.getSubject();
			subject = email.encordingSubject(title);
			if(subject.length() == 0) subject = "�������";

			//	DB������ ���� Ư������ �ٲٱ� (' -> `) 
			subject = str.quoteReplace(subject);		
			subject = subject.replace('<','(');	
			subject = subject.replace('>',')');	
			
			//4.������ �������� �������ϸ� (�ߺ��� ���ϱ� ���� ������)
			String bon_filename = bonfn + i + ".html";
				
			//5.������ ÷������ �������ϸ� (�ߺ��� ���ϱ� ���� ������)
			String add_filename = bonfn + i;
				
			//6-1.÷������ �� ���� �˾ƺ��� 
			if(msg.isMimeType("multipart/*")) {			//÷������ ó�� �ϱ� 
				Multipart multipart = (Multipart)msg.getContent();
				int m = multipart.getCount();					//÷������ ���� 
				//out.println("÷������ ���� = " + m);
				for(int j = 0; j < m; j++) {					
					Part part = multipart.getBodyPart(j);
					String disposition = part.getDisposition();
					
					//out.println("disposition = " + disposition);						
					//���� ���� ( j=0 : null --> ���� ����)
					if(disposition == null) {
						//6-1-1.���� ���Ϸ� ���� (Stream���� ó�� �Ͽ� �ѱ� ���� ����)
						String pbon = msg.getContent().toString();	//���ڵ����� �˱�����
						InputStream inFile = part.getInputStream();   //������ stream���·� ���� 
						if(flag.equals("NEW"))		//���ο� �����̸� ����
							email.saveTextInputStream(bon_filename,inFile,pbon);
					} 
						
					//÷������ �б� (j=1 : attachement ÷������1 , - - , j=3 : attachement ÷������3)
					if(disposition != null && (disposition.equals(part.ATTACHMENT) || disposition.equals(part.INLINE))) {
						
						//6-1-2.÷�����ϸ� 
						String FN = part.getFileName();	
						//out.println("FN = " + FN);
						if(FN != null) {				//÷�������� null�� �ƴѰ�츸 ó����				
							//÷�����ϸ� �ѱ�ó���ϱ�
							FileName = "";	
							String adname = email.encordingName(FN); //���ڵ��� ���ڵ��ϱ�	
							if(adname.equals("NOENC")) 
								FileName = new String(FN.getBytes("8859_1"),"euc-kr");
							else
								FileName = adname;
							//out.println("÷�����ϸ� = " + FileName);
							//������ ���� ÷�����ϸ� ������ ���
							if(j == 1) pad1o = FileName;
							else if(j == 2) pad2o = FileName;
							else if(j == 3) pad3o = FileName;
							else if(j == 4) pad4o = FileName;
							else if(j == 5) pad5o = FileName;

							
							//÷������ Ȯ���ڸ� ���ϱ� 
							String extFileName = "";
							if(FileName.length() > 0) {
								extFileName = FileName.substring(FileName.indexOf("."),FileName.length());
							}
	
							//6-1-3.÷������ �����ϱ� 
							if(FileName.length() > 0) {
								BufferedInputStream in = new BufferedInputStream(part.getInputStream());
								if(flag.equals("NEW"))		//���ο� �����̸� ����
									email.saveAddInputStream(add_filename,extFileName,j,in);	

								//���� ÷�����ϸ�
								if(j == 1) pad1f = add_filename + "_1" + extFileName;
								else if(j == 2) pad2f = add_filename + "_2" + extFileName;
								else if(j == 3) pad3f = add_filename + "_3" + extFileName;
								else if(j == 4) pad4f = add_filename + "_4" + extFileName;
								else if(j == 5) pad5f = add_filename + "_5" + extFileName;						
							} //if	(÷����������)
						}// if (÷�����ϸ��� null�� �ƴϸ�)							
					} //if (÷������ ó��)

				} //for
			//6-2.÷������ ���� 
			} else {											
				FileName = "";
				//6-2-1.���� ���Ϸ� ���� (Stream���� ó�� �Ͽ� �ѱ� ���� ����) 
				String nbon = msg.getContent().toString();	//���ڵ����� �˱�����
				InputStream inFile = msg.getInputStream();
				if(flag.equals("NEW"))		//���ο� �����̸� ����
					email.saveTextInputStream(bon_filename,inFile,nbon);	

			} //if
				
			//���� ��� �ϱ� 
			if(flag.equals("NEW")) {
				out.println("�ۼ����̸� = " + from);
				out.println("Mail���� = " + subject);
				out.println("�ۼ����� = " + sent_date);
				out.println("");	
			}

			//�����ϱ����� DELETED��ũ �ϱ�
			email.markDeleted();
			
			//-------------------- DB�� �����ϱ� --------------------------------//
			if(flag.equals("NEW")) {
				//��Ÿ ����
				String recIDs = name + "/" + id + ";";				//�޴��� (post_master�� ��Ͻ�)
				String rec = id;									//�޴��� (post_letter�� ��Ͻ�)
				String del_date = bean.getMonthNoformat("6");		//���������� (6������ : post_letter)
				String del_year = bean.getYearNoformat("1");		//���������� (1����   : post_master) 
				String conDir = "/email/" + id + "/text";			//����path

				//window POST_LETTER�����ϱ�
				String inputs="";
				inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
				inputs += pid + "','" + subject + "','" + hostname[si] + "','" + from + "','" + sent_date + "','" + rec + "','" + "0" + "','" + del_date + "')";
				bean.execute(inputs);
				//out.println("inputs : " + inputs + "<br>");
			
				// POST_MASTER����
				String m_inputs="";
				m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
				m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
				m_inputs += pid + "','" + subject + "','" + hostname[si] + "','" + from + "','" + sent_date + "','" + recIDs + "','" + "0" + "','";
				m_inputs += "email" + "','" + conDir + "','" + bon_filename + "','" + pad1o + "','" + pad1f + "','";
				m_inputs += pad2o + "','" + pad2f + "','" + pad3o + "','" + pad3f + "','" + del_year + "')";
				bean.execute(m_inputs);
				//out.println("m_inputs : " + m_inputs + "<br>");
			} //if (����)

		} //for	(�ϳ��� ���ڸ��� ���� ���ϰ����� ó���ϱ�)	

		//���� �ϱ� (readtype������ �����¸��� �������� �����ϱ�) 
		email.close();				
	} //if (������ �ִ°�� ó��)

} //for (���ڸ��ϰ����� ��ü����ó��)

%>
				</textarea>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:dclose();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>


<script>
<!--
function dclose()
{	
	//opener.view.location.reload();
	opener.parent.up.location.reload();
	opener.location.reload();
	self.close();
}

function centerWindow() 
{ 
        var sampleWidth = 610;                        // �������� ���� ������ ���� 
        var sampleHeight = 410;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

-->
</script>


