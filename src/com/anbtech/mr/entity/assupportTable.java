package com.anbtech.mr.entity;
public class assupportTable
{
	//��������
	private String pid;							//�����ڵ�
	private String register_no;					//��Ϲ�ȣ
	private String register_date;				//����Ͻ�
	private String as_field;					//AS�о�
	private String code;						//�μ������ڵ�
	private String request_name;				//AS��û��
	private String serial_no;					//����Ϸù�ȣ
	private String request_date;				//AS��û��
	private String as_date;						//AS�湮��
	private String as_type;						//AS����
	private String as_content;					//AS����
	private String as_result;					//ó������
	private String as_delay;					//AS��������
	private String as_issue;					//�̽�����
	private String worker;						//ASó����
	private String company_no;					//AS��ü�ڵ�
	private String value_request;				//�򰡵���Ƚ��

	//������
	private String value_date;					//������
	private String value_yn;					//������
	private String item_pid;					//���׸������ȣ
	private String score;						//���׸�����

	//���׸�
	private String score_no;					//���׸��Ϸù�ȣ
	private String score_item;					//���׸��
	private String use_yn;						//���׸��뿩��

	//���������Ǽ�
	private String division_code;				//�μ��ڵ�
	private String division_name;				//�μ��̸�
	private String company_name;				//ȸ���̸�
	private int jan_cnt;						//1�� AS�Ǽ�
	private int feb_cnt;						//2�� AS�Ǽ�
	private int apr_cnt;						//3�� AS�Ǽ�
	private int mar_cnt;						//4�� AS�Ǽ�
	private int may_cnt;						//5�� AS�Ǽ�
	private int jun_cnt;						//6�� AS�Ǽ�
	private int jul_cnt;						//7�� AS�Ǽ�
	private int aug_cnt;						//8�� AS�Ǽ�
	private int sep_cnt;						//9�� AS�Ǽ�
	private int oct_cnt;						//10�� AS�Ǽ�
	private int nov_cnt;						//11�� AS�Ǽ�
	private int dec_cnt;						//12�� AS�Ǽ�
	private int total_cnt;						//total AS�Ǽ�

	//����������
	private double jan_score;					//1�� ��������
	private double feb_score;					//2�� ��������
	private double apr_score;					//3�� ��������
	private double mar_score;					//4�� ��������
	private double may_score;					//5�� ��������
	private double jun_score;					//6�� ��������
	private double jul_score;					//7�� ��������
	private double aug_score;					//8�� ��������
	private double sep_score;					//9�� ��������
	private double oct_score;					//10�� ��������
	private double nov_score;					//11�� ��������
	private double dec_score;					//12�� ��������
	private double average_score;				//��� ��������

	//AS ITEM MASTER
	private String item_code;					//ǰ���ڵ�
	private String item_name;					//ǰ���̸�
	private String model_name;					//���̸�
	private String desc;						//�԰�
	private String in_date;						//��������
	private double in_price;					//���԰���
	private String in_type;						//���Ա���
	private double exchange_rate;				//����ȯ��
	private String assets_no;					//�ڻ��ȣ

	//�⵵�� AS ITEM
	private String contract_date;				//�������
	private double contract_price;				//���ݾ�
	private double contract_rate;				//���������
	private String contract_yn;					//������뿩��

	//ȯ�漳��
	private String mgr_name;					//������ ����Ű����
	private String register_name;				//������
	private String sno;							//������ ��ӵ� ��ȣ
	
	//event
	private String modify;						//����
	private String delete;						//����
	private String view;						//�󼼺���
	private String mail;						//�������ϱ�

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
		
	//��Ϲ�ȣ
	public void setRegisterNo(String register_no)	{ this.register_no = register_no;	}
	public String getRegisterNo()					{ return this.register_no;			}

	//����Ͻ�
	public void setRegisterDate(String register_date)	{ this.register_date = register_date;	}
	public String getRegisterDate()						{ return this.register_date;			}

	//AS�о�
	public void setAsField(String as_field)			{ this.as_field = as_field;			}
	public String getAsField()						{ return this.as_field;				}

	//�μ������ڵ�
	public void setCode(String code)				{ this.code = code;					}
	public String getCode()							{ return this.code;					}

	//AS��û��
	public void setRequestName(String request_name)	{ this.request_name = request_name;	}
	public String getRequestName()					{ return this.request_name;			}

	//����Ϸù�ȣ
	public void setSerialNo(String serial_no)		{ this.serial_no = serial_no;		}
	public String getSerialNo()						{ return this.serial_no;			}

	//AS��û��
	public void setRequestDate(String request_date)	{ this.request_date = request_date;	}
	public String getRequestDate()					{ return this.request_date;			}

	//AS�湮��
	public void setAsDate(String as_date)			{ this.as_date = as_date;			}
	public String getAsDate()						{ return this.as_date;				}

	//AS����
	public void setAsType(String as_type)			{ this.as_type = as_type;			}
	public String getAsType()						{ return this.as_type;				}

