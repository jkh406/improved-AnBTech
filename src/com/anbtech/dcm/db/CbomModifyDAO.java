package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//�����Է�
	
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
	public CbomModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		ECR�� ���� �޼ҵ� ����
	//
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

	//*******************************************************************
	// ECC COM ��üLIST ��������
	// id : login ���, code : login �μ������ڵ�
	//*******************************************************************/	
	public ArrayList getEccComList(String sItem,String sWord,String boxKind,String id,
			String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="";
		String code = getUserDivMgrCode(id);		//�ش����� �μ������ڵ�

		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();

		//�������� ���ǹ� �����
		if(boxKind.equals("IW")) {				//�����ۼ���
			where = "(ecr_id='"+id+"' or eco_id='"+id+"') and ";
			where += "ecc_status in ('1','6') ";
		} else if(boxKind.equals("IR")) {		//���μ�����
			where = "(ecr_id='"+id+"' and ecc_status='0') or ";			//ECR�ݷ�
			where += "(eco_id='"+id+"' and ecc_status='5') or ";		//ECO�ݷ�
			where += "(mgr_id='"+id+"' and ecc_status='3') or ";		//�������å���� ����
			where += "(eco_id='"+id+"' and ecc_status='4') ";			//����������� ����
		} else if(boxKind.equals("IS")) {		//���ι߽���
			where = "(ecr_id='"+id+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";	//ECR ����
			where += "(eco_id='"+id+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO ����
		} else if(boxKind.equals("DR")) {		//�μ�������
			where = "(mgr_code='"+code+"' and ecc_status='3') or ";		//ECR å��������
			where += "(eco_code='"+code+"' and ecc_status='4') ";		//ECR ���������
		} else if(boxKind.equals("DS")) {		//�μ��߽���
			where = "(ecr_code='"+code+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR ����
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO ����
		} else if(boxKind.equals("EA")) {		//ECR AUDIT
			where = "(ecc_status = '8') ";
		}
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM ECC_COM where ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM ECC_COM where "+sItem+" like '%"+sWord+"%' and ";
		query += where;
		query += "order by pid desc";
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
	// MBOM_MASTER ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ�
	//*******************************************************************/	
	public eccComTable getDisplayPage(String sItem,String sWord,String boxKind,String id,
			String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="",mode="";	
		String code = getUserDivMgrCode(id);		//�ش����� �μ������ڵ�

		Statement stmt = null;
		ResultSet rs = null;

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		eccComTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�������� ���ǹ� �����
		if(boxKind.equals("IW")) {				//�����ۼ���
			where = "(ecr_id='"+id+"' or eco_id='"+id+"') and ";
			where += "ecc_status in ('1','6') ";
			mode = "ecc_iwlist";
		} else if(boxKind.equals("IR")) {		//���μ�����
			where = "(ecr_id='"+id+"' and ecc_status='0') or ";			//ECR�ݷ�
			where += "(eco_id='"+id+"' and ecc_status='5') or ";		//ECO�ݷ�
			where += "(mgr_id='"+id+"' and ecc_status='3') or ";		//�������å���� ����
			where += "(eco_id='"+id+"' and ecc_status='4') ";			//����������� ����
			mode = "ecc_irlist";
		} else if(boxKind.equals("IS")) {		//���ι߽���
			where = "(ecr_id='"+id+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR ����
			where += "(eco_id='"+id+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO ����
			mode = "ecc_islist";
		} else if(boxKind.equals("DR")) {		//�μ�������
			where = "(mgr_code='"+code+"' and eco_no like 'ECR%' and ecc_status='3') or ";		//ECR å��������
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status='4') ";		//ECR ���������
			mode = "ecc_drlist";
		} else if(boxKind.equals("DS")) {		//�μ��߽���
			where = "(ecr_code='"+code+"' and eco_no like 'ECR%' and ecc_status in ('2','3','4','5','6','7','8')) or ";//ECR ����
			where += "(eco_code='"+code+"' and eco_no like 'ECO%' and ecc_status in ('7','8')) ";						//ECO ����
			mode = "ecc_dslist";
		} else if(boxKind.equals("EA")) {		//ECR AUDIT
			where = "(ecc_status = '8') ";
		}
		
		//�Ѱ��� ���ϱ�
		query = "SELECT COUNT(*) FROM ECC_COM where ";
		query += where;
		total_cnt = getTotalCount(query);
	
		//query���� �����
		query = "SELECT * FROM ECC_COM where "+sItem+" like '%"+sWord+"%' and ";
		query += where;
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
			pagecut = "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">[Prev]</a>";
		}

		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">["+curpage+"]</a>";
			}
		
			curpage++;
		}

		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=CbomProcessServlet?&mode="+mode+"&page="+curpage+"&sItem="+sItem+"&sWord="+sWord+"&boxKind="+boxKind+"&id="+id+"&code="+code+">[Next]</a>";
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
	//		���躯�� �׸������ ���� ����
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	���躯�� �����׸� ��������
	//		F01xx : ��������  ECO�� ���
	//		F02xx : ���뱸��  ECO�� ���
	//		F03xx : �������  ECO�� ���
	//		F04xx : ��������  ECR�� ���
	//*******************************************************************/
	public ArrayList getEccItem(String keyword) throws Exception
	{
		//���� �ʱ�ȭ
		String query="",where="";	
		Statement stmt = null;
		ResultSet rs = null;

		stmt = con.createStatement();
		mbomEnvTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT spec FROM mbom_env WHERE m_code LIKE '"+keyword+"[0-9][0-9]' ORDER BY m_code ASC";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomEnvTable();
				table.setSpec(rs.getString("spec"));
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

	//*******************************************************************
	//	������ ���� ���ϱ� 
	//   ���,�̸�,�μ������ڵ�,�μ��ڵ�,�μ���,��ȭ��ȣ
	//*******************************************************************/
	public String[] getUserData(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String data[] = new String[6]; 
		for(int i=0; i<6; i++) data[i]="";

		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT a.id,a.name,b.code,b.ac_code,b.ac_name,a.office_tel FROM ";
		query += "user_table a,class_table b WHERE (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("id");
			data[1] = rs.getString("name");
			data[2] = rs.getString("code");
			data[3] = rs.getString("ac_code");
			data[4] = rs.getString("ac_name");
			data[5] = rs.getString("office_tel");
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	������� �μ������ڵ� ã�� 
	//*******************************************************************/
	public String getUserDivMgrCode(String sabun) throws Exception
	{
		//���� �ʱ�ȭ
		String data = ""; 
		
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "SELECT a.id,a.name,b.code,b.ac_code,b.ac_name,a.office_tel FROM ";
		query += "user_table a,class_table b WHERE (a.id ='"+sabun+"' and a.ac_id = b.ac_id)";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("code");
		stmt.close();
		rs.close();
		return data;			
	}

}

