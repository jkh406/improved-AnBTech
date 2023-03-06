package com.anbtech.es.bogoseo.db;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.text.DecimalFormat;

public class BogoSeoDAO{
	private Connection con;
	private com.anbtech.file.FileWriteString text;					//��������
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó���ϱ�
	
	/*******************************************************************
	 * ������
	 *******************************************************************/
	public BogoSeoDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* ���� ���� �����ϱ� 
	*******************************************************************/
	public void setBogoTable(String bg_id,String user_id,String user_name,String user_code,String user_rank,
		String ac_id,String ac_code,String ac_name,String in_date,String prj_name,String ap_name,String subject,String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		subject = str.repWord(subject,"'","`");		//����
		content = str.repWord(content,"'","`");		//����
		
		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/es/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO BOGOSEO_MASTER(bg_id,ys_kind,user_id,user_name,user_code,user_rank,";
			incommon += "ac_id,ac_code,ac_name,in_date,prj_name,ap_name,subject,bon_path,bon_file) values('";
		
		String input = incommon+bg_id+"','BOGO','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','"+prj_name+"','"+ap_name+"','";
			input += subject+"','"+doc_pat+"','"+bg_id+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,bg_id,content);
		}
	}

	/*******************************************************************
	* ���� ���� ���� �����ϱ� 
	*******************************************************************/
	public void setChuljangBogoTable(String bg_id,String user_id,String user_name,String user_code,String user_rank,
		String ac_id,String ac_code,String ac_name,String in_date,String bg_dest,String bg_purpose,String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		bg_dest = str.repWord(bg_dest,"'","`");				//������
		bg_purpose = str.repWord(bg_purpose,"'","`");		//����
		content = str.repWord(content,"'","`");				//����

		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/es/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO BOGOSEO_MASTER(bg_id,ys_kind,user_id,user_name,user_code,user_rank,";
			incommon += "ac_id,ac_code,ac_name,in_date,bon_path,bon_file,bg_dest,bg_purpose) values('";
		
		String input = incommon+bg_id+"','CHULJANG_BOGO','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','"+doc_pat+"','"+bg_id+"','";
			input += bg_dest+"','"+bg_purpose+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,bg_id,content);
		}
	}

	/*******************************************************************
	* ������ ���Ϸ� �����ϱ�
	* root_path : root Path, doc_pat : Ȯ�� path, content : ��������
	 *******************************************************************/
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}

	/*******************************************************************
	* ÷������ �����ϱ� (�űԷ� ó�� ÷���Ҷ�)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String es_id,String filepath) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����

		int i = 1;					//÷������ Ȯ����
		int atcnt = 0;				//÷������ ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "attachfile"+i;		//upload�� input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,es_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				filename += fname + " |";
				savename += es_id + "_" + i + " |";
				filetype += ftype + " |";
				filesize += fsize + " |";
				atcnt++;
			}
			i++;
		}//while

		//Table�� �����ϱ�
		if(i > 1) {
			setAddFileUpdate(es_id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* ÷������ �����ϱ� (�ӽ������� �����Ͽ� ÷���Ҷ�)
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String es_id,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����
		int att_cnt = Integer.parseInt(attache_cnt);	//÷������ �ִ���� �̸�
		String newdata = "";

		//�űԷ� ÷���� ����
		int i = 1;		//÷������Ȯ����
		int n = 0;		//����迭�� ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "";
			String uname = "";
			for(int a=0; a<att_cnt; a++) {			//÷������ att_cnt ��°���� �а� ��������
				name = "attachfile"+i;
				uname = multi.getFilesystemName(name);
				if(uname != null) break; else { i++; n++; }
			}
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,es_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				newdata += n + "|";					//�迭��ȣ
				newdata += uname + "|";
				newdata += es_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
			}
			i++;
			n++;
		}//while

		//�迭�����
		java.util.StringTokenizer fna = new StringTokenizer(fname,"|");
		int fn = fna.countTokens();

		int an = fn + n;
		String[][] nfile = new String[an][4];
		for(int j=0; j<an; j++) for(int k=0; k<4; k++) nfile[j][k] = "";

		//÷������ �迭�� ���
		java.util.StringTokenizer ndata = new StringTokenizer(newdata,";");
		while(ndata.hasMoreTokens()) {
			String nnd = ndata.nextToken();		//1���� �б�
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			int m = 0, ai = 0;	//�迭��ȣ ã��
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				if(m == 0) ai = Integer.parseInt(nndata.nextToken());
				else {
					nfile[ai][ni] = nndata.nextToken();
					ni++;
				}
				m++;
			}
		}
		//������ �ִ� ÷������ �迭�� ���̱�
		java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			//÷�����ϸ�
		int hi = 0;
		while(o_fname.hasMoreTokens()) {
			String read = o_fname.nextToken();
			if(read.length() != 1) nfile[hi][0] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			//÷������ �����
		hi = 0;
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			if(read.length() != 1) {
				int hi_no = hi + 1;
				String org_file = read.substring(0,read.length()-1);
				File BFN = new File(filepath+"/"+org_file+".bin");			//�ٲ�File��
				File AFN = new File(filepath+"/"+es_id+"_"+hi_no+".bin");	//���ο� ���ϸ�
				if(BFN.exists()) BFN.renameTo(AFN);	
				nfile[hi][1] = es_id+"_"+hi_no;
			}
			hi++;
		}
		java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			//÷������ Ÿ��
		hi = 0;
		while(o_ftype.hasMoreTokens()) {
			String read = o_ftype.nextToken();
			if(read.length() != 1) nfile[hi][2] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			//÷������ ũ��
		hi = 0;
		while(o_fsize.hasMoreTokens()) {
			String read = o_fsize.nextToken();
			if(read.length() != 1) nfile[hi][3] = read.substring(0,read.length()-1);
			hi++;
		}

		//������ ������ ������
		int atcnt = 0;				//÷������ ����
		for(int p=0; p<an; p++) {
			if(nfile[p][0].length() != 0) {
				filename += nfile[p][0] + " |";
				savename += nfile[p][1] + " |";
				filetype += nfile[p][2] + " |";
				filesize += nfile[p][3] + " |";
				atcnt++;
			}
		}

		//Table�� �����ϱ�
		if(an > 0) {
			setAddFileUpdate(es_id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* ÷������ �������� Table�� update�ϱ�
	 *******************************************************************/
	 private void setAddFileUpdate(String es_id, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update bogoseo_master set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where bg_id='"+es_id+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
}	
