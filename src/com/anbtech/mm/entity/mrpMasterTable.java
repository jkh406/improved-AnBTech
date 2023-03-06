package com.anbtech.mm.entity;
public class mrpMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String mps_no;						//MPS이력번호
	private String mrp_no;						//MRP이력번호
	private String mrp_start_date;				//기준일자
	private String mrp_end_date;				//확정전개일자
	private String model_code;					//적용모델
	private String model_name;					//적용모델명
	private String fg_code;						//적용FG코드
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private int p_count;						//대상제품수량
	private String plan_date;					//MPS계획일자
	private String item_unit;					//픔목단위
	private String mrp_status;					//MRP진행상태
	private String factory_no;					//공장번호
	private String factory_name;				//공장명
	private String reg_date;					//등록일
	private String app_date;					//승인일
	private String app_id;						//승인자 사번/이름
	private String reg_div_code;				//등록부서코드
	private String reg_div_name;				//등록부서명
	private String reg_id;						//등록자사번
	private String reg_name;					//등록자이름
	private String app_no;						//결재승인 관리번호
	private String pu_dev_date;					//구매입고희망일
	private String pu_req_no;					//구매요청번호
	private String stock_link;					//재고수량 재고여부
	private String mfg_order;					//제조오더생성여부
	private String pjt_code;					//과제코드
	private String pjt_name;					//과제이름
		
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

	//MPS이력번호
	public void setMpsNo(String mps_no)				{ this.mps_no = mps_no;				}
	public String getMpsNo()						{ return this.mps_no;				}

	//MRP이력번호
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}

	//기준일자
	public void setMrpStartDate(String mrp_start_date)		{ this.mrp_start_date = mrp_start_date;		}
	public String getMrpStartDate()							{ return this.mrp_start_date;				}

	//확정전개일자
	public void setMrpEndDate(String mrp_end_date)	{ this.mrp_end_date = mrp_end_date;	}
	public String getMrpEndDate()					{ return this.mrp_end_date;			}

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

	//대상제품수량
	public void setPCount(int p_count)				{ this.p_count = p_count;			}
	public int getPCount()							{ return this.p_count;				}

	//MPS계획일자
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//픔목단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//MRP진행상태
	public void setMrpStatus(String mrp_status)		{ this.mrp_status = mrp_status;		}
	public String getMrpStatus()					{ return this.mrp_status;			}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장명
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//등록일
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//승인일
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//승인자 사번/이름
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//등록부서코드
	public void setRegDivCode(String reg_div_code)	{ this.reg_div_code = reg_div_code;	}
	public String getRegDivCode()					{ return this.reg_div_code;			}

	//등록부서명
	public void setRegDivName(String reg_div_name)	{ this.reg_div_name = reg_div_name;	}
	public String getRegDivName()					{ return this.reg_div_name;			}

	//등록자사번
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//등록자이름
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//결재승인 관리번호
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//구매입고희망일
	public void setPuDevDate(String pu_dev_date)	{ this.pu_dev_date = pu_dev_date;	}
	public String getPuDevDate()					{ return this.pu_dev_date;			}

	//구매요청번호
	public void setPuReqNo(String pu_req_no)		{ this.pu_req_no = pu_req_no;		}
	public String getPuReqNo()						{ return this.pu_req_no;			}
	
	//재고수량 재고여부
	public void setStockLink(String stock_link)		{ this.stock_link = stock_link;		}
	public String getStockLink()					{ return this.stock_link;			}

	//제조오더생성여부
	public void setMfgOrder(String mfg_order)		{ this.mfg_order = mfg_order;		}
	public String getMfgOrder()						{ return this.mfg_order;			}

	//과제코드
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//과제이름
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}
	
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


