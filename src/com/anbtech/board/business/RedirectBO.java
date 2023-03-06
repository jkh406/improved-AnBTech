package com.anbtech.board.business;

import com.anbtech.board.entity.*;
import com.anbtech.board.db.*;

import java.sql.*;
import java.util.*;

public class RedirectBO{

	private Connection con;

	public RedirectBO(Connection con){
		this.con = con;
	}
	
	public Redirect getRedirect(String tablename, String searchword, String searchscope, String category, String boardpage, String mapping) throws Exception{

		Board_Env board_env = new Board_Env();
		Board_EnvDAO board_envDAO = new Board_EnvDAO(con);
		board_env = board_envDAO.getBoard_env(tablename);

		//list에서 사용할 변수를 board_env에서 가져온다.
		int l_maxlist = board_env.getL_maxlist();
		int l_maxpage = board_env.getL_maxpage();
		int enablechkcool = board_env.getEnablechkcool();
		String enablepreview = board_env.getEnablepreview();
		String category_items = board_env.getCategory_items();

		String where = getWhere(searchword, searchscope, category);

		TableDAO tableDAO = new TableDAO(con);
		int total = tableDAO.getTotal(tablename, where);

		// 전체 페이지의 값을 구한다.
		int totalpage = (int)(total / l_maxlist);
		
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(boardpage) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
	
		
		String pagecut = "";
		
		int curpage = 1;
		if (totalpage <= endpage)
			endpage = totalpage;
		
		if (Integer.parseInt(boardpage) > l_maxpage){
			curpage = startpage -1;
			pagecut = "<a href=AnBBoard?tablename=" + tablename + "&boardpage=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">[Prev]</a>";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(boardpage)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=AnBBoard?tablename=" + tablename + "&boardpage=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=AnBBoard?tablename=" + tablename + "&boardpage=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">[Next]</a>";
		}

		//redirect에 넣을 링크들 지금 만들기.
		Redirect redirect = new Redirect();
		String link_login =  "AnBBoard?tablename="+tablename+"&mode=adminlogin&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;
// 필요에 의해 수정함.(2003.02.25)
//		String link_manager =  mapping+"admin/settingTable.jsp?tablename="+tablename;
		String link_manager =  "http://127.0.0.1/OfficeWare/admin/settingTable.jsp?tablename="+tablename;
		String link_multiview =  "AnBBoard?tablename="+tablename+"&mode=view&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;
		String link_write = "AnBBoard?tablename="+tablename+"&mode=write"+"&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='tablename' VALUE='"+tablename+"'><INPUT TYPE=hidden NAME='mode' VALUE='list'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		redirect.setLink_login(link_login);
//		redirect.setLink_manager(link_manager);
		redirect.setLink_multiview(link_multiview);
		redirect.setLink_write(link_write);
		redirect.setInput_hidden(input_hidden);
		redirect.setView_total(total);
		redirect.setView_boardpage(Integer.parseInt(boardpage));
		redirect.setView_totalpage(totalpage);
		redirect.setView_pagecut(pagecut);

		return redirect;
	}

	public Redirect getRedirect(String tablename, String no, String mode, String searchword, String searchscope, String category, String boardpage) throws Exception{

		String depth = "";
		String thread = "";
		String pos = "";

		if(no != ""){
			Table table = new Table();
			TableDAO tableDAO = new TableDAO(con);
			table = tableDAO.getTable(tablename, no);
			
			depth = table.getDepth();
			thread = table.getThread();
			pos = table.getPos();
		}

		//redirect에 넣을 링크들 지금 만들기.
		Redirect redirect = new Redirect();
		String input_hidden = "<INPUT TYPE=hidden NAME=tablename VALUE='"+tablename+"'><INPUT TYPE=hidden NAME=mode VALUE='"+mode+"'><INPUT TYPE=hidden NAME=boardpage VALUE='"+boardpage+"'><INPUT TYPE=hidden NAME=searchword VALUE='"+searchword+"'><INPUT TYPE=hidden NAME=category VALUE='"+category+"'><INPUT TYPE=hidden NAME=searchscope VALUE='"+searchscope+"'><INPUT TYPE=hidden NAME=no VALUE='"+no+"'><INPUT TYPE=hidden NAME=thread VALUE='"+thread+"'><INPUT TYPE=hidden NAME=depth VALUE='"+depth+"'><INPUT TYPE=hidden NAME=pos VALUE='"+pos+"'>";
		redirect.setInput_hidden(input_hidden);

		return redirect;
	}

