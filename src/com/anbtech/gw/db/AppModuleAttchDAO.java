package com.anbtech.gw.db;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.dbconn.DBConnectionManager;		

public class AppModuleAttchDAO
{
	// Database Wrapper Class 선언
	private DBConnectionManager connMgr;
	private Connection con;

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public AppModuleAttchDAO(Connection con) 
	{	
		this.con = con;
	}

	/******************************************************************************
		기술문서 첨부파일 수량 파악하기
	******************************************************************************/
	public int searchAttchCntTD(String mid) throws SQLException 
	{
		String afile = "";	//첨부파일 명 (구분 : | )
		int cnt = 0;		//첨부파일 수량

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//해당문서번호의 해당자 부서코드 구하기
		String query = "select fname from techdoc_data where ancestor='"+mid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) afile += rs.getString("fname");
		
		//첨부파일 수량 파악하기
		StringTokenizer adata = new StringTokenizer(afile,"|");	
		while(adata.hasMoreTokens()) {
			adata.nextToken();
			cnt++;
		} //while

		stmt.close();
		rs.close();
		return cnt;
	}

	/******************************************************************************
		승인원 첨부파일 수량 파악하기
	******************************************************************************/
	public int searchAttchCntAKG(String mid) throws SQLException 
	{
		String afile = "";	//첨부파일 명 (구분 : | )
		int cnt = 0;		//첨부파일 수량

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//해당문서번호의 해당자 부서코드 구하기
		String query = "select fname from ca_master where tmp_approval_no='"+mid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) afile += rs.getString("fname");
		
		//첨부파일 수량 파악하기
		StringTokenizer adata = new StringTokenizer(afile,"|");	
		while(adata.hasMoreTokens()) {
			adata.nextToken();
			cnt++;
		} //while

		stmt.close();
		rs.close();
		return cnt;
	}

	/******************************************************************************
		고객관리 첨부파일 수량 파악하기
	******************************************************************************/
	public int searchAttchCntSERVICE(String mid) throws SQLException 
	{
		String afile = "";	//첨부파일 명 (구분 : 없음)
		int cnt = 0;		//첨부파일 수량

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		StringTokenizer sid = new StringTokenizer(mid,",");
		while(sid.hasMoreTokens()) {
			String tid = sid.nextToken();

			//해당문서번호의 해당자 부서코드 구하기
			String query = "select file_name from history_table where ah_id='"+tid+"'";
			rs = stmt.executeQuery(query);
			while(rs.next()) afile = rs.getString("file_name");
			
			//첨부파일 수량 파악하기
			StringTokenizer adata = new StringTokenizer(afile,"|");	
			while(adata.hasMoreTokens()) {
				String dd = adata.nextToken();
				if(!dd.equals("null"))	cnt++;
			} //while
		}

		stmt.close();
		rs.close();
		return cnt;
	}

}