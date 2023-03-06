package com.anbtech.mm.entity;
public class mfgStockItem
{
	//Table 구성
	private String pid;							//관리코드
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private String item_unit;					//품목단위
	private int stock_count;					//제조잔고누적수량
	private int add_count;						//제조잔고 조정수량
	private String add_date;					//조정일자
	private String add_mgr;						//조정자
	private String factory_no;					//공장번호
	private String factory_name;				//공장이름
	
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

	//품목코드
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//품목이름
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//픔목규격
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//품목단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}
	
	//제조잔고누적수량
	public void setStockCount(int stock_count)		{ this.stock_count = stock_count;	}
	public int getStockCount()						{ return this.stock_count;			}

	//제조잔고 조정수량
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//조정일자
	public void setAddDate(String add_date)			{ this.add_date = add_date;			}
	public String getAddDate()						{ return this.add_date;				}

	//조정자
	public void setAddMgr(String add_mgr)			{ this.add_mgr = add_mgr;			}
	public String getAddMgr()						{ return this.add_mgr;				}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장이름
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

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




