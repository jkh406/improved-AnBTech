package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;

import java.sql.*;
import java.util.*;

public class LinkUrlBO{

	private Connection con;

	public LinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * ���� ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 * category �� �� �ִ� �������� ���
	 ***************************************/
	public LinkUrl getRedirect(String tablename,String mode, String searchword, String searchscope, String category, String page) throws Exception{

		//list���� ����� ���� ����.
		int l_maxlist = 10;
		int l_maxpage = 7;

		com.anbtech.dms.business.MasterBO masterBO = new com.anbtech.dms.business.MasterBO(con);
		String where = "";
		if(searchscope.equals("detail")) where = masterBO.getWhere(mode,category,searchword);
		else where = masterBO.getWhere(mode,searchword, searchscope, category);

		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		int total = masterDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=AnBDMS?mode="+mode+"&tablename="+tablename+"&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + "&category=" + category + ">Next</a>]";
		}

		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();

		String link_write = "AnBDMS?tablename="+tablename+"&mode=write&category="+category;
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		// ī�װ� �з� ��������
		com.anbtech.dms.admin.makeDocCategory cat = new com.anbtech.dms.admin.makeDocCategory();
		String where_category = cat.viewCategory(Integer.parseInt(category),"");

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		link.setWhereCategory(where_category);

