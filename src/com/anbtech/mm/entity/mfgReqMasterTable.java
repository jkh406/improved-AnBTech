package com.anbtech.mm.entity;
public class mfgReqMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String mfg_req_no;					//��ǰ����Ƿڹ�ȣ
	private String assy_code;					//ASSY CODE
	private String assy_spec;					//ASSY �԰�
	private int level_no;						//������ȣ
	private String req_status;					//�������
	private String req_date;					//��ǰ����Ƿ�����
	private String req_div_code;				//��ǰ���μ��ڵ�
	private String req_div_name;				//��ǰ���μ���
	private String req_user_id;					//��Ǯ����Ƿ��ڻ��
	private String req_user_name;				//��ǰ����Ƿ���
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����̸�
	
	//event
	private String modify;						//����
	private String delete;						//����
	private String view;						//�󼼺���
	
	//������ ǥ���ϱ�
	private String page_cut;					//���ڷ� Listǥ���ϱ�
	private int total_page;						//��������
	private int current_page;					//����������
	private int total_article;					//�� �׸��

	//�˻��׸�
	private String sItem;						//Item
	private String sWord;						//Word
	
	/*--------------------------- �޼ҵ� ����� -------------------------------*/
	//�����ڵ�
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}

	//Group �����ڵ�
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//MFG �����ڵ�
	public void setMfgNo(String mfg_no)				{ this.mfg_no = mfg_no;				}
	public String getMfgNo()						{ return this.mfg_no;				}

	//��ǰ����Ƿڹ�ȣ
	public void setMfgReqNo(String mfg_req_no)		{ this.mfg_req_no = mfg_req_no;		}
	public String getMfgReqNo()						{ return this.mfg_req_no;			}

	//ASSY CODE
	public void setAssyCode(String assy_code)		{ this.assy_code = assy_code;		}
	public String getAssyCode()						{ return this.assy_code;			}

	//ASSY �԰�
	public void setAssySpec(String assy_spec)		{ this.assy_spec = assy_spec;		}
	public String getAssySpec()						{ return this.assy_spec;			}

	//������ȣ
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//�������
	public void setReqStatus(String req_status)		{ this.req_status = req_status;		}
	public String getReqStatus()					{ return this.req_status;			}

	//��ǰ����Ƿ�����
	public void setReqDate(String req_date)			{ this.req_date = req_date;			}
	public String getReqDate()						{ return this.req_date;				}

	//��ǰ���μ��ڵ�
	public void setReqDivCode(String req_div_code)	{ this.req_div_code = req_div_code;	}
	public String getReqDivCode()					{ return this.req_div_code;			}

	//��ǰ���μ���
	public void setReqDivName(String req_div_name)	{ this.req_div_name = req_div_name;	}
	public String getReqDivName()					{ return this.req_div_name;			}

	//��Ǯ����Ƿ��ڻ��
	public void setReqUserId(String req_user_id)	{ this.req_user_id = req_user_id;	}
	public String getReqUserId()					{ return this.req_user_id;			}

	//��ǰ����Ƿ���
	public void setReqUserName(String req_user_name){ this.req_user_name = req_user_name;	}
	public String getReqUserName()					{ return this.req_user_name;			}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����̸�
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//--------------- ���� ���� ---------------------------//
	//����
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//����
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//�󼼺���
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//���ڷ� Listǥ���ϱ�
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;				}
	public String getPageCut()						{ return this.page_cut;					}

	//��������
	public void setTotalPage(int total_page)		{ this.total_page = total_page;			}
	public int getTotalPage()						{ return this.total_page;				}

	//����������
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;		}
	public int getCurrentPage()						{ return this.current_page;				}

	//�� �׸��
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;	}
	public int getTotalArticle()					{ return this.total_article;			}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}







