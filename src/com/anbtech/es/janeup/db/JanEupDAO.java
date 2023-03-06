package com.anbtech.es.janeup.db;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class JanEupDAO{
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public JanEupDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* ����ٹ���û�� ������ ���� �����ϱ� 
	*******************************************************************/
	public void setYeonjangMasterTable(String ju_id,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String in_date,
		String j_year,String j_month,String j_date,String job_kind,String cost_prs) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO JANEUP_MASTER(ju_id,ys_kind,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,in_date,";
			incommon += "j_year,j_month,j_date,job_kind,cost_prs) values('";
		
		String input = incommon+ju_id+"','YEONJANG','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','"+ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','";
			input += j_year+"','"+j_month+"','"+j_date+"','"+job_kind+"','"+cost_prs+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* ����ٹ���û�� worker ���� �����ϱ� 
	*******************************************************************/
	public void setYeonjangWorkerTable(String ju_id,String ju_cid,String j_year,String j_month,String j_date,
		String worker_id,String worker_name,String content,String close_time,String cfm,String job_kind) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO JANEUP_WORKER(ju_id,ju_cid,j_year,j_month,j_date,";
			incommon += "worker_id,worker_name,content,close_time,cfm,job_kind) values('";
		
		String input = incommon+ju_id+"','"+ju_cid+"','"+j_year+"','"+j_month+"','"+j_date+"','";
			input += worker_id+"','"+worker_name+"','"+content+"','"+close_time+"','"+cfm+"','"+job_kind+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

}
