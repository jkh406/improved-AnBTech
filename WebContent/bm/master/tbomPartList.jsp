<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�ӽ� BOM Part List ��Ϻ���"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.0");		//�������

	//-----------------------------------
	//	������ PART ����Ʈ
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("STR_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();
	String model_code = request.getParameter("model_code")==null?"":request.getParameter("model_code");
%>

<HTML>
<HEAD><TITLE>�ӽ�BOM PART LIST</TITLE>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display()'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TD height="33" valign="middle" bgcolor="#73AEEF" colspan='2'><img src="../bm/images/pop_bom_infolist.gif" alt='�ӽ�BOM PART LIST' hspace="10"></TD></TBODY></TABLE></TD></TR>
		<TR><TD height='27' style='padding-left:5px;'>
			<IMG src='../bm/images/model_code.gif' border='0' align='absbottom'>&nbsp;<font color='#636ED9'><%=model_code%></font>
		</TD></TR>
		</TD></TR>
		<TR height=100%><!--����Ʈ-->
			<TD vAlign=top width='98%' style='padding-left:7px;'>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:scroll;">
				<TABLE cellSpacing=0 cellPadding=0 width="100%" align='center'>
					<TBODY>
					<TR><TD height='2' bgcolor='#9CA9BA' colspan='13'></TD></TR>
					<TR vAlign=middle height=23>
						  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=400 align=middle class='list_title'>ǰ��԰�</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>LOC.</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						  <TD noWrap width=60 align=middle class='list_title'>�����ڵ�</TD>
					</TR>			
					<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%	
			int cnt = 1;
			while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=1; i<lv; i++) space += "&nbsp;&nbsp;";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  	  <TD align=middle height="24" class='list_bg'><%=cnt%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'><%=table.getParentCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'><%=table.getChildCode()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=left class='list_bg'><%=table.getPartSpec()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getLocation()%></TD>
						  <TD><IMG height=1 width=1></TD>
						  <TD align=middle class='list_bg'><%=table.getOpCode()%></TD>
					</TR>
					<TR><TD colSpan=13 background="../pu/images/dot_line.gif"></TD></TR>
<%				
			cnt++;
		}  //while 
%>
				</TBODY></TABLE></DIV></TD></TR>

		<!--������-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<a href='javascript:self.close()'><IMG src="../bm/images/bt_close.gif" border='0'></a></TD></TR>
        <TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
     </TBODY></TABLE></TD></TR></TABLE>
</FORM>
</BODY>
</HTML>
<SCRIPT>
<!--
	function display() { 

    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 265;
	
	item_list.style.height = div_h;
}
-->
</SCRIPT>

