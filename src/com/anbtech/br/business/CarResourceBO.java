package com.anbtech.br.business;

import com.anbtech.br.entity.*;
import com.anbtech.br.db.*;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;
import java.sql.*;
import java.util.*;
import java.io.*;

public class CarResourceBO{

	private Connection con;

	public CarResourceBO(Connection con){
		this.con = con;
	}
	

	/*****************************************************************
	 * �����ڵ庰 ���¸� ��������
	 *****************************************************************/
	public String getStatName(String code) throws Exception
	{
		
		String stat_name = "";

		if(code.equals("1")) stat_name = "��밡��";
		else if(code.equals("2")) stat_name = "������";
		else if(code.equals("3")) stat_name = "������";
		else if(code.equals("4")) stat_name = "�������";
		else if(code.equals("5")) stat_name = "������";
		else if(code.equals("6")) stat_name = "����ó��";
		else if(code.equals("7")) stat_name = "�԰�Ϸ�";
		else if(code.equals("8")) stat_name = "�ӽ�����";


		return stat_name;
	}

	/*****************************************************************
	 * ���� ���� ���� �˾ƺ���..
	 *****************************************************************/
	public String checkLending(String start, String end, String cid) throws Exception{
//public void checkLending(String start, String end, String cid) throws Exception{			
		com.anbtech.br.db.CarResourceDAO carResourceDAO= new com.anbtech.br.db.CarResourceDAO(con);
		boolean bl=true;
		String check_msg = "";
		
		if((Double.parseDouble(end) - Double.parseDouble(start)) <= 0) {
			//throw new Exception("<script> alert('��¥�� �ùٷ� ������ �ֽʽÿ�');history.go(-1);</script>");
			check_msg = "��¥�� �ùٷ� ������ �ֽʽÿ�";
		}

		ArrayList table_list = new ArrayList();
		table_list = (ArrayList)carResourceDAO.getSavedate(cid);;
		int i = table_list.size();

		String arr[]=new String[i];

		Iterator table_iter = table_list.iterator();
		String startdate=start,enddate=end;
	//	System.out.println("size = "+i);
	
		//int startdate=Integer.parseInt(start);
		//int startdate=Integer.parseInt(start);
		// Test �迭 ���峻��
		int n=0;
		while(table_iter.hasNext()){
				arr[n]=(String)table_iter.next();
				//System.out.println("while ��"+arr[n]);
				n+=1;
		}

		for(int k=0;k<i;k+=2){
			double arr_start = Double.parseDouble(arr[k]);
			double arr_end = Double.parseDouble(arr[k+1]);

			if(((arr_start <= Double.parseDouble(startdate) &&  Double.parseDouble(startdate) <= arr_end)) || ((arr_start <= Double.parseDouble(enddate)) && ( Double.parseDouble(enddate) <= arr_end)) ) {
				//System.out.println("�� "+k+" "+bl);
				bl=false;
				
				break;
				
			} 
		}
	//	System.out.println(bl);

		//if(!bl) throw new Exception("<script> alert('������ �� �����ϴ�. ���೯¥�� �ߺ��Ǿ����ϴ�.');history.go(-1);</script>");
		//return bl;
		if(!bl) check_msg="������ �� �����ϴ�. ���೯¥�� �ߺ��Ǿ����ϴ�.";
		return check_msg;
	}
	
	/*****************************************************************
	 * where ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category, String login_id,String cid) throws Exception{

		//�˻����ǿ� �°� where������ �����Ѵ�.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		
		if(mode.equals("eachcar")) where = " WHERE c_id='"+cid+"' AND flag2 = 'EF' AND u_year='"+searchword+"'";
		if(mode.equals("req_list")) where = " WHERE v_status='2' and flag = 'EF' AND flag2 = 'EN'";
		
		return where;
	}

	/*****************************************************************************
	 *  ���� �԰� ó��
	 *****************************************************************************/
	public void enteringProcess(String cid,String cr_id, String  mgr_id, String mgr_name, String st,String chg_cont,String w_time) throws Exception {

		CarResourceDAO carResourceDAO = new CarResourceDAO(con);
		
		// �԰� ó�� �ð�
		String y = w_time.substring(0,4);
		String m = w_time.substring(5,7);
		String d = w_time.substring(8,10);
		String t = w_time.substring(11,16);
		
		w_time = y+"-"+m+"-"+d+" "+t;

		carResourceDAO.enteringCar(cid,cr_id,mgr_id,mgr_name,st,chg_cont,w_time,y,m,d,t);
		if(st.equals("������")) {	carResourceDAO.updateCarState(cid,"5");	}
				
	}
}