package com.anbtech.st.business;

import com.anbtech.st.entity.*;
import com.anbtech.st.db.*;

import java.sql.*;
import java.util.*;

public class StockLinkUrlBO{

	private Connection con;

	public StockLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public StockLinkUrl getRedirectForEtcInOutList(String mode,String in_or_out,String searchword,String searchscope,String page,String login_id) throws Exception{
		StockMgrBO stBO			= new StockMgrBO(con);
		StockMgrDAO stDAO		= new StockMgrDAO(con);

		int l_maxlist = 15;
		int l_maxpage = 7;

		String where = stBO.getWhereForEtcInOutInfoList(mode,in_or_out,searchword,searchscope,login_id);
		int total = stDAO.getTotalCount("st_etc_inout_info", where);
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
			pagecut = "[<a href=StockMgrServlet?mode=" + mode + "&in_or_out=" + in_or_out + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=StockMgrServlet?mode=" + mode + "&in_or_out=" + in_or_out + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=StockMgrServlet?mode=" + mode + "&in_or_out=" + in_or_out + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		StockLinkUrl link = new StockLinkUrl();

		String link_write = "";//"PurchaseMgrServlet?mode=reg_item_no";
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";
		link.setViewPagecut(pagecut);
		//link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));

		return link;
	}

	/***************************************
	 * ���� ��Ȳ����Ʈ ��½� �ʿ��� ��ũ ����
	 ***************************************/
	public StockLinkUrl getRedirectForList(String mode,String tablename,String searchword,String searchscope,String page,String login_id) throws Exception{
		StockMgrBO stBO			= new StockMgrBO(con);
		StockMgrDAO stDAO		= new StockMgrDAO(con);

		int l_maxlist = 15;
		int l_maxpage = 7;

		String where = "";
		//table�� ���� where ������ �����´�.
		if(tablename.equals("st_inout_master")) where = stBO.getWhereForInOutList(mode,searchword,login_id);
		else if(tablename.equals("v_item_stock_master")) where = stBO.getWhereForItemStockList(mode,searchword,login_id);
		else if(tablename.equals("st_reserved_item_info")) where = stBO.getWhereForReservedItemList(mode,searchword,login_id);
		else if(tablename.equals("st_item_shift_info")) where = stBO.getWhereForShiftInfoList(mode,searchword,searchscope);
		else if(tablename.equals("v_deliveried_info")) where = stBO.getWhereForDeliveriedInfoList(mode,searchword,searchscope,login_id);

		int total = stDAO.getTotalCount(tablename, where);
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
			pagecut = "[<a href=StockMgrServlet?mode="+mode + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=StockMgrServlet?mode="+mode+ "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=StockMgrServlet?mode="+mode+ "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		StockLinkUrl link = new StockLinkUrl();

		String link_write = "";
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));

		return link;
	}
}