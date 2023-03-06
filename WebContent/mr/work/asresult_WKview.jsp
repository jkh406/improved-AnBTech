<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "AS실적보기(업체용)"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mr.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//----------------------------------------------------//
	String register_no="",register_date="",as_field="",code="",request_name="",serial_no="";
	String request_date="",as_date="",as_type="",as_content="",as_result="",as_delay="",as_issue="";
	String worker="",company_no="";
	//----------------------------------------------------
	//	실적정보 LIST읽기 : as_result
	//----------------------------------------------------
	com.anbtech.mr.entity.assupportTable work;
	ArrayList work_list = new ArrayList();
	work_list = (ArrayList)request.getAttribute("WORK_List");
	work = new assupportTable();
	Iterator work_iter = work_list.iterator();
	
	if(work_iter.hasNext()) {
		work = (assupportTable)work_iter.next();

		register_no = work.getRegisterNo();	
		register_date = work.getRegisterDate();
		as_field = work.getAsField();
		code = work.getCode();
		request_name = work.getRequestName();
		serial_no = work.getSerialNo();
		request_date = work.getRequestDate();
		as_date = work.getAsDate();
		as_type = work.getAsType();
		as_content = work.getAsContent();	
		as_result = work.getAsResult();
		as_delay = work.getAsDelay();
		as_issue = work.getAsIssue();
		worker = work.getWorker();	
		company_no = work.getCompanyNo();	
	}

	//---------------------------------------------------------
	//	AS지원목록 정보
	//--------------------------------------------------------
	com.anbtech.mr.entity.assupportTable field;
	ArrayList field_list = new ArrayList();
	field_list = (ArrayList)request.getAttribute("FIELD_List");
	field = new assupportTable();
	Iterator field_iter = field_list.iterator();
	int field_cnt = field_list.size();

	String[][] asfield = new String[field_cnt][6];
	int e = 0;
	while(field_iter.hasNext()) {
		field = (assupportTable)field_iter.next();
		asfield[e][0] = field.getRegisterName();		//지원이름
		asfield[e][1] = field.getSno();					//지원약속표기
		e++;
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../mr/css/style.css" type="text/css">
<script language='javascript'>
<!--

-->
</script>
</head>

<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../mr/images/blet.gif"> A/S 내용보기</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:self.close();"><img src="../mr/images/bt_close.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">작성자</td>
           <td width="37%" height="25" class="bg_04"><%=worker%></td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">관리코드</td>
           <td width="37%" height="25" class="bg_04"><%=company_no%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지원분야</td>
           <td width="37%" height="25" class="bg_04">
			<%
				for(int si=0; si<field_cnt; si++) {
					if(as_field.equals(asfield[si][1])) out.println(asfield[si][0]);
				}
			%>
			</td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">등록일시</td>
           <td width="37%" height="25" class="bg_04"><%=register_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
		    <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">요청자</td>
           <td width="37%" height="25" class="bg_04"><%=request_name%></td>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">요청일</td>
           <td width="37%" height="25" class="bg_04"><%=request_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지원형태</td>
           <td width="37%" height="25" class="bg_04">
			<%
				if(as_type.equals("1")) out.print("방문지원");
				else out.print("유선지원");
			%></td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">처리여부</td>
           <td width="37%" height="25" class="bg_04">
		   <%
				if(as_result.equals("1")) out.print("완료");
				else out.print("미처리");
			%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">장비일련번호</td>
           <td width="37%" height="25" class="bg_04"><%=serial_no%></td>
		   <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">방문일</td>
           <td width="37%" height="25" class="bg_04"><%=as_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">A/S내용</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="9" name="as_content" cols="93" style="background:#FFFFFF;border:1 solid #787878;" readonly><%=as_content%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">이슈사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="6" name="as_issue" cols="93" style="background:#FFFFFF;border:1 solid #787878;" readonly><%=as_issue%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../mr/images/bg-01.gif">지연사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<textarea rows="5" name="as_delay" cols="93" style="background:#FFFFFF;border:1 solid #787878;" readonly><%=as_delay%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
</td></tr></table>

</body>
</html>
