package com.anbtech.dms.business;

import com.anbtech.dms.entity.*;
import com.anbtech.dms.db.*;
import com.anbtech.dms.admin.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class ProposalBO{

	private Connection con;

	public ProposalBO(Connection con){
		this.con = con;
	}

	public ProposalTable getWrite_form(String tablename,String mode,String data_no,String ver_code,String why_revision) throws Exception{

		ProposalTable proposal = new ProposalTable();
		ProposalDAO proposalDAO = new ProposalDAO(con);

		if (("revision".equals(mode) || "modify".equals(mode) || "modify_a".equals(mode)) && data_no != null && ver_code != null){
			// ������ �Ǵ� ������ ��� ������ ������ �����´�.
			proposal = proposalDAO.getProposalData(tablename,data_no,ver_code);
			
			// �������� ��쿡�� ���泻�� �� ���� �̷� ������ �ʱ�ȭ��Ų��.
			if("revision".equals(mode)){
				String d1 = ver_code.substring(0,1);
				String d2 = ver_code.substring(2,3);

				if(why_revision.equals("b")){ // Bug fix�� ���
					d2 = Integer.toString(Integer.parseInt(d2)+1);
			
				}else if(why_revision.equals("u")){ // ��� ������ ���
					d1 = Integer.toString(Integer.parseInt(d1)+1);
					d2 = "0";
				}

				String curr_version = d1 + "." + d2;

				proposal.setVerCode(curr_version);
				proposal.setWhyRevision("");
				proposal.setModifyHistory("");
			}
		}

		return proposal;
	}

	/**********************************************************
	 * data_no �� ver_code �� �ش��ϴ� ���̺� ������ ������ ��,
	 * ����ϱ� ���� ���·� �����.
	 **********************************************************/
	public ProposalTable getData(String mode,String tablename,String data_id,String ver_code) throws Exception{

		ProposalTable proposal = new ProposalTable();
		ProposalDAO proposalDAO = new ProposalDAO(con);

		proposal = proposalDAO.getProposalData(tablename,data_id,ver_code);

		//���� ��е�� ó��
		String security_level = proposal.getSecurityLevel();
		if(security_level.equals("1")) proposal.setSecurityLevel("1��");
		else if(security_level.equals("2")) proposal.setSecurityLevel("2��");
		else if(security_level.equals("3")) proposal.setSecurityLevel("3��");
		else if(security_level.equals("4")) proposal.setSecurityLevel("��ܺ�");
		else if(security_level.equals("5")) proposal.setSecurityLevel("�Ϲݹ���");

		//���� �����Ⱓ ó��
		String save_period = proposal.getSavePeriod();
		if(save_period.equals("1")) proposal.setSavePeriod("1��");
		else if(save_period.equals("3")) proposal.setSavePeriod("3��");
		else if(save_period.equals("5")) proposal.setSavePeriod("5��");
		else if(save_period.equals("10")) proposal.setSavePeriod("10��");
		else if(save_period.equals("0")) proposal.setSavePeriod("����");

		//�ۼ���� ó��
		String written_lang = proposal.getWrittenLang();
		if(written_lang.equals("KOR")) proposal.setWrittenLang("�ѱ���");
		else if(written_lang.equals("ENG")) proposal.setWrittenLang("����");
		else if(written_lang.equals("JPN")) proposal.setWrittenLang("�Ϻ���");
		else if(written_lang.equals("CHN")) proposal.setWrittenLang("�߱���");
		else if(written_lang.equals("DAU")) proposal.setWrittenLang("���Ͼ�");	

		//�������� ó��
		String doc_type = proposal.getDocType();
		if(doc_type.equals("FILE")) proposal.setDocType("��������");
		if(doc_type.equals("BOOK")) proposal.setDocType("å������");	
		if(doc_type.equals("SHEET")) proposal.setDocType("SHEET����");
		if(doc_type.equals("CD")) proposal.setDocType("CD(DVD)����");	
		if(doc_type.equals("TAPE")) proposal.setDocType("����������");
		if(doc_type.equals("FILM")) proposal.setDocType("�ʸ�����");	

		//reference ó��
		//
		String src = proposal.getReference();
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

			reference += "����:<b>"+ref_item[i][0]+"</b>,����:"+ref_item[i][1]+",���ǻ�:"+ref_item[i][2]+",���ǳ⵵:"+ref_item[i][3]+"<br>";
		}

		proposal.setReference(reference);



		//�������� ó��
		//���� �����͹�ȣ(data_id)�� ������ ���� ����Ʈ�� �����ͼ� �޺��ڽ��� �����.
		//�޺��ڽ� �׸��� ���� �������ϴ� ������ ���õǰ� ó��.
		Iterator ver_iter = proposalDAO.getVerList(mode,tablename,data_id).iterator();
		int j = 1;
		String ver_list = "<select name='ver' onChange='javascript:document.viewForm.submit();'";
		if(!mode.equals("view_t")) ver_list += " disabled";
		ver_list += ">";

		while(ver_iter.hasNext()){
			ProposalTable version = (ProposalTable)ver_iter.next();
			ver_list += "<option value='"+version.getVerCode()+"' ";
			if(version.getVerCode().equals(ver_code)) ver_list += "selected ";
			ver_list += ">"+version.getVerCode()+"</option>";
			j++;
		}
		ver_list += "</select>";
		proposal.setVerCode(ver_list);

		//÷������ ����Ʈ ��������
		String t_id = proposalDAO.getId(tablename,data_id,ver_code);
		Iterator file_iter = proposalDAO.getFile_list(tablename,t_id).iterator();

		int i = 1;
		String filelink = "&nbsp;";
		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			ProposalTable file = (ProposalTable)file_iter.next();
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
		proposal.setFileLink(filelink);
		proposal.setFilePreview(filepreview);

		return proposal;
	}

	/******************************************
	 * �ű� ��� �� ÷�������� ó���Ѵ�.
	 ******************************************/
	public ProposalTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

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
		ProposalTable file = new ProposalTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	} //getFile_frommulti()


	/******************************************
	 * ���� �� ÷�������� ó���Ѵ�.
	 ******************************************/
	public ProposalTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			ProposalTable file = new ProposalTable();
			if(file_iter.hasNext()) file = (ProposalTable)file_iter.next();

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
		ProposalTable file = new ProposalTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}//getFile_frommulti()


	/**************************************************
	 * ÷�������� �ٿ�ε��ϱ� ���� ���� ����Ʈ ���
	 **************************************************/
	public ProposalTable getFile_fordown(String tablename, String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		ProposalDAO proposalDAO = new ProposalDAO(con);
		Iterator file_iter = proposalDAO.getFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",did="";
		int i = 1;
		while (file_iter.hasNext()){
			ProposalTable file = (ProposalTable)file_iter.next();
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
		ProposalTable file = new ProposalTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
//		file.setDid(did);

		return file;
	}


	/**************************************************
	 * ÷������ ������ DB�� �����ϱ� ���� ������ ����
	 **************************************************/
	public void updFile(String tablename, String no, String filename, String filetype, String filesize) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
		String where = " WHERE t_id="+no;

		ProposalDAO proposalDAO = new ProposalDAO(con);
		proposalDAO.updTable(tablename, set, where);
	}
}