package com.anbtech.dcm.entity;
public class eccModelTable
{
	//Table ����
	private String pid;							//�����ڵ�
	private String eco_no;						//���躯�������ȣ
	private String gid;							//�׷�����ڵ�
	private String model_code;					//���ڵ�
	private String model_name;					//�𵨸�
	private String fg_code;						//FG�ڵ�
	private String order_date;					//��������
	private String ecc_status;					//�������
	private String m_status;					//��ǰ�������
	
	/*--------------------------- �޼ҵ� ����� -------------------------------*/
	//�����ڵ�
	public void setPid(String pid)					{ this.pid = pid;					}
	public String getPid()							{ return this.pid;					}

	//���躯�������ȣ
	public void setEcoNo(String eco_no)				{ this.eco_no = eco_no;				}
	public String getEcoNo()						{ return this.eco_no;				}
		
	//�𵨱׷������ȣ
	public void setGid(String gid)					{ this.gid = gid;					}
	public String getGid()							{ return this.gid;					}

	//���ڵ�
	public void setModelCode(String model_code)		{ this.model_code = model_code;		}
	public String getModelCode()					{ return this.model_code;			}

	//�𵨸�
	public void setModelName(String model_name)		{ this.model_name = model_name;		}
	public String getModelName()					{ return this.model_name;			}

	//FG�ڵ�
	public void setFgCode(String fg_code)			{ this.fg_code = fg_code;			}
	public String getFgCode()						{ return this.fg_code;				}

	//��������
	public void setOrderDate(String order_date)		{ this.order_date = order_date;		}
	public String getOrderDate()					{ return this.order_date;			}

	//�������
	public void setEccStatus(String ecc_status)		{ this.ecc_status = ecc_status;		}
	public String getEccStatus()					{ return this.ecc_status;			}

	//��ǰ�������
	public void setMStatus(String m_status)			{ this.m_status = m_status;			}
	public String getMStatus()						{ return this.m_status;				}

}


