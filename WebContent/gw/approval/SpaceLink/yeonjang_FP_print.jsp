<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "����ٹ���û�� ����"		
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
	String div_name		= "&nbsp;";			//�μ���
	String user_name	= "&nbsp;";			//����� ��
	String user_rank	= "&nbsp;";			//����� ����
	String doc_date		= "";			//�ۼ� �����

	String doc_syear	= "";			//�ٹ����� ��
	String doc_smonth	= "";			//�ٹ����� ��
	String doc_sdate	= "";			//�ٹ����� ��
	String job_kind		= "&nbsp;";			//�з�
	String cost_prs		= "&nbsp;";			//�ĺ�����Ȯ��

	int work_cnt = 22;				//����ٹ���û �ۼ� �÷���

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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_OTW")) {
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
	// ����ٹ���û�� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"j_year","j_month","j_date","job_kind","cost_prs"};
	bean.setTable("janeup_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (ju_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		doc_date = bean.getData("in_date");			//�ۼ������

		doc_syear = bean.getData("j_year");			//�ٹ����� ��
		doc_smonth = bean.getData("j_month");		//�ٹ����� ��
		doc_sdate = bean.getData("j_date");			//�ٹ����� ��
		job_kind = bean.getData("job_kind");		//����
		cost_prs = bean.getData("cost_prs");		//�ĺ�����Ȯ��
	} //while

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);			//	  ��
		wdate = doc_date.substring(8,10);			//	  ��
	}

	//�ٹ��� ã��
	String[][] works = new String[work_cnt][5]; //�ٹ��ڻ��,�̸�,����,��ǽð�,�μ���Ȯ��
	for(int i=0; i<work_cnt; i++) for(int j=0; j<5; j++) works[i][j] = "";
	String[] workColumn = {"worker_id","worker_name","content","close_time","cfm"};
	bean.setTable("janeup_worker");			
	bean.setColumns(workColumn);
	bean.setOrder("ju_cid ASC");	
	query = "where (ju_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	
	int w = 0;
	while(bean.isAll()) {
		works[w][0] = bean.getData("worker_id");			//�ٹ��ڻ��
		works[w][1] = bean.getData("worker_name");		//�̸�
		works[w][2] = bean.getData("content");			//����
		works[w][3] = bean.getData("close_time");		//��ǽð�
		works[w][4] = bean.getData("cfm");				//�μ���Ȯ��
		w++;
	} //while

%>
<script language=javascript>
<!--
//�����ϱ�
function winDecision()
{
	window.open("../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS","eleA_app_decision","width=200,height=100,scrollbar=yes,toolbar=no,status=yes,resizable=no");
	window.returnValue='RL';
}
//�ݷ��ϱ�
function winReject()
{
	window.open("../../../servlet/ApprovalProcessServlet?PID=<%=pid%>&mode=PRS_REJ","eleA_app_decision","width=200,height=100,scrollbar=yes,toolbar=no,status=yes,resizable=no");
	window.returnValue='RL';
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
	
	document.eForm.action = "yeonjang_FP_Rewrite.jsp";
	document.eForm.doc_app_line.value=ln;
	document.eForm.submit();
	window.returnValue='RL';
}
//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//���ڰ����� �ݱ�
function winClose()
{
	window.returnValue='RL';
	self.close();
}
-->
</script>


<html>
<head>
<title>����ٹ���û��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">����ٹ���û��</TD>
	<TD width='30%' align="right" valign="bottom">
	<div id="print" style="position:relative;visibility:visible;">
			<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='absmiddle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../../images/bt_close.gif' align='absmiddle' border='0'></a> <!-- �ݱ� -->
	</div></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- �������� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="700" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
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
		<TD noWrap width=50 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="620" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�� �� �� ��</td>
				<td width="85%" class="bg_06" colspan="3"><%=div_name%></td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">���� �ٹ���</td>
				<td width="85%" class="bg_06" colspan="3"><%=doc_syear%> �� <%=doc_smonth%> �� <%=doc_sdate%> ��  </td>
			<tr>
				<td width="100" height="25" align="middle" class="bg_05"><b>�� �� ��</b></td>
				<td width="340" height="25" align="middle" class="bg_05"><b>��������</b></td>
				<td width="100" height="25" align="middle" class="bg_05"><b>��ǽð�</b></td>
				<td width="100" height="25" align="middle" class="bg_05"><b>�μ���Ȯ��</b></td></tr>
			<%
	for(int i=0; i<work_cnt; i++) {
		out.println("<tr>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][1]+"</td>");
		out.println("<td width='340' height='24' align='center' valign='middle'>"+works[i][2]+"</td>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][3]+"</td>");
		out.println("<td width='100' height='24' align='center' valign='middle'>"+works[i][4]+"</td>");
		out.println("</tr>");
	}
	%>
<!--
	<tr>
		<td width="100" height="470" align="center" valign="top">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%	//�ٹ���
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='21' align='center' valign='bottom'>");
					out.println(works[i][1]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=black></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="340" height="470" align="center" valign="top">
			<table width='340' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='340' height='21' align='center' valign='bottom'>");
					out.println(works[i][2]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="top">
			<table width='100' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='100' height='21' align='center' valign='bottom'>");
					out.println(works[i][3]);
					out.println("</td></tr>");
					out.println("<td width='100' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
		<td width="100" height="470" align="center" valign="top">
			<table width='95' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
				<%
				for(int i=0; i<work_cnt; i++) {
					out.println("<tr>");
					out.println("<td width='95' height='21' align='center' valign='bottom'>");
					out.println(works[i][4]);
					out.println("</td></tr>");
					out.println("<td width='95' height='1' bgcolor=gray></td></tr>");
				}
				%>
			</table>
		</td>
	</tr>
-->
			<tr>
				<td width="100" height="25" align="middle" class="bg_05"><b>�з�</b></td>
				<td width="340" height="25" align="middle" class="bg_05"><b><%=job_kind%></b></td>
				<td width="100" height="25" align="middle" class="bg_05"><b>�ĺ�����Ȯ��</b></td>
				<td width="100" height="25" align="middle" class="bg_05"><b><%=cost_prs%></b></td></tr>
			<tr>
				<td width="100%" height="25" align="middle" class="bg_05">÷������</td>
				<br>���� ���� Ư�� �ٹ��� ��û�մϴ�. <br>
					<%=wyear%> �� <%=wmonth%> �� <%=wdate%> �� <br>
					��û�� : <%=user_name%> <br><br></td></tr>
			</table>

			<table width='100%' border='0' cellspacing='0' cellpadding='0'>
				<tr>
					<td width="50%" height="40" rowspan=3 align="left">SLCG-005-0</td> 
					<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)�������75g/m<sup>2</sup></td></tr></table>
	</td></tr></table>

<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_syear' value='<%=doc_syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=doc_smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=doc_sdate%>'>
	<input type='hidden' name='job_kind' value='<%=job_kind%>'>
	<input type='hidden' name='cost_prs' value='<%=cost_prs%>'>

	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
	<input type='hidden' name='read_worker' value='R'>
</form>

</body></html>



