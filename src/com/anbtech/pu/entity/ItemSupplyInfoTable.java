package com.anbtech.pu.entity;

public class ItemSupplyInfoTable {
	private String mid;						// ������ȣ
	private String item_code;				// ǰ���ڵ�
	private String supplyer_code;			// ����ó �ڵ�
	private String order_weight;			// ���ֹ��� ����ġ
	private String lead_time;				// ����L/T (Lead Time)
	private String is_trade_now;			// �ŷ�(���) ����
	private String is_main_supplyer;		// �ְ���ó ����
	private String min_order_quantity;		// �ּҹ��ַ�
	private String max_order_quantity;		// �ִ���ַ�
	private String order_unit;				// ���ִ���
	private String supplyer_item_code;		// ����ó ǰ�� �ڵ�
	private String supplyer_item_name;		// ����ó ǰ�� ��
	private String supplyer_item_desc;		// ����ó ǰ�� �԰�
	private String maker_name;				// ����ȸ�� ��
	private String supply_unit_cost;		// ���޴ܰ�(���ִܰ�)
	private String request_unit_cost;		// ���޴ܰ�(���ſ�û�ܰ�)
	private String supplyer_name;			// ����ó ��
	private String item_desc;				// ǰ�񼳸�
	private String item_name;				// �׸��

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
