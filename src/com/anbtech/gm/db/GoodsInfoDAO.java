package com.anbtech.gm.db;

import com.anbtech.gm.entity.*;
import com.anbtech.gm.business.*;
import com.anbtech.gm.db.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class GoodsInfoDAO{
	private Connection con;

	public GoodsInfoDAO(Connection con){
		this.con = con;
	}


	/************************************************************************
	 * 선택된 관리번호(mid)에 해당하는 제품정보 가져오기
	 ************************************************************************/
	public GoodsInfoTable getGoodsInfoByMid(String mid) throws Exception{
		GoodsInfoTable table = new GoodsInfoTable();
		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);

		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT mid,code,name,name2,short_name,color_code,other_info,glevel,ancestor,gcode,register_id,register_info,register_date,modifier_id,modifier_info,modify_date,aid,stat FROM goods_structure WHERE mid = '" + mid +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		while(rs.next()){
			table.setMid(rs.getString("mid"));
			table.setGoodsCode(rs.getString("code"));
			table.setGoodsName(rs.getString("name"));
			table.setGoodsName2(rs.getString("name2"));
			table.setShortName(rs.getString("short_name"));
			table.setColorCode(rs.getString("color_code"));
			table.setOtherInfo(rs.getString("other_info"));
			table.setGoodsLevel(rs.getString("glevel"));
			table.setAncestor(rs.getString("ancestor"));
			table.setGcode(rs.getString("gcode"));
			table.setRegisterId(rs.getString("register_id"));
			table.setRegisterInfo(rs.getString("register_info"));
			table.setRegisterDate(rs.getString("register_date"));
			table.setModifierId(rs.getString("modifier_id"));
			table.setModifierInfo(rs.getString("modifier_info"));
			table.setModifyDate(rs.getString("modify_date"));
			table.setAid(rs.getString("aid"));
			table.setStat(rs.getString("stat"));

			//선택된 모델코드에 채번된 F/G코드가 있으면 가져온다.
			table.setFgCode(cmDAO.getItemNoByModelCode("F",rs.getString("code")));
		}

		stmt.close();
		rs.close();

		return table;
	}

	/************************************************************************
	 * 검색 조건에 맞는 모델 리스트를 가져온다.
	/************************************************************************/
	public ArrayList getModelList(String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		GoodsInfoTable table = new GoodsInfoTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 17;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);

		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String where = goodsBO.getWhere(mode,searchword, searchscope, category);

		int total = getTotalCount("goods_structure", where);	// 전체 레코드 갯수
		int recNum = total;

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT mid,code,name,name2,register_date,modify_date,glevel,ancestor,gcode FROM goods_structure " + where + " ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String mid		= rs.getString("mid");
			String code		= rs.getString("code");
			String name		= rs.getString("name");
			String name2	= rs.getString("name2");
			String register_date	= rs.getString("register_date");
			String modify_date		= rs.getString("modify_date");
			String glevel	= rs.getString("glevel");
			String ancestor = rs.getString("ancestor");
			String gcode	= rs.getString("gcode");

			//모델명에 링크 설정
			String code_link = "";
			code_link = "<A HREF='GoodsInfoServlet?mode=view_model";
			code_link += "&page="+page+"&searchword="+searchword;
			code_link += "&searchscope="+searchscope+"&category="+category;
			code_link += "&no="+mid+"'>";
			code_link = code_link + code + "</a>";
			
			table = new GoodsInfoTable();
			table.setMid(mid);
			table.setGoodsCode(code_link);
			table.setGoodsName(name);
			table.setGoodsName2(name2);
			table.setRegisterDate(register_date);
			table.setModifyDate(modify_date);
			table.setGoodsLevel(glevel);
			table.setAncestor(ancestor);

			table_list.add(table);
			recNum--;
		}

		stmt.close();
		rs.close();

		return table_list;
	}

	/************************************************************************
	 * 검색 조건에 맞는 모델 리스트를 가져온다.
	/************************************************************************/
	public ArrayList searchModelList(String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		String tablename = "";
	
		GoodsInfoTable table = new GoodsInfoTable();
		ArrayList table_list = new ArrayList();
		com.anbtech.gm.business.makeGoodsTreeItems tree = new com.anbtech.gm.business.makeGoodsTreeItems(con);
		GmLinkUrlBO gmlinkBO = new GmLinkUrlBO(con);
		
		int l_maxlist = 10;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);
		
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String where = "";
				
		//검색조건에 맞는 게시물을 가져온다.
		if (searchscope.equals("item_no"))	{
			where = goodsBO.getWhereByScope(mode,searchword, searchscope, category);
			query = "SELECT model_code, item_no, item_desc FROM item_master "+where+" ORDER BY mid DESC";
			tablename = "item_master";
		} else {
			where = goodsBO.getWhere(mode,searchword, searchscope, category);
			query = "SELECT mid, code FROM goods_structure "+where+" ORDER BY mid DESC";
			tablename = "goods_structure";
		}			
						
		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
		int recNum = total;

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		if (searchscope.equals("item_no"))
		{
			for(int i=0; i < l_maxlist; i++){
				if(!rs.next()){break;}
				
				String[] arry = new String[12];
				String valueStr = "";	// 리턴 값 저장하는 변수
				String model_code	= rs.getString("model_code");
				String mid = (String)getGoodsMidByCode(model_code);
				String item_no      = rs.getString("item_no");
				String item_desc	= rs.getString("item_desc");
				int k = 0;
				
				// return value 가져오기 (스트링-폼필드 값)
				String temp = (String)tree.getReturnValue(Integer.parseInt(mid),"");
				// return value를 배열에 넣기
				java.util.StringTokenizer st = new java.util.StringTokenizer(temp,"|");
				while (st.hasMoreTokens())	{	
					arry[k] = st.nextToken();
					k++;
				}

				arry[8]	 = item_no;	// F/G코드
				arry[9]  = mid;		// 관리번호

				for (int m=0; m<=8;m++ ) {	
					valueStr = valueStr + arry[m]+"|"; 
				}

				arry[10] = valueStr;
				arry[11] = item_desc;
				table_list.add(arry);
				recNum--;
			}

		} else {
			for(int i=0; i < l_maxlist; i++){
				if(!rs.next()){break;}
				
				String[] arry = new String[12];
				String valueStr = "";
				String model_code	= rs.getString("code");
				String item_no      = getFgCode(model_code);
				String mid = (String)getGoodsMidByCode(model_code);
				String item_desc	= (String)getGoodsDescByCode(model_code);
					
				int k = 0;
					
				String temp = (String)tree.getReturnValue(Integer.parseInt(mid),"");
				java.util.StringTokenizer st = new java.util.StringTokenizer(temp,"|");
				while (st.hasMoreTokens())	{	
					arry[k] = st.nextToken();
					k++;
				}

				arry[8]	 = item_no;
				arry[9]  = mid;
				
				for (int m=0; m<=8;m++ ){valueStr = valueStr + arry[m]+"|";	}
				arry[10] = valueStr;
				arry[11] = item_desc;
				table_list.add(arry);
				recNum--;
			}
		}
				
		stmt.close();
		rs.close();

		return table_list;
	}
	

	/********************************************************************
	*  F/G코드 가져오기
	********************************************************************/
	public String getFgCode(String code) throws Exception {
		Statement st = null;
		ResultSet rst = null;

		String query = "SELECT item_no FROM item_master WHERE model_code = '"+code+"' and code_type = 'F'";

		st = con.createStatement();
		rst = st.executeQuery(query);
		String item_no = "";

		if(rst.next()){
			item_no = rst.getString("item_no");
		}

		st.close();
		rst.close();
		
		return item_no;
	}

	/************************************************************************
	 * 선택된 코드에 해당하는 제품정보 가져오기
	 ************************************************************************/
	public GoodsInfoTable getGoodsInfoByCode(String code) throws Exception{
		GoodsInfoTable table = new GoodsInfoTable();

		Statement stmt2 = null;
		ResultSet rs2 = null;

		String sql = "SELECT mid,code,name,glevel,ancestor FROM goods_structure WHERE code = '" + code +"'";
		stmt2 = con.createStatement();
		rs2 = stmt2.executeQuery(sql);

		while(rs2.next()){
			table.setMid(rs2.getString("mid"));
			table.setGoodsCode(rs2.getString("code"));
			table.setGoodsName(rs2.getString("name"));
			table.setGoodsLevel(rs2.getString("glevel"));
			table.setAncestor(rs2.getString("ancestor"));
		}
		
		stmt2.close();
		rs2.close();

		return table;
	}

	/************************************************************************
	 * 선택된 코드에 해당하는 제품 Mid(관리번호) 가져오기
	 ************************************************************************/
	public String getGoodsMidByCode(String code) throws Exception{
		
		Statement stmt2 = null;
		ResultSet rs2 = null;

		String sql = "SELECT mid FROM goods_structure WHERE code = '" + code +"'";
		stmt2 = con.createStatement();
		rs2 = stmt2.executeQuery(sql);
		
		String mid = "";
		if(rs2.next()){
			mid = rs2.getString("mid");
		}
		
		stmt2.close();
		rs2.close();

		return mid;
	}

	/************************************************************************
	 * 선택된 코드에 해당하는 제품 DESCRIPTION 가져오기
	 ************************************************************************/
	public String getGoodsDescByCode(String code) throws Exception{
		
		Statement stmt2 = null;
		ResultSet rs2 = null;

		String sql = "SELECT item_desc FROM item_master WHERE model_code = '" + code +"' and code_type ='F'";
		stmt2 = con.createStatement();
		rs2 = stmt2.executeQuery(sql);
		
		String desc = "";
		if(rs2.next()){
			desc = rs2.getString("item_desc");
		}
		
		stmt2.close();
		rs2.close();

		return desc;
	}


	/*******************************************************************
	 * 레코드의 전체 개수를 구한다.
	 *******************************************************************/
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


	/*******************************************************************************
	 * 선택된 인자값 level,ancestor를 갖는 제품의 최대 gcode 값을 가져온다. 
	 *******************************************************************************/
	public String getMaxGcode(String level,String ancestor) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAX(gcode) FROM goods_structure WHERE glevel = '" + level +"' and ancestor = '" + ancestor + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		String gcode = rs.getString(1);

		stmt.close();
		rs.close();

		return gcode;
	}

	/*******************************************************************************
	 * 선택된 관리번호에 해당하는 제품정보의 gcode 값을 가져온다.
	 *******************************************************************************/
	public String getGcodeByMid(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT gcode FROM goods_structure WHERE mid = '" + mid +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		String gcode = rs.getString("gcode");

		stmt.close();
		rs.close();

		return gcode;
	}


	/*******************************************************************************
	 * 선택된 gcode에 해당하는 제품정보의 code값을 가져온다.
	 *******************************************************************************/
	public String getCodeByGcode(String gcode) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT code FROM goods_structure WHERE gcode = '" + gcode +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		rs.next();
		String code = rs.getString("code");

		stmt.close();
		rs.close();

		return code;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
