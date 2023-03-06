package com.anbtech;
import java.sql.*;
import com.anbtech.dbconn.DbConnection;

public class ViewQueryBean
{

	DbConnection dc = null;
	ResultSet rset = null;

	//-------------------------------------------------------------------------
	// ������
	//-------------------------------------------------------------------------
	public ViewQueryBean()
	{
		dc = new DbConnection();
	}

	//-------------------------------------------------------------------------
	// Ŀ�ؼ��� ������
	//-------------------------------------------------------------------------
	public boolean openConnection() 
	{
		return dc.openConnection();
	}

	//-------------------------------------------------------------------------
	// SQL���� ���� (SELECT)
	//-------------------------------------------------------------------------
	public void executeQuery(String query) throws SQLException 
	{
		
		this.rset = dc.executeQuery(query);
		
	}

	//-------------------------------------------------------------------------
	// SQL���� ���� (INSERT, DELETE, UPDATE)
	//-------------------------------------------------------------------------
	public void executeUpdate(String query) throws SQLException 
	{
		dc.executeUpdate(query);
	}

	//-------------------------------------------------------------------------
	// SQL���� ���� (All sql statement)
	//-------------------------------------------------------------------------
	public void execute(String query) throws SQLException 
	{
		dc.execute(query);
	}
	/**************************************************************************
	// Transaction ó���ϱ�
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
	// Locking ó���ϱ�
	// Transaction�� ����Ǹ� �ڵ� ���������.
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
	// ��ü �÷��� ������ ����.
	//-------------------------------------------------------------------------
	public int getColumnCount() throws SQLException 
	{
		ResultSetMetaData rsmd = rset.getMetaData();
		return rsmd.getColumnCount();
	}

	//-------------------------------------------------------------------------
	// �ε����� �÷����� ����
	//-------------------------------------------------------------------------
	public String getColumnName(int index) throws SQLException 
	{
		ResultSetMetaData rsmd = rset.getMetaData();
		return rsmd.getColumnName(index);
	}

	//-------------------------------------------------------------------------
	// �÷� �ε����� �����͸� ����
	//-------------------------------------------------------------------------
	public String getData(int index) throws SQLException 
	{
		return rset.getString(index);
                // return rset.getString(index).trim();
	}

	//-------------------------------------------------------------------------
	// �÷������� �����͸� ����
	//-------------------------------------------------------------------------
	public String getData(String columnName) throws SQLException 
	{
		return rset.getString(columnName);
                // return rset.getString(columnName).trim();
	}

	//-------------------------------------------------------------------------
	// ResultSet �� �����͸� �������� �̵���Ŵ
	//-------------------------------------------------------------------------
	public boolean next() throws SQLException 
	{
		return rset.next();
	}

	//-------------------------------------------------------------------------
	// ��ü ����
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
