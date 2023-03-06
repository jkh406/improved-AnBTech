package com.anbtech.ca.business;

import com.anbtech.ca.entity.*;
import com.anbtech.ca.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class ComponentApprovalBO{

	private Connection con;

	public ComponentApprovalBO(Connection con){
		this.con = con;
	}
	

	/*****************************************************************
	 * ����Ƿ� ���� �ڵ忡 �����ϴ� �̸��� �����´�.
	 *****************************************************************/
	public String getRequestCodeName(String code) throws Exception{
		String code_name = "";

		if(code.equals("1")) code_name = "�ű�";
		else if(code.equals("2")) code_name = "��纯��";
		else if(code.equals("3")) code_name = "��ü�߰�";
		else if(code.equals("4")) code_name = "��ǰ�������";
		else if(code.equals("5")) code_name = "��ü�������";

		return code_name;
	}

	/*****************************************************************
	 * ���� ���� �ڵ忡 �����ϴ� �̸��� �����´�.
	 *****************************************************************/
	public String getApproveCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("A")) code_name = "�հ�";
		else if(code.equals("B")) code_name = "��������";
		else if(code.equals("C")) code_name = "RESERVED";
		else if(code.equals("D")) code_name = "RESERVED";
		else if(code.equals("E")) code_name = "RESERVED";
		else if(code.equals("F")) code_name = "������";

		return code_name;
	}

	/*****************************************************************
	 * ǰ����� �ڵ忡 �����ϴ� �̸��� �����´�.
	 *****************************************************************/
	public String getUnitCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("EA")) code_name = "��";

		return code_name;
	}

	/*****************************************************************
	 * ÷�ι��� ���� �ڵ忡 �����ϴ� �̸��� �����´�.
	 *****************************************************************/
	public String getAttachFileCodeName(String code) throws Exception{

		String code_name = "";

		if(code.equals("1")) code_name = "���ο�";
		else if(code.equals("2")) code_name = "����/�˻� ������";
		else if(code.equals("3")) code_name = "��ǰ��缭";
		else if(code.equals("4")) code_name = "�����ͺ�";
		else if(code.equals("5")) code_name = "����";
		else if(code.equals("6")) code_name = "�ߺ�ǰ";
		else if(code.equals("7")) code_name = "ī�޷α�";
		else if(code.equals("8")) code_name = "RESERVED";
		else if(code.equals("9")) code_name = "RESERVED";
		else if(code.equals("0")) code_name = "��Ÿ";

		return code_name;
	}

	/**********************************************************
	 * ������ ���ι����� ������ȣ�� �ش��ϴ� ������ �����ͼ� ��������� �����.
	 **********************************************************/
	public CaMasterTable getViewForm(String no) throws Exception{

		CaMasterTable table = new CaMasterTable();
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		table = caDAO.getApprovalInfoByMid(no);

		//÷������ ����Ʈ ��������
		String mid = table.getMid();
		String umask = table.getUmask();

		Iterator file_iter = caDAO.getFile_list(mid).iterator();

		int i = 1;
		String filelink = "&nbsp;";
		String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
		while(file_iter.hasNext()){
			CaMasterTable file = (CaMasterTable)file_iter.next();
			filelink += "<a href='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"&umask="+umask+"_"+i+"'";
			filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
			filelink += "onMouseOut=\"window.status='';return true;\" >";
			filelink += "<img src='../ca/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
			filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

			if (file.getFileType().indexOf("mage")>0){
				filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"' border=1></TD></TR>";
			}
			i++;
		}
		filepreview = filepreview + "</TABLE>";
		table.setFileLink(filelink);
		table.setFilePreview(filepreview);

		return table;
	}


	/**********************************************************
	 * �ű�,����,���� ���� �Է����� �����.
	 * no:������ȣ,item_no:ǰ���ȣ
	 **********************************************************/
	public CaMasterTable getWriteForm(String mode,String no,String item_no) throws Exception{

		CaMasterTable table = new CaMasterTable();
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");

		//��Ƽ ��� ����� ��쿡�� �ӽý��ι�ȣ�� ������ mid�� ���ؿ´�.
		if(mode.equals("write_m")) no = caDAO.getMaxMid(no);

		if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("modify") || mode.equals("write_a")){
			table = caDAO.getApprovalInfoByMid(no);
		}

		if(mode.equals("write_s") || mode.equals("write_m") || mode.equals("write_r") || mode.equals("write_a")){
			table.setRequestDate(vans.format(now));
		}

		//��Ƽ ��� ����� ���
		if(mode.equals("write_m")){
			table.setItemNo("");		// ǰ���ȣ �ʱ�ȭ
			table.setItemName("");		// ǰ��� �ʱ�ȭ
			table.setItemDesc("");		// ǰ�񼳸� �ʱ�ȭ
			table.setMakerPartNo("");	// ��ü��ǰ��ȣ �ʱ�ȭ
			table.setWhyApprove("");	// �������� �ʱ�ȭ
			table.setApproveType("");	// ���α���
			table.setApplyQuantity("");	// �������
			table.setApplyDate("");		// ��������

		}else if(mode.equals("write_a")){
			table.setMakerCode("");
			table.setMakerName("");
			table.setMakerPartNo("");
			table.setPrjCode("");
			table.setPrjName("");
			table.setModelCode("");
			table.setModelName("");
		}

		return table;
	}


	/******************************************
	 * �ű� ��� �� ÷�������� ó���Ѵ�.
	 ******************************************/
	public CaMasterTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

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
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	} //getFile_frommulti()


	/******************************************
	 * ���� �� ÷�������� ó���Ѵ�.
	 ******************************************/
	public CaMasterTable getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			CaMasterTable file = new CaMasterTable();
			if(file_iter.hasNext()) file = (CaMasterTable)file_iter.next();

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
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}//getFile_frommulti()


	/**************************************************
	 * ÷�������� �ٿ�ε��ϱ� ���� ���� ����Ʈ ���
	 **************************************************/
	public CaMasterTable getFile_fordown(String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		Iterator file_iter = caDAO.getFile_list(fileno).iterator();

		String filename="",filetype="",filesize="",did="";
		int i = 1;
		while (file_iter.hasNext()){
			CaMasterTable file = (CaMasterTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
			}else{
			}
			i++;
		}
		CaMasterTable file = new CaMasterTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}


	/**************************************************
	 * ÷������ ������ DB�� �����ϱ� ���� ������ ����
	 **************************************************/
	public void updFile(String umask, String filename, String filetype, String filesize) throws Exception{
		String set = " SET fname='"+filename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
		String where = " WHERE umask = '" + umask + "'";

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		caDAO.updTable(set, where);
	}


	/*****************************************************************
	 * �������� �˻� ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String searchword, String searchscope, String category) throws Exception{

		String where = "", where_and = "", where_sea = "";

		if (searchword.length() > 0){
			if (searchscope.equals("approval_no")){			// ���ι�ȣ
				where_sea += "( approval_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("item_no")){		// ǰ���ȣ
				where_sea += "( item_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("maker_part_no")){	// ��ü��ǰ��ȣ
				where_sea += "( maker_part_no LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("maker_name")){		// ��ü�ڵ�
				where_sea += "( maker_name LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("approve_type")){	// ���������ڵ�
				where_sea += "( approve_type = '" +  searchword + "' )";
			}
			else if (searchscope.equals("approver_info")){	// ������ �̸�
				where_sea += "( approver_info LIKE '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("requestor_info")){	// �Ƿ��� �̸�
				where_sea += "( requestor_info LIKE '%" +  searchword + "%' )";
			}

			where = " WHERE " + where_sea + " and aid not in('EN','EE')";
		}
		else{
			where = " WHERE aid not in('EN','EE')";
		}

		if(mode.equals("list")) where += " and (approve_type in('A','B'))";

		return where;
	}

	/*****************************************************************
	 * �˻����� ���ڿ��� ���� �Ѿ���� ����� where ������ �����.
	 *****************************************************************/
	public String getWhere(String mode,String category,String where_str) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if(category.length() > 0) where_cat = "category like '"+category+"%' ";

		if (where_str.length() > 0){
			//where_str = "and|approval_no|E03-009,and|item_no|320200010, ..... ���·� �Ѿ��.

			StringTokenizer str = new StringTokenizer(where_str, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][3];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<3; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			for(int i = 0; i< scope_count-1; i++){
				if(search[i][1].equals("approve_date")){
					//s_day = 2003070120030830 ���� �Ѿ�´�. 2003-07-01 ~ 2003-08-30 ������ �ǹ���.

					String s_day = search[i][2].substring(0,8);		// ������ 
					String e_day = search[i][2].substring(8,16);	// ������
					where_sea += " (approve_date >= '" + s_day + "' and approve_date <= '" + e_day +"') " + search[i][0];
				}else{
					where_sea += " (" + search[i][1] + " like '%" + search[i][2] + "%') " + search[i][0];
				}
			}
			// ������ �˻��׸񿡴� and�� ������ �ʱ� ���� ���� ó����.
			if(search[scope_count-1][1].equals("approve_date")){
				String s_day = search[scope_count-1][2].substring(0,8);		// ������ 
				String e_day = search[scope_count-1][2].substring(8,16);	// ������
				where_sea += " (approve_date >= '" + s_day + "' and approve_date <= '" + e_day +"')";
			}else{
				where_sea += " (" + search[scope_count-1][1] + " like '%" + search[scope_count-1][2] + "%')";
			}
			where = " WHERE " + where_cat + where_sea;

			//���簡 �Ϸ�� ���Ǹ� ��Ÿ����
			where += " and aid not in('EN','EE')";
		}else{
			//���簡 �Ϸ�� ���Ǹ� ��Ÿ����
			where += " WHERE aid not in('EN','EE')";		
		}

		if(mode.equals("list")) where += " and (approve_type in('A','B'))";

		return where;
	}


	/*****************************************************************************************************
	 * �ӽý��ι�ȣ(no)�� ������ �������� ����Ʈ�� ������ ��, ������ ���Ľ��ι�ȣ�� ���������� �Է��Ѵ�.
	 * update �׸� : no_year,no_month,no_serial,approval_no,approver_id,approver_info,approve_date,aid
	 *****************************************************************************************************/
	public void updateApprovalAndSaveHistoryInfo(String write_type,String no,String approval_no,String pid) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		// 1.���ι�ȣ�� ���꽺Ʈ���Ͽ� �ʿ��� ���� ����
		String no_year = approval_no.substring(1,3);
		String no_month = approval_no.substring(3,5);
		String no_serial = approval_no.substring(6,9);

		// 2.approver_info ���̺��� pid == aid �� �ش��ϴ� ���ڵ� ������ �����´�.(����������)
		CaMasterTable table =  new CaMasterTable();
		table = caDAO.getAppInfoByPid(pid);

		String requestor_id = table.getRequestorId();
		String requestor_info = table.getRequestorInfo();
		String request_date = table.getRequestDate();

		String approver_id = table.getApproverId();
		String approver_info = table.getApproverInfo();

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String approve_date = vans.format(now);

		// 3.�ӽý��ι�ȣ(no)�� ���� ���������� mid ����Ʈ�� �����´�.
		Iterator mid_iter = caDAO.getMidListByTmpAppNo(no).iterator();

		// 4.������ ���ι����� ������ ������Ʈ�ϰ�, history_info ���̺� �̷��� �߰��Ѵ�.
		while(mid_iter.hasNext()){
			String mid = (String)mid_iter.next();
			
			//ca_master ���̺��� �������� ������Ʈ
			caDAO.updateApprovalInfo(mid,no_year,no_month,no_serial,approval_no,approver_id,approver_info,approve_date,pid);

			//ca_master ���̺��� mid �� �ش��ϴ� ���ڵ��� item_no ���� �����´�.
			String item_no = caDAO.getItemNoByMid(mid);

			//history_info ���̺� �̷� �߰�
			String contents = "";
			if(write_type.equals("report_w")){
				contents = "���ξ�ü ���(���ι�ȣ:" + approval_no + ",������:" + approver_info + ")";
			}else if(write_type.equals("report_r")){
				contents = "��纯�� ���(���ι�ȣ:" + approval_no + ",������:" + approver_info + ")";
			}
			caDAO.saveHistoryInfo(item_no,contents,approve_date,requestor_info);

			//��纯���� ��� ���� ���ι����� ��������� F�� ó���Ѵ�.
			if(write_type.equals("report_r")){
				String ancestor = caDAO.getAncestor(mid);
				caDAO.updateApproveType(ancestor,"F");
			}
			
	//		System.out.println(contents);
	//		System.out.println(approve_date);
	//		System.out.println(requestor_info);
		}
	}


	/*****************************************************************************************************
	 * ���õ� ������ȣ�� ������ ���ι����� ������ ó���Ѵ�.
	 *****************************************************************************************************/
	public void deleteApprovalAndSaveHistoryInfo(String no,String pid) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		// approver_info ���̺��� pid == aid �� �ش��ϴ� ���ڵ� ������ �����´�.(����������)
		CaMasterTable table =  new CaMasterTable();
		table = caDAO.getAppInfoByPid(pid);

		String requestor_id = table.getRequestorId();
		String requestor_info = table.getRequestorInfo();
		String request_date = table.getRequestDate();

		String approver_id = table.getApproverId();
		String approver_info = table.getApproverInfo();
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String approve_date = vans.format(now);

		//������� ������ ��������� F ó���Ѵ�.
		caDAO.updateApproveType(no,"F");
		
		//ca_master ���̺��� mid �� �ش��ϴ� ���ڵ��� item_no ���� �����´�.
		String item_no = caDAO.getItemNoByMid(no);

		//history_info ���̺� �̷� �߰�
		String contents = "���� ��ҿ� ���� ��ǰ������(������:" + approver_info + ")";
		caDAO.saveHistoryInfo(item_no,contents,approve_date,requestor_info);
	}

	/*****************************************************************************************************
	 * ���� ���ι�ȣ�� ����Ͽ� �����Ѵ�.
	 *****************************************************************************************************/
	public String getApprovalNo(String no) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		//���� �⵵ ���
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyMM");
		String now_year	= vans.format(now).substring(0,2);
		String now_month = vans.format(now).substring(2,4);

		//���ο� �����ڵ�(E or M)�� �����´�.
		String no_type = caDAO.getNoType(no);

		//�Ϸù�ȣ ���
		String no_serial = caDAO.getMaxSerialNo(now_year,no_type);

		//���� ���ι�ȣ ���
		String approval_no = no_type + now_year + now_month + "-" + no_serial;

		return approval_no;

	}

	/*****************************************************************************************************
	 * ���õ� �ӽý��ι�ȣ�� ������ ������ ����Ʈ�� �����´�. (���ڰ���� ȭ�� ����� ����)
	 *****************************************************************************************************/
	public ArrayList getApprovalList(String no) throws Exception{

		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);

		ArrayList approval_info = new ArrayList();
		// �ӽý��ι�ȣ(no)�� ���� ���������� mid ����Ʈ�� �����´�.
		Iterator mid_iter = caDAO.getMidListByTmpAppNo(no).iterator();

		// ������ ���ι����� ������ 
		while(mid_iter.hasNext()){
			String mid = (String)mid_iter.next();
			CaMasterTable table = new CaMasterTable();
			
			table = caDAO.getApprovalInfoByMid(mid);

			//÷������ ����Ʈ ��������
			String umask = table.getUmask();
			Iterator file_iter = caDAO.getFile_list(mid).iterator();

			int i = 1;
			String filelink = "&nbsp;";
			String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
			while(file_iter.hasNext()){
				CaMasterTable file = (CaMasterTable)file_iter.next();
				filelink += "<a href='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"&umask="+umask+"_"+i+"'";
				filelink += "onMouseOver=\"window.status='Download "+file.getFileName()+" ("+file.getFileSize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../ca/mimetype/" + com.anbtech.util.Module.getMIME(file.getFileName(), file.getFileType()) + ".gif' border=0> "+file.getFileName()+"</a>";
				filelink += " - "+file.getFileSize()+" bytes <br>&nbsp;";

				if (file.getFileType().indexOf("mage")>0){
					filepreview = filepreview + "<TR><TD><font size=1>"+file.getFileName()+"</font><br><img src='ComponentApprovalServlet?mode=download&no="+mid+"_"+i+"' border=1></TD></TR>";
				}
				i++;
			}
			filepreview = filepreview + "</TABLE>";
			table.setFileLink(filelink);
			table.setFilePreview(filepreview);

			approval_info.add(table);
		}

		return approval_info;
	}

	/*****************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ������ �����Ѵ�.
	 * mids = 34|24|45| �� ���·� �Ѿ�´�.
	 *****************************************************************************/
	public void dropApprovalInfos(String mids) throws Exception{
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		ArrayList mid_list = com.anbtech.util.Token.getTokenList(mids);

		int i = 0;
		while(i < mid_list.size()){
			String mid = (String)mid_list.get(i);
			caDAO.dropApprovalInfo(mid);
			i++;
		}
	}

	/*****************************************************************************
	 * ���õ� ������ȣ�� �ش��ϴ� ������ �ӽý��ι�ȣ�� ������Ʈ�Ѵ�.
	 * mid = 34|24|45| �� ���·� �Ѿ�´�.
	 *****************************************************************************/
	public void updateApprovalInfos(String mids,String tmp_approval_no) throws Exception{
		ComponentApprovalDAO caDAO = new ComponentApprovalDAO(con);
		ArrayList mid_list = com.anbtech.util.Token.getTokenList(mids);

		int i = 0;
		while(i < mid_list.size()){
			String mid = (String)mid_list.get(i);
			caDAO.updateTmpApprovalNo(mid,tmp_approval_no);
			i++;
		}
	}

}