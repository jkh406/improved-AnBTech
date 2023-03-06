package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.date.anbDate;

public class AppMenuDAO
{
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	private DBConnectionManager connMgr;
	private Connection con;
	
	//생성자
	public AppMenuDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMenuDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}
	
	//소멸자
	public void close(Connection con) throws Exception {
		connMgr.freeConnection("mssql",con);
	}
	//*******************************************************************
	//	수량 파악하기
	//*******************************************************************/
	public ArrayList getTotal(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번

		//삭제예정일 적용하기
		deleteAppMasterAMV(id);		//마스터 테이블
		deleteAppSaveAMV(id);		//기결문서 저장 테이블
		deleteAppReceive(id);		//통보문서 테이블 삭제

		//삭제된(AMV)데이터 storehouse 테이블스페이스의 app_save로 옮긴후 삭제하기
		StoreHouseApp();			//데이터 복사하기 (groupware app_save --> storehouse app_save)
		deleteAMV();				//복사된 데이터 삭제하기(groupware app_master,app_save)
		
		Statement stmt = null;
		ResultSet rs = null;
		TableItemCount table = null;
		String tablename = "";
		String query = "";	
		
		//return 할 ArrayList 만들기
		ArrayList table_list = new ArrayList();

		//공통 항목
		stmt = con.createStatement();
		

		//----------------------------------------------
		// 미결함
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//일괄적 합의일때(나머지 제외)
		/*String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";
		*/
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "')";	
		
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppIngCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("미결함 : " + table.getAppIngCnt());
		}

		//----------------------------------------------
		// 진행함
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String ask_ing_data = "where (writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
		ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
		ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
		ask_ing_data += " or app_state='APG10' or app_state='APL')";					
		query += ask_ing_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAskIngCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("진행함 : " + table.getAskIngCnt());
		}

		//----------------------------------------------
		// 기결함
		//----------------------------------------------
		tablename = "APP_SAVE";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		//쿼리 문장 (전체문서)
		String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
		
		//현재일기준 주어진날자만큼만 쿼리하기
		String cut_date = anbdt.getID(getAppAps());		//현재일기준 ID구하기
		String app_cut = " and pid >= '"+cut_date+"'";
		
		String abd_query = query + app_box_data+app_cut;
		rs = stmt.executeQuery(abd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함전체 : " + table.getAppBoxCnt());
		}

		//일반문서
		String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
		String agd_query = query + app_gen_data;
		rs = stmt.executeQuery(agd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppGenCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함일반: " + table.getAppGenCnt());
		}

		//고객관리문서
		String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
		String asd_query = query + app_ser_data;
		rs = stmt.executeQuery(asd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppSerCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함고객관리 : " + table.getAppSerCnt());
		}

		String app_yan_data = "";
		String yan_query = "";
		//(외출계 : an outing)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
		yan_query = query + app_yan_data;
		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOutCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함(외출계)관리 : " + table.getAppOutCnt());
		}

		//(출장신청 : a business trip)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
		yan_query = query + app_yan_data;
		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppBtrCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함(출장신청)관리 : " + table.getAppBtrCnt());
		}

		//(휴가원 : holidays)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppHdyCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("기결함(휴가원)관리 : " + table.getAppHdyCnt());
		}

		//(배차신청서 : Allocation of cars)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppCarCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(보고서 : report)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppRepCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(출장보고서 : a business trip report)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppBrpCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(기안서 : drafting)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppDrfCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(명함신청서 : a business card)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppCrdCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(사유서 : a reason)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppRsnCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(협조전 : help)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppHlpCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(연장근무신청서 : overtime work)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOtwCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(구인의뢰서 : job offering)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOffCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(교육일지 : education)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppEduCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(승인원 : acknowledgment)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppAkgCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(기술문서 : technical document)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppTdCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(공지공문 : an official document)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOdtCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(사내공문 : an internal official document)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppIdsCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(사외공문 : an outside official document)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOdsCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(자산관리 : an asset)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppAstCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(견적관리 : an estimate)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppEstCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(특근관리 : an extra work)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppEwkCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(BOM관리 : bill of material)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppBomCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(설계변경관리 : design change management)문서
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppDcmCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(구매요청 : purchase request)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppPcrCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(발주요청 : order request)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppOdrCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(구매입고 : purchase warehousing)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PHW'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppPwhCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}

		//(부품출고 : take goods out of a warehouse)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppTgwCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
		}


		//----------------------------------------------
		// 반려함
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setRejBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("반려함 : " + table.getRejBoxCnt());
		}

		//----------------------------------------------
		// 임시 보관함
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
		query += tmp_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setTmpBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("임시보관함 : " + table.getTmpBoxCnt());
		}

		//----------------------------------------------
		// 통보함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_RECEIVE where receiver='"+id+"' and isopen='0'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setSeeBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("읽지않은수량 : " + table.getSeeBoxCnt());
		}

		//전체 수량
		query = "SELECT COUNT(*) FROM APP_RECEIVE where receiver='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setSeeBoxTot(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("통보전체수량 : " + table.getSeeBoxTot());
		}

		//----------------------------------------------
		// 삭제함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM storehouse.dbo.APP_SAVE where app_state='AMV'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setDelBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			////System.out.println("삭제함 : " + table.getDelBoxCnt());
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	기간만료 임시보관외 문서 찾기 (APP_MASTER)
	//*******************************************************************/
	public void deleteAppMasterAMV(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		//APP_MASTER테이블에서 AMV로 바꿔
		query = "SELECT pid,delete_date from APP_MASTER where (writer='"+id+"') and (app_state != 'AMV')";
		query += " and (delete_date < '"+cur_date+"')";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid"); if(PID == null) PID = "";
			String del = rs.getString("delete_date"); 
			if(del != null) {
				int dedate = Integer.parseInt(del);
				setAmv("APP_MASTER",PID,cudate,dedate);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}
	
	//*******************************************************************
	//	기간만료 저장문서 찾기 (APP_SAVE)
	//*******************************************************************/
	public void deleteAppSaveAMV(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		
		//APP_SAVE테이블에서  AMV로 바꿔
		query = "SELECT pid,delete_date from APP_SAVE where (writer='"+id+"') and (app_state != 'AMV')";
		query += " and (delete_date < '"+cur_date+"')";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid");
			String del = rs.getString("delete_date"); 
			if(del != null) {
				int dedate = Integer.parseInt(del);
				setAmv("APP_SAVE",PID,cudate,dedate);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	삭제일 완료된 문서 상태만 바꿔주기 ( --> AMV) update하기
	//*******************************************************************/
	private void setAmv(String tablename,String PID,int cur, int del) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		if(cur > del) {
				String update = "update "+tablename+" set app_state='AMV' where pid='"+PID+"'";
				stmt.executeUpdate(update);
		}
		stmt.close();
	}

	//*******************************************************************
	//	기간만료 통보문서 찾기 (APP_RECEIVE)
	//*******************************************************************/
	public void deleteAppReceive(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		// APP_RECEIVE테이블에서 완전삭제
		query = "SELECT pid,delete_date from APP_RECEIVE where writer='"+id+"'";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid");
			String del = rs.getString("delete_date");
			if(del != null) {
				int dedate = Integer.parseInt(del);
				StoreHouseReceive(PID);							//storehouse로 옮기기
				setDelete("APP_RECEIVE",PID,cudate,dedate);		//삭제하기
			}
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	삭제일 완료된 문서 삭제하기
	//*******************************************************************/
	private void setDelete(String tablename,String PID,int cu,int de) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		if(cu > de) {
			String update = "delete from "+tablename+" where pid='"+PID+"'";
			stmt.executeUpdate(update);
		}
		
		stmt.close();
	}

	//*******************************************************************
	//	삭제일 완료된 문서 storehouse테이블 스키마의 app_save에 저장하기
	//*******************************************************************/
	private void StoreHouseApp() throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String update = "insert into storehouse.dbo.APP_SAVE ";
			update += "select * from APP_SAVE where app_state='AMV'";
		//System.out.println(" StoreHouseApp : " + update);
		stmt.executeUpdate(update);		
		stmt.close();
	}

	//*******************************************************************
	//	삭제일 완료된 문서 storehouse테이블 스키마의 app_receive에 저장하기
	//*******************************************************************/
	private void StoreHouseReceive(String PID) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String update = "insert into storehouse.dbo.APP_RECEIVE ";
			update += "select * from APP_RECEIVE where pid='"+PID+"'";
		//System.out.println("recevie 옮기기 : " + update);
		stmt.executeUpdate(update);		
		stmt.close();
	}

	//*******************************************************************
	//	삭제일 완료된 문서(AMV) groupware테이블 스키마에서 완전삭제하기
	//*******************************************************************/
	private void deleteAMV() throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String[] tb = {"APP_MASTER","APP_SAVE"};
		for(int i=0; i<2; i++) {
			String update = "delete from "+tb[i]+" where app_state='AMV'";
			//System.out.println(" deleteAMV : " + update);
			stmt.executeUpdate(update);
		}
		stmt.close();
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
		return data;		
	}
}