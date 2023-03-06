package com.anbtech.pjt.entity;
public class prsCodeTable
{
	private String pid;						//관리코드
	private String ph_code;					//phase코드
	private String ph_name;					//phase이름
	private String step_code;				//step코드
	private String step_name;				//step이름
	private String act_code;				//activity코드
	private String act_name;				//activity이름
	private String doc_code;				//산출물코드
	private String doc_name;				//산출물이름
	private String prs_code;				//프로세스코드
	private String prs_name;				//프로세스이름
	private String parent_node;				//부모 노드
	private String child_node;				//자식 노드
	private String node_name;				//노드명
	private String level_no;				//모자 노드 관계
	private String dip_no;					//화면출력 순서
	private String type;					//type

	private String modify;					//수정
	private String delete;					//삭제
	
	//관리코드
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//phase코드
	public void setPhCode(String ph_code)			{ this.ph_code = ph_code;			}
	public String getPhCode()						{ return this.ph_code;				}

	//phase이름
	public void setPhName(String ph_name)			{ this.ph_name = ph_name;			}
	public String getPhName()						{ return this.ph_name;				}

	//step코드
	public void setStepCode(String step_code)		{ this.step_code = step_code;		}
	public String getStepCode()						{ return this.step_code;			}

	//step이름
	public void setStepName(String step_name)		{ this.step_name = step_name;		}
	public String getStepName()						{ return this.step_name;			}

	//activity코드
	public void setActCode(String act_code)			{ this.act_code = act_code;			}
	public String getActCode()						{ return this.act_code;				}

	//activity이름
	public void setActName(String act_name)			{ this.act_name = act_name;			}
	public String getActName()						{ return this.act_name;				}

	//산출물코드
	public void setDocCode(String doc_code)			{ this.doc_code = doc_code;			}
	public String getDocCode()						{ return this.doc_code;				}

	//산출물이름
	public void setDocName(String doc_name)			{ this.doc_name = doc_name;			}
	public String getDocName()						{ return this.doc_name;				}

	//프로세스코드
	public void setPrsCode(String prs_code)			{ this.prs_code = prs_code;			}
	public String getPrsCode()						{ return this.prs_code;				}

	//프로세스이름
	public void setPrsName(String prs_name)			{ this.prs_name = prs_name;			}
	public String getPrsName()						{ return this.prs_name;				}

	//부모 노드
	public void setParentNode(String parent_node)	{ this.parent_node = parent_node;	}
	public String getParentNode()					{ return this.parent_node;			}

	//자식 노드
	public void setChildNode(String child_node)		{ this.child_node = child_node;		}
	public String getChildNode()					{ return this.child_node;			}

	//노드명
	public void setNodeName(String node_name)		{ this.node_name = node_name;		}
	public String getNodeName()						{ return this.node_name;			}

	//모자 노드 관계
	public void setLevelNo(String level_no)			{ this.level_no = level_no;			}
	public String getLevelNo()						{ return this.level_no;				}

	//화면출력 순서
	public void setDipNo(String dip_no)				{ this.dip_no = dip_no;				}
	public String getDipNo()						{ return this.dip_no;				}

	//type
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}

	//수정
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//삭제
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

}

