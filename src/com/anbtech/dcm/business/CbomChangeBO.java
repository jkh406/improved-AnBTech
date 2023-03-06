package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomChangeBO
{
	private Connection con;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private com.anbtech.dcm.db.CbomChangeDAO chgDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//�����ϱ�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	private String fg_head = "F";				//FG�ڵ常 ã�� ����
	private String phantom = "1PH";				//���� ASSY�ڵ� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public CbomChangeBO(Connection con) 
	{
		this.con = con;
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
		chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		���躯�� �ش��ǰ �Է�/����/���� �����ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� ������ ó���ϱ�
	// cpid : ���������͸� ó���ϱ� (����,������)
	//*******************************************************************/	
	public String changeEcoPart(String cpid,String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String data="",where="",ecc_status="",fg_code="";
		
		//�˻�1. ECO������°� ECO�ۼ������̸� ������ �����ϴ�.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO�ݷ������Դϴ�. ECO������ ��������� �����Ͻʽÿ�.";
			else if(ecc_status.equals("7")) data = "ECO������ �Դϴ�.";
			else data = "ECO�ۼ����¿����� ����� �����մϴ�.";
			return data;
		}

		//------------------------------------------
		//�ش� �̺�Ʈ�� �̵��Ͽ� ó���ϱ� : ECC_BOM
		//------------------------------------------
		if(adtag.equals("A")) {					//�űԺ�ǰ �߰��ϱ�
			data = insertEcoPart(gid,parent_code,child_code,location,op_code,qty_unit,qty,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		} else if(adtag.equals("R")) {			//��ǰ �ٲٱ�
			data = replaceEcoPart(cpid,gid,parent_code,child_code,location,op_code,qty_unit,qty,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		} else if(adtag.equals("D")) {			//������ǰ �����ϱ�
			data = deleteEcoPart(cpid,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		}

		//--------------------------------------
		//�ش��[FG]���� ó���ϱ� : ECC_MODEL
		//--------------------------------------
		where = "where gid='"+gid+"' and eco_no='"+eco_no+"'";	
		fg_code = chgDAO.getColumData("ECC_MODEL","fg_code",where);
		if(fg_code.length() == 0) {				
			insertEccModel(gid,eco_no,order_date);
		}

		return data;
	}

	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� �űԺ�ǰ �߰��ϱ�
	// adtag : A
	//*******************************************************************/	
	public String insertEcoPart(String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String input="",data="",where="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ

		//LEVEL NO���ϱ� 
		query = "SELECT level_no FROM MBOM_STR where gid='"+gid+"' and child_code='"+parent_code+"'";
		query += " and assy_dup != 'D'";
		int level_no = chgDAO.getLevelNo(query);

		//�˻�1.PARENT_CODE�� �ش�𵨱���ü�迡 �ִ��� �˻��ϱ�
		if(level_no == 0) {
			data = "��ǰ���ڵ尡 �ش�BOM���� ���ų� Phantom Assy�����ڵ��Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}

		//��ǰ���ڵ尡 phantom assy�ڵ����� �Ǵ��ϱ�
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,�����
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "�̵�� ��ǰ�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}
		String add_date = anbdt.getDateNoformat();
		
		//----------------------------------------------------------
		//MBOM_STR�� �Է��ϱ�  
		// ---------------------------------------------------------
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+"A"+"','"+order_date+"','"+"0"+"','";
		input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);

		//��¼���[chg_order]ã��
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM�� �Է��ϱ�  
		// ---------------------------------------------------------
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','";
		input += "A"+"','"+add_date+"','"+order_date+"','"+"0"+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}
	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� �����ϱ�
	// adtag : R
	//*******************************************************************/	
	public String replaceEcoPart(String cpid,String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String input="",update="",data="",where="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ

		//���������� �б�
		mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();
		mst = chgDAO.readStrItem(cpid);

		//�˻�1. ���� ECO_NO�� ������ ������ Replace �ϴ��� �˻��ϱ�
		where = "where eco_no='"+eco_no+"' and pid='"+cpid+"'";
		String chk_pid = chgDAO.getColumData("ecc_bom","pid",where);
		if(chk_pid.equals(cpid)) {
			data = "�̹� ��ǰ������ �����Ͽ����ϴ�.";
			return data;
		}
		
		//��ǰ���ڵ尡 phantom assy�ڵ����� �Ǵ��ϱ�
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,�����
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "�̵�� ��ǰ�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}
		String add_date = anbdt.getDateNoformat();
		
		//----------------------------------------------------------
		//MBOM_STR�� �Է��ϱ�  
		// ---------------------------------------------------------
		//1. ���������� adtag='RB' : Replace Before
		update = "UPDATE MBOM_STR set eco_no='"+eco_no+"',adtag='RB' where pid='"+cpid+"'";
		//System.out.println("update : " + update);
		chgDAO.executeUpdate(update);

		//2. Replace Data �Է��ϱ� 
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+mst.getLevelNo()+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+"RA"+"','"+order_date+"','"+"0"+"','";
		input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);



		//��¼���[chg_order]ã��
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM�� �Է��ϱ�  
		// ---------------------------------------------------------
		//1. ���������� MBOM_STR�� ������ �Է��ϱ�
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
		input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
		input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
		input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
		input += mst.getBuyType()+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','"+"RB"+"','"+add_date+"','";
		input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+mst.getAssyDup()+"','"+mst.getItemType()+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);

		//2. �߰��� ������ �Է��ϱ�
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+mst.getLevelNo()+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "��"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','";
		input += "RA"+"','"+add_date+"','"+order_date+"','"+"0"+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "���������� ����Ǿ����ϴ�.";
		return data;
	}
	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� ������ǰ �����ϱ�
	// adtag : D
	//*******************************************************************/	
	public String deleteEcoPart(String cpid,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String update="",input="",data="",where="";
		
		//----------------------------------------------------------
		//MBOM_STR�� adtag='D' update�ϱ�  
		// ---------------------------------------------------------
		update = "UPDATE MBOM_STR set eco_no='"+eco_no+"',adtag='D' where pid='"+cpid+"'";
		//System.out.println("update : " + update);
		chgDAO.executeUpdate(update);

		//������ ���� ã��
		mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();
		mst = chgDAO.readStrItem(cpid);

		//�˻�1. ���� ECO_NO�� ������ ������ Replace �ϴ��� �˻��ϱ�
		where = "where eco_no='"+eco_no+"' and pid='"+cpid+"'";
		String chk_pid = chgDAO.getColumData("ecc_bom","pid",where);
		if(chk_pid.equals(cpid)) {
			data = "�̹� ��ǰ������ �����Ͽ����ϴ�.";
			return data;
		}

		//������ ã��
		String change_date = anbdt.getDateNoformat();

		//��¼���[chg_order]ã��
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM�� �Է��ϱ�  
		// ---------------------------------------------------------
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
		input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
		input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
		input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
		input += mst.getBuyType()+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','"+"D"+"','"+change_date+"','";
		input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+mst.getAssyDup()+"','"+mst.getItemType()+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}
	//*******************************************************************
	// ECC MODEL�� ����Ǵ� ��ǰ�� ��[FG]������ �Է��ϱ�
	// 
	//*******************************************************************/	
	public void insertEccModel(String gid,String eco_no,String order_date) throws Exception
	{
		String input="",where="",fg_code="",model_code="",model_name="";

		//���ڵ�,�𵨸�,fg_code���ϱ�
		where = "where pid='"+gid+"'";
		model_code=chgDAO.getColumData("MBOM_MASTER","model_code",where);
		model_name=chgDAO.getColumData("MBOM_MASTER","model_name",where);
		fg_code=chgDAO.getColumData("MBOM_MASTER","fg_code",where);

		//����ϱ�
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO ECC_MODEL (pid,eco_no,gid,model_code,model_name,fg_code,order_date) values('";
		input += pid+"','"+eco_no+"','"+gid+"','"+model_code+"','"+model_name+"','"+fg_code+"','";
		input += order_date+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
	}

	//--------------------------------------------------------------------
	//
	//		���躯�� �ش��ǰ �����ϱ� [���� / UNDO]
	//		
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� ������ �����ϱ�
	//  ADTAG = 'A' or 'RA / RB' �����϶�
	//*******************************************************************/	
	public String modifyEcoPart(String pid,String gid,String child_code,String location,String op_code,
		String eco_no,String adtag,String chg_id,String chg_name,String ecc_reason,String note) throws Exception
	{
		String data="",where="",ecc_status="",app_status="",update="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ
		
		//�˻�1. ECO������°� ECO�ۼ������̸� ������ �����ϴ�.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO�ݷ������Դϴ�. ECO������ ��������� �����Ͻʽÿ�.";
			else if(ecc_status.equals("7")) data = "ECO������ �Դϴ�.";
			else data = "ECO�ۼ����¿����� ����� �����մϴ�.";
			return data;
		}

		//�˻�2. ECC_BOM�� app_status='0'�϶��� ������ �����ϴ�.
		where = "where pid='"+pid+"'";
		ecc_status = chgDAO.getColumData("ECC_BOM","app_status",where);
		if(ecc_status.equals("1")) {
			data = "���躯�� ���ε� ��ǰ�� ������ �� �����ϴ�.";
			return data;
		}

		//��ǰ���ڵ尡 phantom assy�ڵ����� �Ǵ��ϱ�
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "�̵�� ��ǰ�Դϴ�. Ȯ���� �ٽ� �Է��Ͻʽÿ�.";
			return data;
		}
		String change_date = anbdt.getDateNoformat();

		//�����ϱ� : MBOM_STR
		update = "UPDATE mbom_str set child_code='"+child_code+"',part_name='"+part[0]+"',";
		update += "part_spec='"+part[1]+"',location='"+location+"',op_code='"+op_code+"',";
		update += "maker_name='"+part[2]+"',maker_code='"+part[3]+"',price_unit='"+"��"+"',";
		update += "price='"+"0"+"',qty_unit='"+part[4]+"',assy_dup='"+assy_dup+"',item_type='"+part[5]+"' ";
		update += "where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		chgDAO.executeUpdate(update);

		//�����ϱ� : ECC_BOM
		update = "UPDATE ecc_bom set child_code='"+child_code+"',part_name='"+part[0]+"',";
		update += "part_spec='"+part[1]+"',location='"+location+"',op_code='"+op_code+"',";
		update += "maker_name='"+part[2]+"',maker_code='"+part[3]+"',price_unit='"+"��"+"',";
		update += "price='"+"0"+"',chg_id='"+chg_id+"',chg_name='"+chg_name+"',qty_unit='"+part[4]+"',";
		update += "change_date='"+change_date+"',ecc_reason='"+ecc_reason+"',note='"+note+"',";
		update += "assy_dup='"+assy_dup+"',item_type='"+part[5]+"' ";
		update += "where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		chgDAO.executeUpdate(update);
		 
		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// MBOM_STR �� ECC_BOM �� ������ UNDO�����ϱ�
	//  ADTAG = 'A' or 'RA / RB' or 'D'
	//  cpid : ����������[D,RB], pid : �űԵ�����[A,RA]
	//*******************************************************************/	
	public String undoEcoPart(String cpid,String pid,String gid,String adtag,String eco_no) throws Exception
	{
		String data="",where="",ecc_status="",app_status="",delete="",update="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ
		
		//�˻�1. ECO������°� ECO�ۼ������̸� ������ �����ϴ�.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO�ݷ������Դϴ�. ECO������ ��������� �����Ͻʽÿ�.";
			else if(ecc_status.equals("7")) data = "ECO������ �Դϴ�.";
			else data = "ECO�ۼ����¿����� ����� �����մϴ�.";
			return data;
		}

		//�˻�2. ECC_BOM�� app_status='0'�϶��� ������ �����ϴ�.
		where = "where pid='"+pid+"'";
		ecc_status = chgDAO.getColumData("ECC_BOM","app_status",where);
		if(ecc_status.equals("1")) {
			data = "���躯�� ���ε� ��ǰ�� ������ �� �����ϴ�.";
			return data;
		}

		//-----------------------------------
		//UNDO �����ϱ�
		//-----------------------------------
		if(adtag.equals("A")) {									//�űԷ� �߰��� ���
			//MBOM_STR���� �ش系���� ���������Ѵ�.
			delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);

			//ECC_BOM���� �ش系���� ���������Ѵ�.
			delete = "DELETE from ECC_BOM where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);
		} else if(adtag.equals("D")) {							//���������� ����ǥ���� ���
			//MBOM_STR���� ������ ���õ� ������ ���󺹱��Ѵ�.
			update = "UPDATE MBOM_STR set eco_no='',adtag='' where pid='"+cpid+"' and gid='"+gid+"'";
			chgDAO.executeUpdate(update);

			//ECC_BOM���� �ش系���� ���������Ѵ�.
			delete = "DELETE from ECC_BOM where pid='"+cpid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);
		} else if(adtag.equals("RA") || adtag.equals("RB")) {	//��ǰ ������ ���
			//MBOM_STR���� ������ ���õ� ������ ���󺹱��Ѵ�.
			update = "UPDATE MBOM_STR set eco_no='',adtag='' where pid='"+cpid+"' and gid='"+gid+"'";	//RB
			chgDAO.executeUpdate(update);

			delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RB
			chgDAO.executeUpdate(delete);

			//ECC_BOM���� �ش系���� ���������Ѵ�.
			delete = "DELETE from ECC_BOM where pid='"+cpid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RA
			chgDAO.executeUpdate(delete);
			delete = "DELETE from ECC_BOM where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RA
			chgDAO.executeUpdate(delete);
		}

		//---------------------------------------------------------------------
		//	ECC BOM���� �ش� FG�� ��� undo �Ǿ����� ECC MODEL������ undo�ϱ�
		//---------------------------------------------------------------------
		query = "SELECT count(*) FROM ecc_bom WHERE gid='"+gid+"' and eco_no='"+eco_no+"'";	
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) {				
			delete = "DELETE FROM ecc_model WHERE gid='"+gid+"' and eco_no='"+eco_no+"'";						
			chgDAO.executeUpdate(delete);
		}
		
		data = "���������� UNDO �Ǿ����ϴ�.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		���躯���� BOM ���� ����
	//
	//
	//---------------------------------------------------------------------

	/**********************************************************************
	 * ���躯���� BOM���� ����
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList viewCbomStrList(String gid,String level_no,String parent_code) throws Exception
	{
		//����
		String sel_date = anbdt.getDateNoformat();

		//�迭�� ���
		saveFrdStrArray(gid,level_no,parent_code,sel_date);

		//��ũ�޾� �ٽ� ArrayList�� ���
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			String pcode = item[i][1];
			if(!item[i][3].equals("0"))	//ù��°���� ����[level no : 0]
				pcode = "<a href=\"javascript:strView('"+item[i][0]+"','"+gid+"');\">"+item[i][1]+"</a>";
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
	 * ���躯���� ��� ����� LIST
	 *   �߻��� + ��ü����
	 **********************************************************************/
	public ArrayList targetModelList(String eco_no) throws Exception
	{
		String where = "";
		ArrayList table_list = new ArrayList();			//��ü FG�ڵ� ��� �ӽ�
		ArrayList fg_list = new ArrayList();			//unique �� FG�ڵ� ��� ����
		
		//--------------------------------------
		//�߻���[FG�ڵ�] ���� �����ϱ�
		//--------------------------------------
		where = "where eco_no like '"+eco_no+"%'";
		String fg_code_list = chgDAO.getColumData("ecc_com","fg_code",where);
		StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
		while(list.hasMoreTokens()) {
			String fcd = list.nextToken(); fcd = fcd.trim();
			eccModelTable table = new eccModelTable();
			table.setFgCode(fcd);
			table_list.add(table);
		}

		//---------------------------------------
		//�����[FG�ڵ�] ���� ã��
		//---------------------------------------	
		ArrayList model_list = new ArrayList();
		model_list = viewRevAllFGList(eco_no);
		eccModelTable model = new eccModelTable();
		Iterator model_iter = model_list.iterator();
		while(model_iter.hasNext()) {
			model = (eccModelTable)model_iter.next();
			
			eccModelTable table = new eccModelTable();
			table.setFgCode(model.getFgCode());
			table_list.add(table);
		}

		//------------------------------------
		//�迭�� ��� ����ũ�� fg code�� �̱�
		//------------------------------------
		int cnt = table_list.size();
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		eccModelTable fcode = new eccModelTable();
		Iterator fcode_iter = table_list.iterator();
		int n=0;
		while(fcode_iter.hasNext()) {
			fcode = (eccModelTable)fcode_iter.next();
			data[n] = fcode.getFgCode();	//System.out.println("org fg : " + data[n]);
			n++;
		}

		//�����ϱ�
		sort.bubbleSortStringAsc(data);
		
		//�ߺ�FG�ڵ�� ""�� �Է��ϱ�
		int dn = 0;			//�ߺ��� FG�ڵ� ����
		for(int i=0; i<cnt-1; i++) if(data[i].equals(data[i+1])) { data[i] = ""; dn++; }

		//�ߺ��� ������ ���븸 �迭�� �ٽ� ��´�.
		int un = cnt - dn;					//unique�� ����
		String[] udata = new String[un];	//unique�� FG�ڵ� ��
		int k = 0;
		for(int i=0; i<cnt; i++) if(data[i].length() != 0) { udata[k]=data[i]; k++; }

		for(int i=0; i<k; i++) {	
			eccModelTable table = new eccModelTable();
			table.setFgCode(udata[i]);		//System.out.println("fg : " + udata[i]);
			fg_list.add(table);
		}
	
		return fg_list;
	}

	//--------------------------------------------------------------------
	// ���躯�� ���� ��
	//		1.���� : ����� ��
	//		2.������� �߻��� ��ǥ�� ��ǰ : ����𵨵��� ��ǰ ��
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 *  ����[����] : �����[�񱳴��] �� 
	 **********************************************************************/
	public String compareFGList(String eco_no) throws Exception
	{
		//����
		String compare_list = "",where = "";
		

		//����[����] ���� ���ϱ�
		ArrayList target_list = new ArrayList();
		target_list = viewRevAllFGList(eco_no);
		int t_size = target_list.size();
		if(t_size == 0) return compare_list;
		String[] target = new String[t_size];
		
		eccModelTable table = new eccModelTable();
		Iterator target_iter = target_list.iterator();
		int n = 0;
		while(target_iter.hasNext()) {
			table = (eccModelTable)target_iter.next();
			target[n] = table.getFgCode();
			n++;
		}

		//�����[�񱳴��] ���� ���ϱ�
		query = "SELECT count(*) FROM ecc_model where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return compare_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_model","fg_code",where);

		//���ϱ�
		compare_list += "*** ������ ����[FG] ����Ʈ *** <br><br>";
		for(int i=0; i<t_size; i++) {
			int same_fg = 0;
			for(int j=0; j<cnt; j++) {
				if(target[i].equals(data[j])) same_fg++;
			}
			if(same_fg == 0) compare_list += target[i]+"<br>";
		}	

		return compare_list;
	}

	/**********************************************************************
	 *  �߻��� �����ǰ[����] : ����𵨵�[�񱳴��] �����ǰ �� 
	 **********************************************************************/
	public String compareItemList(String eco_no) throws Exception
	{
		//����
		String compare_list = "",where = "";
		String base_fcd="",base_gid="";					//�߻��� ��ǥFG�ڵ尪��

		//-------------------------------------
		// ���ظ�[FG]�� �����ǰ ã��
		//-------------------------------------
		//�߻��� ��ǥFG�ڵ尪,GID ���ϱ�
		where = "where eco_no = '"+eco_no+"'";
		String fg_code_list = chgDAO.getColumData("ecc_com","fg_code",where);
		StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
		if(list.hasMoreTokens()) base_fcd = list.nextToken();
		
		where = "where fg_code = '"+base_fcd+"'";
		base_gid = chgDAO.getColumData("mbom_master","pid",where);

		//��ǥFG�� ���� �����ǰ ã��
		ArrayList base_list = new ArrayList();
		base_list = chgDAO.getEccBomList(eco_no,base_gid);

		int t_size = base_list.size(); if(t_size == 0) t_size = 1;
		String[][] base = new String[t_size][4];
		for(int i=0; i<t_size; i++) for(int j=0; j<4; j++) base[i][j] = "";
	
		eccBomTable table = new eccBomTable();
		Iterator base_iter = base_list.iterator();
		int n = 0;
		while(base_iter.hasNext()) {
			table = (eccBomTable)base_iter.next();
			base[n][0] = table.getParentCode();
			base[n][1] = table.getChildCode();
			base[n][2] = table.getAdTag();
			base[n][3] = "";					//�񱳰�� �ӽ÷� ���
			n++;
		}

		//-------------------------------------
		// �񱳴���[FG]�� �����ǰ ã��
		//-------------------------------------
		//�����[�񱳴��] ���� ���ϱ�
		query = "SELECT count(*) FROM ecc_model where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return compare_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_model","gid",where);
		String pcd="", ccd="", tag="";
		for(int i=0; i<cnt; i++) {
			//���ظ𵨰� ������ �����̸� skip�ϱ�
			if(!data[i].equals(base_gid)) {
				//�񱳴���� fg_code ���ϱ�
				where = "where gid='"+data[i]+"'";
				String tg_fcd = chgDAO.getColumData("ecc_model","fg_code",where);
				compare_list += "*** ���ظ�[FG]: "+base_fcd+" �� �����[FG]: "+tg_fcd+" �����ǰ�� *** <br><br>"; 

				//�񱳴����� �˻��Ͽ� ����� ���
				ArrayList tg_list = new ArrayList();
				tg_list = chgDAO.getEccBomList(eco_no,data[i]);
				eccBomTable tg = new eccBomTable();
				Iterator tg_iter = tg_list.iterator();
				while(tg_iter.hasNext()) {
					tg = (eccBomTable)tg_iter.next();
					pcd = tg.getParentCode();
					ccd = tg.getChildCode();
					tag = tg.getAdTag();

					//���ظ𵨰� ���ϱ�
					int sm = 0;
					for(int k=0; k<t_size; k++) {
						if(base[k][0].equals(pcd) && base[k][1].equals(ccd) && base[k][2].equals(tag)) {
							base[k][3] = "1";
							sm++;
						} //if
					} //for
					//�˻��� ��󳻿��� ������ ��� : ������������ �ִ� ��ǰ����
					compare_list += "# �������[FG:"+tg_fcd+"]���� �ִ� ��ǰ<br>";
					if(sm == 0) compare_list += pcd + ": "+ccd+": "+tag+"<br>";
				} //while
				//�˻��� ���س����� ������ ��� : ���ظ��������� �ִ� ��ǰ����
				compare_list += "<br># ���ظ�[FG:"+base_fcd+"]���� �ִ� ��ǰ<br>";
				for(int c=0; c<t_size; c++) {
					if(!base[c][3].equals("1")) compare_list +=  base[c][0]+": "+base[c][1]+": "+base[c][2]+"<br>";
				}
				//base[n][3]�� clear�ϱ�
				for(int c=0; c<t_size; c++) base[c][3] = "";
				compare_list += "<br><br><br>";
			} //if
		} //for

		return compare_list;
	}
	
	//--------------------------------------------------------------------
	// �����[FG�ڵ�] ã��
	//		�ش�ǰ���ڵ�� FG�ڵ常 ã��
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 *  ��ü ����� �˻��ϱ� : ���躯���� ��ǰ���泻���� �̿��� ����𵨰˻��ϱ�
	 **********************************************************************/
	public ArrayList viewRevAllFGList(String eco_no) throws Exception
	{
		//����
		ArrayList table_list = new ArrayList();
		String where = "";

		//�ش� ECO_NO �� �̿��� ��ǰ���ڵ常 ã��
		query = "SELECT count(*) FROM ecc_bom where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_bom","parent_code",where);

		//�����[FG]����
		table_list = getRevFGList(data);
		return table_list;
	}
	/**********************************************************************
	 *  ���� �˻��ϱ� : ���躯���� ��ǰ���泻���� �̿��� ����𵨰˻��ϱ�
	 **********************************************************************/
	public ArrayList viewRevFGList(String gid,String eco_no) throws Exception
	{
		//����
		ArrayList table_list = new ArrayList();
		String where = "";

		//�ش� ECO_NO, GID�� �̿��� ��ǰ���ڵ常 ã��
		query = "SELECT count(*) FROM ecc_bom where gid='"+gid+"' and eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		where = "where gid='"+gid+"' and eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_bom","parent_code",where);

		//�����[FG]����
		table_list = getRevFGList(data);
		return table_list;
	}
	/**********************************************************************
	 *  FG���� �б� : �����ڵ�,���ڵ�,�𵨸�,FG�ڵ�,��ǰ�������
	 *  �ش�ǰ���ڵ��� �������� FG�ڵ常 ã�� : ���ι��� �� 'F'�� ���۵Ǵ� ��
	 *  sel_date : ��ȿ����
	 **********************************************************************/
	public ArrayList getRevFGList(String[] child_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();
		eccModelTable table = null;
		ArrayList table_list = new ArrayList();

		//�迭�� ������ 0 �̸� ���� 
		if(child_code.length == 0) return table_list;

		//�迭�� ���
		makeRevTextArray(child_code,sel_date);

		//�迭���� FG�ڵ常 ã��
		int f_cnt = 0;
		for(int i=0; i<an; i++) { if(item[i][2].indexOf(fg_head) != -1) f_cnt++; }
		if(f_cnt == 0) return table_list;		//FG�ڵ尡 ������ ����

		String[] data = new String[f_cnt];
		for(int i=0; i<f_cnt; i++) data[i]="";
		int n = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].indexOf(fg_head) != -1) { data[n] = item[i][2]; n++; }
		}

		//�����ϱ�
		sort.bubbleSortStringAsc(data);
		
		//�ߺ�FG�ڵ�� ""�� �Է��ϱ�
		int dn = 0;			//�ߺ��� FG�ڵ� ����
		for(int i=0; i<f_cnt-1; i++) if(data[i].equals(data[i+1])) { data[i] = ""; dn++; }

		//�ߺ��� ������ ���븸 �迭�� �ٽ� ��´�.
		int un = f_cnt - dn;				//unique�� ����
		String[] udata = new String[un];	//unique�� FG�ڵ� ��
		int k = 0;
		for(int i=0; i<f_cnt; i++) if(data[i].length() != 0) { udata[k]=data[i]; k++; }
			
		//ArrayList�� ���
		for(int i=0; i<un; i++) {
			//fg code�� ������ ������ ��������[mbom_master]���� ��������
			mbomMasterTable mst = new mbomMasterTable();
			mst = chgDAO.readMasterItem(udata[i]);

			//Array List�� ��� 
			table = new eccModelTable();
			table.setGid(mst.getPid());
			table.setModelCode(mst.getModelCode());
			table.setModelName(mst.getModelName());
			table.setFgCode(udata[i]);
			table.setMStatus(mst.getMStatus());
			
			//System.out.println(mst.getModelCode()+":"+mst.getModelName()+":"+udata[i]);
			table_list.add(table);
		}
		return table_list;
	}
	
	//--------------------------------------------------------------------
	//
	//		������, ������ BOM ARRAY LIST to ARRAY�� ���
	//		1.������ �迭�� ���
	//		2.������ �迭�� ��� : ������ Raw ������ �б�
	//		3.������ ������ �Ѱ��� ���ϱ�
	//---------------------------------------------------------------------
	/**********************************************************************
	 * ������ �迭�� ��� : MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TREE ����ü�踦 �迭�� ��� : �Ϻα��� ��ü
	 **********************************************************************/
	public void saveFrdStrArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//�迭�� ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an=0;
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

			//System.out.println(item[an][3]+":"+item[an][1]+":"+item[an][2]);
			an++;
		}
	}

	/**********************************************************************
	 * ������ �迭�� ��� : MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TEXT ����ü�踦 ��������� �迭�� �ٽ� ��� : ���ڰ��赵 �ٲ۴�.
	 **********************************************************************/
	public void makeRevTextArray(String[] child_code,String sel_date) throws Exception
	{
		//�迭�� ���
		saveRevStrArray(child_code,sel_date);

		//TREE������ ��������� �迭�� �ٽø����
		String[][] data = new String[an][7];
		for(int i=1; i<an; i++) for(int j=0; j<7; j++) data[i][j]="";
		//System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][1];			//Parent Code (��:���ڵ� -> ���ڵ��)
				data[k][2] = item[i][0];			//Child Code  (��:���ڵ� -> ���ڵ��)
				data[k][3] = item[i+1][3];			//Part Name
				data[k][4] = item[i+1][4];			//Part Spec
				data[k][5] = item[i+1][5];			//Location
				data[k][6] = item[i+1][6];			//OP Code

				//System.out.println(data[k][0]+":"+data[k][1]+":"+data[k][2]+":"+data[k][3]+":"+data[k][4]+":"+data[k][5]+":"+data[k][6]);
				n++;
				k++;
			} //if
		} //for

		//item�迭�� �ٽ� ���
		item = new String[k][7];
		an = k;
		for(int i=0; i<k; i++) {
			for(int j=0; j<7; j++) item[i][j] = data[i][j];		
			//System.out.println(item[i][0]+":"+item[i][1]+":"+item[i][2]);
		}
	}

	/**********************************************************************
	 * ������ RAW������ �迭�� ��� : MBOM_STR���� BOM TREE������ ���ϱ�
	 * ������ TREE ����ü�踦 �迭�� ���
	 **********************************************************************/
	private void saveRevStrArray(String[] child_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getReverseMultiBomItems(child_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][7];
		for(int i=0; i<cnt; i++) for(int j=0; j<7; j++) item[i][j]="0";

		//�迭�� ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getParentCode();
			item[an][1] = table.getChildCode();
			item[an][2] = table.getLevelNo();
			item[an][3] = table.getPartName();
			item[an][4] = table.getPartSpec();
			item[an][5] = "";	//location
			item[an][6] = table.getOpCode();

			//System.out.println(item[an][2]+":"+item[an][0]+":"+item[an][1]);
			an++;
		}
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * ��/������ TREE ����ü�踦 ��������� �迭����
	 **********************************************************************/
	public String getArrayCount() 
	{
		return Integer.toString(an);
	}

}
