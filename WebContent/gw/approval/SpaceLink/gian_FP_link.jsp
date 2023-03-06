<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "기안서 작성 보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="java.sql.Connection"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기

	//기안서 내용관련
	String query = "";
	String div_name = "";			//부서명
	String user_name = "";			//대상자 명
	String user_rank = "";			//대상자 직위
	String doc_date = "";			//작성 년월일

	String doc_no = "";				//문서번호
	String period = "";				//처리기한
	String doc_syear = "";			//시행일자 년도
	String doc_smonth = "";			//시행일자 월
	String doc_sdate = "";			//시행일자 일
	String kind = "";				//구분
	String subject = "";			//제목
	String bon_path = "";			//본문서브패스
	String bon_file = "";			//본문저장파일명

	String fname = "";				//첨부파일명
	String sname = "";				//첨부파일 저장명
	String ftype = "";				//첨부파일Type
	String fsize = "";				//첨부파일Size
	
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

	//*********************************************************************
	// 결재선 내용 받기
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//관리번호
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//관리번호(링크정보로 pid와동일)
	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";	//PROCESS명
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";	//doc_ste

	//전자결재내용 & 결재선 읽기
	String line_note = "";		//결재선 note 내용
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";	//협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt = 0;									//결재선에 출력될 라인 갯수
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();
	
	//보관문서(anb) 인지 보존문서(storehouse)에 따라 구분 : 200408 : 공통
	if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
	else masterDAO.getTable_MasterPid(pid);		
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
		
		//의견
		if(!app.getApStatus().equals("기안")) {
			if(app.getApStatus().equals("통보")) {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"자확인: <font color=blue>"+app.getApComment()+" "+app.getApDate()+"</font><br>";
			} else {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"자의견: <font color=blue>"+app.getApComment()+"</font><br>";
			}
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
			if((PROCESS.equals("APP_BOX") || PROCESS.equals("DEL_BOX")) && app.getApStatus().equals("통보")){

			} else if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_DRF")) {
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
	// 기안서 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"doc_id","period","enforce_year","enforce_month","enforce_date","kind","subject",
						"bon_path","bon_file","fname","sname","ftype","fsize"};
	bean.setTable("jiweon_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (jw_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_name = bean.getData("user_name");		//작성자 명
		user_rank = bean.getData("user_rank");		//작성자 직위
		doc_date = bean.getData("in_date");			//작성년월일

		doc_no = bean.getData("doc_id");			//문서번호
		if(doc_no.length() == 0) doc_no = "결재완료후 자동채번";
		period = bean.getData("period");			//처리기한
		doc_syear = bean.getData("enforce_year");	//시행일자 년도
		doc_smonth = bean.getData("enforce_month");	//시행일자 월
		doc_sdate = bean.getData("enforce_date");	//시행일자 일
		kind = bean.getData("kind");				//구분
		subject = bean.getData("subject");			//제목

		bon_path = bean.getData("bon_path");		//본문서브패스
		bon_file = bean.getData("bon_file");		//본문저장파일명

		fname = bean.getData("fname");				//첨부파일명
		sname = bean.getData("sname");				//첨부파일 저장명
		ftype = bean.getData("ftype");				//첨부파일Type
		fsize = bean.getData("fsize");				//처부파일size
		
	} //while

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);			//	  월
		wdate = doc_date.substring(8,10);			//	  일
	}

	//본문파일읽기
	String read_con = "";
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);

	//첨부파일 개별로 읽기
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

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
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//저장파일명
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}
	
%>
<html>
<head><title>기안서</title> 
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">기안서</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- 닫기 -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- 결재정보 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=48 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">결<p>재</TD>
		<TD noWrap width=50 align=middle class="bg_07">기안자</TD>
		<TD noWrap width=50 align=middle class="bg_07">검토자</TD>
		<TD noWrap width=50 align=middle class="bg_07">승인자</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
							if(vdate.length() == 0)	{//검토자
								if(ddate.length() == 0) out.println("&nbsp;");
								else out.println("전결");
							} else {
								out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
							}
						%>				
		</TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<%
							if(ddate.length() == 0)	{//승인자
								out.println("&nbsp;");
							} else {
								out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
							}
						%>				
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD>
	</TR>
	<TR>
		<TD noWrap width=40 align=middle class="bg_05">의<br>견</TD>
		<TD noWrap width=550 align=left colspan="5"><%=line_note%>&nbsp;</TD>
	</TR>
	</TBODY>
</TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>문서번호</td>
			   <td width="37%" height="25" class="bg_06"><%=doc_no%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>기안일자</td>
			   <td width="37%" height="25" class="bg_06"><%=wyear%> 년 <%=wmonth%> 월 <%=wdate%> 일&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>기안부서</td>
			   <td width="37%" height="25" class="bg_06"><%=div_name%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>처리기한</td>
			   <td width="37%" height="25" class="bg_06"><%=period%>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>구분</td>
			   <td width="37%" height="25" class="bg_06"><%=kind%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>시행일자</td>
			   <td width="37%" height="25" class="bg_06"><%=doc_syear%> 년 <%=doc_smonth%> 월 <%=doc_sdate%> 일&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>제목</td>
			   <td width="87%" height="25" colspan="3" class="bg_06"><%=subject%>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>내용</td>
			   <td width="87%" height="25" colspan="3" class="bg_06"><pre><%=read_con%></pre>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>첨부파일</td>
			   <td width="87%" height="25" colspan="3" class="bg_06">
			<%
			for(int i=0; i<cnt; i++) {
				out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
			} 
			%>
			   &nbsp;</td></tr></table>
	</td></tr></table>

</body></html>

