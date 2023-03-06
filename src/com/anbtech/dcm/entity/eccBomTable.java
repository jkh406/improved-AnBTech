package com.anbtech.dcm.entity;
public class eccBomTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//그룹관리코드
	private String parent_code;					//모품목코드
	private String child_code;					//자품목코드
	private String level_no;					//레벨번호
	private String part_name;					//부품명
	private String part_spec;					//부품특성
	private String location;					//위치정보
	private String op_code;						//공정코드
	private String qty_unit;					//수량단위
	private String qty;							//수량
	private String maker_name;					//메이커명
	private String maker_code;					//형명(메이커코드)
	private String price_unit;					//가격단위
	private String price;						//가격
	private String add_date;					//등록일
	private String buy_type;					//사급/도급
	private String eco_no;						//설계변경관리번호
	private String chg_id;						//설계변경관리자 사번
	private String chg_name;					//설계변경관리자 이름
	private String adtag;						//부품추가/삭제
	private String change_date;					//변경 작업일
	private String bom_start_date;				//유효시작일
	private String bom_end_date;				//유효종료일
	private String ecc_reason;					//변경이유
	private String note;						//비고
	private String chg_order;					//변경내용 같은 ECO NO의 같은 변경부품별 Sorting NO
	private String app_status;					//변경내용 결재승인여부 0 : 승인전, 1 : 승인후
	
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
		
	//모델그룹관리번호
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//모품목코드
	public void setParentCode(String parent_code)	{ this.parent_code = parent_code;	}
	public String getParentCode()					{ return this.parent_code;			}

	//자품목코드
	public void setChildCode(String child_code)		{ this.child_code = child_code;		}
	public String getChildCode()					{ return this.child_code;			}

	//레벨번호
	public void setLevelNo(String level_no)			{ this.level_no = level_no;			}
	public String getLevelNo()						{ return this.level_no;				}

	//부품명
	public void setPartName(String part_name)		{ this.part_name = part_name;		}
	public String getPartName()						{ return this.part_name;			}

	//부품특성
	public void setPartSpec(String part_spec)		{ this.part_spec = part_spec;		}
	public String getPartSpec()						{ return this.part_spec;			}

	//위치정보
	public void setLocation(String location)		{ this.location = location;			}
	public String getLocation()						{ return this.location;				}

	//공정코드
	public void setOpCode(String op_code)			{ this.op_code = op_code;			}
	public String getOpCode()						{ return this.op_code;				}

	//수량단위
	public void setQtyUnit(String qty_unit)			{ this.qty_unit = qty_unit;			}
	public String getQtyUnit()						{ return this.qty_unit;				}

	//메이커명
	public void setMakerName(String maker_name)		{ this.maker_name = maker_name;		}
	public String getMakerName()					{ return this.maker_name;			}

	//형명(메이커코드)
	public void setMakerCode(String maker_code)		{ this.maker_code = maker_code;		}
	public String getMakerCode()					{ return this.maker_code;			}

	//수량
	public void setQty(String qty)					{ this.qty = qty;					}
	public String getQty()							{ return this.qty;					}

	//등록일
	public void setAddDate(String add_date)			{ this.add_date = add_date;			}
	public String getAddDate()						{ return this.add_date;				}

	//사급/도급
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//가격단위
	public void setPriceUnit(String price_unit)		{ this.price_unit = price_unit;		}
	public String getPriceUnit()					{ return this.price_unit;			}

	//가격
	public void setPrice(String price)				{ this.price = price;				}
	public String getPrice()						{ return this.price;				}

	//부품추가/삭제
	public void setAdTag(String adtag)				{ this.adtag = adtag;				}
	public String getAdTag()						{ return this.adtag;				}

	//변경 작업일
	public void setChangeDate(String change_date)	{ this.change_date = change_date;	}
	public String getChangeDate()					{ return this.change_date;			}

	//설계변경관리번호
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}

	//설계변경관리자 사번
	public void setChgId(String chg_id)				{ this.chg_id = chg_id;				}
	public String getChgId()						{ return this.chg_id;				}

	//설계변경관리자 이름
	public void setChgName(String chg_name)			{ this.chg_name = chg_name;			}
	public String getChgName()						{ return this.chg_name;				}

	//유효시작일
	public void setBomStartDate(String bom_start_date)	{ this.bom_start_date = bom_start_date;		}
	public String getBomStartDate()						{ return this.bom_start_date;				}

	//유효종료일
	public void setBomEndDate(String bom_end_date)	{ this.bom_end_date = bom_end_date;	}
	public String getBomEndDate()					{ return this.bom_end_date;			}

	//변경이유
	public void setEccReason(String ecc_reason)		{ this.ecc_reason = ecc_reason;		}
	public String getEccReason()					{ return this.ecc_reason;			}

	//비고
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//변경내용 같은 ECO NO의 같은 변경부품별 Sorting NO
	public void setChgOrder(String chg_order)		{ this.chg_order = chg_order;		}
	public String getChgOrder()						{ return this.chg_order;			}

	//변경내용 결재승인여부 0 : 승인전, 1 : 승인후
	public void setAppStatus(String app_status)		{ this.app_status = app_status;		}
	public String getAppStatus()					{ return this.app_status;			}

	//--------------------------  Modify 관련 ------------------------------------------//
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
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;			}
	public String getPageCut()						{ return this.page_cut;				}

	//총페이지
	public void setTotalPage(int total_page)		{ this.total_page = total_page;		}
	public int getTotalPage()						{ return this.total_page;			}

	//현재페이지
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;	}
	public int getCurrentPage()						{ return this.current_page;			}

	//총 항목수
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;}
	public int getTotalArticle()					{ return this.total_article;		}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}


