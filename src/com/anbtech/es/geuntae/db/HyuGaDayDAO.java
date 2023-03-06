package com.anbtech.es.geuntae.db;
import com.anbtech.es.geuntae.business.HyuGaDayBO;
import com.anbtech.es.geuntae.entity.*;
import com.anbtech.date.anbDate;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.admin.entity.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Double;

public class HyuGaDayDAO{
	private Connection con;
	private com.anbtech.date.anbDate anbdt;
	private com.anbtech.es.geuntae.business.HyuGaDayBO hdBO;
	private com.anbtech.dbconn.DBConnectionManager connMgr;	
	private com.anbtech.admin.db.UserInfoDAO userDAO;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public HyuGaDayDAO(Connection con){
		this.con = con;
		anbdt = new com.anbtech.date.anbDate();
		hdBO = new com.anbtech.es.geuntae.business.HyuGaDayBO();
		userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
	}

	public HyuGaDayDAO() 
	{	
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		anbdt = new com.anbtech.date.anbDate();
		hdBO = new com.anbtech.es.geuntae.business.HyuGaDayBO();
	}	

	/*******************************************************************
	 * �⵵ column nameã��
	 *******************************************************************/
	public String searchColumn(String month) 
	{
		String column = "";
		if(month.equals("01")) column = "jan1";
		else if (month.equals("02")) column = "feb2";
		else if (month.equals("03")) column = "mar3";
		else if (month.equals("04")) column = "apr4";
		else if (month.equals("05")) column = "may5";
		else if (month.equals("06")) column = "jun6";
		else if (month.equals("07")) column = "jul7";
		else if (month.equals("08")) column = "aug8";
		else if (month.equals("09")) column = "sep9";
		else if (month.equals("10")) column = "oct10";
		else if (month.equals("11")) column = "nov11";
		else if (month.equals("12")) column = "dec12";
		return column;
	}

	/*******************************************************************
	 * �ش����� �⵵/���������� ������ ���翩�� �Ǵ�
	 *******************************************************************/
	public boolean isEmpty(String ys_kind, String hd_var, String user_id, String ac_code, String year) throws Exception
	{
		boolean tag = true;
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM geuntae_count where (ys_kind='"+ys_kind+"') and ";
			query += "(hd_var = '"+hd_var+"') and (user_id = '"+user_id+"') and ";
			query += "(ac_code = '"+ac_code+"') and (thisyear='"+year+"')";
		//System.out.println("isEmpty q : " + query);
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();

		if(total_count == 0) tag = true;
		else tag = false;
		return tag;
	}
	
	/*******************************************************************
	 * �ش����� �⵵/���������� ������ ����� �����ڵ�,������� �ľ��ϱ�
	 *******************************************************************/
	public String[] getGTCount(String ys_kind, String hd_var, String user_id, String ac_code, String year, String month) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String[] data = new String[2];	//�����ڵ�, �������
		String month_column = searchColumn(month);

