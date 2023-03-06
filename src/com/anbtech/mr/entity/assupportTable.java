package com.anbtech.mr.entity;
public class assupportTable
{
	//지원실적
	private String pid;							//관리코드
	private String register_no;					//등록번호
	private String register_date;				//등록일시
	private String as_field;					//AS분야
	private String code;						//부서관리코드
	private String request_name;				//AS요청자
	private String serial_no;					//장비일련번호
	private String request_date;				//AS요청일
	private String as_date;						//AS방문일
	private String as_type;						//AS형태
	private String as_content;					//AS내용
	private String as_result;					//처리여부
	private String as_delay;					//AS지연사유
	private String as_issue;					//이슈사항
	private String worker;						//AS처리자
	private String company_no;					//AS업체코드
	private String value_request;				//평가독촉횟수

	//실적평가
	private String value_date;					//평가일자
	private String value_yn;					//평가유무
	private String item_pid;					//평가항목관리번호
	private String score;						//평가항목점수

	//평가항목
	private String score_no;					//평가항목일련번호
	private String score_item;					//평가항목명
	private String use_yn;						//평가항목사용여부

	//월별지원건수
	private String division_code;				//부서코드
	private String division_name;				//부서이름
	private String company_name;				//회사이름
	private int jan_cnt;						//1월 AS건수
	private int feb_cnt;						//2월 AS건수
	private int apr_cnt;						//3월 AS건수
	private int mar_cnt;						//4월 AS건수
	private int may_cnt;						//5월 AS건수
	private int jun_cnt;						//6월 AS건수
	private int jul_cnt;						//7월 AS건수
	private int aug_cnt;						//8월 AS건수
	private int sep_cnt;						//9월 AS건수
	private int oct_cnt;						//10월 AS건수
	private int nov_cnt;						//11월 AS건수
	private int dec_cnt;						//12월 AS건수
	private int total_cnt;						//total AS건수

	//월별평가점수
	private double jan_score;					//1월 총평가점수
	private double feb_score;					//2월 총평가점수
	private double apr_score;					//3월 총평가점수
	private double mar_score;					//4월 총평가점수
	private double may_score;					//5월 총평가점수
	private double jun_score;					//6월 총평가점수
	private double jul_score;					//7월 총평가점수
	private double aug_score;					//8월 총평가점수
	private double sep_score;					//9월 총평가점수
	private double oct_score;					//10월 총평가점수
	private double nov_score;					//11월 총평가점수
	private double dec_score;					//12월 총평가점수
	private double average_score;				//평균 총평가점수

	//AS ITEM MASTER
	private String item_code;					//품목코드
	private String item_name;					//품목이름
	private String model_name;					//모델이름
	private String desc;						//규격
	private String in_date;						//도입일자
	private double in_price;					//도입가격
	private String in_type;						//도입구분
	private double exchange_rate;				//적용환율
	private String assets_no;					//자산번호

	//년도별 AS ITEM
	private String contract_date;				//계약일자
	private double contract_price;				//계약금액
	private double contract_rate;				//계약적용율
	private String contract_yn;					//계약적용여부

	//환경설정
	private String mgr_name;					//고정된 관리키워드
	private String register_name;				//관리명
	private String sno;							//관리될 약속된 번호
	
	//event
	private String modify;						//수정
	private String delete;						//삭제
	private String view;						//상세보기
	private String mail;						//멜전송하기

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
		
	//등록번호
	public void setRegisterNo(String register_no)	{ this.register_no = register_no;	}
	public String getRegisterNo()					{ return this.register_no;			}

	//등록일시
	public void setRegisterDate(String register_date)	{ this.register_date = register_date;	}
	public String getRegisterDate()						{ return this.register_date;			}

	//AS분야
	public void setAsField(String as_field)			{ this.as_field = as_field;			}
	public String getAsField()						{ return this.as_field;				}

	//부서관리코드
	public void setCode(String code)				{ this.code = code;					}
	public String getCode()							{ return this.code;					}

	//AS요청자
	public void setRequestName(String request_name)	{ this.request_name = request_name;	}
	public String getRequestName()					{ return this.request_name;			}

	//장비일련번호
	public void setSerialNo(String serial_no)		{ this.serial_no = serial_no;		}
	public String getSerialNo()						{ return this.serial_no;			}

	//AS요청일
	public void setRequestDate(String request_date)	{ this.request_date = request_date;	}
	public String getRequestDate()					{ return this.request_date;			}

	//AS방문일
	public void setAsDate(String as_date)			{ this.as_date = as_date;			}
	public String getAsDate()						{ return this.as_date;				}

	//AS형태
	public void setAsType(String as_type)			{ this.as_type = as_type;			}
	public String getAsType()						{ return this.as_type;				}

