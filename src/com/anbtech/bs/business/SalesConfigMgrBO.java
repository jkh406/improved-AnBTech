package com.anbtech.bs.business;

import com.anbtech.bs.entity.*;
import com.anbtech.bs.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;

public class SalesConfigMgrBO{

	private Connection con;

	public SalesConfigMgrBO(Connection con){
		this.con = con;
	}

	/****************************************
	 * 품목단가정보 검색시의 검색구문 만들기
	 ****************************************/
	public String getWhereForList(String mode, String searchscope, String searchword) throws Exception{
		String where = "", where_cat = "", where_and = " AND ", where_sea = "";
		if (searchword.length() > 0){
			if(searchscope.equals("item_code")){
				where_sea = " (item_code LIKE '%"+searchword+"%')";
			
			} else if (searchscope.equals("item_name"))	{
				where_sea = " (item_name LIKE '%"+searchword+"%')";
			} else if (searchscope.equals("customer_name"))	{
				where_sea = " (customer_name LIKE '%"+searchword+"%')";
			}

			
			where = " WHERE " + where_sea;
		} 
		
		return where;
	}


	/*********************************************
	 * 수주형태 폼 field  정보 Setting
	 *********************************************/
	public BookingTypeTable getBookingTypeForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();

		String order_code		= "";	// 수주형태코드
		String order_name		= "";	// 수주형태명
		String is_export		= "y";	// 수출여부
		String is_return		= "y";	// 반품여부
		String is_entry			= "y";	// 통관여부
		String is_shipping		= "y";	// 출하여부
		String is_auto_ship		= "y";	// 자동출하여부
		String is_sale			= "y";	// 매출여부
		String is_use			= "y";	// 사용여부
		String shipping_type	= "";	// 출하형태

		if (mode.equals("modify_booking_type"))
		{
			table		= (BookingTypeTable)scDAO.getBookingTypeInfo(mid);
						
			mid				= table.getMid();
			order_code		= table.getOrderCode();					
			order_name		= table.getOrderName();					
			is_export		= table.getIsExport();				
			is_return		= table.getIsReturn();					
			is_entry		= table.getIsEntry();					
			is_shipping		= table.getIsShipping();					
			is_auto_ship	= table.getIsAutoShip();				
			is_sale			= table.getIsSale();					
			is_use			= table.getIsUse();					
			shipping_type	= table.getShippingType();														
		}

		table.setMid(mid);
		table.setOrderCode(order_code);
		table.setOrderName(order_name);
		table.setIsExport(is_export);
		table.setIsReturn(is_return);
		table.setIsEntry(is_entry);
		table.setIsShipping(is_shipping);
		table.setIsAutoShip(is_auto_ship);
		table.setIsSale(is_sale);
		table.setIsUse(is_use);
		table.setShippingType(shipping_type);

