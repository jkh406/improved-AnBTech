package com.anbtech.bm.entity;
public class primeCostTable
{
	//Table ����
	private String item_code;							//ǰ���ڵ�
	private String item_name;							//ǰ���̸�
	private String item_desc;							//ǰ��԰�
	private String item_count;							//ǰ�����
	private String std_price;							//ǥ�شܰ�
	private String ave_price;							//��մܰ�
	private String cur_price;							//����ܰ�
	private String std_sum;								//ǥ�شܰ� �Ѿ�
	private String ave_sum;								//��մܰ� �Ѿ�
	private String cur_sum;								//����ܰ� �Ѿ�

	
	
	/*--------------------------- �޼ҵ� ����� -------------------------------*/
	//ǰ���ڵ�
	public void setItemCode(String item_code)			{ this.item_code = item_code;		}
	public String getItemCode()							{ return this.item_code;			}

	//ǰ���̸�
	public void setItemName(String item_name)			{ this.item_name = item_name;		}
	public String getItemName()							{ return this.item_name;			}

	//ǰ��԰�
	public void setItemDesc(String item_desc)			{ this.item_desc = item_desc;		}
	public String getItemDesc()							{ return this.item_desc;			}

	//ǰ�����
	public void setItemCount(String item_count)			{ this.item_count = item_count;		}
	public String getItemCount()						{ return this.item_count;			}
	
	//ǥ�شܰ�
	public void setStdPrice(String std_price)			{ this.std_price = std_price;		}
	public String getStdPrice()							{ return this.std_price;			}
	
	//��մܰ�
	public void setAvePrice(String ave_price)			{ this.ave_price = ave_price;		}
	public String getAvePrice()							{ return this.ave_price;			}
	
	//����ܰ�
	public void setCurPrice(String cur_price)			{ this.cur_price = cur_price;		}
	public String getCurPrice()							{ return this.cur_price;			}

	//ǥ�شܰ� �Ѿ�
	public void setStdSum(String std_sum)				{ this.std_sum = std_sum;			}
	public String getStdSum()							{ return this.std_sum;				}
	
	//��մܰ� �Ѿ�
	public void setAveSum(String ave_sum)				{ this.ave_sum = ave_sum;			}
	public String getAveSum()							{ return this.ave_sum;				}
	
	//����ܰ� �Ѿ�
	public void setCurSum(String cur_sum)				{ this.cur_sum = cur_sum;			}
	public String getCurSum()							{ return this.cur_sum;				}

	
}




