package com.anbtech.dms.entity;
public class OfficialDocumentAppTable
{
	//공문서 결재승인 전체(OfficialDocument,InDocument_receive,InDocument_send,OutDocument_receive,OutDocument_send)
	private String id;						//공통:관리번호

	private String gian_id;					//기안자 사번
	private String gian_name;				//기안자 이름
	private String gian_rank;				//기안자 직급
	private String gian_div;				//기안자 부서명
	private String gian_date;				//기안자 날자
	private String gian_comment;			//기안자 의견

	private String review_id;				//검토자 사번
	private String review_name;				//검토자 이름
	private String review_rank;				//검토자 직급
	private String review_div;				//검토자 부서명
	private String review_date;				//검토자 날자
	private String review_comment;			//검토자 의견

	private String agree_ids;				//협조자 사번
	private String agree_names;				//협조자 이름
	private String agree_ranks;				//협조자 직급
	private String agree_divs;				//협조자 부서명
	private String agree_dates;				//협조자 날자
	private String agree_comments;			//협조자 의견

	private String decision_id;				//승인자 사번
	private String decision_name;			//승인자 이름
	private String decision_rank;			//승인자 직급
	private String decision_div;			//승인자 부서명
	private String decision_date;			//승인자 날자
	private String decision_comment;		//승인자 의견

		
	//help method
	public void setId(String id)						{ this.id = id;							}
	public String getId()								{ return this.id;						}

	//기안자
	public void setGianId(String gian_id)				{ this.gian_id = gian_id;				}
	public String getGianId()							{ return this.gian_id;					}

	public void setGianName(String gian_name)			{ this.gian_name = gian_name;			}
	public String getGianName()							{ return this.gian_name;				}

	public void setGianRank(String gian_rank)			{ this.gian_rank = gian_rank;			}
	public String getGianRank()							{ return this.gian_rank;				}

	public void setGianDiv(String gian_div)				{ this.gian_div = gian_div;				}
	public String getGianDiv()							{ return this.gian_div;					}

	public void setGianDate(String gian_date)			{ this.gian_date = gian_date;			}
	public String getGianDate()							{ return this.gian_date;				}

	public void setGianComment(String gian_comment)		{ this.gian_comment = gian_comment;		}
	public String getGianComment()						{ return this.gian_comment;				}

	//검토자
	public void setReviewId(String review_id)			{ this.review_id = review_id;			}
	public String getReviewId()							{ return this.review_id;				}

	public void setReviewName(String review_name)		{ this.review_name = review_name;		}
	public String getReviewName()						{ return this.review_name;				}

	public void setReviewRank(String review_rank)		{ this.review_rank = review_rank;		}
	public String getReviewRank()						{ return this.review_rank;				}

	public void setReviewDiv(String review_div)			{ this.review_div = review_div;			}
	public String getReviewDiv()						{ return this.review_div;				}

	public void setReviewDate(String review_date)		{ this.review_date = review_date;		}
	public String getReviewDate()						{ return this.review_date;				}

	public void setReviewComment(String review_comment)	{ this.review_comment = review_comment;	}
	public String getReviewComment()					{ return this.review_comment;			}

	//헙조자
	public void setAgreeIds(String agree_ids)			{ this.agree_ids = agree_ids;			}
	public String getAgreeIds()							{ return this.agree_ids;				}

	public void setAgreeNames(String agree_names)		{ this.agree_names = agree_names;		}
	public String getAgreeNames()						{ return this.agree_names;				}

	public void setAgreeRanks(String agree_ranks)		{ this.agree_ranks = agree_ranks;		}
	public String getAgreeRanks()						{ return this.agree_ranks;				}

	public void setAgreeDivs(String agree_divs)			{ this.agree_divs = agree_divs;			}
	public String getAgreeDivs()						{ return this.agree_divs;				}

	public void setAgreeDates(String agree_dates)		{ this.agree_dates = agree_dates;		}
	public String getAgreeDates()						{ return this.agree_dates;				}

	public void setAgreeComments(String agree_comments)	{ this.agree_comments = agree_comments;	}
	public String getAgreeComments()					{ return this.agree_comments;			}

	//승인자
	public void setDecisionId(String decision_id)		{ this.decision_id = decision_id;		}
	public String getDecisionId()						{ return this.decision_id;				}

	public void setDecisionName(String decision_name)	{ this.decision_name = decision_name;	}
	public String getDecisionName()						{ return this.decision_name;			}

	public void setDecisionRank(String decision_rank)	{ this.decision_rank = decision_rank;	}
	public String getDecisionRank()						{ return this.decision_rank;			}

	public void setDecisionDiv(String decision_div)		{ this.decision_div = decision_div;		}
	public String getDecisionDiv()						{ return this.decision_div;				}

	public void setDecisionDate(String decision_date)	{ this.decision_date = decision_date;	}
	public String getDecisionDate()						{ return this.decision_date;			}

	public void setDecisionComment(String decision_comment)	{ this.decision_comment = decision_comment;	}
	public String getDecisionComment()					{ return this.decision_comment;			}

}
