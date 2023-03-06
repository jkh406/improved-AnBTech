package com.anbtech.em.db;

import java.io.*;
import java.sql.*;
import java.util.*;
import sun.jdbc.odbc.*;
import com.anbtech.em.entity.ItemInfoTable;
import com.anbtech.text.StringProcess;

public class EstimateItemViewDAO{

	private Connection con;

	public EstimateItemViewDAO(Connection con){
		this.con = con;
	}

	private String new_ancestor = "0";

//	private EstimateDAO estimateDAO = new EstimateDAO(con);
	private ItemInfoTable item = new ItemInfoTable();
	private StringProcess sp = new StringProcess();

	/***********************************************************
	 * 견적 품목 리스트를 가져온다.
	 ***********************************************************/
	public ArrayList getEstimateItemList(String estimate_no,String version,String cut_unit) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList item_list = new ArrayList();

		String sql = "SELECT * FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String mid				= rs.getString("mid");
			String item_class		= rs.getString("item_class");
			String item_name		= rs.getString("item_name");
			String model_code		= rs.getString("model_code");
			String model_name		= rs.getString("model_name");
			String quantity			= rs.getString("quantity");
			String unit				= rs.getString("unit");
			String standards		= rs.getString("standards");
			String maker_name		= rs.getString("maker_name");
			String supplyer_code	= rs.getString("supplyer_code");
			String supplyer_name	= rs.getString("supplyer_name");
			String buying_cost		= rs.getString("buying_cost");
			String gains_percent	= rs.getString("gains_percent");
			String gains_value		= rs.getString("gains_value");
			String supply_cost		= rs.getString("supply_cost");
			String discount_percent	= rs.getString("discount_percent");
			String discount_value	= rs.getString("discount_value");
			String tax_percent		= rs.getString("tax_percent");
			String tax_value		= rs.getString("tax_value");
			String estimate_value	= rs.getString("estimate_value");

			String link_modify = "<a href=\"javascript:modify_item('" + mid + "');\"><img src='../em/images/lt_modify.gif' border='0'></a>";
			String link_delete = "<a href=\"javascript:delete_item('" + mid + "');\"><img src='../em/images/lt_del.gif' border='0'></a>";
			String link_url = link_modify + " " + link_delete;

			//String estimate_value_link = "<a href=\"javascript:view_value_info('" + model_code + "','" + supplyer_code + "');\">" + estimate_value + "</a>";

			item = new ItemInfoTable();

			item.setMid(mid);
			item.setItemClass(item_class);
			item.setItemName(item_name);
			item.setModelCode(model_code);
			item.setModelName(model_name);
			item.setQuantity(quantity);
			item.setUnit(unit);
			item.setStandards(standards);
			item.setMakerName(maker_name);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setBuyingCost(buying_cost);
			item.setGainsPercent(gains_percent);
			item.setGainsValue(gains_value);
			item.setSupplyCost(supply_cost);
			item.setDiscountPercent(discount_percent);
			item.setDiscountValue(discount_value);
			item.setTaxPercent(tax_percent);
			item.setTaxValue(tax_value);
			item.setEstimateValue(estimate_value);
			item.setLinkUrl(link_url);

