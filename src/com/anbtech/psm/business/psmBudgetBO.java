package com.anbtech.psm.business;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.util.StringTokenizer;

public class psmBudgetBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.psm.db.psmModifyDAO psmDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public psmBudgetBO(Connection con) 
	{
		this.con = con;
		psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		��������(PSM BUDGET) �� ���� �޼ҵ� ����
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	* ��������������� ���� �����ϱ� 
	*******************************************************************/
	public String inputPsmBudget(String pid,String psm_code,String psm_type,String comp_name,String comp_category,
		String psm_korea,String psm_english,String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,
		String psm_budget,String psm_user,String plan_sum,String plan_labor,String plan_material,
		String plan_cost,String plan_plant,String change_desc,String budget_type) throws Exception
	{
		String input="",data="",update="";
		String change_date = anbdt.getDateNoformat();
		double sum=0.0,labor=0.0,material=0.0,cost=0.0,plant=0.0;

		//���� �Է��� ������ �ִ��� �Ǵ��ϱ�
		query = "select count(*) from psm_budget ";
		query += "where psm_code='"+psm_code+"' and change_date='"+change_date+"'";
		int cnt = psmDAO.getTotalCount(query);
		if(cnt != 0) {
			data = "���� ���꺯�泻���� ��ϵǾ��ֽ��ϴ�.";
			return data;
		} 

		//����PSM MASTER�� �߰����� ���ϱ�
		double[] budget = new double[5];
		budget = psmDAO.readPsmMasterBudget(psm_code,budget_type);

		if(budget_type.equals("1")) {					//�����߰�
			sum = budget[0] + Double.parseDouble(plan_sum);
			labor = budget[1] + Double.parseDouble(plan_labor);
			material = budget[2] + Double.parseDouble(plan_material);
			cost = budget[3] + Double.parseDouble(plan_cost);
			plant = budget[4] + Double.parseDouble(plan_plant);
		} else {										//����谨
			sum = budget[0] + Double.parseDouble(plan_sum)*-1;
			labor = budget[1] + Double.parseDouble(plan_labor)*-1;
			material = budget[2] + Double.parseDouble(plan_material)*-1;
			cost = budget[3] + Double.parseDouble(plan_cost)*-1;
			plant = budget[4] + Double.parseDouble(plan_plant)*-1;
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

		//PSM BUDGET�� �Է��ϱ� : ���꺯�����
		input = "INSERT INTO psm_budget (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,change_desc,change_date,budget_type) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+Double.parseDouble(plan_sum)+"','"+Double.parseDouble(plan_labor)+"','";
		input += Double.parseDouble(plan_material)+"','"+Double.parseDouble(plan_cost)+"','"+Double.parseDouble(plan_plant)+"','";
		input += change_desc+"','"+change_date+"','"+budget_type+"')";
		psmDAO.executeUpdate(input);

		//PSM MASTER�� ���泻�� UPdate�ϱ�
		update = "UPDATE psm_master SET plan_sum='"+sum+"',plan_labor='"+labor;
		update += "',plan_material='"+material+"',plan_cost='"+cost+"',plan_plant='"+plant;
		update += "',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div+"' where psm_code='"+psm_code+"'";
		psmDAO.executeUpdate(update);

		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  PSM BUDGET�� ������ �����ϱ�
	//*******************************************************************/	
	public String updatePsmStatus(String pid,String psm_code,String psm_type,String comp_name,String comp_category,
		String psm_korea,String psm_english,String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,
		String psm_budget,String psm_user,String plan_sum,String plan_labor,String plan_material,
		String plan_cost,String plan_plant,String change_desc,String budget_type) throws Exception
	{
		String data="",where="",update="";
		String change_date = anbdt.getDateNoformat();
		double sum=0.0,labor=0.0,material=0.0,cost=0.0,plant=0.0;

		//���1. PSM_MASTER - ������ PSM BUDGET ���� + ��������
		double[] budget = new double[5];
		budget = psmDAO.readPsmMasterBudget(psm_code,budget_type);		//PSM MASTER �����ȹ����

		double[] mod_budget = new double[5];
		mod_budget = psmDAO.readPsmBudget(pid);				//PSM BUDGET �߰���������

		if(budget_type.equals("1")) {						//�����߰� ����(���꿡��)
			sum = budget[0] - mod_budget[0] + Double.parseDouble(plan_sum);
			labor = budget[1] - mod_budget[1] + Double.parseDouble(plan_labor);
			material = budget[2] - mod_budget[2] + Double.parseDouble(plan_material);
			cost = budget[3] - mod_budget[3] + Double.parseDouble(plan_cost);
			plant = budget[4] - mod_budget[4] + Double.parseDouble(plan_plant);
		} else if (budget_type.equals("3")){				//����谨 ����(���꿡��)
			sum = budget[0] - mod_budget[0] + Double.parseDouble(plan_sum)*-1;
			labor = budget[1] - mod_budget[1] + Double.parseDouble(plan_labor)*-1;
			material = budget[2] - mod_budget[2] + Double.parseDouble(plan_material)*-1;
			cost = budget[3] - mod_budget[3] + Double.parseDouble(plan_cost)*-1;
			plant = budget[4] - mod_budget[4] + Double.parseDouble(plan_plant)*-1;
		} else if (budget_type.equals("2"))	{				//����ݾ� ����(���⿡��)
			sum = budget[0] - mod_budget[0] + Double.parseDouble(plan_sum);
			labor = budget[1] - mod_budget[1] + Double.parseDouble(plan_labor);
			material = budget[2] - mod_budget[2] + Double.parseDouble(plan_material);
			cost = budget[3] - mod_budget[3] + Double.parseDouble(plan_cost);
			plant = budget[4] - mod_budget[4] + Double.parseDouble(plan_plant);
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

		//PSM BUDGET �����ϱ�
		update = "UPDATE psm_budget SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+change_desc;
		update += "',plan_sum='"+Double.parseDouble(plan_sum)+"',plan_labor='"+Double.parseDouble(plan_labor);
		update += "',plan_material='"+Double.parseDouble(plan_material)+"',plan_cost='"+Double.parseDouble(plan_cost);
		update += "',plan_plant='"+Double.parseDouble(plan_plant)+"',change_date='"+change_date;
		update += "',budget_type='"+budget_type+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		
		//PSM MASTER�� ���泻�� UPdate�ϱ�
		if (budget_type.equals("2"))	{			//�������
			update = "UPDATE psm_master SET result_sum='"+sum+"',result_labor='"+labor;
			update += "',result_material='"+material+"',result_cost='"+cost+"',result_plant='"+plant;
			update += "',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div+"' where psm_code='"+psm_code+"'";
		} else {									//�������
			update = "UPDATE psm_master SET plan_sum='"+sum+"',plan_labor='"+labor;
			update += "',plan_material='"+material+"',plan_cost='"+cost+"',plan_plant='"+plant;
			update += "',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div+"' where psm_code='"+psm_code+"'";
		}
		psmDAO.executeUpdate(update);

		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// PSM BUDGET�� ������ �����ϱ�
	//*******************************************************************/	
	public String deletePsmBudget(String pid,String psm_code,String budget_type) throws Exception
	{
		String delete="",data="",update="",where="";
		double sum=0.0,labor=0.0,material=0.0,cost=0.0,plant=0.0;

		//���ʵ�Ϲ� Ȯ����ϵ� ������ 1���� ������ �� ����.(�����̷��� ����� ������ �ƴϹǷ�)
		query = "select count(*) from psm_budget where psm_code='"+psm_code+"'";
		int cnt = psmDAO.getTotalCount(query);
		if(cnt == 1) {
			data = "���ʵ�������� ������ �� �����ϴ�.";
			return data;
		}

		//���ſ� ���� ���������� ������ �� �����ϴ�.
		if(budget_type.equals("2")) {
			data = "���ſ� ���� ���������� ������ �� �����ϴ�.";
			return data;
		}

		//�����ʹ� ������ �ܰ�� �ٽ� �ٲ����ϱ�
		//���1. PSM_MASTER - ������� PSM BUDGET ���� 
		double[] budget = new double[5];
		budget = psmDAO.readPsmMasterBudget(psm_code,budget_type);		//PSM MASTER �����ȹ����

		double[] mod_budget = new double[5];
		mod_budget = psmDAO.readPsmBudget(pid);				//PSM BUDGET �߰���������

		if(budget_type.equals("1")) {						//�����߰� ����
			sum = budget[0] - mod_budget[0];
			labor = budget[1] - mod_budget[1];
			material = budget[2] - mod_budget[2];
			cost = budget[3] - mod_budget[3];
			plant = budget[4] - mod_budget[4];
		} else {											//����谨 ����
			sum = budget[0] + mod_budget[0];
			labor = budget[1] + mod_budget[1];
			material = budget[2] + mod_budget[2];
			cost = budget[3] + mod_budget[3];
			plant = budget[4] + mod_budget[4];
		}

		where = "where psm_code='"+psm_code+"' order by pid desc";
		String second_pid = psmDAO.getSecondData("psm_budget","pid",where);

		com.anbtech.psm.entity.psmBudgetTable budgetT = new com.anbtech.psm.entity.psmBudgetTable();
		budgetT = psmDAO.readPsmBudget(second_pid,psm_code);

		//PSM MASTER�� ���泻�� UPdate�ϱ�
		update = "UPDATE psm_master SET plan_sum='"+sum+"',plan_labor='"+labor;
		update += "',plan_material='"+material+"',plan_cost='"+cost+"',plan_plant='"+plant;
		update += "',psm_budget='"+budgetT.getPsmBudget()+"',psm_budget_div='"+budgetT.getPsmBudgetDiv();
		update += "' where psm_code='"+psm_code+"'";
		psmDAO.executeUpdate(update);

		//���꺯�����
		delete = "DELETE FROM psm_budget WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		data = "���������� �����Ǿ����ϴ�.";
		
		return data;
	}
	
}
