package com.anbtech.st.business;

import com.anbtech.st.entity.*;
import com.anbtech.st.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;

public class StockConfigMgrBO{

	private Connection con;

	public StockConfigMgrBO(Connection con){
		this.con = con;
	}

	/*********************************************
	 * ����(�԰�/���)���� ������ �����´�.
	 *********************************************/
	public StockConfInfoTable getStockConfForm(String mode,String mid) throws Exception{
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.StockConfInfoTable table = new com.anbtech.st.entity.StockConfInfoTable();

		String trade_type_code		= "";		// ���� ���� �ڵ�                       
		String trade_type_name		= "";		// ���� ��                             
		String stock_rise_fall		= "1";		// ��� ���� ���� (1:���� 2:���� 3:����)  
		String stock_type1			= "";		// �������1                            
		String stock_type2			= "";		// �������2                            
		String is_cost_apply		= "y";		// ���ܰ��ݿ�����(1:YES 2:NO)          
		String is_count_posting		= "y";		// ȸ�� posting ����                    
		String is_wharehouse_move = "y";	// â�� �̵�����                       
		String is_factory_move	  = "y";	// ���尣 �̵�����                       
		String is_item_move		  = "y";	// ǰ�� �̵�����                       
		String is_no_move		  = "y";	// ������ �̵�����                       


		if (mode.equals("modify_conf_type"))
		{
			table		= (StockConfInfoTable)stconfigDAO.getStockConfInfo(mid);
						
			mid				= table.getMid();
			trade_type_code = table.getTradeTypeCode();					
			trade_type_name = table.getTradeTypeName();					
			stock_rise_fall = table.getStockRiseFall();				
			stock_type1		= table.getStockType1();					
			stock_type2		= table.getStockType2();					
			is_cost_apply	= table.getIsCostApply();					
			is_count_posting	= table.getIsCountPosting();				
			is_wharehouse_move	= table.getIsWharehouseMove();					
			is_factory_move		= table.getIsFactoryMove();					
			is_item_move		= table.getIsItemMove();						
			is_no_move			= table.getIsNoMove();							
		}
		
			table.setMid(mid);
			table.setTradeTypeCode(trade_type_code);
			table.setTradeTypeName(trade_type_name);
			table.setStockRiseFall(stock_rise_fall);
			table.setStockType1(stock_type1);
			table.setStockType2(stock_type2);
			table.setIsCostApply(is_cost_apply);
			table.setIsCountPosting(is_count_posting);
			table.setIsWharehouseMove(is_wharehouse_move);
			table.setIsFactoryMove(is_factory_move);
			table.setIsItemMove(is_item_move);
			table.setIsNoMove(is_no_move);

		return table;
	}

	
	// ���ǹ� �����
/*	public String getWhere(String mode, String searchscope, String searchword) throws Exception{

		//�˻����ǿ� �°� where������ �����Ѵ�.
		String where = "", where_cat = "", where_and = " AND ", where_sea = "";
		
		if (searchword.length() > 0){
		
		} 

		return where;
	}
*/

	/*********************************************
	 * ���� ������ �����´�.
	 *********************************************/
	public FactoryInfoTable getFactoryInfoForm(String mode,String mid) throws Exception {
	
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();

//		String mid					= "";	// ������ȣ
		String factory_code			= "";	// �����ڵ�
		String factory_name			= "";	// �����
		String production_type		= "";	// ����Ÿ��(���ֻ���, �ڻ����)
		String main_product			= "";	// �� ����ǰ��
		String factory_address		= "";	// ���� �ּ�
		String product_plan_term	= "";	// �����ȹ�Ⱓ (�ϴ���)
		String mps_confirm_term		= "";	// mpsȮ���Ⱓ (�ϴ���)
		String mps_plan_term		= "";	// mps��ȹ�Ⱓ (�ϴ���)
		String mrp_confirm_term		= "";	// mrpȮ���Ⱓ (�ϴ���)
		String agency_code			= "";   // ������ڵ�
		String agency_name			= "";   // ������

		if (mode.equals("modify_factory_info"))	{
				
			table = stconfigDAO.getFactoryInfo(mid);
			factory_code		= table.getFactoryCode();	
			factory_name		= table.getFactoryName();	
			production_type		= table.getProductionType();	
			main_product		= table.getMainProduct();	
			factory_address		= table.getFactoryAddress();	
			product_plan_term	= table.getProductPlanTerm();	
			mps_confirm_term	= table.getMpsConfirmTerm();	
			mps_plan_term		= table.getMpsPlanTerm();	
			mrp_confirm_term	= table.getMrpConfirmTerm();				
			agency_code			= table.getAgencyCode();
			agency_name			= table.getAgencyName();
		}

		table.setMid(mid);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);	
		table.setProductionType(production_type);	
		table.setMainProduct(main_product);	
		table.setFactoryAddress(factory_address);
		table.setProductPlanTerm(product_plan_term);
		table.setMpsConfirmTerm(mps_confirm_term);
		table.setMpsPlanTerm(mps_plan_term);	
		table.setMrpConfirmTerm(mrp_confirm_term);	
		table.setAgencyCode(agency_code);
		table.setAgencyName(agency_name);

		return table;

	}


	/*********************************************
	 * â�������� �����´�.
	 *********************************************/
	public WarehouseInfoTable getWarehouseInfoForm(String mode,String mid) throws Exception {
	
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();

//		String mid					= "";	// ������ȣ
		String warehouse_code	= "";	// â�� �ڵ�                           
		String warehouse_name	= "";	// â���                              
		String warehouse_type	= "";	// â�� Ÿ��-�系â��/�ŷ�â��           
		String warehouse_address= "";	// â�� �ּ�
		String factory_code		= "";	// ���� �ڵ�(������ â�� �����ִ� ����) 
		String factory_name		= "";	// ���� ��
		String group_name		= "";	// â���� �׷��                        
		String manager_name		= "";	// â�� �����ڸ�                        
		String manager_id		= "";	// â�� ������ id                      
		String using_mrp		= "";	// mrp ������ �ش� â���� ��� ���� ����  
		String client			= "";	// ����� â���� �ŷ�ó                  
		

		if (mode.equals("modify_warehouse_info"))	{
				
			table = stconfigDAO.getWarehouseInfo(mid);

			warehouse_code	= table.getWarehouseCode();
			warehouse_name	= table.getWarehouseName();
			warehouse_type	= table.getWarehouseType();
			factory_code	= table.getFactoryCode();
			factory_name	= table.getFactoryName();
			warehouse_address = table.getWarehouseAddress();
			group_name		= table.getGroupName();
			manager_name	= table.getManagerName();
			manager_id		= table.getManagerId();
			using_mrp		= table.getUsingMrp();
			client			= table.getClient();
		
		}

		table.setMid(mid);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setWarehouseType(warehouse_type);
		table.setFactoryCode(factory_code);  
		table.setFactoryName(factory_name);
		table.setWarehouseAddress(warehouse_address);
		table.setGroupName(group_name);    
		table.setManagerName(manager_name);  
		table.setManagerId(manager_id);    
		table.setUsingMrp(using_mrp);     
		table.setClient(client);       
		

		return table;

	}

}