			item_list.add(item);
		}
		stmt.close();
		rs.close();
		
		return item_list;
	}

	/************************
	 * 견적 품목을 복사한다.
	 ************************/
	public void copyEstimateItem(String estimate_no,String version,String new_estimate_no,String new_version) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;

		EstimateDAO estimateDAO = new EstimateDAO(con);

		String sql = "SELECT mid FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String mid	= rs.getString("mid");

			//견적품목을 복사
			estimateDAO.copyEstimateItem(mid,new_estimate_no,new_version);
		}

		stmt.close();
		rs.close();
	}



	/***********************************************************
	 * 견적 품목 추가 리스트 폼을 만든다.
	 * level == 0 인 경우에는 최상위 품목만을 출력한다.
	 **********************************************************
	public ArrayList getAddItemList(String estimate_no,String version,int level,String ancestor,String cut_unit) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
		com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);

		String sql = "";
		
		if(level == 0) sql = "SELECT * FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "' and e_level = '1' ORDER BY mid ASC";
		else sql = "SELECT * FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "' and e_level = '" + level + "' and e_ancestor = '" + ancestor + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String mid				= rs.getString("mid");
			String item_no			= rs.getString("item_no");
			String item_name		= rs.getString("item_name");
			String item_class		= rs.getString("item_class");
			String standards		= rs.getString("standards");
			String unit				= rs.getString("unit");
			String quantity			= rs.getString("quantity");

			String supplyer			= rs.getString("supplyer");
			String supplyer_name	= "";
			if(item_class.equals("1")) supplyer_name = crmDAO.getCompanyNameByNo(supplyer);

			String buying_cost		= sp.getMoneyFormat(rs.getString("buying_cost"),"");
			String gains_percent	= rs.getString("gains_percent");
			String gains_value		= sp.getMoneyFormat(rs.getString("gains_value"),"");
			String supply_cost		= sp.getMoneyFormat(rs.getString("supply_cost"),"");
			String discount_percent	= rs.getString("discount_percent");
			String discount_value	= sp.getMoneyFormat(rs.getString("discount_value"),"");
			String tax_percent		= rs.getString("tax_percent");
			String tax_value		= sp.getMoneyFormat(rs.getString("tax_value"),"");
			String estimate_value	= sp.getMoneyFormat(rs.getString("estimate_value"),"",cut_unit);

			//패키지 품목에 대해서는 견적가합계외에는 모드 공백으로 만든다.
			if(item_class.equals("2")){
				buying_cost		= "";
				gains_percent	= "";
				gains_value		= "";
				supply_cost		= "";
				discount_percent= "";
				discount_value	= "";
				tax_percent		= "";
				tax_value		= "";
			}

			String c_level			= rs.getString("e_level");
			String c_ancestor		= rs.getString("e_ancestor");

			String s_level = "";
	        if (Integer.parseInt(c_level) > 0) {
			    s_level = "-";
	            for (int i=1; i<Integer.parseInt(c_level); i++) s_level = "&nbsp;&nbsp;&nbsp;" + s_level;
			}

			String link_insert = "<a href=\"javascript:search_item('" + estimate_no + "','" + version + "','" + Integer.toString(Integer.parseInt(c_level) + 1) + "','" + mid + "');\"><img src='../em/images/lt_add_s.gif' border='0' '하위추가'></a>";
			String link_insert2 = "<a href=\"javascript:add_item('" + estimate_no + "','" + version + "','" + Integer.toString(Integer.parseInt(c_level) + 1) + "','" + mid + "');\"><img src='../em/images/lt_add_s2.gif' border='0' alt='하위추가'></a>";
			String link_modify = "<a href=\"javascript:modify_item('" + mid + "');\"><img src='../em/images/lt_modify.gif' border='0'></a>";
			String link_delete = "<a href=\"javascript:delete_item('" + mid + "');\"><img src='../em/images/lt_del.gif' border='0'></a>";

			String link_url = "";
			if(item_class.equals("2")) link_url = link_modify + " " + link_delete + " " + link_insert + " " + link_insert2; // 패키지 항목일 경우
			else link_url = link_modify + " " + link_delete; // 단일 항목일 경우

			String estimate_value_link = "<a href=\"javascript:view_value_info('" + item_no + "','" + supplyer + "');\">" + estimate_value + "</a>";

			item = new ItemInfoTable();

			item.setItemName(s_level + item_name);
			item.setStandards(standards);
			item.setUnit(unit);
			item.setQuantity(quantity);

			item.setSupplyerName(supplyer_name);
			item.setBuyingCost(buying_cost);
			item.setGainsPercent(gains_percent);
			item.setGainsValue(gains_value);
			item.setSupplyCost(supply_cost);
			item.setDiscountPercent(discount_percent);
			item.setDiscountValue(discount_value);
			item.setTaxPercent(tax_percent);
			item.setTaxValue(tax_value);
			item.setEstimateValue(estimate_value_link);

			item.setLinkUrl(link_url);

			item_list.add(item);
			if(level > 0) getAddItemList(estimate_no,version,Integer.parseInt(c_level)+1,mid,cut_unit);	
		}
		
		return item_list;
	}
*/

} //EstimateItemViewDAO