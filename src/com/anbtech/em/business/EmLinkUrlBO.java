package com.anbtech.em.business;

import com.anbtech.em.entity.*;
import com.anbtech.em.db.*;

import java.sql.*;
import java.util.*;

public class EmLinkUrlBO{

	private Connection con;

	public EmLinkUrlBO(Connection con){
		this.con = con;
	}

	/*************************************************
	 * 견적서 목록 출력시 필요한 링크 생성
	 *************************************************/	
	public EmLinkUrl getListLink(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		EstimateBO estimateBO = new EstimateBO(con);
		String where = "";

		if(searchscope.equals("detail")) where = estimateBO.getWhereForDetail(mode,searchword,category,login_id);
		else where = estimateBO.getWhere(mode,searchword,searchscope,category,login_id);

		EstimateDAO estimateDAO = new EstimateDAO(con);
		int total = estimateDAO.getTotalCount("estimate_info", where);
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
			pagecut = "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		EmLinkUrl link = new EmLinkUrl();

		String link_write = "EstimateMgrServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/*************************************************
	 * 견적정보 상세 보기시에 필요한 링크 생성
	 *************************************************/
	public EmLinkUrl getViewLink(String mode,String searchword,String searchscope,String category,String page) throws Exception{
		EmLinkUrl link = new EmLinkUrl();
		
		String link_list = "";
		if(mode.equals("view")) link_list = "EstimateMgrServlet?mode=list";
		else if(mode.equals("view_my")) link_list = "EstimateMgrServlet?mode=mylist";
		link_list += "&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;

		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		input_hidden += "<INPUT TYPE=hidden NAME='searchscope' VALUE='"+searchscope+"'><INPUT TYPE=hidden NAME='searchword' VALUE='"+searchword+"'><INPUT TYPE=hidden NAME='page' VALUE='"+page+"'>";

		link.setLinkList(link_list);
		link.setInputHidden(input_hidden);

		return link;
	}


	/*************************************************
	 * 신규 견적서 작성(기본정보)시 필요한 링크 생성
	 *************************************************/
	public EmLinkUrl getWriteLink(String mode,String version) throws Exception{
		EmLinkUrl link = new EmLinkUrl();
		
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";
		input_hidden += "<INPUT TYPE=hidden NAME='ver' VALUE='"+version+"'>";
		
		link.setInputHidden(input_hidden);

		return link;
	}

	/*************************************************
	 * 외부품목 목록 출력시 필요한 링크 생성
	 *************************************************/	
	public EmLinkUrl getOutItemListLink(String mode,String searchword,String searchscope,String category,String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		EstimateItemBO itemBO = new EstimateItemBO(con);
		String where = itemBO.getWhere(mode,searchword, searchscope, category);

		EstimateItemDAO itemDAO = new EstimateItemDAO(con);
		int total = itemDAO.getTotalCount("v_out_item_list", where);
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
			pagecut = "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=EstimateMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		EmLinkUrl link = new EmLinkUrl();

		String link_write = "EstimateMgrServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/*************************************************
	 * 패키지 품목 목록 출력시 필요한 링크 생성
	 *************************************************/	
	public EmLinkUrl getPkgListLink(String mode,String searchword,String searchscope,String category,String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		EstimateItemBO itemBO = new EstimateItemBO(con);
		String where = itemBO.getWhere(mode,searchword, searchscope, category);

		EstimateItemDAO itemDAO = new EstimateItemDAO(con);
		int total = itemDAO.getTotalCount("package_list", where);
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
			pagecut = "[<a href=EstimateMgrServletServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=EstimateMgrServletServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=EstimateMgrServletServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		EmLinkUrl link = new EmLinkUrl();

		String link_write = "EstimateMgrServletServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}
}