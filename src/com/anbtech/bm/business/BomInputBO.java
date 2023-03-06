package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomInputBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//�迭�����ϱ�
	
	private String query = "",update="";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	private String assy_ini = "1";				//ASSY�ڵ� ���� ù����
	private String phantom = "1PH";				//���� ASSY�ڵ� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomInputBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE �� ���� �޼ҵ� ����
	//			���/����/���� 
	//			BOM TREE����
	//---------------------------------------------------------------------

	//*******************************************************************
	// MBOM_STR�� ������ �Է��ϱ�
	//*******************************************************************/	
	public String insertStr(String gid,String parent_code,String child_code,
		String location,String op_code,String qty_unit,String qty,String part_cnt) throws Exception
	{
		String input="",data="",where="",bom_status="",assy_dup="";
		int cnt = Integer.parseInt(part_cnt);
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ

		//�˻�1 BOM���°˻� : BOM���¸� �Ǵ��Ͽ� Template�����̸� ������
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		}

		//LEVEL NO���ϱ� : Assy �ߺ��ڵ�[assy_dup='D']�� ������.
		query = "SELECT level_no FROM MBOM_STR where gid='"+gid+"' and child_code='"+parent_code+"'";
		query += " and assy_dup != 'D'";
		int level_no = modDAO.getLevelNo(query);

		//�˻�2 ��ǰ��˻� : PARENT_CODE�� �ش�𵨱���ü�迡 �ִ��� �˻��ϱ�
		if(level_no == 0) {
			data = "��ǰ���ڵ�["+parent_code+"]�� �ش�BOM���� ���ų� Phantom Assy�����ڵ��Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}

		//�˻�3 ��ǰ��˻� : �̹� �ٸ��𵨿� ����� ���� ������ �̿��ؾ������� ����
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "��ǰ���ڵ�["+parent_code+"]�� �̹� �ٸ��𵨿� ����Ǿ� �ֽ��ϴ�. ";
				return data;
		}

		//�˻�4 ��ǰ���ڵ�˻� : ASSY�ڵ��϶� �˻�
		String child_ini = child_code.substring(0,1);
		if(child_ini.equals(assy_ini)) {
			//4-1 �ڽ��� �𵨿� �ߺ��˻�
			query = "SELECT count(*) FROM mbom_str where gid='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "ASSY�ڵ�["+child_code+"]�� �ߺ����� ����� �� �����ϴ�."; return data;
			}

			//4-2 �ٸ� �𵨿��� ���Ǿ����� �ԷºҰ�
			query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "�ٸ��𵨿� ���� ASSY�ڵ�["+child_code+"]�Դϴ�. ASSY�ڵ�� �ߺ����� ����� �� �����ϴ�."; return data;
			}
		}

		//�˻�5.0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,�����
		part = modDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "�̵�� ��ǰ["+child_code+"]�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}
		
		//��ǰ���ڵ尡 phantom assy�ڵ����� �Ǵ��ϱ�
		if(child_code.indexOf(phantom) != -1) assy_dup = "D"; 

		//MBOM_STR�� �Է��ϱ� : ������ 1���� ����Ѵ�.[���������� ����]
		String add_date = anbdt.getDateNoformat();
		for(int i=0; i<cnt; i++) {
			String pid = anbdt.getNumID(i);
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
			input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
			input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
			input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
			input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
			////System.out.println("input : " + input);
			modDAO.executeUpdate(input);
		}

		//BOM���¸� MBOM_MASTER�� �����ϱ� (bom_status = '3' : BOM �����)
		update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
		modDAO.executeUpdate(update);

		//data = "���������� ��ϵǾ����ϴ�.";
		data = "";
		return data;
	}

	//*******************************************************************
	// MBOM_STR�� ������ �����ϱ�
	// part_type : A[Assy Code��:�Ϻα����� �ִ��� �������� �Ǵܵ�.]
	//*******************************************************************/	
	public String updateStr(String pid,String parent_code,String child_code,String location,String op_code,
		String qty_unit,String qty,String gid,String part_type) throws Exception
	{
		String data="",where="",bom_status="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ

		//�������� ��ǰ���ڵ� ã��
		where = "where pid = '"+pid+"'";
		String org_code = modDAO.getColumData("mbom_str","child_code",where);

		//�˻�1 BOM���°˻� : BOM���¸� �Ǵ��Ͽ� Template�����̸� ������
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		}

		//�˻�2 ��ǰ��˻� : �̹� �ٸ��𵨿� ����� ���� ������ �̿��ؾ������� ����
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "��ǰ���ڵ�["+parent_code+"]�� �̹� �ٸ��𵨿� ���Ǿ� �ֽ��ϴ�. ";
				data += "���躯���� �̿��ϰų�, ��ǰ���ڵ带 �ٸ�ǰ������ ������ �����Ͻʽÿ�."; 
				return data;
		}

		//�˻�3 ��ǰ��˻� : ASSY�ڵ��̸� �ڽſ� �ߺ��˻�,�ٸ��𵨿� �ߺ��˻�,
		String child_ini = child_code.substring(0,1);
		if(child_ini.equals(assy_ini)) {
			//�ڽ��� �𵨿� �ߺ����˻�
			query = "SELECT count(*) FROM mbom_str where gid='"+gid+"' and child_code='"+child_code+"'"; 
			query += " and pid != '"+pid+"'";
			if(modDAO.getTotalCount(query) > 0) {
				data = "ASSY�ڵ�["+child_code+"]�� �ߺ����� ����� �� �����ϴ�."; return data;
			}

			//�ٸ� �𵨿��� ���Ǿ����� �ԷºҰ�
			query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and child_code='"+child_code+"'"; 
			if(modDAO.getTotalCount(query) > 0) {
				data = "�ٸ��𵨿� ���� ASSY�ڵ�["+child_code+"]�Դϴ�. ASSY�ڵ�� �ߺ����� ����� �� �����ϴ�."; return data;
			}

		}

		//�˻�4. 0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,�����
		part = modDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "�̵�� ��ǰ["+child_code+"]�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}

		//��ǰ���ڵ尡 phantom assy�ڵ����� �Ǵ��ϱ�
		if(child_code.indexOf(phantom) != -1) assy_dup = "D"; 
		
		//MBOM_STR�� �����ϱ�
		String add_date = anbdt.getDateNoformat();
		update = "UPDATE MBOM_STR set child_code='"+child_code+"',location='"+location;
		update += "',part_name='"+part[0]+"',part_spec='"+part[1]+"',maker_name='"+part[2];
		update += "',maker_code='"+part[3]+"',price_unit='"+"��"+"',price='"+"0"+"',add_date='"+add_date;
		update += "',op_code='"+op_code+"',qty_unit='"+part[4]+"',qty='"+qty+"',assy_dup='"+assy_dup;
		update += "',item_type='"+part[5]+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//��ǰTYPE�� ����ASSY�ڵ��̸� �ش�Ǵ� ����Assy�ڵ� ��ü�� �ٲ��ش�
		if(part_type.equals("A")) {
			//op�ڵ� ���ϱ�
			where = "where item_no='"+child_code+"'";
			op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

			update = "UPDATE MBOM_STR set parent_code='"+child_code+"',op_code='"+op_code;
			update += "' where gid='"+gid+"' and parent_code='"+org_code+"'";
			modDAO.executeUpdate(update);
		}
		//data = "���������� ���� �Ǿ����ϴ�.";
		data="";
		return data;
	}

	//*******************************************************************
	// MBOM_STR�� ������ �����ϱ�
	//*******************************************************************/	
	public String deleteStr(String pid,String gid,String parent_code) throws Exception
	{
		String delete = "",data="",bom_status="";
		String where = "where pid='"+gid+"'";

		//������� �˻�
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		} 

		//�˻�1 ��ǰ��˻� : �̹� �ٸ��𵨿� ����� ���� ������ �̿��ؾ������� ����
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				data = "��ǰ���ڵ�["+parent_code+"]�� �̹� �ٸ��𵨿� ���ǰ� �־� ���������� �� �� �����ϴ�. ";
				data += "���躯���� �̿��Ͻʽÿ�."; 
				return data;
		}

		//��������
		delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(delete);
		//data = "���������� �����Ǿ����ϴ�.";
		data = "";
		

		//MBOM_MASTER�� BOM���� �����ϱ� (bom_status = '1' : �������� �ۼ���)
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		return data;
	}

	//*******************************************************************
	// MBOM_STR�� ������ �����ϱ� : �Ϻα��� ��ü�� �����ϱ�
	//*******************************************************************/	
	public String deleteAllStr(String pid,String gid,String parent_code) throws Exception
	{
		String delete = "",data="",bom_status="",level_no="",child_code="",p_code="";
		String where = "where pid='"+gid+"'";

		//������� �˻�
		where = "where pid = '"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		} 

		//�˻�1 ��ǰ��˻� : �̹� �ٸ��𵨿� ����� ���� ������ �̿��ؾ������� ����
		query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+parent_code+"'"; 
		if(modDAO.getTotalCount(query) > 0) {
				//��ü���������� �ֻ������� ��ǰ���ϱ�
				ArrayList assy_list = new ArrayList();
				assy_list = modDAO.getAssyListCP(gid);
				Iterator table_iter = assy_list.iterator();
				com.anbtech.bm.entity.mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
				while(table_iter.hasNext()) {
					table = (mbomStrTable)table_iter.next();
					p_code = table.getParentCode();
					query = "SELECT count(*) FROM mbom_str where gid !='"+gid+"' and parent_code='"+p_code+"'"; 
					if(modDAO.getTotalCount(query) > 0) break;
				}

				//�����ϰ����ϴ� ��ǰ���ڵ�� �ߺ���ǰ���ڵ��� �ֻ����ڵ尡 ����������
				if(!p_code.equals(parent_code)) {
					//�޽��� ����
					data = "��ǰ���ڵ�["+parent_code+"]�� �̹� �ٸ��𵨿� ���ǰ� �־� ������ �� �����ϴ�. ";
					data += "���躯���� �̿��ϰų�, ��ǰ�� ["+p_code+"] ���� ��ü������ ���� �Ͻʽÿ�."; 
					return data;
				}
		}

		//�Ϻα��������� ��ü�� ���ϱ� : gid,level_no,parent_code
		//��, assy_dup='D'�� �κ��� ������.
		where = "where gid='"+gid+"' and pid='"+pid+"'";
		level_no = modDAO.getColumData("MBOM_STR","level_no",where);
		level_no = Integer.toString(Integer.parseInt(level_no)+1);
		child_code = modDAO.getColumData("MBOM_STR","child_code",where);

		//�Ϻα��� ��ü �����ϱ�
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,child_code);

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			delete = "DELETE from MBOM_STR where pid='"+table.getPid()+"' and gid='"+gid+"'";
			modDAO.executeUpdate(delete);
		}

		//������ �÷� �����ϱ�
		delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"'";
		modDAO.executeUpdate(delete);

		//������ ��ǰ��� ���� ������ ǰ���ڵ� �����ϱ�
		delete = "DELETE from MBOM_STR where gid='"+gid+"' and parent_code='"+parent_code+"'";
		modDAO.executeUpdate(delete);

		
		//MBOM_MASTER�� BOM���� �����ϱ� (bom_status = '1' : �������� �ۼ���)
		query = "SELECT COUNT(*) FROM mbom_str WHERE gid = '"+gid+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt == 1) {
			update = "UPDATE mbom_master SET bom_status='1' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);
		}

		//data = "���������� �����Ǿ����ϴ�.";
		data = "";
		return data;
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * gid : group�����ڵ�, level_no : 0, parent_code : 0, url : ��ũ
	 **********************************************************************/
	public ArrayList getStrList(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭�� ��´�.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			String pcode = item[i][1];
			if(!item[i][3].equals("0"))	//ù��°���� ����[level no : 0]
				pcode = "<a href=\"javascript:strView('"+item[i][0]+"','"+gid+"','"+parent_code+"');\">"+item[i][1]+"</a>";
			table.setParentCode(pcode);
			table.setChildCode(item[i][2]);
			table.setLevelNo(item[i][3]);
			table.setPartName(item[i][4]);
			table.setPartSpec(item[i][5]);
			table.setLocation(item[i][6]);
			table.setOpCode(item[i][7]);
			table.setQtyUnit(item[i][8]);
			table.setQty(item[i][9]);		
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * MBOM_STR���� �ӽ�BOM TREE������ ���ϱ�
	 * gid : group�����ڵ�, level_no : 0, parent_code : 0, url : ��ũ
	 **********************************************************************/
	public ArrayList getTbomStrList(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭�� ��´�.
		saveBomArray(gid,level_no,parent_code);

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			table.setParentCode(item[i][1]);
			table.setChildCode(item[i][2]);
			table.setLevelNo(item[i][3]);
			table.setPartName(item[i][4]);
			table.setPartSpec(item[i][5]);
			table.setLocation(item[i][6]);
			table.setOpCode(item[i][7]);
			table.setQtyUnit(item[i][8]);
			table.setQty(item[i][9]);		
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * ��ϵ� BOM������ �迭�� ���
	 * ��ϵ� ���� ��º���
	 **********************************************************************/
	public void saveBomArray(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//�迭�� ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLevelNo();
			item[an][4] = table.getPartName();
			item[an][5] = table.getPartSpec();
			item[an][6] = table.getLocation();
			item[an][7] = table.getOpCode();
			item[an][8] = table.getQtyUnit();
			item[an][9] = table.getQty();
			an++;
		}
	}

	//--------------------------------------------------------------------
	//
	//		P/L IMPORT�� ���� ���� 1
	//		Format : ��ǰ�� / ��ǰ�� / Location �϶�
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * P/L IMPORT �б� 
	 * Format : ��ǰ�� / ��ǰ�� / Location �϶�
	 * multi�� �����а�, ck�� �����ϱ�
	 **********************************************************************/
	public String getImportList(MultipartRequest multi,String path,String ck,
		String parent_code,String level_no,String gid) throws Exception
	{
		String data = "",where="",bom_status="",pdg_code="",op_code="",child_code="";
		String input = "";
		String[] part = new String[7];				//��ǰ������ �������
		for(int i=0; i<7; i++) part[i] = "";		//�ʱ�ȭ
		String add_date = anbdt.getDateNoformat();	//�����


		//�˻�1 BOM������� : BOM���¸� �Ǵ��Ͽ� Template�����̸� ������
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		}

		//��ǰ���ڵ� ���ϱ�
		pdg_code = modDAO.getColumData("MBOM_MASTER","pdg_code",where);

		//�����б�� �˻� : Import PL�� �о� �����迭item�� ��� ����� ����
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
		String importPLOK = readImportList(multi,path,ck,parent_code,level_no);
		if(importPLOK.equals("N")) {
			for(int i=0; i<an; i++) {
				if(item[i][0].equals("99")) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			}
			if(data.length() == 0) data = "�����ڰ� Ʋ���ų�, ���Ͽ� ��ǰ���ڵ�["+parent_code+"]�� �����ϴ�.";
			else data += " ��  BOM TREE������ ����µ� ���۵� ��ǰ���ڵ尡 ���� ����� �� �����ϴ�. ������ �ٽ� �����Ͻʽÿ�.";
			return data;
		}

		//�˻�2 ��ǰ���ڵ尡 ����BOM�� ��ǰ��� �ߺ��̸� ����
		for(int i=0; i<an-1; i++) {
			if(!item[i][1].equals(item[i+1][1])) {
				query = "select count(*) from mbom_str where parent_code='"+item[i][1]+"' and gid != '"+gid+"'";
				int dup_cnt = modDAO.getTotalCount(query);
				if(dup_cnt != 0) data += item[i][1]+", ";
			}
		}
		if(data.length() != 0) { 
			data += " �� �ٸ��𵨿��� ����� ǰ���ڵ�� ����� �� �����ϴ�.";
			return data;
		}
			
		//�˻�3 ��ǰ��˻� : ��ǰ���ڵ�[4��°���� = pdg_code]�� ���� ��ǰ������ �ڵ����� �Ǵ��ϱ�
		String four_char = "";	//4��°���ڴ�
		int icnt = an - 1;
		for(int i=0; i<icnt; i++) {
			if(item[i][1] != item[i+1][1]) {	//��ǰ�񳢸��� ���������� (�ߺ����ϱ�����)
				four_char = item[i][1].substring(3,4);				
				if(!four_char.equals(pdg_code)) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			}
		}
		if(data.length() != 0) {
			data += " �� ��ǰ���ڵ�� ������ ��ǰ������ �ڵ尡 �ƴմϴ�. ���� �Է��� �� �����ϴ�.";
			return data;
		}

		//�˻�4 ��ǰ���ߺ� : ������ ��ǰ���ڵ�� IMPORT�� ��ǰ���ڵ尡 �ߺ��Ǵ��� �˻�
		//			 ��ǰ���ڵ�� ������ ������ �ٸ��� Error
		String was_pcd = "";		//������ϵ� ��ǰ���ڵ�

		com.anbtech.bm.entity.mbomStrTable assy = new com.anbtech.bm.entity.mbomStrTable();
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyList(gid);
		Iterator assy_iter = assy_list.iterator();			//������ Assy �ڵ�
		String f_lno = Integer.toString(Integer.parseInt(level_no));
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 

			was_pcd = assy.getParentCode();
			//��ǰ���ڵ� ���ϱ� 
			for(int i=0; i<an; i++) {
				if((was_pcd.equals(item[i][1])) && (!f_lno.equals(item[i][0]))){
					data += item[i][1]+"|"+item[i][2]+", ";
				}
			}
		}
		if(data.length() != 0) {
			data += " �� �̹� ��ϵǾ� �ִ���, ������ ASSY�ڵ尡 BOM TREE������ ����µ� ���۵� ��ǰ���ڵ����� Ȯ���� �ٽ� �����Ͻʽÿ�.";
			return data;
		}

		
		//�˻�5 �ߺ���ϰ˻�(��,��,����) 
		//�̵̹�ϵ� ASSY�ڵ尡 IMPORT�� ASSY�ڵ�� �ߺ��Ǹ� ���������Էµȴ�.
		int cnt = 0;
		for(int i=0; i<an; i++) {
			query = "select COUNT(*) from MBOM_STR where gid='"+gid+"' and parent_code='"+item[i][1]+"' ";
			query += "and child_code='"+item[i][2]+"' and level_no='"+item[i][0]+"'";
			int dcnt = modDAO.getTotalCount(query);
			
			if(dcnt != 0) data += item[i][1]+"/"+item[i][2]+"/"+item[i][3]+", ";
			cnt += dcnt;
		}
		if(cnt != 0) {
			data += "["+cnt+"���ߺ�]�� �־� ����� �� �����ϴ�.";
			return data;
		}

		//�˻�6 ��ǰ������ ��Ͽ��� 
		String[] indb = new String[an];			//������ �迭�� ��´�.
		for(int i=0; i<an; i++) {
			//0:��ǰ�̸�,1:�԰�,2:����Ŀ��,3:����,4:��������,5:item_type,6:op_code
			part = modDAO.getComponentInfo(item[i][2]);

			//��ǰ���ڵ��� OP CODE�� ���Ѵ�.
			where = "where item_no='"+item[i][1]+"'";
			op_code = modDAO.getColumData("ITEM_MASTER","op_code",where);

			//��ǰ������ ��ϰ˻�
			if(part[1].length() == 0) data += item[i][1]+"|"+item[i][2]+"|"+item[i][3]+", ";
			String pid = anbdt.getNumID(i);

			//INSERT������ �迭�� ��� �˻��� �����̸� �Է��Ѵ�.
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
			input += "buy_type,eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+item[i][1]+"','"+item[i][2]+"','"+Integer.parseInt(item[i][0])+"','";
			input += part[0]+"','"+part[1]+"','"+item[i][3]+"','"+op_code+"','"+part[4]+"','"+"1"+"','"+part[2]+"','";
			input += part[3]+"','"+"��"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
			indb[i] = input;	
		}

		//��� ��� ���� �Ǵ��Ͽ� ��ǰ���� ����ϱ�
		if(data.length() != 0) data += "�� �̵�� ��ǰ�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
		else {
			//INSERT������ �迭�� DB�� �Է��Ѵ�.
			for(int i=0; i<an; i++) modDAO.executeUpdate(indb[i]);

			//BOM���¸� MBOM_MASTER�� �����ϱ� (bom_status = '3' : BOM �����)
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);

			data = "���������� �ԷµǾ����ϴ�.";
		}
		
		return data;
		
	}

	/**********************************************************************
	 * P/L IMPORT �б� : ��ǰ���ڵ�,��ǰ���ڵ�,Location
	 * multi�� �����а�, ck�� �����Ͽ� ���������迭 item�� ���
	 * ���������� TREE�������Ǹ� Y, �ƴϸ� N [level no : 99]�� �����Ѵ�.
	 **********************************************************************/
	public String readImportList(MultipartRequest multi,String path,String ck,String parent_code,String level_no) throws Exception
	{
		String isTreeOK = "Y";
		int cnt = 0, ele = 0;
		int SheetNo = 0;			//Excel ������ ���� Sheet No (0,1,2 ...)

		com.anbtech.file.textFileReader reader = new com.anbtech.file.textFileReader();		//Text�� �б�
		com.anbtech.file.excelFileReader excel = new com.anbtech.file.excelFileReader();	//Excel�� �б�

		String file_name = multi.getFilesystemName("file_name");	//�����̸�
		String file = path+"/"+file_name;							//������������

		//Ȯ���� ã�� (xls : excel �׿� : text�� �ν�)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		//EXCEL���Ϸ� �б�
		if(ext_name.equals("xls")) {
			//1.������ �о� �����ڷ� �����Ͽ� �迭�� ���
			excel.setBomSheetArray(file,SheetNo);						//file�� �о� �迭�� ���
			excel.delFilename(file);									//IMPORT�� ���� �����ϱ�		

			//2.�迭�����͸� ������ �迭item�� ���
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = excel.getRowCount();									//������ line ����
			this.an = cnt;
			ele = excel.getMaxColumnCount();							//line�� �����ڷ� ���е� element����
			item = new String[cnt][ele];			
			item = excel.readSheetArray();								//���� ������ �迭�� ��������

		}
		//TEXT���Ϸ� �б�
		else {
			//1.������ �о� �����ڷ� �����Ͽ� �迭�� ���
			reader.readFileArray(file,ck);								//file�� �о� �迭�� ���
			reader.delFilename(file);									//IMPORT�� ���� �����ϱ�		

			//2.�迭�����͸� ������ �迭item�� ���
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = reader.getFileArrayCount();						//������ line ����
			this.an = cnt;
			ele = reader.getFileElementCount();						//line�� �����ڷ� ���е� element����
			item = new String[cnt][ele];			
			item = reader.getFileArray();							//���� ������ �迭�� ��������
		}

		//3.�־��� �ڵ�� �迭1�� ������ ������ ������ȣ �Է�
		//������ �Ѷ����� BOM���� �Ѷ��ΰ� ���ƾ� �Ѵ�.�̸� �������� ������ �����Ѵ�.
		int pc = 0;
		for(int i=0; i<cnt; i++) {
			if(item[i][1].equals(parent_code)) { 
				//item[i][0] = Integer.toString(Integer.parseInt(level_no)+1); 
				item[i][0] = Integer.toString(Integer.parseInt(level_no)); 
				pc++; 
			}
		}
		if(pc == 0) {		//�����ڰ� Ʋ���ų� ��ǰ���ڵ�� �´°��� �ϳ��� ����
			isTreeOK = "N"; return isTreeOK;
		}

		//4.������ȣ�� ������ ������ �ø���.
		bubbleSort(cnt,ele);

		//5.�迭��ȣ�� ���� ������ �迭�� ������ȣ�� �Է��Ѵ�.
		String pcd = "";
		int lvn = 0;
		for(int i=0; i<cnt; i++) {			//��ü ����Ƚ��
			pcd = item[i][2];				//��ǰ���� ��ǰ������ ��ġ
			if(item[i][0].length() != 0) {
				lvn = Integer.parseInt(item[i][0])+1;
				//������ȣ �Է�
				for(int j=i; j<cnt; j++) if(item[j][1].equals(pcd)) item[j][0] = Integer.toString(lvn);
				//������ȣ�� ������ ������ �ø���.
				bubbleSort(cnt,ele);
			}
		}

		//6.������ȣ�� �Է��� �� ����[TREE������ �ɼ�����]PL�� ������ȣ�� 99�� �Է��Ѵ�.
		for(int i=0; i<cnt; i++) if(item[i][0].length() == 0) { item[i][0] = "99"; isTreeOK="N"; }

		//7.�����ϱ� (��ǰ���ڵ��)
		sort.bubbleSortStringMultiDesc(item,1);

		return isTreeOK;
	}

	/**********************************************************************
	 * SORT
	 * ������ȣ�� ������ ������ �ø���.
	 **********************************************************************/
	public void bubbleSort(int cnt,int ele)
	{
		String[] swap = new String[ele];	//swap�� �ӽù迭
		for(int i=1; i<cnt; i++) {			//��ü ����Ƚ��
			for(int j=0; j<cnt-1; j++) {	//��
				if(item[j][0].length() == 0) {
					for(int s=0; s<ele; s++) {
						swap[s] = item[j][s];
						item[j][s] = item[j+1][s];
						item[j+1][s] = swap[s];
					}
				}
			}
		}
	}

	//--------------------------------------------------------------------
	//
	//		P/L IMPORT�� ���� ���� 2
	//		Format : ǰ���ڵ�,�����ڵ�,Location �϶�
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * P/L IMPORT �б� 
	 * Format : ǰ���ڵ� / �����ڵ� / Location �϶�
	 * multi�� �����а�, ck�� �����ϱ�
	 **********************************************************************/
	public String getImportPartList(MultipartRequest multi,String path,String ck,String gid) throws Exception
	{
		String data="",where="",bom_status="",input="";
		String[] part = new String[6];				//��ǰ������ �������
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ
		String add_date = anbdt.getDateNoformat();	//�����

		//1.BOM���¸� �Ǵ��Ͽ� Template�����̸� ������
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		}

		//2.Import PL�� �о� �����迭item�� ��� ����� ����
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location,item[][4]:op code
		String importPLOK = readImportPartList(multi,path,ck,gid);
		if(!importPLOK.equals("Y")) {
			return importPLOK;
		}

		//3.�ű� ����϶� ��ǰ���� �˻��ϱ�
		String[] indb = new String[an];			//������ �迭�� ��´�.
		for(int i=0; i<an; i++) {
			//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type
			part = modDAO.getComponentInfo(item[i][2]);
			if(part[1].length() == 0) data += item[i][1]+"|"+item[i][2]+"|"+item[i][3]+", ";
			String pid = anbdt.getNumID(i);

			//INSERT������ �迭�� ��� �˻��� �����̸� �Է��Ѵ�.
			input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
			input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,";
			input += "buy_type,eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
			input += pid+"','"+gid+"','"+item[i][1]+"','"+item[i][2]+"','"+Integer.parseInt(item[i][0])+"','";
			input += part[0]+"','"+part[1]+"','"+item[i][3]+"','"+item[i][4]+"','"+part[4]+"','"+"1"+"','"+part[2]+"','";
			input += part[3]+"','"+"��"+"','"+"0"+"','"+add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','";
			input += "0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
			indb[i] = input;	
		}

		//4.�űԺ�ǰ�� ��ǰ�����Ϳ� ��ϵǾ����� �Ǵ��Ͽ� BOM����ϱ�
		if(data.length() != 0) data += "�� �̵�� ��ǰ�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
		else {
			//INSERT������ �迭�� DB�� �Է��Ѵ�.
			for(int i=0; i<an; i++) modDAO.executeUpdate(indb[i]);

			//BOM���¸� MBOM_MASTER�� �����ϱ� (bom_status = '3' : BOM �����)
			update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
			modDAO.executeUpdate(update);

			//Template�� �̿��� ǰ������ �Ǿ����� MBOM_STR�� �����ϱ� (tag = '0' )
			update = "UPDATE mbom_str SET tag='0' where gid = '"+gid+"'";
			modDAO.executeUpdate(update);

			data = "���������� �ԷµǾ����ϴ�.";
		}
		
		return data;
	}

	/**********************************************************************
	 * P/L IMPORT �б� : ǰ���ڵ�,�����ڵ�,Location
	 * multi�� �����а�, ck�� �����Ͽ� ���������迭 item�� ���
	 **********************************************************************/
	public String readImportPartList(MultipartRequest multi,String path,String ck,String gid) throws Exception
	{
		String isTreeOK = "";
		String[][] part;
		int cnt = 0, ele = 0;
		int SheetNo = 0;			//Excel ������ ���� Sheet No (0,1,2 ...)

		com.anbtech.file.textFileReader reader = new com.anbtech.file.textFileReader();		//Text�� �б�
		com.anbtech.file.excelFileReader excel = new com.anbtech.file.excelFileReader();	//Excel�� �б�

		String file_name = multi.getFilesystemName("file_name");	//�����̸�
		String file = path+"/"+file_name;							//������������

		//Ȯ���� ã�� (xls : excel �׿� : text�� �ν�)
		String ext_name = file_name.substring(file_name.lastIndexOf(".")+1,file_name.length());

		//EXCEL���Ϸ� �б�
		if(ext_name.equals("xls")) {
			//1.������ �о� �����ڷ� �����Ͽ� �迭�� ���
			excel.setBomSheetArray(file,SheetNo);						//file�� �о� �迭�� ���
			excel.delFilename(file);									//IMPORT�� ���� �����ϱ�		

			//2.�迭�����͸� ������ �迭item�� ���
			//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location
			cnt = excel.getRowCount();									//������ line ����
			this.an = cnt;
			ele = excel.getMaxColumnCount();							//line�� �����ڷ� ���е� element����
			part = new String[cnt][ele];		
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) part[i][j]="";			
			part = excel.readSheetArray();								//���� ������ �迭�� ��������

		}
		//TEXT���Ϸ� �б�
		else {

			//1.������ �о� �����ڷ� �����Ͽ� �迭�� ���
			reader.readFileArray(file,ck);								//file�� �о� �迭�� ���
			reader.delFilename(file);									//IMPORT�� ���� �����ϱ�		

			//2.��ǰ����Ʈ�� �о� �迭 part�� ���
			//part[][0]:level_no,part[][1]:ccd,part[][2]:op code,part[][3]:location
			cnt = reader.getFileArrayCount();						//������ line ����
			this.an = cnt;
			ele = reader.getFileElementCount();						//line�� �����ڷ� ���е� element����
			part = new String[cnt][ele];		
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) part[i][j]="";
			part = reader.getFileArray();								//���� ������ �迭�� ��������
		}

		//���� ���ð˻�
		if(cnt == 0) {
			isTreeOK = "������ ���� �� �����ϴ�. Ȯ���Ͻð� �ٽ� ����Ͻʽÿ�.";
			return isTreeOK;
		}
		
		//������ �˻�
		if(ele < 4) {
			for(int i=0; i<cnt; i++) for(int j=0; j<ele; j++) isTreeOK += part[i][j]+", ";
			isTreeOK += "�����ڸ� Ȯ���Ͻð� �ٽ� ����Ͻʽÿ�.";
			return isTreeOK;
		}
		
		//3.�迭 part�� ������ �����迭 item���� �Űܴ��
		//item[][0]:level_no,item[][1]:pcd,item[][2]:ccd,item[][3]:location,item[][4]:op code
		item = new String[cnt][5];
		for(int i=0; i<cnt; i++) {
			item[i][0] = "";
			item[i][1] = "";
			item[i][2] = part[i][1];		
			item[i][3] = part[i][3];
			item[i][4] = part[i][2];
		}
		
		//4.�ش�BOM�� Template���� ��ϵ� Assy Code ã��
		mbomStrTable assy = null;
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyListTemp(gid);
		Iterator assy_iter = assy_list.iterator();			
		int assy_cnt = assy_list.size();

		String[][] data = new String[assy_cnt][3];
		int as = 0;
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 
			data[as][0] = assy.getLevelNo();
			data[as][1] = assy.getParentCode();
			data[as][2] = assy.getOpCode();
			as++;
		}
		if(assy_cnt == 0) {
			isTreeOK = "�������ø����� ������ ��츸 ǰ�񸮽�Ʈ ����� �����մϴ�.";
			isTreeOK += "�̹� ǰ��LIST����� �Ͽ��ų� �������ø����� �������� �ʾҽ��ϴ�.";
			return isTreeOK;
		}

		//5.item�迭�� Level no �� parent code �Է��ϱ�
		for(int i=0; i<cnt; i++) {
			for(int j=0; j<as; j++) {
				if(item[i][4].equals(data[j][2])) {
					item[i][0] = data[j][0];
					item[i][1] = data[j][1];
				}
			}
		}
		
		//6.Level no�� ���°��� ǰ��LIST�� �������ø��� �ٸ����� �޽��� ���
		for(int i=0; i<cnt; i++) {
			if(item[i][0].length() == 0) isTreeOK += item[i][2]+"/"+item[i][3]+"/"+item[i][4]+", ";
		}
		if(isTreeOK.length() !=0) {
			isTreeOK += " �� �����ڵ忡 �ش�Ǵ� ASSY�ڵ尡 ���ų� �����ڰ� �ٸ��ϴ�.";
			isTreeOK += " Ȯ���� �ٽ� ����Ͻʽÿ�.";
			return isTreeOK;
		}

		isTreeOK = "Y";
		return isTreeOK;
	}

	//--------------------------------------------------------------------
	//
	//		BOM�� �̿��Ͽ� ��ǰ���ڵ���� Unique�� �������� �����ϱ�
	//		[���� �����Ҷ� Ȱ��]
	//		* ���� ��ǰ���ڵ�� unique�� ���븸 �����(location no������� ����)
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�  [���������Ҷ� Ȱ��]
	 * ������ TREE ����ü�踦 �迭�� ��� : �Ϻα��� ��ü
	 **********************************************************************/
	public ArrayList getUniqueMultiLevelBom(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//�迭�� ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getParentCode();
			data[n][2] = table.getChildCode();
			data[n][3] = table.getLevelNo();
			data[n][4] = table.getPartName();
			data[n][5] = table.getPartSpec();
			data[n][6] = table.getLocation();
			data[n][7] = table.getOpCode();
			data[n][8] = table.getQtyUnit();
			data[n][9] = table.getQty();
			data[n][10] = "";					//������ ��ǰ������ �Է��ϱ� ����
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique�� ��ǰ���ڵ�� ���� ����ϱ�
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//���Ϻ�ǰ���� �����Կ� ���� ��迭 ���ְ� �ܰ��Է��ϱ�[�迭�߰�]
		//10:����,11:ǥ�شܰ�,12:��մܰ�,13:����ܰ�,14:ǥ���Ѿ�,15:����Ѿ�,16:�����Ѿ�
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//ǥ�شܰ�
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//��մܰ�
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//����ܰ�
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List�� ���
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//ǰ���ڵ�
			price.setItemName(t[i][4]);		//ǰ���̸�
			price.setItemDesc(t[i][5]);		//ǰ��԰�
			price.setItemCount(t[i][10]);	//ǰ�����
			price.setStdPrice(t[i][11]);	//ǥ�شܰ�
			price.setAvePrice(t[i][12]);	//��մܰ�
			price.setCurPrice(t[i][13]);	//����ܰ�
			price.setStdSum(t[i][14]);		//ǥ�شܰ� �Ѿ�
			price.setAveSum(t[i][15]);		//��մܰ� �Ѿ�
			price.setCurSum(t[i][16]);		//����ܰ� �Ѿ�

			price_list.add(price);
		}

