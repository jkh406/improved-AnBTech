<%@ include file= "../../admin/configHead.jsp"%>
<%@ include file= "../../admin/chk/chkDM01.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate,java.text.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String mode		= request.getParameter("mode");
	String no		= request.getParameter("no");
	String doc_no	= request.getParameter("doc_no");		// 문서번호
	String version	= request.getParameter("version");		// 문서버젼
	String user_id	= request.getParameter("user_id");		// 사용자 ID
	String valid_day= request.getParameter("valid_day");	// 유효기간
	com.anbtech.date.anbDate date_str = new com.anbtech.date.anbDate();
	String today	= date_str.getDate();

	bean.openConnection();

	if(mode.equals("d")){
			query = "DELETE authority_doc_table WHERE no="+no;
			bean.executeUpdate(query);

	} else if(mode.equals("i")){
			java.util.StringTokenizer today_token=new java.util.StringTokenizer(today,"-");
		
			int i=0;
			int j=today_token.countTokens();
			String[] token_temp = new String[j];	// 시작날짜 년/월/일 토큰 저장 변수

			while(today_token.hasMoreTokens()){
				token_temp[i]=today_token.nextToken();
				i++;
			}
			
			int year=Integer.parseInt(token_temp[0]);
			int month=Integer.parseInt(token_temp[1]);
			int day=Integer.parseInt(token_temp[2]);

			String valid_date = date_str.getDate(year,month,day,Integer.parseInt(valid_day)); // 유효기간 계산(종료날짜)
			valid_date = valid_date.replace('/','-');
				
			query = "INSERT INTO authority_doc_table (doc_no,version,user_id,valid_day,today,valid_date,type)";
			query = query + " VALUES ('"+doc_no+"','"+version+"','"+user_id+"','"+valid_day+"','"+today+"','"+valid_date+"','1')";

			bean.executeUpdate(query);
	}

	response.sendRedirect("authorityList.jsp");

%>