<%@ page		
	info= "현재 로긴한 사용자 리스트 보기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.admin.SessionManager"
%>
<HTML>
<BODY>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#DEE8DB">
<tr><td bgcolor="#DEE8DB" align=center>현재로그인</td></tr>
<%
	//SessionManager의 인스턴스를 받아옵니다.
	SessionManager sm = SessionManager.getInstance();
	//아이디가 저장된 Hashtable을 받아옵니다.
	Hashtable hash = sm.getClient();
	Enumeration em = hash.elements();
	while(em.hasMoreElements()){
%>
<tr>
<td align=center height=15 bgcolor=white>
<!-- 아이디를 하나씩 읽어옵니다. -->
<font color="blue"><%=em.nextElement()%></font>
</td>
</tr>
<%
	}
%>
</table>
</BODY>
</HTML>