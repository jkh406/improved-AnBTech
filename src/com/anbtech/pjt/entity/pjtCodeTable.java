package com.anbtech.pjt.entity;
public class pjtCodeTable
{
	private String pid;						//�����ڵ�
	private String pjt_code;				//project �ڵ�
	private String pjt_name;				//project �̸�
	private String in_date;					//�����
	private String mgr_id;					//����� ���
	private String mgr_name;				//����� �̸�
	private String pjt_status;				//project ����
	private String type;					//type

	private String modify;					//����
	private String delete;					//����
	
	//�����ڵ�
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//project �ڵ�
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//project �̸�
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//�����
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//����� ���
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//����� �̸�
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//project ����
	public void setPjtStatus(String pjt_status)		{ this.pjt_status = pjt_status;		}
	public String getPjtStatus()					{ return this.pjt_status;			}

	//type
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}

	//����
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//����
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

}


