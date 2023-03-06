/************************************************************
 * 각종 링크 문자열을 만드는데 사용될 변수의 getter/setter
 ************************************************************/

package com.anbtech.share.entity;

public class ShareParameterTable {

	private String id;
	private String name;
	private String searchword;			// 검색단어
	private String searchscope;			// 검색범위
	private String mode;				// 검색모드
	private String category;			// 카테고리
	private String categorycombo;
	private String tablename;			// 테이블명
	private String page;				// 현재페이지 표시 문자열
	private boolean bool;

	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getCategoryCombo(){
		return categorycombo;
	}
	public void setCategoryCombo(String categorycombo){
		this.categorycombo = categorycombo;
	}
	public String getPage(){
		return page;
	}
	public void setPage(String page){
		this.page = page;
	}

	public String getSearchWord(){
		return searchword;
	}
	public void setSearchWord(String searchword){
		this.searchword = searchword;
	}
	public String getSearchScope(){
		return searchscope;
	}
	public void setSearchScope(String searchscope) {
		this.searchscope = searchscope;
	}
	public String getMode(){
		return mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
	public String getCategory(){
		return category;
	}
	public void setCategory(String category){
		this.category = category;
	}
	public String getTableName(){
		return tablename;
	}
	public void setTableName(String tablename){
		this.tablename = tablename;
	}
	public boolean getBool(){
		return bool;
	}
	public void setBool(boolean bool){
		this.bool = bool;
	}

}