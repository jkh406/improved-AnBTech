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
	// Database Wrapper Class 선언
	private Connection con;
	private ArrayList table_list = new ArrayList();					//읽은내용

	private com.anbtech.date.anbDate anbdt = null;					//일자 처리
	private com.anbtech.util.normalFormat nmf = null;				//출력포멧
	private com.anbtech.text.StringProcess str = null;				//문자열 처리
	private com.anbtech.file.FileWriteString write;					//내용을 파일로 담기
	private com.anbtech.file.textFileReader read;					//내용을 파일로 담기

	private String ip_addr = "";									//승인권자 ip address

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public ModuleApprovalOffiDocDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//날자처리
		nmf = new com.anbtech.util.normalFormat("000");		//문서번호 일련번호
		str = new com.anbtech.text.StringProcess();			//문자열 처리
		write = new com.anbtech.file.FileWriteString();		//내용을 파일로 담기 (개인우편보낼때 사용)
		read = new com.anbtech.file.textFileReader();		//내용을 파일로 읽기
	}
	
	/***************************************************************************
	 * 공지공문 읽기 (id:관리코드,flag:공문종류구분,app_date:승인일자) / 반영하기
	 **************************************************************************/
	public void readODT(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//게시판에 보낼때(공지공문,외부공문)
		appOfficialDocument();						//결재선내용 저장하기 [OfficialDocument_app]
		branchOfficialDocument(flag);				//게시판/전자우편/e-mail로 분기하여 데이터 전송하기
	}

	/***************************************************************************
	 * 사내공문 읽기 / 반영하기
	 **************************************************************************/
	public void readIDS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//게시판에 보낼때(공지공문,외부공문)
		appOfficialDocument();						//결재선내용 저장하기 [OfficialDocument_app]
		branchOfficialDocument(flag);				//게시판/전자우편/e-mail로 분기하여 데이터 전송하기
	}

	/***************************************************************************
	 * 사외공문 읽기 / 반영하기
	 **************************************************************************/
	public void readODS(String id,String flag,String ip_addr) throws Exception  
	{	
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		setIPADDR(ip_addr);							//게시판에 보낼때(공지공문,외부공문)
		appOfficialDocument();						//결재선내용 저장하기 [OfficialDocument_app]
		branchOfficialDocument(flag);				//게시판/전자우편/e-mail로 분기하여 데이터 전송하기
	}

	/***************************************************************************
	 * 결재선 내용 저장하기
	 **************************************************************************/
	public void appOfficialDocument() throws Exception  
	{	
		String app_id = "";							//결재 관리번호
		//전자결재의 결재관리번호 찾기
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();
			app_id = table.getAppId();		if(app_id == null) app_id = "";	//결재 관리번호
		}
		//System.out.println("app_id : " + app_id );

		//결재선 내용 읽기 (from app_save)
		ArrayList app_list = new ArrayList();									//읽은내용
		com.anbtech.dms.db.OfficialDocumentAppDAO appDAO = new com.anbtech.dms.db.OfficialDocumentAppDAO(con);
		app_list = appDAO.getDoc_AppSave(app_id);

		//결재선 내용 저장하기 (to OfficialDocument_app)
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
		//System.out.println("결재선 input : " + input );

	}

	/***************************************************************************
	 * 게시판/전자우편/이메일 전송분기 [공문관리 공통]
	 **************************************************************************/
	private void branchOfficialDocument(String flag) throws Exception  
	{	
		String id = "";					//관리번호
		String module = "";				//게시판으로 전송
		String mail = "";				//전자우편으로 전송
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();
			id = table.getId();
			module = table.getModuleName();		if(module == null) module = "";	//게시판전송여부 판단
			mail = table.getMail();				if(mail == null) mail = "";		//전자우편전송여부 판단
		}
		//System.out.println("module : " + module);
		//System.out.println("mail : " + mail);

		//------------------------------
		// 모듈이름으로 분기 [3종]
		//------------------------------
		//게시판으로 전송
		if(module.equals("게시판"))			{									
			com.anbtech.dms.db.ModuleOffiDocToBoardDAO brd = new com.anbtech.dms.db.ModuleOffiDocToBoardDAO(con);
			if(flag.equals("ODT"))			//공지공문
				brd.readODT(id,"ODT",this.ip_addr);	
			else if(flag.equals("ODR"))		//사외공문 수신
				brd.readODR(id,"ODR",this.ip_addr);
		}
		//이메일로 전송
		else if(module.equals("이메일"))		{	
			//com.anbtech.gw.db.ModuleOffiDocToEmailDAO email = new com.anbtech.gw.db.ModuleOffiDocToEmailDAO(con);
			//email.SendEmailODS(id);			//사외공문 작성시만
			//결재시 결재승인 속도가 느려 결재후 기안자가 사외공문을 메일로 다시 보낸다.
		}	
		//부서수신함으로 전송
		else if(module.equals("부서수신"))	{	
			sendDivision(flag);				//사내공문,사외공문수신
		}			

		//-------------------------------
		// 전자우편으로 분기 [1종]
		//-------------------------------
		if(mail.equals("전자우편")) sendMail();
	}

	/***************************************************************************
	 * 공문 부서수신함으로 전송하기 [공문관리 공통]
	 **************************************************************************/
	private void sendDivision(String flag) throws Exception  
	{
		String insert = "";										//insert 문장	 
		String id = getID();									//관리코드
		String in_date = anbdt.getTime();						//접수일자
		String delete_date = anbdt.getAddDateNoformat(1);		//삭제일자로 화면출력용으로 사용
		String module_add = "";									//부서주소(관리코드/부서명;...)
		String mail_add = "";									//부서장사번/이름;
		com.anbtech.dms.entity.OfficialDocumentTable table;		//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		//기초정보 작성 (사내공문 수신함)
		String input = "";
		if(flag.equals("IDS")) {
			input = "insert into InDocument_receive(id,user_id,user_name,user_rank,ac_id,ac_code,";
			input +="code,ac_name,serial_no,doc_id,in_date,receive,sending,subject,bon_path,bon_file,delete_date,";
			input +="fname,sname,ftype,fsize,flag,app_id,module_add,mail_add) values('";
		}

		//사내공문 수신부서 구하기 없으면 종료
		Iterator div_list = table_list.iterator();
		if(div_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)div_list.next();
			module_add = table.getModuleAdd();	if(module_add == null) module_add = "";
			mail_add = table.getMailAdd();	if(mail_add == null) mail_add = "";
		}
		if(module_add.length() == 0) return;
		//System.out.println("module_add : " + module_add);

		//수신부서 정보구하기
		int cnt = 0;
		for(int i=0; i<module_add.length(); i++) if(module_add.charAt(i) == ';') cnt++;
		//System.out.println("수신부서 수 cnt : " + cnt);
		String[][] address = new String[cnt][4];	//부서관리코드,부서코드,Tree관리코드,부서명
		address = searchDivInfo(cnt,module_add);	//부서정보 구하기

		//수신부서 등록할 일련번호 구하기
		String[] serial_no = new String[cnt];
		serial_no = searchSerialNo("InDocument_receive",address);

		//수신부서 부서장 배열에 담기
		String[] chief = new String[cnt];			//사번/이름
		java.util.StringTokenizer ndata = new StringTokenizer(mail_add,";");
		int ai = 0;
		while(ndata.hasMoreTokens()) {
			String rd = ndata.nextToken();
			chief[ai] = rd.trim()+";";
			//System.out.println("부서장 : " + chief[ai]);
			ai++;
			if(ai == cnt) break;
		}
		
		
		//중간 insert문장만들기
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

		//입력하기
		Statement stmt = null;
		stmt = con.createStatement();
		for(int i=0; i<cnt; i++) {
			insert = "";
			String pid = id+nmf.toDigits(i);
			insert = input + pid+"','"+input_1+"','"+address[i][0]+"','"+address[i][1]+"','"; 
			insert += address[i][2]+"','"+address[i][3]+"','"+serial_no[i]+"','"+input_2+chief[i]+"','')";
			stmt.executeUpdate(insert);
			//System.out.println("부서 수신 insert : " + insert + "\n\n");	
		}
		stmt.close();

	}
	/***************************************************************************
	 * 공문 부서수신함으로 전송하기 [부서정보 구하기]
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
	 * 공문 부서수신함으로 전송하기 [부서 접수번호 구하기 : 일련번호 03-001 ...]
	 **************************************************************************/
	private String[] searchSerialNo(String tablename,String[][] address) throws Exception  
	{
		int add_len = address.length;				//address 배열갯수
		String[] serial_no = new String[add_len];

		String serial_year = anbdt.getYear();
		String s_year = serial_year.substring(2,4);	//년도 두자리 구하기

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
	
		String query = "select distinct serial_no from "+tablename;
		for(int i=0; i<add_len; i++) {
			int no = 1;
			String q = query + " where ac_id ='"+address[i][0]+"' order by serial_no desc";
			//System.out.println("일련번호 q : " + q);
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
	 * 전자우편으로 전송하기 [공문관리 공통]
	 **************************************************************************/
	private void sendMail() throws Exception  
	{
		String sid = "",app_id="",subject="";		//공문내용을 링크로 전달하기(html문장)
		String module_add = "";						//보낼부서 주소로 사내발송문서만 있음.
		//-------------------------------------------------------
		//관련내용 post tabel(post_master,post_letter)로 담기 선언
		//-------------------------------------------------------
		String pid = getID();													//관리번호
		String w_date = anbdt.getTime();										//작성일
		String delete_date = anbdt.getAddMonthNoformat(1);						//삭제예정일
		String filename = pid;													//본문저장파일명
		String mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
			  mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		String lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
			  lquery += "post_receiver,isopen,post_select,delete_date) values('";
		
		Statement stmt = null;
		stmt = con.createStatement();

		//-------------------------------------------------------
		//관련내용 읽어 해당문장 만들기
		//-------------------------------------------------------
		String post_bon_path = "";												//post에 저장할 본문path
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();		
		Iterator table_iter = table_list.iterator();
		if(table_iter.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)table_iter.next();

			//내용을 본문내용에서 링크 처리
			sid = table.getId();												//관리번호
			app_id = table.getAppId();											//결재승인 관리번호
			subject = table.getSubject();										//제목
			module_add = table.getModuleAdd();									//사내 수신부서명
			if(module_add == null) module_add = "";

			post_bon_path = "/post/"+table.getUserId()+"/text_upload";			//post에 저장할 본문path
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
			String receivers = table.getMailAdd();		//개별사번만을 찾아 입력하기
			StringTokenizer dd = new StringTokenizer(receivers,";");
			while(dd.hasMoreTokens()) {
				String rd = dd.nextToken();		rd=rd.trim();		//사번/이름
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
		//본문파일 만들기
		//-------------------------------------------------------
		String upload_path = "";
		upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");	//servlet path
		// 공문 본문내용 만들기
		String content = "<html><head><title>공문</title></head>";
		if(module_add.length() > 0) {		//사내발송 공문
			  content += " <script> function contentAppview(id,app_id){";
			  content += " sParam=\"strSrc=InDocumentServlet&mode=IND_A&id=\"+id+\"&doc_id=\"+app_id"+"\n";
			  content += " var rval = showModalDialog(\""+servlet+"/ods/DocModalFrm.jsp?\"+sParam,\"\",\"dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0\" )";
			  content += "}";
			  content += "</script>";
			  content += "<body>";
			  content += "공문 내용입니다.<br>";
			  content += "상세내용은 아래제목을 클릭하세요.<br>";
			  content += "<br><br>";
			  content += "<a href=\"javascript:contentAppview('"+sid+"','"+app_id+"');\">"+subject+"</a>";
		} else {						//전사 공지 공문
			  content += " <script> function contentAppview(id,app_id){";
			  content += " sParam=\"strSrc=OfficialDocumentServlet&mode=OFD_A&id=\"+id+\"&doc_id=\"+app_id"+"\n";
			  content += " var rval = showModalDialog(\""+servlet+"/ods/DocModalFrm.jsp?\"+sParam,\"\",\"dialogWidth:720px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0\" )";
			  content += "}";
			  content += "</script>";
			  content += "<body>";
			  content += "공문 내용입니다.<br>";
			  content += "상세내용은 아래제목을 클릭하세요.<br>";
			  content += "<br><br>";
			  content += "<a href=\"javascript:contentAppview('"+sid+"','"+app_id+"');\">"+subject+"</a>";
		}
		content += "</body></html>";
		//System.out.println("본문내용 : " + content);

		// 전자우편용 본문내용 파일만들기
		String path = upload_path + "/gw/mail" + post_bon_path;					//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		//-------------------------------------------------------
		//닫기
		//-------------------------------------------------------
		stmt.close();
	}

	/******************************************************************************
		승인권자 ip address 
	******************************************************************************/
	public void setIPADDR(String ip_addr)
	{
		this.ip_addr = ip_addr;
	}

	/******************************************************************************
	// ID을 구하는 메소드
	******************************************************************************/
	private String getID()
	{
		String ID;
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		String y = first.format(now);
		String s = last.format(now);
		nmf.setFormat("000");		//일련번호 출력 형식(6자리)		
		ID = y + nmf.toDigits(Integer.parseInt(s));
		return ID;
	}

}	
