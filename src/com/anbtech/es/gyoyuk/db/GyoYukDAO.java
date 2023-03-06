package com.anbtech.es.gyoyuk.db;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class GyoYukDAO{
	private Connection con;
	private com.anbtech.file.FileWriteString text;					//파일저장
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리하기
	
	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public GyoYukDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* 교육일지 마스터 내용 저장하기 
	*******************************************************************/
	public void setGyoyukiljiMasterTable(String gy_id,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String in_date,
		String e_year,String e_month,String e_date,String lecturer_id,String lecturer_name,String major_kind,String place,String part_cnt,String edu_subject,
		String antiprt_prs,String content,String upload_path) throws Exception
	{
		//' 없애기
		place = str.repWord(place,"'","`");					//장소
		edu_subject = str.repWord(edu_subject,"'","`");		//교육명
		content = str.repWord(content,"'","`");				//내용

		//본문저장디렉토리및 파일명
		String root_path = upload_path;
		String doc_pat = "/es/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GYOYUK_MASTER(gy_id,ys_kind,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,in_date,";
			incommon += "e_year,e_month,e_date,lecturer_id,lecturer_name,major_kind,place,part_cnt,edu_subject,";
			incommon += "antiprt_prs,bon_path,bon_file) values('";
		
		String input = incommon+gy_id+"','GYOYUK_ILJI','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','"+ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','";
			input += e_year+"','"+e_month+"','"+e_date+"','"+lecturer_id+"','"+lecturer_name+"','"+major_kind+"','"+place+"','"+part_cnt+"','"+edu_subject+"','";
			input += antiprt_prs+"','"+doc_pat+"','"+gy_id+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//저장1. 본문파일로 저장하기
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,gy_id,content);
		}
	}

	/*******************************************************************
	* 교육일지 개별 내용 저장하기 
	*******************************************************************/
	public void setGyoyukiljiSubTable(String gy_id,String gy_cid,String e_year,String e_month,String e_date,
		String major_kind,String major_div,String edu_subject,String participator_id,String participator_name,String prt_etc) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GYOYUK_PART(gy_id,gy_cid,e_year,e_month,e_date,";
			incommon += "major_kind,major_div,edu_subject,participator_id,participator_name,prt_etc) values('";
		
		String input = incommon+gy_id+"','"+gy_cid+"','"+e_year+"','"+e_month+"','"+e_date+"','";
			input += major_kind+"','"+major_div+"','"+edu_subject+"','"+participator_id+"','"+participator_name+"','"+prt_etc+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*******************************************************************
	* 본문을 파일로 저장하기
	* root_path : root Path, doc_pat : 확장 path, content : 본문내용
	 *******************************************************************/
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}
}