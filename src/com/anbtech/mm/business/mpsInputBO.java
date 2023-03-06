package com.anbtech.mm.business;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mpsInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.mm.db.mpsModifyDAO mpsDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mpsInputBO(Connection con) 
	{
		this.con = con;
		mpsDAO = new com.anbtech.mm.db.mpsModifyDAO(con);
	}
	//--------------------------------------------------------------------
	//
	//		생산계획(MPS MASTER) 에 관한 메소드 정의
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------

	//*******************************************************************
	// MPS MASTER에 데이터 입력하기
	//*******************************************************************/	
	public String insertMps(String order_no,String mps_type,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String plan_date,String plan_count,
		String item_unit,String factory_no,String factory_name,String reg_date,String reg_id,
		String reg_name,String order_comp) throws Exception
	{
		String input="",data="",where="",bom_status="",gid="",mps_no="",purpose="";
		
		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		purpose = mpsDAO.getColumData("MBOM_MASTER","purpose",where);			//BOM구성목적
		if(!bom_status.equals("5")) {
			data = "BOM구성이 안된 모델입니다. 먼저 BOM을 구성후 생산계획을 수립하십시오.";
			return data;
		}

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "품목번호는 FG코드로 구성된 BOM내의 제품 또는 반제품만 등록할 수 있습니다.";
			return data;
		}

		//MPS관리번호 구하기
		mps_no = mpsDAO.getMpsNo(factory_no);
		
		//MPS MASTER에 입력하기 
		String pid = anbdt.getID();
		input = "INSERT INTO mps_master (pid,mps_no,order_no,mps_type,model_code,model_name,fg_code,";
		input += "item_code,item_name,item_spec,plan_date,plan_count,sell_count,item_unit,mps_status,factory_no,";
		input += "factory_name,reg_date,reg_id,reg_name,app_date,app_id,app_no,order_comp,purpose) values('";
		input += pid+"','"+mps_no+"','"+order_no+"','"+mps_type+"','"+model_code+"','"+model_name+"','";
		input += fg_code+"','"+item_code+"','"+item_name+"','"+item_spec+"','"+plan_date+"','"+plan_count+"','"+"0"+"','";
		input += item_unit+"','"+"1"+"','"+factory_no+"','"+factory_name+"','"+reg_date+"','"+reg_id+"','";
		input += reg_name+"','"+""+"','"+""+"','"+""+"','"+order_comp+"','"+purpose+"')";
		//System.out.println("input : " + input);
		mpsDAO.executeUpdate(input);
		
		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  MPS MASTER에 데이터 수정하기
	//*******************************************************************/	
	public String updateMps(String pid,String order_no,String mps_type,String model_code,String model_name,
		String fg_code,String item_code,String item_name,String item_spec,String plan_date,String plan_count,
		String item_unit,String factory_no,String factory_name,String reg_date,String order_comp) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="",purpose="";

		//해당FG코드가 BOM을 구성한 품목인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		bom_status = mpsDAO.getColumData("MBOM_MASTER","bom_status",where);
		purpose = mpsDAO.getColumData("MBOM_MASTER","purpose",where);			//BOM구성목적
		if(!bom_status.equals("5")) {
			data = "BOM구성이 안된 모델입니다. 먼저 BOM을 구성후 생산계획을 수립하십시오.";
			return data;
		}

		//해당품목이 BOM을 구성한 품목에 포함된 제품 또는 반제품(ASSY코드)인지 판단하기
		where = "where fg_code='"+fg_code+"'";
		gid = mpsDAO.getColumData("MBOM_MASTER","pid",where);			//BOM통합관리코드
		int cnt = mpsDAO.checkItemCode(gid,item_code);
		if(cnt == 0) {
			data = "품목번호는 FG코드로 구성된 BOM내의 제품 또는 반제품만 등록할 수 있습니다.";
			return data;
		}

		//진행상태가 작성중일때만 수정이 가능함
		where = "where pid='"+pid+"'";
		bom_status = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		if(!bom_status.equals("1")) {
			data = "작성중일때만 수정이 가능합니다.";
			return data;
		}
		
		//MBOM_STR에 수정하기
		update = "UPDATE mps_master SET order_no='"+order_no+"',mps_type='"+mps_type;
		update += "',model_code='"+model_code+"',model_name='"+model_name+"',fg_code='"+fg_code;
		update += "',item_code='"+item_code+"',item_name='"+item_name+"',item_spec='"+item_spec+"',plan_date='"+plan_date;
		update += "',plan_count='"+plan_count+"',item_unit='"+item_unit+"',factory_no='"+factory_no;
		update += "',factory_name='"+factory_name+"',reg_date='"+reg_date;
		update += "',order_comp='"+order_comp+"',purpose='"+purpose+"' where pid='"+pid+"'";
		mpsDAO.executeUpdate(update);
		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// MPS MASTER에 데이터 삭제하기
	//*******************************************************************/	
	public String deleteMps(String pid) throws Exception
	{
		String delete = "",data="",mps_status="";
		String where = "where pid='"+pid+"'";

		//진행상태 검사
		mps_status = mpsDAO.getColumData("MPS_MASTER","mps_status",where);
		if(!mps_status.equals("1")) {
			data = "작성상태일때만 삭제가 가능합니다.";
			return data;
		} 

		delete = "DELETE FROM mps_master WHERE pid='"+pid+"'";
		mpsDAO.executeUpdate(delete);
		data = "정상적으로 삭제되었습니다.";
		
		return data;
	}

	//*******************************************************************
	//  MPS MASTER에 상태관리하기
	//*******************************************************************/	
	public String setMpsStatus(String pid,String mps_status,String login_id,String login_name) throws Exception
	{
		String data="",where="",bom_status="",update="",gid="";

		//MPS MASTER에 수정하기
		update = "UPDATE mps_master SET mps_status='"+mps_status+"' where pid='"+pid+"'";
		mpsDAO.executeUpdate(update);

		if(mps_status.equals("3")) {
			update = "UPDATE mps_master SET app_date='"+anbdt.getDateNoformat()+"', ";
			update += "app_id='"+login_id+"/"+login_name+"' where pid='"+pid+"'";
			mpsDAO.executeUpdate(update);
			data = "정상적으로 MPS확정 되었습니다.";
		} else if(mps_status.equals("2")) {
			data = "정상적으로 MPS확정요청 되었습니다.";
		} else if(mps_status.equals("1")) { 
			data = "정상적으로 MPS확정취소 되었습니다.";
		}
		return data;
	}
}

