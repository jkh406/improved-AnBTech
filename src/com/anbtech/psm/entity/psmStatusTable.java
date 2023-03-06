package com.anbtech.psm.entity;
public class psmStatusTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String psm_code;					//�����ڵ�
	private String psm_type;					//��������
	private String comp_name;					//��������
	private String comp_category;				//����ī�װ���
	private String psm_korea;					//�������ѱ�
	private String psm_english;					//��������
	private String psm_start_date;				//����������
	private String psm_end_date;				//����������
	private String psm_pm;						//����PM
	private String psm_pm_div;					//����PM �μ��ڵ�
	private String psm_mgr;						//���������
	private String psm_mgr_div;					//��������� �μ��ڵ�
	private String psm_budget;					//������������
	private String psm_budget_div;				//������������ �μ��ڵ�
	private String psm_user;					//���������
	private String psm_user_div;				//��������� �μ��ڵ�
	private String change_desc;					//��������
	private String psm_status;					//�������
	private String change_date;					//���(����)��
	
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

	//�����ڵ�
	public void setPsmCode(String psm_code)			{ this.psm_code = psm_code;			}
	public String getPsmCode()						{ return this.psm_code;				}

	//��������
	public void setPsmType(String psm_type)			{ this.psm_type = psm_type;			}
	public String getPsmType()						{ return this.psm_type;				}

	//��������
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}

	//����ī�װ���
	public void setCompCategory(String comp_category)	{ this.comp_category = comp_category;	}
	public String getCompCategory()						{ return this.comp_category;			}

	//�������ѱ�
	public void setPsmKorea(String psm_korea)		{ this.psm_korea = psm_korea;		}
	public String getPsmKorea()						{ return this.psm_korea;			}

	//��������
	public void setPsmEnglish(String psm_english)	{ this.psm_english = psm_english;	}
	public String getPsmEnglish()					{ return this.psm_english;			}

	//����������
	public void setPsmStartDate(String psm_start_date)	{ this.psm_start_date = psm_start_date;	}
	public String getPsmStartDate()						{ return this.psm_start_date;			}

	//����������
	public void setPsmEndDate(String psm_end_date)		{ this.psm_end_date = psm_end_date;		}
	public String getPsmEndDate()						{ return this.psm_end_date;				}

	//����PM
	public void setPsmPm(String psm_pm)				{ this.psm_pm = psm_pm;			}
	public String getPsmPm()						{ return this.psm_pm;			}

	//����PM �μ��ڵ�
	public void setPsmPmDiv(String psm_pm_div)		{ this.psm_pm_div = psm_pm_div;		}
	public String getPsmPmDiv()						{ return this.psm_pm_div;			}

	//���������
	public void setPsmMgr(String psm_mgr)			{ this.psm_mgr = psm_mgr;			}
	public String getPsmMgr()						{ return this.psm_mgr;				}

	//��������� �μ��ڵ�
	public void setPsmMgrDiv(String psm_mgr_div)	{ this.psm_mgr_div = psm_mgr_div;	}
	public String getPsmMgrDiv()					{ return this.psm_mgr_div;			}

	//������������
	public void setPsmBudget(String psm_budget)		{ this.psm_budget = psm_budget;		}
	public String getPsmBudget()					{ return this.psm_budget;			}

	//������������ �μ��ڵ�
	public void setPsmBudgetDiv(String psm_budget_div)	{ this.psm_budget_div = psm_budget_div;	}
	public String getPsmBudgetDiv()						{ return this.psm_budget_div;			}

	//���������
	public void setPsmUser(String psm_user)			{ this.psm_user = psm_user;		}
	public String getPsmUser()						{ return this.psm_user;			}

	//��������� �μ��ڵ�
	public void setPsmUserDiv(String psm_user_div)	{ this.psm_user_div = psm_user_div;	}
	public String getPsmUserDiv()					{ return this.psm_user_div;			}

	//��������
	public void setChangeDesc(String change_desc)	{ this.change_desc = change_desc;	}
	public String getChangeDesc()					{ return this.change_desc;			}

	//�������
	public void setPsmStatus(String psm_status)		{ this.psm_status = psm_status;		}
	public String getPsmStatus()					{ return this.psm_status;			}

	//���(����)��
	public void setChangeDate(String change_date)	{ this.change_date = change_date;	}
	public String getChangeDate()					{ return this.change_date;			}
	
	//--------------------------  Modify ���� ------------------------------------------//
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
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;			}
	public String getPageCut()						{ return this.page_cut;				}

	//��������
	public void setTotalPage(int total_page)		{ this.total_page = total_page;		}
	public int getTotalPage()						{ return this.total_page;			}

	//����������
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;	}
	public int getCurrentPage()						{ return this.current_page;			}

	//�� �׸��
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;}
	public int getTotalArticle()					{ return this.total_article;		}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}





