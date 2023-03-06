package com.anbtech.em.business;

import com.anbtech.em.entity.*;
import com.anbtech.em.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class EstimateItemBO{

	private Connection con;

	public EstimateItemBO(Connection con){
		this.con = con;
	}

	/***************************************
	 * 쿼리문의 검색 조건을 만든다.
	 ***************************************/
	public String getWhere(String mode,String searchword,String searchscope,String category) throws Exception{

		String where = "", where_and = "", where_sea = "", where_cat = "";

		if(category.length() > 0) where_cat = "category_code like '" + category + "%'";
		if (searchword.length() > 0){
			if (searchscope.equals("item_name")){			// 품목명
				where_sea += "( item_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("model_name")){		// 모델명
				where_sea += "( model_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("maker_name")){		// 제조회사명
				where_sea += "( maker_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("supplyer_name")){		// 공급회사명
				where_sea += "( supplyer_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("item_no")){	// 품목번호
				where_sea += "( item_no like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("package_name")){	// 패키지명
				where_sea += "( package_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("writer")){		// 작성자 사번
				where_sea += "( writer like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("modifier")){	// 수정자 사번
				where_sea += "( modifier like '%" +  searchword + "%' )";
			}
		}
		if(category.length() > 0 && searchword.length() > 0) where_and = " and ";
		if(category.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;

		return where;
	}

	/***************************************
	 * 외부구입조달 견적품목 등록 및 수정폼을 만든다.
	 ***************************************/
	public ItemInfoTable getWriteOutItemForm(String mode,String model_code,String supplyer_code,String category) throws Exception{
		String category_code		= "";	//카테고리코드
		String item_name			= "";	//품목명
		String model_name			= "";	//모델명
		String maker_name			= "";	//제조회사명
		String maker_part_no		= "";	//제조회사 제품번호
		String standards			= "";	//규격
		String unit					= "";	//단위

		String supplyer_name		= "";	//공급업체명
		String supply_cost			= "0";	//공급단가
		String other_info			= "";	//비고사항
		String writer				= "";	//등록자
		String written_day			= "";	//등록일자

		model_code					= "";
		supplyer_code				= "";

		ItemInfoTable item = new ItemInfoTable();

		item.setCategoryCode(category_code);
		item.setItemName(item_name);
		item.setModelCode(model_code);
		item.setModelName(model_name);
		item.setMakerName(maker_name);
		item.setStandards(standards);
		item.setUnit(unit);
		item.setSupplyerCode(supplyer_code);
		item.setSupplyerName(supplyer_name);
		item.setSupplyCost(supply_cost);
		item.setOtherInfo(other_info);
		item.setWriter(writer);
		item.setWrittenDay(written_day);
		
		return item;
	}

	/***************************************
	 * 첨부파일정보를 업데이트한다.
	 ***************************************/
	public void updFile(String no,String filename,String filetype,String filesize) throws Exception{
		String set = " SET fname = '"+filename+"',ftype = '"+filetype+"',fsize = '"+filesize+"'";
		String where = " WHERE mid = "+no;

		EstimateItemDAO itemDAO = new EstimateItemDAO(con);
		itemDAO.updTable(set, where);
	}

	
	public ItemInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";

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
		ItemInfoTable file = new ItemInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}

	public ItemInfoTable getFile_fordown(String tablename, String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		EstimateItemDAO itemDAO = new EstimateItemDAO(con);
		Iterator file_iter = itemDAO.getFileList(tablename, fileno).iterator();

		String filename="",filetype="",filesize="";
		int i = 1;
		while (file_iter.hasNext()){
			ItemInfoTable file = (ItemInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
			}
			i++;
		}
		ItemInfoTable file = new ItemInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}


	public ItemInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			ItemInfoTable file = new ItemInfoTable();
			if(file_iter.hasNext()) file = (ItemInfoTable)file_iter.next();

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
				filesize = filesize +file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		ItemInfoTable file = new ItemInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}

}