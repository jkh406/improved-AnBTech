package com.anbtech.psm.business;
import com.anbtech.psm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.util.StringTokenizer;

public class psmProcessBO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.file.FileWriteString write = new com.anbtech.file.FileWriteString();	//내용을 파일로 담기
	private com.anbtech.psm.db.psmProcessDAO prsDAO = null;
	private com.anbtech.psm.db.psmModifyDAO modDAO = null;
	private String query = "";
	private int max_cnt = 0;						//동일한 고객의 최대과제수
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public psmProcessBO(Connection con) 
	{
		this.con = con;
		prsDAO = new com.anbtech.psm.db.psmProcessDAO(con);
		modDAO = new com.anbtech.psm.db.psmModifyDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		전자결재시 예산통제에 관한 메소드 정의
	//			
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	*  해당과제의 진행상태 리턴
	*******************************************************************/
	public String checkProjectStatus(String psm_code) throws Exception
	{
		String where="",status="";

		where = "where psm_code='"+psm_code+"'";
		status = prsDAO.getColumData("psm_master","psm_status",where);
		if(status.equals("11")) status = "1";

		return status;
	}

	/*******************************************************************
	*  해당과제의 재료비 차액금액 리턴
	*******************************************************************/
	public double getBalanceMaterial(String psm_code) throws Exception
	{
		String where="",status="";
		double plan_material=0.0,result_material=0.0,diff_material=0.0;

		where = "where psm_code='"+psm_code+"'";
		plan_material = Double.parseDouble(prsDAO.getColumData("psm_master","plan_material",where));
		result_material = Double.parseDouble(prsDAO.getColumData("psm_master","result_material",where));
		diff_material = plan_material - result_material;

		return diff_material;

	}

	/*******************************************************************
	*  결재승인시 구매실적금액을 반영하기
	*******************************************************************/
	public void saveResultMaterial(String psm_code,double material_price,
		String req_info,String req_div_code,String req_no) throws Exception
	{
		String where="",update="",input="";
		double result_sum=0.0,result_material=0.0,sum_material=0.0;

		//현금액 구하기
		where = "where psm_code='"+psm_code+"'";
		result_material = Double.parseDouble(prsDAO.getColumData("psm_master","result_material",where));
		result_sum = Double.parseDouble(prsDAO.getColumData("psm_master","result_sum",where));

		//입력할 금액구하기
		sum_material = material_price + result_material;
		result_sum += material_price;

		//update하기
		update = "UPDATE psm_master SET result_material='"+sum_material+"',result_sum='"+sum_material+"' ";
		update += "where psm_code='"+psm_code+"'";
		prsDAO.executeUpdate(update);

		//이력으로 등록하기
		where = "where psm_code='"+psm_code+"' order by pid desc";
		String pid = prsDAO.getColumData("psm_budget","pid",where);
		psmBudgetTable B = new com.anbtech.psm.entity.psmBudgetTable();
		B = modDAO.readPsmBudget(pid,psm_code);

		String start = str.repWord(B.getPsmStartDate(),"/","");
		String end = str.repWord(B.getPsmEndDate(),"/","");

		input = "INSERT INTO psm_budget (pid,psm_code,psm_type,comp_name,comp_category,psm_korea,psm_english,";
		input += "psm_start_date,psm_end_date,psm_pm,psm_pm_div,psm_mgr,psm_mgr_div,psm_budget,psm_budget_div,";
		input += "psm_user,psm_user_div,plan_sum,plan_labor,plan_material,";
		input += "plan_cost,plan_plant,change_desc,change_date,budget_type) values('";
		input += anbdt.getID()+"','"+psm_code+"','"+B.getPsmType()+"','"+B.getCompName()+"','"+B.getCompCategory()+"','"+B.getPsmKorea()+"','";
		input += B.getPsmEnglish()+"','"+start+"','"+end+"','"+B.getPsmPm()+"','"+B.getPsmPmDiv()+"','";
		input += B.getPsmMgr()+"','"+B.getPsmMgrDiv()+"','"+B.getPsmBudget()+"','"+B.getPsmBudgetDiv()+"','"+req_info+"','";
		input += req_div_code+"','"+material_price+"','"+0.0+"','";
		input += material_price+"','"+0.0+"','"+0.0+"','";
		input += req_no+"','"+anbdt.getDateNoformat()+"','"+"2"+"')";
		prsDAO.executeUpdate(input);

	}

	//--------------------------------------------------------------------
	//
	//		과제계획 현황조회 에 관한 메소드 정의
	//			
	//			
	//---------------------------------------------------------------------
	/*******************************************************************
	*  업체명/카타고리별 과제명 과제전체를 ArrayList에 담기
	*******************************************************************/
	public ArrayList getTotalProjects(String login_id,String psm_start_date) throws Exception
	{
		String input = "";
		ArrayList table_list = new ArrayList();		//리턴값
		int project_cnt = 0;
		psmMasterTable table = new psmMasterTable();

		//전체 과제 구하기
		ArrayList pjt_list = new ArrayList();
		pjt_list = prsDAO.getCategoryProjects(login_id,psm_start_date);
		project_cnt = pjt_list.size();
		if(project_cnt == 0) return table_list;

		//최대과제수량 구하기
		max_cnt = prsDAO.getMaxCountProjects();
		
		//배열로 담기
		String[][] data = new String[project_cnt][8];
		for(int i=0; i<project_cnt; i++) for(int j=0; j<8; j++) data[i][j]="";
		Iterator pjt_iter = pjt_list.iterator();
		int n=0;
		while(pjt_iter.hasNext()) {
			table = (psmMasterTable)pjt_iter.next();
			data[n][0] = table.getPid();
			data[n][1] = table.getPsmCode();	
			data[n][2] = table.getPsmType();	
			data[n][3] = table.getCompName();	
			data[n][4] = table.getCompCategory();	
			data[n][5] = table.getPsmKorea();	
			data[n][6] = table.getPsmEnglish();
			data[n][7] = table.getPsmStatus();
			n++;
		}

		//-----------------------------------------------------------------
		//각 고객별 카타고리별 최대과제수량만큼씩(동일갯수로 맞춤)
		//ArrayList로 다시 담기
		//-----------------------------------------------------------------
		int ele = 0;
		for(int i=0; i<n-1; i++) {
			//읽은값과 다음것이 같은것
			if(data[i][3].equals(data[i+1][3]) & data[i][4].equals(data[i+1][4])) {
				table = new psmMasterTable();
				table.setPid(data[i][0]);
				table.setPsmCode(data[i][1]);
				table.setPsmType(data[i][2]);
				table.setCompName(data[i][3]);
				table.setCompCategory(data[i][4]);
				table.setPsmKorea(data[i][5]);
				table.setPsmEnglish(data[i][6]);
				table.setPsmStatus(data[i][7]);
				table_list.add(table);
				//System.out.println(data[i][3]+" : "+data[i][4]+" | "+data[i][5]);
				ele++;
			} 
			//읽은값과 다음것이 다른것 : 최대수량과 같지않으면 읽은값 쓰고, 최대수량만큼 빈값쓴다.
			else {
				if(ele != max_cnt) {
					table = new psmMasterTable();
					table.setPid(data[i][0]);
					table.setPsmCode(data[i][1]);
					table.setPsmType(data[i][2]);
					table.setCompName(data[i][3]);
					table.setCompCategory(data[i][4]);
					table.setPsmKorea(data[i][5]);
					table.setPsmEnglish(data[i][6]);
					table.setPsmStatus(data[i][7]);
					table_list.add(table);
					//System.out.println(data[i][3]+" : "+data[i][4]+" | "+data[i][5]);
					ele++;

					//최대갯수만큼 빈값으로 채우기
					int dif = max_cnt-ele;
					for(int m=0; m<dif; m++) {
						table = new psmMasterTable();
						table.setPid("");
						table.setPsmCode("");
						table.setPsmType("");
						table.setCompName("");
						table.setCompCategory("");
						table.setPsmKorea("");
						table.setPsmEnglish("");
						table.setPsmStatus("S");
						table_list.add(table);
						//System.out.println("R"+" : "+"R"+" | "+"R");
					}
				}
				ele = 0;
			}
		}

		//마지막 배열값 무조건 담기
		table = new psmMasterTable();
		table.setPid(data[n-1][0]);
		table.setPsmCode(data[n-1][1]);
		table.setPsmType(data[n-1][2]);
		table.setCompName(data[n-1][3]);
		table.setCompCategory(data[n-1][4]);
		table.setPsmKorea(data[n-1][5]);
		table.setPsmEnglish(data[n-1][6]);
		table.setPsmStatus(data[n-1][7]);
		table_list.add(table);
		//System.out.println(data[n-1][3]+" : "+data[n-1][4]+" | "+data[n-1][5]);
		ele++;

		//최대갯수만큼 빈값으로 채우기
		if(ele != max_cnt) {
			int dif = max_cnt-ele;
			for(int m=0; m<dif; m++) {
				table = new psmMasterTable();
				table.setPid("");
				table.setPsmCode("");
				table.setPsmType("");
				table.setCompName("");
				table.setCompCategory("");
				table.setPsmKorea("");
				table.setPsmEnglish("");
				table.setPsmStatus("S");
				table_list.add(table);
				//System.out.println("R"+" : "+"R"+" | "+"R");
			}
		}

/*		//출력해보기
		Iterator table_iter = table_list.iterator();
		while(table_iter.hasNext()) {
			table = (psmMasterTable)table_iter.next();
			System.out.println(table.getCompName()+":"+table.getCompCategory()+":"+table.getPsmKorea());
		}
*/		
		return table_list;

	}

	/**********************************************************************
	 * 업체명/카타고리별 과제명 중 최대로 많은 과제수량
	 *********************************************************************/
	public int getMaxCountProjects() throws Exception
	{
		return max_cnt;
	}
}