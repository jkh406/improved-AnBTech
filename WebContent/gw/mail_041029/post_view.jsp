<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "���ο��� ����"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="javax.mail.*"
	import="javax.mail.internet.*"
	import="javax.activation.*"
	import="com.anbtech.file.textFileReader"
%>
<%@	page import="com.anbtech.text.Hanguel"			%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
	
<%

	//�޽��� ���޺���
	String Message="";		//�޽��� ���� ����  

	String id = "";			//������ id
	String name="";			//������ �̸�

	//ȭ����º���
	String wName = "";		//�߽��� �̸�
	String wDivision = "";	//�߽��� �μ���
	String wTel = "";		//�߽��� ��ȭ��ȣ

	String psub = "";		//����
	String pwde = "";		//����
	String pwrn = "";		//�ۼ��� �̸�
	String prec = "";		//������ ���
	String state= "";		//email ���� ��ü���ο������� �Ǵ�
	String psel = "";		//��޼���
	String path = "";		//÷������ path
	String Bpath = "";		//�������� path

	String pfie = "";		//���� ���ϸ� (window)
	String pcon = "";		//�������� (unix)

	String apath = "";		//÷������ Path
	String pad1o = "";		//÷�ε� ���ϸ�1 �����̸�	
	String pad1f = "";		//÷�ε� ���ϸ�1
	String file1_size="";	//����ũ��

	String pad2o = "";		//÷�ε� ���ϸ�2 �����̸�	
	String pad2f = "";		//÷�ε� ���ϸ�2
	String file2_size="";	//����ũ��

	String pad3o = "";		//÷�ε� ���ϸ�3 �����̸�	
	String pad3f = "";		//÷�ε� ���ϸ�3	
	String file3_size="";	//����ũ��

	//���޹��� ���� (from post_main.jsp)
	String pid = "";		//�������� ��ȣ
	String TITLE = "";		//���� ������ ����(�ѱ�ǥ��)
	String TIT = "";		//���� ������ ����(����ǥ��:�Ѱܹ�����)

	//��޼��������� ���� ó���ϱ�
	String cfm = "";		//����Ȯ�� ��û
	String sec = "";		//������� ��û
	String rsp = "";		//ȸ�ſ�� ��û

