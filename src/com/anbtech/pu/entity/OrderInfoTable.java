package com.anbtech.pu.entity;

public class OrderInfoTable {
	//발주정보관련
	private String mid;
	private String order_no;
	private String order_type;
	private String process_stat;
	private String supplyer_code;
	private String supplyer_name;
	private String order_date;
	private String monetary_unit;
	private String exchange_rate;
	private String order_total_mount;
	private String vat_type;
	private String vat_rate;
	private String vat_mount;
	private String is_vat_contained;
	private String supplyer_info;
	private String supplyer_tel;
	private String requestor_div_code;
	private String requestor_div_name;
	private String requestor_id;
	private String requestor_info;
	private String approval_type;
	private String approval_period;
	private String payment_type;
	private String other_info;
	private String inout_type;

	//발주품목관련
	private String item_code;
	private String item_code_link;
	private String item_name;
	private String item_desc;
	private String order_quantity;
	private String order_unit;
	private String unit_cost;
	private String is_confirm_cost;
	private String order_cost;
	private String delivery_date;
	private String delivery_quantity;
	private String request_no;

	//발주정보
	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getOrderNo() { return order_no; }
	public void setOrderNo(String string) { order_no = string; }

	public String getOrderType() { return order_type; }
	public void setOrderType(String string) { order_type = string; }

	public String getProcessStat() { return process_stat; }
	public void setProcessStat(String string) { process_stat = string; }

	public String getSupplyerCode() { return supplyer_code; }
	public void setSupplyerCode(String string) { supplyer_code = string; }

	public String getSupplyerName() { return supplyer_name; }
	public void setSupplyerName(String string) { supplyer_name = string; }

	public String getOrderDate() { return order_date; }
	public void setOrderDate(String string) { order_date = string; }

	public String getMonetaryUnit() { return monetary_unit; }
	public void setMonetaryUnit(String string) { monetary_unit = string; }

	public String getExchangeRate() { return exchange_rate; }
	public void setExchangeRate(String string) { exchange_rate = string; }

	public String getOrderTotalMount() { return order_total_mount; }
	public void setOrderTotalMount(String string) { order_total_mount = string; }

	public String getVatType() { return vat_type; }
	public void setVatType(String string) { vat_type = string; }

	public String getVatRate() { return vat_rate; }
	public void setVatRate(String string) { vat_rate = string; }

	public String getVatMount() { return vat_mount; }
	public void setVatMount(String string) { vat_mount = string; }

	public String getIsVatContained() { return is_vat_contained; }
	public void setIsVatContained(String string) { is_vat_contained = string; }

	public String getSupplyerInfo() { return supplyer_info; }
	public void setSupplyerInfo(String string) { supplyer_info = string; }

	public String getSupplyerTel() { return supplyer_tel; }
	public void setSupplyerTel(String string) { supplyer_tel = string; }

	public String getRequestorDivCode() { return requestor_div_code; }
	public void setRequestorDivCode(String string) { requestor_div_code = string; }

	public String getRequestorDivName() { return requestor_div_name; }
	public void setRequestorDivName(String string) { requestor_div_name = string; }

	public String getRequestorId() { return requestor_id; }
	public void setRequestorId(String string) { requestor_id = string; }

	public String getRequestorInfo() { return requestor_info; }
	public void setRequestorInfo(String string) { requestor_info = string; }

	public String getApprovalType() { return approval_type; }
	public void setApprovalType(String string) { approval_type = string; }

	public String getApprovalPeriod() { return approval_period; }
	public void setApprovalPeriod(String string) { approval_period = string; }

	public String getPaymentType() { return payment_type; }
	public void setPaymentType(String string) { payment_type = string; }

	public String getOtherInfo() { return other_info; }
	public void setOtherInfo(String string) { other_info = string; }

	public String getInOutType() { return inout_type; }
	public void setInOutType(String string) { inout_type = string; }

	//품목정보
	public String getItemCode() { return item_code; }
	public void setItemCode(String string) { item_code = string; }

	public String getItemCodeLink() { return item_code_link; }
	public void setItemCodeLink(String string) { item_code_link = string; }

	public String getItemName() { return item_name; }
	public void setItemName(String string) { item_name = string; }

	public String getItemDesc() { return item_desc; }
	public void setItemDesc(String string) { item_desc = string; }

	public String getOrderQuantity() { return order_quantity; }
	public void setOrderQuantity(String string) { order_quantity = string; }

	public String getOrderUnit() { return order_unit; }
	public void setOrderUnit(String string) { order_unit = string; }

	public String getUnitCost() { return unit_cost; }
	public void setUnitCost(String string) { unit_cost = string; }

	public String getIsConfirmCost() { return is_confirm_cost; }
	public void setIsConfirmCost(String string) { is_confirm_cost = string; }

	public String getOrderCost() { return order_cost; }
	public void setOrderCost(String string) { order_cost = string; }

	public String getDeliveryDate() { return delivery_date; }
	public void setDeliveryDate(String string) { delivery_date = string; }

	public String getDeliveryQuantity() { return delivery_quantity; }
	public void setDeliveryQuantity(String string) { delivery_quantity = string; }

	public String getRequestNo() { return request_no; }
	public void setRequestNo(String string) { request_no = string; }
}
