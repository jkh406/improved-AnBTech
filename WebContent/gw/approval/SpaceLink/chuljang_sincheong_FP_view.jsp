<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "출장신청서 보기"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//출력형식 (비용)
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기

	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();		//1차일때
	com.anbtech.gw.entity.TableAppLine app2 = new com.anbtech.gw.entity.TableAppLine();		//2차일때

	//출장신청서 내용관련
	String query = "";
	String div_name = "";			//부서명
	String prj_code = "";			//project code
	String user_id = "";			//대상자 id
	String user_name = "";			//대상자 명
	String fellow_names = "";		//동행자	 사번/이름;
	String f_names = "";			//동행자	 이름,
	String bistrip_kind = "";		//국내/국외 구분
	String bistrip_country = "";	//국가명
	String bistrip_city = "";		//도시명
	String traffic_way = "";		//교통편
	String purpose = "";			//사유
	String syear = "";				//시작 년
	String smonth = "";				//    월
	String sdate = "";				//    일
	String edyear = "";				//종료 년
	String edmonth = "";			//    월
	String eddate = "";				//    일
	String rec = "";				//인수인계자
	String tel = "";				//긴급연락처
	String receiver_id = "";		//출장비 영수인 id
	String receiver_name = "";		//출장비 영수인 명
	String doc_date = "";			//작성 년월일
	int period_n = 0;				//from ~ to 기간 : 박
	int period = 0;					//from ~ to 기간 : 일

	//결재선 관련
	String pid = "";				//관리번호
	String doc_id = "";				//관련문서 관리번호
	String line="";					//읽은문서 결재선
	String r_line = "";				//재작성으로 넘겨주기
	String vdate = "";				//검토자 검토 일자
	String ddate = "";				//승인자 승인 일자
	String wid = "";				//기안자사번
	String vid = "";				//검토자사번
	String did = "";				//승인자사번
	String wname = "";				//기안자
	String vname = "";				//검토자
	String dname = "";				//승인자
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2차측 결재 관련
	String line2="";				//읽은문서 결재선
	String vdate2 = "";				//검토자 검토 일자
	String ddate2 = "";				//승인자 승인 일자
	String wid2 = "";				//기안자사번
	String vid2 = "";				//검토자사번
	String did2 = "";				//승인자사번
	String wname2 = "";				//기안자
	String vname2 = "";				//검토자
	String dname2 = "";				//승인자
	String PROCESS2 = "";			//PROCESS
	String doc_ste2 = "";			//doc_ste

	//*********************************************************************
	// 결재선 내용 받기
	//*********************************************************************
	pid = request.getParameter("pid");	if(pid == null) pid = "";				//관리번호
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";	//관련문서 관리번호
	//1차측 문서관리번호인지 2차측 문서관리버호 있지 알아보기
	String[] otColumn = {"pid","plid"};
	bean.setTable("app_master");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("pid DESC");	
	bean.setSearch("pid",pid);
	bean.init_unique();

	String one_two = "";
	String plid = "";
	if(bean.isAll()) plid = bean.getData("plid");
	if(plid.equals(pid)) one_two = "one";			//1차측 관리번호로 링크관리번호와 같다. (pid == plid)
	else one_two = "two";							//2차측 관리번호로 링크관리번호와 다르다.

	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";		//PROCESS명
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";		//doc_ste

	//결재선 1.2차 변수
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//1차:협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt = 0;											//1차:결재선에 출력될 라인 갯수
	String ag_line2="",a_line2="",cmt2="",t_cmt2="",t_line2="";	//2차:협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt2 = 0;											//2차:결재선에 출력될 라인 갯수
	//--------------------------------
	//	1차부서 전자결재시 (2차부서 내용없음)
	//--------------------------------
	if(one_two.equals("one")) {
		//1차측 결재선 내용(결재선 포함)
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);	
	
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//결재선
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("기안")) {
				wname = app.getApName();	if(wname == null) wname="";		//기안자
				wid = app.getApSabun();		if(wid == null) wid="";			//기안자 사번
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("검토"))  {
				vname = app.getApName();	if(vname == null) vname="";		//검토자
				vid = app.getApSabun();		if(vid == null) vid="";			//검토자 사번
				vdate = app.getApDate();	if(vdate == null) vdate="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("승인"))  {
				dname = app.getApName();	if(dname == null) dname="";		//승인자
				did = app.getApSabun();		if(did == null) did="";			//승인자 사번
				ddate = app.getApDate();	if(ddate == null) ddate="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//협조
				if(PROCESS.equals("APP_BOX")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}

		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

	}

	//--------------------------------
	//	2차부서 전자결재시 (1차부서 내용포함)
	//--------------------------------
	else {	
		//1차측 결재선 내용(결재선 포함)
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);
		ArrayList app_line = new ArrayList();				
		app_line = masterDAO.getTable_line();		
		Iterator app_iter = app_line.iterator();

		while(app_iter.hasNext()) {
			app = (TableAppLine)app_iter.next();
			
			//결재선
			cmt = app.getApComment(); if(cmt == null) cmt = "";
			t_cmt="";
			if(cmt.length() != 0) { 
				t_cmt = "\r    "+cmt; 
				cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
				line_cnt++; 
			}

			if(app.getApStatus().equals("기안")) {
				wname = app.getApName();	if(wname == null) wname="";		//기안자
				wid = app.getApSabun();		if(wid == null) wid="";			//기안자 사번
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("검토"))  {
				vname = app.getApName();	if(vname == null) vname="";		//검토자
				vid = app.getApSabun();		if(vid == null) vid="";			//검토자 사번
				vdate = app.getApDate();	if(vdate == null) vdate="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else if(app.getApStatus().equals("승인"))  {
				dname = app.getApName();	if(dname == null) dname="";		//승인자
				did = app.getApSabun();		if(did == null) did="";			//승인자 사번
				ddate = app.getApDate();	if(ddate == null) ddate="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

				line_cnt++;
			}
			else {	//협조 : 기결함문서시 협조자를 승인자 뒤로 보내기 위해
				if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_BTR")) {
					ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				} else {
					line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

					t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";

					r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";

					line_cnt++;
				}
			}
		}
		if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }	

		//2차측 결재선 구하기
		//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
		if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
		else masterDAO.getTable_MasterPid(pid);
		ArrayList app2_line = new ArrayList();				
		app2_line = masterDAO.getTable_line();		
		Iterator app2_iter = app2_line.iterator();

		while(app2_iter.hasNext()) {
			app2 = (TableAppLine)app2_iter.next();
		
			//결재선
			cmt2 = app2.getApComment(); if(cmt2 == null) cmt2 = "";
			if(cmt2.length() != 0) { 
				t_cmt2 = "\r    "+cmt2; 
				cmt2 = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt2; 
				line_cnt2++; 
			}

			if(app2.getApStatus().equals("기안")) {
				wname2 = app2.getApName();		if(wname2 == null) wname2="";		//기안자
				wid2 = app2.getApSabun();		if(wid2 == null) wid2="";			//기안자 사번
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("검토"))  {
				vname2 = app2.getApName();		if(vname2 == null) vname2="";		//검토자
				vid2 = app2.getApSabun();		if(vid2 == null) vid2="";			//검토자 사번
				vdate2 = app2.getApDate();		if(vdate2 == null) vdate2="";		//검토자 검토일자(있으면결재,없으면결재않됨)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else if(app2.getApStatus().equals("승인"))  {
				dname2 = app2.getApName();		if(dname2 == null) dname2="";		//승인자
				did2 = app2.getApSabun();		if(did2 == null) did2="";			//승인자 사번
				ddate2 = app2.getApDate();		if(ddate2 == null) ddate2="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
				line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

				t_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
				line_cnt2++;
			}
			else {	//협조
				if(PROCESS.equals("APP_BOX")) {
					ag_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					a_line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				} else {
					line2 += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+cmt2+"<br>";

					t_line += app2.getApStatus()+" "+app2.getApName()+"("+app2.getApRank()+")"+app2.getApDivision()+" "+app2.getApDate()+" "+t_cmt2+"\r";
					line_cnt2++;
				}
			}

		} //while
		if(ag_line2.length() != 0) { line2 += ag_line2;	t_line2 += a_line2; }	

	}//if
	
	//결재선 진행상태 알아보기 [APV(검토)단계이면 기안자가 상신취소가능] : 200408 : 공통
	String app_status = "",wrt_id="",app_line_data="",app_cancel="N";
	com.anbtech.gw.entity.TableAppMaster tabM = new com.anbtech.gw.entity.TableAppMaster();
	ArrayList tabML = new ArrayList();	
	tabML = masterDAO.getTable_MasterPid(pid);	
	Iterator tab_iter = tabML.iterator();
	while(tab_iter.hasNext()) {
		tabM = (TableAppMaster)tab_iter.next();
		app_status = tabM.getAmAppStatus();						//현결재 진행상태
		wrt_id = tabM.getAmWriter();							//작성자
		app_line_data = tabM.getAmAppLine(); 
		if(app_line_data.length() >2)
			app_line_data = app_line_data.substring(0,3);		//결재라인중 첫단계
	}
	//결재취소 가능여부 판단
	if(wrt_id.equals(login_id) && app_status.equals(app_line_data)) {
		app_cancel="Y";
	}

	//닫기
	connMgr.freeConnection("mssql",con);				//커넥션 닫기

	/*********************************************************************
	 	출장비 항목 및 출장비용 정보
	*********************************************************************/	
	//출장항목 찾기
	String[] csColumn = {"ys_name","ys_value"};
	bean.setTable("yangsic_env");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("ys_name DESC");	
	bean.setSearch("ys_value","출장비");
	bean.init_unique();

	String cs_code = "";
	if(bean.isAll()) cs_code = bean.getData("ys_name");	//출장비 코드(대분류구분)
	String cs_head = cs_code.substring(0,4);			//일련번호 제외한 나머지 부분

	//출장비 항목 배열에 담기
	bean.setOrder("ys_name ASC");	
	bean.setSearch("ys_name",cs_head);
	bean.init();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][4];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("ys_name");				//출장관리코드
		btrip[i][1] = bean.getData("ys_value");				//출장관리명
		i++;
	} //while

	//출장비용 찾기
	int sum = 0;			//비용 합계
	String[] costColumn = {"gt_id","at_var","gt_cost","cost_cont"};
	bean.setTable("geuntae_account");
	bean.setColumns(costColumn);
	bean.setOrder("at_var ASC");
	for(int c=0; c<i; c++) {
		bean.setSearch("gt_id",doc_id,"at_var",btrip[c][0]);
		bean.init_unique();
		if(bean.isAll()) {
			btrip[c][2] = bean.getData("gt_cost");		//출장금액
			btrip[c][3] = bean.getData("cost_cont");	//출장금액 내역
			//비용합계 계산하기
			btrip[c][2] = str.repWord(btrip[c][2],",","");
			sum += Integer.parseInt(btrip[c][2]);
		}
		else {
			btrip[c][2] = "000";
			btrip[c][3] = "";
		}

	}


	/*********************************************************************
	// 	출장 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_id","user_name","fellow_names","prj_code","gt_purpose","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","gt_dest","country_class","gt_country",
						"traffic_way","receiver_id","receiver_name","proxy","em_tel","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_id = bean.getData("user_id");			//작성자 사번
		user_name = bean.getData("user_name");		//작성자 명
		fellow_names = bean.getData("fellow_names");//동행자 사번/이름
		prj_code = bean.getData("prj_code");		//project code
		purpose = bean.getData("gt_purpose");		//사유
		syear = bean.getData("u_year");				//시작 년
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    월
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    일
		edyear = bean.getData("tu_year");			//종료 년
		edmonth = fmt.toDigits(Integer.parseInt(bean.getData("tu_month")));			//    월
		eddate = fmt.toDigits(Integer.parseInt(bean.getData("tu_date")));			//    일
		bistrip_city = bean.getData("gt_dest");		//출장지 도시명
		bistrip_kind = bean.getData("country_class");	//국내국외구분
		bistrip_country = bean.getData("gt_country");	//국가명
		traffic_way = bean.getData("traffic_way");		//교통편
		receiver_id = bean.getData("receiver_id");		//영수인 id
		receiver_name = bean.getData("receiver_name");	//영수인명
		rec = bean.getData("proxy");				//인수인계자
		tel = bean.getData("em_tel");				//긴급연락처
		doc_date = bean.getData("in_date");			//작성년월일
	} //while

	//기간구하기
	if(syear.length() != 0) {
		period_n = anbdt.getPeriodDate(Integer.parseInt(syear),Integer.parseInt(smonth),Integer.parseInt(sdate),Integer.parseInt(edyear),Integer.parseInt(edmonth),Integer.parseInt(eddate));
		period = period_n + 1;
	}

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);		//	  월
		wdate = doc_date.substring(8,10);		//	  일
	}

	//동행자 이름만 구하기
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
		
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String n = name.nextToken();
			if(nm == 1) f_names += n + ",";
			nm++;
			if(nm > 2) break;
		}
	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

%>

<html>
<head><title>출장신청서</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 출장신청서</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<% 	
					//결재문서인 경우 처리
					if(PROCESS.equals("APP_ING")) {  %>
						<% if(doc_ste.equals("APV") || doc_ste.equals("APL")) { //검토, 승인단계 %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='승인'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='반려'></a>
						<% } else { //협조단계 %>
							<a href='Javascript:winDecision();'><img src='../../images/bt_commit_app.gif' align='absmiddle' border='0' alt='합의'></a>
							<a href='Javascript:winReject();'><img src='../../images/bt_reject_app.gif' align='absmiddle' border='0' alt='반려'></a>
						<% } %>
							<a href='Javascript:winprint(<%=pid%>);'><img src='../../images/bt_print.gif' align='absmiddle' border='0' alt='인쇄'></a>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='목록'></a>
					<% } 
					//재작성및 View 문서	
					else { %>
						<% if(PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX")) {	//임시보관함,반려함 처리 %> 
							<a href='Javascript:winRewrite();'><img src='../../images/bt_rewrite.gif' align='absmiddle' border='0' alt='재작성'></a>
							<a href='Javascript:winDelete(<%=pid%>);'><img src='../../images/bt_del.gif' align='absmiddle' border='0' alt='삭제'></a>
						<%	} %>
						
						<!-- 기안자의 상신취소[기안자 바로 앞단계시] : 200408 : 공통 -->
						<% if(app_cancel.equals("Y")) {		//상신취소 %> 
							<a href='Javascript:appCancel(<%=pid%>);'><img src='../../images/bt_app_cancel.gif' align='absmiddle' border='0' alt='상신취소'></a>
						<%	} %>
						<!-- 삭제문서로 전달받을경우 와 그렇지 않을경우 메뉴 : 200408 : 공통 -->
						<% if(PROCESS.equals("DEL_BOX")) {	%>
							<a href='Javascript:winclose();'><img src='../../images/bt_close.gif' align='absmiddle' border='0' alt='닫기'></a>
						<% } else { %>
							<a href='Javascript:winprint(<%=pid%>);'><img src='../../images/bt_print.gif' align='absmiddle' border='0' alt='인쇄'></a>
							<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='목록'></a>
						<% } %>
					<% } %>
	
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=40% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>					
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate.length() == 0)	{//검토자
							if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">소속부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">대상자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">시작일</td>
           <td width="37%" height="25" class="bg_04"><%=syear%>년 <%=smonth%>월 <%=sdate%>일</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">종료일</td>
           <td width="37%" height="25" class="bg_04"><%=edyear%>년 <%=edmonth%>월 <%=eddate%>일</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장일수</td>
           <td width="37%" height="25" class="bg_04"><%=period_n%>박 <%=period%> 일간</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장지</td>
           <td width="37%" height="25" class="bg_04"><%=bistrip_kind%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장목적</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교통편</td>
           <td width="37%" height="25" class="bg_04"><%=traffic_way%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">관련과제</td>
           <td width="37%" height="25" class="bg_04"><%=prj_code%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">동행자</td>
           <td width="37%" height="25" class="bg_04"><%=f_names%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무인수자</td>
           <td width="37%" height="25" class="bg_04"><%=rec%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><%=tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=wyear%>년 <%=wmonth%>월 <%=wdate%>일</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>


<% if(line2.length() != 0) { //2차측 부서 전자결재시만 보여준다 %>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=t_line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=40% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
					<% if(wid2.length() != 0) { %>
						<img src="../../../gw/approval/sign/<%=wid2%>.gif" width=60 height=50 align="center"></td>   
					<% } %>
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate2.length() == 0)	{//검토자
							if(ddate2.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid2 + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate2.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did2 + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname2%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname2%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>
<% } %>

<form action="chuljang_sincheong_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='user_id' value='<%=user_id%>'>
	<input type='hidden' name='prj_code' value='<%=prj_code%>'>
	<input type='hidden' name='fellow_names' value='<%=fellow_names%>'>
	<input type='hidden' name='traffic_way' value='<%=traffic_way%>'>
	<input type='hidden' name='bistrip_kind' value='<%=bistrip_kind%>'>
	<input type='hidden' name='bistrip_country' value='<%=bistrip_country%>'>
	<input type='hidden' name='bistrip_city' value='<%=bistrip_city%>'>
	<input type='hidden' name='purpose' value='<%=purpose%>'>
	<input type='hidden' name='doc_syear' value='<%=syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=sdate%>'>
	<input type='hidden' name='doc_edyear' value='<%=edyear%>'>
	<input type='hidden' name='doc_edmonth' value='<%=edmonth%>'>
	<input type='hidden' name='doc_eddate' value='<%=eddate%>'>
	<input type='hidden' name='doc_receiver' value='<%=rec%>'>
	<input type='hidden' name='doc_tel' value='<%=tel%>'>
<%
	int hrlen = btrip.length-1;
	for(int m=1; m <= hrlen; m++) {
			out.println("<input type='hidden' name='code"+m+"' value='"+btrip[m][0]+"'>");	//항목관리코드
			int item_cost = Integer.parseInt(btrip[m][2]);
			out.println("<input type='hidden' name='cost"+m+"' value='"+money.toDigits(item_cost)+"'>");//비용(금액)
			out.println("<input type='hidden' name='cont"+m+"' value='"+btrip[m][3]+"'></td>");//산출내역
	}

%>
	<input type='hidden' name='receiver_id' value='<%=receiver_id%>'>
	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
</form>  

</body>
</html>

<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=no');
}

//결재하기
function winDecision()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}
//반려하기
function winReject()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//재작성하기
function winRewrite()
{
	var line = '<%=r_line%>';
	var ln = "";
	for(i=0; i<line.length; i++) {
		if(line.charAt(i) == '@') ln += '\n';
		else ln += line.charAt(i);
	}
	
	document.eForm.action = "chuljang_sincheong_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}
//출력하기
function winprint()
{
	wopen('../../../servlet/ApprovalDetailServlet?PID=<%=pid%>&mode=APP_PNT',"print","640","600","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//닫기
function winclose()
{
	if(window.name == 'save_doc') self.close();		//삭제문서 전자우편에서 열때
	else history.go(-1);							//삭제문서 바탕화면에서 열때
}
//상신취소 진행하기
function appCancel(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_CANCEL&PID="+pid;
	document.eForm.submit();
}
//보관,반려문서 삭제하기
function winDelete(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_DELETE&PID="+pid;
	document.eForm.submit();
}
-->
</script>
