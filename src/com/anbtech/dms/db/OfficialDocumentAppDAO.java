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

	private String id;					//공통:관리번호
	private String bon_path;			//확장path
	private String bon_file;			//본문저장 파일명
	private String sname;				//공통:파일저장명	(확장자 .bin이없음)	

	//*******************************************************************
	//	생성자 만들기
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
	// DB OfficialDocument_app에서 해당ID QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getDoc_Read (String id) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentAppTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM OfficialDocument_app where id='"+id+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new OfficialDocumentAppTable();
				
				table.setId(rs.getString("id"));
				
				//기안자
				table.setGianId(rs.getString("gian_id"));							
				table.setGianName(rs.getString("gian_name"));							
				table.setGianRank(rs.getString("gian_rank"));	
				table.setGianDiv(rs.getString("gian_div"));	
				table.setGianDate(rs.getString("gian_date"));	
				table.setGianComment(rs.getString("gian_comment"));	

				//검토자
				table.setReviewId(rs.getString("review_id"));							
				table.setReviewName(rs.getString("review_name"));							
				table.setReviewRank(rs.getString("review_rank"));	
				table.setReviewDiv(rs.getString("review_div"));	
				table.setReviewDate(rs.getString("review_date"));	
				table.setReviewComment(rs.getString("review_comment"));	

				//협조자
				table.setAgreeIds(rs.getString("agree_ids"));							
				table.setAgreeNames(rs.getString("agree_names"));							
				table.setAgreeRanks(rs.getString("agree_ranks"));	
				table.setAgreeDivs(rs.getString("agree_divs"));	
				table.setAgreeDates(rs.getString("agree_dates"));	
				table.setAgreeComments(rs.getString("agree_comments"));	

				//승인자
				table.setDecisionId(rs.getString("decision_id"));							
				table.setDecisionName(rs.getString("decision_name"));							
				table.setDecisionRank(rs.getString("decision_rank"));	
				table.setDecisionDiv(rs.getString("decision_div"));	
				table.setDecisionDate(rs.getString("decision_date"));	
				table.setDecisionComment(rs.getString("decision_comment"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// DB 전자결재저장[app_save]에서 해당ID QUERY하기 (개별 읽기)
	// 결재승인시 app_save --> OfficialDocument_app로 저장시 활용됨.
	// id : 전자결재 관리번호
	//*******************************************************************/	
	public ArrayList getDoc_AppSave (String id) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentAppTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
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

		//데이터 담기
		while(rs.next()) { 
				table = new OfficialDocumentAppTable();
				
				table.setId(rs.getString("pid"));
				
				//기안자
				table.setGianId(rs.getString("writer"));							
				table.setGianName(rs.getString("writer_name"));							
				table.setGianRank(rs.getString("writer_rank"));	
				table.setGianDiv(rs.getString("writer_div"));	
				table.setGianDate(rs.getString("write_date"));	
				table.setGianComment("");	

				//검토자
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

				//협조자
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

				//승인자
				table.setDecisionId(rs.getString("decision"));							
				table.setDecisionName(rs.getString("decision_name"));							
				table.setDecisionRank(rs.getString("decision_rank"));	
				table.setDecisionDiv(rs.getString("decision_div"));	
				table.setDecisionDate(rs.getString("decision_date"));	
				table.setDecisionComment(rs.getString("decision_comment"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
}
