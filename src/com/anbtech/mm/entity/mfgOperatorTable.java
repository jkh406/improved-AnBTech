package com.anbtech.mm.entity;
public class mfgOperatorTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String assy_code;					//ASSY CODE
	private String assy_spec;					//ASSY CODE �԰�
	private int level_no;						//������ȣ
	private int mfg_count;						//��������
	private String mfg_unit;					//ǰ�����
	private String op_start_date;				//��������������
	private String op_end_date;					//�����ϷΌ����
	private String order_date;					//����Ȯ������
	private String buy_type;					//���ޱ���
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����̸�
	private String work_no;						//�۾����ȣ
	private String work_name;					//�۾����
	private String op_no;						//������ȣ
	private String op_name;						//������
	private String mfg_type;					//��������
	private String mfg_id;						//��������ڻ��
	private String mfg_name;					//��������ڸ�
	private String note;						//���
	private String comp_code;					//���ֻ����ü�ڵ�
	private String comp_name;					//���ֻ����ü��
	private String comp_user;					//���ֻ����ü�����
	private String comp_tel;					//���ֻ����ü����ó
	private String op_order;					//������ȹȮ������
	
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

	//ASSY CODE �԰�
	public void setAssySpec(String assy_spec)		{ this.assy_spec = assy_spec;		}
	public String getAssySpec()						{ return this.assy_spec;			}

	//������ȣ
	public void setLevelNo(int level_no)			{ this.level_no = level_no;			}
	public int getLevelNo()							{ return this.level_no;				}

	//��������
	public void setMfgCount(int mfg_count)			{ this.mfg_count = mfg_count;		}
	public int getMfgCount()						{ return this.mfg_count;			}

	//ǰ�����
	public void setMfgUnit(String mfg_unit)			{ this.mfg_unit = mfg_unit;			}
	public String getMfgUnit()						{ return this.mfg_unit;				}

	//��������������
	public void setOpStartDate(String op_start_date)		{ this.op_start_date = op_start_date;		}
	public String getOpStartDate()							{ return this.op_start_date;				}

	//�����ϷΌ����
	public void setOpEndDate(String op_end_date)			{ this.op_end_date = op_end_date;			}
	public String getOpEndDate()							{ return this.op_end_date;					}

	//����Ȯ������
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//���ޱ���
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}
	
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����̸�
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//�۾����ȣ
	public void setWorkNo(String work_no)			{ this.work_no = work_no;			}
	public String getWorkNo()						{ return this.work_no;				}

	//�۾����
	public void setWorkName(String work_name)		{ this.work_name = work_name;		}
	public String getWorkName()						{ return this.work_name;			}

	//������ȣ
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//������
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

	//��������
	public void setMfgType(String mfg_type)			{ this.mfg_type = mfg_type;			}
	public String getMfgType()						{ return this.mfg_type;				}

	//��������ڻ��
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//��������ڸ�
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//���
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//���ֻ����ü�ڵ�
	public void setCompCode(String comp_code)		{ this.comp_code = comp_code;		}
	public String getCompCode()						{ return this.comp_code;			}

	//���ֻ����ü��
	public void setCompName(String comp_name)		{ this.comp_name = comp_name;		}
	public String getCompName()						{ return this.comp_name;			}

	//���ֻ����ü�����
	public void setCompUser(String comp_user)		{ this.comp_user = comp_user;		}
	public String getCompUser()						{ return this.comp_user;			}

	//���ֻ����ü����ó
	public void setCompTel(String comp_tel)			{ this.comp_tel = comp_tel;			}
	public String getCompTel()						{ return this.comp_tel;				}

	//������ȹȮ������
	public void setOpOrder(String op_order)			{ this.op_order = op_order;			}
	public String getOpOrder()						{ return this.op_order;				}

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




