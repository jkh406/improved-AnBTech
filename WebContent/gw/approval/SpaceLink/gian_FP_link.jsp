<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "��ȼ� �ۼ� ����"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="java.sql.Connection"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�

	//��ȼ� �������
	String query = "";
	String div_name = "";			//�μ���
	String user_name = "";			//����� ��
	String user_rank = "";			//����� ����
	String doc_date = "";			//�ۼ� �����

	String doc_no = "";				//������ȣ
	String period = "";				//ó������
	String doc_syear = "";			//�������� �⵵
	String doc_smonth = "";			//�������� ��
	String doc_sdate = "";			//�������� ��
	String kind = "";				//����
	String subject = "";			//����
	String bon_path = "";			//���������н�
	String bon_file = "";			//�����������ϸ�

	String fname = "";				//÷�����ϸ�
	String sname = "";				//÷������ �����
	String ftype = "";				//÷������Type
	String fsize = "";				//÷������Size
	
	//���缱 ����
	String pid = "";				//������ȣ
	String doc_id = "";				//���ù��� ������ȣ
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

	//*********************************************************************
	// ���缱 ���� �ޱ�
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//������ȣ
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//������ȣ(��ũ������ pid�͵���)
	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";	//PROCESS��
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";	//doc_ste

	//���ڰ��系�� & ���缱 �б�
	String line_note = "";		//���缱 note ����
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";	//�������缱,����TextArea,�����ǰ�,TextArea
	int line_cnt = 0;									//���缱�� ��µ� ���� ����
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();
	
	//��������(anb) ���� ��������(storehouse)�� ���� ���� : 200408 : ����
	if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
	else masterDAO.getTable_MasterPid(pid);		
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
		
		//�ǰ�
		if(!app.getApStatus().equals("���")) {
			if(app.getApStatus().equals("�뺸")) {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"��Ȯ��: <font color=blue>"+app.getApComment()+" "+app.getApDate()+"</font><br>";
			} else {
				line_note += app.getApStatus()+"["+app.getApName()+"]"+"���ǰ�: <font color=blue>"+app.getApComment()+"</font><br>";
			}
		}

		if(app.getApStatus().equals("���")) {
			wname = app.getApName();	if(wname == null) wname="";		//�����
			wid = app.getApSabun();		if(wid == null) wid="";			//����� ���
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";
			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";
			line_cnt++;
		}
		else if(app.getApStatus().equals("����"))  {
			vname = app.getApName();	if(vname == null) vname="";		//������
			vid = app.getApSabun();		if(vid == null) vid="";			//������ ���
			vdate = app.getApDate();	if(vdate == null) vdate="";		//������ ��������(���������,���������ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";
			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";
			line_cnt++;
		}
		else if(app.getApStatus().equals("����"))  {
			dname = app.getApName();	if(dname == null) dname="";		//������
			did = app.getApSabun();		if(did == null) did="";			//������ ���
			ddate = app.getApDate();	if(ddate == null) ddate="";		//������ �������� (���������,���������ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";
			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";
			line_cnt++;
		}
		else {	//���� : ����Թ����� �����ڸ� ������ �ڷ� ������ ����
			if((PROCESS.equals("APP_BOX") || PROCESS.equals("DEL_BOX")) && app.getApStatus().equals("�뺸")){

			} else if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_DRF")) {
				ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";
				a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";
				line_cnt++;
			} else {
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";
				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				r_line += app.getApStatus()+" "+app.getApSabun()+" "+app.getApName()+" "+app.getApRank()+"@";
				line_cnt++;
			}
		}
	}
	if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }
	
	//���缱 ������� �˾ƺ��� [APV(����)�ܰ��̸� ����ڰ� �����Ұ���] : 200408 : ����
	String app_status = "",wrt_id="",app_line_data="",app_cancel="N";
	com.anbtech.gw.entity.TableAppMaster tabM = new com.anbtech.gw.entity.TableAppMaster();
	ArrayList tabML = new ArrayList();	
	tabML = masterDAO.getTable_MasterPid(pid);	
	Iterator tab_iter = tabML.iterator();
	while(tab_iter.hasNext()) {
		tabM = (TableAppMaster)tab_iter.next();
		app_status = tabM.getAmAppStatus();						//������ �������
		wrt_id = tabM.getAmWriter();							//�ۼ���
		app_line_data = tabM.getAmAppLine(); 
		if(app_line_data.length() >2)
			app_line_data = app_line_data.substring(0,3);		//��������� ù�ܰ�
	}
	//������� ���ɿ��� �Ǵ�
	if(wrt_id.equals(login_id) && app_status.equals(app_line_data)) {
		app_cancel="Y";
	}

	//�ݱ�
	connMgr.freeConnection("mssql",con);				//Ŀ�ؼ� �ݱ�

	/*********************************************************************
	// ��ȼ� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"doc_id","period","enforce_year","enforce_month","enforce_date","kind","subject",
						"bon_path","bon_file","fname","sname","ftype","fsize"};
	bean.setTable("jiweon_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (jw_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		doc_date = bean.getData("in_date");			//�ۼ������

		doc_no = bean.getData("doc_id");			//������ȣ
		if(doc_no.length() == 0) doc_no = "����Ϸ��� �ڵ�ä��";
		period = bean.getData("period");			//ó������
		doc_syear = bean.getData("enforce_year");	//�������� �⵵
		doc_smonth = bean.getData("enforce_month");	//�������� ��
		doc_sdate = bean.getData("enforce_date");	//�������� ��
		kind = bean.getData("kind");				//����
		subject = bean.getData("subject");			//����

		bon_path = bean.getData("bon_path");		//���������н�
		bon_file = bean.getData("bon_file");		//�����������ϸ�

		fname = bean.getData("fname");				//÷�����ϸ�
		sname = bean.getData("sname");				//÷������ �����
		ftype = bean.getData("ftype");				//÷������Type
		fsize = bean.getData("fsize");				//ó������size
		
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
	
%>
<html>
<head><title>��ȼ�</title> 
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">��ȼ�</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=48 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=50 align=middle class="bg_07">�����</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD>
		<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
	<TR vAlign=middle height=50>
		<TD noWrap width=50 align=middle class="bg_06"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
							if(vdate.length() == 0)	{//������
								if(ddate.length() == 0) out.println("&nbsp;");
								else out.println("����");
							} else {
								out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
							}
						%>				
		</TD>
		<TD noWrap width=50 align=middle class="bg_06">
		<%
							if(ddate.length() == 0)	{//������
								out.println("&nbsp;");
							} else {
								out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
							}
						%>				
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD>
	</TR>
	<TR>
		<TD noWrap width=40 align=middle class="bg_05">��<br>��</TD>
		<TD noWrap width=550 align=left colspan="5"><%=line_note%>&nbsp;</TD>
	</TR>
	</TBODY>
</TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>������ȣ</td>
			   <td width="37%" height="25" class="bg_06"><%=doc_no%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>�������</td>
			   <td width="37%" height="25" class="bg_06"><%=wyear%> �� <%=wmonth%> �� <%=wdate%> ��&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>��Ⱥμ�</td>
			   <td width="37%" height="25" class="bg_06"><%=div_name%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>ó������</td>
			   <td width="37%" height="25" class="bg_06"><%=period%>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>����</td>
			   <td width="37%" height="25" class="bg_06"><%=kind%>&nbsp;</td>
			   <td width="13%" height="25" class="bg_05" align='middle'>��������</td>
			   <td width="37%" height="25" class="bg_06"><%=doc_syear%> �� <%=doc_smonth%> �� <%=doc_sdate%> ��&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>����</td>
			   <td width="87%" height="25" colspan="3" class="bg_06"><%=subject%>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>����</td>
			   <td width="87%" height="25" colspan="3" class="bg_06"><pre><%=read_con%></pre>&nbsp;</td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_05" align='middle'>÷������</td>
			   <td width="87%" height="25" colspan="3" class="bg_06">
			<%
			for(int i=0; i<cnt; i++) {
				out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[i][0]+"&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]+"&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a><br>");
			} 
			%>
			   &nbsp;</td></tr></table>
	</td></tr></table>

</body></html>

