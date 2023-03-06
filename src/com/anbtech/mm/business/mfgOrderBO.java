package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgOrderBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//�����ϱ�
	private com.anbtech.mm.db.mfgModifyDAO mfgDAO = null;	
	private com.anbtech.mm.db.mfgOrderDAO odrDAO = null;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private String query = "";
	private String delivery_no="";				//��ǰ����Ƿڹ�ȣ

	private String[][] item = null;				//�迭
	private int an = 0;							//items�� �迭 ����

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mfgOrderBO(Connection con) 
	{
		this.con = con;
		mfgDAO = new com.anbtech.mm.db.mfgModifyDAO(con);
		odrDAO = new com.anbtech.mm.db.mfgOrderDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		��ǰ����Ƿڼ� ��Ͽ� ���� �޼ҵ�
	//			���/����/����/���°��� 
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  ��ǰ����Ƿ� �ʱ�ȭ ����ϱ� : out_create
	//*******************************************************************/	
	public String saveInitDelivery(String login_id,String login_name,String gid,String mfg_no,
		String assy_code,String level_no,String assy_spec,String factory_no,String factory_name) throws Exception
	{
		String data="",where="",update="",input="";

		//����1 : ��ǰ����Ƿ� �������� ���� �˻��ϱ� : mfg_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		String order_status = odrDAO.getColumData("MFG_MASTER","order_status",where);
		String order_type = odrDAO.getColumData("MFG_MASTER","order_type",where);
		if(order_status.equals("6")) {
			data = "������� ���������Դϴ�.";
			return data;
		}

		//����2 : �ش������ ������¸� �˻��Ѵ�. : mfg_operator
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and assy_code='"+assy_code+"'";
		String op_order = odrDAO.getColumData("MFG_OPERATOR","op_order",where);
		if(!op_order.equals("1")) {
			if(op_order.equals("2")) data = "��ǰ����� �����Դϴ�.";
			else if(op_order.equals("3")) data = "������� ���������Դϴ�.";
			return data;
		}

		//����3 : �ش������ �ش�Ǵ� ��ǰ����Ƿڰ� ������������ ���� �ִ��� �Ǵ��Ѵ�. : mfg_req_master
		String req_status = odrDAO.checkProcess(mfg_no,assy_code,factory_no);
		if(req_status.equals("F")) {
			data = "��ǰ����Ƿ� �ۼ����� ������ �ֽ��ϴ�.";
			return data;
		}

		//�غ�1 : ������ MFG ITEM�� �迭�� ��� : �����Ͱ� �ִ���, ��ü�� ����Ƿ��� ��ǰ�� �ִ���
		data = arrayMfgItemList(mfg_no,assy_code,factory_no);
		if(data.length() != 0) {
			//������� ��ǰ�� ���°�� mfg_operator���¸� ���縶��(op_order='2')���� �ٲ��ش�.
			//������ ��������ϱ����� (���¸� �ٲ����� ������ �������(JSP����)�� �� �� ��������)
			update = "UPDATE mfg_operator SET op_order='2' where mfg_no='"+mfg_no+"' and ";
			update += "assy_code='"+assy_code+"' and factory_no='"+factory_no+"'";
			odrDAO.executeUpdate(update);

			return data; 
		}

		//���1 : ��ǰ����Ƿ� ���̺� �����ϱ� : st_reserved_item_info
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:reserve_count,6:request_count,7:�Ƿڰ��ɼ���,8:factory_no,9:factory_name
		
		//��ǰ����Ƿڹ�ȣ ���� (���屸�о��� ������)
		if(order_type.equals("MRP")) delivery_no = mfgDAO.getDeliveryNo();	//�����ȹ
		else delivery_no = mfgDAO.getDeliveryManNo();	//��޿���

		//��ǰ����Ƿ� �ʱ��ۼ����·� ���
		String[] div_info = new String[2];
		div_info = mfgDAO.getDivInfo(login_id);		//0:�μ��ڵ�, 1:�μ���
		for(int i=0; i<an; i++) {
			if(Integer.parseInt(item[i][7]) > 0) {
				input = "INSERT INTO st_reserved_item_info (delivery_no,item_code,item_name,item_desc,item_type,";
				input += "request_quantity,request_unit,request_date,factory_code,factory_name,ref_no,";
				input += "requestor_div_code,requestor_div_name,requestor_id,requestor_info,process_stat) values('";
				input += delivery_no+"','"+item[i][0]+"','"+item[i][1]+"','"+item[i][2]+"','"+item[i][4]+"','";
				input += item[i][7]+"','"+item[i][3]+"','"+anbdt.getDateNoformat()+"','"+factory_no+"','"+factory_name+"','";
				input += mfg_no+"','"+div_info[0]+"','"+div_info[1]+"','"+login_id+"','"+login_name+"','"+"S50"+"')";
				//System.out.println("input : " + input);
				odrDAO.executeUpdate(input);
			}
		}

		//���2 : ��ǰ������ �����Ϳ� �Է��ϱ� (MFG_REQ_MASTER)
		String pid = anbdt.getID();
		input = "INSERT INTO mfg_req_master (pid,gid,mfg_no,mfg_req_no,assy_code,assy_spec,";
		input += "level_no,req_status,req_date,req_div_code,req_div_name,req_user_id,req_user_name,";
		input += "factory_no,factory_name) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+delivery_no+"','"+assy_code+"','";
		input += assy_spec+"','"+level_no+"','"+"1"+"','"+anbdt.getDateNoformat()+"','"+div_info[0]+"','";
		input += div_info[1]+"','"+login_id+"','"+login_name+"','"+factory_no+"','"+factory_name+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//���3 : MFG_MASTER�� ���� �ٲ��ֱ�
		if(order_status.equals("3")) {
			update = "UPDATE mfg_master SET order_status='4' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		//��ǰ����Ƿڹ�ȣ SETTING �ϱ�
		setDeliveryNo(delivery_no);

		data = "��ǰ����Ƿ� �ʱ�ȭ������ ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  ��ǰ����Ƿ� ��û�ϱ� : out_confirm
	//*******************************************************************/	
	public String confirmDelivery(String mfg_no,String mfg_req_no,String assy_code,
		String level_no,String factory_no) throws Exception
	{
		String data="",where="",update="",input="";
		String reserve_count="",request_count="";
		int cnt = 0;

		//�غ�1 : ��ǰ��� �ۼ����� ������ �迭[dev_item],[dev_an]�� ���: st_reserved_item_info
		//0:pid, 1:mfg_no, 2:mfg_req_no, 3:item_code, 4:item_name, 5:item_spec
		//6:item_type, 7:item_unit, 8:req_count, 9:factory_no
		data = arrayMfgReqItemList(mfg_req_no,factory_no);
		if(data.length() != 0) return data;

		//���1 : MFG_ITEM�� ����Ƿڼ���[request_count]�� update�Ѵ�.
		for(int i=0; i<an; i++) {
			//���ϵ� ����Ƿڼ����� ã�´�.
			where = "where item_code='"+item[i][3]+"' and factory_no='"+factory_no+"' ";
			where +="and mfg_no='"+mfg_no+"' and assy_code='"+assy_code+"' and level_no='"+level_no+"'";
			reserve_count = mfgDAO.getColumData("MFG_ITEM","reserve_count",where);		//�������
			request_count = mfgDAO.getColumData("MFG_ITEM","request_count",where);		//������Ƿڼ���
			if(reserve_count.length() == 0) reserve_count = "0";
			if(request_count.length() == 0) request_count = "0";

			//sum ����Ƿڼ��� : ������Ƿڼ���[request_count] + ������Ƿڼ���[dev_item[][8]
			request_count = Integer.toString(Integer.parseInt(request_count) + Integer.parseInt(item[i][8]));

			//�������� ��������� ������ �ľ��Ѵ�. : ������ ��� �Ϸ�Ǿ����� �˸������� : MFG_OPERATOR
			if(reserve_count.equals(request_count)) cnt++;

			//�������� update�Ѵ�.
			update = "UPDATE mfg_item SET request_count='"+request_count+"' "+where;
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);

		}

		//���2 : ��ǰ����Ƿڻ������� �˸��� : st_reserved_item_info
		update = "UPDATE st_reserved_item_info SET process_stat='S53' where delivery_no='"+mfg_req_no+"' ";
		update += "and factory_code='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//���3 : ��ǰ����Ƿ� ���¸� ��ǰ����Ƿڸ����Ϳ� �˸��� : mfg_req_master
		update = "UPDATE mfg_req_master SET req_status='2' where mfg_req_no='"+mfg_req_no+"' ";
		update += "and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//���4 : �ش������ ��ǰ����Ƿڸ� ���̻� ������ �� �ִ����� �Ǵ��Ѵ�.
		if(an == cnt) {
			update = "UPDATE mfg_operator SET op_order='2' where mfg_no='"+mfg_no+"' ";
			update += "and assy_code='"+assy_code+"' and level_no='"+level_no+"' and factory_no='"+factory_no+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}
		data = "���������� ��ǰ��û�Ƿڰ� ����Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  ��ǰ����Ƿ� ����Ƿڼ��� �����ϱ� 
	//*******************************************************************/	
	public String saveDeliveryItem(String pid,String req_count) throws Exception
	{
		String data="",update="",where="",outto_quantity="",stock_quantity="",was_req_count="0";
		String item_code="",factory_no="";

/*		//������ ��ǰ����Ƿ� ���� ���ϱ�
		where = "where mid='"+pid+"'";
		was_req_count = mfgDAO.getColumData("st_reserved_item_info","request_quantity",where);
		item_code = mfgDAO.getColumData("st_reserved_item_info","item_code",where);
		factory_no = mfgDAO.getColumData("st_reserved_item_info","factory_code",where);
		
		//��ǰ��� ������
		//-----UPDATE�� ��������� ���ϱ� -----//
		//������ ��������� ���ϱ�
		where = "where item_code='"+item_code+"' and factory_code='"+factory_no+"'";
		outto_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","outto_quantity",where);
		if(outto_quantity.length() == 0) outto_quantity = "0";

		//��������� : ������������� - ������ ��ǰ����Ƿ� ���� + ��������
		outto_quantity = Integer.toString(Integer.parseInt(outto_quantity)-Integer.parseInt(was_req_count)+Integer.parseInt(req_count));

		//-----UPDATE�� ������ ���ϱ� -----//
		//������ ������ ���ϱ�
		stock_quantity = mfgDAO.getColumData("ST_ITEM_STOCK_MASTER","stock_quantity",where);
		if(stock_quantity.length() == 0) stock_quantity = "0";

		//������ : ���������� + ������ ��ǰ����Ƿ� ���� - ���������
		stock_quantity = Integer.toString(Integer.parseInt(stock_quantity)+Integer.parseInt(was_req_count)-Integer.parseInt(req_count));

		update = "UPDATE st_item_stock_master SET outto_quantity='"+outto_quantity;
		update += "',stock_quantity='"+stock_quantity;
		update += "' where item_code='"+item_code+"' and factory_code='"+factory_no+"'";
		//System.out.println("update : " + update);
		mfgDAO.executeUpdate(update);

*/
		//��ǰ����Ƿ� ������ 
		update = "UPDATE st_reserved_item_info SET request_quantity='"+req_count+"' where mid='"+pid+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		data = "���������� �����Ǿ����ϴ�.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		������� ������ ���� �޼ҵ� : mfg_product_master , mfg_product_item
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//  ������� ������ �ʱ�ȭ ����ϱ� 
	//*******************************************************************/	
	public String saveInitProduct(String login_id,String login_name,String mfg_no,
		String assy_code,String level_no,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",pid="",gid="";

		//����1 : ���������� ��Ͽ��� �Ǵ� : mfg_product_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		where += " and item_code='"+assy_code+"' and mfg_id='"+login_id+"'";
		String output_status = odrDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		if(output_status.length() != 0) return data;
		

		//���1 : ���������͸� ����Ѵ�. : mfg_product_master
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		where += " and assy_code='"+assy_code+"' and level_no='"+level_no+"'";
		pid = odrDAO.getColumData("MFG_OPERATOR","pid",where);
		mfgOperatorTable table = new mfgOperatorTable();
		table  = odrDAO.readMfgOperator(pid);

		String[] part_info = new String[2];
		part_info = odrDAO.getItemInfo(table.getAssyCode());			//�̸�,�԰�
		pid = anbdt.getID();

		where = "where m_code='"+table.getOpNo()+"'";
		String nickname = odrDAO.getColumData("MBOM_ENV","tag",where);	//op code ���

		com.anbtech.mm.entity.mfgMasterTable mfgT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgT = odrDAO.readMfgMasterItem(mfg_no,factory_no);

		input = "INSERT INTO mfg_product_master (pid,gid,mfg_no,model_code,model_name,fg_code,";
		input += "item_code,item_name,item_spec,order_count,order_unit,mfg_id,mfg_name,output_status,";
		input += "factory_no,output_date,op_code,op_name,op_nickname) values('";
		input += pid+"','"+table.getGid()+"','"+table.getMfgNo()+"','"+mfgT.getModelCode()+"','"+mfgT.getModelName()+"','";
		input += mfgT.getFgCode()+"','"+table.getAssyCode()+"','"+part_info[0]+"','";
		input += part_info[1]+"','"+table.getMfgCount()+"','"+table.getMfgUnit()+"','"+login_id+"','"+login_name+"','";
		input += "1"+"','"+factory_no+"','"+anbdt.getDateNoformat()+"','"+table.getOpNo()+"','";
		input += table.getOpName()+"','"+nickname+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//���2 : ����������� �ٲ��ֱ� : mfg_master
		//�ش� ������ ��ǰ�������� ������ ������.
		query = "select count(*) from mfg_req_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and ";
		query +="req_status != '1'";
		int cnt = odrDAO.getTotalCount(query);
		if(cnt != 0) {
			update = "UPDATE mfg_master SET order_status='5' where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		return data;
	}

	//*******************************************************************
	//  ������ ������� ����ϱ� : product_save
	//*******************************************************************/	
	public String saveProductItem(String login_id,String login_name,String gid,String mfg_no,String item_code,
		String item_name,String item_spec,String total_count,String good_count,String bad_count,
		String bad_type,String bad_note,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",pid="",used_cnt="",op_no ="";
		String delivery_quantity = "";		//������
		int use_count = 0;					//�������� �ҿ�� ����
		int rev_count = 0;					//�������
		int rst_total_count=0,rst_good_count=0,rst_bad_count=0,working_count=0;

		//����1 : �۾����� ���¸� �˻����� ���ο��� ������ �Ǵ��Ѵ�.
		where = "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String op_order = odrDAO.getColumData("MFG_OPERATOR","op_order",where);

		if(op_order.equals("1")) {		//�۾����� ����
			//����1-1 : ��ǰ����Ƿڽ�û�� ���� �ִ��� �Ǵ� : mfg_req_master
			query = "select count(*) from mfg_req_master where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"' ";
			query += "and factory_no='"+factory_no+"'";
			if(odrDAO.getTotalCount(query) == 0) {
				data = "��ǰ����Ƿ��� ��������� �����մϴ�.";
				return data;
			}
		} else if(op_order.equals("3")) {	//��������
			data = "�������� �����Դϴ�.";
			return data;
		}

		//����2 : ���ϵ���� ������ �ִ��� �Ǵ��ϱ� (2�ߵ�� ����)
		where = "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"' and factory_no='"+factory_no+"'";
		String output_date = odrDAO.getColumData("MFG_PRODUCT_ITEM","output_date",where);
		if(output_date.equals(anbdt.getDateNoformat())) {
			data = "���ϵ���� ������ �ֽ��ϴ�.  ������ �����Ͻÿ�.";
			return data;
		}

		//�غ�1 : ������ MFG ITEM�� �迭�� ��� : ����ǰ���� �ش���ǰ�� ������ŭ �����ϱ�����
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:draw_count,6:reserve_count,7:request_count,8:factory_no,9:factory_name
		data = getMfgItemList(mfg_no,item_code,factory_no);
		if(data.length() != 0) return data; 

		//�غ�2 : ������� ������ ���� �б� : mfg_product_master
		com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
		productMT = odrDAO.readMfgProductMasterRead(mfg_no,item_code,factory_no);
		if(productMT.getPid().length() == 0) {
			data = "��������� ���������� �����Ͱ� �����ϴ�. [mfg_product_master]"; return data;
		}

		//���1 [�������]: ������� ��ǰ��� ���̺� �����ϱ� : mfg_product_item
		where = "where item_no='"+item_code+"'";
		op_no = odrDAO.getColumData("ITEM_MASTER","op_code",where);
		pid = anbdt.getID();
		input = "INSERT INTO mfg_product_item (pid,gid,mfg_no,item_code,item_name,item_spec,";
		input += "total_count,good_count,bad_count,mfg_id,mfg_name,output_date,op_no,";
		input += "bad_type,bad_note,output_status,factory_no) values('";
		input += pid+"','"+gid+"','"+mfg_no+"','"+item_code+"','"+item_name+"','"+item_spec+"','";
		input += total_count+"','"+good_count+"','"+bad_count+"','"+login_id+"','"+login_name+"','";
		input += anbdt.getDateNoformat()+"','"+op_no+"','"+bad_type+"','"+bad_note+"','"+"1"+"','"+factory_no+"')";
		//System.out.println("input : " + input);
		odrDAO.executeUpdate(input);

		//���2 [�������]: ������� ��ǰ��� ������ ���̺� �����ϱ� : mfg_product_master
		rst_total_count = productMT.getTotalCount() + Integer.parseInt(total_count);
		rst_good_count = productMT.getGoodCount() + Integer.parseInt(good_count);
		rst_bad_count = productMT.getBadCount() + Integer.parseInt(bad_count);

		update = "UPDATE mfg_product_master SET total_count='"+rst_total_count+"',";
		update += "good_count='"+rst_good_count+"',bad_count='"+rst_bad_count+"',";
		update += "output_date='"+output_date+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);
		

		//���3 [��ǰ����]: �������� ������ ���̺� ����: st_item_stock_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//�������
			if(rev_count > 0) {	
				//���� ������ ����� ���� ���ϱ�
				where = "where item_code='"+item[i][0]+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				//�������� ����� ���� ���ϱ� : BOM���� * ��������
				use_count = Integer.parseInt(item[i][5]) * Integer.parseInt(total_count);
				if(use_count > rev_count) use_count = rev_count;		//��������� ��������.

				//updte�� ���� (������� - �����ҿ䷮)
				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - use_count);

				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][0]+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//���4 [��ǰ����]: ��������� ������ ���̺� ���������� ���ϱ�: mfg_inout_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//�������
			if(rev_count > 0) {	
				//���� �������� ���ϱ�
				where = "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				where += "factory_no='"+factory_no+"'";
				used_cnt = odrDAO.getColumData("MFG_INOUT_MASTER","use_count",where);
				if(used_cnt.length() == 0) used_cnt = "0";

				//�������� ����� ���� ���ϱ� : BOM���� * ��������
				use_count = Integer.parseInt(item[i][5]) * Integer.parseInt(total_count);
				if(use_count > rev_count) use_count = rev_count;		//��������� ��������.

				//updte�� ���� (���������� + �����ҿ䷮)
				use_count = Integer.parseInt(used_cnt) + use_count;

				update = "UPDATE mfg_inout_master SET use_count='"+use_count+"' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				update += "factory_no='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//���5 [�������]: ���� �ٲ��ֱ� (��,������ǰ���� ����� ���� ������ ���� �Է��Ѵ�.)
		com.anbtech.mm.entity.mfgMasterTable mfgMT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgMT = odrDAO.readMfgMasterItem(gid);
		if(mfgMT.getItemCode().equals(item_code)) {		//������ǰ�����ڵ�
			rst_total_count = mfgMT.getRstTotalCount() + Integer.parseInt(total_count);
			rst_good_count = mfgMT.getRstGoodCount() + Integer.parseInt(good_count);
			rst_bad_count = mfgMT.getRstBadCount() + Integer.parseInt(bad_count);
			working_count = mfgMT.getMfgCount() - rst_total_count; 

			update = "UPDATE mfg_master SET rst_total_count='"+rst_total_count+"',";
			update += "rst_good_count='"+rst_good_count+"',rst_bad_count='"+rst_bad_count+"',";
			update += "working_count='"+working_count+"' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		update = "UPDATE mfg_master SET order_status='5' ";
		update += "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		data = "��������� ��ϵǾ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  ������ ������� �����ϱ� : product_modify
	//*******************************************************************/	
	public String updateProductItem(String gid,String pid,String mfg_no,String item_code,String total_count,
		String good_count,String bad_count,String bad_type,String bad_note,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",used_cnt="";
		String delivery_quantity = "";		//������
		int use_count = 0;					//�������� �ҿ�� ����
		int rev_count = 0;					//�������
		int diff_total_count = 0;			//�����ѻ������ - ������ �������
		int diff_good_count = 0;			//�����Ѿ�ǰ���� - ������ ��ǰ����
		int diff_bad_count = 0;				//�����Ѻҷ����� - ������ �ҷ�����
		int rst_total_count=0,rst_good_count=0,rst_bad_count=0,working_count=0;
		if(bad_count.length() == 0) bad_count = "0";

		//�غ�1 : ������ MFG ITEM�� �迭�� ��� : ����ǰ���� �ش���ǰ�� ������ŭ �����ϱ�����
		//0:item_code,1:item_name,2:item_spec,3:item_unit
		//4:item_type,5:draw_count,6:reserve_count,7:request_count,8:factory_no,9:factory_name
		data = getMfgItemList(mfg_no,item_code,factory_no);
		if(data.length() != 0) return data; 

		//�غ�2 : ������� ������ ���� �б� : mfg_product_master
		com.anbtech.mm.entity.mfgProductMasterTable productMT = new com.anbtech.mm.entity.mfgProductMasterTable();
		productMT = odrDAO.readMfgProductMasterRead(mfg_no,item_code,factory_no);
		if(productMT.getPid().length() == 0) {
			data = "��������� ���������� �����Ͱ� �����ϴ�. [mfg_product_master]"; return data;
		}

		//�غ�3 : ������ϵ� �ѻ�������� ������ �ѻ�������� ���̸� ���Ѵ�.
		//���̸�ŭ ��ǰ������ �����Ѵ�.
		com.anbtech.mm.entity.mfgProductItemTable productIT = new com.anbtech.mm.entity.mfgProductItemTable();
		productIT = odrDAO.readMfgProductItemRead(pid);
		diff_total_count = Integer.parseInt(total_count) - productIT.getTotalCount();	//+:�߰�,-:�谨 �ǹ�
		diff_good_count = Integer.parseInt(good_count) - productIT.getGoodCount();		//+:�߰�,-:�谨 �ǹ�
		diff_bad_count = Integer.parseInt(bad_count) - productIT.getBadCount();			//+:�߰�,-:�谨 �ǹ�
		//System.out.println(diff_total_count+" : "+diff_good_count+" : "+diff_bad_count);
		
		//���1 [�������] : ������� ��ǰ��� ���̺� �����ϱ� : mfg_product_item
		update = "UPDATE mfg_product_item SET total_count='"+total_count+"',";
		update += "good_count='"+good_count+"',bad_count='"+bad_count+"',";
		if(bad_count.equals("0")) {		//�ҷ������� �ִٰ� �������� ������� �ҷ����¹� ������ ����
			update += "bad_type='',bad_note='' where pid='"+pid+"'";
		} else {
			update += "bad_type='"+bad_type+"',bad_note='"+bad_note+"' where pid='"+pid+"'";
		}
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//���2 [�������] : ������� ��ǰ��� ������ ���̺� �����ϱ� : mfg_product_master
		rst_total_count = productMT.getTotalCount() + diff_total_count;
		rst_good_count = productMT.getGoodCount() + diff_good_count;
		rst_bad_count = productMT.getBadCount() + diff_bad_count;

		update = "UPDATE mfg_product_master SET total_count='"+rst_total_count+"',";
		update += "good_count='"+rst_good_count+"',bad_count='"+rst_bad_count+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("update : " + update);
		odrDAO.executeUpdate(update);

		//���3 [�������] : ������ǰ���� ����� ���� ������ ���� �����Ѵ�. : mfg_master
		com.anbtech.mm.entity.mfgMasterTable mfgMT = new com.anbtech.mm.entity.mfgMasterTable();
		mfgMT = odrDAO.readMfgMasterItem(gid);
		if(mfgMT.getItemCode().equals(item_code)) {		//������ǰ�����ڵ�
			rst_total_count = mfgMT.getRstTotalCount() + diff_total_count;
			rst_good_count = mfgMT.getRstGoodCount() + diff_good_count;
			rst_bad_count = mfgMT.getRstBadCount() + diff_bad_count;
			working_count = mfgMT.getMfgCount() - rst_total_count; 

			update = "UPDATE mfg_master SET rst_total_count='"+rst_total_count+"',";
			update += "rst_good_count='"+rst_good_count+"',rst_bad_count='"+rst_bad_count+"',";
			update += "working_count='"+working_count+"' where pid='"+gid+"'";
			//System.out.println("update : " + update);
			odrDAO.executeUpdate(update);
		}

		//�Ǵ�1 : �ѻ�������� ���̰� ������  ��ǰ����,�ҷ�����,�ҷ�����,�ҷ������� �����Ѱ����� �����ϰ� ��������.
		if(diff_total_count == 0) {
			data = "��������� �����Ǿ����ϴ�.";
			return data;
		}
		
		//���4 [��ǰ����] : �������� ������ ���̺� update�ϱ�: st_item_stock_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//�������
			if(rev_count > 0) {	
				//���� ������ ����� ���� ���ϱ�
				where = "where item_code='"+item[i][0]+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				//�������� ����� ������ ���̼��� ���ϱ� : BOM���� * ���������� ���̼���
				use_count = Integer.parseInt(item[i][5]) * diff_total_count;
				if(use_count > rev_count) use_count = rev_count;		//��������� ��������.

				//updte�� ���� (������� - �����ҿ䷮)
				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - use_count);

				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][0]+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		//���5 [��ǰ����] : ��������� ������ ���̺� ���������� ���ϱ�: mfg_inout_master
		for(int i=0; i<an; i++) {
			rev_count = Integer.parseInt(item[i][6]);	//�������
			if(rev_count > 0) {	
				//���� �������� ���ϱ�
				where = "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				where += "factory_no='"+factory_no+"'";
				used_cnt = odrDAO.getColumData("MFG_INOUT_MASTER","use_count",where);
				if(used_cnt.length() == 0) used_cnt = "0";

				//�������� ����� ������ ���̼��� ���ϱ� : BOM���� * ���������� ���̼���
				use_count = Integer.parseInt(item[i][5]) * diff_total_count;
				if(use_count > rev_count) use_count = rev_count;		//��������� ��������.

				//updte�� ���� (���������� + �����ҿ䷮)
				use_count = Integer.parseInt(used_cnt) + use_count;

				update = "UPDATE mfg_inout_master SET use_count='"+use_count+"' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][0]+"' and ";
				update += "factory_no='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
		}

		data = "��������� �����Ǿ����ϴ�.";
		return data;
	}

	//*******************************************************************
	//  ������ ������� �����ϱ� : product_confirm
	//*******************************************************************/	
	public String closeProductItem(String login_id,String login_name,String mfg_no,String item_code,String factory_no) throws Exception
	{
		String data="",where="",update="",input="",used_cnt="",output_status="";
		String product="",mrp_no="",mps_no="",rst_good_count="",re_work="",factory_name="";

		//�غ�1 : �ش��۾������� ��������ǰ�ڵ带 ���ϱ�
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		product = odrDAO.getColumData("MFG_MASTER","item_code",where);
		rst_good_count = odrDAO.getColumData("MFG_MASTER","rst_good_count",where);
		re_work = odrDAO.getColumData("MFG_MASTER","re_work",where);
		factory_name = odrDAO.getColumData("MFG_MASTER","factory_name",where);

		//--------------
		//���� ����ó��
		//--------------
		//���� ������ǰ�ϰ�� ����ó���� �Ϻα����� ������ �����Ǿ����� 
		//ó��������� ��.
		where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' and item_code='"+item_code+"'";
		output_status = odrDAO.getColumData("MFG_PRODUCT_MASTER","output_status",where);
		if(product.equals(item_code) && output_status.equals("1")) {
			//��ü���� ���� ���ϱ�
			query = "SELECT count(*) FROM mfg_product_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			int t = odrDAO.getTotalCount(query)-1;

			//����ó���� �����������ϱ�
			query = "SELECT count(*) FROM mfg_product_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"' ";
			query += "and output_status='2'";
			int c = odrDAO.getTotalCount(query);

			//���Ͽ� �Ϻα��� �����Ǿ��� �Ǵ��ϱ� (�Ϻα����� ���θ���ó�� �Ǿ����� ����)
			if(t != c) {
				data = "���� ���긶���� �Ϻΰ����� ���� ����ó���Ǿ����� �����մϴ�.";
				return data;
			}
		}

		//������1 : �ش������ �۾����� ����ó�� : [mfg_operator : op_order = '3']
		update = "UPDATE mfg_operator SET op_order='3' ";
		update += "where mfg_no='"+mfg_no+"' and assy_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("������1 update : " + update);
		odrDAO.executeUpdate(update);

		//������2 : �ش������ ������� ������ ����ó�� : [mfg_product_master : output_status = '2']
		update = "UPDATE mfg_product_master SET output_status='2',output_date='"+anbdt.getDateNoformat()+"' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("������2 update : " + update);
		odrDAO.executeUpdate(update);

		//������3 : �ش������ ������� ���� ����ó�� : [mfg_product_item : output_status = '2']
		update = "UPDATE mfg_product_item SET output_status='2' ";
		update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		update += " and factory_no='"+factory_no+"'";
		//System.out.println("������3 update : " + update);
		odrDAO.executeUpdate(update);

		data = "���� ���������� ����Ǿ����ϴ�.";
		
		//------------------------------------
		//���� ������ǰ���� �۾����� ��ü ����
		//------------------------------------
		if(product.equals(item_code)) {
			//�غ�1 : ������ ��ǰ����� ��Ȳ �迭�� ���
			//0:pid,1:gid,2:mfg_no,3:item_code,4:item_name,5:item_spec,6:item_unit
			//7:inout_status,8:reserve_count,9:req_count,10:receive_count,11:use_count
			//12:rest_count,13:factory_no,14:factory_name
			arrayMfgInOutMasterList(mfg_no,factory_no);

			//�غ�2 : MRP NO���ϱ�
			where = "where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
			mrp_no = odrDAO.getColumData("MFG_MASTER","mrp_no",where);

			//�غ�3 : MPS NO���ϱ�
			where = "where mrp_no='"+mrp_no+"' and factory_no='"+factory_no+"'";
			mps_no = odrDAO.getColumData("MRP_MASTER","mps_no",where);

			//������ 1 : mfg_master [order_status='6']
			update = "UPDATE mfg_master SET order_status='6' ";
			update += "where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
			update += " and factory_no='"+factory_no+"'";
			//System.out.println("������1 update : " + update);
			odrDAO.executeUpdate(update);
			
			//������ 2 : mps_master [msp_status='7']
			update = "UPDATE mps_master SET mps_status='7' ";
			update += "where mps_no='"+mps_no+"' and item_code='"+item_code+"'";
			update += " and factory_no='"+factory_no+"'";
			//System.out.println("������ 2 update : " + update);
			odrDAO.executeUpdate(update);

			//�ܰ�ó�� : ������� ��ǰ�������� [mfg_inout_master]
			for(int i=0; i<an; i++) {
				update = "UPDATE mfg_inout_master SET rest_count='"+item[i][12]+"',inout_status='1' ";
				update += "where mfg_no='"+mfg_no+"' and item_code='"+item[i][3]+"'";
				update += " and factory_no='"+factory_no+"'";
				//System.out.println("�ܰ�ó�� update : " + update);
				odrDAO.executeUpdate(update);
			}

			//�ܰ����� : ������ ��ǰ������ó�� [st_item_stock_master]
			String delivery_quantity="";
			for(int i=0; i<an; i++) {
				where = "where item_code='"+item[i][3]+"' and factory_code='"+factory_no+"'";
				delivery_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","delivery_quantity",where);
				if(delivery_quantity.length() == 0) delivery_quantity = "0";

				delivery_quantity = Integer.toString(Integer.parseInt(delivery_quantity) - Integer.parseInt(item[i][12])); 
				update = "UPDATE st_item_stock_master SET delivery_quantity='"+delivery_quantity+"' ";
				update += "where item_code='"+item[i][3]+"' and factory_code='"+factory_no+"'";
				//System.out.println("�ܰ����� update : " + update);
				odrDAO.executeUpdate(update);
			}

/*			//2004.08.25 ������ ���� ��� ������.
			//������� ���԰˻��� �԰� : ������ ��ǰ������ó�� [st_item_stock_master]
			where = "where item_code='"+product+"' and factory_code='"+factory_no+"'";
			String into_quantity = odrDAO.getColumData("ST_ITEM_STOCK_MASTER","into_quantity",where);

			if(into_quantity.length() == 0) {		//�űԵ��
				into_quantity = rst_good_count;
				String[] part_info = new String[2];
				part_info = odrDAO.getItemInfo(product);	//�̸�,�԰�
				
				input = "INSERT INTO st_item_stock_master(item_code,item_name,item_desc,";
				input += "stock_unit,stock_quantity,into_quantity,good_quantity,bad_quantity,";
				input += "outto_quantity,delivery_quantity,resonable_quantity,unit_type,unit_cost,";
				input += "last_updated_date,factory_code,factory_name) values('";
				input += product+"','"+part_info[0]+"','"+part_info[1]+"','"+"EA"+"','"+"0"+"','"+into_quantity+"','";
				input += "0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"1"+"','"+"0"+"','";
				input += anbdt.getDateNoformat()+"','"+factory_no+"','"+""+"')";
				//System.out.println("input : " + input);
				odrDAO.executeUpdate(input);
			} else {								//update
				into_quantity = Integer.toString(Integer.parseInt(into_quantity) + Integer.parseInt(rst_good_count)); 
				update = "UPDATE st_item_stock_master SET into_quantity='"+into_quantity+"' ";
				update += "where item_code='"+product+"' and factory_code='"+factory_no+"'";
				//System.out.println("update : " + update);
				odrDAO.executeUpdate(update);
			}
*/

			//ǰ���� ����������� ����ϱ�
			if(re_work.equals("�۾�")) {
				String[] part_info = new String[2];
				part_info = odrDAO.getItemInfo(product);	//��ǰ �̸�,�԰�

				String[] div_info = new String[2];
				div_info = odrDAO.getDivInfo(login_id);		//�μ��ڵ�,�μ���

				com.anbtech.qc.db.QualityCtrlDAO qcDAO = new com.anbtech.qc.db.QualityCtrlDAO(con);
				String req_inspect_no = qcDAO.getRequestNo("TST");
				qcDAO.saveInspectionInfo(req_inspect_no,product,part_info[0],part_info[1],"","FMT",mfg_no,rst_good_count,div_info[0],div_info[1],login_id,login_name,factory_no,factory_name,"S01");
			}
			
			data = "��������� �����Ǿ����ϴ�.";
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		ST_RESERVED_ITEM_INFO �� ���� �޼ҵ�
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ ST_RESERVED_ITEM_INFO ����Ƿڳ��� �迭�� ���
	//*******************************************************************/	
	public String arrayMfgReqItemList(String mfg_req_no,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getMfgReqItemConfirmList(mfg_req_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="��ǰ������ �����ϴ�."; return data; }
		item = new String[cnt][10];

		mfgReqItemTable table = new mfgReqItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgReqItemTable)item_iter.next();

			item[an][0] = table.getPid();
			item[an][1] = table.getMfgNo();
			item[an][2] = table.getMfgReqNo();
			item[an][3] = table.getItemCode();
			item[an][4] = table.getItemName();
			item[an][5] = table.getItemSpec();
			item[an][6] = table.getItemType();
			item[an][7] = table.getItemUnit();
			item[an][8] = Integer.toString(table.getReqCount());						
			item[an][9] = table.getFactoryNo();
			
			//System.out.println(item[an][0]+" : "+item[an][3]+ " : "+item[an][8]+" : "+item[an][9]);
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		MFG ITEM �� ���� �޼ҵ�
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ MFG ITEM �ܴܰ� ������ �о� �迭�� ��� : ����ɺ�ǰ ã���
	//*******************************************************************/	
	public String arrayMfgItemList(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		String data = "";
		int out_cnt = 0;			//����� ����

		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getSingleMfgItems(mfg_no,assy_code,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="��ǰ������ �����ϴ�."; return data; }
		item = new String[cnt][10];

		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getItemCode();
			item[an][1] = table.getItemName();
			item[an][2] = table.getItemSpec();
			item[an][3] = table.getItemUnit();
			item[an][4] = table.getItemType();
			item[an][5] = Integer.toString(table.getReserveCount());						//�������
			item[an][6] = Integer.toString(table.getRequestCount());						//������Ƿڼ���
			item[an][7] = Integer.toString(table.getReserveCount()-table.getRequestCount());//����ɼ���
			item[an][8] = table.getFactoryNo();
			item[an][9] = table.getFactoryName();
			if(item[an][7].equals("0")) out_cnt++;
			//System.out.println(item[an][0]+" : "+item[an][5]+ " : "+item[an][6]+" : "+item[an][7]);
			an++;
		}

		//����� ������ �ִ��� �Ǵ��ϱ�
		if(out_cnt == an) data = "������� ��ǰ�� �����ϴ�.";

		return data;
	}

	//*******************************************************************
	// ������ MFG ITEM �ܴܰ� ������ �о� �迭�� ��� : ��������Է¿�
	//*******************************************************************/	
	public String getMfgItemList(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getSingleMfgItems(mfg_no,assy_code,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="��ǰ������ �����ϴ�."; return data; }
		item = new String[cnt][10];

		mfgItemTable table = new mfgItemTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgItemTable)item_iter.next();
			item[an][0] = table.getItemCode();
			item[an][1] = table.getItemName();
			item[an][2] = table.getItemSpec();
			item[an][3] = table.getItemUnit();
			item[an][4] = table.getItemType();
			item[an][5] = Integer.toString(table.getDrawCount());							//BOM����
			item[an][6] = Integer.toString(table.getReserveCount());						//�������
			item[an][7] = Integer.toString(table.getRequestCount());						//������Ƿڼ���
			item[an][8] = table.getFactoryNo();
			item[an][9] = table.getFactoryName();
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		��ǰ����� ���� �޼ҵ� : mfg_inout_master
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ mfg_inout_master ������� �迭�� ��� : ���긶�� �ܰ�ó����
	//*******************************************************************/	
	public String arrayMfgInOutMasterList(String mfg_no,String factory_no) throws Exception
	{
		String data = "";
		
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getMfgInOutMasterList(mfg_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) { data="��ǰ������ �����ϴ�."; return data; }
		item = new String[cnt][15];

		mfgInOutMasterTable table = new mfgInOutMasterTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mfgInOutMasterTable)item_iter.next();

			item[an][0] = table.getPid();
			item[an][1] = table.getGid();
			item[an][2] = table.getMfgNo();
			item[an][3] = table.getItemCode();
			item[an][4] = table.getItemName();
			item[an][5] = table.getItemSpec();
			item[an][6] = table.getItemUnit();
			item[an][7] = table.getInOutStatus();
			item[an][8] = Integer.toString(table.getReserveCount());
			item[an][9] = Integer.toString(table.getReqCount());
			item[an][10] = Integer.toString(table.getReceiveCount());
			item[an][11] = Integer.toString(table.getUseCount());
			item[an][12] = Integer.toString(table.getReceiveCount() - table.getUseCount());
			item[an][13] = table.getFactoryNo();
			item[an][14] = table.getFactoryName();
			
			//System.out.println(item[an][3]+" : "+item[an][8]+ " : "+item[an][10]+" : "+item[an][11]+" : "+item[an][12]);
			an++;
		}

		return data;
	}

	//--------------------------------------------------------------------
	//
	//		�������� ��ǰ�� ���� �޼ҵ�
	//			
	//			
	//---------------------------------------------------------------------
	/**********************************************************************
	 * ������� ����LIST [���������Ҷ� Ȱ��]
	 **********************************************************************/
	public ArrayList getUniqueRunningItemList(String model_code,String mfg_no,String factory_no) throws Exception
	{
		//model_code�� ������ �ش�Ǵ� mfg_no���ϱ�
		String mfg_list = odrDAO.getRunningMfgNo(model_code,factory_no);
		if(mfg_no.length() == 0) mfg_no = mfg_list;

		// �迭����
		ArrayList item_list = new ArrayList();
		item_list = odrDAO.getRunningItemList(mfg_no,factory_no);
		int cnt = item_list.size();
		if(cnt == 0) return item_list;

		String[][] data = new String[cnt][11];
		for(int i=0; i<cnt; i++) for(int j=0; j<11; j++) data[i][j]="";

		//�迭�� ���
		mfgInOutMasterTable table = new mfgInOutMasterTable();
		Iterator item_iter = item_list.iterator();
		int n = 0;
		while(item_iter.hasNext()) {
			table = (mfgInOutMasterTable)item_iter.next();
			data[n][0] = table.getItemCode();
			data[n][1] = table.getItemName();
			data[n][2] = table.getItemSpec();
			data[n][3] = table.getItemUnit();
			data[n][4] = Integer.toString(table.getReserveCount());
			data[n][5] = Integer.toString(table.getReqCount());
			data[n][6] = Integer.toString(table.getReceiveCount());
			data[n][7] = Integer.toString(table.getUseCount());
			data[n][8] = Integer.toString(table.getRestCount());
			data[n][9] = table.getFactoryNo();
			data[n][10] = "0";					//������ ��ǰ������ �Է��ϱ� ����
			//System.out.println(data[n][0]+":"+data[n][4]+":"+data[n][6]);
			n++;
		}
		//Unique�� ��ǰ���ڵ�� ���� ����ϱ�
		bubbleUniqueSortAsc(data,0);
		
		//���Ϻ�ǰ���� �����Կ� ���� ��迭 ���ְ� �ܰ��Է��ϱ�[�迭�߰�]
		//10:����,11:ǥ�شܰ�,12:��մܰ�,13:����ܰ�,14:ǥ���Ѿ�,15:����Ѿ�,16:�����Ѿ�
		String where = "";
		String t[][] = new String[n][17];
		an = 0;
		for(int i=0; i<n; i++) {
			if(data[i][0].length() != 0) {
				for(int j=0; j<11; j++) t[i][j] = data[i][j];
				
				where = "where item_code = '"+t[i][0]+"'";
				t[i][11] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_s",where);//ǥ�شܰ�
				t[i][12] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_a",where);//��մܰ�
				t[i][13] = showDAO.getColumCostData("st_item_unit_cost","unit_cost_c",where);//����ܰ�
				
				t[i][14] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][11]));
				t[i][15] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][12]));
				t[i][16] = Double.toString(Integer.parseInt(t[i][6])*Double.parseDouble(t[i][13]));
				
				an++;
			}
		}

		//Array List�� ���
		ArrayList price_list = new ArrayList();
		primeCostTable price = null;
		for(int i=0; i<an; i++) {
			price = new primeCostTable();	
			price.setItemCode(t[i][0]);		//ǰ���ڵ�
			price.setItemName(t[i][1]);		//ǰ���̸�
			price.setItemDesc(t[i][2]);		//ǰ��԰�
			price.setItemCount(t[i][6]);	//ǰ�����
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
			System.out.println(view.getItemCode()+":"+view.getItemCount()+":"+view.getAvePrice()+":"+view.getAveSum());
		}
