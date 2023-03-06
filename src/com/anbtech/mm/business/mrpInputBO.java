package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mrpInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.SortArray sortA = new com.anbtech.util.SortArray();		//����
	private com.anbtech.mm.db.mrpModifyDAO mrpDAO = null;	
	private com.anbtech.pu.db.PurchaseMgrDAO purchaseDAO = null;						//���Ű���
	private String query = "";

	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����
	private String purchase_type = "4";			//����ǰ (item_type)
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mrpInputBO(Connection con) 
	{
		this.con = con;
		mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);
		purchaseDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		���°����ϱ� 
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  MRP MASTER�� ���°����ϱ�
	// cur_mrp_status : ������ ������� , mrp_status : �ٲ� �������
	// tag : A(�ϰ�),I(����),�׿� skip
	// pid : �ϰ��϶��� mrp master, �����϶��� mrp item
	//*******************************************************************/	
	public String setMrpStatus(String pid,String cur_mrp_status,String mrp_status,String tag) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="",mps_no="",mrp_no="";
		String factory_no="",item_code="",delete="",order_status="",pu_req_no="";

		//MPS MASTE���� �ش����� ���ϱ�
		com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
		mrpT = mrpDAO.readMrpMasterItem(pid);
		mps_no = mrpT.getMpsNo();
		mrp_no = mrpT.getMrpNo();
		factory_no = mrpT.getFactoryNo();
		item_code = mrpT.getItemCode();
		pu_req_no = mrpT.getPuReqNo();

		//---------------------------------------------
		// ���� TABLE�� ���� �����ϱ�
		//---------------------------------------------
		//���� �ݷ�
		if(mrp_status.equals("0")) {
			update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
			mrpDAO.executeUpdate(update);

			deletePurchaseItem(pid);									//���Ű��� : ��ϳ��� �ϰ������ϱ�
			data = "MRS����ݷ� �Ǿ����ϴ�.";
		}
		//MRP�������
		else if(mrp_status.equals("1")) {
			if(cur_mrp_status.equals("2")) {			//����� ���(��������)	
					update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);

					data = "MRP������û�� ����Ǿ����ϴ�.";	
			} else if(cur_mrp_status.equals("3")) {	
					//mfg���� �����Ͽ� ��ϵǾ����� MRPȮ����� �Ұ���.
					where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					order_status = mrpDAO.getColumData("MFG_MASTER","order_status",where);

					//�����ŵǾ����� MRPȮ����� �Ұ���.
					where = "where plid like '"+pu_req_no+"%'";
					String app_state = mrpDAO.getColumData("APP_MASTER","app_state",where);
					if(app_state.length() == 0) order_status="";		//����̻��
					else if(app_state.equals("APR")) order_status="";	//�ݷ�
					else order_status="31";
				
					//���������̰ų� ����Ϸ�
					if(order_status.equals("31")) {
						data = "���ڰ��� ó���� ���� ��� �� �� �����ϴ�.";
					}
					//����̻�� && �������� �̵�� ���´� ��� ����
					else if(order_status.length() == 0) {
						update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
						mrpDAO.executeUpdate(update);

						deletePurchaseItem(pid);							//���ŵ�ϳ��� �ϰ������ϱ� : tag = 'A'
						data = "MRPȮ���� ��ҵǾ����ϴ�.";
					} 
					//�׿ܴ� ��� �Ұ�
					else {
						data = "������������ ó���� ���� ��� �� �� �����ϴ�.";
					}

			} 
		} 
		//����� -> MRP���ο�û (2004.6.4)
		else if(mrp_status.equals("2")) {
			if(cur_mrp_status.equals("1")) {		//����� (��������)
				update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
				mrpDAO.executeUpdate(update);

				data = reserveStockMrpItemList(factory_no,mrp_no,item_code);	//��ǰ���� �� ����update
				if(data.length() == 0) data = "MRP����Ȯ���� ����Ǿ����ϴ�.";	//����ǰ������ ����Ǿ����ϴ�.
			} else if(cur_mrp_status.equals("3")) {	//MRP��� : (���ŵ�ϻ���,mrp_master�Ǳ��ſ�û����)
				update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
				mrpDAO.executeUpdate(update);

				deletePurchaseItem(pid);	
				data = "MRPȮ���� ��ҵǾ����ϴ�.";
			}
		} 
		//MRPȮ�� : ����TABLE�� ������ �Ѱ� ����μ����� ��ü ���Ű���
		else if(mrp_status.equals("3")) {
				if(cur_mrp_status.equals("31")) {	//���ڰ��� ���ſ�û���ν�  
					update = "UPDATE mrp_master SET mrp_status='4' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);
				} else {							//MRPȮ���̸� (�ۼ�:1����,�ݷ�:0���� �ü� ����)
					update = "UPDATE mrp_master SET mrp_status='"+mrp_status+"' where pid='"+pid+"'";
					mrpDAO.executeUpdate(update);
					
					savePurchaseItem(pid);			//���ź�ǰ ���Ÿ��� �����ϱ�				
					data = "MRPȮ���� ����Ǿ����ϴ�.";
				}
		} 
		//������� : ���źμ��� ���۵� ����
		else if(mrp_status.equals("4")) {
				update = "UPDATE mps_master SET mps_status='5' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				mrpDAO.executeUpdate(update);
				data = "MRS������� �Ǿ����ϴ�.";

				//�ӽ�BOM�� ��� ���� TABLE��� ����(MPS_MASTER,MRP_MASTER,MRP_ITEM)
				//BOM_ITEM�� BOM�� ���� ��Ͻ� ������ �ٽ��Է������� ���ܽ�Ŵ.
				where = "where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				String purpose = mrpDAO.getColumData("MPS_MASTER","purpose",where);
				if(purpose.equals("1")) {		//�ӽ�BOM��
					delete = "DELETE FROM mps_master WHERE mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);

					delete = "DELETE FROM mrp_master WHERE mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);

					delete = "DELETE FROM mrp_item WHERE mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
					mrpDAO.executeUpdate(delete);
				}

		} 

		return data;
	}
	//--------------------------------------------------------------------
	//
	//		ǰ��ҿ䷮����(MRP MASTER) �� ���� �޼ҵ� ����
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MRP MASTER�� ������ �Է��ϱ�
	//*******************************************************************/	
	public String insertMrp(String mps_no,String mrp_start_date,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String p_count,String plan_date,
		String item_unit,String factory_no,String factory_name,String reg_date,String reg_id,String reg_name,
		String pu_dev_date,String stock_link,String pjt_code,String pjt_name) throws Exception
	{
		String input="",update="",data="",where="",bom_status="",gid="",mrp_no="";
		String[] div_info = new String[2];		//������ڵ�,����θ�
		String mrp_status = "";
		
		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) {
			data = "BOM������ �ȵ� ���Դϴ�. ���� BOM�� ������ �ҿ䷮������ �����Ͻʽÿ�.";
			return data;
		}

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt = mrpDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "ǰ���ȣ�� FG�ڵ�� ������ BOM���� ��ǰ �Ǵ� ����ǰ�� ����� �� �ֽ��ϴ�.";
			return data;
		}

		//MPS������ȣ ���ϱ�
		mrp_no = mrpDAO.getMrpNo(factory_no);

		//������ڵ� ����θ� ���ϱ�
		div_info = mrpDAO.getDivInfo(reg_id);

		//mrp_status�� ���¸� ���ϱ�
		mrp_status = "1";				//�ۼ����·�
		
		//MRP MASTER�� �Է��ϱ� 
		String pid = anbdt.getID();
		input = "INSERT INTO mrp_master (pid,mps_no,mrp_no,mrp_start_date,mrp_end_date,model_code,model_name,";
		input += "fg_code,item_code,item_name,item_spec,p_count,plan_date,item_unit,mrp_status,factory_no,";
		input += "factory_name,reg_date,app_date,app_id,reg_div_code,reg_div_name,reg_id,reg_name,";
		input += "app_no,pu_dev_date,pu_req_no,stock_link,pjt_code,pjt_name) values('";
		input += pid+"','"+mps_no+"','"+mrp_no+"','"+mrp_start_date+"','"+""+"','"+model_code+"','"+model_name+"','";
		input += fg_code+"','"+item_code+"','"+item_name+"','"+item_spec+"','"+p_count+"','"+plan_date+"','"+item_unit+"','"+mrp_status+"','";
		input += factory_no+"','"+factory_name+"','"+reg_date+"','"+""+"','"+""+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+reg_id+"','"+reg_name+"','"+""+"','"+pu_dev_date+"','"+""+"','"+stock_link+"','";
		input += pjt_code+"','"+pjt_name+"')";
		//System.out.println("input : " + input);
		mrpDAO.executeUpdate(input);

		//MRP ITEM�� �ҿ䷮ �ϰ��Է��ϱ�
		insertMrpItem(pid,mrp_no,fg_code,item_code,mrp_start_date,p_count,factory_no,factory_name,pu_dev_date,stock_link);

		//MPS MASTER�� ������� �ٲ��ֱ�
		update = "UPDATE mps_master SET mps_status='4' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrpDAO.executeUpdate(update);
		
		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  MRP MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String updateMrp(String pid,String mrp_start_date,String reg_date,String pu_dev_date,
		String stock_link,String pjt_code,String pjt_name) throws Exception
	{
		String data="",where="",mrp_status="",update="";

		//������°� �ۼ����϶��� ������ ������
		where = "where pid='"+pid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);
		if(!mrp_status.equals("1")) {
			data = "�ۼ����϶��� ������ �����մϴ�.";
			return data;
		}
		
		//MBOM_STR�� �����ϱ�
		update = "UPDATE mrp_master SET mrp_start_date='"+mrp_start_date+"',reg_date='"+reg_date;
		update += "',pu_dev_date='"+pu_dev_date+"',stock_link='"+stock_link;
		update += "',pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// MRP MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String deleteMrp(String pid,String mps_no,String mrp_no,String mrp_status,String factory_no) throws Exception
	{
		String delete = "",update="",data="";
		
		//������� �˻�
		if(!mrp_status.equals("0") && !mrp_status.equals("1")) {
			data = "�ۼ������϶��� ������ �����մϴ�.";
			return data;
		} 

		//MRP MASTER ���� �����ϱ�
		delete = "DELETE FROM mrp_master WHERE pid='"+pid+"'";
		mrpDAO.executeUpdate(delete);

		//MRP ITEM ���� �����ϱ�
		delete = "DELETE FROM mrp_item WHERE mrp_no='"+mrp_no+"'";
		mrpDAO.executeUpdate(delete);

		//MPS MASTER�� ������� �ٲ��ֱ�
		update = "UPDATE mps_master SET mps_status='3' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
		mrpDAO.executeUpdate(update);
		
		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		�Ϻα��� ǰ��ҿ䷮����(MRP ITEM) �� ���� �޼ҵ� ����
	//		[�ϰ����, ��������, MRP ITEM��ϳ��� ����,MRP ITEM�� ASSY LIST] 
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MPS������ MRP ITEM�� �ҿ䷮ �ϰ��Է��ϱ�
	// [MRP MASTER��Ͻ� ���ÿ� �����]
	//*******************************************************************/	
	private void insertMrpItem(String gid,String mrp_no,String fg_code,String item_code,String mrp_start_date,
		String mrp_count,String factory_no,String factory_name,String pu_dev_date,String stock_link) throws Exception
	{
		String input = "";
		String pid = "";

		//MBOM ITEM�� ������ �����Ѵ�.(���ý��� �̿��� ��������)
		ArrayList item_list = new ArrayList();
		item_list = sortArrayStrList(fg_code,item_code,mrp_start_date,mrp_count,stock_link,factory_no);	
	
		//���̺� �����ϱ�
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			pid = anbdt.getNumID(n);
			//�����ϱ�
			input = "INSERT INTO mrp_item (pid,gid,mrp_no,assy_code,level_no,item_code,item_name,";
			input += "item_spec,item_type,draw_count,mrp_count,need_count,stock_count,open_count,";
			input += "plan_count,add_count,mrs_count,item_unit,buy_type,factory_no,factory_name,";
			input += "pu_dev_date,pu_req_no) values('";
			input += pid+"','"+gid+"','"+mrp_no+"','"+table.getAssyCode()+"','";
			input += table.getLevelNo()+"','"+table.getItemCode()+"','"+table.getItemName()+"','";
			input += table.getItemSpec()+"','"+table.getItemType()+"','"+table.getDrawCount()+"','";
			input += table.getMrpCount()+"','"+table.getNeedCount()+"','"+table.getStockCount()+"','";
			input += table.getOpenCount()+"','"+table.getPlanCount()+"','"+table.getAddCount()+"','";
			input += table.getMrsCount()+"','"+table.getItemUnit()+"','"+table.getBuyType()+"','";
			input += factory_no+"','"+factory_name+"','"+pu_dev_date+"','"+""+"')";
			//System.out.println("input : " + input);
			mrpDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	// MRP ITEM�� ���������ϱ� [MRS����]
	//*******************************************************************/
	public String updateMrpItem(String pid,String plan_count,String add_count) throws Exception
	{
		String data="",where="",mrp_status="",update="",mrs_count="";
		int pcnt = Integer.parseInt(plan_count);
		int acnt =  Integer.parseInt(add_count);

		//���Ź��ּ��� ���ϱ�
		mrs_count = Integer.toString(pcnt + acnt);

		//MRP ITEM�� �����ϱ�
		update = "UPDATE mrp_item SET add_count='"+add_count+"',mrs_count='"+mrs_count+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// �ҿ뷮 ���뺸�� : ���ý��� ���� �Ǵ� �̿��������� �����Ͽ� ����
	// [MRP�κ��� ���۵Ȱ�(mrp_no����) �Ǵ� �ӽ÷� MBOM ITEM���� ����Ȱ�(mrp_no����)]
	//*******************************************************************/	
	public ArrayList getStockMrpItemList(String factory_no,String mrp_no,String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String mrp_status) throws Exception
	{
		//�ʱ�ȭ
		ArrayList mrp_item = new ArrayList();		//������ mrp item List
		String[] stock = new String[2];				//���ý����� �����,�԰�������
		int stock_cnt = 0;							//���ý��ۻ��� ��ǰ�� (���+�԰�����)
		int need_cnt = 0;							//�ʿ����(������Ǽ���*���귮)
		
		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���, ���������ϱ�
		//--------------------------------------------
		ArrayList item_list = new ArrayList();
		item_list = getMrpItemList(factory_no,mrp_no,fg_code,item_code,mrp_start_date,mrp_count,mrp_status);
		int cnt = item_list.size();
		if(cnt == 0) return mrp_item;
		String[][] data = new String[cnt][23];

		//�迭�� ���
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM����
			data[n][10] = Integer.toString(table.getMrpCount());		//�����䱸����

			//-----------------------------------
			//����� �� �԰������� ���ϱ�
			//-----------------------------------
			//��ǰ�ҿ䷮ ������
			if(mrp_status.equals("S")) {		
				if(stock_link.equals("1")) {		//���ý��� ����
					stock = mrpDAO.getItemStockInfo(table.getItemCode(),factory_no);
					data[n][11] = stock[0];									//��������
					data[n][12] = stock[1];									//�԰�������
				} else {							//���ý��� �̿���
					data[n][11] = "0";		
					data[n][12] = "0";
				}

				//�����ʿ���� : BOM���� * �����䱸����
				need_cnt = table.getDrawCount()*table.getMrpCount();
				data[n][13] = Integer.toString(need_cnt);					//�����ʿ����

				//���ſ�������
				stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//������
				data[n][14] = Integer.toString(need_cnt - stock_cnt);		//���ſ�������

				//�߰����� (�������縦 ����Ͽ�)
				if(table.getItemType().equals("4")) data[n][15] = "0";											
				else data[n][15] = Integer.toString((need_cnt - stock_cnt) * -1);

				//���Ź��ּ���
				data[n][16] = Integer.toString(Integer.parseInt(data[n][14]) + Integer.parseInt(data[n][15]));									

			} 
			//��ǰ�ҿ䷮���� ��
			else {
				data[n][11] = Integer.toString(table.getStockCount());		
				data[n][12] = Integer.toString(table.getOpenCount());
				data[n][13] = Integer.toString(table.getNeedCount());		//�����ʿ����
				data[n][14] = Integer.toString(table.getPlanCount());		//���ſ�������
				data[n][15] = Integer.toString(table.getAddCount());		//�߰�����
				data[n][16] = Integer.toString(table.getMrsCount());		//���Ź��ּ���
			}

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][8]+":"+data[n][14]+":"+data[n][15]+":"+data[n][16]);
			n++;
		}

		//--------------------------------------------
		//Array list ������ ���� �����
		//--------------------------------------------
		mrpItemTable tableS = new mrpItemTable();
		for(int i=0; i<n; i++) {
			tableS = new mrpItemTable();
			tableS.setPid(data[i][0]);	
			tableS.setGid(data[i][1]);	
			tableS.setMrpNo(data[i][2]);	
			tableS.setAssyCode(data[i][3]);	
			tableS.setLevelNo(Integer.parseInt(data[i][4]));
			tableS.setItemCode(data[i][5]);	
			tableS.setItemName(data[i][6]);	
			tableS.setItemSpec(data[i][7]);	
			tableS.setItemType(data[i][8]);	
			tableS.setDrawCount(Integer.parseInt(data[i][9]));	
			tableS.setMrpCount(Integer.parseInt(data[i][10]));
			tableS.setNeedCount(Integer.parseInt(data[i][13]));
			tableS.setStockCount(Integer.parseInt(data[i][11]));	
			tableS.setOpenCount(Integer.parseInt(data[i][12]));	
			tableS.setPlanCount(Integer.parseInt(data[i][14]));	
			tableS.setAddCount(Integer.parseInt(data[i][15]));
			tableS.setMrsCount(Integer.parseInt(data[i][16]));
			tableS.setItemUnit(data[i][17]);	
			tableS.setBuyType(data[i][18]);
			tableS.setFactoryNo(data[i][19]);
			tableS.setFactoryName(data[i][20]);
			tableS.setPuDevDate(data[i][21]);	
			tableS.setPuReqNo(data[i][22]);	
			mrp_item.add(tableS);
		}

		return mrp_item;
	}
	//*******************************************************************
	// �ҿ뷮 ���뺸�� 
	// ���ý��� �̿���� MRP ITEM or MBOM ITEM���� ������������������ ����
	// [MRP�κ��� ���۵Ȱ�(mrp_no����) �Ǵ� �ӽ÷� MBOM ITEM���� ����Ȱ�(mrp_no����)]
	// level no : 1 �̸� ��ü�� �����ְ�, �ƴϸ� �ش�Ǵ� �ܴܰ� ������ �����ش�:MRP ITEM�� ��츸
	//*******************************************************************/	
	private ArrayList getMrpItemList(String factory_no,String mrp_no,String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String mrp_status) throws Exception
	{
		String where="",level_no="",use="E";
		ArrayList item_list = new ArrayList();

		//������ȣ ���ϱ�
		where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		//�ҿ䷮�� �뵵���ϱ�
		//if(mrp_status.equals("0")) use = "V";		//�ݷ����·� ���żҿ䷮ ���뺸��
		//else if(mrp_status.equals("1")) use = "V";	//�ۼ����·� ���żҿ䷮ ���뺸��

		//��캰 �ҿ䷮ ���ϱ�
		if(mrp_no.length() != 0) {		//MRP ITEM���� �ҿ䷮ �����ֱ�
			if(level_no.equals("1")) {	//�ٴܰ� ������ [��ü����]
				item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,use);	//��ȸ�뵵
			} else {					//�ܴܰ� ������ [�ش� ASSY�ڵ常 ����]
				item_list = mrpDAO.getSingleMrpItemList(factory_no,mrp_no,level_no,item_code);
			}
		} else {						//MBOM ITEM���� �ҿ䷮ �����ֱ�
			item_list = sortArrayStrList(fg_code,item_code,mrp_start_date,mrp_count,"0",factory_no);
		}
		return item_list;
	}
	//*******************************************************************
	// ASSY CODE LIST ���뺸��
	// �뵵 : ASSY������ ������ ���� [ASSY��,�԰��� ���ϱ�����...]
	//*******************************************************************/	
	public ArrayList getMrpItemAssyList(String factory_no,String mrp_no) throws Exception
	{
		//�ʱ�ȭ
		ArrayList item_assy = new ArrayList();		//������ Array List
		String[] item = new String[2];				//assy ��, �԰� ���ϱ�

		//Array list�� ������ �б�
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getAssyList(factory_no,mrp_no);
		int cnt = item_list.size();
		if(cnt == 0) return item_assy;
		String[][] data = new String[cnt][3];

		//�迭�� ���
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getMrpNo();
			data[n][1] = table.getAssyCode();
			data[n][2] = Integer.toString(table.getLevelNo());
			//System.out.println(data[n][0]+":"+data[n][1]+":"+data[n][2]);
			n++;
		}

		//�ش�ASSY�� ǰ��,�԰��� ���Ͽ� Array List�� �ٽ� ��´�.
		for(int i=0; i<n; i++) {
			table = new mrpItemTable();
			table.setMrpNo(data[i][0]);
			table.setAssyCode(data[i][1]);
			item = mrpDAO.getItemInfo(data[i][1]);
			table.setItemName(item[0]);
			table.setItemSpec(item[1]);
			table.setLevelNo(Integer.parseInt(data[i][2]));
			//System.out.println(data[i][2]+":"+data[i][1]+":"+item[0]+":"+item[1]);
			item_assy.add(table);
		}
		return item_assy;
	}

	//--------------------------------------------------------------------
	//
	//		���ý��ۿ� ��ǰ���� �� ��� ����
	//			
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// ���ý��ۿ� ����� ���� ��������� MRP ITEM�� UPDATE
	//*******************************************************************/	
	public String reserveStockMrpItemList(String factory_no,String mrp_no,String item_code) throws Exception
	{
		//�ʱ�ȭ
		String rdata="",update="";
		String[] stock = new String[2];				//���ý����� �����,�԰�������
		int stock_cnt = 0;							//���ý��ۻ��� ��ǰ�� (���+�԰�����)
		int need_cnt = 0;							//�ʿ����(������Ǽ���*���귮)
		
		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���, ���������ϱ�
		//--------------------------------------------
		String where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,"V");	//��ȸ�� �������ϱ�
		int cnt = item_list.size();
		if(cnt == 0) {
			rdata = "UPDATE�� ������ �����ϴ�.";
			return rdata;
		}
		String[][] data = new String[cnt][23];

		//���ý��� ������ �迭�� ���
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM����
			data[n][10] = Integer.toString(table.getMrpCount());		//�����䱸����

			//���ý��ۿ��� [����� �� �԰�������] ����� �������ϱ�
			stock = mrpDAO.getItemStockInfo(table.getItemCode(),factory_no,mrp_no);
			data[n][11] = stock[0];									//��������
			data[n][12] = stock[1];									//�԰�������
			
			//�����ʿ���� : BOM���� * �����䱸����
			need_cnt = table.getDrawCount()*table.getMrpCount();
			data[n][13] = Integer.toString(need_cnt);					//�����ʿ����

			//���ſ�������
			stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//������
			data[n][14] = Integer.toString(need_cnt - stock_cnt);		//���ſ�������

			//�߰����� (�������縦 ����Ͽ�)
			if(table.getItemType().equals("4")) data[n][15] = "0";											
			else data[n][15] = Integer.toString((need_cnt - stock_cnt) * -1);

			//���Ź��ּ���
			data[n][16] = Integer.toString(Integer.parseInt(data[n][14]) + Integer.parseInt(data[n][15]));									

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][9]+":"+data[n][10]+":"+data[n][13]);
			n++;
		}

		//--------------------------------------------
		//MRP ITEM�� ���� UPDATE�����ϱ�
		//--------------------------------------------
		mrpItemTable tableS = new mrpItemTable();
		for(int i=0; i<n; i++) {
			update = "UPDATE mrp_item SET need_count='"+data[i][13]+"',stock_count='"+data[i][11];
			update +="',open_count='"+data[i][12]+"',plan_count='"+data[i][14]+"',add_count='"+data[i][15];
			update +="',mrs_count='"+data[i][16]+"' where pid='"+data[i][0]+"'";
			//System.out.println(update);
			mrpDAO.executeUpdate(update);
		}

		return rdata;
	}

	//*******************************************************************
	// ���ý��ۿ� ����� ���� �ϰ� ������ҹ� MRP ITEM�� UPDATE
	//*******************************************************************/	
	public String cancelStockMrpItemList(String factory_no,String mrp_no,String item_code,String mrp_status) throws Exception
	{
		//�ʱ�ȭ
		String rdata="",update="";
		String stock = "";							///���ý����� �����
		int stock_cnt = 0;							//���ý��ۻ��� ��ǰ�� (���+�԰�����)
		int need_cnt = 0;							//�ʿ����(������Ǽ���*���귮)
		int mrs_cnt = 0;							//�Ǳ��ż���(���ſ��� + �߰� + �����Ҽ���)
		
		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���, ���������ϱ�
		//--------------------------------------------
		String where = "where mrp_no='"+mrp_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,"V");	//��ȸ�� �������ϱ�
		int cnt = item_list.size();
		if(cnt == 0) {
			rdata = "UPDATE�� ������ �����ϴ�.";
			return rdata;
		}
		String[][] data = new String[cnt][23];

		//���ý��� ������ �迭�� ���
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getGid();
			data[n][2] = table.getMrpNo();
			data[n][3] = table.getAssyCode();
			data[n][4] = Integer.toString(table.getLevelNo());
			data[n][5] = table.getItemCode();
			data[n][6] = table.getItemName();
			data[n][7] = table.getItemSpec();
			data[n][8] = table.getItemType();
			data[n][9] = Integer.toString(table.getDrawCount());		//BOM����
			data[n][10] = Integer.toString(table.getMrpCount());		//�����䱸����

			//���ý��ۿ��� [����� �� �԰�������] ��ҹ� �������ϱ�
			stock = mrpDAO.cancelItemStockInfo(table.getItemCode(),factory_no,mrp_no);
			data[n][11] = Integer.toString(Integer.parseInt(stock)*-1);	//��������(������ҷ�)
			data[n][12] = Integer.toString(table.getOpenCount());;	//�԰�������
			
			//�����ʿ���� : BOM���� * �����䱸����
			need_cnt = table.getDrawCount()*table.getMrpCount();
			data[n][13] = Integer.toString(need_cnt);					//�����ʿ����

			//���ſ�������
			stock_cnt = Integer.parseInt(data[n][11]) + Integer.parseInt(data[n][12]);	//������
			data[n][14] = Integer.toString(need_cnt - stock_cnt);		//���ſ�������

			//�߰������� ���Ź��ּ���
			data[n][15] = Integer.toString(table.getAddCount());		//�߰�����
			mrs_cnt = Integer.parseInt(data[n][14])+Integer.parseInt(data[n][15]+Integer.parseInt(stock));
			data[n][16] = Integer.toString(mrs_cnt);					//���Ź��ּ���

			data[n][17] = table.getItemUnit();
			data[n][18] = table.getBuyType();
			data[n][19] = table.getFactoryNo();
			data[n][20] = table.getFactoryName();
			data[n][21] = table.getPuDevDate();
			data[n][22] = table.getPuReqNo();
	//System.out.println(data[n][5]+":"+data[n][9]+":"+data[n][10]+":"+data[n][13]);
			n++;
		}

		//--------------------------------------------
		//MRP ITEM�� ���� UPDATE�����ϱ�
		//--------------------------------------------
		//���������� ���� ����� ������ ��Ҽ���*-1�� ǥ���Ͽ� ����ǹ�
		if(mrp_status.equals("4")) {				
			for(int i=0; i<n; i++) {
				update = "UPDATE mrp_item SET stock_count='"+data[i][11]+"' where pid='"+data[i][0]+"'";
				mrpDAO.executeUpdate(update);
			}
		} 
		//���������� �ݿ�
		else {									
			for(int i=0; i<n; i++) {
				update = "UPDATE mrp_item SET need_count='"+data[i][13]+"',stock_count='"+data[i][11];
				update +="',open_count='"+data[i][12]+"',plan_count='"+data[i][14]+"',add_count='"+data[i][15];
				update +="',mrs_count='"+data[i][16]+"' where pid='"+data[i][0]+"'";
				//System.out.println(update);
				mrpDAO.executeUpdate(update);
			}
		}

		rdata = "���������� UPDATE�Ǿ����ϴ�.";
		return rdata;
	}
	//*******************************************************************
	// ���ý��ۿ� ����� ���� ���� ������ҹ� MRP ITEM�� UPDATE
	//*******************************************************************/	
	public String cancelIndStockMrpItemList(String pid,String cur_mrp_status,String factory_no) throws Exception
	{
		//�ʱ�ȭ
		String rdata="",update="";
		String stock = "";							//���ý����� �����
		int stock_cnt = 0;							//���ý��ۻ��� ��ǰ�� (���+�԰�����)
		int need_cnt = 0;							//�ʿ����(������Ǽ���*���귮)
		int mrs_cnt = 0;							//�Ǳ��ż���(���ſ��� + �߰� + �����Ҽ���)

		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���, ���������ϱ�
		//--------------------------------------------
		com.anbtech.mm.entity.mrpItemTable table = new com.anbtech.mm.entity.mrpItemTable();
		table = mrpDAO.readMrpItem(pid,factory_no);

		String[] data = new String[23];

		//���ý��� ������ �迭�� ���
		data[0] = table.getPid();
		data[1] = table.getGid();
		data[2] = table.getMrpNo();
		data[3] = table.getAssyCode();
		data[4] = Integer.toString(table.getLevelNo());
		data[5] = table.getItemCode();
		data[6] = table.getItemName();
		data[7] = table.getItemSpec();
		data[8] = table.getItemType();
		data[9] = Integer.toString(table.getDrawCount());		//BOM����
		data[10] = Integer.toString(table.getMrpCount());		//�����䱸����

		//���ý��ۿ��� [����� �� �԰�������] ��ҹ� �������ϱ�
		stock = mrpDAO.cancelItemStockInfo(table.getItemCode(),table.getFactoryNo(),table.getMrpNo());
		data[11] = Integer.toString(Integer.parseInt(stock)*-1);	//��������(������ҷ�)
		data[12] = Integer.toString(table.getOpenCount());;	//�԰�������
		
		//�����ʿ���� : BOM���� * �����䱸����
		need_cnt = table.getDrawCount()*table.getMrpCount();
		data[13] = Integer.toString(need_cnt);					//�����ʿ����

		//���ſ�������
		stock_cnt = Integer.parseInt(data[11]) + Integer.parseInt(data[12]);	//������
		data[14] = Integer.toString(need_cnt - stock_cnt);		//���ſ�������

		//�߰������� ���Ź��ּ���
		data[15] = Integer.toString(table.getAddCount());		//�߰�����
		mrs_cnt = Integer.parseInt(data[14])+Integer.parseInt(data[15]+Integer.parseInt(stock));
		data[16] = Integer.toString(mrs_cnt);					//���Ź��ּ���

		data[17] = table.getItemUnit();
		data[18] = table.getBuyType();
		data[19] = table.getFactoryNo();
		data[20] = table.getFactoryName();
		data[21] = table.getPuDevDate();
		data[22] = table.getPuReqNo();
	
		//--------------------------------------------
		//MRP ITEM�� ���� UPDATE�����ϱ�
		//���°� '4'�̸� �����и� ��ҵȴ�.
		//--------------------------------------------
		if(!cur_mrp_status.equals("4")) {
			update = "UPDATE mrp_item SET need_count='"+data[13]+"',stock_count='"+data[11];
			update +="',open_count='"+data[12]+"',plan_count='"+data[14]+"',add_count='"+data[15];
			update +="',mrs_count='"+data[16]+"' where pid='"+data[0]+"'";
			//System.out.println(update);
			mrpDAO.executeUpdate(update);
		} 
		//���������� ���� ����� ������ -10000�� ǥ���Ͽ� ����ǹ�
		else {
			update = "UPDATE mrp_item SET stock_count='"+data[11]+"' where pid='"+data[0]+"'";
			mrpDAO.executeUpdate(update);
		}
		

		rdata = "���������� UPDATE�Ǿ����ϴ�.";
		return rdata;
	}
	//--------------------------------------------------------------------
	//
	//		���Ű��� �� ���� �޼ҵ� ����
	//			���/����/���� 
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * �ϰ� ���Ű����� ����ϱ�
	 * pid : MRP MASTER�� pid
	 **********************************************************************/
	public void savePurchaseItem(String pid) throws Exception
	{
		
		String data="",where="",update="",input="";
		String mrp_no="",item_code="",level_no="",factory_no="",factory_name="";
		String reg_div_code="",reg_div_name="",reg_id="",reg_name="";
		String pjt_code="",pjt_name="";

		String request_no = purchaseDAO.getRequestNo();	//���ſ�û��ȣ
		String todate = anbdt.getDate();				//���ſ�û����
		String yy = todate.substring(2,4);				//��û�⵵ 2�ڸ�
		String mm = anbdt.getMonth();					//��û�� 2�ڸ�
		int len = request_no.length();
		String serial = request_no.substring(len-3,len);//��û �ø����ȣ

		//MPS MASTE���� �ش����� ���ϱ�
		com.anbtech.mm.entity.mrpMasterTable mrpT = new com.anbtech.mm.entity.mrpMasterTable();
		mrpT = mrpDAO.readMrpMasterItem(pid);
		
		mrp_no = mrpT.getMrpNo();
		item_code = mrpT.getItemCode();
		factory_no = mrpT.getFactoryNo();
		factory_name = mrpT.getFactoryName();
		reg_div_code = mrpT.getRegDivCode();
		reg_div_name = mrpT.getRegDivName();
		reg_id = mrpT.getRegId();
		reg_name = mrpT.getRegName();
		pjt_code = mrpT.getPjtCode();
		pjt_name = mrpT.getPjtName();

		//���� ITEM�� ���ϱ����� level no ���ϱ�
		where = "where gid='"+pid+"' and assy_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MRP_ITEM","level_no",where);

		//--------------------------------------------------------
		//MRP�� ���ſ�û��ȣ update�ϱ� : mrp_master , mrp_item
		//--------------------------------------------------------
		update = "UPDATE mrp_master SET pu_req_no='"+request_no+"' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		update = "UPDATE mrp_item SET pu_req_no='"+request_no+"' where gid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		//--------------------------------------------------------
		//���Ű����� ���ſ�û��������� ����ϱ� : pu_requested_info
		//--------------------------------------------------------
		input = "INSERT INTO pu_requested_info (request_no,request_type,factory_code,factory_name,request_date,";
		input += "requester_div_code,requester_div_name,requester_id,requester_info,request_total_mount,";
		input += "reg_year,reg_month,reg_serial,project_code,project_name,aid) values('";
		input += request_no+"','"+"MRP"+"','"+factory_no+"','"+factory_name+"','"+todate+"','";
		input += reg_div_code+"','"+reg_div_name+"','"+reg_id+"','"+reg_name+"','"+"0"+"','";
		input += yy+"','"+mm+"','"+serial+"','"+pjt_code+"','"+pjt_name+"','"+""+"')";
		//System.out.println("input : " + input);
		mrpDAO.executeUpdate(input);

		//--------------------------------------------------------
		//���Ű����� ���ſ�ûǰ�񸮽�Ʈ�� ����ϱ� : pu_requested_item
		//�� MRS������ 0 ���� ū ������
		//--------------------------------------------------------
		ArrayList item_list = new ArrayList();
		item_list = sortArrayMrpItemList(factory_no,mrp_no,level_no,item_code);

		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			if(table.getMrsCount() > 0) {
				input = "INSERT INTO pu_requested_item (request_no,item_code,item_name,item_desc,request_unit,";
				input += "delivery_date,request_quantity,order_quantity,delivery_quantity,supplyer_code,";
				input += "supplyer_name,request_cost,fname,ftype,fsize,fpath,fumask,supply_cost,process_stat) values('";
				input += request_no+"','"+table.getItemCode()+"','"+table.getItemName()+"','"+table.getItemSpec()+"','"+table.getItemUnit()+"','";
				input += table.getPuDevDate()+"','"+table.getMrsCount()+"','"+0+"','"+0+"','"+""+"','";
				input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+"S01"+"')";
				//System.out.println("input : " + input);
				mrpDAO.executeUpdate(input);
			}
		}

	}

	//*******************************************************************
	// ������� 
	//	���ŵ�ϵ� ���翹�� ��ҽ� ���ſ� ���翹��� �ݿ��ϱ� 
	//  mrp_status = '3'�̰� ����ǰ�̸� ���Ű����� ������ ���ÿ� �����Ѵ�.
	//*******************************************************************/
	public void updateIndPurchaseItem(String pid,String factory_no) throws Exception
	{
		String data="",where="",gid="",mrp_status="",update="",mrs_count="",item_type="";
		String item_code="",pu_req_no="",request_quantity="",mrp_no="";
		int stock_count=0;

		//������·� ���Ű����� ��ϵǾ��� Ȯ���Ͽ� ��Ͻô� �ش�ǰ���� ���ÿ� �����Ѵ�.
		//item_type='4' �ΰ�츸 �ش�ȴ�.
		com.anbtech.mm.entity.mrpItemTable mrpIT = new com.anbtech.mm.entity.mrpItemTable();
		mrpIT = mrpDAO.readMrpItem(pid,factory_no);
		gid = mrpIT.getGid();
		item_code = mrpIT.getItemCode();
		item_type = mrpIT.getItemType();
		stock_count = mrpIT.getStockCount();
		pu_req_no = mrpIT.getPuReqNo();
		mrp_no = mrpIT.getMrpNo();

		String stock = mrpDAO.cancelItemStockInfo(item_code,factory_no,mrp_no);

		//������¸� �˻��Ͽ� mrp_status='3'�ΰ�츸 �����
		where = "where pid='"+gid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);

		if(!mrp_status.equals("3")) {		//���°� 3 �ΰ�츸 ����ȴ�.
			return;
		}

		//����ǰ���� �Ǵ��Ͽ� �����ϱ�
		if(!item_type.equals(purchase_type)) {			//����ǰ�� ����ȴ�. 
			return ;			
		}

		//���ſ� ��ϵ� ������ �ľ��ϱ�
		where = "where request_no='"+pu_req_no+"' and item_code='"+item_code+"'";
		request_quantity = mrpDAO.getColumData("pu_requested_item","request_quantity",where);

		//����� ������ ���ϱ� (��ҵ� ��������ŭ ���Ͽ� �ֱ�)
		mrs_count = Integer.toString(Integer.parseInt(stock) + Integer.parseInt(request_quantity));
	
		//pu_requested_item�� �����ϱ�
		update = "UPDATE pu_requested_item SET request_quantity='"+mrs_count+"' ";
		update +="where request_no='"+pu_req_no+"' and item_code='"+item_code+"'";
		//System.out.println("up : "+update);
		mrpDAO.executeUpdate(update);
		
	}

	//*******************************************************************
	// �ϰ���� 
	//	���ŵ�ϵ� ���翹�� �ϰ���ҽ� ���ſ� ���翹��� �����ϱ�
	//  mrp_status = '3'�� ���
	//*******************************************************************/
	public void deletePurchaseItem(String pid) throws Exception
	{
		String data="",where="",delete="",update="";
		String item_code="",mrp_status="",pu_req_no="";
		
		//������¸� �˻��Ͽ� mrp_status='3'�ΰ�츸 �����
		where = "where pid='"+pid+"'";
		mrp_status = mrpDAO.getColumData("MRP_MASTER","mrp_status",where);
		pu_req_no = mrpDAO.getColumData("MRP_MASTER","pu_req_no",where);

		//pu_requested_item,pu_requested_info �����ϱ�
		delete = "DELETE from pu_requested_item where request_no='"+pu_req_no+"'";
		mrpDAO.executeUpdate(delete);

		delete = "DELETE from pu_requested_info where request_no='"+pu_req_no+"'";
		mrpDAO.executeUpdate(delete);

		//���ſ�û��ȣ ���� : mrp_master,mrp_item
		update = "UPDATE mrp_master SET pu_req_no='' where pid='"+pid+"'";
		mrpDAO.executeUpdate(update);

		update = "UPDATE mrp_item SET pu_req_no='' where gid='"+pid+"'";
		mrpDAO.executeUpdate(update);
		
		
	}

	/**********************************************************************
	 *  ��ǰ�񳢸��� ������
	 *  MRP ITEM �ҿ䷮ ���� 
	 *  mrp_start_date : ��� ��������
	 **********************************************************************/
	private ArrayList sortArrayMrpItemList(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		//����
		ArrayList item_list = new ArrayList();

		//------------------------------------------------------------------
		//���� �迭�����
		//------------------------------------------------------------------
		saveMrpItemArray(factory_no,mrp_no,level_no,assy_code);

		//���� �ľ��ϱ�
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// ������ ��ǰ�� �ڵ庰�� ����, �� ���Ź���ǰ�� ���ؼ���(item_type:4)
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//������ ��ǰ�񳢸� ���� �迭����
		String[][] data = new String[an][8];
		for(int i=0; i<an; i++) for(int j=0; j<8; j++) data[i][j]="";
		
		//���ο� �迭�� ��� 
		int cnt = an -1;
		int n=0,q=0;					//�űԹ迭��ȣ,mrs�����Ѽ���
		for(int i=0; i<=cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < cnt) {
				//��ǰ���ڵ尡 ������ ���ڸ� ���
				if(item[i][5].equals(item[i+1][5])) {	
					q += Integer.parseInt(item[i][16]);
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					q += Integer.parseInt(item[i][16]);
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][5];			//Item Code
					data[n][3] = item[i][6];			//Item Name
					data[n][4] = item[i][7];			//Item Spec
					data[n][5] = item[i][17];			//Item unit
					data[n][6] = item[i][21];			//����԰���
					data[n][7] = Integer.toString(q);	//���ż���
					n++;
					q=0;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				q += Integer.parseInt(item[i][16]);
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][5];			//Item Code
				data[n][3] = item[i][6];			//Item Name
				data[n][4] = item[i][7];			//Item Spec
				data[n][5] = item[i][17];			//Item unit
				data[n][6] = item[i][21];			//����԰���
				data[n][7] = Integer.toString(q);	//���ż���
			} //else
		} //for

		//���Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][2]+":"+data[i][3]+":"+data[i][5]+":"+data[i][7]);
		//}

		//����� �����ͷ� ArrayList�� ��� �����ϱ�
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setItemCode(data[i][2]);
			mit.setItemName(data[i][3]);
			mit.setItemSpec(data[i][4]);
			mit.setItemUnit(data[i][5]);
			mit.setPuDevDate(anbdt.getSepDate(data[i][6],"-"));
			mit.setMrsCount(Integer.parseInt(data[i][7]));
			item_list.add(mit); 
		}
		
		return item_list;
	}
	/**********************************************************************
	 * MRP ITEM�� �迭�� ���
	 * [item type = '4'�� ���Ű��� ǰ��]
	 **********************************************************************/
	private void saveMrpItemArray(String factory_no,String mrp_no,String level_no,String assy_code) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.buyMrpItemList(factory_no,mrp_no,level_no,assy_code);
		int cnt = item_list.size();
		String[][] data = new String[cnt][23];
		for(int i=0; i<cnt; i++) for(int j=0; j<23; j++) data[i][j]="";

		//�迭�� ���
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();

			if(table.getItemType().equals(purchase_type)) {
				data[an][0] = table.getPid();
				data[an][1] = table.getGid();
				data[an][2] = table.getMrpNo();
				data[an][3] = table.getAssyCode();
				data[an][4] = Integer.toString(table.getLevelNo());
				data[an][5] = table.getItemCode();
				data[an][6] = table.getItemName();
				data[an][7] = table.getItemSpec();
				data[an][8] = table.getItemType();
				data[an][9] = Integer.toString(table.getDrawCount());
				data[an][10] = Integer.toString(table.getMrpCount());
				data[an][11] = Integer.toString(table.getNeedCount());
				data[an][12] = Integer.toString(table.getStockCount());
				data[an][13] = Integer.toString(table.getOpenCount());
				data[an][14] = Integer.toString(table.getPlanCount());
				data[an][15] = Integer.toString(table.getAddCount());
				data[an][16] = Integer.toString(table.getMrsCount());
				data[an][17] = table.getItemUnit();
				data[an][18] = table.getBuyType();
				data[an][19] = table.getFactoryNo();
				data[an][20] = table.getFactoryName();
				data[an][21] = table.getPuDevDate();
				data[an][22] = table.getPuReqNo();
				//System.out.println(data[an][4]+":"+data[an][2]+":"+data[an][3]+":"+data[an][5]);
				an++;
			}
		}
		//�����迭�� ���
		item = new String[an][23];
		for(int i=0; i<an; i++) for(int j=0; j<23; j++) item[i][j]=data[i][j];

		//������ ���� �����ϱ�
		sortA.bubbleSortStringMultiAsc(item,5);
	}

	//--------------------------------------------------------------------
	//
	//		MBOM ITEM�� ARRAY LIST�� �迭�� ���
	//			
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * ��ϵ� BOM������ �迭�� ��� : �ҿ䷮���ϱ� �غ� (�ٴܰ�)
	 * ��ϵ� ���� ��º���
	 **********************************************************************/
	public void saveBomItemArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMbomItemList(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) item[i][j]="";

		//�迭�� ���
		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getParentCode();
			item[an][3] = table.getChildCode();
			item[an][4] = table.getLevelNo();
			item[an][5] = table.getPartName();
			item[an][6] = table.getPartSpec();
			item[an][7] = table.getLocation();
			item[an][8] = table.getItemType();
			item[an][9] = table.getQtyUnit();
			item[an][10] = table.getBuyType();
			//System.out.println(item[an][4]+":"+item[an][2]+":"+item[an][3]);
			an++;
		}
	}
	/**********************************************************************
	 *  ��ǰ�� / ��ǰ�񳢸� ������ (�ٴܰ�)
	 *  MBOM ITEM �ҿ䷮ ���� (MBOM ITEM������ ASSY��/��ǰ�񳢸� ������ ���� ����ϱ�) 
	 *  mrp_start_date : ��� ��������
	 **********************************************************************/
	public ArrayList sortArrayStrList(String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String factory_no) throws Exception
	{
		//����
		ArrayList item_list = new ArrayList();
		if(mrp_count.length() == 0) mrp_count = "1";

		//-----------------------------------------------------------------
		//�˻� �� �ʿ����� ���ϱ� : MBOM����� ���� �ʿ����� ���ϱ�
		//-----------------------------------------------------------------
		String where="",bom_status="",gid="",level_no="";

		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) return item_list;
		

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt_item = mrpDAO.checkItemCode(gid,item_code);
		if(cnt_item == 0) return item_list;
		
		//level no ���ϱ�
		where = "where gid='"+gid+"' and parent_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MBOM_ITEM","level_no",where);	

		//------------------------------------------------------------------
		//���� �迭�����
		//------------------------------------------------------------------
		saveBomItemArray(gid,level_no,item_code,mrp_start_date);

		//���� �ľ��ϱ�
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// ASSY�ڵ庰 ������ ��ǰ�� �ڵ庰�� ����
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//������ ��/��ǰ�� ���� ����  �迭����
		String[][] data = new String[an][12];
		for(int i=0; i<an; i++) for(int j=0; j<12; j++) data[i][j]="";

		//���ο� �迭�� ��� 
		int cnt = an -1;
		int n=0,q=1;					//�űԹ迭��ȣ, ����
		for(int i=0; i<=cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < cnt) {
				//��ǰ���ڵ�� ��ǰ���ڵ尡 ������ : location �� ���
				if(item[i][2].equals(item[i+1][2]) && item[i][3].equals(item[i+1][3])) {
					data[n][7] += item[i][7]+",";	
					q++;
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][2];			//Assy Code
					data[n][3] = item[i][3];			//Item Code
					data[n][4] = item[i][4];			//Level no
					data[n][5] = item[i][5];			//Item Name
					data[n][6] = item[i][6];			//Item Spec
					data[n][7] += item[i][7];			//Location
					data[n][8] = item[i][8];			//Item Type
					data[n][9] = item[i][9];			//Item unit
					data[n][10] = item[i][10];			//Buy Type
					data[n][11] = Integer.toString(q);	//������� ������ item code ����
					n++;
					q=1;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][2];			//Assy Code
				data[n][3] = item[i][3];			//Item Code
				data[n][4] = item[i][4];			//Level no
				data[n][5] = item[i][5];			//Item Name
				data[n][6] = item[i][6];			//Item Spec
				data[n][7] += item[i][7];			//Location
				data[n][8] = item[i][8];			//Item Type
				data[n][9] = item[i][9];			//Item unit
				data[n][10] = item[i][10];			//Buy Type
				data[n][11] = Integer.toString(q);	//������� ������ item code ����
			} //else
		} //for

		//���Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][4]+":"+data[i][2]+":"+data[i][3]+":"+data[i][11]);
		//}

		//����� �����ͷ� ArrayList�� ��� �����ϱ�
		String[] stock = new String[2];
		int ncnt=0,stock_count=0,open_count=0,total_stock=0;
		int m_ncnt = 0;
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setPid(data[i][0]);
			mit.setGid(data[i][1]);
			mit.setMrpNo("");
			mit.setAssyCode(data[i][2]);
			mit.setLevelNo(Integer.parseInt(data[i][4]));
			mit.setItemCode(data[i][3]);
			mit.setItemName(data[i][5]);
			mit.setItemSpec(data[i][6]);
			mit.setItemType(data[i][8]);
			mit.setDrawCount(Integer.parseInt(data[i][11]));
			mit.setMrpCount(Integer.parseInt(mrp_count));

			ncnt = Integer.parseInt(data[i][11])*Integer.parseInt(mrp_count);	//�����ʿ����
			mit.setNeedCount(ncnt);

			//���ý��� �̿��� : ������� �������� ��ٷ� �ϴܼ����� �Է��Ѵ�.
			if(stock_link.equals("0")) {
				mit.setStockCount(0);
				mit.setOpenCount(0);
				mit.setPlanCount(ncnt);
				//item type�� ���� ���� (�������籸���Ͽ� ���� �����ֱ�)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt);
				} else {
					m_ncnt = ncnt * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			} 
			//���ý��� ���� : ������ �ϴܼ����� �Է��Ѵ�.���� ���⼭�� ��� 0
			else {
				//����� �� �԰������� �������ϱ�
				stock = mrpDAO.getItemStockInfo(data[i][3],factory_no);
				stock_count = Integer.parseInt(stock[0]);			//��������
				open_count = Integer.parseInt(stock[1]);			//�԰�������
				total_stock = stock_count + open_count;				//�����+�԰�������

				mit.setStockCount(stock_count);
				mit.setOpenCount(open_count);
				mit.setPlanCount(ncnt - total_stock);
				//item type�� ���� ���� (�������籸���Ͽ� ���� �����ֱ�)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt - total_stock);
				} else {
					m_ncnt = (ncnt - total_stock) * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			}
			mit.setItemUnit(data[i][9]);
			mit.setBuyType(data[i][10]);
			mit.setFactoryNo("");
			mit.setFactoryName("");
			mit.setPuDevDate("");
			mit.setPuReqNo("");
			item_list.add(mit); 
		}
		
		return item_list;
	}

	/**********************************************************************
	 * ��ϵ� BOM������ �迭�� ��� : �ҿ䷮���ϱ� �غ� (�ܴܰ�)
	 * ��ϵ� ���� ��º���
	 **********************************************************************/
	private void saveBomSingleItemArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = mrpDAO.getMbomSingleItemList(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) item[i][j]="";

		//�迭�� ���
		mbomItemTable table = new mbomItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomItemTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getParentCode();
			item[an][3] = table.getChildCode();
			item[an][4] = table.getLevelNo();
			item[an][5] = table.getPartName();
			item[an][6] = table.getPartSpec();
			item[an][7] = table.getLocation();
			item[an][8] = table.getItemType();
			item[an][9] = table.getQtyUnit();
			item[an][10] = table.getBuyType();
			//System.out.println(item[an][4]+":"+item[an][2]+":"+item[an][3]);
			an++;
		}
	}
	/**********************************************************************
	 *  ��ǰ�� / ��ǰ�񳢸� ������ (�ܴܰ�)
	 *  MBOM ITEM �ҿ䷮ ���� (MBOM ITEM������ ASSY��/��ǰ�񳢸� ������ ���� ����ϱ�) 
	 *  mrp_start_date : ��� ��������
	 **********************************************************************/
	public ArrayList sortArraySingleStrList(String fg_code,String item_code,
		String mrp_start_date,String mrp_count,String stock_link,String factory_no) throws Exception
	{
		//����
		ArrayList item_list = new ArrayList();
		if(mrp_count.length() == 0) mrp_count = "1";

		//-----------------------------------------------------------------
		//�˻� �� �ʿ����� ���ϱ� : MBOM����� ���� �ʿ����� ���ϱ�
		//-----------------------------------------------------------------
		String where="",bom_status="",gid="",level_no="";

		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mrpDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) return item_list;
		

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mrpDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt_item = mrpDAO.checkItemCode(gid,item_code);
		if(cnt_item == 0) return item_list;
		
		//level no ���ϱ�
		where = "where gid='"+gid+"' and parent_code='"+item_code+"'";
		level_no = mrpDAO.getColumData("MBOM_ITEM","level_no",where);	

		//------------------------------------------------------------------
		//���� �迭�����
		//------------------------------------------------------------------
		saveBomSingleItemArray(gid,level_no,item_code,mrp_start_date);

		//���� �ľ��ϱ�
		if(an == 0) return item_list;
	
		//------------------------------------------------------------------
		// ASSY�ڵ庰 ������ ��ǰ�� �ڵ庰�� ����
		// 0:pid, 1:gid, 2:assy code, 3:item code, 4:level no, 5:item name
		// 6:item_spec, 7:location,, 8:item type, 9:unit, 10:buy type
		//------------------------------------------------------------------
		//������ ��/��ǰ�� ���� ����  �迭����
		String[][] data = new String[an][12];
		for(int i=0; i<an; i++) for(int j=0; j<12; j++) data[i][j]="";

		//���ο� �迭�� ��� 
		int cnt = an -1;
		int n=0,q=1;					//�űԹ迭��ȣ, ����
		for(int i=0; i<=cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < cnt) {
				//��ǰ���ڵ�� ��ǰ���ڵ尡 ������ : location �� ���
				if(item[i][2].equals(item[i+1][2]) && item[i][3].equals(item[i+1][3])) {
					data[n][7] += item[i][7]+",";	
					q++;
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					data[n][0] = item[i][0];			//pid
					data[n][1] = item[i][1];			//gid
					data[n][2] = item[i][2];			//Assy Code
					data[n][3] = item[i][3];			//Item Code
					data[n][4] = item[i][4];			//Level no
					data[n][5] = item[i][5];			//Item Name
					data[n][6] = item[i][6];			//Item Spec
					data[n][7] += item[i][7];			//Location
					data[n][8] = item[i][8];			//Item Type
					data[n][9] = item[i][9];			//Item unit
					data[n][10] = item[i][10];			//Buy Type
					data[n][11] = Integer.toString(q);	//������� ������ item code ����
					n++;
					q=1;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				data[n][0] = item[i][0];			//pid
				data[n][1] = item[i][1];			//gid
				data[n][2] = item[i][2];			//Assy Code
				data[n][3] = item[i][3];			//Item Code
				data[n][4] = item[i][4];			//Level no
				data[n][5] = item[i][5];			//Item Name
				data[n][6] = item[i][6];			//Item Spec
				data[n][7] += item[i][7];			//Location
				data[n][8] = item[i][8];			//Item Type
				data[n][9] = item[i][9];			//Item unit
				data[n][10] = item[i][10];			//Buy Type
				data[n][11] = Integer.toString(q);	//������� ������ item code ����
			} //else
		} //for

		//���Test
		//for(int i=0; i<=n; i++) {
		//	System.out.println(data[i][4]+":"+data[i][2]+":"+data[i][3]+":"+data[i][11]);
		//}

		//����� �����ͷ� ArrayList�� ��� �����ϱ�
		String[] stock = new String[2];
		int ncnt=0,stock_count=0,open_count=0,total_stock=0;
		int m_ncnt = 0;
		for(int i=0; i<=n; i++) {
			mrpItemTable mit = new mrpItemTable();
			mit.setPid(data[i][0]);
			mit.setGid(data[i][1]);
			mit.setMrpNo("");
			mit.setAssyCode(data[i][2]);
			mit.setLevelNo(Integer.parseInt(data[i][4]));
			mit.setItemCode(data[i][3]);
			mit.setItemName(data[i][5]);
			mit.setItemSpec(data[i][6]);
			mit.setItemType(data[i][8]);
			mit.setDrawCount(Integer.parseInt(data[i][11]));
			mit.setMrpCount(Integer.parseInt(mrp_count));

			ncnt = Integer.parseInt(data[i][11])*Integer.parseInt(mrp_count);	//�����ʿ����
			mit.setNeedCount(ncnt);

			//���ý��� �̿��� : ������� �������� ��ٷ� �ϴܼ����� �Է��Ѵ�.
			if(stock_link.equals("0")) {
				mit.setStockCount(0);
				mit.setOpenCount(0);
				mit.setPlanCount(ncnt);
				//item type�� ���� ���� (�������籸���Ͽ� ���� �����ֱ�)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt);
				} else {
					m_ncnt = ncnt * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			} 
			//���ý��� ���� : ������ �ϴܼ����� �Է��Ѵ�.���� ���⼭�� ��� 0
			else {
				//����� �� �԰������� �������ϱ�
				stock = mrpDAO.getItemStockInfo(data[i][3],factory_no);
				stock_count = Integer.parseInt(stock[0]);			//��������
				open_count = Integer.parseInt(stock[1]);			//�԰�������
				total_stock = stock_count + open_count;				//�����+�԰�������

				mit.setStockCount(stock_count);
				mit.setOpenCount(open_count);
				mit.setPlanCount(ncnt - total_stock);
				//item type�� ���� ���� (�������籸���Ͽ� ���� �����ֱ�)
				if(data[i][8].equals("4")) {
					mit.setAddCount(0);
					mit.setMrsCount(ncnt - total_stock);
				} else {
					m_ncnt = (ncnt - total_stock) * -1;
					mit.setAddCount(m_ncnt);
					mit.setMrsCount(0);
				}
			}
			mit.setItemUnit(data[i][9]);
			mit.setBuyType(data[i][10]);
			mit.setFactoryNo("");
			mit.setFactoryName("");
			mit.setPuDevDate("");
			mit.setPuReqNo("");
			item_list.add(mit); 
		}
		
		return item_list;
	}

}


