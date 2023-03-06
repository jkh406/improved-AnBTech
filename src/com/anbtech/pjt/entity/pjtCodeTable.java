package com.anbtech.pjt.entity;
public class pjtCodeTable
{
	private String pid;						//관리코드
	private String pjt_code;				//project 코드
	private String pjt_name;				//project 이름
	private String in_date;					//등록일
	private String mgr_id;					//등록자 사번
	private String mgr_name;				//등록자 이름
	private String pjt_status;				//project 상태
	private String type;					//type

	private String modify;					//수정
	private String delete;					//삭제
	
	//관리코드
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//project 코드
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//project 이름
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//등록일
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//등록자 사번
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//등록자 이름
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//project 상태
	public void setPjtStatus(String pjt_status)		{ this.pjt_status = pjt_status;		}
	public String getPjtStatus()					{ return this.pjt_status;			}

	//type
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}

	//수정
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//삭제
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

}


