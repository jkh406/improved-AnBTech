package com.anbtech.es.geuntae.business;

import com.anbtech.es.geuntae.entity.*;
import com.anbtech.ew.entity.*;
import com.anbtech.es.geuntae.db.*;
import com.anbtech.ew.db.*;
import com.oreilly.servlet.MultipartRequest;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;

import java.sql.*;
import java.util.*;
import java.io.*;

public class GeunTaeBO{

	private Connection con;

	public GeunTaeBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * where ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category, String login_id) throws Exception{

		//�˻����ǿ� �°� where������ �����Ѵ�.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		
		// �˻� ������ ������...
		//if(mode.equals("list") && searchword.equals("") && searchscope.equals("") && !login_id.equals("")) {
		where = " WHERE user_id='"+login_id+"' and flag = 'EF'";
		//}
		return where;
	}

	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		//�˻����ǿ� �°� where������ �����Ѵ�.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		
		// �˻� ������ ������...
		//if(mode.equals("list") && searchword.equals("") && searchscope.equals("") && !login_id.equals("")) {
		where = " WHERE flag = 'EF'";
		//}
		return where;
	}


	/*****************************************************************
	 * ���嵿���� �̸� �˾ƺ���
	 *****************************************************************/
	public String getFellowName(String fellow_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		StringTokenizer ids = new StringTokenizer(fellow_id,";");
		while(ids.hasMoreTokens()) {
			String query = "select name from user_table where id='"+ids.nextToken()+"'";
			rs = stmt.executeQuery(query);
			if(rs.next()) data += rs.getString("name")+";";
		}
		stmt.close();
		rs.close();
		return data;
	}

	/*****************************************************************
	 * �ް��� �˾ƺ���
	 *****************************************************************/
	public String getAccountName(String hd_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select ys_value from yangsic_env where ys_name='"+hd_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data += rs.getString("ys_value");
		
		stmt.close();
		rs.close();
		return data;
	}

	/*****************************************************************
	 * �������� �̸� �˾ƺ���
	 *****************************************************************/
	public String getHolidayName(String at_id) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String data = "";
		String query = "select ys_value from yangsic_env where ys_name='"+at_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data += rs.getString("ys_value");
		
		stmt.close();
		rs.close();
		return data;
	}


	/*****************************************************************************
	 * ���� ���� ���� ��Ȳ ����Ʈ�� �����ͼ� �޷� ���·� �����Ѵ�.
	 *****************************************************************************/
	public ArrayList getPersonalStatusByMonth(String year,String month,String login_id) throws Exception {

		GeunTaeDAO geuntaeDAO			= new GeunTaeDAO(con);
		GeunTaeInfoTable table			= new GeunTaeInfoTable();
		ExtraWorkModuleDAO ewDAO		= new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable	= new ExtraWorkHistoryTable();

		//���� ���� ���� ��Ȳ ����Ʈ�� �����´�.
		//���±Ⱓ�� 2�� �̻��� ��쿡�� �ش� �Ⱓ��ŭ ����Ʈ�� �����ȴ�.
		ArrayList table_list = new ArrayList();
		table_list = (ArrayList)geuntaeDAO.getTableListByPerson(year,month,login_id);
		
		//���� ���� Ư�� ��Ȳ ����Ʈ�� �����´�.
		ArrayList ew_table = new ArrayList();
		ew_table = (ArrayList)ewDAO.getTableListByPerson(year,month,login_id);

		//########## �޷� �������� ��º� ���� ##################
		DecimalFormat fmt = new DecimalFormat("00");
		Calendar curren_date,temp_date;

		int cur_year = Integer.parseInt(year);
		int cur_month = Integer.parseInt(month);

		//������ �ϴ� ����� �����Ѵ�.
		temp_date = Calendar.getInstance();
		temp_date.set(Calendar.YEAR,cur_year);
		temp_date.set(Calendar.MONTH,cur_month-1);
		temp_date.set(Calendar.DATE,1);

		com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
		calendar.setNewDate(temp_date);

		int dayno = calendar.getMyWeek();
		int lastday = calendar.getMyLastDay();

		//������ �ϴ� ��� ����
		String cur_yyyymm = cur_year + fmt.format(cur_month);

		int inx;  
		String wdate	 = "";		// �����(20030927)
		String wcontents = "";		// ���� ����
		String url		 = "";		// �ش� ��¥�� ������ ��ũ ���ڿ�
		String day		 = "";		// �ش� ��¥
		String ys_kind	 = "";		// ���� ����
		String time		 = "";		// ��ٽð�|��ٽð�
		String time_s	 = "";		// ��� �ð�
		String time_e	 = "";		// ��� ����
		
		inx = ((-1) * dayno) + 2 ;


		// �޷����� ����Ʈ�� ���� ���� ����
		GeunTaeInfoTable table_1 = new GeunTaeInfoTable();
		ArrayList table_list_1 = new ArrayList();
		
		while (inx < lastday){
			
		
			//######�Ͽ���########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));
				
				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}

				//Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = "<font color='red'>"+inx+"(��)</font>";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);
								
				table_list_1.add(table_1);
			}
			inx++;


			//######������########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = inx+"(��)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######ȭ����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = inx+"(ȭ)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######������########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = inx+"(��)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######�����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = inx+"(��)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######�ݿ���########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = inx+"(��)";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;

			//######�����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);
				wcontents = "";
				url = "";
				day = "";
				ys_kind = "";
				time = geuntaeDAO.getWorkTime(login_id,wdate);
				time_e = time.substring(time.lastIndexOf("|")+1,time.length());
				time_s = time.substring(0,time.lastIndexOf("|"));

				//����
				Iterator table_iter = table_list.iterator();
				while(table_iter.hasNext()){
					table = (GeunTaeInfoTable)table_iter.next();
					day = table.getDay();
					if(day.equals(wdate)){
						wcontents	+= table.getReason() + "<br>";
						ys_kind		+= table.getYs_kind() + "<br>";
					}
				}
							
				// Ư��
				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate();
					if(day.equals(wdate)){
						wcontents	+= ewtable.getDuty();
						ys_kind		+= "Ư��";
					}
				}

				url = "<font color='blue'>"+inx+"(��)</font>";
				table_1 = new GeunTaeInfoTable();
				table_1.setDay(url);
				table_1.setYs_kind(ys_kind);
				table_1.setReason(wcontents);
				table_1.setTimeS(time_s);
				table_1.setTimeE(time_e);

				table_list_1.add(table_1);
			}
			inx++;
		}//end while

