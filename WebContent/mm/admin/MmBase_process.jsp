<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "MBOM 환경관리 : 추가,수정,삭제를 처리"
	errorPage	= "../../admin/errorpage.jsp"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String pid		= request.getParameter("pid") ==""?"":request.getParameter("pid"); 
	String mgr_type	= request.getParameter("mgr_type") == ""?"":request.getParameter("mgr_type");
	String mgr_code	= request.getParameter("mgr_code") == ""?"":request.getParameter("mgr_code");
	String mgr_name = request.getParameter("mgr_name") == ""?"":Hanguel.toHanguel(request.getParameter("mgr_name"));
	String mgr_id	= request.getParameter("mgr_id") ==""?"":Hanguel.toHanguel(request.getParameter("mgr_id"));
	String mgr_id_name = request.getParameter("mgr_id_name") ==""?"":Hanguel.toHanguel(request.getParameter("mgr_id_name"));
	mgr_id = mgr_id+"/"+mgr_id_name;	//사번/이름

	String factory_no= request.getParameter("factory_no") == ""?"":request.getParameter("factory_no");

	bean.openConnection();	
	if(j.equals("a")) { // 정보 등록
		// 중복 체크
		anbDate anbdate = new com.anbtech.date.anbDate();
		pid = anbdate.getID();
		boolean bool = true;

		query = "SELECT mgr_id FROM mfg_grade_mgr WHERE mgr_type='"+mgr_type+"' and mgr_code='"+mgr_code+"' ";
		query +="and factory_no='"+factory_no+"'";
		bean.executeQuery(query);
		while(bean.next()){
			String temp = bean.getData("mgr_id");
			if(temp.equals(mgr_id)){
				bool = false;
				out.println("<script>alert('"+mgr_id+" 가 이미 등록되었습니다.');history.go(-1);</script>");
			}
		}
		
	    if (bool) {
			query = "INSERT INTO mfg_grade_mgr (pid,mgr_type,mgr_code,mgr_name,mgr_id,factory_no) VALUES('";
			query += pid+"','"+mgr_type+"','"+mgr_code+"','"+mgr_name+"','"+mgr_id+"','"+factory_no+"')";
			bean.executeUpdate(query);
		}		

	} else if (j.equals("u")) { // 수정모드
        query = "UPDATE mfg_grade_mgr SET mgr_type='"+mgr_type+"',mgr_code='"+mgr_code+"',mgr_id='";
		query += mgr_id+"',mgr_name='"+mgr_name+"',factory_no='"+factory_no+"' WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);
		
		
	} else if (j.equals("d")) { // 삭제모드
		query = "DELETE FROM mfg_grade_mgr WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);

		response.sendRedirect("MmBase_list.jsp");
	}
%>
<script language=javascript>

	opener.location.reload();
	this.close();
</script>