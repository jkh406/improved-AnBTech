package com.anbtech.mm.entity;
public class mfgMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String mrp_no;						//MRP�̷¹�ȣ
	private String mfg_no;						//MFG �����ڵ�
	private String model_code;					//�����
	private String model_name;					//����𵨸�
	private String fg_code;						//����FG�ڵ�
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private String item_unit;					//����
	private int mfg_count;						//������������
	private String buy_type;					//���ޱ���
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����̸�
	private String comp_code;					//ȸ���ڵ�
	private String comp_name;					//ȸ���
	private String comp_user;					//���ֻ����ü�����
	private String comp_tel;					//���ֻ����ü����ó
	private String order_status;				//���û���
	private String order_type;					//���ñ���
	private String plan_date;					//MPS��ȹ����
	private String reg_date;					//�����
	private String reg_id;						//����ڻ��
	private String reg_name;					//������̸�
	private String order_start_date;			//������������������
	private String order_end_date;				//�������������Ϸ���
	private String op_start_date;				//������ �ʰ��� ����������
	private String op_end_date;					//������ ������ �ϷΌ����
	private String order_date;					//��������Ȯ����
	private String re_work;						//���۾�
	private String link_mfg_no;					//���۾��� ���۾����ù�ȣ
	private int rst_total_count;				//��������
	private int rst_good_count;					//��ǰ����
	private int rst_bad_count;					//�ҷ�����
	private int working_count;					//�ܷ�
	private int rst_pass_count;					//ǰ���հݼ���
	private int rst_fail_count;					//ǰ���ҷ�����
	private int rst_enter_count;				//â��� �԰�� ����
		
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

	//MRP�̷¹�ȣ
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}

	//MFG �����ڵ�
	public void setMfgNo(String mfg_no)				{ this.mfg_no = mfg_no;				}
	public String getMfgNo()						{ return this.mfg_no;				}

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

	//����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//������������
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//���ޱ���
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����̸�
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//ȸ���ڵ�
	public void setCompCode(String comp_code)		{ this.comp_code = comp_code;		}
	public String getCompCode()						{ return this.comp_code;			}

	//ȸ���
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}
	
	//���ֻ����ü�����
	public void setCompUser(String comp_user)		{ this.comp_user = comp_user;		}
	public String getCompUser()						{ return this.comp_user;			}

	//���ֻ����ü����ó
	public void setCompTel(String comp_tel)			{ this.comp_tel = comp_tel;			}
	public String getCompTel()						{ return this.comp_tel;				}

	//���û���
	public void setOrderStatus(String order_status)	{ this.order_status = order_status;	}
	public String getOrderStatus()					{ return this.order_status;			}

	//���ñ���
	public void setOrderType(String order_type)		{ this.order_type = order_type;		}
	public String getOrderType()					{ return this.order_type;			}

	//MPS��ȹ����
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//�����
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//����ڻ��
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//������̸�
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//������������������
	public void setOrderStartDate(String order_start_date)	{ this.order_start_date = order_start_date;	}
	public String getOrderStartDate()						{ return this.order_start_date;				}

	//�������������Ϸ���
	public void setOrderEndDate(String order_end_date)		{ this.order_end_date = order_end_date;		}
	public String getOrderEndDate()							{ return this.order_end_date;				}

	//������ �ʰ��� ����������
	public void setOpStartDate(String op_start_date)		{ this.op_start_date = op_start_date;		}
	public String getOpStartDate()							{ return this.op_start_date;				}

	//������ ������ �ϷΌ����
	public void setOpEndDate(String op_end_date)			{ this.op_end_date = op_end_date;			}
	public String getOpEndDate()							{ return this.op_end_date;					}

	//��������Ȯ����
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//���۾�
	public void setReWork(String re_work)			{ this.re_work = re_work;			}
	public String getReWork()						{ return this.re_work;				}

	//���۾��� ���۾����ù�ȣ
	public void setLinkMfgNo(String link_mfg_no)	{ this.link_mfg_no = link_mfg_no;	}
	public String getLinkMfgNo()					{ return this.link_mfg_no;			}


	//��������
	public void setRstTotalCount(int rst_total_count){ this.rst_total_count = rst_total_count;	}
	public int getRstTotalCount()					{ return this.rst_total_count;		}

	//��ǰ����
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//�ҷ�����
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

	//�ܷ�
	public void setWorkingCount(int working_count)	{ this.working_count = working_count;	}
	public int getWorkingCount()					{ return this.working_count;			}

	//ǰ���հݼ���
	public void setRstPassCount(int rst_pass_count)	{ this.rst_pass_count = rst_pass_count;	}
	public int getRstPassCount()					{ return this.rst_pass_count;			}

	//ǰ���ҷ�����
	public void setRstFailCount(int rst_fail_count)	{ this.rst_fail_count = rst_fail_count;	}
	public int getRstFailCount()					{ return this.rst_fail_count;			}

	//â��� �԰�� ����
	public void setRstEnterCount(int rst_enter_count)	{ this.rst_enter_count = rst_enter_count;	}
	public int getRstEnterCount()						{ return this.rst_enter_count;			}

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



