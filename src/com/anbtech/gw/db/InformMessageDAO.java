package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import com.anbtech.date.anbDate;

public class InformMessageDAO
{
	private anbDate anbdt = new anbDate();
	private Connection con;

	public InformMessageDAO(Connection con) 
	{
		this.con = con;
	}
	//*******************************************************************
	//	수량 파악하기
	//*******************************************************************/
	public ArrayList getTotal(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt;
		ResultSet rs;
		TableItemCount table;
		String tablename = "";
		String query = "";	
		
		//return 할 ArrayList 만들기
		ArrayList table_list = new ArrayList();

		//공통 항목
		stmt = con.createStatement();

		//----------------------------------------------
		// 미결함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_MASTER ";

		//순차적 합의일때 (검토,합의 포함)
		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//일괄적 합의일때(나머지 제외)
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";

		query += ING_data + PAL_data;

		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppIngCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("미결함 : " + table.getAppIngCnt());
		}
		
		//----------------------------------------------
		// 기결함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_SAVE ";

		//쿼리 문장 (전체문서)
		String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and decision like '%"+anbdt.getDate()+"%'";
		String abd_query = query + app_box_data;
		rs = stmt.executeQuery(abd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setAppBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("기결함전체 : " + table.getAppBoxCnt());
		}
		
		//----------------------------------------------
		// 반려함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_MASTER ";

		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setRejBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("반려함 : " + table.getRejBoxCnt());
		}

		//----------------------------------------------
		// 통보함
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_RECEIVE ";

		//읽지 않은 수량
		String see_box_data = "where receiver='"+id+"' and isOpen='0'";
		query += see_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setSeeBoxCnt(rs.getInt(1));		//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("읽지않은수량 : " + table.getSeeBoxCnt());
		}

		//----------------------------------------------
		// 도착된 편지
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM POST_LETTER "; 
		String post_data = "where post_receiver='"+id+"' and isopen='0'";
		query += post_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount 생성하기
			table.setPostCnt(rs.getInt(1));			//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("도착편지함 : " + table.getPostCnt());
		}

		//----------------------------------------------
		// 공지사항
		//----------------------------------------------
		//등록시간
		//java.util.Date now = new java.util.Date();
		//java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd a hh:mm");
		//String w_time = vans.format(now);

		query = "SELECT COUNT(*) FROM notice_board "; 
		String inform_data = "where w_time like '"+anbdt.getDate()+"%'";
		query += inform_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();				//TableItemCount 생성하기
			table.setInformCnt(rs.getInt(1));			//TableItemCount에 setting하기
			table_list.add(table);					//추가하기
			//System.out.println("공지사항 : " + table.getInformCnt());
		}
		

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

}