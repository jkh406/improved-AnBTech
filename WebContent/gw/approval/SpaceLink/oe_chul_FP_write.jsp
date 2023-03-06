<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "����� �ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.FileWriteString"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

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

	int attache_cnt = 4;			//÷������ �ִ밹�� (�̸�)
	
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
	 	��������ڵ� ��Ͽ��� Ȯ���ϱ�(yangsic_env)
	*********************************************************************/	
/*
	String[] ysColumn = {"ys_name","ys_value"};
	bean.setTable("yangsic_env");			
	bean.setColumns(ysColumn);
	bean.setOrder("ys_name ASC");	
	query = "where ys_name like 'OT_%'";
	bean.setSearchWrite(query);
	bean.init_write();

	String doc_oechul = "";
	while(bean.isAll()) {
		doc_oechul = bean.getData("ys_name");			//��������ڵ�
	} //while
*/
	String doc_oechul = "OT_001";

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//���缱
	String dest = multi.getParameter("dest"); if(dest == null) dest = "";			//������
	String purpose = multi.getParameter("purpose"); if(purpose == null) purpose = "";//����
	String fellow_names = multi.getParameter("fellow_names"); 
		if(fellow_names == null) fellow_names = "";									//������ ���/�̸�;
	String traffic_way = multi.getParameter("traffic_way"); 
		if(traffic_way == null) traffic_way = "";									//������
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//�μ��ΰ���
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//��޿���ó

	//���� �⵵
	String year = anbdt.getYear();			
	int syear = Integer.parseInt(year);
	int ey = syear + 5;
	String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
	//���� ��
	String month = anbdt.getMonth();
	String sel_smonth = multi.getParameter("doc_smonth");	
	if(sel_smonth == null) sel_smonth = month;
	//���� ��
	String dates = anbdt.getDates();
	String sel_sdate = multi.getParameter("doc_sdate");	
	if(sel_sdate == null) sel_sdate = dates;
	int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);
	//����� ���ϱ�
	String hrs = anbdt.getHours();			//HH	
	
	String StartTime = multi.getParameter("hdStartTime"); 
			if(StartTime == null) StartTime = hrs+":00";							//���۽ð�
	String EndTime = multi.getParameter("hdEndTime");
			if(EndTime == null) EndTime = hrs+":30";								//����ð�
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form action="oe_chul_FP_write.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> ����� �ۼ�</TD></TR></TBODY>
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
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ҼӺμ�</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><input size="10" type="text" name="user_name" value='<%=user_name%>' class="text_01" readonly> <a href="Javascript:searchUser();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��������</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//���� �⵵						
				out.println("&nbsp;<SELECT NAME='doc_syear'>");
				for(int iy = syear; iy < ey; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_syear)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.println("</SELECT>��"); 

				//���� ��
				out.println("<SELECT NAME='doc_smonth'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>��"); 

				//���� ��
				out.println("<SELECT NAME='doc_sdate'>");
				for(int iy = 1; iy <= maxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>��");
		   %>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����ð�</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="hdStartTime" CLASS="etc">
				<%
				String Shours = StartTime.substring(0,2);	//���۽�
				String Smins = StartTime.substring(3,5);	//���ۺ�
				String Ehours = EndTime.substring(0,2);		//�����
				String Emins = EndTime.substring(3,5);		//�����

				String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=0; asH<24; asH++){
						if(asH == Integer.parseInt(Shours)) msSEL = "SELECTED"; else msSEL="";
						if(Smins.equals("00")) {
							out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "00");
							out.println("<OPTION>" + asHour[asH] + ":" + "30");
						} else {
							out.println("<OPTION>" + asHour[asH] + ":" + "00");
							out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "30");
						}
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEndTime" CLASS="etc">
				<%
				String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String meSEL = "";
					for(int aeH=0; aeH<24; aeH++){
						if(aeH == Integer.parseInt(Ehours)) meSEL = "SELECTED"; else meSEL="";
						if(Emins.equals("00")) {
							out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "00");
							out.println("<OPTION>" + aeHour[aeH] + ":" + "30");
						} else {
							out.println("<OPTION>" + aeHour[aeH] + ":" + "00");
							out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "30");
						}
					}
				%></SELECT>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�༱��</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="dest" value='<%=dest%>' size="20" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04">
					<select name='traffic_way'">
				<% 
					out.println("<OPTION value='����'>����</OPTION>");
					out.println("<OPTION value='����'>����</OPTION>");
					out.println("<OPTION value='�װ���'>�װ���</OPTION>");
					out.println("<OPTION value='�ڰ�����'>�ڰ�����</OPTION>");
					out.println("<OPTION value='ȸ����'>ȸ����</OPTION>");
					out.println("<OPTION value='����'>����</OPTION>");
					out.println("<OPTION value='��Ÿ'>��Ÿ</OPTION>");
				%>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="purpose" value='<%=purpose%>' size="20" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������</td>
           <td width="37%" height="25" class="bg_04"><TEXTAREA NAME="fellow_names" rows=1 cols=16 readOnly><%=fellow_names%></TEXTAREA> <a href="Javascript:searchProxy();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����μ���</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_receiver" value='<%=rec%>' readOnly> <a href="Javascript:jobReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��޿���ó</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_tel" value='<%=tel%>' size="15" maxlength="20" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��û����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%> �� <%=anbdt.getMonth()%> �� <%=anbdt.getDates()%>��</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			for(int a=1; a<attache_cnt; a++) {
				out.println("&nbsp;<input type=file name=attachfile"+a+" size=60><br>");
			}
		%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='doc_oechul' value='<%=doc_oechul%>'>
</form>
</body>
</html>

<script language=javascript>
<!--
//�����ڵ� ��Ͽ��� Ȯ���ϱ�
var code = '<%=doc_oechul%>';
if(code.length == 0) {
	alert("�����ڵ尡 ��ϵǾ����� �ʽ��ϴ�. �����ڿ��� ������ ����Ͻʽÿ�.");
}
//������ ã��
function searchProxy()
{
	fellows = document.eForm.fellow_names.value; 
	wopen("searchFellows.jsp?target=eForm.fellow_names&fellows="+fellows,"proxy","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//����� ã��
function searchUser()
{
	wopen("searchUser.jsp?target=eForm.user_id/eForm.user_name","user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//�����μ���
function jobReceiver()
{
	wopen("searchName.jsp?target=eForm.doc_receiver","proxy","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

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

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//���� ��� 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("���缱�� �Է��Ͻʽÿ�."); return; }
	else if(eForm.dest.value == "") { alert("�༱���� �Է��Ͻʽÿ�."); return;}
	else if(eForm.purpose.value == "") { alert("��������� �Է��Ͻʽÿ�."); return;}
	else if(eForm.doc_tel.value == "") { alert("��޿���ó�� �Է��Ͻʽÿ�."); return;}
	
	 //���缱 �˻�
	data = eForm.doc_app_line.value;	//���缱 ����
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
	var doc_sub = "����� : "+purpose;

	document.onmousedown=dbclick;  // ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='OE_CHUL';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//���� �ӽú���
function eleApprovalTemp()
{
	//�����ϱ�
	var purpose = document.eForm.purpose.value;
	var doc_sub = "����� : "+purpose;

	document.onmousedown=dbclick;  // ����Ŭ�� check

	document.eForm.action="../../../servlet/GeunTaeServlet";
	document.eForm.mode.value='OE_CHUL_TMP';
	document.eForm.app_mode.value='TMP';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
	
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>