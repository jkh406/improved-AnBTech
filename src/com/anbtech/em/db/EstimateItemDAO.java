package com.anbtech.em.db;

import com.anbtech.em.entity.*;
import com.anbtech.em.business.*;
import com.anbtech.em.db.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;

public class EstimateItemDAO{
	private Connection con;

	public EstimateItemDAO(Connection con){
		this.con = con;
	}

	/***************************************
	 * �ܺα������� ����ǰ�� ����Ʈ ��������
	 ***************************************/
	public ArrayList getOutItemList(String mode,String searchword,String searchscope,String category,String page) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 17;			// ������������ ����� ���ڵ� ��
		int l_maxpage = 7;			// ���������� ǥ���� �ٷΰ��� �������� ��
		int l_maxsubjectlen = 20;	// ������ �ִ� ǥ�ñ���
		int current_page_num =Integer.parseInt(page);

		ArrayList item_list = new ArrayList();
		EstimateItemBO itemBO = new EstimateItemBO(con);
		ItemInfoTable item = new ItemInfoTable();
		
		String where = itemBO.getWhere(mode,searchword,searchscope,category);
		int total = getTotalCount("v_out_item_list", where);
		int recNum = total;

		String query = "SELECT * FROM v_out_item_list " + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String category_code		= rs.getString("category_code");	//ǰ��з�
			String item_name			= rs.getString("item_name");		//ǰ���
			String model_code			= "";		//���ڵ�
			String model_name			= rs.getString("model_name");		//�𵨸�
			String unit					= rs.getString("unit");				//ǰ�����
			String standards			= rs.getString("standards");		//ǰ��԰�(���)
			String maker_name			= rs.getString("maker_name");		//����ȸ���
			String supplyer_code		= rs.getString("supplyer_code");	//���޾�ü�ڵ�
			String supplyer_name		= "";
			String supply_cost			= rs.getString("supply_cost");		//���޿���
			String written_day			= rs.getString("written_day");		//�����

			if(mode.equals("list_out_item")){
				model_code += "<a href='EstimateMgrServlet?mode=view_out_item&model_code="+rs.getString("model_code")+"' ";
				model_code += "onMouseOver=\"window.status='ǰ�������󼼺���';return true;\" ";
				model_code += "onMouseOut=\"window.status='';return true;\" >";
				model_code += rs.getString("model_code") + "</a>";

				supplyer_name += "<a href='EstimateMgrServlet?mode=view_out_item_supply_info&model_code="+rs.getString("model_code")+"&supplyer_code="+supplyer_code+"' ";
				supplyer_name += "onMouseOver=\"window.status='���������󼼺���';return true;\" ";
				supplyer_name += "onMouseOut=\"window.status='';return true;\" >";
				supplyer_name += rs.getString("supplyer_name") + "</a>";

			}else if(mode.equals("search_out_item")){
				model_code = rs.getString("model_code");
				supplyer_name += "<a href=\"javascript:returnValue('"+rs.getString("item_name")+"','"+rs.getString("model_code")+"','"+rs.getString("model_name")+"','"+rs.getString("standards")+"','"+rs.getString("maker_name")+"','"+rs.getString("supplyer_code")+"','"+rs.getString("supplyer_name")+"','"+rs.getString("supply_cost")+"','"+rs.getString("unit")+"');\">";
				supplyer_name += rs.getString("supplyer_name") + "</a>";
			}

			item = new ItemInfoTable();

			item.setCategoryCode(category_code);
			item.setItemName(item_name);
			item.setModelCode(model_code);
			item.setModelName(model_name);
			item.setUnit(unit);
			item.setStandards(standards);
			item.setMakerName(maker_name);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setSupplyCost(supply_cost);
			item.setWrittenDay(written_day);
	
