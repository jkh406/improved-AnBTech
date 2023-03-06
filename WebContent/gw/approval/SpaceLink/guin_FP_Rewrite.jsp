<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "�����Ƿڼ� ���ۼ�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"

	import="java.util.StringTokenizer"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.text.*"

	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	textFileReader text = new com.anbtech.file.textFileReader();			//�������� �б�
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	//��ȼ� �������
	String query = "";
	String writer_id = "";			//�����(�븮����ϼ��� ����) ���
	String writer_name = "";		//�����(�븮����ϼ��� ����) �̸�

	String user_name = "";			//�ش��� ��
	String rank_code = "";			//�ش��� ����code
	String user_rank = "";			//�ش��� ����
	String div_id = "";				//�ش��� �μ��� �����ڵ�
	String div_name = "";			//�ش��� �μ���
	String div_code = "";			//�ش��� �μ��ڵ�

	int work_cnt = 22;				//����ٹ���û �ۼ� �÷���
	
	/*********************************************************************
	 	�����(login) �˾ƺ��� : ���ڰ��� ����� ���� [����]
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//����� ���
		writer_name = bean.getData("name");				//����� ��
	} //while


	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
	*********************************************************************/	
	String user_id = multi.getParameter("user_id"); if(user_id == null) user_id = login_id;
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//�ش��� ��
		rank_code = bean.getData("ar_code");			//�ش��� ���� code
		user_rank = bean.getData("ar_name");			//�ش��� ����
		div_id = bean.getData("ac_id");					//�ش��� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//�ش��� �μ��� 
		div_code = bean.getData("ac_code");				//�ش��� �μ��ڵ�
	} //while

	/*********************************************************************
	 	view.jsp�κ��� ���� ÷������ ����
	*********************************************************************/	
	String doc_id = multi.getParameter("doc_id");
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//���ʷ� �Ѱܹ�������
	
	//�������� �б�
	String job_kind = "";			//��������
	String job_content = "";		//��������
	String career = "";				//�з�
	String major = "";				//����
	String req_qualify = "";		//�ʿ��ڰ���
	String status = "";				//�Ի�����
	String job_career = "";			//�䱸���
	String job_etc = "";			//�䱸��� ��Ÿ
	String req_count = "";			//�����ο�
	String marray = "";				//ȥ��
	String army = "";				//����
	String employ = "";				//�������
	String employ_per = "";			//������� ����� �Ⱓ
	String language_grade = "";		//�ܱ���
	String language_exam = "";		//���ν���
	String language_score = "";		//���/����
	String comp_grade = "";			//����ɷ�
	String comp_etc = "";			//����ɷ� ��Ÿ
	String papers = "";				//���⼭��
	String note = "";				//��Ÿ �ʿ����
	String[] gColumn = {"job_kind","job_content","career","major","req_qualify","status","job_career",
						"job_etc","req_count","marray","army","employ","employ_per","language_grade",
						"language_exam","language_score","comp_grade","comp_etc","papers","note"};
	bean.setTable("insa_master");			
	bean.setColumns(gColumn);
	bean.setOrder("ac_name ASC");	
	query = "where (is_id ='"+old_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		job_kind = bean.getData("job_kind");		//��������
		job_content = bean.getData("job_content");	//��������
		career = bean.getData("career");			//�з�
		major = bean.getData("major");				//����
		req_qualify = bean.getData("req_qualify");	//�ʿ��ڰ���
		status = bean.getData("status");			//�Ի�����
		job_career = bean.getData("job_career");	//�䱸���
		job_etc = bean.getData("job_etc");			//�䱸��� ��Ÿ
		req_count = bean.getData("req_count");		//�����ο�
		marray = bean.getData("marray");			//ȥ��
		army = bean.getData("army");				//����
		employ = bean.getData("employ");			//�������
		employ_per = bean.getData("employ_per");	//������� ����� �Ⱓ
		language_grade = bean.getData("language_grade");	//�ܱ���
		language_exam = bean.getData("language_exam");		//���ν���
		language_score = bean.getData("language_score");	//���/����
		comp_grade = bean.getData("comp_grade");			//����ɷ�
		comp_etc = bean.getData("comp_etc");		//����ɷ� ��Ÿ
		papers = bean.getData("papers");			//���⼭��
		note = bean.getData("note");				//��Ÿ �ʿ����
	} //while
	
	/*********************************************************************
	 	���� ������ ó��
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";		//���缱	
%>
<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="guin_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> �����Ƿڼ�</TD></TR></TBODY>
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
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">��<br>��<br>��</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
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

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">������ȣ</td>
           <td width="37%" height="25" class="bg_04">����Ϸ��� �ڵ�ä��</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ۼ�����</td>
           <td width="37%" height="25" class="bg_04"><%=anbdt.getYear()%>�� <%=anbdt.getMonth()%>�� <%=anbdt.getDates()%>��</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="job_kind" rows=2 cols=60><%=job_kind%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��������</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="job_content" rows=2 cols=60><%=job_content%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�з�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=17 name="career" value='<%=career%>'></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=30 name="major" value='<%=major%>'></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ʿ��ڰ���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" size=60 name="req_qualify" value='<%=req_qualify%>'></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�Ի�����</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] sel_list = {"����","���","����"};
			int sel_cnt = sel_list.length;
			//������ ��
			status = StringProcess.repWord(status,":"," :");
			StringTokenizer status_list = new StringTokenizer(status,":");
			String[] sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			int c = 0;
			while(status_list.hasMoreTokens()) {
				sts[c] = status_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(sel_list[i])) 
					 out.println("<input type='checkbox' checked name='status"+k+"' value='"+sel_list[i]+"'>"+sel_list[i]);
				else out.println("<input type='checkbox' name='status"+k+"' value='"+sel_list[i]+"'>"+sel_list[i]);
			}
		%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�����ο�</td>
           <td width="37%" height="25" class="bg_04"><input type="text" size=3 name="req_count" value='<%=req_count%>'> ��</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�䱸���</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type="text" size=5 name="job_career" value='<%=job_career%>'>���̻� (��Ÿ:<input type="text" size=20 name="job_etc" value=''>)</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�������</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] em_list = {"������","�����","�ð���","�İ߱ٷ�"};
			sel_cnt = em_list.length;
			//������ ��
			employ = StringProcess.repWord(employ,":"," :");
			StringTokenizer employ_list = new StringTokenizer(employ,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(employ_list.hasMoreTokens()) {
				sts[c] = employ_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(em_list[i])) {
					if(em_list[i].equals("�����")) {
						out.println("<input type='checkbox' checked name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
						out.println("(�Ⱓ <input type='text' size=5 name='employ_per' value='"+employ_per+"'> ����)");
					} else {
						 out.println("<input type='checkbox' checked name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
					}
				} else { 
					if(em_list[i].equals("�����")) {
						out.println("<input type='checkbox' name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
						out.println("(�Ⱓ <input type='text' size=5 name='employ_per' value='"+employ_per+"'> ����)");
					} else {
						out.println("<input type='checkbox' name='employ"+k+"' value='"+em_list[i]+"'>"+em_list[i]);
					}
				}
			}

		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">ȥ�ο���</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] mry_list = {"��ȥ","��ȥ","����"};
			sel_cnt = mry_list.length;
			//������ ��
			marray = StringProcess.repWord(marray,":"," :");
			StringTokenizer marray_list = new StringTokenizer(marray,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(marray_list.hasMoreTokens()) {
				sts[c] = marray_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(mry_list[i])) 
					 out.println("<input type='checkbox' checked name='marray"+k+"' value='"+mry_list[i]+"'>"+mry_list[i]);
				else out.println("<input type='checkbox' name='marray"+k+"' value='"+mry_list[i]+"'>"+mry_list[i]);
			}

		%>
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����</td>
           <td width="37%" height="25" class="bg_04">
		<%
			String[] am_list = {"��","����"};
			sel_cnt = am_list.length;
			//������ ��
			army = StringProcess.repWord(army,":"," :");
			StringTokenizer army_list = new StringTokenizer(army,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(army_list.hasMoreTokens()) {
				sts[c] = army_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(am_list[i])) 
					 out.println("<input type='checkbox' checked name='army"+k+"' value='"+am_list[i]+"'>"+am_list[i]);
				else out.println("<input type='checkbox' name='army"+k+"' value='"+am_list[i]+"'>"+am_list[i]);
			}

		%>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">�ܱ���</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] eg_list = {"��","��","��"};
			sel_cnt = eg_list.length;
			//������ ��
			language_grade = StringProcess.repWord(language_grade,":"," :");
			StringTokenizer language_grade_list = new StringTokenizer(language_grade,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(language_grade_list.hasMoreTokens()) {
				sts[c] = language_grade_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(eg_list[i])) 
					 out.println("<input type='checkbox' checked name='language_grade"+k+"' value='"+eg_list[i]+"'>"+eg_list[i]);
				else out.println("<input type='checkbox' name='language_grade"+k+"' value='"+eg_list[i]+"'>"+eg_list[i]);
			}

		%>)&nbsp;
			(���ν���:<input type="text" size=10 name="language_exam" value='<%=language_exam%>'>, 
			���/����:<input type="text" size=10 name="language_score" value='<%=language_score%>'> )
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">����ɷ�</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] cg_list = {"�����ۼ�","����","���������̼�","���ͳ�","Ȩ��������"};
			sel_cnt = cg_list.length;
			//������ ��
			comp_grade = StringProcess.repWord(comp_grade,":"," :");
			StringTokenizer comp_grade_list = new StringTokenizer(comp_grade,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(comp_grade_list.hasMoreTokens()) {
				sts[c] = comp_grade_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(cg_list[i])) 
					 out.println("<input type='checkbox' checked name='comp_grade"+k+"' value='"+cg_list[i]+"'>"+cg_list[i]);
				else out.println("<input type='checkbox' name='comp_grade"+k+"' value='"+cg_list[i]+"'>"+cg_list[i]);
			}

		%>
		��Ÿ( <input type="text" size=10 name="comp_etc" value='<%=comp_etc%>'> )
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">���⼭��</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
		<%
			String[] ps_list = {"�̷¼�","�ڱ�Ұ���","��������","��������","�������","�ڰ����纻"};
			sel_cnt = ps_list.length;
			//������ ��
			papers = StringProcess.repWord(papers,":"," :");
			StringTokenizer papers_list = new StringTokenizer(papers,":");
			sts = new String[sel_cnt];
			for(int i=0; i<sel_cnt; i++) sts[i] = "";

			c = 0;
			while(papers_list.hasMoreTokens()) {
				sts[c] = papers_list.nextToken();
				sts[c] = sts[c].trim();
				c++;
			}
			//ȭ�����
			for(int i=0,k=1; i<sel_cnt; i++,k++) {
				if(sts[i].equals(ps_list[i])) 
					 out.println("<input type='checkbox' checked name='papers"+k+"' value='"+ps_list[i]+"'>"+ps_list[i]);
				else out.println("<input type='checkbox' name='papers"+k+"' value='"+ps_list[i]+"'>"+ps_list[i]);
			}

		%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">��Ÿ�䱸����</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><TEXTAREA NAME="note" rows=3 cols=60><%=note%></TEXTAREA></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>


         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_name" value='<%=user_name%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>

<% //���긴 ������ ó�� %>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='work_cnt' value='<%=work_cnt%>'>
<input type='hidden' name='doc_sub' value='�����Ƿڼ�'>
<input type='hidden' name='doc_per' value='100'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='upload_path' value='<%=upload_path%>'>
</form>  
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

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//���缱 �������� 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
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

	document.onmousedown=dbclick;// ����Ŭ�� check

	//�ϰ����� ����������
	document.eForm.action='../../../servlet/InSaServlet';
	document.eForm.mode.value='R_GUIN';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();
}

//���� �ӽú���
function eleApprovalTemp()
{
	document.onmousedown=dbclick;// ����Ŭ�� check

	document.eForm.action="../../../servlet/InSaServlet";
	document.eForm.mode.value='R_GUIN_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.submit();
}

// ����Ŭ�� ����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>
