package com.anbtech.br.business;

import com.anbtech.br.entity.*;
import com.anbtech.br.db.*;

import java.sql.*;
import java.util.*;

public class CarLinkBO{

	private Connection con;

	public CarLinkBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * Paging 처리
	 ***************************************/
	public CarLinkTable getRedirect(String tablename,String mode, String searchword, String searchscope, String category, String page, String login_id,String cid) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		if(mode.equals("eachcar")) l_maxlist = 10;
		int l_maxpage = 7;
		
		com.anbtech.br.business.CarResourceBO carResourceBO = new com.anbtech.br.business.CarResourceBO(con);
		String where = carResourceBO.getWhere(mode,searchword, searchscope, category, login_id,cid);
		
		com.anbtech.br.db.CarResourceDAO carResourceDAO = new com.anbtech.br.db.CarResourceDAO(con);
		int total = carResourceDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=BookResourceServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&cid="+cid+">Prev</a>]";
		}

		curpage = startpage;
		
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=BookResourceServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&cid="+cid+">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		//System.out.println("totalpage => "+totalpage+" endpage=> "+endpage);
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=BookResourceServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&cid="+cid+">Next</a>]";
		}

		com.anbtech.br.entity.CarLinkTable link = new com.anbtech.br.entity.CarLinkTable();

		//String link_write = "BookResourceServlet?tablename="+tablename+"&mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		link.setViewPagecut(pagecut);
		//link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
	
		//System.out.println("pagecout => "+pagecut+" total=> "+Integer.toString(total)+" page => "+page+"  totalpage=>  "+Integer.toString(totalpage));
		return link;
	}

}