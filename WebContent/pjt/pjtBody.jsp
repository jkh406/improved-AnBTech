<%@ include file="../admin/configHead.jsp"%>
<%@ page		
	info= "������Ʈ����"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.sql.*, java.io.*, java.util.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//���Ѿ˾ƺ� �޴���� �����ϱ�
	String[] mgrColumn = {"keyname","owner","div_code"};
	bean.setTable("pjt_grade_mgr");		
	bean.setColumns(mgrColumn);
	String query = "where owner like '%"+login_id+"%' order by keyname ASC";
	bean.setSearchWrite(query);
	bean.init_write();
	String mgr = "";
	while(bean.isAll()) mgr += bean.getData("keyname")+",";
	
%>
<HTML><HEAD><TITLE>������Ʈ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<FRAMESET border=0 frameSpacing=0 frameBorder=0 cols=175,*>
	<FRAMESET border=0 frameSpacing=0 rows=82,*>
		<FRAME name=title-frame marginWidth=0 marginHeight=0 src="pjtTitle.htm" frameBorder=NO scrolling=no>
		<FRAME name=menu marginWidth=0 marginHeight=0 src="pjtMenuLeft.jsp" frameBorder=0 noResize>
	</FRAMESET>
<% if(mgr.indexOf("PJT_PML") != -1) {			//PM�̸� ����������%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/projectNodeAppServlet?mode=PSN_L" frameBorder=0 noResize scrolling=yes>
<% } else if(mgr.indexOf("PJT_MGR") != -1) {	//���� ������ϰ���%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/pjtCodeServlet?mode=PJC_LA" frameBorder=0 noResize scrolling=yes>
<% } else if(mgr.indexOf("PRS_MGR") != -1) {	//���� ���μ�������%>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/prsCodeServlet?mode=PHA_LA" frameBorder=0 noResize scrolling=yes>
<% } else {										//���߸�� ��������� %>
	<FRAME name=view marginWidth=0 marginHeight=0 src="../servlet/projectStaffServlet?mode=PSM_PL" frameBorder=0 noResize scrolling=yes>
<% } %>
</FRAMESET>
</HTML>