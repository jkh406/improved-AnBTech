package com.anbtech.gm.business;

import com.anbtech.gm.entity.*;
import com.anbtech.gm.db.*;
import com.anbtech.gm.business.*;

import java.sql.*;
import java.util.*;

public class GmLinkUrlBO{

	private Connection con;

	public GmLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public GmLinkUrl getRedirect(String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 17;
		int l_maxpage = 7;

		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String where = goodsBO.getWhere(mode,searchword,searchscope,category);

		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);
		int total = goodsDAO.getTotalCount("goods_structure",where);
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
			pagecut = "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		GmLinkUrl link = new GmLinkUrl();

		String link_write = "GoodsInfoServlet?mode=req_item_no&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/***************************************
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public GmLinkUrl searchRedirect(String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 10;
		int l_maxpage = 7;

		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);
		String where = "";//goodsBO.getWhere(mode,searchword,searchscope,category);
		String query = "";
		String tablename = "";
			
		//검색조건에 맞는 게시물을 가져온다.
		if (searchscope.equals("item_no"))	{
			where = goodsBO.getWhereByScope(mode,searchword, searchscope, category);
			query = "SELECT model_code, item_no FROM item_master "+where+" ORDER BY mid DESC";
			tablename ="item_master";
		} else {
			where = goodsBO.getWhere(mode,searchword, searchscope, category);
			query = "SELECT mid, code FROM goods_structure "+where+" ORDER BY mid DESC";
			tablename ="goods_structure";
		}				

		int total = goodsDAO.getTotalCount(tablename,where);
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
			pagecut = "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=GoodsInfoServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		GmLinkUrl link = new GmLinkUrl();

		String link_write = "GoodsInfoServlet?mode=req_item_no&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/***************************************
	 * 모델정보 상세보기 시 필요한 링크 생성
	 ***************************************/
	public GmLinkUrl getRedirect(String mode, String searchword, String searchscope, String category, String page, String mid) throws Exception{

		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);
		String gcode = goodsDAO.getGcodeByMid(mid);

		String link_list = "GoodsInfoServlet?mode=list_model&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category;		
		String link_modify = "GoodsInfoServlet?mode=mod_model&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "&no=" + mid + "&one_class=" + gcode.substring(0,2) + "&two_class=" + gcode.substring(0,4) + "&three_class=" + gcode.substring(0,6);
		String link_delete = "GoodsInfoServlet?mode=del_model&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "&no=" + mid;

		String link_revision = "GoodsInfoServlet?mode=rev_model&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "&no=" + mid + "&one_class=" + gcode.substring(0,2) + "&two_class=" + gcode.substring(0,4) + "&three_class=" + gcode.substring(0,6);

		GmLinkUrl link = new GmLinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkDelete(link_delete);
		link.setLinkRevision(link_revision);

		return link;
	}

	/***************************************
	 * 모델정보 입력 또는 수정 시 필요한 링크 생성
	 ***************************************/
	public GmLinkUrl getLinkUrlForInput(String mode,String searchword, String searchscope, String category, String page, String mid) throws Exception{

		String input_hidden  = "<input type='hidden' name='no' value='" + mid + "'>";
		input_hidden += "<input type='hidden' name='searchword' value='" + searchword + "'>";
		input_hidden += "<input type='hidden' name='searchscope' value='" + searchscope + "'>";
		input_hidden += "<input type='hidden' name='page' value='" + page + "'>";

		GmLinkUrl link = new GmLinkUrl();
		link.setInputHidden(input_hidden);

		return link;
	}

}