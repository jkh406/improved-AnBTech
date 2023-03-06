/*****************************************
*  매입 형태 등록관리 helper class
*  PurchaseTypeTable.java
******************************************/

package com.anbtech.pu.entity;

public class PurchaseTypeTable
{
	private String mid;					// 관리번호
	private String purchase_type;		// 매입형태
	private String purchase_name;		// 매입명
    // 추가
	private String is_import;			// 수입여부(y or n)
	private String is_return;			// 반품여부(y or n)
	private String is_using;			// 사용여부(y or n)
	private String is_except;			// 예외여부(y or n)
	private String account_type;		// 회계처리형태(y or n)

	public void setMid(String mid)		{ this.mid = mid; }
	public String  getMid()				{ return mid;     }

	public void setPurchaseType(String purchase_type)	{ this.purchase_type = purchase_type; }
	public String getPurchaseType()						{ return purchase_type;		}

	public void setPurchaseName(String purchase_name)	{ this.purchase_name = purchase_name; }
	public String getPurchaseName()						{ return purchase_name;		}

	public void setIsImport(String is_import)			{	this.is_import = is_import; }
	public String getIsImport()							{	return is_import;}

	public void setIsReturn(String is_return)			{	this.is_return = is_return; }
	public String getIsReturn()							{	return is_return;}

	public void setIsUsing(String is_using)				{	this.is_using = is_using; }
	public String getIsUsing()							{	return is_using;}

	public void setIsExcept(String is_except)			{	this.is_except = is_except; }
	public String getIsExcept()							{	return is_except;}

	public void setAccountType(String account_type)		{	this.account_type = account_type; }
	public String getAccountType()						{	return account_type;}


}
