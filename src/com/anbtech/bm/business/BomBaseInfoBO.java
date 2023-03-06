package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomBaseInfoBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomBaseInfoBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER �� ���� �޼ҵ� ���� : �⺻����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_MASTER�� ������ �Է��ϱ�
	//	#default�� MBOM_STR���� �Է��Ѵ�.
	//*******************************************************************/	
	public String insertMaster(String pid,String modelg_code,String modelg_name,String model_code,String model_name,
		String fg_code,String fg_spec,String pdg_code,String pdg_name,String pd_code,String pd_name,
		String pjt_code,String pjt_name,String reg_id,String reg_name,String reg_date,String purpose) throws Exception
	{
		String input="",data="";

		//�ߺ��˻� : FG�ڵ�� �ߺ��˻�����
		query = "SELECT COUNT(*) from MBOM_MASTER where fg_code ='"+fg_code+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt != 0) {
			data = "�̹� FG�ڵ尡 ��ϵǾ� �־� �ߺ������ �� �����ϴ�.";
			////System.out.println(data);
			return data;
		}
		
		//MBOM_MASTER�� �Է��ϱ�
		input = "INSERT INTO MBOM_MASTER (pid,modelg_code,modelg_name,model_code,model_name,fg_code,pdg_code,pdg_name,pd_code,pd_name,pjt_code,";
		input += "pjt_name,reg_id,reg_name,reg_date,app_id,app_name,app_date,bom_status,app_no,m_status,purpose) values('";
		input += pid+"','"+modelg_code+"','"+modelg_name+"','"+model_code+"','"+model_name+"','"+fg_code+"','"+pdg_code+"','"+pdg_name+"','"+pd_code+"','"+pd_name+"','";
		input += pjt_code+"','"+pjt_name+"','"+reg_id+"','"+reg_name+"','"+reg_date+"','"+""+"','"+""+"','";
		input += ""+"','"+"1"+"','"+""+"','"+"1"+"','"+purpose+"')";
		modDAO.executeUpdate(input);

		//��ǰ���� ã�� : [0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type] 
		String[] part = new String[6];
		part = modDAO.getComponentInfo(fg_code);

		//MBOM_STR�� �Է��ϱ�
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
		input += anbdt.getID()+"','"+pid+"','"+model_code+"','"+fg_code+"','"+"0"+"','"+part[0]+"','"+part[1]+"','";
		input += ""+"','"+""+"','"+part[4]+"','"+"1"+"','"+part[2]+"','"+part[3]+"','"+"��"+"','"+"0"+"','"+reg_date+"','";
		input += ""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
		modDAO.executeUpdate(input);

		//MBOM_GRADE_MGR�� �Է��ϱ� : BOM Group����
		String div_code = "";
		div_code = modDAO.getDivCode(reg_id);

		input = "INSERT INTO mbom_grade_mgr(pid,keyname,owner,div_code) values('";
		input += anbdt.getID()+"','"+fg_code+"','"+reg_id+"/"+reg_name+";"+"','"+div_code+"')";
		modDAO.executeUpdate(input);
		
		data = "���������� ��ϵǾ����ϴ�.";
		////System.out.println(data);
		return data;
	}

	//*******************************************************************
	// MBOM_MASTER�� ������ �����ϱ�
	//	#default�� MBOM_STR���� �����Ѵ�.
	//*******************************************************************/	
	public String updateMaster(String pid,String modelg_code,String modelg_name,String model_code,String model_name,String fg_code,
		String fg_spec,String pdg_code,String pdg_name,String pd_code,String pd_name,String pjt_code,String pjt_name,
		String reg_date,String purpose) throws Exception
	{
		String update="",data="",where="";

		//���������� ���¸� �˻��Ѵ�. �ʱ��ϻ��¸� ������
		String bom_status = modDAO.getColumData("MBOM_MASTER","bom_status","where pid ='"+pid+"'");
		if(bom_status.equals("4")) {
			data = "BOM�������� ������ ���� �� �� �����ϴ�.";
			return data;
		} else if(bom_status.equals("5")) {
			data = "BOM���� ������ ���� �� �� �����ϴ�.";
			return data;
		} 

		//MBOM_GRADE_MGR�� ����[fg_code��]�ϱ� : BOM Group����
		where = "where pid='"+pid+"'";
		String was_fg_code = modDAO.getColumData("MBOM_MASTER","fg_code",where);
		update = "UPDATE mbom_grade_mgr SET keyname='"+fg_code+"' where keyname='"+was_fg_code+"'";
		modDAO.executeUpdate(update);
		
		//MBOM_MASTER�� �����ϱ�
		update = "UPDATE MBOM_MASTER set model_code='"+model_code+"',model_name='"+model_name;
		update += "',fg_code='"+fg_code+"',pdg_code='"+pdg_code+"',pdg_name='"+pdg_name;
		update += "',modelg_code='"+modelg_code+"',modelg_name='"+modelg_name;
		update += "',pd_code='"+pd_code+"',pd_name='"+pd_name;
		update += "',pjt_code='"+pjt_code+"',pjt_name='"+pjt_name;
		update += "',reg_date='"+reg_date+"',purpose='"+purpose+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//��ǰ���� ã�� : [0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type] 
		String[] part = new String[6];
		part = modDAO.getComponentInfo(fg_code);

		//MBOM_STR�� �����ϱ� : �ݷ��� ������ �־� �ۼ����϶��� ������
		if(bom_status.equals("1")) {
			update = "UPDATE MBOM_STR set parent_code='"+model_code+"',child_code='"+fg_code+"',part_spec='"+part[1];
			update += "',maker_name='"+part[2]+"',qty_unit='"+part[4]+"',item_type='"+part[5];
			update += "' where gid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

		//MBOM_MASTER�� ���� �ݷ�����='0'�̸� �ۼ�����='3'�� �ٲ��ֱ�
		if(bom_status.equals("0")) {
			update = "UPDATE MBOM_MASTER set bom_status='3' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

		data = "���������� �����Ǿ����ϴ�.";
		////System.out.println(data);
		return data;
	}

	//*******************************************************************
	// MBOM_MASTER�� ������ �����ϱ�
	//	#default�� MBOM_STR���� �����Ѵ�.
	//*******************************************************************/	
	public String deleteMaster(String pid) throws Exception
	{
		String delete = "",data="",where="";
		String query = "SELECT COUNT(*) FROM MBOM_STR where gid='"+pid+"'";
		
		//MBOM_STR�� ��ϵ� ��ǰ�� ������ ������� ������ �����Ͽ� ������ �ʵ�
		if(modDAO.getTotalCount(query) > 1) {	
			data = "��� or �������� BOM���� ������ �� �����ϴ�.";
			return data;
		}

		//MBOM_GRADE_MGR�� ����[fg_code��]�ϱ� : BOM Group����
		where = "where pid='"+pid+"'";
		String was_fg_code = modDAO.getColumData("MBOM_MASTER","fg_code",where);
		delete = "DELETE from mbom_grade_mgr where keyname='"+was_fg_code+"' ";
		modDAO.executeUpdate(delete);
		
		//MBOM_MASTER �����ϱ�
		delete = "DELETE from MBOM_MASTER where pid='"+pid+"' ";
		modDAO.executeUpdate(delete);

		//MBOM_STR �����ϱ�
		delete = "DELETE from MBOM_STR where gid='"+pid+"' ";
		modDAO.executeUpdate(delete);

		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}

}