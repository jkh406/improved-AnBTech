<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= "�����ǰ �񱳰��"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "java.sql.Connection"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	
	//----------------------------------------------------
	//	�Ķ���� �б�
	//----------------------------------------------------
	String fg_comp = (String)request.getAttribute("fg_comp");			//���/����FG �񱳰��
	String item_comp = (String)request.getAttribute("item_comp");		//�����ǰ �񱳰��
	
%>

<HTML>
<HEAD><title>�����ǰ �񱳰��</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0"  oncontextmenu="return false">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY><!-- Ÿ��Ʋ �� ������ ���� -->
		<TR height=27><TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../dcm/images/pop_chgitem_chk.gif" align="absmiddle" alt='�����ǰ��'> </TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
				</TD></TR>
		<TR bgColor='' height=10><TD></TD></TR>
  
	<!--����Ʈ-->
	<TR height=100%><TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=8 width="98%" border=0 align=center>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
				<TR><TD height='1' bgcolor='#9DA8BA' colspan='4'></TD></TR>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<td width="20%" height="50" class="bg_03" background="../dcm/images/bg-01.gif" valign='top'>�𵨺�</td>
					<td width="80%" height="50" class="bg_04">
						<!-- TABLE ���� �˻系��-->
						<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TBODY><TR><TD valign='top'>
						<DIV id="assy_check" style="position:relative; visibility:visible; width:100%; height:30; overflow-x:auto; overflow-y:scroll;">
						<%=fg_comp%>
						</DIV>
						</TD></TR></TBODY></TABLE>
						<!--------------------->
					</td>			
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					<td width="20%" height="50" class="bg_03" background="../dcm/images/bg-01.gif"  valign='top'>�����ǰ��</td>
					<td width="80%" height="100" class="bg_04">
						<!-- TABLE ���� �˻系��-->
						<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TBODY><TR><TD valign='top'>
							<DIV id="item_list" style="position:relative; visibility:visible; width:100%; height:100; overflow-x:auto; overflow-y:scroll;">	<%=item_comp%>
							</DIV>
						</TD></TR></TBODY></TABLE>
						<!--------------------->
					</td>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>	</TBODY></TABLE></TD></TR>
				<!--������-->
				
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'>
					<img src='../dcm/images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
				</TD></TR>
		</TABLE></TBODY></TABLE>
</BODY>
</HTML>