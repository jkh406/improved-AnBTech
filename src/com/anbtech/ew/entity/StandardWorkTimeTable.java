package com.anbtech.ew.entity;

public class  StandardWorkTimeTable
{
	// Ư�� ���� �ð� ���� ����
	private int sw_no;						// Ư�� ���� �ð� TABLE ������ȣ
	private String modify_date;				// Ư�� ���� �ð� ���� ����
	private String fix_stime;				// ���� ���� ���� �ð�
	private String fix_etime;				// ���� ���� ��ħ �ð�
	private String fix_bound;				// ���� ���� �ٷ� �ð�

	private String fix_stime_sat;			// ���� ���� ���� �ð� 
	private String fix_etime_sat;			// ���� ���� ��ħ �ð�
	private String fix_bound_sat;			// ���� ���� �ٷ� �ð�

	private String fix_stime_holiday;		// ���� ���� ���� �ð�
	private String fix_etime_holiday;		// ���� ���� ��ħ �ð�

	private String off_stime;				// �ð��� �ٹ� ���� �ð�
	private String off_etime;				// �ð��� �ٹ� ��ħ �ð�
	private String off_bound;				// �ð��� �ٹ� �ٷ� �ð�

	private String off_stime_sat;			// ���� �ٹ� ���� �ð�(�����)
	private String off_etime_sat;			// ���� �ٹ� ��ħ �ð�(�����)

	private String off_stime_holiday;		// ���� �ٹ� ���� �ð�(����)
	private String off_etime_holiday;		// ���� �ٹ� ��ħ �ð�(����)
	
	private String to_tom;					// ����/ ���� ������ (t:���� m:����)
	private String to_tom_name;				// ����/ ���� ������ ��

	private String overday_n;				// ���� ���� �ٹ� ����
	private String overday_h;				// ���� ���� �ٹ� ����
	private String overday_s;				// ����� ���� �ٹ� ����
	private String overday_n_name;	 
	private String overday_h_name;
	private String overday_s_name;

	// setter...
	public void setSwNo(int sw_no){
		this.sw_no = sw_no;
	}
	public void setModifyDate(String modify_date){
		this.modify_date = modify_date;
	}
	public void setFixStime(String fix_stime){
		this.fix_stime = fix_stime;
	}
	public void setFixEtime(String fix_etime){
		this.fix_etime = fix_etime;
	}
	public void setFixBound(String fix_bound){
		this.fix_bound_sat = fix_bound_sat;
	}
	public void setFixStimeSat(String fix_stime_sat){
		this.fix_stime_sat = fix_stime_sat;
	}
	public void setFixEtimeSat(String fix_etime_sat){
		this.fix_etime_sat = fix_etime_sat;
	}
	public void setFixBoundSat(String fix_bound_sat){
		this.fix_bound_sat = fix_bound_sat;
	}
	public void setOffStime(String off_stime){
		this.off_stime = off_stime;
	}
	public void setOffEtime(String off_etime){
		this.off_etime = off_etime;
	}
	public void setOffBound(String off_bound){
		this.off_bound = off_bound;
	}
	public void setToTom(String to_tom){
		this.to_tom = to_tom;
	}
	public void setToTomName(String to_tom_name){
		this.to_tom_name = to_tom_name;
	}
	public void setOverDayN(String overday_n){
		this.overday_n = overday_n;
	}
	public void setOverDayH(String overday_h){
		this.overday_h = overday_h;
	}
	public void setOverDayS(String overday_s){
		this.overday_s = overday_s;
	}
	public void setOverDayNName(String overday_n_name){
		this.overday_n_name = overday_n_name;
	}		
	public void setOverDayHName(String overday_h_name){
		this.overday_h_name = overday_h_name;
	}
	public void setOverDaySName(String overday_s_name){
		this.overday_s_name = overday_s_name;
	}

	public void setFixStimeHoliday(String fix_stime_holiday){
		this.fix_stime_holiday = fix_stime_holiday;
	}
	public void setFixEtimeHoliday(String fix_etime_holiday){
		this.fix_etime_holiday = fix_etime_holiday;
	}	

	public void setOffStimeSat(String off_stime_sat){
		this.off_stime_sat =  off_stime_sat;
	}
	public void setOffEtimeSat(String off_etime_sat){
		this.off_etime_sat = off_etime_sat;
	}

	public void setOffStimeHoliday(String off_stime_holiday){
		this.off_stime_holiday = off_stime_holiday;
	}
	public void setOffEtimeHoliday(String off_etime_holiday){
		this.off_etime_holiday = off_etime_holiday;
	}
	

	// getter...
	public int getSwNo(){
		return sw_no;
	}
	public String getModifyDate(){
		return  modify_date;
	}
	public String getFixStime(){
		return fix_stime;
	}
	public String getFixEtime(){
		return  fix_etime;
	}
	public String getFixBound(){
		return fix_bound_sat;
	}
	public String getFixStimeSat(){
		return fix_stime_sat;
	}
	public String getFixEtimeSat(){
		return fix_etime_sat;
	}
	public String getFixBoundSat(){
		return fix_bound_sat;
	}
	public String getOffStime(){
		return off_stime;
	}
	public String getOffEtime(){
		return off_etime;
	}
	public String getOffBound(){
		return off_bound;
	}
	public String getToTom(){
		return to_tom;
	}
	public String getToTomName(){
		return to_tom_name;
	}
	public String getOverDayN(){
		return overday_n;
	}
	public String getOverDayH(){
		return overday_h;
	}
	public String getOverDayS(){
		return overday_s;
	}
	public String getOverDayNName(){
		return overday_n_name;
	}
	public String getOverDayHName(){
		return overday_h_name;
	}
	public String getOverDaySName(){
		return overday_s_name;
	}

	public String getFixStimeHoliday(){
		return fix_stime_holiday;
	}
	public String getFixEtimeHoliday(){
		return fix_etime_holiday;
	}

	public String getOffStimeSat(){
		return off_stime_sat;
	}
	public String getOffEtimeSat(){
		return off_etime_sat;
	}

	public String getOffStimeHoliday(){
		return off_stime_holiday;
	}
	public String getOffEtimeHoliday(){
		return off_etime_holiday;
	}
}
