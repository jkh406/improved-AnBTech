/************************************************************
**	전자결재 입고 테이블 컬럼 정의 클래스
*************************************************************/

package com.anbtech.gw.entity;
public class TableAppSave
{
	//------------------------------------------------------
	// 변수 선언하기
	//------------------------------------------------------
	private String pid;
	private String app_subj;
	private String writer;
	private String writer_name;
	private String write_date;
	private String app_state;
	private String reviewer;
	private String review_date;
	private String review_comment;
	private String agree;
	private String agree_date;
	private String agree_comment;
	private String agree2;
	private String agree2_date;
	private String agree2_comment;
	private String agree3;
	private String agree3_date;
	private String agree3_comment;
	private String agree4;
	private String agree4_date;
	private String agree4_comment;
	private String agree5;
	private String agree5_date;
	private String agree5_comment;
	private String agree6;
	private String agree6_date;
	private String agree6_comment;
	private String agree7;
	private String agree7_date;
	private String agree7_comment;
	private String agree8;
	private String agree8_date;
	private String agree8_comment;
	private String agree9;
	private String agree9_date;
	private String agree9_comment;
	private String agree10;
	private String agree10_date;
	private String agree10_comment;
	private String agree_method;
	private String agree_count;
	private String agree_pass;
	private String decision;
	private String decision_date;
	private String decision_comment;
	private String receivers;
	private String app_line;
	private String bon_path;
	private String bon_file;
	private String add_1_original;
	private String add_1_file;
	private String add_2_original;
	private String add_2_file;
	private String add_3_original;
	private String add_3_file;
	private String save_period;
	private String delete_date;
	private String security_level;
	private String plid;
	private String flag;

	private String writer_div;
	private String writer_rank;
	private String reviewer_name;
	private String reviewer_div;
	private String reviewer_rank;
	private String agree_name;
	private String agree_div;
	private String agree_rank;
	private String agree2_name;
	private String agree2_div;
	private String agree2_rank;
	private String agree3_name;
	private String agree3_div;
	private String agree3_rank;
	private String agree4_name;
	private String agree4_div;
	private String agree4_rank;
	private String agree5_name;
	private String agree5_div;
	private String agree5_rank;
	private String agree6_name;
	private String agree6_div;
	private String agree6_rank;
	private String agree7_name;
	private String agree7_div;
	private String agree7_rank;
	private String agree8_name;
	private String agree8_div;
	private String agree8_rank;
	private String agree9_name;
	private String agree9_div;
	private String agree9_rank;
	private String agree10_name;
	private String agree10_div;
	private String agree10_rank;
	private String decision_name;
	private String decision_div;
	private String decision_rank;

	//기타
	private String add_counter;		//첨부파일 수량
	private String sub_link;		//sub link

	//------------------------------------------------------
	// method 만들기
	//------------------------------------------------------
	public void setAmPid(String pid) {
		this.pid = pid;
	}
	public String getAmPid() {
		return this.pid;
	}

	public void setAmAppSubj(String app_subj) {
		this.app_subj = app_subj;
	}
	public String getAmAppSubj() {
		return this.app_subj;
	}
	
	public void setAmWriter(String writer) {
		this.writer = writer;
	}
	public String getAmWriter() {
		return this.writer;
	}
	
	public void setAmWriterName(String writer_name) {
		this.writer_name = writer_name;
	}
	public String getAmWriterName() {
		return this.writer_name;
	}
	
	public void setAmWriteDate(String write_date) {
		this.write_date = write_date;
	}
	public String getAmWriteDate() {
		return this.write_date;
	}

	public void setAmAppStatus(String app_state) {
		this.app_state = app_state;
	}
	public String getAmAppStatus() {
		return this.app_state;
	}
	
	public void setAmReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getAmReviewer() {
		return this.reviewer;
	}
	
