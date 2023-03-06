package com.anbtech.dms.db;
import com.anbtech.dms.entity.*;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.date.*;
import com.anbtech.util.normalFormat;
import com.anbtech.file.*;
import com.anbtech.text.*;

public class InDocumentRecDAO
{
	private Connection con;
	private ArrayList table_list = new ArrayList();					//��������
	private FileWriteString text;

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	private String id;					//����:������ȣ
	private String bon_path;			//Ȯ��path
	private String bon_file;			//�������� ���ϸ�
	private String sname;				//����:���������	(Ȯ���� .bin�̾���)	

	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();				//����ó��
	private com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");		//������ȣ �Ϸù�ȣ
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();		//������ ���Ϸ� ��� (���ο������� ���)
	private com.anbtech.file.textFileReader read = new com.anbtech.file.textFileReader();		//������ ���Ϸ� �б�

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public InDocumentRecDAO(Connection con) 
	{
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ� (�μ�Tree�� ã��)
	//*******************************************************************/
	private int getTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		//�μ����̸� �ش� �μ��ڵ�(ac_code)�� �����Ѵ�.(class_table�� chief_id�� �ִ���)
		String ac_code = checkChief(login_id);		//�μ��� ���
		if(ac_code.length() > 0) {					//�μ����ΰ��(�μ��ڵ�� ����)
			query = "SELECT COUNT(*) FROM InDocument_receive where (ac_code = '"+ac_code+"' or ";	
			query += " mail_add like '%"+login_id+"%') and ("+sItem+" like '%"+sWord+"%')"; 
		} else {									//������ ����
			query = "SELECT COUNT(*) FROM InDocument_receive where mail_add  like '%"+login_id+"%'";	
			query += " and ("+sItem+" like '%"+sWord+"%')"; 
		}
//System.out.println("���� q : " + query);
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
	// DB InDocument_send���� �ش�LIST QUERY�ϱ� (��ü LIST�б�)
	//*******************************************************************/	
	public ArrayList getDoc_List (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//�Ѱ��� ���ϱ�
		total_cnt = getTotalCount(login_id,sItem,sWord);
			
		//�μ����̸� �ش� �μ��ڵ�(ac_code)�� �����Ѵ�.(class_table�� chief_id�� �ִ���)
		String ac_code = checkChief(login_id);		//�μ��� ���
		if(ac_code.length() > 0) {					//�μ����ΰ��
			query = "SELECT * FROM InDocument_receive where (ac_code = '"+ac_code+"' or ";	
			query += " mail_add like '%"+login_id+"%') and ("+sItem+" like '%"+sWord+"%') order by serial_no desc"; 
		} else {									//�������� ���
			query = "SELECT * FROM InDocument_receive where (";	
			query += " mail_add like '%"+login_id+"%') and ("+sItem+" like '%"+sWord+"%') order by serial_no desc"; 
		}
//System.out.println("List q : " + query);
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
				table = new OfficialDocumentTable();
				
				String pid = rs.getString("id");
				table.setId(pid);													//������ȣ	
				String user_id = rs.getString("user_id");							//����� or �߼���
				table.setUserId(user_id);							
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));	

				String rac_code = rs.getString("ac_code");
				table.setAcCode(rac_code);

