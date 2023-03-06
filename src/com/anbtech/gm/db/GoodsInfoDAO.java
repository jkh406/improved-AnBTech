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
	 * ���õ� ������ȣ(mid)�� �ش��ϴ� ��ǰ���� ��������
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

			//���õ� ���ڵ忡 ä���� F/G�ڵ尡 ������ �����´�.
			table.setFgCode(cmDAO.getItemNoByModelCode("F",rs.getString("code")));
		}

		stmt.close();
		rs.close();

		return table;
	}

	/************************************************************************
	 * �˻� ���ǿ� �´� �� ����Ʈ�� �����´�.
	/************************************************************************/
	public ArrayList getModelList(String mode,String searchword,String searchscope,String category,String page) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		GoodsInfoTable table = new GoodsInfoTable();
		ArrayList table_list = new ArrayList();
		
		int l_maxlist = 17;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		int current_page_num =Integer.parseInt(page);

		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String where = goodsBO.getWhere(mode,searchword, searchscope, category);

		int total = getTotalCount("goods_structure", where);	// ��ü ���ڵ� ����
		int recNum = total;

		//�˻����ǿ� �´� �Խù��� �����´�.
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

			//�𵨸� ��ũ ����
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
	 * �˻� ���ǿ� �´� �� ����Ʈ�� �����´�.
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
		
		int l_maxlist = 10;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 30;	// ������ �ִ� ǥ�ñ���

		int current_page_num =Integer.parseInt(page);
		
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String where = "";
				
		//�˻����ǿ� �´� �Խù��� �����´�.
		if (searchscope.equals("item_no"))	{
			where = goodsBO.getWhereByScope(mode,searchword, searchscope, category);
			query = "SELECT model_code, item_no, item_desc FROM item_master "+where+" ORDER BY mid DESC";
			tablename = "item_master";
		} else {
			where = goodsBO.getWhere(mode,searchword, searchscope, category);
			query = "SELECT mid, code FROM goods_structure "+where+" ORDER BY mid DESC";
			tablename = "goods_structure";
		}			
						
		int total = getTotalCount(tablename, where);	// ��ü ���ڵ� ����
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
				String valueStr = "";	// ���� �� �����ϴ� ����
				String model_code	= rs.getString("model_code");
				String mid = (String)getGoodsMidByCode(model_code);
				String item_no      = rs.getString("item_no");
				String item_desc	= rs.getString("item_desc");
				int k = 0;
				
				// return value �������� (��Ʈ��-���ʵ� ��)
				String temp = (String)tree.getReturnValue(Integer.parseInt(mid),"");
				// return value�� �迭�� �ֱ�
				java.util.StringTokenizer st = new java.util.StringTokenizer(temp,"|");
				while (st.hasMoreTokens())	{	
					arry[k] = st.nextToken();
					k++;
				}

				arry[8]	 = item_no;	// F/G�ڵ�
				arry[9]  = mid;		// ������ȣ

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
	*  F/G�ڵ� ��������
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
	 * ���õ� �ڵ忡 �ش��ϴ� ��ǰ���� ��������
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
	 * ���õ� �ڵ忡 �ش��ϴ� ��ǰ Mid(������ȣ) ��������
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
	 * ���õ� �ڵ忡 �ش��ϴ� ��ǰ DESCRIPTION ��������
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
	 * ���ڵ��� ��ü ������ ���Ѵ�.
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
	 * ���õ� ���ڰ� level,ancestor�� ���� ��ǰ�� �ִ� gcode ���� �����´�. 
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
	 * ���õ� ������ȣ�� �ش��ϴ� ��ǰ������ gcode ���� �����´�.
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
	 * ���õ� gcode�� �ش��ϴ� ��ǰ������ code���� �����´�.
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
// �Ʒ� ������δ� ResultSet�� ���Ϲ޴� �޼��忡�� Statement�� �ݾ����� ���ϴ� ����� ������� ���� 04.07.16
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/***********************
	 * ��ǰ�� ����Ʈ ��������
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
	 * ���õ� ��ǰ�� ������ ��ǰ ����Ʈ�� �����´�.
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
	 * ���õ� ��ǰ ������ �𵨱� ����Ʈ�� �����´�.
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
	 * ���õ� ���� �Ӽ����� �����´�.
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
// �������
//////////////

	/*******************************************************************************
	 * ���õ� ��ǰ�� ��ϵ� ǥ�� �������׸� �ڵ��� �ִ밪 + 1�� �����´�.
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
	 * std_template_list ���̺��� ���õ� �׸� ���� ������ ������
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
	 * std_template_list ���̺��� ���õ� ��ǰ�� �׸񸮽�Ʈ�� �����´�.
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
	 * ���ø� �׸� �����ϱ�
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
	 * ���ø� �׸� �����ϱ�
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
	 * �űԸ������� DB�� �����Ѵ�.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "��1|����1!��2|����2! ... ";
	 ****************************************/
	public void saveModelInfo(String three_class,String code,String name,String name2,String short_name,String color_code,String other_info,String code_str,String spec_str,String login_id) throws Exception{
		//�����з� ������ ���� �����ڵ�(gcode)�� ����Ѵ�.
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String gcode = goodsBO.calculateGcode("4",three_class);
		
		//��û�ð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String register_date = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String register_info		= userinfo.getDivision() + " " + userinfo.getUserRank() + " " + userinfo.getUserName();

		//�Ѿ�� �����׸��� ������ �ľ�
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//�������� �����.
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

		//DB�� �����ϱ�
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/****************************************
	 * ������ �������� DB�� �����Ѵ�.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "��1|����1!��2|����2! ... ";
	 ****************************************/
	public void modifyModelInfo(String mid,String three_class,String code,String name,String name2,String short_name,String color_code,String other_info,String code_str,String spec_str,String login_id) throws Exception{

		GoodsInfoTable table = new GoodsInfoTable();
		GoodsInfoBO goodsBO = new GoodsInfoBO(con);
		String sql  = "";

		//��û�ð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String modify_date = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String modifier_info		= userinfo.getDivision() + " " + userinfo.getUserRank() + " " + userinfo.getUserName();
		
/*�𵨱��� ������ �����ϰ� �� ���� �ҽ��� �ּ�ó����, �������� ����(2004.05.04 by ����)		

		//�𵨱� �з��� ����Ǿ����� üũ�ϱ� ���� ���� ancestor���� �����ͼ� ���Ѵ�.
		table = getGoodsInfoByMid(mid);
		String old_ancestor = table.getAncestor();

		//�𵨱� �з��� ����� ��쿡�� gcode�� �ٽ� ����Ͽ� db�� �ݿ��Ѵ�.
		if(!old_ancestor.equals(three_class)){
			String gcode = goodsBO.calculateGcode("4",three_class);
		
			//�Ѿ�� �����׸��� ������ �ľ�
			StringTokenizer str = new StringTokenizer(spec_str, "!");
			int spec_count = str.countTokens();

			//�������� �����.
			sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
			sql += ",ancestor='" + three_class + "',gcode='" + gcode + "'";
			for(int i=1; i<=spec_count; i++){ 
				String item = str.nextToken();
				sql += ",prop" + i + "='" + item + "'";
			}
			sql += " WHERE mid='" + mid +"'";

		//�𵨱� �з��� ������� ���� ���
		}else{
			//�Ѿ�� �����׸��� ������ �ľ�
			StringTokenizer str = new StringTokenizer(spec_str, "!");
			int spec_count = str.countTokens();

			//�������� �����.
			sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
			for(int i=1; i<=spec_count; i++){ 
				String item = str.nextToken();
				sql += ",prop" + i + "='" + item + "'";
			}
			sql += " WHERE mid='" + mid +"'";		
		}
������� */

		//�Ѿ�� �����׸��� ������ �ľ�
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//�������� �����.
		sql  = "UPDATE goods_structure SET code='" + code + "',name='" + name + "',name2='" + name2 + "',short_name='" + short_name + "',color_code='" + color_code + "',other_info='" + other_info + "',modifier_id='" + login_id + "',modifier_info='" + modifier_info + "',modify_date='" + modify_date + "',spec_list='" + code_str + "'";
		for(int i=1; i<=spec_count; i++){ 
			String item = str.nextToken();
			sql += ",prop" + i + "='" + item + "'";
		}
		sql += " WHERE mid='" + mid +"'";		


		//DB�� �����ϱ�
		Statement stmt = con.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}

	/*******************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ���� ���帮��Ʈ���� �����´�.
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
	 * ���ڵ� �ߺ��� üũ�ϱ� ���� ���õ� ���ڵ��� ���ڵ� ������ ����Ѵ�.
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
	 * ������ �𵨱԰������� ���� ���ڵ尪�� �����´�.
	 * ���� ��쿡�� ������ �����Ѵ�.
	 * code_str == "1101001,1101002,1101003,1101004,...."
	 * spec_str == "��1|����1!��2|����2! ... ";
	 ****************************************/
	public String getModelCodeWithSameProperty(String code_str,String spec_str) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String model_code = "";

		//�Ѿ�� �����׸��� ������ �ľ�
		StringTokenizer str = new StringTokenizer(spec_str, "!");
		int spec_count = str.countTokens();

		//�������� �����.
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
	 * ���ڵ����� �����ϱ�
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
	 * model_code_info ���̺��� ���õ� ���� �ִ� �Ļ��ڵ带 �����´�.
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
	 * model_code_info ���̺��� ���õ� ���� �ִ� ��ɱ����ڵ带 �����´�.
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
	 * ���õ� ������ �����ϱ�
	 *******************************/
	public void deleteModelInfo(String mid) throws Exception{
		Statement stmt = null;
		String query = "DELETE goods_structure WHERE mid = '" + mid + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * ���õ� ���ڵ����� �����ϱ�
	 *******************************/
	public void deleteModelCodeInfo(String code) throws Exception{
		Statement stmt = null;
		String query = "DELETE model_code_info WHERE code = '" + code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

}		