package com.anbtech.mm.entity;
public class mfgReqMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String gid;							//Group 관리코드
	private String mfg_no;						//MFG 관리코드
	private String mfg_req_no;					//부품출고의뢰번호
	private String assy_code;					//ASSY CODE
	private String assy_spec;					//ASSY 규격
	private int level_no;						//레벨번호
	private String req_status;					//진행상태
	private String req_date;					//부품출고의뢰일자
	private String req_div_code;				//부품출고부서코드
	private String req_div_name;				//부품출고부서명
	private String req_user_id;					//부풀출고의뢰자사번
	private String req_user_name;				//부품출고의뢰자
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

	//부품출고의뢰번호
	public void setMfgReqNo(String mfg_req_no)		{ this.mfg_req_no = mfg_req_no;		}
	public String getMfgReqNo()						{ return this.mfg_req_no;			}

	//ASSY CODE
	public void setAssyCode(String assy_code)		{ this.assy_code = assy_code;		}
	public String getAssyCode()						{ return this.assy_code;			}

	//ASSY 규격
	public void setAssySpec(String assy_spec)		{ this.assy_spec = assy_spec;		}
	public String getAssySpec()						{ return this.assy_spec;			}

	//레벨번호
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//진행상태
	public void setReqStatus(String req_status)		{ this.req_status = req_status;		}
	public String getReqStatus()					{ return this.req_status;			}

	//부품출고의뢰일자
	public void setReqDate(String req_date)			{ this.req_date = req_date;			}
	public String getReqDate()						{ return this.req_date;				}

	//부품출고부서코드
	public void setReqDivCode(String req_div_code)	{ this.req_div_code = req_div_code;	}
	public String getReqDivCode()					{ return this.req_div_code;			}

	//부품출고부서명
	public void setReqDivName(String req_div_name)	{ this.req_div_name = req_div_name;	}
	public String getReqDivName()					{ return this.req_div_name;			}

	//부풀출고의뢰자사번
	public void setReqUserId(String req_user_id)	{ this.req_user_id = req_user_id;	}
	public String getReqUserId()					{ return this.req_user_id;			}

	//부품출고의뢰자
	public void setReqUserName(String req_user_name){ this.req_user_name = req_user_name;	}
	public String getReqUserName()					{ return this.req_user_name;			}

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







