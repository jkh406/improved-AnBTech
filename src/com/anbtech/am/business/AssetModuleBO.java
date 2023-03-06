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

	//+++++ 현재 카테고리 생성,삭제,수정
	public void setCtBusiness(String div,String c_no,String ct_id,String ct_level,String ct_parent,
		String ct_word,String ct_name,String dc_percent,String apply_dc) throws Exception
	{
		if(div.equals("f")){									// 최상위 카테고리 생성
			ct_parent = "1";									// 최상위 카테고리의 parent는 Root
			ct_level = "1";										// 최상위 카테고리 level은 무조건 "1"

			ct_id = assetModuleDAO.selectCtId(ct_parent,div);	// 현재 최상위 level중 ct_id값이 가장 큰것 가져오기
			int max_code=Integer.parseInt(ct_id);				// 현재 최상위 카테고리 ct_id(max)값 정수로 변환

			//--  ct_id 생성 --------------
			if((max_code+1) < 10)	ct_id = "1" + (max_code+1);
			else ct_id = "" + (max_code+1);			

			assetModuleDAO.save_ct(ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent,  apply_dc); 
		} else if(div.equals("a")) {				// 하위 카테고리 생성
			String temp_id = ct_id;					// 현재 카테고리 ct_id 저장
			ct_id = assetModuleDAO.selectCtId(c_no,div); 
			ct_parent = c_no;						// 추가전 상위 카테고리 c_no를 추가될 하위 카테고리의 parent로 등록
			
			//-- ct_id 생성  -----------
			if(ct_id.equals("0")) {					// 현재 카테고리의 하위 카테고리가 없을경우(현재 카테고리의 첫번째 하위카테고리생성)
				ct_id =temp_id+"01";
			} else {
				ct_id =""+(Integer.parseInt(ct_id)+1);	// 하위 카테고리 생성
			}
		
			ct_level = ""+(Integer.parseInt(ct_level)+1);
			assetModuleDAO.save_ct(ct_id, ct_level, ct_parent, ct_word, ct_name, dc_percent, apply_dc);
		} else if(div.equals("m")) { // 카테고리 수정
			assetModuleDAO.update_ct( c_no, ct_word, ct_name, dc_percent, apply_dc);
		} 
		/*
		else if(div.equals("d")) { // 카테고리 삭제		
			assetModuleDAO.delete_ct(c_no, ct_id);
		}
		*/
	}

	/******************************************************************
	* 자산 번호 만들기
	******************************************************************/
	public String getAsMid(String c_no) throws Exception 
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String regi_date 	= vans.format(now);
		regi_date = regi_date.substring(2,4);

		String ct_word = assetModuleDAO.getCtWord(c_no);
		String serial = assetModuleDAO.getAsTotalNum(c_no); // 추가할 품목의 serial 가져오기
		String as_mid = "SL-"+ct_word+regi_date+"-"+serial; // 자산 번호 생성 예) SL-XX03-0001
		//System.out.println(as_mid);
		return as_mid;
	}

	/******************************************
	 * 첨부파일 업로딩 처리
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
		int j = 1; //화일갯수
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
	 * 첨부파일 업로딩 처리 (수정시)
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
	 * 첨부파일 정보를 DB에 저장하기 위한 쿼리문 생성
	 **************************************************/
	public void updFile(String tablename, String no, String filename, String filetype, 
		String filesize,String filese,String fileumask,String filepath) throws Exception
	{
		String set = " SET file_name='"+filename+"',file_type='"+filetype+"',file_size='"+filesize+"', file_se='"+filese+"',file_umask='"+fileumask+"', file_path='"+filepath+"'";
		String where = " WHERE as_no="+no;
		assetModuleDAO.updTable(tablename, set, where);
	} // updFile()
	

	/**************************************************
	 * 첨부파일 다운로드
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
	 * 상태 코드별 상태명 가져오기
	 **************************************************/
	 public String getStatname(String code) throws Exception 
	{	
			String stat_name = "";
			if(code.equals("1"))       stat_name = "상신전";
			else if(code.equals("2"))  stat_name = "결재진행중";
			else if(code.equals("3"))  stat_name = "1차결재완료";
			else if(code.equals("4"))  stat_name = "2차결중";
			else if(code.equals("5"))  stat_name = "2차결재완료";
			else if(code.equals("6"))  stat_name = "정상";
			else if(code.equals("7"))  stat_name = "반출중";
			else if(code.equals("8"))  stat_name = "연기";
			else if(code.equals("9"))  stat_name = "취소";
			else if(code.equals("10")) stat_name = "폐기";
			else if(code.equals("11")) stat_name = "이관";
			else if(code.equals("12")) stat_name = "반납";
			else if(code.equals("13")) stat_name = "수리";
			else if(code.equals("14")) stat_name = "반려";
			else if(code.equals("15")) stat_name = "상신취소";
			else if(code.equals("16")) stat_name = "대여중";
			else if(code.equals("17")) stat_name = "반납완료";

			else if(code.equals("t"))  stat_name = "이관";
			else if(code.equals("o"))  stat_name = "반출";
			else if(code.equals("l"))  stat_name = "대여";
		
			return stat_name;
	 }


	/**********************************************
	 * 검색시 WHERE 절만들기 (user)
	 *********************************************/
	public String getWhere( String mode, String searchword, String searchscope,String as_no, 
		String div) throws Exception 
	{					
		String where = "";
		if("req_app_list".equals(mode)){
				where = " WHERE as_status='3' and type='2' and (o_status ='t' or o_status = 'o')";
		} else if("user_each_history".equals(mode)){
				where = " WHERE ((((o_status='t' and as_status='5') or (o_status='t' and as_status='3' and type='1') or (o_status='t' and as_status='5' and type='2')) or as_status='11' or as_status='9' or as_status='13')  and as_no='"+as_no+"') or (((o_status='o' and (as_status='5' or as_status='7')) or as_status='12')  and as_no='"+as_no+"') or (((o_status='l' and as_status='5') or as_status='16' or as_status='17') and as_no='"+as_no+"')";
		}  else if("EnteringList".equals(mode) && "lending".equals(div)) {	// 대여 반납 대상 조건절
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and  as_status='16' and m_rank='"+searchword+"') ";
			} else {
				where  = " WHERE (o_status='l' and as_status ='16' and m_rank='권한없음') "; // 권한없을시 검색 내용이 없게끔 구현
			}
		} else if("TransOutList".equals(mode) && "lending".equals(div)) {   // 대여 대상 조건절
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and as_status='5' and m_rank='"+searchword+"') "; 
			} else {
				where  = " WHERE (o_status='l' and as_status ='5' and m_rank='권한없음') ";// 권한없을시 검색 내용이 없게끔 구현
			}
		} else if("lending_list".equals(mode) && "lending".equals(div)) {   // 대여 결재 대상 조건절
			if(searchscope.equals("1")) {
				where  = " WHERE (o_status='l' and as_status ='3' and m_rank='"+searchword+"') ";
			} else {
				where  = " WHERE (o_status='l' and as_status ='3' and m_rank='권한없음') ";// 권한없을시 검색 내용이 없게끔 구현
			}
		} else if("EnteringList".equals(mode)){ // 입고 처리 대상 
				where = " WHERE o_status='o' and as_status = '7'";
		} else if("TransOutList".equals(mode)){ // 이관/반출 대상
				where = " WHERE ( o_status='o' and as_status ='5' ) or ( o_status='t' and as_status='3' and type='1') or (o_status='t' and as_status='5' and type='2') ";
		} else if("asset_del_list".equals(mode)){
				where =" WHERE as_status='10' ";
		}
		//System.out.println(where);
		return where;
	}

	/**********************************************
	 * 검색시 WHERE 절만들기 (관리자)
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
	 * 검색시 WHERE 절만들기 (관리자) - 폐기 자산
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
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.(자산 리스트 상세검색시 where절)
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
        
		// 폐기 자산 list 
		if("asset_del_list".equals(mode)) { where_cat = " as_status = '10' "; }

		if (where_str.length() > 0){
			//where_str = "and|subject|제목,or|writer|작성자, ..... 형태로 넘어옴.

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
	 * 예약 가능 여부 알아보기..
	 * 
	 *****************************************************************/
	public String checkOut(String start, String end, String as_no, String o_status) throws Exception
	{
		boolean bl=true;
		String msg	= "";
		
		String temp	= "";
		/*	   temp	= assetModuleDAO.getOstatus(as_no);*/
		
		if(o_status.equals("l"))		{ temp = "대여";	 }
		else if(o_status.equals("t"))	{ temp = "이관";	 }
		else if(o_status.equals("o"))	{ temp = "반출";	 }


		// 시작날짜와 종료날짜를 제대로 넣지 않았을경우
		if((Double.parseDouble(end) - Double.parseDouble(start)) < 0) {
			bl = false;
			msg = "날짜를 올바로 기입해 주십시요";
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
		
			if(!bl) {//throw new Exception("<script> alert('예약할 수 없습니다. 예약날짜가 중복되었습니다.');history.go(-1);</script>");
				 msg = temp+"신청 할 수 없습니다. 날짜가 중복되었습니다";
			} 
		}
		return msg;
	}
		

	/****************************************************************
	*   이관 신청 가능 여부 확인										*
	*	1. 신청일자가 오늘 이후 인지 확인 오늘 이전 날짜면, 이관 불가		*
	*	2. 현재 자산 상태 확인											*
	*	3. 중복된 날짜가 있는지 확인									*
	****************************************************************/
	public String transEnable(String udate, String as_no, String o_status) throws Exception{
		
		com.anbtech.am.db.AssetModuleDAO asDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		// 현재 날짜
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String crr_date		= anbdt.getDateNoformat();	
		boolean bool		= true;

		String msg = "";
		
		// 1. 신청일자가 오늘 이후 인지 확인
		//    오늘 이전 날짜면, 이관 불가
		if ( Integer.parseInt(udate) > Integer.parseInt(crr_date) )	{
			bool = false;
			msg = "아직 처리일자가 아닙니다.";
		}

		if(bool) {
			// 2. 현재 자산 상태 확인(반출중이거나..대여..또는 수리 폐기 확인)
			String state_temp	= asDAO.getInfoStatus(as_no);	// 현재 자산 상태 가져오기
			int state_int		= Integer.parseInt(state_temp);
			switch(state_int) {
				case 7  : msg = "현재 반출중입니다. 입고되지 않았습니다.";	// 반출중
						  bool = false;
						  break;
				case 10 : msg = "현재 폐기된 자원입니다.";					// 폐기
						  bool = false;
						  break;
				case 13 : msg = "현재 수리입고상태 입니다.";				// 수리
						  bool = false;
						  break;
				case 16 : msg = "현재 대여중입니다. 반납되지 않았습니다.";	// 대여중
						  bool = false;
						  break;
			}
		}

		if (bool) {
			// 3. 중복된 날짜가 있는지 확인
			msg = checkOut(udate, udate, as_no, o_status);
		}
	
		return msg;
	}
	

	/****************************************************************
	*  자원 예약 확인 (이관 요청시 요청일 이후 예약 사항이 없어야 한다.)
	*  *미완
	****************************************************************/
	public boolean checkTranse(String udate, String as_no) throws Exception {

		com.anbtech.am.db.AssetModuleDAO  asDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		boolean bool = true;

		// 예약사항이 있는지 확인
		String booking = asDAO.bookingRecord(udate,as_no);
		// 2. 현재 대여/반출 중인지 확인
		return bool;
	}


	//***********************************************************
	//	user 정보												*
	//***********************************************************
	public String getUserInfo(String str) throws Exception 
	{
		String user_id="";
		String user_name="";
	
		// ---- 들어온 str( id/이름 ) 분리 --------
		java.util.StringTokenizer token = new java.util.StringTokenizer(str,"/");
		int temp=token.countTokens();
		while(token.hasMoreTokens()){
			user_id=token.nextToken();
			user_name=token.nextToken();
		}
		return user_id;
	}

	//********************************************************************
	//	 [  자산 감가 처리  ] 										
	//	 #. 년 Base로 처리  
	//	 #. 감가 적용 일자에 맞춰 감가 적용 ( admin/am/measurePopup.jsp )
	//********************************************************************
	public void getAutoUpdate(String year, String month, String value) throws Exception 
	{
		com.anbtech.am.entity.AsInfoTable asInfoTable;
		
		ArrayList arrylist = new ArrayList();
		ArrayList savelist = new ArrayList();
		
		// 현재일자
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String crr_time = vans.format(now);

		// 기준 일자
		int year_in = Integer.parseInt(year);
		int month_in = Integer.parseInt(month);
		int ym = Integer.parseInt(year+month);
		DecimalFormat fmt = new DecimalFormat("00");
		month = (String)fmt.format(month_in);	
		String ymd = year+month+"01";

		int value_int = Integer.parseInt(value);
		long as_result=0;

		// 감가 적용 리스트 가져오기 (폐기처리 자산 제외)
		arrylist = assetModuleDAO.getAssetValueList();
		Iterator table_iter = arrylist.iterator();
		
		while(table_iter.hasNext()){
			asInfoTable = (com.anbtech.am.entity.AsInfoTable)table_iter.next();
			int as_no=asInfoTable.getAsNo();								// 자산관리번호

			String  buy_date=asInfoTable.getBuyDate();						// 구입년월일
			long  as_price  = Long.parseLong(asInfoTable.getAsPrice());		// 구입가격
			int   dc_count  = Integer.parseInt(asInfoTable.getDcCount());	// 적용횟수
			int   as_each_dc= Integer.parseInt(asInfoTable.getAsEachDc());	// 감가적용비율
			long  as_value  = Long.parseLong(asInfoTable.getAsValue());		// 최종 감가 적용 
			
			String   apply_dcdate = asInfoTable.getApplyDcDate();
			
			int count = (int)((year_in - Integer.parseInt(buy_date.substring(0,4))));  // 기준년도 - 구입년도 => 적용횟수 
				  
			int buy_date_int  = Integer.parseInt(buy_date.substring(0,4));      // 구입년
			int buy_date_month = Integer.parseInt(buy_date.substring(4,6));		//  구입월
			
			// #. 감가 계산 idea
			//	  1. 우선 현재 이전에 자산중에 최저 감가 적용이 이루어진 자산은 제외
			//    2. 기본적으로 년차를 먼저 따져 감가적용
			//    3. 년차는 1년이 되지만, 월차이가 1년이 안되는경우는 제외

			//if( (Long.parseLong(apply_dcdate) < Long.parseLong(crr_time)) && (as_value<=Long.parseLong(value)) && (as_value != 0))  { //--(1)
			    //--------------------> 현재를 기준으로 이전 자산들..  <---| |--> 최저 감가 적용을 받았는지 판단 <---------------|
			//} else {		
				if( count == 1 ){ // --- (2)
					if( (month_in - buy_date_month) > 0 ) {  // --- (3)						
						// 감가적용
						as_result=(long)(as_price*as_each_dc*0.01);
						as_value = as_price - as_result;
						// 최저 감가 금액보다 작으면 최저 감가 금액으로 setting 
						if(as_value < value_int) as_value =  value_int;
					} else {
						as_value = as_price;
					}		
				} else if( count >= 2 ) {						
						// 감가 적용
						as_result=(long)(as_price*as_each_dc*0.01);
						as_value = as_price - (as_result*count);
						if(as_value < value_int) as_value =  value_int;
	
				} else if( count == 0) {						
						as_value = as_price;				

				} else if( count < 0) {				
						as_value = 0;
				
				} 					
				assetModuleDAO.valueUdate(as_no,""+dc_count,""+as_value,ymd ); // 감가정보 UPDATE
			//}   //**** if( apply_dcdate < crr_time && (as_value<=(long)value ))
		}	
	}

	/**************************************************
	/* 결재 처리 [결재 완료]						  *
	/* pid:전자결재관리번호 h_no:자산이력관리번호 "reject","1"
	/*************************************************/
	public void asAppInfoProcess(String pid, String h_no, String mode, String app) throws Exception 
	{			
		String status="";
		//System.out.println("pid :"+ pid+"  h_no : "+ h_no+" mode"+mode+"  app:"+app);
			
		//결재 승인시
		if("approval1".equals(mode)) {
			// app ==> 1차 or 2차 
			// 1차 이면 status = "3", 2차면 status = "5"
			if(app.equals("1")) {
				status = "3";
			} else {
				status = "5";
			}			
			assetModuleDAO.getAppinfoAndSave(pid);					// 가져온 aid로 결재 정보 불러온후 저장하기
			assetModuleDAO.setAid(pid,h_no,app);					// as_history table에 관리번호(h_no)에 aid를 setting
			assetModuleDAO.updateStatus(h_no,status,"","");			// 이관/반출에 대한 결재 처리
		
			String decision = assetModuleDAO.getDecision(h_no);		// 작성자 ID가져오기
			String type		= assetModuleDAO.getType(pid);			// 처리완료 승인 횟수(1차만 타는지.. 2차까지 타는지..)
					
			if(app.equals("1")) {	
				if(type.equals("1")) sendMail("",h_no,decision,"y");
			} else {
				if(type.equals("2")) sendMail("",h_no,decision,"y");
			}
		} 		
		//결재 반려시
		else if("reject".equals(mode)){
			status = "14"; // 반려 처리코드		
			assetModuleDAO.getAppinfoAndSave(pid);				// 가져온 aid로 결재 정보 불러온후 저장하기
			assetModuleDAO.setAid(pid,h_no,app);				// as_history table에 관리번호(h_no)에 aid를 setting
			assetModuleDAO.updateStatus(h_no,status,"","");		// 이관/반출에 대한 반려처리
			
			String decision2 = assetModuleDAO.getDecision(h_no); // 결재 처리자 ID가져오기
			
			sendMail("",h_no,decision2,"n");
		}
		//결재 상신시
		else if("submit".equals(mode)){
			// app ==> 1차 or 2차 
			// 1차 이면 status = "2", 2차면 status = "4" 상신
			if(app.equals("1")) {	//1차 상신시
				status = "2";
				assetModuleDAO.updateStatus(h_no,status,"pid",pid);		  // 상신정보 update
			} else {				//2차 상신시
				status = "4";
				assetModuleDAO.updateStatus(h_no,status,"pid2",pid);		// 상신정보 update
			}
		}
	}
	
	/********************************************************
	/ 원 String으로 만들기(삽입) 
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
		
		while(len_temp>0){  // 들어온 String의 한 문자(숫자)를 배열에 저장			
			arr[k]=won.substring(len_temp-1,len_temp);   // 배열에 String의 처음 문자(숫자)부터 저장
			k++;										 // 배열 index증가
		
			// 배열에 구분자(",") 저장
			if( i==bound ) {							 
				arr[k]=",";
				k++;
				bound+=3;
			}
			i++;		// String의 문자 sequence count
			len_temp--; // String의 index 증가
		}

		// 배열에 저장된 문자(숫자)를 불러온다.
		for( int j=k; j>0; j-- )
		{	
			str+=arr[j-1];
		}		
		if(str.charAt(0)==',') str=str.substring(1);
		return str;
	}

	/***********************************/
	/*   won단위 ',' 제거				/
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
	 * 대여/반출/이관 처리를 할 경우 안내 메시지와 첨부파일을 다운로드 할 수 있는
	 * 링크 문자열을 생성하여 전자우편으로 발송한다.
	 *****************************************************************/
	public void sendMail(String tablename,String h_no,String login_id,String is_allowed) throws Exception{
	//											
		//1.본문 HTML 생성
		StringBuffer sb = new StringBuffer();

		if(is_allowed.equals("y")) {

			 sb = makeMailContentsForCommit(tablename,h_no);
		} else { 

			if(is_allowed.equals("n")) sb = makeMailContentsForReject(tablename,h_no);
		}

		//2.본문 파일 생성
		String filename	= "DOC" + System.currentTimeMillis() + ".html";
		String bon_path = "/email/" + login_id + "/text/";
		String full_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path") + "/gw/mail" + bon_path;

		makeContentsFile(sb,full_path,filename);

		//2. 대여/반출/이관 작성자 ID 알아오기(h_no)
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		String requestor = assetModuleDAO.getWid(h_no);

		//3.메일내용을 저장
	
		assetModuleDAO.saveEmail(login_id,requestor,bon_path,filename);
	
	}

	/*****************************************************************
	 * 대출을 허락할때의 메일본문 내용 문자열을 만든다.
	 *****************************************************************/
	public StringBuffer makeMailContentsForCommit(String tablename,String h_no) throws Exception{
		
		//대출 신청 정보 가져오기
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		
		String as_no = assetModuleDAO.getAsNo(h_no);

		asHistoryTable	= assetModuleDAO.getHistory(h_no);
		asInfoTable		= assetModuleDAO.getInfo(as_no);

		String as_mid	= asInfoTable.getAsMid();		// 자산번호
		String as_name	= asInfoTable.getAsName();		// 자산 품명
		String model_name = asInfoTable.getModelName();	//	자산 모델명
		String crr_name = asInfoTable.getCrrName();		// 해당 자산 관리자

		String w_id		= asHistoryTable.getWid();			//  상신한 사람ID
		String w_name	= asHistoryTable.getWname();		//	상신한 사람ID/이름
		String write_date	= asHistoryTable.getWriteDate();	//	상신일자
		String u_date	= asHistoryTable.getUdate();			//	결재건 
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
		sb.append("<TITLE>"+o_status_name+" 처리 결과</TITLE>");
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
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>"+o_status_name+"처리가 완료되었습니다.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>자산번호:</td><td width='80%'>" + as_mid + "</TD></TR>");
		sb.append("<TR><TD width='20%'>품명:</td><td width='80%'>" + as_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>모델명:</td><td width='80%'>" + model_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>작성자:</td><td width='80%'>" + w_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>작성일:</td><td width='80%'>" + write_date + "</TD></TR>");
		sb.append("<TR><TD width='20%'>요청형태:</td><td width='80%'>" + o_status_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>처리결과:</td><td width='80%'> ["+o_status_name+" 처리가 완료 되었습니다.] </TD></TR>");
		sb.append("</TABLE>");
		sb.append("</TD></TR></TABLE>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb;
		
	}

	/*****************************************************************
	 * 대출을 반려할때의 메일본문 내용 문자열을 만든다.
	 *****************************************************************/
	public StringBuffer makeMailContentsForReject(String tablename,String h_no) throws Exception{
		
			//대출 신청 정보 가져오기
		com.anbtech.am.db.AssetModuleDAO assetModuleDAO = new com.anbtech.am.db.AssetModuleDAO(con);
		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		
		String as_no = assetModuleDAO.getAsNo(h_no);

		asHistoryTable	= assetModuleDAO.getHistory(h_no);
		asInfoTable		= assetModuleDAO.getInfo(as_no);

		String as_mid	= asInfoTable.getAsMid();		// 자산번호
		String as_name	= asInfoTable.getAsName();		// 자산 품명
		String model_name = asInfoTable.getModelName();	//	자산 모델명
		String crr_name = asInfoTable.getCrrName();		// 해당 자산 관리자

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
		sb.append("<TITLE>"+o_status_name+" 처리 결과</TITLE>");
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
		sb.append("<TR><TD colspan='2'><FONT COLOR='black'><B>"+o_status_name+" 반려처리 되었습니다.</B></TD></TR>");
		sb.append("<TR><TD width='20%'>자산번호:</td><td width='80%'>" + as_mid + "</TD></TR>");
		sb.append("<TR><TD width='20%'>품명:</td><td width='80%'>" + as_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>모델명:</td><td width='80%'>" + model_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>작성자:</td><td width='80%'>" + w_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>작성일:</td><td width='80%'>" + write_date + "</TD></TR>");
		sb.append("<TR><TD width='20%'>요청형태:</td><td width='80%'>" + o_status_name + "</TD></TR>");
		sb.append("<TR><TD width='20%'>처리결과:</td><td width='80%'> ["+o_status_name+" 반려 처리가 되었습니다.] </TD></TR>");
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

	// "-" 없애기
	public String delHyphen(String str) throws Exception {
		String temp = "";
		if(str.length()>8) temp = str.substring(0,4)+str.substring(5,7)+str.substring(8,10);
		else temp = str;
		//System.out.println(temp);
	return temp;
	}
}