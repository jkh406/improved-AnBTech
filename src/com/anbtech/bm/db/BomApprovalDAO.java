package com.anbtech.bm.db;
import com.anbtech.bm.entity.*;
import com.anbtech.dcm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class BomApprovalDAO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.bm.business.BmToMmBO btmBO = null;				//MBOM_STR -> MBOM_ITEM I/F
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query="", update="";
	private ArrayList item_list = null;				//PART정보를 ArrayList에 담기
	private mbomStrTable mst = null;				//help class
	private String assy_head = "[1,F]";				//Assy코드만 쿼리시 사용 1:Assy선두문자, F:FG코드 선두문자
	private String phantom_head = "1PH";			//Phantom Assy
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomApprovalDAO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
		btmBO = new com.anbtech.bm.business.BmToMmBO(con);
	}
	//--------------------------------------------------------------------
	//
	//		상태 정보 반영에 관련된 메소드
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 초기BOM구성 <BOM진행상태 변경하기 : MBOM MASTER에 대해서>
	//*******************************************************************/	
	public void setBomStatus(String pid,String bom_status,String app_id,String app_name,String app_no) throws Exception
	{
		//MBOM_MASTER에 BOM 상태 변경하기
		update = "UPDATE MBOM_MASTER set bom_status='"+bom_status+"',app_id='"+app_id;
		update += "',app_name='"+app_name+"',app_date='"+anbdt.getDateNoformat();
		update += "', app_no='"+app_no+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//MBOM_STR에 결재상태 변경하기
		if(bom_status.equals("5")) {		//BOM승인
			update = "UPDATE MBOM_STR set app_status='1' where gid='"+pid+"'";
			modDAO.executeUpdate(update);

			//MBOM_STR --> MBOM_ITEM I/F처리하기 (BOM에서 생산모듈 I/F처리)
			btmBO.tranceBOMList(pid);
		} else {							//그외
			update = "UPDATE MBOM_STR set app_status='0' where gid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

	}

	//*******************************************************************
	//	MBOM_STR에서 레벨1 값의 LIST구하기
	//  상신시 레벨1 이하의 부품이 구성되었나를 확인하기위해
	//*******************************************************************/
	public ArrayList getLevelOneAssy(String gid) throws Exception
	{
		
		//변수 초기화
		String parent_code = "";
		item_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "SELECT * from MBOM_STR ";
		query += "where gid = '"+gid+"' and level_no = '1' ";
		query += "and child_code like '"+assy_head+"%' order by child_code asc";
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			mst = new mbomStrTable();
			mst.setPid(rs.getString("pid"));	
			mst.setGid(rs.getString("gid"));
			
			parent_code = rs.getString("parent_code");
			mst.setParentCode(parent_code);	

			mst.setChildCode(rs.getString("child_code"));	
			mst.setLevelNo(rs.getString("level_no"));	
			mst.setPartName(rs.getString("part_name"));	
			mst.setPartSpec(rs.getString("part_spec"));	
			mst.setLocation(rs.getString("location"));	
			mst.setOpCode(rs.getString("op_code"));	
			mst.setQtyUnit(rs.getString("qty_unit"));	
			mst.setQty(rs.getString("qty"));	
			mst.setMakerName(rs.getString("maker_name"));	
			mst.setMakerCode(rs.getString("maker_code"));	
			mst.setPriceUnit(rs.getString("price_unit"));	
			mst.setPrice(rs.getString("price"));
			mst.setAddDate(rs.getString("add_date"));	
			mst.setBuyType(rs.getString("buy_type"));	
			mst.setEcoNo(rs.getString("eco_no"));	
			mst.setAdTag(rs.getString("adtag"));	
			mst.setBomStartDate(rs.getString("bom_start_date"));	
			mst.setBomEndDate(rs.getString("bom_end_date"));
			mst.setAppStatus(rs.getString("app_status"));
			mst.setTag(rs.getString("tag"));

			//phantom Assy제외
			if(parent_code.indexOf(phantom_head) != -1) item_list.add(mst);
		}
		rs.close();
		stmt.close(); 

		return item_list;
	}

	//--------------------------------------------------------------------
	//
	//		설계변경 결재승인 반영사항
	//			
	//			
	//			
	//			
	//---------------------------------------------------------------------
	//*******************************************************************
	// 설계변경 결재상신시 : 설계변경의 BOM변경을 방지하기 위해 
	//*******************************************************************/	
	public void setEccBomStatus(String eco_no,String app_status) throws Exception
	{
		update = "UPDATE ECC_BOM set app_status='"+app_status+"' where eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}

	//*******************************************************************
	// 설계변경 진행상태변경[결재상신,승인,반려시] : ECC COM 진행상태 변경하기 
	//		commend : 승인[app], 거절[rej]	
	//*******************************************************************/	
	public void setEccStatus(String pid,String app_no,String commend) throws Exception
	{
		String fix_date = anbdt.getDateNoformat();

		//현재의 진행상태를 알아보기
		String where = "where pid='"+pid+"'";
		String ecc_status = modDAO.getColumData("ecc_com","ecc_status",where);
		String eco_no = modDAO.getColumData("ecc_com","eco_no",where);

		//------------------------------------------
		// 결재 상신시
		//------------------------------------------
		if(ecc_status.equals("1")) {					//ECR 결재 상신시
			update = "UPDATE ECC_COM set ecc_status='2' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		} else if(ecc_status.equals("6")) {				//ECO 결재 상신시
			update = "UPDATE ECC_COM set ecc_status='7' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		}
		//------------------------------------------
		//결재 승인, 반려시
		//------------------------------------------
		//ECC_COM에 진행상태 변경하기
		else if(ecc_status.equals("2")) {				//ECR승인요청상태
			//승인시
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='3' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_REQ set app_no='"+app_no+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			} 
			//반려시
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='0' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			}
		} else if(ecc_status.equals("7")) {			//ECO승인요청상태
			//승인시
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='8',fix_date='"+fix_date+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_ORD set app_no='"+app_no+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);
			} 
			//반려시
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='5' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_BOM set app_status='0' where eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			}
		}
		//------------------------------------------
		//ECO AUDIT 승인, 반려시
		//------------------------------------------
		else if(ecc_status.equals("8")) {			//ECO승인상태
			//승인시
			if(commend.equals("app")) {
				update = "UPDATE ECC_COM set ecc_status='9',fix_date='"+fix_date+"' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/F처리하기 (BOM에서 생산모듈 I/F처리)
				btmBO.tranceChangeBOMList(eco_no);
			} 
			//반려시
			else if(commend.equals("rej")) {
				update = "UPDATE ECC_COM set ecc_status='5',fix_date='' where pid='"+pid+"'";
				modDAO.executeUpdate(update);

				update = "UPDATE ECC_BOM set app_status='0' where eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			}
		}
	}
	//*******************************************************************
	// 설계변경 BOM변경[변경관리자 확정시] : MBOM STR에 해당 adtag별 내용 반영하기 
	//	eco_no : 설계변경번호,  order_date : 확정일자
	//			1.adtag = 'D','RB' 일때는 mbom_str 의 bom_end_date = 'eco적용일자'	: 확정
	//			2.adtag = 'A','RA' 일때는 mbom_str 의 app_status = '1',bom_start_date = 'eco적용일자': 확정
	//*******************************************************************/	
	public void changeBomFix(String eco_no,String order_date) throws Exception
	{
		//eco no에 해당되는 변경내용 전부 읽기
		ArrayList item_list = new ArrayList();
		item_list = tagetEccBomList(eco_no);

		//adtag별 데이터 처리하기 : MBOM_STR 
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (eccBomTable)item_iter.next();
			String pid = table.getPid();	
			String gid = table.getGid();	
			String adtag = table.getAdTag();
			if(adtag.equals("D") || adtag.equals("RB")) {
				update = "UPDATE MBOM_STR set bom_end_date='"+order_date+"' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			} else if(adtag.equals("A") || adtag.equals("RA")) {
				update = "UPDATE MBOM_STR set app_status='1',bom_start_date='"+order_date+"' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//ecc_bom에 반영(Audit 반려후 확정일자 변경될경우 대비) 
				update = "UPDATE ECC_BOM set bom_start_date='"+order_date+"' ";
				update += "WHERE eco_no='"+eco_no+"' and adtag='"+adtag+"'";
				modDAO.executeUpdate(update);
			}
		}

		//ecc_model에 반영 (Audit 반려후 확정일자 변경될경우 대비)
		update = "UPDATE ECC_MODEL set order_date='"+order_date+"' ";
		update += "WHERE eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}
	//*******************************************************************
	// 설계변경 BOM변경[변경관리자 확정분 UNDO기능] : MBOM STR에 해당 adtag별 내용 반영하기 
	//	eco_no : 설계변경번호,  order_date : 확정일자
	//			1.adtag = 'D','RB' 일때는 mbom_str 의 bom_end_date = 'eco적용일자'	: 확정
	//			2.adtag = 'A','RA' 일때는 mbom_str 의 app_status = '1'				: 확정
	//*******************************************************************/	
	public void changeBomFixUndo(String eco_no,String order_date) throws Exception
	{
		//eco no에 해당되는 변경내용 전부 읽기
		ArrayList item_list = new ArrayList();
		item_list = tagetEccBomList(eco_no);

		//adtag별 데이터 처리하기 : MBOM_STR
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		Iterator item_iter = item_list.iterator();
		while(item_iter.hasNext()) {
			table = (eccBomTable)item_iter.next();
			String pid = table.getPid();	
			String gid = table.getGid();	
			String adtag = table.getAdTag();
			if(adtag.equals("D") || adtag.equals("RB")) {
				update = "UPDATE MBOM_STR set bom_end_date='0' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/F처리하기 (BOM에서 생산모듈 I/F처리)
				update = "UPDATE MBOM_ITEM set bom_end_date='0',eco_no='',adtag='' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);
			} else if(adtag.equals("A") || adtag.equals("RA")) {
				update = "UPDATE MBOM_STR set app_status='0' ";
				update += "WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(update);

				//MBOM_STR --> MBOM_ITEM I/F처리하기 (BOM에서 생산모듈 I/F처리)
				String delete = "DELETE FROM mbom_item WHERE pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
				modDAO.executeUpdate(delete);
			}
			
		}
		//ECC_COM UNDO
		update = "UPDATE ECC_COM set ecc_status='8' where eco_no='"+eco_no+"'";
		modDAO.executeUpdate(update);
	}

	//*******************************************************************
	// 설계변경에 해당되는 BOM변경 내용 출력하기
	//*******************************************************************/	
	public ArrayList tagetEccBomList(String eco_no) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		eccBomTable table = new com.anbtech.dcm.entity.eccBomTable();
		
		query = "SELECT * FROM ecc_bom where eco_no ='"+eco_no+"'";
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
}

