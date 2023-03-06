/************************************************************
 * 
 * history_info 테이블 내용을 set/get
 *
 ************************************************************/

package com.anbtech.ca.entity;

public class CaHistoryInfoTable{

	private String hid;
	private String item_no;
	private String contents;
	private String apply_date;
	private String requestor_info;

	public String getHid() {
		return this.hid;
	}
	public void setHid(String hid){
		this.hid = hid;
	}

	public String getItemNo() {
		return this.item_no;
	}
	public void setItemNo(String item_no){
		this.item_no = item_no;
	}

	public String getContents(){
		return this.contents;
	}
	public void setContents(String contents){
		this.contents = contents;
	}

	public String getApplyDate(){
		return this.apply_date;
	}
	public void setApplyDate(String apply_date){
		this.apply_date = apply_date;
	}

	public String getRequestorInfo() {
		return this.requestor_info;
	}
	public void setRequestorInfo(String requestor_info){
		this.requestor_info = requestor_info;
	}
}