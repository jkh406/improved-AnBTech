package com.anbtech.pjt.db;
import com.anbtech.pjt.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

public class prsStandardDAO
{
	private Connection con;
	
	private String query = "";
	private String[][] item = null;				//프로세스정보를 배열로 담기
	private int an = 0;							//items의 배열 증가

	private String prs_code = "";				//프로세스 코드
	private String child_node = "";				//프로세스의 자노드
	private String level_no = "0";				//현노드의 레벨번호
	private String type = "";					//P:전사표준,  부서코드:부서표준
	
	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public prsStandardDAO(Connection con) 
	{
		this.con = con;
	}

	public prsStandardDAO() 
	{
		com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		Connection con = connMgr.getConnection("mssql");
		this.con = con;
	}

	/**********************************************************************
	 * 프로세스 정보를 배열에 담는다. 
	 * 트리구조형태로 데이터를 쿼리한 후, 배열에 담는다.
	 *********************************************************************/
	public void saveItemsArray(String prs_code,String level_no,String parent_node,String type) throws Exception
	{
		//변수 초기화
		String lno = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		query = "select child_node,node_name,level_no,pid from prs_process ";
		query += "where level_no = '"+level_no+"' and parent_node = '"+parent_node+"' ";
		query += "and prs_code = '"+prs_code+"' and type='"+type+"' order by child_node asc";

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
			//if(no<2) saveItemsArray(prs_code,lno,item[an-1][2]);	//0,1,2레벨만 출력한다.
			saveItemsArray(prs_code,lno,item[an-1][2],type);
		}
		rs.close();
		stmt.close(); 
		
	} //saveItemsArray

	/**********************************************************************
	 * saveItemsArray() 실행후 담겨진 배열값을 이용하여
	 * tree_items.js 의 String을 만든다.
	 * prs_code : 프로세스코드, level_no : 0, parent_node : 0, url : 링크
	 **********************************************************************/
	public String makeProcessTree(String prs_code,String level_no,String parent_node,String type,String url) throws Exception
	{
		String tree = "";				//tree
		String mode = "STD_LA";
		if(!type.equals("P")) mode="STD_LD";

		//전체수량및 배열 만들기
		int cnt = getAllTotalCount(prs_code,type);
		item = new String[cnt][4];

		// 배열에 담는다.
		saveItemsArray(prs_code,level_no,parent_node,type);

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
				link = url + "?mode="+mode+"&pid="+item[bi][3]+"&p_id="+tmp;
				if(item[bi][0].equals("3")) link = "";	//마지막레벨은 link없음

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
	//	총 수량 파악하기 (전사공통)
	//*******************************************************************/
	public int getAllTotalCount(String prs_code,String type) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		query = "SELECT COUNT(*) FROM prs_process where prs_code='"+prs_code+"' and type='"+type+"'";
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);

		stmt.close();
		rs.close();
		return cnt;			
	}

	//*******************************************************************
	// 관리번호[pid]을 이용하여 기초정보 찾기
	//*******************************************************************/	
	public ArrayList getNodeBaseList(String pid,String tag,String login_id) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		//this.type구하기 [처음 pid가 없을때 type을 setting하기]
		if(tag.equals("P")) this.type = "P";	//전사표준임
		else if(tag.equals("D")) this.type = searchAcId(login_id);

		stmt = con.createStatement();
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM prs_process where pid='"+pid+"'";	
		rs = stmt.executeQuery(query);

		while(rs.next()) { 
				table = new prsCodeTable();
				
				table.setPid(rs.getString("pid"));
				this.prs_code = rs.getString("prs_code");
				table.setPrsCode(this.prs_code);							
				table.setParentNode(rs.getString("parent_node"));
				this.child_node = rs.getString("child_node");
				table.setChildNode(child_node);	
				table.setNodeName(rs.getString("node_name"));	
				this.level_no = rs.getString("level_no");
				table.setLevelNo(level_no);	
				this.type = rs.getString("type");
				table.setType(type);							
				
				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
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
		prsCodeTable table = null;
		ArrayList table_list = new ArrayList();

		query = "SELECT * FROM prs_document ";
		query += "where prs_code='"+prs_code+"' and parent_node='"+this.child_node+"' and child_node like '%"+this.child_node+"%' and type='"+this.type+"' order by child_node asc";
		rs = stmt.executeQuery(query);
		
		while(rs.next()) { 
				table = new prsCodeTable();
				table.setPrsCode(rs.getString("prs_code"));							
				table.setParentNode(rs.getString("parent_node"));	
				table.setChildNode(rs.getString("child_node"));							
				table.setNodeName(rs.getString("node_name"));	
				table.setLevelNo(rs.getString("level_no"));							
				table.setType(rs.getString("type"));	
				table_list.add(table);
		}
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// 표준프로세스 Node 등록하기 [전사,부서 공통]
	//*******************************************************************/	
	public String inputNode(String prs_code,String parent_node,String[] child_node,String level_no,String type) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//관리번호 생성하기
		String pid = getID();

		//해당child_node을 이용하여 node_name구하기
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level이 1 일때 [phase선택]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+type+"' order by ph_code asc";	
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
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+type+"' order by step_code asc";	
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
				query += "where act_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+type+"' order by act_code asc";
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
				data += insertNode(npid,prs_code,parent_node,c_node,c_name,level_no,type,"prs_process");
			}
		}
		
		stmt.close();
		rs.close();
		return data;
		
	}

	//*******************************************************************
	// 표준프로세스 산출기술문서 Node 등록하기 [전사,부서 공통]
	//*******************************************************************/	
	public void inputNodeDoc(String prs_code,String parent_node,String[] child_node,String level_no,String type) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		//관리번호 생성하기
		String pid = getID();

		//해당child_node을 이용하여 node_name구하기
		int cnt = child_node.length;
		String[][] node = new String[cnt][2];
		//level이 1 일때 [phase선택]
		if(level_no.equals("1")) {
			for(int n=0; n<cnt; n++) {
				query = "SELECT ph_code,ph_name FROM prs_phase ";
				query += "where ph_code='"+child_node[n]+"' and type='"+type+"' order by ph_code asc";	
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
				query += "where step_code='"+child_node[n]+"' and ph_code='"+parent_node+"' and type='"+type+"' order by step_code asc";	
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
				query += "where doc_code='"+child_node[n]+"' and step_code='"+parent_node+"' and type='"+type+"' order by doc_code asc";
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
				insertNode(npid,prs_code,parent_node,c_node,c_name,level_no,type,"prs_document");
			}
		}
		
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 입력 실행하기
	//*******************************************************************/	
	public String insertNode(String npid,String prs_code,String parent_node,String child_node,String node_name,String level_no,String type,String tablename) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//노드 등록하기
		String input = "",data="";
		input = "INSERT INTO "+tablename+"(pid,prs_code,parent_node,child_node,node_name,level_no,type) values('";
		input += npid+"','"+prs_code+"','"+parent_node+"','"+child_node+"','"+node_name+"','"+level_no+"','"+type+"')";
		//중복이 없으면 등록됨
		if(isNoDuplicate(prs_code,parent_node,child_node,level_no,type,tablename)) { 	
			data = input + "<br>";
			stmt.executeUpdate(input);
		}
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 등록 중복검사하기 [전사,부서공통]
	//*******************************************************************/	
	public boolean isNoDuplicate(String prs_code,String parent_node,String child_node,String level_no,String type,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where prs_code='"+prs_code+"' and parent_node='"+parent_node+"' and child_node='"+child_node+"' and level_no='"+level_no+"' and type='"+type+"'";	
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		if(cnt > 0) rtn = false; 

		stmt.close();
		rs.close();
		return rtn;		
	}

	//*******************************************************************
	// 표준프로세스 Node 삭제하기
	//*******************************************************************/	
	public String deleteNode(String pid,String prs_code,String parent_node,String[] child_node,String level_no,String type) throws Exception
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
		String query = "SELECT child_node from prs_process ";
		query += "where prs_code='"+prs_code+"' and parent_node='"+parent_node+"' and type='"+type+"' and level_no='3'";	
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
		if(!isNoUnderTree(prs_code,parent_node,type,"prs_document")) {
			cu_doc = "Y";
		}
		
		//3.중단할지 계속할지 판단하기
		if((same == cu_cnt) && cu_doc.equals("Y")){
			rtn = "DOC";
			stmt.close();
			rs.close();
			return rtn;
		}

		//노드 삭제하기
		String delete="",c_node="";
		for(int i=0; i<cnt; i++) {
			c_node = child_node[i];	if(c_node == null) c_node = "";
			if(c_node.length() != 0) {
				rtn += removeNode(prs_code,parent_node,c_node,type,"prs_process");
			}
		}
		
		stmt.close();
		rs.close();
		return rtn;
	}

	//*******************************************************************
	// 표준프로세스 산출기술문서 Node 삭제하기 [전사,부서공통]
	//*******************************************************************/	
	public String deleteNodeDoc(String pid,String prs_code,String parent_node,String[] child_node,String level_no,String type) throws Exception
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
			if(c_node.length() != 0) {
				rtn += removeNode(prs_code,parent_node,c_node,type,"prs_document");
			}
		}
		if(rtn.length() > 1) rtn = rtn.substring(0,rtn.length()-1);
		stmt.close();
		return rtn;
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함] 삭제 실행하기
	//*******************************************************************/	
	public String removeNode(String prs_code,String parent_node,String child_node,String type,String tablename) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();

		//노드 삭제하기
		String delete = "",data="";
		delete = "DELETE from "+tablename+" where prs_code='"+prs_code+"' ";
		delete += "and parent_node='"+parent_node+"' and child_node='"+child_node+"' and type='"+type+"'";
		//하부구조가 없으면 삭제됨
		if(isNoUnderTree(prs_code,child_node,type,tablename))		
			stmt.executeUpdate(delete);
		else data += child_node + ",";
		
		stmt.close();
		return data;
	}

	//*******************************************************************
	// 표준프로세스 Node[산출기술문서포함]의 하부구조가 있는지 검사하기 [전사,부서공통]
	//*******************************************************************/	
	public boolean isNoUnderTree(String prs_code,String child_node,String type,String tablename) throws Exception
	{
		boolean rtn = true;
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";
		stmt = con.createStatement();

		query = "SELECT COUNT(*) from "+tablename+" ";
		query += "where prs_code='"+prs_code+"' and parent_node='"+child_node+"' ";
		query += "and child_node like '%"+child_node+"%' and type='"+type+"'";	
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
