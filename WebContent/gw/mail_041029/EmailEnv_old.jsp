<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ڸ��ϰ���"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"
	import="com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//�޽��� ���޺���
	String Message="";			//�޽��� ���� ����  
	String name = "";			//������ �̸�
	String email = "";			//������ email

	//�Էº���
	String pid = "";			//������ȣ
	String sno = "";			//���õǾ� �Ѿ�� �������ù�ȣ
	String rtype = "";			//�������ϼ��� ����(POP3,IMAP)
	String stype = "";			//�����¸��ϼ��� ����(SMTP)
	String rserver = "";		//�������ϼ�����
	String sserver = "";		//�����¸��ϼ����� 
	String username = "";		//������̸�
	String useraddress = "";	//������ּ� 
	String userid = "";			//�α׿� ID
	String userpassword = "";	//�α׿� 
	String readtype = "";		//������ ���� ��������(RO, RW)

	//�������� ����
	String ronly="";			//�����¸��ϼ����� ��������

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	String[] idColumn = {"id","name","email"};
	bean.setTable("user_table");			
	bean.setColumns(idColumn);
	bean.setOrder("id ASC");	
	bean.setSearch("id",login_id);			
	bean.init_unique();

	while(bean.isAll()) {
		name = bean.getData("name");				//������ ��
		email = bean.getData("email");				//������ email�ּ�
	} //while

	//Clear
	rtype=stype=rserver=sserver=username=useraddress=userid=userpassword=readtype=ronly="";

	/*********************************************************************
	 	���� �����ϱ� (from EmailMain.jsp)
	*********************************************************************/
	String NUM = request.getParameter("SNO");					//���õ� ��ȣ
	String PID = request.getParameter("PID");					//PID
	if(PID != null) pid = PID;
	if(NUM != null) sno = NUM;

	String[] chkemailColumn = {"pid","id","name","address","rtype","stype","rserver","sserver","loginid","loginpwd","readtype"};
	bean.setTable("emailInfo");			
	bean.setColumns(chkemailColumn);

	//PID�� �б�
	if(pid.length() > 0) {
		bean.setOrder("pid ASC");	
		bean.setSearch("pid",PID);			
		bean.init_unique();

		while(bean.isAll()) {
			rtype = bean.getData("rtype");					//�������ϼ��� ����(POP3,IMAP)
			stype =  bean.getData("stype");					//�����¸��ϼ��� ����(SMTP)
			rserver =  bean.getData("rserver");				//�������ϼ�����
			sserver =  bean.getData("sserver");				//�����¸��ϼ����� 
			username =  bean.getData("name");				//������̸�
			useraddress =  bean.getData("address");			//������ּ� 
			userid =  bean.getData("loginid");				//�α׿� ID
			userpassword =  bean.getData("loginpwd");		//�α׿� ��й�ȣ
			readtype = bean.getData("readtype");			//�޾ƿ� ���ϼ��� ��������
		} //while
		if(sno.equals("0")) ronly = "";						//�����Էµ� ��츸 ��������
		else ronly = "readOnly";							//�����Ұ���
	} else {		//�޴¸��ϼ����� �� ����� �ּ� �̸��� �ϳ��� �����
		bean.setOrder("pid ASC");	
		bean.setSearch("id",login_id);			
		bean.init_unique();

		if(bean.isEmpty()) ronly="";						//���� �űԵ��
		else {
			while(bean.isAll()) {
				sserver =  bean.getData("sserver");				//�����¸��ϼ����� 
				username =  bean.getData("name");				//������̸�
				useraddress =  bean.getData("address");			//������ּ� 
			} //while
			ronly="readOnly";	//�����Ұ�
		} //if
	} //if

	/*********************************************************************
	 	���� �����ϱ�
	*********************************************************************/
	if(request.getParameter("rtype") != null){
		//�Է�/������ ���� �б�
		rtype = Hanguel.toHanguel(request.getParameter("rtype"));			//�������ϼ��� ����(POP3,IMAP)
		stype = Hanguel.toHanguel(request.getParameter("stype"));				//�����¸��ϼ��� ����(SMTP)
		rserver = Hanguel.toHanguel(request.getParameter("rserver"));			//�������ϼ�����
		sserver = Hanguel.toHanguel(request.getParameter("sserver"));			//�����¸��ϼ����� 
		username = Hanguel.toHanguel(request.getParameter("username"));			//������̸�
		useraddress = Hanguel.toHanguel(request.getParameter("useraddress"));	//������ּ� 
		userid = Hanguel.toHanguel(request.getParameter("userid"));				//�α׿� ID
		userpassword = Hanguel.toHanguel(request.getParameter("userpassword")); //�α׿� ��й�ȣ

		readtype = request.getParameter("readtype");							//���ϼ��� ���� ��������
		if(readtype == null) readtype = "RW";
		else readtype = "RO";
	
		//����/���� ����ϱ�
		if(pid.length() == 0) {					//�ű� ���
			String inputs = "INSERT INTO emailInfo ";
			inputs += "(pid,id,name,address,rtype,stype,rserver,sserver,loginid,loginpwd,readtype) values('";
			inputs += bean.getID() + "','" + login_id + "','" + username + "','" + useraddress + "','";
			inputs += rtype + "','" + stype + "','" + rserver + "','" + sserver + "','";
			inputs += userid + "','" + userpassword + "','" + readtype + "')";

			if((rserver.length() != 0) && (userid.length() != 0) && (userpassword.length() != 0)){
				//���� �ߺ� ��� �˻� (�������ϼ�����,�����ID �ߺ��˻�)
				String[] emailColumn = {"id","rtype","rserver","loginid","loginpwd"};
				bean.setTable("emailInfo");			
				bean.setColumns(emailColumn);
				bean.setOrder("id ASC");	
				bean.setSearch("id",login_id,"rserver",rserver,"loginid",userid);			
				bean.init_unique();

				if(bean.isEmpty()) {
					try { bean.execute(inputs); } catch (Exception e) { out.println("error : " + e);}
				}
				//out.println("int : " + inputs + "<br>");	
			} //if
		} else {							//�����ϱ�
			String updata = "UPDATE emailInfo set name='" + username + "',address='";
				updata += useraddress + "',rtype='" + rtype + "',stype='" + stype + "',rserver='";
				updata += rserver + "',sserver='" + sserver + "',loginid='" + userid + "',loginpwd='";
				updata += userpassword + "',readtype='" + readtype + "' where pid='" + pid + "'";
			try { bean.execute(updata); } catch (Exception e) { out.println("error : " + e);}
			//out.println("up : " + updata + "<br>");
		}//if 
	}//if

