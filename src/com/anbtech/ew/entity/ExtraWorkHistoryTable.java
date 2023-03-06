// overwork_history
package com.anbtech.ew.entity;

public class ExtraWorkHistoryTable
{
	private int	o_no;			// 관리번호
	private String member_id;		// 작성자(신청자)id
	private String member_name;	// 작성자(신청자)이름
	private String member_rank;	// 작성자(신청자)직책
	private String member_rankname;// 작성자(신청자)직책(text)
	private String division;		// 작성자(신청자) 부서 
	private String division_name;	// 작성자(신청자) 부서(text)
	private String pc_ip;			// 작성자 computer ip
	private String w_sdate;		// 작업 신청 시작일자  ex) 20001203
	private String w_stime;		// 작업 신청 시작시간  es) 2150
	private String w_edate;		// 작업 신청 마침
	private String w_etime;		// 작업 신청 마침시간
	private String c_date;			// 작성 일자
	private String status;			// 상태 코드
	private String r_sdate;		// 실제 시작일자
	private String r_stime;		// 실제 시작시간
	private String r_edate;		// 실제 마침일자
	private String r_etime;		// 실제 마침시간
	private String total_time;		// 총 근무 시간
	private String ew_confirm;		// 당직자 확인 '5' 시작확인 '6' 마침확인
	private String duty;			// 작업내용
	private String duty_cont;		// 작업상세내용
	private String w_type;			// 특근일 날짜 타입
	private String cal_confirm;	// 정산여부(y/n)
	private String result_time;	// 실 적용 시간
	private String workman_num;	// 특근 근무 인원
	private String start_time;		// 출근시간
	private String end_time;		// 퇴근시간
	private String time_sum;		// 근무시간 합
	private String pay_sum;		// 수당 합
	private String num_sum;		// 인원 합
	private String status_name;	// 상태 명
	private String pay_by_work;	// 수당
	private String pay_by_work_won; // 수당 원표시
	private String day;
	private String ys_kind;
	private String w_typename;
	private String confirm_date;
	private String aid;


	// setter...
	public void setOno(int o_no){
		this.o_no = o_no;	
	}
	public void setMemberId(String member_id){
		this.member_id = member_id;
	}
	public void setMemberName(String member_name){
		this.member_name = member_name;
	}
	public void setMemberRank(String member_rank){
		this.member_rank = member_rank;
	}
	public void setMemberRankName(String member_rankname){
		this.member_rankname = member_rankname;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setDivision(String division){
		this.division = division;
	}
	public void setDivisionName(String division_name){
		this.division_name = division_name;
	}
	public void setPcIp(String pc_ip){
		this.pc_ip = pc_ip;
	}
	public void setWsdate(String w_sdate){
		this.w_sdate = w_sdate;
	}
	public void setWedate(String w_edate){
		this.w_edate = w_edate;
	}
	public void setWstime(String w_stime){
		this.w_stime = w_stime;
	}
	public void setWetime(String w_etime){
		this.w_etime = w_etime;
	}
	public void setCdate(String c_date) {
		this.c_date = c_date;
	}
	public void setRsdate(String r_sdate){
		this.r_sdate = r_sdate;
	}
	public void setRstime(String r_stime){
		this.r_stime = r_stime;
	}
	public void setRedate(String r_edate){
		this.r_edate = r_edate;
	}
	public void setRetime(String r_etime){
		this.r_etime = r_etime;
	}
	public void setEwConfirm(String ew_confirm){
		this.ew_confirm = ew_confirm;
	}
	public void setDuty(String duty){
		this.duty = duty;
	}
	public void setDutyCont(String duty_cont){
		this.duty_cont = duty_cont;
	}
	public void setTotalTime(String total_time){
		this.total_time = total_time;
	}
	public void setStatusName(String status_name){
		this.status_name = status_name;
	}
	public void setPayByWork(String pay_by_work){
		this.pay_by_work = pay_by_work;
	}
	public void setDay(String day){
		this.day = day;
	}
	public void setYsKind(String ys_kind){
		this.ys_kind = ys_kind;
	}
	public void setWtype(String w_type){
		this.w_type = w_type;
	}
	public void setWtypeName(String w_typename){
		this.w_typename = w_typename;
	}
	public void setCalConfirm(String cal_confirm){
		this.cal_confirm = cal_confirm;
	}
	public void setResultTime(String result_time) {
		this.result_time =  result_time;
	}
	public void setWorkManNum(String workman_num) {
		this.workman_num = workman_num;
	}
	public void setConfirmDate(String confirm_date) {
		this.confirm_date = confirm_date;
	}
    public void setStartTime(String start_time) {
		this.start_time = start_time;
	}
	public void setEndTime(String end_time) {
		this.end_time = end_time;
	}
	public void setTimeSum(String time_sum) {
		this.time_sum = time_sum;
	}
	public void setPaySum(String pay_sum){
		this.pay_sum = pay_sum;
	}
	public void setNumSum(String num_sum){
	    this.num_sum = num_sum;	
	}
	public void setPayByWorkWon(String pay_by_work_won){
		this.pay_by_work_won = pay_by_work_won;
	}
	public void setAid(String aid){
		this.aid = aid;
	}

	// getter....
	public int getOno(){
		return o_no;
	}
	public String getMemberId(){
		return member_id;
	}
	public String getMemberName(){
		return member_name;
	}
	public String getMemberRank(){
		return member_rank;
	}
	public String getMemberRankName(){
		return member_rankname;
	}
	public String getStatus(){
		return status;
	}
	public String getPcIp(){
		return pc_ip;
	}
	public String getWsdate(){
		return w_sdate;
	}
	public String getWedate(){
		return w_edate;
	}
	public String getWstime(){
		return w_stime;
	}
	public String getWetime(){
		return w_etime;
	}
	public String getCdate(){
		return c_date;
	}
	public String getRsdate(){
		return r_sdate;
	}
	public String getRstime(){
		return r_stime;
	}
	public String getRedate(){
		return r_edate;
	}
	public String getRetime(){
		return r_etime;
	}
	public String getDivision(){
		return division;
	}
	public String getDivisionName(){
		return division_name;
	}
	public String getEwConfirm(){
		return ew_confirm;
	}
	public String getDuty(){
		return duty;
	}
	public String getDutyCont(){
		return duty_cont;
	}
	public String getTotalTime(){
		return total_time;
	}
	public String getStatusName(){
		return status_name;
	}
	public String getPayByWork(){
		return pay_by_work;
	}
	public String getDay(){
		return day;
	}
	public String getYsKind(){
		return ys_kind;
	}
	public String getWtype(){
		return w_type;
	}
	public String getWtypeName(){
		return w_typename;
	}
	public String getCalConfirm(){
		return cal_confirm;
	}
	public String getResultTime(){
		return result_time;
	}
	public String getWorkManNum(){
		return workman_num;
	}
	public String getConfirmDate(){
		return confirm_date;
	}
	public String getStartTime(){
		return start_time;
	}
	public String getEndTime(){
		return end_time;
	}
	public String getTimeSum(){
		return time_sum;
	}
	public String getPaySum(){
		return pay_sum;
	}
	public String getNumSum(){
		return num_sum;
	}
	public String getPayByWorkWon(){
		return pay_by_work_won;
	}
	public String getAid(){
		return aid;
	}	
};