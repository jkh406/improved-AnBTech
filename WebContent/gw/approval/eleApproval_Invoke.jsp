<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "������ ���ڹ��� �����ϱ�"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="javax.swing.*"
%>
<%@	page import="SessionLib"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.office.OfficeListBean" />
	<jsp:useBean id="app" scope="request" class="com.anbtech.office.OfficefileInsertDB" />
<%!
	//�޽��� ���޺���
	private String Message="";		//�޽��� ���� ����  

%>

<%	
	
	/*********************************************************************
	 	�������� �����ϱ�
	*********************************************************************/
	String DOC_PID = request.getParameter("PID");			//������ȣ
	
	String save_day = "";						//���ں��� (�Ϸ縸)
	String save_state = "";						//���º��� (AMV -> APS)
	if(DOC_PID != null) {
		//������ ���̺� �������� ����
		save_day = "update APP_MASTER set delete_date = '" + bean.getTimeNoformat() + "' where pid='" + DOC_PID + "'";
		save_state = "update APP_MASTER set app_state = 'APS' where pid='" + DOC_PID + "'";
		try { 	
			app.execute(save_day); 
			app.execute(save_state); 
			Message="Invoke"; 
		} catch (Exception e) { 
			Message = "QUERY"; 
		}

		//SAVE ���̺� �������� ����
		save_day = "update APP_SAVE set delete_date = '" + bean.getTimeNoformat() + "' where pid='" + DOC_PID + "'";
		save_state = "update APP_SAVE set app_state = 'APS' where pid='" + DOC_PID + "'";
		try { 	
			app.execute(save_day); 
			app.execute(save_state); 
			Message="Invoke"; 
		} catch (Exception e) { 
			Message = "QUERY"; 
		}	

	}

%>

<script language=javascript>
<!--
opener.opener.parent.menu.location.reload();
opener.close();		//�������� �ݱ�
this.close();
</script>

