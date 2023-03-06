/*********************************
 창고 정보 Table
 WarehouseInfoTable.java
*********************************/

package com.anbtech.st.entity;

public class WarehouseInfoTable
{
	private String mid;				// 관리번호
	private String warehouse_code;	// 창고 코드
	private String warehouse_name;	// 창고명
	private String warehouse_type;	// 창고 타입-사내창고/거래창고
	private String factory_code;	// 공장 코드(현재의 창고를 갖고있는 공장)
	private String factory_name;	// 공장 명
	private String group_name;		// 창고의 그룹명
	private String manager_name;	// 창고 관리자명
	private String manager_id;		// 창고 관리자 id
	private String using_mrp;		// mrp 전개시 해당 창고의 재고 감안 여부
	private String client;			// 등록할 창고의 거래처명

	//추가
	private String item_code;			// 품목코드
	private String item_name;			// 품목명
	private String bar_code;			// 바코드
	private String bar_desc;			// 바코드 description
	private String location;			// 품목위치정보
	private String warehouse_address;	// 창고주소
	private String in_date;				// 입고날짜
	private String in_man_id;			// 입고자ID
	private String in_man_name;			// 입고자NAME
	private String out_date;			// 출고일자
	private String out_man_id;			// 출고자Id
	private String out_man_name;		// 출고자명


	public String getMid()					{	return mid;				}
	public void setMid(String mid)			{	this.mid = mid;			}
		
	public String getWarehouseCode()					{	return warehouse_code;	}
	public void setWarehouseCode(String warehouse_code)	{	this.warehouse_code = warehouse_code;}

	public String getWarehouseName()					{	return warehouse_name;	}
	public void setWarehouseName(String warehouse_name)	{	this.warehouse_name = warehouse_name;}

	public String getWarehouseType()					{	return warehouse_type;	}
	public void setWarehouseType(String warehouse_type)	{	this.warehouse_type = warehouse_type;}

	public String getFactoryCode()					{	return factory_code;	}
	public void setFactoryCode(String factory_code)	{	this.factory_code = factory_code;}

	public String getGroupName()				{	return group_name;	}
	public void setGroupName(String group_name)	{	this.group_name = group_name;}

	public String getManagerName()					{	return manager_name;	}
	public void setManagerName(String manager_name)	{	this.manager_name = manager_name;}

	//추가
	public String getFactoryName()					{	return factory_name;}
	public void setFactoryName(String factory_name)	{	this.factory_name = factory_name;}

	public String getManagerId()				{	return manager_id;	}
	public void setManagerId(String manager_id)	{	this.manager_id = manager_id;}

	public String getUsingMrp()					{	return using_mrp;	}
	public void setUsingMrp(String using_mrp)	{	this.using_mrp = using_mrp;}

	public String getClient()				{	return client;	}
	public void setClient(String client)	{	this.client = client;}

	public String getItemCode()					{	return item_code;}
	public void setItemCode(String item_code)	{	this.item_code = item_code;}

	public String getItemName()					{	return item_name;}
	public void setItemName(String item_name)	{	this.item_name = item_name;}

	public String getBarCode()				{	return bar_code;}
	public void setBarCode(String bar_code)	{	this.bar_code = bar_code;}

	public String getBarDesc()				{	return bar_desc;}
	public void setBarDesc(String bar_desc)	{	this.bar_desc = bar_desc;}

	public String getLocation()						{	return location;}
	public void setLocation(String item_location)	{	this.location = location;}

	public String getWarehouseAddress()							{	return warehouse_address;}
	public void setWarehouseAddress(String warehouse_address)	{	this.warehouse_address = warehouse_address;}

	public String getInDate()				{	return in_date;}
	public void setInDate(String in_date)	{	this.in_date = in_date;}

	public String getInManId()					{	return in_man_id;}
	public void setInManId(String in_man_id)	{	this.in_man_id = in_man_id;}

	public String getInManName()					{	return in_man_name;}
	public void setInManName(String in_man_name)	{	this.in_man_name = in_man_name;}

	public String getOutDate()				{	return out_date;}
	public void setOutDate(String out_date)	{	this.out_date = out_date;}

	public String getOutManId()					{	return out_man_id;}
	public void setOutManId(String out_man_id)	{	this.out_man_id = out_man_id;}

	public String getOutManName()					{	return out_man_name;}
	public void setOutManName(String out_man_name)	{	this.out_man_name = out_man_name;}
}
