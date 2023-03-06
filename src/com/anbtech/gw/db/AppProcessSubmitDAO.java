package com.anbtech.gw.db;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import com.anbtech.dbconn.*;
import com.anbtech.gw.*;
import com.anbtech.date.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.util.normalFormat;
import com.anbtech.file.FileWriteString;
import java.lang.SecurityException;
import java.io.UnsupportedEncodingException;

import com.anbtech.dbconn.DBConnectionManager;		

public class AppProcessSubmitDAO
{
	// Database Wrapper Class ����
	private DBConnectionManager connMgr;
	private Connection con;

	private com.anbtech.date.anbDate anbdt = null;					//���� ó��
	private com.anbtech.util.normalFormat nmf = null;				//�������
	private com.anbtech.es.geuntae.db.HyuGaDayDAO hyugaDayDAO;		//�ް��� �ϼ� �Է�(1�� ������νø�)
	private com.anbtech.es.geuntae.db.ChulJangDayDAO chuljangDayDAO;//����� �ϼ� �Է�(1�� ������νø�)
	private com.anbtech.file.FileWriteString text;					//������ ���Ϸ� ���
	private com.anbtech.gw.business.ModuleApprovalBO mdBO;			//�������,���ο� ����/�ݷ� ó��


	// ��� ���޺���
	private String doc_pid="";			//���繮�� ��ȣ
	private String doc_sub="";			//���繮�� ����

	private String doc_wri="";			//���繮�� ����� id
	private String doc_wna="";			//���繮�� ����� �̸� 	
	private String doc_wrd="";			//���繮�� �������

	private String doc_rev="";			//���繮�� ������ id
	private String doc_red="";			//���繮�� ��������
	private String doc_rem="";			//���繮�� �����ǰ�

	private String doc_agr="";			//���繮�� ������ id
	private String doc_agd="";			//���繮�� ��������
	private String doc_agm="";			//���繮�� �����ǰ�

	private String doc_agr2="";			//���繮�� ������2
	private String doc_agd2="";			//���繮�� ��������
	private String doc_agm2="";			//���繮�� �����ǰ�

	private String doc_agr3="";			//���繮�� ������3
	private String doc_agd3="";			//���繮�� ��������
	private String doc_agm3="";			//���繮�� �����ǰ�

	private String doc_agr4="";			//���繮�� ������4
	private String doc_agd4="";			//���繮�� ��������
	private String doc_agm4="";			//���繮�� �����ǰ�

	private String doc_agr5="";			//���繮�� ������5
	private String doc_agd5="";			//���繮�� ��������
	private String doc_agm5="";			//���繮�� �����ǰ�

	private String doc_agr6="";			//���繮�� ������6
	private String doc_agd6="";			//���繮�� ��������
	private String doc_agm6="";			//���繮�� �����ǰ�

	private String doc_agr7="";			//���繮�� ������7
	private String doc_agd7="";			//���繮�� ��������
	private String doc_agm7="";			//���繮�� �����ǰ�

	private String doc_agr8="";			//���繮�� ������8
	private String doc_agd8="";			//���繮�� ��������
	private String doc_agm8="";			//���繮�� �����ǰ�

	private String doc_agr9="";			//���繮�� ������9
	private String doc_agd9="";			//���繮�� ��������
	private String doc_agm9="";			//���繮�� �����ǰ�
	
	private String doc_agr10="";		//���繮�� ������10
	private String doc_agd10="";		//���繮�� ��������
	private String doc_agm10="";		//���繮�� �����ǰ�

	private String agr_met="";			//���� ��� (������ / �ϰ���)
	private int agr_cnt=0;				//������ ���ο� 
	private String agr_pas="";			//���� ������ ���ο�

	private String doc_dec="";			//���繮�� ������
	private String doc_ded="";			//���繮�� ��������
	private String doc_dem="";			//���繮�� �����ǰ�

	private String doc_rec="";			//���繮�� ���޹޴���
	private int rec_str_cnt=0;			//���޹���� �ο���
	
	private String doc_lin="";			//���繮�� �������

	private String doc_pat="";			//���繮�� �������� path (÷�����ϵ���)
	private String doc_bon="";			//���繮�� ��������

	private String doc_atcnt="0";		//÷������ ����

	private String doc_or1="";			//���繮�� ÷�����ϸ�1 (�������ϸ�)
	private String doc_ad1="";			//���繮�� ÷�����ϸ�1 (����Ǵ� ���ϸ�)

	private String doc_or2="";			//���繮�� ÷�����ϸ�2 (�������ϸ�)
	private String doc_ad2="";			//���繮�� ÷�����ϸ�2 (����Ǵ� ���ϸ�)

	private String doc_or3="";			//���繮�� ÷�����ϸ�3 (�������ϸ�)
	private String doc_ad3="";			//���繮�� ÷�����ϸ�3 (����Ǵ� ���ϸ�)

	private String doc_per="";			//���繮�� �����Ⱓ
	private String doc_del="";			//���繮�� �������� (������ ���)
	private String doc_sec="";			//���繮�� ���ȵ��

	//�ܺι��� ó������
	private String doc_flag="";			//�ܺι��� ����
	private String doc_lid ="";			//�ܺι��� ������ȣ

	// ������ ���޺���
	private String doc_ste="";			//���繮�� �������
	private String doc_dat="";			//���繮�� �������
	private String content="";			//���繮�� ���� �б� (unix�� �����)

	// ����ó�� ����
	private static String doc_rev_title="";		// ���缱 �Է� ��û����
	private String[] SEC_DATA;			// ���缱 �Է� ���� ���ҵ�����
	private String[] LINE_ORD;			// ������ �������
	private int lin_str_cnt = 0;		// ������ ������� �Ѽ��� 

	private String[] receiver;			// ���系�� �뺸������
	private String line_order_data;		// ���缱 ����

	private String[] sdiv = null;		// �뺸�ڰ� �μ��� ��� �����μ� ã�� �迭�� ���

	// ���޹޾ƿ� ����
	private String DOC_REQ="";			//���� ��û����
	private String DOC_REQ_NAME="";		//���� ��û�� �̸�
	private String DOC_PID="";			//���� ��ȣ	
	
