<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "BOM 권한관리"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage="../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	내부데이터 받기
	*********************************************************************/
	String query = "",div_name="",div_code="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String keyname = request.getParameter("keyname"); if(keyname == null) keyname = ""; 
	String owner = Hanguel.toHanguel(request.getParameter("owner")); if(owner == null) owner = ""; 
	String msg = "";
	/*********************************************************************
	 	등록하기
	*********************************************************************/
	if(mode.equals("ADD")) {
		//사업부코드 구하기
		String id = owner.substring(0,owner.indexOf("/"));
		String[] idColumn = {"b.ac_code"};
		bean.setTable("user_table a,class_table b");		
		bean.setColumns(idColumn);
		query = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
		bean.setSearchWrite(query);
		bean.init_write();
		if(bean.isAll()) div_code = bean.getData("ac_code");	

		//등록된자 있지 검사하기
		String one = "";
		String[] bmColumn = {"owner"};
		bean.setTable("mbom_grade_mgr");		
		bean.setColumns(bmColumn);
		query = "where owner like '%"+owner+"%' and keyname ='"+keyname+"'";
		bean.setSearchWrite(query);
		bean.init_write();
		if(bean.isAll()) one = bean.getData("owner");
		
		if(!one.equals(owner)){
			query = "insert into mbom_grade_mgr(pid,keyname,owner,div_code) values('";
			query += bean.getID()+"','"+keyname+"','"+owner+"','"+div_code+"')";
			bean.execute(query);
		}else msg = "이미 등록되어 있습니다.";
		owner="";	//clear
	}
	/*********************************************************************
	 	수정하기
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update mbom_grade_mgr set keyname='"+keyname+"',owner='"+owner+"' ";
		query += "where pid='"+pid+"'";
		bean.execute(query);
		owner="";	//clear
	}
	/*********************************************************************
	 	삭제하기
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from mbom_grade_mgr where pid='"+pid+"'";
		bean.execute(query);
		owner="";	//clear
	}
	
	/*********************************************************************
	 	권한관리 LIST가져오기
	*********************************************************************/
	String[] mgrColumn = {"a.pid","a.keyname","a.owner","a.div_code","b.ac_name"};
	bean.setTable("mbom_grade_mgr a,class_table b");		
	bean.setColumns(mgrColumn);
	query = "where (keyname='TBOM_MGR' or keyname='ECO_AUDIT') and (a.div_code = b.ac_code) order by keyname DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] data = new String[cnt][6];
	int n = 0;
	while(bean.isAll()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("keyname");
		data[n][2] = bean.getData("owner");		data[n][2] = data[n][2].replace(';',' ');
		data[n][3] = bean.getData("div_code");
		if(data[n][1].equals("TBOM_MGR")) data[n][4] = "임시BOM관리자";
		else if(data[n][1].equals("ECO_AUDIT")) data[n][4] = "BOM변경관리자";
		data[n][5] = bean.getData("ac_name");
		n++;
	} //while

	/*********************************************************************
	 	개별선택 내용보기
	*********************************************************************/
	if(mode.equals("VIEW")) {
		for(int i=0; i<n; i++) {
			if(data[i][0].equals(pid)) {
				keyname = data[i][1];
				owner = data[i][2];
				div_name = data[i][5];
			}
		}
	}

%>

<HTML>
<HEAD><TITLE>BOM 권한관리</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr" oncontextmenu="return false">
<LINK href="../css/style.css" rel=stylesheet>
<script language=javascript>
<!--
//메시지 출력
var msg = '<%=msg%>';
if(msg.length > 0) {
	alert(msg);
}
//등록하기
function write()
{
	var owner = document.sForm.owner.value;
	if(owner.length == 0) { alert('권한자를 먼저 입력하십시오.'); return; }

	document.sForm.action='BmProcessMgr.jsp';
	document.sForm.mode.value='ADD';
	document.sForm.submit();
}

//내용수정하기
function contentModify(pid)
{
	document.sForm.action='BmProcessMgr.jsp';
	document.sForm.mode.value='MOD';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//내용삭제하기
function contentDelete(pid)
{
	d = confirm("삭제하시겠습니까?");
	if(d == false) return;
	document.sForm.action='BmProcessMgr.jsp';
	document.sForm.mode.value='DEL';
	document.sForm.pid.value=pid;
	document.sForm.submit();
}

//내용보기
function contentView(pid)
{
	document.vForm.action='BmProcessMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.vForm.submit();
}
//권한자 찾기
function select()
{
	wopen("../searchReceiver.jsp?target=sForm.owner","mgr","510","467","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">

<!-- 상단 시작 -->
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top" >
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> BOM 관리자설정</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=><!--버튼 및 선택-->
		<TD vAlign=top><form name="sForm" method="post" style="margin:0"> 
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD height=32 align=left width='100%' style='padding-left:5px;'>
					<INPUT type='hidden' name='pid' value='<%=pid%>'>
					<select name='keyname'>
					<%
						String[] list = {"TBOM_MGR","ECO_AUDIT"};
						String[] list_name = {"임시BOM관리자","BOM변경관리자"};
						String sel="";
						
						for(int i=0; i<list.length; i++) {
							if(keyname.equals(list[i])) sel="selected";
							else sel = "";
							out.println("<option value='"+list[i]+"' "+sel+" >"+list_name[i]+"</option>");
						}
					%>
					<INPUT type='text' name='owner' value='<%=owner%>' size='16' readonly>
					<a href='javascript:select()'><img src='../images/bt_search2.gif' border='0' align='absmiddle'></a>
	
					<% if(owner.length() == 0) {
						out.println("<a href='javascript:write()'><IMG src='../images/bt_add.gif' border='0' align='absmiddle'></a>");
					} else {
						//out.println("<a href='javascript:contentModify("+pid+")'><IMG src='../images/bt_modify.gif' border='0' align='absmiddle'></a>");
						out.println("<a href='javascript:contentDelete("+pid+")'><IMG src='../images/bt_del.gif' border='0' align='absmiddle'></a>");
					}
					%>	
				</TD>
			</TR>
			<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
			<INPUT type="hidden" name="mode" value=''>
			<INPUT type="hidden" name="pid" value=''>
			</FORM> 
			</TBODY>
	</TR>
	</TBODY>
</TABLE>

<!-- 리스트 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR vAlign=middle height=25>
			<TD noWrap width=30 align=middle class='list_title'>번호</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>BOM관리자</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>사번/이름</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>부서명</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'></TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='9' align="middle">***** 내용이 없습니다. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<form name="vForm" method="post" style="margin:0">
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][4]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][2]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=data[i][5]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'></TD>
		</TR>
		<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for
	%>
	</TBODY>
</TABLE>

<INPUT type="hidden" name="mode" value=''>
<INPUT type="hidden" name="pid" value=''>
</form>
</body>
</html>