	public ArrayList getRedirect(String tablename, ArrayList no_list, String multino, String mode, String searchword, String searchscope, String category, String boardpage) throws Exception{

		Iterator no_iter = no_list.iterator();
		ArrayList redirect_view = new ArrayList();

		while(no_iter.hasNext()){

			String no = (String)no_iter.next();

			Table table = new Table();
			TableDAO tableDAO = new TableDAO(con);
			table = tableDAO.getTable(tablename, no);
			
			//redirect에 넣을 링크들 지금 만들기.
			Redirect 	redirect = new Redirect();
			String link_mailto =  "mailto:"+table.getEmail();
			String link_homepage = table.getHomepage();
			String link_modify = "AnBBoard?tablename="+tablename+"&mode=modify&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no;
			String link_delete = "AnBBoard?tablename="+tablename+"&mode=delete&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no;
			String link_download = "AnBBoard?tablename="+tablename+"&mode=download&no="+no;
			String link_vote = "AnBBoard?tablename="+tablename+"&mode=vote&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no+"&multino="+multino;
			String link_reply = "AnBBoard?tablename="+tablename+"&mode=reply&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&no="+no;
			String link_write = "AnBBoard?tablename="+tablename+"&mode=write"+"&category="+category;
			String link_list = "AnBBoard?tablename="+tablename+"&mode=list&boardpage="+boardpage+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category;
			String input_hidden = "<INPUT TYPE=hidden NAME=tablename VALUE='"+tablename+"'><INPUT TYPE=hidden NAME=mode VALUE='comment'><INPUT TYPE=hidden NAME=boardpage VALUE='"+boardpage+"'><INPUT TYPE=hidden NAME=searchword VALUE='"+searchword+"'><INPUT TYPE=hidden NAME=searchscope VALUE='"+searchscope+"'><INPUT TYPE=hidden NAME=category VALUE='"+category+"'><INPUT TYPE=hidden NAME=no VALUE='"+no+"'><INPUT TYPE=hidden NAME=multino VALUE='"+multino+"'>";

			redirect.setLink_mailto(link_mailto);
			redirect.setLink_homepage(link_homepage);
			redirect.setLink_modify(link_modify);
			redirect.setLink_delete(link_delete);
			redirect.setLink_download(link_download);
			redirect.setLink_vote(link_vote);
			redirect.setLink_reply(link_reply);
			redirect.setLink_write(link_write);
			redirect.setLink_list(link_list);
			redirect.setInput_hidden(input_hidden);

			redirect_view.add(redirect);
		}
		return redirect_view;
	}

	public String getWhere(String searchword, String searchscope, String category) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		if(category.length() > 0) where_cat = "category = '"+category+"'";
		if (searchword.length() > 0){
			if ("all".equals(searchscope))
				where_sea = "( writer like '%" +  searchword + "%' or subject like '%" +  searchword + "%' or content like '%" +  searchword + "%' or filename like '%" +  searchword + "%' )";
			if ("writer".equals(searchscope))
				where_sea = "( writer like '%" +  searchword + "%' )";
			if ("subject".equals(searchscope))
				where_sea = "( subject like '%" +  searchword + "%' )";
			if ("content".equals(searchscope))
				where_sea = "( content like '%" +  searchword + "%' )";
			if ("subjectcontent".equals(searchscope))
				where_sea = "( subject like '%" +  searchword + "%' or subject like '%" +  searchword +"' )";
			if ("filename".equals(searchscope))
				where_sea = "( filename like '%" +  searchword + "%' )";
		}
		if(category.length() > 0 && searchword.length() > 0) where_and = " and ";
		if(category.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;

		return where;
	}
}