<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.text.DecimalFormat"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		=	request.getParameter("j");
	String ac_id	=	request.getParameter("ac_id");
	String au_id	=	request.getParameter("au_id");
	String rank		=	Hanguel.toHanguel(request.getParameter("rank"));
	String name		=	Hanguel.toHanguel(request.getParameter("name"));
	String id		=	Hanguel.toHanguel(request.getParameter("id"));
	String passwd	=	Hanguel.toHanguel(request.getParameter("passwd"));
	String email	=	Hanguel.toHanguel(request.getParameter("email"));
	String office_tel	=	Hanguel.toHanguel(request.getParameter("office_tel"));
	String hand_tel	=	Hanguel.toHanguel(request.getParameter("hand_tel"));
	String fax		=	Hanguel.toHanguel(request.getParameter("fax"));
	String main_job	=	Hanguel.toHanguel(request.getParameter("main_job"));
	String address	=	Hanguel.toHanguel(request.getParameter("address"));
	String post_no	=	Hanguel.toHanguel(request.getParameter("post_no"));
	String home_tel	=	Hanguel.toHanguel(request.getParameter("home_tel"));
	String enter_day	=	Hanguel.toHanguel(request.getParameter("enter_day"));
	String passwd_change = request.getParameter("passwd_change");
	String str = Hanguel.toHanguel(request.getParameter("authorityString"));
	String ac_code = "";
	String code    = "";

	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String regi_date 	= vans.format(now);

    bean.openConnection();
		
	query = "SELECT ac_code, code FROM class_table WHERE ac_id='"+Integer.parseInt(ac_id)+"'";
	bean.executeQuery(query);
	bean.next();
	ac_code	= bean.getData("ac_code");
	code	= bean.getData("code");
	
	if(j.equals("a")) { // 사용자 추가
		
		// ID 중복체크
		query = " SELECT count(*) FROM user_table WHERE id='"+id+"'";
		bean.executeQuery(query);
		bean.next();
			
		if(Integer.parseInt(bean.getData(1)) > 0) {
%>
			<script language='javascript'> alert('이미 등록된 사번입니다.');history.go(-1);</script>
<%
		}else{

			query = " INSERT into user_table (ac_id,rank,name,id,passwd,email,office_tel,hand_tel,fax,main_job,address,post_no,home_tel,enter_day,regi_date,access_code,ac_code,code) values('"+ac_id+"','"+rank+"','"+name+"','"+id+"','"+passwd+"','"+email+"','"+office_tel+"','"+hand_tel+"','"+fax+"','"+main_job+"','"+address+"','"+post_no+"','"+home_tel+"','"+enter_day+"','"+regi_date+"','"+str+"','"+ac_code+"','"+code+"')";
			bean.executeUpdate(query);
			response.sendRedirect("userl.jsp?ac_id="+ac_id);	
		}
	} else if (j.equals("u")) { // 수정모드
					
		query = "update user_table set ";
		query += "ac_id='"+ac_id+"',";
		query += "rank='"+rank+"',";
		query += "name='"+name+"',";
		query += "id='"+id+"',";
		if(passwd_change.equals("y"))
			query += "passwd= '"+passwd+"',";
		query += "email='"+email+"',";
		query += "office_tel='"+office_tel+"',";
		query += "hand_tel='"+hand_tel+"',";
		query += "fax='"+fax+"',";
		query += "main_job='"+main_job+"',";
		query += "address='"+address+"',";
		query += "post_no='"+post_no+"',";
		query += "home_tel='"+home_tel+"',";
		query += "enter_day='"+enter_day+"', ";
		query += "ac_code='"+ac_code+"', ";
		query += "code='"+code+"' ";
		query += "where au_id = '"+au_id+"'";

		bean.executeUpdate(query);
		response.sendRedirect("userl.jsp?ac_id="+ac_id);	

	} else if (j.equals("d")) { // 삭제모드
		query = "delete from user_table where au_id ='"+au_id+"'";
		bean.executeUpdate(query);
		response.sendRedirect("userl.jsp?ac_id="+ac_id);	
	}

	
%>
