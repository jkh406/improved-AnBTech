package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.file.textFileReader;

public class AppTmpDeleteDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private String login_id = "";		//������ ���
	private String root_path = "";		//÷������ root directory
	private String tablename = "";		//table name
	private String bon_path = ""; 		//����path									
	private String bon_file = ""; 		//�������ϸ�
	private String add_f1 = "";			//÷��1
	private String add_f2 = "";			//÷��2
	private String add_f3 = "";			//÷��3
	private String flag = "";			//���ڰ��� ����
	private String plid = "";			//�������ڰ��� ������ȣ

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppTmpDeleteDAO(Connection con) 
	{
		this.con = con;
	}

	public AppTmpDeleteDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//*******************************************************************
	//	�־��� ������ȣ�� �ʿ䳻�� ã�� ���ó��� �����ϱ�
	//*******************************************************************/	
	public void deletePid (String pid,String id,String root_path) throws Exception
	{
		//���� �ʱ�ȭ
		if(pid == null) pid = "";			//������ȣ
		this.login_id = id;					//���
		this.root_path = root_path;			//÷������ ���� root path

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		String query  = "SELECT * FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			this.bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //����path									
			this.bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = ""; //�������ϸ�
			this.add_f1 = rs.getString("add_1_file");	if(add_f1 == null) add_f1 = "";		//÷��1
			this.add_f2 = rs.getString("add_2_file");	if(add_f2 == null) add_f2 = "";		//÷��2
			this.add_f3 = rs.getString("add_3_file");	if(add_f3 == null)	add_f3 = "";	//÷��3		
			this.plid = rs.getString("Plid");			if(plid == null)	plid = "";		//���ù��� ������ȣ	
			this.flag = rs.getString("flag");			if(flag == null)	flag = "";		//���ù��� ����	
		}
		//���ó��� ����ϱ�
/*		//System.out.println("bon_path : " + this.bon_path);
		//System.out.println("bon_file : " + this.bon_file);
		//System.out.println("add_f1 : " + this.add_f1);
		//System.out.println("add_f2 : " + this.add_f2);
		//System.out.println("add_f3 : " + this.add_f3);
		//System.out.println("plid : " + this.plid);
		//System.out.println("flag : " + this.flag);
*/
		//���ù��� ������ �����Ͽ� Table����,÷������,�������ϵ� �����ϱ�
		//�Ϲݹ���
		if(flag.equals("GEN")) {
			deleteBonText(this.root_path,this.bon_path,this.bon_file);		//�������� �����ϱ�
			if(add_f1.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f1);	//÷������1 �����ϱ�
			if(add_f2.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f2);	//÷������2 �����ϱ�
			if(add_f3.length() != 0)
				deleteAddFile(this.root_path,this.bon_path,this.add_f3);	//÷������1 �����ϱ�
			deleteTableLine("app_master","pid",pid);						//app_master�� ����line�� ������ �����ϱ�
		}
		//������ ����
		else if(flag.equals("SERVICE")) {
			deleteTableLine("app_master","pid",pid);						//app_master�� ����line�� ������ �����ϱ�
		}
		//����(�ް���,�����,�����û��) ����
		else if(flag.equals("HYU_GA") || flag.equals("OE_CHUL") || flag.equals("CHULJANG_SINCHEONG")) {
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteTableLink("geuntae_master","gt_id",pid);			//���°��� ���̺� MASTER �� ���� ������ȣ����
			deleteTableSubLink("geuntae_account","gt_id",pid);		//���°��� ���̺� ACCOUNT �� ���� ������ȣ����
		}
		//���� ����(����,���庸��)
		else if(flag.equals("BOGO") || flag.equals("CHULJANG_BOGO")) {	
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteBonmunLink("bogoseo_master","bg_id",pid,this.root_path);	 //������������ ����
			deleteAttacheLink("bogoseo_master","bg_id",pid,this.root_path);	 //÷���������� ����
			deleteTableLink("bogoseo_master","bg_id",pid);			//�������� ���̺� MASTER �� ���� ������ȣ����
		}
		//���� ����(��ȼ�,���Խ�û��,������,������)
		else if(flag.equals("GIAN") || flag.equals("MYEONGHAM") || flag.equals("SAYU") || flag.equals("HYEOPJO")) {	
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteBonmunLink("jiweon_master","jw_id",pid,this.root_path);	//������������ ����
			deleteNoteLink("jiweon_master","jw_id",pid,this.root_path);		//Note�������� ����
			deleteAttacheLink("jiweon_master","jw_id",pid,this.root_path);	//÷���������� ����
			deleteTableLink("jiweon_master","jw_id",pid);			//�������� ���̺� MASTER �� ���� ������ȣ����
		}
		//����ٹ�(�ܾ�) ����(����ٹ���û��)
		else if(flag.equals("YEONJANG")) {
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteTableLink("janeup_master","ju_id",pid);			//�������� ���̺� MASTER �� ���� ������ȣ����
			deleteTableSubLink("janeup_worker","ju_id",pid);			//�������� ���̺� WORKER �� ���� ������ȣ����
		}
		//�λ� ����(�����Ƿڼ�)
		else if(flag.equals("GUIN")) {
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteTableLink("insa_master","is_id",pid);				//�λ���� ���̺� MASTER �� ���� ������ȣ����
		}
		//���� ����(��������)
		else if(flag.equals("GYOYUK_ILJI")) {
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
			deleteBonmunLink("gyoyuk_master","gy_id",pid,this.root_path);	//������������ ����
			deleteTableLink("gyoyuk_master","gy_id",pid);			//�������� ���̺� MASTER �� ���� ������ȣ����
			deleteTableSubLink("gyoyuk_part","gy_id",pid);			//�������� ���̺� PART �� ���� ������ȣ����
		}
		//��Ÿ [�ݷ����� ���ڰ��系�� ���븸 ������]
		else {
			deleteTableLine("app_master","pid",pid);			//app_master�� ����line�� ������ �����ϱ�
		}
			

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}
	//*******************************************************************
	//	�������� �����ϱ�
	//*******************************************************************/	
	public void deleteBonText (String root_path,String bon_path,String bon_file)
	{
		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = root_path + bon_path + "/bonmun/" + bon_file;
		//System.out.println("bonmun file : " + filename);
		text.delFilename(filename);	//�ش� ���ϻ��� �ϱ�
	}
	
	//*******************************************************************
	//	÷������ �����ϱ�
	//*******************************************************************/	
	public void deleteAddFile (String root_path,String bon_path,String add_file)
	{
		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = root_path + bon_path + "/addfile/" + add_file;
		//System.out.println("add file : " + filename);
		text.delFilename(filename);	//�ش� ���ϻ��� �ϱ�
	}

	//*******************************************************************
	//	APP Master Table�� Line�����ϱ� 
	//*******************************************************************/	
	public void deleteTableLine (String tablename,String pid_column_name,String pid) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
		//System.out.println("delete : " + query);
		stmt.execute(query);
		stmt.close();
	}

	//*******************************************************************
	//	���ù����� Table�� Line�����ϱ� 
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	public void deleteTableLink (String tablename,String pid_column_name,String pid) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			Statement stmt = null;
			stmt = con.createStatement();
			String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
			//System.out.println("deleteTableLink : " + query);
			stmt.execute(query);
			stmt.close();
		}
		
	}

	//*******************************************************************
	//	���ù����� sub TABLE���� 
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	public void deleteTableSubLink (String tablename,String pid_column_name,String pid) throws Exception
	{	
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where "+pid_column_name+"='"+pid+"'";
		//System.out.println("deleteTableLink : " + query);
		stmt.execute(query);
		stmt.close();	
	}

	//*******************************************************************
	//	�������� �������� �����ϱ�
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	private void deleteBonmunLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//����path �� �������� ���Ͽ� �����ϱ�
			query = "select bon_path,bon_file from "+tablename+" where "+pid_column_name+"='"+pid+"'";
