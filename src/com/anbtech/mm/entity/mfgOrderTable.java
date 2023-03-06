package com.anbtech.mm.entity;
public class mfgOrderTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String work_order_no;				//�۾����ù�ȣ
	private int order_count;					//��������
	private String work_no;						//�۾����ڵ�
	private String work_name;					//�۾����
	private String op_no;						//�����ڵ�
	private String op_name;						//������
	private String comp_code;					//���ֻ����ü�ڵ�
	private String comp_name;					//���ֻ����ü��
	private String comp_user;					//���ֻ����ü�����
	private String comp_tel;					//���ֻ����ü����ó
	private String mfg_type;					//�系�ܱ���
	private String work_order_status;			//���û���
	private String order_start_date;			//����������
	private String order_end_date;				//�ϷΌ����
	private String order_date;					//�۾�������
	private String mfg_id;						//��������ڻ��
	private String mfg_name;					//��������ڸ�
	private String note;						//����Ư�̻���
	private int rst_good_count;					//�ѻ��귮�߾�ǰ����
	private int rst_bad_count;					//�ѻ��귮�ߺҷ�����
	
	
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

	//��������
	public void setOrderCount(int order_count)		{ this.order_count = order_count;	}
	public int getOrderCount()						{ return this.order_count;			}

	//�۾����ڵ�
	public void setWorkNo(String work_no)			{ this.work_no = work_no;			}
	public String getWorkNo()						{ return this.work_no;				}

	//�۾����
	public void setWorkName(String work_name)		{ this.work_name = work_name;		}
	public String getWorkName()						{ return this.work_name;			}

	//�����ڵ�
	public void setOpNo(String op_no)				{ this.op_no = op_no;				}
	public String getOpNo()							{ return this.op_no;				}

	//������
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

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

	//�系�ܱ���
	public void setMfgType(String mfg_type)			{ this.mfg_type = mfg_type;			}
	public String getMfgType()						{ return this.mfg_type;				}

	//���û���
	public void setWorkOrderStatus(String work_order_status)	{ this.work_order_status = work_order_status;	}
	public String getWorkOrderStatus()							{ return this.work_order_status;				}

	//����������
	public void setOrderStartDate(String order_start_date)		{ this.order_start_date = order_start_date;		}
	public String getOrderStartDate()							{ return this.order_start_date;					}

	//�ϷΌ����
	public void setOrderEndDate(String order_end_date)			{ this.order_end_date = order_end_date;		}
	public String getOrderEndDate()								{ return this.order_end_date;				}

	//�۾�������
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//��������ڻ��
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//��������ڸ�
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//����Ư�̻���
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//�ѻ��귮�߾�ǰ����
	public void setRstGoodCount(int rst_good_count)	{ this.rst_good_count = rst_good_count;	}
	public int getRstGoodCount()					{ return this.rst_good_count;			}

	//�ѻ��귮�ߺҷ�����
	public void setRstBadCount(int rst_bad_count)	{ this.rst_bad_count = rst_bad_count;	}
	public int getRstBadCount()						{ return this.rst_bad_count;			}

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




