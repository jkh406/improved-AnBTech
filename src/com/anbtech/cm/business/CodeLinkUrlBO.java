package com.anbtech.cm.business;

import com.anbtech.cm.entity.*;
import com.anbtech.cm.db.*;

import java.sql.*;
import java.util.*;

public class CodeLinkUrlBO{

	private Connection con;

	public CodeLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/***************************************
	 * ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public CodeLinkUrl getRedirect(String mode,String category,String code_b,String code_m,String code_s,String searchword,String searchscope,String page,String login_id) throws Exception{

		int l_maxlist = 15;
		int l_maxpage = 7;
		if(mode.equals("list_item_p")) l_maxlist = 10;
		CodeMgrBO cmBO = new CodeMgrBO(con);
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);

		String where = cmBO.getWhere(mode,category,code_b,code_m,code_s,searchword,searchscope,login_id);

		int total = cmDAO.getTotalCount("item_master",where);
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
			pagecut = "[<a href=CodeMgrServlet?mode="+mode+"&category="+category+"&code_big=" + code_b + "&code_mid=" + code_m + "&code_small="+ code_s + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=CodeMgrServlet?mode="+mode+"&category="+category+"&code_big=" + code_b + "&code_mid=" + code_m + "&code_small="+ code_s + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=CodeMgrServlet?mode="+mode+"&category="+category+"&code_big=" + code_b + "&code_mid=" + code_m + "&code_small="+ code_s + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		CodeLinkUrl link = new CodeLinkUrl();

		String link_write = "CodeMgrServlet?mode=reg_item_no";
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'><INPUT TYPE=hidden NAME='code_big' VALUE='"+code_b+"'><INPUT TYPE=hidden NAME='code_mid' VALUE='"+code_m+"'><INPUT TYPE=hidden NAME='code_small' VALUE='"+code_s+"'>";

		link.setViewPagecut(pagecut);
		link.setLinkWriter(link_write);
		link.setInputHidden(input_hidden);
		link.setViewTotal(Integer.toString(total));
		link.setViewBoardpage(page);
		link.setViewtotalpage(Integer.toString(totalpage));

		return link;
	}


	/***************************************
	 * ǰ������ �󼼺���ÿ� �ʿ��� ��ũ ����
	 ***************************************/
	public CodeLinkUrl getRedirectForView(String mode,String category,String item_no,String searchword,String searchscope,String page,String login_id) throws Exception{

		//����Ʈ ���� ��ư
		String link_list = "CodeMgrServlet?mode=list_item&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;
		
		//���� ��ư
		String link_modify = "";
		if(mode.equals("view_item")){
			link_modify = "CodeMgrServlet?mode=modify_item&item_no="+item_no+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;
		}else if(mode.equals("view_item2")){
			link_modify = "CodeMgrServlet?mode=reg_item2&item_no="+item_no+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;
		}
	

		//���� ��ư
		String link_delete = "CodeMgrServlet?mode=delete_item&item_no="+item_no+"&category="+category+"&searchscope="+searchscope+"&searchword="+searchword+"&page="+page;


		//���谳ü
		String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";

		CodeLinkUrl link = new CodeLinkUrl();
		link.setLinkList(link_list);
		link.setLinkModify(link_modify);
		link.setInputHidden(input_hidden);
		link.setLinkDelete(link_delete);

		return link;
	}

	/******************************************** 
	* ÷��ȭ�� �ٿ�ε� Link String �����
	*********************************************/
	public String getDownFileLink(String item_no) throws Exception {
					
	com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
	ArrayList file_list = new ArrayList();
	file_list = cmDAO.getFile_list(item_no);

    com.anbtech.cm.entity.PartInfoTable partInfo = new com.anbtech.cm.entity.PartInfoTable();
	Iterator file_iter = file_list.iterator();
	String downfile = "";
	
	// ---- ÷������ ----
	int k = 1;
		while(file_iter.hasNext()){
		partInfo = (PartInfoTable)file_iter.next();
		downfile = downfile+"<a href='../servlet/CodeMgrServlet?mode=download&item_no="+partInfo.getFileUmask()+"' onMouseOver=\"window.status='Download "+partInfo.getFileName()+" ("+partInfo.getFileSize()+" bytes)';return true;\" onMouseOut=\"window.status='';return true;\">";
		downfile += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(partInfo.getFileName(), partInfo.getFileType()) +".gif' border=0> "+partInfo.getFileName()+"</a> ("+partInfo.getFileSize()+") bytes <br>";		
		k++;
		}
		return downfile;	
	}

}