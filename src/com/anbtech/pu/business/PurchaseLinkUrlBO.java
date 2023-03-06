package com.anbtech.pu.business;

import com.anbtech.pu.entity.*;
import com.anbtech.pu.db.*;

import java.sql.*;
import java.util.*;

public class PurchaseLinkUrlBO{

	private Connection con;

	public PurchaseLinkUrlBO(Connection con){
		this.con = con;
	}
	
	/**********************************************
	 * 품목구매요청의뢰화면에 필요한 링크 및 버튼
	 **********************************************/
	public PurchaseLinkUrl getLinkForRequestItem(String mode,String searchword,String searchscope,String page,String no,String item_code,String process_stat) throws Exception{
  		PurchaseLinkUrl link = new PurchaseLinkUrl();

		String link_info_modify		= "";	// 정보수정 버튼
		String link_info_delete		= "";	// 정보삭제 버튼
		String link_item_add		= "";	// 품목등록 버튼
		String link_item_modify		= "";	// 품목수정 버튼
		String link_item_delete		= "";	// 품목삭제 버튼
		String link_list			= "";	// 리스트 보기 버튼
		String link_request_info	= "";	// 요청정보 보기 버튼
		String link_approval		= "";   // 결재상신 버튼
		String link_supplyer_assign	= "";   // 발주업체자동지정 버튼
		String input_hidden			= "";	// hidden 타입의 객체 문자열, 검색시에 사용하기 위함.		
		String link_app_info		= "";   // 결재정보 보기 화면
		String link_print			= "";   // 인쇄화면
		
		if(mode.equals("request_info")){		
			link_item_add = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_pu_item_add.gif' border='0' alt='구매품목입력' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='javascript:history.go(-1);'><img src='../pu/images/bt_cancel.gif' border='0' alt='취소' align='absmiddle'></a>";
		}

		if(mode.equals("modify_request_info")){
			if(process_stat.equals("S01") || process_stat.equals("")){
				link_info_modify = "&nbsp;<a href='javascript:modify_request();'><img src='../pu/images/bt_pu_item_add.gif' border='0' alt='저장후품목입력' align='absmiddle'></a>";
				link_info_delete = "&nbsp;<a href='javascript:delete_request();'><img src='../pu/images/bt_del_all.gif' border='0' alt='일괄삭제' align='absmiddle'></a>";
				link_approval	 = "&nbsp;<a href='javascript:request_app_view()'><img src='../pu/images/bt_app.gif' border='0' alt='결재상신' align='absmiddle'></a>";
			}else{
//				link_info_modify = "&nbsp;<a href='PurchaseMgrServlet?mode=request_item_add&request_no="+no+"'><img src='../pu/images/bt_pu_item_view.gif' border='0' alt='구매품목보기' align='absmiddle'></a>";
				link_app_info = "&nbsp;<a href='javascript:viewAppInfo()'><img src='../pu/images/bt_confirm_hist.gif' border='0' alt='승인내역보기' align='absmiddle'></a>";
				link_print = "&nbsp;<a href='javascript:go_print()'><img src='../pu/images/bt_print.gif' border='0' alt='인쇄' align='absmiddle'></a>";
			}
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=request_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";
		}

		if (mode.equals("request_item_add")){
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=request_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";
			if(process_stat.equals("S01") || process_stat.equals("")){
				link_approval	 = "&nbsp;<a href='javascript:request_app_view()'><img src='../pu/images/bt_app.gif' border='0' alt='결재상신' align='absmiddle'></a>";
				link_item_add = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_item_add.gif' border='0' alt='품목추가' align='absmiddle'></a>";
				link_supplyer_assign ="&nbsp;<a href=\"javascript:Display_tip(1, 'show', true);\"  onfocus=\"this.blur();\"><img src=\"../pu/images/bt_auto_assign.gif\" border='0' align='absmiddle' onMouseout=\"Display_tip(1, 'hide', false)\"  alt='발주업체자동지정'></a><div id='tip1' style=\"display:none;position:absolute;z-index:1;margin-top:21px;margin-left:-100px\"  onMouseout=\"Display_tip(1, 'hide', false)\" onMouseover=\"Display_tip(1, 'hide', true)\">				<table border='0' cellpadding='0' cellspacing='0' width='200' style='border:#999999 solid 1px' bgcolor='#f6f6f6'><tr height='5'><td></td></tr><tr><td>&nbsp;<a href=\"javascript:supplyer_assign('order_weight');\"><img src=\"../em/images/i_bullet_04.gif\" border='0' width='3' height='3' align='absmiddle' hspace='3' vspace='7'>발주배정가중치</a><br>&nbsp;<a href=\"javascript:supplyer_assign('unit_cost');\"><img src=\"../em/images/i_bullet_04.gif\" border='0' width='3' height='3' align='absmiddle' hspace='3' vspace='7' >공급단가</a></td></tr><tr height='5'><td></td></tr></table></div>";
			}
		}

		if(mode.equals("modify_request")){
			link_item_modify = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_modify.gif' border='0' alt='수정' align='absmiddle'></a>";
			link_item_delete = "&nbsp;<a href='javascript:del_item();'><img src='../pu/images/bt_del.gif' border='0' alt='삭제' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=request_item_add&request_no=" + no + "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_cancel.gif' border='0' alt='취소' align='absmiddle'></a>";
			link_item_add = "";
		}
		input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";

		link.setLinkInfoModify(link_info_modify);
		link.setLinkInfoDelete(link_info_delete);
		link.setLinkItemAdd(link_item_add);
		link.setLinkItemModify(link_item_modify);
		link.setLinkItemDelete(link_item_delete);
		link.setLinkList(link_list);
		link.setLinkRequestInfo(link_request_info);
		link.setInputHidden(input_hidden);
		link.setLinkApproval(link_approval);
		link.setLinkSupplyerAssign(link_supplyer_assign);
		link.setLinkAppInfo(link_app_info);
		link.setLinkPrint(link_print);

		return link;
	}

