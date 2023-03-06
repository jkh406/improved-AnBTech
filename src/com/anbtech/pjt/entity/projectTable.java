package com.anbtech.pjt.entity;
public class projectTable
{
	private String pid;						//�����ڵ�
	private String pjt_code;				//project �ڵ�
	private String pjt_name;				//project �̸�
	private String owner;					//����������
	private String in_date;					//�ۼ���
	private String pjt_mbr_id;				//����PM/PL���/�̸�
	private String pjt_class;				//�������/����
	private String pjt_target;				//���߸�ǥ
	private String mgt_plan;				//�濵��ȹ
	private String parent_code;				//��ȹ������,���ΰ���
	private int mbr_exp;					//���u�η��ο�
	private String cost_exp;				//���߿��� �Ѻ��(��)
	private String cost_rst;				//���߽��� �Ѻ��(��)
	private String dif_cost;				//����
	private String plan_start_date;			//��ȹ������
	private String plan_end_date;			//��ȹ������
	private String chg_start_date;			//����������
	private String chg_end_date;			//����������
	private String rst_start_date;			//����������
	private String rst_end_date;			//����������
	private String prs_code;				//ǥ�����μ����ڵ�
	private String prs_type;				//���μ������� P:����, �μ��ڵ�:�μ�
	private String pjt_desc;				//��������
	private String pjt_spec;				//���߳���
	private String pjt_status;				//�����������
	private String flag;					//������ο���
	private String plan_labor;				//�ΰǺ�(��ȹ)
	private String plan_sample;				//�������ۺ�(��ȹ)
	private String plan_metal;				//������(��ȹ)
	private String plan_mup;				//M/UP���ڰ��(��ȹ)
	private String plan_oversea;			//�ؿܱ԰ݽ��κ�(��ȹ)
	private String plan_plant;				//�ü����ں�(��ȹ)
	private String result_labor;			//�������ۺ�(����)
	private String result_sample;			//�������ۺ�(����)
	private String result_metal;			//������(����)
	private String result_mup;				//M/UP���ڰ��(����)
	private String result_oversea;			//�ؿܱ԰ݽ��κ�(����)
	private String result_plant;			//�ؿܱ԰ݽ��κ�(����)

	private String plan_labor_ac;			//�ΰǺ�(��ȹ account count �׷�����)
	private String plan_sample_ac;			//�������ۺ�(��ȹ account count �׷�����)
	private String plan_metal_ac;			//������(��ȹ account count �׷�����)
	private String plan_mup_ac;				//M/UP���ڰ��(��ȹ account count �׷�����)
	private String plan_oversea_ac;			//�ؿܱ԰ݽ��κ�(��ȹ account count �׷�����)
	private String plan_plant_ac;			//�ü����ں�(��ȹ account count �׷�����)
	private String result_labor_ac;			//�������ۺ�(���� account count �׷�����)
	private String result_sample_ac;		//�������ۺ�(���� account count �׷�����)
	private String result_metal_ac;			//������(���� account count �׷�����)
	private String result_mup_ac;			//M/UP���ڰ��(���� account count �׷�����)
	private String result_oversea_ac;		//�ؿܱ԰ݽ��κ�(���� account count �׷�����)
	private String result_plant_ac;			//�ؿܱ԰ݽ��κ�(���� account count �׷�����)

	private double weight;					//Activity�� �߿䵵(0.1 ~ 1.0)
	private double progress;				//������	 (0.00 ~ 1.00)

	private String pjt_mbr_type;			//������� ����
	private String mbr_start_date;			//���������
	private String mbr_end_date;			//����Ϸ���
	private double mbr_poration;			//�������
	private String pjt_mbr_name;			//����̸�
	private String pjt_mbr_job;				//���������
	private String pjt_mbr_tel;				//�����ȭ��ȣ
	private String pjt_mbr_grade;			//�������
	private String pjt_mbr_div;				//����μ��ڵ�

	private String parent_node;				//�θ���
	private String child_node;				//�ڽĳ��
	private String level_no;				//��巹��
	private String node_name;				//����̸�
	private String user_id;					//������ڻ��
	private String user_name;				//��������̸�
	private String pjt_node_mbr;			//�����
	private int plan_cnt;					//��ȹ�ϼ�
	private int chg_cnt;					//�����ϼ�
	private int result_cnt;					//�����ϼ�
	private String node_status;				//������
	private String remark;					//�ڸ�Ʈ[�۾����û���]
	private String chg_note;				//�����������

	private String node_code;				//����ڵ�
	private String wm_type;					//�ְ�/���� ��������
	private String evt_content;				//���೻��
	private String evt_note;				//������
	private String evt_issue;				//�̽�����

