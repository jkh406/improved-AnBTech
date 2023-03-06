/********************************
* 수주형태 등록관리 helper class
*********************************/

package com.anbtech.bs.entity;

public class BookingTypeTable
{
	private String	mid;
	private String	order_code;
	private String	order_name;
	private String	is_export;
	private String	is_return;
	private String	is_entry;
	private String	is_shipping;
	private String	is_auto_ship;
	private String	is_sale;
	private String	shipping_type;
	private String	is_use;


	public void setMid(String mid)  { this.mid = mid; }
	public String getMid()			{ return mid;}

	public void setOrderCode(String order_code)  { this.order_code = order_code; }
	public String getOrderCode()			{ return order_code;}

	public void setOrderName(String order_name)  { this.order_name = order_name; }
	public String getOrderName()			{ return order_name;}

	public void setIsExport(String is_export)  { this.is_export = is_export; }
	public String getIsExport()			{ return is_export;}

	public void setIsReturn(String is_return)  { this.is_return = is_return; }
	public String getIsReturn()			{ return is_return;}

	public void setIsEntry(String is_entry)  { this.is_entry = is_entry; }
	public String getIsEntry()			{ return is_entry;}

	public void setIsShipping(String is_shipping)  { this.is_shipping = is_shipping; }
	public String getIsShipping()			{ return is_shipping;}

	public void setIsAutoShip(String is_auto_ship)  { this.is_auto_ship = is_auto_ship; }
	public String getIsAutoShip()			{ return is_auto_ship;}

	public void setIsSale(String is_sale)  { this.is_sale = is_sale; }
	public String getIsSale()			{ return is_sale;}

	public void setShippingType(String shipping_type)  { this.shipping_type = shipping_type; }
	public String getShippingType()			{ return shipping_type;}

	public void setIsUse(String is_use)  { this.is_use = is_use; }
	public String getIsUse()			{ return is_use;}

}
