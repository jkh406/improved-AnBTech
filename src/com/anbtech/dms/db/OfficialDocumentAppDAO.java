package com.anbtech.dms.db;
import com.anbtech.dms.entity.*;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.text.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;

public class OfficialDocumentAppDAO
{
	private Connection con;
	private FileWriteString text;

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	private String id;					//����:������ȣ
	private String bon_path;			//Ȯ��path
	private String bon_file;			//�������� ���ϸ�
	private String sname;				//����:���������	(Ȯ���� .bin�̾���)	

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public OfficialDocumentAppDAO(Connection con) 
	{
		this.con = con;
	}

	public OfficialDocumentAppDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// DB OfficialDocument_app���� �ش�ID QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getDoc_Read (String id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentAppTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM OfficialDocument_app where id='"+id+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new OfficialDocumentAppTable();
				
				table.setId(rs.getString("id"));
				
				//�����
				table.setGianId(rs.getString("gian_id"));							
				table.setGianName(rs.getString("gian_name"));							
				table.setGianRank(rs.getString("gian_rank"));	
				table.setGianDiv(rs.getString("gian_div"));	
				table.setGianDate(rs.getString("gian_date"));	
				table.setGianComment(rs.getString("gian_comment"));	

				//������
				table.setReviewId(rs.getString("review_id"));							
				table.setReviewName(rs.getString("review_name"));							
				table.setReviewRank(rs.getString("review_rank"));	
				table.setReviewDiv(rs.getString("review_div"));	
				table.setReviewDate(rs.getString("review_date"));	
				table.setReviewComment(rs.getString("review_comment"));	

				//������
				table.setAgreeIds(rs.getString("agree_ids"));							
				table.setAgreeNames(rs.getString("agree_names"));							
				table.setAgreeRanks(rs.getString("agree_ranks"));	
				table.setAgreeDivs(rs.getString("agree_divs"));	
				table.setAgreeDates(rs.getString("agree_dates"));	
				table.setAgreeComments(rs.getString("agree_comments"));	

