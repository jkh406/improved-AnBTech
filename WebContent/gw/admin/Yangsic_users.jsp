<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkAP01.jsp"%>
<%@ 	page		
	info= "��������� ȯ��SETTING"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	/*****************************************************
	//	���� SETTING
	//****************************************************/
	String query = "";							//query���� �����
	String Message = "";						//�޽��� ����

	String[][] ysforms = new String[26][2];		
	for(int n=0; n<26; n++) for(int m=0; m<2; m++)  ysforms[n][m] = "";
	ysforms[0][0]="SL_001"; ysforms[0][1] = "�����";
	ysforms[1][0]="SL_002"; ysforms[1][1] = "�����û��";
	ysforms[2][0]="SL_003"; ysforms[2][1] = "��(��)����";
	ysforms[3][0]="SL_004"; ysforms[3][1] = "��������";
	ysforms[4][0]="SL_005"; ysforms[4][1] = "������û��";
	ysforms[5][0]="SL_006"; ysforms[5][1] = "������Ʈ�� �Ҹ�ǰ��û��";
	ysforms[6][0]="SL_007"; ysforms[6][1] = "����";
	ysforms[7][0]="SL_008"; ysforms[7][1] = "���庸��";
	ysforms[8][0]="SL_009"; ysforms[8][1] = "����ٹ� ��û��";
	ysforms[9][0]="SL_010"; ysforms[9][1] = "�����Ƿڼ�";
	ysforms[10][0]="SL_011"; ysforms[10][1] = "������";
	ysforms[11][0]="SL_012"; ysforms[11][1] = "����ǰ�Ǽ�";
	ysforms[12][0]="SL_013"; ysforms[12][1] = "����/����ڻ� �̵�����";
	ysforms[13][0]="SL_014"; ysforms[13][1] = "�԰�ǥ(�˼�Ȯ�μ�)";
	ysforms[14][0]="SL_015"; ysforms[14][1] = "����û��(ǥ)";
	ysforms[15][0]="SL_016"; ysforms[15][1] = "��ȼ�";
	ysforms[16][0]="SL_017"; ysforms[16][1] = "���Խ�û��";
	ysforms[17][0]="SL_018"; ysforms[17][1] = "������";
	ysforms[18][0]="SL_019"; ysforms[18][1] = "������";
	ysforms[19][0]="SL_020"; ysforms[19][1] = "��ü��������";
	ysforms[20][0]="SL_021"; ysforms[20][1] = "�系���߼۰���";
	ysforms[21][0]="SL_022"; ysforms[21][1] = "��ܹ߼۰���";
	ysforms[22][0]="SL_023"; ysforms[22][1] = "�μ�������";
	ysforms[23][0]="SL_024"; ysforms[23][1] = "��������������";
	ysforms[24][0]="SL_025"; ysforms[24][1] = "����������������";
	ysforms[25][0]="SL_026"; ysforms[25][1] = "�������������꼭";
	int fcnt = 26;		//��� �Ѱ���
		

	/*****************************************************
	// �� Setting�ϱ�
	*****************************************************/
	String no = request.getParameter("no");             if(no == null) no ="";
	String req = request.getParameter("req");             if(req == null) req ="";
	String ys_name = request.getParameter("ys_name");	  if(ys_name == null) ys_name = "";
	String ys_value = request.getParameter("ys_value");   if(ys_value == null) ys_value = "";
	String ys_option = request.getParameter("ys_option"+no); if(ys_option == null) ys_option = "";

	if(req.equals("ADD")) {
		//����˻��ϱ�
		String[] cColumns = {"name"};
		bean.setTable("USER_TABLE");
		bean.setColumns(cColumns);
		bean.setSearch("id",ys_value); 
		bean.init_unique();
		if(bean.isEmpty()) Message = "�Էµ� ����� �����ϴ�.";

		//�ߺ��Է� �˻��ϱ�
		String[] yColumns = {"ys_name","ys_value"};
		bean.setTable("YANGSIC_ENV");
		bean.setColumns(yColumns);
		bean.setClear();
		bean.setSearch("ys_name",ys_name,"ys_value",ys_value); 
		bean.init_unique();
		if(!bean.isEmpty()) Message = "�̹� ��ϵ� ����Դϴ�.";

		//����ϱ�
		if(Message.length() == 0) {
			query  ="insert into yangsic_env(ys_name,ys_value,ys_option) values('"+ys_name+"','";
			query +=ys_value+"','"+ys_option+"')";
			bean.execute(query);
		}
	}
	else if(req.equals("UPDATE")) {
		query="update yangsic_env set ys_option='"+ys_option+"' where ys_name='"+ys_name+"' and ys_value='"+ys_value+"'";
		bean.execute(query);
	}
	else if(req.equals("DELETE")) {
		query = "delete from yangsic_env where ys_name='"+ys_name+"' and ys_value='"+ys_value+"'";
		bean.execute(query);
	}
	
	/*****************************************************
	// ������ �����ϱ�
	*****************************************************/
	//1.��ĳ��� ��������
	String[] itemColumns = {"ys_name","ys_value","ys_option"};
	query = "where ys_name like 'SL_%' order by ys_name ASC";
	bean.setTable("YANGSIC_ENV");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(query); 
	bean.init_write();
	int cnt = bean.getTotalCount();

	//��Ĺ�ȣ,��ı��ѻ��,��ı���(����/����),�������̸�,����̸� �迭�����
	String[][] data = new String[cnt][5];		
	for(int n=0; n<cnt; n++) for(int m=0; m<5; m++) data[n][m] = "";

	int i=0;	//data
	while(bean.isAll()){
		data[i][0]=bean.getData("ys_name");		//����ڵ�
		for(int n=0; n<fcnt; n++)				//����̸�
			if(data[i][0].equals(ysforms[n][0])) data[i][4] = ysforms[n][1];
		data[i][1] = bean.getData("ys_value");	//���ѻ��
		data[i][2] = bean.getData("ys_option");	//���ѱ���
		i++;		
	}

	//2.����� ���� �̸� ��������
	String[] Columns = {"name"};
	bean.setTable("USER_TABLE");
	bean.setColumns(Columns);
	bean.setClear();
	for(int k=0; k<i; k++) {
		bean.setSearch("id",data[k][1]); 
		bean.init_unique();
		if(bean.isAll()) data[k][3] = bean.getData("name");
	}	
