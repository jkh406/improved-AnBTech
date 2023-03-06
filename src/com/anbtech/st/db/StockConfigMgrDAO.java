package com.anbtech.st.db;

import com.anbtech.st.entity.*;
import com.anbtech.st.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class StockConfigMgrDAO{
	private Connection con;

	public StockConfigMgrDAO(Connection con){
		this.con = con;
	}

	/*********************************************
	 *  수불유형 정보 list가져오기
	 *********************************************/
	public ArrayList getStockConfList() throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.st.entity.StockConfInfoTable table;

		String query = "SELECT * FROM st_conf_info";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.StockConfInfoTable();
			table.setMid(rs.getString("mid"));
			table.setTradeTypeCode(rs.getString("trade_type_code"));
			table.setTradeTypeName(rs.getString("trade_type_name"));
			table.setStockRiseFall(rs.getString("stock_rise_fall"));
			table.setStockType1(rs.getString("stock_type1"));
			table.setStockType2(rs.getString("stock_type2"));
			table.setIsCostApply(rs.getString("is_cost_apply"));
			table.setIsCountPosting(rs.getString("is_count_posting"));
			table.setIsWharehouseMove(rs.getString("is_wharehouse_move"));
			table.setIsFactoryMove(rs.getString("is_factory_move"));
			table.setIsItemMove(rs.getString("is_item_move"));
			table.setIsNoMove(rs.getString("is_no_move"));			

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}


	/*********************************************
	 *  선택한수불유형 가져오기
	 *********************************************/
	public StockConfInfoTable getStockConfInfo(String mid) throws Exception{

		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.st.entity.StockConfInfoTable table = new com.anbtech.st.entity.StockConfInfoTable();

		String query = "SELECT * FROM st_conf_info WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.StockConfInfoTable();

			table.setMid(rs.getString("mid"));
			table.setTradeTypeCode(rs.getString("trade_type_code"));
			table.setTradeTypeName(rs.getString("trade_type_name"));
			table.setStockRiseFall(rs.getString("stock_rise_fall"));
			table.setStockType1(rs.getString("stock_type1"));
			table.setStockType2(rs.getString("stock_type2"));
			table.setIsCostApply(rs.getString("is_cost_apply"));
			table.setIsCountPosting(rs.getString("is_count_posting"));
			table.setIsWharehouseMove(rs.getString("is_wharehouse_move"));
			table.setIsFactoryMove(rs.getString("is_factory_move"));
			table.setIsItemMove(rs.getString("is_item_move"));
			table.setIsNoMove(rs.getString("is_no_move"));			

		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * 수불유형관리정보 저장
	 *********************************/
	 public void saveStockConfInfo(String  trade_type_code,String trade_type_name,String stock_rise_fall,String stock_type1,String stock_type2,String is_cost_apply,String is_count_posting,String is_wharehouse_move,String is_factory_move,String is_item_move,String is_no_move) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_conf_info (trade_type_code,trade_type_name,stock_rise_fall,stock_type1,stock_type2,is_cost_apply,is_count_posting,is_wharehouse_move,is_factory_move,is_item_move,is_no_move) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,trade_type_code);
		pstmt.setString(2,trade_type_name);
		pstmt.setString(3,stock_rise_fall);
		pstmt.setString(4,stock_type1);
		pstmt.setString(5,stock_type2);
		pstmt.setString(6,is_cost_apply);
		pstmt.setString(7,is_count_posting);
		pstmt.setString(8,is_wharehouse_move);
		pstmt.setString(9,is_factory_move);
		pstmt.setString(10,is_item_move);
		pstmt.setString(11,is_no_move);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 수불유형관리정보 Update
	 *********************************/
		public void modifyStockConfInfo(String mid,String  trade_type_code,String trade_type_name,String stock_rise_fall,String stock_type1,String stock_type2,String is_cost_apply,String is_count_posting,String is_wharehouse_move,String is_factory_move,String is_item_move,String is_no_move) throws Exception{

		PreparedStatement pstmt = null;

		String query = "UPDATE st_conf_info SET trade_type_code=?,trade_type_name=?,stock_rise_fall=?,stock_type1=?,stock_type2=?,is_cost_apply=?,is_count_posting=?,is_wharehouse_move=?,is_factory_move=?,is_item_move=?,is_no_move=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,trade_type_code);
		pstmt.setString(2,trade_type_name);
		pstmt.setString(3,stock_rise_fall);
		pstmt.setString(4,stock_type1);
		pstmt.setString(5,stock_type2);
		pstmt.setString(6,is_cost_apply);
		pstmt.setString(7,is_count_posting);
		pstmt.setString(8,is_wharehouse_move);
		pstmt.setString(9,is_factory_move);
		pstmt.setString(10,is_item_move);
		pstmt.setString(11,is_no_move);
		pstmt.setString(12,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 수불유형관리정보 삭제
	 *********************************/
	 public void deleteStockConfInfo(String mid) throws Exception{
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM st_conf_info WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}

	/*********************************************
	 *  선택한 수불유형코드에 해당하는 수불유형명 가져오기
	 *********************************************/
	public String getTradeTypeName(String trade_type_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String trade_type_name = "";
		String query = "SELECT trade_type_name FROM st_conf_info WHERE trade_type_code = '"+trade_type_code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			trade_type_name = rs.getString("trade_type_name");
		}
		stmt.close();
		rs.close();

		return trade_type_name;
	}

	/*********************************
	 * 공장정보 저장
	 *********************************/
	 public void saveFactoryInfo(String factory_code,String factory_name,String production_type,String main_product,String factory_address,String product_plan_term,String mps_confirm_term,String mps_plan_term,String mrp_confirm_term,String agency_code,String agency_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO factory_info_table (factory_code,factory_name,production_type,main_product,factory_address,product_plan_term,mps_confirm_term,mps_plan_term,mrp_confirm_term,agency_code,agency_name) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,factory_code);
		pstmt.setString(2,factory_name);
		pstmt.setString(3,production_type);
		pstmt.setString(4,main_product);
		pstmt.setString(5,factory_address);
		pstmt.setString(6,product_plan_term);
		pstmt.setString(7,mps_confirm_term);
		pstmt.setString(8,mps_plan_term);
		pstmt.setString(9,mrp_confirm_term);
		pstmt.setString(10,agency_code);
		pstmt.setString(11,agency_name);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 공장관리정보 Update
	 *********************************/
		public void modifyFactoryInfo(String mid,String factory_code,String factory_name,String production_type,String main_product,String factory_address,String product_plan_term,String mps_confirm_term,String mps_plan_term,String mrp_confirm_term,String agency_code,String agency_name) throws Exception{

		PreparedStatement pstmt = null;

		String query = "UPDATE factory_info_table SET factory_name=?,factory_code=?,production_type=?,main_product=?,factory_address=?,product_plan_term=?,mps_confirm_term=?,mps_plan_term=?,mrp_confirm_term=?,agency_code=?,agency_name=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,factory_name);
		pstmt.setString(2,factory_code);
		pstmt.setString(3,production_type);
		pstmt.setString(4,main_product);
		pstmt.setString(5,factory_address);
		pstmt.setString(6,product_plan_term);
		pstmt.setString(7,mps_confirm_term);
		pstmt.setString(8,mps_plan_term);
		pstmt.setString(9,mrp_confirm_term);
		pstmt.setString(10,agency_code);
		pstmt.setString(11,agency_name);
		pstmt.setString(12,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 공장관리정보 List
	 *********************************/		
	public ArrayList getFactoryInfoList() throws Exception {
		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		String temp_production_type = "";
		com.anbtech.st.entity.FactoryInfoTable table;

		String query = "SELECT * FROM factory_info_table";
			
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.FactoryInfoTable();
			table.setMid(rs.getString("mid"));
			table.setFactoryCode(rs.getString("factory_code"));
			temp_production_type = rs.getString("production_type");
			table.setFactoryName(rs.getString("factory_name"));
			if(temp_production_type.equals("in")) temp_production_type = "자사생산";
			if(temp_production_type.equals("out")) temp_production_type = "외주생산";
			table.setProductionType(temp_production_type);
			table.setMainProduct(rs.getString("main_product"));
			table.setAgencyCode(rs.getString("agency_code"));
			table.setAgencyName(rs.getString("agency_name"));
			
			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/*********************************
	 * 공장정보 가져오기
	 *********************************/		
	public FactoryInfoTable getFactoryInfo(String mid) throws Exception {
		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();

		String query = "SELECT * FROM factory_info_table WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.FactoryInfoTable();

			table.setMid(rs.getString("mid"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));	
			table.setProductionType(rs.getString("production_type"));	
			table.setMainProduct(rs.getString("main_product"));	
			table.setFactoryAddress(rs.getString("factory_address"));
			table.setProductPlanTerm(rs.getString("product_plan_term"));
			table.setMpsConfirmTerm(rs.getString("mps_confirm_term"));
			table.setMpsPlanTerm(rs.getString("mps_plan_term"));	
			table.setMrpConfirmTerm(rs.getString("mrp_confirm_term"));	
			table.setAgencyCode(rs.getString("agency_code"));
			table.setAgencyName(rs.getString("agency_name"));

		}
		stmt.close();
		rs.close();

		return table;
	}
	
	/*********************************
	 * 공장정보 삭제
	 *********************************/		
	public void deleteFactoryInfo(String mid) throws Exception {
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM factory_info_table WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}


	/*********************************
	 * 창고정보 저장
	 *********************************/
	 public void saveWarehouseInfo(String warehouse_code,String warehouse_name,String warehouse_type,String factory_code,String factory_name,String group_name,String manager_name,String manager_id,String using_mrp,String client,String warehouse_address) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO warehouse_info_table (warehouse_code,warehouse_name,warehouse_type,factory_code,factory_name,group_name,manager_name,manager_id,using_mrp,client,warehouse_address) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,warehouse_code);
		pstmt.setString(2,warehouse_name);
		pstmt.setString(3,warehouse_type);
		pstmt.setString(4,factory_code);
		pstmt.setString(5,factory_name);
		pstmt.setString(6,group_name);
		pstmt.setString(7,manager_name);
		pstmt.setString(8,manager_id);
		pstmt.setString(9,using_mrp);
		pstmt.setString(10,client);
		pstmt.setString(11,warehouse_address);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 창고관리정보 Update
	 *********************************/
		public void modifyWarehouseInfo(String mid,String warehouse_code,String warehouse_name,String warehouse_type,String factory_code,String factory_name,String group_name,String manager_name,String manager_id,String using_mrp,String client,String warehouse_address) throws Exception{

		PreparedStatement pstmt = null;

		String query = "UPDATE warehouse_info_table SET warehouse_code=?,warehouse_name=?,warehouse_type=?,factory_code=?,factory_name=?,group_name=?,manager_name=?,manager_id=?,using_mrp=?,client=?,warehouse_address=? WHERE mid=?";

		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,warehouse_code);
		pstmt.setString(2,warehouse_name);
		pstmt.setString(3,warehouse_type);
		pstmt.setString(4,factory_code);
		pstmt.setString(5,factory_name);
		pstmt.setString(6,group_name);
		pstmt.setString(7,manager_name);
		pstmt.setString(8,manager_id);
		pstmt.setString(9,using_mrp);
		pstmt.setString(10,client);
		pstmt.setString(11,warehouse_address);
		pstmt.setString(12,mid);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 창고관리정보 List
	 *********************************/		
	public ArrayList getWarehouseInfoList() throws Exception {
		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.st.entity.WarehouseInfoTable table;

		String query = "SELECT * FROM warehouse_info_table";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.WarehouseInfoTable();
			table.setMid(rs.getString("mid"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setWarehouseType(rs.getString("warehouse_type"));
			table.setFactoryName(rs.getString("factory_name"));

			arry.add(table);
		}
		stmt.close();
		rs.close();

		return arry;
	}

	/*********************************
	 * 창고정보 가져오기
	 *********************************/		
	public WarehouseInfoTable getWarehouseInfo(String mid) throws Exception {
		ArrayList arry = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();

		String query = "SELECT * FROM warehouse_info_table WHERE mid = '"+mid+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new com.anbtech.st.entity.WarehouseInfoTable();

			table.setMid(rs.getString("mid"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setWarehouseType(rs.getString("warehouse_type"));
			table.setWarehouseAddress(rs.getString("warehouse_address"));
			table.setFactoryCode(rs.getString("factory_code"));  
			table.setFactoryName(rs.getString("factory_name"));
			
			table.setGroupName(rs.getString("group_name"));    
			table.setManagerName(rs.getString("manager_name"));  
			table.setManagerId(rs.getString("manager_id"));    
			table.setUsingMrp(rs.getString("using_mrp"));     
			table.setClient(rs.getString("client"));       

		}
		stmt.close();
		rs.close();

		return table;
	}
	
	/*********************************
	 * 공장정보 삭제
	 *********************************/		
	public void deleteWarehouseInfo(String mid) throws Exception {
		Statement st = null;
		st = con.createStatement();
		String query = "DELETE FROM warehouse_info_table WHERE mid='"+mid+"'";
		
		st.executeUpdate(query);
		st.close();
	}
}		