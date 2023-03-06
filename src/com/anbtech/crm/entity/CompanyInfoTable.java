/************************************************************
 * company_table 테이블 getter/setter
 ************************************************************/

package com.anbtech.crm.entity;

public class CompanyInfoTable{
		private  String  mid;
		private  String  company_no;
		private  String  passwd;
		private  String  name_kor;
		private  String  name_eng;
		private  String  chief_name;
		private  String  chief_personal_no;
		private  String  company_address;
		private  String  company_post_no;
		private  String  main_tel_no;
		private  String  main_fax_no;
		private  String  homepage_url;
		private  String  business_type;
		private  String  business_item;
		private  String  trade_start_time;
		private  String  trade_end_time;
		private  String  company_type;
		private  String  trade_type;
		private  String  credit_level;
		private  String  estimate_req_level;
		private  String  worker_number;
		private  String  main_bank_name;
		private  String  main_newspaper_name;
		private  String  main_product_name;
		private  String  corporation_no;
		private  String  founding_day;
		private  String  other_info;
		private  String  writer;
		private  String  writer_info;
		private  String  written_day;
		private  String  modifier;
		private  String  modifier_info;
		private  String  modified_day;
		private  String  modify_history;

		//첨부파일
		private String file_name;			// 파일명
		private String file_type;			// 파일타입
		private String file_size;			// 파일사이즈
		private String file_umask;			// 파일저장이름

	
		public void setMid(String mid){	this.mid = mid;	}
		public  String  getMid(){	return mid;	}

		public void setCompanyNo(String company_no){	this.company_no = company_no;	}
		public  String  getCompanyNo(){	return company_no;	}

		public void setPasswd(String passwd){	this.passwd = passwd;	}
		public  String  getPasswd(){	return passwd;	}

		public void setNameKor(String name_kor){	this.name_kor = name_kor;	}
		public  String  getNameKor(){	return name_kor;	}

		public void setNameEng(String name_eng){	this.name_eng = name_eng;	}
		public  String  getNameEng(){	return name_eng;	}

		public void setChiefName(String chief_name){	this.chief_name = chief_name;	}
		public  String  getChiefName(){	return chief_name;	}

		public void setChiefPersonalNo(String chief_personal_no){	this.chief_personal_no = chief_personal_no;	}
		public  String  getChiefPersonalNo(){	return chief_personal_no;	}

		public void setCompanyAddress(String company_address){	this.company_address = company_address;	}
		public  String  getCompanyAddress(){	return company_address;	}

		public void setCompanyPostNo(String company_post_no){	this.company_post_no = company_post_no;	}
		public  String  getCompanyPostNo(){	return company_post_no;	}

		public void setMainTelNo(String main_tel_no){	this.main_tel_no = main_tel_no;	}
		public  String  getMainTelNo(){	return main_tel_no;	}

		public void setMainFaxNo(String main_fax_no){	this.main_fax_no = main_fax_no;	}
		public  String  getMainFaxNo(){	return main_fax_no;	}

		public void setHomepageUrl(String homepage_url){	this.homepage_url = homepage_url;	}
		public  String  getHomepageUrl(){	return homepage_url;	}

		public void setBusinessType(String business_type){	this.business_type = business_type;	}
		public  String  getBusinessType(){	return business_type;	}

		public void setBusinessItem(String business_item){	this.business_item = business_item;	}
		public  String  getBusinessItem(){	return business_item;	}

		public void setTradeStartTime(String trade_start_time){	this.trade_start_time = trade_start_time;	}
		public  String  getTradeStartTime(){	return trade_start_time;	}

		public void setTradeEndTime(String trade_end_time){	this.trade_end_time = trade_end_time;	}
		public  String  getTradeEndTime(){	return trade_end_time;	}

		public void setCompanyType(String company_type){	this.company_type = company_type;	}
		public  String  getCompanyType(){	return company_type;	}

		public void setTradeType(String trade_type){	this.trade_type = trade_type;	}
		public  String  getTradeType(){	return trade_type;	}

		public void setCreditLevel(String credit_level){	this.credit_level = credit_level;	}
		public  String  getCreditLevel(){	return credit_level;	}

		public void setEstimateReqLevel(String estimate_req_level){	this.estimate_req_level = estimate_req_level;	}
		public  String  getEstimateReqLevel(){	return estimate_req_level;	}

		public void setWorkerNumber(String worker_number){	this.worker_number = worker_number;	}
		public  String  getWorkerNumber(){	return worker_number;	}

		public void setMainBankName(String main_bank_name){	this.main_bank_name = main_bank_name;	}
		public  String  getMainBankName(){	return main_bank_name;	}

		public void setMainNewspaperName(String main_newspaper_name){	this.main_newspaper_name = main_newspaper_name;	}
		public  String  getMainNewspaperName(){	return main_newspaper_name;	}

		public void setMainProductName(String main_product_name){	this.main_product_name = main_product_name;	}
		public  String  getMainProductName(){	return main_product_name;	}

		public void setCorporationNo(String corporation_no){	this.corporation_no = corporation_no;	}
		public  String  getCorporationNo(){	return corporation_no;	}

		public void setFoundingDay(String founding_day){	this.founding_day = founding_day;	}
		public  String  getFoundingDay(){	return founding_day;	}

		public void setOtherInfo(String other_info){	this.other_info = other_info;	}
		public  String  getOtherInfo(){	return other_info;	}

		public void setWriter(String writer){	this.writer = writer;	}
		public  String  getWriter(){	return writer;	}

		public void setWriterInfo(String writer_info){	this.writer_info = writer_info;	}
		public  String  getWriterInfo(){	return writer_info;	}

		public void setWrittenDay(String written_day){	this.written_day = written_day;	}
		public  String  getWrittenDay(){	return written_day;	}

		public void setModifier(String modifier){	this.modifier = modifier;	}
		public  String  getModifier(){	return modifier;	}

		public void setModifierInfo(String modifier_info){	this.modifier_info = modifier_info;	}
		public  String  getModifierInfo(){	return modifier_info;	}

		public void setModifiedDay(String modified_day){	this.modified_day = modified_day;	}
		public  String  getModifiedDay(){	return modified_day;	}

		public void setModifyHistory(String modify_history){	this.modify_history = modify_history;	}
		public  String  getModifyHistory(){	return modify_history;	}

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
