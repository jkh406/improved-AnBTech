/************************************************************
 * ���� ��ũ ���ڿ��� ����µ� ���� ������ getter/setter
 ************************************************************/

package com.anbtech.crm.entity;

public class CrmLinkUrl{

	private String view_pagecut;		// ������ �̵����� ��ũ [prev] [1][2][3] [next]
	private String input_hidden;		// hidden Ÿ���� ��ü ���ڿ�, �˻��ÿ� ����ϱ� ����.
	private String view_total;			// �˻��� ������ ����
	private String view_boardpage;		// ���������� ǥ�� ���ڿ�
	private String view_totalpage;		// ��ü������ ǥ�� ���ڿ�
	private String link_list;			// ��Ϻ��� ��ư
	private String link_modify;			// ���� ��ư
	private String link_delete;			// ���� ��ư
	private String file_link;			// ���ϸ�ũ

	public String getViewPagecut(){		return view_pagecut;	}
	public void setViewPagecut(String view_pagecut){		this.view_pagecut = view_pagecut;	}

	public String getInputHidden(){		return input_hidden;	}
	public void setInputHidden(String input_hidden){		this.input_hidden = input_hidden;	}

	public String getViewTotal(){		return view_total;	}
	public void setViewTotal(String view_total){		this.view_total = view_total;	}

	public String getViewBoardpage(){		return view_boardpage;	}
	public void setViewBoardpage(String view_boardpage){		this.view_boardpage = view_boardpage;	}

	public String getViewTotalpage(){		return view_totalpage;	}
	public void setViewtotalpage(String view_totalpage){		this.view_totalpage = view_totalpage;	}

	public String getLinkList(){		return link_list;	}
	public void setLinkList(String link_list){		this.link_list = link_list;	}

	public String getLinkModify(){		return link_modify;	}
	public void setLinkModify(String link_modify){		this.link_modify = link_modify;	}

	public String getLinkDelete(){		return link_delete;	}
	public void setLinkDelete(String link_delete){		this.link_delete = link_delete;	}

	public String getFileLink(){		return file_link;	}
	public void setFileLink(String file_link){		this.file_link = file_link;	}
}