				//������
				table.setDecisionId(rs.getString("decision_id"));							
				table.setDecisionName(rs.getString("decision_name"));							
				table.setDecisionRank(rs.getString("decision_rank"));	
				table.setDecisionDiv(rs.getString("decision_div"));	
				table.setDecisionDate(rs.getString("decision_date"));	
				table.setDecisionComment(rs.getString("decision_comment"));	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// DB ���ڰ�������[app_save]���� �ش�ID QUERY�ϱ� (���� �б�)
	// ������ν� app_save --> OfficialDocument_app�� ����� Ȱ���.
	// id : ���ڰ��� ������ȣ
	//*******************************************************************/	
	public ArrayList getDoc_AppSave (String id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentAppTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT pid,writer,writer_name,writer_rank,writer_div,write_date,";
		query +="reviewer,reviewer_name,reviewer_rank,reviewer_div,review_date,review_comment,";
		query +="agree,agree_name,agree_rank,agree_div,agree_date,agree_comment,";
		query +="agree2,agree2_name,agree2_rank,agree2_div,agree2_date,agree2_comment,";
		query +="agree3,agree3_name,agree3_rank,agree3_div,agree3_date,agree3_comment,";
		query +="agree4,agree4_name,agree4_rank,agree4_div,agree4_date,agree4_comment,";
		query +="agree5,agree5_name,agree5_rank,agree5_div,agree5_date,agree5_comment,";
		query +="agree6,agree6_name,agree6_rank,agree6_div,agree6_date,agree6_comment,";
		query +="agree7,agree7_name,agree7_rank,agree7_div,agree7_date,agree7_comment,";
		query +="agree8,agree8_name,agree8_rank,agree8_div,agree8_date,agree8_comment,";
		query +="agree9,agree9_name,agree9_rank,agree9_div,agree9_date,agree9_comment,";
		query +="decision,decision_name,decision_rank,decision_div,decision_date,decision_comment";
		query +=" FROM app_save where pid='"+id+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new OfficialDocumentAppTable();
				
				table.setId(rs.getString("pid"));
				
				//�����
				table.setGianId(rs.getString("writer"));							
				table.setGianName(rs.getString("writer_name"));							
				table.setGianRank(rs.getString("writer_rank"));	
				table.setGianDiv(rs.getString("writer_div"));	
				table.setGianDate(rs.getString("write_date"));	
				table.setGianComment("");	

				//������
				String review_id = rs.getString("reviewer");			if(review_id == null) review_id = "";				else if(review_id.equals("null")) review_id = "";		
				String review_name = rs.getString("reviewer_name");		if(review_name == null) review_name = "";			else if(review_name.equals("null")) review_name = "";
				String review_rank = rs.getString("reviewer_rank");		if(review_rank == null) review_rank = "";			else if(review_rank.equals("null")) review_rank = "";
				String review_div = rs.getString("reviewer_div");		if(review_div == null) review_div = "";				else if(review_div.equals("null")) review_div = "";
				String review_date = rs.getString("review_date");		if(review_date == null) review_date = "";			else if(review_date.equals("null")) review_date = "";
				String review_comment = rs.getString("review_comment");	if(review_comment == null) review_comment = "";		else if(review_comment.equals("null")) review_comment = "";
				table.setReviewId(review_id);							
				table.setReviewName(review_name);							
				table.setReviewRank(review_rank);	
				table.setReviewDiv(review_div);	
				table.setReviewDate(review_date);	
				table.setReviewComment(review_comment);	

				//������
				String agree_ids = rs.getString("agree");				if(agree_ids == null) agree_ids = "";				else if(agree_ids.length() != 0) agree_ids += ";";		
				String agree_names = rs.getString("agree_name");		if(agree_names == null) agree_names = "";			else if(!agree_names.equals("null")) agree_names += ";";
				String agree_ranks = rs.getString("agree_rank");		if(agree_ranks == null) agree_ranks = "";			else if(!agree_ranks.equals("null")) agree_ranks += ";";
				String agree_divs = rs.getString("agree_div");			if(agree_divs == null) agree_divs = "";				else if(!agree_divs.equals("null")) agree_divs += ";";
				String agree_dates = rs.getString("agree_date");		if(agree_dates == null) agree_dates = "";			else if(!agree_dates.equals("null")) agree_dates += ";";
				String agree_comments = rs.getString("agree_comment");	if(agree_comments == null) agree_comments = "";		else if(!agree_comments.equals("null")) agree_comments += ";";
				String[] num = {"2","3","4","5","6","7","8","9"};
				for(int i=0; i<8; i++) {
					String ad = rs.getString("agree"+num[i]);			if(ad == null) agree_ids += "";				else if(ad.length() != 0) agree_ids = agree_ids+ad+";";
					String na = rs.getString("agree"+num[i]+"_name");	if(na == null) agree_names += "";			else if(!na.equals("null")) agree_names = agree_names+na+";";
					String ra = rs.getString("agree"+num[i]+"_rank");	if(ra == null) agree_ranks += "";			else if(!ra.equals("null")) agree_ranks = agree_ranks+ra+";";
					String di = rs.getString("agree"+num[i]+"_div");	if(di == null) agree_divs += "";			else if(!di.equals("null")) agree_divs = agree_divs+di+";";
					String da = rs.getString("agree"+num[i]+"_date");	if(da == null) agree_dates += "";			else if(!da.equals("null")) agree_dates = agree_dates+da+";";
					String co = rs.getString("agree"+num[i]+"_comment");if(co == null) agree_comments += "";		else if(!co.equals("null")) agree_comments = agree_comments+co+";";
				}
		
				table.setAgreeIds(agree_ids);							
				table.setAgreeNames(agree_names);							
				table.setAgreeRanks(agree_ranks);	
				table.setAgreeDivs(agree_divs);	
				table.setAgreeDates(agree_dates);	
				table.setAgreeComments(agree_comments);	

				//������
				table.setDecisionId(rs.getString("decision"));							
				table.setDecisionName(rs.getString("decision_name"));							
				table.setDecisionRank(rs.getString("decision_rank"));	
				table.setDecisionDiv(rs.getString("decision_div"));	
				table.setDecisionDate(rs.getString("decision_date"));	
				table.setDecisionComment(rs.getString("decision_comment"));	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
}
