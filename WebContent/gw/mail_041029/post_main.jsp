<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���θ��� ����"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.lang.SecurityException"
	import="java.io.UnsupportedEncodingException"
%>
<%@	page import="com.anbtech.text.Hanguel"			%>
<%@	page import="com.anbtech.date.anbDate"			%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<%@	page import="com.anbtech.file.textFileReader"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

	
<%
	//�������� ��¼� ����
	bean.setRowNo(15);					//���������� 20���� ���

	//�޽��� ���޺���
	String Message="";					//�޽��� ���� ����  

	String id = "";						//������ id
	String passwd="";					//������ ��й�ȣ
	String name = "";					//������ �̸�
	String division = "";				//������ �μ���	
	String PROCESS = "REC_ING";			//���������� ����ޱ�
	String PROCESS_NAME = "��������";		//���������� �̸�����
	
	//������DB Columns
	String[] masterColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};

	//������ DB Columns
	String[] wasteColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};
	
	//LETTER DB Columns
	String[] letterColumns= {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_select","delete_date"};

	//������ LINEã��
	int count = 0;			//line ��ȣ
	String ALL_SEL = "";		//��ü�����ϱ� 
	String ALL_CHG = "";		//��ü�����ϱ� ������ �ٲ��ֱ�

	//��޼��������� ���� ó���ϱ�
	String cfm = "";		//����Ȯ�� ��û
	String sec = "";		//������� ��û
	String rsp = "";		//ȸ�ſ�� ��û

	String sendConfirm="";		//���Ȯ�� �����Ͽ� ������������ �����ֱ�

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//���� ����

	/*********************************************************************
	 	������ login �˾ƺ���
	*********************************************************************/
	id = login_id; 		//������ login id

	String[] idColumn = {"a.id","a.passwd","a.name","b.ac_name"};
	bean.setTable("user_table a,class_table b");			//EBOM Master Table List
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	

	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	bean.isAll();
	name = bean.getData("name");			//������ ��
	division = bean.getData("ac_name");	//������ �μ���
	passwd = bean.getData("passwd");		//������ ��й�ȣ

	/********************************************************************
		������ ���� ��ü ���ÿ��ξ˾ƺ���
	*********************************************************************/
	ALL_SEL = request.getParameter("SEL");
	ALL_CHG = request.getParameter("CHG");	if(ALL_CHG == null) ALL_CHG = "";

	if(ALL_SEL == null) {
		 ALL_SEL = ALL_CHG = "";
	} else {
		//��ü���� ���������� ���� �ٲ��ֱ�
		if(ALL_CHG.equals(ALL_SEL)) { 
			ALL_SEL = "";
			ALL_CHG = "";
		}
		else ALL_CHG = ALL_SEL; 	
	}

	/********************************************************************
		PROCESS ���� ���ϱ�
	*********************************************************************/
	PROCESS = request.getParameter("ORDER");
	if(PROCESS != null) {
			if(PROCESS.equals("REC_ING")) PROCESS_NAME = "��������";
			else if(PROCESS.equals("SND_ING")) PROCESS_NAME = "��������";
			else if(PROCESS.equals("TMP_ING")) PROCESS_NAME = "��������";
			else if(PROCESS.equals("WST_ING")) PROCESS_NAME = "��������";
			else PROCESS_NAME = "��������";
	} //if
	else { 
			PROCESS = "REC_ING";
			PROCESS_NAME = "��������";
	}


	/********************************************************************
		������ ���� �����ϱ�
	*********************************************************************/
	String DEL_MSG = request.getParameter("DEL");
	Message="";
	if((DEL_MSG != null) && (DEL_MSG.length() > 0)) {
		for(int i=0; i<15; i++) {
			String check = "check" + i;
			String PID = request.getParameter(check);
			//���� �Ǵ� ������ 
			if(PROCESS.equals("WST_ING") && (PID != null)) { //�����뿡�� --> ���������ϱ�
				//POST_LETTER �� POST_WASTE�� �ش�PID�� ���°�쿡 ���ؼ� 
				//POST_LETTER �� POST_MASTER table,����,÷������ ���������ϰ�
				//�׷��� ���� ���� POST_WASTE table���� �ش緹�ڵ常 ������.(�ٸ������ڿ� link�Ǿ�����)
				String letter_flag = "NONE";	//LETTER table NONE: ���� �Ұ�, OK: ��������	
				String master_flag = "NONE";	//MASTER table NONE: ���� �Ұ�, OK: ��������
				String waste_flag = "NONE";		//WASTE  table NONE: ���� �Ұ�, OK: ��������

				//����1 POST_LETTER�� �ش� PID�� �ִ��� ������ �˻�
				String[] lCol = {"pid"};
				bean.setTable("POST_LETTER");
				bean.setColumns(lCol);
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID);
				bean.init_unique();
				if(bean.isEmpty())  letter_flag = "OK"; 
				else letter_flag = "NONE"; 

				//����2 POST_MASTER�� �ش� PID�� post_state='DEL' ���� �˻�
				String[] mCol ={"pid","post_state"};
				bean.setTable("POST_MASTER");
				bean.setColumns(mCol);	
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID,"post_state","DEL");
				bean.init_unique();
				if(bean.isEmpty())  master_flag = "NONE"; 
				else master_flag = "OK";
				
				//����3 POST_WASTE�� �ش� PID�� 1��(�ڱ��ڽ�) �ִ��� �˻�
				String[] wCol = {"pid"};
				bean.setTable("POST_WASTE");
				bean.setColumns(wCol);
				bean.setOrder("pid DESC");
				bean.setClear();
				bean.setSearch("pid",PID);
				bean.init_unique();
				if(bean.isEmpty())  waste_flag = "OK"; 
				else {
					if(bean.getTotalCount() == 1)
						waste_flag = "OK";
					else waste_flag = "NONE";
				}

				//����1,2,3 �Ǵ��Ͽ� ������ ÷������ ���� ����
				if(letter_flag.equals("OK") && master_flag.equals("OK") && waste_flag.equals("OK")) {
					bean.setTable("POST_WASTE");
					bean.setColumns(wasteColumns);
					bean.setOrder("pid DESC");
					bean.setClear();
					bean.setSearch("pid",PID);
					bean.init_unique();
				
					if(bean.isAll()) {
						//��������
						String bPath = bean.getData("bon_path");
						String bFile = bean.getData("bon_file");
						String bFileDEL = upload_path + crp + bPath + "/" + bFile;					
						text.delFilename(bFileDEL);			//�������� 
						Message="DELETE";
						//÷������ ����
						int lastI = bPath.lastIndexOf('/');

						if(lastI != -1) {
							String aPath = upload_path + crp + bPath.substring(0,lastI) + "/addfile/";
							String a1_File = bean.getData("add_1_file");
							String a2_File = bean.getData("add_2_file");
							String a3_File = bean.getData("add_3_file");
							String a1_FileDEL = aPath + a1_File;
							String a2_FileDEL = aPath + a2_File;
							String a3_FileDEL = aPath + a3_File;
							text.delFilename(a1_FileDEL);				//÷��1 ����
							text.delFilename(a2_FileDEL);				//÷��2 ���� 
							text.delFilename(a3_FileDEL);				//÷��3 ����
						} //if
					 } //if ����,÷������ ��������
					 //POST_MASTER ���̺��� �ش緹�ڵ� �����ϱ�
					 String mstDEL = "delete from POST_MASTER where pid='" + PID + "' and post_state='DEL'";
					 try { bean.execute(mstDEL); } catch (Exception e) { }
				}//����1,2,3 �Ǵ��Ͽ� ������ ÷������ ���� ����
				
				//POST_WASTE table ����
				String wstDEL = "delete from POST_WASTE where pid='" + PID + "' and post_receiver='" + id + "'";     //�����ϱ� (post_waste)
				try { bean.execute(wstDEL); Message="DELETE"; } catch (Exception e) { }
			} else {			//�����Կ��� --> ���������� ������
				if(PROCESS.equals("REC_ING") && (PID != null)) { 	//��������
					String RwstIN = "insert into post_waste(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) select pid,post_subj,writer_id,writer_name,write_date,'"+id+"',isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date from post_master where pid='"+PID+"'";
					String RletDEL = "delete from POST_LETTER where pid='"+PID+"' and post_receiver='" + id + "'";	//�ش���� ����

					//�ܺθ����� ���
					String RmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "' and post_state='email'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(RwstIN); 
						  bean.execute(RmstDEL);  //�ܺθ����� ��츸 �����
						  bean.execute(RletDEL); Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}

				} else if(PROCESS.equals("SND_ING") && (PID != null)) {	//�������� (������ �������� �Ҷ�)
					String SwstIN = "insert into post_waste(pid,post_subj,writer_id,writer_name,write_date,post_receiver,isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date) select pid,post_subj,writer_id,writer_name,write_date,'"+id+"',isopen,open_date,post_state,post_select,bon_path,bon_file,add_1_original,add_1_file,add_2_original,add_2_file,add_3_original,add_3_file,delete_date from post_master where pid='"+PID+"'";
					String SmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(SwstIN); 
						  bean.execute(SmstDEL); 
						  Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}	
				} else if(PROCESS.equals("TMP_ING") && (PID != null)) {	//��������
					String TwstIN = "insert into POST_WASTE select * from POST_MASTER where pid = '" + PID + "'";	//�������״�� ���
					String TwstUP = "update POST_WASTE set post_receiver='" + id + "' where pid = '" + PID + "'";	//������ ID UPdate
					String TmstDEL = "update POST_MASTER set post_state='DEL' where pid = '" + PID + "'";
					bean.setAutoCommit(false);
					bean.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
					try { bean.execute(TwstIN); 
						  bean.execute(TwstUP); 
						  bean.execute(TmstDEL); 
						  Message="DELETE";
						  bean.commit();
					} catch (Exception e) {
						  bean.rollback();
					} finally {bean.setAutoCommit(true);}
				} //if
			}
		} //for
	} //if
	/********************************************************************
		 �������� ��û���� �˾ƺ���
	*********************************************************************/
	//������ ������ �̿��Ͽ� PROCESS�� �˾ƺ���.
	//���ο� �� �������� ���� �Ǵ� ���� ������ ��������
	String pageNo = request.getParameter("pageNo");

	int ipage = 1;				
	if(pageNo!=null) ipage = Integer.parseInt(pageNo);	
	/********************************************************************
		 ���ο��� ���� �����ϱ�
	*********************************************************************/
	if((PROCESS == null) || (PROCESS.equals("REC_ING"))) {
		/***********************************************************************
		����������
		***********************************************************************/	
		bean.setTable("POST_LETTER");
		bean.setColumns(letterColumns);
		bean.setOrder("write_date DESC");
		bean.setClear();
		bean.setSearch("post_receiver",id);
		bean.setPage(ipage);	
		bean.init_unique();
	} 
	else if (PROCESS.equals("SND_ING")) {
		/***********************************************************************
		����������
		***********************************************************************/	
		bean.setTable("POST_MASTER");
		bean.setColumns(masterColumns);
		//bean.setOrder("pid DESC");
		//bean.setClear();
		//bean.setSearch("post_state","SND","writer_id",id);
		//bean.setPage(ipage);	
		//bean.init_unique();
		String query = " where (post_state='SND' or post_state='email') and writer_id='"+id+"' ";
			query += "order by pid desc";
		bean.setSearchWrite(query);
		bean.setPage(ipage);
		bean.init_write();
	}
	else if (PROCESS.equals("TMP_ING")) {
		/***********************************************************************
		����������
		***********************************************************************/	
		bean.setTable("POST_MASTER");
		bean.setColumns(masterColumns);
		bean.setOrder("pid DESC");
		bean.setClear();
		bean.setSearch("post_state","TMP","writer_id",id);
		bean.setPage(ipage);	
		bean.init_unique();
	}
	else if (PROCESS.equals("WST_ING")) {
		/***********************************************************************
		������
		***********************************************************************/	
		bean.setTable("POST_WASTE");
		bean.setColumns(wasteColumns);
		bean.setOrder("pid DESC");
		bean.setClear();
		bean.setSearch("post_receiver",id);
		bean.setPage(ipage);	
		bean.init_unique();		

	}

