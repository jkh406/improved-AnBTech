package com.anbtech.dcm.entity;
public class eccComTable
{
	//ECC_COM 공통항목 
	private String pid;							//관리코드
	private String ecc_subject;					//제목
	private String eco_no;						//변경번호
	private String ecr_id;						//발의자사번
	private String ecr_name;					//발의자이름
	private String ecr_code;					//발의자부서관리코드
	private String ecr_div_code;				//발의자부서코드
	private String ecr_div_name;				//발의자부서명
	private String ecr_tel;						//발의자전화번호
	private String ecr_date;					//발의일자
	private String mgr_id;						//기술검토책임자사번
	private String mgr_name;					//기술검토책임자이름
	private String mgr_code;					//기술검토책임자부서관리코드
	private String mgr_div_code;				//기술검토책임자부서코드
	private String mgr_div_name;				//기술검토책임자부서이름
	private String eco_id;						//기술검토담당자사번
	private String eco_name;					//기술검토담당자이름
	private String eco_code;					//기술검토담당자부서관리코드
	private String eco_div_code;				//기술검토담당자부서코드
	private String eco_div_name;				//기술검토담당자부서이름
	private String eco_tel;						//기술검토담당자전화번호
	private String ecc_reason;					//변경이유
	private String ecc_factor;					//적용구분
	private String ecc_scope;					//적용범위
	private String ecc_kind;					//업무구분
	private String pdg_code;					//제품군
	private String pd_code;						//제품
	private String fg_code;						//발생모델(FG)코드
	private String part_code;					//발생부품코드
	private String order_date;					//적용일자
	private String fix_date;					//확정일자
	private String ecc_status;					//진행상태

	//event
	private String modify;						//수정
	private String delete;						//삭제
	private String view;						//상세보기
	
	//페이지 표현하기
	private String page_cut;					//숫자로 List표현하기
	private int total_page;						//총페이지
	private int current_page;					//현재페이지
	private int total_article;					//총 항목수

	//검색항목
	private String sItem;						//Item
	private String sWord;						//Word
	
	/*--------------------------- 메소드 만들기 -------------------------------*/
	//관리코드
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//설계변경제목
	public void setEccSubject(String ecc_subject)	{ this.ecc_subject = ecc_subject;	}
	public String getEccSubject()					{ return this.ecc_subject;			}

	//ECO NO
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}

	//발의자 사번
	public void setEcrId(String ecr_id)				{ this.ecr_id = ecr_id;				}
	public String getEcrId()						{ return this.ecr_id;				}

	//발의자 이름
	public void setEcrName(String ecr_name)			{ this.ecr_name = ecr_name;			}
	public String getEcrName()						{ return this.ecr_name;				}

	//발의자 부서관리코드
	public void setEcrCode(String ecr_code)			{ this.ecr_code = ecr_code;			}
	public String getEcrCode()						{ return this.ecr_code;				}

	//발의자 부서코드
	public void setEcrDivCode(String ecr_div_code)	{ this.ecr_div_code = ecr_div_code;	}
	public String getEcrDivCode()					{ return this.ecr_div_code;			}

	//발의자 부서명
	public void setEcrDivName(String ecr_div_name)	{ this.ecr_div_name = ecr_div_name;	}
	public String getEcrDivName()					{ return this.ecr_div_name;			}

	//발의자 전화번호
	public void setEcrTel(String ecr_tel)			{ this.ecr_tel = ecr_tel;			}
	public String getEcrTel()						{ return this.ecr_tel;				}

	//발의자 발의일자
	public void setEcrDate(String ecr_date)			{ this.ecr_date = ecr_date;			}
	public String getEcrDate()						{ return this.ecr_date;				}

	//기술검토책임자사번
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//기술검토책임자이름
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//기술검토책임자부서관리코드
	public void setMgrCode(String mgr_code)			{ this.mgr_code = mgr_code;			}
	public String getMgrCode()						{ return this.mgr_code;				}

	//기술검토책임자부서코드
	public void setMgrDivCode(String mgr_div_code)	{ this.mgr_div_code = mgr_div_code;	}
	public String getMgrDivCode()					{ return this.mgr_div_code;			}

	//기술검토책임자부서이름
	public void setMgrDivName(String mgr_div_name)	{ this.mgr_div_name = mgr_div_name;	}
	public String getMgrDivName()					{ return this.mgr_div_name;			}

	//기술검토담당자 사번
	public void setEcoId(String eco_id)				{ this.eco_id = eco_id;				}
	public String getEcoId()						{ return this.eco_id;				}

	//기술검토담당자 이름
	public void setEcoName(String eco_name)			{ this.eco_name = eco_name;			}
	public String getEcoName()						{ return this.eco_name;				}

	//기술검토담당자 부서관리코드
	public void setEcoCode(String eco_code)			{ this.eco_code = eco_code;			}
	public String getEcoCode()						{ return this.eco_code;				}

	//기술검토담당자 부서코드
	public void setEcoDivCode(String eco_div_code)	{ this.eco_div_code = eco_div_code;	}
	public String getEcoDivCode()					{ return this.eco_div_code;			}

	//기술검토담당자 부서명
	public void setEcoDivName(String eco_div_name)	{ this.eco_div_name = eco_div_name;	}
	public String getEcoDivName()					{ return this.eco_div_name;			}

	//기술검토담당자 부서전화번호
	public void setEcoTel(String eco_tel)			{ this.eco_tel = eco_tel;			}
	public String getEcoTel()						{ return this.eco_tel;				}

	//변경이유
	public void setEccReason(String ecc_reason)		{ this.ecc_reason = ecc_reason;		}
	public String getEccReason()					{ return this.ecc_reason;			}

	//적용구분
	public void setEccFactor(String ecc_factor)		{ this.ecc_factor = ecc_factor;		}
	public String getEccFactor()					{ return this.ecc_factor;			}

	//적용범위
	public void setEccScope(String ecc_scope)		{ this.ecc_scope = ecc_scope;		}
	public String getEccScope()						{ return this.ecc_scope;			}

	//업무구분
	public void setEccKind(String ecc_kind)			{ this.ecc_kind = ecc_kind;			}
	public String getEccKind()						{ return this.ecc_kind;				}

	//제품군
	public void setPdgCode(String pdg_code)			{ this.pdg_code = pdg_code;			}
	public String getPdgCode()						{ return this.pdg_code;				}

	//제품
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;			}
	public String getPdCode()						{ return this.pd_code;				}

	//발생모델(FG)코드
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//발생부품코드
	public void setPartCode(String part_code)		{ this.part_code = part_code;		}
	public String getPartCode()						{ return this.part_code;			}

	//적용일자
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//확정일자
	public void setFixDate(String fix_date)			{ this.fix_date = fix_date;			}
	public String getFixDate()						{ return this.fix_date;				}

	//진행상태
	public void setEccStatus(String ecc_status)		{ this.ecc_status = ecc_status;		}
	public String getEccStatus()					{ return this.ecc_status;			}

    //--------------------------  Modify 관련 ------------------------------------------//
	//수정
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//삭제
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//상세보기
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//숫자로 List표현하기
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;			}
	public String getPageCut()						{ return this.page_cut;				}

	//총페이지
	public void setTotalPage(int total_page)		{ this.total_page = total_page;		}
	public int getTotalPage()						{ return this.total_page;			}

	//현재페이지
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;	}
	public int getCurrentPage()						{ return this.current_page;			}

	//총 항목수
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;}
	public int getTotalArticle()					{ return this.total_article;		}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}


