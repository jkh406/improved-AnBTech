package com.anbtech.cm.business;

import com.anbtech.cm.entity.*;
import com.anbtech.cm.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class CodeMgrBO{

	private Connection con;

	public CodeMgrBO(Connection con){
		this.con = con;
	}

	/**********************************************************
	 * ǰ��ä���Ƿ� �Է� �� �� ���� ���� �ʿ��� �Է� �����͸�
	 * �����´�.(ǥ��ǰ �� ���ǰ)
	 **********************************************************/
	public PartInfoTable getPartRegForm(String mode,String code_b,String code_m,String code_s,String item_no) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		PartInfoTable table = new PartInfoTable();
		
		String code_b_list	= "";		// ��з� ǰ�� html
		String code_m_list	= "";		// �ߺз� ǰ�� html
		String code_s_list	= "";		// �Һз� ǰ�� html
		String item_desc	= "";		// ǰ�񼳸�
		String mfg_no		= "";		// ��ü�ڵ�
		String item_name	= "";		// ǰ���
		String item_type	= "";		// ǰ�����
		String stock_unit	= "";		// ǰ��������

		//��з�ǰ�� ���� ǰ������� ����Ʈ�� �������ش�.
		if(code_b.equals("1")) item_type = "1";
		else if(code_b.equals("2")) item_type = "4";
		else if(code_b.equals("3")) item_type = "4";
		else if(code_b.equals("4")) item_type = "4";
		else if(code_b.equals("5")) item_type = "4";
		else if(code_b.equals("6")) item_type = "";
		else if(code_b.equals("7")) item_type = "";
		else if(code_b.equals("8")) item_type = "";
		else if(code_b.equals("9")) item_type = "4";
		else if(code_b.equals("F")) item_type = "M";

		if(mode.equals("modify_item") && item_no != null){
			table = cmDAO.getItemInfo(item_no);

			item_desc	= table.getItemDesc();
			mfg_no		= table.getMfgNo();
			item_name	= table.getItemName();
			item_type	= table.getItemType();
			stock_unit	= table.getStockUnit();
		}

		//��з� ǰ�� ����Ʈ
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = cmDAO.getItemClassListByItemCode("1","");
		while(rs.next()){
			code_b_list += "<option value='" + rs.getString("item_code") + "'";
			if(code_b.equals(rs.getString("item_code"))) code_b_list += " selected";
			code_b_list += ">" + rs.getString("item_name") + "</option>";
		}

		//�ߺз� ǰ�� ����Ʈ
		if(!code_b.equals("")){
			sql = "SELECT * FROM item_class WHERE item_level = '2' and item_code LIKE '" + code_b +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = cmDAO.getItemClassListByItemCode("2",code_b);
			while(rs.next()){
				code_m_list += "<option value='" + rs.getString("item_code") + "'";
				if(code_m.equals(rs.getString("item_code"))) code_m_list += " selected";
				code_m_list += ">" + rs.getString("item_name") + "</option>";
			}
		}

		//�Һз� ǰ�� ����Ʈ
		if(!code_m.equals("")){
			sql = "SELECT * FROM item_class WHERE item_level = '3' and item_code LIKE '" + code_m +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = cmDAO.getItemClassListByItemCode("3",code_m);
			while(rs.next()){
				code_s_list += "<option value='" + rs.getString("item_code") + "'";
				if(code_s.equals(rs.getString("item_code"))){
					code_s_list += " selected";
					//ǰ����� �Һз����� ����Ʈ�� �������ش�.
					item_name = rs.getString("item_name");
				}
				code_s_list += ">" + rs.getString("item_name") + "</option>";
			}
		}

		table.setCodeBig(code_b_list);
		table.setCodeMid(code_m_list);
		table.setCodeSmall(code_s_list);
		table.setItemNo(item_no);
		table.setItemDesc(item_desc);
		table.setMfgNo(mfg_no);
		table.setItemName(item_name);
		table.setItemType(item_type);
		table.setStockUnit(stock_unit);

		stmt.close();
		rs.close();

		return table;
	}

	/**********************************************************
	 * ����ǰ�ڵ� ����� �� �������� �ʿ��� �Է� �����͸� �����´�.
	 **********************************************************/
	public PartInfoTable getFgRegForm(String mode,String code_b,String one_class,String two_class,String item_no,String model_code) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO		= new com.anbtech.gm.db.GoodsInfoDAO(con);
		PartInfoTable table = new PartInfoTable();
		
		String code_b_list		= "";		// ��з� ǰ�� html
		String one_class_list	= "";		// ��ǰ�� ����Ʈ
		String two_class_list	= "";		// ��ǰ ����Ʈ
		String item_desc		= "";		// ǰ�񼳸�
		if(model_code == null) model_code = "";

		if(mode.equals("modify_fg") && item_no != null){
			table = cmDAO.getItemInfo(item_no);

			item_desc = table.getItemDesc();
			model_code = table.getModelCode();
		}

		//��з� ǰ�� ����Ʈ
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = cmDAO.getItemClassListByItemCode("1","");
		while(rs.next()){
			code_b_list += "<option value='" + rs.getString("item_code") + "'";
			if(code_b.equals(rs.getString("item_code"))) code_b_list += " selected";
			code_b_list += ">" + rs.getString("item_name") + "</option>";
		}

		//��ǰ�� ����Ʈ ��������
		sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '1' ORDER BY mid ASC";
		rs = stmt.executeQuery(sql);
