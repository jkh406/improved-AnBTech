<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "�����û�� ���ۼ�"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//������� (��,��)
	normalFormat money = new com.anbtech.util.normalFormat("#,###");		//������� (���)
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	text.setFilepath(filepath);		//directory�����ϱ�

	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	String query = "";
	String writer_id = "";			//�����(�븮����ϼ��� ����) ���
	String writer_name = "";		//�����(�븮����ϼ��� ����) �̸�

	String user_name = "";			//�ش��� ��
	String rank_code = "";			//�ش��� ����code
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�

	int period_n = 0;				//from ~ to �Ⱓ : ��
	int period = 0;					//from ~ to �Ⱓ : ��

	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while


	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����)
	*********************************************************************/	
	String user_id = multi.getParameter("user_id"); if(user_id == null) user_id = login_id;
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//�ش��� ��
		rank_code = bean.getData("ar_code");			//�ش��� ���� code
		user_rank = bean.getData("ar_name");			//�ش��� ����
		div_id = bean.getData("ac_id");					//�ش��� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//�ش��� �μ��� 
		div_code = bean.getData("ac_code");				//�ش��� �μ��ڵ�
	} //while

	/*********************************************************************
	 	����� ������ �˾ƺ���
	*********************************************************************/	
	String receiver_id = multi.getParameter("receiver_id");	if(receiver_id == null) receiver_id = "";	
	if(receiver_id.length() == 0) receiver_id = writer_id;
	String[] recColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(recColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+receiver_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	String receiver_name = "";
	while(bean.isAll()) {
		receiver_name = bean.getData("name");			//������ ��
	} //while
	
	String doc_chuljang = "BT_001";
	/*********************************************************************
	 	����� �׸� ����
	*********************************************************************/	
	String[] csColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("code DESC");	
	query = "WHERE type = 'BT_COST'";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][4];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("code");				//��������ڵ�
		btrip[i][1] = bean.getData("code_name");		//���������
		i++;
	} //while

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//���缱
	String prj_code = multi.getParameter("prj_code"); 
		if(prj_code == null || prj_code.length() == 0) prj_code = ":";  			//������Ʈ �ڵ�
	String project_code = prj_code.substring(0,prj_code.indexOf(":"));
	String prj_name = prj_code.substring(prj_code.indexOf(":")+1,prj_code.length());

	String fellow_names = multi.getParameter("fellow_names"); 
		if(fellow_names == null) fellow_names = "";									//������ ���/�̸�;
	String bistrip_kind = multi.getParameter("bistrip_kind"); 
		if(bistrip_kind == null) bistrip_kind = "����";								//������ ����(����/��)
	String bistrip_country = multi.getParameter("bistrip_country"); 
		if(bistrip_country == null) bistrip_country = "";							//������ ������
	String bistrip_city = multi.getParameter("bistrip_city"); 
		if(bistrip_city == null) bistrip_city = "";									//������ ���ø�
	String traffic_way = multi.getParameter("traffic_way"); 
		if(traffic_way == null) traffic_way = "";									//������
	String purpose = multi.getParameter("purpose"); 
		if(purpose == null) purpose = "";											//������ ����
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//�μ��ΰ���
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//��޿���ó
	String bank_no = multi.getParameter("bank_no"); if(bank_no == null) bank_no = "";//���¹�ȣ
	
	
	//���ݾװ� ���⳻���� �迭�� ���
	int costcnt = btrip.length;
	String[][] cost = new String[costcnt][3];
	String c_code = "";		//�����ڵ�
	String c_cost = "";		//������
	String c_cont = "";		//���⳻��
	int sum = 0;			//��� �հ�
	for(int c=0,m=1; c < costcnt; c++,m++) {
		c_code = "code"+m;
		c_cost = "cost"+m;
		c_cont = "cont"+m;
		cost[c][0] = multi.getParameter(c_code);	if(cost[c][0] == null) cost[c][0] = "";
		cost[c][1] = multi.getParameter(c_cost);	if(cost[c][1] == null) cost[c][1] = "0";
		cost[c][2] = multi.getParameter(c_cont);	if(cost[c][2] == null) cost[c][2] = "";
		
		//����հ� ����ϱ�
		cost[c][1] = str.repWord(cost[c][1],",","");
		try { sum += Integer.parseInt(cost[c][1]); } catch (Exception e) {cost[c][1] = "0";} 
	}
	//out.println("sum : " + fmt.toDigits(sum));

	//----------------------------
	// from chuljang_sincheoung_FP_view.jsp
	//----------------------------
	String old_id = "";
	old_id = multi.getParameter("old_id"); if(old_id == null) old_id = "";		//��ü������
	if(old_id.length() == 0) {
		old_id = multi.getParameter("doc_id"); if(old_id == null) old_id = "";	//from ..FP_view.jsp
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return true" onLoad='selectBisKind()'>
<form action="chuljanb_sincheong_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �����û�� �ۼ�</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a> <!-- ��� -->
				<% if(user_id.equals(login_id)) { //�븮��Ͻô� �ӽ�����޴� ���� %>
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a> <!-- �ӽ����� -->
				<% } %><a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
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

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ҼӺμ�</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"> <input size="10" type="text" name="user_name" value='<%=user_name%>' class="text_01" readonly> <a href="Javascript:searchUser();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//���� �⵵
				String year = anbdt.getYear();			
				int syear = Integer.parseInt(year);
				int ey = syear + 5;

				String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
							
				out.print("<SELECT NAME='doc_syear' onChange='javascript:selDate();'>");
				for(int iy = syear; iy < ey; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_syear)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.print("</SELECT>�� "); 

				//���� ��
				String month = anbdt.getMonth();
				String sel_smonth = multi.getParameter("doc_smonth");	
				if(sel_smonth == null) sel_smonth = Integer.toString(Integer.parseInt(month));

				out.print("<SELECT NAME='doc_smonth' onChange='javascript:selDate();'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>�� "); 

				//���� ��
				//getDateMaximum(int syear,int smonth,int sdate)
				String dates = anbdt.getDates();
				String sel_sdate = multi.getParameter("doc_sdate");	
				if(sel_sdate == null) sel_sdate = Integer.toString(Integer.parseInt(dates));
				int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);

				out.print("<SELECT NAME='doc_sdate' onChange='javascript:selDate();'>");
				for(int iy = 1; iy <= maxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>��");
			%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//�� �⵵
				String edyear = anbdt.getYear();			
				int eyear = Integer.parseInt(edyear);
				int edy = eyear + 5;
				String sel_eyear = multi.getParameter("doc_edyear"); if(sel_eyear == null) sel_eyear = edyear;
				
				out.print("<SELECT NAME='doc_edyear' onChange='javascript:selDate();'>");
				for(int iy = syear; iy < edy; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_eyear)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.print("</SELECT>�� "); 

				//�� ��
				String edmonth = anbdt.getMonth();
				String sel_emonth = multi.getParameter("doc_edmonth");	
				if(sel_emonth == null) sel_emonth = Integer.toString(Integer.parseInt(edmonth));

				out.print("<SELECT NAME='doc_edmonth' onChange='javascript:selDate();'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_emonth)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>�� "); 

				//�� ��
				//getDateMaximum(int syear,int smonth,int sdate)
				String eddates = anbdt.getDates();
				String sel_edate = multi.getParameter("doc_eddate");	
				if(sel_edate == null) sel_edate = Integer.toString(Integer.parseInt(eddates));
				int emaxdates = anbdt.getDateMaximum(Integer.parseInt(sel_eyear),Integer.parseInt(sel_emonth),1);

				out.print("<SELECT NAME='doc_eddate' onChange='javascript:selDate();'>");
				for(int iy = 1; iy <= emaxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_edate)) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.print("</SELECT>��");

			   //�Ⱓ ���ϱ�
			   period_n = anbdt.getPeriodDate(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),Integer.parseInt(sel_sdate),Integer.parseInt(sel_eyear),Integer.parseInt(sel_emonth),Integer.parseInt(sel_edate));
			   period = period_n + 1;
			%>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����ϼ�</td>
           <td width="37%" height="25" class="bg_04">
				<input type='text' name='gab_days' value='<%=period_n%>�� <%=period%>�ϰ�' class="text_01" readonly></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
			<select name='bistrip_kind' onChange="javascript:selectBisKind();">
				<% 
					String seln = "";	String sela = "";
					if(bistrip_kind.equals("����")) seln = " selected ";
					else if(bistrip_kind.equals("����")) sela = " selected ";
					out.print("<OPTION value='����'"+seln+">����</OPTION>");
					out.print("<OPTION value='����'"+sela+">����</OPTION>");
					out.print("</select>");
				%>
				���ø�<input size=10 type='text' name='bistrip_city' class='text_01' value='<%=bistrip_city%>'><span id='ad'></span>			   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><input size=30 type="text" name="purpose" value='<%=purpose%>' maxlength="50" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
					<select name='traffic_way'>
				<% 
					String a = ""; if(traffic_way.equals("����")) a = " selected ";
					String b = ""; if(traffic_way.equals("����")) b = " selected ";
					String c = ""; if(traffic_way.equals("�װ���")) c = " selected ";
					String d = ""; if(traffic_way.equals("�ڰ�����")) d = " selected ";
					String e = ""; if(traffic_way.equals("ȸ����")) e = " selected ";
					String f = ""; if(traffic_way.equals("����")) f = " selected ";
					String g = ""; if(traffic_way.equals("��Ÿ")) g = " selected ";
					out.print("<OPTION value='����'"+a+">����</OPTION>");
					out.print("<OPTION value='����'"+b+">����</OPTION>");
					out.print("<OPTION value='�װ���'"+c+">�װ���</OPTION>");
					out.print("<OPTION value='�ڰ�����'"+d+">�ڰ�����</OPTION>");
					out.print("<OPTION value='ȸ����'"+e+">ȸ����</OPTION>");
					out.print("<OPTION value='����'"+f+">����</OPTION>");
					out.print("<OPTION value='��Ÿ'"+g+">��Ÿ</OPTION>");
				%>
					</select>		   		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������Ʈ�ڵ�</td>
           <td width="37%" height="25" class="bg_04"><input size="15" type="text" name="project_code" value='<%=project_code%>' readonly> <input size="10" type="text" name="prj_name" value='<%=prj_name%>' readonly> <a href="javascript:sel_pjt_code();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><TEXTAREA NAME="fellow_names" rows=1 cols=16 readOnly><%=fellow_names%></TEXTAREA> <a href="Javascript:searchProxy();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����μ���</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_receiver" value='<%=rec%>' size="15" readOnly> <a href="Javascript:jobReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��޿���ó</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_tel" value='<%=tel%>' size="15" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%> �� <%=anbdt.getMonth()%> �� <%=anbdt.getDates()%> ��</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
