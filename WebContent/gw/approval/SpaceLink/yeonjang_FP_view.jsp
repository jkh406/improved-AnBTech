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
	String query = "";
	String div_name = "";			//�μ���
	String user_name = "";			//����� ��
	String user_rank = "";			//����� ����
	String doc_date = "";			//�ۼ� �����

	String doc_syear = "";			//�ٹ����� ��
	String doc_smonth = "";			//�ٹ����� ��
	String doc_sdate = "";			//�ٹ����� ��
	String job_kind = "";			//�з�
	String cost_prs = "";			//�ĺ�����Ȯ��

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
	
	//��������(anb) ���� ��������(storehouse)�� ���� ���� : 200408 : ����
	if(PROCESS.equals("DEL_BOX")) masterDAO.getTable_MasterPid(pid,"storehouse.dbo.app_save");
	else masterDAO.getTable_MasterPid(pid);		
		
	ArrayList app_line = new ArrayList();				
	app_line = masterDAO.getTable_line();		
	Iterator app_iter = app_line.iterator();
	while(app_iter.hasNext()) {
		app = (TableAppLine)app_iter.next();
		
		//���缱
		cmt = app.getApComment(); if(cmt == null) cmt = "";
		t_cmt="";
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

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>����ٹ���û��</title> 
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>
<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<center>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='right' height=20>
	<% 	
	//���繮���� ��� ó��
	if(PROCESS.equals("APP_ING")) {  %>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
		<% if(doc_ste.equals("APV") || doc_ste.equals("APL")) { //����, ���δܰ� %>
			<a href='Javascript:winDecision();'><img src='../../../gw/img/button_approval.gif' align='middle' border='0'></a> <!-- ���� -->
			<a href='Javascript:winReject();'><img src='../../../gw/img/button_reject.gif' align='middle' border='0'></a> <!-- �ݷ� -->
		<% } else { //�����ܰ� %>
			<a href='Javascript:winDecision();'><img src='../../../gw/img/button_agree.gif' align='middle' border='0'></a> <!-- ���� -->
			<a href='Javascript:winReject();'><img src='../../../gw/img/button_reject.gif' align='middle' border='0'></a> <!-- �ݷ� -->
		<% } %>
			<a href='Javascript:winprint();'><img src='../../../gw/img/button_print.gif' align='middle' border='0'></a> <!-- ��� -->
			<a href='Javascript:self.close();'><img src='../../../gw/img/002_013_del.gif' align='middle' border='0'></a> <!-- �ݱ� -->
		</div>
	<% } 
	//���ۼ��� View ����	
	else { %>
		<div id="print" style="position:absolute;left:370px;top:60px;width:300px;height:10px;visibility:visible;">
		<% if(PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX")) {	//�ӽú�����,�ݷ��� ó�� %> 
			<a href='Javascript:winRewrite();'><img src='../../images/bt_rewrite.gif' align='absmiddle' border='0'></a> <!-- ���ۼ� -->
			<a href='Javascript:winDelete(<%=pid%>);'><img src='../../images/bt_del.gif' align='absmiddle' border='0' alt='����'></a>
		<%	} %>
			
		<!-- ������� ������[����� �ٷ� �մܰ��] : 200408 : ���� -->
		<% if(app_cancel.equals("Y")) {		//������ %> 
			<a href='Javascript:appCancel(<%=pid%>);'><img src='../../images/bt_app_cancel.gif' align='absmiddle' border='0' alt='������'></a>
		<%	} %>
		<!-- ���������� ���޹������ �� �׷��� ������� �޴� : 200408 : ���� -->
		<% if(PROCESS.equals("DEL_BOX")) {	%>
			<a href='Javascript:winclose();'><img src='../../images/bt_close.gif' align='absmiddle' border='0' alt='�ݱ�'></a>
		<% } else { %>
			<a href='Javascript:winprint(<%=pid%>);'><img src='../../images/bt_print.gif' align='absmiddle' border='0' alt='�μ�'></a>
			<a href='Javascript:history.go(-1);'><img src='../../images/bt_list.gif' align='absmiddle' border='0' alt='���'></a>
		<% } %>
		</div>
	<% } %>
	</td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
    <tr><td width=100% align='center' height=30><font size=3><b>�� �� �� �� �� û �� </b></font></td></tr>
</table>

<table width='640' border="0" cellspacing="0" cellpadding="0">
	<tr><td width=100% align='left' height=20><img src='../../../gw/img/slink_logo.jpg' align='middle' border='0'></td></tr>
</table>

<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111" bgcolor='#F2F2F2'> 
	<tr>            
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>
		<% if(line_cnt < 7) {    %> 
			<td width="420" height="96" rowspan=3 valign="top"><%=line%></td>
		<% } else {				 %>
			<td width="420" height="96" rowspan=3 align="center" valign="middle">
			<TEXTAREA rows=7 cols=66 readOnly style="text-align:left;font-size:9pt;border:1px solid #787878;"><%=t_line%></TEXTAREA>
			</td>
		<% }					 %>
		<td width="20" height="96" rowspan=3 align="center">��<p>��</td>                     
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
		<td width="60" bgcolor="#E0D6C1" align="center" height="22">�� ��</td>            
	</tr>          
	<tr height="26">         
		<td width="60" height="50" align="center"><img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></td>   
		<td width="60" height="50" align="center">
		<% //������ ������ ���� ǥ���ϱ� (��, �ݷ������� �ƴѰ�츸)
			if(vdate.length() == 0)	{//������
				if(ddate.length() == 0) out.println("&nbsp;");
				else out.println("����");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
			}
		%></td>   
		<td width="60" height="50" align="center">
		<%
			if(ddate.length() == 0)	{//������
				out.println("&nbsp;");
			} else {
				out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
			}
		%>	
		</td>   
	</tr>   
	<tr height="26">         
		<td width="60" height="24" align="center">&nbsp;<%=wname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=vname%>&nbsp;</td>   
		<td width="60" height="24" align="center">&nbsp;<%=dname%>&nbsp;</td>   
	</tr>   
</table>
		
<table width='640' border='1' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111"> 
	<tr>
		<td width="100" height="30" align="center" valign="middle">�� �� �� ��</td>
		<td width="540" height="30" align="center" colspan=3>&nbsp;<%=div_name%></td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle">���� �ٹ���</td>
		<td width="540" height="30" align="center" colspan=3>
			<%=doc_syear%> �� <%=doc_smonth%> �� <%=doc_sdate%> ��  </td>
	</tr>
	<tr>
		<td width="100" height="30" align="center" valign="middle"><b>�� �� ��</b></td>
		<td width="340" height="30" align="center" valign="middle"><b>��������</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>��ǽð�</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>�μ���Ȯ��</b></td>
	</tr>
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
		<td width="100" height="30" align="center" valign="middle"><b>�� ��</b></td>
		<td width="340" height="30" align="center" valign="middle"><b>&nbsp;<%=job_kind%></b></td>
		<td width="100" height="30" align="center" valign="middle"><b>�ĺ�����Ȯ��</b></td>
		<td width="100" height="30" align="center" valign="middle"><b>&nbsp;<%=cost_prs%></b></td>
	</tr>
	<tr>
		<td width="640" height="60" align="center" colspan=4><br>
			���� ���� Ư�� �ٹ��� ��û�մϴ�. <br>
			<%=wyear%> �� <%=wmonth%> �� <%=wdate%> �� <br>
			��û�� : <%=user_name%> <br><br></td>
	</tr>
</table>

<table width='640' border='0' cellspacing='0' cellpadding='0'> 
	<tr>            
		<td width="50%" height="40" rowspan=3 align="left">SLCG-005-0</td> 
		<td width="50%" height="40" rowspan=3 align="right">A4(210x297mm)�������75g/m<sup>2</sup></td> 
	</tr>   
</table>

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
</center>
</body>
</html>

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
//�ݱ�
function winclose()
{
	if(window.name == 'save_doc') self.close();		//�������� ���ڿ����� ����
	else history.go(-1);							//�������� ����ȭ�鿡�� ����
}
//������ �����ϱ�
function appCancel(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_CANCEL&PID="+pid;
	document.eForm.submit();
}
//����,�ݷ����� �����ϱ�
function winDelete(pid)
{
	document.eForm.action = "../../../servlet/ApprovalProcessServlet?mode=APP_DELETE&PID="+pid;
	document.eForm.submit();
}
-->
</script>