stop : {
	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//�����б�

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = sl.id; 		//������ login id

	//������ �̸��˾Ƴ���
	String[] naColumn = {"id","name","rank","office_tel"};
	bean.setTable("user_table");			
	bean.setColumns(naColumn);
	bean.setOrder("id ASC");	
	bean.setClear();
	bean.setSearch("id",id);				
	bean.init_unique();	
	if(bean.isAll()) name = bean.getData("name");

	// ���� Clear ��Ű��	
	wName=wDivision=wTel=Message="";
	psub=pwde=pwrn=prec=state=psel=path=pfie=pcon="";
	apath=pad1o=pad1f=pad2o=pad2f=pad3o=pad3f="";
	pid=TITLE=cfm=sec=rsp="";

	/*********************************************************************
	 	���޹��� ���� �б� (from post_main.jsp)
	*********************************************************************/
	String rPID = request.getParameter("PID");
	if(rPID != null) pid = rPID;

	TIT = request.getParameter("Title");
	if(TIT != null) {
		if(TIT.equals("REC_ING")) TITLE = "��������";
		else if(TIT.equals("SND_ING")) TITLE = "��������";
		else if(TIT.equals("TMP_ING")) TITLE = "��������";
		else if(TIT.equals("WST_ING")) TITLE = "������";
	}

	/**********************************************************************
		�߽��ڰ� �� ����
	**********************************************************************/
	String[] pidColumn = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","post_state","post_select","bon_path","bon_file","add_1_original","add_1_file","add_2_original","add_2_file","add_3_original","add_3_file"};
	if(TITLE.equals("������"))
		bean.setTable("POST_WASTE");
	else 
		bean.setTable("POST_MASTER");
	bean.setColumns(pidColumn);
	bean.setOrder("pid ASC");	
	bean.setSearch("pid",pid);			
	bean.init();

	String write_id = "";
	if(bean.isEmpty()) Message="NO_DATA";
	while(bean.isAll()) {	
		psub = bean.getData("post_subj");			//����
		pwde = bean.getData("write_date");			//����
		pwrn = bean.getData("writer_name");			//�ۼ��� �̸�

		prec = bean.getData("post_receiver");		//������ ���
		state = bean.getData("post_state");			//email ���� ���ο������� �Ǵ�
		psel = bean.getData("post_select");			//��޼���

		String Path = bean.getData("bon_path");		//����path
		if(Path == null) path = crp + "/";
		else {
			path = crp + Path.substring(0,Path.lastIndexOf('/'))+"/addfile";	//÷������ path
			Bpath = crp + Path;													//�������� path
		}

		pfie = bean.getData("bon_file");			//���� ���ϸ� (window)
		if(pfie == null) pfie = "";

		pad1o = bean.getData("add_1_original");		//÷�ε� ���ϸ�1 �����̸�
		pad1f = bean.getData("add_1_file");			//÷�ε� ���ϸ�1

		pad2o = bean.getData("add_2_original");		//÷�ε� ���ϸ�2 �����̸�
		pad2f = bean.getData("add_2_file");			//÷�ε� ���ϸ�2

		pad3o = bean.getData("add_3_original");		//÷�ε� ���ϸ�3 �����̸�
		pad3f = bean.getData("add_3_file");			//÷�ε� ���ϸ�3			

		//÷������ path���ϱ�
		int lslash = Path.lastIndexOf('/');
		if(lslash == -1) lslash = 0;
		apath = upload_path + crp + Path.substring(0,lslash) + "/addfile/";

		//÷������ ����ũ�� ���ϱ�
		String fpath1 = apath + pad1f;
		File fn1 = new File(fpath1);
		file1_size = Long.toString(fn1.length());	//÷��1 ����ũ��

		String fpath2 = apath + pad2f;
		File fn2 = new File(fpath2);
		file2_size = Long.toString(fn2.length());	//÷��2 ����ũ��

		String fpath3 = apath + pad3f;
		File fn3 = new File(fpath3);
		file3_size = Long.toString(fn3.length());	//÷��3 ����ũ��

		write_id = bean.getData("writer_id");		//�߽��� id
	} //while

	/***********************************************************************
		�߽��� ���� LIST UP
	***********************************************************************/
	String[] idColumn = {"a.id","a.name","b.ac_name","a.office_tel"};
	String q = "where a.ac_id = b.ac_id and id ='" + write_id + "'";
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setSearchWrite(q);				//id�� ��ϵ� ���븸�� ������ �� �ִ�.
	bean.init_write();

	if(bean.isEmpty()) wName = pwrn;			//email�ΰ�� �̸�
	while(bean.isAll()) {
		wName = bean.getData("name");			//�߽��ڸ�
		wDivision = bean.getData("ac_name");	//�߽��� �μ���
		wTel = bean.getData("office_tel");	//�߽��� ��ȭ��ȣ
	} //while

	/***********************************************************************
		�߽��� ���Ȯ�� ��û �м� (��, cFm,sEc,rsP,)
	***********************************************************************/
	cfm=sec=rsp="";
	if(psel != null) {
		if(psel.indexOf('F') > 0) cfm="CFM";		//����Ȯ�� ��û
		if(psel.indexOf('E') > 0) sec="SEC";		//���Ȯ�� ��û
		if(psel.indexOf('P') > 0) rsp="RSP";		//ȸ��Ȯ�� ��û
	}
 
	/**********************************************************************
		����Ȯ�ο�û�� ��� ����Ȯ�ο��� �˷��ֱ� (�߽��ڿ��Ը� ǥ������)
	***********************************************************************/
	if(cfm.equals("CFM") && write_id.equals(id)) {
		//out.println("������ : " + prec + "<br>");
		//�����μ� �ľ��Ͽ� ������(;)�� �̿��Ͽ� �迭�� ���
		String[] rec_name = new String[prec.length()];	//������ �̸�
		String[] rec_id = new String[prec.length()];	//������ id

		int rec_cnt = 0;				//�迭��ȣ
		int rec_str = 0;				//������(;) ��ġ (������ ������)
		for(int ri=0; ri<prec.length(); ri++){
			if(prec.charAt(ri) == ';') {
				String full_rec = prec.substring(rec_str,ri);				//������ Full�̸� (���/�̸�)
				int sidx = full_rec.indexOf('/'); 							//������ ��ġ ã��(/)
				if(sidx > 0) {
					rec_id[rec_cnt] = full_rec.substring(0,sidx);			//�����
					rec_name[rec_cnt] = full_rec.substring(sidx+1,full_rec.length());		//�̸���
				}
				rec_cnt++;									//�迭��ȣ����
				rec_str = ri+3;								//������ (\r\n���� 2���� + ;(1))
			}
		}


		//������ �̿� ���Ҵ��� ���θ� �˾ƺ���, ���������Կ� "����Ȯ��" ǥ���ϱ�
		prec = "";	//�� �������� Ŭ�����Ű�� �ϴܿ��� �ٽ� �����Ѵ�.
		String[] seeColumn = {"pid","post_receiver","isopen","open_date"};
		bean.setTable("POST_LETTER");			
		bean.setColumns(seeColumn);
		bean.setOrder("pid ASC");	
		bean.setClear();
		for(int rj=0; rj < rec_cnt; rj++){
			bean.setSearch("pid",pid,"post_receiver",rec_id[rj]);				
			bean.init_unique();
			String isRead = "0";
			if(bean.isAll()) isRead = bean.getData("isopen");
			
			//�������� �����ϱ�
			if(isRead.equals("1")) { //������
				prec += rec_name[rj] + "/" + rec_id[rj] + ";" + "������\n";	//bean.getData("open_date")
			} else {
				prec += rec_name[rj] + "/" + rec_id[rj] + ";\n";
			}	
		} //for
	} //if

	/**********************************************************************
		���ù����� ó���������� Ȯ���Ͽ� �ϴ��� ó����
	***********************************************************************/
	String[] letColumn = {"pid","post_receiver","isopen"};
	bean.setTable("POST_LETTER");			
	bean.setColumns(letColumn);
	bean.setOrder("pid ASC");	
	bean.setClear();
	bean.setSearch("pid",pid,"post_receiver",id);				
	bean.init_unique();
	
	String ISOPEN = "";
	if(bean.isAll()) ISOPEN = bean.getData("isopen");
	if(ISOPEN == null) ISOPEN = "1";

	if(ISOPEN.equals("0")) {		
		//---------------------------------------------
		//������,open����,������������ �˷��ֱ�
		//----------------------------------------------
		int DelM = 1;		//���������� open�� 1����
		String seeL = "update POST_LETTER set isopen='1',open_date='" + anbdt.getTime() + "',delete_date='" + anbdt.getAddMonthNoformat(DelM) + "' where pid='" + pid + "' and post_receiver='" + id + "'";
		try { bean.execute(seeL); } catch (Exception e) {}
		//out.println("seeL : " + seeL + "<br>");
	} //if

} //stop

