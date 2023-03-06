<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "전사공통 프로세스 구성하기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.prsStandardDAO stdDAO = new com.anbtech.pjt.db.prsStandardDAO(con);

	/*********************************************************************
	 	parameter 받기
	*********************************************************************/
	String prs_code = request.getParameter("prs_code");	if(prs_code == null) prs_code = "P001";
	String url = request.getParameter("url");	if(url == null) url = "../../servlet/prsStandardServlet";
	
	/*********************************************************************
	 	프로세스 Tree만들기
	*********************************************************************/
	String JS = "";
	JS = stdDAO.makeProcessTree(prs_code,"0","0","P",url);			//P:전사공통, 부서코드:부서공통
	int cnt = stdDAO.getAllTotalCount(prs_code,"P");				//총갯수
	if(cnt == 0) JS = "var TREE_ITEMS = [ ];";
	connMgr.freeConnection("mssql",con);							//Connection 닫기

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<script language='JavaScript' src='processtree.js'></script>
<script language='JavaScript'>
<!--
var tree_tpl = {
	'target'  : 'viewR',	// name of the frame links will be opened in
							// other possible values are: _blank, _parent, _search, _self and _top

	'icon_e'  : 'icons/empty.gif', // empty image
	'icon_l'  : 'icons/line.gif',  // vertical line
	
	'icon_48' : 'icons/base.gif',   // root icon normal
	'icon_52' : 'icons/base.gif',   // root icon selected
	'icon_56' : 'icons/base.gif',   // root icon opened
	'icon_60' : 'icons/base.gif',   // root icon selected
	
	'icon_16' : 'icons/folder.gif', // node icon normal
	'icon_20' : 'icons/folderopen.gif', // node icon selected
	'icon_24' : 'icons/folder.gif', // node icon opened
	'icon_28' : 'icons/folderopen.gif', // node icon selected opened

	'icon_0'  : 'icons/folder.gif',		// leaf icon normal			(page --> 수정20030526)
	'icon_4'  : 'icons/folderopen.gif', // leaf icon selected		(page --> 수정20030526)
	'icon_8'  : 'icons/folder.gif',		// leaf icon opened			(page --> 수정20030526)
	'icon_12' : 'icons/folderopen.gif', // leaf icon selected		(page --> 수정20030526)
	
	'icon_2'  : 'icons/joinbottom.gif', // junction for leaf
	'icon_3'  : 'icons/join.gif',       // junction for last leaf
	'icon_18' : 'icons/plusbottom.gif', // junction for closed node
	'icon_19' : 'icons/plus.gif',       // junctioin for last closed node
	'icon_26' : 'icons/minusbottom.gif',// junction for opened node
	'icon_27' : 'icons/minus.gif'       // junctioin for last opended node
};
-->
</script>

</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> 전사 표준프로세스</TD>
			</TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<script language='JavaScript'>
<!--
//Tree 만들기
<%=JS%>
	new tree (TREE_ITEMS, tree_tpl);

//노드폴더 전부 펼치기
var cnt = '<%=cnt%>';
for(i=1; i<cnt; i++)  trees[0].toggle(i);
-->
</script>

<form name="sForm" method="post" style="margin:0">
<input type='hidden' name='prs_code' value=''>
<input type='hidden' name='url' value=''>
</form>

</body>
</html>