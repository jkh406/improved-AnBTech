/*********************************************
* car_use_info ���̺� getXX / setXX method
**********************************************/
package com.anbtech.br.entity;

public class  CarUseInfoTable
{
	private String cr_id;		// ������ȣ(���α׷� �ڵ�����)
	private String doc_id;		// ����������ȣ(�����Ĺ�����ȣ�Է�)
	private String ys_kind;		// �������� ��� ����(BAE_CHA)
	private String write_id;	// �ۼ���(id)
	private String write_name;  // �ۼ��� �̸�
	private String user_id;		// ����ڻ��(id)
	private String user_name;   // ������̸�
	private String fellow_names;// �����ڸ�(���/�̸�)
	private String in_date;		// �ۼ�����(��/��/��/��)
	private String c_id;		// ����������ȣ
	private String v_status;	// �����ڿ�����
	private String u_year;		// ������û�ð�(��)
	private String u_month;		// ������û�ð�(��)
	private String u_date;		// ������û�ð�(��)
	private String u_time;		// ������û�ð�(��)
	private String tu_year;		// �ݳ������ð�(��)
	private String tu_month;	// �ݳ������ð�(��)
	private String tu_date;		// �ݳ������ð�(��)
	private String tu_time;		// �ݳ������ð�(��)
	private String cr_purpose;	// ������û ����(����)
	private String cr_dest;		// �༱��
	private String content;		// �󼼳���(��������)
	private String return_date;	// �ݳ�����(��/��/��/��)
	private String mgr_id;		// �ݳ�Ȯ�� ���
	private String mgr_name;	// �ݳ�Ȯ�� �̸�
	private String chg_cont;	// �������
	private String c_year;		// �ݳ�����ð�(��)
	private String c_month;		// �ݳ�����ð�(��)
	private String c_date;		// �ݳ�����ð�(��)
	private String c_time;		// �ݳ�����ð�(��)
	private String md_date;		// �ݳ����� ������(��/��/��/��)
	private String em_tel;		// ��޿���ó
	private int del_date;		// ��������()	
	private String entering_state; // �԰�Ǿ��ִ� ���� ����
	private String v_status_str; // �ڿ� ���� ��Ʈ��()
/*	private String file_path;	// ÷����������path
	private String fname;		// ÷�μ��������̸�
	private String ftype;		// ÷�μ���Ȯ���ڸ�	
	private String fsize;		// ÷������size
*/
	private String flag;		// 1���ְ��μ�(����flag)
	private String flag2;		// 2������μ�(����flag)

	private String car_type;
	private String car_no;
	private String model_name;
	private String car_stat;
	
	private String user_code;
	private String user_rank;
	private String ac_id;
	private String ac_code;
	private String ac_name;

	
	//setXX method
	public void setCrId(String cr_id){ 	
		this.cr_id=cr_id; 	
	}
	public void setDocId(String doc_id){ 
		this.doc_id=doc_id;	
	}
	public void setYsKind(String ys_kind){ 
		this.ys_kind=ys_kind;
	}		
	public void setUserId(String user_id){
		this.user_id=user_id;
	}	
	public void setUserName(String user_name){
		this.user_name=user_name;
	}	
	public void setFellowNames(String fellow_names){
		this.fellow_names=fellow_names;
	}			
	public void setInDate(String in_date){
		this.in_date=in_date;
	}				
	public void setCid(String c_id){
		this.c_id=c_id;
	}				
				
	public void setVstatus(String v_status){
		this.v_status=v_status;
	}			
	public void setUyear(String u_year){
		this.u_year=u_year;
	}				
	public void setUmonth(String u_month){
		this.u_month=u_month;
	}				
	public void setUdate(String u_date){
		this.u_date=u_date;
	}			
	public void setUtime(String u_time){
		this.u_time=u_time;
	}				
	public void setTuYear(String tu_year){
		this.tu_year=tu_year;
	}				
	public void setTuMonth(String tu_month){
		this.tu_month=tu_month;
	}			
	public void setTuDate(String tu_date){
		this.tu_date=tu_date;
	}				
	public void setTuTime(String tu_time){
		this.tu_time=tu_time;
	}			
	public void setCrPurpose(String cr_purpose){
		this.cr_purpose=cr_purpose;
	}			
	public void setCrDest(String cr_dest){
		this.cr_dest=cr_dest;
	}				
	public void setContent(String content){
		this.content=content;
	}				
	public void setReturnDate(String return_date){
		this.return_date=return_date;
	}			
	public void setMgrId(String mgr_id){
		this.mgr_id=mgr_id;
	}				
	public void setMgrName(String mgr_name){
		this.mgr_name=mgr_name;
	}			
	public void setChgCont(String chg_cont){
		this.chg_cont=chg_cont;
	}			
	public void setCyear(String c_year){
		this.c_year=c_year;
	}				
	public void setCmonth(String c_month){
		this.c_month=c_month;
	}				
	public void setCdate(String c_date){
		this.c_date=c_date;
	}				
	public void setCtime(String c_time){
		this.c_time=c_time;
	}				
	public void setMdDate(String md_date){
		this.md_date=md_date;
	}				
	public void setEmTel(String em_tel){
		this.em_tel=em_tel;
	}				
	private void setDelDate(int del_date){
		this.del_date=del_date;
	}			

