/*********************************
 â�� ���� Table
 WarehouseInfoTable.java
*********************************/

package com.anbtech.st.entity;

public class WarehouseInfoTable
{
	private String mid;				// ������ȣ
	private String warehouse_code;	// â�� �ڵ�
	private String warehouse_name;	// â���
	private String warehouse_type;	// â�� Ÿ��-�系â��/�ŷ�â��
	private String factory_code;	// ���� �ڵ�(������ â�� �����ִ� ����)
	private String factory_name;	// ���� ��
	private String group_name;		// â���� �׷��
	private String manager_name;	// â�� �����ڸ�
	private String manager_id;		// â�� ������ id
	private String using_mrp;		// mrp ������ �ش� â���� ��� ���� ����
	private String client;			// ����� â���� �ŷ�ó��

	//�߰�
	private String item_code;			// ǰ���ڵ�
	private String item_name;			// ǰ���
	private String bar_code;			// ���ڵ�
	private String bar_desc;			// ���ڵ� description
	private String location;			// ǰ����ġ����
	private String warehouse_address;	// â���ּ�
	private String in_date;				// �԰�¥
	private String in_man_id;			// �԰���ID
	private String in_man_name;			// �԰���NAME
	private String out_date;			// �������
	private String out_man_id;			// �����Id
	private String out_man_name;		// ����ڸ�


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

	//�߰�
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
