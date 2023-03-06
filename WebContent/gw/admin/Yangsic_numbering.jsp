<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "�������� ������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<%@	page import="com.anbtech.util.normalFormat"			%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	/*****************************************************
	//	���� SETTING
	//****************************************************/
	String query = "";							//query���� �����
	
	/*****************************************************
	// �� Setting�ϱ� (����/����/���)
	*****************************************************/
	String no = request.getParameter("no");             if(no == null) no ="";
	String req = request.getParameter("req");             if(req == null) req ="";
	String ys_name = request.getParameter("ys_name");	  if(ys_name == null) ys_name = "";
	String ys_value = Hanguel.toHanguel(request.getParameter("ys_value"));
		  if(ys_value == null) ys_value = "";
	String ac_name = Hanguel.toHanguel(request.getParameter("ac_name"+no));
		  if(ac_name == null) ac_name = "";
	String ac_alpa = Hanguel.toHanguel(request.getParameter("ac_alpa"+no));
		  if(ac_alpa == null) ac_alpa = "";

	if(req.equals("ADD")) {
		//1.�������� ���ϱ�
		String[] isColumns = {"ys_name","ys_value"};
		query = "where ys_name like 'DN_%' order by ys_name ASC";
		bean.setTable("YANGSIC_ENV");
		bean.setColumns(isColumns);
		bean.setSearchWrite(query); 
		bean.init_write();
		int ecnt = bean.getTotalCount();

		//������ȣ,������ �迭�����
		String[][] edata = new String[ecnt][2];		
		for(int n=0; n<ecnt; n++) for(int m=0; m<2; m++) edata[n][m] = "";

		int ei=0;	//data
		while(bean.isAll()){
			edata[ei][0] = bean.getData("ys_name");			//�����ڵ�
			edata[ei][1] = bean.getData("ys_value");		//�����̸�
			ei++;		
		}

		//2.����� �������� ���ϱ�
		String tstr = "0";
		if(ei != 0)
			tstr = edata[ei-1][0].substring(3,6);			//������������ ���� ���ϱ�
		
		int tint = Integer.parseInt(tstr);					//���������� �ٲٱ�	
		tint++;												//1�����ϱ�
		com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");
		ys_name = "DN_" + nmf.toDigits(tint);
		
		query  ="insert into yangsic_env(ys_name,ys_value) values('"+ys_name+"','"+ys_value+"')";
		bean.execute(query);
	}
	else if(req.equals("UPDATE")) {
		query="update yangsic_env set ys_value='"+ac_name+"', ys_option='"+ac_alpa+"' where ys_name='"+ys_name+"'";
		bean.execute(query);
	}
	else if(req.equals("DELETE")) {
		query = "delete from yangsic_env where ys_name='"+ys_name+"'";
		bean.execute(query);
	}

	/*****************************************************
	// ��ü �������� ������ �����ϱ� (��з����� ����)
	*****************************************************/
	//�������񳻿� ��������
	String[] itemColumns = {"ys_name","ys_value","ys_option"};
	query = "where ys_name like 'DN_%' order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int cnt = bean.getTotalCount();

	//��ȣ,��ĸ�,��ı����ڵ� �迭�����
	String[][] data = new String[cnt][3];		
	for(int n=0; n<cnt; n++) for(int m=0; m<3; m++) data[n][m] = "";

	int i=0;	//data
	while(bean.isAll()){
		data[i][0] = bean.getData("ys_name");		//�����ڵ�
		data[i][1] = bean.getData("ys_value");		//��Ŀ����̸�
		data[i][2] = bean.getData("ys_option");		//��ı�����
		i++;		
	}

%>


