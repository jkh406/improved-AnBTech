package com.anbtech.pu.business;

import com.anbtech.pu.entity.*;
import com.anbtech.pu.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class PurchaseMgrBO{

	private Connection con;

	public PurchaseMgrBO(Connection con){
		this.con = con;
	}

	/************************************
	 * 선택된 구매요청번호에 대한 정보를 가져온다.
	 ************************************/
	public RequestInfoTable getRequestInfoForm(String mode,String request_no,String item_code,String login_id,String request_type) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		RequestInfoTable table = new RequestInfoTable();
		
		//요청정보관련
		String mid					= "";
		String request_date			= "";
		String requester_div_code	= "";
		String requester_div_name	= "";
		String requester_id			= "";
		String requester_info		= "";
		String process_stat			= "S01";
		String request_total_mount	= "0";
		String project_code			= "";
		String project_name			= "";

		//요청품목관련
		String item_name			= "";
		String item_desc			= "";
		String request_unit			= "";
		String delivery_date		= "";
		String request_quantity		= "";
		String order_quantity		= "";
		String delivery_quantity	= "";
		String ftype				= "";
		String fname				= "";
		String fumask				= "";
		String fpath				= "";

		//  신규 등록일 경우 첫번째 실행 시 구매요청번호를 계산한다.
		if( mode.equals("request_info") && request_no == null){
			request_no = "자동생성";
			//요청시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
			request_date = vans.format(now);

			//등록자 정보
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requester_div_name	= userinfo.getDivision();
			requester_div_code	= userinfo.getAcCode();
			requester_info		= userinfo.getUserName();
			requester_id		= login_id;

			item_code			= "";
		}
		
		//  요청정보 수정 
		else if(mode.equals("modify_request_info") && request_no != null){
			table = purchaseDAO.getRequestInfo(request_no);

			request_date		= table.getRequestDate();
			requester_div_code	= table.getRequesterDivCode();
			requester_div_name	= table.getRequesterDivName();
			requester_id		= table.getRequesterId();
			requester_info		= table.getRequesterInfo();
			process_stat		= table.getProcessStat();
			request_type		= table.getRequestType();
			request_total_mount	= table.getRequestTotalMount();
			project_code		= table.getProjectCode();
			project_name		= table.getProjectName();

			item_code			= "";
		}

		//  요청정보 수정 
		else if((mode.equals("request_app_view")||mode.equals("request_app_print")||mode.equals("request_info_print")) && request_no != null){
			table = purchaseDAO.getRequestInfo(request_no);
			
			mid					= table.getMid();
			request_date		= table.getRequestDate();
			requester_div_code	= table.getRequesterDivCode();
			requester_div_name	= table.getRequesterDivName();
			requester_id		= table.getRequesterId();
			requester_info		= table.getRequesterInfo();
			process_stat		= table.getProcessStat();
			request_type		= table.getRequestType();
			request_total_mount	= table.getRequestTotalMount();
			project_code		= table.getProjectCode();
			project_name		= table.getProjectName();

			item_code			= "";
		}
		
		table.setMid(mid);
		table.setRequestNo(request_no);
		table.setItemCode(item_code);
		table.setRequestDate(request_date);
		table.setRequesterDivCode(requester_div_code);
		table.setRequesterDivName(requester_div_name);
		table.setRequesterId(requester_id);
		table.setRequesterInfo(requester_info);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setRequestUnit(request_unit);
		table.setDeliveryDate(delivery_date);
		table.setRequestQuantity(request_quantity);
		table.setProcessStat(process_stat);
		table.setRequestType(request_type);
		table.setProjectCode(project_code);
		table.setProjectName(project_name);
		table.setRequestTotalMount(request_total_mount);

		return table;
	}

	/************************************
	 * 선택된 구매요청번호 및 품목코드에 대한 정보를 가져온다.
	 ************************************/
	public RequestInfoTable getRequestItemForm(String mode,String request_no,String item_code,String login_id,String request_type) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		RequestInfoTable table = new RequestInfoTable();
		
		//요청정보관련
		String mid					= "";
		String request_date			= "";
		String requester_div_code	= "";
		String requester_div_name	= "";
		String requester_id			= "";
		String requester_info		= "";
		String process_stat			= "S01";
		String request_total_mount	= "0";
		
		//요청품목관련
		String item_name			= "";
		String item_desc			= "";
		String request_unit			= "EA";
		String delivery_date		= "";
		String request_quantity		= "1";
		String order_quantity		= "";
		String delivery_quantity	= "";
		String ftype				= "";
		String fname				= "";
		String fumask				= "";
		String fpath				= "";

		String supplyer_code		= "";
		String supplyer_name		= "";
		String supply_cost			= "0";
		String request_cost			= "0";

		//품목추가 등록 시
		if(mode.equals("request_item_add") && request_no != null){
			table = purchaseDAO.getRequestInfo(request_no);

			request_date			= table.getRequestDate();
			requester_div_code		= table.getRequesterDivCode();
			requester_div_name		= table.getRequesterDivName();
			requester_id			= table.getRequesterId();
			requester_info			= table.getRequesterInfo();
			request_total_mount		= purchaseDAO.getRequestTotalMount(request_no);
			request_type			= table.getRequestType();

			item_code				= "";
			process_stat			= purchaseDAO.getMaxStatForRequestItem(request_no);
		}

		//수정 모드일 경우
		else if(mode.equals("modify_request") && request_no != null && item_code != null){
			table = purchaseDAO.getRequestItemInfo(request_no,item_code);

			request_date			= table.getRequestDate();
			requester_div_code		= table.getRequesterDivCode();
			requester_div_name		= table.getRequesterDivName();
			requester_id			= table.getRequesterId();
			requester_info			= table.getRequesterInfo();
			request_total_mount		= purchaseDAO.getRequestTotalMount(request_no);
			request_type			= table.getRequestType();

			mid						= table.getMid();
			item_name				= table.getItemName();
			item_desc				= table.getItemDesc();
			request_unit			= table.getRequestUnit();
			delivery_date			= table.getDeliveryDate();
			request_quantity		= table.getRequestQuantity();
			order_quantity			= table.getOrderQuantity();

			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			supply_cost			= table.getSupplyCost();
			request_cost		= table.getRequestCost();

		}

		table.setRequestNo(request_no);
		table.setItemCode(item_code);
		table.setRequestDate(request_date);
		table.setRequesterDivCode(requester_div_code);
		table.setRequesterDivName(requester_div_name);
		table.setRequesterId(requester_id);
		table.setRequesterInfo(requester_info);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setRequestUnit(request_unit);
		table.setDeliveryDate(delivery_date);
		table.setRequestQuantity(request_quantity);
		table.setProcessStat(process_stat);
		table.setRequestType(request_type);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setSupplyCost(supply_cost);
		table.setRequestCost(request_cost);
		table.setRequestTotalMount(request_total_mount);
		table.setRequestType(request_type);

		
		return table;
	}


	/************************************
	 * 선택된 발주번호에 대한 정보를 가져온다.
	 ************************************/
	public OrderInfoTable getOrderInfoForm(String mode,String order_no,String item_code,String login_id) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		
		//발주정보관련
		String mid					= "";
		String order_type			= "";
		String process_stat			= "S05";
		String order_date			= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String supplyer_code		= "";
		String supplyer_name		= "";
		String order_total_mount	= "0";
		String monetary_unit		= "KRW";
		String exchange_rate		= "0";
		String vat_type				= "";
		String vat_rate				= "10";
		String vat_mount			= "0";
		String is_vat_contained		= "0";
		String approval_type		= "";
		String approval_period		= "";
		String payment_type			= "";
		String supplyer_info		= "";
		String supplyer_tel			= "";
		String other_info			= "";
		String inout_type			="D";

		//발주품목정보관련
		String item_name			= "";
		String item_desc			= "";
		String order_quantity		= "0";
		String order_unit			= "EA";
		String unit_cost			= "0";
		String order_cost			= "0";
		String delivery_date		= "";
		String request_no			= "";

		//신규 등록시
		if(mode.equals("order_info") && order_no == null){
			//발주번호
			order_no = "자동생성";

			//등록시간
			java.util.Date now = new java.util.Date();
			java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
			order_date = vans.format(now);

			//등록자 정보
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(login_id);
			requestor_div_name	= userinfo.getDivision();
			requestor_div_code	= userinfo.getAcCode();
			requestor_info		= userinfo.getUserName();
			requestor_id		= login_id;
			process_stat		= purchaseDAO.getMaxStatForOrderItem(order_no);
			item_code			= "";
		}
		
		//발주등록정보 수정 시
		else if(mode.equals("modify_order_info") && order_no != null){
			table = purchaseDAO.getOrderInfo(order_no);

			order_no			= table.getOrderNo();
			order_type			= table.getOrderType();
			process_stat		= table.getProcessStat();
			order_date			= table.getOrderDate();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			order_total_mount	= table.getOrderTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			exchange_rate		= table.getExchangeRate();
			vat_type			= table.getVatType();
			vat_rate			= table.getVatRate();
			vat_mount			= table.getVatMount();
			is_vat_contained	= table.getIsVatContained();
			approval_type		= table.getApprovalType();
			approval_period		= table.getApprovalPeriod();
			payment_type		= table.getPaymentType();
			supplyer_info		= table.getSupplyerInfo();
			supplyer_tel		= table.getSupplyerTel();
			other_info			= table.getOtherInfo();

			item_code			= "";
		}

		table.setOrderNo(order_no);
		table.setOrderType(order_type);
		table.setProcessStat(process_stat);
		table.setOrderDate(order_date);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setOrderTotalMount(order_total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setExchangeRate(exchange_rate);
		table.setVatType(vat_type);
		table.setVatRate(vat_rate);
		table.setVatMount(vat_mount);
		table.setIsVatContained(is_vat_contained);
		table.setApprovalType(approval_type);
		table.setApprovalPeriod(approval_period);
		table.setPaymentType(payment_type);
		table.setSupplyerInfo(supplyer_info);
		table.setSupplyerTel(supplyer_tel);
		table.setOtherInfo(other_info);
		table.setInOutType(inout_type);

		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setOrderQuantity(order_quantity);
		table.setOrderUnit(order_unit);
		table.setUnitCost(unit_cost);
		table.setOrderCost(order_cost);
		table.setDeliveryDate(delivery_date);
		table.setRequestNo(request_no);

		return table;
	}
	

	/************************************
	 * 선택된 발주번호 및 품목코드에 대한 정보를 가져온다.
	 ************************************/
	public OrderInfoTable getOrderItemForm(String mode,String order_no,String item_code,String request_no,String login_id) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		
		//발주정보관련
		String mid					= "";
		String order_type			= "";
		String process_stat			= "S05";
		String order_date			= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String supplyer_code		= "";
		String supplyer_name		= "";
		String order_total_mount	= "0";
		String monetary_unit		= "KRW";
		String exchange_rate		= "0";
		String vat_type				= "";
		String vat_rate				= "0";
		String vat_mount			= "0";
		String is_vat_contained		= "1";
		String approval_type		= "";
		String approval_period		= "";
		String payment_type			= "";
		String supplyer_info		= "";
		String supplyer_tel			= "";
		String other_info			= "";
		String inout_type			= "";

		//발주품목정보관련
		String item_name			= "";
		String item_desc			= "";
		String order_quantity		= "0";
		String order_unit			= "EA";
		String unit_cost			= "0";
		String order_cost			= "0";
		String delivery_date		= "";
	
		//품목추가 등록 시
		if(mode.equals("order_item_add") && order_no != null){
			table = purchaseDAO.getOrderInfo(order_no);

			order_no			= table.getOrderNo();
			order_type			= table.getOrderType();
			process_stat		= table.getProcessStat();
			order_date			= table.getOrderDate();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			order_total_mount	= table.getOrderTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			exchange_rate		= table.getExchangeRate();
			vat_type			= table.getVatType();
			vat_rate			= table.getVatRate();
			vat_mount			= table.getVatMount();
			is_vat_contained	= table.getIsVatContained();
			approval_type		= table.getApprovalType();
			approval_period		= table.getApprovalPeriod();
			payment_type		= table.getPaymentType();
			supplyer_info		= table.getSupplyerInfo();
			supplyer_tel		= table.getSupplyerTel();
			other_info			= table.getOtherInfo();

			item_code			= "";
			process_stat		= purchaseDAO.getMaxStatForOrderItem(order_no);
		}

		//수정 모드일 경우
		else if(mode.equals("modify_order") && order_no != null && item_code != null){
			table = purchaseDAO.getOrderItemInfo(order_no,item_code,request_no);

			order_no			= table.getOrderNo();
			order_type			= table.getOrderType();
			process_stat		= table.getProcessStat();
			order_date			= table.getOrderDate();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			order_total_mount	= table.getOrderTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			exchange_rate		= table.getExchangeRate();
			vat_type			= table.getVatType();
			vat_rate			= table.getVatRate();
			vat_mount			= table.getVatMount();
			is_vat_contained	= table.getIsVatContained();
			approval_type		= table.getApprovalType();
			approval_period		= table.getApprovalPeriod();
			payment_type		= table.getPaymentType();
			supplyer_info		= table.getSupplyerInfo();
			supplyer_tel		= table.getSupplyerTel();
			other_info			= table.getOtherInfo();

			item_code			= table.getItemCode();
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			delivery_date		= table.getDeliveryDate();
			order_quantity		= table.getOrderQuantity();
			order_unit			= table.getOrderUnit();
			unit_cost			= table.getUnitCost();
			order_cost			= table.getOrderCost();
			request_no			= table.getRequestNo();
		}

		//결재 정보 모드일 경우
		else if((mode.equals("order_app_view") ||mode.equals("order_app_print")) && order_no != null){
			table = purchaseDAO.getOrderItemInfo(order_no,request_no);

			order_no			= table.getOrderNo();
			order_type			= table.getOrderType();
			process_stat		= table.getProcessStat();
			order_date			= table.getOrderDate();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			order_total_mount	= table.getOrderTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			exchange_rate		= table.getExchangeRate();
			vat_type			= table.getVatType();
			vat_rate			= table.getVatRate();
			vat_mount			= table.getVatMount();
			is_vat_contained	= table.getIsVatContained();
			approval_type		= table.getApprovalType();
			approval_period		= table.getApprovalPeriod();
			payment_type		= table.getPaymentType();
			supplyer_info		= table.getSupplyerInfo();
			supplyer_tel		= table.getSupplyerTel();
			other_info			= table.getOtherInfo();
			inout_type			= table.getInOutType();
/*
			item_code			= table.getItemCode();
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			delivery_date		= table.getDeliveryDate();
			order_quantity		= table.getOrderQuantity();
			order_unit			= table.getOrderUnit();
			unit_cost			= table.getUnitCost();
			order_cost			= table.getOrderCost();
			request_no			= table.getRequestNo();*/
		}

		table.setOrderNo(order_no);
		table.setOrderType(order_type);
		table.setProcessStat(process_stat);
		table.setOrderDate(order_date);
		table.setRequestorDivCode(requestor_div_code);
		table.setRequestorDivName(requestor_div_name);
		table.setRequestorId(requestor_id);
		table.setRequestorInfo(requestor_info);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setOrderTotalMount(order_total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setExchangeRate(exchange_rate);
		table.setVatType(vat_type);
		table.setVatRate(vat_rate);
		table.setVatMount(vat_mount);
		table.setIsVatContained(is_vat_contained);
		table.setApprovalType(approval_type);
		table.setApprovalPeriod(approval_period);
		table.setPaymentType(payment_type);
		table.setSupplyerInfo(supplyer_info);
		table.setSupplyerTel(supplyer_tel);
		table.setOtherInfo(other_info);
		table.setInOutType(inout_type);

		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setOrderQuantity(order_quantity);
		table.setOrderUnit(order_unit);
		table.setUnitCost(unit_cost);
		table.setOrderCost(order_cost);
		table.setDeliveryDate(delivery_date);
		table.setRequestNo(request_no);

		return table;
	}

	/************************************
	 * 선택된 입고번호에 대한 정보를 가져온다.
	 ************************************/
	public EnterInfoTable getEnterInfoForm(String mode,String enter_no,String item_code,String login_id) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		EnterInfoTable table = new EnterInfoTable();
		
		//입고정보관련
		String mid					= "";
		String process_stat			= "S21";
		String enter_date			= "";
		String enter_total_mount	= "0";
		String monetary_unit		= "KRW";
		String enter_type			= "";
		String supplyer_code		= "";
		String supplyer_name		= "";
		String requestor_div_code	= "";
		String requestor_div_name	= "";
		String requestor_id			= "";
		String requestor_info		= "";
		String filelink				= "";

		//입고정보 수정화면
		if(mode.equals("modify_enter_info") && enter_no != null){
			table = purchaseDAO.getEnterInfo(enter_no);

			mid					= table.getMid();
			process_stat		= table.getProcessStat();
			enter_date			= table.getEnterDate();
			enter_total_mount	= table.getEnterTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			enter_type			= table.getEnterType();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			requestor_div_code	= table.getRequestorDivCode();
			requestor_div_name	= table.getRequestorDivName();
			requestor_id		= table.getRequestorId();
			requestor_info		= table.getRequestorInfo();

			table = new EnterInfoTable();
			Iterator file_iter = purchaseDAO.getEnteredFile_list("pu_entered_info", mid).iterator();

			int j = 1;
			while(file_iter.hasNext()){
				EnterInfoTable file = (EnterInfoTable)file_iter.next();
				filelink += "<a href='PurchaseMgrServlet?tablename=pu_entered_info&upload_folder=receipt&mode=enterfile_download&mid="+mid+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../pu/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0>"+file.getFname()+"</a><br>";
				j++;				
			}
		}

		table.setMid(mid);
		table.setEnterNo(enter_no);
		table.setProcessStat(process_stat);
		table.setEnterDate(enter_date);
		table.setEnterTotalMount(enter_total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setEnterType(enter_type);
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
	 * 선택된 입고번호 및 품목코드에 대한 정보를 가져온다.
	 ************************************/
	public EnterInfoTable getEnterItemForm(String mode,String enter_no,String item_code,String order_no,String login_id) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		EnterInfoTable table = new EnterInfoTable();
		
		//입고정보관련
		String mid					= "";
		String process_stat			= "S21";
		String enter_date			= "";
		String enter_total_mount	= "0";
		String monetary_unit		= "";
		String enter_type			= "";
		String supplyer_code		= "";
		String supplyer_name		= "";
		String filelink				= "&nbsp;";

		//입고품목관련
		String item_name			= "";
		String item_desc			= "";
		String enter_quantity		= "";
		String enter_unit			= "EA";
		String enter_cost			= "0";
		String unit_cost			= "0";
		String factory_code			= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";
		String request_no			= "";

		//품목추가 입고 시
		if(mode.equals("enter_item_add") && enter_no != null){
			table = purchaseDAO.getEnterInfo(enter_no);

			process_stat		= table.getProcessStat();
			enter_date			= table.getEnterDate();
			enter_total_mount	= table.getEnterTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			enter_type			= table.getEnterType();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			filelink			= table.getFileLink();

			item_code			= "";

			process_stat		= purchaseDAO.getMaxStatForEnterItem(enter_no);
		}

		//수정 모드일 경우
		else if(mode.equals("modify_enter") && enter_no != null && item_code != null){
			table = purchaseDAO.getEnterItemInfo(enter_no,item_code,order_no);

			process_stat		= table.getProcessStat();
			enter_date			= table.getEnterDate();
			enter_total_mount	= table.getEnterTotalMount();
			monetary_unit		= table.getMonetaryUnit();
			enter_type			= table.getEnterType();
			supplyer_code		= table.getSupplyerCode();
			supplyer_name		= table.getSupplyerName();
			filelink			= table.getFileLink();
			
			item_name			= table.getItemName();
			item_desc			= table.getItemDesc();
			enter_quantity		= table.getEnterQuantity();
			enter_unit			= table.getEnterUnit();
			enter_cost			= table.getEnterCost();
			unit_cost			= table.getUnitCost();
			factory_code		= table.getFactoryCode();
			factory_name		= table.getFactoryName();
			warehouse_code		= table.getWarehouseCode();
			warehouse_name		= table.getWarehouseName();
			order_no			= table.getOrderNo();
			request_no			= table.getRequestNo();
		}

		table.setEnterNo(enter_no);
		table.setProcessStat(process_stat);
		table.setEnterDate(enter_date);
		table.setEnterTotalMount(enter_total_mount);
		table.setMonetaryUnit(monetary_unit);
		table.setEnterType(enter_type);
		table.setSupplyerCode(supplyer_code);
		table.setSupplyerName(supplyer_name);
		table.setFileLink(filelink);

		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setEnterQuantity(enter_quantity);
		table.setEnterUnit(enter_unit);
		table.setEnterCost(enter_cost);
		table.setUnitCost(unit_cost);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setOrderNo(order_no);
		table.setRequestNo(request_no);

		return table;
	}

	/***********************************************
	 * 구매요청집계화면에서 품목선택 후 발주등록 화면으로 분기했을 때
     * items 에 넘어온 품목들을 각각 db에 저장한다.
     * items == "요청번호|품목번호,요청번호|품목번호,...." 식임.
	 ***********************************************/
	public void saveOrderItem(String mode,String items,String order_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		RequestInfoTable table = new RequestInfoTable();

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
			String request_no	= item[i][0]; //요청번호
			String item_code	= item[i][1]; //품목코드

			table = purchaseDAO.getRequestItemInfo(request_no,item_code);
			
			//pu_order_item 테이블에 품목정보 기록
			//saveOrderItemInfo(String order_no,String request_no,String item_code,String item_name,String item_desc,String order_quantity,String order_unit,String delivery_date,String unit_cost,String order_cost,String delivery_quantity,String process_stat)
			int distance = Integer.parseInt(table.getRequestQuantity()) - Integer.parseInt(table.getOrderQuantity());
			purchaseDAO.saveOrderItemInfo(order_no,request_no,item_code,table.getItemName(),table.getItemDesc(),Integer.toString(distance),table.getRequestUnit(),table.getDeliveryDate(),table.getSupplyCost(),table.getRequestCost(),"0","S05");

			//pu_requested_item 테이블내 해당 품목의 상태코드와 발주수량을 업데이트
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S05");
			purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"order_quantity",Integer.toString(distance));
		}

	}

	/***********************************************
	 * 선택된 발주품목에 메모 내용을 저장한다.
     * items == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
	 ***********************************************/
	public void saveMemoForOrderItem(String mode,String items,String memo) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);

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
			String request_no	= item[i][0]; //요청번호
			String order_no		= item[i][1]; //발주번호
			String item_code	= item[i][2]; //품목코드

			purchaseDAO.updateOrderItemInfo(order_no,request_no,item_code,memo);
		}
	}

	/***********************************************
	 * 발주정보집계화면에서 품목선택 후 구매입고 화면으로 분기했을 때
     * items 에 넘어온 품목들을 각각 db에 저장한다.
     * items == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
	 ***********************************************/
	public void saveEnterItem(String mode,String items,String enter_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
		RequestInfoTable table2 = new RequestInfoTable();
		com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

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
			String request_no	= item[i][0]; //요청번호
			String order_no		= item[i][1]; //발주번호
			String item_code	= item[i][2]; //품목코드

			table = purchaseDAO.getOrderItemInfo(order_no,item_code,request_no);
			table2 = purchaseDAO.getRequestInfo(request_no);

			int distance = Integer.parseInt(table.getOrderQuantity()) - Integer.parseInt(table.getDeliveryQuantity());
			purchaseDAO.saveEnterItemInfo(enter_no,order_no,request_no,table.getItemCode(),table.getItemName(),table.getItemDesc(),Integer.toString(distance),table.getOrderUnit(),table.getOrderCost(),table.getUnitCost(),table2.getFactoryCode(),table2.getFactoryName(),"","","S18");

			//pu_requested_item 테이블내 해당 품목의 상태코드와 입고수량을 업데이트
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S18");
			purchaseDAO.updateQuantity("pu_requested_item","request_no",request_no,item_code,"delivery_quantity",Integer.toString(distance));

			//pu_order_item 테이블내 해당 품목의 상태코드와 입고수량을 업데이트
			purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,item_code,"S18");
			purchaseDAO.updateQuantity("pu_order_item","order_no",order_no,item_code,"delivery_quantity",Integer.toString(distance));

		}
	}


	/***********************************************
	 * 선택된 요청번호에 존재하는 요청폼목을 일괄 삭제한다.
	 ***********************************************/
	public void deleteAllRequestItems(String request_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		RequestInfoTable table = new RequestInfoTable();
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getRequestItemList(request_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (RequestInfoTable)item_iter.next();
			purchaseDAO.deleteRequestItem(request_no,table.getItemCode());
		}
	}

	/***********************************************
	 * 선택된 발주번호 존재하는 발주품목을 일괄 삭제한다.
	 ***********************************************/
	public void deleteAllOrderItems(String order_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (OrderInfoTable)item_iter.next();
			purchaseDAO.deleteOrderItem(order_no,table.getItemCode());

			//pu_requested_item 테이블의 상태코드 및 발주수량 업데이트
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"S03");
			purchaseDAO.updateQuantity("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"order_quantity","-"+table.getOrderQuantity());
		}
	}

	/***********************************************
	 * 선택된 입고번호 존재하는 입고품목을 일괄 삭제한다.
	 ***********************************************/
	public void deleteAllEnterItems(String enter_no) throws Exception{
		PurchaseMgrDAO purchaseDAO				= new PurchaseMgrDAO(con);
		EnterInfoTable table					= new EnterInfoTable();
		com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
		com.anbtech.qc.db.QualityCtrlDAO qcDAO	= new com.anbtech.qc.db.QualityCtrlDAO(con);
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getEnterItemList(enter_no);
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()){
			table = (EnterInfoTable)item_iter.next();
			purchaseDAO.deleteEnterItem(enter_no,table.getItemCode());

			//pu_requested_item 테이블의 상태코드 및 입고수량 업데이트
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"S13");
			purchaseDAO.updateQuantity("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"delivery_quantity","-"+table.getEnterQuantity());

			//pu_order_item 테이블의 상태코드 및 입고수량 업데이트
			purchaseDAO.updateProcessStat("pu_order_item","order_no",table.getOrderNo(),table.getItemCode(),"S13");
			purchaseDAO.updateQuantity("pu_order_item","order_no",table.getOrderNo(),table.getItemCode(),"delivery_quantity","-"+table.getEnterQuantity());

			//수불현황 기록을 삭제한다.
//			stBO.deleteInOutInfoForPurchasedInputItem(enter_no,table.getRequestNo(),table.getItemCode());

			//품질검사 의뢰정보를 삭제한다.
//			qcDAO.deleteInspectionInfo(table.getItemCode(),enter_no);
		}
	}

	/***********************************************
	 * 공급업체 지정시 품목별 공급단가 정보를 업데이트
	 ***********************************************/
	public void updateOrderItemUnitCost(String mode,String order_no,String supplyer_code) throws Exception{
		com.anbtech.pu.db.PurchaseConfigMgrDAO purchaseconfgDAO = new com.anbtech.pu.db.PurchaseConfigMgrDAO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (OrderInfoTable)item_iter.next();
			String item_code = table.getItemCode();				//품목코드
			int order_quantity = Integer.parseInt(table.getOrderQuantity());	//발주수량
			
			
//			double unit_cost = 2500;							//발주단가
			double unit_cost = purchaseconfgDAO.getSupplyerUnitCost(item_code,supplyer_code);
			double order_cost = order_quantity * unit_cost;		//발주금액

			purchaseDAO.updateOrderItemInfo(order_no,item_code,table.getItemName(),table.getItemDesc(),Integer.toString(order_quantity),table.getOrderUnit(),Double.toString(unit_cost),"",Double.toString(order_cost),table.getDeliveryDate(),table.getDeliveryQuantity(),table.getRequestNo());

		}
	}

	/***********************************************
	 * 구매요청정보 조회시의 쿼리문 검색조건을 만든다.
	 ***********************************************/
	public String getWhereForRequestInfoList(String mode,String searchword,String searchscope,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("request_no")){				//구매요청번호
				where_sea += "( request_no LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("requester_info")){		//요청자이름
				where_sea += "( requester_info LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("requester_div_name")){	//요청자부서명
				where_sea += "( requester_div_name LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("request_date")){	//구매요청일자
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
			}else if(searchscope.equals("process_stat")){	//진행상태
				where_sea += "( process_stat = '" +  searchword + "' )";
			}
	
			where = " WHERE requester_id = '" + login_id + "' AND " + where_sea;
		}else{
			where = " WHERE requester_id = '" + login_id + "'";
		}
