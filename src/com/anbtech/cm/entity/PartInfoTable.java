package com.anbtech.cm.entity;

/**
 *
 * ǰ�� ���� ��� ������ get/set
 */
public class PartInfoTable {

	//ǰ�� �з��Ӽ�
	private String mid;					//������ȣ
	private String code_big;			//��з��ڵ�
	private String code_mid;			//�ߺз��ڵ�
	private String code_small;			//�Һз��ڵ�
	
	//ǰ�� ����Ӽ�
	private String item_no;				//ǰ���ڵ�
	private String item_desc;			//ǰ�񼳸�
	private String mfg_no;				//ǰ����޾�ü�ڵ� 
	private String model_code;			//���ڵ�
	private String item_name;			//ǰ���
	private String item_type;			//ǰ�����(ǰ���ڵ�)
	private String stock_unit;			//������
	private String config_name;			//�����

	//�ĸ� ��ϼӼ�
	private String register_id;
	private String register_info;
	private String register_date;
	private String stat;				// �����ڵ�
	
	//÷������
	private String file_name;			// ���ϸ�
	private String file_type;			// ����Ÿ��
	private String file_size;			// ���ϻ�����
	private String file_umask;			// ���������̸�


	//ǰ�� �з��Ӽ�
	public String getMid() {	return mid;	}
	public void setMid(String string) {	mid = string;	}

	public String getCodeBig() {	return code_big;	}
	public void setCodeBig(String string) {	code_big = string;	}

	public String getCodeMid() {	return code_mid;	}
	public void setCodeMid(String string) {	code_mid = string;	}

	public String getCodeSmall() {	return code_small;	}
	public void setCodeSmall(String string) {	code_small = string;	}

	//ǰ�� ����Ӽ�
	public String getItemNo() {	return item_no;	}
	public void setItemNo(String string) {	item_no = string;	}

	public String getItemDesc() {	return item_desc;	}
	public void setItemDesc(String string) {	item_desc = string;	}

	public String getMfgNo() {	return mfg_no;	}
	public void setMfgNo(String string) {	mfg_no = string;	}

	public String getModelCode() {	return model_code;	}
	public void setModelCode(String string) {	model_code = string;	}

	public String getItemName() {	return item_name;	}
	public void setItemName(String string) {	item_name = string;	}

	public String getItemType() {	return item_type;	}
	public void setItemType(String string) {	item_type = string;	}

	public String getStockUnit() {	return stock_unit;	}
	public void setStockUnit(String string) {	stock_unit = string;	}

	public String getConfigName() {	return config_name;	}
	public void setConfigName(String string) {	config_name = string;	}

	//ǰ�� ��ϼӼ�
	public String getRegisterId() {	return register_id;	}
	public void setRegisterId(String string) {	register_id = string;	}

	public String getRegisterInfo() {	return register_info;	}
	public void setRegisterInfo(String string) {	register_info = string;	}

	public String getRegisterDate() {	return register_date;	}
	public void setRegisterDate(String string) {	register_date = string;	}

	public String getStat() {	return stat;	}
	public void setStat(String string) {	stat = string;	}

	//÷������
	public String getFileName() {	return file_name;	}
	public void setFileName(String string) {	file_name = string;	}

	public String getFileType() {	return file_type;	}
	public void setFileType(String string) {	file_type = string;	}

	public String getFileSize() {	return file_size;	}
	public void setFileSize(String string) {	file_size = string;	}

	public String getFileUmask() {	return file_umask;	}
	public void setFileUmask(String string) {	file_umask = string;	}
}