%>

<HTML><HEAD><TITLE>�����б�</TITLE>
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
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_mail_r.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

    <!--��ư-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
       <tbody>
         <tr><td height=20 colspan="4"></td></tr>
		 <tr><td height=25 colspan="4" align="right">
 		  <% if(TITLE.equals("��������")) { %>
          <a href="Javascript:reply('<%=pid%>');"><img border="0" src="../images/bt_reply.gif" align='absmiddle'></a>
		  <% } %>
		 <a href="javascript:dclose();"><img src="../images/close.gif" border="0" align='absmiddle'></a></td></tr></tbody></table>

    <!--�߼�����-->
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody>
         <tr bgcolor=#60A3EC><td height="2" colspan="4" ></td></tr>
         <tr>
		   <td height=22 colspan="4">&nbsp;</td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">�߽���</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=wName%> [��ȭ: <%=wTel%>,  �μ���:<%=wDivision%>,  �ۼ���:<%=pwde%>]</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">������</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><TEXTAREA NAME="rec_name" rows=1 cols=75 readOnly><%=prec%></TEXTAREA></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02"><%=psub%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<% 
					//state.equals("email")  //email ������			
					//Textfile �б�
					textFileReader text = new com.anbtech.file.textFileReader();			//�����б�
					String buf = "";
					if(!Message.equals("NO_DATA")) {
						String Textfile = upload_path + Bpath + "/" + pfie;
						buf = text.getFileString(Textfile);
					} else buf = "�����Ͱ� �����ϴ�.";

					//ȭ������ϱ�
					if(state.equals("email")) {
						if(buf.indexOf("><") == -1) {
							out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//���� ����
						} else {
							out.println("<IFRAME name=show src='"+server_path+"/upload"+Bpath+"/"+pfie+"' width=100% height=500 border=0 frameborder=0></IFRAME>"); 
						}
					} else if(state.equals("DEL")) {
						if(buf.indexOf("><") == -1) {
							out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//���� ����
						} else {
							out.println("<IFRAME name=show src='"+server_path+"/upload"+Bpath+"/"+pfie+"' width=100% height=500 border=0 frameborder=0></IFRAME>"); 
						}
					} else {
						out.println("<TEXTAREA rows=22 cols=75 readOnly>" + buf + "</TEXTAREA>");		//���� ����
					}
					
			%></td></tr>
         <tr bgcolor="C7C7C7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="13%" height="25" class="bg_01" background="../images/bg-01.gif">÷������</td>
           <td width="87%" height="25" colspan="3" class="bg_02">
			<img src="../images/b-attach.gif" border="0">÷������1:<a href='post_downloadp.jsp?fname=<%=pad1o%>&fsize=<%=file1_size%>&umask=<%=pad1f%>&extend=<%=path%>'><%=pad1o%></a><br>
			<img src="../images/b-attach.gif" border="0">÷������2:<a href='post_downloadp.jsp?fname=<%=pad2o%>&fsize=<%=file2_size%>&umask=<%=pad2f%>&extend=<%=path%>'><%=pad2o%></a><br>
			<img src="../images/b-attach.gif" border="0">÷������3:<a href='post_downloadp.jsp?fname=<%=pad3o%>&fsize=<%=file3_size%>&umask=<%=pad3f%>&extend=<%=path%>'><%=pad3o%></a>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8"><a href="javascript:dclose();"><img src="../images/close.gif"  hspace="10" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<script>
<!--
function centerWindow() 
{ 
        var sampleWidth = 700;                        // �������� ���� ������ ���� 
        var sampleHeight = 650;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function dclose()
{
	opener.parent.Left.location.reload();
	self.close();
}

//Reply
function reply(a)
{
	document.location.href="post_reply.jsp?PID=" + a;
}
-->
</script>

<!-- ****************** �޽��� ���޺κ� ****************************** -->
<% if(Message == "NO_CONTENT") { %>
<script>
alert("������ �������ϴ�.")
</script>
<% Message = "" ; } %>

<% if(Message == "INSERT") { %>
<script>
alert("��� �Ǿ����ϴ�.")
opener.location.reload();
close()
</script>
<% Message = "" ; } %>

<% if(Message == "QUERY") { %>
<script>
alert("���ۿ� �����߽��ϴ�. �ٽ� �����Ͻʽÿ�.")
</script>
<% Message = "" ; } %>

<% if(Message == "REPLY") { %>
<script>
alert("�߽���:<%=wName%>[<%=wDivision%>] ���� ȸ���� ��û�ϼ̽��ϴ�.")
</script>
<% Message = "" ; } %>