//		rs = goodsDAO.getOneClassList();
		while(rs.next()){
			one_class_list += "<option value='" + rs.getString("gcode") + "'";
			if(one_class.equals(rs.getString("gcode"))) one_class_list += " selected";
			one_class_list += ">" + rs.getString("name") + "</option>";
		}

		//���õ� ��ǰ�� ������ ��ǰ ����Ʈ ��������
		if(one_class != null && !one_class.equals("")){
			sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '2' and gcode LIKE '" + one_class +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = goodsDAO.getTwoClassList(one_class);
			while(rs.next()){
				two_class_list += "<option value='" + rs.getString("gcode") + "'";
				if(two_class.equals(rs.getString("gcode"))) two_class_list += " selected";
				two_class_list += ">" + rs.getString("name") + "</option>";
			}
		}

		table.setCodeBig(code_b_list);
		table.setCodeMid(one_class_list);
		table.setCodeSmall(two_class_list);
		table.setItemNo(item_no);
		table.setItemDesc(item_desc);
		table.setModelCode(model_code);

		stmt.close();
		rs.close();

		return table;
	}

	/**********************************************************
	 * ASSY�ڵ� ����� �� �������� �ʿ��� �Է� �����͸� �����´�.
	 **********************************************************/
	public PartInfoTable getAssyRegForm(String mode,String code_b,String one_class,String two_class,String item_no,String model_code) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO		= new com.anbtech.gm.db.GoodsInfoDAO(con);
		PartInfoTable table = new PartInfoTable();
		
		String code_b_list		= "";		// ��з� ǰ�� html
		String one_class_list	= "";		// ��ǰ�� ����Ʈ
		String two_class_list	= "";		// ��ǰ ����Ʈ
		String item_desc		= "";		// ǰ�񼳸�
		if(model_code == null) model_code = "";

		if(mode.equals("modify_assy") && item_no != null){
			table = cmDAO.getItemInfo(item_no);

			item_desc = table.getItemDesc();
			model_code = table.getModelCode();
		}

		//��з� ǰ�� ����Ʈ
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = cmDAO.getItemClassListByItemCode("1","");
		while(rs.next()){
			code_b_list += "<option value='" + rs.getString("item_code") + "'";
			if(code_b.equals(rs.getString("item_code"))) code_b_list += " selected";
			code_b_list += ">" + rs.getString("item_name") + "</option>";
		}

		//��ǰ�� ����Ʈ ��������
		sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '1' ORDER BY mid ASC";
		rs = stmt.executeQuery(sql);
