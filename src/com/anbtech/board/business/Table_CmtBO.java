package com.anbtech.board.business;

import com.anbtech.board.entity.*;
import com.anbtech.board.db.*;

import java.sql.*;

public class Table_CmtBO{

	private Connection con;

	public Table_CmtBO(Connection con){
		this.con = con;
	}

	public void delTable_cmt_afterchk(String tablename, String no, String password) throws Exception{

		Table_Cmt table_cmt = new Table_Cmt();
		Table_CmtDAO table_cmtDAO = new Table_CmtDAO(con);
		table_cmt = table_cmtDAO.getTable_cmt(tablename, no);

		if (password.equals(table_cmt.getPasswd())) table_cmtDAO.delTable_cmt(tablename, no);
		else throw new Exception("<script> alert('비밀번호가 정확하지 않습니다.');	history.go(-1); </script>");
	}
	public void delTable_cmt_admin(String tablename, String no) throws Exception{

		Table_CmtDAO table_cmtDAO = new Table_CmtDAO(con);
		table_cmtDAO.delTable_cmt(tablename, no);
	}
}