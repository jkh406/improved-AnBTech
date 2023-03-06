package com.anbtech.gm.entity;

public class GoodsInfoTable {
	private String mid;					//관리번호
	private String code;				//제품(모델)코드
	private String name;				//제품(모델)명
	private String name2;				//제품(모델)명2
	private String short_name;			//모델약어
	private String color_code;			//모델색상코드
	private String color_name;			//모델색상
	private String other_info;			//기타 정보
	private String fg_code;				//F/G코드

	private String glevel;				//분류단계
	private String ancestor;			//상위분류관리번호
	private String gcode;				//내부코드
	
	private String register_id;			//등록자사번
	private String register_info;		//등록자정보
	private String register_date;		//등록일자
	private String modifier_id;			//수정자사번
	private String modifier_info;		//수정자정보
	private String modify_date;			//수정일자
	private String aid;					//전자결재관리번호
	private String stat;				//상태코드
	
	private String one_class;			//제품군코드
	private String two_class;			//제품코드
	private String three_class;			//모델군코드

	private String fname;				//파일명
	private String ftype;				//파일타입
	private String fsize;				//파일사이즈
	private String umask;				//파일저장이름

	//모델코드생성에 필요한 정보 담을 변수
	private String two_class_code;		//제품코드
	private String revision_code;		//기능코드
	private String derive_code;			//파생코드

	
	//제품정보
	public String getMid(){		return this.mid;	}
	public void setMid(String mid){		this.mid = mid;	}

	public String getGoodsCode(){		return this.code;	}
	public void setGoodsCode(String code){		this.code = code;	}

	public String getGoodsName(){		return this.name;	}
	public void setGoodsName(String name){		this.name = name;	}

	public String getGoodsName2(){		return this.name2;	}
	public void setGoodsName2(String name2){		this.name2 = name2;	}

	public String getShortName(){		return this.short_name;	}
	public void setShortName(String short_name){		this.short_name = short_name;	}

	public String getColorCode(){		return this.color_code;	}
	public void setColorCode(String color_code){		this.color_code = color_code;	}

	public String getColorName(){		return this.color_name;	}
	public void setColorName(String color_name){		this.color_name = color_name;	}

	public String getOtherInfo() {		return other_info;	}
	public void setOtherInfo(String string) {		other_info = string;	}

	public String getFgCode() {		return fg_code;	}
	public void setFgCode(String string) {		fg_code = string;	}

	//트리정보
	public String getGoodsLevel(){		return this.glevel;	}
	public void setGoodsLevel(String glevel){		this.glevel = glevel;	}

	public String getAncestor(){		return this.ancestor;	}
	public void setAncestor(String ancestor){		this.ancestor = ancestor;	}

	public String getGcode(){		return this.gcode;	}
	public void setGcode(String gcode){		this.gcode = gcode;	}

	//등록정보
	public String getRegisterId(){		return this.register_id;	}
	public void setRegisterId(String register_id){		this.register_id = register_id;	}

	public String getRegisterInfo() {		return this.register_info;	}
	public void setRegisterInfo(String register_info){		this.register_info = register_info;	}

	public String getRegisterDate(){		return this.register_date;	}
	public void setRegisterDate(String register_date){		this.register_date = register_date;	}

	public String getModifierId(){		return this.modifier_id;	}
	public void setModifierId(String modifier_id){		this.modifier_id = modifier_id;	}

	public String getModifierInfo() {		return this.modifier_info;	}
	public void setModifierInfo(String modifier_info){		this.modifier_info = modifier_info;	}

	public String getModifyDate(){		return this.modify_date;	}
	public void setModifyDate(String modify_date){		this.modify_date = modify_date;	}

	public String getAid(){		return this.aid;	}
	public void setAid(String aid){		this.aid = aid;	}

	public String getStat() {		return stat;	}
	public void setStat(String string) {		stat = string;	}

	//분류정보
	public String getOneClass(){		return this.one_class;	}
	public void setOneClass(String one_class){		this.one_class = one_class;	}

	public String getTwoClass(){		return this.two_class;	}
	public void setTwoClass(String two_class){		this.two_class = two_class;	}

	public String getThreeClass(){		return this.three_class;	}
	public void setThreeClass(String three_class){		this.three_class = three_class;	}
	
	//첨부파일정보
	public String getFileName() {		return fname;	}
	public void setFileName(String string) {		fname = string;	}

	public String getFileSize() {		return fsize;	}
	public void setFileSize(String string) {		fsize = string;	}

	public String getFileType() {		return ftype;	}
	public void setFileType(String string) {		ftype = string;	}

	public String getUmask() {		return umask;	}
	public void setUmask(String string) {		umask = string;	}

	//모델코드생성에 필요한 정보 담을 변수
	public String getTwoClassCode(){		return this.two_class_code;	}
	public void setTwoClassCode(String two_class_code){		this.two_class_code = two_class_code;	}

	public String getRevisionCode(){		return this.revision_code;	}
	public void setRevisionCode(String revision_code){		this.revision_code = revision_code;	}

	public String getDeriveCode(){		return this.derive_code;	}
	public void setDeriveCode(String derive_code){		this.derive_code = derive_code;	}
}