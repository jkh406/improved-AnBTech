<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "���躯�� ������ ����ϱ�"		
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
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	
	String writer_id = "";			//�ۼ��� ���
	String writer_name = "";		//�ۼ��� �̸�

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

	/*********************************************************************
	 	�����(login) �˾ƺ���
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while

	//*********************************************************************
	// ���缱 ���� �ޱ�
	//*********************************************************************
	pid = request.getParameter("pid");			if(pid == null) pid = "";			//������ȣ
	doc_id = request.getParameter("doc_id");	if(doc_id == null) doc_id = "";		//������ȣ(��ũ������ pid�͵���)
	//doc_id �� ������ȣ+eco no�� ������
	int sep_no = doc_id.indexOf("|");
	int len_no = doc_id.length();
	String eco_pid = doc_id.substring(0,sep_no);
	String eco_no = doc_id.substring(sep_no+1,len_no);

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
		else {	//����
			if(PROCESS.equals("APP_BOX")) {
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
	connMgr.freeConnection("mssql",con);
%>

<html>
<head><title>���躯��</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="5" leftmargin="5">

<!-- �ΰ�,����,��ư -->
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../../images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">���躯��</TD>
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
		<TD noWrap width=50 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE><BR>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center"><iframe id="iframe_main" src="../../../servlet/CbomProcessServlet?mode=ecc_app_print&pid=<%=eco_pid%>&eco_no=<%=eco_no%>" width="100%" height="640" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no">
</iframe></td></tr></table>
</body>
</html>

<script language=javascript>
<!--

//����ϱ�
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
-->
</script>
