package com.anbtech.st.db;

import com.anbtech.st.entity.*;
import com.anbtech.st.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class StockMgrDAO{
	private Connection con;

	public StockMgrDAO(Connection con){
		this.con = con;
	}
	
	/*********************************************
	 * mode==add(수불현황추가) 일때는 선택된 품목에 대한 가장 최근의 수불현황을 가져온다.
	 * mode==modify(수불현황수정) or mode==delete(수불현황삭제) 일때는 선택된 품목에 대한 수불현황 중
	 * 선택된 관련번호를 제외한 가장 최근의 수불현황을 가져온다.
	 *********************************************/
	public InOutInfoTable getRecentInOutInfo(String mode,String item_code,String ref_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		InOutInfoTable table = new InOutInfoTable();

		String inout_type			= "";
		String inout_date			= "";
		String item_name			= "";
		String item_desc			= "";
		String item_type			= "";
		String quantity				= "0";
		String unit					= "EA";
		String unit_cost			= "0";
		String total_cost			= "0";
		String factory_code			= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";
		String stock_unit_cost		= "0";
		String stock_quantity		= "0";

		String sql = "";
		if(mode.equals("ADD")){
			sql = "SELECT TOP 1 * FROM st_inout_master WHERE item_code = '" + item_code + "' AND inout_type='PI' ORDER BY mid DESC";
		}else if(mode.equals("MOD") || mode.equals("DEL")){
			sql = "SELECT TOP 1 * FROM st_inout_master WHERE item_code = '" + item_code + "' AND ref_no != '" + ref_no + "' AND inout_type='PI'  ORDER BY mid DESC";
		}
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			inout_type			= rs.getString("inout_type");
			inout_date			= rs.getString("inout_date");
			ref_no				= rs.getString("ref_no");
			item_name			= rs.getString("item_name");
			item_desc			= rs.getString("item_desc");
			item_type			= rs.getString("item_type");
			quantity			= rs.getString("quantity");
			unit				= rs.getString("unit");
			unit_cost			= rs.getString("unit_cost");
			total_cost			= rs.getString("total_cost");
			factory_code		= rs.getString("factory_code");
			factory_name		= rs.getString("factory_name");
			warehouse_code		= rs.getString("warehouse_code");
			warehouse_name		= rs.getString("warehouse_name");
			stock_unit_cost		= rs.getString("stock_unit_cost");
			stock_quantity		= rs.getString("stock_quantity");
		}
		stmt.close();
		rs.close();

		table.setInOutType(inout_type);
		table.setInOutDate(inout_date);
		table.setRefNo(ref_no);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setItemType(item_type);
		table.setQuantity(quantity);
		table.setUnit(unit);
		table.setUnitCost(unit_cost);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);
		table.setStockUnitCost(stock_unit_cost);
		table.setStockQuantity(stock_quantity);

		return table;
	}


	/****************************************
	 * 품목 수불정보를 기록한다.
	 ****************************************/
	public void addInOutInfo(String inout_type,String inout_date,String ref_no,String item_code,String item_name,String item_desc,String item_type,String quantity,String unit,String unit_cost,String total_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String stock_unit_cost,String stock_quantity) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_inout_master (inout_type,inout_date,ref_no,item_code,item_name,item_desc,item_type,quantity,unit,unit_cost,total_cost,factory_code,factory_name,warehouse_code,warehouse_name,stock_unit_cost,stock_quantity,process_stat) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,inout_type);
		pstmt.setString(2,inout_date);
		pstmt.setString(3,ref_no);
		pstmt.setString(4,item_code);
		pstmt.setString(5,item_name);
		pstmt.setString(6,item_desc);
		pstmt.setString(7,item_type);
		pstmt.setDouble(8,Double.parseDouble(quantity));
		pstmt.setString(9,unit);
		pstmt.setDouble(10,Double.parseDouble(unit_cost));
		pstmt.setDouble(11,Double.parseDouble(total_cost));
		pstmt.setString(12,factory_code);
		pstmt.setString(13,factory_name);
		pstmt.setString(14,warehouse_code);
		pstmt.setString(15,warehouse_name);
		pstmt.setDouble(16,Double.parseDouble(stock_unit_cost));
		pstmt.setDouble(17,Double.parseDouble(stock_quantity));
		pstmt.setString(18,"S21");

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 수불정보를 수정한다.
	 *******************************/
	 public void updateInOutInfo(String inout_type,String inout_date,String ref_no,String item_code,String item_name,String item_desc,String item_type,String quantity,String unit,String unit_cost,String total_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String stock_unit_cost,String stock_quantity,String process_stat) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE st_inout_master SET inout_type=?,inout_date=?,item_name=?,item_desc=?,item_type=?,quantity=?,unit=?,unit_cost=?,total_cost=?,factory_code=?,factory_name=?,warehouse_code=?,warehouse_name=?,stock_unit_cost=?,stock_quantity=?,process_stat=? WHERE item_code = '" + item_code + "' AND ref_no = '" + ref_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_type);
		pstmt.setString(2,inout_date);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setString(5,item_type);
		pstmt.setDouble(6,Double.parseDouble(quantity));
		pstmt.setString(7,unit);
		pstmt.setDouble(8,Double.parseDouble(unit_cost));
		pstmt.setDouble(9,Double.parseDouble(total_cost));
		pstmt.setString(10,factory_code);
		pstmt.setString(11,factory_name);
		pstmt.setString(12,warehouse_code);
		pstmt.setString(13,warehouse_name);
		pstmt.setDouble(14,Double.parseDouble(stock_unit_cost));
		pstmt.setDouble(15,Double.parseDouble(stock_quantity));
		pstmt.setString(16,process_stat);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************************
	 * 선택된 품목에 대한 재고정보를 가져온다.
	 *********************************************/
	public StockInfoTable getItemStockInfo(String factory_code,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		StockInfoTable table = new StockInfoTable();

		String item_name			= "";
		String item_desc			= "";
		String stock_unit			= "EA";
		String stock_quantity		= "0";
		String into_quantity		= "0";
		String good_quantity		= "0";
		String bad_quantity			= "0";
		String outto_quantity		= "0";
		String delivery_quantity	= "0";
		String unit_type			= "1";
		String unit_cost			= "0";
		String last_updated_date	= "";
		String factory_name			= "";
		String warehouse_code		= "";
		String warehouse_name		= "";

		String sql = "SELECT * FROM st_item_stock_master WHERE item_code = '" + item_code + "' AND factory_code = '" + factory_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			item_name			= rs.getString("item_name");
			item_desc			= rs.getString("item_desc");
			stock_unit			= rs.getString("stock_unit");
			stock_quantity		= rs.getString("stock_quantity");
			into_quantity		= rs.getString("into_quantity");
			good_quantity		= rs.getString("good_quantity");
			bad_quantity		= rs.getString("bad_quantity");
			outto_quantity		= rs.getString("outto_quantity");
			delivery_quantity		= rs.getString("delivery_quantity");
			unit_type			= rs.getString("unit_type");
			unit_cost			= rs.getString("unit_cost");
			last_updated_date	= rs.getString("last_updated_date");
			factory_code		= rs.getString("factory_code");
			factory_name		= rs.getString("factory_name");
			warehouse_code		= rs.getString("warehouse_code");
			warehouse_name		= rs.getString("warehouse_name");
		}
		stmt.close();
		rs.close();

		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setStockUnit(stock_unit);
		table.setStockQuantity(stock_quantity);
		table.setIntoQuantity(into_quantity);
		table.setGoodQuantity(good_quantity);
		table.setBadQuantity(bad_quantity);
		table.setOuttoQuantity(outto_quantity);
		table.setDeliveryQuantity(delivery_quantity);
		table.setUnitType(unit_type);
		table.setUnitCost(unit_cost);
		table.setLastUpdatedDate(last_updated_date);
		table.setFactoryCode(factory_code);
		table.setFactoryName(factory_name);
		table.setWarehouseCode(warehouse_code);
		table.setWarehouseName(warehouse_name);

		return table;
	}

	/****************************************
	 * 신규 품목재고정보를 입력한다.
	 ****************************************/
	public void addItemStockInfo(String item_code,String item_name,String item_desc,String stock_unit,String stock_quantity,String into_quantity,String good_quantity,String bad_quantity,String outto_quantity,String delivery_quantity,String unit_type,String unit_cost,String last_updated_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_item_stock_master (item_code,item_name,item_desc,stock_unit,stock_quantity,into_quantity,good_quantity,bad_quantity,outto_quantity,delivery_quantity,unit_type,unit_cost,last_updated_date,factory_code,factory_name,warehouse_code,warehouse_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,item_desc);
		pstmt.setString(4,stock_unit);
		pstmt.setDouble(5,Double.parseDouble(stock_quantity));
		pstmt.setDouble(6,Double.parseDouble(into_quantity));
		pstmt.setDouble(7,Double.parseDouble(good_quantity));
		pstmt.setDouble(8,Double.parseDouble(bad_quantity));
		pstmt.setDouble(9,Double.parseDouble(outto_quantity));
		pstmt.setDouble(10,Double.parseDouble(delivery_quantity));
		pstmt.setString(11,unit_type);
		pstmt.setString(12,unit_cost);
		pstmt.setString(13,last_updated_date);
		pstmt.setString(14,factory_code);
		pstmt.setString(15,factory_name);
		pstmt.setString(16,warehouse_code);
		pstmt.setString(17,warehouse_name);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 재고정보를 업데이트한다.
	 *******************************/
	 public void updateItemStockInfo(String item_code,String item_name,String item_desc,String stock_unit,String stock_quantity,String into_quantity,String good_quantity,String bad_quantity,String outto_quantity,String delivery_quantity,String unit_type,String unit_cost,String last_updated_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE st_item_stock_master SET item_name=?,item_desc=?,stock_unit=?,stock_quantity=?,into_quantity=?,good_quantity=?,bad_quantity=?,outto_quantity=?,delivery_quantity=?,unit_type=?,unit_cost=?,last_updated_date=?,factory_name=?,warehouse_code=?,warehouse_name=? WHERE item_code = '" + item_code + "' AND factory_code = '" + factory_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,item_desc);
		pstmt.setString(3,stock_unit);
		pstmt.setDouble(4,Double.parseDouble(stock_quantity));
		pstmt.setDouble(5,Double.parseDouble(into_quantity));
		pstmt.setDouble(6,Double.parseDouble(good_quantity));
		pstmt.setDouble(7,Double.parseDouble(bad_quantity));
		pstmt.setDouble(8,Double.parseDouble(outto_quantity));
		pstmt.setDouble(9,Double.parseDouble(delivery_quantity));
		pstmt.setString(10,unit_type);
		pstmt.setString(11,unit_cost);
		pstmt.setString(12,last_updated_date);
		pstmt.setString(13,factory_name);
		pstmt.setString(14,warehouse_code);
		pstmt.setString(15,warehouse_name);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************************
	 * 선택된 품목에 대한 공장별 입고예정수량을 가져온다.
	 ********************************************
	public String getInToQuantityByFactory(String factory_code,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String into_quantity	= "0";

		String sql = "SELECT SUM(quantity) FROM st_inout_master WHERE item_code = '" + item_code + "' AND factory_code = '" + factory_code + "' AND process_stat = 'S21'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		if(rs.getString(1) != null) into_quantity	= rs.getString(1);
		stmt.close();
		rs.close();
		
		return into_quantity;
	}
*/
	/************************************
	 * 수불현황 조회결과 리스트
	 ************************************/
	public ArrayList getInOutList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		InOutInfoTable table	= new InOutInfoTable();
		StockConfigMgrDAO configDAO = new StockConfigMgrDAO(con);

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList inout_list = new ArrayList();
		String where = stBO.getWhereForInOutList(mode,searchword,login_id);
		int total = getTotalCount("st_inout_master", where);
		int recNum = total;

		String query = "SELECT * FROM st_inout_master " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new InOutInfoTable();

			table.setInOutType(rs.getString("inout_type"));
			table.setInOutTypeName(configDAO.getTradeTypeName(rs.getString("inout_type")));
			table.setInOutDate(rs.getString("inout_date"));
			table.setRefNo(rs.getString("ref_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setQuantity(rs.getString("quantity"));
			table.setUnit(rs.getString("unit"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setTotalCost(rs.getString("total_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setStockUnitCost(rs.getString("stock_unit_cost"));
			table.setStockQuantity(rs.getString("stock_quantity"));

			inout_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return inout_list;
	}

	/************************************
	 * 품목별,창고별 품목 재고현황 리스트
	 ************************************/
	public ArrayList getItemStockList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		StockInfoTable table	= new StockInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList stock_list = new ArrayList();
		String where = stBO.getWhereForItemStockList(mode,searchword,login_id);
		int total = getTotalCount("v_item_stock_master", where);
		int recNum = total;

		String query = "SELECT * FROM v_item_stock_master " + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new StockInfoTable();

			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setStockUnit(rs.getString("stock_unit"));
			table.setStockQuantity(rs.getString("stock_quantity"));
			table.setIntoQuantity(rs.getString("into_quantity"));
			table.setGoodQuantity(rs.getString("good_quantity"));
			table.setBadQuantity(rs.getString("bad_quantity"));
			table.setOuttoQuantity(rs.getString("outto_quantity"));
			table.setLastUpdatedDate(rs.getString("last_updated_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
//			table.setWarehouseCode(rs.getString("warehouse_code"));
//			table.setWarehouseName(rs.getString("warehouse_name"));

			table.setUnitCostS(rs.getString("unit_cost_s"));
			table.setUnitCostA(rs.getString("unit_cost_a"));
			table.setUnitCostC(rs.getString("unit_cost_c"));
			table.setLeadTime(rs.getString("lead_time"));
			table.setResonableQuantity(rs.getString("resonable_quantity"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));


			stock_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return stock_list;
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

		return total_count;
	}

	/************************************
	 * 검색조건에 맞는 기타입출고정보 가져오기
	 ************************************/
	public ArrayList getEtcInOutInfoList(String mode,String in_or_out,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		StockMgrBO stBO	= new StockMgrBO(con);
		EtcInOutInfoTable table		= new EtcInOutInfoTable();
		StockConfigMgrDAO configDAO = new StockConfigMgrDAO(con);
		
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList list = new ArrayList();
		String where = stBO.getWhereForEtcInOutInfoList(mode,in_or_out,searchword,searchscope,login_id);
		int total = getTotalCount("st_etc_inout_info", where);
		int recNum = total;

		String query = "SELECT * FROM st_etc_inout_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String no_link = "";
			no_link += "<a href='StockMgrServlet?mode=view_etc_inout_info&in_or_out="+in_or_out+"&inout_no=" + rs.getString("inout_no");
			no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
			no_link += "onMouseOver=\"window.status='입출고정보 상세보기 #" + rs.getString("inout_no") + "';return true;\" ";
			no_link += "onMouseOut=\"window.status='';return true;\">" + rs.getString("inout_no") + "</a>";
				
			table = new EtcInOutInfoTable();
			Iterator file_iter = getEtcInOutFileList("st_etc_inout_info", rs.getString("inout_no")).iterator();

			int j = 1;
			String filelink = "&nbsp;";
			while(file_iter.hasNext()){

				EtcInOutInfoTable file = (EtcInOutInfoTable)file_iter.next();
				filelink += "<a href='StockMgrServlet?tablename=st_etc_inout_info&upload_folder=etc_inout&mode=download_etc_inout&inout_no="+rs.getString("inout_no")+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFname()+" ("+file.getFsize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFname(), file.getFtype()) + ".gif' border=0></a>";
				j++;				
			}

			table.setInOutNo(no_link);
			table.setInOutType(rs.getString("inout_type"));
			table.setInOutTypeName(configDAO.getTradeTypeName(rs.getString("inout_type")));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setInOutDate(rs.getString("inout_date"));
			table.setTotalMount(rs.getString("total_mount"));
			table.setFileLink(filelink);
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));

			list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return list;
	}

	/*****************************************************************
	* 첨부파일 리스트 가져오기
	*****************************************************************/
	public ArrayList getEtcInOutFileList(String tablename,String no) throws Exception{

	Statement stmt = null;
	ResultSet rs = null;
	int total = 0;
	ArrayList file_list = new ArrayList();

	String query = "SELECT fname,ftype,fsize,fumask FROM "+tablename+" WHERE inout_no = '"+no+"'";
	stmt = con.createStatement();
	rs = stmt.executeQuery(query);
	if(rs.next()){ 

		Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
		Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
		Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
		Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("fumask")).iterator();

		while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()&&fileumask_iter.hasNext()){
			EtcInOutInfoTable file = new EtcInOutInfoTable();
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
	}


	/*********************************************
	 * 선택된 번호에 해당하는 기타입출고정보 가져오기
	 *********************************************/
	public EtcInOutInfoTable getEtcInOutInfo(String inout_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		EtcInOutInfoTable table = new EtcInOutInfoTable();

		String sql = "SELECT * FROM st_etc_inout_info WHERE inout_no = '" + inout_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setInOutNo(inout_no);
			table.setInOutDate(rs.getString("inout_date"));
			table.setTotalMount(rs.getString("total_mount"));
			table.setMonetaryUnit(rs.getString("monetary_unit"));
			table.setInOutType(rs.getString("inout_type"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/****************************************************************
	 * 기타입출고번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getEtcInOutNo(String in_or_out) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String inout_no		= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM st_etc_inout_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "' AND in_or_out = '" + in_or_out + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		inout_no	= in_or_out + "E" + reg_year + reg_month + "-" + reg_serial;

		return inout_no;
	}

	/*********************************
	 * 기타입출고정보 저장
	 *********************************/
	 public void saveEtcInOutInfo(String in_or_out,String inout_no,String inout_date,String total_mount,String monetary_unit,String inout_type,String supplyer_code,String supplyer_name,String reg_year,String reg_month,String reg_serial,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info) throws Exception{
		PreparedStatement pstmt = null;

		if(requestor_id != null && !requestor_id.equals("")){
			com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
			com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
			userinfo = userDAO.getUserListById(requestor_id);

			if(requestor_div_name == null || requestor_div_name.equals("")) requestor_div_name	= userinfo.getDivision();
			if(requestor_div_code == null || requestor_div_code.equals("")) requestor_div_code	= userinfo.getAcCode();
			if(requestor_info == null || requestor_info.equals("")) requestor_info		= userinfo.getUserName();
		}

		String query = "INSERT INTO st_etc_inout_info (inout_no,inout_date,total_mount,monetary_unit,inout_type,supplyer_code,supplyer_name,reg_year,reg_month,reg_serial,requestor_div_code,requestor_div_name,requestor_id,requestor_info,in_or_out) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,inout_no);
		pstmt.setString(2,inout_date);
		pstmt.setString(3,total_mount);
		pstmt.setString(4,monetary_unit);
		pstmt.setString(5,inout_type);
		pstmt.setString(6,supplyer_code);
		pstmt.setString(7,supplyer_name);
		pstmt.setString(8,reg_year);
		pstmt.setString(9,reg_month);
		pstmt.setString(10,reg_serial);
		pstmt.setString(11,requestor_div_code);
		pstmt.setString(12,requestor_div_name);
		pstmt.setString(13,requestor_id);
		pstmt.setString(14,requestor_info);
		pstmt.setString(15,in_or_out);

		pstmt.executeUpdate();
		pstmt.close();
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

	/*******************************
	 * 기타입출고등록정보 수정하기
	 *******************************/
	 public void updateEtcInOutInfo(String inout_no,String inout_type,String inout_date,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String total_mount,String monetary_unit,String supplyer_code,String supplyer_name) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE st_etc_inout_info SET inout_type=?,inout_date=?,requestor_div_code=?,requestor_div_name=?,requestor_id=?,requestor_info=?,supplyer_code=?,supplyer_name=?,total_mount=?,monetary_unit=? WHERE inout_no = '" + inout_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,inout_type);
		pstmt.setString(2,inout_date);
		pstmt.setString(3,requestor_div_code);
		pstmt.setString(4,requestor_div_name);
		pstmt.setString(5,requestor_id);
		pstmt.setString(6,requestor_info);
		pstmt.setString(7,supplyer_code);
		pstmt.setString(8,supplyer_name);
		pstmt.setString(9,total_mount);
		pstmt.setString(10,monetary_unit);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 선택된 입출고정보 삭제하기
	 *******************************/
	public void deleteEtcInOutInfo(String inout_no) throws Exception{
		Statement stmt = null;
		String query = "DELETE st_etc_inout_info WHERE inout_no = '" + inout_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/************************************
	 * 선택된 기타입출고번호에 존재하는 품목리스트를 가져온다.
	 ************************************/
	public ArrayList getEtcInOutItemList(String inout_no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList item_list = new ArrayList();
		EtcInOutInfoTable table = new EtcInOutInfoTable();
		
		String query = "SELECT * FROM st_etc_inout_item WHERE inout_no = '" + inout_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new EtcInOutInfoTable();
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setQuantity(rs.getString("quantity"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setInOutCost(rs.getString("inout_cost"));
			table.setUnitCost(rs.getString("unit_cost"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setUnitType(rs.getString("unit_type"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/*********************************
	 * 기타입출고품목정보 저장
	 *********************************/
	public void saveEtcInOutItemInfo(String inout_no,String item_code,String item_name,String item_desc,String quantity,String item_unit,String inout_cost,String unit_cost,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String process_stat,String unit_type) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_etc_inout_item (inout_no,item_code,item_name,item_desc,quantity,item_unit,inout_cost,unit_cost,factory_code,factory_name,warehouse_code,warehouse_name,process_stat,unit_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,inout_no);
		pstmt.setString(2,item_code);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setString(5,quantity);
		pstmt.setString(6,item_unit);
		pstmt.setString(7,inout_cost);
		pstmt.setString(8,unit_cost);
		pstmt.setString(9,factory_code);
		pstmt.setString(10,factory_name);
		pstmt.setString(11,warehouse_code);
		pstmt.setString(12,warehouse_name);
		pstmt.setString(13,process_stat);
		pstmt.setString(14,unit_type);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/************************************
	 * 선택된 기타입출고번호에 존재하는 품목수를 가져온다.
	 ************************************/
	public int getEtcInOutItemCount(String inout_no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int count = 0;		
		String query = "SELECT COUNT(*) FROM st_etc_inout_item WHERE inout_no = '" + inout_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			count = rs.getInt(1);
		}
		stmt.close();
		rs.close();

		return count;
	}


	/*******************************
	 * 선택된 수불정보 삭제하기
	 *******************************/
	public void deleteInOutInfo(String ref_no,String item_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE st_inout_master WHERE ref_no = '" + ref_no + "' AND item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*********************************************
	 * 선택된 품목에 대한 원가정보를 가져온다.
	 *********************************************/
	public StockInfoTable getItemUnitCostInfo(String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		StockInfoTable table = new StockInfoTable();

		String item_type			= "";
		String item_name			= "";
		String item_desc			= "";
		String unit_cost_s			= "0";
		String unit_cost_a			= "0";
		String unit_cost_c			= "0";
		String last_updated_date	= "";

		String sql = "SELECT * FROM st_item_unit_cost WHERE item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			item_type			= rs.getString("item_type");
			item_name			= rs.getString("item_name");
			item_desc			= rs.getString("item_desc");
			unit_cost_s			= rs.getString("unit_cost_s");
			unit_cost_a			= rs.getString("unit_cost_a");
			unit_cost_c			= rs.getString("unit_cost_c");
			last_updated_date	= rs.getString("last_updated_date");
		}
		stmt.close();
		rs.close();

		table.setItemType(item_type);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemDesc(item_desc);
		table.setUnitCostS(unit_cost_s);
		table.setUnitCostA(unit_cost_a);
		table.setUnitCostC(unit_cost_c);
		table.setLastUpdatedDate(last_updated_date);

		return table;
	}

	/*********************************
	 * 품목원가정보를 추가한다.
	 *********************************/
	 public void addItemUnitCostInfo(String item_type,String item_code,String item_name,String item_desc,String unit_cost_s,String  unit_cost_a,String unit_cost_c,String last_updated_date) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_item_unit_cost (item_type,item_code,item_name,item_desc,unit_cost_s,unit_cost_a,unit_cost_c,last_updated_date) VALUES (?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_type);
		pstmt.setString(2,item_code);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setDouble(5,Double.parseDouble(unit_cost_s));
		pstmt.setDouble(6,Double.parseDouble(unit_cost_a));
		pstmt.setDouble(7,Double.parseDouble(unit_cost_c));
		pstmt.setString(8,last_updated_date);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 품목원가정보 수정하기
	 *******************************/
	 public void updateItemUnitCostInfo(String item_code,String unit_cost_type,String unit_cost,String last_updated_date) throws Exception{
		Statement stmt = null;

		String query = "UPDATE st_item_unit_cost SET " + unit_cost_type + " = " + Double.parseDouble(unit_cost) + ",last_updated_date = '" + last_updated_date + "' WHERE item_code = '" + item_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}


	
	/*************************************
	* 재고이동 정보 저장
	**************************************/
	public void saveItemShiftInfo(String shift_no,String shift_type,String sr_factory_code,String sr_factory_name,String sr_warehouse_code,String sr_warehouse_name,String sr_item_code,String sr_item_name,String sr_item_type,String sr_item_desc,String dt_factory_code,String dt_factory_name,String dt_warehouse_code,String dt_warehouse_name,String dt_item_code,String dt_item_name,String dt_item_type,String dt_item_desc,String stock_unit,String quantity,String requestor_id,String reg_date,String reg_year,String reg_month,String reg_serial)throws Exception {
	
	PreparedStatement pstmt = null;

	//등록자 정보
	com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
	com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
	userinfo = userDAO.getUserListById(requestor_id);
	String requestor_div_name	= userinfo.getDivision();
	String requestor_div_code	= userinfo.getAcCode();
	String requestor_info		= userinfo.getUserName();
	
	String query = "INSERT INTO st_item_shift_info (shift_no,shift_type,sr_factory_code,sr_factory_name,sr_warehouse_code,sr_warehouse_name,sr_item_code,sr_item_name,sr_item_type,sr_item_desc,dt_factory_code,dt_factory_name,dt_warehouse_code,dt_warehouse_name,dt_item_code,dt_item_name,dt_item_type,dt_item_desc,stock_unit,quantity,requestor_id,requestor_info,requestor_div_code,requestor_div_name,reg_date,reg_year,reg_month,reg_serial) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,shift_no);
		pstmt.setString(2,shift_type);
		pstmt.setString(3,sr_factory_code);
		pstmt.setString(4,sr_factory_name);
		pstmt.setString(5,sr_warehouse_code);
		pstmt.setString(6,sr_warehouse_name);
		pstmt.setString(7,sr_item_code);
		pstmt.setString(8,sr_item_name);
		pstmt.setString(9,sr_item_type);
		pstmt.setString(10,sr_item_desc);
		pstmt.setString(11,dt_factory_code);
		pstmt.setString(12,dt_factory_name);
		pstmt.setString(13,dt_warehouse_code);
		pstmt.setString(14,dt_warehouse_name);
		pstmt.setString(15,dt_item_code);
		pstmt.setString(16,dt_item_name);
		pstmt.setString(17,dt_item_type);
		pstmt.setString(18,dt_item_desc);
		pstmt.setString(19,stock_unit);
		pstmt.setString(20,quantity);
		pstmt.setString(21,requestor_id);
		pstmt.setString(22,requestor_info);
		pstmt.setString(23,requestor_div_code);
		pstmt.setString(24,requestor_div_name);
		pstmt.setString(25,reg_date);
		pstmt.setString(26,reg_year);
		pstmt.setString(27,reg_month);
		pstmt.setString(28,reg_serial);

		pstmt.executeUpdate();
		pstmt.close();

	}
	
	/************************************
	* 재고 이동 현황 list
	************************************/
	public ArrayList getItemShiftInfoList(String mode,String searchword,String searchscope,String page,int l_maxlist) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		StShiftInfoTable table	= new StShiftInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList shift_list = new ArrayList();
		String where = stBO.getWhereForShiftInfoList(mode,searchword,searchscope);

		int total = getTotalCount("st_item_shift_info", where);
		int recNum = total;

		String query = "SELECT * FROM st_item_shift_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new StShiftInfoTable();

			table.setMid(rs.getString("mid"));
			table.setShiftNo(rs.getString("shift_no"));
			table.setShiftType(rs.getString("shift_type"));

			table.setSrFactoryCode(rs.getString("sr_factory_code"));
			table.setSrFactoryName(rs.getString("sr_factory_name"));
			table.setSrWarehouseCode(rs.getString("sr_warehouse_code"));
			table.setSrWarehouseName(rs.getString("sr_warehouse_name"));
			table.setSrItemCode(rs.getString("sr_item_code"));
			table.setSrItemName(rs.getString("sr_item_name"));
			table.setSrItemType(rs.getString("sr_item_type"));
			table.setSrItemDesc(rs.getString("sr_item_desc"));

			table.setDtFactoryCode(rs.getString("dt_factory_code"));
			table.setDtFactoryName(rs.getString("dt_factory_name"));
			table.setDtWarehouseCode(rs.getString("dt_warehouse_code"));
			table.setDtWarehouseName(rs.getString("dt_warehouse_name"));
			table.setDtItemCode(rs.getString("dt_item_code"));
			table.setDtItemName(rs.getString("dt_item_name"));
			table.setDtItemType(rs.getString("dt_item_type"));
			table.setDtItemDesc(rs.getString("dt_item_desc"));

		    table.setStockUnit(rs.getString("stock_unit"));
			table.setQuantity(rs.getString("quantity"));

			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setRegDate(rs.getString("reg_date"));

			shift_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return shift_list;
	}


	/*********************************************
	 * 선택된 관리번호에 해당하는 정보 가져오기
	 *********************************************/
	public StShiftInfoTable getItemShiftInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		StShiftInfoTable table = new StShiftInfoTable();

		String sql = "SELECT * FROM st_item_shift_info WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setShiftNo(rs.getString("shift_no"));
			table.setShiftType(rs.getString("shift_type"));

			table.setSrFactoryCode(rs.getString("sr_factory_code"));
			table.setSrFactoryName(rs.getString("sr_factory_name"));
			table.setSrWarehouseCode(rs.getString("sr_warehouse_code"));
			table.setSrWarehouseName(rs.getString("sr_warehouse_name"));
			table.setSrItemCode(rs.getString("sr_item_code"));
			table.setSrItemName(rs.getString("sr_item_name"));
			table.setSrItemType(rs.getString("sr_item_type"));
			table.setSrItemDesc(rs.getString("sr_item_desc"));

			table.setDtFactoryCode(rs.getString("dt_factory_code"));
			table.setDtFactoryName(rs.getString("dt_factory_name"));
			table.setDtWarehouseCode(rs.getString("dt_warehouse_code"));
			table.setDtWarehouseName(rs.getString("dt_warehouse_name"));
			table.setDtItemCode(rs.getString("dt_item_code"));
			table.setDtItemName(rs.getString("dt_item_name"));
			table.setDtItemType(rs.getString("dt_item_type"));
			table.setDtItemDesc(rs.getString("dt_item_desc"));

		    table.setStockUnit(rs.getString("stock_unit"));
			table.setQuantity(rs.getString("quantity"));

			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setRegDate(rs.getString("reg_date"));
		}
		stmt.close();
		rs.close();

		return table;
	}


	
	/****************************************************************
	 * 재고 이동번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getShiftNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String shift_no	= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM st_item_shift_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		shift_no	= "MOV" + reg_year + reg_month + "-" + reg_serial;

		return shift_no;
	}

	/****************************************************************
	 * 기타출고의뢰번호를 생성하여 리턴한다.
	 ****************************************************************/
	public String getDeliveryNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String delivery_no	= "";
		String reg_serial	= "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM st_reserved_item_info WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		delivery_no	= "DE" + reg_year + reg_month + "-" + reg_serial;

		return delivery_no;
	}

	/************************************
	 * 생산출고의뢰품목 리스트 가져오기
	 ************************************/
	public ArrayList getReservedItemList(String mode,String searchword,String searchscope,String login_id) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		ReservedItemInfoTable table	= new ReservedItemInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		ArrayList item_list = new ArrayList();
		String where = stBO.getWhereForReservedItemList(mode,searchword,login_id);

		String query = "SELECT * FROM st_reserved_item_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new ReservedItemInfoTable();

			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/************************************
	 * 생산출고등록품목 리스트 가져오기
	 ************************************/
	public ArrayList getToEnterItemList(String mode,String searchword,String searchscope,String login_id) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		ReservedItemInfoTable table	= new ReservedItemInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		ArrayList item_list = new ArrayList();
		String where = stBO.getWhereForToEnterItemList(mode,searchword,login_id);

		String query = "SELECT * FROM st_deliveried_item_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new ReservedItemInfoTable();

			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setDeliveriedQuantity(rs.getString("deliveried_quantity"));
			table.setAid(rs.getString("aid"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/************************************
	 * 생산출고완료품목 리스트 가져오기
	 ************************************/
	public ArrayList getDeliveriedItemList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		ReservedItemInfoTable table	= new ReservedItemInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList item_list = new ArrayList();
		String where = stBO.getWhereForDeliveriedItemList(mode,searchword,login_id);
		int total = getTotalCount("st_deliveried_item_info", where);
		int recNum = total;

		String query = "SELECT * FROM st_deliveried_item_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new ReservedItemInfoTable();

			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));

			item_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/************************************
	 * 생산출고현황 리스트 가져오기
	 ************************************/
	public ArrayList getDeliveriedInfoList(String mode,String searchword,String searchscope,String page,String login_id,int l_maxlist) throws Exception {
		StockMgrBO stBO			= new StockMgrBO(con);
		ReservedItemInfoTable table	= new ReservedItemInfoTable();

		Statement stmt = null;
		ResultSet rs = null;
	
		
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList item_list = new ArrayList();
		String where = stBO.getWhereForDeliveriedInfoList(mode,searchword,searchscope,login_id);
		int total = getTotalCount("v_deliveried_info", where);
		int recNum = total;

		String query = "SELECT * FROM v_deliveried_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table = new ReservedItemInfoTable();

			String item_code_link = "";
			item_code_link += "<a href=\"javascript:go_print('" + rs.getString("aid") + "');\">" + rs.getString("aid") + "</a>";

			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setDeliveriedQuantity(rs.getString("deliveried_quantity"));
			table.setAid(rs.getString("aid"));
			table.setItemCodeLink(item_code_link);

			item_list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/*********************************************
	 * 선택된 출고의뢰품목정보 가져오기
	 *********************************************/
	public ReservedItemInfoTable getReservedItemInfo(String delivery_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		ReservedItemInfoTable table = new ReservedItemInfoTable();

		String sql = "SELECT * FROM st_reserved_item_info WHERE delivery_no = '" + delivery_no + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * 선택된 출고등록품목정보 가져오기
	 *********************************************/
	public ReservedItemInfoTable getDeliveriedItemInfo(String delivery_no,String item_code,String aid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		ReservedItemInfoTable table = new ReservedItemInfoTable();

		String sql = "SELECT * FROM st_deliveried_item_info WHERE delivery_no = '" + delivery_no + "' AND item_code = '" + item_code + "' AND aid = '" + aid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setRequestQuantity(rs.getString("request_quantity"));
			table.setRequestUnit(rs.getString("request_unit"));
			table.setRequestDate(rs.getString("request_date"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setAid(aid);
			table.setDeliveriedQuantity(rs.getString("deliveried_quantity"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*******************************
	 * 실제 출고가 이루어졌을 때 생산출고등록품목 정보를 수정한다.
	 *******************************/
	 public void updateDeliveriedItemInfo(String mode,String delivery_no,String item_code,String quantity,String item_unit,String login_id,String delivery_date,String aid) throws Exception{
		PreparedStatement pstmt = null;

		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String deliverer_div_name	= userinfo.getDivision();
		String deliverer_div_code	= userinfo.getAcCode();
		String deliverer_info		= userinfo.getUserName();

		String query = "UPDATE st_deliveried_item_info SET delivery_quantity=?,delivery_unit=?,delivery_date=?,deliverer_div_code=?,deliverer_div_name=?,deliverer_id=?,deliverer_info=?,process_stat=? WHERE delivery_no = '" + delivery_no + "' AND item_code = '" + item_code + "' AND aid = '" + aid + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,quantity);
		pstmt.setString(2,item_unit);
		pstmt.setString(3,delivery_date);
		pstmt.setString(4,deliverer_div_code);
		pstmt.setString(5,deliverer_div_name);
		pstmt.setString(6,login_id);
		pstmt.setString(7,deliverer_info);
		pstmt.setString(8,"S59");
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/****************************************************************
	 * 생산출고 자재를 출고할때 생산모듈의 DB를 업데이트시킨다.
	 ****************************************************************/
	public void updateMfgMoudle(String delivery_no,String ref_no,String item_code,String factory_code,String delivery_quantity) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql				= "";
		String receive_count	= "0";
		
		//mfg_req_master 테이블의 req_stats 를 3으로 변경
		sql = "UPDATE mfg_req_master SET req_status = '3' WHERE mfg_req_no = '" + delivery_no + "' AND factory_no = '" + factory_code + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(sql);

		//mfg_inout_master 테이블의 receive_counter 개수를 업데이트한다.
		sql = "SELECT receive_count FROM mfg_inout_master WHERE mfg_no = '" + ref_no + "' AND factory_no = '" + factory_code + "' AND item_code = '" + item_code + "'";
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			receive_count = rs.getString("receive_count");
		}

		receive_count = Integer.toString(Integer.parseInt(receive_count) + Integer.parseInt(delivery_quantity));

		sql = "UPDATE mfg_inout_master SET receive_count = '" + receive_count + "' WHERE mfg_no = '" + ref_no + "' AND factory_no = '" + factory_code + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(sql);

		stmt.close();
		rs.close();
	}

	/*************************************
	* 기타출고의뢰정보 저장
	**************************************/
	public void saveEtcDeliveryInfo(String delivery_no,String item_code,String item_name,String item_desc,String item_type,String request_quantity,String request_unit,String request_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String ref_no,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String reg_year,String reg_month,String reg_serial) throws Exception {
	
	PreparedStatement pstmt = null;

	String query = "INSERT INTO st_reserved_item_info (delivery_no,item_code,item_name,item_desc,item_type,request_quantity,request_unit,request_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info,process_stat,reg_year,reg_month,reg_serial) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	pstmt = con.prepareStatement(query);

	pstmt.setString(1,delivery_no);
	pstmt.setString(2,item_code);
	pstmt.setString(3,item_name);
	pstmt.setString(4,item_desc);
	pstmt.setString(5,item_type);
	pstmt.setString(6,request_quantity);
	pstmt.setString(7,request_unit);
	pstmt.setString(8,request_date);
	pstmt.setString(9,factory_code);
	pstmt.setString(10,factory_name);
	pstmt.setString(11,warehouse_code);
	pstmt.setString(12,warehouse_name);
	pstmt.setString(13,ref_no);
	pstmt.setString(14,requestor_div_code);
	pstmt.setString(15,requestor_div_name);
	pstmt.setString(16,requestor_id);
	pstmt.setString(17,requestor_info);
	pstmt.setString(18,"S53");
	pstmt.setString(19,reg_year);
	pstmt.setString(20,reg_month);
	pstmt.setString(21,reg_serial);

	pstmt.executeUpdate();
	pstmt.close();

	}

	/*******************************
	 * 기타출고의뢰정보 수정하기
	 *******************************/
	 public void updateEtcDeliveryInfo(String delivery_no,String item_code,String item_name,String item_desc,String item_type,String request_quantity,String item_unit,String request_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String ref_no,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE st_reserved_item_info SET request_quantity=?,request_date=?,factory_code=?,factory_name=?,ref_no=? WHERE delivery_no = '" + delivery_no + "' AND item_code = '" + item_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_quantity);
		pstmt.setString(2,request_date);
		pstmt.setString(3,factory_code);
		pstmt.setString(4,factory_name);
		pstmt.setString(5,ref_no);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*************************************
	* 전자결재 상신 대상 출고대상품목정보 저장
	**************************************/
	public void saveDeliveiedItemInfo(String delivery_no,String item_code,String item_name,String item_desc,String item_type,String request_quantity,String request_unit,String request_date,String factory_code,String factory_name,String warehouse_code,String warehouse_name,String ref_no,String requestor_div_code,String requestor_div_name,String requestor_id,String requestor_info,String delivery_quantity,String delivery_unit,String delivery_date,String deliverer_div_code,String deliverer_div_name,String deliverer_id,String deliverer_info,String process_stat,String aid,String deliveried_quantity) throws Exception {
	
		PreparedStatement pstmt = null;

		String query = "INSERT INTO st_deliveried_item_info (delivery_no,item_code,item_name,item_desc,item_type,request_quantity,request_unit,request_date,factory_code,factory_name,warehouse_code,warehouse_name,ref_no,requestor_div_code,requestor_div_name,requestor_id,requestor_info,delivery_quantity,delivery_unit,delivery_date,deliverer_div_code,deliverer_div_name,deliverer_id,deliverer_info,process_stat,aid,deliveried_quantity) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);

		pstmt.setString(1,delivery_no);
		pstmt.setString(2,item_code);
		pstmt.setString(3,item_name);
		pstmt.setString(4,item_desc);
		pstmt.setString(5,item_type);
		pstmt.setString(6,request_quantity);
		pstmt.setString(7,request_unit);
		pstmt.setString(8,request_date);
		pstmt.setString(9,factory_code);
		pstmt.setString(10,factory_name);
		pstmt.setString(11,warehouse_code);
		pstmt.setString(12,warehouse_name);
		pstmt.setString(13,ref_no);
		pstmt.setString(14,requestor_div_code);
		pstmt.setString(15,requestor_div_name);
		pstmt.setString(16,requestor_id);
		pstmt.setString(17,requestor_info);
		pstmt.setString(18,delivery_quantity);
		pstmt.setString(19,delivery_unit);
		pstmt.setString(20,delivery_date);
		pstmt.setString(21,deliverer_div_code);
		pstmt.setString(22,deliverer_div_name);
		pstmt.setString(23,deliverer_id);
		pstmt.setString(24,deliverer_info);
		pstmt.setString(25,process_stat);
		pstmt.setString(26,aid);
		pstmt.setString(27,deliveried_quantity);

		pstmt.executeUpdate();
		pstmt.close();
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
	 * 선택된 테이블내 품목 상태코드를 지정된 값으로 변경한다.
	 *******************************/
	 public void updateProcessStat(String tablename,String no_type,String no,String process_stat) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + " SET process_stat = '" + process_stat + "' WHERE " + no_type + " = '" + no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 선택된 테이블내 임시 전자결재번호를 정식 번호로 변경한다.
	 *******************************/
	 public void updateAid(String tablename,String tmp_aid,String aid) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + " SET aid = '" + aid + "' WHERE aid = '" + tmp_aid + "'";

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

	/************************************
	 * 선택된 전자결재번호를 갖는 출고등록품목 리스트 가져오기
	 ************************************/
	public ArrayList getDeliveriedItemList(String mode,String aid) throws Exception {
		ReservedItemInfoTable table	= new ReservedItemInfoTable();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList item_list = new ArrayList();

		String query = "SELECT * FROM st_deliveried_item_info WHERE aid = '" + aid + "' ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new ReservedItemInfoTable();

			table.setDeliveryNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setWarehouseCode(rs.getString("warehouse_code"));
			table.setWarehouseName(rs.getString("warehouse_name"));
			table.setRefNo(rs.getString("ref_no"));
			table.setRequestorDivCode(rs.getString("requestor_div_code"));
			table.setRequestorDivName(rs.getString("requestor_div_name"));
			table.setRequestorId(rs.getString("requestor_id"));
			table.setRequestorInfo(rs.getString("requestor_info"));
			table.setDeliveryQuantity(rs.getString("delivery_quantity"));
			table.setDeliveryUnit(rs.getString("delivery_unit"));
			table.setDeliveryDate(rs.getString("delivery_date"));
			table.setDelivererDivCode(rs.getString("deliverer_div_code"));
			table.setDelivererDivName(rs.getString("deliverer_div_name"));
			table.setDelivererId(rs.getString("deliverer_id"));
			table.setDelivererInfo(rs.getString("deliverer_info"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setAid(aid);
			table.setDeliveriedQuantity(rs.getString("deliveried_quantity"));
			table.setRequestQuantity(rs.getString("request_quantity"));

			item_list.add(table);
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/*******************************
	 * 출고수량을 변경
	 *******************************/
	 public void updateDeliveryQuantity(String delivery_no,String ref_no,String item_code,String aid,String delivery_quantity) throws Exception{
		Statement stmt = null;
		String query = "UPDATE st_deliveried_item_info SET delivery_quantity = '" + delivery_quantity + "' WHERE delivery_no = '" + delivery_no + "' AND item_code = '" + item_code + "' AND ref_no = '" + ref_no + "' AND aid= '" + aid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
}		