package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mpsInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mpsInputBO(Connection con) 
	{
		this.con = con;
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		�����ȹ(MPS MASTER) �� ���� �޼ҵ� ����
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MPS MASTER�� ������ �Է��ϱ�
	//*******************************************************************/	
	public String insertMps(String order_no,String mps_type,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String plan_date,String plan_count,
		String item_unit,String factory_no,String factory_name,String reg_date,String reg_id,
		String reg_name,String order_comp) throws Exception
	{
		String input="",data="",where="",bom_status="",gid="",mps_no="",purpose="";
		
		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		purpose = mpsDAO.getColumData("MBOM_MASTER","purpose",where);			//BOM��������
		if(!bom_status.equals("5")) {
			data = "BOM������ �ȵ� ���Դϴ�. ���� BOM�� ������ �����ȹ�� �����Ͻʽÿ�.";
			return data;
		}

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "ǰ���ȣ�� FG�ڵ�� ������ BOM���� ��ǰ �Ǵ� ����ǰ�� ����� �� �ֽ��ϴ�.";
			return data;
		}

		//MPS������ȣ ���ϱ�
		mps_no = mpsDAO.getMpsNo(factory_no);
		
		//MPS MASTER�� �Է��ϱ� 
		String pid = anbdt.getID();
		input = "INSERT INTO mps_master (pid,mps_no,order_no,mps_type,model_code,model_name,fg_code,";
		input += "item_code,item_name,item_spec,plan_date,plan_count,sell_count,item_unit,mps_status,factory_no,";
		input += "factory_name,reg_date,reg_id,reg_name,app_date,app_id,app_no,order_comp,purpose) values('";
		input += pid+"','"+mps_no+"','"+order_no+"','"+mps_type+"','"+model_code+"','"+model_name+"','";
		input += fg_code+"','"+item_code+"','"+item_name+"','"+item_spec+"','"+plan_date+"','"+plan_count+"','"+"0"+"','";
		input += item_unit+"','"+"1"+"','"+factory_no+"','"+factory_name+"','"+reg_date+"','"+reg_id+"','";
		input += reg_name+"','"+""+"','"+""+"','"+""+"','"+order_comp+"','"+purpose+"')";
		//System.out.println("input : " + input);
		mpsDAO.executeUpdate(input);
		
		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  MPS MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String updateMps(String pid,String order_no,String mps_type,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String plan_date,String plan_count,
		String item_unit,String factory_no,String factory_name,String reg_date,String order_comp) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="",purpose="";

		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		purpose = mpsDAO.getColumData("MBOM_MASTER","purpose",where);			//BOM��������
		if(!bom_status.equals("5")) {
			data = "BOM������ �ȵ� ���Դϴ�. ���� BOM�� ������ �����ȹ�� �����Ͻʽÿ�.";
			return data;
		}

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "ǰ���ȣ�� FG�ڵ�� ������ BOM���� ��ǰ �Ǵ� ����ǰ�� ����� �� �ֽ��ϴ�.";
			return data;
		}

		//������°� �ۼ����϶��� ������ ������
		where = "where pid='"+pid+"'";
		bom_status = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		if(!bom_status.equals("1")) {
			data = "�ۼ����϶��� ������ �����մϴ�.";
			return data;
		}
		
		//MBOM_STR�� �����ϱ�
		update = "UPDATE mps_master SET order_no='"+order_no+"',mps_type='"+mps_type;
		update += "',model_code='"+model_code+"',model_name='"+model_name+"',fg_code='"+fg_code;
		update += "',item_code='"+item_code+"',item_name='"+item_name+"',item_spec='"+item_spec+"',plan_date='"+plan_date;
		update += "',plan_count='"+plan_count+"',item_unit='"+item_unit+"',factory_no='"+factory_no;
		update += "',factory_name='"+factory_name+"',reg_date='"+reg_date;
		update += "',order_comp='"+order_comp+"',purpose='"+purpose+"' where pid='"+pid+"'";
		mpsDAO.executeUpdate(update);
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// MPS MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String deleteMps(String pid) throws Exception
	{
		String delete = "",data="",mps_status="";
		String where = "where pid='"+pid+"'";

		//������� �˻�
		mps_status = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		if(!mps_status.equals("1")) {
			data = "�ۼ������϶��� ������ �����մϴ�.";
			return data;
		} 

		delete = "DELETE FROM mps_master WHERE pid='"+pid+"'";
		mpsDAO.executeUpdate(delete);
		data = "���������� �����Ǿ����ϴ�.";
		
		return data;
	}

	//*******************************************************************
	//  MPS MASTER�� ���°����ϱ�
	//*******************************************************************/	
	public String setMpsStatus(String pid,String mps_status,String login_id,String login_name) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="";

		//MPS MASTER�� �����ϱ�
		update = "UPDATE mps_master SET mps_status='"+mps_status+"' where pid='"+pid+"'";
		mpsDAO.executeUpdate(update);

		if(mps_status.equals("3")) {
			update = "UPDATE mps_master SET app_date='"+anbdt.getDateNoformat()+"', ";
			update += "app_id='"+login_id+"/"+login_name+"' where pid='"+pid+"'";
			mpsDAO.executeUpdate(update);
			data = "���������� MPSȮ�� �Ǿ����ϴ�.";
		} else if(mps_status.equals("2")) {
			data = "���������� MPSȮ����û �Ǿ����ϴ�.";
		} else if(mps_status.equals("1")) { 
			data = "���������� MPSȮ����� �Ǿ����ϴ�.";
		}
		return data;
	}
}

