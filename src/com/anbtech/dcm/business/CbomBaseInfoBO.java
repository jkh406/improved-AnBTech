package com.anbtech.dcm.business;
import com.anbtech.dcm.entity.*;
import com.anbtech.bm.entity.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.file.textFileReader;

public class CbomBaseInfoBO
{
	private Connection con;
	private com.anbtech.dcm.db.CbomModifyDAO cmodDAO = null;						//ECC정보
	private com.anbtech.bm.db.BomShowDAO showDAO = null;							//BOM정보
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();		//일자입력
	
	private String query = "";
	private String[][] item = null;				//TREE정보를 배열로 담기
	private int an = 0;							//items의 배열 갯수

	private String[][] plist = null;			//읽은 파일내용을 배열에 담기 
	private int elecnt=0;						//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;						//읽은 파일의 라인갯수 

	//*******************************************************************
	//	생성자 만들기
	//*******************************************************************/
	public CbomBaseInfoBO(Connection con) 
	{
		this.con = con;
		cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);
		showDAO = new com.anbtech.bm.db.BomShowDAO(con);
	}

	//--------------------------------------------------------------------
	//
	//		ECR 에 관한 메소드 정의 : 기본정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECC_COM:ECR에 데이터 입력하기
	//	#default로 ECC_REQ에도 입력한다.
	//*******************************************************************/	
	public String insertECR(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String mgr_id,
		String ecc_kind,String pdg_code,String pd_code,String fg_code,String part_code,
		String chg_position,String trouble,String condition,String solution) throws Exception
	{
		String input="",data="";

		//ECO NO 자동채번 구하기
		String eco_no = getEcoNo("ECR");			//ECO NO 자동채번

		//책임자 정보 구하기 [배열: [0]사번,[1]이름,[2]부서관리코드,[3]부서코드,[4]부서명,[5]전화번호]
		String mgr[] = new String[6];
		mgr = cmodDAO.getUserData(mgr_id);

		//ECC_COM에 입력하기
		input = "INSERT INTO ecc_com (pid,ecc_subject,eco_no,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,";
		input += "ecr_tel,ecr_date,mgr_id,mgr_name,mgr_code,mgr_div_code,mgr_div_name,eco_id,eco_name,";
		input += "eco_code,eco_div_code,eco_div_name,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,";
		input += "pdg_code,pd_code,fg_code,part_code,order_date,fix_date,ecc_status) values('";
		input += pid+"','"+ecc_subject+"','"+eco_no+"','"+ecr_id+"','"+ecr_name+"','"+ecr_code+"','";
		input += ecr_div_code+"','"+ecr_div_name+"','"+ecr_tel+"','"+ecr_date+"','"+mgr_id+"','";
		input += mgr[1]+"','"+mgr[2]+"','"+mgr[3]+"','"+mgr[4]+"','"+""+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+ecc_kind+"','";
		input += pdg_code+"','"+pd_code+"','"+fg_code+"','"+part_code+"','"+""+"','"+""+"','"+"1"+"')";
		cmodDAO.executeUpdate(input);

		//ECC_REQ 입력하기
		input = "INSERT INTO ecc_req (pid,chg_position,trouble,condition,solution,";
		input += "fname,sname,ftype,fsize,app_no) values('";
		input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
		cmodDAO.executeUpdate(input);
		
		data = "정상적으로 ECR등록되었습니다.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:ECR에 데이터 수정하기
	//	#default로 ECC_REQ에도 수정한다.
	//*******************************************************************/	
	public String updateECR(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String mgr_id,
		String ecc_kind,String pdg_code,String pd_code,String fg_code,String part_code,
		String chg_position,String trouble,String condition,String solution) throws Exception
	{
		String update="",data="";

		//수정가능한 상태를 검사한다. 초기등록상태[1]및 반려[0]후 다시작성시만 가능함
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(ecc_status.equals("0")) {			//반려후 다시 작성시 상태를 작성으로 바꿔준다.
			update = "UPDATE ecc_com SET ecc_status='1' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		else if(!ecc_status.equals("1")) {
			data = "작성상태일때만 수정 할 수 있습니다.";
			return data;
		}

		//책임자 정보 구하기 [배열: [0]사번,[1]이름,[2]부서관리코드,[3]부서코드,[4]부서명,[5]전화번호]
		String mgr[] = new String[6];
		mgr = cmodDAO.getUserData(mgr_id);
		
		//ECC_COM에 수정하기
		update = "UPDATE ecc_com SET ecc_subject='"+ecc_subject+"',ecr_id='"+ecr_id;
		update += "',ecr_name='"+ecr_name+"',ecr_code='"+ecr_code+"',ecr_div_code='"+ecr_div_code;
		update += "',ecr_div_name='"+ecr_div_name+"',ecr_tel='"+ecr_tel+"',ecr_date='"+ecr_date;
		update += "',mgr_id='"+mgr_id+"',mgr_name='"+mgr[1]+"',mgr_code='"+mgr[2];
		update += "',mgr_div_code='"+mgr[3]+"',mgr_div_name='"+mgr[4]+"',ecc_kind='"+ecc_kind;
		update += "',pdg_code='"+pdg_code+"',pd_code='"+pd_code+"',fg_code='"+fg_code;
		update += "',part_code='"+part_code+"' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		//ECC_REQ에 수정하기
		update = "UPDATE ecc_req SET chg_position='"+chg_position+"',trouble='"+trouble;
		update += "',condition='"+condition+"',solution='"+solution;
		update += "' where pid='"+pid+"'";
		cmodDAO.executeUpdate(update);

		data = "정상적으로 ECR수정되었습니다.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:REQ에 데이터 삭제하기
	//	#default로 ECC_REQ에도 삭제한다.
	//*******************************************************************/	
	public String deleteECR(String pid,String filepath) throws Exception
	{
		String delete = "",data="";

		//삭제가능한 상태를 검사한다. 초기등록상태만 가능함
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("1")) {
			data = "작성상태일때만 삭제 할 수 있습니다.";
			return data;
		}

		//첨부파일 삭제하기
		com.anbtech.dcm.entity.eccReqTable ecrT = new com.anbtech.dcm.entity.eccReqTable();
		ecrT = cmodDAO.readEccReq(pid);
		String sname = ecrT.getSname();								//첨부파일 저장명
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}
		
		//ECC_COM 삭제하기
		delete = "DELETE from ECC_COM where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		//ECC_REQ 삭제하기
		delete = "DELETE from ECC_REQ where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		data = "정상적으로 ECR삭제되었습니다.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		ECO 에 관한 메소드 정의 : 기본정보
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECC_COM:ECR에 데이터 입력하기  
	//	#default로 ECC_ORD에도 입력한다.
	//*******************************************************************/	
	public String insertECO(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String eco_id,String eco_tel,
		String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,String pdg_code,String pd_code,
		String fg_code,String part_code,String order_date,String chg_position,String trouble,
		String condition,String solution) throws Exception
	{
		String input="",update="",data="";
		String eco_no = getEcoNo("ECO");

		//ECR 부터 시작된 ECO인지 설계자 발의 ECO인지를 판단하여 등록한다.
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");

		//담당자 정보 구하기 [배열: [0]사번,[1]이름,[2]부서관리코드,[3]부서코드,[4]부서명,[5]전화번호]
		String eco[] = new String[6];
		eco= cmodDAO.getUserData(eco_id);
		
		//ECR부터 발의된 경우 : ECC_COM을 UPDATE한다.
		if(mgr_id.length() !=0) {
			update = "UPDATE ecc_com set ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor;
			update += "',ecc_scope='"+ecc_scope+"',order_date='"+order_date;
			update += "' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//ECO부터 발의된 경우 : ECC_COM을 INSERT한다.
		else {
			input = "INSERT INTO ecc_com (pid,ecc_subject,eco_no,ecr_id,ecr_name,ecr_code,ecr_div_code,ecr_div_name,";
			input += "ecr_tel,ecr_date,mgr_id,mgr_name,mgr_code,mgr_div_code,mgr_div_name,eco_id,eco_name,";
			input += "eco_code,eco_div_code,eco_div_name,eco_tel,ecc_reason,ecc_factor,ecc_scope,ecc_kind,";
			input += "pdg_code,pd_code,fg_code,part_code,order_date,fix_date,ecc_status) values('";
			input += pid+"','"+ecc_subject+"','"+eco_no+"','"+ecr_id+"','"+ecr_name+"','"+ecr_code+"','";
			input += ecr_div_code+"','"+ecr_div_name+"','"+ecr_tel+"','"+ecr_date+"','"+""+"','";
			input += ""+"','"+""+"','"+""+"','"+""+"','"+eco_id+"','"+eco[1]+"','"+eco[2]+"','";
			input += eco[3]+"','"+eco[4]+"','"+eco_tel+"','"+ecc_reason+"','"+ecc_factor+"','";
			input += ecc_scope+"','"+ecc_kind+"','"+pdg_code+"','"+pd_code+"','"+fg_code+"','"+part_code+"','";
			input += order_date+"','"+""+"','"+"6"+"')";
			cmodDAO.executeUpdate(input);
		}

		//ECC_ORD 입력하기 : 공통
		input = "INSERT INTO ecc_ord (pid,chg_position,trouble,condition,solution,";
		input += "fname,sname,ftype,fsize,app_no) values('";
		input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
		input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
		cmodDAO.executeUpdate(input);
		
		data = "정상적으로 ECO등록되었습니다.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:ECO에 데이터 수정하기
	//	#default로 ECC_ORD에도 수정한다.
	//*******************************************************************/	
	public String updateECO(String pid,String ecc_subject,String ecr_id,String ecr_name,String ecr_code,
		String ecr_div_code,String ecr_div_name,String ecr_tel,String ecr_date,String eco_id,String eco_tel,
		String ecc_reason,String ecc_factor,String ecc_scope,String ecc_kind,String pdg_code,String pd_code,
		String fg_code,String part_code,String order_date,String chg_position,String trouble,
		String condition,String solution) throws Exception
	{
		String update="",data="",input="";

		//----------------------------------------
		//  등록/수정 가능 상태 검사하기
		//----------------------------------------
		//수정가능한 상태를 검사한다. 초기등록상태[6]및 반려[5]상태시,검토담당자접수[4]시만 가능함
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		//기술검토 담당자 접수시 작성상태
		if(ecc_status.equals("4")) {				
			update = "UPDATE ecc_com SET ecc_status='6' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//반려상태일때
		else if(ecc_status.equals("5")) {				
			update = "UPDATE ecc_com SET ecc_status='6' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//작성상태가 아니면 리턴
		else if(!ecc_status.equals("6")) {
			data = "작성상태일때만 수정 할 수 있습니다.";
			return data;
		}

		//----------------------------------------
		//  등록/수정시 인력정보 구하기
		//----------------------------------------
		//담당자 정보 구하기 [배열: [0]사번,[1]이름,[2]부서관리코드,[3]부서코드,[4]부서명,[5]전화번호]
		String eco[] = new String[6];
		eco= cmodDAO.getUserData(eco_id);

		//ECR 부터 시작된 ECO인지 설계자 발의 ECO인지를 판단하여 등록한다.
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");
		
		//----------------------------------------
		//  ECC_COM에 수정하기
		//----------------------------------------
		//ECR부터 발의된 경우 : ECC_COM을 UPDATE한다.
		if(mgr_id.length() !=0) {
			update = "UPDATE ecc_com SET ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor;
			update += "',ecc_scope='"+ecc_scope+"',fg_code='"+fg_code+"',part_code='"+part_code;
			update += "',order_date='"+order_date+"' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}
		//ECO부터 발의된 경우 : ECC_COM을 INSERT한다.
		else {
			update = "UPDATE ecc_com SET ecc_subject='"+ecc_subject+"',ecr_id='"+ecr_id;
			update += "',ecr_name='"+ecr_name+"',ecr_code='"+ecr_code+"',ecr_div_code='"+ecr_div_code;
			update += "',ecr_div_name='"+ecr_div_name+"',ecr_tel='"+ecr_tel+"',ecr_date='"+ecr_date;
			update += "',eco_id='"+eco_id+"',eco_name='"+eco[1]+"',eco_code='"+eco[2];
			update += "',eco_div_code='"+eco[3]+"',eco_div_name='"+eco[4]+"',eco_tel='"+eco_tel;
			update += "',ecc_reason='"+ecc_reason+"',ecc_factor='"+ecc_factor+"',ecc_scope='"+ecc_scope;
			update += "',ecc_kind='"+ecc_kind+"',pdg_code='"+pdg_code+"',pd_code='"+pd_code;
			update += "',fg_code='"+fg_code+"',part_code='"+part_code;
			update += "',order_date='"+order_date+"' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}

		//----------------------------------------
		//  ECC_ORD에 입력/수정하기
		//----------------------------------------
		if(ecc_status.equals("4")) {	//기술검토 담당자로서 최초입력시
			//ECC_ORD 입력하기 : 공통
			input = "INSERT INTO ecc_ord (pid,chg_position,trouble,condition,solution,";
			input += "fname,sname,ftype,fsize,app_no) values('";
			input += pid+"','"+chg_position+"','"+trouble+"','"+condition+"','"+solution+"','";
			input += ""+"','"+""+"','"+""+"','"+""+"','"+""+"')";
			cmodDAO.executeUpdate(input);
		} else {						//작성자로서 수정시
			//ECC_ORD에 수정하기
			update = "UPDATE ecc_ord SET chg_position='"+chg_position+"',trouble='"+trouble;
			update += "',condition='"+condition+"',solution='"+solution;
			update += "' where pid='"+pid+"'";
			cmodDAO.executeUpdate(update);
		}

		data = "정상적으로 ECO수정되었습니다.";
		return data;
	}

	//*******************************************************************
	// ECC_COM:REQ에 데이터 삭제하기
	//	#default로 ECC_REQ에도 삭제한다.
	//*******************************************************************/	
	public String deleteECO(String pid,String filepath) throws Exception
	{
		String delete = "",data="";

		//삭제가능한 상태를 검사한다. 초기등록상태만 가능함
		String ecc_status = cmodDAO.getColumData("ECC_COM","ecc_status","where pid ='"+pid+"'");
		if(!ecc_status.equals("6")) {
			data = "작성상태일때만 삭제 할 수 있습니다.";
			return data;
		}

		//ECR 부터 시작된 ECO이면 삭제할 수 없음
		String mgr_id = cmodDAO.getColumData("ECC_COM","mgr_id","where pid ='"+pid+"'");
		if(mgr_id.length() !=0) {
			data = "ECR발의부터 시작된 ECO는 삭제 할 수 있습니다.";
			return data;
		}

		//첨부파일 삭제하기
		com.anbtech.dcm.entity.eccOrdTable ecoT = new com.anbtech.dcm.entity.eccOrdTable();
		ecoT = cmodDAO.readEccOrd(pid);
		String sname = ecoT.getSname();								//첨부파일 저장명
		int cnt = 0;
		for(int i=0; i<sname.length(); i++) if(sname.charAt(i) == '|') cnt++;
		for(int i=0; i<cnt; i++) {
			StringTokenizer o = new StringTokenizer(sname,"|");		
			while(o.hasMoreTokens()) {
				String redfile = o.nextToken();
				String delfile = filepath+"/"+redfile.trim()+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}
		}
		
		//ECC_COM 삭제하기
		delete = "DELETE from ECC_COM where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		//ECC_REQ 삭제하기
		delete = "DELETE from ECC_REQ where pid='"+pid+"' ";
		cmodDAO.executeUpdate(delete);

		data = "정상적으로 ECO삭제되었습니다.";
		return data;
	}

	//--------------------------------------------------------------------
	//
	//		첨부 문서 저장하기 
	//
	//
	//---------------------------------------------------------------------
	/*******************************************************************
	* 첨부파일 저장하기 (신규로 처음 첨부할때)
	 *******************************************************************/
	 public int setAddFile(MultipartRequest multi,String tablename,String pid,String save_id,String filepath) throws Exception
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
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				filename += fname + "|";
				savename += save_id + "_" + i + "|";
				filetype += ftype + "|";
				filesize += fsize + "|";
				atcnt++;
			}
			i++;
		}//while

		//Table에 저장하기
		if(i > 1) {
			setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		}
		return atcnt;
	}
	
	/*******************************************************************
	* 첨부파일 저장하기 (임시저장후 수정하여 첨부할때)
	* save_head : 저장할 파일의 선두문자
	* delfile 은 삭제할 파일임
	 *******************************************************************/
	 public int setUpdateFile(MultipartRequest multi,String tablename,String pid,String save_head,String filepath,
		 String fname,String sname,String ftype,String fsize,String attache_cnt,String[] chkDelFile) throws Exception
	{
		String save_id = save_head+anbdt.getID();		//신규로 저장할 파일명
		String filename = "";		//원래이름 파일명
		String savename = "";		//저장 파일명
		String filetype = "";		//원래이름 파일 확장자명
		String filesize = "";		//원래이름 파일사이즈
		int att_cnt = Integer.parseInt(attache_cnt);	//첨부파일 최대수량 미만
		String newdata = "";
		
		//------------------------------
		//기존파일중 check된 파일 삭제하기
		//------------------------------
		int cnt = chkDelFile.length;
		for(int i=0; i<cnt; i++) {
			String dfile = filepath+"/"+chkDelFile[i];
			File FN = new File(dfile);
			if(FN.exists()) FN.delete();	
		}

		//------------------------------
		//기존의 파일정보를 배열에 담기
		//------------------------------
		String[][] fdata = null;
		int flen = fname.length();
		int alen = 0,hi = 0;			//기존등록된 파일수
		if(flen != 0) {
			for(int i=0; i<flen; i++) if(fname.charAt(i) == '|') alen++;
			fdata = new String[alen][4];

			//기존저장 원래파일명
			fname = fname.substring(0,fname.length());	//마지막 '|'제거
			java.util.StringTokenizer o_fname = new StringTokenizer(fname,"|");			
			hi = 0;
			while(o_fname.hasMoreTokens()) {
				String read = o_fname.nextToken();
				if(read.length() != 0) fdata[hi][0] = read;
				hi++;
			}
			//기존저장 저장파일명
			sname = sname.substring(0,sname.length());	//마지막 '|'제거
			java.util.StringTokenizer o_sname = new StringTokenizer(sname,"|");			
			hi = 0;
			while(o_sname.hasMoreTokens()) {
				String read = o_sname.nextToken();
				if(read.length() != 0) fdata[hi][1] = read;
				hi++;
			}
			//기존저장 저장파일Type명
			ftype = ftype.substring(0,ftype.length());	//마지막 '|'제거
			java.util.StringTokenizer o_ftype = new StringTokenizer(ftype,"|");			
			hi = 0;
			while(o_ftype.hasMoreTokens()) {
				String read = o_ftype.nextToken();
				if(read.length() != 0) fdata[hi][2] = read;
				hi++;
			}
			//기존저장 저장파일 Size
			fsize = fsize.substring(0,fsize.length());	//마지막 '|'제거
			java.util.StringTokenizer o_fsize = new StringTokenizer(fsize,"|");			
			hi = 0;
			while(o_fsize.hasMoreTokens()) {
				String read = o_fsize.nextToken();
				if(read.length() != 0) fdata[hi][3] = read;
				hi++;
			}

			//fdata배열중 삭제된 내용이 있으면 해당배열의 값을 clear시킨다.
			for(int i=0; i<cnt; i++) {
				if(chkDelFile[i].length() != 0) fdata[i][0]=fdata[i][1]=fdata[i][2]=fdata[i][3]="";
			}
		}

		//------------------------------
		//신규로 첨부한 파일
		//------------------------------
		int i = 1;		//첨부파일확장자
		int n = 0;		//저장배열을 위해
		java.util.Enumeration files = multi.getFileNames();
		while(files.hasMoreElements()) {
			files.nextElement();							//해당파일 읽기
			String name = "attachfile"+i;					//upload한 input file type name parameter
			String uname = multi.getFilesystemName(name);	//upload한 파일명
			
			//기존저장된 파일이 있으면 삭제하기
			if((alen > n) && (uname != null)) {
				String delfile = filepath+"/"+fdata[n][1]+".bin";
				File FN = new File(delfile);
				if(FN.exists()) FN.delete();
			}

			//첨부한 파일 이름바꿔 저장하기
			if(uname != null) {
				String utype = multi.getContentType(name);	//upload한 파일type
				//file size구하기
				File upFile = multi.getFile(name);
				String usize = Integer.toString((int)upFile.length());
				File myDir = new File(filepath);
				File myFile = new File(myDir,save_id+"_"+i+".bin");
				upFile.renameTo(myFile);					//파일이름 바꾸기

				newdata += uname + "|";
				newdata += save_id + "_" + i + "|";
				newdata += utype + "|";
				newdata += usize + ";";
			}
			else newdata += " | | |;";
			i++;
			n++;
		}//while

		//------------------------------
		//DB로 저장할 data 만들기
		//------------------------------
		//신규로 저장한 파일을 배열에 담기
		String[][] ndata = new String[att_cnt-1][4];
		for(int a=0; a<n; a++) for(int b=0; b<4; b++) ndata[a][b] = "";

		java.util.StringTokenizer rdata = new StringTokenizer(newdata,";");
		int ai = 0;
		while(rdata.hasMoreTokens()) {
			String nnd = rdata.nextToken();		//1라인 읽기
			java.util.StringTokenizer nndata = new StringTokenizer(nnd,"|");
			
			int ni = 0;
			while(nndata.hasMoreTokens()) {
				ndata[ai][ni] = nndata.nextToken();
				ni++;
			}
			ai++;
		}
		

		//저장할 변수로 나누기
		int atcnt = 0;			//첨부파일 갯수 리턴값
		for(int p=0; p<att_cnt-1; p++) {
			//새로첨부한 내용만 있을때
			if((n >= p) && (ndata[p][0].length() > 1)) {				
				filename += ndata[p][0] + "|";
				savename += ndata[p][1] + "|";
				filetype += ndata[p][2] + "|";
				filesize += ndata[p][3] + "|"; 
				atcnt++;
			} 
			//신규로 첨부한 파일이 있으면서 기존등록된 내용이 있을때
			else if((alen > p) && (ndata[p][0].length() <= 1)) {		
				if(fdata[p][0].length() != 0) {		//삭제된 내용은 제외
					filename += fdata[p][0] + "|";
					savename += fdata[p][1] + "|";
					filetype += fdata[p][2] + "|";
					filesize += fdata[p][3] + "|"; 
					atcnt++;
				}
			} 
		}

		//------------------------------
		//Table에 저장하기
		//------------------------------
		setAddFileUpdate(tablename,pid,filename,savename,filetype,filesize);
		
		return atcnt;

	}
	/*******************************************************************
	* 첨부파일 저장한후 Table에 update하기
	 *******************************************************************/
	 private void setAddFileUpdate(String tablename,String pid, String filename, String savename, String filetype, String filesize) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		String update = "update "+tablename+" set fname='"+filename+"',sname='"+savename+"',ftype='"+filetype+"',fsize='"+filesize+"'";
			update += " where pid='"+pid+"'";
		int er = stmt.executeUpdate(update);
		
		stmt.close();
	}

	//--------------------------------------------------------------------
	//
	//		공통 메소드 작성
	//
	//
	//---------------------------------------------------------------------
	//*******************************************************************
	// ECO NO 자동 채번 생성하기
	//*******************************************************************/	
	public String getEcoNo(String headchar) throws Exception
	{
		String eco_no = "";
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");

		//검색할 String만들기 : ECO NO : HEADERCHAR + YY + MM + '-' + nnn 
		//HEADERCHAR는 ECO 부터 시작시 : ECO,  ECR부터 시작시 : ECR
		String yy = anbdt.getYear();
		String eco_body = headchar+yy.substring(2,4)+anbdt.getMonth();

		//이번달에 등록된 ECO NO을 구한다.
		String where = "where eco_no like '"+eco_body+"%' order by eco_no desc";
		String was_eco_no = cmodDAO.getColumData("ECC_COM","eco_no",where);

		//구한 eco_no 에서 일련번호만 +1로 한다.
		if(was_eco_no.length() == 0) {
			eco_no = eco_body + "-" + "001";
		} else {
			String serial_no = was_eco_no.substring(was_eco_no.length()-3,was_eco_no.length());
			int s_no = Integer.parseInt(serial_no)+1;
			eco_no = eco_body + "-" + nmf.toDigits(s_no);
		}

		return eco_no;
	}

	/**********************************************************************
	 * MBOM_STR에서 BOM TREE정보를 구하기 : 해당부품 검색용
	 *  sel_date : 유효일자
	 **********************************************************************/
	public ArrayList viewStrList(String fg_code) throws Exception
	{
		String sel_date = anbdt.getDateNoformat();

		//fg_code로 gid 구하기
		String where = "where fg_code = '"+fg_code+"'";
		String gid = cmodDAO.getColumData("MBOM_MASTER","pid",where);
		String model_code = cmodDAO.getColumData("MBOM_MASTER","model_code",where);

		//BOM LIST 구하기
		ArrayList item_list = new ArrayList();
		item_list = showDAO.getForwardBomItems(gid,"0",model_code,sel_date);

		//BOM LIST을 배열에 담기
		int cnt = item_list.size();
		item = new String[cnt][5];
		
		mbomStrTable table = new mbomStrTable();
		Iterator item_iter = item_list.iterator();
		an = 0;
		while(item_iter.hasNext()) {
			table = (mbomStrTable)item_iter.next();
			item[an][0] = table.getLevelNo();
			item[an][1] = table.getParentCode();
			item[an][2] = table.getChildCode();
			item[an][3] = table.getLocation();
			item[an][4] = table.getPartSpec();
			an++;
		}

		//링크 달기
		ArrayList part_list = new ArrayList();
		for(int i=0; i<cnt; i++) {
			mbomStrTable mst = new mbomStrTable();
			mst.setLevelNo(item[i][0]);
			mst.setParentCode(item[i][1]);
			String child_code = "<a href=\"javascript:itemView('"+item[i][0]+"','";
			child_code += item[i][1]+"','"+item[i][2]+"','"+item[i][3]+"','"+item[i][4];
			child_code += "');\">"+item[i][2]+"</a>";
			mst.setChildCode(child_code);
			mst.setLocation(item[i][3]);
			mst.setPartSpec(item[i][4]);
			part_list.add(mst); 
		}

		return part_list;
	}

}