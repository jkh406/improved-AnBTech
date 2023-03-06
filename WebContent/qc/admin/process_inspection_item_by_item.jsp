<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query					= "";
	String mid						= request.getParameter("mid");
	String mode						= request.getParameter("mode");

	String factory_code				= request.getParameter("factory_code");
	String item_code				= request.getParameter("item_code");
	String inspection_code			= request.getParameter("inspection_code");

	String factory_name				= Hanguel.toHanguel(request.getParameter("factory_name"));
	String item_name				= Hanguel.toHanguel(request.getParameter("item_name"));
	String item_desc				= Hanguel.toHanguel(request.getParameter("item_desc"));
	String inspection_class_code	= request.getParameter("inspection_class_code");
	String inspection_class_name	= Hanguel.toHanguel(request.getParameter("inspection_class_name"));
	String inspection_name			= Hanguel.toHanguel(request.getParameter("inspection_name"));
	String inspection_result_type	= Hanguel.toHanguel(request.getParameter("inspection_result_type"));
	String inspection_order			= request.getParameter("inspection_order");
	String inspection_type_code		= request.getParameter("inspection_type_code");
	String inspection_type_name		= Hanguel.toHanguel(request.getParameter("inspection_type_name"));
	String inspection_grade			= Hanguel.toHanguel(request.getParameter("inspection_grade"));
	String low_standard				= Hanguel.toHanguel(request.getParameter("low_standard"));
	String upper_standard			= Hanguel.toHanguel(request.getParameter("upper_standard"));

    bean.openConnection();
		
	if(mode.equals("add")) { // 추가모드
		//검사항목 중복체크
		query = "SELECT COUNT(*) FROM qc_inspection_item_by_item WHERE factory_code = '" + factory_code + "' AND item_code = '" + item_code + "' AND inspection_code = '" + inspection_code + "'";
		bean.executeQuery(query);
		bean.next();

		if(bean.getData(1).equals("0")){
			query = "INSERT INTO qc_inspection_item_by_item (factory_code,factory_name,item_code,item_name,item_desc,inspection_class_code,inspection_class_name,inspection_code,inspection_name,inspection_result_type,inspection_order,inspection_type_code,inspection_type_name,inspection_grade,low_standard,upper_standard) values('"+factory_code+"','"+factory_name+"','"+item_code+"','"+item_name+"','"+item_desc+"','"+inspection_class_code+"','"+inspection_class_name+"','"+inspection_code+"','"+inspection_name+"','"+inspection_result_type+"','"+inspection_order+"','"+inspection_type_code+"','"+inspection_type_name+"','"+inspection_grade+"','"+low_standard+"','"+upper_standard+"')";
			bean.executeUpdate(query);
			response.sendRedirect("list_inspection_item_by_item.jsp?factory_code="+factory_code+"&item_code="+item_code);
		}else{
%>
			<script>alert("검사항목 <%=inspection_code%> <%=inspection_name%> 는(은) 이미 등록되어 있습니다.");history.go(-1);</script>
<%
			
		}
	} else if (mode.equals("modify")) { //수정모드
		query = "UPDATE qc_inspection_item_by_item SET ";
		query += "inspection_result_type='"+inspection_result_type+"',";
		query += "inspection_order='"+inspection_order+"',";
		query += "inspection_code='"+inspection_code+"',";
		query += "inspection_type_code='"+inspection_type_code+"',";
		query += "inspection_type_name='"+inspection_type_name+"',";
		query += "inspection_grade='"+inspection_grade+"',";
		query += "low_standard='"+low_standard+"',";
		query += "upper_standard='"+upper_standard+"' WHERE ";
		query += "mid='"+mid+"'";
		bean.executeUpdate(query);

		response.sendRedirect("list_inspection_item_by_item.jsp?factory_code="+factory_code+"&item_code="+item_code);
	} else if (mode.equals("delete")) { //삭제모드
		query = "DELETE FROM qc_inspection_item_by_item WHERE mid ='"+mid+"'";
		bean.executeUpdate(query);

		response.sendRedirect("list_inspection_item_by_item.jsp?factory_code="+factory_code+"&item_code="+item_code);
	}
%>
