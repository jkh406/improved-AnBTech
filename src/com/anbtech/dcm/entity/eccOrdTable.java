package com.anbtech.dcm.entity;
public class eccOrdTable
{
	//ECC_ORD �׸� 
	private String pid;							//�����ڵ�
	private String chg_position;				//�������
	private String trouble;						//�����з�
	private String condition;					//����׿���
	private String solution;					//��å
	private String fname;						//÷�����Ͽ����̸�
	private String sname;						//÷�����������̸�
	private String ftype;						//÷������Ȯ���ڸ�
	private String fsize;						//÷������ File Size
	private String app_no;						//������ι�ȣ
	
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
		
	//�������
	public void setChgPosition(String chg_position)	{ this.chg_position = chg_position;	}
	public String getChgPosition()					{ return this.chg_position;			}

	//�����з�
	public void setTrouble(String trouble)			{ this.trouble = trouble;			}
	public String getTrouble()						{ return this.trouble;				}

	//����׿���
	public void setCondition(String condition)		{ this.condition = condition;		}
	public String getCondition()					{ return this.condition;			}

	//��å
	public void setSolution(String solution)		{ this.solution = solution;			}
	public String getSolution()						{ return this.solution;				}

	//÷�����Ͽ����̸�
	public void setFname(String fname)				{ this.fname = fname;				}
	public String getFname()						{ return this.fname;				}

	//÷�����������̸�
	public void setSname(String sname)				{ this.sname = sname;				}
	public String getSname()						{ return this.sname;				}

	//÷������Ȯ���ڸ�
	public void setFtype(String ftype)				{ this.ftype = ftype;				}
	public String getFtype()						{ return this.ftype;				}

	//÷������ File Size
	public void setFsize(String fsize)				{ this.fsize = fsize;				}
	public String getFsize()						{ return this.fsize;				}

	//������ι�ȣ
	public void setAppNo(String app_no)				{ this.app_no = app_no;				}
	public String getAppNo()						{ return this.app_no;				}


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




