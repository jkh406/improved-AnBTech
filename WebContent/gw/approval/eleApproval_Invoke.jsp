<%@ include file="../../admin/configHead.jsp"%>
<%@ 	page		
	info= "삭제한 전자문서 복원하기"		
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
	//메시지 전달변수
	private String Message="";		//메시지 전달 변수  

%>

<%	
	
	/*********************************************************************
	 	삭제문서 복원하기
	*********************************************************************/
	String DOC_PID = request.getParameter("PID");			//문서번호
	
	String save_day = "";						//날자복원 (하루만)
	String save_state = "";						//상태복원 (AMV -> APS)
	if(DOC_PID != null) {
		//마스터 테이블 삭제문서 복원
		save_day = "update APP_MASTER set delete_date = '" + bean.getTimeNoformat() + "' where pid='" + DOC_PID + "'";
		save_state = "update APP_MASTER set app_state = 'APS' where pid='" + DOC_PID + "'";
		try { 	
			app.execute(save_day); 
			app.execute(save_state); 
			Message="Invoke"; 
		} catch (Exception e) { 
			Message = "QUERY"; 
		}

		//SAVE 테이블 삭제문서 복원
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
opener.close();		//모윈도우 닫기
this.close();
</script>

