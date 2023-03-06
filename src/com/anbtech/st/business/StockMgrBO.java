package com.anbtech.st.business;

import com.anbtech.st.entity.*;
import com.anbtech.st.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class StockMgrBO{

	private Connection con;
	
	public StockMgrBO(){}

	public StockMgrBO(Connection con){
		this.con = con;
	}

	/**************************
	 * 코드별 코드명 가져오기
	 **************************/
	public String getProcessStatName(String code) throws Exception {
		String stat_name = "";
		if(code.equals("S50"))	stat_name = "출고작성";
		else if(code.equals("S53"))	stat_name = "출고의뢰";
		else if(code.equals("S55"))	stat_name = "결재중";
		else if(code.equals("S56"))	stat_name = "출고반려";
		else if(code.equals("S57"))	stat_name = "출고승인";
		else if(code.equals("S59"))	stat_name = "출고완료";
		else if(code.equals("S60"))	stat_name = "승인반려";
		else stat_name="";
		
		return stat_name;
	}

	/*****************************************************************
	 * 수불현황 조회시의 검색구문을 만든다.
	 *****************************************************************/
	public String getWhereForInOutList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("inout_date")){
				//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(inout_date >= '" + s_date + "' and inout_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("inout_date")){
					//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (inout_date >= '" + s_date + "' and inout_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}
			where = " WHERE " + where_sea;
		}

		return where;
	}

	/*****************************************************************
	 * 수불현황 조회시의 검색구문을 만든다.
	 *****************************************************************/
	public String getWhereForItemStockList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("inout_date")){
				//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(inout_date >= '" + s_date + "' and inout_date <= '" + e_date +"')";

			}else if(search[0][0].equals("lead_time")){
				where_sea += "(lead_time >= " + search[0][1] +")";
			}else if(search[0][0].equals("unit_cost")){
				where_sea += "(unit_cost_a >= " + search[0][1] +")";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("inout_date")){
					//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (inout_date >= '" + s_date + "' and inout_date <= '" + e_date +"')";
				}else if(search[i][0].equals("lead_time")){
					where_sea += "AND (lead_time >= '" + search[i][1] +"')";
				}else if(search[i][0].equals("unit_cost")){
					where_sea += "AND (unit_cost_a >= '" + search[i][1] +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}
			where = " WHERE " + where_sea;

			if(mode.equals("list_stock_rop")) where += " AND (stock_quantity < resonable_quantity) AND (resonable_quantity != '0')";
		}else if(mode.equals("list_stock_rop")){
			where += " WHERE (stock_quantity < resonable_quantity) AND (resonable_quantity != '0')";
		}


		return where;
	}

	/***********************************************
	 * 기타입출고정보 조회시의 쿼리문 검색조건을 만든다.
	 ***********************************************/
	public String getWhereForEtcInOutInfoList(String mode,String in_or_out,String searchword,String searchscope,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("inout_no")){
				where_sea += "( inout_no LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("requestor_info")){
				where_sea += "( requestor_info LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("inout_date")){
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(inout_date >= '" + s_date + "' and inout_date <= '" + e_date +"')";
			}else if(searchscope.equals("process_stat")){
				where_sea += "( process_stat = '" +  searchword + "' )";
			}
	
			where = " WHERE " + where_sea + " AND in_or_out = '" + in_or_out + "'";
		}else{
			where = " WHERE in_or_out = '" + in_or_out + "'";
		}

		return where;
	}

	/*****************************************************************
	 * 생산출고의뢰품목 리스트 출력시의 검색조건을 만든다.
	 *****************************************************************/
	public String getWhereForReservedItemList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("request_date")){
				//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("request_date")){
					//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}

			where = " WHERE " + where_sea + " AND (process_stat IN ('S53','S55','S57','S59')) AND (request_quantity > delivery_quantity)";
		}else{
			where = " WHERE (process_stat IN ('S53','S55','S57','S59')) AND (request_quantity > delivery_quantity)";
		}

		return where;
	}


	/*****************************************************************
	 * 생산출고등록품목 리스트 출력시의 검색조건을 만든다.
	 *****************************************************************/
	public String getWhereForToEnterItemList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("request_date")){
				//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("request_date")){
					//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}

			where = " WHERE " + where_sea + " AND (process_stat = 'S57')";
		}else{
			where = " WHERE (process_stat = 'S57')";
		}

		return where;
	}

	/*****************************************************************
	 * 생산출고완료품목 리스트 출력시의 검색조건을 만든다.
	 *****************************************************************/
	public String getWhereForDeliveriedItemList(String mode,String where_str,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if (where_str.length() > 0){
			//where_str = "subject|제목,writer|작성자, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][2];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<2; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			if(search[0][0].equals("request_date")){
				//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("request_date")){
					//inout_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}

			where = " WHERE " + where_sea + " AND (process_stat = 'S59')";
		}else{
			where = " WHERE (process_stat = 'S59')";
		}

		return where;
	}

	/***********************************************
	 * 생산출고현황 검색시의 검색조건문 생성
	 ***********************************************/
	public String getWhereForDeliveriedInfoList(String mode,String searchword,String searchscope,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("ref_no")){
				where_sea += "( ref_no LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("requestor_info")){
				where_sea += "( requestor_info LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("delivery_date")){
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(delivery_date >= '" + s_date + "' and delivery_date <= '" + e_date +"')";
			}else if(searchscope.equals("process_stat")){
				where_sea += "( process_stat = '" +  searchword + "' )";
			}
	
			where = " WHERE " + where_sea;
		}else{

		}
		return where;
	}

	/************************************
	 * 선택된 입출고번호에 대한 정보를 가져온다.
	 ************************************/
	public EtcInOutInfoTable getEtcInOutInfoForm(String mode,String inout_no,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		EtcInOutInfoTable table = new EtcInOutInfoTable();
		
		//입고정보관련
		String mid					= "";
		String inout_date			= "";
		String total_mount			= "0";
		String monetary_unit		= "KRW";
		String inout_type			= "";
		String supplyer_code		= "";
		String supplyer_name		= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String filelink				= "";

		if( mode.equals("write_etc_inout_info") && inout_no == null){
			inout_no = "자동생성";
			//요청시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
			inout_date = vans.format(now);

			//등록자 정보
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requestor_div_name	= userinfo.getDivision();
			requestor_div_code	= userinfo.getAcCode();
			requestor_info		= userinfo.getUserName();
			requestor_id		= login_id;
		}
		else if(mode.equals("update_etc_inout_info") && inout_no != null){
			table = stDAO.getEtcInOutInfo(inout_no);

			mid					= table.getMid();
			inout_date			= table.getInOutDate();
			total_mount			= table.getTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			inout_type			= table.getInOutType();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
		}
		else if(mode.equals("view_etc_inout_info") && inout_no != null){
			table = stDAO.getEtcInOutInfo(inout_no);

			mid					= table.getMid();
			inout_date			= table.getInOutDate();
			total_mount			= table.getTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			inout_type			= table.getInOutType();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();


			table = new EtcInOutInfoTable();
			Iterator file_iter = stDAO.getEtcInOutFileList("st_etc_inout_info",inout_no).iterator();

			int j = 1;
			while(file_iter.hasNext()){
				EtcInOutInfoTable file = (EtcInOutInfoTable)file_iter.next();
				filelink += "<a href='StockMgrServlet?tablename=st_etc_inout_info&upload_folder=etc_inout&mode=download_etc_inout&inout_no="+inout_no+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0></a>";
				j++;				
			}
		}

		table.setMid(mid);
		table.setInOutNo(inout_no);
		table.setInOutDate(inout_date);
		table.setTotalMount(total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setInOutType(inout_type);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setFileLink(filelink);

		return table;
	}

	/************************************
	 * 선택된 출고의뢰번호에 해당하는 정보 가져오기
	 ************************************/
	public ReservedItemInfoTable getEtcDeliveryInfoForm(String mode,String delivery_no,String item_code,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		ReservedItemInfoTable table = new ReservedItemInfoTable();
		
		String mid					= "";

		
		String item_name			= "";
		String item_desc			= "";
		String item_type			= "";
		String request_quantity		= "";
		String request_unit			= "";
		String request_date			= "";
		String factory_code			= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";
		String ref_no				= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String delivery_quantity	= "";
		String delivery_unit		= "";
		String delivery_date		= "";
		String deliverer_div_code	= "";
		String deliverer_div_name	= "";
		String deliverer_id			= "";
		String deliverer_info		= "";
		String process_stat			= "";

		if(mode.equals("write_etc_delivery") && delivery_no == null){
			delivery_no = "자동생성";
			//요청시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
			request_date = vans.format(now);

			//요청자 정보
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requestor_div_name	= userinfo.getDivision();
			requestor_div_code	= userinfo.getAcCode();
			requestor_info		= userinfo.getUserName();
			requestor_id		= login_id;

			item_code			= "";
		}

		if(mode.equals("add_etc_delivery") && delivery_no != null){
			//요청시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
			request_date = vans.format(now);

			//요청자 정보
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requestor_div_name	= userinfo.getDivision();
			requestor_div_code	= userinfo.getAcCode();
			requestor_info		= userinfo.getUserName();
			requestor_id		= login_id;

			item_code			= "";
		}

		if(mode.equals("modify_etc_delivery") && delivery_no != null && item_code != null){
			table = stDAO.getReservedItemInfo(delivery_no,item_code);

			mid					= table.getMid();
			delivery_no			= table.getDeliveryNo();
			item_code			= table.getItemCode();
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			item_type			= table.getItemType();
			request_quantity	= table.getRequestQuantity();
			request_unit		= table.getRequestUnit();
			request_date		= table.getRequestDate();
			factory_code		= table.getFactoryCode();
			factory_name		= table.getFactoryName();
			warehouse_code		= table.getWarehouseCode();
			warehouse_name		= table.getWarehouseName();
			ref_no				= table.getRefNo();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			delivery_quantity	= table.getDeliveryQuantity();
			delivery_unit		= table.getDeliveryUnit();
			delivery_date		= table.getDeliveryDate();
			deliverer_div_code	= table.getDelivererDivCode();
			deliverer_div_name	= table.getDelivererDivName();
			deliverer_id		= table.getDelivererId();
			deliverer_info		= table.getDelivererInfo();
			process_stat		= table.getProcessStat();
		}

		table.setMid(mid);
		table.setDeliveryNo(delivery_no);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setItemType(item_type);
		table.setRequestQuantity(request_quantity);
		table.setRequestUnit(request_unit);
		table.setRequestDate(request_date);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setRefNo(ref_no);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setDeliveryQuantity(delivery_quantity);
		table.setDeliveryUnit(delivery_unit);
		table.setDeliveryDate(delivery_date);
		table.setDelivererDivCode(deliverer_div_code);
		table.setDelivererDivName(deliverer_div_name);
		table.setDelivererId(deliverer_id);
		table.setDelivererInfo(deliverer_info);
		table.setProcessStat(process_stat);

		return table;
	}

	/******************************************
	 * 첨부파일 업로딩 처리
	 ******************************************/
	public EtcInOutInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String fileumask = "";
		String did = "";

		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+i+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize + fsize + "|";
				fileumask = fileumask + (no+"_"+i)+"|";
				did = did + i + "|";
			}
			i++;
		}
		EtcInOutInfoTable file = new EtcInOutInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFpath(filepath);
		file.setFumask(fileumask);
		//file.setDid(did);

		return file;
	} //getFile_frommulti()

	/******************************************
	 * 첨부파일 수정 처리
	 ******************************************/
	public EtcInOutInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String fileumask = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			EtcInOutInfoTable file = new EtcInOutInfoTable();
			if(file_iter.hasNext()) file = (EtcInOutInfoTable)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
				fileumask = fileumask + (no+"_"+i)+"|";
				did = did + "0" + "|";
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFname() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFname() + "|";
				filetype = filetype + file.getFtype() + "|";
				filesize = filesize +file.getFsize() + "|";
				fileumask = fileumask + (no+"_"+i)+"|";

				did = did + file.getDid() + "|";
				j++;
			}
			i++;
		}
		EtcInOutInfoTable file = new EtcInOutInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);
		file.setDid(did);

		return file;
	}


	/**************************************************
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String tablename, String mid, String filename, String filetype, String filesize, String fumask) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"', fsize='"+filesize+"', fumask='"+fumask+"'";
		String where = " WHERE inout_no='"+mid+"'";

		StockMgrDAO stDAO = new StockMgrDAO(con);
		stDAO.updTable(tablename, set, where);
	}


	/**************************************************
	 * 첨부파일 다운로드
	 **************************************************/
	public EtcInOutInfoTable getFileForDown(String tablename,String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		StockMgrDAO stDAO = new StockMgrDAO(con);
		Iterator file_iter = stDAO.getEtcInOutFileList(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",fileumask="";
		int i = 1;
		while (file_iter.hasNext()){
			EtcInOutInfoTable file = (EtcInOutInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFname();
				filetype = file.getFtype();
				filesize = file.getFsize();
				fileumask = file.getFumask();
			}
			i++;
		}
		EtcInOutInfoTable file = new EtcInOutInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);


		return file;
	}

	/************************************
	 * 선택된 번호 및 품목코드에 대한 정보를 가져온다.
	 ************************************/
	public EtcInOutInfoTable getEtcInOutItemForm(String mode,String inout_no,String item_code,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		EtcInOutInfoTable table = new EtcInOutInfoTable();
		
		//
		String mid					= "";
		String process_stat			= "";
		String inout_date			= "";
		String total_mount			= "0";
		String monetary_unit		= "";
		String inout_type			= "";
		String supplyer_code		= "";
		String supplyer_name		= "";

		//
		String item_name			= "";
		String item_desc			= "";
		String quantity				= "0";
		String item_unit			= "EA";
		String inout_cost			= "0";
		String unit_cost			= "0";
		String factory_code			= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";
		String unit_type			= "Q";

		item_code					= "";

		//수정 모드일 경우
		//기타입출고품목의 수정은 허용하지 않는다.
		if(mode.equals("update_etc_inout_item") && inout_no != null && item_code != null){

		}

		table.setInOutNo(inout_no);
		table.setInOutDate(inout_date);
		table.setTotalMount(total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setInOutType(inout_type);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);

		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setQuantity(quantity);
		table.setItemUnit(item_unit);
		table.setInOutCost(inout_cost);
		table.setUnitCost(unit_cost);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setUnitType(unit_type);

		return table;
	}


	/***********************************************
	 * ROP에 의한 구매요청의뢰 품목을 저장한다.
     * items == "품목번호1|요청수량,품목번호2|요청번호,...." 식임.
	 ***********************************************/
	public void saveRequestItem(String mode,String items,String request_no) throws Exception{
		com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
		com.anbtech.cm.entity.PartInfoTable part = new com.anbtech.cm.entity.PartInfoTable();

		//품목을 분리하여 배열에 담는다.
		StringTokenizer str = new StringTokenizer(items, ",");
		int item_count = str.countTokens();
		String item_a[] = new String[item_count];
		String[][] item = new String[item_count][2];

		for(int i=0; i<item_count; i++){ 
			item_a[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
			for(int j=0; j<2; j++){ 
				item[i][j] = str2.nextToken();
			}
		}

		//분리된 품목을 db에 담는다.
		for(int i = 0;i< item_count; i++){
			String item_code	= item[i][0]; //품목코드
			String quantity		= item[i][1]; //요청수량

			part = cmDAO.getItemInfo(item_code);
			
			//pu_requested_item 테이블에 품목정보 기록
			//saveRequestItemInfo(String request_no,String item_code,String item_name,String item_desc,String request_unit,String delivery_date,String request_quantity,String order_quantity,String delivery_quantity,String process_stat,String supplyer_code,String supplyer_name,String supply_cost,String request_cost)
			purchaseDAO.saveRequestItemInfo(request_no,item_code,part.getItemName(),part.getItemDesc(),part.getStockUnit(),"",quantity,"0","0","S01","","","0","0");
		}

	}

	/**************************************************
	 * 구매입고 처리시에 수불현황을 기록하고 재고정보 업데이트

	 * 1.구매입고품목에 대한 원가를 계산
	 * 2.구매입고품목에 대한 수불정보를 기록
	 * 3.구매입고품목에 대한 재고정보 중 입고예정수량을 업데이트
	 **************************************************/
	public void updateInOutInfoForPurchasedInputItem(String mode,String item_code,String enter_unit_cost,String enter_quantity,String enter_no,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		com.anbtech.cm.entity.PartInfoTable item	= new com.anbtech.cm.entity.PartInfoTable();
		com.anbtech.cm.db.CodeMgrDAO cmDAO			= new com.anbtech.cm.db.CodeMgrDAO(con);
		com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO= new com.anbtech.pu.db.PurchaseMgrDAO(con);
		StockMgrDAO stDAO							= new StockMgrDAO(con);
		StockInfoTable stock						= new StockInfoTable();
		InOutInfoTable inout						= new InOutInfoTable();

		//해당 품목에 대한 수불정보중 가장 최근의 수불정보를 가져온다.
		inout = stDAO.getRecentInOutInfo(mode,item_code,enter_no);

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String inout_date = vans.format(now);
		String total_cost = Double.toString(Double.parseDouble(enter_unit_cost) * Double.parseDouble(enter_quantity));

		//폼목정보 가져오기(품목명,품목설명,재고단위)
		item = cmDAO.getItemInfo(item_code);

		double stock_unit_cost = 0;
		//원가를 계산하고 수불정보를 기록한다.
		if(inout.getInOutDate().equals("")){	//해당품목에 대한 수불정보가 없을 경우
			if(mode.equals("ADD")){
				stDAO.addInOutInfo("PI",inout_date,enter_no,item_code,item.getItemName(),item.getItemDesc(),item.getItemType(),enter_quantity,item.getStockUnit(),enter_unit_cost,total_cost,factory_code,factory_name,warehouse_code,warehouse_name,enter_unit_cost,enter_quantity);			
			}else if(mode.equals("MOD")){
				stDAO.updateInOutInfo("PI",inout_date,enter_no,item_code,item.getItemName(),item.getItemDesc(),item.getItemType(),enter_quantity,item.getStockUnit(),enter_unit_cost,total_cost,factory_code,factory_name,warehouse_code,warehouse_name,enter_unit_cost,enter_quantity,"S21");
			}
			
			stock_unit_cost = Double.parseDouble(enter_unit_cost);
		}else{	//해당품목에 대한 수불정보가 있을 경우
			stock_unit_cost	= Double.parseDouble(inout.getStockUnitCost());			//원가
			double stock_quantity	= Double.parseDouble(inout.getStockQuantity());	//현 재고수량

			//이동평균단가 계산
			stock_unit_cost = Math.round((stock_unit_cost * stock_quantity + Double.parseDouble(enter_unit_cost) * Double.parseDouble(enter_quantity)) / (stock_quantity + Double.parseDouble(enter_quantity)));

			//재고수량계산
			stock_quantity = Math.round(stock_quantity + Double.parseDouble(enter_quantity));
		
			if(mode.equals("ADD")){				
				stDAO.addInOutInfo("PI",inout_date,enter_no,item_code,item.getItemName(),item.getItemDesc(),item.getItemType(),enter_quantity,item.getStockUnit(),enter_unit_cost,total_cost,factory_code,factory_name,warehouse_code,warehouse_name,Double.toString(stock_unit_cost),Double.toString(stock_quantity));
			}else if(mode.equals("MOD")){
				stDAO.updateInOutInfo("PI",inout_date,enter_no,item_code,item.getItemName(),item.getItemDesc(),item.getItemType(),enter_quantity,item.getStockUnit(),enter_unit_cost,total_cost,factory_code,factory_name,warehouse_code,warehouse_name,Double.toString(stock_unit_cost),Double.toString(stock_quantity),"S21");			
			}
		}

		//해당품목에 대한 공장별 입고예정수량 파악
		String into_quantity = purchaseDAO.getInToQuantityByFactory(factory_code,item_code);

		//재고정보의 입고예정수량 업데이트
		updateIntoQuantity(item_code,into_quantity,factory_code,factory_name,warehouse_code,warehouse_name);

		//원가정보 업데이트
		updateItemUnitCost(item_code,item.getItemName(),item.getItemDesc(),item.getItemType(),"0",Double.toString(stock_unit_cost),"0");

	}

	/**************************************************
	 * 선택된 입고번호와 품목코드에 관한 수불기록을 삭제하고,
	 * 해당품목에 대한 재고정보 중 입고예정수량을 업데이트
	 **************************************************/
	public void deleteInOutInfoForPurchasedInputItem(String enter_no,String request_no,String item_code) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		com.anbtech.pu.entity.RequestInfoTable table = new com.anbtech.pu.entity.RequestInfoTable();
		com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
		table = purchaseDAO.getRequestInfo(request_no);
		InOutInfoTable inout = new InOutInfoTable();

		//삭제대상 수불정보 바로 이전의 수불정보를 가져온다.
		inout = stDAO.getRecentInOutInfo("DEL",item_code,enter_no);

		//수불정보를 삭제한다.
		stDAO.deleteInOutInfo(enter_no,item_code);

		//해당품목에 대한 공장별 입고예정수량 파악
		String into_quantity = purchaseDAO.getInToQuantityByFactory(table.getFactoryCode(),item_code);

		//재고정보의 입고예정수량 업데이트
		updateIntoQuantity(item_code,into_quantity,table.getFactoryCode(),table.getFactoryName(),"","");

		//품목원가정보를 이전으로 되돌린다.
		updateItemUnitCost(item_code,inout.getItemName(),inout.getItemDesc(),inout.getItemType(),"0",inout.getUnitCost(),"0");
	}

	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 입고예정수량(into_quantity)을 업데이트한다.
	 * 구매입고품목정보 수정시에 호출되는 메서드로 기존 입고예정량을 고려하지 않고
	 * 지정된 입고예정수량으로 업데이트한다.
	 **************************************************/
	public void updateIntoQuantity(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		if(stock.getLastUpdatedDate().equals("")){	//재고정보가 없을 경우 새로 추가한다.
			com.anbtech.cm.entity.PartInfoTable item = new com.anbtech.cm.entity.PartInfoTable();
			com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

			//폼목정보 가져오기(품목명,품목설명,재고단위)
			item = cmDAO.getItemInfo(item_code);

			//디비저장
			//addItemStockInfo(String item_code,String item_name,String item_desc,String stock_unit,String stock_quantity,String into_quantity,String good_quantity,String bad_quantity,String outto_quantity,String unit_type,String unit_cost,String last_updated_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name)
			stDAO.addItemStockInfo(item_code,item.getItemName(),item.getItemDesc(),item.getStockUnit(),"0",quantity,"0","0","0","0","2","",last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name);
					
		}else{	//재고정보가 있을 경우 정보를 업데이트한다.(입고예정수량)
			stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),stock.getStockQuantity(),quantity,stock.getGoodQuantity(),stock.getBadQuantity(),stock.getOuttoQuantity(),stock.getDeliveryQuantity(),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());
		}

	}
	
	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 입고예정수량(into_quantity)을 업데이트한다.
	 * 품질관리가 완료되는 시점에 호출되는 메서드로 기존 입고예정수량을 반영하여
	 * 수량을 업데이트시킨다. 입고등록시 재고정보를 쓰여졌을 것이므로 재고정보가
	 * 무조건 있다고 보고, 없는 경우의 처리는 하지 않음.
	 **************************************************/
	public void updateIntoQuantityWhenInspectionFinished(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{

		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		double into_quantity = Double.parseDouble(stock.getIntoQuantity()) + Double.parseDouble(quantity);
		stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),stock.getStockQuantity(),Double.toString(into_quantity),stock.getGoodQuantity(),stock.getBadQuantity(),stock.getOuttoQuantity(),stock.getDeliveryQuantity(),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());

	}

	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 현재고수량(stock_quantity)을 업데이트한다.
	 **************************************************/
	public void updateStockQuantity(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		if(stock.getLastUpdatedDate().equals("")){	//재고정보가 없을 경우 새로 추가한다.
			com.anbtech.cm.entity.PartInfoTable item = new com.anbtech.cm.entity.PartInfoTable();
			com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

			//폼목정보 가져오기(품목명,품목설명,재고단위)
			item = cmDAO.getItemInfo(item_code);
			stDAO.addItemStockInfo(item_code,item.getItemName(),item.getItemDesc(),item.getStockUnit(),quantity,"0","0","0","0","0","2","",last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name);
					
		}else{	//재고정보가 있을 경우 정보를 업데이트한다.(현재고수량)
			double stock_quantity = Double.parseDouble(stock.getStockQuantity()) + Double.parseDouble(quantity);
			stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),Double.toString(stock_quantity),stock.getIntoQuantity(),stock.getGoodQuantity(),stock.getBadQuantity(),stock.getOuttoQuantity(),stock.getDeliveryQuantity(),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());
		}

	}

	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 출고수량(delivery_quantity)을 업데이트한다.
	 **************************************************/
	public void updateDeliveryQuantity(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		if(stock.getLastUpdatedDate().equals("")){	//재고정보가 없을 경우 새로 추가한다.
			com.anbtech.cm.entity.PartInfoTable item = new com.anbtech.cm.entity.PartInfoTable();
			com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

			//폼목정보 가져오기(품목명,품목설명,재고단위)
			item = cmDAO.getItemInfo(item_code);
			stDAO.addItemStockInfo(item_code,item.getItemName(),item.getItemDesc(),item.getStockUnit(),"0","0","0","0","0",quantity,"2","",last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name);
					
		}else{	//재고정보가 있을 경우 정보를 업데이트한다.
			double delivery_quantity = Double.parseDouble(stock.getDeliveryQuantity()) + Double.parseDouble(quantity);
			stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),stock.getStockQuantity(),stock.getIntoQuantity(),stock.getGoodQuantity(),stock.getBadQuantity(),stock.getOuttoQuantity(),Double.toString(delivery_quantity),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());
		}

	}

	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 출고예정수량(outto_quantity)을 업데이트한다.
	 **************************************************/
	public void updateOuttoQuantity(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		if(stock.getLastUpdatedDate().equals("")){	//재고정보가 없을 경우 새로 추가한다.
			com.anbtech.cm.entity.PartInfoTable item = new com.anbtech.cm.entity.PartInfoTable();
			com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

			//폼목정보 가져오기(품목명,품목설명,재고단위)
			item = cmDAO.getItemInfo(item_code);
			stDAO.addItemStockInfo(item_code,item.getItemName(),item.getItemDesc(),item.getStockUnit(),"0","0","0","0",quantity,"0","2","",last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name);
					
		}else{	//재고정보가 있을 경우 정보를 업데이트한다.
			double outto_quantity = Double.parseDouble(stock.getOuttoQuantity()) + Double.parseDouble(quantity);
			stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),stock.getStockQuantity(),stock.getIntoQuantity(),stock.getGoodQuantity(),stock.getBadQuantity(),Double.toString(outto_quantity),stock.getDeliveryQuantity(),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());
		}

	}

	/**************************************************
	 * 선택된 품목에 대한 재고정보 중 불량품수량(bad_quantity)을 업데이트한다.
	 **************************************************/
	public void updateBadQuantity(String item_code,String quantity,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 재고정보를 가져온다.		
		stock = stDAO.getItemStockInfo(factory_code,item_code);

		if(stock.getLastUpdatedDate().equals("")){	//재고정보가 없을 경우 새로 추가한다.
			com.anbtech.cm.entity.PartInfoTable item = new com.anbtech.cm.entity.PartInfoTable();
			com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

			//폼목정보 가져오기(품목명,품목설명,재고단위)
			item = cmDAO.getItemInfo(item_code);
			stDAO.addItemStockInfo(item_code,item.getItemName(),item.getItemDesc(),item.getStockUnit(),"0","0","0",quantity,"0","0","2","",last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name);
					
		}else{	//재고정보가 있을 경우 정보를 업데이트한다.
			double bad_quantity = Double.parseDouble(stock.getBadQuantity()) + Double.parseDouble(quantity);
			stDAO.updateItemStockInfo(stock.getItemCode(),stock.getItemName(),stock.getItemDesc(),stock.getStockUnit(),stock.getStockQuantity(),stock.getIntoQuantity(),stock.getGoodQuantity(),Double.toString(bad_quantity),stock.getOuttoQuantity(),stock.getDeliveryQuantity(),stock.getUnitType(),stock.getUnitCost(),last_updated_date,stock.getFactoryCode(),stock.getFactoryName(),stock.getWarehouseCode(),stock.getWarehouseName());
		}

	}

	/**************************************************
	 * 선택된 품목에 원가정보를 수정한다.
	 **************************************************/
	public void updateItemUnitCost(String item_code,String item_name,String item_desc,String item_type,String unit_cost_s,String unit_cost_a,String unit_cost_c) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable stock = new StockInfoTable();

		//기록일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String last_updated_date = vans.format(now);

		//선택된 품목에 대한 원가정보를 가져온다.	
		stock = stDAO.getItemUnitCostInfo(item_code);

		if(stock.getLastUpdatedDate().equals("")){	//원가정보가 없을 경우 새로 추가한다.
			stDAO.addItemUnitCostInfo(item_type,item_code,item_name,item_desc,unit_cost_s,unit_cost_a,unit_cost_c,last_updated_date);
					
		}else{	//원가정보가 있을 경우 원가를 수정한다.
			stDAO.updateItemUnitCostInfo(item_code,"unit_cost_a",unit_cost_a,last_updated_date);
		}

	}

	/************************************
	 * 선택된 번호 및 품목코드에 대한 정보를 가져온다.
	 ************************************/
	public StShiftInfoTable getShiftInfoForm(String mode,String login_id,String mid) throws Exception{
		StockMgrDAO stockDAO = new StockMgrDAO(con);
		StShiftInfoTable table = new StShiftInfoTable();

		//String mid				="";	// 관리번호
		String shift_no				="자동생성";	// 이동번호
		String shift_type			="";	// 이동유형
		String sr_factory_code		="";	// source 공장코드
		String sr_factory_name		="";	// source 공장명
		String sr_warehouse_code	="";	// source 창고코드
		String sr_warehouse_name	="";	// source 창고명
		String sr_item_code			="";	// source 품목코드
		String sr_item_name			="";	// source 품목명
		String sr_item_type			="";	// source 품목유형
		String sr_item_desc			="";	// source 품목설명
		String dt_factory_code		="";	// target 공장코드
		String dt_factory_name		="";	// target 공장명
		String dt_warehouse_code	="";	// target 창고코드
		String dt_warehouse_name	="";	// target 창고명
		String dt_item_code			="";	// target 품목코드
		String dt_item_name			="";	// target 품목명
		String dt_item_type			="";	// target 품목유형
		String dt_item_desc			="";	// target 품목설명
		String stock_unit			="";	// 재고 단위
		String quantity				="";	// 재고 수량
		String requestor_id			="";	// 등록자 ID
		String requestor_info		="";	// 등록자 NAME
		String requestor_div_code	="";	// 등록자 부서코드
		String requestor_div_name	="";	// 등록자 부서명
		String reg_date				="";	// 이동일자

		if (mode.equals("write_item_shift_info") || mode.equals("write_item_ishift_info") )
		{
			//요청시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
			reg_date = vans.format(now);

			//등록자 정보t
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requestor_div_name	= userinfo.getDivision();
			requestor_div_code	= userinfo.getAcCode();
			requestor_info		= userinfo.getUserName();
			requestor_id		= login_id;
		}
		

		// 이동 재고 정보보기( 공장간&품목간)
		if (mode.equals("view_item_shift_info")||mode.equals("view_item_ishift_info")){
			table = stockDAO.getItemShiftInfo(mid);

			mid					= table.getMid();	
			shift_no			= table.getShiftNo();	
			shift_type			= table.getShiftType();	
			sr_factory_code		= table.getSrFactoryCode();
			sr_factory_name		= table.getSrFactoryName();	
			sr_warehouse_code	= table.getSrWarehouseCode();	
			sr_warehouse_name	= table.getSrWarehouseName();	
			sr_item_code		= table.getSrItemCode();
			sr_item_name		= table.getSrItemName();
			sr_item_type		= table.getSrItemType();	
			sr_item_desc		= table.getSrItemDesc();	
			dt_factory_code		= table.getDtFactoryCode();	
			dt_factory_name		= table.getDtFactoryName();	
			dt_warehouse_code	= table.getDtWarehouseCode();	
			dt_warehouse_name	= table.getDtWarehouseName();	
			dt_item_code		= table.getDtItemCode();	
			dt_item_name		= table.getDtItemName();	
			dt_item_type		= table.getDtItemType();
			dt_item_desc		= table.getDtItemDesc();	
			stock_unit			= table.getStockUnit();	
			quantity			= table.getQuantity();	
			requestor_id		= table.getRequestorId();	
			requestor_info		= table.getRequestorInfo();
			requestor_div_code	= table.getRequestorDivCode();	
			requestor_div_name	= table.getRequestorDivName();	
			reg_date			= table.getRegDate();	
		}

		table.setMid(mid);
		table.setShiftNo(shift_no);
		table.setShiftType(shift_type);
		table.setSrFactoryCode(sr_factory_code);
		table.setSrFactoryName(sr_factory_name);
		table.setSrWarehouseCode(sr_warehouse_code);
		table.setSrWarehouseName(sr_warehouse_name);
		table.setSrItemCode(sr_item_code);
		table.setSrItemName(sr_item_name);
		table.setSrItemType(sr_item_type);
		table.setSrItemDesc(sr_item_desc);
		table.setDtFactoryCode(dt_factory_code);
		table.setDtFactoryName(dt_factory_name);
		table.setDtWarehouseCode(dt_warehouse_code);
		table.setDtWarehouseName(dt_warehouse_name);
		table.setDtItemCode(dt_item_code);
		table.setDtItemName(dt_item_name);
		table.setDtItemType(dt_item_type);
		table.setDtItemDesc(dt_item_desc);
		table.setStockUnit(stock_unit);
		table.setQuantity(quantity);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setRegDate(reg_date);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);

		return table;
	}

	/*************************************
	* 재고이동현황 검색시의 검색구문 생성
	**************************************/
	public String getWhereForShiftInfoList(String mode,String searchword,String searchscope){
		
		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("sr_item_code")){				//품목번호
				where_sea += "(sr_item_code LIKE '%" +  searchword + "%')";
			}else if(searchscope.equals("shift_no")){			//이동번호
				where_sea += "(shift_no LIKE '%" +  searchword + "%')";
			}else if(searchscope.equals("reg_date")){			//이동일자
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(reg_date >= '" + s_date + "' and reg_date <= '" + e_date +"')";
			}else if(searchscope.equals("shift_type")){			//이동유형
				where_sea += "(shift_type = '" +  searchword + "')";
			}
			
			where = " WHERE " + where_sea;
		}

		//System.out.println(where);
		return where;
	}


	/************************************
	 * 선택된 자재예약 정보를 가져온다.
	 ************************************/
	public ReservedItemInfoTable getReservedItemInfoForm(String mode,String delivery_no,String item_code,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		ReservedItemInfoTable table = new ReservedItemInfoTable();

		String mid					= "";
		String item_name			= "";
		String item_desc			= "";
		String item_type			= "";
		String request_quantity		= "";
		String request_unit			= "";
		String request_date			= "";
		String factory_code			= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";
		String ref_no				= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String delivery_quantity	= "";
		String delivery_unit		= "";
		String delivery_date		= "";
		String deliverer_div_code	= "";
		String deliverer_div_name	= "";
		String deliverer_id			= "";
		String deliverer_info		= "";
		String process_stat			= "";
		

		if( mode.equals("modify_reserved_item") && delivery_no != null && item_code != null){
			table = stDAO.getReservedItemInfo(delivery_no,item_code);

			mid					= table.getMid();
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			item_type			= table.getItemType();
			request_quantity	= table.getRequestQuantity();
			request_unit		= table.getRequestUnit();
			request_date		= table.getRequestDate();
			factory_code		= table.getFactoryCode();
			factory_name		= table.getFactoryName();
			warehouse_code		= table.getWarehouseCode();
			warehouse_name		= table.getWarehouseName();
			ref_no				= table.getRefNo();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			delivery_quantity	= table.getDeliveryQuantity();
			delivery_unit		= table.getDeliveryUnit();
			delivery_date		= table.getDeliveryDate();
			deliverer_div_code	= table.getDelivererDivCode();
			deliverer_div_name	= table.getDelivererDivName();
			deliverer_id		= table.getDelivererId();
			deliverer_info		= table.getDelivererInfo();
			process_stat		= table.getProcessStat();
		}

		table.setMid(mid);
		table.setDeliveryNo(delivery_no);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setItemType(item_type);
		table.setRequestQuantity(request_quantity);
		table.setRequestUnit(request_unit);
		table.setRequestDate(request_date);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setRefNo(ref_no);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setDeliveryQuantity(delivery_quantity);
		table.setDeliveryUnit(delivery_unit);
		table.setDeliveryDate(delivery_date);
		table.setDelivererDivCode(deliverer_div_code);
		table.setDelivererDivName(deliverer_div_name);
		table.setDelivererId(deliverer_id);
		table.setDelivererInfo(deliverer_info);
		table.setProcessStat(process_stat);

		return table;
	}

	/************************************
	 * 선택된 출고등록품목을 일괄 출고처리한다.
	 * items = 출고의뢰번호|품목코드|전자결재번호,
	 ************************************/
	public void saveDeliveryItem(String mode,String items,String delivery_date,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		StockInfoTable table = new StockInfoTable();
		ReservedItemInfoTable table2 = new ReservedItemInfoTable();

		//품목을 분리하여 배열에 담는다.
		StringTokenizer str = new StringTokenizer(items, ",");
		int item_count = str.countTokens();
		String item_a[] = new String[item_count];
		String[][] item = new String[item_count][3];

		for(int i=0; i<item_count; i++){ 
			item_a[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
			for(int j=0; j<3; j++){ 
				item[i][j] = str2.nextToken();
			}
		}

		//분리된 품목을 db에 담는다.
		for(int i = 0;i< item_count; i++){
			String delivery_no			= item[i][0]; //출고의뢰번호
			String item_code			= item[i][1]; //품목코드
			String aid					= item[i][2]; //전자결재관리번호

			//선택된 출고등록품목 정보 가져오기
			table2 = stDAO.getDeliveriedItemInfo(delivery_no,item_code,aid);
			String delivery_quantity	= table2.getDeliveryQuantity(); //출고대상수량

			int distance = - Integer.parseInt(delivery_quantity);

			//생산출고의뢰품목의 기출고수량 및 상태코드 업데이트(테이블:st_reserved_item_info)
			stDAO.updateProcessStat("st_reserved_item_info","delivery_no",delivery_no,item_code,"S59");
			stDAO.updateQuantity("st_reserved_item_info","delivery_no",delivery_no,item_code,"delivery_quantity",delivery_quantity);

			//생산출고등록품목의 상태코드 및 출고일자 등 업데이트(테이블:st_deliveried_item_info)
			stDAO.updateDeliveriedItemInfo(mode,delivery_no,item_code,delivery_quantity,table2.getRequestUnit(),login_id,delivery_date,aid);

			//해당품목의 원가정보를 가져온다.
			table = stDAO.getItemUnitCostInfo(item_code);
			String inout_cost = Double.toString(Double.parseDouble(table.getUnitCostA()) * Double.parseDouble(delivery_quantity));

			//수불현황 기록
			stDAO.addInOutInfo("MO",delivery_date,delivery_no,item_code,table2.getItemName(),table2.getItemDesc(),table2.getItemType(),Integer.toString(distance),table2.getRequestUnit(),table.getUnitCostA(),inout_cost,table2.getFactoryCode(),table2.getFactoryName(),table2.getWarehouseCode(),table2.getWarehouseName(),"0","0");

			//재고정보중 출고예정수량(outto_quantity) 업데이트
			updateOuttoQuantity(item_code,Integer.toString(distance),table2.getFactoryCode(),table2.getFactoryName(),table2.getWarehouseCode(),table2.getWarehouseName());

			//재고정보중 출고수량(delivery_quantity) 업데이트
			updateDeliveryQuantity(item_code,Integer.toString(-distance),table2.getFactoryCode(),table2.getFactoryName(),table2.getWarehouseCode(),table2.getWarehouseName());

			//생상관리 모듈의 테이블 업데이트
			stDAO.updateMfgMoudle(delivery_no,table2.getRefNo(),item_code,table2.getFactoryCode(),Integer.toString(-distance));

		}
	}


	/************************************
	 * 선택된 출고의뢰품목의 전자결재 상신을 위해 출고등록품목을 기록한다.
	 * items = 출고의뢰번호|품목코드|출고의뢰수량,
	 ************************************/
	public void saveToDeliveryItem(String mode,String items,String aid,String login_id) throws Exception{
		StockMgrDAO stDAO = new StockMgrDAO(con);
		ReservedItemInfoTable table = new ReservedItemInfoTable();

		//품목을 분리하여 배열에 담는다.
		StringTokenizer str = new StringTokenizer(items, ",");
		int item_count = str.countTokens();
		String item_a[] = new String[item_count];
		String[][] item = new String[item_count][3];

		for(int i=0; i<item_count; i++){ 
			item_a[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
			for(int j=0; j<3; j++){ 
				item[i][j] = str2.nextToken();
			}
		}

		//출고담당자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String deliverer_div_name	= userinfo.getDivision();
		String deliverer_div_code	= userinfo.getAcCode();
		String deliverer_info		= userinfo.getUserName();

		//분리된 품목을 db에 담는다.
		for(int i = 0;i< item_count; i++){
			String delivery_no		= item[i][0]; //출고의뢰번호
			String item_code		= item[i][1]; //품목코드
			String request_quantity	= item[i][2]; //출고의뢰수량

			//선택된 품목의 출고의뢰정보 가져오기
			table = stDAO.getReservedItemInfo(delivery_no,item_code);

			//출고수량 계산(= 출고요청수량 - 기출고수량)
			int distance = Integer.parseInt(table.getRequestQuantity()) - Integer.parseInt(table.getDeliveryQuantity());

			//출고등록품목정보 입력하기
			stDAO.saveDeliveiedItemInfo(table.getDeliveryNo(),table.getItemCode(),table.getItemName(),table.getItemDesc(),table.getItemType(),table.getRequestQuantity(),table.getRequestUnit(),table.getRequestDate(),table.getFactoryCode(),table.getFactoryName(),table.getWarehouseCode(),table.getWarehouseName(),table.getRefNo(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),Integer.toString(distance),table.getRequestUnit(),"",deliverer_div_code,deliverer_div_name,login_id,deliverer_info,"S53",aid,table.getDeliveryQuantity());
		}
	}

	/**************************************************
	 * 생산출고처리 결재 처리 
	 *************************************************/
	public void DeliveryAppInfoProcess(String mode,String tmp_aid,String aid) throws Exception {
		StockMgrDAO stDAO = new StockMgrDAO(con);
		ReservedItemInfoTable table = new ReservedItemInfoTable();
		com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
		com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		
		ArrayList item_list = new ArrayList();
		item_list = stDAO.getDeliveriedItemList(mode,tmp_aid);

		Iterator table_iter = item_list.iterator();
		while(table_iter.hasNext()){
			table = (ReservedItemInfoTable)table_iter.next();
			String delivery_no		= table.getDeliveryNo();		//출고의뢰번호
			String item_code		= table.getItemCode();			//출고품목코드
			String ref_no			= table.getRefNo();				//작업지시번호

			//상신처리
			if (mode.equals("app_delivery"))	{
				//상태코드를 S55(출고결재상신상태)로 설정
				stDAO.updateProcessStat("st_reserved_item_info","delivery_no",delivery_no,item_code,"S55");
				stDAO.updateProcessStat("st_deliveried_item_info","aid",tmp_aid,"S55");
				//임시전자결재관리번호를 정식관리번호로 변경
				stDAO.updateAid("st_deliveried_item_info",tmp_aid,aid);
			}

			//반려처리
			else if (mode.equals("reject_delivery"))	{
				//결재정보저장-(TABLE:st_approval_info)
				appMgrDAO.getAppInfoAndSave("st_approval_info",aid);

				//상태코드 변경
				stDAO.updateProcessStat("st_reserved_item_info","delivery_no",delivery_no,item_code,"S53");
				stDAO.updateProcessStat("st_deliveried_item_info","aid",aid,"S56");
			}

			//승인처리
			else if (mode.equals("commit_delivery"))	{
				//결재정보저장-(TABLE:st_approval_info)
				appMgrDAO.getAppInfoAndSave("st_approval_info",aid);
				//상태코드 변경
				stDAO.updateProcessStat("st_reserved_item_info","delivery_no",delivery_no,item_code,"S57");
				stDAO.updateProcessStat("st_deliveried_item_info","aid",aid,"S57");
			}
		}//end while
	}
}