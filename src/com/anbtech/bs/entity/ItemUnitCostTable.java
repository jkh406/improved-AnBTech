/********************************
* 품목코드별 단가정보관리 테이블
*********************************/

package com.anbtech.bs.entity;

public class ItemUnitCostTable
{
	private String mid;
	private String item_code;
	private String item_name;
	private String sale_type;
	private String approval_type;
	private String apply_date;
	private String sale_unit;
	private String money_type;
	private String sale_unit_cost;
	private String customer_code;
	private String customer_name;

	public void setMid(String mid)  { this.mid = mid; }
	public String getMid()			{ return mid;}

	public void setItemCode(String item_code)  { this.item_code = item_code; }
	public String getItemCode()			{ return item_code;}

	public void setItemName(String item_name)  { this.item_name = item_name; }
	public String getItemName()			{ return item_name;}

	public void setSaleType(String sale_type)  { this.sale_type = sale_type; }
	public String getSaleType()			{ return sale_type;}

	public void setApprovalType(String approval_type)  { this.approval_type = approval_type; }
	public String getApprovalType()			{ return approval_type;}

	public void setApplyDate(String apply_date)  { this.apply_date = apply_date; }
	public String getApplyDate()			{ return apply_date;}

	public void setSaleUnit(String sale_unit)  { this.sale_unit = sale_unit; }
	public String getSaleUnit()			{ return sale_unit;}

	public void setMoneyType(String money_type)  { this.money_type = money_type; }
	public String getMoneyType()			{ return money_type;}

	public void setSaleUnitCost(String sale_unit_cost)  { this.sale_unit_cost = sale_unit_cost; }
	public String getSaleUnitCost()			{ return sale_unit_cost;}

	public void setCustomerCode(String customer_code)  { this.customer_code = customer_code; }
	public String getCustomerCode()			{ return customer_code;}

	public void setCustomerName(String customer_name)  { this.customer_name = customer_name; }
	public String getCustomerName()			{ return customer_name;}
}
