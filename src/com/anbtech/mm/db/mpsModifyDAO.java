package com.anbtech.mm.db;
import com.anbtech.mm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class mpsModifyDAO
{
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자입력
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자
	private com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat();	//포멧
	private String query = "";
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public mpsModifyDAO(Connection con) 
	{
		this.con = con;
	}

	//--------------------------------------------------------------------
	//
	//		MPS MASTER 에 관한 메소드 정의
	//
	//
	//---------------------------------------------------------------------

	//*******************************************************************
	// 관리번호로 해당 MPS_MASTER정보 읽기
	//*******************************************************************/	
	public mpsMasterTable readMasterItem(String pid,String factory_no,String view_td) throws Exception
	{
		//변수 초기화
		String where = "";
		Statement stmt = null;
		ResultSet rs = null;
		
		stmt = con.createStatement();
		mpsMasterTable table = new com.anbtech.mm.entity.mpsMasterTable();
		
		query = "SELECT * FROM mps_master where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			table.setPid(rs.getString("pid"));	
			table.setMpsNo(rs.getString("mps_no"));	
			table.setOrderNo(rs.getString("order_no"));	
			table.setMpsType(rs.getString("mps_type"));	
			table.setModelCode(rs.getString("model_code"));	
			table.setModelName(rs.getString("model_name"));	
			table.setFgCode(rs.getString("fg_code"));
			table.setItemCode(rs.getString("item_code"));
			table.setItemName(rs.getString("item_name"));
			table.setItemSpec(rs.getString("item_spec"));
			table.setPlanDate(rs.getString("plan_date"));
			table.setPlanCount(rs.getInt("plan_count"));
			table.setSellCount(rs.getInt("sell_count"));
			table.setItemUnit(rs.getString("item_unit"));
			table.setMpsStatus(rs.getString("mps_status"));
			table.setFactoryNo(rs.getString("factory_no"));
			table.setFactoryName(rs.getString("factory_name"));
			table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
			table.setRegId(rs.getString("reg_id"));	
			table.setRegName(rs.getString("reg_name"));	
			table.setAppDate(rs.getString("app_date"));	
			table.setAppId(rs.getString("app_id"));	
			table.setAppNo(rs.getString("app_no"));
			table.setOrderComp(rs.getString("order_comp"));
		} else {
			//공장명 구하기
			where = "where factory_code='"+factory_no+"'";
			String factory_name = getColumData("factory_info_table","factory_name",where);
			
			table.setPid("");	
			table.setMpsNo("");	
			table.setOrderNo("");	
			table.setMpsType("");	
			table.setModelCode("");	
			table.setModelName("");	
			table.setFgCode("");
			table.setItemCode("");
			table.setItemName("");
			table.setItemSpec("");
			table.setPlanDate(view_td);
			table.setPlanCount(0);
			table.setSellCount(0);
			table.setItemUnit("");
			table.setMpsStatus("");
			table.setFactoryNo(factory_no);
			table.setFactoryName(factory_name);
			table.setRegDate("");
			table.setRegId("");	
			table.setRegName("");	
			table.setAppDate("");	
			table.setAppId("");	
			table.setAppNo("");
			table.setOrderComp("");
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table;
	}

	//*******************************************************************
	// MPS_MASTER 년도별/월별 Calendar에 출력할 정보 쿼리
	// login_id : 자신작성시는 전부볼수있고, 아닌경우는 승인확정된이후부터 볼수있어
	// 승인관리자는 승인요청된것 부터 볼 수 있어
	//*******************************************************************/	
	public String getMpsCalendarList(String factory_no,String year,String month,String login_id) throws Exception
	{
		//변수 초기화
		String data = "",plan_date=year+month;
		String pdate="",reg_id="",mps_status="",view="",tag="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//사용자 권한알아보기 (USER:담당자,MGR:관리자)
		String mgr = checkGrade("MPS",login_id,factory_no);
		
		//query문장 만들기
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and plan_date like '"+plan_date+"%' ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				pdate = rs.getString("plan_date");
				reg_id = rs.getString("reg_id");
				mps_status = rs.getString("mps_status");
				view = "Y";
				if(mgr.indexOf("MGR") != -1 & !reg_id.equals(login_id)) {
					if(mps_status.equals("1")) view = "N";
				} else if(!reg_id.equals(login_id)) {
					if(mps_status.equals("1") || mps_status.equals("2")) view = "N";	//화면출력여부판단
				} 
				pdate = anbdt.getSepDate(pdate,"/");
	
				//현진행상태 알려주기
				tag = "";
				if(mps_status.equals("3")) {			//MPS승인
					tag="<font color='blue'><b>(S)</b></font>";
				} else if(mps_status.equals("4") || mps_status.equals("5")) {	//MRP생성,MRP승인
					tag="<font color='red'><b>(P)</b></font>";
				} else if(mps_status.equals("6")) {	//제조오더확정
					tag="<font color='brown'><b>(M)</b></font>";
				} else if(mps_status.equals("7")) {	//제조마감
					tag="<font color='gray'><b>(C)</b></font>";
				}
				data += pdate+"*"+view+"@"+tag+rs.getString("fg_code")+"(";
				data += rs.getInt("plan_count")+")@";
				data += rs.getString("pid")+"@;";
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//*******************************************************************
	// MPS_MASTER 주간보기 출력할 정보 쿼리
	// login_id : 자신작성시는 전부볼수있고, 아닌경우는 승인확정된이후부터 볼수있어
	// 승인관리자는 승인요청된것 부터 볼 수 있어
	// week_cnt : 주간단위, view_td : 보고자하는 주간의 일요일 날자(yyyy/mm/dd)
	//*******************************************************************/	
	public ArrayList getMpsWeekList(String factory_no,String view_td,String login_id,String week_cnt) throws Exception
	{
		//변수 초기화
		String data = "",plan_std="",plan_etd="",reg_id="",mps_status="",view="Y";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//사용자 권한알아보기 (USER:담당자,MGR:관리자)
		String mgr = checkGrade("MPS",login_id,factory_no);

		//주간별 검색 시작일과 끝일을 구하기
		String y = view_td.substring(0,4);		//년
		String m = view_td.substring(5,7);		//월
		String d = view_td.substring(8,10);		//일
		int week = Integer.parseInt(week_cnt);
		int wcnt = week*7-1;
		String sdate = anbdt.getDate(Integer.parseInt(y),Integer.parseInt(m),Integer.parseInt(d),wcnt);

		plan_std = y+m+d;
		plan_etd = sdate.substring(0,4)+sdate.substring(5,7)+sdate.substring(8,10);

		//query문장 만들기
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and (plan_date >= '"+plan_std+"' ";
		query += "and plan_date <= '"+plan_etd+"') ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		ArrayList table_list = new ArrayList();
		mpsMasterTable table = null;
		while(rs.next()) { 	
			reg_id = rs.getString("reg_id");
			mps_status = rs.getString("mps_status");

			//조건검색
			if(reg_id.equals(login_id)) { view = "Y"; }
			else if(mgr.indexOf("MGR") != -1 & !mps_status.equals("1")) { view = "Y"; }
			else if(!mps_status.equals("1") & !mps_status.equals("2")) { view = "Y"; }
			else view = "N";


			//내용담기
			if(view.equals("Y")) {
				table = new mpsMasterTable();
				table.setPid(rs.getString("pid"));	
				table.setMpsNo(rs.getString("mps_no"));	
				table.setOrderNo(rs.getString("order_no"));	
				table.setMpsType(rs.getString("mps_type"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPlanDate(anbdt.getSepDate(rs.getString("plan_date"),"/"));
				table.setPlanCount(rs.getInt("plan_count"));
				table.setSellCount(rs.getInt("sell_count"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMpsStatus(rs.getString("mps_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppNo(rs.getString("app_no"));
				table.setOrderComp(rs.getString("order_comp"));
				table_list.add(table);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// MPS_MASTER 월간보기 출력할 정보 쿼리
	// login_id : 자신작성시는 전부볼수있고, 아닌경우는 승인확정된이후부터 볼수있어
	// 승인관리자는 승인요청된것 부터 볼 수 있어
	// view_td : 보고자하는 주간의 일요일 날자(yyyy/mm/dd)
	//*******************************************************************/	
	public ArrayList getMpsMonthList(String factory_no,String view_td,String login_id) throws Exception
	{
		//변수 초기화
		String data = "",plan_date="",reg_id="",mps_status="",view="Y";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//사용자 권한알아보기 (USER:담당자,MGR:관리자)
		String mgr = checkGrade("MPS",login_id,factory_no);

		//월별 검색일을 구하기
		String y = view_td.substring(0,4);		//년
		String m = view_td.substring(5,7);		//월
		plan_date = y+m;
	
		//query문장 만들기
		query = "SELECT * FROM mps_master where factory_no like '"+factory_no+"%' and plan_date like '"+plan_date+"%' ";
		query += "order by plan_date asc";
		rs = stmt.executeQuery(query);

		ArrayList table_list = new ArrayList();
		mpsMasterTable table = null;
		while(rs.next()) { 	
			reg_id = rs.getString("reg_id");
			mps_status = rs.getString("mps_status");

			//조건검색
			if(reg_id.equals(login_id)) { view = "Y"; }
			else if(mgr.indexOf("MGR") != -1 & !mps_status.equals("1")) { view = "Y"; }
			else if(!mps_status.equals("1") & !mps_status.equals("2")) { view = "Y"; }
			else view = "N";

			//내용담기
			if(view.equals("Y")) {
				table = new mpsMasterTable();
				table.setPid(rs.getString("pid"));	
				table.setMpsNo(rs.getString("mps_no"));	
				table.setOrderNo(rs.getString("order_no"));	
				table.setMpsType(rs.getString("mps_type"));	
				table.setModelCode(rs.getString("model_code"));	
				table.setModelName(rs.getString("model_name"));	
				table.setFgCode(rs.getString("fg_code"));
				table.setItemCode(rs.getString("item_code"));
				table.setItemName(rs.getString("item_name"));
				table.setItemSpec(rs.getString("item_spec"));
				table.setPlanDate(anbdt.getSepDate(rs.getString("plan_date"),"-"));
				table.setPlanCount(rs.getInt("plan_count"));
				table.setSellCount(rs.getInt("sell_count"));
				table.setItemUnit(rs.getString("item_unit"));
				table.setMpsStatus(rs.getString("mps_status"));
				table.setFactoryNo(rs.getString("factory_no"));
				table.setFactoryName(rs.getString("factory_name"));
				table.setRegDate(anbdt.getSepDate(rs.getString("reg_date"),"-"));
				table.setRegId(rs.getString("reg_id"));	
				table.setRegName(rs.getString("reg_name"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppNo(rs.getString("app_no"));
				table.setOrderComp(rs.getString("order_comp"));
				table_list.add(table);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	// MPS관리번호 구하기
	// FORMAT : MPS+yy(2)+mm(2)+serial(2)
	//*******************************************************************/	
	public String getMpsNo(String factory_no) throws Exception
	{
		//변수 초기화
		nfm.setFormat("000");
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//MPS번호중 Serial를 제외한 번호 구하기
		String y = anbdt.getYear();
		y = y.substring(2,4);
		String m = anbdt.getMonth();
		String mps_no = "MPS"+y+m;
		
		//query문장 만들기
		query = "SELECT mps_no FROM mps_master where factory_no='"+factory_no+"' and mps_no like '"+mps_no+"%' ";
		query += "order by mps_no desc";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = rs.getString("mps_no");
		
		if(data.length() == 0) {
			data = mps_no+"001";
		} else {
			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			data = mps_no+serial;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		CALENDAR 정보의 휴일정보 가져오기
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// CALENDAR_COMMON 년도별/월별 Calendar에 출력할 공휴일정보 쿼리
	//*******************************************************************/	
	public String getHolidayList(String year,String month) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT * FROM calendar_common where (item='LHD' and hdmon ='"+month+"') or ";
		query += "(item='BHD' and hdyear='"+year+"' and hdmon='"+month+"') ";
		query += "order by nlist asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				data += rs.getString("nlist")+";";
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		BOM관련 정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// 해당품목이 해당관리코드(gid)내에 포함된 제품또는 반제품 품목코드인지 판단하기
	//*******************************************************************/	
	public int checkItemCode(String gid,String item_code) throws Exception
	{
		//변수 초기화
		int cnt = 0;
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		query = "SELECT COUNT(*) FROM mbom_item where gid='"+gid+"' and parent_code='"+item_code+"'";
		rs = stmt.executeQuery(query);

		rs.next();
		cnt = rs.getInt(1);

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return cnt;
	}

	//--------------------------------------------------------------------
	//
	//		공통 메소드 정의
	//
	//
	//---------------------------------------------------------------------
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
	//	해당항목의 권한을 검사
	//*******************************************************************/
	public String checkGrade(String mgr_type,String login_id,String factory_no) throws Exception
	{
		//변수 초기화
		String data = "";
		Statement stmt = con.createStatement();
		ResultSet rs = null;
		
		query = "select mgr_code from mfg_grade_mgr where factory_no='"+factory_no+"' and ";
		query += "mgr_type='"+mgr_type+"' and mgr_id like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			data += rs.getString("mgr_code")+",";
		}
	
		stmt.close();
		rs.close();
		return data;			
	}

}

