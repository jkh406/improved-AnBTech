<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�������� ����"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

	//�������� �������
	String query		= "";
	String div_name		= "&nbsp;";		//�μ���
	String user_name	= "&nbsp;";		//����� ��
	String user_rank	= "&nbsp;";		//����� ����
	String doc_date		= "";			//�ۼ� �����

	String doc_syear	= "";			//�������� ��
	String doc_smonth	= "";			//�������� ��
	String doc_sdate	= "";			//�������� ��
	String lecturer_id	="&nbsp;";		//���� ���
	String lecturer_name ="&nbsp;";		//���� �̸�
	String major_kind	= "&nbsp;";		//�����ְ�
	String place		= "&nbsp;";		//���
	String participators_cnt = "&nbsp;";	//��������ο���
	String edu_subject	= "&nbsp;";		//������
	String antiprt_prs	= "&nbsp;";		//������ ó��		
	String bon_path		= "&nbsp;";		//���������н�
	String bon_file		= "&nbsp;";		//�����������ϸ�

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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_EDU")) {
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
	// �������� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","in_date",
						"e_year","e_month","e_date","lecturer_id","lecturer_name","major_kind",
						"place","part_cnt","edu_subject","antiprt_prs",
						"bon_path","bon_file"};
	bean.setTable("gyoyuk_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gy_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		doc_date = bean.getData("in_date");			//�ۼ������

		doc_syear = bean.getData("e_year");			//�ٹ����� ��
		doc_smonth = bean.getData("e_month");		//�ٹ����� ��
		doc_sdate = bean.getData("e_date");			//�ٹ����� ��

		lecturer_id = bean.getData("lecturer_id");			//���� ���
		lecturer_name = bean.getData("lecturer_name");		//���� �̸�
		major_kind = bean.getData("major_kind");			//�����ְ�
		place = bean.getData("place");						//���
		participators_cnt = bean.getData("part_cnt");		//��������ο���
		edu_subject = bean.getData("edu_subject");			//������
		antiprt_prs = bean.getData("antiprt_prs")==""?"&nbsp;":bean.getData("antiprt_prs");			//������ ó��		
		bon_path = bean.getData("bon_path");				//���������н�
		bon_file = bean.getData("bon_file");				//�����������ϸ�
	} //while

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);			//	  ��
		wdate = doc_date.substring(8,10);			//	  ��
	}
	//�����ְ� ó��
	String major_dp = "";	
	major_dp = StringProcess.repWord(major_kind,":","");	

	//������ ó��
	String antiprt_dp = "";	
	antiprt_dp = StringProcess.repWord(antiprt_prs,":","");	

	//���������б�
	String read_con = "",content = "";
	String full_path = upload_path + bon_path + "/bonmun/" + bon_file;
	read_con = text.getFileString(full_path);
	int con_len = read_con.length();
	for(int i=0; i<con_len; i++) {
		if(read_con.charAt(i) == '\n') content +="<br>";
		else content += read_con.charAt(i);
	}

	//�����ο��� ���� �ۼ��� �Ǳ����� ����÷� �����ϱ� (�⺻ : 20��)
	int content_height = 360;		//�������� ���� (�⺻��)
	int row_cnt = 24;				//content_height�� row��
	int people_height = 210;		//�Ǳ����� ��� ���� (�⺻�� : 20��)
	int people_line = 10;			//������ �ִ���� ��ܼ�
	int people_total = 20;			//��ü �Ǳ����� ��ܼ�
	int prt_cnt = Integer.parseInt(participators_cnt);
	if(prt_cnt >20) {
		int l_cnt = (prt_cnt + 1) / 2;		//���� ������ �ִ���� ��ܼ�
		int l_add = l_cnt - 10;				//�⺻���� �߰��� line ��
		int l_hgt = l_add * 21;				//�߰��� line�� line ũ������ 
		content_height = 210 - l_hgt;
		row_cnt = 24 - l_add;
		people_height = 210 + l_hgt;
		people_line = 10 + l_add;
		people_total = people_line * 2;
	}

	//�Ǳ����� ã��
	String[][] participators = new String[people_total][3]; 
	for(int k=0; k<people_total; k++) for(int m=0; m<3; m++) participators[k][m] = "";
	String[] stdColumn = {"participator_id","participator_name","prt_etc"};
	bean.setTable("gyoyuk_part");			
	bean.setColumns(stdColumn);
	bean.setOrder("gy_cid ASC");	
	query = "where (gy_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	
	int w = 0;
	while(bean.isAll()) {
		participators[w][0] = bean.getData("participator_id");			//�Ǳ����� ���
		participators[w][1] = bean.getData("participator_name");		//�Ǳ����� �̸�
		participators[w][2] = bean.getData("prt_etc");					//�����
		w++;
	} //while

