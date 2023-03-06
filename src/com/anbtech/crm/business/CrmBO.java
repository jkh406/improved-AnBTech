package com.anbtech.crm.business;

import com.anbtech.crm.entity.*;
import com.anbtech.crm.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class CrmBO{

	private Connection con;

	public CrmBO(Connection con){
		this.con = con;
	}

	/********************************************
	 * 신규 고객사 등록 및 수정시의 폼을 만든다.
	 ********************************************/
	public CompanyInfoTable getCompanyWriteForm(String mode,String no,String login_id) throws Exception{
		CrmDAO crmDAO = new CrmDAO(con);
		CompanyInfoTable company = new CompanyInfoTable();
		
		String  company_no			= "";
		String  passwd				= "";
		String  name_kor			= "";
		String  name_eng			= "";
		String  chief_name			= "";
		String  chief_personal_no	= "";
		String  company_address		= "";
		String  company_post_no		= "";
		String  main_tel_no			= "";
		String  main_fax_no			= "";
		String  homepage_url		= "http://";
		String  business_type		= "";
		String  business_item		= "";
		String  trade_start_time	= "";
		String  trade_end_time		= "";
		String  company_type		= "";
		String  trade_type			= "";
		String  credit_level		= "";
		String  estimate_req_level	= "";
		String  worker_number		= "";
		String  main_bank_name		= "";
		String  main_newspaper_name	= "";
		String  main_product_name	= "";
		String  corporation_no		= "";
		String  founding_day		= "";
		String  other_info			= "";
		String  writer				= "";
		String  written_day			= "";
		String  modifier			= "";
		String  modified_day		= "";		

		if(mode.equals("company_modify")){
			company = crmDAO.getCompanyInfo(no);

			company_no		= company.getCompanyNo();
			passwd			= company.getPasswd();
			name_kor		= company.getNameKor();
			name_eng		= company.getNameEng();
			chief_name		= company.getChiefName();
			chief_personal_no	= company.getChiefPersonalNo();
			company_address	= company.getCompanyAddress();
			company_post_no	= company.getCompanyPostNo();
			main_tel_no		= company.getMainTelNo();
			main_fax_no		= company.getMainFaxNo();
			homepage_url	= company.getHomepageUrl();
			business_type	= company.getBusinessType();
			business_item	= company.getBusinessItem();
			trade_start_time= company.getTradeStartTime();
			trade_end_time	= company.getTradeEndTime();
			company_type	= company.getCompanyType();
			trade_type		= company.getTradeType();
			credit_level	= company.getCreditLevel();
			estimate_req_level	= company.getEstimateReqLevel();
			worker_number	= company.getWorkerNumber();
			main_bank_name	= company.getMainBankName();
			main_newspaper_name	= company.getMainNewspaperName();
			main_product_name	= company.getMainProductName();
			corporation_no	= company.getCorporationNo();
			founding_day	= company.getFoundingDay();
			other_info		= company.getOtherInfo();
			writer			= company.getWriter();
			written_day		= company.getWrittenDay();
			modifier		= company.getModifier();
			modified_day	= company.getModifiedDay();

		}

		company.setMid(no);
		company.setCompanyNo(company_no);
		company.setPasswd(passwd);
		company.setNameKor(name_kor);
		company.setNameEng(name_eng);
		company.setChiefName(chief_name);
		company.setChiefPersonalNo(chief_personal_no);
		company.setCompanyAddress(company_address);
		company.setCompanyPostNo(company_post_no);
		company.setMainTelNo(main_tel_no);
		company.setMainFaxNo(main_fax_no);
		company.setHomepageUrl(homepage_url);
		company.setBusinessType(business_type);
		company.setBusinessItem(business_item);
		company.setTradeStartTime(trade_start_time);
		company.setTradeEndTime(trade_end_time);
		company.setCompanyType(company_type);
		company.setTradeType(trade_type);
		company.setCreditLevel(credit_level);
		company.setEstimateReqLevel(estimate_req_level);
		company.setWorkerNumber(worker_number);
		company.setMainBankName(main_bank_name);
		company.setMainNewspaperName(main_newspaper_name);
		company.setMainProductName(main_product_name);
		company.setCorporationNo(corporation_no);
		company.setFoundingDay(founding_day);
		company.setOtherInfo(other_info);
		company.setWriter(writer);
		company.setWrittenDay(written_day);
		company.setModifier(modifier);
		company.setModifiedDay(modified_day);


		return company;
	}

	
	/********************************************
	 * 신규 고객 등록 및 수정시의 폼을 만든다.
	 ********************************************/
	public CustomerInfoTable getCustomerWriteForm(String mode,String no,String login_id) throws Exception{
		CrmDAO crmDAO = new CrmDAO(con);
		CustomerInfoTable customer = new CustomerInfoTable();
		
		String  name_kor			= "";
		String  name_eng			= "";
		String  sex					= "";
		String  company_name		= "";
		String  company_no			= "";
		String  division_name		= "";
		String  main_job			= "";
		String  position_rank		= "";
		String  mobile_no			= "";
		String  company_tel_no		= "";
		String  company_fax_no		= "";
		String  company_address		= "";
		String  company_post_no		= "";
		String  where_to_dm			= "";
		String  email_address		= "";
		String  homepage_url		= "";
		String  customer_type		= "";
		String  customer_class		= "";
		String  home_tel_no			= "";
		String  home_fax_no			= "";
		String  home_address		= "";
		String  home_post_no		= "";
		String  personal_no			= "";
		String  birthday			= "";
		String  whether_wedding		= "";
		String  partner_name		= "";
		String  partner_birthday	= "";
		String  wedding_day			= "";
		String  hobby				= "";
		String  special_ability		= "";
		String  major_field			= "";
		String  other_info			= "";
		String  writer				= "";
		String  written_day			= "";
		String  modifier			= "";
		String  modified_day		= "";		

		if(mode.equals("customer_modify")){
			customer = crmDAO.getCustomerInfo(no);

			name_kor			= customer.getNameKor();
			name_eng			= customer.getNameEng();
			sex					= customer.getSex();
			company_name		= customer.getCompanyName();
			company_no			= customer.getCompanyNo();
			division_name		= customer.getDivisionName();
			main_job			= customer.getMainJob();
			position_rank		= customer.getPositionRank();
			mobile_no			= customer.getMobileNo();
			company_tel_no		= customer.getCompanyTelNo();
			company_fax_no		= customer.getCompanyFaxNo();
			company_address		= customer.getCompanyAddress();
			company_post_no		= customer.getCompanyPostNo();
			where_to_dm			= customer.getWhereToDm();
			email_address		= customer.getEmailAddress();
			homepage_url		= customer.getHomepageUrl();
			customer_type		= customer.getCustomerType();
			customer_class		= customer.getCustomerClass();
			home_tel_no			= customer.getHomeTelNo();
			home_fax_no			= customer.getHomeFaxNo();
			home_address		= customer.getHomeAddress();
			home_post_no		= customer.getHomePostNo();
			personal_no			= customer.getPersonalNo();
			birthday			= customer.getBirthday();
			whether_wedding		= customer.getWhetherWedding();
			partner_name		= customer.getPartnerName();
			partner_birthday	= customer.getPartnerBirthday();
			wedding_day			= customer.getWeddingDay();
			hobby				= customer.getHobby();
			special_ability		= customer.getSpecialAbility();
			major_field			= customer.getMajorField();
			other_info			= customer.getOtherInfo();
			writer				= customer.getWriter();
			written_day			= customer.getWrittenDay();
			modifier			= customer.getModifier();
			modified_day		= customer.getModifiedDay();

		}

		customer.setMid(no);

		customer.setNameKor(name_kor);
		customer.setNameEng(name_eng);
		customer.setSex(sex);
		customer.setCompanyName(company_name);
		customer.setCompanyNo(company_no);
		customer.setDivisionName(division_name);
		customer.setMainJob(main_job);
		customer.setPositionRank(position_rank);
		customer.setMobileNo(mobile_no);
		customer.setCompanyTelNo(company_tel_no);
		customer.setCompanyFaxNo(company_fax_no);
		customer.setCompanyAddress(company_address);
		customer.setCompanyPostNo(company_post_no);
		customer.setWhereToDm(where_to_dm);
		customer.setEmailAddress(email_address);
		customer.setHomepageUrl(homepage_url);
		customer.setCustomerType(customer_type);
		customer.setCustomerClass(customer_class);
		customer.setHomeTelNo(home_tel_no);
		customer.setHomeFaxNo(home_fax_no);
		customer.setHomeAddress(home_address);
		customer.setHomePostNo(home_post_no);
		customer.setPersonalNo(personal_no);
		customer.setBirthday(birthday);
		customer.setWhetherWedding(whether_wedding);
		customer.setPartnerName(partner_name);
		customer.setPartnerBirthday(partner_birthday);
		customer.setWeddingDay(wedding_day);
		customer.setHobby(hobby);
		customer.setMajorField(major_field);
		customer.setOtherInfo(other_info);
		customer.setWriter(writer);
		customer.setWrittenDay(written_day);
		customer.setModifier(modifier);
		customer.setModifiedDay(modified_day);

		return customer;
	}


	/*****************************************************************
	 * 쿼리문의 검색 조건을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword,String searchscope,String category,String login_id) throws Exception{
		String where = "", where_and = "", where_sea = "", where_cat = "";

		if(category.length() > 0) where_cat = "category like '" + category + "%'";
		if (searchword.length() > 0){
			if (searchscope.equals("company_name")){			// 회사명
				where_sea += "( name_kor like '%" +  searchword + "%' or name_eng like '%" +  searchword + "%')";
			}
			else if (searchscope.equals("comp_name")){			// 고객의 회사명
				where_sea += "( company_name like '%" +  searchword + "%')";
			}
			else if (searchscope.equals("customer_name")){		// 고객명
				where_sea += "( name_kor like '%" +  searchword + "%' or name_eng like '%" +  searchword + "%')";
			}
			else if (searchscope.equals("chief_name")){			// 대표자명
				where_sea += "( chief_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("business_type")){			// 고객사유형
				where_sea += "( business_type = '" +  searchword + "' )";
			}
			else if (searchscope.equals("business_item")){			// 고객사분류
				StringTokenizer item_list = new StringTokenizer(searchword,",");
				int item_len = item_list.countTokens();
				for(int i=0; i<item_len-1; i++){
					where_sea += "( business_item like '%" +  item_list.nextToken() + "%' ) or ";
				}
				where_sea += "( business_item like '%" +  item_list.nextToken() + "%' )";
			}
			else if (searchscope.equals("customer_type")){			// 고객유형
				where_sea += "( customer_type = '" +  searchword + "' )";
			}
			else if (searchscope.equals("customer_class")){			// 고객분류
				StringTokenizer item_list = new StringTokenizer(searchword,",");
				int item_len = item_list.countTokens();
				for(int i=0; i<item_len-1; i++){
					where_sea += "( customer_class like '%" +  item_list.nextToken() + "%' ) or ";
				}
				where_sea += "( customer_class like '%" +  item_list.nextToken() + "%' )";
			}
		}
		if(category.length() > 0 && searchword.length() > 0) where_and = " and ";
		if(category.length() > 0 || searchword.length() > 0) where = " WHERE " + where_cat + where_and + where_sea;

		return where;
	}

	/******************************************
	 * 첨부파일 업로딩 처리
	 ******************************************/
	public CompanyInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception
	{
		String filename = "";
		String filetype = "";
		String filesize = "";
		String filese ="";
		String fumask ="";
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
				//fumask = fumask + (no+"_"+i)+"|";
				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
			}
			i++;
		}
		com.anbtech.crm.entity.CompanyInfoTable file = new com.anbtech.crm.entity.CompanyInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(no);

		return file;
	} //getFile_frommulti()


	/******************************************
	 * 첨부파일 업로딩 처리 (수정시)
	 ******************************************/
	public CompanyInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String fileumask = "";
	
		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			CompanyInfoTable file = new CompanyInfoTable();
			if(file_iter.hasNext()) file = (CompanyInfoTable)file_iter.next();

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
				fileumask = fileumask+(no+"_"+j)+"|";
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

				fileumask = fileumask+(no+"_"+i)+"|";
				filename = filename + file.getFileName() + "|";
				filetype = filetype + file.getFileType() + "|";
				filesize = filesize + file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		CompanyInfoTable file = new CompanyInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(no);

		return file;
	}//getFile_frommulti()

	/**************************************************
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String no, String filename, String filetype, String filesize,String fileumask) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"', umask='"+fileumask+"'";
		String where = " WHERE company_no = '" + no + "'";

		com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
		crmDAO.updTable(set, where);
	}

	/**************************************************
	 * 첨부파일 다운로드
	 **************************************************/
	public CompanyInfoTable getFile_fordown(String no) throws Exception
	{
		com.anbtech.crm.db.CrmDAO crmDAO = new com.anbtech.crm.db.CrmDAO(con);
		//String tablename = "item_master";
		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));
		Iterator file_iter = crmDAO.getFile_list(fileno).iterator();

		String filename="",filetype="",filesize="",fileumask="";
		int i = 1;
		while (file_iter.hasNext()){
			CompanyInfoTable file = (CompanyInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
				fileumask= file.getFileUmask();
				//System.out.println(filename);
			}
			i++;
		}
		CompanyInfoTable file = new CompanyInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFileUmask(fileumask);

		return file;
	}
}