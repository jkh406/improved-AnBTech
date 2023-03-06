/**********************************
* helper class!	
* FactoryInfoTable
**********************************/

package com.anbtech.st.entity;

public class FactoryInfoTable 
{
	private String mid;					// ������ȣ
	private String factory_code;		// �����ڵ�
	private String factory_name;		// �����
	private String production_type;		// ����Ÿ��(���ֻ���, �ڻ����)
	private String main_product;		// �� ����ǰ��
	private String factory_address;		// ���� �ּ�
	private String product_plan_term;	// �����ȹ�Ⱓ (�ϴ���)
	private String mps_confirm_term;	// mpsȮ���Ⱓ (�ϴ���)
	private String mps_plan_term;		// mps��ȹ�Ⱓ (�ϴ���)
	private String mrp_confirm_term;	// mrpȮ���Ⱓ (�ϴ���)
	private String agency_code;			// ������ڵ�
	private String agency_name;			// ������

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