/*		//����غ���
		primeCostTable view = new primeCostTable();
		Iterator price_iter = price_list.iterator();
		while(price_iter.hasNext()) {
			view = (primeCostTable)price_iter.next();
			//System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;

	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ� [���������Ҷ� Ȱ��]
	 * ������ TREE ����ü�踦 �迭�� ��� : �ش�Assy
	 **********************************************************************/
	public ArrayList getUniqueSingleLevelBom(String gid,String level_no,String parent_code) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getSingleForwardItems(gid,level_no,parent_code);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//�迭�� ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getParentCode();
			data[n][2] = table.getChildCode();
			data[n][3] = table.getLevelNo();
			data[n][4] = table.getPartName();
			data[n][5] = table.getPartSpec();
			data[n][6] = table.getLocation();
			data[n][7] = table.getOpCode();
			data[n][8] = table.getQtyUnit();
			data[n][9] = table.getQty();
			data[n][10] = "";					//������ ��ǰ������ �Է��ϱ� ����
			////System.out.println(data[n][3]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}
		//Unique�� ��ǰ���ڵ�� ���� ����ϱ�
		sort.bubbleUniqueSortStringMultiAsc(data,2);
		
		//���Ϻ�ǰ���� �����Կ� ���� ��迭 ���ְ� �ܰ��Է��ϱ�[�迭�߰�]
		//10:����,11:ǥ�شܰ�,12:��մܰ�,13:����ܰ�,14:ǥ���Ѿ�,15:����Ѿ�,16:�����Ѿ�
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][2]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//ǥ�شܰ�
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//��մܰ�
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//����ܰ�
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][10])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List�� ���
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][2]);		//ǰ���ڵ�
			price.setItemName(t[i][4]);		//ǰ���̸�
			price.setItemDesc(t[i][5]);		//ǰ��԰�
			price.setItemCount(t[i][10]);	//ǰ�����
			price.setStdPrice(t[i][11]);	//ǥ�شܰ�
			price.setAvePrice(t[i][12]);	//��մܰ�
			price.setCurPrice(t[i][13]);	//����ܰ�
			price.setStdSum(t[i][14]);		//ǥ�شܰ� �Ѿ�
			price.setAveSum(t[i][15]);		//��մܰ� �Ѿ�
			price.setCurSum(t[i][16]);		//����ܰ� �Ѿ�

			price_list.add(price);
		}

/*		//����غ���
		primeCostTable view = new primeCostTable();
		Iterator price_iter = price_list.iterator();
		while(price_iter.hasNext()) {
			view = (primeCostTable)price_iter.next();
			//System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;
	}
}