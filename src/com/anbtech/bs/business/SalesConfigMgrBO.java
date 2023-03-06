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
	 * ǰ��ܰ����� �˻����� �˻����� �����
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
	 * �������� �� field  ���� Setting
	 *********************************************/
	public BookingTypeTable getBookingTypeForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.BookingTypeTable table = new com.anbtech.bs.entity.BookingTypeTable();

		String order_code		= "";	// ���������ڵ�
		String order_name		= "";	// �������¸�
		String is_export		= "y";	// ���⿩��
		String is_return		= "y";	// ��ǰ����
		String is_entry			= "y";	// �������
		String is_shipping		= "y";	// ���Ͽ���
		String is_auto_ship		= "y";	// �ڵ����Ͽ���
		String is_sale			= "y";	// ���⿩��
		String is_use			= "y";	// ��뿩��
		String shipping_type	= "";	// ��������

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
	 * ǰ��ܰ��� �ܰ� ���� Form field ���� setting
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		String item_code		= "";   // ǰ���ڵ�
		String item_name		= "";	// ǰ���
		String sale_type		= "";	// �Ǹ������ڵ�, �Ϲ�/����/Ư�� ��
		String approval_type	= "";	// ���������ڵ�, ����/��ǥ/���� ��
		String apply_date		= "";	// ��������
		String sale_unit		= "";	// �ǸŴ���
		String money_type		= "";	// ȭ������
		String sale_unit_cost	= "0";	// �ǸŴܰ�
		
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
	 * ���� ǰ��ܰ� ���� Form field ���� setting
	 *********************************************/
	public ItemUnitCostTable getItemUnitCostByCustomerForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemUnitCostTable table = new com.anbtech.bs.entity.ItemUnitCostTable();

		
		String item_code		= "";   // ǰ���ڵ�
		String item_name		= "";	// ǰ���
		String sale_type		= "";	// �Ǹ������ڵ�, �Ϲ�/����/Ư�� ��
		String approval_type	= "";	// ���������ڵ�, ����/��ǥ/���� ��
		String apply_date		= "";	// ��������
		String sale_unit		= "";	// �ǸŴ���
		String money_type		= "";	// ȭ������
		String sale_unit_cost	= "0";	// �ǸŴܰ�
		String customer_code	= "";	// �ŷ�ó�ڵ�
		String customer_name	= "";	// �ŷ�ó��
		
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
	 * ǰ���ڵ庰 �������� Form field ���� setting
	 *********************************************/
	public ItemPremiumTable getItemPremiumForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		
		String item_code		= "";   // ǰ���ڵ�
		String item_name		= "";	// ǰ���
		String approval_type	= "";	// ���������ڵ�, ����/��ǥ/���� ��
		String apply_date		= "";	// ��������
		String sale_unit		= "";	// �ǸŴ���
		String premium_type		= "";	// ��������
		String premium_name		= "";	// ����������
		String premium_standard_quantity	= "0";	// ����������ؼ���
		String premium_value	= "";	// ������
		
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
	 * ���� ǰ�� �������� Form field ���� setting
	 *********************************************/
	public ItemPremiumTable getItemPremiumByCustomerForm(String mode,String mid) throws Exception{
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		com.anbtech.bs.entity.ItemPremiumTable table = new com.anbtech.bs.entity.ItemPremiumTable();

		
		String item_code		= "";   // ǰ���ڵ�
		String item_name		= "";	// ǰ���
		String approval_type	= "";	// ���������ڵ�, ����/��ǥ/���� ��
		String apply_date		= "";	// ��������
		String sale_unit		= "";	// �ǸŴ���
		String premium_type		= "";	// ��������
		String premium_name		= "";	// ����������
		String premium_standard_quantity	= "0";	// ����������ؼ���
		String premium_value	= "";	// ������
		String customer_code	= "";	// �ŷ�ó�ڵ�
		String customer_name	= "";	// �ŷ�ó��
		
		
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