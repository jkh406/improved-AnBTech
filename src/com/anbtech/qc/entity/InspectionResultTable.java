package com.anbtech.qc.entity;

public class InspectionResultTable {
	private String mid;
	private String sampled_quantity;	//�÷��
	private String good_quantity;		//�հݼ���
	private String bad_quantity;		//���հݼ���

	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getSampledQuantity() { return sampled_quantity; }
	public void setSampledQuantity(String string) { sampled_quantity = string; }

	public String getGoodQuantity() { return good_quantity; }
	public void setGoodQuantity(String string) { good_quantity = string; }

	public String getBadQuantity() { return bad_quantity; }
	public void setBadQuantity(String string) { bad_quantity = string; }
}
