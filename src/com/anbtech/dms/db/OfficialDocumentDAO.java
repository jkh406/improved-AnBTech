package com.anbtech.dms.db;
import com.anbtech.dms.entity.*;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;

public class OfficialDocumentDAO
{
	private Connection con;
	private FileWriteString text;
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리하기

	private String query = "";
	private int total_page = 0;
	private int current_page = 0;

	private String id;					//공통:관리번호
	private String bon_path;			//확장path
	private String bon_file;			//본문저장 파일명
	private String sname;				//공통:파일저장명	(확장자 .bin이없음)	

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public OfficialDocumentDAO(Connection con) 
	{
		this.con = con;
	}

	//*******************************************************************
	//	총 수량 파악하기
	//*******************************************************************/
	public int getTotalCount(String login_id,String sItem,String sWord) throws Exception
	{
		//변수 초기화
		if(login_id == null) login_id = "";	//사번
		Statement stmt = null;
		ResultSet rs = null;
		
		//공통 항목
		stmt = con.createStatement();

		//부서관리코드 찾기
		String ac_id = searchAcId(login_id);

		query = "SELECT COUNT(*) FROM OfficialDocument where ac_id='"+ac_id+"'";
		query += " and ("+sItem+" like '%"+sWord+"%')"; 
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
	// DB OfficialDocument에서 해당LIST QUERY하기 (전체 LIST읽기)
	//*******************************************************************/	
	public ArrayList getDoc_List (String login_id,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		int total_cnt = 0;				//총 수량
		int startRow = 0;				//시작점
		int endRow = 0;					//마지막점

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//부서관리코드 찾기
		String ac_id = searchAcId(login_id);

		//총갯수 구하기
		total_cnt = getTotalCount(login_id,sItem,sWord);
			
		//query문장 만들기
		query = "SELECT * FROM OfficialDocument where ac_id='"+ac_id+"'";	
		query += " and ("+sItem+" like '%"+sWord+"%') order by doc_id asc"; 
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
				table = new OfficialDocumentTable();
				
				String pid = rs.getString("id");
				table.setId(pid);									//관리번호	
				String user_id = rs.getString("user_id");
				table.setUserId(user_id);							//작성자					
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));							
				table.setAcCode(rs.getString("ac_code"));							
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setSlogan(rs.getString("slogan"));	
				table.setTitleName(rs.getString("title_name"));	
				table.setInDate(rs.getString("in_date"));	
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));
				table.setReference(rs.getString("reference"));
				table.setSending(rs.getString("sending"));
				
				String subj = rs.getString("subject");  if(subj == null) subj = "";
				
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				table.setFirmName(rs.getString("firm_name"));	
				table.setRepresentative(rs.getString("representative"));	
				table.setEtc(rs.getString("etc"));	

				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				String flg = rs.getString("flag");	//if(flg == null) flg = "EN";
				table.setFlag(flg);									//결재진행상태
				String app_id = rs.getString("app_id"); if(app_id == null) app_id = "";
				table.setAppId(app_id);								//전자결재 승인 관리코드
				table.setAppDate(rs.getString("app_date"));	

				// 제목에 href 분기(승인후 결재선내용을 보여주기위해) 
				String subLink = "";
				//승인전 [본인이면 승인전 볼수있으나 아니면 제목만]
				if(login_id.equals(user_id)) subLink = "<a href=\"javascript:contentReview('"+pid+"');\">"+subj+"</a>";
				else subLink = subj;
				//승인후 [같은 부서원이면 볼수 있다]
				if(app_id.length() != 0) {
					subLink = "<a href=\"javascript:contentAppview('"+pid+"','"+app_id+"');\">"+subj+"</a>";	 
				}
				table.setSubject(subLink);							//제목 

				//수정 or 삭제가능 표시 [login_id가 작성자인 경우만 가능]
				String subMod="",subDel="";
				if(login_id.equals(user_id) && ((flg == null) || flg.equals("EN") || flg.equals("EC"))) {
					subMod = "<a href=\"javascript:contentModify('"+pid+"');\"><img src='../ods/images/lt_modify.gif' border='0' align='absmiddle'></a>";
					subDel = "<a href=\"javascript:contentDelete('"+pid+"');\"><img src='../ods/images/lt_del.gif' border='0' align='absmiddle'></a>";
				}
				table.setModify(subMod);
				table.setDelete(subDel);
				
				table.setModuleName(rs.getString("module_name"));	
				table.setMail(rs.getString("mail"));	
				table.setMailAdd(rs.getString("mail_add"));	

				table_list.add(table);
				show_cnt++;
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	// DB OfficialDocument에서 해당ID QUERY하기 (개별 읽기)
	//*******************************************************************/	
	public ArrayList getDoc_Read (String id) throws Exception
	{
		//변수 초기화
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	

		stmt = con.createStatement();
		OfficialDocumentTable table = null;
		ArrayList table_list = new ArrayList();

		//query문장 만들기
		query = "SELECT * FROM OfficialDocument where id='"+id+"'";	
		rs = stmt.executeQuery(query);

		//데이터 담기
		while(rs.next()) { 
				table = new OfficialDocumentTable();
				
				table.setId(rs.getString("id"));							
				table.setUserId(rs.getString("user_id"));							
				table.setUserName(rs.getString("user_name"));							
				table.setUserRank(rs.getString("user_rank"));							
				table.setAcId(rs.getInt("ac_id"));							
				table.setAcCode(rs.getString("ac_code"));	
				table.setCode(rs.getString("code"));	
				table.setAcName(rs.getString("ac_name"));	
				
				table.setSerialNo(rs.getString("serial_no"));	
				table.setClassNo(rs.getString("class_no"));	
				table.setDocId(rs.getString("doc_id"));	
				table.setSlogan(rs.getString("slogan"));	
				table.setTitleName(rs.getString("title_name"));	
				table.setInDate(rs.getString("in_date"));	
				table.setEnforceDate(rs.getString("enforce_date"));	
				table.setReceive(rs.getString("receive"));	
				table.setReference(rs.getString("reference"));
				table.setSending(rs.getString("sending"));	
				table.setSubject(rs.getString("subject"));	
				table.setBonPath(rs.getString("bon_path"));	
				table.setBonFile(rs.getString("bon_file"));	
				table.setFirmName(rs.getString("firm_name"));	
				table.setRepresentative(rs.getString("representative"));
				table.setEtc(rs.getString("etc"));	

				table.setDeleteDate(rs.getString("delete_date"));	
				table.setFilePath(rs.getString("file_path"));	
				table.setFname(rs.getString("fname"));	
				table.setSname(rs.getString("sname"));	
				table.setFtype(rs.getString("ftype"));	
				table.setFsize(rs.getString("fsize"));	

				table.setFlag(rs.getString("flag"));	
				table.setAppId(rs.getString("app_id"));	
				table.setAppDate(rs.getString("app_date"));	
				table.setModuleName(rs.getString("module_name"));	
				table.setMail(rs.getString("mail"));	
				table.setMailAdd(rs.getString("mail_add"));	

				table_list.add(table);
		}

		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	해당사번 부서관리 코드 리턴하기
	//*******************************************************************/	
	private String searchAcId (String login_id) throws Exception
	{
		String rtn = "";		//return data
		String query = "";
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//부서 관리코드 알아보기
		query  = "SELECT b.ac_id FROM user_table a,class_table b ";
		query += "where a.id ='"+login_id+"' and a.ac_id = b.ac_id";
		rs = stmt.executeQuery(query);
		if(rs.next())	rtn = rs.getString("ac_id");
		
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();

		return rtn;
	}

	/*******************************************************************
	* 공지공문 사항 내용 저장하기 
	*******************************************************************/
	public void inputODTTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,String slogan,String title_name,
		String in_date,String enforce_date,String receive,String reference,String sending,String subject,
		String firm_name,String representative,String module_name,String mail,String mail_add,
		String content,String upload_path) throws Exception
	{
		//' 없애기
		slogan = str.repWord(slogan,"'","`");				//표어			
		title_name = str.repWord(title_name,"'","`");		//머리글
		firm_name = str.repWord(firm_name,"'","`");			//꼬리글
		subject = str.repWord(subject,"'","`");				//제목		
		content = str.repWord(content,"'","`");				//내용
		

		//본문저장디렉토리및 파일명
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO OfficialDocument(id,user_id,user_name,user_rank,";
			incommon += "ac_id,ac_code,code,ac_name,class_no,slogan,title_name,in_date,enforce_date,receive,reference,sending,";
			incommon += "subject,bon_path,bon_file,firm_name,representative,module_name,mail,mail_add) values('";
		
		String input = incommon+id+"','"+user_id+"','"+user_name+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+code+"','"+ac_name+"','"+class_no+"','"+slogan+"','"+title_name+"','";
			input += in_date+"','"+enforce_date+"','"+receive+"','"+reference+"','"+sending+"','"+subject+"','";
			input += doc_pat+"','"+id+"','"+firm_name+"','"+representative+"','"+module_name+"','"+mail+"','"+mail_add+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//저장1. 본문파일로 저장하기
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,id,content);
		}
	}

	/*******************************************************************
	* 공지공문 사항 내용 수정하기 
	*******************************************************************/
	public void updateODTTable(String id,String user_id,String user_name,String user_rank,
		String ac_id,String ac_code,String code,String ac_name,String class_no,String slogan,String title_name,
		String in_date,String enforce_date,String receive,String reference,String sending,String subject,
		String firm_name,String representative,String module_name,String mail,String mail_add,
		String content,String upload_path) throws Exception
	{
		//' 없애기
		slogan = str.repWord(slogan,"'","`");				//표어			
		title_name = str.repWord(title_name,"'","`");		//머리글
		firm_name = str.repWord(firm_name,"'","`");			//꼬리글
		subject = str.repWord(subject,"'","`");				//제목		
		content = str.repWord(content,"'","`");				//내용
		
		//본문저장디렉토리및 파일명
		String root_path = upload_path;
		String doc_pat = "/ods/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String update = "UPDATE OfficialDocument set user_id='"+user_id+"',user_name='"+user_name;
			update += "',user_rank='"+user_rank+"',"+"ac_id='"+ac_id+"',ac_code='"+ac_code+"',code='"+code;
			update += "',ac_name='"+ac_name+"',class_no='"+class_no+"',slogan='"+slogan;
			update += "',title_name='"+title_name+"',in_date='"+in_date+"',enforce_date='"+enforce_date;
			update += "',receive='"+receive+"',reference='"+reference+"',sending='"+sending;
			update += "',subject='"+subject+"',bon_path='"+doc_pat+"',bon_file='"+id;
			update += "',firm_name='"+firm_name+"',representative='"+representative;
			update += "',module_name='"+module_name+"',mail='"+mail+"',mail_add='"+mail_add;
			update += "' where id='"+id+"'";
		//System.out.println("update : " + update );
		int er = stmt.executeUpdate(update);
		
		stmt.close();
		//저장1. 본문파일로 저장하기
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,id,content);
		}
	}

	/*******************************************************************
	* 본문을 파일로 저장하기
	* root_path : root Path, doc_pat : 확장 path, content : 본문내용
	 *******************************************************************/
	private void setTableBonFile(String root_path,String doc_pat,String fileName,String content)
	{
		text = new com.anbtech.file.FileWriteString();
		String FullPathName = root_path + doc_pat + "/bonmun";
		text.WriteHanguel(FullPathName,fileName,content);
	}

	/*******************************************************************
	* 첨부파일 저장하기 (신규로 처음 첨부할때)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String id,String filepath) throws Exception
	{
		String filename = "";		//원래이름 파일명
		String savename = "";		//저장 파일명
		String filetype = "";		//원래이름 파일 확장자명
		String filesize = "";		//원래이름 파일사이즈

		int i = 1;					//첨부저장 확장자
		int atcnt = 0;				//첨부파일 수량
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//해당파일 읽기
			String name = "attachfile"+i;		//upload한 input file type name parameter
			String fname = multi.getFilesystemName(name);	//upload한 파일명
			if(fname != null) {
				String ftype = multi.getContentType(name);	//upload한 파일type
				//file size구하기
				File upFile = multi.getFile(name);
				String fsize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				filename += fname + " |";
				savename += id + "_" + i + " |";
				filetype += ftype + " |";
				filesize += fsize + " |";
				atcnt++;
			}
			i++;
		}//while

		//Table에 저장하기
		if(i > 1) {
			setAddFileUpdate(id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* 첨부파일 저장하기 (수정하여 첨부할때)
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String id,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt) throws Exception
	{
		String filename = "";		//원래이름 파일명
		String savename = "";		//저장 파일명
		String filetype = "";		//원래이름 파일 확장자명
		String filesize = "";		//원래이름 파일사이즈
		int att_cnt = Integer.parseInt(attache_cnt);	//첨부파일 최대수량 미만
		String newdata = "";

		//신규로 첨부한 파일
		int i = 1;		//첨부파일확장자
		int n = 0;		//저장배열을 위해
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();				//해당파일 읽기
			String name = "";
			String uname = "";
			for(int a=0; a<att_cnt; a++) {			//첨부파일 att_cnt 번째까지 읽고 빠져나가
				name = "attachfile"+i;
				uname = multi.getFilesystemName(name);
				if(uname != null) break; else { i++; n++; }
			}
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload한 파일type
				//file size구하기
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				newdata += uname + "|";						//첨부파일명
				newdata += id + "_" + i + "|";				//첨부파일 저장명
				newdata += utype + "|";						//첨부파일 type
				newdata += usize + ";";						//첨부파일 크기
			}
			i++;
			n++;
		}//while

		//배열만들기
		java.util.StringTokenizer fna = new StringTokenizer(fname,"|");
		int fn = fna.countTokens();

		int an = fn + n;
		String[][] nfile = new String[an][4];
		for(int j=0; j<an; j++) for(int k=0; k<4; k++) nfile[j][k] = "";

		//첨부파일 배열에 담기
		int ai = 0;		//배열번호
		java.util.StringTokenizer ndata = new StringTokenizer(newdata,";");
		while(ndata.hasMoreTokens()) {
			String nnd = ndata.nextToken();		//1라인 읽기
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				nfile[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		//기존에 있던 첨부파일 배열에 붙이기
		java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			//첨부파일명
		int hi = ai;
		while(o_fname.hasMoreTokens()) {
			String read = o_fname.nextToken();
			if(read.length() != 1) nfile[hi][0] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			//첨부파일 저장명
		hi = ai;
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			if(read.length() != 1) nfile[hi][1] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			//첨부파일 타입
		hi = ai;
		while(o_ftype.hasMoreTokens()) {
			String read = o_ftype.nextToken();
			if(read.length() != 1) nfile[hi][2] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			//첨부파일 크기
		hi = ai;
		while(o_fsize.hasMoreTokens()) {
			String read = o_fsize.nextToken();
			if(read.length() != 1) nfile[hi][3] = read.substring(0,read.length()-1);
			hi++;
		}

		//저장할 변수로 나누기
		int atcnt = 0;				//첨부파일 수량
		for(int p=0; p<an; p++) {
			if(nfile[p][0].length() != 0) {
				filename += nfile[p][0] + " |";
				savename += nfile[p][1] + " |";
				filetype += nfile[p][2] + " |";
				filesize += nfile[p][3] + " |";
				atcnt++;
			}
		}

		//Table에 저장하기
		if(an > 0) {
			setAddFileUpdate(id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* 첨부파일 저장한후 Table에 update하기
	 *******************************************************************/
	 private void setAddFileUpdate(String id, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update OfficialDocument set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where id='"+id+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
	/*******************************************************************
	* 첨부파일 삭제하고 해당내용 update하기 (개별첨부파일 삭제시)
	 *******************************************************************/
	 public void deleteAttachFile(String id,String fname,String ftype,String fsize,String sname,String deleteAttachNo,
						String upload_path,String bon_path) throws Exception
	{
		//----------------------------------
		// 관련내용 배열로 담기
		//----------------------------------
		int cnt = 0;
		for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

		String[][] addFile = new String[cnt][5];
		for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

		if(fname.length() != 0) {
			StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
			int m = 0;
			while(f.hasMoreTokens()) {
				addFile[m][0] = f.nextToken();
				addFile[m][0] = addFile[m][0].trim(); 
				if(addFile[m][0] == null) addFile[m][0] = "";
				m++;
			}
			StringTokenizer t = new StringTokenizer(ftype,"|");		//파일type 담기
			m = 0;
			while(t.hasMoreTokens()) {
				addFile[m][1] = t.nextToken();
				addFile[m][1] = addFile[m][1].trim();
				if(addFile[m][1] == null) addFile[m][1] = "";
				m++;
			}
			StringTokenizer s = new StringTokenizer(fsize,"|");		//파일크기 담기
			m = 0;
			while(s.hasMoreTokens()) {
				addFile[m][2] = s.nextToken();
				addFile[m][2] = addFile[m][2].trim();
				if(addFile[m][2] == null) addFile[m][2] = "";
				m++;
			}
			StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
			m = 0;
			while(o.hasMoreTokens()) {
				addFile[m][3] = o.nextToken();	
				addFile[m][3] = addFile[m][3].trim();				//저장파일명(.bin제외)
				if(addFile[m][3] == null) addFile[m][3] = "";
				//첨부파일에서 확장자(_1_2_3..)번호 찾기
				if(addFile[m][3].length() > 3) {
					int en = addFile[m][3].indexOf("_");
					addFile[m][4] = addFile[m][3].substring(en+1,en+2);
				} else addFile[m][4] = "0";
				m++;
			}
		}

		//----------------------------------
		//해당 첨부파일을 찾아 처리하기
		//----------------------------------
		String nfname="", nftype="", nfsize="", nsname = "";
		int del_no = 0;			//삭제할 배열번호
		int del_ext = Integer.parseInt(deleteAttachNo);
//System.out.println("cnt : del_ext == " + cnt + ":" + del_ext);
		for(int i=0; i<cnt; i++) {
			int ext_no = Integer.parseInt(addFile[i][4]);
			if(ext_no == del_ext) {	//내용을 제외한 
				del_no = i;			//삭제할 배열번호
				nfname += " |";
				nftype += " |";
				nfsize += " |";
				nsname += " |";
			}
			else {
				nfname += addFile[i][0] + " |";
				nftype += addFile[i][1] + " |";
				nfsize += addFile[i][2] + " |";
				nsname += addFile[i][3] + " |";
			}
		}

		//Tabel update 하기
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update OfficialDocument set ";
		update += "fname='"+nfname+"',sname='"+nsname+"',ftype='"+nftype+"',fsize='"+nfsize+"' where id="+id;
		int er = stmt.executeUpdate(update);
		stmt.close();
//System.out.println("upload_path " + upload_path );
//System.out.println("bon_path " + bon_path );
//System.out.println("addFile[del_ext][3] " + addFile[del_ext][3] );
		//첨부파일 삭제하기
		deleteAddFile(upload_path,bon_path,addFile[del_no][3]);		//첨부파일 삭제하기

	}

	//*******************************************************************
	//	주어진 관리번호로 필요내용 찾아 관련내용 삭제하기
	//*******************************************************************/	
	public void deletePid (String id,String upload_path) throws Exception
	{
		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();
		
		//query문장 만들기
		String query  = "SELECT * FROM OfficialDocument where id='"+id+"'";
		rs = stmt.executeQuery(query);
		
		if(rs.next()) { 
			bon_path = rs.getString("bon_path");	if(bon_path == null) bon_path = "";	//확장path
			bon_file = rs.getString("bon_file");	if(bon_file == null) bon_file = "";	//본문저장 파일명
			sname = rs.getString("sname");	if(sname == null) sname = "";				//공통:파일저장명	(확장자 .bin이없음)
		}

		boolean dbt = deleteBonText(upload_path,bon_path,bon_file);		//본문파일 삭제하기
		boolean daf = deleteAddFile(upload_path,bon_path,sname);		//첨부파일 삭제하기
		if(dbt || daf) {
			deleteTableLine("OfficialDocument",id);		//해당line 삭제하기
		}
		//공통 항목 끝내기 (닫기)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	본문파일 삭제하기
	//*******************************************************************/	
	public boolean deleteBonText (String upload_path,String bon_path,String bon_file)
	{
		boolean rtn = false;

		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filename = upload_path + bon_path + "/bonmun/" + bon_file;
		//System.out.println("bonmun file : " + filename);
		rtn = text.delFilename(filename);	//해당 파일삭제 하기
		return rtn;
	}

	//*******************************************************************
	//	첨부파일 삭제하기
	//*******************************************************************/	
	public boolean deleteAddFile (String upload_path,String bon_path,String sname)
	{
		boolean rtn = false;

		com.anbtech.file.textFileReader text = new com.anbtech.file.textFileReader();
		String filepath = upload_path + bon_path + "/addfile"; 
		String filename = "";

		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");		
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			read = read.trim();
			if(read.length() != 1) {
				filename = filepath+"/"+read+".bin";			
				//System.out.println("delete attach file : " + filename);
				rtn = text.delFilename(filename);	//해당 파일삭제 하기
			}
		}
		return rtn;
	}
	//*******************************************************************
	//	Table OfficialDocument의 Line삭제하기 
	//*******************************************************************/	
	public void deleteTableLine (String tablename,String id) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String query  = "delete FROM "+tablename+" where id='"+id+"'";
		//System.out.println("delete : " + query);
		stmt.execute(query);
		stmt.close();
	}
	
}