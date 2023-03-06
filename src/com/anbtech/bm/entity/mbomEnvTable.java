package com.anbtech.bm.entity;
public class mbomEnvTable
{
	//Table 구성
	private String pid;							//관리코드
	private String flag;						//구분자
	private String m_code;						//관리코드
	private String spec;						//규격및 이름
	private String tag;							//비고
	
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

	//구분자
	public void setFlag(String flag)				{ this.flag = flag;					}
	public String getFlag()							{ return this.flag;					}

	//관리코드
	public void setMCode(String m_code)				{ this.m_code = m_code;				}
	public String getMCode()						{ return this.m_code;				}

	//규격및 이름
	public void setSpec(String spec)				{ this.spec = spec;					}
	public String getSpec()							{ return this.spec;					}

	//비고
	public void setTag(String tag)					{ this.tag = tag;					}
	public String getTag()							{ return this.tag;					}

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


