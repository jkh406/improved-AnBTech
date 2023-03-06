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
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public AMLinkTable getRedirect(String tablename,String mode, String searchword, String searchscope,  String page,String c_no,String div,String ct_id,String login_division) throws Exception{                //tablename,      mode,      login_id,           c_no,              tablename,mode,c_no,searchscope,page,as_no,"app",ct_id,login_division
		//list에서 사용할 변수 설정.
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
		

		if(div.equals("detail") && mode.equals("asset_del_list")) {			// 상세검색일경우
			   where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
		} else if(div.equals("general") && mode.equals("asset_del_list") ){
			   where = assetModuleBO.getWhere4(c_no,searchword, searchscope,ct_id);
		} else if(div.equals("detail")) {			// 상세검색일경우
			   where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
		} else if(div.equals("each")){		// 일반 검색일 경우
			   where = assetModuleBO.getWhere(mode,searchword,searchscope,c_no,"each");
			   c_no=searchscope;			// 현재 searchscope에는 c_no가 저장되어 있음 
		} else if(mode.equals("req_app_list")){		// 반출시 1차 결재 완료 문건 
			   as_no=c_no;
			   where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"app");
		} else  if(div.equals("general")) {	// 일반 검색
				where = assetModuleBO.getWhere2(c_no,searchword, searchscope, ct_id,div);
		} else  if(mode.equals("req_app_list")) {
				where =" WHERE as_status='3' and type='2' and (o_status ='t' or o_status = 'o')";
		} else  if(mode.equals("asset_del_list")) 
				where = " WHERE as_status='10' ";

		 if("TransOutList".equals(mode) && "lending".equals(div)){	// 대여 처리 대상
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,"lending");
		} else if("EnteringList".equals(mode) && "lending".equals(div)){	// 대여 반납 대상
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		} else if("lending_list".equals(mode) && "lending".equals(div)){	// 대여 결재 대상
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		} else if("EnteringList".equals(mode)){									// 입고처리대상
				where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		} else if("TransOutList".equals(mode)){								// 반출/이관 대상
				where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		} 
			
	//System.out.println("where:"+where);
		int total = assetModuleDAO.getTotalCount(tablename, where);

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
		
		// 카테고리 분류 가져오기
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
	// link 만들기	(대여/반출/이관/입고/취소)
	//****************************************//
	public String makeLink(boolean bool,String u_date,String o_status,String as_status,String u_id,int h_no,String as_no,String login_id, String info_status) throws Exception {
	
		String link="";
		String str = u_id+","+h_no+","+as_no+","+o_status+"'"+as_status;
		//\""+tablename+"\",\""+category+"\",\""

		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		String plus_status="";
		if(info_status.equals("13")) plus_status ="수리중";
	
			
			if(o_status.equals("l") && as_status.equals("3")) {			// 대여 1차 결재 문건
					
			} else if(o_status.equals("l") && as_status.equals("5")) {		// 대여 2차 결재완료 문건(대여 대기문건)
				if (bool) {
					
					if(!info_status.equals("13")) {
						link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"lending_process\")'><IMG src='../am/images/lt_lend_bt.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_lending\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
					} else {link=plus_status;}

				} else {
				
					link ="대여대기중"+plus_status;
				}
					
			
			} else if(o_status.equals("l") && as_status.equals("16")) {    // 대여 중인 문거
				
				if (bool){
					link ="<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_InputForm\")'><IMG src='../am/images/lt_banab_bt.gif' border='0' align='absmiddle'></a>";
				} else{
					link = "대여중";
				}
				
			} else if(o_status.equals("l") && as_status.equals("17")) {    // 반납 완료
			
				link ="반납완료";
			} else if(o_status.equals("t") && (as_status.equals("3") || as_status.equals("5"))) {  // 이관시 1/2차 결재 완료 문건일경우
				
				String temp = "transfer_process";		

				if(bool) {	
					if(!info_status.equals("13")) {
					link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"transfer_process\")'><IMG src='../am/images/lt_tran_bt.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_transfer\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
					} else {link=plus_status;}
				} else {
					link ="이관대기중"+plus_status;;		// 접속자가 이관 대상자와 틀리경우(제 3자) 대기중 표시
				}

			} else if(o_status.equals("o") && as_status.equals("5")){ // 반출 2차 결재 완료 문건일 경우
				
				if(bool) {							  //  현재 접속자가 권한자 경우 반출 처리 가능
					link = "<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_process\")'><IMG src='../am/images/lt_banchul.gif' border='0' align='absmiddle'></a>&nbsp;<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"cancel_out\")'><IMG src='../am/images/lt_cancel_bt.gif' border='0' align='absmiddle'></a>";
				} else {
					link ="반출대기중";		// 접속자가 이관 대상자와 틀릴경우(제 3자) 반출 대기중 표시		
				}
			
			} else if(o_status.equals("t") && as_status.equals("11")){
				link ="이관완료";

			} else if(o_status.equals("t") && as_status.equals("9")){
				link ="이관취소";

			} else if(o_status.equals("o") && as_status.equals("9")){
				link ="반출취소";

			} else if(o_status.equals("l") && as_status.equals("9")){
				link ="대여취소";

			} else if(o_status.equals("o") && as_status.equals("7")){
				
				if(bool){
					link ="<a href='javascript:go_as_process(\""+u_date+"\",\""+u_id+"\",\""+h_no+"\",\""+as_no+"\",\""+o_status+"\",\""+as_status+"\",\"out_InputForm\")'><IMG src='../am/images/lt_banib_bt.gif' border='0' align='absmiddle'></a>";
				} else {
					link="반출중";			
				}

			} else if(o_status.equals("o") && as_status.equals("12")){
				link ="입고완료";
			} else if((o_status.equals("l") || o_status.equals("o") )&& as_status.equals("13")) {
				link ="수리";
			} else if(o_status.equals("o") && as_status.equals("13")){
				link ="수리";
			} else if (o_status.equals("l") && as_status.equals("12")){
				link ="반납완료";
			}
		//}
		return link;
	}

}
