package com.anbtech.bm.entity;
public class mbomMasterTable
{
	//Table 구성
	private String pid;							//관리코드
	private String model_code;					//모델코드
	private String model_name;					//모델이름
	private String modelg_code;					//모델군코드
	private String modelg_name;					//모델군이름
	private String fg_code;						//FG코드
	private String pdg_code;					//제품군코드
	private String pdg_name;					//제품군명
	private String pd_code;						//제품코드
	private String pd_name;						//제품명
	private String pjt_code;					//프로젝트코드
	private String pjt_name;					//프로젝트명
	private String reg_id;						//등록자 사번
	private String reg_name;					//등록자
	private String reg_date;					//등록일자
	private String app_id;						//승인자 사번
	private String app_name;					//승인자
	private String app_date;					//승인일자
	private String bom_status;					//BOM상태 및 결재상태
	private String app_no;						//승인관리번호
	private String m_status;					//제품생산상태
	private String purpose;						//BOM구성용도
	
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
		
	//모델군코드
	public void setModelgCode(String modelg_code)	{ this.modelg_code = modelg_code;	}
	public String getModelgCode()					{ return this.modelg_code;			}

	//모델군이름
	public void setModelgName(String modelg_name)	{ this.modelg_name = modelg_name;	}
	public String getModelgName()					{ return this.modelg_name;			}

	//모델코드
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//모델이름
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//FG코드
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//제품군코드
	public void setPdgCode(String pdg_code)			{ this.pdg_code = pdg_code;			}
	public String getPdgCode()						{ return this.pdg_code;				}

	//제품군명
	public void setPdgName(String pdg_name)			{ this.pdg_name = pdg_name;			}
	public String getPdgName()						{ return this.pdg_name;				}

	//제품코드
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;			}
	public String getPdCode()						{ return this.pd_code;				}

	//제품명
	public void setPdName(String pd_name)			{ this.pd_name = pd_name;			}
	public String getPdName()						{ return this.pd_name;				}

	//프로젝트코드
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//프로젝트명
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//등록자 사번
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//등록이름
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//등록일
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//승인자
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//승인이름
	public void setAppName(String app_name)			{ this.app_name = app_name;			}
	public String getAppName()						{ return this.app_name;				}

	//승인일
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//BOM상태
	public void setBomStatus(String bom_status)		{ this.bom_status = bom_status;		}
	public String getBomStatus()					{ return this.bom_status;			}

	//승인번호
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//제품생산상태
	public void setMStatus(String m_status)			{ this.m_status = m_status;			}
	public String getMStatus()						{ return this.m_status;				}

	//BOM구성용도
	public void setPurpose(String purpose)			{ this.purpose = purpose;			}
	public String getPurpose()						{ return this.purpose;				}

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
	public void setPageCut(String page_cut)				{ this.page_cut = page_cut;				}
	public String getPageCut()							{ return this.page_cut;					}

	//총페이지
	public void setTotalPage(int total_page)			{ this.total_page = total_page;			}
	public int getTotalPage()							{ return this.total_page;				}

	//현재페이지
	public void setCurrentPage(int current_page)		{ this.current_page = current_page;		}
	public int getCurrentPage()							{ return this.current_page;				}

	//총 항목수
	public void setTotalArticle(int total_article)		{ this.total_article = total_article;	}
	public int getTotalArticle()						{ return this.total_article;			}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}


