package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomHistoryDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//�����Է�
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó��
	
	private String query = "";
	private ArrayList item_list = null;				//PART������ ArrayList�� ���
	private mbomStrTable mst = null;				//help class : MBOM_STR
	private eccComTable ecdt = null;				//help class : ECC_COM
	private eccReqTable ecrt = null;				//help class : ECC_REQ
	private eccOrdTable ecot = null;				//help class : ECC_ORD
	private eccBomTable ecbt = null;				//help class : ECC_BOM
	private eccModelTable ecmt = null;				//help class : ECC_MODEL
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public CbomHistoryDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		ECR�� ���� �޼ҵ� ����
	//		ECC_COM,ECC_REQ,ECC_ORD�б�
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ������ȣ�� �ش� ECC_COM ���� �б�
	//*******************************************************************/	
	public eccComTable readEccCom(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccComTable table = new com.anbtech.dcm.entity.eccComTable();
		
		query = "SELECT * FROM ecc_com where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setEccSubject(rs.getString("ecc_subject"));
			table.setEcoNo(rs.getString("eco_no"));	
			table.setEcrId(rs.getString("ecr_id"));	
			table.setEcrName(rs.getString("ecr_name"));	
			table.setEcrCode(rs.getString("ecr_code"));	
			table.setEcrDivCode(rs.getString("ecr_div_code"));	
			table.setEcrDivName(rs.getString("ecr_div_name"));	
			table.setEcrTel(rs.getString("ecr_tel"));	
			table.setEcrDate(rs.getString("ecr_date"));	
			table.setMgrId(rs.getString("mgr_id"));	
			table.setMgrName(rs.getString("mgr_name"));	
			table.setMgrCode(rs.getString("mgr_code"));	
			table.setMgrDivCode(rs.getString("mgr_div_code"));	
			table.setMgrDivName(rs.getString("mgr_div_name"));	
			table.setEcoId(rs.getString("eco_id"));	
			table.setEcoName(rs.getString("eco_name"));	
			table.setEcoCode(rs.getString("eco_code"));	
			table.setEcoDivCode(rs.getString("eco_div_code"));	
			table.setEcoDivName(rs.getString("eco_div_name"));	
			table.setEcoTel(rs.getString("eco_tel"));	
			table.setEccReason(rs.getString("ecc_reason"));	
			table.setEccFactor(rs.getString("ecc_factor"));	
			table.setEccScope(rs.getString("ecc_scope"));	
			table.setEccKind(rs.getString("ecc_kind"));	
			table.setPdgCode(rs.getString("pdg_code"));	
			table.setPdCode(rs.getString("pd_code"));	
			table.setFgCode(rs.getString("fg_code"));	
			table.setPartCode(rs.getString("part_code"));	
			table.setOrderDate(rs.getString("order_date"));	
			table.setFixDate(rs.getString("fix_date"));	
			table.setEccStatus(rs.getString("ecc_status"));	
		} else {
			table.setPid(anbdt.getID());	
			table.setEccSubject("");	
			table.setEcoNo("");	
			table.setEcrId("");	
			table.setEcrName("");	
			table.setEcrCode("");	
			table.setEcrDivCode("");	
			table.setEcrDivName("");	
			table.setEcrTel("");	
			table.setEcrDate(anbdt.getDateNoformat());	
			table.setMgrId("");	
			table.setMgrName("");
			table.setMgrCode("");	
			table.setMgrDivCode("");	
			table.setMgrDivName("");	
			table.setEcoId("");	
			table.setEcoName("");	
			table.setEcoCode("");	
			table.setEcoDivCode("");	
			table.setEcoDivName("");	
			table.setEcoTel("");	
			table.setEccReason("");	
			table.setEccFactor("");	
			table.setEccScope("");	
			table.setEccKind("");	
			table.setPdgCode("");	
			table.setPdCode("");	
			table.setFgCode("");	
			table.setPartCode("");	
			table.setOrderDate("");	
			table.setFixDate("");	
			table.setEccStatus("");
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ������ȣ�� �ش� ECC_REQ ���� �б�
	//*******************************************************************/	
	public eccReqTable readEccReq(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccReqTable table = new com.anbtech.dcm.entity.eccReqTable();
		
		query = "SELECT * FROM ecc_req where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setChgPosition(rs.getString("chg_position"));	
			table.setTrouble(rs.getString("trouble"));	
			table.setCondition(rs.getString("condition"));	
			table.setSolution(rs.getString("solution"));	
			table.setFname(rs.getString("fname"));	
			table.setSname(rs.getString("sname"));	
			table.setFtype(rs.getString("ftype"));	
			table.setFsize(rs.getString("fsize"));	
			table.setAppNo(rs.getString("app_no"));	
		} else {
			table.setPid("");	
			table.setChgPosition("");	
			table.setTrouble("");	
			table.setCondition("");	
			table.setSolution("");	
			table.setFname("");	
			table.setSname("");	
			table.setFtype("");	
			table.setFsize("");	
			table.setAppNo("");	
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// ������ȣ�� �ش� ECC_ORD ���� �б�
	//*******************************************************************/	
	public eccOrdTable readEccOrd(String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccOrdTable table = new com.anbtech.dcm.entity.eccOrdTable();
		
		query = "SELECT * FROM ecc_ord where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setChgPosition(rs.getString("chg_position"));	
			table.setTrouble(rs.getString("trouble"));	
			table.setCondition(rs.getString("condition"));	
			table.setSolution(rs.getString("solution"));	
			table.setFname(rs.getString("fname"));	
			table.setSname(rs.getString("sname"));	
			table.setFtype(rs.getString("ftype"));	
			table.setFsize(rs.getString("fsize"));	
			table.setAppNo(rs.getString("app_no"));	
		} else {
			table.setPid("");	
			table.setChgPosition("");	
			table.setTrouble("");	
			table.setCondition("");	
			table.setSolution("");	
			table.setFname("");	
			table.setSname("");	
			table.setFtype("");	
			table.setFsize("");	
			table.setAppNo("");	
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}
	//--------------------------------------------------------------------
	//
	//		�˻����ǿ� �´� ����Ʈ ���
	//		
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// ECC COM ��üLIST ��������
	//  �˻����� [�⺻����] : ecc_subject,eco_no, ecr_s_date/ecr_e_date[���ǽ���/����]
	//						  ecr_name,eco_s_date/eco_e_date[�������/����]
	//						  eco_name,ecc_status
	//*******************************************************************/	
	public ArrayList getBaseEccComList(String ecc_subject,String eco_no,String ecr_s_date,String ecr_e_date,
			String ecr_name,String eco_s_date,String eco_e_date,String eco_name,String ecc_status,
			String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		ecr_s_date = str.repWord(ecr_s_date,"/","");		//������ �˻�������
		ecr_e_date = str.repWord(ecr_e_date,"/","");		//������ �˻�������
		eco_s_date = str.repWord(eco_s_date,"/","");		//������ �˻�������
		eco_e_date = str.repWord(eco_e_date,"/","");		//������ �˻�������

		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where ���� �����
		where = "ecc_subject like '%"+ecc_subject+"%' and eco_no like '%"+eco_no+"%' and ";
		where += "ecr_date >= '"+ecr_s_date+"' and ecr_date <= '"+ecr_e_date+"' and ";
		where += "order_date >= '"+eco_s_date+"' and order_date <= '"+eco_e_date+"' and ";
		where += "ecr_name like '%"+ecr_name+"%' and eco_name like '%"+eco_name+"%' and ";
		where += "ecc_status like '%"+ecc_status+"%'";

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM ECC_COM where "+where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM ECC_COM where "+where;
		query += " order by eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);
			
				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	
				
				ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR�ݷ�";
				else if(ecc_status.equals("1")) ecc_status = "ECR�ۼ�";
				else if(ecc_status.equals("2")) ecc_status = "ECR����";
				else if(ecc_status.equals("3")) ecc_status = "ECRå��������";
				else if(ecc_status.equals("4")) ecc_status = "ECR���������";
				else if(ecc_status.equals("5")) ecc_status = "ECO�ݷ�";
				else if(ecc_status.equals("6")) ecc_status = "ECO�ۼ�";
				else if(ecc_status.equals("7")) ecc_status = "ECO����";
				else if(ecc_status.equals("8")) ecc_status = "ECO����";
				else if(ecc_status.equals("9")) ecc_status = "ECOȮ��";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public eccComTable getBaseDisplayPage(String ecc_subject,String eco_no,String ecr_s_date,String ecr_e_date,
			String ecr_name,String eco_s_date,String eco_e_date,String eco_name,String ecc_status,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		ecr_s_date = str.repWord(ecr_s_date,"/","");		//������ �˻�������
		ecr_e_date = str.repWord(ecr_e_date,"/","");		//������ �˻�������
		eco_s_date = str.repWord(eco_s_date,"/","");		//������ �˻�������
		eco_e_date = str.repWord(eco_e_date,"/","");		//������ �˻�������

		String query="",where="",mode="sch_base";	
		String para = "&ecc_subject="+ecc_subject+"&eco_no="+eco_no+"&ecr_s_date="+ecr_s_date+"&ecr_e_date=";
			  para += ecr_e_date+"&ecr_name="+ecr_name+"&eco_s_date="+eco_s_date+"&eco_e_date="+eco_e_date;
			  para += "&eco_name="+eco_name+"&ecc_status="+ecc_status;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where ���� �����
		where = "ecc_subject like '%"+ecc_subject+"%' and eco_no like '%"+eco_no+"%' and ";
		where += "ecr_date >= '"+ecr_s_date+"' and ecr_date <= '"+ecr_e_date+"' and ";
		where += "order_date >= '"+eco_s_date+"' and order_date <= '"+eco_e_date+"' and ";
		where += "ecr_name like '%"+ecr_name+"%' and eco_name like '%"+eco_name+"%' and ";
		where += "ecc_status like '%"+ecc_status+"%'";
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM ECC_COM where "+where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM ECC_COM where "+where;
		query += "order by pid desc";
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist�� ���
		table = new eccComTable();
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
	// ECC COM ��üLIST ��������
	//  �˻����� [��������] : pdg_code,pd_code,e_fg_code[�߻�FG],a_fg_code[����FG]
	//						  part_code[�߻���ǰ],item_code[�����ǰ]
	//						  ecc_reason,ecc_factor,ecc_scope,ecc_kind
	//*******************************************************************/	
	public ArrayList getConditionEccComList(String pdg_code,String pd_code,String e_fg_code,String a_fg_code,
			String part_code,String item_code,String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,
			String page,int max_display_cnt) throws Exception
	{
		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where ���� �����
		where = "pdg_code like '%"+pdg_code+"%' and pd_code like '%"+pd_code+"%' and ";
		where += "a.fg_code like '%"+e_fg_code+"%' and b.fg_code like '%"+a_fg_code+"%' and ";
		where += "a.ecc_reason like '%"+ecc_reason+"%' and ecc_factor like '%"+ecc_factor+"%' and ";
		where += "ecc_scope like '%"+ecc_scope+"%' and ecc_kind like '%"+ecc_kind+"%' and ";
		where += "a.part_code like '%"+part_code+"%' and ";
		where += "(c.parent_code like '%"+item_code+"%' or c.child_code like '"+item_code+"%') and "; 
		where += "a.eco_no = b.eco_no and b.eco_no = c.eco_no and a.eco_no = c.eco_no";

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		query += " order by a.eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);

				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	

				String ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR�ݷ�";
				else if(ecc_status.equals("1")) ecc_status = "ECR�ۼ�";
				else if(ecc_status.equals("2")) ecc_status = "ECR����";
				else if(ecc_status.equals("3")) ecc_status = "ECRå��������";
				else if(ecc_status.equals("4")) ecc_status = "ECR���������";
				else if(ecc_status.equals("5")) ecc_status = "ECO�ݷ�";
				else if(ecc_status.equals("6")) ecc_status = "ECO�ۼ�";
				else if(ecc_status.equals("7")) ecc_status = "ECO����";
				else if(ecc_status.equals("8")) ecc_status = "ECO����";
				else if(ecc_status.equals("9")) ecc_status = "ECOȮ��";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public eccComTable getConditionDisplayPage(String pdg_code,String pd_code,String e_fg_code,String a_fg_code,
			String part_code,String item_code,String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		String query="",where="",mode="sch_condition";	
		String para = "&pdg_code="+pdg_code+"&pd_code="+pd_code+"&e_fg_code="+e_fg_code+"&a_fg_code=";
			  para += a_fg_code+"&ecc_reason="+ecc_reason+"&ecc_factor="+ecc_factor+"&ecc_scope="+ecc_scope;
			  para += "&ecc_kind="+ecc_kind+"&part_code="+part_code+"&item_code="+item_code;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where ���� �����
		where = "pdg_code like '%"+pdg_code+"%' and pd_code like '%"+pd_code+"%' and ";
		where += "a.fg_code like '%"+e_fg_code+"%' and b.fg_code like '%"+a_fg_code+"%' and ";
		where += "a.ecc_reason like '%"+ecc_reason+"%' and ecc_factor like '%"+ecc_factor+"%' and ";
		where += "ecc_scope like '%"+ecc_scope+"%' and ecc_kind like '%"+ecc_kind+"%' and ";
		where += "a.part_code like '%"+part_code+"%' and ";
		where += "(c.parent_code like '%"+item_code+"%' or c.child_code like '"+item_code+"%') and "; 
		where += "a.eco_no = b.eco_no and b.eco_no = c.eco_no and a.eco_no = c.eco_no";

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_MODEL b, ECC_BOM c where "+where;
		query += " order by a.eco_no desc";
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist�� ���
		table = new eccComTable();
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
	// ECC COM ��üLIST ��������
	//  �˻����� [�⺻����] : condition,solution,chg_position,trouble
	//*******************************************************************/	
	public ArrayList getContentEccComList(String condition,String solution,
			String chg_position,String trouble,String page,int max_display_cnt) throws Exception
	{
		String query="",where="";
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//where ���� �����
		where = "(b.condition like '%"+condition+"%' and b.solution like '%"+solution+"%' and ";
		where += "b.chg_position like '%"+chg_position+"%' and b.trouble like '%"+trouble+"%' and a.pid=b.pid) or ";
		where += "(c.condition like '%"+condition+"%' and c.solution like '%"+solution+"%' and ";
		where += "c.chg_position like '%"+chg_position+"%' and c.trouble like '%"+trouble+"%' and a.pid=c.pid)";

		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		total_cnt = getTotalCount(query);

		//query���� �����
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		query += " order by a.eco_no desc";
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
				table = new eccComTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String ecc_subject = rs.getString("ecc_subject");
				ecc_subject = "<a href=\"javascript:eccView('"+pid+"');\">"+ecc_subject+"</a>";
				table.setEccSubject(ecc_subject);

				table.setEcoNo(rs.getString("eco_no"));	
				table.setEcrId(rs.getString("ecr_id"));	
				table.setEcrName(rs.getString("ecr_name"));	
				table.setEcrCode(rs.getString("ecr_code"));	
				table.setEcrDivCode(rs.getString("ecr_div_code"));	
				table.setEcrDivName(rs.getString("ecr_div_name"));	
				table.setEcrTel(rs.getString("ecr_tel"));	
				table.setEcrDate(rs.getString("ecr_date"));	
				table.setMgrId(rs.getString("mgr_id"));	
				table.setMgrName(rs.getString("mgr_name"));	
				table.setMgrCode(rs.getString("mgr_code"));	
				table.setMgrDivCode(rs.getString("mgr_div_code"));	
				table.setMgrDivName(rs.getString("mgr_div_name"));	
				table.setEcoId(rs.getString("eco_id"));	
				table.setEcoName(rs.getString("eco_name"));	
				table.setEcoCode(rs.getString("eco_code"));	
				table.setEcoDivCode(rs.getString("eco_div_code"));	
				table.setEcoDivName(rs.getString("eco_div_name"));	
				table.setEcoTel(rs.getString("eco_tel"));	
				table.setEccReason(rs.getString("ecc_reason"));	
				table.setEccFactor(rs.getString("ecc_factor"));	
				table.setEccScope(rs.getString("ecc_scope"));	
				table.setEccKind(rs.getString("ecc_kind"));	
				table.setPdgCode(rs.getString("pdg_code"));	
				table.setPdCode(rs.getString("pd_code"));	
				table.setFgCode(rs.getString("fg_code"));	
				table.setPartCode(rs.getString("part_code"));	
				table.setOrderDate(rs.getString("order_date"));	
				table.setFixDate(rs.getString("fix_date"));	

				String ecc_status = rs.getString("ecc_status");
				if(ecc_status.equals("0")) ecc_status = "ECR�ݷ�";
				else if(ecc_status.equals("1")) ecc_status = "ECR�ۼ�";
				else if(ecc_status.equals("2")) ecc_status = "ECR����";
				else if(ecc_status.equals("3")) ecc_status = "ECRå��������";
				else if(ecc_status.equals("4")) ecc_status = "ECR���������";
				else if(ecc_status.equals("5")) ecc_status = "ECO�ݷ�";
				else if(ecc_status.equals("6")) ecc_status = "ECO�ۼ�";
				else if(ecc_status.equals("7")) ecc_status = "ECO����";
				else if(ecc_status.equals("8")) ecc_status = "ECO����";
				else if(ecc_status.equals("9")) ecc_status = "ECOȮ��";
				else ecc_status = "";
				table.setEccStatus(ecc_status);	

				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ECC_COM ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public eccComTable getContentDisplayPage(String condition,String solution,
			String chg_position,String trouble,String page,int max_display_cnt,int max_display_page) throws Exception
	{
		String query="",where="",mode="sch_content";	
		String para = "&condition="+condition+"&solution="+solution+"&chg_position=";
			  para += chg_position+"&trouble="+trouble;
		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//where ���� �����
		where = "(b.condition like '%"+condition+"%' and b.solution like '%"+solution+"%' and ";
		where += "b.chg_position like '%"+chg_position+"%' and b.trouble like '%"+trouble+"%' and a.pid=b.pid) or ";
		where += "(c.condition like '%"+condition+"%' and c.solution like '%"+solution+"%' and ";
		where += "c.chg_position like '%"+chg_position+"%' and c.trouble like '%"+trouble+"%' and a.pid=c.pid)";


		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(distinct a.eco_no) FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		total_cnt = getTotalCount(query);

		//query���� �����
		String scnt = "distinct a.pid,a.ecc_subject,a.eco_no,a.ecr_id,a.ecr_name,a.ecr_code,a.ecr_div_code,a.ecr_div_name,";
		scnt += "a.ecr_tel,a.ecr_date,a.mgr_id,a.mgr_name,a.mgr_code,a.mgr_div_code,a.mgr_div_name,";
		scnt += "a.eco_id,a.eco_name,a.eco_code,a.eco_div_code,a.eco_div_name,a.eco_tel,";
		scnt += "a.ecc_reason,a.ecc_factor,a.ecc_scope,a.ecc_kind,a.pdg_code,a.pd_code,";
		scnt += "a.fg_code,a.part_code,a.order_date,a.fix_date,a.ecc_status";

		query = "SELECT "+scnt+" FROM ECC_COM a, ECC_REQ b, ECC_ORD c where "+where;
		query += " order by a.eco_no desc";
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
			pagecut = "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomHistoryServlet?&mode="+mode+"&page="+curpage+para+">[Next]</a>";
		}

		//arraylist�� ���
		table = new eccComTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		���躯�� ����𵨿� ���� �޼ҵ�
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	���躯�� ����� ECO_NO�� LIST
	//		TABLE : ECC_MODEL
	//*******************************************************************/
	public ArrayList getEcoModel(String eco_no) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="";	
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		eccModelTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM ecc_model WHERE eco_no ='"+eco_no+"' ORDER BY fg_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new eccModelTable();
				table.setModelCode(rs.getString("model_code"));
				table.setModelName(rs.getString("model_name"));
				table.setFgCode(rs.getString("fg_code"));
				table_list.add(table);
		}
		return table_list;
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
}

