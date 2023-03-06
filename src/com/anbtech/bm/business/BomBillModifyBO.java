package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomBillModifyBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomBillModifyBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		BOM ����,���̱� ��ɿ� ���� �޼ҵ� ����
	//			BOM�� ���� ������ ���� ���� -> ���̱� ������ ����
	//			�𵨴��� ����, Assy���� ���� ����
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * BOM�� �����ϱ� : �Ϻα��� ��ü
	 * gid : group�����ڵ�, level_no : 0, parent_code : 0, url : ��ũ
	 **********************************************************************/
	public ArrayList getCpyStrList(String gid,String level_no,String parent_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();		//�������ڷ� BOM������
		ArrayList item_list = new ArrayList();
//		item_list = modDAO.getForwardItems(gid,level_no,parent_code);		//BOM������ ����
		item_list = modDAO.getCopyForwardItems(gid,level_no,parent_code,sel_date); //BOM�������� ����

/*		//����غ���
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * MBOM_STR���� BOM TREE������ ���ϱ�
	 * BOM�� �����Ͽ� ���̱�
	 * gid : group�����ڵ�, level_no : 0, parent_code : 0, url : ��ũ
	 **********************************************************************/
	public String pastePartList(ArrayList paste_list) throws Exception
	{
		String input="",data="",pid="";
		String pcd="",ccd="",gid="";				//�˻���� ��ǰ��,��ǰ���ڵ�,�׷��ڵ�
		int lvn=0;									//�˻���� ������ȣ
		String[] part = new String[6];				//��ǰ������ �������
		for(int i=0; i<6; i++) part[i] = "";		//�ʱ�ȭ
		String add_date = anbdt.getDateNoformat();	//�����

		com.anbtech.bm.entity.mbomStrTable chk = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable assy = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable copy = new com.anbtech.bm.entity.mbomStrTable();
		com.anbtech.bm.entity.mbomStrTable paste = new com.anbtech.bm.entity.mbomStrTable();

		//�˻縦 ���� ù������ ��ǰ���ڵ�,��ǰ���ڵ�,������ȣ �б�
		// ù������ ���̱��� ���� ������, �������� ����[������]�� ������
		Iterator chk_iter = paste_list.iterator();
		if(chk_iter.hasNext()) {
			chk = (mbomStrTable)chk_iter.next();
			gid = chk.getGid();
			pcd = chk.getParentCode();
			ccd = chk.getChildCode();
			lvn = Integer.parseInt(chk.getLevelNo());
			//if(pcd.equals("0")) pcd = ccd;	//�𵨴����� �����Ҷ�
		}

		//�˻��ϱ� : �������ø��������� ����/���̱� �� �� ����
		String where = "where pid='"+gid+"'";
		String bom_status = modDAO.getColumData("mbom_master","bom_status",where);
		if(bom_status.equals("2")) {
			data = "�������ø� ���� �����Դϴ�. ����Assy�ڵ�� ����� �����Ͻʽÿ�.";
			return data;
		}

		//�˻��ϱ� : ������ ��ǰ���ڵ�� ������ ��ǰ���ڵ尡 �ߺ��Ǹ� ����/���̱� �� �� ����
		String was_pcd = "";		//������ϵ� ��ǰ���ڵ�
		ArrayList assy_list = new ArrayList();
		assy_list = modDAO.getAssyListCP(gid);
		Iterator assy_iter = assy_list.iterator();			//������ Assy �ڵ�
		while(assy_iter.hasNext()) {
			assy = (mbomStrTable)assy_iter.next(); 

			was_pcd = assy.getParentCode();
			//��ǰ���ڵ� ���ϱ�
			Iterator copy_iter = paste_list.iterator();		//������ ��ǰ����
			int fno = 0;									//ù��° ������ ���̱��� ��/��ǰ���ڵ�� ����
			while(copy_iter.hasNext()) {
				copy = (mbomStrTable)copy_iter.next(); 
				if(was_pcd.equals(copy.getParentCode()) && (fno != 0)) {
					data += copy.getParentCode()+"/"+copy.getChildCode()+", ";
				}
				fno++;
			}
		}
		if(data.length() != 0) {
			String msg = data;
			data = " [��ǰ���ڵ� �ߺ� ��� LIST ERROR] " + msg + " �� �̹� ��ϵǾ� �ֽ��ϴ�. Ȯ���� �ٽ� �����Ͻʽÿ�.";
			return data;
		}

		//��ǰ�����Ϳ� ��ϵ� ��ǰ���� �˻��Ѵ�. (���� ��ǰ������ ��� ���)
		//����ϱ� ��,ù��ǰ���ڵ�� ������ ǰ���ڵ�� �ٲ��ش�.
		int pst_cnt = paste_list.size();			//��ü����
		String[] inDB = new String[pst_cnt];		//�Է¹��� �迭�� ���
		int inDB_n = 0;								//���尹��

		int first_line = 0;			//ù���� skip����
		int level_no = lvn;			//������ȣ 
		int before_lvn = lvn;		//�ٷ����� ���� ������ȣ
		int current_lvn = 0;		//�������� ������ȣ
		String before_pcd = "";		//�ٷ����� ���� ��ǰ���ڵ�
		String current_pcd = "";	//�������� ��ǰ�� �ڵ�
		String parent_code = "";	//��ǰ���ڵ�
		Iterator part_iter = paste_list.iterator();
		while(part_iter.hasNext()) {
			paste = (mbomStrTable)part_iter.next();

			//ù���� ���̱�� skip[���̱��� �ֻ�ܰ� ����] �Ѵ�. �� �̾��ֱ��� pid���� �����ش�.
			if(first_line == 0) { 
				pid = paste.getPid();
			}
			//ù���� ���̱��� ��������� skip�Ѵ�.
			else {
				//������ȣ
				current_lvn = Integer.parseInt(paste.getLevelNo());
				if(first_line == 1) level_no++;
				else if(before_lvn < current_lvn) level_no++;
				else if(before_lvn > current_lvn) level_no--;
				before_lvn = current_lvn;						//������ȣ ����

				//���̱� ASSY�ڵ�� ������ ù��° ��ǰ���ڵ带 ��/�� �����ϱ�
				if(first_line == 1) {
					parent_code = ccd;
					String child_code = paste.getParentCode();

					//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,6op_code
					part = modDAO.getComponentInfo(child_code);
					if(part[1].length() == 0) data += child_code+", ";

					//����� �������� �����
					input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
					input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
					input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
					input += pid+"','"+paste.getGid()+"','"+parent_code+"','";
					input += child_code+"','"+level_no+"','"+part[0]+"','"+part[1]+"','";
					input += paste.getLocation()+"','"+part[6]+"','"+paste.getQtyUnit()+"','";
					input += paste.getQty()+"','"+part[2]+"','"+part[3]+"','"+"��"+"','"+"0"+"','";
					input += add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
					input += paste.getAssyDup()+"','"+part[5]+"')";
					
					//�迭�� ��´�.
					inDB[inDB_n] = input;
					inDB_n++;
					level_no++;
				}

				//ccd�� ��ǰ���ڵ�� �Ұ��(������ ù��° ��ǰ���� ���̱��� ASSY�� ��ġ�� ��
				//��ǰ�� �ڵ�
				//current_pcd = paste.getParentCode();
				//if(first_line == 1) { parent_code = ccd;	before_pcd = current_pcd; }
				//else if(before_pcd.equals(current_pcd)) parent_code = ccd;
				//else parent_code = current_pcd;

				//��ǰ�� �ڵ�
				parent_code = paste.getParentCode();

				//0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type
				part = modDAO.getComponentInfo(paste.getChildCode());
				if(part[1].length() == 0) data += paste.getChildCode()+", ";
				
				//����� �������� �����
				input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
				input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
				input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
				input += paste.getPid()+"','"+paste.getGid()+"','"+parent_code+"','";
				input += paste.getChildCode()+"','"+level_no+"','"+part[0]+"','"+part[1]+"','";
				input += paste.getLocation()+"','"+paste.getOpCode()+"','"+paste.getQtyUnit()+"','";
				input += paste.getQty()+"','"+part[2]+"','"+part[3]+"','"+"��"+"','"+"0"+"','";
				input += add_date+"','"+""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
				input += paste.getAssyDup()+"','"+part[5]+"')";
				
				//�迭�� ��´�.
				inDB[inDB_n] = input;
				inDB_n++;
			} //ù���� skip
			first_line++;


		} //while
		//��� �˻�
		if(data.length() != 0) {
			data += " �� ��ǰ�����Ϳ� ���� ��ǰ�Դϴ�. ���̱⸦ ���� �� �� �����ϴ�.";
			return data;
		}

		//DB�� ���
		for(int i=0; i<inDB_n; i++) modDAO.executeUpdate(inDB[i]);

		//BOM���¸� MBOM_MASTER�� �����ϱ� (bom_status = '3' : BOM �����)
		where = "where pid='"+gid+"'";
		bom_status = modDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(bom_status.equals("1")) {
				String update = "UPDATE mbom_master SET bom_status='3' where pid = '"+gid+"'";
				modDAO.executeUpdate(update);
		}
		
		return data;
	}

	/**********************************************************************
	 * BOM�� �����ϱⰡ �������� �Ǵ��ϱ� : �������� ���� ���簡 �Ұ��ϵ���
	 * gid : group�����ڵ�
	 **********************************************************************/
	public String checkCBomStatus(String gid) throws Exception
	{
		String input="",data="",where="",fg_code="";
		String eco_no="",ecc_status="",tmp="";

		//FG CODE ���ϱ�
		where = "where pid='"+gid+"'";
		fg_code = modDAO.getColumData("mbom_master","fg_code",where);

		//-------------------------------------------------------
		//FG CODE�� �ش�Ǵ� ������ ���躯�� ���������� �Ǵ��ϱ�
		//-------------------------------------------------------
		//FG_CODE�� �ش�Ǵ� ���躯���ȣ ã�� (��,�������̰ų� �Ϸ��� �� ����) : �ټ��� �޸��� ���е�
		where = "where fg_code='"+fg_code+"'";
		eco_no = modDAO.getColumAllData("ecc_model","eco_no",where);

		//�˻�2. ���躯���ȣ�� �ش�Ǵ� ������ ���������� �Ǵ��ϱ�
		//�������̸� ���躯���ȣ ����
		StringTokenizer list = new StringTokenizer(eco_no,",");
		while(list.hasMoreTokens()) {
			tmp = list.nextToken();
			where = "where eco_no='"+tmp+"'";
			ecc_status = modDAO.getColumData("ecc_com","ecc_status",where);
			if(ecc_status.length() != 0 & !ecc_status.equals("9")){		//������������
				data += tmp+",";
			}
		}

		//�������ϰ� �Ǵ��ϱ�
		if(data.length() != 0) data += " �� ���躯�����Դϴ�.";

		return data;
	}
}
