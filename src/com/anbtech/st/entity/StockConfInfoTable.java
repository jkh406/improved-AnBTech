/*****************************************
*  수불 유형 등록관리 helper class
*  StockConfInfoTable.java
******************************************/

package com.anbtech.st.entity;

public class StockConfInfoTable
{
	private String mid;					// 관리번호
	private String trade_type_code;		// 수불 구분 코드
	private String trade_type_name;		// 수불 명
	private String stock_rise_fall;		// 재고 증감 구분 (1:증가 2:감소 3:무관)
	private String stock_type1;			// 재고유형1
	private String stock_type2;			// 재고유형2
	private String is_cost_apply;		// 재고단가반영구분(1:YES 2:NO)
	private String is_count_posting;	// 회계 posting 구분
	private String is_wharehouse_move;	// 창고간 이동여부
	private String is_factory_move;		// 공장간 이동여부
	private String is_item_move;		// 품목간 이동여부
	private String is_no_move;			// 제번간 이동여부

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
