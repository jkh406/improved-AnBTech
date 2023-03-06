package com.anbtech.pu.business;

import com.anbtech.pu.entity.*;
import com.anbtech.st.entity.*;
import com.anbtech.pu.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;

public class PurchaseConfigMgrBO{

	private Connection con;

	public PurchaseConfigMgrBO(Connection con){
		this.con = con;
	}

	/*********************************************
	 * 관리(입고/출고)형태 정보를 가져온다.
	 *********************************************/
	public InOutTypeTable getInOutTypeForm(String mode,String mid) throws Exception{
		PurchaseConfigMgrDAO puconfigDAO = new PurchaseConfigMgrDAO(con);
		com.anbtech.pu.entity.InOutTypeTable table = new com.anbtech.pu.entity.InOutTypeTable();

		String type			= "";		// 처리(입고/출고/매입)형태
		String name			= "";		// 처리(입고/출고/매입)명
		// 추가
		String is_import	= "y";		// 수입여부(y or n)
		String is_enter		= "y";		// 입고여부(y or n)
		String is_return	= "y";		// 반품여부(y or n)
		String is_sageup	= "y";		// 사급여부(y or n)
		String is_using		= "y";		// 사용여부(y or n)
		String stock_type	= "";		// 재고처리형태

		if (mode.equals("modify_inout_type"))
		{
			table		= (InOutTypeTable)puconfigDAO.getInOutTypeInfo(mid);
						
			type		= table.getType();
			name		= table.getName();
			is_import	= table.getIsImport();
			is_enter	= table.getIsEnter();
			is_return	= table.getIsReturn();
			is_sageup	= table.getIsSageup();
			is_using	= table.getIsUsing();
			stock_type	= table.getStockType();
		}
		
		table.setMid(mid);
		table.setType(type);
		table.setName(name);
		table.setIsImport(is_import);
		table.setIsEnter(is_enter);
		table.setIsReturn(is_return);
		table.setIsSageup(is_sageup);
		table.setIsUsing(is_using);
		table.setStockType(stock_type);

		return table;
	}

	/************************************
	 * 선택된 발주형태정보를 가져온다.
	 ************************************/
	public OrderTypeTable getOrderTypeForm(String mode,String mid) throws Exception{
		PurchaseConfigMgrDAO puconfigDAO = new PurchaseConfigMgrDAO(con);
		OrderTypeTable table = new OrderTypeTable();
		
		//발주형태정보
		String	order_type		="";		// 발주형태 코드
		String	order_name		="";		// 발주형태 명
		String	is_import		="y";		// 수입여부(y or n)
		String	is_shipping		="y";		// 선적여부(y or n)
		String	is_pass			="y";		// 통관여부(y or n)
		String	is_enter		="y";		// 입고여부(y or n)
		String	is_purchase		="y";		// 매입여부(y or n)
		String	is_return		="y";		// 반품여부(y or n)
		String	is_sageup		="y";		// 사급여부(y or n)
		String	is_using		="y";		// 사용여부(y or n)
		String	enter_code		="";		// 입고형태 코드(선택)
		String	enter_name		="";		// 입고형태 명
		String	outgo_code		="";		// 출고형태 코드(선택)
		String	outgo_name		="";		// 출고형태 명
		String	purchase_code	="";		// 매입형태 코드(선택)
		String	purchase_name	="";		// 매입형태 명
	
		if (mode.equals("modify_order_type"))
		{
			table = puconfigDAO.getPuOrderTypeInfo(mid);

			order_type		=	table.getOrderType();
			order_name		=	table.getOrderName();
			is_import		=	table.getIsImport();
			is_shipping		=	table.getIsShipping();
			is_pass			=	table.getIsPass();
			is_enter		=	table.getIsEnter();
			is_purchase		=	table.getIsPurchase();
			is_return		=	table.getIsReturn();
			is_sageup		=	table.getIsSageup();
			is_using		=	table.getIsUsing();
			enter_code		=	table.getEnterCode();
			enter_name		=	table.getEnterName();
			outgo_code		=	table.getOutgoCode();
			outgo_name		=	table.getOutgoName();
			purchase_code	=	table.getPurchaseCode();
			purchase_name	=	table.getPurchaseName();
		}
		
		
		table.setOrderType(order_type);
		table.setOrderName(order_name);
		table.setIsImport(is_import);
		table.setIsShipping(is_shipping);
		table.setIsPass(is_pass);
		table.setIsEnter(is_enter);
		table.setIsPurchase(is_purchase);
		table.setIsReturn(is_return);
		table.setIsSageup(is_sageup);
		table.setIsUsing(is_using);
		table.setEnterCode(enter_code);
		table.setEnterName(enter_name);
		table.setOutgoCode(outgo_code);
		table.setOutgoName(outgo_name);
		table.setPurchaseCode(purchase_code);
		table.setPurchaseName(purchase_name);

		return table;
	}