	private String Message="";			//message ����
	private String mail_path = "";		//��������� ������ ������ �������� ������ upload+crp path
	private String ip_addr = "";		//�Խ��ǿ� ���� ���α��� ip_address
	
	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public AppProcessSubmitDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();			//����ó��
		nmf = new com.anbtech.util.normalFormat("000");	//������ȣ �Ϸù�ȣ
		hyugaDayDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);			//�����ϼ� �Է��ϱ�(1�� ���� ���νø�)
		chuljangDayDAO = new com.anbtech.es.geuntae.db.ChulJangDayDAO(con);		//���� �ϼ� �Է��ϱ�(1�� ���� ���νø�)
		text = new com.anbtech.file.FileWriteString();	//������ ���Ϸ� ��� (���ο������� ���)
		mdBO = new com.anbtech.gw.business.ModuleApprovalBO(con);//�������,���ο� ����/�ݷ� ó��
	}
	
	/***************************************************************************
	 * ������ȣ�� �̿��� ������ �ν��Ͻ� ������ ��� 
	 * �ش�Ǵ� ������ ������ �ν��Ͻ� ������ ��´�.
	 **************************************************************************/
	public boolean eleDecisionReadDoc(String idNo) throws SQLException 
	{
		boolean rtn = false;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT distinct ";
		query += "pid,app_subj,writer,writer_name,write_date,app_state,reviewer,review_date,review_comment,";
		query += "agree,agree_date,agree_comment,agree2,agree2_date,agree2_comment,agree3,agree3_date,agree3_comment,";
		query += "agree4,agree4_date,agree4_comment,agree5,agree5_date,agree5_comment,agree6,agree6_date,agree6_comment,";
		query += "agree7,agree7_date,agree7_comment,agree8,agree8_date,agree8_comment,agree9,agree9_date,agree9_comment,";
		query += "agree10,agree10_date,agree10_comment,agree_method,agree_count,agree_pass,decision,decision_date,decision_comment,";
		query += "receivers,app_line,bon_path,bon_file,add_counter,add_1_original,add_1_file,add_2_original,add_2_file,";
		query += "add_3_original,add_3_file,save_period,delete_date,security_level,plid,flag ";
		query += "from APP_MASTER where pid='"+idNo+"'";
	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.doc_pid=rs.getString("pid");							//���繮�� ��ȣ
			this.doc_sub=rs.getString("app_subj");						//���繮�� ����
			this.doc_wri=rs.getString("writer");						//���繮�� ����� id
			this.doc_wna=rs.getString("writer_name");					//���繮�� ����� �̸�
			this.doc_wrd=rs.getString("write_date");					//���繮�� �������
			this.doc_ste=rs.getString("app_state");						//���繮�� ������� ����

			this.doc_rev=rs.getString("reviewer");						//���繮�� ������ id
			this.doc_red=rs.getString("review_date");					//���繮�� ��������
			this.doc_rem=rs.getString("review_comment");				//���繮�� ������ �ǰ�

			this.doc_agr=rs.getString("agree");							//���繮�� ������ id
			this.doc_agd=rs.getString("agree_date");					//���繮�� ��������
			this.doc_agm=rs.getString("agree_comment");					//���繮�� ������ �ǰ�
			
			this.doc_agr2=rs.getString("agree2");						//���繮�� ������
			this.doc_agd2=rs.getString("agree2_date");					//���繮�� ��������
			this.doc_agm2=rs.getString("agree2_comment");				//���繮�� ������ �ǰ�
			
			this.doc_agr3=rs.getString("agree3");						//���繮�� ������
			this.doc_agd3=rs.getString("agree3_date");					//���繮�� ��������
			this.doc_agm3=rs.getString("agree3_comment");				//���繮�� ������ �ǰ�
			
			this.doc_agr4=rs.getString("agree4");						//���繮�� ������
			this.doc_agd4=rs.getString("agree4_date");					//���繮�� ��������
			this.doc_agm4=rs.getString("agree4_comment");				//���繮�� ������ �ǰ�
	
			this.doc_agr5=rs.getString("agree5");						//���繮�� ������
			this.doc_agd5=rs.getString("agree5_date");					//���繮�� ��������
			this.doc_agm5=rs.getString("agree5_comment");				//���繮�� ������ �ǰ�
	
			this.doc_agr6=rs.getString("agree6");						//���繮�� ������
			this.doc_agd6=rs.getString("agree6_date");					//���繮�� ��������
			this.doc_agm6=rs.getString("agree6_comment");				//���繮�� ������ �ǰ�
	
			this.doc_agr7=rs.getString("agree7");						//���繮�� ������
			this.doc_agd7=rs.getString("agree7_date");					//���繮�� ��������
			this.doc_agm7=rs.getString("agree7_comment");				//���繮�� ������ �ǰ�

			this.doc_agr8=rs.getString("agree8");						//���繮�� ������
			this.doc_agd8=rs.getString("agree8_date");					//���繮�� ��������
			this.doc_agm8=rs.getString("agree8_comment");				//���繮�� ������ �ǰ�
	
			this.doc_agr9=rs.getString("agree9");						//���繮�� ������
			this.doc_agd9=rs.getString("agree9_date");					//���繮�� ��������
			this.doc_agm9=rs.getString("agree9_comment");				//���繮�� ������ �ǰ�
			
			this.doc_agr10=rs.getString("agree10");						//���繮�� ������
			this.doc_agd10=rs.getString("agree10_date");				//���繮�� ��������
			this.doc_agm10=rs.getString("agree10_comment");				//���繮�� ������ �ǰ�
			
			this.agr_met=rs.getString("agree_method");					//������ �������
			String doc_cnt = rs.getString("agree_count");				//������ ���ο���
			if(doc_cnt == null) doc_cnt = "0";
			else if(doc_cnt.length() == 0) doc_cnt = "0";
			this.agr_cnt = Integer.parseInt(doc_cnt);

			this.agr_pas = rs.getString("agree_pass");					//���� ������ ���ο� 
			if(this.agr_pas == null) this.agr_pas = "0";
			else if(this.agr_pas.length() == 0) this.agr_pas = "0";

			this.doc_dec=rs.getString("decision");						//���繮�� ������
			this.doc_ded=rs.getString("decision_date");					//���繮�� ��������
			this.doc_dem=rs.getString("decision_comment");				//���繮�� ������ �ǰ�
		
			this.doc_rec=rs.getString("receivers");						//���繮�� ���޹޴���
			this.doc_lin=rs.getString("app_line");						//���繮�� �������

			this.doc_pat=rs.getString("bon_path");						//���繮�� �������� path (÷�����ϵ���)
			this.doc_bon=rs.getString("bon_file");						//���繮�� �������� file��

			this.doc_atcnt=rs.getString("add_counter");					//÷������ ����
			if(doc_atcnt == null) this.doc_atcnt = "0";
			this.doc_or1=rs.getString("add_1_original");				//���繮�� ÷�����ϸ�1 (�������ϸ�)
			this.doc_ad1=rs.getString("add_1_file");					//���繮�� ÷�����ϸ�1 (����Ǵ� ���ϸ�)

			this.doc_or2=rs.getString("add_2_original");				//���繮�� ÷�����ϸ�2 (�������ϸ�)
			this.doc_ad2=rs.getString("add_2_file");					//���繮�� ÷�����ϸ�2 (����Ǵ� ���ϸ�)

			this.doc_or3=rs.getString("add_3_original");				//���繮�� ÷�����ϸ�3 (�������ϸ�)
			this.doc_ad3=rs.getString("add_3_file");					//���繮�� ÷�����ϸ�3 (����Ǵ� ���ϸ�)

			this.doc_per=rs.getString("save_period");					//���繮�� �����Ⱓ
			this.doc_del=rs.getString("delete_date");					//���繮�� �������� (������ ���)
			this.doc_sec=rs.getString("security_level");				//���繮�� ���ȵ��

			this.doc_lid=rs.getString("plid");							//�ܺι��� ������ȣ
			this.doc_flag=rs.getString("flag");							//�ܺι��� ����

			rtn = true;
		}

		stmt.close();
		rs.close();

		return rtn;
	}	
	
	/******************************************************************************
		�������  �ľ��ϱ� (����:LINE_ORD[], ����:lin_str_cnt+1)
	******************************************************************************/
	public String[] eleDecisionLineOrder() throws SQLException 
	{
		//������� �˾ƺ��� (��������� �̿�)
		int lin_str_len = this.doc_lin.length();			//���ڿ� ����
		this.lin_str_cnt = 0;								//������� ������ ã��
		for(int a = 0 ; a < lin_str_len; a++) {
			String l_str = "" + doc_lin.charAt(a);
			if(l_str.equals(",")) this.lin_str_cnt++;		//�Ѱ��� �ܰ� Ƚ���ľ� 
		}

		//������� �迭�� ��� 
		if(this.doc_lin != null) {
			LINE_ORD = new String[lin_str_cnt+1];			
			String ord_data = this.doc_lin;
			for(int aa = 0; aa <= lin_str_len; aa++) {
				int sec_aa = ord_data.indexOf(",");
				if(sec_aa == -1) {
					LINE_ORD[aa] = ord_data;
					break;
				}
				else {
					LINE_ORD[aa] = ord_data.substring(0,sec_aa);
				} //if
				ord_data = ord_data.substring(sec_aa+1,ord_data.length());
			} //for
		} //if
		return LINE_ORD;			
	}

	/******************************************************************************
		�뺸�ο� �ľ��ϱ� (����:receiver[], ����:rec_str_cnt+1)
	******************************************************************************/
	public int eleDecisionReceiver() throws SQLException 
	{	
		//�뺸�� �ο��� �ľ�
		int rec_str_len = doc_rec.length();			//���ڿ� ����
		this.rec_str_cnt = 0;						//�뺸�� �� (�μ��� �ϳ��� ����)
		for(int b = 0 ; b < rec_str_len; b++) {
			String r_str = "" + doc_rec.charAt(b);
			if(r_str.equals(",")) this.rec_str_cnt++;
			if(b == rec_str_len-1) this.rec_str_cnt++;//������ �޸����� ���� 
		}

		//�뺸�ڸ�� �迭�� ���
		if(doc_rec != null) {
			receiver = new String[rec_str_cnt];			
			String red_data = doc_rec;
			for(int bb = 0; bb < rec_str_len; bb++) {
				int sec_bb = red_data.indexOf(",");
				if(sec_bb == -1) {
					//out.println("org line data : " + ord_data + "<br>");
					receiver[bb] = red_data;
					break;
				}
				else {
					receiver[bb] = red_data.substring(0,sec_bb);
				} //if
				red_data = red_data.substring(sec_bb+1,red_data.length());
			} //for
			//out.println("org receiver data : " + doc_rec + "<br>");
//			for(int bbb = 0; bbb < rec_str_cnt; bbb++)
				//System.out.println("receiver_data :" + receiver[bbb] + "<br>");
		} //if
		return rec_str_cnt;
	}
		
	/******************************************************************************
		����Ϸ�� ���� �԰��Ű�� 
		(APP_MASTER:APS, APP_SAVE:�԰�) 
	******************************************************************************/
	public String eleDecisionSave(String status, String comment) throws SQLException 
	{
		String STE = status;						//���� 
		
		//1. ���¿� ���� �ǰ߹� ���� ���ϱ�  
		if(STE.equals("APV")) {						//����
			this.doc_rem += comment;		this.doc_red = anbdt.getTime();	}
		if(STE.equals("APG")) {						//���� 
			this.doc_agm += comment;		this.doc_agd = anbdt.getTime();	}
		if(STE.equals("APG2")) {					//����2 
			this.doc_agm2 += comment;	this.doc_agd2 = anbdt.getTime();	}
		if(STE.equals("APG3")) {					//����3 
			this.doc_agm3 += comment;	this.doc_agd3 = anbdt.getTime();	}
		if(STE.equals("APG4")) {					//����4 
			this.doc_agm4 += comment;	this.doc_agd4 = anbdt.getTime();	}
		if(STE.equals("APG5")) {					//����5 
			this.doc_agm5 += comment;	this.doc_agd5 = anbdt.getTime();	}
		if(STE.equals("APG6")) {					//����6 
			this.doc_agm6 += comment;	this.doc_agd6 = anbdt.getTime();	}						
		if(STE.equals("APG7")) {					//����7 
			this.doc_agm7 += comment;	this.doc_agd7 = anbdt.getTime();	}
		if(STE.equals("APG8")) {					//����8 
			this.doc_agm8 += comment;	this.doc_agd8 = anbdt.getTime();	}														
		if(STE.equals("APG9")) {					//����9 
			this.doc_agm9 += comment;	this.doc_agd9 = anbdt.getTime();	}
		if(STE.equals("APG10")) {					//����10 
			this.doc_agm10 += comment;	this.doc_agd10 = anbdt.getTime();}
		if(STE.equals("APL")) {						//����  
			this.doc_dem += comment;		this.doc_ded = anbdt.getTime();	}																	
																
		//2. �������� ���ϱ�
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//ó���� ��� (1����)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1��
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2��
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3��
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5��
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//���� (100��)

		//3. ������ DB�� ������Ʈ ��Ŵ : �԰� ��Ŵ
		String apv_data_ste ="";
		String apv_data_red ="";
		String apv_data_com =""; 
		String apv_data_del ="";
			
		apv_data_ste = "update APP_MASTER set app_state='APS' where pid='" + this.doc_pid + "'";  //����
		if(STE.equals("APV")) {
			this.doc_red=anbdt.getTime();		//���繮�� ��������
			apv_data_red = "update APP_MASTER set review_date='" + this.doc_red +"' where pid='" + this.doc_pid + "'";  //��������
			apv_data_com = "update APP_MASTER set review_comment='" + this.doc_rem + "' where pid='" + this.doc_pid + "'";  //�����ǰ�
		} else if(STE.equals("APG")) {
			this.doc_agd=anbdt.getTime();		//���繮�� ��������
			apv_data_red = "update APP_MASTER set agree_date='" + this.doc_agd +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree_comment='" + this.doc_agm + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG2")) {
			this.doc_agd2=anbdt.getTime();		//���繮�� ��������2
			apv_data_red = "update APP_MASTER set agree2_date='" + this.doc_agd2 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree2_comment='" + this.doc_agm2 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG3")) {
			this.doc_agd3=anbdt.getTime();		//���繮�� ��������3
			apv_data_red = "update APP_MASTER set agree3_date='" + this.doc_agd3 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree3_comment='" + this.doc_agm3 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG4")) {
			this.doc_agd4=anbdt.getTime();		//���繮�� ��������4
			apv_data_red = "update APP_MASTER set agree4_date='" + this.doc_agd4 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree4_comment='" + this.doc_agm4 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG5")) {
			this.doc_agd5=anbdt.getTime();		//���繮�� ��������5
			apv_data_red = "update APP_MASTER set agree5_date='" + this.doc_agd5 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree5_comment='" + this.doc_agm5 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG6")) {
			this.doc_agd6=anbdt.getTime();		//���繮�� ��������6
			apv_data_red = "update APP_MASTER set agree6_date='" + this.doc_agd6 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree6_comment='" + this.doc_agm6 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG7")) {
			this.doc_agd7=anbdt.getTime();		//���繮�� ��������7
			apv_data_red = "update APP_MASTER set agree7_date='" + this.doc_agd7 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree7_comment='" + this.doc_agm7 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG8")) {
			this.doc_agd8=anbdt.getTime();		//���繮�� ��������8
			apv_data_red = "update APP_MASTER set agree8_date='" + this.doc_agd8 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree8_comment='" + this.doc_agm8 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG9")) {
			this.doc_agd9=anbdt.getTime();		//���繮�� ��������9
			apv_data_red = "update APP_MASTER set agree9_date='" + this.doc_agd9 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree9_comment='" + this.doc_agm9 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APG10")) {
			this.doc_agd10=anbdt.getTime();		//���繮�� ��������10
			apv_data_red = "update APP_MASTER set agree10_date='" + this.doc_agd10 +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set agree10_comment='" + this.doc_agm10 + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else if(STE.equals("APL")) {
			this.doc_ded=anbdt.getTime();		//���繮�� ��������
			apv_data_red = "update APP_MASTER set decision_date='" + this.doc_ded +"' where pid='" + this.doc_pid + "'";  //���� ����
			apv_data_com = "update APP_MASTER set decision_comment='" + this.doc_dem + "' where pid='" + this.doc_pid + "'";  //���� �ǰ�
		} else {
			apv_data_red ="";
			apv_data_com ="";
		} //if 	
		apv_data_del = "update APP_MASTER set delete_date='" + doc_del_date + "' where pid='" + this.doc_pid + "'";  //��������
					
		//4.�԰� DB�� �űԷ� ������ �Է½�Ŵ[����]
		String apv_data_in = "";
		if(STE.equals("APL")) {				//���ν� app_save�� �԰��Ŵ
			apv_data_in = "INSERT INTO APP_SAVE(pid,app_subj,writer,writer_name,write_date,app_state,reviewer,review_date,review_comment,";
			apv_data_in += "agree,agree_date,agree_comment,agree2,agree2_date,agree2_comment,agree3,agree3_date,agree3_comment,";
			apv_data_in += "agree4,agree4_date,agree4_comment,agree5,agree5_date,agree5_comment,agree6,agree6_date,agree6_comment,";
			apv_data_in += "agree7,agree7_date,agree7_comment,agree8,agree8_date,agree8_comment,agree9,agree9_date,agree9_comment,";
			apv_data_in += "agree10,agree10_date,agree10_comment,agree_method,agree_count,agree_pass,";
			apv_data_in += "decision,decision_date,decision_comment,receivers,app_line,bon_path,";
			apv_data_in += "bon_file,add_counter,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,";
			apv_data_in += "save_period,delete_date,security_level,plid,flag) values('";
			apv_data_in += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + "APS" + "','";
			apv_data_in += doc_rev + "','" + doc_red + "','" + doc_rem + "','" + doc_agr + "','" + doc_agd + "','" + doc_agm + "','";
			apv_data_in += doc_agr2 + "','" + doc_agd2 + "','" + doc_agm2 + "','" + doc_agr3 + "','" + doc_agd3 + "','" + doc_agm3 + "','"; 
			apv_data_in += doc_agr4 + "','" + doc_agd4 + "','" + doc_agm4 + "','" + doc_agr5 + "','" + doc_agd5 + "','" + doc_agm5 + "','";
			apv_data_in += doc_agr6 + "','" + doc_agd6 + "','" + doc_agm6 + "','" + doc_agr7 + "','" + doc_agd7 + "','" + doc_agm7 + "','";
			apv_data_in += doc_agr8 + "','" + doc_agd8 + "','" + doc_agm8 + "','" + doc_agr9 + "','" + doc_agd9 + "','" + doc_agm9 + "','";
			apv_data_in += doc_agr10 + "','" + doc_agd10 + "','" + doc_agm10 + "','" + agr_met + "','" + Integer.toString(agr_cnt) + "','" + agr_pas + "','";
			apv_data_in += doc_dec + "','" + doc_ded + "','" + doc_dem + "','" + doc_rec + "','" + doc_lin + "','" + doc_pat + "','";
			apv_data_in += doc_bon + "','" + doc_atcnt + "','" + doc_or1 + "','" + doc_ad1 + "','" + doc_or2 + "','" + doc_ad2 + "','" + doc_or3 + "','" + doc_ad3 + "','";
			apv_data_in += doc_per + "','" + doc_del_date + "','" + doc_sec + "','"+ doc_lid + "','" + doc_flag+"')";
		}

		//5. DB�� ���� ���๮
		Message="";
		Statement stmt = null;
		stmt = con.createStatement();
		stmt.executeUpdate(apv_data_ste);		////System.out.println("apv_data_ste : " + apv_data_ste);
		stmt.executeUpdate(apv_data_red);		////System.out.println("apv_data_red : " + apv_data_red);
		stmt.executeUpdate(apv_data_com);		////System.out.println("apv_data_com : " + apv_data_com);
		stmt.executeUpdate(apv_data_del);		////System.out.println("apv_data_del : " + apv_data_del);
		stmt.executeUpdate(apv_data_in);		////System.out.println("apv_data_in : " + apv_data_in);
		stmt.close();

		if(STE.equals("APL")) {		//���ν� 
			eleInformSave();		//�̸�,�μ���,���� update
		}

		Message="APPROVAL"; 
	
		return Message;
	}

	/******************************************************************************
		����Ϸ�� ���� �ۼ��ںμ��ڵ�,�̸�,�μ���,���� update �ϱ� 
	*******************************************************************************/
	private void eleInformSave() throws SQLException 
	{
		String[] id_name = {doc_wri,doc_rev,doc_dec,doc_agr,doc_agr2,doc_agr3,doc_agr4,doc_agr5,doc_agr6,doc_agr7,doc_agr8,doc_agr9,doc_agr10};
		String[] Aname = {"writer","reviewer","decision","agree","agree2","agree3","agree4","agree5","agree6","agree7","agree8","agree9","agree10"};
		String query = "";
		String name = "";		//�̸� 
		String div = "";		//�μ��� 
		String rank = "";		//���� 
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select distinct a.id,a.name,b.ac_name,c.ar_name from ";
		query += "user_table a,class_table b,rank_table c ";

		for(int i=0; i<id_name.length; i++) {
			if(id_name[i].length() != 0) {
				String query1 = query + "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+id_name[i]+"'";
				rs = stmt.executeQuery(query1);
				while(rs.next()) {
					name = rs.getString("name");
					div = rs.getString("ac_name");
					rank = rs.getString("ar_name");	
				}
				String query2 = "update APP_SAVE set "+Aname[i]+"_name='"+name+"',"+Aname[i]+"_div='"+div+"',"+Aname[i]+"_rank='"+rank+"' where pid='"+this.doc_pid+"'";
				stmt.executeUpdate(query2);
			}
		}				
		stmt.close();
		rs.close();
	}

	/******************************************************************************
		����Ϸ�� ������ �뺸�ڰ� ������� ó���ϱ� 
		(APP_RECEIVE:�뺸) 
	******************************************************************************/
	public void eleDecisionInform(String plid,String flag) throws SQLException 
	{
		//���޺��� 
		String lid = plid;			//��Ÿ���� ���ڰ��� ��Ž� ������ȣ 
		String doc_flag = flag;		//������ ���� (SERVICE:������, BOM:��ǰ����Ʈ ��)
		String query = "";

		Statement stmt = null;
		ResultSet rs = null;
		
		//1. �뺸�� �ο� �ľ��ϱ� 
		rec_str_cnt = eleDecisionReceiver();
//		//System.out.println("�뺸�� �� : " + rec_str_cnt);	

		//2. �������� ���ϱ�
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//ó���� ��� (1����)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1��
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2��
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3��
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5��
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//���� (100��)

		
		//3.�뺸DB (app_reveive)�� ������ �����ϱ�
		for(int rec_on = 0 ; rec_on < rec_str_cnt; rec_on++){
//			//System.out.println("�뺸�� ��� : " + receiver[rec_on]);
			if(checkSabun(receiver[rec_on])) {				//���κ��� �뺸�� �����
				stmt = con.createStatement();
				query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
				query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+receiver[rec_on]+"'";
//				//System.out.println("���� query :" + query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					String receive_data = "";
					receive_data = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
					receive_data += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
					receive_data += receiver[rec_on]+"','"+doc_del_date+"','"+lid+"','"+"0"+"','" + anbdt.getTime() + "','" + doc_flag + "','"+rs.getString("name")+"','"+rs.getString("ac_name")+"','"+rs.getString("ar_name")+"')";
//					//System.out.println("���:" + receive_data);
					// DB�� ���� ���๮
					stmt.executeUpdate(receive_data);	
				} //while
				stmt.close();
				rs.close();
			} 
			else  {		//�μ����� ���� ���κ� ����� ã�� �뺸�Ѵ�.
				//�ֻ��� �μ��� ��� �����μ��� ���ο��� �뺸�Ѵ�.
				//1. �����μ� ��� ã��
				searchDiv(receiver[rec_on]);		//�����μ� ã�� [�迭 sdiv ]�� �ӽ÷� ��´�. private����
				int div_cnt = sdiv.length;			//�����μ� ����

				//2. �μ����� ã�� ���� �뺸�ϱ�
				for(int n=0; n<div_cnt; n++) {
					stmt = con.createStatement();	
					query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
					query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.ac_id='"+sdiv[n]+"'";
//					//System.out.println("�μ� query :" + query);
					rs = stmt.executeQuery(query);
					while(rs.next()) {
						String receive_data = "";
						
						//�����μ��� ��� ���,����,����,������ ����
						String sabun = rs.getString("id");			//���
						String name = rs.getString("name");			//�̸�
						String ac_name = rs.getString("ac_name");	//�μ���
						String ar_name = rs.getString("ar_name");	//���޸�
						sendInform(sabun,name,ac_name,ar_name,lid,doc_del_date);
					} //while
					stmt.close();
					rs.close();
				} //for
			} //if
		} //for	
	}

	/******************************************************************************
		����Ϸ�� ������ �뺸�� �߰� ó���ϱ� 
		(APP_RECEIVE:�뺸) 
	******************************************************************************/
	public void addInformReceiver(String pid,String receivers) throws SQLException 
	{
		//���޺��� 
		String query = "",sabun="",input="",lid="",flag="",exist_id="",where="",update="";
		String recs="",app_line="";		//������ ���̺��� �뺸�� �� �����������
		String[] data = new String[3]; 

		//�������
		eleDecisionReadDoc(pid);	//�󼼳��� �б�
		eleDecisionLineOrder();		//���缱 �ؼ�
		lid=doc_lid;flag=doc_flag;

		Statement stmt = null;
		stmt = con.createStatement();
		
		//�������� ���ϱ�
		String doc_del_date="";
		if(this.doc_per.equals("0"))	doc_del_date = anbdt.getAddMonthNoformat(1);		//ó���� ��� (1����)
		else if(this.doc_per.equals("1")) doc_del_date = anbdt.getAddYearNoformat(1);		//1��
		else if(this.doc_per.equals("2")) doc_del_date = anbdt.getAddYearNoformat(2);		//2��
		else if(this.doc_per.equals("3")) doc_del_date = anbdt.getAddYearNoformat(3);		//3��
		else if(this.doc_per.equals("5")) doc_del_date = anbdt.getAddYearNoformat(5);		//5��
		else if(this.doc_per.equals("EVER")) doc_del_date = anbdt.getAddYearNoformat(100);	//���� (100��)

		StringTokenizer rlist = new StringTokenizer(receivers,"\n");
		while(rlist.hasMoreTokens()) {
			String id_name = rlist.nextToken();
			StringTokenizer receiver = new StringTokenizer(id_name,"/");
			data[0]=data[1]=data[2]="";
			while(receiver.hasMoreTokens()) {
				sabun = receiver.nextToken();
				data = getRankInfo(sabun);			//����,�μ���,���޸� ���ϱ�

				//�̹� ������ �뺸������ �Ǵ�
				where = "where pid='"+pid+"' and receiver='"+sabun+"'";
				exist_id=getColumnData("app_receive","receiver",where);

				//�ش����� �̸��� �ְ�, �����۵� �뺸�� �̸� �����Ѵ�.
				if(data[0].length() != 0 && exist_id.length() == 0) {
					//�뺸�� �߰�
					input = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
					input += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna + "','" + doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
					input += sabun+"','"+doc_del_date+"','"+lid+"','"+"0"+"','" + anbdt.getTime() + "','" + doc_flag + "','"+data[0]+"','"+data[1]+"','"+data[2]+"')";
					////System.out.println("input :" + input);
					stmt.executeUpdate(input);

					//�뺸�� �����Ϳ� update
					//1.������ο� �뺸�� ���ԵǾ��� Ȯ��
					where = "where pid='"+pid+"'";
					app_line=getColumnData("app_master","app_line",where);

					//2.�뺸�� �߰��ϱ�
					where = "where pid='"+pid+"'";
					recs=getColumnData("app_master","receivers",where);
					if(app_line.indexOf("API") == -1) { 
						recs = sabun; 
						app_line += ",API"; 
					} else recs += ","+sabun;

					//3.�����Ϳ� update�ϱ�
					update = "UPDATE app_master SET receivers='"+recs+"',app_line='"+app_line+"' ";
					update += "where pid='"+pid+"'";
					////System.out.println("update :" + update);
					stmt.executeUpdate(update);
					
				}
			} //while
		} //while

		//�ݱ�
		stmt.close();
	}

	/******************************************************************************
		������� �̸�,�μ���,���� ã��
	******************************************************************************/
	public String[] getRankInfo(String sabun) throws SQLException 
	{
		//���޺��� 
		String query = "";
		String[] data = new String[3]; 
		data[0]=data[1]=data[2]="";

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		query = "select distinct a.id,a.name,b.ac_name,c.ar_name from user_table a, class_table b, rank_table c ";
		query += "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+sabun+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("name");			//�̸�
			data[1] = rs.getString("ac_name");		//�μ� �̸�
			data[2] = rs.getString("ar_name");		//���� �̸�
		}
