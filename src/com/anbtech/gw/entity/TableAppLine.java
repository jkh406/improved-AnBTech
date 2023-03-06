package com.anbtech.gw.entity;
public class TableAppLine
{
	private String status;
	private String sabun;
	private String name;
	private String rank;
	private String division;
	private String date;
	private String comment;

	public void setApStatus(String status) {
		this.status = status;
	}
	public String getApStatus() {
		return this.status;
	}

	public void setApSabun(String sabun) {
		this.sabun = sabun;
	}
	public String getApSabun() {
		return this.sabun;
	}
	
	public void setApName(String name) {
		this.name = name;
	}
	public String getApName() {
		return this.name;
	}
	
	public void setApRank(String rank) {
		this.rank = rank;
	}
	public String getApRank() {
		return this.rank;
	}
	
	public void setApDivision(String division) {
		this.division = division;
	}
	public String getApDivision() {
		return this.division;
	}

	public void setApDate(String date) {
		this.date = date;
	}
	public String getApDate() {
		return this.date;
	}
	
	public void setApComment(String comment) {
		this.comment = comment;
	}
	public String getApComment() {
		return this.comment;
	}
}
