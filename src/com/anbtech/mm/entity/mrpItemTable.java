package com.anbtech.mm.entity;
public class mrpItemTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mrp_no;						//MRP �̷¹�ȣ
	private String assy_code;					//ASSY CODE
	private int level_no;						//������ȣ
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private String item_type;					//item type
	private int draw_count;						//������� ����
	private int mrp_count;						//MRP ����
	private int need_count;						//�����ʿ����
	private int stock_count;					//����� ����
	private int open_count;						//�԰� ��������
	private int plan_count;						//�������
	private int add_count;						//�߰�����
	private int mrs_count;						//���ż���
	private String item_unit;					//����
	private String buy_type;					//���ޱ���
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����̸�
	private String pu_dev_date;					//�����԰������
	private String pu_req_no;					//���ſ�û��ȣ
		
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

	//MRP �̷¹�ȣ
	public void setMrpNo(String mrp_no)				{ this.mrp_no = mrp_no;				}
	public String getMrpNo()						{ return this.mrp_no;				}
	
	//ASSY CODE
	public void setAssyCode(String assy_code)		{ this.assy_code = assy_code;		}
	public String getAssyCode()						{ return this.assy_code;			}

	//������ȣ
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//ǰ���ڵ�
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//�ĸ�԰�
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//item type
	public void setItemType(String item_type)		{ this.item_type = item_type;		}
	public String getItemType()						{ return this.item_type;			}

	//������� ����
	public void setDrawCount(int draw_count)		{ this.draw_count = draw_count;		}
	public int getDrawCount()						{ return this.draw_count;			}

	//MRP ����
	public void setMrpCount(int mrp_count)			{ this.mrp_count = mrp_count;		}
	public int getMrpCount()						{ return this.mrp_count;			}

	//�����ʿ����
	public void setNeedCount(int need_count)			{ this.need_count = need_count;		}
	public int getNeedCount()						{ return this.need_count;			}

	//����� ����
	public void setStockCount(int stock_count)		{ this.stock_count = stock_count;	}
	public int getStockCount()						{ return this.stock_count;			}

	//�԰� ��������
	public void setOpenCount(int open_count)		{ this.open_count = open_count;		}
	public int getOpenCount()						{ return this.open_count;			}

	//�������
	public void setPlanCount(int plan_count)		{ this.plan_count = plan_count;		}
	public int getPlanCount()						{ return this.plan_count;			}

	//�߰�����
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//���ż���
	public void setMrsCount(int mrs_count)			{ this.mrs_count = mrs_count;		}
	public int getMrsCount()						{ return this.mrs_count;			}

	//����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//���ޱ���
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����̸�
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//�����԰������
	public void setPuDevDate(String pu_dev_date)	{ this.pu_dev_date = pu_dev_date;	}
	public String getPuDevDate()					{ return this.pu_dev_date;			}

	//���ſ�û��ȣ
	public void setPuReqNo(String pu_req_no)		{ this.pu_req_no = pu_req_no;		}
	public String getPuReqNo()						{ return this.pu_req_no;			}

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