	/**********************************************
	 * 품목발주등록에 필요한 링크 및 버튼
	 **********************************************/
	public PurchaseLinkUrl getLinkForOrderItem(String mode,String searchword,String searchscope,String page,String no,String item_code,String process_stat) throws Exception{
  		PurchaseLinkUrl link = new PurchaseLinkUrl();

		String link_info_modify		= "";	// 정보수정 버튼
		String link_info_delete		= "";	// 정보삭제 버튼
		String link_item_add		= "";	// 품목등록 버튼
		String link_item_modify		= "";	// 품목수정 버튼
		String link_item_delete		= "";	// 품목삭제 버튼
		String link_list			= "";	// 리스트 보기 버튼
		String link_approval		= "";	// 결재상신 버튼
		String link_print			= "";	// 인쇄 버튼
		String input_hidden			= "";	// hidden 타입의 객체 문자열, 검색시에 사용하기 위함.		
		String link_order_info		= "";	// 발주정보보기 버튼
		String link_app_info		= "";	// 결재정보보기 화면
		
		if(mode.equals("order_info")){		
			link_item_add = "&nbsp;<a href='javascript:save_order();'><img src='../pu/images/bt_od_item_add.gif' border='0' alt='발주품목입력' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=order_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_cancel.gif' border='0' alt='취소' align='absmiddle'></a>";
			link_app_info = "&nbsp;<a href='javascript:viewAppInfo()'><img src='../pu/images/bt_confirm_hist.gif' border='0' alt='승인내역보기' align='absmiddle'></a>";
		}

		if(mode.equals("modify_order_info")){
			if(process_stat.equals("S05")){
				link_info_modify = "&nbsp;<a href='javascript:save_order();'><img src='../pu/images/bt_save_n_modify.gif' border='0' alt='저장후품목수정' align='absmiddle'></a>";
				link_info_delete = "&nbsp;<a href='javascript:delete_order();'><img src='../pu/images/bt_del.gif' border='0' alt='일괄삭제' align='absmiddle'></a>";
				link_approval	 = "&nbsp;<a href='javascript:order_app_view()'><img src='../pu/images/bt_app.gif' border='0' alt='결재상신' align='absmiddle'></a>";
			}else{
//				link_info_modify = "&nbsp;<a href='PurchaseMgrServlet?mode=order_item_add&order_no="+no+"'><img src='../pu/images/bt_od_item_view.gif' border='0' alt='발주품목보기' align='absmiddle'></a>";
				link_app_info = "&nbsp;<a href='javascript:viewAppInfo()'><img src='../pu/images/bt_confirm_hist.gif' border='0' alt='승인내역보기' align='absmiddle'></a>";
				link_print = "&nbsp;<a href='javascript:go_print();'><img src='../pu/images/bt_print_order.gif' border='0' alt='발주서출력' align='absmiddle'></a>";
			}
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=order_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";
		}

		if (mode.equals("order_item_add")){
			link_order_info = "&nbsp;<a href='javascript:order_info();'><img src='../pu/images/bt_balju_info_view.gif' border='0' alt='발주정보보기' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=order_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";

			if(process_stat.equals("S05")){
				link_item_add = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_item_add.gif' border='0' alt='품목추가' align='absmiddle'></a>";
				link_approval	 = "&nbsp;<a href='javascript:order_app_view()'><img src='../pu/images/bt_app.gif' border='0' alt='결재상신' align='absmiddle'></a>";
			}else{
				link_print = "&nbsp;<a href='javascript:go_print();'><img src='../pu/images/bt_print_order.gif' border='0' alt='발주서출력' align='absmiddle'></a>";
			}
		}
	
		if(mode.equals("modify_order")){
			link_item_modify = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_modify.gif' border='0' alt='수정' align='absmiddle'></a>";
			link_item_delete = "&nbsp;<a href='javascript:del_item();'><img src='../pu/images/bt_del.gif' border='0' alt='삭제' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=order_item_add&order_no=" + no + "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_calcel.gif' border='0' alt='취소' align='absmiddle'></a>";
			link_item_add = "";
		}

		input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";

		link.setLinkInfoModify(link_info_modify);
		link.setLinkInfoDelete(link_info_delete);
		link.setLinkItemAdd(link_item_add);
		link.setLinkItemModify(link_item_modify);
		link.setLinkItemDelete(link_item_delete);
		link.setLinkList(link_list);
		link.setLinkApproval(link_approval);
		link.setLinkPrint(link_print);
		link.setInputHidden(input_hidden);
		link.setLinkRequestInfo(link_order_info);
		link.setLinkAppInfo(link_app_info);

		return link;
	}