	public void setFlag(String flag){
		this.flag=flag;
	}				
	public void setFlag2(String flag2){
		this.flag2=flag2;
	}	
	
	public void setCarType(String car_type){
		this.car_type=car_type;
	}
	public void setCarNo(String car_no){
		this.car_no=car_no;
	}
	public void setModelName(String model_name){
		this.model_name=model_name;
	}

	public void setUserCode(String user_code){
		this.user_code =  user_code;
	}
	public void setUserRank(String user_code){
		this.user_code =  user_code;
	}
	public void setAcId(String ac_id){
		this.ac_id =  ac_id;
	}
	public void setAcCode(String ac_code){
		this.ac_code =  ac_code;
	}
	public void setAcName(String ac_name){
		this.ac_name =  ac_name;
	}

	public void setWriteId(String write_id){
		this.write_id=write_id;
	}
	public void setWriteName(String write_name){
		this.write_name=write_name;
	}

	public void setEnteringState(String entering_state){
		this.entering_state = entering_state;
	}
	
	public void setVstatusStr(String v_status_str){
		this.v_status_str=v_status_str;
	}

	public void setCarStat(String car_stat){
		this.car_stat = car_stat;
	}
	
	
	//getXX method
	public String getCrId(){
		return cr_id;
	}
	public String getDocId(){
		return doc_id;
	}
	
	public String getYsKind(){
		return ys_kind;
	}		
	public String getUserId(){
		return user_id;
	}				
	public String getFellowNames(){
		return fellow_names;
	}			
	public String getInDate(){
		return in_date;
	}				
	public String getCid(){
		return c_id;
	}				
			
	public String getVstatus(){
		return v_status;
	}			
	public String getUyear(){
		return u_year;
	}				
	public String getUmonth(){
		return u_month;
	}				
	public String getUdate(){
		return u_date;
	}			
	public String getUtime(){
		return u_time;
	}				
	public String getTuYear(){
		return tu_year;
	}				
	public String getTuMonth(){
		return tu_month;
	}			
	public String getTuDate(){
		return tu_date;
	}				
	public String getTuTime(){
		return tu_time;
	}			
	public String getCrPurpose(){
		return cr_purpose;
	}			
	public String getCrDest(){
		return cr_dest;
	}				
	public String getContent(){
		return content;
	}				
	public String getReturnDate(){
		return return_date;
	}			
	public String getMgrId(){
		return mgr_id;
	}				
	public String getMgrName(){
		return mgr_name;
	}			
	public String getChgCont(){
		return chg_cont;
	}			
	public String getCyear(){
		return c_year;
	}				
	public String getCmonth(){
		return c_month;
	}				
	public String getCdate(){
		return c_date;
	}				
	public String getCtime(){
		return c_time;
	}				
	public String getMdDate(){
		return md_date;
	}				
	public String getEmTel(){
		return em_tel;
	}				
	private int getDelDate(){
		return del_date;
	}			

	public String getFlag(){
		return flag;
	}				
	public String getFlag2(){
		return flag2;
	}		
	public String setUserName(){
		return user_name;
	}	

	public String getCarType(){
		return car_type;
	}
	
	public String getCarNo(){
		return car_no;
	}

	public String getModelName(){
		return model_name;
	}

	public String getUserCode(){
		return  user_code;
	}
	public String getUserRank(){
		return user_code;
	}
	public String getAcId(){
		return ac_id;
	}
	public String getAcCode(){
		return  ac_code;
	}
	public String getAcName(){
		return  ac_name;
	}

	public String getUserName(){
		return user_name;
	}

	public String getWriteId(){
		return write_id;
	}

	public String getWriteName(){
		return write_name;
	}

	public String getEnteringState(){
		return entering_state;
	}

	public String getVstatusStr(){
		return v_status_str;
	}
	public String getCarStat(){
		return car_stat;	
	}

	

	//////////////////////////////////\\
	public static void main(String[] args) 
	{
		System.out.println("Hello World!");
	}
}
