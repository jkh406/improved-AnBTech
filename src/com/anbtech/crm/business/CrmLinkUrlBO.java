package com.anbtech.crm.business;

import com.anbtech.crm.entity.*;
import com.anbtech.crm.db.*;

import java.sql.*;
import java.util.*;

public class CrmLinkUrlBO{

	private Connection con;

	public CrmLinkUrlBO(Connection con){
		this.con = con;
	}

	/*************************************************
	 * 고객회사 목록 출력시 필요한 링크 생성
	 *************************************************/	
	public CrmLinkUrl getCompanyListLink(String mode,String module,String searchword,String searchscope,String category,String page,String login_id) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		int l_maxpage = 7;

		CrmBO crmBO = new CrmBO(con);
		String where = "";

		where = crmBO.getWhere(mode,searchword,searchscope,category,login_id);

		CrmDAO crmDAO = new CrmDAO(con);
		int total = crmDAO.getTotalCount("company_customer", where);
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
			pagecut = "[<a href=CrmServlet?mode=" + mode + "&module=" + module + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=CrmServlet?mode=" + mode + "&module=" + module + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=CrmServlet?mode=" + mode + "&module=" + module + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		CrmLinkUrl link = new CrmLinkUrl();

		String link_write = "CrmServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='module' VALUE='"+module+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/*************************************************
	 * 고객회사 상세 보기시에 필요한 링크 생성
	 *************************************************/
	public CrmLinkUrl getCompanyViewLink(String no,String mode,String module,String searchword,String searchscope,String category,String page) throws Exception{
		
		com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
		CrmLinkUrl link = new CrmLinkUrl();
		CompanyInfoTable company = new CompanyInfoTable();
		
		String link_list = "CrmServlet?mode=company_list&module="+module+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&page="+page;
		String link_modify = "CrmServlet?mode=company_modify&module="+module+"&no=" + no+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&page="+page;
		String link_delete = "CrmServlet?mode=company_delete&module="+module+"&no=" + no;
		String file_link   = "";

		
		company = crmDAO.getCompanyInfo(no);

		//첨부파일 리스트 가져오기
		String mid = company.getMid();
		String umask = company.getFileUmask();

		Iterator file_iter = crmDAO.getFile_list(mid).iterator();

		int i = 1;
		String filelink = "&nbsp;";
//		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			CompanyInfoTable file = (CompanyInfoTable)file_iter.next();
			filelink += "<a href='CrmServlet?mode=download&no="+mid+"_"+i+"&umask="+umask+"_"+i+"'";
			filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='../crm/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
			filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";
			i++;
		}
/*
			if (file.getFileType().indexOf("mage")>0){
				filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='CrmServlet?mode=download&no="+mid+"_"+i+"' border=1></TD></TR>";
			}
			
		*/
//		filepreview = filepreview + "</TABLE>";
		
//		table.setFilePreview(filepreview);

		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkDelete(link_delete);
		link.setFileLink(filelink);

		return link;
	}

	/*************************************************
	 * 고객회사 등록,수정시에 필요한 링크 생성
	 *************************************************/
	public CrmLinkUrl getCompanyWriteLink(String no,String mode,String module,String searchword,String searchscope,String category,String page) throws Exception{
		CrmLinkUrl link = new CrmLinkUrl();
		
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='module' VALUE='"+module+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		link.setInputHidden(input_hidden);

		return link;
	}


	/*************************************************
	 * 고객 목록 출력시 필요한 링크 생성
	 *************************************************/	
	public CrmLinkUrl getCustomerListLink(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception{

		//list에서 사용할 변수 설정.
		int l_maxlist = 15;
		int l_maxpage = 7;

		CrmBO crmBO = new CrmBO(con);
		String where = "";

		where = crmBO.getWhere(mode,searchword,searchscope,category,login_id);

		CrmDAO crmDAO = new CrmDAO(con);
		int total = crmDAO.getTotalCount("personal_customer", where);
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
			pagecut = "<a href=CrmServlet?mode=customer_list&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "><img src='../crm/images/back.gif' border='0' align='absmiddle'></a> | ";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage + " | ";
			}else {
				pagecut = pagecut + "<a href=CrmServlet?mode=customer_list&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a> | ";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CrmServlet?mode=customer_list&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + "><img src='../crm/images/next.gif' border='0' align='absmiddle'></a>";
		}

		CrmLinkUrl link = new CrmLinkUrl();

		String link_write = "CrmServlet?mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		link.setViewPagecut(pagecut);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		return link;
	}

	/*************************************************
	 * 고객 상세 보기시에 필요한 링크 생성
	 *************************************************/
	public CrmLinkUrl getCustomerViewLink(String no,String mode,String searchword,String searchscope,String category,String page) throws Exception{
		CrmLinkUrl link = new CrmLinkUrl();
		
		String link_list = "CrmServlet?mode=customer_list&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&page="+page;
		String link_modify = "CrmServlet?mode=customer_modify&no=" + no+"&searchword="+searchword+"&searchscope="+searchscope+"&category="+category+"&page="+page;
		String link_delete = "CrmServlet?mode=customer_delete&no=" + no;

		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkDelete(link_delete);

		return link;
	}

	/*************************************************
	 * 고객 등록,수정시에 필요한 링크 생성
	 *************************************************/
	public CrmLinkUrl getCustomerWriteLink(String no,String mode,String searchword,String searchscope,String category,String page) throws Exception{
		CrmLinkUrl link = new CrmLinkUrl();
		
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		link.setInputHidden(input_hidden);

		return link;
	}
}