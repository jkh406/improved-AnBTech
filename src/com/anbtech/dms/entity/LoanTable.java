/************************************************************
 * 
 * loan_list 테이블 내용을 set/get
 *
 ************************************************************/

package com.anbtech.dms.entity;

public class LoanTable{

	private String no;				// 관리번호
	private String loan_no;			// 대출번호
	private String data_id;			// 데이터 번호
	private String ver_code;		// 버젼
	private String doc_no;			// 문서번호
	private String requestor;		// 대출신청자 정보
	private String req_date;		// 대출 신청일
	private String return_date;		// 대출 처리 일자
	private String why_loan;		// 대출 사유
	private String copy_num;		// 대출 카피수
	private String stat;			// 처리상태 코드
	private String why_reject;		// 처리 결과 메시지
	private String loanend_date;	// 반납 예정일
	
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