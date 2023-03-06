package com.anbtech.es.geuntae.entity;

public class  GeunTaeInfoTable
{
	private String gt_id;			// 관리번호
	private String ys_kind;			// 근태구분(월차,년차,외출...)
	private String member_id;		// 멤버 Id(사원번호)
	private String member_name;		// 멤버 이름
	private String department;		// 멤버 부서이름
	private String rank;			// 멤버 직급
	private String reason;			// 근태 메모(or 사유-기재사항)
	private String h_sdate;			// 근태 시작일자
	private String h_edate;			// 근태 마침일자
	private String day;				// 근태 일수
	private String time_s;			// 출근 시각
	private String time_e;			// 퇴근 시각
	private String c_end_date;		// 퇴근 일자
	
	private String yholiday;		// 당해 년차일수 합
	private String mholiday;		// 당해 월차일수 합
	private String outing;			// 당해 외출일수 합
	private String outbusiness;		// 당해 출장일수 합
	private String somthing;		// 당해 경조사일수 합
	private String sickleave;		// 당해 병가일수 합
	
	private String m_yholiday;		// 해당 월 년차일수
	private String m_mholiday;		// 해당 월 월차일수
	private String m_outing;		// 해당 월 외출일수
	private String m_outbusiness;	// 해당 월 출장일수
	private String m_somthing;		// 해당 월 경조사일수
	private String m_sickleave;		// 해당 월 병가일수

	private String m_totalholiday;	// 해당 월 근태일수 합

	private String user_rank;	 
	private String user_name;
	private String ac_name;
	private String hd_var;		
	private String user_id;		 
	private String user_code;	
	private String thisyear; 
	private String sum; 
	private String rest; 
	
	private String jan1;  
	private	String feb2;  
	private	String mar3;  
	private String apr4;  
	private String may5; 
	private String jun6; 
	private String jul7; 
	private String aug8; 
	private String sep9; 
	private String oct10; 
	private String nov11; 
	private String dec12; 
	private String kindtokor;

	// 특근 정보 추가
	private String ew_ono;
	private String ew_member_id;
	private String ew_wsdate;
	private String ew_wedate;
	private String ew_status;
	private String ew_duty;
	private String status_name;

	// setter-- ew
	public void setEwOno(String ew_ono){
		this.ew_ono = ew_ono;
	}
	public void setEwMemberId(String ew_member_id){
		this.ew_member_id = ew_member_id;
	}
	public void setEwWsdate(String ew_wsdate){
		this.ew_wsdate = ew_wsdate;
	}
	public void setEwWedate(String ew_wedate){
		this.ew_wedate = ew_wedate;
	}
	public void setEwStatus(String ew_status){
		this.ew_status = ew_status;
	}
	public void setEwDuty(String ew_duty){
		this.ew_duty = ew_duty;
	}
	public void setStatusName(String status_name){
		this.status_name = status_name;
	}

	//getter...ew
	public String getEwOno(){
		return ew_ono;
	}
	public String getEwMemberId(){
		return  ew_member_id;
	}
	public String getEwWsdate(){
		return  ew_wsdate;
	}
	public String getEwWedate(){
		return  ew_wedate;
	}
	public String getEwStatus(){
		return  ew_status;
	}
	public String getEwDuty(){
		return ew_duty;
	}
	public String getStatusName(){
		return status_name;
	}

//////////////////////////////////////////// setXXX method  
	public void setGt_id(String gt_id){
		this.gt_id=gt_id;	
	}

	public void setYs_kind(String ys_kind){
		this.ys_kind=ys_kind;
	}

	public void setMember_id(String member_id){
		this.member_id=member_id;
	}

	public void setMember_name(String member_name){
		this.member_name=member_name;
	}
	
	public void setDepartment(String department){
		this.department=department;
	}

	public void setRank(String rank){
		this.rank=rank;
	}

	public void setReason(String reason){
		this.reason=reason;
	}

	public void setH_sdate(String h_sdate){
		this.h_sdate=h_sdate;
	}

	public void setH_edate(String h_edate){
		this.h_edate=h_edate;
	}

	public void setDay(String day){
		this.day=day;
	}

	public void setTimeS(String time_s){
		this.time_s=time_s;
	}

	public void setTimeE(String time_e){
		this.time_e=time_e;
	}
	public void setCendDate(String c_end_date){
		this.c_end_date = c_end_date;
	}

	public void setYholiday(String yholiday){
		this.yholiday=yholiday;
	}

	public void setMholiday(String mholiday){
		this.mholiday=mholiday;
	}

	public void setOuting(String outing){
		this.outing=outing;
	}

	public void setOutbusiness(String outbusiness){
		this.outbusiness=outbusiness;
	}

	public void setSomthing(String somthing){
		this.somthing=somthing;
	}

	public void setSickleave(String sickleave){
		this.sickleave=sickleave;
	}

	public void setM_yholiday(String m_yholiday){
		this.m_yholiday=m_yholiday;
	}

	public void setM_mholiday(String m_mholiday){
		this.m_mholiday=m_mholiday;
	}

	public void setM_outing(String m_outing){
		this.m_outing=m_outing;
	}