%>
<html>
<head>
<title> ��������</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
<style type="text/css">
<!--
.num {text-indent:10;}
-->
</style>
</head>

<BODY topmargin="5" leftmargin="5">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2"> ��������</TD>
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
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="35%" class="bg_06"><%=doc_syear%> �� <%=doc_smonth%> �� <%=doc_sdate%> ��</td>
				<td width="15%" height="25" align="middle" class="bg_05">����</td>
				<td width="35%" class="bg_06"><%=lecturer_id%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�����ְ�</td>
				<td width="35%" class="bg_06"><%=major_dp%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">���</td>
				<td width="35%" class="bg_06"><%=place%>&nbsp;</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�ְ��μ�</td>
				<td width="35%" class="bg_06"><%=div_name%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">�������</td>
				<td width="35%" class="bg_06"><%=participators_cnt%> ��</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=edu_subject%></td>
				<td width="15%" height="25" align="middle" class="bg_05">������ó��</td>
				<td width="35%" class="bg_06"><%=antiprt_dp%>&nbsp;</td></tr>	
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="85%" class="bg_06" colspan="3"><pre><%=read_con%></pre>&nbsp;</td></tr>			
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">����������</td>
				<td width="85%" class="bg_06" colspan="3">
					<TABLE cellSpacing=2 cellPadding=0 width=100% border=0 bordercolordark=white bordercolorlight=#9CA9BA> 
					<tr>
						<td width="150" height="23" align="center" class=bg_05>�� ��</td>
						<td width="150" height="23" align="center" class=bg_05>�� ��</td>
						<td width="200" height="23" align="center" class=bg_05>�� ��</td>
						<td width="150" height="23" align="center" class=bg_05>�� ��</td>
						<td width="150" height="23" align="center" class=bg_05>�� ��</td>
						<td width="200" height="23" align="center" class=bg_05>�� ��</td>
					</tr>
					<tr>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //���� 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='left' valign='middle' class='num'>"+fmt.toDigits(i+1));
									out.println(participators[i][1]);
									out.println("</td></tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
								<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
									<% //���� 1 ~ people_line
									for(int i=0; i<people_line; i++) {
										out.println("<tr>");
										out.println("<td width='90' height='20' align='center' valign='middle'>");
										if(participators[i][0].length() != 0)
											out.println("<img src='../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
										out.println("</td>");
										out.println("</tr>");
										out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
									}
									%>
								</table></td>
						<td width="200" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //��� 1 ~ people_line
								for(int i=0; i<people_line; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='center' valign='middle'>");
									out.println(participators[i][2]);
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //����  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='left' valign='middle' class='num'>"+fmt.toDigits(i+1));
									out.println(participators[i][1]);
									out.println("</td></tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="150" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //����  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='90' height='20' align='center' valign='middle'>");
									if(participators[i][0].length() != 0)
										out.println("<img src='../../approval/sign/"+participators[i][0]+".gif' width=60 height=15 align='center'>");
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td>
						<td width="200" height="210" align="center" class=bg_07>
							<table width='100%' border='0' cellspacing='0' cellpadding='0' style="border-collapse: collapse" bordercolor="#111111">
								<% //���  people_line ~ people_total
								for(int i=people_line; i<people_total; i++) {
									out.println("<tr>");
									out.println("<td width='100' height='20' align='center' valign='middle'>");
									out.println(participators[i][2]);
									out.println("</td>");
									out.println("</tr>");
									out.println("<TR><TD height=1 bgcolor='white'></TD></TR>");
								}
								%>
							</table></td></tr>			
			</table>
	</td></tr></table>
<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='doc_syear' value='<%=doc_syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=doc_smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=doc_sdate%>'>
	<input type='hidden' name='lecturer_id' value='<%=lecturer_id%>'>
	<input type='hidden' name='lecturer_name' value='<%=lecturer_name%>'>
	<input type='hidden' name='major_kind' value='<%=major_kind%>'>
	<input type='hidden' name='place' value='<%=place%>'>
	<input type='hidden' name='participators_cnt' value='<%=participators_cnt%>'>
	<input type='hidden' name='edu_subject' value='<%=edu_subject%>'>
	<input type='hidden' name='antiprt_prs' value='<%=antiprt_prs%>'>
	<input type='hidden' name='content' value='<%=read_con%>'>

	<input type='hidden' name='doc_id' value='<%=doc_id%>'>
	<input type='hidden' name='bon_path' value='<%=bon_path%>'>
	<input type='hidden' name='read_student' value='R'>
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
	
	document.eForm.action = "gyoyuk_ilji_FP_Rewrite.jsp";
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
