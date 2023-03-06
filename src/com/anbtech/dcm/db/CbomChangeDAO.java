package com.anbtech.dcm.db;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class CbomChangeDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();//문자
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();//문자
	private String query = "";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public CbomChangeDAO(Connection con) 
	{
		this.con = con;
	}
	//--------------------------------------------------------------------
	//
	//		설계변경 내용 읽기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 설계변경에 해당되는 BOM변경 내용 출력하기
	//*******************************************************************/	
	public ArrayList readEccBomList(String eco_no,String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	

			String pcode = rs.getString("parent_code");
			String app_status = rs.getString("app_status");
			if(app_status.equals("0"))		//편집가능
					pcode = "<a href=\"javascript:strView('"+rs.getString("pid")+"','"+rs.getString("gid")+"');\">"+rs.getString("parent_code")+"</a>";
			table.setParentCode(pcode);	

			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 변경된 내용을 다시 읽어 편집[수정/삭제]하고자 할때
	//		
	//*******************************************************************/	
	public ArrayList readEccBomItem(String pid,String gid,String eco_no) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();

		//해당되는 chg_order을 구해 짝으로 읽어낸다.[RB,RA / A / D]
		String where = "where gid='"+gid+"' and pid='"+pid+"' and eco_no='"+eco_no+"'";
		String chg_order = getColumData("ecc_bom","chg_order",where);
		
		query = "SELECT * FROM ecc_bom where chg_order ='"+chg_order+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 설계변경에 해당되는 BOM변경 내용 출력하기 : 부품비교을 위해
	//*******************************************************************/	
	public ArrayList getEccBomList(String eco_no,String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by parent_code,child_code desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			table = new eccBomTable();
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setChangeDate(rs.getString("change_date"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setEccReason(rs.getString("ecc_reason"));
			table.setNote(rs.getString("note"));
			table.setChgOrder(rs.getString("chg_order"));
			table.setAppStatus(rs.getString("app_status"));
			table_list.add(table);
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		설계변경 출력물 만들기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 대표변경모델기준의 변경부품 출력 : BOM변경 내역서 출력을 위해
	//*******************************************************************/	
	public ArrayList outEccBomList(String eco_no,String gid) throws Exception
	{
		//변수 초기화
		String op_code="";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by chg_order,adtag desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			//신규및 삭제 부품의 경우 빈공란을 하나더 출력한다.
			String tag = rs.getString("adtag");
			if(tag.equals("A") || tag.equals("D")) {
				//첫라인은 공백으로
				table = new eccBomTable();
				table.setPid("");	
				table.setGid("");	
				table.setParentCode("");	
				table.setChildCode("");	
				table.setLevelNo("");	
				table.setPartName("");	
				table.setPartSpec("");	
				table.setLocation("");	
				table.setOpCode("");	
				table.setQtyUnit("");	
				table.setQty("");	
				table.setMakerName("");	
				table.setMakerCode("");	
				table.setPriceUnit("");	
				table.setPrice("");
				table.setAddDate("");	
				table.setBuyType("");	
				table.setEcoNo("");	
				table.setAdTag("");	
				table.setChangeDate("");	
				table.setBomStartDate("");	
				table.setBomEndDate("");
				table.setEccReason("");
				table.setNote("");
				table.setChgOrder("");
				table.setAppStatus("");
				table_list.add(table);

				//다음라인엔 데이터를
				table = new eccBomTable();
				table.setPid(rs.getString("pid"));	
				table.setGid(rs.getString("gid"));	
				table.setParentCode(rs.getString("parent_code"));	
				table.setChildCode(rs.getString("child_code"));	
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartName(rs.getString("part_name"));	
				table.setPartSpec(rs.getString("part_spec"));	
				table.setLocation(rs.getString("location"));
				op_code = rs.getString("op_code"); if(op_code.length() == 0) op_code="&nbsp;";
				table.setOpCode(op_code);	
				table.setQtyUnit(rs.getString("qty_unit"));	
				table.setQty(rs.getString("qty"));	
				table.setMakerName(rs.getString("maker_name"));	
				table.setMakerCode(rs.getString("maker_code"));	
				table.setPriceUnit(rs.getString("price_unit"));	
				table.setPrice(rs.getString("price"));
				table.setAddDate(rs.getString("add_date"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setEcoNo(rs.getString("eco_no"));	
				table.setAdTag(rs.getString("adtag"));	
				table.setChangeDate(rs.getString("change_date"));	
				table.setBomStartDate(rs.getString("bom_start_date"));	
				table.setBomEndDate(rs.getString("bom_end_date"));
				table.setEccReason(rs.getString("ecc_reason"));
				table.setNote(rs.getString("note"));
				table.setChgOrder(rs.getString("chg_order"));
				table.setAppStatus(rs.getString("app_status"));
				table_list.add(table);
			} else {
				table = new eccBomTable();
				table.setPid(rs.getString("pid"));	
				table.setGid(rs.getString("gid"));	
				table.setParentCode(rs.getString("parent_code"));	
				table.setChildCode(rs.getString("child_code"));	
				table.setLevelNo(rs.getString("level_no"));	
				table.setPartName(rs.getString("part_name"));	
				table.setPartSpec(rs.getString("part_spec"));	
				table.setLocation(rs.getString("location"));	
				op_code = rs.getString("op_code"); if(op_code.length() == 0) op_code="&nbsp;";
				table.setOpCode(op_code);		
				table.setQtyUnit(rs.getString("qty_unit"));	
				table.setQty(rs.getString("qty"));	
				table.setMakerName(rs.getString("maker_name"));	
				table.setMakerCode(rs.getString("maker_code"));	
				table.setPriceUnit(rs.getString("price_unit"));	
				table.setPrice(rs.getString("price"));
				table.setAddDate(rs.getString("add_date"));	
				table.setBuyType(rs.getString("buy_type"));	
				table.setEcoNo(rs.getString("eco_no"));	
				table.setAdTag(rs.getString("adtag"));	
				table.setChangeDate(rs.getString("change_date"));	
				table.setBomStartDate(rs.getString("bom_start_date"));	
				table.setBomEndDate(rs.getString("bom_end_date"));
				table.setEccReason(rs.getString("ecc_reason"));
				table.setNote(rs.getString("note"));
				table.setChgOrder(rs.getString("chg_order"));
				table.setAppStatus(rs.getString("app_status"));
				table_list.add(table);
			}
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 대표변경모델기준의 변경부품 출력 : BOM변경 내역서 출력을 위해
	//*******************************************************************/	
	public ArrayList outEccBomParentList(String eco_no,String gid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"' and gid='"+gid+"'";
		query += " order by parent_code,child_code desc";
		rs = stmt.executeQuery(query);
		
		ArrayList table_list = new ArrayList();
		while(rs.next()) { 
			String tag = rs.getString("adtag");
			if(tag.equals("A") || tag.equals("D") || tag.equals("RB")) {
				table = new eccBomTable();
				table.setParentCode(rs.getString("parent_code"));	
				table.setPartSpec(getComponentSpec(rs.getString("parent_code")));	
				table_list.add(table);
			}
		} 

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MBOM_MASTER정보 읽기
	//*******************************************************************/	
	public mbomMasterTable readMasterItem(String fg_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomMasterTable table = new com.anbtech.bm.entity.mbomMasterTable();
		
		query = "SELECT * FROM mbom_master where fg_code ='"+fg_code+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setPdCode(rs.getString("pd_code"));	
			table.setPdName(rs.getString("pd_name"));	
			table.setPjtCode(rs.getString("pjt_code"));	
			table.setPjtName(rs.getString("pjt_name"));	
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setRegDate(rs.getString("reg_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppName(rs.getString("app_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setBomStatus(rs.getString("bom_status"));	
			table.setAppNo(rs.getString("app_no"));	
			table.setMStatus(rs.getString("m_status"));	
			table.setPurpose(rs.getString("purpose"));	
		} else {
			table.setPid("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setPdCode("");	
			table.setPdName("");	
			table.setPjtCode("");	
			table.setPjtName("");	
			table.setRegId("");	
			table.setRegName("");	
			table.setRegDate("");	
			table.setAppId("");	
			table.setAppName("");	
			table.setAppDate("");	
			table.setBomStatus("");	
			table.setAppNo("");	
			table.setMStatus("");	
			table.setPurpose("0");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//--------------------------------------------------------------------
	//
	//		BOM STRUCTURE 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MBOM_MASTER정보 읽기
	//*******************************************************************/	
	public mbomStrTable readStrItem(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mbomStrTable table = new com.anbtech.bm.entity.mbomStrTable();
		
		query = "SELECT * FROM mbom_str where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setGid(rs.getString("gid"));	
			table.setParentCode(rs.getString("parent_code"));	
			table.setChildCode(rs.getString("child_code"));	
			table.setLevelNo(rs.getString("level_no"));	
			table.setPartName(rs.getString("part_name"));	
			table.setPartSpec(rs.getString("part_spec"));	
			table.setLocation(rs.getString("location"));	
			table.setOpCode(rs.getString("op_code"));	
			table.setQtyUnit(rs.getString("qty_unit"));	
			table.setQty(rs.getString("qty"));	
			table.setMakerName(rs.getString("maker_name"));	
			table.setMakerCode(rs.getString("maker_code"));	
			table.setPriceUnit(rs.getString("price_unit"));	
			table.setPrice(rs.getString("price"));
			table.setAddDate(rs.getString("add_date"));	
			table.setBuyType(rs.getString("buy_type"));	
			table.setEcoNo(rs.getString("eco_no"));	
			table.setAdTag(rs.getString("adtag"));	
			table.setBomStartDate(rs.getString("bom_start_date"));	
			table.setBomEndDate(rs.getString("bom_end_date"));
			table.setAppStatus(rs.getString("app_status"));
			table.setTag(rs.getString("tag"));
			table.setAssyDup(rs.getString("assy_dup"));
			String part_type = getPartType(rs.getString("gid"),rs.getString("pid"),rs.getString("child_code"),rs.getString("level_no"));
			table.setPartType(part_type);
			table.setItemType(rs.getString("item_type"));
		} else {
			table.setPid("");	
			table.setGid("");	
			table.setParentCode("");	
			table.setChildCode("");	
			table.setLevelNo("");	
			table.setPartName("");	
			table.setPartSpec("");	
			table.setLocation("");	
			table.setOpCode("");	
			table.setQtyUnit("");	
			table.setQty("");	
			table.setMakerName("");	
			table.setMakerCode("");	
			table.setPriceUnit("");	
			table.setPrice("");
			table.setAddDate("");	
			table.setBuyType("");	
			table.setEcoNo("");	
			table.setAdTag("");	
			table.setBomStartDate("0");	
			table.setBomEndDate("0");
			table.setAppStatus("0");
			table.setTag("0");
			table.setAssyDup("");
			table.setPartType("P");		//P:부품, A:Assy
			table.setItemType("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}
	//--------------------------------------------------------------------
	//
	//		부품 마스터에서 필요한 정보 쿼리하기
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	//	품목코드에 해당되는 정보쿼리하기
	// [부품이름,규격,메이커명,형명,가격단위,가격] 
	// 현재 : 규격만 사용
	//*******************************************************************/
	public String[] getComponentInfo(String part_code) throws Exception
	{
		//변수 초기화
		String[] data = new String[6];
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		for(int i=0; i<6; i++) data[i] = "";		//배열초기화
		query = "SELECT * FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data[0] = rs.getString("item_name");	if(data[0] == null) data[0] = "원자재";
			data[1] = str.repWord(rs.getString("item_desc"),"'","`");
			data[2] = rs.getString("mfg_no");		if(data[2] == null) data[2] = "";
			data[3] = "";
			data[4] = rs.getString("stock_unit");	if(data[4] == null) data[4] = "EA";
			data[5] = rs.getString("item_type");	if(data[5] == null) data[5] = "4";
		}
		
		stmt.close();
		rs.close();
		return data;			
	}
	//*******************************************************************
	//	해당 품목코드의 SPEC정보 구하기
	//*******************************************************************/
	public String getComponentSpec(String part_code) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		query = "SELECT item_desc FROM item_master WHERE item_no='"+part_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("item_desc");
			
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	품목 코드 검색하기
	//*******************************************************************/
	public ArrayList getComponentCode(String sWord) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		
		//품목코드 찾기
		query = "SELECT item_no,item_desc FROM item_master WHERE item_no like '"+sWord+"%' order by item_no asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new mbomStrTable();	
				table.setParentCode(rs.getString("item_no"));
				table.setPartSpec(rs.getString("item_desc"));
				table_list.add(table);
		}

		stmt.close();
		rs.close();
		return table_list;			
	}

	
	//--------------------------------------------------------------------
	//
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	//	MBOM_STR에서 해당부품이 PART인지 ASSY코드인지 판단하기 : 하부구조 있나 없나
	//  편집시 삭제를 위한 조건으로 사용 [하부구조가 없으면 삭제 가능토록]
	//*******************************************************************/
	public String getPartType(String gid,String pid,String child_code,String level_no) throws Exception
	{
		//변수 초기화
		String part_type = "A";		//Assy코드임
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select count(*) from mbom_str where gid ='"+gid+"' and parent_code='"+child_code+"'";
		query += " and level_no > '"+level_no+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			int cnt = rs.getInt(1);
			if(cnt == 0) part_type = "P";	//부품임
		}

		//중복Assy 표시 [assy_dup='D']인 경우는 제외 : 부품으로 인식토록 하여 삭제할 수 있도록 함.
		query = "select assy_dup from mbom_str where gid='"+gid+"' and pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			String assy_dup = rs.getString("assy_dup");
			if(assy_dup.equals("D")) part_type="P";
		}
	
		stmt.close();
		rs.close();
		return part_type;			
	}
	//*******************************************************************
	//	MBOM_STR에서 LEVEL_NO구하기
	//*******************************************************************/
	public int getLevelNo(String query) throws Exception
	{
		//변수 초기화
		int level_no = 0;
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			level_no = Integer.parseInt(rs.getString("level_no"));
			level_no++;		//해당레벨에서 +1을하여 돌려준다
		}
		
		stmt.close();
		rs.close();
		return level_no;			
	}

	//*******************************************************************
	//	수량 파악하기 
	//*******************************************************************/
	public int getTotalCount(String query) throws Exception
	{
		//변수 초기화
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}
	
	//*******************************************************************
	// SQL update 실행하기
	//*******************************************************************/	
	public void executeUpdate(String update) throws Exception
	{
		Statement stmt = con.createStatement();
		stmt.executeUpdate(update);
		stmt.close();
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String getColumData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			data = rs.getString(getcolumn);
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	주어진 테이블에서 주어진 조건의 컬럼의 데이터 읽기
	//*******************************************************************/
	public String[] getColumArrayData(String tablename,String getcolumn,String where) throws Exception
	{
		//변수 초기화
		String[] data = null;
		Statement stmt = con.createStatement();
		ResultSet rs = null;

		//갯수 파악하기
		query = "select count(*) from "+tablename+" "+where;
		int cnt = getTotalCount(query);
		if(cnt == 0) return data;
		data = new String[cnt];
		
		query = "select "+getcolumn+" from "+tablename+" "+where;
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) {
			data[n] = rs.getString(getcolumn);
			n++;
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

	//*******************************************************************
	//	ECC_BOM에서 출력순서[chg_order]구하기
	//*******************************************************************/
	public String getChgOrder(String query) throws Exception
	{
		//변수 초기화
		nfm.setFormat("0000");
		String chg_order = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		rs = stmt.executeQuery(query);
		if(rs.next()) chg_order = rs.getString("chg_order");
		
		//찾은 순서에서 +1
		if(chg_order.length() == 0) {
			chg_order = nfm.toDigits(1);
		} else {
			chg_order = nfm.toDigits(Integer.parseInt(chg_order)+1);
		}
		
		stmt.close();
		rs.close();
		return chg_order;			
	}
}


