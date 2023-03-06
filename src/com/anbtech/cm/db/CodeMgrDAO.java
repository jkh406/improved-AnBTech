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
	 * 검색조건에 맞는 품목 리스트 가져오기
	 ************************************/
	public ArrayList getListItem(String mode,String category,String code_b,String code_m,String code_s,String searchword,String searchscope,String page,String login_id) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		if(mode.equals("list_item_p")) l_maxlist = 10;
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이
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

			String mid					= rs.getString("mid");				//관리 번호
			String item_no				= rs.getString("item_no");			//품목 코드
			String item_desc			= rs.getString("item_desc");		//품목 설명
			String register_id			= rs.getString("register_id");		//등록자 사번
			String register_info		= rs.getString("register_info");	//등록자 정보
			String register_date		= rs.getString("register_date");	//등록일자
			String code_type			= rs.getString("code_type");		//품목 타입
			String item_name			= rs.getString("item_name");		//품목명
			String stock_unit			= rs.getString("stock_unit");		//품목 단위
			String item_type			= rs.getString("item_type");		//품목 계정

			String item_no_link = item_no;
		
			String temp_mode = "";
		
			if(code_type != null) temp_mode = "view_item2";
			else temp_mode = "view_item";

				item_no_link = "<a href='CodeMgrServlet?mode="+temp_mode+"&category="+category+"&no=" + mid + "&item_no=" + item_no;
				item_no_link += "&page=" + page + "&searchword=" + searchword + "&searchscope=" + searchscope + "' ";
				item_no_link += "onMouseOver=\"window.status='품목정보 상세보기 #" + item_no + "';return true;\" ";
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
	 * 특정모델코드에 부여된 코드리스트 가져오기
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

			String mid					= rs.getString("mid");				//관리 번호
			String item_no				= rs.getString("item_no");			//품목 코드
			String item_desc			= rs.getString("item_desc");		//품목 설명
			String register_id			= rs.getString("register_id");		//등록자 사번
			String register_info		= rs.getString("register_info");	//등록자 정보
			String register_date		= rs.getString("register_date");	//등록일자
			String code_type			= rs.getString("code_type");		//품목 타입
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

	/*********************************************
	 * 선택된 품목번호에 해당하는 품목정보 가져오기
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
	 * 동일속성을 가지는 품목이 등록되어 있는지 체크
	 *********************************************/
	public String getSameItemWithSpec(String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String item_no = "";

		String sql = "SELECT item_no,item_desc FROM item_master " + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			item_no = "품목번호:" + rs.getString("item_no") + ",품목설명:" + rs.getString("item_desc");
		}
		stmt.close();
		rs.close();

		return item_no;
	}

	/*********************************************
	 * 특정모델코드에 채번된 완제품 및 ASSY코드를 가져온다.
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
	 * 선택된 품목번호에 해당하는 품목정보(ResultSet) 가져오기
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
	 * item_spec 테이블에서 해당 소분류 품목 코드에 정의된 코드 문자열을 가져옴.
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
	 * 선택된 품목 리스트를 가져온다.
	 * (레벨 및 상위코드를 가지고)
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
	 * 선택된 품목 리스트를 가져온다.
	 * (레벨 및 품목분류코드를 가지고)
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
	 * 품목코드(IC:320,RESISTOR류:230 등)를 받아서 spec_code 테이블을 검색하여
	 * item_code == '품목코드' 인 레코드의 최대 스펙코드(spec_code)+1 을 리턴한다.
	 * 일치하는 레코드가 없을 경우에는 품목코드+01 을 리턴한다.
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
	 * spec_code 테이블에서 해당 스펙코드에 해당하는 레코드 정보를 가져옴
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
	 * 선택된 소분류품목 코드를 가지는 품목의 item_no의 최대값+1을 계산한다.
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
	 * 완제품 코드 채번시 필요한 시리얼 번호를 계산한다.
	 * 신규일 경우에는 제품군 및 제품에 종속된 일련번호 + 1 을
	 * 파생일 경우에는 해당 모델코드에 부여된 코드의 일련번호를 가져온다.
	 ****************************************************************/
	public String getSerialNo(String code_big,String one_class,String two_class,String model_code,String derive_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "";
		String serial_no = "";
		
		if(derive_code.equals("00")){ //신규일 경우
			sql = "SELECT MAX(serial_no) FROM item_master WHERE code_type = '" + code_big +"' AND one_class = '" + one_class + "' AND two_class = '" + two_class + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);

			DecimalFormat fmt = new DecimalFormat("0000");
			while(rs.next()){
				if(rs.getString(1) == null) serial_no = "0001";
				else serial_no = fmt.format(Integer.parseInt(rs.getString(1)) + 1);
			}
		}else{ //파생일 경우
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
	 * ASS'Y 코드 채번시 필요한 시리얼 번호를 계산한다.
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
	 * 특정모델코드에 채번된 완제품 및 ASSY코드의 최대 파생번호를
	 * 가져온다.
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
	 * 신규 품목정보를 DB에 저장한다.
	 * spec_str == 32001|IDT74FCT165|na,32002|33|EA...
	 ****************************************/
	public void savePartInfo(String item_no,String item_desc,String mfg_no,String item_name,String item_type,String stock_unit,String spec_str,String login_id) throws Exception{

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//등록자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String register_info = division+"/"+user_rank+"/"+user_name;
		
		//넘어온 스펙항목의 개수를 파악하여 각 항목을 담을 배열선언
		StringTokenizer str = new StringTokenizer(spec_str, ",");
		int spec_count = str.countTokens();
		String[] each_item = new String[spec_count];
		
		//쿼리문을 만든다.
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

		//DB에 저장하기
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/****************************************
	 * 기 등록된 품목정보를 업데이트한다.
	 * spec_str == 32001|IDT74FCT165|na,32002|33|EA...
	 ****************************************/
	public void updatePartInfo(String item_no,String item_desc,String mfg_no,String item_name,String item_type,String stock_unit,String spec_str,String login_id) throws Exception{
		//넘어온 스펙항목의 개수를 파악하여 각 항목을 담을 배열선언
		StringTokenizer str = new StringTokenizer(spec_str, ",");
		int spec_count = str.countTokens();
		String[] each_item = new String[spec_count];
		
		//쿼리문을 만든다.
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

		//DB에 저장하기
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/*********************************
	 * 완제품 코드 정보를 저장한다.
	 *********************************/
	 public void saveFgInfo(String model_code,String code_big,String one_class,String two_class,String serial_no,String derive_code,String item_no,String item_desc,String login_id,String item_name,String stock_unit,String item_type) throws Exception{
		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		if(stock_unit==null || stock_unit.equals("")) stock_unit = "EA";
		if(item_name==null || item_name.equals("")) item_name	 = "F/G";

		//등록자 정보
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
	 * Ass'y 코드 정보를 저장한다.
	 *********************************/
	 public void saveAssyInfo(String model_code,String code_big,String one_class,String two_class,String serial_no,String assy_type,String op_code,String item_no,String item_desc,String login_id,String where_assy,String item_name,String stock_unit) throws Exception{
		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);
	
		if(stock_unit==null || stock_unit.equals("")) stock_unit = "EA";
		if(item_name==null || item_name.equals("")) item_name	 = "ASSY";

		//등록자 정보
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
	 * 업체코드와 일치하는 업체이름을 가져온다.
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
	 * spec_code 테이블에서 해당 품목코드에 정의된 스펙 리스트를 가져옴
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
	 * 품목별 표준템플릿 스펙항목 추가하기
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
	 * 품목별 표준템플릿 스펙항목 정보수정하기
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
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE item_master " + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	 * item_no에 해당하는 데이터의 첨부파일 리스트 가져오기
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
	*  부품코드 정보(F/G & Assy) 수정하기
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

	/* 품목 삭제*/
	public void deleteItem(String mid) throws Exception {

		Statement stmt = null;
		String query = "DELETE item_master WHERE mid = '"+mid+"'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************************
	* item_no 품목번호의 mid(관리번호) 가져오기
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
	* item_no(품목번호)에 해당하는 item_type(품목계정) 가져오기
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
	* item_no(품목번호)에 해당하는 모델코드 가져오기
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