<%	/************************************************************
	 ����� �κ��� ���ܽ�Ŵ. ����� �κ��� �߰���Ű���� ���������� ������ ��.
	 - �������׸��� yangsic_env ���� system_minor_code ���� �������� ����
	 - �������׸��� ������ ����� ��Ÿ���� �ʴ� ���� �ذ�
	 - �ݾ׶��� �ʱⰪ�� 000�� �ƴ� 0�� ��Ÿ����
	 - �ڵ������ JSP�� �ƴ� ��ũ��Ʈ�� ó���Ͽ� ȭ���� �������� �ʰ�
	 ************************************************************/
%>

	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�� �� ��<br>û ��</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=90% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='15%' align='center'>�׸�</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>û���ݾ�(��)</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>���ޱݾ�(��)</td>"); 
				out.print("<td class=bg_05 width='45%' align='center'>û���ݾ׻��⳻��</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='cost"+n+"' value='"+money.StringToString(cost[p][1])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//û�����(�ݾ�)
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_cost"+n+"' value='0' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);' readonly></td>");//���޺��(�ݾ�)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value='"+cost[p][2]+"'></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>�հ�</b></td><td class=bg_07><input class='money' size=15 type='text' name='sum' value='"+ money.toDigits(sum) +"' readonly></td>");
				out.print("</td><td class=bg_07><input class='money' size=15 type='text' name='ep_sum' value='0' readonly></td></tr>");
				out.print("</table>");
			%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><input size=10 type="text" name="receiver_name" value='<%=receiver_name%>'> <a href="Javascript:searchReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
		   <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���¹�ȣ</td>
           <td width="37%" height="25" class="bg_04"><input size=30 type="text" name="bank_no" value='<%=bank_no%>'></td>
		 </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table> 

