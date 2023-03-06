package com.anbtech.gw.db;
import java.io.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.*;
import com.anbtech.gw.*;
import com.anbtech.date.*;
import com.anbtech.file.*;
import com.anbtech.text.*;
import java.lang.SecurityException;
import com.anbtech.util.normalFormat;

import java.util.Properties;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Service;
import com.anbtech.email.emailSend;
	
public class ModuleOffiDocToEmailDAO
{
	// Database Wrapper Class 선언
	private Connection con;
	private ArrayList table_list = new ArrayList();					//읽은내용

	private com.anbtech.date.anbDate anbdt = null;					//일자 처리
	private com.anbtech.util.normalFormat nmf = null;				//출력포멧
	private com.anbtech.text.StringProcess str = null;				//문자열 처리
	private com.anbtech.file.FileWriteString write;					//내용을 파일로 담기
	private com.anbtech.file.textFileReader read;					//내용을 파일로 담기
	private com.anbtech.email.emailSend email;					//email 전송하기

	private String strFileName 		= "";		//첨부파일 명1
	private String strFileName2 	= "";		//첨부파일 명2
    private String strFileName3 	= "";		//첨부파일 명3
	private	String strPath 			= "";		//첨부파일 있는 디렉토리1
    
    private String strFileFullName 	= "";		//디렉토리 + 파일명 조합 
    private String strFileFullName2 = "";		//디렉토리 + 파일명 조합2 
    private String strFileFullName3 = "";		//디렉토리 + 파일명 조합3 
          
	private String strSmtpUrl 		= "";		//Host name
	private String addressFrom 		= "";		//보내는 사람 주소 
	private String nameFrom 		= "";		//보내는 사람 이름 
	private String addressTo 		= "";		//받는사람 
	private String strSubject 		= "";		//제목 
	private String strContent 		= "";		//내용 

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public ModuleOffiDocToEmailDAO(Connection con) 
	{	
		this.con = con;

		anbdt = new com.anbtech.date.anbDate();				//날자처리
		nmf = new com.anbtech.util.normalFormat("000");		//문서번호 일련번호
		str = new com.anbtech.text.StringProcess();			//문자열 처리
		write = new com.anbtech.file.FileWriteString();		//내용을 파일로 담기 
		read = new com.anbtech.file.textFileReader();		//내용을 파일로 읽기
		email = new com.anbtech.email.emailSend();		//email 전송하기
	}

	/***************************************************************************
	 * 공지공문 읽기 (id:관리코드,flag:공문종류구분,app_date:승인일자) / 반영하기
	 **************************************************************************/
	public String SendEmailODT(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OfficialDocumentDAO docDAO = new com.anbtech.dms.db.OfficialDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail로 데이터 전송하기
		return msg;
	}

	/***************************************************************************
	 * 사내공문 읽기 / 반영하기
	 **************************************************************************/
	public String SendEmailIDS(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.InDocumentDAO docDAO = new com.anbtech.dms.db.InDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail로 데이터 전송하기
		return msg;
	}

	/***************************************************************************
	 * 사외공문작성 읽기 / 반영하기
	 **************************************************************************/
	public String SendEmailODS(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OutDocumentDAO docDAO = new com.anbtech.dms.db.OutDocumentDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail로 데이터 전송하기
		return msg;
	}

	/***************************************************************************
	 * 사외공문접수 읽기 / 반영하기
	 **************************************************************************/
	public String SendEmailODR(String id) throws Exception  
	{	
		String msg = "";
		com.anbtech.dms.db.OutDocumentRecDAO docDAO = new com.anbtech.dms.db.OutDocumentRecDAO(con);
		this.table_list = docDAO.getDoc_Read(id);

		msg = sendEmail();				//e-mail로 데이터 전송하기
		return msg;
	}

