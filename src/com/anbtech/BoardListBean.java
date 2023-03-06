package com.anbtech;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.util.*;

/*******************************************************************************
 * JSP���� �Խù� ����Ʈ�� �ۼ��ϱ� ���Ͽ� ����ϴ� �����ν�
 * �����͸� �Խù� ���·� �ۼ��ϱ� ���� �������� �Ӽ��� ������ �ִ�
 * �ڹٺ��̴�.
 ******************************************************************************/
public class BoardListBean
{
	// Database Wrapper Class ����
	private ViewQueryBean viewquery = null;
	private normalFormat fmt = null;		//data output format
	
	// ���̺� 
	private String table;
	// �÷�
	private String[] columns;
	// �˻� �ܾ�
	private String search_word;
	private String search_word_2;
	private String search_word_3;
	private String search_word_4;
	// �˻� �׸�
	private String search_item;
	private String search_item_2;
	private String search_item_3;
	private String search_item_4;
	
	// WHERE�� �ٷξ��� 
	private String where_data; 
		
	// ����Ű
	private String order_key;
	// ���� ����ϰ��� �ϴ� ������
	private int current_page_num;
	// �� �������� ����ϴ� ���ڵ��� ����
	private int no_rows = 10;
	// ����Ÿ �׸��� ������ true
	private boolean isEmpty;
	// ��ü ���ڵ� ��
	private int total_count;
	// ��ü ������ ��  
	private int total_page;
	// ���� �Խ��ǿ� ������ ���ڵ� ��
	private int showed_count = 0;
	//����ð��� ����
	private String request_date;
	//DB�� �ԷµǴ� ID�� ���ϴ� ����
	private String ID;
	//DB������ ���� �ѱ�ó�� ���� 
	private String hangul;

	private String sql;
	private String sql2;


	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public BoardListBean() 
	{	
		viewquery = new ViewQueryBean();
		viewquery.openConnection();
		fmt = new normalFormat();
	}

	/***************************************************************************
	 * �����ͺ��̽� ���̺� ���� �޼ҵ�
	 **************************************************************************/
	public void setTable(String table)
	{
		this.table = table;
	}

	/***************************************************************************
	 * ���̺� ���� �÷� ���� �޼ҵ�
	 **************************************************************************/
	public void setColumns(String[] columns)
	{
		this.columns = columns;		
	}

	/***************************************************************************
	 * ����� ������ �ö� ���Ĺ���� ������ �÷��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public void setOrder(String order_key)
	{
		this.order_key = order_key;
	}

	/***************************************************************************
	 * ������ �ϴ� �������� ��ȣ�� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public void setPage(int page)
	{
		this.current_page_num = page;
	}

	/***************************************************************************
	 * �˻��׸�� �˻�� �����ϴ� �޼ҵ�, �˻��׸��� DB �÷����̴�.
	 **************************************************************************/
	public void setSearch(String search_item, String search_word)
	{
		this.search_item = search_item;
		this.search_word = search_word;
	}
	public void setSearch(String search_item,String search_word,String search_item_2,String search_word_2)
	{
		this.search_item = search_item;
		this.search_word = search_word;
		this.search_item_2 = search_item_2;
		this.search_word_2 = search_word_2;		
	}
	public void setSearch(String search_item, String search_word,String search_item_2,String search_word_2,
							String search_item_3,String search_word_3)
	{
		this.search_item = search_item;
		this.search_word = search_word;
		this.search_item_2 = search_item_2;
		this.search_word_2 = search_word_2;
		this.search_item_3 = search_item_3;
		this.search_word_3 = search_word_3;				
	}
	public void setSearch(String search_item, String search_word,String search_item_2,String search_word_2,
							String search_item_3,String search_word_3,String search_item_4,String search_word_4)
	{
		this.search_item = search_item;
		this.search_word = search_word;
		this.search_item_2 = search_item_2;
		this.search_word_2 = search_word_2;
		this.search_item_3 = search_item_3;
		this.search_word_3 = search_word_3;	
		this.search_item_4 = search_item_4;
		this.search_word_4 = search_word_4;						
	}	
	
	
	/***************************************************************************
	 * search_item and word�� Clear��Ų��.
	 **************************************************************************/
	public void setClear()
	{			
		this.search_item_2 = "";
		this.search_item_3 = "";
		this.search_item_4 = "";					
	}
	
