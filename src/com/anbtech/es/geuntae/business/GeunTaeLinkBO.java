package com.anbtech.es.geuntae.business;

import com.anbtech.es.geuntae.entity.*;
import com.anbtech.es.geuntae.db.*;

import java.sql.*;
import java.util.*;

public class GeunTaeLinkBO{

	private Connection con;

	public GeunTaeLinkBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * ���� ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public GeunTaeLink getRedirect(String tablename,String mode, String searchword, String searchscope, String category, String page, String login_id) throws Exception{

		//list���� ����� ���� ����.
		int l_maxlist = 15;
		int l_maxpage = 7;

		com.anbtech.es.geuntae.business.GeunTaeBO geuntaeBO = new com.anbtech.es.geuntae.business.GeunTaeBO(con);
		String where = geuntaeBO.getWhere(mode,searchword, searchscope, category);

		com.anbtech.es.geuntae.db.GeunTaeDAO geuntaeDAO = new com.anbtech.es.geuntae.db.GeunTaeDAO(con);
		int total = geuntaeDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=GeunTaeServlet?tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=GeunTaeServlet?tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=GeunTaeServlet?tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		com.anbtech.es.geuntae.entity.GeunTaeLink link = new com.anbtech.es.geuntae.entity.GeunTaeLink();

		String link_write = "GeunTaeServlet?tablename="+tablename+"&mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));

		return link;
	}
}