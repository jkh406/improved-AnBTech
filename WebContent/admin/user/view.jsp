<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../errorpage.jsp"
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
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> 사용자 정보 보기</TD>
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
				<IMG src='../images/bt_list.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
				<a href="useri.jsp?j=u&au_id=<%=au_id%>&ac_id=<%=ac_id%>"><IMG src='../images/bt_modify.gif' align='absmiddle' border='0'></a>
				<a href="javascript:del_confirm('<%=au_id%>','<%=ac_id%>');"><IMG src='../images/bt_del.gif' align='absmiddle' border='0'></a>
			  </TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<!--사용자 정보-->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
	<TD height=22 colspan="4"><img src="../images/user_info.gif" width="209" height="25" border="0"></TD></TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">소속</TD>
    <TD width="85%" height="25" class="bg_04" colspan='3'><%=recursion.viewHistory_str(ac_id,whereStr)%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">성명</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("name")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">직위</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("ar_name")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">사번</TD>
    <TD width="85%" height="25" class="bg_04" colspan='3'><%=bean.getData("id")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">주요 업무</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("main_job")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">전자우편</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("email")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">근무처 전화번호</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("office_tel")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">근무처 팩스번호</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("fax")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">휴대전화번호</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("hand_tel")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">자택전화번호</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("home_tel")%></TD>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">자택 주소</TD>
    <TD width="85%" height="25" class="bg_04" colspan='3'><%=bean.getData("address")%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">우편번호</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("post_no")%></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">입사일</TD>
    <TD width="35%" height="25" class="bg_04" ><%=bean.getData("enter_day").substring(0,4)+"년 "+bean.getData("enter_day").substring(4,6)+"월 "+bean.getData("enter_day").substring(6,8)+"일"%></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>

</BODY>
</HTML>


<script language='javascript'>
	function del_confirm(uid,cid){
		var req_confirm = confirm('현재 사용자를 정말로 삭제하시겠습니까?');
		if(req_confirm == true) location.href = 'userp.jsp?j=d&au_id='+uid+'&ac_id='+cid;	
	}
</script>