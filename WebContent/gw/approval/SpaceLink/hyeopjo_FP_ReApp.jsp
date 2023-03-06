<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "협조전 2차부서 결재상신"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//본문파일 읽기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//출력형식 (비용)
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //전자결재내용 & 결재선

	//협조전 내용관련
	String query = "";
	String div_name = "";			//부서명
	String user_name = "";			//대상자 명
	String user_rank = "";			//대상자 직위
	String doc_date = "";			//작성 년월일

	String doc_no = "";				//문서번호
	String dest_ac_name = "";		//수신부서
	String period = "";				//처리기한
	String kind = "";				//구분
	String subject = "";			//제목

	String bon_path = "";			//본문서브패스
	String bon_file = "";			//본문저장파일명
	String note_file = "";			//NOTE저장파일명

	String fname = "";				//첨부파일명
	String sname = "";				//첨부파일 저장명
	String ftype = "";				//첨부파일Type
	String fsize = "";				//첨부파일Size

	String ref_id = "";				//관련근거 전자결재 문서id
	String ref_name = "";			//관련근거 전자결재 문서제목

	//결재선 관련
	String doc_id = "";				//전자결재 관리번호
	String link_id = "";			//관련문서 관리번호
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
	String writer_id = "";			//등록자 사번
	String writer_name = "";		//등록자 명

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	//*********************************************************************
	// 2차 결재상신할 관련문서 관리번호 내용 받기
	//*********************************************************************
	link_id = request.getParameter("link_id");	if(link_id == null) link_id = "";	//관련문서 관리번호

	//*********************************************************************
	// 1차 주관부서 결재선 내용 받기
	//*********************************************************************
	//전자결재내용 & 결재선 읽기
	String line_note = "",line_note2="";		//결재선 note 내용

	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine app = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//결재선
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		app = (TableAppLine)line_iter.next();

		//의견
		if(!app.getApStatus().equals("기안")) {
			if(app.getApStatus().equals("통보")) {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"자확인: <font color=blue>"+app.getApComment()+" "+app.getApDate()+"</font><br>";
			} else {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"자의견: <font color=blue>"+app.getApComment()+"</font><br>";
			}
		}
										
		if(app.getApStatus().equals("기안")) {
			wname = app.getApName();	//기안자
			wid = app.getApSabun();	//기안자 사번
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("검토"))  {
			vname = app.getApName();	//검토자
			vid = app.getApSabun();	//검토자 사번
			vdate = app.getApDate();	//검토자 검토일자 (있으면 결재하고 없으면 결재않됨)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("승인"))  {
			dname = app.getApName();	//승인자
			did = app.getApSabun();		//승인자 사번
			ddate = app.getApDate();	//승인자 승인일자 (있으면 결재하고 없으면 결재않됨)\
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("통보"))  {	
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
	}

	/*********************************************************************
	// 협조전 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
					"doc_id","dest_ac_name","period","kind","subject",
					"bon_path","bon_file","note_file","fname","sname","ftype","fsize","ref_id","ref_name"};
	bean.setTable("jiweon_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (jw_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_name = bean.getData("user_name");		//작성자 명
		user_rank = bean.getData("user_rank");		//작성자 직위
		doc_date = bean.getData("in_date");			//작성년월일

		doc_no = bean.getData("doc_id");			//문서번호
		if(doc_no.length() == 0) doc_no = "결재완료후 자동채번";
		dest_ac_name = bean.getData("dest_ac_name");//수신부서
		period = bean.getData("period");			//처리기한
		kind = bean.getData("kind");				//구분
		subject = bean.getData("subject");			//제목

		bon_path = bean.getData("bon_path");		//본문서브패스
		bon_file = bean.getData("bon_file");		//본문저장파일명
		note_file = bean.getData("note_file");		//Note저장파일명

		fname = bean.getData("fname");				//첨부파일명
		sname = bean.getData("sname");				//첨부파일 저장명
		ftype = bean.getData("ftype");				//첨부파일Type
		fsize = bean.getData("fsize");				//처부파일size

		ref_id = bean.getData("ref_id");			//관련근거 전자결재 문서id
		ref_name = bean.getData("ref_name");		//관련근거 전자결재 문서제목
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

	// Note파일읽기
	String read_note = "",note = "";
	full_path = upload_path + bon_path + "/bonmun/" + note_file;
	read_note = text.getFileString(full_path);
	int note_len = read_note.length();
	for(int i=0; i<note_len; i++) {
		if(read_note.charAt(i) == '\n') note +="<br>";
		else note += read_note.charAt(i);
	}

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

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//결재선
%>


<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>협조전(통제부서용)</title>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 협조전 [집행부서]</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../images/bt_sel_line.gif' align='middle' border='0'></a> 
					<a href="Javascript:eleApprovalRequest();"><img src='../../images/bt_sangsin.gif' align='middle' border='0'></a> 
					<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='middle' border='0'></a> 
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
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
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">의<br>견</TD>
		<TD noWrap width=100% align=left colspan="6"><%=line_note%></TD>
	</TR>
	<TR bgcolor="c7c7c7"><TD height=1 colspan="6"></TD></TR></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">작성일자</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%> 년 <%=wmonth%> 월 <%=wdate%> 일</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">수신부서</td>
           <td width="37%" height="25" class="bg_04"><%=dest_ac_name%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">발신부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%>&nbsp;</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">구분</td>
           <td width="37%" height="25" class="bg_04"><%=kind%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">처리기한</td>
           <td width="37%" height="25" class="bg_04"><%=period%>&nbsp;</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">작성자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=user_name%>&nbsp;</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><pre><%=read_con%></pre></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr> <!-- 관련근거 첨부 : 200408 -->
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">관련근거</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><a href="javascript:viewRef('<%=ref_id%>')"><%=ref_name%></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td width="13%" height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<form action="hyeopjo_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data">
<!-- 2차 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='link_id' value='<%=link_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='<%=subject%> 에 대한 협조요청건 [<%=user_name%>]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>  

</body>
</html>
<script language=javascript>
<!--
//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
	
}

//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
	 //결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
		if(rstr.indexOf("협조") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }

	document.onmousedown=dbclick;// 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/JiWeonServlet';
	document.eForm.mode.value='HYEOPJO_SEC';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();

}
//관련근거 내용보기
function viewRef(a)
{
	var Url = 'mode=APP_LNK&PID='+a;
	wopen('../../../servlet/ApprovalDetailServlet?'+Url,"Ref_view","650","600","scrollbars=yes,toolbar=no,status=no,resizable=yes");
}
//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//닫기
function winClose()
{
	window.returnValue='';
	self.close();
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>