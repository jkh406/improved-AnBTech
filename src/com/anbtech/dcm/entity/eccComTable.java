package com.anbtech.dcm.entity;
public class eccComTable
{
	//ECC_COM �����׸� 
	private String pid;							//�����ڵ�
	private String ecc_subject;					//����
	private String eco_no;						//�����ȣ
	private String ecr_id;						//�����ڻ��
	private String ecr_name;					//�������̸�
	private String ecr_code;					//�����ںμ������ڵ�
	private String ecr_div_code;				//�����ںμ��ڵ�
	private String ecr_div_name;				//�����ںμ���
	private String ecr_tel;						//��������ȭ��ȣ
	private String ecr_date;					//��������
	private String mgr_id;						//�������å���ڻ��
	private String mgr_name;					//�������å�����̸�
	private String mgr_code;					//�������å���ںμ������ڵ�
	private String mgr_div_code;				//�������å���ںμ��ڵ�
	private String mgr_div_name;				//�������å���ںμ��̸�
	private String eco_id;						//����������ڻ��
	private String eco_name;					//������������̸�
	private String eco_code;					//����������ںμ������ڵ�
	private String eco_div_code;				//����������ںμ��ڵ�
	private String eco_div_name;				//����������ںμ��̸�
	private String eco_tel;						//�������������ȭ��ȣ
	private String ecc_reason;					//��������
	private String ecc_factor;					//���뱸��
	private String ecc_scope;					//�������
	private String ecc_kind;					//��������
	private String pdg_code;					//��ǰ��
	private String pd_code;						//��ǰ
	private String fg_code;						//�߻���(FG)�ڵ�
	private String part_code;					//�߻���ǰ�ڵ�
	private String order_date;					//��������
	private String fix_date;					//Ȯ������
	private String ecc_status;					//�������

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
		
	//���躯������
	public void setEccSubject(String ecc_subject)	{ this.ecc_subject = ecc_subject;	}
	public String getEccSubject()					{ return this.ecc_subject;			}

	//ECO NO
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}

	//������ ���
	public void setEcrId(String ecr_id)				{ this.ecr_id = ecr_id;				}
	public String getEcrId()						{ return this.ecr_id;				}

	//������ �̸�
	public void setEcrName(String ecr_name)			{ this.ecr_name = ecr_name;			}
	public String getEcrName()						{ return this.ecr_name;				}

	//������ �μ������ڵ�
	public void setEcrCode(String ecr_code)			{ this.ecr_code = ecr_code;			}
	public String getEcrCode()						{ return this.ecr_code;				}

	//������ �μ��ڵ�
	public void setEcrDivCode(String ecr_div_code)	{ this.ecr_div_code = ecr_div_code;	}
	public String getEcrDivCode()					{ return this.ecr_div_code;			}

	//������ �μ���
	public void setEcrDivName(String ecr_div_name)	{ this.ecr_div_name = ecr_div_name;	}
	public String getEcrDivName()					{ return this.ecr_div_name;			}

	//������ ��ȭ��ȣ
	public void setEcrTel(String ecr_tel)			{ this.ecr_tel = ecr_tel;			}
	public String getEcrTel()						{ return this.ecr_tel;				}

	//������ ��������
	public void setEcrDate(String ecr_date)			{ this.ecr_date = ecr_date;			}
	public String getEcrDate()						{ return this.ecr_date;				}

	//�������å���ڻ��
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//�������å�����̸�
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//�������å���ںμ������ڵ�
	public void setMgrCode(String mgr_code)			{ this.mgr_code = mgr_code;			}
	public String getMgrCode()						{ return this.mgr_code;				}

	//�������å���ںμ��ڵ�
	public void setMgrDivCode(String mgr_div_code)	{ this.mgr_div_code = mgr_div_code;	}
	public String getMgrDivCode()					{ return this.mgr_div_code;			}

	//�������å���ںμ��̸�
	public void setMgrDivName(String mgr_div_name)	{ this.mgr_div_name = mgr_div_name;	}
	public String getMgrDivName()					{ return this.mgr_div_name;			}

	//����������� ���
	public void setEcoId(String eco_id)				{ this.eco_id = eco_id;				}
	public String getEcoId()						{ return this.eco_id;				}

	//����������� �̸�
	public void setEcoName(String eco_name)			{ this.eco_name = eco_name;			}
	public String getEcoName()						{ return this.eco_name;				}

	//����������� �μ������ڵ�
	public void setEcoCode(String eco_code)			{ this.eco_code = eco_code;			}
	public String getEcoCode()						{ return this.eco_code;				}

	//����������� �μ��ڵ�
	public void setEcoDivCode(String eco_div_code)	{ this.eco_div_code = eco_div_code;	}
	public String getEcoDivCode()					{ return this.eco_div_code;			}

	//����������� �μ���
	public void setEcoDivName(String eco_div_name)	{ this.eco_div_name = eco_div_name;	}
	public String getEcoDivName()					{ return this.eco_div_name;			}

	//����������� �μ���ȭ��ȣ
	public void setEcoTel(String eco_tel)			{ this.eco_tel = eco_tel;			}
	public String getEcoTel()						{ return this.eco_tel;				}

	//��������
	public void setEccReason(String ecc_reason)		{ this.ecc_reason = ecc_reason;		}
	public String getEccReason()					{ return this.ecc_reason;			}

	//���뱸��
	public void setEccFactor(String ecc_factor)		{ this.ecc_factor = ecc_factor;		}
	public String getEccFactor()					{ return this.ecc_factor;			}

	//�������
	public void setEccScope(String ecc_scope)		{ this.ecc_scope = ecc_scope;		}
	public String getEccScope()						{ return this.ecc_scope;			}

	//��������
	public void setEccKind(String ecc_kind)			{ this.ecc_kind = ecc_kind;			}
	public String getEccKind()						{ return this.ecc_kind;				}

	//��ǰ��
	public void setPdgCode(String pdg_code)			{ this.pdg_code = pdg_code;			}
	public String getPdgCode()						{ return this.pdg_code;				}

	//��ǰ
	public void setPdCode(String pd_code)			{ this.pd_code = pd_code;			}
	public String getPdCode()						{ return this.pd_code;				}

	//�߻���(FG)�ڵ�
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//�߻���ǰ�ڵ�
	public void setPartCode(String part_code)		{ this.part_code = part_code;		}
	public String getPartCode()						{ return this.part_code;			}

	//��������
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//Ȯ������
	public void setFixDate(String fix_date)			{ this.fix_date = fix_date;			}
	public String getFixDate()						{ return this.fix_date;				}

	//�������
	public void setEccStatus(String ecc_status)		{ this.ecc_status = ecc_status;		}
	public String getEccStatus()					{ return this.ecc_status;			}

    //--------------------------  Modify ���� ------------------------------------------//
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
	public void setPageCut(String page_cut)			{ this.page_cut = page_cut;			}
	public String getPageCut()						{ return this.page_cut;				}

	//��������
	public void setTotalPage(int total_page)		{ this.total_page = total_page;		}
	public int getTotalPage()						{ return this.total_page;			}

	//����������
	public void setCurrentPage(int current_page)	{ this.current_page = current_page;	}
	public int getCurrentPage()						{ return this.current_page;			}

	//�� �׸��
	public void setTotalArticle(int total_article)	{ this.total_article = total_article;}
	public int getTotalArticle()					{ return this.total_article;		}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

}


