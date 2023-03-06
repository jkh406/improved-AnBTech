package com.anbtech.mm.entity;
public class mpsMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String mps_no;						//MPS�̷¹�ȣ
	private String order_no;					//���ֹ�ȣ
	private String mps_type;					//MPS��������
	private String model_code;					//�����
	private String model_name;					//����𵨸�
	private String fg_code;						//����FG�ڵ�
	private String item_code;					//ǰ���ȣ
	private String item_name;					//ǰ���
	private String item_spec;					//ǰ��԰�
	private String plan_date;					//��ȹ����
	private int plan_count;						//��ȹ����
	private int sell_count;						//������
	private String item_unit;					//����
	private String mps_status;					//MPS�������
	private String factory_no;					//�����ȣ
	private String factory_name;				//�����̸�
	private String reg_date;					//�����
	private String reg_id;						//����ڻ��
	private String reg_name;					//������̸�
	private String app_date;					//������
	private String app_id;						//������ ���/�̸�
	private String app_no;						//���ι�ȣ
	private String order_comp;					//�����ֹ���ü
	
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

	//���ֹ�ȣ
	public void setOrderNo(String order_no)			{ this.order_no = order_no;			}
	public String getOrderNo()						{ return this.order_no;				}

	//MPS��������
	public void setMpsType(String mps_type)			{ this.mps_type = mps_type;			}
	public String getMpsType()						{ return this.mps_type;				}

	//�����
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//����𵨸�
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//����FG�ڵ�
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//ǰ���ȣ
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//ǰ��԰�
	public void setItemSpec(String item_spec)		{ this.item_spec = item_spec;		}
	public String getItemSpec()						{ return this.item_spec;			}

	//��ȹ����
	public void setPlanDate(String plan_date)		{ this.plan_date = plan_date;		}
	public String getPlanDate()						{ return this.plan_date;			}

	//��ȹ����
	public void setPlanCount(int plan_count)		{ this.plan_count = plan_count;		}
	public int getPlanCount()						{ return this.plan_count;			}

	//������
	public void setSellCount(int sell_count)		{ this.sell_count = sell_count;		}
	public int getSellCount()						{ return this.sell_count;			}

	//����
	public void setItemUnit(String item_unit)		{ this.item_unit = item_unit;		}
	public String getItemUnit()						{ return this.item_unit;			}

	//MPS�������
	public void setMpsStatus(String mps_status)		{ this.mps_status = mps_status;		}
	public String getMpsStatus()					{ return this.mps_status;			}

	//�����ȣ
	public void setFactoryNo(String factory_no)		{ this.factory_no = factory_no;		}
	public String getFactoryNo()					{ return this.factory_no;			}

	//�����̸�
	public void setFactoryName(String factory_name)	{ this.factory_name = factory_name;	}
	public String getFactoryName()					{ return this.factory_name;			}

	//�����
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//����ڻ��
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//������̸�
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//������
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//������ ���/�̸�
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//���ι�ȣ
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//�����ֹ���ü
	public void setOrderComp(String order_comp)		{ this.order_comp = order_comp;		}
	public String getOrderComp()					{ return this.order_comp;			}
		
	
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