	/**********************************************
	 * 구매입고등록에 필요한 링크 및 버튼
	 **********************************************/
	public PurchaseLinkUrl getLinkForEnterItem(String mode,String searchword,String searchscope,String page,String no,String item_code,String process_stat) throws Exception{
  		PurchaseLinkUrl link = new PurchaseLinkUrl();

		String link_info_modify		= "";	// 정보수정 버튼
		String link_info_delete		= "";	// 정보삭제 버튼
		String link_item_add		= "";	// 품목등록 버튼
		String link_item_modify		= "";	// 품목수정 버튼
		String link_item_delete		= "";	// 품목삭제 버튼
		String link_list			= "";	// 리스트 보기 버튼
		String link_approval		= "";	// 결재상신 버튼
		String link_print			= "";	// 인쇄 버튼
		String input_hidden			= "";	// hidden 타입의 객체 문자열, 검색시에 사용하기 위함.		
		String link_enter_info		= "";	// 입고정보보기 버튼
		String link_app_info		= "";	// 결재정보보기 화면
		
		if(mode.equals("modify_enter_info")){
			if(process_stat.equals("S18")){
				link_info_modify = "&nbsp;<a href='javascript:save_enter();'><img src='../pu/images/bt_save_n_modify.gif' border='0' alt='저장후품목수정' align='absmiddle'></a>";
	
				link_info_delete = "&nbsp;<a href='javascript:delete_enter();'><img src='../pu/images/bt_del_all.gif' border='0' alt='일괄삭제' align='absmiddle'></a>";
			}else{
//				link_info_modify = "&nbsp;<a href='PurchaseMgrServlet?mode=enter_item_add&enter_no="+no+"'><img src='../pu/images/bt_view_in_item.gif' border='0' alt='입고품목보기' align='absmiddle'></a>";
				link_app_info = "&nbsp;<a href='javascript:viewAppInfo()'><img src='../pu/images/bt_confirm_hist.gif' border='0' alt='승인내역보기' align='absmiddle'></a>";
			}
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=enter_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";
		}

		if (mode.equals("enter_item_add")){
			link_enter_info = "&nbsp;<a href='javascript:enter_info();'><img src='../pu/images/bt_input_info.gif' border='0' alt='입고정보보기' align='absmiddle'></a>";
			link_approval	 = "&nbsp;<a href='javascript:go_approval();'><img src='../pu/images/bt_sangsin.gif' border='0' alt='결재상신'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=enter_search&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_list.gif' border='0' alt='목록보기' align='absmiddle'></a>";
			//link_item_add = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_.gif' border='0' alt='품목추가' align='absmiddle'></a>";
//			link_print = "&nbsp;<a href='javascript:go_print();'><img src='../pu/images/bt_.gif' border='0' alt='발주서출력' align='absmiddle'></a>";
		}
	
		if(mode.equals("modify_enter")){
			link_item_modify = "&nbsp;<a href='javascript:add_item();'><img src='../pu/images/bt_modify.gif' border='0' alt='수정' align='absmiddle'></a>";
			link_item_delete = "&nbsp;<a href='javascript:del_item();'><img src='../pu/images/bt_del.gif' border='0' alt='삭제' align='absmiddle'></a>";
			link_list = "&nbsp;<a href='PurchaseMgrServlet?mode=enter_item_add&enter_no=" + no + "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "'><img src='../pu/images/bt_cancel.gif' border='0' alt='취소' align='absmiddle'></a>";
			link_item_add = "";
		}

		input_hidden ="<input type='hidden' name='mode' value='"+mode+"'>";
		input_hidden += "<input type='hidden' name='page' value='"+page+"'>";
		input_hidden += "<input type='hidden' name='searchscope' value='"+searchscope+"'>";
		input_hidden += "<input type='hidden' name='searchword' value='"+searchword+"'>";

		link.setLinkInfoModify(link_info_modify);
		link.setLinkInfoDelete(link_info_delete);
		link.setLinkItemAdd(link_item_add);
		link.setLinkItemModify(link_item_modify);
		link.setLinkItemDelete(link_item_delete);
		link.setLinkList(link_list);
		link.setLinkApproval(link_approval);
		link.setLinkPrint(link_print);
		link.setInputHidden(input_hidden);
		link.setLinkRequestInfo(link_enter_info);
		link.setLinkAppInfo(link_app_info);

		return link;
	}