//System.out.println(where);
		return where;
	}

	/***********************************************
	 * 발주정보 조회시의 쿼리문 검색조건을 만든다.
	 ***********************************************/
	public String getWhereForOrderInfoList(String mode,String searchword,String searchscope,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("order_no")){	//발주번호
				where_sea += "( order_no LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("supplyer_name")){	//발주번호
				where_sea += "( supplyer_name LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("order_date")){	//발주일자
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(order_date >= '" + s_date + "' and order_date <= '" + e_date +"')";
			}else if(searchscope.equals("process_stat")){	//진행상태
				where_sea += "( process_stat = '" +  searchword + "' )";
			}
	
			where = " WHERE " + where_sea;
		}

		return where;
	}

	/***********************************************
	 * 구매입고정보 조회시의 쿼리문 검색조건을 만든다.
	 ***********************************************/
	public String getWhereForEnterInfoList(String mode,String searchword,String searchscope,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		//검색어에 따른 검색조건
		if (searchword.length() > 0){
			if(searchscope.equals("enter_no")){
				where_sea += "( enter_no LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("supplyer_name")){
				where_sea += "( supplyer_name LIKE '%" +  searchword + "%' )";
			}else if(searchscope.equals("enter_date")){
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(enter_date >= '" + s_date + "' and enter_date <= '" + e_date +"')";
			}else if(searchscope.equals("process_stat")){
				where_sea += "( process_stat = '" +  searchword + "' )";
			}
	
			where = " WHERE " + where_sea;
		}

		return where;
	}

	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.(구매요청정보)
	 *****************************************************************/
	public String getWhereForRequestItemList(String mode,String where_str,String login_id) throws Exception{

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
				//request_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
			}else if(search[0][0].equals("delivery_date")){
				where_sea += "(delivery_date <= '" + search[0][1] + "')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("request_date")){
					//request_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (request_date >= '" + s_date + "' and request_date <= '" + e_date +"')";
				}else if(search[i][0].equals("delivery_date")){
					where_sea += "(delivery_date <= '" + search[i][1] + "')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}
			where = " WHERE " + where_sea;

			//구매요청집계화면에서는 구매요청승인을 득한(상태코드 S03 이상) 품목들 중,
			//발주된 수량이 요청수량보다 작고,상태코드가 S06(일부발주)
			//이 아닌 품목만 나타나게 한다. 발수수량 < 요청수량 이더라도 S06(일부만 발주하고
			//끝내는 경우)는 리스트에 출력하지 않는다.
			if(mode.equals("requested_list")) where += " AND (process_stat >= 'S03') AND (request_quantity > order_quantity) AND (process_stat != 'S06')";
		}else{
			if(mode.equals("requested_list")) where += " WHERE (process_stat >= 'S03') AND (request_quantity > order_quantity) AND (process_stat != 'S06')";
		}

		return where;
	}

	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.(발주정보)
	 *****************************************************************/
	public String getWhereForOrderItemList(String mode,String where_str,String login_id) throws Exception{

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
			
			if(search[0][0].equals("order_date")){
				//order_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(order_date >= '" + s_date + "' and order_date <= '" + e_date +"')";
			}else if(search[0][0].equals("delivery_date")){
				where_sea += "(delivery_date <= '" + search[0][1] + "')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("order_date")){
					//order_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (order_date >= '" + s_date + "' and order_date <= '" + e_date +"')";
				}else if(search[i][0].equals("delivery_date")){
					where_sea += "(delivery_date <= '" + search[i][1] + "')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}

			where = " WHERE " + where_sea;

			//발주정보집계화면에서는 발주승인을 득한(상태코드 S13 이상) 품목들 중,
			//입고된 수량이 발수수량보다 작고,상태코드가 S22(일부입고)
			//이 아닌 품목만 나타나게 한다. 입고수량 < 발주수량 이더라도 S22(일부만 입고하고
			//끝내는 경우)는 리스트에 출력하지 않는다.
			where += " AND (process_stat >= 'S13') AND (order_quantity > delivery_quantity) AND (process_stat != 'S22')";
		}else{
			where += " WHERE (process_stat >= 'S13') AND (order_quantity > delivery_quantity) AND (process_stat != 'S22')";
		}

		return where;
	}


	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.(입고정보)
	 *****************************************************************/
	public String getWhereForEnterItemList(String mode,String where_str,String login_id) throws Exception{

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
			
			if(search[0][0].equals("enter_date")){
				//enter_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
				String s_date = search[0][1].substring(0,4) + "-" + search[0][1].substring(4,6) + "-" + search[0][1].substring(6,8);
				String e_date = search[0][1].substring(8,12) + "-" + search[0][1].substring(12,14) + "-" + search[0][1].substring(14,16);
				where_sea += "(enter_date >= '" + s_date + "' and enter_date <= '" + e_date +"')";
			}else{
				where_sea += "(" + search[0][0] + " LIKE '%" + search[0][1] + "%') ";
			}

			for(int i = 1; i< scope_count; i++){
				if(search[i][0].equals("enter_date")){
					//enter_date = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.

					String s_date = search[i][1].substring(0,4) + "-" + search[i][1].substring(4,6) + "-" + search[i][1].substring(6,8);
					String e_date = search[i][1].substring(8,12) + "-" + search[i][1].substring(12,14) + "-" + search[i][1].substring(14,16);
					where_sea += "AND (enter_date >= '" + s_date + "' and enter_date <= '" + e_date +"')";
				}else{
					where_sea += "AND (" + search[i][0] + " LIKE '%" + search[i][1] + "%') ";
				}
			}
			where = " WHERE " + where_sea;
		}

		return where;
	}

	/*****************************************************************
	 * 선택된 발주건을 결재상신처리한다.
	 *****************************************************************/
	public void makeAppReviewStat(String order_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (OrderInfoTable)item_iter.next();

			//pu_order_item 테이블의 품목 상태코드를 결재상신(S09)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,table.getItemCode(),"S09");

			//pu_requested_item 테이블의 품목 상태코드를 결재상신(S09)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"S09");
		}
	}


	/*****************************************************************
	 * 선택된 발주건을 결재반려처리한다.
	 *****************************************************************/
	public void makeAppReturnStat(String order_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		ArrayList item_list = new ArrayList();

		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (OrderInfoTable)item_iter.next();

			//pu_order_item 테이블의 품목 상태코드를 발주등록(S05)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,table.getItemCode(),"S05");

			//pu_requested_item 테이블의 품목 상태코드를 발주등록(S05)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"S05");
		}
	}

	/*****************************************************************
	 * 선택된 발주건을 결재승인처리한다.
	 *****************************************************************/
	public void makeAppCommitStat(String order_no,String aid) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		ArrayList item_list = new ArrayList();

		//전자결재정보 가져와서 저장
		appDAO.getAppInfoAndSave("pu_approval_info",aid);

		//선택된 발주건의 aid 값을 업데이트한다.
		purchaseDAO.updateAid(order_no,aid);
		
		//품목상태코드 변경
		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			table = (OrderInfoTable)item_iter.next();

			//pu_order_item 테이블의 품목 상태코드를 결재승인(S13)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,table.getItemCode(),"S13");

			//pu_requested_item 테이블의 품목 상태코드를 결재승인(S13)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",table.getRequestNo(),table.getItemCode(),"S13");
		}
	}

	/*****************************************************************
	 * 품목의 발주희망업체 자동배정 실행하기
	 *****************************************************************/
	public void updateRequestItemInfoBySupplyInfo(String request_no,String assign_rule) throws Exception{
		PurchaseMgrDAO purchaseDAO		= new PurchaseMgrDAO(con);
		PurchaseConfigMgrDAO purchaseconfigDAO	= new PurchaseConfigMgrDAO(con);
		RequestInfoTable item			= new RequestInfoTable();
		ItemSupplyInfoTable supplyer	= new ItemSupplyInfoTable();
		ArrayList item_list				= new ArrayList();
		ArrayList supplyer_list			= new ArrayList();

		//선택된 요청번호에 딸린 요청품목리스트 가져오기
		item_list = purchaseDAO.getRequestItemList(request_no);
		Iterator item_iter = item_list.iterator();
	
		while(item_iter.hasNext()){
			item = (RequestInfoTable)item_iter.next();
			String item_code		= item.getItemCode();
			String supplyer_code	= item.getSupplyerCode();
			String supplyer_name	= item.getSupplyerName();
			String unit_cost		= item.getSupplyCost();
			String order_weight		= "";
			String request_quantity	= item.getRequestQuantity();

			
			//선택된 품목을 공급하는 공급업체리스트 가져오기
			supplyer_list = purchaseconfigDAO.getItemSupplyInfoList(item_code);
			Iterator supplyer_iter = supplyer_list.iterator();

			//첫번째 공급업체값을 세팅한다.
			if(supplyer_iter.hasNext()){
				supplyer = (ItemSupplyInfoTable)supplyer_iter.next();
				supplyer_code = supplyer.getSupplyerCode();
				supplyer_name = supplyer.getSupplyerName();
// 희망업체자동 배정시 발주단가 대신 구매요청단가를 자동으로 올라오게 변경함. (2004-09-10)
//				unit_cost	  = supplyer.getSupplyUnitCost();
				unit_cost	  = supplyer.getRequestUnitCost();
				order_weight  = supplyer.getOrderWeight();
			}

			while(supplyer_iter.hasNext()){
				supplyer = (ItemSupplyInfoTable)supplyer_iter.next();

				//주공급업체이면 변수값 설정하고 반복문 중지
				if(supplyer.getIsMainSupplyer().equals("y")){
					supplyer_code = supplyer.getSupplyerCode();
					supplyer_name = supplyer.getSupplyerName();
//					unit_cost	  = supplyer.getSupplyUnitCost();
					unit_cost	  = supplyer.getRequestUnitCost();
					break;
				}

				//자동배정기준이 발주배정가중치인 경우
				if(assign_rule.equals("order_weight") && Integer.parseInt(order_weight) < Integer.parseInt(supplyer.getOrderWeight())){
					supplyer_code = supplyer.getSupplyerCode();
					supplyer_name = supplyer.getSupplyerName();
//					unit_cost	  = supplyer.getSupplyUnitCost();
					unit_cost	  = supplyer.getRequestUnitCost();
					order_weight  = supplyer.getOrderWeight();				
				}

				//자동배정기준이 공급단가인 경우
				if(assign_rule.equals("unit_cost") && Integer.parseInt(unit_cost) > Integer.parseInt(supplyer.getSupplyUnitCost())){
					supplyer_code = supplyer.getSupplyerCode();
					supplyer_name = supplyer.getSupplyerName();
//					unit_cost	  = supplyer.getSupplyUnitCost();
					unit_cost	  = supplyer.getRequestUnitCost();
					order_weight  = supplyer.getOrderWeight();				
				}
			}

			//설정된 공급업체코드,공급업체명,공급단가값을 DB로 업데이트한다.
			//진행상태코드가 구매요청(S01)인 경우만 업데이트하도록 한다.
			//request_cost 값 계산
			if(item.getProcessStat().equals("S01")){
				String request_cost = Double.toString(Math.round(Double.parseDouble(unit_cost)*Integer.parseInt(request_quantity)));
				purchaseDAO.updateRequestItemInfo(request_no,item_code,item.getItemName(),item.getItemDesc(),item.getRequestUnit(),item.getDeliveryDate(),request_quantity,supplyer_code,supplyer_name,unit_cost,request_cost);
			}
		}
	}


	/******************************************
	 * 첨부파일 업로딩 처리
	 ******************************************/
	public RequestInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

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
		com.anbtech.pu.entity.RequestInfoTable file = new com.anbtech.pu.entity.RequestInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFpath(filepath);
		file.setFumask(fileumask);
		//file.setDid(did);

		return file;
	} //getFile_frommulti()

	/******************************************
	 * 첨부파일 업로딩 처리
	 ******************************************/
	public EnterInfoTable getEnterFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

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
		com.anbtech.pu.entity.EnterInfoTable file = new com.anbtech.pu.entity.EnterInfoTable();
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
	public RequestInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

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

			RequestInfoTable file = new RequestInfoTable();
			if(file_iter.hasNext()) file = (RequestInfoTable)file_iter.next();

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
		RequestInfoTable file = new RequestInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);
		file.setDid(did);

		return file;
	}

	/******************************************
	 * 첨부파일 수정 처리
	 ******************************************/
	public EnterInfoTable getEnterFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

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

			EnterInfoTable file = new EnterInfoTable();
			if(file_iter.hasNext()) file = (EnterInfoTable)file_iter.next();

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
		EnterInfoTable file = new EnterInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);
		file.setDid(did);

		return file;
	}

	/**************************************************
	 * 첨부파일 다운로드
	 **************************************************/
	public RequestInfoTable getFile_fordown(String tablename,String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		Iterator file_iter = purchaseDAO.getFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",fileumask="";
		int i = 1;
		while (file_iter.hasNext()){
			RequestInfoTable file = (RequestInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFname();
				filetype = file.getFtype();
				filesize = file.getFsize();
				fileumask = file.getFumask();
			}
			i++;
		}
		RequestInfoTable file = new RequestInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);

		return file;
	}


	/**************************************************
	 * 첨부파일 다운로드(구매입고)
	 **************************************************/
	public EnterInfoTable getFile_Enterfordown(String tablename,String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		Iterator file_iter = purchaseDAO.getEnteredFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",fileumask="";
		int i = 1;
		while (file_iter.hasNext()){
			EnterInfoTable file = (EnterInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFname();
				filetype = file.getFtype();
				filesize = file.getFsize();
				fileumask = file.getFumask();
			}
			i++;
		}
		EnterInfoTable file = new EnterInfoTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFumask(fileumask);


		return file;
	}

	/**************************************************
	 * 품목 구매 요청 결재 처리 
	 * parameter : 처리모드, 결재번호, (품목구매)요청번호
	 *************************************************/
	public void puRequestAppInfoProcess(String mode, String aid,String request_no) throws Exception {
		PurchaseMgrBO purchaseBO = new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
		com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);

		//상신처리
		//pu_requested_item 테이블의 품목 상태코드를 결재상신(S02)으로 변경한다.
		if (mode.equals("app_req"))	{
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,"S02");

		
		//승인처리
		} else if (mode.equals("commit_req"))	{
			//1.결재정보저장-(TABLE: pu_approval_info)
			appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
			//2.aid(결재번호 저장- TABLE: pu_requested_info)		
			purchaseDAO.saveRequestAid(aid,request_no);
			//3.상태코드변경(TABLE: pu_requested_item)
			//pu_requested_item 테이블의 품목 상태코드를 결재완료(S03)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,"S03");
			//4.과제별 재료비를 업데이트한다.
			updateBudgetByPjt(request_no);


		//승인반려처리
		} else if (mode.equals("reject_req"))	{
			//1.결재정보저장-(TABLE: pu_approval_info)
			appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
			//2.aid(결재번호 저장- TABLE: pu_requested_info)		
			purchaseDAO.saveRequestAid(aid,request_no);
			//3.상태코드변경(TABLE: pu_requested_item)
			//pu_requested_item 테이블의 품목 상태코드를 결재완료(S01)으로 변경한다.
			purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,"S01");
		}
	}

	/**************************************************
	 * 발주 요청 결재 처리 
	 * parameter : 처리모드, 결재번호, 발주요청번호
	 *************************************************/
	public void puOrderAppInfoProcess(String mode, String aid,String order_no) throws Exception {
		PurchaseMgrBO purchaseBO = new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table	= new OrderInfoTable();
		com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
		com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		
		ArrayList item_list = new ArrayList();
		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator table_iter = item_list.iterator();
		while(table_iter.hasNext()){
			table = (OrderInfoTable)table_iter.next();
			String request_no = table.getRequestNo();
			String item_code  = table.getItemCode();

			//상신처리
			if (mode.equals("app_order"))	{
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S09");
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S09");
			//승인처리
			} else if (mode.equals("commit_order"))	{
				//1.결재정보저장-(TABLE: pu_approval_info)
				appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
				//2.aid(결재번호 저장- TABLE: pu_order_info)		
				purchaseDAO.saveOrderAid(aid,order_no);
				//3.pu_order_item 테이블의 발주품목 상태코드를 결재완료(S13)으로 변경한다.
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S13");
				//4.pu_requested_item 테이블의 요청품목 상태코드를 결재완료(S13)으로 변경한다.
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S13");

			//반려처리
			} else if (mode.equals("reject_order"))	{
				//1.결재정보저장-(TABLE: pu_approval_info)
				appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
				//2.aid(결재번호 저장- TABLE: pu_requested_info)		
				purchaseDAO.saveRequestAid(aid,order_no);
				//3.pu_order_item 테이블의 발주품목 상태코드를 발주등록(S05)으로 변경한다.
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S05");
				//4.pu_requested_item 테이블의 요청품목 상태코드를 발주등록(S05)으로 변경한다.
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S05");
			}
		}//end while
	}

	/**************************************************
	 * 구매입고 결재 처리 
	 * parameter : 처리모드, 결재번호, 발주요청번호
	 *************************************************/
	public void puEnterAppInfoProcess(String mode, String aid,String enter_no) throws Exception {
		PurchaseMgrBO purchaseBO	= new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		EnterInfoTable table		= new EnterInfoTable();
		com.anbtech.admin.entity.ApprovalInfoTable appTable = new com.anbtech.admin.entity.ApprovalInfoTable();
		com.anbtech.admin.db.ApprovalInfoMgrDAO appMgrDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		
		ArrayList item_list = new ArrayList();
		item_list = purchaseDAO.getEnterItemList(enter_no);
		Iterator table_iter = item_list.iterator();
		while(table_iter.hasNext()){
			table = (EnterInfoTable)table_iter.next();
			String order_no	  = table.getOrderNo();
			String request_no = table.getRequestNo();
			String item_code  = table.getItemCode();

			//상신처리
			if (mode.equals("app_enter"))	{
				purchaseDAO.updateProcessStat("pu_entered_item","enter_no",enter_no,"S19");
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S19");
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S19");

			//반려처리
			} else if (mode.equals("reject_enter"))	{
				//1.결재정보저장-(TABLE: pu_approval_info)
				appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
				//2.aid(결재번호 저장- TABLE: pu_requested_info)		
				purchaseDAO.saveEnterAid(aid,enter_no);
				//3.상태코드 변경
				purchaseDAO.updateProcessStat("pu_entered_item","enter_no",enter_no,"S18");
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S18");
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S18");

			//승인처리
			} else if (mode.equals("commit_enter"))	{
				//1.결재정보저장-(TABLE: pu_approval_info)
				appMgrDAO.getAppInfoAndSave("pu_approval_info",aid);
				//2.aid(결재번호 저장- TABLE: pu_order_info)		
				purchaseDAO.saveEnterAid(aid,enter_no);
				//3.상태코드 변경
				purchaseDAO.updateProcessStat("pu_entered_item","enter_no",enter_no,"S21");
				purchaseDAO.updateProcessStat("pu_order_item","order_no",order_no,"S21");
				purchaseDAO.updateProcessStat("pu_requested_item","request_no",request_no,item_code,"S21");

				//4.타모듈과 연동
				//품질검사의뢰 정보를 입력하기 위해 검사의뢰번호를 계산해 둔다.
				//for문 안에서는 생성할 수 없어서...
				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				String req_inspect_no = qcDAO.getRequestNo("TST");
				saveEnterItem(mode,enter_no,order_no,request_no,item_code,req_inspect_no);

			}
		}//end while
	}

	/***********************************************
	 * 결재승인이 완료되는 시점에서 입고처리를 한다.
	 ***********************************************/
	public void saveEnterItem(String mode,String enter_no,String order_no,String request_no,String item_code,String req_inspect_no) throws Exception{
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		com.anbtech.st.business.StockMgrBO stBO = new com.anbtech.st.business.StockMgrBO(con);
		RequestInfoTable table2 = new RequestInfoTable();
		com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);

		table = purchaseDAO.getOrderItemInfo(order_no,item_code,request_no);
		table2 = purchaseDAO.getRequestInfo(request_no);

		//////////////////////////////////////////////////////////////
		/////// 여기서부터 재고관리 및 품질관리 모듈과 연동되는 부분임 ///////////
		//////////////////////////////////////////////////////////////

		//요청구분에 따라 재고관리 모듈과의 연동을 고려한다. MRP,ROP에 의한 입고만 재고관리에 영향을
		//미친다.
		if(table2.getRequestType().equals("MRP") || table2.getRequestType().equals("ROP")){

			//수불현황 기록 및 재고DB 업데이트
			stBO.updateInOutInfoForPurchasedInputItem("ADD",item_code,table.getUnitCost(),table.getOrderQuantity(),enter_no,table2.getFactoryCode(),table2.getFactoryName(),"","");

			//품질검사 의뢰정보를 등록한다.
			qcDAO.saveInspectionInfo(req_inspect_no,item_code,table.getItemName(),table.getItemDesc(),table.getSupplyerCode(),table.getSupplyerName(),enter_no,table.getOrderQuantity(),table.getRequestorDivCode(),table.getRequestorDivName(),table.getRequestorId(),table.getRequestorInfo(),table2.getFactoryCode(),table2.getFactoryName(),"S01");

		}
	}

	/**************************************************
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String tablename, String mid, String filename, String filetype, String filesize, String fumask) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"', fsize='"+filesize+"', fumask='"+fumask+"'";
		String where = " WHERE mid='"+mid+"'";

		com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
		purchaseDAO.updTable(tablename, set, where);
	}

	/**************************************************
	 * 구매요청건에 대한 결재승인시 해당 과제의 재료비를 업데이트
	 **************************************************/
	public void updateBudgetByPjt(String request_no) throws Exception{
		com.anbtech.psm.business.psmProcessBO psmBO = new com.anbtech.psm.business.psmProcessBO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		RequestInfoTable table = new RequestInfoTable();

		table = purchaseDAO.getRequestInfo(request_no);
		String project_code = table.getProjectCode();
		String request_type	= table.getRequestType();
		String total_amount = table.getRequestTotalMount();
		String id			= table.getRequesterId();
		String name			= table.getRequesterInfo();
		String div_code		= table.getRequesterDivCode();
		String requester_info = id + "/" + name;

		//재료비 업데이트
		if(!request_type.equals("GEN") && !request_type.equals("ROP")) 			
			psmBO.saveResultMaterial(project_code,Double.parseDouble(total_amount),requester_info,div_code,request_no);
	}
}