package com.anbtech.psm.entity;
public class psmMasterTable
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
	private String psm_desc;					//과제개요
	private String plan_sum;					//과제계획총계
	private String plan_labor;					//계획인건비
	private String plan_material;				//계획재료비
	private String plan_cost;					//계획비용
	private String plan_plant;					//계획시설비

	private String result_sum;					//실적총계
	private String result_labor;				//실적인건비
	private String result_material;				//실적재료비
	private String result_cost;					//실적비용
	private String result_plant;				//실적시설비

	private String diff_sum;					//총계 차액
	private String diff_labor;					//인건비 차액
	private String diff_material;				//재료비 차액
	private String diff_cost;					//비용 차액
	private String diff_plant;					//시설비 차액

	private String contract_date;				//계약일
	private String contract_name;				//계약명
	private String contract_price;				//계약금액
	private String complete_date;				//준공일
	private String fname;						//파일원래명
	private String sname;						//파일저장명
	private String ftype;						//파일타입
	private String fsize;						//파일크기
	private String psm_status;					//진행상태
	private String reg_date;					//등록(수정)일
	private String app_date;					//확정일

	private String pd_code;						//제품코드
	private String pd_name;						//제품명
	private String psm_kind;					//과제구분
	private String psm_view;					//과제조회여부
	private String link_code;					//서브과제링크아디 
	
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
	public void setPsmDesc(String psm_desc)			{ this.psm_desc = psm_desc;		}
	public String getPsmDesc()						{ return this.psm_desc;			}

	//과제계획총계
	public void setPlanSum(String plan_sum)			{ this.plan_sum = plan_sum;		}
	public String getPlanSum()						{ return this.plan_sum;			}

	//계획인건비
	public void setPlanLabor(String plan_labor)		{ this.plan_labor = plan_labor;	}
	public String getPlanLabor()					{ return this.plan_labor;		}

	//계획재료비
	public void setPlanMaterial(String plan_material)	{ this.plan_material = plan_material;	}
	public String getPlanMaterial()						{ return this.plan_material;			}

	//계획비용
	public void setPlanCost(String plan_cost)			{ this.plan_cost = plan_cost;			}
	public String getPlanCost()							{ return this.plan_cost;				}

	//계획시설비
	public void setPlanPlant(String plan_plant)			{ this.plan_plant = plan_plant;			}
	public String getPlanPlant()						{ return this.plan_plant;				}

	//실적총계
	public void setResultSum(String result_sum)			{ this.result_sum = result_sum;			}
	public String getResultSum()						{ return this.result_sum;				}

	//실적인건비
	public void setResultLabor(String result_labor)		{ this.result_labor = result_labor;		}
	public String getResultLabor()						{ return this.result_labor;				}

	//실적재료비
	public void setResultMaterial(String result_material)	{ this.result_material = result_material;	}
	public String getResultMaterial()						{ return this.result_material;				}

	//실적비용
	public void setResultCost(String result_cost)			{ this.result_cost = result_cost;			}
	public String getResultCost()							{ return this.result_cost;					}

	//실적시설비
	public void setResultPlant(String result_plant)			{ this.result_plant = result_plant;			}
	public String getResultPlant()							{ return this.result_plant;					}

	//차액 총계
	public void setDiffSum(String diff_sum)				{ this.diff_sum = diff_sum;				}
	public String getDiffSum()							{ return this.diff_sum;					}

	//차액 인건비
	public void setDiffLabor(String diff_labor)			{ this.diff_labor = diff_labor;			}
	public String getDiffLabor()						{ return this.diff_labor;				}

	//차액 재료비
	public void setDiffMaterial(String diff_material)	{ this.diff_material = diff_material;	}
	public String getDiffMaterial()						{ return this.diff_material;			}

	//차액 비용
	public void setDiffCost(String diff_cost)			{ this.diff_cost = diff_cost;			}
	public String getDiffCost()							{ return this.diff_cost;				}

	//차액 시설비
	public void setDiffPlant(String diff_plant)			{ this.diff_plant = diff_plant;			}
	public String getDiffPlant()						{ return this.diff_plant;				}

	//계약일
	public void setContractDate(String contract_date)	{ this.contract_date = contract_date;		}
	public String getContractDate()						{ return this.contract_date;				}

	//계약명
	public void setContractName(String contract_name)	{ this.contract_name = contract_name;		}
	public String getContractName()						{ return this.contract_name;				}

	//계약금액
	public void setContractPrice(String contract_price)	{ this.contract_price = contract_price;		}
	public String getContractPrice()					{ return this.contract_price;				}

	//준공일
	public void setCompleteDate(String complete_date)	{ this.complete_date = complete_date;		}
	public String getCompleteDate()						{ return this.complete_date;				}

	//파일원래명
	public void setFname(String fname)				{ this.fname = fname;			}
	public String getFname()						{ return this.fname;			}

	//파일저장명
	public void setSname(String sname)				{ this.sname = sname;			}
	public String getSname()						{ return this.sname;			}

	//파일타입
	public void setFtype(String ftype)				{ this.ftype = ftype;			}
	public String getFtype()						{ return this.ftype;			}

	//파일크기
	public void setFsize(String fsize)				{ this.fsize = fsize;			}
	public String getFsize()						{ return this.fsize;			}

	//진행상태
	public void setPsmStatus(String psm_status)		{ this.psm_status = psm_status;		}
	public String getPsmStatus()					{ return this.psm_status;			}

	//등록(수정)일
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;		}
	public String getRegDate()						{ return this.reg_date;			}

	//확정일
	public void setAppDate(String app_date)			{ this.app_date = app_date;		}
	public String getAppDate()						{ return this.app_date;			}

	//제품코드
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;		}
	public String getPdCode()						{ return this.pd_code;			}

	//제품명
	public void setPdName(String pd_name)			{ this.pd_name = pd_name;		}
	public String getPdName()						{ return this.pd_name;			}

	//과제구분
	public void setPsmKind(String psm_kind)			{ this.psm_kind = psm_kind;		}
	public String getPsmKind()						{ return this.psm_kind;			}

	//과제조회여부
	public void setPsmView(String psm_view)			{ this.psm_view = psm_view;		}
	public String getPsmView()						{ return this.psm_view;			}

	//서브과제링크아디 
	public void setLinkCode(String link_code)		{ this.link_code = link_code;	}
	public String getLinkCode()						{ return this.link_code;		}


	
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