// 아래 방법으로는 ResultSet를 리턴받는 메서드에서 Statement를 닫아주지 못하는 관계로 사용하지 않음 04.07.16
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************
	 * 제품군 리스트 가져오기
	 ***********************
	public ResultSet getOneClassList() throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	} //getOneClassList();
*/
	/*******************************************
	 * 선택된 제품군 하위의 제품 리스트를 가져온다.
	 *******************************************
	public ResultSet getTwoClassList(String one_class) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '2' and gcode LIKE '" + one_class +"%' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	} //getTwoClassList();
*/
	/*******************************************
	 * 선택된 제품 하위의 모델군 리스트를 가져온다.
	 *******************************************
	public ResultSet getThreeClassList(String two_class) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT mid,gcode,name FROM goods_structure WHERE glevel = '3' and gcode LIKE '" + two_class +"%' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	} //getThreeClassList();
*/

	/*******************************************
	 * 선택된 모델의 속성값을 가져온다.
	 *******************************************
	public ResultSet getModelProperty(String mid,int spec_count) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT prop1 ";
		for(int i=2; i<=spec_count; i++){
			sql+= ",prop" + i;
		}
		sql += " FROM goods_structure WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		return rs;
	}
*/
//////////////
// 여기까지
//////////////

	/*******************************************************************************
	 * 선택된 제품에 등록된 표준 템프릿항목 코드의 최대값 + 1을 가져온다.
	 *******************************************************************************/
	public String getMaxItemCode(String two_class) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String sql = "SELECT MAX(item_code) FROM std_template_list WHERE item_code LIKE '" + two_class + "%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);

		String r_code = "";
		while(rs.next()){
			if(rs.getString(1) == null) r_code = two_class + "001";
			else r_code = Integer.toString(Integer.parseInt(rs.getString(1))+1);
		}

		stmt.close();
		rs.close();

		return r_code;
	} //getMaxItemCode();


	/************************************************************************
	 * std_template_list 테이블에서 선택된 항목에 대한 정보를 가져옴
	 ************************************************************************/
	public GoodsInfoItemTable getItemInfo(String item_code) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		GoodsInfoItemTable table = new GoodsInfoItemTable();
		
		String query = "SELECT * FROM std_template_list WHERE item_code = '" + item_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemValue(rs.getString("item_value"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setWriteExam(rs.getString("write_exam"));
			table.setItemDesc(rs.getString("item_desc"));
		}

		stmt.close();
		rs.close();

		return table;
	}

	/************************************************************************
	 * std_template_list 테이블에서 선택된 제품의 항목리스트를 가져온다.
	 ************************************************************************/
	public ArrayList getItemList(String code) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		GoodsInfoItemTable table = new GoodsInfoItemTable();
		ArrayList item_list = new ArrayList();
		
		String query = "SELECT * FROM std_template_list WHERE item_code LIKE '" + code + "%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			table = new GoodsInfoItemTable();

			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemValue(rs.getString("item_value"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setWriteExam(rs.getString("write_exam"));
			table.setItemDesc(rs.getString("item_desc"));

			item_list.add(table);
		}

		stmt.close();
		rs.close();

		return item_list;
	}

	/**********************
	 * 템플릿 항목 저장하기
	 **********************/
	 public void saveItemInfo(String item_code,String item_name,String item_value,String item_unit,String write_exam,String item_desc) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO std_template_list (item_code,item_name,item_value,item_unit,write_exam,item_desc) VALUES (?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_code);
		pstmt.setString(2,item_name);
		pstmt.setString(3,item_value);
		pstmt.setString(4,item_unit);
		pstmt.setString(5,write_exam);
		pstmt.setString(6,item_desc);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/***********************
	 * 템플릿 항목 수정하기
	 ***********************/
	 public void updItemInfo(String item_code,String item_name,String item_value,String item_unit,String write_exam,String item_desc) throws Exception{
		PreparedStatement pstmt = null;

		String query = "UPDATE std_template_list SET item_name=?,item_value=?,item_unit=?,write_exam=?,item_desc=? WHERE item_code = '" + item_code + "'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,item_value);
		pstmt.setString(3,item_unit);
		pstmt.setString(4,write_exam);
		pstmt.setString(5,item_desc);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/****************************************
	 * 신규모델정보를 DB에 저장한다.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "값1|단위1!값2|단위2! ... ";
	 ****************************************/
	public void saveModelInfo(String three_class,String code,String name,String name2,String short_name,String color_code,String other_info,String code_str,String spec_str,String login_id) throws Exception{
		//하위분류 쿼리를 위한 내부코드(gcode)를 계산한다.
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String gcode = goodsBO.calculateGcode("4",three_class);
		
		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String register_date = vans.format(now);

		//등록자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String register_info		= userinfo.getDivision() + " " + userinfo.getUserRank() + " " + userinfo.getUserName();

		//넘어온 스펙항목의 개수를 파악
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//쿼리문을 만든다.
		String sql  = "INSERT INTO goods_structure (code,name,name2,short_name,color_code,other_info,register_id,register_info,register_date,modifier_id,modifier_info,modify_date,aid,stat,glevel,ancestor,gcode,spec_list";
		for(int i=1; i<=spec_count; i++){ 
			sql += ",prop" + i;
		}
		sql += ") VALUES('"+code+"','"+name+"','"+name2+"','"+short_name+"','"+color_code+"','"+other_info+"','"+login_id+"','"+register_info+"','"+register_date+"','','','','','1','4','"+three_class+"','"+gcode+"','"+code_str+"'";

		for(int i=1; i<=spec_count; i++){ 
			String item = str.nextToken();

			sql += ",'" + item + "'";
		}
		sql += ")";

		//DB에 저장하기
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/****************************************
	 * 수정된 모델정보를 DB에 저장한다.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "값1|단위1!값2|단위2! ... ";
	 ****************************************/
	public void modifyModelInfo(String mid,String three_class,String code,String name,String name2,String short_name,String color_code,String other_info,String code_str,String spec_str,String login_id) throws Exception{

		GoodsInfoTable table = new GoodsInfoTable();
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String sql  = "";

		//요청시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String modify_date = vans.format(now);

		//등록자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String modifier_info		= userinfo.getDivision() + " " + userinfo.getUserRank() + " " + userinfo.getUserName();
		
/*모델군의 변경이 가능하게 할 때의 소스로 주석처리됨, 삭제하지 말것(2004.05.04 by 동렬)		

		//모델군 분류가 변경되었는지 체크하기 위해 이전 ancestor값을 가져와서 비교한다.
		table = getGoodsInfoByMid(mid);
		String old_ancestor = table.getAncestor();

		//모델군 분류가 변경된 경우에는 gcode를 다시 계산하여 db에 반영한다.
		if(!old_ancestor.equals(three_class)){
			String gcode = goodsBO.calculateGcode("4",three_class);
		
			//넘어온 스펙항목의 개수를 파악
			StringTokenizer str = new StringTokenizer(spec_str, "!");
			int spec_count = str.countTokens();

			//쿼리문을 만든다.
			sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
			sql += ",ancestor='" + three_class + "',gcode='" + gcode + "'";
			for(int i=1; i<=spec_count; i++){ 
				String item = str.nextToken();
				sql += ",prop" + i + "='" + item + "'";
			}
			sql += " WHERE mid='" + mid +"'";

		//모델군 분류가 변경되지 않은 경우
		}else{
			//넘어온 스펙항목의 개수를 파악
			StringTokenizer str = new StringTokenizer(spec_str, "!");
			int spec_count = str.countTokens();

			//쿼리문을 만든다.
			sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
			for(int i=1; i<=spec_count; i++){ 
				String item = str.nextToken();
				sql += ",prop" + i + "='" + item + "'";
			}
			sql += " WHERE mid='" + mid +"'";		
		}
여기까지 */

		//넘어온 스펙항목의 개수를 파악
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//쿼리문을 만든다.
		sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
		for(int i=1; i<=spec_count; i++){ 
			String item = str.nextToken();
			sql += ",prop" + i + "='" + item + "'";
		}
		sql += " WHERE mid='" + mid +"'";		


		//DB에 저장하기
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/*******************************************************************************
	 * 선택된 관리번호에 해당하는 모델의 스펙리스트값을 가져온다.
	 *******************************************************************************/
	public String getSpecList(String mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT spec_list FROM goods_structure WHERE mid = '" + mid +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();
		String spec_list = rs.getString("spec_list");

		stmt.close();
		rs.close();

		return spec_list;
	}

	/*******************************************************************************
	 * 모델코드 중복을 체크하기 위해 선택된 모델코드의 레코드 개수를 계산한다.
	 *******************************************************************************/
	public int getCountWithSameModelCode(String model_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int count = 0;

		String sql = "SELECT COUNT(code) FROM goods_structure WHERE code = '" + model_code + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()){
			count = rs.getInt(1);
		}

		stmt.close();
		rs.close();

		return count;
	}


	/****************************************
	 * 동일한 모델규격정보를 가진 모델코드값을 가져온다.
	 * 없을 경우에는 공백을 리턴한다.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "값1|단위1!값2|단위2! ... ";
	 ****************************************/
	public String getModelCodeWithSameProperty(String code_str,String spec_str) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String model_code = "";

		//넘어온 스펙항목의 개수를 파악
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//쿼리문을 만든다.
		String sql  = "SELECT code FROM goods_structure WHERE (glevel = '4') ";
		for(int i=1; i<=spec_count; i++){ 
			String item = str.nextToken();

			sql += "AND (prop" + i + " = '" + item + "')";
		}

		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		while(rs.next()){
			model_code = rs.getString("code");
		}

		stmt.close();
		rs.close();

		return model_code;
	}

	/**********************
	 * 모델코드정보 저장하기
	 **********************/
	 public void saveModelCodeInfo(String code,String revision_code,String derive_code) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO model_code_info (code,revision_code,derive_code) VALUES (?,?,?)";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,code);
		pstmt.setString(2,revision_code);
		pstmt.setString(3,derive_code);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************************************************************
	 * model_code_info 테이블에서 선택된 모델의 최대 파생코드를 가져온다.
	 *******************************************************************************/
	public String getMaxDeriveCode(String searchword) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAX(derive_code) FROM model_code_info WHERE code LIKE '" + searchword +"%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();

		DecimalFormat fmt = new DecimalFormat("00");
		String next_value = fmt.format(Integer.parseInt(rs.getString(1)) + 1);

		stmt.close();
		rs.close();

		return next_value;
	}

	/*******************************************************************************
	 * model_code_info 테이블에서 선택된 모델의 최대 기능구분코드를 가져온다.
	 *******************************************************************************/
	public String getMaxRevisionCode(String searchword) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT MAX(revision_code) FROM model_code_info WHERE code LIKE '" + searchword +"%'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();

		DecimalFormat fmt = new DecimalFormat("0");
		String next_value = fmt.format(Integer.parseInt(rs.getString(1)) + 1);

		stmt.close();
		rs.close();

		return next_value;
	}

	/*******************************
	 * 선택된 모델정보 삭제하기
	 *******************************/
	public void deleteModelInfo(String mid) throws Exception{
		Statement stmt = null;
		String query = "DELETE goods_structure WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * 선택된 모델코드정보 삭제하기
	 *******************************/
	public void deleteModelCodeInfo(String code) throws Exception{
		Statement stmt = null;
		String query = "DELETE model_code_info WHERE code = '" + code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

}		