	private String cost_type;				//����׸�
	private String node_cost;				//������
	private String exchange;				//ȯ��

	private String use_doc;					//���⹰�׸� ��Ͽ���

	private String users;					//������/�̽����� �����
	private String note;					//���������� ����������
	private String solution;				//������/�̽����� ��å
	private String content;					//������/�̽����� �ذ᳻��
	private String sol_date;				//������/�̽����� �ذ�����
	private String book_date;				//������/�̽����� �ذΌ������
	private String note_status;				//���������� ����
	private String issue;					//�̽����� �̽�����
	private String issue_status;			//�̽����� ����

	private String mgr_id;					//������Ʈ������ ���
	private String mgr_name;				//������Ʈ������ �̸�
	private String type;					//������Ʈ���� (P:����, �μ��ڵ�:�μ�)

	private String modify;					//����
	private String delete;					//����
	private String view;					//�󼼺���

	private String pjtWord;					//�˻� ��������
	private String sItem;					//Item
	private String sWord;					//Word
	
	//�����ڵ�
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//project �ڵ�
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//project �̸�
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//����������
	public void setOwner(String owner)				{ this.owner = owner;				}
	public String getOwner()						{ return this.owner;				}

	//�ۼ���
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//����PM/PL���/�̸�
	public void setPjtMbrId(String pjt_mbr_id)		{ this.pjt_mbr_id = pjt_mbr_id;		}
	public String getPjtMbrId()						{ return this.pjt_mbr_id;			}

	//�������/����
	public void setPjtClass(String pjt_class)		{ this.pjt_class = pjt_class;		}
	public String getPjtClass()						{ return this.pjt_class;			}

	//���߸�ǥ
	public void setPjtTarget(String pjt_target)		{ this.pjt_target = pjt_target;		}
	public String getPjtTarget()					{ return this.pjt_target;			}

	//�濵��ȹ
	public void setMgtPlan(String mgt_plan)			{ this.mgt_plan = mgt_plan;			}
	public String getMgtPlan()						{ return this.mgt_plan;				}

	//��ȹ������,���ΰ���
	public void setParentCode(String parent_code)	{ this.parent_code = parent_code;	}
	public String getParentCode()					{ return this.parent_code;			}

	//�����η��ο�
	public void setMbrExp(int mbr_exp)				{ this.mbr_exp = mbr_exp;			}
	public int getMbrExp()							{ return this.mbr_exp;				}

	//���߿����Ѻ��(��)
	public void setCostExp(String cost_exp)			{ this.cost_exp = cost_exp;			}
	public String getCostExp()						{ return this.cost_exp;				}

	//���߽����Ѻ��(��)
	public void setCostRst(String cost_rst)			{ this.cost_rst = cost_rst;			}
	public String getCostRst()						{ return this.cost_rst;				}

	//����
	public void setDifCost(String dif_cost)			{ this.dif_cost = dif_cost;			}
	public String getDifCost()						{ return this.dif_cost;				}

	//��ȹ������
	public void setPlanStartDate(String plan_start_date)	{ this.plan_start_date = plan_start_date;	}
	public String getPlanStartDate()						{ return this.plan_start_date;				}

	//��ȹ������
	public void setPlanEndDate(String plan_end_date)		{ this.plan_end_date = plan_end_date;		}
	public String getPlanEndDate()							{ return this.plan_end_date;				}

	//����������
	public void setChgStartDate(String chg_start_date)		{ this.chg_start_date = chg_start_date;		}
	public String getChgStartDate()							{ return this.chg_start_date;				}

	//����������
	public void setChgEndDate(String chg_end_date)			{ this.chg_end_date = chg_end_date;			}
	public String getChgEndDate()							{ return this.chg_end_date;					}

	//����������
	public void setRstStartDate(String rst_start_date)		{ this.rst_start_date = rst_start_date;		}
	public String getRstStartDate()							{ return this.rst_start_date;				}

	//����������
	public void setRstEndDate(String rst_end_date)			{ this.rst_end_date = rst_end_date;			}
	public String getRstEndDate()							{ return this.rst_end_date;					}

	//ǥ�����μ����ڵ�
	public void setPrsCode(String prs_code)			{ this.prs_code = prs_code;			}
	public String getPrsCode()						{ return this.prs_code;				}

	//���μ������� P:����, �μ��ڵ�:�μ�
	public void setPrsType(String prs_type)			{ this.prs_type = prs_type;			}
	public String getPrsType()						{ return this.prs_type;				}

