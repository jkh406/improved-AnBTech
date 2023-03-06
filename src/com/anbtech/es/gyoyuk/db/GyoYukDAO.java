package com.anbtech.es.gyoyuk.db;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class GyoYukDAO{
	private Connection con;
	private com.anbtech.file.FileWriteString text;					//��������
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó���ϱ�
	
	/*******************************************************************
	 * ������
	 *******************************************************************/
	public GyoYukDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* �������� ������ ���� �����ϱ� 
	*******************************************************************/
	public void setGyoyukiljiMasterTable(String gy_id,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String in_date,
		String e_year,String e_month,String e_date,String lecturer_id,String lecturer_name,String major_kind,String place,String part_cnt,String edu_subject,
		String antiprt_prs,String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		place = str.repWord(place,"'","`");					//���
		edu_subject = str.repWord(edu_subject,"'","`");		//������
		content = str.repWord(content,"'","`");				//����

		//����������丮�� ���ϸ�
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
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,gy_id,content);
		}
	}

	/*******************************************************************
	* �������� ���� ���� �����ϱ� 
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
	* ������ ���Ϸ� �����ϱ�
	* root_path : root Path, doc_pat : Ȯ�� path, content : ��������
	 *******************************************************************/
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}
}