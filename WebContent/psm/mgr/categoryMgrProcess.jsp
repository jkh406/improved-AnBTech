<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "category ����"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	�ʱ�ȭ ����
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String korea_name = Hanguel.toHanguel(request.getParameter("korea_name"));
	if(korea_name == null) korea_name=""; 
	String english_name = Hanguel.toHanguel(request.getParameter("english_name"));
	if(english_name == null) english_name=""; 
	String key_word = Hanguel.toHanguel(request.getParameter("key_word"));
	if(key_word == null) key_word=""; else key_word = key_word.toUpperCase();
	String comp_no = Hanguel.toHanguel(request.getParameter("comp_no"));
	if(comp_no == null) comp_no=""; 
	String comp_korea = Hanguel.toHanguel(request.getParameter("comp_korea"));
	if(comp_korea == null) comp_korea=""; 
	String comp_english = Hanguel.toHanguel(request.getParameter("comp_english"));
	if(comp_english == null) comp_english="����"; 
	bean.openConnection();	


	/*********************************************************************
	 	����ϱ�
	*********************************************************************/
	if(mode.equals("ADD")) {
		//��ϵ� �������� �Ǵ��ϱ�
		String sts = "";	//����ڹ�ȣ �ߺ��˻�
		query = "select * from psm_category where comp_no='"+comp_no+"'";
		bean.executeQuery(query);
		if(bean.next()) sts = bean.getData("comp_no");
		
		String kname="";	//��ü�� �ߺ��˻� (english_name�� ������ ��츸)
		query = "select * from psm_category where comp_korea='"+comp_korea+"'";
		bean.executeQuery(query);
		if(bean.next()) kname = bean.getData("comp_korea");

		//���ǿ����� ����ϱ�
		if(sts.equals(comp_no)) { 
			msg = "�̹� ��ϵ� ���ü �Դϴ�.";
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");
		} else if(kname.equals(comp_korea) && comp_english.equals("����")) { 
			msg = "��ü���� �����մϴ�."; 
			out.println("<script>alert('"+msg+"');history.go(-1);</script>");
		} else {
			query = "insert into psm_category(pid,korea_name,english_name,key_word,comp_no,comp_korea,";
			query += "comp_english) values('";
			query += anbdt.getID()+"','"+korea_name+"','"+english_name+"','"+key_word+"','";
			query += comp_no+"','"+comp_korea+"','"+comp_english+"')";
			bean.execute(query);
			response.sendRedirect("categoryMgrList.jsp");
		}
		
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update psm_category set korea_name='"+korea_name+"',english_name='"+english_name+"',";
		query += "key_word='"+key_word+"',comp_no='"+comp_no+"',comp_korea='"+comp_korea+"',";
		query += "comp_english='"+comp_english+"' where pid='"+pid+"'";
		bean.execute(query);

		pid=korea_name=english_name=key_word=comp_no=comp_korea="";
		comp_english="����";
		
		response.sendRedirect("categoryMgrList.jsp");
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_category where pid='"+pid+"'";
		bean.execute(query);

		pid=korea_name=english_name=key_word=comp_no=comp_korea="";
		comp_english="����";
		
		response.sendRedirect("categoryMgrList.jsp");
	}
	
%>

