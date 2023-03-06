package com.anbtech.ew.entity;

public class EWLinkTable
{
	private String user_id;
	private String user_name;
	private String user_rank;
	private String user_rankid;
	private String pagecut;
	private String link_write;
	private String input_hidden;
	private int total;
	private String page;
	private int totalpage;
	private String where_category;

	// SETTER
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

	public void setViewPagecut(String pagecut){
		this.pagecut = pagecut;
	}
	public void setLinkWriter(String link_write){
		this.link_write = link_write;
	}
	public void setInputHidden(String input_hidden){
		this.input_hidden = input_hidden;
	}
	public void setViewTotal(int total){
		this.total = total;
	}
    public void	setViewBoardpage(String page){
		this.page = page;
	}
	public void setViewtotalpage(int totalpage){
		this.totalpage = totalpage;
	}
	public void setWhereCategory(String where_category){
		this.where_category = where_category;
	}


	// GETTER
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

	public String getViewPagecut(){
		return pagecut;
	}
	public String getLinkWriter(){
		return link_write;
	}
	public String getInputHidden(){
		return input_hidden;
	}
	public int getViewTotal(){
		return total;
	}
    public String getViewBoardpage(){
		return page;
	}
	public int getViewtotalpage(){
		return totalpage;
	}
	public String getWhereCategory(){
		return where_category;
	}

}

