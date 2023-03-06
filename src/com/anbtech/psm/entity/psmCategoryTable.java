package com.anbtech.psm.entity;
public class psmCategoryTable
{
	//Table 구성
	private String pid;							//관리코드
	private String korea_name;					//카테고리 한글명
	private String english_name;				//카테고리 영문명
	private String key_word;					//카테고리 영문약어
	private String comp_no;						//업체사업자등록번호
	private String comp_korea;					//업체 한글명
	private String comp_english;				//업체 영문명
	
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

	//카테고리 한글명
	public void setKoreaName(String korea_name)		{ this.korea_name = korea_name;		}
	public String getKoreaName()					{ return this.korea_name;			}

	//카테고리 영문명
	public void setEnglishName(String english_name)	{ this.english_name = english_name;	}
	public String getEnglishName()					{ return this.english_name;			}

	//카테고리 영문약어
	public void setKeyWord(String key_word)			{ this.key_word = key_word;			}
	public String getKeyWord()						{ return this.key_word;				}

	//업체사업자등록번호
	public void setCompNo(String comp_no)			{ this.comp_no = comp_no;			}
	public String getCompNo()						{ return this.comp_no;				}

	//업체 한글명
	public void setCompKorea(String comp_korea)		{ this.comp_korea = comp_korea;		}
	public String getCompKorea()					{ return this.comp_korea;			}

	//업체 영문명
	public void setCompEnglish(String comp_english)	{ this.comp_english = comp_english;	}
	public String getCompEnglish()					{ return this.comp_english;			}
		
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



