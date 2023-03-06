/*****************************************
*  ���� ���� ��ϰ��� helper class
*  StockConfInfoTable.java
******************************************/

package com.anbtech.st.entity;

public class StockConfInfoTable
{
	private String mid;					// ������ȣ
	private String trade_type_code;		// ���� ���� �ڵ�
	private String trade_type_name;		// ���� ��
	private String stock_rise_fall;		// ��� ���� ���� (1:���� 2:���� 3:����)
	private String stock_type1;			// �������1
	private String stock_type2;			// �������2
	private String is_cost_apply;		// ���ܰ��ݿ�����(1:YES 2:NO)
	private String is_count_posting;	// ȸ�� posting ����
	private String is_wharehouse_move;	// â�� �̵�����
	private String is_factory_move;		// ���尣 �̵�����
	private String is_item_move;		// ǰ�� �̵�����
	private String is_no_move;			// ������ �̵�����

	public void setMid(String string)	{	mid = string;}
	public String getMid()				{	return mid;}

	public void setTradeTypeCode(String string)			{	trade_type_code = string;}
	public String getTradeTypeCode()					{	return trade_type_code;}

	public void setTradeTypeName(String string)			{	trade_type_name = string;}
	public String getTradeTypeName()					{	return trade_type_name;}

	public void setStockRiseFall(String string)			{	stock_rise_fall = string;}
	public String getStockRiseFall()					{	return stock_rise_fall;}

	public void setStockType1(String string)			{	stock_type1 = string;}
	public String getStockType1()						{	return stock_type1;}

	public void setStockType2(String string)			{	stock_type2 = string;}
	public String getStockType2()						{	return stock_type2;}

	public void setIsCostApply(String string)			{	is_cost_apply = string;}
	public String getIsCostApply()						{	return is_cost_apply;}

	public void setIsCountPosting(String string)		{	is_count_posting = string;}
	public String getIsCountPosting()					{	return is_count_posting;}

	public void setIsWharehouseMove(String string)		{	is_wharehouse_move = string;}
	public String getIsWharehouseMove()					{	return is_wharehouse_move;}

	public void setIsFactoryMove(String string)			{	is_factory_move = string;}
	public String getIsFactoryMove()					{	return is_factory_move;}
	
	public void setIsItemMove(String string)			{	is_item_move = string;}
	public String getIsItemMove()						{	return is_item_move;}
	
	public void setIsNoMove(String string)				{	is_no_move = string;}
	public String getIsNoMove()							{	return is_no_move;}
}
