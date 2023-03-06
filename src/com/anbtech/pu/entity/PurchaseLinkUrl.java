/************************************************************
 * 각종 링크 문자열을 만드는데 사용될 변수의 getter/setter
 ************************************************************/

package com.anbtech.pu.entity;

public class PurchaseLinkUrl{

	private String view_pagecut;		// 페이지 이동관련 링크 [prev] [1][2][3] [next]
	private String view_total;			// 검색된 문서의 개수
	private String view_boardpage;		// 현재페이지 표시 문자열
	private String view_totalpage;		// 전체페이지 표시 문자열

	private String link_info_modify;	// 정보수정 버튼
	private String link_info_delete;	// 정보삭제 버튼
	private String link_item_add;		// 품목등록 버튼
	private String link_item_modify;	// 품목수정 버튼
	private String link_item_delete;	// 품목삭제 버튼
	private String link_list;			// 리스트 보기 버튼
	private String link_approval;		// 결재상신 버튼
	private String link_print;			// 발주서출력 버튼
	private String link_request_info;	// 요청정보 보기 버튼
	private String input_hidden;		// hidden 타입의 객체 문자열, 검색시에 사용하기 위함.
	private String link_supplyer_assign;// 발주업체자동지정 버튼
	private String link_app_info;		// 결재 정보화면 보기 버튼


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