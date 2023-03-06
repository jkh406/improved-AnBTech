<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "전자결재 본문보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기

	//일반문서보고 내용관련
	String query		= "";
	String doc_sub		= "&nbsp;";			//제목
	String doc_per		= "&nbsp;";			//보존기간
	String doc_sec		= "&nbsp;";			//보안등급
	String content		= "&nbsp;";			//본문내용
	String bon_path		= "&nbsp;";			//본문내용 path
	String bon_file		= "&nbsp;";			//본문저장 파일명

	String doc_or1		= "&nbsp;";			//읽은문서 첨부 원래이름1
	String doc_ad1		= "&nbsp;";			//읽은문서 첨부 저장이름1
	String doc_or2		= "&nbsp;";			//읽은문서 첨부 원래이름2
	String doc_ad2		= "&nbsp;";			//읽은문서 첨부 저장이름2
	String doc_or3		= "&nbsp;";			//읽은문서 첨부 원래이름3
	String doc_ad3		= "&nbsp;";			//읽은문서 첨부 저장이름3
	String file1_size	= "&nbsp;";			//첨부파일1 크기
	String file2_size	= "&nbsp;";			//첨부파일2 크기
	String file3_size	= "&nbsp;";			//첨부파일3 크기
	
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
			line_cnt++;
		}
		else if(app.getApStatus().equals("검토"))  {
			vname = app.getApName();	if(vname == null) vname="";		//검토자
			vid = app.getApSabun();		if(vid == null) vid="";			//검토자 사번
			vdate = app.getApDate();	if(vdate == null) vdate="";		//검토자 검토일자(있으면결재,없으면결재않됨)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			line_cnt++;
		}
		else if(app.getApStatus().equals("승인"))  {
			dname = app.getApName();	if(dname == null) dname="";		//승인자
			did = app.getApSabun();		if(did == null) did="";			//승인자 사번
			ddate = app.getApDate();	if(ddate == null) ddate="";		//승인자 승인일자 (있으면결재,없으면결재않됨)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			line_cnt++;
		}
		else {	//협조 : 기결함문서시 협조자를 승인자 뒤로 보내기 위해
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_GEN")) {
				ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				line_cnt++;
			} else {
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				line_cnt++;
			}
		}
	}
	if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }
	connMgr.freeConnection("mssql",con);				//커넥션 닫기

	/*********************************************************************
	// 일반문서보고 정보 알아보기
	*********************************************************************/	
	String[] Column = {"app_subj","save_period","security_level","bon_path","bon_file",
			"add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
	bean.setTable("app_master");			
	bean.setColumns(Column);
	bean.setOrder("app_subj ASC");	
	query = "where (pid ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		doc_sub = bean.getData("app_subj");				//제목
		doc_per = bean.getData("save_period");			//보존기간
		doc_sec = bean.getData("security_level");		//보안등급
		bon_path = bean.getData("bon_path");			//본문내용 path
		bon_file = bean.getData("bon_file");			//본문저장 파일명

		doc_or1 = bean.getData("add_1_original");		//읽은문서 첨부 원래이름1
		doc_ad1 = bean.getData("add_1_file");			//읽은문서 첨부 저장이름1
		doc_or2 = bean.getData("add_2_original");		//읽은문서 첨부 원래이름2
		doc_ad2 = bean.getData("add_2_file");			//읽은문서 첨부 저장이름2
		doc_or3 = bean.getData("add_3_original");		//읽은문서 첨부 원래이름3
		doc_ad3 = bean.getData("add_3_file");			//읽은문서 첨부 저장이름3		
	} //while

	//보존기간
	String period = "";
	if(doc_per.equals("0")) period = "처리후폐기";
	else if(doc_per.equals("1")) period = "1년";
	else if(doc_per.equals("2")) period = "2년";
	else if(doc_per.equals("3")) period = "3년";
	else if(doc_per.equals("5")) period = "5년";
	else if(doc_per.equals("EVER")) period = "영구";

	//보안등급
	String security = "";
	if(doc_sec.equals("1")) security = "1급";
	else if(doc_sec.equals("2")) security = "2급";
	else if(doc_sec.equals("3")) security = "3급";
	else if(doc_sec.equals("INDOR")) security = "대외비";
	else if(doc_sec.equals("GENER")) security = "일반";

	//본문파일읽기
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	content = text.getFileString(full_path);

	//첨부파일 파일사이즈 구하기
	String file_path = "";

	file_path = upload_path + bon_path + "/addfile/" + doc_ad1;
	File fn1 = new File(file_path);
	file1_size = Long.toString(fn1.length());

	file_path = upload_path + bon_path + "/addfile/" + doc_ad2;
	File fn2 = new File(file_path);
	file2_size = Long.toString(fn2.length());

	file_path = upload_path + bon_path + "/addfile/" + doc_ad3;
	File fn3 = new File(file_path);
	file3_size = Long.toString(fn3.length());

%>

<html>
<head>
<title>일반문서</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- 로고,제목,버튼 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">일반문서보고</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- 출력 -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- 닫기 -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>
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
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>				
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TBODY></TABLE><BR>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">문서제목</td>
				<td width="85%" class="bg_06" colspan="3"><%=doc_sub%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">보존기간</td>
				<td width="35%" class="bg_06"><%=period%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">보안등급</td>
				<td width="35%" class="bg_06"><%=security%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">내용</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=content%></pre><img src='' width='0' height='0'></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">첨부파일</td>
				<td width="85%" class="bg_06" colspan="3">&nbsp;
						<a href='../eleApproval_downloadp.jsp?fname=<%=doc_or1%>&fsize=<%=file1_size%>&umask=<%=doc_ad1%>&extend=<%=bon_path%>'><%=doc_or1%></a><br><a href='../eleApproval_downloadp.jsp?fname=<%=doc_or2%>&fsize=<%=file2_size%>&umask=<%=doc_ad2%>&extend=<%=bon_path%>'><%=doc_or2%></a><br><a href='../eleApproval_downloadp.jsp?fname=<%=doc_or3%>&fsize=<%=file1_size%>&umask=<%=doc_ad3%>&extend=<%=bon_path%>'><%=doc_or3%></a></td></tr>
			</table>
	</td></tr></table>
<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='PID' value='<%=doc_id%>'>
	<input type='hidden' name='mode'>	
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
	document.eForm.action="../../../servlet/ApprovalDetailServlet?PID=<%=doc_id%>&mode=REW";		
	document.eForm.PID.value='<%=doc_id%>';
	document.eForm.mode.value='REW';
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
