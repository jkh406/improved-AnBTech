package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.admin.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class LoanBO{

	private Connection con;

	public LoanBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * no에 해당하는 테이블 내용을 가져와서 출력형태로 변환시킨다.
	 *****************************************************************/
	public LoanTable getData(String tablename,String no) throws Exception{

		LoanTable loan = new LoanTable();
		LoanDAO loanDAO = new LoanDAO(con);

		loan = loanDAO.getLoanData(tablename, no);
		String stat = loan.getStat();
		stat = getStatus(stat);
		loan.setStat(stat);

		return loan;
	} //getData()


	/*****************************************************************
	 * 쿼리문의 검색 조건을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope) throws Exception{

		//검색조건에 맞게 where변수를 수정한다.
		String where = "", where_sea = "";

		if (searchword.length() > 0){
			if ("subject".equals(searchscope)){
				where_sea = "( subject like '%" +  searchword + "%' )";
			}
			else if ("requestor_s".equals(searchscope)){
				where_sea = "( requestor_s like '%" +  searchword + "%' )";
			}
			else if ("loan_no".equals(searchscope)){
				where_sea = "( loan_no like '%" +  searchword + "%' )";
			}
			else if ("doc_no".equals(searchscope)){
				where_sea = "( doc_no like '%" +  searchword + "%' )";
			}

		}
		
		if(searchword.length() > 0) where = " WHERE " + where_sea;
		return where;
	}



	/*****************************************************************
	 * 상태코드를 받아서 상태 문자열을 만들어 준다.
	 *****************************************************************/
	public String getStatus(String stat) throws Exception{

		String status = "";

		if(stat.equals("1")) status = "처리중";
		else if(stat.equals("2")) status = "반려됨";
		else if(stat.equals("3")) status = "대출완료";
		else if(stat.equals("4")) status = "반납완료";

		return status;
	}


	/*****************************************************************
	 * 대출처리를 할 경우 안내 메시지와 첨부파일을 다운로드 할 수 있는
	 * 링크 문자열을 생성하여 전자우편으로 발송한다.
	 *****************************************************************/
	public void sendMail(String tablename,String data_id,String ver_code,String no,String login_id,String is_allowed) throws Exception{

		//1.본문 HTML 생성
		StringBuffer sb = new StringBuffer();
		if(is_allowed.equals("y")) sb = makeMailContentsForCommit(tablename,no,data_id,ver_code);
		else if(is_allowed.equals("n")) sb = makeMailContentsForReject(tablename,no,data_id,ver_code);

		//2.본문 파일 생성
		String filename	= "DOC" + System.currentTimeMillis() + ".html";
		String bon_path = "/email/" + login_id + "/text/";
		String full_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/gw/mail" + bon_path;
		makeContentsFile(sb,full_path,filename);

		//2.대출신청자 사번을 구한다.
		LoanDAO loanDAO = new LoanDAO(con);
		String requestor = loanDAO.getRequestorId(no);

		//3.메일내용을 저장
		loanDAO.saveEmail(login_id,requestor,bon_path,filename);
	}

	/*****************************************************************
	 * 대출을 허락할때의 메일본문 내용 문자열을 만든다.
	 *****************************************************************/
	public StringBuffer makeMailContentsForCommit(String tablename,String no,String data_id,String ver_code) throws Exception{
		
		//첨부파일 리스트 가져오기
		TechDocDAO techdocDAO = new TechDocDAO(con);		
		String t_id = techdocDAO.getId(tablename,data_id,ver_code);
		Iterator file_iter = techdocDAO.getFile_list(tablename,t_id).iterator();

		int i = 1;
		String filelink = "&nbsp;";
		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			TechDocTable file = (TechDocTable)file_iter.next();
			filelink += "<a href='"+com.anbtech.admin.db.ServerConfig.getConf("serverURL")+"/servlet/AnBDMS?tablename="+tablename+"&mode=download&t_id="+t_id+"_"+i+"&d_id="+data_id+"&ver="+ver_code+"'";
			filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='"+com.anbtech.admin.db.ServerConfig.getConf("serverURL")+"/dms/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
			filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

			if (file.getFileType().indexOf("mage")>0){
				filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='AnBDMS?tablename="+tablename+"&mode=download&no="+t_id+"_"+i+"' border=1></TD></TR>";
			}
			i++;
		}

		//대출 신청 정보 가져오기
		LoanDAO loanDAO = new LoanDAO(con);
		LoanTable table = new LoanTable();
		table = loanDAO.getLoanData("loan_list",no);

		String loan_no	= table.getLoanNo();
		String doc_no	= table.getDocNo();
		String ver		= table.getVerCode();
		String why		= table.getWhyReject();
		String end_date	= table.getLoanEndDate();


		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>");
		sb.append("<HEAD>");
		sb.append("<TITLE>대출처리 결과</TITLE>");
		sb.append("<STYLE TYPE='text/css'>");
		sb.append("<!--");
		sb.append(".bt {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:black}");
		sb.append(".del {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:#404040}");
		sb.append("A:link    {color:black;text-decoration:nBoard_env;}");
		sb.append("A:visited {color:black;text-decoration:nBoard_env;}");
		sb.append("A:active  {color:#007BEA;text-decoration:nBoard_env;}");
		sb.append("A:hover  {color:#0A99D4;text-decoration:underline}");
		sb.append(".sel {background-color:black;font-family:굴림체;color:white}");
		sb.append(".bd {border:solid 1 black}");
		sb.append(".ver8 {font-family:Verdana,Arial;font-size:8pt}");
		sb.append("BODY,TD,SELECT,input,DIV,form,TEXTAREA,center,option,pre,br, {font-size:10pt;}");
		sb.append("BODY { margin:0 0 0 0 }");
		sb.append("-->");
		sb.append("</STYLE>");
		sb.append("</HEAD>");
		sb.append("<BODY BGCOLOR=#ffffff>");
		sb.append("<TABLE WIDTH='700' BORDER='0' BGCOLOR='white'");
		sb.append("CELLPADDING='0' CELLSPACING='0'>");
		sb.append("<tr><td width='100%' bgcolor=cecece>&nbsp;</td></tr>");
		sb.append("<TR BGCOLOR='#eeeeee'><TD NOWRAP>");
		sb.append("<TABLE WIDTH=100% CELLPADDING=6 CELLSPACING=0 BORDER=0>");
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>대출이 완료되었습니다.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>대출 번호:</td><td width='80%'>" + loan_no + "</TD></TR>");
		sb.append("<TR><TD width='20%'>문서 번호:</td><td width='80%'>" + doc_no + "</TD></TR>");
		sb.append("<TR><TD width='20%'>문서 버젼:</td><td width='80%'>" + ver + "</TD></TR>");
		sb.append("<TR><TD width='20%'>처리 결과:</td><td width='80%'>" + why + "</TD></TR>");
		sb.append("<TR><TD width='20%'>첨부 파일:</td><td width='80%'>" + filelink + "</TD></TR>");
		sb.append("<TR><TD width='20%'>반납 일자:</td><td width='80%'>" + end_date + "</TD></TR>");
		sb.append("</TABLE>");
		sb.append("</TD></TR></TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb;
		
	}

	/*****************************************************************
	 * 대출을 반려할때의 메일본문 내용 문자열을 만든다.
	 *****************************************************************/
	public StringBuffer makeMailContentsForReject(String tablename,String no,String data_id,String ver_code) throws Exception{
		
		//대출 신청 정보 가져오기
		LoanDAO loanDAO = new LoanDAO(con);
		LoanTable table = new LoanTable();
		table = loanDAO.getLoanData("loan_list",no);

		String loan_no	= table.getLoanNo();
		String doc_no	= table.getDocNo();
		String ver		= table.getVerCode();
		String why		= table.getWhyReject();
		String end_date	= table.getLoanEndDate();


		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>");
		sb.append("<HEAD>");
		sb.append("<TITLE>대출처리 결과</TITLE>");
		sb.append("<STYLE TYPE='text/css'>");
		sb.append("<!--");
		sb.append(".bt {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:black}");
		sb.append(".del {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:#404040}");
		sb.append("A:link    {color:black;text-decoration:nBoard_env;}");
		sb.append("A:visited {color:black;text-decoration:nBoard_env;}");
		sb.append("A:active  {color:#007BEA;text-decoration:nBoard_env;}");
		sb.append("A:hover  {color:#0A99D4;text-decoration:underline}");
		sb.append(".sel {background-color:black;font-family:굴림체;color:white}");
		sb.append(".bd {border:solid 1 black}");
		sb.append(".ver8 {font-family:Verdana,Arial;font-size:8pt}");
		sb.append("BODY,TD,SELECT,input,DIV,form,TEXTAREA,center,option,pre,br, {font-size:10pt;}");
		sb.append("BODY { margin:0 0 0 0 }");
		sb.append("-->");
		sb.append("</STYLE>");
		sb.append("</HEAD>");
		sb.append("<BODY BGCOLOR=#ffffff>");
		sb.append("<TABLE WIDTH='700' BORDER='0' BGCOLOR='white'");
		sb.append("CELLPADDING='0' CELLSPACING='0'>");
		sb.append("<tr><td width='100%' bgcolor=cecece>&nbsp;</td></tr>");
		sb.append("<TR BGCOLOR='#eeeeee'><TD NOWRAP>");
		sb.append("<TABLE WIDTH=100% CELLPADDING=6 CELLSPACING=0 BORDER=0>");
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>대출신청이 반려되었습니다.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>대출 번호:</td><td width='80%'>" + loan_no + "</TD></TR>");
		sb.append("<TR><TD width='20%'>문서 번호:</td><td width='80%'>" + doc_no + "</TD></TR>");
		sb.append("<TR><TD width='20%'>문서 버젼:</td><td width='80%'>" + ver + "</TD></TR>");
		sb.append("<TR><TD width='20%'>처리 결과:</td><td width='80%'>" + why + "</TD></TR>");
		sb.append("</TABLE>");
		sb.append("</TD></TR></TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb;
		
	}

	/*****************************************************************
	 * 생성된 메일본문 내용 문자열을 파일로 담든다.
	 *****************************************************************/
	public void makeContentsFile(StringBuffer sb,String full_path,String filename) throws Exception{
		File fTargetDir = new File(full_path);
	    if(!fTargetDir.exists()) fTargetDir.mkdirs();
		BufferedWriter bw = new BufferedWriter(new FileWriter(full_path + filename));

		bw.write(""+sb);
		bw.close();
	}
}