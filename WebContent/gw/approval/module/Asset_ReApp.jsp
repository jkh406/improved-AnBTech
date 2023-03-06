<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "자산 반출 2차부서 결재상신"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //전자결재내용 & 결재선

	//공통 내용관련
	String query = "", subject="";

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

	/*********************************************************************
	 	전달변수
		관리종류 [o_status : t:자산이관, o:자산반출]
		결재상태 [2:1차상신대기,3:1차결재완료,4:2차상신대기,5:2차결재완료]
	*********************************************************************/	
	String title_name = "";
	
	String mode = request.getParameter("mode");			if(mode == null) mode = "";		//mode
	String h_no = request.getParameter("h_no");			if(h_no == null) h_no = "";		//관리번호
	String as_no = request.getParameter("as_no");		if(as_no == null) as_no = "";	//자산번호
	String o_status = request.getParameter("o_status");	if(o_status == null) o_status = "";	//관리종류
	String as_status = request.getParameter("as_status");if(as_status == null) as_status = "";	//결재상태
	if(o_status.equals("t")) title_name = "자산이관";
	else if(o_status.equals("o")) title_name = "자산반출";
	else if(o_status.equals("l")) title_name = "자산대여";

	String[] otColumn = {"pid"};
	bean.setTable("as_history");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("pid DESC");	
	bean.setSearch("h_no",h_no);
	bean.init_unique();

	link_id = "";		//1차 결재승인된 반출의 결재관리번호
	String plid = "";
	if(bean.isAll()){
		link_id = bean.getData("pid");
	}

	/*********************************************************************
	 	자산에서 제목만들기
	*********************************************************************/	
	String[] tColumn = {"a.as_name","a.model_name","b.takeout_reason"};
	bean.setTable("as_info a,as_history b");			
	bean.setColumns(tColumn);
	bean.setOrder("a.model_name ASC");	
	query = "where h_no ='"+h_no+"' and a.as_no = b.as_no";
	bean.setSearchWrite(query);
	bean.init_write();

	if(bean.isAll()) {
		subject = title_name+" : ["+bean.getData("as_name")+"/"+bean.getData("model_name")+"]";
		subject += bean.getData("takeout_reason");
	}

	//*********************************************************************
	// 1차 주관부서 결재선 내용 받기
	//*********************************************************************
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine rline = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//결재선
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		rline = (TableAppLine)line_iter.next();
										
		if(rline.getApStatus().equals("기안")) {
			wname = rline.getApName();	//기안자
			wid = rline.getApSabun();	//기안자 사번
		}
		if(rline.getApStatus().equals("검토"))  {
			vname = rline.getApName();	//검토자
			vid = rline.getApSabun();	//검토자 사번
			vdate = rline.getApDate();	//검토자 검토일자 (있으면 결재하고 없으면 결재않됨)
		}
		if(rline.getApStatus().equals("승인"))  {
			dname = rline.getApName();	//승인자
			did = rline.getApSabun();	//승인자 사번
			ddate = rline.getApDate();	//승인자 승인일자 (있으면 결재하고 없으면 결재않됨)\
		}
			
		line += rline.getApStatus()+" "+rline.getApSabun()+" "+rline.getApName()+" "+rline.getApRank()+" "+rline.getApDivision()+" "+rline.getApDate()+" "+rline.getApComment()+"<br>";
	}

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//결재선

%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title><%=title_name%></title>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
<script language=javascript>
<!--
//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	window.open("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","width=520,height=467,scrollbar=yes,toolbar=no,status=no,resizable=no");
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
	var doc_sub = '<%=subject%>';
	
	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/ApprovalAssetServlet';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
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
-->
</script>

<BODY topmargin="0" leftmargin="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> <%=title_name%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-2);"><img src="../../images/bt_cancel.gif" border="0"  align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=64% align=left valign='top'><%=line%></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
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
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
				%></TD>
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
	<iframe id="iframe_main" src="../../../servlet/AssetServlet?mode=<%=mode%>&h_no=<%=h_no%>&as_no=<%=as_no%>" width="100%" height="100%" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe> </td></tr></table>

<TABLE><TR><TD width="5"></TD></TR></TABLE>
<!-- 2차결재정보 -->
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
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='<%=title_name%>'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='h_no' value='<%=h_no%>'>					<% //자산 관리번호 %>
<input type='hidden' name='as_no' value='<%=as_no%>'>				<% //자산번호 %>
<input type='hidden' name='o_status' value='<%=o_status%>'>			<% //관리종류 %>
<input type='hidden' name='as_status' value='<%=as_status%>'>		<% //결재상태 %>

<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>  
</center>
</body>
</html>

