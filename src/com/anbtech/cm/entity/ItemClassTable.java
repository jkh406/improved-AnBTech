package com.anbtech.cm.entity;

public class ItemClassTable {
	private String mid;					//관리번호
	private String item_code;			//품목분류코드
	private String item_name;			//품목분류명
	private String item_desc;			//품목설명
	private String item_level;			//분류레벨
	private String item_ancestor;		//상위분류관리번호

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