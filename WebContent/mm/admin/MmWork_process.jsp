<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "작업장관리 : 추가,수정,삭제를 처리"
	errorPage	= "../../admin/errorpage.jsp"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("000");	//포멧
	
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String pid		= request.getParameter("pid") ==""?"":request.getParameter("pid"); 
	String work_type= request.getParameter("work_type") == ""?"":request.getParameter("work_type");
	String work_no	= request.getParameter("work_no") == ""?"":request.getParameter("work_no");
	String work_name= request.getParameter("work_name") == ""?"":Hanguel.toHanguel(request.getParameter("work_name"));
	String mgr_id	= request.getParameter("mgr_id") == ""?"":request.getParameter("mgr_id");
	String mgr_name = request.getParameter("mgr_name") == ""?"":Hanguel.toHanguel(request.getParameter("mgr_name"));
	String factory_no= request.getParameter("factory_no") == ""?"":request.getParameter("factory_no");
	String factory_name = request.getParameter("factory_name") == ""?"":Hanguel.toHanguel(request.getParameter("factory_name"));

	bean.openConnection();	
	if(j.equals("a")) { // 정보 등록
		// 중복 체크
		anbDate anbdate = new com.anbtech.date.anbDate();
		pid = anbdate.getID();
		boolean bool = true;

		query = "SELECT work_name FROM mfg_work WHERE work_type='"+work_type+"' and work_name='"+work_name+"' ";
		bean.executeQuery(query);
		while(bean.next()){
			String temp = bean.getData("work_name");
			if(temp.equals(work_name)){
				bool = false;
				out.println("<script>alert('"+work_name+" 가 이미 등록되었습니다.');history.go(-1);</script>");
			}
		}
		// 작업장코드 자동 발생하기
		if(work_type.equals("1")) work_no = "M";
		else if(work_type.equals("2")) work_no = "F";
		else work_no = "M";

		query = "SELECT work_no FROM mfg_work WHERE work_type='"+work_type+"' and factory_no='"+factory_no+"'";
		query += " order by work_no desc";
		bean.executeQuery(query);
		if(bean.next()) {
			String data = bean.getData("work_no");

			int len = data.length();
			String serial = data.substring(len-3,len);
			serial = nfm.toDigits(Integer.parseInt(serial)+1);
			work_no = work_no+serial;
		} else work_no = work_no + "001"; 

		//등록하기
	    if (bool) {
			query = "INSERT INTO mfg_work (pid,work_type,work_no,work_name,mgr_id,mgr_name,factory_no,factory_name) VALUES('";
			query += pid+"','"+work_type+"','"+work_no+"','"+work_name+"','"+mgr_id+"','";
			query += mgr_name+"','"+factory_no+"','"+factory_name+"')";
			bean.executeUpdate(query);
		}		

	} else if (j.equals("u")) { // 수정모드
        query = "UPDATE mfg_work SET mgr_id='"+mgr_id+"',mgr_name='"+mgr_name+"',work_name='";
		query += work_name+"',work_no='"+work_no+"' WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);
		
		
	} else if (j.equals("d")) { // 삭제모드
		query = "DELETE FROM mfg_work WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);

		response.sendRedirect("MmWork_list.jsp");
	}
%>
<script language=javascript>

	opener.location.reload();
	this.close();
</script>