<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "������û�� 2���μ� ������"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //���ڰ��系�� & ���缱

	//������û�� �������
	String query = "";
	String c_id = "";				//����������ȣ
	String v_no = "";				//������ȣ
	String v_model = "";			//��������
	String in_date = "";			//��û����
	String wyear = "";				//�ۼ���
	String wmonth = "";				//	  ��
	String wdate = "";				//	  ��
	String ac_name = "";			//�ҼӺμ���
	String user_name = "";			//����� ��
	String user_rank = "";			//����� ����
	String fellow_names = "";		//������	 ���/�̸�;
	String f_names = "";			//������	 �̸�,
	String u_year = "";				//������û���� ��
	String u_month = "";			//������û���� ��
	String u_date = "";				//������û���� ��
	String u_time = "";				//������û���� ��
	String tu_year = "";			//������û���� ��
	String tu_month = "";			//������û���� ��
	String tu_date = "";			//������û���� ��
	String tu_time = "";			//������û���� ��
	String purpose = "";			//����
	String cr_dest = "";			//�༱��
	String content = "";			//��������
	String em_tel = "";				//��޿���ó

	//���缱 ����
	String doc_id = "";				//���ڰ��� ������ȣ
	String link_id = "";			//���ù��� ������ȣ
	String line="";					//�������� ���缱
	String r_line = "";				//���ۼ����� �Ѱ��ֱ�
	String vdate = "";				//������ ���� ����
	String ddate = "";				//������ ���� ����
	String wid = "";				//����ڻ��
	String vid = "";				//�����ڻ��
	String did = "";				//�����ڻ��
	String wname = "";				//�����
	String vname = "";				//������
	String dname = "";				//������
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2���� ���� ����
	String line2="";				//�������� ���缱
	String writer_id = "";			//����� ���
	String writer_name = "";		//����� ��

	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while

	//*********************************************************************
	// 2�� �������� ���ù��� ������ȣ ���� �ޱ�
	//*********************************************************************
	link_id = request.getParameter("link_id");	if(link_id == null) link_id = "";	//���ù��� ������ȣ

	//*********************************************************************
	// 1�� �ְ��μ� ���缱 ���� �ޱ�
	//*********************************************************************
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine rline = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//���缱
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		rline = (TableAppLine)line_iter.next();
										
		if(rline.getApStatus().equals("���")) {
			wname = rline.getApName();	//�����
			wid = rline.getApSabun();	//����� ���
		}
		if(rline.getApStatus().equals("����"))  {
			vname = rline.getApName();	//������
			vid = rline.getApSabun();	//������ ���
			vdate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)
		}
		if(rline.getApStatus().equals("����"))  {
			dname = rline.getApName();	//������
			did = rline.getApSabun();	//������ ���
			ddate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)\
		}
			
		line += rline.getApStatus()+" "+rline.getApSabun()+" "+rline.getApName()+" "+rline.getApRank()+" "+rline.getApDivision()+" "+rline.getApDate()+" "+rline.getApComment()+"<br>";
	}

	/*********************************************************************
	// 	���� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"c_id","in_date","ac_name","user_name","user_rank","fellow_names",
				"u_year","u_month","u_date","u_time","tu_year","tu_month","tu_date","tu_time",
				"cr_purpose","cr_dest","content","em_tel"};
	bean.setTable("charyang_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (cr_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		c_id = bean.getData("c_id");					//����������ȣ
		in_date = bean.getData("in_date");				//��û����
		ac_name = bean.getData("ac_name");				//�ҼӺμ���
		user_name = bean.getData("user_name");			//����� ��
		user_rank = bean.getData("user_rank");			//����� ����
		fellow_names = bean.getData("fellow_names");	//������	 ���/�̸�;
		u_year = bean.getData("u_year");				//������û���� ��
		u_month = bean.getData("u_month");				//������û���� ��
		u_date = bean.getData("u_date");				//������û���� ��
		u_time = bean.getData("u_time");				//������û���� ��
		tu_year = bean.getData("tu_year");				//������û���� ��
		tu_month = bean.getData("tu_month");			//������û���� ��
		tu_date = bean.getData("tu_date");				//������û���� ��
		tu_time = bean.getData("tu_time");				//������û���� ��
		purpose = bean.getData("cr_purpose");			//����
		cr_dest = bean.getData("cr_dest");				//�༱��
		content = bean.getData("content");				//��������
		em_tel = bean.getData("em_tel");				//��� ����ó
	} //while	

	//�ۼ������ ���ϱ�
	wyear = in_date.substring(0,4);		//�ۼ���
	wmonth = in_date.substring(5,7);	//	  ��
	wdate = in_date.substring(8,10);	//	  ��

	//������ �̸��� ���ϱ�
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
		
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String n = name.nextToken();
			if(nm == 1) f_names += n + ",";
			nm++;
			if(nm > 2) break;
		}
	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

	//���� ����
	String[] carColumn = {"car_no","model_name"};
	bean.setTable("car_info");			
	bean.setColumns(carColumn);
	bean.setOrder("car_no ASC");	
	query = "where (cid ='"+c_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		v_no = bean.getData("car_no");
		v_model = bean.getData("model_name");
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//���缱
%>


<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> ���������û��</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<form action="baecha_sincheong_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=64% align=left valign='top'><%=line%></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
						if(vdate.length() == 0)	{//������
						if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("����");
						} else {
						out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;
					<%
						if(ddate.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
				%></TD>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=v_no%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�𵨸�</td>
           <td width="37%" height="25" class="bg_04"><%=v_model%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����Ͻ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=u_year%>�� <%=u_month%>�� <%=u_date%>�� <%=u_time%> ~  <%=tu_year%> ��<%=tu_month%>�� <%=tu_date%>�� <%=tu_time%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�༱��</td>
           <td width="37%" height="25" class="bg_04"><%=cr_dest%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���������</td>
           <td width="37%" height="25" class="bg_04"><%=user_rank%> <%=user_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��޿���ó</td>
           <td width="37%" height="25" class="bg_04"><%=em_tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=f_names%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��Ÿ����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=content%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr><td height=5 colspan="4"></td></tr></tbody></table>

  <tr><td align="center">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%>�� <%=wmonth%>�� <%=wdate%>��</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û�μ���</td>
           <td width="37%" height="25" class="bg_04"><%=ac_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr><td height=10 colspan="4"></td></tr></tbody></table>  
  </td></tr></table>


<!-- 2���������� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line2%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='link_id' value='<%=link_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>   

</body>
</html>

<script language=javascript>
<!--
//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//���� ��� 
function eleApprovalRequest()
{
	if (document.eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	
	 //���缱 �˻�
	data = document.eForm.doc_app_line.value;		//���缱 ����
	s = 0;								//substring������
	e = data.length;					//���ڿ� ����
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("����") != -1) decision++;
		if(rstr.indexOf("����") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("�����ڰ� �������ϴ�"); return; }
	
	//�����ϱ�
	var doc_sub = "������û�� : "+'<%=purpose%>';

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/ChaRyangServlet';
	document.eForm.mode.value='BAE_CHA_SEC';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//�ݱ�
function winClose()
{
	window.returnValue='';
	self.close();
}
-->
</script>