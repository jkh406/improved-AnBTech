<%@ include file="../../../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
<%@ page import="java.sql.*, java.io.*, java.util.*"%>
<jsp:useBean id="tree" class="com.anbtech.es.makeFormPaperTreeItems"/>
<%
	int n_id = request.getParameter("n_id")==null?0:Integer.parseInt(request.getParameter("n_id"));			// 자신의 노드 아이디
	String p_id = request.getParameter("p_id")==null?"1,":request.getParameter("p_id");	// 자신의 부모 노드 아이디

	StringTokenizer str = new StringTokenizer(p_id, ",");
	int spec_count = str.countTokens();
	String item[] = new String[spec_count];
%>

<HTML>
<TITLE></TITLE>
<head>
	<title>JavaScript Navigation Tree</title>
	<style>
	/* Style for tree item text */
	.t0i {
		font-family: Tahoma, Verdana, Geneva, Arial, Helvetica, sans-serif;
		font-size: 11px;
		color: #000000;
		background-color: #ffffff;
		text-decoration: none;
	}
	/* Style for tree item image */
	.t0im {
		border: 0px;
		width: 19px;
		height: 16px;
	}
	</style>
</head>

<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<base target='view'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width=195 border=0>
  <TBODY>
  <TR>
    <TD style="PADDING-LEFT: 15px; PADDING-TOP: 10px" vAlign=top background='../../../gw/img/left_bg.gif'>
		<script>
		var TREE_ITEMS = [
		 ['전자결재양식','FormPapersInfo.jsp?ag_name2=null&p_id=',    
			   ['외출계','FormPapersInfo.jsp?ag_name2=OE_CHUL&p_id=1,2,'],
			   ['출장신청서','FormPapersInfo.jsp?ag_name2=CHULJANG_SINCHEONG&p_id=1,3,'],
			   ['휴(공)가원','FormPapersInfo.jsp?ag_name2=HYU_GA&p_id=1,4,'], 
			   ['일반문서','FormPapersInfo.jsp?ag_name2=GENER&p_id=2,5,'],
			   ['보고서','FormPapersInfo.jsp?ag_name2=BOGO&p_id=2,6,'],
			   ['출장보고서','FormPapersInfo.jsp?ag_name2=CHULJANG_BOGO&p_id=2,7,'],    
			   ['기안서','FormPapersInfo.jsp?ag_name2=GIAN&p_id=3,8,'],
			   ['명함신청서','FormPapersInfo.jsp?ag_name2=MYEONGHAM&p_id=3,9,'],
			   ['사유서','FormPapersInfo.jsp?ag_name2=SAYU&p_id=3,10,'],
			   ['협조전','FormPapersInfo.jsp?ag_name2=HYEOPJO&p_id=3,11,'],   
			   ['연장근무신청서','FormPapersInfo.jsp?ag_name2=YEONJANG&p_id=4,12,'],   
			   ['구인의뢰서','FormPapersInfo.jsp?ag_name2=GUIN&p_id=5,13,'],   
			   ['교육일지','FormPapersInfo.jsp?ag_name2=GYOYUK_ILJI&p_id=6,14,'], 
		 ],
		];


		</script>
		<script language="JavaScript" src="tree.js"></script>
		<script language="JavaScript" src="tree_tpl.js"></script>
		<script language="JavaScript">
			new tree (TREE_ITEMS, tree_tpl);

		<%
			for(int i=0; i<spec_count; i++){ 
				item[i] = str.nextToken();
				out.print("trees['0'].toggle('"+item[i]+"');\n");
			}
		%>
		</script>

		<form name=f1 method=post action="FormPapersTree.jsp">
		<input type=hidden name=n_id value="<%=n_id%>">
		<input type=hidden name=p_id value="<%=p_id%>">
		</form>
	</TD>
  </TR>
  </TBODY>
</TABLE>
</BODY>
</HTML>