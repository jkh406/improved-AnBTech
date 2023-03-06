package com.anbtech.gw.db;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.*;
import com.anbtech.gw.*;
import com.anbtech.date.*;
import com.anbtech.util.normalFormat;
import com.anbtech.file.*;
import com.anbtech.text.*;
import java.lang.SecurityException;
	
public class ModuleApprovalOffiDocDAO
{
	// Database Wrapper Class ����
	private Connection con;
	private ArrayList table_list = new ArrayList();					//��������

	private com.anbtech.date.anbDate anbdt = null;					//���� ó��
	private com.anbtech.util.normalFormat nmf = null;				//�������
	private com.anbtech.text.StringProcess str = null;				//���ڿ� ó��
	private com.anbtech.file.FileWriteString write;					//������ ���Ϸ� ���
	private com.anbtech.file.textFileReader read;					//������ ���Ϸ� ���

	private String ip_addr = "";									//���α��� ip address

	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public ModuleApprovalOffiDocDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//����ó��
		nmf = new com.anbtech.util.normalFormat("000");		//������ȣ �Ϸù�ȣ
		str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
		write = new com.anbtech.file.FileWriteString();		//������ ���Ϸ� ��� (���ο������� ���)
		read = new com.anbtech.file.textFileReader();		//������ ���Ϸ� �б�
	}
	
	/***************************************************************************
	 * �������� �б� (id:�����ڵ�,flag:������������,app_date:��������) / �ݿ��ϱ�
	 **************************************************************************/
	public void readODT(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//�Խ��ǿ� ������(��������,�ܺΰ���)
		appOfficialDocument();						//���缱���� �����ϱ� [OfficialDocument_app]
		branchOfficialDocument(flag);				//�Խ���/���ڿ���/e-mail�� �б��Ͽ� ������ �����ϱ�
	}

	/***************************************************************************
	 * �系���� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public void readIDS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//�Խ��ǿ� ������(��������,�ܺΰ���)
		appOfficialDocument();						//���缱���� �����ϱ� [OfficialDocument_app]
		branchOfficialDocument(flag);				//�Խ���/���ڿ���/e-mail�� �б��Ͽ� ������ �����ϱ�
	}

	/***************************************************************************
	 * ��ܰ��� �б� / �ݿ��ϱ�
	 **************************************************************************/
	public void readODS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//�Խ��ǿ� ������(��������,�ܺΰ���)
		appOfficialDocument();						//���缱���� �����ϱ� [OfficialDocument_app]
		branchOfficialDocument(flag);				//�Խ���/���ڿ���/e-mail�� �б��Ͽ� ������ �����ϱ�
	}

	/***************************************************************************
	 * ���缱 ���� �����ϱ�
	 **************************************************************************/
	public void appOfficialDocument() throws Exception  
	{	
		String app_id = "";							//���� ������ȣ
		//���ڰ����� ���������ȣ ã��
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();
			app_id = table.getAppId();		if(app_id == null) app_id = "";	//���� ������ȣ
		}
		//System.out.println("app_id : " + app_id );

		//���缱 ���� �б� (from app_save)
		ArrayList app_list = new ArrayList();									//��������
		com.anbtech.dms.db.OfficialDocumentAppDAO appDAO = new com.anbtech.dms.db.OfficialDocumentAppDAO(con);
		app_list = appDAO.getDoc_AppSave(app_id);

		//���缱 ���� �����ϱ� (to OfficialDocument_app)
		com.anbtech.dms.entity.OfficialDocumentAppTable Aptable;				//helper 
		Aptable = new com.anbtech.dms.entity.OfficialDocumentAppTable();	

		Statement stmt = null;
		stmt = con.createStatement();
		String input = "";

		Iterator app_iter = app_list.iterator();
		if(app_iter.hasNext()){
			Aptable = (com.anbtech.dms.entity.OfficialDocumentAppTable)app_iter.next();
			input = "insert into OfficialDocument_app ";
			input +="(id,gian_id,gian_name,gian_rank,gian_div,gian_date,gian_comment,";
			input +="review_id,review_name,review_rank,review_div,review_date,review_comment,";
			input +="agree_ids,agree_names,agree_ranks,agree_divs,agree_dates,agree_comments,";
			input +="decision_id,decision_name,decision_rank,decision_div,decision_date,decision_comment) ";
			input +="values('"+Aptable.getId()+"','"+Aptable.getGianId()+"','"+Aptable.getGianName()+"','";
			input +=Aptable.getGianRank()+"','"+Aptable.getGianDiv()+"','"+Aptable.getGianDate()+"','";
			input +=Aptable.getGianComment()+"','"+Aptable.getReviewId()+"','"+Aptable.getReviewName()+"','";
			input +=Aptable.getReviewRank()+"','"+Aptable.getReviewDiv()+"','"+Aptable.getReviewDate()+"','";
			input +=Aptable.getReviewComment()+"','"+Aptable.getAgreeIds()+"','"+Aptable.getAgreeNames()+"','";
			input +=Aptable.getAgreeRanks()+"','"+Aptable.getAgreeDivs()+"','"+Aptable.getAgreeDates()+"','";
			input +=Aptable.getAgreeComments()+"','"+Aptable.getDecisionId()+"','"+Aptable.getDecisionName()+"','";
			input +=Aptable.getDecisionRank()+"','"+Aptable.getDecisionDiv()+"','"+Aptable.getDecisionDate()+"','";	
			input +=Aptable.getDecisionComment()+"')";
		}
		stmt.executeUpdate(input);
		stmt.close();
		//System.out.println("���缱 input : " + input );

	}

	/***************************************************************************
	 * �Խ���/���ڿ���/�̸��� ���ۺб� [�������� ����]
	 **************************************************************************/
	private void branchOfficialDocument(String flag) throws Exception  
	{	
		String id = "";					//������ȣ
		String module = "";				//�Խ������� ����
		String mail = "";				//���ڿ������� ����
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();
			id = table.getId();
			module = table.getModuleName();		if(module == null) module = "";	//�Խ������ۿ��� �Ǵ�
			mail = table.getMail();				if(mail == null) mail = "";		//���ڿ������ۿ��� �Ǵ�
		}
		//System.out.println("module : " + module);
		//System.out.println("mail : " + mail);

		//------------------------------
		// ����̸����� �б� [3��]
		//------------------------------
		//�Խ������� ����
		if(module.equals("�Խ���"))			{									
			com.anbtech.dms.db.ModuleOffiDocToBoardDAO brd = new com.anbtech.dms.db.ModuleOffiDocToBoardDAO(con);
			if(flag.equals("ODT"))			//��������
				brd.readODT(id,"ODT",this.ip_addr);	
			else if(flag.equals("ODR"))		//��ܰ��� ����
				brd.readODR(id,"ODR",this.ip_addr);
		}
		//�̸��Ϸ� ����
		else if(module.equals("�̸���"))		{	
			//com.anbtech.gw.db.ModuleOffiDocToEmailDAO email = new com.anbtech.gw.db.ModuleOffiDocToEmailDAO(con);
			//email.SendEmailODS(id);			//��ܰ��� �ۼ��ø�
			//����� ������� �ӵ��� ���� ������ ����ڰ� ��ܰ����� ���Ϸ� �ٽ� ������.
		}	
		//�μ����������� ����
		else if(module.equals("�μ�����"))	{	
			sendDivision(flag);				//�系����,��ܰ�������
		}			

		//-------------------------------
		// ���ڿ������� �б� [1��]
		//-------------------------------
		if(mail.equals("���ڿ���")) sendMail();
	}

	/***************************************************************************
	 * ���� �μ����������� �����ϱ� [�������� ����]
	 **************************************************************************/
	private void sendDivision(String flag) throws Exception  
	{
		String insert = "";										//insert ����	 
		String id = getID();									//�����ڵ�
		String in_date = anbdt.getTime();						//��������
		String delete_date = anbdt.getAddDateNoformat(1);		//�������ڷ� ȭ����¿����� ���
		String module_add = "";									//�μ��ּ�(�����ڵ�/�μ���;...)
		String mail_add = "";									//�μ�����/�̸�;
		com.anbtech.dms.entity.OfficialDocumentTable table;		//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		//�������� �ۼ� (�系���� ������)
		String input = "";
		if(flag.equals("IDS")) {
			input = "insert into InDocument_receive(id,user_id,user_name,user_rank,ac_id,ac_code,";
			input +="code,ac_name,serial_no,doc_id,in_date,receive,sending,subject,bon_path,bon_file,delete_date,";
			input +="fname,sname,ftype,fsize,flag,app_id,module_add,mail_add) values('";
		}

		//�系���� ���źμ� ���ϱ� ������ ����
		Iterator div_list = table_list.iterator();
		if(div_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)div_list.next();
			module_add = table.getModuleAdd();	if(module_add == null) module_add = "";
			mail_add = table.getMailAdd();	if(mail_add == null) mail_add = "";
		}
		if(module_add.length() == 0) return;
		//System.out.println("module_add : " + module_add);

		//���źμ� �������ϱ�
		int cnt = 0;
		for(int i=0; i<module_add.length(); i++) if(module_add.charAt(i) == ';') cnt++;
		//System.out.println("���źμ� �� cnt : " + cnt);
		String[][] address = new String[cnt][4];	//�μ������ڵ�,�μ��ڵ�,Tree�����ڵ�,�μ���
		address = searchDivInfo(cnt,module_add);	//�μ����� ���ϱ�

		//���źμ� ����� �Ϸù�ȣ ���ϱ�
		String[] serial_no = new String[cnt];
		serial_no = searchSerialNo("InDocument_receive",address);

		//���źμ� �μ��� �迭�� ���
		String[] chief = new String[cnt];			//���/�̸�
		java.util.StringTokenizer ndata = new StringTokenizer(mail_add,";");
		int ai = 0;
		while(ndata.hasMoreTokens()) {
			String rd = ndata.nextToken();
			chief[ai] = rd.trim()+";";
			//System.out.println("�μ��� : " + chief[ai]);
			ai++;
			if(ai == cnt) break;
		}
		
		
		//�߰� insert���常���
		Iterator table_iter = table_list.iterator();
		String input_1="",input_2="";
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();
			input_1 = table.getUserId()+"','"+table.getUserName()+"','"+table.getUserRank();

			input_2 = table.getDocId()+"','"+in_date+"','"+table.getReceive()+"','";
			input_2 += table.getSending()+"','"+table.getSubject()+"','"+table.getBonPath()+"','";
			input_2 += table.getBonFile()+"','"+delete_date+"','"+table.getFname()+"','";
			input_2 += table.getSname()+"','"+table.getFtype()+"','"+table.getFsize()+"','EF','";
			input_2 += table.getAppId()+"','";
		}

		//�Է��ϱ�
		Statement stmt = null;
		stmt = con.createStatement();
		for(int i=0; i<cnt; i++) {
			insert = "";
			String pid = id+nmf.toDigits(i);
			insert = input + pid+"','"+input_1+"','"+address[i][0]+"','"+address[i][1]+"','"; 
			insert += address[i][2]+"','"+address[i][3]+"','"+serial_no[i]+"','"+input_2+chief[i]+"','')";
			stmt.executeUpdate(insert);
			//System.out.println("�μ� ���� insert : " + insert + "\n\n");	
		}
		stmt.close();

	}
	/***************************************************************************
	 * ���� �μ����������� �����ϱ� [�μ����� ���ϱ�]
	 **************************************************************************/
	private String[][] searchDivInfo(int cnt,String module_add) throws Exception  
	{
		StringTokenizer add = new StringTokenizer(module_add,";");
		String[][] address = new String[cnt][4];
		int n = 0;
		while(add.hasMoreTokens()) {
			String data = add.nextToken();	data = data.trim();
			address[n][0] = data.substring(0,data.indexOf("/"));
			//System.out.println("ac_code : " + address[n][0]);
			n++;
			if(n == cnt) break;
		} 

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select distinct ac_code,code,ac_name from class_table ";
		for(int i=0; i<cnt; i++) {
			String q = query + "where ac_id ='"+address[i][0]+"'";
			//System.out.println("q : " + q);
			rs = stmt.executeQuery(q);
			if(rs.next()) {
				address[i][1] = rs.getString("ac_code");
				address[i][2] = rs.getString("code");
				address[i][3] = rs.getString("ac_name");
				//System.out.println("address : "+address[i][1]+","+address[i][2]+","+address[i][3]);
			}
		}
		stmt.close();
		rs.close();

		return address;
	}

	/***************************************************************************
	 * ���� �μ����������� �����ϱ� [�μ� ������ȣ ���ϱ� : �Ϸù�ȣ 03-001 ...]
	 **************************************************************************/
	private String[] searchSerialNo(String tablename,String[][] address) throws Exception  
	{
		int add_len = address.length;				//address �迭����
		String[] serial_no = new String[add_len];

		String serial_year = anbdt.getYear();
		String s_year = serial_year.substring(2,4);	//�⵵ ���ڸ� ���ϱ�

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
	
		String query = "select distinct serial_no from "+tablename;
		for(int i=0; i<add_len; i++) {
			int no = 1;
			String q = query + " where ac_id ='"+address[i][0]+"' order by serial_no desc";
			//System.out.println("�Ϸù�ȣ q : " + q);
			rs = stmt.executeQuery(q);
			String read_no = "";
			if(rs.next()) {
				read_no = rs.getString("serial_no");
				//System.out.println("read_no : " + read_no);
				if(read_no == null) no = 1;
				else {
					String last_no = read_no.substring(3,6);
					no = Integer.parseInt(last_no);
					no++;
				}
			}
			serial_no[i] = s_year+"-"+nmf.toDigits(no);
			//System.out.println("serial no : " + address[i][0] + " : " + serial_no[i]);
		}
		stmt.close();
		rs.close();

		return serial_no;
	}

	/***************************************************************************
	 * ���ڿ������� �����ϱ� [�������� ����]
	 **************************************************************************/
	private void sendMail() throws Exception  
	{
		String sid = "",app_id="",subject="";		//���������� ��ũ�� �����ϱ�(html����)
		String module_add = "";						//�����μ� �ּҷ� �系�߼۹����� ����.
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
		
		Statement stmt = null;
		stmt = con.createStatement();

		//-------------------------------------------------------
		//���ó��� �о� �ش繮�� �����
		//-------------------------------------------------------
		String post_bon_path = "";												//post�� ������ ����path
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();

			//������ �������뿡�� ��ũ ó��
			sid = table.getId();												//������ȣ
			app_id = table.getAppId();											//������� ������ȣ
			subject = table.getSubject();										//����
			module_add = table.getModuleAdd();									//�系 ���źμ���
			if(module_add == null) module_add = "";

			post_bon_path = "/post/"+table.getUserId()+"/text_upload";			//post�� ������ ����path
			//---------------
			//post_master
			//---------------
			mquery += pid+"','"+table.getSubject()+"','"+table.getUserId()+"','"+table.getUserName()+"','"+w_date+"','"+table.getMailAdd()+"','";
			mquery += "0"+"','"+"email"+"','"+"CFM"+"','"+post_bon_path+"','"+filename+"','"+delete_date+"')";
			stmt.executeUpdate(mquery);
			//System.out.println("email master : " + mquery + "\n");
			
			//---------------
			//post_letter
			//---------------
			String receivers = table.getMailAdd();		//����������� ã�� �Է��ϱ�
			StringTokenizer dd = new StringTokenizer(receivers,";");
			while(dd.hasMoreTokens()) {
				String rd = dd.nextToken();		rd=rd.trim();		//���/�̸�
				if(rd.length() > 5) {
					String sabun = rd.substring(0,rd.indexOf("/"));
					String lq = lquery + pid+"','"+table.getSubject()+"','"+table.getUserId()+"','"+table.getUserName()+"','"+w_date+"','"+sabun+"','";
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
		if(module_add.length() > 0) {		//�系�߼� ����
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
		} else {						//���� ���� ����
			  content += " <script> function contentAppview(id,app_id){";
			  content += " sParam=\"strSrc=OfficialDocumentServlet&mode=OFD_A&id=\"+id+\"&doc_id=\"+app_id"+"\n";
			  content += " var rval = showModalDialog(\""+servlet+"/ods/DocModalFrm.jsp?\"+sParam,\"\",\"dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0\" )";
			  content += "}";
			  content += "</script>";
			  content += "<body>";
			  content += "���� �����Դϴ�.<br>";
			  content += "�󼼳����� �Ʒ������� Ŭ���ϼ���.<br>";
			  content += "<br><br>";
			  content += "<a href=\"javascript:contentAppview('"+sid+"','"+app_id+"');\">"+subject+"</a>";
		}
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
		���α��� ip address 
	******************************************************************************/
	public void setIPADDR(String ip_addr)
	{
		this.ip_addr = ip_addr;
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

}	
