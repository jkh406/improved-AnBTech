package com.anbtech.gw.db;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.*;
import com.anbtech.gw.*;
import com.anbtech.date.*;
import com.anbtech.file.*;
import com.anbtech.text.*;
import java.lang.SecurityException;
import com.anbtech.util.normalFormat;

import java.util.Properties;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Service;
import com.anbtech.email.emailSend;
	
public class ModuleOffiDocToEmailDAO
{
	// Database Wrapper Class ����
	private Connection con;
	private ArrayList table_list = new ArrayList();					//��������

	private com.anbtech.date.anbDate anbdt = null;					//���� ó��
	private com.anbtech.util.normalFormat nmf = null;				//�������
	private com.anbtech.text.StringProcess str = null;				//���ڿ� ó��
	private com.anbtech.file.FileWriteString write;					//������ ���Ϸ� ���
	private com.anbtech.file.textFileReader read;					//������ ���Ϸ� ���
	private com.anbtech.email.emailSend email;					//email �����ϱ�

	private String strFileName 		= "";		//÷������ ��1
	private String strFileName2 	= "";		//÷������ ��2
    private String strFileName3 	= "";		//÷������ ��3
	private	String strPath 			= "";		//÷������ �ִ� ���丮1
    
    private String strFileFullName 	= "";		//���丮 + ���ϸ� ���� 
    private String strFileFullName2 = "";		//���丮 + ���ϸ� ����2 
    private String strFileFullName3 = "";		//���丮 + ���ϸ� ����3 
          
