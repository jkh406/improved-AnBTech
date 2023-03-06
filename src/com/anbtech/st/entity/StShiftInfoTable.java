/**************************
* StShiftInfoTable
* ��� �̵� ���� ���̺�
**************************/
package com.anbtech.st.entity;

public class StShiftInfoTable{

	private String mid;					// ������ȣ
	private String shift_no;			// �̵���ȣ
	private String shift_type;			// �̵�����
	private String sr_factory_code;		// source �����ڵ�
	private String sr_factory_name;		// source �����
	private String sr_warehouse_code;	// source â���ڵ�
	private String sr_warehouse_name;	// source â���
	private String sr_item_code;		// source ǰ���ڵ�
	private String sr_item_name;		// source ǰ���
	private String sr_item_type;		// source ǰ������
	private String sr_item_desc;		// source ǰ�񼳸�
	private String dt_factory_code;		// target �����ڵ�
	private String dt_factory_name;		// target �����
	private String dt_warehouse_code;	// target â���ڵ�
	private String dt_warehouse_name;	// target â���
	private String dt_item_code;		// target ǰ���ڵ�
	private String dt_item_name;		// target ǰ���
	private String dt_item_type;		// target ǰ������
	private String dt_item_desc;		// target ǰ�񼳸�
	private String stock_unit;			// ��� ����
	private String quantity;			// ��� ����
	private String requestor_id;		// ����� ID
	private String requestor_info;		// ����� NAME
	private String requestor_div_code;	// ����� �μ��ڵ�
	private String requestor_div_name;	// ����� �μ���
	private String reg_date;			// �̵�����

    // setter...getter...
	public void setMid(String string)	{	mid = string;}
	public String getMid()				{	return mid;}

	public void setShiftNo(String string)	{	shift_no = string;}
	public String getShiftNo()				{	return shift_no;}

	public void setShiftType(String string)	{	shift_type = string;}
	public String getShiftType()			{	return shift_type;}

	public void setSrFactoryCode(String string)		{	sr_factory_code = string;}
	public String getSrFactoryCode()				{	return sr_factory_code;}

	public void setSrFactoryName(String string)		{	sr_factory_name = string;}
	public String getSrFactoryName()				{	return sr_factory_name;}

	public void setSrWarehouseCode(String string)	{	sr_warehouse_code = string;}
	public String getSrWarehouseCode()				{	return sr_warehouse_code;}

	public void setSrWarehouseName(String string)	{	sr_warehouse_name = string;}
	public String getSrWarehouseName()				{	return sr_warehouse_name;}

	public void setSrItemCode(String string)	{	sr_item_code = string;}
	public String getSrItemCode()				{	return sr_item_code;}

	public void setSrItemName(String string)	{	sr_item_name = string;}
	public String getSrItemName()				{	return sr_item_name;}

	public void setSrItemType(String string)	{	sr_item_type = string;}
	public String getSrItemType()				{	return sr_item_type;}

	public void setSrItemDesc(String string)	{	sr_item_desc = string;}
	public String getSrItemDesc()				{	return sr_item_desc;}

	public void setDtFactoryCode(String string)		{	dt_factory_code = string;}
	public String getDtFactoryCode()				{	return dt_factory_code;}

	public void setDtFactoryName(String string)		{	dt_factory_name = string;}
	public String getDtFactoryName()				{	return dt_factory_name;}

	public void setDtWarehouseCode(String string)	{	dt_warehouse_code = string;}
	public String getDtWarehouseCode()				{	return dt_warehouse_code;}

	public void setDtWarehouseName(String string)	{	dt_warehouse_name = string;}
	public String getDtWarehouseName()				{	return dt_warehouse_name;}

	public void setDtItemCode(String string)	{	dt_item_code = string;}
	public String getDtItemCode()				{	return dt_item_code;}

	public void setDtItemName(String string)	{	dt_item_name = string;}
	public String getDtItemName()				{	return dt_item_name;}

	public void setDtItemType(String string)	{	dt_item_type = string;}
	public String getDtItemType()				{	return dt_item_type;}

	public void setDtItemDesc(String string)	{	dt_item_desc = string;}
	public String getDtItemDesc()				{	return dt_item_desc;}

	public void setStockUnit(String string)		{	stock_unit = string;}
	public String getStockUnit()				{	return stock_unit;}

	public void setQuantity(String string)		{	quantity = string;}
	public String getQuantity()					{	return quantity;}

	public void setRequestorId(String string)		{	requestor_id = string;}
	public String getRequestorId()					{	return requestor_id;}

	public void setRequestorInfo(String string)		{	requestor_info = string;}
	public String getRequestorInfo()				{	return requestor_info;}

	public void setRegDate(String string)			{	reg_date = string;}
	public String getRegDate()						{	return reg_date;}

	public void setRequestorDivCode(String string)	{	requestor_div_code = string;}
	public String getRequestorDivCode()				{	return requestor_div_code;}

	public void setRequestorDivName(String string)	{	requestor_div_name = string;}
	public String getRequestorDivName()				{	return requestor_div_name;}

	
}
