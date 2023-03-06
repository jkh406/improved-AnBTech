/************************************************************
 * AsCategoryTable ���̺� getter/setter
 ************************************************************/

package com.anbtech.am.entity;

public class AsCategoryTable
{
	private int c_no;			// ������ȣ	    [c_no]	     
	private String ct_id;		// ī�װ�ID	[ct_id]	     
	private String ct_level;	// ī�װ����	[ct_level]   
	private String ct_parent;	// �θ�ī�װ�	[ct_parent]  
	private String ct_word;		// �ڻ�ǰ�����	[ct_word]    
	private String ct_name;     // ī�װ��̸�	[ct_name]    
    private String dc_percent;  // ��������		[dc_percent] 
	
	private String apply_dc;    // �������� ���� (y or n)

	// setXX method
	public void setCno(int c_no){
		this.c_no = c_no;
	}
	public void setCtId(String ct_id){
		this.ct_id = ct_id;
	}
	public void setCtLevel(String ct_level){
		this.ct_level = ct_level;
	}
	public void setCtParent(String ct_parent){
		this.ct_parent = ct_parent;
	}
	public void setCtWord(String ct_word){
		this.ct_word = ct_word;		
	}
	public void setCtName(String ct_name){
		this.ct_name = ct_name;
	}
	public void setDcPercent(String dc_percent){
		this.dc_percent = dc_percent;
	}
	
	public void setApplyDc(String apply_dc){
		this.apply_dc = apply_dc;
	}

	//getXX method
	public int getCno(){
		return c_no;
	}
	public String getCtId(){
		return ct_id;
	}

	public String getCtLevel(){
		return ct_level;
	}
	public String getCtParent(){
		return ct_parent;
	}
	public String getCtWord(){
		return ct_word;		
	}
	public String getCtName(){
	    return ct_name;
	}
	public String getDcPercent(){
		return dc_percent;
	}
	
	public String getApplyDc(){
		return apply_dc;
	}
}