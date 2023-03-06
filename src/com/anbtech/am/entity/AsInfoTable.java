/************************************************************
 * AsInfoTable 테이블 getter/setter
 ************************************************************/

package com.anbtech.am.entity;

public class AsInfoTable{

		private  int     as_no;			// 관리번호
		private  String  as_mid;		// 자산번호
		private  String  ct_id;			//카테고리ID
		private  String  c_no;
		private  String  as_item_no;	//자산수량no
		private  String  w_id;
		private  String  w_name;
		private  String  w_rank;
		private  String  b_id;			//자산품구매자ID
		private  String  b_name;		//자산품구매자이름
		private  String  b_rank;		//자산품부서명
		private  String  model_name;	//모델명
		private  String  as_name;		//자산품목이름
		private  String  as_serial;		//자산품시리얼
		private  String  buy_date;		//구매일자
		private  String  as_price;		//품목 가격
		private  String  dc_count;		//감가제한년도
		private  String  dc_bound;		//감가제한횟수
		private  String  as_each_dc;	//품목 감가 비율
		private  String  as_value;		//자산가격
		private  String  crr_id;		//현재 자산 책임자 ID
		private  String  crr_name;		//현재 자산 책임자 이름
		private  String  crr_rank;		//현재 자산 책임자 부서
		private  String  u_id;			//현재 사용자 ID
		private  String  u_name;		//현재 사용자 이름
		private  String  u_rank;		//현재 사용자 부서
		private  String  buy_where;		//구매지(회사)
		private  String  as_maker;		//자산품만든곳(회사)
		private  String  as_setting;	//규격(사양)
		private  String  bw_tel;		//구매회사연락처
		private  String  bw_address;	//구매회사주소
		private  String  bw_employee;	//구매회사담당자
		private  String  bw_mgr_tel;	//구매회사담당자연락처
		private  String  etc;			//기타
		private  String  as_status;		//자산상태(폐기,사용중,감각비해제)
		private  String  as_statusinfo;
		private  String  as_except_day;	//자산 폐기 일자
		private  String  as_except_reason;// 자산 폐기 이유
		private  String  file_se;		//첨부파일 개수
		private  String  file_name;		//첨부파일 이름
		private  String  file_type;		//첨부파일 타입
		private  String  file_size;		//첨부파일 사이즈
		private  String  file_umask;
		private  String  file_path;		//처부파일 경로
		private  String  handle;		//반입/이관/대여 가능여부 "y" or "n"
		private  String  status_name;	//상태 이름
		private  String  now_status;
		private  String  apply_dcdate;
		private  String  del_form;
		private  String  del_reason;
	
		
		// setter
		public void setAsNo(int as_no){
			this.as_no = as_no;	
		}
		public void setAsMid(String as_mid){
			this.as_mid = as_mid;
		}
		public void setCtId(String ct_id){
			this.ct_id = ct_id;
		}
		public void setCno(String c_no){
			this.c_no  = c_no;
		}

		public void setWid(String w_id){
			this.w_id = w_id;		
		}
		public void setWname(String w_name){
			this.w_name = w_name;		
		}
		public void setWrank(String w_rank){
			this.w_rank = w_rank;		
		}

		public void setAsItemNo(String as_item_no){
			this.as_item_no = as_item_no;
		}
		public void setBid(String b_id){
			this.b_id = b_id;		
		}
		public void setBname(String b_name){
			this.b_name = b_name;		
		}
		public void setBrank(String b_rank){
			this.b_rank = b_rank;		
		}
		public void setModelName(String model_name){
			this.model_name = model_name;
		}
		public void setAsName(String as_name){
			this.as_name = as_name;		
		}
		public void setAsSerial(String as_serial){
			this.as_serial = as_serial;		
		}
		public void setBuyDate(String buy_date){
			this.buy_date = buy_date;
		}
		public void setAsPrice(String as_price){
			this.as_price = as_price;
		}
		public void setDcCount(String dc_count){
			this.dc_count = dc_count;		
		}
		public void setDcBound(String dc_bound){
			this.dc_bound = dc_bound;		
		}
		public void setAsEachDc(String as_each_dc){
			this.as_each_dc = as_each_dc;		
		}
		public void setAsValue(String as_value){
			this.as_value = as_value;
		}
		public void setCrrId(String crr_id){
			this.crr_id = crr_id;
		}
		public void setCrrName(String crr_name){
			this.crr_name = crr_name;		
		}
		public void setCrrRank(String crr_rank){
			this.crr_rank = crr_rank;		
		}

		public void setUid(String u_id){
			this.u_id = u_id;
		}
		public void setUname(String u_name){
			this.u_name = u_name;		
		}
		public void setUrank(String u_rank){
			this.u_rank = u_rank;		
		}

