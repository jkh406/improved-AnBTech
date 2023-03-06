package com.anbtech.mr.db;
import com.anbtech.mr.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class asresultworkDAO
{
	private Connection con;
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자처리
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public asresultworkDAO(Connection con) 
	{
		this.con = con;
	}

	public asresultworkDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기 [해당업체별 전체수량]
	//*******************************************************************/
	private int getTotalCount(String company_no,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%'"; 
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
	// AS 지원실적이력정보 전체LIST [해당업체별]
	//*******************************************************************/	
	public ArrayList getWorkList (String company_no,String sItem,String sWord,
		String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		total_cnt = getTotalCount(company_no,sItem,sWord);
	
		//query문장 만들기
		query = "SELECT * FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%' order by pid desc"; 
		rs = stmt.executeQuery(query);

		//페이지 정수로 바꿔주기
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		this.current_page = Integer.parseInt(page);	//출력할 페이지

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
				table = new assupportTable();
								
				String pid = rs.getString("pid");
				table.setPid(pid);

				String register_no = rs.getString("register_no"); if(register_no == null) register_no = "";
				table.setRegisterNo(register_no);	
				
				table.setRegisterDate(rs.getString("register_date"));
				table.setAsField(rs.getString("as_field"));	
				table.setCode(rs.getString("code"));
				table.setRequestName(rs.getString("request_name"));
				table.setSerialNo(rs.getString("serial_no"));
				table.setRequestDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setAsDate(anbdt.getSepDate(rs.getString("as_date"),"-"));
				table.setAsType(rs.getString("as_type"));
				
				String as_content = rs.getString("as_content");
				if(as_content.length() > 30) as_content = as_content.substring(0,30)+" ...";
				as_content = "<a href=\"javascript:workView('"+pid+"');\">"+as_content+"</a>";
				table.setAsContent(as_content); 

				table.setAsResult(rs.getString("as_result"));
				table.setAsDelay(rs.getString("as_delay"));
				table.setAsIssue(rs.getString("as_issue"));
				table.setWorker(rs.getString("worker"));
				table.setCompanyNo(rs.getString("company_no"));

				String value_request = rs.getString("value_request");
				table.setCompanyNo(value_request);

				//멜보내기,보기,수정,삭제가능 표시 [단,수정/삭제는 멜보내기전에 한해 가능함]
				String subMod="",subDel="",subView="",subMail="";
				if(value_request.equals("0")) {				//평가요청전 편집
					subMod = "<a href=\"javascript:workModify('"+pid+"');\"><img src='../mr/images/lt_modify.gif' border='0' align='absmiddle'></a>";
					subDel = "<a href=\"javascript:workDelete('"+pid+"');\"><img src='../mr/images/lt_del.gif' border='0' align='absmiddle'></a>";
					subMail = "<a href=\"javascript:workMail('"+pid+"');\"><img src='../mr/images/lt_mail_e.gif' border='0' align='absmiddle'></a>";
				} else if(register_no.length() == 0){		//초기평가요청및 평가독촉용
					subMail = "<a href=\"javascript:workMail('"+pid+"');\"><img src='../mr/images/lt_mail_e.gif' border='0' align='absmiddle'></a>";
				} 
				table.setModify(subMod);
				table.setDelete(subDel);
				table.setMail(subMail);
				
				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 화면에서 페이지로 바로가기 표현하기 [해당업체별]
	//*******************************************************************/	
	public ArrayList getDisplayPage(String company_no,String sItem,String sWord,
		String page,int max_display_cnt,int max_display_page) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();
		
		//총갯수 구하기
		total_cnt = getTotalCount(company_no,sItem,sWord);

		//query문장 만들기
		query = "SELECT * FROM as_result where company_no='"+company_no+"'";
		query += " and "+sItem+" like '%"+sWord+"%' order by as_date desc"; 
		rs = stmt.executeQuery(query);

		// 전체 페이지의 값을 구한다.
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(total_page*max_display_cnt  != total_cnt) total_page = total_page + 1;

		// 시작페이지와 마지막페이지를 정의
		int startpage = (int)((Integer.parseInt(page) - 1) / max_display_page) * max_display_page + 1;
		int endpage= (int)((((startpage - 1) + max_display_page) / max_display_page) * max_display_page);
	
		// 페이지 이동관련 문자열을 담을 변수. 즉, [prev] [1][2][3] [next]
		String pagecut = "";
		
		//페이지 바로가기 만들기
		int curpage = 1;
		if (total_page <= endpage) endpage = total_page;
		//prev
		if (Integer.parseInt(page) > max_display_page){
			curpage = startpage -1;
			pagecut = "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">[Prev]</a>";
		}
		//중간
		curpage = startpage;
		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (total_page != 1) pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">["+curpage+"]</a>";
			}
		
			curpage++;
		}
		//next
		if (total_page > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=asresultworkServlet?&mode=ART_L&page="+curpage+"&company_no="+company_no+"&sItem="+sItem+"&sWord="+sWord+">[Next]</a>";
		}
	
		//arraylist에 담기
		table = new assupportTable();
		table.setPageCut(pagecut);							//선택할 수 있는 페이지 표현
		table.setTotalPage(total_page);						//총페이지수
		table.setCurrentPage(Integer.parseInt(page));		//현재페이지
		table.setTotalArticle(total_cnt);					//총 조항갯수
		table_list.add(table);

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 해당업체 AS실적 지정내용상세보기
	//*******************************************************************/	
	public ArrayList getWorkRead (String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM as_result where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setPid(rs.getString("pid"));
				String register_no = rs.getString("register_no"); if(register_no == null) register_no = "";
				table.setRegisterNo(register_no);	
				table.setRegisterDate(rs.getString("register_date"));
				table.setAsField(rs.getString("as_field"));	
				table.setCode(rs.getString("code"));
				table.setRequestName(rs.getString("request_name"));
				table.setSerialNo(rs.getString("serial_no"));
				table.setRequestDate(anbdt.getSepDate(rs.getString("request_date"),"-"));
				table.setAsDate(anbdt.getSepDate(rs.getString("as_date"),"-"));
				table.setAsType(rs.getString("as_type"));
				table.setAsContent(rs.getString("as_content"));
				table.setAsResult(rs.getString("as_result"));
				table.setAsDelay(rs.getString("as_delay"));
				table.setAsIssue(rs.getString("as_issue"));
				table.setWorker(rs.getString("worker"));
				table.setCompanyNo(rs.getString("company_no"));
				table.setCompanyNo(rs.getString("value_request"));
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	/*******************************************************************
	* 실적 입력하기
	*******************************************************************/
	public void inputWork(String as_field,String code,String request_name,String serial_no,String request_date,
		String as_date,String as_type,String as_content,String as_result,String as_delay,
		String as_issue,String worker,String company_no) throws Exception
	{
		String input = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//기본정보 생성하기
		String pid = getID();								//관리번호
		String register_no = "";							//등록번호
		String register_date = anbdt.getDateNoformat();		//등록일
		String value_request = "0";							//평가요청횟수

		//실적입력하기 : as_result
		input = "INSERT INTO as_result(pid,register_no,register_date,as_field,code,request_name,serial_no,";
		input += "request_date,as_date,as_type,as_content,as_result,as_delay,as_issue,worker,company_no,";
		input += "value_request) values('";
		input += pid+"','"+register_no+"','"+register_date+"','"+as_field+"','"+code+"','"+request_name+"','"+serial_no+"','";
		input += request_date+"','"+as_date+"','"+as_type+"','"+as_content+"','"+as_result+"','"+as_delay+"','";
		input += as_issue+"','"+worker+"','"+company_no+"','"+value_request+"')";
		stmt.executeUpdate(input);

		stmt.close();
	}

	/*******************************************************************
	* 실적 수정하기 : 평가요청 전송전
	*******************************************************************/
	public void updateWork(String pid,String as_field,String serial_no,String request_date,
		String as_date,String as_type,String as_content,String as_result,String as_delay,
		String as_issue) throws Exception
	{
		String update = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//기본정보 생성하기
		String register_date = anbdt.getDateNoformat();		//등록일

		//실적정보 수정하기 [as_result]
		update = "UPDATE as_result set as_field='"+as_field+"',serial_no='"+serial_no+"',request_date='"+request_date;
		update += "',register_date='"+register_date+"',as_date='"+as_date+"',as_type='"+as_type;
		update += "',as_content='"+as_content+"',as_result='"+as_result+"',as_delay='"+as_delay;
		update += "',as_issue='"+as_issue+"' where pid='"+pid+"'";
		stmt.executeUpdate(update);

		stmt.close();
	}

	/*******************************************************************
	* 실적 삭제하기 : 평가요청 전송전
	*******************************************************************/
	public void deleteWork(String pid) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String delete = "";
		
		stmt = con.createStatement();

		//삭제하기
		delete = "DELETE from as_result where pid='"+pid+"'";
		stmt.executeUpdate(delete);

		stmt.close();
	}


	/*********************************************************************
	 	작업완료내용을 AS요청자에게 전자우편으로 보내기
	*********************************************************************/
/*	public void sendMailToDIV(String u_id,String u_name,String pjt_code,String pjt_name,String node_code,String node_name,
		String evt_content,String evt_note,String evt_issue) throws Exception 
	{	
		String pid = getID();								//관리번호
		String subject = "";								//제목
		String user_id = "", user_name = "", rec = "";		//작성자 사번,이름,수신자List
		String write_date = anbdt.getTime();				//전자우편 전송일자
		String delete_date = anbdt.getAddMonthNoformat(1);	//삭제예정일자

		//1.작성자[과제PM] 정보
		user_id = u_id;											//작성자 사번
		user_name = u_name;										//작성자 이름
		subject = "노드완료 승인요청";								//제목
		String bon_path = "/post/"+user_id+"/text_upload";		//본문패스
		String filename = pid;									//본문저장 파일명

		//2.과제PM 구하기
		rec = searchPjtPM(pjt_code)+";";						//수신자 [사번/이름;]
		String rec_id = rec.substring(0,rec.indexOf("/"));
//		rec = rec_id+"/"+rec_name+";";							//수신자 

		//3.전자우편으로 보내기
		Statement stmt = null;
		stmt = con.createStatement();
		String letter="";
			letter = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,delete_date) values('";
			letter += pid+"','"+subject+"','"+user_id+"','"+user_name+"','"+write_date+"','"+rec_id+"','"+"0"+"','"+delete_date+"')";
		stmt.executeUpdate(letter);	
		String master="";
			master = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
			master += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
			master += pid + "','" + subject + "','" + user_id + "','" + user_name + "','" + write_date + "','" + rec + "','" + "0" + "','";
			master += "email" + "','" + "CFM" + "','" + bon_path + "','" + filename + "','" + "" + "','" + "" + "','";
			master += "" + "','" +"" + "','" + "" + "','" + "" + "','" + delete_date + "')";		
		stmt.executeUpdate(master);

		//4.본문파일 만들기
		String upload_path = com.anbtech.admin.db.ServerConfig.getConf("upload_path");	//upload_path
		String content = "<html><head><title>노드작업 완료승인 요청</title></head>";
			content += "<body>";
			content += "<h3>노드작업 완료내용</h3>";
			content += "<ul>";
			content += "<li>과제이름 : "+pjt_code+" "+pjt_name+"</li>";
			content += "<li>노드이름 : "+node_code+" "+node_name+"</li>";
			content += "<li>주요내용 : <pre>"+evt_content+"</pre></li>";
			content += "<li>주요문제 : <pre>"+evt_note+"</pre></li>";
			content += "<li>주요이슈 : <pre>"+evt_issue+"</pre></li>";
			content += "</ul>";
			content += "</body></html>";

		String path = upload_path + "/gw/mail" + bon_path;						//저장될 path
		write.setFilepath(path);												//directory생성하기
		write.WriteHanguel(path,filename,content);								//내용 파일로 저장하기

		stmt.close();
	}
*/
	//*******************************************************************
	// A/S지원분야 목록가져오기
	//*******************************************************************/	
	public ArrayList getAsField() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT register_name,sno FROM as_env where mgr_name='AS_FIELD' and use_yn='1'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setRegisterName(rs.getString("register_name"));
				table.setSno(rs.getString("sno"));
			
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 평가할 항목 가져오기
	//*******************************************************************/	
	public ArrayList getScoreItem(String as_field) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		assupportTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM as_scoreitem where as_field='"+as_field+"' and use_yn='1' ";
		query += "order by score_no asc";
//System.out.println("q : " + query);
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new assupportTable();
								
				table.setScoreNo(rs.getString("score_no"));
				table.setScoreItem(rs.getString("score_item"));
			
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
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



