package com.anbtech.es.bogoseo.db;
import com.anbtech.file.FileWriteString;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;
import java.text.DecimalFormat;

public class BogoSeoDAO{
	private Connection con;
	private com.anbtech.file.FileWriteString text;					//파일저장
	private com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리하기
	
	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public BogoSeoDAO(Connection con){
		this.con = con;
	}

	/*******************************************************************
	* 보고서 내용 저장하기 
	*******************************************************************/
	public void setBogoTable(String bg_id,String user_id,String user_name,String user_code,String user_rank,
		String ac_id,String ac_code,String ac_name,String in_date,String prj_name,String ap_name,String subject,String content,String upload_path) throws Exception
	{
		//' 없애기
		subject = str.repWord(subject,"'","`");		//제목
		content = str.repWord(content,"'","`");		//내용
		
		//본문저장디렉토리및 파일명
		String root_path = upload_path;
		String doc_pat = "/es/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO BOGOSEO_MASTER(bg_id,ys_kind,user_id,user_name,user_code,user_rank,";
			incommon += "ac_id,ac_code,ac_name,in_date,prj_name,ap_name,subject,bon_path,bon_file) values('";
		
		String input = incommon+bg_id+"','BOGO','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','"+prj_name+"','"+ap_name+"','";
			input += subject+"','"+doc_pat+"','"+bg_id+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//저장1. 본문파일로 저장하기
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,bg_id,content);
		}
	}

	/*******************************************************************
	* 출장 보고서 내용 저장하기 
	*******************************************************************/
	public void setChuljangBogoTable(String bg_id,String user_id,String user_name,String user_code,String user_rank,
		String ac_id,String ac_code,String ac_name,String in_date,String bg_dest,String bg_purpose,String content,String upload_path) throws Exception
	{
		//' 없애기
		bg_dest = str.repWord(bg_dest,"'","`");				//목적지
		bg_purpose = str.repWord(bg_purpose,"'","`");		//제목
		content = str.repWord(content,"'","`");				//내용

		//본문저장디렉토리및 파일명
		String root_path = upload_path;
		String doc_pat = "/es/"+user_id;

		Statement stmt = null;
		stmt = con.createStatement();
		String incommon = "INSERT INTO BOGOSEO_MASTER(bg_id,ys_kind,user_id,user_name,user_code,user_rank,";
			incommon += "ac_id,ac_code,ac_name,in_date,bon_path,bon_file,bg_dest,bg_purpose) values('";
		
		String input = incommon+bg_id+"','CHULJANG_BOGO','"+user_id+"','"+user_name+"','"+user_code+"','"+user_rank+"','";
			input += ac_id+"','"+ac_code+"','"+ac_name+"','"+in_date+"','"+doc_pat+"','"+bg_id+"','";
			input += bg_dest+"','"+bg_purpose+"')";
		//System.out.println("inputs : " + input );
		int er = stmt.executeUpdate(input);
		
		stmt.close();
		//저장1. 본문파일로 저장하기
		if(er > 0) {
			setTableBonFile(root_path,doc_pat,bg_id,content);
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
	 public int setAddFile(MultipartRequest multi,String es_id,String filepath) throws Exception
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
				File myFile = new File(myDir,es_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				filename += fname + " |";
				savename += es_id + "_" + i + " |";
				filetype += ftype + " |";
				filesize += fsize + " |";
				atcnt++;
			}
			i++;
		}//while

		//Table에 저장하기
		if(i > 1) {
			setAddFileUpdate(es_id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* 첨부파일 저장하기 (임시저장후 수정하여 첨부할때)
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String es_id,String filepath,
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
				File myFile = new File(myDir,es_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				newdata += n + "|";					//배열번호
				newdata += uname + "|";
				newdata += es_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
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
		java.util.StringTokenizer ndata = new StringTokenizer(newdata,";");
		while(ndata.hasMoreTokens()) {
			String nnd = ndata.nextToken();		//1라인 읽기
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			int m = 0, ai = 0;	//배열번호 찾기
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				if(m == 0) ai = Integer.parseInt(nndata.nextToken());
				else {
					nfile[ai][ni] = nndata.nextToken();
					ni++;
				}
				m++;
			}
		}
		//기존에 있던 첨부파일 배열에 붙이기
		java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			//첨부파일명
		int hi = 0;
		while(o_fname.hasMoreTokens()) {
			String read = o_fname.nextToken();
			if(read.length() != 1) nfile[hi][0] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			//첨부파일 저장명
		hi = 0;
		while(o_sname.hasMoreTokens()) {
			String read = o_sname.nextToken();
			if(read.length() != 1) {
				int hi_no = hi + 1;
				String org_file = read.substring(0,read.length()-1);
				File BFN = new File(filepath+"/"+org_file+".bin");			//바꿀File명
				File AFN = new File(filepath+"/"+es_id+"_"+hi_no+".bin");	//새로운 파일명
				if(BFN.exists()) BFN.renameTo(AFN);	
				nfile[hi][1] = es_id+"_"+hi_no;
			}
			hi++;
		}
		java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			//첨부파일 타입
		hi = 0;
		while(o_ftype.hasMoreTokens()) {
			String read = o_ftype.nextToken();
			if(read.length() != 1) nfile[hi][2] = read.substring(0,read.length()-1);
			hi++;
		}
		java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			//첨부파일 크기
		hi = 0;
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
			setAddFileUpdate(es_id,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	/*******************************************************************
	* 첨부파일 저장한후 Table에 update하기
	 *******************************************************************/
	 private void setAddFileUpdate(String es_id, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update bogoseo_master set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where bg_id='"+es_id+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}
}	
