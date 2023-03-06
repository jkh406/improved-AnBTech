<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "문서검색하기 : 기술문서에 문서종류등록"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjtWord="0",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
	}

	//-----------------------------------
	//	문서 LIST
	//-----------------------------------
	//해당멤버의 전체 과제 LIST
	com.anbtech.pjt.entity.projectTable node;
	ArrayList node_list = new ArrayList();
	node_list = (ArrayList)request.getAttribute("NODE_List");
	node = new projectTable();
	Iterator node_iter = node_list.iterator();
	int node_cnt = node_list.size();

	String[][] document;
	if(node_cnt > 0) document = new String[node_cnt][2];		//데이터가 있을때
	else document = new String[1][3];						//데이터가 없을때
	int p = 0;
	while(node_iter.hasNext()) {
		node = (projectTable)node_iter.next();
		document[p][0] = node.getChildNode();
		document[p][1] = node.getNodeName();
		p++;
	}

%>
<HTML>
<HEAD>
<title>문서종류 찾기 </title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//문서종류선택하기
function goDocument()
{
	var doc = document.eForm.doc.value;
	var node = doc.split('|');
	opener.document.writeForm.node_code.value=node[0];
	opener.document.writeForm.node_name.value=node[1];
	self.close();
}

-->
</script>

<LINK href="../pjt/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 문서종류 LIST</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR><TD height='2' ></TD></TR>
			<TR>
				<TD align='middle' width='100%'>
					<form name="eForm" method="post" style="margin:0">
					<%
						out.println("<select MULTIPLE size=11 name='doc' onChange='javascript:goDocument()' style=font-size:9pt;color='black';>");
						out.println("<OPTGROUP label='----------------------'>");
						String psel = "";
						for(int i=0; i<node_cnt; i++) {
							if(pjt_code.equals(document[i][0])) psel = "selected"; 
							else psel = "";
							out.print("<option "+psel+" value='"+document[i][0]+"|"+document[i][1]+"'>");
							out.println(document[i][0]+" "+document[i][1]+"</option>");
						}
						out.println("</select>");
					%>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
					<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="">
					<input type="hidden" name="level_no" size="15" value="0">
					<input type="hidden" name="parent_node" size="15" value="0">
					<input type="hidden" name="pid" size="15" value="">
					</form>
				</TD>
			</TR>
			<TR><TD height='2' ></TD></TR>
			<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
			<TR><TD height='2' ></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	
	</TBODY>
</TABLE>

</BODY>
</HTML>