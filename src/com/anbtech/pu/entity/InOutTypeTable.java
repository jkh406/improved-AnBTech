/*****************************************
*  �԰�/��� ���� ��ϰ��� helper class
*  InOutTypeTable.java
******************************************/

package com.anbtech.pu.entity;

public class InOutTypeTable
{
	private String mid;		// ������ȣ
	private String type;	// ó��(�԰�/���/����)����
	private String name;	// ó��(�԰�/���/����)��
    // �߰�
	private String	is_import;			// ���Կ���(y or n)
	private String	is_enter;			// �԰���(y or n)
	private String	is_return;			// ��ǰ����(y or n)
	private String	is_sageup;			// ��޿���(y or n)
	private String	is_using;			// ��뿩��(y or n)
	private String  stock_type;			// ���ó������

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