	/*********************************************
	 * 매입형태 정보를 가져온다.
	 *********************************************/
	public PurchaseTypeTable getPurchaseTypeForm(String mode,String mid) throws Exception{
		PurchaseConfigMgrDAO puconfigDAO = new PurchaseConfigMgrDAO(con);
		com.anbtech.pu.entity.PurchaseTypeTable table = new com.anbtech.pu.entity.PurchaseTypeTable();

		String purchase_type			= "";		// 처리(입고/출고/매입)형태
		String purchase_name			= "";		// 처리(입고/출고/매입)명
		// 추가
		String is_import	= "y";		// 수입여부(y or n)
		String is_return	= "y";		// 반품여부(y or n)
		String is_using		= "y";		// 사용여부(y or n)
		String is_except	= "y";		// 예외여부(y or n)
		String account_type	= "";		// 회계처리형태

		if (mode.equals("modify_purchase_type"))
		{
			table		= (PurchaseTypeTable)puconfigDAO.getPurchaseTypeInfo(mid);
						
			purchase_type	= table.getPurchaseType();
			purchase_name	= table.getPurchaseName();
			is_import		= table.getIsImport();
			is_return		= table.getIsReturn();
			is_using		= table.getIsUsing();
			is_except		= table.getIsExcept();
			account_type	= table.getAccountType();
		}
		
		table.setMid(mid);
		table.setPurchaseType(purchase_type);
		table.setPurchaseName(purchase_name);
		table.setIsImport(is_import);
		table.setIsReturn(is_return);
		table.setIsUsing(is_using);
		table.setIsExcept(is_except);
		table.setAccountType(account_type);

		return table;
	}

	/************************************
	 * 품목별 공급처 관리정보를 가져온다.
	 ************************************/
	public ItemSupplyInfoTable getItemSupplyInfoTableForm(String mode,String mid) throws Exception{
		PurchaseConfigMgrDAO puconfigDAO = new PurchaseConfigMgrDAO(con);
		ItemSupplyInfoTable table = new ItemSupplyInfoTable();
		
		//발주형태정보
		String item_code			="";	// 품목코드
		String supplyer_code		="";	// 공급처 코드
		String supplyer_name		="";	// 공급처 명 
		String order_weight			="";	// 발주배정 가중치
		String lead_time			="";	// 구매L/T (Lead Time)
		String is_trade_now			="y";	// 거래(사용) 여부
		String is_main_supplyer		="y";	// 주공급처 여부
		String min_order_quantity	="";	// 최소발주량
		String max_order_quantity	="";	// 최대발주량
		String order_unit			="";	// 발주단위
		String supplyer_item_code	="";	// 공급처 품목 코드
		String supplyer_item_name	="";	// 공급처 품목 명
		String supplyer_item_desc	="";	// 공급처 품목 규격
		String maker_name			="";	// 제조회사 명
		String supply_unit_cost		="";	// 공급단가
		String request_unit_cost	="";	// 요청단가
		String item_desc			="";    // 품목 설명	
		String item_name			="";    // 품목명
		
		if (mode.equals("modify_item_supply_info") || mode.equals("view_item_supply_info"))
		{
			table = puconfigDAO.getItemSupplyInfo(mid);
			
			item_code			=   table.getItemCode() ;			
			supplyer_code		=   table.getSupplyerCode();	
			supplyer_name		=   table.getSupplyerName();
			order_weight		=   table.getOrderWeight();		
			lead_time			=   table.getLeadTime();			
			is_trade_now		=   table.getIsTradeNow();		
			is_main_supplyer	=   table.getIsMainSupplyer();	
			min_order_quantity	=   table.getMinOrderQuantity();	
			max_order_quantity	=   table.getMaxOrderQuantity();	
			order_unit			=   table.getOrderUnit();			
			supplyer_item_code	=   table.getSupplyerItemCode();	
			supplyer_item_name	=   table.getSupplyerItemName();	
			supplyer_item_desc	=   table.getSupplyerItemDesc();	
			maker_name			=	table.getMakerName();			
			supply_unit_cost	=	table.getSupplyUnitCost();
			request_unit_cost	=	table.getRequestUnitCost();
			item_desc			=   table.getItemDesc();
			item_name			=   table.getItemName();
		}
		table.setMid(mid);
		table.setItemCode(item_code);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setOrderWeight(order_weight);
		table.setLeadTime(lead_time);
		table.setIsTradeNow(is_trade_now);
		table.setIsMainSupplyer(is_main_supplyer);
		table.setMinOrderQuantity(min_order_quantity);
		table.setMaxOrderQuantity(max_order_quantity);
		table.setOrderUnit(order_unit);
		table.setSupplyerItemCode(supplyer_item_code);
		table.setSupplyerItemName(supplyer_item_name);
		table.setSupplyerItemDesc(supplyer_item_desc);
		table.setMakerName(maker_name);
		table.setSupplyUnitCost(supply_unit_cost);
		table.setRequestUnitCost(request_unit_cost);
		table.setItemDesc(item_desc);
		table.setItemName(item_name);

		return table;
	}

