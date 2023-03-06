package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mfgOrderDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//����
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//����
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	private ArrayList item_list = null;				//PART������ ArrayList�� ��� (BOM���� ���)
	private mfgItemTable mfgIT = null;				//help class (������������ BOM����)
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public mfgOrderDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		�۾����� ������ ���� �޼ҵ� ����
	//		[mfg_operator]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// �۾����� �������� ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgOrderList (String mfg_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgOperatorTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		//query = "SELECT COUNT(*) FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order in('1','2') ";
		query = "SELECT COUNT(*) FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order != 0 ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		//query = "SELECT * FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order in('1','2') ";
		query = "SELECT * FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order != 0 ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgOperatorTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setGid(rs.getString("gid"));
				String mfg_no = "<a href=\"javascript:mfgView('"+pid+"');\">"+rs.getString("mfg_no")+"</a>";
				table.setMfgNo(mfg_no);
				table.setAssyCode(rs.getString("assy_code"));
				table.setAssySpec(rs.getString("assy_spec"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
				table.setMfgUnit(rs.getString("mfg_unit"));
				table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"-"));	
				table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"-"));	
				table.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"-"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setWorkNo(rs.getString("work_no"));	
				table.setWorkName(rs.getString("work_name"));
				table.setOpNo(rs.getString("op_no"));
				table.setOpName(rs.getString("op_name"));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setNote(rs.getString("note"));
				table.setCompCode(rs.getString("comp_code"));
				table.setCompName(rs.getString("comp_name"));
				table.setCompUser(rs.getString("comp_user"));
				table.setCompTel(rs.getString("comp_tel"));
				table.setOpOrder(rs.getString("op_order"));

				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �۾����� ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public mfgOperatorTable getRequestDisplayPage(String mfg_id,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgOperatorTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query = "SELECT COUNT(*) FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order in('1','2') ";
		query = "SELECT COUNT(*) FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order != 0 ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		//query = "SELECT * FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order in('1','2') ";
		query = "SELECT * FROM mfg_operator where mfg_id='"+mfg_id+"' and op_order != 0 ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgOrderServlet?&mode=order_receive&page="+curpage+"&mfg_id="+mfg_id+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgOrderServlet?&mode=order_receive&page="+curpage+"&mfg_id="+mfg_id+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgOrderServlet?&mode=order_receive&page="+curpage+"&mfg_id="+mfg_id+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mfgOperatorTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// ������ȣ�� �ش� MFG OPETATOR ���� �б� 
	//*******************************************************************/	
	public mfgOperatorTable readMfgOperator(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgOperatorTable mfgOP = new com.anbtech.mm.entity.mfgOperatorTable();
		
		query = "SELECT * FROM mfg_operator where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
	
		if(rs.next()) { 
			mfgOP.setPid(rs.getString("pid"));	
			mfgOP.setGid(rs.getString("gid"));	
			mfgOP.setMfgNo(rs.getString("mfg_no"));	
			mfgOP.setAssyCode(rs.getString("assy_code"));
			mfgOP.setAssySpec(rs.getString("assy_spec"));
			mfgOP.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgOP.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgOP.setMfgUnit(rs.getString("mfg_unit"));
			mfgOP.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"-"));	
			mfgOP.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"-"));	
			mfgOP.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"-"));	
			mfgOP.setBuyType(rs.getString("buy_type"));	
			mfgOP.setFactoryNo(rs.getString("factory_no"));
			mfgOP.setFactoryName(rs.getString("factory_name"));
			mfgOP.setWorkNo(rs.getString("work_no"));	
			mfgOP.setWorkName(rs.getString("work_name"));
			mfgOP.setOpNo(rs.getString("op_no"));
			mfgOP.setOpName(rs.getString("op_name"));
			mfgOP.setMfgId(rs.getString("mfg_id"));
			mfgOP.setMfgName(rs.getString("mfg_name"));
			mfgOP.setNote(rs.getString("note"));
			mfgOP.setCompCode(rs.getString("comp_code"));
			mfgOP.setCompName(rs.getString("comp_name"));
			mfgOP.setCompUser(rs.getString("comp_user"));
			mfgOP.setCompTel(rs.getString("comp_tel"));
			mfgOP.setOpOrder(rs.getString("op_order"));
			
		} 
		else {
			mfgOP.setPid("");	
			mfgOP.setGid("");	
			mfgOP.setMfgNo("");	
			mfgOP.setAssyCode("");
			mfgOP.setAssySpec("");
			mfgOP.setLevelNo(0);
			mfgOP.setMfgCount(0);	
			mfgOP.setMfgUnit("");
			mfgOP.setOpStartDate("");	
			mfgOP.setOpEndDate("");	
			mfgOP.setOrderDate("");	
			mfgOP.setBuyType("");	
			mfgOP.setFactoryNo("");
			mfgOP.setFactoryName("");
			mfgOP.setWorkNo("");	
			mfgOP.setWorkName("");
			mfgOP.setOpNo("");
			mfgOP.setOpName("");
			mfgOP.setMfgId("");
			mfgOP.setMfgName("");
			mfgOP.setNote("");
			mfgOP.setCompCode("");
			mfgOP.setCompName("");
			mfgOP.setCompUser("");
			mfgOP.setCompTel("");
			mfgOP.setOpOrder("");
			
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return mfgOP;
	}

	//--------------------------------------------------------------------
	//
	//		MFG MASTER �� ���� �޼ҵ� ����
	//		
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MFG MASTER���� �б� 
	//*******************************************************************/	
	public mfgMasterTable readMfgMasterItem(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
		
		query = "SELECT * FROM mfg_master where pid ='"+gid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMrpNo(rs.getString("mrp_no"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setModelCode(rs.getString("model_code"));
			table.setModelName(rs.getString("model_name"));
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));
			table.setBuyType(rs.getString("buy_type"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setCompCode(rs.getString("comp_code"));
			table.setCompName(rs.getString("comp_name"));
			table.setCompUser(rs.getString("comp_user"));
			table.setCompTel(rs.getString("comp_tel"));
			table.setOrderStatus(rs.getString("order_status"));
			table.setOrderType(rs.getString("order_type"));
			table.setRegDate(rs.getString("reg_date"));
			table.setRegId(rs.getString("reg_id"));
			table.setRegName(rs.getString("reg_name"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setOrderStartDate(anbdt.getSepDate(rs.getString("order_start_date"),"-"));
			table.setOrderEndDate(anbdt.getSepDate(rs.getString("order_end_date"),"-"));
			table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"-"));
			table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"-"));
			table.setOrderDate(rs.getString("order_date"));
			table.setReWork(rs.getString("re_work"));
			table.setRstTotalCount(Integer.parseInt(rs.getString("rst_total_count")));
			table.setRstGoodCount(Integer.parseInt(rs.getString("rst_good_count")));
			table.setRstBadCount(Integer.parseInt(rs.getString("rst_bad_count")));
			table.setWorkingCount(Integer.parseInt(rs.getString("working_count")));
			table.setRstPassCount(Integer.parseInt(rs.getString("rst_pass_count")));
			table.setRstFailCount(Integer.parseInt(rs.getString("rst_fail_count")));
		} else {
			table.setPid("");	
			table.setMrpNo("");
			table.setMfgNo("");
			table.setModelCode("");
			table.setModelName("");
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setItemUnit("");
			table.setMfgCount(0);
			table.setBuyType("");
			table.setFactoryNo("");
			table.setFactoryName("");
			table.setCompCode("");
			table.setCompName("");
			table.setCompUser("");
			table.setCompTel("");
			table.setOrderStatus("");
			table.setOrderType("MANUAL");
			table.setRegDate("");
			table.setRegId("");
			table.setRegName("");
			table.setPlanDate("");
			table.setOrderStartDate("");
			table.setOrderEndDate("");
			table.setOpStartDate("");
			table.setOpEndDate("");
			table.setOrderDate("");
			table.setReWork("");
			table.setRstTotalCount(0);
			table.setRstGoodCount(0);
			table.setRstBadCount(0);
			table.setWorkingCount(0);
			table.setRstPassCount(0);
			table.setRstFailCount(0);
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// �����ȣ�� ����������ȣ�� �ش� MFG MASTER���� �б� 
	//*******************************************************************/	
	public mfgMasterTable readMfgMasterItem(String mfg_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgMasterTable table = new com.anbtech.mm.entity.mfgMasterTable();
		
		query = "SELECT * FROM mfg_master where mfg_no ='"+mfg_no+"' and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMrpNo(rs.getString("mrp_no"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setModelCode(rs.getString("model_code"));
			table.setModelName(rs.getString("model_name"));
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));
			table.setBuyType(rs.getString("buy_type"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setCompCode(rs.getString("comp_code"));
			table.setCompName(rs.getString("comp_name"));
			table.setCompUser(rs.getString("comp_user"));
			table.setCompTel(rs.getString("comp_tel"));
			table.setOrderStatus(rs.getString("order_status"));
			table.setOrderType(rs.getString("order_type"));
			table.setRegDate(rs.getString("reg_date"));
			table.setRegId(rs.getString("reg_id"));
			table.setRegName(rs.getString("reg_name"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setOrderStartDate(anbdt.getSepDate(rs.getString("order_start_date"),"-"));
			table.setOrderEndDate(anbdt.getSepDate(rs.getString("order_end_date"),"-"));
			table.setOpStartDate(anbdt.getSepDate(rs.getString("op_start_date"),"-"));
			table.setOpEndDate(anbdt.getSepDate(rs.getString("op_end_date"),"-"));
			table.setOrderDate(rs.getString("order_date"));
			table.setReWork(rs.getString("re_work"));
			table.setRstTotalCount(Integer.parseInt(rs.getString("rst_total_count")));
			table.setRstGoodCount(Integer.parseInt(rs.getString("rst_good_count")));
			table.setRstBadCount(Integer.parseInt(rs.getString("rst_bad_count")));
			table.setWorkingCount(Integer.parseInt(rs.getString("working_count")));
			table.setRstPassCount(Integer.parseInt(rs.getString("rst_pass_count")));
			table.setRstFailCount(Integer.parseInt(rs.getString("rst_fail_count")));
		} else {
			table.setPid("");	
			table.setMrpNo("");
			table.setMfgNo("");
			table.setModelCode("");
			table.setModelName("");
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setItemUnit("");
			table.setMfgCount(0);
			table.setBuyType("");
			table.setFactoryNo("");
			table.setFactoryName("");
			table.setCompCode("");
			table.setCompName("");
			table.setCompUser("");
			table.setCompTel("");
			table.setOrderStatus("");
			table.setOrderType("MANUAL");
			table.setRegDate("");
			table.setRegId("");
			table.setRegName("");
			table.setPlanDate("");
			table.setOrderStartDate("");
			table.setOrderEndDate("");
			table.setOpStartDate("");
			table.setOpEndDate("");
			table.setOrderDate("");
			table.setReWork("");
			table.setRstTotalCount(0);
			table.setRstGoodCount(0);
			table.setRstBadCount(0);
			table.setWorkingCount(0);
			table.setRstPassCount(0);
			table.setRstFailCount(0);
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		��ǰ����Ƿ� �����Ϳ� ���� �޼ҵ� ����
	//		[mfg_req_msater]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ��ǰ����Ƿ� ������ ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgReqMasterList (String req_user_id,String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgReqMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM mfg_req_master where req_user_id='"+req_user_id+"' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_req_master where req_user_id='"+req_user_id+"' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgReqMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				String mfg_req_no = "<a href=\"javascript:mfgReqView('"+rs.getString("mfg_req_no")+"');\">"+rs.getString("mfg_req_no")+"</a>";
				table.setMfgReqNo(mfg_req_no);
				table.setAssyCode(rs.getString("assy_code"));
				table.setAssySpec(rs.getString("assy_spec"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				table.setReqStatus(rs.getString("req_status"));
				table.setReqDate(rs.getString("req_date"));
				table.setReqDivCode(rs.getString("req_div_code"));
				table.setReqDivName(rs.getString("req_div_name"));
				table.setReqUserId(rs.getString("req_user_id"));
				table.setReqUserName(rs.getString("req_user_name"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ��ǰ����Ƿ� ������ ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public mfgReqMasterTable getMfgReqMasterDisplayPage(String req_user_id,String factory_no,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgReqMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM mfg_req_master where req_user_id='"+req_user_id+"' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_req_master where req_user_id='"+req_user_id+"' and factory_no='"+factory_no+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgOrderServlet?&mode=out_list&page="+curpage+"&req_user_id="+req_user_id+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgOrderServlet?&mode=out_list&page="+curpage+"&req_user_id="+req_user_id+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgOrderServlet?&mode=out_list&page="+curpage+"&req_user_id="+req_user_id+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mfgReqMasterTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ��ǰ����Ƿ� ������ ���� 
	//*******************************************************************/	
	public mfgReqMasterTable readMfgReqMaster(String mfg_req_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgReqMasterTable table = new com.anbtech.mm.entity.mfgReqMasterTable();
		
		//query���� �����
		query = "SELECT * FROM mfg_req_master where mfg_req_no='"+mfg_req_no+"' and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setMfgReqNo(rs.getString("mfg_req_no"));
				table.setAssyCode(rs.getString("assy_code"));
				table.setAssySpec(rs.getString("assy_spec"));
				table.setLevelNo(Integer.parseInt(rs.getString("level_no")));
				table.setReqStatus(rs.getString("req_status"));
				table.setReqDate(rs.getString("req_date"));
				table.setReqDivCode(rs.getString("req_div_code"));
				table.setReqDivName(rs.getString("req_div_name"));
				table.setReqUserId(rs.getString("req_user_id"));
				table.setReqUserName(rs.getString("req_user_name"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
		} else {
				table.setPid("");
				table.setGid("");
				table.setMfgNo("");
				table.setMfgReqNo("");
				table.setAssyCode("");
				table.setAssySpec("");
				table.setLevelNo(0);
				table.setReqStatus("");
				table.setReqDate("");
				table.setReqDivCode("");
				table.setReqDivName("");
				table.setReqUserId("");
				table.setReqUserName("");
				table.setFactoryNo("");
				table.setFactoryName("");
		}
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ��ǰ����Ƿ� �����߿� �ִ� ������ �ִ��� �Ǵ��ϱ� 
	//*******************************************************************/	
	public String checkProcess(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "P",req_status="";
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT req_status FROM mfg_req_master where mfg_no='"+mfg_no+"' and ";
		query += "assy_code='"+assy_code+"' and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
			req_status = rs.getString("req_status");
			if(req_status.equals("1")) cnt++;
		}

		if(cnt > 0) data = "F";
		stmt.close();
		rs.close();
		return data;
	}
		

	//--------------------------------------------------------------------
	//
	//		������ ��ǰ����Ƿڿ� ���� �޼ҵ� ����
	//		[st_reserved_item_info,st_item_master_master : ������]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ��ǰ����Ƿ� ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgReqItemList (String mfg_req_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgReqItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM st_reserved_item_info where delivery_no='"+mfg_req_no+"' and factory_code='"+factory_no+"'";
		query += " order by item_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mfgReqItemTable();

				String pid = rs.getString("mid");
				table.setPid(pid);
				table.setMfgNo(rs.getString("ref_no"));
				table.setMfgReqNo(rs.getString("delivery_no"));
				String item_code = "<a href=\"javascript:itemView('"+pid+"','"+rs.getString("item_code")+"');\">"+rs.getString("item_code")+"</a>";
				table.setItemCode(item_code);
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_desc"));
				table.setItemType(rs.getString("item_type"));
				table.setReqCount(Integer.parseInt(rs.getString("request_quantity")));
				table.setItemUnit(rs.getString("request_unit"));
				table.setReqDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setFactoryNo(rs.getString("factory_code"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ��ǰ����Ƿ� ����Ʈ : ��ǰ����Ƿ� Ȯ���� ����
	//*******************************************************************/	
	public ArrayList getMfgReqItemConfirmList(String mfg_req_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgReqItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM st_reserved_item_info where delivery_no='"+mfg_req_no+"' and factory_code='"+factory_no+"'";
		query += " order by item_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mfgReqItemTable();

				table.setPid(rs.getString("mid"));
				table.setMfgNo(rs.getString("ref_no"));
				table.setMfgReqNo(rs.getString("delivery_no"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_desc"));
				table.setItemType(rs.getString("item_type"));
				table.setReqCount(Integer.parseInt(rs.getString("request_quantity")));
				table.setItemUnit(rs.getString("request_unit"));
				table.setReqDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setFactoryNo(rs.getString("factory_code"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ������ȣ�� �ش� st_reserved_item_info ���� �б� 
	//*******************************************************************/	
	public mfgReqItemTable readMfgReqItemRead(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgReqItemTable table = new com.anbtech.mm.entity.mfgReqItemTable();
		
		query = "SELECT * FROM st_reserved_item_info where mid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("mid"));	
			table.setMfgNo(rs.getString("ref_no"));
			table.setMfgReqNo(rs.getString("delivery_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_desc"));
			table.setItemType(rs.getString("item_type"));
			table.setReqCount(Integer.parseInt(rs.getString("request_quantity")));
			table.setItemUnit(rs.getString("request_unit"));
			table.setReqDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
			table.setFactoryNo(rs.getString("factory_code"));
		} else {
			table.setPid("");	
			table.setMfgNo("");
			table.setMfgReqNo("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setItemUnit("");
			table.setItemType("");
			table.setReqCount(0);
			table.setItemUnit("");
			table.setReqDate("");
			table.setFactoryNo("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ��ǰ����Ƿڰ�����ȣ ���ϱ�
	// FORMAT : DM+yy(2)+mm(2)+"-"+serial(2)
	//*******************************************************************/	
	public String getMfgReqNo(String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//��ǰ����Ƿڰ�����ȣ �� Serial�� ������ ��ȣ ���ϱ�
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mfg_req_no = "DM"+y+m+"-";
		
		//query���� �����
		query = "SELECT delivery_no FROM st_reserved_item_info where factory_code='"+factory_no+"' and delivery_no like '"+mfg_req_no+"%' ";
		query += "order by delivery_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("delivery_no");
		
		if(data.length() == 0) {
			data = mfg_req_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mfg_req_no+serial;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		������ ������ �ʿ��� ��ǰ ����� ������
	//		[mfg_inout_master]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ���� ��ǰ ����� ������ ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgInOutMasterList(String mfg_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgInOutMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM mfg_inout_master where mfg_no='"+mfg_no+"' and factory_no='"+factory_no+"'";
		query += " order by item_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mfgInOutMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setInOutStatus(rs.getString("inout_status"));
				table.setReserveCount(rs.getInt("reserve_count"));
				table.setReqCount(rs.getInt("req_count"));
				table.setReceiveCount(rs.getInt("receive_count"));
				table.setUseCount(rs.getInt("use_count"));
				table.setRestCount(rs.getInt("rest_count"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		������ ������� ���� �޼ҵ� ����
	//		[mfg_product_master , mfg_product_item]
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ ���ǰ������� ������ ����Ʈ 
	//*******************************************************************/	
	public ArrayList getMfgProductMasterList (String login_id,String factory_no,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and mfg_id='"+login_id+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and mfg_id='"+login_id+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				table = new mfgProductMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));

				String item_code = "<a href=\"javascript:mfgMasterView('"+rs.getString("mfg_no")+"','"+rs.getString("item_code")+"');\">"+rs.getString("item_code")+"</a>";
				table.setItemCode(item_code);
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setOrderCount(Integer.parseInt(rs.getString("order_count")));
				table.setOrderUnit(rs.getString("order_unit"));
				table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
				table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
				table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setOutputStatus(rs.getString("output_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setOutputDate(anbdt.getSepDate(rs.getString("output_date"),"-"));
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ������ ���ǰ������� ������ ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public mfgProductMasterTable getMfgProductMasterDisplayPage(String login_id,String factory_no,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mfgProductMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and mfg_id='"+login_id+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' ";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM mfg_product_master where factory_no='"+factory_no+"' ";
		query += "and mfg_id='"+login_id+"' ";
		query += "and "+sItem+" like '%"+sWord+"%' order by mfg_no desc";
		rs = stmt.executeQuery(query);

		// ��ü �������� ���� ���Ѵ�.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//������ �ٷΰ��� �����
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=mfgOrderServlet?&mode=product_out_list&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=mfgOrderServlet?&mode=product_out_list&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=mfgOrderServlet?&mode=product_out_list&page="+curpage+"&factory_no="+factory_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mfgProductMasterTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//*******************************************************************
	// ���������� ����Ʈ : mfg_product_item
	//*******************************************************************/	
	public ArrayList getProductItemList(String mfg_no,String item_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgProductItemTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM mfg_product_item where mfg_no='"+mfg_no+"' and item_code='"+item_code+"'";
		query += " and factory_no='"+factory_no+"'";
		query += " order by output_date desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mfgProductItemTable();

				String pid = rs.getString("pid");
				table.setPid(pid);
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				item_code = "<a href=\"javascript:productView('"+pid+"','"+rs.getString("item_code")+"');\">"+rs.getString("item_code")+"</a>";
				table.setItemCode(item_code);
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
				table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
				table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
				table.setMfgId(rs.getString("mfg_id"));
				table.setMfgName(rs.getString("mfg_name"));
				table.setOutputDate(anbdt.getSepDate(rs.getString("output_date"),"-"));
				table.setOpNo(rs.getString("op_no"));
				table.setBadType(rs.getString("bad_type"));
				table.setBadNote(rs.getString("bad_note"));
				table.setOutputStatus(rs.getString("output_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// ������ȣ�� �ش� mfg_product_item ���� �б� 
	//*******************************************************************/	
	public mfgProductItemTable readMfgProductItemRead(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgProductItemTable table = new com.anbtech.mm.entity.mfgProductItemTable();
		
		query = "SELECT * FROM mfg_product_item where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));
			table.setGid(rs.getString("gid"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
			table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
			table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
			table.setMfgId(rs.getString("mfg_id"));
			table.setMfgName(rs.getString("mfg_name"));
			table.setOutputDate(anbdt.getSepDate(rs.getString("output_date"),"-"));
			table.setOpNo(rs.getString("op_no"));
			table.setBadType(rs.getString("bad_type"));
			table.setBadNote(rs.getString("bad_note"));
			table.setOutputStatus(rs.getString("output_status"));
			table.setFactoryNo(rs.getString("factory_no"));
		} else {
			table.setPid("");
			table.setGid("");
			table.setMfgNo("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setTotalCount(0);
			table.setGoodCount(0);
			table.setBadCount(0);
			table.setMfgId("");
			table.setMfgName("");
			table.setOutputDate("");
			table.setOpNo("");
			table.setBadType("");
			table.setBadNote("");
			table.setOutputStatus("");
			table.setFactoryNo("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ������ȣ�� �ش� mfg_product_master ���� �б� 
	//*******************************************************************/	
	public mfgProductMasterTable readMfgProductMasterRead(String mfg_no,String item_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgProductMasterTable table = new com.anbtech.mm.entity.mfgProductMasterTable();
		
		query = "SELECT * FROM mfg_product_master where mfg_no ='"+mfg_no+"' and item_code='"+item_code+"' ";
		query += "and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));
			table.setGid(rs.getString("gid"));
			table.setMfgNo(rs.getString("mfg_no"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setOrderCount(Integer.parseInt(rs.getString("order_count")));
			table.setOrderUnit(rs.getString("order_unit"));
			table.setTotalCount(Integer.parseInt(rs.getString("total_count")));
			table.setGoodCount(Integer.parseInt(rs.getString("good_count")));
			table.setBadCount(Integer.parseInt(rs.getString("bad_count")));
			table.setMfgId(rs.getString("mfg_id"));
			table.setMfgName(rs.getString("mfg_name"));
			table.setOutputStatus(rs.getString("output_status"));
			table.setFactoryNo(rs.getString("factory_no"));
		} else {
			table.setPid("");
			table.setGid("");
			table.setMfgNo("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setOrderCount(0);
			table.setOrderUnit("");
			table.setTotalCount(0);
			table.setGoodCount(0);
			table.setBadCount(0);
			table.setMfgId("");
			table.setMfgName("");
			table.setOutputStatus("");
			table.setFactoryNo("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		MFG ITEM ���� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ������ȣ�� �ش� MRP ITEM ���� �б� 
	//*******************************************************************/	
	public mfgItemTable readMfgItem(String mfg_no,String assy_code,String level_no,
		String item_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mfgItemTable mfgIT = new com.anbtech.mm.entity.mfgItemTable();
		
		query = "SELECT * FROM mfg_item where mfg_no ='"+mfg_no+"' and assy_code='"+assy_code+"' ";
		query += "and level_no='"+level_no+"' and item_code='"+item_code+"' and factory_no='"+factory_no+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			mfgIT.setPid(rs.getString("pid"));	
			mfgIT.setGid(rs.getString("gid"));	
			mfgIT.setMfgNo(rs.getString("mfg_no"));	
			mfgIT.setAssyCode(rs.getString("assy_code"));	
			mfgIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgIT.setItemCode(rs.getString("item_code"));	
			mfgIT.setItemName(rs.getString("item_name"));	
			mfgIT.setItemSpec(rs.getString("item_spec"));	
			mfgIT.setItemUnit(rs.getString("item_unit"));
			mfgIT.setItemType(rs.getString("item_type"));	
			mfgIT.setItemLoss(Double.parseDouble(rs.getString("item_loss")));	
			mfgIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mfgIT.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));
			mfgIT.setSpareCount(Integer.parseInt(rs.getString("spare_count")));
			mfgIT.setAddCount(Integer.parseInt(rs.getString("add_count")));
			mfgIT.setReserveCount(Integer.parseInt(rs.getString("reserve_count")));
			mfgIT.setRequestCount(Integer.parseInt(rs.getString("request_count")));
			mfgIT.setNeedDate(anbdt.getSepDate(rs.getString("need_date"),"/"));	
			mfgIT.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgIT.setFactoryNo(rs.getString("factory_no"));
			mfgIT.setFactoryName(rs.getString("factory_name"));
		} 
		else {
			mfgIT.setPid("");	
			mfgIT.setGid("");	
			mfgIT.setMfgNo("");	
			mfgIT.setAssyCode("");	
			mfgIT.setLevelNo(0);
			mfgIT.setItemCode("");	
			mfgIT.setItemName("");	
			mfgIT.setItemSpec("");
			mfgIT.setItemUnit("");
			mfgIT.setItemType("");	
			mfgIT.setItemLoss(0);	
			mfgIT.setDrawCount(0);	
			mfgIT.setMfgCount(0);	
			mfgIT.setNeedCount(0);	
			mfgIT.setSpareCount(0);
			mfgIT.setAddCount(0);
			mfgIT.setReserveCount(0);
			mfgIT.setRequestCount(0);
			mfgIT.setNeedDate("");	
			mfgIT.setOrderDate("");	
			mfgIT.setFactoryNo("");
			mfgIT.setFactoryName("");
			
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return mfgIT;
	}

	/**********************************************************************
	 * ������ �ܴܰ� Item �ҿ䷮ ���
	 * ���� : MRP NO,ASSY�ڵ�,����
	 *********************************************************************/
	public ArrayList getSingleMfgItems(String mfg_no,String assy_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String lno = "",pid="",item_code="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MFG_ITEM ";
		query += "where mfg_no = '"+mfg_no+"' and assy_code = '"+assy_code+"' ";
		query += "and factory_no='"+factory_no+"' order by item_code asc";
		rs = stmt.executeQuery(query);

		item_list = new ArrayList();
		mfgItemTable mfgIT;
		while (rs.next()) {
			mfgIT = new mfgItemTable();

			pid = rs.getString("pid");
			mfgIT.setPid(pid);	
			mfgIT.setGid(rs.getString("gid"));	
			mfgIT.setMfgNo(rs.getString("mfg_no"));	
			mfgIT.setAssyCode(rs.getString("assy_code"));	
			mfgIT.setLevelNo(Integer.parseInt(rs.getString("level_no")));
			mfgIT.setItemCode(rs.getString("item_code"));
			mfgIT.setItemName(rs.getString("item_name"));	
			mfgIT.setItemSpec(rs.getString("item_spec"));	
			mfgIT.setItemUnit(rs.getString("item_unit"));
			mfgIT.setItemType(rs.getString("item_type"));	
			mfgIT.setItemLoss(Double.parseDouble(rs.getString("item_loss")));	
			mfgIT.setDrawCount(Integer.parseInt(rs.getString("draw_count")));	
			mfgIT.setMfgCount(Integer.parseInt(rs.getString("mfg_count")));	
			mfgIT.setNeedCount(Integer.parseInt(rs.getString("need_count")));
			mfgIT.setSpareCount(Integer.parseInt(rs.getString("spare_count")));
			mfgIT.setAddCount(Integer.parseInt(rs.getString("add_count")));
			mfgIT.setReserveCount(Integer.parseInt(rs.getString("reserve_count")));
			mfgIT.setRequestCount(Integer.parseInt(rs.getString("request_count")));
			mfgIT.setNeedDate(anbdt.getSepDate(rs.getString("need_date"),"/"));	
			mfgIT.setOrderDate(anbdt.getSepDate(rs.getString("order_date"),"/"));	
			mfgIT.setFactoryNo(rs.getString("factory_no"));
			mfgIT.setFactoryName(rs.getString("factory_name"));
	
			item_list.add(mfgIT); 
		}

		rs.close();
		stmt.close();
		return item_list;
	} 

	//--------------------------------------------------------------------
	//
	//		�������� ��ǰ�� ���� �޼ҵ�
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	//	�������� �۾����ù�ȣ ã�� (��ǰ���� �������ΰ͸�) 
	//*******************************************************************/
	public String getRunningMfgNo(String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		if(factory_no.length() == 0) {	//��ü����
			query = "select mfg_no from mfg_master where order_status in('4','5')";
		} else {						//�ش����
			query = "select mfg_no from mfg_master where factory_no='"+factory_no+"' and order_status in('4','5')";
		}

		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("mfg_no")+"|";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�������� ���õȸ��� mfg no ã�� (��ǰ���� �������ΰ͸�) 
	//*******************************************************************/
	public String getRunningMfgNo(String model_code,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		if(factory_no.length() == 0) {	//��ü����
			query = "select mfg_no from mfg_master where model_code like '%"+model_code+"%' and order_status in('4','5')";
		} else {						//�ش����
			query = "select mfg_no from mfg_master where model_code like '%"+model_code+"%' and factory_no='"+factory_no+"' and order_status in('4','5')";
		}

		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("mfg_no")+"|";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�������� ���ڵ�:F/G�ڵ� ã�� (��ǰ���� �������ΰ͸�) 
	//*******************************************************************/
	public String getRunningModelCode(String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		if(factory_no.length() == 0) {	//��ü����
			query = "select distinct model_code,fg_code from mfg_master where order_status in('4','5')";
		} else {						//�ش����
			query = "select distinct model_code,fg_code from mfg_master where factory_no='"+factory_no+"' and order_status in('4','5')";
		}

		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("model_code")+":"+rs.getString("fg_code")+"|";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	
	//*******************************************************************
	//	�ش�������ȣ�� �������� ��ǰ����� �б� 
	//*******************************************************************/
	public ArrayList getRunningItemList(String mfg_no,String factory_no) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mfgInOutMasterTable table = null;
		ArrayList table_list = new ArrayList();

		//mfg_no�� �̿��� �������� �����
		if(mfg_no.indexOf("|") == -1) {
			mfg_no = "'"+mfg_no+"'";
		} else {
			mfg_no = "'"+str.repWord(mfg_no,"|","','");
			mfg_no = mfg_no.substring(0,mfg_no.length()-2);
		}

		if(factory_no.length() == 0) {	//��ü����
			query = "select * from mfg_inout_master where mfg_no in("+mfg_no+")";
		} else {						//�ش����
			query = "select * from mfg_inout_master where factory_no='"+factory_no+"' and mfg_no in("+mfg_no+")";
		}
		
		rs = stmt.executeQuery(query);
		while(rs.next()) { 
				table = new mfgInOutMasterTable();

				table.setPid(rs.getString("pid"));
				table.setGid(rs.getString("gid"));
				table.setMfgNo(rs.getString("mfg_no"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setInOutStatus(rs.getString("inout_status"));
				table.setReserveCount(rs.getInt("reserve_count"));
				table.setReqCount(rs.getInt("req_count"));
				table.setReceiveCount(rs.getInt("receive_count"));
				table.setUseCount(rs.getInt("use_count"));
				table.setRestCount(rs.getInt("rest_count"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

/*		//����غ���
		mfgInOutMasterTable view = new mfgInOutMasterTable();
		Iterator item_iter = table_list.iterator();
		while(item_iter.hasNext()) {
			view = (mfgInOutMasterTable)item_iter.next();
			System.out.println(view.getItemCode()+":"+view.getItemName()+":"+view.getReserveCount()+":"+view.getReceiveCount());
		}
*/
		return table_list;
	}
	//*******************************************************************
	//	�������� ã��
	//*******************************************************************/
	public String getFactoryList() throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT factory_code FROM factory_info_table order by factory_code asc";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("factory_code")+"|";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//--------------------------------------------------------------------
	//
	//		���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------
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
	//	�־�������� �����������
	//*******************************************************************/
	public String[] getDivInfo(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code,b.ac_name from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("ac_code");
			data[1] = rs.getString("ac_name");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�־����ڵ��� ��ǰ������
	//*******************************************************************/
	public String[] getItemInfo(String item_code) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[2];  data[0] = data[1] = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select item_name,item_desc from item_master where item_no='"+item_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "";
			data[1] = rs.getString("item_desc");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	
}

