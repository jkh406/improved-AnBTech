package com.anbtech.gm.business;

import com.anbtech.gm.entity.*;
import com.anbtech.gm.db.*;
import com.anbtech.admin.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class GoodsInfoBO{

	private Connection con;

	public GoodsInfoBO(Connection con){
		this.con = con;
	}
	

	/*****************************************************************
	 * �ű���ǰ ��Ͻ� gcode�� ����Ͽ� �����Ѵ�.
	 * (gcode�� ���õ� ��ǰ�� ������ �ִ� ��ǰ����Ʈ�� �����ϴµ� ���ȴ�.)
	 *****************************************************************/
	public String calculateGcode(String level,String ancestor) throws Exception{
		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);

		String gcode = "";
		String max_gcode = goodsDAO.getMaxGcode(level,ancestor);

		if(max_gcode == null){
			if(level.equals("1")){	// ��ǰ���� ���� ���� 10���� �����Ѵ�.
				gcode = "10";
			}else{					// ��ǰ,�𵨱�,���� ���
				//���� �з��� gcode ���� ������ ��, ����gcode + 01
				String up_gcode = goodsDAO.getGcodeByMid(ancestor);
				gcode = up_gcode + "01";
			}
		}else{
			gcode = Integer.toString(Integer.parseInt(max_gcode)+1);
		}

		return gcode;
	}

	/*************************************
	 * gcode �� �ش��ϴ� ��ǰ�� �ڵ尪�� �����´�.
	 *************************************/
	public String getCodeByGcode(String gcode) throws Exception {

		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT code FROM goods_structure WHERE gcode = '" + gcode + "'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		String code = "";
		while(rs.next()){
			code = rs.getString("code");
		}
		stmt.close();
		rs.close();

		return code;
	}

	/************************************************************************
	 * ��ǰ�� ���ø��׸� �߰����� �����.
	 * one_class:��ǰ���ڵ�, two_class:��ǰ�ڵ�, code:���õ� �׸��ڵ�
	 ************************************************************************/
	public GoodsInfoItemTable getAddItemForm(String mode,String one_class,String two_class,String code) throws Exception{
		GoodsInfoDAO goodsDAO		= new GoodsInfoDAO(con);
		GoodsInfoItemTable table	= new GoodsInfoItemTable();
		
		String one_class_list	= "";		// ��ǰ�� ����Ʈ
		String two_class_list	= "";		// ��ǰ ����Ʈ
		String item_code		= "";		// �׸��ڵ尪
		String item_name		= ""; 		// �׸��
		String item_value		= "";		// �׸�
		String item_unit		= "";		// �׸����
		String write_exam		= "";		// �ۼ���
		String item_desc		= "";		// �׸񼳸�

		//��ǰ�� ����Ʈ ��������
		Statement stmt = null;
		ResultSet rs = null;

		String sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '1' ORDER BY mid ASC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(sql);
//		ResultSet rs = goodsDAO.getOneClassList();
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

		//�߰� ����� ��� �׸��ڵ尪(�ִ밪+1)�� �����´�.
		if(mode.equals("add_template") && two_class != null && !two_class.equals("")) item_code = goodsDAO.getMaxItemCode(two_class);

		//���� ����� ���� ���õ� �׸� ���� ������ �����´�.
		if(mode.equals("upd_template") && item_code != null){
			table = goodsDAO.getItemInfo(code);
			item_code	= table.getItemCode();
			item_name	= table.getItemName();
			item_value	= table.getItemValue();
			item_unit	= table.getItemUnit();
			write_exam	= table.getWriteExam();
			item_desc	= table.getItemDesc();
		}

		table.setOneClass(one_class_list);
		table.setTwoClass(two_class_list);
		table.setItemCode(item_code);
		table.setItemName(item_name);
		table.setItemValue(item_value);
		table.setItemUnit(item_unit);
		table.setWriteExam(write_exam);
		table.setItemDesc(item_desc);

		stmt.close();
		rs.close();

		return table;
	}

	/************************************************************************
	 * ���õ� ��ǰ ������ �����ϴ� ǥ�� ���ø� ����Ʈ�� �����´�.
	 ************************************************************************/
	public ArrayList getItemList(String code) throws Exception{
		GoodsInfoDAO goodsDAO		= new GoodsInfoDAO(con);
		GoodsInfoItemTable table	= new GoodsInfoItemTable();

		//std_template_list ���̺��� ���õ� ��ǰ�� �׸񸮽�Ʈ�� �����´�.
		ArrayList table_list =  new ArrayList();
		if(code != null && !code.equals("")) table_list = (ArrayList)goodsDAO.getItemList(code);

		//�� �����׸� ǥ�ø� ���� html �ڵ带 �����Ѵ�.
		String html_value = "";
		Iterator table_iter = table_list.iterator();
		ArrayList html_list = new ArrayList();
		while(table_iter.hasNext()){
			table = (GoodsInfoItemTable)table_iter.next();
			
			//�׸񰪿� ���� html ����
			//item_value == "" �� ��쿡�� �ؽ�Ʈ �ڽ��� 
			//item_value �� �׸��� ������ ��� �޺��ڽ��� �� �׸��� �־��ش�.
			String item_value = table.getItemValue();
			if(item_value.equals("")){
				html_value = "<input type=text name=v_"+code+">";
			}else{
				int i = 0; 
				StringTokenizer str = new StringTokenizer(item_value, ",");
				String item[] = new String[str.countTokens()]; 
				html_value = "<select name=v_"+code+" style=\"width:150\">";
				while(str.hasMoreTokens()){ 
					item[i] = str.nextToken();
					html_value += "<option value='"+item[i]+"'>"+item[i]+"</option>";
					++i; 
				}
				html_value += "</select>";
			}
			table.setItemValue(html_value);

			//�׸������ ���� html ����
			//item_unit == "" �� ��쿡�� �ƹ��͵� ǥ������ �ʰ�
			//item_unit �� �׸��� ������ ��� �޺��ڽ��� �� �׸��� �־��ش�.
			String item_unit = table.getItemUnit();
			if(item_unit.equals("")){
				html_value = "";
			}else{
				int i = 0; 
				StringTokenizer str = new StringTokenizer(item_unit, ",");

				String item[] = new String[str.countTokens()]; 
				html_value = "<select name=u_"+code+" style=\"width:50\">";
				while(str.hasMoreTokens()){ 
					item[i] = str.nextToken();
					html_value += "<option value='"+item[i]+"'>"+item[i]+"</option>";
					++i; 
				}
				html_value += "</select>";
			}
			table.setItemUnit(html_value);

			String item_code = table.getItemCode();
			String link_mod = "javascript:viewCode('" + item_code + "');";
			String link_del = "";
			table.setLinkMod(link_mod);
			table.setLinkDel(link_del);

			html_list.add(table);
		}

		return html_list;
	}

	/************************************************************************
	 * ������ ��� �Ǵ� ���� ���� �����.
	 ************************************************************************/
	public GoodsInfoTable getAddGoodsForm(String mode,String one_class,String two_class,String three_class,String mid) throws Exception{
		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);
		GoodsInfoTable table = new GoodsInfoTable();
		com.anbtech.admin.db.SysConfigDAO sysDAO = new com.anbtech.admin.db.SysConfigDAO(con);
		
		String one_class_list	= "";	//��ǰ�� ����Ʈ
		String two_class_list	= "";	//��ǰ ����Ʈ
		String three_class_list	= "";	//�𵨱� ����Ʈ

		String goods_code		= "";	//���ڵ�
		String goods_name		= "";	//�𵨸�
		String goods_name2		= "";	//�𵨸�2
		String short_name		= "";	//�𵨾��
		String color_code		= "";	//�𵨻����ڵ�
		String color_name		= "";	//�𵨻���

		//two_class (-> gcode)�� �ش��ϴ� ��ǰ�� �ڵ带 �����´�.
		//��ǰ�ڵ�� ���ڵ� ������ �ʿ���.
		if(mode.equals("add_model") && two_class != null){
			String two_class_code = getCodeByGcode(two_class);
			table.setTwoClassCode(two_class_code);
		}

		//goods_structure ���̺��� mid�� ��ġ�ϴ� ���ڵ� ������ �����´�.
		if(mode.equals("mod_model") && mid != null){
			table = goodsDAO.getGoodsInfoByMid(mid);
						
			goods_code	= table.getGoodsCode();
			goods_name	= table.getGoodsName();
			goods_name2	= table.getGoodsName2();
			short_name	= table.getShortName();
			color_code	= table.getColorCode();	
			color_name  = sysDAO.getMinorCodeName("COLOR",color_code);
		}

		//��ǰ�� ����Ʈ ��������
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String sql = "SELECT gcode,name FROM goods_structure WHERE glevel = '1' ORDER BY mid ASC";
		rs = stmt.executeQuery(sql);
