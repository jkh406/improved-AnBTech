<%@ include file="../../admin/configPopUp.jsp"%>

<%@ 	page		
	info= "���ڰ��� ���缱 �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.*"
	import="com.anbtech.file.*"
	import="com.anbtech.gw.entity.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%	
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	FileWriteString text = new com.anbtech.file.FileWriteString();			//������ ���Ϸ� ���
	String Message = "";

	/*********************************************************************
	 	���缱 �����ϱ� 
	*********************************************************************/
	//from eleApproval_ViewShaareList.jsp
	String receive_data =Hanguel.toHanguel(request.getParameter("Lsave"));	//������ ���缱 ����
	String doc_dat=bean.getTime();						//���缱 ��������
	String doc_pid = bean.getID();						//���缱 ������ȣ
	String doc_pat = "/eleApproval/" + login_id;		//���� Dir Path (DB�� ����path �˷��ֱ�)

	/******************************************************************************************
	//���缱����� ���缱 �������̺� ���缱 �����ϱ� (������ ���̺�)
	*******************************************************************************************/
	String NAME = Hanguel.toHanguel(request.getParameter("save_name")); //������ ���缱 �̸�
	if(NAME == null) NAME = "";

	if((NAME.length() != 0) && (receive_data.length() != 0)) {
		String lineDir = upload_path + doc_pat + "/Linefile";	//File Path
		String line_fi = "LS" + doc_pid;										//����������� file ��

		//���Ϸ� ����
		String FullPathName = lineDir;		//Full path Name
		text.WriteHanguel(FullPathName,line_fi,receive_data);	//path,���ϸ�,����
		
		//������ ������ DB�� �����Ѵ�.
		String line_inputs="INSERT INTO APP_LINESAVE(pid,line_subj,writer,write_date,bon_path,line_file)";
			 line_inputs +=" values('"+doc_pid+"','"+NAME+"','"+login_id+"','"+doc_dat;
			 line_inputs +="','"+doc_pat+"','"+line_fi+"')";
		try { bean.execute(line_inputs);
			  receive_data = "";//clear
			  Message = "LINE_INSERT";
		} catch (Exception e) { Message = "QUERY"; }			
	} //if
%>

<HTML><HEAD><TITLE>���缱 ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="sForm" action="eleApproval_lineSave.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">���缱��</td>
           <td width="70%" height="25" class="bg_02"><input type="text" name="save_name" size="20"> 
			<input type='hidden' name='Lsave' value='<%=receive_data%>'></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:document.sForm.submit();'><img src='../images/bt_save.gif' border='0' align='absmiddle'></a> <a href='javascript:self.close();'><img src='../images/bt_cancel.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></form></BODY></HTML>



<!-- ****************** �޽��� ���޺κ� ****************************** -->

<% if(Message == "LINE_INSERT") { %>
<script>
alert('���缱�� ���� �Ǿ����ϴ�.')
close();
</script>
<% Message = "" ; } %>

<% if(Message == "Query") { %>
<script>
alert('���忡 �����߽��ϴ�.')
close();
</script>
<% Message = "" ; } %>

<% if(Message == "NO_LINE") { %>
<script>
alert('������ ���缱 ������ �����ϴ�.')
close();
</script>
<% Message = "" ; } %>

