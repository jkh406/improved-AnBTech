package com.anbtech.pjt.entity;
public class prsCodeTable
{
	private String pid;						//�����ڵ�
	private String ph_code;					//phase�ڵ�
	private String ph_name;					//phase�̸�
	private String step_code;				//step�ڵ�
	private String step_name;				//step�̸�
	private String act_code;				//activity�ڵ�
	private String act_name;				//activity�̸�
	private String doc_code;				//���⹰�ڵ�
	private String doc_name;				//���⹰�̸�
	private String prs_code;				//���μ����ڵ�
	private String prs_name;				//���μ����̸�
	private String parent_node;				//�θ� ���
	private String child_node;				//�ڽ� ���
	private String node_name;				//����
	private String level_no;				//���� ��� ����
	private String dip_no;					//ȭ����� ����
	private String type;					//type

	private String modify;					//����
	private String delete;					//����
	
	//�����ڵ�
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//phase�ڵ�
	public void setPhCode(String ph_code)			{ this.ph_code = ph_code;			}
	public String getPhCode()						{ return this.ph_code;				}

	//phase�̸�
	public void setPhName(String ph_name)			{ this.ph_name = ph_name;			}
	public String getPhName()						{ return this.ph_name;				}

	//step�ڵ�
	public void setStepCode(String step_code)		{ this.step_code = step_code;		}
	public String getStepCode()						{ return this.step_code;			}

	//step�̸�
	public void setStepName(String step_name)		{ this.step_name = step_name;		}
	public String getStepName()						{ return this.step_name;			}

	//activity�ڵ�
	public void setActCode(String act_code)			{ this.act_code = act_code;			}
	public String getActCode()						{ return this.act_code;				}

	//activity�̸�
	public void setActName(String act_name)			{ this.act_name = act_name;			}
	public String getActName()						{ return this.act_name;				}

	//���⹰�ڵ�
	public void setDocCode(String doc_code)			{ this.doc_code = doc_code;			}
	public String getDocCode()						{ return this.doc_code;				}

	//���⹰�̸�
	public void setDocName(String doc_name)			{ this.doc_name = doc_name;			}
	public String getDocName()						{ return this.doc_name;				}

	//���μ����ڵ�
	public void setPrsCode(String prs_code)			{ this.prs_code = prs_code;			}
	public String getPrsCode()						{ return this.prs_code;				}

	//���μ����̸�
	public void setPrsName(String prs_name)			{ this.prs_name = prs_name;			}
	public String getPrsName()						{ return this.prs_name;				}

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

	//ȭ����� ����
	public void setDipNo(String dip_no)				{ this.dip_no = dip_no;				}
	public String getDipNo()						{ return this.dip_no;				}

	//type
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}

	//����
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//����
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

}

