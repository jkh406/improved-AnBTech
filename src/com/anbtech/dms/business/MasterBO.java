package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.admin.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class MasterBO{

	private Connection con;

	public MasterBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * ���������,������,���� ���� �� �� master_data ������ ������
	 * MasterTable ��� �ִ´�.
	 *****************************************************************/
	public MasterTable getWrite_form(String tablename,String no,String mode,String category,String why_revision) throws Exception{

		int m_id;				// ������ȣ
		String doc_no;			// ������ȣ
		String category_id;		// ī�װ� �ڵ�
		String where_category;	// ���� ī�װ��� �з� ǥ��
		String data_id;			// ���� ������ȣ(���ʹ����� ��)
		String subject;			// ���� ����
		String writer;			// �ۼ���(��������)
		String written_day;		// �ۼ���(��������)
		String register;		// �����(��������)
		String register_day;	// �����(��������)
		String search_keyword;	// �˻���
		String hit;				// ����Ƚ��
		String curr_version;	// �������
		String last_version;	// ��������
		String stat;			// ������ ���� ����


		MasterTable master = new MasterTable();
		MasterDAO masterDAO = new MasterDAO(con);		
		com.anbtech.pjt.db.pjtDocumentDAO pjt = new com.anbtech.pjt.db.pjtDocumentDAO(con);
		com.anbtech.gm.entity.GoodsInfoTable goods = new com.anbtech.gm.entity.GoodsInfoTable();
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

		if ("write".equals(mode)){ //�űԹ����� ���
			curr_version  = "1.0";
			master.setLastVersion(curr_version);
			master.setModelCode("");
			master.setModelName("");
			master.setPjtCode("");
			master.setPjtName("");
			master.setNodeCode("");
			master.setNodeName("");	
		}else if (("modify".equals(mode) || "modify_a".equals(mode) || "revision".equals(mode)) && no != null){ // ������ ���
			master = masterDAO.getMasterData(tablename, no);

			String pjt_name = pjt.getProjectName(master.getPjtCode());
			String node_name = pjt.getDocumentName(master.getPjtCode(),master.getNodeCode());
			goods = goodsDAO.getGoodsInfoByCode(master.getModelCode());
			String model_name = goods.getGoodsName();

			master.setPjtName(pjt_name);
			master.setNodeName(node_name);
			master.setModelName(model_name);

		}
		// �Ѿ�� ī�װ� �ڵ带 ������ ���� ī�װ��� ���� �з��� ��Ÿ���� ���ڿ��� �����.
		String where = "";
		com.anbtech.dms.admin.makeDocCategory view_category = new com.anbtech.dms.admin.makeDocCategory();
		where_category = view_category.viewCategory(Integer.parseInt(category),where);

		master.setWhereCategory(where_category);

		return master;
	}

	/*****************************************************************
	 * no�� �ش��ϴ� ���̺� ������ �����ͼ� ������·� ��ȯ��Ų��.
	 *****************************************************************/
	public MasterTable getData(String tablename,String no) throws Exception{

		int m_id;				// ������ȣ
		String doc_no;			// ������ȣ
		String category_id;		// ī�װ� �ڵ�
		String where_category;	// ���� ī�װ��� �з� ǥ��
		String data_id;			// ���� ������ȣ(���ʹ����� ��)
		String subject;			// ���� ����
		String writer;			// �ۼ���(��������)
		String written_day;		// �ۼ���(��������)
		String register;		// �����(��������)
		String register_day;	// �����(��������)
		String search_keyword;	// �˻���
		String hit;				// ����Ƚ��
		String curr_version;	// �������
		String last_version;	// ��������
		String stat;			// ������ ���� ����

		MasterTable master = new MasterTable();
		MasterDAO masterDAO = new MasterDAO(con);
		com.anbtech.pjt.db.pjtDocumentDAO pjt = new com.anbtech.pjt.db.pjtDocumentDAO(con);
		com.anbtech.gm.entity.GoodsInfoTable goods = new com.anbtech.gm.entity.GoodsInfoTable();
		com.anbtech.gm.db.GoodsInfoDAO goodsDAO = new com.anbtech.gm.db.GoodsInfoDAO(con);

		master = masterDAO.getMasterData(tablename, no);

		//ī�װ� �з� ǥ�� ���ڿ� ���� �� ����
		String category = master.getCategoryId();
		String where = "";
		com.anbtech.dms.admin.makeDocCategory view_category = new com.anbtech.dms.admin.makeDocCategory();
		where_category = view_category.viewCategory(Integer.parseInt(category),where);
		master.setWhereCategory(where_category);

		String pjt_name = pjt.getProjectName(master.getPjtCode());
		String node_name = pjt.getDocumentName(master.getPjtCode(),master.getNodeCode());
		goods = goodsDAO.getGoodsInfoByCode(master.getModelCode());
		String model_name = goods.getGoodsName();

		master.setPjtName(pjt_name);
		master.setNodeName(node_name);
		master.setModelName(model_name);

		return master;
	} //getData()

	/*****************************************************************
	 * �ֽŹ����� ������ ���� �������� �˻� ������ �����.
	 * EDMS ��ü�� �˻��ÿ� ���ȴ�. ����, �󼼰˻��� ���� �ʴ´�.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		//�˻����ǿ� �°� where������ �����Ѵ�.
		String where = "", where_cat = "", where_and = "", where_sea = "";
		if(category.length() > 0) where_cat = "category_id like '"+category+"%'";
		if (searchword.length() > 0){
			if ("subject".equals(searchscope)){
				where_sea = "( subject like '%" +  searchword + "%' )";
			}
			else if ("writer_s".equals(searchscope)){
				where_sea = "( writer_s like '%" +  searchword + "%' )";
			}
			else if ("register_s".equals(searchscope)){
				where_sea = "( register_s like '%" +  searchword + "%' )";
			}
			else if ("doc_no".equals(searchscope)){
				where_sea = "( doc_no like '%" +  searchword + "%' )";
			}
			else if ("model_code".equals(searchscope)){
				where_sea = "( model_code like '" +  searchword + "%' )";
			}
		}
		if(category.length() > 0 && searchword.length() > 0) where_and = " and ";
		if(category.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;

		String where_mode = "";

		//���簡 �Ϸ�� ������ ���
		if(mode.equals("list")) where_mode = " and stat = '5'";

		//�������� ����, ���������� Ȯ������ ����
		else if(mode.equals("processing")) where_mode = " and stat = '3'";

		where = where + where_mode;
		return where;
	}

	/*****************************************************************
	 * �˻����� ���ڿ��� ���� �Ѿ���� ����� where ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String category,String where_str) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if(category.length() > 0) where_cat = "category_id like '"+category+"%' ";
		if (where_str.length() > 0){
			//where_str = "and|subject|����,or|writer|�ۼ���, ..... ���·� �Ѿ��.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][3];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<3; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			for(int i = 0; i< scope_count; i++){
				if(search[i][1].equals("register_day")){
					//s_day = 2003070120030830 ���� �Ѿ�´�. 2003-07-01 ~ 2003-08-30 ������ �ǹ���.

					String s_day = search[i][2].substring(0,8);		// ������ 
					String e_day = search[i][2].substring(8,16);	// ������
					where_sea += search[i][0] + " (register_day_s >= '" + s_day + "' and register_day_s <= '" + e_day +"') ";
				}else{
					where_sea += search[i][0] + " (" + search[i][1] + " like '%" + search[i][2] + "%') ";
				}
			}

		}
		
		where = " WHERE " + where_cat + where_sea;

		//���簡 �Ϸ�� ������ ���
		if(mode.equals("list")) where_mode = " and stat = '5'";
		//�������� ����, ���������� Ȯ������ ����
		else if(mode.equals("processing")) where_mode = " and stat = '3'";

		where = where + where_mode;

		return where;
	}


	/*****************************************************************
	 * �ڽ��� ����� ���� �� ��ŵǱ� ���� ����(stat == 1) �� 
	 * ���ڰ��翡�� �ݷ��� ������ �����´�.
	 * ����ϱ� ���� ���� �Ǵ� ������ �� �ֵ��� �ϱ� ����.
	 *****************************************************************/
	public String getWhere(String login_id) throws Exception{

		String where = " WHERE writer = '" + login_id + "' and stat in ('1','4') ";
		return where;
	}

	/*****************************************************************
	 * �����ڵ带 �޾Ƽ� ���� ���ڿ��� ����� �ش�.
	 *****************************************************************/
	public String getStatus(String stat) throws Exception{

		String status = "";

		if(stat.equals("1"))	  status = "��ϴ��";	// ��� ��
		else if(stat.equals("2")) status = "������";	// ���� ������
		else if(stat.equals("3")) status = "���οϷ�";	// ���� ���� �Ϸ�,���� ������ Ȯ�� ���
		else if(stat.equals("4")) status = "�ݷ�";		// ���ڰ��翡�� �ݷ��� ����
		else if(stat.equals("5")) status = "����";		// ���� ������ Ȯ�α��� ���� ����
		else if(stat.equals("6")) status = "������";
		else if(stat.equals("7")) status = "Reserved";
		else if(stat.equals("8")) status = "Reserved";
		else if(stat.equals("9")) status = "������";	// ���� ������ �Ϸ�� �Ŀ� ���� ó���� ����

		return status;
	}

}