	public void setAmReviewDate(String review_date) {
		this.review_date = review_date;
	}
	public String getAmReviewDate() {
		return this.review_date;
	}
	public void setAmReviewComment(String review_comment) {
		this.review_comment = review_comment;
	}
	public String getAmReviewComment() {
		return this.review_comment;
	}

	public void setAmAgree(String agree) {
		this.agree = agree;
	}
	public String getAmAgree() {
		return this.agree;
	}

	public void setAmAgreeDate(String agree_date) {
		this.agree_date = agree_date;
	}
	public String getAmAgreeDate() {
		return this.agree_date;
	}

	public void setAmAgreeComment(String agree_comment) {
		this.agree_comment = agree_comment;
	}
	public String getAmAgreeComment() {
		return this.agree_comment;
	}

	public void setAmAgree2(String agree2) {
		this.agree2 = agree2;
	}
	public String getAmAgree2() {
		return this.agree2;
	}

	public void setAmAgree2Date(String agree2_date) {
		this.agree2_date = agree2_date;
	}
	public String getAmAgree2Date() {
		return this.agree2_date;
	}

	public void setAmAgree2Comment(String agree2_comment) {
		this.agree2_comment = agree2_comment;
	}
	public String getAmAgree2Comment() {
		return this.agree2_comment;
	}
	
	public void setAmAgree3(String agree3) {
		this.agree3 = agree3;
	}
	public String getAmAgree3() {
		return this.agree3;
	}

	public void setAmAgree3Date(String agree3_date) {
		this.agree3_date = agree3_date;
	}
	public String getAmAgree3Date() {
		return this.agree3_date;
	}

	public void setAmAgree3Comment(String agree3_comment) {
		this.agree3_comment = agree3_comment;
	}
	public String getAmAgree3Comment() {
		return this.agree3_comment;
	}
	
	public void setAmAgree4(String agree4) {
		this.agree4 = agree4;
	}
	public String getAmAgree4() {
		return this.agree4;
	}

	public void setAmAgree4Date(String agree4_date) {
		this.agree4_date = agree4_date;
	}
	public String getAmAgree4Date() {
		return this.agree4_date;
	}

	public void setAmAgree4Comment(String agree4_comment) {
		this.agree4_comment = agree4_comment;
	}
	public String getAmAgree4Comment() {
		return this.agree4_comment;
	}

	public void setAmAgree5(String agree5) {
		this.agree5 = agree5;
	}
	public String getAmAgree5() {
		return this.agree5;
	}

	public void setAmAgree5Date(String agree5_date) {
		this.agree5_date = agree5_date;
	}
	public String getAmAgree5Date() {
		return this.agree5_date;
	}

	public void setAmAgree5Comment(String agree5_comment) {
		this.agree5_comment = agree5_comment;
	}
	public String getAmAgree5Comment() {
		return this.agree5_comment;
	}

	public void setAmAgree6(String agree6) {
		this.agree6 = agree6;
	}
	public String getAmAgree6() {
		return this.agree6;
	}

	public void setAmAgree6Date(String agree6_date) {
		this.agree6_date = agree6_date;
	}
	public String getAmAgree6Date() {
		return this.agree6_date;
	}

	public void setAmAgree6Comment(String agree_comment) {
		this.agree6_comment = agree6_comment;
	}
	public String getAmAgree6Comment() {
		return this.agree6_comment;
	}

	public void setAmAgree7(String agree7) {
		this.agree7 = agree7;
	}
	public String getAmAgree7() {
		return this.agree7;
	}

	public void setAmAgree7Date(String agree7_date) {
		this.agree7_date = agree7_date;
	}
	public String getAmAgree7Date() {
		return this.agree7_date;
	}

	public void setAmAgree7Comment(String agree7_comment) {
		this.agree7_comment = agree7_comment;
	}
	public String getAmAgree7Comment() {
		return this.agree7_comment;
	}

	public void setAmAgree8(String agree8) {
		this.agree8 = agree8;
	}
	public String getAmAgree8() {
		return this.agree8;
	}

