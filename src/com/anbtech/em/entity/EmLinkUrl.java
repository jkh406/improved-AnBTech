/************************************************************
 * ���� ��ũ ���ڿ��� ����µ� ���� ������ getter/setter
 ************************************************************/

package com.anbtech.em.entity;

public class EmLinkUrl{

	private String view_pagecut;		// ������ �̵����� ��ũ [prev] [1][2][3] [next]
	private String input_hidden;		// hidden Ÿ���� ��ü ���ڿ�, �˻��ÿ� ����ϱ� ����.
	private String view_total;			// �˻��� ������ ����
	private String view_boardpage;		// ���������� ǥ�� ���ڿ�
	private String view_totalpage;		// ��ü������ ǥ�� ���ڿ�
	private String write_estimate;		// ���� �⺻���� �Է¹�ư
	private String write_estimate_item;	// ���� ǰ�� �Է� ��ư
	private String link_list;			// ��Ϻ��� ��ư

	public String getViewPagecut(){
		return view_pagecut;
	}
	public void setViewPagecut(String view_pagecut){
		this.view_pagecut = view_pagecut;
	}

	public String getInputHidden(){
		return input_hidden;
	}
	public void setInputHidden(String input_hidden){
		this.input_hidden = input_hidden;
	}

	public String getViewTotal(){
		return view_total;
	}
	public void setViewTotal(String view_total){
		this.view_total = view_total;
	}

	public String getViewBoardpage(){
		return view_boardpage;
	}
	public void setViewBoardpage(String view_boardpage){
		this.view_boardpage = view_boardpage;
	}

	public String getViewTotalpage(){
		return view_totalpage;
	}
	public void setViewtotalpage(String view_totalpage){
		this.view_totalpage = view_totalpage;
	}

	public String getWriteEstimate(){
		return write_estimate;
	}
	public void setWriteEstimate(String write_estimate){
		this.write_estimate = write_estimate;
	}

	public String getWriteEstimateItem(){
		return write_estimate_item;
	}
	public void setWriteEstimateItem(String write_estimate_item){
		this.write_estimate_item = write_estimate_item;
	}

	public String getLinkList(){
		return link_list;
	}
	public void setLinkList(String link_list){
		this.link_list = link_list;
	}
}