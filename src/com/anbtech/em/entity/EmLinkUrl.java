/************************************************************
 * 각종 링크 문자열을 만드는데 사용될 변수의 getter/setter
 ************************************************************/

package com.anbtech.em.entity;

public class EmLinkUrl{

	private String view_pagecut;		// 페이지 이동관련 링크 [prev] [1][2][3] [next]
	private String input_hidden;		// hidden 타입의 객체 문자열, 검색시에 사용하기 위함.
	private String view_total;			// 검색된 문서의 개수
	private String view_boardpage;		// 현재페이지 표시 문자열
	private String view_totalpage;		// 전체페이지 표시 문자열
	private String write_estimate;		// 견적 기본정보 입력버튼
	private String write_estimate_item;	// 견적 품목 입력 버튼
	private String link_list;			// 목록보기 버튼

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