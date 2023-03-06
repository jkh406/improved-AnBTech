package com.anbtech.board.business;

import com.anbtech.board.entity.*;
import com.anbtech.board.db.*;
import com.oreilly.servlet.MultipartRequest;

import java.sql.*;
import java.util.*;
import java.io.*;

public class TableBO{

	private Connection con;

	public TableBO(Connection con){
		this.con = con;
	}
	public void sendMail(String tablename, String no,String writer, String subject, String content, String email) throws Exception{

		TableDAO tableDAO = new TableDAO(con);
		Table table = tableDAO.getTable(tablename,no);
		if ("y".equals(table.getEmail_forward())){
			StringBuffer sb = new StringBuffer();
			sb.append("<HTML>");
			sb.append("<HEAD>");
			sb.append("<TITLE>JSPMaster.com</TITLE>");
			sb.append("<STYLE TYPE='text/css'>");
			sb.append("<!--");
			sb.append(".bt {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:black}");
			sb.append(".del {border:solid 1 black;font-family:Verdana,Tahoma;font-size:8pt;color:white;background-color:#404040}");
			sb.append("A:link    {color:black;text-decoration:nBoard_env;}");
			sb.append("A:visited {color:black;text-decoration:nBoard_env;}");
			sb.append("A:active  {color:#007BEA;text-decoration:nBoard_env;}");
			sb.append("A:hover  {color:#0A99D4;text-decoration:underline}");
			sb.append(".sel {background-color:black;font-family:굴림체;color:white}");
			sb.append(".bd {border:solid 1 black}");
			sb.append(".ver8 {font-family:Verdana,Arial;font-size:8pt}");
			sb.append("BODY,TD,SELECT,input,DIV,form,TEXTAREA,center,option,pre,br, {font-size:10pt;}");
			sb.append("BODY { margin:0 0 0 0 }");
			sb.append("-->");
			sb.append("</STYLE>");
			sb.append("</HEAD>");
			sb.append("<BODY BGCOLOR=#ffffff>");
			sb.append("<BR><BR>");
			sb.append("<CENTER>");
			sb.append("<!----- // Head / Main // ----->");
			sb.append("<TABLE WIDTH='95%' BORDER='0' BGCOLOR='white'");
			sb.append("CELLPADDING='0' CELLSPACING='0'>");
			sb.append("<tr>");
			sb.append("<td width='100%' bgcolor=cecece>&nbsp;</td>");
			sb.append("</tr>");
			sb.append("<TR BGCOLOR='#eeeeee'>");
			sb.append("<TD NOWRAP><TABLE WIDTH=100% CELLPADDING=6 CELLSPACING=0 BORDER=0>");
			sb.append("<TR><TD><FONT COLOR='black'><B>"+writer+"</B><BR>");
			sb.append("<CENTER><B>"+subject+"</B></CENTER><BR><BR>");
			sb.append(com.anbtech.text.StringProcess.repWord(content,"\n","<br>"));
			sb.append("<BR><BR>");
			sb.append("</TD></TR></TABLE></TD></TR>");
			sb.append("</TABLE>");
			sb.append("<TABLE WIDTH=95% BORDER=0 BGCOLOR=white>");
			sb.append("<TR><TD ALIGN=RIGHT><A HREF='" + com.anbtech.board.db.BoardConf.getConf("serverURL") + com.anbtech.board.db.BoardConf.getConf("boardpath") + "/list.jsp?tablename=" + tablename + "'>게시판으로</A>");
			sb.append("</TD></TR></TABLE>");
			sb.append("</CENTER>");
			sb.append("<BR><BR>");
			sb.append("</BODY>");
			sb.append("</HTML>");

			com.anbtech.net.Mail m = new com.anbtech.net.Mail();
			m.sendMail(com.anbtech.board.db.BoardConf.getConf("smtpServer"),email,writer, table.getEmail(), "[답변글 알림] Re: " +subject, sb.toString());
		}
	}

