package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppMasterListDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public AppMasterListDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMasterListDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 미결함 APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalApp(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		//순차적 합의일때 (검토,합의 포함)
		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//일괄적 합의일때(나머지 제외)
		//String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";	
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "')";	
		
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;	
	}	
	
	//--------------------------------------------------------------------
	//	수량 파악하기 : 진행함 APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalAsk(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String ask_ing_data = "where (writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
		ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
		ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
		ask_ing_data += " or app_state='APG10' or app_state='APL')";					
		query += ask_ing_data;
		////System.out.println("진행함 수량 : "  + query);
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;
	}	

	//--------------------------------------------------------------------
	//	수량 파악하기 : 반려함 APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalRej(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}			
	
	//--------------------------------------------------------------------
	//	수량 파악하기 : 임시보관함 APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalTmp(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
		query += tmp_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}	
	//--------------------------------------------------------------------
	//	수량 파악하기 : 삭제함 storehouse.dbo.APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDel(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM storehouse.dbo.APP_SAVE ";
		String del_box_data = "where app_state='AMV'";
		query += del_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 전체 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSav(String id,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_box_data = "";
		if(sWord.length() == 0)
			app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
		else 
			app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and "+sItem+" like '%"+sWord+"%'";
		
		//현재일기준 주어진날자만큼만 쿼리하기
		String cut_date = anbdt.getID(getAppAps());		//현재일기준 ID구하기
		String app_cut = " and pid >= '"+cut_date+"'";

		String abd_query = query + app_box_data+app_cut;

		rs = stmt.executeQuery(abd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 일반문서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalGen(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
		String agd_query = query + app_gen_data;
		rs = stmt.executeQuery(agd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 고객관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSer(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
		String asd_query = query + app_ser_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 외출계 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOut(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_out_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
		String asd_query = query + app_out_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 출장신청서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBtr(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_btr_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
		String asd_query = query + app_btr_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 휴가원 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalHdy(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 배차신청 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalCar(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 보고서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalRep(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 출장보고서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBrp(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 기안서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDrf(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 명함신청서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalCrd(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 사유서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalRsn(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 협조전 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalHlp(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 연장근무신청서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOtw(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 구인의뢰서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOff(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 교육일지 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEdu(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 승인원 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalAkg(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 기술문서 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalTd(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 공지공문 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOdt(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 사내공문 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalIds(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 사외공문 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOds(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 자산관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalAst(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 견적관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEst(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 특근관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEwk(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 BOM관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBom(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 설계변경관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDcm(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 구매요청관리관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalPcr(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 발주요청관리관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOdr(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 구매입고관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalPwh(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PWH'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 기결함 부품출고관리 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalTgw(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	수량 파악하기 : 통보함 APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSee(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_RECEIVE ";
		String see_box_totD = "where receiver='"+id+"'";
		query += see_box_totD;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//*******************************************************************
	//	총 페이지 수 구하기
	//*******************************************************************/
	public int getTotalPage() 
	{
		return this.total_page;
	}

	//*******************************************************************
	//	현 페이지 수 구하기
	//*******************************************************************/
	public int getCurrentPage() 
	{
		return this.current_page;
	}
	//*******************************************************************
	//	Query 내용 가져오기 : 전체 LIST
	// id:사번, appKind:문서함종류, page:현재page, out:한페이지에 출력할 갯수
	// sItem : 검색컬럼명, sWord : 검색할 내용, app_path:본문읽을 rootPath
	//*******************************************************************/	
	public ArrayList getTable_list (String id,String appKind,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";			//사번
		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점
		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String SER_data = "";			//검색조건
		String SORT_data = "";			//정렬조건
		
		stmt = con.createStatement();
		TableAppMaster table = null;	//모든것을 포함함으로 "함"에 관계없이 통일하여 담는다.
		ArrayList table_list = new ArrayList();
			
		//query문장 만들기
		if(appKind.equals("APP_ING")) {				//미결함 APP_MASTER
				total_cnt = getTotalApp(id);	
				
				String ING_data = "where ((app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
				ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
				ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
				ING_data +=  id + "')";

				//일괄적 합의일때(나머지 제외)
				//String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "'))";
				String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "'))";	
		

				//검색
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				//정렬
				SORT_data = " ORDER BY write_date DESC";

				query = "SELECT * FROM APP_MASTER ";
				query += ING_data + PAL_data + SER_data + SORT_data;
				//System.out.println("미결함 : " + query);
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("ASK_ING")) {		//진행함 APP_MASTER
				total_cnt = getTotalAsk(id);
				
				String ask_ing_data = "where ((writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
				ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
				ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
				ask_ing_data += " or app_state='APG10' or app_state='APL'))";						
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += ask_ing_data+SER_data+SORT_data;
				////System.out.println("진행함 : " + query);
				rs = stmt.executeQuery(query);
		} 
		else if(appKind.equals("REJ_BOX")) {		//반려함 APP_MASTER
				total_cnt = getTotalRej(id);
				
				String rej_box_data = "where writer='"+id+"' and app_state='APR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += rej_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("TMP_BOX")) {		//임시보관함 APP_MASTER
				total_cnt = getTotalTmp(id);
				
				String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += tmp_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("DEL_BOX")) {		//삭제함 APP_MASTER
				total_cnt = getTotalDel(id);
				
				String del_box_data = "where app_state='AMV'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM storehouse.dbo.APP_SAVE ";
				query += del_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BOX")) {		//기결함 전체 APP_SAVE
				total_cnt = getTotalSav(id,sItem,sWord);
				
				String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 

				//현재일기준 주어진날자만큼만 쿼리하기
				String cut_date = anbdt.getID(getAppAps());		//현재일기준 ID구하기
				String app_cut = " and pid >= '"+cut_date+"'";

				query = "SELECT * FROM APP_SAVE ";
				query += app_box_data+SER_data+app_cut+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_GEN")) {		//기결함 일반문서 APP_SAVE
				total_cnt = getTotalGen(id);
				
				String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_gen_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_SER")) {		//기결함 고객관리 APP_SAVE
				total_cnt = getTotalSer(id);
				
				String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_ser_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OUT")) {		//기결함 외출계 APP_SAVE
				total_cnt = getTotalOut(id);
				
				String app_out_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_out_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BTR")) {		//기결함 출장신청서 APP_SAVE
				total_cnt = getTotalBtr(id);
				
				String app_btr_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_btr_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_HDY")) {		//기결함 휴가원 APP_SAVE
				total_cnt = getTotalHdy(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_CAR")) {		//기결함 배차신청서 APP_SAVE
				total_cnt = getTotalCar(id);
			
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_REP")) {		//기결함 보고서 APP_SAVE
				total_cnt = getTotalRep(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BRP")) {		//기결함 출장보고서 APP_SAVE
				total_cnt = getTotalBrp(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_DRF")) {		//기결함 기안서 APP_SAVE
				total_cnt = getTotalDrf(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_CRD")) {		//기결함 명함신청서 APP_SAVE
				total_cnt = getTotalCrd(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_RSN")) {		//기결함 사유서 APP_SAVE
				total_cnt = getTotalRsn(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_HLP")) {		//기결함 협조전 APP_SAVE
				total_cnt = getTotalHlp(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OTW")) {		//기결함 연장근무신청서 APP_SAVE
				total_cnt = getTotalOtw(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OFF")) {		//기결함 구인의뢰서 APP_SAVE
				total_cnt = getTotalOff(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EDU")) {		//기결함 교육일지 APP_SAVE
				total_cnt = getTotalEdu(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_AKG")) {		//기결함 승인원 APP_SAVE
				total_cnt = getTotalAkg(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_TD")) {			//기결함 기술문서 APP_SAVE
				total_cnt = getTotalTd(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODT")) {			//기결함 공지공문 APP_SAVE
				total_cnt = getTotalOdt(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_IDS")) {			//기결함 사내공문 APP_SAVE
				total_cnt = getTotalIds(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODS")) {			//기결함 사외공문 APP_SAVE
				total_cnt = getTotalOds(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_AST")) {			//기결함 자산관리 APP_SAVE
				total_cnt = getTotalAst(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EST")) {			//기결함 견적관리 APP_SAVE
				total_cnt = getTotalEst(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EWK")) {			//기결함 특근관리 APP_SAVE
				total_cnt = getTotalEwk(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BOM")) {			//기결함 BOM관리 APP_SAVE
				total_cnt = getTotalBom(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_DCM")) {			//기결함 설계변경관리 APP_SAVE
				total_cnt = getTotalDcm(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_PCR")) {			//기결함 구매요청관리 APP_SAVE
				total_cnt = getTotalPcr(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODR")) {			//기결함 발주요청관리 APP_SAVE
				total_cnt = getTotalOdr(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_PWH")) {			//기결함 구매입고관리 APP_SAVE
				total_cnt = getTotalPwh(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PWH'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_TGW")) {			//기결함 부품출고관리 APP_SAVE
				total_cnt = getTotalTgw(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("SEE_BOX")) {		//통보함 고객관리 APP_RECEIVE
				total_cnt = getTotalSee(id);
				//System.out.println("total_cnt : " + total_cnt);
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_RECEIVE where receiver='"+id+"'";
				query += SER_data+SORT_data;
				//System.out.println("query : " + query);
				rs = stmt.executeQuery(query);
		}

		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		current_page = Integer.parseInt(page);	//출력할 페이지

		//전체 page 구하기
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//페이지에 따른 query 계산하기
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//데이터 담기
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				//리스트 담기
				table = new TableAppMaster();
				String pid = rs.getString("pid");			if(pid == null) pid = "";
				table.setAmPid(pid);									//관리번호

				String content = rs.getString("app_subj");  if(content == null) content = "";
				String subLink = "<a href=\"javascript:eleApprovalView('"+pid+"','"+appKind+"');\">"+content+"</a>";

				table.setAmAppSubj(subLink);							//제목

				table.setAmWriterName(rs.getString("writer_name"));		//기안자
				table.setAmWriteDate(rs.getString("write_date"));		//기안일자

				String STATE = "";										//상태정보
				String  status = "";
				if(appKind.equals("SEE_BOX")) {
					status = rs.getString("isopen");
					if(status.equals("0")) STATE = "미확인";
					else STATE = "확인함";
				} else {
					status = rs.getString("app_state");
					if(status.equals("APV")) STATE = "검토대기";
					else if(status.equals("APG")) STATE = "협조대기";
					else if(status.equals("APL")) STATE="승인대기";
					else if(status.equals("APR")) STATE="반려";
					else if(status.equals("APT")) STATE="임시보관";
					else if(status.equals("APS")) STATE="결재완료";
					else if(status.equals("API")) STATE="통보";
					else if(status.equals("AMV")) STATE="삭제문서";
					else STATE="협조대기";	//APG2 ~ APG10
				} //if
				table.setAmAppStatus(STATE);							//상태
				
				int add_cnt = 0;										//첨부파일 수량
				if(appKind.equals("SEE_BOX")) {
					add_cnt = Integer.parseInt(rs.getString("add_counter"));
				} else {
					String adn = rs.getString("add_counter"); if(adn == null) adn = "0"; if(adn.length() == 0) adn = "0";
					////System.out.println("adn : " + adn);
					add_cnt = Integer.parseInt(adn);
					String add_f1 = rs.getString("add_1_file"); if(add_f1 == null) add_f1 = ""; //else { if(add_f1.length() > 1) add_cnt++; }
					String add_f2 = rs.getString("add_2_file"); if(add_f2 == null) add_f2 = ""; //else { if(add_f2.length() > 1) add_cnt++; }
					String add_f3 = rs.getString("add_3_file"); if(add_f3 == null)	add_f3 = ""; //else { if(add_f3.length() > 1) add_cnt++; }
					table.setAmAdd1File(add_f1);							//첨부파일 1
					table.setAmAdd2File(add_f2);							//첨부파일 2
					table.setAmAdd3File(add_f3);							//첨부파일 3
				}
				table.setAmAddCounter(Integer.toString(add_cnt));		

				table.setAmDeleteDate(rs.getString("delete_date"));			//삭제일
				table.setAmPlid(rs.getString("Plid"));						//관련문서 관리번호
				table_list.add(table);
				show_cnt++;
		} //while

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	삭제 보존함에 있는 문서 쿼리하여 가져오기
	//*******************************************************************/	
	public ArrayList getTableDelBoxlist (String id,String flag,String syear,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";			//사번
		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점
		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		stmt = con.createStatement();
		TableAppMaster table = null;	//모든것을 포함함으로 "함"에 관계없이 통일하여 담는다.
		ArrayList table_list = new ArrayList();
			
		//query문장 만들기
		total_cnt = getTotalDel(id);
		query = "SELECT * FROM storehouse.dbo.APP_SAVE ";
		query += "where flag like '%"+flag+"%' and write_date like '%"+syear+"%' ";
		query += " and "+sItem+" like '%"+sWord+"%' order by write_date desc"; 
		rs = stmt.executeQuery(query);
		
		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		current_page = Integer.parseInt(page);	//출력할 페이지

		//전체 page 구하기
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//페이지에 따른 query 계산하기
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//페이지 skip 하기 (해당되지 않는 페이지의 내용)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//데이터 담기
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				//리스트 담기
				table = new TableAppMaster();
				String pid = rs.getString("pid");			if(pid == null) pid = "";
				table.setAmPid(pid);									//관리번호

				String content = rs.getString("app_subj");  if(content == null) content = "";
				String subLink = "<a href=\"javascript:eleApprovalView('"+pid+"','DEL_BOX');\">"+content+"</a>";

				table.setAmAppSubj(subLink);							//제목

				table.setAmWriterName(rs.getString("writer_name"));		//기안자
				table.setAmWriteDate(rs.getString("write_date"));		//기안일자

				String STATE = "";										//상태정보
				String  status = "보존문서";
				table.setAmAppStatus(status);							//상태
				
				int add_cnt = 0;										//첨부파일 수량
				String adn = rs.getString("add_counter"); if(adn == null) adn = "0"; if(adn.length() == 0) adn = "0";
				////System.out.println("adn : " + adn);
				add_cnt = Integer.parseInt(adn);
				String add_f1 = rs.getString("add_1_file"); if(add_f1 == null) add_f1 = ""; //else { if(add_f1.length() > 1) add_cnt++; }
				String add_f2 = rs.getString("add_2_file"); if(add_f2 == null) add_f2 = ""; //else { if(add_f2.length() > 1) add_cnt++; }
				String add_f3 = rs.getString("add_3_file"); if(add_f3 == null)	add_f3 = ""; //else { if(add_f3.length() > 1) add_cnt++; }
				table.setAmAdd1File(add_f1);							//첨부파일 1
				table.setAmAdd2File(add_f2);							//첨부파일 2
				table.setAmAdd3File(add_f3);							//첨부파일 3
				
				table.setAmAddCounter(Integer.toString(add_cnt));		

				table.setAmDeleteDate(rs.getString("delete_date"));			//삭제일
				table.setAmPlid(rs.getString("Plid"));						//관련문서 관리번호
				table_list.add(table);
				show_cnt++;
		} //while

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	//	기결문서 몇일전 문서까지 화면에 출력할 것인지 숫자 구하기
	//*******************************************************************/
	public int getAppAps() throws Exception
	{
		//변수 초기화
		int data = 0;
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		String query = "SELECT env_value FROM app_env where env_name='APPAPS'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = Integer.parseInt(rs.getString("env_value"));

		stmt.close();
		rs.close();

		return data;
	}
}