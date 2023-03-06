package com.anbtech.pu.db;

import com.anbtech.pu.entity.*;
import com.anbtech.pu.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class PurchaseMgrDAO{
	private Connection con;

	public PurchaseMgrDAO(Connection con){
		this.con = con;
	}

	/*********************************************
	 * 선택된 구매요청번호 및 품목코드에 해당하는 정보 가져오기
	 *********************************************/
	public RequestInfoTable getRequestItemInfo(String request_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		RequestInfoTable table = new RequestInfoTable();

		// pu_requested_info : a
		// pu_requested_item : b
		String sql = "SELECT b.mid,b.*,a.* FROM pu_requested_info a,pu_requested_item b WHERE a.request_no = b.request_no AND a.request_no = '" + request_no + "' AND b.item_code = '" + item_code + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString(1));
			table.setRequestDate(rs.getString("request_date"));
			table.setRequesterDivCode(rs.getString("requester_div_code"));
			table.setRequesterDivName(rs.getString("requester_div_name"));
			table.setRequesterId(rs.getString("requester_id"));
			table.setRequesterInfo(rs.getString("requester_info"));
			table.setProjectCode(rs.getString("project_code"));
			table.setProjectName(rs.getString("project_name"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setOrderQuantity(rs.getString("order_quantity"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setSupplyCost(rs.getString("supply_cost"));
			table.setRequestType(rs.getString("request_type"));
			table.setRequestCost(rs.getString("request_cost"));
			table.setItemCode(item_code);
			table.setRequestNo(request_no);
			table.setProcessStat(rs.getString("process_stat"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 선택된 구매요청번호에 해당하는 정보 가져오기
	 *********************************************/
	public String getRequestTotalMount(String request_no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT request_total_mount FROM pu_requested_info WHERE request_no = '" + request_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		String request_total_mount="0";

		while(rs.next()){
			request_total_mount = rs.getString("request_total_mount");
		}
		stmt.close();
		rs.close();

		return request_total_mount;

	}

	/*********************************************
	 * 선택된 구매요청번호에 해당하는 정보 가져오기
	 *********************************************/
	public RequestInfoTable getRequestInfo(String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		RequestInfoTable table = new RequestInfoTable();

		String sql = "SELECT * FROM pu_requested_info WHERE request_no = '" + request_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setRequestNo(request_no);
			table.setMid(rs.getString("mid"));
			table.setRequestDate(rs.getString("request_date"));
			table.setRequesterDivCode(rs.getString("requester_div_code"));
			table.setRequesterDivName(rs.getString("requester_div_name"));
			table.setRequesterId(rs.getString("requester_id"));
			table.setRequesterInfo(rs.getString("requester_info"));
			table.setRequestType(rs.getString("request_type"));
			table.setRequestTotalMount(rs.getString("request_total_mount"));
			table.setProjectCode(rs.getString("project_code"));
			table.setProjectName(rs.getString("project_name"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			
			table.setProcessStat(getMaxStatForRequestItem(request_no));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/******************************************************
	 * 선택된 발주번호 및 발주 품목코드에 해당하는 정보 가져오기
	 ******************************************************/
	public OrderInfoTable getOrderItemInfo(String order_no,String item_code,String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		OrderInfoTable table = new OrderInfoTable();
		

		// pu_order_info : a
		// pu_order_item : b
		String sql = "SELECT a.*,b.* FROM pu_order_info a,pu_order_item b WHERE a.order_no = b.order_no AND a.order_no = '" + order_no + "' AND b.item_code = '" + item_code + "' AND b.request_no = '" + request_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setOrderNo(order_no);
			table.setOrderType(rs.getString("order_type"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setOrderDate(rs.getString("order_date"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setOrderTotalMount(rs.getString("order_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setExchangeRate(rs.getString("exchange_rate"));
			table.setVatType(rs.getString("vat_type"));
			table.setVatRate(rs.getString("vat_rate"));
			table.setVatMount(rs.getString("vat_mount"));
			table.setIsVatContained(rs.getString("is_vat_contained"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApprovalPeriod(rs.getString("approval_period"));
			table.setPaymentType(rs.getString("payment_type"));
			table.setSupplyerInfo(rs.getString("supplyer_info"));
			table.setSupplyerTel(rs.getString("supplyer_tel"));
			table.setOtherInfo(rs.getString("other_info"));
			table.setInOutType(rs.getString("inout_type"));

			table.setItemCode(item_code);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setOrderQuantity(rs.getString("order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setIsConfirmCost(rs.getString("is_confirmed_cost"));
			table.setOrderCost(rs.getString("order_cost"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setRequestNo(rs.getString("request_no"));
		}
		stmt.close();
		rs.close();

		return table;
	}


	/******************************************************
	 * 선택된 발주번호에 해당하는 정보 가져오기
	 ******************************************************/
	public OrderInfoTable getOrderItemInfo(String order_no,String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		OrderInfoTable table = new OrderInfoTable();
		
		// pu_order_info : a
		// pu_order_item : b
		String sql = "SELECT * FROM pu_order_info WHERE order_no = '" + order_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setOrderNo(order_no);
			table.setOrderType(rs.getString("order_type"));
			//table.setProcessStat(rs.getString("process_stat"));
			table.setOrderDate(rs.getString("order_date"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setOrderTotalMount(rs.getString("order_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setExchangeRate(rs.getString("exchange_rate"));
			table.setVatType(rs.getString("vat_type"));
			table.setVatRate(rs.getString("vat_rate"));
			table.setVatMount(rs.getString("vat_mount"));
			table.setIsVatContained(rs.getString("is_vat_contained"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApprovalPeriod(rs.getString("approval_period"));
			table.setPaymentType(rs.getString("payment_type"));
			table.setSupplyerInfo(rs.getString("supplyer_info"));
			table.setSupplyerTel(rs.getString("supplyer_tel"));
			table.setOtherInfo(rs.getString("other_info"));
			table.setInOutType(rs.getString("inout_type"));
/*
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setOrderQuantity(rs.getString("order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setIsConfirmCost(rs.getString("is_confirmed_cost"));
			table.setOrderCost(rs.getString("order_cost"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setRequestNo(rs.getString("request_no"));*/
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 선택된 입고요청번호 및 품목코드에 해당하는 정보 가져오기
	 *********************************************/
	public EnterInfoTable getEnterItemInfo(String enter_no,String item_code,String order_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		EnterInfoTable table = new EnterInfoTable();

		// pu_entered_info : a
		// pu_entered_item : b
		String sql = "SELECT a.*,b.* FROM pu_entered_info a,pu_entered_item b WHERE a.enter_no = b.enter_no AND a.enter_no = '" + enter_no + "' AND b.item_code = '" + item_code + "' AND b.order_no = '" + order_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setEnterNo(enter_no);
			table.setProcessStat(rs.getString("process_stat"));
			table.setEnterDate(rs.getString("enter_date"));
			table.setEnterTotalMount(rs.getString("enter_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setEnterType(rs.getString("enter_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
	
			Iterator file_iter = getFile_list("pu_entered_info", rs.getString("mid")).iterator();
					
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

				RequestInfoTable file = (RequestInfoTable)file_iter.next();
				filelink += "<a href='PurchaseMgrServlet?tablename=pu_entered_info&upload_folder=estimate&mode=download&mid="+(""+rs.getInt("mid"))+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0>"+file.getFname()+" ("+file.getFsize()+" bytes)</a>";
				j++;				
			}

			table.setFileLink(filelink);
			

			table.setItemCode(item_code);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setEnterQuantity(rs.getString("enter_quantity"));
			table.setEnterUnit(rs.getString("enter_unit"));
			table.setEnterCost(rs.getString("enter_cost"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setOrderNo(rs.getString("order_no"));
			table.setRequestNo(rs.getString("request_no"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 선택된 입고요청번호 및 품목코드에 해당하는 정보 가져오기
	 *********************************************/
	public EnterInfoTable getEnterItemInfo(String enter_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		EnterInfoTable table = new EnterInfoTable();

		String sql = "SELECT * FROM pu_entered_item WHERE enter_no = '" + enter_no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setItemCode(item_code);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setEnterQuantity(rs.getString("enter_quantity"));
			table.setEnterUnit(rs.getString("enter_unit"));
			table.setEnterCost(rs.getString("enter_cost"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setOrderNo(rs.getString("order_no"));
			table.setRequestNo(rs.getString("request_no"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 선택된 발주번호에 해당하는 정보 가져오기
	 *********************************************/
	public OrderInfoTable getOrderInfo(String order_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		OrderInfoTable table = new OrderInfoTable();
		

		String sql = "SELECT * FROM pu_order_info WHERE order_no = '" + order_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setOrderNo(order_no);
			table.setOrderType(rs.getString("order_type"));
			table.setProcessStat(getMaxStatForOrderItem(order_no));
			table.setOrderDate(rs.getString("order_date"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setOrderTotalMount(rs.getString("order_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setExchangeRate(rs.getString("exchange_rate"));
			table.setVatType(rs.getString("vat_type"));
			table.setVatRate(rs.getString("vat_rate"));
			table.setVatMount(rs.getString("vat_mount"));
			table.setIsVatContained(rs.getString("is_vat_contained"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApprovalPeriod(rs.getString("approval_period"));
			table.setPaymentType(rs.getString("payment_type"));
			table.setSupplyerInfo(rs.getString("supplyer_info"));
			table.setSupplyerTel(rs.getString("supplyer_tel"));
			table.setOtherInfo(rs.getString("other_info"));
			table.setInOutType(rs.getString("inout_type"));
		}
		stmt.close();
		rs.close();

		return table;
	}


	/*********************************************
	 * 선택된 입고번호에 해당하는 정보 가져오기
	 *********************************************/
	public EnterInfoTable getEnterInfo(String enter_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		EnterInfoTable table = new EnterInfoTable();

		String sql = "SELECT * FROM pu_entered_info WHERE enter_no = '" + enter_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setEnterNo(enter_no);
			table.setProcessStat(getMaxStatForEnterItem(enter_no));
			table.setEnterDate(rs.getString("enter_date"));
			table.setEnterTotalMount(rs.getString("enter_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setEnterType(rs.getString("enter_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			
			Iterator file_iter = getFile_list("pu_entered_info",rs.getString("mid")).iterator();
			
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

				RequestInfoTable file = (RequestInfoTable)file_iter.next();
				filelink += "<a href='PurchaseMgrServlet?tablename=pu_entered_info&upload_folder=estimate&mode=enterfile_download&mid="+(""+rs.getInt("mid"))+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0>"+file.getFname()+" ("+file.getFsize()+" bytes)</a>";
				j++;				
			}

			table.setFileLink(filelink);
		}
		stmt.close();
		rs.close();

		return table;
	}


	/*************************************************
	 * 선택된 구매요청번호에 존재하는 품목리스트를 가져온다.
	 *************************************************/
	public ArrayList getRequestItemList(String request_no) throws Exception {
		PurchaseMgrBO purchaseBO = new PurchaseMgrBO(con);
		Statement stmt = null;
		ResultSet rs = null;
		String tablename = "pu_requested_item";

		// 구매요청번호에 해당하는 요청구분을 가져온다.
		String request_type = getRequestType(request_no);

		ArrayList item_list = new ArrayList();
		RequestInfoTable table = new RequestInfoTable();
		
		String query = "SELECT * FROM pu_requested_item WHERE request_no = '" + request_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		
		while(rs.next()){
			String mid				= rs.getString("mid");
			String item_code		= rs.getString("item_code");
			String item_name		= rs.getString("item_name");
			String item_desc		= rs.getString("item_desc");
			String request_unit		= rs.getString("request_unit");
			String delivery_date	= rs.getString("delivery_date");
			String request_quantity	= rs.getString("request_quantity");
			String order_quantity	= rs.getString("order_quantity");
			String delivery_quantity= rs.getString("delivery_quantity");
			String process_stat		= rs.getString("process_stat");
			String supply_cost		= rs.getString("supply_cost");
			String request_cost		= rs.getString("request_cost");
			String supplyer_code	= rs.getString("supplyer_code");
			String supplyer_name	= rs.getString("supplyer_name");

			String item_code_link	= rs.getString("item_code");
			
			//상태코드가 S01(등록요청)상태인 경우만 수정,삭제 가능하게 링크 enable시킨다.
			if(process_stat.equals("S01")){
				item_code_link = "<a href='PurchaseMgrServlet";
				item_code_link += "?mode=modify_request";
				item_code_link += "&request_no="+request_no;
				item_code_link += "&request_type="+request_type;
				item_code_link += "&item_code="+rs.getString("item_code");
				item_code_link += "&supply_cost="+rs.getString("supply_cost");
				item_code_link += "'>"+rs.getString("item_code")+"</a>";
			}

			Iterator file_iter = getFile_list(tablename, mid).iterator();
			
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

				RequestInfoTable file = (RequestInfoTable)file_iter.next();
				filelink += "<a href='PurchaseMgrServlet?tablename="+tablename+"&upload_folder=estimate&mode=download&mid="+(""+rs.getInt("mid"))+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0></a>";
				j++;				
			}

			table = new RequestInfoTable();
			table.setMid(mid);
			table.setItemCode(item_code);
			table.setItemCodeLink(item_code_link);
			table.setItemName(item_name);
			table.setItemDesc(item_desc);
			table.setRequestUnit(request_unit);
			table.setDeliveryDate(delivery_date);
			table.setRequestQuantity(request_quantity);
			table.setOrderQuantity(order_quantity);
			table.setDeliveryQuantity(delivery_quantity);
			table.setProcessStat(process_stat);
			table.setFileLink(filelink);
			table.setSupplyCost(supply_cost);
			table.setRequestCost(request_cost);
			table.setSupplyerCode(supplyer_code);
			table.setSupplyerName(supplyer_name);

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/************************************
	 * 선택된 발주번호 존재하는 품목리스트를 가져온다.
	 ************************************/
	public ArrayList getOrderItemList(String order_no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		

		ArrayList item_list = new ArrayList();
		OrderInfoTable table = new OrderInfoTable();
		
		String query = "SELECT * FROM pu_order_item WHERE order_no = '" + order_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new OrderInfoTable();

			String item_code_link	= rs.getString("item_code");
			//상태코드가 S05(발주등록)일 경우만 수정,삭제 가능하게 링크 enable
			if(rs.getString("process_stat").equals("S05")){
				item_code_link = "<a href='PurchaseMgrServlet";
				item_code_link += "?mode=modify_order";
				item_code_link += "&order_no="+order_no;
				item_code_link += "&item_code="+rs.getString("item_code");
				item_code_link += "&request_no="+rs.getString("request_no");
				item_code_link += "'>"+rs.getString("item_code")+"</a>";
			}
			
			table.setItemCode(rs.getString("item_code"));
			table.setItemCodeLink(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setOrderQuantity(rs.getString("order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setIsConfirmCost(rs.getString("is_confirmed_cost"));
			table.setOrderCost(rs.getString("order_cost"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setRequestNo(rs.getString("request_no"));
			table.setProcessStat(rs.getString("process_stat"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/************************************
	 * 요청번호에 해당하는 요청 구분 가져오기
	 ************************************/
	public String getRequestType(String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String request_type = "";

		ArrayList item_list = new ArrayList();
		EnterInfoTable table = new EnterInfoTable();
		
		String query = "SELECT request_type FROM pu_requested_info WHERE request_no = '" + request_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while (rs.next())
		{
			request_type = rs.getString("request_type");
		}

		rs.close();
		stmt.close();
		return request_type;
	}

	/************************************
	 * 선택된 구매입고번호에 존재하는 품목리스트를 가져온다.
	 ************************************/
	public ArrayList getEnterItemList(String enter_no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList item_list = new ArrayList();
		EnterInfoTable table = new EnterInfoTable();
		
		String query = "SELECT * FROM pu_entered_item WHERE enter_no = '" + enter_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new EnterInfoTable();

			String item_code_link	= rs.getString("item_code");
			if(rs.getString("process_stat").equals("S18")){
				item_code_link = "<a href='PurchaseMgrServlet";
				item_code_link += "?mode=modify_enter";
				item_code_link += "&enter_no="+enter_no;
				item_code_link += "&item_code="+rs.getString("item_code");
				item_code_link += "&order_no="+rs.getString("order_no");
				item_code_link += "'>"+rs.getString("item_code")+"</a>";
			}

			table.setItemCode(rs.getString("item_code"));
			table.setItemCodeLink(item_code_link);
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setEnterQuantity(rs.getString("enter_quantity"));
			table.setEnterUnit(rs.getString("enter_unit"));
			table.setEnterCost(rs.getString("enter_cost"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setOrderNo(rs.getString("order_no"));
			table.setRequestNo(rs.getString("request_no"));
			table.setProcessStat(rs.getString("process_stat"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}


	/*********************************
	 * 구매요청정보 저장
	 *********************************/
	 public void saveRequestInfo(String request_no,String request_date,String requester_div_code,String requester_div_name,String requester_id,String requester_info,String request_type,String request_total_mount,String project_code,String project_name,String reg_year,String reg_month,String reg_serial,String factory_code,String factory_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_requested_info (request_no,request_date,requester_div_code,requester_div_name,requester_id,requester_info,request_type,request_total_mount,project_code,project_name,reg_year,reg_month,reg_serial,factory_code,factory_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_no);
		pstmt.setString(2,request_date);
		pstmt.setString(3,requester_div_code);
		pstmt.setString(4,requester_div_name);
		pstmt.setString(5,requester_id);
		pstmt.setString(6,requester_info);
		pstmt.setString(7,request_type);
		pstmt.setString(8,request_total_mount);
		pstmt.setString(9,project_code);
		pstmt.setString(10,project_name);
		pstmt.setString(11,reg_year);
		pstmt.setString(12,reg_month);
		pstmt.setString(13,reg_serial);
		pstmt.setString(14,factory_code);
		pstmt.setString(15,factory_name);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 구매요청품목정보 저장
	 *********************************/
	 public void saveRequestItemInfo(String request_no,String item_code,String item_name,String item_desc,String request_unit,String delivery_date,String request_quantity,String order_quantity,String delivery_quantity,String process_stat,String supplyer_code,String supplyer_name,String supply_cost,String request_cost) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_requested_item (request_no,item_code,item_name,item_desc,request_unit,delivery_date,request_quantity,order_quantity,delivery_quantity,process_stat,supplyer_code,supplyer_name,supply_cost,request_cost) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_no);
		pstmt.setString(2,item_code);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setString(5,request_unit);
		pstmt.setString(6,delivery_date);
		pstmt.setString(7,request_quantity);
		pstmt.setString(8,order_quantity);
		pstmt.setString(9,delivery_quantity);
		pstmt.setString(10,process_stat);
		pstmt.setString(11,supplyer_code);
		pstmt.setString(12,supplyer_name);
		pstmt.setString(13,supply_cost);
		pstmt.setString(14,request_cost);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 발주정보 저장
	 *********************************/
	 public void saveOrderInfo(String order_no,String order_type,String supplyer_code,String supplyer_name,String order_date,String monetary_unit,String exchange_rate,String order_total_mount,String vat_rate,String vat_mount,String supplyer_info,String supplyer_tel,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String vat_type,String is_vat_contained,String approval_type,String approval_period,String payment_type,String other_info,String inout_type,String reg_year,String reg_month,String reg_serial) throws Exception{
		PreparedStatement pstmt = null;

		if(requestor_id != null && !requestor_id.equals("")){
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(requestor_id);

			if(requestor_div_name == null || requestor_div_name.equals("")) requestor_div_name	= userinfo.getDivision();
			if(requestor_div_code == null || requestor_div_code.equals("")) requestor_div_code	= userinfo.getAcCode();
			if(requestor_info == null || requestor_info.equals("")) requestor_info		= userinfo.getUserName();
		}

		String query = "INSERT INTO pu_order_info (order_no,order_type,supplyer_code,supplyer_name,order_date,monetary_unit,exchange_rate,order_total_mount,vat_rate,vat_mount,supplyer_info,supplyer_tel,requestor_div_code,requestor_div_name,requestor_id,requestor_info,vat_type,is_vat_contained,approval_type,approval_period,payment_type,other_info,inout_type,reg_year,reg_month,reg_serial) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_no);
		pstmt.setString(2,order_type);
		pstmt.setString(3,supplyer_code);
		pstmt.setString(4,supplyer_name);
		pstmt.setString(5,order_date);
		pstmt.setString(6,monetary_unit);
		pstmt.setString(7,exchange_rate);
		pstmt.setString(8,order_total_mount);
		pstmt.setString(9,vat_rate);
		pstmt.setString(10,vat_mount);
		pstmt.setString(11,supplyer_info);
		pstmt.setString(12,supplyer_tel);
		pstmt.setString(13,requestor_div_code);
		pstmt.setString(14,requestor_div_name);
		pstmt.setString(15,requestor_id);
		pstmt.setString(16,requestor_info);
		pstmt.setString(17,vat_type);
		pstmt.setString(18,is_vat_contained);
		pstmt.setString(19,approval_type);
		pstmt.setString(20,approval_period);
		pstmt.setString(21,payment_type);
		pstmt.setString(22,other_info);
		pstmt.setString(23,inout_type);
		pstmt.setString(24,reg_year);
		pstmt.setString(25,reg_month);
		pstmt.setString(26,reg_serial);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 발주품목정보 저장
	 *********************************/
	public void saveOrderItemInfo(String order_no,String request_no,String item_code,String item_name,String item_desc,String order_quantity,String order_unit,String delivery_date,String unit_cost,String order_cost,String delivery_quantity,String process_stat) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_order_item (order_no,request_no,item_code,item_name,item_desc,order_quantity,order_unit,unit_cost,order_cost,delivery_date,delivery_quantity,process_stat) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_no);
		pstmt.setString(2,request_no);
		pstmt.setString(3,item_code);
		pstmt.setString(4,item_name);
		pstmt.setString(5,item_desc);
		pstmt.setString(6,order_quantity);
		pstmt.setString(7,order_unit);
		pstmt.setString(8,unit_cost);
		pstmt.setString(9,order_cost);
		pstmt.setString(10,delivery_date);
		pstmt.setString(11,delivery_quantity);
		pstmt.setString(12,process_stat);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 구매입고정보 저장
	 *********************************/
	 public void saveEnterInfo(String enter_no,String process_stat,String enter_date,String enter_total_mount,String monetary_unit,String enter_type,String supplyer_code,String supplyer_name,String reg_year,String reg_month,String reg_serial,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info) throws Exception{
		PreparedStatement pstmt = null;

		if(requestor_id != null && !requestor_id.equals("")){
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(requestor_id);

			if(requestor_div_name == null || requestor_div_name.equals("")) requestor_div_name	= userinfo.getDivision();
			if(requestor_div_code == null || requestor_div_code.equals("")) requestor_div_code	= userinfo.getAcCode();
			if(requestor_info == null || requestor_info.equals("")) requestor_info		= userinfo.getUserName();
		}

		String query = "INSERT INTO pu_entered_info (enter_no,process_stat,enter_date,enter_total_mount,monetary_unit,enter_type,supplyer_code,supplyer_name,reg_year,reg_month,reg_serial,requestor_div_code,requestor_div_name,requestor_id,requestor_info) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,enter_no);
		pstmt.setString(2,process_stat);
		pstmt.setString(3,enter_date);
		pstmt.setString(4,enter_total_mount);
		pstmt.setString(5,monetary_unit);
		pstmt.setString(6,enter_type);
		pstmt.setString(7,supplyer_code);
		pstmt.setString(8,supplyer_name);
		pstmt.setString(9,reg_year);
		pstmt.setString(10,reg_month);
		pstmt.setString(11,reg_serial);
		pstmt.setString(12,requestor_div_code);
		pstmt.setString(13,requestor_div_name);
		pstmt.setString(14,requestor_id);
		pstmt.setString(15,requestor_info);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * 구매입고품목정보 저장
	 *********************************/
	public void saveEnterItemInfo(String enter_no,String order_no,String request_no,String item_code,String item_name,String item_desc,String enter_quantity,String enter_unit,String enter_cost,String unit_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String process_stat) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO pu_entered_item (enter_no,order_no,request_no,item_code,item_name,item_desc,enter_quantity,enter_unit,enter_cost,unit_cost,factory_code,factory_name,warehouse_code,warehouse_name,process_stat) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,enter_no);
		pstmt.setString(2,order_no);
		pstmt.setString(3,request_no);
		pstmt.setString(4,item_code);
		pstmt.setString(5,item_name);
		pstmt.setString(6,item_desc);
		pstmt.setString(7,enter_quantity);
		pstmt.setString(8,enter_unit);
		pstmt.setString(9,enter_cost);
		pstmt.setString(10,unit_cost);
		pstmt.setString(11,factory_code);
		pstmt.setString(12,factory_name);
		pstmt.setString(13,warehouse_code);
		pstmt.setString(14,warehouse_name);
		pstmt.setString(15,process_stat);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/************************************
	 * 검색조건에 맞는 구매요청정보 가져오기
	 ************************************/
	public ArrayList getRequestInfoList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		RequestInfoTable table		= new RequestInfoTable();

		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList request_list = new ArrayList();
		String where = purchaseBO.getWhereForRequestInfoList(mode,searchword,searchscope,login_id);
		int total = getTotalCount("pu_requested_info", where);
		int recNum = total;

		String query = "SELECT * FROM pu_requested_info " + where + " ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");				//관리 번호
			String request_no			= rs.getString("request_no");		//구매요청번호
			String request_date			= rs.getString("request_date");		//요청일자
			String requester_div_name	= rs.getString("requester_div_name");//요청부서
			String requester_info		= rs.getString("requester_info");	//요청자
			String request_type			= rs.getString("request_type");		//요청구분
			String project_code			= rs.getString("project_code");		//과제코드
			String project_name			= rs.getString("project_name");		//과제명
			String request_total_mount	= rs.getString("request_total_mount");

			String process_stat			= getMaxStatForRequestItem(request_no);		//진행상태

			String request_no_link = "";
			request_no_link = "<a href='PurchaseMgrServlet?mode=modify_request_info&request_no=" + request_no+"&request_type="+request_type;
			request_no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
			request_no_link += "onMouseOver=\"window.status='구매요청정보 상세보기 #" + request_no + "';return true;\" ";
			request_no_link += "onMouseOut=\"window.status='';return true;\">" + request_no + "</a>";

			
			table = new RequestInfoTable();
			table.setMid(mid);
			table.setRequestNo(request_no_link);
			table.setRequestDate(request_date);
			table.setRequesterDivName(requester_div_name);
			table.setRequesterInfo(requester_info);
			table.setRequestType(request_type);
			table.setProcessStat(process_stat);
			table.setProjectCode(project_code);
			table.setProjectName(project_name);
			table.setRequestTotalMount(request_total_mount);

			request_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return request_list;
	}

	/************************************
	 * 검색조건에 맞는 발주정보 가져오기
	 ************************************/
	public ArrayList getOrderInfoList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		OrderInfoTable table		= new OrderInfoTable();
		

		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList order_list = new ArrayList();
		String where = purchaseBO.getWhereForOrderInfoList(mode,searchword,searchscope,login_id);
		int total = getTotalCount("pu_order_info", where);
		int recNum = total;

		String query = "SELECT * FROM pu_order_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String order_no_link = "";
			order_no_link += "<a href='PurchaseMgrServlet?mode=modify_order_info&order_no=" + rs.getString("order_no");
			order_no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
			order_no_link += "onMouseOver=\"window.status='발주정보 상세보기 #" + rs.getString("order_no") + "';return true;\" ";
			order_no_link += "onMouseOut=\"window.status='';return true;\">" + rs.getString("order_no") + "</a>";
			
			table = new OrderInfoTable();
			
			table.setOrderNo(order_no_link);
			table.setProcessStat(getMaxStatForOrderItem(rs.getString("order_no")));
			table.setOrderType(rs.getString("order_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setOrderDate(rs.getString("order_date"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setExchangeRate(rs.getString("exchange_rate"));
			table.setOrderTotalMount(rs.getString("order_total_mount"));
			table.setVatRate(rs.getString("vat_rate"));
			table.setVatMount(rs.getString("vat_mount"));
			table.setSupplyerInfo(rs.getString("supplyer_info"));
			table.setSupplyerTel(rs.getString("supplyer_tel"));

			order_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return order_list;
	}

	/*******************************
	 *  발주 총 금액 변경(UPDATE).
	 *******************************/
	public void updateRequestTotalMount(String request_no,String request_total_mount) throws Exception {
		Statement stmt = null;
		String query = "UPDATE pu_requested_info SET request_total_mount='"+request_total_mount+"' WHERE request_no='"+request_no+"'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/************************************
	 * 검색조건에 맞는 구매입고정보 가져오기
	 ************************************/
	public ArrayList getEnterInfoList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		EnterInfoTable table		= new EnterInfoTable();
		
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList enter_list = new ArrayList();
		String where = purchaseBO.getWhereForEnterInfoList(mode,searchword,searchscope,login_id);
		int total = getTotalCount("pu_entered_info", where);
		int recNum = total;

		String query = "SELECT * FROM pu_entered_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String enter_no_link = "";
			enter_no_link += "<a href='PurchaseMgrServlet?mode=modify_enter_info&enter_no=" + rs.getString("enter_no");
			enter_no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
			enter_no_link += "onMouseOver=\"window.status='입고정보 상세보기 #" + rs.getString("enter_no") + "';return true;\" ";
			enter_no_link += "onMouseOut=\"window.status='';return true;\">" + rs.getString("enter_no") + "</a>";
			
		
				
			table = new EnterInfoTable();
				Iterator file_iter = getEnteredFile_list("pu_entered_info", ""+rs.getInt("mid")).iterator();

			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

			EnterInfoTable file = (EnterInfoTable)file_iter.next();
			filelink += "<a href='PurchaseMgrServlet?tablename=pu_entered_info&upload_folder=receipt&mode=enterfile_download&mid="+(""+rs.getInt("mid"))+"_"+j+"' ";
			filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='../pu/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0></a>";
			j++;				
			}

			table.setEnterNo(enter_no_link);
			
			table.setProcessStat(getMaxStatForEnterItem(rs.getString("enter_no")));
			table.setEnterType(rs.getString("enter_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setEnterDate(rs.getString("enter_date"));
			table.setEnterTotalMount(rs.getString("enter_total_mount"));
			table.setFileLink(filelink);

			enter_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return enter_list;
	}

	/************************************
	 * 검색조건에 맞는 구매요청품목정보 가져오기
	 * v_requested_item 뷰 테이블 검색
	 ************************************/
	public ArrayList getRequestItemList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		RequestInfoTable table		= new RequestInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList request_list = new ArrayList();
		String where = purchaseBO.getWhereForRequestItemList(mode,searchword,login_id);
		int total = getTotalCount("v_requested_item", where);
		int recNum = total;

		String query = "SELECT * FROM v_requested_item " + where + " ORDER BY request_no DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
/*
		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
*/
		while(rs.next()){
			String request_no			= rs.getString("request_no");		//구매요청번호
			String request_date			= rs.getString("request_date");		//요청일자
			String requester_div_name	= rs.getString("requester_div_name");//요청부서
			String requester_info		= rs.getString("requester_info");	//요청자
			String request_type			= rs.getString("request_type");		//요청구분
			String process_stat			= rs.getString("process_stat");		//진행상태
			
			String mid					= rs.getString("mid");
			String item_code			= rs.getString("item_code");
			String item_name			= rs.getString("item_name");
			String item_desc			= rs.getString("item_desc");
			String request_unit			= rs.getString("request_unit");
			String delivery_date		= rs.getString("delivery_date");
			String request_quantity		= rs.getString("request_quantity");
			String order_quantity		= rs.getString("order_quantity");
			String delivery_quantity	= rs.getString("delivery_quantity");
			String project_code			= rs.getString("project_code");
			String project_name			= rs.getString("project_name");
			String supplyer_code		= rs.getString("supplyer_code");
			String supplyer_name		= rs.getString("supplyer_name");
			String supply_cost			= rs.getString("supply_cost");
			String request_cost			= rs.getString("request_cost");

			
			String item_code_link		= "";
			item_code_link += "<a href = \"javascript:view_supply_info('" + item_code + "');\">" + item_code + "</a>";
			
			table = new RequestInfoTable();
			table.setRequestNo(request_no);
			table.setRequestDate(request_date);
			table.setRequesterDivName(requester_div_name);
			table.setRequesterInfo(requester_info);
			table.setRequestType(request_type);
			table.setProcessStat(process_stat);

			table.setItemCode(item_code);
			table.setItemCodeLink(item_code_link);
			table.setItemName(item_name);
			table.setItemDesc(item_desc);
			table.setRequestUnit(request_unit);
			table.setDeliveryDate(delivery_date);
			table.setRequestQuantity(request_quantity);
			table.setOrderQuantity(order_quantity);
			table.setDeliveryQuantity(delivery_quantity);
			table.setProjectCode(project_code);
			table.setProjectName(project_name);
			table.setSupplyerCode(supplyer_code);
			table.setSupplyerName(supplyer_name);
			table.setSupplyCost(supply_cost);
			table.setRequestCost(request_cost);

			Iterator file_iter = getFile_list("v_requested_item", mid).iterator();
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

				RequestInfoTable file = (RequestInfoTable)file_iter.next();
				filelink += "<a href='PurchaseMgrServlet?tablename=v_requested_item&mode=download&mid="+(""+rs.getInt("mid"))+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0>"+file.getFname()+" ("+file.getFsize()+" bytes)</a>";
				j++;				
			}
			table.setFileLink(filelink);


			request_list.add(table);
//			recNum--;
		}
		stmt.close();
		rs.close();

		return request_list;
	}

	/************************************
	 * 검색조건에 맞는 발주정보 가져오기
	 * v_order_item 뷰 테이블 검색
	 ************************************/
	public ArrayList getOrderItemList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		OrderInfoTable table		= new OrderInfoTable();

		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList order_list = new ArrayList();
		String where = purchaseBO.getWhereForOrderItemList(mode,searchword,login_id);
		int total = getTotalCount("v_order_item", where);
		int recNum = total;

		String query = "SELECT * FROM v_order_item " + where + " ORDER BY order_no DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
/*
		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
*/
		while(rs.next()){
			table = new OrderInfoTable();

			table.setOrderNo(rs.getString("order_no"));
			table.setOrderType(rs.getString("order_type"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setOrderDate(rs.getString("order_date"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setOrderTotalMount(rs.getString("order_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setExchangeRate(rs.getString("exchange_rate"));
			table.setVatType(rs.getString("vat_type"));
			table.setVatRate(rs.getString("vat_rate"));
			table.setVatMount(rs.getString("vat_mount"));
			table.setIsVatContained(rs.getString("is_vat_contained"));
			table.setApprovalType(rs.getString("approval_type"));
			table.setApprovalPeriod(rs.getString("approval_period"));
			table.setPaymentType(rs.getString("payment_type"));
			table.setSupplyerInfo(rs.getString("supplyer_info"));
			table.setSupplyerTel(rs.getString("supplyer_tel"));
			table.setOtherInfo(rs.getString("other_info"));

			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setOrderQuantity(rs.getString("order_quantity"));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setIsConfirmCost(rs.getString("is_confirmed_cost"));
			table.setOrderCost(rs.getString("order_cost"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setRequestNo(rs.getString("request_no"));

			order_list.add(table);
//			recNum--;
		}
		stmt.close();
		rs.close();

		return order_list;
	}

	/************************************
	 * 검색조건에 맞는 구매입고정보 가져오기
	 * v_entered_item 뷰 테이블 검색
	 ************************************/
	public ArrayList getEnterItemList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		EnterInfoTable table		= new EnterInfoTable();

		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList enter_list = new ArrayList();
		String where = purchaseBO.getWhereForEnterItemList(mode,searchword,login_id);
		int total = getTotalCount("v_entered_item", where);
		int recNum = total;

		String query = "SELECT * FROM v_entered_item " + where + " ORDER BY enter_no DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new EnterInfoTable();

			table.setEnterNo(rs.getString("enter_no"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setEnterDate(rs.getString("enter_date"));
			table.setEnterTotalMount(rs.getString("enter_total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setEnterType(rs.getString("enter_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));

			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setEnterQuantity(rs.getString("enter_quantity"));
			table.setEnterUnit(rs.getString("enter_unit"));
			table.setEnterCost(rs.getString("enter_cost"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setOrderNo(rs.getString("order_no"));
			table.setRequestNo(rs.getString("request_no"));
			table.setProjectCode(rs.getString("project_code"));
			table.setProjectName(rs.getString("project_name"));

			enter_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return enter_list;
	}

	/************************************
	 * 검색조건에 맞는 구매입고품목의 총 입고금액 계산
	 * v_entered_item 뷰 테이블 검색
	 ************************************/
	public String getTotalEnterCost(String mode,String searchword,String searchscope,String login_id) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);

		Statement stmt = null;
		ResultSet rs = null;

		String where = purchaseBO.getWhereForEnterItemList(mode,searchword,login_id);
		String query = "SELECT SUM(enter_cost) FROM v_entered_item " + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		String total_cost = rs.getString(1)==null?"0":rs.getString(1);

		stmt.close();
		rs.close();

		return total_cost;
	}

	/*******************************
	 * 구매요청품목 삭제하기
	 *******************************/
	public void deleteRequestItem(String request_no,String item_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_requested_item WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 발부품목 삭제하기
	 *******************************/
	public void deleteOrderItem(String order_no,String item_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_order_item WHERE order_no = '" + order_no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 입고품목 삭제하기
	 *******************************/
	public void deleteEnterItem(String enter_no,String item_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_entered_item WHERE enter_no = '" + enter_no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 구매요청정보 삭제하기
	 *******************************/
	public void deleteRequestInfo(String request_no) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_requested_info WHERE request_no = '" + request_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 발주정보 삭제하기
	 *******************************/
	public void deleteOrderInfo(String order_no) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_order_info WHERE order_no = '" + order_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 입고정보 삭제하기
	 *******************************/
	public void deleteEnterInfo(String enter_no) throws Exception{
		Statement stmt = null;
		String query = "DELETE pu_entered_info WHERE enter_no = '" + enter_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 구매요청품목정보 수정하기
	 *******************************/
	 public void updateRequestItemInfo(String request_no,String item_code,String item_name,String item_desc,String request_unit,String delivery_date,String request_quantity,String supplyer_code,String supplyer_name,String supply_cost,String request_cost) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_requested_item SET item_name=?,item_desc=?,request_unit=?,delivery_date=?,request_quantity=?,supplyer_code=?,supplyer_name=?,supply_cost=? , request_cost=? WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,item_desc);
		pstmt.setString(3,request_unit);
		pstmt.setString(4,delivery_date);
		pstmt.setString(5,request_quantity);
		pstmt.setString(6,supplyer_code);
		pstmt.setString(7,supplyer_name);
		pstmt.setString(8,supply_cost);
		pstmt.setString(9,request_cost);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 발주품목정보 수정하기
	 *******************************/
	 public void updateOrderItemInfo(String order_no,String item_code,String item_name,String item_desc,String order_quantity,String order_unit,String unit_cost,String is_confirmed_cost,String order_cost,String delivery_date,String delivery_quantity,String request_no) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_order_item SET item_name=?,item_desc=?,order_quantity=?,order_unit=?,unit_cost=?,is_confirmed_cost=?,order_cost=?,delivery_date=?,delivery_quantity=? WHERE order_no = '" + order_no + "' AND item_code = '" + item_code + "' AND request_no = '" + request_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,item_desc);
		pstmt.setString(3,order_quantity);
		pstmt.setString(4,order_unit);
		pstmt.setString(5,unit_cost);
		pstmt.setString(6,is_confirmed_cost);
		pstmt.setString(7,order_cost);
		pstmt.setString(8,delivery_date);
		pstmt.setString(9,delivery_quantity);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 발주품목정보 수정하기
	 *******************************/
	 public void updateOrderItemInfo(String order_no,String request_no,String item_code,String memo) throws Exception{
		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd 'at' hh:mm a");
		String w_time = vans.format(now);
		
		String other_info = memo + "(" + w_time + ")";
		
		String query = "UPDATE pu_order_item SET other_info=? WHERE order_no = '" + order_no + "' AND item_code = '" + item_code + "' AND request_no = '" + request_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,other_info);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 입고품목정보 수정하기
	 *******************************/
	 public void updateEnterItemInfo(String enter_no,String item_code,String item_name,String item_desc,String enter_quantity,String enter_unit,String unit_cost,String enter_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String request_no,String order_no) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_entered_item SET item_name=?,item_desc=?,enter_quantity=?,enter_unit=?,unit_cost=?,enter_cost=?,factory_code=?,factory_name=?,warehouse_code=?,warehouse_name=? WHERE enter_no = '" + enter_no + "' AND item_code = '" + item_code + "' AND order_no = '" + order_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,item_desc);
		pstmt.setString(3,enter_quantity);
		pstmt.setString(4,enter_unit);
		pstmt.setString(5,unit_cost);
		pstmt.setString(6,enter_cost);
		pstmt.setString(7,factory_code);
		pstmt.setString(8,factory_name);
		pstmt.setString(9,warehouse_code);
		pstmt.setString(10,warehouse_name);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 구매요청정보정보 수정하기
	 *******************************/
	 public void updateRequestInfo(String request_no,String request_date,String requester_div_code,String requester_div_name,String requester_id,String requester_info) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_requested_info SET request_date=?,requester_div_code=?,requester_div_name=?,requester_id=?,requester_info=? WHERE request_no = '" + request_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_date);
		pstmt.setString(2,requester_div_code);
		pstmt.setString(3,requester_div_name);
		pstmt.setString(4,requester_id);
		pstmt.setString(5,requester_info);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 발주등록정보 수정하기
	 *******************************/
	 public void updateOrderInfo(String order_no,String order_type,String order_date,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String supplyer_code,String supplyer_name,String order_total_mount,String monetary_unit,String exchange_rate,String vat_type,String vat_rate,String vat_mount,String is_vat_contained,String approval_type,String approval_period,String payment_type,String supplyer_info,String supplyer_tel,String other_info) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_order_info SET order_type=?,order_date=?,requestor_div_code=?,requestor_div_name=?,requestor_id=?,requestor_info=?,supplyer_code=?,supplyer_name=?,order_total_mount=?,monetary_unit=?,exchange_rate=?,vat_type=?,vat_rate=?,vat_mount=?,is_vat_contained=?,approval_type=?,approval_period=?,payment_type=?,supplyer_info=?,supplyer_tel=?,other_info=? WHERE order_no = '" + order_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,order_type);
		pstmt.setString(2,order_date);
		pstmt.setString(3,requestor_div_code);
		pstmt.setString(4,requestor_div_name);
		pstmt.setString(5,requestor_id);
		pstmt.setString(6,requestor_info);
		pstmt.setString(7,supplyer_code);
		pstmt.setString(8,supplyer_name);
		pstmt.setString(9,order_total_mount);
		pstmt.setString(10,monetary_unit);
		pstmt.setString(11,exchange_rate);
		pstmt.setString(12,vat_type);
		pstmt.setString(13,vat_rate);
		pstmt.setString(14,vat_mount);
		pstmt.setString(15,is_vat_contained);
		pstmt.setString(16,approval_type);
		pstmt.setString(17,approval_period);
		pstmt.setString(18,payment_type);
		pstmt.setString(19,supplyer_info);
		pstmt.setString(20,supplyer_tel);
		pstmt.setString(21,other_info);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 입고등록정보 수정하기
	 *******************************/
	 public void updateEnterInfo(String enter_no,String enter_type,String enter_date,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String enter_total_mount,String monetary_unit,String supplyer_code,String supplyer_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE pu_entered_info SET enter_type=?,enter_date=?,requestor_div_code=?,requestor_div_name=?,requestor_id=?,requestor_info=?,supplyer_code=?,supplyer_name=?,enter_total_mount=?,monetary_unit=? WHERE enter_no = '" + enter_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,enter_type);
		pstmt.setString(2,enter_date);
		pstmt.setString(3,requestor_div_code);
		pstmt.setString(4,requestor_div_name);
		pstmt.setString(5,requestor_id);
		pstmt.setString(6,requestor_info);
		pstmt.setString(7,supplyer_code);
		pstmt.setString(8,supplyer_name);
		pstmt.setString(9,enter_total_mount);
		pstmt.setString(10,monetary_unit);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 레코드의 전체 개수를 구한다.
	 *******************************/
	public int getTotalCount(String tablename, String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM " + tablename + where;

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();
//System.out.println(query);
		return total_count;
	}

	/*******************************
	 * 선택된 요청건에 대한 요청총금액을 계산
	 *******************************/
	public String calculateRequestTotalMount(String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String request_total_mount = "0";

		String query = "SELECT SUM(request_cost) FROM pu_requested_item WHERE request_no = '" + request_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		if(rs.getString(1) != null) request_total_mount = rs.getString(1);

		stmt.close();
		rs.close();

		return request_total_mount;
	}

	/*******************************
	 * 선택된 발주건에 대한 발주총금액을 계산
	 *******************************/
	public String calculateOrderTotalMount(String order_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String order_total_mount = "0";

		String query = "SELECT SUM(order_cost) FROM pu_order_item WHERE order_no = '" + order_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		if(rs.getString(1) != null) order_total_mount = rs.getString(1);

		stmt.close();
		rs.close();

		return order_total_mount;
	}

	/*******************************
	 * 선택된 입고건에 대한 총입고금액(매입금액)을 계산
	 *******************************/
	public String calculateEnterTotalMount(String enter_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String enter_total_mount = "0";

		String query = "SELECT SUM(enter_cost) FROM pu_entered_item WHERE enter_no = '" + enter_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		if(rs.getString(1) != null) enter_total_mount = rs.getString(1);

		stmt.close();
		rs.close();

		return enter_total_mount;
	}

	/*******************************
	 * pu_requested_item 테이블내 품목의 최종상태코드값을 가져온다.
	 *******************************/
	public String getMaxStatForRequestItem(String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String code = "";
		String query = "SELECT MAX(process_stat) FROM pu_requested_item WHERE request_no = '" + request_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString(1) != null) code = rs.getString(1);

		stmt.close();
		rs.close();

		return code;
	}

	/*******************************
	 * pu_order_item 테이블내 품목의 최종상태코드값을 가져온다.
	 *******************************/
	public String getMaxStatForOrderItem(String order_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String code = "";
		String query = "SELECT MAX(process_stat) FROM pu_order_item WHERE order_no = '" + order_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString(1) != null) code = rs.getString(1);

		stmt.close();
		rs.close();

		return code;
	}

	/*******************************
	 * pu_entered_item 테이블내 품목의 최종상태코드값을 가져온다.
	 *******************************/
	public String getMaxStatForEnterItem(String enter_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String code = "";
		String query = "SELECT MAX(process_stat) FROM pu_entered_item WHERE enter_no = '" + enter_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString(1) != null) code = rs.getString(1);

		stmt.close();
		rs.close();

		return code;
	}

	/*******************************
	 * pu_entered_item 테이블내 품목의 최종상태코드값을 가져온다.(요청번호를 가지고)
	 *******************************/
	public String getMaxStatForEnterItemByRequestNo(String request_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String code = "";
		String query = "SELECT MAX(process_stat) FROM pu_entered_item WHERE request_no = '" + request_no + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString(1) != null) code = rs.getString(1);

		stmt.close();
		rs.close();

		return code;
	}

	/*******************************
	 * 선택된 테이블내 품목 상태코드를 지정된 값으로 변경한다.
	 *******************************/
	 public void updateProcessStat(String tablename,String no_type,String no,String item_code,String process_stat) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + " SET process_stat = '" + process_stat + "' WHERE " + no_type + " = '" + no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 선택된 테이블내 품목 상태코드를 지정된 값으로 변경한다.(품목요청 결재 처리시)
	 *******************************/
	 public void updateProcessStat(String tablename,String no_type,String no,String process_stat) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + " SET process_stat = '" + process_stat + "' WHERE " + no_type + " = '" + no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 선택된 테이블내 지정된 필드의 수량값을 지정된값과 연산하여 변경한다.
	 *******************************/
	 public void updateQuantity(String tablename,String no_type,String no,String item_code,String quantity_type,String quantity) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		int old_quantity = 0;

		query = "SELECT " + quantity_type + " FROM " + tablename + " WHERE " + no_type + " = '" + no + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		if(rs.getString(1) != null) old_quantity = rs.getInt(1);

		quantity = Integer.toString(Integer.parseInt(quantity) + old_quantity);

		query = "UPDATE " + tablename + " SET " + quantity_type + " = '" + quantity + "' WHERE " + no_type + " = '" + no + "' AND item_code = '" + item_code + "'";

		stmt.executeUpdate(query);
		stmt.close();
		rs.close();
	}

	/********************************************************************
	 * rquest_no와 item_code에 해당하는 데이터의 관리번호를 가져와 리턴한다.
	 ********************************************************************/	
	public String getMid(String tablename, String request_no, String item_code) throws Exception{

		Statement stmt  = null;
		ResultSet rs	= null;

		String query = "SELECT mid FROM "+tablename+" WHERE request_no = '"+request_no+"' and item_code = '"+item_code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String no = rs.getString("mid");
		stmt.close();
		rs.close();
		return no;
	}

	/********************************************************************
	 * enter_no 에 해당하는 데이터의 관리번호를 가져와 리턴한다.
	 ********************************************************************/	
	public String getEnterMid(String tablename, String enter_no) throws Exception{

		Statement stmt  = null;
		ResultSet rs	= null;

		String query = "SELECT mid FROM "+tablename+" WHERE enter_no = '"+enter_no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String no = rs.getString("mid");
		stmt.close();
		rs.close();
		return no;
	}


	/*******************************
	 * 선택된 발주건의 aid 값을 업데이트
	 *******************************/
	 public void updateAid(String order_no,String aid) throws Exception{
		Statement stmt = null;
		String query = "UPDATE pu_order_info SET aid = '" + aid + "' WHERE order_no = '" + order_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
	
	/*************************************
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String tablename, String set, String where) throws Exception{
		Statement stmt = null;

		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	* 첨부파일 리스트 가져오기
	*****************************************************************/
	public ArrayList getFile_list(String tablename,String mid) throws Exception{

	Statement stmt = null;
	ResultSet rs = null;
	int total = 0;
	ArrayList file_list = new ArrayList();

	String query = "SELECT fname,ftype,fsize,fumask FROM "+tablename+" WHERE mid = '"+mid+"'";
	stmt = con.createStatement();
	rs = stmt.executeQuery(query);
	if(rs.next()){ 

		Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
		Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
		Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
		Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("fumask")).iterator();

		while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()&&fileumask_iter.hasNext()){
			RequestInfoTable file = new RequestInfoTable();
			file.setFname((String)filename_iter.next());
			file.setFtype((String)filetype_iter.next());
			file.setFsize((String)filesize_iter.next());
			file.setFumask((String)fileumask_iter.next());
			
			file_list.add(file);
		}
	}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()

	/*****************************************************************
	* 첨부파일 리스트 가져오기 (구매입고)
	*****************************************************************/
	public ArrayList getEnteredFile_list(String tablename,String mid) throws Exception{

	Statement stmt = null;
	ResultSet rs = null;
	int total = 0;
	ArrayList file_list = new ArrayList();

	String query = "SELECT fname,ftype,fsize,fumask FROM "+tablename+" WHERE mid = '"+mid+"'";
	stmt = con.createStatement();
	rs = stmt.executeQuery(query);
	if(rs.next()){ 

		Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
		Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
		Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
		Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("fumask")).iterator();

		while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()&&fileumask_iter.hasNext()){
			EnterInfoTable file = new EnterInfoTable();
			file.setFname((String)filename_iter.next());
			file.setFtype((String)filetype_iter.next());
			file.setFsize((String)filesize_iter.next());
			file.setFumask((String)fileumask_iter.next());
			
			file_list.add(file);
		}
	}

		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()



	/****************************************************************
	 * 구매요청번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getRequestNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String request_no	= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM pu_requested_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		request_no	= "REQ" + reg_year + reg_month + "-" + reg_serial;

		return request_no;
	}

	/****************************************************************
	 * 발주번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getOrderNo(String inout_type) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String order_no	= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM pu_order_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		order_no	= "P" + inout_type + reg_year + reg_month + "-" + reg_serial;

		//System.out.println(order_no);

		return order_no;
	}

	/****************************************************************
	 * 입고번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getEnterNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String enter_no		= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM pu_entered_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		enter_no	= "IN" + reg_year + reg_month + "-" + reg_serial;

		return enter_no;
	}

	/*******************************************************
	* 품목 요청 결재 정보(aid)저장하기
	*******************************************************/
	public void saveRequestAid(String aid,String request_no) throws Exception {
		Statement stmt = null;
		String query = "UPDATE pu_requested_info SET aid = '" + aid + "' WHERE request_no = '" + request_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************************************
	* 발주요청 결재 정보(aid)저장하기
	*******************************************************/
	public void saveOrderAid(String aid,String order_no) throws Exception {
		Statement stmt = null;
		String query = "UPDATE pu_order_info SET aid = '" + aid + "' WHERE order_no = '" + order_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************************************
	* 구매입고 결재 정보(aid)저장하기
	*******************************************************/
	public void saveEnterAid(String aid,String enter_no) throws Exception {
		Statement stmt = null;
		String query = "UPDATE pu_entered_info SET aid = '" + aid + "' WHERE enter_no = '" + enter_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/**********************************
	 * aid(전자결재번호) 값 가져오기
	 **********************************/
	public String getAid(String mode,String tablename,String no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String aid = "";
		String sql = "";

		if (mode.equals("request_app_print"))	{
			sql = "SELECT aid FROM "+tablename+" WHERE request_no = '" + no+"'";	
		}else if (mode.equals("order_app_print"))	{
			sql = "SELECT aid FROM "+tablename+" WHERE order_no = '" + no+"'";
		}else if (mode.equals("enter_app_print"))	{
			sql = "SELECT aid FROM "+tablename+" WHERE enter_no = '" + no+"'";
		}


		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();
		aid = rs.getString("aid");
		if (aid==null) aid = "";
		return aid;
	}//getAid()

	/*********************************************
	 * 선택된 품목에 대한 공장별 입고예정수량을 가져온다.
	 *********************************************/
	public String getInToQuantityByFactory(String factory_code,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String into_quantity	= "0";

		String sql = "SELECT SUM(enter_quantity) FROM pu_entered_item WHERE process_stat = 'S21' AND item_code = '" + item_code + "' AND factory_code = '" + factory_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		if(rs.getString(1) != null) into_quantity	= rs.getString(1);
		stmt.close();
		rs.close();
		
		return into_quantity;
	}

	/*********************************************
	 * 특정 구매요청건에 대해 중복 품목이 있는지 체크한다.
	 *********************************************/
	public int getSameRequestItemCount(String request_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		int cnt = 0;

		String sql = "SELECT COUNT(*) FROM pu_requested_item WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' ";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		
		return cnt;
	}
}		