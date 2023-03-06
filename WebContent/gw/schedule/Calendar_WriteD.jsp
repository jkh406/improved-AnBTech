<%@ include file="../../admin/configHead.jsp"%>
<%@ page	
	language = "java"
	info= "부서일정 작성하기"		
	contentType = "text/html;charset=euc-kr"  	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
%>
<%@	page import="com.anbtech.text.Hanguel"	%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//login 계정 변수
	String id="";					//login

	//메시지 전달변수
	String Message="";				//메시지 전달 변수  
	String FLAG = "";				//회사/부서일정 구분
	String DIV_ID = "";				//부서일정시 부서ID

	//화면출력용 전달변수
	String[] Aitem;					//일정항목 (한글항목)
	String[] Eitem;					//일정항목 (영문항목 : 내부데이터 처리용)
	int items_cnt=0;				//일정항목 총갯수
	String toDay="";				//오늘 년/월/일 표기
	String wName="";				//등록대상자 명
	String hrs="";					//현재 시각(hh)

	//저장용 변수 (공통)
	String uid="";					//일정소유자 사번
	String una="";					//일정소유자 이름
	String udi="";					//일정소유자 부서명

	String eop="";					//공개여부
	String eit="";					//일정항목

	String sub="";					//제목
	String con="";					//회의내용
	String mse="";					//통지선택사항

	//저장용 변수 (옵션:일정항목별로)
	String mna="";					//주제자명(회의,교육)
	String hda="";					//기념일
	String mro="";					//장소			(회의,행사,출장,근무지,약속,교육)
	String mte="";					//전화번호		(회의,출장,휴가,약속,교육)

	String prs="";					//참석자		(회의,약속)
	
	String mye="";					//시작시간 년	(회의,약속,행사,휴가,출장,기념,기타,교육)
	String mmo="";					//시작시간 월	(회의,약속,행사,휴가,출장,기념,기타,교육)
	String mda="";					//시작시간 일	(회의,약속,행사,휴가,출장,기념,기타,교육)

	String mti="";					//전체 시간		(회의,약속,행사,휴가,출장,기념,기타,교육)

	String eye="";					//종료시간 년	(회의,약속,행사,휴가,출장,기념,기타)
	String emo="";					//종료시간 월	(회의,약속,행사,휴가,출장,기념,기타)
	String eda="";					//종료시간 일	(회의,약속,행사,휴가,출장,기념,기타)

	//저장할 쿼리문자
	String DATA="";

