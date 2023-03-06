/************************************************************
 * AsCategoryTable 테이블 getter/setter
 ************************************************************/

package com.anbtech.am.entity;

public class AsHistoryTable 
{
	private int h_no;				// 관리번호			[h_no]		 
	private String as_no;			// 자산관리번호		[as_no]		 
	private String w_id;			// 작성자id			[w_id]		
	private String w_name;			// 작성자이름		[w_name]	 
	private String w_rank;			// 작성자부서		[w_rank]	 
	private String u_id;			// 자산사용자ID		[u_id]	
	private String u_name;			// 자산사용자이름	[u_name]	 
	private String u_rank;			// 자산사용자부서	[u_rank]	 
	private String takeout_reason;	// 반출이유			[takeout_reason] 
	private String out_destination;	// 반출목적지		[out_destination]
	private String write_date;		// 작성일자			[write_date]
	private String u_date;			// 반출요청시간(년월일)
	private String u_year;			// 반출요청시간(년)	[u_year]	 
	private String u_month;			// 반출요청시간(월)	[u_month]	 
	private String u_day;			// 반출요청시간(일)	[u_day]		 
	private String tu_date;         // 반입예정시간(년월일)
	private String tu_year;			// 반입예정시간(년)	[tu_year]	 
	private String tu_month;		// 반입예정시간(월)	[tu_month]	 
	private String tu_day;			// 반입예정시간(일)	[tu_day]	 
	private String in_date;			// 반입일(년/월/일)	[in_date]	 
	private String wi_date;         // 반입처리작성일(년월일) 
	private String c_year;			// 실반납시간(년)	[c_year]	 
	private String c_month;			// 실반납시간(월)	[c_month]	 
	private String c_day;			// 실반납시간(일)	[c_day]	  	 
	private String as_status;		// 반납시자산상태(code)
	private String o_status;		// 이관/반출 형태(code)
	private String as_status_name;	// 반납시자산상태(한글)
	private String o_status_name;	// 이관/반출 형태(한글)
	private String as_statusinfo;	// 반납시자산세부정보	[as_statusinfo]  
	private String flag;			// 결재1			[flag]		 
	private String flag2;			// 결재2			[flag2]	
	private String link;
	private String pid;
	private String type;
	private String info_status;
	private String info_asmid;
	private String pid2;
	
	// setter
	public void setHno(int h_no){
		this.h_no = h_no;
	}
	public void setAsNo(String as_no){
		this.as_no = as_no;
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
	public void setUid(String u_id){
		this.u_id = u_id;
	}
	public void setUname(String u_name){
		this.u_name = u_name;
	}	
	public void setUrank(String u_rank){
		this.u_rank = u_rank;
	}
	public void setTakeOutReason(String takeout_reason){
		this.takeout_reason = takeout_reason;
	}
	public void setOutDestination(String out_destination){
		this.out_destination = out_destination;
	}	
	public void setWriteDate(String write_date){
		this.write_date = write_date;
	}
	public void setUdate(String u_date){
		this.u_date = u_date;
	}
	public void setUyear(String u_year){
		this.u_year = u_year;
	}	
	public void setUmonth(String u_month){
		this.u_month = u_month;
	}
	public void setUday(String u_day){
		this.u_day = u_day;
	}	
	public void setTuDate(String tu_date){
		this.tu_date = tu_date;
	}
	public void setTuYear(String tu_year){
		this.tu_year = tu_year;
	}
	public void setTuMonth(String tu_month){
		this.tu_month = tu_month;
	}
	public void setTuDay(String tu_day){
		this.tu_day = tu_day;
	}
	public void setInDate(String in_date){
		this.in_date = in_date;
	}
	public void setWiDate(String wi_date){
		this.wi_date = wi_date;
	}
	public void setCyear(String c_year){
		this.c_year = c_year;
	}
	public void setCmonth(String c_month){
		this.c_month = c_month;
	}
	public void setCday(String c_day){
		this.c_day = c_day;
	}
	public void setOstatus(String o_status){
		this.o_status = o_status;
	}
	public void setAsStatus(String as_status){
		this.as_status = as_status;
	}
	public void setOstatusName(String o_status_name){
		this.o_status_name = o_status_name;
	}
	public void setAsStatusName(String as_status_name){
		this.as_status_name = as_status_name;
	}
	public void setAsStatusInfo(String as_statusinfo){
		this.as_statusinfo = as_statusinfo;
	}	
	public void setFlag(String flag){
		this.flag = flag;
	}
	public void setFlag2(String flag2){
		this.flag2 = flag2;
	}
	public void setLink(String link){
		this.link = link;
	}
	public void setPid(String pid){
		this.pid = pid;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setInfoStatus(String info_status){
		this.info_status = info_status;
	}
	public void setAsMid(String info_asmid){
		this.info_asmid = info_asmid;
	}
	public void setPid2(String pid2){
		this.pid2 = pid2;
	}

	// getter
	public int getHno(){
		return h_no;
	}
	public String getAsNo(){
		return as_no;
	}	
	public String getWid(){
		return w_id;
	}		
	public String getWname(){
		return w_name;
	}
	public String getWrank(){
		return w_rank;
	}
	public String getUid(){
		return u_id;
	}
	public String getUname(){
		return u_name;
	}
	public String getUrank(){
		return u_rank;
	}
	public String getTakeOutReason(){
		return takeout_reason;
	}
	public String getOutDestination(){
		return out_destination;
	}
	public String getWriteDate(){
		return write_date;
	}
	public String getUdate(){
		return u_date;
	}
	public String getUyear(){
		return u_year;
	}
	public String getUmonth(){
		return u_month;
	}
	public String getUday(){
		return u_day;
	}
	public String getTuDate(){
		return tu_date;
	}
	public String getTuYear(){
		return tu_year;
	}
	public String getTuMonth(){
		return tu_month;
	}	
	public String getTuDay(){
		return tu_day;
	}
	public String getInDate(){
		return in_date;
	}
	public String getWiDate(){
		return wi_date;
	}
	public String getCyear(){
		return c_year;
	}
	public String getCmonth(){
		return c_month;
	}
	public String getCday(){
		return c_day;
	}
	public String getOstatus(){
		return o_status;
	}
	public String getAsStatus(){
		return as_status;
	}
	public String getOstatusName(){
		return o_status_name;
	}
	public String getAsStatusName(){
		return as_status_name;
	}
	public String getAsStatusInfo(){
		return as_statusinfo;
	}
	public String getFlag(){
		return flag;
	}
	public String getFlag2(){
		return flag2;
	}
	public String getLink(){
		return link;
	}
	public String getPid(){
		return pid;
	}
	public String getType(){
		return type;
	}
	public String getInfoStatus(){
		return info_status;
	}
	public String getAsMid(){
		return info_asmid;
	}
	public String getPid2(){
		return pid2;
	}
}
