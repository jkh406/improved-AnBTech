package com.anbtech.pjt.entity;
public class projectTable
{
	private String pid;						//관리코드
	private String pjt_code;				//project 코드
	private String pjt_name;				//project 이름
	private String owner;					//과제관리자
	private String in_date;					//작성일
	private String pjt_mbr_id;				//과제PM/PL사번/이름
	private String pjt_class;				//과제등급/성격
	private String pjt_target;				//개발목표
	private String mgt_plan;				//경영계획
	private String parent_code;				//기획과제명,메인과제
	private int mbr_exp;					//개뱔인력인원
	private String cost_exp;				//개발예산 총비용(원)
	private String cost_rst;				//개발실적 총비용(원)
	private String dif_cost;				//차액
	private String plan_start_date;			//계획시작일
	private String plan_end_date;			//계획종료일
	private String chg_start_date;			//수정시작일
	private String chg_end_date;			//수정종료일
	private String rst_start_date;			//실적시작일
	private String rst_end_date;			//실적종료일
	private String prs_code;				//표준프로세스코드
	private String prs_type;				//프로세스구분 P:전사, 부서코드:부서
	private String pjt_desc;				//과제개요
	private String pjt_spec;				//개발내용
	private String pjt_status;				//과제진행상태
	private String flag;					//결재승인여부
	private String plan_labor;				//인건비(계획)
	private String plan_sample;				//샘플제작비(계획)
	private String plan_metal;				//금형비(계획)
	private String plan_mup;				//M/UP투자경비(계획)
	private String plan_oversea;			//해외규격승인비(계획)
	private String plan_plant;				//시설투자비(계획)
	private String result_labor;			//샘플제작비(실적)
	private String result_sample;			//샘플제작비(실적)
	private String result_metal;			//금형비(실적)
	private String result_mup;				//M/UP투자경비(실적)
	private String result_oversea;			//해외규격승인비(실적)
	private String result_plant;			//해외규격승인비(실적)

	private String plan_labor_ac;			//인건비(계획 account count 그래프용)
	private String plan_sample_ac;			//샘플제작비(계획 account count 그래프용)
	private String plan_metal_ac;			//금형비(계획 account count 그래프용)
	private String plan_mup_ac;				//M/UP투자경비(계획 account count 그래프용)
	private String plan_oversea_ac;			//해외규격승인비(계획 account count 그래프용)
	private String plan_plant_ac;			//시설투자비(계획 account count 그래프용)
	private String result_labor_ac;			//샘플제작비(실적 account count 그래프용)
	private String result_sample_ac;		//샘플제작비(실적 account count 그래프용)
	private String result_metal_ac;			//금형비(실적 account count 그래프용)
	private String result_mup_ac;			//M/UP투자경비(실적 account count 그래프용)
	private String result_oversea_ac;		//해외규격승인비(실적 account count 그래프용)
	private String result_plant_ac;			//해외규격승인비(실적 account count 그래프용)

	private double weight;					//Activity의 중요도(0.1 ~ 1.0)
	private double progress;				//진행율	 (0.00 ~ 1.00)

	private String pjt_mbr_type;			//멤버구성 종류
	private String mbr_start_date;			//멤버시작일
	private String mbr_end_date;			//멤버완료일
	private double mbr_poration;			//멤버투입
	private String pjt_mbr_name;			//멤버이름
	private String pjt_mbr_job;				//멤버담당업무
	private String pjt_mbr_tel;				//멤버전화번호
	private String pjt_mbr_grade;			//멤버직급
	private String pjt_mbr_div;				//멤버부서코드

	private String parent_node;				//부모노드
	private String child_node;				//자식노드
	private String level_no;				//노드레벨
	private String node_name;				//노드이름
	private String user_id;					//노드담당자사번
	private String user_name;				//노드담당자이름
	private String pjt_node_mbr;			//노드멤버
	private int plan_cnt;					//계획일수
	private int chg_cnt;					//수정일수
	private int result_cnt;					//실적일수
	private String node_status;				//노드상태
	private String remark;					//코맨트[작업지시사항]
	private String chg_note;				//일정변경사유

