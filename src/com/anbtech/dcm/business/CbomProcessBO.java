package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomProcessBO
{
	private Connection con;
	private com.anbtech.dcm.db.CbomModifyDAO cmodDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();				//�����Է�
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();//������ ���Ϸ� ���
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public CbomProcessBO(Connection con) 
	{
		this.con = con;
		cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		��� ���� å������ ��Ȱ�� ���� �޼ҵ� 
	//			1. ������� å���� ����
	//			2. ECR���� ��û���� �ݷ�
	//			3. ����������� ����
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* ������� å���� ����
	 *******************************************************************/
	 public String changeMgr(String pid,String mgr_id,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//������� å���� ���� ������ ���¸� �˻��Ѵ�. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("3")) {
			data = "������� å���� �����϶� å���� ������ ���� �� �� �ֽ��ϴ�.";
			//System.out.println(data);
			return data;
		}

		//å���� ���� �˱� : ���,�̸�,�μ������ڵ�,�μ��ڵ�,�μ���,��ȭ��ȣ
		String mgr_info[] = new String[6]; 
		mgr_info = cmodDAO.getUserData(mgr_id);

		//ECC_COM�� �����ϱ�
		update = "UPDATE ecc_com set mgr_id='"+mgr_id+"',mgr_name='"+mgr_info[1];
		update += "',mgr_code='"+mgr_info[2]+"',mgr_div_code='"+mgr_info[3];
		update += "',mgr_div_name='"+mgr_info[4]+"' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "���������� ������� å���ڰ� ����Ǿ����ϴ�.";
		//System.out.println(data);

		//���ڿ������� å���� ��������� �˷��ش�.
		String title = "������� å���� ����";
		sendMail(title,user_id,user_name,note,mgr_id,mgr_info[1]);

		return data;
	} 

	/*******************************************************************
	* ECR���� ��û���� �ݷ�
	 *******************************************************************/
	 public String rejectMgr(String pid,String eco_id,String eco_name,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//������� å���� ���� ������ ���¸� �˻��Ѵ�. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		
		//ECR �ݷ� : �����, �������å���ڹݷ�, ����������ڹݷ�
		if(ecc_status.equals("2") || ecc_status.equals("3") || ecc_status.equals("4")) {
			update = "UPDATE ecc_com set ecc_status='0' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
			data = "���������� ECR�� �ݷ��Ǿ����ϴ�.";
		}
		//ECO �ݷ� : �����, ��������ڹݷ�
		else if(ecc_status.equals("7") || ecc_status.equals("9")) {
			update = "UPDATE ecc_com set ecc_status='5' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
			data = "���������� ECO�� �ݷ��Ǿ����ϴ�.";
		}
		//�׿ܴ� �ݷ�ó�� �ʵ�
		else {
			data = "�ݷ� �� �� ���� �����Դϴ�.";
			return data;
		}

		//���ڿ������� å���� ��������� �˷��ش�.
		String title = "ECR �ݷ�";
		sendMail(title,user_id,user_name,note,eco_id,eco_name);

		return data;
	} 

	/*******************************************************************
	* ����������� ����
	 *******************************************************************/
	 public String setUser(String pid,String eco_id,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//������� å���� ���� ������ ���¸� �˻��Ѵ�. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("3")) {
			data = "������� å���� �����϶� ����������ڸ� ���� �� �� �ֽ��ϴ�.";
			//System.out.println(data);
			return data;
		}

		//����������� ���� �˱� : ���,�̸�,�μ������ڵ�,�μ��ڵ�,�μ���,��ȭ��ȣ
		String eco_info[] = new String[6]; 
		eco_info = cmodDAO.getUserData(eco_id);

		//ECC_COM�� �����ϱ�
		update = "UPDATE ecc_com set eco_id='"+eco_id+"',eco_name='"+eco_info[1];
		update += "',eco_code='"+eco_info[2]+"',eco_div_code='"+eco_info[3];
		update += "',eco_div_name='"+eco_info[4]+"',eco_tel='"+eco_info[5];
		update += "',ecc_status='4' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "���������� ����������ڿ��� ���۵Ǿ����ϴ�.";
		//System.out.println(data);

		//���ڿ������� å���� ��������� �˷��ش�.
		String title = "����������� ����";
		sendMail(title,user_id,user_name,note,eco_id,eco_info[1]);

		return data;
	} 

	//--------------------------------------------------------------------
	//
	//		���ڿ������� ���� ������
	//			
	//
	//---------------------------------------------------------------------
	/*********************************************************************
	// 	���ڿ��� ���� ���뺸����  : ECR�� ���� å����,����� ���Ͻ�
	*********************************************************************/
	public void sendMail(String title,String user_id,String user_name,String note,
				String rec_id,String rec_name) throws Exception 
	{	
		String pid = anbdt.getID();							//������ȣ
		String subject=title,rec="";						//����,������ ����
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		//1.������ ���� �˾ƺ���
		rec = rec_id+"/"+rec_name+";";						//������
		String bon_path = "/post/"+rec_id+"/text_upload";	//�����н�
		String filename = pid;								//�������� ���ϸ�

		//2.���ڿ������� ������
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		cmodDAO.executeUpdate(letter);

		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		cmodDAO.executeUpdate(master);

		//3.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>�˸�</title></head>";
			content += "<body>";
			content += "<h3>"+title+"</h3><br>";
			content += note;
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�
	}

	/*********************************************************************
	// 	���ڿ��� ���� ���뺸����  : ECO AUDIT �� BOM����ó ����
	//  eco_pid : ECC_COM�� ������ȣ, receivers : �����ڵ�, note : ��Ÿ�ǰ�
	*********************************************************************/
	public void sendAuditMail(String server_url,String title,String user_id,String user_name,
			String eco_pid,String eco_no,String receivers,String note) throws Exception 
	{	
		String pid = anbdt.getID();							//������ȣ
		String subject=title;								//����
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		String rec_id="",rec_name="";						//�����ڻ��,�̸�
		
		//1.�ش�Ǵ� ���������� ��ŭ ������
		StringTokenizer list = new StringTokenizer(receivers,"\n");
		while(list.hasMoreTokens()) {
			//������ ���� ����� �̸����� ����
			String rec = list.nextToken();
			if(rec.indexOf("/") == -1) return;
			rec_id = rec.substring(0,rec.indexOf("/"));
			
			//���� ������ ó��
			String letter="";
				letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
				letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";		
			cmodDAO.executeUpdate(letter);
		} //while

		//2.��ü ������ �ۼ�
		String bon_path = "/post/"+rec_id+"/text_upload";	//�����н�
		String filename = pid;								//�������� ���ϸ�

		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + receivers + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		cmodDAO.executeUpdate(master);

		//3.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
//		String url = server_url + "/servlet/CbomProcessServlet?mode=ecc_app_mail&pid="+eco_pid+"&eco_no="+eco_no;
//		String link = "<a href=\"#\" onclick=\"javascript:window.open('" + url;
//			  link += "','audit_view','width=800px,height=600px,scrollbars=yes,toolbar=no,status=yes,resizable=no');\">�󼼳��뺸��</a>";
		String url = "../../../../../../servlet/CbomProcessServlet?mode=ecc_app_mail&pid="+eco_pid+"&eco_no="+eco_no;
		String link = "<a href=\"#\" onclick=\"javascript:wopen('" + url;
			  link += "','audit_view','800','600','scrollbars=yes,toolbar=no,status=yes,resizable=no');\">�󼼳��뺸��</a>";
		String content = "<html><head><title>�˸�</title>";
			content += "<META http-equiv=Content-Type content=\"text/html; charset=euc-kr\">";
			content += "</head>";
			content += "<body>";
			content += note+"<br><br><br><br>";
			content+= "	  <p align='center'>" + link + "</td>";
			content += "</body></html>\n";
			content += "<script language=javascript>\n";
			content += "<!-- \n";
			content += "function wopen(url, t, w, h, s) { \n";
			content += "var sw; \n";
			content += "var sh; \n";
			content += "sw = (screen.Width - w) / 2; \n";
			content += "sh = (screen.Height - h) / 2 - 50; \n";
			content += "window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);\n";
			content += "} \n";
			content += " -->\n";
			content += "</script>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�
		
	}

}
