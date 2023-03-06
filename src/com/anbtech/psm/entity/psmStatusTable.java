package com.anbtech.psm.entity;
public class psmStatusTable
{
	//Table 구성
	private String pid;							//관리코드
	private String psm_code;					//과제코드
	private String psm_type;					//과제종류
	private String comp_name;					//과제고객명
	private String comp_category;				//과제카테고리명
	private String psm_korea;					//과제명한글
	private String psm_english;					//과제명영문
	private String psm_start_date;				//과제시작일
	private String psm_end_date;				//과제종료일
	private String psm_pm;						//과제PM
	private String psm_pm_div;					//과제PM 부서코드
	private String psm_mgr;						//과제담당자
	private String psm_mgr_div;					//과제담당자 부서코드
	private String psm_budget;					//과제예산담당자
	private String psm_budget_div;				//과제예산담당자 부서코드
	private String psm_user;					//과제등록자
	private String psm_user_div;				//과제등록자 부서코드
	private String change_desc;					//과제개요
	private String psm_status;					//진행상태
	private String change_date;					//등록(수정)일
	
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

	//과제코드
	public void setPsmCode(String psm_code)			{ this.psm_code = psm_code;			}
	public String getPsmCode()						{ return this.psm_code;				}

	//과제종류
	public void setPsmType(String psm_type)			{ this.psm_type = psm_type;			}
	public String getPsmType()						{ return this.psm_type;				}

	//과제고객명
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}

	//과제카테고리명
	public void setCompCategory(String comp_category)	{ this.comp_category = comp_category;	}
	public String getCompCategory()						{ return this.comp_category;			}

	//과제명한글
	public void setPsmKorea(String psm_korea)		{ this.psm_korea = psm_korea;		}
	public String getPsmKorea()						{ return this.psm_korea;			}

	//과제명영문
	public void setPsmEnglish(String psm_english)	{ this.psm_english = psm_english;	}
	public String getPsmEnglish()					{ return this.psm_english;			}

	//과제시작일
	public void setPsmStartDate(String psm_start_date)	{ this.psm_start_date = psm_start_date;	}
	public String getPsmStartDate()						{ return this.psm_start_date;			}

	//과제종료일
	public void setPsmEndDate(String psm_end_date)		{ this.psm_end_date = psm_end_date;		}
	public String getPsmEndDate()						{ return this.psm_end_date;				}

	//과제PM
	public void setPsmPm(String psm_pm)				{ this.psm_pm = psm_pm;			}
	public String getPsmPm()						{ return this.psm_pm;			}

	//과제PM 부서코드
	public void setPsmPmDiv(String psm_pm_div)		{ this.psm_pm_div = psm_pm_div;		}
	public String getPsmPmDiv()						{ return this.psm_pm_div;			}

	//과제담당자
	public void setPsmMgr(String psm_mgr)			{ this.psm_mgr = psm_mgr;			}
	public String getPsmMgr()						{ return this.psm_mgr;				}

	//과제담당자 부서코드
	public void setPsmMgrDiv(String psm_mgr_div)	{ this.psm_mgr_div = psm_mgr_div;	}
	public String getPsmMgrDiv()					{ return this.psm_mgr_div;			}

	//과제예산담당자
	public void setPsmBudget(String psm_budget)		{ this.psm_budget = psm_budget;		}
	public String getPsmBudget()					{ return this.psm_budget;			}

	//과제예산담당자 부서코드
	public void setPsmBudgetDiv(String psm_budget_div)	{ this.psm_budget_div = psm_budget_div;	}
	public String getPsmBudgetDiv()						{ return this.psm_budget_div;			}

	//과제등록자
	public void setPsmUser(String psm_user)			{ this.psm_user = psm_user;		}
	public String getPsmUser()						{ return this.psm_user;			}

	//과제등록자 부서코드
	public void setPsmUserDiv(String psm_user_div)	{ this.psm_user_div = psm_user_div;	}
	public String getPsmUserDiv()					{ return this.psm_user_div;			}

	//과제개요
	public void setChangeDesc(String change_desc)	{ this.change_desc = change_desc;	}
	public String getChangeDesc()					{ return this.change_desc;			}

	//진행상태
	public void setPsmStatus(String psm_status)		{ this.psm_status = psm_status;		}
	public String getPsmStatus()					{ return this.psm_status;			}

	//등록(수정)일
	public void setChangeDate(String change_date)	{ this.change_date = change_date;	}
	public String getChangeDate()					{ return this.change_date;			}
	
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





