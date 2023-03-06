package com.anbtech.qc.db;

import com.anbtech.qc.entity.*;
import com.anbtech.qc.business.*;
import com.anbtech.qc.db.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class QualityCtrlDAO{
	private Connection con;

	public QualityCtrlDAO(Connection con){
		this.con = con;
	}

	/************************************
	 * �˻����ǿ� �´� �˻��Ƿ� �� ��� ��������
	 ************************************/
	public ArrayList getInspectList(String mode,String searchword,String searchscope,String category,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���
		int current_page_num =Integer.parseInt(page);

		ArrayList list = new ArrayList();
		QualityCtrlBO qcBO = new QualityCtrlBO(con);
		InspectionMasterTable table = new InspectionMasterTable();
		
		String where = qcBO.getWhere(mode,searchword, searchscope, category,login_id);

		int total = getTotalCount("qc_inspection_master", where);
		int recNum = total;

		String query = "SELECT * FROM qc_inspection_master " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");
			String request_no			= rs.getString("request_no");
			String item_code			= rs.getString("item_code");
			String item_name			= rs.getString("item_name");
			String item_desc			= rs.getString("item_desc");
			String supplyer_code		= rs.getString("supplyer_code");
			String supplyer_name		= rs.getString("supplyer_name");
			String lot_no				= rs.getString("lot_no");
			String lot_quantity			= rs.getString("lot_quantity");
			String inspected_quantity	= rs.getString("inspected_quantity");
			String bad_item_quantity	= rs.getString("bad_item_quantity");
			String inspection_result	= rs.getString("inspection_result");
			String process_stat			= rs.getString("process_stat");
			String request_date			= rs.getString("request_date");
			String requester_id			= rs.getString("requester_id");
			String requester_info		= rs.getString("requester_info");
			String requester_div_code	= rs.getString("requester_div_code");
			String requester_div_name	= rs.getString("requester_div_name");
			String inspect_date			= rs.getString("inspect_date");
			String inspector_id			= rs.getString("inspector_id");
			String inspector_info		= rs.getString("inspector_info");
			String inspector_div_code	= rs.getString("inspector_div_code");
			String inspector_div_name	= rs.getString("inspector_div_name");
			String other_info			= rs.getString("other_info");
			String bad_percentage		= rs.getString("bad_percentage");
			String factory_code			= rs.getString("factory_code");
			String factory_name			= rs.getString("factory_name");
			String item_type			= rs.getString("item_type");

			String link = "";
			if(mode.equals("list_inspect")) link += "<a href = 'QualityCtrlServlet?mode=write_inspect";
			else if(mode.equals("list_result")) link += "<a href = 'QualityCtrlServlet?mode=view_result";
			else if(mode.equals("list_return")) link += "<a href = 'QualityCtrlServlet?mode=write_return";
			else if(mode.equals("list_rework")) link += "<a href = 'QualityCtrlServlet?mode=write_rework";
			
			link += "&page="+page+"&searchword="+searchword;
			link += "&searchscope="+searchscope+"&category="+category;
			link += "&request_no="+request_no+"&item_code="+item_code+"' ";
			link += "onMouseOver=\"window.status='����������';return true;\" onMouseOut=\"window.status='';return true;\">";
			link = link + request_no + "</a>";

			table = new InspectionMasterTable();

			table.setMid(mid);
			table.setRequestNo(link);
			table.setItemCode(item_code);
			table.setItemName(item_name);
			table.setItemDesc(item_desc);
			table.setSupplyerCode(supplyer_code);
			table.setSupplyerName(supplyer_name);
			table.setLotNo(lot_no);
			table.setLotQuantity(lot_quantity);
			table.setInspectedQuantity(inspected_quantity);
			table.setBadItemQuantity(bad_item_quantity);
			table.setInspectionResult(inspection_result);
			table.setProcessStat(process_stat);
			table.setRequestDate(request_date);
			table.setRequesterId(requester_id);
			table.setRequesterInfo(requester_info);
			table.setRequesterDivCode(requester_div_code);
			table.setRequesterDivName(requester_div_name);
			table.setInspectDate(inspect_date);
			table.setInspectorId(inspector_id);
			table.setInspectorInfo(inspector_info);
			table.setInspectorDivCode(inspector_div_code);
			table.setInspectorDivName(inspector_div_name);
			table.setOtherInfo(other_info);
			table.setBadPercentage(bad_percentage);
			table.setFactoryCode(factory_code);
			table.setFactoryName(factory_name);
			table.setItemType(item_type);

			list.add(table);
			recNum--;
		}
		stmt.close();
		rs.close();

		return list;
	}

	/*******************************
	 * ���ڵ��� ��ü ������ ���Ѵ�.
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

	/*********************************************
	 * �˻�ǰ������ ��������
	 *********************************************/
	public InspectionMasterTable getInspectionInfo(String request_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		InspectionMasterTable table = new InspectionMasterTable();

		String sql = "SELECT * FROM qc_inspection_master WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setRequestNo(rs.getString("request_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setLotNo(rs.getString("lot_no"));
			table.setLotQuantity(rs.getString("lot_quantity"));
			table.setInspectedQuantity(rs.getString("inspected_quantity"));
			table.setBadItemQuantity(rs.getString("bad_item_quantity"));
			table.setInspectionResult(rs.getString("inspection_result"));
			table.setProcessStat(rs.getString("process_stat"));
			table.setRequestDate(rs.getString("request_date"));
			table.setRequesterId(rs.getString("requester_id"));
			table.setRequesterInfo(rs.getString("requester_info"));
			table.setRequesterDivCode(rs.getString("requester_div_code"));
			table.setRequesterDivName(rs.getString("requester_div_name"));
			table.setInspectDate(rs.getString("inspect_date"));
			table.setInspectorId(rs.getString("inspector_id"));
			table.setInspectorInfo(rs.getString("inspector_info"));
			table.setInspectorDivCode(rs.getString("inspector_div_code"));
			table.setInspectorDivName(rs.getString("inspector_div_name"));
			table.setOtherInfo(rs.getString("other_info"));
			table.setBadPercentage(rs.getString("bad_percentage"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setPreRequestNo(rs.getString("pre_request_no"));
			table.setItemType(rs.getString("item_type"));
			table.setSerialNoS(rs.getString("serial_no_s"));
			table.setSerialNoE(rs.getString("serial_no_e"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*******************************
	 * ǰ���˻��Ƿ����� ���� ó��(�������� ��)
	 *******************************/
	public void updInspectionInfo(String request_no,String item_code,String inspected_quantity,String bad_item_quantity,String inspection_result,String bad_percentage,String other_info,String login_id,String serial_no_s,String serial_no_e,String lot_no) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//��Ͻð� ���ϱ�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//�����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division_code = userinfo.getAcCode();
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = user_rank + " " + user_name;

		query = "UPDATE qc_inspection_master SET inspected_quantity=?,bad_item_quantity=?,inspection_result=?,process_stat=?,inspect_date=?,inspector_id=?,inspector_info=?,inspector_div_code=?,inspector_div_name=?,other_info=?,bad_percentage=?,serial_no_s=?,serial_no_e=?,lot_no=? WHERE request_no ='" + request_no + "' AND item_code = '" + item_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,inspected_quantity);
		pstmt.setString(2,bad_item_quantity);
		pstmt.setString(3,inspection_result);
		pstmt.setString(4,"S05");
		pstmt.setString(5,w_time);
		pstmt.setString(6,login_id);
		pstmt.setString(7,writer_info);
		pstmt.setString(8,division_code);
		pstmt.setString(9,division);
		pstmt.setString(10,other_info);
		pstmt.setString(11,bad_percentage);
		pstmt.setString(12,serial_no_s);
		pstmt.setString(13,serial_no_e);
		pstmt.setString(14,lot_no);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * ǰ���˻��Ƿ����� ���� ó��(���۾���� ��Ͻ�)
	 *******************************/
	public void updInspectionInfo(String request_no,String item_code,String other_info,String login_id,String lot_no) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//��Ͻð� ���ϱ�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//�����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division_code = userinfo.getAcCode();
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = user_rank + " " + user_name;

		query = "UPDATE qc_inspection_master SET process_stat=?,request_date=?,requester_id=?,requester_info=?,requester_div_code=?,requester_div_name=?,other_info=?,lot_no=? WHERE request_no ='" + request_no + "' AND item_code = '" + item_code + "'";
		System.out.println("<script>alert(query)</script>");
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,"S03");
		pstmt.setString(2,w_time);
		pstmt.setString(3,login_id);
		pstmt.setString(4,writer_info);
		pstmt.setString(5,division_code);
		pstmt.setString(6,division);
		pstmt.setString(7,other_info);
		pstmt.setString(8,lot_no);

		pstmt.executeUpdate();
		pstmt.close();
	}


	/*******************************
	 * ǰ���˻��Ƿ����� ���� ó��
	 * �����԰������� �����Ͽ��� ���� ǰ���˻��Ƿ���������
	 * lot_quantity �� �����Ѵ�.
	 *******************************/
	public void updInspectionInfo(String item_code,String lot_no,String lot_quantity) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE qc_inspection_master SET lot_quantity=? WHERE item_code ='" + item_code + "' AND lot_no = '" + lot_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,lot_quantity);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * ǰ���˻��Ƿ����� �����ϱ�
	 *******************************/
	public void deleteInspectionInfo(String item_code,String lot_no) throws Exception{
		Statement stmt = null;
		String query = "DELETE qc_inspection_master WHERE item_code = '" + item_code + "' AND lot_no = '" + lot_no + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*********************************
	 * �˻��Ƿ����� ����(�ʱ���)
	 *********************************/
	 public void saveInspectionInfo(String request_no,String item_code,String item_name,String item_desc,String supplyer_code,String supplyer_name,String lot_no,String lot_quantity,String requester_div_code,String requester_div_name,String requester_id,String requester_info,String factory_code,String factory_name,String process_stat) throws Exception{
		String pre_request_no = "";
		String serial_no_s = "";
		String serial_no_e = "";
		saveInspectionInfo(request_no,item_code,item_name,item_desc,supplyer_code,supplyer_name,lot_no,lot_quantity,requester_div_code,requester_div_name,requester_id,requester_info,factory_code,factory_name,process_stat,pre_request_no,serial_no_s,serial_no_e);
	}


	/*********************************
	 * �˻��Ƿ����� ����(��˻���)
	 *********************************/
	 public void saveInspectionInfo(String request_no,String item_code,String item_name,String item_desc,String supplyer_code,String supplyer_name,String lot_no,String lot_quantity,String requester_div_code,String requester_div_name,String requester_id,String requester_info,String factory_code,String factory_name,String process_stat,String pre_request_no,String serial_no_s,String serial_no_e) throws Exception{
		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
		PreparedStatement pstmt = null;
		
		String item_type = cmDAO.getItemType(item_code);
		String query = "INSERT INTO qc_inspection_master (request_no,reg_year,reg_month,reg_serial,item_code,item_name,item_desc,supplyer_code,supplyer_name,lot_no,lot_quantity,inspected_quantity,bad_item_quantity,inspection_result,process_stat,request_date,requester_id,requester_info,requester_div_code,requester_div_name,inspect_date,inspector_id,inspector_info,inspector_div_code,inspector_div_name,other_info,bad_percentage,factory_code,factory_name,pre_request_no,item_type,serial_no_s,serial_no_e) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		String reg_year = request_no.substring(3,5);
		String reg_month = request_no.substring(5,7);
		String reg_serial = request_no.substring(8,11);

		//��û��
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String request_date = vans.format(now);

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_no);
		pstmt.setString(2,reg_year);
		pstmt.setString(3,reg_month);
		pstmt.setString(4,reg_serial);
		pstmt.setString(5,item_code);
		pstmt.setString(6,item_name);
		pstmt.setString(7,item_desc);
		pstmt.setString(8,supplyer_code);
		pstmt.setString(9,supplyer_name);
		pstmt.setString(10,lot_no);
		pstmt.setString(11,lot_quantity);
		pstmt.setString(12,"0");
		pstmt.setString(13,"0");
		pstmt.setString(14,"");
		pstmt.setString(15,process_stat);
		pstmt.setString(16,request_date);
		pstmt.setString(17,requester_id);
		pstmt.setString(18,requester_info);
		pstmt.setString(19,requester_div_code);
		pstmt.setString(20,requester_div_name);
		pstmt.setString(21,"");
		pstmt.setString(22,"");
		pstmt.setString(23,"");
		pstmt.setString(24,"");
		pstmt.setString(25,"");
		pstmt.setString(26,"");
		pstmt.setString(27,"0");
		pstmt.setString(28,factory_code);
		pstmt.setString(29,factory_name);
		pstmt.setString(30,pre_request_no);
		pstmt.setString(31,item_type);
		pstmt.setString(32,serial_no_s);
		pstmt.setString(33,serial_no_e);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/****************************************************************
	 * �˻��Ƿڹ�ȣ�� �����Ͽ� �����Ѵ�.
	 ****************************************************************/
	public String getRequestNo(String no_type) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql			= "";
		String request_no	= "";
		String reg_serial	= "";

		//��û�ð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMMdd");
		String reg_year		= vans.format(now).substring(0,2);
		String reg_month	= vans.format(now).substring(2,4);
		
		sql = "SELECT MAX(reg_serial) FROM qc_inspection_master WHERE reg_year = '" + reg_year +"' AND reg_month = '" + reg_month + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("000");
		while(rs.next()){
			if(rs.getString(1) == null) reg_serial = "001";
			else reg_serial = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		request_no	= no_type + reg_year + reg_month + "-" + reg_serial;

		return request_no;
	}

	/************************************
	 * ���õ� ǰ���ڵ忡 ���ǵ� �˻��׸� ����Ʈ ��������
	 ************************************/
	public ArrayList getInspectionItemByItem(String mode,String item_code,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList list = new ArrayList();
		InspectionItemByItemTable table = new InspectionItemByItemTable();
		
		String query = "SELECT * FROM qc_inspection_item_by_item WHERE item_code = '" + item_code + "' ORDER BY inspection_order ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			String mid					= rs.getString("mid");
			String factory_code			= rs.getString("factory_code");
			String factory_name			= rs.getString("factory_name");
			String item_name			= rs.getString("item_name");
			String item_desc			= rs.getString("item_desc");
			String inspection_class_code= rs.getString("inspection_class_code");
			String inspection_class_name= rs.getString("inspection_class_name");
			String inspection_code		= rs.getString("inspection_code");
			String inspection_name		= rs.getString("inspection_name");
			String inspection_result_type = rs.getString("inspection_result_type");
			String inspection_order		= rs.getString("inspection_order");
			String inspection_type_code = rs.getString("inspection_type_code");
			String inspection_type_name = rs.getString("inspection_type_name");
			String inspection_grade		= rs.getString("inspection_grade");
			String low_standard			= rs.getString("low_standard");
			String upper_standard		= rs.getString("upper_standard");

			table = new InspectionItemByItemTable();

			table.setMid(mid);
			table.setFactoryCode(factory_code);
			table.setFactoryName(factory_name);
			table.setItemCode(item_code);
			table.setItemName(item_name);
			table.setItemDesc(item_desc);
			table.setInspectionClassCode(inspection_class_code);
			table.setInspectionClassName(inspection_class_name);
			table.setInspectionCode(inspection_code);
			table.setInspectionName(inspection_name);
			table.setInspectionResultType(inspection_result_type);
			table.setInspectionOrder(inspection_order);
			table.setInspectionTypeCode(inspection_type_code);
			table.setInspectionTypeName(inspection_type_name);
			table.setInspectionGrade(inspection_grade);
			table.setLowStandard(low_standard);
			table.setUpperStandard(upper_standard);

			list.add(table);
		}
		stmt.close();
		rs.close();

		return list;
	}

	/*******************************
	 * �˻��׸� ����
	 *******************************/
	public void saveInspectionResult(String request_no,String item_code,String inspection_code,String sampled_quantity,String good_quantity,String bad_quantity) throws Exception{
		Statement stmt = null;
		String query = "INSERT INTO qc_inspection_result (request_no,item_code,inspection_code,sampled_quantity,good_quantity,bad_quantity) VALUES('"+request_no+"','"+item_code+"','"+inspection_code+"','"+sampled_quantity+"','"+good_quantity+"','"+bad_quantity+"')";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * �˻��׸����� �����ϱ�
	 *******************************/
	public void deleteInspectionResult(String request_no,String item_code,String inspection_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE qc_inspection_result WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * �˻系������ ����
	 *******************************/
	public void saveInspectionValue(String request_no,String item_code,String inspection_code,String inspection_value) throws Exception{
		Statement stmt = null;
		String query = "INSERT INTO qc_inspection_value (request_no,item_code,inspection_code,inspection_value) VALUES('"+request_no+"','"+item_code+"','"+inspection_code+"','"+inspection_value+"')";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * �˻系������ �����ϱ�
	 *******************************/
	public void deleteInspectionValue(String request_no,String item_code,String inspection_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE qc_inspection_value WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*********************************************
	 * �˻��������� ��������
	 *********************************************/
	public InspectionResultTable getInspectionResult(String request_no,String item_code,String inspection_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		InspectionResultTable table = new InspectionResultTable();

		String sql = "SELECT * FROM qc_inspection_result WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setSampledQuantity(rs.getString("sampled_quantity"));
			table.setGoodQuantity(rs.getString("good_quantity"));
			table.setBadQuantity(rs.getString("bad_quantity"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * �˻系������ ��������
	 *********************************************/
	public String getInspectionValue(String request_no,String item_code,String inspection_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT inspection_value FROM qc_inspection_value WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		String inspection_value = "";
		while(rs.next()){
			inspection_value = rs.getString("inspection_value");
		}
		stmt.close();
		rs.close();

		return inspection_value;
	}

	/*********************************************
	 * ���õ� �׸��� �ҷ����� ��������
	 *********************************************/
	public FailureInfoTable getFailureInfo(String request_no,String item_code,String serial_no,String inspection_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		FailureInfoTable table = new FailureInfoTable();

		String mid			= "";
		String why_failure	= "";
		if(serial_no == null) serial_no = "";

		String sql = "SELECT * FROM qc_failure_info WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			mid				= rs.getString("mid");
			request_no		= rs.getString("request_no");
			item_code		= rs.getString("item_code");
			serial_no		= rs.getString("serial_no");
			inspection_code = rs.getString("inspection_code");
			why_failure		= rs.getString("why_failure");
		}

		table.setMid(mid);
		table.setRequestNo(request_no);
		table.setItemCode(item_code);
		table.setSerialNo(serial_no);
		table.setInspectionCode(inspection_code);
		table.setWhyFailure(why_failure);

		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * ���õ� �Ƿڹ�ȣ �� ǰ�� ���� �ҷ�����Ʈ ��������
	 *********************************************/
	public ArrayList getFailureList(String request_no,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		FailureInfoTable table = new FailureInfoTable();
		ArrayList list = new ArrayList();

		String sql = "SELECT * FROM qc_failure_info WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table = new FailureInfoTable();

			table.setMid(rs.getString("mid"));
			table.setRequestNo(rs.getString("request_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setSerialNo(rs.getString("serial_no"));
			table.setInspectionCode(rs.getString("inspection_code"));
			table.setInspectionName(rs.getString("inspection_name"));
			table.setWhyFailure(rs.getString("why_failure"));

			list.add(table);
		}
		stmt.close();
		rs.close();

		return list;
	}

	/*********************************************
	 * ���õ� �˻��׸��ڵ忡 ���� ���� ��������
	 *********************************************/
	public InspectionItemTable getInspectionItemInfo(String inspection_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		InspectionItemTable table = new InspectionItemTable();

		String sql = "SELECT * FROM qc_inspection_item WHERE inspection_code = '" + inspection_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table = new InspectionItemTable();

			table.setMid(rs.getString("mid"));
			table.setInspectionClassCode(rs.getString("inspection_class_code"));

			table.setInspectionClassName(rs.getString("inspection_class_name"));
			table.setInspectionCode(rs.getString("inspection_code"));
			table.setInspectionName(rs.getString("inspection_name"));
			table.setInspectionResultType(rs.getString("inspection_result_type"));
		}

		stmt.close();
		rs.close();

		return table;
	}

	/*********************************
	 * �˻��Ƿڹ�ȣ��,ǰ�� �ҷ����� ���� ����
	 *********************************/
	 public void saveFailureInfo(String request_no,String item_code,String serial_no,String inspection_code,String inspection_name,String why_failure) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO qc_failure_info (request_no,item_code,serial_no,inspection_code,inspection_name,why_failure) VALUES (?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,request_no);
		pstmt.setString(2,item_code);
		pstmt.setString(3,serial_no);
		pstmt.setString(4,inspection_code);
		pstmt.setString(5,inspection_name);
		pstmt.setString(6,why_failure);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * �ҷ����� ���� ó��
	 *********************************/
	 public void updateFailureInfo(String request_no,String item_code,String serial_no,String inspection_code,String inspection_name,String why_failure) throws Exception{
		Statement stmt = null;

		String query = "UPDATE qc_failure_info SET serial_no='" + serial_no + "',why_failure='" + why_failure + "' WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/*********************************
	 * �ҷ����� ���� ó��
	 *********************************/
	 public void deleteFailureInfo(String request_no,String item_code,String inspection_code) throws Exception{
		Statement stmt = null;

		String query = "DELETE qc_failure_info WHERE request_no = '" + request_no + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/*********************************************
	 * ��ǰ�� ���� �Ϸù�ȣ ��������
	 *********************************************/
	public String getGoodsSerialNoS(String produce_year,String produce_month,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String serial_no = "0001";

		String sql = "SELECT serial_no_e FROM qc_goods_serial_info WHERE produce_year = '" + produce_year + "' AND produce_month = '" + produce_month + "' AND item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("0000");
		while(rs.next()){
			serial_no = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		stmt.close();
		rs.close();

		return serial_no;
	}

	/*********************************
	 * ��ǰ�� �Ϸù�ȣ ���� ����
	 *********************************/
	 public void saveGoodsSerialInfo(String produce_year,String produce_month,String item_code,String model_code,String serial_no_s,String serial_no_e,String request_no) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO qc_goods_serial_info (item_code,produce_year,produce_month,model_code,serial_no_s,serial_no_e,request_no) VALUES (?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,produce_year);
		pstmt.setString(3,produce_month);
		pstmt.setString(4,model_code);
		pstmt.setString(5,serial_no_s);
		pstmt.setString(6,serial_no_e);
		pstmt.setString(7,request_no);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/************************************
	 * �˻����ǿ� �´� �ҷ����� ��������
	 * v_failure_info �� ���̺� �˻�
	 ************************************/
	public ArrayList getFailureInfoList(String mode,String searchword,String searchscope,String page,String login_id) throws Exception {
		QualityCtrlBO qcBO		= new QualityCtrlBO(con);
		FailureInfoTable table	= new FailureInfoTable();

		Statement stmt = null;
		ResultSet rs = null;

		ArrayList failure_list = new ArrayList();
		String where = qcBO.getWhereForFailureInfoList(mode,searchword,login_id);

		String query = "SELECT * FROM v_failure_info " + where + " ORDER BY inspection_code ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new FailureInfoTable();

//			table.setMid(rs.getString("mid"));
			table.setRequestNo(rs.getString("request_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setSerialNo(rs.getString("serial_no"));
			table.setInspectionCode(rs.getString("inspection_code"));
			table.setInspectionName(rs.getString("inspection_name"));
			table.setWhyFailure(rs.getString("why_failure"));
			table.setModelCode(rs.getString("model_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setSupplyerCode(rs.getString("supplyer_code"));
			table.setSupplyerName(rs.getString("supplyer_name"));
			table.setLotNo(rs.getString("lot_no"));
			table.setLotQuantity(rs.getString("lot_quantity"));
//			table.setInspectedQuantity(rs.getString("inspected_quantity"));
//			table.setBadItemQuantity(rs.getString("bad_item_quantity"));
//			table.setInspectionResult(rs.getString("inspection_result"));
//			table.setProcessStat(rs.getString("process_stat"));
			table.setRequestDate(rs.getString("request_date"));
			table.setRequesterId(rs.getString("requester_id"));
			table.setRequesterInfo(rs.getString("requester_info"));
			table.setRequesterDivCode(rs.getString("requester_div_code"));
			table.setRequesterDivName(rs.getString("requester_div_name"));
			table.setInspectDate(rs.getString("inspect_date"));
			table.setInspectorId(rs.getString("inspector_id"));
			table.setInspectorInfo(rs.getString("inspector_info"));
			table.setInspectorDivCode(rs.getString("inspector_div_code"));
			table.setInspectorDivName(rs.getString("inspector_div_name"));
//			table.setOtherInfo(rs.getString("other_info"));
//			table.setBadPercentage(rs.getString("bad_percentage"));
			table.setFactoryCode(rs.getString("factory_code"));
			table.setFactoryName(rs.getString("factory_name"));
//			table.setPreRequestNo(rs.getString("pre_request_no"));
			table.setItemType(rs.getString("item_type"));
//			table.setSerialNoS(rs.getString("serial_no_s"));
//			table.setSerialNoE(rs.getString("serial_no_e"));

			failure_list.add(table);
		}
		stmt.close();
		rs.close();

		return failure_list;
	}
}		