				table.setCode(rs.getString("code"));
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setInDate(rs.getString("in_date"));	
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));
				table.setSending(rs.getString("sending"));
				
				String subj = rs.getString("subject");  if(subj == null) subj = "";
											
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				
				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				String flg = rs.getString("flag");	if(flg == null) flg = "EN";
				table.setFlag(flg);									//�����������
				String app_id = rs.getString("app_id"); if(app_id == null) app_id = "";
				table.setAppId(app_id);								//������ι�ȣ

				// ���� href �б�(�ϵ�ī�ǵ�� �� ���ڰ�������� ���缱������ �����ֱ�����)
				String subLink = "<a href=\"javascript:contentReview('"+pid+"');\">"+subj+"</a>";//������
				if(flg.equals("EF")) {
					//���ڰ��� ������ ��ϵ� ���
					subLink = "<a href=\"javascript:contentAppview('"+pid+"','"+app_id+"');\">"+subj+"</a>";
				}
				table.setSubject(subLink);							//���� 

				//����/���� or �μ����� ������ �����ϱ� ǥ��
				String subMod="",subDel="";
				//�ۼ����� ���
				if((flg.equals("EN") || flg.equals("EC")) && login_id.equals(user_id)){
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\"><img src='../ods/images/lt_modify.gif' border='0' align='absmiddle'></a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\"><img src='../ods/images/lt_del.gif' border='0' align='absmiddle'></a>";
				}
				//�μ����� ���
				else if(flg.equals("EF") && rac_code.equals(ac_code)) {
					subMod = "<a href=\"javascript:contentShare('"+pid+"','"+subj+"');\"><img src='../ods/images/lt_doc_p.gif' border='0' align='absmiddle'></a>";
				}
				//�������� ���
				else {
					subMod = "���Ű���";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table_list.add(table);
				show_cnt++;
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// DB InDocument_receive���� �ش�ID QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getDoc_Read (String id) throws Exception
	{
		ArrayList table_list = new ArrayList();

		String flag = searchDocRecInfo (id,"flag");			//�����Է¹������� ���ڰ��繮������ ã��
		String doc_id = searchDocRecInfo (id,"doc_id");		//������ȣ ã�� (������ȣ�� �ƴ�)
//System.out.println("flag : doc_id = " + flag + " : " + doc_id );
		if(flag.equals("EF")) table_list = getDocAppSend(doc_id);	//���ڰ��� ����
		else table_list = getDocHandSend(doc_id);					//���� �Է¹���
		return table_list;
	}

	//*******************************************************************
	// ���ڰ��� ���ε� �系������ DB InDocument_send���� ����������ȣ QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	private ArrayList getDocAppSend (String doc_id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM InDocument_send where doc_id='"+doc_id+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new OfficialDocumentTable();
				
				table.setId(rs.getString("id"));							
				table.setUserId(rs.getString("user_id"));							
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));							
				table.setAcCode(rs.getString("ac_code"));
				table.setCode(rs.getString("code"));
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setSlogan(rs.getString("slogan"));	
				table.setTitleName(rs.getString("title_name"));	
				table.setInDate(rs.getString("in_date"));	
				table.setSendDate(rs.getString("send_date"));	
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));	
				table.setReference(rs.getString("reference"));
				table.setSending(rs.getString("sending"));	
				table.setSubject(rs.getString("subject"));	
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				table.setFirmName(rs.getString("firm_name"));	
				table.setRepresentative(rs.getString("representative"));

				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				table.setFlag(rs.getString("flag"));
				table.setAppId(rs.getString("app_id"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setModuleName(rs.getString("module_name"));
				table.setModuleAdd(rs.getString("module_add"));
				table.setMail(rs.getString("mail"));	
				table.setMailAdd(rs.getString("mail_add"));	

				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// �����Էµ� �系������ DB InDocument_receive���� ����������ȣ QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	private ArrayList getDocHandSend (String doc_id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM InDocument_receive where doc_id='"+doc_id+"'";	
		rs = stmt.executeQuery(query);

		//������ ���
		while(rs.next()) { 
				table = new OfficialDocumentTable();
				
				table.setId(rs.getString("id"));							
				table.setUserId(rs.getString("user_id"));							
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));							
				table.setAcCode(rs.getString("ac_code"));
				table.setCode(rs.getString("code"));
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setInDate(rs.getString("in_date"));		
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));	
				table.setSending(rs.getString("sending"));	
				table.setSubject(rs.getString("subject"));	
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	

				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				table.setFlag(rs.getString("flag"));
					
				table_list.add(table);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	
	/*******************************************************************
	* �系���Ű��� ������ �����ϱ� 
	*******************************************************************/
	public void shareReceiver(String tablename,String id,String share_id) throws Exception
	{
		//����
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//������ �����ϱ�
		String update = "update "+tablename+" set mail_add='"+share_id+"' where id='"+id+"'";
		stmt.executeUpdate(update);
		
		//�ش� ������ �系�����߼ۿ��� ã��
		String doc_id = "";
		String query = "SELECT doc_id FROM "+tablename+" where id='"+id+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) doc_id = rs.getString("doc_id");			//���� ������ȣ


		String send_id = "";
		String send_table = "";
		if(tablename.equals("InDocument_receive")) send_table = "InDocument_send";
		else if(tablename.equals("OutDocument_receive")) send_table = "OutDocument_receive";
		query = "SELECT id FROM "+send_table+" where doc_id='"+doc_id+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) send_id = rs.getString("id");				//�ش繮����ȣ�� ������ȣ


		//�ش系�� �����ڿ��� ���ڸ��Ϸ� �߼��ϱ�
		//�系 ����
		if(tablename.equals("InDocument_receive")) {
			com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
			this.table_list = docDAO.getDoc_Read(send_id);
		} 
		//��� ����
		else if(tablename.equals("OutDocument_receive")) {
//			com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
//			this.table_list = docDAO.getDoc_Read(send_id);
		}

		//���� �����ϱ�
		if(share_id.length() > 5) sendMail(tablename,id,share_id);		//�߼��ϱ�
		
		stmt.close();
	}

	/***************************************************************************
	 * �����ڿ��� ���ڿ������� �����ϱ�  (id : �������̺��� ������ȣ
	 **************************************************************************/
	private void sendMail(String tablename,String id,String share_id) throws Exception  
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String sid = "",app_id="",subject="";		//���������� ��ũ�� �����ϱ�(html����)
		String mgr_id = "", mgr_name = "";			//�μ��� ���, �̸�
		//-------------------------------------------------------
		//���ó��� post tabel(post_master,post_letter)�� ��� ����
		//-------------------------------------------------------
		String query = "select module_add from "+tablename+" where id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			String module_add = rs.getString("module_add");			//�μ��� ���/�̸�;
			int sn = module_add.indexOf("/");
			mgr_id = module_add.substring(0,sn);					//�μ��� ��� (���ڿ��� ������ ���)
			mgr_name = module_add.substring(sn+1,module_add.length()-1); //�̸�
		}

		//-------------------------------------------------------
		//���ó��� post tabel(post_master,post_letter)�� ��� ����
		//-------------------------------------------------------
		String pid = getID();													//������ȣ
		String w_date = anbdt.getTime();										//�ۼ���
		String delete_date = anbdt.getAddMonthNoformat(1);						//����������
		String filename = pid;													//�����������ϸ�
		String mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
			  mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		String lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
			  lquery += "post_receiver,isopen,post_select,delete_date) values('";
		
		//-------------------------------------------------------
		//���ó��� �о� �ش繮�� �����
		//-------------------------------------------------------
		String post_bon_path = "";												//post�� ������ ����path
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_list.size() == 0) return;

		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();

			//������ �������뿡�� ��ũ ó��
			sid = table.getId();												//������ȣ
			app_id = table.getAppId();											//������� ������ȣ
			subject = table.getSubject();										//����
		
			post_bon_path = "/post/"+table.getUserId()+"/text_upload";			//post�� ������ ����path
			//---------------
			//post_master
			//---------------
			mquery += pid+"','"+table.getSubject()+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+share_id+"','";
			mquery += "0"+"','"+"email"+"','"+"CFM"+"','"+post_bon_path+"','"+filename+"','"+delete_date+"')";
			stmt.executeUpdate(mquery);
			System.out.println("email master : " + mquery + "\n");
			
			//---------------
			//post_letter
			//---------------
			String receivers = share_id;		//����������� ã�� �Է��ϱ�
			StringTokenizer dd = new StringTokenizer(receivers,";");
			while(dd.hasMoreTokens()) {
				String rd = dd.nextToken();		rd=rd.trim();		//���/�̸�
				if(rd.length() > 5) {
					String sabun = rd.substring(0,rd.indexOf("/"));
					String lq = lquery + pid+"','"+table.getSubject()+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+sabun+"','";
						  lq += "0"+"','"+"CFM"+"','"+delete_date+"')";
					stmt.executeUpdate(lq);
					//System.out.println("email letter : " + lq + "\n");
				}
			}
		}

		//-------------------------------------------------------
		//�������� �����
		//-------------------------------------------------------
		String upload_path = "";
		upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");	//servlet path
		// ���� �������� �����
		String content = "<html><head><title>����</title></head>";
			  content += " <script> function contentAppview(id,app_id){";
			  content += " sParam=\"strSrc=InDocumentServlet&mode=IND_A&id=\"+id+\"&doc_id=\"+app_id"+"\n";
			  content += " var rval = showModalDialog(\""+servlet+"/ods/DocModalFrm.jsp?\"+sParam,\"\",\"dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0\" )";
			  content += "}";
			  content += "</script>";
			  content += "<body>";
			  content += "���� �����Դϴ�.<br>";
			  content += "�󼼳����� �Ʒ������� Ŭ���ϼ���.<br>";
			  content += "<br><br>";
			  content += "<a href=\"javascript:contentAppview('"+sid+"','"+app_id+"');\">"+subject+"</a>";
			  content += "</body></html>";
		//System.out.println("�������� : " + content);

		// ���ڿ���� �������� ���ϸ����
		String path = upload_path + "/gw/mail" + post_bon_path;					//����� path
		write.setFilepath(path);												//directory�����ϱ�
		write.WriteHanguel(path,filename,content);								//���� ���Ϸ� �����ϱ�

		//-------------------------------------------------------
		//�ݱ�
		//-------------------------------------------------------
		stmt.close();
	}

	/******************************************************************************
	// ID�� ���ϴ� �޼ҵ�
	******************************************************************************/
	private String getID()
	{
		String ID;
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);
		nmf.setFormat("000");		//�Ϸù�ȣ ��� ����(6�ڸ�)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}

	/*******************************************************************
	* �系���� ���� ���� ���� �����ϱ� (Hard copy ���ɺ�) 
	*******************************************************************/
	public void inputIDRTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,
		String in_date,String enforce_date,String receive,String sending,String subject,
		String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		subject = str.repWord(subject,"'","`");				//����		
		content = str.repWord(content,"'","`");				//����

		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO InDocument_receive(id,user_id,user_name,user_rank,";
			incommon += "ac_id,ac_code,code,ac_name,class_no,in_date,enforce_date,receive,sending,";
			incommon += "subject,bon_path,bon_file,flag) values('";
		
		String input = incommon+id+"','"+user_id+"','"+user_name+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+code+"','"+ac_name+"','"+class_no+"','";
			input += in_date+"','"+enforce_date+"','"+receive+"','"+sending+"','"+subject+"','";
			input += doc_pat+"','"+id+"','EN')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,id,content);
		}
	}

	/*******************************************************************
	* �系���� ���� ���� �����ϱ� 
	*******************************************************************/
	public void updateIDRTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,
		String in_date,String enforce_date,String receive,String sending,String subject,
		String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		subject = str.repWord(subject,"'","`");				//����		
		content = str.repWord(content,"'","`");				//����

		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE InDocument_receive set user_id='"+user_id+"',user_name='"+user_name;
			update += "',user_rank='"+user_rank+"',"+"ac_id='"+ac_id+"',ac_code='"+ac_code+"',code='"+code;
			update += "',ac_name='"+ac_name+"',class_no='"+class_no;
			update += "',in_date='"+in_date+"',enforce_date='"+enforce_date;
			update += "',receive='"+receive+"',sending='"+sending;
			update += "',subject='"+subject+"',bon_path='"+doc_pat+"',bon_file='"+id;
			update += "' where id='"+id+"'";
		//System.out.println("update : " + update );
		int er = stmt.executeUpdate(update);
		
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,id,content);
		}
	}

	/*******************************************************************
	* ������ ���Ϸ� �����ϱ�
	* root_path : root Path, doc_pat : Ȯ�� path, content : ��������
	 *******************************************************************/
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}

	/*******************************************************************
	* ÷������ �����ϱ� (�űԷ� ó�� ÷���Ҷ�)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String id,String filepath) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����

		int i = 1;					//÷������ Ȯ����
		int atcnt = 0;				//÷������ ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "attachfile"+i;		//upload�� input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload�� ���ϸ�
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				filename += fname + " |";
				savename += id + "_" + i + " |";
				filetype += ftype + " |";
				filesize += fsize + " |";
				atcnt++;
			}
			i++;
		}//while

		//Table�� �����ϱ�
		if(i > 1) {
			setAddFileUpdate(id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* ÷������ �����ϱ� (�����Ͽ� ÷���Ҷ�)
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String id,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt) throws Exception
	{
		String filename = "";		//�����̸� ���ϸ�
		String savename = "";		//���� ���ϸ�
		String filetype = "";		//�����̸� ���� Ȯ���ڸ�
		String filesize = "";		//�����̸� ���ϻ�����
		int att_cnt = Integer.parseInt(attache_cnt);	//÷������ �ִ���� �̸�
		String newdata = "";

		//�űԷ� ÷���� ����
		int i = 1;		//÷������Ȯ����
		int n = 0;		//����迭�� ����
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//�ش����� �б�
			String name = "";
			String uname = "";
			for(int a=0; a<att_cnt; a++) {			//÷������ att_cnt ��°���� �а� ��������
				name = "attachfile"+i;
				uname = multi.getFilesystemName(name);
				if(uname != null) break; else { i++; n++; }
			}
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload�� ����type
				//file size���ϱ�
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,id+"_"+i+".bin");
				upFile.renameTo(myFile);					//�����̸� �ٲٱ�

				newdata += uname + "|";						//÷�����ϸ�
				newdata += id + "_" + i + "|";				//÷������ �����
				newdata += utype + "|";						//÷������ type
				newdata += usize + ";";						//÷������ ũ��
			}
			i++;
			n++;
		}//while

		//�迭�����
		java.util.StringTokenizer fna = new StringTokenizer(fname,"|");
		int fn = fna.countTokens();

		int an = fn + n;
		String[][] nfile = new String[an][4];
		for(int j=0; j<an; j++) for(int k=0; k<4; k++) nfile[j][k] = "";

		//÷������ �迭�� ���
		int ai = 0;		//�迭��ȣ
		java.util.StringTokenizer ndata = new StringTokenizer(newdata,";");
		while(ndata.hasMoreTokens()) {
			String nnd = ndata.nextToken();		//1���� �б�
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				nfile[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		//������ �ִ� ÷������ �迭�� ���̱�
		java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			//÷�����ϸ�
		int hi = ai;
		while(o_fname.hasMoreTokens()) {
			String read = o_fname.nextToken();
			if(read.length() != 1) nfile[hi][0] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			//÷������ �����
		hi = ai;
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			if(read.length() != 1) nfile[hi][1] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			//÷������ Ÿ��
		hi = ai;
		while(o_ftype.hasMoreTokens()) {
			String read = o_ftype.nextToken();
			if(read.length() != 1) nfile[hi][2] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			//÷������ ũ��
		hi = ai;
		while(o_fsize.hasMoreTokens()) {
			String read = o_fsize.nextToken();
			if(read.length() != 1) nfile[hi][3] = read.substring(0,read.length()-1);
			hi++;
		}

		//������ ������ ������
		int atcnt = 0;				//÷������ ����
		for(int p=0; p<an; p++) {
			if(nfile[p][0].length() != 0) {
				filename += nfile[p][0] + " |";
				savename += nfile[p][1] + " |";
				filetype += nfile[p][2] + " |";
				filesize += nfile[p][3] + " |";
				atcnt++;
			}
		}

		//Table�� �����ϱ�
		if(an > 0) {
			setAddFileUpdate(id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* ÷������ �������� Table�� update�ϱ�
	 *******************************************************************/
	 private void setAddFileUpdate(String id, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update InDocument_receive set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where id='"+id+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
	/*******************************************************************
	* ÷������ �����ϰ� �ش系�� update�ϱ� (����÷������ ������)
	 *******************************************************************/
	 public void deleteAttachFile(String id,String fname,String ftype,String fsize,String sname,String deleteAttachNo,
						String upload_path,String bon_path) throws Exception
	{
		//----------------------------------
		// ���ó��� �迭�� ���
		//----------------------------------
		int cnt = 0;
		for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

		String[][] addFile = new String[cnt][5];
		for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

		if(fname.length() != 0) {
			StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
			int m = 0;
			while(f.hasMoreTokens()) {
				addFile[m][0] = f.nextToken();
				addFile[m][0] = addFile[m][0].trim(); 
				if(addFile[m][0] == null) addFile[m][0] = "";
				m++;
			}
			StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
			m = 0;
			while(t.hasMoreTokens()) {
				addFile[m][1] = t.nextToken();
				addFile[m][1] = addFile[m][1].trim();
				if(addFile[m][1] == null) addFile[m][1] = "";
				m++;
			}
			StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
			m = 0;
			while(s.hasMoreTokens()) {
				addFile[m][2] = s.nextToken();
				addFile[m][2] = addFile[m][2].trim();
				if(addFile[m][2] == null) addFile[m][2] = "";
				m++;
			}
			StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
			m = 0;
			while(o.hasMoreTokens()) {
				addFile[m][3] = o.nextToken();	
				addFile[m][3] = addFile[m][3].trim();				//�������ϸ�(.bin����)
				if(addFile[m][3] == null) addFile[m][3] = "";
				//÷�����Ͽ��� Ȯ����(_1_2_3..)��ȣ ã��
				if(addFile[m][3].length() > 3) {
					int en = addFile[m][3].indexOf("_");
					addFile[m][4] = addFile[m][3].substring(en+1,en+2);
				} else addFile[m][4] = "0";
				m++;
			}
		}

		//----------------------------------
		//�ش� ÷�������� ã�� ó���ϱ�
		//----------------------------------
		String nfname="", nftype="", nfsize="", nsname = "";
		int del_no = 0;			//������ �迭��ȣ
		int del_ext = Integer.parseInt(deleteAttachNo);
//System.out.println("cnt : del_ext == " + cnt + ":" + del_ext);
		for(int i=0; i<cnt; i++) {
			int ext_no = Integer.parseInt(addFile[i][4]);
			if(ext_no == del_ext) {	//������ ������ 
				del_no = i;			//������ �迭��ȣ
				nfname += " |";
				nftype += " |";
				nfsize += " |";
				nsname += " |";
			}
			else {
				nfname += addFile[i][0] + " |";
				nftype += addFile[i][1] + " |";
				nfsize += addFile[i][2] + " |";
				nsname += addFile[i][3] + " |";
			}
		}

		//Tabel update �ϱ�
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update InDocument_receive set ";
		update += "fname='"+nfname+"',sname='"+nsname+"',ftype='"+nftype+"',fsize='"+nfsize+"' where id="+id;
		int er = stmt.executeUpdate(update);
		stmt.close();
//System.out.println("upload_path " + upload_path );
//System.out.println("bon_path " + bon_path );
//System.out.println("addFile[del_ext][3] " + addFile[del_ext][3] );
		//÷������ �����ϱ�
		deleteAddFile(upload_path,bon_path,addFile[del_no][3]);		//÷������ �����ϱ�

	}

	//*******************************************************************
	//	�־��� ������ȣ�� �ʿ䳻�� ã�� ���ó��� �����ϱ�
	//*******************************************************************/	
	public void deletePid (String id,String upload_path) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		String query  = "SELECT * FROM InDocument_receive where id='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = "";	//Ȯ��path
			bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = "";	//�������� ���ϸ�
			sname = rs.getString("sname");	if(sname == null) sname = "";				//����:���������	(Ȯ���� .bin�̾���)
		}

		boolean dbt = deleteBonText(upload_path,bon_path,bon_file);		//�������� �����ϱ�
		boolean daf = deleteAddFile(upload_path,bon_path,sname);		//÷������ �����ϱ�
		if(dbt || daf) {
			deleteTableLine("InDocument_receive",id);		//�ش�line �����ϱ�
		}
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	�������� �����ϱ�
	//*******************************************************************/	
	public boolean deleteBonText (String upload_path,String bon_path,String bon_file)
	{
		boolean rtn = false;

		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = upload_path + bon_path + "/bonmun/" + bon_file;
		System.out.println("bonmun file : " + filename);
		rtn = text.delFilename(filename);	//�ش� ���ϻ��� �ϱ�
		return rtn;
	}

	//*******************************************************************
	//	÷������ �����ϱ�
	//*******************************************************************/	
	public boolean deleteAddFile (String upload_path,String bon_path,String sname)
	{
		boolean rtn = false;

		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filepath = upload_path + bon_path + "/addfile"; 
		String filename = "";

		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");		
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			read = read.trim();
			if(read.length() != 1) {
				filename = filepath+"/"+read+".bin";			
				System.out.println("delete attach file : " + filename);
				rtn = text.delFilename(filename);	//�ش� ���ϻ��� �ϱ�
			}
		}
		return rtn;
	}
	//*******************************************************************
	//	Table OfficialDocument�� Line�����ϱ� 
	//*******************************************************************/	
	public void deleteTableLine (String tablename,String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where id='"+id+"'";
		System.out.println("delete : " + query);
		stmt.execute(query);
		stmt.close();
	}

	//*******************************************************************
	//	�ش��� �μ��� �μ����̸� �μ� �ڵ� �����ϱ�
	//*******************************************************************/	
	private String checkChief (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		String chief_id = "";	//�μ�����
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ������� �˾ƺ���
		query  = "SELECT b.chief_id FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next()) chief_id = rs.getString("chief_id");	
		

		//�μ����̸� �μ��ڵ� ã��(ac_code)
		if(chief_id.equals(login_id)) {
			query  = "SELECT b.ac_code FROM user_table a,class_table b ";
			query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
			rs = stmt.executeQuery(query);
			if(rs.next())	rtn = rs.getString("ac_code");
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}
	
	
	//*******************************************************************
	//	�־��� �μ����� (�ڵ� ��) ã�� 
	//*******************************************************************/	
	private String searchDivTreeCode (String login_id,String col_name) throws Exception
	{
		String rtn = "";		//return data
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		String query  = "SELECT b."+col_name+" FROM user_table a,class_table b ";
			query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			rtn = rs.getString(col_name);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}

	//*******************************************************************
	//	�־��� �系���� �������� ���� ã�� 
	//*******************************************************************/	
	private String searchDocRecInfo (String id,String col_name) throws Exception
	{
		String rtn = "";		//return data
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query���� �����
		String query  = "SELECT "+col_name+" FROM InDocument_receive where id ='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			rtn = rs.getString(col_name);
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}
}