//			//System.out.println("�������� query : " + query );
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //����path									
				String bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = ""; //�������ϸ�
//				//System.out.println("�������� path,���ϸ� : " + bon_path +  " : " + bon_file );
				deleteBonText(root_path,bon_path,bon_file);
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	�������� note���� �����ϱ�
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	private void deleteNoteLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//����path �� Note���� ���Ͽ� �����ϱ�
			query = "select bon_path,note_file from "+tablename+" where "+pid_column_name+"='"+pid+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //����path									
				String note_file = rs.getString("note_file");if(note_file == null) note_file = ""; //Note���ϸ�
				deleteBonText(root_path,bon_path,note_file);
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	�������� ÷������ �����ϱ�
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	private void deleteAttacheLink(String tablename,String pid_column_name,String pid,String root_path) throws Exception
	{
		if(checkFlag(tablename,pid_column_name,pid)) {
			String query  = "";
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();

			//����path �� ÷������ ���Ͽ� �����ϱ�
			query = "select bon_path,sname from "+tablename+" where "+pid_column_name+"='"+pid+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) { 
				String bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = ""; //����path									
				String sname = rs.getString("sname");if(sname == null) sname = ""; //÷�� ���ϸ�

				StringTokenizer s = new StringTokenizer(sname,"|");		//����� ÷������
				while(s.hasMoreTokens()) {
					String add_file = s.nextToken();
					add_file = add_file.trim()+".bin";
					////System.out.println("÷������ ���� : " + add_file);
					deleteAddFile(root_path,bon_path,add_file);
				}
			}
			stmt.close();
			rs.close();
		}
	}

	//*******************************************************************
	//	�������� Table�� Line�����ϱ� ���� �Ǵ��ϱ� 
	// 1����(�ְ��μ�)�� ����ø� ����, 2����(����μ�)�� ����ô� ����ʵ�)
	//*******************************************************************/	
	private boolean checkFlag(String tablename,String pid_column_name,String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select flag from "+tablename+" where "+pid_column_name+"='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("flag");
		
		stmt.close();
		rs.close();
		
		if(data == null) data = "";
		if(data.equals("null")) data = "";
//		//System.out.println("data : " + data);

		if(data.length() == 0) return true;		//�ӽ��������� ��������
		else return false;						//������
	}
}