	//��������
	public void setPjtDesc(String pjt_desc)			{ this.pjt_desc = pjt_desc;			}
	public String getPjtDesc()						{ return this.pjt_desc;				}

	//���߳���
	public void setPjtSpec(String pjt_spec)			{ this.pjt_spec = pjt_spec;			}
	public String getPjtSpec()						{ return this.pjt_spec;				}

	//�����������
	public void setPjtStatus(String pjt_status)		{ this.pjt_status = pjt_status;		}
	public String getPjtStatus()					{ return this.pjt_status;			}

	//������ο���
	public void setFlag(String flag)				{ this.flag = flag;					}
	public String getFlag()							{ return this.flag;					}

	//�ΰǺ�(��ȹ)
	public void setPlanLabor(String plan_labor)		{ this.plan_labor = plan_labor;		}
	public String getPlanLabor()					{ return this.plan_labor;			}

	//�������ۺ�(��ȹ)
	public void setPlanSample(String plan_sample)	{ this.plan_sample = plan_sample;	}
	public String getPlanSample()					{ return this.plan_sample;			}

	//������(��ȹ)
	public void setPlanMetal(String plan_metal)		{ this.plan_metal = plan_metal;		}
	public String getPlanMetal()					{ return this.plan_metal;			}

	//M/UP���ڰ��(��ȹ)
	public void setPlanMup(String plan_mup)			{ this.plan_mup = plan_mup;			}
	public String getPlanMup()						{ return this.plan_mup;				}

	//�ؿܱ԰ݽ��κ�(��ȹ)
	public void setPlanOversea(String plan_oversea)	{ this.plan_oversea = plan_oversea;	}
	public String getPlanOversea()					{ return this.plan_oversea;			}

	//�ü����ں�(��ȹ)
	public void setPlanPlant(String plan_plant)		{ this.plan_plant = plan_plant;		}
	public String getPlanPlant()					{ return this.plan_plant;			}

	//�������ۺ�(����)
	public void setResultLabor(String result_labor)	{ this.result_labor = result_labor;	}
	public String getResultLabor()					{ return this.result_labor;			}

	//�������ۺ�(����)
	public void setResultSample(String result_sample)	{ this.result_sample = result_sample;		}
	public String getResultSample()						{ return this.result_sample;				}

	//������(����)
	public void setResultMetal(String result_metal)	{ this.result_metal = result_metal;	}
	public String getResultMetal()					{ return this.result_metal;			}

	//M/UP���ڰ��(����)
	public void setResultMup(String result_mup)		{ this.result_mup = result_mup;		}
	public String getResultMup()					{ return this.result_mup;			}

	//�ؿܱ԰ݽ��κ�(����)
	public void setResultOversea(String result_oversea)	{ this.result_oversea = result_oversea;		}
	public String getResultOversea()					{ return this.result_oversea;				}

	//�ؿܱ԰ݽ��κ�(����)
	public void setResultPlant(String result_plant)		{ this.result_plant = result_plant;			}
	public String getResultPlant()						{ return this.result_plant;					}

	//�ΰǺ�(��ȹ account count �׷�����)
	public void setPlanLaborAc(String plan_labor_ac)	{ this.plan_labor_ac = plan_labor_ac;		}
	public String getPlanLaborAc()						{ return this.plan_labor_ac;				}

	//�������ۺ�(��ȹ account count �׷�����)
	public void setPlanSampleAc(String plan_sample_ac)	{ this.plan_sample_ac = plan_sample_ac;		}
	public String getPlanSampleAc()						{ return this.plan_sample_ac;				}

	//������(��ȹ account count �׷�����)
	public void setPlanMetalAc(String plan_metal_ac)	{ this.plan_metal_ac = plan_metal_ac;		}
	public String getPlanMetalAc()						{ return this.plan_metal_ac;				}

	//M/UP���ڰ��(��ȹ account count �׷�����)
	public void setPlanMupAc(String plan_mup_ac)		{ this.plan_mup_ac = plan_mup_ac;			}
	public String getPlanMupAc()						{ return this.plan_mup_ac;					}

	//�ؿܱ԰ݽ��κ�(��ȹ account count �׷�����)
	public void setPlanOverseaAc(String plan_oversea_ac){ this.plan_oversea_ac = plan_oversea_ac;	}
	public String getPlanOverseaAc()					{ return this.plan_oversea_ac;				}

	//�ü����ں�(��ȹ account count �׷�����)
	public void setPlanPlantAc(String plan_plant_ac)	{ this.plan_plant_ac = plan_plant_ac;		}
	public String getPlanPlantAc()						{ return this.plan_plant_ac;				}

