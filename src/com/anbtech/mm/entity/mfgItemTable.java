package com.anbtech.mm.entity;
public class mfgItemTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String assy_code;					//ASSY CODE
	private int level_no;						//������ȣ
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private String item_unit;					//ǰ�����
	private String item_type;					//item type
	private double item_loss;					//ǰ��Loss��
	private int draw_count;						//������� ����
	private int mfg_count;						//��������
	private int need_count;						//������� ���� * ��������
	private int spare_count;					//����μ��� �ܰ����
	private int add_count;						//�ҿ䷮���� ����
	private int reserve_count;					//��������
	private int request_count;					//���� ����Ƿ��� ���� ( <= �������)
	private String need_date;					//�ʿ���
	private String order_date;					//����Ȯ������
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

	//ǰ��Loss��
	public void setItemLoss(double item_loss)		{ this.item_loss = item_loss;		}
	public double getItemLoss()						{ return this.item_loss;			}

	//������� ����
	public void setDrawCount(int draw_count)		{ this.draw_count = draw_count;		}
	public int getDrawCount()						{ return this.draw_count;			}

	//��������
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//ǰ�����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//������� ���� * ��������
	public void setNeedCount(int need_count)		{ this.need_count = need_count;		}
	public int getNeedCount()						{ return this.need_count;			}

	//����μ��� �ܰ����
	public void setSpareCount(int spare_count)		{ this.spare_count = spare_count;	}
	public int getSpareCount()						{ return this.spare_count;			}

	//�ҿ䷮���� ����
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//��������
	public void setReserveCount(int reserve_count)	{ this.reserve_count = reserve_count;	}
	public int getReserveCount()					{ return this.reserve_count;			}

	//���� ����Ƿ��� ���� ( <= �������)
	public void setRequestCount(int request_count)	{ this.request_count = request_count;	}
	public int getRequestCount()					{ return this.request_count;			}

	//�ʿ���
	public void setNeedDate(String need_date)		{ this.need_date = need_date;		}
	public String getNeedDate()						{ return this.need_date;			}

	//����Ȯ������
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

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



