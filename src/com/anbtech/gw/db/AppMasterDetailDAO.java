package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import com.anbtech.file.textFileReader;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppMasterDetailDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private textFileReader text;

	private String line_doc="";			//���缱 �б�
	private String[] LINE_ORD;			//���缱 �迭�� ���

	private String writer = "";			//����� ���
	private String writer_d = "";		//����� date
	private String writer_c = "";		//����� comment

	private String review = "";			//������ ���
	private String review_d = "";		//������ date
	private String review_c = "";		//������ comment

	private String decision ="";		//������ ���
	private String decision_d ="";		//������ date
	private String decision_c ="";		//������ comment

	private int agree_cnt = 0;			//������ ���ο���
	private String[][] agree;			//������id, ����, �ڸ�Ʈ

	private String receivers="";		//�뺸�� ���(id)
	private String[][] receiver;		//�뺸��id, ����, �ڸ�Ʈ

	private String agree_method="";		//���� ��������

	private String pid = "";			//������ȣ
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppMasterDetailDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMasterDetailDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//*******************************************************************
	// APP_MASTER ���� �ش� PID�� ���븸 �������� 
	//*******************************************************************/	
	public ArrayList getTable_MasterPid (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		text = new textFileReader();

		//������ ���,����,�ڸ�Ʈ ��� �ʱ�ȭ
		agree = new String[10][3];
		for(int i=0; i<10; i++) for(int j=0; j<3; j++) agree[i][j]="";
		
		stmt = con.createStatement();
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
			
		//query���� �����
		query = "SELECT * FROM APP_MASTER where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new TableAppMaster();
				this.pid = rs.getString("pid");	if(this.pid == null) this.pid = "";
				table.setAmPid(this.pid);									//pid
				table.setAmAppSubj(rs.getString("app_subj"));				//����

				this.writer = rs.getString("writer");			if(this.writer == null) this.writer = "";
				table.setAmWriterName(rs.getString("writer_name"));			//�ۼ��� �̸�
				this.writer_d = rs.getString("write_date");		if(this.writer_d == null) this.writer_d = "";
				this.writer_c = "";								if(this.writer_c == null) this.writer_c = "";
				table.setAmWriter(this.writer);								//�ۼ��� ���
				table.setAmWriteDate(this.writer_d);						//�ۼ� ����

				table.setAmAppStatus(rs.getString("app_state"));			//����

				this.review = rs.getString("reviewer");			if(this.review == null) this.review = "";
				this.review_d = rs.getString("review_date");	if(this.review_d == null) this.review_d = "";
				this.review_c = rs.getString("review_comment");	if(this.review_c == null) this.review_c = "";
				table.setAmReviewer(this.review);							//������ ���
				table.setAmReviewDate(this.review_d);						//���� ����
				table.setAmReviewComment(this.review_c);					//������ �ǰ�

				agree[0][0]=rs.getString("agree");agree[0][1]=rs.getString("agree_date");agree[0][2]=rs.getString("agree_comment");		
				for(int i=0; i<3; i++) if(agree[0][i] == null) agree[0][i]= "";
				table.setAmAgree(agree[0][0]);								//������1 ���
				table.setAmAgreeDate(agree[0][1]);							//������1 ����
				table.setAmAgreeComment(agree[0][2]);						//������1 �ǰ�

				agree[1][0]=rs.getString("agree2");agree[1][1]=rs.getString("agree2_date");agree[1][2]=rs.getString("agree2_comment");
				for(int i=0; i<3; i++) if(agree[1][i] == null) agree[1][i]= "";
				table.setAmAgree2(agree[1][0]);								//������2 ���
				table.setAmAgree2Date(agree[1][1]);							//������2 ����
				table.setAmAgree2Comment(agree[1][2]);						//������2 �ǰ�

				agree[2][0]=rs.getString("agree3");agree[2][1]=rs.getString("agree3_date");agree[2][2]=rs.getString("agree3_comment");
				for(int i=0; i<3; i++) if(agree[2][i] == null) agree[2][i]= "";
				table.setAmAgree3(agree[2][0]);								//������3 ���
				table.setAmAgree3Date(agree[2][1]);							//������3 ����
				table.setAmAgree3Comment(agree[2][2]);						//������3 �ǰ�
				
				agree[3][0]=rs.getString("agree4");agree[3][1]=rs.getString("agree4_date");agree[3][2]=rs.getString("agree4_comment");
				for(int i=0; i<3; i++) if(agree[3][i] == null) agree[3][i]= "";
				table.setAmAgree4(agree[3][0]);								//������4 ���
				table.setAmAgree4Date(agree[3][1]);							//������4 ����
				table.setAmAgree4Comment(agree[3][2]);						//������4 �ǰ�
				
				agree[4][0]=rs.getString("agree5");agree[4][1]=rs.getString("agree5_date");agree[4][2]=rs.getString("agree5_comment");
				for(int i=0; i<3; i++) if(agree[4][i] == null) agree[4][i]= "";
				table.setAmAgree5(agree[4][0]);								//������5 ���
				table.setAmAgree5Date(agree[4][1]);							//������5 ����
				table.setAmAgree5Comment(agree[4][2]);						//������5 �ǰ�
				
				agree[5][0]=rs.getString("agree6");agree[5][1]=rs.getString("agree6_date");agree[5][2]=rs.getString("agree6_comment");
				for(int i=0; i<3; i++) if(agree[5][i] == null) agree[5][i]= "";
				table.setAmAgree6(agree[5][0]);								//������6 ���
				table.setAmAgree6Date(agree[5][1]);							//������6 ����
				table.setAmAgree6Comment(agree[5][2]);						//������6 �ǰ�
				
				agree[6][0]=rs.getString("agree7");agree[6][1]=rs.getString("agree7_date");agree[6][2]=rs.getString("agree7_comment");
				for(int i=0; i<3; i++) if(agree[6][i] == null) agree[6][i]= "";
				table.setAmAgree7(agree[6][0]);								//������7 ���
				table.setAmAgree7Date(agree[6][1]);							//������7 ����
				table.setAmAgree7Comment(agree[6][2]);						//������7 �ǰ�
				
				agree[7][0]=rs.getString("agree8");agree[7][1]=rs.getString("agree8_date");agree[7][2]=rs.getString("agree8_comment");
				for(int i=0; i<3; i++) if(agree[7][i] == null) agree[7][i]= "";
				table.setAmAgree8(agree[7][0]);								//������8 ���
				table.setAmAgree8Date(agree[7][1]);							//������8 ����
				table.setAmAgree8Comment(agree[7][2]);						//������8 �ǰ�
				
				agree[8][0]=rs.getString("agree9");agree[8][1]=rs.getString("agree9_date");agree[8][2]=rs.getString("agree9_comment");
				for(int i=0; i<3; i++) if(agree[8][i] == null) agree[8][i]= "";
				table.setAmAgree9(agree[8][0]);								//������9 ���
				table.setAmAgree9Date(agree[8][1]);							//������9 ����
				table.setAmAgree9Comment(agree[8][2]);						//������9 �ǰ�
				
				agree[9][0]=rs.getString("agree10");agree[9][1]=rs.getString("agree10_date");agree[9][2]=rs.getString("agree10_comment");
				for(int i=0; i<3; i++) if(agree[9][i] == null) agree[9][i]= "";
				table.setAmAgree10(agree[9][0]);							//������10 ���
				table.setAmAgree10Date(agree[9][1]);						//������10 ����
				table.setAmAgree10Comment(agree[9][2]);						//������10 �ǰ�
				
				this.agree_method = rs.getString("agree_method");
				table.setAmAgreeMethod(this.agree_method);					//�������� (Serial Parallel)
				String ag_cnt = rs.getString("agree_count");
				if(ag_cnt != null)
					this.agree_cnt = Integer.parseInt(ag_cnt); 
				table.setAmAgreeCount(Integer.toString(this.agree_cnt));	//������ �ѿ��� 

				table.setAmAgreePass(rs.getString("agree_pass"));			//�� ������ �� (parallel�� ���)

				this.decision = rs.getString("decision");			if(this.decision == null) this.decision = "";
				this.decision_d = rs.getString("decision_date");	if(this.decision_d == null) this.decision_d = "";
				this.decision_c = rs.getString("decision_comment");	if(this.decision_c == null) this.decision_c = "";
				table.setAmDecision(this.decision);							//������ ���
				table.setAmDecisionDate(this.decision_d);					//���� ����
				table.setAmDecisionComment(this.decision_c);				//������ �ǰ�
				
				this.receivers = rs.getString("receivers");			if(this.receivers == null) this.receivers = "";
				table.setAmReceivers(this.receivers);						//�뺸�� ��� or �μ��ڵ�

				this.line_doc = rs.getString("app_line");			if(this.line_doc == null) this.line_doc = "";		
				table.setAmAppLine(this.line_doc);							//���� ����

				String bon_path = rs.getString("bon_path");
				table.setAmBonPath(bon_path);								//����path
				String bon_file = rs.getString("bon_file");
				table.setAmBonFile(bon_file);								//�������ϸ�

				String ao1 = rs.getString("add_1_original");			if(ao1 == null) ao1 = "";
				String af1 = rs.getString("add_1_file");				if(af1 == null) af1 = "";
				String ao2 = rs.getString("add_2_original");			if(ao2 == null) ao2 = "";
				String af2 = rs.getString("add_2_file");				if(af2 == null) af2 = "";
				String ao3 = rs.getString("add_3_original");			if(ao3 == null) ao3 = "";
				String af3 = rs.getString("add_3_file");				if(af3 == null) af3 = "";
				table.setAmAdd1Original(ao1);								//÷������ 1 original
				table.setAmAdd1File(af1);									//÷������ 1
				table.setAmAdd2Original(ao2);								//÷������ 2 original
				table.setAmAdd2File(af2);									//÷������ 2
				table.setAmAdd3Original(ao3);								//÷������ 3 original
				table.setAmAdd3File(af3);									//÷������ 3

				String sp = rs.getString("save_period");			if(sp == null) sp = "";
				table.setAmSavePeriod(sp);									//�����Ⱓ
				String sl = rs.getString("security_level");			if(sl == null) sl = "";
				table.setAmSecurityLevel(sl);								//���ȵ��
				String pd = rs.getString("plid");					if(pd == null) pd = "";
				table.setAmPlid(pd);										//��Ÿ���� ������ȣ
				String fg = rs.getString("flag");					if(fg == null) fg = "";
				table.setAmFlag(fg);										//��Ÿ���� ����

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// APP_MASTER ���� �ش� PID�� ���븸 �������� 
	//*******************************************************************/	
	public ArrayList getTable_MasterPid (String pid,String tablename) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		text = new textFileReader();

		//������ ���,����,�ڸ�Ʈ ��� �ʱ�ȭ
		agree = new String[10][3];
		for(int i=0; i<10; i++) for(int j=0; j<3; j++) agree[i][j]="";
		
		stmt = con.createStatement();
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
			
		//query���� �����
		query = "SELECT * FROM "+tablename+" where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new TableAppMaster();
				this.pid = rs.getString("pid");	if(this.pid == null) this.pid = "";
				table.setAmPid(this.pid);									//pid
				table.setAmAppSubj(rs.getString("app_subj"));				//����

				this.writer = rs.getString("writer");			if(this.writer == null) this.writer = "";
				table.setAmWriterName(rs.getString("writer_name"));			//�ۼ��� �̸�
				this.writer_d = rs.getString("write_date");		if(this.writer_d == null) this.writer_d = "";
				this.writer_c = "";								if(this.writer_c == null) this.writer_c = "";
				table.setAmWriter(this.writer);								//�ۼ��� ���
				table.setAmWriteDate(this.writer_d);						//�ۼ� ����

				table.setAmAppStatus(rs.getString("app_state"));			//����

				this.review = rs.getString("reviewer");			if(this.review == null) this.review = "";
				this.review_d = rs.getString("review_date");	if(this.review_d == null) this.review_d = "";
				this.review_c = rs.getString("review_comment");	if(this.review_c == null) this.review_c = "";
				table.setAmReviewer(this.review);							//������ ���
				table.setAmReviewDate(this.review_d);						//���� ����
				table.setAmReviewComment(this.review_c);					//������ �ǰ�

				agree[0][0]=rs.getString("agree");agree[0][1]=rs.getString("agree_date");agree[0][2]=rs.getString("agree_comment");		
				for(int i=0; i<3; i++) if(agree[0][i] == null) agree[0][i]= "";
				table.setAmAgree(agree[0][0]);								//������1 ���
				table.setAmAgreeDate(agree[0][1]);							//������1 ����
				table.setAmAgreeComment(agree[0][2]);						//������1 �ǰ�

				agree[1][0]=rs.getString("agree2");agree[1][1]=rs.getString("agree2_date");agree[1][2]=rs.getString("agree2_comment");
				for(int i=0; i<3; i++) if(agree[1][i] == null) agree[1][i]= "";
				table.setAmAgree2(agree[1][0]);								//������2 ���
				table.setAmAgree2Date(agree[1][1]);							//������2 ����
				table.setAmAgree2Comment(agree[1][2]);						//������2 �ǰ�

				agree[2][0]=rs.getString("agree3");agree[2][1]=rs.getString("agree3_date");agree[2][2]=rs.getString("agree3_comment");
				for(int i=0; i<3; i++) if(agree[2][i] == null) agree[2][i]= "";
				table.setAmAgree3(agree[2][0]);								//������3 ���
				table.setAmAgree3Date(agree[2][1]);							//������3 ����
				table.setAmAgree3Comment(agree[2][2]);						//������3 �ǰ�
				
				agree[3][0]=rs.getString("agree4");agree[3][1]=rs.getString("agree4_date");agree[3][2]=rs.getString("agree4_comment");
				for(int i=0; i<3; i++) if(agree[3][i] == null) agree[3][i]= "";
				table.setAmAgree4(agree[3][0]);								//������4 ���
				table.setAmAgree4Date(agree[3][1]);							//������4 ����
				table.setAmAgree4Comment(agree[3][2]);						//������4 �ǰ�
				
				agree[4][0]=rs.getString("agree5");agree[4][1]=rs.getString("agree5_date");agree[4][2]=rs.getString("agree5_comment");
				for(int i=0; i<3; i++) if(agree[4][i] == null) agree[4][i]= "";
				table.setAmAgree5(agree[4][0]);								//������5 ���
				table.setAmAgree5Date(agree[4][1]);							//������5 ����
				table.setAmAgree5Comment(agree[4][2]);						//������5 �ǰ�
				
				agree[5][0]=rs.getString("agree6");agree[5][1]=rs.getString("agree6_date");agree[5][2]=rs.getString("agree6_comment");
				for(int i=0; i<3; i++) if(agree[5][i] == null) agree[5][i]= "";
				table.setAmAgree6(agree[5][0]);								//������6 ���
				table.setAmAgree6Date(agree[5][1]);							//������6 ����
				table.setAmAgree6Comment(agree[5][2]);						//������6 �ǰ�
				
				agree[6][0]=rs.getString("agree7");agree[6][1]=rs.getString("agree7_date");agree[6][2]=rs.getString("agree7_comment");
				for(int i=0; i<3; i++) if(agree[6][i] == null) agree[6][i]= "";
				table.setAmAgree7(agree[6][0]);								//������7 ���
				table.setAmAgree7Date(agree[6][1]);							//������7 ����
				table.setAmAgree7Comment(agree[6][2]);						//������7 �ǰ�
				
				agree[7][0]=rs.getString("agree8");agree[7][1]=rs.getString("agree8_date");agree[7][2]=rs.getString("agree8_comment");
				for(int i=0; i<3; i++) if(agree[7][i] == null) agree[7][i]= "";
				table.setAmAgree8(agree[7][0]);								//������8 ���
				table.setAmAgree8Date(agree[7][1]);							//������8 ����
				table.setAmAgree8Comment(agree[7][2]);						//������8 �ǰ�
				
				agree[8][0]=rs.getString("agree9");agree[8][1]=rs.getString("agree9_date");agree[8][2]=rs.getString("agree9_comment");
				for(int i=0; i<3; i++) if(agree[8][i] == null) agree[8][i]= "";
				table.setAmAgree9(agree[8][0]);								//������9 ���
				table.setAmAgree9Date(agree[8][1]);							//������9 ����
				table.setAmAgree9Comment(agree[8][2]);						//������9 �ǰ�
				
				agree[9][0]=rs.getString("agree10");agree[9][1]=rs.getString("agree10_date");agree[9][2]=rs.getString("agree10_comment");
				for(int i=0; i<3; i++) if(agree[9][i] == null) agree[9][i]= "";
				table.setAmAgree10(agree[9][0]);							//������10 ���
				table.setAmAgree10Date(agree[9][1]);						//������10 ����
				table.setAmAgree10Comment(agree[9][2]);						//������10 �ǰ�
				
				this.agree_method = rs.getString("agree_method");
				table.setAmAgreeMethod(this.agree_method);					//�������� (Serial Parallel)
				String ag_cnt = rs.getString("agree_count");
				if(ag_cnt != null)
					this.agree_cnt = Integer.parseInt(ag_cnt); 
				table.setAmAgreeCount(Integer.toString(this.agree_cnt));	//������ �ѿ��� 

				table.setAmAgreePass(rs.getString("agree_pass"));			//�� ������ �� (parallel�� ���)

				this.decision = rs.getString("decision");			if(this.decision == null) this.decision = "";
				this.decision_d = rs.getString("decision_date");	if(this.decision_d == null) this.decision_d = "";
				this.decision_c = rs.getString("decision_comment");	if(this.decision_c == null) this.decision_c = "";
				table.setAmDecision(this.decision);							//������ ���
				table.setAmDecisionDate(this.decision_d);					//���� ����
				table.setAmDecisionComment(this.decision_c);				//������ �ǰ�
				
				this.receivers = rs.getString("receivers");			if(this.receivers == null) this.receivers = "";
				table.setAmReceivers(this.receivers);						//�뺸�� ��� or �μ��ڵ�

				this.line_doc = rs.getString("app_line");			if(this.line_doc == null) this.line_doc = "";		
				table.setAmAppLine(this.line_doc);							//���� ����

				String bon_path = rs.getString("bon_path");
				table.setAmBonPath(bon_path);								//����path
				String bon_file = rs.getString("bon_file");
				table.setAmBonFile(bon_file);								//�������ϸ�

				String ao1 = rs.getString("add_1_original");			if(ao1 == null) ao1 = "";
				String af1 = rs.getString("add_1_file");				if(af1 == null) af1 = "";
				String ao2 = rs.getString("add_2_original");			if(ao2 == null) ao2 = "";
				String af2 = rs.getString("add_2_file");				if(af2 == null) af2 = "";
				String ao3 = rs.getString("add_3_original");			if(ao3 == null) ao3 = "";
				String af3 = rs.getString("add_3_file");				if(af3 == null) af3 = "";
				table.setAmAdd1Original(ao1);								//÷������ 1 original
				table.setAmAdd1File(af1);									//÷������ 1
				table.setAmAdd2Original(ao2);								//÷������ 2 original
				table.setAmAdd2File(af2);									//÷������ 2
				table.setAmAdd3Original(ao3);								//÷������ 3 original
				table.setAmAdd3File(af3);									//÷������ 3

				String sp = rs.getString("save_period");			if(sp == null) sp = "";
				table.setAmSavePeriod(sp);									//�����Ⱓ
				String sl = rs.getString("security_level");			if(sl == null) sl = "";
				table.setAmSecurityLevel(sl);								//���ȵ��
				String pd = rs.getString("plid");					if(pd == null) pd = "";
				table.setAmPlid(pd);										//��Ÿ���� ������ȣ
				String fg = rs.getString("flag");					if(fg == null) fg = "";
				table.setAmFlag(fg);										//��Ÿ���� ����

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	������� �˾ƺ��� (APL,APGx,APL,API)
	//*******************************************************************/	
	private void getLineOrder () 
	{
		//�迭 ũ�� ã��
		int cnt = 0;
		if(this.line_doc.length() > 0) {
			cnt++;
			for(int i=0; i<this.line_doc.length(); i++) if(this.line_doc.charAt(i) == ',') cnt++;
		}
		//System.out.println("������� : " + this.line_doc + " : " + cnt);

		//���缱 �迭�� ���
		LINE_ORD = new String[cnt];			
		StringTokenizer Rlist = new StringTokenizer(line_doc,",");
		int Ri = 0;
		while(Rlist.hasMoreTokens()) {
			LINE_ORD[Ri] = Rlist.nextToken();
			//System.out.println("������� �����迭 : " + LINE_ORD[Ri] + " : " + Ri);
			Ri++;
		}
	}

	//*******************************************************************
	//	���� �뺸�� ��� �˾ƺ���
	//*******************************************************************/
	private void getReceivers() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;

		//�뺸�ο� �迭 ũ�� ã�� (�μ��뺸�� �ϳ��� ����)
		//System.out.println("�뺸�� ��� : " + this.receivers);
		int cnt = 0;
		if(this.receivers.length() > 0) {
			cnt++;
			for(int i=0; i<this.receivers.length(); i++) if(this.receivers.charAt(i) == ',') cnt++;
		} else return;
		
		//�뺸�� ���,����,�ڸ�Ʈ ��� �ʱ�ȭ (����Ϸ��� �뺸�� �� : �μ��뺸�� �μ��ο� ������)
		//receiver = new String[cnt][3];
		//for(int i=0; i<cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";

		//���� �׸�
		stmt = con.createStatement();

		String cnt_query = "SELECT COUNT(*) FROM APP_RECEIVE where pid ='"+this.pid+"'";
		rs = stmt.executeQuery(cnt_query);

		if(rs.next()) {
			//����� ������ ��
			int api_cnt = rs.getInt(1);		//�뺸�Կ� ����� �Ѽ���
			if(api_cnt > 0) {
				receiver = new String[api_cnt][3];	//�μ� + ���κ� �뺸�� �ٽù迭ũ�� ������.
				for(int i=0; i<api_cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";
				String query = "SELECT receiver,read_date,isopen FROM APP_RECEIVE where pid='"+this.pid+"'";
				rs = stmt.executeQuery(query);

				int Ri = 0;
				while(rs.next()) {
					receiver[Ri][0] = rs.getString("receiver");		if(receiver[Ri][0] == null) receiver[Ri][0] = "";
					receiver[Ri][1] = rs.getString("read_date");	if(receiver[Ri][1] == null) receiver[Ri][1] = "";
					String isRead = rs.getString("isopen");
					if(isRead.equals("1")) receiver[Ri][2] = "Ȯ����";
					else receiver[Ri][2] = "";
					//System.out.println("����� �뺸�� ��� �迭 : " + receiver[Ri][0] + " : " + Ri);
					Ri++;
				}
			} 
			//�������� ������ ��
			else {	
				receiver = new String[cnt][3];		//�����뺸 (�μ��� �ϳ��� ���ΰ���)	
				for(int i=0; i<cnt; i++) for(int j=0; j<3; j++) receiver[i][j]="";
				StringTokenizer Rlist = new StringTokenizer(receivers,",");
				int Ri = 0;
				while(Rlist.hasMoreTokens()) {
					receiver[Ri][0] = Rlist.nextToken();	if(receiver[Ri][0] == null) receiver[Ri][0] = "";
					receiver[Ri][1] = "";
					receiver[Ri][2] = "";
					//System.out.println("������ �뺸�� ��� �迭 : " + receiver[Ri][0] + " : " + Ri);
					Ri++;
				}
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
				
	}	
	//*******************************************************************
	// APP_MASTER ���� �ش� PID�� ���缱 �������� 
	//*******************************************************************/	
	public ArrayList getTable_line() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String tablename = "";
		String where = "";

		//���缱�� �뺸�� �б�
		getLineOrder();						//���缱 ��������
		getReceivers();						//�뺸�� �б�

		stmt = con.createStatement();
		TableAppLine table = null;
		ArrayList table_list = new ArrayList();
				
		//���������� ���缱 ����� (���� ��� �̸� ���� �μ� ���� �ڸ�Ʈ)
		//1. �����
		tablename="USER_TABLE a,CLASS_TABLE b,RANK_TABLE c ";
		where = "where (a.id='"+this.writer+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
		query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			table = new TableAppLine();
			table.setApStatus("���");						//GIAN
			table.setApSabun(this.writer);					//���
			table.setApName(rs.getString("name"));			//�̸�
			table.setApRank(rs.getString("ar_name"));		//����
			table.setApDivision(rs.getString("ac_name"));	//�μ���
			table.setApDate(this.writer_d);					//����
			table.setApComment(this.writer_c);				//�ڸ�Ʈ
			table_list.add(table);
		}

		//2. ��Ÿ ������
		int cnt = LINE_ORD.length;
		for(int i = 0; i < cnt; i++) {
			//���� �ܰ�
			if(LINE_ORD[i].equals("APV")) {
				where = "where (a.id='"+this.review+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");				//APV
					table.setApSabun(this.review);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.review_d);
					table.setApComment(this.review_c);	
					table_list.add(table);
				}
			}

			//���� �ܰ�
			else if(LINE_ORD[i].equals("APL")) {
				where = "where (a.id='"+this.decision+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");				//APL
					table.setApSabun(this.decision);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.decision_d);
					table.setApComment(this.decision_c);
					table_list.add(table);
				}
			}


			//���� �ܰ� 1 (�Է��� ������� ���Ű���� ��� Ǯ� �ۼ���)
			else if(LINE_ORD[i].equals("APG")) {
				if(this.agree_method.equals("PARALLEL")) {
					for(int n=0,m=0; n<10; n++) {					//�������ට���� ó��
						if(agree[m][0].length() == 0) break;		//�������ට���� ó��
						where = "where (a.id='"+agree[m][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
						query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
						////System.out.println(query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							table = new TableAppLine();
							table.setApStatus("����");					//APG
							table.setApSabun(agree[m][0]);
							table.setApName(rs.getString("name"));
							table.setApRank(rs.getString("ar_name"));
							table.setApDivision(rs.getString("ac_name"));
							table.setApDate(this.agree[m][1]);
							table.setApComment(this.agree[m][2]);
							table_list.add(table);
						} 
						m++;
					}//for
				} else {
						where = "where (a.id='"+agree[0][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
						query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
						////System.out.println(query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							table = new TableAppLine();
							table.setApStatus("����");					//APG
							table.setApSabun(agree[0][0]);
							table.setApName(rs.getString("name"));
							table.setApRank(rs.getString("ar_name"));
							table.setApDivision(rs.getString("ac_name"));
							table.setApDate(this.agree[0][1]);
							table.setApComment(this.agree[0][2]);
							table_list.add(table);
						} 
				} //if
			}

			//���� �ܰ� 2
			else if(LINE_ORD[i].equals("APG2")) {
				where = "where (a.id='"+agree[1][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[1][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[1][1]);
					table.setApComment(this.agree[1][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 3
			else if(LINE_ORD[i].equals("APG3")) {
				where = "where (a.id='"+agree[2][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[2][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[2][1]);
					table.setApComment(this.agree[2][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 4
			else if(LINE_ORD[i].equals("APG4")) {
				where = "where (a.id='"+agree[3][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[3][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[3][1]);
					table.setApComment(this.agree[3][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 5
			else if(LINE_ORD[i].equals("APG5")) {
					where = "where (a.id='"+agree[4][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[4][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[4][1]);
					table.setApComment(this.agree[4][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 6
			else if(LINE_ORD[i].equals("APG6")) {
				where = "where (a.id='"+agree[5][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[5][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[5][1]);
					table.setApComment(this.agree[5][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 7
			else if(LINE_ORD[i].equals("APG7")) {
				where = "where (a.id='"+agree[6][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[6][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[6][1]);
					table.setApComment(this.agree[6][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 8
			else if(LINE_ORD[i].equals("APG8")) {
				where = "where (a.id='"+agree[7][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[7][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[7][1]);
					table.setApComment(this.agree[7][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 9
			else if(LINE_ORD[i].equals("APG9")) {
					where = "where (a.id='"+agree[8][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[8][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[8][1]);
					table.setApComment(this.agree[8][2]);
					table_list.add(table);
				}
			}
			//���� �ܰ� 10
			else if(LINE_ORD[i].equals("APG10")) {
				where = "where (a.id='"+agree[9][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
				query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
				////System.out.println(query);
				rs = stmt.executeQuery(query);
				if(rs.next()) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[9][0]);
					table.setApName(rs.getString("name"));
					table.setApRank(rs.getString("ar_name"));
					table.setApDivision(rs.getString("ac_name"));
					table.setApDate(this.agree[9][1]);
					table.setApComment(this.agree[9][2]);
					table_list.add(table);
				}
			}

			//�뺸 �ܰ� (�μ��뺸�� user_table�� �������� ������ �뺸�� ��µ��� ����)
			else if(LINE_ORD[i].equals("API")) {
				int rec_cnt = receiver.length;
				//System.out.println("���缱 �迭 �뺸�ڼ� : " + rec_cnt);
				for(int r=0; r<rec_cnt; r++) {
					//����� �뺸
					where = "where (a.id='"+receiver[r][0]+"') and (a.ac_id = b.ac_id) and (a.rank = c.ar_code)";
					query = "SELECT a.name,c.ar_name,b.ac_name FROM "+tablename+where;	
					////System.out.println("������뺸 ���缱 : " + query);
					rs = stmt.executeQuery(query);
					if(rs.next()) {
						table = new TableAppLine();
						table.setApStatus("�뺸");				//API
						table.setApSabun(receiver[r][0]);
						table.setApName(rs.getString("name"));
						table.setApRank(rs.getString("ar_name"));
						table.setApDivision(rs.getString("ac_name"));
						table.setApDate(this.receiver[r][1]);
						table.setApComment(this.receiver[r][2]);
						table_list.add(table);
					}

					//�μ� �뺸
					else {
						tablename="CLASS_TABLE ";		//�μ��� �����Ҷ�
						where = "where (ac_id = '"+receiver[r][0]+"')";
						query = "SELECT ac_name FROM "+tablename+where;	
						////System.out.println("������뺸 �μ� ���缱 : "+query);
						rs = stmt.executeQuery(query);
						if(rs.next()) {
							String ac_name = rs.getString("ac_name");
							table = new TableAppLine();
							table.setApStatus("�뺸");				//API
							table.setApSabun(receiver[r][0]);
							table.setApName("�μ��뺸");
							table.setApRank("�μ���");
							table.setApDivision(ac_name);
							table.setApDate(this.receiver[r][1]);
							table.setApComment(this.receiver[r][2]);
							table_list.add(table);
						}
						tablename="USER_TABLE a,CLASS_TABLE b,RANK_TABLE c ";	//������Ŵ
					}
				} //for
			}

		}//for

		//���� �׸� ������ (�ݱ�)
		rs.close();
		stmt.close();
		return table_list;
	}	

	//*******************************************************************
	// ���ڰ��� ÷������ �����ϱ�
	//*******************************************************************/	
	public void deleteFile(String pid,String File1,String File2,String File3) throws Exception
	{
		//���� ���� ����
		text = new textFileReader();
		if(pid == null) pid = "";
		if(File1 == null) File1 = "";
		if(File2 == null) File2 = "";
		if(File3 == null) File3 = "";

		//DB Update ����
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		if(File1.length() != 0) {
			update = "UPDATE APP_MASTER set add_1_original='', add_1_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			//System.out.println(update);
			//System.out.println(File1);
			text.delFilename(File1);	//�ش� ���ϻ��� �ϱ�
		}

		if(File2.length() != 0) {
			update = "UPDATE APP_MASTER set add_2_original='', add_2_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			text.delFilename(File2);	//�ش� ���ϻ��� �ϱ�
		}

		if(File3.length() != 0) {
			update = "UPDATE APP_MASTER set add_3_original='', add_3_file='' where pid='"+pid+"'";
			stmt.executeUpdate(update);
			text.delFilename(File3);	//�ش� ���ϻ��� �ϱ�
		}
		stmt.close();
		
	}
}