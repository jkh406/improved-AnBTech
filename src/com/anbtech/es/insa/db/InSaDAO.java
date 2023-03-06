package com.anbtech.es.insa.db;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class InSaDAO{
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리하기
	
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public InSaDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* 구인의뢰서 내용 저장하기 
	*******************************************************************/
	public void setGuinTable(String is_id,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String in_date,
		String job_kind,String job_content,String career,String major,String req_qualify,String status,String job_career,String job_etc,String req_count,String marray,
		String army,String employ,String employ_per,String language_grade,String language_exam,String language_score,String comp_grade,String comp_etc,String papers,String note) throws Exception
	{
		//' 없애기
		job_kind = str.repWord(job_kind,"'","`");				//모집직종
		job_content = str.repWord(job_content,"'","`");			//업무내용
		career = str.repWord(career,"'","`");					//학력
		req_qualify = str.repWord(req_qualify,"'","`");			//필요자격증
		job_career = str.repWord(job_career,"'","`");			//요구경력
		note = str.repWord(note,"'","`");						//요구사항

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

