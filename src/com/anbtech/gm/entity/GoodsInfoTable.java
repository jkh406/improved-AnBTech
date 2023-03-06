package com.anbtech.gm.entity;

public class GoodsInfoTable {
	private String mid;					//������ȣ
	private String code;				//��ǰ(��)�ڵ�
	private String name;				//��ǰ(��)��
	private String name2;				//��ǰ(��)��2
	private String short_name;			//�𵨾��
	private String color_code;			//�𵨻����ڵ�
	private String color_name;			//�𵨻���
	private String other_info;			//��Ÿ ����
	private String fg_code;				//F/G�ڵ�

	private String glevel;				//�з��ܰ�
	private String ancestor;			//�����з�������ȣ
	private String gcode;				//�����ڵ�
	
	private String register_id;			//����ڻ��
	private String register_info;		//���������
	private String register_date;		//�������
	private String modifier_id;			//�����ڻ��
	private String modifier_info;		//����������
	private String modify_date;			//��������
	private String aid;					//���ڰ��������ȣ
	private String stat;				//�����ڵ�
	
	private String one_class;			//��ǰ���ڵ�
	private String two_class;			//��ǰ�ڵ�
	private String three_class;			//�𵨱��ڵ�

	private String fname;				//���ϸ�
	private String ftype;				//����Ÿ��
	private String fsize;				//���ϻ�����
	private String umask;				//���������̸�

	//���ڵ������ �ʿ��� ���� ���� ����
	private String two_class_code;		//��ǰ�ڵ�
	private String revision_code;		//����ڵ�
	private String derive_code;			//�Ļ��ڵ�

	
	//��ǰ����
	public String getMid(){		return this.mid;	}
	public void setMid(String mid){		this.mid = mid;	}

	public String getGoodsCode(){		return this.code;	}
	public void setGoodsCode(String code){		this.code = code;	}

	public String getGoodsName(){		return this.name;	}
	public void setGoodsName(String name){		this.name = name;	}

	public String getGoodsName2(){		return this.name2;	}
	public void setGoodsName2(String name2){		this.name2 = name2;	}

	public String getShortName(){		return this.short_name;	}
	public void setShortName(String short_name){		this.short_name = short_name;	}

	public String getColorCode(){		return this.color_code;	}
	public void setColorCode(String color_code){		this.color_code = color_code;	}

	public String getColorName(){		return this.color_name;	}
	public void setColorName(String color_name){		this.color_name = color_name;	}

	public String getOtherInfo() {		return other_info;	}
	public void setOtherInfo(String string) {		other_info = string;	}

	public String getFgCode() {		return fg_code;	}
	public void setFgCode(String string) {		fg_code = string;	}

	//Ʈ������
	public String getGoodsLevel(){		return this.glevel;	}
	public void setGoodsLevel(String glevel){		this.glevel = glevel;	}

	public String getAncestor(){		return this.ancestor;	}
	public void setAncestor(String ancestor){		this.ancestor = ancestor;	}

	public String getGcode(){		return this.gcode;	}
	public void setGcode(String gcode){		this.gcode = gcode;	}

	//�������
	public String getRegisterId(){		return this.register_id;	}
	public void setRegisterId(String register_id){		this.register_id = register_id;	}

	public String getRegisterInfo() {		return this.register_info;	}
	public void setRegisterInfo(String register_info){		this.register_info = register_info;	}

	public String getRegisterDate(){		return this.register_date;	}
	public void setRegisterDate(String register_date){		this.register_date = register_date;	}

	public String getModifierId(){		return this.modifier_id;	}
	public void setModifierId(String modifier_id){		this.modifier_id = modifier_id;	}

	public String getModifierInfo() {		return this.modifier_info;	}
	public void setModifierInfo(String modifier_info){		this.modifier_info = modifier_info;	}

	public String getModifyDate(){		return this.modify_date;	}
	public void setModifyDate(String modify_date){		this.modify_date = modify_date;	}

	public String getAid(){		return this.aid;	}
	public void setAid(String aid){		this.aid = aid;	}

	public String getStat() {		return stat;	}
	public void setStat(String string) {		stat = string;	}

	//�з�����
	public String getOneClass(){		return this.one_class;	}
	public void setOneClass(String one_class){		this.one_class = one_class;	}

	public String getTwoClass(){		return this.two_class;	}
	public void setTwoClass(String two_class){		this.two_class = two_class;	}

	public String getThreeClass(){		return this.three_class;	}
	public void setThreeClass(String three_class){		this.three_class = three_class;	}
	
	//÷����������
	public String getFileName() {		return fname;	}
	public void setFileName(String string) {		fname = string;	}

	public String getFileSize() {		return fsize;	}
	public void setFileSize(String string) {		fsize = string;	}

	public String getFileType() {		return ftype;	}
	public void setFileType(String string) {		ftype = string;	}

	public String getUmask() {		return umask;	}
	public void setUmask(String string) {		umask = string;	}

	//���ڵ������ �ʿ��� ���� ���� ����
	public String getTwoClassCode(){		return this.two_class_code;	}
	public void setTwoClassCode(String two_class_code){		this.two_class_code = two_class_code;	}

	public String getRevisionCode(){		return this.revision_code;	}
	public void setRevisionCode(String revision_code){		this.revision_code = revision_code;	}

	public String getDeriveCode(){		return this.derive_code;	}
	public void setDeriveCode(String derive_code){		this.derive_code = derive_code;	}
}