	public void setAmAgree8Date(String agree8_date) {
		this.agree8_date = agree8_date;
	}
	public String getAmAgree8Date() {
		return this.agree8_date;
	}

	public void setAmAgree8Comment(String agree8_comment) {
		this.agree8_comment = agree8_comment;
	}
	public String getAmAgree8Comment() {
		return this.agree8_comment;
	}

	public void setAmAgree9(String agree9) {
		this.agree9 = agree9;
	}
	public String getAmAgree9() {
		return this.agree9;
	}

	public void setAmAgree9Date(String agree9_date) {
		this.agree9_date = agree9_date;
	}
	public String getAmAgree9Date() {
		return this.agree9_date;
	}

	public void setAmAgree9Comment(String agree9_comment) {
		this.agree9_comment = agree9_comment;
	}
	public String getAmAgree9Comment() {
		return this.agree9_comment;
	}

	public void setAmAgree10(String agree10) {
		this.agree10 = agree10;
	}
	public String getAmAgree10() {
		return this.agree10;
	}

	public void setAmAgree10Date(String agree10_date) {
		this.agree10_date = agree10_date;
	}
	public String getAmAgree10Date() {
		return this.agree10_date;
	}

	public void setAmAgree10Comment(String agree10_comment) {
		this.agree10_comment = agree10_comment;
	}
	public String getAmAgree10Comment() {
		return this.agree10_comment;
	}

	public void setAmAgreeMethod(String agree_method) {
		this.agree_method = agree_method;
	}
	public String getAmAgreeMethod() {
		return this.agree_method;
	}

	public void setAmAgreeCount(String agree_count) {
		this.agree_count = agree_count;
	}
	public String getAmAgreeCount() {
		return this.agree_count;
	}

	public void setAmAgreePass(String agree_pass) {
		this.agree_pass = agree_pass;
	}
	public String getAmAgreePass() {
		return this.agree_pass;
	}
	
	public void setAmDecision(String decision) {
		this.decision = decision;
	}
	public String getAmDecision() {
		return this.decision;
	}

	public void setAmDecisionDate(String decision_date) {
		this.decision_date = decision_date;
	}
	public String getAmDecisionDate() {
		return this.decision_date;
	}

	public void setAmDecisionComment(String decision_comment) {
		this.decision_comment = decision_comment;
	}
	public String getAmDecisionComment() {
		return this.decision_comment;
	}

	public void setAmReceivers(String receivers) {
		this.receivers = receivers;
	}
	public String getAmReceivers() {
		return this.receivers;
	}
	
	public void setAmAppLine(String app_line) {
		this.app_line = app_line;
	}
	public String getAmAppLine() {
		return this.app_line;
	}
	
	public void setAmBonPath(String bon_path) {
		this.bon_path = bon_path;
	}
	public String getAmBonPath() {
		return this.bon_path;
	}
	
	public void setAmBonFile(String bon_file) {
		this.bon_file = bon_file;
	}
	public String getAmBonFile() {
		return this.bon_file;
	}

	public void setAmAdd1Original(String add_1_original) {
		this.add_1_original = add_1_original;
	}
	public String getAmAdd1Original() {
		return this.add_1_original;
	}

	public void setAmAdd1File(String add_1_file) {
		this.add_1_file = add_1_file;
	}
	public String getAmAdd1File() {
		return this.add_1_file;
	}

	public void setAmAdd2Original(String add_2_original) {
		this.add_2_original = add_2_original;
	}
	public String getAmAdd2Original() {
		return this.add_2_original;
	}
	
	public void setAmAdd2File(String add_2_file) {
		this.add_2_file = add_2_file;
	}
	public String getAmAdd2File() {
		return this.add_2_file;
	}

	public void setAmAdd3Original(String add_3_original) {
		this.add_3_original = add_3_original;
	}
	public String getAmAdd3Original() {
		return this.add_3_original;
	}

	public void setAmAdd3File(String add_3_file) {
		this.add_3_file = add_3_file;
	}
	public String getAmAdd3File() {
		return this.add_3_file;
	}

