<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ڸ��� �ۼ�"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean"	/>

<%
	String Message = "M";
	String id	= "";		//������ id
	String[] pid;			//������ȣ
	String[] smtp;			//smtp��
	String[] name;			//�����»�� �̸�
	String[] address;		//�����»�� �ּ�

	int rid = 0;			//���Ϲ��� ���ڰ�(stmp���ý�)
	String msg = "S";		//���� �޽��� ���

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = sl.id; 		//������ login id

	//���� �ʱ�ȭ
	Message = "M"; msg = "S";

	/*********************************************************************
	 	������ ���ϼ����� �о����
	*********************************************************************/
	String[] idColumn = {"pid","id","name","address","sserver"};
	bean.setTable("emailInfo");			
	bean.setColumns(idColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("id",id);			
	bean.init_unique();
	
	int cnt = bean.getTotalCount();
	if(cnt == 0) cnt = 1;

	pid		= new String[cnt];						//������ȣ
	smtp	= new  String[cnt];						//smtp��
	name	= new String[cnt];						//�����»�� �̸�
	address = new String[cnt];						//�����»�� �ּ�

	if(bean.isEmpty()) {
		pid[0] = "";
		smtp[0] = "";
		name[0] = "";
		address[0] = "";
		Message = "NODATA";
	} else {
		int i = 0;
		while(bean.isAll()) {
			pid[i]		= bean.getData("pid");				
			smtp[i]		= bean.getData("sserver");		
			name[i]		= bean.getData("name");	
			address[i]	= bean.getData("address");
			i++;
		} //while
	}

%>

<HTML><HEAD><TITLE>�������ۼ�</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_n.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <a href="javascript:emailSend()"><img border="0" src="../images/bt_export.gif" align="absmiddle"></a>
          <a href="javascript:self.close()"><img border="0" src="../images/bt_close.gif" align="absmiddle"></a></td></tr></tbody></table>

    <!--�߼�����-->
	<form name="mailForm" method="post" encType="multipart/form-data" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">�����¼���</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=smtp[0]%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif"><a href="Javascript:postReceiver();">�ۼ���</a></td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=name[rid]%> (<%=address[rid]%>)
				<input type="hidden" name="strFrom" value="<%=address[rid]%>">
				<input type="hidden" name="strName" value="<%=name[rid]%>"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">�޴»��</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><textarea name="strTo" rows="1" cols="70"></textarea><br>*�������� ��� ;�� ����</td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><input type="text" name="strSubject" size="76"></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="strContent" rows=22 cols=75></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
					<img src="../images/b-attach.gif" border="0">÷������: <input type="file" name="file" size="47"><BR>
					<img src="../images/b-attach.gif" border="0">÷������: <input type="file" name="file2" size="47"><BR>
					<img src="../images/b-attach.gif" border="0">÷������: <input type="file" name="file3" size="47"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>
  <input type='hidden' name='SEND' >
  </form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:self.close();"><img src="../images/close.gif" width="46" height="19" hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table>
</BODY>
</HTML>

<script>
<!--
//���ڸ��� ���� ó����� 
if("<%=Message%>" == "SEND"){ alert("<%=msg%>"); self.close(); }
//������ ������ �޽��� �����ϱ�
if("<%=Message%>" == "NODATA"){ alert("ȯ�漳���� ����� �����Ͻʽÿ�."); self.close(); }

//���� ������
function emailSend()
{
	if(mailForm.strTo.value == ""){	alert("�޴»���� �Է��Ͻʽÿ�.");	return;	}
	if(mailForm.strFrom.value == ""){	alert("�����»���� �Է��Ͻʽÿ�.");	return;	}
	if(mailForm.strSubject.value == ""){	alert("������ �Է��Ͻʽÿ�.");return;	}
	if(mailForm.strContent.value == ""){	alert("���������� �Է��Ͻʽÿ�.");return;	}

	document.mailForm.action="sendEmailSave.jsp";
	document.mailForm.submit();	
	
}

//���õ� smtp�� �ٷΰ���
function toSmtp(form) 
{
    var myindex=form.smtpUrl.selectedIndex;
    if (form.smtpUrl.options[myindex].value != null) {
         window.location=form.smtpUrl.options[myindex].value;
    }
}

-->
</script>
