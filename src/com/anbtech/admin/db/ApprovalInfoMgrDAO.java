package com.anbtech.admin.db;

import java.sql.*;
import java.util.*;
import com.anbtech.admin.entity.*;

public class ApprovalInfoMgrDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public ApprovalInfoMgrDAO(Connection con){
		this.con = con;
	}

	/*****************************************************************************
	 * 전자결재모듈의 app_save 테이블에서 선택된 pid의 레코드 정보를 가져온 뒤,
	 * 각 모듈의 approval_info 테이블에 입력한다.
	 *****************************************************************************/
	public void getAppInfoAndSave(String tablename,String pid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM app_save WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			query = "INSERT INTO " + tablename + " (pid,writer,writer_name,writer_div,writer_rank,write_date,";
			query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
			query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
			query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
			query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
			query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
			query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
			query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date) ";
			query += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1,pid);
			pstmt.setString(2,rs.getString("writer"));
			pstmt.setString(3,rs.getString("writer_name"));
			pstmt.setString(4,rs.getString("writer_div"));
			pstmt.setString(5,rs.getString("writer_rank"));
			pstmt.setString(6,rs.getString("write_date"));
			pstmt.setString(7,rs.getString("reviewer"));
			pstmt.setString(8,rs.getString("reviewer_name"));
			pstmt.setString(9,rs.getString("reviewer_div"));
			pstmt.setString(10,rs.getString("reviewer_rank"));
			pstmt.setString(11,rs.getString("review_comment"));
			pstmt.setString(12,rs.getString("review_date"));
			pstmt.setString(13,rs.getString("decision"));
			pstmt.setString(14,rs.getString("decision_name"));
			pstmt.setString(15,rs.getString("decision_div"));
			pstmt.setString(16,rs.getString("decision_rank"));
			pstmt.setString(17,rs.getString("decision_comment"));
			pstmt.setString(18,rs.getString("decision_date"));
			pstmt.setString(19,rs.getString("agree"));
			pstmt.setString(20,rs.getString("agree_name"));
			pstmt.setString(21,rs.getString("agree_div"));
			pstmt.setString(22,rs.getString("agree_rank"));
			pstmt.setString(23,rs.getString("agree_comment"));
			pstmt.setString(24,rs.getString("agree_date"));
			pstmt.setString(25,rs.getString("agree2"));
			pstmt.setString(26,rs.getString("agree2_name"));
			pstmt.setString(27,rs.getString("agree2_div"));
			pstmt.setString(28,rs.getString("agree2_rank"));
			pstmt.setString(29,rs.getString("agree2_comment"));
			pstmt.setString(30,rs.getString("agree2_date"));
			pstmt.setString(31,rs.getString("agree3"));
			pstmt.setString(32,rs.getString("agree3_name"));
			pstmt.setString(33,rs.getString("agree3_div"));
			pstmt.setString(34,rs.getString("agree3_rank"));
			pstmt.setString(35,rs.getString("agree3_comment"));
			pstmt.setString(36,rs.getString("agree3_date"));
			pstmt.setString(37,rs.getString("agree4"));
			pstmt.setString(38,rs.getString("agree4_name"));
			pstmt.setString(39,rs.getString("agree4_div"));
			pstmt.setString(40,rs.getString("agree4_rank"));
			pstmt.setString(41,rs.getString("agree4_comment"));
			pstmt.setString(42,rs.getString("agree4_date"));
			pstmt.setString(43,rs.getString("agree5"));
			pstmt.setString(44,rs.getString("agree5_name"));
			pstmt.setString(45,rs.getString("agree5_div"));
			pstmt.setString(46,rs.getString("agree5_rank"));
			pstmt.setString(47,rs.getString("agree5_comment"));
			pstmt.setString(48,rs.getString("agree5_date"));
		
			pstmt.executeUpdate();
			pstmt.close();
		}
		stmt.close();
		rs.close();
	}

	/*****************************************************************************
	 * approval_info 테이블에서 선택된 관리번호(pid)의 결재정보를 가져온다.
	 *****************************************************************************/
	public ApprovalInfoTable getApprovalInfo(String tablename,String pid,String sign_path) throws Exception{
		ApprovalInfoTable table = new ApprovalInfoTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM " + tablename + " WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		String sign_src = "";
		String memo = "";
		while(rs.next()){
			table = new ApprovalInfoTable();

			table.setWriterName(rs.getString("writer_name"));
			String writer = rs.getString("writer");
			sign_src = "<img src = '" + sign_path + writer + ".gif' border='0'>";
			table.setWriterSig(sign_src);

			String reviewer = rs.getString("reviewer");
			if(reviewer == null || reviewer.equals("")){
				sign_src = "<img src = '" + sign_path + "wan.gif' border='0'>";			
				table.setReviewerName("");
			}else{
				sign_src = "<img src = '" + sign_path + reviewer + ".gif' border='0'>";
				table.setReviewerName(rs.getString("reviewer_name"));
			}
			table.setReviewerSig(sign_src);

			table.setDecisionName(rs.getString("decision_name"));
			String decision = rs.getString("decision");
			sign_src = "<img src = '" + sign_path + decision + ".gif' border='0'>";
			table.setDecisionSig(sign_src);

			memo = "기안 " + writer + " " + rs.getString("writer_name") + " " + rs.getString("writer_rank") + " " + rs.getString("writer_div") + " " + rs.getString("write_date") + "\n";
			if(!reviewer.equals("")){
				memo += "검토 " + reviewer + " " + rs.getString("reviewer_name") + " " + rs.getString("reviewer_rank") + " " + rs.getString("reviewer_div") + " " + rs.getString("review_date") + " " + rs.getString("review_comment") + "\n";
			}
			memo += "승인 " + decision + " " + rs.getString("decision_name") + " " + rs.getString("decision_rank") + " " + rs.getString("decision_div") + " " + rs.getString("decision_date") + " " + rs.getString("decision_comment") + "\n";

			String agree = rs.getString("agree");
			if(agree != null && !agree.equals("")) memo += "합의 " + agree + " " + rs.getString("agree_name") + " " + rs.getString("agree_rank") + " " + rs.getString("agree_div") + " " + rs.getString("agree_date") + " " + rs.getString("agree_comment") + "\n";

			String agree2 = rs.getString("agree2");
			if(agree2 != null && !agree2.equals("")) memo += "합의 " + agree2 + " " + rs.getString("agree2_name") + " " + rs.getString("agree2_rank") + " " + rs.getString("agree2_div") + " " + rs.getString("agree2_date") + " " + rs.getString("agree2_comment") + "\n";

			String agree3 = rs.getString("agree3");
			if(agree3 != null && !agree3.equals("")) memo += "합의 " + agree3 + " " + rs.getString("agree3_name") + " " + rs.getString("agree3_rank") + " " + rs.getString("agree3_div") + " " + rs.getString("agree3_date") + " " + rs.getString("agree3_comment") + "\n";

			String agree4 = rs.getString("agree4");
			if(agree4 != null && !agree4.equals("")) memo += "합의 " + agree4 + " " + rs.getString("agree4_name") + " " + rs.getString("agree4_rank") + " " + rs.getString("agree4_div") + " " + rs.getString("agree4_date") + " " + rs.getString("agree4_comment") + "\n";

			String agree5 = rs.getString("agree5");
			if(agree5 != null && !agree5.equals("")) memo += "합의 " + agree5 + " " + rs.getString("agree5_name") + " " + rs.getString("agree5_rank") + " " + rs.getString("agree5_div") + " " + rs.getString("agree5_date") + " " + rs.getString("agree5_comment") + "\n";

			table.setMemo(memo);

		}
		stmt.close();
		rs.close();

		return table;
	}
}		