//		ResultSet rs = goodsDAO.getOneClassList();
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

		//���õ� ��ǰ ������ �𵨱� ����Ʈ ��������
		if(two_class != null && !two_class.equals("")){
			sql = "SELECT mid,gcode,name FROM goods_structure WHERE glevel = '3' and gcode LIKE '" + two_class +"%' ORDER BY mid ASC";
			rs = stmt.executeQuery(sql);
//			rs = goodsDAO.getThreeClassList(two_class);
			while(rs.next()){
				three_class_list += "<option value='" + rs.getString("mid") + "'";
				if(three_class.equals(rs.getString("gcode"))) three_class_list += " selected";
				three_class_list += ">" + rs.getString("name") + "</option>";
			}
		}

		table.setOneClass(one_class_list);
		table.setTwoClass(two_class_list);
		table.setThreeClass(three_class_list);
		table.setGoodsCode(goods_code);
		table.setGoodsName(goods_name);
		table.setGoodsName2(goods_name2);
		table.setShortName(short_name);
		table.setColorCode(color_code);
		table.setColorName(color_name);
		
		stmt.close();
		rs.close();

		return table;
	}

	/************************************************************************
	 * ���Ļ� ������� �����.
	 * why : �Ļ�����(r:����ڵ� ����,d:�Ļ��ڵ� ����)
	 ************************************************************************/
	public GoodsInfoTable getRevModelForm(String mode,String mid,String why) throws Exception{
		GoodsInfoDAO goodsDAO = new GoodsInfoDAO(con);
		GoodsInfoTable table = new GoodsInfoTable();
		
		String one_class		= "";	//��ǰ���ڵ�
		String two_class		= "";	//��ǰ�ڵ�
		String three_class		= "";	//�𵨱��ڵ�
		String gcode			= "";	//�����ڵ�

		String goods_code		= "";	//���ڵ�
		String goods_name		= "";	//�𵨸�
		String goods_name2		= "";	//�𵨸�2
		String short_name		= "";	//�𵨾��
		String color_code		= "";	//�𵨻����ڵ�
		String color_name		= "";	//�𵨻���

		String two_class_code	= "";	//��ǰ�ڵ�
		String revision_code	= "";	//��ɱ����ڵ�
		String derive_code		= "";	//�Ļ��ڵ�

		//goods_structure ���̺��� mid�� ��ġ�ϴ� ���ڵ� ������ �����´�.
		if(mid != null){
			table = goodsDAO.getGoodsInfoByMid(mid);
			
			goods_code		= table.getGoodsCode();
			goods_name		= table.getGoodsName();
			goods_name2		= table.getGoodsName2();
			short_name		= table.getShortName();
			color_code		= table.getColorCode();

			gcode			= table.getGcode();
			one_class		= gcode.substring(0,2);
			two_class		= gcode.substring(0,4);
			three_class		= table.getAncestor();

			//���ڵ�����ǰ� ����� �Ʒ� 3������ ����Ǿ�� ��.
			two_class_code	= goods_code.substring(0,2);
			revision_code	= goods_code.substring(4,5);
			derive_code		= goods_code.substring(5,7);
		}

		//�Ļ������ڵ� why == 'r' �϶��� ��ɱ����ڵ尡 ����Ǿ�� �ϹǷ�
		//��ǰ�ڵ�(2)�� �𵨾��(2)�� ���� ���� �ִ� ��ɱ����ڵ� + 1 ��,
		//�Ļ������ڵ� why == 'd' �϶��� �Ļ��ڵ尡 ����Ǿ�� �ϹǷ�,
		//��ǰ�ڵ�(2)�� �𵨾��(2)�� ��ɱ����ڵ尡 ��ġ�ϴ� ���� �ִ� �Ļ��ڵ� + 1��
		//�����ͼ� ��ɱ����ڵ� �� �Ļ��ڵ带 �����.
		if(why.equals("d")){
			String searchword = two_class_code + short_name + revision_code;
			derive_code = goodsDAO.getMaxDeriveCode(searchword);
		}else if(why.equals("r")){
			String searchword = two_class_code + short_name;
			revision_code = goodsDAO.getMaxRevisionCode(searchword);		
		}

		table.setOneClass(one_class);
		table.setTwoClass(two_class);
		table.setThreeClass(three_class);

		table.setGoodsCode(goods_code);
		table.setGoodsName(goods_name);
		table.setGoodsName2(goods_name2);
		table.setShortName(short_name);
		table.setColorCode(color_code);
		table.setColorName(color_name);

		table.setTwoClassCode(two_class_code);
		table.setRevisionCode(revision_code);
		table.setDeriveCode(derive_code);

		return table;
	}

	/************************************************************************
	 * ���õ� ��ǰ�� ���ǵ� ���� ����Ʈ�� �����´�.
	 * ���� �ÿ��� ���� �� �� ������ ���� �����Ͽ� �ش�.
	 ************************************************************************/
	public ArrayList getSpecList(String mode,String two_class,String mid) throws Exception{
		GoodsInfoDAO goodsDAO		= new GoodsInfoDAO(con);
		GoodsInfoItemTable table	= new GoodsInfoItemTable();
		ArrayList table_list		= new ArrayList();

		if(mode.equals("add_model")){ // �űԵ�ϸ��
			//���õ� ��ǰ�� ���ǵ� ���� ����Ʈ�� �����´�.
			table_list = goodsDAO.getItemList(two_class);
			Iterator table_iter = table_list.iterator();

			while(table_iter.hasNext()){
				table = (GoodsInfoItemTable)table_iter.next();

				String item_code	= table.getItemCode();
				String item_name	= table.getItemName();
				String item_value	= table.getItemValue();
				String item_unit	= table.getItemUnit();
				String write_exam	= table.getWriteExam();
				String item_desc	= table.getItemDesc();

				String item_name_s = "<a onmouseover=\"ANB_layerAction(z" + item_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + item_code + ", 'hidden')\">" + item_name + "</a>";
				item_name_s += "<div id=z" + item_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + item_desc + "</div>";
				table.setItemName(item_name_s);

				String item_value_s = "";
				if(item_value.equals("")){
					item_value_s = "<input type=text name=v_"+item_code+">";
				}else{
					int k = 0; 
					StringTokenizer str2 = new StringTokenizer(item_value, ",");
					String item[] = new String[str2.countTokens()]; 
					item_value_s = "<select name=v_"+item_code+">";
					item_value_s += "<option value=''>NA</option>";
					while(str2.hasMoreTokens()){ 
						item[k] = str2.nextToken();
						item_value_s += "<option value='"+item[k]+"'>"+item[k]+"</option>";
						++k; 
					}
					item_value_s += "</select>";
				}
				table.setItemValue(item_value_s);

				String item_unit_s = "";
				if(item_unit.equals("")){
					item_unit_s = "<input type='hidden' name='u_"+item_code+"' value='na'>";
				}else{
					int k = 0; 
					StringTokenizer str2 = new StringTokenizer(item_unit, ",");

					String item[] = new String[str2.countTokens()]; 
					item_unit_s = "<select name=u_"+item_code+">";
					while(str2.hasMoreTokens()){ 
						item[k] = str2.nextToken();
						item_unit_s += "<option value='"+item[k]+"'>"+item[k]+"</option>";
						++k; 
					}
					item_unit_s += "</select>";
				}

				table.setItemUnit(item_unit_s);		
			}
		}
		else if((mode.equals("mod_model") || mode.equals("rev_model")) && mid != null){	// ���� or �Ļ� ����� ���
			Statement stmt = null;
			ResultSet rs = null;

			//���õ� ��ǰ�� ���ǵ� ���� ����Ʈ�� �����´�.
			table_list = goodsDAO.getItemList(two_class);
			Iterator table_iter = table_list.iterator();

			String spec_list = goodsDAO.getSpecList(mid);	//31001,31002,31003....
			StringTokenizer str = new StringTokenizer(spec_list, ",");
			int spec_count = str.countTokens();				//�Ѿ�� �����׸��� ������ �ľ�

			//���õ� ���� �Ӽ����� �����´�.
			String sql = "SELECT prop1 ";
			for(int i=2; i<=spec_count; i++){
				sql+= ",prop" + i;
			}
			sql += " FROM goods_structure WHERE mid = '" + mid + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
//			ResultSet rs = goodsDAO.getModelProperty(mid,spec_count);
			rs.next();
			int i = 1;
			while(table_iter.hasNext()){
				table = (GoodsInfoItemTable)table_iter.next();

				String item_code			= table.getItemCode();
				String item_name			= table.getItemName();
				String item_value			= table.getItemValue();
				String item_unit			= table.getItemUnit();
				String write_exam			= table.getWriteExam();
				String item_desc			= table.getItemDesc();

				if(i<=spec_count){
					String item_value_db	= "";
					String item_unit_db		= "";
					if(rs.getString(i).indexOf("|") == 0){
						item_unit_db		= rs.getString(i).substring(2,rs.getString(i).length());
					}else{
						ArrayList node_list	= com.anbtech.util.Token.getTokenList(rs.getString(i)); 
						item_value_db	= (String)node_list.get(0); 
						item_unit_db	= (String)node_list.get(1);
					}

					String item_name_s = "<a onmouseover=\"ANB_layerAction(z" + item_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + item_code + ", 'hidden')\">" + item_name + "</a>";
					item_name_s += "<div id=z" + item_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + item_desc + "</div>";
					table.setItemName(item_name_s);

					String item_value_s = "";
					if(item_value.equals("")){
						item_value_s = "<input type=text name=v_"+item_code+" value='"+item_value_db+"'>";
					}else{
						int k = 0; 
						StringTokenizer str2 = new StringTokenizer(item_value, ",");
						String item[] = new String[str2.countTokens()]; 
						item_value_s = "<select name=v_"+item_code+">";
						item_value_s += "<option value=''>NA</option>";
						while(str2.hasMoreTokens()){ 
							item[k] = str2.nextToken();
							item_value_s += "<option value='"+item[k]+"'";
							if(item[k].equals(item_value_db)) item_value_s += " selected";
							item_value_s += ">" + item[k] + "</option>";
							++k; 
						}
						item_value_s += "</select>";
					}
					table.setItemValue(item_value_s);

					String item_unit_s = "";
					if(item_unit.equals("")){
						item_unit_s = "<input type='hidden' name='u_"+item_code+"' value='na'>";
					}else{
						int k = 0; 
						StringTokenizer str2 = new StringTokenizer(item_unit, ",");

						String item[] = new String[str2.countTokens()]; 
						item_unit_s = "<select name=u_"+item_code+">";
						while(str2.hasMoreTokens()){ 
							item[k] = str2.nextToken();
							item_unit_s += "<option value='"+item[k]+"' ";
							if(item[k].equals(item_unit_db)) item_unit_s += " selected";
							item_unit_s += ">" + item[k] + "</option>";
							++k; 
						}
						item_unit_s += "</select>";
					}

					table.setItemUnit(item_unit_s);
				}else{
					String item_name_s = "<a onmouseover=\"ANB_layerAction(z" + item_code + ", 'visible')\" onmouseout=\"ANB_layerAction(z" + item_code + ", 'hidden')\">" + item_name + "</a>";
					item_name_s += "<div id=z" + item_code + " style=\"position:absolute;background-color:#FEFEED;width:200; height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '����ü';border:#C0C0C0 1px solid;	visibility:hidden;\">" + item_desc + "</div>";
					table.setItemName(item_name_s);

					String item_value_s = "";
					if(item_value.equals("")){
						item_value_s = "<input type=text name=v_"+item_code+">";
					}else{
						int k = 0; 
						StringTokenizer str2 = new StringTokenizer(item_value, ",");
						String item[] = new String[str2.countTokens()]; 
						item_value_s = "<select name=v_"+item_code+">";
						while(str2.hasMoreTokens()){ 
							item[k] = str2.nextToken();
							item_value_s += "<option value='"+item[k]+"'>"+item[k]+"</option>";
							++k; 
						}
						item_value_s += "</select>";
					}
					table.setItemValue(item_value_s);

					String item_unit_s = "";
					if(item_unit.equals("")){
						item_unit_s = "<input type='hidden' name='u_"+item_code+"' value='na'>";
					}else{
						int k = 0; 
						StringTokenizer str2 = new StringTokenizer(item_unit, ",");

						String item[] = new String[str2.countTokens()]; 
						item_unit_s = "<select name=u_"+item_code+">";
						while(str2.hasMoreTokens()){ 
							item[k] = str2.nextToken();
							item_unit_s += "<option value='"+item[k]+"'>"+item[k]+"</option>";
							++k; 
						}
						item_unit_s += "</select>";
					}

					table.setItemUnit(item_unit_s);						
				}
				i++;
			}

			stmt.close();
			rs.close();

		}
		else if(mode.equals("view_model") && mid != null){	//���� ����� ���
			Statement stmt = null;
			ResultSet rs = null;

			String spec_list = goodsDAO.getSpecList(mid);	//31001,31002,31003....
			StringTokenizer str = new StringTokenizer(spec_list, ",");
			int spec_count = str.countTokens();				//�Ѿ�� �����׸��� ������ �ľ�

			//���õ� ���� �Ӽ����� �����´�.
			String sql = "SELECT prop1 ";
			for(int i=2; i<=spec_count; i++){
				sql+= ",prop" + i;
			}
			sql += " FROM goods_structure WHERE mid = '" + mid + "'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			//ResultSet rs = goodsDAO.getModelProperty(mid,spec_count);
			for(int i=0; i<spec_count; i++){ 
				String item_code = str.nextToken();
				table = new GoodsInfoItemTable();
				table = goodsDAO.getItemInfo(item_code);

				String item_value	= "";
				String item_unit	= "";

				//���� �ִ� ��츸 �׸��� ����Ѵ�.
				if(rs.getString(i+1).indexOf("|") != 0){
					ArrayList node_list	= com.anbtech.util.Token.getTokenList(rs.getString(i+1)); 
					item_value	= (String)node_list.get(0); 
					item_unit	= (String)node_list.get(1);

					if(item_unit.equals("na")) item_unit = "";

					table.setItemValue(item_value);
					table.setItemUnit(item_unit);

					table_list.add(table);
				}
			}
			stmt.close();
			rs.close();
		}

		return table_list;
	}


	/*****************************************************************
	 * �������� �˻� ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		String where = "", where_and = "", where_sea = "", where_cat = "";

		if (searchword.length() > 0){
			if (searchscope.equals("name")){
				where_sea += "( name like '%" + searchword + "%' )";
			}
			else if (searchscope.equals("name2")){
				where_sea += "( name2 like '%" + searchword + "%' )";
			}
			else if (searchscope.equals("code")){
				where_sea += "( code like '%" + searchword + "%' )";
			}
		}
		if(searchword.length() > 0) where_and = " and ";
		where = " WHERE glevel = '4'"  + where_and + where_sea;

		return where;
	}

	/*****************************************************************
	 * �������� �˻� ������ �����.
	 *****************************************************************/
	public String getWhereByScope(String mode, String searchword, String searchscope, String category) throws Exception{

		String where = "", where_and = "", where_sea = "", where_cat = "";

		if (searchword.length() > 0){
			if (searchscope.equals("item_no"))
			{
				where = " WHERE code_type ='F' and (item_no like '%"+searchword+"%')";
								
			} else if (searchscope.equals("pd_name"))
			{
				where = " WHERE glevel='2' AND name like '%"+searchword+"%'";
			} else if (searchscope.equals("name"))
			{
				where = " WHERE glevel='4' AND name like '%"+searchword+"%'";
			} else if (searchscope.equals("code"))
			{
				where = " WHERE glevel='4' AND code like '%"+searchword+"%'";
			} 
		}
		
		return where;
	}

	// form field Name �迭�� ���
	public String[] getFieldArry(String one_class,String one_name,String two_class,String two_name,String three_class,String three_name,String four_class,String four_name,String fg_code) throws Exception{
		String[] fieldArry = new String[9];
		fieldArry[0] = one_class==null?"#":one_class;
		fieldArry[1] = one_name==null?"#":one_name;
		fieldArry[2] = two_class==null?"#":two_class;
		fieldArry[3] = two_name==null?"#":two_name;
		fieldArry[4] = three_class==null?"#":three_class;
		fieldArry[5] = three_name==null?"#":three_name;
		fieldArry[6] = four_class==null?"#":four_class;
		fieldArry[7] = four_name==null? "#":four_name;
		fieldArry[8] = fg_code==null?"#":fg_code;

		return fieldArry;
	}

}