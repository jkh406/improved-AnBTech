<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String query = "";
	String code_b = request.getParameter("code_b")==null?"":request.getParameter("code_b");
	String code_m = request.getParameter("code_m")==null?"":request.getParameter("code_m");
	String code_s = request.getParameter("code_s")==null?"":request.getParameter("code_s");
	String item_code = request.getParameter("item_code");

	bean.openConnection();
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM ACTION="modifyItem.jsp" METHOD="post" style="margin:0">
<table cellspacing=0 cellpadding=2 width="100%" border=0>
   <tbody>
	 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	 <tr>
		 <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">품목분류</td>
		 <td width="87%" height="25" class="bg_04">
			<%
				//대분류 선택
				query = "SELECT * FROM item_class WHERE item_level = '1' order by mid asc";
				bean.executeQuery(query);
			%>
					<select name="code_b" onChange="javascript:changeClassB();">
						<option value="">대분류 선택</option>
			<%		while(bean.next()){	%>
						<option value="<%=bean.getData("mid")%>">[<%=bean.getData("item_code")%>]<%=bean.getData("item_name")%></option>
			<%		}	%>
					</select>
			<%	if(!code_b.equals("")){	%>
					<script language='javascript'>
						document.forms[0].code_b.value = '<%=code_b%>';
					</script>
			<%	}
				//중분류 선택
				query = "SELECT * FROM item_class WHERE item_level = '2' and item_ancestor = '"+code_b+"' order by mid asc";
				bean.executeQuery(query);
			%>
					<select name="code_m" onChange="javascript:changeClassM();">
						<option value="">중분류 선택</option>
			<%		while(bean.next()){	%>
						<option value="<%=bean.getData("mid")%>">[<%=bean.getData("item_code")%>]<%=bean.getData("item_name")%></option>
			<%		}	%>
					</select>

			<%		if(!code_m.equals("")){	%>
						<script language='javascript'>
							document.forms[0].code_m.value = '<%=code_m%>';
						</script>
			<%		}

				//소분류 선택
				query = "SELECT * FROM item_class WHERE item_level = '3' and item_ancestor = '"+code_m+"' order by mid asc";
				bean.executeQuery(query);
			%>
					<select name="code_s" onChange="javascript:changeClassS();">
						<option value="">소분류 선택</option>
			<%		while(bean.next()){	%>
						<option value="<%=bean.getData("item_code")%>">[<%=bean.getData("item_code")%>]<%=bean.getData("item_name")%></option>
			<%		}	%>
					</select>

			<%		if(!code_s.equals("")){	%>
						<script language='javascript'>
							document.forms[0].code_s.value = '<%=code_s%>';
						</script>
			<%		}	%>


		 </td></tr>
<!--     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>-->
     <tr><td height=10 colspan="4"></td></tr></tbody></table>
</FORM>
</body>
</html>

<script language="Javascript">

function changeClassB(){ 
	var f = document.forms[0];
	var code_b = f.code_b.value;

	location.href="selectItem.jsp?code_b="+code_b;
}

function changeClassM(){ 
	var f = document.forms[0];
	var code_b = f.code_b.value;

	var len = f.code_m.length;
	var code_m = "";
	var item_code = "";
	for(i=0;i<len;i++){
		if(f.code_m.options[i].selected){
		    code_m = f.code_m.options[i].value;
//			var fromField = f.code_m.options[i].text.split("]");
			item_code = f.code_m.options[i].text.substring(1,f.code_m.options[i].text.indexOf("]"));
		}
	}
	parent.view_code.location.href="modifyItem.jsp?item_code="+item_code;
	location.href="selectItem.jsp?code_b="+code_b+"&code_m="+code_m+"&item_code="+item_code;
}

function changeClassS(){ 
	var f = document.forms[0];
	var code_b = f.code_b.value;
	var code_m = f.code_m.value;
	var code_s = f.code_s.value;
	parent.view_code.location.href="modifyItem.jsp?item_code=<%=item_code%>&code_s="+code_s;
}
</script>