<HTML>
<HEAD>
<LINK href="../css/style.css" rel=stylesheet>
<title>�����ڵ����</title>
</HEAD>
<style type="text/css">
<!--
.inp {border:1 dotted #D9E8F2; height:18}
-->
</style>
<Script language = "Javascript">
 <!-- 

//�����ϱ�
function userUpdate(a,b)
{
	d = confirm("������ ��ϵ� ������ ������ �� �� �ֽ��ϴ�. ����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='UPDATE';
	document.sForm.ys_name.value=a;
	document.sForm.no.value=b;
	document.sForm.submit();
}
//�����ϱ�
function userDelete(a,b)
{
	d = confirm("������ ��ϵ� ������ ������ �� �� �ֽ��ϴ�. ����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='DELETE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.submit();
}
//����ϱ�
function userAdd()
{
	b = document.sForm.ys_desc.value;
	
	document.sForm.action="Yangsic_numbering.jsp";
	document.sForm.req.value='ADD';
	document.sForm.ys_value.value=b;
	document.sForm.submit();
}
-->
</Script>

<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form name="sForm" method="post">
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> �����ڵ����</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<!--//�Է�â �����-->
				&nbsp;&nbsp;
				��ĸ� <input type='text' size=20 name='ys_desc'>
				<a href="javascript:userAdd();"><img src='../images/bt_save.gif' border='0' align=absbottom></a> </TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
					
 <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
		<!--<TR height=23><TD colspan=12><p>
		&nbsp;&nbsp;&nbsp;[��ı����� ���� ���]<br>
			&nbsp;&nbsp;&nbsp; - ��ĸ�(�����ѱ�ǥ��)�� ������ϵ� �����Դϴ�.<br>
			&nbsp;&nbsp;&nbsp; - ��ĸ��� DB������ ��ı����ڷ� �����Ǿ��ֽ��ϴ�. �ݵ�� ������ �����ѱ�ǥ��Ƿ� ����մϴ�.<br>
			&nbsp;&nbsp;&nbsp; - ��ı����ڴ� ������ȣ�� �ο��Ǹ� ��ĺ� ������ȣ�� ���е˴ϴ�.<br>
			&nbsp;&nbsp;&nbsp; - ������ȣ ǥ�� : �μ��ڵ�,��ı�����,�⵵(2),-,�Ϸù�ȣ(3)<br>
			&nbsp;&nbsp;&nbsp; - ������ȣ ǥ�� ��) ��ȹ���� �ް��� : PAC03-001<br><p>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></TD></TR>
		<TR height=23><TD colspan=12><IMG src='../images/gw_yang_number.gif' align='absmiddle'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>-->
		<TR vAlign=middle height=23>
			  <!--<TD noWrap width=120 align=middle class='list_title'>��з���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>-->
			  <TD noWrap width=150 align=middle class='list_title'>�����ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>��ĸ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
  
	
<%		//ȭ�� ����ϱ�%>
		
		<!--<TD class=bgsetdw1 width='25%' rowspan='<%=i%>' align=center>����׸�</TD>-->
		
<%		for(int n=0; n<i; n++) {			//�� ��������
%>			<!--<TD><IMG height=1 width=1></TD>-->
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
			<TD height='24' class='list_bg' align=center><%=data[n][0]%>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<input size=25 type='text' name='ac_name<%=n%>' value='<%=data[n][1]%>'></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<input size=5 type='text' name='ac_alpa<%=n%>' value='<%=data[n][2]%>'></TD>
			<TD><IMG height=1 width=1></TD>
			<TD height='24' class='list_bg' align=center>
				<a href="javascript:userUpdate('<%=data[n][0]%>','<%=n%>')"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a> 
				<a href="javascript:userDelete('<%=data[n][0]%>','<%=data[n][1]%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a></TD>
			<TD height='24' class='list_bg' align=center>
			<TD><IMG height=1 width=1></TD>
			</TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%		}
		
	%>
	  <TR><TD colSpan=12 background="../images/dot_line.gif"></TD></TR>
	  </TBODY></TABLE>
		
	  <input type='hidden' name='ys_name'>
	  <input type='hidden' name='ys_value'>
	  <input type='hidden' name='ys_option'>
	  <input type='hidden' name='no'>
	  <input type='hidden' name='req'>
	  
</TBODY></TABLE>
</form>
</BODY>
</HTML>



	