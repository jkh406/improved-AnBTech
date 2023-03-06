package com.anbtech.dcm.entity;
public class eccOrdTable
{
	//ECC_ORD 항목 
	private String pid;							//관리코드
	private String chg_position;				//변경부위
	private String trouble;						//문제분류
	private String condition;					//현상및원인
	private String solution;					//대책
	private String fname;						//첨부파일원래이름
	private String sname;						//첨부파일저장이름
	private String ftype;						//첨부파일확장자명
	private String fsize;						//첨부파일 File Size
	private String app_no;						//결재승인번호
	
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
		
	//변경부위
	public void setChgPosition(String chg_position)	{ this.chg_position = chg_position;	}
	public String getChgPosition()					{ return this.chg_position;			}

	//문제분류
	public void setTrouble(String trouble)			{ this.trouble = trouble;			}
	public String getTrouble()						{ return this.trouble;				}

	//현상및원인
	public void setCondition(String condition)		{ this.condition = condition;		}
	public String getCondition()					{ return this.condition;			}

	//대책
	public void setSolution(String solution)		{ this.solution = solution;			}
	public String getSolution()						{ return this.solution;				}

	//첨부파일원래이름
	public void setFname(String fname)				{ this.fname = fname;				}
	public String getFname()						{ return this.fname;				}

	//첨부파일저장이름
	public void setSname(String sname)				{ this.sname = sname;				}
	public String getSname()						{ return this.sname;				}

	//첨부파일확장자명
	public void setFtype(String ftype)				{ this.ftype = ftype;				}
	public String getFtype()						{ return this.ftype;				}

	//첨부파일 File Size
	public void setFsize(String fsize)				{ this.fsize = fsize;				}
	public String getFsize()						{ return this.fsize;				}

	//결재승인번호
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}


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




