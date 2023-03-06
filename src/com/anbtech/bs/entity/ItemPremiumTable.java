/********************************
* 품목코드별 할증정보관리 테이블
*********************************/

package com.anbtech.bs.entity;

public class ItemPremiumTable
{
	private String mid;
	private String item_code;
	private String item_name;
	private String approval_type;
	private String apply_date;
	private String sale_unit;
	private String premium_type;
	private String premium_name;
	private String premium_standard_quantity;
	private String premium_value;
	private String customer_code;
	private String customer_name;

	public void setMid(String mid)  { this.mid = mid; }
	public String getMid()			{ return mid;}

	public void setItemCode(String item_code)  { this.item_code = item_code; }
	public String getItemCode()			{ return item_code;}

	public void setItemName(String item_name)  { this.item_name = item_name; }
	public String getItemName()			{ return item_name;}

	public void setApprovalType(String approval_type)  { this.approval_type = approval_type; }
	public String getApprovalType()			{ return approval_type;}

	public void setApplyDate(String apply_date)  { this.apply_date = apply_date; }
	public String getApplyDate()			{ return apply_date;}

	public void setSaleUnit(String sale_unit)  { this.sale_unit = sale_unit; }
	public String getSaleUnit()			{ return sale_unit;}

	public void setPremiumType(String premium_type)  { this.premium_type = premium_type; }
	public String getPremiumType()			{ return premium_type;}

	public void setPremiumName(String premium_name)  { this.premium_name = premium_name; }
	public String getPremiumName()			{ return premium_name;}

	public void setPremiumStandardQuantity(String premium_standard_quantity)  { this.premium_standard_quantity = premium_standard_quantity; }
	public String getPremiumStandardQuantity()			{ return premium_standard_quantity;}

	public void setPremiumValue(String premium_value)  { this.premium_value = premium_value; }
	public String getPremiumValue()			{ return premium_value;}

	public void setCustomerCode(String customer_code)  { this.customer_code = customer_code; }
	public String getCustomerCode()			{ return customer_code;}

	public void setCustomerName(String customer_name)  { this.customer_name = customer_name; }
	public String getCustomerName()			{ return customer_name;}
}
