/************************************************************
 * ���� ��ũ ���ڿ��� ����µ� ���� ������ getter/setter
 ************************************************************/

package com.anbtech.pu.entity;

public class PurchaseLinkUrl{

	private String view_pagecut;		// ������ �̵����� ��ũ [prev] [1][2][3] [next]
	private String view_total;			// �˻��� ������ ����
	private String view_boardpage;		// ���������� ǥ�� ���ڿ�
	private String view_totalpage;		// ��ü������ ǥ�� ���ڿ�

	private String link_info_modify;	// �������� ��ư
	private String link_info_delete;	// �������� ��ư
	private String link_item_add;		// ǰ���� ��ư
	private String link_item_modify;	// ǰ����� ��ư
	private String link_item_delete;	// ǰ����� ��ư
	private String link_list;			// ����Ʈ ���� ��ư
	private String link_approval;		// ������ ��ư
	private String link_print;			// ���ּ���� ��ư
	private String link_request_info;	// ��û���� ���� ��ư
	private String input_hidden;		// hidden Ÿ���� ��ü ���ڿ�, �˻��ÿ� ����ϱ� ����.
	private String link_supplyer_assign;// ���־�ü�ڵ����� ��ư
	private String link_app_info;		// ���� ����ȭ�� ���� ��ư


	public String getViewPagecut(){		return view_pagecut;	}
	public void setViewPagecut(String view_pagecut){		this.view_pagecut = view_pagecut;	}

	public String getViewTotal(){		return view_total;	}
	public void setViewTotal(String view_total){		this.view_total = view_total;	}

	public String getViewBoardpage(){		return view_boardpage;	}
	public void setViewBoardpage(String view_boardpage){		this.view_boardpage = view_boardpage;	}

	public String getViewTotalpage(){		return view_totalpage;	}
	public void setViewtotalpage(String view_totalpage){		this.view_totalpage = view_totalpage;	}

	public String getLinkInfoModify(){		return link_info_modify;	}
	public void setLinkInfoModify(String link_info_modify){		this.link_info_modify = link_info_modify;	}

	public String getLinkInfoDelete(){		return link_info_delete;	}
	public void setLinkInfoDelete(String link_info_delete){		this.link_info_delete = link_info_delete;	}

	public String getLinkItemAdd(){		return link_item_add;	}
	public void setLinkItemAdd(String link_item_add){		this.link_item_add = link_item_add;	}

	public String getLinkItemModify(){		return link_item_modify;	}
	public void setLinkItemModify(String link_item_modify){		this.link_item_modify = link_item_modify;	}

	public String getLinkItemDelete(){		return link_item_delete;	}
	public void setLinkItemDelete(String link_item_delete){		this.link_item_delete = link_item_delete;	}

	public String getLinkList(){		return link_list;	}
	public void setLinkList(String link_list){		this.link_list = link_list;	}

	public String getLinkApproval(){		return link_approval;	}
	public void setLinkApproval(String link_approval){		this.link_approval = link_approval;	}

	public String getLinkPrint(){		return link_print;	}
	public void setLinkPrint(String link_print){		this.link_print = link_print;	}

	public String getInputHidden(){		return input_hidden;	}
	public void setInputHidden(String input_hidden){		this.input_hidden = input_hidden;	}

	public String getLinkRequestInfo(){	return link_request_info;}
	public void setLinkRequestInfo(String link_request_info)	{this.link_request_info = link_request_info;}

	public String getLinkSupplyerAssign(){	return link_supplyer_assign;}
	public void setLinkSupplyerAssign(String link_supplyer_assign)	{this.link_supplyer_assign = link_supplyer_assign;}

	public String getLinkAppInfo(){	return link_app_info;}
	public void setLinkAppInfo(String link_app_info)	{this.link_app_info = link_app_info;}
}