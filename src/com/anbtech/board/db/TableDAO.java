/************************************************************
 * 게시판 테이블 내용을 입력,수정,삭제한다. 
 * 
 *
 ************************************************************/

package com.anbtech.board.db;

import com.anbtech.board.entity.*;
import com.anbtech.board.business.*;

import java.sql.*;
import java.util.*;

public class TableDAO{

	private Connection con;

	public TableDAO(Connection con){
		this.con = con;
	}

	public void setTable(String tablename, String thread, String depth, String pos, String writer, String email, String homepage, String ip_addr, 
		String password, String subject, String content, String html, String email_forward, String category) throws Exception{

		PreparedStatement pstmt = null;

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd a hh:mm");
		String w_time = vans.format(now);

		//사용자가 입력한 글을 데이터베이스에 저장한다.
		String query = "INSERT INTO " + tablename + "(thread,depth,pos,rid,vid,cid,writer,email,homepage,ip_addr,passwd,subject,content,html,email_forward,category,w_time) VALUES " +
			"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,thread);
		pstmt.setInt(2,Integer.parseInt(depth));
		pstmt.setString(3,pos);
		pstmt.setInt(4,0);
		pstmt.setInt(5,0);
		pstmt.setInt(6,0);
		pstmt.setString(7,writer);
		pstmt.setString(8,email);
		pstmt.setString(9,homepage);
		pstmt.setString(10,ip_addr);
		pstmt.setString(11,password);
		pstmt.setString(12,subject);
		pstmt.setString(13,content);
		pstmt.setString(14,html);
		pstmt.setString(15,email_forward);
		pstmt.setString(16,category);
		pstmt.setString(17,w_time);
		
		pstmt.executeUpdate();
		pstmt.close();
	}

	public String getNo_bypos(String tablename, String pos) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		String query = "SELECT no FROM " + tablename + " WHERE pos=" + pos;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		String no = Integer.toString(rs.getInt("no"));
		stmt.close();
		rs.close();