	/************************************
	 * 품목원가 정보 작성 및 수정 폼
	 ************************************/
	public StockInfoTable getItemUnitCostAddForm(String mode,String item_code) throws Exception{
		PurchaseConfigMgrDAO puconfigDAO = new PurchaseConfigMgrDAO(con);
		com.anbtech.st.entity.StockInfoTable table = new com.anbtech.st.entity.StockInfoTable();
		
		String item_type			= "";	
		String item_name			= "";
		String item_desc			= "";
		String unit_cost_a			= "0";
		String unit_cost_c			= "0";
		String unit_cost_s			= "0";
		String last_updated_date	= "";

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		last_updated_date = vans.format(now);
		
		if(mode.equals("modify_unit_cost")){
			table = puconfigDAO.getItemCostInfo(item_code);
			
			item_type			= table.getItemType();
			item_code			= table.getItemCode();
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			unit_cost_a			= table.getUnitCostA();
			unit_cost_c			= table.getUnitCostC();
			unit_cost_s			= table.getUnitCostS();
			last_updated_date	= table.getLastUpdatedDate();
		}else{
			item_code			= "";
		}

		table.setItemType(item_type);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setUnitCostA(unit_cost_a);
		table.setUnitCostC(unit_cost_c);
		table.setUnitCostS(unit_cost_s);
		table.setLastUpdatedDate(last_updated_date);

		return table;
	}

	/************************************
	 * 품목별 공급정보 검색시의 where 구문 만들기
	 ************************************/
	public String getWhere(String mode, String searchscope, String searchword) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = " AND ", where_sea = "";
		
		where	  = " WHERE a.item_code = b.item_no AND a.supplyer_code = c.company_no ";
		
		if (searchword.length() > 0){
		
			if(searchscope.equals("item_code")){
				where_sea = " AND a.item_code like '%"+searchword+"%'";
			
			} else if (searchscope.equals("item_desc"))	{
				where_sea = " AND b.item_desc like '%"+searchword+"%'";
			
			} else if (searchscope.equals("supplyer_name"))	{
				where_sea = " AND ( c.name_kor like '%"+searchword+"%' OR c.name_eng like '%"+searchword+"%' )";
			}
		} 

		where = where + where_sea;
		return where;
	}


	/****************************************
	 * 품목원가정보 검색시의 검색구문 만들기
	 ****************************************/
	public String getWhereForItemCostList(String mode, String searchscope, String searchword) throws Exception{
		String where = "", where_cat = "", where_and = " AND ", where_sea = "";
		if (searchword.length() > 0){
			if(searchscope.equals("item_code")){
				where_sea = " (item_code LIKE '%"+searchword+"%')";
			
			} else if (searchscope.equals("item_desc"))	{
				where_sea = " (item_desc LIKE '%"+searchword+"%')";
			
			} else if (searchscope.equals("item_type"))	{
				where_sea = " (item_type = '"+searchword+"')";
			}
			where = " WHERE " + where_sea;
		} 
		
		return where;
	}
}