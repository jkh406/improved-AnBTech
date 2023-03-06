package com.anbtech.util;

import com.oreilly.servlet.MultipartRequest;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.anbtech.util.*;

public class FileUploadBean{
	private com.anbtech.ViewQueryBean viewquery = null;

	private	String file_name = "";
	private	String file_type = "";
	private	String file_size = "";

	/***************************************************************************
	 * 생성자 메소드
	 **************************************************************************/
	public FileUploadBean() 
	{	
		viewquery = new com.anbtech.ViewQueryBean();
		viewquery.openConnection();
	}


	public String getFileName()
	{
		return this.file_name;
	}

	public String getFileType()
	{
		return this.file_type;
	}

	public String getFileSize()
	{
		return this.file_size;
	}

	/*************************************************************************
	 * file upload (신규 등록일 때)
	 *************************************************************************/
	public void getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

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
				did = did + "0" + "|";
			}
			i++;
		}
		
		this.file_name = filename;
		this.file_type = filetype;
		this.file_size = filesize;
	}


	/*************************************************************************
	 * file upload (수정일 때)
	 *************************************************************************/
	public void getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			UpFileTable file = new UpFileTable();
			if(file_iter.hasNext()) file = (UpFileTable)file_iter.next();

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
				filesize = filesize +fsize + "|";
				did = did + "0" + "|";
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFilename() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFilename() + "|";
				filetype = filetype + file.getFiletype() + "|";
				filesize = filesize +file.getFilesize() + "|";
				did = did + file.getDid() + "|";
				j++;
			}
			i++;
		}
		this.file_name = filename;
		this.file_type = filetype;
		this.file_size = filesize;
	}


	// get uploaded file list
	//
	// id:글번호가 저장된 필드명
	// no:검색할 글번호
	// table:검색할 테이블명
	// fname:첨부파일명이 저장된 필드명
	// ftype:첨부파일의 타입이 저장된 필드명
	// fsize:첨부파일의 사이즈가 저장된 필드명
	// fmask:첨부팔일의 umask가 자정된 필드명
	public ArrayList getFile_list(String id,String no,String table,String fname,String ftype,String fsize,String fmask) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String umask	= "";

		String query = "select "+fname+","+ftype+","+fsize+","+fmask+" from "+table+" where "+id+"= '"+no+"'";


		try 
		{
			viewquery.executeQuery(query.toString());
			if(viewquery.next()) {
				filename = viewquery.getData(1);
				filetype = viewquery.getData(2);
				filesize = viewquery.getData(3);
				umask	 = viewquery.getData(4);
			}
		} 
		catch(SQLException e)
		{
		}
		ArrayList file_list = new ArrayList();

		Iterator filename_iter = com.anbtech.util.Token.getTokenList(filename).iterator();
		Iterator filetype_iter = com.anbtech.util.Token.getTokenList(filetype).iterator();
		Iterator filesize_iter = com.anbtech.util.Token.getTokenList(filesize).iterator();

		while(filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
			UpFileTable file = new UpFileTable();
			file.setFilename((String)filename_iter.next());
			file.setFiletype((String)filetype_iter.next());
			file.setFilesize((String)filesize_iter.next());
			file.setUmask((String)umask);

			file_list.add(file);
		}

		return file_list;
	}

/*
	public static void main(String args[])
	{
		FileUploadBean app = new FileUploadBean();
		try {
			app.getFile_list();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
*/
}