package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class pjtProcessDAO
{
	private Connection con;
	
	private String query = "";
	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가

	private String pjt_code = "";				//프로세스 코드
	private String child_node = "";				//프로세스의 자노드
	private String level_no = "0";				//현노드의 레벨번호
	private String type = "";					//P:전사표준,  부서코드:부서표준
	private String prs_code = "";				//프로세스 코드번호
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public pjtProcessDAO(Connection con) 
	{
		this.con = con;
	}

	public pjtProcessDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	/**********************************************************************
	 * 과제일정중 프로세스 정보를 배열에 담는다. 
	 * 트리구조형태로 데이터를 쿼리한 후, 배열에 담는다.
	 *********************************************************************/
	public void saveItemsArray(String pjt_code,String level_no,String parent_node) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select child_node,node_name,level_no,pid from pjt_schedule ";
		query += "where level_no = '"+level_no+"' and parent_node = '"+parent_node+"' ";
		query += "and pjt_code = '"+pjt_code+"' order by child_node asc";

		rs = stmt.executeQuery(query);

		int no=0;	String c_nd = "";
		while (rs.next()) {
			item[an][0] = rs.getString("level_no");
			c_nd = rs.getString("child_node");
			item[an][1] = c_nd + " " + rs.getString("node_name");
			item[an][2] = c_nd;
			item[an][3] = rs.getString("pid");
			//System.out.println(item[an][0]+":"+item[an][2]+":"+item[an][1]+":"+item[an][3]);
			an++;
			no = Integer.parseInt(item[an-1][0]);					//String을 정수로 바꾸기
			lno = Integer.toString(no+1);							//+1하여 정수를 String으로 바꾸기 
			//if(no<2) saveItemsArray(pjt_code,lno,item[an-1][2]);	//0,1,2레벨만 출력한다.
			saveItemsArray(pjt_code,lno,item[an-1][2]);
		}
		rs.close();
		stmt.close(); 
		
	} //saveItemsArray

	/**********************************************************************
	 * saveItemsArray() 실행후 담겨진 배열값을 이용하여
	 * tree_items.js 의 String을 만든다.
	 * pjt_code : 과제코드, level_no : 0, parent_node : 0, url : 링크
	 **********************************************************************/
	public String makeProcessTree(String pjt_code,String level_no,String parent_node,String url) throws Exception
	{
		String tree = "";				//tree
		String mode = "PNP_L";

		//전체수량및 배열 만들기
		int cnt = getAllTotalCount(pjt_code);
		item = new String[cnt][4];

		// 배열에 담는다.
		saveItemsArray(pjt_code,level_no,parent_node);

		//tree_items만들기 (an : item의 갯수)
		if(an > 0){
			String space = " ";
			int st = 0;		//시작점 level
			int cu = 0;		//현 읽은 level
			int di = 0;		//차이 : cu - st 

			String link = "";	// 링크URL을 담을 변수
			String tmp = "";	// 
			String [] pid = new String[4];

			tree = "var TREE_ITEMS = [";	//최초 시작점
			for(int bi=0; bi<cnt; bi++){
				if(item[bi][0].equals("1")) pid[0] = Integer.toString(bi);
				if(item[bi][0].equals("2")) pid[1] = Integer.toString(bi);
				if(item[bi][0].equals("3")) pid[2] = Integer.toString(bi);
				if(item[bi][0].equals("4")) pid[3] = Integer.toString(bi);
				tmp = "";
				for(int j=0;j<Integer.parseInt(item[bi][0]);j++){
					tmp += pid[j]+",";
				}
				if(tmp.length() == 0) tmp="0,";
				//링크 만들기
				if(item[bi][0].equals("1") || item[bi][0].equals("2")) 		//step,Activity만 링크있음
					link = url + "?mode="+mode+"&pid="+item[bi][3]+"&p_id="+tmp;
				else link = "";						//과제명,phase,step은 link없음

				//시작점
				if(bi == 0) {
					st = Integer.parseInt(item[bi][0]);
					space = " ";
					for(int s=0; s < st; s++) space += "   ";
					tree += space + "['"+item[bi][1]+"','"+link+"'";
					if(cnt == 1) tree += "],";		//1개일경우
				} else if(bi == (cnt-1)) {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
				
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],"; //앞레벨 끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'],";		//시작점
					}
					
					//마지막 레벨 닫기
					di = cu - 0;
					for(int e=di; e > 0; e--) {
						space = " ";
						for(int es=1; es < e; es++) space += "   ";
						tree += space + "],"; //끝점
					}
				} else {
					cu = Integer.parseInt(item[bi][0]);
					di = cu - st;
					
					space = " ";
					for(int s=0; s < cu; s++) space += "   ";

					if(di == 1) {
						tree += ",";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					} else if(di == 0) {
						tree += "],";		//끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					} else {
						tree += "],";		//끝점
						for(int m=0; m > di; di++) tree += space + "],"; //앞레벨 끝점
						tree += space + "['"+item[bi][1]+"','"+link+"'";		//시작점
					}
					st = Integer.parseInt(item[bi][0]);
				} //if	
			} //for
			tree += "];";	//맨 마지막
		} //if
		return tree;
	} //makeCategoryTree

	//*******************************************************************
	//	해당 과제코드의 총 수량 파악하기 
	//*******************************************************************/
	public int getAllTotalCount(String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM pjt_schedule where pjt_code='"+pjt_code+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	//*******************************************************************
	// 과제일정의 해당PID을 이용하여 기초정보 찾기
	//*******************************************************************/	
	public ArrayList getNodeBaseList(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		//프로세스코드,type구하기
		searchProcessUsePid(pid);
		
		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM pjt_schedule where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new projectTable();
				
				table.setPid(rs.getString("pid"));
				this.pjt_code = rs.getString("pjt_code");
				table.setPjtCode(this.pjt_code);	
				table.setPjtName(rs.getString("pjt_name"));
				table.setParentNode(rs.getString("parent_node"));
				this.child_node = rs.getString("child_node");
				table.setChildNode(child_node);	
				table.setNodeName(rs.getString("node_name"));	
				this.level_no = rs.getString("level_no");
				table.setLevelNo(level_no);	
				table.setPrsCode(this.prs_code);
				table.setPrsType(this.type);
											
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 과제일정의 해당PID을 이용하여 프로세스 code,type찾기
	//*******************************************************************/	
	public void searchProcessUsePid(String pid) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "",pjt_code="";	

		stmt = con.createStatement();

		//과제코드 찾기
		query = "SELECT pjt_code FROM pjt_schedule where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			pjt_code = rs.getString("pjt_code");
		}
				
		//프로세스코드및 type구하기
		query = "SELECT prs_code,prs_type FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.prs_code = rs.getString("prs_code");
			this.type = rs.getString("prs_type");
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 표준프로세스[전사,부서공통]에 등록할 노드 List가져오기
	//*******************************************************************/	
	public ArrayList getNodeList() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//level이 0 일때 [phase선택]
		if(level_no.equals("0")) {
			query = "SELECT ph_code,ph_name FROM prs_phase ";
			query += "where type='"+this.type+"' order by ph_code asc";	
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setPhCode(rs.getString("ph_code"));							
					table.setPhName(rs.getString("ph_name"));					
					table_list.add(table);
			}
		}
		//level이 1 일때 [step선택]
		else if(level_no.equals("1")) {
			query = "SELECT step_code,step_name FROM prs_step ";
			query += "where ph_code='"+this.child_node+"' and type='"+this.type+"' order by step_code asc";	
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setStepCode(rs.getString("step_code"));							
					table.setStepName(rs.getString("step_name"));					
					table_list.add(table);
			}
		}
		//level이 2 일때 [activity선택]
		else if(level_no.equals("2")) {
			query = "SELECT act_code,act_name FROM prs_activity ";
			query += "where step_code='"+this.child_node+"' and type='"+this.type+"' order by act_code asc";
			rs = stmt.executeQuery(query);
			while(rs.next()) { 
					table = new prsCodeTable();
					table.setActCode(rs.getString("act_code"));							
					table.setActName(rs.getString("act_name"));					
					table_list.add(table);
			}
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 표준프로세스[전사,부서공통]에 등록할 산출물 가져오기
	//*******************************************************************/	
	public ArrayList getNodeDocList() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT doc_code,doc_name FROM prs_docname ";
		query += "where step_code='"+this.child_node+"' and type='"+this.type+"' order by doc_code asc";
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new prsCodeTable();
				table.setDocCode(rs.getString("doc_code"));							
				table.setDocName(rs.getString("doc_name"));					
				table_list.add(table);
		}
	
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 표준프로세스산출물[전사,부서공통]에 등록된 산출물 가져오기
	//*******************************************************************/	
	public ArrayList getSaveDocList() throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		projectTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT * FROM pjt_document ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+this.child_node+"' and child_node like '%"+this.child_node+"%' order by child_node asc";
		rs = stmt.executeQuery(query);
		
		while(rs.next()) { 
				table = new projectTable();
				table.setPjtCode(rs.getString("pjt_code"));							
				table.setParentNode(rs.getString("parent_node"));	
				table.setChildNode(rs.getString("child_node"));							
				table.setNodeName(rs.getString("node_name"));	
				table.setLevelNo(rs.getString("level_no"));							
					
				table_list.add(table);
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 표준프로세스 Node 등록하기 [과제일정]
	//*******************************************************************/	
	public String inputNode(String pjt_code,String pjt_name,String parent_node,String[] child_node,String level_no) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//프로세스코드,type구하기
		searchProcessUsePjtCode(pjt_code);

		//관리번호 생성하기
		String pid = getID();

		//해당child_node을 이용하여 node_name구하기
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level이 1 일때 [phase선택]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+this.type+"' order by ph_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("ph_code");							
						node[n][1] = rs.getString("ph_name");					
				}
			}
		}
		//level이 2 일때 [step선택]
		else if(level_no.equals("2")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT step_code,step_name FROM prs_step ";
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+this.type+"' order by step_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("step_code");							
						node[n][1] = rs.getString("step_name");					
				}
			}
		}
		//level이 3 일때 [activity선택]
		else if(level_no.equals("3")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT act_code,act_name FROM prs_activity ";
				query += "where act_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+this.type+"' order by act_code asc";
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("act_code");							
						node[n][1] = rs.getString("act_name");					
				}
			}
		}


		//노드 등록하기
		String data = "";
		String npid = "",input="",c_node="",c_name="";
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");
		for(int i=0; i<cnt; i++) {
			c_node = node[i][0]; if(c_node == null) c_node = "";	//child node
			c_name = node[i][1]; if(c_name == null) c_name = "";	//node name
			npid = pid+nfm.toDigits(i);
			if(c_node.length() != 0) {
				data += insertNode(npid,pjt_code,pjt_name,parent_node,c_node,c_name,level_no,"pjt_schedule");
			}
		}
		
		stmt.close();
		rs.close();
		return data;
		
	}

	//*******************************************************************
	// 표준프로세스 산출기술문서 Node 등록하기 [전사,부서 공통]
	//*******************************************************************/	
	public void inputNodeDoc(String pjt_code,String pjt_name,String parent_node,String[] child_node,String level_no) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//프로세스코드,type구하기
		searchProcessUsePjtCode(pjt_code);

		//관리번호 생성하기
		String pid = getID();

		//해당child_node을 이용하여 node_name구하기
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level이 1 일때 [phase선택]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+this.type+"' order by ph_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("ph_code");							
						node[n][1] = rs.getString("ph_name");					
				}
			}
		}
		//level이 2 일때 [step선택]
		else if(level_no.equals("2")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT step_code,step_name FROM prs_step ";
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+this.type+"' order by step_code asc";	
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("step_code");							
						node[n][1] = rs.getString("step_name");					
				}
			}
		}
		//level이 3 일때 [activity선택]
		else if(level_no.equals("3")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT doc_code,doc_name FROM prs_docname ";
				query += "where doc_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+this.type+"' order by doc_code asc";
				rs = stmt.executeQuery(query);
				if(rs.next()) { 
						node[n][0] = rs.getString("doc_code");							
						node[n][1] = rs.getString("doc_name");					
				}
			}
		}


		//노드 등록하기
		String npid = "",input="",c_node="",c_name="";
		com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("00");
		for(int i=0; i<cnt; i++) {
			c_node = node[i][0]; if(c_node == null) c_node = "";	//child node
			c_name = node[i][1]; if(c_name == null) c_name = "";	//node name
			npid = pid+nfm.toDigits(i);
			if(c_node.length() != 0) {
				insertNode(npid,pjt_code,pjt_name,parent_node,c_node,c_name,level_no,"pjt_document");
			}
		}
		
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 입력 실행하기
	//*******************************************************************/	
	public String insertNode(String npid,String pjt_code,String pjt_name,String parent_node,String child_node,String node_name,String level_no,String tablename) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//노드 등록하기
		String input = "",data="";
		input = "INSERT INTO "+tablename+"(pid,pjt_code,pjt_name,parent_node,child_node,node_name,level_no) values('";
		input += npid+"','"+pjt_code+"','"+pjt_name+"','"+parent_node+"','"+child_node+"','"+node_name+"','"+level_no+"')";
		//중복이 없으면 등록됨
		if(isNoDuplicate(pjt_code,parent_node,child_node,level_no,tablename)) { 	
			data = input + "<br>";
			stmt.executeUpdate(input);
		}
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 등록 중복검사하기 [전사,부서공통]
	//*******************************************************************/	
	public boolean isNoDuplicate(String pjt_code,String parent_node,String child_node,String level_no,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"' and level_no='"+level_no+"'";	
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = false; 

		stmt.close();
		rs.close();
		return rtn;		
	}

	
	//*******************************************************************
	// 과제일정의 pjt_code 을 이용하여 프로세스 code,type찾기
	//*******************************************************************/	
	public void searchProcessUsePjtCode(String pjt_code) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
	
		//프로세스코드및 type구하기
		query = "SELECT prs_code,prs_type FROM pjt_general where pjt_code='"+pjt_code+"'";	
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			this.prs_code = rs.getString("prs_code");
			this.type = rs.getString("prs_type");
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 일정관리의 프로세스 Node 삭제하기
	//*******************************************************************/	
	public String deleteNode(String pid,String pjt_code,String parent_node,String[] child_node,String level_no) throws Exception
	{
		String rtn = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//해당child_node 크기 구하기
		int cnt = child_node.length;

		//노드삭제전 산출물이 있는지 판단하고, 전노드내용삭제전에 산출물을 먼저 삭제토록 메시지 출력
		//1. 해당노드 전체삭제인지 검사
		String cu_node = "";
		int same = 0,cu_cnt = 0;
		String query = "SELECT child_node from pjt_schedule ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and level_no='3'";	
		rs = stmt.executeQuery(query);
		while(rs.next()) {
			cu_node = rs.getString("child_node");
			for(int i=0; i<child_node.length; i++) {
				if(cu_node.equals(child_node[i])) same++;
			}
			cu_cnt++;
		}

		//2.해당노드의 산출물이 있는지 조사
		String cu_doc = "N";
		if(!isNoUnderTree(pjt_code,parent_node,"pjt_document")) {
			cu_doc = "Y";
		}
		
		//3.중단할지 계속할지 판단하기
		if((same == cu_cnt) && cu_doc.equals("Y")){ rtn = "DOC"; stmt.close(); 	rs.close(); return rtn; }

		//노드 삭제하기
		String delete="",c_node="";
		for(int i=0; i<cnt; i++) {
			c_node = child_node[i];	if(c_node == null) c_node = "";
			if(c_node.length() != 0) {
				rtn += removeNode(pjt_code,parent_node,c_node,"pjt_schedule");
			}
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// 표준프로세스 산출기술문서 Node 삭제하기 [전사,부서공통]
	//*******************************************************************/	
	public String deleteNodeDoc(String pid,String pjt_code,String parent_node,String[] child_node,String level_no) throws Exception
	{
		String rtn = "";
		Statement stmt = null;
		stmt = con.createStatement();

		//해당child_node 크기 구하기
		int cnt = child_node.length;

		//노드 삭제하기
		String delete="",c_node="";
		for(int i=0; i<cnt; i++) {
			c_node = child_node[i];	if(c_node == null) c_node = "";
			//산출물 등록된 노드이면 삭제불가
			if(c_node.length() != 0) {
				rtn += removeNode(pjt_code,parent_node,c_node,"pjt_document");
			}
		}
	
		if(rtn.length() > 1) rtn = rtn.substring(0,rtn.length()-1);
		stmt.close();
		return rtn;
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 삭제 실행하기
	//*******************************************************************/	
	public String removeNode(String pjt_code,String parent_node,String child_node,String tablename) throws Exception
	{
		String delete = "",data="";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		//1. 해당 프로세스만 노드의 상태를 파악한다.[노드상태가 NULL일때만 가능] 
		String node_status = "",use_doc="N";
		if(tablename.equals("pjt_schedule")) {
			query = "SELECT node_status from pjt_schedule ";
			query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"'";	
			rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				node_status = rs.getString("node_status"); if(node_status == null) node_status = "";
				
				if(node_status.equals("1")) node_status = "[node working]";
				else if(node_status.equals("2")) node_status = "[node finished]";
				else if(node_status.equals("3")) node_status = "[node drop]";
				else if(node_status.equals("4")) node_status = "[node hold]";
				else if(node_status.equals("5")) node_status = "[node skip]";
			}
			if(node_status.length() != 0) { 
				data = child_node+node_status+","; stmt.close(); 	rs.close(); 
				return data;
			}
			rs.close();
		}
		//산출물인 경우:산출물이 등록된 경우면 삭제불가
		else if(tablename.equals("pjt_document")) {
			query = "SELECT use_doc from pjt_document ";
			query += "where pjt_code='"+pjt_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"'";	
			rs = stmt.executeQuery(query);
		
			if(rs.next()) {
				use_doc = rs.getString("use_doc"); if(use_doc == null) use_doc = "N";
			}	
			if(use_doc.equals("Y")) {
				data = child_node+"[approval]"+","; stmt.close(); 	rs.close(); 
				return data;
			}
			rs.close();
		}

		//노드 삭제하기
		delete = "DELETE from "+tablename+" where pjt_code='"+pjt_code+"' ";
		delete += "and parent_node='"+parent_node+"' and child_node='"+child_node+"'";

		//하부구조가 없으면 삭제됨
		if(isNoUnderTree(pjt_code,child_node,tablename)) {		
			stmt.executeUpdate(delete);
		} else { data += child_node + ","; }
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// 일정관리 프로세스 Node[산출기술문서포함]의 하부구조가 있는지 검사하기 
	//*******************************************************************/	
	public boolean isNoUnderTree(String pjt_code,String child_node,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where pjt_code='"+pjt_code+"' and parent_node='"+child_node+"' ";
		query += "and child_node like '%"+child_node+"%'";	
	
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);	
		if(cnt > 0) rtn = false; 

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