	/********************************************************************
		전자메일 보내기
	*********************************************************************/
	public String sendEmail() throws Exception
	{
		//쿼리 변수
		String msg_file = "";							//처리결과 전자우편으로 보낼때
		String msg = "";								//처리결과 화면의 스크립트로 보낼때
		String user_id = "";							//보내는사람 id
		String rec_name = "";							//수신자 이름
		String rec_mail = "";							//수신자 멜 주소
		
		//e-mail전송 변수 선언 
		String fromName = "";							//보내는사람 이름
		String fromAdd = "";							//보내는사람 주소
		String host = "";								//보내는 멜 서버명
		String subject = "";							//제목
		String content = "";							//내용
		
		//1. 공문종류별 필요정보 읽기
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			subject = table.getSubject();				//제목
			user_id = table.getUserId();				//보내는 사람 사번
			rec_name = table.getRecName();				//수신자 이름
			rec_mail = table.getRecMail();				//수신자 멜 주소
		}
		//2. 보내는사람의 이메일 보내는서버및 이메일주소 읽기
		String[] sendInfo = new String[3];
		sendInfo = getServer(user_id);
		if(sendInfo[0] == null) {
			msg = "기안자의 메일계정 정보가 없습니다.	 따라서 이메일전송을 할 수 없습니다.	 ";
			msg +="전자우편에서 환경설정후 사외공문작성에서 다시 전송하십시요.";

			//전자우편으로 보낼때
			//msg_file = "기안자의 메일계정 정보가 없습니다.<br> 따라서 이메일전송을 할 수 없습니다.<br>";
			//msg_file +="전자우편에서 환경설정후 사외공문작성에서 다시 전송하십시요.";
			//recResult(msg_file);		전송결과를 개인 전자우편으로 전달하기
			//System.out.println("msg : " + msg);
			return msg;
		} else {
			fromName = sendInfo[0];				//보내는사람 이름
			fromAdd = sendInfo[1];				//보내는사람 주소
			host = sendInfo[2];					//보내는 멜 서버명
		}
		//3.받은사람 이름 배열로 담기 (e-mail전송시 필요치 않음)
		int cnt = 1;
		for(int i=0; i<rec_name.length(); i++) if(rec_name.charAt(i) == ',') cnt++;
		String[] toName = new String[cnt];
		StringTokenizer r_name = new StringTokenizer(rec_name,",");
		int n = 0;
		while(r_name.hasMoreTokens()) {
			toName[n] = r_name.nextToken();
			n++;
		}
		//4.받은사람 멜주소 배열로 담기
		//콤마 구분자로 수신자수 찾기
		int acnt = 1;
		for(int i=0; i<rec_mail.length(); i++) if(rec_mail.charAt(i) == ',') acnt++;
		//이메일 구분자(@)로 수신자수 찾기
		int ecnt = 0;
		for(int i=0; i<rec_mail.length(); i++) if(rec_mail.charAt(i) == '@') ecnt++;
		//콤마구분자가 있는지 판단하기
		if(ecnt != acnt) {
			msg = "수신인 구분자가 없습니다. ";
			msg +="수신인이 여러명일 경우는 콤마(,)을 입력하여 구분하여 주십시요.";

			//전자우편으로 보낼때
			//msg_file = "수신인 구분자가 없습니다.<br>";
			//msg_file +="수신인이 여러명일 경우는 콤마(,)을 입력하여 구분하여 주십시요.";
			//recResult(msg_file);		전송결과를 개인 전자우편으로 전달하기
			return msg;
		}
		String[] toAdd = new String[acnt];
		StringTokenizer r_mail = new StringTokenizer(rec_mail,",");
		int m = 0;
		while(r_mail.hasMoreTokens()) {
			toAdd[m] = r_mail.nextToken();
			m++;
		}
		//5.보낼 본문내용 읽기(html형태)
		content = getContent();

		//6.첨부파일 읽기 [0],[1],[2] : 첨부파일명, [3]:첨부파일 디렉토리
		String[] filename = new String[4];
		filename = getAttachFile();

