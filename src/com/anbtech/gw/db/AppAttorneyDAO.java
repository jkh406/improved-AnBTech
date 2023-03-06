package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppAttorneyDAO
{
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//일자 처리
	private DBConnectionManager connMgr;
	private Connection con;

	private String[][] app_ing = null;		//미결문서 관리번호담기

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public AppAttorneyDAO(Connection con) 
	{
		this.con = con;
	}

	public AppAttorneyDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}
	//--------------------------------------------------------------------------//
	//		단순히 대리결재자를 처리하고자 할때										//
	//		입력/삭제/대리결재내용/대리결재자사번 등									//
	//--------------------------------------------------------------------------//

	//*******************************************************************
	//	부재중 대리인으로 지정한 결재자 찾기 : app_attorney
	//*******************************************************************/
	public String searchAttorney(String id) throws Exception
	{ 
		String attorney_id="";					//부재중 대리결재자로 지정한 결재자 사번
		int s_date=0,e_date=0;					//부재중 결재 시작일,종료일
		int t_date=0;							//금일
		if(id == null) return attorney_id;		//null이면 return

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//쿼리하기
		String query = "SELECT attorney_id,start_date,end_date FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			attorney_id = rs.getString("attorney_id");
			String sd = rs.getString("start_date");	if(sd == null) sd = "";
			String ed = rs.getString("end_date");	if(ed == null) ed = "";
			if(sd.length() != 0) s_date = Integer.parseInt(sd);
			if(ed.length() != 0) e_date = Integer.parseInt(ed);
		}
	
		//날자체크 하기
		t_date = Integer.parseInt(anbdt.getDateNoformat());
		if(t_date < s_date)  attorney_id = "";
		else if(t_date > e_date) attorney_id = "";

		//닫기
		stmt.close();
		rs.close();
		return attorney_id;
	}

	//*******************************************************************
	//	부재중 대리인으로 지정한 결재자 찾기 : app_attorney
	//*******************************************************************/
	public String searchNameAttorney(String id) throws Exception
	{ 
		String attorney_name="";				//부재중 대리결재자로 지정한 결재자 이름
		if(id == null) return attorney_name;	//null이면 return

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_name FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) attorney_name = rs.getString("attorney_name");
	
		stmt.close();
		rs.close();
		return attorney_name;
	}

	//*******************************************************************
	//	부재중 결재자 찾기 : app_attorney
	//*******************************************************************/
	public ArrayList isAttorney(String login_id) throws Exception
	{
		TableAppMaster table = null;
		ArrayList table_list = new ArrayList();
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//기존에 지정한 경우가 있는다
		String query = "SELECT * FROM APP_ATTORNEY where approval_id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			table = new TableAppMaster();							
			table.setAttorneyId(rs.getString("attorney_id"));
			table.setAttorneyName(rs.getString("attorney_name"));
			table.setStartDate(rs.getString("start_date"));
			table.setEndDate(rs.getString("end_date"));
			table_list.add(table);					
		}

		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	//	부재중 결재자 입력하기 : app_attorney
	//*******************************************************************/
	public void inputAttorney(String login_id,String attorney_id,String attorney_name,
		String start_date,String end_date) throws Exception
	{
		String approval_id = "";				//결재지정권자
		String query="",update="",input="";
		String rtn = "N";			//대리결재자가 중복되지 않음
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//기존에 지정한 경우가 있는지 찾기
		query = "SELECT approval_id FROM APP_ATTORNEY where approval_id='"+login_id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) approval_id = rs.getString("approval_id");

		//입력한 경우가 있는경우 
		if(approval_id.equals(login_id)) {
			update = "UPDATE APP_ATTORNEY set attorney_id='"+attorney_id+"',attorney_name='";
			update += attorney_name+"',start_date='"+start_date+"',end_date='"+end_date+"'";
			update += " where approval_id='"+login_id+"'";
			stmt.executeUpdate(update);
		}
		//최초로 입력하는 경우
		else {
			input = "INSERT into APP_ATTORNEY(approval_id,attorney_id,attorney_name,start_date,end_date) values('"+login_id+"','";
			input += attorney_id+"','"+attorney_name+"','"+start_date+"','"+end_date+"')";
			stmt.executeUpdate(input);
		}
	
		stmt.close();
		rs.close();
		
	}
	//*******************************************************************
	//	부재중 결재자 삭제하기 : app_attorney
	//*******************************************************************/
	public void deleteAttorney(String login_id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		String update = "UPDATE APP_ATTORNEY set attorney_id='',attorney_name='',start_date='',end_date='' where approval_id='"+login_id+"'";
		stmt.executeUpdate(update);
	
		stmt.close();
	}

	//--------------------------------------------------------------------------//
	//		전자결재에 미결문서에 처리하고 할때  									//
	//		(현재 사용하지 않음 : 2003-12-10)										//
	//--------------------------------------------------------------------------//
	//*******************************************************************
	//	현재 부재중 대리인으로 지정한 결재자 찾기 : app_master
	//*******************************************************************/
	public String[] getAttorney(String pid) throws Exception
	{
		String[] attorney=new String[2];			//부재중 대리결재자로 지정한 결재상태,결재자사번
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_state,attorney_id FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			attorney[0]= rs.getString("attorney_state");
			attorney[1]= rs.getString("attorney_id");
		}
	
		stmt.close();
		rs.close();
		return attorney;
	}
	
	//*******************************************************************
	//	부재중 대리인 지정자 여부 찾기 : app_attorney [지정자 중심]
	//*******************************************************************/
	public String checkAttorney(String id) throws Exception
	{
		String rtn="N";			//부재중 대리결재자 없음
		String att_id = "";		//부재중 대리결재자 사번
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT attorney_id FROM APP_ATTORNEY where approval_id='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) att_id = rs.getString("attorney_id");
		if(att_id.length() > 2) rtn = "Y";
	
		stmt.close();
		rs.close();
		return rtn;
	}
	//*******************************************************************
	//	대리인이 해당문서의 결재자인지 찾기 : app_attorney [대리결재자 중심]
	//*******************************************************************/
	public String checkApproval(String login_id,String pid) throws Exception
	{
		String rtn="N";			//해당문서 대리결재권한 없음
		String self_id = "";	//결재자들의 사번
		String att_id = "";		//대리결재자 사번
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT * FROM APP_MASTER where pid='"+pid+"'";
		rs = stmt.executeQuery(query);
	
		if(rs.next()) {
			self_id = rs.getString("reviewer")+"|";
			self_id += rs.getString("decision")+"|";
			self_id += rs.getString("agree")+"|";
			self_id += rs.getString("agree2")+"|";
			self_id += rs.getString("agree3")+"|";
			self_id += rs.getString("agree4")+"|";
			self_id += rs.getString("agree5")+"|";
			self_id += rs.getString("agree6")+"|";
			self_id += rs.getString("agree7")+"|";
			self_id += rs.getString("agree8")+"|";
			self_id += rs.getString("agree9")+"|";
			self_id += rs.getString("agree10")+"|";
			self_id += rs.getString("attorney_id");
			att_id = rs.getString("attorney_id");
		}

		//판단하기
		if(self_id.indexOf(login_id) != -1) rtn = "Y";		//자신의 결재문서
		if(att_id.length() < 3) rtn = "O";					//대리결재문서가 아님

		stmt.close();
		rs.close();
		return rtn;
	}
	//*******************************************************************
	//	부재중 대리 결재자 결재시 대리결재상태를 바꿔주기
	// 단,변경상태는 입고[APS],반려[APR]일때만 적용시킴
	//*******************************************************************/
	public void statusAttorney(String pid,String attorney_state) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		update = "UPDATE APP_MASTER set attorney_state='"+attorney_state+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		update = "UPDATE APP_SAVE set attorney_state='"+attorney_state+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);
	
		stmt.close();
	}

	//*******************************************************************
	//	부재중 대리인을 대리결재자로 결재테이블에 입력하기 : 기존 미결함에 반영(app_master)
	//*******************************************************************/
	public void attorneyApproval(String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//대리인으로 지정한 결재자 찾기
		String att_id = searchAttorney(id);

		//미결함문서 찾기
		getAppIngList(id);

		//미결함에 대리결재자를 입력한다.
		String pid = "",state="",a_id = att_id;
		String[] s = {"","APV","APL","APG","APG2","APG3","APG4","APG5","APG6","APG7","APG8","APG9","APG10"};
		int cnt = app_ing.length;		//미결함 갯수
		for(int i=0; i<cnt; i++) {
			for(int j=1; j<13; j++) {	//해당자 찾기
				pid = app_ing[i][0];	//관리번호
				if(app_ing[i][j].equals(id)) { 
					state = s[j];
					a_id = att_id;
					break; 
				}
			}
			//입력하기
			String update = "UPDATE app_master set attorney_state='"+state+"',attorney_id='"+a_id+"' where pid='"+pid+"'";
			//System.out.println(update);
			stmt.executeUpdate(update);
		}

		stmt.close();
	}
	//*******************************************************************
	//	부재중 대리인을 대리결재자로 결재테이블에 해지하기 : 기존 미결함에 반영(app_master)
	//*******************************************************************/
	public void attorneyDelete(String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//미결함문서 찾기
		getAppIngList(id);

		//미결함에 대리결재자를 입력한다.
		String pid = "";
		int cnt = app_ing.length;		//미결함 갯수
		for(int i=0; i<cnt; i++) {
			pid = app_ing[i][0];	//관리번호
			//해지하기
			String update = "UPDATE app_master set attorney_state='',attorney_id='' where pid='"+pid+"'";
			//System.out.println(update);
			stmt.executeUpdate(update);
		}

		stmt.close();
	}

	//*******************************************************************
	//	미결함 APP_MASTER 전체수량
	//*******************************************************************/
	private int getTotalApp(String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
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
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;
	}

	//*******************************************************************
	//	미결문서 전체LIST에서 결재자 사번을 배열에 담기
	//*******************************************************************/	
	private void getAppIngList (String id) throws Exception
	{
		//변수 초기화
		if(id == null) id = "";			//사번
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		stmt = con.createStatement();
		
		//배열만들기
		int total_cnt = getTotalApp(id);
		app_ing = new String[total_cnt][13];

		//query문장 만들기
		String ING_data = "where ((app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
			ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
			ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
			ING_data +=  id + "')";

		//일괄적 합의일때(나머지 제외)
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
			PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "'))";
		query = "SELECT * FROM APP_MASTER ";
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		int n = 0;
		while(rs.next()) { 
			app_ing[n][0] = rs.getString("pid");			//관리번호
			app_ing[n][1] = rs.getString("reviewer");		//검토
			app_ing[n][2] = rs.getString("decision");		//승인
			app_ing[n][3] = rs.getString("agree");			//협조1
			app_ing[n][4] = rs.getString("agree2");			//협조2
			app_ing[n][5] = rs.getString("agree3");			//협조3
			app_ing[n][6] = rs.getString("agree4");			//협조4
			app_ing[n][7] = rs.getString("agree5");			//협조5
			app_ing[n][8] = rs.getString("agree6");			//협조6
			app_ing[n][9] = rs.getString("agree7");			//협조7
			app_ing[n][10] = rs.getString("agree8");		//협조8
			app_ing[n][11] = rs.getString("agree9");		//협조9
			app_ing[n][12] = rs.getString("agree10");		//협조10
			n++;
		} 
		stmt.close();
		rs.close();
	}
}