		return link;
	}


	/*************************************************
	 * �����Ƿ� ����Ʈ�� �� �� �ʿ��� ��ũ ���ڿ� ����
	 *************************************************/
	public LinkUrl getRedirect(String tablename,String mode, String searchword, String searchscope, String page) throws Exception{

		//list���� ����� ���� ����.
		int l_maxlist = 10;
		int l_maxpage = 7;

		com.anbtech.dms.business.LoanBO loanBO = new com.anbtech.dms.business.LoanBO(con);
		String where = loanBO.getWhere(mode,searchword, searchscope);

		com.anbtech.dms.db.LoanDAO loanDAO = new com.anbtech.dms.db.LoanDAO(con);
		int total = loanDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=AnBDMS?tablename="+tablename+"&mode=loan&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();

		String link_write = "AnBDMS?tablename="+tablename+"&mode=write";
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'>";
		
		// ī�װ� �з� ��������
		String where_category = "";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));
		link.setWhereCategory(where_category);

		return link;
	}



	/**********************************************
	 * ���� ����󼼺���� �ʿ��� ��ũ ���ڿ� ����
	 **********************************************/
	public LinkUrl getLinkForView(String tablename,String mode,String category,String searchword,String searchscope,String page,String no,String data_id,String ver,String why_revision,String org_category) throws Exception{
		
		//����Ʈ ���� ��ư
		String link_list = "";

		//�� ī�װ��� �б��� �� ���⸦ �� ���
		if(mode.equals("view")){
			link_list = "AnBDMS?mode=list&category="+org_category+"&tablename="+tablename;
		}
		//ó������ ���� ����Ʈ���� ���⸦ �� ���
		else if(mode.equals("view_a")){
			link_list = "AnBDMS?mode=processing&category=1&tablename="+tablename;
		}
		//��ü���� ��Ͽ��� ���⸦ �� ���
		else if(mode.equals("view_t")){
			link_list = "AnBDMS?mode=list&category="+org_category+"&tablename=master_data";
		}
		//���������� ���⸦ �� ���
		else if(mode.equals("view_m")){
			link_list = "AnBDMS?mode=mylist&category=1&tablename="+tablename;
		}

		link_list += "&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;

		//���� ��ư
		String link_modify = "";
		if(mode.equals("view_m")) link_modify = "AnBDMS?tablename="+tablename+"&mode=modify&category="+category+"&searchscope="+searchscope;
		else if(mode.equals("view_a")) link_modify = "AnBDMS?tablename="+tablename+"&mode=modify_a&category="+category+"&searchscope="+searchscope;
	
		link_modify += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//���� ��ư
		String link_delete = "javascript:confirm_del(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_delete += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";


		//������ ��ư
		String link_revision = "javascript:why_revision(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_revision += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//���谳ü
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='no' value='"+no+"'>";
		input_hidden += "<input type='hidden' name='d_id' value='"+data_id+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		input_hidden += "<input type='hidden' name='category' value='"+category+"'>";
		input_hidden += "<input type='hidden' name='org_category' value='"+org_category+"'>";
		input_hidden += "<input type='hidden' name='tablename' value='"+tablename+"'>";

		//���ó�� ��ư
		String link_commit = "AnBDMS?tablename="+tablename+"&mode=commit2&category="+category+"&searchscope="+searchscope;
		link_commit += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//�����û ��ư
		String link_loan = "javascript:req_loan(\""+tablename+"\",\""+category+"\",\""+searchscope+"\",";
		link_loan += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//������ ȭ�� ��ư
		String link_print = "AnBDMS?tablename="+tablename+"&mode=print&category="+category+"&searchscope="+searchscope;
		link_print += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//���ڰ��� ��� ��ư
		String link_approval = "AnBDMS?tablename="+tablename+"&mode=approval&category="+category+"&searchscope="+searchscope;
		link_approval += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setLinkRevision(link_revision);
		link.setInputHidden(input_hidden);
		link.setLinkCommit(link_commit);
		link.setLinkLoan(link_loan);
		link.setLinkDelete(link_delete);
		link.setLinkPrint(link_print);
		link.setLinkApproval(link_approval);

		return link;

	}

	/**********************************************
	 * ���� ���� �� �ʿ��� ��ũ ���ڿ� ����
	 **********************************************/
	public LinkUrl getLinkForWrite(String tablename,String mode,String category,String searchword,String searchscope,String page,String no,String data_id,String ver) throws Exception{
		
		//ī�װ� ����
		String link_change_category = "javascript:modify_category(\""+tablename+"\",\""+mode+"\",\""+category+"\",\""+searchscope+"\",";
		link_change_category += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkChangeCategory(link_change_category);

		//���谳ü
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		link.setInputHidden(input_hidden);

		return link;

	}


	/*************************************************
	 * �����Ƿ� ���� ���� �󼼺���� �ʿ��� ��ũ ����
	 *************************************************/
	public LinkUrl getLinkForView(String tablename,String mode,String searchword,String searchscope,String page,String no,String data_id,String ver) throws Exception{
		
		//����Ʈ ���� ��ư
		String link_list = "AnBDMS?mode=loan";
		link_list += "&tablename="+tablename+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;

		//���� ��ư
		String link_modify = "AnBDMS?tablename="+tablename+"&mode=modify_l&searchscope="+searchscope;
		link_modify += "&searchword="+searchword+"&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;

		//����ó�� ��ư
		String loan_process = "javascript:loan_process(\""+tablename+"\",\""+searchscope+"\",";
		loan_process += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//���� ����
		String delete_loan = "javascript:deleteloanConfirm(\""+tablename+"\",\""+searchscope+"\",";
		delete_loan += "\""+searchword+"\",\""+page+"\",\""+no+"\",\""+data_id+"\",\""+ver+"\");";

		//���ù��� ���� ���� URL
		com.anbtech.dms.db.MasterDAO masterDAO = new com.anbtech.dms.db.MasterDAO(con);
		String mid = masterDAO.getMid(data_id);
		String link_doc_info = "AnBDMS?tablename="+tablename+"&mode=view&no="+mid+"&d_id="+data_id+"&ver="+ver;

		//���谳ü
		String input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='no' value='"+no+"'>";
		input_hidden += "<input type='hidden' name='d_id' value='"+data_id+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";
		input_hidden += "<input type='hidden' name='tablename' value='"+tablename+"'>";

  		com.anbtech.dms.entity.LinkUrl link = new com.anbtech.dms.entity.LinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setInputHidden(input_hidden);
		link.setLinkCommit(loan_process);
		link.setLinkDeleteLoan(delete_loan);
		link.setLinkDocInfoUrl(link_doc_info);

		return link;

	}

}