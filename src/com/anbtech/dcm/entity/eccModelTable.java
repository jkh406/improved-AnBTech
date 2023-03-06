package com.anbtech.dcm.entity;
public class eccModelTable
{
	//Table 구성
	private String pid;							//관리코드
	private String eco_no;						//설계변경관리번호
	private String gid;							//그룹관리코드
	private String model_code;					//모델코드
	private String model_name;					//모델명
	private String fg_code;						//FG코드
	private String order_date;					//적용일자
	private String ecc_status;					//진행상태
	private String m_status;					//제품생산상태
	
	/*--------------------------- 메소드 만들기 -------------------------------*/
	//관리코드
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}

	//설계변경관리번호
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}
		
	//모델그룹관리번호
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//모델코드
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//모델명
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//FG코드
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//적용일자
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//진행상태
	public void setEccStatus(String ecc_status)		{ this.ecc_status = ecc_status;		}
	public String getEccStatus()					{ return this.ecc_status;			}

	//제품생산상태
	public void setMStatus(String m_status)			{ this.m_status = m_status;			}
	public String getMStatus()						{ return this.m_status;				}

}


