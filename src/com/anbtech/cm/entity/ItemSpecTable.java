package com.anbtech.cm.entity;

public class ItemSpecTable {
	private String mid;					//관리번호
	private String code_big;			//대분류코드
	private String code_mid;			//중분류코드
	private String code_small;			//소분류코드
	private String code_str;			//코드문자열
	private String spec_code;			//스펙코드
	private String is_essence;			//필수여부
	private String is_desc;				//description여부

	public String getMid() {	return mid;	}
	public void setMid(String string) {	mid = string;	}

	public String getCodeBig() {	return code_big;	}
	public void setCodeBig(String string) {	code_big = string;	}

	public String getCodeMid() {	return code_mid;	}
	public void setCodeMid(String string) {	code_mid = string;	}

	public String getCodeSmall() {	return code_small;	}
	public void setCodeSmall(String string) {	code_small = string;	}

	public String getCodeStr() {	return code_str;	}
	public void setCodeStr(String string) {	code_str = string;	}

	public String getSpecCode() {	return spec_code;	}
	public void setSpecCode(String string) {	spec_code = string;	}

	public String getIsEssence() {	return is_essence;	}
	public void setIsEssence(String string) {	is_essence = string;	}

	public String getIsDesc() {	return is_desc;	}
	public void setIsDesc(String string) {	is_desc = string;	}
}