	public Table getWrite_form(String tablename, String no, String mode, String category, String s[]) throws Exception{

		String thread="", depth="", pos="",writer="",email="";
		String homepage="",subject="",content="",html="",email_forward="";
		String chk_html="",chk_email_forward="checked";

/*		// 수정이 아닐경우에만 쿠키에서 값을 가져온다.
		if (!"modify".equals(mode)){
			writer = s[0];
			email = s[1];
			homepage = s[2];
		}
*/		
		if (writer == null)	writer = "";
		if (email == null)	email = "";
		if (homepage == null)	homepage="http://";
		
		if ("reply".equals(mode) || "modify".equals(mode) && no != null){


			// 답변일 경우 글의 내용을 가져온다.
			Table table = new Table();
			TableDAO tableDAO = new TableDAO(con);
			table = tableDAO.getTable(tablename, no);

			thread = table.getThread();
			depth = table.getDepth();
			pos = table.getPos();
			subject = table.getSubject();
			content = table.getContent();
			html =  table.getHtml();
			category = table.getCategory();

			if ("modify".equals(mode)){
				writer = table.getWriter();
				email = table.getEmail();
				homepage = table.getHomepage();
				email_forward = table.getEmail_forward();			
				
				if (email == null)	email = "";
				if (homepage == null)	homepage = "";
			}

			if (subject == null)	subject = "";
			if (content == null)	content = "";
			
			if ("y".equals(html))	chk_html = "checked";
			if ("n".equals(email_forward))	chk_email_forward = "";
			
			if ("reply".equals(mode)){
				content = table.getWriter() + "님이 " + table.getW_time()+ "에 쓰신글\n" + ">" + com.anbtech.text.StringProcess.repWord(content, "\n", ">");
			}
		}
		// \"을 &quot;로 바꾸어서 input창에 자료 손실되지 않도록 한다.
		writer = com.anbtech.text.StringProcess.repWord(writer, "\"", "&quot;");
		subject = com.anbtech.text.StringProcess.repWord(subject, "\"", "&quot;");

		//table에 넣을 것들..
		Table table = new Table();
		table.setNo(no);
		table.setThread(thread);
		table.setDepth(depth);
		table.setPos(pos);
		table.setWriter(writer);
		table.setCategory(category);
		table.setEmail(email);
		table.setHomepage(homepage);
		table.setSubject(subject);
		table.setContent(content);
		table.setHtml(chk_html);
		table.setEmail_forward(chk_email_forward);

		return table;
	}