	public void setAmSavePeriod(String save_period) {
		this.save_period = save_period;
	}
	public String getAmSavePeriod() {
		return this.save_period;
	}
	
	public void setAmDeleteDate(String delete_date) {
		this.delete_date = delete_date;
	}
	public String getAmDeleteDate() {
		return this.delete_date;
	}
	
	public void setAmSecurityLevel(String security_level) {
		this.security_level = security_level;
	}
	public String getAmSecurityLevel() {
		return this.security_level;
	}

	public void setAmPlid(String plid) {
		this.plid = plid;
	}
	public String getAmPlid() {
		return this.plid;
	}

	public void setAmFlag(String flag) {
		this.flag = flag;
	}
	public String getAmFlag() {
		return this.flag;
	}

	public void setAmWriterDiv(String writer_div) {
		this.writer_div = writer_div;
	}
	public String getAmWriterDiv() {
		return this.writer_div;
	}

	public void setAmWriterRank(String writer_rank) {
		this.writer_rank = writer_rank;
	}
	public String getAmWriterRank() {
		return this.writer_rank;
	}

	public void setAmReviewerName(String reviewer_name) {
		this.reviewer_name = reviewer_name;
	}
	public String getAmReviewerName() {
		return this.reviewer_name;
	}

	public void setAmReviewerDiv(String reviewer_div) {
		this.reviewer_div = reviewer_div;
	}
	public String getAmReviewerDiv() {
		return this.reviewer_div;
	}

	public void setAmReviewerRank(String reviewer_rank) {
		this.reviewer_rank = reviewer_rank;
	}
	public String getAmReviewerRank() {
		return this.reviewer_rank;
	}

	public void setAmAgreeName(String agree_name) {
		this.agree_name = agree_name;
	}
	public String getAmAgreeName() {
		return this.agree_name;
	}
	public void setAmAgreeDiv(String agree_div) {
		this.agree_div = agree_div;
	}
	public String getAmAgreeDiv() {
		return this.agree_div;
	}
	public void setAmAgreeRank(String agree_rank) {
		this.agree_rank = agree_rank;
	}
	public String getAmAgreeRank() {
		return this.agree_rank;
	}

	public void setAmAgree2Name(String agree2_name) {
		this.agree2_name = agree2_name;
	}
	public String getAmAgree2Name() {
		return this.agree2_name;
	}
	public void setAmAgree2Div(String agree2_div) {
		this.agree_div = agree_div;
	}
	public String getAmAgree2Div() {
		return this.agree2_div;
	}
	public void setAmAgree2Rank(String agree2_rank) {
		this.agree2_rank = agree2_rank;
	}
	public String getAmAgree2Rank() {
		return this.agree2_rank;
	}

	public void setAmAgree3Name(String agree3_name) {
		this.agree3_name = agree3_name;
	}
	public String getAmAgree3Name() {
		return this.agree3_name;
	}
	public void setAmAgree3Div(String agree3_div) {
		this.agree3_div = agree3_div;
	}
	public String getAmAgree3Div() {
		return this.agree3_div;
	}
	public void setAmAgree3Rank(String agree3_rank) {
		this.agree3_rank = agree3_rank;
	}
	public String getAmAgree3Rank() {
		return this.agree3_rank;
	}

	public void setAmAgree4Name(String agree4_name) {
		this.agree4_name = agree4_name;
	}
	public String getAmAgree4Name() {
		return this.agree4_name;
	}
	public void setAmAgree4Div(String agree4_div) {
		this.agree4_div = agree4_div;
	}
	public String getAmAgree4Div() {
		return this.agree4_div;
	}
	public void setAmAgree4Rank(String agree4_rank) {
		this.agree4_rank = agree4_rank;
	}
	public String getAmAgree4Rank() {
		return this.agree4_rank;
	}

