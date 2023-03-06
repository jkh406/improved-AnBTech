package com.anbtech.cm.entity;

/**
 *
 * 품목에 대한 등록 정보를 get/set
 */
public class PartInfoTable {

	//품목 분류속성
	private String mid;					//관리번호
	private String code_big;			//대분류코드
	private String code_mid;			//중분류코드
	private String code_small;			//소분류코드
	
	//품목 공통속성
	private String item_no;				//품목코드
	private String item_desc;			//품목설명
	private String mfg_no;				//품목공급업체코드 
	private String model_code;			//모델코드
	private String item_name;			//품목명
	private String item_type;			//품목계정(품종코드)
	private String stock_unit;			//재고단위
	private String config_name;			//형상명

	//픔목 등록속성
	private String register_id;
	private String register_info;
	private String register_date;
	private String stat;				// 상태코드
	
	//첨부파일
	private String file_name;			// 파일명
	private String file_type;			// 파일타입
	private String file_size;			// 파일사이즈
	private String file_umask;			// 파일저장이름


	//품목 분류속성
	public String getMid() {	return mid;	}
	public void setMid(String string) {	mid = string;	}

	public String getCodeBig() {	return code_big;	}
	public void setCodeBig(String string) {	code_big = string;	}

	public String getCodeMid() {	return code_mid;	}
	public void setCodeMid(String string) {	code_mid = string;	}

	public String getCodeSmall() {	return code_small;	}
	public void setCodeSmall(String string) {	code_small = string;	}

	//품목 공통속성
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

	//품목 등록속성
	public String getRegisterId() {	return register_id;	}
	public void setRegisterId(String string) {	register_id = string;	}

	public String getRegisterInfo() {	return register_info;	}
	public void setRegisterInfo(String string) {	register_info = string;	}

	public String getRegisterDate() {	return register_date;	}
	public void setRegisterDate(String string) {	register_date = string;	}

	public String getStat() {	return stat;	}
	public void setStat(String string) {	stat = string;	}

	//첨부파일
	public String getFileName() {	return file_name;	}
	public void setFileName(String string) {	file_name = string;	}

	public String getFileType() {	return file_type;	}
	public void setFileType(String string) {	file_type = string;	}

	public String getFileSize() {	return file_size;	}
	public void setFileSize(String string) {	file_size = string;	}

	public String getFileUmask() {	return file_umask;	}
	public void setFileUmask(String string) {	file_umask = string;	}
}