package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();//����
	private String query = "";
	private ArrayList item_list = null;				//PART������ ArrayList�� ���
	private mbomStrTable mst = null;				//help class
	private int total_page = 0;
	private int current_page = 0;
	private String assy_head = "[1,F]";				//Assy�ڵ常 ������ ��� 1:Assy���ι���, F:FG�ڵ� ���ι���
	private String ele_assy = "[1][E]";				//ȸ��Assy
	private String ele_item = "[2,3]";				//ȸ�κ�ǰ

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public BomModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MBOM_MASTER���� �б�
	//*******************************************************************/	
	public mbomMasterTable readMasterItem(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = new com.anbtech.bm.entity.mbomMasterTable();
		
		query = "SELECT * FROM mbom_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setModelgCode(rs.getString("modelg_code"));	
			table.setModelgName(rs.getString("modelg_name"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setPdgCode(rs.getString("pdg_code"));	
			table.setPdgName(rs.getString("pdg_name"));	
			table.setPdCode(rs.getString("pd_code"));	
			table.setPdName(rs.getString("pd_name"));
			table.setPjtCode(rs.getString("pjt_code"));	
			table.setPjtName(rs.getString("pjt_name"));	
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setRegDate(rs.getString("reg_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppName(rs.getString("app_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setBomStatus(rs.getString("bom_status"));	
			table.setAppNo(rs.getString("app_no"));	
			table.setMStatus(rs.getString("m_status"));	
			table.setPurpose(rs.getString("purpose"));	
		} else {
			table.setPid(anbdt.getID());	
			table.setModelgCode("");	
			table.setModelgName("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setPdgCode("");	
			table.setPdgName("");	
			table.setPdCode("");	
			table.setPdName("");	
			table.setPjtCode("");	
			table.setPjtName("");	
			table.setRegId("");	
			table.setRegName("");	
			table.setRegDate("");	
			table.setAppId("");	
			table.setAppName("");	
			table.setAppDate("");	
			table.setBomStatus("");	
			table.setAppNo("");	
			table.setMStatus("");	
			table.setPurpose("0");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MBOM_MASTER ��üLIST ��������
	//*******************************************************************/	
	public ArrayList getMasterList (String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM MBOM_MASTER";
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' order by pid desc";
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
				table = new mbomMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));	

				String mcode = rs.getString("model_code");
				String bom_status = rs.getString("bom_status");
				mcode = "<a href=\"javascript:masterView('"+pid+"');\">"+rs.getString("model_code")+"</a>";
				table.setModelCode(mcode);

				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	

				if(bom_status.equals("0")) bom_status = "�ݷ�";
				else if(bom_status.equals("1")) bom_status = "�ʱ���";
				else if(bom_status.equals("2")) bom_status = "���ø����";
				else if(bom_status.equals("3")) bom_status = "BOM���";
				else if(bom_status.equals("4")) bom_status = "��������";
				else if(bom_status.equals("5")) bom_status = "BOMȮ��";
				else bom_status = "";
				table.setBomStatus(bom_status);

				table.setAppNo(rs.getString("app_no"));	
				String m_status = rs.getString("m_status");
				if(m_status.equals("0")) m_status = "����";
				else if(m_status.equals("1")) m_status = "����";
				else if(m_status.equals("2")) m_status = "���";
				else m_status = "";
				table.setMStatus(m_status);	
				table.setPurpose(rs.getString("purpose"));	
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public mbomMasterTable getDisplayPage(String sItem,String sWord,
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
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM MBOM_MASTER";
		total_cnt = getTotalCount(query);

		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' order by pid desc";
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
			pagecut = "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=BomBaseInfoServlet?&mode=info_list&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}

		//arraylist�� ���
		table = new mbomMasterTable();
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
	// MBOM_MASTER �ӽ�BOM ���� ��������
	//*******************************************************************/	
	public ArrayList getTmpBomMasterList (String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where "+sItem+" like '%"+sWord+"%' and purpose='1' order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));

				String mcode = rs.getString("model_code");
				String bom_status = rs.getString("bom_status");
				mcode = "<a href=\"javascript:masterView('"+pid+"');\">"+rs.getString("model_code")+"</a>";
				table.setModelCode(mcode);

				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	

				if(bom_status.equals("0")) bom_status = "�ݷ�";
				else if(bom_status.equals("1")) bom_status = "�ʱ���";
				else if(bom_status.equals("2")) bom_status = "���ø����";
				else if(bom_status.equals("3")) bom_status = "BOM���";
				else if(bom_status.equals("4")) bom_status = "��������";
				else if(bom_status.equals("5")) bom_status = "BOMȮ��";
				else bom_status = "";
				table.setBomStatus(bom_status);

				table.setAppNo(rs.getString("app_no"));	
				String m_status = rs.getString("m_status");
				if(m_status.equals("0")) m_status = "����";
				else if(m_status.equals("1")) m_status = "����";
				else if(m_status.equals("2")) m_status = "���";
				else m_status = "";
				table.setMStatus(m_status);	
				table.setPurpose(rs.getString("purpose"));	
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER ���� ��Ȯ�� BOM LIST
	//*******************************************************************/	
	public ArrayList getXbomList (String sItem,String sWord,String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		String pid="",model_code="",fg_code="",mgr="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where bom_status in('0','1','2','3') and ";
		query += sItem+" like '%"+sWord+"%' order by pid desc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();	
				pid = rs.getString("pid");
				table.setPid(pid);	

				table.setModelgCode(rs.getString("modelg_code"));	
				table.setModelgName(rs.getString("modelg_name"));

				model_code = rs.getString("model_code");
				table.setModelCode(model_code);	
				table.setModelName(rs.getString("model_name"));

				fg_code = rs.getString("fg_code");
				fg_code = "<a href=\"javascript:goBranch('"+pid+"','"+model_code+"','"+fg_code+"');\">"+fg_code+"</a>";
				table.setFgCode(fg_code);

				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));	
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	
				table.setPurpose(rs.getString("purpose"));	

				//�������� ã��
				mgr = getFgGrade(login_id,pid);
				if(mgr.length() == 0) table_list.add(table);

		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER ���� ��Ȯ�� ù��° ���� : ���ʰ� ���ϱ�
	//*******************************************************************/	
	public String getXbomFirst() throws Exception
	{
		//���� �ʱ�ȭ
		String pid = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where bom_status in('0','1','2','3') order by pid desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) pid = rs.getString("pid");
		
		//���� �׸� ������ 
		stmt.close();
		rs.close();
		return pid;
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE �� ���� �޼ҵ� ����
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� MBOM_STR���� �б�
	//*******************************************************************/	
	public mbomStrTable readStrItem(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
		
		query = "SELECT * FROM mbom_str where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
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
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setAppStatus(rs.getString("app_status"));
			table.setTag(rs.getString("tag"));
			String part_type = getPartType(rs.getString("gid"),rs.getString("pid"),rs.getString("child_code"),rs.getString("level_no"));
			table.setPartType(part_type);
		} else {
			table.setPid(anbdt.getID());	
			table.setGid("");	
			table.setParentCode("");	
			table.setChildCode("");	
			table.setLevelNo("");	
			table.setPartName("");	
			table.setPartSpec("");	
			table.setLocation("");	
			table.setOpCode("");	
			table.setQtyUnit("");	
			table.setQty("");	
			table.setMakerName("");	
			table.setMakerCode("");	
			table.setPriceUnit("");	
			table.setPrice("");
			table.setAddDate("");	
			table.setBuyType("");	
			table.setEcoNo("");	
			table.setAdTag("");	
			table.setBomStartDate("0");	
			table.setBomEndDate("0");
			table.setAppStatus("0");
			table.setTag("0");
			table.setPartType("P");		//P:��ǰ, A:Assy
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	//	MBOM_STR���� �ش��ǰ�� PART���� ASSY�ڵ����� �Ǵ��ϱ� : �Ϻα��� �ֳ� ����
	//  ������ ������ ���� �������� ��� [�Ϻα����� ������ ���� �������]
	//*******************************************************************/
	public String getPartType(String gid,String pid,String child_code,String level_no) throws Exception
	{
		//���� �ʱ�ȭ
		String part_type = "A";		//Assy�ڵ���
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select count(*) from mbom_str where gid ='"+gid+"' and parent_code='"+child_code+"'";
		query += " and level_no > '"+level_no+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			int cnt = rs.getInt(1);
			if(cnt == 0) part_type = "P";	//��ǰ��
		}

		//�ߺ�Assy ǥ�� [assy_dup='D']�� ���� ���� : ��ǰ���� �ν���� �Ͽ� ������ �� �ֵ��� ��.
		query = "select assy_dup from mbom_str where gid='"+gid+"' and pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			String assy_dup = rs.getString("assy_dup");
			if(assy_dup.equals("D")) part_type="P";
		}
	
		stmt.close();
		rs.close();
		return part_type;			
	}

	//*******************************************************************
	//	MBOM_STR���� LEVEL_NO���ϱ�
	//*******************************************************************/
	public int getLevelNo(String query) throws Exception
	{
		//���� �ʱ�ȭ
		int level_no = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			level_no = Integer.parseInt(rs.getString("level_no"));
			level_no++;		//�ش緹������ +1���Ͽ� �����ش�
		}
		
		stmt.close();
		rs.close();
		return level_no;			
	}

	//--------------------------------------------------------------------
	//Ȯ���� BOM �����ϱ�
	//		BOM STRUCTURE �� ���� �޼ҵ� ����
	//			1. ������ [����]
	//			2. ������ [�ٴܰ�]
	//			3. ������
	//---------------------------------------------------------------------
	/**********************************************************************
	 * MBOM ������ �迭�� ��´�. 
	 * MBOM ������ TREE���Ͽ� ArrayList�� ��� : �� �ش� ��ǰ�� �ڵ常
	 *********************************************************************/
	public void saveSingleForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		//���� �ʱ�ȭ
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
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
			item_list.add(mst);
		}
		rs.close();
		stmt.close(); 
		
	} //saveSingleForwardItems

	/**********************************************************************
	 * MBOM������ ���� item�迭�� �����Ѵ�.
	 * MBOM ������ TREE���Ͽ� �迭�� ��� �����ϱ� : �� �ش��ǰ�� �ڵ常
	 *********************************************************************/
	public ArrayList getSingleForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		item_list = new ArrayList();
		saveSingleForwardItems(gid,level_no,parent_code);
		return item_list;
	}

	/**********************************************************************
	 * MBOM ������ �迭�� ��´�. 
	 * MBOM ������ TREE���Ͽ� ArrayList�� ��� : �Ϻα��� ��ü
	 *********************************************************************/
	public void saveForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		//���� �ʱ�ȭ
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));
			mst.setParentCode(rs.getString("parent_code"));	
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
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst);

			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveForwardItems(gid,lno,rs.getString("child_code"));
		}
		rs.close();
		stmt.close(); 
		
	} //saveForwardItems

	/**********************************************************************
	 * MBOM������ ���� item�迭�� �����Ѵ�.
	 * MBOM ������ TREE���Ͽ� �迭�� ��� �����ϱ� : �Ϻα��� ��ü
	 *********************************************************************/
	public ArrayList getForwardItems(String gid,String level_no,String parent_code) throws Exception
	{
		item_list = new ArrayList();
		saveForwardItems(gid,level_no,parent_code);	

		//����غ���
/*		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			//System.out.println(table.getParentCode()+":"+table.getChildCode()+":"+table.getLevelNo());
		}
*/
		return item_list;
	}

	/**********************************************************************
	 * MBOM ������ �迭�� ��´�. 
	 * MBOM ������ TREE���Ͽ� ArrayList�� ���
	 *********************************************************************/
	public void saveReverseItems(String child_code) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT distinct parent_code,child_code,level_no,part_name,part_spec,op_code from MBOM_STR ";
		query += "where child_code = '"+child_code+"' ";
		query += "order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setParentCode(rs.getString("parent_code"));
			mst.setChildCode(rs.getString("child_code"));
			mst.setLevelNo(rs.getString("level_no"));
			mst.setPartName(rs.getString("part_name"));
			mst.setPartSpec(rs.getString("part_spec"));
			mst.setOpCode(rs.getString("op_code"));
			item_list.add(mst);
			saveReverseItems(rs.getString("parent_code"));
		}
		rs.close();
		stmt.close(); 
		
	} //saveReverseItems

	/**********************************************************************
	 * MBOM������ ���� item�迭�� �����Ѵ�.
	 * MBOM ������ TREE���Ͽ� �迭�� ��� �����ϱ�
	 *********************************************************************/
	public ArrayList getReverseItems(String child_code) throws Exception
	{
		item_list = new ArrayList();
		saveReverseItems(child_code);	
		return item_list;
	}

	/**********************************************************************
	 * MBOM ������ �迭�� ��´�. : BOM �����
	 * MBOM ������ TREE���Ͽ� ArrayList�� ��� : �Ϻα��� ��ü
	 *********************************************************************/
	public void saveCopyForwardItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		//���� �ʱ�ȭ
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where level_no = '"+level_no+"' and parent_code = '"+parent_code+"' ";
		query += "and gid = '"+gid+"' and ";
		query += "((bom_start_date <='"+sel_date+"' and bom_end_date = '0') or "; 
		query += "(bom_start_date <='"+sel_date+"' and bom_end_date > '"+sel_date+"'))";
		query += " order by child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
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
			String assy_dup = rs.getString("assy_dup");
			mst.setAssyDup(assy_dup);
			item_list.add(mst);

			lno = Integer.toString(Integer.parseInt(rs.getString("level_no"))+1);
			if(!assy_dup.equals("D")) saveCopyForwardItems(gid,lno,rs.getString("child_code"),sel_date);
		}
		rs.close();
		stmt.close(); 
		
	} //saveForwardItems

	/**********************************************************************
	 * MBOM������ ���� item�迭�� �����Ѵ�. : BOM�����
	 * MBOM ������ TREE���Ͽ� �迭�� ��� �����ϱ� : �Ϻα��� ��ü
	 *********************************************************************/
	public ArrayList getCopyForwardItems(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		item_list = new ArrayList();
		saveCopyForwardItems(gid,level_no,parent_code,sel_date);	

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
	//--------------------------------------------------------------------
	//
	//		ȸ�ι��� ���� Location���� �� �ߺ��˻縦 ���� �޼ҵ�
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 * ȸ�� Assy�� �ִ� ȸ�κ�ǰ�� ���� Location�˻縦 ���� 
	 * [��� BOM�� ���� : �ۼ��߹� ���躯�� ���� ����]
	 *********************************************************************/
	public ArrayList getElectronicItems(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		item_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where gid = '"+gid+"' and parent_code like '"+ele_assy+"%' ";
		query += "and child_code like '"+ele_item+"%' ";
		query += "and adtag != 'D' and adtag != 'RB' ";
		query += "order by parent_code,child_code,location asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));	
			mst.setParentCode(rs.getString("parent_code"));	
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
			mst.setAssyDup(rs.getString("assy_dup"));
			item_list.add(mst);
		}
		rs.close();
		stmt.close(); 
		
		return item_list;
	} //getElectronicItems
	
	//--------------------------------------------------------------------
	//
	//		BOM ���� , ���̱� ������ ���� �޼ҵ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_MASTER FG�ڵ� �˻��ϱ�
	//*******************************************************************/	
	public ArrayList getFGList (String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				table.setPid(rs.getString("pid"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER FG�ڵ� �˻��ϱ� : ������ [Ȯ���� BOM ��]
	//*******************************************************************/	
	public ArrayList getAppFGList (String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' and bom_status='5' order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
								
				table.setPid(rs.getString("pid"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_MASTER FG�ڵ� �˻��ϱ� : ���̱��� [������ �����鼭,Ȯ��,�������� ������ BOM]
	//*******************************************************************/	
	public ArrayList getMakeFGList (String sWord,String login_id) throws Exception
	{
		//���� �ʱ�ȭ
		String pid = "",mgr="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = null;
		ArrayList table_list = new ArrayList();
		
		//query���� �����
		query = "SELECT * FROM MBOM_MASTER where fg_code like '"+sWord+"%' and bom_status in ('0','1','2','3') ";
		query += "order by fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomMasterTable();
							
				pid = rs.getString("pid");				
				table.setPid(pid);	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdgName(rs.getString("pdg_name"));
				table.setPdCode(rs.getString("pd_code"));	
				table.setPdName(rs.getString("pd_name"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setRegDate(rs.getString("reg_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppName(rs.getString("app_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setBomStatus(rs.getString("bom_status"));	
				table.setAppNo(rs.getString("app_no"));	
				table.setMStatus(rs.getString("m_status"));	

				//�������� ã��
				mgr = getFgGrade(login_id,pid);
				if(mgr.length() == 0) table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STR���� �ش� ���� Assy�ڵ常 ã�� : ù��ȣ�� 1�� ���۵�
	// �뵵 : ���� Import �Ҷ� ��� [�б⶧����]
	//*******************************************************************/	
	public ArrayList getAssyList(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		String level_no="",parent_code="",p_code="",where="",part_spec="",op_code="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy�ڵ� ã�� [1xxx:����ASSY,Fxxx:FG�ڵ� �� ���۵Ǵ� �ڵ�]
		query = "select distinct gid,parent_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and (parent_code like '"+assy_head+"%' and level_no != '0') ";
		query += "order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				table.setLevelNo(level_no);

				p_code = rs.getString("parent_code"); 
				parent_code = "<a href=\"javascript:goBranch('"+gid+"','"+level_no+"','"+p_code+"');\">"+p_code+"</a>";
				table.setParentCode(parent_code);

				where = "where item_no = '"+p_code+"'";
				part_spec = getColumData("ITEM_MASTER","item_desc",where);
				table.setPartSpec(part_spec);

				table.setOpCode(rs.getString("op_code"));
				table_list.add(table);
		}

		//������������ ����ASSY�ڵ尡 ������ �̸� �ٿ��ش�.
		query = "select gid,child_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and parent_code = '"+p_code+"' and level_no='"+level_no+"'";
		query += " and child_code like '"+assy_head+"%' order by level_no,child_code ASC";
		rs = stmt.executeQuery(query);
		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no) + 1);
				table.setLevelNo(level_no);

				p_code = rs.getString("child_code");
				parent_code = "<a href=\"javascript:goBranch('"+gid+"','"+level_no+"','"+p_code+"');\">"+p_code+"</a>";
				table.setParentCode(parent_code);

				where = "where item_no = '"+p_code+"'";
				part_spec = getColumData("ITEM_MASTER","item_desc",where);
				op_code = getColumData("ITEM_MASTER","op_code",where);
				table.setPartSpec(part_spec);

				table.setOpCode(op_code);
				////System.out.println(level_no+" : "+rs.getString("child_code")+" : "+parent_code.substring(len-2,len));
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MBOM_STR���� �ش� ���� Assy�ڵ常 ã�� : ù��ȣ�� 1�� ���۵�
	// �뵵 : 1. ����, ���̱� �Ҷ� ���
	//        2. BOM ������/������ �� �Ϸ�BOM�� Assy���� ã��
	//*******************************************************************/	
	public ArrayList getAssyListCP(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy�ڵ� ã�� [1xxx:����ASSY,Fxxx:FG�ڵ� �� ���۵Ǵ� �ڵ�]
		query = "select distinct gid,child_code,level_no,part_spec,op_code from mbom_str where gid='"+gid;
		query += "' and (child_code like '"+assy_head+"%' or level_no = '0') ";
		query += "order by level_no,child_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);
				String level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no)+1);	//��ǰ������ �˻������� ������ +1
				table.setLevelNo(level_no);
				table.setParentCode(rs.getString("child_code"));
				table.setPartSpec(rs.getString("part_spec"));
				table.setOpCode(rs.getString("op_code"));
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش�BOM������ ��ǰ���ڵ带 �̿��� �����ڵ� ã��
	//*******************************************************************/	
	public String getOpCode (String gid,String child_code) throws Exception
	{
		//���� �ʱ�ȭ
		String op_code = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		query = "select op_code from mbom_str where gid='"+gid+"' and child_code ='"+child_code+"' ";
		rs = stmt.executeQuery(query);

		if(rs.next()) { 
				op_code = rs.getString("op_code");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return op_code;
	}

	//*******************************************************************
	// MBOM_STR���� �ش� ���� Assy�ڵ常 ã�� : ù��ȣ�� 1�� ���۵�
	// �뵵 : 1. Part List Import �Ҷ� Template�� ��ϵ� Assy�ڵ� : tag='2'
	//        2. ǰ���ڵ�,�����ڵ�,Location �����϶�
	//*******************************************************************/	
	public ArrayList getAssyListTemp(String gid) throws Exception
	{
		//���� �ʱ�ȭ
		String parent_code="",level_no = "",op_code="",where="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//Assy�ڵ� ã�� [1xxx:����ASSY,Fxxx:FG�ڵ� �� ���۵Ǵ� �ڵ�]
		query = "select distinct gid,parent_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and (parent_code like '"+assy_head+"%' or level_no = '0') and tag='2'";
		query += "order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				table.setLevelNo(level_no);

				parent_code = rs.getString("parent_code");
				table.setParentCode(parent_code);

				table.setOpCode(rs.getString("op_code"));
				////System.out.println(level_no+" : "+rs.getString("parent_code")+" : "+rs.getString("op_code"));
				table_list.add(table);
		}

		//Template�ڵ�� ������ ���[tag=2] �������� ���ڵ嵵 �����ڵ������� �̸� �߰��Ѵ�.
		query = "select gid,child_code,level_no,op_code from mbom_str where gid='"+gid;
		query += "' and parent_code = '"+parent_code+"' and level_no='"+level_no+"' and tag='2'";
		query += " and child_code like '"+assy_head+"%' order by level_no,parent_code ASC";
		rs = stmt.executeQuery(query);
		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setGid(gid);

				level_no = rs.getString("level_no");
				level_no = Integer.toString(Integer.parseInt(level_no) + 1);
				table.setLevelNo(level_no);

				parent_code = rs.getString("child_code");
				table.setParentCode(parent_code);

				where = "where item_no = '"+parent_code+"'";
				op_code = getColumData("ITEM_MASTER","op_code",where);
				table.setOpCode(op_code);
				////System.out.println(level_no+" : "+rs.getString("child_code")+" : "+parent_code.substring(len-2,len));
				table_list.add(table);
		}


		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		��ǰ �����Ϳ��� �ʿ��� ���� �����ϱ�
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	//	ǰ���ڵ忡 �ش�Ǵ� ���������ϱ�
	// [0��ǰ�̸�,1�԰�,2����Ŀ��,3����,4��������,5item_type,6op_code] 
	// ���� : �԰ݸ� ���
	//*******************************************************************/
	public String[] getComponentInfo(String part_code) throws Exception
	{
		//���� �ʱ�ȭ
		String[] data = new String[7];
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		for(int i=0; i<6; i++) data[i] = "";		//�迭�ʱ�ȭ
		query = "SELECT * FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "������";
			data[1] = str.repWord(rs.getString("item_desc"),"'","`");
			data[2] = rs.getString("mfg_no");		if(data[2] == null) data[2] = "";
			data[3] = "";
			data[4] = rs.getString("stock_unit");	if(data[4] == null) data[4] = "EA";
			data[5] = rs.getString("item_type");	if(data[5] == null) data[5] = "4";
			data[6] = rs.getString("op_code");		if(data[6] == null) data[6] = "";
		}
		
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�ش� ǰ���ڵ��� SPEC���� ���ϱ�
	//*******************************************************************/
	public String getComponentSpec(String part_code) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT item_desc FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("item_desc");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	ǰ�� �ڵ� �˻��ϱ�
	//*******************************************************************/
	public ArrayList getComponentCode(String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//ǰ���ڵ� ã��
		query = "SELECT item_no,item_desc FROM item_master WHERE item_no like '"+sWord+"%' order by item_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setParentCode(rs.getString("item_no"));
				table.setPartSpec(rs.getString("item_desc"));
				table_list.add(table);
		}

		stmt.close();
		rs.close();
		return table_list;			
	}
	//--------------------------------------------------------------------
	//
	//		�ش� ��[FG �ڵ�]�� ������ �Ǵ��ϱ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	�ӽ�BOM ���������� �ִ��� �Ǵ��ϱ�
	//*******************************************************************/
	public String getTbomMgrGrade(String sabun) throws Exception
	{
		String msg = "",where="",mgr="";

		//�ӽ�BOM ����/��� �������� ã��
		where = "where owner like '"+sabun+"%' and keyname='TBOM_MGR'";
		mgr =getColumData("mbom_grade_mgr","keyname",where);

		//����� �� �ִ� ������ �ִ��� �Ǵ��ϱ�
		if(!mgr.equals("TBOM_MGR")) {
			msg = "�ӽ�BOM�� ���� ���������� �����ϴ�.";
		}

		return msg;
	}
	//*******************************************************************
	//	ECO AUDIT ���������� �ִ��� �Ǵ��ϱ�
	//*******************************************************************/
	public String getEcoAuditGrade(String sabun) throws Exception
	{
		String msg = "",where="",mgr="";

		//�ӽ�BOM ����/��� �������� ã��
		where = "where owner like '"+sabun+"%' and keyname='ECO_AUDIT'";
		mgr =getColumData("mbom_grade_mgr","keyname",where);

		//����� �� �ִ� ������ �ִ��� �Ǵ��ϱ�
		if(!mgr.equals("ECO_AUDIT")) {
			msg = "ECO AUDIT�� ���� ���������� �����ϴ�.";
		}

		return msg;
	}
	//*******************************************************************
	//	BOM�� ����[����,����,��ŵ�]�� �� �ִ� ������ �ִ��� �Ǵ��ϱ�
	//  sabun : ��� ,   pid : mbom_master�� �����ڵ�
	//*******************************************************************/
	public String getFgGrade(String sabun,String pid) throws Exception
	{
		String msg = "",where="",fg_code="FG",mgr_fg_code="MG";

		//MBOM_MASTER�� FG�ڵ� ã��
		where = "where pid='"+pid+"'";
		fg_code =getColumData("mbom_master","fg_code",where);

		//MBOM_GRADE_MGR�� FG�ڵ尡 ���ԵǾ��� ã��
		mgr_fg_code = getMgrFgCode(sabun);
	
		//����� �� �ִ� ������ �ִ��� �Ǵ��ϱ�
		if(mgr_fg_code.indexOf(fg_code) == -1) {
			msg = "�ش� ��[FG]������ �������� �����ϴ�.";
		}

		return msg;
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
	//	������������ LEVEL NO�� �հ�
	//*******************************************************************/
	public int sumTotalLevelNo(String query) throws Exception
	{
		//���� �ʱ�ȭ
		int cnt = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cnt += rs.getInt("level_no") + 1;
		}
		
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
	//	�־��� ���̺��� �־��� ������ �÷��� ������ �б�
	//*******************************************************************/
	public String getColumAllData(String tablename,String getcolumn,String where) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString(getcolumn)+",";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	�־�������� ����� �ڵ��
	//*******************************************************************/
	public String getDivCode(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select b.ac_code from user_table a,class_table b where (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString("ac_code");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	BOM GROUP������ �ش����� FG�ڵ� ���ϱ�
	//*******************************************************************/
	public String getMgrFgCode(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String fg_code = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select keyname from mbom_grade_mgr where owner like '%"+sabun+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			fg_code += rs.getString("keyname");
		}
	
		stmt.close();
		rs.close();
		return fg_code;			
	}

}