		String query = "SELECT gt_id,"+month_column+" FROM geuntae_count where (ys_kind='"+ys_kind+"') and ";
		query += "(hd_var = '"+hd_var+"') and (user_id = '"+user_id+"') and ";
		query += "(ac_code = '"+ac_code+"') and (thisyear='"+year+"')";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data[0] = rs.getString("gt_id");
			data[1] = rs.getString(month_column);
		}
		if(data[1] == null) data[1] = "0";
		if(data[1].length() == 0) data[1] = "0";
	
		stmt.close();
		rs.close();

		return data;
	}

	/*****************************************************************************
	 * �ű� ����ϱ�(geuntae_count ���̺�)
	 *****************************************************************************/
	 public void insertCountTable(String gt_id,String ys_kind,String hd_var,String user_id,String user_name,String user_code,
		String user_rank,String div_code,String code,String div_name,String this_year,String month_column,String count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO GEUNTAE_COUNT(gt_id,ys_kind,hd_var,user_id,user_name,user_code,";
			incommon += "user_rank,ac_code,code,ac_name,thisyear,"+month_column+") values('";
		String input = "";
		
		input = incommon+gt_id+"','"+ys_kind+"','"+hd_var+"','"+user_id+"','"+user_name+"','"+user_code+"','";
		input += user_rank+"','"+div_code+"','"+code+"','"+div_name+"','"+this_year+"','"+count+"')";

		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
	}

	/*****************************************************************************
	 * ���� ����ϱ�(geuntae_count ���̺�)
	 *****************************************************************************/
	 public void updateCountTable(String gt_id,String month_column,String count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE GEUNTAE_COUNT SET "+month_column+"='"+count+"' WHERE ";
			update += "gt_id='"+gt_id+"'";
		
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
	
	/*****************************************************************************
	 * ����ں� ���������� ������ ó���ϱ�
	 *****************************************************************************/
	 public void processCount(String doc_lid) throws Exception
	{
		String gt_id,ys_kind,hd_var;
		String user_id,user_name,ac_id,user_code,user_rank,code;
		String div_name,div_code;
		String u_year,u_month,u_date;
		String tu_year,tu_month,tu_date;
		String input1,input2,data;
		//�ʱ�ȭ �ϱ�
		gt_id=ys_kind=hd_var=user_id=user_name=user_code=user_rank=ac_id=code="";
		div_name=div_code=u_year=u_month=u_date=tu_year=tu_month=tu_date="";
		input1=input2=data="";

		//------------------------------------
		// geuntae_master���� ���ó��� ���ϱ�
		//------------------------------------
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,";
			query += "ac_id,ac_code,ac_name,u_year,u_month,u_date,tu_year,tu_month,tu_date FROM ";
			query += "geuntae_master where gt_id='"+doc_lid+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			gt_id = rs.getString("gt_id");
			ys_kind = rs.getString("ys_kind");
			hd_var = rs.getString("hd_var");
			user_id = rs.getString("user_id");
			user_name = rs.getString("user_name");
			user_code = rs.getString("user_code");
			user_rank = rs.getString("user_rank");
			ac_id = rs.getString("ac_id");
			div_code = rs.getString("ac_code");
			div_name = rs.getString("ac_name");
			u_year = rs.getString("u_year");
			u_month = rs.getString("u_month");
			u_date = rs.getString("u_date");
			tu_year = rs.getString("tu_year");
			tu_month = rs.getString("tu_month");
			tu_date = rs.getString("tu_date");

			//�����ϼ� ���ϱ�
			input1 = u_year + u_month + u_date;
			input2 = tu_year + tu_month + tu_date;
			data = hdBO.getCount(input1,input2);			//����� �ش����� ������������(200308,24|200309,12| ...) 
		}
		stmt.close();
		rs.close();

		//------------------------------------
		//	�μ� �����ڵ��� �̿� ���ΰ��� �ڵ� ���ϱ�
		//------------------------------------
		code = userDAO.getCode(ac_id);

		//------------------------------------
		//���� �ϼ��� �⵵/���� �迭�� ���
		//------------------------------------
		StringTokenizer bar_cnt = new StringTokenizer(data,"|");
		int tot_cnt = bar_cnt.countTokens();
		String[][] list = new String[tot_cnt][3];
		int i = 0;
		while(bar_cnt.hasMoreTokens()) {
			String ea_ym = bar_cnt.nextToken();
			StringTokenizer ea_data = new StringTokenizer(ea_ym,",");
			int ea = 0;
			while(ea_data.hasMoreTokens()) {
				if(ea == 0) {
					String ym = ea_data.nextToken();
					list[i][0] = ym.substring(0,4);						//�⵵
					list[i][1] = ym.substring(4,6);						//��
				} else if(ea == 1) list[i][2] = ea_data.nextToken();	//����
				ea++;
			}
			i++;
		}

		//------------------------------------
		//�����/���º� ����ϱ�
		//------------------------------------
		for(int n=0; n<i; n++) {
			String month_col_name = searchColumn(list[n][1]);		//�ش���� �÷���
			//�ű� ����ϱ�
			if(isEmpty(ys_kind,hd_var,user_id,div_code,list[n][0])) {
				insertCountTable(gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,div_code,code,div_name,list[n][0],month_col_name,list[n][2]);
			} 
			//���� �ϱ� (update)
			else {
				String[] idCnt = new String[2];			//�����ڵ�,�ش����
				idCnt = getGTCount(ys_kind,hd_var,user_id,div_code,list[n][0],list[n][1]);

				double t_cnt = Double.parseDouble(idCnt[1]) + Double.parseDouble(list[n][2]);//����+�ű�
				String u_cnt = Double.toString(t_cnt);
				updateCountTable(idCnt[0],month_col_name,u_cnt);
			}
		} //for

		//con �ݱ�
		//close();
	}

	/*****************************************************************************
	 * �� ���� �ϼ� ���ϱ�
	 *****************************************************************************/
	 public double getAACount(String sy,String sm,String sd,String ey,String em,String ed) throws Exception
	{
		//�����ϼ� ���ϱ�
		String input1 = sy + sm + sd;
		String input2 = ey + em + ed;
		String data = hdBO.getCount(input1,input2);			//����� �ش����� ������������(200308,24|200309,12| ...) 
		
		double total_cnt = 0.0;
		StringTokenizer bar_cnt = new StringTokenizer(data,"|");
		while(bar_cnt.hasMoreTokens()) {
			String ea_ym = bar_cnt.nextToken();
			StringTokenizer ea_data = new StringTokenizer(ea_ym,",");
			int ea = 0;
			while(ea_data.hasMoreTokens()) {
				if(ea == 0) {
					ea_data.nextToken();
				} else if(ea == 1) total_cnt += Double.parseDouble(ea_data.nextToken());	//����
				ea++;
			}
		}
		return total_cnt;
	}


	/*****************************************************************************
	 * �⵵��,���±��к�,�μ��� ����� �ް��ܷ��� ����Ͽ� ����Ѵ�.
	 * �ش� �����Ͱ� ������ ��°� ���ÿ� db�� �����Ѵ�.
	 * year:�ش�⵵, kind:�ް�����, code:�μ��ڵ�(class_table�� code �ʵ尪)
	 *****************************************************************************/
	 public ArrayList getUserHyuGaRestDay(String year,String kind,String code) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";

		if(kind == null || kind.equals("")) kind = "HD_003"; // ����Ʈ�� ������ �����Ѵ�.

		com.anbtech.es.geuntae.business.HyuGaDayBO hyugadayBO = new com.anbtech.es.geuntae.business.HyuGaDayBO(con);
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable table = new com.anbtech.admin.entity.UserInfoTable();

		ArrayList table_view = new ArrayList();
		ArrayList user_list = new ArrayList();
		
		// user_table ���� ����� ����Ʈ�� �����´�.
		user_list = (ArrayList)userinfoDAO.getUserListByInnerCode(code);

		Iterator user_iter = user_list.iterator();

		// ������ ����� ID�� geuntae_hd_max ���̺��� �˻��Ͽ� �ش� �����Ͱ� ������ �����ؼ� �����ϰ�,
		// ������ ����Ͽ� db�� ������ ���� �����Ͽ� �����Ѵ�.
		while(user_iter.hasNext()){
			table = (UserInfoTable)user_iter.next();
			
			String id = table.getUserId();			// ����� ����� �����´�.
			String enter_day = table.getEnterDay();	// ����� �Ի����� �����´�.
			int continous_year = hyugadayBO.getContinousYear(enter_day,year); // �ټ� ��� ���
			table.setContinuousYear(Integer.toString(continous_year));

			// �ش� ����� ������ geuntae_hd_max ���̺� �ִ��� üũ
			query = "select hdmax from geuntae_hd_max where id = '" + id + "' and byyear = '" + year + "' and kind = '" + kind + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);

			// �ش� ����� ������ ������ �� ������ table_view�� �����Ѵ�.				
			String rest_day = "";

			if(rs.next()){
				rest_day = rs.getString("hdmax");
				table.setHyuGaRestDay(rest_day);
			}
				// �ش� ����� ������ ������ �ټӳ���, �ܷ��� ����Ͽ� db�� ������ ���� table_view�� �����Ѵ�.
			else{

				String previous_year = Integer.toString(Integer.parseInt(year)-1); // ���� �⵵ ���
				// �����⵵�� �ް� �ܷ����� �����´�.
				query = "select hdmax from geuntae_hd_max where id = '" + id + "' and byyear = '" + previous_year + "' and kind = '" + kind + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				double previous_rest_day = 0.0;
				while(rs.next()){
					previous_rest_day = Double.parseDouble(rs.getString("hdmax"));
				}

				// �����⵵�� ����� �ް����� �����´�.
				query = "select jan1,feb2,mar3,apr4,may5,jun6,jul7,aug8,sep9,oct10,nov11,dec12 ";
				query += "from geuntae_count where thisyear = '" + previous_year + "' and hd_var = '" + kind + "' and user_id = '" + id + "'";
				stmt = con.createStatement();
				rs = stmt.executeQuery(query);
				double used_day = 0.0;
				while(rs.next()){
						used_day = Double.parseDouble(rs.getString("jan1"));
						used_day += Double.parseDouble(rs.getString("feb2"));
						used_day += Double.parseDouble(rs.getString("mar3"));
						used_day += Double.parseDouble(rs.getString("apr4"));
						used_day += Double.parseDouble(rs.getString("may5"));
						used_day += Double.parseDouble(rs.getString("jun6"));
						used_day += Double.parseDouble(rs.getString("jul7"));
						used_day += Double.parseDouble(rs.getString("aug8"));
						used_day += Double.parseDouble(rs.getString("sep9"));
						used_day += Double.parseDouble(rs.getString("oct10"));
						used_day += Double.parseDouble(rs.getString("nov11"));
						used_day += Double.parseDouble(rs.getString("dec12"));			
				}

				// �ش�⵵�� ���� �ܷ��� = �����⵵ �ް� �ܷ� + 10(�⺻) + �ټӳ�� - �����⵵ �ް���뷮
				// �ش�⵵�� ���� �ܷ��� = �����⵵ �ް� �ܷ� + 12(�⺻) - �����⵵ �ް���뷮
				double restday = 0.0;
				if(kind.equals("HD_006")) restday = previous_rest_day + 10.0 + continous_year - used_day; //����
				else if(kind.equals("HD_003")) restday = previous_rest_day + 12.0 - used_day;	//����

				//db�� �����Ѵ�.
				query = "insert into geuntae_hd_max (id,byyear,hdmax,kind) values('"+id+"','"+year+"','"+restday+"','"+kind+"')";
				stmt = con.createStatement();
				stmt.executeUpdate(query);
				table.setHyuGaRestDay(Double.toString(restday));
			}

			table_view.add(table);

		}
		stmt.close();
		rs.close();

		return table_view;
	}

	/***************************************
	 * ���� ���� �� ���� �ܷ� ���
	 ***************************************/
	public String getPersonalHoliday(String id, String year,String hd_var,String month) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		
		// �ش�⵵�� �ܷ�(��ȹ����)�� �����´�.
		double rest_day		= getHdMax(id,year,hd_var);
		String rest_maxday	= Double.toString(rest_day);

		// �ش����� �����ޱ��� ���� �ް� �ϼ��� ����Ѵ�.
		String query = "SELECT jan1,feb2,mar3,apr4,may5,jun6,jul7,aug8,sep9,oct10,nov11,dec12,user_id,hd_var FROM geuntae_count WHERE thisyear='"+year+"' and hd_var='"+hd_var+"' and user_id='"+id+"'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);

		double sum = 0.0;
		if(rs.next()){
			for(int i=1; i < Integer.parseInt(month); i++){
				sum += Double.parseDouble(rs.getString(i));
			}
		}

		// ���� �ܷ� =  �ش�⵵ �ܷ� - ��뷮
		rest_day = rest_day - sum;
		String rest = Double.toString(rest_day) + "/" + rest_maxday;

		rs.close();
		stmt.close();
		
		return rest;
	}


	/***************************************************
	 * geuntae_hd_max ���̺��� hdmax ���� �����´�.
	 ***************************************************/
	 public double getHdMax(String id, String year, String kind) throws Exception  {
			
		Statement stmt = null;
		ResultSet rs = null;
		double rest=0.0;
			
		String query = "SELECT hdmax FROM geuntae_hd_max WHERE id ='" + id + "' and byyear = '" + year + "' and kind='" + kind + "'";
		stmt = con.createStatement();
		rs=stmt.executeQuery(query);
			
		if(rs.next()) {
			rest = Double.parseDouble(rs.getString("hdmax"));
		} 
		
		rs.close();
		stmt.close();
		
		return rest;
	}

	/*********************************************************************
	 * �Ҹ���
	 *********************************************************************/
	public void close() throws Exception{
		connMgr.freeConnection("mssql",con);
	}

}
