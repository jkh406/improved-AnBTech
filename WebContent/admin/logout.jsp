<%@ page		
	info= "로그아웃"		
	contentType = "text/html; charset=euc-kr" 		
%>

<% 
//	session.removeAttribute("curLogin");
	session.invalidate(); 
%>

<script>
	top.location.href = "../index.jsp";
</script>