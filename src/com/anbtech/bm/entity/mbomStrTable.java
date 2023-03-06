package com.anbtech.bm.entity;
public class mbomStrTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String gid;							//�׷�����ڵ�
	private String parent_code;					//��ǰ���ڵ�
	private String child_code;					//��ǰ���ڵ�
	private String level_no;					//������ȣ
	private String part_name;					//��ǰ��
	private String part_spec;					//��ǰƯ��
	private String location;					//��ġ����
	private String op_code;						//�����ڵ�
	private String qty_unit;					//��������
	private String qty;							//����
	private String maker_name;					//����Ŀ��
	private String maker_code;					//����(����Ŀ�ڵ�)
	private String price_unit;					//���ݴ���
	private String price;						//����
	private String add_date;					//�����
	private String buy_type;					//���/����
	private String eco_no;						//���躯�������ȣ
	private String adtag;						//��ǰ�߰�/����
	private String bom_start_date;				//��ȿ������
	private String bom_end_date;				//��ȿ������
	private String app_status;					//�������
	private String tag;							//ǰ���ڵ� ����(template���� ����)
	private String part_type;					//��ǰ���� Assy�ڵ����� �Ǵ��ϱ�
	private String assy_dup;					//Assy�ڵ��ߺ��Է� ǥ��
	private String item_type;					//ITEM TYPE

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
		
	//�𵨱׷������ȣ
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//��ǰ���ڵ�
	public void setParentCode(String parent_code)	{ this.parent_code = parent_code;	}
	public String getParentCode()					{ return this.parent_code;			}

	//��ǰ���ڵ�
	public void setChildCode(String child_code)		{ this.child_code = child_code;		}
	public String getChildCode()					{ return this.child_code;			}

	//������ȣ
	public void setLevelNo(String level_no)			{ this.level_no = level_no;			}
	public String getLevelNo()						{ return this.level_no;				}

	//��ǰ��
	public void setPartName(String part_name)		{ this.part_name = part_name;		}
	public String getPartName()						{ return this.part_name;			}

	//��ǰƯ��
	public void setPartSpec(String part_spec)		{ this.part_spec = part_spec;		}
	public String getPartSpec()						{ return this.part_spec;			}

	//��ġ����
	public void setLocation(String location)		{ this.location = location;			}
	public String getLocation()						{ return this.location;				}

	//�����ڵ�
	public void setOpCode(String op_code)			{ this.op_code = op_code;			}
	public String getOpCode()						{ return this.op_code;				}

	//��������
	public void setQtyUnit(String qty_unit)			{ this.qty_unit = qty_unit;			}
	public String getQtyUnit()						{ return this.qty_unit;				}

	//����Ŀ��
	public void setMakerName(String maker_name)		{ this.maker_name = maker_name;		}
	public String getMakerName()					{ return this.maker_name;			}

	//����(����Ŀ�ڵ�)
	public void setMakerCode(String maker_code)		{ this.maker_code = maker_code;		}
	public String getMakerCode()					{ return this.maker_code;			}

	//����
	public void setQty(String qty)					{ this.qty = qty;					}
	public String getQty()							{ return this.qty;					}

	//�����
	public void setAddDate(String add_date)			{ this.add_date = add_date;			}
	public String getAddDate()						{ return this.add_date;				}

	//���/����
	public void setBuyType(String buy_type)			{ this.buy_type = buy_type;			}
	public String getBuyType()						{ return this.buy_type;				}

	//���ݴ���
	public void setPriceUnit(String price_unit)		{ this.price_unit = price_unit;		}
	public String getPriceUnit()					{ return this.price_unit;			}

	//����
	public void setPrice(String price)				{ this.price = price;				}
	public String getPrice()						{ return this.price;				}

	//��ǰ�߰�/����
	public void setAdTag(String adtag)				{ this.adtag = adtag;				}
	public String getAdTag()						{ return this.adtag;				}

	//���躯�������ȣ
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}

	//��ȿ������
	public void setBomStartDate(String bom_start_date)	{ this.bom_start_date = bom_start_date;		}
	public String getBomStartDate()						{ return this.bom_start_date;				}

	//��ȿ������
	public void setBomEndDate(String bom_end_date)		{ this.bom_end_date = bom_end_date;			}
	public String getBomEndDate()						{ return this.bom_end_date;					}

	//�������
	public void setAppStatus(String app_status)			{ this.app_status = app_status;				}
	public String getAppStatus()						{ return this.app_status;					}

	//ǰ���ڵ� ����(template���� ����)
	public void setTag(String tag)					{ this.tag = tag;					}
	public String getTag()							{ return this.tag;					}

	//Assy�ڵ��ߺ��Է� ǥ��
	public void setAssyDup(String assy_dup)			{ this.assy_dup = assy_dup;			}
	public String getAssyDup()						{ return this.assy_dup;				}

	//ITEM TYPE
	public void setItemType(String item_type)		{ this.item_type = item_type;		}
	public String getItemType()						{ return this.item_type;			}

	//--------------- ���� ���� ---------------------------//

	//��ǰ���� Assy�ڵ����� �Ǵ��ϱ�
	public void setPartType(String part_type)		{ this.part_type = part_type;		}
	public String getPartType()						{ return this.part_type;			}

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


