package com.anbtech.bm.entity;
public class mbomMasterTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String model_code;					//���ڵ�
	private String model_name;					//���̸�
	private String modelg_code;					//�𵨱��ڵ�
	private String modelg_name;					//�𵨱��̸�
	private String fg_code;						//FG�ڵ�
	private String pdg_code;					//��ǰ���ڵ�
	private String pdg_name;					//��ǰ����
	private String pd_code;						//��ǰ�ڵ�
	private String pd_name;						//��ǰ��
	private String pjt_code;					//������Ʈ�ڵ�
	private String pjt_name;					//������Ʈ��
	private String reg_id;						//����� ���
	private String reg_name;					//�����
	private String reg_date;					//�������
	private String app_id;						//������ ���
	private String app_name;					//������
	private String app_date;					//��������
	private String bom_status;					//BOM���� �� �������
	private String app_no;						//���ΰ�����ȣ
	private String m_status;					//��ǰ�������
	private String purpose;						//BOM�����뵵
	
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
		
	//�𵨱��ڵ�
	public void setModelgCode(String modelg_code)	{ this.modelg_code = modelg_code;	}
	public String getModelgCode()					{ return this.modelg_code;			}

	//�𵨱��̸�
	public void setModelgName(String modelg_name)	{ this.modelg_name = modelg_name;	}
	public String getModelgName()					{ return this.modelg_name;			}

	//���ڵ�
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//���̸�
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//FG�ڵ�
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//��ǰ���ڵ�
	public void setPdgCode(String pdg_code)			{ this.pdg_code = pdg_code;			}
	public String getPdgCode()						{ return this.pdg_code;				}

	//��ǰ����
	public void setPdgName(String pdg_name)			{ this.pdg_name = pdg_name;			}
	public String getPdgName()						{ return this.pdg_name;				}

	//��ǰ�ڵ�
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;			}
	public String getPdCode()						{ return this.pd_code;				}

	//��ǰ��
	public void setPdName(String pd_name)			{ this.pd_name = pd_name;			}
	public String getPdName()						{ return this.pd_name;				}

	//������Ʈ�ڵ�
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//������Ʈ��
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//����� ���
	public void setRegId(String reg_id)				{ this.reg_id = reg_id;				}
	public String getRegId()						{ return this.reg_id;				}

	//����̸�
	public void setRegName(String reg_name)			{ this.reg_name = reg_name;			}
	public String getRegName()						{ return this.reg_name;				}

	//�����
	public void setRegDate(String reg_date)			{ this.reg_date = reg_date;			}
	public String getRegDate()						{ return this.reg_date;				}

	//������
	public void setAppId(String app_id)				{ this.app_id = app_id;				}
	public String getAppId()						{ return this.app_id;				}

	//�����̸�
	public void setAppName(String app_name)			{ this.app_name = app_name;			}
	public String getAppName()						{ return this.app_name;				}

	//������
	public void setAppDate(String app_date)			{ this.app_date = app_date;			}
	public String getAppDate()						{ return this.app_date;				}

	//BOM����
	public void setBomStatus(String bom_status)		{ this.bom_status = bom_status;		}
	public String getBomStatus()					{ return this.bom_status;			}

	//���ι�ȣ
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}

	//��ǰ�������
	public void setMStatus(String m_status)			{ this.m_status = m_status;			}
	public String getMStatus()						{ return this.m_status;				}

	//BOM�����뵵
	public void setPurpose(String purpose)			{ this.purpose = purpose;			}
	public String getPurpose()						{ return this.purpose;				}

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
	public void setPageCut(String page_cut)				{ this.page_cut = page_cut;				}
	public String getPageCut()							{ return this.page_cut;					}

	//��������
	public void setTotalPage(int total_page)			{ this.total_page = total_page;			}
	public int getTotalPage()							{ return this.total_page;				}

	//����������
	public void setCurrentPage(int current_page)		{ this.current_page = current_page;		}
	public int getCurrentPage()							{ return this.current_page;				}

	//�� �׸��
	public void setTotalArticle(int total_article)		{ this.total_article = total_article;	}
	public int getTotalArticle()						{ return this.total_article;			}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}


