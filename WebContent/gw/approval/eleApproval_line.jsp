<%@ include file="../../admin/configPopUp.jsp"%>

<%@ 	page		
	info= "���ڰ��� ���缱 ã��"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.lang.SecurityException"
	import="java.io.UnsupportedEncodingException"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.*"
	import="com.anbtech.file.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	//---------------------------
	//���� ���� 
	//---------------------------
	String Message="";			//�޽��� ���� ����  

	//������ ���� ���޺���
	String name = "";			//������ �̸�
	String division = "";		//������ �μ���	

	//����� ������� ���޺���
	String[] line_subj;			//����� ������� ��
	String[] line_file;			//����� ������� ���ϸ�
	String[] pid;				//�����ڵ� 
	int line_cnt;				//���� ��

	String search_subj="";		//ã��������� �ڵ� �������
	String LINE_DATA="";		//����� ������ ����

	//��û�� window�� ���� ����
	String TITLE="";			//��û�� ���缱 ������ڴ�

	//����� ���缱 ������ �о� �Ѱ��� ����
	String SEND_DATA;			// ���缱 �Է³��� �Ѱ��ٵ�����
	int send_cnt;				// �Էµ� ���缱 ����


	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//text���� �б�

	/*********************************************************************
	 	���缱�� ����
	*********************************************************************/
	String read_line_file = request.getParameter("L_FILE");
	String read_line_name = request.getParameter("L_NAME");

	if(read_line_file != null){
		//���缱�� �ѱۺ���
		read_line_name = Hanguel.toHanguel(read_line_name);					//�ѱ�
		String chg_data = "update APP_LINESAVE set line_subj='" + read_line_name + "' where line_file='" + read_line_file + "'";
		try { bean.execute(chg_data); } catch (Exception e) { Message = "QUERY"; }
	} //if

	/*********************************************************************
	 	���缱�� ����
	*********************************************************************/
	String delete_line_file = request.getParameter("D_FILE");

	if(delete_line_file != null){
		//DB���� �ش� ���ڵ� ����
		String DEL = "delete from APP_LINESAVE where line_file='" + delete_line_file + "'";
		bean.execute(DEL);

		//�ش� ���ϻ��� �ϱ�(Text file����)
		String Textdir = upload_path+"/eleApproval/"+login_id+"/Linefile/"+delete_line_file;
		text.delFilename(Textdir);
	} //if

	/*********************************************************************
	 	����� ��ü�� ���缱 �˾ƺ��� (login�� �۾��ڿ� ���ؼ�)
	*********************************************************************/
	String[] line_dbColumns = {"pid","line_subj","writer","write_date","bon_path","line_file"};
	bean.setTable("APP_LINESAVE");
	bean.setColumns(line_dbColumns);
	bean.setOrder("pid ASC");
	bean.setSearch("writer",login_id);	
	bean.init();				//������ �����Ͽ� Table�� ���

	line_cnt=bean.getTotalcount();

	line_subj = new String [line_cnt];	//saved Line name
	line_file = new String [line_cnt];	//saved file name
	pid = new String [line_cnt];		//�����ڵ�
	int k=0;
	while(bean.isAll()){
		pid[k] = bean.getData("pid");
		line_subj[k] = bean.getData("line_subj");
		line_file[k] = bean.getData("line_file");
		k++;
	}

	/****************************************************************
		���缱 ���� �б�
	****************************************************************/
	String ReadFile = request.getParameter("moDivision");		//���缱 ���ϸ�
	if(ReadFile == null) ReadFile = "";
	LINE_DATA = "";
	//���Ϸ� ã�� 
	if(ReadFile.length() != 0){
		String LineSavefile = upload_path+"/eleApproval/"+login_id+"/Linefile/"+ReadFile;
		
		try { LINE_DATA = text.getFileString(LineSavefile);	   		//Text file Read 
		} catch (Exception e) { }
		LINE_DATA = str.repWord(LINE_DATA,";",";\r");
	}

	// ���缱 �����ľ��ϱ�
	SEND_DATA = "";
	SEND_DATA = LINE_DATA;
	send_cnt = 0;
	for(int ld=0; ld < LINE_DATA.length(); ld++){  
		char ch = LINE_DATA.charAt(ld);
		if(ch == ';') send_cnt++;
	}	

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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_app_o.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">
				<table border=0 width=100%>
				<tr><td width=100%>
					<form method=post  name="sForm" style="margin:0">
						<select name="moDivision" onChange="toDivision(this.form);">
						<option value='eleApproval_line.jsp?moDivision=&subName='>::���缱 ����::</option>
						<%for(int gk=0; gk < line_cnt; gk++) { 
							out.print("<option "); 
							//���õ� ���缱�� selected ǥ���ϱ�
							String div_fn = request.getParameter("moDivision");
							if(div_fn != null){ 
								if(div_fn.equals(line_file[gk])) out.print("selected ");
							}
							String[] send_p = new String[line_cnt];
							send_p[gk] = line_file[gk];	//���Ϸ� ���� �б� ������ ���ϸ��� 
							out.println("value='eleApproval_line.jsp?moDivision=" + send_p[gk] + "&subName=" + gk + "'>" + line_subj[gk]); 
						}%> 
					</td></select></form></tr>
				<tr><td width=100%><b>���缱��</b><br>
						<form name="cForm" method="post" style="margin:0">
							<input type="hidden" name="L_FILE" value='<%=request.getParameter("moDivision")%>'>
							<input type="text" size="14" name="L_NAME" <% if((request.getParameter("subName") == null) || ((request.getParameter("subName").length() == 0))) out.println("vlaue="); else out.println("value='" + line_subj[Integer.parseInt(request.getParameter("subName"))] + "'"); %>></form></td></tr>
				<tr><td width=100% height="30"><form name="dForm" method="post" style="margin:0">
					<a href="javascript:line_change();"><img src='../images/bt_modify.gif' align='absmiddle' border='0'></a>	<input type="hidden" name="D_FILE" value='<%=request.getParameter("moDivision")%>'> <a href="javascript:line_delete();"><img src='../images/bt_del.gif' align='absmiddle' border='0'></a></td></form></td></tr></table>
		   </td>
           <td width="70%" height="25" class="bg_02">
			<form name="aForm" method="post" style="margin:0">
			  <TEXTAREA NAME="dec_app_line" rows=7 cols=40 readOnly><%=LINE_DATA%></TEXTAREA></form>
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:return_value('<%=send_cnt%>','<%=SEND_DATA%>')"><img src='../images/bt_confirm.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--

