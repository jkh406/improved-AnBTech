/********************************
*  �������� ��ϰ��� helper class
*  OrderTypeTable.java
*********************************/

package com.anbtech.pu.entity;

public class OrderTypeTable
{
	private String	mid;				// ������ȣ
	private String	order_type;			// �������� �ڵ�
	private String	order_name;			// �������� ��
	private String	is_import;			// ���Կ���(y or n)
	private String	is_shipping;		// ��������(y or n)
	private String	is_pass;			// �������(y or n)
	private String	is_enter;			// �԰���(y or n)
	private String	is_purchase;		// ���Կ���(y or n)
	private String	is_return;			// ��ǰ����(y or n)
	private String	is_sageup;			// ��޿���(y or n)
	private String	is_using;			// ��뿩��(y or n)
	private String	enter_code;			// �԰����� �ڵ�(����)
	private String	enter_name;			// �԰����� ��
	private String	outgo_code;			// ������� �ڵ�(����)
	private String	outgo_name;			// ������� ��
	private String	purchase_code;		// �������� �ڵ�(����)
	private String	purchase_name;		// �������� ��

	public void setMid(String mid)  { this.mid = mid; }
	public String getMid()			{ return mid;}

	public void setOrderType(String order_type)	{	this.order_type = order_type; }
	public String getOrderType()					{	return order_type;}

	public void setOrderName(String order_name)	{	this.order_name = order_name; }
	public String getOrderName()					{	return order_name;}

	public void setIsImport(String is_import)	{	this.is_import = is_import; }
	public String getIsImport()					{	return is_import;}

	public void setIsShipping(String is_shipping)	{	this.is_shipping = is_shipping; }
	public String getIsShipping()					{	return is_shipping;}

	public void setIsPass(String is_pass)		{	this.is_pass = is_pass; }
	public String getIsPass()					{	return is_pass;}

	public void setIsEnter(String is_enter)		{	this.is_enter = is_enter; }
	public String getIsEnter()					{	return is_enter;}

	public void setIsReturn(String is_return)	{	this.is_return = is_return; }
	public String getIsReturn()					{	return is_return;}

	public void setIsPurchase(String is_purchase)		{	this.is_purchase = is_purchase; }
	public String getIsPurchase()						{	return is_purchase;}

	public void setIsSageup(String is_sageup)			{	this.is_sageup = is_sageup; }
	public String getIsSageup()							{	return is_sageup;}

	public void setIsUsing(String is_using)				{	this.is_using = is_using; }
	public String getIsUsing()							{	return is_using;}

	public void setEnterCode(String enter_code)				{	this.enter_code = enter_code; }
	public String getEnterCode()							{	return enter_code;}

	public void setEnterName(String enter_name)				{	this.enter_name = enter_name; }
	public String getEnterName()							{	return enter_name;}

	public void setOutgoCode(String outgo_code)				{	this.outgo_code = outgo_code; }
	public String getOutgoCode()							{	return outgo_code;}

	public void setOutgoName(String outgo_name)				{	this.outgo_name = outgo_name; }
	public String getOutgoName()							{	return outgo_name;}

	public void setPurchaseCode(String purchase_code)		{	this.purchase_code = purchase_code; }
	public String getPurchaseCode()							{	return purchase_code;}

	public void setPurchaseName(String purchase_name)		{	this.purchase_name = purchase_name; }
	public String getPurchaseName()							{	return purchase_name;}
}
