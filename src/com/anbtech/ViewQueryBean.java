package com.anbtech;
import java.sql.*;
import com.anbtech.dbconn.DbConnection;

public class ViewQueryBean
{

	DbConnection dc = null;
	ResultSet rset = null;

	//-------------------------------------------------------------------------
	// 생성자
	//-------------------------------------------------------------------------
	public ViewQueryBean()
	{
		dc = new DbConnection();
	}

	//-------------------------------------------------------------------------
	// 커넥션을 연결함
	//-------------------------------------------------------------------------
	public boolean openConnection() 
	{
		return dc.openConnection();
	}

	//-------------------------------------------------------------------------
	// SQL문을 수행 (SELECT)
	//-------------------------------------------------------------------------
	public void executeQuery(String query) throws SQLException 
	{
		
		this.rset = dc.executeQuery(query);
		
	}

	//-------------------------------------------------------------------------
	// SQL문을 수행 (INSERT, DELETE, UPDATE)
	//-------------------------------------------------------------------------
	public void executeUpdate(String query) throws SQLException 
	{
		dc.executeUpdate(query);
	}

	//-------------------------------------------------------------------------
	// SQL문을 수행 (All sql statement)
	//-------------------------------------------------------------------------
	public void execute(String query) throws SQLException 
	{
		dc.execute(query);
	}
	/**************************************************************************
	// Transaction 처리하기
	***************************************************************************/
	public void setAutoCommit(boolean flag) throws SQLException 
	{
		dc.setAutoCommit(flag);
	}

	public boolean getAutoCommit() throws SQLException 
	{
		return dc.getAutoCommit();
	}

	public void commit() throws SQLException 
	{
		dc.commit();
	}
	
	public void rollback() throws SQLException 
	{
		dc.rollback();
	}
	/**************************************************************************
	// Locking 처리하기
	// Transaction이 종료되면 자동 잠금해제됨.
	***************************************************************************/
	public void setTransactionIsolation (int level) throws SQLException 
	{
		dc.setTransactionIsolation(level);
		/*
		if(level == 1) 
			dc.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		else if(level == 2) 
			dc.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		else if(level == 3) 
			dc.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		*/
	}	

	public int getTransactionIsolation () throws SQLException 
	{
		return dc.getTransactionIsolation();
	}
	//-------------------------------------------------------------------------
	// 전체 컬럼의 개수를 구함.
	//-------------------------------------------------------------------------
	public int getColumnCount() throws SQLException 
	{
		ResultSetMetaData rsmd = rset.getMetaData();
		return rsmd.getColumnCount();
	}

	//-------------------------------------------------------------------------
	// 인덱스로 컬럼명을 얻음
	//-------------------------------------------------------------------------
	public String getColumnName(int index) throws SQLException 
	{
		ResultSetMetaData rsmd = rset.getMetaData();
		return rsmd.getColumnName(index);
	}

	//-------------------------------------------------------------------------
	// 컬럼 인덱스로 데이터를 얻음
	//-------------------------------------------------------------------------
	public String getData(int index) throws SQLException 
	{
		return rset.getString(index);
                // return rset.getString(index).trim();
	}

	//-------------------------------------------------------------------------
	// 컬럼명으로 데이터를 얻음
	//-------------------------------------------------------------------------
	public String getData(String columnName) throws SQLException 
	{
		return rset.getString(columnName);
                // return rset.getString(columnName).trim();
	}

	//-------------------------------------------------------------------------
	// ResultSet 의 포인터를 다음으로 이동시킴
	//-------------------------------------------------------------------------
	public boolean next() throws SQLException 
	{
		return rset.next();
	}

	//-------------------------------------------------------------------------
	// 객체 정리
	//-------------------------------------------------------------------------
	public void close() throws SQLException
	{
		if(rset != null) rset.close();
		if(dc != null)	dc.close();
	}
	
	//-------------------------------------------------------------------------
	// finalize()
	//-------------------------------------------------------------------------
	protected void finalize() throws Throwable
	{	  
		close();
	}
}
