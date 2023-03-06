package com.anbtech.am.entity;

public class AMUserTable
{
	private String user_id;
	private String user_name;
	private String user_rank;
	private String user_rankid;
	private String where;



	public void setUserId(String user_id){
		this.user_id = user_id;
	}

	public void setUserName(String user_name){
		this.user_name = user_name;
	}

	public void setUserRank(String user_rank){
		this.user_rank = user_rank;
	}

	public void setUser_RankId(String user_rankid){
		this.user_rankid = user_rankid;
	}
	public void setWhere(String where){
		this.where = where;
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

	public String getUserRankId(){
		return user_rankid;
	}
	public String getWhere(){
		return where;
	}
}