	public Table getFile_frommulti(MultipartRequest multi, String no, String filepath, ArrayList file_list) throws Exception{

		Iterator file_iter = file_list.iterator();
		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1,j = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();

			Table file = new Table();
			if(file_iter.hasNext()) file = (Table)file_iter.next();

			String deletefile = multi.getParameter("deletefile"+i);
			String name = "attachfile"+i;

			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+j+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
				did = did + "0" + "|";
				j++;
			}else 	if(deletefile != null){
				File myDir = new File(filepath);
				File delFile = new File(myDir, no+"_"+i+".bin");
				if(delFile.exists()) delFile.delete();
			}else 	if(file.getFilename() != null){
				File myDir = new File(filepath);
				File chFile = new File(myDir, no+"_"+i+".bin");
				File myFile = new File(myDir, no+"_"+j+".bin");
				chFile.renameTo(myFile);

				filename = filename + file.getFilename() + "|";
				filetype = filetype + file.getFiletype() + "|";
				filesize = filesize +file.getFilesize() + "|";
				did = did + file.getDid() + "|";
				j++;
			}
			i++;
		}
		Table file = new Table();
		file.setFilename(filename);
		file.setFiletype(filetype);
		file.setFilesize(filesize);
		file.setDid(did);

		return file;
	}

	public Table getFile_frommulti(MultipartRequest multi, String no, String filepath) throws Exception{

		String filename = "";
		String filetype = "";
		String filesize = "";
		String did = "";

		int i = 1;
		java.util.Enumeration files = multi.getFileNames();
		while (files.hasMoreElements()) {
			files.nextElement();
			String name = "attachfile"+i;
			//filename과 type
			String fname = multi.getFilesystemName(name);
			if(fname != null){
				String ftype = multi.getContentType(name);

				//file의 사이즈를 재기위한것
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir, no+"_"+i+".bin");
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);

				filename = filename + fname + "|";
				filetype = filetype + ftype + "|";
				filesize = filesize +fsize + "|";
				did = did + "0" + "|";
			}
			i++;
		}
		Table file = new Table();
		file.setFilename(filename);
		file.setFiletype(filetype);
		file.setFilesize(filesize);
		file.setDid(did);

		return file;
	}

	public ArrayList getTable_view(String tablename, ArrayList no_list, String mapping) throws Exception{

		Iterator no_iter = no_list.iterator();
		ArrayList table_view = new ArrayList();

		while(no_iter.hasNext()){

			String no = (String)no_iter.next();

			TableDAO tableDAO = new TableDAO(con);
			Table table = new Table();
			table = tableDAO.getTable(tablename, no);

			//content빼서 html y/n인지 확인한다.
			String content = table.getContent();
			if ("n".equals(table.getHtml())){
				content = com.anbtech.text.StringProcess.repWord(content, "<", "&lt;");
				if(content.length() < 2000){//content가 2000자 이상일 경우 줄내림/빈칸/url처리 안 한다.
					content = com.anbtech.text.StringProcess.repWord(content, "\n", "<br>");
					if(content.length() < 1000){//1000자 이상일 경우 빈칸/url처리 안 한다.
						content = com.anbtech.text.StringProcess.repWord(content, " ", "&nbsp;");
						content = com.anbtech.text.StringProcess.repUrl(content); // url에 링크처리
					}
				}// 나중에 몇자이상일경우 처리하는거 DB에서 빼와야겠다.. 관리자가 관리할수 있도록 
			}
			
			//u_time있으면 보여주고 없으면 빈값으로 보낸다.
			
			String u_time = table.getU_time();
			/*
			if(u_time != null) "Last Modifid Time : " + u_time;
			else u_time = "";
			*/

			//filelink하구 filepreview만든다.
			Iterator file_iter = tableDAO.getFile_list(tablename, no).iterator();
			int j = 1;
			String filelink = "&nbsp;";
			String filepreview = "<TABLE CELLPADDING=1 CELLPADDING=0>";
			while(file_iter.hasNext()){
				Table file = (Table)file_iter.next();
				filelink += "<a href='AnBBoard?tablename="+tablename+"&mode=download&no="+no+"_"+j+"' ";
				filelink += "onMouseOver=\"window.status='Download "+file.getFilename()+" ("+file.getFilesize()+" bytes)';return true;\" ";
				filelink += "onMouseOut=\"window.status='';return true;\" >";
				filelink += "<img src='../board/mimetype/" + com.anbtech.util.Module.getMIME(file.getFilename(), file.getFiletype()) + ".gif' border=0> "+file.getFilename()+"</a>";
				filelink += " - "+file.getFilesize()+" bytes  (downloaded "+file.getDid()+" times) <br>&nbsp;";
				if (file.getFiletype().indexOf("mage")>0){
					filepreview = filepreview + "<TR><TD><font size=1>"+file.getFilename()+"</font><br><img src='AnBBoard?tablename="+tablename+"&mode=download&no="+no+"_"+j+"' border=1></TD></TR>";
				}
				j++;
			}
			filepreview = filepreview + "</TABLE>";

			//테이블을 바뀐것만 새로 저장해서 보낸다.
			table.setContent(content);
			table.setW_time(table.getW_time());
			table.setU_time(u_time);
			table.setFilelink(filelink);
			table.setFilepreview(filepreview);
			table_view.add(table);
		}
		return table_view;
	}

	public Table getFile_fordown(String tablename, String no) throws Exception{

		String file_no = no.substring(no.lastIndexOf("_")+1, no.length());
		String fileno = no.substring(0, no.lastIndexOf("_"));

		TableDAO tableDAO = new TableDAO(con);
		Iterator file_iter = tableDAO.getFile_list(tablename, fileno).iterator();

		String filename="",filetype="",filesize="",did="";
		int i = 1;
		while (file_iter.hasNext()){
			Table file = (Table)file_iter.next();
			if(i == Integer.parseInt(file_no)){
				filename = file.getFilename();
				filetype = file.getFiletype();
				filesize = file.getFilesize();
				did = did + Integer.toString(Integer.parseInt(file.getDid())+1) + "|";
			}else{
				did = did + file.getDid() + "|";
			}
			i++;
		}
		Table file = new Table();
		file.setFilename(filename);
		file.setFiletype(filetype);
		file.setFilesize(filesize);
		file.setDid(did);

		return file;
	}

	public void updFile(String tablename, String no, String filename, String filetype, String filesize, String did) throws Exception{
		String set = " SET filename='"+filename+"',filetype='"+filetype+"',filesize='"+filesize+"',did='"+did+"'";
		String where = " WHERE no="+no;

		TableDAO tableDAO = new TableDAO(con);
		tableDAO.updTable(tablename, set, where);
	}

	public void updPos(String tablename, String pos) throws Exception{

		TableDAO tableDAO = new TableDAO(con);
		tableDAO.updTable(tablename, " SET pos=pos+1"," WHERE  pos>=" + pos);
	}
	public void updRid(String tablename, String no, String ip_addr) throws Exception{

		Table table = new Table();
		TableDAO tableDAO = new TableDAO(con);
		table = tableDAO.getTable(tablename, no);
		if(!ip_addr.equals(table.getIp_addr()))	tableDAO.updTable(tablename, " SET rid=rid+1"," WHERE no=" + no);
	}
	public void updVote(String tablename, String no, String ip_addr) throws Exception{

		Table table = new Table();
		TableDAO tableDAO = new TableDAO(con);
		table = tableDAO.getTable(tablename, no);
		if(!ip_addr.equals(table.getIp_addr()))	tableDAO.updTable(tablename, " SET vid=vid+1"," WHERE no=" + no);
	}
	public void updDid(String tablename, String no, String did, String ip_addr) throws Exception{

		Table table = new Table();
		TableDAO tableDAO = new TableDAO(con);
		table = tableDAO.getTable(tablename, no);
		if(!ip_addr.equals(table.getIp_addr()))	tableDAO.updTable(tablename, " SET did='" + did +"'"," WHERE no=" + no);
	}
	public void chkWriter(String tablename, MultipartRequest multi, String no, String password) throws Exception{

		Table table = new Table();
		TableDAO tableDAO = new TableDAO(con);
		table = tableDAO.getTable(tablename, no);
/* 글 수정 시 비밀번호 체크하는 것을 빼기 위해 수정함. 2003년 04월 25일 박동렬
		if (!password.equals(table.getPasswd())){
			java.util.Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String)files.nextElement();
				String fname = multi.getFilesystemName(name);
				if(fname != null){
					File upFile = multi.getFile(name);
					if(upFile.exists()) upFile.delete();
				}
			}
			throw new Exception("<script> alert('비밀번호가 정확하지 않습니다.');	history.go(-1); </script>");
		}
*/
	}

	//선택 항목 모두 삭제하기
	public void deleteAllChecked(String tablename,String no,String boardpath) throws Exception{
		TableDAO tableDAO = new TableDAO(con);

		ArrayList no_list = com.anbtech.util.Token.getTokenList(no);
		int i = 0;
		while(i < no_list.size()){
			String del_no = (String)no_list.get(i);
			tableDAO.delTable(tablename,del_no,boardpath);
			i++;
		}
	}
}