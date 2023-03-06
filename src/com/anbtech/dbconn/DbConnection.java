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
	 *		메소드명	: openConnection 
	 *		인자		: 없음
	 *		리턴형		: boolean 
	 *		설명		: 데이터베이스로 커넥션을 열기 위한 메소드
	 *				      커넥션을 열기 위해 클래스패스에 위치한 db.properties를 읽는다.
	 *************************************************************************/
	public boolean openConnection() 
	{
		
		//---------------------------------------------------------------------
		// STEP 1. 프로퍼티 파일을 로드한다.
		//---------------------------------------------------------------------
		Properties prop = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("./db.properties");
			prop.load(is);
			if(is!=null) is.close();
		} catch(IOException e) {
			System.out.println("[DbConnection] 파일 오픈 중 에러");
		}

		//---------------------------------------------------------------------
		// STEP 2. 프로퍼티 파일에서 프로퍼티를 읽는다. 
		//---------------------------------------------------------------------
		String jdbc = prop.getProperty("drivers");
		String url = prop.getProperty("mssql.host");
		String user = prop.getProperty("mssql.db_user");
		String password = prop.getProperty("mssql.db_password");

		//---------------------------------------------------------------------
		// STEP 3. 입력된 프로퍼티를 디버깅 차원에서 출력하여 준다. 
		//---------------------------------------------------------------------
		//System.out.println("jdbc=["+jdbc+"]");
		//System.out.println("url=["+url+"]");
		//System.out.println("user=["+user+"]");
		//System.out.println("password=["+password+"]");

		//---------------------------------------------------------------------
		// STEP 4. JDBC 드라이버를 적재한다.
		//---------------------------------------------------------------------
		try {
			Class.forName(jdbc);
		} catch (ClassNotFoundException e) {
			System.out.println(" JDBC 드라이버 등록중 에러: "+e.getMessage());
			return false;
		}

		//---------------------------------------------------------------------
		// STEP 5. 데이터베이스 커넥션을 오픈한다.
		//---------------------------------------------------------------------
		try {
			this.conn = DriverManager.getConnection (url, user, password);
		} 
		catch(SQLException e) {
			System.out.println("커넥션생성중 에러:"+e.getMessage());
			return false;
		}
		return true;	
			
	}

	/**************************************************************************
	 *	 connection결과 리턴하기
	 *************************************************************************/
	public Connection getConnection() 
	{
		return this.conn;
	}
	/**************************************************************************
	 *		메소드명	: executeQuery
	 *		인자		: query ( SQL 문 )
	 *		리턴형		: java.sql.ResultSet
	 *		설명		: 데이터베이스로 질의를 하기 위한 메소드 (SELECT)
	 *************************************************************************/
	public ResultSet executeQuery(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		this.rset = stmt.executeQuery(query);
		return rset;
	}


	/**************************************************************************
	 *		메소드명	: executeUpdate
	 *		인자		: query ( SQL 문 )
	 *		리턴형		: void
	 *		설명		: 데이터베이스로 수정을 위한 메소드 
	 *					: (UPDATE,DELETE,INSERT)
	 *************************************************************************/
	public void executeUpdate(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		stmt.executeUpdate(query);
		if(stmt!=null) stmt.close();
		
	}
	
	/**************************************************************************
	 *		메소드명	: execute
	 *		인자		: query ( SQL 문 )
	 *		리턴형		: void
	 *		설명		: 데이터베이스의 일반적인 메소드 
	 *************************************************************************/
	public void execute(String query) throws SQLException 
	{
		this.stmt = conn.createStatement();
		stmt.execute(query);
		if(stmt!=null) stmt.close();
		
	}
	
	/**************************************************************************
	// Transaction 처리하기
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
	// Locking 처리하기
	// Transaction이 종료되면 자동 잠금해제됨.
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
	 *		메소드명	: close
	 *		인자		: 없음
	 *		리턴형		: void
	 *		설명		: 데이터베이스의 커넥션과 관련된 자원을 돌려준다. 
	 *************************************************************************/
	public void close() throws SQLException 
	{
		if(conn!=null) conn.close();
		if(rset!=null) rset.close();
		if(stmt!=null) stmt.close();
	}
	
	/**************************************************************************
	 *		메소드명	: finalize
	 *		인자		: 없음
	 *		리턴형		: void
	 *		설명		: 웹서버 
	 *************************************************************************/
	protected void finalize() throws Throwable
	{	  
		this.close();
	}
	
}
