package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomApprovalBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.db.BomApprovalDAO appDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
	private String query = "",update="";
	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����

	private String[][] plist = null;			//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;						//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;						//���� ������ ���ΰ��� 

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomApprovalBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		appDAO = new com.anbtech.bm.db.BomApprovalDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM����� �˻��׸� �˻��ϱ�
	//		1. Phantom Assy�� ���ɼ��� �ִ���.
	//		2. ȸ��Assy�� ��� Location No �ִ���, ������ �ߺ����� �˻��ϱ� 
	//---------------------------------------------------------------------
	/**********************************************************************
	 * Phantom Assy�� �ִ��� �Ǵ��ϱ� : ��, phantom_assy�� ǥ�õȰ��� ����
	 * [��ǰ���ڵ尡 �������� ��ǰ�� �ڵ�� ��ϵǴ� ���]
	 **********************************************************************/
	public String checkPhantomAssy(String gid) throws Exception
	{
		
		String rtn = "";			//���ϰ�
		String where="",model_code="";

		//���ڵ� ã��
		where = "where pid='"+gid+"'";
		model_code = modDAO.getColumData("MBOM_MASTER","model_code",where);

		//�����ϵ� BOM LIST
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getForwardItems(gid,"0",model_code);
		int item_len = item_list.size();		

		//�迭�� ����� clear�Ѵ�.
		String[][] list = new String[item_len][4];
		for(int i=0; i<item_len; i++) for(int j=0; j<4; j++) list[i][j] = "";

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			list[n][0] = table.getLevelNo();
			list[n][1] = table.getParentCode();
			list[n][2] = table.getChildCode();
			list[n][3] = table.getAssyDup();
			
			//Phantom Assy�� �����Ȱ��� ���ܽ�Ŵ
			if(list[n][3].equals("D")) list[n][2]="";	

			n++;
		}

		//��ǰ���ڵ尡 ��ǰ���ڵ忡 2���̻� ������ 1���� ������ �������� phantom assy�ڵ���
		for(int i=0; i<item_len; i++) {
			int dn = 0;					//�ߺ��Ǵ� �� ����
			String sm = "";				//ù��° ������
			for(int j=0; j<item_len; j++) {
				if(list[i][1].equals(list[j][2])) {
					dn++;
					if(dn == 1)		sm = list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
					if(dn == 2)		rtn += sm+list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
					else if(dn > 2) rtn += list[j][0]+": "+list[j][1]+"/[*"+list[j][2]+"], ";
				}
			}
		}
		if(rtn.length() != 0) {
			String msg = "*** "+rtn;
			rtn = "<�˻�:Assy�ڵ尡 �ߺ�[Phantom Assy����]���� ��ǰ������ �Էµ� ���>||";
			rtn += msg;
			rtn += "||[*ǥ�ô� Assy�ڵ�� Phantom Assy�� ��츦 �����ϰ�� ";
			rtn += "��ǰ������ ���� ����� �� �����ϴ�. | Phantom Assy�� �ǴܵǴ� �ڵ带";
			rtn += "������ �ٽ� �Է��ϸ� Phantom Assy�� �ڵ� ǥ�õ˴ϴ�.]||";
		}
		return rtn;
	}

	/**********************************************************************
	 * Location �˻��ϱ�
	 * 1. Location ���� 2. �ߺ��˻�
	 **********************************************************************/
	public String checkLocation(String gid) throws Exception
	{
		
		String rtn = "";			//���ϰ�
		
		//�����ϵ� ȸ�θ� ����� BOM LIST
		ArrayList item_list = new ArrayList();
		item_list = modDAO.getElectronicItems(gid);
		int item_len = item_list.size();		

		//�迭�� ����� clear�Ѵ�.
		String[][] list = new String[item_len][4];
		for(int i=0; i<item_len; i++) for(int j=0; j<4; j++) list[i][j] = "";

		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			list[n][0] = table.getLevelNo();
			list[n][1] = table.getParentCode();
			list[n][2] = table.getChildCode();
			list[n][3] = table.getLocation();
			list[n][3] = list[n][3].trim();
			n++;
		}
		//�˻�1. Location No ���� �˻��ϱ�
		String loc_yn = "";
		for(int i=0; i<item_len; i++) {
			if(list[i][3].length() == 0) {
				loc_yn += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+"], <br>";
			}
		}
		if(loc_yn.length() != 0) {
			rtn = "<�˻�1: Location �Է� ���� �˻� ��� [����:��ǰ���ڵ�:��ǰ���ڵ�]>||";
			rtn += loc_yn+"||";
		}

		//�˻�2. Location No �ߺ� �˻��ϱ�
		String loc_dup = "";
		int dup = 1;
		for(int i=0; i<item_len-1; i++) {
			for(int j=i+1; j<item_len; j++) {
				if(list[i][3].equals(list[j][3]) && (list[i][3].length() != 0)) {
					if(dup%2 == 0) loc_dup += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+" : "+list[i][3]+"], <br>";
					else loc_dup += list[i][0]+": "+list[i][1]+"/[*"+list[i][2]+" : "+list[i][3]+"], ";
					dup++;
					break;
				}
			}
		}
		if(loc_dup.length() != 0) {
			rtn += "<�˻�2: Location �ߺ��Է� �˻� ��� [����:��ǰ���ڵ�:��ǰ���ڵ�]>||";
			rtn += loc_dup;
		}
		return rtn;
	}

	/**********************************************************************
	 * BOM���� �˻��ϱ�
	 * 1������ BOM�� �����Ǿ����� �˻��ϱ�
	 **********************************************************************/
	public String checkAssySet(String gid) throws Exception
	{
		String data = "";
		int cnt = 0;

		//1������ ASSY�ڵ常 ã��
		ArrayList item_list = new ArrayList();
		item_list = appDAO.getLevelOneAssy(gid);

		//�˻��ϱ�
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();

			query = "select count(*) from mbom_str where gid='"+gid+"' and parent_code='"+table.getChildCode()+"'";
			cnt = modDAO.getTotalCount(query);
			if(cnt == 0) data += table.getChildCode()+", ";
		}
		if(data.length() != 0) data += " �� �̱��� ASSY SET �ڵ��Դϴ�. BOM�� ������ �����Ͻʽÿ�.";
		
		return data;
	}
}