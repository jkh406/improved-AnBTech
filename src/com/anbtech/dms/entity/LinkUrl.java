/************************************************************
 * ���� ��ũ ���ڿ��� ����µ� ���� ������ getter/setter
 ************************************************************/

package com.anbtech.dms.entity;

public class LinkUrl{

	private String view_pagecut;		// ������ �̵����� ��ũ [prev] [1][2][3] [next]
	private String link_write;			// ���� ��� ��ư
	private String link_modify;			// ���� ���� ��ư
	private String link_revision;		// ���� ���� ��ư
	private String link_delete;			// ���� ���� ��ư
	private String link_list;			// ����Ʈ ���� ��ư
	private String input_hidden;		// hidden Ÿ���� ��ü ���ڿ�, �˻��ÿ� ����ϱ� ����.
	private String view_total;			// �˻��� ������ ����
	private String view_boardpage;		// ���������� ǥ�� ���ڿ�
	private String view_totalpage;		// ��ü������ ǥ�� ���ڿ�
	private String where_category;		// ����ī�װ��� ���� �з� ǥ��
	private String link_commit;			// ó�� ��ư
	private String link_reject;			// �ݷ� ��ư
	private String link_loan;			// �����û
	private String link_change_category;// ī�װ� ���� ��ư
	private String link_delete_loan;	// ī�װ� ���� ��ư
	private String link_doc_info_url;	// �������� ���� �� �ش� ���� ���� ���� URL
	private String link_print;			// ������ ȭ�� ��ư
	private String link_approval;		// ���ڰ��� ���



	public String getViewPagecut(){
		return view_pagecut;
	}
	public void setViewPagecut(String view_pagecut){
		this.view_pagecut = view_pagecut;
	}

	public String getLinkWriter(){
		return link_write;
	}
	public void setLinkWriter(String link_write){
		this.link_write = link_write;
	}

	public String getLinkModify(){
		return link_modify;
	}
	public void setLinkModify(String link_modify){
		this.link_modify = link_modify;
	}

	public String getLinkRevision(){
		return link_revision;
	}
	public void setLinkRevision(String link_revision){
		this.link_revision = link_revision;
	}

	public String getLinkDelete(){
		return link_delete;
	}
	public void setLinkDelete(String link_delete){
		this.link_delete = link_delete;
	}

	public String getLinkList(){
		return link_list;
	}
	public void setLinkList(String link_list){
		this.link_list = link_list;
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

	public String getWhereCategory(){
		return where_category;
	}
	public void setWhereCategory(String where_category){
		this.where_category = where_category;
	}

	public String getLinkCommit(){
		return link_commit;
	}
	public void setLinkCommit(String link_commit){
		this.link_commit = link_commit;
	}

	public String getLinkReject(){
		return link_reject;
	}
	public void setLinkReject(String link_reject){
		this.link_reject = link_reject;
	}

	public String getLinkLoan(){
		return link_loan;
	}
	public void setLinkLoan(String link_loan){
		this.link_loan = link_loan;
	}

	public String getLinkChangeCategory(){
		return link_change_category;
	}
	public void setLinkChangeCategory(String link_change_category){
		this.link_change_category = link_change_category;
	}

	public String getLinkDeleteLoan(){
		return link_delete_loan;
	}

	public void setLinkDeleteLoan(String link_delete_loan){
		this.link_delete_loan = link_delete_loan;
	}


	public String getLinkDocInfoUrl(){
		return link_doc_info_url;
	}

	public void setLinkDocInfoUrl(String link_doc_info_url){
		this.link_doc_info_url = link_doc_info_url;
	}

	public String getLinkPrint(){
		return link_print;
	}
	public void setLinkPrint(String link_print){
		this.link_print = link_print;
	}

	public String getLinkApproval(){
		return link_approval;
	}
	public void setLinkApproval(String link_approval){
		this.link_approval = link_approval;
	}
}