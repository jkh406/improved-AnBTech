package com.anbtech.psm.entity;
public class psmMasterTable
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
	private String psm_desc;					//��������
	private String plan_sum;					//������ȹ�Ѱ�
	private String plan_labor;					//��ȹ�ΰǺ�
	private String plan_material;				//��ȹ����
	private String plan_cost;					//��ȹ���
	private String plan_plant;					//��ȹ�ü���

	private String result_sum;					//�����Ѱ�
	private String result_labor;				//�����ΰǺ�
	private String result_material;				//��������
	private String result_cost;					//�������
	private String result_plant;				//�����ü���

	private String diff_sum;					//�Ѱ� ����
	private String diff_labor;					//�ΰǺ� ����
	private String diff_material;				//���� ����
	private String diff_cost;					//��� ����
	private String diff_plant;					//�ü��� ����

	private String contract_date;				//�����
	private String contract_name;				//����
	private String contract_price;				//���ݾ�
	private String complete_date;				//�ذ���
	private String fname;						//���Ͽ�����
	private String sname;						//���������
	private String ftype;						//����Ÿ��
	private String fsize;						//����ũ��
	private String psm_status;					//�������
	private String reg_date;					//���(����)��
	private String app_date;					//Ȯ����

	private String pd_code;						//��ǰ�ڵ�
	private String pd_name;						//��ǰ��
	private String psm_kind;					//��������
	private String psm_view;					//������ȸ����
	private String link_code;					//���������ũ�Ƶ� 
	
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
	public void setPsmDesc(String psm_desc)			{ this.psm_desc = psm_desc;		}
	public String getPsmDesc()						{ return this.psm_desc;			}

	//������ȹ�Ѱ�
	public void setPlanSum(String plan_sum)			{ this.plan_sum = plan_sum;		}
	public String getPlanSum()						{ return this.plan_sum;			}

	//��ȹ�ΰǺ�
	public void setPlanLabor(String plan_labor)		{ this.plan_labor = plan_labor;	}
	public String getPlanLabor()					{ return this.plan_labor;		}

	//��ȹ����
	public void setPlanMaterial(String plan_material)	{ this.plan_material = plan_material;	}
	public String getPlanMaterial()						{ return this.plan_material;			}

	//��ȹ���
	public void setPlanCost(String plan_cost)			{ this.plan_cost = plan_cost;			}
	public String getPlanCost()							{ return this.plan_cost;				}

	//��ȹ�ü���
	public void setPlanPlant(String plan_plant)			{ this.plan_plant = plan_plant;			}
	public String getPlanPlant()						{ return this.plan_plant;				}

	//�����Ѱ�
	public void setResultSum(String result_sum)			{ this.result_sum = result_sum;			}
	public String getResultSum()						{ return this.result_sum;				}

	//�����ΰǺ�
	public void setResultLabor(String result_labor)		{ this.result_labor = result_labor;		}
	public String getResultLabor()						{ return this.result_labor;				}

	//��������
	public void setResultMaterial(String result_material)	{ this.result_material = result_material;	}
	public String getResultMaterial()						{ return this.result_material;				}

	//�������
	public void setResultCost(String result_cost)			{ this.result_cost = result_cost;			}
	public String getResultCost()							{ return this.result_cost;					}

	//�����ü���
	public void setResultPlant(String result_plant)			{ this.result_plant = result_plant;			}
	public String getResultPlant()							{ return this.result_plant;					}

	//���� �Ѱ�
	public void setDiffSum(String diff_sum)				{ this.diff_sum = diff_sum;				}
	public String getDiffSum()							{ return this.diff_sum;					}

	//���� �ΰǺ�
	public void setDiffLabor(String diff_labor)			{ this.diff_labor = diff_labor;			}
	public String getDiffLabor()						{ return this.diff_labor;				}

	//���� ����
	public void setDiffMaterial(String diff_material)	{ this.diff_material = diff_material;	}
	public String getDiffMaterial()						{ return this.diff_material;			}

	//���� ���
	public void setDiffCost(String diff_cost)			{ this.diff_cost = diff_cost;			}
	public String getDiffCost()							{ return this.diff_cost;				}

	//���� �ü���
	public void setDiffPlant(String diff_plant)			{ this.diff_plant = diff_plant;			}
	public String getDiffPlant()						{ return this.diff_plant;				}

	//�����
	public void setContractDate(String contract_date)	{ this.contract_date = contract_date;		}
	public String getContractDate()						{ return this.contract_date;				}

	//����
	public void setContractName(String contract_name)	{ this.contract_name = contract_name;		}
	public String getContractName()						{ return this.contract_name;				}

	//���ݾ�
	public void setContractPrice(String contract_price)	{ this.contract_price = contract_price;		}
	public String getContractPrice()					{ return this.contract_price;				}

	//�ذ���
	public void setCompleteDate(String complete_date)	{ this.complete_date = complete_date;		}
	public String getCompleteDate()						{ return this.complete_date;				}

	//���Ͽ�����
	public void setFname(String fname)				{ this.fname = fname;			}
	public String getFname()						{ return this.fname;			}

	//���������
	public void setSname(String sname)				{ this.sname = sname;			}
	public String getSname()						{ return this.sname;			}

	//����Ÿ��
	public void setFtype(String ftype)				{ this.ftype = ftype;			}
	public String getFtype()						{ return this.ftype;			}

	//����ũ��
	public void setFsize(String fsize)				{ this.fsize = fsize;			}
	public String getFsize()						{ return this.fsize;			}

	//�������
	public void setPsmStatus(String psm_status)		{ this.psm_status = psm_status;		}
	public String getPsmStatus()					{ return this.psm_status;			}

	//���(����)��
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;		}
	public String getRegDate()						{ return this.reg_date;			}

	//Ȯ����
	public void setAppDate(String app_date)			{ this.app_date = app_date;		}
	public String getAppDate()						{ return this.app_date;			}

	//��ǰ�ڵ�
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;		}
	public String getPdCode()						{ return this.pd_code;			}

	//��ǰ��
	public void setPdName(String pd_name)			{ this.pd_name = pd_name;		}
	public String getPdName()						{ return this.pd_name;			}

	//��������
	public void setPsmKind(String psm_kind)			{ this.psm_kind = psm_kind;		}
	public String getPsmKind()						{ return this.psm_kind;			}

	//������ȸ����
	public void setPsmView(String psm_view)			{ this.psm_view = psm_view;		}
	public String getPsmView()						{ return this.psm_view;			}

	//���������ũ�Ƶ� 
	public void setLinkCode(String link_code)		{ this.link_code = link_code;	}
	public String getLinkCode()						{ return this.link_code;		}


	
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




