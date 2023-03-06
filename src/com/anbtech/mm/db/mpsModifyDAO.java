package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mpsModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mpsModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		MPS MASTER �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MPS_MASTER���� �б�
	//*******************************************************************/	
	public mpsMasterTable readMasterItem(String pid,String factory_no,String view_td) throws Exception
	{
		//���� �ʱ�ȭ
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mpsMasterTable table = new com.anbtech.mm.entity.mpsMasterTable();
		
		query = "SELECT * FROM mps_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMpsNo(rs.getString("mps_no"));	
			table.setOrderNo(rs.getString("order_no"));	
			table.setMpsType(rs.getString("mps_type"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setPlanCount(rs.getInt("plan_count"));
			table.setSellCount(rs.getInt("sell_count"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMpsStatus(rs.getString("mps_status"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppNo(rs.getString("app_no"));
			table.setOrderComp(rs.getString("order_comp"));
		} else {
			//����� ���ϱ�
			where = "where factory_code='"+factory_no+"'";
			String factory_name = getColumData("factory_info_table","factory_name",where);
			
			table.setPid("");	
			table.setMpsNo("");	
			table.setOrderNo("");	
			table.setMpsType("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setPlanDate(view_td);
			table.setPlanCount(0);
			table.setSellCount(0);
			table.setItemUnit("");
			table.setMpsStatus("");
			table.setFactoryNo(factory_no);
			table.setFactoryName(factory_name);
			table.setRegDate("");
			table.setRegId("");	
			table.setRegName("");	
			table.setAppDate("");	
			table.setAppId("");	
			table.setAppNo("");
			table.setOrderComp("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MPS_MASTER �⵵��/���� Calendar�� ����� ���� ����
	// login_id : �ڽ��ۼ��ô� ���κ����ְ�, �ƴѰ��� ����Ȯ�������ĺ��� �����־�
	// ���ΰ����ڴ� ���ο�û�Ȱ� ���� �� �� �־�
	//*******************************************************************/	
	public String getMpsCalendarList(String factory_no,String year,String month,String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "",plan_date=year+month;
		String pdate="",reg_id="",mps_status="",view="",tag="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//����� ���Ѿ˾ƺ��� (USER:�����,MGR:������)
		String mgr = checkGrade("MPS",login_id,factory_no);
		
		//query���� �����
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and plan_date like '"+plan_date+"%' ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				pdate = rs.getString("plan_date");
				reg_id = rs.getString("reg_id");
				mps_status = rs.getString("mps_status");
				view = "Y";
				if(mgr.indexOf("MGR") != -1 & !reg_id.equals(login_id)) {
					if(mps_status.equals("1")) view = "N";
				} else if(!reg_id.equals(login_id)) {
					if(mps_status.equals("1") || mps_status.equals("2")) view = "N";	//ȭ����¿����Ǵ�
				} 
				pdate = anbdt.getSepDate(pdate,"/");
	
				//��������� �˷��ֱ�
				tag = "";
				if(mps_status.equals("3")) {			//MPS����
					tag="<font color='blue'><b>(S)</b></font>";
				} else if(mps_status.equals("4") || mps_status.equals("5")) {	//MRP����,MRP����
					tag="<font color='red'><b>(P)</b></font>";
				} else if(mps_status.equals("6")) {	//��������Ȯ��
					tag="<font color='brown'><b>(M)</b></font>";
				} else if(mps_status.equals("7")) {	//��������
					tag="<font color='gray'><b>(C)</b></font>";
				}
				data += pdate+"*"+view+"@"+tag+rs.getString("fg_code")+"(";
				data += rs.getInt("plan_count")+")@";
				data += rs.getString("pid")+"@;";
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return data;
	}

	//*******************************************************************
	// MPS_MASTER �ְ����� ����� ���� ����
	// login_id : �ڽ��ۼ��ô� ���κ����ְ�, �ƴѰ��� ����Ȯ�������ĺ��� �����־�
	// ���ΰ����ڴ� ���ο�û�Ȱ� ���� �� �� �־�
	// week_cnt : �ְ�����, view_td : �������ϴ� �ְ��� �Ͽ��� ����(yyyy/mm/dd)
	//*******************************************************************/	
	public ArrayList getMpsWeekList(String factory_no,String view_td,String login_id,String week_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "",plan_std="",plan_etd="",reg_id="",mps_status="",view="Y";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//����� ���Ѿ˾ƺ��� (USER:�����,MGR:������)
		String mgr = checkGrade("MPS",login_id,factory_no);

		//�ְ��� �˻� �����ϰ� ������ ���ϱ�
		String y = view_td.substring(0,4);		//��
		String m = view_td.substring(5,7);		//��
		String d = view_td.substring(8,10);		//��
		int week = Integer.parseInt(week_cnt);
		int wcnt = week*7-1;
		String sdate = anbdt.getDate(Integer.parseInt(y),Integer.parseInt(m),Integer.parseInt(d),wcnt);

		plan_std = y+m+d;
		plan_etd = sdate.substring(0,4)+sdate.substring(5,7)+sdate.substring(8,10);

		//query���� �����
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and (plan_date >= '"+plan_std+"' ";
		query += "and plan_date <= '"+plan_etd+"') ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		ArrayList table_list = new ArrayList();
		mpsMasterTable table = null;
		while(rs.next()) { 	
			reg_id = rs.getString("reg_id");
			mps_status = rs.getString("mps_status");

			//���ǰ˻�
			if(reg_id.equals(login_id)) { view = "Y"; }
			else if(mgr.indexOf("MGR") != -1 & !mps_status.equals("1")) { view = "Y"; }
			else if(!mps_status.equals("1") & !mps_status.equals("2")) { view = "Y"; }
			else view = "N";


			//������
			if(view.equals("Y")) {
				table = new mpsMasterTable();
				table.setPid(rs.getString("pid"));	
				table.setMpsNo(rs.getString("mps_no"));	
				table.setOrderNo(rs.getString("order_no"));	
				table.setMpsType(rs.getString("mps_type"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPlanDate(anbdt.getSepDate(rs.getString("plan_date"),"/"));
				table.setPlanCount(rs.getInt("plan_count"));
				table.setSellCount(rs.getInt("sell_count"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMpsStatus(rs.getString("mps_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppNo(rs.getString("app_no"));
				table.setOrderComp(rs.getString("order_comp"));
				table_list.add(table);
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MPS_MASTER �������� ����� ���� ����
	// login_id : �ڽ��ۼ��ô� ���κ����ְ�, �ƴѰ��� ����Ȯ�������ĺ��� �����־�
	// ���ΰ����ڴ� ���ο�û�Ȱ� ���� �� �� �־�
	// view_td : �������ϴ� �ְ��� �Ͽ��� ����(yyyy/mm/dd)
	//*******************************************************************/	
	public ArrayList getMpsMonthList(String factory_no,String view_td,String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "",plan_date="",reg_id="",mps_status="",view="Y";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//����� ���Ѿ˾ƺ��� (USER:�����,MGR:������)
		String mgr = checkGrade("MPS",login_id,factory_no);

		//���� �˻����� ���ϱ�
		String y = view_td.substring(0,4);		//��
		String m = view_td.substring(5,7);		//��
		plan_date = y+m;
	
		//query���� �����
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and plan_date like '"+plan_date+"%' ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		ArrayList table_list = new ArrayList();
		mpsMasterTable table = null;
		while(rs.next()) { 	
			reg_id = rs.getString("reg_id");
			mps_status = rs.getString("mps_status");

			//���ǰ˻�
			if(reg_id.equals(login_id)) { view = "Y"; }
			else if(mgr.indexOf("MGR") != -1 & !mps_status.equals("1")) { view = "Y"; }
			else if(!mps_status.equals("1") & !mps_status.equals("2")) { view = "Y"; }
			else view = "N";

			//������
			if(view.equals("Y")) {
				table = new mpsMasterTable();
				table.setPid(rs.getString("pid"));	
				table.setMpsNo(rs.getString("mps_no"));	
				table.setOrderNo(rs.getString("order_no"));	
				table.setMpsType(rs.getString("mps_type"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPlanDate(anbdt.getSepDate(rs.getString("plan_date"),"-"));
				table.setPlanCount(rs.getInt("plan_count"));
				table.setSellCount(rs.getInt("sell_count"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMpsStatus(rs.getString("mps_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppNo(rs.getString("app_no"));
				table.setOrderComp(rs.getString("order_comp"));
				table_list.add(table);
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// MPS������ȣ ���ϱ�
	// FORMAT : MPS+yy(2)+mm(2)+serial(2)
	//*******************************************************************/	
	public String getMpsNo(String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//MPS��ȣ�� Serial�� ������ ��ȣ ���ϱ�
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mps_no = "MPS"+y+m;
		
		//query���� �����
		query = "SELECT mps_no FROM mps_master where factory_no='"+factory_no+"' and mps_no like '"+mps_no+"%' ";
		query += "order by mps_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("mps_no");
		
		if(data.length() == 0) {
			data = mps_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mps_no+serial;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		CALENDAR ������ �������� ��������
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// CALENDAR_COMMON �⵵��/���� Calendar�� ����� ���������� ����
	//*******************************************************************/	
	public String getHolidayList(String year,String month) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT * FROM calendar_common where (item='LHD' and hdmon ='"+month+"') or ";
		query += "(item='BHD' and hdyear='"+year+"' and hdmon='"+month+"') ";
		query += "order by nlist asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				data += rs.getString("nlist")+";";
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		BOM���� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// �ش�ǰ���� �ش�����ڵ�(gid)���� ���Ե� ��ǰ�Ǵ� ����ǰ ǰ���ڵ����� �Ǵ��ϱ�
	//*******************************************************************/	
	public int checkItemCode(String gid,String item_code) throws Exception
	{
		//���� �ʱ�ȭ
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT COUNT(*) FROM mbom_item where gid='"+gid+"' and parent_code='"+item_code+"'";
		rs = stmt.executeQuery(query);

		rs.next();
		cnt = rs.getInt(1);

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return cnt;
	}

	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// SQL update �����ϱ�
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	�ش��׸��� ������ �˻�
	//*******************************************************************/
	public String checkGrade(String mgr_type,String login_id,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select mgr_code from mfg_grade_mgr where factory_no='"+factory_no+"' and ";
		query += "mgr_type='"+mgr_type+"' and mgr_id like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("mgr_code")+",";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

}

