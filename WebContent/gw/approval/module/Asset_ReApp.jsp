<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�ڻ� ���� 2���μ� ������"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
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
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //���ڰ��系�� & ���缱

	//���� �������
	String query = "", subject="";

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

	/*********************************************************************
	 	���޺���
		�������� [o_status : t:�ڻ��̰�, o:�ڻ����]
		������� [2:1����Ŵ��,3:1������Ϸ�,4:2����Ŵ��,5:2������Ϸ�]
	*********************************************************************/	
	String title_name = "";
	
	String mode = request.getParameter("mode");			if(mode == null) mode = "";		//mode
	String h_no = request.getParameter("h_no");			if(h_no == null) h_no = "";		//������ȣ
	String as_no = request.getParameter("as_no");		if(as_no == null) as_no = "";	//�ڻ��ȣ
	String o_status = request.getParameter("o_status");	if(o_status == null) o_status = "";	//��������
	String as_status = request.getParameter("as_status");if(as_status == null) as_status = "";	//�������
	if(o_status.equals("t")) title_name = "�ڻ��̰�";
	else if(o_status.equals("o")) title_name = "�ڻ����";
	else if(o_status.equals("l")) title_name = "�ڻ�뿩";

	String[] otColumn = {"pid"};
	bean.setTable("as_history");			
	bean.setColumns(otColumn);
	bean.setClear();
	bean.setOrder("pid DESC");	
	bean.setSearch("h_no",h_no);
	bean.init_unique();

	link_id = "";		//1�� ������ε� ������ ���������ȣ
	String plid = "";
	if(bean.isAll()){
		link_id = bean.getData("pid");
	}

	/*********************************************************************
	 	�ڻ꿡�� ���񸸵��
	*********************************************************************/	
	String[] tColumn = {"a.as_name","a.model_name","b.takeout_reason"};
	bean.setTable("as_info a,as_history b");			
	bean.setColumns(tColumn);
	bean.setOrder("a.model_name ASC");	
	query = "where h_no ='"+h_no+"' and a.as_no = b.as_no";
	bean.setSearchWrite(query);
	bean.init_write();

	if(bean.isAll()) {
		subject = title_name+" : ["+bean.getData("as_name")+"/"+bean.getData("model_name")+"]";
		subject += bean.getData("takeout_reason");
	}

	//*********************************************************************
	// 1�� �ְ��μ� ���缱 ���� �ޱ�
	//*********************************************************************
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine rline = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//���缱
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		rline = (TableAppLine)line_iter.next();
										
		if(rline.getApStatus().equals("���")) {
			wname = rline.getApName();	//�����
			wid = rline.getApSabun();	//����� ���
		}
		if(rline.getApStatus().equals("����"))  {
			vname = rline.getApName();	//������
			vid = rline.getApSabun();	//������ ���
			vdate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)
		}
		if(rline.getApStatus().equals("����"))  {
			dname = rline.getApName();	//������
			did = rline.getApSabun();	//������ ���
			ddate = rline.getApDate();	//������ �������� (������ �����ϰ� ������ ����ʵ�)\
		}
			
		line += rline.getApStatus()+" "+rline.getApSabun()+" "+rline.getApName()+" "+rline.getApRank()+" "+rline.getApDivision()+" "+rline.getApDate()+" "+rline.getApComment()+"<br>";
	}

	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//���缱

%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title><%=title_name%></title>
<LINK href="../../../gw/css/style.css" rel=stylesheet>
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>
<script language=javascript>
<!--
//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	window.open("../eleApproval_Share.jsp?target=eForm.doc_app_line","eleA_app_search_select","width=520,height=467,scrollbar=yes,toolbar=no,status=no,resizable=no");
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
	var doc_sub = '<%=subject%>';
	
	//�ϰ����� ����������
	document.eForm.action='../../../servlet/ApprovalAssetServlet';	
	document.eForm.app_mode.value='REQ';
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
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
-->
</script>

<BODY topmargin="0" leftmargin="0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> <%=title_name%></TD></TR></TBODY>
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
				<a href="Javascript:history.go(-2);"><img src="../../images/bt_cancel.gif" border="0"  align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=64% align=left valign='top'><%=line%></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<p>��</TD>
		<TD noWrap width=36% align=left><!-- ����ĭ-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">�����</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD>
					<TD noWrap width=80 align=middle class="bg_07">������</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"></TD>
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
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;
					<%
						if(ddate.length() == 0)	{//������
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
				%></TD>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- ����ĭ �� -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<iframe id="iframe_main" src="../../../servlet/AssetServlet?mode=<%=mode%>&h_no=<%=h_no%>&as_no=<%=as_no%>" width="100%" height="100%" marginwidth="0" marginheight="0" hspace="0" vspace="0" frameborder="0" scrolling="no"></iframe> </td></tr></table>

<TABLE><TR><TD width="5"></TD></TR></TABLE>
<!-- 2���������� -->
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
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='<%=title_name%>'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='h_no' value='<%=h_no%>'>					<% //�ڻ� ������ȣ %>
<input type='hidden' name='as_no' value='<%=as_no%>'>				<% //�ڻ��ȣ %>
<input type='hidden' name='o_status' value='<%=o_status%>'>			<% //�������� %>
<input type='hidden' name='as_status' value='<%=as_status%>'>		<% //������� %>

<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
</form>  
</center>
</body>
</html>