	//AS����
	public void setAsContent(String as_content)		{ this.as_content = as_content;		}
	public String getAsContent()					{ return this.as_content;			}

	//ó������
	public void setAsResult(String as_result)		{ this.as_result = as_result;		}
	public String getAsResult()						{ return this.as_result;			}

	//AS��������
	public void setAsDelay(String as_delay)			{ this.as_delay = as_delay;			}
	public String getAsDelay()						{ return this.as_delay;				}

	//�̽�����
	public void setAsIssue(String as_issue)			{ this.as_issue = as_issue;			}
	public String getAsIssue()						{ return this.as_issue;				}

	//ASó����
	public void setWorker(String worker)			{ this.worker = worker;				}
	public String getWorker()						{ return this.worker;				}

	//AS��ü�ڵ�
	public void setCompanyNo(String company_no)		{ this.company_no = company_no;		}
	public String getCompanyNo()					{ return this.company_no;			}

	//������
	public void setValueDate(String value_date)		{ this.value_date = value_date;		}
	public String getValueDate()					{ return this.value_date;			}

	//������
	public void setValueYn(String value_yn)			{ this.value_yn = value_yn;			}
	public String getValueYn()						{ return this.value_yn;				}

	//�򰡵���Ƚ��
	public void setValueRequest(String value_request)	{ this.value_request = value_request;	}
	public String getValueRequest()						{ return this.value_request;			}

	//���׸������ȣ
	public void setItemPid(String item_pid)			{ this.item_pid = item_pid;			}
	public String getItemPid()						{ return this.item_pid;				}

	//���׸�����
	public void setScore(String score)				{ this.score = score;				}
	public String getScore()						{ return this.score;				}

	//���׸��Ϸù�ȣ
	public void setScoreNo(String score_no)			{ this.score_no = score_no;			}
	public String getScoreNo()						{ return this.score_no;				}

	//���׸��
	public void setScoreItem(String score_item)		{ this.score_item = score_item;		}
	public String getScoreItem()					{ return this.score_item;			}

	//���׸��뿩��
	public void setUseYn(String use_yn)				{ this.use_yn = use_yn;				}
	public String getUseYn()						{ return this.use_yn;				}

	//�μ��ڵ�
	public void setDivisionCode(String division_code)	{ this.division_code = division_code;	}
	public String getDivisionCode()						{ return this.division_code;			}

	//�μ��̸�
	public void setDivisionName(String division_name)	{ this.division_name = division_name;	}
	public String getDivisionName()						{ return this.division_name;			}

	//ȸ���̸�
	public void setCompanyName(String company_name)		{ this.company_name = company_name;		}
	public String getCompanyName()						{ return this.company_name;				}

	//1�� AS�Ǽ�
	public void setJanCnt(int jan_cnt)				{ this.jan_cnt = jan_cnt;			}
	public int getJanCnt()							{ return this.jan_cnt;				}

	//2�� AS�Ǽ�
	public void setFebCnt(int feb_cnt)				{ this.feb_cnt = feb_cnt;			}
	public int getFebCnt()							{ return this.feb_cnt;				}

	//3�� AS�Ǽ�
	public void setAprCnt(int apr_cnt)				{ this.apr_cnt = apr_cnt;			}
	public int getAprCnt()							{ return this.apr_cnt;				}

	//4�� AS�Ǽ�
	public void setMarCnt(int mar_cnt)				{ this.mar_cnt = mar_cnt;			}
	public int getMarCnt()							{ return this.mar_cnt;				}

	//5�� AS�Ǽ�
	public void setMayCnt(int may_cnt)				{ this.may_cnt = may_cnt;			}
	public int getMayCnt()							{ return this.may_cnt;				}

	//6�� AS�Ǽ�
	public void setJunCnt(int jun_cnt)				{ this.jun_cnt = jun_cnt;			}
	public int getJunCnt()							{ return this.jun_cnt;				}

	//7�� AS�Ǽ�
	public void setJulCnt(int jul_cnt)				{ this.jul_cnt = jul_cnt;			}
	public int getJulCnt()							{ return this.jul_cnt;				}

	//8�� AS�Ǽ�
	public void setAugCnt(int aug_cnt)				{ this.aug_cnt = aug_cnt;			}
	public int getAugCnt()							{ return this.aug_cnt;				}

	//9�� AS�Ǽ�
	public void setSepCnt(int sep_cnt)				{ this.sep_cnt = sep_cnt;			}
	public int getSepCnt()							{ return this.sep_cnt;				}

	//10�� AS�Ǽ�
	public void setOctCnt(int oct_cnt)				{ this.oct_cnt = oct_cnt;			}
	public int getOctCnt()							{ return this.oct_cnt;				}

	//11�� AS�Ǽ�
	public void setNovCnt(int nov_cnt)				{ this.nov_cnt = nov_cnt;			}
	public int getNovCnt()							{ return this.nov_cnt;				}