	//AS내용
	public void setAsContent(String as_content)		{ this.as_content = as_content;		}
	public String getAsContent()					{ return this.as_content;			}

	//처리여부
	public void setAsResult(String as_result)		{ this.as_result = as_result;		}
	public String getAsResult()						{ return this.as_result;			}

	//AS지연사유
	public void setAsDelay(String as_delay)			{ this.as_delay = as_delay;			}
	public String getAsDelay()						{ return this.as_delay;				}

	//이슈사항
	public void setAsIssue(String as_issue)			{ this.as_issue = as_issue;			}
	public String getAsIssue()						{ return this.as_issue;				}

	//AS처리자
	public void setWorker(String worker)			{ this.worker = worker;				}
	public String getWorker()						{ return this.worker;				}

	//AS업체코드
	public void setCompanyNo(String company_no)		{ this.company_no = company_no;		}
	public String getCompanyNo()					{ return this.company_no;			}

	//평가일자
	public void setValueDate(String value_date)		{ this.value_date = value_date;		}
	public String getValueDate()					{ return this.value_date;			}

	//평가유무
	public void setValueYn(String value_yn)			{ this.value_yn = value_yn;			}
	public String getValueYn()						{ return this.value_yn;				}

	//평가독촉횟수
	public void setValueRequest(String value_request)	{ this.value_request = value_request;	}
	public String getValueRequest()						{ return this.value_request;			}

	//평가항목관리번호
	public void setItemPid(String item_pid)			{ this.item_pid = item_pid;			}
	public String getItemPid()						{ return this.item_pid;				}

	//평가항목점수
	public void setScore(String score)				{ this.score = score;				}
	public String getScore()						{ return this.score;				}

	//평가항목일련번호
	public void setScoreNo(String score_no)			{ this.score_no = score_no;			}
	public String getScoreNo()						{ return this.score_no;				}

	//평가항목명
	public void setScoreItem(String score_item)		{ this.score_item = score_item;		}
	public String getScoreItem()					{ return this.score_item;			}

	//평가항목사용여부
	public void setUseYn(String use_yn)				{ this.use_yn = use_yn;				}
	public String getUseYn()						{ return this.use_yn;				}

	//부서코드
	public void setDivisionCode(String division_code)	{ this.division_code = division_code;	}
	public String getDivisionCode()						{ return this.division_code;			}

	//부서이름
	public void setDivisionName(String division_name)	{ this.division_name = division_name;	}
	public String getDivisionName()						{ return this.division_name;			}

	//회사이름
	public void setCompanyName(String company_name)		{ this.company_name = company_name;		}
	public String getCompanyName()						{ return this.company_name;				}

	//1월 AS건수
	public void setJanCnt(int jan_cnt)				{ this.jan_cnt = jan_cnt;			}
	public int getJanCnt()							{ return this.jan_cnt;				}

	//2월 AS건수
	public void setFebCnt(int feb_cnt)				{ this.feb_cnt = feb_cnt;			}
	public int getFebCnt()							{ return this.feb_cnt;				}

	//3월 AS건수
	public void setAprCnt(int apr_cnt)				{ this.apr_cnt = apr_cnt;			}
	public int getAprCnt()							{ return this.apr_cnt;				}

	//4월 AS건수
	public void setMarCnt(int mar_cnt)				{ this.mar_cnt = mar_cnt;			}
	public int getMarCnt()							{ return this.mar_cnt;				}

	//5월 AS건수
	public void setMayCnt(int may_cnt)				{ this.may_cnt = may_cnt;			}
	public int getMayCnt()							{ return this.may_cnt;				}

	//6월 AS건수
	public void setJunCnt(int jun_cnt)				{ this.jun_cnt = jun_cnt;			}
	public int getJunCnt()							{ return this.jun_cnt;				}

	//7월 AS건수
	public void setJulCnt(int jul_cnt)				{ this.jul_cnt = jul_cnt;			}
	public int getJulCnt()							{ return this.jul_cnt;				}

	//8월 AS건수
	public void setAugCnt(int aug_cnt)				{ this.aug_cnt = aug_cnt;			}
	public int getAugCnt()							{ return this.aug_cnt;				}

	//9월 AS건수
	public void setSepCnt(int sep_cnt)				{ this.sep_cnt = sep_cnt;			}
	public int getSepCnt()							{ return this.sep_cnt;				}

	//10월 AS건수
	public void setOctCnt(int oct_cnt)				{ this.oct_cnt = oct_cnt;			}
	public int getOctCnt()							{ return this.oct_cnt;				}

	//11월 AS건수
	public void setNovCnt(int nov_cnt)				{ this.nov_cnt = nov_cnt;			}
	public int getNovCnt()							{ return this.nov_cnt;				}

