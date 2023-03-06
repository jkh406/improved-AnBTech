package com.anbtech.mm.entity;
public class mrpItemTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mrp_no;						//MRP 이력번호
	private String assy_code;					//ASSY CODE
	private int level_no;						//레벨번호
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private String item_type;					//item type
	private int draw_count;						//도면상의 수량
	private int mrp_count;						//MRP 수량
	private int need_count;						//실질필요수량
	private int stock_count;					//현재고 수량
	private int open_count;						//입고 예정수량
	private int plan_count;						//가용수량
	private int add_count;						//추가수량
	private int mrs_count;						//구매수량
	private String item_unit;					//단위
	private String buy_type;					//조달구분
	private String factory_no;					//공장번호
	private String factory_name;				//공장이름
	private String pu_dev_date;					//구매입고희망일
	private String pu_req_no;					//구매요청번호
		
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

	//Group 관리코드
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//MRP 이력번호
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}
	
	//ASSY CODE
	public void setAssyCode(String assy_code)		{ this.assy_code = assy_code;		}
	public String getAssyCode()						{ return this.assy_code;			}

	//레벨번호
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//품목코드
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//품목이름
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//픔목규격
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//item type
	public void setItemType(String item_type)		{ this.item_type = item_type;		}
	public String getItemType()						{ return this.item_type;			}

	//도면상의 수량
	public void setDrawCount(int draw_count)		{ this.draw_count = draw_count;		}
	public int getDrawCount()						{ return this.draw_count;			}

	//MRP 수량
	public void setMrpCount(int mrp_count)			{ this.mrp_count = mrp_count;		}
	public int getMrpCount()						{ return this.mrp_count;			}

	//실질필요수량
	public void setNeedCount(int need_count)			{ this.need_count = need_count;		}
	public int getNeedCount()						{ return this.need_count;			}

	//현재고 수량
	public void setStockCount(int stock_count)		{ this.stock_count = stock_count;	}
	public int getStockCount()						{ return this.stock_count;			}

	//입고 예정수량
	public void setOpenCount(int open_count)		{ this.open_count = open_count;		}
	public int getOpenCount()						{ return this.open_count;			}

	//가용수량
	public void setPlanCount(int plan_count)		{ this.plan_count = plan_count;		}
	public int getPlanCount()						{ return this.plan_count;			}

	//추가수량
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//구매수량
	public void setMrsCount(int mrs_count)			{ this.mrs_count = mrs_count;		}
	public int getMrsCount()						{ return this.mrs_count;			}

	//단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//조달구분
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장이름
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//구매입고희망일
	public void setPuDevDate(String pu_dev_date)	{ this.pu_dev_date = pu_dev_date;	}
	public String getPuDevDate()					{ return this.pu_dev_date;			}

	//구매요청번호
	public void setPuReqNo(String pu_req_no)		{ this.pu_req_no = pu_req_no;		}
	public String getPuReqNo()						{ return this.pu_req_no;			}

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



