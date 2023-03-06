package com.anbtech.mm.entity;
public class mfgProductMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//Group �����ڵ�
	private String mfg_no;						//MFG �����ڵ�
	private String model_code;					//�����
	private String model_name;					//����𵨸�
	private String fg_code;						//����FG�ڵ�
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String item_spec;					//�ĸ�԰�
	private int order_count;					//��������
	private String order_unit;					//��������
	private int total_count;					//�ѻ������
	private int good_count;						//��ǰ����
	private int bad_count;						//�ҷ�����
	private String mfg_id;						//����ڻ��
	private String mfg_name;					//������̸�
	private String output_status;				//�������
	private String factory_no;					//�����ȣ
	private String output_date;					//���������
	private String op_code;						//�����ڵ�
	private String op_name;						//������
	private String op_nickname;					//������ ���

	private String product_rate;				//���������
	private String good_rate;					//��ǰ��
	private String bad_rate;					//�ҷ���

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

	//��������
	public void setOrderCount(int order_count)		{ this.order_count = order_count;	}
	public int getOrderCount()						{ return this.order_count;			}

	//��������
	public void setOrderUnit(String order_unit)		{ this.order_unit = order_unit;		}
	public String getOrderUnit()					{ return this.order_unit;			}

	//��������
	public void setTotalCount(int total_count)		{ this.total_count = total_count;	}
	public int getTotalCount()						{ return this.total_count;			}

	//��ǰ����
	public void setGoodCount(int good_count)		{ this.good_count = good_count;		}
	public int getGoodCount()						{ return this.good_count;			}

	//�ҷ�����
	public void setBadCount(int bad_count)			{ this.bad_count = bad_count;		}
	public int getBadCount()						{ return this.bad_count;			}

	//����ڻ��
	public void setMfgId(String mfg_id)				{ this.mfg_id = mfg_id;				}
	public String getMfgId()						{ return this.mfg_id;				}

	//������̸�
	public void setMfgName(String mfg_name)			{ this.mfg_name = mfg_name;			}
	public String getMfgName()						{ return this.mfg_name;				}

	//�������
	public void setOutputStatus(String output_status)	{ this.output_status = output_status;		}
	public String getOutputStatus()						{ return this.output_status;				}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//���������
	public void setOutputDate(String output_date)	{ this.output_date = output_date;	}
	public String getOutputDate()					{ return this.output_date;			}

	//�����ڵ�
	public void setOpCode(String op_code)			{ this.op_code = op_code;			}
	public String getOpCode()						{ return this.op_code;				}

	//������
	public void setOpName(String op_name)			{ this.op_name = op_name;			}
	public String getOpName()						{ return this.op_name;				}

	//������ ���
	public void setOpNickname(String op_nickname)	{ this.op_nickname = op_nickname;	}
	public String getOpNickname()					{ return this.op_nickname;			}

	//���������
	public void setProductRate(String product_rate)	{ this.product_rate = product_rate;	}
	public String getProductRate()					{ return this.product_rate;			}

	//��ǰ��
	public void setGoodRate(String good_rate)		{ this.good_rate = good_rate;		}
	public String getGoodRate()						{ return this.good_rate;			}

	//�ҷ���
	public void setBadRate(String bad_rate)			{ this.bad_rate = bad_rate;			}
	public String getBadRate()						{ return this.bad_rate;				}

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



