package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.admin.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class TechDocBO{

	private Connection con;

	public TechDocBO(Connection con){
		this.con = con;
	}

	public TechDocTable getWrite_form(String tablename,String mode,String data_no,String ver_code,String why_revision) throws Exception{

		TechDocTable techdoc = new TechDocTable();
		TechDocDAO techdocDAO = new TechDocDAO(con);

		if (("revision".equals(mode) || "modify".equals(mode) || "modify_a".equals(mode)) && data_no != null && ver_code != null){
			// 리비젼 또는 수정일 경우 기존의 내용을 가져온다.
			techdoc = techdocDAO.getTechDocData(tablename,data_no,ver_code);
			
			// 리비젼일 경우에는 변경내용 및 수정 이력 정보를 초기화시킨다.
			if("revision".equals(mode)){
				String d1 = ver_code.substring(0,1);
				String d2 = ver_code.substring(2,3);

				if(why_revision.equals("b")){ // Bug fix인 경우
					d2 = Integer.toString(Integer.parseInt(d2)+1);
			
				}else if(why_revision.equals("u")){ // 기능 개선인 경우
					d1 = Integer.toString(Integer.parseInt(d1)+1);
					d2 = "0";
				}

				String curr_version = d1 + "." + d2;

				techdoc.setVerCode(curr_version);
				techdoc.setWhyRevision("");
				techdoc.setModifyHistory("");
			}
		}

		return techdoc;
	}

	/**********************************************************
	 * data_no 및 ver_code 에 해당하는 테이블 정보를 가져온 뒤,
	 * 출력하기 위한 형태로 만든다.
	 **********************************************************/
	public TechDocTable getData(String mode,String tablename,String data_id,String ver_code) throws Exception{

		TechDocTable techdoc = new TechDocTable();
		TechDocDAO techdocDAO = new TechDocDAO(con);

		techdoc = techdocDAO.getTechDocData(tablename,data_id,ver_code);

		//문서 비밀등급 처리
		String security_level = techdoc.getSecurityLevel();
		if(security_level.equals("1")) techdoc.setSecurityLevel("1급");
		else if(security_level.equals("2")) techdoc.setSecurityLevel("2급");
		else if(security_level.equals("3")) techdoc.setSecurityLevel("3급");
		else if(security_level.equals("4")) techdoc.setSecurityLevel("대외비");
		else if(security_level.equals("5")) techdoc.setSecurityLevel("일반문서");

		//문서 보존기간 처리
		String save_period = techdoc.getSavePeriod();
		if(save_period.equals("1")) techdoc.setSavePeriod("1년");
		else if(save_period.equals("3")) techdoc.setSavePeriod("3년");
		else if(save_period.equals("5")) techdoc.setSavePeriod("5년");
		else if(save_period.equals("10")) techdoc.setSavePeriod("10년");
		else if(save_period.equals("0")) techdoc.setSavePeriod("영구");

		//작성언어 처리
		String written_lang = techdoc.getWrittenLang();
		if(written_lang.equals("KOR")) techdoc.setWrittenLang("한국어");
		else if(written_lang.equals("ENG")) techdoc.setWrittenLang("영어");
		else if(written_lang.equals("JPN")) techdoc.setWrittenLang("일본어");
		else if(written_lang.equals("CHN")) techdoc.setWrittenLang("중국어");
		else if(written_lang.equals("DAU")) techdoc.setWrittenLang("독일어");	

		//문서유형 처리
		String doc_type = techdoc.getDocType();
		if(doc_type.equals("FILE")) techdoc.setDocType("파일형태");
		if(doc_type.equals("BOOK")) techdoc.setDocType("책자형태");	
		if(doc_type.equals("SHEET")) techdoc.setDocType("SHEET형태");
		if(doc_type.equals("CD")) techdoc.setDocType("CD(DVD)형태");	
		if(doc_type.equals("TAPE")) techdoc.setDocType("테이프형태");
		if(doc_type.equals("FILM")) techdoc.setDocType("필름형태");	

		//reference 처리
		//
		String src = techdoc.getReference();
		String reference = "";

		StringTokenizer str = new StringTokenizer(src, "^");
		int ref_count = str.countTokens();
		String ref[] = new String[ref_count];
		String ref_item[][] = new String[ref_count][4];

		for(int i=0; i<ref_count; i++){ 
			ref[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(ref[i],"|");
			int item_count = str2.countTokens();

			if(item_count < 1 ) break;

			for(int j=0; j<item_count; j++){ 
				ref_item[i][j] = str2.nextToken();
			}

			if(ref_item[i][1]==null) ref_item[i][1] = "";
			if(ref_item[i][2]==null) ref_item[i][2] = "";
			if(ref_item[i][3]==null) ref_item[i][3] = "";

			reference += "제목:<b>"+ref_item[i][0]+"</b>,저자:"+ref_item[i][1]+",출판사:"+ref_item[i][2]+",출판년도:"+ref_item[i][3]+"<br>";
		}

		techdoc.setReference(reference);



		//문서버젼 처리
		//현재 데이터번호(data_id)를 가지는 버젼 리스트를 가져와서 콤보박스를 만든다.
		//콤보박스 항목중 현재 엑세스하는 버젼을 선택되게 처리.
		Iterator ver_iter = techdocDAO.getVerList(mode,tablename,data_id).iterator();
		int j = 1;
		String ver_list = "<select name='ver' onChange='javascript:document.viewForm.submit();'";
		if(!mode.equals("view_t")) ver_list += " disabled";
		ver_list += ">";

		while(ver_iter.hasNext()){
			TechDocTable version = (TechDocTable)ver_iter.next();
			ver_list += "<option value='"+version.getVerCode()+"' ";
			if(version.getVerCode().equals(ver_code)) ver_list += "selected ";
			ver_list += ">"+version.getVerCode()+"</option>";
			j++;
		}
		ver_list += "</select>";
		techdoc.setVerCode(ver_list);

		//첨부파일 리스트 가져오기
		String t_id = techdocDAO.getId(tablename,data_id,ver_code);
		Iterator file_iter = techdocDAO.getFile_list(tablename,t_id).iterator();

		int i = 1;
		String filelink = "&nbsp;";
		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			TechDocTable file = (TechDocTable)file_iter.next();
			filelink += "<a href='AnBDMS?tablename="+tablename+"&mode=download&t_id="+t_id+"_"+i+"&d_id="+data_id+"&ver="+ver_code+"'";
			filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='../dms/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
			filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

			if (file.getFileType().indexOf("mage")>0){
				filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='AnBDMS?tablename="+tablename+"&mode=download&no="+t_id+"_"+i+"' border=1></TD></TR>";
			}
			i++;
		}
		filepreview = filepreview + "</TABLE>";
		techdoc.setFileLink(filelink);
		techdoc.setFilePreview(filepreview);

		return techdoc;
	}

	/******************************************
	 * 신규 등록 시 첨부파일을 처리한다.
	 ******************************************/
	public TechDocTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+i+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
			}
			i++;
		}
		TechDocTable file = new TechDocTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	} //getFile_frommulti()


	/******************************************
	 * 수정 시 첨부파일을 처리한다.
	 ******************************************/
	public TechDocTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			TechDocTable file = new TechDocTable();
			if(file_iter.hasNext()) file = (TechDocTable)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize + fsize + "|";
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFileName() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFileName() + "|";
				filetype = filetype + file.getFileType() + "|";
				filesize = filesize + file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		TechDocTable file = new TechDocTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}//getFile_frommulti()


	/**************************************************
	 * 첨부파일을 다운로드하기 위한 파일 리스트 출력
	 **************************************************/
	public TechDocTable getFile_fordown(String tablename, String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		TechDocDAO techdocDAO = new TechDocDAO(con);
		Iterator file_iter = techdocDAO.getFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",did="";
		int i = 1;
		while (file_iter.hasNext()){
			TechDocTable file = (TechDocTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
//				did = did + Integer.toString(Integer.parseInt(file.getDid())+1) + "|";
			}else{
//				did = did + file.getDid() + "|";
			}
			i++;
		}
		TechDocTable file = new TechDocTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
//		file.setDid(did);

		return file;
	}


	/**************************************************
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String tablename, String no, String filename, String filetype, String filesize) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
		String where = " WHERE t_id="+no;

		TechDocDAO techdocDAO = new TechDocDAO(con);
		techdocDAO.updTable(tablename, set, where);
	}
}