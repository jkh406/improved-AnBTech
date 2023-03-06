package com.anbtech.board.db;

import com.anbtech.board.entity.*;
import com.anbtech.board.business.*;

import java.sql.*;
import java.util.*;

public class Table_CmtDAO{

	private Connection con;

	public Table_CmtDAO(Connection con){
		this.con = con;
	}

	public void setTable_cmt(String tablename, String no, String writer, String comment, String password) throws Exception{

		PreparedStatement pstmt = null;

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd 'at' hh:mm:ss");
		String w_time = vans.format(now);

		String query = "INSERT INTO " + tablename + "_cmt VALUES (?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1, no);
		pstmt.setString(2, writer);
		pstmt.setString(3, comment);
		pstmt.setString(4, password);
		pstmt.setString(5, w_time);
		pstmt.executeUpdate();
		pstmt.close();

		TableDAO tableDAO = new TableDAO(con);
		tableDAO.updTable(tablename, " SET cid=cid+1"," WHERE no=" + no);
	}

	public Table_Cmt getTable_cmt(String tablename, String no) throws Exception{

		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		Table_Cmt table_cmt = new Table_Cmt();
		query = "SELECT * FROM " + tablename + "_cmt WHERE no=" + no;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){
			table_cmt.setNo(rs.getInt("no"));
			table_cmt.setOno(rs.getInt("ono"));
			table_cmt.setWriter(rs.getString("writer"));
			table_cmt.setComment(rs.getString("comment"));
			table_cmt.setW_time(rs.getString("w_time"));
			table_cmt.setPasswd(rs.getString("passwd"));
		}else{
			throw new Exception("no에 해당하는 comment가 없습니다.");
		}
		stmt.close();
		rs.close();
		return table_cmt;
	}

	public ArrayList getTable_cmt_list(String tablename, String no, String multino, String searchword, String searchscope, String category, String boardpage) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		ArrayList table_cmt_list = new ArrayList();

		String query = "SELECT * FROM " + tablename + "_cmt WHERE ono=" + no + " ORDER BY no ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()){
			String comment = rs.getString("comment");
			comment = com.anbtech.text.StringProcess.repWord(comment, "<", "&lt;");
			comment = com.anbtech.text.StringProcess.repWord(comment, ">", "&gt;");
			comment = com.anbtech.text.StringProcess.repWord(comment, "\n", "<br>");

			String input_hidden = "<INPUT TYPE=hidden NAME=tablename VALUE='"+tablename+"'><INPUT TYPE=hidden NAME=mode VALUE='comment_del'><INPUT TYPE=hidden NAME=boardpage VALUE='"+boardpage+"'><INPUT TYPE=hidden NAME=searchword VALUE='"+searchword+"'><INPUT TYPE=hidden NAME=searchscope VALUE='"+searchscope+"'><INPUT TYPE=hidden NAME=category VALUE='"+category+"'><INPUT TYPE=hidden NAME=no_cmt VALUE='"+rs.getInt("no")+"'><INPUT TYPE=hidden NAME=no VALUE='"+no+"'><INPUT TYPE=hidden NAME=multino VALUE='"+multino+"'>";

			Table_Cmt table_cmt = new Table_Cmt();
			table_cmt.setWriter(rs.getString("writer"));
			table_cmt.setW_time(rs.getString("w_time"));
			table_cmt.setComment(comment);
			table_cmt.setPasswd(input_hidden);

			table_cmt_list.add(table_cmt);
		}
		stmt.close();
		rs.close();

		return table_cmt_list;
	}

	public void delTable_cmt(String tablename, String no) throws Exception{

		Statement stmt = null;
		String query = null;

		//코맨트의 no값에 할당하는 Table의 no를 가져와서 cid를 -1한다.
		Table_Cmt table_cmt = new Table_Cmt();
		table_cmt = getTable_cmt(tablename, no);
		TableDAO tableDAO = new TableDAO(con);
		tableDAO.updTable(tablename, " SET cid=cid-1"," WHERE no=" + table_cmt.getOno());

		//실제적으로 코맨트를 삭제한다.
		query = "DELETE FROM " + tablename + "_cmt WHERE no=" + no;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
}