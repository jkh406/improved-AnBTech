<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "������ 2���μ� ������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.file.textFileReader"
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
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	normalFormat money = new com.anbtech.util.normalFormat("#,000");		//������� (���)
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //���ڰ��系�� & ���缱

	//������ �������
	String query = "";
	String div_name = "";			//�μ���
	String user_name = "";			//����� ��
	String user_rank = "";			//����� ����
	String doc_date = "";			//�ۼ� �����

	String doc_no = "";				//������ȣ
	String dest_ac_name = "";		//���źμ�
	String period = "";				//ó������
	String kind = "";				//����
	String subject = "";			//����

	String bon_path = "";			//���������н�
	String bon_file = "";			//�����������ϸ�
	String note_file = "";			//NOTE�������ϸ�

	String fname = "";				//÷�����ϸ�
	String sname = "";				//÷������ �����
	String ftype = "";				//÷������Type
	String fsize = "";				//÷������Size

	String ref_id = "";				//���ñٰ� ���ڰ��� ����id
	String ref_name = "";			//���ñٰ� ���ڰ��� ��������

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
	//���ڰ��系�� & ���缱 �б�
	String line_note = "",line_note2="";		//���缱 note ����

	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine app = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//���缱
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		app = (TableAppLine)line_iter.next();

		//�ǰ�
		if(!app.getApStatus().equals("���")) {
			if(app.getApStatus().equals("�뺸")) {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"��Ȯ��: <font color=blue>"+app.getApComment()+" "+app.getApDate()+"</font><br>";
			} else {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"���ǰ�: <font color=blue>"+app.getApComment()+"</font><br>";
			}
		}
										
		if(app.getApStatus().equals("���")) {
			wname = app.getApName();	//�����
			wid = app.getApSabun();	//����� ���
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("����"))  {
			vname = app.getApName();	//������
			vid = app.getApSabun();	//������ ���
			vdate = app.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("����"))  {
			dname = app.getApName();	//������
			did = app.getApSabun();		//������ ���
			ddate = app.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)\
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
		if(app.getApStatus().equals("�뺸"))  {	
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+"\r";
		}
	}

	/*********************************************************************
	// ������ ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
					"doc_id","dest_ac_name","period","kind","subject",
					"bon_path","bon_file","note_file","fname","sname","ftype","fsize","ref_id","ref_name"};
	bean.setTable("jiweon_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (jw_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		doc_date = bean.getData("in_date");			//�ۼ������

		doc_no = bean.getData("doc_id");			//������ȣ
		if(doc_no.length() == 0) doc_no = "����Ϸ��� �ڵ�ä��";
		dest_ac_name = bean.getData("dest_ac_name");//���źμ�
		period = bean.getData("period");			//ó������
		kind = bean.getData("kind");				//����
		subject = bean.getData("subject");			//����

		bon_path = bean.getData("bon_path");		//���������н�
		bon_file = bean.getData("bon_file");		//�����������ϸ�
		note_file = bean.getData("note_file");		//Note�������ϸ�

		fname = bean.getData("fname");				//÷�����ϸ�
		sname = bean.getData("sname");				//÷������ �����
		ftype = bean.getData("ftype");				//÷������Type
		fsize = bean.getData("fsize");				//ó������size

		ref_id = bean.getData("ref_id");			//���ñٰ� ���ڰ��� ����id
		ref_name = bean.getData("ref_name");		//���ñٰ� ���ڰ��� ��������
	} //while

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);			//	  ��
		wdate = doc_date.substring(8,10);			//	  ��
	}

	//���������б�
	String read_con = "";
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);

	// Note�����б�
	String read_note = "",note = "";
	full_path = upload_path + bon_path + "/bonmun/" + note_file;
	read_note = text.getFileString(full_path);
	int note_len = read_note.length();
	for(int i=0; i<note_len; i++) {
		if(read_note.charAt(i) == '\n') note +="<br>";
		else note += read_note.charAt(i);
	}

	//÷������ ������ �б�
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][4];
	for(int i=0; i<cnt; i++) for(int j=0; j<4; j++) addFile[i][j]="";

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
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//�������ϸ�
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//���缱
%>


<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>������(�����μ���)</title>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> ������ [����μ�]</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../images/bt_sel_line.gif' align='middle' border='0'></a> 
					<a href="Javascript:eleApprovalRequest();"><img src='../../images/bt_sangsin.gif' align='middle' border='0'></a> 
					<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='middle' border='0'></a> 
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=40% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>					
					</TD>
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
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��</TD>
		<TD noWrap width=100% align=left colspan="6"><%=line_note%></TD>
	</TR>
	<TR bgcolor="c7c7c7"><TD height=1 colspan="6"></TD></TR></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ۼ�����</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%> �� <%=wmonth%> �� <%=wdate%> ��</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���źμ�</td>
           <td width="37%" height="25" class="bg_04"><%=dest_ac_name%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�߽źμ�</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%>&nbsp;</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><%=kind%>&nbsp;</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">ó������</td>
           <td width="37%" height="25" class="bg_04"><%=period%>&nbsp;</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ۼ���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=user_name%>&nbsp;</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=subject%></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><pre><%=read_con%></pre></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr> <!-- ���ñٰ� ÷�� : 200408 -->
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���ñٰ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><a href="javascript:viewRef('<%=ref_id%>')"><%=ref_name%></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
		for(int i=0; i<cnt; i++) {
			out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
		} 
		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td width="13%" height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<form action="hyeopjo_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data">
<!-- 2�� ���� ���� -->
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
<input type='hidden' name='doc_sub' value='<%=subject%> �� ���� ������û�� [<%=user_name%>]'>
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

	document.onmousedown=dbclick;// ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/JiWeonServlet';
	document.eForm.mode.value='HYEOPJO_SEC';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();

}
//���ñٰ� ���뺸��
function viewRef(a)
{
	var Url = 'mode=APP_LNK&PID='+a;
	wopen('../../../servlet/ApprovalDetailServlet?'+Url,"Ref_view","650","600","scrollbars=yes,toolbar=no,status=no,resizable=yes");
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
//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>