package com.anbtech.qc.entity;

public class InspectionResultTable {
	private String mid;
	private String sampled_quantity;	//시료수
	private String good_quantity;		//합격수량
	private String bad_quantity;		//불합격수량

	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getSampledQuantity() { return sampled_quantity; }
	public void setSampledQuantity(String string) { sampled_quantity = string; }

	public String getGoodQuantity() { return good_quantity; }
	public void setGoodQuantity(String string) { good_quantity = string; }

	public String getBadQuantity() { return bad_quantity; }
	public void setBadQuantity(String string) { bad_quantity = string; }
}
