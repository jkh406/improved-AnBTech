<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String mode				= request.getParameter("mode");
	String request_no		= request.getParameter("request_no");		//�˻��Ƿڹ�ȣ
	String item_code		= request.getParameter("item_code");		//�˻���ǰ���ڵ�
	String inspection_code	= request.getParameter("inspection_code");	//�˻��׸��ڵ�
	String sampled_quantity	= request.getParameter("sampled_quantity");	//�÷����

	//����Ʈ
	ArrayList inspection_value_list = new ArrayList();
	inspection_value_list = (ArrayList)request.getAttribute("INSPECTION_VALUE_LIST");
	Iterator table_iter = inspection_value_list.iterator();
%>

<HTML><HEAD><TITLE>�˻系��</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();'>
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../qc/images/pop_chk_info.gif" border='0' alt="�˻系��" align='absmiddle'></TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=19><TD></TD></TR>
  
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		    <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			    <TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
					<TR vAlign=middle height=23>
						  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>����ġ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
						  <TD noWrap width=120 align=middle class='list_title'>����</TD>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
			int no = 1;
			String[] values = new String[2];
			while(table_iter.hasNext()){
				values = (String[])table_iter.next();
				String inspection_value = values[0];
				String is_passed = values[1];
				if(is_passed.equals("Y")) is_passed = "�հ�";
				else is_passed = "���հ�";
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=no%></td>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=inspection_value%></td>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=is_passed%></td>
					</TR>
					<TR><TD colSpan=5 background="../qc/images/dot_line.gif"></TD></TR>
<%				no++;
	}	
%>	
				</TBODY></TABLE></DIV></TD></TR>
				<!--������-->
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'>
					<img src='../qc/images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
			</TD></TR></TABLE></TBODY></TABLE>
</BODY>
</HTML>

<SCRIPT language='javascript'>
<!--
//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 585;
	item_list.style.height = div_h;
}
-->
</SCRIPT>