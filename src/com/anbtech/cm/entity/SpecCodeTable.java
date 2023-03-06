package com.anbtech.cm.entity;

public class SpecCodeTable {
	private String mid;					//관리번호
	private String code_big;			//대분류코드
	private String code_mid;			//중분류코드

	private String spec_code;			//스펙코드
	private String spec_name;			//스펙명
	private String spec_value;			//스펙값의 형태
	private String spec_unit;			//스펙단위의 형태
	private String write_exam;			//입력예
	private String spec_desc;			//설명
	private String is_essence;			//필수여부
	private String is_desc;				//description여부

	private String link_modify;			//수정버튼

	public String getMid() {	return mid;	}
	public void setMid(String string) {	mid = string;	}

	public String getCodeBig() {	return code_big;	}
	public void setCodeBig(String string) {	code_big = string;	}

	public String getCodeMid() {	return code_mid;	}
	public void setCodeMid(String string) {	code_mid = string;	}

	public String getSpecCode() {	return spec_code;	}
	public void setSpecCode(String string) {	spec_code = string;	}

	public String getSpecName() {	return spec_name;	}
	public void setSpecName(String string) {	spec_name = string;	}

	public String getSpecValue() {	return spec_value;	}
	public void setSpecValue(String string) {	spec_value = string;	}

	public String getSpecUnit() {	return spec_unit;	}
	public void setSpecUnit(String string) {	spec_unit = string;	}

	public String getWriteExam() {	return write_exam;	}
	public void setWriteExam(String string) {	write_exam = string;	}

	public String getSpecDesc() {	return spec_desc;	}
	public void setSpecDesc(String string) {	spec_desc = string;	}

	public String getIsEssence() {	return is_essence;	}
	public void setIsEssence(String string) {	is_essence = string;	}

	public String getIsDesc() {	return is_desc;	}
	public void setIsDesc(String string) {	is_desc = string;	}

	public String getLinkModify() {	return link_modify;	}
	public void setLinkModify(String string) {	link_modify = string;	}

}