			item_list.add(item);
			recNum--;
		}
		stmt.close();
		rs.close();

		return item_list;
	}

	/***************************************
	 * v_out_item_list ���� ���õ� ǰ���ڵ�� ���޾�ü�ڵ忡
	 * �ش��ϴ� ���ڵ� ������ �����´�.
	 ***************************************/
	public ItemInfoTable getOutItemInfo(String model_code,String supplyer_code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ItemInfoTable item = new ItemInfoTable();

		String query = "SELECT TOP 1 * FROM v_out_item_list WHERE model_code = '" + model_code + "'  and supplyer_code = '" + supplyer_code + "' ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			String category_code		= rs.getString("category_code");
			String item_name			= rs.getString("item_name");
			String model_name			= rs.getString("model_name");
			String maker_name			= rs.getString("maker_name");
			String standards			= rs.getString("standards");
			String unit					= rs.getString("unit");
			String mid					= rs.getString("mid");
			String supplyer_name		= rs.getString("supplyer_name");
			String supply_cost			= rs.getString("supply_cost");
			String other_info			= rs.getString("other_info");
			String writer				= rs.getString("writer");
			String written_day			= rs.getString("written_day");
			String stat					= rs.getString("stat");

			Iterator file_iter = getFileList("v_out_item_list",mid).iterator();
					
			int j = 1;
			String filelink = "";
			while(file_iter.hasNext()){

				ItemInfoTable file = (ItemInfoTable)file_iter.next();
				filelink += "<a href='EstimateMgrServlet?mode=download&tablename=v_out_item_list&no="+(""+mid)+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../em/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0>"+file.getFileName()+" ("+file.getFileSize()+" bytes)</a>";
				j++;				
			}

			item = new ItemInfoTable();

			item.setCategoryCode(category_code);
			item.setItemName(item_name);
			item.setModelCode(model_code);
			item.setModelName(model_name);
			item.setMakerName(maker_name);
			item.setStandards(standards);
			item.setUnit(unit);
			item.setMid(mid);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setSupplyCost(supply_cost);
			item.setOtherInfo(other_info);
			item.setWriter(writer);
			item.setWrittenDay(written_day);
			item.setStat(stat);
			item.setLinkUrl(filelink);
		}
		stmt.close();
		rs.close();

		return item;
	}

	/***************************************
	 * ���õ� ����ǰ�� ���õ� ����ȸ���� �������� �����̷� ��������
	 ***************************************/
	public ArrayList getListSupplyInfo(String model_code,String supplyer_code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList list = new ArrayList();
		ItemInfoTable item = new ItemInfoTable();
		
		String query = "SELECT * FROM item_supply_info WHERE supplyer_code = '" + supplyer_code + "' AND model_code = '" + model_code + "' ORDER BY mid DESC";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){

			String mid					= rs.getString("mid");				//������ȣ
			String supplyer_name		= rs.getString("supplyer_name");	//����ȸ���
			String supply_cost			= rs.getString("supply_cost");		//���ް�
			String writer				= rs.getString("writer");
			String written_day			= rs.getString("written_day");

			Iterator file_iter = getFileList("item_supply_info",mid).iterator();

			int j = 1;
			String filelink = "&nbsp;";
			while(file_iter.hasNext()){
				ItemInfoTable file = (ItemInfoTable)file_iter.next();
				filelink += "<a href='EstimateMgrServlet?tablename=item_supply_info&mode=download&no="+mid+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../em/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0></a>";
				j++;
			}

			item = new ItemInfoTable();

			item.setMid(mid);
			item.setModelCode(model_code);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setSupplyCost(supply_cost);
			item.setWriter(writer);
			item.setWrittenDay(written_day);
			item.setFileName(filelink);

			list.add(item);
		}
		stmt.close();
		rs.close();

		return list;
	}

	/***************************************
	 * ÷������ ����Ʈ ��������
	 ***************************************/
	public ArrayList getFileList(String tablename,String mid) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT fname,ftype,fsize FROM " + tablename + " WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("fname")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("ftype")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("fsize")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				ItemInfoTable file = new ItemInfoTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}

	/***************************************
	 * ÷���������� ������Ʈ
	 ***************************************/
	public void updTable(String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE item_supply_info " + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * ǰ�� �������� ���� ó��(by ������ȣ)
	 *******************************/
	public void deleteOutItemSupplyInfo(String mid,String filepath) throws Exception{
		Statement stmt = null;

		// ÷�������� ������ ����
		for(int i=0; i < 10; i++){
			java.io.File f = new java.io.File(filepath + mid + "_"+(i+1) + ".bin");
			if (f.exists()) f.delete();
		}

		//���ڵ� ����
		String query = "DELETE item_supply_info WHERE mid = '" + mid + "'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}

	/******************************************************************
	 * ���õ� ���ڵ忡 �ش��ϴ� ��������ǰ���� ������ ��������
	 ******************************************************************/
	public ItemInfoTable getOutItemInfo(String model_code) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		ItemInfoTable item = new ItemInfoTable();

		String sql = "SELECT * FROM out_item_list WHERE model_code = '" + model_code +"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
		rs.next();

		item.setItemName(rs.getString("item_name"));
		item.setModelCode(rs.getString("model_code"));
		item.setModelName(rs.getString("model_name"));
		item.setStandards(rs.getString("standards"));
		item.setUnit(rs.getString("unit"));
		item.setMakerName(rs.getString("maker_name"));
		item.setWriter(rs.getString("writer"));
		item.setWrittenDay(rs.getString("written_day"));

		return item;
	}

	/******************************************************************
	 * ���õ� ǰ���� �����ϴ� ���������� ���� �ֱ� ����Ʈ ��������
	 ******************************************************************/
	public ArrayList getOutItemSupplyList(String model_code) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;

		ArrayList supplyer_list = new ArrayList();
		ItemInfoTable item = new ItemInfoTable();
		
		String query = "SELECT A.* FROM (SELECT supplyer_code, MAX(mid) mid FROM item_supply_info WHERE model_code = '" + model_code + "' GROUP BY supplyer_code) RE INNER JOIN item_supply_info A ON RE.mid = A.mid";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		while(rs.next()){
			String mid					= rs.getString("mid");				//������ȣ
			String supplyer_code		= rs.getString("supplyer_code");	//��ü�ڵ�
			String supplyer_name		= rs.getString("supplyer_name");	//��ü��
			String supply_cost			= rs.getString("supply_cost");		//���޴ܰ�
			String written_day			= rs.getString("written_day");		//�����

			Iterator file_iter = getFileList("item_supply_info",mid).iterator();

			int j = 1;
			String filelink = "&nbsp;";
			while(file_iter.hasNext()){
				ItemInfoTable file = (ItemInfoTable)file_iter.next();
				filelink += "<a href='EstimateMgrServlet?tablename=item_supply_info&mode=download&no="+mid+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../em/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0></a>";
				j++;
			}

			item = new ItemInfoTable();

			item.setMid(mid);
			item.setModelCode(model_code);
			item.setSupplyerCode(supplyer_code);
			item.setSupplyerName(supplyer_name);
			item.setSupplyCost(supply_cost);
			item.setWrittenDay(written_day);
			item.setFileName(filelink);

			supplyer_list.add(item);
		}

		stmt.close();
		rs.close();

		return supplyer_list;
	}

	/*******************************
	 * ��������ǰ������ ����
	 *******************************/
	public void saveOutItemInfo(String item_name,String unit,String model_name,String model_code,String standards,String maker_name,String login_id) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO out_item_list(item_name,unit,model_name,model_code,standards,maker_name,writer,written_day,modifier,modified_day) VALUES(?,?,?,?,?,?,?,?,?,?)";

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String register_info = login_id + "/" + userinfo.getUserName();

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,unit);
		pstmt.setString(3,model_name);
		pstmt.setString(4,model_code);
		pstmt.setString(5,standards);
		pstmt.setString(6,maker_name);
		pstmt.setString(7,register_info);
		pstmt.setString(8,w_time);
		pstmt.setString(9,"");
		pstmt.setString(10,"");

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * ��������ǰ�� �������� ����
	 *******************************/
	public void addSupplyInfo(String mid,String model_code,String supplyer_code,String supplyer_name,String supply_cost,String other_info,String login_id) throws Exception{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO item_supply_info(mid,model_code,supplyer_code,supplyer_name,supply_cost,other_info,writer,written_day) VALUES(?,?,?,?,?,?,?,?)";

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String register_info = login_id + "/" + userinfo.getUserName();

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,mid);
		pstmt.setString(2,model_code);
		pstmt.setString(3,supplyer_code);
		pstmt.setString(4,supplyer_name);
		pstmt.setString(5,supply_cost);
		pstmt.setString(6,other_info);
		pstmt.setString(7,register_info);
		pstmt.setString(8,w_time);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * �ܺ�����ǰ������ ����
	 *******************************/
	public void modifyOutItemInfo(String item_name,String unit,String model_code,String model_name,String standards,String maker_name,String login_id) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		//��Ͻð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//����� ����
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);
		String register_info = login_id + "/" + userinfo.getUserName();

		query = "UPDATE out_item_list SET item_name=?,unit=?,model_name=?,standards=?,maker_name=?,modifier=?,modified_day=? WHERE model_code='"+model_code+"'";

		pstmt = con.prepareStatement(query);
		pstmt.setString(1,item_name);
		pstmt.setString(2,unit);
		pstmt.setString(3,model_name);
		pstmt.setString(4,standards);
		pstmt.setString(5,maker_name);
		pstmt.setString(6,register_info);
		pstmt.setString(7,w_time);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************
	 * ��������ǰ������ ����
	 *******************************/
	public void deleteOutItemInfo(String model_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE out_item_list WHERE model_code = '" + model_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*******************************
	 * ��������ǰ�� �������� ����
	 *******************************/
	public void deleteAllOutItemSupplyInfo(String model_code) throws Exception{
		Statement stmt = null;
		String query = "DELETE item_supply_info WHERE model_code = '" + model_code + "'";

		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
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

}		