	/***************************************
	 * 리스트를 볼 때 필요한 링크 생성
	 ***************************************/
	public PurchaseLinkUrl getRedirect(String mode,String searchword,String searchscope,String page,String login_id) throws Exception{

		int l_maxlist = 15;
		int l_maxpage = 7;
		if(mode.equals("list_item_p")) l_maxlist = 10;
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		RequestInfoTable table		= new RequestInfoTable();

		String where		= "";
		String tablename	= "";
		if(mode.equals("order_search")) {
			where = purchaseBO.getWhereForOrderInfoList(mode,searchword,searchscope,login_id);
			tablename = "pu_order_info";
		}else if(mode.equals("enter_search"))	{
			where = purchaseBO.getWhereForEnterInfoList(mode,searchword,searchscope,login_id);
			tablename = "pu_entered_info";
		}else if(mode.equals("request_search"))	{
			where = purchaseBO.getWhereForRequestInfoList(mode,searchword,searchscope,login_id);
			tablename = "pu_requested_info";
		}

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
			pagecut = "[<a href=PurchaseMgrServlet?mode="+mode + "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">Prev</a>]";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "[<a href=PurchaseMgrServlet?mode="+mode+ "&page=" + curpage + "&searchword=" + searchword + "&searchscope=" + searchscope + ">" + curpage + "</a>]";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "[<a href=PurchaseMgrServlet?mode="+mode+ "&searchword=" + searchword + "&searchscope=" + searchscope + ">Next</a>]";
		}

		PurchaseLinkUrl link = new PurchaseLinkUrl();

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
}