		return no;
	}
	

	public int getTotal(String tablename, String where) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;

		String query = "SELECT COUNT(*) FROM "+tablename+where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total = rs.getInt(1);
		stmt.close();
		rs.close();
		return total;
	}

	public Table getTable_max(String tablename) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;

		// 가장 큰 thread 값을 가져와서 1을 더한 값을 thread에 넣는다
		String query = "SELECT MAX(thread) as thread, MAX(pos) as pos FROM " + tablename;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		Table table = new Table();
		if (rs.next()){
			table.setThread(Integer.toString(rs.getInt("thread")+1));
			table.setPos(Integer.toString(rs.getInt("pos")+1));
		}else{
			table.setThread("1");
			table.setPos("1");
		}
		stmt.close();
		rs.close();

		return table;
	}

	public Table getTable(String tablename, String no) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		Table table = new Table();

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT * FROM " + tablename + " WHERE no ='"+no+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		table.setNo(Integer.toString(rs.getInt("no")));
		table.setThread(Integer.toString(rs.getInt("thread")));
		table.setDepth(Integer.toString(rs.getInt("depth")));
		table.setPos(Integer.toString(rs.getInt("pos")));
		table.setRid(rs.getInt("rid"));
		table.setVid(rs.getInt("vid"));
		table.setCid(rs.getInt("cid"));
		table.setWriter(rs.getString("writer"));
		table.setEmail(rs.getString("email"));
		table.setHomepage(rs.getString("homepage"));
		table.setIp_addr(rs.getString("ip_addr"));
		table.setPasswd(rs.getString("passwd"));
		table.setSubject(rs.getString("subject"));
		table.setContent(rs.getString("content"));
		table.setHtml(rs.getString("html"));
		table.setEmail_forward(rs.getString("email_forward"));
		table.setFilename(rs.getString("filename"));
		table.setFiletype(rs.getString("filetype"));
		table.setDid(rs.getString("did"));
		table.setCategory(rs.getString("category"));
		table.setW_time(rs.getString("w_time"));
		table.setU_time(rs.getString("u_time"));

		stmt.close();
		rs.close();
		return table;
	}

	public ArrayList getTable_list(String tablename, ArrayList no_list, String searchword, String searchscope, String category, String boardpage, String mapping) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;

		Board_Env board_env = new Board_Env();
		Board_EnvDAO board_envDAO = new Board_EnvDAO(con);
		board_env = board_envDAO.getBoard_env(tablename);

		Table table = new Table();
		ArrayList table_list = new ArrayList();
		
		//list에서 사용할 변수를 board_env에서 가져온다.
		int l_maxlist = board_env.getL_maxlist();
		int l_maxpage = board_env.getL_maxpage();
		int l_maxsubjectlen = board_env.getL_maxsubjectlen();
		int enablechkcool = board_env.getEnablechkcool();
		int current_page_num =Integer.parseInt(boardpage);

		String enablepreview = board_env.getEnablepreview();
		String enablecomment = board_env.getEnablecomment();
		String skin = board_env.getSkin();

		// 현재의 시간을 저장
		com.anbtech.util.Date jud = new com.anbtech.util.Date();
		String now_time = jud.getCurrentDate();

		RedirectBO redirectBO = new RedirectBO(con);
		String where = redirectBO.getWhere(searchword, searchscope, category);

		int total = getTotal(tablename, where);	// 전체 게시물 갯수
		int recNum = total;
		

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT w_time,cid,no,content,pos,subject,thread,depth,rid,vid,writer,email,homepage,html FROM " + tablename + where + " ORDER BY pos DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			String w_time = rs.getString("w_time").substring(0,10);
			int cid = rs.getInt("cid");
			int no = rs.getInt("no");
			String content = rs.getString("content");
			String pos = Integer.toString(rs.getInt("pos"));
			String subject = rs.getString("subject");
			String thread = rs.getString("thread");
			int depth = rs.getInt("depth");
			int rid = rs.getInt("rid");
			String html = rs.getString("html");

			String no_link = "<input type=checkbox name=checkbox value=" + no + ">";

			if ("y".equals(enablepreview)){
				content = content;
				if (content.length() > 101)	content = content.substring(0, 100) + "...";
				content = com.anbtech.text.StringProcess.repWord(content, "\"", "'");
			}

			if (subject.length() > l_maxsubjectlen+1) subject = subject.substring(0, l_maxsubjectlen) + "...";
			
			String subject_link = "";
			if(html.equals("d")){ // 사내,외 공문일 경우
				subject_link = "<A HREF=\"javascript:view_doc('" + content + "','" + no + "');\">";
			}else{	
				subject_link = "<A HREF='AnBBoard?tablename="+tablename+"&mode=view";
				subject_link += "&boardpage="+boardpage+"&searchword="+searchword;
				subject_link += "&searchscope="+searchscope+"&category="+category+"&no="+no+"' ";
				subject_link += "onMouseOver=\"window.status='View article #"+pos+"';return true;\" ";
				subject_link += "onMouseOut=\"window.status='';return true;\" TITLE=\""+content+"\">";
			}

			boolean here = false;
			int k=0;
			while(k < no_list.size()){
				if(Integer.toString(no).equals((String)no_list.get(k))) here = true;
				k++;
			}

			// pos에 대한처리 here일경우 here가 아닐경우 notice이면 icon으로 새로운글이면 굵은글씨
			if(here){
				pos = "<img src=../board/images/arrow.gif border=0>";
			}else{
				if("0".equals(thread)) pos = "<img src=../board/images/icon_notice.gif border=0>";
				if(now_time.equals(w_time)) pos = "<b>"+pos+"</b>";
			}

			// subject에 대한처리 
			// 답변일경우 새로운글 옛날글로 나눈다..
			// 답변이 아닐경우 공지사항과 새로운글 옛날글로 나눈다.

			if (depth>0){
				if (now_time.equals(w_time)) subject = "<img src=../board/images/reply_new_head.gif border=0> " + subject_link + " Re: " + subject + "</A>&nbsp;<img src=../board/images/new.gif>";
				else subject = "<img src=../board/images/reply_head.gif border=0> " + subject_link + " Re: " + subject + "</A>";
				
				for (int j=0; j<depth;j++)
					subject = "&nbsp;&nbsp;&nbsp;" + subject;
			}else{
				if(now_time.equals(w_time)){
					if("0".equals(thread)) subject = "<img src=../board/images/notice_head.gif border=0> " + subject_link + subject + "</A>&nbsp;<img src=../board/images/new.gif>";
					else subject = "<img src=../board/images/new_head.gif border=0> " + subject_link + subject + "</A>&nbsp;<img src=../board/images/new.gif>";
				}else{
					if("0".equals(thread)) subject = "<img src=../board/images/notice_head.gif border=0> " + subject_link + subject + "</A>";
					else subject = "<img src=../board/images/old_head.gif border=0> " + subject_link + subject + "</A>";
				}
			}

			// 읽은수가 많을 때 cool처리한다.
			if (rid >= enablechkcool && enablechkcool > 0)
				subject += "&nbsp;<font color=red size=1 face=Verdana><b>★cool</b></font>";

			if(cid > 0 && "y".equals(enablecomment)) subject += "&nbsp;<FONT SIZE=1><b>["+cid+"]</b></font>";

			Iterator file_iter = getFile_list(tablename, Integer.toString(no)).iterator();

			int j = 1;
			String filelink = "&nbsp;";
			while(file_iter.hasNext()){
				Table file = (Table)file_iter.next();
				filelink += "<a href='AnBBoard?tablename="+tablename+"&mode=download&no="+no+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFilename()+" ("+file.getFilesize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFilename(), file.getFiletype()) + ".gif' border=0></a>";
				j++;
			}

			table = new Table();
			table.setNo(no_link);
			table.setPos(pos);
			table.setVid(rs.getInt("vid"));
			table.setRid(rid);
			table.setWriter(rs.getString("writer"));
			table.setEmail(rs.getString("email"));
			table.setHomepage(rs.getString("homepage"));
			table.setSubject(subject);
			table.setFilelink(filelink);
			table.setW_time(w_time);

			table_list.add(table);

			recNum--;

		}
		stmt.close();
		rs.close();
		return table_list;
	}

	public ArrayList getFile_list(String tablename, String no) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		int total = 0;
		ArrayList file_list = new ArrayList();

		String query = "SELECT filename,filetype,filesize,did FROM "+tablename+" WHERE no = "+no;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()){ 

			Iterator filename_iter = com.anbtech.util.Token.getTokenList(rs.getString("filename")).iterator();
			Iterator filetype_iter = com.anbtech.util.Token.getTokenList(rs.getString("filetype")).iterator();
			Iterator filesize_iter = com.anbtech.util.Token.getTokenList(rs.getString("filesize")).iterator();
			Iterator did_iter = com.anbtech.util.Token.getTokenList(rs.getString("did")).iterator();

			while (filename_iter.hasNext()&&filetype_iter.hasNext()&&filesize_iter.hasNext()){
				Table file = new Table();
				file.setFilename((String)filename_iter.next());
				file.setFiletype((String)filetype_iter.next());
				file.setFilesize((String)filesize_iter.next());
				file.setDid((String)did_iter.next());
				file_list.add(file);
			}
		}
		stmt.close();
		rs.close();
		return file_list;
	}

	public void updTable(String tablename, String set, String where) throws Exception{
		Statement stmt = null;
		String query = "UPDATE " + tablename + set + where;
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	public void updTable(String tablename, String no, String writer, String email, String homepage,String subject, String content, String html, 
		String email_forward, String ip_addr, String category) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		Table table = new Table();
		table = getTable(tablename, no);
		String thread = table.getThread();
		String depth = table.getDepth();
		String pos = table.getPos();
		int select_pos = 0;

		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd a hh:mm");
		String w_time = vans.format(now);

		boolean reply_none = true;
		// 같은 thread에 pos가 자기보다작은 것중에서 가장 큰것을 구해온다
		// 만약에 있다면 그 pos에 해당하는 depth를 가져온다.
		// 가져온 depth 가 지우려는파일의 depth보다 크다면 지울수 없다.
		query = "SELECT MAX(pos) as pos FROM "+tablename+" WHERE pos<"+pos+" and thread="+thread;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()) select_pos = rs.getInt("pos");
		rs.close();
		if(select_pos != 0){
			query = "SELECT depth FROM "+tablename+" WHERE pos="+select_pos+" and thread="+thread;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			if(rs.getInt("depth") > Integer.parseInt(depth)) reply_none = false;
			rs.close();
		}
		if(!reply_none && !category.equals(table.getCategory())) throw new Exception("<script> alert('답변이 있으면 카테고리 변경이 불가능합니다.');	history.go(-1); </script>");
		else if(Integer.parseInt(depth)>0 && !category.equals(table.getCategory())) throw new Exception("<script> alert('답변은 카테고리 변경이 불가능합니다.');	history.go(-1); </script>");
		else{
			query = "UPDATE " + tablename + " SET writer=?,email=?,homepage=?,ip_addr=?,subject=?,content=?,html=?,email_forward=?,category=?,u_time=? WHERE no="+no;
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1,writer);
			pstmt.setString(2,email);
			pstmt.setString(3,homepage);
			pstmt.setString(4,ip_addr);
			pstmt.setString(5,subject);
			pstmt.setString(6,content);
			pstmt.setString(7,html);
			pstmt.setString(8,email_forward);
			pstmt.setString(9,category);
			pstmt.setString(10,w_time);
			
			pstmt.executeUpdate();
			pstmt.close();
		}
	}

	public void delTable(String tablename, String no, String filepath) throws Exception{

		Statement stmt = null;
		String query = null;
		ResultSet rs = null;

		// 답변글이 있는지 확인
		Table table = new Table();
		table = getTable(tablename, no);
		String thread = table.getThread();
		String depth = table.getDepth();
		String pos = table.getPos();
		int select_pos = 0;

		boolean reply_none = true;

		// 같은 thread에 pos가 자기보다작은 것중에서 가장 큰것을 구해온다
		// 만약에 있다면 그 pos에 해당하는 depth를 가져온다.
		// 가져온 depth 가 지우려는파일의 depth보다 크다면 지울수 없다.
		query = "SELECT MAX(pos) as pos FROM "+tablename+" WHERE pos<"+pos+" and thread="+thread;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		if(rs.next()) select_pos = rs.getInt("pos");
		rs.close();
		if(select_pos != 0){
			query = "SELECT depth FROM "+tablename+" WHERE pos="+select_pos+" and thread="+thread;
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			if(rs.getInt("depth") > Integer.parseInt(depth)) reply_none = false;
			rs.close();
		}

		if (reply_none){

			// 파일이 있으면 삭제
			for(int i=0; i < 10; i++){
				java.io.File f = new java.io.File(filepath + no + "_"+(i+1) + ".bin");
				if (f.exists()) f.delete();
			}
		
			// 코멘트를 삭제한다.
			query = "DELETE FROM " + tablename + "_cmt WHERE ono=" + no;
			stmt = con.createStatement();
			stmt.executeUpdate(query);
			
			// 글 삭제
			query = "DELETE FROM " + tablename + " WHERE no=" + no;
			stmt = con.createStatement();
			stmt.executeUpdate(query);

			stmt.close();
		}
		else throw new Exception("<script> alert('답변이 있어서 삭제를 할수 없습니다.');	history.go(-1); </script>");
	}
}		