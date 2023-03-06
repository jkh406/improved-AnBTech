package com.anbtech.am.business;

import com.anbtech.am.entity.*;
import com.anbtech.am.db.*;

import java.sql.*;
import java.util.*;


public class AMLinkBO 
{
	private Connection con;

	public AMLinkBO(Connection con){
		this.con = con;
	}

	/***************************************
	 * ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public AMLinkTable getRedirect(String tablename,String mode, String searchword, String searchscope,  String page,String c_no,String div,String ct_id,String login_division) throws Exception{                //tablename,      mode,      login_id,           c_no,              tablename,mode,c_no,searchscope,page,as_no,"app",ct_id,login_division
		//list���� ����� ���� ����.
		int l_maxlist = 15;
		int l_maxpage = 7;
		String as_no="";

		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		String where = " WHERE as_status!='10'";

		boolean bool = false;
//		bool = assetModuleDAO.chk_privilege(searchword,div); // searchword => login_id
		String temp = "";
		if(bool) { temp ="1"; } else { temp="2"; }
		

		if(div.equals("detail") && mode.equals("asset_del_list")) {			// �󼼰˻��ϰ��
			   where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
		} else if(div.equals("general") && mode.equals("asset_del_list") ){
			   where = assetModuleBO.getWhere4(c_no,searchword, searchscope,ct_id);
		} else if(div.equals("detail")) {			// �󼼰˻��ϰ��
			   where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
		} else if(div.equals("each")){		// �Ϲ� �˻��� ���
			   where = assetModuleBO.getWhere(mode,searchword,searchscope,c_no,"each");
			   c_no=searchscope;			// ���� searchscope���� c_no�� ����Ǿ� ���� 
		} else if(mode.equals("req_app_list")){		// ����� 1�� ���� �Ϸ� ���� 
			   as_no=c_no;
			   where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"app");
		} else  if(div.equals("general")) {	// �Ϲ� �˻�
				where = assetModuleBO.getWhere2(c_no,searchword, searchscope, ct_id,div);
		} else  if(mode.equals("req_app_list")) {
				where =" WHERE as_status='3' and type='2' and (o_status ='t' or o_status = 'o')";
		} else  if(mode.equals("asset_del_list")) 
				where = " WHERE as_status='10' ";

		 if("TransOutList".equals(mode) && "lending".equals(div)){	// �뿩 ó�� ���
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,"lending");
		} else if("EnteringList".equals(mode) && "lending".equals(div)){	// �뿩 �ݳ� ���
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		} else if("lending_list".equals(mode) && "lending".equals(div)){	// �뿩 ���� ���
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		} else if("EnteringList".equals(mode)){									// �԰�ó�����
				where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		} else if("TransOutList".equals(mode)){								// ����/�̰� ���
				where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		} 
			
	//System.out.println("where:"+where);
		int total = assetModuleDAO.getTotalCount(tablename, where);

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
			pagecut = "[<a href=AssetServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage +"&c_no="+c_no+"&searchscope=" + searchscope +"&div="+div+"&ct_id="+ct_id+"&login_division="+login_division+"&searchword=" + searchword +">Prev</a>]";
		}
		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=AssetServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage +"&c_no="+c_no+"&searchscope=" + searchscope +"&div="+div+"&ct_id="+ct_id+"&login_division="+login_division+ "&searchword=" + searchword +">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=AssetServlet?tablename="+tablename+"&mode="+mode+"&page=" + curpage + "&c_no="+c_no+ "&searchscope=" + searchscope +"&div="+div+"&ct_id="+ct_id+"&login_division="+login_division+"&searchword=" + searchword + ">Next</a>]";
		}
		
		com.anbtech.am.entity.AMLinkTable link = new com.anbtech.am.entity.AMLinkTable();

		//String link_write = "AssetServlet?tablename="+tablename+"&mode=write&category="+category;
		//String input_hidden = "<INPUT TYPE=hidden NAME='mode' VALUE='"+mode+"'><INPUT TYPE=hidden NAME='category' VALUE='"+category+"'>";
		
		// ī�װ� �з� ��������
		com.anbtech.am.admin.makeCtTree cat = new com.anbtech.am.admin.makeCtTree();
		String where_category = cat.viewCategory(c_no,"");

		link.setViewPagecut(pagecut);
		//link.setLinkWriter(link_write);
		//link.setInputHidden(input_hidden);
		link.setViewTotal(total);
		link.setViewBoardpage(page);
		link.setViewtotalpage(totalpage);
		link.setWhereCategory(where_category);
		
		return link;
	}


	//****************************************//
	// link �����	(�뿩/����/�̰�/�԰�/���)
	//****************************************//
	public String makeLink(boolean bool,String u_date,String o_status,String as_status,String u_id,int h_no,String as_no,String login_id, String info_status) throws Exception {
	
		String link="";
		String str = u_id+","+h_no+","+as_no+","+o_status+"'"+as_status;
		//\""+tablename+"\",\""+category+"\",\""

		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		String plus_status="";
		if(info_status.equals("13")) plus_status ="������";
	
			
			if(o_status.equals("l") && as_status.equals("3")) {			// �뿩 1�� ���� ����
					
			} else if(o_status.equals("l") && as_status.equals("5")) {		// �뿩 2�� ����Ϸ� ����(�뿩 ��⹮��)
				if (bool) {
					
					if(!info_status.equals("13")) {
						link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"lending_process\")'><IMG src='../am/images/lt_lend_bt.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_lending\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
					} else {link=plus_status;}

				} else {
				
					link ="�뿩�����"+plus_status;
				}
					
			
			} else if(o_status.equals("l") && as_status.equals("16")) {    // �뿩 ���� ����
				
				if (bool){
					link ="<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_InputForm\")'><IMG src='../am/images/lt_banab_bt.gif' border='0' align='absmiddle'></a>";
				} else{
					link = "�뿩��";
				}
				
			} else if(o_status.equals("l") && as_status.equals("17")) {    // �ݳ� �Ϸ�
			
				link ="�ݳ��Ϸ�";
			} else if(o_status.equals("t") && (as_status.equals("3") || as_status.equals("5"))) {  // �̰��� 1/2�� ���� �Ϸ� �����ϰ��
				
				String temp = "transfer_process";		

				if(bool) {	
					if(!info_status.equals("13")) {
					link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"transfer_process\")'><IMG src='../am/images/lt_tran_bt.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_transfer\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
					} else {link=plus_status;}
				} else {
					link ="�̰������"+plus_status;;		// �����ڰ� �̰� ����ڿ� Ʋ�����(�� 3��) ����� ǥ��
				}

			} else if(o_status.equals("o") && as_status.equals("5")){ // ���� 2�� ���� �Ϸ� ������ ���
				
				if(bool) {							  //  ���� �����ڰ� ������ ��� ���� ó�� ����
					link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_process\")'><IMG src='../am/images/lt_banchul.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_out\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
				} else {
					link ="��������";		// �����ڰ� �̰� ����ڿ� Ʋ�����(�� 3��) ���� ����� ǥ��		
				}
			
			} else if(o_status.equals("t") && as_status.equals("11")){
				link ="�̰��Ϸ�";

			} else if(o_status.equals("t") && as_status.equals("9")){
				link ="�̰����";

			} else if(o_status.equals("o") && as_status.equals("9")){
				link ="�������";

			} else if(o_status.equals("l") && as_status.equals("9")){
				link ="�뿩���";

			} else if(o_status.equals("o") && as_status.equals("7")){
				
				if(bool){
					link ="<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_InputForm\")'><IMG src='../am/images/lt_banib_bt.gif' border='0' align='absmiddle'></a>";
				} else {
					link="������";			
				}

			} else if(o_status.equals("o") && as_status.equals("12")){
				link ="�԰�Ϸ�";
			} else if((o_status.equals("l") || o_status.equals("o") )&& as_status.equals("13")) {
				link ="����";
			} else if(o_status.equals("o") && as_status.equals("13")){
				link ="����";
			} else if (o_status.equals("l") && as_status.equals("12")){
				link ="�ݳ��Ϸ�";
			}
		//}
		return link;
	}

}
