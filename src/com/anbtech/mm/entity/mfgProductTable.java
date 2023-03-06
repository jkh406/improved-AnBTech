package com.anbtech.mm.entity;
public class mfgProductTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String work_order_no;				//작업지시번호
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String item_spec;					//픔목규격
	private int order_count;					//오더수량
	private String order_unit;					//오더단위
	private int rst_total_count;				//총생산수량
	private int rst_good_count;					//양품수량
	private int rst_bad_count;					//불량수량
	private String worker;						//등록자사번/이름
	private String output_date;					//실적등록일
	private String op_no;						//공정코드
	private String bad_type;					//불량형태
	private String bad_note;					//불량원인

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

	//작업지시번호
	public void setWorkOrderNo(String work_order_no){ this.work_order_no = work_order_no;	}
	public String getWorkOrderNo()					{ return this.work_order_no;			}

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
	public void setRstTotalCount(int rst_total_count){ this.rst_total_count = rst_total_count;	}
	public int getRstTotalCount()					{ return this.rst_total_count;		}

	//양품수량
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//불량수량
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

	//등록자사번/이름
	public void setWorker(String worker)			{ this.worker = worker;				}
	public String getWorker()						{ return this.worker;				}

	//실적등록일
	public void setOutputDate(String output_date)	{ this.output_date = output_date;	}
	public String getOutputDate()					{ return this.output_date;			}

	//공정코드
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//불량형태
	public void setBadType(String bad_type)			{ this.bad_type = bad_type;			}
	public String getBadType()						{ return this.bad_type;				}

	//불량원인
	public void setBadNote(String bad_note)			{ this.bad_note = bad_note;			}
	public String getBadNote()						{ return this.bad_note;				}

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



