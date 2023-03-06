package com.anbtech.es.geuntae.db;
import com.anbtech.es.geuntae.business.ChulJangDayBO;
import com.anbtech.es.geuntae.entity.*;
import com.anbtech.date.anbDate;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.admin.entity.*;
import java.sql.*;
import java.util.*;
import java.text.*;
import java.lang.Double;

public class ChulJangDayDAO{
	private Connection con;
	private com.anbtech.date.anbDate anbdt;
	private com.anbtech.es.geuntae.business.ChulJangDayBO cjBO;
	private com.anbtech.dbconn.DBConnectionManager connMgr;	
	private com.anbtech.admin.db.UserInfoDAO userDAO;
	private String[][] fellows;
	private int fels_cnt = 0;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	public ChulJangDayDAO(Connection con) 
	{	
		this.con = con;
		anbdt = new com.anbtech.date.anbDate();
		cjBO = new com.anbtech.es.geuntae.business.ChulJangDayBO();
		userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
	}	

	public ChulJangDayDAO() 
	{	
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
		anbdt = new com.anbtech.date.anbDate();
		cjBO = new com.anbtech.es.geuntae.business.ChulJangDayBO();
	}	

	/*******************************************************************
	 * �⵵ column nameã��
	 *******************************************************************/
	private String searchColumn(String month) 
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
	private boolean isEmpty(String ys_kind, String hd_var, String user_id, String ac_code, String year) throws Exception
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
		//System.out.println("isEmpty : ���� " + total_count);
		if(total_count == 0) tag = true;
		else tag = false;
		return tag;
	}
	
	/*******************************************************************
	 * �ش����� �⵵/���������� ������ ����� �����ڵ�,������� �ľ��ϱ�
	 *******************************************************************/
	private String[] getGTCount(String ys_kind, String hd_var, String user_id, String ac_code, String year, String month) throws Exception
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
	 private void insertCountTable(String gt_id,String ys_kind,String hd_var,String user_id,String user_name,String user_code,
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
	 private void updateCountTable(String gt_id,String month_column,String count) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE GEUNTAE_COUNT set "+month_column+"='"+count+"' where ";
			update += "gt_id='"+gt_id+"'";
		
		//System.out.println("update : " + update );
		int er = stmt.executeUpdate(update);
		//System.out.println("���ฮ�ϰ� : " + er );
		stmt.close();
	}
	
	/*****************************************************************************
	 * ����ں� ���庰 ������ ó���ϱ� (������ ó������)
	 *****************************************************************************/
	 public void processChulJangCount(String doc_lid) throws Exception
	{
		String gt_id,ys_kind,hd_var;
		String user_id,user_name,user_code,user_rank;
		String div_name,div_code,code,ac_id;
		String u_year,u_month,u_date;
		String tu_year,tu_month,tu_date;
		String fellow_names = "";
		String input1,input2,data;
		//�ʱ�ȭ �ϱ�
		gt_id=ys_kind=hd_var=user_id=user_name=user_code=user_rank=code=ac_id="";
		div_name=div_code=u_year=u_month=u_date=tu_year=tu_month=tu_date="";
		input1=input2=data="";

		//------------------------------------
		// geuntae_master���� ���ó��� ���ϱ�
		//------------------------------------
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,";
			query += "ac_id,ac_code,ac_name,fellow_names,u_year,u_month,u_date,tu_year,tu_month,tu_date FROM ";
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
			fellow_names = rs.getString("fellow_names");	if(fellow_names == null) fellow_names = "";
			u_year = rs.getString("u_year");
			u_month = rs.getString("u_month");
			u_date = rs.getString("u_date");
			tu_year = rs.getString("tu_year");
			tu_month = rs.getString("tu_month");
			tu_date = rs.getString("tu_date");

			//�����ϼ� ���ϱ�
			input1 = u_year + u_month + u_date;
			input2 = tu_year + tu_month + tu_date;		
			data = cjBO.getCount(input1,input2);			//����� �ش����� ������������(200308,24|200309,12| ...) 
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

		//------------------------------------
		//������/���º� ����ϱ�
		//------------------------------------
		if(fellow_names.length() != 0) {
			getFellows(fellow_names);		//���,�̸�,�����ڵ�,���޸�,�μ��ڵ�,�μ��� �迭���ϱ�
			for(int m=0; m<fels_cnt; m++) {
				for(int n=0; n<i; n++) {
					String month_col_name = searchColumn(list[n][1]);		//�ش���� �÷���
					//�ű� ����ϱ�
					if(isEmpty(ys_kind,hd_var,fellows[m][0],fellows[m][4],list[n][0])) {
						String mid = Long.toString(Long.parseLong(gt_id) + m + 1);
						insertCountTable(mid,ys_kind,hd_var,fellows[m][0],fellows[m][1],fellows[m][2],fellows[m][3],fellows[m][4],code,fellows[m][5],list[n][0],month_col_name,list[n][2]);
					} 
					//���� �ϱ� (update)
					else {
						String[] idCnt = new String[2];			//�����ڵ�,�ش����
						idCnt = getGTCount(ys_kind,hd_var,fellows[m][0],fellows[m][4],list[n][0],list[n][1]);

						double t_cnt = Double.parseDouble(idCnt[1]) + Double.parseDouble(list[n][2]);//����+�ű�
						String u_cnt = Double.toString(t_cnt);
						updateCountTable(idCnt[0],month_col_name,u_cnt);
					}
				} //for (��/���� ����)
			} //for	(������ ó��)
		} //if

		//con �ݱ�
		//close();

	}

	/*****************************************************************************
	 * ����ں� ���⺰ ������ ó���ϱ� (������ ó������)
	 *****************************************************************************/
	 public void processEoChulCount(String doc_lid) throws Exception
	{
		String gt_id,ys_kind,hd_var;
		String user_id,user_name,user_code,user_rank;
		String div_name,div_code,code,ac_id;
		String u_year,u_month;
		String fellow_names = "";
		
		//�ʱ�ȭ �ϱ�
		gt_id=ys_kind=hd_var=user_id=user_name=user_code=user_rank=code=ac_id="";
		div_name=div_code=u_year=u_month="";

		//------------------------------------
		// geuntae_master���� ���ó��� ���ϱ�
		//------------------------------------
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,";
			query += "ac_id,ac_code,ac_name,fellow_names,u_year,u_month FROM ";
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
			fellow_names = rs.getString("fellow_names");	if(fellow_names == null) fellow_names = "";
			u_year = rs.getString("u_year");
			u_month = rs.getString("u_month");
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
		String[][] list = new String[1][3];
		list[0][0] = u_year;						//�⵵
		list[0][1] = u_month;						//��
		list[0][2] = "1.0";							//����

		//------------------------------------
		//�����/���º� ����ϱ�
		//------------------------------------
		String month_col_name = searchColumn(list[0][1]);		//�ش���� �÷���
		//�ű� ����ϱ�
		if(isEmpty(ys_kind,hd_var,user_id,div_code,list[0][0])) {
			insertCountTable(gt_id,ys_kind,hd_var,user_id,user_name,user_code,user_rank,div_code,code,div_name,list[0][0],month_col_name,list[0][2]);
		} 
		//���� �ϱ� (update)
		else {
			String[] idCnt = new String[2];			//�����ڵ�,�ش����
			idCnt = getGTCount(ys_kind,hd_var,user_id,div_code,list[0][0],list[0][1]);
			double t_cnt = Double.parseDouble(idCnt[1]) + Double.parseDouble(list[0][2]);//����+�ű�
			String u_cnt = Double.toString(t_cnt);
			updateCountTable(idCnt[0],month_col_name,u_cnt);
		}
		

		//------------------------------------
		//������/���º� ����ϱ�
		//------------------------------------
		if(fellow_names.length() != 0) {
			getFellows(fellow_names);		//���,�̸�,�����ڵ�,���޸�,�μ��ڵ�,�μ��� �迭���ϱ�
			for(int m=0; m<fels_cnt; m++) {
				//�ű� ����ϱ�
				if(isEmpty(ys_kind,hd_var,fellows[m][0],fellows[m][4],list[0][0])) {
					String mid = Long.toString(Long.parseLong(gt_id) + m + 1);
					insertCountTable(mid,ys_kind,hd_var,fellows[m][0],fellows[m][1],fellows[m][2],fellows[m][3],fellows[m][4],code,fellows[m][5],list[0][0],month_col_name,list[0][2]);
				} 
				//���� �ϱ� (update)
				else {
					String[] idCnt = new String[2];			//�����ڵ�,�ش����
					idCnt = getGTCount(ys_kind,hd_var,fellows[m][0],fellows[m][4],list[0][0],list[0][1]);

					double t_cnt = Double.parseDouble(idCnt[1]) + Double.parseDouble(list[0][2]);//����+�ű�
					String u_cnt = Double.toString(t_cnt);
					updateCountTable(idCnt[0],month_col_name,u_cnt);
				}
			} //for	(������ ó��)
		} //if

		//con �ݱ�
		//close();

	}
	/*******************************************************************
	 * ������ �λ� �� �μ����� �ľ��ϱ�
	 *******************************************************************/
	private void getFellows(String fellow_names) throws Exception
	{
		//------------------------------------
		// �������������� ����� ����ϱ�
		//------------------------------------
		fellow_names = fellow_names.trim();
		StringTokenizer fls = new StringTokenizer(fellow_names,";");
		int tot_cnt = fls.countTokens();

		String[] uid = new String[tot_cnt];		//������ ������ ����� �迭�� ���
		int i = 0;
		while(fls.hasMoreTokens()) {
			String idn = fls.nextToken();
			StringTokenizer ids = new StringTokenizer(idn,"/");
			int ea = 0;
			while(ids.hasMoreTokens()) {
				if(ea == 0) {
					uid[i] = ids.nextToken();
					uid[i] = uid[i].trim();
				} else ids.nextToken();	
				ea++;
			}
			i++;
		}

		//------------------------------------
		// �������������� ����� �̿��Ͽ� �λ�� �μ����� ã��
		//------------------------------------
		fels_cnt = uid.length;
		fellows = new String[fels_cnt][6];		//���,�̸�,�����ڵ�,���޸�,�μ��ڵ�,�μ���

		Statement stmt = null;
		ResultSet rs = null;
		
		for(int m=0; m<fels_cnt; m++) {
			String query = "SELECT a.id,a.name,c.ar_code,c.ar_name,b.ac_code,b.ac_name ";
				query += "FROM user_table a,class_table b,rank_table c ";
				query += "where (a.id ='"+uid[m]+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if(rs.next()) {
				fellows[m][0] = rs.getString("id");			//������ ���
				fellows[m][1] = rs.getString("name");		//������ �̸�
				fellows[m][2] = rs.getString("ar_code");	//������ �����ڵ�
				fellows[m][3] = rs.getString("ar_name");	//������ ���޸�
				fellows[m][4] = rs.getString("ac_code");	//������ �μ��ڵ�
				fellows[m][5] = rs.getString("ac_name");	//������ �μ���
			} //if
		}//for

		stmt.close();
		rs.close();
	}

	/*********************************************************************
	 * �Ҹ���
	 *********************************************************************/
	public void close() throws Exception{
		connMgr.freeConnection("mssql",con);
	}

}

