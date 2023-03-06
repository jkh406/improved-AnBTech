<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "������ ���ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"

	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	//��ȼ� �������
	String query = "";
	String writer_id = "";			//�����(�븮����ϼ��� ����) ���
	String writer_name = "";		//�����(�븮����ϼ��� ����) �̸�

	String user_name = "";			//�ش��� ��
	String rank_code = "";			//�ش��� ����code
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�

	String fname = "";				//÷�����ϸ�
	String sname = "";				//÷������ �����
	String ftype = "";				//÷������Type
	String fsize = "";				//÷������Size
	int attache_cnt = 4;			//÷������ �ִ밹�� (�̸�)
	String bon_path = "";			//����path
	
	/*********************************************************************
	 	�����(login) �˾ƺ��� : ���ڰ��� ����� ���� [����]
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
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
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
	 	view.jsp�� or ������ ��ü������ ���� ���� ÷������ ����
	*********************************************************************/	
	//÷������ ������ �б� from bogo_FP_view.jsp
	String doc_id = multi.getParameter("doc_id");
	fname = multi.getParameter("fname"); if(fname == null) fname = "";			
	sname = multi.getParameter("sname"); if(sname == null) sname = "";			
	ftype = multi.getParameter("ftype"); if(ftype == null) ftype = "";			
	fsize = multi.getParameter("fsize"); if(fsize == null) fsize = "";			
	bon_path = multi.getParameter("bon_path"); if(bon_path == null) bon_path = "";
	//÷������ ������ �б�
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();	
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//�������ϸ�
			addFile[m][4] = addFile[m][4].trim();					//TEMP �������ϸ�
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//���缱
	String content = multi.getParameter("content"); if(content == null) content = "";	//����
	String note = multi.getParameter("note"); if(note == null) note = "";				//Note
	
	//-----------------------------
	//��ü ÷������ ������ ����
	//-----------------------------
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//���ʷ� �Ѱܹ�������
	String req = multi.getParameter("req"); if(req == null) req = "";
	String ext = multi.getParameter("ext"); if(ext == null) ext = "";		//������ ÷������ ��ȣ

	if(req.equals("ADD_DEL")) {
		fname = ftype = fsize = sname = "";
		int del_ext = Integer.parseInt(ext) - 1;
		String update = "update jiweon_master set ";
		
		for(int i=0; i<cnt; i++) {
			if(i == del_ext) {	//������ ������ 
				fname += " |";
				ftype += " |";
				fsize += " |";
				sname += " |";
			}
			else {
				fname += addFile[i][0] + " |";
				ftype += addFile[i][1] + " |";
				fsize += addFile[i][2] + " |";
				sname += addFile[i][4] + " |";
			}
		}
		//Tabel update �ϱ�
		update += "fname='"+fname+"',sname='"+sname+"',ftype='"+ftype+"',fsize='"+fsize+"' where jw_id="+old_id;
		bean.execute(update);		//�ش系�� update�ϱ�

		//÷������ �����ϱ�
		String delfilename = filepath + "/" + addFile[del_ext][3];
		text.delFilename(delfilename);	//�ش� ���ϻ��� �ϱ�

		//������ ÷������ �迭��ȣ clear �ϱ�
		addFile[del_ext][0]=addFile[del_ext][1]=addFile[del_ext][2]=addFile[del_ext][3]="";
	}
	
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="sayu_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> ������</TD></TR></TBODY>
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
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ۼ�����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%>�� <%=anbdt.getMonth()%>�� <%=anbdt.getDates()%>��</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ҼӺμ�</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=user_rank%> <%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="content" rows=20 cols=88><%=content%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<% 
			for(int i=0,no=1; i<cnt; i++,no++) {	//�������� �����ֱ�
				if(addFile[i][0].length() == 0) {		//���������� ������ ÷���϶�
					out.println("<input type=file name=attachfile"+no+" size=60><br>");
				} else {
					out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<a href=javascript:attachDel('"+no+"')>[����]<a><br>"); 
				}
			} 
			cnt++;		//attachefile�� ��ȣ�� ������ ����
			if(cnt < attache_cnt) {							//÷������ �߰��ϱ�
				for(int i=cnt; i<attache_cnt; i++) {
					out.println("<input type=file name=attachfile"+i+" size=60><br>");
				}
			}
			%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>
<input type="hidden" name="note" value=''>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_name" value='<%=user_name%>'>
<% // ���� ������ ó�� %>
<input type='hidden' name='req' value=''>
<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>

<% //���긴 ������ ó�� %>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='������'>
<input type='hidden' name='doc_per' value='3'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
</form>  
</body>
</html>


<script language=javascript>
<!--
//÷������ �����ϱ�
function attachDel(a)
{
	document.eForm.action='sayu_FP_Rewrite.jsp';
	document.eForm.req.value='ADD_DEL';	
	document.eForm.ext.value=a;	
	document.eForm.submit();
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

	document.onmousedown=dbclick;  // ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/JiWeonServlet';
	document.eForm.mode.value='R_SAYU';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//���� �ӽú���
function eleApprovalTemp()
{
	document.onmousedown=dbclick;  // ����Ŭ�� check

	document.eForm.action="../../../servlet/JiWeonServlet";
	document.eForm.mode.value='R_SAYU_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.submit();
	
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
