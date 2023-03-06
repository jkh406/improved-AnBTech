package com.anbtech.pu.business;

import com.anbtech.pu.entity.*;
import com.anbtech.pu.db.*;

import java.sql.*;
import java.util.*;

public class PurchaseConfigLinkUrlBO{

	private Connection con;

	public PurchaseConfigLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 품목공급정보 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public PurchaseConfigLinkUrl getRedirect(String tablename,String mode, String searchword, String searchscope, String page) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		int l_maxpage = 7;

		com.anbtech.pu.business.PurchaseConfigMgrBO purchaseBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
		String where = "";
		tablename = " pu_item_supply_info a, item_master b, company_customer c ";
		where = purchaseBO.getWhere(mode, searchscope, searchword);

		com.anbtech.pu.db.PurchaseConfigMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
		int total = purchaseDAO.getTotalCount(tablename, where);

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
			pagecut = "<a href=PurchaseConfigMgrServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[Prev]</a>";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PurchaseConfigMgrServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PurchaseConfigMgrServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[Next]</a>";
		}

		com.anbtech.pu.entity.PurchaseConfigLinkUrl link = new com.anbtech.pu.entity.PurchaseConfigLinkUrl();

		String link_write = "PurchaseConfigMgrServlet?tablename="+tablename+"&mode="+mode;
		String input_hidden =  "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";//<INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
	
		return link;
	}

	/***************************************
	 * 품목단가정보 리스트를 볼 때의 링크 문자열 생성
	 ***************************************/
	public PurchaseConfigLinkUrl getRedirectForItemCostList(String mode, String searchword, String searchscope, String page) throws Exception{
		com.anbtech.pu.business.PurchaseConfigMgrBO purchaseconfigBO = new com.anbtech.pu.business.PurchaseConfigMgrBO(con);
		com.anbtech.pu.db.PurchaseConfigMgrDAO purchaseconfigDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
		com.anbtech.pu.entity.PurchaseConfigLinkUrl link = new com.anbtech.pu.entity.PurchaseConfigLinkUrl();

		int l_maxlist = 15;
		int l_maxpage = 7;

		String where = purchaseconfigBO.getWhereForItemCostList(mode, searchscope, searchword);
		int total = purchaseconfigDAO.getTotalCount("st_item_unit_cost", where);

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
			pagecut = "<a href=PurchaseConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[Prev]</a>";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=PurchaseConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=PurchaseConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">[Next]</a>";
		}

		String link_write = "PurchaseConfigMgrServlet?mode="+mode;
		String input_hidden =  "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";
		
		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
	
		return link;
	}
}