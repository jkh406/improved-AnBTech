package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomChangeDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();//����
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();//����
	private String query = "";
	private ArrayList item_list = null;				//PART������ ArrayList�� ���
	private mbomStrTable mst = null;				//help class
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public CbomChangeDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		���躯�� ���� �б�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ���躯�濡 �ش�Ǵ� BOM���� ���� ����ϱ�
	//*******************************************************************/	
	public ArrayList readEccBomList(String eco_no,String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	

			String pcode = rs.getString("parent_code");
			String app_status = rs.getString("app_status");
			if(app_status.equals("0"))		//��������
					pcode = "<a href=\"javascript:strView('"+rs.getString("pid")+"','"+rs.getString("gid")+"');\">"+rs.getString("parent_code")+"</a>";
			table.setParentCode(pcode);	

			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ����� ������ �ٽ� �о� ����[����/����]�ϰ��� �Ҷ�
	//		
	//*******************************************************************/	
	public ArrayList readEccBomItem(String pid,String gid,String eco_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();

		//�ش�Ǵ� chg_order�� ���� ¦���� �о��.[RB,RA / A / D]
		String where = "where gid='"+gid+"' and pid='"+pid+"' and eco_no='"+eco_no+"'";
		String chg_order = getColumData("ecc_bom","chg_order",where);
		
		query = "SELECT * FROM ecc_bom where chg_order ='"+chg_order+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ���躯�濡 �ش�Ǵ� BOM���� ���� ����ϱ� : ��ǰ���� ����
	//*******************************************************************/	
	public ArrayList getEccBomList(String eco_no,String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by parent_code,child_code desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		���躯�� ��¹� �����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ��ǥ����𵨱����� �����ǰ ��� : BOM���� ������ ����� ����
	//*******************************************************************/	
	public ArrayList outEccBomList(String eco_no,String gid) throws Exception
	{
		//���� �ʱ�ȭ
		String op_code="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			//�űԹ� ���� ��ǰ�� ��� ������� �ϳ��� ����Ѵ�.
			String tag = rs.getString("adtag");
			if(tag.equals("A") || tag.equals("D")) {
				//ù������ ��������
				table = new eccBomTable();
				table.setPid("");	
				table.setGid("");	
				table.setParentCode("");	
				table.setChildCode("");	
				table.setLevelNo("");	
				table.setPartName("");	
				table.setPartSpec("");	
				table.setLocation("");	
				table.setOpCode("");	
				table.setQtyUnit("");	
				table.setQty("");	
				table.setMakerName("");	
				table.setMakerCode("");	
				table.setPriceUnit("");	
				table.setPrice("");
				table.setAddDate("");	
				table.setBuyType("");	
				table.setEcoNo("");	
				table.setAdTag("");	
				table.setChangeDate("");	
				table.setBomStartDate("");	
				table.setBomEndDate("");
				table.setEccReason("");
				table.setNote("");
				table.setChgOrder("");
				table.setAppStatus("");
				table_list.add(table);

				//�������ο� �����͸�
				table = new eccBomTable();
				table.setPid(rs.getString("pid"));	
				table.setGid(rs.getString("gid"));	
				table.setParentCode(rs.getString("parent_code"));	
				table.setChildCode(rs.getString("child_code"));	
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartName(rs.getString("part_name"));	
				table.setPartSpec(rs.getString("part_spec"));	
				table.setLocation(rs.getString("location"));
				op_code = rs.getString("op_code"); if(op_code.length() == 0) op_code="&nbsp;";
				table.setOpCode(op_code);	
				table.setQtyUnit(rs.getString("qty_unit"));	
				table.setQty(rs.getString("qty"));	
				table.setMakerName(rs.getString("maker_name"));	
				table.setMakerCode(rs.getString("maker_code"));	
				table.setPriceUnit(rs.getString("price_unit"));	
				table.setPrice(rs.getString("price"));
				table.setAddDate(rs.getString("add_date"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setEcoNo(rs.getString("eco_no"));	
				table.setAdTag(rs.getString("adtag"));	
				table.setChangeDate(rs.getString("change_date"));	
				table.setBomStartDate(rs.getString("bom_start_date"));	
				table.setBomEndDate(rs.getString("bom_end_date"));
				table.setEccReason(rs.getString("ecc_reason"));
				table.setNote(rs.getString("note"));
				table.setChgOrder(rs.getString("chg_order"));
				table.setAppStatus(rs.getString("app_status"));
				table_list.add(table);
			} else {
				table = new eccBomTable();
				table.setPid(rs.getString("pid"));	
				table.setGid(rs.getString("gid"));	
				table.setParentCode(rs.getString("parent_code"));	
				table.setChildCode(rs.getString("child_code"));	
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartName(rs.getString("part_name"));	
				table.setPartSpec(rs.getString("part_spec"));	
				table.setLocation(rs.getString("location"));	
				op_code = rs.getString("op_code"); if(op_code.length() == 0) op_code="&nbsp;";
				table.setOpCode(op_code);		
				table.setQtyUnit(rs.getString("qty_unit"));	
				table.setQty(rs.getString("qty"));	
				table.setMakerName(rs.getString("maker_name"));	
				table.setMakerCode(rs.getString("maker_code"));	
				table.setPriceUnit(rs.getString("price_unit"));	
				table.setPrice(rs.getString("price"));
				table.setAddDate(rs.getString("add_date"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setEcoNo(rs.getString("eco_no"));	
				table.setAdTag(rs.getString("adtag"));	
				table.setChangeDate(rs.getString("change_date"));	
				table.setBomStartDate(rs.getString("bom_start_date"));	
				table.setBomEndDate(rs.getString("bom_end_date"));
				table.setEccReason(rs.getString("ecc_reason"));
				table.setNote(rs.getString("note"));
				table.setChgOrder(rs.getString("chg_order"));
				table.setAppStatus(rs.getString("app_status"));
				table_list.add(table);
			}
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ��ǥ����𵨱����� �����ǰ ��� : BOM���� ������ ����� ����
	//*******************************************************************/	
	public ArrayList outEccBomParentList(String eco_no,String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by parent_code,child_code desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			String tag = rs.getString("adtag");
			if(tag.equals("A") || tag.equals("D") || tag.equals("RB")) {
				table = new eccBomTable();
				table.setParentCode(rs.getString("parent_code"));	
				table.setPartSpec(getComponentSpec(rs.getString("parent_code")));	
				table_list.add(table);
			}
		} 

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MBOM_MASTER���� �б�
	//*******************************************************************/	
	public mbomMasterTable readMasterItem(String fg_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = new com.anbtech.bm.entity.mbomMasterTable();
		
		query = "SELECT * FROM mbom_master where fg_code ='"+fg_code+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setPdCode(rs.getString("pd_code"));	
			table.setPdName(rs.getString("pd_name"));	
			table.setPjtCode(rs.getString("pjt_code"));	
			table.setPjtName(rs.getString("pjt_name"));	
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setRegDate(rs.getString("reg_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppName(rs.getString("app_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setBomStatus(rs.getString("bom_status"));	
			table.setAppNo(rs.getString("app_no"));	
			table.setMStatus(rs.getString("m_status"));	
			table.setPurpose(rs.getString("purpose"));	
		} else {
			table.setPid("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setPdCode("");	
			table.setPdName("");	
			table.setPjtCode("");	
			table.setPjtName("");	
			table.setRegId("");	
			table.setRegName("");	
			table.setRegDate("");	
			table.setAppId("");	
			table.setAppName("");	
			table.setAppDate("");	
			table.setBomStatus("");	
			table.setAppNo("");	
			table.setMStatus("");	
			table.setPurpose("0");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MBOM_MASTER���� �б�
	//*******************************************************************/	
	public mbomStrTable readStrItem(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
		
		query = "SELECT * FROM mbom_str where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setAppStatus(rs.getString("app_status"));
			table.setTag(rs.getString("tag"));
			table.setAssyDup(rs.getString("assy_dup"));
			String part_type = getPartType(rs.getString("gid"),rs.getString("pid"),rs.getString("child_code"),rs.getString("level_no"));
			table.setPartType(part_type);
			table.setItemType(rs.getString("item_type"));
		} else {
			table.setPid("");	
			table.setGid("");	
			table.setParentCode("");	
			table.setChildCode("");	
			table.setLevelNo("");	
			table.setPartName("");	
			table.setPartSpec("");	
			table.setLocation("");	
			table.setOpCode("");	
			table.setQtyUnit("");	
			table.setQty("");	
			table.setMakerName("");	
			table.setMakerCode("");	
			table.setPriceUnit("");	
			table.setPrice("");
			table.setAddDate("");	
			table.setBuyType("");	
			table.setEcoNo("");	
			table.setAdTag("");	
			table.setBomStartDate("0");	
			table.setBomEndDate("0");
			table.setAppStatus("0");
			table.setTag("0");
			table.setAssyDup("");
			table.setPartType("P");		//P:��ǰ, A:Assy
			table.setItemType("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//--------------------------------------------------------------------
	//
	//		��ǰ �����Ϳ��� �ʿ��� ���� �����ϱ�
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	//	ǰ���ڵ忡 �ش�Ǵ� ���������ϱ�
	// [��ǰ�̸�,�԰�,����Ŀ��,����,���ݴ���,����] 
	// ���� : �԰ݸ� ���
	//*******************************************************************/
	public String[] getComponentInfo(String part_code) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[6];
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		for(int i=0; i<6; i++) data[i] = "";		//�迭�ʱ�ȭ
		query = "SELECT * FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "������";
			data[1] = str.repWord(rs.getString("item_desc"),"'","`");
			data[2] = rs.getString("mfg_no");		if(data[2] == null) data[2] = "";
			data[3] = "";
			data[4] = rs.getString("stock_unit");	if(data[4] == null) data[4] = "EA";
			data[5] = rs.getString("item_type");	if(data[5] == null) data[5] = "4";
		}
		
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�ش� ǰ���ڵ��� SPEC���� ���ϱ�
	//*******************************************************************/
	public String getComponentSpec(String part_code) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT item_desc FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("item_desc");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	ǰ�� �ڵ� �˻��ϱ�
	//*******************************************************************/
	public ArrayList getComponentCode(String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//ǰ���ڵ� ã��
		query = "SELECT item_no,item_desc FROM item_master WHERE item_no like '"+sWord+"%' order by item_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setParentCode(rs.getString("item_no"));
				table.setPartSpec(rs.getString("item_desc"));
				table_list.add(table);
		}

		stmt.close();
		rs.close();
		return table_list;			
	}

	
	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	MBOM_STR���� �ش��ǰ�� PART���� ASSY�ڵ����� �Ǵ��ϱ� : �Ϻα��� �ֳ� ����
	//  ������ ������ ���� �������� ��� [�Ϻα����� ������ ���� �������]
	//*******************************************************************/
	public String getPartType(String gid,String pid,String child_code,String level_no) throws Exception
	{
		//���� �ʱ�ȭ
		String part_type = "A";		//Assy�ڵ���
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select count(*) from mbom_str where gid ='"+gid+"' and parent_code='"+child_code+"'";
		query += " and level_no > '"+level_no+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			int cnt = rs.getInt(1);
			if(cnt == 0) part_type = "P";	//��ǰ��
		}

		//�ߺ�Assy ǥ�� [assy_dup='D']�� ���� ���� : ��ǰ���� �ν���� �Ͽ� ������ �� �ֵ��� ��.
		query = "select assy_dup from mbom_str where gid='"+gid+"' and pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			String assy_dup = rs.getString("assy_dup");
			if(assy_dup.equals("D")) part_type="P";
		}
	
		stmt.close();
		rs.close();
		return part_type;			
	}
	//*******************************************************************
	//	MBOM_STR���� LEVEL_NO���ϱ�
	//*******************************************************************/
	public int getLevelNo(String query) throws Exception
	{
		//���� �ʱ�ȭ
		int level_no = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			level_no = Integer.parseInt(rs.getString("level_no"));
			level_no++;		//�ش緹������ +1���Ͽ� �����ش�
		}
		
		stmt.close();
		rs.close();
		return level_no;			
	}

	//*******************************************************************
	//	���� �ľ��ϱ� 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	
	//*******************************************************************
	// SQL update �����ϱ�
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String[] getColumArrayData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = null;
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		//���� �ľ��ϱ�
		query = "select count(*) from "+tablename+" "+where;
		int cnt = getTotalCount(query);
		if(cnt == 0) return data;
		data = new String[cnt];
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			data[n] = rs.getString(getcolumn);
			n++;
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	ECC_BOM���� ��¼���[chg_order]���ϱ�
	//*******************************************************************/
	public String getChgOrder(String query) throws Exception
	{
		//���� �ʱ�ȭ
		nfm.setFormat("0000");
		String chg_order = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) chg_order = rs.getString("chg_order");
		
		//ã�� �������� +1
		if(chg_order.length() == 0) {
			chg_order = nfm.toDigits(1);
		} else {
			chg_order = nfm.toDigits(Integer.parseInt(chg_order)+1);
		}
		
		stmt.close();
		rs.close();
		return chg_order;			
	}
}


