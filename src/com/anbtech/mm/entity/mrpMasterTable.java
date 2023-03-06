package com.anbtech.mm.entity;
public class mrpMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String mps_no;						//MPS�̷¹�ȣ
	private String mrp_no;						//MRP�̷¹�ȣ
	private String mrp_start_date;				//��������
	private String mrp_end_date;				//Ȯ����������
	private String model_code;					//�����
	private String model_name;					//����𵨸�
	private String fg_code;						//����FG�ڵ�
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private int p_count;						//�����ǰ����
	private String plan_date;					//MPS��ȹ����
	private String item_unit;					//�ĸ����
	private String mrp_status;					//MRP�������
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����
	private String reg_date;					//�����
	private String app_date;					//������
	private String app_id;						//������ ���/�̸�
	private String reg_div_code;				//��Ϻμ��ڵ�
	private String reg_div_name;				//��Ϻμ���
	private String reg_id;						//����ڻ��
	private String reg_name;					//������̸�
	private String app_no;						//������� ������ȣ
	private String pu_dev_date;					//�����԰������
	private String pu_req_no;					//���ſ�û��ȣ
	private String stock_link;					//������ �����
	private String mfg_order;					//����������������
	private String pjt_code;					//�����ڵ�
	private String pjt_name;					//�����̸�
		
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

	//MPS�̷¹�ȣ
	public void setMpsNo(String mps_no)				{ this.mps_no = mps_no;				}
	public String getMpsNo()						{ return this.mps_no;				}

	//MRP�̷¹�ȣ
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}

	//��������
	public void setMrpStartDate(String mrp_start_date)		{ this.mrp_start_date = mrp_start_date;		}
	public String getMrpStartDate()							{ return this.mrp_start_date;				}

	//Ȯ����������
	public void setMrpEndDate(String mrp_end_date)	{ this.mrp_end_date = mrp_end_date;	}
	public String getMrpEndDate()					{ return this.mrp_end_date;			}

	//�����
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//����𵨸�
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//����FG�ڵ�
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}
	
	//ǰ���ڵ�
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//�ĸ�԰�
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//�����ǰ����
	public void setPCount(int p_count)				{ this.p_count = p_count;			}
	public int getPCount()							{ return this.p_count;				}

	//MPS��ȹ����
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//�ĸ����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//MRP�������
	public void setMrpStatus(String mrp_status)		{ this.mrp_status = mrp_status;		}
	public String getMrpStatus()					{ return this.mrp_status;			}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//�����
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//������
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//������ ���/�̸�
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//��Ϻμ��ڵ�
	public void setRegDivCode(String reg_div_code)	{ this.reg_div_code = reg_div_code;	}
	public String getRegDivCode()					{ return this.reg_div_code;			}

	//��Ϻμ���
	public void setRegDivName(String reg_div_name)	{ this.reg_div_name = reg_div_name;	}
	public String getRegDivName()					{ return this.reg_div_name;			}

	//����ڻ��
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//������̸�
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//������� ������ȣ
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//�����԰������
	public void setPuDevDate(String pu_dev_date)	{ this.pu_dev_date = pu_dev_date;	}
	public String getPuDevDate()					{ return this.pu_dev_date;			}

	//���ſ�û��ȣ
	public void setPuReqNo(String pu_req_no)		{ this.pu_req_no = pu_req_no;		}
	public String getPuReqNo()						{ return this.pu_req_no;			}
	
	//������ �����
	public void setStockLink(String stock_link)		{ this.stock_link = stock_link;		}
	public String getStockLink()					{ return this.stock_link;			}

	//����������������
	public void setMfgOrder(String mfg_order)		{ this.mfg_order = mfg_order;		}
	public String getMfgOrder()						{ return this.mfg_order;			}

	//�����ڵ�
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//�����̸�
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}
	
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


