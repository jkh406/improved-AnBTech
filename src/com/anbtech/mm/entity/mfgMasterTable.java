package com.anbtech.mm.entity;
public class mfgMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String mrp_no;						//MRP이력번호
	private String mfg_no;						//MFG 관리코드
	private String model_code;					//적용모델
	private String model_name;					//적용모델명
	private String fg_code;						//적용FG코드
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private String item_unit;					//단위
	private int mfg_count;						//제조오더수량
	private String buy_type;					//조달구분
	private String factory_no;					//공장번호
	private String factory_name;				//공장이름
	private String comp_code;					//회사코드
	private String comp_name;					//회사명
	private String comp_user;					//외주생산업체담당자
	private String comp_tel;					//외주생산업체연락처
	private String order_status;				//지시상태
	private String order_type;					//지시구분
	private String plan_date;					//MPS계획일자
	private String reg_date;					//등록일
	private String reg_id;						//등록자사번
	private String reg_name;					//등록자이름
	private String order_start_date;			//제조오더착수예정일
	private String order_end_date;				//제조오더착수완료일
	private String op_start_date;				//공정중 초공정 착수예정일
	private String op_end_date;					//공정중 말공정 완료예정일
	private String order_date;					//제조오더확정일
	private String re_work;						//재작업
	private String link_mfg_no;					//재작업시 전작업지시번호
	private int rst_total_count;				//실적수량
	private int rst_good_count;					//양품수량
	private int rst_bad_count;					//불량수량
	private int working_count;					//잔량
	private int rst_pass_count;					//품질합격수량
	private int rst_fail_count;					//품질불량수량
	private int rst_enter_count;				//창고로 입고된 수량
		
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

	//MRP이력번호
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}

	//MFG 관리코드
	public void setMfgNo(String mfg_no)				{ this.mfg_no = mfg_no;				}
	public String getMfgNo()						{ return this.mfg_no;				}

	//적용모델
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//적용모델명
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//적용FG코드
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//품목코드
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//품목이름
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//픔목규격
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//제조오더수량
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//조달구분
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장이름
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//회사코드
	public void setCompCode(String comp_code)		{ this.comp_code = comp_code;		}
	public String getCompCode()						{ return this.comp_code;			}

	//회사명
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}
	
	//외주생산업체담당자
	public void setCompUser(String comp_user)		{ this.comp_user = comp_user;		}
	public String getCompUser()						{ return this.comp_user;			}

	//외주생산업체연락처
	public void setCompTel(String comp_tel)			{ this.comp_tel = comp_tel;			}
	public String getCompTel()						{ return this.comp_tel;				}

	//지시상태
	public void setOrderStatus(String order_status)	{ this.order_status = order_status;	}
	public String getOrderStatus()					{ return this.order_status;			}

	//지시구분
	public void setOrderType(String order_type)		{ this.order_type = order_type;		}
	public String getOrderType()					{ return this.order_type;			}

	//MPS계획일자
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//등록일
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//등록자사번
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//등록자이름
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//제조오더착수예정일
	public void setOrderStartDate(String order_start_date)	{ this.order_start_date = order_start_date;	}
	public String getOrderStartDate()						{ return this.order_start_date;				}

	//제조오더착수완료일
	public void setOrderEndDate(String order_end_date)		{ this.order_end_date = order_end_date;		}
	public String getOrderEndDate()							{ return this.order_end_date;				}

	//공정중 초공정 착수예정일
	public void setOpStartDate(String op_start_date)		{ this.op_start_date = op_start_date;		}
	public String getOpStartDate()							{ return this.op_start_date;				}

	//공정중 말공정 완료예정일
	public void setOpEndDate(String op_end_date)			{ this.op_end_date = op_end_date;			}
	public String getOpEndDate()							{ return this.op_end_date;					}

	//제조오더확정일
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//재작업
	public void setReWork(String re_work)			{ this.re_work = re_work;			}
	public String getReWork()						{ return this.re_work;				}

	//재작업시 전작업지시번호
	public void setLinkMfgNo(String link_mfg_no)	{ this.link_mfg_no = link_mfg_no;	}
	public String getLinkMfgNo()					{ return this.link_mfg_no;			}


	//실적수량
	public void setRstTotalCount(int rst_total_count){ this.rst_total_count = rst_total_count;	}
	public int getRstTotalCount()					{ return this.rst_total_count;		}

	//양품수량
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//불량수량
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

	//잔량
	public void setWorkingCount(int working_count)	{ this.working_count = working_count;	}
	public int getWorkingCount()					{ return this.working_count;			}

	//품질합격수량
	public void setRstPassCount(int rst_pass_count)	{ this.rst_pass_count = rst_pass_count;	}
	public int getRstPassCount()					{ return this.rst_pass_count;			}

	//품질불량수량
	public void setRstFailCount(int rst_fail_count)	{ this.rst_fail_count = rst_fail_count;	}
	public int getRstFailCount()					{ return this.rst_fail_count;			}

	//창고로 입고된 수량
	public void setRstEnterCount(int rst_enter_count)	{ this.rst_enter_count = rst_enter_count;	}
	public int getRstEnterCount()						{ return this.rst_enter_count;			}

	//--------------- 관련 정보 ---------------------------//
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
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;				}
	public String getPageCut()						{ return this.page_cut;					}

	//총페이지
	public void setTotalPage(int total_page)		{ this.total_page = total_page;			}
	public int getTotalPage()						{ return this.total_page;				}

	//현재페이지
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;		}
	public int getCurrentPage()						{ return this.current_page;				}

	//총 항목수
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;	}
	public int getTotalArticle()					{ return this.total_article;			}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}



