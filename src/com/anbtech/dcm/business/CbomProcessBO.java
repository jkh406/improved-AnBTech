package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomProcessBO
{
	private Connection con;
	private com.anbtech.dcm.db.CbomModifyDAO cmodDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();				//일자입력
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();//내용을 파일로 담기
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public CbomProcessBO(Connection con) 
	{
		this.con = con;
		cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		기술 검토 책임자의 역활에 관한 메소드 
	//			1. 기술검토 책임자 변경
	//			2. ECR보완 요청으로 반려
	//			3. 기술검토담당자 전송
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* 기술검토 책임자 변경
	 *******************************************************************/
	 public String changeMgr(String pid,String mgr_id,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//기술검토 책임자 변경 가능한 상태를 검사한다. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("3")) {
			data = "기술검토 책임자 접수일때 책임자 변경을 지정 할 수 있습니다.";
			//System.out.println(data);
			return data;
		}

		//책임자 정보 알기 : 사번,이름,부서관리코드,부서코드,부서명,전화번호
		String mgr_info[] = new String[6]; 
		mgr_info = cmodDAO.getUserData(mgr_id);

		//ECC_COM에 수정하기
		update = "UPDATE ecc_com set mgr_id='"+mgr_id+"',mgr_name='"+mgr_info[1];
		update += "',mgr_code='"+mgr_info[2]+"',mgr_div_code='"+mgr_info[3];
		update += "',mgr_div_name='"+mgr_info[4]+"' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "정상적으로 기술검토 책임자가 변경되었습니다.";
		//System.out.println(data);

		//전자우편으로 책임자 변경사항을 알려준다.
		String title = "기술검토 책임자 변경";
		sendMail(title,user_id,user_name,note,mgr_id,mgr_info[1]);

		return data;
	} 

	/*******************************************************************
	* ECR보완 요청으로 반려
	 *******************************************************************/
	 public String rejectMgr(String pid,String eco_id,String eco_name,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//기술검토 책임자 변경 가능한 상태를 검사한다. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		
		//ECR 반려 : 결재시, 기술검토책임자반려, 기술검토담당자반려
		if(ecc_status.equals("2") || ecc_status.equals("3") || ecc_status.equals("4")) {
			update = "UPDATE ecc_com set ecc_status='0' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
			data = "정상적으로 ECR이 반려되었습니다.";
		}
		//ECO 반려 : 결재시, 변경관리자반려
		else if(ecc_status.equals("7") || ecc_status.equals("9")) {
			update = "UPDATE ecc_com set ecc_status='5' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
			data = "정상적으로 ECO가 반려되었습니다.";
		}
		//그외는 반려처리 않됨
		else {
			data = "반려 할 수 없는 상태입니다.";
			return data;
		}

		//전자우편으로 책임자 변경사항을 알려준다.
		String title = "ECR 반려";
		sendMail(title,user_id,user_name,note,eco_id,eco_name);

		return data;
	} 

	/*******************************************************************
	* 기술검토담당자 전송
	 *******************************************************************/
	 public String setUser(String pid,String eco_id,String user_id,String user_name,String note) throws Exception
	{
		 String update="",data="";

		//기술검토 책임자 변경 가능한 상태를 검사한다. 
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("3")) {
			data = "기술검토 책임자 접수일때 기술검토담당자를 지정 할 수 있습니다.";
			//System.out.println(data);
			return data;
		}

		//기술검토담당자 정보 알기 : 사번,이름,부서관리코드,부서코드,부서명,전화번호
		String eco_info[] = new String[6]; 
		eco_info = cmodDAO.getUserData(eco_id);

		//ECC_COM에 수정하기
		update = "UPDATE ecc_com set eco_id='"+eco_id+"',eco_name='"+eco_info[1];
		update += "',eco_code='"+eco_info[2]+"',eco_div_code='"+eco_info[3];
		update += "',eco_div_name='"+eco_info[4]+"',eco_tel='"+eco_info[5];
		update += "',ecc_status='4' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "정상적으로 기술검토담당자에게 전송되었습니다.";
		//System.out.println(data);

		//전자우편으로 책임자 변경사항을 알려준다.
		String title = "기술검토담당자 지정";
		sendMail(title,user_id,user_name,note,eco_id,eco_info[1]);

		return data;
	} 

	//--------------------------------------------------------------------
	//
	//		전자우편으로 내용 보내기
	//			
	//
	//---------------------------------------------------------------------
	/*********************************************************************
	// 	전자우편 으로 내용보내기  : ECR의 내용 책임자,담당자 리턴시
	*********************************************************************/
	public void sendMail(String title,String user_id,String user_name,String note,
				String rec_id,String rec_name) throws Exception 
	{	
		String pid = anbdt.getID();							//관리번호
		String subject=title,rec="";						//제목,수신자 정보
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		//1.수신자 정보 알아보기
		rec = rec_id+"/"+rec_name+";";						//수신자
		String bon_path = "/post/"+rec_id+"/text_upload";	//본문패스
		String filename = pid;								//본문저장 파일명

		//2.전자우편으로 보내기
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		cmodDAO.executeUpdate(letter);

		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		cmodDAO.executeUpdate(master);

		//3.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>알림</title></head>";
			content += "<body>";
			content += "<h3>"+title+"</h3><br>";
			content += note;
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기
	}

	/*********************************************************************
	// 	전자우편 으로 내용보내기  : ECO AUDIT 시 BOM배포처 지정
	//  eco_pid : ECC_COM의 관리번호, receivers : 수신자들, note : 기타의견
	*********************************************************************/
	public void sendAuditMail(String server_url,String title,String user_id,String user_name,
			String eco_pid,String eco_no,String receivers,String note) throws Exception 
	{	
		String pid = anbdt.getID();							//관리번호
		String subject=title;								//제목
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		String rec_id="",rec_name="";						//수신자사번,이름
		
		//1.해당되는 배포수신자 만큼 보내기
		StringTokenizer list = new StringTokenizer(receivers,"\n");
		while(list.hasMoreTokens()) {
			//수신자 정보 사번과 이름으로 분해
			String rec = list.nextToken();
			if(rec.indexOf("/") == -1) return;
			rec_id = rec.substring(0,rec.indexOf("/"));
			
			//개별 수신인 처리
			String letter="";
				letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
				letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";		
			cmodDAO.executeUpdate(letter);
		} //while

		//2.전체 마스터 작성
		String bon_path = "/post/"+rec_id+"/text_upload";	//본문패스
		String filename = pid;								//본문저장 파일명

		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + receivers + "','" + "0" + "','";
			master += "email" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";
		cmodDAO.executeUpdate(master);

		//3.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
//		String url = server_url + "/servlet/CbomProcessServlet?mode=ecc_app_mail&pid="+eco_pid+"&eco_no="+eco_no;
//		String link = "<a href=\"#\" onclick=\"javascript:window.open('" + url;
//			  link += "','audit_view','width=800px,height=600px,scrollbars=yes,toolbar=no,status=yes,resizable=no');\">상세내용보기</a>";
		String url = "../../../../../../servlet/CbomProcessServlet?mode=ecc_app_mail&pid="+eco_pid+"&eco_no="+eco_no;
		String link = "<a href=\"#\" onclick=\"javascript:wopen('" + url;
			  link += "','audit_view','800','600','scrollbars=yes,toolbar=no,status=yes,resizable=no');\">상세내용보기</a>";
		String content = "<html><head><title>알림</title>";
			content += "<META http-equiv=Content-Type content=\"text/html; charset=euc-kr\">";
			content += "</head>";
			content += "<body>";
			content += note+"<br><br><br><br>";
			content+= "	  <p align='center'>" + link + "</td>";
			content += "</body></html>\n";
			content += "<script language=javascript>\n";
			content += "<!-- \n";
			content += "function wopen(url, t, w, h, s) { \n";
			content += "var sw; \n";
			content += "var sh; \n";
			content += "sw = (screen.Width - w) / 2; \n";
			content += "sh = (screen.Height - h) / 2 - 50; \n";
			content += "window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);\n";
			content += "} \n";
			content += " -->\n";
			content += "</script>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기
		
	}

}