stop : {
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	id = login_id; 			//접속자

	//부서일정 관리자 정보
	String[] u_dbColumn={"a.id","a.name","b.ac_name"};	
	String query = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(u_dbColumn);
	bean.setSearchWrite(query);
	bean.init_write();
	String wdn = "";
	while(bean.isAll()) wdn = bean.getData("ac_name");	//작성자 부서명 찾기
		
	//부서일정 처리하기
	String flag= request.getParameter("FLAG");				//회사/부서 일정구분
	String div_id = request.getParameter("Sabun");			//부서 코드
	if(div_id != null) {
		FLAG = "DIV";				//부서일정으로 무조건 DIV
		DIV_ID = div_id;			//부서 관리 코드
	}
	wName = "부서일정/"+wdn;

	//인수 초기화
	uid=una=udi=eop=eit=sub=mro=mna=mte=prs=mse=mye=mmo=mda=mti=con="";
	hda=eye=emo=eda=hrs="";

	/*****************************************************
	//화면출력용 일정항목 (목록을 읽어 배열에 담는다.)
	*****************************************************/
	String[] itemColumns = {"id","item","nlist"};
	String item_data = "where (item='CIT') or (item='IIT' and id='" + id + "')"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(item_data);
	bean.init_write();
	
	String items = "";					//전체 항목LIST (구분자 : ';')
	while(bean.isAll()) {
		items += bean.getData("nlist");
	}	

	//총 항목갯수 구하기
	items_cnt = 0;						//항목갯수
	for(int ii = 0; ii < items.length(); ii++)
		if(items.charAt(ii) == ';') items_cnt++;

	//배열에 항목담기
	Aitem = new String[items_cnt];				//배열할당
	Eitem = new String[items_cnt];				//배열할당	
	int aj = 0;						//복사 시작점
	int ak = 0;						//배열 시작수
	for(int ai = 0; ai < items.length(); ai++) {
		if(items.charAt(ai) == ';') {
			Aitem[ak] = items.substring(aj,ai);
			if(items.substring(aj,ai).equals("회의")) Eitem[ak] = "Meeting";
			else if(items.substring(aj,ai).equals("약속")) Eitem[ak] = "Appointment";
			else if(items.substring(aj,ai).equals("행사")) Eitem[ak] = "Event";
			else if(items.substring(aj,ai).equals("국내출장")) Eitem[ak] = "TripH";
			else if(items.substring(aj,ai).equals("해외출장")) Eitem[ak] = "TripA";
			else if(items.substring(aj,ai).equals("휴가")) Eitem[ak] = "Vacation";
			else if(items.substring(aj,ai).equals("기념일")) Eitem[ak] = "Holiday";
			else if(items.substring(aj,ai).equals("방문")) Eitem[ak] = "Area";
			else if(items.substring(aj,ai).equals("교육/세미나")) Eitem[ak] = "Education";
			else Eitem[ak] = "Etc"+ak;

			aj = ai+1;				//복사시작점
			ak++;
		}
	}

	/*****************************************************
	// 금일의 년월일 및 시각 구하기 
	*****************************************************/
	//년월일 구하기
	String tD = request.getParameter("DAY");	//from calendar_view.jsp 달력의 날자를 누르면
	if(tD == null)
		toDay = anbdt.getDate(0);
	else toDay = tD;
	
	//현재시 구하기
	hrs = anbdt.getHours();			//HH
	
	/*****************************************************
	//저장할 내용 읽기 
	*****************************************************/
	String Username = request.getParameter("hdUserName");
	if((Username == null) || (Username.length() == 0)) break stop;
	else {
		//--------------------------------------------------------------------------------	
		//부서일정 공통항목 읽기
		//--------------------------------------------------------------------------------	
		eop=Hanguel.toHanguel(request.getParameter("hdOpen"));							//공개여부
		String ditem=Hanguel.toHanguel(request.getParameter("hdAppointmentType"));		//일정항목
	
		for(int si = 0; si < ak; si++) {
			if(ditem.equals("Meeting")) eit = "회의";
			else if(ditem.equals("Appointment")) eit = "약속";
			else if(ditem.equals("Event")) eit = "행사";
			else if(ditem.equals("TripH")) eit = "국내출장";
			else if(ditem.equals("TripA")) eit = "해외출장";
			else if(ditem.equals("Vacation")) eit = "휴가";
			else if(ditem.equals("Holiday")) eit = "기념일";
			else if(ditem.equals("Area")) eit = "방문";
			else if(ditem.equals("Education")) eit = "교육/세미나";
			else {
				int ETC = Integer.parseInt(ditem.substring(3,ditem.length()));
				eit = Aitem[ETC];
			}
	
		} //for

		sub=Hanguel.toHanguel(request.getParameter("hdSubject"));						//제목
		con=Hanguel.toHanguel(request.getParameter("Body"));							//회의내용

		//--------------------------------------------------------------------------------	
		//옵션항목 읽기 (일정항목별로 다름)
		//--------------------------------------------------------------------------------	
		if(ditem.equals("Meeting")) {			//회의
			mro=Hanguel.toHanguel(request.getParameter("hdLocation"));					//회의장소
			mna=Hanguel.toHanguel(request.getParameter("hdChair"));						//회의 주제자명
			mte=Hanguel.toHanguel(request.getParameter("hdChairTel"));					//회의 주제자 전화번호

			String Meetdate = request.getParameter("hdMeetDate");
			if(Meetdate != null) {
				int MFirst = Meetdate.indexOf("/");
				int MLast = Meetdate.lastIndexOf("/");
				mye=Hanguel.toHanguel(Meetdate.substring(0,MFirst));					//회의시간 년
				mmo=Hanguel.toHanguel(Meetdate.substring(MFirst+1,MLast));				//회의시간 월
				mda=Hanguel.toHanguel(Meetdate.substring(MLast+1,Meetdate.length()));	//회의시간 일
			}
			String StartTime = request.getParameter("hdStartTime");
			String EndTime = request.getParameter("hdEndTime");
			mti=StartTime + "~" + EndTime;												//회의시간 시간
		} else if(ditem.equals("Appointment")) { //약속
			prs=Hanguel.toHanguel(request.getParameter("hdMeetUsers"));					//약속대상자
			String Appointdate = request.getParameter("hdAppointDate");
			int AFirst = Appointdate.indexOf("/");
			int ALast = Appointdate.lastIndexOf("/");
			mte=Hanguel.toHanguel(request.getParameter("hdAppointTel"));				//연락처
			mye=Hanguel.toHanguel(Appointdate.substring(0,AFirst));						//약속시간 년
			mmo=Hanguel.toHanguel(Appointdate.substring(AFirst+1,ALast));				//약속시간 월
			mda=Hanguel.toHanguel(Appointdate.substring(ALast+1,Appointdate.length()));	//약속시간 일
			mti=request.getParameter("hdAppointTime");									//약속시간
			mro=Hanguel.toHanguel(request.getParameter("hdAppointLocation"));			//약속장소
		} else if(ditem.equals("Event")) {		//행사
			mro=Hanguel.toHanguel(request.getParameter("hdEventLocation"));				//행사장소
			String Eventdate = request.getParameter("hdEventDate");
			int EFirst = Eventdate.indexOf("/");
			int ELast = Eventdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Eventdate.substring(0,EFirst));						//행사시작시간 년
			mmo=Hanguel.toHanguel(Eventdate.substring(EFirst+1,ELast));					//행사시작시간 월
			mda=Hanguel.toHanguel(Eventdate.substring(ELast+1,Eventdate.length()));		//행사시작시간 일

			String Eventdate_1 = request.getParameter("hdEventDate_1");
			int EFirst_1 = Eventdate_1.indexOf("/");
			int ELast_1 = Eventdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Eventdate_1.substring(0,EFirst_1));					//행사끝시간 년
			emo=Hanguel.toHanguel(Eventdate_1.substring(EFirst_1+1,ELast_1));			//행사끝시간 월
			eda=Hanguel.toHanguel(Eventdate_1.substring(ELast_1+1,Eventdate_1.length()));//행사끝시간 일

			String eStartTime = request.getParameter("hdEventTime");
			String eEndTime = request.getParameter("hdEventTime_1");
//			mti=eStartTime + "~" + eEndTime;											//행사시간 시간
			if(mmo.equals(emo) && mda.equals(eda))										//금일중일때 시간만
				mti = eStartTime + "~" + eEndTime;
			else																//금일중아니면 날자만 표기
				mti=eStartTime + "~" + "[" + eda + "]" + eEndTime;
		} else if(ditem.equals("Vacation")) {		//휴가
			mte=Hanguel.toHanguel(request.getParameter("hdUrgencyTel"));			//긴급연락처 전화번호
			String Vacationdate = request.getParameter("hdVacationDate");
			int VFirst = Vacationdate.indexOf("/");
			int VLast = Vacationdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Vacationdate.substring(0,VFirst));					//휴가시작시간 년
			mmo=Hanguel.toHanguel(Vacationdate.substring(VFirst+1,VLast));				//휴가시작시간 월
			mda=Hanguel.toHanguel(Vacationdate.substring(VLast+1,Vacationdate.length()));//휴가시작시간 일

			String Vacationdate_1 = request.getParameter("hdVacationDate_1");
			int VFirst_1 = Vacationdate_1.indexOf("/");
			int VLast_1 = Vacationdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Vacationdate_1.substring(0,VFirst_1));				//휴가끝시간 년
			emo=Hanguel.toHanguel(Vacationdate_1.substring(VFirst_1+1,VLast_1));		//휴가끝시간 월
			eda=Hanguel.toHanguel(Vacationdate_1.substring(VLast_1+1,Vacationdate_1.length()));	//휴가끝시간 일

			String vStartTime = request.getParameter("hdVaTime");
			String vEndTime = request.getParameter("hdVaTime_1");
