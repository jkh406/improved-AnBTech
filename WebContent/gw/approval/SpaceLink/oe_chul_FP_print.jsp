<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page		
	info= "����� ����"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

	//�ް��� �������
	String query		= "";
	String div_name		= "&nbsp;";			//�μ���
	String user_name	= "&nbsp;";			//����� ��
	String user_rank	= "&nbsp;";			//����� ����
	String fellow_names = "&nbsp;";			//������	 ���/�̸�;
	String f_names		= "&nbsp;";			//������	 �̸�,
	String syear		= "";				//���� ��
	String smonth		= "";				//    ��
	String sdate		= "";				//    ��
	String StartTime	= "";				//���۽ð�
	String EndTime		= "";				//����ð�
	String dest			= "&nbsp;";			//������
	String traffic_way	= "&nbsp;";			//������
	String purpose		= "&nbsp;";			//����
	String period		= "";				//from ~ to �Ⱓ : ��
	String doc_date		= "";				//�Է���
	String rec			= "&nbsp;";			//�μ��ΰ���
	String tel			= "&nbsp;";			//��޿���ó

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
			if(PROCESS.equals("APP_BOX") || PROCESS.equals("APP_OUT")) {
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
	// 	����� ���� �˾ƺ���
	*********************************************************************/	
	String[] Column = {"ac_name","user_name","user_rank","fellow_names","u_year","u_month",
		"u_date","gt_time_per","gt_dest","traffic_way","gt_purpose","proxy","em_tel","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+doc_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//�μ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		user_rank = bean.getData("user_rank");		//�ۼ��� ����
		fellow_names = bean.getData("fellow_names");//������ ���/�̸�
		syear = bean.getData("u_year");				//���� ��
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    ��
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    ��

		period = bean.getData("gt_time_per");		//�Ⱓ
		StartTime = period.substring(0,5);			//���۽ð�
		EndTime = period.substring(6,11);			//����ð�
	
		dest = bean.getData("gt_dest");				//������
		traffic_way = bean.getData("traffic_way");	//������
		purpose = bean.getData("gt_purpose");		//����
		rec = bean.getData("proxy");				//�μ��ΰ���
		tel = bean.getData("em_tel");				//��޿���ó
		doc_date = bean.getData("in_date");			//�ۼ������
	} //while

	//�ۼ������ ���ϱ�
	String wyear="",wmonth="",wdate="";
	if(doc_date.length() != 0) {
		wyear = doc_date.substring(0,4);			//�ۼ���
		wmonth = doc_date.substring(5,7);			//	  ��
		wdate = doc_date.substring(8,10);			//	  ��
	}
	
	//������ �̸��� ���ϱ�
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
	
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String ns = name.nextToken();
			if(nm == 1) f_names += ns + ",";
			nm++;
			if(nm > 2) break;
		}

	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

%>

<html>
<head>
<title>�����</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5" oncontextmenu="return false">
<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">�����</TD>
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
				<td width="15%" height="25" align="middle" class="bg_05">�ҼӺμ�</td>
				<td width="35%" class="bg_06"><%=div_name%>&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">�����</td>
				<td width="35%" class="bg_06"><%=user_name%>&nbsp;</td></tr>
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��������</td>
				<td width="35%" class="bg_06"><%=syear%>�� <%=smonth%>�� <%=sdate%>��&nbsp;</td>
				<td width="15%" height="25" align="middle" class="bg_05">����ð�</td>
				<td width="35%" class="bg_06"><%=StartTime%> ~ <%=EndTime%>&nbsp;</td></tr>				
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�༱��</td>
				<td width="35%" class="bg_06"><%=dest%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=traffic_way%>&nbsp;</td></tr>								
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�������</td>
				<td width="35%" class="bg_06"><%=purpose%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">������</td>
				<td width="35%" class="bg_06"><%=f_names%><img src='' width='0' height='0'></td></tr>											
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">�����μ���</td>
				<td width="35%" class="bg_06"><%=rec%><img src='' width='0' height='0'></td>
				<td width="15%" height="25" align="middle" class="bg_05">��޿���ó</td>
				<td width="35%" class="bg_06"><%=tel%><img src='' width='0' height='0'></td></tr>								
			<tr>
				<td width="15%" height="25" align="middle" class="bg_05">��û����</td>
				<td width="85%" class="bg_06" colspan="3"><%=anbdt.getYear()%> �� <%=anbdt.getMonth()%> �� <%=anbdt.getDates()%>��</td></tr></table>
	</td></tr></table>

<form name="eForm" method="post" encType="multipart/form-data">
	<input type='hidden' name='doc_app_line' value=''>
	<input type='hidden' name='fellow_names' value='<%=fellow_names%>'>
	<input type='hidden' name='purpose' value='<%=purpose%>'>
	<input type='hidden' name='dest' value='<%=dest%>'>
	<input type='hidden' name='traffic_way' value='<%=traffic_way%>'>
	<input type='hidden' name='doc_syear' value='<%=syear%>'>
	<input type='hidden' name='doc_smonth' value='<%=smonth%>'>
	<input type='hidden' name='doc_sdate' value='<%=sdate%>'>
	<input type='hidden' name='hdStartTime' value='<%=StartTime%>'>
	<input type='hidden' name='hdEndTime' value='<%=EndTime%>'>
	<input type='hidden' name='doc_receiver' value='<%=rec%>'>
	<input type='hidden' name='doc_tel' value='<%=tel%>'>
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
	
	document.eForm.action = "oe_chul_FP_Rewrite.jsp";
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