package com.anbtech.am.business;

import com.anbtech.am.entity.*;
import com.anbtech.am.db.*;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.admin.entity.*;
import java.text.NumberFormat;
import com.anbtech.text.Hanguel;
import java.sql.*;
import java.util.*;
import java.io.*;

public class AssetModuleBO
{
	private Connection con;
	com.anbtech.am.db.AssetModuleDAO assetModuleDAO;			//
	com.anbtech.am.entity.AsCategoryTable asCategoryTable;		//help class

	public AssetModuleBO(Connection con){
		this.con = con;
		assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
	}

	//+++++ ���� ī�װ� ����,����,����
	public void setCtBusiness(String div,String c_no,String ct_id,String ct_level,String ct_parent,
		String ct_word,String ct_name,String dc_percent,String apply_dc) throws Exception
	{
		if(div.equals("f")){									// �ֻ��� ī�װ� ����
			ct_parent = "1";									// �ֻ��� ī�װ��� parent�� Root
			ct_level = "1";										// �ֻ��� ī�װ� level�� ������ "1"

			ct_id = assetModuleDAO.selectCtId(ct_parent,div);	// ���� �ֻ��� level�� ct_id���� ���� ū�� ��������
			int max_code=Integer.parseInt(ct_id);				// ���� �ֻ��� ī�װ� ct_id(max)�� ������ ��ȯ

			//--  ct_id ���� --------------
			if((max_code+1) < 10)	ct_id = "1" + (max_code+1);
			else ct_id = "" + (max_code+1);			

			assetModuleDAO.save_ct(ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent,  apply_dc); 
		} else if(div.equals("a")) {				// ���� ī�װ� ����
			String temp_id = ct_id;					// ���� ī�װ� ct_id ����
			ct_id = assetModuleDAO.selectCtId(c_no,div); 
			ct_parent = c_no;						// �߰��� ���� ī�װ� c_no�� �߰��� ���� ī�װ��� parent�� ���
			
			//-- ct_id ����  -----------
			if(ct_id.equals("0")) {					// ���� ī�װ��� ���� ī�װ��� �������(���� ī�װ��� ù��° ����ī�װ�����)
				ct_id =temp_id+"01";
			} else {
				ct_id =""+(Integer.parseInt(ct_id)+1);	// ���� ī�װ� ����
			}
		
			ct_level = ""+(Integer.parseInt(ct_level)+1);
			assetModuleDAO.save_ct(ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent, apply_dc);
		} else if(div.equals("m")) { // ī�װ� ����
			assetModuleDAO.update_ct( c_no, ct_word, ct_name, dc_percent, apply_dc);
		} 
		/*
		else if(div.equals("d")) { // ī�װ� ����		
			assetModuleDAO.delete_ct(c_no, ct_id);
		}
		*/
	}

	/******************************************************************
	* �ڻ� ��ȣ �����
	******************************************************************/
	public String getAsMid(String c_no) throws Exception 
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String regi_date 	= vans.format(now);
		regi_date = regi_date.substring(2,4);

		String ct_word = assetModuleDAO.getCtWord(c_no);
		String serial = assetModuleDAO.getAsTotalNum(c_no); // �߰��� ǰ���� serial ��������
		String as_mid = "SL-"+ct_word+regi_date+"-"+serial; // �ڻ� ��ȣ ���� ��) SL-XX03-0001
		//System.out.println(as_mid);
		return as_mid;
	}

	/******************************************
	 * ÷������ ���ε� ó��
	 ******************************************/
	public AsInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception
	{
		String filename = "";
		String filetype = "";
		String filesize = "";
		String filese ="";
		String fumask ="";
		String did = "";

		int i = 1;
		int j = 1; //ȭ�ϰ���
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
				fumask = fumask + (no+"_"+i)+"|";
				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
				j++;
			}
			i++;
		}
		com.anbtech.am.entity.AsInfoTable file = new com.anbtech.am.entity.AsInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFilePath(filepath);
		file.setFileSe(""+(j-1));
		file.setFileUmask(fumask);

		return file;
	} //getFile_frommulti()

	/******************************************
	 * ÷������ ���ε� ó�� (������)
	 ******************************************/
	public AsInfoTable getFile_frommulti(MultipartRequest multi, String no, String filepath, 
		ArrayList file_list) throws Exception
	{
		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String fileumask = "";
	
		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			AsInfoTable file = new AsInfoTable();
			if(file_iter.hasNext()) file = (AsInfoTable)file_iter.next();

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
//				fileumask = fileumask+(no+"_"+j)+"|";
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

//				fileumask = fileumask+(no+"_"+i)+"|";
				filename = filename + file.getFileName() + "|";
				filetype = filetype + file.getFileType() + "|";
				filesize = filesize + file.getFileSize() + "|";
				j++;
			}
			i++;
		}
		AsInfoTable file = new AsInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);
		file.setFilePath(filepath);
		file.setFileSe(""+(j-1));
