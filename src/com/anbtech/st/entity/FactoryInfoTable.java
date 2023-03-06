/**********************************
* helper class!	
* FactoryInfoTable
**********************************/

package com.anbtech.st.entity;

public class FactoryInfoTable 
{
	private String mid;					// 관리번호
	private String factory_code;		// 공장코드
	private String factory_name;		// 공장명
	private String production_type;		// 생산타입(외주생산, 자사생산)
	private String main_product;		// 주 생산품목
	private String factory_address;		// 공장 주소
	private String product_plan_term;	// 생산계획기간 (일단위)
	private String mps_confirm_term;	// mps확정기간 (일단위)
	private String mps_plan_term;		// mps계획기간 (일단위)
	private String mrp_confirm_term;	// mrp확정기간 (일단위)
	private String agency_code;			// 사업장코드
	private String agency_name;			// 사업장명

	public String getMid()				{	return mid;				}
	public void setMid(String mid)	{	this.mid = mid;			}
		
	public String getFactoryCode()	{	return factory_code;	}
	public void setFactoryCode(String factory_code)	{	this.factory_code = factory_code;}

	public String getFactoryName()	{	return factory_name;	}
	public void setFactoryName(String factory_name){	this.factory_name = factory_name;}

	public String getProductionType()	{	return production_type;	}
	public void setProductionType(String production_type){	this.production_type = production_type;}

	public String getMainProduct()	{	return main_product;	}
	public void setMainProduct(String main_product){	this.main_product = main_product;}

	public String getFactoryAddress(){	return factory_address;	}
	public void setFactoryAddress(String factory_address){	this.factory_address = factory_address;	}

	public String getProductPlanTerm()		{	return product_plan_term;	}
	public void setProductPlanTerm(String product_plan_term){
		this.product_plan_term = product_plan_term;	
	}
	public String getMpsConfirmTerm()		{	return mps_confirm_term;	}
	public void setMpsConfirmTerm(String mps_confirm_term){	this.mps_confirm_term = mps_confirm_term;	}

	public String getMpsPlanTerm()	{	return mps_plan_term;}
	public void setMpsPlanTerm(String mps_plan_term){	this.mps_plan_term = mps_plan_term;	}

	public String getMrpConfirmTerm()		{	return mrp_confirm_term;	}
	public void setMrpConfirmTerm(String mrp_confirm_term){	this.mrp_confirm_term = mrp_confirm_term;	}

	public String getAgencyCode()				{	return agency_code;}
	public void setAgencyCode(String agency_code)	{	this.agency_code = agency_code;}

	public String getAgencyName()				{	return agency_name;}
	public void setAgencyName(String agency_name)	{	this.agency_name = agency_name;}

}	
