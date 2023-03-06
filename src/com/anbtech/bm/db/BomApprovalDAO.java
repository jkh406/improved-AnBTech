package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import com.anbtech.dcm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomApprovalDAO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.business.BmToMmBO btmBO = null;				//MBOM_STR -> MBOM_ITEM I/F
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query="", update="";
	private ArrayList item_list = null;				//PART������ ArrayList�� ���
	private mbomStrTable mst = null;				//help class
	private String assy_head = "[1,F]";				//Assy�ڵ常 ������ ��� 1:Assy���ι���, F:FG�ڵ� ���ι���
	private String phantom_head = "1PH";			//Phantom Assy
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomApprovalDAO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		btmBO = new com.anbtech.bm.business.BmToMmBO(con);
	}
	//--------------------------------------------------------------------
	//
	//		���� ���� �ݿ��� ���õ� �޼ҵ�
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// �ʱ�BOM���� <BOM������� �����ϱ� : MBOM MASTER�� ���ؼ�>
	//*******************************************************************/	
	public void setBomStatus(String pid,String bom_status,String app_id,String app_name,String app_no) throws Exception
	{
		//MBOM_MASTER�� BOM ���� �����ϱ�
		update = "UPDATE MBOM_MASTER set bom_status='"+bom_status+"',app_id='"+app_id;
		update += "',app_name='"+app_name+"',app_date='"+anbdt.getDateNoformat();
		update += "', app_no='"+app_no+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//MBOM_STR�� ������� �����ϱ�
		if(bom_status.equals("5")) {		//BOM����
			update = "UPDATE MBOM_STR set app_status='1' where gid='"+pid+"'";
			modDAO.executeUpdate(update);

			//MBOM_STR --> MBOM_ITEM I/Fó���ϱ� (BOM���� ������ I/Fó��)
			btmBO.tranceBOMList(pid);
		} else {							//�׿�
			update = "UPDATE MBOM_STR set app_status='0' where gid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

	}

	//*******************************************************************
	//	MBOM_STR���� ����1 ���� LIST���ϱ�
	//  ��Ž� ����1 ������ ��ǰ�� �����Ǿ����� Ȯ���ϱ�����
	//*******************************************************************/
	public ArrayList getLevelOneAssy(String gid) throws Exception
	{
		
		//���� �ʱ�ȭ
		String parent_code = "";
		item_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where gid = '"+gid+"' and level_no = '1' ";
		query += "and child_code like '"+assy_head+"%' order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));
			
			parent_code = rs.getString("parent_code");
			mst.setParentCode(parent_code);	

			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));

			//phantom Assy����
			if(parent_code.indexOf(phantom_head) != -1) item_list.add(mst);
		}
		rs.close();
		stmt.close(); 

		return item_list;
	}

	//--------------------------------------------------------------------
	//
	//		���躯�� ������� �ݿ�����
	//			
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// ���躯�� �����Ž� : ���躯���� BOM������ �����ϱ� ���� 
	//*******************************************************************/	
	public void setEccBomStatus(String eco_no,String app_status) throws Exception
	{
		update = "UPDATE ECC_BOM set app_status='"+app_status+"' where eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}

	//*******************************************************************
	// ���躯�� ������º���[������,����,�ݷ���] : ECC COM ������� �����ϱ� 
	//		commend : ����[app], ����[rej]	
	//*******************************************************************/	
	public void setEccStatus(String pid,String app_no,String commend) throws Exception
	{
		String fix_date = anbdt.getDateNoformat();

		//������ ������¸� �˾ƺ���
		String where = "where pid='"+pid+"'";
		String ecc_status = modDAO.getColumData("ecc_com","ecc_status",where);
		String eco_no = modDAO.getColumData("ecc_com","eco_no",where);

		//------------------------------------------
		// ���� ��Ž�
		//------------------------------------------
		if(ecc_status.equals("1")) {					//ECR ���� ��Ž�
			update = "UPDATE ECC_COM set ecc_status='2' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		} else if(ecc_status.equals("6")) {				//ECO ���� ��Ž�
			update = "UPDATE ECC_COM set ecc_status='7' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		}
		//------------------------------------------
		//���� ����, �ݷ���
		//------------------------------------------
		//ECC_COM�� ������� �����ϱ�
		else if(ecc_status.equals("2")) {				//ECR���ο�û����
			//���ν�
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='3' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_REQ set app_no='"+app_no+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			} 
			//�ݷ���
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='0' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			}
		} else if(ecc_status.equals("7")) {			//ECO���ο�û����
			//���ν�
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='8',fix_date='"+fix_date+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_ORD set app_no='"+app_no+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			} 
			//�ݷ���
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='5' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_BOM set app_status='0' where eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			}
		}
		//------------------------------------------
		//ECO AUDIT ����, �ݷ���
		//------------------------------------------
		else if(ecc_status.equals("8")) {			//ECO���λ���
			//���ν�
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='9',fix_date='"+fix_date+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/Fó���ϱ� (BOM���� ������ I/Fó��)
				btmBO.tranceChangeBOMList(eco_no);
			} 
			//�ݷ���
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='5',fix_date='' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_BOM set app_status='0' where eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			}
		}
	}
	//*******************************************************************
	// ���躯�� BOM����[��������� Ȯ����] : MBOM STR�� �ش� adtag�� ���� �ݿ��ϱ� 
	//	eco_no : ���躯���ȣ,  order_date : Ȯ������
	//			1.adtag = 'D','RB' �϶��� mbom_str �� bom_end_date = 'eco��������'	: Ȯ��
	//			2.adtag = 'A','RA' �϶��� mbom_str �� app_status = '1',bom_start_date = 'eco��������': Ȯ��
	//*******************************************************************/	
	public void changeBomFix(String eco_no,String order_date) throws Exception
	{
		//eco no�� �ش�Ǵ� ���泻�� ���� �б�
		ArrayList item_list = new ArrayList();
		item_list = tagetEccBomList(eco_no);

		//adtag�� ������ ó���ϱ� : MBOM_STR 
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (eccBomTable)item_iter.next();
			String pid = table.getPid();	
			String gid = table.getGid();	
			String adtag = table.getAdTag();
			if(adtag.equals("D") || adtag.equals("RB")) {
				update = "UPDATE MBOM_STR set bom_end_date='"+order_date+"' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			} else if(adtag.equals("A") || adtag.equals("RA")) {
				update = "UPDATE MBOM_STR set app_status='1',bom_start_date='"+order_date+"' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//ecc_bom�� �ݿ�(Audit �ݷ��� Ȯ������ ����ɰ�� ���) 
				update = "UPDATE ECC_BOM set bom_start_date='"+order_date+"' ";
				update += "WHERE eco_no='"+eco_no+"' and adtag='"+adtag+"'";
				modDAO.executeUpdate(update);
			}
		}

		//ecc_model�� �ݿ� (Audit �ݷ��� Ȯ������ ����ɰ�� ���)
		update = "UPDATE ECC_MODEL set order_date='"+order_date+"' ";
		update += "WHERE eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}
	//*******************************************************************
	// ���躯�� BOM����[��������� Ȯ���� UNDO���] : MBOM STR�� �ش� adtag�� ���� �ݿ��ϱ� 
	//	eco_no : ���躯���ȣ,  order_date : Ȯ������
	//			1.adtag = 'D','RB' �϶��� mbom_str �� bom_end_date = 'eco��������'	: Ȯ��
	//			2.adtag = 'A','RA' �϶��� mbom_str �� app_status = '1'				: Ȯ��
	//*******************************************************************/	
	public void changeBomFixUndo(String eco_no,String order_date) throws Exception
	{
		//eco no�� �ش�Ǵ� ���泻�� ���� �б�
		ArrayList item_list = new ArrayList();
		item_list = tagetEccBomList(eco_no);

		//adtag�� ������ ó���ϱ� : MBOM_STR
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (eccBomTable)item_iter.next();
			String pid = table.getPid();	
			String gid = table.getGid();	
			String adtag = table.getAdTag();
			if(adtag.equals("D") || adtag.equals("RB")) {
				update = "UPDATE MBOM_STR set bom_end_date='0' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/Fó���ϱ� (BOM���� ������ I/Fó��)
				update = "UPDATE MBOM_ITEM set bom_end_date='0',eco_no='',adtag='' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			} else if(adtag.equals("A") || adtag.equals("RA")) {
				update = "UPDATE MBOM_STR set app_status='0' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/Fó���ϱ� (BOM���� ������ I/Fó��)
				String delete = "DELETE FROM mbom_item WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(delete);
			}
			
		}
		//ECC_COM UNDO
		update = "UPDATE ECC_COM set ecc_status='8' where eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}

	//*******************************************************************
	// ���躯�濡 �ش�Ǵ� BOM���� ���� ����ϱ�
	//*******************************************************************/	
	public ArrayList tagetEccBomList(String eco_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"'";
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
}

