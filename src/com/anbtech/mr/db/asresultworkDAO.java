package com.anbtech.mr.db;
import com.anbtech.mr.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asresultworkDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����ó��
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public asresultworkDAO(Connection con) 
	{
		this.con = con;
	}

	public asresultworkDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� [�ش��ü�� ��ü����]
	//*******************************************************************/
	private int getTotalCount(String company_no,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	//*******************************************************************
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getTotalPage() 
	{
		return this.total_page;
	}

	//*******************************************************************
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getCurrentPage() 
	{
		return this.current_page;
	}
	
	//*******************************************************************
	// AS ���������̷����� ��üLIST [�ش��ü��]
	//*******************************************************************/	
	public ArrayList getWorkList (String company_no,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		total_cnt = getTotalCount(company_no,sItem,sWord);
	
		//query���� �����
		query = "SELECT * FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%' order by pid desc"; 
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
				table = new assupportTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String register_no = rs.getString("register_no"); if(register_no == null) register_no = "";
				table.setRegisterNo(register_no);	
				
				table.setRegisterDate(rs.getString("register_date"));
				table.setAsField(rs.getString("as_field"));	
				table.setCode(rs.getString("code"));
				table.setRequestName(rs.getString("request_name"));
				table.setSerialNo(rs.getString("serial_no"));
				table.setRequestDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setAsDate(anbdt.getSepDate(rs.getString("as_date"),"-"));
				table.setAsType(rs.getString("as_type"));
				
				String as_content = rs.getString("as_content");
				if(as_content.length() > 30) as_content = as_content.substring(0,30)+" ...";
				as_content = "<a href=\"javascript:workView('"+pid+"');\">"+as_content+"</a>";
				table.setAsContent(as_content); 

				table.setAsResult(rs.getString("as_result"));
				table.setAsDelay(rs.getString("as_delay"));
				table.setAsIssue(rs.getString("as_issue"));
				table.setWorker(rs.getString("worker"));
				table.setCompanyNo(rs.getString("company_no"));

				String value_request = rs.getString("value_request");
				table.setCompanyNo(value_request);

				//�Ẹ����,����,����,�������� ǥ�� [��,����/������ �Ẹ�������� ���� ������]
				String subMod="",subDel="",subView="",subMail="";
				if(value_request.equals("0")) {				//�򰡿�û�� ����
					subMod = "<a href=\"javascript:workModify('"+pid+"');\"><img src='../mr/images/lt_modify.gif' border='0' align='absmiddle'></a>";
					subDel = "<a href=\"javascript:workDelete('"+pid+"');\"><img src='../mr/images/lt_del.gif' border='0' align='absmiddle'></a>";
					subMail = "<a href=\"javascript:workMail('"+pid+"');\"><img src='../mr/images/lt_mail_e.gif' border='0' align='absmiddle'></a>";
				} else if(register_no.length() == 0){		//�ʱ��򰡿�û�� �򰡵��˿�
					subMail = "<a href=\"javascript:workMail('"+pid+"');\"><img src='../mr/images/lt_mail_e.gif' border='0' align='absmiddle'></a>";
				} 
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setMail(subMail);
				
				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ȭ�鿡�� �������� �ٷΰ��� ǥ���ϱ� [�ش��ü��]
	//*******************************************************************/	
	public ArrayList getDisplayPage(String company_no,String sItem,String sWord,
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
		assupportTable table = null;
		ArrayList table_list = new ArrayList();
		
		//�Ѱ��� ���ϱ�
		total_cnt = getTotalCount(company_no,sItem,sWord);

		//query���� �����
		query = "SELECT * FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%' order by as_date desc"; 
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
			pagecut = "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}
		//�߰�
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}
		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}
	
		//arraylist�� ���
		table = new assupportTable();
		table.setPageCut(pagecut);							//������ �� �ִ� ������ ǥ��
		table.setTotalPage(total_page);						//����������
		table.setCurrentPage(Integer.parseInt(page));		//����������
		table.setTotalArticle(total_cnt);					//�� ���װ���
		table_list.add(table);

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �ش��ü AS���� ��������󼼺���
	//*******************************************************************/	
	public ArrayList getWorkRead (String pid) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM as_result where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setPid(rs.getString("pid"));
				String register_no = rs.getString("register_no"); if(register_no == null) register_no = "";
				table.setRegisterNo(register_no);	
				table.setRegisterDate(rs.getString("register_date"));
				table.setAsField(rs.getString("as_field"));	
				table.setCode(rs.getString("code"));
				table.setRequestName(rs.getString("request_name"));
				table.setSerialNo(rs.getString("serial_no"));
				table.setRequestDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setAsDate(anbdt.getSepDate(rs.getString("as_date"),"-"));
				table.setAsType(rs.getString("as_type"));
				table.setAsContent(rs.getString("as_content"));
				table.setAsResult(rs.getString("as_result"));
				table.setAsDelay(rs.getString("as_delay"));
				table.setAsIssue(rs.getString("as_issue"));
				table.setWorker(rs.getString("worker"));
				table.setCompanyNo(rs.getString("company_no"));
				table.setCompanyNo(rs.getString("value_request"));
				
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* ���� �Է��ϱ�
	*******************************************************************/
	public void inputWork(String as_field,String code,String request_name,String serial_no,String request_date,
		String as_date,String as_type,String as_content,String as_result,String as_delay,
		String as_issue,String worker,String company_no) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�⺻���� �����ϱ�
		String pid = getID();								//������ȣ
		String register_no = "";							//��Ϲ�ȣ
		String register_date = anbdt.getDateNoformat();		//�����
		String value_request = "0";							//�򰡿�ûȽ��

		//�����Է��ϱ� : as_result
		input = "INSERT INTO as_result(pid,register_no,register_date,as_field,code,request_name,serial_no,";
		input += "request_date,as_date,as_type,as_content,as_result,as_delay,as_issue,worker,company_no,";
		input += "value_request) values('";
		input += pid+"','"+register_no+"','"+register_date+"','"+as_field+"','"+code+"','"+request_name+"','"+serial_no+"','";
		input += request_date+"','"+as_date+"','"+as_type+"','"+as_content+"','"+as_result+"','"+as_delay+"','";
		input += as_issue+"','"+worker+"','"+company_no+"','"+value_request+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* ���� �����ϱ� : �򰡿�û ������
	*******************************************************************/
	public void updateWork(String pid,String as_field,String serial_no,String request_date,
		String as_date,String as_type,String as_content,String as_result,String as_delay,
		String as_issue) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//�⺻���� �����ϱ�
		String register_date = anbdt.getDateNoformat();		//�����

		//�������� �����ϱ� [as_result]
		update = "UPDATE as_result set as_field='"+as_field+"',serial_no='"+serial_no+"',request_date='"+request_date;
		update += "',register_date='"+register_date+"',as_date='"+as_date+"',as_type='"+as_type;
		update += "',as_content='"+as_content+"',as_result='"+as_result+"',as_delay='"+as_delay;
		update += "',as_issue='"+as_issue+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* ���� �����ϱ� : �򰡿�û ������
	*******************************************************************/
	public void deleteWork(String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "";
		
		stmt = con.createStatement();

		//�����ϱ�
		delete = "DELETE from as_result where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}


	/*********************************************************************
	 	�۾��Ϸ᳻���� AS��û�ڿ��� ���ڿ������� ������
	*********************************************************************/
/*	public void sendMailToDIV(String u_id,String u_name,String pjt_code,String pjt_name,String node_code,String node_name,
		String evt_content,String evt_note,String evt_issue) throws Exception 
	{	
		String pid = getID();								//������ȣ
		String subject = "";								//����
		String user_id = "", user_name = "", rec = "";		//�ۼ��� ���,�̸�,������List
		String write_date = anbdt.getTime();				//���ڿ��� ��������
		String delete_date = anbdt.getAddMonthNoformat(1);	//������������

		//1.�ۼ���[����PM] ����
		user_id = u_id;											//�ۼ��� ���
		user_name = u_name;										//�ۼ��� �̸�
		subject = "���Ϸ� ���ο�û";								//����
		String bon_path = "/post/"+user_id+"/text_upload";		//�����н�
		String filename = pid;									//�������� ���ϸ�

		//2.����PM ���ϱ�
		rec = searchPjtPM(pjt_code)+";";						//������ [���/�̸�;]
		String rec_id = rec.substring(0,rec.indexOf("/"));
//		rec = rec_id+"/"+rec_name+";";							//������ 

		//3.���ڿ������� ������
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + "CFM" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";		
		stmt.executeUpdate(master);

		//4.�������� �����
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>����۾� �Ϸ���� ��û</title></head>";
			content += "<body>";
			content += "<h3>����۾� �Ϸ᳻��</h3>";
			content += "<ul>";
			content += "<li>�����̸� : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>����̸� : "+node_code+" "+node_name+"</li>";
			content += "<li>�ֿ䳻�� : <pre>"+evt_content+"</pre></li>";
			content += "<li>�ֿ乮�� : <pre>"+evt_note+"</pre></li>";
			content += "<li>�ֿ��̽� : <pre>"+evt_issue+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		stmt.close();
	}
*/
	//*******************************************************************
	// A/S�����о� ��ϰ�������
	//*******************************************************************/	
	public ArrayList getAsField() throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT register_name,sno FROM as_env where mgr_name='AS_FIELD' and use_yn='1'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setRegisterName(rs.getString("register_name"));
				table.setSno(rs.getString("sno"));
			
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// ���� �׸� ��������
	//*******************************************************************/	
	public ArrayList getScoreItem(String as_field) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM as_scoreitem where as_field='"+as_field+"' and use_yn='1' ";
		query += "order by score_no asc";
//System.out.println("q : " + query);
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setScoreNo(rs.getString("score_no"));
				table.setScoreItem(rs.getString("score_item"));
			
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	/***************************************************************************
	 * ID�� ���ϴ� �޼ҵ�
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}
}



