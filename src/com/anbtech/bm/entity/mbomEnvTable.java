package com.anbtech.bm.entity;
public class mbomEnvTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String flag;						//������
	private String m_code;						//�����ڵ�
	private String spec;						//�԰ݹ� �̸�
	private String tag;							//���
	
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

	//������
	public void setFlag(String flag)				{ this.flag = flag;					}
	public String getFlag()							{ return this.flag;					}

	//�����ڵ�
	public void setMCode(String m_code)				{ this.m_code = m_code;				}
	public String getMCode()						{ return this.m_code;				}

	//�԰ݹ� �̸�
	public void setSpec(String spec)				{ this.spec = spec;					}
	public String getSpec()							{ return this.spec;					}

	//���
	public void setTag(String tag)					{ this.tag = tag;					}
	public String getTag()							{ return this.tag;					}

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