		//---------------------------------------
		//7. e-mail 전송하기 
		//---------------------------------------
		for(int i=0; i<acnt; i++) {
			email.setSmtpUrl(host);				//smtp host명
			email.setFrom(fromAdd);				//보내는 사람 주소
			email.setFromName(fromName);		//보내는 사람 이름
			email.setTo(toAdd[i]);				//받은사람 주소
			email.setSubject(subject);			//제목
			email.setContent(content);			//내용

			email.setFileName(filename[0]);		//첨부파일명1
			email.setFileName2(filename[1]);	//첨부파일명2
			email.setFileName3(filename[2]);	//첨부파일명3
			email.setPath(filename[3]);			//첨부파일 디렉토리
			
			//메시지 메일로 보내기
			String Result = "";
			Result = email.sendMessageHtml();//메시지 보내기

			//메시지 전달하기
			msg += "["+toAdd[i]+" 전송결과] : "+Result;			//화면 스크립트로 보낼때

			//msg_file += "["+toAdd[i]+" 전송결과]<br>"+Result+"<br><br>";	//전자우편으로 보낼때
		}

		//7.원래이름으로 저장된 첨부파일 삭제하기 [최대 3개]
		for(int i=0; i<3; i++) {
			String delfile = filename[3]+filename[i];
			read.delFilename(delfile);
		}

		//8.전송결과를 개인 전자우편으로 전달하기
		//recResult(msg_file);

		//9.특수문자 제외시키기
		msg = msg.replace('\t',' ');
		msg = msg.replace('\r',' ');
		msg = msg.replace('\n',' ');

