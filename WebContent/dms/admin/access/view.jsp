<%@ include file="../../../admin/configHead.jsp"%>
<%@ include file="../../../admin/chk/chkDM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/HTML;charset=KSC5601"
	errorPage	= "../../../admin/errorpage.jsp"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	int ac_id = request.getParameter("ac_id") == null?0:Integer.parseInt(request.getParameter("ac_id"));
	int au_id = request.getParameter("au_id") == null?0:Integer.parseInt(request.getParameter("au_id"));

	String whereStr = "";
	
	String sql = "select a.*,b.ar_name from user_table a, rank_table b where a.au_id = '"+au_id+"' and a.rank = b.ar_code";
	bean.openConnection();
	bean.executeQuery(sql);
	bean.next();
%>

<HTML>
<head><title>사용자 정보 보기</title></head>
<link rel="stylesheet" type="text/css" href="../../css/style.css">

<body topmargin="0" leftmargin="0" marginwidth="0" oncontextmenu="return false">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 문서엑세스 권한</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<IMG src='../images/bt_list.gif' onclick="javascript:location.href='userl.jsp'" align='absmiddle' border='0' style='cursor:hand'>
				<a href="useri.jsp?j=u&au_id=<%=au_id%>&ac_id=<%=ac_id%>"><IMG src='../images/bt_modify.gif' align='absmiddle' border='0'></a></TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<!--사용자 정보-->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
	<TD height=22 colspan="4"><img src="../images/user_info.gif" width="209" height="25" border="0"></TD></TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">소속</TD>
    <TD width="35%" height="25" class="bg_04" ><%=recursion.viewHistory_str(ac_id,whereStr)%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직위</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("ar_name")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">성명</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("name")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">사번</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("id")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>

<%
	String access_code=bean.getData("access_code");
		
	String[] code_value={"","","","",""}; // 각 코트 저장변수
	String code_temp;         
	String[] code_value_text;     // 해당권한코드의 값(코드 정보 : 예- "N"->"모든문서 ACCESS 불능" ) 저장변수
	
	int codelength;  // access_code length

		codelength=access_code.length();    
		code_value=new String[codelength]; // code_value[#] -- #급문서 권한 ( # => 1~3 )
										   // code_value[4] -- 대외비문서   code_value[5] -- 일반문서
		for(int k=0;k<codelength;k++){
			code_temp=access_code;
			code_value[k]=code_temp.substring(k,k+1);
		}
	
	code_value_text=new String[codelength]; 

	for(int no=0;no<codelength;no++) {	
		
		if(code_value[no].equals("A")) {
			code_value_text[no]="모든문서 ACCESS 가능";
		} else if(code_value[no].equals("C")) {
			code_value_text[no]="동일본부문서 ACCESS";
		} else if(code_value[no].equals("D")) {
			code_value_text[no]="동일부서문서 ACCESS";
		} else if(code_value[no].equals("N")) {
			code_value_text[no]="모든문서 ACCESS 불능";
		}
	}
	
%>

<!--기술문서 엑세스권한정보-->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0>
	<TR><TD height=10 colspan="4"></TD></TR>
	<TR><TD height=22 colspan="4"><img src="../images/dms_access_info.gif" width="209" height="25" border="0"></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">1급문서</TD>
		<TD width="35%" height="25" class="bg_04" ><%=code_value_text[0]%></TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">2급문서</TD>
		<TD width="35%" height="25" class="bg_04" ><%=code_value_text[1]%></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">3급문서</TD>
		<TD width="35%" height="25" class="bg_04" ><%=code_value_text[2]%></TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">대외비문서</TD>
		<TD width="35%" height="25" class="bg_04" ><%=code_value_text[3]%></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
	<TR>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">일반문서</TD>
		<TD width="35%" height="25" class="bg_04" ><%=code_value_text[4]%></TD>
		<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif"></TD>
		<TD width="35%" height="25" class="bg_04" ></TD></TR>
	<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
</TABLE>
</BODY>
</HTML>