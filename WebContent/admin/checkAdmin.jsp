<%!
	public boolean checkAdmin(String id, String password, HttpSession session, HttpServletRequest request){
		try {
			// 세션값을 넣어야 할 필요성이 있다.
			session = request.getSession(true);
			
			String sessionId = (String) session.getAttribute("ANBTech.comAdminId");
			if (sessionId == null) sessionId = "";
			
			if (sessionId.length()<1){

				String admin_user = com.anbtech.admin.db.ServerConfig.getConf("admin_user");
				String admin_password = com.anbtech.admin.db.ServerConfig.getConf("admin_password");
				
				if (id.equals(admin_user) && password.equals(admin_password)){
					synchronized(session) {
						session.setAttribute("ANBTech.comAdminId", id);
					}
					return true;
				}else {
					return false;
				}
			}
				return true;
		}catch (Exception e){
			System.out.println(e.toString());
			return false;
		}
	}
%>

<%	

	session = request.getSession(true);
	String id_a = request.getParameter("id");
	String password_a = request.getParameter("password");

	if (id_a == null) id_a = "";
	if (password_a == null) password_a = "";

	 //관리자 체크
	if (checkAdmin(id_a, password_a, session, request) == false){
		response.sendRedirect("index.htm");
		return;
	}
	
%>	