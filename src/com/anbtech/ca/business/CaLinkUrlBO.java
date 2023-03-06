package com.anbtech.ca.business;

import com.anbtech.ca.entity.*;
import com.anbtech.ca.db.*;

import java.sql.*;
import java.util.*;

public class CaLinkUrlBO{

	private Connection con;

	public CaLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public CaLinkUrl getRedirect(String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = caBO.getWhere(mode,category,searchword);
		else where = caBO.getWhere(mode,searchword, searchscope, category);

		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		int total = caDAO.getTotalCount("ca_master", where);
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
			pagecut = "[<a href=ComponentApprovalServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=ComponentApprovalServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=ComponentApprovalServlet?page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		com.anbtech.ca.entity.CaLinkUrl link = new com.anbtech.ca.entity.CaLinkUrl();

		String link_write = "ComponentApprovalServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/*******************************************************************************************
	 * 의뢰자 사번별로 상신전(flag='EN'), 신규등록문건(ancestor='NEW') 리스트 볼 때 필요한 링크
	 *******************************************************************************************/
	public CaLinkUrl getRedirect(String requestor_id,String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = " WHERE requestor_id = '" + requestor_id + "' and aid = 'EN' and ancestor = 'NEW'";

		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		int total = caDAO.getTotalCount("ca_master", where);
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
			pagecut = "[<a href=ComponentApprovalServlet?page=" + curpage + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=ComponentApprovalServlet?page=" + curpage + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=ComponentApprovalServlet?page=" + curpage + ">Next</a>]";
		}

		com.anbtech.ca.entity.CaLinkUrl link = new com.anbtech.ca.entity.CaLinkUrl();

		String link_write = "ComponentApprovalServlet?mode=write";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}
}