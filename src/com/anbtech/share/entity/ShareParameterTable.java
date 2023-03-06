/************************************************************
 * ���� ��ũ ���ڿ��� ����µ� ���� ������ getter/setter
 ************************************************************/

package com.anbtech.share.entity;

public class ShareParameterTable {

	private String id;
	private String name;
	private String searchword;			// �˻��ܾ�
	private String searchscope;			// �˻�����
	private String mode;				// �˻����
	private String category;			// ī�װ�
	private String categorycombo;
	private String tablename;			// ���̺��
	private String page;				// ���������� ǥ�� ���ڿ�
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