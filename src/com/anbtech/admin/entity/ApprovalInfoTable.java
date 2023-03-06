/************************************************************
 * 
 * 전자결재 정보 set/get
 *
 ************************************************************/

package com.anbtech.admin.entity;

public class ApprovalInfoTable{

	private String memo;
	private String writer_sig;
	private String writer_name;
	private String reviewer_sig;
	private String reviewer_name;
	private String decision_sig;
	private String decision_name;

	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo){
		this.memo = memo;
	}

	public String getWriterSig(){
		return this.writer_sig;
	}
	public void setWriterSig(String writer_sig){
		this.writer_sig = writer_sig;
	}

	public String getWriterName() {
		return this.writer_name;
	}
	public void setWriterName(String writer_name){
		this.writer_name = writer_name;
	}

	public String getReviewerSig(){
		return this.reviewer_sig;
	}
	public void setReviewerSig(String reviewer_sig){
		this.reviewer_sig = reviewer_sig;
	}

	public String getReviewerName() {
		return this.reviewer_name;
	}
	public void setReviewerName(String reviewer_name){
		this.reviewer_name = reviewer_name;
	}

	public String getDecisionSig(){
		return this.decision_sig;
	}
	public void setDecisionSig(String decision_sig){
		this.decision_sig = decision_sig;
	}

	public String getDecisionName(){
		return this.decision_name;
	}
	public void setDecisionName(String decision_name){
		this.decision_name = decision_name;
	}
}