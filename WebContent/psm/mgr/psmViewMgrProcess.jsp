<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제조회권한관리"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	초기화 정보
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	내부데이터 받기
	*********************************************************************/
	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String pjt_type = Hanguel.toHanguel(request.getParameter("pjt_type"));
	if(pjt_type == null) pjt_type="A"; 
	String pjt_grade = Hanguel.toHanguel(request.getParameter("pjt_grade"));
	if(pjt_grade == null) pjt_grade=""; 
	String user_id = Hanguel.toHanguel(request.getParameter("user_id"));
	if(user_id == null) user_id=""; else user_id = user_id.toUpperCase();
	String user_name = Hanguel.toHanguel(request.getParameter("user_name"));
	if(user_name == null) user_name=""; 
	String div_code = Hanguel.toHanguel(request.getParameter("div_code"));
	if(div_code == null) div_code=""; 
	String div_name = Hanguel.toHanguel(request.getParameter("div_name"));
	if(div_name == null) div_name=""; 
	String psm_code = Hanguel.toHanguel(request.getParameter("psm_code"));
	if(psm_code == null) psm_code=""; 
	String psm_korea = Hanguel.toHanguel(request.getParameter("psm_korea"));
	if(psm_korea == null) psm_korea=""; 

	//사업부 코드 및 사업부 명 구하기
	if(user_id.length() != 0) {
		query = "select b.ac_code,b.ac_name from user_table a,class_table b ";
		query += "where a.id='"+user_id+"' and a.ac_id=b.ac_id";
		bean.executeQuery(query);
		
		if(bean.next()) {
			div_code = bean.getData("ac_code");
			div_name = bean.getData("ac_name");
		}
	}


	/*********************************************************************
	 	등록하기
	*********************************************************************/
	if(mode.equals("ADD")) {
		//등록된 정보인지 판단하기 
		String e_user_id = "",e_pjt_type="",e_psm_code="";
		query = "select * from psm_view_mgr where user_id='"+user_id+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			e_pjt_type = bean.getData("pjt_type");
			e_user_id = bean.getData("user_id");
			e_psm_code = bean.getData("psm_code");
		}

		//PJT_TYPE:A이면 전과제 조회가능으로 등록할 필요없음
		if(e_pjt_type.equals("A")) { 
			msg = "전과제 조회가능 등급으로 등록된 임직원입니다."; 
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");
		} else if(e_user_id.equals(user_id) && e_psm_code.equals(psm_code)) { 
			msg = "해당과제 조회가능으로 등록된 임직원입니다."; 
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");
		} else {
			query = "insert into psm_view_mgr(pid,pjt_type,pjt_grade,user_id,user_name,div_code,";
			query += "div_name,psm_code,psm_korea) values('";
			query += anbdt.getID()+"','"+pjt_type+"','"+pjt_grade+"','"+user_id+"','";
			query += user_name+"','"+div_code+"','"+div_name+"','"+psm_code+"','"+psm_korea+"')";
			bean.execute(query);
			pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name=psm_code=psm_korea="";
			response.sendRedirect("psmViewMgrList.jsp");
		}

	
	}
	/*********************************************************************
	 	수정하기
	*********************************************************************/
	if(mode.equals("MOD")) {
		if(pjt_type.equals("A")) psm_code=psm_korea="";	//전과제조회등급
		query = "update psm_view_mgr set pjt_type='"+pjt_type+"',pjt_grade='"+pjt_grade+"',";
		query += "user_id='"+user_id+"',user_name='"+user_name+"',div_code='"+div_code+"',";
		query += "psm_code='"+psm_code+"',psm_korea='"+psm_korea+"'";
		query += " where pid='"+pid+"'";
		bean.execute(query);

		pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name=psm_code=psm_korea="";
		response.sendRedirect("psmViewMgrList.jsp");
	}
	/*********************************************************************
	 	삭제하기
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_view_mgr where pid='"+pid+"'";
		bean.execute(query);

		pid=pjt_type=pjt_grade=user_id=user_name=div_code=div_name="";
		response.sendRedirect("psmViewMgrList.jsp");
	}
	
%>
