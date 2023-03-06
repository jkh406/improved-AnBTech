package com.anbtech.st.entity;

public class EtcEnterInfoTable {
	//입고정보관련
	private String mid;
	private String enter_no;
	private String enter_date;
	private String enter_type;
	private String enter_total_mount;
	private String monetary_unit;
	private String vat_rate;
	private String vat_mount;
	private String supplyer_code;
	private String supplyer_name;
	private String supplyer_info;
	private String supplyer_tel;
	private String fname;
	private String ftype;
	private String fsize;
	private String fpath;
	private String fumask;
	private String filelink;
	private String did;
	private String requestor_div_code;
	private String requestor_div_name;
	private String requestor_id;
	private String requestor_info;

	//입고품목관련
	private String item_code;
	private String item_code_link;
	private String item_name;
	private String item_desc;
	private String enter_quantity;
	private String enter_unit;
	private String enter_cost;
	private String unit_cost;
	private String factory_code;
	private String factory_name;
	private String warehouse_code;
	private String warehouse_name;
	private String unit_type;
	private String process_stat;
	
	//입고정보관련
	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getEnterNo() { return enter_no; }
	public void setEnterNo(String string) { enter_no = string; }

	public String getEnterDate() { return enter_date; }
	public void setEnterDate(String string) { enter_date = string; }

	public String getProcessStat() { return process_stat; }
	public void setProcessStat(String string) { process_stat = string; }

	public String getEnterType() { return enter_type; }
	public void setEnterType(String string) { enter_type = string; }

	public String getEnterTotalMount() { return enter_total_mount; }
	public void setEnterTotalMount(String string) { enter_total_mount = string; }

	public String getMonetaryUnit() { return monetary_unit; }
	public void setMonetaryUnit(String string) { monetary_unit = string; }

	public String getVatRate() { return vat_rate; }
	public void setVatRate(String string) { vat_rate = string; }

	public String getVatMount() { return vat_mount; }
	public void setVatMount(String string) { vat_mount = string; }

	public String getSupplyerCode() { return supplyer_code; }
	public void setSupplyerCode(String string) { supplyer_code = string; }

	public String getSupplyerName() { return supplyer_name; }
	public void setSupplyerName(String string) { supplyer_name = string; }

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

	//입고품목관련
	public String getItemCode() { return item_code; }
	public void setItemCode(String string) { item_code = string; }

	public String getItemCodeLink() { return item_code_link; }
	public void setItemCodeLink(String string) { item_code_link = string; }

	public String getItemName() { return item_name; }
	public void setItemName(String string) { item_name = string; }

	public String getItemDesc() { return item_desc; }
	public void setItemDesc(String string) { item_desc = string; }

	public String getEnterQuantity() { return enter_quantity; }
	public void setEnterQuantity(String string) { enter_quantity = string; }

	public String getEnterUnit() { return enter_unit; }
	public void setEnterUnit(String string) { enter_unit = string; }

	public String getEnterCost() { return enter_cost; }
	public void setEnterCost(String string) { enter_cost = string; }

	public String getUnitCost() { return unit_cost; }
	public void setUnitCost(String string) { unit_cost = string; }

	public String getFactoryName() { return factory_name; }
	public void setFactoryName(String string) { factory_name = string; }

	public String getFactoryCode() { return factory_code; }
	public void setFactoryCode(String string) { factory_code = string; }

	public String getWarehouseName() { return warehouse_name; }
	public void setWarehouseName(String string) { warehouse_name = string; }

	public String getWarehouseCode() { return warehouse_code; }
	public void setWarehouseCode(String string) { warehouse_code = string; }

	public String getUnitType() { return unit_type; }
	public void setUnitType(String string) { unit_type = string; }

	public String getFsize() { return fsize; }
	public void setFsize(String string) { fsize = string; }

	public String getFtype() { return ftype; }
	public void setFtype(String string) { ftype = string; }

	public String getFname() { return fname; }
	public void setFname(String string) { fname = string; }

	public String getFpath() { return fpath; }
	public void setFpath(String string) { fpath = string; }

	public String getDid() { return did;}
	public void setDid(String string)	{ did = string;}

	public String getFumask() { return fumask;}
	public void setFumask(String string)	{ fumask = string;}

	public String getFileLink()	{	return filelink;}
	public void setFileLink(String string)	{ filelink = string;}
}