	public void setM_outbusiness(String m_outbusiness){
		this.m_outbusiness=m_outbusiness;
	}

	public void setM_somthing(String m_somthing){
		this.m_somthing=m_somthing;
	}

	public void setM_sickleave(String m_sickleave){
		this.m_sickleave=m_sickleave;
	}


	public void setM_totalholiday(String m_totalholiday){
		this.m_totalholiday=m_totalholiday;
	}

	public void setUser_rank(String user_rank){
		this.user_rank=user_rank;
	}

	public void setUser_name(String user_name){
		this.user_name=user_name;
	}
	
	public void setAc_name(String ac_name){
		this.ac_name=ac_name;
	}
	
	public void setHd_var(String hd_var){
		this.hd_var=hd_var;
	}
	
	public void setUser_id(String user_id){	
		this.user_id=user_id;    
	}
	
	public void setUser_code(String user_code){
		this.user_code=user_code;
	} 
	
	public void setThisyear(String thisyear){ 
		this.thisyear=thisyear;  
	}
	
	public void setSum(String sum){
		this.sum=sum;
	} 
	
	public void setRest(String rest){ 
		this.rest=rest;
	}	
	
	public void setJan1(String jan1){ 
		this.jan1=jan1;
	}
	
	public void setFeb2(String feb2){ 
		this.feb2=feb2;
	}
	
	public void setMar3(String mar3){ 
		this.mar3=mar3;
	}
	
	public void setApr4(String apr4){ 
		this.apr4=apr4;
	}
	
	public void setMay5(String may5){ 
		this.may5=may5;
	} 
	
	public void setJun6 (String jun6){ 
		this.jun6=jun6;
	}
	
	public void setJul7(String jul7){
		this.jul7=jul7;
	}
	
	public void setAug8(String aug8){
		this.aug8=aug8;
	}
	
	public void setSep9(String sep9){ 
		this.sep9=sep9;
	}
	
	public void setOct10(String oct10){ 
		this.oct10=oct10;
	}
	
	public void setNov11(String nov11){ 
		this.nov11=nov11;
	}
	
	public void setDec12(String dec12){
		this.dec12=dec12;
	}
	
	public void setKindtokor(String kindtokor){ 
		this.kindtokor=kindtokor;
	}


//////////////////////////////////////////// getXXX method  
	public String getGt_id(){
		return this.gt_id;
	}

	public String getYs_kind(){
		return this.ys_kind;
	}

	public String getMember_id(){
		return this.member_id;
	}

	public String getMember_name(){
		return this.member_name;
	}
	
	public String getDepartment(){
		return this.department;
	}
	
	public String getRank(){
		return this.rank;
	}

	public String getReason(){
		return this.reason;
	}

	public String getH_sdate(){
		return this.h_sdate;
	}

	public String getH_edate(){
		return this.h_edate;
	}

	public String getDay(){
		return this.day;
	}

	public String getTimeS(){
		return this.time_s;
	}

	public String getTimeE(){
		return this.time_e;
	}

	public String getCendDate(){
		return this.c_end_date;
	}

	public String getYholiday(){
		return this.yholiday;
	}

	public String getMholiday(){
		return this.mholiday;
	}

	public String getOuting(){
		return this.outing;
	}

	public String getOutbusiness(){
		return this.outbusiness;
	}

	public String getSomthing(){
		return this.somthing;
	}

	public String getSickleave(){
		return this.sickleave;
	}

	
	public String getM_yholiday(){
		return this.m_yholiday;
	}

	public String getM_mholiday(){
		return this.m_mholiday;
	}

	public String getM_outing(){
		return this.m_outing;
	}

	public String getM_outbusiness(){
		return this.m_outbusiness;
	}

	public String getM_somthing(){
		return this.m_somthing;
	}

	public String getM_sickleave(){
		return this.m_sickleave;
	}

	public String getM_totalholiday(){
		return this.m_totalholiday;
	}

	public String getUser_rank(){
		return this.user_rank;
	}

	public String getUser_name(){
		return this.user_name;
	}

	public String getAc_name(){
		return this.ac_name;  
	}

	public String getHd_var(){
		return this.hd_var;  
	} 

	public String getUser_id(){ 
		return this.user_id;  } 

	public String getUser_code(){
		return this.user_code;
	} 

	public String getThisyear(){
		return this.thisyear; 
	} 

	public String getSum(){
		return this.sum;    
	}

	public String getRest(){
		return this.rest;   
	}
	
	public String getJan1(){
		return this.jan1;		
	} 

	public String getFeb2(){
		return this.feb2;	
	} 

	public String getMar3(){
		return this.mar3;	
	} 

	public String getApr4(){
		return this.apr4;	
	} 

	public String getMay5(){ 
		return this.may5;	
	} 

	public String getJun6(){
		return this.jun6;	
	} 

	public String getJul7(){ 
		return this.jul7;	
	} 

	public String getAug8(){
		return this.aug8;	
	} 

	public String getSep9(){
		return this.sep9;	
	} 

	public String getOct10(){
		return this.oct10;	
	} 

	public String getNov11(){
		return this.nov11;	
	} 

	public String getDec12(){
		return this.dec12;	
	} 

	public String getKindtokor(){
		return this.kindtokor;
	}

}