%>


<HTML>
<HEAD>
<LINK href="../css/style.css" rel=stylesheet>
<title>��İ��� ����ڱ��� ȯ��SETTING</title>
</HEAD>
<Script language = "Javascript">
 <!-- 
//�޽��� �����ϱ�
var msg = '<%=Message%>';
if(msg.length != 0) alert(msg);

//�����ϱ�
function userUpdate(a,b,c)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='UPDATE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.no.value=c;
	document.sForm.submit();
}
//�����ϱ�
function userDelete(a,b,c)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='DELETE';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.no.value=c;
	document.sForm.submit();
}
//����ϱ�
function userAdd()
{
	num = sForm.ys_code.selectedIndex;
	a = sForm.ys_code.options[num].value;
	b = document.sForm.ys_id.value;
	nm = sForm.ys_opt.length;
	for(i=0; i<nm; i++)
		if(sForm.ys_opt[i].checked) c = document.sForm.ys_opt[i].value;
	
	document.sForm.action="Yangsic_users.jsp";
	document.sForm.req.value='ADD';
	document.sForm.ys_name.value=a;
	document.sForm.ys_value.value=b;
	document.sForm.ys_option.value=c;
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
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle"> ��ĺ� ����� ���Ѽ���</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='100%'> 
				<!--//�Է�â �����-->
				&nbsp;
				������� <select name='ys_code'>
				<%			for(int n=0; n<fcnt; n++) {%>
								<option value='"+ysforms[n][0]+"'><%=ysforms[n][1]%>
						<%	}%>
						</select>
				&nbsp;
				����� ��� <input  class='inp' type='text' size=10 name='ys_id'>
				&nbsp;
				����� ���� <input type='radio' name='ys_opt' value='0' >view
							<input type='radio' name='ys_opt' value='1' >����
				&nbsp;		<a href="javascript:userAdd()"><img src='../images/bt_save.gif' border='0' align=absbottom></a></TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
					
 <TR height=100%><!--����Ʈ-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
	  <TBODY>
		<!--<TR height=23><TD colspan=12><p>
		&nbsp;&nbsp;&nbsp;[����� ���]<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - �� ��Ŀ� �ش�Ǵ� ��� ����ڸ� ����մϴ�.<br>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; - ����ں� ��� �������� ����մϴ�.<br><p>
        <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr></TD></TR>-->
		<!--<TR height=23><TD colspan=12><IMG src='../images/gw_yang_user.gif' align='absmiddle'></TD></TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>-->
		<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>NO</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>��ĸ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�̸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=12></TD></TR>
  
	<%	//ȭ�� ����ϱ�
		for(int n=0,m=1; n<i; n++,m++) {%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor="#ffffff">
				<TD align=middle height='24' class='list_bg'><%=m%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height='24' class='list_bg'>&nbsp;<%=data[n][4]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'><%=data[n][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'><%=data[n][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'>

		<%  if(data[n][2].equals("1")){%>
				<input type='radio' name="ys_option'<%=n%>'" value='0' >view
				<input type='radio' name="ys_option'<%=n%>'" value='1' checked >����
		<%  } else {%>
				<input type='radio' name="ys_option'<%=n%>'" value='0' checked >view
				<input type='radio' name="ys_option'<%=n%>'" value='1' >����
        <%  }%>
				</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height='24' class='list_bg'>
				<a href="javascript:userUpdate('<%=data[n][0]%>','<%=data[n][1]%>','<%=n%>')"><img src='../images/lt_modify.gif' border='0' align='absmiddle'></a> <a href="javascript:userDelete('<%=data[n][0]%>','<%=data[n][1]%>','<%=n%>')"><img src='../images/lt_del.gif' border='0' align='absmiddle'></a></TD>
			</TR>
			<TR><TD colSpan=16 background="../images/dot_line.gif"></TD></TR>
		<%	}%>
	  
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
