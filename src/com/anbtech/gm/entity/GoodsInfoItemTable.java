package com.anbtech.gm.entity;

/**
 *
 * ��ǰ�� ǥ�����ø��׸��� set/get �ϴ� helper class
 */
public class GoodsInfoItemTable {
	private String one_class;	// ��ǰ���ڵ�
	private String two_class;	// ��ǰ�ڵ�
	private String item_code;	// �׸��ڵ�(��ǰ�ڵ�+�ø���(3�ڸ�))
	private String item_name; 	// �׸��(��������,���ⱹ ��)
	private String item_value; 	// �׸�
	private String item_unit;	// �׸����
	private String write_exam;	// �ۼ���
	private String item_desc;	// �׸� ����
	private String link_mod;	// ���� ��ũ ���ڿ�
	private String link_del;	// ���� ��ũ ���ڿ�

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