/* ������ �ڵ�
		Iterator table_iter = table_list_1.iterator();
		while(table_iter.hasNext()){
			table_1 = (GeunTaeInfoTable)table_iter.next();
			String day_1 = table_1.getDay();
			String ys_kind_1 = table_1.getYs_kind();
			String reason_1 = table_1.getReason();
		}
*/
		return table_list_1;
	}

	/*****************************************************************************
	 * �Ϻ� ���� ���� ��Ȳ ����Ʈ�� �����ͼ� �ش� ������ ���±��й��ڿ��� �����Ѵ�.
	 *****************************************************************************/
	public String getPersonalStatusByDay(String year,String month,String day,String login_id) throws Exception {

		GeunTaeDAO geuntaeDAO			= new GeunTaeDAO(con);
		GeunTaeInfoTable table			= new GeunTaeInfoTable();
		ExtraWorkModuleDAO ewDAO		= new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable	= new ExtraWorkHistoryTable();

		//���� ���� ���� ��Ȳ ����Ʈ�� �����´�.
		//���±Ⱓ�� 2�� �̻��� ��쿡�� �ش� �Ⱓ��ŭ ����Ʈ�� �����ȴ�.
		ArrayList geuntae_list = new ArrayList();
		geuntae_list = (ArrayList)geuntaeDAO.getTableListByPerson(year,month,login_id);
		
		//���� ���� Ư�� ��Ȳ ����Ʈ�� �����´�.
		ArrayList ew_list = new ArrayList();
		ew_list = (ArrayList)ewDAO.getTableListByPerson(year,month,login_id);

		String wdate = year + month + day;	//���õ� �����
		String ys_kind = "";				//���±���
		
		//�������� ��������
		Iterator table_iter = geuntae_list.iterator();
		while(table_iter.hasNext()){
			table = (GeunTaeInfoTable)table_iter.next();
			String geuntae_day = table.getDay();
			if(geuntae_day.equals(wdate)){
				ys_kind += table.getYs_kind() + "<br>";
			}
		}

		//Ư������ ��������
		Iterator table_iter2 = ew_list.iterator();
		while(table_iter2.hasNext()){
			ewtable = (com.anbtech.ew.entity.ExtraWorkHistoryTable)table_iter2.next();
			String ew_day = ewtable.getRsdate();
			if(ew_day.equals(wdate)){
				ys_kind		+= "Ư��";
			}
		}

		return ys_kind;
	}

	/****************
	 * ���� ����
	 ****************/
	 public String getHdkind(String kind) throws Exception  {
			
			Statement stmt = null;
			ResultSet rs = null;
			String hyuga_kind="";
				
		
			stmt=con.createStatement();
			String query = "SELECT ys_value FROM  yangsic_env WHERE ys_name = '"+kind+"'";
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				hyuga_kind=rs.getString("ys_value");
			}
			stmt.close();
			rs.close();
		
			return hyuga_kind;
	}

	/***************************************************************************
	  * ������ ������� ���� ��¥(�����) �� ��������
	*****************************************************************************/
		public String getYesterday(String year, String month, String day) throws Exception {		
		
		com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
		
		DecimalFormat fmt = new DecimalFormat("00");
		Calendar curren_date,temp_date;

		int cur_year  = Integer.parseInt(year);
		int cur_month = Integer.parseInt(month);
		int day_int	  = Integer.parseInt(day);

		String yesterday ="";

		if(day_int==1) { // ���� ���ڰ� 1������ Ȯ��(������� ������ setting)
		
			if((cur_month - 1) <1) {     // ������� 1���϶� (���⵵�� setting)
									
				cur_month = 12;
				cur_year  = cur_year - 1;

			} else {
				
				cur_month--;			//  ������� 1���� �ƴҶ� ������� 1�� ����
	
			}

			// ���� ���� ���ϱ�
			temp_date = Calendar.getInstance();
			temp_date.set(Calendar.YEAR,cur_year);
			temp_date.set(Calendar.MONTH,cur_month-1);
			temp_date.set(Calendar.DATE,1);

			//com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
			calendar.setNewDate(temp_date);
			int lastday = calendar.getMyLastDay(); 
			month = fmt.format(cur_month);
			day = fmt.format(lastday);

			yesterday = cur_year + "" + month + "" + day;
			
		} else {
			month = fmt.format(cur_month);
			day = fmt.format(day_int-1);
		
			yesterday = cur_year + "" + month + "" + day;
		}

		return yesterday;
	}		

	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ�
	 **************************************************************************/
	public String getID() throws Exception 
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}		

}