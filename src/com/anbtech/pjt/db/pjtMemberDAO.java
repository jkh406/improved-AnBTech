package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtMemberDAO
{
	private Connection con;
	
	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtMemberDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtMemberDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	// 해당 PM의 전체 과제 List [from pjt_general]
	//*******************************************************************/	
	public ArrayList getAllProjectList (String mgr_plm_id,String pjtWord,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();
	
		//query문장 만들기
		query = "SELECT * FROM pjt_general where pjt_mbr_id like '%"+mgr_plm_id+"%'";	
		query += " and pjt_status like '%"+pjtWord+"%'";
		query += " and ("+sItem+" like '%"+sWord+"%') order by pjt_code desc"; 

		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setPjtName(rs.getString("pjt_name"));	
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당과제코드의 멤버 QUERY하기 
	//*******************************************************************/	
	public ArrayList getProjectRead (String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_member where pjt_code='"+pjt_code+"' order by pjt_mbr_type asc";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String pcode = rs.getString("pjt_code");
				table.setPjtCode(pcode);
				
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtMbrType(rs.getString("pjt_mbr_type"));
				table.setMbrStartDate(rs.getString("mbr_start_date"));
				table.setMbrEndDate(rs.getString("mbr_end_date"));
				table.setMbrPoration(rs.getDouble("mbr_poration"));
				
				String sabun = rs.getString("pjt_mbr_id");
				table.setPjtMbrId(sabun);
				table.setPjtMbrName(rs.getString("pjt_mbr_name"));

				table.setPjtMbrJob(rs.getString("pjt_mbr_job"));
				table.setPjtMbrTel(rs.getString("pjt_mbr_tel"));
				table.setPjtMbrGrade(rs.getString("pjt_mbr_grade"));
				table.setPjtMbrDiv(rs.getString("pjt_mbr_div"));

				//수정 or 삭제가능 표시 [login_id가 과제관리자인 경우만 가능]
				String subMod="",subDel="";
				String man_sch = checkManSchedule(pcode,sabun);				//일정에 할당되었나.
				String pjt_pml = checkPjtPML (sabun);						//과제 PM인가
				if(man_sch.equals("N") && pjt_pml.equals("N")) {			//일정에없고,PM아님
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\">[삭제]</a>";
				} else if(man_sch.equals("N") && pjt_pml.equals("Y")) {		//일정없고,PM
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
				} else if(man_sch.equals("Y")) {							//기타
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\">[수정]</a>";
				}
			
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당멤버 QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getMemberRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_member where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new projectTable();
								
				table.setPid(rs.getString("pid"));
				table.setPjtCode(rs.getString("pjt_code"));	
				table.setPjtName(rs.getString("pjt_name"));	
				table.setPjtMbrType(rs.getString("pjt_mbr_type"));
				table.setMbrStartDate(rs.getString("mbr_start_date"));
				table.setMbrEndDate(rs.getString("mbr_end_date"));
				table.setMbrPoration(rs.getDouble("mbr_poration"));
				table.setPjtMbrId(rs.getString("pjt_mbr_id"));
				table.setPjtMbrName(rs.getString("pjt_mbr_name"));
				table.setPjtMbrJob(rs.getString("pjt_mbr_job"));
				table.setPjtMbrTel(rs.getString("pjt_mbr_tel"));
				table.setPjtMbrGrade(rs.getString("pjt_mbr_grade"));
				table.setPjtMbrDiv(rs.getString("pjt_mbr_div"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 과제인력정보 내용 저장하기 
	*******************************************************************/
	public void inputMember(String pjt_code,String pjt_name,String pjt_mbr_type,String mbr_start_date,
		String mbr_end_date,String mbr_poration,String pjt_member,String pjt_mbr_job) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//과제기본정보의 상태코드 바꿔주기
		changeGeneralStatus(pjt_code,"0");

		//관리번호 찾기
		String pid = getID();

		//사번만 찾기
		String sabun = pjt_member.substring(0,pjt_member.indexOf("/"));

		//PM관련정보
		String[] man = new String[5];
		man = searchManinfo(sabun);
		String name="",tel="",grade="",div="";
		name=man[1]; tel=man[2]; grade=man[3]; div=man[4];

		input = "INSERT INTO pjt_member(pid,pjt_code,pjt_name,pjt_mbr_type,mbr_start_date,mbr_end_date,";
		input += "mbr_poration,pjt_mbr_id,pjt_mbr_name,pjt_mbr_job,pjt_mbr_tel,pjt_mbr_grade,pjt_mbr_div) values('";
		input += pid+"','"+pjt_code+"','"+pjt_name+"','"+pjt_mbr_type+"','"+mbr_start_date+"','"+mbr_end_date+"','";
		input += Double.parseDouble(mbr_poration)+"','"+sabun+"','"+name+"','"+pjt_mbr_job+"','"+tel+"','"+grade+"','"+div+"')";
		stmt.executeUpdate(input);

		//기본정보에 인원수 반영하기
		updateGeneralMember (pjt_code,"A");

		stmt.close();
	}

	//*******************************************************************
	//	최초 인력구성시 기본정보의 상태코드를 바꿔주기
	//  1[PM] : S , 2 : 0 , else -- [기본정보pjt_general)의 상태코드바꿔]
	//*******************************************************************/	
	public void changeGeneralStatus (String pjt_code,String pjt_status) throws Exception
	{
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_member ";
		query += "where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		//1이면 기본정보[pjt_general]의 상태코드를 "S"
		String update = "";
		if(cnt == 1) {			//입력할때 [현재 1명]
			//기본정보
			update = "UPDATE pjt_general set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//과제코드정보
			update = "UPDATE prs_project set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//과제상태정보
			update = "UPDATE pjt_status set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		else if(cnt == 2) {		//삭제할때 [삭제전 현재2명]
			//기본정보
			update = "UPDATE pjt_general set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//과제코드정보
			update = "UPDATE prs_project set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
			//과제코드정보
			update = "UPDATE pjt_status set pjt_status = '"+pjt_status+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

	}

	/*******************************************************************
	* 인력정보 내용 수정하기 
	*******************************************************************/
	public void updateMember(String pid,String pjt_code,String pjt_name,String pjt_mbr_type,
		String mbr_start_date,String mbr_end_date,String mbr_poration,String pjt_mbr_id,
		String pjt_mbr_name,String pjt_mbr_job,String pjt_mbr_tel,String pjt_mbr_grade,
		String pjt_mbr_div) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//인력정보 수정하기 [pjt_member]
		update = "UPDATE pjt_member set pjt_code='"+pjt_code+"',pjt_name='"+pjt_name+"',pjt_mbr_type='"+pjt_mbr_type;
		update += "',mbr_start_date='"+mbr_start_date+"',mbr_end_date='"+mbr_end_date+"',mbr_poration='"+mbr_poration;
		update += "',pjt_mbr_id='"+pjt_mbr_id+"',pjt_mbr_name='"+pjt_mbr_name+"',pjt_mbr_job='"+pjt_mbr_job;
		update += "',pjt_mbr_tel='"+pjt_mbr_tel+"',pjt_mbr_grade='"+pjt_mbr_grade+"',pjt_mbr_div='"+pjt_mbr_div;
		update += "' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* 인력정보 내용 삭제하기
	*******************************************************************/
	public void deleteMember(String pid) throws Exception
	{
		Statement stmt = null;
		String delete = "";
		stmt = con.createStatement();

		//과제기본정보의 상태코드 바꿔주기
		String pjt_code = searchPjtCode(pid);
		changeGeneralStatus(pjt_code,"S");

		//1.사번구하기
		String sabun = searchSabun(pid);

		//2.과제PM인지 판단하기
		String pjt_pml = checkPjtPML (sabun);

		//3.기본정보 테이블 삭제하기 (과제PM이면 삭제 불가)
		if(pjt_pml.equals("N")) {
			delete = "DELETE from pjt_member where pid='"+pid+"'";
			stmt.executeUpdate(delete);
		}

		//4.기본정보에 인원수를 반영한다.
		updateGeneralMember (pjt_code,"D");

		stmt.close();
	}
	
	//*******************************************************************
	//	인력 등록/삭제시 총인원을 과제기본정보의 개발인원수에 반영하기
	//*******************************************************************/	
	public void updateGeneralMember (String pjt_code,String tag) throws Exception
	{
		String query = "",update="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//1.현재의 기본정보상의 인원수를 파악한다.
		int gen_cnt = 0;
		query  = "SELECT mbr_exp FROM pjt_general where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			gen_cnt = Integer.parseInt(rs.getString("mbr_exp"));
		}

		//2.현재의 인력정보상의 인원수를 파악한다
		int man_cnt = 0;
		query  = "SELECT COUNT(*) FROM pjt_member where pjt_code = '"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) {
			man_cnt = rs.getInt(1);
		}

		//3.인원수를 비교하여 인원수를 반영한다.
		if(tag.equals("A")) {			//인원추가
			update = "UPDATE pjt_general set mbr_exp='"+man_cnt+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);	
		} else if(tag.equals("D")) {	//인원삭제
			update = "UPDATE pjt_general set mbr_exp='"+man_cnt+"' where pjt_code='"+pjt_code+"'";
			stmt.executeUpdate(update);	
		}
	
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

	}

	//*******************************************************************
	//	pjt_code 구하기
	//*******************************************************************/	
	public String searchPjtCode (String pid) throws Exception
	{
		String pjt_code = "";
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT pjt_code FROM pjt_member ";
		query += "where pid = '"+pid+"'";
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			pjt_code = rs.getString("pjt_code");
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return pjt_code;

	}
	//*******************************************************************
	//	해당자가 일정에 추가되어나 판단하기
	//*******************************************************************/	
	public String checkManSchedule (String pjt_code,String sabun) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_schedule ";
		query += "where pjt_code = '"+pjt_code+"' and user_id = '"+sabun+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;	
	}
	//*******************************************************************
	//	과제PM 여부 판단하기
	//*******************************************************************/	
	public String checkPjtPML (String login_id) throws Exception
	{
		String rtn = "N";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT COUNT(*) FROM pjt_grade_mgr ";
		query += "where keyname = 'PJT_PML' and owner like '%"+login_id+"%'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = "Y";
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;	
	}

	//*******************************************************************
	//	해당관리번호의 사번리턴하기
	//*******************************************************************/	
	private String searchSabun(String pid) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT pjt_mbr_id FROM pjt_member where pid ='"+pid+"'";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("pjt_mbr_id");
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}

	//*******************************************************************
	//	해당사번 부서코드 리턴하기
	//*******************************************************************/	
	private String searchAcId (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT b.ac_code FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("ac_code");
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}

	//*******************************************************************
	//	해당사번 관련정보 리턴하기 [사번,이름,전화번호,직급,부서명]
	//*******************************************************************/	
	private String[] searchManinfo (String login_id) throws Exception
	{
		String[] rtn = new String[5];		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//PM정보 알아보기
		query  = "SELECT a.name,a.office_tel,c.ar_name,b.ac_name FROM user_table a,class_table b,rank_table c ";
		query += "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
		rs = stmt.executeQuery(query);
		if(rs.next())	{
			rtn[0] = login_id;						//사번
			rtn[1] = rs.getString("name");			//이름
			rtn[2] = rs.getString("office_tel");	//전화번호
			rtn[3] = rs.getString("ar_name");		//직급명
			rtn[4] = rs.getString("ac_name");		//부서명
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}

	/***************************************************************************
	 * ID을 구하는 메소드
	 **************************************************************************/
	public String getID()
	{
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("000");	
		String ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}	
}


