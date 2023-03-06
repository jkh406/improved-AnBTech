package com.anbtech.em.db;

import com.anbtech.em.entity.*;
import com.anbtech.em.business.*;
import com.anbtech.em.db.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class EstimateDAO{
	private Connection con;
	private String whereStr = "";

	public EstimateDAO(Connection con){
		this.con = con;
	}


	/********************
	 * 견적 리스트 출력
	 ********************/
	public ArrayList getEstimateList(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 17;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
		int current_page_num =Integer.parseInt(page);

		ArrayList estimate_list = new ArrayList();
		EstimateInfoTable estimate = new EstimateInfoTable();
		EstimateBO estimateBO = new EstimateBO(con);

		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		
		String where = "";

		if(searchscope.equals("detail")) where = estimateBO.getWhereForDetail(mode,searchword,category,login_id);
		else where = estimateBO.getWhere(mode,searchword,searchscope,category,login_id);

		int total = getTotalCount("estimate_info", where);
		int recNum = total;

		String query = "SELECT * FROM estimate_info " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");				//관리번호
			String estimate_no			= rs.getString("estimate_no");		//견적번호
			String version				= rs.getString("version");			//버젼
			String company_name			= rs.getString("company_name");		//회사명
			String estimate_subj		= rs.getString("estimate_subj");	//견적제목
			String delivery_day			= rs.getString("delivery_day");		//납품일자
			String writer				= rs.getString("writer");			//작성자
			String written_day			= rs.getString("written_day");		//작성일자
			String cut_unit				= rs.getString("cut_unit");			//단위절사
			String special_change		= rs.getString("special_change");	//특별할인율
			String total_amount			= getTotalAmount(estimate_no,version,cut_unit,special_change);//견적가 합계

			String subj_link = "";
			if(mode.equals("list")) subj_link = "<A HREF='EstimateMgrServlet?mode=view";
			else if(mode.equals("mylist")) subj_link = "<A HREF='EstimateMgrServlet?mode=view_my";

			subj_link += "&page="+page+"&searchword="+searchword;
			subj_link += "&searchscope="+searchscope+"&category="+category;
			subj_link += "&estimate_no="+estimate_no+"&ver="+version+"'>";
			subj_link = subj_link + estimate_subj + "</a>";

			estimate = new EstimateInfoTable();

			estimate.setMid(mid);
			estimate.setCompanyName(company_name);
			estimate.setEstimateNo(estimate_no);
			estimate.setVersion(version);
			estimate.setEstimateSubj(subj_link);
			estimate.setDeliveryDay(delivery_day);
			estimate.setWrittenDay(written_day);
			estimate.setWriter(writer);
			estimate.setTotalAmount(total_amount);

			estimate_list.add(estimate);
			recNum--;
		}
		stmt.close();
		rs.close();

		return estimate_list;
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

	/*********************************************************
	 * 선택된 견적번호 및 버젼의 견적 합계금액을 계산하여 리턴
	 *********************************************************/
	public String getTotalAmount(String estimate_no, String version) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String total_amount = "0";

		String query = "SELECT SUM(estimate_value) FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()) total_amount = rs.getString(1);
		
		stmt.close();
		rs.close();

		//특별조정금액을 가져온다.
		String special_change = getSpecialChange(estimate_no,version);

		if(special_change.equals("0")){
			total_amount = total_amount;
		}
		else{
			total_amount = Double.toString(Double.parseDouble(total_amount) - Double.parseDouble(total_amount) * Integer.parseInt(special_change) / 100);
			
		}

		return total_amount;
	}

	/*********************************************************
	 * 선택된 견적번호 및 버젼의 견적 합계금액을 계산하여 리턴
	 *********************************************************/
	public String getTotalAmount(String estimate_no, String version,String cut_unit,String special_change) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		double total_amount = 0.0;
		String result = "";

		String query = "SELECT estimate_value FROM estimate_item_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		
		DecimalFormat fmt = new java.text.DecimalFormat("#");
		while(rs.next()){
			String value = fmt.format(Double.parseDouble(rs.getString("estimate_value")));

			if(cut_unit.equals("1")){
				value = value.substring(0,value.length()-1) + "0";
			}
			else if(cut_unit.equals("10")){
				value = value.substring(0,value.length()-2) + "00";		
			}
			else if(cut_unit.equals("100")){
				value = value.substring(0,value.length()-3) + "000";		
			}
			else if(cut_unit.equals("1000")){
				value = value.substring(0,value.length()-4) + "0000";		
			}
			else if(cut_unit.equals("10000")){
				value = value.substring(0,value.length()-5) + "00000";		
			}

			total_amount += Double.parseDouble(value);
		}
		
		stmt.close();
		rs.close();

		if(special_change.equals("0")){
			result = Double.toString(total_amount);
		}
		else{
			result = Double.toString(total_amount - total_amount * Integer.parseInt(special_change) / 100);
			
		}

		return result;
	}

	/*******************************************************************
	 * 선택된 견적번호 및 버젼에 해당하는 견적정보를 가져온다.
	 *******************************************************************/
	public EstimateInfoTable getEstimateInfo(String estimate_no,String version) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		com.anbtech.em.entity.EstimateInfoTable table = new com.anbtech.em.entity.EstimateInfoTable();
		com.anbtech.em.business.EstimateBO estimateBO = new com.anbtech.em.business.EstimateBO(con);

		String query = "SELECT * FROM estimate_info WHERE estimate_no = '" + estimate_no +"' and version = '" + version + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();

		table.setMid(rs.getString("mid"));
		table.setCompanyName(rs.getString("company_name"));

		table.setEstimateSubj(rs.getString("estimate_subj"));
		table.setEstimateNo(rs.getString("estimate_no"));
		table.setVersion(rs.getString("version"));
		table.setAid(rs.getString("aid"));

		table.setChargeName(rs.getString("charge_name"));
		table.setChargeRank(rs.getString("charge_rank"));
		table.setChargeDiv(rs.getString("charge_div"));
		table.setChargeTel(rs.getString("charge_tel"));
		table.setChargeMobile(rs.getString("charge_mobile"));
		table.setChargeFax(rs.getString("charge_fax"));
		table.setChargeEmail(rs.getString("charge_email"));
		table.setDeliveryPlace(rs.getString("delivery_place"));
		table.setPaymentTerms(rs.getString("payment_terms"));
		table.setGuaranteeTerm(rs.getString("guarantee_term"));
		table.setDeliveryPeriod(rs.getString("delivery_period"));
		table.setValidPeriod(rs.getString("valid_period"));
		table.setDeliveryDay(rs.getString("delivery_day"));
		table.setWrittenDay(rs.getString("written_day"));
		table.setWriter(rs.getString("writer"));
		table.setLastUpdatedDay(rs.getString("last_updated_day"));
		table.setLastModifier(rs.getString("last_modifier"));
		table.setSpecialChange(rs.getString("special_change"));
		table.setCutUnit(rs.getString("cut_unit"));

		String total_amount = getTotalAmount(estimate_no,version,rs.getString("cut_unit"),rs.getString("special_change"));
		String special_info = "";
		if(!rs.getString("special_change").equals("0")) special_info = "<font color=red> (특별할인 " + rs.getString("special_change") + "% 적용)</font>";
				
		table.setTotalAmount(total_amount);
		table.setSpecialInfo(special_info);

		Iterator ver_iter = getVerList(estimate_no).iterator();
		int j = 1;
		String ver_list = "<select name='ver' onChange='javascript:document.viewForm.submit();'>";

		while(ver_iter.hasNext()){
			String ver = (String)ver_iter.next();
			ver_list += "<option value='"+ver+"' ";
			if(ver.equals(version)) ver_list += "selected ";
			ver_list += ">"+ver+"</option>";
			j++;
		}
		ver_list += "</select>";

		table.setVersionList(ver_list);

		stmt.close();
		rs.close();

		return table;
	}

	/*****************************************************************
	 * 선택된 견적건에 존재하는 버젼 리스트를 가져와 리턴한다.
	 *****************************************************************/
	public ArrayList getVerList(String estimate_no) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		ArrayList ver_list = new ArrayList();

		String query = "SELECT version FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and stat = '3'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){ 
			ver_list.add(rs.getString("version"));
		}
		stmt.close();
		rs.close();

		return ver_list;
	} //getVerList()

	/*******************************
	 * 현재 견적번호로 승인완료되지 않은 견적의 건수 구하기
	 *******************************/
	public int getRevisioningCount(String estimate_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and stat = '1'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		
		while(rs.next()) total_count = Integer.parseInt(rs.getString(1));

		stmt.close();
		rs.close();
		
		return total_count;
	}

	/*******************************
	 * 견적정보 복사 처리
	 *******************************/
	public void copyEstimateInfo(String old_estimate_no,String old_version,String new_estimate_no,String new_version,String login_id) throws Exception{
		Statement stmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = login_id + " " + user_rank + " " + user_name;

		String query = "INSERT INTO estimate_info (company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,written_day,writer,last_updated_day,last_modifier,stat,charge_mobile,special_change,cut_unit) ";

		query += "SELECT company_name,estimate_subj,'"+new_estimate_no+"','"+new_version+"',charge_name,charge_rank,charge_div,charge_tel,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,'"+w_time+"','"+writer_info+"','"+w_time+"','"+writer_info+"','1',charge_mobile,special_change,cut_unit FROM estimate_info WHERE estimate_no = '"+old_estimate_no+"' and version = '"+old_version+"'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 견적 품목 복사 처리
	 *******************************/	
	public void copyEstimateItem(String mid,String new_estimate_no,String new_version) throws Exception{
		Statement stmt = null;

		String query = "INSERT INTO estimate_item_info (estimate_no,version,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value) ";

		query += "SELECT '" + new_estimate_no + "','" + new_version + "',item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value FROM estimate_item_info WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 현재 승인번호의 최대 버젼값 구하기
	 *******************************/
	public String getMaxVersion(String estimate_no) throws Exception{
		Statement stmt	= null;
		ResultSet rs	= null;
		String version	= "";

		String query = "SELECT MAX(version) FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and stat = '3'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		version = rs.getString(1);
		stmt.close();
		rs.close();

		return version;
	}

	/*******************************
	 * 견적정보 등록처리
	 *******************************/
	public void saveEstimateInfo(String company_name,String estimate_subj,String estimate_no,String version,String charge_name,String charge_rank,String charge_div,String charge_tel,String charge_mobile,String charge_fax,String charge_email,String delivery_place,String payment_terms,String guarantee_term,String delivery_period,String valid_period,String delivery_day,String login_id) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO estimate_info(company_name,estimate_subj,estimate_no,version,charge_name,charge_rank,charge_div,charge_tel,charge_mobile,charge_fax,charge_email,delivery_place,payment_terms,guarantee_term,delivery_period,valid_period,delivery_day,written_day,writer,last_updated_day,last_modifier,special_change,cut_unit,stat,aid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = login_id + " " + user_rank + " " + user_name;

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,company_name);
		pstmt.setString(2,estimate_subj);
		pstmt.setString(3,estimate_no);
		pstmt.setString(4,version);
		pstmt.setString(5,charge_name);
		pstmt.setString(6,charge_rank);
		pstmt.setString(7,charge_div);
		pstmt.setString(8,charge_tel);
		pstmt.setString(9,charge_mobile);
		pstmt.setString(10,charge_fax);
		pstmt.setString(11,charge_email);
		pstmt.setString(12,delivery_place);
		pstmt.setString(13,payment_terms);
		pstmt.setString(14,guarantee_term);
		pstmt.setString(15,delivery_period);
		pstmt.setString(16,valid_period);
		pstmt.setString(17,delivery_day);
		pstmt.setString(18,w_time);
		pstmt.setString(19,writer_info);
		pstmt.setString(20,"");
		pstmt.setString(21,"");
		pstmt.setString(22,"0");
		pstmt.setString(23,"0");
		pstmt.setString(24,"1");
		pstmt.setString(25,"");

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 견적정보 수정 처리
	 *******************************/
	public void updEstimateInfo(String company_name,String estimate_subj,String estimate_no,String version,String charge_name,String charge_rank,String charge_div,String charge_tel,String charge_mobile,String charge_fax,String charge_email,String delivery_place,String payment_terms,String guarantee_term,String delivery_period,String valid_period,String delivery_day,String login_id) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = login_id + " " + user_rank + " " + user_name;

		query = "UPDATE estimate_info SET company_name=?,estimate_subj=?,charge_name=?,charge_rank=?,charge_div=?,charge_tel=?,charge_mobile=?,charge_fax=?,charge_email=?,delivery_place=?,payment_terms=?,guarantee_term=?,delivery_period=?,valid_period=?,delivery_day=?,last_updated_day=?,last_modifier=? WHERE estimate_no='"+estimate_no+"' and version='"+version+"'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,company_name);
		pstmt.setString(2,estimate_subj);
		pstmt.setString(3,charge_name);
		pstmt.setString(4,charge_rank);
		pstmt.setString(5,charge_div);
		pstmt.setString(6,charge_tel);
		pstmt.setString(7,charge_mobile);
		pstmt.setString(8,charge_fax);
		pstmt.setString(9,charge_email);
		pstmt.setString(10,delivery_place);
		pstmt.setString(11,payment_terms);
		pstmt.setString(12,guarantee_term);
		pstmt.setString(13,delivery_period);
		pstmt.setString(14,valid_period);
		pstmt.setString(15,delivery_day);
		pstmt.setString(16,w_time);
		pstmt.setString(17,writer_info);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 특별할인 및 금액 절사 저장
	 *******************************/
	public void updateSpecialChange(String estimate_no,String version,String special_change,String cut_unit) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE estimate_info SET special_change=?,cut_unit=? WHERE estimate_no='" + estimate_no + "' and version = '" + version + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,special_change);
		pstmt.setString(2,cut_unit);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/***********************************************************
	 * 선택된 관리번호에 해당하는 견적품목 정보를 가져온다.
	 ***********************************************************/
	public ItemInfoTable getEstimateItemInfo(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		ItemInfoTable item = new ItemInfoTable();

		String sql = "SELECT * FROM estimate_item_info WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String estimate_no		= rs.getString("estimate_no");
			String version			= rs.getString("version");
			String item_class		= rs.getString("item_class");
			String item_name		= rs.getString("item_name");
			String model_code		= rs.getString("model_code");
			String model_name		= rs.getString("model_name");
			String quantity			= rs.getString("quantity");
			String unit				= rs.getString("unit");
			String standards		= rs.getString("standards");
			String maker_name		= rs.getString("maker_name");
			String supplyer_code	= rs.getString("supplyer_code");
			String supplyer_name	= rs.getString("supplyer_name");
			String buying_cost		= rs.getString("buying_cost");
			String gains_percent	= rs.getString("gains_percent");
			String gains_value		= rs.getString("gains_value");
			String supply_cost		= rs.getString("supply_cost");
			String discount_percent	= rs.getString("discount_percent");
			String discount_value	= rs.getString("discount_value");
			String tax_percent		= rs.getString("tax_percent");
			String tax_value		= rs.getString("tax_value");
			String estimate_value	= rs.getString("estimate_value");

			item = new ItemInfoTable();

			item.setMid(mid);
			item.setEstimateNo(estimate_no);
			item.setVersion(version);
			item.setItemClass(item_class);
			item.setItemName(item_name);
			item.setModelCode(model_code);
			item.setModelName(model_name);
			item.setQuantity(quantity);
			item.setUnit(unit);
			item.setStandards(standards);
			item.setMakerName(maker_name);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setBuyingCost(buying_cost);
			item.setGainsPercent(gains_percent);
			item.setGainsValue(gains_value);
			item.setSupplyCost(supply_cost);
			item.setDiscountPercent(discount_percent);
			item.setDiscountValue(discount_value);
			item.setTaxPercent(tax_percent);
			item.setTaxValue(tax_value);
			item.setEstimateValue(estimate_value);
		}
		stmt.close();
		rs.close();
		
		return item;
	}

	/*******************************
	 * 견적 품목 추가 처리
	 *******************************/	
	public void addEstimateItem(String estimate_no,String version,String item_class,String item_name,String model_code,String model_name,String quantity,String unit,String standards,String maker_name,String supplyer_code,String supplyer_name,String buying_cost,String gains_percent,String gains_value,String supply_cost,String discount_percent,String discount_value,String tax_percent,String tax_value,String estimate_value) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO estimate_item_info(estimate_no,version,item_class,item_name,model_code,model_name,quantity,unit,standards,maker_name,supplyer_code,supplyer_name,buying_cost,gains_percent,gains_value,supply_cost,discount_percent,discount_value,tax_percent,tax_value,estimate_value) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,estimate_no);
		pstmt.setString(2,version);
		pstmt.setString(3,item_class);
		pstmt.setString(4,item_name);
		pstmt.setString(5,model_code);
		pstmt.setString(6,model_name);
		pstmt.setString(7,quantity);
		pstmt.setString(8,unit);
		pstmt.setString(9,standards);
		pstmt.setString(10,maker_name);
		pstmt.setString(11,supplyer_code);
		pstmt.setString(12,supplyer_name);
		pstmt.setDouble(13,Double.parseDouble(buying_cost));
		pstmt.setInt(14,Integer.parseInt(gains_percent));
		pstmt.setDouble(15,Double.parseDouble(gains_value));
		pstmt.setDouble(16,Double.parseDouble(supply_cost));
		pstmt.setInt(17,Integer.parseInt(discount_percent));
		pstmt.setDouble(18,Double.parseDouble(discount_value));
		pstmt.setInt(19,Integer.parseInt(tax_percent));
		pstmt.setDouble(20,Double.parseDouble(tax_value));
		pstmt.setDouble(21,Double.parseDouble(estimate_value));

		pstmt.executeUpdate();
		pstmt.close();
	
	}

	/*******************************
	 * 견적품목 수정 처리
	 *******************************/
	public void modifyEstimateItem(String mid,String item_class,String item_name,String model_code,String model_name,String quantity,String unit,String standards,String maker_name,String supplyer_code,String supplyer_name,String buying_cost,String gains_percent,String gains_value,String supply_cost,String discount_percent,String discount_value,String tax_percent,String tax_value,String estimate_value) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE estimate_item_info SET item_class=?,item_name=?,model_code=?,model_name=?,quantity=?,unit=?,standards=?,maker_name=?,supplyer_code=?,supplyer_name=?,buying_cost=?,gains_percent=?,gains_value=?,supply_cost=?,discount_percent=?,discount_value=?,tax_percent=?,tax_value=?,estimate_value=? WHERE mid='" + mid + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_class);
		pstmt.setString(2,item_name);
		pstmt.setString(3,model_code);
		pstmt.setString(4,model_name);
		pstmt.setString(5,quantity);
		pstmt.setString(6,unit);
		pstmt.setString(7,standards);
		pstmt.setString(8,maker_name);
		pstmt.setString(9,supplyer_code);
		pstmt.setString(10,supplyer_name);
		pstmt.setDouble(11,Double.parseDouble(buying_cost));
		pstmt.setInt(12,Integer.parseInt(gains_percent));
		pstmt.setDouble(13,Double.parseDouble(gains_value));
		pstmt.setDouble(14,Double.parseDouble(supply_cost));
		pstmt.setInt(15,Integer.parseInt(discount_percent));
		pstmt.setDouble(16,Double.parseDouble(discount_value));
		pstmt.setInt(17,Integer.parseInt(tax_percent));
		pstmt.setDouble(18,Double.parseDouble(tax_value));
		pstmt.setDouble(19,Double.parseDouble(estimate_value));

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * 견적품목 삭제 처리(by 관리번호)
	 *******************************/
	public void deleteEstimateItemInfo(String mid) throws Exception{
		Statement stmt = null;
		String query = "DELETE estimate_item_info WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}


	/*********************************************************
	 * 선택된 견적번호 및 버젼의 특별 조정금액을 계산하여 리턴
	 *********************************************************/
	public String getSpecialChange(String estimate_no, String version) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String special_change = "";

		String query = "SELECT special_change FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		special_change = rs.getString("special_change");
		
		stmt.close();
		rs.close();

		return special_change;
	}

	/**********************************
	 * 현재 카테고리의 상위 분류 문자열 가져오기
	 **********************************/
	public String viewCategory(String c_class,String category,String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT c_name,c_ancestor FROM category_info WHERE c_class = '" + c_class + "' and c_id = '" + category + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String c_name = rs.getString("c_name");
			String c_ancestor = rs.getString("c_ancestor");

			if(whereStr.equals("")){
				whereStr = " " + c_name;
			} else {
				whereStr = c_name+" > " + where;
			}
			viewCategory(c_class,c_ancestor,whereStr);
		}
		
		return whereStr;
	}//viewCategory()


	/**********************************
	 * 견적번호,버젼에 해당하는 견적건의 단위절사값 가져오기
	 **********************************/
	public String getCutUnit(String estimate_no,String version) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT cut_unit FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();
		String cut_unit = rs.getString("cut_unit");
		
		return cut_unit;
	}//getCutUnit()


	/*******************************
	 * 상태코드 변경
	 *******************************/
	public void updStat(String estimate_no,String version,String stat) throws Exception{

		Statement stmt = null;
		String query= "";

		query = "UPDATE estimate_info SET stat = '" + stat + "' WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 전자결재 번호 변경
	 *******************************/
	public void updAid(String estimate_no,String version,String aid) throws Exception{

		Statement stmt = null;
		String query= "";

		query = "UPDATE estimate_info SET aid = '" + aid + "' WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 견적번호 업데이트
	 *******************************/
	public void updEstimateNo(String old_estimate_no,String version,String tablename,String new_estimate_no) throws Exception{

		Statement stmt = null;
		String query= "";

		query = "UPDATE " + tablename + " SET estimate_no = '" + new_estimate_no + "' WHERE estimate_no = '" + old_estimate_no + "' and version = '" + version + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}


	/*****************************************************************************
	 * 전자결재모듈의 app_save 테이블에서 선택된 pid의 레코드 정보를 가져온 뒤,
	 * 승인원 관리모듈의 approval_info 테이블에 입력한다.
	 *****************************************************************************/
	public void getAppInfoAndSave(String pid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM app_save WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			query = "INSERT INTO em_approval_info (pid,writer,writer_name,writer_div,writer_rank,write_date,";
			query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
			query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
			query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
			query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
			query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
			query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
			query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date) ";
			query += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1,pid);
			pstmt.setString(2,rs.getString("writer"));
			pstmt.setString(3,rs.getString("writer_name"));
			pstmt.setString(4,rs.getString("writer_div"));
			pstmt.setString(5,rs.getString("writer_rank"));
			pstmt.setString(6,rs.getString("write_date"));
			pstmt.setString(7,rs.getString("reviewer"));
			pstmt.setString(8,rs.getString("reviewer_name"));
			pstmt.setString(9,rs.getString("reviewer_div"));
			pstmt.setString(10,rs.getString("reviewer_rank"));
			pstmt.setString(11,rs.getString("review_comment"));
			pstmt.setString(12,rs.getString("review_date"));
			pstmt.setString(13,rs.getString("decision"));
			pstmt.setString(14,rs.getString("decision_name"));
			pstmt.setString(15,rs.getString("decision_div"));
			pstmt.setString(16,rs.getString("decision_rank"));
			pstmt.setString(17,rs.getString("decision_comment"));
			pstmt.setString(18,rs.getString("decision_date"));
			pstmt.setString(19,rs.getString("agree"));
			pstmt.setString(20,rs.getString("agree_name"));
			pstmt.setString(21,rs.getString("agree_div"));
			pstmt.setString(22,rs.getString("agree_rank"));
			pstmt.setString(23,rs.getString("agree_comment"));
			pstmt.setString(24,rs.getString("agree_date"));
			pstmt.setString(25,rs.getString("agree2"));
			pstmt.setString(26,rs.getString("agree2_name"));
			pstmt.setString(27,rs.getString("agree2_div"));
			pstmt.setString(28,rs.getString("agree2_rank"));
			pstmt.setString(29,rs.getString("agree2_comment"));
			pstmt.setString(30,rs.getString("agree2_date"));
			pstmt.setString(31,rs.getString("agree3"));
			pstmt.setString(32,rs.getString("agree3_name"));
			pstmt.setString(33,rs.getString("agree3_div"));
			pstmt.setString(34,rs.getString("agree3_rank"));
			pstmt.setString(35,rs.getString("agree3_comment"));
			pstmt.setString(36,rs.getString("agree3_date"));
			pstmt.setString(37,rs.getString("agree4"));
			pstmt.setString(38,rs.getString("agree4_name"));
			pstmt.setString(39,rs.getString("agree4_div"));
			pstmt.setString(40,rs.getString("agree4_rank"));
			pstmt.setString(41,rs.getString("agree4_comment"));
			pstmt.setString(42,rs.getString("agree4_date"));
			pstmt.setString(43,rs.getString("agree5"));
			pstmt.setString(44,rs.getString("agree5_name"));
			pstmt.setString(45,rs.getString("agree5_div"));
			pstmt.setString(46,rs.getString("agree5_rank"));
			pstmt.setString(47,rs.getString("agree5_comment"));
			pstmt.setString(48,rs.getString("agree5_date"));
		
			pstmt.executeUpdate();
			pstmt.close();
		}
		stmt.close();
		rs.close();
	}

	/*******************************************************************************
	 * 정식승인번호를 계산하여 리턴한다.
	 *******************************************************************************/
	public String calculateEstimateNo() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMM");
		String w_time = vans.format(now);

		String sql = "SELECT MAX(estimate_no) FROM estimate_info WHERE stat = '3' and written_day like '" + w_time + "%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		String estimate_no = "";
		while(rs.next()){
			if(rs.getString(1) == null){
				estimate_no = w_time + "-001";
			}
			else{
				estimate_no = w_time + "-" + fmt.format(Integer.parseInt(rs.getString(1).substring(8,10)) + 1);
			}
		}
		return estimate_no;
	}

	/**********************************
	 * 선택된 견적번호,버젼에 해당하는 견적건의 aid(전자결재번호) 값 가져오기
	 **********************************/
	public String getAid(String estimate_no,String version) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT aid FROM estimate_info WHERE estimate_no = '" + estimate_no + "' and version = '" + version + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();
		String aid = rs.getString("aid");
		
		return aid;
	}//getAid()

}		