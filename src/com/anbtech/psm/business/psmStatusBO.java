package com.anbtech.psm.business;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.util.StringTokenizer;

public class psmStatusBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.psm.db.psmModifyDAO psmDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public psmStatusBO(Connection con) 
	{
		this.con = con;
		psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		과제상태(PSM STATUS) 에 관한 메소드 정의
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	* 과제진행상태정보 내용 저장하기 
	*******************************************************************/
	public String inputPsmStatus(String pid,String psm_code,String psm_type,String comp_name,String comp_category,
		String psm_korea,String psm_english,String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,
		String psm_budget,String psm_user,String psm_status,String change_desc) throws Exception
	{
		String input="",data="",update="";
		String change_date = anbdt.getDateNoformat();

		//금일 입력한 내용이 있는지 판단하기
		query = "select count(*) from psm_status ";
		query += "where psm_code='"+psm_code+"' and change_date='"+change_date+"'";
		int cnt = psmDAO.getTotalCount(query);
		if(cnt != 0) {
			data = "금일 과제진행상태 변경내용이 등록되어있습니다.";
			return data;
		} 

		//각담당자별 사업부 코드 구하기
		int pm_div = psm_pm.indexOf("/"); if(pm_div == -1) pm_div = 0;
		String psm_pm_div = psmDAO.getDivCode(psm_pm.substring(0,pm_div));

		int mgr_div = psm_mgr.indexOf("/"); if(mgr_div == -1) mgr_div = 0;
		String psm_mgr_div = psmDAO.getDivCode(psm_mgr.substring(0,mgr_div));

		int budget_div = psm_budget.indexOf("/"); if(budget_div == -1) budget_div = 0;
		String psm_budget_div = psmDAO.getDivCode(psm_budget.substring(0,budget_div));

		int user_div = psm_user.indexOf("/"); if(user_div == -1) user_div = 0;
		String psm_user_div = psmDAO.getDivCode(psm_user.substring(0,user_div));

		//PSM STATUS에 입력하기 : 상태변경관리
		input = "INSERT INTO psm_status (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,change_desc,psm_status,change_date) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+change_desc+"','"+psm_status+"','"+change_date+"')";
		psmDAO.executeUpdate(input);


		//PSM MASTER에 변경내용 UPdate하기
		update = "UPDATE psm_master SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_status='"+psm_status+"' where psm_code='"+psm_code+"'";
		
		psmDAO.executeUpdate(update);
		  

		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  PSM STSTUS에 데이터 수정하기
	//*******************************************************************/	
	public String updatePsmStatus(String pid,String psm_code,String psm_type,String comp_name,String comp_category,
		String psm_korea,String psm_english,String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,
		String psm_budget,String psm_user,String psm_status,String change_desc) throws Exception
	{
		String data="",where="",update="";
		String change_date = anbdt.getDateNoformat();

		//각담당자별 사업부 코드 구하기
		int pm_div = psm_pm.indexOf("/"); if(pm_div == -1) pm_div = 0;
		String psm_pm_div = psmDAO.getDivCode(psm_pm.substring(0,pm_div));

		int mgr_div = psm_mgr.indexOf("/"); if(mgr_div == -1) mgr_div = 0;
		String psm_mgr_div = psmDAO.getDivCode(psm_mgr.substring(0,mgr_div));

		int budget_div = psm_budget.indexOf("/"); if(budget_div == -1) budget_div = 0;
		String psm_budget_div = psmDAO.getDivCode(psm_budget.substring(0,budget_div));

		int user_div = psm_user.indexOf("/"); if(user_div == -1) user_div = 0;
		String psm_user_div = psmDAO.getDivCode(psm_user.substring(0,user_div));

		//PSM STATUS 수정하기
		update = "UPDATE psm_status SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+change_desc;
		update += "',change_date='"+change_date+"',psm_status='"+psm_status+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		//PSM MASTER 수정하기
		update = "UPDATE psm_master SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_status='"+psm_status+"' where psm_code='"+psm_code+"'";
		psmDAO.executeUpdate(update);

		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	// PSM STATUS에 데이터 삭제하기
	//*******************************************************************/	
	public String deletePsm(String pid,String psm_code) throws Exception
	{
		String delete="",data="",update="",where="";

		//최초등록및 확정등록된 마지막 2개는 삭제할 수 없음.(상태이력을 등록한 내용이 아니므로)
		query = "select count(*) from psm_status where psm_code='"+psm_code+"'";
		int cnt = psmDAO.getTotalCount(query);
		if(cnt == 2) {
			data = "최초등록정보(등록/확정)는 삭제할 수 없습니다.";
			return data;
		}

		//마스터는 수정전 단계로 다시 바꿔등록하기
		where = "where psm_code='"+psm_code+"' order by pid desc";
		String second_pid = psmDAO.getSecondData("psm_status","pid",where);

		com.anbtech.psm.entity.psmStatusTable statusT = new com.anbtech.psm.entity.psmStatusTable();
		statusT = psmDAO.readPsmStatus(second_pid,psm_code);

		update = "UPDATE psm_master SET psm_type='"+statusT.getPsmType()+"',comp_name='"+statusT.getCompName();
		update += "',comp_category='"+statusT.getCompCategory()+"',psm_korea='"+statusT.getPsmKorea();
		update += "',psm_english='"+statusT.getPsmEnglish()+"',psm_start_date='"+statusT.getPsmStartDate();
		update += "',psm_end_date='"+statusT.getPsmEndDate()+"',psm_pm='"+statusT.getPsmPm();
		update += "',psm_pm_div='"+statusT.getPsmPmDiv()+"',psm_mgr='"+statusT.getPsmMgr();
		update += "',psm_mgr_div='"+statusT.getPsmMgrDiv()+"',psm_budget='"+statusT.getPsmBudget();
		update += "',psm_budget_div='"+statusT.getPsmBudgetDiv()+"',psm_status='"+statusT.getPsmStatus();
		update +="' where psm_code='"+psm_code+"'";
		psmDAO.executeUpdate(update);

		//상태변경관리
		delete = "DELETE FROM psm_status WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		data = "정상적으로 삭제되었습니다.";
		
		return data;
	}
}