	//�������ۺ�(���� account count �׷�����)
	public void setResultLaborAc(String result_labor_ac){ this.result_labor_ac = result_labor_ac;	}
	public String getResultLaborAc()					{ return this.result_labor_ac;				}

	//�������ۺ�(���� account count �׷�����)
	public void setResultSampleAc(String result_sample_ac)	{ this.result_sample_ac = result_sample_ac;	}
	public String getResultSampleAc()						{ return this.result_sample_ac;				}

	//������(���� account count �׷�����)
	public void setResultMetalAc(String result_metal_ac)	{ this.result_metal_ac = result_metal_ac;	}
	public String getResultMetalAc()						{ return this.result_metal_ac;				}

	//M/UP���ڰ��(���� account count �׷�����)
	public void setResultMupAc(String result_mup_ac)		{ this.result_mup_ac = result_mup_ac;		}
	public String getResultMupAc()							{ return this.result_mup_ac;				}

	//�ؿܱ԰ݽ��κ�(���� account count �׷�����)
	public void setResultOverseaAc(String result_oversea_ac){ this.result_oversea_ac = result_oversea_ac;}
	public String getResultOverseaAc()						{ return this.result_oversea_ac;			}

	//�ؿܱ԰ݽ��κ�(���� account count �׷�����)
	public void setResultPlantAc(String result_plant_ac)	{ this.result_plant_ac = result_plant_ac;	}
	public String getResultPlantAc()						{ return this.result_plant_ac;				}

	//Activity�� �߿䵵(0.1 ~ 1.0)
	public void setWeight(double weight)				{ this.weight = weight;						}
	public double getWeight()							{ return this.weight;						}

	//������	 (0.00 ~ 1.00)
	public void setProgress(double progress)			{ this.progress = progress;					}
	public double getProgress()							{ return this.progress;						}

	//������� ����
	public void setPjtMbrType(String pjt_mbr_type)		{ this.pjt_mbr_type = pjt_mbr_type;			}
	public String getPjtMbrType()						{ return this.pjt_mbr_type;					}

	//���������
	public void setMbrStartDate(String mbr_start_date)	{ this.mbr_start_date = mbr_start_date;		}
	public String getMbrStartDate()						{ return this.mbr_start_date;				}

	//����Ϸ���
	public void setMbrEndDate(String mbr_end_date)		{ this.mbr_end_date = mbr_end_date;			}
	public String getMbrEndDate()						{ return this.mbr_end_date;					}

	//���������
	public void setMbrPoration(double mbr_poration)		{ this.mbr_poration = mbr_poration;			}
	public double getMbrPoration()						{ return this.mbr_poration;					}

	//����̸�
	public void setPjtMbrName(String pjt_mbr_name)		{ this.pjt_mbr_name = pjt_mbr_name;			}
	public String getPjtMbrName()						{ return this.pjt_mbr_name;					}

	//���������
	public void setPjtMbrJob(String pjt_mbr_job)		{ this.pjt_mbr_job = pjt_mbr_job;			}
	public String getPjtMbrJob()						{ return this.pjt_mbr_job;					}

	//�����ȭ��ȣ
	public void setPjtMbrTel(String pjt_mbr_tel)		{ this.pjt_mbr_tel = pjt_mbr_tel;			}
	public String getPjtMbrTel()						{ return this.pjt_mbr_tel;					}

	//�������
	public void setPjtMbrGrade(String pjt_mbr_grade)	{ this.pjt_mbr_grade = pjt_mbr_grade;		}
	public String getPjtMbrGrade()						{ return this.pjt_mbr_grade;				}

	//����μ��ڵ�
	public void setPjtMbrDiv(String pjt_mbr_div)		{ this.pjt_mbr_div = pjt_mbr_div;			}
	public String getPjtMbrDiv()						{ return this.pjt_mbr_div;					}

	//�θ� ���
	public void setParentNode(String parent_node)	{ this.parent_node = parent_node;	}
	public String getParentNode()					{ return this.parent_node;			}

	//�ڽ� ���
	public void setChildNode(String child_node)		{ this.child_node = child_node;		}
	public String getChildNode()					{ return this.child_node;			}

	//����
	public void setNodeName(String node_name)		{ this.node_name = node_name;		}
	public String getNodeName()						{ return this.node_name;			}

	//���� ��� ����
	public void setLevelNo(String level_no)			{ this.level_no = level_no;			}
	public String getLevelNo()						{ return this.level_no;				}

	//������ڻ��
	public void setUserId(String user_id)			{ this.user_id = user_id;			}
	public String getUserId()						{ return this.user_id;				}