	//12�� AS�Ǽ�
	public void setDecCnt(int dec_cnt)				{ this.dec_cnt = dec_cnt;			}
	public int getDecCnt()							{ return this.dec_cnt;				}

	//total AS�Ǽ�
	public void setTotalCnt(int total_cnt)				{ this.total_cnt = total_cnt;			}
	public int getTotalCnt()							{ return this.total_cnt;				}

	//1�� ��������
	public void setJanScore(double jan_score)		{ this.jan_score = jan_score;		}
	public double getJanScore()						{ return this.jan_score;			}

	//2�� ��������
	public void setFebScore(double feb_score)		{ this.feb_score = feb_score;		}
	public double getFebScore()						{ return this.feb_score;			}

	//3�� ��������
	public void setAprScore(double apr_score)		{ this.apr_score = apr_score;		}
	public double getAprScore()						{ return this.apr_score;			}

	//4�� ��������
	public void setMarScore(double mar_score)		{ this.mar_score = mar_score;		}
	public double getMarScore()						{ return this.mar_score;			}

	//5�� ��������
	public void setMayScore(double may_score)		{ this.may_score = may_score;		}
	public double getMayScore()						{ return this.may_score;			}

	//6�� ��������
	public void setJunScore(double jun_score)		{ this.jun_score = jun_score;		}
	public double getJunScore()						{ return this.jun_score;			}

	//7�� ��������
	public void setJulScore(double jul_score)		{ this.jul_score = jul_score;		}
	public double getJulScore()						{ return this.jul_score;			}

	//8�� ��������
	public void setAugScore(double aug_score)		{ this.aug_score = aug_score;		}
	public double getAugScore()						{ return this.aug_score;			}

	//9�� ��������
	public void setSepScore(double sep_score)		{ this.sep_score = sep_score;		}
	public double getSepScore()						{ return this.sep_score;			}

	//10�� ��������
	public void setOctScore(double oct_score)		{ this.oct_score = oct_score;		}
	public double getOctScore()						{ return this.oct_score;			}

	//11�� ��������
	public void setNovScore(double nov_score)		{ this.nov_score = nov_score;		}
	public double getNovScore()						{ return this.nov_score;			}

	//12�� ��������
	public void setDecScore(double dec_score)		{ this.dec_score = dec_score;		}
	public double getDecScore()						{ return this.dec_score;			}

	//��� ��������
	public void setAverageScore(double average_score)	{ this.average_score = average_score;	}
	public double getAverageScore()						{ return this.average_score;			}

	//ǰ���ڵ�
	public void setItemCode(String item_code)		{ this.item_code = item_code;		}
	public String getItemCode()						{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)		{ this.item_name = item_name;		}
	public String getItemName()						{ return this.item_name;			}

	//���̸�
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//�԰�
	public void setDesc(String desc)				{ this.desc = desc;					}
	public String getDesc()							{ return this.desc;					}

	//��������
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//���԰���
	public void setInPrice(double in_price)			{ this.in_price = in_price;			}
	public double getInPrice()						{ return this.in_price;				}

	//���Ա���
	public void setInType(String in_type)			{ this.in_type = in_type;			}
	public String getInType()						{ return this.in_type;				}

	//����ȯ��
	public void setExchangeRate(double exchange_rate)	{ this.exchange_rate = exchange_rate;	}
	public double getExchangeRate()						{ return this.exchange_rate;			}

	//�ڻ��ȣ
	public void setAssetsNo(String assets_no)		{ this.assets_no = assets_no;		}
	public String getAssetsNo()						{ return this.assets_no;			}

	//�������
	public void setContractDate(String contract_date)	{ this.contract_date = contract_date;	}
	public String getContractDate()						{ return this.contract_date;			}

	//���ݾ�
	public void setContractPrice(double contract_price)	{ this.contract_price = contract_price;	}
	public double getContractPrice()					{ return this.contract_price;			}

	//���������
	public void setContractRate(double contract_rate)	{ this.contract_rate = contract_rate;	}
	public double getContractRate()						{ return this.contract_rate;			}

	//������뿩��
	public void setContractYn(String contract_yn)		{ this.contract_yn = contract_yn;		}
	public String getContractYn()						{ return this.contract_yn;				}

	//������ ����Ű����
	public void setMgrName(String mgr_name)				{ this.mgr_name = mgr_name;				}
	public String getMgrName()							{ return this.mgr_name;					}

	//������
	public void setRegisterName(String register_name)	{ this.register_name = register_name;	}
	public String getRegisterName()						{ return this.register_name;			}

	//������ ��ӵ� ��ȣ
	public void setSno(String sno)					{ this.sno = sno;					}
	public String getSno()							{ return this.sno;					}

	//����
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//����
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//�󼼺���
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//�������ϱ�
	public void setMail(String mail)				{ this.mail = mail;					}
	public String getMail()							{ return this.mail;					}

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


