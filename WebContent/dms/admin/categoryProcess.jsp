<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkDM01.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=KSC5601"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.text.DecimalFormat"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");  // 삽입/수정/삭제 모드(j 값)
	String c_id		= request.getParameter("c_id")==null?"":request.getParameter("c_id"); // 카테고리 id
	String c_name	= request.getParameter("c_name") == ""?"":Hanguel.toHanguel(request.getParameter("c_name")); // 카테고리명
	String c_code	= request.getParameter("c_code") == ""?"":request.getParameter("c_code");   // 대표문자
	String c_level	= request.getParameter("c_level") == ""?"":request.getParameter("c_level"); // 카테고리 level
	String error	= "no"; 
	
	String enable_rev = request.getParameter("revision"); // revision 관리
	String enable_pjt = request.getParameter("project");  // project 관리
	String enable_model = request.getParameter("model");  // 형상(모델)관리
	String enable_eco = request.getParameter("eco");      // eco 관리
	String enable_app = request.getParameter("eapp");	  // 결재 관리

	String security_level = Hanguel.toHanguel(request.getParameter("security"));
	String save_period    = Hanguel.toHanguel(request.getParameter("save"));
	String tablename      = request.getParameter("tablename");
	String loan_period      = request.getParameter("loan_period");

	bean.openConnection();	

	
	if(j.equals("d")) { // 삭제 모드 
	
		query = " select count(c_id) 'no' from category_data where c_id like'" + c_id+"%'";
		bean.executeQuery(query);
		
		while(bean.next())
		{
		    if(Integer.parseInt(bean.getData("no")) > 1) error = "이 분류에 포함된 카테고리가 있어 삭제할 수 없습니다.";
		}

	    query = " select count(c_id) 'no' from category_data where c_ancestor ='" + c_id +"'";
		bean.executeQuery(query);

		while(bean.next())
		{
		    if(Integer.parseInt(bean.getData("no")) > 0) error = "이 분류에 포함된 카테고리가 있어 삭제할 수 없습니다.";
		}
	}


	if (j.equals("a")) {  // 하위카테고리 추가

		//동일 카테고리 분류가 있는지 확인하여 있으면 c_id 값을 가져온다.
		query = "select max(c_id) from category_data where c_ancestor = '"+c_id+"'";
		bean.executeQuery(query);
		bean.next();
		String now_code_str = bean.getData(1);

		String code_str = "";
		if( now_code_str == null ) {  
			code_str = c_id + "01";
		} else {

			int code_len = now_code_str.length();
			String sub_code = now_code_str.substring(code_len-2,code_len);
			int next_sub_code = Integer.parseInt(sub_code) + 1;
			DecimalFormat fmt = new DecimalFormat("00");
			code_str = c_id + fmt.format(next_sub_code);

		}		


	    query = " insert into category_data(c_id,c_level,c_ancestor,c_name,c_code,tablename,enable_rev,enable_pjt,enable_model,enable_eco,enable_app,security_level,save_period,p_id,loan_period) values('"+code_str+"','"+c_level+"','"+c_id+"','"+c_name+"','"+c_code+"','"+tablename+"','"+enable_rev+"','"+enable_pjt+"','"+enable_model+"','"+enable_eco+"','"+enable_app+"','"+security_level+"','"+save_period+"','0','"+loan_period+"')";
		bean.executeUpdate(query);
		response.sendRedirect("categoryList.jsp");

	} else if (j.equals("u")) { // 수정모드

		query = " update category_data set c_name='"+c_name+"', c_code= '"+c_code+"', enable_rev='"+enable_rev+"', enable_pjt='"+enable_pjt+"', enable_model='"+enable_model+"', enable_eco='"+enable_eco+"', enable_app='"+enable_app+"', security_level='"+security_level+"',save_period='"+save_period+"',tablename='"+tablename+"',loan_period='"+loan_period+"' where c_id='"+c_id+"'";
		bean.executeUpdate(query);
		response.sendRedirect("categoryList.jsp");


	} else if (j.equals("d")) { // 삭제모드
		if(error.equals("no")){
			query = " delete from category_data where c_id = '"+c_id+"'";
			bean.executeUpdate(query);
			response.sendRedirect("categoryList.jsp");
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}

	}
	
%>

