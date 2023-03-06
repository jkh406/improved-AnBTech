/*****************************************
*  입고/출고 형태 등록관리 helper class
*  InOutTypeTable.java
******************************************/

package com.anbtech.pu.entity;

public class InOutTypeTable
{
	private String mid;		// 관리번호
	private String type;	// 처리(입고/출고/매입)형태
	private String name;	// 처리(입고/출고/매입)명
    // 추가
	private String	is_import;			// 수입여부(y or n)
	private String	is_enter;			// 입고여부(y or n)
	private String	is_return;			// 반품여부(y or n)
	private String	is_sageup;			// 사급여부(y or n)
	private String	is_using;			// 사용여부(y or n)
	private String  stock_type;			// 재고처리형태

	public void setMid(String mid) { this.mid = mid; }
	public String  getMid()			{ return mid;     }

	public void setType(String type)	{ this.type = type; }
	public String getType()				{ return type;		}

	public void setName(String name)	{ this.name = name; }
	public String getName()				{ return name;		}

	public void setIsImport(String is_import)	{	this.is_import = is_import; }
	public String getIsImport()					{	return is_import;}

	public void setIsEnter(String is_enter)		{	this.is_enter = is_enter; }
	public String getIsEnter()					{	return is_enter;}

	public void setIsReturn(String is_return)	{	this.is_return = is_return; }
	public String getIsReturn()					{	return is_return;}

	public void setIsSageup(String is_sageup)			{	this.is_sageup = is_sageup; }
	public String getIsSageup()							{	return is_sageup;}

	public void setIsUsing(String is_using)				{	this.is_using = is_using; }
	public String getIsUsing()							{	return is_using;}

	public void setStockType(String stock_type)			{	this.stock_type = stock_type; }
	public String getStockType()						{	return stock_type;}


}
