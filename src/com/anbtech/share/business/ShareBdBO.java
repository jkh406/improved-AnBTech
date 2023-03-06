package com.anbtech.share.business;

import com.anbtech.share.entity.*;
import com.anbtech.share.db.*;
import com.anbtech.share.business.*;

import com.oreilly.servlet.MultipartRequest;
import java.sql.*;
import java.util.*;
import java.io.*;

public class ShareBdBO{

	private Connection con;

	public ShareBdBO(Connection con){
		this.con = con;
	}

	/**********************************************
	 * �˻��� WHERE ������� (user)
	 *********************************************/
	public String getWhere(String tablename, String mode, String searchword, String searchscope,String category) throws Exception {
							
		String where ="";
		//category = "1";
		if(category.equals("��ü")) { category = ""; }

		if("list".equals(mode)){
			if(!category.equals("") && !searchscope.equals("")) {
				where =  " WHERE (category ='"+category+"') and ("+searchscope+" like '%"+searchword+"%')";
			} else if(!category.equals("") && searchscope.equals("")){
				where =  " WHERE category ='"+category+"'"; 
			} else if(category.equals("") && !searchscope.equals("")){
				where =  " WHERE "+searchscope+" like '%"+searchword+"%'";
			}
		}
		//System.out.println("where: "+where);

		return where;
	}

	/******************************************
	 * ÷������ ���ε� ó��
	 ******************************************/
	public ShareBdTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		
		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename�� type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file�� ����� ������Ѱ�
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
		com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		file.setFpath(filepath);
		

		return file;
	} //getFile_frommulti()

		
	/******************************************
	 * ÷������ ���� ó��
	 ******************************************/
	public ShareBdTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			ShareBdTable file = new ShareBdTable();
			if(file_iter.hasNext()) file = (ShareBdTable)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename�� type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file�� ����� ������Ѱ�
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
				
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFname() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFname() + "|";
				filetype = filetype + file.getFtype() + "|";
				filesize = filesize +file.getFsize() + "|";
				
				j++;
			}
			i++;
		}
		ShareBdTable file = new ShareBdTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);

		return file;
	}

	/**************************************************
	 * ÷������ ������ DB�� �����ϱ� ���� ������ ����
	 **************************************************/
	public void updFile(String tablename, String no, String filename, String filetype, String filesize, String filepath) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"', fsize='"+filesize+"', fpath='"+filepath+"'";
		String where = " WHERE no='"+no+"'";

		com.anbtech.share.db.ShareBdDAO sbdDAO = new com.anbtech.share.db.ShareBdDAO(con);
		sbdDAO.updTable(tablename, set, where);
	}

	/**************************************************
	 * ÷������ �ٿ�ε�
	 **************************************************/
	public ShareBdTable getFile_fordown(String tablename,String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		ShareBdDAO shDAO = new ShareBdDAO(con);
		Iterator file_iter = shDAO.getFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="";
		int i = 1;
		while (file_iter.hasNext()){
			ShareBdTable file = (ShareBdTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFname();
				filetype = file.getFtype();
				filesize = file.getFsize();
			}
			i++;
		}
		ShareBdTable file = new ShareBdTable();
		file.setFname(filename);
		file.setFtype(filetype);
		file.setFsize(filesize);
		return file;
	}

	/******************************************************
	*	�������� ������ ���� Ȯ��							  *
	*  input : id(login_id), tablename
	*  1. �ش��ϴ� ���̺��� �����ڸ� �����´�.
	*  2. ����Ȯ��
	******************************************************/
	public boolean adminValid(String id, String tablename) throws Exception {
		
	com.anbtech.share.db.ShareBdDAO shDAO = new com.anbtech.share.db.ShareBdDAO(con);
	
	// 1. �ش��ϴ� ���̺��� ������ ����Ʈ(String)�� �����´�.
	String adminlist = (String)shDAO.adminList(tablename); 
	boolean bool = false;

	java.util.StringTokenizer st = new java.util.StringTokenizer(adminlist,";");
	
	// 2. login_id�� ���������� ���� �Ǵ�
	//    ������ ������ bool�� true�� setting
	while(st.hasMoreTokens()){
		String token =  st.nextToken();
		if(id.equals(token)) bool = true;
	}

	return bool;			
	}

	/****************************************************
	* �������� ��ü TABLE�� ���� BEAN(helper class)�� ���
	*****************************************************/
	public ShareBdTable getWrite_form(String tablename,String mode,String temp_no,String login_id) throws Exception{
	
	com.anbtech.share.db.ShareBdDAO shDAO = new com.anbtech.share.db.ShareBdDAO(con);
	com.anbtech.share.entity.ShareBdTable sbTable = new com.anbtech.share.entity.ShareBdTable();
		
	int		no		= 0;	//  ������ȣ 		
	String	subject = "";	//	����	 
	String	ver		= "";	//	����	 
	String	wid		= "";	//	�����ID
	String	wname	= "";	//	������̸�
	String	wdate	= "";	//	�����	
	String	doc_no	= "";	//	��Ϲ��� ��ȣ	
	String	ac_code = "";	//	�μ�Code		
	String	ac_name = "";	//	�μ���
	String	category= "";	//	ī�װ�	
	String	content = "";	//	����		
	String	fname	= "";	//	ȭ���̸�	
	String	fsize	= "";	//	ȭ�ϻ�����
	String	ftype	= "";	//	ȭ��Ÿ��	
	int		cnt		= 0;	//	��ȸ��	
//	String  category_text = "";

	//�ۼ��� ����
	com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
	com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
	userinfo = userDAO.getUserListById(login_id);

//	String division = userinfo.getDivision();
	wname	= userinfo.getUserName();
	wid		= login_id;
//	String user_rank = userinfo.getUserRank();

	if(mode.equals("modify") || mode.equals("view")) {
		sbTable = shDAO.getShareInfo(tablename,temp_no);
		no		= sbTable.getNo();			
		subject = sbTable.getSubject(); 
		ver		= sbTable.getVer();		 
		wid		= sbTable.getWid();		
		wname	= sbTable.getWname();	
		wdate	= sbTable.getWdate();		
		doc_no	= sbTable.getDocNo();	
		ac_name = sbTable.getAcName();	
		category= sbTable.getCategory();
		content = sbTable.getContent();		
		fname	= sbTable.getFname();	
		fsize	= sbTable.getFsize();	
		ftype	= sbTable.getFtype();	
		cnt		= sbTable.getCnt();	
//		category_text = getCategoryName(category,tablename);
	}

	sbTable.setNo(no);
	sbTable.setSubject(subject);
	sbTable.setVer(ver);
	sbTable.setWid(wid);
	sbTable.setWname(wname);
	sbTable.setWdate(wdate);
	sbTable.setDocNo(doc_no);
	sbTable.setAcName(ac_name);
	sbTable.setCategory(category);
	sbTable.setContent(content);
	sbTable.setCnt(cnt);	
//	sbTable.setCategoryText(category_text);
	
	return sbTable;
	}

}