//			mti=vStartTime + "~" + vEndTime;											//휴가시간 
			mti=mmo + "/" + mda + "~" + emo + "/" + eda;								//휴가기간
		} else if(ditem.equals("TripH")) {			//국내출장
			mro=Hanguel.toHanguel(request.getParameter("hdTripLoc"));					//출장처
			mte=Hanguel.toHanguel(request.getParameter("hdTripTel"));					//긴급연락처 전화번호

			String Tripdate = request.getParameter("hdTripDate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//출장시작시간 년
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//출장시작시간 월
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//출장시작시간 일

			String Tripdate_1 = request.getParameter("hdTripDate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//출장끝시간 년
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//출장끝시간 월
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//출장끝시간 일

			String tStartTime = request.getParameter("hdTripTime");
			String tEndTime = request.getParameter("hdTripTime_1");
//			mti=tStartTime + "~" + tEndTime;											//출장시간 시간
			if(mmo.equals(emo) && mda.equals(eda))							//금일중일 경우는 시간만
				mti = tStartTime + "~" + tEndTime;
			else															//금일중이 아닌경우는 날자만 표기
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
		} else if(ditem.equals("TripA")) {			//해외출장
			mro=Hanguel.toHanguel(request.getParameter("hdTripALoc"));					//출장처
			mte=Hanguel.toHanguel(request.getParameter("hdTripATel"));					//긴급연락처 전화번호

			String Tripdate = request.getParameter("hdTripADate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//출장시작시간 년
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//출장시작시간 월
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//출장시작시간 일

			String Tripdate_1 = request.getParameter("hdTripADate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//출장끝시간 년
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//출장끝시간 월
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//출장끝시간 일

			String tStartTime = request.getParameter("hdTripATime");
			String tEndTime = request.getParameter("hdTripATime_1");
//			mti=tStartTime + "~" + tEndTime;											//출장시간 시간
			if(mmo.equals(emo) && mda.equals(eda))							//금일중일 경우는 시간만
				mti = tStartTime + "~" + tEndTime;
			else															//금일중이 아닌경우는 날자만 표기
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
		} else if(ditem.equals("Holiday")) {		//기념일
			hda=Hanguel.toHanguel(request.getParameter("hdHolidayDate"));				//기념일자
			mye=hda.substring(0,hda.indexOf("/"));										//기념시작시간 년
			mmo=hda.substring(hda.indexOf("/")+1,hda.lastIndexOf("/"));					//기념시작시간 월
			mda=hda.substring(hda.lastIndexOf("/")+1,hda.length());						//기념시작시간 일
			mti=request.getParameter("hdHolidayTime");									//시간
		} else if(ditem.equals("Area")) {			//방문
			mro=Hanguel.toHanguel(request.getParameter("hdArea"));					//방문처
			mte=Hanguel.toHanguel(request.getParameter("hdAreaTel"));					//긴급연락처 전화번호

			String Tripdate = request.getParameter("hdAreaDate");
			int TFirst = Tripdate.indexOf("/");
			int TLast = Tripdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Tripdate.substring(0,TFirst));						//방문시작시간 년
			mmo=Hanguel.toHanguel(Tripdate.substring(TFirst+1,TLast));					//방문시작시간 월
			mda=Hanguel.toHanguel(Tripdate.substring(TLast+1,Tripdate.length()));		//방문시작시간 일

			String Tripdate_1 = request.getParameter("hdAreaDate_1");
			int TFirst_1 = Tripdate_1.indexOf("/");
			int TLast_1 = Tripdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Tripdate_1.substring(0,TFirst_1));					//방문끝시간 년
			emo=Hanguel.toHanguel(Tripdate_1.substring(TFirst_1+1,TLast_1));			//방문끝시간 월
			eda=Hanguel.toHanguel(Tripdate_1.substring(TLast_1+1,Tripdate_1.length()));	//방문끝시간 일

			String tStartTime = request.getParameter("hdAreaTime");
			String tEndTime = request.getParameter("hdAreaTime_1");
			if(mmo.equals(emo) && mda.equals(eda))							//금일중일 경우는 시간만
				mti = tStartTime + "~" + tEndTime;
			else															//금일중이 아닌경우는 날자만 표기
				mti=tStartTime + "~" + "[" + eda + "]" + tEndTime;
/*
			//시간은 금일 날자로 입력
			mye=anbdt.getYear();														//방문시작시간 년
			mmo=anbdt.getMonth();														//방문시작시간 월
			mda=anbdt.getDates();														//방문시작시간 일
			mro=Hanguel.toHanguel(request.getParameter("hdArea"));						//방문지
			mti=mro;																	//방문지 표기
*/
		} else if(ditem.equals("Education")) {			//교육/세미나
			mro=Hanguel.toHanguel(request.getParameter("hdEduLocation"));				//교육장소
			mna=Hanguel.toHanguel(request.getParameter("hdEduChair"));					//교육주관자 명
			mte=Hanguel.toHanguel(request.getParameter("hdEduChairTel"));				//전화번호

			String Educationdate = request.getParameter("hdEducationDate");
			if(Educationdate != null) {
				int MFirst = Educationdate.indexOf("/");
				int MLast = Educationdate.lastIndexOf("/");
				mye=Hanguel.toHanguel(Educationdate.substring(0,MFirst));						//년
				mmo=Hanguel.toHanguel(Educationdate.substring(MFirst+1,MLast));					//월
				mda=Hanguel.toHanguel(Educationdate.substring(MLast+1,Educationdate.length()));	//일
			}
			String StartTime = request.getParameter("hdEduStartTime");
			String EndTime = request.getParameter("hdEduEndTime");
			mti=StartTime + "~" + EndTime;	
		} else  {									//기타
			String Etcdate = request.getParameter("hdEtcDate");
			int EtcFirst = Etcdate.indexOf("/");
			int EtcLast = Etcdate.lastIndexOf("/");
			mye=Hanguel.toHanguel(Etcdate.substring(0,EtcFirst));						//기타시작시간 년
			mmo=Hanguel.toHanguel(Etcdate.substring(EtcFirst+1,EtcLast));				//기타시작시간 월
			mda=Hanguel.toHanguel(Etcdate.substring(EtcLast+1,Etcdate.length()));		//기타시작시간 일

			String Etcdate_1 = request.getParameter("hdEtcDate_1");
			int EtcFirst_1 = Etcdate_1.indexOf("/");
			int EtcLast_1 = Etcdate_1.lastIndexOf("/");
			eye=Hanguel.toHanguel(Etcdate_1.substring(0,EtcFirst_1));					//기타끝시간 년
			emo=Hanguel.toHanguel(Etcdate_1.substring(EtcFirst_1+1,EtcLast_1));			//기타끝시간 월
			eda=Hanguel.toHanguel(Etcdate_1.substring(EtcLast_1+1,Etcdate_1.length()));	//기타끝시간 일

			String etcStartTime = request.getParameter("hdEtcTime");
			String etcEndTime = request.getParameter("hdEtcTime_1");
//			mti=etcStartTime + "~" + etcEndTime;										//기타시간 시간
			if(mmo.equals(emo) && mda.equals(eda))							//금일중일 경우는 시간만
				mti = etcStartTime + "~" + etcEndTime;
			else															//금일중이 아닌경우는 날자만 표기
				mti=etcStartTime + "~" + "[" + eda + "]" + etcEndTime;

		}
		//---------------------------------------------------------------
		//	DB저장을 위한 특수문자 바꾸기 (' -> `) 
		//---------------------------------------------------------------
		sub = str.quoteReplace(sub);		//제목
		mro = str.quoteReplace(mro);		//장소
		mna = str.quoteReplace(mna);		//담당자
		mte = str.quoteReplace(mte);		//전화번호
		con = str.quoteReplace(con);		//내용
		//---------------------------------------------------------------
		//부서일정 저장하기
		//---------------------------------------------------------------
		//부서일정 처리하기
		uid = DIV_ID;					//부서ID
		una = company_name;				//회사명
		udi = wdn;						//부서명

		DATA = "INSERT INTO CALENDAR_SCHEDULE(pid,id,name,division,isopen,item,sub,mroom,mname,mtel,presents,isselect,myear,mmonth,mday,mtime,content,hday,eyear,emonth,eday,idate)";
		      DATA += " VALUES('" + bean.getID() + "','" + uid + "','" + una + "','" + udi + "','" + eop + "','" + eit + "','" + sub + "','";
		      DATA += mro + "','" + mna + "','" + mte + "','" + prs + "','" + mse + "','" + mye + "','" + mmo + "','" + mda + "','" + mti + "','";
			  DATA += con + "','" + hda + "','" + eye + "','" + emo + "','" + eda + "','" + bean.getTime() + "')";
		//out.println(DATA); 
		try { bean.execute(DATA);	Message="INSERT"; 	} catch (Exception e) { out.println(e);}
	} //if

} //stop

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif"> 부서일정등록</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5></TD>
			  <TD align=left width=30><a href="javascript:okClick();"><img src="../images/bt_save.gif" border="0"></a></TD>
			  <TD width=4></TD>
			  <TD align=left width=30><a href="Calendar_View.jsp?FLAG=IND&Sabun=<%=login_id%>"><img src="../images/bt_cancel.gif" border="0"></a></TD>
			  <TD width=4></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><FORM METHOD=post ACTION="Calendar_WriteD.jsp" NAME="_hdForm1" style="margin:0">
	<!--기본정보-->
	<div id="Main" style="left:0px;visiblity:visible">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">등록구분</td>
           <td width="37%" height="25" class="bg_04">개인일정</td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">등록대상</td>
           <td width="37%" height="25" class="bg_04"><%=wName%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">일정항목</td>
           <td width="37%" height="25" class="bg_04">
				<table border=0><tr><td valign=middle width=50%>
				<SELECT NAME="hdAppointmentType" onChange="var sourceitem = document.forms[0].hdAppointmentType;
					var tmpIndex = sourceitem.selectedIndex;
					var tmpValue = sourceitem.options[tmpIndex].value;
					if (document.forms[0].hdDocType.value == &quot;&quot;) { //새문서인 경우
						document.forms[0].hdIndex.value = tmpIndex;
						show_menu(tmpValue);
					} else {
						alert (&quot;일정항목은 변경할 수 없습니다.&quot;);
						tmpPreIndex = document.forms[0].hdIndex.value;
						sourceitem.value = sourceitem.options[tmpPreIndex].value;
					}" CLASS="select">
				<%
					for(int si = 0 ; si < items_cnt; si++) {
						String SEL = "";
						if(si == 0) SEL = " SELECTED";
						else SEL = "";
						out.println("<OPTION" + SEL + " VALUE=" + Eitem[si] + ">" + Aitem[si]);
					}
				%>
				</SELECT></td><td valign=middle width=50%><a href="javascript:wopen('Calendar_addItem.jsp','manager_item','380','200')"><img src="../images/bt_add.gif" border="0" alt="일정항목을 추가 또는 삭제합니다."></a></td></tr></table></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">공개여부</td>
           <td width="37%" height="25" class="bg_04"><INPUT TYPE=radio NAME="hdOpen" VALUE="1" CHECKED>공개	<INPUT TYPE=radio NAME="hdOpen" VALUE="0">비공개</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">제목</td>
           <td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdSubject" VALUE="" size=50 class='text_01'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table></div>

	<!-- 회의 -->
	<div id="Meeting" class="expanded">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">회의장소</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdLocation" VALUE="" class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">회의시간</td>
           <td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdMeetDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdMeetDate', '', 180, 250)"><img src="../images/bt_calendar.gif" border="0" align='absmiddle'></A>
				<SELECT NAME="hdStartTime">
				<%
				String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=0; asH<24; asH++){
						if(asH == Integer.parseInt(hrs)) msSEL = "SELECTED"; else msSEL="";
						out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "00");
						out.println("<OPTION>" + asHour[asH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEndTime">
				<%
				String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String meSEL = "";
					for(int aeH=0; aeH<24; aeH++){
						if(aeH == Integer.parseInt(hrs)) meSEL = "SELECTED"; else meSEL="";
						out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "00");
						out.println("<OPTION>" + aeHour[aeH] + ":" + "30");
					}
				%></SELECT></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">담당자명</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdChair" VALUE="" class='text_01'></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">담당자연락처</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdChairTel" VALUE="" size="10"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>


	<!-- 약속 -->
	<div id="Appointment" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">약속대상</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdMeetUsers" VALUE=""></td>
           <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">연락처</td>
           <td width="37%" height="25" class="bg_04"><INPUT NAME="hdAppointTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">약속시간</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAppointDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAppointDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAppointTime">
				<%
					String[] aptHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String aSEL = "";
					for(int aptH=0; aptH<24; aptH++){
						if(aptH == Integer.parseInt(hrs)) aSEL = "SELECTED"; else aSEL="";
						out.println("<OPTION " + aSEL + ">" + aptHour[aptH] + ":" + "00");
						out.println("<OPTION>" + aptHour[aptH] + ":" + "30");
					}
				%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">약속장소</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdAppointLocation" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- 행사 -->
	<div id="Event" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEventDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEventDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime">
					<%
						String[] edHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edSEL = "";
						for(int edH=0; edH<24; edH++){
							if(edH == Integer.parseInt(hrs)) edSEL = "SELECTED"; else edSEL="";
							out.println("<OPTION " + edSEL + ">" + edHour[edH] + ":" + "00");
							out.println("<OPTION>" + edHour[edH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEventDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEventDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEventTime_1">
					<%
						String[] edeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String edeSEL = "";
						for(int edeH=0; edeH<24; edeH++){
							if(edeH == Integer.parseInt(hrs)) edeSEL = "SELECTED"; else edeSEL="";
							out.println("<OPTION " + edeSEL + ">" + edeHour[edeH] + ":" + "00");
							out.println("<OPTION>" + edeHour[edeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">행사장소</td>
			<td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdEventLocation" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- 휴가 -->
	<div id="Vacation" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdVacationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdVacationDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime">
					<%
						String[] vdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdSEL = "";
						for(int vdH=0; vdH<24; vdH++){
							if(vdH == Integer.parseInt(hrs)) vdSEL = "SELECTED"; else vdSEL="";
							out.println("<OPTION " + vdSEL + ">" + vdHour[vdH] + ":" + "00");
							out.println("<OPTION>" + vdHour[vdH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdVacationDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdVacationDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdVaTime_1">
					<%
						String[] vdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String vdeSEL = "";
						for(int vdeH=0; vdeH<24; vdeH++){
							if(vdeH == Integer.parseInt(hrs)) vdeSEL = "SELECTED"; else vdeSEL="";
							out.println("<OPTION " + vdeSEL + ">" + vdeHour[vdeH] + ":" + "00");
							out.println("<OPTION>" + vdeHour[vdeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">긴급연락처</td>
			<td width="87%" height="25" class="bg_04" colspan="3"><INPUT NAME="hdUrgencyTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- 국내출장 -->
	<div id="TripH" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime">
					<%
						String[] tdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdSEL = "";
						for(int tdH=0; tdH<24; tdH++){
							if(tdH == Integer.parseInt(hrs)) tdSEL = "SELECTED"; else tdSEL="";
							out.println("<OPTION " + tdSEL + ">" + tdHour[tdH] + ":" + "00");
							out.println("<OPTION>" + tdHour[tdH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripTime_1">
					<%
						String[] tdeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeSEL = "";
						for(int tdeH=0; tdeH<24; tdeH++){
							if(tdeH == Integer.parseInt(hrs)) tdeSEL = "SELECTED"; else tdeSEL="";
							out.println("<OPTION " + tdeSEL + ">" + tdeHour[tdeH] + ":" + "00");
							out.println("<OPTION>" + tdeHour[tdeH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">국내 출장처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripLoc" VALUE=""></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">긴급연락처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripTel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- 해외출장 -->
	<div id="TripA" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripADate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripADate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime">
					<%
						String[] tdAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdASEL = "";
						for(int tdAH=0; tdAH<24; tdAH++){
							if(tdAH == Integer.parseInt(hrs)) tdASEL = "SELECTED"; else tdASEL="";
							out.println("<OPTION " + tdASEL + ">" + tdAHour[tdAH] + ":" + "00");
							out.println("<OPTION>" + tdAHour[tdAH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdTripADate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdTripADate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdTripATime_1">
					<%
						String[] tdeAHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeASEL = "";
						for(int tdeAH=0; tdeAH<24; tdeAH++){
							if(tdeAH == Integer.parseInt(hrs)) tdeASEL = "SELECTED"; else tdeASEL="";
							out.println("<OPTION " + tdeASEL + ">" + tdeAHour[tdeAH] + ":" + "00");
							out.println("<OPTION>" + tdeAHour[tdeAH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">해외 출장처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripALoc" VALUE=""></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">긴급연락처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdTripATel" VALUE=""></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- 기념일 -->
	<div id="Holiday" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">기념일자</td>
			<td width="87%" height="25" class="bg_04">
			<INPUT NAME="hdHolidayDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdHolidayDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
			<SELECT NAME="hdHolidayTime">
				<%
					String[] hdHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String hdSEL = "";
					for(int hdH=0; hdH<24; hdH++){
						if(hdH == Integer.parseInt(hrs)) hdSEL = "SELECTED"; else hdSEL="";
						out.println("<OPTION " + hdSEL + ">" + hdHour[hdH] + ":" + "00");
						out.println("<OPTION>" + hdHour[hdH] + ":" + "30");
					}
				%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- 방문 -->
	<div id="Area" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAreaDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAreaDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime">
					<%
						String[] tdLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdLSEL = "";
						for(int tdLH=0; tdLH<24; tdLH++){
							if(tdLH == Integer.parseInt(hrs)) tdLSEL = "SELECTED"; else tdLSEL="";
							out.println("<OPTION " + tdLSEL + ">" + tdLHour[tdLH] + ":" + "00");
							out.println("<OPTION>" + tdLHour[tdLH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdAreaDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdAreaDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdAreaTime_1">
					<%
						String[] tdeLHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String tdeLSEL = "";
						for(int tdeLH=0; tdeLH<24; tdeLH++){
							if(tdeLH == Integer.parseInt(hrs)) tdeLSEL = "SELECTED"; else tdeLSEL="";
							out.println("<OPTION " + tdeLSEL + ">" + tdeLHour[tdeLH] + ":" + "00");
							out.println("<OPTION>" + tdeLHour[tdeLH] + ":" + "30");
						}
					%></SELECT></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">방문지</td>
			<td width="37%" height="25" class="bg_04">
			<INPUT NAME="hdArea" VALUE="" class='text_01'></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">긴급연락처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdAreaTel" VALUE=""></td></tr>
		 <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></Table></div>

	<!-- 교육/세미나 -->
	<div id="Education" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">교육기간</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEducationDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEducationDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEduStartTime">
				<%
				String[] edusHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String emsSEL = "";
					for(int easH=0; easH<24; easH++){
						if(easH == Integer.parseInt(hrs)) emsSEL = "SELECTED"; else emsSEL="";
						out.println("<OPTION " + emsSEL + ">" + edusHour[easH] + ":" + "00");
						out.println("<OPTION>" + edusHour[easH] + ":" + "30");
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEduEndTime">
				<%
				String[] edueHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String emeSEL = "";
					for(int eaeH=0; eaeH<24; eaeH++){
						if(eaeH == Integer.parseInt(hrs)) emeSEL = "SELECTED"; else emeSEL="";
						out.println("<OPTION " + emeSEL + ">" + edueHour[eaeH] + ":" + "00");
						out.println("<OPTION>" + edueHour[eaeH] + ":" + "30");
					}
				%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">교육장소</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduLocation" VALUE="" class='text_01'></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">교육주관</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduChair" VALUE="" class='text_01'></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">담당자 연락처</td>
			<td width="37%" height="25" class="bg_04"><INPUT NAME="hdEduChairTel" VALUE="" size="10"></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- 기타 -->
	<div id="Etc" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">시작일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEtcDate" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEtcDate', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime">
					<%
						String[] etHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String etSEL = "";
						for(int etH=0; etH<24; etH++){
							if(etH == Integer.parseInt(hrs)) etSEL = "SELECTED"; else etSEL="";
							out.println("<OPTION " + etSEL + ">" + etHour[etH] + ":" + "00");
							out.println("<OPTION>" + etHour[etH] + ":" + "30");
						}
					%></SELECT></td>
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">종료일</td>
			<td width="37%" height="25" class="bg_04">
				<INPUT NAME="hdEtcDate_1" VALUE="<%=toDay%>" SIZE=10 READONLY><A Href="javascript:wopen('Calendar.jsp?FieldName=hdEtcDate_1', '', 180, 250);"><img src="../images/bt_calendar.gif" border="0" align="absmiddle"></A>
				<SELECT NAME="hdEtcTime_1">
					<%
						String[] eteHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
						String eteSEL = "";
						for(int eteH=0; eteH<24; eteH++){
							if(eteH == Integer.parseInt(hrs)) eteSEL = "SELECTED"; else eteSEL="";
							out.println("<OPTION " + eteSEL + ">" + eteHour[eteH] + ":" + "00");
							out.println("<OPTION>" + eteHour[eteH] + ":" + "30");
						}
					%></SELECT></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

	<!-- 내용 -->
	<div id="comment" style="left:0px;visiblity:visible;">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		<tr> 
			<td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">내용</td>
			<td width="87%" height="25" class="bg_04">
				<TEXTAREA NAME="Body" ROWS=7 COLS=70 WRAP=VIRTUAL></TEXTAREA></td></tr>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr></tbody></table></div>

</td></tr></table>
<INPUT type="hidden" NAME="hdUserName" VALUE="<%=wName%>">
<input Type="hidden" Name="hdMembersInfo" value="">
<input Type="hidden" Name="hdDocType" value="">
<INPUT NAME="hdIndex" VALUE="0" Type=hidden>
<input type='hidden' name='Sabun' value='<%=DIV_ID%>'></form>
</body>
</html>


<% if(Message == "INSERT"){ %>
<script>
	var FLAG = '<%=FLAG%>';
	if((FLAG.indexOf("COM") == -1) && (FLAG.indexOf("DIV") == -1))
		document.location.href="Calendar_View.jsp";
</script>
<% Message = "" ; } %>

<!-- 자바스크립트 -->
<Script Language="Javascript">
<!--

window.onload = setField;
function setField() {
	document.forms[0].hdSubject.focus();
}// -->

//등록된 부서 일정LIST 보기
function ListView()
{	
	var data = "Sabun="+'<%=DIV_ID%>'+"&Date="+'<%=toDay%>';	window.open("Calendar_divList.jsp?"+data,"divList","width=500,height=500,scrollbars=yes,toolbar=no,menubar=no,resizable=yes");
}

//항목추가하기
function addItem()
{
	window.open("Calendar_addItem.jsp","addItem","width=380,height=200,scrollbars=no,toolbar=no,menubar=no");

}
//----------------각 일정항목별 내용표시---------------------
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
      document.layers[menuname].visibility="hide";
  } else {
      document.all[menuname].className="collapsed"
   }
}
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
       document.layers[menuname].visibility="show";
  } else {
       document.all[menuname].className="expanded"
  }
}
//가장먼저 수행
function show_menu( menuname )
{
   var obj = document.forms[0].hdAppointmentType;
   var maxItem = obj.length;
   var ItemArray = new Array(maxItem);
   
   for (var k=0; k < maxItem; k++) {
   		ItemArray[k] = obj.options[k].value;
   }

   for ( var i = 0; i < maxItem; i++ ) {
	  if ( menuname == ItemArray[i]  ) {
	     var tmpRt = checkEtc(menuname);
      	     show( tmpRt );
	     if (tmpRt == "Etc") { return false;}
      } else{
   	     var tmpRt = checkEtc(ItemArray[i]);
      	     hide( tmpRt );
      }
  }
}
function checkEtc( menuname )
{
	if (menuname != "Meeting" && menuname != "Appointment" && menuname != "Event" && menuname != "TripH" && menuname != "TripA" && menuname != "Vacation" && menuname != "Holiday" && menuname != "Area" && menuname != "Education") {
		return ("Etc");
	} else {
		return (menuname);
	}
}
//---------------------------------------------------------
//내용 저장하기
function okClick() {
     var cform = document.forms[0];
     var action = cform.hdAppointmentType.value;
     var altmsg = "";
     now = new Date();
     var nowday = Date.UTC(now.getYear(), now.getMonth(), now.getDate());
     if (confirm("등록하시겠습니까?")) {
           if (cform.hdDocType.value=="") {
                 if (cform.hdUserName.value == "") {
                      alert ("등록대상자를 입력하십시오."); 
                      cform.hdUserName.focus();
                      return;
                 } 
           }

           if (cform.hdAppointmentType.value != "Area" && cform.hdSubject.value == "") {
                 alert ("제목을 입력하십시오."); 
                 cform.hdSubject.focus();
                 return;
           } else {              
	             if (action == "Meeting") {
                       if (cform.hdLocation.value == "") {
							alert("회의 장소를 입력하십시오.");
							return;
					  }
                      if (cform.hdChair.value == "") {
						 alert("담당자를 입력하십시오.");
							return;

					  }
                     
						 var startTimeHour = cform.hdStartTime.options[cform.hdStartTime.selectedIndex].text.substring(0,2); 
						 var startTimeMin = cform.hdStartTime.options[cform.hdStartTime.selectedIndex].text.substring(3,5); 
						 var endTimeHour = cform.hdEndTime.options[cform.hdEndTime.selectedIndex].text.substring(0,2); 
						 var endTimeMin = cform.hdEndTime.options[cform.hdEndTime.selectedIndex].text.substring(3,5); 
						 if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
								 alert("회의 시작시간이 종료시간 보다 이후입니다.\n\n회의 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
								 return;
						 }
                               		
                 } else if (action == "Event") {
	                      var timevalue = cform.hdEventDate.value;
	                      var eventday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
     	                  var ceventday = Date.UTC(eventday.getYear(), eventday.getMonth(), eventday.getDate());
                          var timevalue_1 = cform.hdEventDate_1.value;
                          var eventday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                          var ceventday_1 = Date.UTC(eventday_1.getYear(), eventday_1.getMonth(), eventday_1.getDate());  
	                      if (ceventday > ceventday_1) {
		                          alert("행사 시작일이 종료일 이후로 설정되었습니다.\n\n행사 종료일을 시작일보다 이후로 설정하시기 바랍니다.");
          		                  return;
	                      } else if (ceventday == ceventday_1) {
		                          var startTimeHour = cform.hdEventTime.options[cform.hdEventTime.selectedIndex].text.substring(0,2); 
		                          var startTimeMin = cform.hdEventTime.options[cform.hdEventTime.selectedIndex].text.substring(3,5); 
		                          var endTimeHour = cform.hdEventTime_1.options[cform.hdEventTime_1.selectedIndex].text.substring(0,2); 
		                          var endTimeMin = cform.hdEventTime_1.options[cform.hdEventTime_1.selectedIndex].text.substring(3,5); 
		                          if (parseInt(startTimeHour,10) > parseInt(endTimeHour,10)) 	{
				                          alert("행사 시작시간이 종료시간 보다 이후입니다.\n\n행사 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
				                          return;
		                          } 
		                          if (parseInt(startTimeHour,10) == parseInt(endTimeHour,10) && parseInt(startTimeMin,10) > parseInt(endTimeMin,10)) {
				                           alert("행사 시작시간이 종료시간 보다 이후입니다.\n\n행사 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
				                           return;
		                          }		
	                     }	
               } else if (action == "Vacation") {
                       var timevalue = cform.hdVacationDate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdVacationDate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());                    
		               if (cvacationday > cvacationday_1) {
		                       alert("휴가/출장 시작일이 종료일보다 크게 설정되었습니다.\n\n휴가/출장 종료일을 시작일보다 크게 설정하시기 바랍니다..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdVaTime.options[cform.hdVaTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdVaTime.options[cform.hdVaTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdVaTime_1.options[cform.hdVaTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdVaTime_1.options[cform.hdVaTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                         alert("휴가/출장 시작시간이 종료시간 보다 이후입니다.\n\n휴가/출장 종료시간을 시작시간 보다 이후로 설정하시기  바랍니다.");
			                         return;
                               }		
		              }
	           } else if (action == "TripH"){
                       var timevalue = cform.hdTripDate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdTripDate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());
                    
		               if (cvacationday > cvacationday_1) {
		                       alert("출장 시작일이 종료일보다 크게 설정되었습니다.\n\n출장 종료일을 시작일보다 크게 설정하시기 바랍니다..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdTripTime.options[cform.hdTripTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdTripTime.options[cform.hdTripTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdTripTime_1.options[cform.hdTripTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdTripTime_1.options[cform.hdTripTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                           alert("출장 시작시간이 종료시간 보다 이후입니다.\n\n출장 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
			                           return;
                                }		
		               }
				} else if (action == "TripA"){
                       var timevalue = cform.hdTripADate.value;
                       var vacationday = new Date(timevalue.substring(0, 4), timevalue.substring(5, 7)-1, timevalue.substring(8, 10));
                       var cvacationday = Date.UTC(vacationday.getYear(), vacationday.getMonth(), vacationday.getDate());
                    
                       var timevalue_1 = cform.hdTripADate_1.value;
                       var vacationday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
                       var cvacationday_1 = Date.UTC(vacationday_1.getYear(), vacationday_1.getMonth(), vacationday_1.getDate());
                    
		               if (cvacationday > cvacationday_1) {
		                       alert("출장 시작일이 종료일보다 크게 설정되었습니다.\n\n출장 종료일을 시작일보다 크게 설정하시기 바랍니다..");
          		               return;
		               } else if (cvacationday == cvacationday_1) {
		                       var startTimeHour = cform.hdTripATime.options[cform.hdTripATime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdTripATime.options[cform.hdTripATime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdTripATime_1.options[cform.hdTripATime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdTripATime_1.options[cform.hdTripATime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
			                           alert("출장 시작시간이 종료시간 보다 이후입니다.\n\n출장 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
			                           return;
                                }		
		               }
			   } else if (action == "Education") {
                      if (cform.hdEduLocation.value == "") {
							alert("교육 장소를 입력하십시오.");
							return;
						}
                      if (cform.hdEduChair.value == "") {
						  alert("교육주관을 입력하십시오.");
							return;
					  }

						 var startTimeHour = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(0,2); 
						 var startTimeMin = cform.hdEduStartTime.options[cform.hdEduStartTime.selectedIndex].text.substring(3,5); 
						 var endTimeHour = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(0,2); 
						 var endTimeMin = cform.hdEduEndTime.options[cform.hdEduEndTime.selectedIndex].text.substring(3,5); 
						 if (parseInt(startTimeHour + startTimeMin,10) > parseInt(endTimeHour + endTimeMin,10)) {
								 alert("교육/세미나 시작시간이 종료시간 보다 이후입니다.\n\n교육/세미나 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
								 return;
						 }
                      
			  } else if (action == "Appointment") {
			  } else if (action == "Holiday") {
			  } else if (action == "Area") {
				  var appType = cform.hdAppointmentType.options[cform.hdAppointmentType.selectedIndex].text;
		          var timevalue = cform.hdAreaDate.value;
		          var etcday = new Date(timevalue.substring(0,4), timevalue.substring(5,7)-1, timevalue.substring(8,10));
		          var cetcday = Date.UTC(etcday.getYear(), etcday.getMonth(), etcday.getDate());
		          var timevalue_1 = cform.hdAreaDate_1.value;
		          var etcday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
		          var cetcday_1 = Date.UTC(etcday_1.getYear(), etcday_1.getMonth(), etcday_1.getDate());
		          if (cetcday > cetcday_1) {
		              alert(appType + " 시작일이 종료일보다 크게 설정되었습니다.\n\n" + appType + " 종료일을 시작일보다 크게 설정하시기 바랍니다..");
		               return;
		          }
				  if(cform.hdSubject.value == "") {
					alert ("제목를 입력하십시오."); cform.hdSubject.focus(); return;
				  } else if (cform.hdArea.value == "") {
						alert ("방문지를 입력하십시오."); cform.hdArea.focus(); return;
				  }
	          } else {
		              var appType = cform.hdAppointmentType.options[cform.hdAppointmentType.selectedIndex].text;
		              var timevalue = cform.hdEtcDate.value;
		              var etcday = new Date(timevalue.substring(0,4), timevalue.substring(5,7)-1, timevalue.substring(8,10));
		              var cetcday = Date.UTC(etcday.getYear(), etcday.getMonth(), etcday.getDate());
		              var timevalue_1 = cform.hdEtcDate_1.value;
		              var etcday_1 = new Date(timevalue_1.substring(0, 4), timevalue_1.substring(5, 7)-1, timevalue_1.substring(8, 10));
		              var cetcday_1 = Date.UTC(etcday_1.getYear(), etcday_1.getMonth(), etcday_1.getDate());
		              if (cetcday > cetcday_1) {
		                       alert(appType + " 시작일이 종료일보다 크게 설정되었습니다.\n\n" + appType + " 종료일을 시작일보다 크게 설정하시기 바랍니다..");
		                       return;
		              } else if (cetcday == cetcday_1) {
		                       var startTimeHour = cform.hdEtcTime.options[cform.hdEtcTime.selectedIndex].text.substring(0,2); 
		                       var startTimeMin = cform.hdEtcTime.options[cform.hdEtcTime.selectedIndex].text.substring(3,5); 
		                       var endTimeHour = cform.hdEtcTime_1.options[cform.hdEtcTime_1.selectedIndex].text.substring(0,2); 
		                       var endTimeMin = cform.hdEtcTime_1.options[cform.hdEtcTime_1.selectedIndex].text.substring(3,5); 
		                       if (parseInt(startTimeHour,10) > parseInt(endTimeHour,10)) 	{
				                      alert("시작시간이 종료시간 보다 이후입니다.\n\n행사 종료시간을 시작시간 보다 이후로 설정하시기 바랍니다.");
				                      return;
		                       } 
		                       if (parseInt(startTimeHour,10) == parseInt(endTimeHour,10) && parseInt(startTimeMin,10) > parseInt(endTimeMin,10)) {
				                       alert("시작시간이 종료시간 보다 이후입니다.\n\n행사 종료시간을 시작시간 보다 이후로 설정하십시요.");
				                       return;
		                       }			
		              }
             }
     }

	 cform.submit();
     } else {
	        return;
     }
}

function selectkind(newKind) {
	var newOpt = new Option(newKind, newKind, false, true);
	var catField = document.forms[0].hdAppointmentType;
	catField.options[catField.options.length] = newOpt;
	var tmpstring = catField.options[catField.options.selectedIndex].value;
	show_menu( tmpstring );
}
function addkind() {
	var pathname = (window.location.pathname);
    		window.open(pathname.substring(0,(pathname.lastIndexOf('.nsf')+5))+'wAddKind?OpenForm','AddKind','screenX=0,screenY=0,width=370,height=170');
	return;
}
//------------------------------------------------------------------------
function OpenCalendar(FieldName) {
	var strUrl = "Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//일정항목 추가
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh);
}
-->
</Script>