package com.anbtech.em.entity;

public class ItemInfoTable {
	private String mid;				//관리번호
	private String category_code;	//분류코드
	private String where_category;	//분류 문자열
//	private String item_no;			//품목번호
	private String item_name;		//품목명
	private String item_class;		//품목구분(자체개발 or 외주조달 or 패키지품목)
	private String model_name;		//모델명
	private String model_code;		//모델코드
//	private String maker_code;		//제조회사코드
	private String maker_name;		//제조회사명
//	private String maker_part_no;	//제조회사 부품번호
//	private String serial_no;		//일련번호
	private String supplyer_name;	//공급사 이름
	private String supplyer_code;	//공급사 업체코드
	private String last_update_date;//최종 정보 갱신일

//	private String quality;			//재질
//	private String type;			//타입
//	private String color;			//색상
	private String standards;		//규격
	private String unit;			//단위
	private String quantity;		//수량

	private String fname;			//첨부파일명
	private String ftype;			//첨부파일타입
	private String fsize;			//첨부파일크기
	private String umask;			//첨부파일 저장명
	private String other_info;		//기타정보

	private String stat;			//상태
	private String link_url;		//
	private String writer;			//작성자
	private String written_day;		//작성일
	private String buying_cost;		//매입 또는 개발 원가
	private String gains_percent;	//이익율(%)
	private String gains_value;		//이익금액
	private String supply_cost;		//공급원가 = 개발원가 + 이익금액
	private String discount_percent;//할인율(%)
	private String discount_value;	//할인금액
	private String tax_percent;		//세율(%)
	private String tax_value;		//세액
	private String estimate_value;	//견적금액 = 공급원가 + 세액 - 할인금액
//	private String level;			//품목레벨
//	private String ancestor;		//모품목의 관리번호
	private String estimate_no;		//견적번호
	private String version;			//견적버젼

	public String getMid() { return mid; }
	public void setMid(String string) { mid = string; }

	public String getCategoryCode() { return category_code; }
	public void setCategoryCode(String string) { category_code = string; }

	public String getWhereCategory() { return where_category; }
	public void setWhereCategory(String string) { where_category = string; }

//	public String getItemNo() { return item_no; }
//	public void setItemNo(String string) { item_no = string; }

	public String getItemName() { return item_name; }
	public void setItemName(String string) { item_name = string; }

	public String getItemClass() { return item_class; }
	public void setItemClass(String string) { item_class = string; }

	public String getModelName() { return model_name; }
	public void setModelName(String string) { model_name = string; }

	public String getModelCode() { return model_code; }
	public void setModelCode(String string) { model_code = string; }

//	public String getMakerCode() { return maker_code; }
//	public void setMakerCode(String string) { maker_code = string; }

	public String getMakerName() { return maker_name; }
	public void setMakerName(String string) { maker_name = string; }

//	public String getMakerPartNo() { return maker_part_no; }
//	public void setMakerPartNo(String string) { maker_part_no = string; }

//	public String getSerialNo() { return serial_no; }
//	public void setSerialNo(String string) { serial_no = string; }

	public String getSupplyerName() { return supplyer_name; }
	public void setSupplyerName(String string) { supplyer_name = string; }

	public String getSupplyerCode() { return supplyer_code; }
	public void setSupplyerCode(String string) { supplyer_code = string; }

	public String getLastUpdateDate() { return last_update_date; }
	public void setLastUpdateDate(String string) { last_update_date = string; }

//	public String getQuality() { return quality; }
//	public void setQuality(String string) { quality = string; }

//	public String getType() { return type; }
//	public void setType(String string) { type = string; }

//	public String getColor() { return color; }
//	public void setColor(String string) { color = string; }

	public String getStandards() { return standards; }
	public void setStandards(String string) { standards = string; }

	public String getUnit() { return unit; }
	public void setUnit(String string) { unit = string; }

	public String getFileName() { return fname; }
	public void setFileName(String string) { fname = string; }

	public String getFileType() { return ftype; }
	public void setFileType(String string) { ftype = string; }

	public String getFileSize() { return fsize; }
	public void setFileSize(String string) { fsize = string; }

	public String getUmask() { return umask; }
	public void setUmask(String string) { umask = string; }

	public String getOtherInfo() { return other_info; }
	public void setOtherInfo(String string) { other_info = string; }

	public String getQuantity() { return quantity; }
	public void setQuantity(String string) { quantity = string; }

	public String getStat() { return stat; }
	public void setStat(String string) { stat = string; }

	public String getLinkUrl() { return link_url; }
	public void setLinkUrl(String string) { link_url = string; }

	public String getWriter() { return writer; }
	public void setWriter(String string) { writer = string; }

	public String getWrittenDay() { return written_day; }
	public void setWrittenDay(String string) { written_day = string; }

	public String getBuyingCost() { return buying_cost; }
	public void setBuyingCost(String string) { buying_cost = string; }

	public String getGainsPercent() { return gains_percent; }
	public void setGainsPercent(String string) { gains_percent = string; }

	public String getGainsValue() { return gains_value; }
	public void setGainsValue(String string) { gains_value = string; }

	public String getSupplyCost() { return supply_cost; }
	public void setSupplyCost(String string) { supply_cost = string; }

	public String getDiscountPercent() { return discount_percent; }
	public void setDiscountPercent(String string) { discount_percent = string; }

	public String getDiscountValue() { return discount_value; }
	public void setDiscountValue(String string) { discount_value = string; }

	public String getTaxPercent() { return tax_percent; }
	public void setTaxPercent(String string) { tax_percent = string; }

	public String getTaxValue() { return tax_value; }
	public void setTaxValue(String string) { tax_value = string; }

	public String getEstimateValue() { return estimate_value; }
	public void setEstimateValue(String string) { estimate_value = string; }

//	public String getLevel() { return level; }
//	public void setLevel(String string) { level = string; }

//	public String getAncestor() { return ancestor; }
//	public void setAncestor(String string) { ancestor = string; }

	public String getEstimateNo() { return estimate_no; }
	public void setEstimateNo(String string) { estimate_no = string; }

	public String getVersion() { return version; }
	public void setVersion(String string) { version = string; }
}
