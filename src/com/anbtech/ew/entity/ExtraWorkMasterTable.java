// ExtraWorkMaterTable.java
package com.anbtech.ew.entity;

public class ExtraWorkMasterTable
{
	private int mno;			// table 관리번호
	private String ew_id;		// 문서번호
	private String ys_kind;
	private String hd_var;
	private String user_id;		
	private String user_name;
	private String user_rank;
	private String ac_code;
	private String ac_name;
	private String w_date;		// 상신일(작성일)
	private String status;		// 상신 상태 코드
	private String worker;		// 작업 대상자 string

	// setter....
	public void setMno(int mno){
		this.mno = mno;
	}
	public void setEwId(String ew_id){
		this.ew_id = ew_id;
	}
	public void setYsKind(String ys_kind){
		this.ys_kind = ys_kind;
	}
	public void setHdVar(String hd_var){
		this.hd_var = hd_var;
	}
	public void setUserId(String user_id){
		this.user_id = user_id;
	}
	public void setUserName(String user_name){
		this.user_name = user_name;
	}
	public void setUserRank(String user_rank){
		this.user_rank = user_rank;
	}
	public void setAcCode(String ac_code){
		this.ac_code = ac_code;
	}
	public void setAcName(String ac_name){
		this.ac_name = ac_name;
	}
	public void setWdate(String w_date){
		this.w_date = w_date;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setWorker(String worker){
		this.worker = worker;
	}

	// getter....
	public int getMno(){
		return mno;
	}
	public String getEwId(){
		return ew_id;
	}
	public String getYsKind(){
		return ys_kind;
	}
	public String getHdVar(){
		return hd_var;
	}
	public String getUserId(){
		return user_id;
	}
	public String getUserName(){
		return user_name;
	}
	public String getUserRank(){
		return user_rank;
	}
	public String getAcCode(){
		return ac_code;
	}
	public String getAcName(){
		return ac_name;
	}
	public String getWdate(){
		return w_date;
	}
	public String getStatus(){
		return status;
	}
	public String getWorker(){
		return worker;
	}

}
