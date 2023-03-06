package com.anbtech.bs.business;

import com.anbtech.bs.entity.*;
import com.anbtech.bs.db.*;

import java.sql.*;
import java.util.*;

public class SalesConfigLinkUrlBO{

	private Connection con;

	public SalesConfigLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * ǰ��ܰ����� ����Ʈ�� �� ���� ��ũ ���ڿ� ����
	 ***************************************/
	public SalesConfigLinkUrl getRedirectForList(String tablename,String mode,String searchword,String searchscope,String page) throws Exception{
		SalesConfigMgrBO scBO = new SalesConfigMgrBO(con);
		SalesConfigMgrDAO scDAO = new SalesConfigMgrDAO(con);
		SalesConfigLinkUrl link = new SalesConfigLinkUrl();

		int l_maxlist = 15;
		int l_maxpage = 7;

		String where = scBO.getWhereForList(mode, searchscope, searchword);
		int total = scDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=SalesConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=SalesConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=SalesConfigMgrServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		String link_write = "SalesConfigMgrServlet?mode="+mode;
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