//		rs = goodsDAO.getOneClassList();
		while(rs.next()){
			one_class_list += "<option value='" + rs.getString("gcode") + "'";
			if(one_class.equals(rs.getString("gcode"))) one_class_list += " selected";
			one_class_list += ">" + rs.getString("name") + "</option>";
		}

		//���õ� ��ǰ�� ������ ��ǰ ����Ʈ ��������
		if(one_class != null && !one_class.equals("")){
			sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '2' and gcode LIKE '" + one_class +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = goodsDAO.getTwoClassList(one_class);
			while(rs.next()){
				two_class_list += "<option value='" + rs.getString("gcode") + "'";
				if(two_class.equals(rs.getString("gcode"))) two_class_list += " selected";
				two_class_list += ">" + rs.getString("name") + "</option>";
			}
		}

		table.setCodeBig(code_b_list);
		table.setCodeMid(one_class_list);
		table.setCodeSmall(two_class_list);
		table.setItemNo(item_no);
		table.setItemDesc(item_desc);
		table.setModelCode(model_code);

		stmt.close();
		rs.close();

		return table;
	}	

	/************************
	 * ǰ������ �� ���� ��
	 ************************/
	public PartInfoTable getViewForm(String item_no) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		PartInfoTable part = new PartInfoTable();

		part = cmDAO.getItemInfo(item_no);

		//Maker
		part.setMfgNo(cmDAO.getMfgName(part.getMfgNo()) + "["+part.getMfgNo()+"]");
		
		return part;
	}


	/***************************************************
	 * ���õ� �Һз� ǰ���ڵ忡 ���ǵ� ���� ����Ʈ�� �����´�.
	 * ���� �ÿ��� ���� �� �� ������ ���� �����Ͽ� �ش�.
	 ***************************************************/
	public ArrayList getSpecList(String mode,String code_s,String item_no) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		SpecCodeTable spec = new SpecCodeTable();
		ArrayList spec_list =  new ArrayList();

		if(mode.equals("reg_item") || mode.equals("search_by_spec")){ // �űԵ�� �� �˻�
			//item_spec ���̺��� �ش� �Һз� ǰ�� ���ǵ� ���帮��Ʈ�� �����´�.
			String code_string = cmDAO.getCodeStr(code_s); //code_string = 31001|y|y,31002|y|n,31003|y|y,...

			StringTokenizer str = new StringTokenizer(code_string, ",");
			String each_code[] = new String[str.countTokens()]; 

			int i = 0;
			while(str.hasMoreTokens()){ 
				each_code[i] = str.nextToken();

				int m = each_code[i].indexOf("|");
				String spec_code = each_code[i].substring(0,m);
				String is_essence = each_code[i].substring(m+1,m+2);
				String is_desc = each_code[i].substring(m+3,m+4);

				spec = new SpecCodeTable();
				spec = cmDAO.getSpecInfo(spec_code);
				String spec_name = spec.getSpecName();
				String spec_value = spec.getSpecValue();
				String spec_unit = spec.getSpecUnit();
				String write_exam = spec.getWriteExam();
				String spec_desc = spec.getSpecDesc();

				String spec_name_s = "<a onmouseover=\"ANB_layerAction(z" + spec_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + spec_code + ", 'hidden')\">" + spec_name + "</a>";
				spec_name_s += "<div id=z" + spec_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + spec_desc + "</div>";

				if(is_essence.equals("y")) spec_name_s += " *"; 

				spec.setSpecName(spec_name_s);
				spec.setSpecValue(getSpecValueHtml(spec_code,spec_value,"",""));
				spec.setSpecUnit(getSpecUnitHtml(spec_code,spec_unit,"",""));
				spec.setIsEssence(is_essence);
				spec.setIsDesc(is_desc);

				spec_list.add(spec);
				++i;
			}
		}

		else if((mode.equals("view_item") || mode.equals("modify_item")) && item_no != null){
			//item_spec ���̺��� �ش� �Һз� ǰ�� ���ǵ� ���帮��Ʈ�� �����´�.
			String code_string = cmDAO.getCodeStr(code_s); //code_string = 31001|y|y,31002|y|n,31003|y|y,...

			//���õ� ǰ���ȣ�� �ش��ϴ� ǰ������ ��������
			Statement stmt = null;
			ResultSet rs = null;
			PartInfoTable table = new PartInfoTable();

			String sql = "SELECT * FROM item_master WHERE item_no = '" + item_no + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			//ResultSet rs = cmDAO.getItemInfoWithResultSet(item_no);
			rs.next();

			StringTokenizer str = new StringTokenizer(code_string, ",");
			String each_code[] = new String[str.countTokens()]; 

			int i = 0;
			String read_only = "";
			if(mode.equals("view_item")) read_only = " disabled";

			while(str.hasMoreTokens()){ 
				each_code[i] = str.nextToken();

				int m = each_code[i].indexOf("|");
				String spec_code = each_code[i].substring(0,m);
				String is_essence = each_code[i].substring(m+1,m+2);
				String is_desc = each_code[i].substring(m+3,m+4);

				spec = new SpecCodeTable();
				spec = cmDAO.getSpecInfo(spec_code);
				String spec_name = spec.getSpecName();
				String spec_value = spec.getSpecValue();
				String spec_unit = spec.getSpecUnit();
				String write_exam = spec.getWriteExam();
				String spec_desc = spec.getSpecDesc();

				//db�� ����� ���� �����Ѵ�.
				String db = rs.getString("prop" + spec_code.substring(spec_code.length()-2));

				String db_value = "";
				String db_unit = "";
				int offset = db.indexOf("|");
				if(offset >= 0){
					db_value = db.substring(0,offset);
					db_unit = db.substring(offset + 1);
				}

				String spec_name_s = "<a onmouseover=\"ANB_layerAction(z" + spec_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + spec_code + ", 'hidden')\">" + spec_name + "</a>";
				spec_name_s += "<div id=z" + spec_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + spec_desc + "</div>";

				if(is_essence.equals("y")) spec_name_s += " *"; 

				spec.setSpecName(spec_name_s);
				spec.setSpecValue(getSpecValueHtml(spec_code,spec_value,db_value,read_only));
				spec.setSpecUnit(getSpecUnitHtml(spec_code,spec_unit,db_unit,read_only));
				spec.setIsEssence(is_essence);
				spec.setIsDesc(is_desc);

				spec_list.add(spec);

				++i;
			}
			stmt.close();
			rs.close();
		}	
		return spec_list;
	}

	/***************************************************
	 * ���õ� �ߺз� �Ǵ� �Һз� ǰ���ڵ忡 ���ǵ� ���� ����Ʈ��
	 * �����´�.
	 ***************************************************/
	public ArrayList getSpecList(String code_m,String code_s) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		SpecCodeTable spec = new SpecCodeTable();
		ArrayList spec_list =  new ArrayList();

		if(!code_m.equals("") && code_s.equals("")){
			//spec_code ���̺��� �ش� �ߺз� ǰ�� ���ǵ� ���帮��Ʈ�� �����´�.
			ArrayList std_tmp_list =  new ArrayList();
			std_tmp_list = cmDAO.getStdTemplateSpecList(code_m);
			Iterator spec_iter = std_tmp_list.iterator();

			while(spec_iter.hasNext()){
				spec = (SpecCodeTable)spec_iter.next();

				String spec_code = spec.getSpecCode();
				String spec_name = spec.getSpecName();
				String spec_value = spec.getSpecValue();
				String spec_unit = spec.getSpecUnit();
				String write_exam = spec.getWriteExam();
				String spec_desc = spec.getSpecDesc();

				String spec_name_s = "<a onmouseover=\"ANB_layerAction(z" + spec_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + spec_code + ", 'hidden')\">" + spec_name + "</a>";
				spec_name_s += "<div id=z" + spec_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + spec_desc + "</div>";

				spec.setSpecName(spec_name_s);
				spec.setSpecValue(getSpecValueHtml(spec_code,spec_value,"",""));
				spec.setSpecUnit(getSpecUnitHtml(spec_code,spec_unit,"",""));

				spec_list.add(spec);
//				System.out.println(spec_code);
			}

		}else if(!code_s.equals("")){
			//item_spec ���̺��� �ش� �Һз� ǰ�� ���ǵ� ���帮��Ʈ�� �����´�.
			String code_string = cmDAO.getCodeStr(code_s); //code_string = 31001|y|y,31002|y|n,31003|y|y,...

			StringTokenizer str = new StringTokenizer(code_string, ",");
			String each_code[] = new String[str.countTokens()]; 

			int i = 0;
			while(str.hasMoreTokens()){ 
				each_code[i] = str.nextToken();

				int m = each_code[i].indexOf("|");
				String spec_code = each_code[i].substring(0,m);
				String is_essence = each_code[i].substring(m+1,m+2);
				String is_desc = each_code[i].substring(m+3,m+4);

				spec = new SpecCodeTable();
				spec = cmDAO.getSpecInfo(spec_code);
				String spec_name = spec.getSpecName();
				String spec_value = spec.getSpecValue();
				String spec_unit = spec.getSpecUnit();
				String write_exam = spec.getWriteExam();
				String spec_desc = spec.getSpecDesc();

				String spec_name_s = "<a onmouseover=\"ANB_layerAction(z" + spec_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + spec_code + ", 'hidden')\">" + spec_name + "</a>";
				spec_name_s += "<div id=z" + spec_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + spec_desc + "</div>";

				if(is_essence.equals("y")) spec_name_s += " *"; 

				spec.setSpecName(spec_name_s);
				spec.setSpecValue(getSpecValueHtml(spec_code,spec_value,"",""));
				spec.setSpecUnit(getSpecUnitHtml(spec_code,spec_unit,"",""));
				spec.setIsEssence(is_essence);
				spec.setIsDesc(is_desc);

				spec_list.add(spec);
				++i;
			}
		}
		return spec_list;
	}

	/****************************
	 * �������� �˻� ������ �����.
	 ****************************/
	public String getWhere(String mode,String category,String code_b,String code_m,String code_s,String searchword,String searchscope,String login_id) throws Exception{
		String where = "", where_and = "", where_sea = "", where_cat = "";

/*
		if(code_b.length() > 0) where_cat = "item_no LIKE '" + code_b + "%' ";
		if(code_m.length() > 0) where_cat = "item_no LIKE '" + code_m + "%' ";
		if(code_s.length() > 0) where_cat = "item_no LIKE '" + code_s + "%' ";
*/
		if(category.length() > 0) where_cat = "item_no LIKE '" + category + "%' ";

		if (searchword.length() > 0){
			if (searchscope.equals("item_no")){
				where_sea += "( item_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("item_desc")){
				where_sea += "( item_desc LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("mfg_no")){
				where_sea += "( mfg_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("register_info")){
				where_sea += "( register_info LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("register_date")){
				String s_date = searchword.substring(0,4) + "-" + searchword.substring(4,6) + "-" + searchword.substring(6,8);
				String e_date = searchword.substring(8,12) + "-" + searchword.substring(12,14) + "-" + searchword.substring(14,16);
				where_sea += "(register_date >= '" + s_date + "' and register_date <= '" + e_date +"')";
			}
		}

		if (searchword.length() > 0 && searchscope.equals("spec")){
				where_sea = getWhereSea(searchword);
		}
		if(where_cat.length() > 0 && searchword.length() > 0) where_and = " AND ";

		if(where_cat.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;
		if(where_cat.length() > 0 && searchscope.equals("spec")) where = " WHERE " + where_cat + where_sea;

		return where;
	}

	/***************************************************
	 * �󼼽���˻����� �˻����ڿ�(where_sea)�� �����.
	 * searchword = "32001|MT1585DT|na,32002|SMD|na,32003|50|mA, ..."
	 ***************************************************/
	public String getWhereSea(String searchword) throws Exception{
		String where_sea = "";

		if (searchword.length() > 0){
			StringTokenizer str = new StringTokenizer(searchword,",");
			int spec_count = str.countTokens();
			String[] each_item = new String[spec_count];
			
			int i=0;
			while(str.hasMoreTokens()){ 
				each_item[i] = str.nextToken();	//each_item == '32001|IDT74FCT165|na'

				String spec_code = each_item[i].substring(0,each_item[i].indexOf("|"));
				String spec_value = each_item[i].substring(each_item[i].indexOf("|")+1);
				where_sea += " AND (prop" + spec_code.substring(spec_code.length()-2) + " = '" + spec_value + "') ";

				i++;
			}
		}
		return where_sea;
	}

	/************************************
	 * ���ϼӼ��� ǰ���� ��ϵǾ� �ִ��� üũ
	 * �ߺ�üũ ����:���� �Һз�ǰ�� �� ���� �󼼽����� �����°�?
	 ***********************************/
	public String checkSameItemExist(String item_no,String code_s,String mfg_no,String spec_str) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		String where_sea = getWhereSea(spec_str);

		String where = " WHERE item_no LIKE '" + code_s + "%'" + where_sea;
		String same_item = cmDAO.getSameItemWithSpec(where);

		return same_item;
	}


	/************************************
	 * ǰ�� ǥ�����ø� ���� �߰� �� ���� ��
	 ************************************/
	public SpecCodeTable getAddStdTemplateCodeForm(String mode,String code_big,String code_mid,String spec_code) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		SpecCodeTable table = new SpecCodeTable();
		
		String code_big_list	= "";	// ��з� ǰ�� ����Ʈ
		String code_mid_list	= "";	// �ߺз� ǰ�� ����Ʈ	
		String next_code		= "";	// �����ڵ尪
		String spec_name		= ""; 	// ���� �̸�
		String spec_value		= "";	// ���� ��
		String spec_unit		= "";	// ���� ����
		String write_exam		= "";	// �Է¿�
		String spec_desc		= "";	// ���忡 ���� ����

		//��з� ǰ�� ����Ʈ
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '1' and item_ancestor = '0' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = cmDAO.getItemClassList("1","0");
		while(rs.next()){
			code_big_list += "<option value='" + rs.getString("mid") + "'";
			if(code_big.equals(rs.getString("mid"))) code_big_list += " selected";
			code_big_list += ">" + rs.getString("item_name") + "</option>";
		}

		//�ߺз� ǰ�� ����Ʈ
		sql = "SELECT * FROM item_class WHERE item_level = '2' and item_ancestor = '" + code_big +"' ORDER BY mid ASC";
		rs = stmt.executeQuery(sql);
