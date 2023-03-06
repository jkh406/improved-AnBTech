<%@ page		
	info= "TEXT PDF변환내용"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	String file_path = (String)request.getAttribute("file_path");			//file path
	String save_file = (String)request.getAttribute("save_file");			//save_file
	String org_file = (String)request.getAttribute("org_file");				//orginal_file
	String file_size = (String)request.getAttribute("file_size");			//파일 크기
	String ftype = "application/pdf";

%>

<HTML>
<HEAD><TITLE>TEXT PDF변환내용</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../sc/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../sc/images/blet.gif">TEXT PDF변환</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32<!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px'>
						
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../sc/images/bg-01.gif">다운로드</td>
			   <td width="87%" height="25" class="bg_04">
					<a href='../sc/pdf_download.jsp?fname=<%=org_file%>&ftype=<%=ftype%>&fsize=<%=file_size%>&sname=<%=save_file%>&fpath=<%=file_path%>'><%=org_file%></a>
					</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>



