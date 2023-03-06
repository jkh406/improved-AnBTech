<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info = "제품 분류의 추가,수정,삭제를 처리"
	language = "java"
	contentType = "text/html;charset=KSC5601"
	errorPage="../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.date.anbDate,java.text.DecimalFormat,java.text.NumberFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query	= "";
	String j		= request.getParameter("j")==null?"":request.getParameter("j");
	String error	= "no";

	String pid		= request.getParameter("pid") ==""?"":request.getParameter("pid"); 
	String flag		= request.getParameter("flag") == ""?"":request.getParameter("flag");
	String m_code	= request.getParameter("m_code") == ""?"":request.getParameter("m_code");
	String spec		= request.getParameter("spec") == ""?"":Hanguel.toHanguel(request.getParameter("spec"));
	String tag		= request.getParameter("tag") == ""?"":request.getParameter("tag");
	String m_code_val = request.getParameter("m_code_val") == ""?"":request.getParameter("m_code_val");

	com.anbtech.text.Hanguel hanguel = new com.anbtech.text.Hanguel();
	bean.openConnection();	

	if(j.equals("d")) { // 삭제 모드
	  
	    query = "SELECT COUNT(*) FROM mbom_env WHERE m_code LIKE '"+m_code+"%'";
		bean.executeQuery(query);
		if(bean.next()){
		    if(Integer.parseInt(bean.getData(1)) > 1) error = "삭제하고자 하는 설계변경 항목의 하위항목이 포함되어 있어 삭제할 수 없습니다.";
		}
	}

	if(j.equals("a")) { // 새 공정코드 정보 등록
		
		anbDate anbdate = new com.anbtech.date.anbDate();
		pid = anbdate.getID();
		boolean bool = true;
		
		query = "SELECT count(*) FROM mbom_env WHERE m_code like '"+m_code+"' and flag='3'";
		bean.executeQuery(query);
		bean.next();

		if(Integer.parseInt(bean.getData(1))<1) {
			
			query = "INSERT INTO mbom_env VALUES ('"+pid+"', '3','"+m_code+"','"+hanguel.toHanguel(m_code_val)+"','')";
			bean.executeUpdate(query);
			
			pid =  anbdate.getNumID(1);   
			query = "INSERT INTO mbom_env VALUES ('"+pid+"', '4','"+m_code+"01','"+spec+"','"+tag+"')";
			bean.executeUpdate(query);
			
		} else {
			query = "SELECT max(m_code) FROM mbom_env WHERE m_code like '"+m_code+"%' and flag='4'";
			bean.executeQuery(query);		
			bean.next();
			
			if (bean.getData(1)==null){
				query = "INSERT INTO mbom_env VALUES ('"+pid+"', '4','"+m_code+"01','"+spec+"','"+tag+"')";
				bean.executeUpdate(query);
			} else {
				String max_code = (String)bean.getData(1);
				int code_len = max_code.length();
				DecimalFormat fmt = new DecimalFormat("00");
				String code_str = fmt.format(Integer.parseInt(bean.getData(1).substring(code_len-2,code_len))+1);
				m_code = m_code +  code_str;

				query = "INSERT INTO mbom_env VALUES('"+pid+"','4','"+m_code+"','"+spec+"','"+tag+"')";
				bean.executeUpdate(query);
			}
			
		}
	} else if (j.equals("u")) { // 수정모드
		query = "SELECT m_code FROM mbom_env WHERE pid = '"+pid+"'";
		bean.executeQuery(query);
		bean.next();
		m_code = bean.getData("m_code");
        
	    query = "UPDATE mbom_env SET m_code='"+m_code+"',spec='"+spec+"',tag='"+tag+"' WHERE pid = '"+pid+"'";
		bean.executeUpdate(query);
		
	} else if (j.equals("d")) { // 삭제모드
		if(error.equals("no")){
			query = "DELETE FROM mbom_env WHERE pid = '"+pid+"'";
			bean.executeUpdate(query);
			response.sendRedirect("BmItem_list.jsp");
		}else{
			out.println("<script>alert('"+error+"');history.go(-1);</script>");			
		}
	}
%>
<script language=javascript>
//	alert("정상적으로 처리되었습니다");
	opener.location.reload();
	this.close();
</script>