	//��������̸�
	public void setUserName(String user_name)		{ this.user_name = user_name;		}
	public String getUserName()						{ return this.user_name;			}

	//�����
	public void setPjtNodeMbr(String pjt_node_mbr)	{ this.pjt_node_mbr = pjt_node_mbr;	}
	public String getPjtNodeMbr()					{ return this.pjt_node_mbr;			}

	//��ȹ�ϼ�
	public void setPlanCnt(int plan_cnt)			{ this.plan_cnt = plan_cnt;			}
	public int getPlanCnt()							{ return this.plan_cnt;				}

	//�����ϼ�
	public void setChgCnt(int chg_cnt)				{ this.chg_cnt = chg_cnt;			}
	public int getChgCnt()							{ return this.chg_cnt;				}

	//�����ϼ�
	public void setResultCnt(int result_cnt)		{ this.result_cnt = result_cnt;		}
	public int getResultCnt()						{ return this.result_cnt;			}

	//������
	public void setNodeStatus(String node_status)	{ this.node_status = node_status;	}
	public String getNodeStatus()					{ return this.node_status;			}

	//�ڸ�Ʈ
	public void setRemark(String remark)			{ this.remark = remark;				}
	public String getRemark()						{ return this.remark;				}

	//�����������
	public void setChgNote(String chg_note)			{ this.chg_note = chg_note;			}
	public String getChgNote()						{ return this.chg_note;				}

	//����̸�
	public void setNodeCode(String node_code)		{ this.node_code = node_code;		}
	public String getNodeCode()						{ return this.node_code;			}

	//�ְ�/���� ��������
	public void setWmType(String wm_type)			{ this.wm_type = wm_type;			}
	public String getWmType()						{ return this.wm_type;				}

	//���೻��
	public void setEvtContent(String evt_content)	{ this.evt_content = evt_content;	}
	public String getEvtContent()					{ return this.evt_content;			}

	//������
	public void setEvtNote(String evt_note)			{ this.evt_note = evt_note;			}
	public String getEvtNote()						{ return this.evt_note;				}

	//�̽�����
	public void setEvtIssue(String evt_issue)		{ this.evt_issue = evt_issue;		}
	public String getEvtIssue()						{ return this.evt_issue;			}

	//����׸�
	public void setCostType(String cost_type)		{ this.cost_type = cost_type;		}
	public String getCostType()						{ return this.cost_type;			}

	//������
	public void setNodeCost(String node_cost)		{ this.node_cost = node_cost;		}
	public String getNodeCost()						{ return this.node_cost;			}

	//ȯ��
	public void setExchange(String exchange)		{ this.exchange = exchange;			}
	public String getExchange()						{ return this.exchange;				}

	//���⹰�׸� ��Ͽ���
	public void setUseDoc(String use_doc)			{ this.use_doc = use_doc;			}
	public String getUseDoc()						{ return this.use_doc;				}

	//������/�̽����� �����
	public void setUsers(String users)				{ this.users = users;				}
	public String getUsers()						{ return this.users;				}

	//���������� ����������
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//������/�̽����� ��å
	public void setSolution(String solution)		{ this.solution = solution;			}
	public String getSolution()						{ return this.solution;				}

	//������/�̽����� �ذ᳻��
	public void setContent(String content)			{ this.content = content;			}
	public String getContent()						{ return this.content;				}

	//������/�̽����� �ذ�����
	public void setSolDate(String sol_date)			{ this.sol_date = sol_date;			}
	public String getSolDate()						{ return this.sol_date;				}

	//������/�̽����� �ذΌ������
	public void setBookDate(String book_date)		{ this.book_date = book_date;		}
	public String getBookDate()						{ return this.book_date;			}

	//���������� ����
	public void setNoteStatus(String note_status)	{ this.note_status = note_status;	}
	public String getNoteStatus()					{ return this.note_status;			}

	//�̽����� �̽�����
	public void setIssue(String issue)				{ this.issue = issue;				}
	public String getIssue()						{ return this.issue;				}

	//�̽����� ����
	public void setIssueStatus(String issue_status)	{ this.issue_status = issue_status;	}
	public String getIssueStatus()					{ return this.issue_status;			}

	//����
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//����
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//�󼼺���
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//�˻� ��������
	public void setPjtword(String pjtWord)			{ this.pjtWord = pjtWord;			}
	public String getPjtword()						{ return this.pjtWord;				}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

	//Project������ ���
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//Project������ �̸�
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//������Ʈ���� (P:����, �μ��ڵ�:�μ�)
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}


}