</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'></td>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='[�����û��]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='account_cnt' value='<%=i%>'>
<input type='hidden' name='doc_chuljang' value='<%=doc_chuljang%>'>
<input type='hidden' name='period' value='<%=period%>'>
<input type='hidden' name='prj_code' value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
//����� ã��
function searchUser()
{
	wopen("searchUser.jsp?target=eForm.user_id/eForm.user_name","user","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//������Ʈ ã��
function sel_pjt_code()
{
	wopen('../../../servlet/PsmProcessServlet?mode=search_project&target=eForm.project_code/eForm.prj_name','search_pjt','400','230','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//������ ã��
function searchProxy()
{
	var fellows = document.eForm.fellow_names.value;
	var url = "searchFellows.jsp?target=eForm.fellow_names&fellows="+fellows;
	wopen(url,"proxy","510","467","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//�����μ��ΰ���
function jobReceiver()
{
	wopen("searchName.jsp?target=eForm.doc_receiver","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//������ ã��
function searchReceiver()
{
	wopen("searchUser.jsp?target=eForm.receiver_id/eForm.receiver_name","proxy","250","380","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//������ ����
function selectBisKind()
{
	var kind = document.eForm.bistrip_kind.value;
	if(kind == "����") ad.innerHTML="";
	else ad.innerHTML=" ������<input size=10 type='text' name='bistrip_country' class='text_01' value='<%=bistrip_country%>'>";
}

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//�Ⱓ ���ϱ� (���ڼ���)
function selDate() {
	var sy = document.eForm.doc_syear.value;
	var sm = document.eForm.doc_smonth.value;
	var sd = document.eForm.doc_sdate.value;
	var start_day = new Date(sy,sm-1,sd);
	start_day = start_day.getTime();

	var ey = document.eForm.doc_edyear.value;
	var em = document.eForm.doc_edmonth.value;
	var ed = document.eForm.doc_eddate.value;
	var end_day = new Date(ey,em-1,ed);
	end_day = end_day.getTime();

	var night_gab = Math.floor((end_day-start_day)/(60*60*24*1000));
	var day_gab = night_gab+1;

	document.eForm.gab_days.value=night_gab+"�� "+day_gab+"�ϰ�";

}
//�ݾ� õ������ �޸��ֱ�
function InputMoney(input){
	str = input.value;	
	str = unComma(str);
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//�Ѿ�ǥ���ϱ�
function sumMoney(){
	var n = '<%=cnt%>';
	var sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		sum += unCommaObj(eval("document.eForm.cost"+j+".value"));
	}
	document.eForm.sum.value=Comma(sum);
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj�ι޾� �޸� ���ֱ�
function unCommaObj(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}
//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//���� ���� 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	else if(eForm.bistrip_city.value == "") { alert("������ ���ø��� �Է��Ͻʽÿ�."); return; }
	else if(eForm.purpose.value == "") { alert("��������� �Է��Ͻʽÿ�."); return; }

	if(eForm.bistrip_kind.value =="����") {
		if (eForm.bistrip_country.value =="") { alert("������ �������� �Է��Ͻʽÿ�."); return; }
	} else if(eForm.bistrip_kind.value =="����"){
		if (eForm.doc_tel.value =="") { alert("��޿���ó�� �Է��Ͻʽÿ�."); return; }
	}
	
	 //���缱 �˻�
	data = eForm.doc_app_line.value;		//���缱 ����
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
	var purpose = document.eForm.purpose.value;
	var doc_sub = "�����û�� : "+purpose;

	//pjt_code���ϱ�
	prj_code = document.eForm.project_code.value+":"+document.eForm.prj_name.value;

	document.onmousedown=dbclick;
	//�ϰ����� ����������
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='R_CHULJANG_SINCHEONG';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.prj_code.value=prj_code;
	document.eForm.submit();
}

//���� �� �ӽú���
function eleApprovalTemp()
{
	//�����ϱ�
	var purpose = document.eForm.purpose.value;
	var doc_sub = "�����û�� : "+purpose;

	//pjt_code���ϱ�
	prj_code = document.eForm.project_code.value+":"+document.eForm.prj_name.value;

	document.onmousedown=dbclick;
	document.eForm.action="../../../servlet/GeunTaeServlet";
	document.eForm.mode.value='R_CHULJANG_SINCHEONG_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.prj_code.value=prj_code;
	document.eForm.submit();
	
}

function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