	private String node_code;				//노드코드
	private String wm_type;					//주간/월간 실적구분
	private String evt_content;				//진행내용
	private String evt_note;				//문제점
	private String evt_issue;				//이슈사항

	private String cost_type;				//경비항목
	private String node_cost;				//지출경비
	private String exchange;				//환율

	private String use_doc;					//산출물항목 등록여부

	private String users;					//문제점/이슈관리 담당자
	private String note;					//문제점관리 문제점내용
	private String solution;				//문제점/이슈관리 대책
	private String content;					//문제점/이슈관리 해결내용
	private String sol_date;				//문제점/이슈관리 해결일자
	private String book_date;				//문제점/이슈관리 해결예정일자
	private String note_status;				//문제점관리 상태
	private String issue;					//이슈관리 이슈사항
	private String issue_status;			//이슈관리 상태

	private String mgr_id;					//프로젝트관리자 사번
	private String mgr_name;				//프로젝트관리자 이름
	private String type;					//프로젝트구분 (P:전사, 부서코드:부서)

	private String modify;					//수정
	private String delete;					//삭제
	private String view;					//상세보기

	private String pjtWord;					//검색 과제상태
	private String sItem;					//Item
	private String sWord;					//Word
	
	//관리코드
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}
		
	//project 코드
	public void setPjtCode(String pjt_code)			{ this.pjt_code = pjt_code;			}
	public String getPjtCode()						{ return this.pjt_code;				}

	//project 이름
	public void setPjtName(String pjt_name)			{ this.pjt_name = pjt_name;			}
	public String getPjtName()						{ return this.pjt_name;				}

	//과제관리자
	public void setOwner(String owner)				{ this.owner = owner;				}
	public String getOwner()						{ return this.owner;				}

	//작성일
	public void setInDate(String in_date)			{ this.in_date = in_date;			}
	public String getInDate()						{ return this.in_date;				}

	//과제PM/PL사번/이름
	public void setPjtMbrId(String pjt_mbr_id)		{ this.pjt_mbr_id = pjt_mbr_id;		}
	public String getPjtMbrId()						{ return this.pjt_mbr_id;			}

	//과제등급/성격
	public void setPjtClass(String pjt_class)		{ this.pjt_class = pjt_class;		}
	public String getPjtClass()						{ return this.pjt_class;			}

	//개발목표
	public void setPjtTarget(String pjt_target)		{ this.pjt_target = pjt_target;		}
	public String getPjtTarget()					{ return this.pjt_target;			}

	//경영계획
	public void setMgtPlan(String mgt_plan)			{ this.mgt_plan = mgt_plan;			}
	public String getMgtPlan()						{ return this.mgt_plan;				}

	//기획과제명,메인과제
	public void setParentCode(String parent_code)	{ this.parent_code = parent_code;	}
	public String getParentCode()					{ return this.parent_code;			}

	//개발인력인원
	public void setMbrExp(int mbr_exp)				{ this.mbr_exp = mbr_exp;			}
	public int getMbrExp()							{ return this.mbr_exp;				}

	//개발예산총비용(원)
	public void setCostExp(String cost_exp)			{ this.cost_exp = cost_exp;			}
	public String getCostExp()						{ return this.cost_exp;				}

	//개발실적총비용(원)
	public void setCostRst(String cost_rst)			{ this.cost_rst = cost_rst;			}
	public String getCostRst()						{ return this.cost_rst;				}

	//차액
	public void setDifCost(String dif_cost)			{ this.dif_cost = dif_cost;			}
	public String getDifCost()						{ return this.dif_cost;				}

	//계획시작일
	public void setPlanStartDate(String plan_start_date)	{ this.plan_start_date = plan_start_date;	}
	public String getPlanStartDate()						{ return this.plan_start_date;				}

	//계획종료일
	public void setPlanEndDate(String plan_end_date)		{ this.plan_end_date = plan_end_date;		}
	public String getPlanEndDate()							{ return this.plan_end_date;				}

	//수정시작일
	public void setChgStartDate(String chg_start_date)		{ this.chg_start_date = chg_start_date;		}
	public String getChgStartDate()							{ return this.chg_start_date;				}

	//수정종료일
	public void setChgEndDate(String chg_end_date)			{ this.chg_end_date = chg_end_date;			}
	public String getChgEndDate()							{ return this.chg_end_date;					}

	//실적시작일
	public void setRstStartDate(String rst_start_date)		{ this.rst_start_date = rst_start_date;		}
	public String getRstStartDate()							{ return this.rst_start_date;				}

	//실적종료일
	public void setRstEndDate(String rst_end_date)			{ this.rst_end_date = rst_end_date;			}
	public String getRstEndDate()							{ return this.rst_end_date;					}

	//표준프로세스코드
	public void setPrsCode(String prs_code)			{ this.prs_code = prs_code;			}
	public String getPrsCode()						{ return this.prs_code;				}

	//프로세스구분 P:전사, 부서코드:부서
	public void setPrsType(String prs_type)			{ this.prs_type = prs_type;			}
	public String getPrsType()						{ return this.prs_type;				}

	//과제개요
	public void setPjtDesc(String pjt_desc)			{ this.pjt_desc = pjt_desc;			}
	public String getPjtDesc()						{ return this.pjt_desc;				}

	//개발내용
	public void setPjtSpec(String pjt_spec)			{ this.pjt_spec = pjt_spec;			}
	public String getPjtSpec()						{ return this.pjt_spec;				}

	//과제진행상태
	public void setPjtStatus(String pjt_status)		{ this.pjt_status = pjt_status;		}
	public String getPjtStatus()					{ return this.pjt_status;			}

	//결재승인여부
	public void setFlag(String flag)				{ this.flag = flag;					}
	public String getFlag()							{ return this.flag;					}

	//인건비(계획)
	public void setPlanLabor(String plan_labor)		{ this.plan_labor = plan_labor;		}
	public String getPlanLabor()					{ return this.plan_labor;			}

	//샘플제작비(계획)
	public void setPlanSample(String plan_sample)	{ this.plan_sample = plan_sample;	}
	public String getPlanSample()					{ return this.plan_sample;			}

	//금형비(계획)
	public void setPlanMetal(String plan_metal)		{ this.plan_metal = plan_metal;		}
	public String getPlanMetal()					{ return this.plan_metal;			}

	//M/UP투자경비(계획)
	public void setPlanMup(String plan_mup)			{ this.plan_mup = plan_mup;			}
	public String getPlanMup()						{ return this.plan_mup;				}

	//해외규격승인비(계획)
	public void setPlanOversea(String plan_oversea)	{ this.plan_oversea = plan_oversea;	}
	public String getPlanOversea()					{ return this.plan_oversea;			}

	//시설투자비(계획)
	public void setPlanPlant(String plan_plant)		{ this.plan_plant = plan_plant;		}
	public String getPlanPlant()					{ return this.plan_plant;			}

	//샘플제작비(실적)
	public void setResultLabor(String result_labor)	{ this.result_labor = result_labor;	}
	public String getResultLabor()					{ return this.result_labor;			}

	//샘플제작비(실적)
	public void setResultSample(String result_sample)	{ this.result_sample = result_sample;		}
	public String getResultSample()						{ return this.result_sample;				}

	//금형비(실적)
	public void setResultMetal(String result_metal)	{ this.result_metal = result_metal;	}
	public String getResultMetal()					{ return this.result_metal;			}

	//M/UP투자경비(실적)
	public void setResultMup(String result_mup)		{ this.result_mup = result_mup;		}
	public String getResultMup()					{ return this.result_mup;			}

	//해외규격승인비(실적)
	public void setResultOversea(String result_oversea)	{ this.result_oversea = result_oversea;		}
	public String getResultOversea()					{ return this.result_oversea;				}

	//해외규격승인비(실적)
	public void setResultPlant(String result_plant)		{ this.result_plant = result_plant;			}
	public String getResultPlant()						{ return this.result_plant;					}

	//인건비(계획 account count 그래프용)
	public void setPlanLaborAc(String plan_labor_ac)	{ this.plan_labor_ac = plan_labor_ac;		}
	public String getPlanLaborAc()						{ return this.plan_labor_ac;				}

	//샘플제작비(계획 account count 그래프용)
	public void setPlanSampleAc(String plan_sample_ac)	{ this.plan_sample_ac = plan_sample_ac;		}
	public String getPlanSampleAc()						{ return this.plan_sample_ac;				}

	//금형비(계획 account count 그래프용)
	public void setPlanMetalAc(String plan_metal_ac)	{ this.plan_metal_ac = plan_metal_ac;		}
	public String getPlanMetalAc()						{ return this.plan_metal_ac;				}

	//M/UP투자경비(계획 account count 그래프용)
	public void setPlanMupAc(String plan_mup_ac)		{ this.plan_mup_ac = plan_mup_ac;			}
	public String getPlanMupAc()						{ return this.plan_mup_ac;					}

	//해외규격승인비(계획 account count 그래프용)
	public void setPlanOverseaAc(String plan_oversea_ac){ this.plan_oversea_ac = plan_oversea_ac;	}
	public String getPlanOverseaAc()					{ return this.plan_oversea_ac;				}

	//시설투자비(계획 account count 그래프용)
	public void setPlanPlantAc(String plan_plant_ac)	{ this.plan_plant_ac = plan_plant_ac;		}
	public String getPlanPlantAc()						{ return this.plan_plant_ac;				}

	//샘플제작비(실적 account count 그래프용)
	public void setResultLaborAc(String result_labor_ac){ this.result_labor_ac = result_labor_ac;	}
	public String getResultLaborAc()					{ return this.result_labor_ac;				}

	//샘플제작비(실적 account count 그래프용)
	public void setResultSampleAc(String result_sample_ac)	{ this.result_sample_ac = result_sample_ac;	}
	public String getResultSampleAc()						{ return this.result_sample_ac;				}

	//금형비(실적 account count 그래프용)
	public void setResultMetalAc(String result_metal_ac)	{ this.result_metal_ac = result_metal_ac;	}
	public String getResultMetalAc()						{ return this.result_metal_ac;				}

	//M/UP투자경비(실적 account count 그래프용)
	public void setResultMupAc(String result_mup_ac)		{ this.result_mup_ac = result_mup_ac;		}
	public String getResultMupAc()							{ return this.result_mup_ac;				}

	//해외규격승인비(실적 account count 그래프용)
	public void setResultOverseaAc(String result_oversea_ac){ this.result_oversea_ac = result_oversea_ac;}
	public String getResultOverseaAc()						{ return this.result_oversea_ac;			}

	//해외규격승인비(실적 account count 그래프용)
	public void setResultPlantAc(String result_plant_ac)	{ this.result_plant_ac = result_plant_ac;	}
	public String getResultPlantAc()						{ return this.result_plant_ac;				}

	//Activity의 중요도(0.1 ~ 1.0)
	public void setWeight(double weight)				{ this.weight = weight;						}
	public double getWeight()							{ return this.weight;						}

	//진행율	 (0.00 ~ 1.00)
	public void setProgress(double progress)			{ this.progress = progress;					}
	public double getProgress()							{ return this.progress;						}

	//멤버구성 종류
	public void setPjtMbrType(String pjt_mbr_type)		{ this.pjt_mbr_type = pjt_mbr_type;			}
	public String getPjtMbrType()						{ return this.pjt_mbr_type;					}

	//멤버시작일
	public void setMbrStartDate(String mbr_start_date)	{ this.mbr_start_date = mbr_start_date;		}
	public String getMbrStartDate()						{ return this.mbr_start_date;				}

	//멤버완료일
	public void setMbrEndDate(String mbr_end_date)		{ this.mbr_end_date = mbr_end_date;			}
	public String getMbrEndDate()						{ return this.mbr_end_date;					}

	//멤버투입율
	public void setMbrPoration(double mbr_poration)		{ this.mbr_poration = mbr_poration;			}
	public double getMbrPoration()						{ return this.mbr_poration;					}

	//멤버이름
	public void setPjtMbrName(String pjt_mbr_name)		{ this.pjt_mbr_name = pjt_mbr_name;			}
	public String getPjtMbrName()						{ return this.pjt_mbr_name;					}

	//멤버담당업무
	public void setPjtMbrJob(String pjt_mbr_job)		{ this.pjt_mbr_job = pjt_mbr_job;			}
	public String getPjtMbrJob()						{ return this.pjt_mbr_job;					}

	//멤버전화번호
	public void setPjtMbrTel(String pjt_mbr_tel)		{ this.pjt_mbr_tel = pjt_mbr_tel;			}
	public String getPjtMbrTel()						{ return this.pjt_mbr_tel;					}

	//멤버직급
	public void setPjtMbrGrade(String pjt_mbr_grade)	{ this.pjt_mbr_grade = pjt_mbr_grade;		}
	public String getPjtMbrGrade()						{ return this.pjt_mbr_grade;				}

	//멤버부서코드
	public void setPjtMbrDiv(String pjt_mbr_div)		{ this.pjt_mbr_div = pjt_mbr_div;			}
	public String getPjtMbrDiv()						{ return this.pjt_mbr_div;					}

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

	//노드담당자사번
	public void setUserId(String user_id)			{ this.user_id = user_id;			}
	public String getUserId()						{ return this.user_id;				}

	//노드담당자이름
	public void setUserName(String user_name)		{ this.user_name = user_name;		}
	public String getUserName()						{ return this.user_name;			}

	//노드멤버
	public void setPjtNodeMbr(String pjt_node_mbr)	{ this.pjt_node_mbr = pjt_node_mbr;	}
	public String getPjtNodeMbr()					{ return this.pjt_node_mbr;			}

	//계획일수
	public void setPlanCnt(int plan_cnt)			{ this.plan_cnt = plan_cnt;			}
	public int getPlanCnt()							{ return this.plan_cnt;				}

	//수정일수
	public void setChgCnt(int chg_cnt)				{ this.chg_cnt = chg_cnt;			}
	public int getChgCnt()							{ return this.chg_cnt;				}

	//실적일수
	public void setResultCnt(int result_cnt)		{ this.result_cnt = result_cnt;		}
	public int getResultCnt()						{ return this.result_cnt;			}

	//노드상태
	public void setNodeStatus(String node_status)	{ this.node_status = node_status;	}
	public String getNodeStatus()					{ return this.node_status;			}

	//코맨트
	public void setRemark(String remark)			{ this.remark = remark;				}
	public String getRemark()						{ return this.remark;				}

	//일정변경사유
	public void setChgNote(String chg_note)			{ this.chg_note = chg_note;			}
	public String getChgNote()						{ return this.chg_note;				}

	//노드이름
	public void setNodeCode(String node_code)		{ this.node_code = node_code;		}
	public String getNodeCode()						{ return this.node_code;			}

	//주간/월간 실적구분
	public void setWmType(String wm_type)			{ this.wm_type = wm_type;			}
	public String getWmType()						{ return this.wm_type;				}

	//진행내용
	public void setEvtContent(String evt_content)	{ this.evt_content = evt_content;	}
	public String getEvtContent()					{ return this.evt_content;			}

	//문제점
	public void setEvtNote(String evt_note)			{ this.evt_note = evt_note;			}
	public String getEvtNote()						{ return this.evt_note;				}

	//이슈사항
	public void setEvtIssue(String evt_issue)		{ this.evt_issue = evt_issue;		}
	public String getEvtIssue()						{ return this.evt_issue;			}

	//경비항목
	public void setCostType(String cost_type)		{ this.cost_type = cost_type;		}
	public String getCostType()						{ return this.cost_type;			}

	//지출경비
	public void setNodeCost(String node_cost)		{ this.node_cost = node_cost;		}
	public String getNodeCost()						{ return this.node_cost;			}

	//환율
	public void setExchange(String exchange)		{ this.exchange = exchange;			}
	public String getExchange()						{ return this.exchange;				}

	//산출물항목 등록여부
	public void setUseDoc(String use_doc)			{ this.use_doc = use_doc;			}
	public String getUseDoc()						{ return this.use_doc;				}

	//문제점/이슈관리 담당자
	public void setUsers(String users)				{ this.users = users;				}
	public String getUsers()						{ return this.users;				}

	//문제점관리 문제점내용
	public void setNote(String note)				{ this.note = note;					}
	public String getNote()							{ return this.note;					}

	//문제점/이슈관리 대책
	public void setSolution(String solution)		{ this.solution = solution;			}
	public String getSolution()						{ return this.solution;				}

	//문제점/이슈관리 해결내용
	public void setContent(String content)			{ this.content = content;			}
	public String getContent()						{ return this.content;				}

	//문제점/이슈관리 해결일자
	public void setSolDate(String sol_date)			{ this.sol_date = sol_date;			}
	public String getSolDate()						{ return this.sol_date;				}

	//문제점/이슈관리 해결예정일자
	public void setBookDate(String book_date)		{ this.book_date = book_date;		}
	public String getBookDate()						{ return this.book_date;			}

	//문제점관리 상태
	public void setNoteStatus(String note_status)	{ this.note_status = note_status;	}
	public String getNoteStatus()					{ return this.note_status;			}

	//이슈관리 이슈사항
	public void setIssue(String issue)				{ this.issue = issue;				}
	public String getIssue()						{ return this.issue;				}

	//이슈관리 상태
	public void setIssueStatus(String issue_status)	{ this.issue_status = issue_status;	}
	public String getIssueStatus()					{ return this.issue_status;			}

	//수정
	public void setModify(String modify)			{ this.modify = modify;				}
	public String getModify()						{ return this.modify;				}

	//삭제
	public void setDelete(String delete)			{ this.delete = delete;				}
	public String getDelete()						{ return this.delete;				}

	//상세보기
	public void setView(String view)				{ this.view = view;					}
	public String getView()							{ return this.view;					}

	//검색 과제상태
	public void setPjtword(String pjtWord)			{ this.pjtWord = pjtWord;			}
	public String getPjtword()						{ return this.pjtWord;				}

	//Item
	public void setSitem(String sItem)				{ this.sItem = sItem;				}
	public String getSitem()						{ return this.sItem;				}

	//Word
	public void setSword(String sWord)				{ this.sWord = sWord;				}
	public String getSword()						{ return this.sWord;				}

	//Project관리자 사번
	public void setMgrId(String mgr_id)				{ this.mgr_id = mgr_id;				}
	public String getMgrId()						{ return this.mgr_id;				}

	//Project관리자 이름
	public void setMgrName(String mgr_name)			{ this.mgr_name = mgr_name;			}
	public String getMgrName()						{ return this.mgr_name;				}

	//프로젝트구분 (P:전사, 부서코드:부서)
	public void setType(String type)				{ this.type = type;					}
	public String getType()							{ return this.type;					}


}


