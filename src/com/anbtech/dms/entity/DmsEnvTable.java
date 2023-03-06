/************************************************************
 * 
 * 기술문서 관리모듈 각종 모드 내용을 set/get
 *
 ************************************************************/

package com.anbtech.dms.entity;

public class DmsEnvTable{

	private String category_id;		// 카테고리 코드
	private String category_name;	// 카테고리명
	private String initial_char;	// 대표문자
	private String enable_revision;	// 리비젼관리 여부
	private String enable_project;	// 프로젝트관리 여부
	private String enable_model;	// 프로젝트관리 여부
	private String enable_eco;		// ECO관리 여부
	private String enable_approval;	// 전자결재관리 여부
	private String security_level;	// 보안등급
	private String save_period;		// 보존기간
	private String loan_period;		// 대출기간
	private String table_name;		// 테이블명


	public String getCategoryId(){
		return category_id;
	}
	public void setCategoryId(String category_id){
		this.category_id = category_id;
	}

	public String getCategoryName(){
		return category_name;
	}
	public void setCategoryName(String category_name){
		this.category_name = category_name;
	}

	public String getInitialChar(){
		return initial_char;
	}
	public void setInitialChar(String initial_char){
		this.initial_char = initial_char;
	}

	public String getEnableRevision(){
		return enable_revision;
	}
	public void setEnableRevision(String enable_revision){
		this.enable_revision = enable_revision;
	}

	public String getEnableProject(){
		return enable_project;
	}
	public void setEnableProject(String enable_project){
		this.enable_project = enable_project;
	}

	public String getEnableModel(){
		return enable_model;
	}
	public void setEnableModel(String enable_model){
		this.enable_model = enable_model;
	}

	public String getEnableEco(){
		return enable_eco;
	}
	public void setEnableEco(String enable_eco){
		this.enable_eco = enable_eco;
	}

	public String getEnableApproval(){
		return enable_approval;
	}
	public void setEnableApproval(String enable_approval){
		this.enable_approval = enable_approval;
	}

	public String getSecurityLevel(){
		return security_level;
	}
	public void setSecurityLevel(String security_level){
		this.security_level = security_level;
	}

	public String getSavePeriod(){
		return save_period;
	}
	public void setSavePeriod(String save_period){
		this.save_period = save_period;
	}

	public String getLoanPeriod(){
		return loan_period;
	}
	public void setLoanPeriod(String loan_period){
		this.loan_period = loan_period;
	}

	public String getTableName(){
		return table_name;
	}
	public void setTableName(String table_name){
		this.table_name = table_name;
	}
}