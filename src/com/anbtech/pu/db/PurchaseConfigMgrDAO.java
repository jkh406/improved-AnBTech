package com.anbtech.pu.db;

import com.anbtech.pu.entity.*;
import com.anbtech.st.entity.*;
import com.anbtech.pu.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class PurchaseConfigMgrDAO{
	private Connection con;

	public PurchaseConfigMgrDAO(Connection con){
		this.con = con;
	}

	/*********************************************
	 * 선택된 거래(입고/출고)형태 정보 가져오기
	 * input : mid -> pu_inout_type 관리번호
	 *********************************************/
	public InOutTypeTable getInOutTypeInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.pu.entity.InOutTypeTable table = new com.anbtech.pu.entity.InOutTypeTable();

		String sql = "SELECT * FROM pu_inout_type WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setType(rs.getString("type"));
			table.setName(rs.getString("name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsEnter(rs.getString("is_enter"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsSageup(rs.getString("is_sageup"));
			table.setIsUsing(rs.getString("is_using"));
			table.setStockType(rs.getString("stock_type"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	
	/*********************************************
	 * 선택된 발주번호 및 발주 품목코드에 해당하는 정보 가져오기
	 *********************************************/
	public OrderTypeTable getPuOrderTypeInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		OrderTypeTable table = new OrderTypeTable();

		String sql = "SELECT * FROM pu_order_type WHERE mid = '" + mid +"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setOrderType(rs.getString("order_type"));
			table.setOrderName(rs.getString("order_name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsShipping(rs.getString("is_shipping"));
			table.setIsPass(rs.getString("is_pass"));
			table.setIsEnter(rs.getString("is_enter"));
			table.setIsPurchase(rs.getString("is_purchase"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsSageup(rs.getString("is_sageup"));
			table.setIsUsing(rs.getString("is_using"));
			table.setEnterCode(rs.getString("enter_code"));
			table.setEnterName(rs.getString("enter_name"));
			table.setOutgoCode(rs.getString("outgo_code"));
			table.setOutgoName(rs.getString("outgo_name"));
			table.setPurchaseCode(rs.getString("purchase_code"));
			table.setPurchaseName(rs.getString("purchase_name"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 거래(입고/출고)형태정보 List 가져오기
	 *********************************************/
	public ArrayList getInOutTypeList() throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.pu.entity.InOutTypeTable table;

		String query = "SELECT * FROM pu_inout_type";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.pu.entity.InOutTypeTable();
			table.setMid(rs.getString("mid"));
			table.setType(rs.getString("type"));
			table.setName(rs.getString("name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsEnter(rs.getString("is_enter"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsSageup(rs.getString("is_sageup"));
			table.setIsUsing(rs.getString("is_using"));
			table.setStockType(rs.getString("stock_type"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}


	/*********************************
	 * 입/출고형태 관리정보 저장
	 *********************************/
	 public void saveInOutTypeInfo(String type,String name,String is_import,String is_enter,String is_return,String is_sageup,String is_using,String stock_type) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_inout_type (type,name,is_import,is_enter,is_return,is_sageup,is_using,stock_type) VALUES (?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,type);
		pstmt.setString(2,name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_enter);
		pstmt.setString(5,is_return);
		pstmt.setString(6,is_sageup);
		pstmt.setString(7,is_using);
		pstmt.setString(8,stock_type);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 입/출고형태 관리정보 Update
	 *********************************/
	 public void updateInOutTypeInfo(String mid,String type,String name,String is_import,String is_enter,String is_return,String is_sageup,String is_using,String stock_type) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_inout_type SET type=?,name=?,is_import=?,is_enter=?,is_return=?,is_sageup=?,is_using=?,stock_type=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,type);
		pstmt.setString(2,name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_enter);
		pstmt.setString(5,is_return);
		pstmt.setString(6,is_sageup);
		pstmt.setString(7,is_using);
		pstmt.setString(8,stock_type);
		pstmt.setString(9,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 입/출고형태 관리정보 삭제
	 *********************************/
	 public void deleteInOutTypeInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM pu_inout_type WHERE mid='"+mid+"'";
		//System.out.println(query);
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 * 발주형태정보 List 가져오기
	 *********************************************/
	public ArrayList getOrderTypeList() throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.pu.entity.OrderTypeTable table;

		String query = "SELECT * FROM pu_order_type";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.pu.entity.OrderTypeTable();
			table.setMid(rs.getString("mid"));
			table.setOrderType(rs.getString("order_type"));
			table.setOrderName(rs.getString("order_name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsEnter(rs.getString("is_enter"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsSageup(rs.getString("is_sageup"));
			table.setIsUsing(rs.getString("is_using"));
			table.setIsShipping(rs.getString("is_shipping"));
			table.setIsPass(rs.getString("is_pass"));
			table.setIsPurchase(rs.getString("is_purchase"));
			table.setEnterCode(rs.getString("enter_code"));
			table.setEnterName(rs.getString("enter_name"));
			table.setOutgoCode(rs.getString("outgo_code"));
			table.setOutgoName(rs.getString("outgo_name"));
			table.setPurchaseCode(rs.getString("purchase_code"));
			table.setPurchaseName(rs.getString("purchase_name"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/*********************************
	 * 발주형태 관리정보 저장
	 *********************************/
	 public void saveOrderTypeInfo(String type,String name,String is_import,String is_shipping,String is_pass,String is_enter,String is_purchase,String is_return,String is_sageup,String is_using,String enter_code,String enter_name,String outgo_code,String outgo_name,String purchase_code,String purchase_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_order_type (order_type,order_name,is_import,is_shipping,is_pass,is_enter,is_purchase,is_return,is_sageup,is_using,enter_code,enter_name,outgo_code,outgo_name,purchase_code,purchase_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,type);
		pstmt.setString(2,name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_shipping);
		pstmt.setString(5,is_pass);
		pstmt.setString(6,is_enter);
		pstmt.setString(7,is_purchase);
		pstmt.setString(8,is_return);
		pstmt.setString(9,is_sageup);
		pstmt.setString(10,is_using);
		pstmt.setString(11,enter_code);
		pstmt.setString(12,enter_name);
		pstmt.setString(13,outgo_code);
		pstmt.setString(14,outgo_name);
		pstmt.setString(15,purchase_code);
		pstmt.setString(16,purchase_name);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 발주형태 관리정보 Update
	 *********************************/
	 public void updateOrderTypeInfo(String mid,String order_type,String order_name,String is_import,String is_shipping,String is_pass,String is_enter,String is_purchase,String is_return,String is_sageup,String is_using,String enter_code,String enter_name,String outgo_code,String outgo_name,String purchase_code,String purchase_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_order_type SET order_type=?,order_name=?,is_import=?,is_enter=?,is_return=?,is_sageup=?,is_using=?,is_shipping=?,is_pass=?,is_purchase=?,enter_code=?,enter_name=?,outgo_code=?,outgo_name=?,purchase_code=?,purchase_name=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_type);
		pstmt.setString(2,order_name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_enter);
		pstmt.setString(5,is_return);
		pstmt.setString(6,is_sageup);
		pstmt.setString(7,is_using);
		pstmt.setString(8,is_shipping);
		pstmt.setString(9,is_pass);
		pstmt.setString(10,is_purchase);
		pstmt.setString(11,enter_code);
		pstmt.setString(12,enter_name);
		pstmt.setString(13,outgo_code);
		pstmt.setString(14,outgo_name);
		pstmt.setString(15,purchase_code);
		pstmt.setString(16,purchase_name);
		pstmt.setString(17,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 발주형태 관리정보 삭제
	 *********************************/
	 public void deleteOrderTypeInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM pu_order_type WHERE mid='"+mid+"'";
		//System.out.println(query);
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 * 매입형태정보 List 가져오기
	 *********************************************/
	public ArrayList getPurchaseTypeList() throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.pu.entity.PurchaseTypeTable table;

		String query = "SELECT * FROM pu_purchase_type";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.pu.entity.PurchaseTypeTable();
			table.setMid(rs.getString("mid"));
			table.setPurchaseType(rs.getString("purchase_type"));
			table.setPurchaseName(rs.getString("purchase_name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsExcept(rs.getString("is_except"));
			table.setIsUsing(rs.getString("is_using"));
			table.setAccountType(rs.getString("account_type"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}


	/*********************************
	 * 매입형태 관리정보 저장
	 *********************************/
	 public void savePurchaseTypeInfo(String type,String name,String is_import,String is_return,String is_using,String is_except, String account_type) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_purchase_type (purchase_type,purchase_name,is_import,is_return,is_using,is_except,account_type) VALUES (?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,type);
		pstmt.setString(2,name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_return);
		pstmt.setString(5,is_using);
		pstmt.setString(6,is_except);
		pstmt.setString(7,account_type);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 매입형태 관리정보 Update
	 *********************************/
	 public void updatePurchaseTypeInfo(String mid,String type,String name,String is_import,String is_return,String is_using,String is_except, String account_type) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_purchase_type SET purchase_type=?,purchase_name=?,is_import=?,is_return=?,is_using=?,is_except=?,account_type=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,type);
		pstmt.setString(2,name);
		pstmt.setString(3,is_import);
		pstmt.setString(4,is_return);
		pstmt.setString(5,is_using);
		pstmt.setString(6,is_except);
		pstmt.setString(7,account_type);
		pstmt.setString(8,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************************
	 * 선택된 발주번호 및 발주 품목코드에 해당하는 정보 가져오기
	 *********************************************/
	public PurchaseTypeTable getPurchaseTypeInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PurchaseTypeTable table = new PurchaseTypeTable();

		String sql = "SELECT * FROM pu_purchase_type WHERE mid = '" + mid +"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
	
			table.setMid(rs.getString("mid"));
			table.setPurchaseType(rs.getString("purchase_type"));
			table.setPurchaseName(rs.getString("purchase_name"));
			table.setIsImport(rs.getString("is_import"));
			table.setIsReturn(rs.getString("is_return"));
			table.setIsExcept(rs.getString("is_except"));
			table.setIsUsing(rs.getString("is_using"));
			table.setAccountType(rs.getString("account_type"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 매입형태 관리정보 삭제
	 *********************************/
	 public void deletePurchaseTypeInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM pu_purchase_type WHERE mid='"+mid+"'";
		//System.out.println(query);
		st.executeUpdate(query);
		st.close();
	}


	/*********************************************
	 * 특정 품목을 공급하는 공급처 List 가져오기
	 *********************************************/
	public ArrayList getItemSupplyInfoList(String item_code) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		ItemSupplyInfoTable table;

		String query = "SELECT a.*,b.name_kor,b.name_eng FROM pu_item_supply_info a,company_customer b WHERE a.supplyer_code = b.company_no AND item_code = '" + item_code + "' AND is_trade_now = 'y'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new ItemSupplyInfoTable();
			
			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("name_kor")+"/"+rs.getString("name_eng"));
			table.setOrderWeight(rs.getString("order_weight"));
			table.setLeadTime(rs.getString("lead_time"));
			table.setIsTradeNow(rs.getString("is_trade_now"));
			table.setIsMainSupplyer(rs.getString("is_main_supplyer"));
			table.setMinOrderQuantity(rs.getString("min_order_quantity"));
			table.setMaxOrderQuantity(rs.getString("max_order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setSupplyerItemCode(rs.getString("supplyer_item_code"));
			table.setSupplyerItemName(rs.getString("supplyer_item_name"));
			table.setSupplyerItemDesc(rs.getString("supplyer_item_desc"));
			table.setMakerName(rs.getString("maker_name"));
			table.setSupplyUnitCost(rs.getString("supply_unit_cost"));
			table.setRequestUnitCost(rs.getString("request_unit_cost"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/*********************************************
	 * 조건에 맞는 품목별 공급처관리 List 가져오기
	 *********************************************/
	public ArrayList getItemSupplyInfoList(String tablename,String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		String where = "";

		com.anbtech.pu.entity.ItemSupplyInfoTable table;
		com.anbtech.pu.business.PurchaseConfigMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		tablename = " pu_item_supply_info a, item_master b, company_customer c ";
		where = purchaseBO.getWhere(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount(tablename, where);
		int recNum = total;
		
		//검색조건에 맞는 게시물을 가져온다.
		String query   = " SELECT a.*,b.item_name,b.item_desc,c.name_kor,c.name_eng FROM "+ tablename;
		String orderBy = " ORDER BY a.MID DESC";
	    query = query + where + orderBy;

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new com.anbtech.pu.entity.ItemSupplyInfoTable();
			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("name_kor"));
			table.setOrderWeight(rs.getString("order_weight"));
			table.setLeadTime(rs.getString("lead_time"));
			table.setIsTradeNow(rs.getString("is_trade_now"));
			table.setIsMainSupplyer(rs.getString("is_main_supplyer"));
			table.setMinOrderQuantity(rs.getString("min_order_quantity"));
			table.setMaxOrderQuantity(rs.getString("max_order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setSupplyerItemCode(rs.getString("supplyer_item_code"));
			table.setSupplyerItemName(rs.getString("supplyer_item_name"));
			table.setSupplyerItemDesc(rs.getString("supplyer_item_desc"));
			table.setMakerName(rs.getString("maker_name"));
			table.setSupplyUnitCost(rs.getString("supply_unit_cost"));
			table.setRequestUnitCost(rs.getString("request_unit_cost"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemName(rs.getString("item_name"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}


	/*********************************************
	 * 조건에 맞는 품목단가정보 가져오기
	 *********************************************/
	public ArrayList getItemCostInfoList(String mode,String searchword,String searchscope,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.pu.business.PurchaseConfigMgrBO purchaseconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
		com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		
		String where = purchaseconfigBO.getWhereForItemCostList(mode,searchscope,searchword);		

		int current_page_num =Integer.parseInt(page);
		int total = getTotalCount("st_item_unit_cost", where);
		int recNum = total;
		
		String query = "SELECT * FROM st_item_unit_cost " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
			table = new com.anbtech.st.entity.StockInfoTable();

			table.setMid(rs.getString("mid"));
			String item_code_link = "";
			item_code_link += "PurchaseConfigMgrServlet?mode=modify_unit_cost";
			item_code_link += "&item_code=" + rs.getString("item_code");
			item_code_link = "<a href='" + item_code_link + "'>" + rs.getString("item_code") + "</a>";
			table.setItemCode(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setUnitCostS(rs.getString("unit_cost_s"));
			table.setUnitCostA(rs.getString("unit_cost_a"));
			table.setUnitCostC(rs.getString("unit_cost_c"));
			table.setLastUpdatedDate(rs.getString("last_updated_date"));

			table_list.add(table);

			recNum--;
		}
		stmt.close();
		rs.close();

		return table_list;
	}

	/*********************************************
	 * 선택된 품목의 단가정보 가져오기
	 *********************************************/
	public StockInfoTable getItemCostInfo(String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		StockInfoTable table = new StockInfoTable();

		String sql = "SELECT * FROM st_item_unit_cost WHERE item_code = '" + item_code +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setItemType(rs.getString("item_type"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setUnitCostA(rs.getString("unit_cost_a"));
			table.setUnitCostC(rs.getString("unit_cost_c"));
			table.setUnitCostS(rs.getString("unit_cost_s"));
			table.setLastUpdatedDate(rs.getString("last_updated_date"));
		}

		stmt.close();
		rs.close();

		return table;
	}


	/*********************************
	 * 품목단가정보 저장
	 *********************************/
	 public void saveUnitCostInfo(String item_type,String item_code,String item_name,String item_desc,String unit_cost_a,String unit_cost_s,String unit_cost_c,String last_updated_date) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_item_unit_cost (item_type,item_code,item_name,item_desc,unit_cost_a,unit_cost_s,unit_cost_c,last_updated_date) VALUES (?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_type);
		pstmt.setString(2,item_code);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setDouble(5,Double.parseDouble(unit_cost_a));
		pstmt.setDouble(6,Double.parseDouble(unit_cost_s));
		pstmt.setDouble(7,Double.parseDouble(unit_cost_c));
		pstmt.setString(8,last_updated_date);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 품목단가정보 수정
	 *********************************/
	 public void updateUnitCostInfo(String item_type,String item_code,String item_name,String item_desc,String unit_cost_a,String unit_cost_s,String unit_cost_c,String last_updated_date) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE st_item_unit_cost SET unit_cost_a=?,unit_cost_s=?,unit_cost_c=?,last_updated_date=? WHERE item_code = '" + item_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setDouble(1,Double.parseDouble(unit_cost_a));
		pstmt.setDouble(2,Double.parseDouble(unit_cost_s));
		pstmt.setDouble(3,Double.parseDouble(unit_cost_c));
		pstmt.setString(4,last_updated_date);

		pstmt.executeUpdate();
		pstmt.close();
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


	/*********************************
	 * 품목별 공급처 관리정보 저장
	 *********************************/
	 public void saveItemSupplyInfo(String item_code,String supplyer_code,String order_weight,String lead_time,String is_trade_now,String is_main_supplyer,String min_order_quantity,String max_order_quantity,String order_unit,String supplyer_item_code,String supplyer_item_name,String supplyer_item_desc,String maker_name,String supply_unit_cost,String request_unit_cost) throws Exception{
		
		PreparedStatement pstmt = null;
		
		String query = "INSERT INTO pu_item_supply_info (item_code,supplyer_code,order_weight,lead_time,is_trade_now,is_main_supplyer,min_order_quantity, max_order_quantity,order_unit,supplyer_item_code,supplyer_item_name,supplyer_item_desc,maker_name,supply_unit_cost,request_unit_cost) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,supplyer_code);
		pstmt.setString(3,order_weight);
		pstmt.setString(4,lead_time);
		pstmt.setString(5,is_trade_now);
		pstmt.setString(6,is_main_supplyer);
		pstmt.setString(7,min_order_quantity);
		pstmt.setString(8,max_order_quantity);
		pstmt.setString(9,order_unit);
		pstmt.setString(10,supplyer_item_code);
		pstmt.setString(11,supplyer_item_name);
		pstmt.setString(12,supplyer_item_desc);
		pstmt.setString(13,maker_name);
		pstmt.setString(14,supply_unit_cost);
		pstmt.setString(15,request_unit_cost);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 품목별 공급처관리정보 Update
	 *********************************/
	 public void updateItemSupplyInfo(String mid,String item_code,String supplyer_code,String order_weight,String lead_time,String is_trade_now,String is_main_supplyer,String min_order_quantity,String max_order_quantity,String order_unit,String supplyer_item_code,String supplyer_item_name,String supplyer_item_desc,String maker_name,String supply_unit_cost,String request_unit_cost) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_item_supply_info SET item_code=?,supplyer_code=?,order_weight=?,lead_time=?,is_trade_now=?,is_main_supplyer=?,min_order_quantity=?, max_order_quantity=?,order_unit=?,supplyer_item_code=?,supplyer_item_name=?,supplyer_item_desc=?,maker_name =?, supply_unit_cost=?,request_unit_cost=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,supplyer_code);
		pstmt.setString(3,order_weight);
		pstmt.setString(4,lead_time);
		pstmt.setString(5,is_trade_now);
		pstmt.setString(6,is_main_supplyer);
		pstmt.setString(7,min_order_quantity);
		pstmt.setString(8,max_order_quantity);
		pstmt.setString(9,order_unit);
		pstmt.setString(10,supplyer_item_code);
		pstmt.setString(11,supplyer_item_name);
		pstmt.setString(12,supplyer_item_desc);
		pstmt.setString(13,maker_name);
		pstmt.setString(14,supply_unit_cost);
		pstmt.setString(15,request_unit_cost);
		pstmt.setString(16,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************************
	 * 선택된 품목공급정보 가져오기
	 *********************************************/
	public ItemSupplyInfoTable getItemSupplyInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		ItemSupplyInfoTable table = new ItemSupplyInfoTable();

		String sql = "SELECT a.*,b.item_name,b.item_desc,c.name_kor,c.name_eng FROM pu_item_supply_info a, item_master b, company_customer c WHERE  a.item_code = b.item_no AND a.supplyer_code = c.company_no  AND a.mid = '" + mid +"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			
			table.setMid(rs.getString("mid"));
			table.setItemCode(rs.getString("item_code"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("name_kor")+"/"+rs.getString("name_eng"));
			table.setOrderWeight(rs.getString("order_weight"));
			table.setLeadTime(rs.getString("lead_time"));
			table.setIsTradeNow(rs.getString("is_trade_now"));
			table.setIsMainSupplyer(rs.getString("is_main_supplyer"));
			table.setMinOrderQuantity(rs.getString("min_order_quantity"));
			table.setMaxOrderQuantity(rs.getString("max_order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setSupplyerItemCode(rs.getString("supplyer_item_code"));
			table.setSupplyerItemName(rs.getString("supplyer_item_name"));
			table.setSupplyerItemDesc(rs.getString("supplyer_item_desc"));
			table.setMakerName(rs.getString("maker_name"));
			table.setSupplyUnitCost(rs.getString("supply_unit_cost"));
			table.setRequestUnitCost(rs.getString("request_unit_cost"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemName(rs.getString("item_name"));

		}
		stmt.close();
		rs.close();

		return table;
	}


	/*********************************
	 * 품목별 공급처관리정보 삭제
	 *********************************/
	 public void deleteItemSupplyInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM pu_item_supply_info WHERE mid='"+mid+"'";
		//System.out.println(query);
		st.executeUpdate(query);
		st.close();
	}

	/*********************************
	 *  주 거래 여부 확인
	 *********************************/
	 public boolean isMainSupplyer(String item_code,String mid) throws Exception{
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		
		boolean bool = false;
		String query = "";
		if (mid.equals(""))	{
			query = "SELECT count(*) FROM pu_item_supply_info WHERE item_code like '"+item_code+"' and  is_main_supplyer='y'";
		} else {
			query = "SELECT count(*) FROM pu_item_supply_info WHERE item_code like '"+item_code+"' and  is_main_supplyer='y' and mid <>'"+mid+"'";			
		}


		rs = st.executeQuery(query);
		int count = 0;
		if (rs.next())	{
			count = rs.getInt(1);
		}
		st.close();

		if(count <1 ) bool = true;
		return bool;

	 }
		

	 /*************************************
	 *  공급업체의 품목 단가 가져오기
	 ************************************/
	 public double getSupplyerUnitCost(String item_code,String supplyer_code) throws Exception {
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		String query = "SELECT supply_unit_cost FROM pu_item_supply_info WHERE item_code='"+item_code+"' and supplyer_code = '"+supplyer_code+"'";
		double unit_cost = 0;
		rs = st.executeQuery(query);
		if(rs.next()) unit_cost = Double.parseDouble(rs.getString("supply_unit_cost"));
		st.close();
		//System.out.println("unit_cost:"+unit_cost);
		return unit_cost;
	 }
}		