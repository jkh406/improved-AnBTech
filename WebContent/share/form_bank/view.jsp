<%@ include file= "../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage="../../admin/errorpage.jsp"
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.share.entity.*"%>
<%
	// �������� Helper Class
	com.anbtech.share.entity.ShareBdTable sbTable = new com.anbtech.share.entity.ShareBdTable();
	com.anbtech.share.entity.ShareLinkTable redirect;

	ArrayList arry = new ArrayList();
	Iterator file_iter;

	com.anbtech.share.entity.ShareParameterTable sbpara = new com.anbtech.share.entity.ShareParameterTable();

	sbpara   = (ShareParameterTable)request.getAttribute("sbParameter");
	String category		= sbpara.getCategory();
	String tablename	= sbpara.getTableName();
	String mode			= sbpara.getMode();
	boolean bool		= sbpara.getBool();
	String id			= sbpara.getId();
	
	redirect = new com.anbtech.share.entity.ShareLinkTable();
	redirect = (ShareLinkTable)request.getAttribute("Redirect");
	String view_pagecut		= redirect.getViewPagecut();
	String input_hidden_search = redirect.getInputHidden();
	String view_total		= redirect.getViewTotal();
	String view_boardpage	= redirect.getViewBoardpage();
	String view_totalpage	= redirect.getViewTotalpage();
	String link_list		= redirect.getLinkList();
	String link_modify		= redirect.getLinkModify();
	String link_delete		= redirect.getLinkDelete();

	sbTable = (ShareBdTable)request.getAttribute("shareBdTable");
	String wdate	= sbTable.getWdate();		
	String wid		= sbTable.getWid();
	String category_temp = sbTable.getCategory();
	
%>

<HTML>
<HEAD><TITLE>��Գ�����</TITLE>
<META http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK rel="stylesheet" href="../share/css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM method='GET' name='frm' style='margin:0'>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><IMG src="../share/images/blet.gif"> ��Ĳٷ��� </TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5></TD>
					<TD align=left width=30><%=link_list%><img src="../share/images/bt_list.gif" border="0"></a></TD>
					<TD width=4></TD>
			  <% if(id.equals(wid) || bool==true) { %>
					<TD align=left width=30><%=link_modify%><img src="../share/images/bt_modify.gif" border="0"></a></TD>
					<TD width=4></TD>
					<TD align=left width=30><%=link_delete%><img src="../share/images/bt_del.gif" border="0" onClick="if(confirm('�����Ͻðڽ��ϱ�?')) return true; return false;"></a></TD>
					<TD width=4></TD>
			  <%}%>	</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
    <!--�⺻����-->
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR><TD height="25" colspan="4"><img src="../share/images/share_docinfo.gif" width="209" height="25" border="0"></td></tr>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">��ĸ�</td>
				<TD width="37%" height="25" class="bg_04" colspan='3'><%=sbTable.getSubject()%></td></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">��Ĺ�ȣ</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getDocNo()%></td>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">����</td>
				<TD width="87%" height="25" class="bg_04" ><%=sbTable.getVer()%></td></tr>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">�о�</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getCategory()%></td>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">�ְ��μ�</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getAcName()%></td>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">÷��ȭ��</td>
				<TD width="87%" height="25" class="bg_04" colspan='3'><%=sbTable.getFlink()%></td>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">÷��ȭ������</td>
				<TD width="87%" height="25" class="bg_04" colspan='3'><%=sbTable.getContent()%></td>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">�����</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getWname()%></td>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">�������</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getWdate()%></td></tr>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<%if(!sbTable.getMname().equals("") && sbTable.getMname()!=null) {%>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">������</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getMname()%></td>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">��������</td>
				<TD width="37%" height="25" class="bg_04"><%=sbTable.getMdate()%></td></tr>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></td></tr>
			<%}%>
			</TBODY></TABLE></TD></TR></TABLE>

	<INPUT type='hidden' name='no' value='<%=sbTable.getNo()%>'>
	<INPUT type='hidden' name='mode' value='delete'>
</BODY>
</FORM>
</HTML>