package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomChangeBO
{
	private Connection con;
	private com.anbtech.bm.db.BomShowDAO showDAO = null;
	private com.anbtech.dcm.db.CbomChangeDAO chgDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	private com.anbtech.util.SortArray sort = new com.anbtech.util.SortArray();		//정렬하기
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	private String fg_head = "F";				//FG코드만 찾기 위해
	private String phantom = "1PH";				//팬텀 ASSY코드 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public CbomChangeBO(Connection con) 
	{
		this.con = con;
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
		chgDAO = new com.anbtech.dcm.db.CbomChangeDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		설계변경 해당부품 입력/수정/삭제 진행하기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 데이터 처리하기
	// cpid : 기존데이터를 처리하기 (삭제,변경전)
	//*******************************************************************/	
	public String changeEcoPart(String cpid,String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String data="",where="",ecc_status="",fg_code="";
		
		//검사1. ECO진행상태가 ECO작성상태이면 편집이 가능하다.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO반려상태입니다. ECO내용을 수정등록후 진행하십시오.";
			else if(ecc_status.equals("7")) data = "ECO결재중 입니다.";
			else data = "ECO작성상태에서만 등록이 가능합니다.";
			return data;
		}

		//------------------------------------------
		//해당 이벤트로 이동하여 처리하기 : ECC_BOM
		//------------------------------------------
		if(adtag.equals("A")) {					//신규부품 추가하기
			data = insertEcoPart(gid,parent_code,child_code,location,op_code,qty_unit,qty,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		} else if(adtag.equals("R")) {			//부품 바꾸기
			data = replaceEcoPart(cpid,gid,parent_code,child_code,location,op_code,qty_unit,qty,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		} else if(adtag.equals("D")) {			//기존부품 삭제하기
			data = deleteEcoPart(cpid,eco_no,chg_id,chg_name,adtag,order_date,ecc_reason,note);
		}

		//--------------------------------------
		//해당모델[FG]정보 처리하기 : ECC_MODEL
		//--------------------------------------
		where = "where gid='"+gid+"' and eco_no='"+eco_no+"'";	
		fg_code = chgDAO.getColumData("ECC_MODEL","fg_code",where);
		if(fg_code.length() == 0) {				
			insertEccModel(gid,eco_no,order_date);
		}

		return data;
	}

	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 신규부품 추가하기
	// adtag : A
	//*******************************************************************/	
	public String insertEcoPart(String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String input="",data="",where="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화

		//LEVEL NO구하기 
		query = "SELECT level_no FROM MBOM_STR where gid='"+gid+"' and child_code='"+parent_code+"'";
		query += " and assy_dup != 'D'";
		int level_no = chgDAO.getLevelNo(query);

		//검사1.PARENT_CODE가 해당모델구조체계에 있는지 검사하기
		if(level_no == 0) {
			data = "모품목코드가 해당BOM내에 없거나 Phantom Assy공정코드입니다. 확인후 다시 입력하십시요.";
			return data;
		}

		//자품목코드가 phantom assy코드인지 판단하기
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,등록일
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "미등록 부품입니다. 확인후 다시 입력하십시요.";
			return data;
		}
		String add_date = anbdt.getDateNoformat();
		
		//----------------------------------------------------------
		//MBOM_STR에 입력하기  
		// ---------------------------------------------------------
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+"A"+"','"+order_date+"','"+"0"+"','";
		input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);

		//출력순서[chg_order]찾기
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM에 입력하기  
		// ---------------------------------------------------------
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+level_no+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','";
		input += "A"+"','"+add_date+"','"+order_date+"','"+"0"+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "정상적으로 등록되었습니다.";
		return data;
	}
	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 변경하기
	// adtag : R
	//*******************************************************************/	
	public String replaceEcoPart(String cpid,String gid,String parent_code,String child_code,String location,
		String op_code,String qty_unit,String qty,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String input="",update="",data="",where="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화

		//기존데이터 읽기
		mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();
		mst = chgDAO.readStrItem(cpid);

		//검사1. 같은 ECO_NO에 동일한 내용을 Replace 하는지 검사하기
		where = "where eco_no='"+eco_no+"' and pid='"+cpid+"'";
		String chk_pid = chgDAO.getColumData("ecc_bom","pid",where);
		if(chk_pid.equals(cpid)) {
			data = "이미 부품변경을 진행하였습니다.";
			return data;
		}
		
		//자품목코드가 phantom assy코드인지 판단하기
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type,등록일
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "미등록 부품입니다. 확인후 다시 입력하십시요.";
			return data;
		}
		String add_date = anbdt.getDateNoformat();
		
		//----------------------------------------------------------
		//MBOM_STR에 입력하기  
		// ---------------------------------------------------------
		//1. 기존데이터 adtag='RB' : Replace Before
		update = "UPDATE MBOM_STR set eco_no='"+eco_no+"',adtag='RB' where pid='"+cpid+"'";
		//System.out.println("update : " + update);
		chgDAO.executeUpdate(update);

		//2. Replace Data 입력하기 
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+mst.getLevelNo()+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+"RA"+"','"+order_date+"','"+"0"+"','";
		input += "0"+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);



		//출력순서[chg_order]찾기
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM에 입력하기  
		// ---------------------------------------------------------
		//1. 기존데이터 MBOM_STR의 데이터 입력하기
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
		input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
		input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
		input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
		input += mst.getBuyType()+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','"+"RB"+"','"+add_date+"','";
		input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+mst.getAssyDup()+"','"+mst.getItemType()+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);

		//2. 추가한 데이터 입력하기
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += pid+"','"+gid+"','"+parent_code+"','"+child_code+"','"+mst.getLevelNo()+"','"+part[0]+"','";
		input += part[1]+"','"+location+"','"+op_code+"','"+part[4]+"','"+qty+"','"+part[2]+"','"+part[3]+"','";
		input += "원"+"','"+"0"+"','"+add_date+"','"+""+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','";
		input += "RA"+"','"+add_date+"','"+order_date+"','"+"0"+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+assy_dup+"','"+part[5]+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "정상적으로 변경되었습니다.";
		return data;
	}
	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 기존부품 삭제하기
	// adtag : D
	//*******************************************************************/	
	public String deleteEcoPart(String cpid,String eco_no,String chg_id,String chg_name,
		String adtag,String order_date,String ecc_reason,String note) throws Exception
	{
		String update="",input="",data="",where="";
		
		//----------------------------------------------------------
		//MBOM_STR에 adtag='D' update하기  
		// ---------------------------------------------------------
		update = "UPDATE MBOM_STR set eco_no='"+eco_no+"',adtag='D' where pid='"+cpid+"'";
		//System.out.println("update : " + update);
		chgDAO.executeUpdate(update);

		//삭제할 정보 찾기
		mbomStrTable mst = new com.anbtech.bm.entity.mbomStrTable();
		mst = chgDAO.readStrItem(cpid);

		//검사1. 같은 ECO_NO에 동일한 내용을 Replace 하는지 검사하기
		where = "where eco_no='"+eco_no+"' and pid='"+cpid+"'";
		String chk_pid = chgDAO.getColumData("ecc_bom","pid",where);
		if(chk_pid.equals(cpid)) {
			data = "이미 부품삭제를 진행하였습니다.";
			return data;
		}

		//변경일 찾기
		String change_date = anbdt.getDateNoformat();

		//출력순서[chg_order]찾기
		query = "SELECT chg_order FROM ecc_bom where eco_no='"+eco_no+"' order by chg_order DESC";
		String chg_order = chgDAO.getChgOrder(query);

		//----------------------------------------------------------
		//ECC_BOM에 입력하기  
		// ---------------------------------------------------------
		input = "INSERT INTO ECC_BOM (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,chg_id,chg_name,adtag,change_date,bom_start_date,bom_end_date,ecc_reason,note,";
		input += "chg_order,app_status,assy_dup,item_type) values('";
		input += mst.getPid()+"','"+mst.getGid()+"','"+mst.getParentCode()+"','"+mst.getChildCode()+"','";
		input += mst.getLevelNo()+"','"+mst.getPartName()+"','"+mst.getPartSpec()+"','"+mst.getLocation()+"','";
		input += mst.getOpCode()+"','"+mst.getQtyUnit()+"','"+mst.getQty()+"','"+mst.getMakerName()+"','";
		input += mst.getMakerCode()+"','"+mst.getPriceUnit()+"','"+mst.getPrice()+"','"+mst.getAddDate()+"','";
		input += mst.getBuyType()+"','"+eco_no+"','"+chg_id+"','"+chg_name+"','"+"D"+"','"+change_date+"','";
		input += mst.getBomStartDate()+"','"+mst.getBomEndDate()+"','"+ecc_reason+"','"+note+"','";
		input += chg_order+"','"+"0"+"','"+mst.getAssyDup()+"','"+mst.getItemType()+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
		

		data = "정상적으로 삭제되었습니다.";
		return data;
	}
	//*******************************************************************
	// ECC MODEL에 변경되는 부품의 모델[FG]정보를 입력하기
	// 
	//*******************************************************************/	
	public void insertEccModel(String gid,String eco_no,String order_date) throws Exception
	{
		String input="",where="",fg_code="",model_code="",model_name="";

		//모델코드,모델명,fg_code구하기
		where = "where pid='"+gid+"'";
		model_code=chgDAO.getColumData("MBOM_MASTER","model_code",where);
		model_name=chgDAO.getColumData("MBOM_MASTER","model_name",where);
		fg_code=chgDAO.getColumData("MBOM_MASTER","fg_code",where);

		//등록하기
		String pid = anbdt.getNumID(0);
		input = "INSERT INTO ECC_MODEL (pid,eco_no,gid,model_code,model_name,fg_code,order_date) values('";
		input += pid+"','"+eco_no+"','"+gid+"','"+model_code+"','"+model_name+"','"+fg_code+"','";
		input += order_date+"')";
		//System.out.println("input : " + input);
		chgDAO.executeUpdate(input);
	}

	//--------------------------------------------------------------------
	//
	//		설계변경 해당부품 편집하기 [수정 / UNDO]
	//		
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 데이터 편집하기
	//  ADTAG = 'A' or 'RA / RB' 수정일때
	//*******************************************************************/	
	public String modifyEcoPart(String pid,String gid,String child_code,String location,String op_code,
		String eco_no,String adtag,String chg_id,String chg_name,String ecc_reason,String note) throws Exception
	{
		String data="",where="",ecc_status="",app_status="",update="",assy_dup="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화
		
		//검사1. ECO진행상태가 ECO작성상태이면 편집이 가능하다.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO반려상태입니다. ECO내용을 수정등록후 진행하십시오.";
			else if(ecc_status.equals("7")) data = "ECO결재중 입니다.";
			else data = "ECO작성상태에서만 등록이 가능합니다.";
			return data;
		}

		//검사2. ECC_BOM의 app_status='0'일때만 편집이 가능하다.
		where = "where pid='"+pid+"'";
		ecc_status = chgDAO.getColumData("ECC_BOM","app_status",where);
		if(ecc_status.equals("1")) {
			data = "설계변경 승인된 부품은 편집할 수 없습니다.";
			return data;
		}

		//자품목코드가 phantom assy코드인지 판단하기
		if(child_code.indexOf(phantom) != -1) {
			assy_dup = "D"; 
		}

		//0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type
		part = chgDAO.getComponentInfo(child_code);
		if(part[1].length() == 0) {
			data = "미등록 부품입니다. 확인후 다시 입력하십시요.";
			return data;
		}
		String change_date = anbdt.getDateNoformat();

		//수정하기 : MBOM_STR
		update = "UPDATE mbom_str set child_code='"+child_code+"',part_name='"+part[0]+"',";
		update += "part_spec='"+part[1]+"',location='"+location+"',op_code='"+op_code+"',";
		update += "maker_name='"+part[2]+"',maker_code='"+part[3]+"',price_unit='"+"원"+"',";
		update += "price='"+"0"+"',qty_unit='"+part[4]+"',assy_dup='"+assy_dup+"',item_type='"+part[5]+"' ";
		update += "where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		chgDAO.executeUpdate(update);

		//수정하기 : ECC_BOM
		update = "UPDATE ecc_bom set child_code='"+child_code+"',part_name='"+part[0]+"',";
		update += "part_spec='"+part[1]+"',location='"+location+"',op_code='"+op_code+"',";
		update += "maker_name='"+part[2]+"',maker_code='"+part[3]+"',price_unit='"+"원"+"',";
		update += "price='"+"0"+"',chg_id='"+chg_id+"',chg_name='"+chg_name+"',qty_unit='"+part[4]+"',";
		update += "change_date='"+change_date+"',ecc_reason='"+ecc_reason+"',note='"+note+"',";
		update += "assy_dup='"+assy_dup+"',item_type='"+part[5]+"' ";
		update += "where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
		chgDAO.executeUpdate(update);
		 
		data = "정상적으로 수정되었습니다.";
		return data;
	}

	//*******************************************************************
	// MBOM_STR 및 ECC_BOM 에 데이터 UNDO진행하기
	//  ADTAG = 'A' or 'RA / RB' or 'D'
	//  cpid : 기존데이터[D,RB], pid : 신규데이터[A,RA]
	//*******************************************************************/	
	public String undoEcoPart(String cpid,String pid,String gid,String adtag,String eco_no) throws Exception
	{
		String data="",where="",ecc_status="",app_status="",delete="",update="";
		String[] part = new String[6];
		for(int i=0; i<6; i++) part[i] = "";		//초기화
		
		//검사1. ECO진행상태가 ECO작성상태이면 편집이 가능하다.
		where = "where eco_no='"+eco_no+"'";
		ecc_status = chgDAO.getColumData("ECC_COM","ecc_status",where);
		if(!ecc_status.equals("6")) {
			if(ecc_status.equals("5")) data = "ECO반려상태입니다. ECO내용을 수정등록후 진행하십시오.";
			else if(ecc_status.equals("7")) data = "ECO결재중 입니다.";
			else data = "ECO작성상태에서만 등록이 가능합니다.";
			return data;
		}

		//검사2. ECC_BOM의 app_status='0'일때만 편집이 가능하다.
		where = "where pid='"+pid+"'";
		ecc_status = chgDAO.getColumData("ECC_BOM","app_status",where);
		if(ecc_status.equals("1")) {
			data = "설계변경 승인된 부품은 편집할 수 없습니다.";
			return data;
		}

		//-----------------------------------
		//UNDO 진행하기
		//-----------------------------------
		if(adtag.equals("A")) {									//신규로 추가된 경우
			//MBOM_STR에서 해당내용을 완전삭제한다.
			delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);

			//ECC_BOM에서 해당내용을 완전삭제한다.
			delete = "DELETE from ECC_BOM where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);
		} else if(adtag.equals("D")) {							//기존데이터 삭제표시의 경우
			//MBOM_STR에서 설변에 관련된 정보만 원상복귀한다.
			update = "UPDATE MBOM_STR set eco_no='',adtag='' where pid='"+cpid+"' and gid='"+gid+"'";
			chgDAO.executeUpdate(update);

			//ECC_BOM에서 해당내용을 완전삭제한다.
			delete = "DELETE from ECC_BOM where pid='"+cpid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";
			chgDAO.executeUpdate(delete);
		} else if(adtag.equals("RA") || adtag.equals("RB")) {	//부품 변경의 경우
			//MBOM_STR에서 설변에 관련된 정보만 원상복귀한다.
			update = "UPDATE MBOM_STR set eco_no='',adtag='' where pid='"+cpid+"' and gid='"+gid+"'";	//RB
			chgDAO.executeUpdate(update);

			delete = "DELETE from MBOM_STR where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RB
			chgDAO.executeUpdate(delete);

			//ECC_BOM에서 해당내용을 완전삭제한다.
			delete = "DELETE from ECC_BOM where pid='"+cpid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RA
			chgDAO.executeUpdate(delete);
			delete = "DELETE from ECC_BOM where pid='"+pid+"' and gid='"+gid+"' and eco_no='"+eco_no+"'";//RA
			chgDAO.executeUpdate(delete);
		}

		//---------------------------------------------------------------------
		//	ECC BOM에서 해당 FG가 모두 undo 되었으면 ECC MODEL에서도 undo하기
		//---------------------------------------------------------------------
		query = "SELECT count(*) FROM ecc_bom WHERE gid='"+gid+"' and eco_no='"+eco_no+"'";	
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) {				
			delete = "DELETE FROM ecc_model WHERE gid='"+gid+"' and eco_no='"+eco_no+"'";						
			chgDAO.executeUpdate(delete);
		}
		
		data = "정상적으로 UNDO 되었습니다.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		설계변경할 BOM 관련 정보
	//
	//
	//---------------------------------------------------------------------

	/**********************************************************************
	 * 설계변경할 BOM구조 보기
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList viewCbomStrList(String gid,String level_no,String parent_code) throws Exception
	{
		//변수
		String sel_date = anbdt.getDateNoformat();

		//배열에 담기
		saveFrdStrArray(gid,level_no,parent_code,sel_date);

		//링크달아 다시 ArrayList에 담기
		mbomStrTable table = null;
		ArrayList table_list = new ArrayList();
		for(int i=0; i<an; i++) {
			table = new mbomStrTable();
			table.setPid(item[i][0]);
			String pcode = item[i][1];
			if(!item[i][3].equals("0"))	//첫번째값은 제외[level no : 0]
				pcode = "<a href=\"javascript:strView('"+item[i][0]+"','"+gid+"');\">"+item[i][1]+"</a>";
			table.setParentCode(pcode);
			table.setChildCode(item[i][2]);
			table.setLevelNo(item[i][3]);
			table.setPartName(item[i][4]);
			table.setPartSpec(item[i][5]);
			table.setLocation(item[i][6]);
			table.setOpCode(item[i][7]);
			table.setQtyUnit(item[i][8]);
			table.setQty(item[i][9]);		
			table_list.add(table);
		}
		return table_list;
	}

	/**********************************************************************
	 * 설계변경할 대상 적용모델 LIST
	 *   발생모델 + 전체대상모델
	 **********************************************************************/
	public ArrayList targetModelList(String eco_no) throws Exception
	{
		String where = "";
		ArrayList table_list = new ArrayList();			//전체 FG코드 담기 임시
		ArrayList fg_list = new ArrayList();			//unique 한 FG코드 담아 리턴
		
		//--------------------------------------
		//발생모델[FG코드] 정보 전달하기
		//--------------------------------------
		where = "where eco_no like '"+eco_no+"%'";
		String fg_code_list = chgDAO.getColumData("ecc_com","fg_code",where);
		StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
		while(list.hasMoreTokens()) {
			String fcd = list.nextToken(); fcd = fcd.trim();
			eccModelTable table = new eccModelTable();
			table.setFgCode(fcd);
			table_list.add(table);
		}

		//---------------------------------------
		//적용모델[FG코드] 정보 찾기
		//---------------------------------------	
		ArrayList model_list = new ArrayList();
		model_list = viewRevAllFGList(eco_no);
		eccModelTable model = new eccModelTable();
		Iterator model_iter = model_list.iterator();
		while(model_iter.hasNext()) {
			model = (eccModelTable)model_iter.next();
			
			eccModelTable table = new eccModelTable();
			table.setFgCode(model.getFgCode());
			table_list.add(table);
		}

		//------------------------------------
		//배열에 담아 유니크한 fg code만 뽑기
		//------------------------------------
		int cnt = table_list.size();
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		eccModelTable fcode = new eccModelTable();
		Iterator fcode_iter = table_list.iterator();
		int n=0;
		while(fcode_iter.hasNext()) {
			fcode = (eccModelTable)fcode_iter.next();
			data[n] = fcode.getFgCode();	//System.out.println("org fg : " + data[n]);
			n++;
		}

		//정렬하기
		sort.bubbleSortStringAsc(data);
		
		//중복FG코드는 ""로 입력하기
		int dn = 0;			//중복된 FG코드 갯수
		for(int i=0; i<cnt-1; i++) if(data[i].equals(data[i+1])) { data[i] = ""; dn++; }

		//중복을 제거한 내용만 배열에 다시 담는다.
		int un = cnt - dn;					//unique한 갯수
		String[] udata = new String[un];	//unique한 FG코드 값
		int k = 0;
		for(int i=0; i<cnt; i++) if(data[i].length() != 0) { udata[k]=data[i]; k++; }

		for(int i=0; i<k; i++) {	
			eccModelTable table = new eccModelTable();
			table.setFgCode(udata[i]);		//System.out.println("fg : " + udata[i]);
			fg_list.add(table);
		}
	
		return fg_list;
	}

	//--------------------------------------------------------------------
	// 설계변경 내용 비교
	//		1.대상모델 : 적용모델 비교
	//		2.적용모델중 발생모델 대표의 부품 : 적용모델들의 부품 비교
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 *  대상모델[기준] : 적용모델[비교대상] 비교 
	 **********************************************************************/
	public String compareFGList(String eco_no) throws Exception
	{
		//선언
		String compare_list = "",where = "";
		

		//대상모델[기준] 정보 구하기
		ArrayList target_list = new ArrayList();
		target_list = viewRevAllFGList(eco_no);
		int t_size = target_list.size();
		if(t_size == 0) return compare_list;
		String[] target = new String[t_size];
		
		eccModelTable table = new eccModelTable();
		Iterator target_iter = target_list.iterator();
		int n = 0;
		while(target_iter.hasNext()) {
			table = (eccModelTable)target_iter.next();
			target[n] = table.getFgCode();
			n++;
		}

		//적용모델[비교대상] 정보 구하기
		query = "SELECT count(*) FROM ecc_model where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return compare_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_model","fg_code",where);

		//비교하기
		compare_list += "*** 미적용 대상모델[FG] 리스트 *** <br><br>";
		for(int i=0; i<t_size; i++) {
			int same_fg = 0;
			for(int j=0; j<cnt; j++) {
				if(target[i].equals(data[j])) same_fg++;
			}
			if(same_fg == 0) compare_list += target[i]+"<br>";
		}	

		return compare_list;
	}

	/**********************************************************************
	 *  발생모델 변경부품[기준] : 적용모델들[비교대상] 변경부품 비교 
	 **********************************************************************/
	public String compareItemList(String eco_no) throws Exception
	{
		//선언
		String compare_list = "",where = "";
		String base_fcd="",base_gid="";					//발생모델 대표FG코드값들

		//-------------------------------------
		// 기준모델[FG]의 변경부품 찾기
		//-------------------------------------
		//발생모델 대표FG코드값,GID 구하기
		where = "where eco_no = '"+eco_no+"'";
		String fg_code_list = chgDAO.getColumData("ecc_com","fg_code",where);
		StringTokenizer list = new StringTokenizer(fg_code_list,"\n");
		if(list.hasMoreTokens()) base_fcd = list.nextToken();
		
		where = "where fg_code = '"+base_fcd+"'";
		base_gid = chgDAO.getColumData("mbom_master","pid",where);

		//대표FG를 갖는 변경부품 찾기
		ArrayList base_list = new ArrayList();
		base_list = chgDAO.getEccBomList(eco_no,base_gid);

		int t_size = base_list.size(); if(t_size == 0) t_size = 1;
		String[][] base = new String[t_size][4];
		for(int i=0; i<t_size; i++) for(int j=0; j<4; j++) base[i][j] = "";
	
		eccBomTable table = new eccBomTable();
		Iterator base_iter = base_list.iterator();
		int n = 0;
		while(base_iter.hasNext()) {
			table = (eccBomTable)base_iter.next();
			base[n][0] = table.getParentCode();
			base[n][1] = table.getChildCode();
			base[n][2] = table.getAdTag();
			base[n][3] = "";					//비교결과 임시로 담기
			n++;
		}

		//-------------------------------------
		// 비교대상모델[FG]의 변경부품 찾기
		//-------------------------------------
		//적용모델[비교대상] 정보 구하기
		query = "SELECT count(*) FROM ecc_model where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return compare_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_model","gid",where);
		String pcd="", ccd="", tag="";
		for(int i=0; i<cnt; i++) {
			//기준모델과 동일한 대상모델이면 skip하기
			if(!data[i].equals(base_gid)) {
				//비교대상의 fg_code 구하기
				where = "where gid='"+data[i]+"'";
				String tg_fcd = chgDAO.getColumData("ecc_model","fg_code",where);
				compare_list += "*** 기준모델[FG]: "+base_fcd+" 와 적용모델[FG]: "+tg_fcd+" 변경부품비교 *** <br><br>"; 

				//비교대상모델을 검색하여 결과을 담기
				ArrayList tg_list = new ArrayList();
				tg_list = chgDAO.getEccBomList(eco_no,data[i]);
				eccBomTable tg = new eccBomTable();
				Iterator tg_iter = tg_list.iterator();
				while(tg_iter.hasNext()) {
					tg = (eccBomTable)tg_iter.next();
					pcd = tg.getParentCode();
					ccd = tg.getChildCode();
					tag = tg.getAdTag();

					//기준모델과 비교하기
					int sm = 0;
					for(int k=0; k<t_size; k++) {
						if(base[k][0].equals(pcd) && base[k][1].equals(ccd) && base[k][2].equals(tag)) {
							base[k][3] = "1";
							sm++;
						} //if
					} //for
					//검사결과 대상내용을 변수로 담기 : 대상모델정보에만 있는 부품정보
					compare_list += "# 적용대상모델[FG:"+tg_fcd+"]에만 있는 부품<br>";
					if(sm == 0) compare_list += pcd + ": "+ccd+": "+tag+"<br>";
				} //while
				//검사결과 기준내용을 변수로 담기 : 기준모델정보에만 있는 부품정보
				compare_list += "<br># 기준모델[FG:"+base_fcd+"]에만 있는 부품<br>";
				for(int c=0; c<t_size; c++) {
					if(!base[c][3].equals("1")) compare_list +=  base[c][0]+": "+base[c][1]+": "+base[c][2]+"<br>";
				}
				//base[n][3]을 clear하기
				for(int c=0; c<t_size; c++) base[c][3] = "";
				compare_list += "<br><br><br>";
			} //if
		} //for

		return compare_list;
	}
	
	//--------------------------------------------------------------------
	// 적용모델[FG코드] 찾기
	//		해당품목코드로 FG코드만 찾기
	//
	//
	//---------------------------------------------------------------------
	/**********************************************************************
	 *  전체 적용모델 검색하기 : 설계변경의 부품변경내용을 이용한 적용모델검색하기
	 **********************************************************************/
	public ArrayList viewRevAllFGList(String eco_no) throws Exception
	{
		//선언
		ArrayList table_list = new ArrayList();
		String where = "";

		//해당 ECO_NO 을 이용한 모품목코드만 찾기
		query = "SELECT count(*) FROM ecc_bom where eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		where = "where eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_bom","parent_code",where);

		//적용모델[FG]내용
		table_list = getRevFGList(data);
		return table_list;
	}
	/**********************************************************************
	 *  대상모델 검색하기 : 설계변경의 부품변경내용을 이용한 적용모델검색하기
	 **********************************************************************/
	public ArrayList viewRevFGList(String gid,String eco_no) throws Exception
	{
		//선언
		ArrayList table_list = new ArrayList();
		String where = "";

		//해당 ECO_NO, GID을 이용한 모품목코드만 찾기
		query = "SELECT count(*) FROM ecc_bom where gid='"+gid+"' and eco_no='"+eco_no+"'";
		int cnt = chgDAO.getTotalCount(query);
		if(cnt == 0) return table_list;

		String[] data = new String[cnt];
		where = "where gid='"+gid+"' and eco_no='"+eco_no+"'";
		data = chgDAO.getColumArrayData("ecc_bom","parent_code",where);

		//적용모델[FG]내용
		table_list = getRevFGList(data);
		return table_list;
	}
	/**********************************************************************
	 *  FG정보 읽기 : 관리코드,모델코드,모델명,FG코드,제품생산상태
	 *  해당품목코드의 역전개로 FG코드만 찾기 : 선두문자 가 'F'로 시작되는 것
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList getRevFGList(String[] child_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();
		eccModelTable table = null;
		ArrayList table_list = new ArrayList();

		//배열의 갯수가 0 이면 리턴 
		if(child_code.length == 0) return table_list;

		//배열에 담기
		makeRevTextArray(child_code,sel_date);

		//배열에서 FG코드만 찾기
		int f_cnt = 0;
		for(int i=0; i<an; i++) { if(item[i][2].indexOf(fg_head) != -1) f_cnt++; }
		if(f_cnt == 0) return table_list;		//FG코드가 없으면 리턴

		String[] data = new String[f_cnt];
		for(int i=0; i<f_cnt; i++) data[i]="";
		int n = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].indexOf(fg_head) != -1) { data[n] = item[i][2]; n++; }
		}

		//정렬하기
		sort.bubbleSortStringAsc(data);
		
		//중복FG코드는 ""로 입력하기
		int dn = 0;			//중복된 FG코드 갯수
		for(int i=0; i<f_cnt-1; i++) if(data[i].equals(data[i+1])) { data[i] = ""; dn++; }

		//중복을 제거한 내용만 배열에 다시 담는다.
		int un = f_cnt - dn;				//unique한 갯수
		String[] udata = new String[un];	//unique한 FG코드 값
		int k = 0;
		for(int i=0; i<f_cnt; i++) if(data[i].length() != 0) { udata[k]=data[i]; k++; }
			
		//ArrayList에 담기
		for(int i=0; i<un; i++) {
			//fg code에 연관된 정보를 기초정보[mbom_master]에서 가져오기
			mbomMasterTable mst = new mbomMasterTable();
			mst = chgDAO.readMasterItem(udata[i]);

			//Array List로 담기 
			table = new eccModelTable();
			table.setGid(mst.getPid());
			table.setModelCode(mst.getModelCode());
			table.setModelName(mst.getModelName());
			table.setFgCode(udata[i]);
			table.setMStatus(mst.getMStatus());
			
			//System.out.println(mst.getModelCode()+":"+mst.getModelName()+":"+udata[i]);
			table_list.add(table);
		}
		return table_list;
	}
	
	//--------------------------------------------------------------------
	//
	//		정전개, 역전개 BOM ARRAY LIST to ARRAY로 담기
	//		1.정전개 배열에 담기
	//		2.역전개 배열에 담기 : 역전개 Raw 데이터 읽기
	//		3.정전개 역전개 총갯수 구하기
	//---------------------------------------------------------------------
	/**********************************************************************
	 * 정전개 배열에 담기 : MBOM_STR에서 BOM TREE정보를 구하기
	 * 정전개 TREE 구조체계를 배열에 담기 : 하부구조 전체
	 **********************************************************************/
	public void saveFrdStrArray(String gid,String level_no,String parent_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,level_no,parent_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][10];
		for(int i=0; i<cnt; i++) for(int j=0; j<10; j++) item[i][j]="";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getPid();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLevelNo();
			item[an][4] = table.getPartName();
			item[an][5] = table.getPartSpec();
			item[an][6] = table.getLocation();
			item[an][7] = table.getOpCode();
			item[an][8] = table.getQtyUnit();
			item[an][9] = table.getQty();

			//System.out.println(item[an][3]+":"+item[an][1]+":"+item[an][2]);
			an++;
		}
	}

	/**********************************************************************
	 * 역전개 배열에 담기 : MBOM_STR에서 BOM TREE정보를 구하기
	 * 역전개 TEXT 구조체계를 만들기위한 배열로 다시 담기 : 모자관계도 바꾼다.
	 **********************************************************************/
	public void makeRevTextArray(String[] child_code,String sel_date) throws Exception
	{
		//배열로 담기
		saveRevStrArray(child_code,sel_date);

		//TREE구조를 만들기위해 배열을 다시만들기
		String[][] data = new String[an][7];
		for(int i=1; i<an; i++) for(int j=0; j<7; j++) data[i][j]="";
		//System.out.println(data[0][0]+":"+data[0][1]);

		int n = 1, k = 0;
		for(int i=0; i<an; i++) {
			if(item[i][2].equals("0")) { 
				n = 1;
			} else {
				data[k][0] = Integer.toString(n);	//Level No
				data[k][1] = item[i][1];			//Parent Code (원:자코드 -> 모코드로)
				data[k][2] = item[i][0];			//Child Code  (원:모코드 -> 자코드로)
				data[k][3] = item[i+1][3];			//Part Name
				data[k][4] = item[i+1][4];			//Part Spec
				data[k][5] = item[i+1][5];			//Location
				data[k][6] = item[i+1][6];			//OP Code

				//System.out.println(data[k][0]+":"+data[k][1]+":"+data[k][2]+":"+data[k][3]+":"+data[k][4]+":"+data[k][5]+":"+data[k][6]);
				n++;
				k++;
			} //if
		} //for

		//item배열로 다시 담기
		item = new String[k][7];
		an = k;
		for(int i=0; i<k; i++) {
			for(int j=0; j<7; j++) item[i][j] = data[i][j];		
			//System.out.println(item[i][0]+":"+item[i][1]+":"+item[i][2]);
		}
	}

	/**********************************************************************
	 * 역전개 RAW데이터 배열에 담기 : MBOM_STR에서 BOM TREE정보를 구하기
	 * 역전개 TREE 구조체계를 배열에 담기
	 **********************************************************************/
	private void saveRevStrArray(String[] child_code,String sel_date) throws Exception
	{
		// 배열선언
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getReverseMultiBomItems(child_code,sel_date);
		int cnt = item_list.size();
		item = new String[cnt][7];
		for(int i=0; i<cnt; i++) for(int j=0; j<7; j++) item[i][j]="0";

		//배열에 담기
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an=0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getParentCode();
			item[an][1] = table.getChildCode();
			item[an][2] = table.getLevelNo();
			item[an][3] = table.getPartName();
			item[an][4] = table.getPartSpec();
			item[an][5] = "";	//location
			item[an][6] = table.getOpCode();

			//System.out.println(item[an][2]+":"+item[an][0]+":"+item[an][1]);
			an++;
		}
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기
	 * 정/역전개 TREE 구조체계를 만들기위한 배열갯수
	 **********************************************************************/
	public String getArrayCount() 
	{
		return Integer.toString(an);
	}

}
