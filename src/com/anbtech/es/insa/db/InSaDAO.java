package com.anbtech.es.insa.db;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class InSaDAO{
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó���ϱ�
	
	private Connection con;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public InSaDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* �����Ƿڼ� ���� �����ϱ� 
	*******************************************************************/
	public void setGuinTable(String is_id,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String in_date,
		String job_kind,String job_content,String career,String major,String req_qualify,String status,String job_career,String job_etc,String req_count,String marray,
		String army,String employ,String employ_per,String language_grade,String language_exam,String language_score,String comp_grade,String comp_etc,String papers,String note) throws Exception
	{
		//' ���ֱ�
		job_kind = str.repWord(job_kind,"'","`");				//��������
		job_content = str.repWord(job_content,"'","`");			//��������
		career = str.repWord(career,"'","`");					//�з�
		req_qualify = str.repWord(req_qualify,"'","`");			//�ʿ��ڰ���
		job_career = str.repWord(job_career,"'","`");			//�䱸���
		note = str.repWord(note,"'","`");						//�䱸����

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO INSA_MASTER(is_id,ys_kind,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,in_date,";
			incommon += "job_kind,job_content,career,major,req_qualify,status,job_career,job_etc,req_count,marray,";
			incommon += "army,employ,employ_per,language_grade,language_exam,language_score,comp_grade,comp_etc,papers,note) values('";
		String input = incommon+is_id+"','GUIN','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','"+ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','";
			input += job_kind+"','"+job_content+"','"+career+"','"+major+"','"+req_qualify+"','"+status+"','"+job_career+"','"+job_etc+"','"+req_count+"','"+marray+"','";
			input += army+"','"+employ+"','"+employ_per+"','"+language_grade+"','"+language_exam+"','"+language_score+"','"+comp_grade+"','"+comp_etc+"','"+papers+"','"+note+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}
}