	public void setAmAgree5Name(String agree5_name) {
		this.agree5_name = agree5_name;
	}
	public String getAmAgree5Name() {
		return this.agree5_name;
	}
	public void setAmAgree5Div(String agree5_div) {
		this.agree5_div = agree5_div;
	}
	public String getAmAgree5Div() {
		return this.agree5_div;
	}
	public void setAmAgree5Rank(String agree5_rank) {
		this.agree5_rank = agree5_rank;
	}
	public String getAmAgree5Rank() {
		return this.agree5_rank;
	}

	public void setAmAgree6Name(String agree6_name) {
		this.agree6_name = agree6_name;
	}
	public String getAmAgree6Name() {
		return this.agree6_name;
	}
	public void setAmAgree6Div(String agree6_div) {
		this.agree6_div = agree6_div;
	}
	public String getAmAgree6Div() {
		return this.agree6_div;
	}
	public void setAmAgree6Rank(String agree6_rank) {
		this.agree6_rank = agree6_rank;
	}
	public String getAmAgree6Rank() {
		return this.agree6_rank;
	}

	public void setAmAgree7Name(String agree7_name) {
		this.agree7_name = agree7_name;
	}
	public String getAmAgree7Name() {
		return this.agree7_name;
	}
	public void setAmAgree7Div(String agree7_div) {
		this.agree7_div = agree7_div;
	}
	public String getAmAgree7Div() {
		return this.agree7_div;
	}
	public void setAmAgree7Rank(String agree7_rank) {
		this.agree7_rank = agree7_rank;
	}
	public String getAmAgree7Rank() {
		return this.agree7_rank;
	}

	public void setAmAgree8Name(String agree8_name) {
		this.agree8_name = agree8_name;
	}
	public String getAmAgree8Name() {
		return this.agree8_name;
	}
	public void setAmAgree8Div(String agree8_div) {
		this.agree8_div = agree8_div;
	}
	public String getAmAgree8Div() {
		return this.agree8_div;
	}
	public void setAmAgree8Rank(String agree8_rank) {
		this.agree8_rank = agree8_rank;
	}
	public String getAmAgree8Rank() {
		return this.agree8_rank;
	}

	public void setAmAgree9Name(String agree9_name) {
		this.agree9_name = agree9_name;
	}
	public String getAmAgree9Name() {
		return this.agree9_name;
	}
	public void setAmAgree9Div(String agree9_div) {
		this.agree9_div = agree9_div;
	}
	public String getAmAgree9Div() {
		return this.agree9_div;
	}
	public void setAmAgree9Rank(String agree9_rank) {
		this.agree9_rank = agree9_rank;
	}
	public String getAmAgree9Rank() {
		return this.agree9_rank;
	}

	public void setAmAgree10Name(String agree10_name) {
		this.agree10_name = agree10_name;
	}
	public String getAmAgree10Name() {
		return this.agree10_name;
	}
	public void setAmAgree10Div(String agree10_div) {
		this.agree10_div = agree10_div;
	}
	public String getAmAgree10Div() {
		return this.agree10_div;
	}
	public void setAmAgree10Rank(String agree10_rank) {
		this.agree10_rank = agree10_rank;
	}
	public String getAmAgree10Rank() {
		return this.agree10_rank;
	}
	
	public void setAmDecisionName(String decision_name) {
		this.decision_name = decision_name;
	}
	public String getAmDecisionName() {
		return this.decision_name;
	}
	public void setAmDecisionDiv(String decision_div) {
		this.decision_div = decision_div;
	}
	public String getAmDecisionDiv() {
		return this.agree10_div;
	}
	public void setAmDecisionRank(String decision_rank) {
		this.decision_rank = decision_rank;
	}
	public String getAmDecisionRank() {
		return this.decision_rank;
	}

	public void setAmAddCounter(String add_counter) {
		this.add_counter = add_counter;
	}
	public String getAmAddCounter() {
		return this.add_counter;
	}

	public void setAmSubLink(String sub_link) {
		this.sub_link = sub_link;
	}
	public String getAmSubLink() {
		return this.sub_link;
	}
}