		//System.out.println("msg : " + msg);
		return msg;
	}

	/*********************************************************************
	 	보내는 본문내용 읽기
	*********************************************************************/
	public String getContent() throws Exception  
	{
		String content = "";			//리턴값
		//-----------------------------------
		// 변수선언
		//-----------------------------------
		String user_name="";			//기안자 이름
		String doc_id="";				//문서번호
		String slogan="";				//슬로건
		String title_name="";			//부서 Title명
		String in_date="";				//기안일자		
		String receive="";				//수신
		String reference="";			//참조
		String sending="";				//발신
		String subject="";				//제목
		String address="";				//발신자 주소
		String tel="";					//전화번호
		String fax="";					//팩스번호
		String bon_path="";				//본문저장 확장path
		String bon_file="";				//본문저장 파일명
		String read_con="";				//본문내용
		String firm_name="";			//발신부서명
		String representative="";		//발신부서 대표명	
		String fname="";				//공통:파일원래명	
		String sname="";				//공통:파일저장명		
		String ftype="";				//공통:파일확장자명	
		String fsize="";				//공통:파일크기
		String[][] addFile;				//첨부관련내용 담기
		

		//1.사외공문작성 내용 읽기
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			user_name=table.getUserName();				//기안자 이름
			doc_id=table.getDocId(); 
				if(doc_id == null) doc_id = "";			//문서번호
			slogan=table.getSlogan();					//슬로건
			title_name=table.getTitleName();			//부서 Title명
			in_date=table.getInDate();					//기안일자
				in_date = in_date.substring(0,10);
			receive=table.getReceive();;				//수신
			reference=table.getReference();				//참조
				if(reference == null) reference = "";
			sending=table.getSending();					//발신
			subject=table.getSubject();					//제목
			address=table.getAddress();					//발신자 주소
			tel=table.getTel();							//전화번호
			fax=table.getFax();							//팩스번호
			bon_path=table.getBonPath();				//본문저장 확장path
			bon_file=table.getBonFile();				//본문저장 파일명
			firm_name=table.getFirmName();				//발신부서명
			representative=table.getRepresentative();	//발신부서 대표명	
		}

		//2.본문 내용 읽기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");			//servlet path
		String img_path = servlet + "/gw/img";
		String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
		read_con = read.getFileString(full_path);

		content+="<html>\n";
		content+="<head>\n";
		content+="<meta http-equiv='Content-Language' content='euc-kr'>\n";
		content+="<title>사외공문</title>\n";
		content+="</head>\n";

		content+="<BODY leftmargin='0' topmargin='10' marginwidth='0' marginheight='0'>\n";
		content+="<center>";
		if(slogan.length() > 0)		content+="<font size=3><b>"+slogan+"</b></font><br>\n";
		if(title_name.length() > 0) content+="<font size=6><b>"+title_name+"</b></font><br>\n";
		if(address.length() > 0)	content+="<font size=2>&nbsp;"+address+"&nbsp;</font>\n";
		if(tel.length() > 0)		content+="<font size=2>&nbsp;TEL:"+tel+"&nbsp;</font>\n";
		if(fax.length() > 0)		content+="<font size=2>&nbsp;FAX:"+fax+"&nbsp;</font>\n";
	
		content+="<table width='640' border='0' cellspacing='0' cellpadding='0'>\n";
		//content+="	<tr><td width=100% align='left' height=20><img src='"+img_path+"/slink_logo.jpg' align='middle' border='0'></td>\n";
		content+="	<tr><td width=100% align='left' height=20><font size=3><b>SpaceLink</b></font></td>\n";
		content+="	</tr>\n";
		content+="	<tr><td width=100% align='center' height=2 bgcolor=black colspan=2></td></tr>\n";
		content+="</table>\n";
			
		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n"; 
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>문서번호 :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+doc_id+"</td>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			일&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;자 :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+in_date+"</td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			수&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;신 :</td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+receive+"</td>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>기 안 자 : </td>\n";
		content+="		<td width='230' height='20' align='left' valign='middle'>"+user_name+"</td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='20' align='center' valign='middle'>\n";
		content+="			참&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;조 :</td>\n";
		content+="		<td width='550' height='20' align='left' valign='middle' colspan=3>"+reference+"</td>\n";
		content+="	</tr>\n";
		content+="</table>\n";

		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n";
		content+="	<tr>\n";
		content+="		<td width='100%' height='10' align='left' valign='middle' colspan=2></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='90' height='30' align='center' valign='middle'>\n";
		content+="			<font size=3><b>제&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목 :</b></font></td>\n";
		content+="		<td width='550' height='30' align='left' valign='middle'>\n";
		content+="			<font size=3><b>"+subject+"</b></font></td>\n";
		content+="	</tr>\n";
		content+="	<tr><td width=100% height='2' align='center' valign='middle' colspan=2 bgcolor=black></td></tr>\n";
		content+="</table>\n";

		content+="<table width='640' border='0' cellspacing='0' cellpadding='0' style='border-collapse: collapse' bordercolor='#111111'>\n";
		content+="	<tr>\n";
		content+="		<td width='640' height='10' align='left' valign='top'></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='640' height='560' align='left' valign='top'>\n";
		content+="		<pre>"+read_con+"</pre></td>\n";
		content+="	</tr>\n";
		content+="	<tr>\n";
		content+="		<td width='100%' height='10' align='left' valign='middle' colspan=2></td>\n";
		content+="	</tr>\n";
		content+="</table>\n";

		content+="<font size=6><b>"+firm_name+"</b></font><br>\n";
		content+="<font size=6><b>"+representative+"</b></font>\n";

		content+="</center>\n";
		content+="</body>\n";
		content+="</html>\n";

		return content;
	}

	/*********************************************************************
	 	첨부파일 가져오기
	*********************************************************************/
	public String[] getAttachFile() throws Exception  
	{
		String[] filename = new String[4];			//리턴값
		for(int i=0; i<4; i++) filename[i] = "";

		//-----------------------------------
		// 변수선언
		//-----------------------------------
		String bon_path="";				//본문저장 확장path
		String bon_file="";				//본문저장 파일명
		String fname="";				//공통:파일원래명	
		String sname="";				//공통:파일저장명		
		String ftype="";				//공통:파일확장자명	
		String fsize="";				//공통:파일크기
		String[][] addFile;				//첨부관련내용 담기
		

		//1.사외공문작성 내용 읽기
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			bon_path=table.getBonPath();				//본문저장 확장path
			bon_file=table.getBonFile();				//본문저장 파일명
			fname=table.getFname();						//파일원래명	
			sname=table.getSname();						//파일저장명
		}

		//2.첨부파일 path구하기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		filename[3] = upload_path + bon_path + "/addfile/";
		//System.out.println("path : " + filename[3]);

		//3.첨부파일 구하기
		if(fname == null) fname = "";
		int cnt = 0;
		for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

		addFile = new String[cnt][2];
		for(int i=0; i<cnt; i++) for(int j=0; j<2; j++) addFile[i][j]="";

		if(fname.length() != 0) {
			StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
			int m = 0;
			while(f.hasMoreTokens()) {
				addFile[m][0] = f.nextToken();
				addFile[m][0] = addFile[m][0].trim(); 
				if(addFile[m][0] == null) addFile[m][0] = "";
				//System.out.println("fname : " + addFile[m][0]);
				m++;
			}
			StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
			m = 0;
			while(o.hasMoreTokens()) {
				addFile[m][1] = o.nextToken();
				addFile[m][1] = addFile[m][1].trim() + ".bin";			
				if(addFile[m][1] == null) addFile[m][1] = "";
				//System.out.println("sname : " + addFile[m][1]);
				m++;
			}
		}
		//4.첨부파일을 원래이름으로 바꾸기
		for(int i=0; i<cnt; i++) {
			if(addFile[i][0].length() > 3) {
				String orgFile = filename[3]+addFile[i][0];				//원래의 파일명
				String savFile = filename[3]+addFile[i][1];				//저장된 파일명
				read.fileCopy(savFile,orgFile);							//원래파일명으로 복사한다.
				filename[i] = addFile[i][0];							//원래의 파일명 리턴배열에 담기
			}
		}

		return filename;
	}

	/*********************************************************************
	 	보내는 메일서버 정보 읽어오기
	*********************************************************************/
	public String[] getServer(String login_id) throws Exception  
	{
		String[] rtn = new String[3];

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		String query = "select name,address,sserver from emailInfo where id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			rtn[0] = rs.getString("name");			//보내는 사람이름
			rtn[1] = rs.getString("address");		//보내는 사람 이메일 주소
			rtn[2] = rs.getString("sserver");		//보내는 메일서버
		}
		stmt.close();
		rs.close();

		return rtn;
	}

	/*********************************************************************
	 	e-mail로 보낸결과를 기안자의 전자우편 수신함으로 받기
	*********************************************************************/
	public void recResult(String msg) throws Exception 
	{	
		String pid = getID();								//관리번호
		String subject = "";								//제목
		String user_id = "", user_name = "", rec = "";		//기안자 사번,이름
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		//1.기안자 정보 알아보기
		com.anbtech.dms.entity.OfficialDocumentTable table;						//helper 
		table = new com.anbtech.dms.entity.OfficialDocumentTable();	

		Iterator od_list = table_list.iterator();
		if(od_list.hasNext()){
			table = (com.anbtech.dms.entity.OfficialDocumentTable)od_list.next();

			user_id = table.getUserId();					//기안자 사번
			user_name = table.getUserName();				//기안자 이름
			subject = table.getSubject()+" [이메일전송 결과]";//제목
		}
		rec = user_id+"/"+user_name+";";					//수신자
		String bon_path = "/post/"+user_id+"/text_upload";	//본문패스
		String filename = pid;								//본문저장 파일명

		//2.전자우편으로 보내기
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+""+"','"+"전자결재 승인권자"+"','"+write_date+"','"+user_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + "" + "','" + "전자결재 승인권자" + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		stmt.executeUpdate(master);

		//3.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>공문이메일전송 결과</title></head>";
			content += "<body>";
			content += "<h3>공문 이메일 전송 결과</h3><br>";
			content += msg;
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		stmt.close();
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