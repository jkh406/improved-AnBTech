package com.anbtech.gm.entity;

/**
 *
 * 제품별 표준템플릿항목을 set/get 하는 helper class
 */
public class GoodsInfoItemTable {
	private String one_class;	// 제품군코드
	private String two_class;	// 제품코드
	private String item_code;	// 항목코드(제품코드+시리얼(3자리))
	private String item_name; 	// 항목명(수출지역,수출국 등)
	private String item_value; 	// 항목값
	private String item_unit;	// 항목단위
	private String write_exam;	// 작성예
	private String item_desc;	// 항목 설명
	private String link_mod;	// 수정 링크 문자열
	private String link_del;	// 삭제 링크 문자열

	public String getOneClass() {	return one_class;	}
	public void setOneClass(String string) {	one_class = string;	}

	public String getTwoClass() {	return two_class;	}
	public void setTwoClass(String string) {	two_class = string;	}

	public String getItemCode() {	return item_code;	}
	public void setItemCode(String string) {	item_code = string;	}

	public String getItemName() {	return item_name;	}
	public void setItemName(String string) {	item_name = string;	}

	public String getItemValue() {	return item_value;	}
	public void setItemValue(String string) {	item_value = string;	}

	public String getItemUnit() {	return item_unit;	}
	public void setItemUnit(String string) {	item_unit = string;	}

	public String getWriteExam() {	return write_exam;	}
	public void setWriteExam(String string) {	write_exam = string;	}

	public String getItemDesc() {	return item_desc;	}
	public void setItemDesc(String string) {	item_desc = string;	}

	public String getLinkMod() {	return link_mod;	}
	public void setLinkMod(String string) {	link_mod = string;	}

	public String getLinkDel() {	return link_del;	}
	public void setLinkDel(String string) {	link_del = string;	}
}