		return table;
	}


	/*********************************************
	 * 품목단가별 단가 정보 Form field 정보 setting
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		String item_code		= "";   // 품목코드
		String item_name		= "";	// 품목명
		String sale_type		= "";	// 판매유형코드, 일반/직영/특판 등
		String approval_type	= "";	// 결재유형코드, 현금/수표/어음 등
		String apply_date		= "";	// 적용일자
		String sale_unit		= "";	// 판매단위
		String money_type		= "";	// 화폐유형
		String sale_unit_cost	= "0";	// 판매단가
		
		if (mode.equals("modify_item_unit_cost"))
		{
			table		= (ItemUnitCostTable)scDAO.getItemUnitCostInfo(mid);
						
			mid				= table.getMid();           
			item_code		= table.getItemCode();     
			item_name		= table.getItemName();     
			sale_type		= table.getSaleType();     
			approval_type	= table.getApprovalType(); 
			apply_date		= table.getApplyDate();    
			sale_unit		= table.getSaleUnit();     
			money_type		= table.getMoneyType();    
			sale_unit_cost	= table.getSaleUnitCost();
		}

		table.setMid(mid);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setSaleType(sale_type);
		table.setApprovalType(approval_type);
		table.setApplyDate(apply_date);
		table.setSaleUnit(sale_unit);
		table.setMoneyType(money_type);
		table.setSaleUnitCost(sale_unit_cost);

		return table;
	}

	
	/*********************************************
	 * 고객별 품목단가 정보 Form field 정보 setting
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostByCustomerForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		
		String item_code		= "";   // 품목코드
		String item_name		= "";	// 품목명
		String sale_type		= "";	// 판매유형코드, 일반/직영/특판 등
		String approval_type	= "";	// 결재유형코드, 현금/수표/어음 등
		String apply_date		= "";	// 적용일자
		String sale_unit		= "";	// 판매단위
		String money_type		= "";	// 화폐유형
		String sale_unit_cost	= "0";	// 판매단가
		String customer_code	= "";	// 거래처코드
		String customer_name	= "";	// 거래처명
		
		if (mode.equals("modify_item_unit_cost_by_customer"))
		{
			table		= (ItemUnitCostTable)scDAO.getItemUnitCostByCustomerInfo(mid);
						
			mid				= table.getMid();           
			item_code		= table.getItemCode();     
			item_name		= table.getItemName();     
			sale_type		= table.getSaleType();     
			approval_type	= table.getApprovalType(); 
			apply_date		= table.getApplyDate();    
			sale_unit		= table.getSaleUnit();     
			money_type		= table.getMoneyType();    
			sale_unit_cost	= table.getSaleUnitCost();
			customer_code	= table.getCustomerCode();
			customer_name	= table.getCustomerName();
		}

		table.setMid(mid);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setSaleType(sale_type);
		table.setApprovalType(approval_type);
		table.setApplyDate(apply_date);
		table.setSaleUnit(sale_unit);
		table.setMoneyType(money_type);
		table.setSaleUnitCost(sale_unit_cost);
		table.setCustomerCode(customer_code);
		table.setCustomerName(customer_name);

		return table;
	}


	/*********************************************
	 * 품목코드별 할증정보 Form field 정보 setting
	 *********************************************/
	public ItemPremiumTable getItemPremiumForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		
		String item_code		= "";   // 품목코드
		String item_name		= "";	// 품목명
		String approval_type	= "";	// 결재유형코드, 현금/수표/어음 등
		String apply_date		= "";	// 적용일자
		String sale_unit		= "";	// 판매단위
		String premium_type		= "";	// 할증유형
		String premium_name		= "";	// 할증유형명
		String premium_standard_quantity	= "0";	// 할증적용기준수량
		String premium_value	= "";	// 할증값
		
		if (mode.equals("modify_item_premium"))
		{
			table		= (ItemPremiumTable)scDAO.getItemPremiumInfo(mid);
						
			mid				= table.getMid();           
			item_code		= table.getItemCode();     
			item_name		= table.getItemName();     
			approval_type	= table.getApprovalType(); 
			apply_date		= table.getApplyDate();    
			sale_unit		= table.getSaleUnit();     
			premium_type		= table.getPremiumType();    
			premium_name		= table.getPremiumName();
			premium_standard_quantity	= table.getPremiumStandardQuantity();
			premium_value		= table.getPremiumValue();
		}

		table.setMid(mid);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setApprovalType(approval_type);
		table.setApplyDate(apply_date);
		table.setSaleUnit(sale_unit);
		table.setPremiumType(premium_type);
		table.setPremiumName(premium_name);
		table.setPremiumStandardQuantity(premium_standard_quantity);
		table.setPremiumValue(premium_value);

		return table;
	}


	/*********************************************
	 * 고객별 품목 할증정보 Form field 정보 setting
	 *********************************************/
	public ItemPremiumTable getItemPremiumByCustomerForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		
		String item_code		= "";   // 품목코드
		String item_name		= "";	// 품목명
		String approval_type	= "";	// 결재유형코드, 현금/수표/어음 등
		String apply_date		= "";	// 적용일자
		String sale_unit		= "";	// 판매단위
		String premium_type		= "";	// 할증유형
		String premium_name		= "";	// 할증유형명
		String premium_standard_quantity	= "0";	// 할증적용기준수량
		String premium_value	= "";	// 할증값
		String customer_code	= "";	// 거래처코드
		String customer_name	= "";	// 거래처명
		
		
		if (mode.equals("modify_item_premium_by_customer"))
		{
			table		= (ItemPremiumTable)scDAO.getItemPremiumByCustomerInfo(mid);
						
			mid				= table.getMid();           
			item_code		= table.getItemCode();     
			item_name		= table.getItemName();     
			approval_type	= table.getApprovalType(); 
			apply_date		= table.getApplyDate();    
			sale_unit		= table.getSaleUnit();     
			premium_type		= table.getPremiumType();    
			premium_name		= table.getPremiumName();
			premium_standard_quantity	= table.getPremiumStandardQuantity();
			premium_value		= table.getPremiumValue();
			customer_code	= table.getCustomerCode();
			customer_name	= table.getCustomerName();
		}

		table.setMid(mid);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setApprovalType(approval_type);
		table.setApplyDate(apply_date);
		table.setSaleUnit(sale_unit);
		table.setPremiumType(premium_type);
		table.setPremiumName(premium_name);
		table.setPremiumStandardQuantity(premium_standard_quantity);
		table.setPremiumValue(premium_value);
		table.setCustomerCode(customer_code);
		table.setCustomerName(customer_name);

		return table;
	}
}