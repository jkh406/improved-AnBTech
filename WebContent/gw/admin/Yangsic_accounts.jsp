<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "�������� ȯ��SETTING"		
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
	String[] alpha = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O",
						"P","Q","R","S","T","U","V","W","X","Y","Z"};
	String no = request.getParameter("no");             if(no == null) no ="";
	String req = request.getParameter("req");             if(req == null) req ="";
	String ys_name = request.getParameter("ys_name");	  if(ys_name == null) ys_name = "";
	String ys_value = Hanguel.toHanguel(request.getParameter("ys_value"));
		  if(ys_value == null) ys_value = "";
	String ac_name = Hanguel.toHanguel(request.getParameter("ac_name"+no));
		  if(ac_name == null) ac_name = "";

	if(req.equals("ADD")) {
		//��з� ���
		if(ys_name.equals("large")) {	
			//1.��з� �� ���ϱ�
			String[] lvColumns = {"ys_name","ys_value"};
			query = "where ys_name like 'AC_[A-Z]000'  order by ys_name ASC";
			bean.setTable("YANGSIC_ENV");
			bean.setColumns(lvColumns);
			bean.setSearchWrite(query); 
			bean.init_write();
			int lvcnt = bean.getTotalCount();

			//��з� ������ȣ,������ �迭�����
			String[][] vlist = new String[lvcnt][2];		
			for(int n=0; n<lvcnt; n++) for(int m=0; m<2; m++) vlist[n][m] = "";

			int jv=0;	
			while(bean.isAll()){
				vlist[jv][0] = bean.getData("ys_name");			//�����ڵ�
				vlist[jv][1] = bean.getData("ys_value");		//�����̸�
				jv++;		
			}

			//2.����� ��з��� ���ϱ�
			String str = vlist[jv-1][0].substring(3,4);	//��з� ���� �빮��
			int sn = 0;
			for(int n=0; n<26; n++) if(str.equals(alpha[n])) sn = n;
			ys_name = "AC_"+alpha[sn+1]+"000";
		}
		//�������� �з� ���
		else {
			//1.�������� ���ϱ�
			String[] isColumns = {"ys_name","ys_value"};
			query = "where ys_name like 'AC_%[^0]' order by ys_name ASC";
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
				edata[ei][0] = bean.getData("ys_name");		//�����ڵ�
				edata[ei][1] = bean.getData("ys_value");		//�����̸�
				ei++;		
			}

			//2.����� �������� ���ϱ�
			String hstr = ys_name.substring(0,4);				//�����ڵ��� �����κ�
			String hname = "";		//�ش������ ������ �����ڵ� ã��
			for(int m=0; m<ei; m++) {
				String cstr = edata[m][0].substring(0,4);
				if(hstr.equals(cstr)) hname =edata[m][0];
			}
			String tstr = "";		//�����ڵ��� ���ںκ�
			if(hname.length() == 0)	//ù ��з��� �����ڵ� ��Ͻ�
				tstr = "000";
			else
				tstr = hname.substring(4,hname.length());	//�����ڵ��� ���ںκ�
			int tint = Integer.parseInt(tstr);					//���������� �ٲٱ�	
			tint++;												//1�����ϱ�
			com.anbtech.util.normalFormat nmf = new com.anbtech.util.normalFormat("000");
			ys_name = hstr + nmf.toDigits(tint);
		}
		query  ="insert into yangsic_env(ys_name,ys_value) values('"+ys_name+"','"+ys_value+"')";
		bean.execute(query);
	}
	else if(req.equals("UPDATE")) {
		query="update yangsic_env set ys_value='"+ac_name+"' where ys_name='"+ys_name+"'";
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
	String[] itemColumns = {"ys_name","ys_value"};
	query = "where ys_name like 'AC_%[^0]' order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int cnt = bean.getTotalCount();

	//������ȣ,������ �迭�����
	String[][] data = new String[cnt][2];		
	for(int n=0; n<cnt; n++) for(int m=0; m<2; m++) data[n][m] = "";

	int i=0;	//data
	while(bean.isAll()){
		data[i][0] = bean.getData("ys_name");		//�����ڵ�
		data[i][1] = bean.getData("ys_value");		//�����̸�
		i++;		
	}

	/*****************************************************
	// ��з� ������ �����ϱ�
	*****************************************************/
	String[] lColumns = {"ys_name","ys_value"};
	query = "where ys_name like 'AC_[A-Z]000'  order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(lColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int lcnt = bean.getTotalCount();

	//��з� ������ȣ,������ �迭�����
	String[][] list = new String[lcnt][2];		
	for(int n=0; n<lcnt; n++) for(int m=0; m<2; m++) list[n][m] = "";

	int j=0;	
	while(bean.isAll()){
		list[j][0] = bean.getData("ys_name");		//�����ڵ�
		list[j][1] = bean.getData("ys_value");		//�����̸�
		j++;		
	}
	
	/*****************************************************
	// �ش� ��з��� ���ϴ� �������� ���� �ľ�
	*****************************************************/
	int[] ele_cnt = new int[j];
	for(int n=0; n<j; n++) {			//��з� 
		String large = list[n][0].substring(0,4);
		int s = 0;
		for(int m=0; m<i; m++) {		//�� ��������
			String small = data[m][0].substring(0,4);
			if(large.equals(small)) s++;
		}
		ele_cnt[n] = s;
	}
%>


<HTML>
<HEAD>
<LINK href="../css/style.css" rel=stylesheet>
<title>�����������</title>
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

	document.sForm.action="Yangsic_accounts.jsp";
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

	document.sForm.action="Yangsic_accounts.jsp";
	document.sForm.req.value='DELETE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.submit();
}
//����ϱ�
function userAdd()
{
	num = sForm.ys_code.selectedIndex;
	a = sForm.ys_code.options[num].value;
	b = document.sForm.ys_desc.value;
	
	document.sForm.action="Yangsic_accounts.jsp";
	document.sForm.req.value='ADD';
	document.sForm.ys_name.value=a;
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
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> �����������</TD>
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
				&nbsp;
				�����з� <select name='ys_code'>
							<option value='large'>��з�
<%							for(int n=0; n<j; n++) { %>
							<option value='<%=list[n][0]%>'><%=list[n][1]%>
<%}%>
						</select>
				&nbsp;
				������ <input type='text' size=15 name='ys_desc'>
				&nbsp;
				<a href="javascript:userAdd()"><img src='../images/bt_save.gif' border='0' align=absmiddle></a></TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
					
 <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
		<!--<TR height=23><TD colspan=12><p>
		&nbsp;&nbsp;&nbsp;[�������� ���]<br>
			&nbsp;&nbsp;&nbsp; - ��з� ���������� ����մϴ�.<br>
			&nbsp;&nbsp;&nbsp; - ��з��� �ش��ϴ� ���� ���������� ����մϴ�.<br>
			&nbsp;&nbsp;&nbsp; - ������ �ش� �����׸��� �������� �����մϴ�.<br><p>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></TD></TR>
		<TR height=23><TD colspan=12><IMG src='../images/gw_yang_account.gif' align='absmiddle'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=7></TD></TR>-->
		<TR vAlign=middle height=23>
			  <TD noWrap width=200 align=middle class='list_title'>��з���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�����ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>��������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
  
	<%	//ȭ�� ����ϱ�
		for(int n=0,m=0,num=1; n<j; n++,num++) {		//��з�%>
			<TR>
				<TD align=left height='24' class='list_bg' rowspan="<%=ele_cnt[n]%>" style="padding-left:20px">&nbsp;&nbsp;<%=list[n][1]%>	<%//��з���%>
<%				if(m == i) {	//��з� �ڵ常���� �ɶ� ������ �� �ֵ���
%>					&nbsp;&nbsp;&nbsp;<a href="javascript:userDelete('<%=list[n][0]%>','<%=list[n][1]%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a>
<%				}
%>				</TD>
<%				for(int x=m,y=0; x<i; x++,y++) {			//�� ��������
					if(y == ele_cnt[n]) break;
	%>					<TD><IMG height=1 width=1></TD>
						<TD align=middle height='24' class='list_bg' align=left >&nbsp;&nbsp;<%=data[x][0]%>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle height='24' class='list_bg' align=left>&nbsp;&nbsp;
							<input type='text' name="ac_name'<%=m%>'" value='<%=data[x][1]%>'></TD>
						<TD><IMG height=1 width=1></TD>
						<TD height='24' class='list_bg' align=center>
							<a href="javascript:userUpdate('<%=data[x][0]%>','<%=m%>')"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a>&nbsp;
							<a href="javascript:userDelete('<%=data[x][0]%>','<%=data[x][1]%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a></TD>
						<TD align=middle height='24' class='list_bg' align=center >
						<TD><IMG height=1 width=1></TD></TR>
<%					m++;
					}
%>			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<%		}
%>			
	</TBODY></TABLE>
		
	  <input type='hidden' name='ys_name'>
	  <input type='hidden' name='ys_value'>
	  <input type='hidden' name='no'>
	  <input type='hidden' name='req'>
	  
</TBODY></TABLE>
</form>
</BODY>
</HTML>



 