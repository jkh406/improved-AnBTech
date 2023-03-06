package com.anbtech.mm.entity;
public class mfgOrderTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String work_order_no;				//작업지시번호
	private int order_count;					//오더수량
	private String work_no;						//작업장코드
	private String work_name;					//작업장명
	private String op_no;						//공정코드
	private String op_name;						//공정명
	private String comp_code;					//외주생산업체코드
	private String comp_name;					//외주생산업체명
	private String comp_user;					//외주생산업체담당자
	private String comp_tel;					//외주생산업체연락처
	private String mfg_type;					//사내외구분
	private String work_order_status;			//지시상태
	private String order_start_date;			//착수예정일
	private String order_end_date;				//완료예정일
	private String order_date;					//작업지시일
	private String mfg_id;						//제조담당자사번
	private String mfg_name;					//제조담당자명
	private String note;						//오더특이사항
	private int rst_good_count;					//총생산량중양품수량
	private int rst_bad_count;					//총생산량중불량수량
	
	
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

	//오더수량
	public void setOrderCount(int order_count)		{ this.order_count = order_count;	}
	public int getOrderCount()						{ return this.order_count;			}

	//작업장코드
	public void setWorkNo(String work_no)			{ this.work_no = work_no;			}
	public String getWorkNo()						{ return this.work_no;				}

	//작업장명
	public void setWorkName(String work_name)		{ this.work_name = work_name;		}
	public String getWorkName()						{ return this.work_name;			}

	//공정코드
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//공정명
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

	//외주생산업체코드
	public void setCompCode(String comp_code)		{ this.comp_code = comp_code;		}
	public String getCompCode()						{ return this.comp_code;			}

	//외주생산업체명
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}

	//외주생산업체담당자
	public void setCompUser(String comp_user)		{ this.comp_user = comp_user;		}
	public String getCompUser()						{ return this.comp_user;			}

	//외주생산업체연락처
	public void setCompTel(String comp_tel)			{ this.comp_tel = comp_tel;			}
	public String getCompTel()						{ return this.comp_tel;				}

	//사내외구분
	public void setMfgType(String mfg_type)			{ this.mfg_type = mfg_type;			}
	public String getMfgType()						{ return this.mfg_type;				}

	//지시상태
	public void setWorkOrderStatus(String work_order_status)	{ this.work_order_status = work_order_status;	}
	public String getWorkOrderStatus()							{ return this.work_order_status;				}

	//착수예정일
	public void setOrderStartDate(String order_start_date)		{ this.order_start_date = order_start_date;		}
	public String getOrderStartDate()							{ return this.order_start_date;					}

	//완료예정일
	public void setOrderEndDate(String order_end_date)			{ this.order_end_date = order_end_date;		}
	public String getOrderEndDate()								{ return this.order_end_date;				}

	//작업지시일
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//제조담당자사번
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//제조담당자명
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//오더특이사항
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//총생산량중양품수량
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//총생산량중불량수량
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

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




