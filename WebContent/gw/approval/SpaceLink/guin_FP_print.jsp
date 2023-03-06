<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "구인의뢰서 보기"		
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

	//연장근무신청서 내용관련
	String query		= "";
	String div_name		= "";		//부서명
	String user_name	= "&nbsp;";		//대상자 명
	String user_rank	= "&nbsp;";		//대상자 직위
	String doc_date		= "";		//작성 년월일
	
	String doc_no		= "&nbsp;";		//문서번호
	String job_kind		= "&nbsp;";		//모집직종
	String job_content	= "&nbsp;";		//업무내용
	String career		= "&nbsp;";		//학력
	String major		= "&nbsp;";		//전공
	String req_qualify	= "&nbsp;";		//필요자격증
	String status		= "&nbsp;";		//입사형태
	String job_career	= "&nbsp;";		//요구경력
	String job_etc		= "&nbsp;";		//요구경력 기타
	String req_count	= "&nbsp;";		//모집인원
	String marray		= "&nbsp;";		//혼인
	String army			= "&nbsp;";		//병역
	String employ		= "&nbsp;";		//고용형태
	String employ_per		= "&nbsp;";	//고용형태 계약직 기간
	String language_grade	= "&nbsp;";	//외국어
	String language_exam	= "&nbsp;";	//공인시험
	String language_score	= "&nbsp;";	//등급/점수
	String comp_grade		= "&nbsp;";	//전산능력
	String comp_etc			= "&nbsp;";	//전산능력 기타
	String papers			= "&nbsp;";	//제출서류
	String note				= "&nbsp;";	//기타 필요사항

	//결재선 관련
	String pid			= "";		//관리번호
	String doc_id		= "";		//관련문서 관리번호
	String line			="";		//읽은문서 결재선
	String r_line		= "";		//재작성으로 넘겨주기
	String vdate		= "";		//검토자 검토 일자
	String ddate		= "";		//승인자 승인 일자
	String wid			= "";		//기안자사번
	String vid			= "";		//검토자사번
	String did			= "";		//승인자사번
	String wname		= "";		//기안자
	String vname		= "";		//검토자
	String dname		= "";		//승인자
	String PROCESS		= "";		//PROCESS
	String doc_ste		= "";		//doc_ste

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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_OFF")) {
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
	// 구인의뢰서 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date","doc_id",
						"job_kind","job_content","career","major","req_qualify","status","job_career",
						"job_etc","req_count","marray","army","employ","employ_per","language_grade",
						"language_exam","language_score","comp_grade","comp_etc","papers","note"};
	bean.setTable("insa_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (is_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_name = bean.getData("user_name");		//작성자 명
		user_rank = bean.getData("user_rank");		//작성자 직위
		doc_date = bean.getData("in_date");			//작성년월일

		doc_no = bean.getData("doc_id");			//문서번호
		if(doc_no.length() == 0) doc_no = "결재완료후 자동채번";
		job_kind = bean.getData("job_kind");		//모집직종
		job_content = bean.getData("job_content");	//업무내용
		career = bean.getData("career");			//학력
		major = bean.getData("major");				//전공
		req_qualify = bean.getData("req_qualify")==""?"&nbsp;":bean.getData("req_qualify");	//필요자격증
		status = bean.getData("status");			//입사형태
		job_career = bean.getData("job_career");	//요구경력
		job_etc = bean.getData("job_etc");			//요구경력 기타
		req_count = bean.getData("req_count");		//모집인원
		marray = bean.getData("marray");			//혼인
		army = bean.getData("army");				//병역
		employ = bean.getData("employ");			//고용형태
		employ_per = bean.getData("employ_per");	//고용형태 계약직 기간
		language_grade = bean.getData("language_grade");	//외국어
		language_exam = bean.getData("language_exam");		//공인시험
		language_score = bean.getData("language_score");	//등급/점수
		comp_grade = bean.getData("comp_grade");			//전산능력
		comp_etc = bean.getData("comp_etc");		//전산능력 기타
		papers = bean.getData("papers");			//제출서류
		note = bean.getData("note")==""?"&nbsp;":bean.getData("note");				//기타 필요사항
	} //while

	//작성년월일 구하기
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//작성년
		wmonth = doc_date.substring(5,7);			//	  월
		wdate = doc_date.substring(8,10);			//	  일
	}
	//화면출력
	status = StringProcess.repWord(status,":","");	//입사형태
	marray = StringProcess.repWord(marray,":","");	//혼인
	army = StringProcess.repWord(army,":","");		//병역
	language_grade = StringProcess.repWord(language_grade,":","");	//외국어

	//comp_grade = StringProcess.repWord(comp_grade,":",", ");		//전산능력
	StringTokenizer comp_list = new StringTokenizer(comp_grade,":");		//전산능력
	comp_grade = "";
	int c = 0;
	while(comp_list.hasMoreTokens()) {
		comp_grade += comp_list.nextToken()+", ";
		c++;
	}
	int comp_len = comp_grade.length();
	if(comp_len > 2)	comp_grade = comp_grade.substring(0,comp_len-2);

	//papers = StringProcess.repWord(papers,":",", ");	//제출서류
	StringTokenizer papers_list = new StringTokenizer(papers,":");		//제출서류
	papers = "";
	int p = 0;
	while(papers_list.hasMoreTokens()) {
		papers += papers_list.nextToken()+", ";
		p++;
	}
	int ps_len = papers.length();
	if(ps_len > 2)	papers = papers.substring(0,ps_len-2);

	String employ_data = "";
	StringTokenizer emp_list = new StringTokenizer(employ,":");		//고용형태
	int e = 0;
	while(emp_list.hasMoreTokens()) {
		String emp = emp_list.nextToken();
		if(emp.equals("계약직")) employ_data += emp+"(기간 "+employ_per+"개월)" + ", ";
		else employ_data += emp+", ";
		e++;
	}
	int emp_len = employ_data.length();
	if(emp_len > 2)	employ_data = employ_data.substring(0,emp_len-2);