%>


<HTML><HEAD><TITLE>POP���� ���/����</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<form name="sForm" type="post" style="margin:0">
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�޴¸���</td>
           <td width="80%" height="25" class="bg_02">
			   <select name="rtype">
			<% 
				if(rtype.equals("IMAP")) {
					out.println("<option>POP3");
					out.println("<option selected>IMAP");
				} else {
					out.println("<option selected>POP3");
					out.println("<option>IMAP");
				}
			%></select> <input name="rserver" type="text" value="<%=rserver%>" size="30">
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <% if(sno.equals("0")) { %>
         <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�����¸���</td>
           <td width="80%" height="25" class="bg_02">
				<select name="stype"><option selected>SMTP</select> 
				<input name="sserver" type="text" value="<%=sserver%>" size="30" <%=ronly%> >
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		  <% } else { %>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�����¸���</td>
           <td width="80%" height="25" class="bg_02">
				<select name="stype"><option selected>SMTP</select> <input name="sserver" type="text" value="<%=sserver%>" size="30" <%=ronly%> >
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
			<input name="sserver" type="hidden" value="<%=sserver%>" size="30" <%=ronly%> >
		  <% } %>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�α���ID</td>
           <td width="80%" height="25" class="bg_02"><input name="userid" type="text" value="<%=userid%>" size="30"><br>�ش� ������ �����ϱ� ���� �α��� ID�� �Է��ϼ���.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�α���PW</td>
           <td width="80%" height="25" class="bg_02"><input name="userpassword" type="password" value="<%=userpassword%>" size="20"><br>�α��� �н����带 �Է��ϼ���.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">��޼���</td>
           <td width="80%" height="25" class="bg_02"><input name="readtype" type="checkbox" <% if(readtype.equals("RO")) out.println("CHECKED"); %> value="RO" size="20" >������ �޽��� ���纻 ����<br>üũ �� �޽����� �����͵� ������ ������ �����մϴ�.</td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">�̸�</td>
           <td width="80%" height="25" class="bg_02">
		   <%
			if(username.length() == 0) out.println("<input name='username' value='" + name + "' type='text' size='10' readOnly>");
			else out.println("<input name='username' value='" + username + "' type='text' size='10' readOnly>");
		   %><br>�����̶��� ��µ� �̸��� �Է��ϼ���.
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
		 <tr>
           <td width="20%" height="25" class="bg_01" background="../images/bg-01.gif">���ڿ���</td>
           <td width="80%" height="25" class="bg_02">
		   <%
			if(useraddress.length() == 0) out.println("<input name='useraddress' value='" + email + "'  type='text' size='30' readOnly>");
			else out.println("<input name='useraddress' value='" + useraddress + "'  type='text' size='30'>");
		   %><br>�����̶��� ��µ� ���ڿ��� �ּҸ� �Է��ϼ���.
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table></form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="Javascript:envTest()"><img border="0" src="../images/bt_test.gif" align="absmiddle"></a> <a href="javascript:envSave();"><img border="0" src="../images/bt_save.gif" align="absmiddle"></a> <a href='javascript:self.close()'><img border="0" src="../images/bt_cancel.gif" align="absmiddle"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--

