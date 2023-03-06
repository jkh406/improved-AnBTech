package com.anbtech.qc.business;

import com.anbtech.qc.entity.*;
import com.anbtech.qc.db.*;

import java.sql.*;
import java.util.*;

public class QualityCtrlLinkUrlBO{

	private Connection con;

	public QualityCtrlLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public QualityCtrlLinkUrl getLinkForList(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		int l_maxpage = 7;

		com.anbtech.qc.business.QualityCtrlBO qcBO = new com.anbtech.qc.business.QualityCtrlBO(con);
		String where = qcBO.getWhere(mode,searchword, searchscope, category,login_id);

		com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
		int total = qcDAO.getTotalCount("qc_inspection_master", where);

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
			pagecut = "[<a href=QualityCtrlServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=QualityCtrlServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=QualityCtrlServlet?mode="+mode+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		QualityCtrlLinkUrl link = new QualityCtrlLinkUrl();

		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));

		return link;
	}

	/*************************************************
	 * 결과등록 및 결과정보 상세보기시의 링크 생성
	 *************************************************/
	public QualityCtrlLinkUrl getLinkForView(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception{
		QualityCtrlLinkUrl link = new QualityCtrlLinkUrl();
		
		String link_list = "QualityCtrlServlet?";
		if(mode.equals("write_inspect")) link_list += "mode=list_inspect";
		else if(mode.equals("view_result")) link_list += "mode=list_result";
		else if(mode.equals("write_return")) link_list += "mode=list_return";
		else if(mode.equals("write_rework")) link_list += "mode=list_rework";
		link_list += "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "&page=" + page;

		link.setLinkList(link_list);

		return link;
	}
}