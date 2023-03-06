<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "교육일지 보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식

	//교육일지 내용관련
	String query		= "";
	String div_name		= "&nbsp;";		//부서명
	String user_name	= "&nbsp;";		//대상자 명
	String user_rank	= "&nbsp;";		//대상자 직위
	String doc_date		= "";			//작성 년월일

	String doc_syear	= "";			//교육일자 년
	String doc_smonth	= "";			//교육일자 월
	String doc_sdate	= "";			//교육일자 일
	String lecturer_id	="&nbsp;";		//강사 사번
	String lecturer_name ="&nbsp;";		//강사 이름
	String major_kind	= "&nbsp;";		//교육주관
	String place		= "&nbsp;";		//장소
	String participators_cnt = "&nbsp;";	//교육대상인원수
	String edu_subject	= "&nbsp;";		//교육명
	String antiprt_prs	= "&nbsp;";		//불참자 처리		
	String bon_path		= "&nbsp;";		//본문서브패스
	String bon_file		= "&nbsp;";		//본문저장파일명

	//결재선 관련
	String pid		= "";			//관리번호
	String doc_id	= "";			//관련문서 관리번호
	String line		= "";			//읽은문서 결재선
	String r_line	= "";			//재작성으로 넘겨주기
	String vdate	= "";			//검토자 검토 일자
	String ddate	= "";			//승인자 승인 일자
	String wid		= "";			//기안자사번
	String vid		= "";			//검토자사번
	String did		= "";			//승인자사번
	String wname	= "";			//기안자
	String vname	= "";			//검토자
	String dname	= "";			//승인자
	String PROCESS	= "";			//PROCESS
	String doc_ste	= "";			//doc_ste

	//*********************************************************************
	// 결재선 내용 받기
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//관리번호
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//관리번호(링크정보로 pid와동일)
	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";	//PROCESS명
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";	//doc_ste

	//전자결재내용 & 결재선 읽기
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//협조결재선,협조TextArea,결재의견,TextArea
	int line_cnt = 0;									//결재선에 출력될 라인 갯수
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();
	masterDAO.getTable_MasterPid(pid);	
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
		
		//결재선
		cmt = app.getApComment(); if(cmt == null) cmt = "";
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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_EDU")) {
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
	connMgr.freeConnection("mssql",con);

	/*********************************************************************
	// 교육일지 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"e_year","e_month","e_date","lecturer_id","lecturer_name","major_kind",
						"place","part_cnt","edu_subject","antiprt_prs",
						"bon_path","bon_file"};
	bean.setTable("gyoyuk_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gy_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_name = bean.getData("user_name");		//작성자 명
		user_rank = bean.getData("user_rank");		//작성자 직위
		doc_date = bean.getData("in_date");			//작성년월일

		doc_syear = bean.getData("e_year");			//근무일자 년
		doc_smonth = bean.getData("e_month");		//근무일자 월
		doc_sdate = bean.getData("e_date");			//근무일자 일

		lecturer_id = bean.getData("lecturer_id");			//강사 사번
		lecturer_name = bean.getData("lecturer_name");		//강사 이름
		major_kind = bean.getData("major_kind");			//교육주관
		place = bean.getData("place");						//장소
		participators_cnt = bean.getData("part_cnt");		//교육대상인원수
		edu_subject = bean.getData("edu_subject");			//교육명
		antiprt_prs = bean.getData("antiprt_prs")==""?"&nbsp;":bean.getData("antiprt_prs");			//불참자 처리		
		bon_path = bean.getData("bon_path");				//본문서브패스
		bon_file = bean.getData("bon_file");				//본문저장파일명
	} //while

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);			//	  월
		wdate = doc_date.substring(8,10);			//	  일
	}
	//교육주관 처리
	String major_dp = "";	
	major_dp = StringProcess.repWord(major_kind,":","");	

	//불참자 처리
	String antiprt_dp = "";	
	antiprt_dp = StringProcess.repWord(antiprt_prs,":","");	

	//본문파일읽기
	String read_con = "",content = "";
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);
	int con_len = read_con.length();
	for(int i=0; i<con_len; i++) {
		if(read_con.charAt(i) == '\n') content +="<br>";
		else content += read_con.charAt(i);
	}

	//교육인원에 따라 작성할 피교육자 명단컬럼 조절하기 (기본 : 20명)
	int content_height = 360;		//교육내용 높이 (기본값)
	int row_cnt = 24;				//content_height의 row값
	int people_height = 210;		//피교육자 명단 높이 (기본값 : 20명)
	int people_line = 10;			//세로줄 최대기입 명단수
	int people_total = 20;			//전체 피교육생 명단수
	int prt_cnt = Integer.parseInt(participators_cnt);
	if(prt_cnt >20) {
		int l_cnt = (prt_cnt + 1) / 2;		//계산된 세로줄 최대기입 명단수
		int l_add = l_cnt - 10;				//기본에서 추가된 line 수
		int l_hgt = l_add * 21;				//추가된 line당 line 크기조절 
		content_height = 210 - l_hgt;
		row_cnt = 24 - l_add;
		people_height = 210 + l_hgt;
		people_line = 10 + l_add;
		people_total = people_line * 2;
	}

	//피교육생 찾기
	String[][] participators = new String[people_total][3]; 
	for(int k=0; k<people_total; k++) for(int m=0; m<3; m++) participators[k][m] = "";
	String[] stdColumn = {"participator_id","participator_name","prt_etc"};
	bean.setTable("gyoyuk_part");			
	bean.setColumns(stdColumn);
	bean.setOrder("gy_cid ASC");	
	query = "where (gy_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	
	int w = 0;
	while(bean.isAll()) {
		participators[w][0] = bean.getData("participator_id");			//피교육생 사번
		participators[w][1] = bean.getData("participator_name");		//피교육생 이름
		participators[w][2] = bean.getData("prt_etc");					//비고내용
		w++;
	} //while

%>
<html>
<head>
<title> 교육일지</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.num {text-indent:10;}
-->
</style>
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2"> 교육일지</TD>
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
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">메<p>모</TD>
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=50 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
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
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">교육일자</td>
				<td width="35%" class="bg_06"><%=doc_syear%> 년 <%=doc_smonth%> 월 <%=doc_sdate%> 일</td>
				<td width="15%" height="25" align="middle" class="bg_05">강사</td>
				<td width="35%" class="bg_06"><%=lecturer_id%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">교육주관</td>
				<td width="35%" class="bg_06"><%=major_dp%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">장소</td>
				<td width="35%" class="bg_06"><%=place%>&nbsp;</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">주관부서</td>
				<td width="35%" class="bg_06"><%=div_name%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">교육대상</td>
				<td width="35%" class="bg_06"><%=participators_cnt%> 명</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">교육명</td>
				<td width="35%" class="bg_06"><%=edu_subject%></td>
				<td width="15%" height="25" align="middle" class="bg_05">불참자처리</td>
				<td width="35%" class="bg_06"><%=antiprt_dp%>&nbsp;</td></tr>	
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">교육내용</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=read_con%></pre>&nbsp;</td></tr>			
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">교육참석자</td>
				<td width="85%" class="bg_06" colspan="3">
					<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA> 
					<tr>
						<td width="150" height="23" align="center" class=bg_05>성 명</td>
						<td width="150" height="23" align="center" class=bg_05>서 명</td>
						<td width="200" height="23" align="center" class=bg_05>비 고</td>
						<td width="150" height="23" align="center" class=bg_05>성 명</td>
						<td width="150" height="23" align="center" class=bg_05>서 명</td>
						<td width="200" height="23" align="center" class=bg_05>비 고</td>
					</tr>
					<tr>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //성명 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='left' valign='middle' class='num'>"+fmt.toDigits(i+1));
									out.println(participators[i][1]);
									out.println("</td></tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
								<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
									<% //서명 1 ~ people_line
									for(int i=0; i<people_line; i++) {
										out.println("<tr>");
										out.println("<td width='90' height='20' align='center' valign='middle'>");
										if(participators[i][0].length() != 0)
											out.println("<img src='../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
										out.println("</td>");
										out.println("</tr>");
										out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
									}
									%>
								</table></td>
						<td width="200" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //비고 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='center' valign='middle'>");
									out.println(participators[i][2]);
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //성명  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='left' valign='middle' class='num'>"+fmt.toDigits(i+1));
									out.println(participators[i][1]);
									out.println("</td></tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //서명  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='90' height='20' align='center' valign='middle'>");
									if(participators[i][0].length() != 0)
										out.println("<img src='../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="200" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //비고  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='center' valign='middle'>");
									out.println(participators[i][2]);
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td></tr>			
			</table>
	</td></tr></table>
<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_syear' value='<%=doc_syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=doc_smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=doc_sdate%>'>
	<input type='hidden' name='lecturer_id' value='<%=lecturer_id%>'>
	<input type='hidden' name='lecturer_name' value='<%=lecturer_name%>'>
	<input type='hidden' name='major_kind' value='<%=major_kind%>'>
	<input type='hidden' name='place' value='<%=place%>'>
	<input type='hidden' name='participators_cnt' value='<%=participators_cnt%>'>
	<input type='hidden' name='edu_subject' value='<%=edu_subject%>'>
	<input type='hidden' name='antiprt_prs' value='<%=antiprt_prs%>'>
	<input type='hidden' name='content' value='<%=read_con%>'>

	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
	<input type='hidden' name='bon_path' value='<%=bon_path%>'>
	<input type='hidden' name='read_student' value='R'>
</form>
</body></html>

<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
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
	
	document.eForm.action = "gyoyuk_ilji_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

-->
</script>
