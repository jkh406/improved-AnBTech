<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "BOM ���� Frame"		
	contentType = "text/html; charset=euc-kr"                                         		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<%
	//parameter ���޹ޱ�
	String fg_code = request.getParameter("fg_code"); if(fg_code == null) fg_code = "";
	String eco_no = request.getParameter("eco_no"); if(eco_no == null) eco_no = "";
	String para = "fg_code="+fg_code+"&eco_no="+eco_no;

%>

<HTML>
<HEAD>
<TITLE> Cbom Frame</TITLE>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>
<BODY onload='hideScrollBar()'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27>
			<TD vAlign=top><!-- Ÿ��Ʋ �� ������ ���� -->
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%" >
				<TBODY>
					<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD"><TD valign='middle' class="title" style='padding-left:5px'><img src="../images/blet.gif" align="absmiddle"> BOM ���� [���躯��]</TD></TR></TBODY></TABLE></TD></TR>
		<TR><TD>
			<IFRAME name="search" src="CbomFrame_main.jsp?fg_code=<%=fg_code%>&eco_no=<%=eco_no%>&para=<%=para%>" width="100%" height="100%" border="0" frameborder="0" scrolling="no">�������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</IFRAME>
		</TD></TR>
</BODY>
</HTML>

<script language='javascript'>
function hideScrollBar(){
		parent.document.all.view.scrolling.value = "no";
	}
</script>