		public void setBuyWhere(String buy_where){
			this.buy_where = buy_where;		
		}
		public void setAsMaker(String as_maker){
			this.as_maker = as_maker;
		}
		public void setAsSetting(String as_setting){
			this.as_setting = as_setting;
		}
		public void setBwTel(String bw_tel){
			this.bw_tel = bw_tel;		
		}
		public void setBwAddress(String bw_address){
			this.bw_address = bw_address;		
		}
		public void setBwEmployee(String bw_employee){
			this.bw_employee = bw_employee;		
		}
		public void setBwMgrTel(String bw_mgr_tel){
			this.bw_mgr_tel = bw_mgr_tel;
		}
		public void setEtc(String etc){
			this.etc = etc;		
		}
		public void setAsStatus(String as_status){
			this.as_status = as_status;
		}
		public void setAsStatusInfo(String as_statusinfo){
			this.as_statusinfo = as_statusinfo;
		}
		public void setAsExceptDay(String as_except_day){
			this.as_except_day = as_except_day;
		}
		public void setAsExceptReason(String as_except_reason){
			this.as_except_reason = as_except_reason;
		}
		public void setFileSe(String file_se){
			this.file_se = file_se;
		}
		public void setFileName(String file_name){
			this.file_name = file_name;		
		}
		public void setFileType(String file_type){
			this.file_type = file_type;
		}
		public void setFileSize(String file_size){
			this.file_size = file_size;
		}
		public void setFileUmask(String file_umask){
			this.file_umask = file_umask;
		}
		public void setFilePath(String file_path){
			this.file_path = file_path;		
		}
		public void setHandle(String handle){
			this.handle = handle;
		}
		public void setAsStatusName(String status_name){
			this.status_name = status_name;
		}
		public void setNowStatus(String now_status){
			this.now_status = now_status;
		}
		public void setApplyDcDate(String apply_dcdate){
			this.apply_dcdate = apply_dcdate;
		}
		public void setDelForm(String del_form){
			this.del_form = del_form;
		}
		public void setDelReason(String del_reason){
			this.del_reason = del_reason;
		}


		// getter
		public int getAsNo(){
			return as_no;
		}
		public  String  getAsMid(){
			return as_mid;
		}
		public  String  getCtId(){
			return ct_id;
		}
		public String getCno(){
			return c_no;
		}
		public  String  getWid(){
			return w_id;
		}
		public  String  getWname(){
			return w_name;
		}
		public  String  getWrank(){
			return w_rank;
		}
		public  String  getAsItemNo(){
			return as_item_no;
		}
		public  String  getBid(){
			return b_id;
		}
		public  String  getBname(){
			return b_name;
		}
		public  String  getBrank(){
			return b_rank;
		}
		public  String  getModelName(){
			return model_name;
		}
		public  String  getAsName(){
			return as_name;
		}
		public  String  getAsSerial(){
			return as_serial;
		}
		public  String  getBuyDate(){
			return buy_date;
		}
		public  String  getAsPrice(){
			return as_price;
		}
		public  String  getDcCount(){
			return dc_count;
		}
		public  String  getDcBound(){
			return dc_bound;
		}
		public  String  getAsEachDc(){
			return as_each_dc;
		}
		public  String  getAsValue(){
			return as_value;
		}
		public  String  getCrrId(){
			return crr_id;
		}
		public  String  getCrrName(){
			return crr_name;
		}
		public  String  getCrrRank(){
			return crr_rank;
		}
		public  String  getBuyWhere(){
			return buy_where;
		}
		public  String  getAsMaker(){
			return as_maker;
		}
		public  String  getAsSetting(){
			return as_setting;
		}
		public  String  getBwTel(){
			return bw_tel;
		}
		public  String  getBwAddress(){
			return bw_address;
		}
		public  String  getBwEmployee(){
			return bw_employee;
		}
		public  String  getBwMgrTel(){
			return bw_mgr_tel;
		}
		public  String  getEtc(){
			return etc;
		}
		public  String  getAsStatus(){
			return as_status;
		}
		public String getAsStatusInfo(){
			return as_statusinfo;
		}
		public  String  getAsExceptDay(){
			return as_except_day;
		}
		public  String  getAsExceptReason(){
			return as_except_reason;
		}
		public  String  getFileSe(){
			return file_se;
		}
		public  String  getFileName(){
			return file_name;
		}
		public  String  getFileType(){
			return file_type;
		}
		public  String  getFileSize(){
			return file_size;
		}
		public  String  getFileUmask(){
			return file_umask;
		}
		public  String  getFilePath(){
			return file_path;
		}
		public  String  getHandle(){
			return handle;
		}
		public  String getAsStatusName(){
			return status_name;
		}
		public  String getNowStatus(){
			return now_status;
		}
		public  String getApplyDcDate(){
			return apply_dcdate;
		}
		public  String getDelForm(){
			return del_form;
		}
		public  String getDelReason(){
			return del_reason;
		}
		public  String getUid(){
			return u_id;
		}
		public  String getUname(){
			return u_name;
		}
		public  String getUrank(){
			return u_rank;
		}
		
}
