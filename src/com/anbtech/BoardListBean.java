package com.anbtech;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.util.*;

/*******************************************************************************
 * JSP에서 게시물 리스트를 작성하기 위하여 사용하는 빈으로써
 * 데이터를 게시물 형태로 작성하기 위한 여러가지 속성을 가지고 있는
 * 자바빈이다.
 ******************************************************************************/
public class BoardListBean
{
	// Database Wrapper Class 선언
	private ViewQueryBean viewquery = null;
	private normalFormat fmt = null;		//data output format
	
	// 테이블 
	private String table;
	// 컬럼
	private String[] columns;
	// 검색 단어
	private String search_word;
	private String search_word_2;
	private String search_word_3;
	private String search_word_4;
	// 검색 항목
	private String search_item;
	private String search_item_2;
	private String search_item_3;
	private String search_item_4;
	
	// WHERE절 바로쓰기 
	private String where_data; 
		
	// 정렬키
	private String order_key;
	// 현재 출력하고자 하는 페이지
	private int current_page_num;
	// 한 페이지에 출력하는 레코드의 갯수
	private int no_rows = 10;
	// 데이타 항목이 없으면 true
	private boolean isEmpty;
	// 전체 레코드 수
	private int total_count;
	// 전체 페이지 수  
	private int total_page;
	// 현재 게시판에 보여준 레코드 수
	private int showed_count = 0;
	//현재시간을 리턴
	private String request_date;
	//DB에 입력되는 ID를 구하는 변수
	private String ID;
	//DB저장을 위한 한글처리 변수 
	private String hangul;

	private String sql;
	private String sql2;


	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public BoardListBean() 
	{	
		viewquery = new ViewQueryBean();
		viewquery.openConnection();
		fmt = new normalFormat();
	}

	/***************************************************************************
	 * 데이터베이스 테이블 설정 메소드
	 **************************************************************************/
	public void setTable(String table)
	{
		this.table = table;
	}

	/***************************************************************************
	 * 테이블에 따른 컬럼 설정 메소드
	 **************************************************************************/
	public void setColumns(String[] columns)
	{
		this.columns = columns;		
	}

	/***************************************************************************
	 * 목록을 가지고 올때 정렬방식을 결정할 컬럼을 설정하는 메소드
	 **************************************************************************/
	public void setOrder(String order_key)
	{
		this.order_key = order_key;
	}

	/***************************************************************************
	 * 보고자 하는 페이지의 번호를 설정하는 메소드
	 **************************************************************************/
	public void setPage(int page)
	{
		this.current_page_num = page;
	}

	/***************************************************************************
	 * 검색항목과 검색어를 설정하는 메소드, 검색항목은 DB 컬럼명이다.
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
	 * search_item and word을 Clear시킨다.
	 **************************************************************************/
	public void setClear()
	{			
		this.search_item_2 = "";
		this.search_item_3 = "";
		this.search_item_4 = "";					
	}
	
	/***************************************************************************
	 * where 절을 직접 표현하기 
	 **************************************************************************/				
	public void setSearchWrite(String where_data)
	{
		this.where_data = where_data;	
	}
	

	/***************************************************************************
	 * 쿼리문을 직접 설정하는 메서드
	 **************************************************************************/
	public void setQuery(String sql, String sql2)
	{
		this.sql = sql;
		this.sql2 = sql2;
	}

	/***************************************************************************
	 * 한페이지에 출력하는 레코드의 수 설정
	 **************************************************************************/
	public void setRowNo(int no_rows)
	{
		this.no_rows = no_rows;
	}

	/***************************************************************************
	 * 빈을 구동시키는 메소드이다. (전체를 쿼리함)
	 **************************************************************************/
	public void init()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. 전체 데이터에 대한 정보를 얻기 위해 모든 레코드의 갯수를 구한다.
			//------------------------------------------------------------------
			initCount();

