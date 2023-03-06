package com.anbtech.bm.entity;
public class primeCostTable
{
	//Table 구성
	private String item_code;							//품목코드
	private String item_name;							//품목이름
	private String item_desc;							//품목규격
	private String item_count;							//품목수량
	private String std_price;							//표준단가
	private String ave_price;							//평균단가
	private String cur_price;							//현재단가
	private String std_sum;								//표준단가 총액
	private String ave_sum;								//평균단가 총액
	private String cur_sum;								//현재단가 총액

	
	
	/*--------------------------- 메소드 만들기 -------------------------------*/
	//품목코드
	public void setItemCode(String item_code)			{ this.item_code = item_code;		}
	public String getItemCode()							{ return this.item_code;			}

	//품목이름
	public void setItemName(String item_name)			{ this.item_name = item_name;		}
	public String getItemName()							{ return this.item_name;			}

	//품목규격
	public void setItemDesc(String item_desc)			{ this.item_desc = item_desc;		}
	public String getItemDesc()							{ return this.item_desc;			}

	//품목수량
	public void setItemCount(String item_count)			{ this.item_count = item_count;		}
	public String getItemCount()						{ return this.item_count;			}
	
	//표준단가
	public void setStdPrice(String std_price)			{ this.std_price = std_price;		}
	public String getStdPrice()							{ return this.std_price;			}
	
	//평균단가
	public void setAvePrice(String ave_price)			{ this.ave_price = ave_price;		}
	public String getAvePrice()							{ return this.ave_price;			}
	
	//현재단가
	public void setCurPrice(String cur_price)			{ this.cur_price = cur_price;		}
	public String getCurPrice()							{ return this.cur_price;			}

	//표준단가 총액
	public void setStdSum(String std_sum)				{ this.std_sum = std_sum;			}
	public String getStdSum()							{ return this.std_sum;				}
	
	//평균단가 총액
	public void setAveSum(String ave_sum)				{ this.ave_sum = ave_sum;			}
	public String getAveSum()							{ return this.ave_sum;				}
	
	//현재단가 총액
	public void setCurSum(String cur_sum)				{ this.cur_sum = cur_sum;			}
	public String getCurSum()							{ return this.cur_sum;				}

	
}