	//12월 AS건수
	public void setDecCnt(int dec_cnt)				{ this.dec_cnt = dec_cnt;			}
	public int getDecCnt()							{ return this.dec_cnt;				}

	//total AS건수
	public void setTotalCnt(int total_cnt)				{ this.total_cnt = total_cnt;			}
	public int getTotalCnt()							{ return this.total_cnt;				}

	//1월 총평가점수
	public void setJanScore(double jan_score)		{ this.jan_score = jan_score;		}
	public double getJanScore()						{ return this.jan_score;			}

	//2월 총평가점수
	public void setFebScore(double feb_score)		{ this.feb_score = feb_score;		}
	public double getFebScore()						{ return this.feb_score;			}

	//3월 총평가점수
	public void setAprScore(double apr_score)		{ this.apr_score = apr_score;		}
	public double getAprScore()						{ return this.apr_score;			}

	//4월 총평가점수
	public void setMarScore(double mar_score)		{ this.mar_score = mar_score;		}
	public double getMarScore()						{ return this.mar_score;			}

	//5월 총평가점수
	public void setMayScore(double may_score)		{ this.may_score = may_score;		}
	public double getMayScore()						{ return this.may_score;			}

	//6월 총평가점수
	public void setJunScore(double jun_score)		{ this.jun_score = jun_score;		}
	public double getJunScore()						{ return this.jun_score;			}

	//7월 총평가점수
	public void setJulScore(double jul_score)		{ this.jul_score = jul_score;		}
	public double getJulScore()						{ return this.jul_score;			}

	//8월 총평가점수
	public void setAugScore(double aug_score)		{ this.aug_score = aug_score;		}
	public double getAugScore()						{ return this.aug_score;			}

	//9월 총평가점수
	public void setSepScore(double sep_score)		{ this.sep_score = sep_score;		}
	public double getSepScore()						{ return this.sep_score;			}

	//10월 총평가점수
	public void setOctScore(double oct_score)		{ this.oct_score = oct_score;		}
	public double getOctScore()						{ return this.oct_score;			}

	//11월 총평가점수
	public void setNovScore(double nov_score)		{ this.nov_score = nov_score;		}
	public double getNovScore()						{ return this.nov_score;			}

	//12월 총평가점수
	public void setDecScore(double dec_score)		{ this.dec_score = dec_score;		}
	public double getDecScore()						{ return this.dec_score;			}

	//평균 총평가점수
	public void setAverageScore(double average_score)	{ this.average_score = average_score;	}
	public double getAverageScore()						{ return this.average_score;			}

	//품목코드
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//품목이름
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//모델이름
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//규격
	public void setDesc(String desc)				{ this.desc = desc;					}
	public String getDesc()							{ return this.desc;					}

	//도입일자
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//도입가격
	public void setInPrice(double in_price)			{ this.in_price = in_price;			}
	public double getInPrice()						{ return this.in_price;				}

	//도입구분
	public void setInType(String in_type)			{ this.in_type = in_type;			}
	public String getInType()						{ return this.in_type;				}

	//적용환율
	public void setExchangeRate(double exchange_rate)	{ this.exchange_rate = exchange_rate;	}
	public double getExchangeRate()						{ return this.exchange_rate;			}

	//자산번호
	public void setAssetsNo(String assets_no)		{ this.assets_no = assets_no;		}
	public String getAssetsNo()						{ return this.assets_no;			}

	//계약일자
	public void setContractDate(String contract_date)	{ this.contract_date = contract_date;	}
	public String getContractDate()						{ return this.contract_date;			}

	//계약금액
	public void setContractPrice(double contract_price)	{ this.contract_price = contract_price;	}
	public double getContractPrice()					{ return this.contract_price;			}

	//계약적용율
	public void setContractRate(double contract_rate)	{ this.contract_rate = contract_rate;	}
	public double getContractRate()						{ return this.contract_rate;			}

	//계약적용여부
	public void setContractYn(String contract_yn)		{ this.contract_yn = contract_yn;		}
	public String getContractYn()						{ return this.contract_yn;				}

	//고정된 관리키워드
	public void setMgrName(String mgr_name)				{ this.mgr_name = mgr_name;				}
	public String getMgrName()							{ return this.mgr_name;					}

	//관리명
	public void setRegisterName(String register_name)	{ this.register_name = register_name;	}
	public String getRegisterName()						{ return this.register_name;			}

	//관리될 약속된 번호
	public void setSno(String sno)					{ this.sno = sno;					}
	public String getSno()							{ return this.sno;					}

	//수정
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//삭제
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//상세보기
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//멜전송하기
	public void setMail(String mail)				{ this.mail = mail;					}
	public String getMail()							{ return this.mail;					}

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