function centerWindow() 
{ 
        var sampleWidth = 500;                        // �������� ���� ������ ���� 
        var sampleHeight = 520;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

//�����ϱ�
function envSave()
{
	if(sForm.rserver.value == ""){	alert("�޴¸��ϼ������� �Է��Ͻʽÿ�.");	return;	}
	if(sForm.userid.value == ""){	alert("�����ID�� �Է��Ͻʽÿ�.");	return;	}
	if(sForm.userpassword.value == ""){	alert("����� ��й�ȣ�� �����Ͻʽÿ�.");return;	}

	document.sForm.action="EmailEnv.jsp";
	document.sForm.submit();
    if(confirm("���� �Ͻðڽ��ϱ�?"))	{ self.close(); } else { return; }
}

//���� �˻��ϱ�
function envTest()
{
	rt = sForm.rtype.options[sForm.rtype.selectedIndex].text;			//�������� ��������
	
	if(sForm.rserver.value == ""){	alert("�޴¸��ϼ������� �Է��Ͻʽÿ�.");	return;	}
	else rs = sForm.rserver.value ;

	ss = sForm.sserver.value ;			//�����¸��� ������

	if(sForm.useraddress.value == ""){	alert("�����ּҸ� �Է��Ͻʽÿ�.");	return;	}
	else ua = sForm.useraddress.value;

	if(sForm.userid.value == ""){	alert("�����ID�� �Է��Ͻʽÿ�.");	return;	}
	else ui = sForm.userid.value;

	if(sForm.userpassword.value == ""){	alert("����� ��й�ȣ�� �����Ͻʽÿ�.");return;	}
	else up = sForm.userpassword.value; 

	inmail = rt + ";" + rs + ";" + ui + ";" + up + ";" + ss + ";" + ua + ";" + <%=sno%> + ";";
	wopen('EnvTest.jsp?INF='+inmail,'post_write','400','200','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>