package com.anbtech.share.business;

import com.anbtech.share.entity.*;
import com.anbtech.share.db.*;

import java.sql.*;
import java.util.*;

public class ShareLinkBO{

	private Connection con;

	public ShareLinkBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 
	 ***************************************/
	public ShareLinkTable getRedirect(String tablename,String mode,String searchword,String searchscope,String category,String page,String no) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		int l_maxpage = 7;
		
		com.anbtech.share.business.ShareBdBO shbBO = new com.anbtech.share.business.ShareBdBO(con);
		com.anbtech.share.db.ShareBdDAO shbDAO = new com.anbtech.share.db.ShareBdDAO(con);
		
		String where = shbBO.getWhere(tablename,mode,searchword,searchscope,category);
		int total = shbDAO.getTotalCount(tablename, where);

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
			pagecut = "<a href=ShareBdServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope +">[Prev]</a>";
		}

		curpage = startpage;
		
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=ShareBdServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope +">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		//System.out.println("totalpage => "+totalpage+" endpage=> "+endpage);
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=ShareBdServlet?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope +">[Next]</a>";
		}
		
		com.anbtech.share.entity.ShareLinkTable link = new com.anbtech.share.entity.ShareLinkTable();
		
		//String link_write = "ShareBdServlet?tablename="+tablename+"&mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='tablename' VALUE='"+tablename+"'>";
		input_hidden = input_hidden+"<INPUT TYPE=hidden NAME='searchscope' VALUE='"+searchscope+"'><INPUT TYPE=hidden NAME='searchword' VALUE='"+searchword+"'><INPUT TYPE=hidden NAME='page' VALUE='"+page+"'>";
	  
		String link_list	= "<a href=ShareBdServlet?mode=list&tablename="+tablename+"&page=" + page + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope+"&no="+no+">";

		String link_modify	= "<a href=ShareBdServlet?mode=modify&tablename="+tablename+"&page=" + page + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope+"&no="+no+">";

		String link_delete	= "<a href=ShareBdServlet?mode=delete&tablename="+tablename+"&page=" + page + "&category=" + category + "&searchword=" + searchword + "&searchscope=" + searchscope+"&no="+no+">";
		
		link.setViewPagecut(""+pagecut);
		//link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(""+total);
		link.setViewBoardpage(""+page);
		link.setViewtotalpage(""+totalpage);
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkDelete(link_delete);

		return link;
	}

	
	/****************
	* 정보 테이블 	*
	****************/
	public String[] makeParameter(String tablename,String mode,String page,String searchword,String searchscope,String category) throws Exception {
		
/*		com.anbtech.share.entity.ShareLinkTable slTable = new com.anbtech.share.entity.ShareLinkTable();

		slTable.setTableName(tablename);
		slTable.setMode(mode);
		slTable.setViewBoardpage(page);
		slTable.setSearchWord(searchword);
		slTable.setSearchScope(searchscope);
		slTable.setCategory(category);
*/		String str[] = new String[7];
		str[0] = tablename;
		str[1] = mode;
		str[2] = page;
		str[3] = searchword;
		str[4] = searchscope;
		str[5] = category;
	
		return str;
	}

}