package com.anbtech.pu.entity;

public class ItemSupplyInfoTable {
	private String mid;						// 관리번호
	private String item_code;				// 품목코드
	private String supplyer_code;			// 공급처 코드
	private String order_weight;			// 발주배정 가중치
	private String lead_time;				// 구매L/T (Lead Time)
	private String is_trade_now;			// 거래(사용) 여부
	private String is_main_supplyer;		// 주공급처 여부
	private String min_order_quantity;		// 최소발주량
	private String max_order_quantity;		// 최대발주량
	private String order_unit;				// 발주단위
	private String supplyer_item_code;		// 공급처 품목 코드
	private String supplyer_item_name;		// 공급처 품목 명
	private String supplyer_item_desc;		// 공급처 품목 규격
	private String maker_name;				// 제조회사 명
	private String supply_unit_cost;		// 공급단가(발주단가)
	private String request_unit_cost;		// 공급단가(구매요청단가)
	private String supplyer_name;			// 공급처 명
	private String item_desc;				// 품목설명
	private String item_name;				// 항목명

	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getItemCode() { return item_code; }
	public void setItemCode(String string) { item_code = string; }

	public String getSupplyerCode() { return supplyer_code; }
	public void setSupplyerCode(String string) { supplyer_code = string; }

	public String getOrderWeight() { return order_weight; }
	public void setOrderWeight(String string) { order_weight = string; }

	public String getLeadTime() { return lead_time; }
	public void setLeadTime(String string) { lead_time = string; }

	public String getIsTradeNow() { return is_trade_now; }
	public void setIsTradeNow(String string) { is_trade_now = string; }

	public String getIsMainSupplyer() { return is_main_supplyer; }
	public void setIsMainSupplyer(String string) { is_main_supplyer = string; }

	public String getMinOrderQuantity() { return min_order_quantity; }
	public void setMinOrderQuantity(String string) { min_order_quantity = string; }

	public String getMaxOrderQuantity() { return max_order_quantity; }
	public void setMaxOrderQuantity(String string) { max_order_quantity = string; }

	public String getOrderUnit() { return order_unit; }
	public void setOrderUnit(String string) { order_unit = string; }

	public String getSupplyerItemCode() { return supplyer_item_code; }
	public void setSupplyerItemCode(String string) { supplyer_item_code = string; }

	public String getSupplyerItemName() { return supplyer_item_name; }
	public void setSupplyerItemName(String string) { supplyer_item_name = string; }

	public String getSupplyerItemDesc() { return supplyer_item_desc; }
	public void setSupplyerItemDesc(String string) { supplyer_item_desc = string; }

	public String getMakerName() { return maker_name; }
	public void setMakerName(String string) { maker_name = string; }

	public String getSupplyUnitCost()	{ return supply_unit_cost;}
	public void setSupplyUnitCost(String string)	{	supply_unit_cost = string;}

	public String getRequestUnitCost()	{ return request_unit_cost;}
	public void setRequestUnitCost(String string)	{	request_unit_cost = string;}

	public String getSupplyerName()	{ return supplyer_name;}
	public void setSupplyerName(String string)	{	supplyer_name = string;}
	
	public String getItemDesc()		{	return item_desc;}
	public void setItemDesc(String string)	{	item_desc = string;}

	public String getItemName()		{	return item_name;}
	public void setItemName(String string)	{	item_name = string;}
}
