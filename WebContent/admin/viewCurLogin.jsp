<%@ page		
	info= "���� �α��� ����� ����Ʈ ����"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.admin.SessionManager"
%>
<HTML>
<BODY>
<table width="100%" border="0" cellpadding="1" cellspacing="1" bgcolor="#DEE8DB">
<tr><td bgcolor="#DEE8DB" align=center>����α���</td></tr>
<%
	//SessionManager�� �ν��Ͻ��� �޾ƿɴϴ�.
	SessionManager sm = SessionManager.getInstance();
	//���̵� ����� Hashtable�� �޾ƿɴϴ�.
	Hashtable hash = sm.getClient();
	Enumeration em = hash.elements();
	while(em.hasMoreElements()){
%>
<tr>
<td align=center height=15 bgcolor=white>
<!-- ���̵� �ϳ��� �о�ɴϴ�. -->
<font color="blue"><%=em.nextElement()%></font>
</td>
</tr>
<%
	}
%>
</table>
</BODY>
</HTML>