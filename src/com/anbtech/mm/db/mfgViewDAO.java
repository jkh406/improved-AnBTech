package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgViewDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mfgViewDAO(Connection con) 
	{
		this.con = con;
	}
	
	//--------------------------------------------------------------------
	//
	//		������ ������� ���� �޼ҵ� ����
	//		[mfg_product_master ]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ ���ǰ������� ������ ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgProductMasterRunList (String factory_no,String sItem,String sWord,
		String start_date,String end_date,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		String query = "";	
		double order_count=0,total_count=0,good_count=0,bad_count=0;
		String product_rate="",good_rate="",bad_rate="";

		nfm.setFormat("0.0");
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		if(start_date.length() == 0) start_date = "0";
		if(end_date.length() == 0) end_date = anbdt.getDateNoformat();
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		query += "order by model_code,fg_code,output_date,item_code asc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgProductMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setOrderCount(Integer.parseInt(rs.getString("order_count")));
				table.setOrderUnit(rs.getString("order_unit"));
				table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
				table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
				table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setOutputStatus(rs.getString("output_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setOutputDate(anbdt.getSepDate(rs.getString("output_date"),"-"));
				table.setOpCode(rs.getString("op_code"));
				table.setOpName(rs.getString("op_name"));
				table.setOpNickname(rs.getString("op_nickname"));

				//���� ���ϱ�
				order_count = Double.parseDouble(rs.getString("order_count"));
				total_count = Double.parseDouble(rs.getString("total_count"));
				good_count = Double.parseDouble(rs.getString("good_count"));
				bad_count = Double.parseDouble(rs.getString("bad_count"));

				product_rate = nfm.DoubleToString(total_count/order_count * 100);
				good_rate = nfm.DoubleToString(good_count/total_count * 100);
				bad_rate = nfm.DoubleToString(bad_count/total_count * 100);

				table.setProductRate(product_rate);
				table.setGoodRate(good_rate);
				table.setBadRate(bad_rate);
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ������ ���ǰ������� ������ ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public mfgProductMasterTable getMfgProductMasterRunDisplayPage(String factory_no,String sItem,String sWord,
		String start_date,String end_date,String mode,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String para = mode+"&start_date="+start_date+"&end_date="+end_date;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		if(start_date.length() == 0) start_date = "0";
		if(end_date.length() == 0) end_date = anbdt.getDateNoformat();
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		query += "order by model_code,fg_code,output_date,item_code asc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mfgProductMasterTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ������ ��ǰ������� ������ ����Ʈ  : F�� ���۵Ǵ°͸�
	//*******************************************************************/	
	public ArrayList getMfgProductMasterList (String factory_no,String sItem,String sWord,
		String start_date,String end_date,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		String query = "";	
		double order_count=0,total_count=0,good_count=0,bad_count=0;
		String product_rate="",good_rate="",bad_rate="";

		nfm.setFormat("0.0");
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		if(start_date.length() == 0) start_date = "0";
		if(end_date.length() == 0) end_date = anbdt.getDateNoformat();
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and item_code like 'F%' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and item_code like 'F%' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		query += "order by model_code,fg_code,output_date asc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgProductMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setOrderCount(Integer.parseInt(rs.getString("order_count")));
				table.setOrderUnit(rs.getString("order_unit"));
				table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
				table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
				table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setOutputStatus(rs.getString("output_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setOutputDate(anbdt.getSepDate(rs.getString("output_date"),"-"));
				table.setOpCode(rs.getString("op_code"));
				table.setOpName(rs.getString("op_name"));
				table.setOpNickname(rs.getString("op_nickname"));

				//���� ���ϱ�
				order_count = Double.parseDouble(rs.getString("order_count"));
				total_count = Double.parseDouble(rs.getString("total_count"));
				good_count = Double.parseDouble(rs.getString("good_count"));
				bad_count = Double.parseDouble(rs.getString("bad_count"));

				product_rate = nfm.DoubleToString(total_count/order_count * 100);
				good_rate = nfm.DoubleToString(good_count/total_count * 100);
				bad_rate = nfm.DoubleToString(bad_count/total_count * 100);

				table.setProductRate(product_rate);
				table.setGoodRate(good_rate);
				table.setBadRate(bad_rate);
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ������ ��ǰ������� ������ ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ� F�� ���۵Ǵ°͸�
	//*******************************************************************/	
	public mfgProductMasterTable getMfgProductMasterDisplayPage(String factory_no,String sItem,String sWord,
		String start_date,String end_date,String mode,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String para = mode+"&start_date="+start_date+"&end_date="+end_date;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		if(start_date.length() == 0) start_date = "0";
		if(end_date.length() == 0) end_date = anbdt.getDateNoformat();
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and item_code like 'F%' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and item_code like 'F%' ";
		if(sItem.length() != 0) query += "and "+sItem+" like '%"+sWord+"%' ";
		query += "and output_date >= '"+start_date+"' and output_date <= '"+end_date+"' ";
		query += "order by model_code,fg_code,output_date asc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgViewServlet?&mode="+para+"&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mfgProductMasterTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	
	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	���� �ľ��ϱ� 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
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
	//	�־�������� �����������
	//*******************************************************************/
	public String[] getDivInfo(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code,b.ac_name from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("ac_code");
			data[1] = rs.getString("ac_name");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�־����ڵ��� ��ǰ������
	//*******************************************************************/
	public String[] getItemInfo(String item_code) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select item_name,item_desc from item_master where item_no='"+item_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "";
			data[1] = rs.getString("item_desc");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

}


