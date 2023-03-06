package com.anbtech.bm.business;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class BomBaseInfoBO
{
	private Connection con;
	private com.anbtech.bm.db.BomModifyDAO modDAO = null;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public BomBaseInfoBO(Connection con) 
	{
		this.con = con;
		modDAO = new com.anbtech.bm.db.BomModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		BOM MASTER 에 관한 메소드 정의 : 기본정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// MBOM_MASTER에 데이터 입력하기
	//	#default로 MBOM_STR에도 입력한다.
	//*******************************************************************/	
	public String insertMaster(String pid,String modelg_code,String modelg_name,String model_code,String model_name,
		String fg_code,String fg_spec,String pdg_code,String pdg_name,String pd_code,String pd_name,
		String pjt_code,String pjt_name,String reg_id,String reg_name,String reg_date,String purpose) throws Exception
	{
		String input="",data="";

		//중복검사 : FG코드로 중복검사진행
		query = "SELECT COUNT(*) from MBOM_MASTER where fg_code ='"+fg_code+"'";
		int cnt = modDAO.getTotalCount(query);
		if(cnt != 0) {
			data = "이미 FG코드가 등록되어 있어 중복등록할 수 없습니다.";
			////System.out.println(data);
			return data;
		}
		
		//MBOM_MASTER에 입력하기
		input = "INSERT INTO MBOM_MASTER (pid,modelg_code,modelg_name,model_code,model_name,fg_code,pdg_code,pdg_name,pd_code,pd_name,pjt_code,";
		input += "pjt_name,reg_id,reg_name,reg_date,app_id,app_name,app_date,bom_status,app_no,m_status,purpose) values('";
		input += pid+"','"+modelg_code+"','"+modelg_name+"','"+model_code+"','"+model_name+"','"+fg_code+"','"+pdg_code+"','"+pdg_name+"','"+pd_code+"','"+pd_name+"','";
		input += pjt_code+"','"+pjt_name+"','"+reg_id+"','"+reg_name+"','"+reg_date+"','"+""+"','"+""+"','";
		input += ""+"','"+"1"+"','"+""+"','"+"1"+"','"+purpose+"')";
		modDAO.executeUpdate(input);

		//부품정보 찾기 : [0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type] 
		String[] part = new String[6];
		part = modDAO.getComponentInfo(fg_code);

		//MBOM_STR에 입력하기
		input = "INSERT INTO MBOM_STR (pid,gid,parent_code,child_code,level_no,part_name,part_spec,";
		input += "location,op_code,qty_unit,qty,maker_name,maker_code,price_unit,price,add_date,buy_type,";
		input += "eco_no,adtag,bom_start_date,bom_end_date,app_status,tag,item_type) values('";
		input += anbdt.getID()+"','"+pid+"','"+model_code+"','"+fg_code+"','"+"0"+"','"+part[0]+"','"+part[1]+"','";
		input += ""+"','"+""+"','"+part[4]+"','"+"1"+"','"+part[2]+"','"+part[3]+"','"+"원"+"','"+"0"+"','"+reg_date+"','";
		input += ""+"','"+""+"','"+""+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+part[5]+"')";
		modDAO.executeUpdate(input);

		//MBOM_GRADE_MGR에 입력하기 : BOM Group관리
		String div_code = "";
		div_code = modDAO.getDivCode(reg_id);

		input = "INSERT INTO mbom_grade_mgr(pid,keyname,owner,div_code) values('";
		input += anbdt.getID()+"','"+fg_code+"','"+reg_id+"/"+reg_name+";"+"','"+div_code+"')";
		modDAO.executeUpdate(input);
		
		data = "정상적으로 등록되었습니다.";
		////System.out.println(data);
		return data;
	}

	//*******************************************************************
	// MBOM_MASTER에 데이터 수정하기
	//	#default로 MBOM_STR에도 수정한다.
	//*******************************************************************/	
	public String updateMaster(String pid,String modelg_code,String modelg_name,String model_code,String model_name,String fg_code,
		String fg_spec,String pdg_code,String pdg_name,String pd_code,String pd_name,String pjt_code,String pjt_name,
		String reg_date,String purpose) throws Exception
	{
		String update="",data="",where="";

		//수정가능한 상태를 검사한다. 초기등록상태만 가능함
		String bom_status = modDAO.getColumData("MBOM_MASTER","bom_status","where pid ='"+pid+"'");
		if(bom_status.equals("4")) {
			data = "BOM결재중인 정보는 수정 할 수 없습니다.";
			return data;
		} else if(bom_status.equals("5")) {
			data = "BOM승인 정보는 수정 할 수 없습니다.";
			return data;
		} 

		//MBOM_GRADE_MGR에 수정[fg_code만]하기 : BOM Group관리
		where = "where pid='"+pid+"'";
		String was_fg_code = modDAO.getColumData("MBOM_MASTER","fg_code",where);
		update = "UPDATE mbom_grade_mgr SET keyname='"+fg_code+"' where keyname='"+was_fg_code+"'";
		modDAO.executeUpdate(update);
		
		//MBOM_MASTER에 수정하기
		update = "UPDATE MBOM_MASTER set model_code='"+model_code+"',model_name='"+model_name;
		update += "',fg_code='"+fg_code+"',pdg_code='"+pdg_code+"',pdg_name='"+pdg_name;
		update += "',modelg_code='"+modelg_code+"',modelg_name='"+modelg_name;
		update += "',pd_code='"+pd_code+"',pd_name='"+pd_name;
		update += "',pjt_code='"+pjt_code+"',pjt_name='"+pjt_name;
		update += "',reg_date='"+reg_date+"',purpose='"+purpose+"' where pid='"+pid+"'";
		modDAO.executeUpdate(update);

		//부품정보 찾기 : [0부품이름,1규격,2메이커명,3형명,4수량단위,5item_type] 
		String[] part = new String[6];
		part = modDAO.getComponentInfo(fg_code);

		//MBOM_STR에 수정하기 : 반려가 있을수 있어 작성중일때만 적용함
		if(bom_status.equals("1")) {
			update = "UPDATE MBOM_STR set parent_code='"+model_code+"',child_code='"+fg_code+"',part_spec='"+part[1];
			update += "',maker_name='"+part[2]+"',qty_unit='"+part[4]+"',item_type='"+part[5];
			update += "' where gid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

		//MBOM_MASTER가 현재 반려상태='0'이면 작성상태='3'로 바꿔주기
		if(bom_status.equals("0")) {
			update = "UPDATE MBOM_MASTER set bom_status='3' where pid='"+pid+"'";
			modDAO.executeUpdate(update);
		}

		data = "정상적으로 수정되었습니다.";
		////System.out.println(data);
		return data;
	}

	//*******************************************************************
	// MBOM_MASTER에 데이터 삭제하기
	//	#default로 MBOM_STR에도 삭제한다.
	//*******************************************************************/	
	public String deleteMaster(String pid) throws Exception
	{
		String delete = "",data="",where="";
		String query = "SELECT COUNT(*) FROM MBOM_STR where gid='"+pid+"'";
		
		//MBOM_STR에 등록된 부품이 있으면 등록중인 것으로 간주하여 삭제가 않됨
		if(modDAO.getTotalCount(query) > 1) {	
			data = "등록 or 결재중인 BOM으로 삭제할 수 없습니다.";
			return data;
		}

		//MBOM_GRADE_MGR에 수정[fg_code만]하기 : BOM Group관리
		where = "where pid='"+pid+"'";
		String was_fg_code = modDAO.getColumData("MBOM_MASTER","fg_code",where);
		delete = "DELETE from mbom_grade_mgr where keyname='"+was_fg_code+"' ";
		modDAO.executeUpdate(delete);
		
		//MBOM_MASTER 삭제하기
		delete = "DELETE from MBOM_MASTER where pid='"+pid+"' ";
		modDAO.executeUpdate(delete);

		//MBOM_STR 삭제하기
		delete = "DELETE from MBOM_STR where gid='"+pid+"' ";
		modDAO.executeUpdate(delete);

		data = "정상적으로 삭제되었습니다.";
		return data;
	}

}