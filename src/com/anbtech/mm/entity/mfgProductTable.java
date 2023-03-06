package com.anbtech.mm.entity;
public class mfgProductTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String work_order_no;				//�۾����ù�ȣ
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private int order_count;					//��������
	private String order_unit;					//��������
	private int rst_total_count;				//�ѻ������
	private int rst_good_count;					//��ǰ����
	private int rst_bad_count;					//�ҷ�����
	private String worker;						//����ڻ��/�̸�
	private String output_date;					//���������
	private String op_no;						//�����ڵ�
	private String bad_type;					//�ҷ�����
	private String bad_note;					//�ҷ�����

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

	//�۾����ù�ȣ
	public void setWorkOrderNo(String work_order_no){ this.work_order_no = work_order_no;	}
	public String getWorkOrderNo()					{ return this.work_order_no;			}

	//ǰ���ڵ�
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//�ĸ�԰�
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//��������
	public void setOrderCount(int order_count)		{ this.order_count = order_count;	}
	public int getOrderCount()						{ return this.order_count;			}

	//��������
	public void setOrderUnit(String order_unit)		{ this.order_unit = order_unit;		}
	public String getOrderUnit()					{ return this.order_unit;			}

	//��������
	public void setRstTotalCount(int rst_total_count){ this.rst_total_count = rst_total_count;	}
	public int getRstTotalCount()					{ return this.rst_total_count;		}

	//��ǰ����
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//�ҷ�����
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

	//����ڻ��/�̸�
	public void setWorker(String worker)			{ this.worker = worker;				}
	public String getWorker()						{ return this.worker;				}

	//���������
	public void setOutputDate(String output_date)	{ this.output_date = output_date;	}
	public String getOutputDate()					{ return this.output_date;			}

	//�����ڵ�
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//�ҷ�����
	public void setBadType(String bad_type)			{ this.bad_type = bad_type;			}
	public String getBadType()						{ return this.bad_type;				}

	//�ҷ�����
	public void setBadNote(String bad_note)			{ this.bad_note = bad_note;			}
	public String getBadNote()						{ return this.bad_note;				}

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



