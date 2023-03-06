package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import com.anbtech.file.textFileReader;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppStoreHouseDetailDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private textFileReader text;

	private String line_doc="";			//���缱 �б�
	private String[] LINE_ORD;			//���缱 �迭�� ���

	private String[] writer;			//����� ���,�̸�,����,�μ�,����, �ڸ�Ʈ
	private String[] review;			//������ ���,�̸�,����,�μ�,����, �ڸ�Ʈ
	private String[] decision;			//������ ���,�̸�,����,�μ�,����, �ڸ�Ʈ
	private int agree_cnt = 0;			//������ ���ο���
	private String[][] agree;			//�����ڻ��,�̸�,����,�μ�,����, �ڸ�Ʈ

	private String receivers="";		//�뺸�� ���(id)
	private String[][] receiver;		//�뺸�ڻ��,�̸�,����,�μ�,����, �ڸ�Ʈ

	private String pid = "";			//������ȣ
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppStoreHouseDetailDAO(Connection con) 
	{
		this.con = con;
	}

	public AppStoreHouseDetailDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//*******************************************************************
	// APP_MASTER ���� �ش� PID�� ���븸 �������� 
	//*******************************************************************/	
	public ArrayList getTable_SavePid (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		text = new textFileReader();

		//���� �迭 �ʱ�ȭ
		writer = new String[6];
		review = new String[6];
		decision = new String[6];
		agree = new String[10][6];
		
		stmt = con.createStatement();
		TableAppSave table = null;
		ArrayList table_list = new ArrayList();
			
		//query���� �����
		query = "SELECT * FROM storehouse.dbo.APP_SAVE where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new TableAppSave();
				this.pid = rs.getString("pid");	if(this.pid == null) this.pid = "";
				table.setAmPid(this.pid);									//pid
				table.setAmAppSubj(rs.getString("app_subj"));				//����

				writer[0]=rs.getString("writer");		if(writer[0] == null) writer[0] = "";
				writer[1]=rs.getString("writer_name");	if(writer[1] == null) writer[1] = "";
				writer[4]=rs.getString("write_date");	if(writer[4] == null) writer[4] = "";
				table.setAmWriter(writer[0]);								//�ۼ��� ���
				table.setAmWriterName(writer[1]);							//�ۼ��� �̸�
				table.setAmWriteDate(writer[4]);							//�ۼ� ����

				table.setAmAppStatus(rs.getString("app_state"));			//����

				review[0]=rs.getString("reviewer");			if(review[0] == null) review[0] = "";
				review[4]=rs.getString("review_date");		if(review[4] == null) review[4] = "";
				review[5]=rs.getString("review_comment");	if(review[5] == null) review[5] = "";
				table.setAmReviewer(review[0]);								//������ ���
				table.setAmReviewDate(review[4]);							//���� ����
				table.setAmReviewComment(review[5]);						//������ �ǰ�

				agree[0][0]=rs.getString("agree");			if(agree[0][0] == null) agree[0][0] = "";
				agree[0][4]=rs.getString("agree_date");		if(agree[0][4] == null) agree[0][4] = "";
				agree[0][5]=rs.getString("agree_comment");	if(agree[0][5] == null) agree[0][5] = "";	
				table.setAmAgree(agree[0][0]);								//������1 ���
				table.setAmAgreeDate(agree[0][4]);							//������1 ����
				table.setAmAgreeComment(agree[0][5]);						//������1 �ǰ�

				agree[1][0]=rs.getString("agree2");			if(agree[1][0] == null) agree[1][0] = "";
				agree[1][4]=rs.getString("agree2_date");		if(agree[1][4] == null) agree[1][4] = "";
				agree[1][5]=rs.getString("agree2_comment");	if(agree[1][5] == null) agree[1][5] = "";
				table.setAmAgree2(agree[1][0]);								//������2 ���
				table.setAmAgree2Date(agree[1][4]);							//������2 ����
				table.setAmAgree2Comment(agree[1][5]);						//������2 �ǰ�

				agree[2][0]=rs.getString("agree3");			if(agree[2][0] == null) agree[2][0] = "";
				agree[2][4]=rs.getString("agree3_date");		if(agree[2][4] == null) agree[2][4] = "";
				agree[2][5]=rs.getString("agree3_comment");	if(agree[2][5] == null) agree[2][5] = "";
				table.setAmAgree3(agree[2][0]);								//������3 ���
				table.setAmAgree3Date(agree[2][4]);							//������3 ����
				table.setAmAgree3Comment(agree[2][5]);						//������3 �ǰ�
				
				agree[3][0]=rs.getString("agree4");			if(agree[3][0] == null) agree[3][0] = "";
				agree[3][4]=rs.getString("agree4_date");		if(agree[3][4] == null) agree[3][4] = "";
				agree[3][5]=rs.getString("agree4_comment");	if(agree[3][5] == null) agree[3][5] = "";
				table.setAmAgree4(agree[3][0]);								//������4 ���
				table.setAmAgree4Date(agree[3][4]);							//������4 ����
				table.setAmAgree4Comment(agree[3][5]);						//������4 �ǰ�
				
				agree[4][0]=rs.getString("agree5");			if(agree[4][0] == null) agree[4][0] = "";
				agree[4][4]=rs.getString("agree5_date");		if(agree[4][4] == null) agree[4][4] = "";
				agree[4][5]=rs.getString("agree5_comment");	if(agree[4][5] == null) agree[4][5] = "";
				table.setAmAgree5(agree[4][0]);								//������5 ���
				table.setAmAgree5Date(agree[4][4]);							//������5 ����
				table.setAmAgree5Comment(agree[4][5]);						//������5 �ǰ�
				
				agree[5][0]=rs.getString("agree6");			if(agree[5][0] == null) agree[5][0] = "";
				agree[5][4]=rs.getString("agree6_date");		if(agree[5][4] == null) agree[5][4] = "";
				agree[5][5]=rs.getString("agree6_comment");	if(agree[5][5] == null) agree[5][5] = "";
				table.setAmAgree6(agree[5][0]);								//������6 ���
				table.setAmAgree6Date(agree[5][4]);							//������6 ����
				table.setAmAgree6Comment(agree[5][5]);						//������6 �ǰ�
				
				agree[6][0]=rs.getString("agree7");			if(agree[6][0] == null) agree[6][0] = "";
				agree[6][4]=rs.getString("agree7_date");		if(agree[6][4] == null) agree[6][4] = "";
				agree[6][5]=rs.getString("agree7_comment");	if(agree[6][5] == null) agree[6][5] = "";
				table.setAmAgree7(agree[6][0]);								//������7 ���
				table.setAmAgree7Date(agree[6][4]);							//������7 ����
				table.setAmAgree7Comment(agree[6][5]);						//������7 �ǰ�
				
				agree[7][0]=rs.getString("agree8");			if(agree[7][0] == null) agree[7][0] = "";
				agree[7][4]=rs.getString("agree8_date");		if(agree[7][4] == null) agree[7][4] = "";
				agree[7][5]=rs.getString("agree8_comment");	if(agree[7][5] == null) agree[7][5] = "";
				table.setAmAgree8(agree[7][0]);								//������8 ���
				table.setAmAgree8Date(agree[7][4]);							//������8 ����
				table.setAmAgree8Comment(agree[7][5]);						//������8 �ǰ�
				
				agree[8][0]=rs.getString("agree9");			if(agree[8][0] == null) agree[8][0] = "";
				agree[8][4]=rs.getString("agree9_date");		if(agree[8][4] == null) agree[8][4] = "";
				agree[8][5]=rs.getString("agree9_comment");	if(agree[8][5] == null) agree[8][5] = "";
				table.setAmAgree9(agree[8][0]);								//������9 ���
				table.setAmAgree9Date(agree[8][4]);							//������9 ����
				table.setAmAgree9Comment(agree[8][5]);						//������9 �ǰ�
				
				agree[9][0]=rs.getString("agree10");			if(agree[9][0] == null) agree[9][0] = "";
				agree[9][4]=rs.getString("agree10_date");		if(agree[9][4] == null) agree[9][4] = "";
				agree[9][5]=rs.getString("agree10_comment");	if(agree[9][5] == null) agree[9][5] = "";
				table.setAmAgree10(agree[9][0]);							//������10 ���
				table.setAmAgree10Date(agree[9][4]);						//������10 ����
				table.setAmAgree10Comment(agree[9][5]);						//������10 �ǰ�
				
				table.setAmAgreeMethod(rs.getString("agree_method"));		//�������� (Serial Parallel)
				String ag_cnt = rs.getString("agree_count");
				if(ag_cnt != null)
					this.agree_cnt = Integer.parseInt(ag_cnt); 
				table.setAmAgreeCount(Integer.toString(this.agree_cnt));	//������ �ѿ��� 

				table.setAmAgreePass(rs.getString("agree_pass"));			//�� ������ �� (parallel�� ���)

				decision[0]=rs.getString("decision");			if(decision[0] == null) decision[0] = "";
				decision[4]=rs.getString("decision_date");		if(decision[4] == null) decision[4] = "";
				decision[5]=rs.getString("decision_comment");	if(decision[5] == null) decision[5] = "";
				table.setAmDecision(decision[0]);							//������ ���
				table.setAmDecisionDate(decision[4]);						//���� ����
				table.setAmDecisionComment(decision[5]);					//������ �ǰ�
				
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

				writer[3] = rs.getString("writer_div");				if(writer[3] == null) writer[3] = "";
				writer[2] = rs.getString("writer_rank");			if(writer[2] == null) writer[2] = "";

				review[1] = rs.getString("reviewer_name");			if(review[1] == null) review[1] = "";
				review[3] = rs.getString("reviewer_div");			if(review[3] == null) review[3] = "";
				review[2] = rs.getString("reviewer_rank");			if(review[2] == null) review[2] = "";

				agree[0][1] = rs.getString("agree_name");			if(agree[0][1] == null) agree[0][1] = "";
				agree[0][3] = rs.getString("agree_div");			if(agree[0][3] == null) agree[0][3] = "";
				agree[0][2] = rs.getString("agree_rank");			if(agree[0][2] == null) agree[0][2] = "";

				agree[1][1] = rs.getString("agree2_name");			if(agree[1][1] == null) agree[1][1] = "";
				agree[1][3] = rs.getString("agree2_div");			if(agree[1][3] == null) agree[1][3] = "";
				agree[1][2] = rs.getString("agree2_rank");			if(agree[1][2] == null) agree[1][2] = "";

				agree[2][1] = rs.getString("agree3_name");			if(agree[2][1] == null) agree[2][1] = "";
				agree[2][3] = rs.getString("agree3_div");			if(agree[2][3] == null) agree[2][3] = "";
				agree[2][2] = rs.getString("agree3_rank");			if(agree[2][2] == null) agree[2][2] = "";

				agree[3][1] = rs.getString("agree4_name");			if(agree[3][1] == null) agree[3][1] = "";
				agree[3][3] = rs.getString("agree4_div");			if(agree[3][3] == null) agree[3][3] = "";
				agree[3][2] = rs.getString("agree4_rank");			if(agree[3][2] == null) agree[3][2] = "";

				agree[4][1] = rs.getString("agree5_name");			if(agree[4][1] == null) agree[4][1] = "";
				agree[4][3] = rs.getString("agree5_div");			if(agree[4][3] == null) agree[4][3] = "";
				agree[4][2] = rs.getString("agree5_rank");			if(agree[4][2] == null) agree[4][2] = "";

				agree[5][1] = rs.getString("agree6_name");			if(agree[5][1] == null) agree[5][1] = "";
				agree[5][3] = rs.getString("agree6_div");			if(agree[5][3] == null) agree[5][3] = "";
				agree[5][2] = rs.getString("agree6_rank");			if(agree[5][2] == null) agree[5][2] = "";

				agree[6][1] = rs.getString("agree7_name");			if(agree[6][1] == null) agree[6][1] = "";
				agree[6][3] = rs.getString("agree7_div");			if(agree[6][3] == null) agree[6][3] = "";
				agree[6][2] = rs.getString("agree7_rank");			if(agree[6][2] == null) agree[6][2] = "";

				agree[7][1] = rs.getString("agree8_name");			if(agree[7][1] == null) agree[7][1] = "";
				agree[7][3] = rs.getString("agree8_div");			if(agree[7][3] == null) agree[7][3] = "";
				agree[7][2] = rs.getString("agree8_rank");			if(agree[7][2] == null) agree[7][2] = "";

				agree[8][1] = rs.getString("agree9_name");			if(agree[8][1] == null) agree[8][1] = "";
				agree[8][3] = rs.getString("agree9_div");			if(agree[8][3] == null) agree[8][3] = "";
				agree[8][2] = rs.getString("agree9_rank");			if(agree[8][2] == null) agree[8][2] = "";

				agree[9][1] = rs.getString("agree10_name");			if(agree[9][1] == null) agree[9][1] = "";
				agree[9][3] = rs.getString("agree10_div");			if(agree[9][3] == null) agree[9][3] = "";
				agree[9][2] = rs.getString("agree10_rank");			if(agree[9][2] == null) agree[9][2] = "";

				decision[1] = rs.getString("decision_name");		if(decision[1] == null) decision[1] = "";
				decision[3] = rs.getString("decision_div");			if(decision[3] == null) decision[3] = "";
				decision[2] = rs.getString("decision_rank");		if(decision[2] == null) decision[2] = "";

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
	private void getLineOrder() 
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
	//	���� �뺸�� ��� �˾ƺ��� (�뺸���̺��� : app_receive)
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
		}
		//�뺸�� ���,����,�ڸ�Ʈ ��� �ʱ�ȭ
		receiver = new String[cnt][6];
		
		//���� �׸�
		stmt = con.createStatement();

		String cnt_query = "SELECT COUNT(*) FROM storehouse.dbo.APP_RECEIVE where pid ='"+this.pid+"'";

		rs = stmt.executeQuery(cnt_query);
		if(rs.next()) {
			int api_cnt = rs.getInt(1);		//�뺸�Կ� ����� �Ѽ���
			//����� ������ ��
			if(api_cnt > 0) {
				receiver = new String[api_cnt][6];	//�μ� + ���κ� �뺸�� �ٽù迭ũ�� ������.
				for(int i=0; i<api_cnt; i++) for(int j=0; j<6; j++) receiver[i][j]="";
				String query = "SELECT receiver,read_date,isopen,receiver_name,receiver_div,receiver_rank FROM storehouse.dbo.APP_RECEIVE where pid='"+this.pid+"'";
			
				rs = stmt.executeQuery(query);
				int Ri = 0;
				while(rs.next()) {
					receiver[Ri][0] = rs.getString("receiver");		if(receiver[Ri][0] == null) receiver[Ri][0] = "";
					receiver[Ri][4] = rs.getString("read_date");	if(receiver[Ri][4] == null) receiver[Ri][4] = "";
					receiver[Ri][1] = rs.getString("receiver_name");if(receiver[Ri][1] == null) receiver[Ri][1] = "";
					receiver[Ri][3] = rs.getString("receiver_div");	if(receiver[Ri][3] == null) receiver[Ri][3] = "";
					receiver[Ri][2] = rs.getString("receiver_rank");if(receiver[Ri][2] == null) receiver[Ri][2] = "";

					String isRead = rs.getString("isopen");
					if(isRead.equals("1")) receiver[Ri][5] = "Ȯ����";
					else receiver[Ri][5] = "";
					Ri++;
				}
			} 
		}
	
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
				
	}	
	
	//*******************************************************************
	// APP_MASTER ���� �ش� PID�� ���븸 �������� 
	//*******************************************************************/	
	public ArrayList getTable_line() throws Exception
	{
		//���缱�� �뺸�� �б�
		getLineOrder();						//���缱 ��������
		getReceivers();						//�뺸�� �б�

		TableAppLine table = null;
		ArrayList table_list = new ArrayList();
				
		//���������� ���缱 ����� (���� ��� �̸� ���� �μ� ���� �ڸ�Ʈ)
		//1. �����
		table = new TableAppLine();
		table.setApStatus("���");					//GIAN
		table.setApSabun(writer[0]);				//���
		table.setApName(writer[1]);					//�̸�
		table.setApRank(writer[2]);					//����
		table.setApDivision(writer[3]);				//�μ�
		table.setApDate(writer[4]);					//����
		table.setApComment("");						//�ڸ�Ʈ
		table_list.add(table);

		//2. ��Ÿ ������
		int cnt = LINE_ORD.length;
		for(int i = 0; i < cnt; i++) {
			//���� �ܰ�
			if(LINE_ORD[i].equals("APV")) {
					table = new TableAppLine();
					table.setApStatus("����");				//APV
					table.setApSabun(review[0]);
					table.setApName(review[1]);
					table.setApRank(review[2]);
					table.setApDivision(review[3]);
					table.setApDate(review[4]);
					table.setApComment(review[5]);	
					table_list.add(table);
			}

			//���� �ܰ�
			else if(LINE_ORD[i].equals("APL")) {
					table = new TableAppLine();
					table.setApStatus("����");				//APL
					table.setApSabun(decision[0]);
					table.setApName(decision[1]);
					table.setApRank(decision[2]);
					table.setApDivision(decision[3]);
					table.setApDate(decision[4]);
					table.setApComment(decision[5]);
					table_list.add(table);
			}


			//���� �ܰ� 1 (�Է��� ������� ���Ű���� ��� Ǯ� �ۼ���)
			else if(LINE_ORD[i].equals("APG")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[0][0]);
					table.setApName(agree[0][1]);
					table.setApRank(agree[0][2]);
					table.setApDivision(agree[0][3]);
					table.setApDate(agree[0][4]);
					table.setApComment(agree[0][5]);
					table_list.add(table);
			}
			//���� �ܰ� 2
			else if(LINE_ORD[i].equals("APG2")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[1][0]);
					table.setApName(agree[1][1]);
					table.setApRank(agree[1][2]);
					table.setApDivision(agree[1][3]);
					table.setApDate(agree[1][4]);
					table.setApComment(agree[1][5]);
					table_list.add(table);
			}
			//���� �ܰ� 3
			else if(LINE_ORD[i].equals("APG3")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[2][0]);
					table.setApName(agree[2][1]);
					table.setApRank(agree[2][2]);
					table.setApDivision(agree[2][3]);
					table.setApDate(agree[2][4]);
					table.setApComment(agree[2][5]);
					table_list.add(table);
			}
			//���� �ܰ� 4
			else if(LINE_ORD[i].equals("APG4")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[3][0]);
					table.setApName(agree[3][1]);
					table.setApRank(agree[3][2]);
					table.setApDivision(agree[3][3]);
					table.setApDate(agree[3][4]);
					table.setApComment(agree[3][5]);
					table_list.add(table);
			}
			//���� �ܰ� 5
			else if(LINE_ORD[i].equals("APG5")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[4][0]);
					table.setApName(agree[4][1]);
					table.setApRank(agree[4][2]);
					table.setApDivision(agree[4][3]);
					table.setApDate(agree[4][4]);
					table.setApComment(agree[4][5]);
					table_list.add(table);
			}
			//���� �ܰ� 6
			else if(LINE_ORD[i].equals("APG6")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[5][0]);
					table.setApName(agree[5][1]);
					table.setApRank(agree[5][2]);
					table.setApDivision(agree[5][3]);
					table.setApDate(agree[5][4]);
					table.setApComment(agree[5][5]);
					table_list.add(table);
			}
			//���� �ܰ� 7
			else if(LINE_ORD[i].equals("APG7")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[6][0]);
					table.setApName(agree[6][1]);
					table.setApRank(agree[6][2]);
					table.setApDivision(agree[6][3]);
					table.setApDate(agree[6][4]);
					table.setApComment(agree[6][5]);
					table_list.add(table);
			}
			//���� �ܰ� 8
			else if(LINE_ORD[i].equals("APG8")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[7][0]);
					table.setApName(agree[7][1]);
					table.setApRank(agree[7][2]);
					table.setApDivision(agree[7][3]);
					table.setApDate(agree[7][4]);
					table.setApComment(agree[7][5]);
					table_list.add(table);
			}
			//���� �ܰ� 9
			else if(LINE_ORD[i].equals("APG9")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[8][0]);
					table.setApName(agree[8][1]);
					table.setApRank(agree[8][2]);
					table.setApDivision(agree[8][3]);
					table.setApDate(agree[8][4]);
					table.setApComment(agree[8][5]);
					table_list.add(table);
			}
			//���� �ܰ� 10
			else if(LINE_ORD[i].equals("APG10")) {
					table = new TableAppLine();
					table.setApStatus("����");					//APG
					table.setApSabun(agree[9][0]);
					table.setApName(agree[9][1]);
					table.setApRank(agree[9][2]);
					table.setApDivision(agree[9][3]);
					table.setApDate(agree[9][4]);
					table.setApComment(agree[9][5]);
					table_list.add(table);
			}

			//�뺸 �ܰ�
			else if(LINE_ORD[i].equals("API")) {
				int rec_cnt = receiver.length;
				for(int r=0; r<rec_cnt; r++) {
						table = new TableAppLine();
						table.setApStatus("�뺸");				//API
						table.setApSabun(receiver[r][0]);
						table.setApName(receiver[r][1]);
						table.setApRank(receiver[r][2]);
						table.setApDivision(receiver[r][3]);
						table.setApDate(receiver[r][4]);
						table.setApComment(receiver[r][5]);
						table_list.add(table);
				} //for
			}

		}//for
		return table_list;
	}	

	//*******************************************************************
	//	�뺸�ڰ� �뺸������ �������� ���̺� �����ϱ�
	//*******************************************************************/
	public void setReadInform(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		// APP_RECEIVE���̺��� ��������
		query = "SELECT pid,isopen from storehouse.dbo.APP_RECEIVE where receiver='"+id+"'";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid");
			String isopen = rs.getString("isopen");
			if(isopen.equals("0")) {
				setIsOpen("storehouse.dbo.APP_RECEIVE",PID,id);
			}
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
				
	}
	
	//*******************************************************************
	//	�뺸���� ������,��������,������������
	//*******************************************************************/
	private void setIsOpen(String tablename,String PID,String id) throws Exception
	{
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		
		Statement stmt = null;
		stmt = con.createStatement();
		
		String update = "update "+tablename+" set isopen='1',read_date='"+anbdt.getTime()+"',";
			  update += "delete_date='"+anbdt.getAddMonthNoformat(1)+"' where pid='"+PID+"' and ";
			  update += "receiver='"+id+"'";
		stmt.executeUpdate(update);
		
		stmt.close();
	}
}
