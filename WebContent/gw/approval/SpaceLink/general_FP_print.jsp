<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ڰ��� ��������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="java.sql.Connection"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�

	//�Ϲݹ������� �������
	String query		= "";
	String doc_sub		= "&nbsp;";			//����
	String doc_per		= "&nbsp;";			//�����Ⱓ
	String doc_sec		= "&nbsp;";			//���ȵ��
	String content		= "&nbsp;";			//��������
	String bon_path		= "&nbsp;";			//�������� path
	String bon_file		= "&nbsp;";			//�������� ���ϸ�

	String doc_or1		= "&nbsp;";			//�������� ÷�� �����̸�1
	String doc_ad1		= "&nbsp;";			//�������� ÷�� �����̸�1
	String doc_or2		= "&nbsp;";			//�������� ÷�� �����̸�2
	String doc_ad2		= "&nbsp;";			//�������� ÷�� �����̸�2
	String doc_or3		= "&nbsp;";			//�������� ÷�� �����̸�3
	String doc_ad3		= "&nbsp;";			//�������� ÷�� �����̸�3
	String file1_size	= "&nbsp;";			//÷������1 ũ��
	String file2_size	= "&nbsp;";			//÷������2 ũ��
	String file3_size	= "&nbsp;";			//÷������3 ũ��
	
	//���缱 ����
	String pid		= "";			//������ȣ
	String doc_id	= "";			//���ù��� ������ȣ
	String line		= "";			//�������� ���缱
	String r_line	= "";			//���ۼ����� �Ѱ��ֱ�
	String vdate	= "";			//������ ���� ����
	String ddate	= "";			//������ ���� ����
	String wid		= "";			//����ڻ��
	String vid		= "";			//�����ڻ��
	String did		= "";			//�����ڻ��
	String wname	= "";			//�����
	String vname	= "";			//������
	String dname	= "";			//������
	String PROCESS	= "";			//PROCESS
	String doc_ste	= "";			//doc_ste

	//*********************************************************************
	// ���缱 ���� �ޱ�
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//������ȣ
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//������ȣ(��ũ������ pid�͵���)
	PROCESS = request.getParameter("PROCESS");	if(PROCESS == null) PROCESS = "";	//PROCESS��
	doc_ste = request.getParameter("doc_ste");	if(doc_ste == null) doc_ste = "";	//doc_ste

	//���ڰ��系�� & ���缱 �б�
	String ag_line="",a_line="",cmt="",t_cmt="",t_line="";		//�������缱,����TextArea,�����ǰ�,TextArea
	int line_cnt = 0;									//���缱�� ��µ� ���� ����
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.gw.db.AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(con); 
	com.anbtech.gw.entity.TableAppLine app = new com.anbtech.gw.entity.TableAppLine();
	masterDAO.getTable_MasterPid(pid);	
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
	
		//���缱
		cmt = app.getApComment(); if(cmt == null) cmt = "";
		if(cmt.length() != 0) { 
			t_cmt = "\r    "+cmt; 
			cmt = "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+cmt; 
			line_cnt++; 
		}

		if(app.getApStatus().equals("���")) {
			wname = app.getApName();	if(wname == null) wname="";		//�����
			wid = app.getApSabun();		if(wid == null) wid="";			//����� ���
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			line_cnt++;
		}
		else if(app.getApStatus().equals("����"))  {
			vname = app.getApName();	if(vname == null) vname="";		//������
			vid = app.getApSabun();		if(vid == null) vid="";			//������ ���
			vdate = app.getApDate();	if(vdate == null) vdate="";		//������ ��������(���������,���������ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			line_cnt++;
		}
		else if(app.getApStatus().equals("����"))  {
			dname = app.getApName();	if(dname == null) dname="";		//������
			did = app.getApSabun();		if(did == null) did="";			//������ ���
			ddate = app.getApDate();	if(ddate == null) ddate="";		//������ �������� (���������,���������ʵ�)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

			t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
			line_cnt++;
		}
		else {	//���� : ����Թ����� �����ڸ� ������ �ڷ� ������ ����
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_GEN")) {
				ag_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				a_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				line_cnt++;
			} else {
				line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"<br>";

				t_line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+t_cmt+"\r";
				line_cnt++;
			}
		}
	}
	if(ag_line.length() != 0) { line += ag_line;	t_line += a_line; }
	connMgr.freeConnection("mssql",con);				//Ŀ�ؼ� �ݱ�

	/*********************************************************************
	// �Ϲݹ������� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"app_subj","save_period","security_level","bon_path","bon_file",
			"add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
	bean.setTable("app_master");			
	bean.setColumns(Column);
	bean.setOrder("app_subj ASC");	
	query = "where (pid ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		doc_sub = bean.getData("app_subj");				//����
		doc_per = bean.getData("save_period");			//�����Ⱓ
		doc_sec = bean.getData("security_level");		//���ȵ��
		bon_path = bean.getData("bon_path");			//�������� path
		bon_file = bean.getData("bon_file");			//�������� ���ϸ�

		doc_or1 = bean.getData("add_1_original");		//�������� ÷�� �����̸�1
		doc_ad1 = bean.getData("add_1_file");			//�������� ÷�� �����̸�1
		doc_or2 = bean.getData("add_2_original");		//�������� ÷�� �����̸�2
		doc_ad2 = bean.getData("add_2_file");			//�������� ÷�� �����̸�2
		doc_or3 = bean.getData("add_3_original");		//�������� ÷�� �����̸�3
		doc_ad3 = bean.getData("add_3_file");			//�������� ÷�� �����̸�3		
	} //while

	//�����Ⱓ
	String period = "";
	if(doc_per.equals("0")) period = "ó�������";
	else if(doc_per.equals("1")) period = "1��";
	else if(doc_per.equals("2")) period = "2��";
	else if(doc_per.equals("3")) period = "3��";
	else if(doc_per.equals("5")) period = "5��";
	else if(doc_per.equals("EVER")) period = "����";

	//���ȵ��
	String security = "";
	if(doc_sec.equals("1")) security = "1��";
	else if(doc_sec.equals("2")) security = "2��";
	else if(doc_sec.equals("3")) security = "3��";
	else if(doc_sec.equals("INDOR")) security = "��ܺ�";
	else if(doc_sec.equals("GENER")) security = "�Ϲ�";

	//���������б�
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	content = text.getFileString(full_path);

	//÷������ ���ϻ����� ���ϱ�
	String file_path = "";

	file_path = upload_path + bon_path + "/addfile/" + doc_ad1;
	File fn1 = new File(file_path);
	file1_size = Long.toString(fn1.length());

	file_path = upload_path + bon_path + "/addfile/" + doc_ad2;
	File fn2 = new File(file_path);
	file2_size = Long.toString(fn2.length());

	file_path = upload_path + bon_path + "/addfile/" + doc_ad3;
	File fn3 = new File(file_path);
	file3_size = Long.toString(fn3.length());

%>

<html>
<head>
<title>�Ϲݹ���</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">�Ϲݹ�������</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
<TBODY>
	<TR vAlign=middle height=23>
		<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
		<TD noWrap width=370 align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=50 readOnly style="border:0"><%=t_line%></TEXTAREA></TD>
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
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>				
		</TD></TR>
	<TR vAlign=middle height=23>
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TBODY></TABLE><BR>

<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="85%" class="bg_06" colspan="3"><%=doc_sub%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�����Ⱓ</td>
				<td width="35%" class="bg_06"><%=period%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">���ȵ��</td>
				<td width="35%" class="bg_06"><%=security%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">����</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=content%></pre><img src='' width='0' height='0'></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">÷������</td>
				<td width="85%" class="bg_06" colspan="3">&nbsp;
						<a href='../eleApproval_downloadp.jsp?fname=<%=doc_or1%>&fsize=<%=file1_size%>&umask=<%=doc_ad1%>&extend=<%=bon_path%>'><%=doc_or1%></a><br><a href='../eleApproval_downloadp.jsp?fname=<%=doc_or2%>&fsize=<%=file2_size%>&umask=<%=doc_ad2%>&extend=<%=bon_path%>'><%=doc_or2%></a><br><a href='../eleApproval_downloadp.jsp?fname=<%=doc_or3%>&fsize=<%=file1_size%>&umask=<%=doc_ad3%>&extend=<%=bon_path%>'><%=doc_or3%></a></td></tr>
			</table>
	</td></tr></table>
<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='PID' value='<%=doc_id%>'>
	<input type='hidden' name='mode'>	
</form>
</body></html>



<script language=javascript>
<!--
function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
}

//�����ϱ�
function winDecision()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}
//�ݷ��ϱ�
function winReject()
{
	wopen('../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ',"eleA_app_decision","400","200","scrollbar=no,toolbar=no,status=no,resizable=no");
}

//���ۼ��ϱ�
function winRewrite()
{
	document.eForm.action="../../../servlet/ApprovalDetailServlet?PID=<%=doc_id%>&mode=REW";		
	document.eForm.PID.value='<%=doc_id%>';
	document.eForm.mode.value='REW';
	document.eForm.submit();
	window.returnValue='RL';
}

function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
-->
</script>
