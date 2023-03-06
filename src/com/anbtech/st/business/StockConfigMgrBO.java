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
	 * 관리(입고/출고)형태 정보를 가져온다.
	 *********************************************/
	public StockConfInfoTable getStockConfForm(String mode,String mid) throws Exception{
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.StockConfInfoTable table = new com.anbtech.st.entity.StockConfInfoTable();

		String trade_type_code		= "";		// 수불 구분 코드                       
		String trade_type_name		= "";		// 수불 명                             
		String stock_rise_fall		= "1";		// 재고 증감 구분 (1:증가 2:감소 3:무관)  
		String stock_type1			= "";		// 재고유형1                            
		String stock_type2			= "";		// 재고유형2                            
		String is_cost_apply		= "y";		// 재고단가반영구분(1:YES 2:NO)          
		String is_count_posting		= "y";		// 회계 posting 구분                    
		String is_wharehouse_move = "y";	// 창고간 이동여부                       
		String is_factory_move	  = "y";	// 공장간 이동여부                       
		String is_item_move		  = "y";	// 품목간 이동여부                       
		String is_no_move		  = "y";	// 제번간 이동여부                       


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

	
	// 조건문 만들기
/*	public String getWhere(String mode, String searchscope, String searchword) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = " AND ", where_sea = "";
		
		if (searchword.length() > 0){
		
		} 

		return where;
	}
*/

	/*********************************************
	 * 공장 정보를 가져온다.
	 *********************************************/
	public FactoryInfoTable getFactoryInfoForm(String mode,String mid) throws Exception {
	
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.FactoryInfoTable table = new com.anbtech.st.entity.FactoryInfoTable();

//		String mid					= "";	// 관리번호
		String factory_code			= "";	// 공장코드
		String factory_name			= "";	// 공장명
		String production_type		= "";	// 생산타입(외주생산, 자사생산)
		String main_product			= "";	// 주 생산품목
		String factory_address		= "";	// 공장 주소
		String product_plan_term	= "";	// 생산계획기간 (일단위)
		String mps_confirm_term		= "";	// mps확정기간 (일단위)
		String mps_plan_term		= "";	// mps계획기간 (일단위)
		String mrp_confirm_term		= "";	// mrp확정기간 (일단위)
		String agency_code			= "";   // 사업장코드
		String agency_name			= "";   // 사업장명

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
	 * 창고정보를 가져온다.
	 *********************************************/
	public WarehouseInfoTable getWarehouseInfoForm(String mode,String mid) throws Exception {
	
		StockConfigMgrDAO stconfigDAO = new StockConfigMgrDAO(con);
		com.anbtech.st.entity.WarehouseInfoTable table = new com.anbtech.st.entity.WarehouseInfoTable();

//		String mid					= "";	// 관리번호
		String warehouse_code	= "";	// 창고 코드                           
		String warehouse_name	= "";	// 창고명                              
		String warehouse_type	= "";	// 창고 타입-사내창고/거래창고           
		String warehouse_address= "";	// 창고 주소
		String factory_code		= "";	// 공장 코드(현재의 창고를 갖고있는 공장) 
		String factory_name		= "";	// 공장 명
		String group_name		= "";	// 창고의 그룹명                        
		String manager_name		= "";	// 창고 관리자명                        
		String manager_id		= "";	// 창고 관리자 id                      
		String using_mrp		= "";	// mrp 전개시 해당 창고의 재고 감안 여부  
		String client			= "";	// 등록할 창고의 거래처                  
		

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