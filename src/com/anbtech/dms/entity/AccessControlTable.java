/************************************************************
 * 문서 엑세스 권한 파악에 필요한 데이터 get/set
 ************************************************************/

package com.anbtech.dms.entity;

public class AccessControlTable{

	private String t_id;			// 관리번호.
	private String ver_code;		// 버젼코드
	private String ancestor;		// 최초버젼의 관리번호
	private String doc_no;			// 문서번호(회사에서 관리되는)
	private String save_period;		// 문서보존기간
	private String security_level;	// 문서 보안등급
	private String doc_type;		// 문서유형
	private String writer;			// 작성자
	private String written_day;		// 작성일
	private String register;		// 등록자
	private String register_day;	// 등록일
	private String stat;			// 문서상태
	private String category_id;		// 카테고리 번호

	public String getTid(){		return t_id;	}
	public void setTid(String t_id){		this.t_id = t_id;	}

	public String getVerCode(){		return ver_code;	}
	public void setVerCode(String ver_code){		this.ver_code = ver_code;	}

	public String getAncestor(){		return ancestor;	}
	public void setAncestor(String ancestor){		this.ancestor = ancestor;	}

	public String getDocNo(){		return doc_no;	}
	public void setDocNo(String doc_no){		this.doc_no = doc_no;	}

	public String getSavePeriod(){		return save_period;	}
	public void setSavePeriod(String save_period){		this.save_period = save_period;	}

	public String getSecurityLevel(){		return security_level;	}
	public void setSecurityLevel(String security_level){		this.security_level = security_level;	}

	public String getDocType(){		return doc_type;	}
	public void setDocType(String doc_type){		this.doc_type = doc_type;	}

	public String getWriter(){		return writer;	}
	public void setWriter(String writer){		this.writer = writer;	}

	public String getWrittenDay(){		return written_day;	}
	public void setWrittenDay(String written_day){		this.written_day = written_day;	}

	public String getRegister(){		return register;	}
	public void setRegister(String register){		this.register = register;	}

	public String getRegisterDay(){		return register_day;	}
	public void setRegisterDay(String register_day){		this.register_day = register_day;	}

	public String getStat(){		return stat;	}
	public void setStat(String stat){		this.stat = stat;	}

	public String getCategoryId(){		return category_id;	}
	public void setCategoryId(String category_id){		this.category_id = category_id;	}
}

