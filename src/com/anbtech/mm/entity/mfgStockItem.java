package com.anbtech.mm.entity;
public class mfgStockItem
{
	//Table ����
	private String pid;							//�����ڵ�
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private String item_unit;					//ǰ�����
	private int stock_count;					//�����ܰ�������
	private int add_count;						//�����ܰ� ��������
	private String add_date;					//��������
	private String add_mgr;						//������
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

	//ǰ���ڵ�
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//�ĸ�԰�
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//ǰ�����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}
	
	//�����ܰ�������
	public void setStockCount(int stock_count)		{ this.stock_count = stock_count;	}
	public int getStockCount()						{ return this.stock_count;			}

	//�����ܰ� ��������
	public void setAddCount(int add_count)			{ this.add_count = add_count;		}
	public int getAddCount()						{ return this.add_count;			}

	//��������
	public void setAddDate(String add_date)			{ this.add_date = add_date;			}
	public String getAddDate()						{ return this.add_date;				}

	//������
	public void setAddMgr(String add_mgr)			{ this.add_mgr = add_mgr;			}
	public String getAddMgr()						{ return this.add_mgr;				}

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




