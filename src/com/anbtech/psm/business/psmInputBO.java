package com.anbtech.psm.business;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.util.StringTokenizer;

public class psmInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//������ ���Ϸ� ���
	private com.anbtech.psm.db.psmModifyDAO psmDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public psmInputBO(Connection con) 
	{
		this.con = con;
		psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		������ȹ(PSM MASTER) �� ���� �޼ҵ� ����
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	*  �������� ���� �����ϱ�
	*******************************************************************/
	public void processStatus(String pid,String psm_status) throws Exception
	{
		String input = "";

		//���� �����ϱ�
		String update = "UPDATE psm_master SET psm_status='"+psm_status+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		//���ڿ��� ���� : �������� ��û�� 
		if(psm_status.equals("11")) sendMail(pid);

		//PSM STATUS�� �Է��ϱ� : ���º������
		if(psm_status.equals("2")) {		//����Ȯ���ø� 
			com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
			masterT = psmDAO.readPsmMaster(pid);

			String reg_date = anbdt.getDateNoformat();
			pid = anbdt.getID();
			input = "INSERT INTO psm_status (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
			input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
			input += "psm_user,psm_user_div,change_desc,psm_status,change_date) values('";
			input += pid+"','"+masterT.getPsmCode()+"','"+masterT.getPsmType()+"','"+masterT.getCompName()+"','";
			input += masterT.getCompCategory()+"','"+masterT.getPsmKorea()+"','";
			input += masterT.getPsmEnglish()+"','"+str.repWord(masterT.getPsmStartDate(),"/","")+"','"+str.repWord(masterT.getPsmEndDate(),"/","")+"','";
			input += masterT.getPsmPm()+"','"+masterT.getPsmPmDiv()+"','";
			input += masterT.getPsmMgr()+"','"+masterT.getPsmMgrDiv()+"','";
			input += masterT.getPsmBudget()+"','"+masterT.getPsmBudgetDiv()+"','";
			input += masterT.getPsmUser()+"','"+masterT.getPsmUserDiv()+"','";
			input += "����Ȯ��"+"','"+psm_status+"','"+reg_date+"')";
			psmDAO.executeUpdate(input);
		}

	}
	/*******************************************************************
	* �����⺻���� ���� �����ϱ� 
	*******************************************************************/
	public String inputPsm(String pid,String psm_type,String comp_name,String comp_category,String psm_korea,String psm_english,
		String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,String psm_budget,String psm_user,String psm_desc,String plan_sum,
		String plan_labor,String plan_material,String plan_cost,String plan_plant,String contract_date,String contract_name,
		String contract_price,String complete_date,String pd_code,String pd_name,String psm_kind,
		String psm_view,String link_code) throws Exception
	{
		String input="",data="",update="",where="";
		String reg_date = anbdt.getDateNoformat();

		//������ں� ����� �ڵ� ���ϱ�
		int pm_div = psm_pm.indexOf("/"); if(pm_div == -1) pm_div = 0;
		String psm_pm_div = psmDAO.getDivCode(psm_pm.substring(0,pm_div));

		int mgr_div = psm_mgr.indexOf("/"); if(mgr_div == -1) mgr_div = 0;
		String psm_mgr_div = psmDAO.getDivCode(psm_mgr.substring(0,mgr_div));

		int budget_div = psm_budget.indexOf("/"); if(budget_div == -1) budget_div = 0;
		String psm_budget_div = psmDAO.getDivCode(psm_budget.substring(0,budget_div));

		int user_div = psm_user.indexOf("/"); if(user_div == -1) user_div = 0;
		String psm_user_div = psmDAO.getDivCode(psm_user.substring(0,user_div));

		//PSM CODE �ڵ�ä�� ���ϱ�
		String psm_code = getPsmCode(comp_category,psm_type,pd_code);			//PSM CODE �ڵ�ä��

		//���ΰ������� ����������� �Ǵ��ϱ� 
		if(link_code.length() != 0) {	//���������.
			psm_view="N";		
		}

		//PSM MASTER�� �Է��ϱ� : ������
		input = "INSERT INTO psm_master (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,psm_desc,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,result_sum,result_labor,result_material,result_cost,result_plant,";
		input += "contract_date,contract_name,contract_price,complete_date,fname,sname,ftype,fsize,";
		input += "psm_status,reg_date,app_date,pd_code,pd_name,psm_kind,psm_view,link_code) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+psm_desc+"','"+Double.parseDouble(plan_sum)+"','";
		input += Double.parseDouble(plan_labor)+"','"+Double.parseDouble(plan_material)+"','";
		input += Double.parseDouble(plan_cost)+"','"+Double.parseDouble(plan_plant)+"','";
		input += "0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
		input += contract_date+"','"+contract_name+"','"+Double.parseDouble(contract_price)+"','";
		input += complete_date+"','"+""+"','"+""+"','"+""+"','"+""+"','"+"1"+"','"+reg_date+"','";
		input += ""+"','"+pd_code+"','"+pd_name+"','"+psm_kind+"','"+psm_view+"','"+link_code+"')";
		psmDAO.executeUpdate(input);

		//���ΰ����� ��������� �������� ��� ���ΰ����� �ݿ��ϱ�
		//���� : link_code�� ������ link_code�� ���ΰ����� ��.
		//���ΰ����� psm_view='VM'��, psm_kind='S'��, link_code=psm_code�� update�Ѵ�.
		if(link_code.length() != 0) {	//���ΰ� ��������� �������°�����
			//������ link_code���� ���Ͽ� ������
			where = "where psm_code = '"+link_code+"'";
			String exist_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			if(exist_link_code.length() != 0) exist_link_code += ","+psm_code; 
			else exist_link_code = psm_code;
			
			//�����ϱ�
			update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+exist_link_code;
			update += "' where psm_code='"+link_code+"'";
			psmDAO.executeUpdate(update);
		}

		//PSM STATUS�� �Է��ϱ� : ���º������
		input = "INSERT INTO psm_status (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,change_desc,psm_status,change_date) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+"�������"+"','"+"1"+"','"+reg_date+"')";
		psmDAO.executeUpdate(input);

		//PSM BUDGET�� �Է��ϱ� : �����������
		input = "INSERT INTO psm_budget (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,change_desc,change_date,budget_type) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+Double.parseDouble(plan_sum)+"','"+Double.parseDouble(plan_labor)+"','";
		input += Double.parseDouble(plan_material)+"','"+Double.parseDouble(plan_cost)+"','"+Double.parseDouble(plan_plant)+"','";
		input += "�������"+"','"+reg_date+"','"+"1"+"')";
		psmDAO.executeUpdate(input);

		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  PSM MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String updatePsm(String pid,String psm_type,String comp_name,String comp_category,String psm_korea,String psm_english,
		String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,String psm_budget,String psm_user,String psm_desc,String plan_sum,
		String plan_labor,String plan_material,String plan_cost,String plan_plant,String contract_date,String contract_name,
		String contract_price,String complete_date,String pd_code,String pd_name,String psm_kind,
		String psm_view,String link_code) throws Exception
	{
		String data="",where="",psm_status="",update="";
		String reg_date = anbdt.getDateNoformat();

		//������� �˻�
		where = "where pid = '"+pid+"'";
		psm_status = psmDAO.getColumData("PSM_MASTER","psm_status",where);
		if(!psm_status.equals("1")) {
			data = "����������϶��� ������ �����մϴ�.";
			return data;
		} 

		//������ں� ����� �ڵ� ���ϱ�
		int pm_div = psm_pm.indexOf("/"); if(pm_div == -1) pm_div = 0;
		String psm_pm_div = psmDAO.getDivCode(psm_pm.substring(0,pm_div));

		int mgr_div = psm_mgr.indexOf("/"); if(mgr_div == -1) mgr_div = 0;
		String psm_mgr_div = psmDAO.getDivCode(psm_mgr.substring(0,mgr_div));

		int budget_div = psm_budget.indexOf("/"); if(budget_div == -1) budget_div = 0;
		String psm_budget_div = psmDAO.getDivCode(psm_budget.substring(0,budget_div));

		int user_div = psm_user.indexOf("/"); if(user_div == -1) user_div = 0;
		String psm_user_div = psmDAO.getDivCode(psm_user.substring(0,user_div));

		//�����з��Ͽ� �ݿ��ϱ� : ������ �����͸� �̿���.
		String[] d = new String[2];
		d = updateMasterSubLinkCode(pid,link_code,psm_kind,psm_view);
		psm_kind = d[0];
		psm_view = d[1];
		if(psm_kind.length() > 2) {		//�Ұ��� ���� ������
			data = psm_kind;
			return data;
		}

		//PSM MASTER �����ϱ�
		update = "UPDATE psm_master SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',psm_desc='"+psm_desc;
		update += "',plan_sum='"+Double.parseDouble(plan_sum)+"',plan_labor='"+Double.parseDouble(plan_labor);
		update += "',plan_material='"+Double.parseDouble(plan_material)+"',plan_cost='"+Double.parseDouble(plan_cost);
		update += "',plan_plant='"+Double.parseDouble(plan_plant)+"',contract_date='"+contract_date;
		update += "',contract_name='"+contract_name+"',contract_price='"+Double.parseDouble(contract_price);
		update += "',reg_date='"+reg_date+"',complete_date='"+complete_date;
		update += "',pd_code='"+pd_code+"',pd_name='"+pd_name+"',psm_kind='"+psm_kind+"',psm_view='"+psm_view;
		update += "',link_code='"+link_code+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);
	
		//PSM STATUS �����ϱ�
		update = "UPDATE psm_status SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+"�������";
		update += "',change_date='"+reg_date+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		//PSM BUDGET �����ϱ�
		update = "UPDATE psm_budget SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+"�������";
		update += "',plan_sum='"+Double.parseDouble(plan_sum)+"',plan_labor='"+Double.parseDouble(plan_labor);
		update += "',plan_material='"+Double.parseDouble(plan_material)+"',plan_cost='"+Double.parseDouble(plan_cost);
		update += "',plan_plant='"+Double.parseDouble(plan_plant);
		update += "',change_date='"+reg_date+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  PSM MASTER�� ������ ����/���� ���� �Ǵ� �����ϱ�
	//*******************************************************************/	
	public String[] updateMasterSubLinkCode(String pid,String link_code,String psm_kind,String psm_view) throws Exception
	{
		String where="",update="",e_link_code="",pjt_kind="",psm_code="";
		String main_link_code="";
		String[] data = new String[2];		//psm_kind, psm_view
		data[0] = psm_kind;
		data[1] = psm_view;

		//�������� �Ǵ��ϱ� (psm_view=V[�ܵ�], VM[����], N[����] ������)
		where = "where pid = '"+pid+"'";
		pjt_kind = psmDAO.getColumData("PSM_MASTER","psm_view",where);
		e_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);	//���� link_code ���ϱ�
		psm_code = psmDAO.getColumData("PSM_MASTER","psm_code",where);		//�����ڵ�

		//---------------------------------------------
		//���������� ������ ����� �ش系�� �ݿ��ϱ�
		//---------------------------------------------
		//1.���� �ܵ������� ���
		if(pjt_kind.equals("V")) {
				//1.���������� �ܵ�
				if(link_code.length() == 0) {
					return data;		
				}
				//2.���ΰ� ����� ��������
				else {
					//���ΰ����� update
					//link_code���� ���Ͽ� ������
					where = "where psm_code = '"+link_code+"'";
					main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
					if(main_link_code.length() != 0) main_link_code += ","+psm_code; 
					else main_link_code = psm_code;
				
					//�����ϱ�
					update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+main_link_code;
					update += "' where psm_code='"+link_code+"'";
					psmDAO.executeUpdate(update);

					//�ش������ ��������� ���ϰ� ���ϱ�
					data[0] = "M";
					data[1] = "N";
				}
		} 
		//2.���� ���ΰ����� ���
		else if(pjt_kind.equals("VM")) {
				return data;
		}
		//3.���� ��������� ���
		else if(pjt_kind.equals("N")) {
				//�ܵ������� ����� ���
				if(link_code.length() == 0) {
						//���ΰ��� �ݿ��ϱ�
						//����link_code���� ���Ͽ� �ش� �ű�link_code�� ����.
						where = "where psm_code = '"+e_link_code+"'";
						main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
						if(main_link_code.length() != 0) {
							main_link_code = str.repWord(main_link_code,psm_code,"");
							//�ʿ���� �޸�(,) ����
							main_link_code = str.repWord(main_link_code,",,",",");	//�߰����� �����
							if(main_link_code.length() != 0) {
								if(main_link_code.substring(0,1).equals(","))			//ó��,�̸� ����
									main_link_code=main_link_code.substring(1,main_link_code.length());
								if(main_link_code.charAt(main_link_code.length()-1) == ','){//����,�̸� ����
									int len = main_link_code.length();
									main_link_code=main_link_code.substring(0,len-1);
								}
							}
						}
						//���ο��� �ܵ����� ����� ���
						if(main_link_code.length() == 0) {
							psm_kind="M";
							psm_view="V";
						} 
						//��Ӹ����̸�
						else {
							psm_kind="S";
							psm_view="VM";
						}
						//�����ϱ�
						update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
						update += "' where psm_code='"+e_link_code+"'";
						psmDAO.executeUpdate(update);
						
						//�ش������ �ܵ������� ������ ���ϰ� ���ϱ�
						data[0] = "M";
						data[1] = "V";
				}
				//������� ������ ���
				else {
						//link_code�� ������ �������
						if(link_code.equals(e_link_code)) {
							return data;
						} 
						//�ٸ����[���ΰ����� �����]
						else {
							//1. ���� ���ΰ��� �ݿ��ϱ�
							//����e_link_code���� ���Ͽ� ���� psm_code ���� update
							where = "where psm_code = '"+e_link_code+"'";
							main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);

							if(main_link_code.length() != 0) {
								main_link_code = str.repWord(main_link_code,psm_code,"");
				
								//�ʿ���� �޸�(,) ����
								main_link_code = str.repWord(main_link_code,",,",",");	//�߰����� �����
								if(main_link_code.length() != 0) {
									if(main_link_code.substring(0,1).equals(","))		//ó��,�̸� ����
										main_link_code=main_link_code.substring(1,main_link_code.length());
									if(main_link_code.charAt(main_link_code.length()-1) == ','){//����,�̸� ����
										int len = main_link_code.length();
										main_link_code=main_link_code.substring(0,len-1);
									}
								}
								//���Ǳ��ϱ�
								if(main_link_code.length() == 0) {
									psm_view="V";
									psm_kind="M";
								}
							}
							update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
							update += "' where psm_code='"+e_link_code+"'";
							psmDAO.executeUpdate(update);

							//2. �ű� ���ΰ��� �ݿ��ϱ�
							//����link_code���� ���Ͽ� �ű� link_code�߰� 
							where = "where psm_code = '"+link_code+"'";
							main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
							if(main_link_code.length() != 0) main_link_code += ","+psm_code; 
						
							//�����ϱ�
							update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+psm_code;
							update += "' where psm_code='"+link_code+"'";
							psmDAO.executeUpdate(update);

						}
				} //if
		} //if
		return data;
	}

	//*******************************************************************
	// PSM MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String deletePsm(String pid,String filepath) throws Exception
	{
		String delete = "",data="",psm_status="",psm_view="",link_code="",psm_code="";
		String main_link_code="",psm_kind="",update="";
		String where = "where pid='"+pid+"'";

		//������� �˻�
		psm_status = psmDAO.getColumData("PSM_MASTER","psm_status",where);
		if(!psm_status.equals("1")) {
			data = "����������϶��� ������ �����մϴ�.";
			return data;
		}
		
		//���ΰ����̸� ����������� ������ ����������
		psm_view = psmDAO.getColumData("PSM_MASTER","psm_view",where);
		if(psm_view.equals("VM")) {
			data = "���ΰ����� ������ �� �����ϴ�. �ش� ����������� ���ΰ����� ������ �����Ͻʽÿ�.";
			return data;
		}

		//��������̸� �ش� ���ΰ����� �ݿ��� ����
		if(psm_view.equals("N")) {
			link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			psm_code = psmDAO.getColumData("PSM_MASTER","psm_code",where);

			//�ش���ΰ����� �ݿ��ϱ�
			where = "where psm_code = '"+link_code+"'";
			main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			if(main_link_code.length() != 0) {
					main_link_code = str.repWord(main_link_code,psm_code,"");

					//�ʿ���� �޸�(,) ����
					main_link_code = str.repWord(main_link_code,",,",",");	//�߰����� �����
					if(main_link_code.substring(0,1).equals(","))			//ó��,�̸� ����
							main_link_code=main_link_code.substring(1,main_link_code.length());
					if(main_link_code.charAt(main_link_code.length()) == ','){//����,�̸� ����
							int len = main_link_code.length();
							main_link_code=main_link_code.substring(0,len-1);
					}
			}
			//���ΰ������� �ܵ������� �ٲ�
			if(main_link_code.length() == 0) {
				psm_view="V";
				psm_kind="M";
			} 
			//���ΰ����� ���ӵɶ�
			else {
				psm_view="VM";
				psm_kind="S";
			}
			update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
			update += "' where psm_code='"+link_code+"'";
			psmDAO.executeUpdate(update);

		}

		//÷������ �����ϱ�
		com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
		masterT = psmDAO.readPsmMaster(pid);
		String sname = masterT.getSname();								//÷������ �����
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}


		//������
		delete = "DELETE FROM psm_master WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		//���º������
		delete = "DELETE FROM psm_status WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		//�����������
		delete = "DELETE FROM psm_budget WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		data = "���������� �����Ǿ����ϴ�.";
		
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		÷�� ���� �����ϱ� 
	//
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* ÷������ �����ϱ� (�űԷ� ó�� ÷���Ҷ�)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String tablename,String pid,String save_id,String filepath) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����

		int i = 1;					//÷������ Ȯ����
		int atcnt = 0;				//÷������ ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "attachfile"+i;		//upload�� input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				filename += fname + "|";
				savename += save_id + "_" + i + "|";
				filetype += ftype + "|";
				filesize += fsize + "|";
				atcnt++;
			}
			i++;
		}//while

		//Table�� �����ϱ�
		if(i > 1) {
			setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* ÷������ �����ϱ� (�ӽ������� �����Ͽ� ÷���Ҷ�)
	* save_head : ������ ������ ���ι���
	* delfile �� ������ ������
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String tablename,String pid,String save_head,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt,String[] chkDelFile) throws Exception
	{
		String save_id = save_head+anbdt.getID();		//�űԷ� ������ ���ϸ�
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����
		int att_cnt = Integer.parseInt(attache_cnt);	//÷������ �ִ���� �̸�
		String newdata = "";
		
		//------------------------------
		//���������� check�� ���� �����ϱ�
		//------------------------------
		int cnt = chkDelFile.length;
		for(int i=0; i<cnt; i++) {
			String dfile = filepath+"/"+chkDelFile[i];
			File FN = new File(dfile);
			if(FN.exists()) FN.delete();	
		}

		//------------------------------
		//������ ���������� �迭�� ���
		//------------------------------
		String[][] fdata = null;
		int flen = fname.length();
		int alen = 0,hi = 0;			//������ϵ� ���ϼ�
		if(flen != 0) {
			for(int i=0; i<flen; i++) if(fname.charAt(i) == '|') alen++;
			fdata = new String[alen][4];

			//�������� �������ϸ�
			fname = fname.substring(0,fname.length());	//������ '|'����
			java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			
			hi = 0;
			while(o_fname.hasMoreTokens()) {
				String read = o_fname.nextToken();
				if(read.length() != 0) fdata[hi][0] = read;
				hi++;
			}
			//�������� �������ϸ�
			sname = sname.substring(0,sname.length());	//������ '|'����
			java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			
			hi = 0;
			while(o_sname.hasMoreTokens()) {
				String read = o_sname.nextToken();
				if(read.length() != 0) fdata[hi][1] = read;
				hi++;
			}
			//�������� ��������Type��
			ftype = ftype.substring(0,ftype.length());	//������ '|'����
			java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			
			hi = 0;
			while(o_ftype.hasMoreTokens()) {
				String read = o_ftype.nextToken();
				if(read.length() != 0) fdata[hi][2] = read;
				hi++;
			}
			//�������� �������� Size
			fsize = fsize.substring(0,fsize.length());	//������ '|'����
			java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			
			hi = 0;
			while(o_fsize.hasMoreTokens()) {
				String read = o_fsize.nextToken();
				if(read.length() != 0) fdata[hi][3] = read;
				hi++;
			}

			//fdata�迭�� ������ ������ ������ �ش�迭�� ���� clear��Ų��.
			for(int i=0; i<cnt; i++) {
				if(chkDelFile[i].length() != 0) fdata[i][0]=fdata[i][1]=fdata[i][2]=fdata[i][3]="";
			}
		}

		//------------------------------
		//�űԷ� ÷���� ����
		//------------------------------
		int i = 1;		//÷������Ȯ����
		int n = 0;		//����迭�� ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();							//�ش����� �б�
			String name = "attachfile"+i;					//upload�� input file type name parameter
			String uname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			
			//��������� ������ ������ �����ϱ�
			if((alen > n) && (uname != null)) {
				String delfile = filepath+"/"+fdata[n][1]+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}

			//÷���� ���� �̸��ٲ� �����ϱ�
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				newdata += uname + "|";
				newdata += save_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
			}
			else newdata += " | | |;";
			i++;
			n++;
		}//while

		//------------------------------
		//DB�� ������ data �����
		//------------------------------
		//�űԷ� ������ ������ �迭�� ���
		String[][] ndata = new String[att_cnt-1][4];
		for(int a=0; a<n; a++) for(int b=0; b<4; b++) ndata[a][b] = "";

		java.util.StringTokenizer rdata = new StringTokenizer(newdata,";");
		int ai = 0;
		while(rdata.hasMoreTokens()) {
			String nnd = rdata.nextToken();		//1���� �б�
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				ndata[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		

		//������ ������ ������
		int atcnt = 0;			//÷������ ���� ���ϰ�
		for(int p=0; p<att_cnt-1; p++) {
			//����÷���� ���븸 ������
			if((n >= p) && (ndata[p][0].length() > 1)) {				
				filename += ndata[p][0] + "|";
				savename += ndata[p][1] + "|";
				filetype += ndata[p][2] + "|";
				filesize += ndata[p][3] + "|"; 
				atcnt++;
			} 
			//�űԷ� ÷���� ������ �����鼭 ������ϵ� ������ ������
			else if((alen > p) && (ndata[p][0].length() <= 1)) {		
				if(fdata[p][0].length() != 0) {		//������ ������ ����
					filename += fdata[p][0] + "|";
					savename += fdata[p][1] + "|";
					filetype += fdata[p][2] + "|";
					filesize += fdata[p][3] + "|"; 
					atcnt++;
				}
			} 
		}

		//------------------------------
		//Table�� �����ϱ�
		//------------------------------
		setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		
		return atcnt;

	}
	/*******************************************************************
	* ÷������ �������� Table�� update�ϱ�
	 *******************************************************************/
	 private void setAddFileUpdate(String tablename,String pid, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update "+tablename+" set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where pid='"+pid+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� �ۼ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// PSM CODE �ڵ� ä�� �����ϱ�
	//*******************************************************************/	
	public String getPsmCode(String comp_category,String psm_type,String pd_code) throws Exception
	{
		String psm_code = "",where="",head="T";
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("00");

		//��������[�������:T,���İ���:��ǰ�ڵ�]�� ���� ���ι��ڸ� �����Ѵ�.
		where = "where env_name like '"+psm_type+"%'";
		String pjt_kind = psmDAO.getColumData("psm_env","env_status",where);
		if(pjt_kind.equals("2")) head=pd_code;

		//�˻��� String����� : PSM CODE : HEADERCHAR + YY + '-' + ī�װ���� + nn 
		String yy = anbdt.getYear();
		where = "where korea_name='"+comp_category+"'";
		String key_word = psmDAO.getColumData("psm_category","key_word",where);
		String psm_body = head+yy.substring(2,4)+"-"+key_word;

		//��ϵ� PSM CODE�� ���Ѵ�.
		where = "where psm_code like '"+psm_body+"%' order by psm_code desc";
		String was_psm_code = psmDAO.getColumData("psm_master","psm_code",where);

		//���� psm_code ���� �Ϸù�ȣ�� +1�� �Ѵ�.
		if(was_psm_code.length() == 0) {
			psm_code = psm_body + "01";
		} else {
			String serial_no = was_psm_code.substring(was_psm_code.length()-2,was_psm_code.length());
			int s_no = Integer.parseInt(serial_no)+1;
			psm_code = psm_body + nmf.toDigits(s_no);
		}

		return psm_code;
	}

	/***************************************************************************
	 * ����ڿ��� ���ڿ������� �˷��ֱ�
	 * pid : ������ȣ 
	 **************************************************************************/
	private void sendMail(String pid) throws Exception  
	{
		String share_id="";				//share_id (������) : ���/�̸�;
		String post_bon_path="",w_date="",delete_date="",filename="",mquery="",lquery="",subject="";
		String psm_code="",psm_type="",comp_name="",comp_cate="",psm_per="";
		String mgr_id = "",mgr_name ="";			//�����»�� : ���, �̸�

		//-------------------------------------------------------
		// ���ó��� �����
		//-------------------------------------------------------
		com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
		masterT = psmDAO.readPsmMaster(pid);

		String psm_user = masterT.getPsmUser();					//���/�̸�
		int sn = psm_user.indexOf("/");
		mgr_id = psm_user.substring(0,sn);						//�ۼ��� ��� (���ڿ��� ������ ���)
		mgr_name = psm_user.substring(sn+1,psm_user.length());	//�ۼ��� �̸�

		share_id = masterT.getPsmMgr()+";";						//������
		subject = " ����Ȯ����û: "+masterT.getPsmKorea();		//����
		post_bon_path = "/post/"+mgr_id+"/text_upload";			//post�� ������ ����path
		pid = anbdt.getID();									//���ڿ��� ���� ������ȣ
		w_date = anbdt.getTime();								//�ۼ���
		delete_date = anbdt.getAddMonthNoformat(1);				//����������
		filename = pid;											//�����������ϸ�
		psm_code = masterT.getPsmCode();						//�����ڵ�
		psm_type = masterT.getPsmType();						//��������
		comp_name = masterT.getCompName();						//������
		comp_cate = masterT.getCompCategory();					//����ī�װ�
		psm_per = masterT.getPsmStartDate()+" ~ "+masterT.getPsmEndDate();	//�����Ⱓ

		//-------------------------------------------------------
		//���ó��� post tabel(post_master,post_letter)�� ��� 
		//-------------------------------------------------------
		mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
		mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		mquery += pid+"','"+subject+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+share_id+"','";
		mquery += "0"+"','"+"email"+"','"+"CFM"+"','"+post_bon_path+"','"+filename+"','"+delete_date+"')";
		psmDAO.executeUpdate(mquery);


		lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
		lquery += "post_receiver,isopen,post_select,delete_date) values('";
		String receivers = share_id;		//����������� ã�� �Է��ϱ�
		StringTokenizer dd = new StringTokenizer(receivers,";");
		while(dd.hasMoreTokens()) {
			String rd = dd.nextToken();		rd=rd.trim();		//���/�̸�
			if(rd.length() > 5) {
				String sabun = rd.substring(0,rd.indexOf("/"));
				String lq = lquery + pid+"','"+subject+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+sabun+"','";
				lq += "0"+"','"+"CFM"+"','"+delete_date+"')";
				psmDAO.executeUpdate(lq);
			}
		}
		
		//-------------------------------------------------------
		//�������� �����
		//-------------------------------------------------------
		String upload_path = "";
		upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");	//servlet path
		// ���� �������� �����
		String content = "<html><head><title>����Ȯ����û</title></head>";
		content += "<body>";
		content += "����Ȯ�� ��û �����Դϴ�.<br>";
		content += "�󼼳����� ������⿡�� Ȯ���Ͻð� ����Ȯ�� ��û�մϴ�.<br>";
		content += "<br><br><br>";
		content += "������ȣ : "+psm_code+"<br>";
		content += "�������� : "+psm_type+"<br>";
		content += "������ : "+comp_name+"<br>";
		content += "����ī�װ� : "+comp_cate+"<br>";
		content += "�����Ⱓ : "+psm_per+"<br>";
		content += "</body></html>";

		// ���ڿ���� �������� ���ϸ����
		String path = upload_path + "/gw/mail" + post_bon_path;					//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

	}
}
