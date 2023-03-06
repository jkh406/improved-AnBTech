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
	 * ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public CaLinkUrl getRedirect(String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list���� ����� ���� ����.
		int l_maxlist = 17;
		int l_maxpage = 7;

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = caBO.getWhere(mode,category,searchword);
		else where = caBO.getWhere(mode,searchword, searchscope, category);

		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		int total = caDAO.getTotalCount("ca_master", where);
		// ��ü �������� ���� ���Ѵ�.
		int totalpage = (int)(total / l_maxlist);

		
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
		
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
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
	 * �Ƿ��� ������� �����(flag='EN'), �űԵ�Ϲ���(ancestor='NEW') ����Ʈ �� �� �ʿ��� ��ũ
	 *******************************************************************************************/
	public CaLinkUrl getRedirect(String requestor_id,String page) throws Exception{

		//list���� ����� ���� ����.
		int l_maxlist = 17;
		int l_maxpage = 7;

		com.anbtech.ca.business.ComponentApprovalBO caBO = new com.anbtech.ca.business.ComponentApprovalBO(con);
		String where = " WHERE requestor_id = '" + requestor_id + "' and aid = 'EN' and ancestor = 'NEW'";

		com.anbtech.ca.db.ComponentApprovalDAO caDAO = new com.anbtech.ca.db.ComponentApprovalDAO(con);
		int total = caDAO.getTotalCount("ca_master", where);
		// ��ü �������� ���� ���Ѵ�.
		int totalpage = (int)(total / l_maxlist);

		
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
		
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
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