/************************************************************
 * car_info ���̺� getter/setter
 ************************************************************/

package com.anbtech.br.entity;

public class CarInfoTable{

	private String cid;				// ������ȣ
	private String car_type;		// ����
	private String car_no;			// ������ȣ
	private String model_name;		// �𵨸�
	private String produce_year;	// ���
	private String buy_date;		// ������
	private String price;			// ���԰�
	private String fuel_type;		// ���ᱸ��
	private String fuel_efficiency;	// ����
	private String maker_company;	// ��������Ŀ
	private String reg_date;		// �����
	private String stat;			// ��������
	private String car_id;			// ���� ������ȣ

	public String getCid(){
		return cid;
	}
	public void setCid(String cid){
		this.cid = cid;
	}

	public String getCarType(){
		return car_type;
	}
	public void setCarType(String car_type){
		this.car_type = car_type;
	}

	public String getCarNo(){
		return car_no;
	}
	public void setCarNo(String car_no){
		this.car_no = car_no;
	}

	public String getModelName(){
		return model_name;
	}
	public void setModelName(String model_name){
		this.model_name = model_name;
	}

	public String getProduceYear(){
		return produce_year;
	}
	public void setProduceYear(String produce_year){
		this.produce_year = produce_year;
	}

	public String getBuyDate(){
		return buy_date;
	}
	public void setBuyDate(String buy_date){
		this.buy_date = buy_date;
	}

	public String getPrice(){
		return price;
	}
	public void setPrice(String price){
		this.price = price;
	}

	public String getFuelType(){
		return fuel_type;
	}
	public void setFuelType(String fuel_type){
		this.fuel_type = fuel_type;
	}

	public String getFuelEfficiency(){
		return fuel_efficiency;
	}
	public void setFuelEfficiency(String fuel_efficiency){
		this.fuel_efficiency = fuel_efficiency;
	}

	public String getMakerCompany(){
		return maker_company;
	}
	public void setMakerCompany(String maker_company){
		this.maker_company = maker_company;
	}

	public String getRegDate(){
		return reg_date;
	}
	public void setRegDate(String reg_date){
		this.reg_date = reg_date;
	}

	public String getStat(){
		return stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}

	public String getCarId(){
		return car_id;
	}
	public void setCarId(String car_id){
		this.car_id = car_id;
	}
}