			//------------------------------------------------------------------
			// STEP 2. SQL 쿼리문을 조합한다.
			//          (유사한 모든 데이터를 쿼리문장을 만든다.)
			//------------------------------------------------------------------
			String query = makeQuery();

			//------------------------------------------------------------------
			// STEP 3. 조합된 SQL쿼리문을 이용하여 데이터베이스로 질의를 한다.
			//          (유사한 모든 데이터를 쿼리한다.)
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. 지정된 페이지 번호의 레코드까지 ResultSet의 포인터를 이동시킨다.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * 빈을 구동시키는 메소드이다. (필요한 것만 쿼리함)
	 **************************************************************************/
	public void init_unique()
	{
		try
		{

			//------------------------------------------------------------------
			// STEP 1. 전체 데이터에 대한 정보를 얻기 위해 모든 레코드의 갯수를 구한다.
			//------------------------------------------------------------------
			initCount_unique();

			//------------------------------------------------------------------
			// STEP 2. SQL 쿼리문을 조합한다.
			//          (찾고자 하는 정확한 데이터만을 쿼리한다..)
			//------------------------------------------------------------------
			String query = makeQuery_unique();

			//------------------------------------------------------------------
			// STEP 3. 조합된 SQL쿼리문을 이용하여 데이터베이스로 질의를 한다.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. 지정된 페이지 번호의 레코드까지 ResultSet의 포인터를 이동시킨다.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();			
					
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	
	/***************************************************************************
	 * 빈을 구동시키는 메소드이다. (유사한것을 쿼리함:Structure을위해)
	 **************************************************************************/
	public void init_like()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. 전체 데이터에 대한 정보를 얻기 위해 모든 레코드의 갯수를 구한다.
			//------------------------------------------------------------------
			initCount();

			//------------------------------------------------------------------
			// STEP 2. SQL 쿼리문을 조합한다.
			//          (찾고자 하는 정확한 데이터만을 쿼리한다..)
			//------------------------------------------------------------------
			String query = makeQuery_like();

			//------------------------------------------------------------------
			// STEP 3. 조합된 SQL쿼리문을 이용하여 데이터베이스로 질의를 한다.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. 지정된 페이지 번호의 레코드까지 ResultSet의 포인터를 이동시킨다.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * 빈을 구동시키는 메소드이다. (where절을 직접표현한겻을 받아서 처리한다)
	 **************************************************************************/
	public void init_write()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. 전체 데이터에 대한 정보를 얻기 위해 모든 레코드의 갯수를 구한다.
			//------------------------------------------------------------------
			initCount_write();

			//------------------------------------------------------------------
			// STEP 2. SQL 쿼리문을 조합한다.
			//          (찾고자 하는 정확한 데이터만을 쿼리한다..)
			//------------------------------------------------------------------
			String query = makeQuery_write();

			//------------------------------------------------------------------
			// STEP 3. 조합된 SQL쿼리문을 이용하여 데이터베이스로 질의를 한다.
			//------------------------------------------------------------------
			initData(query);

			//------------------------------------------------------------------
			// STEP 4. 지정된 페이지 번호의 레코드까지 ResultSet의 포인터를 이동시킨다.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	/***************************************************************************
	 * 레코드의 전체 카운트를 구하기 위한 메소드이다.
	 **************************************************************************/
	private void initCount() 
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// 검색키에 따른 쿼리문 조합
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE "+this.search_item+" LIKE upper('%"+this.search_word+"%')");
		}

		try 
		{
			// 데이터베이스로 질의를 한다.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// 전체 레코드의 갯수를 구한다.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// 레코드의 갯수가 0 일 경우는 isEmpty 속성을 true로 설정한다.
				if(total_count == 0) isEmpty = true;
				else isEmpty=false;
				
				// 마지막 페이지의 번호를 구한다. 첫페이지는 1부터 시작한다.
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
	 * 레코드의 전체 카운트를 구하기 위한 메소드이다.
	 **************************************************************************/
	private  void initCount_unique()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// 검색키에 따른 쿼리문 조합
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
			// 데이터베이스로 질의를 한다.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// 전체 레코드의 갯수를 구한다.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// 레코드의 갯수가 0 일 경우는 isEmpty 속성을 true로 설정한다.
				if(total_count==0) isEmpty = true;
				else isEmpty=false;

				// 마지막 페이지의 번호를 구한다. 첫페이지는 1부터 시작한다.
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
	 * 레코드의 전체 카운트를 구하기 위한 메소드이다.(whrer절 직접쓰기)
	 **************************************************************************/
	private void initCount_write()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT COUNT(*) FROM "+ this.table);

		// 검색키에 따른 쿼리문 조합(order by 문장을 뺀 나머지 조합)
		if (this.where_data != null && !this.where_data.equals("")) {
			String wd = this.where_data.toUpperCase();	//대문자로 
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
			// 데이터베이스로 질의를 한다.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// 전체 레코드의 갯수를 구한다.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// 레코드의 갯수가 0 일 경우는 isEmpty 속성을 true로 설정한다.
				if(total_count == 0) isEmpty = true;
				else isEmpty=false;
				
				// 마지막 페이지의 번호를 구한다. 첫페이지는 1부터 시작한다.
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
	 * 쿼리문을 직접 날려 처리할 경우에 빈즈를 구동하는 메소드
	 **************************************************************************/
	public void initWithQuery()
	{
		try
		{
			//------------------------------------------------------------------
			// STEP 1. 전체 데이터에 대한 정보를 얻기 위해 모든 레코드의 갯수를 구한다.
			//------------------------------------------------------------------
			initCountWithQuery();

			//------------------------------------------------------------------
			// STEP 2. 넘어온 SQL쿼리문을 이용하여 데이터베이스로 질의를 한다.
			//------------------------------------------------------------------
			viewquery.executeQuery(this.sql);

			//------------------------------------------------------------------
			// STEP 4. 지정된 페이지 번호의 레코드까지 ResultSet의 포인터를 이동시킨다.
			//------------------------------------------------------------------			
			getPoint_of_ResultSet();		
		}
		catch (Exception e)
		{

		}
	}

	/***************************************************************************
	 * 쿼리문을 직접 날려 처리할 경우에 레코드의 전체 카운트를 구하기 
	 * 위한 메소드이다.
	 **************************************************************************/
	private void initCountWithQuery()
	{
		StringBuffer query = new StringBuffer();
		query.append(this.sql2);

		try 
		{
			// 데이터베이스로 질의를 한다.
			viewquery.executeQuery(query.toString());

			if(viewquery.next()) {

				// 전체 레코드의 갯수를 구한다.
				this.total_count = Integer.parseInt(viewquery.getData(1));

				// 레코드의 갯수가 0 일 경우는 isEmpty 속성을 true로 설정한다.
				if(total_count==0) isEmpty = true;

				// 마지막 페이지의 번호를 구한다. 첫페이지는 1부터 시작한다.
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
	 * 지정된 모든 조건을 가지고 올바른 쿼리문을 조합해 내는 메소드이다.
	 **************************************************************************/
	public String makeQuery()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		
		//----------------------------------------------------------------------
		// STEP 1. 필드값 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. 테이블 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. 검색키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE ");
			query.append( this.search_item + " LIKE upper('%" + this.search_word+"%')");
		}
		//----------------------------------------------------------------------
		// STEP 4. 정렬키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 5. 조합된 쿼리문을 리턴한다.
		//----------------------------------------------------------------------
		return query.toString();
	}

	/***************************************************************************
	 * 지정된 모든 조건을 가지고 올바른 쿼리문을 조합해 내는 메소드이다.
	 **************************************************************************/
	public String makeQuery_like()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT distinct ");
		
		//----------------------------------------------------------------------
		// STEP 1. 필드값 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. 테이블 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. 검색키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if (this.search_item != null && !this.search_item.equals("")) {
			query.append(" WHERE ");
			query.append( this.search_item + " LIKE upper('%" + this.search_word+"%')");
		}
		//----------------------------------------------------------------------
		// STEP 4. 정렬키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 1. 조합된 쿼리문을 리턴한다.
		//----------------------------------------------------------------------
		return query.toString();
	}

	/***************************************************************************
	 * 지정된 모든 조건을 가지고 올바른 쿼리문을 조합해 내는 메소드이다.
	 *     (지정된 정확한 쿼리 문장을 만든다.?
	 **************************************************************************/
	public String makeQuery_unique()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT distinct ");
		
		//----------------------------------------------------------------------
		// STEP 1. 필드값 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. 테이블 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. 검색키에 따른 쿼리문을 조합한다.
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
		// STEP 4. 정렬키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if(this.order_key != null && !this.order_key.equals("")) {
			query.append(" ORDER BY " + this.order_key);
		}

		//----------------------------------------------------------------------
		// STEP 5. 조합된 쿼리문을 리턴한다.
		//----------------------------------------------------------------------
		return query.toString();
	}
	
