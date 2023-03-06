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
		 MultipartRequest 빈을 생성시키기 위함.
	*********************************************************************/
	int maxUploadSize	= 50*1024*1024; 			// 첨부파일의 최대 크기를 지정(5M)
	String saveDir = upload_path + "/service";	// 첨부파일의 저장 디렉토리 지정
	MultipartRequest multi = new MultipartRequest(request,saveDir,maxUploadSize);

	String visit_count =	multi.getParameter("visit_count")==null?"1":multi.getParameter("visit_count"); // 방문수

	String query	= "";
	String j		=	multi.getParameter("j"); // 모드

	String ah_id	=	multi.getParameter("ah_id")==null?"":multi.getParameter("ah_id"); // 이력ID
	String ap_id	=	multi.getParameter("ap_id")==null?"":multi.getParameter("ap_id"); // 방문회사ID
	String au_id	=	multi.getParameter("au_id")==null?"":multi.getParameter("au_id"); // 등록자ID
	String s_day	=	multi.getParameter("s_day"); // 방문일자
	String s_ah_id	=	"";


	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String regi_date 	= vans.format(now);

	if(j.equals("a")) { // 신규
		for(int i=1;i<=Integer.parseInt(visit_count);i++){
			/* 첨부파일 처리 */
			String fname	= Hanguel.toHanguel(multi.getFilesystemName("file"+i)); //첨부파일
			String fsize = "";
			String umask = "";
			String ext = "";
			if(fname != null){
				File upFile = multi.getFile("file"+i);
				fsize = Integer.toString((int)upFile.length());
				ext = fname.substring(fname.indexOf(".")+1,fname.length());		//확장자명
				umask = System.currentTimeMillis()+"."+ext;
				File myFile = new File(saveDir,umask);
				if(myFile.exists()) myFile.delete();
				upFile.renameTo(myFile);
			}
			/* 첨부파일 처리 끝 */

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

	} else if (j.equals("u")) { // 수정
		/* 첨부파일 처리 */
		String fname = Hanguel.toHanguel(multi.getParameter("file_name")); //기존 첨부파일 이름
		String fsize = multi.getParameter("file_size"); //기존 첨부파일 크기
		String umask = multi.getParameter("umask"); //기존 첨부파일 저장이름

		String new_file = Hanguel.toHanguel(multi.getFilesystemName("attach_file")); //첨부파일
		String ext = "";

		if(new_file != null){
			File uploadedFile = new File(saveDir,umask);
			if(uploadedFile.exists()) uploadedFile.delete(); // 기존첨부파일 삭제

			File upFile = multi.getFile("attach_file");
			fname = new_file;
			fsize = Integer.toString((int)upFile.length());
			ext = new_file.substring(new_file.indexOf(".")+1,new_file.length());	//확장자명
			umask = System.currentTimeMillis()+"."+ext;
			File myFile = new File(saveDir,umask);
			if(myFile.exists()) myFile.delete();
			upFile.renameTo(myFile);
		}

		/* 첨부파일 처리 끝 */

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

	} else if (j.equals("d")) { // 삭제
		String umask = multi.getParameter("umask"); //기존 첨부파일 저장이름

		if(umask != null){
			File uploadedFile = new File(saveDir,umask);
			if(uploadedFile.exists()) uploadedFile.delete(); // 기존첨부파일 삭제
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