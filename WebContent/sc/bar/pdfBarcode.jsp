<%@ page		
	info= "���ڵ� ��������"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	String file_path = (String)request.getAttribute("file_path");			//file path
	String save_file = (String)request.getAttribute("save_file");			//save_file
	String file_size = (String)request.getAttribute("file_size");			//���� ũ��
	String ftype = "application/pdf";

	String barcode_type = (String)request.getAttribute("barcode_type");		//���ڵ�Ÿ��
	String barcode = (String)request.getAttribute("barcode");				//���ڵ�
	String paper_type = (String)request.getAttribute("paper_type");			//���ؿ���
	String barcode_count = (String)request.getAttribute("barcode_count");	//��¼���
	String used_count = (String)request.getAttribute("used_count");			//���ȼ���
	String goods_name = (String)request.getAttribute("goods_name");			//��ǰ��
	String serial_no = (String)request.getAttribute("serial_no");			//�Ϸù�ȣ

%>

<HTML>
<HEAD><TITLE>���ڵ� ��������</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../sc/css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0">

<TABLE height="100%"  width="100%" cellSpacing=0 cellPadding=0 border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top >
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="" hspace="10" alt='BAR CODE ��³���'></TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR>
					</TBODY></TABLE></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR><TD height='5' bgcolor='#FFFFFF'></TD></TR>
	<TR height=100%><!--����Ʈ-->
		<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			<TBODY>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ڵ� Type</td>
				   <td width="60%" height="25" class="bg_04"><%=barcode_type%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ڵ� ������</td>
				   <td width="60%" height="25" class="bg_04"><%=barcode%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ؿ���</td>
				   <td width="60%" height="25" class="bg_04"><%=paper_type%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">��¼���</td>
				   <td width="60%" height="25" class="bg_04"><%=barcode_count%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">���ȼ���</td>
				   <td width="60%" height="25" class="bg_04"><%=used_count%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">��ǰ��</td>
				   <td width="60%" height="25" class="bg_04"><%=goods_name%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">�Ϸù�ȣ</td>
				   <td width="60%" height="25" class="bg_04"><%=serial_no%></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
				<tr>
				   <td width="40%" height="25" class="bg_03" background="../sc/images/bg-01.gif">�ٿ�ε�</td>
				   <td width="60%" height="25" class="bg_04"><a href='../sc/pdf_download.jsp?fname=<%=save_file%>&ftype=<%=ftype%>&fsize=<%=file_size%>&sname=<%=save_file%>&fpath=<%=file_path%>'><%=save_file%></a></td>
				</tr>
				<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			</TBODY></TABLE></TD></TR>
		<!--������-->
        <TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
			<a href='javascript:self.close()'><img src='../sc/images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
        <TR><TD width="100%" height=3 bgcolor="0C2C55"></TD></TR></TD></tr>
		</TABLE></TBODY></TABLE>
</BODY>
</HTML>