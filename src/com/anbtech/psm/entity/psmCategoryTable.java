package com.anbtech.psm.entity;
public class psmCategoryTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String korea_name;					//ī�װ� �ѱ۸�
	private String english_name;				//ī�װ� ������
	private String key_word;					//ī�װ� �������
	private String comp_no;						//��ü����ڵ�Ϲ�ȣ
	private String comp_korea;					//��ü �ѱ۸�
	private String comp_english;				//��ü ������
	
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

	//ī�װ� �ѱ۸�
	public void setKoreaName(String korea_name)		{ this.korea_name = korea_name;		}
	public String getKoreaName()					{ return this.korea_name;			}

	//ī�װ� ������
	public void setEnglishName(String english_name)	{ this.english_name = english_name;	}
	public String getEnglishName()					{ return this.english_name;			}

	//ī�װ� �������
	public void setKeyWord(String key_word)			{ this.key_word = key_word;			}
	public String getKeyWord()						{ return this.key_word;				}

	//��ü����ڵ�Ϲ�ȣ
	public void setCompNo(String comp_no)			{ this.comp_no = comp_no;			}
	public String getCompNo()						{ return this.comp_no;				}

	//��ü �ѱ۸�
	public void setCompKorea(String comp_korea)		{ this.comp_korea = comp_korea;		}
	public String getCompKorea()					{ return this.comp_korea;			}

	//��ü ������
	public void setCompEnglish(String comp_english)	{ this.comp_english = comp_english;	}
	public String getCompEnglish()					{ return this.comp_english;			}
		
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