%>

<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../images/blet.gif" align="absmiddle">
			<%	
				if(PROCESS.equals("REC_ING")) out.println("���� ������");
				else if(PROCESS.equals("SND_ING")) out.println("���� ������");
				else if(PROCESS.equals("TMP_ING")) out.println("���� ������");
				else if(PROCESS.equals("WST_ING")) out.println("���� ������");
			%>						  
			  </TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=bean.getCurrentPage()%>/<%=bean.getLastPage()%> <img src="../images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='300'>
				<% if(request.getParameter("ORDER") == null) { 	//���� �޴��Է½� null����%>
						<a href="javascript:wopen('post_write.jsp','post_write','700','650');">
							<img src="../images/bt_new_letter.gif" border="0" align="absmiddle"></a>
						<a href='post_main.jsp?ORDER=REC_ING&SEL=ALL&DEL=&CHG=&pageNo=<%=ipage%>'>
							<img src="../images/bt_sel_all.gif" border="0" align="absmiddle"></a>
						<a href="javascript:postSelectDel();">
							<img src="../images/bt_del.gif" border="0" align="absmiddle"></a>
				<% } else { %>
						<a href="javascript:wopen('post_write.jsp','post_write','700','650');">
							<img src="../images/bt_new_letter.gif" border="0" align="absmiddle"></a>
						<a href='post_main.jsp?ORDER=<%=request.getParameter("ORDER")%>&SEL=ALL&DEL=&CHG=<%=ALL_CHG%>&pageNo=<%=ipage%>'>
							<img src="../images/bt_sel_all.gif" border="0" align="absmiddle"></a>
						<a href="javascript:postSelectDel();">
							<img src="../images/bt_del.gif" border="0" align="absmiddle"></a>
				<% } %></TD>
			  <TD width='' align='right' style="padding-right:10px">
				<%	if (bean.getCurrentPage() <= 1) {	%>
					<img src='../images/bt_previous.gif' border='0' align="absmiddle">
				<%	} else 	{	%>
					<a href='javascript:goPage(<%=bean.getCurrentPage()-1%>)'><img src='../images/bt_previous.gif' border='0' align="absmiddle"></a>
				<%	} if ((bean.getCurrentPage() != bean.getLastPage()) && (bean.getLastPage() != -1 )) { %>
					<a href='javascript:goPage(<%=bean.getCurrentPage()+1%>)'><img src='../images/bt_next.gif' border='0' align="absmiddle"></a>
				<%	} else 	{  %> 
					<img src='../images/bt_next.gif' border='0' align="absmiddle">
				<%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--����Ʈ-->
  <TR height=100%><form method="post" name="dForm" action="post_main.jsp" style="margin:0">
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�߽���</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>�߽���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=9></TD></TR>
<% 
	count=0;
	while(bean.isAvailable()) {	

		/***********************************************************************
		�߽��� ���Ȯ�� ��û �м� (��, cFm,sEc,rsP,)
		***********************************************************************/
		String psel = bean.getData("post_select");

		cfm=sec=rsp=sendConfirm="";
		if((psel != null) && (psel.length() > 0)) {
			sendConfirm="[";
			if(psel.indexOf('F') > 0) cfm="����";		//����Ȯ�� ��û
			if(psel.indexOf('E') > 0) sec="���";		//���Ȯ�� ��û
			if(psel.indexOf('P') > 0) rsp="���";		//���Ȯ�� ��û
			sendConfirm += cfm + " " + sec + " " + rsp + "]";		
		}
		if(sendConfirm.length() == 4) sendConfirm = "";	//�ɼǻ��� ����
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><input type="checkbox" name="check<%=count%>" value='<%=bean.getData("pid")%>' <% if(ALL_SEL.equals("ALL")) out.print("CHECKED"); %>></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'>
			<% 	//����ô��� ����
				String isSee = bean.getData("isopen");
				if(isSee != null) {
					if(PROCESS.equals("REC_ING")) {			//��������
						if(isSee.equals("0")) { 				//��Ȯ��
							out.println("<img src='../images/new_letter.gif' align='middle' border='0'>"); 
						} else {						//Ȯ��
							out.println("&nbsp;");
						}
					} else if(PROCESS.equals("SND_ING")) {		//��������
						out.println("&nbsp;"); 
					} else if(PROCESS.equals("TMP_ING")) {		//��������
						out.println("&nbsp;"); 
					} else if(PROCESS.equals("WST_ING")) {		//������
						out.println("&nbsp;"); 
					} //if
				} //if
			%>			  
			  </td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:3px">
			<% if((sec.length() > 0) && (PROCESS.equals("REC_ING"))){ 	//��й���, ���������� ���� %>
				<a href="javascript:wopen('post_viewPass.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','300','145');" >&nbsp;<%=bean.getData("post_subj")%></a>
			<% } else if(PROCESS.equals("SND_ING")) { 			//�������� ���� (��޼��û��� �����ֱ�)%>
				<a href="javascript:wopen('post_view.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','700','650');"><%=sendConfirm%>&nbsp;
				<%=bean.getData("post_subj")%></a>
			<% } else if(PROCESS.equals("TMP_ING")) { //���������� ��޼��û��� �����ְ� �ٽ� �ۼ��޴���  %>
				<a href="javascript:wopen('post_write.jsp?INI=&PID=<%=bean.getData("pid")%>','post_decision','700','650');"><%=sendConfirm%>&nbsp;
				<%=bean.getData("post_subj")%></a>	
			<% } else  { 							//���� �����ֱ� %>
				<a href="javascript:wopen('post_view.jsp?PID=<%=bean.getData("pid")%>&Title=<%=PROCESS%>','post_decision','700','650');">&nbsp;
				<%=bean.getData("post_subj")%></a>
			<% } %>						  
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
				<%
					String wname = bean.getData("writer_name");
					if(wname.length() > 20) out.println(wname.substring(0,20));
					else out.println(wname);
				%>			  
			  </TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("write_date")%></TD></TR>
			<TR><TD colSpan=9 background="../images/dot_line.gif"></TD></TR>
<% 
	count++;
	}  //while 
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>

<input type="hidden" name="ORDER" value="<%=PROCESS%>">
<input type="hidden" name="DEL" value="OK">
<input type="hidden" name="SEL" value="">
<input type="hidden" name="pageNo" value="<%=ipage%>">
</form>

<form name = "eForm" method = "post">
	<input type="hidden" name="pageNo" value="">
	<input type="hidden" name="ORDER" value="<%=PROCESS%>">
</form>
</body>
</html>

<script language=javascript>
<!--
var m = '<%=Message%>';
if(m.length > 0) parent.Left.location.reload();

function goPage(pageNo)
{
	document.eForm.action = "post_main.jsp";
	document.eForm.pageNo.value = pageNo;
	document.eForm.submit();
}

function postSelectDel()
{
	var sel = confirm('������ �����Ͻðڽ��ϱ�?');
	if(sel == false) return;

	document.dForm.action="post_main.jsp";
	document.dForm.ORDER.values='<%=PROCESS%>';
	document.dForm.DEL.values='OK';
	document.dForm.SEL.values='';
	document.dForm.submit();

}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=yes,toolbar=no,status=no,resizable=yes');
}
-->
</script>