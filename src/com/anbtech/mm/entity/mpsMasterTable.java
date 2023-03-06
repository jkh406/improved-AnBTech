package com.anbtech.mm.entity;
public class mpsMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String mps_no;						//MPS이력번호
	private String order_no;					//수주번호
	private String mps_type;					//MPS생성구분
	private String model_code;					//적용모델
	private String model_name;					//적용모델명
	private String fg_code;						//적용FG코드
	private String item_code;					//품목번호
	private String item_name;					//품목명
	private String item_spec;					//품목규격
	private String plan_date;					//계획일자
	private int plan_count;						//계획수량
	private int sell_count;						//출고수량
	private String item_unit;					//단위
	private String mps_status;					//MPS진행상태
	private String factory_no;					//공장번호
	private String factory_name;				//공장이름
	private String reg_date;					//등록일
	private String reg_id;						//등록자사번
	private String reg_name;					//등록자이름
	private String app_date;					//승인일
	private String app_id;						//승인자 사번/이름
	private String app_no;						//승인번호
	private String order_comp;					//생산주문업체
	
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

	//수주번호
	public void setOrderNo(String order_no)			{ this.order_no = order_no;			}
	public String getOrderNo()						{ return this.order_no;				}

	//MPS생성구분
	public void setMpsType(String mps_type)			{ this.mps_type = mps_type;			}
	public String getMpsType()						{ return this.mps_type;				}

	//적용모델
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//적용모델명
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//적용FG코드
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//품목번호
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//품목명
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//품목규격
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//계획일자
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//계획수량
	public void setPlanCount(int plan_count)		{ this.plan_count = plan_count;		}
	public int getPlanCount()						{ return this.plan_count;			}

	//출고수량
	public void setSellCount(int sell_count)		{ this.sell_count = sell_count;		}
	public int getSellCount()						{ return this.sell_count;			}

	//단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//MPS진행상태
	public void setMpsStatus(String mps_status)		{ this.mps_status = mps_status;		}
	public String getMpsStatus()					{ return this.mps_status;			}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장이름
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//등록일
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//등록자사번
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//등록자이름
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//승인일
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//승인자 사번/이름
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//승인번호
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//생산주문업체
	public void setOrderComp(String order_comp)		{ this.order_comp = order_comp;		}
	public String getOrderComp()					{ return this.order_comp;			}
		
	
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