	/***************************************************************************
	 * 지정된 모든 조건을 가지고 올바른 쿼리문을 조합해 내는 메소드이다.
	 * (where절 직접쓰기)
	 **************************************************************************/
	public String makeQuery_write()
	{
		StringBuffer query = new StringBuffer();
		query.append("SELECT ");
		
		//----------------------------------------------------------------------
		// STEP 1. 필드값 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		for(int i=0; i<this.columns.length; i++) {
			query.append(columns[i]);
			if(i!=this.columns.length-1) {
				query.append(", ");
			}
		}
		
		//----------------------------------------------------------------------
		// STEP 2. 테이블 설정에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		query.append(" FROM " + this.table);

		//----------------------------------------------------------------------
		// STEP 3. 검색키에 따른 쿼리문을 조합한다.
		//----------------------------------------------------------------------
		if (this.where_data != null && !this.where_data.equals("")) {
			query.append(" " + this.where_data + "");
		}

		//----------------------------------------------------------------------
		// STEP 4. 조합된 쿼리문을 리턴한다.
		//----------------------------------------------------------------------
		return query.toString();
	}
	

	/***************************************************************************
	 * SQL질의를 데이터베이스로 전송하는 메소드
	 **************************************************************************/
	private void initData(String query) throws SQLException 
	{
		try {
			viewquery.executeQuery(query);
		} catch (SQLException e) {
		}
	}

	/***************************************************************************
	 * 지정된 페이지번호의 데이터만을 보기 위하여 지정된 페이지까지 
	 * ResultSet의 포인터를 이동시키는 메소드
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
	 * 레코드 셋 에서 지정된 이름의 컬럼에 해당하는  데이터를 String 형식으로 
	 * 리턴하는 메소드 만약 데이터가 없으면 공백문자열 리턴한다
	 **************************************************************************/
	public String getData(String columnName) throws SQLException 
	{	
		return viewquery.getData(columnName)!=null?viewquery.getData(columnName):"";
	}


	/***************************************************************************
	 * 레코드 셋 에서 지정된 컬럼값의 데이터를 String 형식으로 리턴하는 메소드
	 * 만약 데이터가 없으면 공백문자열 리턴한다
	 **************************************************************************/
	public String getData(int index) throws SQLException 
	{
		return viewquery.getData(index)!=null?viewquery.getData(index):"";
	}


	/***************************************************************************
	 * 1. 지정된 페이지 내에서 데이터의 출력이 유효한 동안 true 리턴 한다
	 * 2. ResultSet에서 포인트를 다음으로  이동시킨다.
	 **************************************************************************/
	public boolean isAvailable() throws SQLException 
	{	
		if(showed_count > no_rows) showed_count = 0;	
		if( viewquery!= null && viewquery.next() && showed_count++ < no_rows )
			return true;
		return false;
	}

	/***************************************************************************
	 * 1. 모든 데이터를 출력한다.
	 * 2. ResultSet에서 포인트를 다음으로  이동시킨다.
	 **************************************************************************/
	public boolean isAll() throws SQLException 
	{

		try {
			if(viewquery != null && viewquery.next())	return true;
			else return false;
		}catch (SQLException e) { return false;}
	}
	
	/***************************************************************************
	 * 존재하는 데이터가 없을 경우 true 리턴하는 메소드
	 **************************************************************************/
	public boolean isEmpty()
	{
		return isEmpty;
	}

	/***************************************************************************
	 * 현재 설정된 페이지를 리턴하는 메소드
	 **************************************************************************/
	public int getCurrentPage()
	{
		return this.current_page_num;
	}

	/***************************************************************************
	 * 게시물의 총 페이지를 리턴하는 메소드
	 **************************************************************************/	
	public int getLastPage()
	{
		return this.total_page;
	}
	
	/***************************************************************************
	 * 게시물의 총 페이지를 리턴하는 메소드
	 **************************************************************************/	
	public int getTotalcount()
	{
		return this.total_count;
	}
	
	/***************************************************************************
	 * 현재 페이지상에 보여준 레코드의 숫자를 리턴하는 메소드
	 **************************************************************************/
	public int getShowedCount()
	{
		return showed_count;
	}

	/***************************************************************************
	 * 검색항목(데이터베이스 필드명)을 리턴하는 메소드
	 **************************************************************************/
	public String getSearchItem()
	{
		return this.search_item;
	}

	/***************************************************************************
	 * 검색어를 리턴하는 메소드
	 **************************************************************************/
	public String getSearchWord()
	{
		return this.search_word;
	}
	

	/***************************************************************************
	 * query한 총부품수를 리턴하는 메소드
	 **************************************************************************/
	public int getTotalCount()
	{
		return this.total_count;
	}
	
	/***************************************************************************
		하단은 프로그램 제작에 필요한 부분들 
		추가한 내용임
	***************************************************************************/
	/***************************************************************************
	 * 현재요일을 리턴하는 메소드 (입력: yyyy, mm, dd)
	 * 1:일 2:월 3:화 4:수 5:목 6:금 7:토  
	 **************************************************************************/
	public int getDay(int year,int month,int date)
	{
		GregorianCalendar gc=new GregorianCalendar(year, month -1 , date);
		return gc.get(gc.DAY_OF_WEEK); 
	}	
			 	
	/***************************************************************************
	 * 주어진 날자만큼을 계산(더하거나 빼거나)하여 년/월/일을 리턴하는 메소드 
	 * 주어진 날자로 Setting (syear, smonth, sdate)
	 * 주어진 날자만큼 날자계산하기 (date)
	 **************************************************************************/
	public String getDate(int syear,int smonth, int sdate, int date)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(cal.YEAR, syear);
		cal.set(cal.MONTH, smonth-1); 			//1월이 0부터 시작하므로 month-1
		cal.set(Calendar.DAY_OF_MONTH,sdate); 	//DAY_OF_MONTH를 1로 설정 (월의 첫날로 설정)
		
		cal.add(Calendar.DATE,date);
		int Sdd = cal.get(cal.DAY_OF_MONTH);
		int Smm = cal.get(cal.MONTH)+1;
		int Syy = cal.get(cal.YEAR);

		//String으로 바꾸기 
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
	 * 주어진 날자만큼을 계산(더하거나 빼거나)하여 년/월/일을 리턴하는 메소드 
	 * 기준 : 현재 날자 
	 **************************************************************************/
	public String getDate(int date)
	{

		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE,date);
		
		int Sdd = now.get(now.DAY_OF_MONTH);
		int Smm = now.get(now.MONTH)+1;
		int Syy = now.get(now.YEAR);

		//String으로 바꾸기 
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
	 * 현재시간을 리턴하는 메소드
	 **************************************************************************/
	public String getCurrentTime()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		request_date = vans.format(now);
		return this.request_date;
	}	

	/***************************************************************************
	 * 현재시간을 리턴하는 메소드
	 **************************************************************************/
	public String getTime()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		request_date = vans.format(now);
		return this.request_date;
	}	
	
	/***************************************************************************
	 * 현재시간을 리턴하는 메소드
	 **************************************************************************/
	public String getTimeNoformat()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		request_date = vans.format(now);
		return this.request_date;
	}	

	/***************************************************************************
	 * 주어진 년도만큼 더하여 리턴하는 메소드
	 **************************************************************************/
	public String getYearNoformat(String y)
	{
		java.util.Date now = new java.util.Date();
		//현재의 년도를 구하기
		java.text.SimpleDateFormat s_year = new java.text.SimpleDateFormat("yyyy");
		String request_year = s_year.format(now);
		
		//현재의 월일를 구하기 
		java.text.SimpleDateFormat s_day = new java.text.SimpleDateFormat("MMdd");
		String request_day = s_day.format(now);		
		
		//주어진 년도만큼 더하기
		int get_year = Integer.parseInt(request_year);
		int rec_year = Integer.parseInt(y);
		int add_year = get_year + rec_year;
		
		//숫자를 문자열로 바꾼다
		String cvt_year = Integer.toString(add_year);
		
		//문자열 더하기 
		request_date = cvt_year + request_day;
		
		return this.request_date;
	}	

	/***************************************************************************
	 * 주어진 월만큼 더하여 리턴하는 메소드
	 **************************************************************************/
	public String getMonthNoformat(String m)
	{
		java.util.Date now = new java.util.Date();
		//현재의 년도를 구하기
		java.text.SimpleDateFormat s_year = new java.text.SimpleDateFormat("yyyy");
		String request_year = s_year.format(now);
		
		//현재의 월 구하기 
		java.text.SimpleDateFormat s_mon = new java.text.SimpleDateFormat("MM");
		String request_mon = s_mon.format(now);	
			
		//현재의 일 구하기 
		java.text.SimpleDateFormat s_day = new java.text.SimpleDateFormat("dd");
		String request_day = s_day.format(now);		
		
		//주어진 년도만큼 더하기
		int int_year = Integer.parseInt(request_year);	//년도
		int int_month = Integer.parseInt(request_mon);	//월
		int rec_month = Integer.parseInt(m);			//받은 월
		
		
		String cvt_mon = "";
		int add_month = int_month + rec_month;
		if(add_month > 12) {			//12월을 넘으면 보상 
			int_year = int_year + 1;
			add_month = add_month - 12;
			//월을 두자리로 보상하기 
			cvt_mon = Integer.toString(add_month);
			if(cvt_mon.length() == 1)
				cvt_mon = "0" + cvt_mon;
		} else {
			cvt_mon = Integer.toString(add_month);
			if(cvt_mon.length() == 1)
				cvt_mon = "0" + cvt_mon;
			
		}
				
		//문자열 더하기 
		request_date = Integer.toString(int_year) + cvt_mon + request_day;
		
		return this.request_date;
	}	

	
	/***************************************************************************
	 * ID을 구하는 메소드 (15자리)
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
	 * MID을 구하는 메소드 (19자리)
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
	// Transaction 처리하기
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
	// Locking 처리하기
	// Transaction이 종료되면 자동 잠금해제됨.
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
	 * 한글을 DB종류에 따라 변환해주는 메소드 : Incoding
	 **************************************************************************/
	public String toHanguelInc(String str, String dbkind) 
	{
		String HAN = str;			//변환할 한글 
		String DBK = dbkind;		//DB종류 
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
	 * 한글을 DB종류에 따라 변환해주는 메소드 : Decoding
	 **************************************************************************/
	public String toHanguelDec(String str, String dbkind) 
	{
		String HAN = str;			//변환할 한글 
		String DBK = dbkind;		//DB종류 
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
	 * 웹서버가 종료될때 자원을 회수하기 위한 메소드 
	 **************************************************************************/
	protected void finalize() throws Throwable 
	{
		if(viewquery != null) {
			viewquery.close();
		}
	}
}