*/
		return price_list;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� Unique�ϰ� �����Ѵ�. : ��������
	 ****************************************************************************/
	 private void bubbleUniqueSortAsc(String[][] b,int sort_no)
	{
		 int b_len = b.length;			//�迭�� ����
		 int e_len = b[0].length;		//�迭 ������ ����

		 //�����ϱ�
		 int rn = sort.bubbleSortStringMultiAsc(b,sort_no);
		 if(rn != 1) return;

		//unique�� ���볢�� ���� �迭�� ���
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int rev=0;				//�������
		int req=0;				//�䱸����
		int rec=0;				//������
		int use=0;				//������
		int rst=0;				//��������
		int an = 0;				//�迭�� ����
		for(int i=0; i<=b_cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < b_cnt) {
				//sort_no�� ������ ���ڸ� ���
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					rev += Integer.parseInt(b[i][4]);				//�������
					req += Integer.parseInt(b[i][5]);				//�䱸����
					rec += Integer.parseInt(b[i][6]);				//������
					use += Integer.parseInt(b[i][7]);				//������
					rst += Integer.parseInt(b[i][8]);				//��������
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					rev += Integer.parseInt(b[i][4]);				//�������
					req += Integer.parseInt(b[i][5]);				//�䱸����
					rec += Integer.parseInt(b[i][6]);				//������
					use += Integer.parseInt(b[i][7]);				//������
					rst += Integer.parseInt(b[i][8]);				//��������	
					
					for(int e=0; e<4; e++) a[an][e] = b[i][e];
					a[an][4] = Integer.toString(rev);	
					a[an][5] = Integer.toString(req);	
					a[an][6] = Integer.toString(rec);	
					a[an][7] = Integer.toString(use);	
					a[an][8] = Integer.toString(rst);	
					a[an][9] = b[i][9];
					a[an][10] = b[i][10];

					an++;
					rev=req=rec=use=rst=0;				
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				rev += Integer.parseInt(b[i][4]);				//�������
				req += Integer.parseInt(b[i][5]);				//�䱸����
				rec += Integer.parseInt(b[i][6]);				//������
				use += Integer.parseInt(b[i][7]);				//������
				rst += Integer.parseInt(b[i][8]);				//��������
					
				for(int e=0; e<4; e++) a[an][e] = b[i][e];
				a[an][4] = Integer.toString(rev);	
				a[an][5] = Integer.toString(req);	
				a[an][6] = Integer.toString(rec);	
				a[an][7] = Integer.toString(use);	
				a[an][8] = Integer.toString(rst);	
				a[an][9] = b[i][9];
				a[an][10] = b[i][10];

				an++;
			} //else
		} //for

		//������ �迭�� ���Ұ����� 1���� �÷� �Ǹ������� ���İ��� ���
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//�ű��
			
	}


	//--------------------------------------------------------------------
	//
	//		��Ÿ �޼ҵ�
	//			
	//			
	//---------------------------------------------------------------------
	public void setDeliveryNo(String delivery_no)	{ this.delivery_no = delivery_no;	}
	public String getDeliveryNo()					{ return this.delivery_no;			}
		
}