//		rs = cmDAO.getItemClassList("2",code_big);
		while(rs.next()){
			code_mid_list += "<option value='" + rs.getString("item_code") + "'";
			if(code_mid.equals(rs.getString("item_code"))) code_mid_list += " selected";
			code_mid_list += ">" + rs.getString("item_name") + "</option>";
		}

		//�����ڵ� ��
		if(mode.equals("add_template_code") && !code_mid.equals("") ) next_code = cmDAO.getMaxSpecCode(code_mid);

		//���� ����� ���
		if(mode.equals("modify_template_code") && spec_code != null){
			table = cmDAO.getSpecInfo(spec_code);
			next_code	= table.getSpecCode();
			spec_name	= table.getSpecName();
			spec_value	= table.getSpecValue();
			spec_unit	= table.getSpecUnit();
			write_exam	= table.getWriteExam();
			spec_desc	= table.getSpecDesc();
		}

		table.setCodeBig(code_big_list);
		table.setCodeMid(code_mid_list);
		table.setSpecCode(next_code);
		table.setSpecName(spec_name);
		table.setSpecValue(spec_value);
		table.setSpecUnit(spec_unit);
		table.setWriteExam(write_exam);
		table.setSpecDesc(spec_desc);

		stmt.close();
		rs.close();

		return table;
	}


	/************************************************************************
	 * spec_code ���̺��� �ش� ǰ��з��� ���ǵ� ǥ�����ø� ���帮��Ʈ�� ������ ��,
	 * HTML �ڵ�� �����. 
	 ************************************************************************/
	public ArrayList getStdTemplateSpecList(String code) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		SpecCodeTable table = new SpecCodeTable();
		ArrayList spec_list =  new ArrayList();
		ArrayList html_list =  new ArrayList();
		spec_list = (ArrayList)cmDAO.getStdTemplateSpecList(code);

		//�� �����׸� ǥ�ø� ���� html �ڵ带 �����Ѵ�.
		Iterator table_iter = spec_list.iterator();
		while(table_iter.hasNext()){
			table = (SpecCodeTable)table_iter.next();

			//�����׸� html ����
			table.setSpecValue(getSpecValueHtml(table.getSpecCode(),table.getSpecValue(),"",""));

			//������� html ����
			table.setSpecUnit(getSpecUnitHtml(table.getSpecCode(),table.getSpecUnit(),"",""));

			//������ư
			String link_modify = "javascript:viewCode('" + table.getSpecCode() + "');";
			table.setLinkModify(link_modify);

			html_list.add(table);
		}

		return html_list;
	}


	/*************************************
	 * ���尪�� ���� HTML �ڵ带 �����Ѵ�.
	 *************************************/
	public String getSpecValueHtml(String spec_code,String spec_value,String db_value,String read_only) throws Exception{
		String html_value = "";

		if(spec_value.equals("")){ //spec_value == "" �� ��쿡�� �ؽ�Ʈ �ڽ���
			html_value = "<input type=text name=v_"+spec_code+" value='" + db_value + "' size=25" + read_only + ">";
		}else if(spec_value.indexOf(".") == 1){ //m.n ������ ��쿡�� ���� �ڹٽ�ũ��Ʈ�� ������ �ش�.

		}else if(spec_value.indexOf(",") > 0){ //spec_value�� �׸��� ������ ��� �޺��ڽ��� �� �׸��� ����				
			int i = 0; 
			StringTokenizer str = new StringTokenizer(spec_value, ",");
			String item[] = new String[str.countTokens()]; 
			html_value = "<select name=v_"+spec_code + read_only + "><option value=''>����</option>";
			while(str.hasMoreTokens()){ 
				item[i] = str.nextToken(); //item[i] == "SOP(Small Outline Package) or SOP,DIP"
/* ��ȣ�� �ν����� �ʴ� ������ �־� ������.
				String option_value = "";
				String option_text	= "";

				if(item[i].indexOf("(") > 0){
					option_value = item[i].substring(0,item[i].indexOf("("));
					option_text	= item[i].substring(item[i].indexOf("(")+1,item[i].indexOf(")"));
				}else{
					option_value = item[i];
					option_text	= item[i];
				}
*/
				String option_value = item[i];
				String option_text	= item[i];

				html_value += "<option value='"+option_value+"'";
				if(option_value.equals(db_value)) html_value += " selected";
				html_value += ">"+option_text+"</option>";
				++i; 
			}
			html_value += "</select>";
		}

		return html_value;
	}

	/*************************************
	 * ��������� ���� HTML �ڵ带 �����Ѵ�.
	 *************************************/
	public String getSpecUnitHtml(String spec_code,String spec_unit,String db_unit,String read_only) throws Exception{
		String html_value = "";

		if(spec_unit.equals("")){
			html_value = "<input type='hidden' name='u_"+spec_code+"' value='na' " + read_only + ">";
		}else{
			int i = 0; 
			StringTokenizer str = new StringTokenizer(spec_unit, ",");
			String item[] = new String[str.countTokens()]; 
			html_value = "<select name=u_"+spec_code + read_only + ">";
				while(str.hasMoreTokens()){ 
					item[i] = str.nextToken();
					html_value += "<option value='"+item[i]+"'";
					if(item[i].equals(db_unit)) html_value += " selected";					
					html_value += ">"+item[i]+"</option>";
					++i; 
				}
				html_value += "</select>";
			}

		return html_value;
	}

	/******************************************
	 * ÷������ ���ε� ó��
	 ******************************************/
	public PartInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception
	{
		String filename = "";
		String filetype = "";
		String filesize = "";
		String filese ="";
		String fumask ="";
		String did = "";

		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename�� type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);
				
				//file�� ����� ������Ѱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+i+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);
				fumask = fumask + (no+"_"+i)+"|";
				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
			}
			i++;
		}
		com.anbtech.cm.entity.PartInfoTable file = new com.anbtech.cm.entity.PartInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(fumask);

		return file;
	} //getFile_frommulti()

	/******************************************
	 * ÷������ ���ε� ó�� (������)
	 ******************************************/
	public PartInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String fileumask = "";
	
		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			PartInfoTable file = new PartInfoTable();
			if(file_iter.hasNext()) file = (PartInfoTable)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename�� type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file�� ����� ������Ѱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);
				fileumask = fileumask+(no+"_"+j)+"|";
				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize + fsize + "|";
				j++;
			
			}else 	if(deletefile != null){
			
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			
			}else 	if(file.getFileName() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				fileumask = fileumask+(no+"_"+i)+"|";
				filename = filename + file.getFileName() + "|";
				filetype = filetype + file.getFileType() + "|";
				filesize = filesize + file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		PartInfoTable file = new PartInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(fileumask);

		return file;
	}//getFile_frommulti()

	/**************************************************
	 * ÷������ �ٿ�ε�
	 **************************************************/
	public PartInfoTable getFile_fordown(String no) throws Exception
	{
		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
		//String tablename = "item_master";
		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));
		Iterator file_iter = cmDAO.getFile_list(fileno).iterator();

		String filename="",filetype="",filesize="",fileumask="";
		int i = 1;
		while (file_iter.hasNext()){
			PartInfoTable file = (PartInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
				fileumask= file.getFileUmask();
				//System.out.println(filename);
			}
			i++;
		}
		PartInfoTable file = new PartInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(fileumask);

		return file;
	}


	/**************************************************
	 * ÷������ ������ DB�� �����ϱ� ���� ������ ����
	 **************************************************/
	public void updFile(String no, String filename, String filetype, String filesize,String fileumask) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"', umask='"+fileumask+"'";
		String where = " WHERE item_no = '" + no + "'";

		com.anbtech.cm.db.CodeMgrDAO cmDAO = new com.anbtech.cm.db.CodeMgrDAO(con);
		cmDAO.updTable(set, where);
	}

	/**************************************************
	 * Assy Code ��� �� Description�� �����Ѵ�.
	 **************************************************/
	public String getDescForAssy(String item_type, String assy_type,String op_code,String model_code, String model_name,String config_name) throws Exception{
		String desc = "";

		String item_type_s = "";
		if(item_type.equals("E")) item_type_s = "ȸ�� ";
		else if(item_type.equals("M")) item_type_s = "�ⱸ ";
		else if(item_type.equals("C")) item_type_s = "���̺� ";
		else if(item_type.equals("PH")) item_type_s = "PHANTOM ";

		if(assy_type.equals("S")) desc = item_type_s + "Assy Set," + model_code + "," + model_name;
		else if(assy_type.equals("G")) desc = config_name + " Assy";
		else if(assy_type.equals("P")) desc = config_name + " " + op_code + " Assy";
		else desc = config_name + " Phantom Assy";

		return desc;
	}

	/**********************************************************
	 * ǰ��з� �޺��ڽ��� �����´�.
	 **********************************************************/
	public PartInfoTable getItemClass(String mode,String code_b,String code_m,String code_s) throws Exception{
		CodeMgrDAO cmDAO = new CodeMgrDAO(con);
		PartInfoTable table = new PartInfoTable();
		
		String code_b_list	= "";		// ��з� ǰ�� html
		String code_m_list	= "";		// �ߺз� ǰ�� html
		String code_s_list	= "";		// �Һз� ǰ�� html

		//��з� ǰ�� ����Ʈ
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM item_class WHERE item_level = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = cmDAO.getItemClassListByItemCode("1","");
		while(rs.next()){
			code_b_list += "<option value='" + rs.getString("item_code") + "'";
			if(code_b.equals(rs.getString("item_code"))) code_b_list += " selected";
			code_b_list += ">" + rs.getString("item_name") + "</option>";
		}

		//�ߺз� ǰ�� ����Ʈ
		if(!code_b.equals("")){
			sql = "SELECT * FROM item_class WHERE item_level = '2' and item_code LIKE '" + code_b +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = cmDAO.getItemClassListByItemCode("2",code_b);
			while(rs.next()){
				code_m_list += "<option value='" + rs.getString("item_code") + "'";
				if(code_m.equals(rs.getString("item_code"))) code_m_list += " selected";
				code_m_list += ">" + rs.getString("item_name") + "</option>";
			}
		}

		//�Һз� ǰ�� ����Ʈ
		if(!code_m.equals("")){
			sql = "SELECT * FROM item_class WHERE item_level = '3' and item_code LIKE '" + code_m +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = cmDAO.getItemClassListByItemCode("3",code_m);
			while(rs.next()){
				code_s_list += "<option value='" + rs.getString("item_code") + "'";
				if(code_s.equals(rs.getString("item_code"))){
					code_s_list += " selected";
				}
				code_s_list += ">" + rs.getString("item_name") + "</option>";
			}
		}

		table.setCodeBig(code_b_list);
		table.setCodeMid(code_m_list);
		table.setCodeSmall(code_s_list);

		stmt.close();
		rs.close();

		return table;
	}

}