package com.anbtech.cm.entity;

public class ItemClassTable {
	private String mid;					//������ȣ
	private String item_code;			//ǰ��з��ڵ�
	private String item_name;			//ǰ��з���
	private String item_desc;			//ǰ�񼳸�
	private String item_level;			//�з�����
	private String item_ancestor;		//�����з�������ȣ

	public String getMid() {	return mid;	}
	public void setMid(String string) {	mid = string;	}

	public String getItemCode() {	return item_code;	}
	public void setItemCode(String string) {	item_code = string;	}

	public String getItemName() {	return item_name;	}
	public void setItemName(String string) {	item_name = string;	}

	public String getItemDesc() {	return item_desc;	}
	public void setItemDesc(String string) {	item_desc = string;	}

	public String getItemLevel() {	return item_level;	}
	public void setItemLevel(String string) {	item_level = string;	}

	public String getItemAncestor() {	return item_ancestor;	}
	public void setItemAncestor(String string) {	item_ancestor = string;	}
}