/************************************************************
 * AsCategoryTable ���̺� getter/setter
 ************************************************************/

package com.anbtech.am.entity;

public class AsHistoryTable 
{
	private int h_no;				// ������ȣ			[h_no]		 
	private String as_no;			// �ڻ������ȣ		[as_no]		 
	private String w_id;			// �ۼ���id			[w_id]		
	private String w_name;			// �ۼ����̸�		[w_name]	 
	private String w_rank;			// �ۼ��ںμ�		[w_rank]	 
	private String u_id;			// �ڻ�����ID		[u_id]	
	private String u_name;			// �ڻ������̸�	[u_name]	 
	private String u_rank;			// �ڻ����ںμ�	[u_rank]	 
	private String takeout_reason;	// ��������			[takeout_reason] 
	private String out_destination;	// ���������		[out_destination]
	private String write_date;		// �ۼ�����			[write_date]
	private String u_date;			// �����û�ð�(�����)
	private String u_year;			// �����û�ð�(��)	[u_year]	 
	private String u_month;			// �����û�ð�(��)	[u_month]	 
	private String u_day;			// �����û�ð�(��)	[u_day]		 
	private String tu_date;         // ���Կ����ð�(�����)
	private String tu_year;			// ���Կ����ð�(��)	[tu_year]	 
	private String tu_month;		// ���Կ����ð�(��)	[tu_month]	 
	private String tu_day;			// ���Կ����ð�(��)	[tu_day]	 
	private String in_date;			// ������(��/��/��)	[in_date]	 
	private String wi_date;         // ����ó���ۼ���(�����) 
	private String c_year;			// �ǹݳ��ð�(��)	[c_year]	 
	private String c_month;			// �ǹݳ��ð�(��)	[c_month]	 
	private String c_day;			// �ǹݳ��ð�(��)	[c_day]	  	 
	private String as_status;		// �ݳ����ڻ����(code)
	private String o_status;		// �̰�/���� ����(code)
	private String as_status_name;	// �ݳ����ڻ����(�ѱ�)
	private String o_status_name;	// �̰�/���� ����(�ѱ�)
	private String as_statusinfo;	// �ݳ����ڻ꼼������	[as_statusinfo]  
	private String flag;			// ����1			[flag]		 
	private String flag2;			// ����2			[flag2]	
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
