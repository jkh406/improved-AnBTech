package com.anbtech.dms.db;
import com.anbtech.dms.entity.*;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;

public class OfficialDocumentDAO
{
	private Connection con;
	private FileWriteString text;
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//���ڿ�ó���ϱ�

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	private String id;					//����:������ȣ
	private String bon_path;			//Ȯ��path
	private String bon_file;			//�������� ���ϸ�
	private String sname;				//����:���������	(Ȯ���� .bin�̾���)	

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public OfficialDocumentDAO(Connection con) 
	{
		this.con = con;
	}

	//*******************************************************************
	//	�� ���� �ľ��ϱ�
	//*******************************************************************/
	public int getTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		if(login_id == null) login_id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		//�μ������ڵ� ã��
		String ac_id = searchAcId(login_id);

		query = "SELECT COUNT(*) FROM OfficialDocument where ac_id='"+ac_id+"'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
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
	// DB OfficialDocument���� �ش�LIST QUERY�ϱ� (��ü LIST�б�)
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

		//�μ������ڵ� ã��
		String ac_id = searchAcId(login_id);

		//�Ѱ��� ���ϱ�
		total_cnt = getTotalCount(login_id,sItem,sWord);
			
		//query���� �����
		query = "SELECT * FROM OfficialDocument where ac_id='"+ac_id+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by doc_id asc"; 
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
				table.setId(pid);									//������ȣ	
				String user_id = rs.getString("user_id");
				table.setUserId(user_id);							//�ۼ���					
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));							
				table.setAcCode(rs.getString("ac_code"));							
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setSlogan(rs.getString("slogan"));	
				table.setTitleName(rs.getString("title_name"));	
				table.setInDate(rs.getString("in_date"));	
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));
				table.setReference(rs.getString("reference"));
				table.setSending(rs.getString("sending"));
				
				String subj = rs.getString("subject");  if(subj == null) subj = "";
				
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				table.setFirmName(rs.getString("firm_name"));	
				table.setRepresentative(rs.getString("representative"));	
				table.setEtc(rs.getString("etc"));	

				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				String flg = rs.getString("flag");	//if(flg == null) flg = "EN";
				table.setFlag(flg);									//�����������
				String app_id = rs.getString("app_id"); if(app_id == null) app_id = "";
				table.setAppId(app_id);								//���ڰ��� ���� �����ڵ�
				table.setAppDate(rs.getString("app_date"));	

				// ���� href �б�(������ ���缱������ �����ֱ�����) 
				String subLink = "";
				//������ [�����̸� ������ ���������� �ƴϸ� ����]
				if(login_id.equals(user_id)) subLink = "<a href=\"javascript:contentReview('"+pid+"');\">"+subj+"</a>";
				else subLink = subj;
				//������ [���� �μ����̸� ���� �ִ�]
				if(app_id.length() != 0) {
					subLink = "<a href=\"javascript:contentAppview('"+pid+"','"+app_id+"');\">"+subj+"</a>";	 
				}
				table.setSubject(subLink);							//���� 

				//���� or �������� ǥ�� [login_id�� �ۼ����� ��츸 ����]
				String subMod="",subDel="";
				if(login_id.equals(user_id) && ((flg == null) || flg.equals("EN") || flg.equals("EC"))) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\"><img src='../ods/images/lt_modify.gif' border='0' align='absmiddle'></a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\"><img src='../ods/images/lt_del.gif' border='0' align='absmiddle'></a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table.setModuleName(rs.getString("module_name"));	
				table.setMail(rs.getString("mail"));	
				table.setMailAdd(rs.getString("mail_add"));	

				table_list.add(table);
				show_cnt++;
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// DB OfficialDocument���� �ش�ID QUERY�ϱ� (���� �б�)
	//*******************************************************************/	
	public ArrayList getDoc_Read (String id) throws Exception
	{
		//���� �ʱ�ȭ
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//query���� �����
		query = "SELECT * FROM OfficialDocument where id='"+id+"'";	
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
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));	
				table.setReference(rs.getString("reference"));
				table.setSending(rs.getString("sending"));	
				table.setSubject(rs.getString("subject"));	
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				table.setFirmName(rs.getString("firm_name"));	
				table.setRepresentative(rs.getString("representative"));
				table.setEtc(rs.getString("etc"));	

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
	//	�ش��� �μ����� �ڵ� �����ϱ�
	//*******************************************************************/	
	private String searchAcId (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//�μ� �����ڵ� �˾ƺ���
		query  = "SELECT b.ac_id FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("ac_id");
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();

		return rtn;
	}

	/*******************************************************************
	* �������� ���� ���� �����ϱ� 
	*******************************************************************/
	public void inputODTTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,String slogan,String title_name,
		String in_date,String enforce_date,String receive,String reference,String sending,String subject,
		String firm_name,String representative,String module_name,String mail,String mail_add,
		String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		slogan = str.repWord(slogan,"'","`");				//ǥ��			
		title_name = str.repWord(title_name,"'","`");		//�Ӹ���
		firm_name = str.repWord(firm_name,"'","`");			//������
		subject = str.repWord(subject,"'","`");				//����		
		content = str.repWord(content,"'","`");				//����
		

		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO OfficialDocument(id,user_id,user_name,user_rank,";
			incommon += "ac_id,ac_code,code,ac_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,";
			incommon += "subject,bon_path,bon_file,firm_name,representative,module_name,mail,mail_add) values('";
		
		String input = incommon+id+"','"+user_id+"','"+user_name+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+code+"','"+ac_name+"','"+class_no+"','"+slogan+"','"+title_name+"','";
			input += in_date+"','"+enforce_date+"','"+receive+"','"+reference+"','"+sending+"','"+subject+"','";
			input += doc_pat+"','"+id+"','"+firm_name+"','"+representative+"','"+module_name+"','"+mail+"','"+mail_add+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//����1. �������Ϸ� �����ϱ�
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,id,content);
		}
	}

	/*******************************************************************
	* �������� ���� ���� �����ϱ� 
	*******************************************************************/
	public void updateODTTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,String slogan,String title_name,
		String in_date,String enforce_date,String receive,String reference,String sending,String subject,
		String firm_name,String representative,String module_name,String mail,String mail_add,
		String content,String upload_path) throws Exception
	{
		//' ���ֱ�
		slogan = str.repWord(slogan,"'","`");				//ǥ��			
		title_name = str.repWord(title_name,"'","`");		//�Ӹ���
		firm_name = str.repWord(firm_name,"'","`");			//������
		subject = str.repWord(subject,"'","`");				//����		
		content = str.repWord(content,"'","`");				//����
		
		//����������丮�� ���ϸ�
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE OfficialDocument set user_id='"+user_id+"',user_name='"+user_name;
			update += "',user_rank='"+user_rank+"',"+"ac_id='"+ac_id+"',ac_code='"+ac_code+"',code='"+code;
			update += "',ac_name='"+ac_name+"',class_no='"+class_no+"',slogan='"+slogan;
			update += "',title_name='"+title_name+"',in_date='"+in_date+"',enforce_date='"+enforce_date;
			update += "',receive='"+receive+"',reference='"+reference+"',sending='"+sending;
			update += "',subject='"+subject+"',bon_path='"+doc_pat+"',bon_file='"+id;
			update += "',firm_name='"+firm_name+"',representative='"+representative;
			update += "',module_name='"+module_name+"',mail='"+mail+"',mail_add='"+mail_add;
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
		String update = "update OfficialDocument set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
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
		String update = "update OfficialDocument set ";
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
		String query  = "SELECT * FROM OfficialDocument where id='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = "";	//Ȯ��path
			bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = "";	//�������� ���ϸ�
			sname = rs.getString("sname");	if(sname == null) sname = "";				//����:���������	(Ȯ���� .bin�̾���)
		}

		boolean dbt = deleteBonText(upload_path,bon_path,bon_file);		//�������� �����ϱ�
		boolean daf = deleteAddFile(upload_path,bon_path,sname);		//÷������ �����ϱ�
		if(dbt || daf) {
			deleteTableLine("OfficialDocument",id);		//�ش�line �����ϱ�
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
		//System.out.println("bonmun file : " + filename);
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
				//System.out.println("delete attach file : " + filename);
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
		//System.out.println("delete : " + query);
		stmt.execute(query);
		stmt.close();
	}
	
}