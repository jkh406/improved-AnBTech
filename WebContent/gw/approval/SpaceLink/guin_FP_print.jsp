<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�����Ƿڼ� ����"		
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

	//����ٹ���û�� �������
	String query		= "";
	String div_name		= "";		//�μ���
	String user_name	= "&nbsp;";		//����� ��
	String user_rank	= "&nbsp;";		//����� ����
	String doc_date		= "";		//�ۼ� �����
	
	String doc_no		= "&nbsp;";		//������ȣ
	String job_kind		= "&nbsp;";		//��������
	String job_content	= "&nbsp;";		//��������
	String career		= "&nbsp;";		//�з�
	String major		= "&nbsp;";		//����
	String req_qualify	= "&nbsp;";		//�ʿ��ڰ���
	String status		= "&nbsp;";		//�Ի�����
	String job_career	= "&nbsp;";		//�䱸���
	String job_etc		= "&nbsp;";		//�䱸��� ��Ÿ
	String req_count	= "&nbsp;";		//�����ο�
	String marray		= "&nbsp;";		//ȥ��
	String army			= "&nbsp;";		//����
	String employ		= "&nbsp;";		//�������
	String employ_per		= "&nbsp;";	//������� ����� �Ⱓ
	String language_grade	= "&nbsp;";	//�ܱ���
	String language_exam	= "&nbsp;";	//���ν���
	String language_score	= "&nbsp;";	//���/����
	String comp_grade		= "&nbsp;";	//����ɷ�
	String comp_etc			= "&nbsp;";	//����ɷ� ��Ÿ
	String papers			= "&nbsp;";	//���⼭��
	String note				= "&nbsp;";	//��Ÿ �ʿ����

	//���缱 ����
	String pid			= "";		//������ȣ
	String doc_id		= "";		//���ù��� ������ȣ
	String line			="";		//�������� ���缱
	String r_line		= "";		//���ۼ����� �Ѱ��ֱ�
	String vdate		= "";		//������ ���� ����
	String ddate		= "";		//������ ���� ����
	String wid			= "";		//����ڻ��
	String vid			= "";		//�����ڻ��
	String did			= "";		//�����ڻ��
	String wname		= "";		//�����
	String vname		= "";		//������
	String dname		= "";		//������
	String PROCESS		= "";		//PROCESS
	String doc_ste		= "";		//doc_ste

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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_OFF")) {
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
	connMgr.freeConnection("mssql",con);

	/*********************************************************************
	// �����Ƿڼ� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date","doc_id",
						"job_kind","job_content","career","major","req_qualify","status","job_career",
						"job_etc","req_count","marray","army","employ","employ_per","language_grade",
						"language_exam","language_score","comp_grade","comp_etc","papers","note"};
	bean.setTable("insa_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (is_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		doc_date = bean.getData("in_date");			//�ۼ������

		doc_no = bean.getData("doc_id");			//������ȣ
		if(doc_no.length() == 0) doc_no = "����Ϸ��� �ڵ�ä��";
		job_kind = bean.getData("job_kind");		//��������
		job_content = bean.getData("job_content");	//��������
		career = bean.getData("career");			//�з�
		major = bean.getData("major");				//����
		req_qualify = bean.getData("req_qualify")==""?"&nbsp;":bean.getData("req_qualify");	//�ʿ��ڰ���
		status = bean.getData("status");			//�Ի�����
		job_career = bean.getData("job_career");	//�䱸���
		job_etc = bean.getData("job_etc");			//�䱸��� ��Ÿ
		req_count = bean.getData("req_count");		//�����ο�
		marray = bean.getData("marray");			//ȥ��
		army = bean.getData("army");				//����
		employ = bean.getData("employ");			//�������
		employ_per = bean.getData("employ_per");	//������� ����� �Ⱓ
		language_grade = bean.getData("language_grade");	//�ܱ���
		language_exam = bean.getData("language_exam");		//���ν���
		language_score = bean.getData("language_score");	//���/����
		comp_grade = bean.getData("comp_grade");			//����ɷ�
		comp_etc = bean.getData("comp_etc");		//����ɷ� ��Ÿ
		papers = bean.getData("papers");			//���⼭��
		note = bean.getData("note")==""?"&nbsp;":bean.getData("note");				//��Ÿ �ʿ����
	} //while

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);			//	  ��
		wdate = doc_date.substring(8,10);			//	  ��
	}
	//ȭ�����
	status = StringProcess.repWord(status,":","");	//�Ի�����
	marray = StringProcess.repWord(marray,":","");	//ȥ��
	army = StringProcess.repWord(army,":","");		//����
	language_grade = StringProcess.repWord(language_grade,":","");	//�ܱ���

	//comp_grade = StringProcess.repWord(comp_grade,":",", ");		//����ɷ�
	StringTokenizer comp_list = new StringTokenizer(comp_grade,":");		//����ɷ�
	comp_grade = "";
	int c = 0;
	while(comp_list.hasMoreTokens()) {
		comp_grade += comp_list.nextToken()+", ";
		c++;
	}
	int comp_len = comp_grade.length();
	if(comp_len > 2)	comp_grade = comp_grade.substring(0,comp_len-2);

	//papers = StringProcess.repWord(papers,":",", ");	//���⼭��
	StringTokenizer papers_list = new StringTokenizer(papers,":");		//���⼭��
	papers = "";
	int p = 0;
	while(papers_list.hasMoreTokens()) {
		papers += papers_list.nextToken()+", ";
		p++;
	}
	int ps_len = papers.length();
	if(ps_len > 2)	papers = papers.substring(0,ps_len-2);

	String employ_data = "";
	StringTokenizer emp_list = new StringTokenizer(employ,":");		//�������
	int e = 0;
	while(emp_list.hasMoreTokens()) {
		String emp = emp_list.nextToken();
		if(emp.equals("�����")) employ_data += emp+"(�Ⱓ "+employ_per+"����)" + ", ";
		else employ_data += emp+", ";
		e++;
	}
	int emp_len = employ_data.length();
	if(emp_len > 2)	employ_data = employ_data.substring(0,emp_len-2);

%>

<html>
<head>
<title> �����Ƿڼ�</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2"> �����Ƿڼ�</TD>
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
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>
<TABLE><TR><TD height='5'></TD></TR></TABLE>

<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">������ȣ</td>
				<td width="35%" class="bg_06"><%=doc_no%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">�ۼ�����</td>
				<td width="35%" class="bg_06"><%=doc_date%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=job_kind%></pre>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="85%" class="bg_06" colspan="3"><%=job_content%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�з�</td>
				<td width="35%" class="bg_06"><%=career%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">����</td>
				<td width="35%" class="bg_06"><%=major%>&nbsp;</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�ʿ����ڰ���</td>
				<td width="85%" class="bg_06" colspan="3"><%=req_qualify%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�Ի�����</td>
				<td width="35%" class="bg_06"><%=status%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">�����ο�</td>
				<td width="35%" class="bg_06"><%=req_count%> ��</td></tr>								
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�䱸���</td>
				<td width="85%" class="bg_06" colspan="3"><%=job_career%>���̻� (��Ÿ:<%=job_etc%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�������</td>
				<td width="85%" class="bg_06" colspan="3"><%=employ_data%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">ȥ�ο���</td>
				<td width="35%" class="bg_06"><%=marray%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">����</td>
				<td width="35%" class="bg_06"><%=army%>&nbsp;</td></tr>		
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�ܱ���</td>
				<td width="85%" class="bg_06" colspan="3">���� (����:<%=language_grade%>) (���ν���:<%=language_exam%>, ���/����:<%=language_score%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">����ɷ�</td>
				<td width="85%" class="bg_06" colspan="3"><%=comp_grade%> ��Ÿ(<%=comp_etc%>)</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���⼭��</td>
				<td width="85%" class="bg_06" colspan="3"><%=papers%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��Ÿ�䱸����</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=note%></pre>&nbsp;</td></tr></table>
	</td></tr></table>

<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
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
	var line = '<%=r_line%>';
	var ln = "";
	for(i=0; i<line.length; i++) {
		if(line.charAt(i) == '@') ln += '\n';
		else ln += line.charAt(i);
	}
	
	document.eForm.action = "guin_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
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
