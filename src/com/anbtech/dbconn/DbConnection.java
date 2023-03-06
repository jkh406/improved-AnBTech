package com.anbtech.dbconn;
import java.util.*;
import java.sql.*;
import java.io.*;
import sun.jdbc.odbc.*;

public class DbConnection
{

	Connection	conn	= null;
	Statement	stmt	= null;
	ResultSet	rset	= null;	

	public DbConnection()
	{

	}
	/**************************************************************************
	 *		�޼ҵ��	: openConnection 
	 *		����		: ����
	 *		������		: boolean 
	 *		����		: �����ͺ��̽��� Ŀ�ؼ��� ���� ���� �޼ҵ�
	 *				      Ŀ�ؼ��� ���� ���� Ŭ�����н��� ��ġ�� db.properties�� �д´�.
	 *************************************************************************/
	public boolean openConnection() 
	{
		
		//---------------------------------------------------------------------
		// STEP 1. ������Ƽ ������ �ε��Ѵ�.
		//---------------------------------------------------------------------
		Properties prop = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("./db.properties");
			prop.load(is);
			if(is!=null) is.close();
		} catch(IOException e) {
			System.out.println("[DbConnection] ���� ���� �� ����");
		}

		//---------------------------------------------------------------------
		// STEP 2. ������Ƽ ���Ͽ��� ������Ƽ�� �д´�. 
		//---------------------------------------------------------------------
		String jdbc = prop.getProperty("drivers");
		String url = prop.getProperty("mssql.host");
		String user = prop.getProperty("mssql.db_user");
		String password = prop.getProperty("mssql.db_password");

		//---------------------------------------------------------------------
		// STEP 3. �Էµ� ������Ƽ�� ����� �������� ����Ͽ� �ش�. 
		//---------------------------------------------------------------------
		//System.out.println("jdbc=["+jdbc+"]");
		//System.out.println("url=["+url+"]");
		//System.out.println("user=["+user+"]");
		//System.out.println("password=["+password+"]");

		//---------------------------------------------------------------------
		// STEP 4. JDBC ����̹��� �����Ѵ�.
		//---------------------------------------------------------------------
		try {
			Class.forName(jdbc);
		} catch (ClassNotFoundException e) {
			System.out.println(" JDBC ����̹� ����� ����: "+e.getMessage());
			return false;
		}

		//---------------------------------------------------------------------
		// STEP 5. �����ͺ��̽� Ŀ�ؼ��� �����Ѵ�.
		//---------------------------------------------------------------------
		try {
			this.conn = DriverManager.getConnection (url, user, password);
		} 
		catch(SQLException e) {
			System.out.println("Ŀ�ؼǻ����� ����:"+e.getMessage());
			return false;
		}
		return true;	
			
	}

	/**************************************************************************
	 *	 connection��� �����ϱ�
	 *************************************************************************/
	public Connection getConnection() 
	{
		return this.conn;
	}
	/**************************************************************************
	 *		�޼ҵ��	: executeQuery
	 *		����		: query ( SQL �� )
	 *		������		: java.sql.ResultSet
	 *		����		: �����ͺ��̽��� ���Ǹ� �ϱ� ���� �޼ҵ� (SELECT)
	 *************************************************************************/
	public ResultSet executeQuery(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		this.rset = stmt.executeQuery(query);
		return rset;
	}


	/**************************************************************************
	 *		�޼ҵ��	: executeUpdate
	 *		����		: query ( SQL �� )
	 *		������		: void
	 *		����		: �����ͺ��̽��� ������ ���� �޼ҵ� 
	 *					: (UPDATE,DELETE,INSERT)
	 *************************************************************************/
	public void executeUpdate(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		stmt.executeUpdate(query);
		if(stmt!=null) stmt.close();
		
	}
	
	/**************************************************************************
	 *		�޼ҵ��	: execute
	 *		����		: query ( SQL �� )
	 *		������		: void
	 *		����		: �����ͺ��̽��� �Ϲ����� �޼ҵ� 
	 *************************************************************************/
	public void execute(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		stmt.execute(query);
		if(stmt!=null) stmt.close();
		
	}
	
	/**************************************************************************
	// Transaction ó���ϱ�
	***************************************************************************/
	public void setAutoCommit(boolean flag) throws SQLException 
	{
		conn.setAutoCommit(flag);
	}

	public boolean getAutoCommit() throws SQLException 
	{
		return conn.getAutoCommit();
	}

	public void commit() throws SQLException 
	{
		conn.commit();
	}
	
	public void rollback() throws SQLException 
	{
		conn.rollback();
	}

	/**************************************************************************
	// Locking ó���ϱ�
	// Transaction�� ����Ǹ� �ڵ� ���������.
	***************************************************************************/
	public void setTransactionIsolation (int level) throws SQLException 
	{
		conn.setTransactionIsolation(level);
		/*
		if(level == 1) 
			conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		else if(level == 2) 
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		else if(level == 3) 
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		*/
	}

	public int getTransactionIsolation () throws SQLException 
	{
		return conn.getTransactionIsolation();
	}

	/**************************************************************************
	 *		�޼ҵ��	: close
	 *		����		: ����
	 *		������		: void
	 *		����		: �����ͺ��̽��� Ŀ�ؼǰ� ���õ� �ڿ��� �����ش�. 
	 *************************************************************************/
	public void close() throws SQLException 
	{
		if(conn!=null) conn.close();
		if(rset!=null) rset.close();
		if(stmt!=null) stmt.close();
	}
	
	/**************************************************************************
	 *		�޼ҵ��	: finalize
	 *		����		: ����
	 *		������		: void
	 *		����		: ������ 
	 *************************************************************************/
	protected void finalize() throws Throwable
	{	  
		this.close();
	}
	
}
