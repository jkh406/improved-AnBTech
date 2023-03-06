package com.anbtech.mm.entity;
public class mfgProductMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String model_code;					//적용모델
	private String model_name;					//적용모델명
	private String fg_code;						//적용FG코드
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private int order_count;					//오더수량
	private String order_unit;					//오더단위
	private int total_count;					//총생산수량
	private int good_count;						//양품수량
	private int bad_count;						//불량수량
	private String mfg_id;						//등록자사번
	private String mfg_name;					//등록자이름
	private String output_status;				//진행상태
	private String factory_no;					//공장번호
	private String output_date;					//실적등록일
	private String op_code;						//공정코드
	private String op_name;						//공정명
	private String op_nickname;					//공정명 약어

	private String product_rate;				//생산실적율
	private String good_rate;					//양품율
	private String bad_rate;					//불량율

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

	//오더수량
	public void setOrderCount(int order_count)		{ this.order_count = order_count;	}
	public int getOrderCount()						{ return this.order_count;			}

	//오더단위
	public void setOrderUnit(String order_unit)		{ this.order_unit = order_unit;		}
	public String getOrderUnit()					{ return this.order_unit;			}

	//실적수량
	public void setTotalCount(int total_count)		{ this.total_count = total_count;	}
	public int getTotalCount()						{ return this.total_count;			}

	//양품수량
	public void setGoodCount(int good_count)		{ this.good_count = good_count;		}
	public int getGoodCount()						{ return this.good_count;			}

	//불량수량
	public void setBadCount(int bad_count)			{ this.bad_count = bad_count;		}
	public int getBadCount()						{ return this.bad_count;			}

	//등록자사번
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//등록자이름
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//진행상태
	public void setOutputStatus(String output_status)	{ this.output_status = output_status;		}
	public String getOutputStatus()						{ return this.output_status;				}

	//공장번호
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//실적등록일
	public void setOutputDate(String output_date)	{ this.output_date = output_date;	}
	public String getOutputDate()					{ return this.output_date;			}

	//공정코드
	public void setOpCode(String op_code)			{ this.op_code = op_code;			}
	public String getOpCode()						{ return this.op_code;				}

	//공정명
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

	//공정명 약어
	public void setOpNickname(String op_nickname)	{ this.op_nickname = op_nickname;	}
	public String getOpNickname()					{ return this.op_nickname;			}

	//생산실적율
	public void setProductRate(String product_rate)	{ this.product_rate = product_rate;	}
	public String getProductRate()					{ return this.product_rate;			}

	//양품율
	public void setGoodRate(String good_rate)		{ this.good_rate = good_rate;		}
	public String getGoodRate()						{ return this.good_rate;			}

	//불량율
	public void setBadRate(String bad_rate)			{ this.bad_rate = bad_rate;			}
	public String getBadRate()						{ return this.bad_rate;				}

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



