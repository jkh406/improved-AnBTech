<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "프로세스 선택"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%

	/*********************************************************************
	 	해당자 정보 알아보기 (대상자) : 대상자 정보 [공통]
	*********************************************************************/
	String query = "";
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	String div_code = "";			//작성자 부서코드
	while(bean.isAll()) {
		div_code = bean.getData("ac_code");				//기안자 부서코드
	} //while

	/*********************************************************************
	 	프로세스 코드 및 이름 찾기
	*********************************************************************/
	String[] prsColumn = {"prs_code","prs_name","type"};
	bean.setTable("prs_name");		
	bean.setColumns(prsColumn);
	query = "where type='"+div_code+"' order by prs_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] prs = new String[cnt][2];
	int n = 0;
	while(bean.isAll()) {
		prs[n][0] = bean.getData("prs_code");				
		prs[n][1] = bean.getData("prs_name");
		n++;			
	} //while
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//선택하기
function selPrs()
{
	var code = document.sForm.prs_code.value;
	var url = '../../servlet/prsStandardServlet';

	//TREE 실행하기
	parent.viewL.document.sForm.action='processDiv_tree.jsp';
	parent.viewL.document.sForm.prs_code.value=code;
	parent.viewL.document.sForm.url.value=url;
	parent.viewL.document.sForm.div_code.value='<%=div_code%>';
	parent.viewL.document.sForm.submit();

	//NODE 실행하기
	parent.viewR.document.sForm.action = '../servlet/prsStandardServlet';
	parent.viewR.document.sForm.mode.value ='STD_LD';
	parent.viewR.document.sForm.submit();
}
-->
</script>

<LINK href="../css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<form name="sForm" method="post" style="margin:0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 프로세스을 선택</TD>
			</TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD align=left width='400'>프로세스명
					<select name="prs_code" style=font-size:9pt;color="black"; onChange='javascript:selPrs()';>  
					<%
						for(int si=0; si<prs.length; si++) {
							out.println("<option value='"+prs[si][0]+"'>"+prs[si][0]+" "+ prs[si][1]+"</option>");
						}
					%>
					</select>
				</TD>
			</TR>
			<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>
</form>

</BODY>
</HTML>

