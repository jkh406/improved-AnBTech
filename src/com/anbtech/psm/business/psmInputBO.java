package com.anbtech.psm.business;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.util.StringTokenizer;

public class psmInputBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//내용을 파일로 담기
	private com.anbtech.psm.db.psmModifyDAO psmDAO = null;			
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public psmInputBO(Connection con) 
	{
		this.con = con;
		psmDAO = new com.anbtech.psm.db.psmModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		과제계획(PSM MASTER) 에 관한 메소드 정의
	//			등록/수정/삭제/상태관리 
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	*  과제진행 상태 관리하기
	*******************************************************************/
	public void processStatus(String pid,String psm_status) throws Exception
	{
		String input = "";

		//상태 변경하기
		String update = "UPDATE psm_master SET psm_status='"+psm_status+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		//전자우편 전달 : 과제승인 요청시 
		if(psm_status.equals("11")) sendMail(pid);

		//PSM STATUS에 입력하기 : 상태변경관리
		if(psm_status.equals("2")) {		//승인확정시만 
			com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
			masterT = psmDAO.readPsmMaster(pid);

			String reg_date = anbdt.getDateNoformat();
			pid = anbdt.getID();
			input = "INSERT INTO psm_status (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
			input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
			input += "psm_user,psm_user_div,change_desc,psm_status,change_date) values('";
			input += pid+"','"+masterT.getPsmCode()+"','"+masterT.getPsmType()+"','"+masterT.getCompName()+"','";
			input += masterT.getCompCategory()+"','"+masterT.getPsmKorea()+"','";
			input += masterT.getPsmEnglish()+"','"+str.repWord(masterT.getPsmStartDate(),"/","")+"','"+str.repWord(masterT.getPsmEndDate(),"/","")+"','";
			input += masterT.getPsmPm()+"','"+masterT.getPsmPmDiv()+"','";
			input += masterT.getPsmMgr()+"','"+masterT.getPsmMgrDiv()+"','";
			input += masterT.getPsmBudget()+"','"+masterT.getPsmBudgetDiv()+"','";
			input += masterT.getPsmUser()+"','"+masterT.getPsmUserDiv()+"','";
			input += "과제확정"+"','"+psm_status+"','"+reg_date+"')";
			psmDAO.executeUpdate(input);
		}

	}
	/*******************************************************************
	* 과제기본정보 내용 저장하기 
	*******************************************************************/
	public String inputPsm(String pid,String psm_type,String comp_name,String comp_category,String psm_korea,String psm_english,
		String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,String psm_budget,String psm_user,String psm_desc,String plan_sum,
		String plan_labor,String plan_material,String plan_cost,String plan_plant,String contract_date,String contract_name,
		String contract_price,String complete_date,String pd_code,String pd_name,String psm_kind,
		String psm_view,String link_code) throws Exception
	{
		String input="",data="",update="",where="";
		String reg_date = anbdt.getDateNoformat();

		//각담당자별 사업부 코드 구하기
		int pm_div = psm_pm.indexOf("/"); if(pm_div == -1) pm_div = 0;
		String psm_pm_div = psmDAO.getDivCode(psm_pm.substring(0,pm_div));

		int mgr_div = psm_mgr.indexOf("/"); if(mgr_div == -1) mgr_div = 0;
		String psm_mgr_div = psmDAO.getDivCode(psm_mgr.substring(0,mgr_div));

		int budget_div = psm_budget.indexOf("/"); if(budget_div == -1) budget_div = 0;
		String psm_budget_div = psmDAO.getDivCode(psm_budget.substring(0,budget_div));

		int user_div = psm_user.indexOf("/"); if(user_div == -1) user_div = 0;
		String psm_user_div = psmDAO.getDivCode(psm_user.substring(0,user_div));

		//PSM CODE 자동채번 구하기
		String psm_code = getPsmCode(comp_category,psm_type,pd_code);			//PSM CODE 자동채번

		//메인과제인지 서브과제인지 판단하기 
		if(link_code.length() != 0) {	//서브과제임.
			psm_view="N";		
		}

		//PSM MASTER에 입력하기 : 마스터
		input = "INSERT INTO psm_master (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,psm_desc,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,result_sum,result_labor,result_material,result_cost,result_plant,";
		input += "contract_date,contract_name,contract_price,complete_date,fname,sname,ftype,fsize,";
		input += "psm_status,reg_date,app_date,pd_code,pd_name,psm_kind,psm_view,link_code) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+psm_desc+"','"+Double.parseDouble(plan_sum)+"','";
		input += Double.parseDouble(plan_labor)+"','"+Double.parseDouble(plan_material)+"','";
		input += Double.parseDouble(plan_cost)+"','"+Double.parseDouble(plan_plant)+"','";
		input += "0"+"','"+"0"+"','"+"0"+"','"+"0"+"','"+"0"+"','";
		input += contract_date+"','"+contract_name+"','"+Double.parseDouble(contract_price)+"','";
		input += complete_date+"','"+""+"','"+""+"','"+""+"','"+""+"','"+"1"+"','"+reg_date+"','";
		input += ""+"','"+pd_code+"','"+pd_name+"','"+psm_kind+"','"+psm_view+"','"+link_code+"')";
		psmDAO.executeUpdate(input);

		//메인과제와 서브과제로 나눠지는 경우 메인과제에 반영하기
		//조건 : link_code가 있으면 link_code는 메인과제가 됨.
		//메인과제의 psm_view='VM'로, psm_kind='S'로, link_code=psm_code를 update한다.
		if(link_code.length() != 0) {	//메인과 서브과제로 나눠지는과제임
			//현재의 link_code값을 구하여 더한후
			where = "where psm_code = '"+link_code+"'";
			String exist_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			if(exist_link_code.length() != 0) exist_link_code += ","+psm_code; 
			else exist_link_code = psm_code;
			
			//수정하기
			update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+exist_link_code;
			update += "' where psm_code='"+link_code+"'";
			psmDAO.executeUpdate(update);
		}

		//PSM STATUS에 입력하기 : 상태변경관리
		input = "INSERT INTO psm_status (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,change_desc,psm_status,change_date) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+"과제등록"+"','"+"1"+"','"+reg_date+"')";
		psmDAO.executeUpdate(input);

		//PSM BUDGET에 입력하기 : 예산수정관리
		input = "INSERT INTO psm_budget (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,change_desc,change_date,budget_type) values('";
		input += pid+"','"+psm_code+"','"+psm_type+"','"+comp_name+"','"+comp_category+"','"+psm_korea+"','";
		input += psm_english+"','"+psm_start_date+"','"+psm_end_date+"','"+psm_pm+"','"+psm_pm_div+"','";
		input += psm_mgr+"','"+psm_mgr_div+"','"+psm_budget+"','"+psm_budget_div+"','"+psm_user+"','";
		input += psm_user_div+"','"+Double.parseDouble(plan_sum)+"','"+Double.parseDouble(plan_labor)+"','";
		input += Double.parseDouble(plan_material)+"','"+Double.parseDouble(plan_cost)+"','"+Double.parseDouble(plan_plant)+"','";
		input += "과제등록"+"','"+reg_date+"','"+"1"+"')";
		psmDAO.executeUpdate(input);

		data = "정상적으로 등록되었습니다.";
		return data;
	}

	//*******************************************************************
	//  PSM MASTER에 데이터 수정하기
	//*******************************************************************/	
	public String updatePsm(String pid,String psm_type,String comp_name,String comp_category,String psm_korea,String psm_english,
		String psm_start_date,String psm_end_date,String psm_pm,String psm_mgr,String psm_budget,String psm_user,String psm_desc,String plan_sum,
		String plan_labor,String plan_material,String plan_cost,String plan_plant,String contract_date,String contract_name,
		String contract_price,String complete_date,String pd_code,String pd_name,String psm_kind,
		String psm_view,String link_code) throws Exception
	{
		String data="",where="",psm_status="",update="";
		String reg_date = anbdt.getDateNoformat();

		//진행상태 검사
		where = "where pid = '"+pid+"'";
		psm_status = psmDAO.getColumData("PSM_MASTER","psm_status",where);
		if(!psm_status.equals("1")) {
			data = "미진행상태일때만 수정이 가능합니다.";
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

		//과제분류하여 반영하기 : 수정전 데이터를 이용함.
		String[] d = new String[2];
		d = updateMasterSubLinkCode(pid,link_code,psm_kind,psm_view);
		psm_kind = d[0];
		psm_view = d[1];
		if(psm_kind.length() > 2) {		//불가능 경우로 리턴함
			data = psm_kind;
			return data;
		}

		//PSM MASTER 수정하기
		update = "UPDATE psm_master SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',psm_desc='"+psm_desc;
		update += "',plan_sum='"+Double.parseDouble(plan_sum)+"',plan_labor='"+Double.parseDouble(plan_labor);
		update += "',plan_material='"+Double.parseDouble(plan_material)+"',plan_cost='"+Double.parseDouble(plan_cost);
		update += "',plan_plant='"+Double.parseDouble(plan_plant)+"',contract_date='"+contract_date;
		update += "',contract_name='"+contract_name+"',contract_price='"+Double.parseDouble(contract_price);
		update += "',reg_date='"+reg_date+"',complete_date='"+complete_date;
		update += "',pd_code='"+pd_code+"',pd_name='"+pd_name+"',psm_kind='"+psm_kind+"',psm_view='"+psm_view;
		update += "',link_code='"+link_code+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);
	
		//PSM STATUS 수정하기
		update = "UPDATE psm_status SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+"과제등록";
		update += "',change_date='"+reg_date+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		//PSM BUDGET 수정하기
		update = "UPDATE psm_budget SET psm_type='"+psm_type+"',comp_name='"+comp_name+"',comp_category='"+comp_category;
		update += "',psm_korea='"+psm_korea+"',psm_english='"+psm_english+"',psm_start_date='"+psm_start_date;
		update += "',psm_end_date='"+psm_end_date+"',psm_pm='"+psm_pm+"',psm_pm_div='"+psm_pm_div;
		update += "',psm_mgr='"+psm_mgr+"',psm_mgr_div='"+psm_mgr_div+"',psm_budget='"+psm_budget+"',psm_budget_div='"+psm_budget_div;
		update += "',psm_user='"+psm_user+"',psm_user_div='"+psm_user_div+"',change_desc='"+"과제등록";
		update += "',plan_sum='"+Double.parseDouble(plan_sum)+"',plan_labor='"+Double.parseDouble(plan_labor);
		update += "',plan_material='"+Double.parseDouble(plan_material)+"',plan_cost='"+Double.parseDouble(plan_cost);
		update += "',plan_plant='"+Double.parseDouble(plan_plant);
		update += "',change_date='"+reg_date+"' where pid='"+pid+"'";
		psmDAO.executeUpdate(update);

		data = "정상적으로 수정 되었습니다.";
		return data;
	}

	//*******************************************************************
	//  PSM MASTER에 데이터 메인/서브 과제 판단 수정하기
	//*******************************************************************/	
	public String[] updateMasterSubLinkCode(String pid,String link_code,String psm_kind,String psm_view) throws Exception
	{
		String where="",update="",e_link_code="",pjt_kind="",psm_code="";
		String main_link_code="";
		String[] data = new String[2];		//psm_kind, psm_view
		data[0] = psm_kind;
		data[1] = psm_view;

		//과제종류 판단하기 (psm_view=V[단독], VM[메인], N[서브] 과제임)
		where = "where pid = '"+pid+"'";
		pjt_kind = psmDAO.getColumData("PSM_MASTER","psm_view",where);
		e_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);	//기존 link_code 구하기
		psm_code = psmDAO.getColumData("PSM_MASTER","psm_code",where);		//과제코드

		//---------------------------------------------
		//과제종류별 가능한 경우의 해당내용 반영하기
		//---------------------------------------------
		//1.기존 단독과제인 경우
		if(pjt_kind.equals("V")) {
				//1.수정조건이 단독
				if(link_code.length() == 0) {
					return data;		
				}
				//2.메인과 서브로 나눠지면
				else {
					//메인과제에 update
					//link_code값을 구하여 더한후
					where = "where psm_code = '"+link_code+"'";
					main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
					if(main_link_code.length() != 0) main_link_code += ","+psm_code; 
					else main_link_code = psm_code;
				
					//수정하기
					update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+main_link_code;
					update += "' where psm_code='"+link_code+"'";
					psmDAO.executeUpdate(update);

					//해당과제는 서브과제로 리턴값 구하기
					data[0] = "M";
					data[1] = "N";
				}
		} 
		//2.기존 메인과제인 경우
		else if(pjt_kind.equals("VM")) {
				return data;
		}
		//3.기존 서브과제인 경우
		else if(pjt_kind.equals("N")) {
				//단독과제로 변경된 경우
				if(link_code.length() == 0) {
						//메인과제 반영하기
						//기존link_code값을 구하여 해당 신규link_code를 뺀다.
						where = "where psm_code = '"+e_link_code+"'";
						main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
						if(main_link_code.length() != 0) {
							main_link_code = str.repWord(main_link_code,psm_code,"");
							//필요없는 콤마(,) 빼기
							main_link_code = str.repWord(main_link_code,",,",",");	//중간에서 뺀경우
							if(main_link_code.length() != 0) {
								if(main_link_code.substring(0,1).equals(","))			//처음,이면 없애
									main_link_code=main_link_code.substring(1,main_link_code.length());
								if(main_link_code.charAt(main_link_code.length()-1) == ','){//끝이,이면 없애
									int len = main_link_code.length();
									main_link_code=main_link_code.substring(0,len-1);
								}
							}
						}
						//메인에서 단독으로 변경된 경우
						if(main_link_code.length() == 0) {
							psm_kind="M";
							psm_view="V";
						} 
						//계속메인이면
						else {
							psm_kind="S";
							psm_view="VM";
						}
						//수정하기
						update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
						update += "' where psm_code='"+e_link_code+"'";
						psmDAO.executeUpdate(update);
						
						//해당과제를 단독과제로 변경할 리턴값 구하기
						data[0] = "M";
						data[1] = "V";
				}
				//서브과제 수정의 경우
				else {
						//link_code가 기존과 같은경우
						if(link_code.equals(e_link_code)) {
							return data;
						} 
						//다른경우[메인과제가 변경됨]
						else {
							//1. 기존 메인과제 반영하기
							//기존e_link_code값을 구하여 위의 psm_code 뺀후 update
							where = "where psm_code = '"+e_link_code+"'";
							main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);

							if(main_link_code.length() != 0) {
								main_link_code = str.repWord(main_link_code,psm_code,"");
				
								//필요없는 콤마(,) 빼기
								main_link_code = str.repWord(main_link_code,",,",",");	//중간에서 뺀경우
								if(main_link_code.length() != 0) {
									if(main_link_code.substring(0,1).equals(","))		//처음,이면 없애
										main_link_code=main_link_code.substring(1,main_link_code.length());
									if(main_link_code.charAt(main_link_code.length()-1) == ','){//끝이,이면 없애
										int len = main_link_code.length();
										main_link_code=main_link_code.substring(0,len-1);
									}
								}
								//조건구하기
								if(main_link_code.length() == 0) {
									psm_view="V";
									psm_kind="M";
								}
							}
							update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
							update += "' where psm_code='"+e_link_code+"'";
							psmDAO.executeUpdate(update);

							//2. 신규 메인과제 반영하기
							//기존link_code값을 구하여 신규 link_code추가 
							where = "where psm_code = '"+link_code+"'";
							main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
							if(main_link_code.length() != 0) main_link_code += ","+psm_code; 
						
							//수정하기
							update = "UPDATE psm_master SET psm_view='VM',psm_kind='S',link_code='"+psm_code;
							update += "' where psm_code='"+link_code+"'";
							psmDAO.executeUpdate(update);

						}
				} //if
		} //if
		return data;
	}

	//*******************************************************************
	// PSM MASTER에 데이터 삭제하기
	//*******************************************************************/	
	public String deletePsm(String pid,String filepath) throws Exception
	{
		String delete = "",data="",psm_status="",psm_view="",link_code="",psm_code="";
		String main_link_code="",psm_kind="",update="";
		String where = "where pid='"+pid+"'";

		//진행상태 검사
		psm_status = psmDAO.getColumData("PSM_MASTER","psm_status",where);
		if(!psm_status.equals("1")) {
			data = "미진행상태일때만 삭제가 가능합니다.";
			return data;
		}
		
		//메인과제이면 서브과제에서 수정후 삭제가능함
		psm_view = psmDAO.getColumData("PSM_MASTER","psm_view",where);
		if(psm_view.equals("VM")) {
			data = "메인과제는 삭제할 수 없습니다. 해당 서브과제에서 메인과제를 해지후 진행하십시오.";
			return data;
		}

		//서브과제이면 해당 메인과제에 반영후 삭제
		if(psm_view.equals("N")) {
			link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			psm_code = psmDAO.getColumData("PSM_MASTER","psm_code",where);

			//해당메인과제에 반영하기
			where = "where psm_code = '"+link_code+"'";
			main_link_code = psmDAO.getColumData("PSM_MASTER","link_code",where);
			if(main_link_code.length() != 0) {
					main_link_code = str.repWord(main_link_code,psm_code,"");

					//필요없는 콤마(,) 빼기
					main_link_code = str.repWord(main_link_code,",,",",");	//중간에서 뺀경우
					if(main_link_code.substring(0,1).equals(","))			//처음,이면 없애
							main_link_code=main_link_code.substring(1,main_link_code.length());
					if(main_link_code.charAt(main_link_code.length()) == ','){//끝이,이면 없애
							int len = main_link_code.length();
							main_link_code=main_link_code.substring(0,len-1);
					}
			}
			//메인과제에서 단독과제로 바뀔때
			if(main_link_code.length() == 0) {
				psm_view="V";
				psm_kind="M";
			} 
			//메인과제로 지속될때
			else {
				psm_view="VM";
				psm_kind="S";
			}
			update = "UPDATE psm_master SET psm_view='"+psm_view+"',psm_kind='"+psm_kind+"',link_code='"+main_link_code;
			update += "' where psm_code='"+link_code+"'";
			psmDAO.executeUpdate(update);

		}

		//첨부파일 삭제하기
		com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
		masterT = psmDAO.readPsmMaster(pid);
		String sname = masterT.getSname();								//첨부파일 저장명
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}


		//마스터
		delete = "DELETE FROM psm_master WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		//상태변경관리
		delete = "DELETE FROM psm_status WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		//예산수정관리
		delete = "DELETE FROM psm_budget WHERE pid='"+pid+"'";
		psmDAO.executeUpdate(delete);

		data = "정상적으로 삭제되었습니다.";
		
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		첨부 문서 저장하기 
	//
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* 첨부파일 저장하기 (신규로 처음 첨부할때)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String tablename,String pid,String save_id,String filepath) throws Exception
	{
		String filename = "";		//원래이름 파일명
		String savename = "";		//저장 파일명
		String filetype = "";		//원래이름 파일 확장자명
		String filesize = "";		//원래이름 파일사이즈

		int i = 1;					//첨부저장 확장자
		int atcnt = 0;				//첨부파일 수량
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//해당파일 읽기
			String name = "attachfile"+i;		//upload한 input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload한 파일명
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload한 파일type
				//file size구하기
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				filename += fname + "|";
				savename += save_id + "_" + i + "|";
				filetype += ftype + "|";
				filesize += fsize + "|";
				atcnt++;
			}
			i++;
		}//while

		//Table에 저장하기
		if(i > 1) {
			setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* 첨부파일 저장하기 (임시저장후 수정하여 첨부할때)
	* save_head : 저장할 파일의 선두문자
	* delfile 은 삭제할 파일임
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String tablename,String pid,String save_head,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt,String[] chkDelFile) throws Exception
	{
		String save_id = save_head+anbdt.getID();		//신규로 저장할 파일명
		String filename = "";		//원래이름 파일명
		String savename = "";		//저장 파일명
		String filetype = "";		//원래이름 파일 확장자명
		String filesize = "";		//원래이름 파일사이즈
		int att_cnt = Integer.parseInt(attache_cnt);	//첨부파일 최대수량 미만
		String newdata = "";
		
		//------------------------------
		//기존파일중 check된 파일 삭제하기
		//------------------------------
		int cnt = chkDelFile.length;
		for(int i=0; i<cnt; i++) {
			String dfile = filepath+"/"+chkDelFile[i];
			File FN = new File(dfile);
			if(FN.exists()) FN.delete();	
		}

		//------------------------------
		//기존의 파일정보를 배열에 담기
		//------------------------------
		String[][] fdata = null;
		int flen = fname.length();
		int alen = 0,hi = 0;			//기존등록된 파일수
		if(flen != 0) {
			for(int i=0; i<flen; i++) if(fname.charAt(i) == '|') alen++;
			fdata = new String[alen][4];

			//기존저장 원래파일명
			fname = fname.substring(0,fname.length());	//마지막 '|'제거
			java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			
			hi = 0;
			while(o_fname.hasMoreTokens()) {
				String read = o_fname.nextToken();
				if(read.length() != 0) fdata[hi][0] = read;
				hi++;
			}
			//기존저장 저장파일명
			sname = sname.substring(0,sname.length());	//마지막 '|'제거
			java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			
			hi = 0;
			while(o_sname.hasMoreTokens()) {
				String read = o_sname.nextToken();
				if(read.length() != 0) fdata[hi][1] = read;
				hi++;
			}
			//기존저장 저장파일Type명
			ftype = ftype.substring(0,ftype.length());	//마지막 '|'제거
			java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			
			hi = 0;
			while(o_ftype.hasMoreTokens()) {
				String read = o_ftype.nextToken();
				if(read.length() != 0) fdata[hi][2] = read;
				hi++;
			}
			//기존저장 저장파일 Size
			fsize = fsize.substring(0,fsize.length());	//마지막 '|'제거
			java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			
			hi = 0;
			while(o_fsize.hasMoreTokens()) {
				String read = o_fsize.nextToken();
				if(read.length() != 0) fdata[hi][3] = read;
				hi++;
			}

			//fdata배열중 삭제된 내용이 있으면 해당배열의 값을 clear시킨다.
			for(int i=0; i<cnt; i++) {
				if(chkDelFile[i].length() != 0) fdata[i][0]=fdata[i][1]=fdata[i][2]=fdata[i][3]="";
			}
		}

		//------------------------------
		//신규로 첨부한 파일
		//------------------------------
		int i = 1;		//첨부파일확장자
		int n = 0;		//저장배열을 위해
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();							//해당파일 읽기
			String name = "attachfile"+i;					//upload한 input file type name parameter
			String uname = multi.getFilesystemName(name);	//upload한 파일명
			
			//기존저장된 파일이 있으면 삭제하기
			if((alen > n) && (uname != null)) {
				String delfile = filepath+"/"+fdata[n][1]+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}

			//첨부한 파일 이름바꿔 저장하기
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload한 파일type
				//file size구하기
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				newdata += uname + "|";
				newdata += save_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
			}
			else newdata += " | | |;";
			i++;
			n++;
		}//while

		//------------------------------
		//DB로 저장할 data 만들기
		//------------------------------
		//신규로 저장한 파일을 배열에 담기
		String[][] ndata = new String[att_cnt-1][4];
		for(int a=0; a<n; a++) for(int b=0; b<4; b++) ndata[a][b] = "";

		java.util.StringTokenizer rdata = new StringTokenizer(newdata,";");
		int ai = 0;
		while(rdata.hasMoreTokens()) {
			String nnd = rdata.nextToken();		//1라인 읽기
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				ndata[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		

		//저장할 변수로 나누기
		int atcnt = 0;			//첨부파일 갯수 리턴값
		for(int p=0; p<att_cnt-1; p++) {
			//새로첨부한 내용만 있을때
			if((n >= p) && (ndata[p][0].length() > 1)) {				
				filename += ndata[p][0] + "|";
				savename += ndata[p][1] + "|";
				filetype += ndata[p][2] + "|";
				filesize += ndata[p][3] + "|"; 
				atcnt++;
			} 
			//신규로 첨부한 파일이 있으면서 기존등록된 내용이 있을때
			else if((alen > p) && (ndata[p][0].length() <= 1)) {		
				if(fdata[p][0].length() != 0) {		//삭제된 내용은 제외
					filename += fdata[p][0] + "|";
					savename += fdata[p][1] + "|";
					filetype += fdata[p][2] + "|";
					filesize += fdata[p][3] + "|"; 
					atcnt++;
				}
			} 
		}

		//------------------------------
		//Table에 저장하기
		//------------------------------
		setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		
		return atcnt;

	}
	/*******************************************************************
	* 첨부파일 저장한후 Table에 update하기
	 *******************************************************************/
	 private void setAddFileUpdate(String tablename,String pid, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update "+tablename+" set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where pid='"+pid+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	//--------------------------------------------------------------------
	//
	//		공통 메소드 작성
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// PSM CODE 자동 채번 생성하기
	//*******************************************************************/	
	public String getPsmCode(String comp_category,String psm_type,String pd_code) throws Exception
	{
		String psm_code = "",where="",head="T";
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("00");

		//과제종류[예비과제:T,정식과제:제품코드]에 따라 선두문자를 구분한다.
		where = "where env_name like '"+psm_type+"%'";
		String pjt_kind = psmDAO.getColumData("psm_env","env_status",where);
		if(pjt_kind.equals("2")) head=pd_code;

		//검색할 String만들기 : PSM CODE : HEADERCHAR + YY + '-' + 카테고리약어 + nn 
		String yy = anbdt.getYear();
		where = "where korea_name='"+comp_category+"'";
		String key_word = psmDAO.getColumData("psm_category","key_word",where);
		String psm_body = head+yy.substring(2,4)+"-"+key_word;

		//등록된 PSM CODE을 구한다.
		where = "where psm_code like '"+psm_body+"%' order by psm_code desc";
		String was_psm_code = psmDAO.getColumData("psm_master","psm_code",where);

		//구한 psm_code 에서 일련번호만 +1로 한다.
		if(was_psm_code.length() == 0) {
			psm_code = psm_body + "01";
		} else {
			String serial_no = was_psm_code.substring(was_psm_code.length()-2,was_psm_code.length());
			int s_no = Integer.parseInt(serial_no)+1;
			psm_code = psm_body + nmf.toDigits(s_no);
		}

		return psm_code;
	}

	/***************************************************************************
	 * 담당자에게 전자우편으로 알려주기
	 * pid : 관리번호 
	 **************************************************************************/
	private void sendMail(String pid) throws Exception  
	{
		String share_id="";				//share_id (수신자) : 사번/이름;
		String post_bon_path="",w_date="",delete_date="",filename="",mquery="",lquery="",subject="";
		String psm_code="",psm_type="",comp_name="",comp_cate="",psm_per="";
		String mgr_id = "",mgr_name ="";			//보내는사람 : 사번, 이름

		//-------------------------------------------------------
		// 관련내용 만들기
		//-------------------------------------------------------
		com.anbtech.psm.entity.psmMasterTable masterT = new com.anbtech.psm.entity.psmMasterTable();
		masterT = psmDAO.readPsmMaster(pid);

		String psm_user = masterT.getPsmUser();					//사번/이름
		int sn = psm_user.indexOf("/");
		mgr_id = psm_user.substring(0,sn);						//작성자 사번 (전자우편 보내는 사번)
		mgr_name = psm_user.substring(sn+1,psm_user.length());	//작성자 이름

		share_id = masterT.getPsmMgr()+";";						//수신자
		subject = " 과제확정요청: "+masterT.getPsmKorea();		//제목
		post_bon_path = "/post/"+mgr_id+"/text_upload";			//post에 저장할 본문path
		pid = anbdt.getID();									//전자우편 저장 관리번호
		w_date = anbdt.getTime();								//작성일
		delete_date = anbdt.getAddMonthNoformat(1);				//삭제예정일
		filename = pid;											//본문저장파일명
		psm_code = masterT.getPsmCode();						//과제코드
		psm_type = masterT.getPsmType();						//과제종류
		comp_name = masterT.getCompName();						//과제고객
		comp_cate = masterT.getCompCategory();					//과제카테고리
		psm_per = masterT.getPsmStartDate()+" ~ "+masterT.getPsmEndDate();	//과제기간

		//-------------------------------------------------------
		//관련내용 post tabel(post_master,post_letter)로 담기 
		//-------------------------------------------------------
		mquery = "insert into post_master(pid,post_subj,writer_id,writer_name,write_date,";
		mquery += "post_receiver,isopen,post_state,post_select,bon_path,bon_file,delete_date) values('";
		mquery += pid+"','"+subject+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+share_id+"','";
		mquery += "0"+"','"+"email"+"','"+"CFM"+"','"+post_bon_path+"','"+filename+"','"+delete_date+"')";
		psmDAO.executeUpdate(mquery);


		lquery = "insert into post_letter(pid,post_subj,writer_id,writer_name,write_date,";
		lquery += "post_receiver,isopen,post_select,delete_date) values('";
		String receivers = share_id;		//개별사번만을 찾아 입력하기
		StringTokenizer dd = new StringTokenizer(receivers,";");
		while(dd.hasMoreTokens()) {
			String rd = dd.nextToken();		rd=rd.trim();		//사번/이름
			if(rd.length() > 5) {
				String sabun = rd.substring(0,rd.indexOf("/"));
				String lq = lquery + pid+"','"+subject+"','"+mgr_id+"','"+mgr_name+"','"+w_date+"','"+sabun+"','";
				lq += "0"+"','"+"CFM"+"','"+delete_date+"')";
				psmDAO.executeUpdate(lq);
			}
		}
		
		//-------------------------------------------------------
		//본문파일 만들기
		//-------------------------------------------------------
		String upload_path = "";
		upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");		//upload_path
		String servlet = com.anbtech.admin.db.ServerConfig.getConf("serverURL");	//servlet path
		// 공문 본문내용 만들기
		String content = "<html><head><title>과제확정요청</title></head>";
		content += "<body>";
		content += "과제확정 요청 내용입니다.<br>";
		content += "상세내용은 과제모듈에서 확인하시고 과제확정 요청합니다.<br>";
		content += "<br><br><br>";
		content += "과제번호 : "+psm_code+"<br>";
		content += "과제종류 : "+psm_type+"<br>";
		content += "과제고객 : "+comp_name+"<br>";
		content += "과제카테고리 : "+comp_cate+"<br>";
		content += "과제기간 : "+psm_per+"<br>";
		content += "</body></html>";

		// 전자우편용 본문내용 파일만들기
		String path = upload_path + "/gw/mail" + post_bon_path;					//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

	}
}
