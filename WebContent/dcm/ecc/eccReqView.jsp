<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "ECR 내용보기"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
%>

<%
	//----------------------------------------------------
	//	ECC COM 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");

	//----------------------------------------------------
	//	ECC REQ 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccReqTable ecr;
	ecr = (eccReqTable)request.getAttribute("REQ_List");

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	String fname = ecr.getFname();			//첨부파일명
	String sname = ecr.getSname();			//첨부파일 저장명
	String ftype = ecr.getFtype();			//첨부파일Type
	String fsize = ecr.getFsize();			//첨부파일Size
	int attache_cnt = 4;					//첨부파일 최대갯수 (미만)
	String bon_path = "/dcm/"+ecc.getEcrId();	//첨부파일 저장 확장path

	//첨부파일 개별로 읽기
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

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
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//저장파일명
			addFile[m][4] = addFile[m][4].trim();					//TEMP 저장파일명
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

%>

<HTML>
<HEAD><TITLE>ECR내용</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
	<!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
		        <TR>
					<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../dcm/images/pop_app_u.gif" width="181" height="17" hspace="10">ECR내용</TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
		<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
			<TBODY>
				 <TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
					   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제분류</TD>
					   <TD width="37%" height="25" class="bg_04"><%=ecr.getTrouble()%></TD>
					   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부분</TD>
					   <TD width="37%" height="25" class="bg_04"><%=ecr.getChgPosition()%></TD>
				</TR>
				<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
				    <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">현상및원인</TD>
					<TD width="37%" height="25" class="bg_04">
						<TEXTAREA NAME="condition" rows='10' cols='40'  class="bg_05" readonly><%=ecr.getCondition()%></TEXTAREA></TD>
				   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO적용내용</TD>
				   <TD width="37%" height="25" class="bg_04">
						<TEXTAREA NAME="solution" rows='10' cols='40' readonly><%=ecr.getSolution()%></TEXTAREA></TD>
				</TR>
				<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</TD>
					<TD width="87%" height="25" class="bg_04" colspan="3">
				<% 
				for(int i=0; i<cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
					out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
					out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </TD>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
	  </TBODY></TABLE>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="../dcm/images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR>
</TABLE>

</body>
</html>
