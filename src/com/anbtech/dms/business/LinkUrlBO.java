package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;

import java.sql.*;
import java.util.*;

public class LinkUrlBO{

	private Connection con;

	public LinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 문서 리스트를 볼 때 필요한 링크 생성
	 * category 가 들어가 있는 데이터의 경우
	 ***************************************/
	public LinkUrl getRedirect(String tablename,String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 10;
		int l_maxpage = 7;

		com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = masterBO.getWhere(mode,category,searchword);
		else where = masterBO.getWhere(mode,searchword, searchscope, category);

		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		int total = masterDAO.getTotalCount(tablename, where);

		// 전체 페이지의 값을 구한다.
		int totalpage = (int)(total / l_maxlist);
		
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
	
		
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		int curpage = 1;
		if (totalpage <= endpage)
			endpage = totalpage;
		
		if (Integer.parseInt(page) > l_maxpage){
			curpage = startpage -1;
			pagecut = "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();

		String link_write = "AnBDMS?tablename="+tablename+"&mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		// 카테고리 분류 가져오기
		com.anbtech.dms.admin.makeDocCategory cat = new com.anbtech.dms.admin.makeDocCategory();
		String where_category = cat.viewCategory(Integer.parseInt(category),"");

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		link.setWhereCategory(where_category);

		return link;
	}


	/*************************************************
	 * 대출의뢰 리스트를 볼 때 필요한 링크 문자열 생성
	 *************************************************/
	public LinkUrl getRedirect(String tablename,String mode, String searchword, String searchscope, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 10;
		int l_maxpage = 7;

		com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
		String where = loanBO.getWhere(mode,searchword, searchscope);

		com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
		int total = loanDAO.getTotalCount(tablename, where);

		// 전체 페이지의 값을 구한다.
		int totalpage = (int)(total / l_maxlist);
		
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
	
		
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		int curpage = 1;
		if (totalpage <= endpage)
			endpage = totalpage;
		
		if (Integer.parseInt(page) > l_maxpage){
			curpage = startpage -1;
			pagecut = "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();

		String link_write = "AnBDMS?tablename="+tablename+"&mode=write";
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";
		
		// 카테고리 분류 가져오기
		String where_category = "";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		link.setWhereCategory(where_category);

		return link;
	}



	/**********************************************
	 * 문서 내용상세보기시 필요한 링크 문자열 생성
	 **********************************************/
	public LinkUrl getLinkForView(String tablename,String mode,String category,String searchword,String searchscope,String page,String no,String data_id,String ver,String why_revision,String org_category) throws Exception{
		
		//리스트 보기 버튼
		String link_list = "";

		//각 카테고리로 분기한 후 보기를 한 경우
		if(mode.equals("view")){
			link_list = "AnBDMS?mode=list&category="+org_category+"&tablename="+tablename;
		}
		//처리중인 문서 리스트에서 보기를 한 경우
		else if(mode.equals("view_a")){
			link_list = "AnBDMS?mode=processing&category=1&tablename="+tablename;
		}
		//전체문서 목록에서 보기를 한 경우
		else if(mode.equals("view_t")){
			link_list = "AnBDMS?mode=list&category="+org_category+"&tablename=master_data";
		}
		//내폴더에서 보기를 한 경우
		else if(mode.equals("view_m")){
			link_list = "AnBDMS?mode=mylist&category=1&tablename="+tablename;
		}

		link_list += "&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;

		//수정 버튼
		String link_modify = "";
		if(mode.equals("view_m")) link_modify = "AnBDMS?tablename="+tablename+"&mode=modify&category="+category+"&searchscope="+searchscope;
		else if(mode.equals("view_a")) link_modify = "AnBDMS?tablename="+tablename+"&mode=modify_a&category="+category+"&searchscope="+searchscope;
	
		link_modify += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//삭제 버튼
		String link_delete = "javascript:confirm_del(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_delete += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";


		//리비젼 버튼
		String link_revision = "javascript:why_revision(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_revision += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//숨김개체
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='no' value='"+no+"'>";
		input_hidden += "<input type='hidden' name='d_id' value='"+data_id+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		input_hidden += "<input type='hidden' name='category' value='"+category+"'>";
		input_hidden += "<input type='hidden' name='org_category' value='"+org_category+"'>";
		input_hidden += "<input type='hidden' name='tablename' value='"+tablename+"'>";

		//등록처리 버튼
		String link_commit = "AnBDMS?tablename="+tablename+"&mode=commit2&category="+category+"&searchscope="+searchscope;
		link_commit += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//대출신청 버튼
		String link_loan = "javascript:req_loan(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_loan += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//프린터 화면 버튼
		String link_print = "AnBDMS?tablename="+tablename+"&mode=print&category="+category+"&searchscope="+searchscope;
		link_print += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//전자결재 상신 버튼
		String link_approval = "AnBDMS?tablename="+tablename+"&mode=approval&category="+category+"&searchscope="+searchscope;
		link_approval += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkRevision(link_revision);
		link.setInputHidden(input_hidden);
		link.setLinkCommit(link_commit);
		link.setLinkLoan(link_loan);
		link.setLinkDelete(link_delete);
		link.setLinkPrint(link_print);
		link.setLinkApproval(link_approval);

		return link;

	}

	/**********************************************
	 * 문서 수정 시 필요한 링크 문자열 생성
	 **********************************************/
	public LinkUrl getLinkForWrite(String tablename,String mode,String category,String searchword,String searchscope,String page,String no,String data_id,String ver) throws Exception{
		
		//카테고리 변경
		String link_change_category = "javascript:modify_category(\""+tablename+"\",\""+mode+"\",\""+category+"\",\""+searchscope+"\",";
		link_change_category += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkChangeCategory(link_change_category);

		//숨김개체
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		link.setInputHidden(input_hidden);

		return link;

	}


	/*************************************************
	 * 대출의뢰 문서 정보 상세보기시 필요한 링크 생성
	 *************************************************/
	public LinkUrl getLinkForView(String tablename,String mode,String searchword,String searchscope,String page,String no,String data_id,String ver) throws Exception{
		
		//리스트 보기 버튼
		String link_list = "AnBDMS?mode=loan";
		link_list += "&tablename="+tablename+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;

		//수정 버튼
		String link_modify = "AnBDMS?tablename="+tablename+"&mode=modify_l&searchscope="+searchscope;
		link_modify += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//대출처리 버튼
		String loan_process = "javascript:loan_process(\""+tablename+"\",\""+searchscope+"\",";
		loan_process += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//대출 삭제
		String delete_loan = "javascript:deleteloanConfirm(\""+tablename+"\",\""+searchscope+"\",";
		delete_loan += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//관련문서 보기 위한 URL
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		String mid = masterDAO.getMid(data_id);
		String link_doc_info = "AnBDMS?tablename="+tablename+"&mode=view&no="+mid+"&d_id="+data_id+"&ver="+ver;

		//숨김개체
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='no' value='"+no+"'>";
		input_hidden += "<input type='hidden' name='d_id' value='"+data_id+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		input_hidden += "<input type='hidden' name='tablename' value='"+tablename+"'>";

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setInputHidden(input_hidden);
		link.setLinkCommit(loan_process);
		link.setLinkDeleteLoan(delete_loan);
		link.setLinkDocInfoUrl(link_doc_info);

		return link;

	}

}