////System.out.println("data :" + data[0]+" : "+data[1]+" : "+data[2]);
		//�ݱ�
		stmt.close();
		rs.close();

		return data;
	}

	/******************************************************************************
		����Ϸ�� ������ �뺸�ڰ� ������� �ڵ��� ���� �Ǵ��ϱ� 
		�뺸�� �ڵ尡 �������, �μ������ڵ����� �Ǵ��ϱ�
	******************************************************************************/
	private boolean checkSabun(String id) throws SQLException 
	{
		boolean rtn = true;			//�����

		//1. ���ڷ� �����Ǿ��� �Ǵ��ϱ� [num:����, Nnum:���ĺ����� (�μ������ڵ�� ac_id : tinyint����)]
		String tag = "num";	
		try{ Integer.parseInt(id); } catch(Exception e) {tag = "Nnum"; }
//		//System.out.println("tag : " + tag);

		//2. ���ڷ� ������ ��� �μ������ڵ� ������ �Ǵ��Ѵ�.
		String div = "Ndiv";		//�μ����� �Ǵ� div:�μ�, Ndiv:����
		if(tag.equals("num")) {
			Statement stmt = null;
			ResultSet rs = null;
			stmt = con.createStatement();
			String query = "select distinct ac_id from class_table where ac_id='"+id+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) div = "div";
			else div = "Ndiv";
			stmt.close();
			rs.close();
		}
