package com.anbtech.mm.entity;
public class mfgOperatorTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String assy_code;					//ASSY CODE
	private String assy_spec;					//ASSY CODE 규격
	private int level_no;						//레벨번호
	private int mfg_count;						//제조수량
	private String mfg_unit;					//품목단위
	private String op_start_date;				//공정착수예정일
	private String op_end_date;					//공정완료예정일
	private String order_date;					//오더확정일자
	private String buy_type;					//조달구분
	private String factory_no;					//공장번호
	private String factory_name;				//공장이름
	private String work_no;						//작업장번호
	private String work_name;					//작업장명
	private String op_no;						//공정번호
	private String op_name;						//공정명
	private String mfg_type;					//제조구분
	private String mfg_id;						//제조담당자사번
	private String mfg_name;					//제조담당자명
	private String note;						//비고
	private String comp_code;					//외주생산업체코드
	private String comp_name;					//외주생산업체명
	private String comp_user;					//외주생산업체담당자
	private String comp_tel;					//외주생산업체연락처
	private String op_order;					//공정계획확정여부
	
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

	//ASSY CODE 규격
	public void setAssySpec(String assy_spec)		{ this.assy_spec = assy_spec;		}
	public String getAssySpec()						{ return this.assy_spec;			}

	//레벨번호
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//제조수량
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//품목단위
	public void setMfgUnit(String mfg_unit)			{ this.mfg_unit = mfg_unit;			}
	public String getMfgUnit()						{ return this.mfg_unit;				}

	//공정착수예정일
	public void setOpStartDate(String op_start_date)		{ this.op_start_date = op_start_date;		}
	public String getOpStartDate()							{ return this.op_start_date;				}

	//공정완료예정일
	public void setOpEndDate(String op_end_date)			{ this.op_end_date = op_end_date;			}
	public String getOpEndDate()							{ return this.op_end_date;					}

	//오더확정일자
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//조달구분
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}
	
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//공장이름
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//작업장번호
	public void setWorkNo(String work_no)			{ this.work_no = work_no;			}
	public String getWorkNo()						{ return this.work_no;				}

	//작업장명
	public void setWorkName(String work_name)		{ this.work_name = work_name;		}
	public String getWorkName()						{ return this.work_name;			}

	//공정번호
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//공정명
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

	//제조구분
	public void setMfgType(String mfg_type)			{ this.mfg_type = mfg_type;			}
	public String getMfgType()						{ return this.mfg_type;				}

	//제조담당자사번
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//제조담당자명
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//비고
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

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

	//공정계획확정여부
	public void setOpOrder(String op_order)			{ this.op_order = op_order;			}
	public String getOpOrder()						{ return this.op_order;				}

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