//		file.setFileUmask(fileumask);

		return file;
	}//getFile_frommulti()


	/**************************************************
	 * ÷������ ������ DB�� �����ϱ� ���� ������ ����
	 **************************************************/
	public void updFile(String tablename, String no, String filename, String filetype, 
		String filesize,String filese,String fileumask,String filepath) throws Exception
	{
		String set = " SET file_name='"+filename+"',file_type='"+filetype+"',file_size='"+filesize+"', file_se='"+filese+"',file_umask='"+fileumask+"', file_path='"+filepath+"'";
		String where = " WHERE as_no="+no;
		assetModuleDAO.updTable(tablename, set, where);
	} // updFile()
	

	/**************************************************
	 * ÷������ �ٿ�ε�
	 **************************************************/
	public AsInfoTable getFile_fordown(String no) throws Exception
	{
		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));
		Iterator file_iter = assetModuleDAO.getFileList(fileno).iterator();

		String filename="",filetype="",filesize="";
		int i = 1;
		while (file_iter.hasNext()){
			AsInfoTable file = (AsInfoTable)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFileName();
				filetype = file.getFileType();
				filesize = file.getFileSize();
				//System.out.println(filename);
			}
			i++;
		}
		AsInfoTable file = new AsInfoTable();
		file.setFileName(filename);
		file.setFileType(filetype);
		file.setFileSize(filesize);

		return file;
	}

	/**************************************************
	 * ���� �ڵ庰 ���¸� ��������
	 **************************************************/
	 public String getStatname(String code) throws Exception 
	{	
			String stat_name = "";
			if(code.equals("1"))       stat_name = "�����";
			else if(code.equals("2"))  stat_name = "����������";
			else if(code.equals("3"))  stat_name = "1������Ϸ�";
			else if(code.equals("4"))  stat_name = "2������";
			else if(code.equals("5"))  stat_name = "2������Ϸ�";
			else if(code.equals("6"))  stat_name = "����";
			else if(code.equals("7"))  stat_name = "������";
			else if(code.equals("8"))  stat_name = "����";
			else if(code.equals("9"))  stat_name = "���";
			else if(code.equals("10")) stat_name = "���";
			else if(code.equals("11")) stat_name = "�̰�";
			else if(code.equals("12")) stat_name = "�ݳ�";
			else if(code.equals("13")) stat_name = "����";
			else if(code.equals("14")) stat_name = "�ݷ�";
			else if(code.equals("15")) stat_name = "������";
			else if(code.equals("16")) stat_name = "�뿩��";
			else if(code.equals("17")) stat_name = "�ݳ��Ϸ�";

			else if(code.equals("t"))  stat_name = "�̰�";
			else if(code.equals("o"))  stat_name = "����";
			else if(code.equals("l"))  stat_name = "�뿩";
		
			return stat_name;
	 }


	/**********************************************
	 * �˻��� WHERE ������� (user)
	 *********************************************/
	public String getWhere( String mode, String searchword, String searchscope,String as_no, 
		String div) throws Exception 
	{					
		String where = "";
		if("req_app_list".equals(mode)){
				where = " WHERE as_status='3' and type='2' and (o_status ='t' or o_status = 'o')";
		} else if("user_each_history".equals(mode)){
				where = " WHERE ((((o_status='t' and as_status='5') or (o_status='t' and as_status='3' and type='1') or (o_status='t' and as_status='5' and type='2')) or as_status='11' or as_status='9' or as_status='13')  and as_no='"+as_no+"') or (((o_status='o' and (as_status='5' or as_status='7')) or as_status='12')  and as_no='"+as_no+"') or (((o_status='l' and as_status='5') or as_status='16' or as_status='17') and as_no='"+as_no+"')";
		}  else if("EnteringList".equals(mode) && "lending".equals(div)) {	// �뿩 �ݳ� ��� ������
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and  as_status='16' and m_rank='"+searchword+"') ";
			} else {
				where  = " WHERE (o_status='l' and as_status ='16' and m_rank='���Ѿ���') "; // ���Ѿ����� �˻� ������ ���Բ� ����
			}
		} else if("TransOutList".equals(mode) && "lending".equals(div)) {   // �뿩 ��� ������
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and as_status='5' and m_rank='"+searchword+"') "; 
			} else {
				where  = " WHERE (o_status='l' and as_status ='5' and m_rank='���Ѿ���') ";// ���Ѿ����� �˻� ������ ���Բ� ����
			}
		} else if("lending_list".equals(mode) && "lending".equals(div)) {   // �뿩 ���� ��� ������
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and as_status ='3' and m_rank='"+searchword+"') ";
			} else {
				where  = " WHERE (o_status='l' and as_status ='3' and m_rank='���Ѿ���') ";// ���Ѿ����� �˻� ������ ���Բ� ����
			}
		} else if("EnteringList".equals(mode)){ // �԰� ó�� ��� 
				where = " WHERE o_status='o' and as_status = '7'";
		} else if("TransOutList".equals(mode)){ // �̰�/���� ���
				where = " WHERE ( o_status='o' and as_status ='5' ) or ( o_status='t' and as_status='3' and type='1') or (o_status='t' and as_status='5' and type='2') ";
		} else if("asset_del_list".equals(mode)){
				where =" WHERE as_status='10' ";
		}
		//System.out.println(where);
		return where;
	}

	/**********************************************
	 * �˻��� WHERE ������� (������)
	 *********************************************/
	public String getWhere2(String c_no, String searchword, String searchscope,String ct_id,String div) throws Exception 
	{
		String where = "";

		if(c_no.equals("0")) c_no="";
		if(ct_id.equals("0")) ct_id="";
		
		if(c_no.equals("")  && searchscope.equals("") && searchword.equals("")) {
				where = " WHERE as_status != '10' ";
		} else  if( !c_no.equals("")  && !searchscope.equals("") && !searchword.equals("")){		
				where = " WHERE (ct_id like '"+ct_id+"%') and ("+searchscope+" like '%"+searchword+"%') and (as_status != '10')";		
		} else  if( c_no.equals("")  && !searchscope.equals("") && !searchword.equals("") ) {				
				where = " WHERE ( "+searchscope+" like '%"+searchword+"%') and as_status != '10' ";	
		} else  if( !c_no.equals("") && searchscope.equals("") && searchword.equals("")){			
				where = " WHERE ( ct_id like '"+ct_id+"%') and as_status != '10'";
		} 
		//System.out.println(where);
		return where;
	}

	/**********************************************
	 * �˻��� WHERE ������� (������) - ��� �ڻ�
	 *********************************************/
	public String getWhere4(String c_no, String searchword, String searchscope,String ct_id) throws Exception 
	{	
		String where = "";


		if(c_no.equals("0")) c_no="";
		if(ct_id.equals("0")) ct_id="";
		
		if(c_no.equals("")  && searchscope.equals("") && searchword.equals("")) {
				where = " WHERE as_status = '10' ";
		} else  if( !c_no.equals("")  && !searchscope.equals("") && !searchword.equals("")){				
				where = " WHERE (ct_id like '"+ct_id+"%') and ("+searchscope+" like '%"+searchword+"%') and (as_status = '10')";		
		} else  if( c_no.equals("")  && !searchscope.equals("") && !searchword.equals("") ) {				
				where = " WHERE ( "+searchscope+" like '%"+searchword+"%') and as_status = '10' ";	
		} else  if( !c_no.equals("") && searchscope.equals("") && searchword.equals("")){				
				where = " WHERE ( ct_id like '"+ct_id+"%') and as_status = '10'";
		} 

		//System.out.println(where);
		return where;
	}

	
	/***********************************************************************************
	 * �˻����� ���ڿ��� ���� �Ѿ���� ����� where ������ �����.(�ڻ� ����Ʈ �󼼰˻��� where��)
	 **********************************************************************************/
	public String getWhere3(String c_no, String mode,String where_str,String category,
		String ct_id,String div) throws Exception
	{
		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
		if(c_no.equals("0") || c_no.equals("")) where_cat = " as_status != '10' ";
		else   where_cat = " ct_id like '"+ct_id+"%' and as_status != '10' ";
        
		// ��� �ڻ� list 
		if("asset_del_list".equals(mode)) { where_cat = " as_status = '10' "; }

		if (where_str.length() > 0){
			//where_str = "and|subject|����,or|writer|�ۼ���, ..... ���·� �Ѿ��.

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
			for(int i = 0; i< scope_count; i++){
				if(search[i][1].equals("model_name")){
				   where_sea += search[i][0] + " ( model_name like '%" + search[i][2]+"%' )";
				}
				else if(search[i][1].equals("s_date") ){
				   where_sea += search[i][0] + " ( buy_date >='"+search[i][2] +"' and buy_date <= '"+ search[i+1][2]+"') ";
				}				
				else if(search[i][1].equals("as_value1")){
				   where_sea += search[i][0] + " ( as_value >='"+search[i][2] +"' ) and ( as_value <= '"+ search[i+1][2]+"') ";
				}
				else if(search[i][1].equals("crr_name")){
				   where_sea += search[i][0] + " ( crr_name like '%" + search[i][2]+"%' )";
				}
				else if(search[i][1].equals("crr_rank")){
				   where_sea += search[i][0] + " ( crr_rank like '%" + search[i][2]+"%' )";
				}
			}
		}					
		where = " WHERE " + where_cat + where_sea;// + " AND as_status !='2'";
		////System.out.println(where);
		return where;
	}

	/*****************************************************************
	 * ���� ���� ���� �˾ƺ���..
	 * 
	 *****************************************************************/
	public String checkOut(String start, String end, String as_no, String o_status) throws Exception
	{
		boolean bl=true;
		String msg	= "";
		
		String temp	= "";
		/*	   temp	= assetModuleDAO.getOstatus(as_no);*/
		
		if(o_status.equals("l"))		{ temp = "�뿩";	 }
		else if(o_status.equals("t"))	{ temp = "�̰�";	 }
		else if(o_status.equals("o"))	{ temp = "����";	 }


		// ���۳�¥�� ���ᳯ¥�� ����� ���� �ʾ������
		if((Double.parseDouble(end) - Double.parseDouble(start)) < 0) {
			bl = false;
			msg = "��¥�� �ùٷ� ������ �ֽʽÿ�";
		} else {

			ArrayList table_list = new ArrayList();
			table_list = (ArrayList)assetModuleDAO.getSavedate(as_no);;
			int i = table_list.size();

			String arr[]=new String[i];

			Iterator table_iter = table_list.iterator();
			String startdate=start,enddate=end;
		
			int n=0;
			while(table_iter.hasNext()){
					arr[n]=(String)table_iter.next();
					n+=1;
			}

			for(int k=0;k<i;k+=2){
				double arr_start = Double.parseDouble(arr[k]);
				double arr_end = Double.parseDouble(arr[k+1]);
				if(((arr_start <= Double.parseDouble(startdate) &&  Double.parseDouble(startdate) <= arr_end)) || ((arr_start <= Double.parseDouble(enddate)) && ( Double.parseDouble(enddate) <= arr_end)) ) {
					bl=false;					
					break;
				} 
			} 
			msg = "enable";
			//--> end_for(int k=0;k<i;k+=2){
		
			if(!bl) {//throw new Exception("<script> alert('������ �� �����ϴ�. ���೯¥�� �ߺ��Ǿ����ϴ�.');history.go(-1);</script>");
				 msg = temp+"��û �� �� �����ϴ�. ��¥�� �ߺ��Ǿ����ϴ�";
			} 
		}
		return msg;
	}
		

	/****************************************************************
	*   �̰� ��û ���� ���� Ȯ��										*
	*	1. ��û���ڰ� ���� ���� ���� Ȯ�� ���� ���� ��¥��, �̰� �Ұ�		*
	*	2. ���� �ڻ� ���� Ȯ��											*
	*	3. �ߺ��� ��¥�� �ִ��� Ȯ��									*
	****************************************************************/
	public String transEnable(String udate, String as_no, String o_status) throws Exception{
		
		com.anbtech.am.db.AssetModuleDAO asDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		// ���� ��¥
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String crr_date		= anbdt.getDateNoformat();	
		boolean bool		= true;

		String msg = "";
		
		// 1. ��û���ڰ� ���� ���� ���� Ȯ��
		//    ���� ���� ��¥��, �̰� �Ұ�
		if ( Integer.parseInt(udate) > Integer.parseInt(crr_date) )	{
			bool = false;
			msg = "���� ó�����ڰ� �ƴմϴ�.";
		}

		if(bool) {
			// 2. ���� �ڻ� ���� Ȯ��(�������̰ų�..�뿩..�Ǵ� ���� ��� Ȯ��)
			String state_temp	= asDAO.getInfoStatus(as_no);	// ���� �ڻ� ���� ��������
			int state_int		= Integer.parseInt(state_temp);
			switch(state_int) {
				case 7  : msg = "���� �������Դϴ�. �԰���� �ʾҽ��ϴ�.";	// ������
						  bool = false;
						  break;
				case 10 : msg = "���� ���� �ڿ��Դϴ�.";					// ���
						  bool = false;
						  break;
				case 13 : msg = "���� �����԰���� �Դϴ�.";				// ����
						  bool = false;
						  break;
				case 16 : msg = "���� �뿩���Դϴ�. �ݳ����� �ʾҽ��ϴ�.";	// �뿩��
						  bool = false;
						  break;
			}
		}

		if (bool) {
			// 3. �ߺ��� ��¥�� �ִ��� Ȯ��
			msg = checkOut(udate, udate, as_no, o_status);
		}
	
		return msg;
	}
	

	/****************************************************************
	*  �ڿ� ���� Ȯ�� (�̰� ��û�� ��û�� ���� ���� ������ ����� �Ѵ�.)
	*  *�̿�
	****************************************************************/
	public boolean checkTranse(String udate, String as_no) throws Exception {

		com.anbtech.am.db.AssetModuleDAO  asDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		boolean bool = true;

		// ��������� �ִ��� Ȯ��
		String booking = asDAO.bookingRecord(udate,as_no);
		// 2. ���� �뿩/���� ������ Ȯ��
		return bool;
	}


	//***********************************************************
	//	user ����												*
	//***********************************************************
	public String getUserInfo(String str) throws Exception 
	{
		String user_id="";
		String user_name="";
	
		// ---- ���� str( id/�̸� ) �и� --------
		java.util.StringTokenizer token = new java.util.StringTokenizer(str,"/");
		int temp=token.countTokens();
		while(token.hasMoreTokens()){
			user_id=token.nextToken();
			user_name=token.nextToken();
		}
		return user_id;
	}

	//********************************************************************
	//	 [  �ڻ� ���� ó��  ] 										
	//	 #. �� Base�� ó��  
	//	 #. ���� ���� ���ڿ� ���� ���� ���� ( admin/am/measurePopup.jsp )
	//********************************************************************
	public void getAutoUpdate(String year, String month, String value) throws Exception 
	{
		com.anbtech.am.entity.AsInfoTable asInfoTable;
		
		ArrayList arrylist = new ArrayList();
		ArrayList savelist = new ArrayList();
		
		// ��������
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String crr_time = vans.format(now);

		// ���� ����
		int year_in = Integer.parseInt(year);
		int month_in = Integer.parseInt(month);
		int ym = Integer.parseInt(year+month);
		DecimalFormat fmt = new DecimalFormat("00");
		month = (String)fmt.format(month_in);	
		String ymd = year+month+"01";

		int value_int = Integer.parseInt(value);
		long as_result=0;

		// ���� ���� ����Ʈ �������� (���ó�� �ڻ� ����)
		arrylist = assetModuleDAO.getAssetValueList();
		Iterator table_iter = arrylist.iterator();
		
		while(table_iter.hasNext()){
			asInfoTable = (com.anbtech.am.entity.AsInfoTable)table_iter.next();
			int as_no=asInfoTable.getAsNo();								// �ڻ������ȣ

			String  buy_date=asInfoTable.getBuyDate();						// ���Գ����
			long  as_price  = Long.parseLong(asInfoTable.getAsPrice());		// ���԰���
			int   dc_count  = Integer.parseInt(asInfoTable.getDcCount());	// ����Ƚ��
			int   as_each_dc= Integer.parseInt(asInfoTable.getAsEachDc());	// �����������
			long  as_value  = Long.parseLong(asInfoTable.getAsValue());		// ���� ���� ���� 
			
			String   apply_dcdate = asInfoTable.getApplyDcDate();
			
			int count = (int)((year_in - Integer.parseInt(buy_date.substring(0,4))));  // ���س⵵ - ���Գ⵵ => ����Ƚ�� 
				  
			int buy_date_int  = Integer.parseInt(buy_date.substring(0,4));      // ���Գ�
			int buy_date_month = Integer.parseInt(buy_date.substring(4,6));		//  ���Կ�
			
			// #. ���� ��� idea
			//	  1. �켱 ���� ������ �ڻ��߿� ���� ���� ������ �̷���� �ڻ��� ����
			//    2. �⺻������ ������ ���� ���� ��������
			//    3. ������ 1���� ������, �����̰� 1���� �ȵǴ°��� ����

			//if( (Long.parseLong(apply_dcdate) < Long.parseLong(crr_time)) && (as_value<=Long.parseLong(value)) && (as_value != 0))  { //--(1)
			    //--------------------> ���縦 �������� ���� �ڻ��..  <---| |--> ���� ���� ������ �޾Ҵ��� �Ǵ� <---------------|
			//} else {		
				if( count == 1 ){ // --- (2)
					if( (month_in - buy_date_month) > 0 ) {  // --- (3)						
						// ��������
						as_result=(long)(as_price*as_each_dc*0.01);
						as_value = as_price - as_result;
						// ���� ���� �ݾ׺��� ������ ���� ���� �ݾ����� setting 
						if(as_value < value_int) as_value =  value_int;
					} else {
						as_value = as_price;
					}		
				} else if( count >= 2 ) {						
						// ���� ����
						as_result=(long)(as_price*as_each_dc*0.01);
						as_value = as_price - (as_result*count);
						if(as_value < value_int) as_value =  value_int;
	
				} else if( count == 0) {						
						as_value = as_price;				

				} else if( count < 0) {				
						as_value = 0;
				
				} 					
				assetModuleDAO.valueUdate(as_no,""+dc_count,""+as_value,ymd ); // �������� UPDATE
			//}   //**** if( apply_dcdate < crr_time && (as_value<=(long)value ))
		}	
	}

	/**************************************************
	/* ���� ó�� [���� �Ϸ�]						  *
	/* pid:���ڰ��������ȣ h_no:�ڻ��̷°�����ȣ "reject","1"
	/*************************************************/
	public void asAppInfoProcess(String pid, String h_no, String mode, String app) throws Exception 
	{			
		String status="";
		//System.out.println("pid :"+ pid+"  h_no : "+ h_no+" mode"+mode+"  app:"+app);
			
		//���� ���ν�
		if("approval1".equals(mode)) {
			// app ==> 1�� or 2�� 
			// 1�� �̸� status = "3", 2���� status = "5"
			if(app.equals("1")) {
				status = "3";
			} else {
				status = "5";
			}			
			assetModuleDAO.getAppinfoAndSave(pid);					// ������ aid�� ���� ���� �ҷ����� �����ϱ�
			assetModuleDAO.setAid(pid,h_no,app);					// as_history table�� ������ȣ(h_no)�� aid�� setting
			assetModuleDAO.updateStatus(h_no,status,"","");			// �̰�/���⿡ ���� ���� ó��
		
			String decision = assetModuleDAO.getDecision(h_no);		// �ۼ��� ID��������
			String type		= assetModuleDAO.getType(pid);			// ó���Ϸ� ���� Ƚ��(1���� Ÿ����.. 2������ Ÿ����..)
					
			if(app.equals("1")) {	
				if(type.equals("1")) sendMail("",h_no,decision,"y");
			} else {
				if(type.equals("2")) sendMail("",h_no,decision,"y");
			}
		} 		
		//���� �ݷ���
		else if("reject".equals(mode)){
			status = "14"; // �ݷ� ó���ڵ�		
			assetModuleDAO.getAppinfoAndSave(pid);				// ������ aid�� ���� ���� �ҷ����� �����ϱ�
			assetModuleDAO.setAid(pid,h_no,app);				// as_history table�� ������ȣ(h_no)�� aid�� setting
			assetModuleDAO.updateStatus(h_no,status,"","");		// �̰�/���⿡ ���� �ݷ�ó��
			
			String decision2 = assetModuleDAO.getDecision(h_no); // ���� ó���� ID��������
			
			sendMail("",h_no,decision2,"n");
		}
		//���� ��Ž�
		else if("submit".equals(mode)){
			// app ==> 1�� or 2�� 
			// 1�� �̸� status = "2", 2���� status = "4" ���
			if(app.equals("1")) {	//1�� ��Ž�
				status = "2";
				assetModuleDAO.updateStatus(h_no,status,"pid",pid);		  // ������� update
			} else {				//2�� ��Ž�
				status = "4";
				assetModuleDAO.updateStatus(h_no,status,"pid2",pid);		// ������� update
			}
		}
	}
	
	/********************************************************
	/ �� String���� �����(����) 
	/********************************************************/
	public String makeWon(String won) throws Exception 
	{
		int len = won.length();
		int len_temp =len;
		String str="";
		int i=0;
		int k=0;
		int bound=2;

		String arr[] = new String[len+10];
		
		while(len_temp>0){  // ���� String�� �� ����(����)�� �迭�� ����			
			arr[k]=won.substring(len_temp-1,len_temp);   // �迭�� String�� ó�� ����(����)���� ����
			k++;										 // �迭 index����
		
			// �迭�� ������(",") ����
			if( i==bound ) {							 
				arr[k]=",";
				k++;
				bound+=3;
			}
			i++;		// String�� ���� sequence count
			len_temp--; // String�� index ����
		}

		// �迭�� ����� ����(����)�� �ҷ��´�.
		for( int j=k; j>0; j-- )
		{	
			str+=arr[j-1];
		}		
		if(str.charAt(0)==',') str=str.substring(1);
		return str;
	}

	/***********************************/
	/*   won���� ',' ����				/
	/***********************************/
	public String getStringWon(String str){
		
		java.util.StringTokenizer st = new java.util.StringTokenizer(str,",");
		String stringWon = "";
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			stringWon+=token;
		}
		return stringWon;
	}

	////////////// MAIL Sending Process ////////////////
	
	/*****************************************************************
	 * �뿩/����/�̰� ó���� �� ��� �ȳ� �޽����� ÷�������� �ٿ�ε� �� �� �ִ�
	 * ��ũ ���ڿ��� �����Ͽ� ���ڿ������� �߼��Ѵ�.
	 *****************************************************************/
	public void sendMail(String tablename,String h_no,String login_id,String is_allowed) throws Exception{
	//											
		//1.���� HTML ����
		StringBuffer sb = new StringBuffer();

		if(is_allowed.equals("y")) {

			 sb = makeMailContentsForCommit(tablename,h_no);
		} else { 

			if(is_allowed.equals("n")) sb = makeMailContentsForReject(tablename,h_no);
		}

		//2.���� ���� ����
		String filename	= "DOC" + System.currentTimeMillis() + ".html";
		String bon_path = "/email/" + login_id + "/text/";
		String full_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/gw/mail" + bon_path;

		makeContentsFile(sb,full_path,filename);

		//2. �뿩/����/�̰� �ۼ��� ID �˾ƿ���(h_no)
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		String requestor = assetModuleDAO.getWid(h_no);

		//3.���ϳ����� ����
	
		assetModuleDAO.saveEmail(login_id,requestor,bon_path,filename);
	
	}

	/*****************************************************************
	 * ������ ����Ҷ��� ���Ϻ��� ���� ���ڿ��� �����.
	 *****************************************************************/
	public StringBuffer makeMailContentsForCommit(String tablename,String h_no) throws Exception{
		
		//���� ��û ���� ��������
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		
		String as_no = assetModuleDAO.getAsNo(h_no);

		asHistoryTable	= assetModuleDAO.getHistory(h_no);
		asInfoTable		= assetModuleDAO.getInfo(as_no);

		String as_mid	= asInfoTable.getAsMid();		// �ڻ��ȣ
		String as_name	= asInfoTable.getAsName();		// �ڻ� ǰ��
		String model_name = asInfoTable.getModelName();	//	�ڻ� �𵨸�
		String crr_name = asInfoTable.getCrrName();		// �ش� �ڻ� ������

		String w_id		= asHistoryTable.getWid();			//  ����� ���ID
		String w_name	= asHistoryTable.getWname();		//	����� ���ID/�̸�
		String write_date	= asHistoryTable.getWriteDate();	//	�������
		String u_date	= asHistoryTable.getUdate();			//	����� 
		String in_date	= asHistoryTable.getInDate();			
		String type		= asHistoryTable.getType();				// 
		String o_status	= asHistoryTable.getOstatus();
		String as_status= asHistoryTable.getAsStatus();
		//String pid		= asHistoryTable.getPid();
		//String pid2		= asHistoryTable.getPid2();
		String o_status_name	= asHistoryTable.getOstatusName();
		String as_status_name	= asHistoryTable.getAsStatusName();


		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>");
		sb.append("<HEAD>");
		sb.append("<TITLE>"+o_status_name+" ó�� ���</TITLE>");
		sb.append("<STYLE TYPE='text/css'>");
		sb.append("<!--");
		sb.append(".bt {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:black}");
		sb.append(".del {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:#404040}");
		sb.append("A:link    {color:black;text-decoration:nBoard_env;}");
		sb.append("A:visited {color:black;text-decoration:nBoard_env;}");
		sb.append("A:active  {color:#007BEA;text-decoration:nBoard_env;}");
		sb.append("A:hover  {color:#0A99D4;text-decoration:underline}");
		sb.append(".sel {background-color:black;font-family:����ü;color:white}");
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
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>"+o_status_name+"ó���� �Ϸ�Ǿ����ϴ�.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>�ڻ��ȣ:</td><td width='80%'>" + as_mid + "</TD></TR>");
		sb.append("<TR><TD width='20%'>ǰ��:</td><td width='80%'>" + as_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�𵨸�:</td><td width='80%'>" + model_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�ۼ���:</td><td width='80%'>" + w_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�ۼ���:</td><td width='80%'>" + write_date + "</TD></TR>");
		sb.append("<TR><TD width='20%'>��û����:</td><td width='80%'>" + o_status_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>ó�����:</td><td width='80%'> ["+o_status_name+" ó���� �Ϸ� �Ǿ����ϴ�.] </TD></TR>");
		sb.append("</TABLE>");
		sb.append("</TD></TR></TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb;
		
	}

	/*****************************************************************
	 * ������ �ݷ��Ҷ��� ���Ϻ��� ���� ���ڿ��� �����.
	 *****************************************************************/
	public StringBuffer makeMailContentsForReject(String tablename,String h_no) throws Exception{
		
			//���� ��û ���� ��������
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		
		String as_no = assetModuleDAO.getAsNo(h_no);

		asHistoryTable	= assetModuleDAO.getHistory(h_no);
		asInfoTable		= assetModuleDAO.getInfo(as_no);

		String as_mid	= asInfoTable.getAsMid();		// �ڻ��ȣ
		String as_name	= asInfoTable.getAsName();		// �ڻ� ǰ��
		String model_name = asInfoTable.getModelName();	//	�ڻ� �𵨸�
		String crr_name = asInfoTable.getCrrName();		// �ش� �ڻ� ������

		String w_id		= asHistoryTable.getWid();
		String w_name	= asHistoryTable.getWname();
		String write_date = asHistoryTable.getWriteDate();
		String u_date	= asHistoryTable.getUdate();
		String in_date	= asHistoryTable.getInDate();
		String type		= asHistoryTable.getType();
		String o_status	= asHistoryTable.getOstatus();
		String as_status= asHistoryTable.getAsStatus();
		//String pid		= asHistoryTable.getPid();
		//String pid2		= asHistoryTable.getPid2();
		String o_status_name	= asHistoryTable.getOstatusName();
		String as_status_name	= asHistoryTable.getAsStatusName();

		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>");
		sb.append("<HEAD>");
		sb.append("<TITLE>"+o_status_name+" ó�� ���</TITLE>");
		sb.append("<STYLE TYPE='text/css'>");
		sb.append("<!--");
		sb.append(".bt {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:black}");
		sb.append(".del {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:#404040}");
		sb.append("A:link    {color:black;text-decoration:nBoard_env;}");
		sb.append("A:visited {color:black;text-decoration:nBoard_env;}");
		sb.append("A:active  {color:#007BEA;text-decoration:nBoard_env;}");
		sb.append("A:hover  {color:#0A99D4;text-decoration:underline}");
		sb.append(".sel {background-color:black;font-family:����ü;color:white}");
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
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>"+o_status_name+" �ݷ�ó�� �Ǿ����ϴ�.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>�ڻ��ȣ:</td><td width='80%'>" + as_mid + "</TD></TR>");
		sb.append("<TR><TD width='20%'>ǰ��:</td><td width='80%'>" + as_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�𵨸�:</td><td width='80%'>" + model_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�ۼ���:</td><td width='80%'>" + w_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>�ۼ���:</td><td width='80%'>" + write_date + "</TD></TR>");
		sb.append("<TR><TD width='20%'>��û����:</td><td width='80%'>" + o_status_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>ó�����:</td><td width='80%'> ["+o_status_name+" �ݷ� ó���� �Ǿ����ϴ�.] </TD></TR>");
		sb.append("</TABLE>");
		sb.append("</TD></TR></TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb;
		
	}

	/*****************************************************************
	 * ������ ���Ϻ��� ���� ���ڿ��� ���Ϸ� ����.
	 *****************************************************************/
	public void makeContentsFile(StringBuffer sb,String full_path,String filename) throws Exception{
		File fTargetDir = new File(full_path);
	    if(!fTargetDir.exists()) fTargetDir.mkdirs();
		BufferedWriter bw = new BufferedWriter(new FileWriter(full_path + filename));

		bw.write(""+sb);
		bw.close();
	}

	// "-" ���ֱ�
	public String delHyphen(String str) throws Exception {
		String temp = "";
		if(str.length()>8) temp = str.substring(0,4)+str.substring(5,7)+str.substring(8,10);
		else temp = str;
		//System.out.println(temp);
	return temp;
	}
}