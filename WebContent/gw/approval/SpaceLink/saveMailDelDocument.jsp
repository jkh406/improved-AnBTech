<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "보관문서 멜작성 저장하기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.text.*"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.file.textFileReader"

%>
<%@	page import="com.anbtech.date.anbDate"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//메시지 전달변수
	String Message="";		//메시지 전달 변수  

	String id = "";			//접속자 id
	String name = "";		//접속자 이름
	String division = "";	//접속자 부서명
	String tel = "";		//접속자 전화번호

	//내부처리 변수
	String LIST="";			//수신인명단
	String RES="";			//발송/미발송 받기
	String subject="";		//문서제목
	String content="";		//본문
	String selopt = "";		//옵션사항
	String bPath = "";		//본문저장 path
	String bFile = "";		//본문파일명

	String apath = "";		//첨부파일 Path
	String pad1o = "";		//첨부된 파일명1 원래이름	
	String pad1f = "";		//첨부된 파일명1
	String pad2o = "";		//첨부된 파일명2 원래이름	
	String pad2f = "";		//첨부된 파일명2
	String pad3o = "";		//첨부된 파일명3 원래이름	
	String pad3f = "";		//첨부된 파일명3	

	String SMSG = "";		//배달선택사항 다시저장하기

	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	FileWriteString text = new com.anbtech.file.FileWriteString();			//내용을 파일로 담기
	textFileReader Rtext = new com.anbtech.file.textFileReader();			//text문자 읽기

	/*********************************************************************
	 	접속자 login 알아보기
	*********************************************************************/
	id = sl.id; 		//접속자 login id
	
	String[] idColumn = {"a.id","a.name","a.office_tel","b.ac_name"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	while(bean.isAll()) {
		name = bean.getData("name");				//접속자 명
		division = bean.getData("ac_name");		//접속자 부서명
		tel = bean.getData("office_tel");			//접속자 전화번호
	} //while

	/********************************************************************
		 MultipartRequest 빈을 생성시키기 위함.
	*********************************************************************/
	String DIR = bean.getID();	//Directory생성 PID (text file and upload file을 동일한 파일명으로 갖는다. 저장Dir은 다름)
	int maxUploadSize	= 10; 								// 첨부파일의 최대 크기를 지정(단위:Mbyte)
	String saveDir = upload_path+crp+"/email/"+id+"/addfile";// 첨부파일의 저장 디렉토리 지정(Full path을 쓴다.)
	text.setFilepath(saveDir);		//directory생성하기

	//조건에따라 생성자 생성
	com.oreilly.servlet.MultipartRequest multi;
	multi = new MultipartRequest(request,saveDir,maxUploadSize*1024*1024,"euc-kr");

	/*********************************************************************
	// 임시저장 처리키 위한 준비 
	// [임시저장내용 삭제하고 다시 전송/미전송하기,	단, 첨부파일이 있으면 보상하기 ]
	*********************************************************************/
	String rpid ="";		//전달받은 pid로 전송/미전송시 해당내용 삭제키 위해
	String path = "";		//첨부파일 path
	String Bpath = "";		//본문파일 path
	String pfie = "";		//본문 파일명 (window)

	rpid = multi.getParameter("pid");		//from post_write.jsp
	if(rpid == null) rpid = "";				//신규 편지일때
	if(rpid.equals("null")) rpid = "";

	if(rpid.length() != 0) {				
		String[] pidColumn = {"pid","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
		
		bean.setTable("POST_MASTER");
		bean.setColumns(pidColumn);
		bean.setOrder("pid ASC");	
		bean.setSearch("pid",rpid);	
		bean.init();

		if(bean.isEmpty()) Message="NO_DATA";
		else if (bean.isAll()) {	
			selopt = bean.getData("post_select");		//배달선택사항
			String Path = bean.getData("bon_path");		//본문path
			if(Path == null) path = crp + "/";
			else {
				path = crp + Path.substring(0,Path.lastIndexOf('/'))+"/addfile";	//첨부파일 path
				Bpath = crp + Path;													//본문파일 path
			}

			pfie = bean.getData("bon_file");			//본문 파일명 (window)
			if(pfie == null) pfie = "";

			pad1o = bean.getData("add_1_original");		//첨부된 파일명1 원래이름
			pad1f = bean.getData("add_1_file");			//첨부된 파일명1

			pad2o = bean.getData("add_2_original");		//첨부된 파일명2 원래이름
			pad2f = bean.getData("add_2_file");			//첨부된 파일명2

			pad3o = bean.getData("add_3_original");		//첨부된 파일명3 원래이름
			pad3f = bean.getData("add_3_file");			//첨부된 파일명3			
		} //while
	}


	/*********************************************************************
		배달선택사항값읽기 (from post_select.jsp)
	*********************************************************************/
	String cfm = multi.getParameter("ReturnReceipt");
	String sec = multi.getParameter("SecretSetup");
	String rsp = multi.getParameter("ReplySetup");

	String SEL_DATA = "";			//저장하기
	if(cfm != null) SEL_DATA += cfm + ","; 
	if(sec != null) SEL_DATA += sec + ","; 
	if(rsp != null) SEL_DATA += rsp + ","; 

	//from post_main.jsp로 읽은데이터 가 있으면
	if(SEL_DATA.length() == 0) {
		if(SMSG.length() > 0) SEL_DATA = SMSG;
	} 

	//임시저장으로 부터 읽었을때 배달선택이 없으면 과거데이터로 보상
	if((SEL_DATA.length() == 3) && (rpid.length() != 0)) SEL_DATA = selopt;

	/*********************************************************************
	 	내용 저장하기
	*********************************************************************/
	//--------------------------------
	// 발송/미발송[저장] 처리 (최초 메뉴입력시 여기서 중단)
	//---------------------------------
	RES = multi.getParameter("res");						//발송(SND) 미발송(TMP)

	//---------------------------------
	//제목 읽기
	//----------------------------------
	subject = multi.getParameter("SUBJECT");			//제목 

	//---------------------------------
	//본문내용 읽기
	//----------------------------------
	content=multi.getParameter("CONTENT");								//쓴내용(unix oracle에서 한글입력)
	//	본문내용 파일로 저장
	String contentDir = upload_path + crp + "/email/" + id + "/text";	//저장 Dir  
	text.setFilepath(contentDir);											//directory생성하기
	String text_file = DIR;													//저장 파일명 (full path)
	text.WriteHanguel(contentDir,text_file,content);						//내용 파일로 저장하기

	//-----------------------------------
	// 	수신인 개인별 입력받기
	//------------------------------------
	LIST=multi.getParameter("rec_name");
	if(LIST == null) LIST = "";

	//--------------------------------
	//본문path,입력일, 삭제예정일 설정
	//---------------------------------
	String conDir = "/email/" + id + "/text";				//본문 path
	String up_date = anbdt.getTime();							//입력일
	String del_date = anbdt.getAddMonthNoformat(6);				//삭제예정일 (6개월후 : post_letter)
	String del_year = anbdt.getAddYearNoformat(1);				//삭제예정일 (1년후   : post_master) 

	//--------------------------------
	// 첨부파일1 처리
	//---------------------------------/
	String File_Name1 = multi.getFilesystemName("UP_FILE1");//첨부파일명
	if(File_Name1 == null) File_Name1 = "";

	//파일저장 이름 바꾸기
	String nFile1 = "";													//새로저장할 파일명
	if(File_Name1.length() != 0) {
		int d = File_Name1.indexOf(".");
		String Hfile = File_Name1.substring(0,d);						//파일명
		String Ext = File_Name1.substring(d+1,File_Name1.length());		//확장자명

		nFile1 = DIR + "_1." + Ext;										//새로운 파일명
		String oFilename = saveDir + "/" + File_Name1;					//기존파일명(path 포함)
		String nFilename = saveDir + "/" + nFile1;						//새로운파일명(path 포함)
		Rtext.chgFilename(oFilename,nFilename);
	}

	//--------------------------------
	// 첨부파일2 처리
	//---------------------------------/
	String File_Name2 = multi.getFilesystemName("UP_FILE2");//첨부파일명
	if(File_Name2 == null) File_Name2 = "";

	//파일저장 이름 바꾸기
	String nFile2 = "";													//새로저장할 파일명
	if(File_Name2.length() != 0) {
		int d = File_Name2.indexOf(".");
		String Hfile = File_Name2.substring(0,d);						//파일명
		String Ext = File_Name2.substring(d+1,File_Name2.length());		//확장자명

		nFile2 = DIR + "_2." + Ext;										//새로운 파일명
		String oFilename = saveDir + "/" + File_Name2;					//기존파일명(path 포함)
		String nFilename = saveDir + "/" + nFile2;						//새로운파일명(path 포함)
		Rtext.chgFilename(oFilename,nFilename);
	}

	//--------------------------------
	// 첨부파일3 처리
	//---------------------------------/
	String File_Name3 = multi.getFilesystemName("UP_FILE3");//첨부파일명
	if(File_Name3 == null) File_Name3 = "";

	//파일저장 이름 바꾸기
	String nFile3 = "";													//새로저장할 파일명
	if(File_Name3.length() != 0) {
		int d = File_Name3.indexOf(".");
		String Hfile = File_Name3.substring(0,d);						//파일명
		String Ext = File_Name3.substring(d+1,File_Name3.length());		//확장자명

		nFile3 = DIR + "_3." + Ext;										//새로운 파일명
		String oFilename = saveDir + "/" + File_Name3;					//기존파일명(path 포함)
		String nFilename = saveDir + "/" + nFile3;						//새로운파일명(path 포함)
		Rtext.chgFilename(oFilename,nFilename);
	}

	//--------------------------------
	// 관리번호 설정
	//---------------------------------/
	String pid = bean.getID();											//관리번호

	//----------------------------------
	// 수신자를 분해해서 저장하기 (이름/사번;)
	//-----------------------------------/
	//콜론으로 나누어 수신자 인원수 파악
	int Rcnt = 0;														//수신자 총인원파악
	for(int i=0; i < LIST.length(); i++) 
		if(LIST.charAt(i) == ';') Rcnt++;

	//사번만을 배열에 담기	
	int Scnt = 0;														//찾은 콜론 리턴번호 갖기
	int Tcnt = 0;
	String[] LIST_ID = new String[LIST.length()];						//배열만들기
	for(int j=0; j < LIST.length(); j++) {
		if(LIST.charAt(j) == ';') {
			String FID = LIST.substring(Scnt,j);						//개인별 전체 : 이름/사번
			String CID = FID.substring(0,FID.indexOf('/'));				//사번만      : 사번
			LIST_ID[Tcnt] = CID;										//사번만 배열에 담기
			//out.println("ID : " + LIST_ID[Tcnt] + "<br>");
			Scnt = j+3;				//리턴키(2) + ;값(1) = 3을 더한다.  
			Tcnt++;
		}
	}

	//--------------------------------
	// 관련내용을 DB로 저장하기
	// (INFROM_BRD)
	//---------------------------------/	
	//	DB저장을 위한 특수문자 바꾸기 (' -> `) 
	subject = str.quoteReplace(subject);		//제목

	String inputs = ""; 		//POST_LETTER Table
	String m_inputs = "";		//POST_MASTER Table
	String rpid_del = "";		//from post_main.jsp의 보낼편지로 부터 읽은 편지일경우
	
	if(RES.equals("SND")) {					//전송
		for(int k=0; k < Rcnt; k++) {		//POST_LETTER저장
			inputs = "INSERT INTO POST_LETTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_select,delete_date) values('";
			inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST_ID[k] + "','" + "0" + "','" + SEL_DATA + "','" + del_date + "')";
			try { bean.execute(inputs); } catch (Exception e) { out.println("수신자 구분 : " + e); } 
			//out.println("inputs : " + inputs + "<br>");
		}
		// POST_MASTER저장
		m_inputs = "INSERT INTO POST_MASTER(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,post_state,post_select,bon_path,bon_file,";
		m_inputs += "add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) values('";
		m_inputs += pid + "','" + subject + "','" + id + "','" + name + "','" + up_date + "','" + LIST + "','" + "0" + "','";
		m_inputs += "email" + "','" + SEL_DATA + "','" + conDir + "','" + text_file + "','" + File_Name1 + "','" + nFile1 + "','";
		m_inputs += File_Name2 + "','" + nFile2 + "','" + File_Name3 + "','" + nFile3 + "','" + del_year + "')";
		//보낼편지로 부터 읽은 편지일 경우 해당내용(POST_MASTER) 삭제하기
		if(rpid.length() > 5) {
			rpid_del = "delete from POST_MASTER where pid='" + rpid + "'";
		}
	} 
	//out.println("m_inputs : " + m_inputs + "<br>");
	
	//-----------------------------------------------------
	//  공통항목 저장 실행하기
	///------------------------------------------------------
	try { 	
		bean.execute(m_inputs);							//저장하기 (전송,미전송)
		if(rpid.length() > 0) {
			bean.execute(rpid_del);	//미전송에서 재전송시
		}
	} 
	catch (Exception e) { 
		//저장실패시 첨부파일 삭제하기
		String Filedir1 = upload_path + crp + "/email/" + id + "/addfile/" + nFile1;
		String Filedir2 = upload_path + crp + "/email/" + id + "/addfile/" + nFile2;
		String Filedir3 = upload_path + crp + "/email/" + id + "/addfile/" + nFile3;
		Rtext.delFilename(Filedir1);	Rtext.delFilename(Filedir2);	Rtext.delFilename(Filedir3);	
		
		//본문파일 삭제하기 (Windows)
		String bFileD = upload_path + crp + "/email/" + id + "/text/" + text_file;
		Rtext.delFilename(bFileD);	
	}
%>

<!-- ****************** 메시지 전달부분 ****************************** -->
<script>
self.close()
</script>

