/************************************************************
 * 
 * loan_list ���̺� ������ set/get
 *
 ************************************************************/

package com.anbtech.dms.entity;

public class LoanTable{

	private String no;				// ������ȣ
	private String loan_no;			// �����ȣ
	private String data_id;			// ������ ��ȣ
	private String ver_code;		// ����
	private String doc_no;			// ������ȣ
	private String requestor;		// �����û�� ����
	private String req_date;		// ���� ��û��
	private String return_date;		// ���� ó�� ����
	private String why_loan;		// ���� ����
	private String copy_num;		// ���� ī�Ǽ�
	private String stat;			// ó������ �ڵ�
	private String why_reject;		// ó�� ��� �޽���
	private String loanend_date;	// �ݳ� ������
	
	public String getNo(){
		return no;
	}
	public void setNo(String no){
		this.no = no;
	}

	public String getLoanNo(){
		return loan_no;
	}
	public void setLoanNo(String loan_no){
		this.loan_no = loan_no;
	}

	public String getDataId(){
		return data_id;
	}
	public void setDataId(String data_id){
		this.data_id = data_id;
	}

	public String getVerCode(){
		return ver_code;
	}
	public void setVerCode(String ver_code){
		this.ver_code = ver_code;
	}

	public String getDocNo(){
		return doc_no;
	}
	public void setDocNo(String doc_no){
		this.doc_no = doc_no;
	}

	public String getRequestor(){
		return requestor;
	}
	public void setRequestor(String requestor){
		this.requestor = requestor;
	}

	public String getReqDate(){
		return req_date;
	}
	public void setReqDate(String req_date){
		this.req_date = req_date;
	}

	public String getReturnDate(){
		return return_date;
	}
	public void setReturnDate(String return_date){
		this.return_date = return_date;
	}

	public String getWhyLoan(){
		return why_loan;
	}
	public void setWhyLoan(String why_loan){
		this.why_loan = why_loan;
	}

	public String getCopyNum(){
		return copy_num;
	}
	public void setCopyNum(String copy_num){
		this.copy_num = copy_num;
	}

	public String getStat(){
		return stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}

	public String getWhyReject(){
		return why_reject;
	}
	public void setWhyReject(String why_reject){
		this.why_reject = why_reject;
	}

	public String getLoanEndDate(){
		return loanend_date;
	}
	public void setLoanEndDate(String loanend_date){
		this.loanend_date = loanend_date;
	}
}