//		//System.out.println("div : " + div);

		//3. ������� �Ǵ��ϱ� [���:true, �μ������ڵ�:false]
		if(div.equals("div")) rtn = false;
		else rtn = true;
		return rtn;
		
	}

	/******************************************************************************
		�μ��뺸�ڸ� ������ �����Ͽ� �뺸ó�� �ϱ�
	******************************************************************************/
	private void sendInform(String sabun,String name,String ac_name,String ar_name,String lid,String deldate) throws SQLException 
	{
		String receive_data = "";

		Statement stmt = null;
		stmt = con.createStatement();

		if(!doc_wri.equals(sabun) && !doc_rev.equals(sabun) && !doc_agr.equals(sabun) && 
				!doc_agr2.equals(sabun) && !doc_agr3.equals(sabun) && !doc_agr4.equals(sabun) && 
				!doc_agr5.equals(sabun) && !doc_agr6.equals(sabun) && !doc_agr7.equals(sabun) &&
				!doc_agr8.equals(sabun) && !doc_agr9.equals(sabun) && !doc_agr10.equals(sabun) &&
				!doc_dec.equals(sabun)) {
			//�뺸�μ��� ��ܿ� �����뺸�ڰ� ���ԵǸ� skip
			int dind = doc_rec.indexOf(sabun);
			if(dind == -1) {
				receive_data = "INSERT INTO APP_RECEIVE(pid,app_subj,writer,writer_name,write_date,add_counter,isOpen,receiver,delete_date,plid,send_bom,request_date,flag,receiver_name,receiver_div,receiver_rank) values('";
				receive_data += doc_pid + "','" + doc_sub + "','" + doc_wri + "','" + doc_wna+"','"+doc_wrd + "','" + doc_atcnt + "','" + "0" + "','";
				receive_data += sabun + "','" + deldate + "','" + lid + "','" + "0" + "','" + anbdt.getTime() + "','" + doc_flag + "','"+name+"','"+ac_name+"','"+ar_name+"')";
				//System.out.println("�μ�:" + receive_data);
				// DB�� ���� ���๮
				stmt.executeUpdate(receive_data);
			}
		}				
		stmt.close();
	}

	/******************************************************************************
		�뺸�ڰ� �μ��� ��� �����μ� ��� ã��
	******************************************************************************/
	private void searchDiv(String div) throws SQLException 
	{
		String query = "";
		String code = "";			//code
		int div_cnt = 0;

		Statement stmt = null;
		ResultSet rs = null;

		//1.�־��� �μ������ڵ�� �ش�μ��� �ڵ�ã��
		stmt = con.createStatement();
		query = "select distinct code from class_table where ac_id='"+div+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) code = rs.getString("code");
		stmt.close();
		rs.close();

		//2.ã�� code�� ���Ե� ��� �μ������ڵ��� ������ �ľ��Ѵ�.
		stmt = con.createStatement();
		query = "select count(*) from class_table where code like '"+code+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			div_cnt = rs.getInt(1);
		}
		stmt.close();
		rs.close();

		//�迭����
		sdiv = new String[div_cnt];
		for(int i=0; i<div_cnt; i++) sdiv[i] = "";

		//3.ã�� code�� ���Ե� ��� �μ������ڵ� ã��
		stmt = con.createStatement();
		query = "select distinct ac_id from class_table where code like '"+code+"%'";
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			sdiv[n] = rs.getString("ac_id");
			n++;
		}
		stmt.close();
		rs.close();

	}

	/******************************************************************************
		���� ������ �����ڰ� ������� ó���ϱ� (������ �����ϰ�츸 ���) 
		(status :����, docid :������ȣ, comment:�ǰ�) 
	******************************************************************************/
	public void eleSerialAgree(String status,String docid,String comment) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		for(int ap = 0; ap <= this.lin_str_cnt; ap++) {		//������� �迭�� �д´�.
			if(LINE_ORD[ap].equals(status)) {				//�������¸� �����ܰ�� ó��
				//�������� ���� (���Ǵܰ迡�� ���繮���� ������ �����԰�)
				if(ap == this.lin_str_cnt) {	
					eleDecisionSave(status,comment); 
				}  
				else {	//��������� �����ܰ�� ���� (������ DB�� ������Ʈ ��Ŵ [����,��������,�����ǰ�])
					String doc_agd=anbdt.getTime();		//���繮�� ��������
					String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";			
					if(status.equals("APG2"))
						   query += "agree2_date='" + doc_agd +"', agree2_comment='" + this.doc_agm2+comment + "' where pid='" + docid + "'";	
					else if(status.equals("APG3"))
						   query += "agree3_date='" + doc_agd +"', agree3_comment='" + this.doc_agm3+comment + "' where pid='" + docid + "'";	
					else if(status.equals("APG4"))
						   query += "agree4_date='" + doc_agd +"', agree4_comment='" + this.doc_agm4+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG5"))
						   query += "agree5_date='" + doc_agd +"', agree5_comment='" + this.doc_agm5+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG6"))
						   query += "agree6_date='" + doc_agd +"', agree6_comment='" + this.doc_agm6+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG7"))
						   query += "agree7_date='" + doc_agd +"', agree7_comment='" + this.doc_agm7+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG8"))
						   query += "agree8_date='" + doc_agd +"', agree8_comment='" + this.doc_agm8+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG9"))
						   query += "agree9_date='" + doc_agd +"', agree9_comment='" + this.doc_agm9+comment + "' where pid='" + docid + "'";
					else if(status.equals("APG10"))
						   query += "agree10_date='" + doc_agd +"', agree10_comment='" + this.doc_agm10+comment + "' where pid='" + docid + "'";
					// DB�� ���� ���๮
					stmt.executeUpdate(query);
				} //if		
			} //if
		} //for	
		stmt.close();	
	}
	
	/******************************************************************************
		�뺸�� �ο��� �����ϱ� 
		���� : �뺸�� �ο�
	******************************************************************************/
	public int getInformCount() throws SQLException 
	{
		return this.rec_str_cnt;
	}
		
	/******************************************************************************
		���缱 ����� �����ϱ� 
		���� : ���缱�� 
	******************************************************************************/
	public int getLineCount() throws SQLException 
	{
		return this.lin_str_cnt;
	}

	/******************************************************************************
		����ܰ� ������ ó���ϱ�
	******************************************************************************/
	public void processAPV(String pid,String comment) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//�������
		eleDecisionReadDoc(pid);	//�󼼳��� �б�
		eleDecisionLineOrder();		//���缱 �ؼ�

		//������ ó���ϱ�
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//������� �迭�� �д´�.
			if(LINE_ORD[ap].equals("APV")) {			//�������¸� �����ܰ�� ó��
				//�������� ����
				if(ap == lin_str_cnt) {					//����ܰ迡�� ���繮���� ������ �����԰�
					eleDecisionSave("APV",comment);
				}
				else {					//��������� �����ܰ�� ����.
					//������ DB�� ������Ʈ ��Ŵ [����,��������,�����ǰ�]
					String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";	//����
						  query += "review_date='" + anbdt.getTime() +"', ";						//��������
						  query += "review_comment='" + this.doc_rem+comment + "' where pid='" + pid + "'";  //�����ǰ�
					
					// DB�� ���� ���๮
					stmt.executeUpdate(query);
				} //if		
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		���δܰ� ������ ó���ϱ�
	******************************************************************************/
	public void processAPL(String pid,String comment) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//�������
		eleDecisionReadDoc(pid);	//�󼼳��� �б�
		eleDecisionLineOrder();		//���缱 �ؼ�

		//������ ó���ϱ�
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//������� �迭�� �д´�.
			if(LINE_ORD[ap].equals("APL")) {			//�������¸� �����ܰ�� ó��
				//------------------------------
				//������������� ������ �԰� ��Ų��.
				//------------------------------
				eleDecisionSave("APL",comment);
	
				//-------------------------------
				//��Ÿ���� ���ڰ����Ž� ���ڰ��� �Ϸ�(EF)���� �˷��ֱ�(�������� ������)
				//-------------------------------
				if(this.doc_flag.equals("SERVICE")) {		//������ ���� ������
					StringTokenizer strid = new StringTokenizer(this.doc_lid,",");//�޸��� ���е� ���ù���
					while(strid.hasMoreTokens()) {
						String EE = "update HISTORY_TABLE set flag='EF' where ah_id='"+strid.nextToken()+"'";
						stmt.executeUpdate(EE);
					} //while
				}
				//��� �����ΰ�� 
				else if(doc_flag.equals("TD")) {	
					String[] data = new String[3]; 
					if(doc_lid.length() != 0) {
						StringTokenizer num = new StringTokenizer(doc_lid,"|");
						int sn = 0;
						while(num.hasMoreTokens()) {
							data[sn] = num.nextToken();				//������ȣ,table name,version
							sn++;
						}
					}
					mdBO.approvalTD(pid,data[0],data[1],data[2]);	//���ڰ�������ڵ�,������������ڵ�,tablename,version
				}
				//���ο� �����ΰ�� 
				else if(doc_flag.equals("AKG")) {	
					mdBO.approvalCA(pid,doc_lid);		//���ڰ�������ڵ�,���ο������ڵ�
				}
				//�Ϲݺ��� �����ΰ�� 
				else if(doc_flag.equals("GEN")) {
					setAcCode();				 					//�μ��ڵ� �Է��ϱ� (app_save)
					numberingDoc("APP_SAVE",doc_flag,"pid",doc_pid);	//������ȣ �Է�
				}
				//���� ������ ���
				else if(doc_flag.equals("EST")) {
					String estimate_no="";			//������ȣ
					String ver = "";				//��������
					int bar = doc_lid.indexOf("|");
					estimate_no = doc_lid.substring(0,bar);
					ver = doc_lid.substring(bar+1,doc_lid.length());

					com.anbtech.em.db.EstimateDAO estDAO = new com.anbtech.em.db.EstimateDAO(con);
					String real_est_no = estDAO.calculateEstimateNo();

					com.anbtech.em.business.EstimateBO estBO = new com.anbtech.em.business.EstimateBO(con);
					estBO.makeCommitStat(estimate_no,ver,doc_pid,real_est_no);

					//����������ȣ ���Ĺ�ȣ�� �Է��ϱ�
					String n_plid = real_est_no+"|"+ver;
					updateColumn("APP_MASTER","plid",n_plid,pid);		//��������
					updateColumn("APP_SAVE","plid",n_plid,pid);		//������

				}
				//Ư�ٰ��� �����ΰ�� 
				else if(doc_flag.equals("EWK")) {
					com.anbtech.ew.business.ExtraWorkModuleBO ewkBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);//Ư�ٽ�û
					ewkBO.ewAppInfoProcess(doc_pid,doc_lid,"approval");
				}
				//BOM���� �����ΰ�� 
				else if(doc_flag.equals("BOM")) {
					com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//BOM���ν�û
					String where = "where id='"+doc_dec+"'";
					String app_name = getColumnData("user_table","name",where);
					bomDAO.setBomStatus(doc_lid,"5",doc_dec,app_name,doc_pid);
				}
				//���躯����� �����ΰ�� 
				else if(doc_flag.equals("DCM")) {
					com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//���躯����ν�û
					int sep_no = doc_lid.indexOf("|");
					int len_no = doc_lid.length();
					String eco_pid = doc_lid.substring(0,sep_no);
					String eco_no = doc_lid.substring(sep_no+1,len_no);
					bomDAO.setEccStatus(eco_pid,doc_pid,"app");
				}
				//���ſ�û���� �����ΰ�� 
				else if(doc_flag.equals("PCR")) {
					com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);//MRP 
					com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);//MRP
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
					int sep_no = doc_lid.indexOf("|");
					int len_no = doc_lid.length();
					String req_pid = doc_lid.substring(0,sep_no);			//���Ű�����ȣ
					String req_type = doc_lid.substring(sep_no+1,len_no);	//����TYPE (MRP : ������� MRP���ſ�û)
					//������� MRP���ſ�û�� ��츸 ����
					if(req_type.equals("MRP")) {
						String where = "where pu_req_no = '"+req_pid+"'";
						String mrp_pid = mrpDAO.getColumData("MRP_MASTER","pid",where);
						mrpBO.setMrpStatus(mrp_pid,"31","3",""); 
					}
					purBO.puRequestAppInfoProcess("commit_req",doc_pid,req_pid);
				}
				//���ֿ�û���� �����ΰ�� 
				else if(doc_flag.equals("ODR")) {
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
					purBO.puOrderAppInfoProcess("commit_order",doc_pid,doc_lid);
				}
				//�����԰���� �����ΰ�� 
				else if(doc_flag.equals("PWH")) {
					com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
					purBO.puEnterAppInfoProcess("commit_enter",doc_pid,doc_lid);
				}
				//��ǰ������ �����ΰ�� 
				else if(doc_flag.equals("TGW")) {
					com.anbtech.st.business.StockMgrBO stcBO = new com.anbtech.st.business.StockMgrBO(con);//������
					stcBO.DeliveryAppInfoProcess("commit_delivery",doc_lid,doc_lid);
				}
				//���ڰ��� ����� ���
				else {
					formPaperUp(doc_flag,doc_lid);
				}

				//-------------------------------
				//���� ���� --> �뺸�� �Ѿ�� �뺸DB (app_reveive)�� ������ �����ϱ�
				//-------------------------------
				if((ap < lin_str_cnt) && (LINE_ORD[ap+1].equals("API"))) {		
					eleDecisionInform(this.doc_lid,this.doc_flag);	//�뺸�� ���̺� ��� (��Ÿ����������ȣ ����)
				} //if	
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		�����ܰ� ������ ó���ϱ�
	******************************************************************************/
	public void processAPG(String pid,String comment,String login_owner) throws SQLException 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//�������
		eleDecisionReadDoc(pid);	//�󼼳��� �б�
		eleDecisionLineOrder();		//���缱 �ؼ�

		//������ ó���ϱ�
		for(int ap = 0; ap <= lin_str_cnt; ap++) {		//������� �迭�� �д´�.
			if(LINE_ORD[ap].equals("APG")) {		//�������¸� �����ܰ�� ó��
				//�������� ���� (���Ǵܰ迡�� ���繮���� ������ �����԰�)
				if(ap == lin_str_cnt) {	eleDecisionSave("APG",comment); }
				else {	//��������� �����ܰ�� ���� (������ DB�� ������Ʈ ��Ŵ [����,��������,�����ǰ�])
					String doc_agd=anbdt.getTime();		//���繮�� ��������

					//���Ǵܰ� ������ ���� (���Ǵܰ� �ݿ�)
					if(this.agr_met.equals("SERIAL")) {  	
						String query = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"', ";			//����
							query += "agree_date='" + doc_agd +"', ";										//��������
							query += "agree_comment='" + this.doc_agm+comment + "' where pid='" + doc_pid + "'";		//�����ǰ�
						// DB�� ���� ���๮
						stmt.executeUpdate(query);					

					//���Ǵܰ� �ϰ��� ���� (�� ������ �ο��� �� ���Ǵܰ� �ݿ�)
					} else if (agr_met.equals("PARALLEL")) {  	
						int pass_cnt = Integer.parseInt(this.agr_pas);	//���� ������ ��
						pass_cnt += 1;									//�� ������ �ڽ��� �ο��� �����Ѵ�.
						String updata = "";								//update����
						// ���ǻ��� �� �� ������ ���ο���(pass_cnt) �Է�  [���ο��� : this.agr_cnt]
						if(this.agr_cnt > pass_cnt) {			//����������
							updata = "update APP_MASTER set app_state='" + LINE_ORD[ap] +"',";				//����
							updata += "agree_pass='" + Integer.toString(pass_cnt) +"',";					//������ �ο�
						} else {								//������ ������ ����
							updata = "update APP_MASTER set app_state='" + LINE_ORD[ap+1] +"',";			//����
							updata += "agree_pass='" + Integer.toString(pass_cnt) +"',";					//������ �ο�
						} //if
			
						// �� �����ں� �������� �� �ǰ��Է�
						if(login_owner.equals(this.doc_agr)){			//������
							updata += "agree_date='" + doc_agd +"',";						  				//��������
							updata += "agree_comment='" + this.doc_agm+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�
						} else if(login_owner.equals(this.doc_agr2)){	//������2
							updata += "agree2_date='" + doc_agd +"',";  									//��������
							updata += "agree2_comment='" + this.doc_agm2+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�2
						} else if(login_owner.equals(this.doc_agr3)){	//������3
							updata += "agree3_date='" + doc_agd +"',";  									//��������
							updata += "agree3_comment='" + this.doc_agm3+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�3
						} else if(login_owner.equals(this.doc_agr4)){	//������4
							updata += "agree4_date='" + doc_agd +"',"; 										//��������
							updata += "agree4_comment='" + this.doc_agm4+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�4
						} else if(login_owner.equals(this.doc_agr5)){	//������5
							updata += "agree5_date='" + doc_agd +"',"; 										//��������
							updata += "agree5_comment='" + this.doc_agm5+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�5
						} else if(login_owner.equals(this.doc_agr6)){	//������6
							updata += "agree6_date='" + doc_agd +"',"; 										//��������
							updata += "agree6_comment='" + this.doc_agm6+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�6
						} else if(login_owner.equals(this.doc_agr7)){	//������7
							updata += "agree7_date='" + doc_agd +"',";  									//��������
							updata += "agree7_comment='" + this.doc_agm7+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�7
						} else if(login_owner.equals(this.doc_agr8)){	//������8
							updata += "agree8_date='" + doc_agd +"',"; 										//��������
							updata += "agree8_comment='" + this.doc_agm8+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�8
						} else if(login_owner.equals(this.doc_agr9)){	//������9
							updata += "agree9_date='" + doc_agd +"',";  									//��������
							updata += "agree9_comment='" + this.doc_agm9+comment + "' where pid='" + doc_pid + "'";  	//�����ǰ�9
						} else if(login_owner.equals(this.doc_agr10)){	//������10
							updata += "agree10_date='" + doc_agd +"',"; 									//��������
							updata += "agree10_comment='" + this.doc_agm10+comment + "' where pid='" + doc_pid + "'";  //�����ǰ�10
						} //if
						// DB�� ���� ���๮
						stmt.executeUpdate(updata);
					} //if
				} //if		
			} //if
		} //for
		stmt.close();
	}

	/******************************************************************************
		�ݷ� ������ ó���ϱ�
	******************************************************************************/
	public void processAPR(String pid,String comment) throws Exception  
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		//�������
		eleDecisionReadDoc(pid);	//�󼼳��� �б�
		eleDecisionLineOrder();		//���缱 �ؼ�
		
		//������ ó���ϱ�
		String query = "update APP_MASTER set app_state='APR',";
		if("APV".equals(this.doc_ste)) 				//����ܰ迡�� �ݷ��� �ǰ��� �������
			query += "review_date='" + anbdt.getTime() +"',review_comment='" + this.doc_rem+comment + "',";
		else if("APL".equals(this.doc_ste)) 		//���δܰ迡�� �ݷ��� �ǰ��� ���ζ���
			query += "decision_date='" + anbdt.getTime() +"',decision_comment='" + this.doc_dem+comment + "',";
		query += "delete_date='" + anbdt.getAddDateNoformat(7) + "' where pid='" + pid + "'";  //����������
		stmt.executeUpdate(query);

		//��Ÿ���� ���ڰ����Ž� ���ڰ��� �ݷ����� �˷��ֱ�(�������� ������)
		if(this.doc_flag.equals("SERVICE")) {		//������ ���� ������
			StringTokenizer strid = new StringTokenizer(doc_lid,",");//�޸��� ���е� ���ù���
			while(strid.hasMoreTokens()) {
				String EE = "update HISTORY_TABLE set flag='' where ah_id='"+strid.nextToken()+"'";
				stmt.executeUpdate(EE);
			} //while
		} 
		//��� �����ΰ�� 
		else if(doc_flag.equals("TD")) {	
			String[] data = new String[3]; 
			if(doc_lid.length() != 0) {
					StringTokenizer num = new StringTokenizer(doc_lid,"|");
					int sn = 0;
					while(num.hasMoreTokens()) {
						data[sn] = num.nextToken();				//������ȣ,table name,version
						sn++;
					}
			}
			mdBO.rejectTD(pid,data[0],data[1],data[2]);	//���ڰ�������ڵ�,������������ڵ�,tablename,version
		} 
		//���ο� �����ΰ�� 
		else if(doc_flag.equals("AKG")) {	
			int no = doc_lid.indexOf("|");
			String tm_id = doc_lid.substring(0,no);							//���ΰ�����ȣ
			String tm_report = doc_lid.substring(no+1,doc_lid.length());	//��������
			//�űԵ�� 
			if(tm_report.equals("report_w")) {
				String EN = "update ca_master set aid='EN' where tmp_approval_no='"+tm_id+"'";
				stmt.executeUpdate(EN);
			}
		}
		//���� ������ ���
		else if(doc_flag.equals("EST")) {
			String estimate_no="";			//������ȣ
			String ver = "";				//��������
			int bar = doc_lid.indexOf("|");
			estimate_no = doc_lid.substring(0,bar);
			ver = doc_lid.substring(bar+1,doc_lid.length());

			com.anbtech.em.business.EstimateBO estBO = new com.anbtech.em.business.EstimateBO(con);
			estBO.makeReturnStat(estimate_no,ver);
		}
		//Ư�ٰ��� �����ΰ�� 
		else if(doc_flag.equals("EWK")) {
			com.anbtech.ew.business.ExtraWorkModuleBO ewkBO = new com.anbtech.ew.business.ExtraWorkModuleBO(con);//Ư�ٽ�û
			ewkBO.ewAppInfoProcess(doc_pid,doc_lid,"reject");
		}
		//BOM���� �����ΰ�� 
		else if(doc_flag.equals("BOM")) {
			com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//BOM���ν�û
			String where = "where id='"+doc_dec+"'";
			String app_name = getColumnData("user_table","name",where);
			bomDAO.setBomStatus(doc_lid,"0",doc_dec,app_name,doc_pid);
		}
		//���躯����� �����ΰ�� 
		else if(doc_flag.equals("DCM")) {
			com.anbtech.bm.db.BomApprovalDAO bomDAO = new com.anbtech.bm.db.BomApprovalDAO(con);//���躯����ν�û
			int sep_no = doc_lid.indexOf("|");
			int len_no = doc_lid.length();
			String eco_pid = doc_lid.substring(0,sep_no);
			String eco_no = doc_lid.substring(sep_no+1,len_no);
			bomDAO.setEccStatus(eco_pid,doc_pid,"rej");			//ECC_COM,ECC_REQ or ECC_ORD
			bomDAO.setEccBomStatus(eco_no,"0");					//ECC_BOM
		}
		//���ſ�û���� �����ΰ�� 
		else if(doc_flag.equals("PCR")) {
			com.anbtech.mm.db.mrpModifyDAO mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);//MRP 
			com.anbtech.mm.business.mrpInputBO mrpBO = new com.anbtech.mm.business.mrpInputBO(con);//MRP
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
			int sep_no = doc_lid.indexOf("|");
			int len_no = doc_lid.length();
			String req_pid = doc_lid.substring(0,sep_no);			//���Ű�����ȣ
			String req_type = doc_lid.substring(sep_no+1,len_no);	//����TYPE (MRP : ������� MRP���ſ�û)
			//������� MRP���ſ�û�� ��츸 ����
			if(req_type.equals("MRP")) {
				String where = "where pu_req_no = '"+req_pid+"'";
				String mrp_pid = mrpDAO.getColumData("MRP_MASTER","pid",where);
				mrpBO.setMrpStatus(mrp_pid,"3","0",""); 
			}
			purBO.puRequestAppInfoProcess("reject_req",doc_pid,req_pid);
		}
		//���ֿ�û���� �����ΰ�� 
		else if(doc_flag.equals("ODR")) {
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
			purBO.puOrderAppInfoProcess("reject_order",doc_pid,doc_lid);
		}
		//�����԰���� �����ΰ�� 
		else if(doc_flag.equals("PWH")) {
			com.anbtech.pu.business.PurchaseMgrBO purBO = new com.anbtech.pu.business.PurchaseMgrBO(con);//���Ű���
			purBO.puEnterAppInfoProcess("reject_enter",doc_pid,doc_lid);
		}
		//��ǰ������ �����ΰ�� 
		else if(doc_flag.equals("TGW")) {
			com.anbtech.st.business.StockMgrBO stcBO = new com.anbtech.st.business.StockMgrBO(con);//������
			stcBO.DeliveryAppInfoProcess("reject_delivery",doc_lid,doc_lid);
		}
		//��Ÿ ��Ĺ����� ���
		else {
			formPaperDown(doc_flag,doc_lid,comment);	
		}
		stmt.close();
	}
	/******************************************************************************
		����Ϸ�� �Ϲݺ����� �ۼ��� �μ��ڵ� update�ϱ� (app_save��)
	*******************************************************************************/
	private void setAcCode() throws SQLException 
	{
		String name = "";			//�̸�
		String ac_code = "";		//�μ��ڵ�
		String div = "";			//����θ�
		String rank = "";			//���޸�
		String query = "";
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select distinct a.id,a.name,b.ac_code,b.ac_name,c.ar_name from ";
		query += "user_table a,class_table b,rank_table c ";

		if(doc_wri.length() != 0) {
			String query1 = query + "where a.ac_id = b.ac_id and a.rank = c.ar_code and a.id='"+doc_wri+"'";
			//System.out.println("q1 : " + query1);
			rs = stmt.executeQuery(query1);
			while(rs.next()) {
				name = rs.getString("name");
				ac_code = rs.getString("ac_code");
				div = rs.getString("ac_name");
				rank = rs.getString("ar_name");	
			}
			String query2 = "update APP_SAVE set ac_code='"+ac_code+"' where pid='"+this.doc_pid+"'";
			//System.out.println("q2 : " + query2);
			stmt.executeUpdate(query2);
		}
					
		stmt.close();
		rs.close();
	}
	/******************************************************************************
		�� ��Ĺ����� ������ ó���ϱ� (���ο��ι� ����������ȣ)
	******************************************************************************/
	private void formPaperUp(String doc_flag,String doc_lid) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		String data = "";		//update
		String tag = "";		//flag or flag2 

		//���°���(�ް���,�����,�����û��)
		if(doc_flag.equals("HYU_GA") || doc_flag.equals("OE_CHUL") || doc_flag.equals("CHULJANG_SINCHEONG")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("GEUNTAE_MASTER","gt_id",doc_lid);

			//������� Ȯ��
			data = "update GEUNTAE_MASTER set "+tag+"='EF' where gt_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �� �����ϼ� �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("GEUNTAE_MASTER",doc_flag,"gt_id",doc_lid);	//������ȣ �Է�

				//�����ϼ� �Է�(geuntae_count)
				if(doc_flag.equals("HYU_GA")) {								//�ް���
					try {hyugaDayDAO.processCount(doc_lid); } catch (Exception e) {} 	
				} else if(doc_flag.equals("CHULJANG_SINCHEONG"))	{		//����
					try {chuljangDayDAO.processChulJangCount(doc_lid);} catch (Exception e) {} 
				} else if(doc_flag.equals("OE_CHUL")) {						//����
					try {chuljangDayDAO.processEoChulCount(doc_lid);} catch (Exception e) {} 
				}

			}

		} 
		//��������(������û)
		else if(doc_flag.equals("BAE_CHA")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("CHARYANG_MASTER","cr_id",doc_lid);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				//������� Ȯ��
				data = "update CHARYANG_MASTER set flag='EF' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				//������ȣ�Է�
				numberingDoc("CHARYANG_MASTER",doc_flag,"cr_id",doc_lid);
			} 
			//email ������(2���� ����ø�)
			else if(tag.equals("flag2")) {
				//������� Ȯ��
				data = "update CHARYANG_MASTER set flag2='EF' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				//email ������
				ApprovalToEmail(this.doc_wri,this.doc_wna,"BAE_CHA",doc_lid,"APP",""); //APP:����
			}
		} 
		//���� ����(����,���庸��)
		else if(doc_flag.equals("BOGO") || doc_flag.equals("CHULJANG_BOGO")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("BOGOSEO_MASTER","bg_id",doc_lid);

			//������� Ȯ��
			data = "update BOGOSEO_MASTER set "+tag+"='EF' where bg_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("BOGOSEO_MASTER",doc_flag,"bg_id",doc_lid);	//������ȣ�Է�
			} 
		} 
		//���� ����(��ȼ�,���Խ�û��,������,������)
		else if(doc_flag.equals("GIAN") || doc_flag.equals("MYEONGHAM") || doc_flag.equals("SAYU") || doc_flag.equals("HYEOPJO")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("JIWEON_MASTER","jw_id",doc_lid);

			//������� Ȯ��
			data = "update JIWEON_MASTER set "+tag+"='EF' where jw_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("JIWEON_MASTER",doc_flag,"jw_id",doc_lid);	//������ȣ�Է�
			} 
		} 
		//����ٹ�(�ܾ�) ����(����ٹ���û��)
		else if(doc_flag.equals("YEONJANG")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("JANEUP_MASTER","ju_id",doc_lid);

			//������� Ȯ��
			data = "update JANEUP_MASTER set "+tag+"='EF' where ju_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("JANEUP_MASTER",doc_flag,"ju_id",doc_lid);	//������ȣ�Է�
			} 
		}
		//�λ� ����(�����Ƿڼ�)
		else if(doc_flag.equals("GUIN")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("INSA_MASTER","is_id",doc_lid);

			//������� Ȯ��
			data = "update INSA_MASTER set "+tag+"='EF' where is_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("INSA_MASTER",doc_flag,"is_id",doc_lid);	//������ȣ�Է�
			} 
		}
		//���� ����(��������)
		else if(doc_flag.equals("GYOYUK_ILJI")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("GYOYUK_MASTER","gy_id",doc_lid);

			//������� Ȯ��
			data = "update GYOYUK_MASTER set "+tag+"='EF' where gy_id='"+doc_lid+"'";
			stmt.executeUpdate(data);

			//������ȣ �Է�(1���� ����ø�)
			if(tag.equals("flag")){
				numberingDoc("GYOYUK_MASTER",doc_flag,"gy_id",doc_lid);	//������ȣ�Է�
			} 
		}
		//���� ����
		else if(doc_flag.equals("ODT")) {	
			//�������,�����ڵ�,�����������,������������ Ȯ��
			data = "update OfficialDocument set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			//System.out.println("Flag update : " + data);
			stmt.executeUpdate(data);
			//������ȣ �Է�
			numberingDoc("OfficialDocument",doc_flag,"id",doc_lid);
			//���ø��� ���� �� ���缱 �Է��ϱ�(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readODT(doc_lid,"ODT",this.ip_addr);
		}
		//�系����
		else if(doc_flag.equals("IDS")) {	
			//�������,�����ڵ�,�����������,������������ Ȯ��
			data = "update InDocument_send set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
			//������ȣ �Է�
			numberingDoc("InDocument_send",doc_flag,"id",doc_lid);
			//���ø��� ���� �� ���缱 �Է��ϱ�(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readIDS(doc_lid,"IDS",this.ip_addr);
		}
		//��ܰ���
		else if(doc_flag.equals("ODS")) {	
			//�������,�����ڵ�,�����������,������������ Ȯ��
			data = "update OutDocument_send set flag='EF',";
			data += "app_id='"+this.doc_pid+"',app_date='"+this.doc_ded+"',";
			data += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
			//������ȣ �Է�
			numberingDoc("OutDocument_send",doc_flag,"id",doc_lid);
			//���ø��� ���� �� ���缱 �Է��ϱ�(to OfficialDocument_app)
			com.anbtech.gw.db.ModuleApprovalOffiDocDAO appDAO = new com.anbtech.gw.db.ModuleApprovalOffiDocDAO(con);
			appDAO.readODS(doc_lid,"ODS",this.ip_addr);
		}
		//�ڻ����
		else if(doc_flag.equals("ASSET")) {	
			//1����(as_status:2) ���� 2����(as_status:4)���� �Ǵ�
			tag = checkFlag("as_history",doc_lid);

			//1�� ������� 
			if(tag.equals("2")) {
				//���缱 �ڻ������ �����ϱ�
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"approval1","1");
			}
			//2�� ���� ���� 
			else if(tag.equals("4")) {
				//���缱 �ڻ������ �����ϱ�
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"approval1","2");
				//���� �������� �뺸�ϱ�
				//ApprovalToEmail(this.doc_wri,this.doc_wna,"ASSET",doc_lid,"APP",""); //APP:����
			}
		}

		stmt.close();

	}
	/******************************************************************************
		�� ��Ĺ����� ������ ó���ϱ� (�ݷ��� flag�ݿ��ϱ� : EN)
	******************************************************************************/
	private void formPaperDown(String doc_flag,String doc_lid,String rej_comment) throws Exception 
	{	
		Statement stmt = null;
		stmt = con.createStatement();

		String data = "";		//update
		String tag = "";		//flag or flag2 

		//���°���(�ް���,�����,�����û��)
		if(doc_flag.equals("HYU_GA") || doc_flag.equals("OE_CHUL") || doc_flag.equals("CHULJANG_SINCHEONG")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("GEUNTAE_MASTER","gt_id",doc_lid);

			//����ݷ� ó��
			data = "update GEUNTAE_MASTER set "+tag+"='EN' where gt_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//��������(������û)
		else if(doc_flag.equals("BAE_CHA")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("CHARYANG_MASTER","cr_id",doc_lid);

			//����ݷ� ó��
			if(tag.equals("flag")) {
				data = "update CHARYANG_MASTER set flag='EN' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
			} else if(tag.equals("flag2")) {
				data = "update CHARYANG_MASTER set flag='EN' where cr_id='"+doc_lid+"'";
				String data2 = "update CHARYANG_MASTER set flag2='EN' where cr_id='"+doc_lid+"'";
				stmt.executeUpdate(data);
				stmt.executeUpdate(data2);
				//email ������
				ApprovalToEmail(this.doc_wri,this.doc_wna,"BAE_CHA",doc_lid,"REJ",rej_comment); //REJ : �ݷ�
			}
		} 
		//���� ����(����,���庸��)
		else if(doc_flag.equals("BOGO") || doc_flag.equals("CHULJANG_BOGO")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("BOGOSEO_MASTER","bg_id",doc_lid);

			//����ݷ� ó��
			data = "update BOGOSEO_MASTER set "+tag+"='EN' where bg_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//���� ����(��ȼ�,���Խ�û��,������,������)
		else if(doc_flag.equals("GIAN") || doc_flag.equals("MYEONGHAM") || doc_flag.equals("SAYU") || doc_flag.equals("HYEOPJO")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("JIWEON_MASTER","jw_id",doc_lid);

			//����ݷ� ó��
			data = "update JIWEON_MASTER set "+tag+"='EN' where jw_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		} 
		//����ٹ�(�ܾ�) ����(����ٹ���û��)
		else if(doc_flag.equals("YEONJANG")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("JANEUP_MASTER","ju_id",doc_lid);

			//����ݷ� ó��
			data = "update JANEUP_MASTER set "+tag+"='EN' where ju_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//�λ� ����(�����Ƿڼ�)
		else if(doc_flag.equals("GUIN")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("INSA_MASTER","is_id",doc_lid);

			//����ݷ� ó��
			data = "update INSA_MASTER set "+tag+"='EN' where is_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//���� ����(��������)
		else if(doc_flag.equals("GYOYUK_ILJI")) {	
			//1����(�ְ��μ�:flag) ���� 2����(����μ�:flag2)���� �Ǵ�
			tag = checkFlag("GYOYUK_MASTER","gy_id",doc_lid);

			//����ݷ� ó��
			data = "update GYOYUK_MASTER set "+tag+"='EN' where gy_id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//���� ����
		else if(doc_flag.equals("ODT")) {	
			//����ݷ� ó��
			data = "update OfficialDocument set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}

		//�系����
		else if(doc_flag.equals("IDS")) {	
			//������� Ȯ��
			data = "update InDocument_send set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}

		//��ܰ���
		else if(doc_flag.equals("ODS")) {	
			//������� Ȯ��
			data = "update OutDocument_send set flag='EN' where id='"+doc_lid+"'";
			stmt.executeUpdate(data);
		}
		//�ڻ����
		else if(doc_flag.equals("ASSET")) {	
			//1����(as_status:2) ���� 2����(as_status:4)���� �Ǵ�
			tag = checkFlag("as_history",doc_lid);

			//1�� ����ݷ� 
			if(tag.equals("2")) {
				//���缱 �ڻ������ �����ϱ�
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"reject","1");
			}
			//2�� ����ݷ�
			else if(tag.equals("4")) {
				com.anbtech.am.business.AssetModuleBO appAs = new com.anbtech.am.business.AssetModuleBO(con);
				appAs.asAppInfoProcess(doc_pid,doc_lid,"reject","2");
				//���� �������� �뺸�ϱ�
				//ApprovalToEmail(this.doc_wri,this.doc_wna,"ASSET",doc_lid,"REJ",""); //REJ:�ݷ�
			}

		}

		stmt.close();
	}

	/******************************************************************************
		��Ĺ��� ������ ó���ϱ�Ű ���� �ְ��μ����� ����μ����� �Ǵ��ϱ�
		1����(�ְ��μ�) : flag,  2����(����μ�) : flag2 �� �����Ѵ�.
	******************************************************************************/
	private String checkFlag(String tablename,String id_name,String doc_lid) throws SQLException 
	{	
		String flg1 = "";	//flag�� ��
		String flg2 = "";	//flag2�� ��

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct flag,flag2 from "+tablename+" where "+id_name+"='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			flg1 = rs.getString("flag");
			flg2 = rs.getString("flag2");
		}
		stmt.close();
		rs.close();

		String rdata = "";
		if(flg1.equals("EE")) rdata = "flag";
		else if(flg1.equals("EF")) rdata = "flag2";
		
		return rdata;
	}

	/******************************************************************************
		�ڻ�������� 1�� �������� 2�� �������� �Ǵ��ϱ�
		as_history as_status [2:1�����, 3:1������Ϸ�, 4:2�����, 5:2������Ϸ�
	******************************************************************************/
	public String checkFlag(String tablename,String doc_lid) throws SQLException 
	{	
		String rtn = "";	//���ϰ�
		
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct as_status from "+tablename+" where h_no='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			rtn = rs.getString("as_status");
		}
		stmt.close();
		rs.close();

		return rtn;
	}

	
	/******************************************************************************
		��� Table�� ������ȣ �Է��ϱ�  : 1���� (�ְ��μ�)���� ������νø�
		[�μ����(2) + ��ĺ� ������ + �⵵(2) + �Ϸù�ȣ(3)]
	******************************************************************************/
	private void numberingDoc(String tablename,String doc_flag,String id_name,String doc_lid) throws SQLException 
	{
		String doc_num = "";

		Statement stmt = null;
		ResultSet rs = null;

		//1.�μ��ڵ� ã��
		String head = searchDivCode(tablename,id_name,doc_lid);

		//2.��ĺ� ������ ã��
		String fp = searchFPCode(doc_flag);

		//3.�⵵ ������ ���ڸ� ã��
		String getyear = anbdt.getYear();
		String year = getyear.substring(2,4);

		doc_num = head + fp + year + "-";

		//4.�ش�μ��ڵ庰 �����Է� ����ã�� (sorting�� ���ʰ��� �����Է°���)
		String flag_col_name = "ys_kind";
		if(doc_flag.equals("GEN")) flag_col_name = "flag";			//�Ϲݺ�����
		else if(doc_flag.equals("ODT")) {flag_col_name = "flag"; doc_flag="EF"; }	//��������
		else if(doc_flag.equals("IDS")) {flag_col_name = "flag"; doc_flag="EF"; }	//�系����
		else if(doc_flag.equals("ODS")) {flag_col_name = "flag"; doc_flag="EF"; }	//��ܰ���

		stmt = con.createStatement();
		String query = "select distinct doc_id,ac_code from "+tablename+" where ac_code='"+head+"' and "+flag_col_name+"='"+doc_flag+"' order by doc_id desc";
		rs = stmt.executeQuery(query);
	
		String doc_id = "";
		if(rs.next()) doc_id = rs.getString("doc_id");
		if(doc_id == null) doc_id = "";
	
		
		//���� �Է��ϱ�
		if(doc_id.length() == 0) {
			doc_num += "001";
		} 
		//�Ϸù�ȣ �����ϱ�
		else {
			//�Ϸù�ȣ 3�ڸ� ã��
			int len = doc_id.length();
			int serial = Integer.parseInt(doc_id.substring(len-3,len));	
			serial++;	//1����
			
			//�����ϱ� (�μ��ڵ�+���ĺ�����+YY+"-"+�Ϸù�ȣ(3)
			doc_num += nmf.toDigits(serial);
		}
		//System.out.println("����������ȣ : " + doc_num); 
		//5.����ϱ�
		numberingDocInput(tablename,doc_num,id_name,doc_lid);
	
		stmt.close();
		rs.close();
	}

	/******************************************************************************
		��� Table�� ������ȣ �Է��ϱ�  : 1���� (�ְ��μ�)���� ������νø�
		���� TABLE�� ����������ȣ �Է��ϱ� ����
	******************************************************************************/
	private void numberingDocInput(String tablename,String doc_num,String id_name,String doc_lid) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String data = "update "+tablename+" set doc_id='"+doc_num+"' where "+id_name+"='"+doc_lid+"'";
		//System.out.println("������ȣ �Է� : " + data);
		stmt.executeUpdate(data);
		stmt.close();
	}

	/******************************************************************************
		��� Table�� ������ȣ�Է��� �μ��ڵ�ã�� (����� ����μ��÷��� : ac_code)
	******************************************************************************/
	private String searchDivCode(String tablename,String id_name,String doc_lid) throws SQLException 
	{

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�ش繮����ȣ�� �ش��� �μ��ڵ� ���ϱ�
		String query = "select distinct ac_code from "+tablename+" where "+id_name+"='"+doc_lid+"'";
		rs = stmt.executeQuery(query);
		
		String div_code = "";
		if(rs.next()) div_code = rs.getString("ac_code");
		if(div_code == null) div_code = "";

		stmt.close();
		rs.close();
		return div_code;
	}

	/******************************************************************************
		��ĺ� ������ ã�� (table : system_minor_code)
	******************************************************************************/
	private String searchFPCode(String doc_flag) throws SQLException 
	{

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//�ش繮����ȣ�� �ش��� �μ��ڵ� ���ϱ�
		String query = "select distinct code from system_minor_code where code_name='"+doc_flag+"'";
		rs = stmt.executeQuery(query);
		
		String form_code = "";
		if(rs.next()) form_code = rs.getString("code");
		if(form_code == null) form_code = "";

		stmt.close();
		rs.close();
		return form_code;
	}

	/******************************************************************************
		2�� �μ� ���� ������ ���ο������� �����ϱ�
		writer_id/writer_name: �ۼ��ڷμ� �����μ� ����� ���/�̸�
		doc_kind : ��������
		link_id : ���ù��� ������ȣ
		tag : ����[APP] or �ݷ�[REJ]
	******************************************************************************/
	private void ApprovalToEmail(String writer_id,String writer_name,String doc_kind,String link_id,String tag,String comment) throws SQLException 
	{
		String subj = "";						//����
		String receiver = "";					//������ (���/�̸�;) post_master
		String l_receiver = "";					//������ (���) post_letter
		String pid = getID();					//������ȣ
		String w_date = anbdt.getTime();		//�ۼ���
		String bon_path="/post/"+writer_id+"/text_upload";	//����path
		String delete_date = anbdt.getAddMonthNoformat(1);	//����������
		String filename = "";					//�������ϸ�

		Statement stmt = null;
		stmt = con.createStatement();
		
		//�������� ���� ������ ���ϱ�
		if(doc_kind.equals("BAE_CHA")) {	
			receiver = getBaeChaReceiver(link_id);	//������ (post_master�� ������ : ���/�̸�;)
			if(tag.equals("APP")) {		//����
				subj = "������û ����Ϸ�";					//����
				filename = pid;								//�������� ���ϸ�
				setBaeChaContent(link_id,filename);			//���� �ۼ����� ���Ϸ� ���(pid : �������� ���ϸ�)
			} else if(tag.equals("REJ")) { //�ݷ�
				subj = "������û �������";					//����
				filename = pid+"_r";						//�������� ���ϸ�
				setBaeChaRejContent(link_id,filename,comment);	//���� �ۼ����� ���Ϸ� ���(pid : �������� ���ϸ�)
			}
		}
		else if(doc_kind.equals("ASSET")) {	
			receiver = getAssetReceiver(link_id);	//������ (post_master�� ������ : ���/�̸�;)
			if(tag.equals("APP")) {		//����
				subj = "�ڻ�����û ���οϷ�";				//����
				filename = pid;								//�������� ���ϸ�
				setAssetContent(link_id,filename);			//���� �ۼ����� ���Ϸ� ���(pid : �������� ���ϸ�)
			} else if(tag.equals("REJ")) { //�ݷ�
				subj = "�ڻ�����û �������";				//����
				filename = pid+"_r";						//�������� ���ϸ�
				setAssetRejContent(link_id,filename,comment);	//���� �ۼ����� ���Ϸ� ���(pid : �������� ���ϸ�)
			}

		}

		//post_letter ���̺� �Է��� ������ (����� �ʿ�)
		l_receiver = receiver.substring(0,receiver.indexOf("/"));
			
		//���ó��� post tabel(post_master,post_letter)�� ���
		String mquery = "";
		String lquery = "";
		//post_master
		mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
		mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		mquery += pid+"','"+subj+"','"+writer_id+"','"+writer_name+"','"+w_date+"','"+receiver+"','";
		mquery += "0"+"','"+"SND"+"','"+"CFM"+"','"+bon_path+"','"+filename+"','"+delete_date+"')";
		//System.out.println("email master : " + mquery);
		//post_letter
		lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
		lquery += "post_receiver,isopen,post_select,delete_date) values('";
		lquery += pid+"','"+subj+"','"+writer_id+"','"+writer_name+"','"+w_date+"','"+l_receiver+"','";
		lquery += "0"+"','"+"CFM"+"','"+delete_date+"')";
		//System.out.println("email letter : " + lquery);
		try{ 
			stmt.executeUpdate(mquery);
			stmt.executeUpdate(lquery);
		} catch (Exception e) {}
		stmt.close();
	}

	/******************************************************************************
		2���μ� ���� ������ ���ο������� �����ϱ�
		�������� ������ full path 
	******************************************************************************/
	public void setMailFullPath(String path)
	{
		this.mail_path = path;
	}

	/******************************************************************************
		2���μ� ���� ������ ���ο������� �����ϱ�
		������ ���/�̸� ���ϱ� (������û��)
	******************************************************************************/
	private String getBaeChaReceiver(String link_id) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct user_id,user_name from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);
		
		String receiver = "";
		if(rs.next()) receiver = rs.getString("user_id")+"/"+rs.getString("user_name")+";";
		if(receiver == null) receiver = "";

		stmt.close();
		rs.close();
		return receiver;
	}

	/******************************************************************************
		2���μ� ���� ������ ���ο������� �����ϱ�
		�������� �����ϱ� (������û��)
	******************************************************************************/
	private void setBaeChaContent(String link_id,String file_name) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time ";
		query += "from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="������û�� ����Ǿ����ϴ�.\n\n";
		content += "�����Ⱓ : ";
		while(rs.next()) {
			content += rs.getString("u_year")+"-"+rs.getString("u_month")+"-"+rs.getString("u_date")+" "+rs.getString("u_time")+" ~ ";
			content += rs.getString("tu_year")+"-"+rs.getString("tu_month")+"-"+rs.getString("tu_date")+" "+rs.getString("tu_time");
		}
		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory�����ϱ�
		text.WriteHanguel(path,file_name,content);		//���� ���Ϸ� �����ϱ�
	}

	/******************************************************************************
		2���μ� ���� �ݷ��� ���ο������� �����ϱ�
		�������� �����ϱ� (������û��)
	******************************************************************************/
	private void setBaeChaRejContent(String link_id,String file_name,String comment) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time ";
		query += "from charyang_master where cr_id='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="������û ������ ��ҵǾ����ϴ�.\n\n";
		content += "����Ⱓ : ";
		while(rs.next()) {
			content += rs.getString("u_year")+"-"+rs.getString("u_month")+"-"+rs.getString("u_date")+" "+rs.getString("u_time")+" ~ ";
			content += rs.getString("tu_year")+"-"+rs.getString("tu_month")+"-"+rs.getString("tu_date")+" "+rs.getString("tu_time");
		}
		content +="\n\n����ǰ� : "+comment+"\n\n";

		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory�����ϱ�
		text.WriteHanguel(path,file_name,content);		//���� ���Ϸ� �����ϱ�
	}

	/******************************************************************************
		2���μ� ���� ������ ���ο������� �����ϱ�
		������ ���/�̸� ���ϱ� (�ڻ���� ��û��� ���ڿ��� ������)
	******************************************************************************/
	private String getAssetReceiver(String link_id) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct w_name from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);
		
		String receiver = "";
		if(rs.next()) receiver = rs.getString("w_name")+";";
		if(receiver == null) receiver = "";

		stmt.close();
		rs.close();
		return receiver;
	}

	/******************************************************************************
		2���μ� ���� ������ ���ο������� �����ϱ�
		�������� �����ϱ� (�ڻ���� ���ν�)
	******************************************************************************/
	private void setAssetContent(String link_id,String file_name) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_date,tu_date ";
		query += "from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="�����û�� ���εǾ����ϴ�.\n\n";
		content += "�����û�Ⱓ : ";
		while(rs.next()) {
			String from = rs.getString("u_date");
			String to = rs.getString("tu_date"); if(to == null) to = "";
			content += from.substring(0,4)+"-"+from.substring(4,6)+"-"+from.substring(6,8)+" ~ ";
			if(to.length() > 4)
				content += to.substring(0,4)+"-"+to.substring(4,6)+"-"+to.substring(6,8);
		}
		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory�����ϱ�
		text.WriteHanguel(path,file_name,content);		//���� ���Ϸ� �����ϱ�
	}

	/******************************************************************************
		2���μ� ���� �ݷ��� ���ο������� �����ϱ�
		�������� �����ϱ� (�ڻ���� ��ҽ�)
	******************************************************************************/
	private void setAssetRejContent(String link_id,String file_name,String comment) throws SQLException 
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "select distinct u_date,tu_date ";
		query += "from as_history where h_no='"+link_id+"'";
		rs = stmt.executeQuery(query);

		String content="�����û�� ������ҵǾ����ϴ�.\n\n";
		content += "�����û�Ⱓ : ";
		while(rs.next()) {
			String from = rs.getString("u_date");
			String to = rs.getString("tu_date");
			content += from.substring(0,4)+"-"+from.substring(4,6)+"-"+from.substring(6,8)+" ~ ";
			content += to.substring(0,4)+"-"+to.substring(4,6)+"-"+to.substring(6,8);
		}
		content +="\n\n����ǰ� : "+comment+"\n\n";

		stmt.close();
		rs.close();

		String path = this.mail_path + "/post/" + this.doc_wri + "/text_upload";
		////System.out.println("content : " + content);
		////System.out.println("path : " + path);
		text.setFilepath(path);							//directory�����ϱ�
		text.WriteHanguel(path,file_name,content);		//���� ���Ϸ� �����ϱ�
	}

	/******************************************************************************
		�ӽú�����,�ݷ����� ��� �����ϱ�
	******************************************************************************/
	public void appDelete(String pid) throws SQLException 
	{
		String where="", flag="",plid="",updata="",delete="",add_file="";
		String user_id="";

		Statement stmt = null;
		stmt = con.createStatement();

		//��Ĺ����� �������ϱ�
		where = "where pid='"+pid+"'";
		flag = getColumnData("app_master","flag",where);
		plid = getColumnData("app_master","plid",where);

		//���°���(�����,�����û,�ް���) ����
		if(flag.equals("OE_CHUL") || flag.equals("CHULJANG_SINCHEONG") || flag.equals("HYU_GA")) { 
			delete = "delete from geuntae_master where gt_id='"+plid+"'";
			stmt.executeUpdate(delete);
			
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//�������(����,���庸��) ����
		else if(flag.equals("BOGO") || flag.equals("CHULJANG_BOGO")) { 
			//������ ÷�����ϻ���
			where = "where bg_id='"+plid+"'";
			add_file = getColumnData("bogoseo_master","sname",where);
			user_id = getColumnData("bogoseo_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from bogoseo_master where bg_id='"+plid+"'";
			stmt.executeUpdate(delete);
			
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//��������(��ȼ�,����,������,������) ����
		else if(flag.equals("GIAN") || flag.equals("MYEONGHAM") || flag.equals("SAYU") || flag.equals("HYEOPJO")) { 
			//������ ÷�����ϻ���
			where = "where jw_id='"+plid+"'";
			add_file = getColumnData("jiweon_master","sname",where);
			user_id = getColumnData("jiweon_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from jiweon_master where jw_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//�λ����(�����Ƿڼ�) ����
		else if(flag.equals("GUIN")) { 
			delete = "delete from insa_master where is_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//��������(����) ����
		else if(flag.equals("GYOYUK_ILJI")) { 
			//������ ÷�����ϻ���
			where = "where gy_id='"+plid+"'";
			add_file = getColumnData("gyoyuk_master","sname",where);
			user_id = getColumnData("gyoyuk_master","user_id",where);
			appDeleteAttachFile(plid,"es",user_id,add_file);

			delete = "delete from gyoyuk_master where gy_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from gyoyuk_part where gy_id='"+plid+"'";
			stmt.executeUpdate(delete);

			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		//��⹮�� ����
		else {
			delete = "delete from app_master where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}
		stmt.close();

	}
	/******************************************************************************
		��Ĺ��� ÷������ �����ϱ�
	******************************************************************************/
	public void appDeleteAttachFile(String plid,String ext_path,String id_path,String add_file) throws SQLException 
	{
		String bonmun_path="",addfile_path="",af="";

		com.anbtech.admin.db.ServerConfig config = new com.anbtech.admin.db.ServerConfig();
		String upload_path = config.getConf("upload_path");				//��Ʈ PATH
		
		//�������� �����ϱ�
		bonmun_path = upload_path+"/"+ext_path+"/"+id_path+"/bonmun/"+plid;	
////System.out.println("b : " + bonmun_path);
		File BN = new File(bonmun_path);
		if(BN.exists()) BN.delete();

		//÷������ �����ϱ�
		if(add_file == null) add_file = "";
		StringTokenizer f = new StringTokenizer(add_file,"|");
		while(f.hasMoreTokens()) {
			af = f.nextToken().trim();
			addfile_path = upload_path+"/"+ext_path+"/"+id_path+"/addfile/"+af+".bin";
////System.out.println("a : " + addfile_path);
			File FN = new File(addfile_path);
			if(FN.exists()) FN.delete();
		}
	}

	/******************************************************************************
		�������ϱ�
	******************************************************************************/
	public void appCancel(String pid) throws SQLException 
	{
		String where="", flag="",plid="",updata="",delete="";
		String form_paper = "HYU_GA,OE_CHUL,CHULJANG_SINCHEONG,BOGO,CHULJANG_BOGO,GIAN,";
			  form_paper += "MYEONGHAM,SAYU,HYEOPJO,GUIN,GYOYUK_ILJI,GEN,YEONGJANG";
		Statement stmt = null;
		stmt = con.createStatement();

		//��Ĺ����� �������ϱ�
		where = "where pid='"+pid+"'";
		flag = getColumnData("app_master","flag",where);
		plid = getColumnData("app_master","plid",where);

		//��Ĺ����� ���� ���ڰ��系���� �ӽú��������� ��ȯ
		if(form_paper.indexOf(flag) != -1) {
			updata = "update app_master set app_state='APT' where pid='"+pid+"'";
			stmt.executeUpdate(updata);
		} else {
			//��������
			if(flag.equals("ODT")) {
				updata = "update OfficialDocument set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//�系����
			else if(flag.equals("IDS")) {
				updata = "update InDocument_send set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//��ܰ���
			else if(flag.equals("ODS")) {
				updata = "update OutDocument_send set flag='EC' where id='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//BOM ����
			else if(flag.equals("BOM")) {
				updata = "update mbom_master set bom_status='3' where pid='"+plid+"'";
				stmt.executeUpdate(updata);

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}
			//���躯��
			else if(flag.equals("DCM")) {
				String cpid = plid.substring(0,plid.indexOf("|"));			//����������ȣ
				where = "where pid='"+cpid+"'";
				String st = getColumnData("ecc_com","ecc_status",where);	//���������
				if(st.equals("2")) {										//ECR������
					updata = "update ecc_com set edd_status='1' where pid='"+cpid+"'";
					stmt.executeUpdate(updata);
				} else if(st.equals("7")) {									//ECO������
					updata = "update ecc_com set edd_status='6' where pid='"+cpid+"'";
					stmt.executeUpdate(updata);
				}

				delete = "delete from app_master where pid='"+pid+"'";
				stmt.executeUpdate(delete);
			}

		}
		stmt.close();
	}

	/******************************************************************************
		���ڰ����� Ư���÷��� �����ϱ�
		 ex)�������� ������� ������ȣ�ٲ�
	******************************************************************************/
	private void updateColumn(String tablename,String column_name,String new_data,String pid) throws SQLException 
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String data = "update "+tablename+" set "+column_name+"='"+new_data+"' where pid='"+pid+"'";
		stmt.executeUpdate(data);
		stmt.close();
	}

	/******************************************************************************
		�־��� Table name���� Column name�� ���� ���ϱ�
	******************************************************************************/
	private String getColumnData(String tablename,String column_name,String where) throws SQLException 
	{
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query= "SELECT "+column_name+" FROM "+tablename +" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString(column_name);
		stmt.close();
		rs.close();
		return data;
	}

	/******************************************************************************
		flag�� ��������
	******************************************************************************/
	public String getFlag()
	{
		return this.doc_flag;
	}

	/******************************************************************************
		���α��� ip address 
	******************************************************************************/
	public void setIPADDR(String ip_addr)
	{
		this.ip_addr = ip_addr;
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
	/***************************************************************************
	 * �������� ����ɶ� �ڿ��� ȸ���ϱ� ���� �޼ҵ� 
	 **************************************************************************/
	protected void finalize() throws Throwable 
	{
		

	}

}	