//������â �ʱ�ȭ
function setFocus() 
{
	defaultStatus = "���缱 ���峻�� �ҷ�����";		//������ �ϴ� ��¸޽���
	window.resizeTo(580,380);				//������ ũ������	
	window.moveTo(100,100);					//������ �����ġ ����
}

//�̸�����
function line_change()
{
	document.cForm.action = "eleApproval_line.jsp";
	document.cForm.submit();
}

//�̸�����
function line_delete()
{
	document.dForm.action = "eleApproval_line.jsp";
	document.dForm.submit();
}

//�������쿡 �� �����ֱ�
function return_value(no,str)
{
	var cnt = no;
	var my_str =str;
	var data = "";

	for(i = 0; i < cnt; i++) {
		var p = my_str.indexOf(';');
		if(i == 0)
			data = my_str.substring(0,p) + ';';
		else
			data = data + my_str.substring(0,p) + ';';
		my_str = my_str.substring(p+1,my_str.length);	
	}  //for

	//�Ѱ��ֱ�	
	opener.lineCall(no,data);
	this.close();
}

//���õ� ���缱 �ٷ��б�
function toDivision(form) {
    var myindex=form.moDivision.selectedIndex;
    if (form.moDivision.options[myindex].value != null) {
         window.location=form.moDivision.options[myindex].value;
    }
}

-->
</script>


<!-- ****************** �޽��� ���޺κ� ****************************** -->

<% if(Message == "NO_SESSION") { %>
<script>
alert("���ӽð��� ����Ǿ����ϴ�.\n\n�ٽ� ������ �����Ͻʽÿ�.")
close()
</script>
<% Message = "" ; } %>

<% if(Message == "UPDATE") { %>
<script>
alert("���缱���� ����Ǿ����ϴ�.")
</script>
<% Message = "" ; } %>

<% if(Message == "QUERY") { %>
<script>
alert("���缱�� ���濡 ������ �ֽ��ϴ�.")
</script>
<% Message = "" ; } %>

