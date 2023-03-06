package com.anbtech.mm.entity;
public class mfgItemTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String assy_code;					//ASSY CODE
	private int level_no;						//레벨번호
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private String item_unit;					//품목단위
	private String item_type;					//item type
	private double item_loss;					//품목Loss율
	private int draw_count;						//도면상의 수량
	private int mfg_count;						//제조수량
	private int need_count;						//도면상의 수량 * 제조수량
	private int spare_count;					//생산부서의 잔고수량
	private int add_count;						//소요량조정 수량
	private int reserve_count;					//출고예약수량
	private int request_count;					//실제 출고의뢰한 수량 ( <= 예약수량)
	private String need_date;					//필요일
	private String order_date;					//오더확정일자
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

	//Group 관리코드
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//MFG 관리코드
	public void setMfgNo(String mfg_no)				{ this.mfg_no = mfg_no;				}
	public String getMfgNo()						{ return this.mfg_no;				}

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

	//품목Loss율
	public void setItemLoss(double item_loss)		{ this.item_loss = item_loss;		}
	public double getItemLoss()						{ return this.item_loss;			}

	//도면상의 수량
	public void setDrawCount(int draw_count)		{ this.draw_count = draw_count;		}
	public int getDrawCount()						{ return this.draw_count;			}

	//제조수량
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//품목단위
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//도면상의 수량 * 제조수량
	public void setNeedCount(int need_count)		{ this.need_count = need_count;		}
	public int getNeedCount()						{ return this.need_count;			}

	//생산부서의 잔고수량
	public void setSpareCount(int spare_count)		{ this.spare_count = spare_count;	}
	public int getSpareCount()						{ return this.spare_count;			}

	//소요량조정 수량
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//출고예약수량
	public void setReserveCount(int reserve_count)	{ this.reserve_count = reserve_count;	}
	public int getReserveCount()					{ return this.reserve_count;			}

	//실제 출고의뢰한 수량 ( <= 예약수량)
	public void setRequestCount(int request_count)	{ this.request_count = request_count;	}
	public int getRequestCount()					{ return this.request_count;			}

	//필요일
	public void setNeedDate(String need_date)		{ this.need_date = need_date;		}
	public String getNeedDate()						{ return this.need_date;			}

	//오더확정일자
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

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



