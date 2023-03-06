<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	info		= ""		
	contentType = "text/html; charset=euc-kr"
	import		= "com.anbtech.text.Hanguel,java.io.*,java.util.*"
	import		= "com.oreilly.servlet.MultipartRequest"
	import		= "com.anbtech.text.StringProcess"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	/********************************************************************
		 MultipartRequest ���� ������Ű�� ����.
	*********************************************************************/
	int maxUploadSize	= 50*1024*1024; 			// ÷�������� �ִ� ũ�⸦ ����(5M)
	String saveDir = upload_path + "/service";	// ÷�������� ���� ���丮 ����
	MultipartRequest multi = new MultipartRequest(request,saveDir,maxUploadSize);

	String visit_count =	multi.getParameter("visit_count")==null?"1":multi.getParameter("visit_count"); // �湮��

	String query	= "";
	String j		=	multi.getParameter("j"); // ���

	String ah_id	=	multi.getParameter("ah_id")==null?"":multi.getParameter("ah_id"); // �̷�ID
	String ap_id	=	multi.getParameter("ap_id")==null?"":multi.getParameter("ap_id"); // �湮ȸ��ID
	String au_id	=	multi.getParameter("au_id")==null?"":multi.getParameter("au_id"); // �����ID
	String s_day	=	multi.getParameter("s_day"); // �湮����
	String s_ah_id	=	"";


	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String regi_date 	= vans.format(now);

	if(j.equals("a")) { // �ű�
		for(int i=1;i<=Integer.parseInt(visit_count);i++){
			/* ÷������ ó�� */
			String fname	= Hanguel.toHanguel(multi.getFilesystemName("file"+i)); //÷������
			String fsize = "";
			String umask = "";
			String ext = "";
			if(fname != null){
				File upFile = multi.getFile("file"+i);
				fsize = Integer.toString((int)upFile.length());
				ext = fname.substring(fname.indexOf(".")+1,fname.length());		//Ȯ���ڸ�
				umask = System.currentTimeMillis()+"."+ext;
				File myFile = new File(saveDir,umask);
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);
			}
			/* ÷������ ó�� �� */

			ah_id = System.currentTimeMillis() + "_" + i;
			s_ah_id += ah_id + ",";

			query = "INSERT INTO history_table (ah_id,ap_id,ap_name,au_id,at_id,at_name,s_day,s_time,class,subject,content,result,other_customer,file_name,file_size,umask,regi_date) values (";
			query += "'"+ah_id+"',";
			query += "'"+ap_id+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("ap_name"))+"',";
			query += "'"+au_id+"',";
			query += "'"+multi.getParameter("at_id"+i)+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("at_name"+i))+"',";
			query += "'"+s_day+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("s_time"+i))+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("category"+i))+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("subject"+i))+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("content"+i))+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("result"+i))+"',";
			query += "'"+Hanguel.toHanguel(multi.getParameter("other_customer"+i))+"',";
			query += "'"+fname+"',";
			query += "'"+fsize+"',";
			query += "'"+umask+"',";
			query += "'"+regi_date+"')";

			bean.openConnection();	
			bean.executeUpdate(query);
		}
		response.sendRedirect("../approval/module/service_FP_App.jsp?INI=&flag=SERVICE&plid="+s_ah_id);

	} else if (j.equals("u")) { // ����
		/* ÷������ ó�� */
		String fname = Hanguel.toHanguel(multi.getParameter("file_name")); //���� ÷������ �̸�
		String fsize = multi.getParameter("file_size"); //���� ÷������ ũ��
		String umask = multi.getParameter("umask"); //���� ÷������ �����̸�

		String new_file = Hanguel.toHanguel(multi.getFilesystemName("attach_file")); //÷������
		String ext = "";

		if(new_file != null){
			File uploadedFile = new File(saveDir,umask);
			if(uploadedFile.exists()) uploadedFile.delete(); // ����÷������ ����

			File upFile = multi.getFile("attach_file");
			fname = new_file;
			fsize = Integer.toString((int)upFile.length());
			ext = new_file.substring(new_file.indexOf(".")+1,new_file.length());	//Ȯ���ڸ�
			umask = System.currentTimeMillis()+"."+ext;
			File myFile = new File(saveDir,umask);
			if(myFile.exists()) myFile.delete();
			upFile.renameTo(myFile);
		}

		/* ÷������ ó�� �� */

		query = "update history_table set ";
		query += "ap_id='"+ap_id+"',";
		query += "ap_name='"+Hanguel.toHanguel(multi.getParameter("ap_name"))+"',";
		query += "at_id='"+multi.getParameter("at_id1")+"',";
		query += "at_name='"+Hanguel.toHanguel(multi.getParameter("at_name1"))+"',";
		query += "s_day='"+Hanguel.toHanguel(multi.getParameter("s_day"))+"',";
		query += "s_time='"+Hanguel.toHanguel(multi.getParameter("s_time"))+"',";
		query += "class='"+Hanguel.toHanguel(multi.getParameter("category"))+"',";
		query += "subject='"+Hanguel.toHanguel(multi.getParameter("subject"))+"',";
		query += "content='"+Hanguel.toHanguel(multi.getParameter("content"))+"',";
		query += "result='"+Hanguel.toHanguel(multi.getParameter("result"))+"',";
		query += "other_customer='"+Hanguel.toHanguel(multi.getParameter("other_customer"))+"',";
		query += "file_name='"+fname+"',";
		query += "file_size='"+fsize+"',";
		query += "umask='"+umask+"' ";
		query += "where ah_id = '"+ah_id+"'";

		bean.openConnection();	
		bean.executeUpdate(query);

	} else if (j.equals("d")) { // ����
		String umask = multi.getParameter("umask"); //���� ÷������ �����̸�

		if(umask != null){
			File uploadedFile = new File(saveDir,umask);
			if(uploadedFile.exists()) uploadedFile.delete(); // ����÷������ ����
		}

		query = "delete from history_table where ah_id ='"+ah_id+"'";

		bean.openConnection();	
		bean.executeUpdate(query);
		response.sendRedirect("historyl.jsp");
	}
%>

<script language=javascript>
	location.href="historyl.jsp";
</script>