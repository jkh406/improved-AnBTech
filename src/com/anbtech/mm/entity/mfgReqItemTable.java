package com.anbtech.mm.entity;
public class mfgReqItemTable
{
	//-----------------------------------------------------------
	//  �������� st_reserved_item_info Table�� �ڵ帱�ϱ�����
	//  ������ Table��
	//-----------------------------------------------------------
	//Table ����
	private String pid;							//�����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String mfg_req_no;					//��ǰ����Ƿڹ�ȣ
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private String item_unit;					//ǰ�����
	private String item_type;					//item type
	private int req_count;						//��ǰ����Ƿڼ���
	private String req_date;					//�����
	private String factory_no;					//�����ȣ

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

	//MFG �����ڵ�
	public void setMfgNo(String mfg_no)				{ this.mfg_no = mfg_no;				}
	public String getMfgNo()						{ return this.mfg_no;				}

	//��ǰ����Ƿڹ�ȣ
	public void setMfgReqNo(String mfg_req_no)		{ this.mfg_req_no = mfg_req_no;		}
	public String getMfgReqNo()						{ return this.mfg_req_no;			}

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

	//ǰ�����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//��ǰ����Ƿڼ���
	public void setReqCount(int req_count)			{ this.req_count = req_count;		}
	public int getReqCount()						{ return this.req_count;			}

	//�����
	public void setReqDate(String req_date)			{ this.req_date = req_date;			}
	public String getReqDate()						{ return this.req_date;				}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

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



