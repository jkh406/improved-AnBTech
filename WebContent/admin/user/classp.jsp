<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.text.DecimalFormat"
	contentType	= "text/html;charset=KSC5601"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String ac_id	= request.getParameter("ac_id")==null?"":request.getParameter("ac_id");
	String ac_name	= request.getParameter("ac_name") == ""?"":Hanguel.toHanguel(request.getParameter("ac_name"));
	String ac_code	= request.getParameter("ac_code") == ""?"":request.getParameter("ac_code");
	String chief_id	= request.getParameter("chief_id") == ""?"":request.getParameter("chief_id");
	String ac_level	= request.getParameter("ac_level") == ""?"":request.getParameter("ac_level");
	String code		= request.getParameter("code") == ""?"":request.getParameter("code");
	String isuse	= request.getParameter("isuse")== null?"":request.getParameter("isuse");
		
	String error = "no";
	
	bean.openConnection();

	///////////////
	// 회사입력
	///////////////
	if(j.equals("")) {
		//회사코드 중복여부 검사
		query = "SELECT COUNT(*) FROM class_table WHERE ac_code = '" + ac_code + "' and ac_level='0' and isuse='y'";
		bean.executeQuery(query);
		bean.next();
		String sel_count = bean.getData(1);
		
		//중복코드일 경우 메시지 출력
		if(!sel_count.equals("0")) {
%>
				<script>alert('회사코드가 중복됩니다. 회사 코드를 변경후 등록하십시요.'); history.go(-1);</script>
<%
		//중복코드가 아닐경우 디비 입력처리
		}else{
			query = "INSERT INTO class_table(ac_name,ac_code,ac_level,ac_ancestor,code,chief_id,isuse) VALUES('" + ac_name + "','" + ac_code + "', '0', '0','"+ac_code+"','','y')";
			bean.executeUpdate(query);
			response.sendRedirect("classl.jsp");
		}
	}

	///////////////////
	// 부서 정보 수정 처리
	///////////////////
	else if(j.equals("u")) {
		if(isuse.equals("n")) { // 부서 사용 비활성화 처리
			query = " select count(au_id) from user_table where ac_id ='" + ac_id +"'";
			bean.executeQuery(query);
			bean.next();

			//현재 비활성화처리시키고자하는 조직에 구성원이 포함되어 있을 경우 에러 메시지 출력
			if(Integer.parseInt(bean.getData(1)) > 0)  {
				error = "yes";
%>
				<script>alert("이 부서에 포함된 구성원이 있어 부서를 비활성화시킬 수 없습니다.");history.go(-2);</script>
<%
			}

			query = " select count(ac_id) from class_table where ac_ancestor ='" + ac_id +"' and isuse='y'";
			bean.executeQuery(query);
			bean.next();

			//현재 비활성화처리시키고자하는 조직에 하위조직이 있을 경우 에러 메시지 출력
			if(Integer.parseInt(bean.getData(1)) > 0) {
				error = "yes";
%>
				<script>alert("이 부서의 하위 조직이 있어 부서를 비활성화시킬 수 없습니다.");history.go(-2);</script>
<%
			}
			
			//에러가 없을 경우에 부서 비활성화 처리
			if(error.equals("no")){
				query = "update class_table set isuse='n' where ac_id='"+ac_id+"'";
				bean.executeUpdate(query);
				response.sendRedirect("classl.jsp");
			}

		} else { // 부서 사용 비활성화 처리외의 정보를 변경할 경우
			// class_table 부서 정보 update
			query = "update class_table set ac_name='"+ac_name+"', ac_code='"+ac_code+"', chief_id = '" + chief_id + "',isuse='"+isuse+"' where ac_id = '"+ac_id+"'";
			bean.executeUpdate(query);
			
			// user_table 부서원 정보 update
			query = "update user_table set ac_code='"+ac_code+"' where code='"+code+"'";
			bean.executeUpdate(query);
			out.print("<script>location.href='classl.jsp';</script>");
			response.sendRedirect("classl.jsp");		
		}
	}

	///////////////////
	// 하위분류 추가
	///////////////////
	else if(j.equals("a")) {	
		String code_str = "";	

		query = "SELECT MAX(code) FROM class_table WHERE ac_ancestor = '"+ac_id+"' and isuse='y'";
		bean.executeQuery(query);
		bean.next();
		
		if(bean.getData(1) == null){
			code_str = code + "01";
		} else {
			int code_len = bean.getData(1).length();
			DecimalFormat fmt = new DecimalFormat("00");
			code_str = fmt.format(Integer.parseInt(bean.getData(1).substring(code_len-2,code_len))+1);
			code_str = bean.getData(1).substring(0,code_len-2)+code_str;
		}

		query = " insert into class_table(ac_name,ac_code,ac_level,ac_ancestor,code,chief_id,isuse) values ('"+ac_name+"','"+ac_code+"','"+ac_level+"','"+ac_id+"','"+code_str+"','"+chief_id+"','y')";
			
		bean.executeUpdate(query);
		response.sendRedirect("classl.jsp");
	}

	///////////////
	// 삭제처리
	///////////////
	else if (j.equals("d")) { 

		query = "select count(au_id) 'no' from user_table where ac_id ='" + ac_id +"'";
		bean.executeQuery(query);
		
		while(bean.next()){
		if(Integer.parseInt(bean.getData("no")) > 0) error = "이 분류에 포함된 사원이 있어 삭제할 수 없습니다.";}

	    query = " select count(ac_id) 'no' from class_table where ac_ancestor ='" + ac_id +"'";
		bean.executeQuery(query);
		while(bean.next()){
		if(Integer.parseInt(bean.getData("no")) > 0) error = "이 분류의 하위 분류가 있어 삭제할 수 없습니다.";}

		if(error.equals("no")){
			query = " DELETE FROM class_table WHERE ac_id = '"+ac_id+"'";
			bean.executeUpdate(query);

			response.sendRedirect("classl.jsp");
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}

	///////////////////
	// 회사 정보 수정 처리
	///////////////////
	else if(ac_level.equals("0") && j.equals("u")){
		if(isuse.equals("n")) { // 회사조직 사용여부 변경시
			
			// 회사(최상위) 정보 비활성화 처리시 하위 조직에 구성원이 포함되어 있는지 확인
			query = " select count(au_id) from user_table where code like '"+ac_code+"%'";
			bean.executeQuery(query);
			bean.next();

			//현재 비활성화처리시키고자하는 조직에 구성원이 포함되어 있을 경우 에러 메시지 출력
			if(Integer.parseInt(bean.getData(1)) > 0)  {
				error = "yes";
%>
				<script>alert("하위에 포함된 구성원이 있어 비활성화시킬 수 없습니다.");history.go(-2);</script>
<%
			} else {
				query = "update class_table set isuse='n' where code like '"+code+"%'";
				bean.executeUpdate(query);
				response.sendRedirect("classl.jsp");
			}
		} else { // 회사명만 변경했을경우
			query ="update class_table set ac_name='"+ac_name+"',isuse = '" + isuse + "' WHERE ac_id='"+ac_id+"'";			
			bean.executeUpdate(query);
			response.sendRedirect("classl.jsp");
		}	
	}
%>