	/***************************************************************************
	 * where ���� ���� ǥ���ϱ� 
	 **************************************************************************/				
	public void setSearchWrite(String where_data)
	{
		this.where_data = where_data;	
	}
	

	/***************************************************************************
	 * �������� ���� �����ϴ� �޼���
	 **************************************************************************/
	public void setQuery(String sql, String sql2)
	{
		this.sql = sql;
		this.sql2 = sql2;
	}

	/***************************************************************************
	 * ���������� ����ϴ� ���ڵ��� �� ����
	 **************************************************************************/
	public void setRowNo(int no_rows)
	{
		this.no_rows = no_rows;
	}

	/***************************************************************************
	 * ���� ������Ű�� �޼ҵ��̴�. (��ü�� ������)
	 **************************************************************************/
	public void init()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. ��ü �����Ϳ� ���� ������ ��� ���� ��� ���ڵ��� ������ ���Ѵ�.
			//------------------------------------------------------------------
			initCount();

			//------------------------------------------------------------------
			// STEP 2. SQL �������� �����Ѵ�.
			//          (������ ��� �����͸� ���������� �����.)
			//------------------------------------------------------------------
			String query = makeQuery();

			//------------------------------------------------------------------
			// STEP 3. ���յ� SQL�������� �̿��Ͽ� �����ͺ��̽��� ���Ǹ� �Ѵ�.
			//          (������ ��� �����͸� �����Ѵ�.)
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. ������ ������ ��ȣ�� ���ڵ���� ResultSet�� �����͸� �̵���Ų��.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * ���� ������Ű�� �޼ҵ��̴�. (�ʿ��� �͸� ������)
	 **************************************************************************/
	public void init_unique()
	{
		try
		{

			//------------------------------------------------------------------
			// STEP 1. ��ü �����Ϳ� ���� ������ ��� ���� ��� ���ڵ��� ������ ���Ѵ�.
			//------------------------------------------------------------------
			initCount_unique();

			//------------------------------------------------------------------
			// STEP 2. SQL �������� �����Ѵ�.
			//          (ã���� �ϴ� ��Ȯ�� �����͸��� �����Ѵ�..)
			//------------------------------------------------------------------
			String query = makeQuery_unique();

			//------------------------------------------------------------------
			// STEP 3. ���յ� SQL�������� �̿��Ͽ� �����ͺ��̽��� ���Ǹ� �Ѵ�.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. ������ ������ ��ȣ�� ���ڵ���� ResultSet�� �����͸� �̵���Ų��.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();			
					
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	/***************************************************************************
	 * ���� ������Ű�� �޼ҵ��̴�. (�����Ѱ��� ������:Structure������)
	 **************************************************************************/
	public void init_like()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. ��ü �����Ϳ� ���� ������ ��� ���� ��� ���ڵ��� ������ ���Ѵ�.
			//------------------------------------------------------------------
			initCount();

			//------------------------------------------------------------------
			// STEP 2. SQL �������� �����Ѵ�.
			//          (ã���� �ϴ� ��Ȯ�� �����͸��� �����Ѵ�..)
			//------------------------------------------------------------------
			String query = makeQuery_like();

			//------------------------------------------------------------------
			// STEP 3. ���յ� SQL�������� �̿��Ͽ� �����ͺ��̽��� ���Ǹ� �Ѵ�.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. ������ ������ ��ȣ�� ���ڵ���� ResultSet�� �����͸� �̵���Ų��.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * ���� ������Ű�� �޼ҵ��̴�. (where���� ����ǥ���Ѱ��� �޾Ƽ� ó���Ѵ�)
	 **************************************************************************/
	public void init_write()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. ��ü �����Ϳ� ���� ������ ��� ���� ��� ���ڵ��� ������ ���Ѵ�.
			//------------------------------------------------------------------
			initCount_write();

			//------------------------------------------------------------------
			// STEP 2. SQL �������� �����Ѵ�.
			//          (ã���� �ϴ� ��Ȯ�� �����͸��� �����Ѵ�..)
			//------------------------------------------------------------------
			String query = makeQuery_write();

			//------------------------------------------------------------------
			// STEP 3. ���յ� SQL�������� �̿��Ͽ� �����ͺ��̽��� ���Ǹ� �Ѵ�.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. ������ ������ ��ȣ�� ���ڵ���� ResultSet�� �����͸� �̵���Ų��.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * ���ڵ��� ��ü ī��Ʈ�� ���ϱ� ���� �޼ҵ��̴�.
	 **************************************************************************/
	private void initCount() 
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// �˻�Ű�� ���� ������ ����
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE "+this.search_item+" LIKE upper('%"+this.search_word+"%')");
		}

		try 
		{
			// �����ͺ��̽��� ���Ǹ� �Ѵ�.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// ��ü ���ڵ��� ������ ���Ѵ�.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// ���ڵ��� ������ 0 �� ���� isEmpty �Ӽ��� true�� �����Ѵ�.
				if(total_count == 0) isEmpty = true;
				else isEmpty=false;
				
				// ������ �������� ��ȣ�� ���Ѵ�. ù�������� 1���� �����Ѵ�.
				this.total_page = total_count / no_rows + 1;
				if ( (this.total_count % no_rows) == 0 ) {
					this.total_page = this.total_page - 1;
				}
			}
		} 
		catch(SQLException e)
		{
		}
	}
	/***************************************************************************
	 * ���ڵ��� ��ü ī��Ʈ�� ���ϱ� ���� �޼ҵ��̴�.
	 **************************************************************************/
	private  void initCount_unique()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// �˻�Ű�� ���� ������ ����
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE "+this.search_item+" = '"+this.search_word+"'");
		}
		if (this.search_item_2 != null && !this.search_item_2.equals("")) {
			query.append(" and "+this.search_item_2 +" = '"+this.search_word_2 +"'");
		}
		if (this.search_item_3 != null && !this.search_item_3.equals("")) {
			query.append(" and "+this.search_item_3 +" = '"+this.search_word_3 +"'");
		}		
		if (this.search_item_4 != null && !this.search_item_4.equals("")) {
			query.append(" and "+this.search_item_4 +" = '"+this.search_word_4 +"'");
		}				
				
		try 
		{
			// �����ͺ��̽��� ���Ǹ� �Ѵ�.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// ��ü ���ڵ��� ������ ���Ѵ�.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// ���ڵ��� ������ 0 �� ���� isEmpty �Ӽ��� true�� �����Ѵ�.
				if(total_count==0) isEmpty = true;
				else isEmpty=false;

				// ������ �������� ��ȣ�� ���Ѵ�. ù�������� 1���� �����Ѵ�.
				this.total_page = total_count / no_rows + 1;
				if ( (this.total_count % no_rows) == 0 ) {
					this.total_page = this.total_page - 1;
				}
			}
		} 
		catch(SQLException e)
		{
		}
		
	}

	/***************************************************************************
	 * ���ڵ��� ��ü ī��Ʈ�� ���ϱ� ���� �޼ҵ��̴�.(whrer�� ��������)
	 **************************************************************************/
	private void initCount_write()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// �˻�Ű�� ���� ������ ����(order by ������ �� ������ ����)
		if (this.where_data != null && !this.where_data.equals("")) {
			String wd = this.where_data.toUpperCase();	//�빮�ڷ� 
			int wds = wd.indexOf("ORDER BY");
			int wde = wd.indexOf("ASC")+3;
			if(wde == 2) wde = wd.indexOf("DESC")+4;
			String n_wd = "";
			if(wds != -1) {
				n_wd = wd.substring(0,wds);
				n_wd += wd.substring(wde,wd.length());
			} else n_wd = this.where_data;	
			query.append(" " + n_wd + "");
		}

		try 
		{
			// �����ͺ��̽��� ���Ǹ� �Ѵ�.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// ��ü ���ڵ��� ������ ���Ѵ�.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// ���ڵ��� ������ 0 �� ���� isEmpty �Ӽ��� true�� �����Ѵ�.
				if(total_count == 0) isEmpty = true;
				else isEmpty=false;
				
				// ������ �������� ��ȣ�� ���Ѵ�. ù�������� 1���� �����Ѵ�.
				this.total_page = total_count / no_rows + 1;
				if ( (this.total_count % no_rows) == 0 ) {
					this.total_page = this.total_page - 1;
				}
			}
		} 
		catch(SQLException e)
		{
		}
	}


	/***************************************************************************
	 * �������� ���� ���� ó���� ��쿡 ��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public void initWithQuery()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. ��ü �����Ϳ� ���� ������ ��� ���� ��� ���ڵ��� ������ ���Ѵ�.
			//------------------------------------------------------------------
			initCountWithQuery();

			//------------------------------------------------------------------
			// STEP 2. �Ѿ�� SQL�������� �̿��Ͽ� �����ͺ��̽��� ���Ǹ� �Ѵ�.
			//------------------------------------------------------------------
			viewquery.executeQuery(this.sql);

			//------------------------------------------------------------------
			// STEP 4. ������ ������ ��ȣ�� ���ڵ���� ResultSet�� �����͸� �̵���Ų��.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{

		}
	}

	/***************************************************************************
	 * �������� ���� ���� ó���� ��쿡 ���ڵ��� ��ü ī��Ʈ�� ���ϱ� 
	 * ���� �޼ҵ��̴�.
	 **************************************************************************/
	private void initCountWithQuery()
	{
		StringBuffer query = new StringBuffer();
		query.append(this.sql2);

		try 
		{
			// �����ͺ��̽��� ���Ǹ� �Ѵ�.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// ��ü ���ڵ��� ������ ���Ѵ�.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// ���ڵ��� ������ 0 �� ���� isEmpty �Ӽ��� true�� �����Ѵ�.
				if(total_count==0) isEmpty = true;

				// ������ �������� ��ȣ�� ���Ѵ�. ù�������� 1���� �����Ѵ�.
				this.total_page = total_count / no_rows + 1;
				if ( (this.total_count % no_rows) == 0 ) {
					this.total_page = this.total_page - 1;
				}
			}
		} 
		catch(SQLException e)
		{
		}
	}

	/***************************************************************************
	 * ������ ��� ������ ������ �ùٸ� �������� ������ ���� �޼ҵ��̴�.
	 **************************************************************************/
	public String makeQuery()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		
		//----------------------------------------------------------------------
		// STEP 1. �ʵ尪 ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. ���̺� ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. �˻�Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE ");
			query.append( this.search_item + " LIKE upper('%" + this.search_word+"%')");
		}
		//----------------------------------------------------------------------
		// STEP 4. ����Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 5. ���յ� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		return query.toString();
	}

	/***************************************************************************
	 * ������ ��� ������ ������ �ùٸ� �������� ������ ���� �޼ҵ��̴�.
	 **************************************************************************/
	public String makeQuery_like()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT distinct ");
		
		//----------------------------------------------------------------------
		// STEP 1. �ʵ尪 ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. ���̺� ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. �˻�Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE ");
			query.append( this.search_item + " LIKE upper('%" + this.search_word+"%')");
		}
		//----------------------------------------------------------------------
		// STEP 4. ����Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 1. ���յ� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		return query.toString();
	}

	/***************************************************************************
	 * ������ ��� ������ ������ �ùٸ� �������� ������ ���� �޼ҵ��̴�.
	 *     (������ ��Ȯ�� ���� ������ �����.?
	 **************************************************************************/
	public String makeQuery_unique()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT distinct ");
		
		//----------------------------------------------------------------------
		// STEP 1. �ʵ尪 ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. ���̺� ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. �˻�Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE (");
			query.append( this.search_item + " = '" + this.search_word+"')");
		}
		if (this.search_item_2 != null && !this.search_item_2.equals("")) {
			query.append(" and (");
			query.append( this.search_item_2 + " = '" + this.search_word_2 +"')");
		}
		if (this.search_item_3 != null && !this.search_item_3.equals("")) {
			query.append(" and (");
			query.append( this.search_item_3 + " = '" + this.search_word_3 +"')");
		}
		if (this.search_item_4 != null && !this.search_item_4.equals("")) {
			query.append(" and (");
			query.append( this.search_item_4 + " = '" + this.search_word_4 +"')");
		}
		//----------------------------------------------------------------------
		// STEP 4. ����Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 5. ���յ� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		return query.toString();
	}
	
	/***************************************************************************
	 * ������ ��� ������ ������ �ùٸ� �������� ������ ���� �޼ҵ��̴�.
	 * (where�� ��������)
	 **************************************************************************/
	public String makeQuery_write()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		
		//----------------------------------------------------------------------
		// STEP 1. �ʵ尪 ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. ���̺� ������ ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. �˻�Ű�� ���� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		if (this.where_data != null && !this.where_data.equals("")) {
			query.append(" " + this.where_data + "");
		}

		//----------------------------------------------------------------------
		// STEP 4. ���յ� �������� �����Ѵ�.
		//----------------------------------------------------------------------
		return query.toString();
	}
	

	/***************************************************************************
	 * SQL���Ǹ� �����ͺ��̽��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	private void initData(String query) throws SQLException 
	{
		try {
			viewquery.executeQuery(query);
		} catch (SQLException e) {
		}
	}

	/***************************************************************************
	 * ������ ��������ȣ�� �����͸��� ���� ���Ͽ� ������ ���������� 
	 * ResultSet�� �����͸� �̵���Ű�� �޼ҵ�
	 **************************************************************************/
	private void getPoint_of_ResultSet() throws SQLException 
	{
		for (int i=1; i<current_page_num; i++) {
			for(int j=0; j<no_rows; j++) {
				viewquery.next();
			}
		}
//		System.out.println("current_page_num: "+ current_page_num);
	}

	/***************************************************************************
	 * ���ڵ� �� ���� ������ �̸��� �÷��� �ش��ϴ�  �����͸� String �������� 
	 * �����ϴ� �޼ҵ� ���� �����Ͱ� ������ ���鹮�ڿ� �����Ѵ�
	 **************************************************************************/
	public String getData(String columnName) throws SQLException 
	{	
		return viewquery.getData(columnName)!=null?viewquery.getData(columnName):"";
	}


	/***************************************************************************
	 * ���ڵ� �� ���� ������ �÷����� �����͸� String �������� �����ϴ� �޼ҵ�
	 * ���� �����Ͱ� ������ ���鹮�ڿ� �����Ѵ�
	 **************************************************************************/
	public String getData(int index) throws SQLException 
	{
		return viewquery.getData(index)!=null?viewquery.getData(index):"";
	}


	/***************************************************************************
	 * 1. ������ ������ ������ �������� ����� ��ȿ�� ���� true ���� �Ѵ�
	 * 2. ResultSet���� ����Ʈ�� ��������  �̵���Ų��.
	 **************************************************************************/
	public boolean isAvailable() throws SQLException 
	{	
		if(showed_count > no_rows) showed_count = 0;	
		if( viewquery!= null && viewquery.next() && showed_count++ < no_rows )
			return true;
		return false;
	}

	/***************************************************************************
	 * 1. ��� �����͸� ����Ѵ�.
	 * 2. ResultSet���� ����Ʈ�� ��������  �̵���Ų��.
	 **************************************************************************/
	public boolean isAll() throws SQLException 
	{

		try {
			if(viewquery != null && viewquery.next())	return true;
			else return false;
		}catch (SQLException e) { return false;}
	}
	
	/***************************************************************************
	 * �����ϴ� �����Ͱ� ���� ��� true �����ϴ� �޼ҵ�
	 **************************************************************************/
	public boolean isEmpty()
	{
		return isEmpty;
	}

	/***************************************************************************
	 * ���� ������ �������� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public int getCurrentPage()
	{
		return this.current_page_num;
	}

	/***************************************************************************
	 * �Խù��� �� �������� �����ϴ� �޼ҵ�
	 **************************************************************************/	
	public int getLastPage()
	{
		return this.total_page;
	}
	
	/***************************************************************************
	 * �Խù��� �� �������� �����ϴ� �޼ҵ�
	 **************************************************************************/	
	public int getTotalcount()
	{
		return this.total_count;
	}
	
	/***************************************************************************
	 * ���� �������� ������ ���ڵ��� ���ڸ� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public int getShowedCount()
	{
		return showed_count;
	}

	/***************************************************************************
	 * �˻��׸�(�����ͺ��̽� �ʵ��)�� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getSearchItem()
	{
		return this.search_item;
	}

	/***************************************************************************
	 * �˻�� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getSearchWord()
	{
		return this.search_word;
	}
	

	/***************************************************************************
	 * query�� �Ѻ�ǰ���� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public int getTotalCount()
	{
		return this.total_count;
	}
	
	/***************************************************************************
		�ϴ��� ���α׷� ���ۿ� �ʿ��� �κе� 
		�߰��� ������
	***************************************************************************/
	/***************************************************************************
	 * ��������� �����ϴ� �޼ҵ� (�Է�: yyyy, mm, dd)
	 * 1:�� 2:�� 3:ȭ 4:�� 5:�� 6:�� 7:��  
	 **************************************************************************/
	public int getDay(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.DAY_OF_WEEK); 
	}	
			 	
	/***************************************************************************
	 * �־��� ���ڸ�ŭ�� ���(���ϰų� ���ų�)�Ͽ� ��/��/���� �����ϴ� �޼ҵ� 
	 * �־��� ���ڷ� Setting (syear, smonth, sdate)
	 * �־��� ���ڸ�ŭ ���ڰ���ϱ� (date)
	 **************************************************************************/
	public String getDate(int syear,int smonth, int sdate, int date)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(cal.YEAR, syear);
		cal.set(cal.MONTH, smonth-1); 			//1���� 0���� �����ϹǷ� month-1
		cal.set(Calendar.DAY_OF_MONTH,sdate); 	//DAY_OF_MONTH�� 1�� ���� (���� ù���� ����)
		
		cal.add(Calendar.DATE,date);
		int Sdd = cal.get(cal.DAY_OF_MONTH);
		int Smm = cal.get(cal.MONTH)+1;
		int Syy = cal.get(cal.YEAR);

		//String���� �ٲٱ� 
		String Sy = Integer.toString(Syy);
		String Sm = "";
		if(Smm < 10) Sm = "0" + Integer.toString(Smm);
		else Sm = Integer.toString(Smm);
		String Sd = "";
		if(Sdd < 10) Sd = "0" + Integer.toString(Sdd);
		else Sd = Integer.toString(Sdd);

		return Sy + "/" + Sm + "/" + Sd; 
	}
		
	/***************************************************************************
	 * �־��� ���ڸ�ŭ�� ���(���ϰų� ���ų�)�Ͽ� ��/��/���� �����ϴ� �޼ҵ� 
	 * ���� : ���� ���� 
	 **************************************************************************/
	public String getDate(int date)
	{

		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE,date);
		
		int Sdd = now.get(now.DAY_OF_MONTH);
		int Smm = now.get(now.MONTH)+1;
		int Syy = now.get(now.YEAR);

		//String���� �ٲٱ� 
		String Sy = Integer.toString(Syy);
		String Sm = "";
		if(Smm < 10) Sm = "0" + Integer.toString(Smm);
		else Sm = Integer.toString(Smm);
		String Sd = "";
		if(Sdd < 10) Sd = "0" + Integer.toString(Sdd);
		else Sd = Integer.toString(Sdd);

		return Sy + "/" + Sm + "/" + Sd; 
	}	
			
	/***************************************************************************
	 * ����ð��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getCurrentTime()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		request_date = vans.format(now);
		return this.request_date;
	}	

	/***************************************************************************
	 * ����ð��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getTime()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		request_date = vans.format(now);
		return this.request_date;
	}	
	
	/***************************************************************************
	 * ����ð��� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getTimeNoformat()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		request_date = vans.format(now);
		return this.request_date;
	}	

	/***************************************************************************
	 * �־��� �⵵��ŭ ���Ͽ� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getYearNoformat(String y)
	{
		java.util.Date now = new java.util.Date();
		//������ �⵵�� ���ϱ�
		java.text.SimpleDateFormat s_year = new java.text.SimpleDateFormat("yyyy");
		String request_year = s_year.format(now);
		
		//������ ���ϸ� ���ϱ� 
		java.text.SimpleDateFormat s_day = new java.text.SimpleDateFormat("MMdd");
		String request_day = s_day.format(now);		
		
		//�־��� �⵵��ŭ ���ϱ�
		int get_year = Integer.parseInt(request_year);
		int rec_year = Integer.parseInt(y);
		int add_year = get_year + rec_year;
		
		//���ڸ� ���ڿ��� �ٲ۴�
		String cvt_year = Integer.toString(add_year);
		
		//���ڿ� ���ϱ� 
		request_date = cvt_year + request_day;
		
		return this.request_date;
	}	

	/***************************************************************************
	 * �־��� ����ŭ ���Ͽ� �����ϴ� �޼ҵ�
	 **************************************************************************/
	public String getMonthNoformat(String m)
	{
		java.util.Date now = new java.util.Date();
		//������ �⵵�� ���ϱ�
		java.text.SimpleDateFormat s_year = new java.text.SimpleDateFormat("yyyy");
		String request_year = s_year.format(now);
		
		//������ �� ���ϱ� 
		java.text.SimpleDateFormat s_mon = new java.text.SimpleDateFormat("MM");
		String request_mon = s_mon.format(now);	
			
		//������ �� ���ϱ� 
		java.text.SimpleDateFormat s_day = new java.text.SimpleDateFormat("dd");
		String request_day = s_day.format(now);		
		
		//�־��� �⵵��ŭ ���ϱ�
		int int_year = Integer.parseInt(request_year);	//�⵵
		int int_month = Integer.parseInt(request_mon);	//��
		int rec_month = Integer.parseInt(m);			//���� ��
		
		
		String cvt_mon = "";
		int add_month = int_month + rec_month;
		if(add_month > 12) {			//12���� ������ ���� 
			int_year = int_year + 1;
			add_month = add_month - 12;
			//���� ���ڸ��� �����ϱ� 
			cvt_mon = Integer.toString(add_month);
			if(cvt_mon.length() == 1)
				cvt_mon = "0" + cvt_mon;
		} else {
			cvt_mon = Integer.toString(add_month);
			if(cvt_mon.length() == 1)
				cvt_mon = "0" + cvt_mon;
			
		}
				
		//���ڿ� ���ϱ� 
		request_date = Integer.toString(int_year) + cvt_mon + request_day;
		
		return this.request_date;
	}	

	
	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ� (15�ڸ�)
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");		
		ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return this.ID;
	}
	
	/***************************************************************************
	 * MID�� ���ϴ� �޼ҵ� (19�ڸ�)
	 **************************************************************************/
	public String getMID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		com.anbtech.util.normalFormat ram = new com.anbtech.util.normalFormat("0000");	
		ID = y + fmt.toDigits(Integer.parseInt(s))+ram.toDigits((int)(Math.random()*3000));
		
		return this.ID;
	}

	/*******************************************************************************************
	*   SQL execute  (create, join, etc)
	*******************************************************************************************/	
	public void execute(String query) throws SQLException 
	{
		viewquery.execute(query);
	}

	/**************************************************************************
	// Transaction ó���ϱ�
	***************************************************************************/
	public void setAutoCommit(boolean flag) throws SQLException 
	{
		viewquery.setAutoCommit(flag);
	}

	public boolean getAutoCommit() throws SQLException 
	{
		return viewquery.getAutoCommit();
	}

	public void commit() throws SQLException 
	{
		viewquery.commit();
	}
	
	public void rollback() throws SQLException 
	{
		viewquery.rollback();
	}
	/**************************************************************************
	// Locking ó���ϱ�
	// Transaction�� ����Ǹ� �ڵ� ���������.
	***************************************************************************/
	public void setTransactionIsolation (int level) throws SQLException 
	{
		viewquery.setTransactionIsolation(level);
		/*
		if(level == 1) 
			viewquery.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		else if(level == 2) 
			viewquery.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		else if(level == 3) 
			viewquery.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		*/
	}

	public int getTransactionIsolation () throws SQLException 
	{
		return viewquery.getTransactionIsolation();
	}
	/***************************************************************************
	 * �ѱ��� DB������ ���� ��ȯ���ִ� �޼ҵ� : Incoding
	 **************************************************************************/
	public String toHanguelInc(String str, String dbkind) 
	{
		String HAN = str;			//��ȯ�� �ѱ� 
		String DBK = dbkind;		//DB���� 
		if(HAN == null) HAN = "";
		
		try {	
			if(HAN != null) {
				if(DBK.equals("MY"))
					HAN = new String(HAN.getBytes("euc-kr"),"ISO-8859-1");
				else if(DBK.equals("OR"))
					HAN = new String(HAN.getBytes("ISO-8859-1"),"euc-kr");
				return HAN;	
			}
			else return HAN;
		} catch (UnsupportedEncodingException e) {
			return "Encoding Error";	
		}
	}
	
	/***************************************************************************
	 * �ѱ��� DB������ ���� ��ȯ���ִ� �޼ҵ� : Decoding
	 **************************************************************************/
	public String toHanguelDec(String str, String dbkind) 
	{
		String HAN = str;			//��ȯ�� �ѱ� 
		String DBK = dbkind;		//DB���� 
		if(HAN == null) HAN = "";
				
		try {	
			if(HAN != null) {
				if(DBK.equals("MY"))
					HAN = new String(HAN.getBytes("ISO-8859-1"),"euc-kr");
				else if(DBK.equals("OR"))
					HAN = new String(HAN.getBytes("euc-kr"),"ISO-8859-1");
				return HAN;	
			}
			else return HAN;
		} catch (UnsupportedEncodingException e) {
			return "Encoding Error";	
		}
	}
	/***************************************************************************
	 * �������� ����ɶ� �ڿ��� ȸ���ϱ� ���� �޼ҵ� 
	 **************************************************************************/
	protected void finalize() throws Throwable 
	{
		if(viewquery != null) {
			viewquery.close();
		}
	}
}