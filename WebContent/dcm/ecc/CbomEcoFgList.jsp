<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "��[FG] �˻��ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	
	//----------------------------------------------------
	//	�Ķ���� �б�
	//----------------------------------------------------
	String fg_code	= (String)request.getAttribute("fg_code");			//FG code
	String eco_no	= (String)request.getAttribute("eco_no");				//���Ժ��� ��ȣ
	String gid		= (String)request.getAttribute("gid");					//�����ڵ�

	//----------------------------------------------------
	//	������ �ش�ǰ�� ���� �б�
	//----------------------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("FG_List");
	com.anbtech.dcm.entity.eccModelTable table;
	table = new eccModelTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD><title>���� �˻����</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();' oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
						<TR>
							<TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../dcm/images/pop_tmodel.gif" alt='����[FG]��ȸ LIST' border='0' align='absmiddle'></TD></TR>
							<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY>
							<TD height="23" valign="middle"></TD></TR>						
				</TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--����Ʈ-->
		<TR height=100%>
		    <TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:120; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 border=0  align='center' width='100%'>
			        <TBODY>
						<TR vAlign=middle height=23>
							<TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
							<TD noWrap width=6 class='list_title'><IMG 	src="../dcm/images/list_tep2.gif"></TD>
							<TD noWrap width=100 align=middle class='list_title'>�����</TD>
							<TD noWrap width=6 class='list_title'><IMG src="../dcm/images/list_tep2.gif"></TD>
							<TD noWrap width=100 align=middle class='list_title'>����F/G�ڵ�</TD>
							<TD noWrap width=6 class='list_title'><IMG 	src="../dcm/images/list_tep2.gif"></TD>
							<TD noWrap width=100% align=middle class='list_title'>�𵨸�</TD>
						</TR>
						<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>

			<%
						int n=1;
						while(table_iter.hasNext()){
						table = (eccModelTable)table_iter.next();
	%>	
						<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor='#ffffff' height='25'>
							<TD align=middle class='list_bg'><%=n%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getModelCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getFgCode()%></td>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getModelName()%></td>
						<TR><TD colSpan=11 background="../bm/images/dot_line.gif"></TD></TR>			
	<% 	
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR><TD height=5 colspan="2"></TD></TR>
		<!--������-->
		<TR><TD>
			<TABLE cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR><TD width='100%' height="33" align="right" bgcolor="#73AEEF" style='padding-right:4px'>
						<A href='javascript:self.close()'><img src='../dcm/images/bt_close.gif' border='0' align='absright'></a></TD></TR>
					<TR><TD height="" bgcolor="0C2C55" colspan='2'></TD></TR></TBODY></TABLE></TD></TR>		
</TBODY></TABLE>

<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<input type='hidden' name='mode' value=''>
<input type='hidden' name='gid' value='<%=gid%>'>
<input type='hidden' name='eco_no' value='<%=eco_no%>'>
<input type='hidden' name='fg_code' value='<%=fg_code%>'>
</FORM>
</BODY>
</HTML>


<script language=javascript>
<!--
// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	//var div_w = w - 430 ; 
	var div_h = h - 472;
	item_list.style.height = div_h;
}
-->
</script>
