package com.anbtech.bs.db;

import com.anbtech.bs.entity.*;
import com.anbtech.bs.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class SalesConfigMgrDAO{
	private Connection con;

	public SalesConfigMgrDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * 레코드의 전체 개수를 구한다.
	 *******************************************************************/
		public int getTotalCount(String tablename, String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM " + tablename + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();
		return total_count;
	}

	/*********************************************
	 * 수주형태 등록 리스트 가져오기
	 *********************************************/
	public ArrayList getBookingTypeList() throws Exception{
		BookingTypeTable table;
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT * FROM bs_booking_type";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		ArrayList arry = new ArrayList();
		while(rs.next()){
			table = new BookingTypeTable();
			table.setMid(rs.getString("mid"));
			table.setOrderCode(rs.getString("order_code"));
			table.setOrderName(rs.getString("order_name"));
			table.setIsExport(rs.getString("is_export"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsEntry(rs.getString("is_entry"));
			table.setIsShipping(rs.getString("is_shipping"));
			table.setIsAutoShip(rs.getString("is_auto_ship"));
			table.setIsSale(rs.getString("is_sale"));
			table.setShippingType(rs.getString("shipping_type"));
			table.setIsUse(rs.getString("is_use"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/*********************************************
	 *  수주형태정보 가져오기
	 *********************************************/
	public BookingTypeTable getBookingTypeInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();

		String query = "SELECT * FROM bs_booking_type WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.bs.entity.BookingTypeTable();

			table.setMid(rs.getString("mid"));
			table.setOrderCode(rs.getString("order_code"));
			table.setOrderName(rs.getString("order_name"));
			table.setIsExport(rs.getString("is_export"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsEntry(rs.getString("is_entry"));
			table.setIsShipping(rs.getString("is_shipping"));
			table.setIsAutoShip(rs.getString("is_auto_ship"));
			table.setIsSale(rs.getString("is_sale"));
			table.setShippingType(rs.getString("shipping_type"));
			table.setIsUse(rs.getString("is_use"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 수주형태정보 저장
	 *********************************/
	 public void saveBookingTypeInfo(String order_code, String order_name, String is_export, String is_return, String is_entry, String is_shipping, String is_auto_ship, String is_sale, String shipping_type, String is_use) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "INSERT INTO bs_booking_type (order_code,order_name,is_export,is_return,is_entry,is_shipping,is_auto_ship,is_sale,shipping_type,is_use) VALUES (?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_code);
		pstmt.setString(2,order_name);
		pstmt.setString(3,is_export);
		pstmt.setString(4,is_return);
		pstmt.setString(5,is_entry);
		pstmt.setString(6,is_shipping);
		pstmt.setString(7,is_auto_ship);
		pstmt.setString(8,is_sale);
		pstmt.setString(9,shipping_type);
		pstmt.setString(10,is_use);
		
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*********************************
	 * 수주형태정보 수정
	 *********************************/
	 public void modifyBookingTypeInfo(String mid,String order_code, String order_name, String is_export, String is_return, String is_entry, String is_shipping, String is_auto_ship, String is_sale, String shipping_type, String is_use) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "UPDATE bs_booking_type SET  order_code=?,order_name=?,is_export=?,is_return=?,is_entry=?,is_shipping=?,is_auto_ship=?,is_sale=?,shipping_type=?,is_use=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_code);
		pstmt.setString(2,order_name);
		pstmt.setString(3,is_export);
		pstmt.setString(4,is_return);
		pstmt.setString(5,is_entry);
		pstmt.setString(6,is_shipping);
		pstmt.setString(7,is_auto_ship);
		pstmt.setString(8,is_sale);
		pstmt.setString(9,shipping_type);
		pstmt.setString(10,is_use);
		pstmt.setString(11,mid);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 수주형태정보 삭제
	 *********************************/
	 public void deleteBookingTypeInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM bs_booking_type WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 * 조건에 맞는 품목단가정보 가져오기
	 *********************************************/
	public ArrayList getItemUnitCostList(String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		SalesConfigMgrBO scBO = new SalesConfigMgrBO(con);
		ItemUnitCostTable table = new ItemUnitCostTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		String where = scBO.getWhereForList(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount("bs_item_unit_cost", where);
		int recNum = total;
		
		String query = "SELECT * FROM bs_item_unit_cost " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new ItemUnitCostTable();

			table.setMid(rs.getString("mid"));
			String item_code_link = "";
			item_code_link += "SalesConfigMgrServlet?mode=modify_item_unit_cost";
			item_code_link += "&item_code=" + rs.getString("item_code");
			item_code_link += "&mid=" + rs.getString("mid");
			item_code_link = "<a href='" + item_code_link + "'>" + rs.getString("item_code") + "</a>";
			table.setItemCode(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setSaleType(rs.getString("sale_type"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setMoneyType(rs.getString("money_type"));
			table.setSaleUnitCost(rs.getString("sale_unit_cost"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}


	/*********************************************
	 *  품목 단가정보 가져오기
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		String query = "SELECT * FROM bs_item_unit_cost WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.bs.entity.ItemUnitCostTable();

			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setSaleType(rs.getString("sale_type"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setMoneyType(rs.getString("money_type"));
			table.setSaleUnitCost(rs.getString("sale_unit_cost"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 품목 단가정보 등록
	 *********************************/
	 public void saveItemUnitCostInfo(String  item_code, String item_name, String sale_type, String approval_type, String apply_date, String sale_unit, String money_type, String sale_unit_cost) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "INSERT INTO bs_item_unit_cost (item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost) VALUES (?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,sale_type);
		pstmt.setString(4,approval_type);
		pstmt.setString(5,apply_date);
		pstmt.setString(6,sale_unit);
		pstmt.setString(7,money_type);
		pstmt.setDouble(8,Double.parseDouble(sale_unit_cost));
		
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*********************************
	 * 품목 단가정보 수정
	 *********************************/
	 public void modifyItemUnitCostInfo(String mid, String item_code, String item_name, String sale_type, String approval_type, String apply_date, String sale_unit, String money_type, String sale_unit_cost) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "UPDATE bs_item_unit_cost  SET  item_code=?, item_name=?, sale_type=?, approval_type=?, apply_date=?, sale_unit=?, money_type=?, sale_unit_cost=? WHERE mid = ?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,sale_type);
		pstmt.setString(4,approval_type);
		pstmt.setString(5,apply_date);
		pstmt.setString(6,sale_unit);
		pstmt.setString(7,money_type);
		pstmt.setDouble(8,Double.parseDouble(sale_unit_cost));
		pstmt.setString(9,mid);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 품목 단가정보 삭제
	 *********************************/
	 public void deleteItemUnitCostInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM bs_item_unit_cost WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 * 조건에 맞는 고객별 품목단가정보 가져오기
	 *********************************************/
	public ArrayList getItemUnitCostByCustomerList(String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		SalesConfigMgrBO scBO = new SalesConfigMgrBO(con);
		ItemUnitCostTable table = new ItemUnitCostTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		String where = scBO.getWhereForList(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount("bs_item_unit_cost_by_customer", where);
		int recNum = total;
		
		String query = "SELECT * FROM bs_item_unit_cost_by_customer " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new ItemUnitCostTable();

			table.setMid(rs.getString("mid"));
			String item_code_link = "";
			item_code_link += "SalesConfigMgrServlet?mode=modify_item_unit_cost_by_customer";
			item_code_link += "&item_code=" + rs.getString("item_code");
			item_code_link += "&mid=" + rs.getString("mid");
			item_code_link = "<a href='" + item_code_link + "'>" + rs.getString("item_code") + "</a>";
			table.setItemCode(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setSaleType(rs.getString("sale_type"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setMoneyType(rs.getString("money_type"));
			table.setSaleUnitCost(rs.getString("sale_unit_cost"));
			table.setCustomerCode(rs.getString("customer_code"));
			table.setCustomerName(rs.getString("customer_name"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*********************************************
	 *  고객별 품목 단가정보 가져오기
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostByCustomerInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		String query = "SELECT * FROM bs_item_unit_cost_by_customer WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.bs.entity.ItemUnitCostTable();

			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setSaleType(rs.getString("sale_type"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setMoneyType(rs.getString("money_type"));
			table.setSaleUnitCost(rs.getString("sale_unit_cost"));
			table.setCustomerCode(rs.getString("customer_code"));
			table.setCustomerName(rs.getString("customer_name"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 고객별 품목 단가정보 등록
	 *********************************/
	 public void saveItemUnitCostByCustomerInfo(String  item_code, String item_name, String sale_type, String approval_type, String apply_date, String sale_unit, String money_type, String sale_unit_cost, String customer_code, String customer_name) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "INSERT INTO bs_item_unit_cost_by_customer  (item_code,item_name,sale_type,approval_type,apply_date,sale_unit,money_type,sale_unit_cost,customer_code,customer_name) VALUES (?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,sale_type);
		pstmt.setString(4,approval_type);
		pstmt.setString(5,apply_date);
		pstmt.setString(6,sale_unit);
		pstmt.setString(7,money_type);
		pstmt.setDouble(8,Double.parseDouble(sale_unit_cost));
		pstmt.setString(9,customer_code);
		pstmt.setString(10,customer_name);
		
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*********************************
	 * 고객별 품목 단가정보 수정
	 *********************************/
	 public void modifyItemUnitCostByCustomerInfo(String mid, String item_code, String item_name, String sale_type, String approval_type, String apply_date, String sale_unit, String money_type, String sale_unit_cost, String customer_code, String customer_name) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "UPDATE bs_item_unit_cost_by_customer  SET  item_code=?, item_name=?, sale_type=?, approval_type=?, apply_date=?, sale_unit=?, money_type=?, sale_unit_cost=?, customer_code=?, customer_name=? WHERE mid = ?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,sale_type);
		pstmt.setString(4,approval_type);
		pstmt.setString(5,apply_date);
		pstmt.setString(6,sale_unit);
		pstmt.setString(7,money_type);
		pstmt.setDouble(8,Double.parseDouble(sale_unit_cost));
		pstmt.setString(9,customer_code);
		pstmt.setString(10,customer_name);
		pstmt.setString(11,mid);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 고객별 품목 단가정보 삭제
	 *********************************/
	 public void deleteItemUnitCostByCustomerInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM bs_item_unit_cost_by_customer WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}


	/*********************************************
	 * 조건에 맞는 품목할증정보 가져오기
	 *********************************************/
	public ArrayList getItemPremiumList(String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		SalesConfigMgrBO scBO = new SalesConfigMgrBO(con);
		ItemPremiumTable table = new ItemPremiumTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		String where = scBO.getWhereForList(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount("bs_item_premium", where);
		int recNum = total;
		
		String query = "SELECT * FROM bs_item_premium " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new ItemPremiumTable();

			table.setMid(rs.getString("mid"));
			String item_code_link = "";
			item_code_link += "SalesConfigMgrServlet?mode=modify_item_premium";
			item_code_link += "&item_code=" + rs.getString("item_code");
			item_code_link += "&mid=" + rs.getString("mid");
			item_code_link = "<a href='" + item_code_link + "'>" + rs.getString("item_code") + "</a>";
			table.setItemCode(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setPremiumType(rs.getString("premium_type"));
			table.setPremiumName(rs.getString("premium_name"));
			table.setPremiumStandardQuantity(rs.getString("premium_standard_quantity"));
			table.setPremiumValue(rs.getString("premium_value"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*********************************************
	 *  품목할증정보 가져오기
	 *********************************************/
	public ItemPremiumTable getItemPremiumInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		String query = "SELECT * FROM bs_item_premium WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.bs.entity.ItemPremiumTable();

			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setPremiumType(rs.getString("premium_type"));
			table.setPremiumName(rs.getString("premium_name"));
			table.setPremiumStandardQuantity(rs.getString("premium_standard_quantity"));
			table.setPremiumValue(rs.getString("premium_value"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 품목할증정보 등록
	 *********************************/
	 public void saveItemPremiumInfo(String item_code,String item_name,String approval_type,String apply_date,String sale_unit,String premium_type,String premium_name,String premium_standard_quantity,String premium_value) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "INSERT INTO bs_item_premium  (item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value) VALUES (?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,approval_type);
		pstmt.setString(4,apply_date);
		pstmt.setString(5,sale_unit);
		pstmt.setString(6,premium_type);
		pstmt.setString(7,premium_name);
		pstmt.setString(8,premium_standard_quantity);
		pstmt.setString(9,premium_value);
		
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*********************************
	 * 품목할증정보 수정
	 *********************************/
	 public void modifyItemPremiumInfo(String mid, String item_code,String item_name,String approval_type,String apply_date,String sale_unit,String premium_type,String premium_name,String premium_standard_quantity,String premium_value) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "UPDATE bs_item_premium SET  item_code=?, item_name=?, approval_type=?, apply_date=?, sale_unit=?, premium_type=?, premium_name=?, premium_standard_quantity=?, premium_value=? WHERE mid = ?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,approval_type);
		pstmt.setString(4,apply_date);
		pstmt.setString(5,sale_unit);
		pstmt.setString(6,premium_type);
		pstmt.setString(7,premium_name);
		pstmt.setString(8,premium_standard_quantity);
		pstmt.setString(9,premium_value);
		pstmt.setString(10,mid);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 품목할증정보 삭제
	 *********************************/
	 public void deleteItemPremiumInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM bs_item_premium WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 * 조건에 맞는 고객별 품목할증정보 가져오기
	 *********************************************/
	public ArrayList getItemPremiumListByCustomer(String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		SalesConfigMgrBO scBO = new SalesConfigMgrBO(con);
		ItemPremiumTable table = new ItemPremiumTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		String where = scBO.getWhereForList(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount("bs_item_premium_by_customer", where);
		int recNum = total;
		
		String query = "SELECT * FROM bs_item_premium_by_customer " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new ItemPremiumTable();

			table.setMid(rs.getString("mid"));
			String item_code_link = "";
			item_code_link += "SalesConfigMgrServlet?mode=modify_item_premium_by_customer";
			item_code_link += "&item_code=" + rs.getString("item_code");
			item_code_link += "&mid=" + rs.getString("mid");
			item_code_link = "<a href='" + item_code_link + "'>" + rs.getString("item_code") + "</a>";
			table.setItemCode(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setPremiumType(rs.getString("premium_type"));
			table.setPremiumName(rs.getString("premium_name"));
			table.setPremiumStandardQuantity(rs.getString("premium_standard_quantity"));
			table.setPremiumValue(rs.getString("premium_value"));
			table.setCustomerCode(rs.getString("customer_code"));
			table.setCustomerName(rs.getString("customer_name"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*********************************************
	 *  고객별 품목할증정보 가져오기
	 *********************************************/
	public ItemPremiumTable getItemPremiumByCustomerInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		String query = "SELECT * FROM bs_item_premium_by_customer WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.bs.entity.ItemPremiumTable();

			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApplyDate(rs.getString("apply_date"));
			table.setSaleUnit(rs.getString("sale_unit"));
			table.setPremiumType(rs.getString("premium_type"));
			table.setPremiumName(rs.getString("premium_name"));
			table.setPremiumStandardQuantity(rs.getString("premium_standard_quantity"));
			table.setPremiumValue(rs.getString("premium_value"));
			table.setCustomerCode(rs.getString("customer_code"));
			table.setCustomerName(rs.getString("customer_name"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 고객별 품목할증정보 등록
	 *********************************/
	 public void saveItemPremiumByCustomerInfo(String item_code,String item_name,String approval_type,String apply_date,String sale_unit,String premium_type,String premium_name,String premium_standard_quantity,String premium_value,String customer_code, String customer_name) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "INSERT INTO bs_item_premium_by_customer  (item_code,item_name,approval_type,apply_date,sale_unit,premium_type,premium_name,premium_standard_quantity,premium_value,customer_code, customer_name) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,approval_type);
		pstmt.setString(4,apply_date);
		pstmt.setString(5,sale_unit);
		pstmt.setString(6,premium_type);
		pstmt.setString(7,premium_name);
		pstmt.setString(8,premium_standard_quantity);
		pstmt.setString(9,premium_value);
		pstmt.setString(10,customer_code);
		pstmt.setString(11,customer_name);
		
		pstmt.executeUpdate();
		pstmt.close();
	}


	/*********************************
	 * 고객별 품목할증정보 수정
	 *********************************/
	 public void modifyItemPremiumByCustomerInfo(String mid, String item_code,String item_name,String approval_type,String apply_date,String sale_unit,String premium_type,String premium_name,String premium_standard_quantity,String premium_value,String customer_code,String customer_name) throws Exception{
		
		PreparedStatement pstmt = null;

		String query = "UPDATE bs_item_premium_by_customer SET  item_code=?, item_name=?, approval_type=?, apply_date=?, sale_unit=?, premium_type=?, premium_name=?, premium_standard_quantity=?, premium_value=?, customer_code=?, customer_name=? WHERE mid = ?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,approval_type);
		pstmt.setString(4,apply_date);
		pstmt.setString(5,sale_unit);
		pstmt.setString(6,premium_type);
		pstmt.setString(7,premium_name);
		pstmt.setString(8,premium_standard_quantity);
		pstmt.setString(9,premium_value);
		pstmt.setString(10,customer_code);
		pstmt.setString(11,customer_name);
		pstmt.setString(12,mid);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 고객별 품목할증정보 삭제
	 *********************************/
	 public void deleteItemPremiumByCustomerInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM bs_item_premium_by_customer WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}

}		