	private String strSmtpUrl 		= "";		//Host name
	private String addressFrom 		= "";		//������ ��� �ּ� 
	private String nameFrom 		= "";		//������ ��� �̸� 
	private String addressTo 		= "";		//�޴»�� 
	private String strSubject 		= "";		//���� 
	private String strContent 		= "";		//���� 

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public ModuleOffiDocToEmailDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//����ó��
		nmf = new com.anbtech.util.normalFormat("000");		//������ȣ �Ϸù�ȣ
		str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
		write = new com.anbtech.file.FileWriteString();		//������ ���Ϸ� ��� 
		read = new com.anbtech.file.textFileReader();		//������ ���Ϸ� �б�
		email = new com.anbtech.email.emailSend();		//email �����ϱ�
	}

	/***************************************************************************
	 * �������� �б� (id:�����ڵ�,flag:������������,app_date:��������) / �ݿ��ϱ�
	 **************************************************************************/
	public String SendEmailODT(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail�� ������ �����ϱ�
		return msg;
	}

	/***************************************************************************
	 * �系���� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public String SendEmailIDS(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail�� ������ �����ϱ�
		return msg;
	}

	/***************************************************************************
	 * ��ܰ����ۼ� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public String SendEmailODS(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail�� ������ �����ϱ�
		return msg;
	}

	/***************************************************************************
	 * ��ܰ������� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public String SendEmailODR(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail�� ������ �����ϱ�
		return msg;
	}

	/********************************************************************
		���ڸ��� ������
	*********************************************************************/
	public String sendEmail() throws Exception
	{
		//���� ����
		String msg_file = "";							//ó����� ���ڿ������� ������
		String msg = "";								//ó����� ȭ���� ��ũ��Ʈ�� ������
		String user_id = "";							//�����»�� id
		String rec_name = "";							//������ �̸�
		String rec_mail = "";							//������ �� �ּ�
		
		//e-mail���� ���� ���� 
		String fromName = "";							//�����»�� �̸�
		String fromAdd = "";							//�����»�� �ּ�
		String host = "";								//������ �� ������
		String subject = "";							//����
		String content = "";							//����
		
		//1. ���������� �ʿ����� �б�
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			subject = table.getSubject();				//����
			user_id = table.getUserId();				//������ ��� ���
			rec_name = table.getRecName();				//������ �̸�
			rec_mail = table.getRecMail();				//������ �� �ּ�
		}
		//2. �����»���� �̸��� �����¼����� �̸����ּ� �б�
		String[] sendInfo = new String[3];
		sendInfo = getServer(user_id);
		if(sendInfo[0] == null) {
			msg = "������� ���ϰ��� ������ �����ϴ�.	 ���� �̸��������� �� �� �����ϴ�.	 ";
			msg +="���ڿ����� ȯ�漳���� ��ܰ����ۼ����� �ٽ� �����Ͻʽÿ�.";

			//���ڿ������� ������
			//msg_file = "������� ���ϰ��� ������ �����ϴ�.<br> ���� �̸��������� �� �� �����ϴ�.<br>";
			//msg_file +="���ڿ����� ȯ�漳���� ��ܰ����ۼ����� �ٽ� �����Ͻʽÿ�.";
			//recResult(msg_file);		���۰���� ���� ���ڿ������� �����ϱ�
			//System.out.println("msg : " + msg);
			return msg;
		} else {
			fromName = sendInfo[0];				//�����»�� �̸�
			fromAdd = sendInfo[1];				//�����»�� �ּ�
			host = sendInfo[2];					//������ �� ������
		}
		//3.������� �̸� �迭�� ��� (e-mail���۽� �ʿ�ġ ����)
		int cnt = 1;
		for(int i=0; i<rec_name.length(); i++) if(rec_name.charAt(i) == ',') cnt++;
		String[] toName = new String[cnt];
		StringTokenizer r_name = new StringTokenizer(rec_name,",");
		int n = 0;
		while(r_name.hasMoreTokens()) {
			toName[n] = r_name.nextToken();
			n++;
		}
		//4.������� ���ּ� �迭�� ���
		//�޸� �����ڷ� �����ڼ� ã��
		int acnt = 1;
		for(int i=0; i<rec_mail.length(); i++) if(rec_mail.charAt(i) == ',') acnt++;
		//�̸��� ������(@)�� �����ڼ� ã��
		int ecnt = 0;
		for(int i=0; i<rec_mail.length(); i++) if(rec_mail.charAt(i) == '@') ecnt++;
		//�޸������ڰ� �ִ��� �Ǵ��ϱ�
		if(ecnt != acnt) {
			msg = "������ �����ڰ� �����ϴ�. ";
			msg +="�������� �������� ���� �޸�(,)�� �Է��Ͽ� �����Ͽ� �ֽʽÿ�.";

			//���ڿ������� ������
			//msg_file = "������ �����ڰ� �����ϴ�.<br>";
			//msg_file +="�������� �������� ���� �޸�(,)�� �Է��Ͽ� �����Ͽ� �ֽʽÿ�.";
			//recResult(msg_file);		���۰���� ���� ���ڿ������� �����ϱ�
			return msg;
		}
		String[] toAdd = new String[acnt];
		StringTokenizer r_mail = new StringTokenizer(rec_mail,",");
		int m = 0;
		while(r_mail.hasMoreTokens()) {
			toAdd[m] = r_mail.nextToken();
			m++;
		}
		//5.���� �������� �б�(html����)
		content = getContent();

		//6.÷������ �б� [0],[1],[2] : ÷�����ϸ�, [3]:÷������ ���丮
		String[] filename = new String[4];
		filename = getAttachFile();

		//---------------------------------------
		//7. e-mail �����ϱ� 
		//---------------------------------------
		for(int i=0; i<acnt; i++) {
			email.setSmtpUrl(host);				//smtp host��
			email.setFrom(fromAdd);				//������ ��� �ּ�
			email.setFromName(fromName);		//������ ��� �̸�
			email.setTo(toAdd[i]);				//������� �ּ�
			email.setSubject(subject);			//����
			email.setContent(content);			//����

			email.setFileName(filename[0]);		//÷�����ϸ�1
			email.setFileName2(filename[1]);	//÷�����ϸ�2
			email.setFileName3(filename[2]);	//÷�����ϸ�3
			email.setPath(filename[3]);			//÷������ ���丮
			
			//�޽��� ���Ϸ� ������
			String Result = "";
			Result = email.sendMessageHtml();//�޽��� ������

			//�޽��� �����ϱ�
			msg += "["+toAdd[i]+" ���۰��] : "+Result;			//ȭ�� ��ũ��Ʈ�� ������

			//msg_file += "["+toAdd[i]+" ���۰��]<br>"+Result+"<br><br>";	//���ڿ������� ������
		}

		//7.�����̸����� ����� ÷������ �����ϱ� [�ִ� 3��]
		for(int i=0; i<3; i++) {
			String delfile = filename[3]+filename[i];
			read.delFilename(delfile);
		}

		//8.���۰���� ���� ���ڿ������� �����ϱ�
		//recResult(msg_file);

		//9.Ư������ ���ܽ�Ű��
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');

		//System.out.println("msg : " + msg);
		return msg;
	}

	/*********************************************************************
	 	������ �������� �б�
	*********************************************************************/
	public String getContent() throws Exception  
	{
		String content = "";			//���ϰ�
		//-----------------------------------
		// ��������
		//-----------------------------------
		String user_name="";			//����� �̸�
		String doc_id="";				//������ȣ
		String slogan="";				//���ΰ�
		String title_name="";			//�μ� Title��
		String in_date="";				//�������		
		String receive="";				//����
		String reference="";			//����
		String sending="";				//�߽�
		String subject="";				//����
		String address="";				//�߽��� �ּ�
		String tel="";					//��ȭ��ȣ
		String fax="";					//�ѽ���ȣ
		String bon_path="";				//�������� Ȯ��path
		String bon_file="";				//�������� ���ϸ�
		String read_con="";				//��������
		String firm_name="";			//�߽źμ���
		String representative="";		//�߽źμ� ��ǥ��	
		String fname="";				//����:���Ͽ�����	
		String sname="";				//����:���������		
		String ftype="";				//����:����Ȯ���ڸ�	
		String fsize="";				//����:����ũ��
		String[][] addFile;				//÷�ΰ��ó��� ���
		

		//1.��ܰ����ۼ� ���� �б�
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			user_name=table.getUserName();				//����� �̸�
			doc_id=table.getDocId(); 
				if(doc_id == null) doc_id = "";			//������ȣ
			slogan=table.getSlogan();					//���ΰ�
			title_name=table.getTitleName();			//�μ� Title��
			in_date=table.getInDate();					//�������
				in_date = in_date.substring(0,10);
			receive=table.getReceive();;				//����
			reference=table.getReference();				//����
				if(reference == null) reference = "";
			sending=table.getSending();					//�߽�
			subject=table.getSubject();					//����
			address=table.getAddress();					//�߽��� �ּ�
			tel=table.getTel();							//��ȭ��ȣ
			fax=table.getFax();							//�ѽ���ȣ
			bon_path=table.getBonPath();				//�������� Ȯ��path
			bon_file=table.getBonFile();				//�������� ���ϸ�
			firm_name=table.getFirmName();				//�߽źμ���
			representative=table.getRepresentative();	//�߽źμ� ��ǥ��	
		}

		//2.���� ���� �б�
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");			//servlet path
		String img_path = servlet + "/gw/img";
		String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
		read_con = read.getFileString(full_path);

		content+="<html>\n";
		content+="<head>\n";
		content+="<meta http-equiv='Content-Language' content='euc-kr'>\n";
		content+="<title>��ܰ���</title>\n";
		content+="</head>\n";

		content+="<BODY leftmargin='0' topmargin='10' marginwidth='0' marginheight='0'>\n";
		content+="<center>";
		if(slogan.length() > 0)		content+="<font size=3><b>"+slogan+"</b></font><br>\n";
		if(title_name.length() > 0) content+="<font size=6><b>"+title_name+"</b></font><br>\n";
		if(address.length() > 0)	content+="<font size=2>&nbsp;"+address+"&nbsp;</font>\n";
		if(tel.length() > 0)		content+="<font size=2>&nbsp;TEL:"+tel+"&nbsp;</font>\n";
		if(fax.length() > 0)		content+="<font size=2>&nbsp;FAX:"+fax+"&nbsp;</font>\n";
	
		content+="<table width='640' border='0' cellspacing='0' cellpadding='0'>\n";
		//content+="	<tr><td width=100% align='left' height=20><img src='"+img_path+"/slink_logo.jpg' align='middle' border='0'></td>\n";
		content+="	<tr><td width=100% align='left' height=20><font size=3><b>SpaceLink</b></font></td>\n";
		content+="	</tr>\n";
		content+="	<tr><td width=100% align='center' height=2 bgcolor=black colspan=2></td></tr>\n";
		content+="</table>\n";
			
		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n"; 
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>������ȣ :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+doc_id+"</td>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+in_date+"</td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+receive+"</td>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>�� �� �� : </td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+user_name+"</td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</td>\n";
		content+="		<td width='550' height='20' align='left' valign='middle' colspan=3>"+reference+"</td>\n";
		content+="	</tr>\n";
		content+="</table>\n";

		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n";
		content+="	<tr>\n";
		content+="		<td width='100%' height='10' align='left' valign='middle' colspan=2></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='30' align='center' valign='middle'>\n";
		content+="			<font size=3><b>��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�� :</b></font></td>\n";
		content+="		<td width='550' height='30' align='left' valign='middle'>\n";
		content+="			<font size=3><b>"+subject+"</b></font></td>\n";
		content+="	</tr>\n";
		content+="	<tr><td width=100% height='2' align='center' valign='middle' colspan=2 bgcolor=black></td></tr>\n";
		content+="</table>\n";

		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n";
		content+="	<tr>\n";
		content+="		<td width='640' height='10' align='left' valign='top'></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='640' height='560' align='left' valign='top'>\n";
		content+="		<pre>"+read_con+"</pre></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='100%' height='10' align='left' valign='middle' colspan=2></td>\n";
		content+="	</tr>\n";
		content+="</table>\n";

		content+="<font size=6><b>"+firm_name+"</b></font><br>\n";
		content+="<font size=6><b>"+representative+"</b></font>\n";

		content+="</center>\n";
		content+="</body>\n";
		content+="</html>\n";

		return content;
	}

	/*********************************************************************
	 	÷������ ��������
	*********************************************************************/
	public String[] getAttachFile() throws Exception  
	{
		String[] filename = new String[4];			//���ϰ�
		for(int i=0; i<4; i++) filename[i] = "";

		//-----------------------------------
		// ��������
		//-----------------------------------
		String bon_path="";				//�������� Ȯ��path
		String bon_file="";				//�������� ���ϸ�
		String fname="";				//����:���Ͽ�����	
		String sname="";				//����:���������		
		String ftype="";				//����:����Ȯ���ڸ�	
		String fsize="";				//����:����ũ��
		String[][] addFile;				//÷�ΰ��ó��� ���
		

		//1.��ܰ����ۼ� ���� �б�
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			bon_path=table.getBonPath();				//�������� Ȯ��path
			bon_file=table.getBonFile();				//�������� ���ϸ�
			fname=table.getFname();						//���Ͽ�����	
			sname=table.getSname();						//���������
		}

		//2.÷������ path���ϱ�
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		filename[3] = upload_path + bon_path + "/addfile/";
		//System.out.println("path : " + filename[3]);

		//3.÷������ ���ϱ�
		if(fname == null) fname = "";
		int cnt = 0;
		for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

		addFile = new String[cnt][2];
		for(int i=0; i<cnt; i++) for(int j=0; j<2; j++) addFile[i][j]="";

		if(fname.length() != 0) {
			StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
			int m = 0;
			while(f.hasMoreTokens()) {
				addFile[m][0] = f.nextToken();
				addFile[m][0] = addFile[m][0].trim(); 
				if(addFile[m][0] == null) addFile[m][0] = "";
				//System.out.println("fname : " + addFile[m][0]);
				m++;
			}
			StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
			m = 0;
			while(o.hasMoreTokens()) {
				addFile[m][1] = o.nextToken();
				addFile[m][1] = addFile[m][1].trim() + ".bin";			
				if(addFile[m][1] == null) addFile[m][1] = "";
				//System.out.println("sname : " + addFile[m][1]);
				m++;
			}
		}
		//4.÷�������� �����̸����� �ٲٱ�
		for(int i=0; i<cnt; i++) {
			if(addFile[i][0].length() > 3) {
				String orgFile = filename[3]+addFile[i][0];				//������ ���ϸ�
				String savFile = filename[3]+addFile[i][1];				//����� ���ϸ�
				read.fileCopy(savFile,orgFile);							//�������ϸ����� �����Ѵ�.
				filename[i] = addFile[i][0];							//������ ���ϸ� ���Ϲ迭�� ���
			}
		}

		return filename;
	}

	/*********************************************************************
	 	������ ���ϼ��� ���� �о����
	*********************************************************************/
	public String[] getServer(String login_id) throws Exception  
	{
		String[] rtn = new String[3];

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select name,address,sserver from emailInfo where id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			rtn[0] = rs.getString("name");			//������ ����̸�
			rtn[1] = rs.getString("address");		//������ ��� �̸��� �ּ�
			rtn[2] = rs.getString("sserver");		//������ ���ϼ���
		}
		stmt.close();
		rs.close();

		return rtn;
	}

	/*********************************************************************
	 	e-mail�� ��������� ������� ���ڿ��� ���������� �ޱ�
	*********************************************************************/
	public void recResult(String msg) throws Exception 
	{	
		String pid = getID();								//������ȣ
		String subject = "";								//����
		String user_id = "", user_name = "", rec = "";		//����� ���,�̸�
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		//1.����� ���� �˾ƺ���
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			user_id = table.getUserId();					//����� ���
			user_name = table.getUserName();				//����� �̸�
			subject = table.getSubject()+" [�̸������� ���]";//����
		}
		rec = user_id+"/"+user_name+";";					//������
		String bon_path = "/post/"+user_id+"/text_upload";	//�����н�
		String filename = pid;								//�������� ���ϸ�

		//2.���ڿ������� ������
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+""+"','"+"���ڰ��� ���α���"+"','"+write_date+"','"+user_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + "" + "','" + "���ڰ��� ���α���" + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		stmt.executeUpdate(master);

		//3.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>�����̸������� ���</title></head>";
			content += "<body>";
			content += "<h3>���� �̸��� ���� ���</h3><br>";
			content += msg;
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		stmt.close();
	}

	/******************************************************************************
	// ID�� ���ϴ� �޼ҵ�
	******************************************************************************/
	private String getID()
	{
		String ID;
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);
		nmf.setFormat("000");		//�Ϸù�ȣ ��� ����(6�ڸ�)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}

}