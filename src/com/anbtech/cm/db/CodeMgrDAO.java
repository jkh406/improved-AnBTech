package com.anbtech.cm.db;

import com.anbtech.cm.entity.*;
import com.anbtech.cm.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class CodeMgrDAO{
	private Connection con;

	public CodeMgrDAO(Connection con){
		this.con = con;
	}

	/************************************
	 * �˻����ǿ� �´� ǰ�� ����Ʈ ��������
	 ************************************/
	public ArrayList getListItem(String mode,String category,String code_b,String code_m,String code_s,String searchword,String searchscope,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;			// ������������ ����� ���ڵ� ��
		if(mode.equals("list_item_p")) l_maxlist = 10;
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���
		int current_page_num = Integer.parseInt(page);

		ArrayList list_item = new ArrayList();
		CodeMgrBO cmBO = new CodeMgrBO(con);
		PartInfoTable partinfo = new PartInfoTable();
		
		String where = cmBO.getWhere(mode,category,code_b,code_m,code_s,searchword,searchscope,login_id);

		int total = getTotalCount("item_master", where);
		int recNum = total;

		String query = "SELECT * FROM item_master " + where + " ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid					= rs.getString("mid");				//���� ��ȣ
			String item_no				= rs.getString("item_no");			//ǰ�� �ڵ�
			String item_desc			= rs.getString("item_desc");		//ǰ�� ����
			String register_id			= rs.getString("register_id");		//����� ���
			String register_info		= rs.getString("register_info");	//����� ����
			String register_date		= rs.getString("register_date");	//�������
			String code_type			= rs.getString("code_type");		//ǰ�� Ÿ��
			String item_name			= rs.getString("item_name");		//ǰ���
			String stock_unit			= rs.getString("stock_unit");		//ǰ�� ����
			String item_type			= rs.getString("item_type");		//ǰ�� ����

			String item_no_link = item_no;
		
			String temp_mode = "";
		
			if(code_type != null) temp_mode = "view_item2";
			else temp_mode = "view_item";

				item_no_link = "<a href='CodeMgrServlet?mode="+temp_mode+"&category="+category+"&no=" + mid + "&item_no=" + item_no;
				item_no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
				item_no_link += "onMouseOver=\"window.status='ǰ������ �󼼺��� #" + item_no + "';return true;\" ";
				item_no_link += "onMouseOut=\"window.status='';return true;\">" + item_no + "</a>";
			
			if(mode.equals("list_item_p")){
				item_no_link = "<a href=\"javascript:returnValue('"+item_no+"','"+item_desc+"','"+item_name+"','"+stock_unit+"','"+item_type+"');\">" + item_no + "</a>";
			}
			
			partinfo = new PartInfoTable();
			partinfo.setMid(mid);
			partinfo.setItemNo(item_no_link);
			partinfo.setItemDesc(item_desc);
			partinfo.setRegisterId(register_id);
			partinfo.setRegisterInfo(register_info);
			partinfo.setRegisterDate(register_date);
			partinfo.setItemType(item_type);

			list_item.add(partinfo);
			recNum--;
		}
		stmt.close();
		rs.close();

		return list_item;
	}

	/************************************
	 * Ư�����ڵ忡 �ο��� �ڵ帮��Ʈ ��������
	 ************************************/
	public ArrayList getItemListByModelCode(String model_code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList list_item = new ArrayList();
		PartInfoTable partinfo = new PartInfoTable();
		
		String query = "SELECT * FROM item_master WHERE model_code = '" + model_code + "' ORDER BY mid DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){

			String mid					= rs.getString("mid");				//���� ��ȣ
			String item_no				= rs.getString("item_no");			//ǰ�� �ڵ�
			String item_desc			= rs.getString("item_desc");		//ǰ�� ����
			String register_id			= rs.getString("register_id");		//����� ���
			String register_info		= rs.getString("register_info");	//����� ����
			String register_date		= rs.getString("register_date");	//�������
			String code_type			= rs.getString("code_type");		//ǰ�� Ÿ��
			if(code_type.equals("F")) code_type = "F/G";
			else if(code_type.equals("1")) code_type = "ASSY";

			partinfo = new PartInfoTable();
			partinfo.setMid(mid);
			partinfo.setItemNo(item_no);
			partinfo.setItemDesc(item_desc);
			partinfo.setRegisterId(register_id);
			partinfo.setRegisterInfo(register_info);
			partinfo.setRegisterDate(register_date);
			partinfo.setItemType(code_type);

			list_item.add(partinfo);
		}
		stmt.close();
		rs.close();

		return list_item;
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
	 * ���õ� ǰ���ȣ�� �ش��ϴ� ǰ������ ��������
	 *********************************************/
	public PartInfoTable getItemInfo(String item_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PartInfoTable table = new PartInfoTable();

		String sql = "SELECT * FROM item_master WHERE item_no = '" + item_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
//			table.setCodeBig(rs.getString("code_b"));
//			table.setCodeMid(rs.getString("code_m"));
//			table.setCodeSmall(rs.getString("code_s"));
			table.setItemNo(rs.getString("item_no"));
			table.setItemDesc(rs.getString("item_desc"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setItemName(rs.getString("item_name"));
			table.setItemType(rs.getString("item_type"));
			table.setStockUnit(rs.getString("stock_unit"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/*********************************************
	 * ���ϼӼ��� ������ ǰ���� ��ϵǾ� �ִ��� üũ
	 *********************************************/
	public String getSameItemWithSpec(String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String item_no = "";

		String sql = "SELECT item_no,item_desc FROM item_master " + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			item_no = "ǰ���ȣ:" + rs.getString("item_no") + ",ǰ�񼳸�:" + rs.getString("item_desc");
		}
		stmt.close();
		rs.close();

		return item_no;
	}

	/*********************************************
	 * Ư�����ڵ忡 ä���� ����ǰ �� ASSY�ڵ带 �����´�.
	 *********************************************/
	public String getItemNoByModelCode(String code_big,String model_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String item_no = "";

		String sql = "SELECT item_no FROM item_master WHERE model_code = '" + model_code + "' AND code_type = '" + code_big + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			item_no = rs.getString("item_no");
		}
		stmt.close();
		rs.close();

		return item_no;
	}


	/*********************************************
	 * ���õ� ǰ���ȣ�� �ش��ϴ� ǰ������(ResultSet) ��������
	 *********************************************
	public ResultSet getItemInfoWithResultSet(String item_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		PartInfoTable table = new PartInfoTable();

		String sql = "SELECT * FROM item_master WHERE item_no = '" + item_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	}
*/
	/************************************************************************
	 * item_spec ���̺��� �ش� �Һз� ǰ�� �ڵ忡 ���ǵ� �ڵ� ���ڿ��� ������.
	 ************************************************************************/
	public String getCodeStr(String code_s) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT code_str FROM item_spec WHERE code_s = '" + code_s + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String code_str = "";
		while(rs.next()){
			code_str = rs.getString("code_str");
		}
		stmt.close();
		rs.close();

		return code_str;
	}

	/*****************************
	 * ���õ� ǰ�� ����Ʈ�� �����´�.
	 * (���� �� �����ڵ带 ������)
	 *****************************
	public ResultSet getItemClassList(String level,String ancestor) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '" + level + "' and item_ancestor = '" + ancestor +"' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	} //getItemClassList();
*/
	/*****************************
	 * ���õ� ǰ�� ����Ʈ�� �����´�.
	 * (���� �� ǰ��з��ڵ带 ������)
	 *****************************
	public ResultSet getItemClassListByItemCode(String level,String item_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '" + level + "' and item_code LIKE '" + item_code +"%' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	} //getItemClassList();
*/
	/***********************************************************************
	 * ǰ���ڵ�(IC:320,RESISTOR��:230 ��)�� �޾Ƽ� spec_code ���̺��� �˻��Ͽ�
	 * item_code == 'ǰ���ڵ�' �� ���ڵ��� �ִ� �����ڵ�(spec_code)+1 �� �����Ѵ�.
	 * ��ġ�ϴ� ���ڵ尡 ���� ��쿡�� ǰ���ڵ�+01 �� �����Ѵ�.
	 ***********************************************************************/
	public String getMaxSpecCode(String code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT MAX(spec_code) FROM spec_code WHERE item_code = '"+code+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
/*
		String r_code = "";
		while(rs.next()){
			if(rs.getString(1) == null) r_code = code + "01";
			else r_code = Integer.toString(Integer.parseInt(rs.getString(1))+1);
		}
*/
		DecimalFormat fmt = new DecimalFormat("00");
		String r_code = "";
		while(rs.next()){
			if(rs.getString(1) == null) r_code = code + "01";
			else r_code = rs.getString(1).substring(0,3) + fmt.format(Integer.parseInt(rs.getString(1).substring(3,5))+1);
		}

		stmt.close();
		rs.close();

		return r_code;
	} //getMaxSpecCode();

	/************************************************************************
	 * spec_code ���̺��� �ش� �����ڵ忡 �ش��ϴ� ���ڵ� ������ ������
	 ************************************************************************/
	public SpecCodeTable getSpecInfo(String code) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		SpecCodeTable table = new SpecCodeTable();
		
		String query = "SELECT * FROM spec_code WHERE spec_code = '" + code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setSpecCode(rs.getString("spec_code"));
			table.setSpecName(rs.getString("spec_name"));
			table.setSpecValue(rs.getString("spec_value"));
			table.setSpecUnit(rs.getString("spec_unit"));
			table.setWriteExam(rs.getString("write_exam"));
			table.setSpecDesc(rs.getString("spec_desc"));
		}
		stmt.close();
		rs.close();

		return table;
	}

	/****************************************************************
	 * ���õ� �Һз�ǰ�� �ڵ带 ������ ǰ���� item_no�� �ִ밪+1�� ����Ѵ�.
	 ****************************************************************/
	public String calculateItemNo(String code_s) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAX(item_no) FROM item_master WHERE item_no LIKE '" + code_s +"%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("00000");
		String item_no = "";
		while(rs.next()){
			if(rs.getString(1) == null) item_no = code_s + "00001";
			else item_no = code_s + fmt.format(Integer.parseInt(rs.getString(1).substring(code_s.length())) + 1);
		}
		stmt.close();
		rs.close();

		return item_no;
	}

	/****************************************************************
	 * ����ǰ �ڵ� ä���� �ʿ��� �ø��� ��ȣ�� ����Ѵ�.
	 * �ű��� ��쿡�� ��ǰ�� �� ��ǰ�� ���ӵ� �Ϸù�ȣ + 1 ��
	 * �Ļ��� ��쿡�� �ش� ���ڵ忡 �ο��� �ڵ��� �Ϸù�ȣ�� �����´�.
	 ****************************************************************/
	public String getSerialNo(String code_big,String one_class,String two_class,String model_code,String derive_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		String serial_no = "";
		
		if(derive_code.equals("00")){ //�ű��� ���
			sql = "SELECT MAX(serial_no) FROM item_master WHERE code_type = '" + code_big +"' AND one_class = '" + one_class + "' AND two_class = '" + two_class + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			DecimalFormat fmt = new DecimalFormat("0000");
			while(rs.next()){
				if(rs.getString(1) == null) serial_no = "0001";
				else serial_no = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
			}
		}else{ //�Ļ��� ���
			sql = "SELECT serial_no FROM item_master WHERE model_code = '" + model_code +"'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				serial_no = rs.getString("serial_no");
			}		
		}

		stmt.close();
		rs.close();

		return serial_no;
	}

	/****************************************************************
	 * ASS'Y �ڵ� ä���� �ʿ��� �ø��� ��ȣ�� ����Ѵ�.
	 ****************************************************************/
	public String getSerialNo(String code_big,String one_class,String two_class,String model_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		String serial_no = "";
		
		sql = "SELECT MAX(serial_no) FROM item_master WHERE code_type = '" + code_big +"' AND one_class = '" + one_class + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("0000");
		while(rs.next()){
			if(rs.getString(1) == null) serial_no = "0001";
			else serial_no = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}

		stmt.close();
		rs.close();

		return serial_no;
	}

	/*********************************************
	 * Ư�����ڵ忡 ä���� ����ǰ �� ASSY�ڵ��� �ִ� �Ļ���ȣ��
	 * �����´�.
	 *********************************************/
	public String getDeriveCode(String code_big,String model_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String derive_code = "";

		String sql = "SELECT MAX(derive_code) FROM item_master WHERE model_code = '" + model_code + "' AND code_type = '" + code_big + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		DecimalFormat fmt = new DecimalFormat("00");
		while(rs.next()){
			if(rs.getString(1) == null) derive_code = "00";
			else derive_code = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
		}
		
		stmt.close();
		rs.close();

		return derive_code;
	}


	/****************************************
	 * �ű� ǰ�������� DB�� �����Ѵ�.
	 * spec_str == 32001|IDT74FCT165|na,32002|33|EA...
	 ****************************************/
	public void savePartInfo(String item_no,String item_desc,String mfg_no,String item_name,String item_type,String stock_unit,String spec_str,String login_id) throws Exception{

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String register_info = division+"/"+user_rank+"/"+user_name;
		
		//�Ѿ�� �����׸��� ������ �ľ��Ͽ� �� �׸��� ���� �迭����
		StringTokenizer str = new StringTokenizer(spec_str, ",");
		int spec_count = str.countTokens();
		String[] each_item = new String[spec_count];
		
		//�������� �����.
		String sql  = "INSERT INTO item_master (item_no,item_desc,mfg_no,register_id,register_info,register_date,item_name,item_type,stock_unit,stat";
		int i=0;
		while(str.hasMoreTokens()){ 
			each_item[i] = str.nextToken();	//each_item == '32001|IDT74FCT165|na'
			String spec_code = each_item[i].substring(0,each_item[i].indexOf("|"));
			sql += ",prop" + spec_code.substring(spec_code.length()-2,spec_code.length());

			i++;
		}
		sql += ") VALUES('"+item_no+"','"+item_desc+"','"+mfg_no+"','"+login_id+"','"+register_info+"','"+w_time+"','"+item_name+"','"+item_type+"','"+stock_unit+"','1'";

		for(int m=0;m<i;m++){ 
			sql += ",'" + each_item[m].substring(each_item[m].indexOf("|")+1) + "'";
		}
		sql += ")";

		//DB�� �����ϱ�
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/****************************************
	 * �� ��ϵ� ǰ�������� ������Ʈ�Ѵ�.
	 * spec_str == 32001|IDT74FCT165|na,32002|33|EA...
	 ****************************************/
	public void updatePartInfo(String item_no,String item_desc,String mfg_no,String item_name,String item_type,String stock_unit,String spec_str,String login_id) throws Exception{
		//�Ѿ�� �����׸��� ������ �ľ��Ͽ� �� �׸��� ���� �迭����
		StringTokenizer str = new StringTokenizer(spec_str, ",");
		int spec_count = str.countTokens();
		String[] each_item = new String[spec_count];
		
		//�������� �����.
		String sql  = "UPDATE item_master SET item_desc = '" + item_desc + "',mfg_no = '" + mfg_no + "',item_name = '" + item_name + "',item_type = '" + item_type + "',stock_unit = '" + stock_unit + "'";
		int i=0;
		while(str.hasMoreTokens()){ 
			each_item[i] = str.nextToken();	//each_item == '32001|IDT74FCT165|na'
			int offset = each_item[i].indexOf("|");
			String spec_code = each_item[i].substring(0,offset);
			String spec_value = each_item[i].substring(offset+1);

			sql += ",prop" + spec_code.substring(spec_code.length()-2) + " = '" + spec_value + "'";

			i++;
		}
		sql += " WHERE item_no = '"+item_no+"'";

		//DB�� �����ϱ�
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/*********************************
	 * ����ǰ �ڵ� ������ �����Ѵ�.
	 *********************************/
	 public void saveFgInfo(String model_code,String code_big,String one_class,String two_class,String serial_no,String derive_code,String item_no,String item_desc,String login_id,String item_name,String stock_unit,String item_type) throws Exception{
		PreparedStatement pstmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		if(stock_unit==null || stock_unit.equals("")) stock_unit = "EA";
		if(item_name==null || item_name.equals("")) item_name	 = "F/G";

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String register_info = division+"/"+user_rank+"/"+user_name;

		
		String query = "INSERT INTO item_master (model_code,code_type,one_class,two_class,serial_no,derive_code,item_no,item_desc,register_id,register_info,register_date,stat,item_name,stock_unit,item_type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,model_code);
		pstmt.setString(2,code_big);
		pstmt.setString(3,one_class);
		pstmt.setString(4,two_class);
		pstmt.setString(5,serial_no);
		pstmt.setString(6,derive_code);
		pstmt.setString(7,item_no);
		pstmt.setString(8,item_desc);
		pstmt.setString(9,login_id);
		pstmt.setString(10,register_info);
		pstmt.setString(11,w_time);
		pstmt.setString(12,"1");
		pstmt.setString(13,item_name);
		pstmt.setString(14,stock_unit);
		pstmt.setString(15,item_type);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*********************************
	 * Ass'y �ڵ� ������ �����Ѵ�.
	 *********************************/
	 public void saveAssyInfo(String model_code,String code_big,String one_class,String two_class,String serial_no,String assy_type,String op_code,String item_no,String item_desc,String login_id,String where_assy,String item_name,String stock_unit) throws Exception{
		PreparedStatement pstmt = null;

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);
	
		if(stock_unit==null || stock_unit.equals("")) stock_unit = "EA";
		if(item_name==null || item_name.equals("")) item_name	 = "ASSY";

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String register_info = division+"/"+user_rank+"/"+user_name;

		
		String query = "INSERT INTO item_master (model_code,code_type,one_class,two_class,serial_no,assy_type,op_code,item_no,item_desc,register_id,register_info,register_date,stat,item_type,item_name,stock_unit) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,model_code);
		pstmt.setString(2,code_big);
		pstmt.setString(3,one_class);
		pstmt.setString(4,two_class);
		pstmt.setString(5,serial_no);
		pstmt.setString(6,assy_type);
		pstmt.setString(7,op_code);
		pstmt.setString(8,item_no);
		pstmt.setString(9,item_desc);
		pstmt.setString(10,login_id);
		pstmt.setString(11,register_info);
		pstmt.setString(12,w_time);
		pstmt.setString(13,"1");
		pstmt.setString(14,where_assy);
		pstmt.setString(15,item_name);
		pstmt.setString(16,stock_unit);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*************************************
	 * ��ü�ڵ�� ��ġ�ϴ� ��ü�̸��� �����´�.
	 *************************************/
	public String getMfgName(String mfg_no) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT name FROM maker_code_table WHERE code = '" + mfg_no + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String mfg_name = "";
		while(rs.next()){
			mfg_name = rs.getString("name");
		}
		stmt.close();
		rs.close();

		return mfg_name;
	}

	/************************************************************************
	 * spec_code ���̺��� �ش� ǰ���ڵ忡 ���ǵ� ���� ����Ʈ�� ������
	 ************************************************************************/
	public ArrayList getStdTemplateSpecList(String code) throws Exception {
		SpecCodeTable table = new SpecCodeTable();
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList spec_list = new ArrayList();
		
		String query = "SELECT * FROM spec_code WHERE item_code = '" + code + "' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new SpecCodeTable();

			table.setMid(rs.getString("mid"));
			table.setSpecCode(rs.getString("spec_code"));
			table.setSpecName(rs.getString("spec_name"));
			table.setSpecValue(rs.getString("spec_value"));
			table.setSpecUnit(rs.getString("spec_unit"));
			table.setWriteExam(rs.getString("write_exam"));
			table.setSpecDesc(rs.getString("spec_desc"));

			spec_list.add(table);
		}
		stmt.close();
		rs.close();

		return spec_list;
	}

	/*********************************
	 * ǰ�� ǥ�����ø� �����׸� �߰��ϱ�
	 *********************************/
	 public void saveSpecInfo(String code_mid,String spec_code,String spec_name,String spec_value,String spec_unit,String write_exam,String spec_desc) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO spec_code (item_code,spec_code,spec_name,spec_value,spec_unit,write_exam,spec_desc) VALUES (?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,code_mid);
		pstmt.setString(2,spec_code);
		pstmt.setString(3,spec_name);
		pstmt.setString(4,spec_value);
		pstmt.setString(5,spec_unit);
		pstmt.setString(6,write_exam);
		pstmt.setString(7,spec_desc);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/************************************
	 * ǰ�� ǥ�����ø� �����׸� ���������ϱ�
	 ************************************/
	 public void updateSpecInfo(String code_mid,String spec_code,String spec_name,String spec_value,String spec_unit,String write_exam,String spec_desc) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE spec_code SET spec_name=?,spec_value=?,spec_unit=?,write_exam=?,spec_desc=? WHERE spec_code = '" + spec_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,spec_name);
		pstmt.setString(2,spec_value);
		pstmt.setString(3,spec_unit);
		pstmt.setString(4,write_exam);
		pstmt.setString(5,spec_desc);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*************************************
	 * ÷������ ������ DB�� �����Ѵ�.
	 *************************************/
	public void updTable(String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE item_master " + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	 * item_no�� �ش��ϴ� �������� ÷������ ����Ʈ ��������
	 *****************************************************************/
	public ArrayList getFile_list(String item_no) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,fsize,ftype,umask FROM item_master WHERE item_no = '"+item_no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
	
		if(rs.next()){ 
			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
			Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("umask")).iterator();
			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()&&fileumask_iter.hasNext()){
				PartInfoTable file = new PartInfoTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				file.setFileUmask((String)fileumask_iter.next());
				//System.out.println("umask:"+rs.getString("umask"));
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()

	/******************************************
	*  ��ǰ�ڵ� ����(F/G & Assy) �����ϱ�
	******************************************/
	public void updateItemInfo(String item_no,String item_desc,String item_type,String stock_unit) throws Exception {
		
		PreparedStatement pstmt = null;

		String query = "UPDATE item_master SET item_desc=?,item_type=?,stock_unit=? WHERE item_no = '" + item_no + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_desc);
		pstmt.setString(2,item_type);
		pstmt.setString(3,stock_unit);
					
		pstmt.executeUpdate();
		pstmt.close();
	
	}

	/* ǰ�� ����*/
	public void deleteItem(String mid) throws Exception {

		Statement stmt = null;
		String query = "DELETE item_master WHERE mid = '"+mid+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************************
	* item_no ǰ���ȣ�� mid(������ȣ) ��������
	*******************************************/
	public String getMid(String item_no) throws Exception {
		
		Statement stmt = null;
		ResultSet rs = null;
		String mid = "";

		String query = "SELECT mid FROM item_master WHERE item_no = '"+item_no+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		if (rs.next()){
			mid = rs.getString("mid");
		}

		stmt.close();
		rs.close();

		return mid;
	}

	/*******************************************
	* item_no(ǰ���ȣ)�� �ش��ϴ� item_type(ǰ�����) ��������
	*******************************************/
	public String getItemType(String item_no) throws Exception {
		
		Statement stmt = null;
		ResultSet rs = null;
		String item_type = "";

		String query = "SELECT item_type FROM item_master WHERE item_no = '"+item_no+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		if (rs.next()){
			item_type = rs.getString("item_type");
		}

		stmt.close();
		rs.close();

		return item_type;
	}

	/*******************************************
	* item_no(ǰ���ȣ)�� �ش��ϴ� ���ڵ� ��������
	*******************************************/
	public String getModelCode(String item_no) throws Exception {
		
		Statement stmt = null;
		ResultSet rs = null;
		String model_code = "";

		String query = "SELECT model_code FROM item_master WHERE item_no = '"+item_no+"'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		if (rs.next()){
			model_code = rs.getString("model_code");
		}

		stmt.close();
		rs.close();

		return model_code;
	}
}		