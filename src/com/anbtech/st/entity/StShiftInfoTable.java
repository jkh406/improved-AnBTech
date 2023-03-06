/**************************
* StShiftInfoTable
* 재고 이동 정보 테이블
**************************/
package com.anbtech.st.entity;

public class StShiftInfoTable{

	private String mid;					// 관리번호
	private String shift_no;			// 이동번호
	private String shift_type;			// 이동유형
	private String sr_factory_code;		// source 공장코드
	private String sr_factory_name;		// source 공장명
	private String sr_warehouse_code;	// source 창고코드
	private String sr_warehouse_name;	// source 창고명
	private String sr_item_code;		// source 품목코드
	private String sr_item_name;		// source 품목명
	private String sr_item_type;		// source 품목유형
	private String sr_item_desc;		// source 품목설명
	private String dt_factory_code;		// target 공장코드
	private String dt_factory_name;		// target 공장명
	private String dt_warehouse_code;	// target 창고코드
	private String dt_warehouse_name;	// target 창고명
	private String dt_item_code;		// target 품목코드
	private String dt_item_name;		// target 품목명
	private String dt_item_type;		// target 품목유형
	private String dt_item_desc;		// target 품목설명
	private String stock_unit;			// 재고 단위
	private String quantity;			// 재고 수량
	private String requestor_id;		// 등록자 ID
	private String requestor_info;		// 등록자 NAME
	private String requestor_div_code;	// 등록자 부서코드
	private String requestor_div_name;	// 등록자 부서명
	private String reg_date;			// 이동일자

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