%>

<html>
<head>
<title> 구인의뢰서</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2"> 구인의뢰서</TD>
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
				<td width="15%" height="25" align="middle" class="bg_05">문서번호</td>
				<td width="35%" class="bg_06"><%=doc_no%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">작성일자</td>
				<td width="35%" class="bg_06"><%=doc_date%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">모집직종</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=job_kind%></pre>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">업무내용</td>
				<td width="85%" class="bg_06" colspan="3"><%=job_content%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">학력</td>
				<td width="35%" class="bg_06"><%=career%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">전공</td>
				<td width="35%" class="bg_06"><%=major%>&nbsp;</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">필요한자격증</td>
				<td width="85%" class="bg_06" colspan="3"><%=req_qualify%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">입사형태</td>
				<td width="35%" class="bg_06"><%=status%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">모집인원</td>
				<td width="35%" class="bg_06"><%=req_count%> 명</td></tr>								
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">요구경력</td>
				<td width="85%" class="bg_06" colspan="3"><%=job_career%>년이상 (기타:<%=job_etc%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">고용형태</td>
				<td width="85%" class="bg_06" colspan="3"><%=employ_data%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">혼인여부</td>
				<td width="35%" class="bg_06"><%=marray%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">병력</td>
				<td width="35%" class="bg_06"><%=army%>&nbsp;</td></tr>		
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">외국어</td>
				<td width="85%" class="bg_06" colspan="3">영어 (수준:<%=language_grade%>) (공인시험:<%=language_exam%>, 등급/점수:<%=language_score%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">전산능력</td>
				<td width="85%" class="bg_06" colspan="3"><%=comp_grade%> 기타(<%=comp_etc%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">제출서류</td>
				<td width="85%" class="bg_06" colspan="3"><%=papers%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">기타요구사항</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=note%></pre>&nbsp;</td></tr></table>
	</td></tr></table>

<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
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
	
	document.eForm.action = "guin_FP_Rewrite.jsp";
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
