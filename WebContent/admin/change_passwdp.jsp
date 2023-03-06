<%@ include file= "configHead.jsp"%>
<%@ page
	language = "java"
	info = "비빌번호 변경"		
	contentType = "text/html; charset=euc-kr"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%

	/********************************************************************
		 Get the parameter
	*********************************************************************/
	String id			= request.getParameter("id");
	String old_passwd	= request.getParameter("old_passwd");
	String passwd		= request.getParameter("passwd");

	bean.openConnection();
	String sql="select count(passwd) from user_table where id='"+id+"'";
	bean.executeQuery(sql);
	bean.next();

	if (bean.getData(1).equals("0")){ // 입력된 ID가 DB에 없을 때
		out.println("<script>");
		out.println("window.alert('해당 사용자는 존재하지 않습니다.')");
		out.println("self.close();");
		out.println("</script>");
	}else{
		sql="select count(*) from user_table where id='"+id+"' and passwd = '"+old_passwd+"'";
		bean.executeQuery(sql);
		bean.next();

		if(bean.getData(1).equals("0")){ // 비밀번호가 일치하지 않을 때
			out.println("<script>");
			out.println("alert('이전 비밀번호가 일치하지 않습니다.')");
			out.println("self.close();");
			out.println("</script>");
		}else{	// 비밀번호가 일치할 경우 수정한다.
			sql = "update user_table set passwd = '"+passwd+"' where id ='"+id+"'";
			bean.executeUpdate(sql);
			out.println("<script>");
			out.println("window.alert('비밀번호를 변경하였습니다. 다음 로그인시에는 새로운 비밀번호로 로그인하십시요.')");
			out.println("self.close();");
			out.println("</script>");
		}
	}

%>