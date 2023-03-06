package com.anbtech.am.db;

import com.anbtech.am.entity.*;
import com.anbtech.am.business.*;
import com.anbtech.text.Hanguel;
import com.anbtech.dms.db.AccessControlDAO;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.*;


public class AssetModuleDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public AssetModuleDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	 * 카테고리 생성(저장)
	 *******************************************************************/
	public void save_ct(String ct_id,String ct_level,String ct_parent,String ct_word,String ct_name,String dc_percent,String apply_dc) throws Exception
	{
		PreparedStatement pstmt = null;

		String query = "INSERT INTO as_category (ct_id,ct_level,ct_parent,ct_word,ct_name,dc_percent,apply_dc) VALUES (?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,ct_id);
		pstmt.setString(2,ct_level);
		pstmt.setString(3,ct_parent);
		pstmt.setString(4,ct_word);
		pstmt.setString(5,ct_name);
		pstmt.setString(6,dc_percent);
		pstmt.setString(7,apply_dc);

			
		pstmt.executeUpdate();
		pstmt.close();
	}

	/*******************************************************************
	 * 카테고리 수정
	 *******************************************************************/
	public void update_ct(String c_no,String ct_word,String ct_name,String dc_percent,String apply_dc) throws Exception
	{
		Statement st = null;
		st = con.createStatement();

		String query = "UPDATE as_category SET ct_word='"+ct_word+"',ct_name='"+ct_name+"',dc_percent='"+dc_percent+"', apply_dc='"+apply_dc+"' WHERE c_no='"+c_no+"'";

		st.executeUpdate(query);
		st.close();

	}

	/***********************************************************************
	//   카테고리 삭제 - ( 카테고리 삭제 요청시 호출  )
	//   1. 하위카테고리 검색 (하위카테고리 존재시 현재 카테고리 삭제 불능)
	//   2. 현재 카테고리에 영역에 항목 검색 
	//      (항목 존재시 카테고리 삭제 불능)
	 ***********************************************************************/
	public String delete_ct(String c_no,String ct_id) throws Exception 
	{
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		String msg ="";  // 메소드 요청 결과 메시지 저장 변수

		String query = "SELECT count(ct_id) FROM as_category WHERE ct_id like '"+ct_id+"%' ";  // 현재 카테고리 문자를 포함하는 카테고리 SELECT
		rs=st.executeQuery(query);
		int j =0;
		if(rs.next()){
			
			int i = Integer.parseInt(rs.getString(1));	// 하위 카테고리 검색(갯수현황)
				
			if(i>1) {
				msg = "하위분류가 있어 삭제할 수 없습니다.";
			} else {
					query = "SELECT count(as_no) FROM as_info WHERE c_no='"+c_no+"'";
					rs=st.executeQuery(query);
					if(rs.next()) j=Integer.parseInt(rs.getString(1));
					
				 if( j > 0 ){
					 
					 msg = "현재 분류에 속한 자산이 존재하여 삭제할 수 없습니다."; 
				 
				 } else {				
			 
					query = "DELETE as_category WHERE c_no = '"+c_no+"'";
					st.executeUpdate(query);
					msg = "삭제 되었습니다.";
				 }
			} // end_if

		}  // end_if
	
		rs.close();
		st.close();
		return msg;

	}


	/**********************************
	* 카테고리 ID가져오기
	***********************************/	
	public String selectCtId(String str,String div) throws Exception{
		
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String ctid = "";
		String query = "";

		if(div.equals("f")){   // 최상위 카테고리 
			query = "SELECT max(ct_id) FROM as_category WHERE c_no ='"+str+"' or ct_level ='1'";
		} else if(div.equals("a")) { // 하위 카테고리
			query = "SELECT max(ct_id) FROM as_category WHERE ct_parent ='"+str+"'";
		} else if(div.equals("m")) {
				
		} else if(div.equals("d")) {
				
		}
			
		rs = st.executeQuery(query);
		
		if(rs.next()){
			ctid = rs.getString(1);
		} 	
		rs.close();
		st.close();
		
		if(ctid==null) ctid="0";

		return ctid;
	}

	/*******************************************************************
	 * 자산 List
	 *******************************************************************/
	 public ArrayList getAssetList() throws Exception{
	
		com.anbtech.am.entity.AsInfoTable asInfoTable;
		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
		Statement st = null;
		ResultSet rs = null;

		ArrayList arrylist = new ArrayList();
		st = con.createStatement();

		String query = "SELECT * FROM as_info WHERE as_status != '10'  ORDER BY as_no DESC";  // 폐기된 자산 제외
		rs=st.executeQuery(query);

		while (rs.next())
		{
			asInfoTable = new com.anbtech.am.entity.AsInfoTable();
			asInfoTable.setAsNo(rs.getInt("as_no"));
			asInfoTable.setAsMid(rs.getString("as_mid"));
			asInfoTable.setCno(rs.getString("c_no"));
			asInfoTable.setWid(rs.getString("w_id"));
			asInfoTable.setWname(rs.getString("w_name"));
			asInfoTable.setBname(rs.getString("b_name"));
			asInfoTable.setModelName(rs.getString("model_name"));
			asInfoTable.setAsName(rs.getString("as_name"));
			asInfoTable.setAsSerial(rs.getString("as_serial"));
			asInfoTable.setBuyDate(rs.getString("buy_date"));
			asInfoTable.setAsPrice(rs.getString("as_price"));
			asInfoTable.setDcCount(rs.getString("dc_count"));
			asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
			asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
			asInfoTable.setAsValue(assetModuleBO.makeWon(rs.getString("as_value")));
			asInfoTable.setCrrId(rs.getString("crr_id"));
			asInfoTable.setCrrName(rs.getString("crr_name"));
			asInfoTable.setBuyWhere(rs.getString("buy_where"));
			asInfoTable.setAsMaker(rs.getString("as_maker"));
			asInfoTable.setAsSetting(rs.getString("as_setting"));
			asInfoTable.setBwTel(rs.getString("bw_tel"));
			asInfoTable.setBwAddress(rs.getString("bw_address"));
			asInfoTable.setBwEmployee(rs.getString("bw_employee"));
			asInfoTable.setBwMgrTel(rs.getString("bw_mgr_tel"));
			asInfoTable.setEtc(rs.getString("etc"));
			asInfoTable.setAsStatus(rs.getString("as_status"));
			asInfoTable.setAsStatusName(assetModuleBO.getStatname(rs.getString("as_status")));
			asInfoTable.setHandle(rs.getString("handle"));
			
		
			asInfoTable.setAsExceptDay(rs.getString("as_except_day"));
			asInfoTable.setAsExceptReason(rs.getString("as_except_reason"));
			
			asInfoTable.setNowStatus(assetModuleBO.getStatname(getNowStatus(""+rs.getInt("as_no"))));
		/*	asInfoTable.setFileSe(rs.getString("file_se"));
			asInfoTable.setFileName(rs.getString("file_name"));
			asInfoTable.setFileType(rs.getString("file_type"));
			asInfoTable.setFileSize(rs.getString("file_size"));
			asInfoTable.setFileUmask(rs.getString("file_umask"));
			asInfoTable.setFilePath(rs.getString("file_path"));*/
			arrylist.add(asInfoTable);
		}

		rs.close();
		st.close();

		return arrylist;
	}

	// 현재 상태 정보 
	public String getNowStatus(String as_no) throws Exception{
		
		Statement nst = null;
		ResultSet nrs = null;

		nst = con.createStatement();
		
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String crr_date = anbdt.getDateNoformat();	

		String query = "SELECT as_status,h_no FROM as_history WHERE (( u_date <='"+crr_date+"' and in_date >='"+crr_date+"') and (  as_status='7' or as_status='16' ) and as_status!='10' ) and as_no='"+as_no+"'";

		nrs = nst.executeQuery(query);
		String status="";
		String hno ="";
		if(nrs.next()) {
			status = nrs.getString("as_status");
			hno = nrs.getString("h_no");
		}
		
		nrs.close();
		nst.close();

		return status;
	}

	/*******************************************************************
	 * 감가 적용 자산 List 가져오기 
	 *******************************************************************/
	 public ArrayList getAssetValueList() throws Exception{
	
		Statement st = null;
		ResultSet rs = null;

		com.anbtech.am.entity.AsInfoTable asInfoTable;
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String hrs = (anbdt.getDateNoformat()).substring(0,4);	
		NumberFormat nf = NumberFormat.getInstance();

		ArrayList arrylist = new ArrayList();
		st = con.createStatement();

		String query = "SELECT as_no,buy_date,as_price,dc_count,as_each_dc,as_value,apply_dcdate FROM as_info WHERE as_status != '10'";  // 부동산 제외 방법 생각

		rs=st.executeQuery(query);

		while (rs.next())
		{
			asInfoTable = new com.anbtech.am.entity.AsInfoTable();
			asInfoTable.setAsNo(rs.getInt("as_no"));
			asInfoTable.setBuyDate(rs.getString("buy_date"));
			asInfoTable.setAsPrice(rs.getString("as_price"));
			asInfoTable.setDcCount(rs.getString("dc_count"));
			asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
			asInfoTable.setAsValue(""+nf.parse(rs.getString("as_value")));
			asInfoTable.setApplyDcDate(rs.getString("apply_dcdate"));

			arrylist.add(asInfoTable);
		}

		rs.close();
		st.close();

		return arrylist;
	}
	
	
	/**********************************
	* 카테고리(1개) 정보 가져오기
	***********************************/
	public AsCategoryTable getCtInfo(String ct_id) throws Exception{
		
		Statement st = null;
		ResultSet rs = null;

		com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
		st = con.createStatement();
		String query = " SELECT * From as_category WHERE ct_id = '"+ct_id+"'";
		rs = st.executeQuery(query);
		
		if(rs.next()){
			asCategoryTable.setCno(Integer.parseInt(rs.getString("c_no")));
			asCategoryTable.setCtId(rs.getString("ct_id"));
			asCategoryTable.setCtLevel(rs.getString("ct_level"));
			asCategoryTable.setCtParent(rs.getString("ct_parent"));
			asCategoryTable.setCtWord(rs.getString("ct_word"));
			asCategoryTable.setCtName(rs.getString("ct_name"));
			asCategoryTable.setDcPercent(rs.getString("dc_percent"));
			asCategoryTable.setApplyDc(rs.getString("apply_dc"));
		}
		
		rs.close();
		st.close();
		return asCategoryTable;
	}

	/**********************************
	* 카테고리(1개) 정보 가져오기
	***********************************/
	public AsCategoryTable getCtInfoByCno(String c_no) throws Exception{
		
		Statement st = null;
		ResultSet rs = null;

		com.anbtech.am.entity.AsCategoryTable asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
		st = con.createStatement();
		String query = " SELECT * From as_category WHERE c_no = '"+c_no+"'";
		rs = st.executeQuery(query);
		
		if(rs.next()){
			asCategoryTable.setCno(Integer.parseInt(rs.getString("c_no")));
			asCategoryTable.setCtId(rs.getString("ct_id"));
			asCategoryTable.setCtLevel(rs.getString("ct_level"));
			asCategoryTable.setCtParent(rs.getString("ct_parent"));
			asCategoryTable.setCtWord(rs.getString("ct_word"));
			asCategoryTable.setCtName(rs.getString("ct_name"));
			asCategoryTable.setDcPercent(rs.getString("dc_percent"));
			asCategoryTable.setApplyDc(rs.getString("apply_dc"));
		}
		
		rs.close();
		st.close();
		return asCategoryTable;
	}

	// 작성자 ID 가져오기
	public String getWid(String h_no) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT w_id FROM as_history WHERE h_no='"+h_no+"'";
		rs = st.executeQuery(query);
		String w_id="";
		if(rs.next()) w_id=rs.getString("w_id");

		rs.close();
		st.close();

		return w_id;
	}


	/*****************************************************
	* 자산 정보 등록 가능 여부 판단 - return type boolean
	* (현재 카테고리의 하위 카테고리 존재시 등록 불능)
	*  존재시 true, 없을시 false 리턴
	******************************************************/
	public boolean assetChk(String c_no) throws Exception {
		
		Statement st = null;
		ResultSet rs = null;

		st =  con.createStatement();
		String query = "SELECT ct_id FROM as_category WHERE c_no = '"+c_no+"'";
		rs = st.executeQuery(query);
		rs.next();
		String ct_id = rs.getString("ct_id");
		

		query = "SELECT count(c_no) FROM as_category WHERE ct_id like '"+ct_id+"%'";
		rs = st.executeQuery(query);
		
		boolean bool = false;

		if(rs.next()){ 
		
			int i = Integer.parseInt(rs.getString(1));
			if(i>1){ bool = true; }
		}
		
		rs.close();
		st.close();

		return bool;
	}


	/**********************************
	* 자산 정보 등록
	***********************************/
	public void saveAssetInfo (String as_mid,String c_no,String as_item_no,String w_id,String w_name,String w_rank,String b_id,String  b_name,String b_rank,String model_name,String as_name,String as_serial, String buy_date,String as_price,String dc_count,String  as_each_dc,String as_value,String crr_id,String crr_name,String crr_rank,String buy_where,String as_maker,String  as_setting,String  bw_tel,String bw_address,String bw_employee,String bw_mgr_tel,String etc,String handle,String ct_id) throws Exception{
			//,String file_se,String file_name,String file_type,String file_size,String file_umask,String file_path		
		
		PreparedStatement pstmt = null;
		
		ct_id = getCtId(c_no); // as_category 에서 ct_id 가져오기 
				
		String query = "INSERT INTO as_info (as_mid, c_no, as_item_no,w_id,w_name,w_rank,b_id,b_name,b_rank,model_name,as_name,as_serial, buy_date,as_price,dc_count,as_each_dc,as_value, crr_id, crr_name, crr_rank, u_id, u_name, u_rank, buy_where, as_maker, as_setting, bw_tel, bw_address, bw_employee, bw_mgr_tel, etc,handle,ct_id,apply_dcdate) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
		
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,as_mid);
		pstmt.setString(2,c_no);
		pstmt.setString(3,as_item_no);
		pstmt.setString(4,w_id);
		pstmt.setString(5,w_name);
		pstmt.setString(6,w_rank);
		pstmt.setString(7,b_id);
		pstmt.setString(8,b_name);
		pstmt.setString(9,b_rank);
		pstmt.setString(10,model_name);
		pstmt.setString(11,as_name);
		pstmt.setString(12,as_serial);
		pstmt.setString(13,buy_date);
		pstmt.setString(14,as_price);
		pstmt.setString(15,dc_count);
		pstmt.setString(16,as_each_dc);
		pstmt.setString(17,as_value);
		pstmt.setString(18,crr_id);
		pstmt.setString(19,crr_name);
		pstmt.setString(20,crr_rank);
		pstmt.setString(21,crr_id);
		pstmt.setString(22,crr_name);
		pstmt.setString(23,crr_rank);
		pstmt.setString(24,buy_where);
		pstmt.setString(25,as_maker);
		pstmt.setString(26,as_setting);
		pstmt.setString(27,bw_tel);
		pstmt.setString(28,bw_address);
		pstmt.setString(29,bw_employee);
		pstmt.setString(30,bw_mgr_tel);
		pstmt.setString(31,etc);
		pstmt.setString(32,handle);
		pstmt.setString(33,ct_id);
		pstmt.setString(34,"0");
	/*	
		pstmt.setString(30,file_se);
		pstmt.setString(31,file_name);
		pstmt.setString(32,file_type);
		pstmt.setString(33,file_size);
		pstmt.setString(34,file_umask);
		pstmt.setString(35,file_path);
	*/			
		pstmt.executeUpdate();
		pstmt.close();
	}

	/**************************************
	* 자산 정보 삭제(폐기) --> status = 10 *
	***************************************/
	public void asDelete(String as_no,String as_except_day,String as_except_reason,String del_form,String del_reason) throws Exception {
		
		Statement stmt = con.createStatement();
		String query = "UPDATE as_info SET as_status='10', as_except_day='"+as_except_day+"',as_except_reason='"+as_except_reason+"',del_form='"+del_form+"',del_reason='"+del_reason+"'  WHERE as_no='"+as_no+"'";
		stmt.executeUpdate(query);
		
		stmt.close();
	}

	/**************************************
	* 카테고리명(품목명) 가져오기 input=>c_no / output=> ct_name
	***************************************/
	public String getAsName(String c_no) throws Exception {
		
		
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		String query = "SELECT ct_name FROM as_category WHERE c_no='"+c_no+"'";
			
		rs=stmt.executeQuery(query);
		rs.next();
		String ct_name=rs.getString("ct_name");

		stmt.close();
		rs.close();
		return ct_name;
	
	}

	/**********************************
	* 자산 정보 수정 
	***********************************/
	public void modifyAssetInfo(String asno,String as_mid,String c_no,String as_item_no,String w_id,String w_name,String w_rank,String b_id,String  b_name,String b_rank,String model_name,String as_name,String as_serial, String buy_date,String as_price,String dc_count,String  as_each_dc,String as_value,String crr_id,String crr_name,String crr_rank,String buy_where,String as_maker,String  as_setting,String  bw_tel,String bw_address,String bw_employee,String bw_mgr_tel,String etc,String handle,String ct_id,String as_status) throws Exception {
	
		int as_no= Integer.parseInt(asno);
		PreparedStatement pstmt = null;
		
		String query = "UPDATE as_info SET as_mid=?, c_no=?, as_item_no=?,w_id=?,w_name=?,w_rank=?,b_id=?,b_name=?,b_rank=?,model_name=?,as_name=?,as_serial=?, buy_date=?,as_price=?,dc_count=?,as_each_dc=?,as_value=?, crr_id=?, crr_name=?, crr_rank=?, buy_where=?, as_maker=?, as_setting=?, bw_tel=?, bw_address=?, bw_employee=?, bw_mgr_tel=?, etc=? ,handle=? ,ct_id=?,as_status=? WHERE as_no=?";
		
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,as_mid);
		pstmt.setString(2,c_no);
		pstmt.setString(3,as_item_no);
		pstmt.setString(4,w_id);
		pstmt.setString(5,w_name);
		pstmt.setString(6,w_rank);
		pstmt.setString(7,b_id);
		pstmt.setString(8,b_name);
		pstmt.setString(9,b_rank);
		pstmt.setString(10,model_name);
		pstmt.setString(11,as_name);
		pstmt.setString(12,as_serial);
		pstmt.setString(13,buy_date);
		pstmt.setString(14,as_price);
		pstmt.setString(15,dc_count);
		
		pstmt.setString(16,as_each_dc);
		pstmt.setString(17,as_value);
		pstmt.setString(18,crr_id);
		pstmt.setString(19,crr_name);
		pstmt.setString(20,crr_rank);
		pstmt.setString(21,buy_where);
		pstmt.setString(22,as_maker);
		pstmt.setString(23,as_setting);
		pstmt.setString(24,bw_tel);
		pstmt.setString(25,bw_address);
		pstmt.setString(26,bw_employee);
		pstmt.setString(27,bw_mgr_tel);
		pstmt.setString(28,etc);
		
		pstmt.setString(29,handle);
		pstmt.setString(30,ct_id);
		pstmt.setString(31,as_status);
		pstmt.setInt(32,as_no);

		pstmt.executeUpdate();
		pstmt.close();
	}

	/******************************************************************
	* 카테고리의 품목 시리얼 num가져오기( =>  추가될 품목의 시리얼번호 생성)
	******************************************************************/
	public String getAsTotalNum(String c_no) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();

		String query = "SELECT max(as_item_no) FROM as_info WHERE c_no = '"+c_no+"'";

		String num="001";	
		int no=0;
		rs = st.executeQuery(query);

		if(rs.next()){
			 if(rs.getString(1)==null) 
			 {

			 } else {
				 no = Integer.parseInt(rs.getString(1))+1;
						
				if(no<10) {
					num = "00" + no;
				} else if(no<100){
					num ="0"+no;
				} else if(no<1000){
					num =""+no;
				}
			}
		} 
		

		rs.close();
		st.close();
		return num;


	}

	/*********************************************************
	* as_category(카테고리DB) Table에서 해당 카테고리의 Ct_Id 
	**********************************************************/
	public String getCtId(String c_no) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		st = con. createStatement();
		String query = "SELECT ct_id FROM as_category WHERE c_no='"+c_no+"'";
		rs = st.executeQuery(query);

		String id="";
		if(rs.next()){
			id = rs.getString("ct_id");
		}

		rs.close();
		st.close();
		return id;
	}

	/*********************************************************
	* as_category(카테고리DB) Table에서 해당 카테고리의 Ct_Name 
	**********************************************************/
	public String getCtName(String c_no) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		st = con. createStatement();
		String query = "SELECT ct_name FROM as_category WHERE c_no='"+c_no+"'";
		rs = st.executeQuery(query);

		String ct_name="";
		if(rs.next()){
			ct_name = rs.getString("ct_name");
		}

		rs.close();
		st.close();
		return ct_name;
	}


	/*********************************************************
	* as_category(카테고리DB) Table에서 해당 카테고리의 ct_word 
	**********************************************************/
	public String getCtWord(String c_no) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		st = con. createStatement();
		String query = "SELECT ct_word FROM as_category WHERE c_no='"+c_no+"'";
		rs = st.executeQuery(query);

		String ct_word="";
		if(rs.next()){
			ct_word = rs.getString("ct_word");
		}

		rs.close();
		st.close();
		return ct_word;
	}

	/*********************************************************
	* as_info(TABLE)에서 c_no 가져오기
	**********************************************************/
	public String getCno(String as_no) throws Exception{
		
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT c_no FROM as_info WHERE as_no='"+as_no+"'";
		rs =  st.executeQuery(query);

		String temp_cno="";
		if(rs.next()){
			temp_cno = rs.getString("c_no");
		}
		rs.close();
		st.close();
		return temp_cno;
	}

	/***************************************************************
	* as_info(카테고리DB) Table에서 해당 카테고리의 자산정보가져오기
	***************************************************************/
	public AsInfoTable getInfo(String asno) throws Exception {
		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
		com.anbtech.am.entity.AsInfoTable asInfoTable = new com.anbtech.am.entity.AsInfoTable();
		Statement st = null;
		ResultSet rs = null;

		int as_no = Integer.parseInt(asno);
		st = con. createStatement();
		String query = "SELECT * FROM as_info WHERE as_no = '"+as_no+"'";
		rs = st.executeQuery(query);
		rs.next();

		asInfoTable.setAsNo(rs.getInt("as_no"));
		asInfoTable.setAsMid(rs.getString("as_mid"));
		asInfoTable.setCno(rs.getString("c_no"));
		asInfoTable.setWid(rs.getString("w_id"));
		asInfoTable.setWname(rs.getString("w_name"));
		asInfoTable.setWrank(rs.getString("w_rank"));
		
		asInfoTable.setBid(rs.getString("b_id"));
		asInfoTable.setBname(rs.getString("b_name"));
		asInfoTable.setBrank(rs.getString("b_rank"));

		asInfoTable.setModelName(rs.getString("model_name"));
		asInfoTable.setAsName(rs.getString("as_name"));
		asInfoTable.setAsSerial(rs.getString("as_serial"));
		asInfoTable.setBuyDate(rs.getString("buy_date"));
		asInfoTable.setAsPrice(rs.getString("as_price"));
		asInfoTable.setDcCount(rs.getString("dc_count"));
		asInfoTable.setApplyDcDate(rs.getString("apply_dcdate"));
		//asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
		asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
		asInfoTable.setAsValue(rs.getString("as_value"));
		asInfoTable.setCrrId(rs.getString("crr_id"));
		asInfoTable.setCrrName(rs.getString("crr_name"));
		asInfoTable.setCrrRank(rs.getString("crr_rank"));
		asInfoTable.setUname(rs.getString("u_name"));
		asInfoTable.setBuyWhere(rs.getString("buy_where"));
		asInfoTable.setAsMaker(rs.getString("as_maker"));
		asInfoTable.setAsSetting(rs.getString("as_setting"));
		asInfoTable.setBwTel(rs.getString("bw_tel"));
		asInfoTable.setBwAddress(rs.getString("bw_address"));
		asInfoTable.setBwEmployee(rs.getString("bw_employee"));
		asInfoTable.setBwMgrTel(rs.getString("bw_mgr_tel"));
		asInfoTable.setEtc(rs.getString("etc"));
		asInfoTable.setAsStatus(rs.getString("as_status"));
		asInfoTable.setAsExceptDay(rs.getString("as_except_day"));
		asInfoTable.setAsExceptReason(rs.getString("as_except_reason"));
		asInfoTable.setFileSe(rs.getString("file_se"));
		asInfoTable.setFileName(rs.getString("file_name"));
		asInfoTable.setFileType(rs.getString("file_type"));
		asInfoTable.setFileSize(rs.getString("file_size"));
		asInfoTable.setFileUmask(rs.getString("file_umask"));
		asInfoTable.setFilePath(rs.getString("file_path"));
		asInfoTable.setHandle(rs.getString("handle"));
		asInfoTable.setCtId(rs.getString("ct_id"));
		asInfoTable.setDelForm(rs.getString("del_form"));
		asInfoTable.setDelReason(rs.getString("del_reason"));

		rs.close();
		st.close();
		return asInfoTable;
	}

	/************************************************************************
	* as_history(이력정보DB) Table에서 해당 자산의 이력정보가져오기 -리스트 *
	*************************************************************************/
	public ArrayList getInfoList(String tablename,String mode,String searchword,String searchscope,String page,String as_no, String div,String login_division) throws Exception {//tablename,mode,login_id,searchscope,page,as_no,""
		Statement st = null;
		ResultSet rs = null;

		// 1.이력정보 리스트 
		ArrayList arrylist = new ArrayList();
		st = con.createStatement();
		String query="";
		
		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		int current_page_num =Integer.parseInt(page);
		String where="";
		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
		com.anbtech.am.business.AMLinkBO amLinkBO = new com.anbtech.am.business.AMLinkBO(con);

		// 권한 여부
		boolean bool = false;
		bool = chk_privilege(searchword,div); // searchword => login_id
		String temp = "";
		if(bool) { temp ="1"; } else { temp="2"; }

		if(mode.equals("req_app_list")) {
				 where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"app");
		
		} else if(mode.equals("user_each_history")){ // 1. 이력정보 리스트       
				 where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"each");	// searchword => login_id
		
		} else if(mode.equals("EnteringList") && div.equals("lending")){					// 자산 대여 업무(반납처리대상)
				 where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		
		} else if(mode.equals("TransOutList") && div.equals("lending")){					// 자산 대여 업무(대여처리대상)
				 where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		
		} else if(mode.equals("EnteringList")){
				 where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		
		} else if(mode.equals("TransOutList")){
				 where = assetModuleBO.getWhere(mode,searchword,searchscope,as_no,"");
		
		} else if(mode.equals("lending_list")){ // 대여 결재 대상
				
		         where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		} else if(mode.equals("asset_del_list")){
				
				where = assetModuleBO.getWhere(mode,login_division,temp,as_no,div);
		}

		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
		total = 1;
		int recNum = total;
		


		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
		
		query = "SELECT * FROM as_history "+ where + " ORDER BY h_no desc";
		rs = st.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}
		String temp_asno="";    // 자산 관리 번호 임시저장 변수
		String info_status="";	// 자산 상태 번호
		String info_asmid="";	// 자산 관리 번호 임시저장 변수
		
		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}
		
			asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();
			
			asHistoryTable.setHno(rs.getInt("h_no"));
				temp_asno=rs.getString("as_no");
				info_asmid=getInfoAsMid(rs.getString("as_no"));
			asHistoryTable.setAsMid(info_asmid);
			asHistoryTable.setWid(rs.getString("w_id"));
			asHistoryTable.setWname(rs.getString("w_name"));
			asHistoryTable.setWrank(rs.getString("w_rank"));
			asHistoryTable.setWriteDate(rs.getString("write_date"));
			//asHistoryTable.setOstatus(rs.getString("o_status"));
			asHistoryTable.setUid(rs.getString("u_id"));
			asHistoryTable.setUname(rs.getString("u_name"));
			asHistoryTable.setUrank(rs.getString("u_rank"));
			asHistoryTable.setTakeOutReason(rs.getString("takeout_reason"));
			asHistoryTable.setOutDestination(rs.getString("out_destination"));
			asHistoryTable.setUdate(rs.getString("u_date"));
			asHistoryTable.setUyear(rs.getString("u_year"));
			asHistoryTable.setUmonth(rs.getString("u_month"));
			asHistoryTable.setUday(rs.getString("u_day"));
			asHistoryTable.setTuDate(rs.getString("tu_date"));
			asHistoryTable.setTuYear(rs.getString("tu_year"));
			asHistoryTable.setTuMonth(rs.getString("tu_month"));
			asHistoryTable.setTuDay(rs.getString("tu_day"));
			asHistoryTable.setInDate(rs.getString("in_date"));
			asHistoryTable.setCyear(rs.getString("c_year"));
			asHistoryTable.setCmonth(rs.getString("c_month"));
			asHistoryTable.setCday(rs.getString("c_day"));
			asHistoryTable.setAsStatus(rs.getString("as_status"));
			asHistoryTable.setOstatus(rs.getString("o_status"));
			asHistoryTable.setAsStatusName(assetModuleBO.getStatname(rs.getString("as_status")));
			asHistoryTable.setOstatusName(assetModuleBO.getStatname(rs.getString("o_status")));
			asHistoryTable.setAsStatusInfo(rs.getString("as_statusinfo"));
		
			asHistoryTable.setAsNo(temp_asno);
				info_status = getInfoStatus(temp_asno);
			asHistoryTable.setInfoStatus(info_status);

			String  link = amLinkBO.makeLink(bool,rs.getString("u_date"),rs.getString("o_status"),rs.getString("as_status"),rs.getString("u_id"),rs.getInt("h_no"),rs.getString("as_no"),searchword,info_status); // searchword => login_id;  link문자열 생성 
			asHistoryTable.setLink(link);
			asHistoryTable.setPid(rs.getString("pid"));

		    arrylist.add(asHistoryTable);
		}
		
		rs.close();
		st.close();
		return arrylist;
	}
	
	/************************************
	/  자산 상태 코드 가져오기
	************************************/
	public String getInfoStatus(String as_no) throws Exception {
	
		Statement nst = null;
		ResultSet nrs = null;

		nst = con.createStatement();
		String query = "SELECT as_status FROM as_info WHERE as_no='"+as_no+"'";
		nrs = nst.executeQuery(query);
		String info_status="";
		if(nrs.next()){
			info_status = nrs.getString("as_status");

		}

		nrs.close();
		nst.close();
		
		return info_status;
	}

	/***********************************
	*  예약 사항 알아오기
	*  ** 미완코드
	************************************/
	public String bookingRecord(String udate, String as_no) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "";
		String msg	 = "";
		query = "SELECT as_status FROM as_history WHERE as_no='"+as_no+"' and u_date >= '"+udate+"'";
			
		rs = st.executeQuery("as_status");
		
		while (rs.next())
		{	
			int temp =  Integer.parseInt(rs.getString("as_status"));
			switch (temp)	{
				case 2 :
				case 3 :
				case 4 :
				case 5 : msg = "현재 예약된 처리가 있습니다. 이관신청이 불가능합니다.";
						 break;
			}
		}

		st.close();
		rs.close();
		return msg;
	}


	/*************************************
	* 자산번호 가져오기
	*************************************/
	public String getInfoAsMid(String as_no) throws Exception {
		
		Statement nst = null;
		ResultSet nrs = null;

		nst = con.createStatement();
		String query = "SELECT as_mid FROM as_info WHERE as_no='"+as_no+"'";
		nrs = nst.executeQuery(query);
		String info_asmid="";
		if(nrs.next()){
			info_asmid = nrs.getString("as_mid");

		}

		nrs.close();
		nst.close();
		
		return info_asmid;
	}

	/*************************************
	 * 첨부파일 정보를 DB에 저장한다.
	 *************************************/
	public void updTable(String tablename, String set, String where) throws Exception{

		Statement stmt = null;
		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************
	 * 첨부파일 리스트 가져오기
	 *****************************************************************/
	public ArrayList getFileList(String asno) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();
		int as_no = Integer.parseInt(asno);

		String query = "SELECT file_name,file_size,file_type,file_umask FROM as_info WHERE as_no = '"+as_no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("file_name")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("file_size")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("file_type")).iterator();
			Iterator fileumask_iter = com.anbtech.util.Token.getTokenList(rs.getString("file_umask")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				AsInfoTable file = new AsInfoTable();
				file.setFileName((String)filename_iter.next());
				file.setFileType((String)filetype_iter.next());
				file.setFileSize((String)filesize_iter.next());
//				file.setFileUmask((String)fileumask_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}//getFile_list()

	
	/********************************************************************
	 * 자산번호의 관리번호를 가져와 리턴한다.
	 ********************************************************************/	
	public String getId(String tablename, String as_mid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT as_no FROM "+tablename+" WHERE as_mid = '"+as_mid+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String as_no = rs.getString("as_no");
		stmt.close();
		rs.close();
		return as_no;
	}

	/********************************************************************
	 * 자산 History( 건당 ) 정보 가져오기
	 ********************************************************************/	
	public AsHistoryTable getHistory(String h_no) throws Exception{
		
		Statement stmt = null;
		ResultSet rs = null;
		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);
		com.anbtech.am.entity.AsHistoryTable asHistoryTable = new com.anbtech.am.entity.AsHistoryTable();

		String query = "SELECT * FROM as_history WHERE h_no = '"+h_no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		if(rs.next()){
			asHistoryTable.setHno(rs.getInt("h_no"));	
			asHistoryTable.setAsNo(rs.getString("as_no"));
			asHistoryTable.setWid(rs.getString("w_id"));
			asHistoryTable.setWname(rs.getString("w_name"));
			asHistoryTable.setWrank(rs.getString("w_rank"));
			asHistoryTable.setUid(rs.getString("u_id"));
			asHistoryTable.setUname(rs.getString("u_name"));
			asHistoryTable.setUrank(rs.getString("u_rank"));
			asHistoryTable.setTakeOutReason(rs.getString("takeout_reason"));
			asHistoryTable.setOutDestination(rs.getString("out_destination"));
			asHistoryTable.setWriteDate(rs.getString("write_date"));
			asHistoryTable.setUdate(rs.getString("u_date"));
			asHistoryTable.setUyear(rs.getString("u_year"));
			asHistoryTable.setUmonth(rs.getString("u_month"));
			asHistoryTable.setUday(rs.getString("u_day"));
			asHistoryTable.setTuDate(rs.getString("tu_date"));
			asHistoryTable.setTuYear(rs.getString("tu_year"));
			asHistoryTable.setTuMonth(rs.getString("tu_month"));
			asHistoryTable.setTuDay(rs.getString("tu_day"));
			asHistoryTable.setInDate(rs.getString("in_date"));
			asHistoryTable.setWiDate(rs.getString("wi_date"));
			asHistoryTable.setCyear(rs.getString("c_year"));
			asHistoryTable.setCmonth(rs.getString("c_month"));
			asHistoryTable.setCday(rs.getString("c_day"));
			asHistoryTable.setAsStatus(rs.getString("as_status"));
			asHistoryTable.setAsStatusName(assetModuleBO.getStatname(rs.getString("as_status")));	// 상태 코드(문자)
			asHistoryTable.setOstatusName(assetModuleBO.getStatname(rs.getString("o_status")));		// 상태 코드(문자)

			asHistoryTable.setAsStatusInfo(rs.getString("as_statusinfo"));	
			asHistoryTable.setOstatus(rs.getString("o_status"));			
			asHistoryTable.setPid(rs.getString("pid"));
			asHistoryTable.setPid2(rs.getString("pid2"));

		}
			
		stmt.close();
		rs.close();
		return asHistoryTable;
	}

	/********************************************************************
	 * 입고 처리 Process
	 *******************************************************************/	
	public void asInputProcess(String h_no,String as_status,String o_status,String as_statusinfo,String as_no,String in_date,String wi_date) throws Exception{
	
		Statement st = null;
		ResultSet rs = null;

		st= con.createStatement();
		String query="";

		// as_history의 자산 책임자 정보를 가져온다.
		// 자산 반납 및 입고시 자산의 현재사용자를 자산의 책임자 정보를 변경하기 위해
		query = "SELECT crr_id, crr_name, crr_rank FROM as_info WHERE as_no='"+as_no+"'";
		rs = st.executeQuery(query);
		String temp_id   = "";
		String temp_name = "";
		String temp_rank = "";
		
		if (rs.next())	{
			temp_id   = rs.getString("crr_id");	
			temp_name = rs.getString("crr_name");	
			temp_rank = rs.getString("crr_rank");  }

	
		if(as_status.equals("13") || as_status.equals("10")){ // 입고 상태가 수리 or 폐기 일경우

			query = "UPDATE as_info SET as_status ='"+as_status+"', u_id='"+temp_id+"', u_name='"+temp_name+"', u_rank='"+temp_rank+"'  WHERE as_no='"+as_no+"'";	
			st.executeUpdate(query);
			
			query = "UPDATE as_history SET as_status='"+as_status+"', as_statusinfo='"+as_statusinfo+"', in_date='"+in_date+"', wi_date='"+wi_date+"'  WHERE h_no='"+h_no+"'";
			st.executeUpdate(query);

		} else {
			// 자산 입고 처리시 현재 자산 사용자를 관리자 이름으로 변경(반납)한다.
			query = "UPDATE as_info SET u_id='"+temp_id+"', u_name='"+temp_name+"', u_rank='"+temp_rank+"' WHERE as_no = '"+as_no+"'";
			st.executeUpdate(query);

			query = "UPDATE as_history SET as_status='"+as_status+"', as_statusinfo='"+as_statusinfo+"', in_date='"+in_date+"', wi_date='"+wi_date+"'  WHERE h_no='"+h_no+"'";
			st.executeUpdate(query);
		}

		st.close();
		rs.close();
	}

	
	/********************************************************************
	 * 검색 결과 가져오기
	 ********************************************************************/	
	public ArrayList getAssetList(String tablename,String mode,String searchword,String searchscope,String page,String c_no,String div,String ct_id) throws Exception {
		
		Statement st = null;
		ResultSet rs = null;

		com.anbtech.am.entity.AsInfoTable asInfoTable;
		ArrayList arrylist = new ArrayList();
		st = con.createStatement();

		int l_maxlist = 15;			// 한페이지내에 출력할 레코드 수
		int l_maxpage = 7;			// 페이지내에 표시할 바로가기 페이지의 수
		int l_maxsubjectlen = 30;	// 제목의 최대 표시길이

		String orderby = "";		// 정렬
		if(tablename.equals("as_info"))			{	orderby = "  ORDER BY as_no DESC";	} 
		else if(tablename.equals("as_history"))	{	orderby = "  ORDER BY h_no DESC ";	}

		int current_page_num =Integer.parseInt(page);
		String where=" WHERE as_status!='10' ";
		com.anbtech.am.business.AssetModuleBO assetModuleBO = new com.anbtech.am.business.AssetModuleBO(con);

			if(div.equals("detail") && mode.equals("asset_del_list")) {					// 폐기 자산 상세 검색
				where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
			}else if(div.equals("general") && mode.equals("asset_del_list")) {			// 폐기 자산 일반 검색
				where = assetModuleBO.getWhere4(c_no,searchword, searchscope, ct_id);
			}else if(div.equals("detail") && mode.equals("user_asset_list")) {			// 사용자 리스트 상세 검색
				where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
			}else if(div.equals("detail") && mode.equals("asset_list")) {				// 관리자 리스트 상세 검색
				where = assetModuleBO.getWhere3(c_no,mode,searchword,searchscope,ct_id,div); 
			}else if(div.equals("general")) {	// 일반 검색
				where = assetModuleBO.getWhere2(c_no,searchword, searchscope, ct_id,div);
			}else if("asset_del_list".equals(mode)) {
				where = assetModuleBO.getWhere4(c_no,searchword, searchscope, ct_id);
			}
		

		int total = getTotalCount(tablename, where);	// 전체 레코드 갯수
		total = 1;
		int recNum = total;

		String query = "SELECT * FROM as_info  "+ where + orderby;
		rs=st.executeQuery(query);
		
		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			asInfoTable = new com.anbtech.am.entity.AsInfoTable();
			asInfoTable.setAsNo(rs.getInt("as_no"));
			asInfoTable.setAsMid(rs.getString("as_mid"));
			asInfoTable.setCno(rs.getString("c_no"));
			asInfoTable.setWid(rs.getString("w_id"));
			asInfoTable.setWname(rs.getString("w_name"));
			asInfoTable.setBname(rs.getString("b_name"));
			asInfoTable.setModelName(rs.getString("model_name"));
			asInfoTable.setAsName(rs.getString("as_name"));
			asInfoTable.setAsSerial(rs.getString("as_serial"));
			asInfoTable.setBuyDate(rs.getString("buy_date"));
			asInfoTable.setAsPrice(rs.getString("as_price"));
			asInfoTable.setDcCount(rs.getString("dc_count"));

			asInfoTable.setApplyDcDate(rs.getString("apply_dcdate"));

			asInfoTable.setAsEachDc(rs.getString("as_each_dc"));
			//asInfoTable.setAsValue(assetModuleBO.makeWon(rs.getString("as_value")));
			asInfoTable.setAsValue(rs.getString("as_value"));			
			asInfoTable.setCrrId(rs.getString("crr_id"));
			asInfoTable.setCrrName(rs.getString("crr_name"));
			asInfoTable.setUid(rs.getString("u_id"));
			asInfoTable.setUname(rs.getString("u_name"));
			asInfoTable.setBuyWhere(rs.getString("buy_where"));
			asInfoTable.setAsMaker(rs.getString("as_maker"));
			asInfoTable.setAsSetting(rs.getString("as_setting"));
			asInfoTable.setBwTel(rs.getString("bw_tel"));
			asInfoTable.setBwAddress(rs.getString("bw_address"));
			asInfoTable.setBwEmployee(rs.getString("bw_employee"));
			asInfoTable.setBwMgrTel(rs.getString("bw_mgr_tel"));
			asInfoTable.setEtc(rs.getString("etc"));
			asInfoTable.setAsStatus(rs.getString("as_status"));
			asInfoTable.setAsStatusName(assetModuleBO.getStatname(rs.getString("as_status")));
			asInfoTable.setCtId(rs.getString("ct_id"));
			asInfoTable.setHandle(rs.getString("handle"));
			asInfoTable.setAsExceptDay(rs.getString("as_except_day"));
			asInfoTable.setAsExceptReason(rs.getString("as_except_reason"));
			asInfoTable.setNowStatus(assetModuleBO.getStatname(getNowStatus(""+rs.getInt("as_no"))));
		/*	asInfoTable.setFileSe(rs.getString("file_se"));
			asInfoTable.setFileName(rs.getString("file_name"));
			asInfoTable.setFileType(rs.getString("file_type"));
			asInfoTable.setFileSize(rs.getString("file_size"));
			asInfoTable.setFileUmask(rs.getString("file_umask"));
			asInfoTable.setFilePath(rs.getString("file_path"));*/
			arrylist.add(asInfoTable);
		}

		rs.close();
		st.close();

		return arrylist;
	}


	/********************************************************************
	 * Data 총 개수 가져오기
	 ********************************************************************/	
	public int getTotalCount(String tablename, String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM " + tablename + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));
		stmt.close();
		rs.close();

		return total_count;
	}

	/*****************************************************************************
	 *  해당 차량 예약일자 가져오기
	 *****************************************************************************/
	public ArrayList getSavedate(String as_no) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT count(*) FROM as_history WHERE as_no = '"+as_no+"'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);
		int j=0;
		if(rs.next()){
			j=rs.getInt(1);
		}

		ArrayList arr_list = new ArrayList();
		String str[]=new String[j*2];
		
		query = "SELECT u_date , tu_date FROM as_history WHERE (as_status='2' or as_status='3' or as_status='4' or as_status='5' or  as_status='7' or as_status='16') and as_no ='"+as_no+"'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);
				
		int i=0;
		while(rs.next()){

			String u_date=rs.getString("u_date");
			str[i] = u_date;
			arr_list.add(str[i]);

			String tu_date=rs.getString("tu_date")=="0"?u_date:rs.getString("tu_date");
			str[i+=1] = tu_date;
			arr_list.add(str[i]);
			i+=1;
		}
		rs.close();
		stmt.close();

		return arr_list;
			
	}

		
	// ***********************************// 
	// 자산 감가비율 적용( UPDATE )		  //
	// ***********************************// 
	public void valueUdate(int as_no, String dc_count, String as_value,String apply_dcdate) throws Exception {
		
		Statement stmt = null;
	
		String query = "UPDATE as_info SET as_value='"+as_value+"' , apply_dcdate='"+apply_dcdate+"' WHERE as_no='"+as_no+"'";
		stmt=con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}

	/*************************************** 
	 * 자산 반출/이관 신청 등록			   *
	 **************************************/ 
	public String[] saveAsHistory(String as_no, String m_id, String m_name, String m_rank,String write_id,String  write_name,String  write_rank,String  crr_id,String  crr_name,String crr_rank ,String takeout_reason ,String out_destination ,String as_status,String o_status ,String w_date ,String u_date ,String tu_date) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String type="2"; //  type ='1' 1차 결재만 타는건,  type='2' 2차 결재까지 타는건
		
		// 이관이면서 같은 부서일 경우 결재 코드를 1을 setting
		// ==> 이관시 같은 부서일경우 결재는 한번만 탄다.
	 	if(write_rank.equals(crr_rank) && o_status.equals("t"))	type = "1"; 
		
		String query = "INSERT INTO as_history (as_no,m_id,m_name,m_rank, w_id, w_name, w_rank, u_id, u_name,u_rank ,takeout_reason ,out_destination ,as_status,o_status ,write_date ,u_date ,tu_date,in_date,type) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
				
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,as_no);
		pstmt.setString(2,m_id);
		pstmt.setString(3,m_name);
		pstmt.setString(4,m_rank);
		pstmt.setString(5,write_id);
		pstmt.setString(6,write_name);
		pstmt.setString(7,write_rank);
		pstmt.setString(8,crr_id);
		pstmt.setString(9,crr_name);
		pstmt.setString(10,crr_rank);
		pstmt.setString(11,takeout_reason);
		pstmt.setString(12,out_destination);
		pstmt.setString(13,"1");
		pstmt.setString(14,o_status);
		pstmt.setString(15,w_date);
		pstmt.setString(16,u_date);
		pstmt.setString(17,tu_date);
		pstmt.setString(18,tu_date);
		pstmt.setString(19,type);
		
		pstmt.executeUpdate();
		pstmt.close();

		st = con.createStatement();
		query = " SELECT max(h_no) FROM as_history ";
		rs = st.executeQuery(query);
		String  hno_temp = "";
		if(rs.next()) hno_temp = ""+rs.getInt(1);
		
		query = "SELECT h_no, as_no, o_status, as_status FROM as_history WHERE h_no='"+hno_temp+"'";
		rs = st.executeQuery(query);
		String[] app =new String[4];
				
		if(rs.next()) {
			app[0] =  rs.getString("h_no");
			app[1] =  rs.getString("as_no");
			app[2] =  rs.getString("o_status");
			app[3] =  rs.getString("as_status");
		}
						
		rs.close();
		st.close();

		return app;
	}

	/********************************************* 
	* 자산 반출/이관 신청/등록 or 입고/반납 처리      *
	**********************************************/ 
	public void asManagerProcess(String u_id,String  as_no,String h_no,String o_status,String as_status,String mode) throws Exception {
	
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		String query="";	

		// as_history의 자산 책임자 정보를 가져온다.
		// 자산 반납 및 입고시 자산의 현재사용자를 자산의 책임자 정보로 변경하기 위해 책임자 정보를 SELECT 해온다.
		query = "SELECT crr_id, crr_name, crr_rank FROM as_info WHERE as_no='"+as_no+"'";
		rs = st.executeQuery(query);
		String temp_id   = "";
		String temp_name = "";
		String temp_rank = "";
		
		if (rs.next())
		{	temp_id   = rs.getString("crr_id");	
			temp_name = rs.getString("crr_name");	
			temp_rank = rs.getString("crr_rank");	
		}
		
			// 이관 처리
		if("transfer_process".equals(mode)){
		
			 // 사용자 정보 가져오기 
			 user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(u_id);
			 String u_name = user_info.getUserName();
			 String u_rank = user_info.getDivision();
			 u_name = u_id+"/"+u_name;
			
			// 자산 정보 업데이트
			query = "UPDATE as_info SET crr_id='"+u_id+"', crr_name='"+u_name+"', crr_rank ='"+u_rank+"',  u_id='"+u_id+"', u_name='"+u_name+"', u_rank='"+u_rank+"' WHERE as_no='"+as_no+"'"; 
			st.executeUpdate(query);

			// 자산 이력정보 업데이트 (as_status => '11' 이관완료!!!)
			query = "UPDATE as_history SET as_status='11' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);

			//  이관 취소
		}	else if("cancel_transfer".equals(mode)){
			query = "UPDATE as_history SET as_status='9' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);
		
			// 반출 처리
		}	else if("out_process".equals(mode)){
				
			// 사용자 정보(반출시 사용자 정보) 가져오기 ----------
			user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(u_id);
			String u_name = user_info.getUserName();
			String u_rank = user_info.getDivision();
			u_name = u_id+"/"+u_name;
			
			// 반출처리시 자산정보Table 현재 사용자(정보) 변경
			query = "UPDATE as_info SET u_id='"+u_id+"', u_name='"+u_name+"', u_rank='"+u_rank+"' WHERE as_no='"+as_no+"'";
			st.executeUpdate(query);

			//  자산 이력 정보 업데이트 
			query = "UPDATE as_history SET as_status='7' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);
		
			// 반출 취소
		}	else if("cancel_out".equals(mode)){
			query = "UPDATE as_history SET as_status='9' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);
		
			// 입고 처리
		} 	else if("out_Input".equals(mode)){
			
			// 입고시 수리 및 폐기시 자산 정보 업데이트 
			if(as_status.equals("10") || as_status.equals("13")){
			query  = "UPDATE as_info SET as_status ='"+as_status+"' WHERE as_no='"+as_no+"'";
			st.executeUpdate(query);
			} 
			
			// 자산 입고 처리시 현재 자산 사용자를 관리자 이름으로 변경(반납)한다.
			query = "UPDATE as_info SET u_id='"+temp_id+"', u_name='"+temp_name+"', u_rank='"+temp_rank+"' WHERE as_no = '"+as_no+"'";
			st.executeUpdate(query);
			
			// 자산 이력 정보 업데이트
			query = "UPDATE as_history SET as_status='12' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);
		
			// 자산 대여 처리
		}	else if("lending_process".equals(mode)){
			
			// 사용자 정보 가져오기 
			user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(u_id);
			String u_name = user_info.getUserName();
			String u_rank = user_info.getDivision();
			u_name = u_id+"/"+u_name;

			// 대여 처리시 자산정보Table 현재 사용자(정보) 변경
			query = "UPDATE as_info SET u_id='"+u_id+"', u_name='"+u_name+"', u_rank='"+u_rank+"' WHERE as_no='"+as_no+"'";
			st.executeUpdate(query);
			
			// 대여 처리시 자산 이력 정보 변경
			query = "UPDATE as_history SET as_status='16' WHERE h_no='"+h_no+"'";
			st.executeUpdate(query);
		
			// 자산 반납 처리
		}	else if("lending_input".equals(mode)){

			// 자산 반납 처리시 현재 자산 사용자를 관리자 이름으로 변경(반납)한다.
			query = "UPDATE as_info SET u_id='"+temp_id+"', u_name='"+temp_name+"', u_rank='"+temp_rank+"' WHERE as_no = '"+as_no+"'";
			st.executeUpdate(query);
			
			// 자산 이력 정보 업데이트
			query = "UPDATE as_history SET as_status='17' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);

			// 자산 대여 취소
		}	else if("cancel_lending".equals(mode)){
			query = "UPDATE as_history SET as_status='9' WHERE h_no='"+h_no+"'"; 
			st.executeUpdate(query);
		}
		
		rs.close();
		st.close();

	}

	/*****************************************
	 *     이관 가능 여부 판단			
	 *  #. 현재 이관신청을 하거나 이관결재처리중인 
	 *     문건이 있는지 확인
	 *	#. 날짜 판단
	 ***************************************
	public boolean transEnable(String udate, String as_no) throws Exception {
	
		// 현재 날짜
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		
		String crr_date = anbdt.getDateNoformat();	
		
		int udate_int = Integer.parseInt(udate);
		boolean bool = false;
		String query = "";
		st = con.createStatement();
		//query = "SELECT count(as_no) FROM as_info WHERE as_status=";

		query = "SELECT u_date FROM as_history WHERE and as_no='"+as_no+"' and ((type='1' and as_status='3') or (type='2' and as_status='5'))";
		rs = st.executeQuery(query);

		while(rs.next()){
			int date = Integer.parseInt(rs.getString("u_date"));
			if(udate_int > date) { bool = true; break; }
		}

		return bool;
	}*/

	/*****************************************************************************
	 * 전자결재모듈의 app_save 테이블에서 선택된 pid의 레코드 정보를 가져온 뒤,
	 * 승인원 관리모듈의 approval_info 테이블에 입력한다.
	 ****************************************************************************/
	public void getAppinfoAndSave(String pid) throws Exception{
		
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM app_save WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			query = "INSERT INTO am_app_save (pid,writer,writer_name,writer_div,writer_rank,write_date,";
			query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
			query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
			query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
			query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
			query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
			query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
			query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date) ";
			query += "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(query);
			pstmt.setString(1,pid);
			pstmt.setString(2,rs.getString("writer"));
			pstmt.setString(3,rs.getString("writer_name"));
			pstmt.setString(4,rs.getString("writer_div"));
			pstmt.setString(5,rs.getString("writer_rank"));
			pstmt.setString(6,rs.getString("write_date"));
			pstmt.setString(7,rs.getString("reviewer"));
			pstmt.setString(8,rs.getString("reviewer_name"));
			pstmt.setString(9,rs.getString("reviewer_div"));
			pstmt.setString(10,rs.getString("reviewer_rank"));
			pstmt.setString(11,rs.getString("review_comment"));
			pstmt.setString(12,rs.getString("review_date"));
			pstmt.setString(13,rs.getString("decision"));
			pstmt.setString(14,rs.getString("decision_name"));
			pstmt.setString(15,rs.getString("decision_div"));
			pstmt.setString(16,rs.getString("decision_rank"));
			pstmt.setString(17,rs.getString("decision_comment"));
			pstmt.setString(18,rs.getString("decision_date"));
			pstmt.setString(19,rs.getString("agree"));
			pstmt.setString(20,rs.getString("agree_name"));
			pstmt.setString(21,rs.getString("agree_div"));
			pstmt.setString(22,rs.getString("agree_rank"));
			pstmt.setString(23,rs.getString("agree_comment"));
			pstmt.setString(24,rs.getString("agree_date"));
			pstmt.setString(25,rs.getString("agree2"));
			pstmt.setString(26,rs.getString("agree2_name"));
			pstmt.setString(27,rs.getString("agree2_div"));
			pstmt.setString(28,rs.getString("agree2_rank"));
			pstmt.setString(29,rs.getString("agree2_comment"));
			pstmt.setString(30,rs.getString("agree2_date"));
			pstmt.setString(31,rs.getString("agree3"));
			pstmt.setString(32,rs.getString("agree3_name"));
			pstmt.setString(33,rs.getString("agree3_div"));
			pstmt.setString(34,rs.getString("agree3_rank"));
			pstmt.setString(35,rs.getString("agree3_comment"));
			pstmt.setString(36,rs.getString("agree3_date"));
			pstmt.setString(37,rs.getString("agree4"));
			pstmt.setString(38,rs.getString("agree4_name"));
			pstmt.setString(39,rs.getString("agree4_div"));
			pstmt.setString(40,rs.getString("agree4_rank"));
			pstmt.setString(41,rs.getString("agree4_comment"));
			pstmt.setString(42,rs.getString("agree4_date"));
			pstmt.setString(43,rs.getString("agree5"));
			pstmt.setString(44,rs.getString("agree5_name"));
			pstmt.setString(45,rs.getString("agree5_div"));
			pstmt.setString(46,rs.getString("agree5_rank"));
			pstmt.setString(47,rs.getString("agree5_comment"));
			pstmt.setString(48,rs.getString("agree5_date"));
		
			pstmt.executeUpdate();
			pstmt.close();
		}
		stmt.close();
		rs.close();
	}

	// ***********************************// 
	// ** 결재 번호 (pid) 저장  **********//
	// ***********************************// 
	public void setAid(String pid, String h_no,String app) throws Exception {
	
		Statement nst = null;

		nst = con.createStatement();
		String query = "";
		if(app.equals("1")) query = "UPDATE as_history SET pid='"+pid+"' WHERE h_no='"+h_no+"'";
		else query = "UPDATE as_history SET pid2='"+pid+"' WHERE h_no='"+h_no+"'";

		nst.executeUpdate(query);
		nst.close();
	}

	// ***********************************// 
	// ** 결재 처리 코드 변경(update) ****//
	// ***********************************// 
	public void updateStatus(String h_no,String status,String field, String pid) throws Exception{
		
		Statement nst = null;

		String query = "";
		nst = con.createStatement();
		if(field.equals("")){
			query =" UPDATE as_history SET as_status ='"+status+"' WHERE h_no='"+h_no+"'";
		} else {
			query =" UPDATE as_history SET as_status ='"+status+"', "+field+"='"+pid+"' WHERE h_no='"+h_no+"'";
		}
	
		nst.executeUpdate(query);
		nst.close();
	}

	//**********************************************************************//
	// approval_info 테이블에서 선택된 관리번호(pid)의 결재정보를 가져온다. //
	// *********************************************************************//
	public AsApprovalInfoTable getApprovalInfo(String pid,String sign_path) throws Exception{
		AsApprovalInfoTable table = new AsApprovalInfoTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT writer,writer_name,writer_div,writer_rank,write_date,";
		query += "reviewer,reviewer_name,reviewer_div,reviewer_rank,review_comment,review_date,";
		query += "decision,decision_name,decision_div,decision_rank,decision_comment,decision_date,";
		query += "agree,agree_name,agree_div,agree_rank,agree_comment,agree_date,";
		query += "agree2,agree2_name,agree2_div,agree2_rank,agree2_comment,agree2_date,";
		query += "agree3,agree3_name,agree3_div,agree3_rank,agree3_comment,agree3_date,";
		query += "agree4,agree4_name,agree4_div,agree4_rank,agree4_comment,agree4_date,";
		query += "agree5,agree5_name,agree5_div,agree5_rank,agree5_comment,agree5_date ";
		query += "FROM am_app_save WHERE pid='" + pid + "'";

		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		String sign_src = "";
		String memo = "";
		while(rs.next()){
			table = new AsApprovalInfoTable();

			table.setWriterName(rs.getString("writer_name"));
			String writer = rs.getString("writer");
			sign_src = "<img src = '" + sign_path + writer + ".gif' border='0'>";
			table.setWriterSig(sign_src);

			String reviewer = rs.getString("reviewer");
			if(reviewer == null || reviewer.equals("")){
				sign_src = "<img src = '" + sign_path + "wan.gif' border='0'>";			
				table.setReviewerName("");
			}else{
				sign_src = "<img src = '" + sign_path + reviewer + ".gif' border='0'>";
				table.setReviewerName(rs.getString("reviewer_name"));
			}
			table.setReviewerSig(sign_src);

			table.setDecisionName(rs.getString("decision_name"));
			String decision = rs.getString("decision");
			sign_src = "<img src = '" + sign_path + decision + ".gif' border='0'>";
			table.setDecisionSig(sign_src);

			memo = "기안 " + writer + " " + rs.getString("writer_name") + " " + rs.getString("writer_rank") + " " + rs.getString("writer_div") + " " + rs.getString("write_date") + "\n";
			if(!reviewer.equals("")){
				memo += "검토 " + reviewer + " " + rs.getString("reviewer_name") + " " + rs.getString("reviewer_rank") + " " + rs.getString("reviewer_div") + " " + rs.getString("review_date") + " " + rs.getString("review_comment") + "\n";
			}
			memo += "승인 " + decision + " " + rs.getString("decision_name") + " " + rs.getString("decision_rank") + " " + rs.getString("decision_div") + " " + rs.getString("decision_date") + " " + rs.getString("decision_comment") + "\n";

			String agree = rs.getString("agree");
			if(agree != null && !agree.equals("")) memo += "합의 " + agree + " " + rs.getString("agree_name") + " " + rs.getString("agree_rank") + " " + rs.getString("agree_div") + " " + rs.getString("agree_date") + " " + rs.getString("agree_comment") + "\n";

			String agree2 = rs.getString("agree2");
			if(agree2 != null && !agree2.equals("")) memo += "합의 " + agree2 + " " + rs.getString("agree2_name") + " " + rs.getString("agree2_rank") + " " + rs.getString("agree2_div") + " " + rs.getString("agree2_date") + " " + rs.getString("agree2_comment") + "\n";

			String agree3 = rs.getString("agree3");
			if(agree3 != null && !agree3.equals("")) memo += "합의 " + agree3 + " " + rs.getString("agree3_name") + " " + rs.getString("agree3_rank") + " " + rs.getString("agree3_div") + " " + rs.getString("agree3_date") + " " + rs.getString("agree3_comment") + "\n";

			String agree4 = rs.getString("agree4");
			if(agree4 != null && !agree4.equals("")) memo += "합의 " + agree4 + " " + rs.getString("agree4_name") + " " + rs.getString("agree4_rank") + " " + rs.getString("agree4_div") + " " + rs.getString("agree4_date") + " " + rs.getString("agree4_comment") + "\n";

			String agree5 = rs.getString("agree5");
			if(agree5 != null && !agree5.equals("")) memo += "합의 " + agree5 + " " + rs.getString("agree5_name") + " " + rs.getString("agree5_rank") + " " + rs.getString("agree5_div") + " " + rs.getString("agree5_date") + " " + rs.getString("agree5_comment") + "\n";

			table.setMemo(memo);

		}
		
		rs.close();
		stmt.close();
		return table;
	}
	
	// ***********************************// 
	// ** pid 가져오기					  //
	// ***********************************// 
	public String getPid(String h_no) throws Exception {
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT pid,pid2 FROM as_history WHERE h_no='"+h_no+"'";
		rs = st.executeQuery(query);
		String pid = "";

		if(rs.next()) pid = rs.getString("pid");
		
		rs.close();
		st.close();
		
		return pid;		
	}

	// ***********************************// 
	// ** pid2 가져오기					  //
	// ***********************************// 
	public String getPid2(String h_no) throws Exception {
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT pid,pid2 FROM as_history WHERE h_no='"+h_no+"'";
		rs = st.executeQuery(query);
		String pid = "";
		String pid2 = "";
		if(rs.next()){
			pid = rs.getString("pid");
			pid2 = rs.getString("pid2");
		}
		
		rs.close();
		st.close();
		
		if(pid2.equals("empty")) {
			return pid;
		} else {
			return pid2;
		}
		
	}
		
	// ***********************************// 
	// ** 결재건 상신 취소시 삭제  *******//
	// ***********************************// 
	public void delete(String h_no) throws Exception {
		
		Statement st = null;

		st = con.createStatement();
		String query ="DELETE as_history WHERE h_no='"+h_no+"'";
		st.executeUpdate(query);
		st.close();
	}

	/************************************ 
	* 총 자산 금액						*
	* 폐기자산(as_status CODE가 10인 자산)을
	* 제외한 모든 자산의 자산 액수 구하기.
	************************************/ 
	public String sumAsValue() throws Exception {
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		String query ="SELECT as_value FROM as_info WHERE as_status!='10'";
		rs = st.executeQuery(query);	
		long sum=0;
		while(rs.next()){
			sum+=(Long.parseLong(rs.getString("as_value")));
		}

		st.close();
		rs.close();
		return sum+"";
	}

	// ***********************************// 
	// ** 자원관리 권한 승인              //
	// ***********************************//
	public boolean chk_privilege(String login_id,String div) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;
		st = con.createStatement();
		String query ="";
		if(div.equals("lending")) {
			query = "SELECT owner FROM prg_privilege WHERE code_s='AM02'";	
		}else {
			query = "SELECT owner FROM prg_privilege WHERE code_s='AM01'";	
		}

		rs = st.executeQuery(query);
		String str="";
		if(rs.next()){
			str=rs.getString("owner");
		}

		java.util.StringTokenizer stn = new java.util.StringTokenizer(str,";");
		boolean pri=false;
			while(stn.hasMoreTokens()){
				if(stn.nextToken().equals(login_id)) 	pri=true;
			}
		st.close();
		rs.close();

		return pri;
		
	}

	/************************************/
	/* 자산의 관리자 ID 가져오기		*/	 
	/************************************/
	public String getManagerId(String as_no) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT crr_id FROM as_info WHERE as_no='"+as_no+"'";
		rs =  st.executeQuery(query);
		String m_id="";
		if(rs.next()) m_id = rs.getString("crr_id");
		rs.close();
		st.close();

		return m_id;
	}


	////////////// saveMAIL ///////////////
	/*****************************************************************
	 * 대출결과 통보용 개인우편 발송을 위한 내용을 DB에 저장한다.
	 *****************************************************************/
	public void saveEmail(String login_id,String requestor,String bon_path,String filename) throws Exception{

		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd a hh:mm");
		String w_time = vans.format(now);

		String pid = "doc" + System.currentTimeMillis();

		com.anbtech.dms.db.AccessControlDAO ac = new com.anbtech.dms.db.AccessControlDAO(con);
		String requestor_s = requestor + "/" + ac.getUserName(requestor);
		String decision_name = ac.getUserName(login_id);

		//post_master 테이블 입력
		String query = "INSERT INTO post_master";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,bon_path,bon_file) ";
		query += "VALUES (?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"반출/이관/대여 처리결과 통보");
		pstmt.setString(3,login_id);
		pstmt.setString(4,decision_name);
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor_s);
		pstmt.setString(7,"0");
		pstmt.setString(8,"email");
		pstmt.setString(9,bon_path);
		pstmt.setString(10,filename);
		
		pstmt.executeUpdate();


		//post_letter 테이블 입력
		query = "INSERT INTO post_letter";
		query += "(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen) ";
		query += "VALUES (?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,pid);
		pstmt.setString(2,"반출/이관/대여 처리결과 통보");
		pstmt.setString(3,login_id);
		pstmt.setString(4,decision_name);
		pstmt.setString(5,w_time);
		pstmt.setString(6,requestor);
		pstmt.setString(7,"0");
		
		pstmt.executeUpdate();
		pstmt.close();

	} //saveEmail()*/

	//***********************************//
	// 결재건에 따른 관리번호 가져오기	 //
	//***********************************//
	public String getAsNo(String h_no) throws Exception {
	
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT as_no FROM as_history WHERE h_no='"+h_no+"'";
		rs = st.executeQuery(query);
		String as_no="";
		if(rs.next()) as_no=rs.getString("as_no");

		rs.close();
		st.close();

		return as_no;
	}

	// *********************************** //
	// 승인권자 이름 가져오기			   //
	// *********************************** //
	public String getDecision(String h_no) throws Exception {
		
		Statement nst = null;
		ResultSet nrs = null;

		nst = con.createStatement();
		String query = "SELECT w_id FROM as_history WHERE h_no='"+h_no+"'";
		nrs = nst.executeQuery(query);
		String w_id = "";

		if(nrs.next()) w_id = nrs.getString("w_id");
		nrs.close();
		nst.close();

		return w_id;
	}

	// ******************************************//
	// 결재 형태 (1차만 맡는지, 2차까지 맡는지..)//
	// ******************************************//
	public String getType(String pid) throws Exception {
		Statement st = null;
		ResultSet rs = null;

		st = con.createStatement();
		String query = "SELECT type FROM as_history WHERE pid='"+pid+"' or pid2='"+pid+"'";
		rs = st.executeQuery(query);
		String type = "";
		if(rs.next()) type = rs.getString("type");
		rs.close();
		st.close();

		return type;
		
	}

	/********************************************
	*	폐기된 자원 복구							*
	*	as_info TABLE의 '폐기상태'CODE('10')을 
	*   정상 CODE('6')로 변경
	*********************************************/
	public void assetRepair(String as_no) throws Exception {
		Statement st = null;
	
		st = con.createStatement();
		String query = "UPDATE as_info SET as_status='6' WHERE as_no='"+as_no+"'";
		st.executeUpdate(query);
		st.close();
	}

	
	/********************************************
	*	쓰레기 정보 정리							*
	*********************************************/
	public void cleanDB() throws Exception {
		Statement st = null;

		st = con.createStatement();
		String query = "DELETE FROM as_history WHERE as_status='1'";
		st.executeUpdate(query);
		st.close();
	}

	
	// test 용 method
	public void testPrint(String pid, String h_no, String mode, String app,String decision) throws Exception {
		Statement st = null;

		st = con.createStatement();
		String query = "UPDATE as_history SET as_statusinfo='"+pid+";"+h_no+";"+mode+";"+app+";"+decision+"' WHERE h_no='2'";
		st.executeUpdate(query);
		st.close();
	}
}