package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();			//����
	private com.anbtech.mm.db.mfgModifyDAO mfgDAO = null;	
	private com.anbtech.mm.business.mrpInputBO mrpBO = null;
	private com.anbtech.mm.db.mrpModifyDAO mrpDAO = null;
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;	
	private String query = "";

	private String[][] item = null;				//TREE������ �迭�� ���
	private int an = 0;							//items�� �迭 ����
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mfgInputBO(Connection con) 
	{
		this.con = con;
		mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
		mrpBO = new com.anbtech.mm.business.mrpInputBO(con);
		mrpDAO = new com.anbtech.mm.db.mrpModifyDAO(con);
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		���°����ϱ� 
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  MFG MASTER�� ���°����ϱ�
	//*******************************************************************/	
	public String setMfgStatus(String login_id,String login_name,String pid,String mrp_no,String mfg_no,
		String item_code,String item_unit,String fg_code,String mfg_count,String order_type,
		String order_status,String order_start_date,String order_end_date,String factory_no,
		String factory_name,String re_work) throws Exception
	{
		String data="",where="",update="";
		String date = anbdt.getDateNoformat();
		
		//��������, �����ۼ�����, ����ǰ�ҿ䷮��������
		if(order_status.equals("2")) {
			//MFG ITEM�� �ҿ䷮ �ϰ��Է��ϱ�
			insertMfgItem(pid,mrp_no,mfg_no,item_code,order_start_date,factory_no,order_type,fg_code,mfg_count,re_work);

			//MFG OPERATOR ������ �����⺻���� �ϰ��Է��ϱ�
			insertMfgOperator(pid,mrp_no,mfg_no,mfg_count,item_unit,order_start_date,order_end_date,factory_no,factory_name,order_type,fg_code,item_code,re_work);

			//������� 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "������ȹ ������ �����Ǿ����ϴ�.";
		} 
		//����Ȯ������, ����ǰ�������,
		else if(order_status.equals("3")) {
			//��ǰ ���뼺 �˻�
			data = checkPuStatus(order_type,mrp_no,mfg_no,factory_no,item_code);
			if(!data.equals("P")) return data;

			//���� �����ϱ� (�������:mfg_inout_master, ������:st_item_stock_master)
			//pid�� mfg_master�� pid��
			reserveMfgItem(pid,mfg_no,item_code,factory_no,factory_name);

			//�������� Ȯ�� : ������
			update = "UPDATE mfg_master SET order_date='"+date+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//�������� Ȯ�� : ��ǰ�ҿ䷮
			update = "UPDATE mfg_item SET order_date='"+date+"' where gid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//�������� Ȯ�� : �۾����ü�
			update = "UPDATE mfg_operator SET order_date='"+date+"',op_order='1' where gid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//������� 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			//MPS�� ������� �˷��ֱ�
			if(mrp_no.length() !=0) {
				where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
				String mps_no = mfgDAO.getColumData("MRP_MASTER","mps_no",where);
				update = "UPDATE mps_master SET mps_status='6' where mps_no='"+mps_no+"' and factory_no='"+factory_no+"'";
				mfgDAO.executeUpdate(update);
			}
			data = "������ȹ�� Ȯ���Ǿ����ϴ�."; 
		} 
		//��ǰ����Ƿڿ�û����
		else if(order_status.equals("4")) { 
			//��ǰ����Ƿ��ϱ�
			data = saveBatchDelivery(pid,login_id,login_name,mfg_no,item_code,factory_no,factory_name);
			if(!data.equals("P")) return data;

			//������� 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "��ǰ��� �ǷڵǾ����ϴ�.";
		} 
		//���ǰ ��ϻ���
		else if(order_status.equals("5")) {
			//������� 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "��������� ��ϵǾ����ϴ�.";
		} 
		//������� ���� ����
		else if(order_status.equals("6")) {
			//������� 
			update = "UPDATE mfg_master SET order_status='"+order_status+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);

			data = "��������� �����Ǿ����ϴ�.";
		}
		
		return data;
	}

	//*******************************************************************
	//  ����������� �˻��ϱ�
	//  Return : P [Pass] , else [��������]
	//*******************************************************************/	
	private String checkPuStatus(String order_type,String mrp_no,String mfg_no,
		String factory_no,String item_code) throws Exception
	{
		String data="P",where="",pu_req_no="",pu_status="",part="";
		String stock_quantity="";		//�������� ����� ����
		int dif_cnt = 0;				//��޿����� ��������� ���������� ũ��.
		
		//��޿��� ���������϶� : ���������� 
		if(order_type.equals("MANUAL")) {
			//item�迭�� ���� ���
			//0:item_code,1:item_name,2:item_spec,3:item_unit
			//4:item_type,5:reserve_count,6:factory_no
			arrayMfgItemUniqueList(factory_no,mfg_no,item_code);
			for(int i=0; i<an; i++) {
				//����� ���డ������ �Ǵ��ϱ�
				where = "where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
				stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
				if(stock_quantity.length() == 0) stock_quantity = "0";
			
				//���డ���� �������� �ִ��� �Ǵ��ϱ�
				if(Integer.parseInt(stock_quantity) < Integer.parseInt(item[i][5])) {
					part += item[i][0]+"("+stock_quantity+":"+item[i][5]+"), ";
					dif_cnt++; 
				}
			}
			if(dif_cnt != 0) data = "�������� �������� ������� ���� ǰ��["+part+"]�� �ֽ��ϴ�.";
		}
		//MRS���� ���������϶� : �����԰��� ���԰˻�Ȯ������
		else {
			//�����Ƿڹ�ȣ ã��
			where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
			pu_req_no = mfgDAO.getColumData("MRP_MASTER","pu_req_no",where);

			//�����԰���� �ľ�
			//where = "where request_no like '%"+pu_req_no+"%'";
			//pu_status = mfgDAO.getColumData("PU_ENTERED_ITEM","process_stat",where);
			com.anbtech.pu.db.PurchaseMgrDAO pmDAO = new com.anbtech.pu.db.PurchaseMgrDAO(con);
			pu_status = pmDAO.getMaxStatForEnterItemByRequestNo(pu_req_no);

			//�Ǵ��ϱ�
			if(pu_status.equals("S25")) data = "P"; //Pass
			else if(pu_status.equals("S21")) data = "�����԰� �����Դϴ�";
			else data = "���Ź����Ƿ� �Ǵ� ���Ź������� �����Դϴ�.";
		}

		return data;
	}

	//*******************************************************************
	//  �ϰ���ǰ����Ƿ� �����ϱ�
	//*******************************************************************/	
	private String saveBatchDelivery(String gid,String login_id,String login_name,String mfg_no,String item_code,
		String factory_no,String factory_name) throws Exception
	{
		String data="P",where="",update="",input="",delivery_no="";

		//���� : �ϰ���ǰ����Ƿ� �������� ���� �˻��ϱ�
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		String order_status = mfgDAO.getColumData("MFG_MASTER","order_status",where);
		String order_type = mfgDAO.getColumData("MFG_MASTER","order_type",where);
		if(!order_status.equals("3")) {
			data = "��ǰ���� ���¿����� �����մϴ�.";
			return data;
		}

		//�Է�1 : ��������� ����Ƿڼ������� update�ϱ�
		//0:pid,1:reserve_count
		arrayMfgItemList(factory_no,mfg_no,item_code);		//������ ����Ʈ
		for(int i=0; i<an; i++) {
			update = "UPDATE mfg_item SET request_count='"+item[i][1]+"' where pid='"+item[i][0]+"'";
			//System.out.println("update : " + update);
			mfgDAO.executeUpdate(update);
		}

		//�Է�2 : ��ǰ����Ƿڼ� �Է��ϱ� (������ : ST_RESERVER_ITEM_INFO)
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:factory_no
		//��, ���������� 0���� Ŭ��츸 �����.
		
		//��ǰ����Ƿڹ�ȣ ���� (���屸�о��� ������)
		if(order_type.equals("MRP")) delivery_no = mfgDAO.getDeliveryNo();	//�����ȹ
		else delivery_no = mfgDAO.getDeliveryManNo();	//��޿���

		arrayMfgItemUniqueList(factory_no,mfg_no,item_code); //��ǰ�� ����Ʈ
		String[] div_info = new String[2];
		div_info = mfgDAO.getDivInfo(login_id);		//0:�μ��ڵ�, 1:�μ���
		for(int i=0; i<an; i++) {
			if(Integer.parseInt(item[i][5]) > 0) {
				input = "INSERT INTO st_reserved_item_info (delivery_no,item_code,item_name,item_desc,item_type,";
				input += "request_quantity,request_unit,request_date,factory_code,factory_name,ref_no,";
				input += "requestor_div_code,requestor_div_name,requestor_id,requestor_info,process_stat) values('";
				input += delivery_no+"','"+item[i][0]+"','"+item[i][1]+"','"+item[i][2]+"','"+item[i][4]+"','";
				input += item[i][5]+"','"+item[i][3]+"','"+anbdt.getDateNoformat()+"','"+factory_no+"','"+factory_name+"','";
				input += mfg_no+"','"+div_info[0]+"','"+div_info[1]+"','"+login_id+"','"+login_name+"','"+"S53"+"')";
				//System.out.println("input : " + input);
				mfgDAO.executeUpdate(input);
			}
		}

		//�Է�3 : ��ǰ������ �����Ϳ� �Է��ϱ� (MFG_REQ_MASTER)
		where = "where item_no='"+item_code+"'";
		String item_spec = mfgDAO.getColumData("item_master","item_desc",where);
		String pid = anbdt.getID();

		input = "INSERT INTO mfg_req_master (pid,gid,mfg_no,mfg_req_no,assy_code,assy_spec,";
		input += "level_no,req_status,req_date,req_div_code,req_div_name,req_user_id,req_user_name,";
		input += "factory_no,factory_name) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+delivery_no+"','"+item_code+"','";
		input += item_spec+"','"+""+"','"+"2"+"','"+anbdt.getDateNoformat()+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+login_id+"','"+login_name+"','"+factory_no+"','"+factory_name+"')";
		//System.out.println("input : " + input);
		mfgDAO.executeUpdate(input);

		//�Է�4 : MFG_MASTER�� ���� �ٲ��ֱ�
		update = "UPDATE mfg_master SET order_status='4' where pid='"+gid+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

		//�Է�5 : MFG_OPERATOR�� ���� �ٲ��ֱ�
		update = "UPDATE mfg_operator SET op_order='2' where gid='"+gid+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		�������� �� ���� �޼ҵ� ����
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MFG MASTER�� ������ �Է��ϱ�
	//*******************************************************************/	
	public String insertMfg(String mrp_no,String model_code,String model_name,String fg_code,String item_code,
		String item_name,String item_spec,String item_unit,String mfg_count,String buy_type,String factory_no,
		String factory_name,String comp_code,String comp_name,String comp_user,String comp_tel,
		String order_type,String reg_date,String reg_id,String reg_name,String plan_date,String order_start_date,
		String order_end_date,String re_work,String link_mfg_no) throws Exception
	{
		String input="",update="",data="",where="",gid="",mfg_no="";
		String mrp_status="",bom_status="";

		//��޵���� ����Ͽ� BOM������ FG����, �ش�BOM���� �ִ� ����ǰ���ڵ����� �˻��Ѵ�.
		//�ش�FG�ڵ尡 BOM�� ������ ǰ������ �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		if(!bom_status.equals("5")) {
			data = "BOM������ �ȵ� ���Դϴ�. ���� BOM�� ������ �����Ͻʽÿ�.";
			return data;
		}

		//�ش�ǰ���� BOM�� ������ ǰ�� ���Ե� ��ǰ �Ǵ� ����ǰ(ASSY�ڵ�)���� �Ǵ��ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM���հ����ڵ�
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "ǰ���ڵ�� FG�ڵ�� ������ BOM���� ��ǰ �Ǵ� ����ǰ�� ����� �� �ֽ��ϴ�.";
			return data;
		}
		
		//MFG������ȣ ���ϱ�
		mfg_no = mfgDAO.getMfgNo(factory_no);

		//MFG MASTER�� �Է��ϱ� 
		String pid = anbdt.getID();
		input = "INSERT INTO mfg_master (pid,mrp_no,mfg_no,model_code,model_name,fg_code,item_code,item_name,";
		input += "item_spec,item_unit,mfg_count,buy_type,factory_no,factory_name,comp_code,comp_name,";
		input += "comp_user,comp_tel,order_status,order_type,reg_date,reg_id,reg_name,plan_date,";
		input += "order_start_date,order_end_date,op_start_date,op_end_date,order_date,re_work,link_mfg_no) values('";
		input += pid+"','"+mrp_no+"','"+mfg_no+"','"+model_code+"','"+model_name+"','"+fg_code+"','";
		input += item_code+"','"+item_name+"','"+item_spec+"','"+item_unit+"','"+mfg_count+"','"+buy_type+"','";
		input += factory_no+"','"+factory_name+"','"+comp_code+"','"+comp_name+"','"+comp_user+"','"+comp_tel+"','";
		input += "1"+"','"+order_type+"','"+reg_date+"','"+reg_id+"','"+reg_name+"','"+plan_date+"','";
		input += order_start_date+"','"+order_end_date+"','"+""+"','"+""+"','"+""+"','"+re_work+"','"+link_mfg_no+"')";
		//System.out.println("input : " + input);
		mfgDAO.executeUpdate(input);

		//MRP MASTER�� ������� �ٲ��ֱ�
		update = "UPDATE mrp_master SET mfg_order='1' where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
		mfgDAO.executeUpdate(update);
		
		data = "���������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  MFG MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String updateMfg(String pid,String plan_date,String model_code,String model_name,String fg_code,
		String item_code,String item_name,String item_spec,String item_unit,String mfg_count,String buy_type,
		String factory_no,String factory_name,String comp_code,String comp_name,String comp_user,String comp_tel,
		String order_type,String reg_date,String order_start_date,String order_end_date,String re_work,String link_mfg_no) throws Exception
	{
		String data="",where="",order_status="",update="";

		//������°� �ۼ����϶��� ������ ������
		where = "where pid='"+pid+"'";
		order_status = mfgDAO.getColumData("MFG_MASTER","order_status",where);
		if(!order_status.equals("1")) {
			data = "�ۼ����϶��� ������ �����մϴ�.";
			return data;
		}
		
		//MFG MASTER�� �����ϱ�
		if(plan_date.length() !=0) {		//MRP������ ���� ���� 
			update = "UPDATE mfg_master SET comp_code='"+comp_code+"',comp_name='"+comp_name;
			update += "',comp_user='"+comp_user+"',comp_tel='"+comp_tel+"',reg_date='"+reg_date;
			update += "',order_start_date='"+order_start_date+"',order_end_date='"+order_end_date;
			update += "',buy_type='"+buy_type+"',re_work='"+re_work+"',link_mfg_no='"+link_mfg_no;
			update += "' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);
		} else {							//��޿��� ����
			update = "UPDATE mfg_master SET model_code='"+model_code+"',model_name='"+model_name;
			update += "',fg_code='"+fg_code+"',item_code='"+item_code+"',item_name='"+item_name;
			update += "',item_spec='"+item_spec+"',item_unit='"+item_unit+"',mfg_count='"+mfg_count;
			update += "',buy_type='"+buy_type+"',factory_no='"+factory_no+"',factory_name='"+factory_name;
			update += "',comp_code='"+comp_code+"',comp_name='"+comp_name+"',comp_user='"+comp_user;
			update += "',comp_tel='"+comp_tel+"',order_type='"+order_type+"',reg_date='"+reg_date;
			update += "',order_start_date='"+order_start_date+"',order_end_date='"+order_end_date;
			update += "',re_work='"+re_work+"',link_mfg_no='"+link_mfg_no+"' where pid='"+pid+"'";
			mfgDAO.executeUpdate(update);
		}
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// Mfg MASTER�� ������ �����ϱ�
	//*******************************************************************/	
	public String deleteMfg(String pid,String mrp_no,String order_status) throws Exception
	{
		String delete = "",update="",data="";
		
		//������� �˻�
		if(!order_status.equals("1")) {
			data = "�ۼ������϶��� ������ �����մϴ�.";
			return data;
		}
		
		//�޴��� �Է¸� ��������
		if(mrp_no.length() != 0) {
			data = "��޿����� ������ �����մϴ�.";
			return data;
		}

		//MFG MASTER�����ϱ�
		delete = "DELETE FROM mfg_master WHERE pid='"+pid+"'";
		mfgDAO.executeUpdate(delete);

		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}
	//--------------------------------------------------------------------
	//
	//		�������� ������(MFG OPERATOR) �� ���� �޼ҵ� ����
	//		
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ �������� �ϰ��Է��ϱ�
	// [MFG MASTER��Ͻ� ���ÿ� �����]
	//*******************************************************************/	
	private void insertMfgOperator(String gid,String mrp_no,String mfg_no,String mfg_count,String mfg_unit,
		String order_start_date,String order_end_date,String factory_no,String factory_name,String order_type,
		String fg_code,String item_code,String re_work) throws Exception
	{
		String pid = "",input="",where="",assy_spec="",op_no="";

		//Array list�� ������ �б�
		ArrayList item_list = new ArrayList();
		//MRP NO�� �ִ°�� : MRP���� 
		if(mrp_no.length() != 0) {
			item_list = mfgDAO.getAssyOpList(factory_no,mrp_no,order_type);
		} 
		//��޿����� ���躯���� ����Ͽ� mbom_item���� �����͸� ���Ѵ�.
		else {	
			if(re_work.equals("���۾�")) { 
				item_list = mfgDAO.getMbomAssyOp(fg_code,item_code);	//1���� ASSY�ڵ常
			} else {
				if(fg_code.equals(item_code)) item_list = mfgDAO.getMbomAssyOpList(fg_code);	//��ü��
				else item_list = getMbomAssyOpList(fg_code,item_code,order_start_date);			//�Ϻα�����
			
			}
		}

		//�����ϱ�
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			//�԰�,op_no ���ϱ�
			where = "where item_no='"+table.getAssyCode()+"'";
			assy_spec = mfgDAO.getColumData("ITEM_MASTER","item_desc",where);
			op_no = mfgDAO.getColumData("ITEM_MASTER","op_code",where);

			pid = anbdt.getNumID(n);
			//�����ϱ�
			input = "INSERT INTO mfg_operator (pid,gid,mfg_no,assy_code,assy_spec,level_no,mfg_count,mfg_unit,";
			input += "op_start_date,op_end_date,order_date,buy_type,factory_no,factory_name,work_no,work_name,";
			input += "op_no,op_name,mfg_id,mfg_name,note,comp_code,comp_name,comp_user,comp_tel) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+table.getAssyCode()+"','"+assy_spec+"','";
			input += table.getLevelNo()+"','"+mfg_count+"','"+mfg_unit+"','";
			input += order_start_date+"','"+order_end_date+"','"+""+"','"+""+"','";
			input += factory_no+"','"+factory_name+"','"+""+"','"+""+"','";
			input += op_no+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	//  MFG OPERATOR�� ������ �����ϱ�
	//*******************************************************************/	
	public String updateMfgOperator(String pid,String op_start_date,String op_end_date,String buy_type,
		String work_no,String work_name,String op_no,String op_name,String mfg_id,String mfg_name,
		String note,String comp_code,String comp_name,String comp_user,String comp_tel) throws Exception
	{
		String data="",where="",order_status="",update="";

		//MFG OPERATOR�� �����ϱ�
		update = "UPDATE mfg_operator SET op_start_date='"+op_start_date+"',op_end_date='"+op_end_date;
		update += "',buy_type='"+buy_type+"',work_no='"+work_no+"',work_name='"+work_name;
		update += "',op_no='"+op_no+"',op_name='"+op_name+"',mfg_id='"+mfg_id;
		update += "',mfg_name='"+mfg_name+"',note='"+note;
		update += "',comp_code='"+comp_code+"',comp_name='"+comp_name+"',comp_user='"+comp_user;
		update += "',comp_tel='"+comp_tel+"' where pid='"+pid+"'";
		mfgDAO.executeUpdate(update);
		
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// ���ذ��������� ���� ASSY�ڵ屸�ϱ�
	//*******************************************************************/	
	public ArrayList getMbomAssyOpList(String fg_code,String parent_code,String sel_date) throws Exception
	{

		String where="",gid="",level_no="";

		//�������� ���ϱ�
		where = "where fg_code='"+fg_code+"'";
		gid = mfgDAO.getColumData("MBOM_MASTER","pid",where);

		where = "where gid='"+gid+"' and parent_code='"+parent_code+"'";
		level_no = mfgDAO.getColumData("MBOM_ITEM","level_no",where);

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
			item[an][0] = table.getParentCode();
			item[an][1] = table.getLevelNo();
			an++;
		}

		//����
		sort.bubbleSortStringMultiAsc(item,1);

		//Array list�� ���
		ArrayList assy_list = new ArrayList();
		mrpItemTable assy = null;
		for(int i=0; i<an-1; i++) {
			if(!item[i][1].equals(item[i+1][1])) {
				assy = new mrpItemTable();	
				assy.setMrpNo("");
				assy.setAssyCode(item[i][0]);
				assy.setLevelNo(Integer.parseInt(item[i][1]));
				//System.out.println(item[i][0]+":"+item[i][1]); 
				assy_list.add(assy);
			}
		}
		assy = new mrpItemTable();	
		assy.setMrpNo("");
		assy.setAssyCode(item[an-1][0]);
		assy.setLevelNo(Integer.parseInt(item[an-1][1]));
		//System.out.println(item[an-1][0]+":"+item[an-1][1]); 
		assy_list.add(assy);
		

		return assy_list;
	}

	//--------------------------------------------------------------------
	//
	//		ǰ��ҿ䷮����(MFG ITEM) �� ���� �޼ҵ� ����
	//		
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MFG������ MFG ITEM�� �ҿ䷮ �ϰ��Է��ϱ�
	// [MFG MASTER��Ͻ� ���ÿ� �����]
	//*******************************************************************/	
	private void insertMfgItem(String gid,String mrp_no,String mfg_no,String item_code,
		String order_start_date,String factory_no,String order_type,String fg_code,
		String mrp_count,String re_work) throws Exception
	{
		String input = "",pid = "",level_no="",where="",spare_count="0",reserve_count="",add_count="0";

		//level_no ���ϱ�
		where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"' and assy_code='"+item_code+"'";
		level_no = mfgDAO.getColumData("MRP_ITEM","level_no",where);	

		//MRP ITEM�� ��ǰ������ �����´�.
		ArrayList item_list = new ArrayList();
		//MRP NO�� �ִ°�� : MRP���� 
		if(mrp_no.length() != 0) {
			item_list = mfgDAO.getMrpItemList(factory_no,mrp_no,level_no,item_code,order_type);	
		} 
		//��޿����� ���躯���� ����Ͽ� mbom_item���� �����͸� ���Ѵ�.
		else {		
			if(re_work.equals("�۾�")) {		//�ٴܰ�
				item_list = mrpBO.sortArrayStrList(fg_code,item_code,order_start_date,mrp_count,"0",factory_no);
			} else {							//�ܴܰ� (���۾����� �ش������ ����Ѵ�.)
				item_list = mrpBO.sortArraySingleStrList(fg_code,item_code,order_start_date,mrp_count,"0",factory_no);
			}
		}

		//���̺� �����ϱ�
		mrpItemTable table = new mrpItemTable();
		Iterator item_iter = item_list.iterator();
		int n=0;
		while(item_iter.hasNext()) {
			table = (mrpItemTable)item_iter.next();
			pid = anbdt.getNumID(n);

			//�����ܰ�Table���� �ش�ǰ���� �ܰ������������ : 2004.6.8 ������������
			//where = "where item_code='"+table.getItemCode()+"' and factory_no='"+factory_no+"'";
			//spare_count = mfgDAO.getColumData("MFG_STOCK_ITEM","stock_count",where);
			//if(spare_count.length() == 0) spare_count = "0";

			//����డ�ɼ��� ���ϱ�(need_count - spare_count);
			//�����ܰ� ����Ͽ� ������� ����
			//reserve_count = Integer.toString(table.getNeedCount()-Integer.parseInt(spare_count));
			
			//�����ܰ�� �����������ϰ� ����� �ʿ������ ����������� �Ҷ�
			//item type�� ����Ͽ� (4:���������϶�)
			if(table.getItemType().equals("4")) {
				add_count = "0";
				reserve_count = Integer.toString(table.getNeedCount());
			} else {
				add_count = Integer.toString(table.getNeedCount() * -1);
				reserve_count = "0";
			}

			//�����ϱ�
			input = "INSERT INTO mfg_item (pid,gid,mfg_no,assy_code,level_no,item_code,item_name,";
			input += "item_spec,item_unit,item_type,item_loss,draw_count,mfg_count,need_count,spare_count,";
			input += "add_count,reserve_count,request_count,need_date,order_date,factory_no,factory_name) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+table.getAssyCode()+"','";
			input += table.getLevelNo()+"','"+table.getItemCode()+"','"+table.getItemName()+"','";
			input += table.getItemSpec()+"','"+table.getItemUnit()+"','"+table.getItemType()+"','"+"0"+"','";
			input += table.getDrawCount()+"','"+table.getMrpCount()+"','"+table.getNeedCount()+"','";
			input += spare_count+"','"+add_count+"','"+reserve_count+"','"+"0"+"','";
			input += order_start_date+"','"+""+"','"+factory_no+"','"+table.getFactoryName()+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
			n++;
		}
	}

	//*******************************************************************
	//  MFG ITEM �ҿ䷮���� �ϱ� 
	// need_count : ������ʿ��� ���� , add_count : �߰��� ����
	//*******************************************************************/	
	public String updateMfgItem(String pid,String need_count,String add_count) throws Exception
	{
		String data="",where="",reserve_count="",update="";

		//������� ����ϱ�
		reserve_count = Integer.toString(Integer.parseInt(need_count) + Integer.parseInt(add_count));

		//MFG OPERATOR�� �����ϱ�
		update = "UPDATE mfg_item SET add_count='"+add_count+"',reserve_count='"+reserve_count;
		update += "' where pid='"+pid+"'";
		mfgDAO.executeUpdate(update);
		
		data = "���������� ���� �Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	// MFG ITEM �ٴܰ� ������ �о� �迭�� ��� 
	//*******************************************************************/	
	public void arrayMfgItemList(String factory_no,String mfg_no,String item_code) throws Exception
	{

		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���
		//--------------------------------------------
		String where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mfgDAO.getColumData("MFG_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mfgDAO.getMfgItemList(factory_no,mfg_no,level_no,item_code);
		int cnt = item_list.size();
		if(cnt == 0) return;
		item = new String[cnt][2];

		//���ý��� ������ �迭�� ���
		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = Integer.toString(table.getReserveCount());
			an++;
		}
	}

	//*******************************************************************
	// MFG ITEM �ٴܰ� ������ �о� �迭�� ��� : ������ ��ǰ���� ����
	//*******************************************************************/	
	public void arrayMfgItemUniqueList(String factory_no,String mfg_no,String item_code) throws Exception
	{

		//--------------------------------------------
		//Array list�� ������ �о� �迭�� ���
		//--------------------------------------------
		String where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String level_no = mfgDAO.getColumData("MFG_ITEM","level_no",where);

		ArrayList item_list = new ArrayList();
		item_list = mfgDAO.getMfgItemList(factory_no,mfg_no,level_no,item_code);
		int cnt = item_list.size();
		if(cnt == 0) return;
		String[][] data = new String[cnt][7];

		//���ý��� ������ �迭�� ���
		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			data[n][0] = table.getItemCode();
			data[n][1] = table.getItemName();
			data[n][2] = table.getItemSpec();
			data[n][3] = table.getItemUnit();
			data[n][4] = table.getItemType();
			data[n][5] = Integer.toString(table.getReserveCount());
			data[n][6] = table.getFactoryNo();
			n++;
		}
		//������ ���� �����ϱ�
		sort.bubbleSortStringMultiAsc(data,0);

		//---------------------------------------------------
		// ������ ��ǰ���� �����ϱ�
		//---------------------------------------------------
		item = new String[n][7];

		//���ο� �迭�� ��� 
		int item_cnt = n -1;
		int q=0; //�������	
		an = 0;	 //�迭�� ����
		for(int i=0; i<=item_cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < item_cnt) {
				//��ǰ���ڵ尡 ������ ���ڸ� ���
				if(data[i][0].equals(data[i+1][0])) {	
					q += Integer.parseInt(data[i][5]);
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					q += Integer.parseInt(data[i][5]);
					item[an][0] = data[i][0];			//item code
					item[an][1] = data[i][1];			//item name
					item[an][2] = data[i][2];			//item spec
					item[an][3] = data[i][3];			//item unit
					item[an][4] = data[i][4];			//item type
					item[an][5] = Integer.toString(q);	//��������
					item[an][6] = data[i][6];			//�����ȣ
					an++;
					q=0;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				q += Integer.parseInt(data[i][5]);
				item[an][0] = data[i][0];			//item code
				item[an][1] = data[i][1];			//item name
				item[an][2] = data[i][2];			//item spec
				item[an][3] = data[i][3];			//item unit
				item[an][4] = data[i][4];			//item type
				item[an][5] = Integer.toString(q);	//��������
				item[an][6] = data[i][6];			//�����ȣ
				an++;
			} //else
		} //for

		//���Test
		//for(int i=0; i<an; i++) {
		//	System.out.println(item[i][0]+":"+item[i][4]+":"+item[i][5]+":"+item[i][6]);
		//}

	}

	//--------------------------------------------------------------------
	//
	//		���� �����ϱ� (�������:mfg_inout_master, ������:st_item_stock_master)
	//	     
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// MFG ITEM �ٴܰ� ������ �о� �ش�Table�� ���������ϱ�
	//*******************************************************************/	
	public void reserveMfgItem(String gid,String mfg_no,String item_code,String factory_no,String factory_name) throws Exception
	{
		//�ʱ�ȭ
		String where="",input="",update="",pid="",outto_quantity="",stock_quantity="";

		//�ش系���� ��ǰ�� ����迭�� ��ǰ�� �����Ͽ� ���
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:factory_no
		arrayMfgItemUniqueList(factory_no,mfg_no,item_code);

		//��������� ���೻�� �����ϱ� : �������:mfg_inout_master
        for(int i=0; i<an; i++) {
			pid = anbdt.getNumID(i);
			input = "INSERT INTO mfg_inout_master (pid,gid,mfg_no,item_code,item_name,item_spec,item_unit,";
			input += "inout_status,reserve_count,factory_no,factory_name) values('";
			input += pid+"','"+gid+"','"+mfg_no+"','"+item[i][0]+"','"+item[i][1]+"','";
			input += item[i][2]+"','"+item[i][3]+"','"+"0"+"','";
			input += item[i][5]+"','"+factory_no+"','"+factory_name+"')";
			//System.out.println("input : " + input);
			mfgDAO.executeUpdate(input);
		}

		//�������� ���೻�� UPDATE�ϱ� : ������:st_item_stock_maste
		for(int i=0; i<an; i++) {
			//-----UPDATE�� ��������� ���ϱ� -----//
			//������ ��������� ���ϱ�
			where = "where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
			outto_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","outto_quantity",where);
			if(outto_quantity.length() == 0) outto_quantity = "0";

			//��������� : ������������� + ��������
			outto_quantity = Integer.toString(Integer.parseInt(outto_quantity)+Integer.parseInt(item[i][5]));

			//-----UPDATE�� ������ ���ϱ� -----//
			//������ ������ ���ϱ�
			stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
			if(stock_quantity.length() == 0) stock_quantity = "0";

			//������ : ���������� - ���������
			stock_quantity = Integer.toString(Integer.parseInt(stock_quantity)-Integer.parseInt(item[i][5]));

			update = "UPDATE st_item_stock_master SET outto_quantity='"+outto_quantity;
			update += "',stock_quantity='"+stock_quantity;
			update += "' where item_code='"+item[i][0]+"' and factory_code='"+factory_no+"'";
			//System.out.println("update : " + update);
			mfgDAO.executeUpdate(update);
		}

	}

}


