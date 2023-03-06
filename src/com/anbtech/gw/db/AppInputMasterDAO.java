package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import com.anbtech.gw.business.*;
import com.anbtech.file.*;
import java.util.StringTokenizer;
import java.sql.*;
import java.io.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.oreilly.servlet.MultipartRequest;

public class AppInputMasterDAO
{
	// Class ����
	private com.anbtech.dbconn.DBConnectionManager connMgr;
	private com.anbtech.file.FileWriteString text;					//��������
	private com.anbtech.gw.business.AppInputMasterBO masterBO;		//���� business login ����
	private com.anbtech.gw.db.AppAttorneyDAO attDAO;				//�븮������ ó��
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó���ϱ�
	private Connection con;
	private int atcnt = 0;											//÷������ ����
	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public AppInputMasterDAO() 
	{	
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);
	}	

	public AppInputMasterDAO(Connection con) {
		this.con = con;
		attDAO = new com.anbtech.gw.db.AppAttorneyDAO(con);
	}

	//-----------------------------------------------------------
	//���ڰ��� MASTER�� �����ϱ� : �����Է½�
	//------------------------------------------------------------
	public String setTable(String mode,String doc_pid,String doc_subj,String writer_id,String writer_name,String writer_date,
			String doc_line,String doc_peri,String doc_secu,String root_path,String doc_cont,String doc_plid,String doc_flag) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		masterBO = new com.anbtech.gw.business.AppInputMasterBO();		//���缱 ����
		if(doc_line == null) doc_line = "";		//���缱

		//' ���ֱ�
		doc_subj = str.repWord(doc_subj,"'","`");		//����
		doc_cont = str.repWord(doc_cont,"'","`");		//����
	
		//����1. ������ ã��
		masterBO.exeLineData(doc_line);											//���缱������ �м� �����ϱ�
		String doc_lin = "";
		if(mode.equals("REQ")) doc_lin = masterBO.getAppLine();					//���缱 ���� ���ϱ� (������ ����)
		else if(mode.equals("ABAT")) doc_lin = masterBO.getAppLineParallel();	//���缱 ���� ���ϱ� (�ϰ��� ����)
		else if(mode.equals("TMP")) doc_lin = masterBO.getAppLine();			//���缱 ���� ���ϱ� (������ ���Ƿ� ó��)
		String doc_rev = masterBO.getAppRev();							//������
		String rev_cmt = "";											//����븮�� ǥ��
		String att_rev = attDAO.searchAttorney(doc_rev);				//����븮������ 
		if(att_rev.length() != 0) {
			rev_cmt = "[������:"+doc_rev+"/"+getName(doc_rev)+"]";
			doc_rev = att_rev;
		}
		

		String doc_dec = masterBO.getAppDec();							//������
		String dec_cmt = "";											//���δ븮�� ǥ��
		String att_dec = attDAO.searchAttorney(doc_dec);				//���δ븮������ 
		if(att_dec.length() != 0) {
			dec_cmt = "[������:"+doc_dec+"/"+getName(doc_dec)+"]";
			doc_dec = att_dec;
		}

		String doc_agr = masterBO.getAppAgr();							//������
		String agr_cmt = "";											//������ �븮�� ǥ��
		String att_agr = attDAO.searchAttorney(doc_agr);				//������ �븮������ 
		if(att_agr.length() != 0) {
			agr_cmt = "[������:"+doc_agr+"/"+getName(doc_agr)+"]";
			doc_agr = att_agr;
		}

		String doc_agr2 = masterBO.getAppAgr2();						//������2
		String agr_cmt2 = "";											//������ �븮�� ǥ��
		String att_agr2 = attDAO.searchAttorney(doc_agr2);				//������ �븮������ 
		if(att_agr2.length() != 0) {
			agr_cmt2 = "[������:"+doc_agr2+"/"+getName(doc_agr2)+"]";
			doc_agr2 = att_agr2;
		}

		String doc_agr3 = masterBO.getAppAgr3();						//������3
		String agr_cmt3 = "";											//������ �븮�� ǥ��
		String att_agr3 = attDAO.searchAttorney(doc_agr3);				//������ �븮������ 
		if(att_agr3.length() != 0) {
			agr_cmt3 = "[������:"+doc_agr3+"/"+getName(doc_agr3)+"]";
			doc_agr3 = att_agr3;
		}

		String doc_agr4 = masterBO.getAppAgr4();						//������4
		String agr_cmt4 = "";											//������ �븮�� ǥ��
		String att_agr4 = attDAO.searchAttorney(doc_agr4);				//������ �븮������ 
		if(att_agr4.length() != 0) {
			agr_cmt4 = "[������:"+doc_agr4+"/"+getName(doc_agr4)+"]";
			doc_agr4 = att_agr4;
		}

		String doc_agr5 = masterBO.getAppAgr5();						//������5
		String agr_cmt5 = "";											//������ �븮�� ǥ��
		String att_agr5 = attDAO.searchAttorney(doc_agr5);				//������ �븮������ 
		if(att_agr5.length() != 0) {
			agr_cmt5 = "[������:"+doc_agr5+"/"+getName(doc_agr5)+"]";
			doc_agr5 = att_agr5;
		}

		String doc_agr6 = masterBO.getAppAgr6();						//������6
		String agr_cmt6 = "";											//������ �븮�� ǥ��
		String att_agr6 = attDAO.searchAttorney(doc_agr6);				//������ �븮������ 
		if(att_agr6.length() != 0) {
			agr_cmt6 = "[������:"+doc_agr6+"/"+getName(doc_agr6)+"]";
			doc_agr6 = att_agr6;
		}

		String doc_agr7 = masterBO.getAppAgr7();						//������7
		String agr_cmt7 = "";											//������ �븮�� ǥ��
		String att_agr7 = attDAO.searchAttorney(doc_agr7);				//������ �븮������ 
		if(att_agr7.length() != 0) {
			agr_cmt7 = "[������:"+doc_agr7+"/"+getName(doc_agr7)+"]";
			doc_agr7 = att_agr7;
		}

		String doc_agr8 = masterBO.getAppAgr8();						//������8
		String agr_cmt8 = "";											//������ �븮�� ǥ��
		String att_agr8 = attDAO.searchAttorney(doc_agr8);				//������ �븮������ 
		if(att_agr8.length() != 0) {
			agr_cmt8 = "[������:"+doc_agr8+"/"+getName(doc_agr8)+"]";
			doc_agr8 = att_agr8;
		}

		String doc_agr9 = masterBO.getAppAgr9();						//������9
		String agr_cmt9 = "";											//������ �븮�� ǥ��
		String att_agr9 = attDAO.searchAttorney(doc_agr9);				//������ �븮������ 
		if(att_agr9.length() != 0) {
			agr_cmt9 = "[������:"+doc_agr9+"/"+getName(doc_agr9)+"]";
			doc_agr9 = att_agr9;
		}

		String doc_agr10 = masterBO.getAppAgr10();						//������10
		String agr_cmt10 = "";											//������ �븮�� ǥ��
		String att_agr10 = attDAO.searchAttorney(doc_agr10);			//������ �븮������ 
		if(att_agr10.length() != 0) {
			agr_cmt10 = "[������:"+doc_agr10+"/"+getName(doc_agr10)+"]";
			doc_agr10 = att_agr10;
		}

		String agr_cnt = masterBO.getAppAgreeCnt(); 					//������ �ο���
		String doc_rec = masterBO.getAppRec();							//�뺸��
		String doc_pat = "/eleApproval/" + writer_id;					//����Ȯ�� path
		String doc_bon = doc_pid;										//�������� ���ϸ��� ������ȣ�� ����

		//����2. ������û ���� (��� or �ӽú���)
		String doc_ste = "";
		if(mode.equals("REQ") || mode.equals("ABAT"))
			doc_ste = masterBO.getAppLineState();					//���繮�� ���±��ϱ� (����:APV, ����:APL, �������뺸:APG,APG2~10)
		else if(mode.equals("TMP"))
			doc_ste="APT";											//�ӽú���

		//����3. ��� ó�� ���
		String doc_met = "";
		if(mode.equals("REQ")) doc_met = "SERIAL";
		else if(mode.equals("ABAT")) doc_met = "PARALLEL";

		String inputs="INSERT INTO APP_MASTER(pid,app_subj,writer,writer_name,write_date,app_state,reviewer,review_comment,";
			inputs += "agree,agree_comment,agree2,agree2_comment,agree3,agree3_comment,agree4,agree4_comment,";
			inputs += "agree5,agree5_comment,agree6,agree6_comment,agree7,agree7_comment,agree8,agree8_comment,";
			inputs += "agree9,agree9_comment,agree10,agree10_comment,";
			inputs += "agree_method,agree_count,decision,decision_comment,receivers,app_line,bon_path,bon_file,";
			inputs += "save_period,security_level,plid,flag) values('";
			inputs += doc_pid + "','" + doc_subj + "','" + writer_id + "','" + writer_name + "','" + writer_date + "','";
			inputs += doc_ste + "','" + doc_rev + "','" + rev_cmt + "','" + doc_agr + "','" + agr_cmt + "','";
			inputs += doc_agr2 + "','" + agr_cmt2 + "','" + doc_agr3 + "','" + agr_cmt3 + "','" + doc_agr4 + "','";
			inputs += agr_cmt4 + "','" + doc_agr5 + "','" + agr_cmt5 + "','" + doc_agr6 + "','" + agr_cmt6 + "','";
			inputs += doc_agr7 + "','" + agr_cmt7 + "','" + doc_agr8 + "','" + agr_cmt8 + "','" + doc_agr9 + "','";
			inputs += agr_cmt9 + "','" + doc_agr10 + "','" + agr_cmt10 + "','" + doc_met + "','";
			inputs += agr_cnt + "','" + doc_dec + "','" + dec_cmt + "','" + doc_rec + "','" + doc_lin + "','" + doc_pat + "','";
			inputs += doc_bon + "','" + doc_peri + "','" + doc_secu + "','" + doc_plid + "','" + doc_flag + "')";											

		int er = stmt.executeUpdate(inputs);
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,doc_pid,doc_cont);
		}
		return inputs;
	}

	//-----------------------------------------------------------
	//���ڰ��� MASTER�� �����ϱ�
	//------------------------------------------------------------
	public void setTableUpdate(String mode,String doc_pid,String doc_subj,String writer_id,String writer_name,String writer_date,
			String doc_line,String doc_peri,String doc_secu,String root_path,String doc_cont,String doc_plid,String doc_flag) throws Exception
	{
		//' ���ֱ�
		doc_subj = str.repWord(doc_subj,"'","`");		//����
		doc_cont = str.repWord(doc_cont,"'","`");		//����

		Statement stmt = null;
		stmt = con.createStatement();
		masterBO = new com.anbtech.gw.business.AppInputMasterBO();
		if(doc_line == null) doc_line = "";		//���缱
		
		//����1. ������ ã��
		masterBO.exeLineData(doc_line);												//���缱������ �м� �����ϱ�
		String doc_lin = "";
		if(mode.equals("REQ_UP")) doc_lin = masterBO.getAppLine();					//���缱 ���� ���ϱ� (������ ����)
		else if(mode.equals("ABAT_UP")) doc_lin = masterBO.getAppLineParallel();	//���缱 ���� ���ϱ� (�ϰ��� ����)
		else if(mode.equals("TMP_UP")) doc_lin = masterBO.getAppLineParallel();		//���缱 ���� ���ϱ� (�ϰ��� ���Ƿ� ó��)
		String doc_rev = masterBO.getAppRev();							//������
		String rev_cmt = "";											//����븮�� ǥ��
		String att_rev = attDAO.searchAttorney(doc_rev);				//����븮������ 
		if(att_rev.length() != 0) {
			rev_cmt = "[������:"+doc_rev+"/"+getName(doc_rev)+"]";
			doc_rev = att_rev;
		}
			

		String doc_dec = masterBO.getAppDec();							//������
		String dec_cmt = "";											//���δ븮�� ǥ��
		String att_dec = attDAO.searchAttorney(doc_dec);				//���δ븮������ 
		if(att_dec.length() != 0) {
			dec_cmt = "[������:"+doc_dec+"/"+getName(doc_dec)+"]";
			doc_dec = att_dec;
		}

		String doc_agr = masterBO.getAppAgr();							//������
		String agr_cmt = "";											//������ �븮�� ǥ��
		String att_agr = attDAO.searchAttorney(doc_agr);				//������ �븮������ 
		if(att_agr.length() != 0) {
			agr_cmt = "[������:"+doc_agr+"/"+getName(doc_agr)+"]";
			doc_agr = att_agr;
		}

		String doc_agr2 = masterBO.getAppAgr2();						//������2
		String agr_cmt2 = "";											//������ �븮�� ǥ��
		String att_agr2 = attDAO.searchAttorney(doc_agr2);				//������ �븮������ 
		if(att_agr2.length() != 0) {
			agr_cmt2 = "[������:"+doc_agr2+"/"+getName(doc_agr2)+"]";
			doc_agr2 = att_agr2;
		}

		String doc_agr3 = masterBO.getAppAgr3();						//������3
		String agr_cmt3 = "";											//������ �븮�� ǥ��
		String att_agr3 = attDAO.searchAttorney(doc_agr3);				//������ �븮������ 
		if(att_agr3.length() != 0) {
			agr_cmt3 = "[������:"+doc_agr3+"/"+getName(doc_agr3)+"]";
			doc_agr3 = att_agr3;
		}

		String doc_agr4 = masterBO.getAppAgr4();						//������4
		String agr_cmt4 = "";											//������ �븮�� ǥ��
		String att_agr4 = attDAO.searchAttorney(doc_agr4);				//������ �븮������ 
		if(att_agr4.length() != 0) {
			agr_cmt4 = "[������:"+doc_agr4+"/"+getName(doc_agr4)+"]";
			doc_agr4 = att_agr4;
		}

		String doc_agr5 = masterBO.getAppAgr5();						//������5
		String agr_cmt5 = "";											//������ �븮�� ǥ��
		String att_agr5 = attDAO.searchAttorney(doc_agr5);				//������ �븮������ 
		if(att_agr5.length() != 0) {
			agr_cmt5 = "[������:"+doc_agr5+"/"+getName(doc_agr5)+"]";
			doc_agr5 = att_agr5;
		}

		String doc_agr6 = masterBO.getAppAgr6();						//������6
		String agr_cmt6 = "";											//������ �븮�� ǥ��
		String att_agr6 = attDAO.searchAttorney(doc_agr6);				//������ �븮������ 
		if(att_agr6.length() != 0) {
			agr_cmt6 = "[������:"+doc_agr6+"/"+getName(doc_agr6)+"]";
			doc_agr6 = att_agr6;
		}

		String doc_agr7 = masterBO.getAppAgr7();						//������7
		String agr_cmt7 = "";											//������ �븮�� ǥ��
		String att_agr7 = attDAO.searchAttorney(doc_agr7);				//������ �븮������ 
		if(att_agr7.length() != 0) {
			agr_cmt7 = "[������:"+doc_agr7+"/"+getName(doc_agr7)+"]";
			doc_agr7 = att_agr7;
		}

		String doc_agr8 = masterBO.getAppAgr8();						//������8
		String agr_cmt8 = "";											//������ �븮�� ǥ��
		String att_agr8 = attDAO.searchAttorney(doc_agr8);				//������ �븮������ 
		if(att_agr8.length() != 0) {
			agr_cmt8 = "[������:"+doc_agr8+"/"+getName(doc_agr8)+"]";
			doc_agr8 = att_agr8;
		}

		String doc_agr9 = masterBO.getAppAgr9();						//������9
		String agr_cmt9 = "";											//������ �븮�� ǥ��
		String att_agr9 = attDAO.searchAttorney(doc_agr9);				//������ �븮������ 
		if(att_agr9.length() != 0) {
			agr_cmt9 = "[������:"+doc_agr9+"/"+getName(doc_agr9)+"]";
			doc_agr9 = att_agr9;
		}

		String doc_agr10 = masterBO.getAppAgr10();						//������10
		String agr_cmt10 = "";											//������ �븮�� ǥ��
		String att_agr10 = attDAO.searchAttorney(doc_agr10);			//������ �븮������ 
		if(att_agr10.length() != 0) {
			agr_cmt10 = "[������:"+doc_agr10+"/"+getName(doc_agr10)+"]";
			doc_agr10 = att_agr10;
		}
		String agr_cnt = masterBO.getAppAgreeCnt(); 					//������ �ο���
		String doc_rec = masterBO.getAppRec();							//�뺸��
		String doc_pat = "/eleApproval/" + writer_id;					//����Ȯ�� path
		String doc_bon = doc_pid;										//�������� ���ϸ��� ������ȣ�� ����

		//����2. ������û ���� (��� or �ӽú���)
		String doc_ste = "";
		if(mode.equals("REQ_UP") || mode.equals("ABAT_UP"))
			doc_ste = masterBO.getAppLineState();					//���繮�� ���±��ϱ� (����:APV, ����:APL, �������뺸:APG,APG2~10)
		else if(mode.equals("TMP_UP"))
			doc_ste="APT";											//�ӽú���

		//����3. ��� ó�� ���
		String doc_met = "";
		if(mode.equals("REQ_UP")) doc_met = "SERIAL";
		else if(mode.equals("ABAT_UP")) doc_met = "PARALLEL";

		String update="UPDATE APP_MASTER set ";
			  update+="app_subj='"+doc_subj+"',";
			  update+="writer='"+writer_id+"',";
			  update+="writer_name='"+writer_name+"',";
			  update+="write_date='"+writer_date+"',";
			  update+="app_state='"+doc_ste+"',";
			  update+="reviewer='"+doc_rev+"',";
			  update+="review_comment='"+rev_cmt+"',";
			  update+="agree='"+doc_agr+"',";
			  update+="agree_comment='"+agr_cmt+"',";
			  update+="agree2='"+doc_agr2+"',";
			  update+="agree2_comment='"+agr_cmt2+"',";
			  update+="agree3='"+doc_agr3+"',";
			  update+="agree3_comment='"+agr_cmt3+"',";
			  update+="agree4='"+doc_agr4+"',";
			  update+="agree4_comment='"+agr_cmt4+"',";
			  update+="agree5='"+doc_agr5+"',";
			  update+="agree5_comment='"+agr_cmt5+"',";
			  update+="agree6='"+doc_agr6+"',";
			  update+="agree6_comment='"+agr_cmt6+"',";
			  update+="agree7='"+doc_agr7+"',";
			  update+="agree7_comment='"+agr_cmt7+"',";
			  update+="agree8='"+doc_agr8+"',";
			  update+="agree8_comment='"+agr_cmt8+"',";
			  update+="agree9='"+doc_agr9+"',";
			  update+="agree9_comment='"+agr_cmt9+"',";
			  update+="agree10='"+doc_agr10+"',";
			  update+="agree10_comment='"+agr_cmt10+"',";
			  update+="agree_method='"+doc_met+"',";
			  update+="agree_count='"+agr_cnt+"',";
			  update+="decision='"+doc_dec+"',";
			  update+="decision_comment='"+dec_cmt+"',";
			  update+="receivers='"+doc_rec+"',";
			  update+="app_line='"+doc_lin+"',";
			  update+="bon_path='"+doc_pat+"',";
			  update+="bon_file='"+doc_bon+"',";
			  update+="save_period='"+doc_peri+"',";
			  update+="security_level='"+doc_secu+"',";
			  update+="plid='"+doc_plid+"',";
			  update+=" flag='"+doc_flag+"'";
			  update+=" where pid='"+doc_pid+"'";
		int er = stmt.executeUpdate(update);
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,doc_pid,doc_cont);
		}
	}

	//--------------------------------------------------------------------------
	// ������ ���Ϸ� �����ϱ�
	// root_path : root Path, doc_pat : Ȯ�� path, content : ��������
	//--------------------------------------------------------------------------
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}

	//--------------------------------------------------------------------------
	// ÷������ �����ϱ�   
	// multi:multi������ ����, root_path : root Path, addFile : ������ȣ(pid)
	//--------------------------------------------------------------------------
	public TableText getFile_frommulti(MultipartRequest multi,String root_path,String sabun,String addFile) throws IOException
	{
		String filename = "";
		String doc_pat =  "/eleApproval/" + sabun;
		String FullPathName = root_path + doc_pat + "/addfile/";

/*		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();
			String name = "doc_ad"+i;
			String fname = multi.getFilesystemName(name);				//�������ϸ�
			String cname = addFile+"_"+Integer.toString(i);				//�ű� ����(������ȣ)�� �ٲ� ���ϸ�
			if(fname != null){
				chgRename(FullPathName+fname,FullPathName+cname);			//���Ͻý��۳��� ���ϸ��� �ٲ��ֱ�
				filename += fname + "|" + cname + ";";
			}
			else filename += "#"+"|"+"#"+";";
			i++;
		} //while
*/

		//÷�� 1
		String fname = multi.getFilesystemName("doc_ad1");				//�������ϸ�
		String cname = addFile+"_1";									//�ű� ����(������ȣ)�� �ٲ� ���ϸ�
		if(fname == null) {
			filename += "#|" + "#;";
		} else {  
			chgRename(FullPathName+fname,FullPathName+cname);			//���Ͻý��۳��� ���ϸ��� �ٲ��ֱ�
			filename += fname + "|" + cname + ";";
			this.atcnt++;												//÷������ ����
		}
	
		//÷�� 2
		fname = multi.getFilesystemName("doc_ad2");						//�������ϸ�
		cname = addFile+"_2";											//�ű� ����(������ȣ)�� �ٲ� ���ϸ�
		if(fname == null) {
			filename += "#|" + "#;";
		} else {  
			chgRename(FullPathName+fname,FullPathName+cname);			//���Ͻý��۳��� ���ϸ��� �ٲ��ֱ�
			filename += fname + "|" + cname + ";";
			this.atcnt++;												//÷������ ����
		}

		//÷�� 3
		fname = multi.getFilesystemName("doc_ad3");						//�������ϸ�
		cname = addFile+"_3";											//�ű� ����(������ȣ)�� �ٲ� ���ϸ�
		if(fname == null) {
			filename += "#|" + "#;";
		} else {  
			chgRename(FullPathName+fname,FullPathName+cname);			//���Ͻý��۳��� ���ϸ��� �ٲ��ֱ�
			filename += fname + "|" + cname + ";";
			this.atcnt++;												//÷������ ����
		}

		TableText file = new com.anbtech.gw.entity.TableText();
		file.setText(filename);
		return file;
	}

	
	//--------------------------------------------------------------------------
	// �̸� �ٲ��ֱ�  
	// 
	//--------------------------------------------------------------------------
	public void chgRename(String filename1,String filename2) throws IOException
	{
		File BFN = new File(filename1);		//�ٲ�File��
		File AFN = new File(filename2);		//���ο� ���ϸ�
		if(BFN.exists()) BFN.renameTo(AFN);	
	}
	//--------------------------------------------------------------------------
	// ÷������ �����ϱ�   
	// multi:multi������ ����, root_path : root Path, addFile : ������ȣ(pid)
	//--------------------------------------------------------------------------
	public void updFile(String filename,String pid) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//finename�� �����̸��� �����̸����� �����ϱ� (�����̸�|�����̸�;)
		StringTokenizer str = new StringTokenizer(filename,";");
		String query = "";
		int n = 1;
		while(str.hasMoreTokens()) {
			String oneFile = str.nextToken();
			StringTokenizer nstr = new StringTokenizer(oneFile,"|");
			int i = 1;
			String fname = "";
			String cname = "";
			while(nstr.hasMoreTokens()) {
				if(i == 1) fname = nstr.nextToken();
				else if(i == 2) cname = nstr.nextToken();
				i++;
			}
			if((n == 1) && (!fname.equals("#"))) {
				query = "update APP_MASTER set add_1_original='"+fname+"', add_1_file='"+cname+"' where pid='"+pid+"'";
				stmt.executeUpdate(query);
			} else if((n == 2) && (!fname.equals("#"))) {
				query = "update APP_MASTER set add_2_original='"+fname+"', add_2_file='"+cname+"' where pid='"+pid+"'"; 
				stmt.executeUpdate(query);
			} else if((n == 3) && (!fname.equals("#"))) {
				query = "update APP_MASTER set add_3_original='"+fname+"', add_3_file='"+cname+"' where pid='"+pid+"'"; 
				stmt.executeUpdate(query);
			}
			n++;
		}
		stmt.close();
	}
	//--------------------------------------------------------------------------
	// ÷������ ���� ���� �ľ��ϱ� (�űԷ� ��Ͻ�)
	//--------------------------------------------------------------------------
	public int getAttacheCount()
	{
		return this.atcnt;
	}

	//--------------------------------------------------------------------------
	// ÷������ ���� ���� �ľ��ϱ� (�����Ҷ�)
	//--------------------------------------------------------------------------
	public int getAttacheCount(String pid) throws SQLException
	{
		int cnt = 0;
		String flg1 = "", flg2 = "", flg3 = "";

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select add_1_file,add_2_file,add_3_file from app_master ";
			query += "where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			flg1 = rs.getString("add_1_file");	if(flg1 == null) flg1 = "";
			flg2 = rs.getString("add_2_file");	if(flg2 == null) flg2 = "";
			flg3 = rs.getString("add_3_file");	if(flg3 == null) flg3 = "";
		}
		stmt.close();
		rs.close();

		if(flg1.length() > 1) cnt++;
		if(flg2.length() > 1) cnt++;
		if(flg3.length() > 1) cnt++;

		return cnt;
	}

	//--------------------------------------------------------------------------
	// ÷������ ���� �����ϱ� 
	// table name, pid, ����
	//--------------------------------------------------------------------------
	public void setAttacheCount(String tablename,String pid,int count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String query = "update "+tablename+" set add_counter='"+count+"' where pid='"+pid+"'";
		stmt.executeUpdate(query);
		stmt.close();
	}
	//---------------------------------------------------------------------------
	//���ڰ��� ���ù����� ���ڰ��� ������(EE) �Ϸ�(EF) �˷��ֱ�
	//tablename:table�̸�, columnName:���ڰ������ �÷���, mgrId:������ȣ �÷���, 
	//doc_plid :������ȣ,  status:���ڰ������ (EE or EF)
	//----------------------------------------------------------------------------
	public void setTableLink(String tablename,String mgrName,String mgrId,String doc_plid,String status) throws Exception 
	{
		Statement stmt = null;
		stmt = con.createStatement();

		StringTokenizer strid = new StringTokenizer(doc_plid,",");//�޸��� ���е� ���ù���
		while(strid.hasMoreTokens()) {
			String query = "update "+tablename+" set "+mgrName+"='"+status+"' where "+mgrId+"='"+strid.nextToken()+"'";
			stmt.executeUpdate(query);
		} //while
		stmt.close();
	}

	//--------------------------------------------------------------------------
	// �ش����� �̸� ���ϱ�
	//--------------------------------------------------------------------------
	public String getName(String id) throws SQLException
	{
		String rtn = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select name from user_table where id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			rtn = rs.getString("name");
		}
		stmt.close();
		rs.close();
		return rtn;
	}
}