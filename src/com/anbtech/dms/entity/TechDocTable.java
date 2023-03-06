/************************************************************
 * 
 * techdoc_data 테이블 내용을 set/get
 *
 ************************************************************/

package com.anbtech.dms.entity;

public class TechDocTable{

	private String t_id;			// 관리번호.
	private String ver_code;		// 버젼코드
	private String ancestor;		// 최초버젼의 관리번호
	private String ver_no;			// 버젼번호(순차적으로 증가)
	private String subject;			// 문서제목
	private String doc_no;			// 문서번호(회사에서 관리되는)
	private String fname;			// 첨부파일 이름
	private String fsize;			// 첨부파일의 크기
	private String ftype;			// 첨부파일의 타입
	private String modify_history;	// 동일버젼에 대한 수정이력
	private String preview;			// 해당 문서의 내용 요약
	private String why_revision;	// 리비젼 시의 변경 내용 요약
	private String memo;			// 기타 주요사항
	private String save_period;		// 문서보존기간
	private String security_level;	// 문서 보안등급
	private String written_lang;	// 문서가 작성된 언어
	private String doc_type;		// 문서유형
	private String save_url;		// 파일형태가 아닌 문서의 보관위치 또는 URL
	private String where_from;		// 자료 출처
	private String writer;			// 작성자
	private String written_day;		// 작성일
	private String register;		// 등록자
	private String register_day;	// 등록일
	private String reference;		// 참조자료 정보
	private String ref_subject;		// 참조자료 제목
	private String ref_writer;		// 참조자료 저자
	private String ref_press_name;	// 참조자료 출판사
	private String ref_press_year;	// 참조자료 출판년도
	private String eco_no;			// ECO 번호
	private String copy_num;		// 건수

	private String file_link;		// 첨부파일 링크
	private String file_preview;	// 첨부파일 미리보기

	private String stat;			// 문서상태
	private String hit;				// 문서 열람회수

	private String category_id;		// 카테고리 번호

	private String writer_s;		// 검색을 위한 작성자 정보(A030003/박동렬/개발팀 등)
	private String register_s;		// 검색을 위한 등록자 정보

	private String aid;				// 전자결재 관리번호



	public String getTid(){
		return t_id;
	}
	public void setTid(String t_id){
		this.t_id = t_id;
	}

	public String getVerCode(){
		return ver_code;
	}
	public void setVerCode(String ver_code){
		this.ver_code = ver_code;
	}

	public String getAncestor(){
		return ancestor;
	}
	public void setAncestor(String ancestor){
		this.ancestor = ancestor;
	}

	public String getVerNo(){
		return ver_no;
	}
	public void setVerNo(String ver_no){
		this.ver_no = ver_no;
	}

	public String getSubject(){
		return subject;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}

	public String getDocNo(){
		return doc_no;
	}
	public void setDocNo(String doc_no){
		this.doc_no = doc_no;
	}

	public String getFileName(){
		return fname;
	}
	public void setFileName(String fname){
		this.fname = fname;
	}

	public String getFileSize(){
		return fsize;
	}
	public void setFileSize(String fsize){
		this.fsize = fsize;
	}

	public String getFileType(){
		return ftype;
	}
	public void setFileType(String ftype){
		this.ftype = ftype;
	}

	public String getModifyHistory(){
		return modify_history;
	}
	public void setModifyHistory(String modify_history){
		this.modify_history = modify_history;
	}

	public String getPreview(){
		return preview;
	}
	public void setPreview(String preview){
		this.preview = preview;
	}

	public String getWhyRevision(){
		return why_revision;
	}
	public void setWhyRevision(String why_revision){
		this.why_revision = why_revision;
	}

	public String getMemo(){
		return memo;
	}
	public void setMemo(String memo){
		this.memo = memo;
	}

	public String getSavePeriod(){
		return save_period;
	}
	public void setSavePeriod(String save_period){
		this.save_period = save_period;
	}

	public String getSecurityLevel(){
		return security_level;
	}
	public void setSecurityLevel(String security_level){
		this.security_level = security_level;
	}

	public String getWrittenLang(){
		return written_lang;
	}
	public void setWrittenLang(String written_lang){
		this.written_lang = written_lang;
	}

	public String getDocType(){
		return doc_type;
	}
	public void setDocType(String doc_type){
		this.doc_type = doc_type;
	}

	public String getSaveUrl(){
		return save_url;
	}
	public void setSaveUrl(String save_url){
		this.save_url = save_url;
	}

	public String getWhereFrom(){
		return where_from;
	}
	public void setWhereFrom(String where_from){
		this.where_from = where_from;
	}

	public String getWriter(){
		return writer;
	}
	public void setWriter(String writer){
		this.writer = writer;
	}

	public String getWrittenDay(){
		return written_day;
	}
	public void setWrittenDay(String written_day){
		this.written_day = written_day;
	}

	public String getRegister(){
		return register;
	}
	public void setRegister(String register){
		this.register = register;
	}

	public String getRegisterDay(){
		return register_day;
	}
	public void setRegisterDay(String register_day){
		this.register_day = register_day;
	}

	public String getReference(){
		return reference;
	}
	public void setReference(String reference){
		this.reference = reference;
	}

	public String getRefSubject(){
		return ref_subject;
	}
	public void setRefSubject(String ref_subject){
		this.ref_subject = ref_subject;
	}

	public String getRefWriter(){
		return ref_writer;
	}
	public void setRefWriter(String ref_writer){
		this.ref_writer = ref_writer;
	}

	public String getRefPressName(){
		return ref_press_name;
	}
	public void setRefPressName(String ref_press_name){
		this.ref_press_name = ref_press_name;
	}

	public String getRefPressYear(){
		return ref_press_year;
	}
	public void setRefPressYear(String ref_press_year){
		this.ref_press_year = ref_press_year;
	}

	public String getEcoNo(){
		return eco_no;
	}
	public void setEcoNo(String eco_no){
		this.eco_no = eco_no;
	}

	public String getCopyNum(){
		return copy_num;
	}
	public void setCopyNum(String copy_num){
		this.copy_num = copy_num;
	}

	public String getFileLink(){
		return file_link;
	}
	public void setFileLink(String file_link){
		this.file_link = file_link;
	}

	public String getFilePreview(){
		return file_preview;
	}
	public void setFilePreview(String file_preview){
		this.file_preview = file_preview;
	}

	public String getStat(){
		return stat;
	}
	public void setStat(String stat){
		this.stat = stat;
	}

	public String getHit(){
		return hit;
	}
	public void setHit(String hit){
		this.hit = hit;
	}

	public String getCategoryId(){
		return category_id;
	}
	public void setCategoryId(String category_id){
		this.category_id = category_id;
	}

	public String getWriterS(){
		return writer_s;
	}
	public void setWriterS(String writer_s){
		this.writer_s = writer_s;
	}

	public String getRegisterS(){
		return register_s;
	}
	public void setRegisterS(String register_s){
		this.register_s = register_s;
	}

	public String getAid(){
		return aid;
	}
	public void setAid(String aid){
		this.aid = aid;
	}
}

